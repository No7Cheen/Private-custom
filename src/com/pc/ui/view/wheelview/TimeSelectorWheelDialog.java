package com.pc.ui.view.wheelview;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pc.ui.widget.WheelView.ArrayWheelAdapter;
import com.pc.ui.widget.WheelView.timeselector.OnTimeWheelChangedListener;
import com.pc.ui.widget.WheelView.timeselector.TimeSelectorWheelView;
import com.pc.utils.StringUtils;
import com.pc.utils.android.sys.TerminalUtils;
import com.privatecustom.publiclibs.R;

public class TimeSelectorWheelDialog extends Dialog {

	private final String[] times;

	private TimeSelectorWheelView mSelectorWheelView; // time selector view

	private final String mLabel;
	private final String mTitle;
	private final Context mContext;

	public TimeSelectorWheelDialog(Context context, String label, String title) {
		this(context, label, title, null);
	}

	public TimeSelectorWheelDialog(Context context, String label, String title, String[] _times) {
		super(context);
		mContext = context;

		mLabel = label;
		mTitle = title;
		if (_times != null && _times.length > 0) {
			times = _times;
		} else {
			times = mContext.getResources().getStringArray(R.array.times_four);
		}
	}

	public TimeSelectorWheelDialog(Context context, String label, String title, int theme) {
		this(context, label, title, theme, null);
	}

	public TimeSelectorWheelDialog(Context context, String label, String title, int theme, String[] _times) {
		super(context, theme);
		mContext = context;

		mLabel = label;
		mTitle = title;
		if (_times != null && _times.length > 0) {
			times = _times;
		} else {
			times = mContext.getResources().getStringArray(R.array.times_four);
		}
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		findViews();
		adjustView();

		if (mTitleTextView != null) {
			if (StringUtils.isNull(mTitle)) {
				mTitleTextView.setVisibility(View.INVISIBLE);
			} else {
				mTitleTextView.setText(mTitle);
			}
		}

		setListener();
		initDate();
	}

	private TextView mTitleTextView;

	private void findViews() {
		mSelectorWheelView = (TimeSelectorWheelView) findViewById(R.id.time_selector);
		mTitleTextView = (TextView) findViewById(R.id.title_textview);
	}

	/**
	 * 校正View
	 * @Description
	 */
	@SuppressWarnings("unused")
	private void adjustView() {
		final RelativeLayout rootView = (RelativeLayout) findViewById(R.id.root);
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
		mSelectorWheelView.addChangingListener(mSelectorWheelChangedListener);
		if (okBtnLister != null) {
			findViewById(R.id.ok_btn).setOnClickListener(okBtnLister);
		}
		if (cancelBtnLister != null) {
			findViewById(R.id.cancel_btn).setOnClickListener(cancelBtnLister);
		}
	}

	private View.OnClickListener okBtnLister;
	private View.OnClickListener cancelBtnLister;

	public void setOkBtnLister(View.OnClickListener okBtnLister) {
		this.okBtnLister = okBtnLister;
	}

	public void setCancelBtnLister(View.OnClickListener cancelBtnLister) {
		this.cancelBtnLister = cancelBtnLister;
	}

	/**
	 * Adds changing listener for wheel-hours
	 */
	private OnTimeWheelChangedListener mSelectorWheelChangedListener = new OnTimeWheelChangedListener() {

		@Override
		public void onChanged(TimeSelectorWheelView wheel, int oldValue, int newValue) {
			try {
				mCurrValue = times[newValue];
			} catch (Exception e) {
				mCurrValue = times[0];
			}
		}
	};

	public void initDate() {
		final float scale = mContext.getResources().getDisplayMetrics().density; // 屏幕密度
		int textSize = (int) (20 * scale + 0.5f);// 根据屏幕密度来指定选择器字体的大小

		mSelectorWheelView.TEXT_SIZE = textSize; // 字体大小

		mSelectorWheelView.setAdapter(new ArrayWheelAdapter<String>(times));
		if (!StringUtils.isNull(mLabel)) {
			mSelectorWheelView.setLabel(mLabel);
		}
		mSelectorWheelView.setCyclic(true);
		mSelectorWheelView.setVisibleItems(5);

		// set current value
		// mSelectorWheelView.setCurrentItem(0);
		refreshIndexOfCurrValue();
	}

	/** 当前显示的Value */
	private String mCurrValue;

	public String getCurrValue() {
		return mCurrValue;
	}

	public void setCurrValue(String value) {
		if (null == value || value.length() == 0) {
			return;
		}

		mCurrValue = value;
	}

	public void refreshIndexOfCurrValue() {
		int indexOf = 0;
		if (times != null && times.length >= 0) {
			for (int i = 0; i < times.length; i++) {
				if (StringUtils.equals(times[i], getCurrValue())) {
					indexOf = i;
					break;
				}
			}
		}

		if (mSelectorWheelView != null) {
			mSelectorWheelView.setCurrentItem(indexOf);
		}
	}

	/**
	 * 底部弹出框
	 * @param context
	 * @param currvalue
	 * @param label
	 * @param title
	 * @param times
	 * @param cancelLister
	 * @param okBtnLister
	 * @param cancelBtnLister
	 * @return
	 */
	public static TimeSelectorWheelDialog bottomPoppupDialog4TimeSelector(Context context, String currvalue, String label, String title, String[] times,
			OnCancelListener cancelLister, View.OnClickListener okBtnLister, View.OnClickListener cancelBtnLister) {
		TimeSelectorWheelDialog dialog = new TimeSelectorWheelDialog(context, label, title, R.style.Bottom_Dialog_Theme, times);

		Window window = dialog.getWindow();
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		int[] wh = TerminalUtils.terminalWH(context);
		params.x = 0;
		params.y = wh[1];
		window.setAttributes(params);
		dialog.setContentView(R.layout.timeselector_only);
		dialog.getWindow().setLayout(wh[0], LayoutParams.WRAP_CONTENT);

		if (!StringUtils.isNull(currvalue)) {
			dialog.setCurrValue(currvalue);
			dialog.refreshIndexOfCurrValue();
		}

		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		if (okBtnLister != null) {
			dialog.setOkBtnLister(okBtnLister);
		}
		if (cancelBtnLister != null) {
			dialog.setCancelBtnLister(cancelBtnLister);
		}
		if (cancelLister != null) {
			dialog.setOnCancelListener(cancelLister);
		}

		dialog.show();

		return dialog;
	}

	/**
	 * 底部弹出框
	 * @Description
	 * @param context
	 * @param cancelLister
	 * @return
	 */
	public static TimeSelectorWheelDialog bottomPoppupDialog4TimeSelector(Context context, String currvalue, String label, String title, OnCancelListener cancelLister,
			View.OnClickListener okBtnLister, View.OnClickListener cancelBtnLister) {

		return bottomPoppupDialog4TimeSelector(context, currvalue, label, title, null, cancelLister, okBtnLister, cancelBtnLister);
	}

}
