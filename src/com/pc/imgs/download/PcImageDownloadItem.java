package com.pc.imgs.download;

import android.graphics.Bitmap;

import com.pc.utils.imgs.EmImageOperationType;

/**
 * 图片下载Item
 */
public class PcImageDownloadItem {

	// 需要下载的图片的互联网地址
	public String imageUrl;

	// 显示的图片的宽
	public int width;

	// 显示的图片的高
	public int height;

	// 图片存储目录
	public String dir;

	// 图片的处理类型
	public EmImageOperationType type = EmImageOperationType.ORIGINALIMG;

	// 下载完成的到的Bitmap对象
	public Bitmap bitmap;

	// 下载完成的回调接口
	public PcImageDownloadListener listener;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	/** 
	 * @return the dir
	 */
	public String getDir() {
		return dir;
	}

	/**
	 * @param dir the dir to set
	 */
	public void setDir(String dir) {
		this.dir = dir;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public EmImageOperationType getType() {
		return type;
	}

	public void setType(EmImageOperationType type) {
		this.type = type;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public PcImageDownloadListener getListener() {
		return listener;
	}

	public void setListener(PcImageDownloadListener listener) {
		this.listener = listener;
	}

}
