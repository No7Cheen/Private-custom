package com.pc.pager.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 通用的Fragment适配器
 */
public class AbsFragmentPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> mFragmentList;

	public AbsFragmentPagerAdapter(FragmentManager mFragmentManager, ArrayList<Fragment> fragmentList) {
		super(mFragmentManager);
		mFragmentList = fragmentList;
	}

	/**
	 * 获取数量.
	 * @return the count
	 */
	@Override
	public int getCount() {
		if (null == mFragmentList || mFragmentList.isEmpty()) {
			return 0;
		}

		return mFragmentList.size();
	}

	/**
	 * 获取索引位置的Fragment.
	 * @param position the position
	 * @return the item
	 */
	@Override
	public Fragment getItem(int position) {
		if (null == mFragmentList || mFragmentList.isEmpty()) {
			return null;
		}

		Fragment fragment = null;
		if (position < mFragmentList.size()) {
			fragment = mFragmentList.get(position);
		} else {
			fragment = mFragmentList.get(0);
		}
		return fragment;

	}
}
