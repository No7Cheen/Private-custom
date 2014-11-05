/**
 * @(#)SingleListView.java 2013-10-12 Copyright 2013 it.kedacom.com, Inc. All
 *                         rights reserved.
 */

package com.pc.ui.listview.x.expandable;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;

import com.pc.ui.listview.x.libraries.PinnedSectionedExpandableAdapter;

/**
 * Single ExpandableListView
 * @author chenjian
 * @date 2013-10-12
 */

public class SinglePinnedExpandableListView extends ExpandableListView implements OnScrollListener {

	// Header View, Footer View
	private View mHeaderView;
	private View mFooterView;

	private boolean mAddedLvFooter;

	private FrameLayout mLvHeaderFrame;
	private FrameLayout mLvFooterFrame;

	private View mCurrentPinnedView;
	private float mPinnedViewOffset;
	private int mWidthMode;
	private int mHeightMode;
	private int mCurrentSection = 0;

	private OnScrollListener mDelegateOnScrollListener;

	private PinnedSectionedExpandableAdapter mAdapter;

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SinglePinnedExpandableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public SinglePinnedExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	/**
	 * @param context
	 */
	public SinglePinnedExpandableListView(Context context) {
		super(context);

		init();
	}

	private void init() {
		mLvHeaderFrame = new FrameLayout(getContext());
		mLvFooterFrame = new FrameLayout(getContext());
		addHeaderView(mLvHeaderFrame, null, false);

		super.setOnScrollListener(this);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		mWidthMode = MeasureSpec.getMode(widthMeasureSpec);
		mHeightMode = MeasureSpec.getMode(heightMeasureSpec);
	}

	public void setOnScrollListener(OnScrollListener l) {
		mDelegateOnScrollListener = l;
	}

	/**
	 * set Adapter
	 * @param adapter
	 */
	public void setAdapter(ExpandableListAdapter adapter) {
		if (null == adapter) {
			throw new IllegalArgumentException("adapter is null");
		}

		if (!(adapter instanceof PinnedSectionedExpandableAdapter))
			throw new IllegalArgumentException("Does your adapter implement PinnedSectionedExpandableAdapter?");

		mCurrentPinnedView = null;
		mAdapter = (PinnedSectionedExpandableAdapter) adapter;

		// Add the Footer View at the last possible moment
		if (null != mLvFooterFrame && !mAddedLvFooter) {
			addFooterView(mLvFooterFrame, null, false);
			mAddedLvFooter = true;
		}

		super.setAdapter(adapter);
	}

	/**
	 * 设置页眉
	 * @param headerView
	 */
	public void setHeaderView(View headerView) {
		mLvHeaderFrame.removeAllViews();
		mHeaderView = headerView;

		if (null == headerView) {
			return;
		}

		final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
		mLvHeaderFrame.addView(mHeaderView, lp);
		mHeaderView.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置页眉是否显示
	 * @param visible 显示
	 */
	public void setHeaderViewVisibility(boolean visible) {
		if (null == mHeaderView) {
			return;
		}

		if (visible) {
			mHeaderView.setVisibility(View.VISIBLE);
		} else {
			mHeaderView.setVisibility(View.GONE);
		}
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
	public void setFooterViewVisibility(boolean visible) {
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
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mDelegateOnScrollListener != null) {
			mDelegateOnScrollListener.onScrollStateChanged(view, scrollState);
		}

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (mDelegateOnScrollListener != null) {
			mDelegateOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}

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
}
