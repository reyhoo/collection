package com.uidemo;

import android.app.Application;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uidemo.fragment.CashierFragment;
import com.uidemo.fragment.MerchantServiceFragment;
import com.uidemo.fragment.MineFragment;
import com.uidemo.fragment.SelectedAppFragment;

/**
 * Created by Administrator on 2016/10/18.
 */

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private int mBottomLayoutHeight;//98


    private LinearLayout bottomLayout;

    private CashierFragment mCashierFragment;
    private MerchantServiceFragment mMerchantServiceFragment;
    private MineFragment mMineFragment;
    private SelectedAppFragment mSelectedAppFragment;

    private FragmentManager mFragmentManager;


    private int[] titleRes = {R.string.mposHomePageCheckOut, R.string.mposHomePageMerService,
            R.string.mposHomePageSelectedApp, R.string.mposHomePageMine};

    private LinearLayout[] mTabBtns = new LinearLayout[4];
    private ImageView[] mTabIvs = new ImageView[4];
    private TextView[] mTabTvs = new TextView[4];


    private int mCurrPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        computeSize();
        setupView();
    }

    private void setupView() {
        initTitleView(R.drawable.title_trans_query_icon_selector,"交易查询");
        mTitleRightIcon.setVisibility(View.VISIBLE);
        mTitleRightIcon.setImageResource(R.drawable.title_tongzhi_icon_selector);
        mFragmentManager = getFragmentManager();
        bottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
        mTabBtns[0] = (LinearLayout) findViewById(R.id.tab_0_btn);
        mTabBtns[1] = (LinearLayout) findViewById(R.id.tab_1_btn);
        mTabBtns[2] = (LinearLayout) findViewById(R.id.tab_2_btn);
        mTabBtns[3] = (LinearLayout) findViewById(R.id.tab_3_btn);

        mTabIvs[0] = (ImageView) findViewById(R.id.tab_0_iv);
        mTabIvs[1] = (ImageView) findViewById(R.id.tab_1_iv);
        mTabIvs[2] = (ImageView) findViewById(R.id.tab_2_iv);
        mTabIvs[3] = (ImageView) findViewById(R.id.tab_3_iv);

        mTabTvs[0] = (TextView) findViewById(R.id.tab_0_tv);
        mTabTvs[1] = (TextView) findViewById(R.id.tab_1_tv);
        mTabTvs[2] = (TextView) findViewById(R.id.tab_2_tv);
        mTabTvs[3] = (TextView) findViewById(R.id.tab_3_tv);

        setLeftBtnOnClickListener(this);
        setRightBtnOnClickListener(this);
        mTabBtns[0].setOnClickListener(this);
        mTabBtns[1].setOnClickListener(this);
        mTabBtns[2].setOnClickListener(this);
        mTabBtns[3].setOnClickListener(this);


        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bottomLayout.getLayoutParams();
        params.height = mBottomLayoutHeight;
        bottomLayout.setLayoutParams(params);

        mCashierFragment = new CashierFragment();
        mSelectedAppFragment = new SelectedAppFragment();
        mMerchantServiceFragment = new MerchantServiceFragment();
        mMineFragment = new MineFragment();
        selectFragment(0);
    }

    private void computeSize() {
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int screenHeight = outMetrics.heightPixels;
        mBottomLayoutHeight = 98 * screenHeight / 1334;
    }


    private void selectFragment(int position) {
        if (position == mCurrPosition) {
            return;
        }
        mCurrPosition = position;
        updateViewOtherView(position);
        FragmentTransaction tr = mFragmentManager.beginTransaction();
//        hideAllFragment(tr);
//        tr.setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out);
        switch (position) {
            case 0:
                tr.replace(R.id.fragment_container, mCashierFragment);
//                tr.addToBackStack(null);
//                if (mCashierFragment == null) {
//                    mCashierFragment = new CashierFragment();
//                    tr.add(R.id.fragment_container, mCashierFragment);
//                } else {
//                    tr.show(mCashierFragment);
//                }
                break;
            case 1:
                tr.replace(R.id.fragment_container, mMerchantServiceFragment);
//                tr.addToBackStack(null);
//                if (mMerchantServiceFragment == null) {
//                    mMerchantServiceFragment = new MerchantServiceFragment();
//                    tr.add(R.id.fragment_container, mMerchantServiceFragment);
//                } else {
//                    tr.show(mMerchantServiceFragment);
//                }
                break;
            case 2:
                tr.replace(R.id.fragment_container, mSelectedAppFragment);
//                tr.addToBackStack(null);
//                if (mSelectedAppFragment == null) {
//                    mSelectedAppFragment = new SelectedAppFragment();
//                    tr.add(R.id.fragment_container, mSelectedAppFragment);
//                } else {
//                    tr.show(mSelectedAppFragment);
//                }
                break;
            case 3:
                tr.replace(R.id.fragment_container, mMineFragment);
//                tr.addToBackStack(null);
//                if (mMineFragment == null) {
//                    mMineFragment = new MineFragment();
//                    tr.add(R.id.fragment_container, mMineFragment);
//                } else {
//                    tr.show(mMineFragment);
//                }
                break;
        }
        tr.commit();

    }

    private void updateViewOtherView(int position) {
        setMyTitle(titleRes[position]);
        for (int i = 0; i < mTabIvs.length; i++) {
            if (position == i) {
                mTabIvs[i].setEnabled(true);
                mTabTvs[i].setTextColor(Color.parseColor("#ff6050"));
            } else {
                mTabIvs[i].setEnabled(false);
                mTabTvs[i].setTextColor(Color.parseColor("#bbbbbb"));
            }
        }

    }

    private void hideAllFragment(FragmentTransaction transaction) {
        if (mCashierFragment != null) {
            transaction.hide(mCashierFragment);
        }
        if (mMineFragment != null) {
            transaction.hide(mMineFragment);
        }
        if (mSelectedAppFragment != null) {
            transaction.hide(mSelectedAppFragment);
        }
        if (mMerchantServiceFragment != null) {
            transaction.hide(mMerchantServiceFragment);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case sTitleLeftBtnId:
                Toast.makeText(getApplicationContext(), "title_right_btn", Toast.LENGTH_SHORT).show();
                break;
            case sTitleRightBtnId:
                Toast.makeText(getApplicationContext(), "title_left_btn", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tab_0_btn:
                selectFragment(0);
                break;
            case R.id.tab_1_btn:
                selectFragment(1);
                break;
            case R.id.tab_2_btn:
                selectFragment(2);
                break;
            case R.id.tab_3_btn:
                selectFragment(3);
                break;
        }
    }
}
