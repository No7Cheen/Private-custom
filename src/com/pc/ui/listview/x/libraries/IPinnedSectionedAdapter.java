/**
 * @(#)IPinnedSectionedAdapter.java 2014-3-6 Copyright 2014 it.kedacom.com, Inc.
 *                                  All rights reserved.
 */

package com.pc.ui.listview.x.libraries;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author chenjian
 * @date 2014-3-6
 */

public interface IPinnedSectionedAdapter {

	public abstract int count();

	public abstract int getSectionForPosition(int position);

	public abstract int getPositionInSectionForPosition(int position);

	public abstract boolean isSectionHeader(int position);

	/**
	 * ListView Item View Type
	 * @param section
	 * @param position
	 * @return
	 */
	public abstract int getItemViewType(int section, int position);

	public abstract int getItemViewTypeCount();

	/**
	 * Pinned View Type
	 */
	public abstract int getSectionHeaderViewType(int section);

	public abstract int getSectionHeaderViewTypeCount();

	public abstract Object getItem(int section, int position);

	public abstract long getItemId(int section, int position);

	public abstract Object getSectionItem(int section);

	/**
	 * Pinned Header的Size
	 * @return
	 */
	public abstract int getSectionCount();

	/**
	 * 对应Pinned Header的Size
	 * @param section
	 * @return
	 */
	public abstract int getCountForSection(int section);

	public abstract View getItemView(int section, int position, View convertView, ViewGroup parent);

	public abstract View getSectionHeaderView(int section, View convertView, ViewGroup parent);
}
