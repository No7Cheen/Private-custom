/**
 * @(#)PcCacheConstant.java   2014-7-18
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.cache;

/**
  * 
  * @author chenj
  * @date 2014-7-18
  */

public class PcCacheConstant {

	// LruCache通过构造函数传入缓存值，以KB为单位。
	public static final int maxMemory;

	public static final int defSize;

	// 缓存空间大小8MB
	public static final int cacheSize;

	static {
		maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

		// 使用最大可用内存值的1/8作为缓存的大小
		defSize = maxMemory / 8;

		cacheSize = defSize >= 8 * 1024 * 1024 ? defSize : 8 * 1024 * 1024;
	}

}
