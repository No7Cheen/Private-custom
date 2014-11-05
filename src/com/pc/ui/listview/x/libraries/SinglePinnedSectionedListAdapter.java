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

public abstract class SinglePinnedSectionedListAdapter extends BaseAdapter implements IPinnedSectionedAdapter {

	// Pinned Header View Type
	private final int HEADER_VIEW_TYPE = 0;

	// List Item View Type
	private final int ITEM_VIEW_TYPE = 1;

	/**
	 * Holds the calculated values of @{link getPositionInSectionForPosition}
	 */
	private SparseArray<Integer> mSectionPositionCache;
	/**
	 * Holds the calculated values of @{link getSectionForPosition}
	 */
	private SparseArray<Integer> mSectionCache;
	/**
	 * Holds the calculated values of @{link getCountForSection}
	 */
	private SparseArray<Integer> mSectionCountCache;

	/**
	 * Caches the item count
	 */
	private int mCount;
	/**
	 * Caches the section count
	 */
	private int mSectionCount;

	public SinglePinnedSectionedListAdapter() {
		super();

		mCount = -1;
		mSectionCount = -1;

		mSectionCache = new SparseArray<Integer>();
		mSectionCountCache = new SparseArray<Integer>();
		mSectionPositionCache = new SparseArray<Integer>();
	}

	@Override
	public void notifyDataSetChanged() {
		mCount = -1;
		mSectionCount = -1;

		mSectionCache.clear();
		mSectionCountCache.clear();
		mSectionPositionCache.clear();

		super.notifyDataSetChanged();
	}

	@Override
	public void notifyDataSetInvalidated() {
		mCount = -1;
		mSectionCount = -1;

		mSectionCache.clear();
		mSectionCountCache.clear();
		mSectionPositionCache.clear();

		super.notifyDataSetInvalidated();
	}

	@Override
	public final int getCount() {
		if (mCount >= 0) {
			return mCount;
		}

		int count = 0;
		for (int i = 0; i < internalGetSectionCount(); i++) {
			count += internalGetCountForSection(i);
			count++; // for the header view
		}
		mCount = count;

		return count;
	}

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

	public final int getSectionForPosition(int position) {
		// first try to retrieve values from cache
		Integer cachedSection = mSectionCache.get(position);
		if (cachedSection != null) {
			return cachedSection;
		}

		int sectionStart = 0;
		for (int i = 0; i < internalGetSectionCount(); i++) {
			int sectionCount = internalGetCountForSection(i);
			int sectionEnd = sectionStart + sectionCount + 1;
			if (position >= sectionStart && position < sectionEnd) {
				mSectionCache.put(position, i);

				return i;
			}
			sectionStart = sectionEnd;
		}

		return 0;
	}

	public int getPositionInSectionForPosition(int position) {
		// first try to retrieve values from cache
		Integer cachedPosition = mSectionPositionCache.get(position);
		if (cachedPosition != null) {
			return cachedPosition;
		}

		int sectionStart = 0;
		for (int i = 0; i < internalGetSectionCount(); i++) {
			int sectionCount = internalGetCountForSection(i);
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
	public final boolean isSectionHeader(int position) {
		int sectionStart = 0;
		for (int i = 0; i < internalGetSectionCount(); i++) {
			if (position == sectionStart) {
				return true;
			} else if (position < sectionStart) {
				return false;
			}
			sectionStart += internalGetCountForSection(i) + 1;
		}
		return false;
	}

	/**
	 * ListView Item View Type
	 * @param section
	 * @param position
	 * @return
	 */
	public int getItemViewType(int section, int position) {
		return ITEM_VIEW_TYPE;
	}

	public int getItemViewTypeCount() {
		return 1;
	}

	/**
	 * Pinned View Type
	 */
	public int getSectionHeaderViewType(int section) {
		return HEADER_VIEW_TYPE;
	}

	public int getSectionHeaderViewTypeCount() {
		return 1;
	}

	private int internalGetCountForSection(int section) {
		Integer cachedSectionCount = mSectionCountCache.get(section);
		if (cachedSectionCount != null) {
			return cachedSectionCount;
		}

		int sectionCount = getCountForSection(section);
		mSectionCountCache.put(section, sectionCount);

		return sectionCount;
	}

	private int internalGetSectionCount() {
		if (mSectionCount >= 0) {
			return mSectionCount;
		}

		mSectionCount = getSectionCount();

		return mSectionCount;
	}

}
