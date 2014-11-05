/**
 * @(#)PcAbsFragment.java   2014-8-12
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 * <pre>
 * Fragment的生命周期
 * onAttach-->onCreate-->onCreateView-->onViewCreated-->onActivityCreated-->onStart-->onResume
 * onPause-->onStop-->onDestroyView-->onDestroy-->onDetach
 * </pre>
 *
 *
 */

package com.pc.app.v4fragment;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pc.app.base.PcActivity;
import com.pc.app.base.PcBaseActivity;
import com.pc.app.base.PcSwipeBackActivity;
import com.pc.app.base.PcSwipeBackFragmentActivity;
import com.pc.app.dialog.v4.PcDialogFragmentV4;

/**
  * Pc abstract Fragment
  * 
  * @author chenj
  * @date 2014-8-12
  */

public abstract class PcAbsFragment extends Fragment {

	protected PcFragmentLib mPcFragmentLib;

	/**
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		mPcFragmentLib = new PcFragmentLib(this);
	}

	/**
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * 
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public abstract View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

	/**
	 * @see android.support.v4.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mPcFragmentLib.initIocView();

		findViews();
		initComponentValue();
		registerListeners();
	}

	/**
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
	}

	/**
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
	}

	/**
	 * find views (eg:for id)
	 */
	public abstract void findViews();

	/**
	 * 初始化控件的值
	 */
	public abstract void initComponentValue();

	/**
	 * 注册监听事件
	 */
	public abstract void registerListeners();

	/**
	 * 注册广播
	 *
	 * @param receiver
	 * @param filter
	 * @return
	 */
	public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
		if (null == receiver) {
			return null;
		}

		mPcFragmentLib.addReceiver(receiver);

		return getActivity().registerReceiver(receiver, filter);
	}

	/**
	 * 注销广播
	 *
	 * @param receiver
	 */
	public void unregisterReceiver(BroadcastReceiver receiver) {
		if (null == receiver) {
			return;
		}

		getActivity().unregisterReceiver(receiver);
		mPcFragmentLib.removeReceiver(receiver);
	}

	/**
	 * 注销所有广播
	 */
	public void unregisterAllReceiver() {
		List<BroadcastReceiver> receivers = mPcFragmentLib.getReceiver();
		if (null == receivers || receivers.isEmpty()) {
			return;
		}

		for (BroadcastReceiver broadcastReceiver : receivers) {
			if (broadcastReceiver == null) continue;
			unregisterReceiver(broadcastReceiver);
		}
	}

	/**
	 * @see android.support.v4.app.Fragment#onConfigurationChanged(android.content.res.Configuration)
	 */

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);
	}

	/**
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
	}

	/**
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		super.onStop();
	}

	/**
	 * @see android.support.v4.app.Fragment#onDestroyView()
	 */
	@Override
	public void onDestroyView() {
		unregisterAllReceiver();
		dismissAllDialogFragment();

		super.onDestroyView();
	}

	/**
	 * @see android.support.v4.app.Fragment#onDetach()
	 */
	@Override
	public void onDetach() {

		super.onDetach();
	}

	/**
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {

		super.onDestroy();
	}

	/**
	 * @see android.support.v4.app.Fragment#onLowMemory()
	 */
	@Override
	public void onLowMemory() {

		super.onLowMemory();
	}

	/**
	 * @see android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);
	}

	/**
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 返回指定 DialogFragment
	 *
	 * @param tag
	 * @return
	 */
	public DialogFragment getDialogFragment(String tag) {
		Activity a = getActivity();
		if (a instanceof PcBaseActivity) {
			return ((PcBaseActivity) a).getDialogFragment(tag);
		}

		if (a instanceof PcActivity) {
			return ((PcActivity) a).getDialogFragment(tag);
		}

		if (a instanceof PcSwipeBackFragmentActivity) {
			return ((PcSwipeBackFragmentActivity) a).getDialogFragment(tag);
		}

		if (a instanceof PcSwipeBackActivity) {
			return ((PcSwipeBackActivity) a).getDialogFragment(tag);
		}

		return mPcFragmentLib.getDialogFragment(tag);
	}

	/**
	 * set DialogFragment
	 *
	 * @param dialog
	 * @param dialogTag
	 * @param show
	 */
	public void setDialogFragment(final DialogFragment dialog, final String dialogTag, final boolean show) {
		Activity a = getActivity();
		if (a instanceof PcBaseActivity) {
			((PcBaseActivity) a).setDialogFragment(dialog, dialogTag, show);
			return;
		}

		if (a instanceof PcActivity) {
			((PcActivity) a).setDialogFragment(dialog, dialogTag, show);
			return;
		}

		if (a instanceof PcSwipeBackFragmentActivity) {
			((PcSwipeBackFragmentActivity) a).setDialogFragment(dialog, dialogTag, show);
			return;
		}

		if (a instanceof PcSwipeBackActivity) {
			((PcSwipeBackActivity) a).setDialogFragment(dialog, dialogTag, show);
			return;
		}

		mPcFragmentLib.setDialogFragment(dialog, dialogTag);

		if (show && getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mPcFragmentLib.setCurrDialogTag(dialogTag);

					if (dialog instanceof PcDialogFragmentV4 && ((PcDialogFragmentV4) dialog).isShowing()) {
						return;
					}

					if (dialog.isAdded()) {
						return;
					}

					FragmentTransaction ft = getFragmentManager().beginTransaction();
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
					dialog.show(ft, dialogTag);
				}
			});
		}
	}

	/**
	 * 是否为当前Dialog
	 *
	 * @param tag
	 * @return
	 */
	public boolean isCurrDialog(String tag) {
		Activity a = getActivity();
		if (a instanceof PcBaseActivity) {
			return ((PcBaseActivity) a).isCurrDialog(tag);
		}

		if (a instanceof PcActivity) {
			return ((PcActivity) a).isCurrDialog(tag);
		}

		if (a instanceof PcSwipeBackFragmentActivity) {
			return ((PcSwipeBackFragmentActivity) a).isCurrDialog(tag);
		}

		if (a instanceof PcSwipeBackActivity) {
			return ((PcSwipeBackActivity) a).isCurrDialog(tag);
		}

		return mPcFragmentLib.isCurrDialog(tag);
	}

	/**
	 * Dialog is showing
	 *
	 * @param tag
	 * @return
	 */
	public boolean isShowingDialog(String tag) {
		Activity a = getActivity();
		if (a instanceof PcBaseActivity) {
			return ((PcBaseActivity) a).isShowingDialog(tag);
		}

		if (a instanceof PcActivity) {
			return ((PcActivity) a).isShowingDialog(tag);
		}

		if (a instanceof PcSwipeBackFragmentActivity) {
			return ((PcSwipeBackFragmentActivity) a).isShowingDialog(tag);
		}

		if (a instanceof PcSwipeBackActivity) {
			return ((PcSwipeBackActivity) a).isShowingDialog(tag);
		}

		return mPcFragmentLib.isShowingDialog(tag);
	}

	/**
	 * 当前Dialog是否显示 
	 *
	 * @return
	 */
	public boolean isShowingCurrDialog() {
		Activity a = getActivity();
		if (a instanceof PcBaseActivity) {
			return ((PcBaseActivity) a).isShowingCurrDialog();
		}

		if (a instanceof PcActivity) {
			return ((PcActivity) a).isShowingCurrDialog();
		}

		if (a instanceof PcSwipeBackFragmentActivity) {
			return ((PcSwipeBackFragmentActivity) a).isShowingCurrDialog();
		}

		if (a instanceof PcSwipeBackActivity) {
			return ((PcSwipeBackActivity) a).isShowingCurrDialog();
		}

		return mPcFragmentLib.isShowingCurrDialog();
	}

	/**
	 * 是否是当前Dialog，且是否显示
	 *
	 * @param tag
	 * @return
	 */
	public boolean isShowingCurrDialog(String tag) {
		Activity a = getActivity();
		if (a instanceof PcBaseActivity) {
			return ((PcBaseActivity) a).isShowingCurrDialog(tag);
		}

		if (a instanceof PcActivity) {
			return ((PcActivity) a).isShowingCurrDialog(tag);
		}

		if (a instanceof PcSwipeBackFragmentActivity) {
			return ((PcSwipeBackFragmentActivity) a).isShowingCurrDialog(tag);
		}

		if (a instanceof PcSwipeBackActivity) {
			return ((PcSwipeBackActivity) a).isShowingCurrDialog(tag);
		}

		return mPcFragmentLib.isShowingCurrDialog(tag);
	}

	/**
	 * 关闭DialogFragment
	 *
	 * @param tag
	 */
	public void closeDialogFragment(final String tag) {
		Activity a = getActivity();
		if (a instanceof PcBaseActivity) {
			((PcBaseActivity) a).closeDialogFragment(tag);
			return;
		}

		if (a instanceof PcActivity) {
			((PcActivity) a).closeDialogFragment(tag);
			return;
		}

		if (a instanceof PcSwipeBackFragmentActivity) {
			((PcSwipeBackFragmentActivity) a).closeDialogFragment(tag);
			return;
		}

		if (a instanceof PcSwipeBackActivity) {
			((PcSwipeBackActivity) a).closeDialogFragment(tag);
			return;
		}

		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					DialogFragment dialog = mPcFragmentLib.getDialogFragment(tag);
					mPcFragmentLib.setCurrDialogTag(null);

					if (null != dialog) {
						if (dialog.getDialog() != null) {
							dialog.getDialog().cancel();
						} else {
							dialog.dismiss();
						}
					}
				}
			});
		}
	}

	/**
	 * 关闭当前DialogFragment
	 * 
	 * @see com.pc.app.base.PcBaseActivity#closeCurrDialogFragment()
	 */
	public void closeCurrDialogFragment() {
		Activity a = getActivity();
		if (a instanceof PcBaseActivity) {
			((PcBaseActivity) a).closeCurrDialogFragment();
			return;
		}

		if (a instanceof PcActivity) {
			((PcActivity) a).closeCurrDialogFragment();
			return;
		}

		if (a instanceof PcSwipeBackFragmentActivity) {
			((PcSwipeBackFragmentActivity) a).closeCurrDialogFragment();
			return;
		}

		if (a instanceof PcSwipeBackActivity) {
			((PcSwipeBackActivity) a).closeCurrDialogFragment();
			return;
		}

		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					DialogFragment dialog = mPcFragmentLib.getCurrDialogFragment();
					mPcFragmentLib.setCurrDialogTag(null);

					if (null != dialog) {
						if (dialog.getDialog() != null) {
							dialog.getDialog().cancel();
						} else {
							dialog.dismiss();
						}
					}
				}
			});
		}
	}

	/**
	 * close all dialog
	 */
	public void closeAllDialogFragment() {
		Activity a = getActivity();
		if (a instanceof PcBaseActivity) {
			((PcBaseActivity) a).closeAllDialogFragment();
			return;
		}

		if (a instanceof PcActivity) {
			((PcActivity) a).closeAllDialogFragment();
			return;
		}

		if (a instanceof PcSwipeBackFragmentActivity) {
			((PcSwipeBackFragmentActivity) a).closeAllDialogFragment();
			return;
		}

		if (a instanceof PcSwipeBackActivity) {
			((PcSwipeBackActivity) a).closeAllDialogFragment();
			return;
		}

		final Map<String, DialogFragment> dialogs = mPcFragmentLib.getDialogFragments();
		if (null == dialogs || dialogs.isEmpty()) {
			return;
		}

		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mPcFragmentLib.setCurrDialogTag(null);
					if (dialogs.entrySet().isEmpty()) {
						return;
					}

					Iterator<Entry<String, DialogFragment>> it = dialogs.entrySet().iterator();
					if (null == it) {
						return;
					}

					while (it.hasNext()) {
						Entry<String, DialogFragment> entry = it.next();
						DialogFragment dialog = entry.getValue();
						if (null != dialog) {
							if (dialog.getDialog() != null) {
								dialog.getDialog().cancel();
							} else {
								dialog.dismiss();
							}
						}
					}
				}
			});
		}
	}

	/**
	 * 隐藏所有DialogFragment
	 */
	public void dismissAllDialogFragment() {
		Activity a = getActivity();
		if (a instanceof PcBaseActivity) {
			((PcBaseActivity) a).dismissAllDialogFragment();
			return;
		}

		if (a instanceof PcActivity) {
			((PcActivity) a).dismissAllDialogFragment();
			return;
		}

		if (a instanceof PcSwipeBackFragmentActivity) {
			((PcSwipeBackFragmentActivity) a).dismissAllDialogFragment();
			return;
		}

		if (a instanceof PcSwipeBackActivity) {
			((PcSwipeBackActivity) a).dismissAllDialogFragment();
			return;
		}

		final Map<String, DialogFragment> dialogs = mPcFragmentLib.getDialogFragments();
		if (null == dialogs || dialogs.isEmpty()) {
			return;
		}

		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mPcFragmentLib.setCurrDialogTag(null);
					if (dialogs.entrySet().isEmpty()) {
						return;
					}

					Iterator<Entry<String, DialogFragment>> it = dialogs.entrySet().iterator();
					if (null == it) {
						return;
					}

					while (it.hasNext()) {
						Entry<String, DialogFragment> entry = it.next();
						DialogFragment dialog = entry.getValue();
						if (null != dialog) {
							dialog.dismiss();
						}
					}
				}
			});
		}
	}

	/**
	 * 关闭当前DialogFragment
	 */
	public void dismissCurrDialogFragment() {
		Activity a = getActivity();
		if (a instanceof PcBaseActivity) {
			((PcBaseActivity) a).dismissCurrDialogFragment();
			return;
		}

		if (a instanceof PcActivity) {
			((PcActivity) a).dismissCurrDialogFragment();
			return;
		}

		if (a instanceof PcSwipeBackFragmentActivity) {
			((PcSwipeBackFragmentActivity) a).dismissCurrDialogFragment();
			return;
		}

		if (a instanceof PcSwipeBackActivity) {
			((PcSwipeBackActivity) a).dismissCurrDialogFragment();
			return;
		}

		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					DialogFragment dialog = mPcFragmentLib.getCurrDialogFragment();
					mPcFragmentLib.setCurrDialogTag(null);
					if (null != dialog) {
						dialog.dismiss();
					}
				}
			});
		}
	}

	/**
	 * 关闭指定DialogFragment
	 */
	public void dismissDialogFragment(final String tag) {
		Activity a = getActivity();
		if (a instanceof PcBaseActivity) {
			((PcBaseActivity) a).dismissDialogFragment(tag);
			return;
		}

		if (a instanceof PcActivity) {
			((PcActivity) a).dismissDialogFragment(tag);
			return;
		}

		if (a instanceof PcSwipeBackFragmentActivity) {
			((PcSwipeBackFragmentActivity) a).dismissDialogFragment(tag);
			return;
		}

		if (a instanceof PcSwipeBackActivity) {
			((PcSwipeBackActivity) a).dismissDialogFragment(tag);
			return;
		}

		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					DialogFragment dialog = mPcFragmentLib.getDialogFragment(tag);
					mPcFragmentLib.setCurrDialogTag(null);

					if (null != dialog) {
						dialog.dismiss();
					}
				}
			});
		}
	}

}
