/**
 * @(#)PinnedSectionedExpandableAdapter.java 2013-10-23 Copyright 2013
 *                                           it.kedacom.com, Inc. All rights
 *                                           reserved.
 */

package com.pc.ui.listview.x.libraries;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

/**
 * @author chenjian
 * @date 2013-10-23
 */

public abstract class PinnedSectionedExpandableAdapter extends BaseExpandableListAdapter implements
		ExpandableListAdapter
{

	public abstract View getSectionHeaderView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent);

	@Override
	public abstract Object getGroup(int groupPosition);

	@Override
	public abstract Object getChild(int groupPosition, int childPosition);

	/**
	 * 这里的Sectiong,即Group Position
	 * 
	 * @param exListView
	 * @param position
	 * @return
	 */
	public int getSectionForPosition(ExpandableListView exListView, int position) {
		if (null == exListView) {
			return -1;
		}

		// 当前position归属的 Group Position
		return ExpandableListView.getPackedPositionGroup(exListView.getExpandableListPosition(position));
	}

	public int getCount() {

		int count = 0;
		int gCount = getGroupCount();
		if (gCount == 0) {
			return 0;
		}

		for (int i = 0; i < gCount; i++) {
			count += getChildrenCount(i);
		}

		count += gCount;

		return count;
	}

	@Override
	public void notifyDataSetInvalidated() {
		super.notifyDataSetInvalidated();
	}

	@Override
	public abstract int getGroupCount();

	@Override
	public abstract int getChildrenCount(int groupPosition);

	public abstract int getSectionType(int groupPosition);

	public abstract int getSectionTypeCount();
}
