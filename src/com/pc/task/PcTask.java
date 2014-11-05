package com.pc.task;

import android.os.AsyncTask;

/**
 * 下载数据的任务实现，单次下载
 * 
 * @version v1.0
 */
public class PcTask extends AsyncTask<PcTaskItem, Integer, PcTaskItem> {

	public PcTask() {
		super();
	}

	@Override
	protected PcTaskItem doInBackground(PcTaskItem... items) {
		PcTaskItem item = items[0];
		if (item.listener != null) {
			item.listener.get();
		}
		return item;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(PcTaskItem item) {
		if (item.listener != null) {
			item.listener.update();
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

}
