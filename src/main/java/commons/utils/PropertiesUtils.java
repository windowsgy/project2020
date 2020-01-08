package commons.utils;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PropertiesUtils {


    /**
     * 获取文件Buffer
     *
     * @param path 路径
     * @return buffer
     */
    private BufferedReader getBuffer(String path) {
        try {
            InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream(path);
            if (null == inputStream) {
                Log.error("Load Properties error");
                return null;
            }
            return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));//解决读取properties文件中产生中文乱码的问题

        } catch (Exception e) {
            Log.error(e);
            return null;
        }
    }

    /**
     * 获取配置文件
     *
     * @param path 文件路径
     */
    public List<String> getList(String path) {
        Log.debug("load properties is :"+path);
        List<String> list = new ArrayList<>();
        try {
            BufferedReader bf = getBuffer(path);
            if (null == bf) {
                return null;
            }
            String line;
            while ((line = bf.readLine()) != null) {
                list.add(line);
            }
        } catch (Exception e) {
            Log.error("not found file");
            return null;
        }
        return list;
    }

    /**
     * 获取配置文件 生成 map
     *
     * @param path 文件路径
     */
    public Map<String, String> getMap(String path) {
        Log.debug("load properties is :"+path);
        Map<String, String> params = new HashMap<>();
        Properties props = new Properties();
        try {
            BufferedReader bf = getBuffer(path);
            props.load(bf);
            for (Object keys : props.keySet()) {
                String key = String.valueOf(keys);  //key
                String val = props.getProperty(key);//value
                params.put(key, val);
            }
        } catch (Exception e) {
            Log.error("not found file");
            Log.error(e);
            return null;
        }
        return params;
    }




    /**
     * 获取配置文件 生成 double map
     *
     * @param path 配置文件路径
     */
    public Map<String, Map<String, String>> getDoubleMap(String path) {
        Log.debug("load properties is :"+path);
        Properties props = new Properties();
        Map<String, Map<String, String>> params = new HashMap<>();
        BufferedReader bf = getBuffer(path);
        try {
            props.load(bf);
            for (Object key : props.keySet()) {
                String keyString = String.valueOf(key);  //Key
                String valString = props.getProperty(keyString);//Value
                if (keyString.contains(".")) { //判断参数Key包含 . ，用于截取typeKey,subKey
                    String keys[] = keyString.split("\\.");
                    String superKey = keys[0];
                    String subKey = keys[1];
                    if (params.containsKey(superKey)) { //如果参数类型列表包含设定参数
                        params.get(superKey).put(subKey, valString);
                    } else {
                        Map<String, String> sub = new HashMap<>();
                        sub.put(subKey, valString);
                        params.put(superKey, sub);
                    }
                }
            }
            return params;
        } catch (Exception e) {
            Log.error("not found file");
            Log.error(e);
            return null;
        }
    }



    /**
     * 获取配置文件 生成 double map
     *
     * @param path 配置文件路径
     */
    public Map<Integer, String> getIntMap(String path) {
        Log.debug("load properties is :"+path);
        Properties props = new Properties();
        Map<Integer, String> params = new HashMap<>();
        BufferedReader bf = getBuffer(path);
        try {
            props.load(bf);
            for (Object key : props.keySet()) {
                String keyStr = String.valueOf(key);
                int keyInt = Integer.parseInt(keyStr);  //key
                String valString = props.getProperty(keyStr);//value
                params.put(keyInt,valString);
            }
            return params;
        } catch (Exception e) {
            Log.error("not found file");
            Log.error(e);
            return null;
        }
    }


    /**
     *
     * 更新properties文件的键值对
     * 如果该主键已经存在，更新该主键的值；
     * 如果该主键不存在，则插件一对键值。
     *
     * @param value value
     */
    public  boolean update(String filePath, String key, String value) {
        Log.debug("Set Properties:" + filePath);
        try {
            File file = new File(PropertiesUtils.class.getClassLoader().getResource(filePath).getPath());
            Properties props = new Properties();
            props.setProperty(key, value);
            FileOutputStream fos = new FileOutputStream(file);
            props.store(fos, "update");
            fos.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.error("update file error");
            return false;
        }
    }


    /**
     * 写入properties
     * @param path 路径
     * @param list List
     * @return boolean
     */
    public  boolean write(String path, List<String> list) {
        Log.debug("Write Properties:" + path);
        try {
            File file = new File(PropertiesUtils.class.getClassLoader().getResource(path).getPath());
            Properties props = new Properties();
            for(int i = 0 ; i < list.size();i++){
                props.setProperty(String.valueOf(i),list.get(i));
            }
            FileOutputStream fos = new FileOutputStream(file);
            props.store(fos, null);
            fos.flush();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.error("Write Properties Error");
            return false;
        }
    }
}
