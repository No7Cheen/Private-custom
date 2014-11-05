package com.pc.task;

/**
 * 数据执行单位.
 * @version v1.0
 */
public class PcTaskItem {

	/** 记录的当前索引. */
	public int position;

	/** 执行完成的回调接口. */
	public PcTaskListener listener;

	/** 执行完成的结果. */
	private Object result;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public PcTaskListener getListener() {
		return listener;
	}

	public void setListener(PcTaskListener listener) {
		this.listener = listener;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

}
