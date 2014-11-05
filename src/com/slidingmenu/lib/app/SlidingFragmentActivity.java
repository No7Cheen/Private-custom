package com.slidingmenu.lib.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.slidingmenu.lib.SlidingMenu;

public class SlidingFragmentActivity extends FragmentActivity implements SlidingActivityBase {

	private final String Tag = "SlidingFragmentActivity";

	private SlidingActivityHelper mHelper;

	/*
	 * (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			mHelper = new SlidingActivityHelper(this);
			if (null != mHelper) {
				mHelper.onCreate(savedInstanceState);
			}
		} catch (Exception e) {
			Log.e(Tag, "onCreate ", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onPostCreate(android.os.Bundle)
	 */
	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mHelper.onPostCreate(savedInstanceState);
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#findViewById(int)
	 */
	@Override
	public View findViewById(int id) {
		View v = super.findViewById(id);
		if (v != null) return v;
		if (mHelper == null) {
			return v;
		}

		return mHelper.findViewById(id);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os
	 * .Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mHelper.onSaveInstanceState(outState);
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#setContentView(int)
	 */
	@Override
	public void setContentView(int id) {
		setContentView(getLayoutInflater().inflate(id, null));
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#setContentView(android.view.View)
	 */
	@Override
	public void setContentView(View v) {
		setContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#setContentView(android.view.View,
	 * android.view.ViewGroup.LayoutParams)
	 */
	@Override
	public void setContentView(View v, LayoutParams params) {
		try {
			super.setContentView(v, params);
			if (null != mHelper) {
				mHelper.registerAboveContentView(v, params);
			}
		} catch (Exception e) {
			Log.e(Tag, "setContentView ", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#
	 * setBehindContentView(int)
	 */
	public void setBehindContentView(int id) {
		setBehindContentView(getLayoutInflater().inflate(id, null));
	}

	/*
	 * (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#
	 * setBehindContentView(android.view.View)
	 */
	public void setBehindContentView(View v) {
		setBehindContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	/*
	 * (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#
	 * setBehindContentView(android.view.View,
	 * android.view.ViewGroup.LayoutParams)
	 */
	public void setBehindContentView(View v, LayoutParams params) {
		try {
			if (null != mHelper) {
				mHelper.setBehindContentView(v, params);
			}
		} catch (Exception e) {
			Log.e(Tag, "setBehindContentView", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#getSlidingMenu
	 * ()
	 */
	public SlidingMenu getSlidingMenu() {
		try {
			if (null != mHelper) {
				return mHelper.getSlidingMenu();
			}
		} catch (Exception e) {
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#toggle()
	 */
	public void toggle() {
		if (null != mHelper) mHelper.toggle();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#showAbove()
	 */
	public void showContent() {
		if (null != mHelper) mHelper.showContent();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#showBehind()
	 */
	public void showMenu() {
		if (null != mHelper) mHelper.showMenu();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#showSecondaryMenu
	 * ()
	 */
	public void showSecondaryMenu() {
		if (null != mHelper) mHelper.showSecondaryMenu();
	}

	/*
	 * (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#
	 * setSlidingActionBarEnabled(boolean)
	 */
	public void setSlidingActionBarEnabled(boolean b) {
		if (null != mHelper) mHelper.setSlidingActionBarEnabled(b);
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onKeyUp(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (null == mHelper) {
			return super.onKeyUp(keyCode, event);
		}

		boolean b = mHelper.onKeyUp(keyCode, event);
		if (b) return b;
		return super.onKeyUp(keyCode, event);
	}

	public void setShowContent4BackKey(boolean isShowContent4BackKey) {
		if (null != mHelper) {
			mHelper.setShowContent4BackKey(isShowContent4BackKey);
		}
	}

	public void setCustomOnKeyup(IOnKeyup customOnKeyup) {
		if (null != mHelper) {
			mHelper.setCustomOnKeyup(customOnKeyup);
		}
	}

}
