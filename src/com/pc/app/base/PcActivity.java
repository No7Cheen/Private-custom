/**
 * @(#)PcActivity.java 2014-5-16 Copyright 2014 it.kedacom.com, Inc. All rights
 *                     reserved.
 */

package com.pc.app.base;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;

import com.pc.app.PcAppStackManager;
import com.pc.utils.android.sys.ActivityUtils;
import com.pc.utils.ime.ImeUtil;
import com.pc.utils.toast.PcToastUtil;

/**
 * pc ActionBar Activity 
 * 
 * @author chenjian
 * @date 2014-5-16
 */

public abstract class PcActivity extends ActionBarActivity implements PcIActivity {

	private boolean isAvailable = true;
	private PcActivityLib mPcActivityLib;

	/**
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		isAvailable = true;
		mPcActivityLib = new PcActivityLib(this);
		PcAppStackManager.Instance().pushActivity(this);
	}

	/**
	 * @see android.support.v7.app.ActionBarActivity#setContentView(int)
	 */
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);

		mPcActivityLib.initIocView();
	}

	/**
	 * initialize action bar
	 */
	protected void initActionBar() {

	}

	/**
	 * created View.建议在OnCreate()或在onPostCreate()中调用
	 */
	protected void onViewCreated() {
		findViews();
		initComponentValue();
		registerListeners();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

	/**
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		isAvailable = true;

		super.onRestart();
	}

	/**
	 * @see android.support.v4.app.FragmentActivity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
	}

	/**
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		isAvailable = true;

		super.onResume();
	}

	/**
	 * @see android.support.v7.app.ActionBarActivity#onPostResume()
	 */
	@Override
	protected void onPostResume() {
		super.onPostResume();
	}

	@Override
	public boolean hasExtra(String pExtraKey) {
		Bundle b = getExtra();
		if (null == b) return false;

		return b.containsKey(pExtraKey);
	}

	@Override
	public String getAction() {
		Intent intent = getIntent();
		if (null == intent) {
			return "";
		}

		return intent.getAction();
	}

	@Override
	public Bundle getExtra() {
		Intent intent = getIntent();
		if (null == intent) {
			return null;
		}

		return intent.getExtras();
	}

	@Override
	public void initExtras() {

	}

	/**
	 * @see com.pc.app.base.PcBaseActivity#registerReceivers()
	 */
	@Override
	public void registerReceivers() {
	}

	/**
	 * @see com.pc.app.base.PcBaseActivity#unregisterReceivers()
	 */
	@Override
	public void unregisterReceivers() {
	}

	/**
	 * @see android.content.ContextWrapper#registerReceiver(android.content.BroadcastReceiver, android.content.IntentFilter)
	 */
	@Override
	public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
		if (null == receiver) {
			return null;
		}

		mPcActivityLib.addReceiver(receiver);

		return super.registerReceiver(receiver, filter);
	}

	/**
	 * @see android.content.ContextWrapper#unregisterReceiver(android.content.BroadcastReceiver)
	 */
	@Override
	public void unregisterReceiver(BroadcastReceiver receiver) {
		if (null == receiver) {
			return;
		}

		super.unregisterReceiver(receiver);
		mPcActivityLib.removeReceiver(receiver);
	}

	/**
	 * @see android.support.v4.app.FragmentActivity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
	}

	/**
	 * @see android.support.v7.app.ActionBarActivity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();

		isAvailable = false;
	}

	/**
	 * @see com.pc.app.base.PcIBaseActivity#isAvailable()
	 */
	@Override
	public boolean isAvailable() {
		return isAvailable;
	}

	/**
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		isAvailable = false;

		List<BroadcastReceiver> receivers = mPcActivityLib.getReceiver();
		if (receivers != null && !receivers.isEmpty()) {
			for (BroadcastReceiver broadcastReceiver : receivers) {
				if (broadcastReceiver == null) continue;
				unregisterReceiver(broadcastReceiver);
			}
		}

		PcAppStackManager.Instance().popActivity(this, false);

		super.onDestroy();
	}

	/**
	 * @see android.app.Activity#finish()
	 */
	@Override
	public void finish() {
		isAvailable = false;

		super.finish();
	}

	@Override
	public void onBackPressed() {
		isAvailable = false;

		super.onBackPressed();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public boolean isFinishing() {
		return super.isFinishing();
	}

	@Override
	public boolean isDestroyed() {
		return super.isDestroyed();
	}

	/**
	 * @see com.pc.app.base.PcIBaseActivity#onFinish()
	 */
	@Override
	public void onFinish() {
		finish(true);
	}

	/**
	 * @see com.pc.app.base.PcIActivity#finish(boolean)
	 */
	@Override
	public void finish(boolean checkSoftInput) {
		finish(checkSoftInput, -1, -1);
	}

	@Override
	public void finish(int enterAnim, int exitAnim) {
		finish(false, enterAnim, enterAnim);
	}

	/**
	 * @see com.pc.app.base.PcIActivity#finish(boolean, int, int)
	 */
	@Override
	public void finish(boolean checkSoftInput, int enterAnim, int exitAnim) {
		isAvailable = false;

		if (checkSoftInput && ImeUtil.isImeShow(getApplicationContext())) {
			ImeUtil.hideImeThen(this);

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}

		if (enterAnim > 0 && exitAnim > 0) {
			overridePendingTransition(enterAnim, exitAnim);
		}

		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean moveTaskToBack(boolean nonRoot) {
		return super.moveTaskToBack(nonRoot);
	}

	/**
	 * 返回指定 DialogFragment
	 * @see com.pc.app.base.PcBaseActivity#getDialogFragment(java.lang.String)
	 */
	public DialogFragment getDialogFragment(String tag) {
		return mPcActivityLib.getDialogFragment(tag);
	}

	/**
	 * set DialogFragment
	 * 
	 * @see com.pc.app.base.PcBaseActivity#setDialogFragment(android.support.v4.app.DialogFragment, java.lang.String, boolean)
	 */
	public void setDialogFragment(final DialogFragment dialog, final String dialogTag, final boolean show) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (null == mPcActivityLib) return;

				mPcActivityLib.setDialogFragment(dialog, dialogTag, show);
			}
		});
	}

	/**
	 * 是否为当前Dialog
	 *
	 * @param tag
	 * @return
	 */
	public boolean isCurrDialog(String tag) {
		return mPcActivityLib.isCurrDialog(tag);
	}

	/**
	 * Dialog is showing
	 *
	 * @param tag
	 * @return
	 */
	public boolean isShowingDialog(String tag) {
		return mPcActivityLib.isShowingDialog(tag);
	}

	/**
	 * 当前Dialog是否显示 
	 *
	 * @return
	 */
	public boolean isShowingCurrDialog() {
		return mPcActivityLib.isShowingCurrDialog();
	}

	/**
	 * 是否是当前Dialog，且是否显示
	 *
	 * @param tag
	 * @return
	 */
	public boolean isShowingCurrDialog(String tag) {
		return mPcActivityLib.isShowingCurrDialog(tag);
	}

	/**
	 * 关闭DialogFragment
	 * 
	 * @see com.pc.app.base.PcBaseActivity#closeDialogFragment(java.lang.String)
	 */
	public void closeDialogFragment(final String tag) {
		if (null == mPcActivityLib) return;

		mPcActivityLib.closeDialogFragment(tag);
	}

	/**
	 * 关闭当前DialogFragment
	 * 
	 * @see com.pc.app.base.PcBaseActivity#closeCurrDialogFragment()
	 */
	@Override
	public void closeCurrDialogFragment() {
		if (null == mPcActivityLib) return;

		closeDialogFragment(mPcActivityLib.getCurrDialogTag());
	}

	/**
	 * 关闭所有Dialog
	 */
	@Override
	public void closeAllDialogFragment() {
		if (null == mPcActivityLib) return;

		mPcActivityLib.closeAllDialogFragment();
	}

	/**
	 * 隐藏所有DialogFragment
	 * 
	 * @see com.pc.app.base.PcBaseActivity#dismissAllDialogFragment()
	 */
	@Override
	public void dismissAllDialogFragment() {
		if (null == mPcActivityLib) return;

		mPcActivityLib.dismissAllDialogFragment();
	}

	/**
	 * 关闭当前DialogFragment
	 * 
	 * @see com.pc.app.base.PcBaseActivity#dismissCurrDialogFragment()
	 */
	@Override
	public void dismissCurrDialogFragment() {
		if (null == mPcActivityLib) return;

		dismissDialogFragment(mPcActivityLib.getCurrDialogTag());
	}

	/**
	 * 关闭指定DialogFragment
	 * 
	 * @see com.pc.app.base.PcBaseActivity#dismissDialogFragment(java.lang.String)
	 */
	public void dismissDialogFragment(final String tag) {
		if (null == mPcActivityLib) return;

		mPcActivityLib.dismissDialogFragment(tag);
	}

	@Override
	public void openActivity(Class<?> pClass) {
		ActivityUtils.openActivity(this, pClass);
	}

	@Override
	public void openActivity(Class<?> pClass, int requestCode) {
		ActivityUtils.openActivity(this, pClass, requestCode);
	}

	@Override
	public void openActivity(String pAction, int requestCode) {
		ActivityUtils.openActivity(this, pAction, requestCode);
	}

	@Override
	public void openActivity(Class<?> pClass, Bundle pBundle) {
		ActivityUtils.openActivity(this, pClass, pBundle);
	}

	@Override
	public void openActivity(String pAction, Bundle pBundle, int requestCode) {
		ActivityUtils.openActivity(this, pAction, pBundle, requestCode);
	}

	@Override
	public void openActivity(Class<?> pClass, int requestCode, int enterAnim, int exitAnim) {
		ActivityUtils.openActivity(this, pClass, requestCode, enterAnim, exitAnim);
	}

	@Override
	public void openActivity(Intent intent, int requestCode, int enterAnim, int exitAnim) {
		ActivityUtils.openActivity(this, intent, requestCode, enterAnim, exitAnim);
	}

	@Override
	public void openActivity(Class<?> pClass, Bundle pBundle, int requestCode, int enterAnim, int exitAnim) {
		ActivityUtils.openActivity(this, pClass, pBundle, requestCode, enterAnim, exitAnim);
	}

	@Override
	public void openActivity(Class<?> pClass, int enterAnim, int exitAnim) {
		ActivityUtils.openActivity(this, pClass, enterAnim, exitAnim);
	}

	@Override
	public void show(String pMsg, int duration) {
		PcToastUtil.Instance().showCustomToast(pMsg, duration);
	}

	@Override
	public void showShortToast(int pResId) {
		PcToastUtil.Instance().showCustomShortToast(pResId);
	}

	@Override
	public void showLongToast(int pResId) {
		PcToastUtil.Instance().showCustomLongToast(pResId);
	}

	@Override
	public void showLongToast(String pMsg) {
		PcToastUtil.Instance().showCustomLongToast(pMsg);
	}

	@Override
	public void showShortToast(String pMsg) {
		PcToastUtil.Instance().showCustomShortToast(pMsg);
	}

	/** 
	 * @return the mPcActivityLib
	 */
	public PcActivityLib getPcActivityLib() {
		return mPcActivityLib;
	}

	/**
	 * @see com.pc.app.base.PcIActivity#measureView(android.view.View)
	 */
	@Override
	public void measureView(View view) {
	}

}
