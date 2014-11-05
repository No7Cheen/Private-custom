/**
 * @(#)SingleListView.java 2013-10-12 Copyright 2013 it.kedacom.com, Inc. All
 *                         rights reserved.
 */

package com.pc.ui.listview.x;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.pc.ui.listener.IOnResizeListener;

/**
 * Single ListView
 * @author chenjian
 * @date 2013-10-12
 */

public class SingleListView extends ListView {

	private View mHeaderView;
	private View mFooterView;

	private boolean mAddedLvFooter;

	private IOnResizeListener mResizeListener;

	private FrameLayout mLvHeaderFrame;
	private FrameLayout mLvFooterFrame;
	private boolean heightMost;
	private final String NAME_SPACE = "com.pc.ui.listview.x.SingleListView";

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SingleListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		String hm = attrs.getAttributeValue(NAME_SPACE, "heightMost");
		heightMost = Boolean.valueOf(hm);
		init();
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public SingleListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		String hm = attrs.getAttributeValue(NAME_SPACE, "heightMost");
		heightMost = Boolean.valueOf(hm);
		init();
	}

	/**
	 * @param context
	 */
	public SingleListView(Context context) {
		super(context);
		init();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (heightMost) {
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	private void init() {
		mLvHeaderFrame = new FrameLayout(getContext());
		mLvFooterFrame = new FrameLayout(getContext());
		addHeaderView(mLvHeaderFrame, null, false);
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
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

		final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
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

		final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
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

	/**
	 * 隐藏页脚
	 */
	public void hideFooterView() {
		if (null == mFooterView || mFooterView.getVisibility() == View.GONE) {
			return;
		}

		mFooterView.setVisibility(View.GONE);
	}

	/**
	 * 显示页脚
	 */
	public void visibleFooterView() {
		if (null == mFooterView) {
			return;
		}

		mFooterView.setVisibility(View.VISIBLE);
	}

	/**
	 * 隐藏页眉
	 */
	public void hideHeaderView() {
		if (null == mHeaderView) {
			return;
		}

		mHeaderView.setVisibility(View.GONE);
	}

	/**
	 * 显示页眉
	 */
	public void visibleHeaderView() {
		if (null == mHeaderView) {
			return;
		}

		mHeaderView.setVisibility(View.VISIBLE);
	}

	public void setOnResizeListener(IOnResizeListener l) {
		mResizeListener = l;
	}

	/**
	 * 检测布局的高度变化
	 * @see android.view.View#onSizeChanged(int, int, int, int)
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		if (mResizeListener != null) {
			mResizeListener.OnResize(w, h, oldw, oldh);
		}
	}

}
