package com.pc.ui.bouncescrollview.libraries.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListView;

import com.pc.ui.bouncescrollview.libraries.BounceScrollViewAdapterViewBase;
import com.pc.ui.bouncescrollview.libraries.EmptyViewMethodAccessor;

public class BounceScrollExpandableListView extends BounceScrollViewAdapterViewBase<ExpandableListView> {

	public BounceScrollExpandableListView(Context context) {
		super(context);
	}

	public BounceScrollExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected ExpandableListView createBounceScrollableView(Context context, AttributeSet attrs) {
		final ExpandableListView lv = new InternalExpandableListView(context, attrs);

		// Set it to this so it can be used in ListActivity/ListFragment
		lv.setId(android.R.id.list);

		return lv;
	}

	@Override
	protected void handleStyledAttributes(TypedArray a) {
		super.handleStyledAttributes(a);
	}

	class InternalExpandableListView extends ExpandableListView implements EmptyViewMethodAccessor {

		public InternalExpandableListView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		public void setEmptyView(View emptyView) {
			BounceScrollExpandableListView.this.setEmptyView(emptyView);
		}

		@Override
		public void setEmptyViewInternal(View emptyView) {
			super.setEmptyView(emptyView);
		}
	}

}
