/**
 * @(#)PIBaseActivity.java 2014-1-16 Copyright 2014 it.kedacom.com, Inc. All
 *                         rights reserved.
 */

package com.pc.app.base;

/**
 * supper pc BaseActivity interface
 * 
 * @author chenjian
 * @date 2014-1-16
 */

public interface PcIBaseActivity {

	/**
	 * Activity finish
	 */
	public void onFinish();

	/**
	 * Activity 是否可用 
	 *
	 * @return
	 */
	public boolean isAvailable();
}
