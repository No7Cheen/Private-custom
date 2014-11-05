/**
 * @(#)DateTimePickerDialog.java 2014-3-21 Copyright 2014 it.kedacom.com, Inc.
 *                               All rights reserved.
 */

package com.pc.app.dialog;

import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.FrameLayout;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.ViewSwitcher;

import com.privatecustom.publiclibs.R;

/**
 * @author chenjian
 * @date 2014-3-21
 */

public class DateTimePickerDialog extends AlertDialog implements OnDateChangedListener, OnTimeChangedListener {

	private Context mContext;

	private final String YEAR = "year";
	private final String MONTH = "month";
	private final String DAY = "day";
	private final String HOUR = "hour";
	private final String MINUTE = "minute";
	private final String IS_24_HOUR = "is24hour";

	private final Calendar mCalendar;
	private final Calendar mInitCalendar;

	private DatePicker mDatePicker;

	private TimePicker mTimePicker;
	private final OnDateTimeSetListener mDatetimeCallback;

	private final OnClickDoneListener mClickDoneListener;

	private boolean mIs24HourView;

	private boolean mTitleNeedsUpdate = true;

	private boolean currTimePicker;
	private ViewSwitcher viewSwitcher;
	private boolean dismissValid = true;

	private boolean isCancel;

	/**
	 * The callback used to indicate the user is done filling in the date.
	 */
	public interface OnDateTimeSetListener {

		// /**
		// * @param view
		// * @param calendar
		// */
		// void onDateSet(DateTimePickerDialog view, Calendar calendar);

		/**
		 * @param view The view associated with this listener.
		 * @param year The year that was set.
		 * @param monthOfYear The month that was set (0-11) for compatibility
		 *            with {@link java.util.Calendar}.
		 * @param dayOfMonth The day of the month that was set.
		 * @param hourOfDay The hour that was set.
		 * @param minute The minute that was set.
		 */
		void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute);

		public void onDateChanged(DatePicker view, int year, int month, int day);

		public void onTimeChanged(TimePicker view, int hourOfDay, int minute);
	}

	public interface OnClickDoneListener {

		public void onDoneListner(DialogInterface dialog);
	}

	public DateTimePickerDialog(Context context, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute, OnDateTimeSetListener dateTimeCallback,
			OnClickDoneListener doneListener) {
		this(context, 0, year, monthOfYear, dayOfMonth, hourOfDay, minute, true, true, dateTimeCallback, doneListener);
	}

	/**
	 * @param context The context the dialog is to run in.
	 * @param callBack How the parent is notified that the date is set.
	 * @param year The initial year of the dialog.
	 * @param monthOfYear The initial month of the dialog.
	 * @param dayOfMonth The initial day of the dialog.
	 */
	public DateTimePickerDialog(Context context, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute, boolean is24HourView, boolean isTimePicker,
			OnDateTimeSetListener dateTimeCallback, OnClickDoneListener doneListener) {
		this(context, 0, year, monthOfYear, dayOfMonth, hourOfDay, minute, is24HourView, isTimePicker, dateTimeCallback, doneListener);
	}

	/**
	 * @param context The context the dialog is to run in.
	 * @param theme the theme to apply to this dialog
	 * @param dateCallBack How the parent is notified that the date is set.
	 * @param year The initial year of the dialog.
	 * @param monthOfYear The initial month of the dialog.
	 * @param dayOfMonth The initial day of the dialog.
	 */
	public DateTimePickerDialog(Context context, int theme, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute, boolean is24HourView, boolean isTimePicker,
			OnDateTimeSetListener dateTimeCallback, OnClickDoneListener doneListener) {
		super(context, theme);

		mContext = context;
		this.currTimePicker = isTimePicker;
		mDatetimeCallback = dateTimeCallback;
		mClickDoneListener = doneListener;

		mIs24HourView = is24HourView;

		mCalendar = Calendar.getInstance();
		mCalendar.set(Calendar.YEAR, year);
		mCalendar.set(Calendar.MONTH, monthOfYear);
		mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		mCalendar.set(Calendar.MINUTE, minute);

		mInitCalendar = Calendar.getInstance();
		mInitCalendar.setTimeInMillis(mCalendar.getTimeInMillis());

		Context themeContext = getContext();
		CharSequence neutralText = themeContext.getText(R.string.time_picker_dialog_title);
		if (isTimePicker) {
			neutralText = themeContext.getText(R.string.date_picker_dialog_title);
		}
		// done
		setButton(BUTTON_POSITIVE, themeContext.getText(R.string.date_time_done), positiveListener);
		// cancel
		setButton(BUTTON_NEGATIVE, themeContext.getText(R.string.date_time_cancel), negativeListener);
		// time/date picker
		setButton(BUTTON_NEUTRAL, neutralText, viewSwitcherListener);
		setIcon(0);

		viewSwitcher = new ViewSwitcher(themeContext);
		viewSwitcher.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

		LayoutInflater inflater = (LayoutInflater) themeContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View datePickerView = inflater.inflate(R.layout.date_picker_dialog, null);
		mDatePicker = (DatePicker) datePickerView.findViewById(R.id.datePicker);

		View timePickerView = inflater.inflate(R.layout.time_picker_dialog, null);
		mTimePicker = (TimePicker) timePickerView.findViewById(R.id.timePicker);

		if (this.currTimePicker) {
			viewSwitcher.addView(timePickerView);
			viewSwitcher.addView(datePickerView);
		} else {
			viewSwitcher.addView(datePickerView);
			viewSwitcher.addView(timePickerView);
		}

		setView(viewSwitcher);

		mDatePicker.init(year, monthOfYear, dayOfMonth, this);

		// initialize state
		mTimePicker.setIs24HourView(mIs24HourView);
		mTimePicker.setCurrentHour(hourOfDay);
		mTimePicker.setCurrentMinute(minute);
		mTimePicker.setOnTimeChangedListener(this);

		updateTitle(year, monthOfYear, dayOfMonth, mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute());

		setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				isCancel = true;
				dismissValid = true;

				setCurrCalendar(mInitCalendar);
			}
		});
	}

	public Calendar getInitCalendar() {
		return mInitCalendar;
	}

	public void setInitCalendar(Calendar cal) {
		if (null == cal) return;

		mInitCalendar.setTime(cal.getTime());
		updateTitle(mInitCalendar);
	}

	public Calendar getCurrCalendar() {
		return mCalendar;
	}

	private void setCurrCalendar(Calendar cal) {
		if (null == cal) return;

		mCalendar.setTime(cal.getTime());
		mInitCalendar.setTime(cal.getTime());
		updateTitle(mCalendar);
	}

	public void setDismissValid(boolean dismissValid) {
		this.currTimePicker = dismissValid;
	}

	/**
	 * 设置Dialog点击无效
	 * @param invalid 无效
	 */
	public void setDialogInvalid(boolean invalid) {
		if (!invalid) return;

		isCancel = true;
		dismissValid = false;
	}

	private OnClickListener viewSwitcherListener = new OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			isCancel = false;
			dismissValid = false;

			if (currTimePicker) {
				viewSwitcher.showNext();
				getButton(BUTTON_NEUTRAL).setText(R.string.time_picker_dialog_title);
			} else {
				viewSwitcher.showPrevious();
				getButton(BUTTON_NEUTRAL).setText(R.string.date_picker_dialog_title);
			}

			currTimePicker = !currTimePicker;
			updateTitle(mCalendar);
		}
	};

	// cancel
	private OnClickListener negativeListener = new OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			isCancel = true;
			dismissValid = true;

			setCurrCalendar(mInitCalendar);
		}
	};

	// done
	private OnClickListener positiveListener = new OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			isCancel = false;
			dismissValid = true;

			if (mClickDoneListener != null) {
				mClickDoneListener.onDoneListner(dialog);
			}
		}
	};

	public void onDateChanged(DatePicker view, int year, int month, int day) {
		mDatePicker.init(year, month, day, this);
		updateTitle(year, month, day, 0, 0);

		if (mDatetimeCallback != null) {
			mDatetimeCallback.onDateChanged(view, year, month, day);
		}
	}

	/**
	 * init date picker
	 * @param cal
	 */
	public void initDatePicker(Calendar cal) {
		if (null == cal) return;
		if (null == mDatePicker) return;

		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);

		mDatePicker.init(year, month, day, this);

		updateTitle(year, month, day, hourOfDay, minute);
	}

	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		updateTitle(0, 0, 0, hourOfDay, minute);

		if (mDatetimeCallback != null) {
			mDatetimeCallback.onTimeChanged(view, hourOfDay, minute);
		}
	}

	/**
	 * Gets the {@link DatePicker} contained in this dialog.
	 * @return The calendar view.
	 */
	public DatePicker getDatePicker() {
		return mDatePicker;
	}

	/**
	 * Sets the current date.
	 * @param year The date year.
	 * @param monthOfYear The date month.
	 * @param dayOfMonth The date day of month.
	 */
	public void updateDate(int year, int monthOfYear, int dayOfMonth) {
		mDatePicker.updateDate(year, monthOfYear, dayOfMonth);
	}

	public void updateTime(int hourOfDay, int minutOfHour) {
		mTimePicker.setCurrentHour(hourOfDay);
		mTimePicker.setCurrentMinute(minutOfHour);
	}

	private void tryNotifyDatetimeSet() {
		if (null == mDatetimeCallback) return;

		mDatePicker.clearFocus();
		mTimePicker.clearFocus();

		// mDatetimeCallback.onDateSet(mDatePicker, calendar);
		mDatetimeCallback.onDateSet(mDatePicker, mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth(), mTimePicker.getCurrentHour(),
				mTimePicker.getCurrentMinute());
	}

	@Override
	public void show() {
		super.show();
	}

	@Override
	public void dismiss() {
		if (!dismissValid) {
			dismissValid = true;
			return;
		}

		super.dismiss();
	}

	@Override
	public void cancel() {
		isCancel = true;
		super.cancel();
	}

	@Override
	protected void onStop() {
		if (!isCancel) {
			tryNotifyDatetimeSet();
		}

		super.onStop();
	}

	private void updateTitle(Calendar mCalendar) {
		if (null == mCalendar) return;
		updateTitle(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), mCalendar.get(Calendar.HOUR_OF_DAY),
				mCalendar.get(Calendar.MINUTE));
	}

	private void updateTitle(int year, int month, int day, int hourOfDay, int minute) {
		if (currTimePicker) {
			mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
			mCalendar.set(Calendar.MINUTE, minute);
			String title = DateUtils.formatDateTime(mContext, mCalendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME);
			setTitle(title);
			return;
		}

		if (!mDatePicker.getCalendarViewShown()) {
			mCalendar.set(Calendar.YEAR, year);
			mCalendar.set(Calendar.MONTH, month);
			mCalendar.set(Calendar.DAY_OF_MONTH, day);
			String title = DateUtils.formatDateTime(mContext, mCalendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_YEAR
					| DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_ABBREV_WEEKDAY);
			setTitle(title);
			mTitleNeedsUpdate = true;
		} else {
			if (mTitleNeedsUpdate) {
				mTitleNeedsUpdate = false;
				setTitle(R.string.date_picker_dialog_title);
			}
		}
	}

	@Override
	public Bundle onSaveInstanceState() {
		Bundle state = super.onSaveInstanceState();
		state.putInt(YEAR, mDatePicker.getYear());
		state.putInt(MONTH, mDatePicker.getMonth());
		state.putInt(DAY, mDatePicker.getDayOfMonth());

		state.putInt(HOUR, mTimePicker.getCurrentHour());
		state.putInt(MINUTE, mTimePicker.getCurrentMinute());
		state.putBoolean(IS_24_HOUR, mTimePicker.is24HourView());

		return state;
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		int year = savedInstanceState.getInt(YEAR);
		int month = savedInstanceState.getInt(MONTH);
		int day = savedInstanceState.getInt(DAY);
		mDatePicker.init(year, month, day, this);

		int hour = savedInstanceState.getInt(HOUR);
		int minute = savedInstanceState.getInt(MINUTE);
		mTimePicker.setIs24HourView(savedInstanceState.getBoolean(IS_24_HOUR));
		mTimePicker.setCurrentHour(hour);
		mTimePicker.setCurrentMinute(minute);
	}
}
