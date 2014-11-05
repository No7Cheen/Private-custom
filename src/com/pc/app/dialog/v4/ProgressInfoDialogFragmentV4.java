/**
 * @(#)ProgressInfoDialogFragmentV4.java   2014-7-22
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
import android.widget.TextView;

import com.pc.utils.StringUtils;
import com.privatecustom.publiclibs.R;

/**
  * 
  * @author chenj
  * @date 2014-7-22
  */

public class ProgressInfoDialogFragmentV4 extends PcDialogFragmentV4 {

	private boolean large;
	private boolean cancelable;
	private boolean progressbar;
	private String infoTxt;
	private int infoTxtResId;

	// private OnCancelListener cancelListener;

	/**
	 */
	public ProgressInfoDialogFragmentV4(boolean _large, boolean _progressbar, boolean _cancelable, String _infoTxt, OnCancelListener _cancelListener) {
		super();

		this.large = _large;
		this.infoTxt = _infoTxt;
		this.cancelable = _cancelable;
		this.progressbar = _progressbar;

		setOnCancelListener(_cancelListener);

		setStyle(DialogFragment.STYLE_NORMAL, R.style.Loading_Dialog_Theme);
	}

	/**
	 */
	public ProgressInfoDialogFragmentV4(boolean _large, boolean _progressbar, boolean _cancelable, int _infoTxtResId, OnCancelListener _cancelListener) {
		super();

		this.large = _large;
		this.infoTxtResId = _infoTxtResId;
		this.cancelable = _cancelable;
		this.progressbar = _progressbar;

		setOnCancelListener(_cancelListener);

		setStyle(DialogFragment.STYLE_NORMAL, R.style.Loading_Dialog_Theme);
	}

	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (large) {
			return inflater.inflate(R.layout.progress_dialog_txt_large, container, false);
		} else {
			return inflater.inflate(R.layout.progress_dialog_txt, container, false);
		}
	}

	/**
	 * @see android.support.v4.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		TextView messageTextView = (TextView) view.findViewById(R.id.loading_info_txt);
		if (messageTextView != null) {
			String txt = "";
			if (StringUtils.isNull(infoTxt)) {
				if (infoTxtResId > 0) txt = getString(infoTxtResId);
			} else {
				txt = infoTxt;
			}

			if (StringUtils.isNull(txt)) {
				messageTextView.setVisibility(View.GONE);
			} else {
				messageTextView.setText(txt);
				messageTextView.setVisibility(View.VISIBLE);
			}
		}

		if (!progressbar) {
			view.findViewById(R.id.loading_progressbar).setVisibility(View.GONE);
		}
	}

	/**
	 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog d = super.onCreateDialog(savedInstanceState);

		d.setCanceledOnTouchOutside(false);
		d.setCancelable(cancelable);

		// if (null != cancelListener) {
		// if (cancelable && cancelListener != null) {
		// d.setOnCancelListener(cancelListener);
		// }
		// }

		return d;
	}

	@Override
	public void onResume() {
		super.onResume();

		setCancelable(cancelable);
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		if (!cancelable) {
			return;
		}

		super.onCancel(dialog);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

}
