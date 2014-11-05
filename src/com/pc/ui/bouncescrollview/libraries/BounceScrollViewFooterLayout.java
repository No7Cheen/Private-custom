/**
 * @(#)OverScrollFooterLayout.java 2013-5-27 Copyright 2013 it.kedacom.com, Inc.
 *                                 All rights reserved.
 */

package com.pc.ui.bouncescrollview.libraries;

import android.content.Context;
import android.content.res.TypedArray;
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

public class BounceScrollViewFooterLayout extends HeaderFooterBase {

	protected final ImageView mImageSign;

	/**
	 * @param context
	 * @param attrs
	 */
	public BounceScrollViewFooterLayout(Context context, TypedArray attrs) {
		super(context, attrs);

		removeAllViews();
		LayoutInflater.from(context).inflate(R.layout.overscroll_footer_layout_vertical, this);

		mRootLayout = (FrameLayout) findViewById(R.id.fl_inner);
		mImageSign = (ImageView) mRootLayout.findViewById(R.id.overscroll_sign_img);

		FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mRootLayout.getLayoutParams();
		lp.gravity = Gravity.BOTTOM;

		if (attrs.hasValue(R.styleable.OverScroll_osHeaderBackground)) {
			Drawable background = attrs.getDrawable(R.styleable.OverScroll_osHeaderBackground);
			if (null != background) {
				setBackgroundDrawable(background);
			}
		}

		// Try and get defined drawable from Attrs
		if (attrs.hasValue(R.styleable.OverScroll_osDrawable)) {
			Drawable imageDrawable = attrs.getDrawable(R.styleable.OverScroll_osDrawable);
			if (imageDrawable != null) {
				mImageSign.setImageDrawable(imageDrawable);
			}
		}
	}

	public void visibleAllViews() {
		if (null != mImageSign) {
			if (View.VISIBLE != mImageSign.getVisibility()) {
				mImageSign.setVisibility(View.VISIBLE);
			}
		}

		if (null != mRootLayout) {
			if (View.VISIBLE != mRootLayout.getVisibility()) {
				mRootLayout.setVisibility(View.VISIBLE);
			}
		}
	}

	public void hideAllViews() {
		if (null == mImageSign) {
			return;
		}

		if (View.VISIBLE == mImageSign.getVisibility() || View.GONE == mImageSign.getVisibility()) {
			mImageSign.setVisibility(View.INVISIBLE);
		}
	}

	public void hideHeaderImage() {
		if (null == mImageSign) {
			return;
		}

		if (View.VISIBLE == mImageSign.getVisibility() || View.GONE == mImageSign.getVisibility()) {
			mImageSign.setVisibility(View.INVISIBLE);
		}
	}

}
