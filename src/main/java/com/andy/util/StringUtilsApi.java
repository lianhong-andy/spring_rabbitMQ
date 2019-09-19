package com.andy.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lianhong
 * @description 字符串工具类
 * @date 2019/9/19 0019下午 2:24
 */
public class StringUtilsApi {

    /**
     * 保留几位小数
     */
    public interface FORMAT_NUMBER {
        /**
         * 对浮点数进行格式化
         */
        public interface Float {
            public static final String ONE_DECIMAL = "%.1f";
            public static final String TWO_DECIMAL = "%.2f";
            public static final String THREE_DECIMAL = "%.3f";
            public static final String FOUR_DECIMAL = "%.4f";
        }

        /**
         * 对整数进行格式化
         * 占位符格式为：%[index$][标识]*[最小宽度]转换符
         */
        public interface Integer {
            /**
             * 将1显示为0001
             */
            public static final String ADD_PREFIXX_ZERO = "%04d";
            /**
             * 将-1000显示为(1,000)
             */
            public static final String ADD_COMMA = "%(,d";
        }
    }


    /**
     * 格式化字符串
     *
     * @param format
     * @param args
     * @return
     */
    public static String format(String format, Object... args) {
        String rst = String.format(format, args);
        if ("NaN".equals(rst)) {
            return "0";
        }

        return rst;
    }

    /**
     * 下划线转驼峰
     *
     * @param param
     * @return
     */
    public static String underlineToCamel(String param) {
        if (StringUtils.isBlank(param)) {
            return null;
        }

        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == '_') {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 驼峰转下划线
     * @param param
     * @return
     */
    public static String camelToUnderline(String param) {
        if (StringUtils.isBlank(param)) {
            return "";
        }
        if (param.indexOf("_") != -1) {
            return param;
        }

        int len = param.length();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append('_');
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 首字母大写
     * @return
     */
    public static String firstUpper(String str){
        if(str == null){
            return null;
        }
        str = str.replaceFirst(str.substring(0, 1),str.substring(0, 1).toUpperCase());
        return str;
    }

    public static String join(String[] pieces,String glue){
        StringBuilder resual = new StringBuilder();
        int size = pieces.length;

        for(int i=0;i<size;i++){
            resual.append(pieces[i]);
            if(i != size-1){
                resual.append(glue);
            }
        }
        return resual.toString();
    }

    public static String joinNoDup(String[] pieces,String glue){
        StringBuilder resual = new StringBuilder();
        List<String> list = new ArrayList<String>();
        int size = pieces.length;

        for(int i=0;i<size;i++){
            if( !list.contains(pieces[i])){
                list.add(pieces[i]);
                resual.append(pieces[i]);
                if(i != size-1){
                    resual.append(glue);
                }
            }
        }
        return resual.toString();
    }

    public static String joinNoDupNoBlank(String[] pieces,String glue){
        StringBuilder resual = new StringBuilder();
        List<String> list = new ArrayList<String>();
        int size = pieces.length;

        for(int i=0;i<size;i++){
            if(StringUtils.isNotBlank(pieces[i])){
                if( !list.contains(pieces[i])){
                    list.add(pieces[i]);
                    resual.append(pieces[i]);
                    resual.append(glue);
                }
            }
        }
        String re = resual.toString();
        if(re.length()>glue.length()){
            re = re.substring(0, re.length()-glue.length());
        }

        return re;
    }

    /**
     * 判断字符串是否是整数
     */
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 字符串反转移
     * @param str
     * @return
     */
    public static String stripslashes(String str){
        if (StringUtils.isBlank(str)) {
            return str;
        }
        char[] sc = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sc.length; i++) {
            if (sc[i]=='\\') {
                if (i+1 <= sc.length-1 && sc[i+1]=='\\') {
                    sb.append(sc[i]);
                    i++;
                }
            }else{
                sb.append(sc[i]);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String hgjg_hgg_hgh = underlineToCamel("hgjg_hgg_hgh");
        System.out.println("hgjg_hgg_hgh = " + hgjg_hgg_hgh);
        StringBuilder hhhg = new StringBuilder(3);
        System.out.println("hhhg = " + hhhg);
    }
}
