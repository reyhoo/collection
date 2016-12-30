package com.example.administrator.bigfileupload;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Administrator on 2016/7/12.
 */
public class DialogUtil {
    private static MyProgressDialog loadingDialog;


    private static AlertDialog tipDialog;

    public static void showTipDialog(Activity activity,String title,String msg,final Runnable task){
        cancelTipDialog();
        if(activity == null || activity.isFinishing()){
            return;
        }
        tipDialog = new AlertDialog.Builder(activity).setMessage(msg)
                .setTitle(title).setNeutralButton("确定",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        task.run();
                    }
                }).create();
        tipDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                tipDialog = null;
            }
        });
        tipDialog.setCancelable(false);
        tipDialog.setCanceledOnTouchOutside(false);
        tipDialog.show();

    }
    public static void cancelTipDialog(){
        if(tipDialog == null)return;
        if(!((Activity)tipDialog.getContext()).isFinishing()){
            tipDialog.dismiss();
        }
        tipDialog = null;
    }

    public static void showLoading(Activity activity){
        showLoading(activity,true);
    }
    public static void showLoading(Activity activity,boolean canCancel){
        showLoading(activity,canCancel,"加载中...");
    }
    public static void showLoading(Activity activity,boolean canCancel,String msg){
        cancelLoading();
        if(activity == null || activity.isFinishing()){
            return;
        }
        loadingDialog = new MyProgressDialog(activity);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setCancelable(canCancel);
        loadingDialog.setMessage(msg);
        loadingDialog.show();
    }
    public static void cancelLoading(){
        if(loadingDialog == null)return;
        if(!loadingDialog.activity.isFinishing()){
            loadingDialog.dismiss();
        }
        loadingDialog = null;
    }


    private static class MyProgressDialog extends ProgressDialog{
        private boolean canCancel;
        Activity activity;
        public MyProgressDialog(Context context) {
            super(context);
            activity = (Activity)context;
        }

        @Override
        public void setCancelable(boolean canCancel) {
            super.setCancelable(canCancel);
            this.canCancel = canCancel;
        }

        @Override
        public void onBackPressed() {
            if(canCancel){
                cancelLoading();
//                activity.finish();
            }
        }
    }


    private static class MyTipDialog extends AlertDialog{
        private boolean canCancel;
        Activity activity;
        public MyTipDialog(Context context) {
            super(context);
            activity = (Activity)context;
        }

        @Override
        public void setMessage(CharSequence message) {
            super.setMessage(message);
        }

        @Override
        public void setButton(int whichButton, CharSequence text, OnClickListener listener) {
            super.setButton(whichButton, text, listener);
        }

        @Override
        public void setCancelable(boolean canCancel) {
            super.setCancelable(canCancel);
            this.canCancel = canCancel;

        }

        @Override
        public void onBackPressed() {
            if(canCancel){
                cancelLoading();
//                activity.finish();
            }
        }
    }


}
