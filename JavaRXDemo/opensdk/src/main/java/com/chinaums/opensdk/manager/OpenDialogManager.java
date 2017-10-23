package com.chinaums.opensdk.manager;

import android.content.Context;
import android.view.View;

import com.chinaums.opensdk.activity.view.IDialog;
import com.chinaums.opensdk.load.view.AbsBizWebView.CallWeb;
import com.chinaums.opensdk.load.view.AbsBizWebView.DialogCallback;
import com.chinaums.opensdk.net.ILoadingWidget;
import com.chinaums.opensdk.util.UmsLog;

import java.util.concurrent.atomic.AtomicInteger;

public class OpenDialogManager implements IOpenManager, IDialog, ILoadingWidget {

    /**
     * instance
     */
    private static OpenDialogManager instance;

    /**
     * dialog
     */
    private IDialog dialog;

    synchronized public static OpenDialogManager getInstance(IDialog dialog) {
        if (instance == null) {
            instance = new OpenDialogManager(dialog);
        }
        return instance;
    }

    synchronized public static OpenDialogManager getInstance() {
        return instance;
    }

    private OpenDialogManager(IDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void showHint(Context context, String txt) {
        try {
            dialog.showHint(context, txt);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
    }

    @Override
    public void showDialogWithTitle(Context context, boolean canCancel,
                                    String title, String message, String[] btnNames,
                                    AtomicInteger callbackChoose, Runnable runnable) {
        try {
            dialog.showDialogWithTitle(context, canCancel, title, message,
                    btnNames, callbackChoose, runnable);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
    }

    @Override
    public void cancelLoading() {
        try {
            dialog.cancelLoading();
        } catch (Exception e) {
            UmsLog.e("", e);
        }
    }

    @Override
    public void showSelect(Context context, View parent, String[] choices,
                           int selectIndex, CallWeb callWeb) {
        try {
            dialog.showSelect(context, parent, choices, selectIndex, callWeb);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
    }

    @Override
    public void showLoading(Context context, String str, boolean canCancel) {
        try {
            dialog.showLoading(context, str, canCancel);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
    }

    @Override
    public void showLoading(Context context) {
        showLoading(context, "努力加载中....", true);
    }

    @Override
    public void hideLoading(Context context) {
        cancelLoading();
    }

    @Override
    public void showInputPinDialog(Context context, String securityKeyType,
                                   String inputMinLength, String inputMaxLength, String inputType,
                                   String mainAccount, String calcFactor, DialogCallback callback) {
        try {
            dialog.showInputPinDialog(context, securityKeyType, inputMinLength,
                    inputMaxLength, inputType, mainAccount, calcFactor,
                    callback);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
    }

}
