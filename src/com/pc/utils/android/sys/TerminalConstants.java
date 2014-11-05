/**
 * @(#)TerminalContents.java 2013-5-8 Copyright 2013 it.kedacom.com, Inc. All
 *                           rights reserved.
 */

package com.pc.utils.android.sys;

/**
 * 设备常量
 * 
 * @author chenjian
 * @date 2013-5-8
 */

public class TerminalConstants {

	// 产品名称

	/** 华为*/
	public static final String BUILD_MODEL_HUAWEIC8825D = "HUAWEI C8825D";

	/** 摩托罗拉 */
	public static final String BUILD_MODEL_ME525 = "ME525+";

	/** HTC */
	public static final String BUILD_MODEL_HTCEVO3DX515m = "HTC EVO 3D X515m";

	/** 魅族 */
	public static final String BUILD_MODEL_MEIZUMX = "MEIZU MX";

	/** 中兴V970 */
	public static final String BUILD_MODEL_ZTEV970 = "ZTE V970";

	/** 三星S4 */
	public static final String BUILD_MODEL_GTI9502 = "GT-I9502";

	/** note2 */
	public static final String BUILD_MODEL_GTN7100 = "GT-N7100";

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
