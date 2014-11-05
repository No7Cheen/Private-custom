/**
 * @(#)IOnDatetimePickerSet.java 2014-3-21 Copyright 2014 it.kedacom.com, Inc.
 *                               All rights reserved.
 */

package com.pc.app.dialog;

import java.util.Calendar;

import android.widget.DatePicker;
import android.widget.TimePicker;

/**
 * @author chenjian
 * @date 2014-3-21
 */

public interface IOnDatetimePickerSet {

	public void onDatetimeSet(Calendar calendar);

	public void onDatetimeSet(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute);

	public void onDateChanged(DatePicker view, int year, int month, int day);

	public void onTimeChanged(TimePicker view, int hourOfDay, int minute);

}
