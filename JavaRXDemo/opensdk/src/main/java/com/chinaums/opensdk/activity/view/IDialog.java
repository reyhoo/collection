package com.chinaums.opensdk.activity.view;

import android.content.Context;
import android.view.View;

import com.chinaums.opensdk.load.view.AbsBizWebView.CallWeb;
import com.chinaums.opensdk.load.view.AbsBizWebView.DialogCallback;

import java.util.concurrent.atomic.AtomicInteger;


public interface IDialog {

    /**
     * 提示信息
     */
    public void showHint(Context context, String txt);

    /**
     * 有标题单按钮提示框
     */
    public void showDialogWithTitle(Context context, boolean canCancel,
                                    String title, String message, String[] btnNames,
                                    AtomicInteger callbackChoose, Runnable runnable);

    /**
     * 弹出等待框
     */
    public void showLoading(Context context, String str, boolean canCancel);

    /**
     * 关闭等待框
     */
    public void cancelLoading();


    public void showSelect(Context context, View parent, String[] choices,
                           int selectIndex, CallWeb callWeb);

    /**
     * 弹出密码键盘输入框
     *
     * @param context
     * @param securityKeyType 密钥类型，标示密码键盘初始化时使用的公钥所属平台。值如下：100001代表开放平台前置，100002代表账户前置。
     * @param inputMinLength  可允许输入的值的最小长度
     * @param inputMaxLength  可允许输入的值的最大长度
     * @param inputType       可允许输入的字符类型，是一个4位的字符串，每位代表一种类型是否可以输入。从左到右第一位代表是否接受数字，第二位代表是否接受字母，
     *                        第三位代表是否接受特殊字符（如.%!@等），第四位代表是否接受中文。如：1100代表接受数字和字母。
     * @param mainAccount     主账户             ，配合caclFactor使用。该内容可能是卡号，可能是账户号，根据securityKeyType和实际的需要设置。
     * @param calcFactor      计算因子             。0：不变换格式；1：不带主账号X9.8格式；2：带主账号的X9.8格式
     */
    public void showInputPinDialog(Context context, String securityKeyType,
                                   String inputMinLength, String inputMaxLength, String inputType,
                                   String mainAccount, String calcFactor, DialogCallback callback);

}
