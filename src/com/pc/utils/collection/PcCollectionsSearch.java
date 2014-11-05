/**
 * @(#)PcCollections.java 2014-4-17 Copyright 2014 it.kedacom.com, Inc. All
 *                        rights reserved.
 */

package com.pc.utils.collection;

import java.util.Arrays;

/**
 * @author chenjian
 * @date 2014-4-17
 */

public class PcCollectionsSearch {

	/**
	 * 2分法查找数组
	 * @param array
	 * @param value
	 * @return
	 */
	public static int binarySearch(int[] array, int value) {
		return binarySearch(array, 0, array.length, value);
	}

	/**
	 * 2分法查找int数组
	 * @param array
	 * @param startIndex
	 * @param endIndex
	 * @param value
	 * @return
	 */
	public static int binarySearch(int[] array, int startIndex, int endIndex, int value) {
		if (null == array || 0 == array.length) {
			return -1;
		}

		Arrays.sort(array);

		return Arrays.binarySearch(array, startIndex, endIndex, value);
	}

	/**
	 * 2分法查找char数组
	 * @param array
	 * @param value
	 * @return
	 */
	public static int binarySearch(char[] array, char value) {
		return binarySearch(array, 0, array.length, value);
	}

	/**
	 * 2分法查找char数组
	 * @param array
	 * @param startIndex
	 * @param endIndex
	 * @param value
	 * @return
	 */
	public static int binarySearch(char[] array, int startIndex, int endIndex, char value) {
		if (null == array || 0 == array.length) {
			return -1;
		}

		Arrays.sort(array);

		return Arrays.binarySearch(array, startIndex, endIndex, value);
	}

}
