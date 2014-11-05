/**
 * @(#)MyAnimation.java 2013-10-9 Copyright 2013 it.kedacom.com, Inc. All rights
 *                      reserved.
 */

package com.pc.ui.animations;

import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * @author chenjian
 * @date 2013-10-9
 */

public abstract class MyAnimation extends Animation {

	/**
	 * @see android.view.animation.Animation#applyTransformation(float,
	 *      android.view.animation.Transformation)
	 */
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		// interpolatedTime:动画进度值，范围为0～1
		if (mListener != null) {
			mListener.interpolatedTime(interpolatedTime);
		}

		super.applyTransformation(interpolatedTime, t);
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

}
