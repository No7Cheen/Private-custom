/**
 * @(#)SingleListView.java 2013-10-12 Copyright 2013 it.kedacom.com, Inc. All
 *                         rights reserved.
 */

package com.pc.ui.listview.x.expandable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;

/**
 * Single ExpandableListView
 * 
 * @author chenjian
 * @date 2013-10-12
 */

public class SingleExpandableListView extends ExpandableListView
{

	private View mHeaderView;
	private View mFooterView;

	private boolean mAddedLvFooter;

	private FrameLayout mLvHeaderFrame;
	private FrameLayout mLvFooterFrame;

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SingleExpandableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public SingleExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * @param context
	 */
	public SingleExpandableListView(Context context) {
		super(context);

		init();
	}

	private void init() {
		mLvHeaderFrame = new FrameLayout(getContext());
		mLvFooterFrame = new FrameLayout(getContext());
		addHeaderView(mLvHeaderFrame, null, false);
	}

	@Override
	public void setAdapter(ExpandableListAdapter adapter) {
		// Add the Footer View at the last possible moment
		if (null != mLvFooterFrame && !mAddedLvFooter) {
			addFooterView(mLvFooterFrame, null, false);
			mAddedLvFooter = true;
		}

		super.setAdapter(adapter);
	}

	/**
	 * 设置页眉
	 * 
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
	 * 
	 * @param visible
	 *        显示
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
	 * 
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
	 * 
	 * @param visible
	 *        显示
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

}
