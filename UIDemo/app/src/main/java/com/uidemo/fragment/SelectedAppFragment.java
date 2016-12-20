package com.uidemo.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.ui.demo.activity.AllAppActivity;
import com.uidemo.DataCenter;
import com.uidemo.R;
import com.uidemo.bean.AppCellBean;
import com.uidemo.bean.AppGroupBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/10/18.
 */

public class SelectedAppFragment extends BaseFragment implements View.OnClickListener {
    private int mGroupTitleHeight;// 86
    private int mCellHeight;// 180
    private int mRecommendItemHeight;//126
    private int mItemAddBtnHeight;//50

    private LinearLayout mMainLayout;
    private LinearLayout mMyAppLayout;
    private LinearLayout mRecommendLayout;


    private LayoutInflater mInflater;


    private List<AppCellBean> myAppList;
    private List<AppCellBean> myAppAllList;
    private List<AppCellBean> recommendList;

    private int rowSize;

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag.contains("myApp")) {
            int position = Integer.parseInt(tag.split("###")[1]);
            if (position == -1) {
                Intent intent = new Intent(getActivity(), AllAppActivity.class);
                startActivity(intent);
                return;
            }
            AppCellBean cellBean = myAppList.get(position);

        } else if (tag.contains("recommendAppAdd")) {
            int position = Integer.parseInt(tag.split("###")[1]);
            LinearLayout contentLayout = (LinearLayout)
                    mRecommendLayout.findViewWithTag("recommendContentLayout###" + position);
            AppCellBean cellBean = recommendList.get(position);
            cellBean.inMyAppList = true;
            if(!myAppAllList.isEmpty()){
                if(cellBean.order<=myAppAllList.get(myAppAllList.size()-1).order){
                    cellBean.order = myAppAllList.get(myAppAllList.size()-1).order+1;
                }
            }
            startAnim(contentLayout);

        } else if (tag.contains("recommendApp")) {
//            int position = Integer.parseInt(tag.split("###")[1]);

//            Toast.makeText(getActivity(), "recommendApp:" + cellBean.name, Toast.LENGTH_SHORT).show();

        }
    }

    private void startAnim(LinearLayout contentLayout) {
        View allButton =  mMyAppLayout.findViewWithTag("myApp###-1");
        int[] location = new int[2];
        allButton.getLocationInWindow(location);
        int toX = location[0]+allButton.getWidth()/2;
        int toY = location[1]+allButton.getHeight()/2;
        contentLayout.getLocationInWindow(location);
        int fromX = location[0]+contentLayout.getWidth()/2;
        int fromY = location[1]+contentLayout.getHeight()/2;
        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                updateList();
                updateView();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(contentLayout,"translationY",toY-fromY),
                ObjectAnimator.ofFloat(contentLayout,"translationX",toX-fromX),
                ObjectAnimator.ofFloat(contentLayout,"scaleX",1f,0),
                ObjectAnimator.ofFloat(contentLayout,"scaleY",1f,0)
        );
        animatorSet.setDuration(300).start();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selected_app, null);
        computeSize();
        initView(view);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        updateList();
        updateView();
    }

    private void updateList() {


        if(myAppList == null){
            myAppList = new ArrayList<>();
        }else{
            myAppList.clear();
        }
        if(recommendList == null){
            recommendList = new ArrayList<>();
        }else{
            recommendList.clear();
        }
        myAppAllList = new ArrayList<>();

        for (int i = 0; i < DataCenter.appGroup.size(); i++) {
            AppGroupBean appGroupBean = DataCenter.appGroup.get(i);
            for (int j = 0; j < appGroupBean.apps.size(); j++) {
                AppCellBean appCellBean = appGroupBean.apps.get(j);
                if(appCellBean.inMyAppList){
                    myAppAllList.add(appCellBean);
                }else if(appCellBean.inRecommendList){
                    recommendList.add(appCellBean);
                }
            }
        }
        Collections.sort(myAppAllList);
        int size = myAppAllList.size() <= 7 ? myAppAllList.size() : 7;
        for (int i = 0; i < size; i++) {
            myAppList.add(myAppAllList.get(i));
        }


    }


    private void updateView() {
        updateMyAppLayout(mMyAppLayout, myAppList);


        updateRecommendLayout(mRecommendLayout, recommendList);
    }


    private void updateRecommendLayout(LinearLayout listLayout, List<AppCellBean> recommendList) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) listLayout.findViewById(R.id.padding_view).getLayoutParams();
        params.height = mGroupTitleHeight + mCellHeight * rowSize;
        listLayout.findViewById(R.id.padding_view).requestLayout();
        for (int i = listLayout.getChildCount() - 1; i >= 0; i--) {
            if (i != 0 && i !=1) {
                listLayout.removeViewAt(i);
            }
        }
        if (recommendList == null) {
            recommendList = new ArrayList<>();
        }
        for (int i = 0; i < recommendList.size(); i++) {

            AppCellBean cellBean = recommendList.get(i);
            final LinearLayout itemLayout = (LinearLayout) mInflater.inflate(
                    R.layout.recommend_app_item, null);
            listLayout.addView(itemLayout);
            ImageView iconIv = (ImageView) itemLayout.findViewById(R.id.item_icon_iv);
            TextView nameTv = (TextView) itemLayout.findViewById(R.id.item_name_tv);
            TextView DescTv = (TextView) itemLayout.findViewById(R.id.item_desc_tv);
            LinearLayout contentLayout = (LinearLayout) itemLayout.findViewById(R.id.item_content_layout);
            iconIv.setImageResource(cellBean.resId);
            nameTv.setText(cellBean.name);
            DescTv.setText(cellBean.desc);

            itemLayout.findViewById(R.id.item_add_btn).setOnClickListener(this);

            itemLayout.findViewById(R.id.item_add_btn).setTag("recommendAppAdd###" + i);
            contentLayout.setTag("recommendContentLayout###" + i);
            reLayoutReCommendItem(itemLayout);
        }
    }


    private void updateMyAppLayout(LinearLayout groupLayout,
                                   List<AppCellBean> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        for (int i = groupLayout.getChildCount() - 1; i >= 0; i--) {
            if (i != 0 && i !=1) {
                groupLayout.removeViewAt(i);
            }

        }
        int size = list.size() + 1;
        rowSize = size % 4 == 0 ? size / 4
                : size / 4 + 1;

        ArrayList<View> cellLayouts = new ArrayList<>();
        for (int i = 0; i < rowSize; i++) {
            LinearLayout rowLayout = (LinearLayout) mInflater.inflate(
                    R.layout.app_manage_row, null);
            groupLayout.addView(rowLayout);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rowLayout
                    .getLayoutParams();
            params.height = mCellHeight;
            rowLayout.requestLayout();
            for (int j = 0; j < rowLayout.getChildCount(); j++) {
                View layout = rowLayout.getChildAt(j);
                cellLayouts.add(layout);
            }
        }
        inputCellLayout(cellLayouts, list);
    }


    private void inputCellLayout(ArrayList<View> cellLayouts,
                                 List<AppCellBean> list) {
        for (int i = 0; i < list.size(); i++) {
            AppCellBean cellBean = list.get(i);
            View cellLayout = cellLayouts.get(i);
            cellLayout.setOnClickListener(this);
            cellLayout.setTag("myApp###" + i);
            cellLayout.setVisibility(View.VISIBLE);
            cellLayout.setBackgroundResource(R.drawable.app_cell_bg_selector);
            TextView tv = (TextView) cellLayout.findViewById(R.id.cell_tv);
            ImageView iv = (ImageView) cellLayout.findViewById(R.id.cell_iv);
            tv.setText(cellBean.name);
            iv.setImageResource(cellBean.resId);
        }
        View cellLayout = cellLayouts.get(list.size());
        cellLayout.setOnClickListener(this);
        cellLayout.setTag("myApp###" + -1);
        cellLayout.setVisibility(View.VISIBLE);
        cellLayout.setBackgroundResource(R.drawable.app_cell_bg_selector);
        TextView tv = (TextView) cellLayout.findViewById(R.id.cell_tv);
        ImageView iv = (ImageView) cellLayout.findViewById(R.id.cell_iv);
        tv.setText("全部");
        iv.setImageResource(R.drawable.app_all);
        tv.setTextColor(Color.parseColor("#999999"));
    }


    private void initView(View view) {
        mInflater = getActivity().getLayoutInflater();
        mMainLayout = (LinearLayout) view.findViewById(R.id.main_layout);
        mMyAppLayout = (LinearLayout) mMainLayout.getChildAt(0);
        mRecommendLayout = (LinearLayout) view.findViewById(R.id.recommend_layout);
        LinearLayout titleLayout = (LinearLayout) ((LinearLayout) mMainLayout.getChildAt(0))
                .getChildAt(1);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) titleLayout
                .getLayoutParams();
        params.height = mGroupTitleHeight;
        titleLayout.requestLayout();

        TextView tv1 = (TextView) titleLayout.getChildAt(0);
        TextView tv2 = (TextView) titleLayout.getChildAt(1);
        tv1.setText("我的应用");
        tv2.setText("");


        titleLayout = (LinearLayout) mRecommendLayout
                .getChildAt(1);
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
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int screenWidth = outMetrics.widthPixels;
        mCellHeight = (int) (screenWidth * 1.0 / 4 * 180 / 186);
        mGroupTitleHeight = (int) (screenWidth * 1.0 / 4 * 86 / 186);

        mRecommendItemHeight = (int) (screenWidth * 1.0 / 4 * 126 / 186);
        mItemAddBtnHeight = (int) (screenWidth * 1.0 / 4 * 50 / 186);

    }


    private void reLayoutReCommendItem(final LinearLayout itemLayout) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) itemLayout
                .getLayoutParams();
        params.height = mRecommendItemHeight;
        itemLayout.setLayoutParams(params);
        params = (LinearLayout.LayoutParams) itemLayout.findViewById(R.id.item_add_btn)
                .getLayoutParams();
        params.height = mItemAddBtnHeight;
        itemLayout.findViewById(R.id.item_add_btn).setLayoutParams(params);
        StateListDrawable addBtnDrawable = new StateListDrawable();
        int pressed = android.R.attr.state_pressed;
        GradientDrawable normalDrawable = (GradientDrawable) getResources().getDrawable(R.drawable.app_recommend_add_bg);
        GradientDrawable pressedDrawable = (GradientDrawable) getResources().getDrawable(R.drawable.app_recommend_add_press_bg);
        normalDrawable.setCornerRadius(mItemAddBtnHeight * 1f / 2);
        pressedDrawable.setCornerRadius(mItemAddBtnHeight * 1f / 2);
        addBtnDrawable.addState(new int[]{pressed}, pressedDrawable);
        addBtnDrawable.addState(new int[]{-pressed}, normalDrawable);
        addBtnDrawable.addState(new int[]{}, normalDrawable);
        itemLayout.findViewById(R.id.item_add_btn).setBackgroundDrawable(addBtnDrawable);
    }
}
