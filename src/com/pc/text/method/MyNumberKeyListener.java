/**
 * @(#)MyNumberKeyListener.java 2013-11-7 Copyright 2013 it.kedacom.com, Inc.
 *                              All rights reserved.
 */

package com.pc.text.method;

import android.text.method.NumberKeyListener;

/**
 * For numeric text entry
 * 
 * @author chenjian
 * @date 2013-11-7
 */

public abstract class MyNumberKeyListener extends NumberKeyListener {

	private int mInputType;
	private String mAcceptedChars;

	public MyNumberKeyListener(int _InputType, String _AcceptedChars) {
		mInputType = _InputType;
		mAcceptedChars = _AcceptedChars;
	}

	/**
	 * 指定键盘类型
	 * 
	 * @see android.text.method.KeyListener#getInputType()
	 */
	@Override
	public int getInputType() {
		return mInputType;
	}

	/**
	 * 指定你所接受的字符
	 * 
	 * @see android.text.method.NumberKeyListener#getAcceptedChars()
	 */
	@Override
	protected char[] getAcceptedChars() {
		try {
			return mAcceptedChars.toCharArray();
		} catch (Exception e) {
		}

		return null;
	}

}
