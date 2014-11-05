/**
 * @(#)OnRScrollListener.java 2013-10-23 Copyright 2013 it.kedacom.com, Inc. All
 *                            rights reserved.
 */

package com.pc.ui.listview.x.libraries;

import android.view.View;

/**
 * you can listen ListView.OnScrollListener or this one. it will invoke
 * onScrolling when header/footer scroll back.
 * 
 * @author chenjian
 * @date 2013-10-23
 */

public interface OnRScrollListener
{

	public void onRScrolling(View view);
}
