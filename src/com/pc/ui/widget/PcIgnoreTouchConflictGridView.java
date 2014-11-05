/**
 * @(#)PcIgnoreTouchConflictGridView.java   2014-8-8
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.GridView;
import android.widget.ScrollView;

/**
  * 忽略手势冲突 <br>
  * 需调用#setParentScrollView()
  * 
  * @author chenj
  * @date 2014-8-8
  */

public class PcIgnoreTouchConflictGridView extends GridView implements OnTouchListener {

	private ScrollView mParentScrollView;

	public PcIgnoreTouchConflictGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public PcIgnoreTouchConflictGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PcIgnoreTouchConflictGridView(Context context) {
		super(context);
	}

	public void setParentScrollView(ScrollView scrollView) {
		mParentScrollView = scrollView;
		setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (mParentScrollView == null) return false;
		if (event.getAction() == MotionEvent.ACTION_UP)
			mParentScrollView.requestDisallowInterceptTouchEvent(false);
		else
			mParentScrollView.requestDisallowInterceptTouchEvent(true);
		return false;
	}

}
