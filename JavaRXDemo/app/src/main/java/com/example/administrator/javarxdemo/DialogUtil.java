package com.example.administrator.javarxdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.provider.Settings;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/29.
 */

public class DialogUtil {

    static MyProgressDialog mDialog;
    public static void showLoading(Activity activity, String msg, DialogInterface.OnDismissListener listener){


        mDialog = new MyProgressDialog(activity);
        mDialog.setIndeterminate(true);
        mDialog.setMessage(msg);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setOnDismissListener(listener);
        mDialog.show();
    }
    public static void dismissDialog(){
        if(mDialog == null)return;
        if(((Activity)mDialog.context).isFinishing())return;
        mDialog.dismiss();
        mDialog = null;
    }

    private static class MyProgressDialog extends ProgressDialog{

        private Context context;
        private DialogInterface.OnDismissListener listener;
        public MyProgressDialog(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public void onBackPressed() {
            dismissDialog();
            listener.onDismiss(this);
        }

        @Override
        public void setOnDismissListener(@Nullable OnDismissListener listener) {
            this.listener = listener;
        }
    }
}
