/**
 * @(#)PinyinComparator2.java 2013-7-19 Copyright 2013 it.kedacom.com, Inc. All
 *                            rights reserved.
 */

package com.pc.utils.pingyin;

import java.util.Comparator;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * 拼音比较
 * 
 * @author chenj
 * @date 2013-7-19
 */

public class PinyinComparator implements Comparator<String> {

	// 忽略大小写
	private boolean mIgnoreCase;

	public PinyinComparator() {
		mIgnoreCase = false;
	}

	public PinyinComparator(boolean _IgnoreCase) {
		mIgnoreCase = _IgnoreCase;
	}

	/**
	 * 字符串以拼音方式比较
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(String o1, String o2) {
		if (mIgnoreCase) {
			if (o1 != null && o1.length() > 0) {
				o1 = o1.toLowerCase();
			}

			if (o2 != null && o2.length() > 0) {
				o2 = o2.toLowerCase();
			}
		}

		for (int i = 0; i < o1.length() && i < o2.length(); i++) {
			int codePoint1 = o1.charAt(i);
			int codePoint2 = o2.charAt(i);

			if (Character.isSupplementaryCodePoint(codePoint1) || Character.isSupplementaryCodePoint(codePoint2)) {
				i++;
			}

			if (codePoint1 != codePoint2) {
				if (Character.isSupplementaryCodePoint(codePoint1) || Character.isSupplementaryCodePoint(codePoint2)) {
					return codePoint1 - codePoint2;
				}

				String pinyin1 = pinyin((char) codePoint1);
				String pinyin2 = pinyin((char) codePoint2);

				if (pinyin1 != null && pinyin2 != null) { // 两个字符都是汉字
					if (!pinyin1.equals(pinyin2)) {
						if (mIgnoreCase) {
							return pinyin1.compareToIgnoreCase(pinyin2);
						} else {
							return pinyin1.compareTo(pinyin2);
						}
					}
				} else {
					return codePoint1 - codePoint2;
				}
			}
		}

		return o1.length() - o2.length();
	}

	/**
	 * 字符的拼音，多音字就得到第一个拼音;不是汉字，就return null
	 *
	 * @param c 字符
	 * @return
	 */
	private String pinyin(char c) {
		String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(c);
		if (pinyins == null) {
			return null;
		}

		return pinyins[0];
	}

}
