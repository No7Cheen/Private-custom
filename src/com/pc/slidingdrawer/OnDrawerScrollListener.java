/**
 * @(#)OnDrawerScrollListener.java 2014-1-17 Copyright 2014 it.kedacom.com, Inc.
 *                                 All rights reserved.
 */

package com.pc.slidingdrawer;

/**
 * Callback invoked when the drawer is scrolled.
 * @author chenjian
 * @date 2014-1-17
 */

public interface OnDrawerScrollListener {

	/**
	 * Invoked when the user starts dragging/flinging the drawer's handle.
	 */
	public void onScrollStarted();

	/**
	 * Invoked when the user stops dragging/flinging the drawer's handle.
	 */
	public void onScrollEnded();
}
