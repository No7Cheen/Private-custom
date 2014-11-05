/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes. Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *******************************************************************************/
package com.pc.ui.bouncescrollview.libraries.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.pc.ui.bouncescrollview.libraries.BounceScrollViewBase;
import com.privatecustom.publiclibs.R;

public class BounceScrollWebView extends BounceScrollViewBase<WebView> {

	private final WebChromeClient defaultWebChromeClient = new WebChromeClient() {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
			}
		}

	};

	public BounceScrollWebView(Context context) {
		super(context);

		mBounceScrollableView.setWebChromeClient(defaultWebChromeClient);
	}

	public BounceScrollWebView(Context context, AttributeSet attrs) {
		super(context, attrs);

		mBounceScrollableView.setWebChromeClient(defaultWebChromeClient);
	}

	@Override
	protected WebView createBounceScrollableView(Context context, AttributeSet attrs) {
		WebView webView = new WebView(context, attrs);

		webView.setId(R.id.webview);

		return webView;
	}

	@Override
	protected boolean isReadyForPullTop() {
		return mBounceScrollableView.getScrollY() == 0;
	}

	@Override
	protected boolean isReadyForPullBottom() {
		float exactContentHeight = FloatMath.floor(mBounceScrollableView.getContentHeight()
				* mBounceScrollableView.getScale());
		return mBounceScrollableView.getScrollY() >= (exactContentHeight - mBounceScrollableView.getHeight());
	}

}
