package com.chinaums.opensdk.weex.module;

import android.text.TextUtils;

import com.chinaums.opensdk.weex.utils.AppletConstant;
import com.chinaums.opensdk.weex.utils.Utils;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *  @version 1.0
 *  @description         文件操作模块
 */

public class UmsFileModule extends UmsBasicModule {

    /**
     * 将文件保存到指定位置
     * @param filePath  临时文件路径
     * @param callback JS回调
     */
    @JSMethod
    public void saveFile(String filePath, JSCallback callback) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            if (TextUtils.isEmpty(filePath)) {
                callBySimple(false, callback);
                return;
            }

            File old = Utils.FileUtil.newFile(filePath);
            String name = old.getName();
            File dir = Utils.FileUtil.newFile(AppletConstant.Path.SAVED);

            boolean isSaved = false;
            //验证文件是否存在
            if (old.exists() && dir.exists()) {
                String path = AppletConstant.Path.SAVED + name;
                isSaved = Utils.FileUtil.copyTo(filePath, path);

                result.put("path", path);
                callByResult(isSaved, result, callback);
            } else {
                callBySimple(isSaved, callback);
            }

        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }
    }

    /**
     * 获取保存的文件列表
     *
     * @param callback JS回调
     */
    @JSMethod
    public void getSavedFileList(JSCallback callback) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            File saved = Utils.FileUtil.newFile(AppletConstant.Path.SAVED);
            ArrayList<String> array = new ArrayList<String>();
            if (saved.exists()) {
                File[] files = saved.listFiles();
                for (File file : files) {
                    if (file != null && file.exists()) {
                        array.add(file.getAbsolutePath());
                    }
                }
            }
            result.put("fileList", array);
            callByResult(true, result, callback);

        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }
    }

    /**
     * 获取文件相关信息
     *
     * @param filePath 文件路径
     * @param callback JS回调
     */
    @JSMethod
    public void getSavedFileInfo(String filePath, JSCallback callback) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            if (!Utils.FileUtil.isFile(filePath)) {
                callBySimple(false, callback);
                return;
            }

            File file = Utils.FileUtil.newFile(filePath);
            if (file.exists()) {
                result.put("size", file.length() + "");
                result.put("createTime", file.lastModified() + "");
                callByResult(true, result, callback);
            } else {
                callBySimple(false, callback);
            }
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }
    }

    /**
     * 删除文件
     * @param filePath 文件路径
     * @param callback JS回调
     */
    @JSMethod
    public void removeSavedFile(String filePath, JSCallback callback) {
        try {
            if (!Utils.FileUtil.isFile(filePath)) {
                callBySimple(false, callback);
                return;
            }
            callBySimple(Utils.FileUtil.delete(filePath), callback);
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }
    }
}
