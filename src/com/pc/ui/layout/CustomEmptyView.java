/**
 * @(#)TTEmptyView.java 2013-7-19 Copyright 2013 it.kedacom.com, Inc. All rights
 *                      reserved.
 */

package com.pc.ui.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.privatecustom.publiclibs.R;

/**
 * 空信息提示
 * @author chenjian
 * @date 2013-7-19
 */

public class CustomEmptyView extends LinearLayout {

	public CustomEmptyView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomEmptyView(Context context) {
		super(context);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		init();
	}

	private ImageView mEmptyImg;
	private TextView mEmptyText;

	private void init() {
		mEmptyImg = (ImageView) findViewById(R.id.empty_img);
		mEmptyText = (TextView) findViewById(R.id.empty_text);
	}

	/**
	 * 设置空信息提示图标
	 * @param resId
	 */
	public void setEmptyDrawable(int resId) {
		if (mEmptyImg == null) {
			return;
		}

		mEmptyImg.setImageResource(resId);
	}

	/**
	 * 设置空信息提示文字
	 * @param resid
	 */
	public void setEmptyText(int resid) {
		if (mEmptyText == null) {
			return;
		}

		mEmptyText.setText(resid);
	}

	/**
	 * 显示Empty View
	 */
	public void showEmptyView() {
		if (mEmptyImg != null && mEmptyImg.getVisibility() != View.VISIBLE) {
			mEmptyImg.setVisibility(View.VISIBLE);
		}

		if (mEmptyText != null && mEmptyText.getVisibility() != View.VISIBLE) {
			mEmptyText.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 隐藏Empty View
	 */
	public void hideEmptyView() {
		if (mEmptyImg != null) {
			mEmptyImg.setVisibility(View.INVISIBLE);
		}

		if (mEmptyText != null) {
			mEmptyText.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 去掉Empty View
	 */
	public void goneEmptyView() {
		if (mEmptyImg != null) {
			mEmptyImg.setVisibility(View.GONE);
		}

		if (mEmptyText != null) {
			mEmptyText.setVisibility(View.GONE);
		}
	}

}
