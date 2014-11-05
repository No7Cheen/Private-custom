/**
 * @(#)CustomClipLoading.java 2014-1-16 Copyright 2014 it.kedacom.com, Inc. All
 *                            rights reserved.
 */

package com.pc.ui.cloading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.privatecustom.publiclibs.R;

/**
 * @author chenjian
 * @date 2014-1-16
 */

public class CustomClipLoading extends FrameLayout {

	private final int Wat = 0x123;
	private final int MAX_PROGRESS = 10000;

	private int mProgress;
	private boolean running;
	private ClipDrawable mClipDrawable;

	public CustomClipLoading(Context context) {
		this(context, null, 0);
	}

	public CustomClipLoading(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomClipLoading(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init(context);
	}

	private void init(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.ac__loading_clip_layout, null);
		addView(view);
		ImageView imageView = (ImageView) findViewById(R.id.iv_progress);
		mClipDrawable = (ClipDrawable) imageView.getDrawable();
	}

	/**
	 * update clip level
	 * @param scale
	 */
	public void updateClip(float scale) {
		if (scale < 0) {
			mProgress = 0;
		} else if (scale > 1) {
			mProgress = MAX_PROGRESS;
		} else {
			mProgress = (int) (MAX_PROGRESS * scale);
			if (mProgress < 0) mProgress = 0;
			if (mProgress > MAX_PROGRESS) mProgress = MAX_PROGRESS;
		}

		mHandler.sendEmptyMessage(Wat);
	}

	/**
	 * 开始裁剪，用于自动裁剪
	 */
	public void startClip() {
		server = Executors.newFixedThreadPool(1);
		server.submit(run);
		server.shutdown();
	}

	/**
	 * 停止裁剪，用于自动裁剪
	 */
	public void stopClip() {
		mHandler.removeMessages(Wat);

		mProgress = 0;
		running = false;

		if (null != server) {
			server.shutdownNow();
		}
	}

	/**
	 * 循环裁剪，用于自动裁剪
	 * @param isLoop
	 */
	public void setLoopClip(boolean isLoop) {
		this.isLoop = isLoop;
	}

	private boolean isLoop;
	private ExecutorService server;

	private Runnable run = new Runnable() {

		@Override
		public void run() {
			running = true;

			while (running) {
				mProgress += 100;

				if (mProgress < 0) mProgress = 0;
				if (mProgress > MAX_PROGRESS) mProgress = MAX_PROGRESS;

				mHandler.sendEmptyMessage(Wat);
				try {
					Thread.sleep(18);
				} catch (InterruptedException e) {
				}
			}
		}
	};

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what != Wat) {
				return;
			}

			mClipDrawable.setLevel(mProgress);
			if (mProgress == MAX_PROGRESS) {
				stopClip();
				if (isLoop) startClip();
			}
		}
	};

}
