package com.pc.cache;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Bitmap;
import android.util.Log;

import com.pc.utils.StringUtils;

/**
 * 缓存器
 * @author jsl
 * @date 2013-8-22
 */
public class MemoryCache {

	private Map<String, Bitmap> cache = Collections.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));// Last
																														// argument
																														// true
																														// for
																														// LRU
																														// ordering
	private long size = 0;// current allocated size
	private long limit = 4 * 1024 * 1024;// max memory in bytes

	public MemoryCache() {
		// use 25% of available heap size
		// setLimit(Runtime.getRuntime().maxMemory() / 4);
	}

	/**
	 * 设置缓存大小
	 * @param new_limit
	 */
	public void setLimit(long new_limit) {
		limit = new_limit;
		// Log.i(TAG, "MemoryCache will use up to " + limit / 1024. / 1024. +
		// "MB");
	}

	public Bitmap get(String id) {
		if (StringUtils.isNull(id)) {
			return null;
		}
		try {
			if (!cache.containsKey(id)) {
				return null;
			}
			// NullPointerException sometimes happen here
			// http://code.google.com/p/osmdroid/issues/detail?id=78
			return cache.get(id);
		} catch (NullPointerException ex) {
			Log.e("Exception", "MemoryCache get ", ex);
			return null;
		}
	}

	public void put(String id, Bitmap bitmap) {
		if (StringUtils.isNull(id)) {
			return;
		}
		try {
			if (cache != null && cache.containsKey(id)) {
				size -= getSizeInBytes(cache.get(id));
			}
			cache.put(id, bitmap);
			size += getSizeInBytes(bitmap);
			checkSize();
		} catch (Throwable th) {
			Log.e("Exception", "MemoryCache put ", th);
		}
	}

	public void remove(String id) {
		if (StringUtils.isNull(id)) {
			return;
		}
		try {
			if (cache != null && cache.containsKey(id)) {
				cache.remove(id);
			}
		} catch (Throwable th) {
			Log.e("Exception", "MemoryCache remove ", th);
		}
	}

	private void checkSize() {
		// Log.i(TAG, "cache size=" + size + " length=" + cache.size());
		if (size > limit) {
			Iterator<Entry<String, Bitmap>> iter = cache.entrySet().iterator();// least
																				// recently
																				// accessed
																				// item
																				// will
																				// be
																				// the
																				// first
																				// one
																				// iterated
			while (iter.hasNext()) {
				Entry<String, Bitmap> entry = iter.next();
				size -= getSizeInBytes(entry.getValue());
				iter.remove();
				if (size <= limit) break;
			}
			// Log.i(TAG, "Clean cache. New size " + cache.size());
		}
	}

	public void clear() {
		try {
			// NullPointerException sometimes happen here
			// http://code.google.com/p/osmdroid/issues/detail?id=78
			if (cache != null) {
				cache.clear();
			}
			size = 0;
		} catch (NullPointerException ex) {
			Log.e("Exception", "MemoryCache clear ", ex);
		}
	}

	private long getSizeInBytes(Bitmap bitmap) {
		if (bitmap == null) {
			return 0;
		}
		return bitmap.getRowBytes() * bitmap.getHeight();
	}
}
