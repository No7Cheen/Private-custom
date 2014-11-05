package com.pc.imgs.download;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.pc.cache.PcCacheConstant;
import com.pc.utils.StringUtils;
import com.pc.utils.encryption.PcMd5;
import com.pc.utils.imgs.EmImageOperationType;
import com.pc.utils.log.PcLog;

/**
 * 图片缓存
 */
public class PcImageDownloadCache {

	// 锁对象
	public static final ReentrantLock lock = new ReentrantLock();

	// 正在下载中的线程
	private static final HashMap<String, Runnable> runRunnableCache = new HashMap<String, Runnable>();

	// 等待中的线程
	private static final List<HashMap<String, Runnable>> waitRunnableList = new ArrayList<HashMap<String, Runnable>>();

	// 为了加快速度，在内存中开启缓存,使用最新的LruCache
	private static final LruCache<String, Bitmap> bitmapCache = new LruCache<String, Bitmap>(PcCacheConstant.cacheSize) {

		protected int sizeOf(String key, Bitmap bitmap) {
			return bitmap.getRowBytes() * bitmap.getHeight();
		}

		@Override
		protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
			if (PcLog.isPrint) {
				PcLog.d(PcImageDownloadCache.class.getSimpleName(), "LruCache:移除了" + key);
			}
		}

	};

	/**
	 * 从缓存中获取这个Bitmap
	 * 
	 * @param key the key
	 */
	public static Bitmap getBitmapFromCache(String key) {
		return bitmapCache.get(key);
	}

	/**
	 * 增加一个图片到缓存
	 * 
	 * @param key
	 */
	public static void addBitmapToCache(String key, Bitmap bitmap) {
		try {
			lock.lock();
			if (StringUtils.isNull(key)) {
				return;
			}

			if (getBitmapFromCache(key) == null && bitmap != null) {
				bitmapCache.put(key, bitmap);
			}

			// 表示下载中的缓存清除
			removeRunRunnableFromCache(key);

			if (PcLog.isPrint) {
				PcLog.d(PcImageDownloadCache.class.getSimpleName(), "检查挂起线程:" + waitRunnableList.size());
			}

			// 唤醒等待线程并移除列表
			removeWaitRunnableFromCache(key);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 从缓存删除
	 * 
	 * @param key 通过url计算的缓存key
	 */
	public static void removeBitmapFromCache(String key) {
		try {
			lock.lock();
			if (getBitmapFromCache(key) != null) {
				bitmapCache.remove(key);
			}
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 清空缓存的Bitmap
	 */
	public static void removeAllBitmapFromCache() {
		bitmapCache.evictAll();
	}

	public static String getCacheKey(String url) {
		return getCacheKey(url, 0, 0, EmImageOperationType.ORIGINALIMG);
	}

	/**
	 * 根据url计算缓存key,这个key+后缀就是文件名
	 * 
	 * @param url 
	 * @param width 
	 * @param height 
	 * @param type 处理类型
	 */
	public static String getCacheKey(String url, int width, int height, EmImageOperationType type) {
		if (StringUtils.isNull(url)) {
			return "";
		}

		if (width <= 0 || height <= 0) {
			return PcMd5.MD5(new StringBuilder(url.length() + 12).append(url).toString());
		}

		if (null == type || type == EmImageOperationType.ORIGINALIMG) {
			return PcMd5.MD5(new StringBuilder(url.length() + 12).append("#W").append(width).append("#H").append(height).append(url).toString());
		}

		return PcMd5.MD5(new StringBuilder(url.length() + 12).append("#W").append(width).append("#H").append(height).append("#T").append(type.ordinal()).append(url).toString());
	}

	/**
	 * 从缓存中获取这个正在执行线程
	 *
	 * @param key
	 * @return the runnable
	 */
	public static Runnable getRunRunnableFromCache(String key) {
		return runRunnableCache.get(key);
	}

	/**
	 * 增加一个正在执行线程的记录
	 * 
	 * @param key 
	 * @param runnable the runnable
	 */
	public static void addToRunRunnableCache(String key, Runnable runnable) {
		try {
			lock.lock();
			if (StringUtils.isNull(key) || runnable == null) {
				return;
			}

			if (getRunRunnableFromCache(key) == null) {
				runRunnableCache.put(key, runnable);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 移除缓存一个正在执行的线程
	 * 
	 * @param key
	 */
	public static void removeRunRunnableFromCache(String key) {
		if (null == getRunRunnableFromCache(key)) {
			return;
		}

		runRunnableCache.remove(key);
	}

	/**
	 * 从缓存中获取这个正在等待线程
	 * 
	 * @param key
	 * @return the runnable
	 */
	public static Runnable getWaitRunnableFromCache(String key) {
		return runRunnableCache.get(key);
	}

	/**
	 * 增加一个等待线程的记录
	 * 
	 * @param key
	 * @param runnable the runnable
	 */
	public static void addToWaitRunnableCache(String key, Runnable runnable) {
		try {
			lock.lock();
			if (StringUtils.isNull(key) || runnable == null) {
				return;
			}

			HashMap<String, Runnable> runnableMap = new HashMap<String, Runnable>();
			runnableMap.put(key, runnable);
			waitRunnableList.add(runnableMap);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 从缓存删除一个等待线程
	 * 
	 * @param key
	 */
	public static void removeWaitRunnableFromCache(String key) {
		try {
			lock.lock();

			for (int i = 0; i < waitRunnableList.size(); i++) {
				HashMap<String, Runnable> runnableMap = waitRunnableList.get(i);
				Runnable runnable = runnableMap.get(key);
				if (runnable != null) {
					if (PcLog.isPrint) {
						PcLog.d(PcImageDownloadCache.class.getSimpleName(), "从缓存删除并唤醒:" + runnable);
					}

					synchronized (runnable) {
						runnable.notify();
					}
					waitRunnableList.remove(runnableMap);
					i--;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

}
