package com.chinaums.opensdk.manager;

import android.content.Context;

import com.chinaums.opensdk.manager.OpenDelegateDefined.IProcessDelegate;


public class OpenDelegateManager implements IOpenManager {

    /**
     * instance
     */
    private static OpenDelegateManager instance;

    /**
     * processDelegate
     */
    private IProcessDelegate processDelegate;

    /**
     * context
     */
    private Context context;

    private OpenDelegateManager(IProcessDelegate processDelegate) {
        this.processDelegate = processDelegate;
    }

    synchronized public static OpenDelegateManager getInstance(
            IProcessDelegate processDelegate) {
        if (instance == null) {
            instance = new OpenDelegateManager(processDelegate);
        }
        return instance;
    }

    synchronized public static OpenDelegateManager getInstance() {
        return instance;
    }

    @Override
    public void init(Context context) {
        this.context = context;
    }

    static public String getPackageName() throws Exception {
        return getInstance().context.getPackageName();
    }

    static public IProcessDelegate getProcessDelegate() throws Exception {
        return getInstance().processDelegate;
    }

    @Override
    public void destroy() {
        context = null;
        processDelegate = null;
    }

}
