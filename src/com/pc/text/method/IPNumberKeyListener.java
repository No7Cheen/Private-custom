/**
 * @(#)E164.java 2013-11-7 Copyright 2013 it.kedacom.com, Inc. All rights
 *               reserved.
 */

package com.pc.text.method;

import android.text.InputType;

/**
 * For numeric text entry
 * 
 * @author chenjian
 * @date 2013-11-7
 */

public class IPNumberKeyListener extends MyNumberKeyListener {

	// IP输入类型
	public final static String IP_CHARS = "0123456789.";

	public IPNumberKeyListener() {
		super(InputType.TYPE_TEXT_VARIATION_URI, IP_CHARS);
	}

}
