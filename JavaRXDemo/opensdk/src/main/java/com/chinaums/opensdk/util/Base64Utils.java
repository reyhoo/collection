package com.chinaums.opensdk.util;

import android.util.Base64;

public class Base64Utils {

    public static String encrypt(String str) {
        String encode = null;
        if (str.equals("")) {
            return "";
        }
        if (str != null) {
            encode = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
            encode = encode.replace("\n", "");
        }
        return encode;
    }

    public static String decryptBase64(String str) {
        String decode = null;
        if (str.equals("")) {
            return "";
        }
        if (str != null) {
            decode = new String(Base64.decode(str, Base64.DEFAULT));
        }
        return decode;
    }

}
