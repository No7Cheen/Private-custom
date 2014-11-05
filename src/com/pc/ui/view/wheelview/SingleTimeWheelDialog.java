package com.pc.ui.view.wheelview;

import java.util.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.pc.ui.widget.WheelView.NumericWheelAdapter;
import com.pc.ui.widget.WheelView.OnWheelChangedListener;
import com.pc.ui.widget.WheelView.WheelView;
import com.privatecustom.publiclibs.R;

public class SingleTimeWheelDialog extends Dialog {

	private WheelView mWheelViewHours; // hours view
	private WheelView mWheelViewMins; // mins view

	private final Context mContext;
	private Calendar mCalendar;

	public SingleTimeWheelDialog(Context context) {
		super(context);
		mContext = context;
		mCalendar = Calendar.getInstance();
	}

	public SingleTimeWheelDialog(Context context, int theme) {
		super(context, theme);
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

	/**
	 * 校正View
	 * @Description
	 */
	@SuppressWarnings("unused")
	private void adjustView() {
		final LinearLayout rootWheelView = (LinearLayout) findViewById(R.id.rootWheelView);
		int paddingBottom = rootWheelView.getPaddingBottom();
		int paddingTop = rootWheelView.getPaddingTop();
		int paddingRight = rootWheelView.getPaddingRight();
		int paddingLeft = rootWheelView.getPaddingLeft();
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		rootWheelView.setMinimumWidth(dm.widthPixels);
		int minWidth = dm.widthPixels / 2 - paddingLeft; // 计算WheelView的宽度，屏幕宽度与左右间距之差的1/2
		// mWheelViewHours.setMinimumWidth(minWidth); // 设置WheelView的宽度
		// mWheelViewMins.setMinimumWidth(minWidth); // 设置WheelView的宽度

		final float scale = mContext.getResources().getDisplayMetrics().density; // 屏幕密度
		int textSize = (int) (28 * scale + 0.5f);// 根据屏幕密度来指定选择器字体的大小
		mWheelViewMins.TEXT_SIZE = textSize; // 字体大小
		mWheelViewHours.TEXT_SIZE = textSize; // 字体大小
	}

	/**
	 * add listeners
	 * @Description
	 */
	private void setListener() {
		mWheelViewHours.addChangingListener(mWheelChangedListenerHour);
		mWheelViewMins.addChangingListener(mWheelChangedListenerMins);
	}

	/**
	 * Adds changing listener for wheel-hours
	 */
	private OnWheelChangedListener mWheelChangedListenerHour = new OnWheelChangedListener() {

		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			mCurrValue[0] = newValue;
		}
	};

	/**
	 * Adds changing listener for wheel-mins
	 */
	private OnWheelChangedListener mWheelChangedListenerMins = new OnWheelChangedListener() {

		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			mCurrValue[1] = newValue;
		}
	};

	/**
	 * Adds changing listener for wheel that updates the wheel label
	 * @param wheel the wheel
	 * @param label the wheel label
	 */
	@SuppressWarnings("unused")
	private void addChangingListener(final WheelView wheel, final String label) {
		wheel.addChangingListener(new OnWheelChangedListener() {

			public void onChanged(WheelView wheel, int oldValue, int newValue) {
			}
		});
	}

	public void setDate(Calendar calendar) {
		mWheelViewHours.setAdapter(new NumericWheelAdapter(0, 23));
		// mWheelViewHours.setLabel("hour");
		mWheelViewHours.setCyclic(true);

		mWheelViewMins.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
		// mWheelViewMins.setLabel("min");
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

	/** 当前显示的Value */
	public int[] mCurrValue = new int[2];

	public int[] getCurrValue() {
		return mCurrValue;
	}

	/**
	 * 底部弹出框
	 * @Description
	 * @param context
	 * @param cancelLister
	 * @return
	 */
	public static SingleTimeWheelDialog bottomPoppupDialog4SingleTime(Context context, OnCancelListener cancelLister) {
		SingleTimeWheelDialog dialog = new SingleTimeWheelDialog(context, R.style.Bottom_Dialog_Theme);
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

		dialog.show();

		return dialog;
	}

}
