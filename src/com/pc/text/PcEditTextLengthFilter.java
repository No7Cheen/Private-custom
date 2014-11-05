package com.pc.text;

import android.text.InputFilter;
import android.text.Spanned;

import com.pc.utils.StringUtils;

/**
 * EditText Length Filter. 1个英文字符长度为1,1个中文字符长度为2
 * 
 * @author chenj
 * @date 2014-7-31
 */
public class PcEditTextLengthFilter implements InputFilter {

	// 允许的最大长度
	private int nMax;

	public PcEditTextLengthFilter(int max) {
		nMax = max;
	}

	/**
	 * source:新输入字符串
	 * dest:原始字符串
	 * dstart、dend:原始字符串将被替换的起始和结束位置
	 * 
	 * @Description 下面字符串长度均按照1个英文字符长度为1，1个中文字符长度为2来计算
	 * @see android.text.InputFilter#filter(java.lang.CharSequence, int, int, android.text.Spanned, int, int)
	 */
	@Override
	public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
		// int sourceLen = (source == null) ? 0 :
		// StringUtils.lengthByEnglish(source.toString());
		String newSource = (source == null) ? "" : (source.toString()).substring(start, end);
		int newSourceLen = (newSource == null) ? 0 : StringUtils.lengthByEnglish(newSource); // 输入字符串长度
		int destLen = (dest == null) ? 0 : StringUtils.lengthByEnglish(dest.toString()); // 原始字符串长度
		String coverDest = (dest == null) ? "" : (dest.toString()).substring(dstart, dend); // 将会被覆盖的字符串
		int coverLen = (coverDest == null) ? 0 : StringUtils.lengthByEnglish(coverDest); // 将会被覆盖的字符串长度

		// if (hasChinese(dest.toString()) || hasChinese(source.toString())) {
		// nMax = 32;
		// } else {
		// nMax = 64;
		// }

		// int keep = nMax - (dest.length() - (dend - dstart));
		int keep = nMax - (destLen - coverLen);

		if (keep <= 0) {
			return "";
			// } else if (keep >= end - start) {
		} else if (keep >= newSourceLen) {
			return null; // keep original
		} else {
			if ((start + keep) > source.length()) { // keep是按照中文长度为2英文长度为1来计算的，所以这里可能越界
				keep = source.length() - start;
			}
			CharSequence subSequence = source.subSequence(start, start + keep);
			if (subSequence == null || subSequence.toString() == null || subSequence.toString().length() == 0) {
				return "";
			}

			int subLen = StringUtils.lengthByEnglish(subSequence.toString());
			if (subLen == subSequence.length()) { // 全是英文，直接截取
				return source.subSequence(start, start + keep);
			} else if (subLen > subSequence.length()) { // 有中文,重新计算keep值
				char[] c = subSequence.toString().toCharArray();
				int len = 0; // 中文长度+2
				for (int i = 0; i < c.length; i++) {
					if (isChinese(c[i])) {
						len += 2;
					} else {
						len++;
					}
					if (len == keep) {
						keep = i + 1;
						break;
					} else if (len > keep) {
						keep = i;
						break;
					}
				}
			}

			if (keep <= 0) {
				return "";
			}

			return source.subSequence(start, start + keep);
		}

	}

	/**
	 * 是否包含中文字符
	 * 
	 * @Description
	 * @param str
	 * @return
	 */
	public static boolean hasChinese(String str) {
		if (str == null || str.length() == 0) {
			return false;
		}

		char[] c = str.toCharArray();
		for (int i = 0; i < c.length; i++) {
			boolean f = isChinese(c[i]);
			if (f) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 是否是中文字符
	 * 
	 * @Description
	 * @param c
	 * @return
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}

		return false;
	}

}
