/**
 * @(#)MyAnimationSet.java 2013-10-9 Copyright 2013 it.kedacom.com, Inc. All
 *                         rights reserved.
 */

package com.pc.ui.animations;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;

/**
 * @author chenjian
 * @date 2013-10-9
 */

public class MyAnimationSet extends AnimationSet {

	/** 用于监听动画进度，当值过半时需更新的内 */
	private InterpolatedTimeListener mListener;

	/**
	 * @param shareInterpolator
	 */

	public MyAnimationSet(boolean shareInterpolator) {
		super(shareInterpolator);
	}

	/**
	 * @param context
	 * @param attrs
	 */

	public MyAnimationSet(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

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
	 * @Description
	 * @param listener
	 */
	public void setInterpolatedTimeListener(InterpolatedTimeListener listener) {
		this.mListener = listener;
	}

}
