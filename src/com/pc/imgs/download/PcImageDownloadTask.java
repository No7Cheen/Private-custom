package com.pc.imgs.download;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.pc.utils.StringUtils;
import com.pc.utils.log.PcLog;

/**
 * AsyncTask实现的下载，单次下载
 * 
 * @author chenj
 * @date 2014-7-18
 */
public class PcImageDownloadTask extends AsyncTask<PcImageDownloadItem, Integer, PcImageDownloadItem> {

	// 下载完成后的消息句柄
	@Deprecated
	private static Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (null == msg) return;

			PcImageDownloadItem item = (PcImageDownloadItem) msg.obj;
			if (null == item) return;

			item.listener.update(item.bitmap, item.imageUrl);
		}
	};

	public PcImageDownloadTask() {
		super();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	/**
	 * 这里的第一个参数对应AsyncTask中的第一个参数 这里的String返回值对应AsyncTask的第三个参数
	 * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
	 * 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作
	 */
	@Override
	protected PcImageDownloadItem doInBackground(PcImageDownloadItem... items) {
		PcImageDownloadItem item = items[0];
		if (null == item) {
			return null;
		}

		String url = StringUtils.trim(item.imageUrl);
		String cacheKey = PcImageDownloadCache.getCacheKey(url, item.width, item.height, item.type);
		item.bitmap = PcImageDownloadCache.getBitmapFromCache(cacheKey);
		if (item.bitmap == null) {
			item.bitmap = PcImageFileUtil.getBitmapFromSDCache(item.imageUrl, item.type, item.width, item.height, item.dir);
			PcImageDownloadCache.addBitmapToCache(cacheKey, item.bitmap);
		} else {
			if (PcLog.isPrint) {
				PcLog.d(PcImageDownloadTask.class.getSimpleName(), "从内存缓存中得到图片:" + cacheKey);
			}
		}

		// if (item.listener != null) {
		// Message msg = handler.obtainMessage();
		// msg.obj = item;
		// handler.sendMessage(msg);
		// }

		return item;
	}

	@Override
	protected void onPostExecute(PcImageDownloadItem item) {
		try {
			if (null != item && null != item.listener) {
				item.listener.update(item.bitmap, item.imageUrl);
			}
		} catch (Exception e) {
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

}
