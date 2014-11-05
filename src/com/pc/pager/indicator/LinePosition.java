/**
 * @(#)LinePosition.java 2014-1-18 Copyright 2014 it.kedacom.com, Inc. All
 *                       rights reserved.
 */

package com.pc.pager.indicator;


/**
 * @author chenjian
 * @date 2014-1-18
 */

public enum LinePosition {
	Bottom(0), Top(1);

	public final int value;

	private LinePosition(int value) {
		this.value = value;
	}

	public static LinePosition fromValue(int value) {
		for (LinePosition position : LinePosition.values()) {
			if (position.value == value) {
				return position;
			}
		}
		return null;
	}
}
