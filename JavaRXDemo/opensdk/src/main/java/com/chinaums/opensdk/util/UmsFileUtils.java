package com.chinaums.opensdk.util;

import android.os.StatFs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class UmsFileUtils {

    private static final long MB = 1024L * 1024;
    private final static int FILE_SIZE = 8192;
    private static final long LOW_STORAGE_THRESHOLD = 3L * MB;

    /**
     * @param sourceDirPath
     * @param sourceFileName
     * @param targetDirPath
     * @param targetFileName
     * @return
     */
    public static boolean copyFile(String sourceDirPath, String sourceFileName,
                                   String targetDirPath, String targetFileName) {
        boolean result = false;
        try {
            File sourceFile = new File(sourceDirPath + File.separator
                    + sourceFileName);
            if (!sourceFile.exists())
                return result;
            InputStream inStream = new FileInputStream(sourceFile); // 读入原文件
            result = copyFile(inStream, targetDirPath, targetFileName);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return result;
    }

    /**
     * @param sourceInputStream
     * @param targetDirPath
     * @param targetFileName
     * @return
     */
    public static boolean copyFile(InputStream sourceInputStream,
                                   String targetDirPath, String targetFileName) {
        boolean result = false;
        int bytesRead = 0;
        FileOutputStream fs = null;
        try {
            if (sourceInputStream == null)
                return result;
            File targetFileFolder = new File(targetDirPath);
            if (!targetFileFolder.exists())
                targetFileFolder.mkdirs();
            File targetFile = new File(targetDirPath + File.separator
                    + targetFileName);
            if (!targetFile.exists())
                targetFile.createNewFile();
            fs = new FileOutputStream(targetFile);
            byte[] buffer = new byte[FILE_SIZE];
            while ((bytesRead = sourceInputStream.read(buffer, 0, FILE_SIZE)) != -1) {
                fs.write(buffer, 0, bytesRead);
            }
            result = true;
        } catch (Exception e) {
            UmsLog.e("", e);
        } finally {
            try {
                if (sourceInputStream != null)
                    sourceInputStream.close();
            } catch (Exception e2) {
                UmsLog.e("", e2);
            }
            try {
                if (fs != null) {
                    fs.flush();
                    fs.close();
                }
            } catch (Exception e2) {
                UmsLog.e("", e2);
            }
        }
        return result;
    }

    /**
     * @param dirPath
     * @param fileName
     * @param data
     * @return
     * @throws Exception
     */
    public static boolean createFile(String dirPath, String fileName,
                                     byte[] data) {
        boolean result = false;
        OutputStream output = null;
        try {
            File folder = new File(dirPath);
            if (!folder.exists())
                folder.mkdirs();
            File file = new File(dirPath + File.separator + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            output = new FileOutputStream(file);
            output.write(data, 0, data.length);
            output.flush();
            result = true;
        } catch (Exception e) {
            UmsLog.e("", e);
            result = false;
        } finally {
            try {
                if (output != null)
                    output.close();
            } catch (Exception e2) {
                UmsLog.e("", e2);
            }
        }
        return result;
    }

    /**
     * 校验是否存在
     *
     * @param dirPath
     * @param fileName
     * @return
     */
    public static boolean checkFile(String dirPath, String fileName) {
        boolean result = false;
        try {
            File file = new File(dirPath + File.separator + fileName);
            if (file.exists())
                result = true;
        } catch (Exception e) {
            UmsLog.e("", e);
            result = false;
        }
        return result;
    }

    /**
     * @param dirPath
     * @param fileName
     * @return
     */
    public static String readFile2String(String dirPath, String fileName) {
        try {
            byte[] fileDataByte = readFile(dirPath, fileName);
            return new String(fileDataByte, "UTF-8");
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return null;
    }

    /**
     * @param filePathAndName
     * @return
     */
    public static String readFile2String(String filePathAndName) {
        try {
            String dirPath = filePathAndName.substring(0,
                    filePathAndName.lastIndexOf(File.separator));
            String fileName = filePathAndName.substring(filePathAndName
                    .lastIndexOf(File.separator) + 1);
            return readFile2String(dirPath, fileName);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return null;
    }

    /**
     * @param filePathAndName
     * @return
     */
    public static byte[] readFile(String filePathAndName) {
        byte[] result = null;
        try {
            String dirPath = filePathAndName.substring(0,
                    filePathAndName.lastIndexOf(File.separator));
            String fileName = filePathAndName.substring(filePathAndName
                    .lastIndexOf(File.separator) + 1);
            return readFile(dirPath, fileName);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return result;
    }

    /**
     * @param inputStream
     * @return
     */
    public static String readStream2String(InputStream inputStream) {
        try {
            byte[] fileDataByte = readStream(inputStream);
            return new String(fileDataByte, "UTF-8");
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return null;
    }

    /**
     * @param inputStream
     * @return
     */
    public static byte[] readStream(InputStream inputStream) {
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[FILE_SIZE];
            int length = 0;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            return outputStream.toByteArray();
        } catch (Exception e) {
            UmsLog.e("", e);
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
            }
            try {
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
            }
        }
        return null;
    }

    /**
     * @param dirPath
     * @param fileName
     * @return
     */
    public static byte[] readFile(String dirPath, String fileName) {
        byte[] result = null;
        try {
            File folder = new File(dirPath);
            if (!folder.exists())
                return result;
            File file = new File(dirPath + File.separator + fileName);
            if (!file.exists() || !file.isFile())
                return result;
            FileInputStream fileInputStream = new FileInputStream(file);
            result = readStream(fileInputStream);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return result;
    }

    /**
     * @param dirPath
     * @param fileName
     * @return
     */
    public static boolean removeFile(String dirPath, String fileName) {
        boolean result = true;
        try {
            File folder = new File(dirPath);
            if (!folder.exists())
                return result;
            File file = new File(dirPath + File.separator + fileName);
            file.deleteOnExit();
        } catch (Exception e) {
            UmsLog.e("", e);
            result = false;
        }
        return result;
    }

    /**
     * 删除指定文件夹下的所有文件
     *
     * @param dirPath
     * @return
     */
    public static boolean removeAllFile(String dirPath) {
        boolean flag = false;
        try {
            File file = new File(dirPath);
            if (!file.exists()) {
                return flag;
            }
            if (!file.isDirectory()) {
                return flag;
            }
            String[] tempList = file.list();
            File temp = null;
            for (int i = 0; i < tempList.length; i++) {
                if (dirPath.endsWith(File.separator)) {
                    temp = new File(dirPath + tempList[i]);
                } else {
                    temp = new File(dirPath + File.separator + tempList[i]);
                }
                if (temp.isFile()) {
                    flag = temp.delete();
                } else if (temp.isDirectory()) {
                    removeAllFile(dirPath + "/" + tempList[i]);
                    flag = temp.delete();
                }
            }
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return flag;
    }

    /**
     * 判断是否有足够空间
     *
     * @param dirPath
     * @return
     */
    public static boolean hasEnoughSpace(String dirPath) {
        return getAvailableSpace(dirPath) >= LOW_STORAGE_THRESHOLD;
    }

    /**
     * 得到某一路径下的可用空间大小
     *
     * @param rootDir
     * @return
     */
    public static long getAvailableSpace(String rootDir) {
        try {
            File file = new File(rootDir);
            if (!file.exists())
                file.mkdirs();
            StatFs stat = new StatFs(rootDir);
            return stat.getAvailableBlocks() * (long) stat.getBlockSize();
        } catch (Exception e) {
            UmsLog.e("", e);
            return 0;
        }
    }

}
