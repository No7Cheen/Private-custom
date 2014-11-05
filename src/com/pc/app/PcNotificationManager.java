/**
 * @(#)PcNotificationManager.java   2014-8-11
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.net.Uri;
import android.os.Vibrator;

import com.pc.utils.StringUtils;

/**
  * Notification Manager
  * 
  * @author chenj
  * @date 2014-8-11
  */

public class PcNotificationManager {

	public static long timeIinterval = 2 * 1000;

	// 上次声音通知时间
	public static long mPreNotifiTimeMillis;

	/**
	 * notification通知
	 *
	 * @param context
	 * @param iconResId icon资源
	 * @param tickerText 状态栏上显示的滚动提示文字
	 * @param contentTitle 通知栏标题
	 * @param contentText 通知栏内容
	 * @param contentIntent 点击该通知后要跳转的Activity
	 * @param autoCancel 通知能被状态栏的清除按钮给清除掉
	 * @param lights LED灯提醒
	 * @param voice 声音
	 * @param voiceRawResource 声音资源文件 eg:R.raw.sms
	 * @param shake 震动
	 * @param tag the notification tag
	 * @param id the notification Id
	 */
	public static void notification(Context context, int iconResId, String tickerText, String contentTitle, String contentText, PendingIntent contentIntent, boolean autoCancel,
			boolean lights, boolean voice, int voiceRawResource, boolean shake, String tag, int id) {
		NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();// 创建一个通知

		// icon 图标
		notification.icon = iconResId;

		// 通知产生的时间，会在通知信息里显示
		notification.when = System.currentTimeMillis();

		// 状态栏上显示的滚动提示文字
		notification.tickerText = tickerText;

		// 该通知能被状态栏的清除按钮给清除掉
		if (autoCancel) {
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
		} else {
			notification.flags |= Notification.FLAG_NO_CLEAR;
		}

		// notification.defaults |= Notification.DEFAULT_LIGHTS; // LED灯提醒
		// 设置闪灯绿光，也可以设置默认的效果
		if (lights) {
			notification.ledARGB = 0xff00ff00;
			notification.ledOnMS = 300;
			notification.ledOffMS = 1000;
			notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		}

		if (voice) {
			// 2次声音通知的时间间隔为2S
			if (timeIinterval > 0) {
				voice = System.currentTimeMillis() - mPreNotifiTimeMillis >= timeIinterval;
			}
		}

		if (shake) {
			// 2次震动的时间间隔为2S
			if (timeIinterval > 0) {
				shake = System.currentTimeMillis() - mPreNotifiTimeMillis >= timeIinterval;
			}
		}

		// 声音
		if (voice) {
			if (voiceRawResource <= 0) {
				notification.defaults |= Notification.DEFAULT_SOUND;
			} else {
				mPreNotifiTimeMillis = System.currentTimeMillis();
				try {
					notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + voiceRawResource); // R.raw.sms
				} catch (Exception e) {
					notification.defaults |= Notification.DEFAULT_SOUND;
				}
			}
		}

		// 振动模式
		if (shake) {
			// 1
			// long[] vibrate = {
			// 0, 100, 200, 300
			// }; // 0毫秒后开始振动，振动100毫秒后停止，再过200毫秒后再次振动300毫秒
			// notification.vibrate = vibrate;

			// 2
			// mPreNotifiTimeMillis = System.currentTimeMillis();
			// vibrate(context, 500);

			// 3
			notification.defaults |= Notification.DEFAULT_VIBRATE;
		}

		// 设置Notification的Title和详细内容（打开提示栏后在通知列表中显示）
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

		// 显示通知
		if (null == tag || tag.length() == 0) {
			notifManager.notify(id, notification);
		} else {
			notifManager.notify(tag, id, notification);
		}
	}

	/**
	 * notification
	 * 
	 * @param context
	 * @param iconResId icon 资源
	 * @param tickerText 状态栏上显示的滚动提示文字
	 * @param contentTitle 通知栏标题
	 * @param contentText 通知栏内容
	 * @param contentIntent 点击该通知后要跳转的Activity
	 * @param autoCancel 通知能被状态栏的清除按钮给清除掉
	 * @param lights LED灯提醒
	 * @param voice 声音
	 * @param shake 震动
	 * @param tag notification tag
	 * @param id notification Id
	 */
	public static void notification2(Context context, int iconResId, String tickerText, String contentTitle, String contentText, PendingIntent contentIntent, boolean autoCancel,
			boolean lights, boolean voice, int voiceRawResource, boolean shake, String tag, int id) {
		NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();// 创建一个通知

		// icon 图标
		notification.icon = iconResId;

		// 通知产生的时间，会在通知信息里显示
		notification.when = System.currentTimeMillis();

		// 状态栏上显示的滚动提示文字
		notification.tickerText = tickerText;

		// 该通知能被状态栏的清除按钮给清除掉
		notification.flags |= Notification.FLAG_NO_CLEAR;

		if (voice) {
			// 2次声音通知的时间间隔为2S
			if (timeIinterval > 0) {
				voice = System.currentTimeMillis() - mPreNotifiTimeMillis >= timeIinterval;
			}
		}

		if (shake) {
			// 2次震动的时间间隔为2S
			if (timeIinterval > 0) {
				shake = System.currentTimeMillis() - mPreNotifiTimeMillis >= timeIinterval;
			}
		}

		// 声音
		if (voice) {

			if (voiceRawResource <= 0) {
				notification.defaults |= Notification.DEFAULT_SOUND;
			} else {
				mPreNotifiTimeMillis = System.currentTimeMillis();
				try {
					notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + voiceRawResource);// R.raw.sms
				} catch (Exception e) {
					notification.defaults |= Notification.DEFAULT_SOUND;
				}
			}

			try {

			} catch (NullPointerException e) {
			}
		}

		// 振动模式
		if (shake) {
			// vibrate(context, 500);
			notification.defaults |= Notification.DEFAULT_VIBRATE;
		}

		// 设置Notification的Title和详细内容（打开提示栏后在通知列表中显示）
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

		// 显示通知
		if (null == tag || tag.length() == 0) {
			notifManager.notify(id, notification);
			notifManager.notify(id, notification);
		} else {
			notifManager.notify(tag, id, notification);
		}
	}

	/**
	 * 静音更新Notification
	 * 
	 * @param context
	 * @param iconResId
	 * @param tickerText
	 * @param contentTitle
	 * @param contentText
	 * @param contentIntent
	 * @param autoCancel
	 * @param tag
	 * @param id
	 */
	public void updateNotification(Context context, int iconResId, String tickerText, String contentTitle, String contentText, PendingIntent contentIntent, boolean autoCancel,
			String tag, int id) {
		notification(context, iconResId, tickerText, contentTitle, contentText, contentIntent, autoCancel, false, false, 0, false, tag, id);
	}

	/**
	 * 取消通知
	 * 
	 * @param context
	 * @param tag
	 * @param id
	 */
	public static void cancelNotification(Context context, String tag, int id) {
		if (context == null) return;

		NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		if (StringUtils.isNull(tag)) {
			notifManager.cancel(id);
		} else {
			notifManager.cancel(tag, id);
		}
	}

	/**
	 * 取消所有通知
	 * 
	 * @param context
	 */
	public static void cancelAllNotification(Context context) {
		NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notifManager.cancelAll();
	}

	/**
	 * 还原初始化数据
	 */
	public static void restoreInitialData() {
	}

	/**
	 * 控制振动
	 * 
	 * @param activity
	 * @param milliseconds
	 */
	public static void vibrate(Context context, long milliseconds) {
		Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
		vib = null;
	}

}
