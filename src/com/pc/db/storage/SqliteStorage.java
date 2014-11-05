package com.pc.db.storage;

import java.util.List;

import android.content.Context;

import com.pc.db.orm.dao.DbDaoImpl;
import com.pc.db.storage.SqliteStorageListener.DataInfoListener;
import com.pc.db.storage.SqliteStorageListener.DataInsertListener;
import com.pc.db.storage.SqliteStorageListener.DataOperationListener;
import com.pc.task.PcTaskItem;
import com.pc.task.PcTaskListListener;
import com.pc.task.PcTaskListener;
import com.pc.task.PcTaskQueue;

/**
 * The Class AbSqliteStorage.
 */
public class SqliteStorage {

	/** The m context. */
	private static Context mContext;

	/** The m sqlite storage. */
	private static SqliteStorage mSqliteStorage = null;

	/** The m ab task queue. */
	private static PcTaskQueue mAbTaskQueue = null;

	/** The error code100. */
	private int errorCode100 = 100;

	/** The error message100. */
	private String errorMessage100 = "参数错误";

	/** The error code101. */
	private int errorCode101 = 101;

	/** The error message101. */
	private String errorMessage101 = "执行时错误";

	/** The ret value. */
	private long retValue = -1;

	/**
	 * 获取存储实例
	 * 
	 * @param context the context
	 * @return single instance of AbSqliteStorage
	 */
	public static SqliteStorage getInstance(Context context) {
		mContext = context;
		if (null == mSqliteStorage) {
			mSqliteStorage = new SqliteStorage(context);
		}
		// 用队列避免并发访问数据库问题
		mAbTaskQueue = PcTaskQueue.getInstance();
		return mSqliteStorage;
	}

	/**
	 * Instantiates a new ab sqlite storage
	 * 
	 * @param context the context
	 */
	private SqliteStorage(Context context) {
		super();
	}

	/**
	 * 插入数据
	 * 
	 * @param <T> the generic type
	 * @param entity 实体类 设置了对象关系映射
	 * @param dao 实现AbDBDaoImpl的Dao
	 * @param paramDataInsertListener 返回监听器
	 */
	public <T> void insertData(final T entity, final DbDaoImpl<T> dao, final DataInsertListener paramDataInsertListener) {
		if (null == entity) {
			if (paramDataInsertListener != null) {
				paramDataInsertListener.onFailure(errorCode100, errorMessage100);
			}

			return;
		}

		PcTaskItem item = new PcTaskItem();
		item.listener = new PcTaskListener() {

			@Override
			public void update() {
				if (retValue >= 0) {
					if (paramDataInsertListener != null) {
						paramDataInsertListener.onSuccess(retValue);
					}
				} else {
					if (paramDataInsertListener != null) {
						paramDataInsertListener.onFailure(errorCode101, errorMessage101);
					}
				}
			}

			@Override
			public void get() {
				// 执行插入
				// (1)获取数据库
				dao.startWritableDatabase(false);
				// (2)执行
				retValue = dao.insert(entity);
				// (3)关闭数据库
				dao.closeDatabase(false);
			}
		};
		mAbTaskQueue.execute(item);
	}

	/**
	 * 插入数据
	 * 
	 * @param <T> the generic type
	 * @param entityList 实体类 设置了对象关系映射
	 * @param dao 实现AbDBDaoImpl的Dao
	 * @param paramDataInsertListener 返回监听器
	 */
	public <T> void insertData(final List<T> entityList, final DbDaoImpl<T> dao, final DataInsertListener paramDataInsertListener) {
		if (null == entityList) {
			if (paramDataInsertListener != null) {
				paramDataInsertListener.onFailure(errorCode100, errorMessage100);
			}

			return;
		}

		PcTaskItem item = new PcTaskItem();
		item.listener = new PcTaskListener() {

			@Override
			public void update() {
				if (retValue >= 0) {
					if (paramDataInsertListener != null) {
						paramDataInsertListener.onSuccess(retValue);
					}
				} else {
					if (paramDataInsertListener != null) {
						paramDataInsertListener.onFailure(errorCode101, errorMessage101);
					}
				}
			}

			@Override
			public void get() {
				// 执行插入
				// (1)获取数据库
				dao.startWritableDatabase(false);
				// (2)执行
				retValue = dao.insertList(entityList);
				// (3)关闭数据库
				dao.closeDatabase(false);

			}
		};
		mAbTaskQueue.execute(item);
	}

	/**
	 * Find data.
	 * @param <T> 查询数据
	 * @param storageQuery the storage query
	 * @param dao 实现AbDBDaoImpl的Dao
	 * @param paramDataInsertListener 返回监听器
	 */
	public <T> void findData(final StorageQuery storageQuery, final DbDaoImpl<T> dao, final DataInfoListener paramDataInsertListener) {

		final PcTaskItem item = new PcTaskItem();
		item.listener = new PcTaskListListener() {

			@Override
			public void update(List<?> paramList) {
				if (paramDataInsertListener != null) {
					paramDataInsertListener.onSuccess(paramList);
				}
			}

			@Override
			public void get() {
				List<?> list = null;
				// 执行插入
				// (1)获取数据库
				dao.startReadableDatabase(false);
				// (2)执行
				if (storageQuery.getLimit() != -1 && storageQuery.getOffset() != -1) {
					list = dao.queryList(null, storageQuery.getWhereClause(), storageQuery.getWhereArgs(), storageQuery.getGroupBy(), storageQuery.getHaving(),
							storageQuery.getOrderBy() + " limit " + storageQuery.getLimit() + " offset " + storageQuery.getOffset(), null);
				} else {
					list = dao.queryList(null, storageQuery.getWhereClause(), storageQuery.getWhereArgs(), storageQuery.getGroupBy(), storageQuery.getHaving(),
							storageQuery.getOrderBy(), null);
				}
				// (3)关闭数据库
				dao.closeDatabase(false);

				// 设置返回结果
				item.setResult(list);

			}
		};
		mAbTaskQueue.execute(item);

	}

	/**
	 * 修改数据.
	 * @param <T> the generic type
	 * @param entity 实体类 设置了对象关系映射
	 * @param dao 实现AbDBDaoImpl的Dao
	 * @param paramDataInsertListener 返回监听器
	 */
	public <T> void updateData(final T entity, final DbDaoImpl<T> dao, final DataOperationListener paramDataInsertListener) {
		if (null == entity) {
			if (paramDataInsertListener != null) {
				paramDataInsertListener.onFailure(errorCode100, errorMessage100);
			}
			return;
		}

		PcTaskItem item = new PcTaskItem();
		item.listener = new PcTaskListListener() {

			@Override
			public void update(List<?> paramList) {
				if (retValue >= 0) {
					if (paramDataInsertListener != null) {
						paramDataInsertListener.onSuccess(retValue);
					}
				} else {
					if (paramDataInsertListener != null) {
						paramDataInsertListener.onFailure(errorCode101, errorMessage101);
					}
				}
			}

			@Override
			public void get() {
				// 执行插入
				// (1)获取数据库
				dao.startWritableDatabase(false);
				// (2)执行
				retValue = dao.update(entity);
				// (3)关闭数据库
				dao.closeDatabase(false);

			}
		};
		mAbTaskQueue.execute(item);
	}

	/**
	 * 修改数据.
	 * @param <T> the generic type
	 * @param entityList 实体类 设置了对象关系映射
	 * @param dao 实现AbDBDaoImpl的Dao
	 * @param paramDataInsertListener 返回监听器
	 */
	public <T> void updateData(final List<T> entityList, final DbDaoImpl<T> dao, final DataOperationListener paramDataInsertListener) {

		if (entityList != null) {

			PcTaskItem item = new PcTaskItem();
			item.listener = new PcTaskListener() {

				@Override
				public void update() {
					if (retValue >= 0) {
						if (paramDataInsertListener != null) {
							paramDataInsertListener.onSuccess(retValue);
						}
					} else {
						if (paramDataInsertListener != null) {
							paramDataInsertListener.onFailure(errorCode101, errorMessage101);
						}
					}
				}

				@Override
				public void get() {
					// 执行插入
					// (1)获取数据库
					dao.startWritableDatabase(false);
					// (2)执行
					retValue = dao.updateList(entityList);
					// (3)关闭数据库
					dao.closeDatabase(false);

				}
			};
			mAbTaskQueue.execute(item);

		} else {
			if (paramDataInsertListener != null) {
				paramDataInsertListener.onFailure(errorCode100, errorMessage100);
			}
		}

	}

	/**
	 * 修改数据
	 * 
	 * @param <T> the generic type
	 * @param storageQuery 条件实体
	 * @param dao 实现AbDBDaoImpl的Dao
	 * @param paramDataInsertListener 返回监听器
	 */
	public <T> void deleteData(final StorageQuery storageQuery, final DbDaoImpl<T> dao, final DataOperationListener paramDataInsertListener) {

		PcTaskItem item = new PcTaskItem();
		item.listener = new PcTaskListener() {

			@Override
			public void update() {
				if (retValue >= 0) {
					if (paramDataInsertListener != null) {
						paramDataInsertListener.onSuccess(retValue);
					}
				} else {
					if (paramDataInsertListener != null) {
						paramDataInsertListener.onFailure(errorCode101, errorMessage101);
					}
				}
			}

			@Override
			public void get() {
				// 执行插入
				// (1)获取数据库
				dao.startWritableDatabase(false);
				// (2)执行
				retValue = dao.delete(storageQuery.getWhereClause(), storageQuery.getWhereArgs());
				// (3)关闭数据库
				dao.closeDatabase(false);

			}
		};
		mAbTaskQueue.execute(item);
	}

	/**
	 * 释放存储实例
	 */
	public void release() {
		if (mAbTaskQueue != null) {
			mAbTaskQueue.quit();
		}
	}

}
