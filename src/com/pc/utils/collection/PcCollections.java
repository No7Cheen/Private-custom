/**
 * @(#)PcCollections.java 2014-4-23 Copyright 2014 it.kedacom.com, Inc. All
 *                        rights reserved.
 */

package com.pc.utils.collection;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.pc.utils.StringUtils;
import com.pc.utils.pingyin.PinyinComparator;

/**
 * @author chenjian
 * @date 2014-4-23
 */

public final class PcCollections {

	/**
	 * 排序
	 * @param firstStr
	 * @return
	 */
	public static Comparator<String> sortStr(final String firstStr) {

		return new Comparator<String>() {

			@Override
			public int compare(String lhs, String rhs) {
				if (lhs == null || rhs == null) {
					return -1;
				}

				if (rhs == null || lhs == null) {
					return -1;
				}

				// firstStr排序到第一位
				if (!StringUtils.isNull(firstStr)) {
					if (StringUtils.equals(lhs, firstStr)) {
						return -1;
					}
					if (StringUtils.equals(rhs, firstStr)) {
						return 1;
					}
				}

				try {
					return new PinyinComparator(false).compare(lhs, rhs);
				} catch (Exception e) {
				}

				return 0;
			}
		};
	}

	/**
	* 去除重复元素
	* @param list
	*/
	public static void removeDuplicate(List<Object> list) {
		if (null == list || list.isEmpty()) {
			return;
		}

		Set<Object> set = new HashSet<Object>(list);
		list.clear();
		list.addAll(set);
	}

	public static void removeDuplicate4String(List<String> list) {
		if (null == list || list.isEmpty()) {
			return;
		}

		Set<String> set = new HashSet<String>(list);
		list.clear();
		list.addAll(set);
	}

}
