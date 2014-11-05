/**
 * @(#)AbsSMSReceiver.java 2014-1-15 Copyright 2014 it.kedacom.com, Inc. All
 *                         rights reserved.
 */

package com.pc.utils.platformtools;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.pc.utils.time.TimeUtils;

/**
 * @author chenjian
 * @date 2014-1-15
 */

public abstract class PcAbsSMSReceiver extends BroadcastReceiver {

	private final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (null == intent) {
			return;
		}

		String action = intent.getAction();
		if (!SMS_RECEIVED_ACTION.equals(action)) {
			return;
		}

		Bundle bundle = intent.getExtras();
		if (null == bundle) {
			return;
		}

		SmsMessage msg = null;
		Object[] pdusObj = (Object[]) bundle.get("pdus");
		for (Object p : pdusObj) {
			msg = SmsMessage.createFromPdu((byte[]) p);

			// 得到消息的内容
			String msgTxt = msg.getMessageBody();

			// 接受时间
			Date date = new Date(msg.getTimestampMillis());
			SimpleDateFormat format = new SimpleDateFormat(TimeUtils.TIMEFORMAT_ALL);
			String receiveTime = format.format(date);

			// 发送人
			String senderNumber = msg.getOriginatingAddress();
		}

		// SmsMessage[] messages = getMessagesFromBundle(bundle);
		// for (SmsMessage message : messages) {
		// Log.i(TAG, message.getOriginatingAddress() + " : " +
		// message.getDisplayOriginatingAddress() + " : "
		// + message.getDisplayMessageBody() + " : " +
		// message.getTimestampMillis());
		// }
	}

	public final SmsMessage[] getMessagesFromBundle(Bundle bundle) {
		Object[] messages = (Object[]) bundle.get("pdus");
		byte[][] pduObjs = new byte[messages.length][];
		for (int i = 0; i < messages.length; i++) {
			pduObjs[i] = (byte[]) messages[i];
		}

		byte[][] pdus = new byte[pduObjs.length][];
		int pduCount = pdus.length;
		SmsMessage[] msgs = new SmsMessage[pduCount];
		for (int i = 0; i < pduCount; i++) {
			pdus[i] = pduObjs[i];
			msgs[i] = SmsMessage.createFromPdu(pdus[i]);
		}

		return msgs;
	}

}
