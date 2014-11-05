/**
 * @(#)RotationHelper.java 2013-8-8 Copyright 2013 it.kedacom.com, Inc. All
 *                         rights reserved.
 */

package com.pc.ui.animations.apis;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

/**
 * @author chenjian
 * @date 2013-8-8
 */

public class RotationViesHelper {

	// 逆时针旋转
	private final boolean mRotateCounterClockwise;
	private final int mDuration = 500;
	private SwapView mSwapView;

	public RotationViesHelper(boolean _RotateCounterClockwise) {
		mRotateCounterClockwise = _RotateCounterClockwise;
	}

	/**
	 * Setup a new 3D rotation on the container view.
	 * @param position the item that was clicked to show a picture, or -1 to
	 *            show the list
	 * @param start the start angle at which the rotation must begin
	 * @param end the end angle of the rotation
	 */
	public void applyRotation(View container, float start, float end, SwapView swapView) {
		// Find the center of the container
		final float centerX = container.getWidth() / 2.0f;
		final float centerY = container.getHeight() / 2.0f;

		// Create a new 3D rotation with the supplied parameter
		// The animation listener is used to trigger the next animation
		final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true);
		rotation.setDuration(mDuration);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new DisplayNextView(container));

		mSwapView = swapView;

		container.startAnimation(rotation);
	}

	private class DisplayNextView implements Animation.AnimationListener {

		private final View mContainer;

		private DisplayNextView(View _Container) {
			mContainer = _Container;
		}

		public void onAnimationStart(Animation animation) {
		}

		// 动画结束时，打开View
		public void onAnimationEnd(Animation animation) {
			mContainer.post(new SwapViews(mContainer));
		}

		public void onAnimationRepeat(Animation animation) {
		}

	}

	/**
	 * This class is responsible for swapping the views and start the second
	 * half of the animation.
	 */
	private final class SwapViews implements Runnable {

		private final View mContainer;

		public SwapViews(View _Container) {
			mContainer = _Container;
		}

		public void run() {
			final float centerX = mContainer.getWidth() / 2.0f;
			final float centerY = mContainer.getHeight() / 2.0f;
			Rotate3dAnimation rotation;

			if (mSwapView != null) {
				mSwapView.swapView();
			}

			// 逆时针
			if (mRotateCounterClockwise) {
				rotation = new Rotate3dAnimation(90, 180, centerX, centerY, 310.0f, false);
			}

			// 顺时针
			else {
				rotation = new Rotate3dAnimation(90, 0, centerX, centerY, 310.0f, false);
			}

			rotation.setDuration(mDuration);
			rotation.setFillAfter(true);
			rotation.setInterpolator(new DecelerateInterpolator());

			mContainer.startAnimation(rotation);
		}
	}

	public interface SwapView {

		/**
		 * swap view
		 */
		public void swapView();
	}

}
