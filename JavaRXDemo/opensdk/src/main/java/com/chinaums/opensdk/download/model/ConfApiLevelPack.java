package com.chinaums.opensdk.download.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.OpenConst.DynamicDownloadConf;
import com.chinaums.opensdk.util.UmsFileUtils;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;

import java.io.File;

public class ConfApiLevelPack extends ConfPack {

    /**
     * apiLevelStr
     */
    private String apiLevelStr = null;

    /**
     * processLevel
     */
    private JSONObject processLevel;

    /**
     * 默认action名
     */
    public static final String DEFAULT_ACTION = "default";

    /**
     * 默认level值
     */
    public static final int DEFAULT_LEVEL = 0;

    @Override
    protected boolean initPack() throws Exception {
        boolean success = false;
        String apiLevelStr = UmsFileUtils
                .readFile2String(getResProcessPath() + File.separator
                        + DynamicDownloadConf.CONF_API_LEVEL_PROCESS_FILE_NAME);
        if (UmsStringUtils.isNotBlank(apiLevelStr)) {
            this.apiLevelStr = apiLevelStr;
            try {
                processLevel = JSON.parseObject(apiLevelStr);
            } catch (JSONException e) {
                UmsLog.e("", e);
            }
            success = true;
        }
        return success;
    }

    public final String getApiLevelStr() {
        return apiLevelStr;
    }

    public int getLevel(String process) {
        return getLevel(process, null);
    }

    public int getLevel(String process, String action) {
        try {
            if (!UmsStringUtils.hasValue(process)) {
                return DEFAULT_LEVEL;
            }
            JSONObject processJson = processLevel.getJSONObject(process);
            if (processJson == null) {
                return DEFAULT_LEVEL;
            }
            action = UmsStringUtils.isBlank(action) ? DEFAULT_ACTION : action;
            if (processJson.get(action) != null) {
                return processJson.getIntValue(action);
            } else if (processJson.get(DEFAULT_ACTION) != null) {
                return processJson.getIntValue(DEFAULT_ACTION);
            } else {
                return DEFAULT_LEVEL;
            }
        } catch (JSONException e) {
            UmsLog.e("", e);
        }
        return DEFAULT_LEVEL;
    }

}
