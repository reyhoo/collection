package com.chinaums.opensdk.load.process;

import android.content.Intent;

import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;

/**
 * 刷新网页的API
 */
public class SystemRefreshWebViewProcessor extends AbsStdDynamicProcessor {

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception {

    }

    @Override
    public int getType() {
        return DynamicProcessorType.SYSTEM_REFRESH_WEBVIEW;
    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.ASYNCHRONIZED;
    }

    @Override
    protected void execute(final DynamicWebModel model) throws Exception {
        model.getHandler().post(new Runnable() {
            public void run() {
                model.getWebView().reload();
            }
        });
    }

}
