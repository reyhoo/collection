package com.chinaums.opensdk.manager;

import android.content.Context;

import com.chinaums.opensdk.activity.view.IDialog;
import com.chinaums.opensdk.cons.OpenEnv;
import com.chinaums.opensdk.cons.OpenEnvironment;
import com.chinaums.opensdk.download.process.ResourceManager;
import com.chinaums.opensdk.manager.OpenDelegateDefined.IProcessDelegate;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.weex.AppletManager;

public final class MyOpenManagerLoader {

    /**
     * managers
     */
    static private IOpenManager[] managers = null;

    /**
     * 是否初始化
     */
    static private boolean isInited = false;

    static synchronized public void initApp(Context context,
                                            OpenEnvironment currentEnvironment, Boolean isProdVerify,
                                            String backendEnvironment, IDialog dialog,
                                            IProcessDelegate processDelegate) {
        if (isInited)
            return;
        OpenEnv.setCurrentEnvironment(currentEnvironment);
        OpenEnv.setProdVerify(isProdVerify);
        OpenEnv.setBackendEnvironment(backendEnvironment);
        managers = new IOpenManager[]{
                OpenDelegateManager.getInstance(processDelegate),
                OpenHistoryDataManager.getInstance(),
                OpenEnvManager.getInstance(),
                OpenConfigManager.getInstance(),
                OpenExecutorManager.getInstance(),
                OpenEventManager.getInstance(),
                OpenDialogManager.getInstance(dialog),
                OpenDynamicFileManager.getInstance(),
                OpenDynamicBizHistoryManager.getInstance(),
                OpenDynamicSecurityManager.getInstance(),
                OpenDynamicWebProcessorManager.getInstance(),
                ResourceManager.getInstance(),
                AppletManager.getInstance()};
        for (IOpenManager manager : managers) {
            UmsLog.i("Initializing {}", manager.getClass().getName());
            manager.init(context);
        }
        isInited = true;
    }

    /**
     * 关闭
     */
    static synchronized public void destroy() {
        for (IOpenManager manager : managers) {
            UmsLog.i("Destroying {}", manager.getClass().getName());
            manager.destroy();
        }
        isInited = false;
    }

}
