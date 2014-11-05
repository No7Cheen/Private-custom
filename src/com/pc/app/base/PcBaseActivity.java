/**
 * @(#)PBaseActivity.java 2014-1-16 Copyright 2014 it.kedacom.com, Inc. All
 *                        rights reserved.
 */

package com.pc.app.base;

import android.support.v4.app.DialogFragment;

/**
 * pc BaseActivity interface
 * 
 * @author chenjian
 * @date 2014-1-16
 */

public interface PcBaseActivity extends PcIBaseActivity {

	/**
	 * init Extras
	 */
	public void initExtras();

	/**
	 * find views (eg:for id)
	 */
	public void findViews();

	/**
	 * 初始化控件的值
	 */
	public void initComponentValue();

	/**
	 * 注册监听事件
	 */
	public void registerListeners();

	/**
	 * 注册所有广播
	 */
	public void registerReceivers();

	/**
	 * 注销所有广播
	 */
	public void unregisterReceivers();

	/**
	 * 弹出框Dialog
	 * 
	 * @return
	 */
	public DialogFragment getDialogFragment(String tag);

	/**
	 * 设置弹出框
	 *
	 * @param dialogFragment
	 * @param tag
	 * @param show
	 */
	public void setDialogFragment(final DialogFragment dialogFragment, final String tag, final boolean show);

	/**
	 * 是否为当前Dialog
	 *
	 * @param tag
	 * @return
	 */
	public boolean isCurrDialog(String tag);

	/**
	 * Dialog is showing
	 *
	 * @param tag
	 * @return
	 */
	public boolean isShowingDialog(String tag);

	/**
	 * 当前Dialog是否显示 
	 *
	 * @return
	 */
	public boolean isShowingCurrDialog();

	/**
	 * 是否是当前Dialog，且是否显示
	 *
	 * @param tag
	 * @return
	 */
	public boolean isShowingCurrDialog(String tag);

	/**
	 * cloase Dialog
	 *
	 * @param tag
	 */
	public void closeDialogFragment(String tag);

	/**
	 * cloase当前Dialog
	 */
	public void closeCurrDialogFragment();

	/**
	 * 关闭所有Dialog
	 */
	public void closeAllDialogFragment();

	/**
	 * 解散所有Dialog
	 */
	public void dismissAllDialogFragment();

	/**
	 * 解散当前Dialog
	 */
	public void dismissCurrDialogFragment();

	/**
	 * 解散指定Dialog
	 *
	 * @param tag
	 */
	public void dismissDialogFragment(String tag);

}
