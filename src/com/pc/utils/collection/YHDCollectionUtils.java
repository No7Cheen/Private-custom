/**
 * @(#)YHDCollectionUtils.java 2014-4-16 Copyright 2014 it.kedacom.com, Inc. All
 *                             rights reserved.
 */

package com.pc.utils.collection;

import java.util.Collection;
import java.util.List;

/**
 * @author chenjian
 * @date 2014-4-16
 */

public class YHDCollectionUtils {

	// public static final Collection NULL_COLLECTION = new NullCollection();

	public static final <T> Collection<T> nullCollection() {
		return (List<T>) new NullCollection();
	}
}
