/**
 * @(#)PcDialogFragment.java   2014-9-3
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.app.dialog.v4;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.pc.utils.log.PcLog;

/**
  * 
  * @author chenj
  * @date 2014-9-3
  */

public class PcDialogFragmentV4 extends DialogFragment {

	private boolean isShowing;
	private OnCancelListener onCancelListener;

	public void setOnCancelListener(final OnCancelListener cancelLister) {
		onCancelListener = cancelLister;
	}

	/**
	 * @see android.support.v4.app.DialogFragment#show(android.support.v4.app.FragmentManager, java.lang.String)
	 */
	@Override
	public void show(FragmentManager manager, String tag) {
		super.show(manager, tag);

		isShowing = true;

		if (PcLog.isPrint) {
			Log.i("PcDialogFragmentV4", "show(FragmentManager manager, String tag)");
		}
	}

	/**
	 * @see android.support.v4.app.DialogFragment#show(android.support.v4.app.FragmentTransaction, java.lang.String)
	 */
	@Override
	public int show(FragmentTransaction transaction, String tag) {
		if (PcLog.isPrint) {
			Log.i("PcDialogFragmentV4", "show(FragmentTransaction transaction, String tag)");
		}
		isShowing = true;
		return super.show(transaction, tag);
	}

	/**
	 * @see android.support.v4.app.DialogFragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		isShowing = false;

		if (PcLog.isPrint) {
			Log.i("PcDialogFragmentV4", "onAttach");
		}
	}

	/**
	 * @see android.support.v4.app.DialogFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (PcLog.isPrint) {
			Log.i("PcDialogFragmentV4", "onCreate");
		}
	}

	/**
	 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		if (PcLog.isPrint) {
			Log.i("PcDialogFragmentV4", "onCreateDialog");
		}

		return super.onCreateDialog(savedInstanceState);
	}

	/**
	 * @see android.support.v4.app.DialogFragment#getDialog()
	 */
	@Override
	public Dialog getDialog() {
		return super.getDialog();
	}

	/**
	 * @see android.support.v4.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
	 */

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (PcLog.isPrint) {
			Log.i("PcDialogFragmentV4", "onViewCreated");
		}
	}

	/**
	 * @see android.support.v4.app.DialogFragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);

		if (PcLog.isPrint) {
			Log.i("PcDialogFragmentV4", "onActivityCreated");
		}
	}

	/**
	 * @see android.support.v4.app.DialogFragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();

		if (PcLog.isPrint) {
			Log.i("PcDialogFragmentV4", "onStart");
		}
	}

	/**
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();

		isShowing = true;

		if (PcLog.isPrint) {
			Log.i("PcDialogFragmentV4", "onResume");
		}
	}

	/**
	 * Dialog是否显示
	 *
	 * @return
	 */
	public boolean isShowing() {
		if (!isShowing) {
			if (getDialog() != null && getDialog().isShowing()) {
				isShowing = true;
			}
		}

		return isShowing;
	}

	/**
	 * @see android.support.v4.app.DialogFragment#onCancel(android.content.DialogInterface)
	 */
	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);

		isShowing = false;

		if (PcLog.isPrint) {
			Log.i("PcDialogFragmentV4", "onCancel");
		}

		if (null != onCancelListener) {
			onCancelListener.onCancel(dialog);
		}

		if (null != dialog) {
			dialog.cancel();
		}
	}

	/**
	 * @see android.support.v4.app.DialogFragment#dismiss()
	 */
	@Override
	public void dismiss() {
		try {
			super.dismiss();
		} catch (Exception e) {
		}

		isShowing = false;

		if (PcLog.isPrint) {
			Log.i("PcDialogFragmentV4", "dismiss");
		}
	}

	/**
	 * @see android.support.v4.app.DialogFragment#dismissAllowingStateLoss()
	 */
	@Override
	public void dismissAllowingStateLoss() {
		super.dismissAllowingStateLoss();

		isShowing = false;

		if (PcLog.isPrint) {
			Log.i("PcDialogFragmentV4", "dismissAllowingStateLoss");
		}
	}

	/**
	 * @see android.support.v4.app.DialogFragment#onDismiss(android.content.DialogInterface)
	 */
	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);

		isShowing = false;

		if (PcLog.isPrint) {
			Log.i("PcDialogFragmentV4", "onDismiss");
		}
	}

	/**
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();

		if (PcLog.isPrint) {
			Log.i("PcDialogFragmentV4", "onPause");
		}
	}

	/**
	 * @see android.support.v4.app.DialogFragment#onStop()
	 */
	@Override
	public void onStop() {
		super.onStop();

		if (PcLog.isPrint) {
			Log.i("PcDialogFragmentV4", "onStop");
		}
	}

	/**
	 * @see android.support.v4.app.DialogFragment#onDestroyView()
	 */
	@Override
	public void onDestroyView() {
		super.onDestroyView();

		isShowing = false;

		if (PcLog.isPrint) {
			Log.i("PcDialogFragmentV4", "onDestroyView");
		}
	}

	/**
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		isShowing = false;

		super.onDestroy();

		if (PcLog.isPrint) {
			Log.i("PcDialogFragmentV4", "onDestroy");
		}
	}

	/**
	 * @see android.support.v4.app.DialogFragment#onDetach()
	 */
	@Override
	public void onDetach() {
		super.onDetach();

		isShowing = false;

		if (PcLog.isPrint) {
			Log.i("PcDialogFragmentV4", "onDetach");
		}
	}

}
