/**
 * @(#)PListView.java 2013-10-14 Copyright 2013 it.kedacom.com, Inc. All rights
 *                    reserved.
 */

package com.pc.ui.listview.x;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.pc.ui.listview.x.libraries.IPListViewListener;
import com.pc.ui.listview.x.libraries.OnPScrollListener;
import com.pc.ui.listview.x.libraries.RListViewHeader;
import com.privatecustom.publiclibs.R;

/**
 * 简单的下拉刷新ListView，禁用下拉刷新时不可回弹
 * @author chenjian
 * @date 2013-10-14
 */

public class SPListView extends ListView implements OnScrollListener {

	// save event y
	private float mLastY = -1;
	// used for scroll back
	private Scroller mScroller;
	// user's scroll listener
	private OnScrollListener mScrollListener;

	// 下拉刷新/上拉加载接口
	private IPListViewListener mListViewListener;

	// 页眉
	private RListViewHeader mHeaderView;
	// header view content, use it to calculate the Header's height. And hide it
	// when disable pull refresh.
	private RelativeLayout mHeaderViewContent;
	private TextView mSubHeaderTextView;
	private int mHeaderViewHeight; // header view's height
	private boolean mEnablePullRefresh = true;//
	private boolean mPullRefreshing; // is refreashing.
	private boolean mEnableDropdownRebound;

	private int mDefHeaderH;

	// 页脚
	private FrameLayout mLvFooterFrame;
	private boolean mIsAddFooter;
	private View mFooterView;

	// total list items, used to detect is at the bottom of listview.
	// private int mTotalItemCount;

	// for mScroller, scroll back from header or footer.
	private int mScrollBack;
	private final int SCROLLBACK_HEADER = 0;

	// scroll back duration
	private final int SCROLL_DURATION = 400;
	// support iOS like pull feature.
	private final float OFFSET_RADIO = 1.8f;

	/**
	 * @param context
	 */
	public SPListView(Context context) {
		super(context);
		initWithContext(context);
	}

	public SPListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
	}

	public SPListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWithContext(context);
	}

	private void initWithContext(Context context) {
		mScroller = new Scroller(context, new DecelerateInterpolator());
		// ListView need the scroll event, and it will dispatch the event to
		// user's listener (as a proxy).
		super.setOnScrollListener(this);

		// init header view
		mHeaderView = new RListViewHeader(context);
		mHeaderViewContent = (RelativeLayout) mHeaderView.findViewById(R.id.header_content);
		mSubHeaderTextView = (TextView) mHeaderView.findViewById(R.id.subHeaderText);
		addHeaderView(mHeaderView);

		// init footer view
		mLvFooterFrame = new FrameLayout(getContext());

		// init header height
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				mHeaderViewHeight = mHeaderViewContent.getHeight();
				getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});

		mDefHeaderH = (int) context.getResources().getDimension(R.dimen.default_list_pHeaderH);
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		// Add the Footer View at the last possible moment
		if (null != mLvFooterFrame && !mIsAddFooter) {
			addFooterView(mLvFooterFrame, null, false);
			mIsAddFooter = true;
		}

		super.setAdapter(adapter);
	}

	/**
	 * 启用或禁用拉刷新功能。
	 * @param enable
	 */
	public void setPullRefreshEnable(boolean enable) {
		mEnablePullRefresh = enable;
		if (!mEnablePullRefresh) { // disable, hide the content
			mHeaderViewContent.setVisibility(View.INVISIBLE);
			mEnableDropdownRebound = false;
		} else {
			mEnableDropdownRebound = true;
			mHeaderViewContent.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * stop refresh, reset header view.
	 */
	public void completeRefresh() {
		if (!mPullRefreshing) {
			return;
		}

		mPullRefreshing = false;
		resetHeaderHeight();
	}

	/**
	 * @param time
	 */
	public void setSubHeaderText(String subHeadText) {
		if (null == subHeadText || subHeadText.length() == 0) {
			mSubHeaderTextView.setVisibility(View.GONE);
		} else {
			mSubHeaderTextView.setVisibility(View.VISIBLE);
		}

		mSubHeaderTextView.setText(subHeadText);
	}

	private void invokeOnScrolling() {
		if (mScrollListener instanceof OnPScrollListener) {
			OnPScrollListener l = (OnPScrollListener) mScrollListener;
			l.onPScrolling(this);
		}
	}

	/**
	 * 第一次强制刷新，显示下拉进度
	 */
	public void firstForceRefresh() {
		if (!mEnablePullRefresh) {
			return;
		}

		if (mPullRefreshing) {
			return;
		}

		if (mListViewListener == null) {
			return;
		}

		mHeaderView.setVisiableHeight(mDefHeaderH);

		// scroll to top each time
		setSelection(0);

		// invoke refresh
		mPullRefreshing = true;
		mHeaderView.setState(RListViewHeader.STATE_REFRESHING);
		mListViewListener.onRefresh();

		resetHeaderHeight();
	}

	private void updateHeaderHeight(float delta) {
		if (!mEnableDropdownRebound) {
			return;
		}

		// Log.e("Test", "delta " + delta + " getVisiableHeight " +
		// mHeaderView.getVisiableHeight());
		mHeaderView.setVisiableHeight((int) delta + mHeaderView.getVisiableHeight());

		// 未处于刷新状态，更新箭头
		if (mEnablePullRefresh && !mPullRefreshing) {
			if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
				mHeaderView.setState(RListViewHeader.STATE_READY);
			} else {
				mHeaderView.setState(RListViewHeader.STATE_NORMAL);
			}
		}

		// scroll to top each time
		setSelection(0);
	}

	/**
	 * reset header view's height.
	 */
	private void resetHeaderHeight() {
		int height = mHeaderView.getVisiableHeight();

		// not visible.
		if (height == 0) {
			return;
		}

		// 如果正在刷新，Header View 并没有显示完全，则什么也不做
		if (mPullRefreshing && height <= mHeaderViewHeight) {
			return;
		}

		int finalHeight = 0; // default: scroll back to dismiss header.

		// 如果正在刷新,只是回弹到Header view
		if (mPullRefreshing && height > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);

		// trigger computeScroll
		invalidate();
	}

	/**
	 * 设置页脚
	 * @param footerView
	 */
	public void setFooterView(View footerView) {
		mLvFooterFrame.removeAllViews();
		mFooterView = footerView;

		if (null == footerView) {
			return;
		}

		final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
		mLvFooterFrame.addView(mFooterView, lp);
		mFooterView.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置页脚是否显示
	 * @param visible 显示
	 */
	public void visibilityFooterView(boolean visible) {
		if (null == mFooterView) {
			return;
		}

		if (visible) {
			mFooterView.setVisibility(View.VISIBLE);
		} else {
			mFooterView.setVisibility(View.GONE);
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
				if (getFirstVisiblePosition() == 0 && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
					// the first item is showing, header has shown or pull down.
					updateHeaderHeight(deltaY / OFFSET_RADIO);
					invokeOnScrolling();
				}
				break;

			default:
				mLastY = -1; // reset
				if (getFirstVisiblePosition() == 0) {
					// invoke refresh
					if (mEnablePullRefresh && mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
						mPullRefreshing = true;
						mHeaderView.setState(RListViewHeader.STATE_REFRESHING);
						if (mListViewListener != null) {
							mListViewListener.onRefresh();
						}
					}
					resetHeaderHeight();
				}
				break;
		}

		return super.onTouchEvent(ev);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				mHeaderView.setVisiableHeight(mScroller.getCurrY());
			}
			postInvalidate();
			invokeOnScrolling();
		}
		super.computeScroll();
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
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
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}

	public void setPListViewListener(IPListViewListener l) {
		mListViewListener = l;
	}

}
