/**
 * @(#)MyViewPagerFactory.java 2013-5-14 Copyright 2013 it.kedacom.com, Inc. All
 *                             rights reserved.
 */
package com.pc.pager.indicator.modle;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.pc.pager.indicator.BasePagerListFragment;
import com.privatecustom.publiclibs.R;

/**
 * @author chenjian
 * @date 2013-5-14
 */

public class MyViewPagerFactory {

	public final static String VIEWPAGER_TEST = "MyViewPager_Test";

	private final static HashMap<String, List<BasePagerListFragment>> mMyIdToPagersMap = new HashMap<String, List<BasePagerListFragment>>();

	/**
	 * 注册ViewPager
	 * @param key
	 * @param fragments
	 */
	public final static void regViewPager(String key, List<BasePagerListFragment> basePagerListFragment) {
		mMyIdToPagersMap.put(key, basePagerListFragment);
	}

	/**
	 * get ViewPager对应的Pager
	 * @param key
	 * @return
	 */
	public final static List<BasePagerListFragment> getPagers(String key) {
		return mMyIdToPagersMap.get(key);
	}

	/**
	 * get viewpager对于的Title
	 * @param key
	 * @return
	 */
	public final static List<String> getPagerTitles(Context context, String key) {
		List<String> titles = new ArrayList<String>();

		int resId = getArrayResourceId(key);
		if (resId == -1) {
			return null;
		}

		try {
			String values[] = context.getResources().getStringArray(resId);// 资源字符
			if (null == values || values.length == 0) {
				return null;
			}

			titles = Arrays.asList(values);
		} catch (Exception e) {
			return null;
		}

		return titles;
	}

	public final static int getArrayResourceId(String key) {
		int id = -1;
		if (null == null || key.length() == 0) {
			return id;
		}

		try {
			Field f = R.array.class.getField(key);
			if (f == null) return id;

			return f.getInt(null);
		} catch (Exception e) {
		}

		return id;
	}

}
