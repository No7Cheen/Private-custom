package com.pc.ui.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class ResizeLinearLayout extends LinearLayout {

	private Scroller mScroller;
	private final int DURATION = 150;

	private IOnResizeListener mListener;

	public void setOnResizeListener(IOnResizeListener l) {
		mListener = l;
	}

	public ResizeLinearLayout(Context context, AttributeSet attrs) {
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

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (mListener != null) {
			mListener.OnResize(w, h, oldw, oldh);
		}
	}

}
