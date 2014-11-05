package com.pc.imgs.download;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import android.os.Handler;
import android.os.Message;

import com.pc.utils.StringUtils;
import com.pc.utils.android.sys.TerminalUtils;
import com.pc.utils.log.PcLog;

/**
 * 线程池图片下载
 */

public class PcImageDownloadPool {

	private static PcImageDownloadPool imageDownload;

	// 固定3个线程来执行任务
	private static int nThreads = 3;

	// The executor service
	private ExecutorService executorService;

	/**
	 * 下载完成后的消息句柄
	 */
	private static Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (null == msg) {
				return;
			}

			PcImageDownloadItem item = (PcImageDownloadItem) msg.obj;
			if (null == item) {
				return;
			}

			item.listener.update(item.bitmap, item.imageUrl);
		}
	};

	/**
	 * 构造图片下载器
	 * 
	 * @param nThreads the n threads
	 */
	protected PcImageDownloadPool(int nThreads) {
		executorService = Executors.newFixedThreadPool(nThreads);
	}

	/**
	 * 单例构造图片下载器
	 * 
	 * @return single instance of AbImageDownloadPool
	 */
	public static PcImageDownloadPool getInstance() {
		if (imageDownload == null) {
			nThreads = TerminalUtils.getNumCores();
			if (nThreads <= 0) {
				nThreads = 1;
			}
			imageDownload = new PcImageDownloadPool(nThreads * 3);
		}

		return imageDownload;
	}

	/**
	 * Download
	 * 
	 * @param item the item
	 */
	public void download(final PcImageDownloadItem item) {
		String imageUrl = item.imageUrl;
		if (StringUtils.isNull(imageUrl)) {
			if (PcLog.isPrint) {
				PcLog.d(PcImageDownloadPool.class.getSimpleName(), "图片URL为空");
			}
		} else {
			imageUrl = imageUrl.trim();
		}

		// 从缓存中获取图片
		final String cacheKey = PcImageDownloadCache.getCacheKey(imageUrl, item.width, item.height, item.type);
		item.bitmap = PcImageDownloadCache.getBitmapFromCache(cacheKey);

		// 缓存中有图像,直接返回显示
		if (null != item.bitmap) {
			if (item.listener != null) {
				Message msg = handler.obtainMessage();
				msg.obj = item;
				handler.sendMessage(msg);
			}

			return;
		}

		// 缓存中没有图像，则从网络上取出数据，并将取出的数据缓存到内存中
		executorService.submit(new Runnable() {

			public void run() {
				try {
					// 判断这个任务是否有其他线程在执行，如果有等待，直到下载完成唤醒显示
					Runnable runnable = PcImageDownloadCache.getRunRunnableFromCache(cacheKey);
					if (runnable != null) {// 线程等待通知后显示
						PcImageDownloadCache.addToWaitRunnableCache(cacheKey, this);
						synchronized (this) {
							this.wait();
						}

						// 直接获取
						item.bitmap = PcImageDownloadCache.getBitmapFromCache(cacheKey);
					} else {
						// 增加下载中的线程记录
						PcImageDownloadCache.addToRunRunnableCache(cacheKey, this);
						item.bitmap = PcImageFileUtil.getBitmapFromSDCache(item.imageUrl, item.type, item.width, item.height, item.dir);

						// 增加到下载完成的缓存，删除下载中的记录和等待的记录，同时唤醒所有等待列表中key与其key相同的线程
						PcImageDownloadCache.addBitmapToCache(cacheKey, item.bitmap);
					}
				} catch (Exception e) {
					if (PcLog.isPrint) {
						PcLog.d(PcImageDownloadPool.class.getSimpleName(), item.imageUrl, e);
					} else {
						e.printStackTrace();
					}
				} finally {
					if (item.listener != null) {
						Message msg = handler.obtainMessage();
						msg.obj = item;
						handler.sendMessage(msg);
					}
				}
			}
		});
	}

	/**
	 * 立即关闭.
	 */
	public void shutdownNow() {
		if (null == executorService) {
			return;
		}

		if (!executorService.isTerminated()) {
			executorService.shutdownNow();
			listenShutdown();
		}

	}

	/**
	 * 平滑关闭.
	 */
	public void shutdown() {
		if (null == executorService) {
			return;
		}

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
				if (PcLog.isPrint) {
					PcLog.d(PcImageDownloadPool.class.getSimpleName(), "线程池未关闭");
				}
			}

			if (PcLog.isPrint) {
				PcLog.d(PcImageDownloadPool.class.getSimpleName(), "线程池已关闭");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
