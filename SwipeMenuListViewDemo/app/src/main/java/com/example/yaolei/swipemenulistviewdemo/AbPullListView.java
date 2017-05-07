/*
 * Copyright (C) 2013 www.418log.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.yaolei.swipemenulistviewdemo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Scroller;

import java.lang.reflect.Field;

// TODO: Auto-generated Javadoc

/**
 * The Class AbPullListView.
 */
public class AbPullListView extends ListView implements OnScrollListener {

	/** The m last y. */
	private float mLastY = -1;

	/** The m scroller. */
	private Scroller mScroller;

	/** The m list view listener. */
	private AbOnListViewListener mListViewListener;

	/** The m header view. */
	private AbListViewHeader mHeaderView;

	/** The m footer view. */
	private AbListViewFooter mFooterView;

	/** The m header view height. */
	private int mHeaderViewHeight;

	/** The m footer view height. */
	private int mFooterViewHeight;

	/** The m enable pull refresh. */
	private boolean mEnablePullRefresh = true;

	/** The m enable pull load. */
	private boolean mEnablePullLoad = true;

	/** The m pull refreshing. */
	private boolean mPullRefreshing = false;

	/** The m pull loading. */
	private boolean mPullLoading = false;

	/** The m is footer ready. */
	@SuppressWarnings("unused")
	private boolean mIsFooterReady = false;
	/** The m is footer ready. */
	@SuppressWarnings("unused")
	private boolean mIsPull = false;

	/** 总条数. */
	public int mTotalItemCount;

	/** The m scroll back. */
	private int mScrollBack;
	/** The m scroll back. */
	private int mScrollBack_ = 1;

	/** The Constant SCROLLBACK_HEADER. */
	private final static int SCROLLBACK_HEADER = 0;

	/** The Constant SCROLLBACK_FOOTER. */
	private final static int SCROLLBACK_FOOTER = 1;

	/** The Constant SCROLL_DURATION. */
	private final static int SCROLL_DURATION = 200;

	/** The Constant OFFSET_RADIO. */
	private final static float OFFSET_RADIO = 1.8f;

	/** 数据相关. */
	private ListAdapter mAdapter = null;

	/** 上一次的数量 */
	private int count = 0;

	/**
	 * 构造.
	 * 
	 * @param context
	 *            the context
	 */
	public AbPullListView(Context context) {
		super(context);
		initView(context);
		chanceEdgeGlowTop();
		chanceEdgeGlowBottom();
	}

	private void chanceEdgeGlowTop() {
		try {
			Field f = AbsListView.class.getDeclaredField("mEdgeGlowTop");
			f.setAccessible(true);
			Object o = f.get(this);

			f = f.getType().getDeclaredField("mGlow");

			f.setAccessible(true);

			Drawable drawable = (Drawable) f.get(o);
			drawable = getResources().getDrawable(R.drawable.transparent);

			f.set(o, drawable);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void chanceEdgeGlowBottom() {
		try {
			Field f = AbsListView.class.getDeclaredField("mEdgeGlowBottom");
			f.setAccessible(true);
			Object o = f.get(this);
			
			f = f.getType().getDeclaredField("mGlow");
			
			f.setAccessible(true);
			
			Drawable drawable = (Drawable) f.get(o);
			drawable = getResources().getDrawable(R.drawable.transparent);
			
			f.set(o, drawable);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 构造.
	 * 
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 */
	public AbPullListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		/**
		 * 
		try {
			chanceEdgeGlowTop();
			chanceEdgeGlowBottom();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 */
	}

	/**
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public AbPullListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	/**
	 * 初始化View.
	 * 
	 * @param context
	 *            the context
	 */
	private void initView(Context context) {

		mScroller = new Scroller(context, new DecelerateInterpolator());

		super.setOnScrollListener(this);

		// init header view
		mHeaderView = new AbListViewHeader(context);

		// init header height
		mHeaderViewHeight = mHeaderView.getHeaderHeight();
		mHeaderView.setGravity(Gravity.BOTTOM);
		addHeaderView(mHeaderView);
		mHeaderView.setVisiableHeight(0);

		// init footer view
		mFooterView = new AbListViewFooter(context);
		mFooterViewHeight = mFooterView.getFooterHeight();
		addFooterView(mFooterView);
		// 先隐藏
		mFooterView.setVisiableHeight(0);
		// 默认是打开刷新与更多
		setPullRefreshEnable(true);
		setPullLoadEnable(true);

	}

	/**
	 * 描述：设置适配器
	 */
	@Override
	public void setAdapter(ListAdapter adapter) {
		mAdapter = adapter;
		// if (mIsFooterReady == false) {
		// mIsFooterReady = true;
		// mFooterView.setGravity(Gravity.TOP);
		// addFooterView(mFooterView);
		// }
		super.setAdapter(adapter);
	}

	/**
	 * 打开或者关闭下拉刷新功能.
	 * 
	 * @param enable
	 *            开关标记
	 */
	public void setPullRefreshEnable(boolean enable) {
		mEnablePullRefresh = enable;
		if (!mEnablePullRefresh) {
			mHeaderView.setVisibility(View.INVISIBLE);
		} else {
			mHeaderView.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 打开或者关闭加载更多功能.
	 * 
	 * @param enable
	 *            开关标记
	 */
	public void setPullLoadEnable(boolean enable) {
		mEnablePullLoad = enable;
		if (!mEnablePullLoad) {
			mFooterView.setVisibility(View.INVISIBLE);
		} else {
			mFooterView.setVisibility(View.VISIBLE);
		}
		// if (!mEnablePullLoad) {
		// mFooterView.setVisiableHeight(0);
		// mFooterView.setOnClickListener(null);
		// } else {
		// mPullLoading = false;
		// mFooterView.setState(AbListViewFooter.STATE_READY);
		// // load more点击事件.
		// mFooterView.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// // startLoadMore();
		// }
		// });
		// }
	}

	/**
	 * 停止刷新并重置header的状态.
	 */
	public void stopRefresh() {
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight();
		}
		count = mAdapter.getCount();
		// 判断有没有数据
		if (count > 0) {
			mFooterView.setState(AbListViewFooter.STATE_READY);
		} else {
			mFooterView.setState(AbListViewFooter.STATE_NORMAL);
		}
	}

	/**
	 * 更新header的高度.
	 * 
	 * @param delta
	 *            差的距离
	 */
	private void updateHeaderHeight(float delta) {
		int newHeight = (int) delta + mHeaderView.getVisiableHeight();
		mHeaderView.setVisiableHeight(newHeight);
		if (mEnablePullRefresh) {
			if (mHeaderView.getVisiableHeight() >= mHeaderViewHeight) {
				mHeaderView.setState(AbListViewHeader.STATE_READY);
			} else {
				mHeaderView.setState(AbListViewHeader.STATE_NORMAL);
			}
		}
		setSelection(0);
	}

	/**
	 * 更新footer的高度.
	 * 
	 * @param delta
	 *            差的距离
	 */
	private void updateFooterHeight(float delta) {
		int newHeight = (int) delta + mFooterView.getVisiableHeight();
		if(delta<0)
			newHeight = (int) (delta + mFooterView.getVisiableHeight());
			
		mFooterView.setVisiableHeight(newHeight);
		if (mEnablePullLoad) {
			if (mFooterView.getVisiableHeight() >= mFooterViewHeight) {
				mFooterView.setState(AbListViewFooter.STATE_READY);
			} else {
				mFooterView.setState(AbListViewFooter.STATE_NORMAL);
			}
		}
		// setSelection(0);
	}

	/**
	 * 根据状态设置Header的位置.
	 */
	private void resetHeaderHeight() {
		// 当前下拉到的高度
		int height = mHeaderView.getVisiableHeight();
		if (height < mHeaderViewHeight || !mPullRefreshing) {
			// 距离短 隐藏
			mScrollBack = SCROLLBACK_HEADER;
			mScroller.startScroll(0, height, 0, -1 * height, SCROLL_DURATION);
		} else if (height > mHeaderViewHeight || !mPullRefreshing) {
			// 距离多的 弹回到mHeaderViewHeight
			mScrollBack = SCROLLBACK_HEADER;
			mScroller.startScroll(0, height, 0,
					-(int) (height - 1.1 * mHeaderViewHeight), SCROLL_DURATION);
		}

		invalidate();
	}

	/**
	 * 根据状态设置Footer的位置.
	 */
	private void resetFooterHeight() {
		// 当前下拉到的高度
		int height = mFooterView.getVisiableHeight();
		if (height < mFooterViewHeight || !mPullRefreshing) {
			// 距离短 隐藏
			mScrollBack_ = SCROLLBACK_FOOTER;
			mScroller.startScroll(0, height, 0, -1 * height, SCROLL_DURATION);
		} else if (height > mFooterViewHeight || !mPullRefreshing) {
			// 距离多的 弹回到mHeaderViewHeight
			mScrollBack_ = SCROLLBACK_FOOTER;
			mScroller.startScroll(0, height, 0, -(height - mFooterViewHeight),
					SCROLL_DURATION);
		}
		mHeaderView.setState(AbListViewHeader.STATE_NORMAL);
		invalidate();
	}

	/**
	 * 开始加载更多.
	 */
	@SuppressWarnings("unused")
	private void startLoadMore() {
		Log.d("TAG", "startLoadMore");
		// mFooterView.show();
		mPullLoading = true;
		// mFooterView.setState(AbListViewFooter.STATE_LOADING);
		if (mListViewListener != null) {
			// 开始下载数据
			mListViewListener.onLoadMore();
		}
	}

	/**
	 * 停止加载更多并重置footer的状态.
	 * 
	 */
	public void stopLoadMore() {
		mFooterView.setVisiableHeight(0);
		mPullLoading = false;
		int countNew = mAdapter.getCount();
		// 判断有没有更多数据了
		if (countNew > count) {
			mFooterView.setState(AbListViewFooter.STATE_READY);
		} else {
			mFooterView.setState(AbListViewFooter.STATE_NORMAL);
		}
	}

	/**
	 * 描述：onTouchEvent
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float deltaY = ev.getRawY() - mLastY;
			mLastY = ev.getRawY();
			if (mEnablePullRefresh && getFirstVisiblePosition() == 0
					&& (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
				updateHeaderHeight(deltaY / OFFSET_RADIO);
				mIsPull = true;
			}
			if (mEnablePullLoad && !mPullLoading
					&& getLastVisiblePosition() == mTotalItemCount - 1) {
				// startLoadMore();
				// if(deltaY<0)
				updateFooterHeight(-deltaY / OFFSET_RADIO);
			}
			
			break;
		case MotionEvent.ACTION_UP:
			mIsPull = false;
			mLastY = -1;
			if (getFirstVisiblePosition() == 0) {
				// 需要刷新的条件
				if (mEnablePullRefresh
						&& mHeaderView.getVisiableHeight() >= mHeaderViewHeight) {
					mPullRefreshing = true;
					mHeaderView.setState(AbListViewHeader.STATE_REFRESHING);
					if (mListViewListener != null) {
						// 刷新
						mListViewListener.onRefresh();
					}
				}
				// 弹回
				resetHeaderHeight();
			}
			if (getLastVisiblePosition() == mTotalItemCount - 1) {
				// 需要刷新的条件
				if (mEnablePullLoad
						&& mFooterView.getVisiableHeight() >= mFooterViewHeight) {
					mPullRefreshing = true;
					mFooterView.setState(AbListViewFooter.STATE_REFRESHING);
					if (mListViewListener != null) {
						// 刷新
						mListViewListener.onLoadMore();
					}
				}
				// 弹回
				resetFooterHeight();
			}
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 描述：TODO
	 * 
	 * @see View#computeScroll()
	 */
	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				mHeaderView.setVisiableHeight(mScroller.getCurrY());
			}
			if (mScrollBack_ == SCROLLBACK_FOOTER) {
				mFooterView.setVisiableHeight(mScroller.getCurrY());
			}
			postInvalidate();
		}
		super.computeScroll();
	}

	/**
	 * 描述：设置ListView的监听器.
	 * 
	 * @param listViewListener
	 */
	public void setAbOnListViewListener(AbOnListViewListener listViewListener) {
		mListViewListener = listViewListener;
	}

	/**
	 * 描述：TODO
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	/**
	 * 描述：TODO
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if(mIsPull){
			mTotalItemCount = totalItemCount - 1;
		}else{
			mTotalItemCount = totalItemCount;
		}
	}

	/**
	 * 
	 * 描述：获取Header View
	 * 
	 * @return
	 * @throws
	 */
	public AbListViewHeader getHeaderView() {
		return mHeaderView;
	}

	/**
	 * 
	 * 描述：获取Footer View
	 * 
	 * @return
	 * @throws
	 */
	public AbListViewFooter getFooterView() {
		return mFooterView;
	}

	/**
	 * 
	 * 描述：获取Header ProgressBar，用于设置自定义样式
	 * 
	 * @return
	 * @throws
	 */
	public ProgressBar getHeaderProgressBar() {
		return mHeaderView.getHeaderProgressBar();
	}

	/**
	 * 
	 * 描述：获取Footer ProgressBar，用于设置自定义样式
	 * 
	 * @return
	 * @throws
	 */
	public ProgressBar getFooterProgressBar() {
		return mFooterView.getHeaderProgressBar();
	}

}
