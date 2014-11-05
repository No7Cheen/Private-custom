/**
 * @(#)PcActivityLib.java   2014-5-20
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.app.base;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;

import com.pc.app.dialog.v4.PcDialogFragmentV4;
import com.pc.app.view.ioc.EmMethod;
import com.pc.app.view.ioc.IocSelect;
import com.pc.app.view.ioc.IocView;
import com.pc.app.view.ioc.IocViewManager;
import com.pc.utils.StringUtils;
import com.pc.view.listener.AbIocEventListener;

/**
 * Pc Activity Lib
 * 
 * @author chenj
 * @date 2014-5-20
 */
public class PcActivityLib {

	// show dailog value
	private static final int SHOW = 0x145;

	// FragmentActivity
	private Activity mActivity;

	private String currDialogTag;
	private Map<String, DialogFragment> mDialogFragments;
	private List<BroadcastReceiver> mReceivers;

	private final Handler mHandler = new Handler();
	private final Handler mSetDialogHandler = new Handler() {

		/**
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (null == msg) return;

			if (null == mDialogFragments) return;

			if (null == mActivity || mActivity.isFinishing()) {
				return;
			}

			DialogFragment dft = mDialogFragments.get((String) msg.obj);
			if (null == dft) {
				return;
			}

			if (msg.what == SHOW) {
				currDialogTag = (String) msg.obj;

				if (dft instanceof PcDialogFragmentV4 && ((PcDialogFragmentV4) dft).isShowing()) {
					return;
				}

				if (dft.isAdded()) {
					return;
				}

				if (mActivity instanceof FragmentActivity) {
					FragmentTransaction ft = ((FragmentActivity) mActivity).getSupportFragmentManager().beginTransaction();
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
					dft.show(ft, (String) msg.obj);
				}
			}
		}

	};
	private final Runnable mDismissAction = new Runnable() {

		public void run() {
			dismiss(currDialogTag);
		}
	};
	private final Runnable mCloseAction = new Runnable() {

		public void run() {
			close(currDialogTag);
		}
	};

	public PcActivityLib(FragmentActivity _Activity) {
		mActivity = _Activity;

		mReceivers = new ArrayList<BroadcastReceiver>();
		mDialogFragments = Collections.synchronizedMap(new LinkedHashMap<String, DialogFragment>());
	}

	/**
	 * BroadcastReceiver
	 *
	 * @return
	 */
	public List<BroadcastReceiver> getReceiver() {
		return mReceivers;
	}

	/**
	 * 存储BroadcastReceiver
	 *
	 * @param receiver
	 */
	public void addReceiver(BroadcastReceiver receiver) {
		if (null == receiver) {
			return;
		}

		if (null == mReceivers) {
			mReceivers = new ArrayList<BroadcastReceiver>();
		}

		if (mReceivers.contains(receiver)) {
			return;
		}

		mReceivers.add(receiver);
	}

	/**
	 * 删除BroadcastReceiver
	 *
	 * @param receiver
	 */
	public void removeReceiver(BroadcastReceiver receiver) {
		if (null == receiver || null == mReceivers || mReceivers.isEmpty()) {
			return;
		}

		if (!mReceivers.contains(receiver)) {
			return;
		}

		mReceivers.remove(receiver);
		receiver = null;
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

			HashMap<Field, View> pfieldMap = new HashMap<Field, View>();

			for (Field field : fields) {
				field.setAccessible(true);
				if (field.get(mActivity) != null) continue;
				IocView viewInject = field.getAnnotation(IocView.class);

				if (viewInject == null) continue;

				int viewId = viewInject.id();
				int viewPid = viewInject.parentId();
				if (viewPid > 0) {
					pfieldMap.put(field, mActivity.findViewById(viewPid));
					continue;
				}

				field.set(mActivity, mActivity.findViewById(viewId));
				IocViewManager.initVisibility(mActivity.findViewById(viewId), viewInject.visibility());
				IocViewManager.initHintResid(mActivity.findViewById(viewId), viewInject.initHintResid());
				IocViewManager.initTextResid(mActivity.findViewById(viewId), viewInject.initTextResid());
				IocViewManager.initImageresId(mActivity.findViewById(viewId), viewInject.initImageresId());

				setListener(field, viewInject.click(), EmMethod.Click);
				setListener(field, viewInject.longClick(), EmMethod.LongClick);
				setListener(field, viewInject.itemClick(), EmMethod.ItemClick);
				setListener(field, viewInject.itemLongClick(), EmMethod.itemLongClick);

				IocSelect select = viewInject.select();
				if (!TextUtils.isEmpty(select.selected())) {
					setViewSelectListener(field, select.selected(), select.noSelected());
				}
			}

			if (!pfieldMap.entrySet().isEmpty()) {
				Iterator<Entry<Field, View>> it = pfieldMap.entrySet().iterator();
				while (it.hasNext()) {
					Entry<Field, View> entry = it.next();
					if (null == entry) continue;

					Field field = entry.getKey();
					View parentView = entry.getValue();
					if (null == field || null == parentView) continue;
					if (field.get(mActivity) != null) continue;

					IocView viewInject = field.getAnnotation(IocView.class);
					if (viewInject == null) continue;

					int viewId = viewInject.id();
					int viewParentid = viewInject.parentId();
					if (viewParentid <= 0) continue;

					field.set(mActivity, parentView.findViewById(viewId));
					IocViewManager.initVisibility(parentView.findViewById(viewId), viewInject.visibility());
					IocViewManager.initHintResid(parentView.findViewById(viewId), viewInject.initHintResid());
					IocViewManager.initTextResid(parentView.findViewById(viewId), viewInject.initTextResid());
					IocViewManager.initImageresId(parentView.findViewById(viewId), viewInject.initImageresId());

					setListener(field, viewInject.click(), EmMethod.Click);
					setListener(field, viewInject.longClick(), EmMethod.LongClick);
					setListener(field, viewInject.itemClick(), EmMethod.ItemClick);
					setListener(field, viewInject.itemLongClick(), EmMethod.itemLongClick);

					IocSelect select = viewInject.select();
					if (!TextUtils.isEmpty(select.selected())) {
						setViewSelectListener(field, select.selected(), select.noSelected());
					}
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

	/**
	 * 设置DialogFragment
	 *
	 * @param dialog DialogFragment
	 * @param tag
	 * @param show
	 */
	public void setDialogFragment(final DialogFragment dialog, final String tag, final boolean show) {
		if (null == mActivity || mActivity.isFinishing()) return;

		mDialogFragments.put(tag, dialog);

		Message showMessage = new Message();
		showMessage.obj = tag;
		if (show) {
			showMessage.what = SHOW;
			currDialogTag = tag;
		}
		// mSetDialogHandler.sendMessageDelayed(showMessage, 10);
		mSetDialogHandler.sendMessage(showMessage);
	}

	/**
	 * 获取DialogFragment
	 *
	 * @param tag
	 * @return
	 */
	public DialogFragment getDialogFragment(String tag) {
		try {
			return mDialogFragments.get(tag);
		} catch (Exception e) {
			return null;
		}
	}

	/** 
	 * 获取当前DialogFragment
	 * 
	 * @return the currDialogTag
	 */
	public String getCurrDialogTag() {
		return currDialogTag;
	}

	/**
	 * 是否为当前Dialog
	 *
	 * @param tag
	 * @return
	 */
	public boolean isCurrDialog(String tag) {
		if (StringUtils.isNull(currDialogTag)) {
			return false;
		}

		if (!StringUtils.equals(currDialogTag, tag)) {
			return false;
		}

		return (null != getDialogFragment(tag));
	}

	/**
	 * Dialog is showing
	 *
	 * @param tag
	 * @return
	 */
	public boolean isShowingDialog(String tag) {
		DialogFragment df = getDialogFragment(tag);
		if (null == df) {
			return false;
		}

		if (df instanceof PcDialogFragmentV4) {
			return ((PcDialogFragmentV4) df).isShowing();
		}

		if (null == df.getDialog()) {
			return false;
		}

		return df.getDialog().isShowing();
	}

	/**
	 * 当前Dialog是否显示 
	 *
	 * @return
	 */
	public boolean isShowingCurrDialog() {
		return isShowingDialog(getCurrDialogTag());
	}

	/**
	 * 是否是当前Dialog，且是否显示
	 *
	 * @param tag
	 * @return
	 */
	public boolean isShowingCurrDialog(String tag) {
		if (StringUtils.isNull(currDialogTag)) {
			return false;
		}

		if (!StringUtils.equals(currDialogTag, tag)) {
			return false;
		}

		DialogFragment df = getDialogFragment(tag);
		if (null == df) {
			return false;
		}

		if (df instanceof PcDialogFragmentV4) {
			return ((PcDialogFragmentV4) df).isShowing();
		}

		if (null == df.getDialog()) {
			return false;
		}

		return df.getDialog().isShowing();
	}

	/**
	 * close dialog
	 */
	public void closeDialogFragment(String tag) {
		currDialogTag = tag;

		if (Looper.myLooper() == mHandler.getLooper()) {
			close(tag);
		} else {
			mHandler.post(mCloseAction);
		}
	}

	/**
	 * close all dialog
	 */
	public void closeAllDialogFragment() {
		if (null == mDialogFragments || mDialogFragments.isEmpty()) {
			return;
		}

		Iterator<String> keyIt = mDialogFragments.keySet().iterator();
		if (null == keyIt) return;

		while (keyIt.hasNext()) {
			String key = keyIt.next();
			if (StringUtils.isNull(key)) continue;

			closeDialogFragment(key);
		}
	}

	/**
	 * dismiss all dialog
	 */
	public void dismissAllDialogFragment() {
		if (null == mDialogFragments || mDialogFragments.isEmpty()) {
			return;
		}

		Iterator<String> keyIt = mDialogFragments.keySet().iterator();
		if (null == keyIt) return;

		while (keyIt.hasNext()) {
			String key = keyIt.next();
			if (StringUtils.isNull(key)) continue;

			dismissDialogFragment(key);
		}
	}

	/**
	 * dismiss dialog
	 */
	public void dismissDialogFragment(String tag) {
		currDialogTag = tag;

		if (Looper.myLooper() == mHandler.getLooper()) {
			dismiss(tag);
		} else {
			mHandler.post(mDismissAction);
		}
	}

	/**
	 * dismiss dialog
	 * 
	 * @param tag
	 */
	private void dismiss(String tag) {
		DialogFragment d = getDialogFragment(tag);
		if (null == d) return;

		if (d instanceof PcDialogFragmentV4) {
			((PcDialogFragmentV4) d).dismiss();
			return;
		}

		d.dismiss();

		if (StringUtils.equals(currDialogTag, tag)) {
			currDialogTag = "";
		}
	}

	/**
	 * cloas dialog
	 *
	 * @param tag
	 */
	private void close(String tag) {
		DialogFragment d = getDialogFragment(tag);
		if (null == d) return;

		if (null != d.getDialog()) {
			d.getDialog().cancel();
		} else {
			if (d instanceof PcDialogFragmentV4) {
				((PcDialogFragmentV4) d).dismiss();
			} else {
				d.dismiss();
			}
		}

		if (StringUtils.equals(currDialogTag, tag)) {
			currDialogTag = "";
		}
	}

}
