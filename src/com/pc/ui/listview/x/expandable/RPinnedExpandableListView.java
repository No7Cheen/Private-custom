package com.pc.ui.listview.x.expandable;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.pc.ui.listview.x.libraries.PinnedSectionedExpandableAdapter;
import com.pc.ui.listview.x.libraries.RListViewHeader;
import com.privatecustom.publiclibs.R;

/**
 * Pull To Refresh Pinned Header ExpandableListView
 * @author chenjian
 * @date 2013-10-12
 */
public class RPinnedExpandableListView extends AbsRExpandableListView {

	// //////////////////////////////////////////////////////////////////////////////////////
	// Pinned Header view
	// //////////////////////////////////////////////////////////////////////////////////////
	private View mCurrentPinnedView;
	private float mPinnedViewOffset;
	private int mWidthMode;
	private int mHeightMode;
	private int mCurrentSection = 0;
	private int mDefHeaderH;

	/**
	 * 悬浮框对应的组位置
	 */
	private int mGroupLocation;

	private PinnedSectionedExpandableAdapter mAdapter;

	/**
	 * @param context
	 */
	public RPinnedExpandableListView(Context context) {
		super(context);
		initHeaderHeight();
	}

	public RPinnedExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderHeight();
	}

	public RPinnedExpandableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initHeaderHeight();
	}

	private void initHeaderHeight() {
		mDefHeaderH = (int) getResources().getDimension(R.dimen.default_list_pHeaderH);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		mWidthMode = MeasureSpec.getMode(widthMeasureSpec);
		mHeightMode = MeasureSpec.getMode(heightMeasureSpec);
	}

	@Override
	public void setAdapter(ExpandableListAdapter adapter) {
		if (null == adapter) {
			throw new IllegalArgumentException("adapter is null");
		}

		if (!(adapter instanceof PinnedSectionedExpandableAdapter))
			throw new IllegalArgumentException("Does your adapter implement PinnedSectionedExpandableAdapter?");

		mCurrentPinnedView = null;
		mAdapter = (PinnedSectionedExpandableAdapter) adapter;

		super.setAdapter(adapter);
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		super.onScrollStateChanged(view, scrollState);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);

		if (mAdapter == null || mAdapter.getCount() == 0 || (firstVisibleItem < getHeaderViewsCount())) {
			mCurrentPinnedView = null;
			mPinnedViewOffset = 0.0f;
			for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
				View header = getChildAt(i);
				if (header != null) {
					header.setVisibility(VISIBLE);
				}
			}

			return;
		}

		// firstVisibleItem -= mRefreshableView.getHeaderViewsCount();

		final long flatPos = getExpandableListPosition(firstVisibleItem);
		// 当前第一行归属的groupPosition
		int groupPosition = ExpandableListView.getPackedPositionGroup(flatPos);

		int section = getSectionForPosition(firstVisibleItem);
		mCurrentPinnedView = getSectionHeaderView(section, groupPosition, mCurrentPinnedView);
		ensurePinnedHeaderLayout(mCurrentPinnedView);

		mGroupLocation = section;

		mPinnedViewOffset = 0.0f;

		if (groupPosition < 0) {
			mPinnedViewOffset = -mCurrentPinnedView.getMeasuredHeight();
		} else {
			// 如果组没有展开，浮动组隐藏
			if (!isGroupExpanded(groupPosition)) {
				mPinnedViewOffset = -mCurrentPinnedView.getMeasuredHeight();
			} else {
				for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
					final long pos = getExpandableListPosition(i);
					if (ExpandableListView.getPackedPositionType(pos) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
						View header = getChildAt(i - firstVisibleItem);
						if (header == null) {
							continue;
						}
						float headerTop = header.getTop();
						float pinnedHeaderHeight = mCurrentPinnedView.getMeasuredHeight();
						header.setVisibility(VISIBLE);
						if (pinnedHeaderHeight >= headerTop && headerTop > 0) {
							mPinnedViewOffset = headerTop - header.getHeight();
						}
					}
				}
			}
		}

		invalidate();
	}

	private float mDownX;
	private float mDownY;
	private long mStartTime;
	private TimerTask mTimerTask;
	private Timer mTimer = new Timer();

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mCurrentPinnedView == null || mPinnedViewOffset == -mCurrentPinnedView.getMeasuredHeight()) {
			return super.onTouchEvent(ev);
		}

		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mDownX = ev.getX();
				mDownY = ev.getY();
				mStartTime = System.currentTimeMillis();

				int dw = mCurrentPinnedView.getMeasuredWidth();
				int dh = mCurrentPinnedView.getMeasuredHeight();

				if (mDownX <= dw && mDownY <= dh) {
					mTimerTask = new TimerTask() {

						@Override
						public void run() {
							headerViewClick();
						}
					};

					mTimer.schedule(mTimerTask, 1000);

					return true;
				}
				break;

			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				float x = ev.getX();
				float y = ev.getY();
				float offsetX = Math.abs(x - mDownX);
				float offsetY = Math.abs(y - mDownY);
				int w = mCurrentPinnedView.getMeasuredWidth();
				int h = mCurrentPinnedView.getMeasuredHeight();
				if (x <= w && y <= h && offsetX <= w && offsetY <= h) {
					if (System.currentTimeMillis() - mStartTime < 1000) {
						if (mTimerTask != null) {
							mTimerTask.cancel();
						}

						headerViewClick();
					}

					return true;
				}
				break;
		}

		return super.onTouchEvent(ev);
	}

	/**
	 * Pinned Header View 点击事件
	 */
	private void headerViewClick() {
		post(new Runnable() {

			@Override
			public void run() {
				if (mGroupLocation < 0) {
					return;
				}
				int groupPosition = mGroupLocation;
				if (!isGroupExpanded(groupPosition)) {
					expandGroup(groupPosition);
				} else {
					collapseGroup(groupPosition);
				}
			}
		});
	}

	/**
	 * 这里的Section,即Group Position
	 * @param position
	 * @return
	 */
	private int getSectionForPosition(int position) {
		if (mAdapter == null) {
			return ExpandableListView.getPackedPositionGroup(getExpandableListPosition(position));
		}

		return mAdapter.getSectionForPosition(this, position);
	}

	/**
	 * 获取指定位置 Header View
	 * @param section
	 * @param oldView
	 * @return
	 */
	private View getSectionHeaderView(int section, final int groupPosition, View oldView) {
		if (null == mAdapter) {
			return oldView;
		}

		boolean shouldLayout = (section != mCurrentSection || oldView == null);

		View view = mAdapter.getSectionHeaderView(section, isGroupExpanded(groupPosition), oldView, this);
		if (shouldLayout) {
			// ensurePinnedHeaderLayout(view);
			mCurrentSection = section;
		}

		return view;
	}

	private void ensurePinnedHeaderLayout(View header) {
		if (header == null || !header.isLayoutRequested()) {
			return;
		}

		int widthSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), mWidthMode);

		int heightSpec;
		ViewGroup.LayoutParams layoutParams = header.getLayoutParams();
		if (layoutParams != null && layoutParams.height > 0) {
			heightSpec = MeasureSpec.makeMeasureSpec(layoutParams.height, MeasureSpec.EXACTLY);
		} else {
			heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		header.measure(widthSpec, heightSpec);
		header.layout(0, 0, header.getMeasuredWidth(), header.getMeasuredHeight());
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (mAdapter == null || mCurrentPinnedView == null) {
			return;
		}

		int saveCount = canvas.save();
		canvas.translate(0, mPinnedViewOffset);
		canvas.clipRect(0, 0, getWidth(), mCurrentPinnedView.getMeasuredHeight()); // needed
		mCurrentPinnedView.draw(canvas);
		canvas.restoreToCount(saveCount);
	}

	@Override
	public void startRefresh() {
		mPullRefreshing = true;
		mRHeaderView.setState(RListViewHeader.STATE_REFRESHING);
		mRHeaderView.setVisiableHeight(mDefHeaderH);
		if (mListViewListener != null) {
			mListViewListener.onRefresh();
		}
		resetHeaderHeight();
	}

}
