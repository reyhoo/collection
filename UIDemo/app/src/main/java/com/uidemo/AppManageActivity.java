package com.uidemo;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.uidemo.bean.AppCellBean;
import com.uidemo.bean.AppGroupBean;
import com.uidemo.recycleview.MyItemTouchCallback;
import com.uidemo.recycleview.OnRecyclerItemClickListener;
import com.uidemo.recycleview.RecyclerAdapter;

public class AppManageActivity extends BaseActivity implements OnClickListener  ,MyItemTouchCallback.OnDragListener{

    public static final int MODE_NORMAL = 1;
    public static final int MODE_MODIFY = 2;

    private int mGroupTitleHeight;// 86
    private int mCellHeight;// 180
    private LinearLayout mMainLayout;


    private LayoutInflater mInflater;

    private RecyclerView mDragView;

    private ItemTouchHelper itemTouchHelper;

    private TextView myAppTv1;
    private TextView myAppTv2;

    private RecyclerAdapter mRecyclerAdapter;

    private int mCurrentMode = MODE_NORMAL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        computeSize();
//        initView();
//        updateMyAppView();
//        updateView();
    }

//    private void updateMyAppView() {
//        mRecyclerAdapter = new RecyclerAdapter(R.layout.app_manage_cell,DataCenter.myApp);
//        mDragView.setHasFixedSize(true);
//        mDragView.setAdapter(mRecyclerAdapter);
////        mDragView.addItemDecoration(new DividerGridItemDecoration(this));
//        mDragView.setLayoutManager(new GridLayoutManager(this, 4));
//        itemTouchHelper = new ItemTouchHelper(new MyItemTouchCallback(mRecyclerAdapter).setOnDragListener(this));
//        itemTouchHelper.attachToRecyclerView(mDragView);
//
//        mDragView.addOnItemTouchListener(new OnRecyclerItemClickListener(mDragView) {
//            @Override
//            public void onLongClick(RecyclerView.ViewHolder vh) {
////				if (vh.getLayoutPosition()!=results.size()-1) {
//                itemTouchHelper.startDrag(vh);
//                if(mCurrentMode == MODE_NORMAL){
//                    mCurrentMode = MODE_MODIFY;
//                    updateDragView();
//                    updateView();
//                }
////                    VibratorUtil.Vibrate(getActivity(), 70);   //震动70ms
////				}
//            }
//            @Override
//            public void onItemClick(RecyclerView.ViewHolder vh) {
//                AppCellBean item = DataCenter.myApp.get(vh.getLayoutPosition());
//                Toast.makeText(getApplication(),item.name,Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void updateView() {
//        removeOtherAppView();
//
//        for (int i = 0; i < DataCenter.appGroup.size(); i++) {
//            AppGroupBean groupBean = DataCenter.appGroup.get(i);
//            LinearLayout groupLayout = (LinearLayout) mInflater.inflate(
//                    R.layout.app_manage_group, null);
//            LinearLayout titleLayout = ((LinearLayout) groupLayout
//                    .getChildAt(1));
//            LinearLayout.LayoutParams params = (LayoutParams) titleLayout
//                    .getLayoutParams();
//            params.height = mGroupTitleHeight;
//            titleLayout.requestLayout();
//            TextView tv1 = (TextView) titleLayout.getChildAt(0);
//            TextView tv2 = (TextView) titleLayout.getChildAt(1);
//            tv1.setText(groupBean.name);
//            tv2.setText("");
//            updateGroupLayout(groupBean,groupLayout, false);
//            mMainLayout.addView(groupLayout);
//        }
//    }
//
//    private void updateGroupLayout(AppGroupBean groupBean, LinearLayout groupLayout, boolean isMyApp) {
//        for (int i = groupLayout.getChildCount() - 1; i >= 0; i--) {
//            if (i != 0&&i != 1){
//                groupLayout.removeViewAt(i);
//            }
//
//        }
//        if (groupBean.apps == null) {
//            return;
//        }
//
//        int rowSize = groupBean.apps .size() % 4 == 0 ? groupBean.apps .size() / 4
//                : groupBean.apps .size() / 4 + 1;
//        ArrayList<View> cellLayouts = new ArrayList<>();
//        for (int i = 0; i < rowSize; i++) {
//            LinearLayout rowLayout = (LinearLayout) mInflater.inflate(
//                    R.layout.app_manage_row, null);
//            groupLayout.addView(rowLayout);
//            LinearLayout.LayoutParams params = (LayoutParams) rowLayout
//                    .getLayoutParams();
//            params.height = mCellHeight;
//            rowLayout.requestLayout();
//            for (int j = 0; j < rowLayout.getChildCount(); j++) {
//                View layout =  rowLayout.getChildAt(j);
//                cellLayouts.add(layout);
//            }
//        }
//        inputCellLayout(groupBean,cellLayouts, isMyApp);
//    }
//
//    private void inputCellLayout(AppGroupBean groupBean,ArrayList<View> cellLayouts,
//                                 boolean isMyApp) {
//        for (int i = 0; i < groupBean.apps.size(); i++) {
//            AppCellBean cellBean = groupBean.apps.get(i);
//            View cellLayout = cellLayouts.get(i);
//            cellLayout.setOnClickListener(this);
//            cellLayout.setTag("app###" + isMyApp + "###" +cellBean.cellId+"###"+cellBean.groupId );
//            cellLayout.setVisibility(View.VISIBLE);
//            TextView tv = (TextView) cellLayout.findViewById(R.id.cell_tv);
//            View borderView =  cellLayout.findViewById(R.id.cell_border_view);
//            ImageView iv = (ImageView) cellLayout.findViewById(R.id.cell_iv);
//            tv.setText(cellBean.name);
//            iv.setImageResource(cellBean.resId);
//
//
//            if(mCurrentMode == MODE_NORMAL){
//                borderView.setBackgroundColor(Color.TRANSPARENT);
//                cellLayout.setBackgroundResource(R.drawable.app_cell_bg_selector);
//            }else if(mCurrentMode == MODE_MODIFY){
//                borderView.setBackgroundResource(R.drawable.dash_line_bg);
//                cellLayout.setBackgroundColor(Color.TRANSPARENT);
//            }
//        }
//    }
//
//
//    private void initView() {
//        initTitleView(R.drawable.title_back_icon_selector, "返回");
//        setMyTitle("应用管理");
//        mTitleRightTv.setVisibility(View.VISIBLE);
//        mTitleRightTv.setText("管理");
//
//        setLeftBtnOnClickListener(this);
//        setRightBtnOnClickListener(this);
//        mInflater = getLayoutInflater();
//        mMainLayout = (LinearLayout) findViewById(R.id.main_layout);
//        removeOtherAppView();
//
//
//        mDragView = (RecyclerView) findViewById(R.id.dragRecyclerView);
//        LinearLayout.LayoutParams params = (LayoutParams) mMainLayout.getChildAt(0).findViewById(R.id.title_layout).getLayoutParams();
//        params.height = mGroupTitleHeight;
//        mMainLayout.getChildAt(0).findViewById(R.id.title_layout).requestLayout();
//        myAppTv1 = (TextView) mMainLayout.getChildAt(0).findViewById(R.id.title_text1);
//        myAppTv2 = (TextView) mMainLayout.getChildAt(0).findViewById(R.id.title_text2);
//        myAppTv1.setText("我的应用");
//        myAppTv2.setText("");
//        removeOtherAppView();
//    }
//
//    private void computeSize() {
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
//        int screenWidth = outMetrics.widthPixels;
//        mCellHeight = (int) (screenWidth * 1.0 / 4 * 180 / 186);
//        mGroupTitleHeight = (int) (screenWidth * 1.0 / 4 * 86 / 186);
//    }
//
//    private void removeOtherAppView() {
//        for (int i = mMainLayout.getChildCount() - 1; i >= 0; i--) {
//            if (i == 0)
//                return;
//            mMainLayout.removeViewAt(i);
//        }
//    }
//
//
//    public void updateDragView(){
//        for (int i = 0; i < mDragView.getChildCount(); i++) {
//            View view = mDragView.getChildAt(i);
//            View borderView = view.findViewById(R.id.cell_border_view);
//            borderView.setBackgroundResource(R.drawable.dash_line_bg);
//            view.setBackgroundColor(Color.TRANSPARENT);
//            view.setClickable(false);
////            if (mCurrentMode == AppManageActivity.MODE_NORMAL) {
////
////                contentLayout.setBackgroundColor(Color.TRANSPARENT);
////                view.setBackgroundResource(R.drawable.app_cell_bg_selector);
////            } else {
////                contentLayout.setBackgroundResource(R.drawable.dash_line_bg);
////                view.setBackgroundColor(Color.TRANSPARENT);
////            }
//        }
////        mRecyclerAdapter.setShowMode(mCurrentMode);
////        mRecyclerAdapter.notifyDataSetChanged();
//    }
//
//    private void updateViewWithMode(){
//        mRecyclerAdapter.setShowMode(mCurrentMode);
//        mRecyclerAdapter.notifyDataSetChanged();
//        updateView();
//    }
    @Override
    public void onClick(View v) {
//
//
//        switch (v.getId()) {
//            case sTitleLeftBtnId:
//                finish();
//                break;
//            case sTitleRightBtnId:
//                if(mCurrentMode == MODE_NORMAL){
//                    mCurrentMode = MODE_MODIFY;
//                }else if(mCurrentMode == MODE_MODIFY){
//                    mCurrentMode = MODE_NORMAL;
//                }
//                updateViewWithMode();
//                break;
//            default:
//                String tag = (String) v.getTag();
//                String[] arr = tag.split("###");
//                boolean isMyApp = Boolean.parseBoolean(arr[1]);
//                int cellId = Integer.parseInt(arr[2]);
//                int groupId = Integer.parseInt(arr[3]);
//                Toast.makeText(getApplicationContext(),isMyApp+","+cellId+","+groupId,Toast.LENGTH_SHORT).show();
//                break;
//        }
    }
//
    @Override
    public void onFinishDrag() {

    }
}
