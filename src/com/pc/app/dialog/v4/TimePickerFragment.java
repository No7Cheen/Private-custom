/**
 * @(#)DatePickerFragment.java 2014-3-17 Copyright 2014 it.kedacom.com, Inc. All
 *                             rights reserved.
 */

package com.pc.app.dialog.v4;

import java.util.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.TimePicker;

import com.pc.app.dialog.IOnDatePickerSet;

/**
 * @author chenjian
 * @date 2014-3-17
 */

public class TimePickerFragment extends PcDialogFragmentV4 implements OnTimeSetListener {

	private Calendar cal;
	private Calendar preCal;
	private IOnDatePickerSet mIOnDatePickerSet;

	public TimePickerFragment(Calendar _Calendar, IOnDatePickerSet onDatePickerSet) {
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
		int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);

		// Create a new instance of DatePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hourOfDay, minute, true);
	}

	/**
	 * @see android.support.v4.app.DialogFragment#show(android.support.v4.app.FragmentManager,
	 *      java.lang.String)
	 */

	@Override
	public void show(FragmentManager manager, String tag) {
		try {
			preCal.setTime(cal.getTime());
		} catch (Exception e) {
		}

		super.show(manager, tag);
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
		cal.set(Calendar.MINUTE, minute);

		if (mIOnDatePickerSet != null) {
			mIOnDatePickerSet.onDatePickerSet(cal);
		}
	}

}
