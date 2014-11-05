/**
 * @(#)SlideListItemListener.java 2014-1-20 Copyright 2014 it.kedacom.com, Inc.
 *                                All rights reserved.
 */

package com.pc.ui.listview.x.slideitem;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.ViewConfiguration;
import android.widget.ListView;

import com.pc.ui.listview.x.slideitem.libs.ISlideListItemListener;
import com.pc.ui.listview.x.slideitem.libs.SlideListItemListener;
import com.privatecustom.publiclibs.R;

/*
 * @author chenjian
 * @date 2014-1-21
 */
public class SlideListView extends ListView {

	private int showviewId;
	private int hideviewId;
	private boolean isSlideItemView;

	public SlideListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SlideListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlideListViewItem, defStyle, 0);
		isSlideItemView = a.getBoolean(R.styleable.SlideListViewItem_slide, false);
		showviewId = a.getResourceId(R.styleable.SlideListViewItem_showview, 0);
		hideviewId = a.getResourceId(R.styleable.SlideListViewItem_hideview, 0);
		if (showviewId == 0 || hideviewId == 0) {
			isSlideItemView = false;
		}

		a.recycle();
	}

	/** @return the showviewId */
	public int getShowviewId() {
		return showviewId;
	}

	/** @return the hideviewId */
	public int getHideviewId() {
		return hideviewId;
	}

	/** @return the isSlideItemView */
	public boolean isSlideItemView() {
		return isSlideItemView;
	}

	public void setSlideListItemListener(ISlideListItemListener slideListItemListener) {
		if (showviewId == 0 || hideviewId == 0) {
			isSlideItemView = false;
		}

		if (!isSlideItemView) {
			return;
		}

		final ViewConfiguration configuration = ViewConfiguration.get(getContext());
		final int touchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
		SlideListItemListener swipListItemListener = new SlideListItemListener(this, touchSlop, showviewId, hideviewId);
		setOnTouchListener(swipListItemListener);
		swipListItemListener.setISlideListItemListener(slideListItemListener);
	}

}
