/**
 * @(#)DatePickerFragment.java 2014-3-17 Copyright 2014 it.kedacom.com, Inc. All
 *                             rights reserved.
 */

package com.pc.app.dialog.v4;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.DatePicker;

import com.pc.app.dialog.IOnDatePickerSet;

/**
 * @author chenjian
 * @date 2014-3-17
 */

public class DatePickerFragment extends PcDialogFragmentV4 implements OnDateSetListener {

	private Calendar cal;
	private Calendar preCal;
	private IOnDatePickerSet mIOnDatePickerSet;

	public DatePickerFragment(Calendar _Calendar, IOnDatePickerSet onDatePickerSet) {
		if (null == _Calendar) {
			cal = Calendar.getInstance();
		} else {
			cal = _Calendar;
		}
		preCal = Calendar.getInstance();

		mIOnDatePickerSet = onDatePickerSet;
	}

	public Calendar getPreCalendar() {
		return preCal;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	@Override
	public void show(FragmentManager manager, String tag) {
		try {
			preCal.setTime(cal.getTime());
		} catch (Exception e) {
		}

		super.show(manager, tag);
	}

	/**
	 * @see android.app.DatePickerDialog.OnDateSetListener#onDateSet(android.widget.DatePicker,
	 *      int, int, int)
	 */
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, monthOfYear);
		cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

		if (mIOnDatePickerSet != null) {
			mIOnDatePickerSet.onDatePickerSet(cal);
		}
	}

}
