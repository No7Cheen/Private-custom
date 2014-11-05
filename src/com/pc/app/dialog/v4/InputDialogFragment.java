/**
 * @(#)ConveneVConfDialog.java   2014-8-26
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.app.dialog.v4;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.pc.app.dialog.OnOkListener;
import com.pc.text.PcEditTextLengthFilter;
import com.pc.utils.dialog.PcDialogUtil;
import com.pc.utils.ime.ImeUtil;
import com.privatecustom.publiclibs.R;

/**
  * 输入弹出框
  * 
  * @author chenj
  * @date 2014-8-26
  */

public class InputDialogFragment extends PcDialogFragmentV4 {

	private String hint;

	// 输入框输入的最大文字量
	private int inputMax = 0;
	private int inputType;

	// 邀请会议事件处理
	private OnOkListener mOnOkListener;

	public InputDialogFragment(final OnOkListener onOkListener, String hint) {
		this(onOkListener, hint, 0, -1);
	}

	public InputDialogFragment(final OnOkListener onOkListener, String hint, int inputMaxCount, int inputType) {
		this.hint = hint;
		this.inputType = inputType;
		this.inputMax = inputMaxCount;
		this.mOnOkListener = onOkListener;

		setStyle(DialogFragment.STYLE_NORMAL, R.style.My_Dialog_Theme);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog d = super.onCreateDialog(savedInstanceState);
		if (null != d) {
			PcDialogUtil.setAttributes4PupfromBottom(getActivity(), d);
		}

		return d;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.alert_dialog_input_view, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		final TextView cancelView = (TextView) view.findViewById(R.id.cancel);
		final TextView doneView = (TextView) view.findViewById(R.id.ok);
		final EditText editView = (EditText) view.findViewById(R.id.editView);

		if (!TextUtils.isEmpty(hint)) {
			editView.setHint(hint);
		}
		if (TextUtils.isEmpty(editView.getText())) {
			doneView.setEnabled(false);
		} else {
			doneView.setEnabled(true);
		}

		// 输入框输入的最大文字量
		if (inputMax > 0) {
			InputFilter[] filters = {
				new PcEditTextLengthFilter(inputMax)
			};
			editView.setFilters(filters);
		}

		if (inputType >= 0) {
			editView.setInputType(inputType);
		}

		editView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (TextUtils.isEmpty(s)) {
					doneView.setEnabled(false);
				} else {
					doneView.setEnabled(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		doneView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final String text = editView.getText().toString().trim();

				if (mOnOkListener != null) {
					mOnOkListener.onOkClick(getDialog(), text);
				} else {
					if (null != getDialog()) {
						ImeUtil.hideIme(getDialog().getCurrentFocus());
					}
				}
			}
		});

		// 取消
		cancelView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getDialog() != null) {
					ImeUtil.hideIme(getDialog().getCurrentFocus());
				}

				dismissAllowingStateLoss();
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();

		PcDialogUtil.updatewWindowParamsWH4PupfromBottom(getActivity(), getDialog());
	}

}
