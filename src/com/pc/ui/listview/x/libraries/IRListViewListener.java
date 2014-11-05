/**
 * @(#)IRListViewListener.java 2013-10-23 Copyright 2013 it.kedacom.com, Inc.
 *                             All rights reserved.
 */

package com.pc.ui.listview.x.libraries;

/**
 * implements this interface to get refresh/load more event.
 * 
 * @author chenjian
 * @date 2013-10-23
 */

public interface IRListViewListener
{

	public void onRefresh();

	public void onLoadMore();
}
