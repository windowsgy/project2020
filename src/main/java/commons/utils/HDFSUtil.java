package commons.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * HDFS 工具
 * Created by jlgaoyuan on 2017/8/22.
 */
public class HDFSUtil implements FileSystemAll,Serializable {

    transient private Configuration conf;

    public HDFSUtil(Configuration conf) {
        this.conf = conf;
        conf.setBoolean("dfs.support.append", true);//持续追加写入;
    }

    public HDFSUtil(Map<String,String> params) {
        String clusterURI = params.get("clusterURI");
        String clusterName = params.get("clusterName");
        String nameNode1 = params.get("nameNode1");
        String nameNode2 = params.get("nameNode2");
        String nameNode1URI = params.get("nameNode1URI");
        String nameNode2URI = params.get("nameNode2URI");
        String homeDir = params.get("homeDir");
        String principal = params.get("keyPrincipal");
        String keyFilePath = params.get("keyFilePath");
        if (null == clusterURI || null == clusterName || null == nameNode1 || null == nameNode2 ||
                null == nameNode1URI || null == nameNode2URI ||
                null == homeDir || null == principal || null == keyFilePath) {
            Log.error("Set hdfs params fail");
        }
        Configuration conf = new Configuration();
        String nameNodes = nameNode1 + "," + nameNode2;//拼接字符串
        String nameNode1rpc = "dfs.namenode.rpc-address." + clusterName + "." + nameNode1;//拼接RPC
        String nameNode2rpc = "dfs.namenode.rpc-address." + clusterName + "." + nameNode2;//拼接RPC
        conf.set("fs.defaultFS", clusterURI);
        conf.set("dfs.nameservices", clusterName);
        conf.set("dfs.ha.namenodes." + clusterName, nameNodes);
        conf.set(nameNode1rpc, nameNode1URI);
        conf.set(nameNode2rpc, nameNode2URI);
        conf.set("dfs.client.failover.proxy.provider." + clusterName, "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
        System.setProperty("java.security.krb5.conf", "/etc/krb5.conf");//Kerberos身份验证模块文件位置。
        System.setProperty("hadoop.home.dir", homeDir);//无实质意义，用于解决未在HADOOP环境下执行，需要设置此参数。避免提示错误找不到HADOOP_HOME。
        conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());//设置文件系统为HDFS
        //增加hadoop开启安全配置
        conf.setBoolean("hadoop.security.authorization", true);
        //配置安全认证方式为kerberos
        conf.set("hadoop.security.authentication", "kerberos");
        //设置namenode的principal
        conf.set("dfs.namenode.kerberos.principal", principal);
        //设置datanode的principal
        conf.set("dfs.datanode.kerberos.principal", principal);
        conf.set("dfs.namenode.kerberos.principal.pattern", "hdfs/*@CHINATELECOM.CN");
        //通过hadoop security下中的 UserGroupInformation类来实现使用keytab文件登录
        UserGroupInformation.setConfiguration(conf);
        try {
            //设置登录的kerberos principal和对应的keytab文件，其中keytab文件需要kdc管理员生成给到开发人员
            UserGroupInformation.loginUserFromKeytab(principal, keyFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.conf = conf;
        conf.setBoolean("dfs.support.append", true);//持续追加写入;
    }


    /**
     * hdfs追加文本，已有文件输出流
     *
     * @param HDFSPath 路径
     * @return 输出流
     */
    private FSDataOutputStream getOutputStream(String HDFSPath) {
        Path path = new Path(HDFSPath);
        try {
            FileSystem fs = FileSystem.get(URI.create(HDFSPath), conf);
            if (!fs.exists(path)) {//如果文件不存在，创建文件返回输出流
                return fs.create(path, false);
            }
            return fs.append(path);
        } catch (Exception e) {
            Log.error(" getOutputStream error:" + e);
            return null;
        }
    }


    /**
     * 获取输入流
     *
     * @param HDFSPath 路径
     * @return 输入流
     */
    private FSDataInputStream getInputStream(String HDFSPath) {
        Path path = new Path(HDFSPath);
        try {
            FileSystem fs = FileSystem.get(new URI(HDFSPath), conf);
            if (!fs.exists(path)) {//如果文件不存在
                Log.error(HDFSPath + " not exist");
                fs.close();
                return null;
            }
            return fs.open(path);
        } catch (Exception e) {
            Log.error(e);
            return null;
        }
    }


    /**
     * HDFS追加写入文件
     *
     * @param HDFSPath 文件路径
     */
    public void append(String HDFSPath, String str) {
        try {
            FSDataOutputStream out = getOutputStream(HDFSPath);
            if (out != null) {
                String charSet = "UTF-8";
                out.write(str.getBytes(charSet));
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            Log.error(e);
        }
    }




    /**
     * 判断HDFS路径是否存在
     *
     * @param HDFSPath HDFS文件路径
     */
    public boolean exist(String HDFSPath) {
        boolean exist;
        try {
            Path path = new Path(HDFSPath);
            FileSystem fs = FileSystem.get(new URI(HDFSPath), conf);
            exist = fs.exists(path);
            fs.close();
        } catch (IOException | URISyntaxException e) {
            Log.error(e);
            return false;
        }
        return exist;
    }


    private boolean delPath(String filePath) {
        try {
            FileSystem fs = FileSystem.get(new URI(filePath), conf);
            Log.debug("Delete Path:" + filePath);
            return fs.delete(new Path(filePath), true);
        } catch (Exception e) {
            Log.debug(e);
            return false;
        }
    }


    /**
     * 获取目录下的路径
     *
     * @param HDFSPath 目录路径
     * @return List<String> 文件
     */
    public List<String> getSubPath(String HDFSPath) {
        Log.debug("Get Path:" + HDFSPath);
        Path path = new Path(HDFSPath);
        List<String> list = new ArrayList<>();
        try {
            FileSystem fs = path.getFileSystem(conf);
            FileStatus[] fileStatus = fs.listStatus(path);
            for (FileStatus files : fileStatus) {
                list.add(files.getPath().toString());
            }
            fs.close();
        } catch (IOException e) {
            Log.error(e);
            return null;
        }
        return list;
    }

    /**
     * 复制HDFS文件至本地
     *
     * @param HDFSPath  HDFS路径
     * @param localPath 本地路径
     */
    public void copyFileToLocal(String HDFSPath, String localPath) {
        try {
            FSDataInputStream in = getInputStream(HDFSPath);
            OutputStream out = new FileOutputStream(localPath);
            assert in != null;
            IOUtils.copyBytes(in, out, 4096, true);
        } catch (Exception e) {
            Log.error(e);
        }
    }


    /**
     * 读取HDFS文件内容每行至List
     *
     * @param filePath 文件路径
     * @return List<String>
     */
    public List<String> readToList(String filePath) {
        List<String> list = new ArrayList<>();
        BufferedReader reader = null;
        try {
            String line;
            FSDataInputStream inputStream = getInputStream(filePath);
            assert inputStream != null;
            reader = new BufferedReader(new InputStreamReader(inputStream));
            // 一次读入一行，直到读入null为文件结束
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
            reader.close();
        } catch (IOException e) {
            Log.error(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.error(e);
                }
            }
        }
        return list;
    }

    @Override
    public boolean writeToFile(String filePath, String str) {
        Log.debug("writePath:"+filePath);
        String charSet = "UTF-8";
        FileSystem fs;
        Path path = new Path(filePath);
        try {
            fs = FileSystem.get(URI.create(filePath), conf);
            if (!fs.exists(path)) {//如果文件不存在则创建文件
                Log.debug("Create File:" + filePath);
                this.createFile(filePath);
            }
            FSDataOutputStream out = fs.append(path); //定义输出流
            if (str != null) {
                Log.debug("Write To File:" + filePath);
                out.write(str.getBytes(charSet));
                out.flush();
                out.close();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addToFile(String filePath, String str) {
        return false;
    }

    @Override
    public boolean isFile(String path) {
        return this.exist(path);
    }

    @Override
    public boolean isDir(String path) {
        return this.exist(path);
    }

    @Override
    public boolean delFile(String path) {
        return false;
    }

    @Override
    public boolean delDir(String path) {
        return false;
    }

    @Override
    public boolean createFile(String path) {
        return false;
    }

    @Override
    public boolean createDir(String path) {
        return false;
    }

    @Override
    public boolean delFiles(String path) {
        return false;
    }

}
