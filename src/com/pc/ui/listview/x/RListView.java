package com.pc.ui.listview.x;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
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
 * @date 2013-10-12
 */
public class RListView extends ListView implements OnScrollListener {

	// save event y
	private float mLastY = -1;
	// used for scroll back
	private Scroller mScroller;
	// user's scroll listener
	private OnScrollListener mScrollListener;

	// 下拉刷新/上拉加载接口
	private IRListViewListener mListViewListener;

	// 页眉
	private RListViewHeader mHeaderView;
	// header view content, use it to calculate the Header's height. And hide it
	// when disable pull refresh.
	private RelativeLayout mHeaderViewContent;
	private TextView mSubHeaderTextView;
	private int mHeaderViewHeight; // header view's height
	private boolean mEnablePullRefresh = true;//
	private boolean mPullRefreshing = false; // is refreashing.
	private boolean mEnableDropdownRebound = true;

	// 页脚
	private RListViewFooter mFooterView;
	private boolean mEnablePullLoad;
	private boolean mPullLoading;
	private boolean mIsAddFooter;
	// 下拉回弹效果
	private boolean mEnablePullOnRebound = true;

	// total list items, used to detect is at the bottom of listview.
	private int mTotalItemCount;

	// for mScroller, scroll back from header or footer.
	private int mScrollBack;
	private final int SCROLLBACK_HEADER = 0;
	private final int SCROLLBACK_FOOTER = 1;

	// scroll back duration
	private final int SCROLL_DURATION = 400;
	// when pull up >= 50px at bottom, trigger load more.
	private final int PULL_LOAD_MORE_DELTA = 50;
	// support iOS like pull feature.
	private final float OFFSET_RADIO = 1.8f;

	/**
	 * @param context
	 */
	public RListView(Context context) {
		super(context);
		initWithContext(context);
	}

	public RListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
	}

	public RListView(Context context, AttributeSet attrs, int defStyle) {
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
		mFooterView = new RListViewFooter(context);

		// init header height
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				mHeaderViewHeight = mHeaderViewContent.getHeight();
				getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		if (!mIsAddFooter) {
			mIsAddFooter = true;
			addFooterView(mFooterView, null, false);
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
		} else {
			mEnableDropdownRebound = true;
			mHeaderViewContent.setVisibility(View.VISIBLE);
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
			mFooterView.hide();
			mFooterView.setOnClickListener(null);
		} else {
			mPullLoading = false;
			mEnablePullOnRebound = true;
			mFooterView.show();
			mFooterView.setState(RListViewFooter.STATE_NORMAL);
			// both "pull up" and "click" will invoke load more.
			mFooterView.setOnClickListener(new OnClickListener() {

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

	/**
	 * stop load more, reset footer view.
	 */
	public void stopLoadMore() {
		if (!mPullLoading) {
			return;
		}

		mPullLoading = false;
		mFooterView.setState(RListViewFooter.STATE_NORMAL);
	}

	/**
	 * @param time
	 */
	public void setSubHeaderText(String subHeadText) {
		mSubHeaderTextView.setText(subHeadText);
	}

	private void invokeOnScrolling() {
		if (mScrollListener instanceof OnRScrollListener) {
			OnRScrollListener l = (OnRScrollListener) mScrollListener;
			l.onRScrolling(this);
		}
	}

	private void updateHeaderHeight(float delta) {
		if (!mEnableDropdownRebound) {
			return;
		}

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

	private void updateFooterHeight(float delta) {
		if (!mEnablePullOnRebound) {
			return;
		}

		int height = mFooterView.getBottomMargin() + (int) delta;
		if (mEnablePullLoad && !mPullLoading) {
			// height enough to invoke load more.
			if (height > PULL_LOAD_MORE_DELTA) {
				mFooterView.setState(RListViewFooter.STATE_READY);
			} else {
				mFooterView.setState(RListViewFooter.STATE_NORMAL);
			}
		}
		mFooterView.setBottomMargin(height);

		// setSelection(mTotalItemCount - 1); // scroll to bottom
	}

	private void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLLBACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin, SCROLL_DURATION);

			invalidate();
		}
	}

	private void startLoadMore() {
		mPullLoading = true;
		mFooterView.setState(RListViewFooter.STATE_LOADING);
		if (mListViewListener != null) {
			mListViewListener.onLoadMore();
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
				} else if (getLastVisiblePosition() == mTotalItemCount - 1
						&& (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
					// last item, already pulled up or want to pull up.
					updateFooterHeight(-deltaY / OFFSET_RADIO);
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
				} else if (getLastVisiblePosition() == mTotalItemCount - 1) {
					// invoke load more.
					if (mEnablePullLoad && mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
						startLoadMore();
					}
					resetFooterHeight();
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
			} else {
				mFooterView.setBottomMargin(mScroller.getCurrY());
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
		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}

	public void setRListViewListener(IRListViewListener l) {
		mListViewListener = l;
	}

}
