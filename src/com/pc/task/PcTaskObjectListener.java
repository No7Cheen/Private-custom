package com.pc.task;

/**
 */
public abstract class PcTaskObjectListener extends PcTaskListener {

	/**
	 * 执行开始后调用.
	 * @param <T> 返回的对象
	 */
	public abstract <T> void update(T entity);

}
