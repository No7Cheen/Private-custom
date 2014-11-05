/**
 * @(#)BluetoothReceiver.java 2013-11-23 Copyright 2013 it.kedacom.com, Inc. All
 *                            rights reserved.
 */

package com.pc.utils.platformtools;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pc.utils.log.PcLog;

/**
 * @author chenj
 * @date 2013-11-23
 */

public abstract class PcAbsBluetoothReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (null == intent) {
			return;
		}

		String action = intent.getAction();

		// 发现远程设备
		if (BluetoothDevice.ACTION_FOUND.equals(action)) {
			// 获取发现的设备对象
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

			String dName = device.getName();
			String dAddr = device.getAddress();
			int state = device.getBondState();
			BluetoothClass bluetoothClass = device.getBluetoothClass();

			PcLog.w("Test", " name " + dName + " addr " + dAddr);

			// 已配对的设备
			if (device.getBondState() == BluetoothDevice.BOND_BONDED) {

			}
		} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
		}
	}

}
