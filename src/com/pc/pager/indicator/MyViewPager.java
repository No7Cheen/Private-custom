/**
 * 2013-5-14
 * 
 * <pre>
 * Copyright 2013 it.kedacom.com, Inc. All rights reserved.
 * </pre>
 * 
 * <pre>
 * Fragment的生命周期
 * onAttach-->onCreate-->onCreateView-->onViewCreated-->onActivityCreated-->onStart-->onResume
 * onPause-->onStop-->onDestroyView-->onDestroy-->onDetach
 * </pre>
 */

package com.pc.pager.indicator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.pc.pager.indicator.modle.BaseFragmentPageAdapter;
import com.privatecustom.publiclibs.R;

/**
 * @author chenjian
 * @date 2013-5-14
 */

public class MyViewPager extends Fragment {

	private Activity mActivity;
	private Context mApplicationContext;

	private FrameLayout mPageIndicatorLayout; // ViewPager指示器布局
	private ImageView mLeftIndicatorImg; // 指示器左边箭头
	private ImageView mRightIndicatorImg; // 指示器右边箭头
	private TitlePageIndicator mPageIndicator; // 指示器
	private ViewPager mViewPager;
	private BaseFragmentPageAdapter mPageAdapter; // ViewPager Adapter

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		mActivity = activity;
		mApplicationContext = activity.getApplicationContext();
	}

	/**
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 *      android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.main_content_layout_viewpager, container, false);

		mPageIndicatorLayout = (FrameLayout) v.findViewById(R.id.pageIndicator_layout);
		mLeftIndicatorImg = (ImageView) v.findViewById(R.id.imageview_leftIndicator);
		mRightIndicatorImg = (ImageView) v.findViewById(R.id.imageview_rightIndicator);
		mPageIndicator = (TitlePageIndicator) v.findViewById(R.id.page_indicator);
		mViewPager = (ViewPager) v.findViewById(R.id.view_pager);

		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mPageIndicatorLayout.setVisibility(View.GONE);
		mLeftIndicatorImg.setVisibility(View.GONE);
		mRightIndicatorImg.setVisibility(View.VISIBLE);
		mViewPager.setVisibility(View.GONE);
		getView().findViewById(R.id.view_load_failed).setVisibility(View.GONE);
		getView().findViewById(R.id.view_loading).setVisibility(View.VISIBLE);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	/**
	 * viewPager切换页面
	 * @author chenjian
	 * @date 2013-5-15
	 */
	class MyPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			if (arg0 == 0) {
				mLeftIndicatorImg.setVisibility(8);
			} else if (arg0 == mPageAdapter.getFragmentSize() - 1) {
				mRightIndicatorImg.setVisibility(8);
			} else {
				mRightIndicatorImg.setVisibility(0);
				mLeftIndicatorImg.setVisibility(0);
			}
		}
	}

	/**
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {

		super.onPause();
	}

	@Override
	public void onStop() {

		super.onStop();
	}

	@Override
	public void onDestroyView() {

		super.onDestroyView();
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}

	@Override
	public void onDetach() {

		super.onDetach();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
	}

}
