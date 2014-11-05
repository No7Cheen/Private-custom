/**
 * @(#)MyBaseAnimation.java 2013-10-9 Copyright 2013 it.kedacom.com, Inc. All
 *                          rights reserved.
 */

package com.pc.ui.animations;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * @author chenjian
 * @date 2013-10-9
 */

public class MyBaseAnimation<T extends Animation> extends Animation {

	/**
	 */
	public MyBaseAnimation() {
		super();
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public MyBaseAnimation(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 用于监听动画进度
	 */
	private InterpolatedTimeListener mListener;

	/**
	 * @Description
	 * @param listener
	 */
	public void setInterpolatedTimeListener(InterpolatedTimeListener listener) {
		this.mListener = listener;
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		// interpolatedTime:动画进度值，范围为0～1
		if (mListener != null) {
			mListener.interpolatedTime(interpolatedTime);
		}

		super.applyTransformation(interpolatedTime, t);
	}

}
