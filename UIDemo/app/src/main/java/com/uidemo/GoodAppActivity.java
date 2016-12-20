package com.uidemo;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/17.
 */

public class GoodAppActivity extends Activity implements View.OnClickListener {
    private int mGroupTitleHeight;// 86
    private int mCellHeight;// 180
    private int mRecommendItemHeight;//126
    private int mItemAddBtnHeight;//50

    private LinearLayout mMainLayout;


    private LayoutInflater mInflater;

    @Override
    public void onClick(View v) {

    }

    public class CellBean {
        String name;
        String desc;
        int resId;
    }


    private ArrayList<GoodAppActivity.CellBean> mMyAppList;
    private ArrayList<GoodAppActivity.CellBean> mRecommendList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_selected_app);
        computeSize();
        initView();
        myAppTestData();
        recommendTestData();
        updateView();
    }

    private void recommendTestData() {
        mRecommendList = new ArrayList<>();
        GoodAppActivity.CellBean cellBean = new GoodAppActivity.CellBean();
        cellBean.name = "余额查询";
        cellBean.desc = "余额查询余额查询余额查询余额查询余额查询余额查询余额查询。";
        cellBean.resId = R.drawable.app_balance_inquery;
        mRecommendList.add(cellBean);

        cellBean = new GoodAppActivity.CellBean();
        cellBean.name = "免费招聘";
        cellBean.desc = "余额查询余额查询余额查询余额查询余额查询余额查询余额查询。";
        cellBean.resId = R.drawable.app_mianfeizhaopingi;
        mRecommendList.add(cellBean);


        cellBean = new GoodAppActivity.CellBean();
        cellBean.name = "我的招聘";
        cellBean.desc = "余额查询余额查询余额查询余额查询余额查询余额查询余额查询。";
        cellBean.resId = R.drawable.app_wodezhaoping;
        mRecommendList.add(cellBean);

        cellBean = new GoodAppActivity.CellBean();
        cellBean.name = "餐饮ERP";
        cellBean.desc = "余额查询余额查询余额查询余额查询余额查询余额查询余额查询。";
        cellBean.resId = R.drawable.app_canyingerp;
        mRecommendList.add(cellBean);
    }


    private void updateView() {
        inputGroupLayout((LinearLayout) mMainLayout.getChildAt(0), mMyAppList);

//        for (int i = 0; i < mList.size(); i++) {
//            AppManageActivity.GroupBean groupBean = mList.get(i);
//            LinearLayout groupLayout = (LinearLayout) mInflater.inflate(
//                    R.layout.app_manage_group, null);
//            LinearLayout titleLayout = ((LinearLayout) groupLayout
//                    .getChildAt(0));
//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) titleLayout
//                    .getLayoutParams();
//            params.height = mGroupTitleHeight;
//            titleLayout.requestLayout();
//            TextView tv1 = (TextView) titleLayout.getChildAt(0);
//            TextView tv2 = (TextView) titleLayout.getChildAt(1);
//            tv1.setText(groupBean.title);
//            tv2.setText("");
//            inputGroupLayout(groupLayout, groupBean.list);
//            mMainLayout.addView(groupLayout);
//        }

        LinearLayout recommendLayout = (LinearLayout) mMainLayout.getChildAt(1);

        inputRecommendLayout(recommendLayout,mRecommendList);



    }



    private void inputRecommendLayout(LinearLayout listLayout,ArrayList<GoodAppActivity.CellBean> recommendList){
        if (recommendList == null || recommendList.isEmpty()) {
            return;
        }
        for (int i = 0; i < recommendList.size(); i ++ ){

            CellBean cellBean = recommendList.get(i);
            final LinearLayout itemLayout = (LinearLayout) mInflater.inflate(
                    R.layout.recommend_app_item, null);
            listLayout.addView(itemLayout);
            ImageView iconIv = (ImageView) itemLayout.findViewById(R.id.item_icon_iv);
            TextView nameTv = (TextView) itemLayout.findViewById(R.id.item_name_tv);
            TextView DescTv = (TextView) itemLayout.findViewById(R.id.item_desc_tv);
            iconIv.setImageResource(cellBean.resId);
            nameTv.setText(cellBean.name);
            DescTv.setText(cellBean.desc);

            itemLayout.setOnClickListener(this);
            itemLayout.findViewById(R.id.item_add_btn).setOnClickListener(this);
            reLayoutReCommendItem(itemLayout);
        }
    }



    private void inputGroupLayout(LinearLayout groupLayout,
                                  ArrayList<GoodAppActivity.CellBean> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        int rowSize = list.size() % 4 == 0 ? list.size() / 4
                : list.size() / 4 + 1;
        ArrayList<LinearLayout> cellLayouts = new ArrayList<LinearLayout>();
        for (int i = 0; i < rowSize; i++) {
            LinearLayout rowLayout = (LinearLayout) mInflater.inflate(
                    R.layout.app_manage_row, null);
            groupLayout.addView(rowLayout);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rowLayout
                    .getLayoutParams();
            params.height = mCellHeight;
            rowLayout.requestLayout();
            for (int j = 0; j < rowLayout.getChildCount(); j++) {
                LinearLayout layout = (LinearLayout) rowLayout.getChildAt(j);
                cellLayouts.add(layout);
            }
        }
        inputCellLayout(cellLayouts, list);
    }


    private void inputCellLayout(ArrayList<LinearLayout> cellLayouts,
                                 ArrayList<GoodAppActivity.CellBean> list) {
        for (int i = 0; i < list.size(); i++) {
            GoodAppActivity.CellBean cellBean = list.get(i);
            LinearLayout cellLayout = cellLayouts.get(i);
            cellLayout.setOnClickListener(this);
            cellLayout.setTag(cellBean.name);
            cellLayout.setVisibility(View.VISIBLE);
            TextView tv = (TextView) cellLayout.findViewById(R.id.cell_tv);
            ImageView iv = (ImageView) cellLayout.findViewById(R.id.cell_iv);
            tv.setText(cellBean.name);
            iv.setImageResource(cellBean.resId);
        }
    }


    private void myAppTestData() {
        mMyAppList = new ArrayList<>();

        GoodAppActivity.CellBean cellBean = new GoodAppActivity.CellBean();
        cellBean.name = "湖南人保";
        cellBean.resId = R.drawable.app_hunanrenbao;
        mMyAppList.add(cellBean);

        cellBean = new GoodAppActivity.CellBean();
        cellBean.name = "湖南助农取款";
        cellBean.resId = R.drawable.app_hunanzhumingshoukuan;
        mMyAppList.add(cellBean);

        cellBean = new GoodAppActivity.CellBean();
        cellBean.name = "实时转账";
        cellBean.resId = R.drawable.app_real_time_transfer;
        mMyAppList.add(cellBean);

        cellBean = new GoodAppActivity.CellBean();
        cellBean.name = "易POS管理";
        cellBean.resId = R.drawable.app_mpos_manage;
        mMyAppList.add(cellBean);

        cellBean = new GoodAppActivity.CellBean();
        cellBean.name = "余额查询";
        cellBean.resId = R.drawable.app_balance_inquery;
        mMyAppList.add(cellBean);

        cellBean = new GoodAppActivity.CellBean();
        cellBean.name = "广告发布";
        cellBean.resId = R.drawable.app_adver_send;
        mMyAppList.add(cellBean);

        cellBean = new GoodAppActivity.CellBean();
        cellBean.name = "餐饮ERP";
        cellBean.resId = R.drawable.app_canyingerp;
        mMyAppList.add(cellBean);

        cellBean = new GoodAppActivity.CellBean();
        cellBean.name = "全部";
        cellBean.resId = R.drawable.app_all;
        mMyAppList.add(cellBean);


    }

    private void initView() {
        mInflater = getLayoutInflater();
        mMainLayout = (LinearLayout) findViewById(R.id.main_layout);
        LinearLayout titleLayout = (LinearLayout) ((LinearLayout) mMainLayout.getChildAt(0))
                .getChildAt(0);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) titleLayout
                .getLayoutParams();
        params.height = mGroupTitleHeight;
        titleLayout.requestLayout();

        TextView tv1 = (TextView) titleLayout.getChildAt(0);
        TextView tv2 = (TextView) titleLayout.getChildAt(1);
        tv1.setText("我的应用");
        tv2.setText("");


        titleLayout = (LinearLayout) ((LinearLayout) mMainLayout.getChildAt(1))
                .getChildAt(0);
        params = (LinearLayout.LayoutParams) titleLayout
                .getLayoutParams();
        params.height = mGroupTitleHeight;
        titleLayout.requestLayout();

        tv1 = (TextView) titleLayout.getChildAt(0);
        tv2 = (TextView) titleLayout.getChildAt(1);
        tv1.setText("推荐应用");
        tv2.setText("");

    }

    private void computeSize() {
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int screenWidth = outMetrics.widthPixels;
        mCellHeight = (int) (screenWidth * 1.0 / 4 * 180 / 186);
        mGroupTitleHeight = (int) (screenWidth * 1.0 / 4 * 86 / 186);

        mRecommendItemHeight = (int) (screenWidth * 1.0 / 4 * 126 / 186);
        mItemAddBtnHeight =(int) (screenWidth * 1.0 / 4 * 50 / 186);
    }

    private void reLayoutReCommendItem(final LinearLayout itemLayout) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) itemLayout
                .getLayoutParams();
        params.height = mRecommendItemHeight;

        params = (LinearLayout.LayoutParams) itemLayout.findViewById(R.id.item_add_btn)
                .getLayoutParams();
        params.height = mItemAddBtnHeight;

        itemLayout.requestLayout();

        itemLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                itemLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                StateListDrawable stateListDrawable = new StateListDrawable();
                int pressed = android.R.attr.state_pressed;
                GradientDrawable normalDrawable = (GradientDrawable) getResources().getDrawable(R.drawable.app_recommend_add_bg);
                GradientDrawable pressedDrawable = (GradientDrawable) getResources().getDrawable(R.drawable.app_recommend_add_press_bg);
                normalDrawable.setCornerRadius(itemLayout.getWidth() * 1f / 2);
                pressedDrawable.setCornerRadius(itemLayout.getWidth() * 1f / 2);
                stateListDrawable.addState(new int[]{pressed}, pressedDrawable);
                stateListDrawable.addState(new int[]{-pressed}, normalDrawable);
                stateListDrawable.addState(new int[]{}, normalDrawable);
                itemLayout.findViewById(R.id.item_add_btn).setBackgroundDrawable(stateListDrawable);
            }
        });
    }
}
