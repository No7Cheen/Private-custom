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
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

public abstract class BounceScrollViewAdapterViewBase<T extends AbsListView> extends BounceScrollViewBase<T> implements
		OnScrollListener {

	private boolean mLastItemVisible; // 是否滚动到最后一个Item时，OnLastItemVisibleListener可用
	private OnScrollListener mOnScrollListener;
	private OnLastItemVisibleListener mOnLastItemVisibleListener;
	private View mEmptyView; // 当数据为空时显示的View

	public BounceScrollViewAdapterViewBase(Context context) {
		super(context);
		mBounceScrollableView.setOnScrollListener(this);
	}

	public BounceScrollViewAdapterViewBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		mBounceScrollableView.setOnScrollListener(this);
	}

	@Override
	protected void handleStyledAttributes(TypedArray a) {
		super.handleStyledAttributes(a);
	}

	/**
	 * set OnItemClickListener
	 * @param listener
	 */
	public void setOnItemClickListener(OnItemClickListener listener) {
		mBounceScrollableView.setOnItemClickListener(listener);
	}

	/**
	 * set OnScrollListener
	 * @param listener
	 */
	public final void setOnScrollListener(OnScrollListener listener) {
		mOnScrollListener = listener;
	}

	/**
	 * set OnLastItemVisibleListener
	 * @param listener 用于监听List滚动到最后一个Item的事件
	 */
	public final void setOnLastItemVisibleListener(OnLastItemVisibleListener listener) {
		mOnLastItemVisibleListener = listener;
	}

	/**
	 * {@link AdapterView#setAdapter(android.widget.Adapter)}
	 * @param adapter - Adapter to set
	 */
	public void setAdapter(ListAdapter adapter) {
		((AdapterView<ListAdapter>) mBounceScrollableView).setAdapter(adapter);
	}

	@Override
	public final void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount,
			final int totalItemCount) {

		if (DEBUG) {
			Log.d(LOG_TAG, "First Visible: " + firstVisibleItem + ". Visible Count: " + visibleItemCount
					+ ". Total Items:" + totalItemCount);
		}

		/**
		 * 设置是否最后一个Item是可用
		 */
		if (null != mOnLastItemVisibleListener) {
			mLastItemVisible = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount - 1);
		}

		// Finally call OnScrollListener if we have one
		if (null != mOnScrollListener) {
			mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}

	@Override
	public final void onScrollStateChanged(final AbsListView view, final int state) {
		/**
		 * 检测scrolling是否停止，且是否滚动到最底部一个Item
		 */
		if (state == OnScrollListener.SCROLL_STATE_IDLE && null != mOnLastItemVisibleListener && mLastItemVisible) {
			mOnLastItemVisibleListener.onLastItemVisibleListener();
		}

		if (null != mOnScrollListener) {
			mOnScrollListener.onScrollStateChanged(view, state);
		}
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		// if (null != mEmptyView) {
		// Log.e("Test", "--------");
		//
		// mEmptyView.scrollTo(-l, -t);
		// }
	}

	/**
	 * 是否可下拉
	 * @see com.example.overscroll.libraries.OverScrollViewBase#isReadyForPullTop()
	 */
	protected boolean isReadyForPullTop() {
		return isFirstItemVisible();
	}

	/**
	 * 是否可上拉
	 * @see com.example.overscroll.libraries.OverScrollViewBase#isReadyForPullBottom()
	 */
	protected boolean isReadyForPullBottom() {
		return isLastItemVisible();
	}

	/**
	 * 是否显示的是List的第一个Item
	 * @return
	 */
	private boolean isFirstItemVisible() {
		final Adapter adapter = mBounceScrollableView.getAdapter();

		if (null == adapter || adapter.isEmpty()) {
			if (DEBUG) {
				Log.d(LOG_TAG, "isFirstItemVisible. Empty View.");
			}

			return true;
		}

		/**
		 * 这里检测是否显示的是第一个Item时，需要考虑到add Header的情况
		 */
		if (mBounceScrollableView.getFirstVisiblePosition() <= 1) {
			final View firstVisibleChild = mBounceScrollableView.getChildAt(0);
			if (firstVisibleChild != null) {
				return firstVisibleChild.getTop() >= mBounceScrollableView.getTop();
			}
		}

		return false;
	}

	/**
	 * 是否显示的是List的最后一个Item
	 * @return
	 */
	private boolean isLastItemVisible() {
		final Adapter adapter = mBounceScrollableView.getAdapter();

		if (null == adapter || adapter.isEmpty()) {
			if (DEBUG) {
				Log.d(LOG_TAG, "isLastItemVisible. Empty View.");
			}

			return true;
		}

		final int lastItemPosition = mBounceScrollableView.getCount() - 1;
		final int lastVisiblePosition = mBounceScrollableView.getLastVisiblePosition();

		if (DEBUG) {
			Log.d(LOG_TAG, "isLastItemVisible. Last Item Position: " + lastItemPosition + " Last Visible Pos: "
					+ lastVisiblePosition);
		}

		if (lastVisiblePosition >= lastItemPosition - 1) {
			final int childIndex = lastVisiblePosition - mBounceScrollableView.getFirstVisiblePosition();
			final View lastVisibleChild = mBounceScrollableView.getChildAt(childIndex);
			if (lastVisibleChild != null) {
				return lastVisibleChild.getBottom() <= mBounceScrollableView.getBottom();
			}
		}

		return false;
	}

	/**
	 * 设置Empty View用于Adapter View
	 * <p/>
	 * We need it handle it ourselves so that we can Pull-to-Refresh when the
	 * Empty View is shown.
	 * <p/>
	 * Please note, you do <strong>not</strong> usually need to call this method
	 * yourself. Calling setEmptyView on the AdapterView will automatically call
	 * this method and set everything up. This includes when the Android
	 * Framework automatically sets the Empty View based on it's ID.
	 * @param newEmptyView - Empty View to be used
	 */
	public final void setEmptyView(View newEmptyView) {
		FrameLayout overScrollViewWrapper = getOverScrollViewWrapper();

		if (null != newEmptyView) {
			newEmptyView.setClickable(true);

			// 一出Parent View的Views
			ViewParent newEmptyViewParent = newEmptyView.getParent();
			if (null != newEmptyViewParent && newEmptyViewParent instanceof ViewGroup) {
				((ViewGroup) newEmptyViewParent).removeView(newEmptyView);
			}

			FrameLayout.LayoutParams lp = convertEmptyViewLayoutParams(newEmptyView.getLayoutParams());
			if (null != lp) {
				overScrollViewWrapper.addView(newEmptyView, lp);
			} else {
				overScrollViewWrapper.addView(newEmptyView);
			}
		}

		if (mBounceScrollableView instanceof EmptyViewMethodAccessor) {
			((EmptyViewMethodAccessor) mBounceScrollableView).setEmptyViewInternal(newEmptyView);
		} else {
			mBounceScrollableView.setEmptyView(newEmptyView);
		}

		mEmptyView = newEmptyView;
	}

	private static FrameLayout.LayoutParams convertEmptyViewLayoutParams(ViewGroup.LayoutParams lp) {
		FrameLayout.LayoutParams newLp = null;

		if (null != lp) {
			newLp = new FrameLayout.LayoutParams(lp);

			if (lp instanceof LinearLayout.LayoutParams) {
				newLp.gravity = ((LinearLayout.LayoutParams) lp).gravity;
			} else {
				newLp.gravity = Gravity.CENTER;
			}
		}

		return newLp;
	}

}
