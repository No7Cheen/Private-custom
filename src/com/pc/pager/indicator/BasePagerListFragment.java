/**
 * @(#)BasePagerListFragment.java 2013-5-15 Copyright 2013 it.kedacom.com, Inc.
 *                                All rights reserved.
 */

package com.pc.pager.indicator;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pc.ui.listview.x.pulllistview.XPullToRefreshListView;
import com.pc.ui.listview.x.pulllistview.XPullToRefreshListView.IXPullToRefreshListViewListener;
import com.privatecustom.publiclibs.R;

/**
 * @author chenjian
 * @date 2013-5-15
 */

public abstract class BasePagerListFragment extends Fragment implements IXPullToRefreshListViewListener {

	private final String DEBUG_TAG = "BasePagerListFragment";
	public String Tag = "BasePagerListFragment";

	protected XPullToRefreshListView mPull2RefreshListView;
	protected View view;
	protected LayoutInflater mInflater;
	protected boolean mIsScroll = false;
	protected boolean mIsAutoLoadMore = false; // 自动加载更多

	protected Activity mActivity;

	/**
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		mActivity = activity;

		Tag = this.getClass().getSimpleName();
		com.pc.utils.log.PcLog.i(DEBUG_TAG, Tag + " onAttach");
	}

	/**
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		com.pc.utils.log.PcLog.i(DEBUG_TAG, Tag + " onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		com.pc.utils.log.PcLog.i(DEBUG_TAG, Tag + " onCreateView");

		mInflater = inflater;
		view = inflater.inflate(R.layout.x_pulllistview_main, null);
		mPull2RefreshListView = (XPullToRefreshListView) view.findViewById(R.id.list_view);

		return view;
	}

	/**
	 * @see android.support.v4.app.Fragment#onViewCreated(android.view.View,
	 *      android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// mPull2RefreshListView.stopRefresh();
		// mPull2RefreshListView.stopLoadMore();
		// setPullLoadEnable(true);
		// setPullRefreshEnable(true);

		initPull2RefreshListViwe();

		com.pc.utils.log.PcLog.i(DEBUG_TAG, Tag + " onViewCreated");
	}

	/**
	 * 初始化Pull-refresh list view
	 * 
	 * <pre>
	 * 此方法已在上层(BasePagerListFragment的onViewCreated())方法中被调用
	 * </pre>
	 */
	protected abstract void initPull2RefreshListViwe();

	/**
	 * 创建ListView时第一次,一般指需要从服务器(或缓存)显示View *
	 * 
	 * <pre>
	 * 此方法已在上层(BasePagerListFragment的onResume())方法中被调用
	 * </pre>
	 */
	protected abstract void firstRefreshListView();

	/**
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		com.pc.utils.log.PcLog.i(DEBUG_TAG, Tag + " onActivityCreated");
	}

	/**
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();

		com.pc.utils.log.PcLog.i(DEBUG_TAG, Tag + " onStart");
	}

	/**
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();

		firstRefreshListView();

		com.pc.utils.log.PcLog.i(DEBUG_TAG, Tag + " onResume");
	}

	/**
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();

		com.pc.utils.log.PcLog.v(DEBUG_TAG, Tag + " onPause...");
	}

	/**
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		super.onStop();

		com.pc.utils.log.PcLog.v(DEBUG_TAG, Tag + " onStop...");
	}

	/**
	 * @see android.support.v4.app.Fragment#onDestroyView()
	 */
	@Override
	public void onDestroyView() {
		super.onDestroyView();

		com.pc.utils.log.PcLog.v(DEBUG_TAG, Tag + " onDestroyView...");
	}

	/**
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();

		com.pc.utils.log.PcLog.v(DEBUG_TAG, Tag + " onDestroy");
	}

	/**
	 * @see android.support.v4.app.Fragment#onDetach()
	 */
	@Override
	public void onDetach() {
		super.onDetach();

		com.pc.utils.log.PcLog.v(DEBUG_TAG, Tag + " onDetach");
	}

	/**
	 * 下拉刷新是否可用
	 * @param enable
	 */
	protected void setPullLoadEnable(boolean enable) {
		mPull2RefreshListView.setPullLoadEnable(enable);
	}

	/**
	 * 上拉加载更多是否可用
	 * @param enable
	 */
	protected void setPullRefreshEnable(boolean enable) {
		mPull2RefreshListView.setPullRefreshEnable(enable);
	}

	/**
	 * notifyDataSetChanged for Adapter
	 */
	protected abstract void notifyDataSetChanged4Adapter();

}
