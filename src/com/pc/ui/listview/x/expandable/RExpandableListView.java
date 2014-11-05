package com.pc.ui.listview.x.expandable;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListAdapter;

/**
 * Pull To Refresh ExpandableListView
 * 
 * @author chenjian
 * @date 2013-10-12
 */
public class RExpandableListView extends AbsRExpandableListView
{

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public RExpandableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public RExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param context
	 */
	public RExpandableListView(Context context) {
		super(context);
	}

	@Override
	public void setAdapter(ExpandableListAdapter adapter) {
		if (null == adapter) {
			throw new IllegalArgumentException("adapter is null");
		}

		super.setAdapter(adapter);
	}

}
