/**
 * @(#)TopPopdownDialog.java   2014-7-22
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import com.pc.utils.android.sys.TerminalUtils;
import com.privatecustom.publiclibs.R;

/**
  * 顶部弹出框
  * 
  * @author chenj
  * @date 2014-7-22
  */

public class TopPopdownDialog extends Dialog {

	/**
	 * @param context
	 * @param cancelable
	 * @param cancelListener
	 */
	private TopPopdownDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	/**
	 * @param context
	 */
	private TopPopdownDialog(Context context) {
		super(context, R.style.Top_Dialog_Theme);
	}

	private int layoutResId;
	private View view;

	public TopPopdownDialog(Context context, int layoutResId) {
		this(context);

		this.layoutResId = layoutResId;
	}

	public TopPopdownDialog(Context context, View view) {
		this(context, R.style.Top_Dialog_Theme);

		this.view = view;
	}

	/**
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (null != view) {
			setContentView(view);
		} else {
			setContentView(layoutResId);
		}

		Window window = getWindow();
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		int[] wh = TerminalUtils.terminalWH(getContext());
		params.x = 0;
		params.y = -wh[1];
		window.setAttributes(params);
		// window.setGravity(Gravity.TOP);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}

}
