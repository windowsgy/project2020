package commons.utils;


import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.io.Serializable;
import java.util.List;

/**
 * 日志记录类
 * Created by Telis on 17/7/12.
 * info 打印输出 并可选记录文件
 * debug 打印输出 并可选记录文件
 * warn 打印输出 并可选记录文件
 * error 打印输出 并可选记录文件
 * msg 消息不记录文件
 * out 系统输出不换行，不记录文件
 */
public class Log implements Serializable {

    private static DateTime dtUtils = new DateTime("yyyy-MM-dd HH:mm:ss");
    private static boolean debugOnOff = false;//debug 开关
    private static FileSystemAll fileUtils;
    private static String logFilePath = null;

    /**
     * 设置日志Debug开关
     *
     * @param debug debug开关
     */
    public static void setDebug(boolean debug) {
        debugOnOff = debug;
    }

    /**
     * 设置日志文件参数
     *
     * @param logFilePath 日志路径
     */
    public static void setLogFile(FileUtils fileUtils, String logFilePath) {
        Log.fileUtils = fileUtils;
        Log.logFilePath = logFilePath;
    }


    public static <T> void debug(T x) {
        if (debugOnOff) {
            String message = "\033[35;4m" + "[debug][" + dtUtils.getCurTime() + "]" + x + "\033[0m" + "\r\n";
            System.out.print(message);
            if (logFilePath != null) {
                fileUtils.addToFile(logFilePath, message);
            }
        }
    }

    public static <T> void info(T x) {
        String message = "[info][" + dtUtils.getCurTime() + "]" + x + "\r\n";
        System.out.print(message);
        if (logFilePath != null) {
            fileUtils.addToFile(logFilePath, message);
        }

    }

    public static <T> void warn(T x) {
        String message = "\033[33;2m" + "[warn][" + dtUtils.getCurTime() + "]" + x + "\033[0m" + "\r\n";
        System.out.print(message);
        if (logFilePath != null) {
            fileUtils.addToFile(logFilePath, message);
        }

    }

    public static <T> void error(T x) {
        String message = "\033[31;2m" + "[error][" + dtUtils.getCurTime() + "]" + x + "\033[0m" + "\r\n";
        System.out.print(message);
        if (logFilePath != null) {
            fileUtils.addToFile(logFilePath, message);
        }

    }

    public static <T> void fatal(T x) {
        String message = "\033[33;2m" + "[error][" + dtUtils.getCurTime() + "]" + x + "\033[0m" + "\r\n";
        System.out.print(message);
        if (logFilePath != null) {
            fileUtils.addToFile(logFilePath, message);
        }

    }

    public static <T> void message(T x) {
        String message = "[message][" + dtUtils.getCurTime() + "]" + x + "\r\n";
        System.out.print(message);
    }


    public static void lineL0() {
        String message = "################################################################################################################################################" + "\r\n";
        System.out.print(message);
        if (logFilePath != null) {
            fileUtils.addToFile(logFilePath, message);
        }

    }

    public static void lineL1() {
        String message = "***********************************************************************************************************************" + "\r\n";

        System.out.print(message);
        if (logFilePath != null) {
            fileUtils.addToFile(logFilePath, message);
        }

    }

    public static void lineL2() {
        String message = "==========================================================================================================" + "\r\n";
        System.out.print(message);
        if (logFilePath != null) {
            fileUtils.addToFile(logFilePath, message);
        }
    }


    public static void lineL3() {
        String message = "-----------------------------------------------------------------------------------------" + "\r\n";
        System.out.print(message);
        if (logFilePath != null) {
            fileUtils.addToFile(logFilePath, message);
        }

    }

    public static void lineL4() {
        String message = "..........................................................." + "\r\n";
        System.out.print(message);
        if (logFilePath != null) {
            fileUtils.addToFile(logFilePath, message);
        }

    }


    public static void sleep(int timeLong) {

        try {
            Thread.sleep(timeLong);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印List中的日志
     *
     * @param list List
     */
    public static void printList(List<String> list) {
        if (list.size() > 500) {
            for (int i = 0; i < 500; i++) {
                System.out.println(list.get(i));
            }
            Log.info("Only 500 records are printed");
        } else {
            for (String aList : list) {
                System.out.println(aList);
            }
        }
    }



    /**
     * 打印RDD
     *
     * @param rdd rdd
     */
    public static void printFilterKeyRDD(JavaPairRDD<String, String> rdd, String filter) {
        rdd.filter((Function<Tuple2<String, String>, Boolean>) stringStringTuple2 -> stringStringTuple2._1.contains(filter)).foreach((VoidFunction<Tuple2<String, String>>) structAlarmTuple2 ->
                Log.debug(structAlarmTuple2._1 + "    " + structAlarmTuple2._2)) ;
    }



    /**
     * 打印RDD
     *
     * @param rdd rdd
     */
    public static void printStringRDD(JavaRDD<String> rdd) {
        rdd.foreach((VoidFunction<String>) s -> Log.debug(s));

    }



}
