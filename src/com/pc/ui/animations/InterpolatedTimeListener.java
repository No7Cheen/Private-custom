/**
 * @(#)InterpolatedTimeListener.java 2013-10-9 Copyright 2013 it.kedacom.com,
 *                                   Inc. All rights reserved.
 */

package com.pc.ui.animations;

/**
 * 动画进度监听器
 * @author chenjian
 * @date 2013-10-9
 */

public interface InterpolatedTimeListener {

	// interpolatedTime:动画进度值，范围为0～1
	public void interpolatedTime(float interpolatedTime);
}
