/**
 * @(#)BackActivityGestureListener.java 2013-7-5 Copyright 2013 it.kedacom.com,
 *                                      Inc. All rights reserved.
 */

package com.pc.ui.animations.gestures;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * @author chenjian
 * @date 2013-7-5
 */

public class BackActivityGestureListener extends GestureDetector.SimpleOnGestureListener {

	public static interface OnBackFinishedListener {

		public void onBackFinishListener();
	}

	private Activity mActivity;
	private OnBackFinishedListener mOnBackFinishedListener;

	public BackActivityGestureListener(Activity _Activity, OnBackFinishedListener _onBackFinishedListener) {
		mActivity = _Activity;
		mOnBackFinishedListener = _onBackFinishedListener;
	}

	@Override
	public boolean onDown(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				break;

			case MotionEvent.ACTION_MOVE:
				break;

			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				break;
			default:
				break;
		}
		return true;
	}

	@Override
	public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
		if (mActivity == null) {
			return false;
		}
		if (event1 == null || event2 == null) {
			return false;
		}

		// velocityX速度, 一般Math.abs(velocityX) > 200
		// e2.getX() - e1.getX()移动的距离，
		// translateX > 0 向右滑动
		// translateX < 0 向左滑动
		// translateY < 0 向上
		// translateY > 0 向下
		float translateX = event2.getX() - event1.getX();
		float translateY = event2.getY() - event1.getY();

		if (Math.abs(translateY) > 180) { // 上下滑动不可用

			return false;
		}

		if (translateX < 150) {// 向左

			return false;
		}

		if (translateX > 150) {// 向右

			if (mOnBackFinishedListener != null) {
				mOnBackFinishedListener.onBackFinishListener();
			}
			mActivity.finish();

			return true;
		}

		return false;
	}
}
