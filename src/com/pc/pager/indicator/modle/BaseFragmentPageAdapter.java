package com.pc.pager.indicator.modle;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.pc.pager.indicator.BasePagerListFragment;
import com.pc.pager.indicator.ErrorFragment;
import com.privatecustom.publiclibs.R;

public class BaseFragmentPageAdapter extends FragmentStatePagerAdapter {

	private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
	private ArrayList<String> mPageTitles = new ArrayList<String>();

	private Activity mActivity;

	public BaseFragmentPageAdapter(FragmentActivity activity) {
		super(activity.getSupportFragmentManager());
		mActivity = activity;
	}

	/**
	 * listview
	 * @param listObject
	 */
	public void addFragment(String viewPagerKey) {
		List<String> titles = MyViewPagerFactory.getPagerTitles(mActivity, viewPagerKey);
		List<BasePagerListFragment> listFragment = MyViewPagerFactory.getPagers(viewPagerKey);
		if (null == listFragment || listFragment.isEmpty()) {
			return;
		}

		if (null != titles && !titles.isEmpty()) {
			mPageTitles.addAll(titles);
		}

		for (BasePagerListFragment fragment : listFragment) {
			if (fragment == null) {
				continue;
			}

			addTabFragment(fragment);
		}
	}

	/**
	 * listview
	 * @param listObject
	 */
	public void addSingleFragment(String viewPagerKey) {
		List<BasePagerListFragment> listFragment = MyViewPagerFactory.getPagers(viewPagerKey);
		if (null == listFragment || listFragment.isEmpty()) {
			return;
		}

		for (BasePagerListFragment fragment : listFragment) {
			if (fragment == null) {
				continue;
			}

			addTabFragment(fragment);

			break;
		}
	}

	/**
	 * pagerFragment与title一一对应
	 * @param titles
	 * @param pagerFragments
	 */
	public void addFragment(List<String> titles, List<BasePagerListFragment> pagerFragments) {
		if (null != titles && !titles.isEmpty()) {
			mPageTitles.addAll(titles);
		}

		for (BasePagerListFragment fragment : pagerFragments) {
			addTabFragment(fragment);
		}
	}

	/**
	 * listview
	 * @param listObject
	 */
	public void addSingleFragment(BasePagerListFragment pagerFragemnt) {
		if (null == pagerFragemnt) {
			return;
		}

		addTabFragment(pagerFragemnt);
	}

	/**
	 * 添加一个Null Fragment
	 */
	public void addNullFragment() {
		addNullFragment(null);
	}

	/**
	 * 添加一个Null Fragment
	 * @param info
	 */
	public void addNullFragment(String info) {
		ErrorFragment errorFragment = new ErrorFragment(R.layout.error_fragment);
		errorFragment.setErrorInfo(info);
		addTabFragment(errorFragment);
	}

	public void ClearFragments() {
		mFragments.clear();
	}

	/**
	 * 添加pager
	 * @param fragment
	 */
	public void addTabFragment(Fragment fragment) {
		mFragments.add(fragment);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mPageTitles.get(position);
	}

	@Override
	public Fragment getItem(int arg0) {
		return mFragments.get(arg0);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		super.destroyItem(container, position, object);
	}

	public ArrayList<Fragment> getFragments() {
		return mFragments;
	}

	public int getFragmentSize() {
		if (null == mFragments || mFragments.isEmpty()) {
			return 0;
		}

		return mFragments.size();
	}

}
