/**
 * @(#)PcKenBurnsView.java   2014-8-29
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.ui.flaviofaria.kenburnsview;

import java.util.Random;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.privatecustom.publiclibs.R;

/**
  * 
  * @author chenj
  * @date 2014-8-29
  */

public class PcKenBurnsMultiImageView extends FrameLayout {

	private int[] mResourceIds;
	private final Handler mHandler;
	private ImageView[] mImageViews;

	private int mActiveImageIndex = -1;

	private final Random random = new Random();
	private int mSwapMs = 10000;
	private int mFadeInOutMs = 400;

	private float minScaleFactor = 1.2F;
	private float maxScaleFactor = 1.5F;

	private Runnable mSwapImageRunnable = new Runnable() {

		@Override
		public void run() {
			swapImage();
			mHandler.postDelayed(mSwapImageRunnable, mSwapMs - mFadeInOutMs * 2);
		}
	};

	public PcKenBurnsMultiImageView(Context context) {
		this(context, null);
	}

	public PcKenBurnsMultiImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PcKenBurnsMultiImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mHandler = new Handler();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		View view = inflate(getContext(), R.layout.kenburns_double_img_layoug, this);

		mImageViews = new ImageView[2];
		mImageViews[0] = (ImageView) view.findViewById(R.id.image0);
		mImageViews[1] = (ImageView) view.findViewById(R.id.image1);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		startKenBurnsAnimation();
	}

	/**
	 * 设置图片资源
	 *
	 * @param resourceIds
	 */
	public void setResourceIds(int... resourceIds) {
		mResourceIds = resourceIds;

		fillImageViews();
	}

	/**
	 * 填充图片资源
	 */
	private void fillImageViews() {
		// mImageViews.length = 2
		for (int i = 0; i < mImageViews.length; i++) {
			mImageViews[i].setImageResource(mResourceIds[i]);
		}
	}

	/**
	 * swap image
	 */
	private void swapImage() {
		if (mActiveImageIndex == -1) {
			mActiveImageIndex = 1;
			animate(mImageViews[mActiveImageIndex]);
			return;
		}

		int inactiveIndex = mActiveImageIndex;
		mActiveImageIndex = (1 + mActiveImageIndex) % mImageViews.length;

		final ImageView activeImageView = mImageViews[mActiveImageIndex];
		activeImageView.setAlpha(0.0f);
		ImageView inactiveImageView = mImageViews[inactiveIndex];

		animate(activeImageView);

		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.setDuration(mFadeInOutMs);
		animatorSet.playTogether(ObjectAnimator.ofFloat(inactiveImageView, "alpha", 1.0f, 0.0f), ObjectAnimator.ofFloat(activeImageView, "alpha", 0.0f, 1.0f));
		animatorSet.start();
	}

	/**
	 * 开始启动动画
	 */
	private void startKenBurnsAnimation() {
		mHandler.post(mSwapImageRunnable);
	}

	public void animate(View view) {
		float fromScale = pickScale();
		float toScale = pickScale();
		float fromTranslationX = pickTranslation(view.getWidth(), fromScale);
		float fromTranslationY = pickTranslation(view.getHeight(), fromScale);
		float toTranslationX = pickTranslation(view.getWidth(), toScale);
		float toTranslationY = pickTranslation(view.getHeight(), toScale);

		start(view, this.mSwapMs, fromScale, toScale, fromTranslationX, fromTranslationY, toTranslationX, toTranslationY);
	}

	private void start(View view, long duration, float fromScale, float toScale, float fromTranslationX, float fromTranslationY, float toTranslationX, float toTranslationY) {
		view.setScaleX(fromScale);
		view.setScaleY(fromScale);
		view.setTranslationX(fromTranslationX);
		view.setTranslationY(fromTranslationY);
		ViewPropertyAnimator propertyAnimator = view.animate().translationX(toTranslationX).translationY(toTranslationY).scaleX(toScale).scaleY(toScale).setDuration(duration);
		propertyAnimator.start();
	}

	private float pickScale() {
		return this.minScaleFactor + this.random.nextFloat() * (this.maxScaleFactor - this.minScaleFactor);
	}

	private float pickTranslation(int value, float ratio) {
		return value * (ratio - 1.0f) * (this.random.nextFloat() - 0.5f);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mHandler.removeCallbacks(mSwapImageRunnable);
	}

}
