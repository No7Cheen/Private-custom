/**
 * @(#)PinnedSectionedListAdapter.java 2014-3-6 Copyright 2014 it.kedacom.com,
 *                                     Inc. All rights reserved.
 */

package com.pc.ui.listview.x.libraries;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author chenjian
 * @date 2014-3-6
 */

public abstract class PinnedSectionedListAdapter extends BaseAdapter implements IPinnedSectionedAdapter {

	/**
	 * Holds the calculated values of @{link getPositionInSectionForPosition}
	 */
	private SparseArray<Integer> mSectionPositionCache;
	/**
	 * Holds the calculated values of @{link getSectionForPosition}
	 */
	private SparseArray<Integer> mSectionCache;

	/**
	 * Caches the item count
	 */
	private int mCount;

	public PinnedSectionedListAdapter() {
		super();

		mCount = -1;

		mSectionCache = new SparseArray<Integer>();
		mSectionPositionCache = new SparseArray<Integer>();
	}

	@Override
	public void notifyDataSetChanged() {
		mCount = -1;

		mSectionCache.clear();
		mSectionPositionCache.clear();

		super.notifyDataSetChanged();
	}

	@Override
	public void notifyDataSetInvalidated() {
		mCount = -1;

		mSectionCache.clear();
		mSectionPositionCache.clear();

		super.notifyDataSetInvalidated();
	}

	@Override
	public final int getCount() {
		if (mCount >= 0) {
			return mCount;
		}

		int sectionCount = getSectionCount();
		if (sectionCount <= 0) return 0;

		int count = 0;
		for (int i = 0; i < sectionCount; i++) {
			count += getCountForSection(i);
		}

		// for the header view
		count += sectionCount;

		mCount = count;

		return count;
	}

	/**
	 * @see com.pc.ui.listview.x.libraries.IPinnedSectionedAdapter#count()
	 */
	@Override
	public final int count() {
		return getCount();
	}

	@Override
	public final Object getItem(int position) {
		return getItem(getSectionForPosition(position), getPositionInSectionForPosition(position));
	}

	@Override
	public final long getItemId(int position) {
		return getItemId(getSectionForPosition(position), getPositionInSectionForPosition(position));
	}

	@Override
	public final View getView(int position, View convertView, ViewGroup parent) {
		// 如果是Pinned Header, 返回Section Header View
		if (isSectionHeader(position)) {
			return getSectionHeaderView(getSectionForPosition(position), convertView, parent);
		}

		// List Item View
		return getItemView(getSectionForPosition(position), getPositionInSectionForPosition(position), convertView,
				parent);
	}

	@Override
	public final int getItemViewType(int position) {
		// 如果是Pinned Header, 对应位置的View Type
		if (isSectionHeader(position)) {
			return getSectionHeaderViewType(getSectionForPosition(position));
		}

		// 对应位置的List Item ViewType
		return getItemViewType(getSectionForPosition(position), getPositionInSectionForPosition(position));
	}

	/**
	 * View Type
	 * @see android.widget.BaseAdapter#getViewTypeCount()
	 */
	@Override
	public final int getViewTypeCount() {
		return getItemViewTypeCount() + getSectionHeaderViewTypeCount();
	}

	@Override
	public final int getSectionForPosition(int position) {
		// first try to retrieve values from cache
		Integer cachedSection = mSectionCache.get(position);
		if (cachedSection != null) {
			return cachedSection;
		}

		int sectionStart = 0;
		for (int i = 0; i < getSectionCount(); i++) {
			int sectionCount = getCountForSection(i);
			int sectionEnd = sectionStart + sectionCount + 1;
			if (position >= sectionStart && position < sectionEnd) {
				mSectionCache.put(position, i);

				return i;
			}

			sectionStart = sectionEnd;
		}

		return 0;
	}

	@Override
	public int getPositionInSectionForPosition(int position) {
		// first try to retrieve values from cache
		Integer cachedPosition = mSectionPositionCache.get(position);
		if (cachedPosition != null) {
			return cachedPosition;
		}

		int sectionStart = 0;
		for (int i = 0; i < getSectionCount(); i++) {
			int sectionCount = getCountForSection(i);
			int sectionEnd = sectionStart + sectionCount + 1;
			if (position >= sectionStart && position < sectionEnd) {
				int positionInSection = position - sectionStart - 1;
				mSectionPositionCache.put(position, positionInSection);

				return positionInSection;
			}
			sectionStart = sectionEnd;
		}

		return 0;
	}

	/**
	 * 指定位置是否是Pinned Header
	 */
	@Override
	public final boolean isSectionHeader(int position) {
		int sectionStart = 0;
		for (int i = 0; i < getSectionCount(); i++) {
			if (position == sectionStart) {
				return true;
			} else if (position < sectionStart) {
				return false;
			}
			sectionStart += getCountForSection(i) + 1;
		}
		return false;
	}

}
