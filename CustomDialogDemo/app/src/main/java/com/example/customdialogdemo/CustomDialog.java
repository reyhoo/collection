package com.example.customdialogdemo;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/5/27.
 */
public class CustomDialog extends Dialog {

    public static final int BUTTON_TYPE_SINGLE = 1;
    public static final int BUTTON_TYPE_NO = 0;
    public static final int BUTTON_TYPE_TWO = 2;


    public static final int leftButtonId = R.id.leftButton;
    public static final int rightButtonId = R.id.rightButton;
    private TextView msgTv;
    private Button leftButton;
    private Button rightButton;
    private LinearLayout buttonLayout;
    private int buttonType = -1;
    private String msg = "";
    private ButtonSetting leftButtonSetting;
    private ButtonSetting rightButtonSetting;

    public CustomDialog(Context context, int type, ButtonSetting leftButtonSetting, ButtonSetting rightButtonSetting) {
        super(context, R.style.CustomDialogStyle);
        buttonType = type;
        this.leftButtonSetting = leftButtonSetting;
        this.rightButtonSetting = rightButtonSetting;
    }

    @Deprecated
    public void show() {
//        super.show();
        String s = null;
        if(s == null){
            throw new RuntimeException("must have a string arg");
        }
    }

    public void show(String msg) {
        this.msg = msg;
        if(msgTv!=null){
            msgTv.setText(msg);
        }
        super.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom);
        Window window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        onWindowAttributesChanged(wl);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        msgTv = (TextView) findViewById(R.id.msgTv);
        msgTv.setText(msg);
        leftButton = (Button) findViewById(R.id.leftButton);
        rightButton = (Button) findViewById(R.id.rightButton);
        buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        switch (buttonType) {
            case BUTTON_TYPE_SINGLE:
                buttonLayout.setVisibility(View.VISIBLE);
                rightButton.setVisibility(View.GONE);
                updateButton();
                break;
            case BUTTON_TYPE_NO:
                buttonLayout.setVisibility(View.GONE);
                findViewById(R.id.buttonDiverView).setVisibility(View.GONE);
                break;
            case BUTTON_TYPE_TWO:
                buttonLayout.setVisibility(View.VISIBLE);
                rightButton.setVisibility(View.VISIBLE);
                updateButton();
                break;
        }
    }

    private void updateButton() {
        if(leftButtonSetting!=null){
            leftButton.setText(leftButtonSetting.getButtonText());
            leftButton.setOnClickListener(leftButtonSetting.getClickListener());
            GradientDrawable myGrad = (GradientDrawable)leftButton.getBackground();
            myGrad.setColor(leftButtonSetting.getButtonBgColor());
        }
        if(rightButtonSetting!=null){
            rightButton.setText(rightButtonSetting.getButtonText());
            rightButton.setOnClickListener(rightButtonSetting.getClickListener());
            GradientDrawable myGrad = (GradientDrawable)rightButton.getBackground();
            myGrad.setColor(rightButtonSetting.getButtonBgColor());
        }
    }
}
