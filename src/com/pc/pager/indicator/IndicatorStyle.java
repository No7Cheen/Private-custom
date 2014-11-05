/**
 * @(#)IndicatorStyle.java 2014-1-18 Copyright 2014 it.kedacom.com, Inc. All
 *                         rights reserved.
 */

package com.pc.pager.indicator;


/**
 * @author chenjian
 * @date 2014-1-18
 */

public enum IndicatorStyle {
	None(0), Triangle(1), Underline(2);

	public final int value;

	private IndicatorStyle(int value) {
		this.value = value;
	}

	public static IndicatorStyle fromValue(int value) {
		for (IndicatorStyle style : IndicatorStyle.values()) {
			if (style.value == value) {
				return style;
			}
		}
		return null;
	}
}
