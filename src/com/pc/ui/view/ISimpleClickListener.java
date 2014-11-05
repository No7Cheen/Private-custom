/**
 * @(#)ISimpleTouchListener.java   2014-9-24
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.ui.view;

import android.view.View;

/**
  * 点击事件
  * 包括：点击、双击和长按
  * 
  * 
  * @author chenj
  * @date 2014-9-24
  */

public interface ISimpleClickListener {

	/**
	 * 单击事件
	 */
	public void onClick(View v);

	/**
	 * 双击事件
	 */
	public void onDoubleClick(View v);

	/**
	 * 长按
	 */
	public void onLongPress(View v);
}
