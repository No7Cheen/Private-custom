/**
 * @(#)FixdHorizontalScrollView.java   2014-9-18
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.ui.widget;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
  * Fixed HorizontalScrollView
  * 
  * @author chenj
  * @date 2014-9-18
  */

public class FixedHorizontalScrollView extends HorizontalScrollView {

	public interface ChildViewWatcher {

		public void beforeAddChanged(View v, int count);

		public void onAddChanged(View v, int before, int count);

		public void afterAddChanged(View v, int count);
	}

	LinearLayout container = null;
	private ChildViewWatcher mChildViewWatcher;

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */

	public FixedHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();
	}

	/**
	 * @param context
	 * @param attrs
	 */

	public FixedHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	/**
	 * @param context
	 */

	public FixedHorizontalScrollView(Context context) {
		super(context);

		init();
	}

	private void init() {
		container = new LinearLayout(getContext());
		container.setClipChildren(false);

		addView(container);
		setClipChildren(false);
		setupTransition();
	}

	public void addChildListener(ChildViewWatcher watcher) {
		mChildViewWatcher = watcher;
	}

	/**
	 * 设置右边固定View
	 *
	 * @param v
	 */
	public void addRightFixed(View v) {
		if (null == v) {
			return;
		}

		View oldView = container.getChildAt(-1);
		if (null != oldView && oldView.equals(v)) {
			return;
		}

		container.addView(v, -1);
	}

	/**
	 * container child Count
	 *
	 * @return
	 */
	public int getContainerCount() {
		if (null == container) {
			return 0;
		}

		return container.getChildCount();
	}

	/**
	 * 
	 *
	 * @param child
	 * @return
	 */
	public int indexOfContainerChild(View child) {
		if (null == container) {
			return -1;
		}

		return container.indexOfChild(child);
	}

	/**
	 * 删除子视图 
	 *
	 * @param index
	 */
	public void removeChildView(final int index) {
		removeChildView(container.getChildAt(index));
	}

	/**
	 * 删除子视图 
	 *
	 * @param child
	 */
	public void removeChildView(final View child) {
		if (null == container || null == child) {
			return;
		}

		if (null != mChildViewWatcher) {
			mChildViewWatcher.beforeAddChanged(child, getChildCount());
		}

		container.removeView(child);

		if (null != mChildViewWatcher) {
			mChildViewWatcher.onAddChanged(child, getChildCount() + 1, getChildCount());
			mChildViewWatcher.afterAddChanged(child, getChildCount());
		}
	}

	/**
	 * 添加子视图 
	 *
	 * @param child
	 */
	public void addChildView(final View child) {
		addChildView(child, null);
	}

	/**
	 * 添加子视图 
	 *
	 * @param child
	 * @param l
	 */
	public void addChildView(final View child, View.OnClickListener l) {
		if (null == container || null == child) {
			return;
		}

		if (null != mChildViewWatcher) {
			mChildViewWatcher.beforeAddChanged(child, getChildCount());
		}

		if (l == null) {
			child.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					// container.removeView(v);
					removeChildView(v);
				}
			});
		} else {
			child.setOnClickListener(l);
		}

		container.addView(child, container.getChildCount() - 1);

		if (null != mChildViewWatcher) {
			mChildViewWatcher.onAddChanged(child, getChildCount() - 1, getChildCount());
		}

		postDelayed(new Runnable() {

			@Override
			public void run() {
				smoothScrollBy(child.getWidth(), 0);

				if (null != mChildViewWatcher) {
					mChildViewWatcher.afterAddChanged(child, getChildCount());
				}
			}
		}, 300);
		// getResources().getInteger(android.R.integer.config_shortAnimTime);
	}

	/**
	 * cleanup
	 */
	public void clearChildAllView() {
		if (null == container) {
			return;
		}

		container.removeAllViews();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupTransition() {
		if (null == container) {
			return;
		}

		final LayoutTransition transition = new LayoutTransition();
		transition.setAnimator(LayoutTransition.APPEARING, transition.getAnimator(LayoutTransition.APPEARING));
		transition.setAnimator(LayoutTransition.DISAPPEARING, transition.getAnimator(LayoutTransition.DISAPPEARING));
		transition.setAnimator(LayoutTransition.CHANGE_APPEARING, transition.getAnimator(LayoutTransition.CHANGE_APPEARING));
		transition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, transition.getAnimator(LayoutTransition.CHANGE_DISAPPEARING));
		container.setLayoutTransition(transition);
	}

}
