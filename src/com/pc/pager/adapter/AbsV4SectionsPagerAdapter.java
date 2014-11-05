/**
 * @(#)AbsSectionsPagerAdapter.java 2014-1-17 Copyright 2014 it.kedacom.com,
 *                                  Inc. All rights reserved.
 */

package com.pc.pager.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @author chenjian
 * @date 2014-1-17
 */

public abstract class AbsV4SectionsPagerAdapter extends FragmentPagerAdapter {

	public AbsV4SectionsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	/**
	 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	 */
	@Override
	public abstract Fragment getItem(int arg0);

	/**
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public abstract int getCount();

}
