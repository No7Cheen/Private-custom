/**
 * @(#)PcActivityLib.java   2014-5-20
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.app.base;

import java.lang.reflect.Field;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;

import com.pc.app.view.ioc.EmMethod;
import com.pc.app.view.ioc.IocSelect;
import com.pc.app.view.ioc.IocView;
import com.pc.view.listener.AbIocEventListener;

/**
 * @author chenj
 * @date 2014-5-20
 */

public class PcActivityLib_back {

	protected Dialog mDialog;
	private Activity mActivity;

	private static final int SHOW = 0x145;
	private final Handler mHandler = new Handler();
	private final Handler mSetDialogHandler = new Handler() {

		/**
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (null == msg) return;

			if (null == mActivity || mActivity.isDestroyed() || mActivity.isFinishing()) {
				return;
			}

			mDialog = (Dialog) msg.obj;
			if (null == mDialog) {
				return;
			}

			mDialog.setOwnerActivity(mActivity);
			if (msg.what == SHOW && !mDialog.isShowing()) {
				mDialog.show();
			}
		}

	};
	private final Runnable mDismissAction = new Runnable() {

		public void run() {
			dismiss();
		}
	};
	private final Runnable mCloseAction = new Runnable() {

		public void run() {
			close();
		}
	};

	public PcActivityLib_back(Activity _Activity) {
		this(_Activity, null);
	}

	public PcActivityLib_back(Activity _Activity, Dialog _Dialog) {
		mActivity = _Activity;
		mDialog = _Dialog;
	}

	/**
	 * 初始化为IOC控制的View
	 */
	public void initIocView() {
		if (null == mActivity) {
			return;
		}

		try {
			Field[] fields = mActivity.getClass().getDeclaredFields();
			if (null == fields || fields.length <= 0) {
				return;
			}

			for (Field field : fields) {
				field.setAccessible(true);
				if (field.get(mActivity) != null) continue;
				IocView viewInject = field.getAnnotation(IocView.class);

				if (viewInject == null) continue;

				int viewId = viewInject.id();
				field.set(mActivity, mActivity.findViewById(viewId));

				setListener(field, viewInject.click(), EmMethod.Click);
				setListener(field, viewInject.longClick(), EmMethod.LongClick);
				setListener(field, viewInject.itemClick(), EmMethod.ItemClick);
				setListener(field, viewInject.itemLongClick(), EmMethod.itemLongClick);

				IocSelect select = viewInject.select();
				if (!TextUtils.isEmpty(select.selected())) {
					setViewSelectListener(field, select.selected(), select.noSelected());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置view的监听器
	 * 
	 * @param field the field
	 * @param select the select
	 * @param noSelect the no select
	 * @throws Exception the exception
	 */
	public void setViewSelectListener(Field field, String select, String noSelect) throws Exception {
		Object obj = field.get(mActivity);
		if (obj instanceof View) {
			((AbsListView) obj).setOnItemSelectedListener(new AbIocEventListener(mActivity).select(select).noSelect(noSelect));
		}
	}

	/**
	 * 设置view的监听器
	 * 
	 * @param field the field
	 * @param methodName the method name
	 * @param method the method
	 * @throws Exception the exception
	 */
	public void setListener(Field field, String methodName, EmMethod method) throws Exception {
		if (methodName == null || methodName.trim().length() == 0) return;

		Object obj = field.get(mActivity);

		switch (method) {
			case Click:
				if (obj instanceof View) {
					((View) obj).setOnClickListener(new AbIocEventListener(mActivity).click(methodName));
				}
				break;

			case ItemClick:
				if (obj instanceof AbsListView) {
					((AbsListView) obj).setOnItemClickListener(new AbIocEventListener(mActivity).itemClick(methodName));
				}
				break;

			case LongClick:
				if (obj instanceof View) {
					((View) obj).setOnLongClickListener(new AbIocEventListener(mActivity).longClick(methodName));
				}
				break;

			case itemLongClick:
				if (obj instanceof AbsListView) {
					((AbsListView) obj).setOnItemLongClickListener(new AbIocEventListener(mActivity).itemLongClick(methodName));
				}
				break;

			default:
				break;
		}
	}

	public void setDialog(final Dialog dialog, final int dialogId, final boolean show) {
		Message showMessage = mSetDialogHandler.obtainMessage();
		if (null == showMessage) return;

		showMessage.obj = dialog;
		showMessage.arg1 = dialogId;
		if (show) {
			showMessage.what = SHOW;
		}
		showMessage.sendToTarget();
	}

	public Dialog getDialog() {
		return mDialog;
	}

	/**
	 * close dialog
	 */
	public void closeDialog() {
		if (Looper.myLooper() == mHandler.getLooper()) {
			close();
		} else {
			mHandler.post(mCloseAction);
		}
	}

	/**
	 * dismiss dialog
	 */
	public void dismissDialog() {
		if (Looper.myLooper() == mHandler.getLooper()) {
			dismiss();
		} else {
			mHandler.post(mDismissAction);
		}
	}

	private void dismiss() {
		if (null == mDialog) {
			return;
		}

		mDialog.dismiss();
	}

	private void close() {
		if (null == mDialog) {
			return;
		}

		mDialog.cancel();
		mDialog = null;
	}
}
