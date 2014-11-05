/**
 * @(#)HashCodeHelper.java 2014-3-18 Copyright 2014 it.kedacom.com, Inc. All
 *                         rights reserved.
 */

package com.pc.utils;

import java.util.Arrays;

/**
 * 一个链式调用的通用hashCode生成器<br>
 * <a href="http://my.oschina.net/arthor" target="_blank"
 * rel="nofollow">@author</a> hongze.chi@gmail.com
 * @author chenjian
 * @date 2014-3-18
 */

public class HashCodeHelper {

	private static final int multiplierNum = 31;

	private int hashCode;

	private HashCodeHelper() {
		this(17);
	}

	private HashCodeHelper(int hashCode) {
		this.hashCode = hashCode;
	}

	public static HashCodeHelper getInstance() {
		return new HashCodeHelper();
	}

	public static HashCodeHelper getInstance(int hashCode) {
		return new HashCodeHelper(hashCode);
	}

	public HashCodeHelper appendByte(byte val) {
		return appendInt(val);
	}

	public HashCodeHelper appendShort(short val) {
		return appendInt(val);
	}

	public HashCodeHelper appendChar(char val) {
		return appendInt(val);
	}

	public HashCodeHelper appendLong(long val) {
		return appendInt((int) (val ^ (val >>> 32)));
	}

	public HashCodeHelper appendFloat(float val) {
		return appendInt(Float.floatToIntBits(val));
	}

	public HashCodeHelper appendDouble(double val) {
		return appendLong(Double.doubleToLongBits(val));
	}

	public HashCodeHelper appendBoolean(boolean val) {
		return appendInt(val ? 1 : 0);
	}

	public HashCodeHelper appendObj(Object... val) {
		return appendInt(Arrays.deepHashCode(val));
	}

	public HashCodeHelper appendInt(int val) {
		hashCode = hashCode * multiplierNum + val;
		return this;
	}

	public int getHashCode() {
		return this.hashCode;
	}
}
