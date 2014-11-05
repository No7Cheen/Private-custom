package com.pc.imgs.downloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.pc.utils.StringUtils;
import com.pc.utils.ValidateUtils;
import com.pc.utils.imgs.ImageUtil;

/**
 * 获取网络图片
 * @author chenjian
 */
public class WebImageView {

	public static Bitmap loadRemoteImage(String url) {
		if (StringUtils.isNull(url)) {
			return null;
		}

		Bitmap bmp = null;
		URL myURL = null;
		InputStream is = null;
		try {
			myURL = new URL(url);

			HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
			myURL.openConnection();
			conn.setDoInput(true);
			conn.connect();
			is = conn.getInputStream();

			bmp = BitmapFactory.decodeStream(is);
		} catch (MalformedURLException e) {
			return bmp;
		} catch (OutOfMemoryError error) {
			return null;
		} catch (IOException e) {
			return bmp;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
			}
		}

		return bmp;
	}

	public static Bitmap loadRemoteImage2(String url) {
		Bitmap bmp = null;
		if (StringUtils.isNull(url)) return null;
		InputStream is = null;
		try {
			URL myURL = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
			conn.setDoInput(true);
			conn.connect();
			is = conn.getInputStream();
			int len = conn.getContentLength();
			if (len != -1) {
				byte[] imgData = new byte[len];
				byte[] temp = new byte[512];
				int readLen = 0;
				int destPos = 0;
				while ((readLen = is.read(temp)) > 0) {
					System.arraycopy(temp, 0, imgData, destPos, readLen);
					destPos += readLen;
				}

				bmp = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
			}
		} catch (OutOfMemoryError error) {
			return null;
		} catch (Exception e) {
			return bmp;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
			}
		}

		return bmp;
	}

	public static void loadRemoteImageAndSave(String url, String dir, String fileName) {
		loadRemoteImageAndSave(url, dir, fileName, true);
	}

	/**
	 * @param url
	 * @param dir
	 * @param fileName
	 * @param checkEextension
	 */
	public static void loadRemoteImageAndSave(String url, String dir, String fileName, boolean checkEextension) {
		try {
			InputStream inputStream = WebImageView.loadRemoteImageReturnInputStream(url);
			final String extension = ".png";
			if (checkEextension && !StringUtils.isNull(fileName) && !fileName.endsWith(extension)) {
				fileName = fileName + extension;
			}

			if (inputStream != null) {
				ImageUtil.saveImageFile(dir, fileName, inputStream);
				inputStream.close();
			}
		} catch (Exception e) {
		}
	}

	/**
	 * @date 2013-8-26
	 * @param url
	 * @return inputStream
	 */
	public static InputStream loadRemoteImageReturnInputStream(String url) {
		if (StringUtils.isNull(url)) {
			return null;
		}

		URL myURL = null;
		InputStream inputStream = null;
		try {
			myURL = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
			myURL.openConnection();
			conn.setDoInput(true);
			conn.connect();
			inputStream = conn.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			// 这里不需要关闭，因为还要返回这个inputstream
			// try {
			// if (is != null) {
			// is.close();
			// }
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
		}
		return inputStream;
	}

	public static String changeToUrl(String url, String httpPrefix) {
		if (StringUtils.isNull(url)) {
			return "";
		}

		if (StringUtils.isNull(httpPrefix)) {
			return url;
		}

		// 检测是否为一个有效的URL
		if (ValidateUtils.isUrl(url.toLowerCase())) {
			return url;
		}

		return httpPrefix + url;
	}
}
