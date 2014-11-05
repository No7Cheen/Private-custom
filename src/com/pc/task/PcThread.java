package com.pc.task;

import android.os.Handler;
import android.os.Message;

/**
 * 数据下载线程.
 */
public class PcThread extends Thread {

	/** The tag. */
	private static String TAG = "AbHttpThread";

	/** The Constant D. */
	private static final boolean D = true;

	/** 下载单位. */
	public PcTaskItem item = null;

	/** 下载完成后的消息句柄. */
	private static Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			PcTaskItem item = (PcTaskItem) msg.obj;
			item.listener.update();
		}
	};

	/**
	 * 构造下载线程队列.
	 */
	public PcThread() {
	}

	/**
	 * 开始一个下载任务.
	 * @param item 下载单位
	 */
	public void execute(PcTaskItem item) {
		this.item = item;
		this.start();
	}

	/**
	 * 描述：线程运行.
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		if (null == item || null == item.listener) return;

		// 定义了回调
		item.listener.get();
		// 交由UI线程处理
		Message msg = handler.obtainMessage();
		msg.obj = item;
		handler.sendMessage(msg);
	}

}
