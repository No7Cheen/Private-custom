/*
 * Copyright 2012 by Handsomedylan 使用AlertDialog的思路进行了封装喵,同时以合适的字体自适应各种屏幕。
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package com.pc.ui.view.wheelview;

import java.util.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.pc.ui.widget.WheelView.NumericWheelAdapter;
import com.pc.ui.widget.WheelView.OnWheelChangedListener;
import com.pc.ui.widget.WheelView.WheelView;
import com.privatecustom.publiclibs.R;

public class SingleTimeWheelBuilderDialog extends Dialog {

	private WheelView mWheelViewHours; // hours view
	private WheelView mWheelViewMins; // mins view

	private final Context mContext;
	private CharSequence positiveText;
	private CharSequence negativeText;
	private Calendar mCalendar;
	private View.OnClickListener positiveClickListener;
	private View.OnClickListener negativeClickListener;

	private SingleTimeWheelBuilderDialog(Context context) {
		super(context);
		mContext = context;
		mCalendar = Calendar.getInstance();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.timeselector_single_layout);

		findViews();
		adjustView();
		setListener();
		setDate(mCalendar);

	}

	private void findViews() {
		mWheelViewHours = (WheelView) findViewById(R.id.hourWheelView);
		mWheelViewMins = (WheelView) findViewById(R.id.minsWheelView);
	}

	private void adjustView() {
		// 根据屏幕密度来指定选择器字体的大小
		final float scale = mContext.getResources().getDisplayMetrics().density;
		int textSize = (int) (28 * scale + 0.5f);
		mWheelViewMins.TEXT_SIZE = textSize;
		mWheelViewHours.TEXT_SIZE = textSize;
	}

	/**
	 * add listeners
	 * @Description
	 */
	private void setListener() {
		addChangingListener(mWheelViewHours, "hour");
		addChangingListener(mWheelViewMins, "min");
	}

	/**
	 * Adds changing listener for wheel that updates the wheel label
	 * @param wheel the wheel
	 * @param label the wheel label
	 */
	private void addChangingListener(final WheelView wheel, final String label) {
		wheel.addChangingListener(new OnWheelChangedListener() {

			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// wheel.setLabel(newValue != 1 ? label + "s" : label);
			}
		});
	}

	private final View.OnClickListener dismissListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			dismiss();
		}
	};

	public void setDate(Calendar calendar) {
		mWheelViewHours.setAdapter(new NumericWheelAdapter(0, 23));
		mWheelViewHours.setLabel("hour");
		mWheelViewHours.setCyclic(true);

		mWheelViewMins.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
		mWheelViewMins.setLabel("min");
		mWheelViewMins.setCyclic(true);

		if (calendar == null) {
			calendar = Calendar.getInstance();
			mCalendar = calendar;
		}

		// set current time
		int currHours = calendar.get(Calendar.HOUR_OF_DAY);
		int currMinutes = calendar.get(Calendar.MINUTE);
		mWheelViewHours.setCurrentItem(currHours);// 初始化时显示hour
		mWheelViewMins.setCurrentItem(currMinutes);// 初始化时显示min
	}

	public static class Builder {

		private final DatePickParams P;

		public Builder(Context context) {
			P = new DatePickParams(context);
		}

		public Builder setTitle(CharSequence title) {
			P.mTitle = title;

			return this;
		}

		public Builder setIcon(int iconId) {
			P.mIconId = iconId;

			return this;
		}

		public Builder setPositiveButton(CharSequence text, final View.OnClickListener listener) {
			P.mPositiveButtonText = text;
			P.mPositiveButtonListener = listener;

			return this;
		}

		public Builder setNegativeButton(CharSequence text, final View.OnClickListener listener) {
			P.mNegativeButtonText = text;
			P.mNegativeButtonListener = listener;

			return this;
		}

		public SingleTimeWheelBuilderDialog create() {
			final SingleTimeWheelBuilderDialog dialog = new SingleTimeWheelBuilderDialog(P.mContext);
			P.apply(dialog);

			return dialog;
		}
	}

	public static class DatePickParams {

		public int mIconId;
		public View.OnClickListener mPositiveButtonListener;
		public CharSequence mPositiveButtonText;
		public CharSequence mTitle;
		public final Context mContext;
		public Calendar calendar;
		private CharSequence mNegativeButtonText;
		private View.OnClickListener mNegativeButtonListener;

		public DatePickParams(Context context) {
			mContext = context;
			calendar = Calendar.getInstance();
		};

		public DatePickParams(Context context, Calendar calendar) {
			mContext = context;
			this.calendar = calendar;
		}

		public void apply(SingleTimeWheelBuilderDialog dialog) {
			if (mTitle != null) {
				dialog.setTitle(mTitle);
			}

			if (mPositiveButtonText != null) {
				dialog.setPositiveButton(mPositiveButtonText, mPositiveButtonListener);
			}
			if (mNegativeButtonText != null) {
				dialog.setNegativeButton(mNegativeButtonText, mNegativeButtonListener);
			}
			if (calendar != null) dialog.setCalendar(calendar);

		}
	}

	private void setPositiveButton(CharSequence mPositiveButtonText, View.OnClickListener onClickListener) {
		positiveText = mPositiveButtonText;
		positiveClickListener = onClickListener;// can't use btn_sure here
												// because it's on defined yet
	}

	private void setNegativeButton(CharSequence mNegativeButtonText, View.OnClickListener onClickListener) {
		negativeText = mNegativeButtonText;
		negativeClickListener = onClickListener;// can't use btn_sure here
												// because it's on defined yet
	}

	private void setCalendar(Calendar calendar) {
		this.mCalendar = calendar;
	}

}
