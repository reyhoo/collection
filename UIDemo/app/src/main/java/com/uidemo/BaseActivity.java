package com.uidemo;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/10/19.
 */

public abstract class BaseActivity extends Activity {


    private int leftIconSampleHeight = 28;
    private int leftIconSampleWidth = 16;
    private int rightIconSampleHeight = 32;
    private int rightIconSampleWidth = 28;
    private int rightLayoutSimpleWidth = 90;
    private int rightLayoutSimpleHeight= 44;


    protected int mTitleLayoutHeight;//88
    protected int mTitleLeftIconHeight;//30
    protected int mTitleRightLayoutHeight;//44
    protected int mTitleRightLayoutWidth;//90
    protected int mTitleRightIconHeight;//32
    protected int mTitleMargin1;//36//左右两边的margin
    protected int mTitleMargin2;//12 //左边图和字的间距

    protected RelativeLayout mTitleLayout;

    protected ImageView mTitleLeftIcon;
    protected TextView mTitleLeftTv;
    protected LinearLayout mTitleLeftBtn;

    protected LinearLayout mTitleRightBtn;
    protected TextView mTitleRightTv;
    protected ImageView mTitleRightIcon;
    protected RelativeLayout mTitleRightLayout;


    protected TextView mTitleTv;

    protected View mRedPoint;
    private Activity instance;

    protected static final int sTitleLeftBtnId = R.id.title_left_btn;
    protected static final int sTitleRightBtnId = R.id.title_right_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        instance = this;
        if (instance instanceof HomeActivity) {
            leftIconSampleHeight = 30;
            leftIconSampleWidth = 30;
        }
        computeSize();
    }

    protected final void initTitleView(int leftIcon, String leftTxt) {
        mTitleLayout = (RelativeLayout) findViewById(R.id.title_main_layout);
        mTitleLeftIcon = (ImageView) findViewById(R.id.title_left_iv);
        mTitleLeftBtn = (LinearLayout) findViewById(R.id.title_left_btn);
        mTitleLeftTv = (TextView) findViewById(R.id.title_left_tv);


        mTitleRightBtn = (LinearLayout) findViewById(R.id.title_right_btn);

        mTitleRightIcon = (ImageView) findViewById(R.id.title_right_iv);
        mTitleRightTv = (TextView) findViewById(R.id.title_right_tv);
        mTitleRightLayout = (RelativeLayout) findViewById(R.id.title_right_layout);
        mRedPoint = findViewById(R.id.title_point);
        mTitleTv = (TextView) findViewById(R.id.title_tv);

        mTitleLeftIcon.setImageResource(leftIcon);
        mTitleLeftTv.setText(leftTxt);
        mRedPoint.setVisibility(View.GONE);
        reLayoutTitle();
    }

    private void reLayoutTitle() {
//        if (mTitleLayout != null) {
//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTitleLayout.getLayoutParams();
//            params.height = mTitleLayoutHeight;
//            mTitleLayout.setLayoutParams(params);
//        }
//        if (mTitleLeftIcon != null) {
//
//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTitleLeftIcon.getLayoutParams();
//            params.height = mTitleLeftIconHeight;
//            params.width = leftIconSampleWidth * mTitleLeftIconHeight / leftIconSampleHeight;
//            mTitleLeftIcon.setLayoutParams(params);
//        }
//
//        if (mTitleLeftTv != null) {
//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTitleLeftTv.getLayoutParams();
//            params.leftMargin = mTitleMargin2;
//            mTitleLeftTv.setLayoutParams(params);
//        }
//
//        if (mTitleLeftBtn != null) {
//            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) mTitleLeftBtn.getLayoutParams();
//            params1.leftMargin = mTitleMargin1 * 4 / 6;
//
//            mTitleLeftBtn.setLayoutParams(params1);
//
//            mTitleLeftBtn.setPadding(mTitleMargin1 * 2 / 6, mTitleMargin1 * 2 / 6,
//                    mTitleMargin1 * 2 / 6, mTitleMargin1* 2  / 6);
//        }
//
//
//        if (mTitleRightBtn != null) {
//            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) mTitleRightBtn.getLayoutParams();
//            params1.rightMargin = mTitleMargin1 * 4 / 6;
//            mTitleRightBtn.setLayoutParams(params1);
//            mTitleRightBtn.setPadding(mTitleMargin1 * 2 / 6, mTitleMargin1 * 2 / 6,
//                    mTitleMargin1 * 2 / 6, mTitleMargin1* 2  / 6);
//        }
//
//        if (mTitleRightIcon != null) {
//            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) mTitleRightIcon.getLayoutParams();
//            params1.height = mTitleRightIconHeight;
//            params1.width = rightIconSampleWidth * mTitleRightIconHeight / rightIconSampleHeight;
//            mTitleRightIcon.setLayoutParams(params1);
//        }
        if (mTitleRightLayout != null) {
            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) mTitleRightLayout.getLayoutParams();
            params1.height = mTitleRightLayoutHeight;
            params1.width = mTitleRightLayoutWidth;
            mTitleRightLayout.setLayoutParams(params1);
            GradientDrawable drawable = (GradientDrawable) getResources().getDrawable(R.drawable.app_recommend_add_press_bg);
            drawable.setCornerRadius(mTitleRightLayoutHeight * 1f / 2);
            mTitleRightLayout.setBackgroundDrawable(drawable);
        }

    }

    private void computeSize() {
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int screenWidth = outMetrics.widthPixels;
        int screenHeight = outMetrics.heightPixels;
        mTitleLayoutHeight = 88 * screenHeight / 1334;
        mTitleLeftIconHeight = leftIconSampleHeight * screenHeight / 1334;
        mTitleRightIconHeight = rightIconSampleHeight * screenHeight / 1334;
        mTitleMargin1 = 36 * screenWidth / 750;
        mTitleMargin2 = 12 * screenWidth / 750;
        mTitleRightLayoutHeight = rightLayoutSimpleHeight * screenWidth / 750;
        mTitleRightLayoutWidth = rightLayoutSimpleWidth * screenWidth / 750;
    }

    protected final void setMyTitle(String txt) {
        if (mTitleTv == null) return;
        mTitleTv.setText(txt);
    }

    protected final void setMyTitle(int resId) {
        setMyTitle(getString(resId));
    }

    protected final void setLeftBtnOnClickListener(View.OnClickListener listener) {
        if (mTitleLeftBtn != null) {
            mTitleLeftBtn.setOnClickListener(listener);
        }
    }

    protected final void setRightBtnOnClickListener(View.OnClickListener listener) {
        if (mTitleRightBtn != null) {
            mTitleRightBtn.setOnClickListener(listener);
        }
    }

}
