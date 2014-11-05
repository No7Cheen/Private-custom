package com.pc.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.privatecustom.publiclibs.R;

public class SeekBarPlus extends RelativeLayout {

	private SeekBar mSeekbar;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public SeekBarPlus(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public SeekBarPlus(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SeekBarPlus(Context context) {
		super(context);
		init();
	}

	/**
	 * @see android.view.View#onFinishInflate()
	 */

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		mSeekbar = (SeekBar) findViewById(R.id.seekBar);
		final View seekbarSub = findViewById(R.id.seekbar_sub);
		final View seekbarAdd = findViewById(R.id.seekbar_add);
		if (null != seekbarSub) {
			seekbarSub.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int progress = mSeekbar.getProgress() - 10;
					progress = progress > mSeekbar.getMax() ? mSeekbar.getMax() : progress;
					mSeekbar.setProgress(progress);

					if (mOnSeekBarChangeListener != null) {
						mOnSeekBarChangeListener.onSeekbarSub(mSeekbar, progress);
					}
				}
			});
		}

		if (null != seekbarAdd) {
			seekbarAdd.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int progress = mSeekbar.getProgress() + 10;
					progress = progress < 0 ? 0 : progress;
					mSeekbar.setProgress(progress);

					if (mOnSeekBarChangeListener != null) {
						mOnSeekBarChangeListener.onSeekbarAdd(mSeekbar, progress);
					}
				}
			});
		}
	}

	public void init() {
		mSeekbar = (SeekBar) findViewById(R.id.seekBar);
	}

	public void restoreProgress() {
		if (null == mSeekbar) {
			return;
		}

		mSeekbar.setProgress(0);
	}

	private IOnSeekBarChangeListenerPlus mOnSeekBarChangeListener;

	public void setOnSeekbarChangeListener(IOnSeekBarChangeListenerPlus listener) {
		mOnSeekBarChangeListener = listener;
		mSeekbar.setOnSeekBarChangeListener(listener);
	}

}
