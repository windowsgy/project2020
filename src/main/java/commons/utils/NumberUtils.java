package commons.utils;

import java.text.NumberFormat;

/**
 *
 * Created by jlgaoyuan on 2017/9/1.
 */
public class NumberUtils {

    //数值格式转换
    private static NumberFormat nf = NumberFormat.getPercentInstance();

    /**
     * 判断字符串是否是整形数值格式
     * @param str 字符串
     * @return boolean
     */
    public static boolean isInt(String str) {// 判断整型
        return str.matches("^\\d+$$");
    }

    /**
     * 判断字符串是否是double类型格式
     * @param str 字符串
     * @return boolean
     */
    private static boolean isDouble(String str) {// 判断小数，与判断整型的区别在与d后面的小数点
        return str.matches("\\d+\\.\\d+$");
    }

    /**
     * 判断字符串是否是数值
     * @param str 字符串
     * @return boolean
     */
    public static boolean isNumeric(String str){
        boolean isInt = isInt(str);
        boolean isDouble = isDouble(str);
        return isInt || isDouble;
    }

    /**
     * double 数值转换为 百分比格式字符串
     * @param  d  double数值
     * @param fractionDigits 保留小数点后几位
     * @return String
     */
    public static String toRatio(double d ,int fractionDigits){
        nf.setMinimumFractionDigits(fractionDigits);// 小数点后保留几位
        String str = nf.format(d);
        return str;
    }

    /**
     * double 保留小数点后几位
     * @param  d  double数值
     * @param fractionDigits 保留小数点后几位
     * @return String
     */
    public static String toDot(double d ,int fractionDigits){

        return String.format("%.3f", d);

    }

}
