package com.chinaums.opensdk.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

public class UmsMessageDigestUtils {

    private static char  hexChars[]          = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'    };

    public static String MESSAGE_DIGEST_TYPE = "sha-1";

    /**
     * 摘要值计算，输入String，输出String.
     * 
     * @param s
     * @return
     */
    public static String encode(String s) {
        if (s == null) {
            return null;
        }
        return encode(s.getBytes());
    }

    /**
     * 摘要值计算，输入bytes，输出String.
     * 
     * @param b
     * @return
     */
    public static String encode(byte[] b) {
        if (b == null) {
            return null;
        }
        try {
            MessageDigest mdTemp = MessageDigest.getInstance(MESSAGE_DIGEST_TYPE);
            mdTemp.update(b);
            byte[] md = mdTemp.digest();
            return toHexString(md);
        } catch (Exception e) {
        	UmsLog.e("",e);
            return null;
        }
    }

    /**
     * 摘要值计算，输入文件路径，输出String.
     * 
     * @param fileName
     * @return
     */
    public static String encodeFile(String fileUrl) {
        if (fileUrl == null) {
            return null;
        }
        InputStream fis = null;
        byte[] buffer = new byte[1024];
        int numRead = 0;
        MessageDigest md5;
        String output = null;
        try {
            fis = new FileInputStream(fileUrl);
            md5 = MessageDigest.getInstance(MESSAGE_DIGEST_TYPE);
            while ((numRead = fis.read(buffer)) > 0) {
                md5.update(buffer, 0, numRead);
            }
            output = toHexString(md5.digest());
        } catch (Exception e) {
        	UmsLog.e("",e);
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
            	UmsLog.e("",e);
            }
        }
        return output;
    }

    public static String toHexString(byte[] b) {
        int j = b.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            str[k++] = hexChars[b[i] >>> 4 & 0xf];
            str[k++] = hexChars[b[i] & 0xf];
        }
        return new String(str);
    }
}
