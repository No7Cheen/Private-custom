/**
 * XPullToRefreshListViewFooter.java 2013-5-20 Copyright 2013 it.kedacom.com,
 * Inc. All rights reserved.
 */

package com.pc.ui.listview.x.pulllistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.privatecustom.publiclibs.R;

/**
 * @author chenjian
 * @date 2013-5-20
 */

public class XPullToRefreshListViewFooter extends LinearLayout {

	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;

	private Context mContext;

	private View mContentView;
	private View mProgressBar; // 加载
	private TextView mHintView; // 提示

	public XPullToRefreshListViewFooter(Context context) {
		super(context);
		initView(context);
	}

	public XPullToRefreshListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	/**
	 * initialize footer view
	 * @param context
	 */
	private void initView(Context context) {
		mContext = context;
		LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.x_pulllistview_footer,
				null);
		addView(moreView);
		moreView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		mContentView = moreView.findViewById(R.id.xlistview_footer_content);
		mProgressBar = moreView.findViewById(R.id.xlistview_footer_progressbar);
		mHintView = (TextView) moreView.findViewById(R.id.xlistview_footer_hint_textview);
	}

	/**
	 * set State
	 * @param state
	 */
	public void setState(int state) {
		mHintView.setVisibility(View.INVISIBLE);
		mProgressBar.setVisibility(View.INVISIBLE);
		mHintView.setVisibility(View.INVISIBLE);
		if (state == STATE_READY) { // 准备状态
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText(R.string.listview_footer_hint_ready);
		} else if (state == STATE_LOADING) { // 正在加载状态
			mProgressBar.setVisibility(View.VISIBLE);
		} else { // 默认状态
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText(R.string.footer_hint_normal);
		}
	}

	/**
	 * @param height
	 */
	public void setBottomMargin(int height) {
		if (height < 0) {
			return;
		}

		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
		lp.bottomMargin = height;
		mContentView.setLayoutParams(lp);
	}

	/**
	 * @return
	 */
	public int getBottomMargin() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
		return lp.bottomMargin;
	}

	/**
	 * normal status
	 */
	public void normal() {
		mHintView.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
	}

	/**
	 * loading status
	 */
	public void loading() {
		mHintView.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);
	}

	/**
	 * hide footer when disable pull load more
	 */
	public void hide() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
		lp.height = 0;
		mContentView.setLayoutParams(lp);
	}

	/**
	 * show footer
	 */
	public void show() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);
	}

}
