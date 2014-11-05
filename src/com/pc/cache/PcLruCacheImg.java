/**
 * @(#)LruCacheImg.java 2013-11-26 Copyright 2013 it.kedacom.com, Inc. All
 *                      rights reserved.
 */

package com.pc.cache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.pc.utils.StringUtils;

/**
 * @author chenjian
 * @date 2013-11-26
 */

public class PcLruCacheImg {

	private LruCache<String, Bitmap> mCacheImg;

	public PcLruCacheImg() {
		this(0);
	}

	public PcLruCacheImg(int cacheSize) {
		initLruCache();
	}

	private void initLruCache() {
		mCacheImg = new LruCache<String, Bitmap>(PcCacheConstant.cacheSize) {

			@Override
			protected int sizeOf(String key, Bitmap value) {
				// 重写此方法来衡量每张图片的大小，默认返回图片数量
				// return value.getRowBytes() * value.getHeight();
				return value.getByteCount() / 1024;
			}

			/**
			 * @see android.support.v4.util.LruCache#entryRemoved(boolean, java.lang.Object, java.lang.Object, java.lang.Object)
			 */
			@Override
			protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
				super.entryRemoved(evicted, key, oldValue, newValue);
			}

			// @Override
			// protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
			// if (evicted && oldValue != null && !oldValue.isRecycled()) {
			// oldValue.recycle();
			// oldValue = null;
			// }
			// }
		};
	}

	/**
	 * 从缓存获取Bitmap
	 * 
	 * @param key
	 * @return
	 */
	public Bitmap getCacheImg(final String key) {
		if (StringUtils.isNull(key)) {
			return null;
		}

		if (mCacheImg == null) {
			return null;
		}

		Bitmap portrait = mCacheImg.get(key);
		if (null == portrait || portrait.isRecycled()) {
			return null;
		}

		return portrait;
	}

	public void putImg(String key, Bitmap bitmap) {
		if (StringUtils.isNull(key)) {
			return;
		}

		if (bitmap == null) {
			return;
		}

		if (bitmap.isRecycled()) {
			return;
		}

		if (mCacheImg != null) {
			mCacheImg.put(key, bitmap);
		}
	}

	/**
	 * 清除缓存
	 */
	public void clearLruCache() {
		if (mCacheImg == null) {
			return;
		}

		mCacheImg.evictAll();
	}

}
