/**
 * @(#)PhoneNumberUtils.java   2014-6-4
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chenj
 * @date 2014-6-4
 */

public class PhoneNumUtils {

	// 用于匹配手机号码
	private final static String REGEX_MOBILEPHONE = "^0?1[3458]\\d{9}$";

	// 用于匹配固定电话号码
	private final static String REGEX_FIXEDPHONE = "^(010|02\\d|0[3-9]\\d{2})?\\d{6,8}$";

	// 用于获取固定电话中的区号
	private final static String REGEX_ZIPCODE = "^(010|02\\d|0[3-9]\\d{2})\\d{6,8}$";

	/**
	 * 是否是电话号码
	 * @param phoneNum
	 * @return
	 */
	public static boolean isPhoneNum(String phoneNum) {
		if (StringUtils.isNull(phoneNum)) {
			return false;
		}

		if (isCellphone4AreaCode(phoneNum)) {
			return true;
		}

		return isFixedPhone(phoneNum);
	}

	/**
	 * 判断是否为手机号码(可以带有国家区号)
	 * @param cellphone
	 * @return
	 */
	public static boolean isCellphone4AreaCode(String cellphone) {
		String checkcellphone = "^[+,0-9]{0,4}(13[0-9]|15[0-9]|18[0-9])[0-9]{8}$";
		return ValidateUtils.validate(checkcellphone, cellphone);
	}

	/**
	 * 判断是否为手机号码
	 * @param number 手机号码
	 * @return
	 */
	public static boolean isCellphone(String number) {
		return ValidateUtils.validate(REGEX_MOBILEPHONE, number);
	}

	/**
	 * 判断是否为手机号码
	 * @param cellphone
	 * @return
	 */
	public static boolean isCellphone2(String cellphone) {
		String checkcellphone = "^(13[0-9]|15[0-9]|18[0-9])[0-9]{8}$";
		return ValidateUtils.validate(checkcellphone, cellphone);
	}

	/**
	 * 判断是否为固定电话号码
	 * @param number 固定电话号码
	 * @return
	 */
	public static boolean isFixedPhone(String number) {
		return ValidateUtils.validate(REGEX_FIXEDPHONE, number);
	}

	/**
	 * 获取固定号码号码中的区号
	 * @param strNumber
	 * @return
	 */
	public static String getZipFromHomephone(String strNumber) {
		Matcher matcher = Pattern.compile(REGEX_ZIPCODE).matcher(strNumber);
		if (matcher.find()) {
			return matcher.group(1);
		}

		return null;
	}

	/**
	 * 检查号码类型，并获取号码前缀，手机获取前7位，固话获取区号
	 * @param number
	 * @return
	 */
	public static PhoneNumber checkNumber(String _number) {
		String number = _number;
		PhoneNumber rtNum = null;

		if (number != null && number.length() > 0) {
			if (isCellphone(number)) {
				// 如果手机号码以0开始，则去掉0
				if (number.charAt(0) == '0') {
					number = number.substring(1);
				}

				rtNum = new PhoneNumber(PhoneType.CELLPHONE, number.substring(0, 7), _number);
			} else if (isFixedPhone(number)) {
				// 获取区号
				String zipCode = getZipFromHomephone(number);
				rtNum = new PhoneNumber(PhoneType.FIXEDPHONE, zipCode, _number);
			} else {
				rtNum = new PhoneNumber(PhoneType.INVALIDPHONE, null, _number);
			}
		}

		return rtNum;
	}

}
