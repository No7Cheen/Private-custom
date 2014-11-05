/*
 *  Copyright 2010 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.pc.ui.widget.WheelView;

/**
 * 带有间隔的数字 Numeric Wheel adapter.
 */
public class NumericSPaceWheelAdapter implements WheelAdapter
{

	/** The default min value */
	public static final int DEFAULT_MAX_VALUECount = 10;

	/** The default max value */
	private static final int DEFAULT_MIN_VALUE = 0;

	// Values
	private int minValue;
	private int valueCount;

	// format
	private String format;

	private int space = 1; // 默认没有间隔

	/**
	 * Default constructor
	 */
	public NumericSPaceWheelAdapter() {
		this(DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUECount);
	}

	/**
	 * Constructor
	 * 
	 * @param minValue
	 *        the wheel min value
	 * @param valueCount
	 *        the wheel value count
	 */
	public NumericSPaceWheelAdapter(int minValue, int valueCount) {
		this(minValue, valueCount, null);
	}

	public NumericSPaceWheelAdapter(int minValue, int valueCount, int space) {
		this(minValue, valueCount, null, space);
	}

	/**
	 * Constructor
	 * 
	 * @param minValue
	 *        the wheel min value
	 * @param valueCount
	 *        the wheel value count
	 * @param format
	 *        the format string
	 * 
	 */
	public NumericSPaceWheelAdapter(int minValue, int valueCount, String format) {
		this(minValue, valueCount, format, 1);
	}

	/**
	 * Constructor
	 * 
	 * @param minValue
	 *        the wheel min value
	 * @param valueCount
	 *        the wheel value count
	 * @param format
	 *        the format string
	 * @param space
	 */
	public NumericSPaceWheelAdapter(int minValue, int valueCount, String format, int space) {
		this.minValue = minValue;
		this.valueCount = valueCount;
		this.format = format;
		this.space = space;
	}

	@Override
	public String getItem(int index) {
		if (index == 0) { // 最小值
			return format != null ? String.format(format, minValue) : Integer.toString(minValue);
		}

		if (index >= 1 && index < getItemsCount()) {
			int value = minValue + index * space;
			return format != null ? String.format(format, value) : Integer.toString(value);
		}

		return null;
	}

	public int indexOf(int value) {
		int indexof = (value - minValue) / space;
		return indexof;
	}

	@Override
	public int getItemsCount() {
		return valueCount;
	}

	@Override
	public int getMaximumLength() {
		int max = Math.max(Math.abs(minValue + valueCount * space), Math.abs(minValue));
		int maxLen = Integer.toString(max).length();
		if (minValue < 0) {
			maxLen++;
		}
		return maxLen;
	}
}
