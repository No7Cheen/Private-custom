package com.pc.view.listener;

import android.view.MotionEvent;

/**
 * 触摸屏幕接口
 */
public interface AbOnTouchListener {

	/**
	 * Touch事件.
	 * @param event 触摸手势
	 */
	public void onTouch(MotionEvent event);
}
