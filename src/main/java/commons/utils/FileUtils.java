package commons.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 文件工具类
 */
public class FileUtils implements FileSystemAll {

    /**
     * 判断路径是否存在
     *
     * @param filePath 文件路径
     * @return boolean
     */
    @Override
    public boolean exist(String filePath) {
        try {
            File path = new File(filePath);
            return path.exists();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断路径是否为一个文件
     *
     * @param filePath 文件路径
     * @return boolean
     */
    public boolean isFile(String filePath) {
        try {
            File path = new File(filePath);
            return path.exists() && path.isFile();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    /**
     * 判断路径是否为一个目录
     *
     * @param dirPath 目录路径
     * @return boolean
     */
    public boolean isDir(String dirPath) {
        try {
            File path = new File(dirPath);
            return path.exists() && path.isDirectory();
        } catch (Exception e) {
            Log.error(e.getMessage());
            return false;
        }

    }

    /**
     * 创建目录
     *
     * @param path 路径
     */
    public boolean createDir(String path) {
        try {
            File file = new File(path);
            if (file.exists() && file.isDirectory()) {
                Log.debug("isExists :" + path);
                return false;
            } else {
                return file.mkdir();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 删除目录
     *
     * @param path 目录
     * @return boolean
     */
    public boolean delDir(String path) {
        File dir = new File(path);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            assert children != null;
            for (String aChildren : children) {
                File subPath = new File(dir, aChildren);//构建父子
                boolean success = delDir(subPath.getAbsolutePath());
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 删除目录中的文件
     *
     * @param path 路径
     * @return boolean
     */
    public boolean delFiles(String path) {
        Log.debug("delFiles Path" + path);
        boolean bool = false;
        try {
            File files = new File(path);
            if (!files.exists() || !files.isDirectory()) {//如果目录不存在返回
                Log.debug("path not exist :" + path);
                return bool;
            }
            String[] file = files.list();
            if (file == null) {
                Log.debug("path is null :" + path);
                return bool;
            } else {
                for (String aFile : file) {
                    String full_filePath = files.getPath() + "/" + aFile;
                    File f = new File(full_filePath);
                    bool = f.delete();
                    if (!bool) {
                        return bool;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return bool;
    }

    /**
     * 读取文件到list
     *
     * @param path       文件路径
     * @param lineNumber 从第几行开始读取
     * @return list
     */
    public List<String> readToList(String path, long lineNumber) {
        Log.debug("readFiles Path" + path);

        List<String> list = new ArrayList<>();//返回结果
        BufferedReader br;
        try {
            File file = new File(path);
            if (!file.exists() || !file.isFile()) {
                Log.debug("path is exists" + path);
                return list;
            }
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line;
            int lineCtl = 0; //读取位置控制
            //int loadLineCount = 0;//读取行数计数
            // 一次读入一行，直到读入null为文件结束
            while ((line = br.readLine()) != null) {
                lineCtl++;
                if (lineCtl >= lineNumber) {//如果当前行号大于指定行号
                    //loadLineCount++;
                    list.add(line.trim());
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     *  按指定行读取文件
     * @param path 路径
     * @param start 起始行
     * @param end 结束行
     * @return list
     */
    public List<String> readToList(String path, long start,long end) {
        Log.debug("readFiles Path" + path);
        List<String> list = new ArrayList<>();//返回结果
        BufferedReader br;
        try {
            File file = new File(path);
            if (!file.exists() || !file.isFile()) {
                Log.debug("path is exists" + path);
                return list;
            }
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            String line;
            int lineCtl = 0; //读取位置控制
            //int loadLineCount = 0;//读取行数计数
            // 一次读入一行，直到读入null为文件结束
            while ((line = br.readLine()) != null) {
                lineCtl++;
                if (lineCtl >= start && lineCtl <= end) {//如果当前行号大于指定行号
                    list.add(line.trim());
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 读取文件到list
     *
     * @param path 文件路径
     * @return list
     */
    @Override
    public List<String> readToList(String path) {
        Log.debug("readFile Path" + path);
        List<String> list = new ArrayList<>();//返回结果
        BufferedReader br;
        try {
            File file = new File(path);
            if (!file.exists() || !file.isFile()) {
                Log.debug("Path Exists" + path);
                return list;
            }
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {

                list.add(line.trim());
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 创建文件
     *
     * @param path Path
     * @return boolean
     */
    public boolean createFile(String path) {
        Log.debug("create File path:" + path);
        try {
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                Log.debug(path + " :Path Exist");
                return false;
            }
            return file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 删除文件
     *
     * @param path path
     * @return boolean
     */
    public boolean delFile(String path) {
        Log.debug("delFile Path:" + path);
        try {
            File file = new File(path);
            if (!file.exists() && !file.isFile()) {
                Log.warn(path + " :Path Not Exist");
                return false;
            } else {
                return file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 字符串写入文件方法
     *
     * @param str  字符串
     * @param path 文件路径
     */
    @Override
    public boolean writeToFile(String path, String str) {
        Log.debug("writePath:" + path);
        try {
            File file = new File(path);
            if (!file.exists() && !file.isFile()) {
                if (!file.createNewFile()) {
                    Log.error("Create File Fail");
                    return false;
                }
            }
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
            writer.write(str);
            writer.close();
            return true;
        } catch (Exception e) {
            Log.error(e);
            return false;
        }
    }

    /**
     * 字符串写入文件方法
     *
     * @param str  字符串
     * @param path 文件路径
     * @param code 字符编码
     */
    public boolean writeToFile(String str, String path, String code) {
        Log.debug("writePath:" + path);
        try {
            File file = new File(path);
            if (!file.exists() && !file.isFile()) {
                Log.error("File Not Exist :" + path);
                return false;
            }
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), code));
            writer.write(str);
            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 追加写入文件
     *
     * @param path 文件路径
     * @param str  追加字符串
     */
    @Override
    public boolean addToFile(String path, String str) {
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(path, true);
            writer.write(str);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 获取文件名称
     *
     * @param path 路径
     * @return List
     */
    public String getFileName(String path) {
        try {
            File file = new File(path);
            if (!file.exists() && !file.isFile()) {
                Log.error(path + " :Path Not Exist");
            } else {
                return file.getName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取文件名称到List
     *
     * @param path 路径
     * @return List
     */
    public List<String> getSubPath(String path) {
        List<String> list = new ArrayList<>();//返回结果
        try {
            File file = new File(path);
            if (!file.exists()) {
                Log.error(path + " :not exists");
            } else {
                list = Arrays.asList(Objects.requireNonNull(file.list()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 读取文件第一行
     *
     * @param path 文件路径
     * @return L
     */
    public String readFirstLine(String path, String code) {
        String firstLine = null;//返回结果
        try {
            File file = new File(path);
            if (!file.exists() && !file.isFile()) {
                Log.error(path + " :not exists");
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), code));
                firstLine = br.readLine().trim();
                br.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return firstLine;
    }


    /**
     * 判断目录中全是文件
     *
     * @return boolean
     */
    private boolean allFileInSubDir(String path) {
        File dir = new File(path);
        if (!dir.exists() && !dir.isDirectory()) {
            Log.debug("dir not exists :" + path);
            return false;
        }
        String[] fileArr = dir.list();
        if (fileArr != null && fileArr.length > 0) {
            for (String aFileArr : fileArr) {
                File file = new File(path + "\\" + aFileArr);
                if (!file.isFile()) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }


    /**
     * file read to str
     *
     * @param path       path
     * @param lineNumber lineNumber
     * @param code       coder
     * @return string
     */
    public String readFile(String path, long lineNumber, String code) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br;
        try {
            File file = new File(path);
            if (!file.exists() && !file.isFile()) {
                Log.error("not exists :" + path);
                return null;
            }

            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), code));
            String line;
            int lineCtl = 0; //读取位置控制
            //int loadLineCount = 0;//读取行数计数
            // 一次读入一行，直到读入null为文件结束
            while ((line = br.readLine()) != null) {
                lineCtl++;
                if (lineCtl >= lineNumber && !("".equals(line.trim()))) {//如果当前行号大于指定行号
                    //loadLineCount++;
                    sb.append(line.trim()).append("\r\n");
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }


    /**
     * 数值切分
     * @param n 数值
     * @param split 切分多少份
     * @return 数值起止位
     */
    public List<Map<Integer,Integer>> splitNumbers(int n,int split ){
        List<Map<Integer,Integer>> list = new ArrayList<>();
        if(split>n){
            return null;
        }
        int x = n/split;
        int y = n%split;
        for(int i = 0 ; i <=split ;i++){
            Map<Integer,Integer> map = new HashMap<>();
            int start = i*x;
            int end = start+x-1;
            map.put(start,end);
            list.add(map);
        }
        if(0 != y){//余数不为0
            Map<Integer,Integer> map = new HashMap<>();
            int start = split*x;
            int end = split*x+y;
            map.put(start,end);
            list.add(map);
        }
        return list;
    }


}
