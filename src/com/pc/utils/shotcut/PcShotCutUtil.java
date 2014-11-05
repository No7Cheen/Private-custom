package com.pc.utils.shotcut;

import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;

import com.pc.utils.log.PcLog;

/**
 * app快捷工具
 * <pre>
 * 权限
 * 	<uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
 *  <uses-permission android:name="com.android.launcher.permission.UNINSTALL_SHORTCUT" />
 *  <uses-permission android:name="com.android.launcher.permission.READ_SETTINGS" />
 * 
 * @author songningning 
 */
public class PcShotCutUtil {

	/**
	 * 判断桌面是否已添加快捷方式
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasShortcut(Context context) {
		boolean result = false;
		// 获取当前应用名称
		String title = null;
		try {
			final PackageManager pm = context.getPackageManager();
			title = pm.getApplicationLabel(pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA)).toString();
		} catch (Exception e) {
		}

		String uriStr;
		// if (android.os.Build.VERSION.SDK_INT < 8) {
		// uriStr =
		// "content://com.android.launcher.settings/favorites?notify=true";
		// } else {
		// uriStr =
		// "content://com.android.launcher2.settings/favorites?notify=true";
		// }
		uriStr = "content://com.android.launcher.settings/favorites?notify=true";
		Cursor cursor = null;
		Uri CONTENT_URI = null;
		try {
			CONTENT_URI = Uri.parse(uriStr); // 可能会有NullPointerException
			cursor = context.getContentResolver().query(CONTENT_URI, new String[] {
					"title", "iconResource"
			}, "title=?", new String[] {
				title
			}, null);

			if (cursor != null && cursor.getCount() > 0) {
				result = true;
			}

			if (result) {
				return true;
			}
		} catch (Exception e) {
		} finally {
			if (null != cursor && !cursor.isClosed()) {
				cursor.close();
			}
		}

		Cursor cursor2 = null;
		boolean result2 = false;
		uriStr = "content://com.android.launcher2.settings/favorites?notify=true";
		try {
			CONTENT_URI = Uri.parse(uriStr);// 可能会有NullPointerException
			cursor2 = context.getContentResolver().query(CONTENT_URI, new String[] {
					"title", "iconResource"
			}, "title=?", new String[] {
				title
			}, null);

			if (cursor2 != null && cursor2.getCount() > 0) {
				result2 = true;
			}
		} catch (Exception e) {
		} finally {
			if (null != cursor2 && !cursor2.isClosed()) {
				cursor2.close();
			}
		}

		return result || result2;
	}

	/**
	 * 是否已经创建快捷方式
	 *
	 * @param context
	 * @param appNameResId
	 * @return
	 */
	public static boolean hasShortCut11(Context context, int appNameResId) {
		Cursor cursor = null;
		try {
			String spermi;
			Uri uri = null;
			// if(getSystemVersion() < 8){
			// uri = Uri.parse("content://"+spermi+"/favorites?notify=true");
			// }else{
			// uri = Uri.parse("content://"+spermi+"/favorites?notify=true");
			// }
			// com.sec.android.app.twlauncher.settings
			// delShortcut(context);

			// 制造商
			String manuFacturer = android.os.Build.MANUFACTURER;
			if (manuFacturer.equals("HTC")) {
				spermi = "com.htc.launcher.settings";
			} else if (manuFacturer.equals("ZTE") || manuFacturer.equals("Meizu")) {
				if (android.os.Build.VERSION.SDK_INT < 8) {
					spermi = "com.android.launcher.settings";
				} else {
					spermi = "com.android.launcher2.settings";
				}
			} else {
				spermi = getAuthorityFromPermission(context, "READ_SETTINGS");
			}

			uri = Uri.parse("content://" + spermi + "/favorites?notify=true");
			final ContentResolver cr = context.getContentResolver();
			cursor = cr.query(uri, new String[] {
					"title", "iconResource"
			}, "title=?", new String[] {
				context.getString(appNameResId).trim()
			}, null);

			return (cursor != null && cursor.getCount() > 0) ? true : false;
		} catch (Exception e) {
			PcLog.e("ShotCutUtil", "ShotCutUtil Exception", e);
			return false;
		} finally {
			if (null != cursor && !cursor.isClosed()) {
				cursor.close();
			}
		}
	}

	/**
	 * 获取Authority
	 *
	 * @param context
	 * @param permission
	 * @return
	 */
	public static String getAuthorityFromPermission(Context context, String permission) {
		if (permission == null) return null;

		List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
		if (null == packs) {
			return null;
		}

		for (PackageInfo pack : packs) {
			ProviderInfo[] providers = pack.providers;
			if (null == providers) continue;

			for (ProviderInfo provider : providers) {
				if (provider.readPermission != null && (provider.readPermission).contains(permission)) {
					return provider.authority;
				}
			}
		}

		return null;
	}

	/**
	 * SDK版本
	 *
	 * @return
	 */
	public static int getSystemVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}

	/**
	 * 删除快捷方式
	 *
	 * @param cx
	 */
	public static void delShortcut(Context cx) {
		Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");

		// 获取当前应用名称
		String title = null;
		try {
			final PackageManager pm = cx.getPackageManager();
			title = pm.getApplicationLabel(pm.getApplicationInfo(cx.getPackageName(), PackageManager.GET_META_DATA)).toString();
		} catch (Exception e) {
		}

		// 快捷方式名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
		Intent shortcutIntent = cx.getPackageManager().getLaunchIntentForPackage(cx.getPackageName());
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		cx.sendBroadcast(shortcut);
	}

	/**
	 *  创建快捷方式
	 *  此方法的缺点是，用户清空数据后，在点击快捷方式的时候提示“已创建快捷方式” 但qq也是如此
	 *
	 * @param context
	 * @param iconResourceId 快捷方式图标, 如：R.drawable.ic_launcher
	 * @param appId 快捷方式name,如：R.string.app_name
	 * @param target activity
	 */
	@SuppressWarnings("rawtypes")
	public static void addShortcut(Context context, int iconResourceId, int appId, Class target) {
		// delShortcut(context);

		Intent addIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

		Intent myIntent = new Intent(Intent.ACTION_MAIN);
		// Intent myIntent = context.getPackageManager()
		// .getLaunchIntentForPackage(context.getPackageName());
		myIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		if (target != null) {
			myIntent.setClass(context, target);
		}

		// 快捷方式的标题
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(appId));

		// 获取快捷键的图标
		Parcelable icon = Intent.ShortcutIconResource.fromContext(context, iconResourceId);

		// 快捷方式的图标
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

		// 是否允许重复创建
		addIntent.putExtra("duplicate", false);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		myIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);

		// 设置启动程序
		// ComponentName comp = new ComponentName(context.getPackageName(),"." +
		// ((Activity) context).getLocalClassName());
		// myIntent.setComponent(comp);
		// addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, myIntent);

		// 快捷方式的动作
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, myIntent);

		// 发送广播生成快捷方式
		context.sendBroadcast(addIntent);
	}

	/**
	 * 为当前应用添加桌面快捷方式
	 *
	 * @param cx
	 * @param resourceId
	 */
	private static void addShortcut(Context cx, int resourceId) {
		Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

		Intent shortcutIntent = cx.getPackageManager().getLaunchIntentForPackage(cx.getPackageName());
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		// 获取当前应用名称
		String title = null;
		try {
			final PackageManager pm = cx.getPackageManager();
			title = pm.getApplicationLabel(pm.getApplicationInfo(cx.getPackageName(), PackageManager.GET_META_DATA)).toString();
		} catch (Exception e) {
		}
		// 快捷方式名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
		// 不允许重复创建（不一定有效）
		shortcut.putExtra("duplicate", false);
		// 快捷方式的图标
		Parcelable iconResource = Intent.ShortcutIconResource.fromContext(cx, resourceId);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconResource);

		cx.sendBroadcast(shortcut);
	}

	/**
	 * 删除当前应用的桌面快捷方式
	 * 
	 * @param context
	 * @param iconResourceId R.drawable.icon
	 * @param appId
	 * @param target
	 * @param many
	 */
	@SuppressWarnings("rawtypes")
	private static void addShortcut(Context context, int iconResourceId, int appId, Class target, boolean many) {
		Intent addIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		Parcelable icon = Intent.ShortcutIconResource.fromContext(context, iconResourceId); // 获取快捷键的图标
		String label = context.getString(appId);
		Intent myIntent = new Intent(Intent.ACTION_MAIN);
		// Intent myIntent = context.getPackageManager()
		// .getLaunchIntentForPackage(context.getPackageName());
		myIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		if (target != null) {
			myIntent.setClass(context, target);
		}
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, label);// 快捷方式的标题
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);// 快捷方式的图标
		// 是否允许重复创建 -- fase-->否
		addIntent.putExtra("duplicate", false);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		myIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);

		// 设置启动程序
		// ComponentName comp = new ComponentName(context.getPackageName(),"." +
		// ((Activity) context).getLocalClassName());
		// myIntent.setComponent(comp);
		// addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, myIntent);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, myIntent);// 快捷方式的动作
		context.sendBroadcast(addIntent);// 发送广播
	}

}
