/**
 * @(#)BottomPopupDialog.java   2014-7-22
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import com.pc.utils.android.sys.TerminalUtils;
import com.privatecustom.publiclibs.R;

/**
  * 
  * @author chenj
  * @date 2014-7-22
  */

public class BottomPopupDialogSingle extends Dialog {

	/**
	 * @param context
	 * @param cancelable
	 * @param cancelListener
	 */
	private BottomPopupDialogSingle(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	/**
	 * @param context
	 */
	private BottomPopupDialogSingle(Context context) {
		super(context);
	}

	private int layoutResId;

	public BottomPopupDialogSingle(Context context, int layoutResId) {
		super(context, R.style.Bottom_Dialog_Theme);

		this.layoutResId = layoutResId;
	}

	/**
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(layoutResId);

		Window window = getWindow();
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		int[] wh = TerminalUtils.terminalWH(getContext());
		params.x = 0;
		params.y = wh[1];
		window.setAttributes(params);
		getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		setCancelable(true);
		setCanceledOnTouchOutside(true);
	}

}
