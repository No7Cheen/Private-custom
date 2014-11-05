/**
 * @(#)PromptDialogFragmentV4.java   2014-7-22
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.app.dialog.v4;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pc.utils.StringUtils;
import com.privatecustom.publiclibs.R;

/**
  * 可自动消隐的提示框
  * 
  * @author chenj
  * @date 2014-7-22
  */

public class PromptDialogFragmentV4 extends PcDialogFragmentV4 {

	private String message;
	private final long durationMillis;

	// private final OnCancelListener listener;

	/**
	 * onAttach-->onCreateDialog-->onCreateView-->onViewCreated-->onResume
	 */
	public PromptDialogFragmentV4(String message, final long durationMillis, final OnCancelListener listener) {
		super();

		this.message = message;
		this.durationMillis = durationMillis;

		setOnCancelListener(listener);

		setStyle(DialogFragment.STYLE_NORMAL, R.style.Loading_Dialog_Theme);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void show(final FragmentManager manager, final String tag) {
		if (getActivity() == null || getActivity().isFinishing() || getActivity().isDestroyed()) {
			return;
		}

		super.show(manager, tag);
	}

	@Override
	public int show(final FragmentTransaction transaction, final String tag) {

		return super.show(transaction, tag);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		return inflater.inflate(R.layout.progress_dialog_txt, container, false);
	}

	/**
	 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		final Dialog dialog = super.onCreateDialog(savedInstanceState);

		if (dialog != null) {
			// if (null != listener) {
			// dialog.setOnCancelListener(new OnCancelListener() {
			//
			// @Override
			// public void onCancel(DialogInterface dialog) {
			// listener.onCancel(dialog);
			// mHideHandler.removeCallbacks(mHideRunnable);
			// }
			// });
			// }
			dialog.setCanceledOnTouchOutside(true);
			dialog.setCancelable(true);
		}

		return dialog;
	}

	@Override
	public void onResume() {
		super.onResume();

		if (durationMillis > 0) {
			delayedHide(durationMillis);
		}
	}

	@Override
	public void onPause() {

		super.onPause();
	}

	@Override
	public void onStop() {
		mHideHandler.removeCallbacks(mHideRunnable);

		super.onStop();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		view.findViewById(R.id.loading_progressbar).setVisibility(View.GONE);

		TextView messageTextView = (TextView) view.findViewById(R.id.loading_info_txt);
		if (messageTextView != null) {
			if (!StringUtils.isNull(message)) {
				messageTextView.setText(message);
				messageTextView.setVisibility(View.VISIBLE);
			} else {
				messageTextView.setVisibility(View.GONE);
			}
		}
	}

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {

		@Override
		public void run() {
			Dialog d = getDialog();
			if (null == d || !d.isShowing()) {
				return;
			}

			d.dismiss();
		}
	};

	private void delayedHide(long delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}

	/**
	 * @see com.pc.app.dialog.v4.PcDialogFragmentV4#onCancel(android.content.DialogInterface)
	 */
	@Override
	public void onCancel(DialogInterface dialog) {
		mHideHandler.removeCallbacks(mHideRunnable);

		super.onCancel(dialog);
	}

}
