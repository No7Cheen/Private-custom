package com.pc.view.listener;

import android.support.v4.view.ViewPager;

public interface AbOnPageChangeListener extends ViewPager.OnPageChangeListener {

	/**
	 * Sets the view pager.
	 * @param view the new view pager
	 */
	public void setViewPager(ViewPager view);

	/**
	 * Sets the view pager.
	 * @param view the view
	 * @param initialPosition the initial position
	 */
	public void setViewPager(ViewPager view, int initialPosition);

	/**
	 * Sets the current item.
	 * @param item the new current item
	 */
	public void setCurrentItem(int item);

	/**
	 * Sets the on page change listener.
	 * @param listener the new on page change listener
	 */
	public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener);

	/**
	 * Notify data set changed.
	 */
	public void notifyDataSetChanged();
}
