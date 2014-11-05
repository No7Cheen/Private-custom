/**
 * @(#)PcLruCacheImgMananger.java   2014-7-18
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.cache;

import java.util.concurrent.locks.ReentrantLock;

import android.graphics.Bitmap;

/**
  * 
  * @author chenj
  * @date 2014-7-18
  */

public class PcLruCacheImgMananger {

	public static final ReentrantLock lock = new ReentrantLock();

	private static PcLruCacheImg mPcLruCacheImg;
	static {
		mPcLruCacheImg = new PcLruCacheImg();
	}

	public static PcLruCacheImg getPcLruCacheImg() {
		return mPcLruCacheImg;
	}

	/**
	 * 从缓存获取Bitmap
	 * 
	 * @param key
	 * @return
	 */
	public Bitmap getCacheImg(final String key) {
		try {
			return mPcLruCacheImg.getCacheImg(key);
		} catch (Exception e) {
			return null;
		}
	}

	public void putImg(String key, Bitmap bitmap) {
		try {
			lock.lock();
			mPcLruCacheImg.putImg(key, bitmap);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 清除缓存
	 */
	public void clearLruCache() {
		try {
			lock.lock();
			mPcLruCacheImg.clearLruCache();
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}
}
