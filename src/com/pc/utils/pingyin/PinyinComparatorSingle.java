package com.pc.utils.pingyin;

import java.util.Comparator;

/**
 * 拼音比较
 * 
 * @author chenj
 * @date 2014-7-31
 */
public class PinyinComparatorSingle implements Comparator<Object> {

	@Override
	public int compare(Object o1, Object o2) {
		String str1 = PingYinUtil.getPingYin(o1.toString());
		String str2 = PingYinUtil.getPingYin(o2.toString());

		return str1.compareTo(str2);
	}

}
