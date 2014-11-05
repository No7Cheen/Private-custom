/**
 * @(#)AsyncLoaderImageViewUtils.java 2013-6-27 Copyright 2013 it.kedacom.com,
 *                                    Inc. All rights reserved.
 */

package com.pc.imgs.downloader;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @author chenjian
 * @date 2013-6-27
 */

public class AsyncLoaderImageViewUtils {

	public final static String ACTION_DOWNLOAD = "com.pc.utils.imgs.download.AsyncLoaderImageViewUtils";

	/**
	 * 得到图片字节流 数组大小
	 * @param inStream
	 * @return
	 */
	public static byte[] readStream(InputStream inStream) {
		try {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}

			byte[] data = outStream.toByteArray();
			outStream.flush();
			outStream.close();
			inStream.close();

			return data;
		} catch (OutOfMemoryError e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}
}
