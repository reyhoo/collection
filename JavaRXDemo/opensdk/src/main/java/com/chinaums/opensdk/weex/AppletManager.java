package com.chinaums.opensdk.weex;

import android.content.Context;
import android.content.SharedPreferences;

import com.chinaums.opensdk.OpenSdkApplication;
import com.chinaums.opensdk.manager.IOpenManager;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.weex.adapter.DefaultWebSocketAdapterFactory;
import com.chinaums.opensdk.weex.adapter.ImageAdapter;
import com.chinaums.opensdk.weex.adapter.JSExceptionAdapter;
import com.chinaums.opensdk.weex.adapter.PlayDebugAdapter;
import com.chinaums.opensdk.weex.module.UmsAccelerometerModule;
import com.chinaums.opensdk.weex.module.UmsAnimationModule;
import com.chinaums.opensdk.weex.module.UmsBatteryModule;
import com.chinaums.opensdk.weex.module.UmsCameraModule;
import com.chinaums.opensdk.weex.module.UmsClipboardModule;
import com.chinaums.opensdk.weex.module.UmsCompassModule;
import com.chinaums.opensdk.weex.module.UmsDeviceModule;
import com.chinaums.opensdk.weex.module.UmsFileModule;
import com.chinaums.opensdk.weex.module.UmsGeolocationModule;
import com.chinaums.opensdk.weex.module.UmsGlobalEventModule;
import com.chinaums.opensdk.weex.module.UmsInstanceWrap;
import com.chinaums.opensdk.weex.module.UmsMediaModule;
import com.chinaums.opensdk.weex.module.UmsMetaModule;
import com.chinaums.opensdk.weex.module.UmsModalUIModule;
import com.chinaums.opensdk.weex.module.UmsNavigatorModule;
import com.chinaums.opensdk.weex.module.UmsNetworkModule;
import com.chinaums.opensdk.weex.module.UmsPickersModule;
import com.chinaums.opensdk.weex.module.UmsRecorderModule;
import com.chinaums.opensdk.weex.module.UmsScreenModule;
import com.chinaums.opensdk.weex.module.UmsStorageModule;
import com.chinaums.opensdk.weex.module.UmsTimerModule;
import com.chinaums.opensdk.weex.module.UmsVibrationModule;
import com.chinaums.opensdk.weex.module.UmsVolumeModule;
import com.chinaums.opensdk.weex.module.UmsWebSocketModule;
import com.chinaums.opensdk.weex.module.UmsWebViewModule;
import com.chinaums.opensdk.weex.module.sqlite.UmsSQLiteModule;
import com.chinaums.opensdk.weex.module.stream.UmsStreamModule;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;
import com.taobao.weex.common.WXModule;


public class AppletManager implements IOpenManager {

    /**
     * 单例
     */
    private static AppletManager instance = new AppletManager();

    /**
     * context
     */
    private Context context;

    public static AppletManager getInstance() {
        return instance;
    }

    @Override
    public void init(Context context) {
        if (context == null) {
            return;
        }
        this.context = context;
        ExecutorManager.getInstance().init(context);
        initWeex();
        UmsLog.d("AppletManager init success!");
    }

    @Override
    public void destroy() {
        SharedPreferences sp = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
        ExecutorManager.getInstance().destroy();
        UmsLog.d("AppletManager destory success!");
    }

    private void initWeex() {
        WXSDKEngine.initialize(OpenSdkApplication.getInstance(),
                new InitConfig.Builder()
                        .setImgAdapter(new ImageAdapter())
                        .setDebugAdapter(new PlayDebugAdapter())
                        .setWebSocketAdapterFactory(new DefaultWebSocketAdapterFactory())
                        .setJSExceptionAdapter(new JSExceptionAdapter())
                        .build()
        );
        registerModule("ums_animation", UmsAnimationModule.class);
        registerModule("ums_clipboard", UmsClipboardModule.class);
        registerModule("ums_globalEvent", UmsGlobalEventModule.class);
        registerModule("ums_instanceWrap", UmsInstanceWrap.class);
        registerModule("ums_meta", UmsMetaModule.class);
        registerModule("ums_modal", UmsModalUIModule.class, false);
        registerModule("ums_navigator", UmsNavigatorModule.class);
        registerModule("ums_picker", UmsPickersModule.class);
        registerModule("ums_storage", UmsStorageModule.class);
        registerModule("ums_stream", UmsStreamModule.class);
        registerModule("ums_timer", UmsTimerModule.class, false);
        registerModule("ums_webSocket", UmsWebSocketModule.class);
        registerModule("ums_webView", UmsWebViewModule.class);
        registerModule("ums_device", UmsDeviceModule.class);
        registerModule("ums_location", UmsGeolocationModule.class);
        registerModule("ums_battery", UmsBatteryModule.class);
        registerModule("ums_volume", UmsVolumeModule.class);
        registerModule("ums_vibrate", UmsVibrationModule.class);
        registerModule("ums_netstatus", UmsNetworkModule.class);
        registerModule("ums_compass", UmsCompassModule.class);
        registerModule("ums_accelerometer", UmsAccelerometerModule.class);
        registerModule("ums_recorder", UmsRecorderModule.class);
        registerModule("ums_screen", UmsScreenModule.class);
        registerModule("ums_file", UmsFileModule.class);
        registerModule("ums_camera", UmsCameraModule.class);
        registerModule("ums_media", UmsMediaModule.class);
        registerModule("ums_sqlite", UmsSQLiteModule.class);
    }

    /**
     * @param moduleName  模块名称
     * @param moduleClass Class 用于生成实例
     * @return 是否注册成功
     * @Created at 2017/6/9
     * @Developer Jiangbo
     * @Version 1.0
     * @Description 注册Weex模块
     */
    public boolean registerModule(String moduleName, Class<? extends WXModule> moduleClass) {
        return registerModule(moduleName, moduleClass, false);
    }

    /**
     * @param moduleName  模块名称
     * @param moduleClass Class 用于生成实例
     * @param global      是否注册时生成实例(建议填写false)
     * @return 是否注册成功
     * @Created at 2017/6/9
     * @Developer Jiangbo
     * @Version 1.0
     * @Description 注册Weex模块
     */

    public boolean registerModule(String moduleName, Class<? extends WXModule> moduleClass, boolean global) {
        try {
            return WXSDKEngine.registerModule(moduleName, moduleClass, global);
        } catch (WXException e) {
            e.printStackTrace();
            return false;
        }
    }

}
