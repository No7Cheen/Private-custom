package com.pc.ui.view.wheelview;

import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pc.ui.widget.WheelView.ArrayWheelAdapter;
import com.pc.ui.widget.WheelView.NumericSPaceWheelAdapter;
import com.pc.ui.widget.WheelView.NumericWheelAdapter;
import com.pc.ui.widget.WheelView.OnTTSelectorWheelChangedListener;
import com.pc.ui.widget.WheelView.TTSelectorWheelView;
import com.pc.utils.StringUtils;
import com.privatecustom.publiclibs.R;

public class PcDateTimeSelectorWheelDialog extends Dialog {

	private final String FORMAT = "%02d";
	private final int VISIBLE_WHELLITEMS = 3;

	private ArrayWheelAdapter<String> datesAdapter;
	private final NumericWheelAdapter mHoursAdapter = new NumericWheelAdapter(8, 21, FORMAT);
	// private final NumericWheelAdapter mMinuteAdapter = new
	// NumericWheelAdapter(0, 59, FORMAT);
	private final NumericSPaceWheelAdapter mMinuteAdapter2 = new NumericSPaceWheelAdapter(0, 4, FORMAT, 15);

	private TTSelectorWheelView dateWheelView; // 日期
	private TTSelectorWheelView timeHoursWheelView; // Hours
	private TTSelectorWheelView timeMinuteWheelView; // Minute

	private TextView mDayOfWeekTitleTV; // dat of week
	private TextView mDateTitleTV; // month
	private TextView mYearTitleTV; // year
	private TextView mTimesTitleTV; // Title

	private final Context mContext;

	private final int mThisYear = Calendar.getInstance().get(Calendar.YEAR);
	private final int mThisMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
	private final int mToady = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

	private View.OnClickListener okBtnLister;
	private View.OnClickListener cancelBtnLister;

	public PcDateTimeSelectorWheelDialog(Context context) {
		super(context);
		mContext = context;
	}

	public PcDateTimeSelectorWheelDialog(Context context, int theme) {
		super(context, theme);
		mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.timeselector_datetime);

		findViews();
		setListener();
		initDate();
	}

	private void findViews() {
		dateWheelView = (TTSelectorWheelView) findViewById(R.id.date);
		timeHoursWheelView = (TTSelectorWheelView) findViewById(R.id.time_hours);
		timeMinuteWheelView = (TTSelectorWheelView) findViewById(R.id.time_minute);

		mYearTitleTV = (TextView) findViewById(R.id.year_title);
		mDateTitleTV = (TextView) findViewById(R.id.date_title);
		mDayOfWeekTitleTV = (TextView) findViewById(R.id.day_of_week_title);
		mTimesTitleTV = (TextView) findViewById(R.id.times_title);

		if (okBtnLister != null) {
			findViewById(R.id.ok_btn).setOnClickListener(okBtnLister);
		}
		if (cancelBtnLister != null) {
			findViewById(R.id.cancel_btn).setOnClickListener(cancelBtnLister);
		}
	}

	public void setOkBtnLister(View.OnClickListener okBtnLister) {
		this.okBtnLister = okBtnLister;
	}

	public void setCancelBtnLister(View.OnClickListener cancelBtnLister) {
		this.cancelBtnLister = cancelBtnLister;
	}

	/**
	 * 校正View
	 * @Description
	 */
	@SuppressLint("WrongViewCast")
	@SuppressWarnings("unused")
	private void adjustView() {
		final RelativeLayout rootView = (RelativeLayout) findViewById(R.id.tt_meeting_dateselector_root);
		int paddingBottom = rootView.getPaddingBottom();
		int paddingTop = rootView.getPaddingTop();
		int paddingRight = rootView.getPaddingRight();
		int paddingLeft = rootView.getPaddingLeft();
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
	}

	/**
	 * add listeners
	 * @Description
	 */
	private void setListener() {
		dateWheelView.addChangingListener(DateOnTTSelectorWheelChangedListener);
		timeHoursWheelView.addChangingListener(HoursOnTTSelectorWheelChangedListener);
		timeMinuteWheelView.addChangingListener(MinuteOnTTSelectorWheelChangedListener);
	}

	/**
	 * Adds changing listener for wheel-Date
	 */
	private OnTTSelectorWheelChangedListener DateOnTTSelectorWheelChangedListener = new OnTTSelectorWheelChangedListener() {

		@Override
		public void onChanged(TTSelectorWheelView wheel, int oldValue, int newValue) {
			try {
				String newDate = datesAdapter.getItem(newValue); // 10月23日
				String oldDate = datesAdapter.getItem(oldValue); // 10月23日
				if (newDate != null && newDate.length() > 0) {
					mCurrValueMonth = newDate.substring(0, newDate.indexOf("月"));
					mCurrValueDay = newDate.substring(newDate.indexOf("月") + 1, newDate.indexOf("日"));
				}

				// 如果旧日期是12-31，新日期是01-01(如果快速滑动有可能是01-02|01-03),则显示下年
				if ((StringUtils.equals(oldDate, "12月29日") || StringUtils.equals(oldDate, "12月30日") || StringUtils
						.equals(oldDate, "12月31日"))
						&& (StringUtils.equals(newDate, "01月01日") || StringUtils.equals(newDate, "01月02日") || StringUtils
								.equals(newDate, "01月03日"))) { // 下年
					int year = StringUtils.str2Int(mCurrValueYear, mThisYear) + 1;

					setCurrValueYear(year + "");

					// 如果旧日期是01-0X，新日期是12-31,则显示下年
				} else if ((StringUtils.equals(oldDate, "01月01日") || StringUtils.equals(oldDate, "01月02日") || StringUtils
						.equals(oldDate, "01月03日"))
						&& (StringUtils.equals(newDate, "12月31日") || StringUtils.equals(newDate, "12月30日") || StringUtils
								.equals(newDate, "12月29日"))) { // 上年
					int year = StringUtils.str2Int(mCurrValueYear, mThisYear) - 1;

					setCurrValueYear(year + "");
				}
			} catch (Exception e) {
			}

			setTitle();
		}
	};

	/**
	 * Adds changing listener for wheel-Hours
	 */
	private OnTTSelectorWheelChangedListener HoursOnTTSelectorWheelChangedListener = new OnTTSelectorWheelChangedListener() {

		@Override
		public void onChanged(TTSelectorWheelView wheel, int oldValue, int newValue) {
			try {
				mCurrValueHour = mHoursAdapter.getItem(newValue);
			} catch (Exception e) {
			}
			setTitle();
		}
	};

	/**
	 * Adds changing listener for wheel-Minute
	 */
	private OnTTSelectorWheelChangedListener MinuteOnTTSelectorWheelChangedListener = new OnTTSelectorWheelChangedListener() {

		@Override
		public void onChanged(TTSelectorWheelView wheel, int oldValue, int newValue) {
			try {
				mCurrValueMinute = mMinuteAdapter2.getItem(newValue);
			} catch (Exception e) {
			}
			setTitle();
		}
	};

	public void initDate() {
		mCurrValueDay = String.format(FORMAT, mToady);
		mCurrValueMonth = String.format(FORMAT, mThisMonth);
		if (StringUtils.isNull(mCurrValueHour)) {
			mCurrValueHour = String.format(FORMAT, Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		}
		if (StringUtils.isNull(mCurrValueMinute)) {
			mCurrValueMinute = String.format(FORMAT, Calendar.getInstance().get(Calendar.MINUTE));
		}

		initDatesOfYear();

		final float scale = mContext.getResources().getDisplayMetrics().density; // 屏幕密度
		int textSize = (int) (24 * scale + 0.5f);// 根据屏幕密度来指定选择器字体的大小

		dateWheelView.TEXT_SIZE = textSize; // 字体大小
		dateWheelView.setAdapter(datesAdapter);
		dateWheelView.setVisibleItems(VISIBLE_WHELLITEMS);
		dateWheelView.setCyclic(true);

		timeHoursWheelView.TEXT_SIZE = textSize; // 字体大小
		timeHoursWheelView.setAdapter(mHoursAdapter);
		timeHoursWheelView.setVisibleItems(VISIBLE_WHELLITEMS);
		timeHoursWheelView.setCyclic(true);

		timeMinuteWheelView.TEXT_SIZE = textSize; // 字体大小
		timeMinuteWheelView.setAdapter(mMinuteAdapter2);
		timeMinuteWheelView.setVisibleItems(VISIBLE_WHELLITEMS);
		timeMinuteWheelView.setCyclic(true);

		refreshIndexOfWheelView();

		setTitle();
	}

	public void initDatesOfYear() {
		ArrayList<String> datesOfYearList = new ArrayList<String>();
		for (int month = 1; month <= 12; month++) {
			int days = computDaysOfMonth(mThisYear, month);
			for (int day = 1; day <= days; day++) {
				datesOfYearList.add(String.format(FORMAT, month) + "月" + String.format(FORMAT, day) + "日");
			}
		}

		String datesOfYear[] = datesOfYearList.toArray(new String[datesOfYearList.size()]);
		datesAdapter = new ArrayWheelAdapter<String>(datesOfYear);
	}

	public void refreshIndexOfCurrDate() {
		if (datesAdapter == null || dateWheelView == null) return;

		String date = mCurrValueMonth + "月" + mCurrValueDay + "日";
		for (int index = 0; index < datesAdapter.getItemsCount(); index++) {
			String item = datesAdapter.getItem(index);
			if (item != null && item.length() > 0 && item.equals(date)) {
				dateWheelView.setCurrentItem(index);
				break;
			}
		}
	}

	public void refreshIndexOfCurrHours() {
		int currHour = getCurrCalendar().get(Calendar.HOUR_OF_DAY);
		if (timeHoursWheelView != null) timeHoursWheelView.setCurrentItem(mHoursAdapter.indexOf(currHour));
	}

	public void refreshIndexOfCurrMinute() {
		int currMinute = getCurrCalendar().get(Calendar.MINUTE);
		if (timeMinuteWheelView != null) timeMinuteWheelView.setCurrentItem(mMinuteAdapter2.indexOf(currMinute));
	}

	public void refreshIndexOfWheelView() {
		refreshIndexOfCurrDate();
		refreshIndexOfCurrHours();
		refreshIndexOfCurrMinute();
	}

	/** 当前年 */
	private String mCurrValueYear;
	/** 当前月 */
	private String mCurrValueMonth;
	/** 当前天 */
	private String mCurrValueDay;
	private String mCurrValueHour;
	private String mCurrValueMinute;

	public String getCurrValueYear() {
		return mCurrValueYear;
	}

	public void setCurrValueYear(String year) {
		mCurrValueYear = year;
	}

	public String getCurrValueMonth() {
		return mCurrValueMonth;
	}

	public void setCurrMonth(String month) {
		try {
			mCurrValueMonth = String.format(FORMAT, month);
		} catch (Exception e) {
			mCurrValueMonth = month;
		}
	}

	public String getCurrValueDay() {
		return mCurrValueDay;
	}

	public void setCurrDay(String day) {
		try {
			mCurrValueDay = String.format(FORMAT, day);
		} catch (Exception e) {
			mCurrValueDay = day;
		}
	}

	public String getCurrHour() {
		return mCurrValueHour;
	}

	public void setCurrHour(String hours) {
		try {
			mCurrValueHour = String.format(FORMAT, hours);
		} catch (Exception e) {
			mCurrValueHour = hours;
		}
	}

	public String getCurrMinute() {
		return mCurrValueMinute;
	}

	public void setCurrMinute(String minutes) {
		try {
			mCurrValueMinute = String.format(FORMAT, minutes);
		} catch (Exception e) {
			mCurrValueMinute = minutes;
		}
	}

	/**
	 * 计算某月的天数
	 * @Description
	 * @param year
	 * @param month
	 */
	public int computDaysOfMonth(int year, int month) {
		int days = 30;
		if (year == 0 || month == 0) {
			return days;
		}

		if (month != 2) {
			switch (month) {
				case 1:
				case 3:
				case 5:
				case 7:
				case 8:
				case 10:
				case 12:
					days = 31;
					break;

				case 4:
				case 6:
				case 9:
				case 11:
					days = 30;
					break;
			}
		} else {
			if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) { // 闰年2月29天
				days = 29;
			} else {
				days = 28;
			}
		}

		return days;
	}

	private void setTitle() {
		String week = "";
		switch (getDayOfWeek()) {
			case Calendar.SUNDAY:
				week = "周日";
				break;
			case Calendar.MONDAY:
				week = "周一";
				break;
			case Calendar.TUESDAY:
				week = "周二";
				break;
			case Calendar.WEDNESDAY:
				week = "周三";
				break;
			case Calendar.THURSDAY:
				week = "周四";
				break;
			case Calendar.FRIDAY:
				week = "周五";
				break;
			case Calendar.SATURDAY:
				week = "周六";
				break;
		}

		if (StringUtils.isNull(mCurrValueYear)) {
			mCurrValueYear = mThisYear + "";
		}
		mYearTitleTV.setText(mCurrValueYear + "年,");
		mDateTitleTV.setText(getCurrValueMonth() + "月" + getCurrValueDay() + "日,");
		mDayOfWeekTitleTV.setText(week + ",");

		if (mCurrValueHour == null || mCurrValueHour.length() == 0 || mCurrValueMinute == null
				|| mCurrValueMinute.length() == 0) {
			Calendar c = Calendar.getInstance();
			int curHours = c.get(Calendar.HOUR_OF_DAY);
			int curMinutes = c.get(Calendar.MINUTE);
			mTimesTitleTV.setText(String.format("%02d", curHours) + ":" + String.format("%02d", curMinutes) + ",");
		} else {
			mTimesTitleTV.setText(mCurrValueHour + ":" + mCurrValueMinute);
		}
	}

	public String getChineseDayOfWeek() {
		String chineseWeek = "";
		switch (getDayOfWeek()) {
			case Calendar.SUNDAY:
				chineseWeek = "周日";
				break;
			case Calendar.MONDAY:
				chineseWeek = "周一";
				break;
			case Calendar.TUESDAY:
				chineseWeek = "周二";
				break;
			case Calendar.WEDNESDAY:
				chineseWeek = "周三";
				break;
			case Calendar.THURSDAY:
				chineseWeek = "周四";
				break;
			case Calendar.FRIDAY:
				chineseWeek = "周五";
				break;
			case Calendar.SATURDAY:
				chineseWeek = "周六";
				break;
		}

		return chineseWeek;
	}

	/**
	 * @Description 周日=1，周一=2
	 * @return
	 */
	public int getDayOfWeek() {

		return getCurrCalendar().get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * Year
	 * @Description
	 * @return
	 */
	public int getCurrYear() {

		return getCurrCalendar().get(Calendar.YEAR);
	}

	/**
	 * Month
	 * @Description
	 * @return
	 */
	public int getCurrMonth() {

		return getCurrCalendar().get(Calendar.MONTH) + 1;
	}

	/**
	 * day of month
	 * @Description
	 * @return
	 */
	public int getCurrDayOfMonth() {

		return getCurrCalendar().get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * curr calendar
	 * @Description
	 * @return
	 */
	public Calendar getCurrCalendar() {
		Calendar cal = Calendar.getInstance();
		int iYear = cal.get(Calendar.YEAR);
		int iMonth = cal.get(Calendar.MONTH);
		int iDay = cal.get(Calendar.DAY_OF_MONTH);
		int iHour = cal.get(Calendar.HOUR_OF_DAY);
		int iMinute = cal.get(Calendar.MINUTE);
		try {
			iYear = Integer.parseInt(mCurrValueYear);
			iMonth = Integer.parseInt(mCurrValueMonth) - 1; // 因为前面月份被加了1
			iDay = Integer.parseInt(mCurrValueDay);
			iHour = Integer.parseInt(mCurrValueHour);
			iMinute = Integer.parseInt(mCurrValueMinute);
		} catch (Exception e) {
		}

		cal.set(iYear, iMonth, iDay, iHour, iMinute);

		return cal;
	}

	/**
	 * 底部弹出框
	 * @Description
	 * @param context
	 * @param cancelLister
	 * @return
	 */
	public static PcDateTimeSelectorWheelDialog bottomPoppupDialog4DateTimeSelector(Context context, String year,
			String currHour, String currMinute, OnCancelListener cancelLister, View.OnClickListener okBtnLister,
			View.OnClickListener cancelBtnLister) {
		PcDateTimeSelectorWheelDialog dialog = new PcDateTimeSelectorWheelDialog(context, R.style.Bottom_Dialog_Theme);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		int h = dm.heightPixels;
		params.x = 0;
		params.y = h;

		Window window = dialog.getWindow();
		window.setAttributes(params);

		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);

		if (cancelLister != null) {
			dialog.setOnCancelListener(cancelLister);
		}

		dialog.setOkBtnLister(okBtnLister);
		dialog.setCancelBtnLister(cancelBtnLister);

		if (!StringUtils.isNull(year)) {
			dialog.setCurrValueYear(year);
		}
		if (!StringUtils.isNull(currHour)) {
			dialog.setCurrHour(currHour);
		}
		if (!StringUtils.isNull(currMinute)) {
			dialog.setCurrMinute(currMinute);
		}
		dialog.refreshIndexOfWheelView();

		dialog.show();

		return dialog;
	}

}
