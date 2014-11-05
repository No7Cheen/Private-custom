package com.pc.ui.bouncescrollview.libraries.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import com.pc.ui.bouncescrollview.libraries.BounceScrollViewAdapterViewBase;
import com.pc.ui.bouncescrollview.libraries.EmptyViewMethodAccessor;
import com.privatecustom.publiclibs.R;

public class BounceScrollGridView extends BounceScrollViewAdapterViewBase<GridView> {

	public BounceScrollGridView(Context context) {
		super(context);
	}

	public BounceScrollGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected final GridView createBounceScrollableView(Context context, AttributeSet attrs) {
		final GridView gv = new InternalGridView(context, attrs);

		// Use Generated ID (from res/values/ids.xml)
		gv.setId(R.id.gridview);

		return gv;
	}

	@Override
	protected void handleStyledAttributes(TypedArray a) {
		super.handleStyledAttributes(a);
	}

	class InternalGridView extends GridView implements EmptyViewMethodAccessor {

		public InternalGridView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		public void setEmptyView(View emptyView) {
			BounceScrollGridView.this.setEmptyView(emptyView);
		}

		@Override
		public void setEmptyViewInternal(View emptyView) {
			super.setEmptyView(emptyView);
		}
	}

}
