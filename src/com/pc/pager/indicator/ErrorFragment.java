package com.pc.pager.indicator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.privatecustom.publiclibs.R;

@SuppressLint("ValidFragment")
public class ErrorFragment extends Fragment {

	private int mErrorResId;
	private final int DEF_ERRORRESID = R.layout.error_fragment;

	public ErrorFragment(int errorResId) {
		mErrorResId = errorResId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(mErrorResId, null);
		if (null == view) {
			inflater.inflate(DEF_ERRORRESID, null);
		}

		return view;
	}

	/**
	 * set error info
	 * @param errorInfo
	 */
	public void setErrorInfo(String errorInfo) {
		if (null == errorInfo || errorInfo.length() == 0) {
			return;
		}

		((TextView) getView().findViewById(R.id.error_info)).setText(errorInfo);
	}
}
