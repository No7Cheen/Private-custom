/**
 * @(#)TopPopdownDialogFragment.java   2014-7-22
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.app.dialog.v4;

import android.app.Dialog;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import com.pc.app.dialog.TopPopdownDialog;
import com.pc.utils.android.sys.TerminalUtils;

/**
  * 顶部弹出框
  * 
  * @author chenj
  * @date 2014-7-22
  */

public class TopPopdownDialogFragment extends PcDialogFragmentV4 {

	private View mView;
	private int layoutResId;
	private boolean cancelableOnTouchOutside;
	private int wh[] = new int[2];

	public TopPopdownDialogFragment(int layoutResId, boolean cancelableOnTouchOutside, OnCancelListener cancelLister, int width, int height) {
		this.layoutResId = layoutResId;
		this.cancelableOnTouchOutside = cancelableOnTouchOutside;

		wh[0] = width;
		wh[1] = height;

		setOnCancelListener(cancelLister);
	}

	public TopPopdownDialogFragment(View view, boolean cancelableOnTouchOutside, OnCancelListener cancelLister, int width, int height) {
		this.mView = view;
		this.cancelableOnTouchOutside = cancelableOnTouchOutside;

		wh[0] = width;
		wh[1] = height;

		setOnCancelListener(cancelLister);
	}

	public TopPopdownDialogFragment(int layoutResId, boolean cancelableOnTouchOutside, OnCancelListener cancelLister) {
		this.layoutResId = layoutResId;
		this.cancelableOnTouchOutside = cancelableOnTouchOutside;

		wh[0] = -2;
		wh[1] = -2;

		setOnCancelListener(cancelLister);
	}

	public TopPopdownDialogFragment(View view, boolean cancelableOnTouchOutside, OnCancelListener cancelLister) {
		this.mView = view;
		this.cancelableOnTouchOutside = cancelableOnTouchOutside;

		wh[0] = -2;
		wh[1] = -2;

		setOnCancelListener(cancelLister);
	}

	/**
	 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog d;
		if (null != mView) {
			d = new TopPopdownDialog(getActivity(), mView);
		} else {
			d = new TopPopdownDialog(getActivity(), layoutResId);
		}

		if (null != d) {
			d.setCanceledOnTouchOutside(cancelableOnTouchOutside);
		}

		return d;
	}

	@Override
	public void onResume() {
		super.onResume();

		Window window = getDialog().getWindow();
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		int[] wh = TerminalUtils.terminalWH(getActivity());
		params.x = 0;
		params.y = -wh[1];
		window.setAttributes(params);
		int width = this.wh[0], height = this.wh[1];
		if (width <= 0) {
			width = LayoutParams.MATCH_PARENT;
		}
		if (height <= 0) {
			height = LayoutParams.WRAP_CONTENT;
		}
		getDialog().getWindow().setLayout(width, height);
	}
}
