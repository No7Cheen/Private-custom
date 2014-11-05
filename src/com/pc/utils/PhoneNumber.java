/**
 * @(#)PhoneNumber.java   2014-6-4
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.utils;

/**
 * @author chenj
 * @date 2014-6-4
 */

public class PhoneNumber {

	private PhoneType type;
	/**
	 * 如果是手机号码，则该字段存储的是手机号码 前七位；如果是固定电话，则该字段存储的是区号
	 */
	private String code;
	private String number;

	public PhoneNumber(PhoneType _type, String _code, String _number) {
		this.type = _type;
		this.code = _code;
		this.number = _number;
	}

	public PhoneType getType() {
		return type;
	}

	public String getCode() {
		return code;
	}

	public String getNumber() {
		return number;
	}

	public String toString() {
		return String.format("[number:%s, type:%s, code:%s]", number, type.name(), code);
	}
}
