/**
 * @(#)ScreenStatus.java 2013-12-3 Copyright 2013 it.kedacom.com, Inc. All
 *                       rights reserved.
 */

package com.pc.utils.platformtools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pc.utils.StringUtils;

/**
 * 屏幕状态
 * @author chenjian
 * @date 2013-12-3
 */

public abstract class PcAbsScreenStatus extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (null == intent) {
			return;
		}

		String action = intent.getAction();
		if (StringUtils.isNull(action)) {
			return;
		}

		// 锁屏状态
		if (StringUtils.equals(action, Intent.ACTION_SCREEN_OFF)) {

			screenOff();

			return;
		}

		if (StringUtils.equals(action, Intent.ACTION_SCREEN_ON)) {

			screenON();

			return;
		}
	}

	public abstract void screenOff();

	public abstract void screenON();

}
