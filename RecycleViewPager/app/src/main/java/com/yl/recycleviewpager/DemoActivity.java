package com.yl.recycleviewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/30.
 */
public class DemoActivity extends FragmentActivity implements View.OnClickListener {
    private RecycleViewPager mViewPager;
    //    private ArrayList<String> list1 = new ArrayList<>();
//    private ArrayList<String> list2 = new ArrayList<>();
//    private ArrayList<String> list3 = new ArrayList<>();
    private List<ArrayList<String>> items = new ArrayList<>();

    private static final String TAG = "DemoActivity_";

    private LinearLayout indicateLayout;
    private View indicateView;
    private float mIndicateWidth;
    private ImageView currPoint;

    private LinearLayout pointLayout;

    private LinearLayout buttonLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        final int size = 1;
        for (int i = 0; i < size; i++) {
            ArrayList<String> list = new ArrayList<>();
            for (int j = 0; j < 40; j++) {
                list.add("list" + (i) + "###" + (j + 1));
            }
            items.add(list);
        }

        mViewPager = (RecycleViewPager) findViewById(R.id.viewPager);
        indicateLayout = (LinearLayout) findViewById(R.id.indicate_layout);
        indicateView = findViewById(R.id.indicate_view);
        currPoint = (ImageView) findViewById(R.id.curr_point);
        pointLayout = (LinearLayout) findViewById(R.id.pointLayout);
        buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        pointLayout.removeAllViews();
        float density = getResources().getDisplayMetrics().density;

        for (int i = 0; i < size; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (8 * density), (int) (8 * density));
            ImageView iv = new ImageView(this);
            if (i != 0) {
                params.leftMargin = (int) (15 * density);
            }
            iv.setImageResource(R.drawable.point);
            iv.setLayoutParams(params);
            pointLayout.addView(iv);

        }
        for (int i = 0; i < size; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            Button btn = new Button(this);
            btn.setTag(i);
            btn.setOnClickListener(this);
            btn.setLayoutParams(params);
            buttonLayout.addView(btn);
        }
        int left = (int) ((getResources().getDisplayMetrics().widthPixels - (size - 1) * 15 * density - 8 * density * size) / 2);
        ((FrameLayout.LayoutParams) currPoint.getLayoutParams()).leftMargin = left;
        currPoint.requestLayout();

//        MyFragmentAdapter adapter = new MyFragmentAdapter(items, getSupportFragmentManager());
        indicateLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                indicateLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mIndicateWidth = indicateLayout.getWidth() * 1f / size;
                indicateView.getLayoutParams().width = (int) mIndicateWidth;
                indicateView.requestLayout();
                initAdapter();
            }
        });

    }

    private void updateIndicate(int position, float positionOffset) {
        int x = (int) (position * mIndicateWidth + positionOffset * mIndicateWidth);
        indicateLayout.scrollTo(-x, 0);
        float density = getResources().getDisplayMetrics().density;
        float translationX = (position * 23 * density + positionOffset * 23 * density);

        currPoint.setTranslationX(translationX);
    }
    private void initAdapter(){
        MyPagerAdapter adapter = new MyPagerAdapter(this,items);
        mViewPager.setAdapter(adapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i(TAG, TAG + "onPageScrolled:position:" + position + ";positionOffset:" + positionOffset + ";positionOffsetPixels:" + positionOffsetPixels);

                updateIndicate(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                Log.i(TAG, TAG + "onPageSelected:position:" + position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i(TAG, TAG + "onPageScrollStateChanged:state:" + state);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Integer position = (Integer) v.getTag();
        if (position != null) {
            mViewPager.setCurrentItem(position);
        }
    }
}
