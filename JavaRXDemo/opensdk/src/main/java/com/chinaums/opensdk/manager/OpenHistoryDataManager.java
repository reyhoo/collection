package com.chinaums.opensdk.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 客户端历史记录
 */
public class OpenHistoryDataManager implements IOpenManager {

    private static final String SP_NAME = "historyData";

    /**
     * sp
     */
    private static SharedPreferences sp;

    /**
     * editor
     */
    private static Editor editor;

    /**
     * instance
     */
    private static OpenHistoryDataManager instance;

    synchronized public static OpenHistoryDataManager getInstance() {
        if (instance == null) {
            instance = new OpenHistoryDataManager();
        }
        return instance;
    }

    @Override
    public void init(Context context) {
        sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    @Override
    public void destroy() {

    }

    public static <T> void setHistoryData(String key, T value) {
        if (value instanceof String) {
            editor.putString(key, String.valueOf(value)).commit();
            return;
        }
        if (value instanceof Integer) {
            editor.putInt(key, (Integer) value).commit();
            return;
        }
        throw new UnknownError("无法修改该类型数据");
    }

    /**
     * 通过Key获取历史数据，若查询失败，返回"";
     */
    public static String getHistoryStringData(String key) {
        return sp.getString(key, "");
    }

    /**
     * 通过Key获取历史数据，若查询失败，返回 defaultValue;
     */
    public static String getHistoryStringData(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    /**
     * 通过Key获取历史数据，若查询失败，返回0;
     */
    public static int getHistoryIntData(String key) {
        return sp.getInt(key, 0);
    }

    /**
     * 删除一条历史记录
     */
    public static void deleteHistoryData(String key) {
        editor.remove(key).commit();
    }

}
