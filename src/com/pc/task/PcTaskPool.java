package com.pc.task;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import android.os.Handler;
import android.os.Message;

import com.pc.utils.android.sys.TerminalUtils;
import com.pc.utils.log.PcLog;

/**
 * 线程池,程序中只有1个
 * @version v1.0
 */

public class PcTaskPool {

	/** The tag. */
	private static String TAG = PcTaskPool.class.getSimpleName();

	/** 单例对象 The http pool. */
	private static PcTaskPool mAbTaskPool = null;

	/** 固定5个线程来执行任务. */
	private static int nThreads = 5;

	/** 线程执行器. */
	private static ExecutorService executorService = null;

	/**
	 * 初始化线程池
	 */
	static {
		nThreads = TerminalUtils.getNumCores();
		mAbTaskPool = new PcTaskPool(nThreads * 5);
	}

	/**
	 * 构造线程池.
	 * @param nThreads 初始的线程数
	 */
	protected PcTaskPool(int nThreads) {
		executorService = Executors.newFixedThreadPool(nThreads);
	}

	/**
	 * 单例构造下载器.
	 * @return single instance of AbHttpPool
	 */
	public static PcTaskPool getInstance() {
		return mAbTaskPool;
	}

	/**
	 * 执行任务.
	 * @param item the item
	 */
	public void execute(final PcTaskItem item) {
		executorService.submit(new Runnable() {

			public void run() {
				try {
					// 定义了回调
					if (item.listener != null) {
						item.listener.get();
						// 交由UI线程处理
						Message msg = handler.obtainMessage();
						msg.obj = item;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * 获取线程池的执行器
	 * @return executorService
	 * @throws
	 */
	public static ExecutorService getExecutorService() {
		return executorService;
	}

	/**
	 * 立即关闭.
	 */
	public void shutdownNow() {
		if (null == executorService) return;

		if (!executorService.isTerminated()) {
			executorService.shutdownNow();
			listenShutdown();
		}

	}

	/**
	 * 平滑关闭.
	 */
	public void shutdown() {
		if (null == executorService) return;

		if (!executorService.isTerminated()) {
			executorService.shutdown();
			listenShutdown();
		}
	}

	/**
	 * 关闭监听.
	 */
	public void listenShutdown() {
		try {
			while (!executorService.awaitTermination(1, TimeUnit.MILLISECONDS)) {
				if (PcLog.isPrint) PcLog.d(TAG, "线程池未关闭");
			}

			if (PcLog.isPrint) PcLog.d(TAG, "线程池已关闭");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 下载完成后的消息句柄. */
	private static Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (null == msg) return;

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
}
