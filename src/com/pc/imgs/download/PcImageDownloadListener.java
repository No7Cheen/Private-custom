package com.pc.imgs.download;

import android.graphics.Bitmap;

/**
 * 图片下载的回调接口
 */
public interface PcImageDownloadListener {

	/**
	 * 更新视图
	 *
	 * @param imageView 显示Bitmap的View
	 * @param bitmap Bitmap
	 * @param imageUrl Url
	 */
	public void update(Bitmap bitmap, String imageUrl);

}
