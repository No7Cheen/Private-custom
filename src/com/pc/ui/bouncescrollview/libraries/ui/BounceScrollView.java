package com.pc.ui.bouncescrollview.libraries.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.pc.ui.bouncescrollview.libraries.BounceScrollViewBase;
import com.privatecustom.publiclibs.R;

public class BounceScrollView extends BounceScrollViewBase<ScrollView> {

	public BounceScrollView(Context context) {
		super(context);
	}

	public BounceScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected ScrollView createBounceScrollableView(Context context, AttributeSet attrs) {
		ScrollView scrollView = new ScrollView(context, attrs);

		scrollView.setId(R.id.scrollview);

		return scrollView;
	}

	@Override
	protected boolean isReadyForPullTop() {
		if (null == mBounceScrollableView) {
			return false;
		}

		return mBounceScrollableView.getScrollY() == 0;
	}

	@Override
	protected boolean isReadyForPullBottom() {
		if (null == mBounceScrollableView) {
			return false;
		}

		View scrollViewChild = mBounceScrollableView.getChildAt(0);
		if (null != scrollViewChild) {
			return mBounceScrollableView.getScrollY() >= (scrollViewChild.getHeight() - getHeight());
		}

		return false;
	}

}
