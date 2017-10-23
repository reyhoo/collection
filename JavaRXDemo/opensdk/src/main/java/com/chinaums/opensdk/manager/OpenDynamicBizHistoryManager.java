package com.chinaums.opensdk.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.OpenConst.DynamicBizHistory;
import com.chinaums.opensdk.data.model.ResourceListHistory;
import com.chinaums.opensdk.data.model.ResourcePreload;
import com.chinaums.opensdk.data.model.ResourceSignHistory;
import com.chinaums.opensdk.data.model.ResourceWorkspaceHistory;
import com.chinaums.opensdk.util.UmsFileUtils;
import com.chinaums.opensdk.util.UmsLog;

/**
 * 最近一次成功运行的上下文信息
 */
public class OpenDynamicBizHistoryManager implements IOpenManager {

    /**
     * instance
     */
    private static OpenDynamicBizHistoryManager instance;

    /**
     * defaultStdBizIcon
     */
    private static Bitmap defaultStdBizIcon = null;

    /**
     * defaultLargeBizIcon
     */
    private static Bitmap defaultLargeBizIcon = null;

    /**
     * defaultCategoryIcon
     */
    private static Bitmap defaultCategoryIcon = null;

    /**
     * resourcePreload
     */
    private static ResourcePreload resourcePreload = null;

    /**
     * resourceListHistory
     */
    private static ResourceListHistory resourceListHistory = null;

    /**
     * resourceSignHistory
     */
    private static ResourceSignHistory resourceSignHistory = null;

    /**
     * resourceWorkspaceHistory
     */
    private static ResourceWorkspaceHistory resourceWorkspaceHistory = null;

    synchronized public static OpenDynamicBizHistoryManager getInstance() {
        if (instance == null) {
            instance = new OpenDynamicBizHistoryManager();
        }
        return instance;
    }

    @Override
    public void init(Context context) {
        initDefaultBizIcon(context);
        initResourcePreload(context);
        resourceListHistory = new ResourceListHistory();
        resourceSignHistory = new ResourceSignHistory();
        resourceWorkspaceHistory = new ResourceWorkspaceHistory();
    }

    @Override
    public void destroy() {

    }

    private void initDefaultBizIcon(Context context) {
        try {
            byte[] stdIconData = UmsFileUtils.readStream(context.getAssets()
                    .open(DynamicBizHistory.DEFAULT_BIZ_STD_ICON_FILE_PATH));
            byte[] largeIconData = UmsFileUtils.readStream(context.getAssets()
                    .open(DynamicBizHistory.DEFAULT_BIZ_LARGE_ICON_FILE_PATH));
            byte[] categoryIconData = UmsFileUtils
                    .readStream(context
                            .getAssets()
                            .open(DynamicBizHistory.DEFAULT_CATEGORY_STD_ICON_FILE_PATH));
            defaultStdBizIcon = BitmapFactory.decodeByteArray(stdIconData, 0,
                    stdIconData.length);
            defaultLargeBizIcon = BitmapFactory.decodeByteArray(largeIconData,
                    0, largeIconData.length);
            defaultCategoryIcon = BitmapFactory.decodeByteArray(
                    categoryIconData, 0, categoryIconData.length);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
    }

    private void initResourcePreload(Context context) {
        try {
            resourcePreload = null;
            String preloadJsonStr = UmsFileUtils.readStream2String(context
                    .getAssets().open(
                            DynamicBizHistory.PRELOAD_PROPERTIES_FILE_PATH));
            JSONObject jsonObject = JSONObject.parseObject(preloadJsonStr);
            String personalBizCodes = jsonObject
                    .getString(DynamicBizHistory.PRELOAD_PROPERTIES_KEY_PERSONAL_BIZ_CODES);
            String displayBizCodes = jsonObject
                    .getString(DynamicBizHistory.PRELOAD_PROPERTIES_KEY_DISPLAY_BIZ_CODES);
            String displayAdsCodes = jsonObject
                    .getString(DynamicBizHistory.PRELOAD_PROPERTIES_KEY_DISPLAY_ADS_CODES);
            String recommendBizCodes = jsonObject
                    .getString(DynamicBizHistory.PRELOAD_PROPERTIES_KEY_RECOMMEND_BIZ_CODES);
            String bizlistSignData = jsonObject
                    .getString(DynamicBizHistory.PRELOAD_PROPERTIES_KEY_SIGN_BIZLIST);
            String categorySignData = jsonObject
                    .getString(DynamicBizHistory.PRELOAD_PROPERTIES_KEY_SIGN_CATEGORY);
            String areaSignData = jsonObject
                    .getString(DynamicBizHistory.PRELOAD_PROPERTIES_KEY_SIGN_AREA);
            String clientUpdateSignData = jsonObject
                    .getString(DynamicBizHistory.PRELOAD_PROPERTIES_KEY_SIGN_CLIENT_UPDATE);
            resourcePreload = new ResourcePreload(personalBizCodes,
                    displayBizCodes, displayAdsCodes, recommendBizCodes,
                    bizlistSignData, categorySignData, areaSignData,
                    clientUpdateSignData);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
    }

    public Bitmap getDefaultStdBizIcon() {
        return defaultStdBizIcon;
    }

    public Bitmap getDefaultLargeBizIcon() {
        return defaultLargeBizIcon;
    }

    public Bitmap getDefaultCategoryIcon() {
        return defaultCategoryIcon;
    }

    public ResourcePreload getResourcePreload() {
        return resourcePreload;
    }

    public ResourceListHistory getResourceListHistory() {
        return resourceListHistory;
    }

    public ResourceSignHistory getResourceSignHistory() {
        return resourceSignHistory;
    }

    public ResourceWorkspaceHistory getResourceWorkspaceHistory() {
        return resourceWorkspaceHistory;
    }
    
}
