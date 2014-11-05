/**
 * @(#)SlideListItemListener.java 2014-1-20 Copyright 2014 it.kedacom.com, Inc.
 *                                All rights reserved.
 */

package com.pc.ui.listview.x.slideitem.libs;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * @author chenjian
 * @date 2014-1-20
 */

public class SlideListItemListener implements View.OnTouchListener {

	// 屏幕宽度
	private float screenWidth = 1;

	// 最后一次触发的坐标
	private float lastMotionX, lastMotionY;

	// 横向移动的路径
	private float moveX;
	// 自动滑行的速度
	private long animationTime = 200;

	// 用来判定Y轴的滑动
	private int touchSlop;

	// 垂直滑动
	private boolean isScrollInY;
	private boolean haveItemSwipe;
	private boolean haveItemSelected;

	private ListView slideListView;

	private View mItemView;
	private View mShowView;
	private View mHideView;

	private int showviewId;
	private int hideviewId;

	// 现在按下的位置
	private int downPosition;

	private Rect rect = new Rect();

	private ISlideListItemListener mISlideListItemListener;

	public SlideListItemListener(ListView slideListView, int touchSlop, int showviewId, int hideviewId) {
		this.slideListView = slideListView;
		this.touchSlop = touchSlop;
		this.showviewId = showviewId;
		this.hideviewId = hideviewId;
	}

	public void setISlideListItemListener(ISlideListItemListener slideListItemListener) {
		mISlideListItemListener = slideListItemListener;
	}

	/**
	 * 
	 */
	public void resetMoveX() {
		moveX = 0;
	}

	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		if (screenWidth < 2) {
			screenWidth = slideListView.getWidth();
		}

		switch (motionEvent.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				haveItemSwipe = false;
				lastMotionX = motionEvent.getRawX();
				lastMotionY = motionEvent.getRawY();
				final int position = getPosition();
				final int firstVisiblePosition = slideListView.getFirstVisiblePosition();
				final int lastVisiblePosition = slideListView.getLastVisiblePosition();
				mItemView = slideListView.getChildAt(downPosition - firstVisiblePosition);

				if ((downPosition != position) && (downPosition >= firstVisiblePosition)
						&& (downPosition <= lastVisiblePosition)) {
					if (null != mItemView) {
						mShowView = mItemView.findViewById(showviewId);
						mHideView = mItemView.findViewById(hideviewId);
					}
					ViewPropertyAnimator.animate(mShowView).translationX(0).setDuration(100);
					ViewPropertyAnimator.animate(mHideView).translationX(0).setDuration(100);
					moveX = 0;
					haveItemSelected = false;
				}

				downPosition = position;

				if (null != mISlideListItemListener) {
					mISlideListItemListener.onActionDown(downPosition, mItemView);
				}
				break;

			case MotionEvent.ACTION_UP:
				// 垂直滑动
				if (isScrollInY) {
					break;
				}

				else {
					final int hideViewWidth = mHideView.getWidth();
					final float translationX = ViewHelper.getTranslationX(mShowView);
					float deltaX = 0;
					if (haveItemSelected) {

					} else if (translationX == -hideViewWidth) {
						animationTime = 0;
					} else {
						animationTime = 200;
					}

					if (translationX > -hideViewWidth / 2 || haveItemSelected) {
						deltaX = 0;
						haveItemSelected = false;
					} else if (translationX <= -hideViewWidth / 2) {
						deltaX = -hideViewWidth;
						haveItemSelected = true;
					}
					moveX += deltaX;
					if ((moveX >= 0) || (deltaX == 0)) {
						moveX = 0;
					} else if (moveX <= -hideViewWidth) {
						moveX = -hideViewWidth;
					}

					// 自动滑行
					ViewPropertyAnimator.animate(mShowView).translationX(deltaX).setDuration(animationTime);
					ViewPropertyAnimator.animate(mHideView).translationX(deltaX).setDuration(animationTime)
							.setListener(new AnimatorListener() {

								@Override
								public void onAnimationStart(Animator animation) {
								}

								// 动画结束时才能点击删除按钮
								@Override
								public void onAnimationEnd(Animator animation) {
									if (mISlideListItemListener != null) {
										mISlideListItemListener.onClick4HideView(downPosition, mItemView);
									}
								}

								@Override
								public void onAnimationCancel(Animator animation) {
								}

								@Override
								public void onAnimationRepeat(Animator animation) {
								}
							});

					if (null != mISlideListItemListener) {
						mISlideListItemListener.onActionUp(downPosition, mItemView);
					}
					break;
				}

			case MotionEvent.ACTION_MOVE:
				if (downPosition == -1) {
					break;
				}

				float deltaX = motionEvent.getRawX() - lastMotionX;
				isScrollInY = isMovingInY(motionEvent.getRawX(), motionEvent.getRawY());

				if (mISlideListItemListener != null) {
					mISlideListItemListener.onActionMove(isScrollInY, downPosition, mItemView);
				}

				if ((isScrollInY)) {
					return false;
				}

				mItemView = slideListView.getChildAt(downPosition - slideListView.getFirstVisiblePosition());
				mShowView = mItemView.findViewById(showviewId);
				mHideView = mItemView.findViewById(hideviewId);
				final int hideViewWidth = mHideView.getWidth();
				deltaX += moveX;
				if (deltaX >= -hideViewWidth / 5) {
					deltaX = 0;
				}
				if (deltaX <= -hideViewWidth) {
					deltaX = -hideViewWidth;
				}
				ViewHelper.setTranslationX(mShowView, deltaX);
				ViewHelper.setTranslationX(mHideView, deltaX);
				float translationX = ViewHelper.getTranslationX(mShowView);
				if (translationX != -hideViewWidth && translationX != 0) haveItemSwipe = true;
				return true;

			default:
				return false;
		}

		return false;
	}

	/**
	 * 检测是否是横向滑动
	 * @param x Position X
	 * @param y Position Y
	 */
	private boolean isMovingInY(float x, float y) {
		final int xDiff = (int) Math.abs(x - lastMotionX);
		final int yDiff = (int) Math.abs(y - lastMotionY);
		if (haveItemSelected || haveItemSwipe) {
			return false;
		}

		if (xDiff == 0 || x > lastMotionX) {
			return true;
		}

		if ((yDiff / (xDiff / (float) 5) >= 1) && (yDiff > touchSlop)) {
			return true;
		}

		return false;
	}

	/**
	 * 获取当前手指所在的position
	 */
	private int getPosition() {
		final int childCount = slideListView.getChildCount();
		int[] listViewCoords = new int[2];
		slideListView.getLocationOnScreen(listViewCoords);
		final int x = (int) lastMotionX - listViewCoords[0];
		final int y = (int) lastMotionY - listViewCoords[1];
		View child;
		int childPosition = 0;
		for (int i = 0; i < childCount; i++) {
			child = slideListView.getChildAt(i);
			child.getHitRect(rect);
			childPosition = slideListView.getPositionForView(child);
			if (rect.contains(x, y)) {
				return childPosition;
			}
		}
		return childPosition;
	}

}
