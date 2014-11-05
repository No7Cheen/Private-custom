/**
 * @(#)TerminalConstants2.java 2014-2-12 Copyright 2014 it.kedacom.com, Inc. All
 *                             rights reserved.
 */

package com.pc.utils.android.sys;

/**
 * @author chenjian
 * @date 2014-2-12
 */

public enum EmTerminalConstants {
	// 华为
	BUILD_MODEL_HUAWEIC8825D("HUAWEI C8825D"),

	// 摩托罗拉
	BUILD_MODEL_ME525("ME525+"),

	// HTC
	BUILD_MODEL_HTCEVO3DX515m("HTC EVO 3D X515m"),

	// 魅族
	BUILD_MODEL_MEIZUMX("MEIZU MX"),

	// 中兴V970
	BUILD_MODEL_ZTEV970("ZTE V970"),

	// 三星S4
	BUILD_MODEL_GTI9502("GT-I9502"),

	// note2
	BUILD_MODEL_GTN7100("GT-N7100");

	private final String v;

	EmTerminalConstants(String value) {
		v = value;
	}

	public String getValue() {
		return v;
	}

	@Override
	public String toString() {
		return v;
	}

	/**
	 * 判断是否只指定类型
	 * 
	 * @param buildModel
	 * @return
	 */
	public static boolean isTerminal(String buildModel) {
		return android.os.Build.MODEL.equals(buildModel);
	}

}
