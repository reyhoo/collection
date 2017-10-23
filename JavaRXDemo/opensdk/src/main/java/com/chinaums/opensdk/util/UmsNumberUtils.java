package com.chinaums.opensdk.util;

import java.math.BigDecimal;
import java.text.NumberFormat;


public final class UmsNumberUtils {

    /**
     * double类型数值保留小数点后n位。
     *
     * @param num
     * @param maximumFractionDigits
     * @return
     */
    public static double format(double num, int maximumFractionDigits)
            throws Exception {
        NumberFormat nf2 = NumberFormat.getNumberInstance();
        nf2.setMaximumFractionDigits(maximumFractionDigits);
        String temp = nf2.format(num);
        return Double.parseDouble(temp);
    }

    /**
     * 比较数字大小
     *
     * @param n1
     * @param n2
     * @return -1 小于   0 等于  1 大于  2 异常
     */
    public static int compareStringNumber(String n1, String n2) {
        try {
            BigDecimal b1 = new BigDecimal(n1);
            BigDecimal b2 = new BigDecimal(n2);
            return b1.compareTo(b2);
        } catch (Exception e) {
            return 2;
        }
    }

}
