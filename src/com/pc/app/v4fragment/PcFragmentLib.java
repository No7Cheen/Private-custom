/**
 * @(#)PcFragmentLib.java   2014-8-12
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.app.v4fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.BroadcastReceiver;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.View;

import com.pc.app.dialog.v4.PcDialogFragmentV4;
import com.pc.app.v4fragment.ioc.FragmentIocView;
import com.pc.app.view.ioc.IocViewManager;
import com.pc.utils.StringUtils;

/**
  * 
  * @author chenj
  * @date 2014-8-12
  */

public class PcFragmentLib {

	private Fragment mFragment;
	private List<BroadcastReceiver> mReceivers;

	private String currDialogTag;
	private Map<String, DialogFragment> mDialogFragments;

	public PcFragmentLib(Fragment _Fragment) {
		mFragment = _Fragment;
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
		if (null == mFragment) {
			return;
		}

		View v = mFragment.getView();
		if (null == v) {
			return;
		}

		try {
			Field[] fields = mFragment.getClass().getDeclaredFields();
			if (null == fields || fields.length <= 0) {
				return;
			}

			HashMap<Field, View> pfieldMap = new HashMap<Field, View>();

			for (Field field : fields) {
				field.setAccessible(true);
				if (field.get(mFragment) != null) continue;

				FragmentIocView viewInject = field.getAnnotation(FragmentIocView.class);
				if (viewInject == null) continue;

				int viewId = viewInject.id();
				int viewPid = viewInject.parentId();
				if (viewPid > 0) {
					pfieldMap.put(field, v.findViewById(viewPid));
					continue;
				}

				field.set(mFragment, v.findViewById(viewId));
				IocViewManager.initVisibility(v.findViewById(viewId), viewInject.visibility());
				IocViewManager.initHintResid(v.findViewById(viewId), viewInject.initHintResid());
				IocViewManager.initTextResid(v.findViewById(viewId), viewInject.initTextResid());
				IocViewManager.initImageresId(v.findViewById(viewId), viewInject.initImageresId());
			}

			if (!pfieldMap.entrySet().isEmpty()) {
				Iterator<Entry<Field, View>> it = pfieldMap.entrySet().iterator();
				while (it.hasNext()) {
					Entry<Field, View> entry = it.next();
					if (null == entry) continue;

					Field field = entry.getKey();
					View parentView = entry.getValue();
					if (null == field || null == parentView) continue;
					if (field.get(mFragment) != null) continue;

					FragmentIocView viewInject = field.getAnnotation(FragmentIocView.class);
					if (viewInject == null) continue;

					int viewId = viewInject.id();
					int viewParentid = viewInject.parentId();
					if (viewParentid <= 0) continue;

					field.set(mFragment, parentView.findViewById(viewId));
					IocViewManager.initVisibility(parentView.findViewById(viewId), viewInject.visibility());
					IocViewManager.initHintResid(parentView.findViewById(viewId), viewInject.initHintResid());
					IocViewManager.initTextResid(parentView.findViewById(viewId), viewInject.initTextResid());
					IocViewManager.initImageresId(parentView.findViewById(viewId), viewInject.initImageresId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置DialogFragment
	 *
	 * @param dialog DialogFragment
	 * @param tag
	 */
	public void setDialogFragment(final DialogFragment dialog, final String tag) {
		if (null == mFragment || mFragment.isHidden() || mFragment.isRemoving() || mFragment.isDetached()) return;

		mDialogFragments.put(tag, dialog);
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
	 * 删除DialogFragment
	 *
	 * @param tag
	 */
	public void removeDialogFragment(String tag) {
		if (null == mDialogFragments) return;

		mDialogFragments.remove(tag);
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

	public DialogFragment getCurrDialogFragment() {
		return getDialogFragment(currDialogTag);
	}

	public Map<String, DialogFragment> getDialogFragments() {
		return mDialogFragments;
	}

	/** 
	 * 获取当前DialogFragment
	 * 
	 * @return the currDialogTag
	 */
	public String getCurrDialogTag() {
		return currDialogTag;
	}

	public void setCurrDialogTag(String currDialogTag) {
		this.currDialogTag = currDialogTag;
	}

}
