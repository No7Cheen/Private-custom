/**
 * @(#)OverScrollHeaderLayout.java 2013-5-27 Copyright 2013 it.kedacom.com, Inc.
 *                                 All rights reserved.
 */

package com.pc.ui.bouncescrollview.libraries;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.privatecustom.publiclibs.R;

/**
 * @author chenjian
 * @date 2013-5-27
 */

public class BounceScrollViewHeaderLayout extends HeaderFooterBase {

	/**
	 * @param context
	 * @param attrs
	 */
	public BounceScrollViewHeaderLayout(Context context, TypedArray attrs) {
		super(context, attrs);

		removeAllViews();
		LayoutInflater.from(context).inflate(R.layout.overscroll_header_layout_vertical, this);

		mRootLayout = findViewById(R.id.fl_inner);
		ImageView mImageSign = (ImageView) mRootLayout.findViewById(R.id.overscroll_sign_img);

		FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mRootLayout.getLayoutParams();
		lp.gravity = Gravity.BOTTOM;

		if (attrs != null && attrs.hasValue(R.styleable.OverScroll_osHeaderBackground)) {
			Drawable background = attrs.getDrawable(R.styleable.OverScroll_osHeaderBackground);
			if (null != background) {
				setBackgroundDrawable(background);
			}
			setBackgroundColor(Color.RED);
		}

		// Try and get defined drawable from Attrs
		if (attrs != null && attrs.hasValue(R.styleable.OverScroll_osDrawable)) {
			Drawable imageDrawable = attrs.getDrawable(R.styleable.OverScroll_osDrawable);
			if (imageDrawable != null) {
				mImageSign.setImageDrawable(imageDrawable);
				mImageSign.setVisibility(View.VISIBLE);
			}
		}
	}

	public void visibleAllViews() {
		ImageView imageSign = (ImageView) mRootLayout.findViewById(R.id.overscroll_sign_img);
		if (null == imageSign) {
			return;
		}

		if (View.VISIBLE != imageSign.getVisibility()) {
			imageSign.setVisibility(View.VISIBLE);
		}

		if (View.VISIBLE != mRootLayout.getVisibility()) {
			mRootLayout.setVisibility(View.VISIBLE);
		}
	}

	public void hideAllViews() {
		ImageView imageSign = (ImageView) mRootLayout.findViewById(R.id.overscroll_sign_img);
		if (null == imageSign) {
			return;
		}

		if (View.VISIBLE == imageSign.getVisibility() || View.GONE == imageSign.getVisibility()) {
			imageSign.setVisibility(View.INVISIBLE);
		}
	}

}
