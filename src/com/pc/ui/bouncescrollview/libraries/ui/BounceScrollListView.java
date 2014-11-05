package com.pc.ui.bouncescrollview.libraries.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.pc.ui.bouncescrollview.libraries.BounceScrollViewAdapterViewBase;
import com.pc.ui.bouncescrollview.libraries.EmptyViewMethodAccessor;
import com.pc.ui.bouncescrollview.libraries.HeaderFooterBase;

public class BounceScrollListView extends BounceScrollViewAdapterViewBase<ListView> {

	private HeaderFooterBase mHeaderView;
	private HeaderFooterBase mFooterView;

	private FrameLayout mLvHeaderFrame;
	private FrameLayout mLvFooterFrame;

	public BounceScrollListView(Context context) {
		super(context);
	}

	public BounceScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * add header view
	 * @param headerLayout
	 */
	public void setHeader(HeaderLayout headerLayout) {
		if (null == headerLayout) {
			return;
		}

		final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
		mHeaderView.removeAllViews();
		mHeaderView.addView(headerLayout, lp);

		mLvHeaderFrame.removeAllViews();
		mLvHeaderFrame.addView(mHeaderView, lp);
	}

	/**
	 * add Footer View
	 * @param footerLayout
	 */
	public void setFooter(FooterLayout footerLayout) {
		if (null == footerLayout) {
			return;
		}

		final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
		mFooterView.removeAllViews();
		mFooterView.addView(footerLayout, lp);

		mLvFooterFrame.removeAllViews();
		mLvFooterFrame.addView(mFooterView, lp);

		visibleFooterFrame();
	}

	@Override
	protected ListView createBounceScrollableView(Context context, AttributeSet attrs) {
		ListView lv = new InternalListView(context, attrs);

		// Set it to this so it can be used in ListActivity/ListFragment
		lv.setId(android.R.id.list);

		return lv;
	}

	@Override
	protected void handleStyledAttributes(TypedArray a) {
		super.handleStyledAttributes(a);

		final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);

		mLvHeaderFrame = new FrameLayout(getContext());
		mHeaderView = new HeaderLayout(getContext(), null);
		mLvHeaderFrame.addView(mHeaderView, lp);

		mLvFooterFrame = new FrameLayout(getContext());
		mFooterView = new FooterLayout(getContext(), null);
		mLvFooterFrame.addView(mFooterView, lp);

		// 添加页眉
		mBounceScrollableView.addHeaderView(mLvHeaderFrame, null, false);

		mLvHeaderFrame.setVisibility(View.VISIBLE);
		mLvFooterFrame.setVisibility(View.GONE);
	}

	/**
	 * 显示Header View
	 */
	public void visibleHeaderFrame() {
		if (null == mLvHeaderFrame) {
			return;
		}

		if (View.VISIBLE != mLvHeaderFrame.getVisibility()) {
			mLvHeaderFrame.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 隐藏Header View
	 */
	public void hideHeaderFrame() {
		if (null == mLvHeaderFrame) {
			return;
		}

		if (View.GONE != mLvHeaderFrame.getVisibility()) {
			mLvHeaderFrame.setVisibility(View.GONE);
		}
	}

	/**
	 * 显示Footer View
	 */
	public void visibleFooterFrame() {
		if (null == mLvFooterFrame) {
			return;
		}

		if (View.VISIBLE != mLvFooterFrame.getVisibility()) {
			mLvFooterFrame.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 隐藏Footer View
	 */
	public void hideFooterFrame() {
		if (null == mLvFooterFrame) {
			return;
		}

		if (View.GONE != mLvFooterFrame.getVisibility()) {
			mLvFooterFrame.setVisibility(View.GONE);
		}

		// mLvFooterFrame.removeAllViews();
	}

	protected class InternalListView extends ListView implements EmptyViewMethodAccessor {

		private boolean mAddedLvFooter = false;

		public InternalListView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		protected void dispatchDraw(Canvas canvas) {
			/**
			 * This is a bit hacky, but Samsung's ListView has got a bug in it
			 * when using Header/Footer Views and the list is empty. This masks
			 * the issue so that it doesn't cause an FC. See Issue #66.
			 */
			try {
				super.dispatchDraw(canvas);
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
		}

		@Override
		public boolean dispatchTouchEvent(MotionEvent ev) {
			/**
			 * This is a bit hacky, but Samsung's ListView has got a bug in it
			 * when using Header/Footer Views and the list is empty. This masks
			 * the issue so that it doesn't cause an FC. See Issue #66.
			 */
			try {
				return super.dispatchTouchEvent(ev);
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		public void setAdapter(ListAdapter adapter) {
			// Add the Footer View at the last possible moment
			if (null != mLvFooterFrame && !mAddedLvFooter) {
				addFooterView(mLvFooterFrame, null, false);
				mAddedLvFooter = true;
			}

			super.setAdapter(adapter);
		}

		@Override
		public void setEmptyView(View emptyView) {
			BounceScrollListView.this.setEmptyView(emptyView);
		}

		@Override
		public void setEmptyViewInternal(View emptyView) {
			super.setEmptyView(emptyView);
		}

	}

	/**
	 * Header View
	 * @author chenjian
	 * @date 2013-5-27
	 */
	public static class HeaderLayout extends HeaderFooterBase {

		public HeaderLayout(Context context, View headerView) {
			super(context);

			if (getParent() != null) {
				((HeaderFooterBase) getParent()).removeAllViews();
			}

			removeAllViews();

			if (headerView != null) {
				if (headerView.getParent() != null) {
					((HeaderLayout) getParent()).removeAllViews();
				}
				this.addView(headerView);
			}
		}

		@Override
		public void visibleAllViews() {

		}

		@Override
		public void hideAllViews() {

		}
	}

	/**
	 * Footer View
	 * @author chenjian
	 * @date 2013-5-27
	 */
	public static class FooterLayout extends HeaderFooterBase {

		public FooterLayout(Context context, View footerView) {
			super(context);

			if (getParent() != null) {
				((HeaderFooterBase) getParent()).removeAllViews();
			}

			removeAllViews();

			if (footerView != null) {
				this.addView(footerView);
			}
		}

		@Override
		public void visibleAllViews() {

		}

		@Override
		public void hideAllViews() {

		}
	}

	private OnResizeListener mListener;

	public interface OnResizeListener {

		void OnResize(int w, int h, int oldw, int oldh);
	}

	public void setOnResizeListener(OnResizeListener l) {
		mListener = l;
	}

	/**
	 * 检测布局的高度变化
	 * @see android.view.View#onSizeChanged(int, int, int, int)
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		if (mListener != null) {
			mListener.OnResize(w, h, oldw, oldh);
		}
	}

}
