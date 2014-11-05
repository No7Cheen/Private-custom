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

public class PcTimeSelectorWheelDialog extends Dialog {

	private final NumericWheelAdapter mHoursAdapter = new NumericWheelAdapter(0, 23, "%02d");
	private final NumericWheelAdapter mMinuteAdapter = new NumericWheelAdapter(0, 59, "%02d");

	private final int VISIBLE_WHELLITEMS = 3;

	private TTSelectorWheelView timeHoursWheelView;
	private TTSelectorWheelView timeMinuteWheelView;

	private TextView mTimesTxt;

	private final Context mContext;

	private View.OnClickListener okBtnLister;
	private View.OnClickListener cancelBtnLister;

	public PcTimeSelectorWheelDialog(Context context) {
		super(context);
		mContext = context;
	}

	public PcTimeSelectorWheelDialog(Context context, int theme) {
		super(context, theme);
		mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.timeselector_time_layout);

		findViews();
		setListener();
		initDate();
	}

	@Override
	public void show() {
		super.show();
	}

	private void findViews() {
		timeHoursWheelView = (TTSelectorWheelView) findViewById(R.id.time_hours);
		timeMinuteWheelView = (TTSelectorWheelView) findViewById(R.id.time_minute);

		mTimesTxt = (TextView) findViewById(R.id.times_txt);

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
		timeHoursWheelView.addChangingListener(HoursOnTTSelectorWheelChangedListener);
		timeMinuteWheelView.addChangingListener(MinuteOnTTSelectorWheelChangedListener);
	}

	/**
	 * Adds changing listener for wheel-dayOfMonth
	 */
	private OnTTSelectorWheelChangedListener HoursOnTTSelectorWheelChangedListener = new OnTTSelectorWheelChangedListener() {

		@Override
		public void onChanged(TTSelectorWheelView wheel, int oldValue, int newValue) {
			try {
				mCurrHour = mHoursAdapter.getItem(newValue);
			} catch (Exception e) {
			}
			setTitle();
		}
	};

	/**
	 * Adds changing listener for wheel-monthOfYear
	 */
	private OnTTSelectorWheelChangedListener MinuteOnTTSelectorWheelChangedListener = new OnTTSelectorWheelChangedListener() {

		@Override
		public void onChanged(TTSelectorWheelView wheel, int oldValue, int newValue) {
			try {
				mCurrMinute = mMinuteAdapter.getItem(newValue);
			} catch (Exception e) {
			}
			setTitle();
		}
	};

	public void initDate() {
		final float scale = mContext.getResources().getDisplayMetrics().density; // 屏幕密度
		int textSize = (int) (40 * scale + 0.5f);// 根据屏幕密度来指定选择器字体的大小

		timeHoursWheelView.TEXT_SIZE = textSize; // 字体大小
		timeHoursWheelView.setAdapter(mHoursAdapter);
		timeHoursWheelView.setVisibleItems(VISIBLE_WHELLITEMS);
		timeHoursWheelView.setCyclic(true);

		timeMinuteWheelView.TEXT_SIZE = textSize; // 字体大小
		timeMinuteWheelView.setAdapter(mMinuteAdapter);
		timeMinuteWheelView.setVisibleItems(VISIBLE_WHELLITEMS);
		timeMinuteWheelView.setCyclic(true);

		// set current time
		Calendar c = Calendar.getInstance();
		int curHours = c.get(Calendar.HOUR_OF_DAY);
		int curMinutes = c.get(Calendar.MINUTE);
		timeHoursWheelView.setCurrentItem(curHours);
		timeMinuteWheelView.setCurrentItem(curMinutes);

		setTitle();
	}

	/**
	 * 设置Hour当前显示位置
	 * @Description
	 * @param hours
	 */
	public void setCurrItemByHours(String hours) {
		if (timeHoursWheelView == null || mHoursAdapter == null) {
			return;
		}

		try {
			int hoursIndexOf = Integer.parseInt(hours);
			timeHoursWheelView.setCurrentItem(hoursIndexOf);
		} catch (Exception e) {
		}
	}

	/**
	 * 设置Minute当前显示位置
	 * @Description
	 * @param hours
	 */
	public void setCurrItemByMinutes(String hours) {
		if (timeMinuteWheelView == null || mMinuteAdapter == null) {
			return;
		}

		try {
			int minutesIndexOf = Integer.parseInt(hours);
			timeMinuteWheelView.setCurrentItem(minutesIndexOf);
		} catch (Exception e) {
		}
	}

	private String mCurrHour;

	public String getCurrHour() {
		return mCurrHour;
	}

	private String mCurrMinute;

	public String getCurrMinute() {
		return mCurrMinute;
	}

	private void setTitle() {
		if (mCurrHour == null || mCurrHour.length() == 0 || mCurrMinute == null || mCurrMinute.length() == 0) {
			Calendar c = Calendar.getInstance();
			int curHours = c.get(Calendar.HOUR_OF_DAY);
			int curMinutes = c.get(Calendar.MINUTE);
			mTimesTxt.setText(String.format("%02d", curHours) + ":" + String.format("%02d", curMinutes));

			return;
		}

		mTimesTxt.setText(mCurrHour + ":" + mCurrMinute);
	}

	/**
	 * 底部弹出框
	 * @Description
	 * @param context
	 * @param cancelLister
	 * @return
	 */
	public static PcTimeSelectorWheelDialog bottomPoppupDialog4TimeSelector(Context context,
			OnCancelListener cancelLister, View.OnClickListener okBtnLister, View.OnClickListener cancelBtnLister) {
		PcTimeSelectorWheelDialog dialog = new PcTimeSelectorWheelDialog(context, R.style.Bottom_Dialog_Theme);
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
