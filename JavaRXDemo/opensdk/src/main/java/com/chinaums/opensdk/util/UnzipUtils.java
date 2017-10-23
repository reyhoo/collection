package com.chinaums.opensdk.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UnzipUtils {

    private static final int BUFFEREDSIZE = 4096;

    /**
     * 解压zip或者rar包的内容到指定的目录下，可以处理其文件夹下包含子文件夹的情况
     *
     * @param zipFilename
     * @param outputDirectory
     * @return
     */
    public synchronized static boolean Unzip(String zipFilename, String outputDirectory) {
        boolean ret = false;
        try {
            File outFile = new File(outputDirectory);
            if (!outFile.exists()) {
                outFile.mkdirs();
            }
            ZipFile zipFile = new ZipFile(zipFilename);
            Enumeration<? extends ZipEntry> en = zipFile.entries();
            ZipEntry zipEntry = null;
            while (en.hasMoreElements()) {
                zipEntry = (ZipEntry) en.nextElement();
                if (zipEntry.isDirectory()) {
                    String dirName = zipEntry.getName();
                    dirName = dirName.substring(0, dirName.length() - 1);
                    File f = new File(outFile.getPath() + File.separator + dirName);
                    f.mkdirs();
                } else {
                    String strFilePath = outFile.getPath() + File.separator + zipEntry.getName();
                    File f = new File(strFilePath);
                    // 判断文件不存在的话，就创建该文件所在文件夹的目录
                    if (!f.exists()) {
                        String[] arrFolderName = zipEntry.getName().split("/");
                        String strRealFolder = "";
                        for (int i = 0; i < (arrFolderName.length - 1); i++) {
                            strRealFolder += arrFolderName[i] + File.separator;
                        }
                        strRealFolder = outFile.getPath() + File.separator + strRealFolder;
                        File tempDir = new File(strRealFolder);
                        tempDir.mkdirs();
                    }
                    InputStream in = zipFile.getInputStream(zipEntry);
                    FileOutputStream out = new FileOutputStream(f);
                    try {
                        int c;
                        byte[] by = new byte[BUFFEREDSIZE];
                        while ((c = in.read(by)) != -1) {
                            out.write(by, 0, c);
                        }
                        out.flush();
                        ret = true;
                    } catch (IOException e) {
                        throw e;
                    } finally {
                        out.close();
                        in.close();
                    }
                }
            }
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return ret;
    }
    
}
