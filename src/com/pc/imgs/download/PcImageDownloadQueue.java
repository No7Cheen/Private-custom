package com.pc.imgs.download;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.Message;

import com.pc.utils.StringUtils;
import com.pc.utils.log.PcLog;

/**
 * 队列下载,图片下载线程， 先检查SD卡是否存在相同文件，不存在则下载，最后再从SD卡中读取
 * 
 * @author chenj
 * @date 2014-7-17
 */
public class PcImageDownloadQueue extends Thread {

	// 下载队列
	private List<PcImageDownloadItem> queue;

	// 控制释放
	private static boolean stop = false;

	// 下载完成后的消息句柄
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

	// 图片下载线程单例类
	private static PcImageDownloadQueue imageDownloadThread;

	private PcImageDownloadQueue() {
		stop = false;
		queue = new ArrayList<PcImageDownloadItem>();
	}

	/**
	 * 单例构造图片下载线程.
	 * @return single instance of AbImageDownloadQueue
	 */
	public static PcImageDownloadQueue getInstance() {
		if (imageDownloadThread == null) {
			imageDownloadThread = new PcImageDownloadQueue();
			imageDownloadThread.setName("PcImageDownloadQueue-1");
			// 创建后立刻运行
			imageDownloadThread.start();
		}

		return imageDownloadThread;
	}

	/**
	 * 开始一个下载任务
	 *
	 * @param item
	 */
	public void download(PcImageDownloadItem item) {
		if (null == item) {
			return;
		}

		// 检查图片路径
		String imageUrl = item.imageUrl;
		if (StringUtils.isNull(imageUrl)) {
			if (PcLog.isPrint) {
				PcLog.d(PcImageDownloadQueue.class.getSimpleName(), "图片URL为空，请先判断");
			}

			return;
		}

		imageUrl = StringUtils.trim(imageUrl);

		// 从缓存中获取这个Bitmap.
		String cacheKey = PcImageDownloadCache.getCacheKey(imageUrl, item.width, item.height, item.type);
		item.bitmap = PcImageDownloadCache.getBitmapFromCache(cacheKey);
		if (item.bitmap == null) {
			addDownloadItem(item);

			return;
		}

		if (item.listener != null) {
			if (PcLog.isPrint) {
				PcLog.d(PcImageDownloadQueue.class.getSimpleName(), "从内存缓存中得到图片:" + cacheKey + "," + item.bitmap);
			}

			Message msg = handler.obtainMessage();
			msg.obj = item;
			handler.sendMessage(msg);
		}
	}

	/**
	 * 添加到图片下载线程队列
	 *
	 * @param item
	 */
	private synchronized void addDownloadItem(PcImageDownloadItem item) {
		if (null == item) {
			return;
		}

		queue.add(item);

		// 添加了下载项就激活本线程
		this.notify();
	}

	/**
	 * 开始一个下载任务并清除原来队列
	 * 
	 * @param item
	 */
	public void downloadBeforeClean(PcImageDownloadItem item) {
		if (null == item) {
			return;
		}

		queue.clear();
		addDownloadItem(item);
	}

	/**
	 * 线程运行
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while (!stop) {
			try {
				if (PcLog.isPrint) {
					PcLog.d(PcImageDownloadQueue.class.getSimpleName(), "图片下载队列大小：" + queue.size());
				}

				while (queue.size() > 0) {
					PcImageDownloadItem item = queue.remove(0);

					// 缓存图片路径
					String cacheKey = PcImageDownloadCache.getCacheKey(item.imageUrl, item.width, item.height, item.type);

					// 判断这个任务是否有其他线程在执行，如果有等待，直到下载完成唤醒显示
					Runnable runnable = PcImageDownloadCache.getRunRunnableFromCache(cacheKey);
					if (runnable != null) {
						if (PcLog.isPrint) {
							PcLog.d(PcImageDownloadQueue.class.getSimpleName(), "等待:" + cacheKey + "," + item.imageUrl);
						}

						PcImageDownloadCache.addToWaitRunnableCache(cacheKey, this);
						synchronized (this) {
							this.wait();
						}

						if (PcLog.isPrint) {
							PcLog.d(PcImageDownloadQueue.class.getSimpleName(), "醒了:" + item.imageUrl);
						}

						// 直接获取
						item.bitmap = PcImageDownloadCache.getBitmapFromCache(cacheKey);
					} else {
						// 增加下载中的线程记录
						if (PcLog.isPrint) {
							PcLog.d(PcImageDownloadQueue.class.getSimpleName(), "增加图片下载中:" + cacheKey + "," + item.imageUrl);
						}

						PcImageDownloadCache.addToRunRunnableCache(cacheKey, this);
						item.bitmap = PcImageFileUtil.getBitmapFromSDCache(item.imageUrl, item.type, item.width, item.height, item.dir);

						// 增加到下载完成的缓存，删除下载中的记录和等待的记录，同时唤醒所有等待列表中key与其key相同的线程
						PcImageDownloadCache.addBitmapToCache(cacheKey, item.bitmap);
					}

					// 需要执行回调来显示图片
					if (item.listener != null) {
						// 交由UI线程处理
						Message msg = handler.obtainMessage();
						msg.obj = item;
						handler.sendMessage(msg);
					}

				}

				// 停止
				if (stop) {
					queue.clear();
					return;
				}

				// 没有下载项时等待
				synchronized (this) {
					this.wait();
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		}
	}

	/**
	 * 终止队列释放线程
	 * @throws
	 */
	public void stopQueue() {
		try {
			stop = true;
			imageDownloadThread = null;
			this.interrupt();
		} catch (Exception e) {
		}
	}

}
