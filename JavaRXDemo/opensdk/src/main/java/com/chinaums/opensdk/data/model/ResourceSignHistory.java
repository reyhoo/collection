package com.chinaums.opensdk.data.model;

import com.chinaums.opensdk.manager.OpenHistoryDataManager;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;

import java.io.Serializable;

public class ResourceSignHistory implements Serializable {

    private static final long serialVersionUID = -960773506445655294L;

    public static String getBizlistSign() {
        String defaultKey = "bizlistSign";
        try {
            return OpenHistoryDataManager.getHistoryStringData(defaultKey);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return null;
    }

    public static String getCategorySign() {
        String defaultKey = "categorySign";
        try {
            return OpenHistoryDataManager.getHistoryStringData(defaultKey);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return null;
    }

    public static String getAreaSign() {
        String defaultKey = "areaSign";
        try {
            return OpenHistoryDataManager.getHistoryStringData(defaultKey);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return null;
    }

    public static String getClientUpdateSign() {
        String defaultKey = "clientUpdateSign";
        try {
            return OpenHistoryDataManager.getHistoryStringData(defaultKey);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return null;
    }

    public static void setBizlistSign(String bizlistSign) {
        String defaultKey = "bizlistSign";
        try {
            bizlistSign = UmsStringUtils.isBlank(bizlistSign) ? ""
                    : bizlistSign.trim();
            OpenHistoryDataManager.setHistoryData(defaultKey, bizlistSign);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
    }

    public static void setCategorySign(String categorySign) {
        String defaultKey = "categorySign";
        try {
            categorySign = UmsStringUtils.isBlank(categorySign) ? ""
                    : categorySign.trim();
            OpenHistoryDataManager.setHistoryData(defaultKey, categorySign);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
    }

    public static void setAreaSign(String areaSign) {
        String defaultKey = "areaSign";
        try {
            areaSign = UmsStringUtils.isBlank(areaSign) ? "" : areaSign.trim();
            OpenHistoryDataManager.setHistoryData(defaultKey, areaSign);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
    }

    public static void setClientUpdateSign(String clientUpdateSign) {
        String defaultKey = "clientUpdateSign";
        try {
            clientUpdateSign = UmsStringUtils.isBlank(clientUpdateSign) ? ""
                    : clientUpdateSign.trim();
            OpenHistoryDataManager.setHistoryData(defaultKey, clientUpdateSign);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
    }

}
