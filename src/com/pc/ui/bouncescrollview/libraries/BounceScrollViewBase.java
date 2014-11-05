/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes. Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *******************************************************************************/
package com.pc.ui.bouncescrollview.libraries;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.privatecustom.publiclibs.R;

public abstract class BounceScrollViewBase<T extends View> extends LinearLayout {

	public final boolean DEBUG = false;

	public final String LOG_TAG = "BounceScrollViewBase";

	private final float FRICTION = 3.0f;

	private final int SMOOTH_SCROLL_DURATION_MS = 200;
	private final int SMOOTH_SCROLL_LONG_DURATION_MS = 325;
	private final int DEMO_SCROLL_INTERVAL = 225;

	static final String STATE_CURRENT_MODE = "os_current_mode";
	static final String STATE_SUPER = "os_super";

	private int mTouchSlop;
	private float mLastMotionX, mLastMotionY;
	private float mInitialMotionX, mInitialMotionY;

	private boolean mIsBeingDragged = false;

	protected T mBounceScrollableView;

	private Mode mCurrentMode;
	private FrameLayout mBounceScrollViewWrapper;

	private boolean mFilterTouchEvents = true;

	private Interpolator mScrollAnimationInterpolator;

	private HeaderFooterBase mBounceScrollHeaderLayout;
	private HeaderFooterBase mBounceScrollFooterLayout;

	private SmoothScrollRunnable mCurrentSmoothScrollRunnable;

	public BounceScrollViewBase(Context context) {
		super(context);
		init(context, null);
	}

	public BounceScrollViewBase(Context context, AttributeSet attrs) {
		super(context, attrs);

		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		setOrientation(LinearLayout.VERTICAL);
		setGravity(Gravity.CENTER);

		ViewConfiguration config = ViewConfiguration.get(context);
		mTouchSlop = config.getScaledTouchSlop();

		// Styleables from XML
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OverScroll);

		// OverScrollable View
		// By passing the attrs, we can add ListView/GridView params via XML
		mBounceScrollableView = createBounceScrollableView(context, attrs);
		addBounceScrollableView(context, mBounceScrollableView);

		// We need to create now layouts now
		mBounceScrollHeaderLayout = createBounceScrollHeaderLayout(context, a);
		mBounceScrollFooterLayout = createBounceScrollFooterLayout(context, a);

		// Let the derivative classes have a go at handling attributes, then
		// recycle them...
		handleStyledAttributes(a);
		a.recycle();

		// Finally update the UI for the modes
		updateUIForMode();
	}

	/**
	 * Updates the View State when the mode has been set. This does not do any
	 * checking that the mode is different to current state so always updates.
	 */
	protected void updateUIForMode() {
		// We need to use the correct LayoutParam values, based on scroll
		// direction
		final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		// Remove Header, and then add Header Loading View again if needed
		if (this == mBounceScrollHeaderLayout.getParent()) {
			removeView(mBounceScrollHeaderLayout);
		}

		// add Header
		addViewInternal(mBounceScrollHeaderLayout, 0, lp);

		// Remove Footer, and then add Footer Loading View again if needed
		if (this == mBounceScrollFooterLayout.getParent()) {
			removeView(mBounceScrollFooterLayout);
		}

		// add Footer
		addViewInternal(mBounceScrollFooterLayout, lp);

		refreshBounceScrollViewsSize();
	}

	@Override
	public void addView(View child, int index, ViewGroup.LayoutParams params) {
		if (DEBUG) {
			Log.d(LOG_TAG, "addView: " + child.getClass().getSimpleName());
		}

		final T bounceScrollableView = getBounceScrollableView();

		if (bounceScrollableView instanceof ViewGroup) {
			((ViewGroup) bounceScrollableView).addView(child, index, params);
		} else {
			throw new UnsupportedOperationException("OverScrollable View is not a ViewGroup so can't addView");
		}
	}

	public final Mode getCurrentMode() {
		return mCurrentMode;
	}

	public final T getBounceScrollableView() {
		return mBounceScrollableView;
	}

	@Override
	public final boolean onInterceptTouchEvent(MotionEvent event) {
		final int action = event.getAction();

		if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
			mIsBeingDragged = false;
			return false;
		}

		if (action != MotionEvent.ACTION_DOWN && mIsBeingDragged) {
			return true;
		}

		switch (action) {
			case MotionEvent.ACTION_MOVE: {
				if (isReadyForPull()) {
					final float y = event.getY(), x = event.getX();
					final float diff, oppositeDiff, absDiff;

					// We need to use the correct values, based on scroll
					// direction
					diff = y - mLastMotionY;
					oppositeDiff = x - mLastMotionX;
					absDiff = Math.abs(diff);

					if (absDiff > mTouchSlop && (!mFilterTouchEvents || absDiff > Math.abs(oppositeDiff))) {
						if (diff >= 1f && isReadyForPullTop()) {
							mLastMotionY = y;
							mLastMotionX = x;
							mIsBeingDragged = true;
							mCurrentMode = Mode.PULL_FROM_TOP;
						} else if (diff <= -1f && isReadyForPullBottom()) {
							mLastMotionY = y;
							mLastMotionX = x;
							mIsBeingDragged = true;
							mCurrentMode = Mode.PULL_FROM_BOTTOM;
						}
					}
				}
				break;
			}
			case MotionEvent.ACTION_DOWN: {
				if (isReadyForPull()) {
					mLastMotionY = mInitialMotionY = event.getY();
					mLastMotionX = mInitialMotionX = event.getX();
					mIsBeingDragged = false;
				}
				break;
			}
		}

		return mIsBeingDragged;
	}

	private boolean isReadyForPull() {
		if (isReadyForPullTop()) {
			return isReadyForPullTop();
		}

		if (isReadyForPullBottom()) {
			return isReadyForPullBottom();
		}

		return false;
	}

	@Override
	public final boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN && event.getEdgeFlags() != 0) {
			return false;
		}

		switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE:
				if (mIsBeingDragged) {
					mLastMotionY = event.getY();
					mLastMotionX = event.getX();
					pullEvent();
					return true;
				}
				break;

			case MotionEvent.ACTION_DOWN:
				if (isReadyForPull()) {
					mLastMotionY = mInitialMotionY = event.getY();
					mLastMotionX = mInitialMotionX = event.getX();
					return true;
				}
				break;

			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				if (mIsBeingDragged) {
					mIsBeingDragged = false;

					smoothScrollTo(0);

					return true;
				}
				break;
		}

		return false;
	}

	@Override
	public void setLongClickable(boolean longClickable) {
		getBounceScrollableView().setLongClickable(longClickable);
	}

	public void setScrollAnimationInterpolator(Interpolator interpolator) {
		mScrollAnimationInterpolator = interpolator;
	}

	/**
	 * Used internally for adding view. Need because we override addView to
	 * pass-through to the OverScrollable View
	 */
	protected final void addViewInternal(View child, int index, ViewGroup.LayoutParams params) {
		super.addView(child, index, params);
	}

	/**
	 * Used internally for adding view. Need because we override addView to
	 * pass-through to the OverScrollable View
	 */
	protected final void addViewInternal(View child, ViewGroup.LayoutParams params) {
		super.addView(child, -1, params);
	}

	protected HeaderFooterBase createBounceScrollHeaderLayout(Context context, TypedArray attrs) {
		// HeaderLayout layout = new HeaderLayout(context, attrs);
		HeaderFooterBase layout = new BounceScrollViewHeaderLayout(context, attrs);
		layout.setVisibility(View.VISIBLE);

		return layout;
	}

	protected HeaderFooterBase createBounceScrollFooterLayout(Context context, TypedArray attrs) {
		// FooterLayout layout = new FooterLayout(context, attrs);
		HeaderFooterBase layout = new BounceScrollViewFooterLayout(context, attrs);
		layout.setVisibility(View.VISIBLE);

		return layout;
	}

	/**
	 * This is implemented by derived classes to return the created View. If you
	 * need to use a custom View (such as a custom ListView), override this
	 * method and return an instance of your custom class.
	 * <p/>
	 * Be sure to set the ID of the view in this method, especially if you're
	 * using a ListActivity or ListFragment.
	 * @param context Context to create view with
	 * @param attrs AttributeSet from wrapped class. Means that anything you
	 *            include in the XML layout declaration will be routed to the
	 *            created View
	 * @return New instance of the OverScrollable View
	 */
	protected abstract T createBounceScrollableView(Context context, AttributeSet attrs);

	protected final HeaderFooterBase getFooterLayout() {
		return mBounceScrollFooterLayout;
	}

	protected final int getFooterSize() {
		return mBounceScrollFooterLayout.getContentSize();
	}

	protected final HeaderFooterBase getHeaderLayout() {
		return mBounceScrollHeaderLayout;
	}

	protected final int getHeaderSize() {
		return mBounceScrollHeaderLayout.getContentSize();
	}

	protected int getPullToScrollDuration() {
		return SMOOTH_SCROLL_DURATION_MS;
	}

	protected int getPullToScrollDurationLonger() {
		return SMOOTH_SCROLL_LONG_DURATION_MS;
	}

	protected FrameLayout getOverScrollViewWrapper() {
		return mBounceScrollViewWrapper;
	}

	/**
	 * Allows Derivative classes to handle the XML Attrs without creating a
	 * TypedArray themsevles
	 */
	protected void handleStyledAttributes(TypedArray a) {
	}

	/**
	 * Implemented by derived class to return whether the View is in a state
	 * where the user can Pull by scrolling from the start.
	 * @return true if the View is currently the correct state (for example, top
	 *         of a ListView)
	 */
	protected abstract boolean isReadyForPullTop();

	/**
	 * Implemented by derived class to return whether the View is in a state
	 * where the user can Pull by scrolling from the end.
	 * @return true if the View is currently in the correct state (for example,
	 *         bottom of a ListView)
	 */
	protected abstract boolean isReadyForPullBottom();

	/**
	 * Called by {@link #onRestoreInstanceState(Parcelable)} so that derivative
	 * classes can handle their saved instance state.
	 * @param savedInstanceState - Bundle which contains saved instance state.
	 */
	protected void onPtrRestoreInstanceState(Bundle savedInstanceState) {
	}

	/**
	 * Called by {@link #onSaveInstanceState()} so that derivative classes can
	 * save their instance state.
	 * @param saveState - Bundle to be updated with saved state.
	 */
	protected void onPtrSaveInstanceState(Bundle saveState) {
	}

	@Override
	protected final void onRestoreInstanceState(Parcelable state) {
		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;

			mCurrentMode = Mode.mapIntToValue(bundle.getInt(STATE_CURRENT_MODE, 0));

			// Let super Restore Itself
			super.onRestoreInstanceState(bundle.getParcelable(STATE_SUPER));

			// Now let derivative classes restore their state
			onPtrRestoreInstanceState(bundle);

			return;
		}

		super.onRestoreInstanceState(state);
	}

	@Override
	protected final Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle();

		// Let derivative classes get a chance to save state first, that way we
		// can make sure they don't overrite any of our values
		onPtrSaveInstanceState(bundle);

		if (null != mCurrentMode) {
			bundle.putInt(STATE_CURRENT_MODE, mCurrentMode.getIntValue());
		}
		bundle.putParcelable(STATE_SUPER, super.onSaveInstanceState());

		return bundle;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		if (DEBUG) {
			Log.d(LOG_TAG, String.format("onSizeChanged. W: %d, H: %d", w, h));
		}

		super.onSizeChanged(w, h, oldw, oldh);

		// We need to update the header/footer when our size changes
		refreshBounceScrollViewsSize();

		// Update the OverScrollable View layout
		refreshBounceScrollViewSize(w, h);

		/**
		 * As we're currently in a Layout Pass, we need to schedule another one
		 * to layout any changes we've made here
		 */
		post(new Runnable() {

			@Override
			public void run() {
				requestLayout();
			}
		});
	}

	/**
	 * Re-measure the Loading Views height, and adjust internal padding as
	 * necessary
	 */
	protected final void refreshBounceScrollViewsSize() {
		final int maximumPullScroll = (int) (getMaximumPullScroll() * 1.2f);

		int pLeft = getPaddingLeft();
		int pTop = getPaddingTop();
		int pRight = getPaddingRight();
		int pBottom = getPaddingBottom();

		mBounceScrollHeaderLayout.setHeight(maximumPullScroll);
		pTop = -maximumPullScroll;

		mBounceScrollFooterLayout.setHeight(maximumPullScroll);
		pBottom = -maximumPullScroll;

		if (DEBUG) {
			Log.d(LOG_TAG, String.format("Setting Padding. L: %d, T: %d, R: %d, B: %d", pLeft, pTop, pRight, pBottom));
		}

		setPadding(pLeft, pTop, pRight, pBottom);
	}

	protected final void refreshBounceScrollViewSize(int width, int height) {
		// We need to set the Height of the OverScrollable View to the same as
		// this layout
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mBounceScrollViewWrapper.getLayoutParams();
		if (lp.height != height) {
			lp.height = height;
			mBounceScrollViewWrapper.requestLayout();
		}
	}

	/**
	 * Helper method which just calls scrollTo() in the correct scrolling
	 * direction.
	 * @param value - New Scroll value
	 */
	protected final void setHeaderScroll(int value) {
		if (DEBUG) {
			Log.d(LOG_TAG, "setHeaderScroll: " + value);
		}

		// Clamp value to with pull scroll range
		final int maximumPullScroll = getMaximumPullScroll();
		value = Math.min(maximumPullScroll, Math.max(-maximumPullScroll, value));

		scrollTo(0, value);
	}

	/**
	 * Smooth Scroll to position using the default duration of
	 * {@value #SMOOTH_SCROLL_DURATION_MS} ms.
	 * @param scrollValue - Position to scroll to
	 */
	protected final void smoothScrollTo(int scrollValue) {
		smoothScrollTo(scrollValue, getPullToScrollDuration());
	}

	/**
	 * Smooth Scroll to position using the default duration of
	 * {@value #SMOOTH_SCROLL_DURATION_MS} ms.
	 * @param scrollValue - Position to scroll to
	 * @param listener - Listener for scroll
	 */
	protected final void smoothScrollTo(int scrollValue, OnSmoothScrollFinishedListener listener) {
		smoothScrollTo(scrollValue, getPullToScrollDuration(), 0, listener);
	}

	/**
	 * Smooth Scroll to position using the longer default duration of
	 * {@value #SMOOTH_SCROLL_LONG_DURATION_MS} ms.
	 * @param scrollValue - Position to scroll to
	 */
	protected final void smoothScrollToLonger(int scrollValue) {
		smoothScrollTo(scrollValue, getPullToScrollDurationLonger());
	}

	private void addBounceScrollableView(Context context, T overScrollableView) {
		mBounceScrollViewWrapper = new FrameLayout(context);
		mBounceScrollViewWrapper.addView(overScrollableView, ViewGroup.LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);

		addViewInternal(mBounceScrollViewWrapper, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
	}

	/**
	 * Actions a Pull Event
	 * @return true if the Event has been handled, false if there has been no
	 *         change
	 */
	private void pullEvent() {
		final int newScrollValue;
		final int itemDimension;
		final float initialMotionValue, lastMotionValue;

		initialMotionValue = mInitialMotionY;
		lastMotionValue = mLastMotionY;

		switch (mCurrentMode) {
			case PULL_FROM_BOTTOM:
				newScrollValue = Math.round(Math.max(initialMotionValue - lastMotionValue, 0) / FRICTION);
				itemDimension = getFooterSize();
				break;
			case PULL_FROM_TOP:
			default:
				newScrollValue = Math.round(Math.min(initialMotionValue - lastMotionValue, 0) / FRICTION);
				itemDimension = getHeaderSize();
				break;
		}

		setHeaderScroll(newScrollValue);
	}

	private int getMaximumPullScroll() {
		return Math.round(getHeight() / FRICTION);
	}

	/**
	 * Smooth Scroll to position using the specific duration
	 * @param scrollValue - Position to scroll to
	 * @param duration - Duration of animation in milliseconds
	 */
	private final void smoothScrollTo(int scrollValue, long duration) {
		smoothScrollTo(scrollValue, duration, 0, null);
	}

	private final void smoothScrollTo(int newScrollValue, long duration, long delayMillis,
			OnSmoothScrollFinishedListener listener) {
		if (null != mCurrentSmoothScrollRunnable) {
			mCurrentSmoothScrollRunnable.stop();
		}

		final int oldScrollValue = getScrollY();

		if (oldScrollValue != newScrollValue) {
			if (null == mScrollAnimationInterpolator) {
				// Default interpolator is a Decelerate Interpolator
				mScrollAnimationInterpolator = new DecelerateInterpolator();
			}
			mCurrentSmoothScrollRunnable = new SmoothScrollRunnable(oldScrollValue, newScrollValue, duration, listener);

			if (delayMillis > 0) {
				postDelayed(mCurrentSmoothScrollRunnable, delayMillis);
			} else {
				post(mCurrentSmoothScrollRunnable);
			}
		}
	}

	private final void smoothScrollToAndBack(int y) {
		smoothScrollTo(y, SMOOTH_SCROLL_DURATION_MS, 0, new OnSmoothScrollFinishedListener() {

			@Override
			public void onSmoothScrollFinished() {
				smoothScrollTo(0, SMOOTH_SCROLL_DURATION_MS, DEMO_SCROLL_INTERVAL, null);
			}
		});
	}

	public static enum Mode {

		/**
		 * Only allow the user to Pull from the start of the OverScrollable
		 * View. The start is either the Top or Left, depending on the scrolling
		 * direction.
		 */
		PULL_FROM_TOP(0x1),

		/**
		 * Only allow the user to Pull from the end of the OverScrollable View .
		 * The start is either the Bottom or Right, depending on the scrolling
		 * direction.
		 */
		PULL_FROM_BOTTOM(0x2);

		static Mode getDefault() {
			return PULL_FROM_TOP;
		}

		private int mIntValue;

		// The modeInt values need to match those from attrs.xml
		Mode(int modeInt) {
			mIntValue = modeInt;
		}

		int getIntValue() {
			return mIntValue;
		}

		static Mode mapIntToValue(final int modeInt) {
			for (Mode value : Mode.values()) {
				if (modeInt == value.getIntValue()) {
					return value;
				}
			}

			// If not, return default
			return getDefault();
		}
	}

	final class SmoothScrollRunnable implements Runnable {

		private final Interpolator mInterpolator;
		private final int mScrollToY;
		private final int mScrollFromY;
		private final long mDuration;
		private OnSmoothScrollFinishedListener mListener;

		private boolean mContinueRunning = true;
		private long mStartTime = -1;
		private int mCurrentY = -1;

		public SmoothScrollRunnable(int fromY, int toY, long duration, OnSmoothScrollFinishedListener listener) {
			mScrollFromY = fromY;
			mScrollToY = toY;
			mInterpolator = mScrollAnimationInterpolator;
			mDuration = duration;
			mListener = listener;
		}

		@Override
		public void run() {

			/**
			 * Only set mStartTime if this is the first time we're starting,
			 * else actually calculate the Y delta
			 */
			if (mStartTime == -1) {
				mStartTime = System.currentTimeMillis();
			} else {

				/**
				 * We do do all calculations in long to reduce software float
				 * calculations. We use 1000 as it gives us good accuracy and
				 * small rounding errors
				 */
				long normalizedTime = (1000 * (System.currentTimeMillis() - mStartTime)) / mDuration;
				normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);

				final int deltaY = Math.round((mScrollFromY - mScrollToY)
						* mInterpolator.getInterpolation(normalizedTime / 1000f));
				mCurrentY = mScrollFromY - deltaY;
				setHeaderScroll(mCurrentY);
			}

			// If we're not at the target Y, keep going...
			if (mContinueRunning && mScrollToY != mCurrentY) {
				postDelayed(this, 16);
			} else {
				if (null != mListener) {
					mListener.onSmoothScrollFinished();
				}
			}
		}

		public void stop() {
			mContinueRunning = false;
			removeCallbacks(this);
		}
	}

	static interface OnSmoothScrollFinishedListener {

		void onSmoothScrollFinished();
	}

	/**
	 * 一个简单的监听器，主要实现滚动到AdapterView底部时的事件
	 * @author chenjian
	 * @date 2013-5-30
	 */
	public static interface OnLastItemVisibleListener {

		/**
		 * 当一个List滚动到底部时被调用
		 */
		public void onLastItemVisibleListener();
	}

}
