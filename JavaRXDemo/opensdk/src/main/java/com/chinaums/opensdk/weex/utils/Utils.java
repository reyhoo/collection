package com.chinaums.opensdk.weex.utils;

import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Created at :  2017/9/19.
 * @Version :  1.0
 * @Description : 常用工具类集合
 */

public class Utils {
    /**
     * @Created at :  2017/6/13.
     * @Version :  1.0
     * @Description :
     */

    public static class FileUtil {
        /**
         * 根据路径创建文件
         *
         * @param path 路径
         * @return
         */
        public static File newFile(String path) {
            if (TextUtils.isEmpty(path)) {
                return null;
            }
            File file = new File(path);
            if (file.exists()) {
                return file;
            } else {
                if (path.lastIndexOf("/") > path.lastIndexOf(".")) {
                    file.mkdirs();
                    return file;
                } else {
                    File parent = file.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                }
                return file;
            }
        }

        /**
         * 判断是否是文件
         *
         * @param path 文件路径
         * @return
         */
        public static boolean isFile(String path) {
            if (TextUtils.isEmpty(path)) {
                return false;
            }
            File file = new File(path);
            if (!file.exists() || !file.isFile()) {
                return false;
            }
            return true;
        }

        /**
         * 删除文件
         *
         * @param filePath 文件路径
         * @return
         */
        public static boolean delete(String filePath) {
            File file = new File(filePath);
            if (file.exists()) {
                return file.delete();
            } else {
                return false;
            }
        }

        /**
         * 复制文件到指定路径
         *
         * @param path   文件路径
         * @param toPath 目标路径
         * @return
         */
        public static boolean copyTo(String path, String toPath) {
            try {
                FileInputStream in = new FileInputStream(path);
                return copyTo(in, toPath);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        /**
         * 复制文件到指定路径
         *
         * @param stream 文件流
         * @param toPath 目标路径
         * @return
         */
        public static boolean copyTo(InputStream stream, String toPath) {
            boolean isSucceed = true;
            try {
                BufferedInputStream inBuff = new BufferedInputStream(stream);
                FileOutputStream out = new FileOutputStream(toPath);
                BufferedOutputStream outBuff = new BufferedOutputStream(out);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inBuff.read(buffer)) != -1) {
                    outBuff.write(buffer, 0, length);
                }
                // 刷新缓冲的输出流
                outBuff.flush();

                //关闭流
                CloseableUtil.close(stream);
                CloseableUtil.close(out);
                CloseableUtil.close(inBuff);
                CloseableUtil.close(outBuff);

            } catch (IOException e) {
                e.printStackTrace();
                isSucceed = false;
            }

            return isSucceed;
        }
    }

    /**
     * @version 1.0
     * @description Closeable关闭处理工具
     */

    public static class CloseableUtil {
        public static void close(Closeable closeable) {
            try {
                if (closeable != null) {
                    closeable.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
