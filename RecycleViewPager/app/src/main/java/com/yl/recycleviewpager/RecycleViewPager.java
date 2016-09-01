package com.yl.recycleviewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2016/8/31.
 */
public class RecycleViewPager extends ViewPager {

    private OnPageChangeListener mExtraOnPageChangeListener = null;
    private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {

        private Runnable runnable;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (position >= getAdapter().getCount() - 2) {
                return;
            }

            if (position == 0) {
                return;
            }
            position--;
            if (mExtraOnPageChangeListener != null) {
                mExtraOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (position == getAdapter().getCount() - 1) {
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        setCurrentItem(0, false);
                    }
                };


            } else if (position == 0) {
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        setCurrentItem(getAdapter().getCount() - 3, false);
                    }
                };
            } else {
                if (mExtraOnPageChangeListener != null) {
                    mExtraOnPageChangeListener.onPageSelected(position - 1);
                }
                if (position == getAdapter().getCount() - 2) {
                    if (mExtraOnPageChangeListener != null) {
                        mExtraOnPageChangeListener.onPageScrolled(position - 1, 0, 0);
                    }
                }
            }


        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mExtraOnPageChangeListener != null) {
                mExtraOnPageChangeListener.onPageScrollStateChanged(state);
            }
            if (state == 0) {
                if (runnable != null) {
                    post(runnable);
                    runnable = null;
                }
            }
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
        mExtraOnPageChangeListener = listener;
    }

    @Override
    public void setCurrentItem(int item) {
        if (getAdapter().getCount() == 0) {
            return;
        }
        super.setCurrentItem(item + 1);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        if (getAdapter().getCount() == 0) {
            return;
        }
        super.setCurrentItem(item + 1, smoothScroll);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        setCurrentItem(0);
    }

    @Override
    public int getCurrentItem() {
        if (getAdapter().getCount() == 0) {
            return -1;
        }
        int currentItem = super.getCurrentItem();
        if (currentItem == 0) {
            return getAdapter().getCount() - 3;
        }
        if (currentItem == getAdapter().getCount() - 1) {
            return 0;
        }
        return super.getCurrentItem() - 1;
    }

}
