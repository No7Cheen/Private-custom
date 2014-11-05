package com.pc.task;

import java.util.List;

/**
 * 数据执行的接口.
 * @version v1.0
 */
public abstract class PcTaskListListener extends PcTaskListener {

	/**
	 * 执行完成后回调. 不管成功与否都会执行
	 * @param paramList 返回的List
	 */
	public abstract void update(List<?> paramList);

}
