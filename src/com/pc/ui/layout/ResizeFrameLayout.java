/**
 * @(#)ResizeFrameLayout.java   2014-9-29
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.ui.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
  * 
  * @author chenj
  * @date 2014-9-29
  */

public class ResizeFrameLayout extends FrameLayout {

	private IOnResizeListener mListener;

	public void setOnResizeListener(IOnResizeListener l) {
		mListener = l;
	}

	public ResizeFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (mListener != null) {
			mListener.OnResize(w, h, oldw, oldh);
		}
	}
}
