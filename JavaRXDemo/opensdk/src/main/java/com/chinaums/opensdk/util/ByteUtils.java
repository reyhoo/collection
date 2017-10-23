package com.chinaums.opensdk.util;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class ByteUtils {

    private static final int BUFFER_SIZE = 100;
    private static final String ENCODING_UTF_8 = "UTF-8";
    private static final byte[] DEFAULT_0 = toByteArray(0);

    /**
     * 把传入的数组转化为整型，数组长度为4
     *
     * @param br
     * @return
     */
    public static int tol(byte[] br) {
        return tol(br, 4);
    }

    public static int tol(byte[] br, int len) {
        int is = 0;
        if (br == null || br.length < len) {
            return -1;
        } else {
            for (int i = 0; i < len; i++) {
                is += (br[i] & 0xFF) << (8 * i);
            }
        }
        return is;
    }

    /**
     * 根据issource，生成一个长度为4的byte数组 此数组记录isource
     *
     * @param isource
     * @return
     */
    public static byte[] toByteArray(int isource) {
        return toByteArray(isource, 4);
    }

    /**
     * 根据issoirce，生成一个长度为len的字节数组
     *
     * @param isource
     * @param len
     * @return
     */
    public static byte[] toByteArray(int isource, int len) {
        byte[] bl = new byte[len];
        for (int i = 0; i < len; i++) {
            bl[i] = (byte) (isource >> 8 * i & 0xff);
        }
        return bl;
    }

    /**
     * 拼接两个字符数组
     *
     * @param a
     * @param b
     * @return
     */
    public static byte[] revert(byte[] a, byte[] b) {
        if (a == null) {
            a = new byte[0];
        }
        if (b == null) {
            b = new byte[0];
        }
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    /**
     * 处理字符串,返回符号长度要求的字符串,不满长度的右补“ ” 当要处理字符串长度大于指定长度时,返回null
     *
     * @param s
     * @param length
     * @return
     */
    public static String dealAlpha(String s, int length) {
        try {
            if (s == null || "".equals(s)) {
                s = "";
                for (int j = 0; j < length; j++)
                    s = s + " ";
            } else {
                if (s.length() > length) {
                    return null;
                } else if (s.length() < length) {
                    int rest = length - s.length();
                    for (int i = 0; i < rest; i++) {
                        s = s.concat(" ");
                    }
                    return s;
                } else {
                    return s;
                }
            }
        } catch (RuntimeException e) {
            UmsLog.e("", e);
        }
        return s;
    }

    /**
     * 处理byte数组,返回符号长度要求的byte数组,不满长度的补0
     *
     * @param by
     * @param length
     * @return
     */
    public static byte[] byteDealAlpha(byte[] by, int length) {
        if (by == null) {
            by = new byte[0];
            return by;
        } else {
            // 保证数组是8的倍数
            if (length % 8 != 0) {
                int num = 8 - length % 8;
                byte[] a = new byte[num];
                for (int i = 0; i < num; i++) {
                    a[i] = (byte) num;
                }
                by = revert(by, a);
            }
        }
        return by;
    }

    public static byte[] getLenAndValue(String value) {
        return getLenAndValue(value, null);
    }

    public static byte[] getLenAndValue(String value, String encoding) {
        try {
            if (encoding == null) {
                encoding = ENCODING_UTF_8;
            }
            if (value == null) {
                value = "";
            }
            byte[] valueByte = value.getBytes(ENCODING_UTF_8);
            byte[] valueByteAll = revert(toByteArray(valueByte.length),
                    valueByte);
            return valueByteAll;
        } catch (UnsupportedEncodingException e) {
            return DEFAULT_0;
        }
    }

    public static byte[] getLenAndValue(byte[] value) {
        if (value == null) {
            value = new byte[0];
            return value;
        } else {
            byte[] valueByteAll = revert(toByteArray(value.length), value);
            return valueByteAll;
        }
    }

    /**
     * 3DES分段 byte[]明文cipherText return int part(段数)
     */
    public static int getPart(byte[] cipherText) {
        int part = 0; // 密文段数
        if (cipherText.length / 2048 >= 1) {
            if (cipherText.length % 2048 != 0) {
                part = (cipherText.length / 2048) + 1;
            } else {
                part = (cipherText.length / 2048);
            }
        } else {
            part = 1;
        }
        return part;
    }

    /**
     * 十六进制 转换 byte[]
     *
     * @param hexStr
     * @return
     */
    public static byte[] hexString2ByteArray(String hexStr) {
        if (hexStr == null)
            return null;
        if (hexStr.length() % 2 != 0) {
            return null;
        }
        byte[] data = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            char hc = hexStr.charAt(2 * i);
            char lc = hexStr.charAt(2 * i + 1);
            byte hb = hexChar2Byte(hc);
            byte lb = hexChar2Byte(lc);
            if (hb < 0 || lb < 0) {
                return null;
            }
            int n = hb << 4;
            data[i] = (byte) (n + lb);
        }
        return data;
    }

    public static byte hexChar2Byte(char c) {
        if (c >= '0' && c <= '9')
            return (byte) (c - '0');
        if (c >= 'a' && c <= 'f')
            return (byte) (c - 'a' + 10);
        if (c >= 'A' && c <= 'F')
            return (byte) (c - 'A' + 10);
        return -1;
    }

    /**
     * byte[] 转 16进制字符串
     *
     * @param arr
     * @return
     */
    public static String byteArray2HexString(byte[] arr) {
        StringBuilder sbd = new StringBuilder();
        for (byte b : arr) {
            String tmp = Integer.toHexString(0xFF & b);
            if (tmp.length() < 2)
                tmp = "0" + tmp;
            sbd.append(tmp);
        }
        return sbd.toString();
    }

    public static final byte[] getData(InputStream is, int length,
                                       int bufferSize) {
        int lenLoaded = 0;
        byte[] data = null;
        ByteBuffer buffer = ByteBuffer.allocate(length);
        byte[] dataTemp = null;
        while (true) {
            if (lenLoaded >= length) {
                break;
            }
            if (length - lenLoaded < bufferSize) {
                bufferSize = length - lenLoaded;
            }
            dataTemp = new byte[bufferSize];
            try {
                is.read(dataTemp);
                buffer.put(dataTemp);
            } catch (Exception e) {
                Log.d("Test", "read Exception = " + e.toString());
            }
            lenLoaded += bufferSize;
        }
        buffer.flip();
        data = buffer.array();
        return data;
    }

    /**
     * 从输入流中读取固定长度的数据
     *
     * @param is     输入流
     * @param length 数据长度
     * @return 输入流中的数据
     */
    public static final byte[] getBytes(InputStream is, int length) {
        if (length <= 0)
            return null;
        byte[] result = null;
        int lenRead = 0;
        byte[] temp = new byte[256];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (true) {
            try {
                int remain = length - lenRead;
                if (remain > 256)
                    remain = 256;
                int len = is.read(temp, 0, remain);
                if (len > 0) {
                    baos.write(temp, 0, len);
                    lenRead += len;
                    if (lenRead >= length) {
                        break;
                    }
                } else {
                    return null;
                }
            } catch (Exception e) {
                Log.e("Test", "getBytes Exception = " + e.toString());
                break;
            }
        }
        result = baos.toByteArray();
        return result;
    }

    /**
     * 获取二维字节数组从第二个字节数组到最后一个字节数组的字节长度总和,再转成字节数组
     *
     * @param srcData
     * @return
     * @author wxtang 2015-4-14下午1:54:38
     */
    public static final byte[] getLengthAll(byte[][] srcData) {
        int length = 0;
        for (int i = 1; i < srcData.length; i++) {
            length = length + srcData[i].length;
        }
        return intToByte(length);
    }

    /**
     * 从ByteBuffer读取出一个字节数组数据（数据之前有四个字节代表数据的长度）
     *
     * @param buffer 源ByteBuffer
     * @return 字节数组数据
     */
    public static final byte[] getDataWithLength(ByteBuffer buffer) {
        byte[] lengthData = new byte[4];
        int lengthTemp = 0;
        byte[] data = null;
        buffer.get(lengthData);
        lengthTemp = bytesToInt(lengthData);
        if (lengthTemp > 0) {
            data = new byte[lengthTemp];
            buffer.get(data);
        }
        return data;
    }

    /**
     * 从ByteBuffer读取出一个字节数组数据（数据之前有四个字节代表数据的长度）
     *
     * @param srcData 源ByteBuffer
     * @return 字节数组数据
     */
    public static final byte[] getDataWithLength(byte[] srcData) {
        ByteBuffer buffer = ByteBuffer.wrap(srcData);
        byte[] lengthData = new byte[4];
        int lengthTemp = 0;
        byte[] data = null;
        buffer.get(lengthData);
        lengthTemp = bytesToInt(lengthData);
        if (lengthTemp > 0) {
            data = new byte[lengthTemp];
            buffer.get(data);
        }
        buffer.clear();
        return data;
    }

    /**
     * 从ByteBuffer读取出一个UTF-8编码的字符串（字符串数据之前有四个字节代表字符串数据的长度）
     *
     * @param buffer 源ByteBuffer
     * @return 字符串
     */
    public static final String getStringWithLength(ByteBuffer buffer) {
        String result = null;
        byte[] data = getDataWithLength(buffer);
        try {
            if (data != null) {
                result = new String(data, "UTF-8").trim();
            }
        } catch (Exception e) {
            Log.d("Test", "getStringWithLength Length = " + e.toString());
        }
        return result;
    }

    /**
     * 获取数据里的所有字符串（每个数据前都有长度）
     *
     * @param srcData 源数据
     * @param length  数据里包含的字符串总数
     * @return 数据里包含的所有字符
     */
    public static final String[] getAllString(byte[] srcData, int length) {
        String[] allString = new String[length];
        byte[] lengthData = new byte[4];
        int lengthTemp = 0;
        byte[] dataString = null;
        ByteBuffer buffer = ByteBuffer.wrap(srcData);
        for (int i = 0; i < length; i++) {
            buffer.get(lengthData);
            lengthTemp = bytesToInt(lengthData);
            if (lengthTemp > 0) {
                dataString = new byte[lengthTemp];
                buffer.get(dataString);
                try {
                    allString[i] = new String(dataString, "UTF-8").trim();
                } catch (UnsupportedEncodingException e) {
                    Log.d("Test", "getAllString Exception = " + e.toString());
                }
            }
        }
        buffer.clear();
        return allString;
    }

    /**
     * 获取数据里的所有字符串（每个数据前都有长度）
     *
     * @param buffer 源数据
     * @param length 数据里包含的字符串总数
     * @return 数据里包含的所有字符
     */
    public static final String[] getAllString(ByteBuffer buffer, int length) {
        String[] allString = new String[length];
        byte[] lengthData = new byte[4];
        int lengthTemp = 0;
        byte[] dataString = null;
        for (int i = 0; i < length; i++) {
            buffer.get(lengthData);
            lengthTemp = bytesToInt(lengthData);
            if (lengthTemp > 0) {
                dataString = new byte[lengthTemp];
                buffer.get(dataString);
                try {
                    allString[i] = new String(dataString, "UTF-8").trim();
                    // Log.d("Test", "getAllString "+i+" = " + allString[i]);
                } catch (UnsupportedEncodingException e) {
                    Log.d("Test", "getAllString Exception = " + e.toString());
                }
            }
        }
        return allString;
    }

    /**
     * 获取源数据里的所有子数据（每个数据前都有长度）
     *
     * @param srcData 源数据
     * @param length  源数据里包含的子数据总数
     * @return 源数据里包含的所有字符
     */
    public static final byte[][] getAllData(byte[] srcData, int length) {
        byte[][] allData = new byte[length][];
        byte[] lengthData = new byte[4];
        int lengthTemp = 0;
        byte[] subData = null;
        ByteBuffer buffer = ByteBuffer.wrap(srcData);
        for (int i = 0; i < length; i++) {
            buffer.get(lengthData);
            lengthTemp = bytesToInt(lengthData);
            if (lengthTemp > 0) {
                subData = new byte[lengthTemp];
                buffer.get(subData);
                allData[i] = subData;
            }
        }
        buffer.clear();
        return allData;
    }

    /**
     * 根据缓冲大小分割字节数组
     *
     * @param srcData
     * @return
     * @author wxtang 2015-4-14下午1:54:59
     */
    public static final byte[][] spliteByteArray(byte[] srcData) {
        int lengthDest = srcData.length % BUFFER_SIZE == 0 ? srcData.length
                / BUFFER_SIZE : srcData.length / BUFFER_SIZE + 1;
        byte[][] destData = new byte[lengthDest][];
        int lengthTemp = 0;
        ByteBuffer buffer = ByteBuffer.wrap(srcData);
        for (int i = 0; i < lengthDest; i++) {
            if (i == lengthDest - 1) {
                lengthTemp = srcData.length % BUFFER_SIZE == 0 ? BUFFER_SIZE
                        : srcData.length % BUFFER_SIZE;
            } else {
                lengthTemp = BUFFER_SIZE;
            }
            destData[i] = new byte[lengthTemp];
            buffer.get(destData[i]);
        }
        buffer.clear();
        return destData;
    }

    /**
     * 连接字节数组
     *
     * @param byteData
     * @return
     * @author wxtang 2015-4-14下午1:55:12
     */
    public static final byte[] getIntegrationBytes(byte[][] byteData) {
        byte[] data = null;
        int lengthData = 0;
        for (int i = 0; i < byteData.length; i++) {
            if (byteData[i] != null) {
                lengthData += byteData[i].length;
            }
        }
        ByteBuffer buffer = ByteBuffer.allocate(lengthData);
        for (int i = 0; i < byteData.length; i++) {
            if (byteData[i] != null) {
                buffer.put(byteData[i]);
            }
        }
        buffer.flip();
        data = buffer.array();
        buffer.clear();
        return data;
    }

    /**
     * 连接字节数组后将总长度加在前面
     *
     * @param byteData
     * @return
     * @author wxtang 2015-4-14下午1:55:22
     */
    public static final byte[] getIntegrationBytesWithLength(byte[][] byteData) {
        byte[] data = null;
        int lengthData = 0;
        for (int i = 0; i < byteData.length; i++) {
            if (byteData[i] != null) {
                lengthData += byteData[i].length;
            }
        }
        ByteBuffer buffer = ByteBuffer.allocate(4 + lengthData);
        byte[] len = intToByte(lengthData);
        buffer.put(len);
        for (int i = 0; i < byteData.length; i++) {
            if (byteData[i] != null) {
                buffer.put(byteData[i]);
            }
        }
        buffer.flip();
        data = buffer.array();
        buffer.clear();
        return data;
    }

    /**
     * 将字节数组的长度加在前面
     *
     * @param byteData
     * @return
     * @author wxtang 2015-4-14下午1:55:36
     */
    public static final byte[] getIntegrationBytesWithLength(byte[] byteData) {
        byte[] data = null;
        int lengthData = byteData.length;
        ByteBuffer buffer = ByteBuffer.allocate(lengthData + 4);
        byte[] len = intToByteBigEnd(lengthData);
        buffer.put(len);
        buffer.put(byteData);
        buffer.flip();
        data = buffer.array();
        buffer.clear();
        return data;
    }

    /**
     * 将字节数组的长度加在前面
     *
     * @param info
     * @return
     * @author wxtang 2015-4-14下午1:55:47
     */
    public static final byte[] getIntegrationBytesWithLength(String info) {
        byte[] byteData = null;
        try {
            byteData = info.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            UmsLog.d("getIntegrationBytesWithLength UnsupportedEncodingException = "
                    + e.toString());
        }
        byte[] data = null;
        int lengthData = byteData.length;
        ByteBuffer buffer = ByteBuffer.allocate(lengthData + 4);
        byte[] len = intToByte(lengthData);
        buffer.put(len);
        buffer.put(byteData);
        buffer.flip();
        data = buffer.array();
        buffer.clear();
        return data;
    }

    /**
     * 将字符串拼成带数据长度的字节数组
     *
     * @param strData
     * @return
     * @author wxtang 2015-4-14下午1:55:59
     */
    public static final byte[] getIntegrationBytes(String[] strData) {
        byte[] data = null;
        int lengthData = 0;
        int lengthTemp = 0;
        byte[][] integrationLength = new byte[strData.length][];
        byte[][] integrationBytes = new byte[strData.length][];
        for (int i = 0; i < integrationBytes.length; i++) {
            if (strData[i] != null) {
                try {
                    integrationBytes[i] = strData[i].getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    Log.d("Test",
                            "getIntegrationBytes Exception = " + e.toString());
                }
                lengthTemp = integrationBytes[i].length;
                lengthData += lengthTemp;
                integrationLength[i] = intToByte(lengthTemp);
                lengthData += integrationLength[i].length;
            } else {
                integrationBytes[i] = null;
                lengthTemp = 0;
                integrationLength[i] = intToByte(lengthTemp);
                lengthData += integrationLength[i].length;
            }
        }
        ByteBuffer buffer = ByteBuffer.allocate(lengthData);
        for (int i = 0; i < integrationBytes.length; i++) {
            if (integrationLength[i] != null) {
                buffer.put(integrationLength[i]);
            }
            if (integrationBytes[i] != null) {
                buffer.put(integrationBytes[i]);
            }
        }
        buffer.flip();
        data = buffer.array();
        buffer.clear();
        return data;
    }

    /**
     * 将字符串拼成不带数据长度的字节数组
     *
     * @param strData
     * @return
     * @author wxtang 2015-4-14下午1:56:15
     */
    public static final byte[] getIntegrationBytes1(String[] strData) {
        byte[] data = null;
        int lengthData = 0;
        int lengthTemp = 0;
        byte[][] integrationLength = new byte[strData.length][];
        byte[][] integrationBytes = new byte[strData.length][];
        for (int i = 0; i < integrationBytes.length; i++) {
            if (strData[i] != null) {
                try {
                    integrationBytes[i] = strData[i].getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    Log.d("Test",
                            "getIntegrationBytes Exception = " + e.toString());
                }
                lengthTemp = integrationBytes[i].length;
                lengthData += lengthTemp;
                integrationLength[i] = intToByte(lengthTemp);
                lengthData += integrationLength[i].length;
            } else {
                integrationBytes[i] = null;
                lengthTemp = 0;
                integrationLength[i] = intToByte(lengthTemp);
                lengthData += integrationLength[i].length;
            }

        }
        ByteBuffer buffer = ByteBuffer.allocate(lengthData);
        for (int i = 0; i < integrationBytes.length; i++) {
            if (integrationLength[i] != null) {
                buffer.put(integrationLength[i]);
            }
            if (integrationBytes[i] != null) {
                buffer.put(integrationBytes[i]);
            }
        }
        buffer.flip();
        data = buffer.array();
        buffer.clear();
        return data;
    }

    /**
     * INT 转成 字节数组
     *
     * @param i
     * @return
     * @author wxtang 2015-4-14下午1:56:27
     */
    public static byte[] intToByte(int i) {
        byte[] bt = new byte[4];
        bt[0] = (byte) (0xff & i);
        bt[1] = (byte) ((0xff00 & i) >> 8);
        bt[2] = (byte) ((0xff0000 & i) >> 16);
        bt[3] = (byte) ((0xff000000 & i) >> 24);
        return bt;
    }

    /**
     * INT 转成 字节数组
     *
     * @param i
     * @return
     * @author wxtang 2015-4-14下午1:56:37
     */
    public static byte[] intToByteBigEnd(int i) {
        byte[] bt = new byte[4];
        bt[0] = (byte) ((0xff000000 & i) >> 24);
        bt[1] = (byte) ((0xff0000 & i) >> 16);
        bt[2] = (byte) ((0xff00 & i) >> 8);
        bt[3] = (byte) (0xff & i);
        return bt;
    }

    /**
     * 字节数组 转成 INT
     *
     * @param bytes
     * @return
     * @author wxtang 2015-4-14下午1:56:47
     */
    public static int bytesToInt(byte[] bytes) {
        int num = bytes[3] & 0xFF;
        num |= ((bytes[2] << 8) & 0xFF00);
        num |= ((bytes[1] << 16) & 0xFF0000);
        num |= ((bytes[0] << 24) & 0xFF000000);
        return num;
    }

    /**
     * 数组后面填充0，保证8字节对齐
     *
     * @param data
     * @return
     */
    public static byte[] addPadding(byte[] data) {
        byte[] data2 = data;
        if (data.length % 8 != 0) {
            data2 = new byte[(data.length / 8 + 1) * 8];
            System.arraycopy(data, 0, data2, 0, data.length);
        }
        return data2;
    }

    /**
     * 对数据进行PKCS7填充
     *
     * @param data
     * @return
     */
    public static byte[] addPKCS7Padding(byte[] data) {
        if (data == null)
            return data;
        int len = 8 - (data.length % 8);
        byte b = (byte) len;
        byte[] result = new byte[data.length + len];
        System.arraycopy(data, 0, result, 0, data.length);
        for (int i = 0; i < len; i++) {
            result[data.length + i] = b;
        }
        return result;
    }

    /**
     * 去除PKCS7填充
     *
     * @param data
     * @return
     */
    public static byte[] removePKCS7Padding(byte[] data) {
        if (data == null)
            return data;
        if (data.length < 8 || data.length % 8 != 0)
            throw new IllegalArgumentException(
                    "Input data is not PKCS7 padded.");
        byte b = data[data.length - 1];
        byte[] result = new byte[data.length - b];
        System.arraycopy(data, 0, result, 0, result.length);
        return result;
    }

}
