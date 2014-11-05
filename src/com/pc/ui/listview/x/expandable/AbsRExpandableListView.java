/**
 * @(#)AbsRExpandableListView.java 2013-10-23 Copyright 2013 it.kedacom.com,
 *                                 Inc. All rights reserved.
 */

package com.pc.ui.listview.x.expandable;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.pc.ui.listview.x.libraries.IRListViewListener;
import com.pc.ui.listview.x.libraries.OnRScrollListener;
import com.pc.ui.listview.x.libraries.RListViewFooter;
import com.pc.ui.listview.x.libraries.RListViewHeader;
import com.privatecustom.publiclibs.R;

/**
 * @author chenjian
 * @date 2013-10-23
 */

public abstract class AbsRExpandableListView extends ExpandableListView implements OnScrollListener {

	protected float mLastY = -1;
	// used for scroll back
	protected Scroller mScroller;
	protected OnScrollListener mScrollListener;

	// 下拉刷新/上拉加载接口
	protected IRListViewListener mListViewListener;

	// 下拉刷新页眉
	protected RListViewHeader mRHeaderView;
	protected RelativeLayout mRHeaderViewContent;
	protected TextView mSubRHeaderTextView;
	protected int mRHeaderViewHeight; // header view's height
	protected boolean mEnablePullRefresh = true;//
	protected boolean mPullRefreshing = false; // is refreashing.
	protected boolean mEnableDropdownRebound = true;

	// 上拉加载页脚
	protected RListViewFooter mRFooterView;
	protected boolean mEnablePullLoad;
	protected boolean mPullLoading;
	protected boolean mIsAddFooter;
	// 下拉回弹效果
	protected boolean mEnablePullOnRebound = true;

	// total list items, used to detect is at the bottom of listview.
	protected int mTotalItemCount;

	// for mScroller, scroll back from header or footer.
	protected int mScrollBack;
	protected final int SCROLLBACK_HEADER = 0;
	protected final int SCROLLBACK_FOOTER = 1;

	// scroll back duration
	protected final int SCROLL_DURATION = 400;
	// when pull up >= 50px at bottom, trigger load more.
	protected final int PULL_LOAD_MORE_DELTA = 50;
	// support iOS like pull feature.
	protected final float OFFSET_RADIO = 1.8f;

	protected View mExtendHeaderView;
	protected View mExtendFooterView;

	protected boolean mAddedLvExtendFooter;

	protected FrameLayout mLvExtendHeaderFrame;
	protected FrameLayout mLvExtendFooterFrame;

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public AbsRExpandableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		initWithContext(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public AbsRExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);

		initWithContext(context);
	}

	/**
	 * @param context
	 */
	public AbsRExpandableListView(Context context) {
		super(context);

		initWithContext(context);
	}

	protected void initWithContext(Context context) {
		mScroller = new Scroller(context, new DecelerateInterpolator());
		super.setOnScrollListener(this);

		this.setGroupIndicator(null);

		// init header view
		mRHeaderView = new RListViewHeader(context);
		mRHeaderViewContent = (RelativeLayout) mRHeaderView.findViewById(R.id.header_content);
		mSubRHeaderTextView = (TextView) mRHeaderView.findViewById(R.id.subHeaderText);
		addHeaderView(mRHeaderView, null, false);

		// 外部扩展页眉、页脚
		mLvExtendHeaderFrame = new FrameLayout(getContext());
		mLvExtendFooterFrame = new FrameLayout(getContext());
		addHeaderView(mLvExtendHeaderFrame, null, false);

		// init footer view
		mRFooterView = new RListViewFooter(context);

		// init header height
		mRHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				mRHeaderViewHeight = mRHeaderViewContent.getHeight();
				getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});
	}

	/**
	 * 启用或禁用拉刷新功能。
	 * @param enable
	 */
	public void setPullRefreshEnable(boolean enable) {
		mEnablePullRefresh = enable;
		if (!mEnablePullRefresh) { // disable, hide the content
			mRHeaderViewContent.setVisibility(View.INVISIBLE);
		} else {
			mEnableDropdownRebound = true;
			mRHeaderViewContent.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 是否可下拉回弹
	 * 
	 * <pre>
	 * Note:如果可以下拉刷新，则下拉回弹效果一直存在
	 * </pre>
	 * @param enable
	 */
	public void setDropdownReboundEnable(boolean enable) {
		if (mEnablePullRefresh) {
			mEnableDropdownRebound = true;
			return;
		}

		mEnableDropdownRebound = enable;
	}

	/**
	 * 启用或禁用上拉刷新
	 * @param enable
	 */
	public void setPullLoadEnable(boolean enable) {
		mEnablePullLoad = enable;
		if (!mEnablePullLoad) {
			mRFooterView.hide();
			mRFooterView.setOnClickListener(null);
		} else {
			mPullLoading = false;
			mEnablePullOnRebound = true;
			mRFooterView.show();
			mRFooterView.setState(RListViewFooter.STATE_NORMAL);
			// both "pull up" and "click" will invoke load more.
			mRFooterView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					startLoadMore();
				}
			});
		}
	}

	/**
	 * 是否可上拉回弹
	 * 
	 * <pre>
	 * Note:如果可以上拉刷新，则上拉回弹效果一直存在
	 * </pre>
	 * @param enable
	 */
	public void setPullOnReboundEnable(boolean enable) {
		if (mEnablePullLoad) {
			mEnablePullOnRebound = true;
			return;
		}

		mEnablePullOnRebound = enable;
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	public void setRListViewListener(IRListViewListener l) {
		mListViewListener = l;
	}

	@Override
	public void setAdapter(ExpandableListAdapter adapter) {
		// 添加一个外部页脚
		if (null != mLvExtendFooterFrame && !mAddedLvExtendFooter) {
			addFooterView(mLvExtendFooterFrame, null, false);
			mAddedLvExtendFooter = true;
		}

		// 上拉加载页脚
		if (!mIsAddFooter) {
			mIsAddFooter = true;
			addFooterView(mRFooterView, null, false);
		}

		super.setAdapter(adapter);
	}

	/**
	 * stop refresh, reset header view.
	 */
	public void stopRefresh() {
		if (!mPullRefreshing) {
			return;
		}

		mPullRefreshing = false;
		resetHeaderHeight();
	}

	public void startLoadMore() {
		mPullLoading = true;
		mRFooterView.setState(RListViewFooter.STATE_LOADING);
		if (mListViewListener != null) {
			mListViewListener.onLoadMore();
		}
	}

	/**
	 * stop load more, reset footer view.
	 */
	public void stopLoadMore() {
		if (!mPullLoading) {
			return;
		}

		mPullLoading = false;
		mRFooterView.setState(RListViewFooter.STATE_NORMAL);
	}

	/**
	 * @param time
	 */
	public void setSubHeaderText(String subHeadText) {
		if (TextUtils.isEmpty(subHeadText)) {
			mSubRHeaderTextView.setVisibility(View.GONE);
		} else {
			mSubRHeaderTextView.setText(subHeadText);
			mSubRHeaderTextView.setVisibility(View.VISIBLE);
		}
	}

	protected void invokeOnScrolling() {
		if (mScrollListener instanceof OnRScrollListener) {
			OnRScrollListener l = (OnRScrollListener) mScrollListener;
			l.onRScrolling(this);
		}
	}

	protected void updateHeaderHeight(float delta) {
		if (!mEnableDropdownRebound) {
			return;
		}

		mRHeaderView.setVisiableHeight((int) delta + mRHeaderView.getVisiableHeight());

		// 未处于刷新状态，更新箭头
		if (mEnablePullRefresh && !mPullRefreshing) {
			if (mRHeaderView.getVisiableHeight() > mRHeaderViewHeight) {
				mRHeaderView.setState(RListViewHeader.STATE_READY);
			} else {
				mRHeaderView.setState(RListViewHeader.STATE_NORMAL);
			}
		}

		// scroll to top each time
		setSelection(0);
	}

	/**
	 * reset header view's height.
	 */
	protected void resetHeaderHeight() {
		int height = mRHeaderView.getVisiableHeight();

		// not visible.
		if (height == 0) {
			return;
		}

		// 如果正在刷新，Header View 并没有显示完全，则什么也不做
		if (mPullRefreshing && height <= mRHeaderViewHeight) {
			return;
		}

		int finalHeight = 0; // default: scroll back to dismiss header.

		// 如果正在刷新,只是回弹到Header view
		if (mPullRefreshing && height > mRHeaderViewHeight) {
			finalHeight = mRHeaderViewHeight;
		}
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);

		// trigger computeScroll
		invalidate();
	}

	protected void updateFooterHeight(float delta) {
		if (!mEnablePullOnRebound) {
			return;
		}

		int height = mRFooterView.getBottomMargin() + (int) delta;
		if (mEnablePullLoad && !mPullLoading) {
			// height enough to invoke load more.
			if (height > PULL_LOAD_MORE_DELTA) {
				mRFooterView.setState(RListViewFooter.STATE_READY);
			} else {
				mRFooterView.setState(RListViewFooter.STATE_NORMAL);
			}
		}
		mRFooterView.setBottomMargin(height);

		// setSelection(mTotalItemCount - 1); // scroll to bottom
	}

	protected void resetFooterHeight() {
		int bottomMargin = mRFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLLBACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin, SCROLL_DURATION);

			invalidate();
		}
	}

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
				if (getFirstVisiblePosition() == 0 && (mRHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
					// the first item is showing, header has shown or pull down.
					updateHeaderHeight(deltaY / OFFSET_RADIO);
					invokeOnScrolling();
				} else if (getLastVisiblePosition() == mTotalItemCount - 1
						&& (mRFooterView.getBottomMargin() > 0 || deltaY < 0)) {
					// last item, already pulled up or want to pull up.
					updateFooterHeight(-deltaY / OFFSET_RADIO);
				}
				break;

			default:
				mLastY = -1; // reset
				if (getFirstVisiblePosition() == 0) {
					// invoke refresh
					if (mEnablePullRefresh && mRHeaderView.getVisiableHeight() > mRHeaderViewHeight) {
						mPullRefreshing = true;
						mRHeaderView.setState(RListViewHeader.STATE_REFRESHING);
						if (mListViewListener != null) {
							mListViewListener.onRefresh();
						}
					}
					resetHeaderHeight();
				} else if (getLastVisiblePosition() == mTotalItemCount - 1) {
					// invoke load more.
					if (mEnablePullLoad && mRFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
						startLoadMore();
					}
					resetFooterHeight();
				}
				break;
		}

		return super.onTouchEvent(ev);
	}

	public void startRefresh() {
		mPullRefreshing = true;
		mRHeaderView.setState(RListViewHeader.STATE_REFRESHING);
		if (mListViewListener != null) {
			mListViewListener.onRefresh();
		}
		resetHeaderHeight();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// send to user's listener
		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				mRHeaderView.setVisiableHeight(mScroller.getCurrY());
			} else {
				mRFooterView.setBottomMargin(mScroller.getCurrY());
			}
			postInvalidate();
			invokeOnScrolling();
		}
		super.computeScroll();
	}

	/**
	 * 设置页眉
	 * @param headerView
	 */
	public void setExtendHeaderView(View headerView) {
		mLvExtendHeaderFrame.removeAllViews();
		mExtendHeaderView = headerView;

		if (null == headerView) {
			return;
		}

		final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
		mLvExtendHeaderFrame.addView(mExtendHeaderView, lp);
		mExtendHeaderView.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置页眉是否显示
	 * @param visible 显示
	 */
	public void setExtendHeaderViewVisibility(boolean visible) {
		if (null == mExtendHeaderView) {
			return;
		}

		if (visible) {
			mExtendHeaderView.setVisibility(View.VISIBLE);
		} else {
			mExtendHeaderView.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置页脚
	 * @param footerView
	 */
	public void setExtendFooterView(View footerView) {
		mLvExtendFooterFrame.removeAllViews();
		mExtendFooterView = footerView;

		if (null == footerView) {
			return;
		}

		final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
		mLvExtendFooterFrame.addView(mExtendFooterView, lp);
		mExtendFooterView.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置页脚是否显示
	 * @param visible 显示
	 */
	public void setExtendFooterViewVisibility(boolean visible) {
		if (null == mExtendFooterView) {
			return;
		}

		if (visible) {
			mExtendFooterView.setVisibility(View.VISIBLE);
		} else {
			mExtendFooterView.setVisibility(View.GONE);
		}
	}
}
