package com.pc.ui.bouncescrollview.libraries;

import android.content.Context;
import android.content.res.TypedArray;

public abstract class FooterLayout extends HeaderFooterBase {

	public FooterLayout(Context context) {
		super(context);
	}

	public FooterLayout(Context context, TypedArray attrs) {
		super(context);
	}

	public abstract void visibleAllViews();

	public abstract void hideAllViews();

}
