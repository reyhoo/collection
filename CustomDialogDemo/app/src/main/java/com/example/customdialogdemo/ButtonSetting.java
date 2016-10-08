package com.example.customdialogdemo;

import android.view.View;

/**
 * Created by Administrator on 2016/5/28.
 */
public class ButtonSetting {

    private int buttonBgColor;
    private String buttonText;
    private View.OnClickListener  clickListener;


    public ButtonSetting (int buttonBgColor, String buttonText, View.OnClickListener  clickListener) {
        setButtonBgColor(buttonBgColor);
        setButtonText(buttonText);
        setClickListener(clickListener);
    }

    public View.OnClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public int getButtonBgColor() {
        return buttonBgColor;
    }

    public void setButtonBgColor(int buttonBgColor) {
        this.buttonBgColor = buttonBgColor;
    }
}
