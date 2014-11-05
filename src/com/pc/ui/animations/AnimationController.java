package com.pc.ui.animations;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class AnimationController {

	public final long DEF_DURATIONMILLIS = 2000;
	public final long DEF_DELAYMILLIS = 0;

	public final int RELA_2_SELF = Animation.RELATIVE_TO_SELF;
	public final int RELA_2_PARENT = Animation.RELATIVE_TO_PARENT;

	public final int Default = -1;
	public final int Linear = 0;
	public final int Accelerate = 1;
	public final int Decelerate = 2;
	public final int AccelerateDecelerate = 3;
	public final int Bounce = 4;
	public final int Overshoot = 5;
	public final int Anticipate = 6;
	public final int AnticipateOvershoot = 7;

	// LinearInterpolator,AccelerateInterpolator,DecelerateInterpolator,AccelerateDecelerateInterpolator,
	// BounceInterpolator,OvershootInterpolator,AnticipateInterpolator,AnticipateOvershootInterpolator

	public AnimationController() {

	}

	private class MyAnimationListener implements AnimationListener {

		private View view;

		public MyAnimationListener(View view) {
			this.view = view;
		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			// this.view.setVisibility(View.VISIBLE);
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub
			this.view.setVisibility(View.GONE);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}

	}

	private void setEffect(Animation animation, int interpolatorType, long durationMillis, long delayMillis) {
		switch (interpolatorType) {
			case 0:
				animation.setInterpolator(new LinearInterpolator());
				break;
			case 1:
				animation.setInterpolator(new AccelerateInterpolator());
				break;
			case 2:
				animation.setInterpolator(new DecelerateInterpolator());
				break;
			case 3:
				animation.setInterpolator(new AccelerateDecelerateInterpolator());
				break;
			case 4:
				animation.setInterpolator(new BounceInterpolator());
				break;
			case 5:
				animation.setInterpolator(new OvershootInterpolator());
				break;
			case 6:
				animation.setInterpolator(new AnticipateInterpolator());
				break;
			case 7:
				animation.setInterpolator(new AnticipateOvershootInterpolator());
				break;
			default:
				break;
		}
		animation.setDuration(durationMillis);
		animation.setStartOffset(delayMillis);
	}

	private void baseIn(View view, Animation animation, long durationMillis, long delayMillis) {
		setEffect(animation, Default, durationMillis, delayMillis);
		view.setVisibility(View.VISIBLE);
		view.startAnimation(animation);
	}

	private void baseOut(View view, Animation animation, long durationMillis, long delayMillis) {
		setEffect(animation, Default, durationMillis, delayMillis);
		animation.setAnimationListener(new MyAnimationListener(view));
		view.startAnimation(animation);
		view.setVisibility(View.INVISIBLE);
	}

	public void show(View view) {
		view.setVisibility(View.VISIBLE);
	}

	public void hide(View view) {
		view.setVisibility(View.GONE);
	}

	public void transparent(View view) {
		view.setVisibility(View.INVISIBLE);
	}

	/**
	 * 渐入效果
	 * @Description
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 */
	public void fadeIn(View view, long durationMillis, long delayMillis) {
		AlphaAnimation animation = new AlphaAnimation(0, 1);
		baseIn(view, animation, durationMillis, delayMillis);
	}

	/**
	 * 渐出效果
	 * @Description
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 */
	public void fadeOut(View view, long durationMillis, long delayMillis) {
		AlphaAnimation animation = new AlphaAnimation(1, 0);
		baseOut(view, animation, durationMillis, delayMillis);
	}

	/**
	 * 从左边滑入
	 * @Description
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 */
	public void slideInFromLeft(View view, long durationMillis, long delayMillis) {
		// 屏幕从左至右:-1、0、1
		// fromXValue=-1,toXValue=0
		TranslateAnimation animation = new TranslateAnimation(RELA_2_PARENT, -1, RELA_2_PARENT, 0, RELA_2_PARENT, 0,
				RELA_2_PARENT, 0);
		baseIn(view, animation, durationMillis, delayMillis);
	}

	/**
	 * 从右边滑入
	 * @Description
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 */
	public void slideInFromRight(View view, long durationMillis, long delayMillis) {
		// 屏幕从左至右:-1、0、1
		// fromXValue=1,toXValue=0
		TranslateAnimation animation = new TranslateAnimation(RELA_2_PARENT, 1, RELA_2_PARENT, 0, RELA_2_PARENT, 0,
				RELA_2_PARENT, 0);
		baseIn(view, animation, durationMillis, delayMillis);
	}

	/**
	 * 从上边滑入
	 * @Description
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 */
	public void slideInFromTop(View view, long durationMillis, long delayMillis) {
		// 屏幕从上至下:-1、0、1
		// fromYValue=-1,toYValue=0
		TranslateAnimation animation = new TranslateAnimation(RELA_2_PARENT, 0, RELA_2_PARENT, 0, RELA_2_PARENT, -1,
				RELA_2_PARENT, 0);
		baseIn(view, animation, durationMillis, delayMillis);
	}

	/**
	 * 从下边滑入
	 * @Description
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 */
	public void slideInFromBottom(View view, long durationMillis, long delayMillis) {
		// 屏幕从上至下:-1、0、1
		// fromYValue=1,toYValue=0
		TranslateAnimation animation = new TranslateAnimation(RELA_2_PARENT, 0, RELA_2_PARENT, 0, RELA_2_PARENT, 1,
				RELA_2_PARENT, 0);
		baseIn(view, animation, durationMillis, delayMillis);
	}

	/**
	 * 从左滑出
	 * @Description
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 */
	public void slideOutFromLeft(View view, long durationMillis, long delayMillis) {
		// 屏幕从左至右:-1、0、1
		// fromXValue=0,toXValue=-1
		TranslateAnimation animation = new TranslateAnimation(RELA_2_PARENT, 0, RELA_2_PARENT, -1, RELA_2_PARENT, 0,
				RELA_2_PARENT, 0);
		baseOut(view, animation, durationMillis, delayMillis);
	}

	/**
	 * 从右边滑出
	 * @Description
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 */
	public void slideOutFromRight(View view, long durationMillis, long delayMillis) {
		// 屏幕从左至右:-1、0、1
		// 从右边滑入，fromXValue=0,toXValue=1
		TranslateAnimation animation = new TranslateAnimation(RELA_2_PARENT, 0, RELA_2_PARENT, 1, RELA_2_PARENT, 0,
				RELA_2_PARENT, 0);
		baseOut(view, animation, durationMillis, delayMillis);
	}

	/**
	 * 从上边滑出
	 * @Description
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 */
	public void slideOutFromTop(View view, long durationMillis, long delayMillis) {
		// 屏幕从上至下:-1、0、1
		// fromYValue=0,toYValue=-1
		TranslateAnimation animation = new TranslateAnimation(RELA_2_PARENT, 0, RELA_2_PARENT, 0, RELA_2_PARENT, 0,
				RELA_2_PARENT, -1);
		baseOut(view, animation, durationMillis, delayMillis);
	}

	/**
	 * 从下边滑出
	 * @Description
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 */
	public void slideOutFromBottom(View view, long durationMillis, long delayMillis) {
		// 屏幕从上至下:-1、0、1
		// fromYValue=0,toYValue=1
		TranslateAnimation animation = new TranslateAnimation(RELA_2_PARENT, 0, RELA_2_PARENT, 0, RELA_2_PARENT, 0,
				RELA_2_PARENT, 1);
		baseOut(view, animation, durationMillis, delayMillis);
	}

	/**
	 * 放大显示
	 * @Description
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 */
	public void scaleIn(View view, long durationMillis, long delayMillis) {
		ScaleAnimation animation = new ScaleAnimation(0, 1, 0, 1, RELA_2_PARENT, 0.5f, RELA_2_PARENT, 0.5f);
		baseIn(view, animation, durationMillis, delayMillis);
	}

	/**
	 * 缩小消失
	 * @Description
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 */
	public void scaleOut(View view, long durationMillis, long delayMillis) {
		ScaleAnimation animation = new ScaleAnimation(1, 0, 1, 0, RELA_2_PARENT, 0.5f, RELA_2_PARENT, 0.5f);
		baseOut(view, animation, durationMillis, delayMillis);
	}

	public void rotateIn(View view, long durationMillis, long delayMillis) {
		RotateAnimation animation = new RotateAnimation(-90, 0, RELA_2_SELF, 0, RELA_2_SELF, 1);
		baseIn(view, animation, durationMillis, delayMillis);
	}

	public void rotateOut(View view, long durationMillis, long delayMillis) {
		RotateAnimation animation = new RotateAnimation(0, 90, RELA_2_SELF, 0, RELA_2_SELF, 1);
		baseOut(view, animation, durationMillis, delayMillis);
	}

	public void scaleRotateIn(View view, long durationMillis, long delayMillis) {
		ScaleAnimation animation1 = new ScaleAnimation(0, 1, 0, 1, RELA_2_SELF, 0.5f, RELA_2_SELF, 0.5f);
		RotateAnimation animation2 = new RotateAnimation(0, 360, RELA_2_SELF, 0.5f, RELA_2_SELF, 0.5f);
		AnimationSet animation = new AnimationSet(false);
		animation.addAnimation(animation1);
		animation.addAnimation(animation2);
		baseIn(view, animation, durationMillis, delayMillis);
	}

	public void scaleRotateOut(View view, long durationMillis, long delayMillis) {
		ScaleAnimation animation1 = new ScaleAnimation(1, 0, 1, 0, RELA_2_SELF, 0.5f, RELA_2_SELF, 0.5f);
		RotateAnimation animation2 = new RotateAnimation(0, 360, RELA_2_SELF, 0.5f, RELA_2_SELF, 0.5f);
		AnimationSet animation = new AnimationSet(false);
		animation.addAnimation(animation1);
		animation.addAnimation(animation2);
		baseOut(view, animation, durationMillis, delayMillis);
	}

	public void slideFadeIn(View view, long durationMillis, long delayMillis) {
		TranslateAnimation animation1 = new TranslateAnimation(RELA_2_PARENT, 1, RELA_2_PARENT, 0, RELA_2_PARENT, 0,
				RELA_2_PARENT, 0);
		AlphaAnimation animation2 = new AlphaAnimation(0, 1);
		AnimationSet animation = new AnimationSet(false);
		animation.addAnimation(animation1);
		animation.addAnimation(animation2);
		baseIn(view, animation, durationMillis, delayMillis);
	}

	public void slideFadeInFromLeft(View view, long durationMillis, long delayMillis) {
		TranslateAnimation animation1 = new TranslateAnimation(RELA_2_SELF, -1, RELA_2_SELF, 0, RELA_2_SELF, 0,
				RELA_2_SELF, 0);
		AlphaAnimation animation2 = new AlphaAnimation(0, 1);
		AnimationSet animation = new AnimationSet(false);
		animation.addAnimation(animation1);
		animation.addAnimation(animation2);
		baseIn(view, animation, durationMillis, delayMillis);
	}

	public void slideFadeInFromLeft(View view, long durationMillis, long delayMillis, InterpolatedTimeListener listener) {
		MyTranslateAnimation animation1 = new MyTranslateAnimation(RELA_2_SELF, -1, RELA_2_SELF, 0, RELA_2_SELF, 0,
				RELA_2_SELF, 0);
		animation1.setInterpolatedTimeListener(listener);
		AlphaAnimation animation2 = new AlphaAnimation(0, 1);
		AnimationSet animation = new AnimationSet(false);
		animation.addAnimation(animation1);
		animation.addAnimation(animation2);
		baseIn(view, animation, durationMillis, delayMillis);
	}

	public void slideFadeOut(View view, long durationMillis, long delayMillis) {
		TranslateAnimation animation1 = new TranslateAnimation(RELA_2_SELF, 0, RELA_2_SELF, -1, RELA_2_SELF, 0,
				RELA_2_SELF, 0);
		AlphaAnimation animation2 = new AlphaAnimation(1, 0);
		AnimationSet animation = new AnimationSet(false);
		animation.addAnimation(animation1);
		animation.addAnimation(animation2);
		baseOut(view, animation, durationMillis, delayMillis);
	}

	/**
	 * 渐变划入
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public void slideFadeIn(View view, long durationMillis, long delayMillis, boolean left, boolean top, boolean right,
			boolean bottom) {
		TranslateAnimation animation1 = null;
		if (left) {
			animation1 = new TranslateAnimation(RELA_2_PARENT, -1, RELA_2_PARENT, 0, RELA_2_PARENT, 0, RELA_2_PARENT, 0);
		} else if (top) {
			animation1 = new TranslateAnimation(RELA_2_PARENT, 0, RELA_2_PARENT, 0, RELA_2_PARENT, -1, RELA_2_PARENT, 0);
		} else if (right) {
			animation1 = new TranslateAnimation(RELA_2_SELF, 1, RELA_2_SELF, 0, RELA_2_SELF, 0, RELA_2_SELF, 0);
		} else if (bottom) {
			animation1 = new TranslateAnimation(RELA_2_PARENT, 0, RELA_2_PARENT, 0, RELA_2_PARENT, 1, RELA_2_PARENT, 0);
		}

		AnimationSet animation = new AnimationSet(false);
		AlphaAnimation animation2 = new AlphaAnimation(0, 1);
		if (null != animation1) {
			animation.addAnimation(animation1);
		}
		animation.addAnimation(animation2);
		baseIn(view, animation, durationMillis, delayMillis);
	}

	/**
	 * 渐变滑出
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public void slideFadeOut(View view, long durationMillis, long delayMillis, boolean left, boolean top,
			boolean right, boolean bottom) {
		if (null == view) {
			return;
		}

		// 屏幕从左至右:-1、0、1
		// fromXValue=-1,toXValue=0

		// 屏幕从上至下:-1、0、1
		// fromYValue=-1,toYValue=0

		TranslateAnimation animation1 = null;
		if (left) {
			animation1 = new TranslateAnimation(RELA_2_PARENT, 0, RELA_2_PARENT, -1, RELA_2_PARENT, 0, RELA_2_PARENT, 0);
		} else if (top) {
			animation1 = new TranslateAnimation(RELA_2_PARENT, 0, RELA_2_PARENT, 0, RELA_2_PARENT, 0, RELA_2_PARENT, -1);
		} else if (right) {
			animation1 = new TranslateAnimation(RELA_2_SELF, 0, RELA_2_SELF, 1, RELA_2_SELF, 0, RELA_2_SELF, 0);
		} else if (bottom) {
			animation1 = new TranslateAnimation(RELA_2_PARENT, 0, RELA_2_PARENT, 0, RELA_2_PARENT, 0, RELA_2_PARENT, 1);
		}

		AnimationSet animation = new AnimationSet(false);
		AlphaAnimation animation2 = new AlphaAnimation(1, 0);
		if (null != animation1) {
			animation.addAnimation(animation1);
		}
		animation.addAnimation(animation2);
		baseOut(view, animation, durationMillis, delayMillis);
	}

	/**
	 * 渐变滑出
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 * @param translateListener 平移动画进度
	 * @param animationListener
	 */
	public void slideFadeOut(View view, long durationMillis, long delayMillis, boolean left, boolean top,
			boolean right, boolean bottom, InterpolatedTimeListener translateListener,
			AnimationListener animationListener) {
		AnimationSet animationSet = new AnimationSet(false);
		AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0.0f);
		MyTranslateAnimation translateAnimation = null;
		if (left) {
			translateAnimation = new MyTranslateAnimation(RELA_2_PARENT, 0, RELA_2_PARENT, -1, RELA_2_PARENT, 0,
					RELA_2_PARENT, 0);
		} else if (top) {
			translateAnimation = new MyTranslateAnimation(RELA_2_PARENT, 0, RELA_2_PARENT, 0, RELA_2_PARENT, 0,
					RELA_2_PARENT, -1);
		} else if (right) {
			translateAnimation = new MyTranslateAnimation(RELA_2_SELF, 0, RELA_2_SELF, 1, RELA_2_SELF, 0, RELA_2_SELF,
					0);
		} else if (bottom) {
			translateAnimation = new MyTranslateAnimation(RELA_2_PARENT, 0, RELA_2_PARENT, 0, RELA_2_PARENT, 0,
					RELA_2_PARENT, 1);
		}

		if (null != translateAnimation) {
			animationSet.addAnimation(translateAnimation);
			if (null != translateListener) {
				translateAnimation.setInterpolatedTimeListener(translateListener);
			}
		}
		animationSet.addAnimation(alphaAnimation);
		animationSet.setDuration(durationMillis);
		animationSet.setStartOffset(delayMillis);

		if (null != animationListener) {
			animationSet.setAnimationListener(animationListener);
		} else {
			animationSet.setAnimationListener(new MyAnimationListener(view));
		}
		view.startAnimation(animationSet);
	}

	/**
	 * 渐变划入
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 * @param translateListener 平移动画进度
	 * @param animationListener
	 */
	public void slideFadeIn(View view, long durationMillis, long delayMillis, boolean left, boolean top, boolean right,
			boolean bottom, InterpolatedTimeListener translateListener, AnimationListener animationListener) {
		AnimationSet animationSet = new AnimationSet(false);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1f);
		MyTranslateAnimation translateAnimation = null;
		if (left) {
			translateAnimation = new MyTranslateAnimation(RELA_2_PARENT, -1, RELA_2_PARENT, 0, RELA_2_PARENT, 0,
					RELA_2_PARENT, 0);
		} else if (top) {
			translateAnimation = new MyTranslateAnimation(RELA_2_PARENT, 0, RELA_2_PARENT, 0, RELA_2_PARENT, -1,
					RELA_2_PARENT, 0);
		} else if (right) {
			translateAnimation = new MyTranslateAnimation(RELA_2_SELF, 1, RELA_2_SELF, 0, RELA_2_SELF, 0, RELA_2_SELF,
					0);
		} else if (bottom) {
			translateAnimation = new MyTranslateAnimation(RELA_2_PARENT, 0, RELA_2_PARENT, 0, RELA_2_PARENT, 1,
					RELA_2_PARENT, 0);
		}

		if (null != translateAnimation) {
			animationSet.addAnimation(translateAnimation);
			if (null != translateListener) {
				translateAnimation.setInterpolatedTimeListener(translateListener);
			}
		}
		animationSet.addAnimation(alphaAnimation);
		animationSet.setDuration(durationMillis);
		animationSet.setStartOffset(delayMillis);

		if (null != animationListener) {
			animationSet.setAnimationListener(animationListener);
		} else {
			view.setVisibility(View.VISIBLE);
		}
		view.startAnimation(animationSet);
	}

	/**
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 * @param fromX 动画起始时 X坐标上的伸缩尺寸
	 * @param fromY 动画起始时Y坐标上的伸缩尺寸
	 * @param pivotXValue 动画相对于物件的X坐标的开始位置
	 * @param pivotYValue 动画相对于物件的Y坐标的开始位置
	 */
	public void scaleIn(View view, long durationMillis, long delayMillis, float fromX, float fromY, float pivotXValue,
			float pivotYValue) {
		if (fromX < 0) {
			fromX = 0;
		}
		if (fromX > 1) {
			fromX = 1;
		}

		if (fromY < 0) {
			fromY = 0;
		}
		if (fromY > 1) {
			fromY = 1;
		}

		if (pivotXValue < 0) {
			pivotXValue = 0;
		}
		if (pivotXValue > 1) {
			pivotXValue = 1;
		}

		if (pivotYValue < 0) {
			pivotYValue = 0;
		}
		if (pivotYValue > 1) {
			pivotYValue = 1;
		}

		ScaleAnimation scale = new ScaleAnimation(fromX, // 动画起始时 X坐标上的伸缩尺寸
				1.0f,// 动画结束时 X坐标上的伸缩尺寸
				fromY, // 动画起始时Y坐标上的伸缩尺寸
				1.0f,// 动画结束时Y坐标上的伸缩尺寸
				Animation.RELATIVE_TO_SELF,//
				pivotXValue,// 动画相对于物件的X坐标的开始位置
				Animation.RELATIVE_TO_SELF, //
				pivotYValue);// 动画相对于物件的Y坐标的开始位置

		scale.setDuration(durationMillis);
		scale.setStartOffset(delayMillis);
		view.setVisibility(View.VISIBLE);
		view.startAnimation(scale);
	}

	public void scaleOut(View view, long durationMillis, long delayMillis, float fromX, float fromY, float pivotXValue,
			float pivotYValue) {
		if (fromX < 0) {
			fromX = 0;
		}
		if (fromX > 1) {
			fromX = 1;
		}

		if (fromY < 0) {
			fromY = 0;
		}
		if (fromY > 1) {
			fromY = 1;
		}

		if (pivotXValue < 0) {
			pivotXValue = 0;
		}
		if (pivotXValue > 1) {
			pivotXValue = 1;
		}

		if (pivotYValue < 0) {
			pivotYValue = 0;
		}
		if (pivotYValue > 1) {
			pivotYValue = 1;
		}

		ScaleAnimation scale = new ScaleAnimation(fromX, 0, fromY, 0, Animation.RELATIVE_TO_SELF, pivotXValue,
				Animation.RELATIVE_TO_SELF, pivotYValue);

		scale.setDuration(durationMillis);
		scale.setStartOffset(delayMillis);
		scale.setAnimationListener(new MyAnimationListener(view));
		view.startAnimation(scale);
	}

	public void scaleTranslateIn(View view, long durationMillis, long delayMillis, float fromX, float fromY,
			float pivotXValue, float pivotYValue, float fromXDelta, float toXDelta, float fromYDelta, float toYDelta) {
		if (fromX < 0) {
			fromX = 0;
		}
		if (fromX > 1) {
			fromX = 1;
		}

		if (fromY < 0) {
			fromY = 0;
		}
		if (fromY > 1) {
			fromY = 1;
		}

		if (pivotXValue < 0) {
			pivotXValue = 0;
		}
		if (pivotXValue > 1) {
			pivotXValue = 1;
		}

		if (pivotYValue < 0) {
			pivotYValue = 0;
		}
		if (pivotYValue > 1) {
			pivotYValue = 1;
		}

		AlphaAnimation alpha = new AlphaAnimation(0, 1);
		TranslateAnimation translate = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
		ScaleAnimation scale = new ScaleAnimation(fromX, 1.0f, fromY, 1.0f, Animation.RELATIVE_TO_SELF, pivotXValue,
				Animation.RELATIVE_TO_SELF, pivotYValue);

		AnimationSet animation = new AnimationSet(false);
		animation.setDuration(durationMillis);
		animation.setStartOffset(delayMillis);
		animation.addAnimation(translate);
		animation.addAnimation(scale);
		animation.addAnimation(alpha);

		view.setVisibility(View.VISIBLE);
		view.startAnimation(scale);
	}

	public void scaleTranslateOut(View view, long durationMillis, long delayMillis, float fromX, float fromY,
			float pivotXValue, float pivotYValue, float fromXDelta, float toXDelta, float fromYDelta, float toYDelta) {
		if (fromX < 0) {
			fromX = 0;
		}
		if (fromX > 1) {
			fromX = 1;
		}

		if (fromY < 0) {
			fromY = 0;
		}
		if (fromY > 1) {
			fromY = 1;
		}

		if (pivotXValue < 0) {
			pivotXValue = 0;
		}
		if (pivotXValue > 1) {
			pivotXValue = 1;
		}

		if (pivotYValue < 0) {
			pivotYValue = 0;
		}
		if (pivotYValue > 1) {
			pivotYValue = 1;
		}

		AlphaAnimation alpha = new AlphaAnimation(1, 0);
		TranslateAnimation translate = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
		ScaleAnimation scale = new ScaleAnimation(fromX, 0, fromY, 0, Animation.RELATIVE_TO_SELF, pivotXValue,
				Animation.RELATIVE_TO_SELF, pivotYValue);

		AnimationSet animation = new AnimationSet(false);
		animation.setDuration(durationMillis);
		animation.setStartOffset(delayMillis);
		animation.addAnimation(translate);
		animation.addAnimation(scale);
		animation.addAnimation(alpha);
		animation.setAnimationListener(new MyAnimationListener(view));
		view.startAnimation(scale);
	}

	/**
	 * 旋转
	 * @Description view旋转动画
	 * @param view
	 * @param fromDegrees
	 * @param toDegrees
	 * @param durationMillis 持续时间(ms)
	 * @param clockwise 顺时针方向
	 */
	public void rotateAnimation(View view, float fromDegrees, float toDegrees, int durationMillis, boolean clockwise) {
		RotateAnimation rotate = null;
		if (clockwise) {
			rotate = new RotateAnimation(fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
		} else {
			rotate = new RotateAnimation(toDegrees, fromDegrees, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
		}
		rotate.setDuration(durationMillis);
		rotate.setFillEnabled(true);
		rotate.setFillAfter(true);
		view.startAnimation(rotate);
	}

	/**
	 * icon缩小消失的动画
	 * @Description
	 * @param durationMillis
	 * @return
	 */
	public Animation iconMiniAnimation(int durationMillis) {
		Animation miniAnimation = new ScaleAnimation(1.0f, 0f, 1.0f, 0f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		miniAnimation.setDuration(durationMillis);
		miniAnimation.setFillAfter(true);

		return miniAnimation;
	}

	/**
	 * icon放大渐变消失的动画
	 * @Description
	 * @param durationMillis
	 * @param multiple 放大倍数
	 * @return
	 */
	public Animation iconMaxAnimation(int durationMillis, int multiple) {
		if (multiple <= 0) { // 默认放大倍数为2
			multiple = 2;
		}

		AnimationSet animationset = new AnimationSet(true);

		// Animation maxAnimation = new ScaleAnimation(1.0f, 4.0f, 1.0f, 4.0f,
		// Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		Animation maxAnimation = new ScaleAnimation(1.0f, 1.0f * multiple, 1.0f, 1.0f * multiple,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		Animation alphaAnimation = new AlphaAnimation(1, 0);

		animationset.addAnimation(maxAnimation);
		animationset.addAnimation(alphaAnimation);

		animationset.setDuration(durationMillis);
		animationset.setFillAfter(true);

		return animationset;
	}
}
