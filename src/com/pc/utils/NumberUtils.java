/**
 * @(#)IntegerUtils.java 2014-4-11 Copyright 2014 it.kedacom.com, Inc. All
 *                       rights reserved.
 */

package com.pc.utils;

/**
 * @author chenjian
 * @date 2014-4-11
 */

public class NumberUtils {

	public static int bool2Int(boolean b) {
		if (b) {
			return 1;
		}

		return 0;
	}

	public static boolean int2Bool(int i) {
		return i >= 1;
	}

	public static boolean str2Bool(String src) {
		if (StringUtils.isNull(src)) {
			return false;
		}

		return StringUtils.equals(src.toLowerCase(), "true");
	}

	/**
	 * nuber 2 boolean
	 * @param s
	 * @param defaultV
	 * @return >0 true
	 */
	public static boolean str2Boolean(String s, boolean defaultV) {
		if (StringUtils.isNull(s)) return defaultV;

		int num = StringUtils.str2Int(s, 0);
		if (num <= 0) return defaultV;

		return true;
	}
}
