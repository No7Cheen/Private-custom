package com.pc.utils.network;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;

public class WifiAdmin {

	// WiFiMangager常用的状态：
	// WiFiManager.WIFI_STATE_ENABLED WiFi已打开
	// WiFiManager.WIFI_STATE_ENABLING WiFi打开中
	// WiFiManager.WIFI_STATE_DISABLING WiFi关闭中
	// WiFiManager.WIFI_STATE_DISABLED WiFi已关闭
	// WiFiManager.WIFI_STATE_UNKNOWN 未知的WiFi状态

	private WifiManager mWifiManager;

	// 定义WifiInfo对象
	private WifiInfo mWifiInfo;

	// 扫描出的网络连接列表
	private List<ScanResult> mWifiList;

	// 定义一个WifiLock
	private WifiLock mWifiLock;

	// 网络连接列表
	private List<WifiConfiguration> mWifiConfiguration;

	public WifiAdmin(Context context) {
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		mWifiInfo = mWifiManager.getConnectionInfo();
	}

	/**
	 * 打开WIFI
	 */
	public void OpenWifi() {
		if (mWifiManager.isWifiEnabled()) {
			return;
		}

		mWifiManager.setWifiEnabled(true);
	}

	/**
	 * 关闭WIFI
	 */
	public void CloseWifi() {
		if (mWifiManager.isWifiEnabled()) {
			return;
		}
		mWifiManager.setWifiEnabled(false);
	}

	/**
	 * 锁定WifiLock
	 */
	public void AcquireWifiLock() {
		mWifiLock.acquire();
	}

	/**
	 * 解锁WifiLock
	 */
	public void ReleaseWifiLock() {
		if (!mWifiLock.isHeld()) {
			return;
		}

		mWifiLock.acquire();
	}

	/**
	 * 创建一个WifiLock
	 */
	public void creatWifiLock() {
		mWifiLock = mWifiManager.createWifiLock("Test");
	}

	/**
	 * 得到配置好的网络
	 *
	 * @return
	 */
	public List<WifiConfiguration> getConfiguration() {
		return mWifiConfiguration;
	}

	/**
	 * 指定配置好的网络进行连接
	 * 
	 * @param index
	 */
	public void connectConfiguration(int index) {
		// 索引大于配置好的网络索引返回
		if (index > mWifiConfiguration.size()) {
			return;
		}

		// 连接配置好的指定ID的网络
		mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId, true);
	}

	/**
	 * 开始扫描
	 */
	public void startScan() {
		mWifiManager.startScan();
		// 得到扫描结果
		mWifiList = mWifiManager.getScanResults();
		// 得到配置好的网络连接
		mWifiConfiguration = mWifiManager.getConfiguredNetworks();
	}

	/**
	 * 得到网络列表
	 * 
	 * @return
	 */
	public List<ScanResult> getWifiList() {
		return mWifiList;
	}

	/**
	 * 查看扫描结果
	 * 
	 * @return
	 */
	@SuppressLint("UseValueOf")
	public StringBuilder lookUpScan() {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < mWifiList.size(); i++) {
			stringBuilder.append("Index_" + new Integer(i + 1).toString() + ":");
			// 将ScanResult信息转换成一个字符串包
			// 其中把包括：BSSID、SSID、capabilities、frequency、level
			stringBuilder.append((mWifiList.get(i)).toString());
			stringBuilder.append("\n");
		}

		return stringBuilder;
	}

	/**
	 * 得到MAC地址
	 * 
	 * @return
	 */
	public String getMacAddress() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
	}

	/**
	 * 得到接入点的BSSID
	 * 
	 * @return
	 */
	public String getBSSID() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
	}

	/**
	 * 得到IP地址
	 * 
	 * @return
	 */
	public int getIPAddress() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
	}

	/**
	 * 得到连接的ID
	 * 
	 * @return
	 */
	public int getNetworkId() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
	}

	/**
	 * 得到WifiInfo的所有信息包
	 * 
	 * @return
	 */
	public String getWifiInfo() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
	}

	/**
	 * 添加一个网络并连接
	 * @param wcg
	 */
	public void addNetwork(WifiConfiguration wcg) {
		int wcgID = mWifiManager.addNetwork(wcg);
		mWifiManager.enableNetwork(wcgID, true);
	}

	/**
	 * 断开指定ID的网络
	 * 
	 * @param netId
	 */
	public void disconnectWifi(int netId) {
		mWifiManager.disableNetwork(netId);
		mWifiManager.disconnect();
	}

	/**
	 * Wifi是否可用
	 *
	 * @param context
	 * @return
	 */
	public static boolean isWifiEnabled(Context context) {
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		return wifi.isWifiEnabled() && wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED;
	}

}
