/**
 * @(#)RectFRectF.java   2014-8-29
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.graphics.util;

import android.graphics.RectF;

/**
  * 
  * @author chenj
  * @date 2014-8-29
  */

public class RectUtils {

	/**
	 * 返回RectF比率
	 *
	 * @param rect
	 * @return
	 */
	public static float getRectRatio(RectF rect) {
		return rect.width() / rect.height();
	}
}
