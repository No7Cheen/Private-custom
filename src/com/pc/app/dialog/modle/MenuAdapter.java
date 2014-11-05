/**
 * @(#)MenuAdapter.java 2014-1-15 Copyright 2014 it.kedacom.com, Inc. All rights
 *                      reserved.
 */

package com.pc.app.dialog.modle;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.privatecustom.publiclibs.R;

/**
 * @author chenjian
 * @date 2014-1-15
 */

public class MenuAdapter extends BaseAdapter {

	private final int TYPE_MAX_COUNT = 3;

	private final int MENU_NORMAL = 0;
	private final int MENU_CANCEL = 1;
	private final int MENU_RED = 2;

	private LayoutInflater mLInflater;
	private String[] mMenuText;
	private int[] mDrawableResIds;
	private PcEmDialogType[] mTypes;
	private View.OnClickListener[] mListeners;

	private Context mContext;

	public MenuAdapter(Context context, String[] menuText, PcEmDialogType[] types, int[] drawableResIds, View.OnClickListener[] listener) {
		mContext = context;

		mTypes = types;
		mMenuText = menuText;
		mListeners = listener;
		mDrawableResIds = drawableResIds;

		mLInflater = LayoutInflater.from(context);
	}

	public void setDrawableResIds(int[] drawableResIds) {
		this.mDrawableResIds = drawableResIds;
	}

	public void setListeners(View.OnClickListener[] listeners) {
		mListeners = listeners;
	}

	@Override
	public int getCount() {
		if (mMenuText == null) {
			return 0;
		}

		return mMenuText.length;
	}

	@Override
	public String getItem(int position) {
		if (mMenuText == null || mMenuText.length == 0) {
			return "";
		}

		return mMenuText[position];
	}

	/**
	 * compound left Drawable resId
	 * 
	 * @param position
	 * @return
	 */
	public int getDrawableResId(int position) {
		if (mDrawableResIds == null || mDrawableResIds.length == 0) {
			return 0;
		}

		if (position < 0 || position >= mDrawableResIds.length) {
			return 0;
		}

		return mDrawableResIds[position];
	}

	/**
	 * compound left Drawable resId
	 * 
	 * @param position
	 * @return
	 */
	public Drawable getCompoundDrawableLeft(int position) {
		int resId = getDrawableResId(position);
		if (resId <= 0) {
			return null;
		}

		try {
			return mContext.getResources().getDrawable(resId);
		} catch (Exception e) {
			return null;
		}
	}

	public PcEmDialogType getType(int position) {
		if (mTypes == null || mTypes.length == 0) {
			return PcEmDialogType.normal;
		}

		if (position < 0 || position >= mTypes.length) {
			return PcEmDialogType.normal;
		}

		return mTypes[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_MAX_COUNT;
	}

	@Override
	public int getItemViewType(int position) {
		int type = MENU_NORMAL;
		switch (getType(position)) {
			case cancel:
				type = MENU_CANCEL;
				break;

			case red:
				type = MENU_RED;
				break;

			case normal:
			default:
				type = MENU_NORMAL;
				break;
		}

		return type;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int type = getItemViewType(position);
		final String menuText = getItem(position);

		if (convertView == null) {
			switch (type) {
				case MENU_CANCEL:
					convertView = mLInflater.inflate(R.layout.alert_dialog_menu_cancel_list_layout, null);
					break;

				case MENU_RED:
					convertView = mLInflater.inflate(R.layout.alert_dialog_redmenu_list_layout, null);
					break;

				case MENU_NORMAL:
				default:
					convertView = mLInflater.inflate(R.layout.alert_dialog_menu_list_layout, null);
					break;
			}
		}

		TextView itemText = (TextView) convertView.findViewById(R.id.popup_text);
		LinearLayout itemlayout = (LinearLayout) convertView.findViewById(R.id.popup_layout);
		itemText.setText(menuText);
		itemText.setCompoundDrawablesWithIntrinsicBounds(getDrawableResId(position), 0, 0, 0);
		// Drawable[] compoundDrawables = itemText.getCompoundDrawables();
		// if (null != compoundDrawables) {
		// if (null != compoundDrawables[3]) {
		// compoundDrawables[3] = mContext.getResources().getDrawable(R.drawable.divider_line);
		// }
		// itemText.setCompoundDrawables(getCompoundDrawableLeft(position), compoundDrawables[1], compoundDrawables[2],
		// compoundDrawables[3]);
		// } else {
		// itemText.setCompoundDrawablesWithIntrinsicBounds(getDrawableResId(position), 0, 0, 0);
		// }

		if (mListeners != null && mListeners[position] != null) {
			if (type == MENU_CANCEL) {
				itemText.setOnClickListener(mListeners[position]);
			} else {
				itemlayout.setOnClickListener(mListeners[position]);
			}
		}

		return convertView;
	}

}
