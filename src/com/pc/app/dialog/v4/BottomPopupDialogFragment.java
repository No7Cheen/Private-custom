/**
 * @(#)BottomPopupDialogFragment.java   2014-7-22
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.app.dialog.v4;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pc.app.dialog.BottomPopupDialog;
import com.pc.app.dialog.modle.PcEmDialogType;
import com.pc.utils.StringUtils;
import com.pc.utils.dialog.PcDialogUtil;
import com.privatecustom.publiclibs.R;

/**
  * 底部弹出框
  * 
  * @author chenj
  * @date 2014-7-22
  */

public class BottomPopupDialogFragment extends PcDialogFragmentV4 {

	private String title;
	private int titleResId;
	private int arrayResId;
	private PcEmDialogType[] types;
	private boolean touchOutsideCanceled;
	private View.OnClickListener[] listeners;

	public BottomPopupDialogFragment(int arrayResId, PcEmDialogType[] types, View.OnClickListener[] listeners, final boolean touchOutsideCanceled,
			final OnCancelListener onCancelListener, String title) {
		this.title = title;
		this.types = types;
		this.arrayResId = arrayResId;
		this.touchOutsideCanceled = touchOutsideCanceled;
		this.listeners = listeners;

		setOnCancelListener(onCancelListener);

		setStyle(DialogFragment.STYLE_NORMAL, R.style.Bottom_Dialog_Theme);
	}

	public BottomPopupDialogFragment(int arrayResId, PcEmDialogType[] types, View.OnClickListener[] listeners, final boolean touchOutsideCanceled,
			final OnCancelListener onCancelListener, int titleResId) {
		this.titleResId = titleResId;
		this.types = types;
		this.arrayResId = arrayResId;
		this.touchOutsideCanceled = touchOutsideCanceled;
		this.listeners = listeners;

		setOnCancelListener(onCancelListener);

		setStyle(DialogFragment.STYLE_NORMAL, R.style.Bottom_Dialog_Theme);
	}

	/**
	 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		try {
			if (StringUtils.isNull(title) && titleResId != 0) {
				title = getString(titleResId);
			}
		} catch (Exception e) {
			title = "";
		}
		Dialog d = new BottomPopupDialog(getActivity(), arrayResId, types, listeners, title);
		if (null != d) {
			// d.setOnCancelListener(onCancelListener);
			d.setCanceledOnTouchOutside(touchOutsideCanceled);
		}

		return d;
	}

	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/**
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();

		PcDialogUtil.updatewWindowParamsWH4PupfromBottom(getActivity(), getDialog());
	}

	/**
	 * @see com.pc.app.dialog.v4.PcDialogFragmentV4#onCancel(android.content.DialogInterface)
	 */
	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
	}

}
