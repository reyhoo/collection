package com.yl.recycleviewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2016/8/31.
 */
public class RecycleViewPager extends ViewPager {

    private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    public RecycleViewPager(Context context) {
        super(context);
        init();
    }


    public RecycleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        super.setOnPageChangeListener(mOnPageChangeListener);
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {

    }

    @Override
    public int getCurrentItem() {
        int currentItem = super.getCurrentItem();
        if (currentItem == 0) {
            return getChildCount() - 2;
        }else if(currentItem == getChildCount()-1){
            return 0;
        }
        return super.getCurrentItem() - 1;
    }

}
