/**
 * @(#)IocViewManager.java   2014-8-25
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.app.view.ioc;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
  * 
  * @author chenj
  * @date 2014-8-25
  */

public class IocViewManager {

	/**
	 * 初始化View显示方式
	 *
	 * @param view
	 * @param visibility
	 */
	public static void initVisibility(View view, int visibility) {
		if (null == view || -1 == visibility) {
			return;
		}

		switch (visibility) {
			case View.GONE:
				view.setVisibility(View.GONE);
				break;

			case View.INVISIBLE:
				view.setVisibility(View.INVISIBLE);
				break;

			case View.VISIBLE:
				view.setVisibility(View.VISIBLE);
				break;
		}
	}

	/**
	 * 初始化TextView显示内容
	 *
	 * @param view
	 * @param resid
	 */
	public static void initTextResid(View view, int resid) {
		if (null == view || 0 == resid || !(view instanceof TextView)) {
			return;
		}

		((TextView) view).setText(resid);

		if (view instanceof EditText) {
			int len = ((EditText) view).getText().length();
			((EditText) view).setSelection(len);
		}
	}

	/**
	 * 初始化TextView Hint 
	 *
	 * @param view
	 * @param resid
	 */
	public static void initHintResid(View view, int resid) {
		if (null == view || 0 == resid || !(view instanceof TextView)) {
			return;
		}

		((TextView) view).setHint(resid);
	}

	/**
	 * 初始化ImageVie drawable
	 *
	 * @param view
	 * @param resid
	 */
	public static void initImageresId(View view, int resid) {
		if (null == view || 0 == resid || !(view instanceof ImageView)) {
			return;
		}

		((ImageView) view).setImageResource(resid);
	}
}
