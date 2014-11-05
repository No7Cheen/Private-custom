package com.pc.db.storage;

import java.util.List;

public class SqliteStorageListener {

	/**
	 * 插入数据的监听
	 */
	public static abstract interface DataInsertListener {

		/**
		 * On success
		 * @param paramLong the param long
		 */
		public abstract void onSuccess(long paramLong);

		/**
		 * On failure
		 * @param errorCode the error code
		 * @param errorMessage the error message
		 */
		public abstract void onFailure(int errorCode, String errorMessage);
	}

	/**
	 * 查询数据的监听
	 */
	public static abstract interface DataInfoListener {

		/**
		 * On success
		 * @param paramList the param list
		 */
		public abstract void onSuccess(List<?> paramList);

		/**
		 * On failure
		 * @param errorCode the error code
		 * @param errorMessage the error message
		 */
		public abstract void onFailure(int errorCode, String errorMessage);
	}

	/**
	 * 修改数据的监听
	 * @see AbDataOperationEvent
	 */
	public static abstract interface DataOperationListener {

		/**
		 * On success
		 * @param paramLong the param long
		 */
		public abstract void onSuccess(long paramLong);

		/**
		 * On failure
		 * @param errorCode the error code
		 * @param errorMessage the error message
		 */
		public abstract void onFailure(int errorCode, String errorMessage);
	}
}
