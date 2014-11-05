/**
 * @(#)PhoneStatReceiver.java 2013-12-17 Copyright 2013 it.kedacom.com, Inc. All
 *                            rights reserved.
 */

package com.pc.utils.platformtools;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.pc.utils.StringUtils;

/**
 * <pre>
 * filter.addAction(android.intent.action.PHONE_STATE);
 * filter.addAction(android.intent.action.NEW_OUTGOING_CALL);
 * </pre>
 * @author chenjian
 * @date 2013-12-17
 */

public abstract class PcAbsPhoneStatReceiver extends BroadcastReceiver {

	private final String INCOMING_NUMBER = "incoming_number";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (null == intent) {
			return;
		}
		// 如果是拨打电话
		if (StringUtils.equals(Intent.ACTION_NEW_OUTGOING_CALL, intent.getAction())) {
			String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			outgoingCall(phoneNumber);

			return;
		}

		if (StringUtils.equals("android.intent.action.PHONE_STATE", intent.getAction())) {
			String incomingNumber = "";

			// 如果是来电
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
			switch (tm.getCallState()) {
			// 来电状态
				case TelephonyManager.CALL_STATE_RINGING:
					incomingNumber = intent.getStringExtra(INCOMING_NUMBER);
					ringing(incomingNumber);
					break;

				// 接通
				case TelephonyManager.CALL_STATE_OFFHOOK:
					incomingNumber = intent.getStringExtra(INCOMING_NUMBER);
					offhook(incomingNumber);
					break;

				// 挂断
				case TelephonyManager.CALL_STATE_IDLE:
					hangup();
					break;
			}

			return;
		}
	}

	/**
	 * 拨打电话
	 */
	public abstract void outgoingCall(String phoneNumber);

	/**
	 * 来电状态
	 */
	public abstract void ringing(String incomingNumber);

	/**
	 * 接通
	 */
	public abstract void offhook(String incomingNumber);

	/**
	 * 挂断
	 */
	public abstract void hangup();

}
