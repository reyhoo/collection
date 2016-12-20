package com.uidemo.view;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.ui.demo.activity.AllAppActivity;
import com.uidemo.AppManageActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 2016/10/21.
 */

public class AppContainer extends ViewGroup implements View.OnLongClickListener, View.OnClickListener {


    private int mGroupTitleHeight;// 86
    private int mCellHeight;// 180
    private int mCellWidth;//

    private int screenWidth;
    private int mMarginTop;
    private View mTopView;


    private LayoutInflater mInflater;
    private int mCurrentMode = AllAppActivity.MODE_NORMAL;


    private int mLastDownX;
    private int mLastDownY;
    private boolean isLongClick = false;

    private ArrayList<View> myAppList = new ArrayList<>();
    private ArrayList<Integer> positions = new ArrayList<>();

    private SparseArray<List<View>> groupsViews = new SparseArray<>();
    private View dragView;


    private int dragPosition = -1;


    private int lastTargetPosition = -1;
    private AdapterView.OnItemClickListener itemClickListener;

//    private int myAppCellStartIndex = -1;

    protected OnRearrangeListener onRearrangeListener;

    public AppContainer(Context context) {
        super(context);
        init();
    }


    public AppContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public AppContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mInflater = LayoutInflater.from(getContext());
        mTopView = new View(getContext());
        ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        mTopView.setLayoutParams(params);
        addView(mTopView);
        computeSize();
        setOnLongClickListener(this);
        setOnClickListener(this);
    }

    private void computeSize() {
        DisplayMetrics outMetrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int screenWidth = outMetrics.widthPixels;
        mCellHeight = (int) (screenWidth * 1.0 / 4 * 180 / 186);
        mGroupTitleHeight = (int) (screenWidth * 1.0 / 4 * 86 / 186);
        mCellWidth = (int) (screenWidth * 1.0 / 4);
        this.screenWidth = screenWidth;
    }

    

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        String action = "";

        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                mLastDownX = x;
                mLastDownY = y;

                break;

            case MotionEvent.ACTION_MOVE:
                boolean isMyCell = dragView != null;
                requestDisallowInterceptTouchEvent(isMyCell && isLongClick);
                if (isMyCell) {
                    int centerX = (dragView.getRight() + dragView.getLeft()) / 2;
                    int centerY = (dragView.getTop() + dragView.getBottom()) / 2;
                    dragView.setTranslationX(x - centerX);
                    dragView.setTranslationY(y - centerY);
                    int targetPosition = getTargetPosition(x, y);
                    if (targetPosition != -1 && targetPosition != dragPosition && targetPosition != lastTargetPosition) {
                        animateGap(targetPosition);
                        lastTargetPosition = targetPosition;
                    }

                }
                action = "ACTION_MOVE";
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                isLongClick = false;
                if (dragView != null) {
                    dragView.setTranslationX(0);
                    dragView.setTranslationY(0);
                    dragView = null;
                }
                lastTargetPosition = -1;
                dragPosition = -1;
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                isLongClick = false;
                if (dragView != null) {
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.setDuration(100);
                    int translationX;
                    int translationY;
                    if (onRearrangeListener != null && lastTargetPosition != -1 && dragPosition != -1) {
                        Point point1 = getCoorFromIndex(dragPosition);
                        Point point2 = getCoorFromIndex(lastTargetPosition);
                        translationX = point2.x - point1.x;
                        translationY = point2.y - point1.y;
                        onRearrangeListener.onRearrange(dragPosition, lastTargetPosition);
                    } else {
                        translationX = 0;
                        translationY = 0;
                    }
                    animatorSet.playTogether(ObjectAnimator.ofFloat(dragView, "translationX", translationX));
                    animatorSet.playTogether(ObjectAnimator.ofFloat(dragView, "translationY", translationY));
                    animatorSet.start();
                }
                dragView = null;
                lastTargetPosition = -1;
                dragPosition = -1;
                break;
        }
        return super.onTouchEvent(event);
    }

    protected void animateGap(int target) {
        for (int i = 0; i < myAppList.size(); i++) {
            View v = myAppList.get(i);
            if (i == dragPosition)
                continue;
            int newPos = i;
            if (dragPosition < target && i >= dragPosition + 1 && i <= target)
                newPos--;
            else if (target < dragPosition && i >= target && i < dragPosition)
                newPos++;

            //animate
            int oldPos = i;
            if (positions.get(i) != -1)
                oldPos = positions.get(i);
            if (oldPos == newPos)
                continue;

            Point oldXY = getCoorFromIndex(oldPos);
            Point newXY = getCoorFromIndex(newPos);
            Point oldOffset = new Point(oldXY.x - v.getLeft(), oldXY.y - v.getTop());
            Point newOffset = new Point(newXY.x - v.getLeft(), newXY.y - v.getTop());

            TranslateAnimation translate = new TranslateAnimation(Animation.ABSOLUTE, oldOffset.x,
                    Animation.ABSOLUTE, newOffset.x,
                    Animation.ABSOLUTE, oldOffset.y,
                    Animation.ABSOLUTE, newOffset.y);
            translate.setDuration(150);
            translate.setFillEnabled(true);
            translate.setFillAfter(true);
            v.clearAnimation();
            v.startAnimation(translate);

            positions.set(i, newPos);
        }
    }


    public int removeMyAppCell() {
        for (View view :
                myAppList) {
            removeView(view);
        }
        return 2;

    }

    public int removeGroupApp(int groupId) {
        List<View> list = groupsViews.get(groupId);
        if (list != null && !list.isEmpty()) {
            for (View view :
                    list) {
                removeView(view);
            }
        }

        int groupCellStartIndex = -1;
        View view = findViewWithTag("title###" + groupId);
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) == view) {
                groupCellStartIndex = i + 1;
                break;
            }
        }
        return groupCellStartIndex;
    }

    private Point getCoorFromIndex(int index) {
        if (index >= 0 && index < myAppList.size()) {
            View view = myAppList.get(index);
            int left = view.getLeft();
            int top = view.getTop();
            int bottom = view.getBottom();
            int right = view.getRight();
            return new Point(left, top);
        }
        return null;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercepted = false;
        if (mCurrentMode == AppManageActivity.MODE_MODIFY) {
            isIntercepted = inDragViewPosition((int) ev.getX(), (int) ev.getY()) != null;
        }
        return isIntercepted;
    }


    private View inDragViewPosition(int x, int y) {

        for (int i = 0; i < myAppList.size(); i++) {
            View view = myAppList.get(i);
            int left = view.getLeft();
            int top = view.getTop();
            int bottom = view.getBottom();
            int right = view.getRight();
            if (x >= left && x <= right && y >= top && y <= bottom) {
                return view;
            }
        }
        return null;
    }


    private View getDragView(int x, int y) {

        for (int i = 0; i < myAppList.size(); i++) {
            View view = myAppList.get(i);
            int left = view.getLeft();
            int top = view.getTop();
            int bottom = view.getBottom();
            int right = view.getRight();
            if (x >= left && x <= right && y >= top && y <= bottom) {
                dragView = view;
                dragPosition = i;
                return view;
            }
        }

        return null;
    }

    private int getTargetPosition(int x, int y) {

        for (int i = 0; i < myAppList.size(); i++) {
            View view = myAppList.get(i);
            int left = view.getLeft();
            int top = view.getTop();
            int bottom = view.getBottom();
            int right = view.getRight();
            if (x >= left && x <= right && y >= top && y <= bottom) {
                return i;
            }
        }
        return -1;
    }



    public void changeMode(int mode) {
        mCurrentMode = mode;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        myAppList.clear();
        groupsViews.clear();
        positions.clear();
        int childCount = getChildCount();
        View view = getChildAt(0);
        view.layout(0, 0, screenWidth, mMarginTop);
        int totalHeight = mMarginTop;
        int totalWidth = 0;
        int myAppCellSize = 0;
        for (int i = 1; i < childCount; i++) {
            view = getChildAt(i);
            String tag = (String) view.getTag();
            if (tag.startsWith("title")) {
                view.layout(0, totalHeight, view.getMeasuredWidth(), view.getMeasuredHeight() + totalHeight);
                totalHeight = totalHeight + view.getMeasuredHeight();


                String[] arr = tag.split("###");
                int groupId = Integer.parseInt(arr[1]);
            } else if (tag.startsWith("cell")) {
                String[] arr = tag.split("###");
                int size = Integer.parseInt(arr[1]);
                int position = Integer.parseInt(arr[2]);
                int groupId = Integer.parseInt(arr[3]);
                boolean isMyApp = Boolean.parseBoolean(arr[4]);
                if (isMyApp) {
                    myAppList.add(view);
                    positions.add(-1);
                } else {
                    List<View> list = groupsViews.get(groupId);
                    if (list == null) {
                        list = new ArrayList<>();
                        groupsViews.put(groupId, list);
                    }
                    list.add(view);
                }
                view.layout(totalWidth, totalHeight, view.getMeasuredWidth() + totalWidth, view.getMeasuredHeight() + totalHeight);
                if ((position > 0 && (position + 1) % 4 == 0) || position == size - 1) {
                    totalHeight = totalHeight + view.getMeasuredHeight();
                    totalWidth = 0;
                } else {
                    totalWidth = totalWidth + view.getMeasuredWidth();
                }

            }
        }
    }

    public void removeAllViewsWithoutFirst() {
        for (int i = getChildCount() - 1; i > 0; i--) {
            removeViewAt(i);
        }
    }

    public int getGroupTitleHeight() {
        return mGroupTitleHeight;
    }

    public int getCellHeight() {
        return mCellHeight;
    }

    public void setTopHeight(int height) {
        LayoutParams params = mTopView.getLayoutParams();
        params.height = height;
        mMarginTop = height;
        requestLayout();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = 0;
        int measureHeight = mMarginTop;
        final int childCount = getChildCount();
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int lastGroupId = -1;
        int height = 0;
        for (int i = 1; i < childCount; i++) {
            View view = getChildAt(i);
            String tag = (String) view.getTag();
            if (tag.startsWith("title")) {
                measureHeight = measureHeight + mGroupTitleHeight;
                view.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mGroupTitleHeight, MeasureSpec.EXACTLY));
            } else if (tag.startsWith("cell")) {

                view.measure(MeasureSpec.makeMeasureSpec(screenWidth / 4, MeasureSpec.EXACTLY)
                        , MeasureSpec.makeMeasureSpec(mCellHeight, MeasureSpec.EXACTLY));
                String[] arr = tag.split("###");
                int size = Integer.parseInt(arr[1]);
                int position = Integer.parseInt(arr[2]);
                if (position % 4 == 0) {
                    measureHeight = measureHeight + mCellHeight;
                }
            }

        }

        int widthSpaceSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthSpaceMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpaceSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightSpaceMode = MeasureSpec.getMode(heightMeasureSpec);
        setMeasuredDimension(widthSpaceSize, measureHeight);
//        if (childCount == 0) {
//            setMeasuredDimension(0, 0);
//        } else if (widthSpaceMode == MeasureSpec.AT_MOST && heightSpaceMode == MeasureSpec.AT_MOST) {
//
//        }
    }


    @Override
    public boolean onLongClick(View v) {
        if (mCurrentMode == AppManageActivity.MODE_MODIFY) {

            View view = getDragView(mLastDownX, mLastDownY);

            if (view == null) {
                return true;
            }
            isLongClick = true;
            int centerX = (view.getRight() + view.getLeft()) / 2;
            int centerY = (view.getTop() + view.getBottom()) / 2;
//            int translationX = (int) view.getTranslationX();
//            int translationY = (int) view.getTranslationY();
            view.setTranslationX(mLastDownX - centerX);
            view.setTranslationY(mLastDownY - centerY);
        } else {
            dragView = null;
        }
        return true;
    }

    /***
     * 点击时移动的目标位置
     *
     * @param groupId group列表ID
     * @param size      group包含多少item
     * @param isMyApp
     * @return
     */
    public Point getNextTargetPosition(int groupId, int size, boolean isMyApp) {

        View view = findViewWithTag("cell###" + size + "###" + (size - 1) + "###" + groupId + "###" + isMyApp);
        if (view != null) {
            Point point = new Point(0, 0);
            if (size % 4 == 0) {
                point.x = 0;
                point.y = view.getTop() + view.getHeight();
            } else {
                point.x = view.getLeft() + view.getWidth();
                point.y = view.getTop();
            }
            return point;
        }
        view = findViewWithTag("title###" + (isMyApp ? Integer.MAX_VALUE : groupId));
        if (view == null) return null;
        Point point = new Point(view.getLeft(), view.getBottom());
        return point;
    }


    public List<View> getMyAppViews() {
        return myAppList;
    }

    public List<View> getGroupViews(int groupId) {
        return groupsViews.get(groupId);
    }

    /***
     * @param groupId groupId=Integer.MAX_VALUE表示我的应用
     * @return
     */
    public int getGroupEndPosition(int groupId) {
        if (groupId == Integer.MAX_VALUE) {
            return 1 + myAppList.size();
        }
        List<View> list = groupsViews.get(groupId);
        if (list != null && !list.isEmpty()) {
            View view = list.get(list.size() - 1);
            for (int i = 0; i < getChildCount(); i++) {
                if (view == getChildAt(i)) {
                    return i;
                }
            }
        }
        View view = findViewWithTag("title###" + groupId);
        for (int i = 0; i < getChildCount(); i++) {
            if (view == getChildAt(i)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener == null) {
            return;
        }
        int position = getTargetPosition(mLastDownX, mLastDownY);
        if (position == -1) {
            return;
        }
        itemClickListener.onItemClick(null, myAppList.get(position), position, 0);
    }

    public void setOnRearrangeListener(OnRearrangeListener l) {
        this.onRearrangeListener = l;
    }

    public void setMyAppItemClick(AdapterView.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


}
