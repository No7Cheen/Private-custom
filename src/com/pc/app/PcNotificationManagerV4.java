/**
 * @(#)PcNotificationManagerV4.java   2014-9-19
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.pc.utils.StringUtils;

/**
  * Notification Manager
  * 
  * @author chenj
  * @date 2014-9-19
  */

public class PcNotificationManagerV4 {

	public static long timeIinterval = 2 * 1000;

	// 上次声音通知时间
	public static long mPreNotifiTimeMillis;

	/**
	 * create Notification Builder
	 *
	 * @param context
	 * @param icon
	 * @param smallIcon
	 * @param tickerText
	 * @param contentTitle
	 * @param contentText
	 * @param subText
	 * @param autoCancel
	 * @param lights
	 * @param voice
	 * @param voiceRawResource
	 * @param shake
	 * @return
	 */
	public static NotificationCompat.Builder createBuilder(Context context, Bitmap icon, int smallIcon, String tickerText, String contentTitle, String contentText, String subText,
			boolean autoCancel, boolean lights, boolean voice, int voiceRawResource, boolean shake) {
		if (null == context) {
			context = PcBaseApplicationImpl.getContext();
		}

		if (null == context) {
			return null;
		}

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

		// icon 图标
		if (null != icon) {
			builder.setLargeIcon(icon);
		}

		// 小图标
		if (smallIcon != 0) {
			builder.setSmallIcon(smallIcon);
		}

		// 时间
		builder.setWhen(System.currentTimeMillis());

		// 状态栏上显示的滚动提示文字
		if (!StringUtils.isNull(tickerText)) {
			builder.setTicker(tickerText);
		}

		// 内容标题
		if (!StringUtils.isNull(contentTitle)) {
			builder.setContentTitle(contentTitle);
		}

		// 内容
		if (!StringUtils.isNull(contentText)) {
			builder.setContentText(contentText);
		}

		// 内容附加信息
		if (!StringUtils.isNull(subText)) {
			builder.setSubText(subText);
		}

		builder.setAutoCancel(autoCancel);

		// builder.setNumber(0);

		// 设置闪灯绿光，也可以设置默认的效果
		if (lights) {
			builder.setLights(0xff00ff00, 300, 1000);
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
				builder.setDefaults(Notification.DEFAULT_SOUND);
			} else {
				mPreNotifiTimeMillis = System.currentTimeMillis();
				try {
					builder.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + voiceRawResource)); // R.raw.sms
				} catch (Exception e) {
					builder.setDefaults(Notification.DEFAULT_SOUND);
				}
			}
		}

		// 振动模式
		if (shake) {
			builder.setDefaults(Notification.DEFAULT_VIBRATE);
		}

		return builder;
	}

	/**
	 * notification通知
	 *
	 * @param context
	 * @param icon icon资源
	 * @param smallIcon 
	 * @param tickerText 状态栏上显示的滚动提示文字
	 * @param contentTitle 通知栏标题
	 * @param contentText 通知栏内容
	 * @param contentText 内容附加信息
	 * @param pendingIntent 点击该通知后要跳转的Activity
	 * @param autoCancel 通知能被状态栏的清除按钮给清除掉
	 * @param lights LED灯提醒
	 * @param voice 声音
	 * @param voiceRawResource 声音资源文件 eg:R.raw.sms
	 * @param shake 震动
	 * @param tag the notification tag
	 * @param id the notification Id
	 */
	public static void notification(Context context, Bitmap icon, int smallIcon, String tickerText, String contentTitle, String contentText, String subText,
			PendingIntent pendingIntent, boolean autoCancel, boolean lights, boolean voice, int voiceRawResource, boolean shake, String tag, int id) {
		if (null == context) {
			context = PcBaseApplicationImpl.getContext();
		}

		if (null == context) {
			return;
		}

		NotificationCompat.Builder builder = createBuilder(context, icon, smallIcon, tickerText, contentTitle, contentText, subText, autoCancel, lights, voice, voiceRawResource,
				shake);
		if (null == builder) {
			return;
		}

		if (null != pendingIntent) {
			builder.setContentIntent(pendingIntent);
		}
		NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		// 显示通知
		if (null == tag || tag.length() == 0) {
			notifManager.notify(id, builder.build());
		} else {
			notifManager.notify(tag, id, builder.build());
		}
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

}
