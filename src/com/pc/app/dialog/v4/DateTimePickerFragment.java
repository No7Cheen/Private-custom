/**
 * @(#)DatePickerFragment.java 2014-3-17 Copyright 2014 it.kedacom.com, Inc. All
 *                             rights reserved.
 */

package com.pc.app.dialog.v4;

import java.util.Calendar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.pc.app.dialog.DateTimePickerDialog;
import com.pc.app.dialog.DateTimePickerDialog.OnClickDoneListener;
import com.pc.app.dialog.DateTimePickerDialog.OnDateTimeSetListener;
import com.pc.app.dialog.IOnDatetimePickerSet;

/**
 * @author chenjian
 * @date 2014-3-17
 */

public class DateTimePickerFragment extends PcDialogFragmentV4 implements OnDateTimeSetListener {

	private Calendar cal;
	private IOnDatetimePickerSet mOnDatetimePickerSet;
	private OnClickDoneListener mOnClickDoneListener;

	public DateTimePickerFragment(Calendar _Calendar, IOnDatetimePickerSet onDatetimePickerSet, OnClickDoneListener onClickDoneListener) {
		if (null == _Calendar) {
			cal = Calendar.getInstance();
		} else {
			cal = _Calendar;
		}

		mOnDatetimePickerSet = onDatetimePickerSet;
		mOnClickDoneListener = onClickDoneListener;
	}

	public Calendar getPreCalendar() {
		if (getDialog() == null) return null;

		return ((DateTimePickerDialog) getDialog()).getInitCalendar();
	}

	public boolean isShowing() {
		if (getDialog() == null) return false;

		try {
			return ((DateTimePickerDialog) getDialog()).isShowing();
		} catch (Exception e) {
		}

		return false;
	}

	public void initDatePicker(Calendar cal) {
		if (null == cal) return;
		if (getDialog() == null) return;

		((DateTimePickerDialog) getDialog()).initDatePicker(cal);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);

		return new DateTimePickerDialog(getActivity(), year, month, day, hourOfDay, minute, this, mOnClickDoneListener);
	}

	@Override
	public void show(FragmentManager manager, String tag) {
		super.show(manager, tag);
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute) {
		if (null == mOnDatetimePickerSet) return;

		mOnDatetimePickerSet.onDatetimeSet(year, monthOfYear, dayOfMonth, hourOfDay, minute);

		Calendar currCal = ((DateTimePickerDialog) getDialog()).getCurrCalendar();
		if (null != currCal) {
			mOnDatetimePickerSet.onDatetimeSet(currCal);
			cal.setTime(currCal.getTime());
		} else {
			cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
			cal.set(Calendar.MINUTE, minute);

			mOnDatetimePickerSet.onDatetimeSet(currCal);
		}
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int month, int day) {
		if (null == mOnDatetimePickerSet) return;

		mOnDatetimePickerSet.onDateChanged(view, year, month, day);
	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		if (null == mOnDatetimePickerSet) return;

		mOnDatetimePickerSet.onTimeChanged(view, hourOfDay, minute);
	}

}
