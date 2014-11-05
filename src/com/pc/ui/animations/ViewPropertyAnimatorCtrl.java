/**
 * @(#)ViewPropertyAnimatorCtrl.java   2014-10-20
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.ui.animations;

import android.view.View;

import com.nineoldandroids.view.ViewPropertyAnimator;
import com.pc.utils.android.sys.TerminalUtils;

/**
  * 
  * @author chenj
  * @date 2014-10-20
  */

public class ViewPropertyAnimatorCtrl {

	/**
	 * 透明渐变效果
	 *
	 * @param v
	 * @param value
	 */
	public void alpha4longAnimTime(View v) {
		alpha(v, 1, android.R.integer.config_longAnimTime);
	}

	/**
	 * 透明渐变效果
	 *
	 * @param v
	 * @param value
	 */
	public void alpha(View v) {
		alpha(v, 1, android.R.integer.config_mediumAnimTime);
	}

	/**
	 * 透明渐变效果
	 *
	 * @param v
	 * @param value
	 */
	public void alpha(View v, long duration) {
		alpha(v, 1, duration);
	}

	/**
	 * 完全透明 
	 *
	 * @param v
	 */
	public void alphaTransparent(View v) {
		alpha(v, 0, android.R.integer.config_mediumAnimTime);
	}

	/**
	 * 透明渐变效果
	 *
	 * @param v
	 * @param value
	 */
	public void alpha(View v, float value, long duration) {
		if (null == v) {
			return;
		}

		// 适用于3.0以下版本
		if (TerminalUtils.getSdkVersion() < android.os.Build.VERSION_CODES.HONEYCOMB) {
			ViewPropertyAnimator.animate(v).alpha(value).setDuration(duration);
		}
		// 适用于3.0以上版本
		else {
			v.animate().alpha(value).setDuration(duration);
		}
	}

	/**
	 * 透明渐变效果
	 *
	 * @param v
	 * @param value
	 */
	public void alpha(View v, float value, int durationIntegerId) {
		if (null == v) {
			return;
		}

		// 适用于3.0以下版本
		if (TerminalUtils.getSdkVersion() < android.os.Build.VERSION_CODES.HONEYCOMB) {
			ViewPropertyAnimator.animate(v).alpha(value).setDuration(v.getResources().getInteger(durationIntegerId));
		}
		// 适用于3.0以上版本
		else {
			v.animate().alpha(value).setDuration(v.getResources().getInteger(durationIntegerId));
		}
	}

	/**
	 * Y轴平移
	 *
	 * @param v
	 * @param value
	 * @param duration
	 */
	public void translationY(View v, float value, long duration) {
		if (null == v) {
			return;
		}

		// 适用于3.0以下版本
		if (TerminalUtils.getSdkVersion() < android.os.Build.VERSION_CODES.HONEYCOMB) {
			ViewPropertyAnimator.animate(v).translationY(value).setDuration(duration);
		}
		// 适用于3.0以上版本
		else {
			v.animate().translationY(value).setDuration(duration);
		}
	}

	/**
	 * Y轴平移
	 *
	 * @param v
	 * @param value
	 * @param duration
	 */
	public void translationY(View v, float value, int durationIntegerId) {
		if (null == v) {
			return;
		}

		// 适用于3.0以下版本
		if (TerminalUtils.getSdkVersion() < android.os.Build.VERSION_CODES.HONEYCOMB) {
			ViewPropertyAnimator.animate(v).translationY(value).setDuration(v.getResources().getInteger(durationIntegerId));
		}
		// 适用于3.0以上版本
		else {
			v.animate().translationY(value).setDuration(v.getResources().getInteger(durationIntegerId));
		}
	}

	/**
	 * Y轴平移
	 *
	 * @param v
	 * @param value
	 * @param alpha
	 * @param duration
	 */
	public void translationY(View v, float value, float alpha, long duration) {
		if (null == v) {
			return;
		}

		// 适用于3.0以下版本
		if (TerminalUtils.getSdkVersion() < android.os.Build.VERSION_CODES.HONEYCOMB) {
			ViewPropertyAnimator.animate(v).translationY(value).setDuration(duration);
			ViewPropertyAnimator.animate(v).alpha(value).setDuration(duration);
		}
		// 适用于3.0以上版本
		else {
			v.animate().translationY(value).setDuration(duration);
			v.animate().alpha(alpha).setDuration(duration);
		}
	}

	/**
	 * Y轴平移
	 *
	 * @param v
	 * @param value
	 * @param alpha
	 * @param duration
	 */
	public void translationY(View v, float value, float alpha, int durationIntegerId) {
		if (null == v) {
			return;
		}

		// 适用于3.0以下版本
		if (TerminalUtils.getSdkVersion() < android.os.Build.VERSION_CODES.HONEYCOMB) {
			ViewPropertyAnimator.animate(v).translationY(value).setDuration(v.getResources().getInteger(durationIntegerId));
			ViewPropertyAnimator.animate(v).alpha(value).setDuration(v.getResources().getInteger(durationIntegerId));
		}
		// 适用于3.0以上版本
		else {
			v.animate().translationY(value).setDuration(v.getResources().getInteger(durationIntegerId));
			v.animate().alpha(alpha).setDuration(v.getResources().getInteger(durationIntegerId));
		}
	}

}
