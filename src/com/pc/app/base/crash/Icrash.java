/**
 * @(#)Icrash.java 2014-1-16 Copyright 2014 it.kedacom.com, Inc. All rights
 *                 reserved.
 */

package com.pc.app.base.crash;

import java.io.StringWriter;

/**
 * crash interface
 * 
 * @author chenjian
 * @date 2014-1-16
 */

public interface Icrash {

	/**
	 * crash
	 *
	 * @param error
	 * @param errorInfo
	 */
	public void crash(StringWriter error, String errorInfo);

}
