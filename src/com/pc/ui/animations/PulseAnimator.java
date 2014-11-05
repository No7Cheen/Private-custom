/**
 * @(#)PulseAnimator.java 2014-3-4 Copyright 2014 it.kedacom.com, Inc. All
 *                        rights reserved.
 */

package com.pc.ui.animations;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;

/**
 * 脉冲/脉搏 动画
 * @author chenjian
 * @date 2014-3-4
 */

public class PulseAnimator {

	public static ObjectAnimator getPulseAnimator(View labelToAnimate) {
		return getPulseAnimator(labelToAnimate, 0.9F, 1.05F);
	}

	public static ObjectAnimator getPulseAnimator(View labelToAnimate, float decreaseRatio, float increaseRatio) {
		Keyframe k0 = Keyframe.ofFloat(0f, 1f);
		Keyframe k1 = Keyframe.ofFloat(0.275f, decreaseRatio);
		Keyframe k2 = Keyframe.ofFloat(0.69f, increaseRatio);
		Keyframe k3 = Keyframe.ofFloat(1f, 1f);

		PropertyValuesHolder scaleX = PropertyValuesHolder.ofKeyframe("scaleX", k0, k1, k2, k3);
		PropertyValuesHolder scaleY = PropertyValuesHolder.ofKeyframe("scaleY", k0, k1, k2, k3);
		ObjectAnimator pulseAnimator = ObjectAnimator.ofPropertyValuesHolder(labelToAnimate, scaleX, scaleY);
		pulseAnimator.setDuration(544);

		return pulseAnimator;
	}

	public static void startPulsAnimator(View v, float decreaseRatio, float increaseRatio) {
		getPulseAnimator(v, decreaseRatio, increaseRatio).start();
	}

	public static void startPulsAnimator(View v) {
		getPulseAnimator(v, 0.9F, 1.05F).start();
	}

}
