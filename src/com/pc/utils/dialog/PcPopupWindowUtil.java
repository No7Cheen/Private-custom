/**
 * @(#)PopupWindowUtil.java 2014-1-15 Copyright 2014 it.kedacom.com, Inc. All
 *                          rights reserved.
 */

package com.pc.utils.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pc.app.dialog.modle.MenuAdapter;
import com.pc.utils.StringUtils;
import com.privatecustom.publiclibs.R;

/**
 * @author chenjian
 * @date 2014-1-15
 */

public class PcPopupWindowUtil {

	/**
	 * 底部弹出框
	 * <p>
	 * PopupWindow
	 * </p>
	 * @Description
	 * @param context
	 * @param parentView
	 * @param resourcesId Resources Id:代表一个Menu数组
	 * @param listener 每个Menu对应的Click Listener,与Menu数组一一对应
	 */
	public static PopupWindow popUpWindowMenu(final Context context, final View parentView, final int resourcesId,
			final View.OnClickListener[] listeners, final String title) {
		if (context == null) {
			return null;
		}
		String[] alertText = context.getResources().getStringArray(resourcesId);
		if (alertText == null || alertText.length == 0) {
			return null;
		}

		View menuView = LayoutInflater.from(context).inflate(R.layout.alert_dialog_menu_layout, null);
		if (menuView == null) {
			return null;
		}

		// 没有Title
		if (StringUtils.isNull(title)) {
			menuView.findViewById(R.id.topBlank_frame).setVisibility(View.VISIBLE);
		} else {
			menuView.findViewById(R.id.topBlank_frame).setVisibility(View.GONE);
			View tView = LayoutInflater.from(context).inflate(R.layout.alert_dialog_menu_layout_title, null);
			((TextView) tView).setText(title);
		}

		ListView contentList = (ListView) menuView.findViewById(R.id.content_list);

		final PopupWindow popWin = new PopupWindow(menuView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		popWin.setFocusable(true);
		popWin.setOutsideTouchable(true);
		popWin.setAnimationStyle(R.style.popupWindowAnimation_Bottom);
		if (parentView != null) {
			popWin.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
			// 显示动画
			Animation anim = AnimationUtils.loadAnimation(context, R.anim.popupwindow_bg_dark);
			parentView.startAnimation(anim);
			// 消失动画
			popWin.setOnDismissListener(new PopupWindow.OnDismissListener() {

				@Override
				public void onDismiss() {
					Animation anim = AnimationUtils.loadAnimation(context, R.anim.popupwindow_bg_light);
					parentView.startAnimation(anim);
				}
			});
		}

		View.OnClickListener[] tmpListener = new View.OnClickListener[alertText.length];
		for (int i = 0; i < alertText.length; i++) {
			if (listeners != null && listeners.length > 0 && i < listeners.length && listeners[i] != null) {
				tmpListener[i] = listeners[i];
			} else {
				if (i == alertText.length - 1) {
					tmpListener[i] = new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							popWin.dismiss();
						}
					};
				} else {
					tmpListener[i] = null;
				}
			}
		}

		MenuAdapter menuAdapter = new MenuAdapter(context, alertText, null, null, tmpListener);
		contentList.setAdapter(menuAdapter);
		menuAdapter.notifyDataSetChanged();

		return popWin;
	}

}
