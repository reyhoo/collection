package com.chinaums.opensdk.util;

import android.text.TextUtils;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.util.Locale;

/**
 * 字符串转换工具
 */
public class UmsStringUtils extends StringUtils {

    /**
     * 比较两个字符串是否相等，允许传入null值
     *
     * @param s1
     * @param s2
     * @return
     * @author wxtang 2015-4-14上午11:39:15
     */
    public static boolean isEqual(String s1, String s2) {
        if (s1 == s2) {
            return true;
        }
        if (s1 == null && s2 != null) {
            return false;
        }
        return s1.equals(s2);
    }

    /**
     * 描述 ： CharArray 转为 String
     *
     * @param a
     * @param length
     * @return
     */
    public static String changeCharArraytoString(char[] a, int length) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            stringBuffer.append(a[i]);
        }
        return stringBuffer.toString();
    }

    /**
     * 获取文件名称（包含后缀）http://123/23.xml->23.xml
     *
     * @param s
     * @param ident
     * @return
     */
    public static String subString(String s, String ident) {
        return s.substring(s.lastIndexOf(ident) + 1);
    }

    /**
     * @param s        待截取的字符串
     * @param ident    开始字符串
     * @param identend 结束字符串
     * @return
     * @Description (截取开始字符串与结束字符串中间的部分)
     */
    public static String subString(String s, String ident, String identend) {
        return s.substring(s.lastIndexOf(ident) + 1, s.lastIndexOf(identend));
    }

    /**
     * 超链接字符转换
     *
     * @param str
     * @return
     */
    public static String filterToUrl(String str) {
        str = str.replace("%25", "%");
        str = str.replace("%23", "#");
        str = str.replace("%3F", "?");
        str = str.replace("%2F", "/");
        str = str.replace("%3D", "=");
        str = str.replace("%2C", ",");
        str = str.replace("%3B", ";");
        str = str.replace("%26", "&");
        str = str.replace("%20", " ");
        str = str.replace("%3C", "<");
        str = str.replace("%3E", ">");
        str = str.replace("%27", "'");
        str = str.replace("%22", "\"");
        return str;
    }

    /**
     * @param str
     * @return
     * @Description UTF-8 编码字符串
     */
    public static String utf8tostring(String str) {
        String changeAfter;
        if (str == null || str.equals("")) {
            return null;
        }
        try {
            changeAfter = URLDecoder.decode(str, "UTF-8");
            return changeAfter;
        } catch (UnsupportedEncodingException e) {
            UmsLog.e("", e);
        }
        return null;
    }

    /**
     * 去除文件名后缀 123.xml -> 123
     *
     * @param str
     * @return
     */
    public static String deleteSuffix(String str) {
        int index = str.lastIndexOf(".");
        str = str.substring(0, index);
        return str;
    }

    /**
     * 删除字符串分隔符"/"后半部分，如123/234/34.xml->123/234 若没有"/"，则返回"";如34.xml->""
     *
     * @param str
     * @return
     */
    public static String deleteLastData(String str) {
        StringBuffer sb = new StringBuffer();
        int n = str.lastIndexOf("/");
        if (n == -1) {
            return "";
        }
        sb.append(str.substring(0, n));
        return sb.toString();
    }

    /**
     * 删除最右边"/"后的字符串， "/"保留。 abc/efg -> abc/
     *
     * @param str
     * @return
     */
    public static String deleteRight(String str) {
        StringBuffer sb = new StringBuffer();
        int n = str.lastIndexOf("/");
        if (n == -1) {
            return "";
        }
        sb.append(str.substring(0, n));
        sb.append("/");
        return sb.toString();
    }

    /**
     * @param str
     * @return
     * @Description (校验是否为http链接)
     */
    public static boolean isHttpUrl(String str) {
        if (TextUtils.isEmpty(str))
            return false;
        Locale defloc = Locale.getDefault();
        return str.toLowerCase(defloc).startsWith("http");
    }

    /**
     * @param url
     * @return
     * @Description (TODO这里用一句话描述这个方法的作用)
     */
    public static boolean isObjectUrl(String url) {
        if (TextUtils.isEmpty(url))
            return false;
        return url.startsWith("objc:");
    }

    /**
     * @param url
     * @return
     * @Description 字符串是否为空
     */
    public static boolean isBlankUrl(String url) {
        if (TextUtils.isEmpty(url))
            return true;
        Locale defloc = Locale.getDefault();
        return url.toLowerCase(defloc).equals("about:blank");
    }

    /**
     * @param str
     * @return
     * @Description 判断URI
     */
    public static boolean isURI(String str) {
        if (TextUtils.isEmpty(str))
            return false;
        Locale defloc = Locale.getDefault();
        return str.toLowerCase(defloc).startsWith("file:///");
    }

    /**
     * @param str
     * @return
     * @Description 判断字符串是否为空
     */
    public static boolean isNull(String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        Locale defloc = Locale.getDefault();
        return str.toLowerCase(defloc).equals("null");
    }

    /**
     * @param str
     * @return
     * @Description 判断字符串是否不为空
     */
    public static boolean isNotEmpty(String str) {
        if (str == null || str.length() <= 0) {
            return false;
        }
        return true;
    }

    /**
     * @param str
     * @return
     * @Description 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.length() <= 0) {
            return true;
        }
        return false;
    }

    /**
     * @param cs
     * @return
     * @Description 判断字符是否为空
     */
    public static boolean isBlank(CharSequence cs) {
        int strLen = 0;
        if ((cs == null) || ((strLen = cs.length()) == 0))
            return true;
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param cs
     * @return
     * @Description 判断字符是否不为空
     */
    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    /**
     * @param value
     * @return
     * @Description (TODO这里用一句话描述这个方法的作用)
     */
    public static boolean hasValue(String value) {
        if (value != null && UmsStringUtils.isNotEmpty(value)
                && UmsStringUtils.isNotBlank(value)) {
            return true;
        }
        return false;
    }

    /**
     * @param str
     * @return
     * @Description (TODO这里用一句话描述这个方法的作用)
     */
    public static Integer convertStr2Integer(String str) {
        try {
            Integer integer = Integer.valueOf(str);
            return integer;
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return null;
    }

    /**
     * format2String 的实现描述：TODO 方法实现描述
     *
     * @param bi
     * @param maxLength
     * @return 参数说明
     * @author yltang 2015-9-29 上午11:09:53
     */
    public static String format2String(BigInteger bi, int maxLength) {
        String result = null;
        try {
            if (bi == null || bi.longValue() < 0 || maxLength <= 0) {
                return result;
            }
            result = String.format("%0" + maxLength + "d", bi);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return result;
    }
    
}
