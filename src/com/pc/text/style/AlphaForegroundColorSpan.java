/**
 * @(#)AlphaForegroundColorSpan.java   2014-8-29
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.text.style;

import android.graphics.Color;
import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;

/**
  * Alpha ForegroundColorSpan
  * 
  * @author chenj
  * @date 2014-8-29
  */

public class AlphaForegroundColorSpan extends ForegroundColorSpan {

	private float mAlpha;

	public AlphaForegroundColorSpan(int color) {
		super(color);
	}

	public AlphaForegroundColorSpan(Parcel src) {
		super(src);
		mAlpha = src.readFloat();
	}

	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeFloat(mAlpha);
	}

	@Override
	public void updateDrawState(TextPaint ds) {
		ds.setColor(getAlphaColor());
	}

	public void setAlpha(float alpha) {
		mAlpha = alpha;
	}

	public float getAlpha() {
		return mAlpha;
	}

	private int getAlphaColor() {
		int foregroundColor = getForegroundColor();
		return Color.argb((int) (mAlpha * 255), Color.red(foregroundColor), Color.green(foregroundColor), Color.blue(foregroundColor));
	}
}
