package com.pc.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import android.os.Handler;
import android.os.Message;
import android.os.Process;

/**
 * 执行任务线程（按队列执行）. 每个程序只有1个
 * @version v1.0
 */
public class PcTaskQueue extends Thread {

	/** 等待执行的任务. */
	private static List<PcTaskItem> mAbTaskItemList = null;

	/** 单例对象 */
	private static PcTaskQueue mAbTaskQueue = null;

	/** 停止的标记. */
	private boolean mQuit = false;

	/** 执行完成后的消息句柄. */
	private static Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			PcTaskItem item = (PcTaskItem) msg.obj;
			if (item.getListener() instanceof PcTaskListListener) {
				((PcTaskListListener) item.listener).update((List<?>) item.getResult());
			} else if (item.getListener() instanceof PcTaskObjectListener) {
				((PcTaskObjectListener) item.listener).update(item.getResult());
			} else {
				item.listener.update();
			}
		}
	};

	/**
	 * 单例构造.
	 */
	public static PcTaskQueue getInstance() {
		if (mAbTaskQueue == null) {
			mAbTaskQueue = new PcTaskQueue();
		}
		return mAbTaskQueue;
	}

	/**
	 * 构造执行线程队列.
	 * @param context the context
	 */
	public PcTaskQueue() {
		mQuit = false;
		mAbTaskItemList = new ArrayList<PcTaskItem>();
		// 设置优先级
		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
		// 从线程池中获取
		ExecutorService mExecutorService = PcTaskPool.getExecutorService();
		mExecutorService.submit(this);
	}

	/**
	 * 开始一个执行任务.
	 * @param item 执行单位
	 */
	public void execute(PcTaskItem item) {
		addTaskItem(item);
	}

	/**
	 * 开始一个执行任务并清除原来队列.
	 * @param item 执行单位
	 * @param clean 清空之前的任务
	 */
	public void execute(PcTaskItem item, boolean clean) {
		if (clean) {
			if (mAbTaskQueue != null) {
				mAbTaskQueue.quit();
			}
		}
		addTaskItem(item);
	}

	/**
	 * 添加到执行线程队列.
	 * @param item 执行单位
	 */
	private synchronized void addTaskItem(PcTaskItem item) {
		if (mAbTaskQueue == null) {
			mAbTaskQueue = new PcTaskQueue();
			mAbTaskItemList.add(item);
		} else {
			mAbTaskItemList.add(item);
		}
		// 添加了执行项就激活本线程
		this.notify();

	}

	/**
	 * 线程运行.
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while (!mQuit) {
			try {
				while (mAbTaskItemList.size() > 0) {
					PcTaskItem item = mAbTaskItemList.remove(0);
					// 定义了回调
					if (item.listener != null) {
						item.listener.get();
						// 交由UI线程处理
						Message msg = handler.obtainMessage();
						msg.obj = item;
						handler.sendMessage(msg);
					}

					// 停止后清空
					if (mQuit) {
						mAbTaskItemList.clear();
						return;
					}
				}
				try {
					// 没有执行项时等待
					synchronized (this) {
						this.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					// 被中断的是退出就结束，否则继续
					if (mQuit) {
						mAbTaskItemList.clear();
						return;
					}
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 终止队列释放线程.
	 */
	public void quit() {
		mQuit = true;
		interrupted();
		mAbTaskQueue = null;
	}

}
