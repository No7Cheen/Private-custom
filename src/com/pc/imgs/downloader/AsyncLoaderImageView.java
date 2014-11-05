/**
 * @(#)AsyncLoaderImageView.java 2013-6-27 Copyright 2013 it.kedacom.com, Inc.
 *                               All rights reserved.
 */

package com.pc.imgs.downloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.pc.utils.StringUtils;
import com.pc.utils.imgs.ImageUtil;

/**
 * 图片下载
 * @author chenjian
 * @date 2013-6-27
 */

public class AsyncLoaderImageView {

	private final String TAG = "AsyncLoaderImageView";

	public interface ImageCallback {

		public void imageLoaded(Bitmap imageBitmap, String imageUrl);
	}

	/**
	 * 异步下载图片
	 * @param imageUrl
	 * @param roundPx
	 * @param imageCallback
	 * @return
	 */
	public Bitmap asyncLoaderImageBitmap(final String imageUrl, final float roundPx, final ImageCallback imageCallback) {
		final Handler handler = new Handler() {

			@Override
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Bitmap) message.obj, imageUrl);
			}
		};

		new Thread() {

			@Override
			public void run() {
				Bitmap bmp = loadImageFromUrl(imageUrl, roundPx);
				Message message = handler.obtainMessage(0, bmp);
				handler.sendMessage(message);
			}
		}.start();

		return null;
	}

	/**
	 * 下载图片
	 * @param url
	 * @param roundPx
	 * @param fileName
	 * @param dir
	 * @return
	 */
	public Bitmap loadImageFromUrl(String url, float roundPx) {
		if (StringUtils.isNull(url)) {
			return null;
		}

		Bitmap bmp = null;
		URL myURL = null;
		InputStream is = null;
		try {
			myURL = new URL(url);

			HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
			conn.setDoInput(true);
			conn.connect();
			is = conn.getInputStream();

			bmp = BitmapFactory.decodeStream(is);
		} catch (MalformedURLException e) {
			Log.e(TAG, "MalformedURLException : ", e);
			return bmp;
		} catch (IOException e) {
			Log.e(TAG, "IOException : ", e);
			return bmp;
		} catch (Exception e) {
			Log.e(TAG, "Exception : ", e);
			return bmp;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				Log.e(TAG, "Exception : ", e);
			}
		}

		if (roundPx > 0 && bmp != null) {
			return ImageUtil.createRoundedCornerBitmap(bmp, roundPx);
		}

		return bmp;
	}
}
