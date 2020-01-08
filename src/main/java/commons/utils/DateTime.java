package commons.utils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


/**
 * 时间处理类
 * Created by jlgaoyuan on 2017/6/19.
 */
public class DateTime implements Serializable {

    private SimpleDateFormat format;

    /**
     * 构造函数
     *
     * @param format 时间格式
     */
    public DateTime(String format) {
        this.format = new SimpleDateFormat(format);
    }

    /**
     * 获取当前 日期时间字段
     *
     * @return 日期时间
     */
    public String getCurTime() {
        Calendar cal = Calendar.getInstance();
        return format.format(cal.getTime());
    }


    /**
     * 获取前n个分钟的 日期字段
     *
     * @param n 前n分钟
     * @return 日期字段
     */
    public String getBeforeMI(int n) {
        Calendar cal = Calendar.getInstance();// 取当前日期。
        cal.add(Calendar.MINUTE, -n);// 取当前时间的前n分钟.
        return format.format(cal.getTime());
    }

    /**
     * 获取前n个小时的 日期字段
     *
     * @param n 前n小时
     * @return 日期字段
     */
    public String getBeforeHour(int n) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, -n);// 取当前日期的前n小时.
        return format.format(cal.getTime());
    }

    /**
     * 获取前n天的 日期字段
     *
     * @param n 前n天
     * @return 日期字段
     */
    public String getBeforeDay(int n) {
        Calendar cal = Calendar.getInstance();// 取当前日期。
        cal.add(Calendar.DAY_OF_MONTH, -n);// 取当前日期的前n天.
        return format.format(cal.getTime());
    }

    /**
     * 分钟时间减 ，计算给定时间格式之前N分钟
     *
     * @param timeStr 时间格式字符串
     * @param n       时间差
     * @return 计算后时间
     */
    public String subtractionMi(String timeStr, int n) {
        try {
            Date date = format.parse(timeStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, -n);// 取指定时间的前n分钟.
            return format.format(calendar.getTime());
        } catch (ParseException e) {
            Log.error(e);
            return null;
        }
    }

    /**
     * 分钟时间减计算给定时间格式之前N小时
     *
     * @param timeStr 时间格式字符串
     * @param n       时间差
     * @return 计算后时间
     */
    public String subtractionH(String timeStr, int n) {
        try {
            Date date = format.parse(timeStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR, -n);// 取指定时间的前n小时.
            return format.format(calendar.getTime());
        } catch (ParseException e) {
            Log.error(e);
            return null;
        }

    }

    /**
     * 分钟时间减计算给定时间格式之前N天
     *
     * @param timeStr 时间格式字符串
     * @param n       时间差
     * @return 计算后时间
     */
    public String subtractionD(String timeStr, int n) {
        try {
            Date date = format.parse(timeStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -n);// 取指定时间的前n小时.
            return format.format(calendar.getTime());
        } catch (ParseException e) {
            Log.error(e);
            return null;
        }

    }


    /**
     * 计算起止时间差
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 时间差 单位分钟
     */
    public Long timeDifference(String startTime, String endTime, String timeFormat) {
        SimpleDateFormat format = new SimpleDateFormat(timeFormat);
        format.setLenient(false);
        try {
            Date d1 = format.parse(endTime);
            Date d2 = format.parse(startTime);
            long diff = d1.getTime() - d2.getTime();
            long mi = diff / 1000 / 60;
            return mi;
        } catch (Exception e) {
            e.printStackTrace();
            Log.error("startTime Is:" + startTime + "   " + "endTime Is :" + endTime);
            return null;
        }
    }


    /**
     * 判断时间是否大于等于
     * @param s1 time1
     * @param s2 time2
     * @param format 时间格式
     * @return boolean
     */
    public boolean greatAndEqu(String s1, String s2, String format) {
        DateFormat df = new SimpleDateFormat(format);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(df.parse(s1));
            c2.setTime(df.parse(s2));
        } catch (ParseException e) {
            Log.error(e);
        }
        int result = c1.compareTo(c2);
        return result == 0 || result > 0;
    }


    /**
     * 计算起止时间差
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 时间差 单位分钟
     */
    public double timeDifferenceSecond(String startTime, String endTime, String timeFormat) {
        double mi = -1;
        SimpleDateFormat format = new SimpleDateFormat(timeFormat);
        format.setLenient(false);
        try {
            Date d1 = format.parse(endTime);
            Date d2 = format.parse(startTime);
            long diff = d1.getTime() - d2.getTime();
            mi = diff / 1000;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mi;
    }


    private boolean whileOn = true;
    private String formatTime = null;

    /**
     * 获取给定时间的整点5分钟时间
     *
     * @param timeStr 给定时间
     * @return 时间格式 字符串
     */
    public String formatMinute5(String timeStr) {
        Date date = null;
        try {
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int minute = calendar.get(Calendar.MINUTE);
        if (0 == minute % 5) {
            formatTime = timeStr;
        }
        while (0 != minute % 5 && whileOn) {
            calendar.add(Calendar.MINUTE, -1);
            timeStr = format.format(calendar.getTime());
            formatMinute5(timeStr);
        }
        whileOn = false;
        return formatTime.substring(0,12)+"00";
    }

    /**
     * 获取给定时间的整点5分钟时间
     *
     * @param timeStr 给定时间
     * @return 时间格式 字符串
     */
    public String formatH(String timeStr) {
        return timeStr.substring(0,10)+"0000";
    }


    /**
     * 获取给定时间的整点5分钟时间
     *
     * @param timeStr 给定时间
     * @return 时间格式 字符串
     */
    public String formatD(String timeStr) {
        return timeStr.substring(0,8)+"000000";
    }

    /**
     * 日期时间格式检查
     *
     * @param dateTime 日期时间
     * @return boolean
     */
    public boolean dtCheck(String dateTime) {
        try {
            format.parse(dateTime);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * 时间戳格式字符串转换为 时间段格式 "yyyy-MM-dd HH:mm:ss"
     *
     * @param str 时间戳格式字段
     * @return 格式化日期时间
     */
    public String timestampConvTime(String str) {
        String yyyy = str.substring(0, 4);
        String mm = str.substring(4, 6);
        String dd = str.substring(6, 8);
        String hh = str.substring(8, 10);
        String mi = str.substring(10, 12);
        String ss = str.substring(12, str.length());
        return yyyy + "-" + mm + "-" + dd + " " + hh + ":" + mi + ":" + ss;
    }

    /**
     * 时间段格式 "yyyy-MM-dd HH:mm:ss" 字符串转换为 时间戳格式
     *
     * @param str 时格式字段
     * @return 时间戳格式
     */
    public String timeConvTimestamp(String str) {
        String yyyy = str.substring(0, 4);
        String mm = str.substring(5, 7);
        String dd = str.substring(8, 10);
        String hh = str.substring(11, 13);
        String mi = str.substring(14, 16);
        String ss = str.substring(17, 19);
        return yyyy + mm + dd + hh + mi + ss;
    }


    /**
     * 时间戳格式字符串转换为 路径格式 "yyyy/MM/dd/HH/mm"
     *
     * @param str 时间戳格式字段
     * @return 路径格式
     */
    public String timePathMI(String str) {
        String yyyy = str.substring(0, 4);
        String mm = str.substring(4, 6);
        String dd = str.substring(6, 8);
        String hh = str.substring(8, 10);
        String mi = str.substring(10, 12);
        if (System.getProperty("os.name").toLowerCase().startsWith("win")) {//如果是windows系统环境运行
            return "\\" + yyyy + "\\" + mm + "\\" + dd + "\\" + hh + "\\" + mi;//windows子路径格式
        } else {
            return "/" + yyyy + "/" + mm + "/" + dd + "/" + hh + "/" + mi;//linux子路径格式
        }

    }

    /**
     * 时间戳格式字符串转换为 路径格式 "yyyy/MM/dd/HH/"
     *
     * @param str 时间戳格式字段
     * @return 路径格式
     */
    public String timePathHH(String str) {
        String yyyy = str.substring(0, 4);
        String mm = str.substring(4, 6);
        String dd = str.substring(6, 8);
        String hh = str.substring(8, 10);
        if (System.getProperty("os.name").toLowerCase().startsWith("win")) {//如果是windows系统环境运行
            return "\\" + yyyy + "\\" + mm + "\\" + dd + "\\" + hh + "\\";//windows子路径格式
        } else {
            return "/" + yyyy + "/" + mm + "/" + dd + "/" + hh + "/";//linux子路径格式
        }
    }


    /**
     * 时间戳格式字符串转换为 路径格式 "yyyy/MM/dd/"
     *
     * @param str 时间戳格式字段
     * @return 路径格式
     */
    public String timePathD(String str) {
        String yyyy = str.substring(0, 4);
        String mm = str.substring(4, 6);
        String dd = str.substring(6, 8);
        if (System.getProperty("os.name").toLowerCase().startsWith("win")) {//如果是windows系统环境运行
            return "\\" + yyyy + "\\" + mm + "\\" + dd + "\\";//windows子路径格式
        } else {
            return "/" + yyyy + "/" + mm + "/" + dd + "/";//linux子路径格式
        }
    }


    /**
     * 时间戳格式字符串转换为 路径格式 "yyyy/MM/"
     *
     * @param str 时间戳格式字段
     * @return 路径格式
     */
    public String timePathM(String str) {
        String yyyy = str.substring(0, 4);
        String mm = str.substring(4, 6);
        if (System.getProperty("os.name").toLowerCase().startsWith("win")) {//如果是windows系统环境运行
            return "\\" + yyyy + "\\" + mm + "\\";//windows子路径格式
        } else {
            return "/" + yyyy + "/" + mm + "/";//linux子路径格式
        }

    }

    /**
     * 获取通配符路径
     *
     * @param i 通配符数量
     * @return 路径格式
     */
    public String getGlobbingPath(int i) {
        String splintChar = "/";
        if (System.getProperty("os.name").toLowerCase().startsWith("win")) {//如果是windows系统环境运行
            splintChar = "\\";
        }
        if (1 == i) {
            return "*";
        } else if (2 == i) {
            return "*" + splintChar + "*";
        } else if (3 == i) {
            return "*" + splintChar + "*" + splintChar + "*";
        }
        return null;
    }


    /**
     * 判断输入时间是否是周末
     *
     * @param timeStr    输入时间
     * @param timeFormat 时间格式
     * @return boolean
     */
    public boolean isWeekend(String timeStr, String timeFormat) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(timeFormat);
        LocalDateTime time = LocalDateTime.parse(timeStr, df);
        DayOfWeek dayOfWeek = time.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }


    public String toTimestamp(String str){
        String yyyy = str.substring(0, 4);
        String mm = str.substring(4, 6);
        String dd = str.substring(6, 8);
        String hh = str.substring(8, 10);
        String mi = str.substring(10, 12);
        return yyyy+"-"+mm+"-"+dd+"T"+hh+":"+mi+":00.000Z";
    }


}