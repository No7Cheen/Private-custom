/**
 * @(#)OnPScrollListener.java 2014-2-21 Copyright 2014 it.kedacom.com, Inc. All
 *                            rights reserved.
 */

package com.pc.ui.listview.x.libraries;

import android.view.View;

/**
 * you can listen ListView.OnScrollListener or this one. it will invoke
 * onXScrolling when header/footer scroll back.
 * @author chenjian
 * @date 2014-2-21
 */

public interface OnPScrollListener {

	public void onPScrolling(View view);
}
