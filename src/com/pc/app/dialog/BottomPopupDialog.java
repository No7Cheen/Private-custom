/**
 * @(#)BottomPopupDialog.java   2014-7-22
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pc.app.dialog.modle.PcEmDialogType;
import com.pc.utils.StringUtils;
import com.pc.utils.android.sys.TerminalUtils;
import com.privatecustom.publiclibs.R;

/**
  * 
  * @author chenj
  * @date 2014-7-22
  */

public class BottomPopupDialog extends Dialog {

	/**
	 * @param context
	 * @param cancelable
	 * @param cancelListener
	 */
	private BottomPopupDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	/**
	 * @param context
	 * @param theme
	 */
	private BottomPopupDialog(Context context, int theme) {
		super(context, theme);
	}

	/**
	 * @param context
	 */
	private BottomPopupDialog(Context context) {
		this(context, R.style.Bottom_Dialog_Theme);
	}

	private String title;
	private int arrayResId;
	private PcEmDialogType[] types;
	private View.OnClickListener[] listeners;

	public BottomPopupDialog(Context context, int arrayResId, PcEmDialogType[] types, View.OnClickListener[] listeners, String title) {
		this(context);

		this.title = title;
		this.arrayResId = arrayResId;
		this.types = types;
		this.listeners = listeners;
	}

	/**
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int size = types.length;
		String itemValues[] = null;
		try {
			// 资源字符
			itemValues = getContext().getResources().getStringArray(arrayResId);
		} catch (Exception e) {
		}

		if (itemValues == null) {
			return;
		}

		final LayoutInflater lInflater = LayoutInflater.from(getContext());

		float spacing = getContext().getResources().getDimension(R.dimen.alert_dialog_spacing);
		float spacingR = getContext().getResources().getDimension(R.dimen.alert_dialog_spacingRight);
		float spacingB = getContext().getResources().getDimension(R.dimen.alert_dialog_spacingBottom);
		float topBlank = getContext().getResources().getDimension(R.dimen.alert_dialog_spacingTopBlank);

		final int fill_parent = ViewGroup.LayoutParams.MATCH_PARENT;
		final int wrap_content = ViewGroup.LayoutParams.WRAP_CONTENT;
		int marginTop = (int) spacing;

		int padding = (int) spacingR;
		int paddingBottom = (int) spacingB;
		int paddingTop = StringUtils.isNull(title) ? (int) topBlank : (int) spacing;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(fill_parent, fill_parent);
		LinearLayout contentView = new LinearLayout(getContext());
		contentView.setLayoutParams(params);
		contentView.setPadding(padding, paddingTop, padding, 0);
		contentView.setGravity(Gravity.CENTER);
		contentView.setOrientation(LinearLayout.VERTICAL);

		// 没有Title
		if (StringUtils.isNull(title)) {
			View topBlankView = lInflater.inflate(R.layout.alert_dialog_menu_layout_topblank, null);
			contentView.addView(topBlankView, 0);
		} else {
			TextView titleView = (TextView) lInflater.inflate(R.layout.alert_dialog_menu_layout_title, null);
			titleView.setText(title);
			contentView.addView(titleView, 0);
		}

		for (int i = 0; i < size; i++) {
			LinearLayout.LayoutParams llLP = new LinearLayout.LayoutParams(fill_parent, wrap_content);
			llLP.setMargins(0, 0, 0, 0);

			View convertView = null;
			PcEmDialogType type = PcEmDialogType.normal;
			if (types != null && types.length > 0 && i < types.length) {
				type = types[i];
			}

			switch (type) {
				case cancel:
					convertView = lInflater.inflate(R.layout.alert_dialog_menu_cancel_list_layout, null);
					break;

				case red:
					convertView = lInflater.inflate(R.layout.alert_dialog_redmenu_list_layout, null);
					break;

				case normal:
				default:
					convertView = lInflater.inflate(R.layout.alert_dialog_menu_list_layout, null);
					break;
			}

			convertView.setLayoutParams(llLP);

			LinearLayout itemLayout = (LinearLayout) convertView.findViewById(R.id.popup_layout);
			TextView itemTextView = (TextView) convertView.findViewById(R.id.popup_text);
			itemTextView.setText(itemValues[i]);

			if (i == size - 1) {
				itemTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			}

			View.OnClickListener listener = null;
			if (listeners != null && listeners.length > 0 && i < listeners.length) {
				listener = listeners[i];
			}
			if (type == PcEmDialogType.cancel) {
				itemTextView.setOnClickListener(listener);
			} else {
				itemLayout.setOnClickListener(listener);
			}

			contentView.addView(convertView);
		}

		Window window = getWindow();
		WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
		int[] wh = TerminalUtils.terminalWH(getContext());
		windowParams.x = 0;
		windowParams.y = wh[1];
		window.setAttributes(windowParams);// 控制dialog停放位置

		window.setBackgroundDrawableResource(R.drawable.alert_dialog_background);
		setCanceledOnTouchOutside(true);
		setContentView(contentView);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);// 最终决定dialog的大小
	}
}
