/**
 * @(#)BottomPopupDialogFragmentListView.java   2014-7-23
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.app.dialog.v4;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.pc.app.dialog.modle.MenuAdapter;
import com.pc.app.dialog.modle.PcEmDialogType;
import com.pc.utils.StringUtils;
import com.pc.utils.dialog.PcDialogUtil;
import com.privatecustom.publiclibs.R;

/**
 * 底部弹出列表框
 *  
 * onAttach-->onCreateDialog-->onCreateView-->onViewCreated-->onResume
 * 
 * @author chenj
 * @date 2014-7-23
 */

public class BottomPopupDialogFragmentListView extends PcDialogFragmentV4 {

	private int arrayResId;
	private int[] drawableResId;
	private String title, subTitle;
	private PcEmDialogType[] types;
	private View.OnClickListener[] listeners;

	private MenuAdapter mMenuAdapter;

	public BottomPopupDialogFragmentListView(int arrayResId, PcEmDialogType[] types, View.OnClickListener[] listeners, int[] drawableResId, String title, String subTitle) {
		this.arrayResId = arrayResId;
		this.types = types;
		this.listeners = listeners;
		this.drawableResId = drawableResId;
		this.title = title;
		this.subTitle = subTitle;

		setStyle(DialogFragment.STYLE_NORMAL, R.style.Bottom_Dialog_Theme);
	}

	public BottomPopupDialogFragmentListView(int arrayResId, PcEmDialogType[] types, View.OnClickListener[] listeners, int[] drawableResId) {
		this.arrayResId = arrayResId;
		this.types = types;
		this.listeners = listeners;
		this.drawableResId = drawableResId;

		setStyle(DialogFragment.STYLE_NORMAL, R.style.Bottom_Dialog_Theme);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	/**
	 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog d = super.onCreateDialog(savedInstanceState);
		if (null != d) {
			PcDialogUtil.setAttributes4PupfromBottom(getActivity(), d);
		}

		return d;
	}

	/**
	 * Set a listener to be invoked when the dialog is canceled.
	 *
	 * @param cancelListener
	 */
	public void setOnCancelListener(OnCancelListener cancelListener) {
		if (null == getDialog()) {
			return;
		}

		getDialog().setOnCancelListener(cancelListener);
	}

	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.alert_dialog_menu_layout, container, false);
	}

	/**
	 * @see android.support.v4.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		String itemValues[] = null;
		try {
			// 资源字符
			itemValues = getResources().getStringArray(arrayResId);
		} catch (Exception e) {
			return;
		}

		if (itemValues == null || itemValues.length == 0) {
			return;
		}

		setTitles(title, subTitle);

		int[] drawableResIds = new int[itemValues.length];
		int listenerSize = listeners == null ? 0 : listeners.length;
		int drawableResIdSize = drawableResId == null ? 0 : drawableResId.length;

		View.OnClickListener[] tmpListener = new View.OnClickListener[itemValues.length];

		for (int i = 0; i < itemValues.length; i++) {
			if (i < listenerSize && listeners[i] != null) {
				tmpListener[i] = listeners[i];
			} else {
				tmpListener[i] = null;
			}

			if (i < drawableResIdSize && drawableResId[i] > 0) {
				drawableResIds[i] = drawableResId[i];
			} else {
				drawableResIds[i] = 0;
			}
		}

		ListView contentListView = (ListView) getView().findViewById(R.id.content_list);
		mMenuAdapter = new MenuAdapter(getActivity(), itemValues, types, drawableResIds, tmpListener);
		contentListView.setAdapter(mMenuAdapter);
	}

	/**
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();

		PcDialogUtil.updatewWindowParamsWH4PupfromBottom(getActivity(), getDialog());
	}

	/**
	 * set title
	 *
	 * @param title
	 * @param subTitle
	 */
	public void setTitles(String title, String subTitle) {
		if (null == getView()) {
			return;
		}

		this.title = title;
		this.subTitle = subTitle;

		FrameLayout titleFrame = (FrameLayout) getView().findViewById(R.id.title_frame);

		// 没有Title
		if (StringUtils.isNull(subTitle) && StringUtils.isNull(title)) {
			titleFrame.setVisibility(View.GONE);
			getView().findViewById(R.id.topBlank_frame).setVisibility(View.VISIBLE);
		} else {
			titleFrame.setVisibility(View.VISIBLE);
			getView().findViewById(R.id.topBlank_frame).setVisibility(View.GONE);

			// 两种Title都有
			if (!StringUtils.isNull(subTitle) && !StringUtils.isNull(title)) {
				View eView = LayoutInflater.from(getActivity()).inflate(R.layout.alert_dialog_menu_layout_extrastitle, null);
				TextView titleView = (TextView) eView.findViewById(R.id.title_testview);
				TextView subTitleView = (TextView) eView.findViewById(R.id.subTitle_testview);
				titleView.setText(title);
				subTitleView.setText(subTitle);

				titleFrame.addView(eView);
			}

			// 只有一种Title
			else {
				if (!StringUtils.isNull(title)) {
					View tView = LayoutInflater.from(getActivity()).inflate(R.layout.alert_dialog_menu_layout_title, null);
					((TextView) tView).setText(title);

					titleFrame.addView(tView);
				} else {
					View sView = LayoutInflater.from(getActivity()).inflate(R.layout.alert_dialog_menu_layout_subtitle, null);
					((TextView) sView).setText(subTitle);

					titleFrame.addView(sView);
				}
			}
		}
	}

	/**
	 * set drawableResId
	 *
	 * @param drawableResId
	 */
	public void setDrawableResIds(int[] drawableResId) {
		String itemValues[] = null;
		try {
			itemValues = getResources().getStringArray(arrayResId);
		} catch (Exception e) {
			return;
		}

		if (itemValues == null || itemValues.length == 0) {
			return;
		}

		int drawableResIdSize = drawableResId == null ? 0 : drawableResId.length;
		int[] drawableResIds = new int[itemValues.length];

		for (int i = 0; i < itemValues.length; i++) {
			if (i < drawableResIdSize && drawableResId[i] != 0) {
				drawableResIds[i] = drawableResId[i];
			} else {
				drawableResIds[i] = 0;
			}
		}

		this.drawableResId = drawableResIds;
		if (null != mMenuAdapter) {
			mMenuAdapter.setDrawableResIds(drawableResIds);
			mMenuAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * set listeners 
	 *
	 * @param listeners
	 */
	public void setListeners(View.OnClickListener[] listeners) {
		this.listeners = listeners;

		String itemValues[] = null;
		try {
			itemValues = getResources().getStringArray(arrayResId);
		} catch (Exception e) {
			return;
		}

		if (itemValues == null || itemValues.length == 0) {
			return;
		}

		int listenerSize = listeners == null ? 0 : listeners.length;
		View.OnClickListener[] tmpListener = new View.OnClickListener[itemValues.length];
		for (int i = 0; i < itemValues.length; i++) {
			if (i < listenerSize && listeners[i] != null) {
				tmpListener[i] = listeners[i];
			} else {
				if (i == itemValues.length - 1) {
					tmpListener[i] = new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							if (null != getDialog()) {
								getDialog().cancel();
							}
						}
					};
				} else {
					tmpListener[i] = null;
				}
			}
		}

		this.listeners = tmpListener;

		if (null != mMenuAdapter) {
			mMenuAdapter.setListeners(this.listeners);
		}
	}

}
