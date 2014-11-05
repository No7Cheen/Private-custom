/**
 * @(#)BottomPopupDialogListView.java   2014-7-23
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.pc.app.dialog.modle.MenuAdapter;
import com.pc.app.dialog.modle.PcEmDialogType;
import com.pc.utils.StringUtils;
import com.pc.utils.android.sys.TerminalUtils;
import com.privatecustom.publiclibs.R;

/**
  * 底部弹List出框
  * 
  * @author chenj
  * @date 2014-7-23
  */
public class BottomPopupDialogListView extends Dialog {

	/**
	 * @param context
	 * @param cancelable
	 * @param cancelListener
	 */
	private BottomPopupDialogListView(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	/**
	 * @param context
	 * @param theme
	 */
	private BottomPopupDialogListView(Context context, int theme) {
		super(context, theme);
	}

	/**
	 * @param context
	 */
	private BottomPopupDialogListView(Context context) {
		this(context, R.style.Bottom_Dialog_Theme);
	}

	private int arrayResId;
	private int[] drawableResId;
	private String title, subTitle;
	private PcEmDialogType[] types;
	private View.OnClickListener[] listeners;

	public BottomPopupDialogListView(Context context, int arrayResId, PcEmDialogType[] types, View.OnClickListener[] listeners, int[] drawableResId, String title, String subTitle) {
		this(context);

		this.arrayResId = arrayResId;
		this.types = types;
		this.listeners = listeners;
		this.drawableResId = drawableResId;
		this.title = title;
		this.subTitle = subTitle;
	}

	/**
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.alert_dialog_menu_layout);

		String itemValues[] = null;
		try {
			// 资源字符
			itemValues = getContext().getResources().getStringArray(arrayResId);
		} catch (Exception e) {
			return;
		}

		if (itemValues == null || itemValues.length == 0) {
			return;
		}

		FrameLayout titleFrame = (FrameLayout) findViewById(R.id.title_frame);

		// 没有Title
		if (StringUtils.isNull(subTitle) && StringUtils.isNull(title)) {
			titleFrame.setVisibility(View.GONE);
			findViewById(R.id.topBlank_frame).setVisibility(View.VISIBLE);
		} else {
			titleFrame.setVisibility(View.VISIBLE);
			findViewById(R.id.topBlank_frame).setVisibility(View.GONE);

			// 两种Title都有
			if (!StringUtils.isNull(subTitle) && !StringUtils.isNull(title)) {
				View eView = LayoutInflater.from(getContext()).inflate(R.layout.alert_dialog_menu_layout_extrastitle, null);
				TextView titleView = (TextView) eView.findViewById(R.id.title_testview);
				TextView subTitleView = (TextView) eView.findViewById(R.id.subTitle_testview);
				titleView.setText(title);
				subTitleView.setText(subTitle);

				titleFrame.addView(eView);
			}

			// 只有一种Title
			else {
				if (!StringUtils.isNull(title)) {
					View tView = LayoutInflater.from(getContext()).inflate(R.layout.alert_dialog_menu_layout_title, null);
					((TextView) tView).setText(title);

					titleFrame.addView(tView);
				} else {
					View sView = LayoutInflater.from(getContext()).inflate(R.layout.alert_dialog_menu_layout_subtitle, null);
					((TextView) sView).setText(subTitle);

					titleFrame.addView(sView);
				}
			}
		}

		ListView contentListView = (ListView) findViewById(R.id.content_list);
		Window window = getWindow();
		WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
		int[] wh = TerminalUtils.terminalWH(getContext());
		windowParams.x = 0;
		windowParams.y = wh[1];
		window.setAttributes(windowParams);// 控制dialog停放位置

		int listenerSize = listeners == null ? 0 : listeners.length;
		int drawableResIdSize = drawableResId == null ? 0 : drawableResId.length;
		int[] drawableResIds = new int[itemValues.length];
		View.OnClickListener[] tmpListener = new View.OnClickListener[itemValues.length];
		for (int i = 0; i < itemValues.length; i++) {
			if (i < listenerSize && listeners[i] != null) {
				tmpListener[i] = listeners[i];
			} else {
				if (i == itemValues.length - 1) {
					tmpListener[i] = new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							cancel();
						}
					};
				} else {
					tmpListener[i] = null;
				}
			}

			if (i < drawableResIdSize && drawableResId[i] != 0) {
				drawableResIds[i] = drawableResId[i];
			} else {
				drawableResIds[i] = 0;
			}
		}

		MenuAdapter menuAdapter = new MenuAdapter(getContext(), itemValues, types, drawableResIds, tmpListener);
		contentListView.setAdapter(menuAdapter);

		window.setBackgroundDrawableResource(R.drawable.alert_dialog_background);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);// 最终决定dialog的大小
	}

}
