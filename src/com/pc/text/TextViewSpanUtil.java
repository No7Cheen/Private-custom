/**
 * @(#)TextViewSpanUtil.java 2014-1-13 Copyright 2014 it.kedacom.com, Inc. All
 *                           rights reserved.
 */
package com.pc.text;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.provider.Browser;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import com.pc.utils.StringUtils;
import com.pc.utils.log.PcLog;

/**
 * TextView Span Util
 * 
 * @author chenj
 * @date 2014-1-13
 */

public class TextViewSpanUtil {

	/**
	 * 渲染字体前景色
	 * 
	 * @param tv TextView
	 * @param color 颜色
	 */
	public static void foregroundColor(TextView tv, int color) {
		if (tv == null) return;

		if (tv.getText() == null || StringUtils.isNull(tv.getText().toString())) {
			return;
		}

		int end = tv.getText().length() - 1;

		SpannableStringBuilder stryle = new SpannableStringBuilder(tv.getText().toString());
		stryle.setSpan(new ForegroundColorSpan(color), 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv.setText(stryle);
	}

	/**
	 * 渲染字体前景色
	 * 
	 * @param tv TextView
	 * @param start 起始位置
	 * @param end 结束位置
	 * @param color 颜色
	 */
	public static void foregroundColor(TextView tv, int start, int end, int color) {
		if (tv == null) return;

		if (tv.getText() == null || StringUtils.isNull(tv.getText().toString())) {
			return;
		}

		SpannableStringBuilder stryle = new SpannableStringBuilder(tv.getText().toString());
		stryle.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv.setText(stryle);
	}

	/**
	 * 渲染字体前景色
	 * 
	 * @param context
	 * @param tv TextView
	 * @param start 起始位置
	 * @param end 结束位置
	 * @param appearance TextAppearanceSpan,用于new TextAppearanceSpan(context, appearance)
	 */
	public static void foregroundColorSpan(Context context, TextView tv, int start, int end, int appearance) {
		if (tv == null) {
			return;
		}

		if (tv.getText() == null || StringUtils.isNull(tv.getText().toString())) {
			return;
		}

		if (start < 0) {
			return;
		}

		if (end > tv.getText().length()) {
			return;
		}

		Spannable span = (Spannable) tv.getText();
		TextAppearanceSpan textappearancespan = new TextAppearanceSpan(context, appearance);
		span.setSpan(textappearancespan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

	/**
	 * 渲染字体前景色
	 * 
	 * @param context
	 * @param tv TextView
	 * @param appearance R.style.TextColor_60D4FD
	 */
	public static void foregroundColorSpan(Context context, TextView tv, int appearance) {
		if (tv == null) return;

		if (tv.getText() == null || StringUtils.isNull(tv.getText().toString())) {
			return;
		}

		Spannable span = (Spannable) tv.getText();
		TextAppearanceSpan textappearancespan = new TextAppearanceSpan(context, appearance);
		span.setSpan(textappearancespan, 0, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

	/**
	 * 设置带有前景色的TextView
	 * 
	 * @param context
	 * @param tv TextView
	 * @param text 字符串
	 * @param appearance TextAppearanceSpan属性
	 */
	public static void setTextPaintForegroundColor(Context context, TextView tv, String text, int appearance) {
		if (tv == null) return;

		if (StringUtils.isNull(text)) {
			return;
		}

		Spannable span = (Spannable) tv.getText();
		TextAppearanceSpan textappearancespan = new TextAppearanceSpan(context, appearance);
		span.setSpan(textappearancespan, 0, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

	/**
	 * 设置带有前景色的TextView
	 * 
	 * @param context
	 * @param tv TextView
	 * @param text 字符串
	 * @param start 起始位置
	 * @param end 结束位置
	 * @param appearance TextAppearanceSpan属性
	 */
	public static void setTextPaintForegroundColor(Context context, TextView tv, String text, int start, int end, int appearance) {
		if (tv == null) return;

		if (StringUtils.isNull(text)) {
			return;
		}

		if (start < 0) return;

		if (end > text.length()) return;

		Spannable span = (Spannable) tv.getText();
		TextAppearanceSpan textappearancespan = new TextAppearanceSpan(context, appearance);
		span.setSpan(textappearancespan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

	/**
	 * 添加下划线
	 * 
	 * @param context
	 * @param tv TextView
	 * @param start 起始位置
	 * @param end 结束位置
	 * @param appearance TextAppearanceSpan属性
	 */
	public static void setUnderline(Context context, TextView tv, int start, int end, int appearance) {
		if (tv == null) return;

		if (tv.getText() == null || StringUtils.isNull(tv.getText().toString())) {
			return;
		}

		if (start < 0) return;

		if (end > tv.getText().length()) return;

		Spannable span = (Spannable) tv.getText();
		TextAppearanceSpan textappearancespan = new TextAppearanceSpan(context, appearance);
		span.setSpan(textappearancespan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		span.setSpan(new UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

	/**
	 * 设置TextView部分字符可点击
	 *
	 * @param context
	 * @param tv TextView
	 * @param start 起始位置
	 * @param end 结束位置
	 * @param appearance TextAppearanceSpan属性
	 * @param onClickListener onClick Listener
	 */
	public static void setTextClickableSpan(Context context, TextView tv, int start, int end, int appearance, ClickableSpan onClickListener) {
		if (tv == null) return;

		if (tv.getText() == null || StringUtils.isNull(tv.getText().toString())) {
			return;
		}

		if (start < 0) return;

		if (end > tv.getText().length()) return;

		Spannable span = (Spannable) tv.getText();
		TextAppearanceSpan textappearancespan = new TextAppearanceSpan(context, appearance);
		if (onClickListener != null) {
			span.setSpan(onClickListener, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		span.setSpan(textappearancespan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv.setMovementMethod(LinkMovementMethod.getInstance());// 可点击
		tv.setFocusable(false);
		tv.setFocusableInTouchMode(false);
	}

	/**
	 * TextView链接
	 *
	 * @param tv TextView
	 * @param start url起始位置
	 * @param end url结束位置
	 * @param appearance TextAppearanceSpan属性
	 */
	public static void urlLinkMovementSpan(TextView tv, int start, int end, int appearance) {
		if (tv == null || tv.getText() == null || StringUtils.isNull(tv.getText().toString())) return;

		if (start < 0) return;

		if (end > tv.getText().length()) return;

		String url = tv.getText().subSequence(start, end).toString();

		Spannable span = (Spannable) tv.getText();
		TextAppearanceSpan textappearancespan = new TextAppearanceSpan(tv.getContext(), appearance);
		span.setSpan(new MyURLSpan(url), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		span.setSpan(textappearancespan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv.setMovementMethod(LinkMovementMethod.getInstance());// 可点击
		tv.setFocusable(false);
		tv.setFocusableInTouchMode(false);
	}

	/**
	 * TextView链接
	 *
	 * @param tv TextView
	 * @param start 超链接起始位置
	 * @param end 超链接结束位置
	 * @param appearance TextAppearanceSpan属性
	 * @param url 链接
	 */
	public static void urlLinkMovementSpan(TextView tv, int start, int end, int appearance, String url) {
		if (tv == null || tv.getText() == null || StringUtils.isNull(url) || StringUtils.isNull(tv.getText().toString())) return;

		if (!(url.toLowerCase().startsWith("http:"))) {
			return;
		}

		if (start < 0) return;

		if (end > tv.getText().length()) return;

		Spannable span = (Spannable) tv.getText();
		TextAppearanceSpan textappearancespan = new TextAppearanceSpan(tv.getContext(), appearance);
		span.setSpan(new MyURLSpan(url), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		span.setSpan(textappearancespan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv.setMovementMethod(LinkMovementMethod.getInstance());// 可点击
		tv.setFocusable(false);
		tv.setFocusableInTouchMode(false);
	}

	/**
	 * 设置TextViewBitmap
	 *
	 * @param tv TextView
	 * @param start Bitmap起始位置
	 * @param end Bitmap结束位置
	 * @param bitmap 位图
	 */
	public static void setImgSpan(TextView tv, int start, int end, Bitmap bitmap) {
		if (tv == null || tv.getText() == null || StringUtils.isNull(tv.getText().toString()) || bitmap == null) return;

		if (start < 0 || end > tv.getText().length()) return;

		Spannable span = (Spannable) tv.getText();
		ImageSpan imgSpan = new ImageSpan(bitmap);
		span.setSpan(imgSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

	/**
	 * Url Span
	 * 
	 * @author chenj
	 */
	public static class MyURLSpan extends URLSpan {

		public MyURLSpan(Parcel src) {
			super(src);
		}

		public MyURLSpan(String url) {
			super(url);
		}

		@Override
		public void onClick(View widget) {
			String mURL = getURL();
			if (!StringUtils.isNull(mURL) && mURL.startsWith("www.")) {
				mURL = "http://" + mURL;
			}

			Uri uri = Uri.parse(mURL);
			Context context = widget.getContext();
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
			try {
				context.startActivity(intent);
			} catch (ActivityNotFoundException e) {
				if (PcLog.isPrint) {
					PcLog.e("ActivityNotFoundException", "Activity Not Found Exception : ", e);
				}
			}
		}
	}

	/**
	 * 
	 * 
	 */
	public static class MyLinkMovementMethod extends LinkMovementMethod {

		private static MyLinkMovementMethod sInstance;

		public static MyLinkMovementMethod getInstance() {
			if (sInstance == null) sInstance = new MyLinkMovementMethod();

			return sInstance;
		}
	}

}
