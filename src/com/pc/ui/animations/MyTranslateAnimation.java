/**
 * @(#)MyAnimationSet.java 2013-8-6 Copyright 2013 it.kedacom.com, Inc. All
 *                         rights reserved.
 */

package com.pc.ui.animations;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;

/**
 * @author chenjian
 * @date 2013-8-6
 */

public class MyTranslateAnimation extends TranslateAnimation {

	/**
	 * @param context
	 * @param attrs
	 */
	public MyTranslateAnimation(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param fromXDelta
	 * @param toXDelta
	 * @param fromYDelta
	 * @param toYDelta
	 */
	public MyTranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta) {
		super(fromXDelta, toXDelta, fromYDelta, toYDelta);
	}

	/**
	 * @param fromXType
	 * @param fromXValue
	 * @param toXType
	 * @param toXValue
	 * @param fromYType
	 * @param fromYValue
	 * @param toYType
	 * @param toYValue
	 */
	public MyTranslateAnimation(int fromXType, float fromXValue, int toXType, float toXValue, int fromYType,
			float fromYValue, int toYType, float toYValue) {
		super(fromXType, fromXValue, toXType, toXValue, fromYType, fromYValue, toYType, toYValue);
	}

	/**
	 * @see android.view.animation.Animation#applyTransformation(float,
	 *      android.view.animation.Transformation)
	 */
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		// interpolatedTime:动画进度值，范围为0～1，0.5为正好翻转一半
		if (mListener != null) {
			mListener.interpolatedTime(interpolatedTime);
		}

		super.applyTransformation(interpolatedTime, t);
	}

	/** 用于监听动画进度，当值过半时需更新的内 */
	private InterpolatedTimeListener mListener;

	/**
	 * @Description
	 * @param listener
	 */
	public void setInterpolatedTimeListener(InterpolatedTimeListener listener) {
		this.mListener = listener;
	}

}
