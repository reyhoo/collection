package com.chinaums.opensdk.data.model;

import com.chinaums.opensdk.manager.OpenDelegateManager;
import com.chinaums.opensdk.manager.OpenDynamicBizHistoryManager;
import com.chinaums.opensdk.manager.OpenHistoryDataManager;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;

import java.io.Serializable;

public class ResourceListHistory implements Serializable {

    private static final long serialVersionUID = -960773506445655294L;
    private static final String DEFAULT_VALUE_NULLS = "NULLS";

    public static String getPersonalBizCodes() {
        String defaultKey = "personalBizCodes_";
        try {
            String key = OpenDelegateManager.getProcessDelegate()
                    .getDynamicResourceHistoryKeyGenerateRule()
                    .getKeyForPersonalBizList();
            key = UmsStringUtils.isBlank(key) ? "" : key.trim();
            String ret = OpenHistoryDataManager.getHistoryStringData(defaultKey
                    + key, DEFAULT_VALUE_NULLS);
            if (!DEFAULT_VALUE_NULLS.equals(ret))
                return ret;
            // SP中没有就从预安装中获取
            return OpenDynamicBizHistoryManager.getInstance()
                    .getResourcePreload().getPersonalBizCodes();
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return null;
    }

    public static String getDisplayBizCodes() {
        String defaultKey = "displayBizCodes_";
        try {
            String key = OpenDelegateManager.getProcessDelegate()
                    .getDynamicResourceHistoryKeyGenerateRule()
                    .getKeyForDisplayBizList();
            key = UmsStringUtils.isBlank(key) ? "" : key.trim();
            String ret = OpenHistoryDataManager.getHistoryStringData(defaultKey
                    + key, DEFAULT_VALUE_NULLS);
            if (!DEFAULT_VALUE_NULLS.equals(ret))
                return ret;
            return OpenDynamicBizHistoryManager.getInstance()
                    .getResourcePreload().getDisplayBizCodes();
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return null;
    }

    public static String getLastDisplayBizCodes() {
        String defaultKey = "displayBizCodes_";
        try {
            String key = "last";
            String ret = OpenHistoryDataManager.getHistoryStringData(defaultKey
                    + key, DEFAULT_VALUE_NULLS);
            if (!DEFAULT_VALUE_NULLS.equals(ret))
                return ret;
            // 同预安装获取
            return OpenDynamicBizHistoryManager.getInstance()
                    .getResourcePreload().getDisplayBizCodes();
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return null;
    }

    public static String getDisplayAdsCodes() {
        String defaultKey = "displayAdsCodes_";
        try {
            String key = OpenDelegateManager.getProcessDelegate()
                    .getDynamicResourceHistoryKeyGenerateRule()
                    .getKeyForDisplayAdsBizList();
            key = UmsStringUtils.isBlank(key) ? "" : key.trim();
            String ret = OpenHistoryDataManager.getHistoryStringData(defaultKey
                    + key, DEFAULT_VALUE_NULLS);
            if (!DEFAULT_VALUE_NULLS.equals(ret))
                return ret;
            return OpenDynamicBizHistoryManager.getInstance()
                    .getResourcePreload().getDisplayAdsCodes();
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return null;
    }

    public static String getRecommendBizCodes() {
        String defaultKey = "recommendBizCodes_";
        try {
            String key = OpenDelegateManager.getProcessDelegate()
                    .getDynamicResourceHistoryKeyGenerateRule()
                    .getKeyForRecommendBizList();
            key = UmsStringUtils.isBlank(key) ? "" : key.trim();
            String ret = OpenHistoryDataManager.getHistoryStringData(defaultKey
                    + key, DEFAULT_VALUE_NULLS);
            if (!DEFAULT_VALUE_NULLS.equals(ret))
                return ret;
            return OpenDynamicBizHistoryManager.getInstance()
                    .getResourcePreload().getRecommendBizCodes();
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return null;
    }

    public static void setPersonalBizCodes(String personalBizCodes) {
        String defaultKey = "personalBizCodes_";
        try {
            String key = OpenDelegateManager.getProcessDelegate()
                    .getDynamicResourceHistoryKeyGenerateRule()
                    .getKeyForPersonalBizList();
            key = UmsStringUtils.isBlank(key) ? "" : key.trim();
            personalBizCodes = UmsStringUtils.isBlank(personalBizCodes) ? ""
                    : personalBizCodes.trim();
            OpenHistoryDataManager.setHistoryData(defaultKey + key,
                    personalBizCodes);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
    }

    public static void setDisplayBizCodes(String displayBizCodes) {
        String defaultKey = "displayBizCodes_";
        try {
            String key = OpenDelegateManager.getProcessDelegate()
                    .getDynamicResourceHistoryKeyGenerateRule()
                    .getKeyForDisplayBizList();
            key = UmsStringUtils.isBlank(key) ? "" : key.trim();
            displayBizCodes = UmsStringUtils.isBlank(displayBizCodes) ? ""
                    : displayBizCodes.trim();
            OpenHistoryDataManager.setHistoryData(defaultKey + key,
                    displayBizCodes);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
    }

    public static void setLastDisplayBizCodes(String lastDisplayBizCodes) {
        String defaultKey = "displayBizCodes_";
        try {
            String key = "last";
            lastDisplayBizCodes = UmsStringUtils.isBlank(lastDisplayBizCodes) ? ""
                    : lastDisplayBizCodes.trim();
            OpenHistoryDataManager.setHistoryData(defaultKey + key,
                    lastDisplayBizCodes);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
    }

    public static void setDisplayAdsCodes(String displayAdsCodes) {
        String defaultKey = "displayAdsCodes_";
        try {
            String key = OpenDelegateManager.getProcessDelegate()
                    .getDynamicResourceHistoryKeyGenerateRule()
                    .getKeyForDisplayAdsBizList();
            key = UmsStringUtils.isBlank(key) ? "" : key.trim();
            displayAdsCodes = UmsStringUtils.isBlank(displayAdsCodes) ? ""
                    : displayAdsCodes.trim();
            OpenHistoryDataManager.setHistoryData(defaultKey + key,
                    displayAdsCodes);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
    }

    public static void setRecommendBizCodes(String recommendBizCodes) {
        String defaultKey = "recommendBizCodes_";
        try {
            String key = OpenDelegateManager.getProcessDelegate()
                    .getDynamicResourceHistoryKeyGenerateRule()
                    .getKeyForRecommendBizList();
            key = UmsStringUtils.isBlank(key) ? "" : key.trim();
            recommendBizCodes = UmsStringUtils.isBlank(recommendBizCodes) ? ""
                    : recommendBizCodes.trim();
            OpenHistoryDataManager.setHistoryData(defaultKey + key,
                    recommendBizCodes);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
    }

}
