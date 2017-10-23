package com.chinaums.opensdk.util;

import android.text.Html;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

public class JsonUtils {

    static private Gson gson = new Gson();

    /**
     * @param jsonStr
     * @return
     */
    public static JSONObject convert2Json(String jsonStr) {
        return JSON.parseObject(jsonStr);
    }

    /**
     * @param object
     * @return
     */
    public static JSONObject convert2Json(Object object) {
        return convert2Json(gson.toJson(object));
    }

    /**
     * @param root
     * @param key
     * @return
     */
    public static int getInt(JSONObject root, String key) {
        if (!root.containsKey(key)) {
            return 0;
        } else {
            return root.getIntValue(key);
        }
    }

    /**
     * @param root
     * @param key
     * @return
     */
    public static String getString(JSONObject root, String key) {
        if (!root.containsKey(key)) {
            return "";
        } else {
            if (key.equals("memo")) {
                return root.getString(key);
            }
            return Html.fromHtml(root.getString(key)).toString();
        }
    }

    /**
     * @param root
     * @param key
     * @return
     */
    public static double getDouble(JSONObject root, String key) {
        if (!root.containsKey(key)) {
            return 0;
        } else {
            return root.getDouble(key);
        }
    }

    /**
     * @param root
     * @param key
     * @return
     */
    public static JSONArray getJsonArray(JSONObject root, String key) {
        return root.getJSONArray(key);
    }

    /**
     * @param root
     * @param key
     * @return
     */
    public static boolean getBoolean(JSONObject root, String key) {
        String str = getString(root, key);
        if (str.equals("true"))
            return true;
        else
            return false;
    }

    /**
     * @param jsonStr
     * @return
     */
    public static JSONObject getJsonObject(String jsonStr) {
        return JSON.parseObject(jsonStr);
    }

    /**
     * @param json
     * @param clz
     * @return
     */
    public static <T extends Object> T fromJsonString(String json, Class<T> clz) {
        T resp = gson.fromJson(json, clz);
        return resp;
    }

    /**
     * @param obj
     * @return
     * @author wxtang 2015-6-12下午7:02:50
     */
    public static String convertObject2String(Object obj) {
        return gson.toJson(obj);
    }

    /**
     * @param jsonString1
     * @param jsonString2
     * @return
     */
    public static JSONObject mergeJSONString(String jsonString1,
                                             String jsonString2) {
        try {
            if (UmsStringUtils.isBlank(jsonString1)) {
                return JSON.parseObject(jsonString2);
            }
            if (UmsStringUtils.isBlank(jsonString2)) {
                return JSON.parseObject(jsonString1);
            }
            JSONObject reusltJSON = JSON.parseObject(jsonString1);
            reusltJSON.putAll(JSON.parseObject(jsonString2));
            return reusltJSON;
        } catch (Exception e) {
            UmsLog.e("", e);
            return new JSONObject();
        }
    }

}
