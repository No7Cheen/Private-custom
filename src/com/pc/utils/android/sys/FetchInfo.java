package com.pc.utils.android.sys;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

public class FetchInfo {

	public static class AppVersionInfo {

		public String packageStr;
		public String versionName = "General";
		public int versionCode = 1;
	}

	public static AppVersionInfo getApplicationVersionInfo(Context context, String packageStr) {
		AppVersionInfo info = null;
		try {
			PackageInfo pinfo = context.getPackageManager().getPackageInfo(packageStr,
					PackageManager.GET_CONFIGURATIONS);

			String versionName = pinfo.versionName;
			int versionCode = pinfo.versionCode;

			info = new AppVersionInfo();
			info.packageStr = packageStr;
			info.versionCode = versionCode;
			info.versionName = versionName;

		} catch (NameNotFoundException e) {
		}

		return info;
	}

	public static class SystemInfo {

		// 操作系统信息
		public String os_Arch;// 芯片类型
		public String os_Name;// 操作系统名
		public String os_Version;// 操作系统版本号

		// build version
		public int sdkInt;// sdk版本号
		@Deprecated
		public String sdkVersion;// sdk版本号
		public String codename;//
		public String incremental;
		public String release;
		public String language;
		public String locale;

		// 设备信息
		public String device;// 设备名
		public String model;//
		public String product;

		// 激活信息
		public String license;//
		public String hwid;//
		public String sequence = "";

		private static SystemInfo info;

		public static SystemInfo Instance() {
			if (info == null) info = new SystemInfo();
			return info;
		}

		public static String getCountry() {
			Locale locale = Locale.getDefault();
			return locale.getCountry();
		}

		public static String getLanguage() {
			Locale locale = Locale.getDefault();
			return locale.getLanguage();
		}

		private SystemInfo() {
			sdkVersion = android.os.Build.VERSION.SDK;
			sdkInt = android.os.Build.VERSION.SDK_INT;

			model = android.os.Build.MODEL;
			release = android.os.Build.VERSION.RELEASE;

			device = android.os.Build.DEVICE;
			product = android.os.Build.PRODUCT;
			codename = android.os.Build.VERSION.CODENAME;
			incremental = android.os.Build.VERSION.INCREMENTAL;

			os_Arch = System.getProperty("os.arch");
			os_Name = System.getProperty("os.name");
			os_Version = System.getProperty("os.version");

			Locale locale = Locale.getDefault();
			SystemInfo.this.locale = locale.getCountry();
			language = locale.getLanguage();
			// language = language.toLowerCase();

			/*
			 * if (!language.equals("zh")) { }
			 */

			license = "";
			hwid = "";
			sequence = "";
		}
	}

	public static String getHWID(Context context) {
		return getHWID(context, FetchInfo.getLocalMacAddress(context));
	}

	public static String getHWID(Context context, String localMacAddress) {
		String s = getCPUID() + "_" + SystemInfo.Instance().device + "_" + localMacAddress;
		return s;
	}

	public static String getCPUID() {
		String str = "", strCPU = "", cpuAddress = "0000000000000000";
		try {
			// 读取CPU信息
			Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			// 查找CPU序列号
			for (int i = 1; i < 100; i++) {
				str = input.readLine();
				if (str != null) {
					// 查找到序列号所在行
					if (str.indexOf("Serial") > -1) {
						// 提取序列号
						strCPU = str.substring(str.indexOf(":") + 1, str.length());
						// 去空格
						cpuAddress = strCPU.trim();
						break;
					}
				} else {
					// 文件结尾
					break;
				}
			}
		} catch (IOException ex) {
		}
		return cpuAddress;

	}

	/**
	 * 需要以下权限 android.permission.ACCESS_NETWORK_STATE
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		}

		NetworkInfo[] info = connectivity.getAllNetworkInfo();
		if (info != null) {
			for (int i = 0; i < info.length; i++) {
				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}

		return false;
	}

	public static boolean is3GNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		}
		NetworkInfo[] info = connectivity.getAllNetworkInfo();
		if (info != null) {
			for (int i = 0; i < info.length; i++) {
				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					if (info[i].getType() == ConnectivityManager.TYPE_MOBILE) return true;
				}
			}
		}

		return false;
	}

	public static class WifiConfigInfo {

		public String BSSID;
		public int networkid;
		public String PreSharedKey;
		public String SSID;

		WifiConfigInfo(WifiConfiguration config) {
			BSSID = config.BSSID;
			networkid = config.networkId;
			PreSharedKey = config.preSharedKey;
			SSID = config.SSID;
		}

	}

	public static List<WifiConfigInfo> getWifiConfigInfos(Context context) {
		WifiManager wifi_service = (WifiManager) context.getSystemService("WIFI_SERVICE");
		if (wifi_service == null) return null;
		List<WifiConfiguration> l = wifi_service.getConfiguredNetworks();
		List<WifiConfigInfo> ws = new ArrayList<WifiConfigInfo>();
		if (l != null) {
			for (WifiConfiguration w : l) {
				ws.add(new WifiConfigInfo(w));
			}
		}

		return ws;
	}

	public static WifiInfo getWifiInfo(Context context) {
		WifiManager wifi_service = (WifiManager) context.getSystemService("WIFI_SERVICE");
		if (wifi_service == null) return null;

		return wifi_service.getConnectionInfo();
	}

	public static DhcpInfo getDhcpInfo(Context context) {
		WifiManager wifi_service = (WifiManager) context.getSystemService("WIFI_SERVICE");
		if (wifi_service == null) return null;
		return wifi_service.getDhcpInfo();
	}

	/**
	 * WIFI_STATE_DISABLING：常量0，表示停用中。 WIFI_STATE_DISABLED：常量1，表示不可用。
	 * WIFI_STATE_ENABLING：常量2，表示启动中。 WIFI_STATE_ENABLED：常量3，表示准备就绪。
	 * WIFI_STATE_UNKNOWN：常量4，表示未知状态。
	 * @param context
	 * @return
	 */
	public static int getWifiState(Context context) {
		WifiManager wifi_service = (WifiManager) context.getSystemService("WIFI_SERVICE");
		if (wifi_service == null) return WifiManager.WIFI_STATE_UNKNOWN;
		return wifi_service.getWifiState();
	}

	public static String getLocalMacAddress(Context context) {
		// if(true)
		// return "defaultMacAddress";
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}
		return null;
	}

	public synchronized static String runCmd(String[] cmd, String workdirectory) throws IOException {
		String result = "";

		try {
			ProcessBuilder builder = new ProcessBuilder(cmd);
			// 设置一个路径
			if (workdirectory != null) builder.directory(new File(workdirectory));
			builder.redirectErrorStream(true);
			Process process = builder.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[1024];
			while (in.read(re) > 0) {
				result = result + new String(re);
			}
			in.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static String fetch_version_info() {
		String result = null;
		try {
			String[] args = {
					"/system/bin/cat", "/proc/version"
			};
			result = runCmd(args, "system/bin/");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 运营商信息
	 * @param cx
	 * @return
	 */
	public static String fetch_tel_status(Context cx) {
		String result = null;
		TelephonyManager tm = (TelephonyManager) cx.getSystemService(Context.TELEPHONY_SERVICE);//
		String str = " ";
		str += "DeviceId(IMEI) = " + tm.getDeviceId() + "\n";
		str += "DeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion() + "\n";
		int mcc = cx.getResources().getConfiguration().mcc;
		int mnc = cx.getResources().getConfiguration().mnc;
		str += "IMSI MCC (Mobile Country Code): " + String.valueOf(mcc) + "\n";
		str += "IMSI MNC (Mobile Native Code): " + String.valueOf(mnc) + "\n";
		result = str;
		return result;
	}

	public static String fetch_cpu_info() {
		String result = null;
		try {
			String[] args = {
					"/system/bin/cat", "/proc/cpuinfo"
			};
			result = runCmd(args, "/system/bin/");
			Log.i("result", "result=" + result);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static String getMemoryInfo(Context context) {
		StringBuffer memoryInfo = new StringBuffer();
		final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(outInfo);
		memoryInfo.append("\nTotal Available Memory :").append(outInfo.availMem >> 10).append("k");
		memoryInfo.append("\nTotal Available Memory :").append(outInfo.availMem >> 20).append("k");
		memoryInfo.append("\nIn low memory situation:").append(outInfo.lowMemory);
		String result = null;
		try {
			String[] args = {
					"/system/bin/cat", "/proc/meminfo"
			};
			result = runCmd(args, "/system/bin/");
		} catch (IOException ex) {
			Log.i("fetch_process_info", "ex=" + ex.toString());
		}
		return memoryInfo.toString() + "\n\n" + result;
	}

	public static String fetch_disk_info() {
		String result = null;
		try {
			String[] args = {
				"/system/bin/df"
			};
			result = runCmd(args, "/system/bin/");
			Log.i("result", "result=" + result);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static String fetch_netcfg_info() {
		String result = null;
		try {
			String[] args = {
				"/system/bin/netcfg"
			};
			result = runCmd(args, "/system/bin/");
		} catch (IOException ex) {
			Log.i("fetch_process_info", "ex=" + ex.toString());
		}
		return result;
	}

	/**
	 * 获取显示频信息
	 * @param cx
	 * @return
	 */
	public static String getDisplayMetrics(Context cx) {
		String str = " ";
		DisplayMetrics dm = new DisplayMetrics();
		dm = cx.getApplicationContext().getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		int screenHeight = dm.heightPixels;
		float density = dm.density;
		float xdpi = dm.xdpi;
		float ydpi = dm.ydpi;
		str += "The absolute width: " + String.valueOf(screenWidth) + "pixels\n";
		str += "The absolute heightin: " + String.valueOf(screenHeight) + "pixels\n";
		str += "The logical density of the display. : " + String.valueOf(density) + "\n";
		str += "X dimension : " + String.valueOf(xdpi) + "pixels per inch\n";
		str += "Y dimension : " + String.valueOf(ydpi) + "pixels per inch\n";
		return str;
	}

	public static int getSdkVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}

}
