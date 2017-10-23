package com.chinaums.opensdk.weex.module;

import android.text.TextUtils;

import com.chinaums.opensdk.util.UmsLog;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.ui.module.WXModalUIModule;


public class UmsModalUIModule extends WXModalUIModule {

    @JSMethod
    public void showLog(String msg) {
        if (TextUtils.isEmpty(msg)) {
            UmsLog.e("not print null message!");
        } else {
            UmsLog.i(msg);
        }
    }
}
