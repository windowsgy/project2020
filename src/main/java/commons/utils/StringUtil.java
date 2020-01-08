package commons.utils;

import java.util.List;

public class StringUtil {
    /**
     * List<String> 转换为 String
     * @param list list
     * @return String
     */
    public static String listToString(List<String> list){
        StringBuilder sb = new StringBuilder();
        for (String aList : list) {
            sb.append(aList).append("\r\n");
        }
        return sb.toString();
    }
}
