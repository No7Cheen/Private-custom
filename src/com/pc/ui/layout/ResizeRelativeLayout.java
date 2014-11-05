/**
 * @(#)ResizeRelativeLayout.java 2013-11-15 Copyright 2013 it.kedacom.com, Inc.
 *                               All rights reserved.
 */

package com.pc.ui.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * @author chenjian
 * @date 2013-11-15
 */

public class ResizeRelativeLayout extends RelativeLayout {

	private Scroller mScroller;
	private final int DURATION = 150;

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public ResizeRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mScroller = new Scroller(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public ResizeRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		mScroller = new Scroller(context);
	}

	/**
	 * 向上滚动 (至顶部)
	 *
	 * @param offsetY
	 */
	public void anim2Top(int offsetY) {
		if (0 == offsetY) return;

		mScroller.startScroll(0, 0, 0, offsetY, DURATION);
	}

	/**
	 * 向下滚动(至底部)
	 *
	 * @param offsetY
	 */
	public void anim2Bottom(int offsetY) {
		if (0 == offsetY) return;

		mScroller.startScroll(0, offsetY, 0, -offsetY, DURATION);
	}

	/**
	 * @see android.view.View#computeScroll()
	 */

	@Override
	public void computeScroll() {
		super.computeScroll();

		if (!mScroller.isFinished() && mScroller.computeScrollOffset()) {
			scrollTo(0, mScroller.getCurrY());
			invalidate();
		}
	}

	/**
	 * @param context
	 */
	public ResizeRelativeLayout(Context context) {
		super(context);
	}

	private IOnResizeListener mListener;

	public void setOnResizeListener(IOnResizeListener l) {
		mListener = l;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (mListener != null) {
			mListener.OnResize(w, h, oldw, oldh);
		}
	}

}
