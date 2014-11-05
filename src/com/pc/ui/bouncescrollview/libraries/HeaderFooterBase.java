/**
 * @(#)IHeaderFooter.java 2013-5-27 Copyright 2013 it.kedacom.com, Inc. All
 *                        rights reserved.
 */

package com.pc.ui.bouncescrollview.libraries;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 页眉/页脚
 * @author chenjian
 * @date 2013-5-27
 */

public abstract class HeaderFooterBase extends FrameLayout {

	protected View mRootLayout; // 页眉/页脚Root View

	public HeaderFooterBase(Context context) {
		super(context);
	}

	public HeaderFooterBase(Context context, TypedArray attrs) {
		super(context);
	}

	public final void setHeight(int height) {
		ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) getLayoutParams();
		lp.height = height;
		requestLayout();
	}

	public final void setWidth(int width) {
		ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) getLayoutParams();
		lp.width = width;
		requestLayout();
	}

	public final int getContentSize() {
		if (null == mRootLayout) {
			return 0;
		}

		return mRootLayout.getHeight();
	}

	public abstract void visibleAllViews();

	public abstract void hideAllViews();
}
