package com.pc.ui.view.wheelview;

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

import com.pc.ui.widget.WheelView.NumericWheelAdapter;
import com.pc.ui.widget.WheelView.OnTTSelectorWheelChangedListener;
import com.pc.ui.widget.WheelView.TTSelectorWheelView;
import com.privatecustom.publiclibs.R;

@SuppressLint("WrongViewCast")
public class PcDateSelectorWheelDialog extends Dialog {

	private final String FORMAT_YEAR = "%04d";
	private final String FORMAT_MONTH = "%02d";
	private final String FORMAT_DAY = "%02d";

	private final NumericWheelAdapter mYearsAdapter = new NumericWheelAdapter(1970, 3000, FORMAT_YEAR); // 2000年--2099年
	private final NumericWheelAdapter mMonthAdapter = new NumericWheelAdapter(1, 12, FORMAT_MONTH); // 1年12个月
	private NumericWheelAdapter mDayOfMonthAdapter = new NumericWheelAdapter(1, 30, FORMAT_DAY); // 默认每月30天

	// private final String YEAR_PREFIX = "20";
	private final int VISIBLE_WHELLITEMS = 3;

	private TTSelectorWheelView dayOfMonthWheelView;
	private TTSelectorWheelView monthOfYearWheelView;
	private TTSelectorWheelView yearWheelView;

	private TextView mDayOfWeekTitleTV; // dat of week
	private TextView mDateTitleTV; // month
	private TextView mYearTitleTV;// year

	private final Context mContext;

	private final int mThisYear = Calendar.getInstance().get(Calendar.YEAR);
	private final int mThisMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
	private final int mToady = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

	private int yearIndexof;
	private int monthIndexof;
	private int dayOfMonthIndexOf;

	private View.OnClickListener okBtnLister;
	private View.OnClickListener cancelBtnLister;

	public PcDateSelectorWheelDialog(Context context) {
		super(context);
		mContext = context;
	}

	public PcDateSelectorWheelDialog(Context context, int theme) {
		super(context, theme);
		mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.timeselector_date);

		findViews();
		// adjustView();
		setListener();
		initDate();
	}

	private void findViews() {
		dayOfMonthWheelView = (TTSelectorWheelView) findViewById(R.id.day_of_month);
		monthOfYearWheelView = (TTSelectorWheelView) findViewById(R.id.month_of_year);
		yearWheelView = (TTSelectorWheelView) findViewById(R.id.year);

		mDayOfWeekTitleTV = (TextView) findViewById(R.id.day_of_week_title);
		mDateTitleTV = (TextView) findViewById(R.id.date_title);
		mYearTitleTV = (TextView) findViewById(R.id.year_title);

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
		dayOfMonthWheelView.addChangingListener(dayOfMonthOnTTSelectorWheelChangedListener);
		monthOfYearWheelView.addChangingListener(monthOfYearOnTTSelectorWheelChangedListener);
		yearWheelView.addChangingListener(yearOnTTSelectorWheelChangedListener);
	}

	/**
	 * Adds changing listener for wheel-dayOfMonth
	 */
	private OnTTSelectorWheelChangedListener dayOfMonthOnTTSelectorWheelChangedListener = new OnTTSelectorWheelChangedListener() {

		@Override
		public void onChanged(TTSelectorWheelView wheel, int oldValue, int newValue) {
			try {
				mCurrValueDay = mDayOfMonthAdapter.getItem(newValue);
			} catch (Exception e) {
				mCurrValueDay = String.format(FORMAT_DAY, mToady);
			}

			setTitle();
		}
	};

	/**
	 * Adds changing listener for wheel-monthOfYear
	 */
	private OnTTSelectorWheelChangedListener monthOfYearOnTTSelectorWheelChangedListener = new OnTTSelectorWheelChangedListener() {

		@Override
		public void onChanged(TTSelectorWheelView wheel, int oldValue, int newValue) {
			try {
				mCurrValueMonth = mMonthAdapter.getItem(newValue);
			} catch (Exception e) {
				mCurrValueMonth = String.format(FORMAT_MONTH, mThisMonth);
			}

			refreshDaysOfMonth(mCurrValueYear, mCurrValueMonth);

			// 如果当前月是2月
			if ("02".equals(mCurrValueMonth)) {
				boolean isCommonYear = true; // 平年
				try {
					int iYear = Integer.parseInt(mCurrValueYear);
					if (iYear % 4 == 0 && iYear % 100 != 0 || iYear % 400 == 0) { // 当前年份为闰年
						isCommonYear = false;
					} else {
						isCommonYear = true;
					}
				} catch (Exception e) {
				}

				// 平年，需要检测被选中天数是否是：29/30/31
				if (isCommonYear) {
					if ("31".equals(mCurrValueDay) || "30".equals(mCurrValueDay) || "29".equals(mCurrValueDay)) {
						dayOfMonthIndexOf = 0;
						dayOfMonthWheelView.setCurrentItem(dayOfMonthIndexOf);
					}
				}
				// 闰年，需要检测被选中天数是否是：30/31
				else {
					if ("31".equals(mCurrValueDay) || "30".equals(mCurrValueDay)) {
						dayOfMonthIndexOf = 0;
						dayOfMonthWheelView.setCurrentItem(dayOfMonthIndexOf);
					}
				}
			}
			// 如果当前月不是2月，需要检测被选中的是否是31号
			else {
				if ("31".equals(mCurrValueDay)
						&& ("04".equals(mCurrValueMonth) || "06".equals(mCurrValueMonth)
								|| "09".equals(mCurrValueMonth) || "11".equals(mCurrValueMonth))) { // 当前月是小月(30天)
					dayOfMonthIndexOf = 0;
					dayOfMonthWheelView.setCurrentItem(dayOfMonthIndexOf);
				}
			}

			setTitle();
		}
	};

	/**
	 * Adds changing listener for wheel-year
	 */
	private OnTTSelectorWheelChangedListener yearOnTTSelectorWheelChangedListener = new OnTTSelectorWheelChangedListener() {

		@Override
		public void onChanged(TTSelectorWheelView wheel, int oldValue, int newValue) {
			boolean isResetInitIndexof4Day = false;
			if ("02".equals(mCurrValueMonth) && "29".equals(mCurrValueDay)) { // 如果当前月是2月,且当前处于2-29位置
				isResetInitIndexof4Day = true;
			}

			try {
				mCurrValueYear = mYearsAdapter.getItem(newValue);
			} catch (Exception e) {
				mCurrValueYear = mThisYear + "";
			}

			refreshDaysOfMonth(mCurrValueYear, mCurrValueMonth);
			try {
				if (isResetInitIndexof4Day) {
					int iYear = Integer.parseInt(mCurrValueYear);
					if (iYear % 4 == 0 && iYear % 100 != 0 || iYear % 400 == 0) { // 改变之后的年份为闰年

					} else {// 改变之后的年份为平年，2月只有28天
						dayOfMonthIndexOf = 0;
						dayOfMonthWheelView.setCurrentItem(dayOfMonthIndexOf);
					}
				}
			} catch (Exception e) {
			}

			setTitle();
		}
	};

	public void initDate() {
		yearIndexof = 0;
		monthIndexof = 0;
		dayOfMonthIndexOf = 0;

		mCurrValueDay = String.format(FORMAT_DAY, mToady);
		mCurrValueMonth = String.format(FORMAT_MONTH, mThisMonth);
		mCurrValueYear = String.format(FORMAT_YEAR, mThisYear);

		computIndexOfCurrYear();
		computIndexOfCurrMonth();

		final float scale = mContext.getResources().getDisplayMetrics().density; // 屏幕密度
		int textSize = (int) (40 * scale + 0.5f);// 根据屏幕密度来指定选择器字体的大小

		yearWheelView.TEXT_SIZE = textSize; // 字体大小
		yearWheelView.setAdapter(mYearsAdapter);
		yearWheelView.setVisibleItems(VISIBLE_WHELLITEMS);
		yearWheelView.setCyclic(true);

		monthOfYearWheelView.TEXT_SIZE = textSize; // 字体大小
		monthOfYearWheelView.setAdapter(mMonthAdapter);
		monthOfYearWheelView.setVisibleItems(VISIBLE_WHELLITEMS);
		monthOfYearWheelView.setCyclic(true);

		dayOfMonthWheelView.TEXT_SIZE = textSize; // 字体大小
		dayOfMonthWheelView.setAdapter(mDayOfMonthAdapter);
		dayOfMonthWheelView.setVisibleItems(VISIBLE_WHELLITEMS);
		dayOfMonthWheelView.setCyclic(true);

		yearWheelView.setCurrentItem(yearIndexof);
		monthOfYearWheelView.setCurrentItem(monthIndexof);

		refreshDaysOfMonth(getCurrValueYear(), getCurrValueMonth());
		computIndexOfToady();
		dayOfMonthWheelView.setCurrentItem(dayOfMonthIndexOf);

		mCurrValueDay = mDayOfMonthAdapter.getItem(dayOfMonthIndexOf);
		mCurrValueMonth = mMonthAdapter.getItem(monthIndexof);
		mCurrValueYear = mYearsAdapter.getItem(yearIndexof);

		setTitle();
	}

	/**
	 * 设置Years当前显示位置
	 * @Description
	 */
	public void setCurrItemByYears(String year) {
		if (yearWheelView == null || mYearsAdapter == null) {
			return;
		}

		try {
			int count = mYearsAdapter.getItemsCount();
			if (count == 0 || year == null || year.length() == 0) {
				return;
			}

			for (int i = 0; i < count; i++) {
				String tmpYear = mYearsAdapter.getItem(i);
				if (tmpYear == null || tmpYear.length() == 0) {
					continue;
				}
				try {
					if (year.equals(tmpYear)) {
						yearIndexof = i;
						break;
					}
				} catch (Exception e) {
				}
			}

			yearWheelView.setCurrentItem(yearIndexof);
			refreshDaysOfMonth(mCurrValueYear, mCurrValueMonth);
		} catch (Exception e) {
		}
	}

	/**
	 * 设置Month当前显示位置
	 * @Description
	 */
	public void setCurrItemByMonth(String month) {
		if (monthOfYearWheelView == null || mMonthAdapter == null) {
			return;
		}

		try {

			int monthCount = mMonthAdapter.getItemsCount();
			if (monthCount == 0 || month == null || month.length() == 0) {
				return;
			}

			for (int i = 0; i < monthCount; i++) {
				try {
					if (month.equals(mMonthAdapter.getItem(i))) {
						monthIndexof = i;
						break;
					}
				} catch (Exception e) {
				}
			}

			monthOfYearWheelView.setCurrentItem(monthIndexof);
			refreshDaysOfMonth(mCurrValueYear, mCurrValueMonth);
		} catch (Exception e) {
		}
	}

	/**
	 * 设置Day当前显示位置
	 * @Description
	 */
	public void setCurrItemByDay(String day) {
		if (dayOfMonthWheelView == null || mDayOfMonthAdapter == null) {
			return;
		}

		try {
			int dayCount = mDayOfMonthAdapter.getItemsCount();
			if (dayCount == 0 || day == null || day.length() == 0) {
				return;
			}

			for (int i = 0; i < dayCount; i++) {
				try {
					if (day.equals(mDayOfMonthAdapter.getItem(i))) {
						dayOfMonthIndexOf = i;
						break;
					}
				} catch (Exception e) {
				}

				dayOfMonthWheelView.setCurrentItem(dayOfMonthIndexOf);
			}
		} catch (Exception e) {
		}
	}

	/** 当前年 */
	private String mCurrValueYear;
	/** 当前月 */
	private String mCurrValueMonth;
	/** 当前天 */
	private String mCurrValueDay;

	public String getCurrValueYear() {
		return mCurrValueYear;
	}

	public String getCurrValueMonth() {
		return mCurrValueMonth;
	}

	public String getCurrValueDay() {
		return mCurrValueDay;
	}

	/**
	 * 计算某月的天数
	 * @Description
	 * @param year
	 * @param month
	 */
	public void refreshDaysOfMonth(String year, String month) {
		try {
			int iYear = Integer.parseInt(year);
			int iMonth = Integer.parseInt(month);
			refreshDaysOfMonth(iYear, iMonth);
		} catch (Exception e) {
			refreshDaysOfMonth(mThisYear, mThisMonth);
		}
	}

	/**
	 * 计算某月的天数
	 * @Description
	 * @param year
	 * @param month
	 */
	public void refreshDaysOfMonth(int year, int month) {
		if (year == 0 || month == 0) {
			return;
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
					mDayOfMonthAdapter = new NumericWheelAdapter(1, 31, FORMAT_DAY);
					break;

				case 4:
				case 6:
				case 9:
				case 11:
					mDayOfMonthAdapter = new NumericWheelAdapter(1, 30, FORMAT_DAY);
					break;
			}
		} else {
			if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) { // 闰年2月29天
				mDayOfMonthAdapter = new NumericWheelAdapter(1, 29, FORMAT_DAY);
			} else {
				mDayOfMonthAdapter = new NumericWheelAdapter(1, 28, FORMAT_DAY);
			}
		}

		dayOfMonthWheelView.setAdapter(mDayOfMonthAdapter);
		dayOfMonthWheelView.setVisibleItems(VISIBLE_WHELLITEMS);
		dayOfMonthWheelView.setCyclic(true);
	}

	/**
	 * 计算当前年的位置
	 * @Description
	 */
	private void computIndexOfCurrYear() {
		monthIndexof = 0;

		if (mYearsAdapter == null) {
			return;
		}

		String year = String.format(FORMAT_YEAR, mThisYear);
		int count = mYearsAdapter.getItemsCount();
		if (count == 0 || year == null || year.length() == 0) {
			return;
		}

		for (int i = 0; i < count; i++) {
			String tmpYear = mYearsAdapter.getItem(i);
			if (tmpYear == null || tmpYear.length() == 0) {
				continue;
			}
			try {
				if (year.equals(tmpYear)) {
					yearIndexof = i;
					break;
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 计算当前月的位置
	 * @Description
	 */
	private void computIndexOfCurrMonth() {
		monthIndexof = 0;

		if (mMonthAdapter == null) {
			return;
		}
		String month = String.format(FORMAT_MONTH, mThisMonth);
		int monthCount = mMonthAdapter.getItemsCount();
		if (monthCount == 0 || month == null || month.length() == 0) {
			return;
		}

		for (int i = 0; i < monthCount; i++) {
			try {
				if (month.equals(mMonthAdapter.getItem(i))) {
					monthIndexof = i;
					break;
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 计算当天的位置
	 * @Description
	 */
	private void computIndexOfToady() {
		dayOfMonthIndexOf = 0;

		if (mDayOfMonthAdapter == null) {
			return;
		}
		String day = String.format(FORMAT_DAY, mToady);
		int dayCount = mDayOfMonthAdapter.getItemsCount();
		if (dayCount == 0 || day == null || day.length() == 0) {
			return;
		}

		for (int i = 0; i < dayCount; i++) {
			try {
				if (day.equals(mDayOfMonthAdapter.getItem(i))) {
					dayOfMonthIndexOf = i;
					break;
				}
			} catch (Exception e) {
			}
		}
	}

	private void setTitle() {
		switch (getDayOfWeek()) {
			case Calendar.SUNDAY:
				mDayOfWeekTitleTV.setText("周日");
				break;
			case Calendar.MONDAY:
				mDayOfWeekTitleTV.setText("周一");
				break;
			case Calendar.TUESDAY:
				mDayOfWeekTitleTV.setText("周二");
				break;
			case Calendar.WEDNESDAY:
				mDayOfWeekTitleTV.setText("周三");
				break;
			case Calendar.THURSDAY:
				mDayOfWeekTitleTV.setText("周四");
				break;
			case Calendar.FRIDAY:
				mDayOfWeekTitleTV.setText("周五");
				break;
			case Calendar.SATURDAY:
				mDayOfWeekTitleTV.setText("周六");
				break;
		}

		mYearTitleTV.setText(getCurrValueYear() + "年");
		mDateTitleTV.setText(getCurrValueMonth() + "月" + getCurrValueDay() + "日");
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
		try {
			iYear = Integer.parseInt(mCurrValueYear);
			iMonth = Integer.parseInt(mCurrValueMonth) - 1; // 因为前面月份被加了1
			iDay = Integer.parseInt(mCurrValueDay);
		} catch (Exception e) {
		}
		cal.set(iYear, iMonth, iDay);

		return cal;
	}

	/**
	 * 底部弹出框
	 * @Description
	 * @param context
	 * @param cancelLister
	 * @return
	 */
	public static PcDateSelectorWheelDialog bottomPoppupDialog4DateSelector(Context context,
			OnCancelListener cancelLister, View.OnClickListener okBtnLister, View.OnClickListener cancelBtnLister) {
		PcDateSelectorWheelDialog dialog = new PcDateSelectorWheelDialog(context, R.style.Bottom_Dialog_Theme);
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

		dialog.show();

		return dialog;
	}

}
