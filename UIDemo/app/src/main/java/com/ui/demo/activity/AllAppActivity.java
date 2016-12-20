package com.ui.demo.activity;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.uidemo.BaseActivity;
import com.uidemo.DataCenter;
import com.uidemo.R;
import com.uidemo.bean.AppCellBean;
import com.uidemo.bean.AppGroupBean;
import com.uidemo.view.AppContainer;
import com.uidemo.view.OnRearrangeListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created by Administrator on 2016/10/21.
 */

public class AllAppActivity extends BaseActivity implements View.OnClickListener {


    public static final int MODE_NORMAL = 1;
    public static final int MODE_MODIFY = 2;

    private AppContainer mAppContainer;
    private View dashView;
    private LayoutInflater mInflater;
    private int mCurrentMode = MODE_NORMAL;

    private List<AppCellBean> myAppList;

    private List<AppGroupBean> otherGroupList;

    private AtomicBoolean mMoving = new AtomicBoolean(false);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_app);
        initView();
        updateList();

        inputContainer();

        mAppContainer.setOnRearrangeListener(new OnRearrangeListener() {
            @Override
            public void onRearrange(final int oldIndex, final int newIndex) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AppCellBean appCellBean = myAppList.remove(oldIndex);
                        myAppList.add(newIndex, appCellBean);
                        for (int i = 0; i < myAppList.size(); i++) {
                            myAppList.get(i).order = i;
                        }
                        updateList();
                        updateMyApp(false);
                    }
                }, 300);
            }
        });
        mAppContainer.setMyAppItemClick(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (mMoving.get()) return;
                String tag = (String) view.getTag();
                String[] arr = tag.split("###");
                int groupId = Integer.parseInt(arr[3]);
                Point point = mAppContainer.getNextTargetPosition(groupId, getGroupWithId(groupId).apps.size(), false);
                if (point == null) return;
                List<View> viewList = mAppContainer.getMyAppViews();
                List<AppCellBean> list = getGroupWithId(groupId).apps;
                if (list != null && !list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).order = i;
                    }
                    myAppList.get(position).order = list.size();
                }
                myAppList.get(position).inMyAppList = false;
                updateList();
                updateGroupApp(groupId);
                move(view, position, point, viewList);
            }
        });
    }

    private void translate(View view, float translationX, float translationY, Animator.AnimatorListener listener) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(150);
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", translationX),
                ObjectAnimator.ofFloat(view, "translationY", translationY)

        );
        if (listener != null) {
            animatorSet.addListener(listener);
        }
        animatorSet.start();
    }

    private AppGroupBean getGroupWithId(int groupId) {
        if (otherGroupList != null && !otherGroupList.isEmpty()) {
            for (int i = 0; i < otherGroupList.size(); i++) {
                AppGroupBean appGroupBean = otherGroupList.get(i);
                if (appGroupBean.groupId == groupId) {
                    return appGroupBean;
                }
            }
        }
        return null;
    }

    private void updateList() {
        if (myAppList == null) {
            myAppList = new ArrayList<>();
        } else {
            myAppList.clear();
        }
        if (otherGroupList == null) {
            otherGroupList = new ArrayList<>();
        } else {
            otherGroupList.clear();
        }
        for (int i = 0; i < DataCenter.appGroup.size(); i++) {
            AppGroupBean appGroupBean = DataCenter.appGroup.get(i);
            AppGroupBean appGroupBean1 = new AppGroupBean();
            appGroupBean1.groupId = appGroupBean.groupId;
            appGroupBean1.name = appGroupBean.name;
            List<AppCellBean> appCellBeanList = new ArrayList<>();
            appGroupBean1.apps = appCellBeanList;
            otherGroupList.add(appGroupBean1);
            for (int j = 0; j < appGroupBean.apps.size(); j++) {
                AppCellBean appCellBean = appGroupBean.apps.get(j);
                if (appCellBean.inMyAppList) {
                    myAppList.add(appCellBean);
                } else {
                    appCellBeanList.add(appCellBean);
                }
            }
            Collections.sort(appCellBeanList);
        }
        Collections.sort(myAppList);
    }

    private void initView() {
        initTitleView(R.drawable.title_back_icon_selector, "返回");
        setMyTitle("应用管理");
        mTitleRightTv.setVisibility(View.VISIBLE);
        mTitleRightTv.setText("管理");

        setLeftBtnOnClickListener(this);
        setRightBtnOnClickListener(this);
        mInflater = LayoutInflater.from(this);
        mAppContainer = (AppContainer) findViewById(R.id.container);
        dashView = findViewById(R.id.dash_line_view);
        mAppContainer.changeMode(mCurrentMode);
    }

    private void inputContainer() {

        mAppContainer.removeAllViewsWithoutFirst();

        AppGroupBean appGroupBean = new AppGroupBean();
        appGroupBean.groupId = Integer.MAX_VALUE;
        appGroupBean.name = "我的应用";

        LinearLayout view = getGroupTitleLayout(appGroupBean, true);
        mAppContainer.addView(view);
        if (myAppList != null && !myAppList.isEmpty()) {
            for (int j = 0; j < myAppList.size(); j++) {
                AppCellBean cellBean = myAppList.get(j);
                mAppContainer.addView(getCellLayout(cellBean, myAppList.size(), j));
            }
        }
        for (int i = 0; i < otherGroupList.size(); i++) {
            AppGroupBean groupBean = otherGroupList.get(i);
            view = getGroupTitleLayout(groupBean, false);
            mAppContainer.addView(view);
            if (groupBean.apps != null && !groupBean.apps.isEmpty()) {
                for (int j = 0; j < groupBean.apps.size(); j++) {
                    AppCellBean cellBean = groupBean.apps.get(j);
                    mAppContainer.addView(getCellLayout(cellBean, groupBean.apps.size(), j));
                }
            }

        }
        updateWithMode();
    }


    private FrameLayout getCellLayout(AppCellBean cellBean, int size, int position) {
        FrameLayout cellLayout = (FrameLayout) mInflater.inflate(R.layout.app_manage_cell, null);
        boolean isLast = (position + 1) == size;
        cellLayout.setTag("cell###" + size + "###" + position + "###" + cellBean.groupId + "###" + cellBean.inMyAppList);

        TextView tv = (TextView) cellLayout.findViewById(R.id.cell_tv);
        View borderView = cellLayout.findViewById(R.id.cell_border_view);
        ImageView iv = (ImageView) cellLayout.findViewById(R.id.cell_iv);
        tv.setText(cellBean.name);
        iv.setImageResource(cellBean.resId);

        borderView.setBackgroundResource(R.drawable.dash_line_bg);
        cellLayout.setBackgroundResource(R.drawable.app_cell_bg_selector);
        cellLayout.setOnClickListener(this);
        return cellLayout;
    }


    @NonNull
    private LinearLayout getGroupTitleLayout(AppGroupBean appGroupBean, boolean isMyApp) {
        LinearLayout view = (LinearLayout) mInflater.inflate(R.layout.app_group_title, null);
        TextView tv1 = (TextView) view.getChildAt(0);
        TextView tv2 = (TextView) view.getChildAt(1);
        tv1.setText(appGroupBean.name);
        if (isMyApp) {
            tv2.setText("按住拖动调整顺序");
        } else {
            tv2.setText("点选可添加或删除");
        }

        view.setTag("title###" + appGroupBean.groupId);
        return view;
    }

    public void updateMyApp(boolean isTemp) {
        int startIndex = mAppContainer.removeMyAppCell();
        if (myAppList != null && !myAppList.isEmpty()) {
            for (int j = 0; j < myAppList.size(); j++) {
                AppCellBean cellBean = myAppList.get(j);
                FrameLayout cellLayout = getCellLayout(cellBean, myAppList.size(), j);
                mAppContainer.addView(cellLayout, startIndex + j);
                if (isTemp && j == myAppList.size() - 1) {
                    cellLayout.setVisibility(View.INVISIBLE);
                }
            }
        }
        updateWithMode();
    }

    private void updateGroupApp(int groupId) {
        int startIndex = mAppContainer.removeGroupApp(groupId);
        AppGroupBean bean = getGroupWithId(groupId);
        if (bean == null) return;
        List<AppCellBean> list = bean.apps;
        if (list == null || list.isEmpty()) {
            return;
        }
        for (int j = 0; j < list.size(); j++) {
            AppCellBean cellBean = list.get(j);
            FrameLayout cellLayout = getCellLayout(cellBean, list.size(), j);
            mAppContainer.addView(cellLayout, startIndex + j);
            if (j == list.size() - 1) {
                cellLayout.setVisibility(View.INVISIBLE);
            }
        }
        updateWithMode();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mCurrentMode == MODE_MODIFY) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case sTitleLeftBtnId:
                finish();
                break;
            case sTitleRightBtnId:
                if (mCurrentMode == MODE_MODIFY) {
                    mCurrentMode = MODE_NORMAL;
                } else if (mCurrentMode == MODE_NORMAL) {
                    mCurrentMode = MODE_MODIFY;
                }

                updateWithMode();
                break;
            default:
                String tag = (String) view.getTag();
                if (tag != null && tag.startsWith("cell")) {
                    String[] arr = tag.split("###");
                    int size = Integer.parseInt(arr[1]);
                    int position = Integer.parseInt(arr[2]);
                    int groupId = Integer.parseInt(arr[3]);
                    boolean isMyApp = Boolean.parseBoolean(arr[4]);
                    if (mCurrentMode == MODE_NORMAL) {
                        String name;
                        if (isMyApp) {
                            name = myAppList.get(position).name;
                        } else {
                            name = getGroupWithId(groupId).apps.get(position).name;
                        }
                        Toast.makeText(getApplication(), "" + name, Toast.LENGTH_SHORT).show();
                    } else {
                        if (isMyApp) return;
                        if (mMoving.get()) return;
                        //移动到我的应用
                        groupId = Integer.MAX_VALUE;
                        if (!myAppList.isEmpty()) {
                            groupId = myAppList.get(myAppList.size() - 1).groupId;
                        }
                        Point point = mAppContainer.getNextTargetPosition(groupId, myAppList.size(), true);
                        if (point == null) return;
                        groupId = Integer.parseInt(arr[3]);
                        List<AppCellBean> currGroupApps = getGroupWithId(groupId).apps;
                        List<View> viewList = mAppContainer.getGroupViews(groupId);
                        List<AppCellBean> list = myAppList;
                        if (list != null && !list.isEmpty()) {
                            for (int i = 0; i < list.size(); i++) {
                                list.get(i).order = i;
                            }
                            currGroupApps.get(position).order = list.size();
                        }
                        currGroupApps.get(position).inMyAppList = true;
                        if (point.x == 0) {
                            point.y = point.y - view.getHeight();
                        }
                        updateList();
                        updateMyApp(true);
                        move(view, position, point, viewList);
                    }
                }
        }

    }

    private void move(View view, int position, Point point, List<View> viewList) {
        mMoving.set(true);
        translate(view, point.x - view.getLeft(), point.y - view.getTop(), new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                inputContainer();
                mMoving.set(false);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        for (int i = position + 1; i < viewList.size(); i++) {
            view = viewList.get(i);
            int translationX, translationY;
            if (i % 4 == 0) {
                translationX = view.getWidth() * 3;
                translationY = -view.getHeight();
            } else {
                translationX = -view.getWidth();
                translationY = 0;
            }
            translate(view, translationX, translationY, null);
        }
    }

    private void updateWithMode() {

        int bgColor;
        if (mCurrentMode == MODE_MODIFY) {
            bgColor = Color.WHITE;
            initTitleView(R.drawable.title_back_icon_selector, "返回");
            setMyTitle("应用管理");
            mTitleRightTv.setVisibility(View.GONE);
            mTitleRightLayout.setVisibility(View.VISIBLE);
            mTitleLeftBtn.setVisibility(View.GONE);
            mTitleLayout.setBackgroundColor(getResources().getColor(R.color.white));
            mTitleTv.setTextColor(getResources().getColor(R.color.black));
            dashView.setVisibility(View.VISIBLE);
        } else {
            mTitleLeftBtn.setVisibility(View.VISIBLE);
            initTitleView(R.drawable.title_back_icon_selector, "返回");
            setMyTitle("应用管理");
            mTitleRightTv.setVisibility(View.VISIBLE);
            mTitleRightTv.setText("管理");
            mTitleRightLayout.setVisibility(View.GONE);
            mTitleLayout.setBackgroundColor(Color.parseColor("#0076ff"));
            mTitleTv.setTextColor(getResources().getColor(R.color.white));
            bgColor = Color.WHITE;
            dashView.setVisibility(View.GONE);
        }
        mAppContainer.changeMode(mCurrentMode);
        mAppContainer.setBackgroundColor(bgColor);
        updateChild();
    }

    private void updateChild() {
        for (int i = 0; i < mAppContainer.getChildCount(); i++) {
            View view = mAppContainer.getChildAt(i);
            String tag = (String) view.getTag();
            if (tag != null) {
                if (tag.startsWith("cell")) {
                    View borderView = view.findViewById(R.id.cell_border_view);
                    borderView.setBackgroundColor(Color.TRANSPARENT);
                    view.setBackgroundResource(R.drawable.app_cell_bg_selector);
                    if (mCurrentMode == MODE_MODIFY) {
                        borderView.setBackgroundResource(R.drawable.dash_line_bg);
                        view.setBackgroundColor(Color.TRANSPARENT);
                    }
                } else if (tag.startsWith("title")) {
                    View rightTv = ((LinearLayout) view).getChildAt(1);
                    if (mCurrentMode == MODE_MODIFY) {
                        view.setBackgroundColor(Color.TRANSPARENT);
                        rightTv.setVisibility(View.VISIBLE);
                    } else {
                        view.setBackgroundColor(Color.parseColor("#f2f2f2"));
                        rightTv.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
    }

}
