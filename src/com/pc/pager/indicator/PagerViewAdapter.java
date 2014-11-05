package com.pc.pager.indicator;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * ViewPager适配器 实现各页卡的装入和卸载
 * @author chenjian
 */
public class PagerViewAdapter extends PagerAdapter {

	public List<View> mListViews;

	public PagerViewAdapter(List<View> mListViews) {
		this.mListViews = mListViews;
	}

	@Override
	public void destroyItem(View view, int arg1, Object object) {
		((ViewPager) view).removeView(mListViews.get(arg1));
	}

	@Override
	public void finishUpdate(View view) {
	}

	@Override
	public int getCount() {
		if (mListViews == null || mListViews.isEmpty()) {
			return 0;
		}

		return mListViews.size();
	}

	@Override
	public Object instantiateItem(View collection, int position) {
		((ViewPager) collection).addView(mListViews.get(position), 0);

		return mListViews.get(position);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((View) object);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View view) {
	}

}
