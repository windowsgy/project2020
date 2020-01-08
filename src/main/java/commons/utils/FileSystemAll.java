package commons.utils;

import java.util.List;

public interface FileSystemAll {
    boolean exist(String path);
    boolean isFile(String path);
    boolean isDir(String path);
    boolean delFile(String path);
    boolean delDir(String path);
    boolean createFile(String path);
    boolean createDir(String path);
    boolean delFiles(String path);
    List<String> readToList(String path);
    boolean writeToFile(String path, String Str);
    public boolean addToFile(String filePath, String str);
    public List<String> getSubPath(String path);
}
