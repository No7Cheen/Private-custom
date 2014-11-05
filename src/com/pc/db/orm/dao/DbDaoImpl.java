package com.pc.db.orm.dao;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pc.db.BasicDBDao;
import com.pc.db.orm.TableHelper;
import com.pc.db.orm.annotation.ActionType;
import com.pc.db.orm.annotation.Column;
import com.pc.db.orm.annotation.Id;
import com.pc.db.orm.annotation.Relations;
import com.pc.db.orm.annotation.RelationsType;
import com.pc.db.orm.annotation.Table;
import com.pc.utils.StringUtils;
import com.pc.utils.log.PcLog;

/**
 * The Class AbDBDaoImpl
 * @param <T> the generic type
 */
public class DbDaoImpl<T> extends BasicDBDao implements DbDBDao<T> {

	/** The tag. */
	private String TAG = DbDaoImpl.class.getSimpleName();

	/** The db helper. */
	private SQLiteOpenHelper dbHelper;

	/** 锁对象 */
	private final ReentrantLock lock = new ReentrantLock();

	/** The table name. */
	private String tableName;

	/** The id column. */
	private String idColumn;

	/** The clazz. */
	private Class<T> clazz;

	/** The all fields. */
	private List<Field> allFields;

	/** The Constant METHOD_INSERT. */
	private final int METHOD_INSERT = 0;

	/** The Constant METHOD_UPDATE. */
	private final int METHOD_UPDATE = 1;

	/** The Constant TYPE_NOT_INCREMENT. */
	private final int TYPE_NOT_INCREMENT = 0;

	/** The Constant TYPE_INCREMENT. */
	private final int TYPE_INCREMENT = 1;

	/** 这个Dao的数据库对象 */
	private SQLiteDatabase db;

	@SuppressWarnings("rawtypes")
	private Class relationsClazz;

	/**
	 * 用一个对象实体初始化这个数据库操作实现类
	 * 
	 * @param dbHelper 数据库操作实现类
	 * @param clazz 映射对象实体
	 * @param _RelationsClazz 关系对象（Android项目中混淆编译不能反射泛型获取类型）
	 */
	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	public DbDaoImpl(SQLiteOpenHelper dbHelper, Class<T> clazz, Class _RelationsClazz) {
		this.relationsClazz = _RelationsClazz;

		this.dbHelper = dbHelper;
		if (clazz == null) {
			this.clazz = ((Class<T>) ((java.lang.reflect.ParameterizedType) super.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
		} else {
			this.clazz = clazz;
		}

		if (this.clazz.isAnnotationPresent(Table.class)) {
			Table table = (Table) this.clazz.getAnnotation(Table.class);
			this.tableName = table.name();
		}

		// 加载所有字段
		this.allFields = TableHelper.joinFields(this.clazz.getDeclaredFields(), this.clazz.getSuperclass().getDeclaredFields());

		// 找到主键
		for (Field field : this.allFields) {
			if (field.isAnnotationPresent(Id.class)) {
				Column column = (Column) field.getAnnotation(Column.class);
				this.idColumn = column.name();
				break;
			}
		}

		if (PcLog.isPrint) {
			Log.d(TAG, "clazz:" + this.clazz + " tableName:" + this.tableName + " idColumn:" + this.idColumn);
		}
	}

	/**
	 * @param dbHelper
	 * @param clazz
	 */
	public DbDaoImpl(SQLiteOpenHelper dbHelper, Class<T> clazz) {
		this(dbHelper, clazz, null);
	}

	/**
	 * 初始化这个数据库操作实现类
	 * 
	 * @param dbHelper 数据库操作实现类
	 */
	public DbDaoImpl(SQLiteOpenHelper dbHelper) {
		this(dbHelper, null, null);
	}

	/** 
	 * @return the relationsClazz
	 */
	@SuppressWarnings("rawtypes")
	public Class getRelationsClazz() {
		return relationsClazz;
	}

	/**
	 * @param relationsClazz the relationsClazz to set
	 */
	@SuppressWarnings("rawtypes")
	public void setRelationsClazz(Class relationsClazz) {
		this.relationsClazz = relationsClazz;
	}

	/**
	 * @return the db helper
	 * @see com.pc.db.orm.dao.DbDBDao#getDbHelper()
	 */
	@Override
	public SQLiteOpenHelper getDbHelper() {
		return dbHelper;
	}

	/**
	 * 查询一条数据
	 * 
	 * @param id the id
	 * @return the t
	 * @see com.pc.db.orm.dao.DbDBDao#queryOne(int)
	 */
	@Override
	public T queryOne(int id) {
		synchronized (lock) {
			String selection = this.idColumn + " = ?";
			String[] selectionArgs = {
				Integer.toString(id)
			};

			if (PcLog.isPrint) {
				Log.d(TAG, "[queryOne]: select * from " + this.tableName + " where " + this.idColumn + " = '" + id + "'");
			}

			List<T> list = queryList(null, selection, selectionArgs, null, null, null, null);
			if (null == list || list.isEmpty()) {
				return null;
			}

			return list.get(0);
		}
	}

	/**
	 * 一种更灵活的方式查询，不支持对象关联，可以写完整的sql
	 * 
	 * @param sql 完整的sql如：select * from a ,b where a.id=b.id and a.id = ?
	 * @param selectionArgs 绑定变量值
	 * @param clazz 返回的对象类型
	 * @return the list
	 * @see com.pc.db.orm.dao.DbDBDao#rawQuery(java.lang.String, java.lang.String[])
	 */
	@Override
	public List<T> rawQuery(String sql, String[] selectionArgs, Class<T> clazz) {
		List<T> list = new ArrayList<T>();
		Cursor cursor = null;
		try {
			lock.lock();

			if (PcLog.isPrint) {
				Log.d(TAG, "[rawQuery]: " + getLogSql(sql, selectionArgs));
			}

			cursor = db.rawQuery(sql, selectionArgs);
			getListFromCursor(clazz, list, cursor);
		} catch (Exception e) {
			if (PcLog.isPrint) {
				Log.e(this.TAG, "[rawQuery] from DB Exception.", e);
			}
		} finally {
			closeCursor(cursor);
			lock.unlock();
		}

		return list;
	}

	/**
	 * @see com.pc.db.orm.dao.DbDBDao#exists(java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean exists(String selection, String[] selectionArgs) {
		Cursor cursor = null;
		try {
			lock.lock();

			cursor = db.query(this.tableName, null, selection, selectionArgs, null, null, null, null);
			return cursor != null ? cursor.getCount() > 0 : false;
		} catch (Exception e) {
		} finally {
			closeCursor(cursor);
			lock.unlock();
		}

		return false;
	}

	/**
	 * 
	 * @see com.pc.db.orm.dao.DbDBDao#exists(java.lang.String[], java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean exists(String[] columns, String selection, String[] selectionArgs) {
		Cursor cursor = null;
		try {
			lock.lock();

			cursor = db.query(this.tableName, columns, selection, selectionArgs, null, null, null, null);
			return cursor != null ? cursor.getCount() > 0 : false;
		} catch (Exception e) {
		} finally {
			closeCursor(cursor);
			lock.unlock();
		}

		return false;
	}

	/**
	 * 是否存在
	 * 
	 * @param sql the sql
	 * @param selectionArgs the selection args
	 * @return true, if is exist
	 * @see com.pc.db.orm.dao.DbDBDao#isExist(java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean isExist(String sql, String[] selectionArgs) {
		Cursor cursor = null;
		try {
			lock.lock();

			if (PcLog.isPrint) {
				Log.d(TAG, "[isExist]: " + getLogSql(sql, selectionArgs));
			}

			cursor = db.rawQuery(sql, selectionArgs);
			if (null == cursor) {
				return false;
			}

			if (cursor.getCount() > 0) {
				return true;
			}
		} catch (Exception e) {
			if (PcLog.isPrint) {
				Log.e(this.TAG, "[isExist] from DB Exception.", e);
			}
		} finally {
			closeCursor(cursor);
			lock.unlock();
		}

		return false;
	}

	/**
	 * 查询所有数据
	 * 
	 * @return the list
	 * @see com.pc.db.orm.dao.DbDBDao#queryList()
	 */
	@Override
	public List<T> queryList() {
		return queryList(null, null, null, null, null, null, null);
	}

	/**
	 * 查询列表
	 * @param columns the columns
	 * @param selection the selection
	 * @param selectionArgs the selection args
	 * @param groupBy the group by
	 * @param having the having
	 * @param orderBy the order by
	 * @param limit the limit
	 * @return the list
	 * @see com.pc.db.orm.dao.DbDBDao#queryList(java.lang.String[],java.lang.String, java.lang.String[], java.lang.String,java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	@Override
	public List<T> queryList(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
		List<T> list = new ArrayList<T>();
		Cursor cursor = null;
		try {
			lock.lock();

			if (PcLog.isPrint) {
				Log.d(TAG, "[queryList] from" + this.tableName + " where " + selection + "(" + selectionArgs + ")" + " group by " + groupBy + " having " + having + " order by "
						+ orderBy + " limit " + limit);
			}

			cursor = db.query(this.tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

			getListFromCursor(this.clazz, list, cursor);

			closeCursor(cursor);

			// 获取关联域的操作类型和关系类型
			String foreignKey = null;
			String type = null;
			String action = null;

			// 需要判断是否有关联表
			for (Field relationsField : allFields) {
				if (!relationsField.isAnnotationPresent(Relations.class)) {
					continue;
				}

				Relations relations = (Relations) relationsField.getAnnotation(Relations.class);
				// 获取外键列名
				foreignKey = relations.foreignKey();
				// 关联类型
				type = relations.type();
				// 操作类型
				action = relations.action();
				// 设置可访问
				relationsField.setAccessible(true);

				if (!(action.indexOf(ActionType.query) != -1)) {
					return list;
				}

				// 得到关联表的表名查询
				for (T entity : list) {
					// 一对一关系
					if (RelationsType.one2one.equals(type)) {
						// 获取这个实体的表名
						String relationsTableName = "";
						if (relationsField.getType().isAnnotationPresent(Table.class)) {
							Table table = (Table) relationsField.getType().getAnnotation(Table.class);
							relationsTableName = table.name();
						}

						List<T> relationsList = new ArrayList<T>();
						Field[] relationsEntityFields = relationsField.getType().getDeclaredFields();
						for (Field relationsEntityField : relationsEntityFields) {
							Column relationsEntityColumn = (Column) relationsEntityField.getAnnotation(Column.class);
							// 获取外键的值作为关联表的查询条件
							if (null != relationsEntityColumn && null != relationsEntityColumn.name() && relationsEntityColumn.name().equals(foreignKey)) {

								// 主表的用于关联表的foreignKey值
								String value = "-1";
								for (Field entityField : allFields) {
									// 设置可访问
									if (null != entityField) {
										entityField.setAccessible(true);
										Column entityForeignKeyColumn = (Column) entityField.getAnnotation(Column.class);
										if (entityForeignKeyColumn == null) {
											continue;
										}
										if (entityForeignKeyColumn.name().equals(foreignKey)) {
											value = String.valueOf(entityField.get(entity));
											break;
										}
									}
								}
								// 查询数据设置给这个域
								cursor = db.query(relationsTableName, null, foreignKey + " = ?", new String[] {
									value
								}, null, null, null, null);
								getListFromCursor(relationsField.getType(), relationsList, cursor);
								if (relationsList.size() > 0) {
									// 获取关联表的对象设置值
									relationsField.set(entity, relationsList.get(0));
								}

								break;
							}
						}
					}

					// 一对多关系
					else if (RelationsType.one2many.equals(type) || RelationsType.many2many.equals(type)) {
						// 得到泛型里的class类型对象
						Class listEntityClazz = null;
						Class<?> fieldClass = relationsField.getType();
						if (fieldClass.isAssignableFrom(List.class)) {
							Type fc = relationsField.getGenericType();
							if (fc == null) continue;
							if (fc instanceof ParameterizedType) {
								ParameterizedType pt = (ParameterizedType) fc;
								listEntityClazz = (Class) pt.getActualTypeArguments()[0];
							}
						}

						// 混淆编译不能反射泛型获取类型
						if (listEntityClazz == null) {
							listEntityClazz = relationsClazz;
						}

						if (listEntityClazz == null) {
							if (PcLog.isPrint) {
								Log.e(TAG, "对象模型需要设置List的泛型");
							}
							return list;
						}

						// 得到表名
						String relationsTableName = "";
						if (listEntityClazz.isAnnotationPresent(Table.class)) {
							Table table = (Table) listEntityClazz.getAnnotation(Table.class);
							relationsTableName = table.name();
						}

						List<T> relationsList = new ArrayList<T>();
						Field[] relationsEntityFields = listEntityClazz.getDeclaredFields();
						for (Field relationsEntityField : relationsEntityFields) {
							Column relationsEntityColumn = (Column) relationsEntityField.getAnnotation(Column.class);
							// 获取外键的值作为关联表的查询条件
							if (null != relationsEntityColumn && null != relationsEntityColumn.name() && relationsEntityColumn.name().equals(foreignKey)) {
								// 主表的用于关联表的foreignKey值
								String value = "-1";
								for (Field entityField : allFields) {
									// 设置可访问
									if (null != entityField) {
										entityField.setAccessible(true);
										Column entityForeignKeyColumn = (Column) entityField.getAnnotation(Column.class);
										if (entityForeignKeyColumn.name().equals(foreignKey)) {
											value = String.valueOf(entityField.get(entity));
											break;
										}
									}
								}
								// 查询数据设置给这个域
								cursor = db.query(relationsTableName, null, foreignKey + " = ?", new String[] {
									value
								}, null, null, null, null);
								getListFromCursor(listEntityClazz, relationsList, cursor);
								if (relationsList.size() > 0) {
									// 获取关联表的对象设置值
									relationsField.set(entity, relationsList);
								}

								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			if (PcLog.isPrint) {
				android.util.Log.e(this.TAG, "[queryList] from DB Exception", e);
			}
		} finally {
			closeCursor(cursor);
			lock.unlock();
		}

		return list;
	}

	/**
	 * 简单一些的查询
	 * 
	 * @param selection the selection
	 * @param selectionArgs the selection args
	 * @return the list
	 * @see com.pc.db.orm.dao.DbDBDao#queryList(java.lang.String,java.lang.String[])
	 * @author: zhaoqp
	 */
	@Override
	public List<T> queryList(String selection, String[] selectionArgs) {
		return queryList(null, selection, selectionArgs, null, null, null, null);
	}

	/**
	 * 从游标中获得映射对象列表
	 * 
	 * @param list 返回的映射对象列表
	 * @param cursor 当前游标
	 * @return the list from cursor
	 * @throws IllegalAccessException the illegal access exception
	 * @throws InstantiationException the instantiation exception
	 */
	@SuppressWarnings("unchecked")
	private void getListFromCursor(Class<?> clazz, List<T> list, Cursor cursor) throws IllegalAccessException, InstantiationException {
		while (cursor.moveToNext()) {
			Object entity = clazz.newInstance();
			// 加载所有字段
			List<Field> allFields = TableHelper.joinFields(entity.getClass().getDeclaredFields(), entity.getClass().getSuperclass().getDeclaredFields());

			for (Field field : allFields) {
				Column column = null;
				if (field.isAnnotationPresent(Column.class)) {
					column = (Column) field.getAnnotation(Column.class);

					field.setAccessible(true);
					Class<?> fieldType = field.getType();

					int c = cursor.getColumnIndex(column.name());
					if (c < 0) {
						continue; // 如果不存则循环下个属性值
					} else if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
						field.set(entity, cursor.getInt(c));
					} else if (String.class == fieldType) {
						field.set(entity, cursor.getString(c));
					} else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
						field.set(entity, Long.valueOf(cursor.getLong(c)));
					} else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
						field.set(entity, Float.valueOf(cursor.getFloat(c)));
					} else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
						field.set(entity, Short.valueOf(cursor.getShort(c)));
					} else if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {
						field.set(entity, Double.valueOf(cursor.getDouble(c)));
					} else if (Date.class == fieldType) {// 处理java.util.Date类型,update2012-06-10
						Date date = new Date();
						date.setTime(cursor.getLong(c));
						field.set(entity, date);
					} else if (Blob.class == fieldType) {
						field.set(entity, cursor.getBlob(c));
					} else if (Character.TYPE == fieldType) {
						String fieldValue = cursor.getString(c);
						if ((fieldValue != null) && (fieldValue.length() > 0)) {
							field.set(entity, Character.valueOf(fieldValue.charAt(0)));
						}
					} else if ((Boolean.TYPE == fieldType) || (Boolean.class == fieldType)) {
						String temp = cursor.getString(c);
						if ("true".equals(temp) || "1".equals(temp)) {
							field.set(entity, true);
						} else {
							field.set(entity, false);
						}
					}

				}
			}

			list.add((T) entity);
		}
	}

	/**
	 * 插入实体
	 * 
	 * @param entity the entity
	 * @return the long
	 * @see com.pc.db.orm.dao.DbDBDao#insert(java.lang.Object)
	 */
	@Override
	public long insert(T entity) {
		return insert(entity, true);
	}

	/**
	 * 插入实体
	 * 
	 * @param entity the entity
	 * @param flag the flag
	 * @return the long
	 * @see com.pc.db.orm.dao.DbDBDao#insert(java.lang.Object,boolean)
	 */
	@SuppressWarnings({
			"unchecked", "unused"
	})
	@Override
	public long insert(T entity, boolean flag) {
		String sql = null;
		long row = 0L;
		try {
			lock.lock();
			ContentValues cv = new ContentValues();
			if (flag) {
				// id自增
				sql = setContentValues(entity, cv, TYPE_INCREMENT, METHOD_INSERT);
			} else {
				// id需指定
				sql = setContentValues(entity, cv, TYPE_NOT_INCREMENT, METHOD_INSERT);
			}

			if (PcLog.isPrint) {
				Log.d(TAG, "[insert]: insert into " + this.tableName + " " + sql);
			}

			row = db.insert(this.tableName, null, cv);

			// 获取关联域的操作类型和关系类型
			String foreignKey = null;
			String type = null;
			String action = null;
			// 需要判断是否有关联表
			for (Field relationsField : allFields) {
				if (!relationsField.isAnnotationPresent(Relations.class)) {
					continue;
				}

				Relations relations = (Relations) relationsField.getAnnotation(Relations.class);
				// 获取外键列名
				foreignKey = relations.foreignKey();
				// 关联类型
				type = relations.type();
				// 操作类型
				action = relations.action();
				// 设置可访问
				relationsField.setAccessible(true);

				if (!(action.indexOf(ActionType.insert) != -1)) {
					return row;
				}

				// 一对一关系
				if (RelationsType.one2one.equals(type)) {
					// 获取关联表的对象
					T relationsEntity = (T) relationsField.get(entity);
					if (relationsEntity != null) {
						ContentValues relationsCv = new ContentValues();
						if (flag) {
							// id自增
							sql = setContentValues(relationsEntity, relationsCv, TYPE_INCREMENT, METHOD_INSERT);
						} else {
							// id需指定
							sql = setContentValues(relationsEntity, relationsCv, TYPE_NOT_INCREMENT, METHOD_INSERT);
						}
						String relationsTableName = "";
						if (relationsEntity.getClass().isAnnotationPresent(Table.class)) {
							Table table = (Table) relationsEntity.getClass().getAnnotation(Table.class);
							relationsTableName = table.name();
						}
						if (PcLog.isPrint) {
							Log.d(TAG, "[insert]: insert into " + relationsTableName + " " + sql);
						}
						row += db.insert(relationsTableName, null, relationsCv);
					}
				}
				// 一对多关系
				else if (RelationsType.one2many.equals(type) || RelationsType.many2many.equals(type)) {
					// 获取关联表的对象
					List<T> list = (List<T>) relationsField.get(entity);
					if (list != null && list.size() > 0) {
						for (T relationsEntity : list) {
							ContentValues relationsCv = new ContentValues();
							if (flag) {
								// id自增
								sql = setContentValues(relationsEntity, relationsCv, TYPE_INCREMENT, METHOD_INSERT);
							} else {
								// id需指定
								sql = setContentValues(relationsEntity, relationsCv, TYPE_NOT_INCREMENT, METHOD_INSERT);
							}
							String relationsTableName = "";
							if (relationsEntity.getClass().isAnnotationPresent(Table.class)) {
								Table table = (Table) relationsEntity.getClass().getAnnotation(Table.class);
								relationsTableName = table.name();
							}

							if (PcLog.isPrint) {
								Log.d(TAG, "[insert]: insert into " + relationsTableName + " " + sql);
							}

							row += db.insert(relationsTableName, null, relationsCv);
						}
					}

				}
			}

		} catch (Exception e) {
			if (PcLog.isPrint) {
				android.util.Log.d(this.TAG, "[insert] into DB Exception.", e);
			}
			row = -1;
		} finally {
			lock.unlock();
		}
		return row;
	}

	/**
	 * 插入列表
	 * 
	 * @see com.pc.db.orm.dao.DbDBDao#insertList(java.util.List)
	 */
	@Override
	public long insertList(List<T> entityList) {
		return insertList(entityList, true);
	}

	/**
	 * 插入列表
	 * 
	 * @see com.pc.db.orm.dao.DbDBDao#insertList(java.util.List,boolean)
	 */
	@SuppressWarnings({
			"unused", "unchecked"
	})
	@Override
	public long insertList(List<T> entityList, boolean flag) {
		String sql = null;
		long rows = 0;
		try {
			lock.lock();
			for (T entity : entityList) {
				ContentValues cv = new ContentValues();
				if (flag) {
					// id自增
					sql = setContentValues(entity, cv, TYPE_INCREMENT, METHOD_INSERT);
				} else {
					// id需指定
					sql = setContentValues(entity, cv, TYPE_NOT_INCREMENT, METHOD_INSERT);
				}

				if (PcLog.isPrint) {
					Log.d(TAG, "[insertList]: insert into " + this.tableName + " " + sql);
				}

				rows += db.insert(this.tableName, null, cv);

				// 获取关联域的操作类型和关系类型
				String foreignKey = null;
				String type = null;
				String action = null;
				Field field = null;
				// 需要判断是否有关联表
				for (Field relationsField : allFields) {
					if (!relationsField.isAnnotationPresent(Relations.class)) {
						continue;
					}

					Relations relations = (Relations) relationsField.getAnnotation(Relations.class);
					// 获取外键列名
					foreignKey = relations.foreignKey();
					// 关联类型
					type = relations.type();
					// 操作类型
					action = relations.action();
					// 设置可访问
					relationsField.setAccessible(true);
					field = relationsField;
				}

				if (field == null) {
					continue;
				}

				if (!(action.indexOf(ActionType.insert) != -1)) {
					continue;
				}

				if (RelationsType.one2one.equals(type)) {
					// 一对一关系
					// 获取关联表的对象
					T relationsEntity = (T) field.get(entity);
					if (relationsEntity != null) {
						ContentValues relationsCv = new ContentValues();
						if (flag) {
							// id自增
							sql = setContentValues(relationsEntity, relationsCv, TYPE_INCREMENT, METHOD_INSERT);
						} else {
							// id需指定
							sql = setContentValues(relationsEntity, relationsCv, TYPE_NOT_INCREMENT, METHOD_INSERT);
						}
						String relationsTableName = "";
						if (relationsEntity.getClass().isAnnotationPresent(Table.class)) {
							Table table = (Table) relationsEntity.getClass().getAnnotation(Table.class);
							relationsTableName = table.name();
						}

						if (PcLog.isPrint) {
							Log.d(TAG, "[insertList]: insert into " + relationsTableName + " " + sql);
						}

						rows += db.insert(relationsTableName, null, relationsCv);
					}
				}
				// 一对多关系
				else if (RelationsType.one2many.equals(type) || RelationsType.many2many.equals(type)) {
					// 获取关联表的对象
					List<T> list = (List<T>) field.get(entity);
					if (list != null && list.size() > 0) {
						for (T relationsEntity : list) {
							ContentValues relationsCv = new ContentValues();
							if (flag) {
								// id自增
								sql = setContentValues(relationsEntity, relationsCv, TYPE_INCREMENT, METHOD_INSERT);
							} else {
								// id需指定
								sql = setContentValues(relationsEntity, relationsCv, TYPE_NOT_INCREMENT, METHOD_INSERT);
							}
							String relationsTableName = "";
							if (relationsEntity.getClass().isAnnotationPresent(Table.class)) {
								Table table = (Table) relationsEntity.getClass().getAnnotation(Table.class);
								relationsTableName = table.name();
							}

							if (PcLog.isPrint) {
								Log.d(TAG, "[insertList]: insert into " + relationsTableName + " " + sql);
							}

							rows += db.insert(relationsTableName, null, relationsCv);
						}
					}
				}
			}
		} catch (Exception e) {
			if (PcLog.isPrint) {
				android.util.Log.d(this.TAG, "[insertList] into DB Exception.", e);
			}
		} finally {
			lock.unlock();
		}

		return rows;
	}

	/**
	 * 按id删除
	 * 
	 * @param id the id
	 * @see com.pc.db.orm.dao.DbDBDao#delete(int)
	 */
	@Override
	public long delete(int id) {
		long rows = -1;
		try {
			lock.lock();
			String where = this.idColumn + " = ?";
			String[] whereValue = {
				Integer.toString(id)
			};

			if (PcLog.isPrint) {
				Log.d(TAG, "[delete]: delelte from " + this.tableName + " where " + where.replace("?", String.valueOf(id)));
			}

			rows = db.delete(this.tableName, where, whereValue);
		} catch (Exception e) {
			if (PcLog.isPrint) {
				android.util.Log.d(this.TAG, "[delete] Exception.", e);
			}
		} finally {
			lock.unlock();
		}
		return rows;
	}

	/**
	 * 按id删除
	 * 
	 * @param ids the ids
	 * @see com.pc.db.orm.dao.DbDBDao#delete(java.lang.Integer[])
	 */
	@Override
	public long delete(int... ids) {
		long rows = -1;
		if (null == ids || ids.length <= 0) {
			return rows;
		}

		for (int id : ids) {
			rows += delete(id);
		}

		return rows;
	}

	/**
	 * 按条件删除数据
	 * 
	 * @see com.pc.db.orm.dao.DbDBDao#delete(java.lang.String,java.lang.String[])
	 */
	@Override
	public long delete(String whereClause, String[] whereArgs) {
		long rows = -1;
		try {
			lock.lock();

			String mLogSql = getLogSql(whereClause, whereArgs);
			if (!StringUtils.isNull(mLogSql)) {
				mLogSql += " where ";
			}

			if (PcLog.isPrint) {
				Log.d(TAG, "[delete]: delete from " + this.tableName + mLogSql);
			}

			rows = db.delete(this.tableName, whereClause, whereArgs);
		} catch (Exception e) {
			if (PcLog.isPrint) {
				android.util.Log.d(this.TAG, "[delete] Exception.", e);
			}
		} finally {
			lock.unlock();
		}
		return rows;
	}

	/**
	 * 清空数据
	 * 
	 * @see com.pc.db.orm.dao.DbDBDao#deleteAll()
	 */
	@Override
	public long deleteAll() {
		long rows = -1;
		try {
			lock.lock();

			if (PcLog.isPrint) {
				Log.d(TAG, "[delete]: delete from " + this.tableName);
			}

			rows = db.delete(this.tableName, null, null);
		} catch (Exception e) {
			if (PcLog.isPrint) {
				android.util.Log.d(this.TAG, "[deleteAll] Exception.", e);
			}
		} finally {
			lock.unlock();
		}
		return rows;
	}

	/**
	 * 更新实体
	 * 
	 * @param entity the entity
	 * @return the long
	 * @see com.pc.db.orm.dao.DbDBDao#update(java.lang.Object)
	 */
	@Override
	public long update(T entity) {
		long row = 0;
		try {
			lock.lock();
			ContentValues cv = new ContentValues();

			// 注意返回的sql中包含主键列
			String sql = setContentValues(entity, cv, TYPE_NOT_INCREMENT, METHOD_UPDATE);

			String where = this.idColumn + " = ?";
			int id = Integer.parseInt(cv.get(this.idColumn).toString());
			// set sql中不能包含主键列
			cv.remove(this.idColumn);

			if (PcLog.isPrint) {
				Log.d(TAG, "[update]: update " + this.tableName + " set " + sql + " where " + where.replace("?", String.valueOf(id)));
			}

			row = db.update(this.tableName, cv, where, new String[] {
				Integer.toString(id)
			});
		} catch (Exception e) {
			if (PcLog.isPrint) {
				android.util.Log.d(this.TAG, "[update] DB Exception.", e);
			}
		} finally {
			lock.unlock();
		}
		return row;
	}

	@Override
	public long update(T entity, String whereClause, String[] whereArgs) {
		long row = 0;

		if (StringUtils.isNull(whereClause) || whereArgs == null || whereArgs.length == 0) {
			return update(entity);
		}

		try {
			lock.lock();
			ContentValues cv = new ContentValues();

			// 注意返回的sql中包含主键列
			setContentValues(entity, cv, TYPE_NOT_INCREMENT, METHOD_UPDATE);
			// set sql中不能包含主键列
			cv.remove(this.idColumn);

			// ContentValues不包含Where中的字段
			List<Field> allFields = TableHelper.joinFields(entity.getClass().getDeclaredFields(), entity.getClass().getSuperclass().getDeclaredFields());
			for (Field field : allFields) {
				if (!field.isAnnotationPresent(Column.class)) {
					continue;
				}
				Column column = (Column) field.getAnnotation(Column.class);
				if (whereClause.contains(column.name())) {
					cv.remove(column.name());
				}
			}
			row = db.update(this.tableName, cv, whereClause, whereArgs);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
		return row;
	}

	/**
	 * @see com.pc.db.orm.dao.DbDBDao#update(android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public long update(ContentValues contentValues, String whereClause, String[] whereArgs) {
		if (null == contentValues) {
			return 0;
		}

		long row = 0;

		try {
			lock.lock();
			contentValues.remove(this.idColumn);
			row = db.update(this.tableName, contentValues, whereClause, whereArgs);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}

		return row;
	}

	@Override
	public long update(List<T> entityList, String whereClause, String[] whereArgs) {
		long row = 0;

		if (StringUtils.isNull(whereClause) || whereArgs == null || whereArgs.length == 0) {
			return updateList(entityList);
		}

		try {
			lock.lock();

			for (T entity : entityList) {
				ContentValues cv = new ContentValues();

				// 注意返回的sql中包含主键列
				setContentValues(entity, cv, TYPE_NOT_INCREMENT, METHOD_UPDATE);
				// set sql中不能包含主键列
				cv.remove(this.idColumn);

				// ContentValues不包含Where中的字段
				List<Field> allFields = TableHelper.joinFields(entity.getClass().getDeclaredFields(), entity.getClass().getSuperclass().getDeclaredFields());
				for (Field field : allFields) {
					if (!field.isAnnotationPresent(Column.class)) {
						continue;
					}
					Column column = (Column) field.getAnnotation(Column.class);
					if (whereClause.contains(column.name())) {
						cv.remove(column.name());
					}
				}
				row += db.update(this.tableName, cv, whereClause, whereArgs);
			}
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}

		return row;
	}

	/**
	 * 更新列表
	 * 
	 * @see com.pc.db.orm.dao.DbDBDao#updateList(java.util.List)
	 */
	@Override
	public long updateList(List<T> entityList) {
		String sql = null;
		long row = 0;
		try {
			lock.lock();

			for (T entity : entityList) {
				ContentValues cv = new ContentValues();

				sql = setContentValues(entity, cv, TYPE_NOT_INCREMENT, METHOD_UPDATE);

				String where = this.idColumn + " = ?";
				int id = Integer.parseInt(cv.get(this.idColumn).toString());
				cv.remove(this.idColumn);

				if (PcLog.isPrint) {
					Log.d(TAG, "[update]: update " + this.tableName + " set " + sql + " where " + where.replace("?", String.valueOf(id)));
				}

				String[] whereValue = {
					Integer.toString(id)
				};
				row += db.update(this.tableName, cv, where, whereValue);
			}
		} catch (Exception e) {
			if (PcLog.isPrint) {
				android.util.Log.d(this.TAG, "[update] DB Exception.", e);
			}
		} finally {
			lock.unlock();
		}

		return row;
	}

	/**
	 * 设置这个ContentValues
	 * 
	 * @param entity 映射实体
	 * @param cv the cv
	 * @param type id类的类型，是否自增
	 * @param method 预执行的操作
	 * @return sql的字符串
	 * @throws IllegalAccessException the illegal access exception
	 */
	private String setContentValues(T entity, ContentValues cv, int type, int method) throws IllegalAccessException {
		StringBuffer strField = new StringBuffer("(");
		StringBuffer strValue = new StringBuffer(" values(");
		StringBuffer strUpdate = new StringBuffer(" ");

		// 加载所有字段
		List<Field> allFields = TableHelper.joinFields(entity.getClass().getDeclaredFields(), entity.getClass().getSuperclass().getDeclaredFields());
		for (Field field : allFields) {
			if (!field.isAnnotationPresent(Column.class)) {
				continue;
			}
			Column column = (Column) field.getAnnotation(Column.class);

			field.setAccessible(true);
			Object fieldValue = field.get(entity);
			if (fieldValue == null) continue;
			if ((type == TYPE_INCREMENT) && (field.isAnnotationPresent(Id.class))) {
				continue;
			}

			// 处理java.util.Date类型,update
			if (Date.class == field.getType()) {
				cv.put(column.name(), ((Date) fieldValue).getTime());
				continue;
			}

			String value = String.valueOf(fieldValue);
			cv.put(column.name(), value);
			if (method == METHOD_INSERT) {
				strField.append(column.name()).append(",");
				strValue.append("'").append(value).append("',");
			} else {
				strUpdate.append(column.name()).append("=").append("'").append(value).append("',");
			}

		}
		if (method == METHOD_INSERT) {
			strField.deleteCharAt(strField.length() - 1).append(")");
			strValue.deleteCharAt(strValue.length() - 1).append(")");
			return strField.toString() + strValue.toString();
		} else {
			return strUpdate.deleteCharAt(strUpdate.length() - 1).append(" ").toString();
		}
	}

	/**
	 * 查询为map列表
	 * 
	 * @param sql the sql
	 * @param selectionArgs the selection args
	 * @return the list
	 * @see com.pc.db.orm.dao.DbDBDao#queryMapList(java.lang.String,java.lang.String[])
	 */
	@Override
	public List<Map<String, String>> queryMapList(String sql, String[] selectionArgs) {
		Cursor cursor = null;
		List<Map<String, String>> retList = new ArrayList<Map<String, String>>();
		try {
			lock.lock();

			if (PcLog.isPrint) {
				Log.d(TAG, "[queryMapList]: " + getLogSql(sql, selectionArgs));
			}

			cursor = db.rawQuery(sql, selectionArgs);
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (String columnName : cursor.getColumnNames()) {
					int c = cursor.getColumnIndex(columnName);
					if (c < 0) {
						continue; // 如果不存在循环下个属性值
					} else {
						map.put(columnName.toLowerCase(), cursor.getString(c));
					}
				}
				retList.add(map);
			}
		} catch (Exception e) {
			if (PcLog.isPrint) {
				android.util.Log.d(this.TAG, "[queryMapList] from DB exception", e);
			}
		} finally {
			closeCursor(cursor);
			lock.unlock();
		}

		return retList;
	}

	/**
	 * 查询数量
	 * 
	 * @param sql the sql
	 * @param selectionArgs the selection args
	 * @return the int
	 * @see com.pc.db.orm.dao.DbDBDao#queryCount(java.lang.String,java.lang.String[])
	 */
	@Override
	public int queryCount(String sql, String[] selectionArgs) {
		Cursor cursor = null;
		int count = 0;
		try {
			lock.lock();

			if (PcLog.isPrint) {
				Log.d(TAG, "[queryCount]: " + getLogSql(sql, selectionArgs));
			}

			cursor = db.query(this.tableName, null, sql, selectionArgs, null, null, null);
			if (cursor != null) {
				count = cursor.getCount();
			}
		} catch (Exception e) {
			if (PcLog.isPrint) {
				android.util.Log.e(TAG, "[queryCount] from DB exception");
			} else {
				e.printStackTrace();
			}
		} finally {
			closeCursor(cursor);
			lock.unlock();
		}

		return count;
	}

	/**
	 * 执行特定的sql
	 * 
	 * @param sql the sql
	 * @param selectionArgs the selection args
	 * @see com.pc.db.orm.dao.DbDBDao#execSql(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void execSql(String sql, Object[] selectionArgs) {
		try {
			lock.lock();

			if (PcLog.isPrint) {
				Log.d(TAG, "[execSql]: " + getLogSql(sql, selectionArgs));
			}
			if (selectionArgs == null) {
				db.execSQL(sql);
			} else {
				db.execSQL(sql, selectionArgs);
			}
		} catch (Exception e) {
			if (PcLog.isPrint) {
				android.util.Log.e(TAG, "[execSql] DB exception.");
			} else {
				e.printStackTrace();
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 获取写数据库，数据操作前必须调用
	 * 
	 * @param transaction 是否开启事务
	 * @throws
	 */
	public void startWritableDatabase(boolean transaction) {
		try {
			lock.lock();
			if (db == null || !db.isOpen()) {
				db = this.dbHelper.getWritableDatabase();
			}
			if (db != null && transaction) {
				db.beginTransaction();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

	}

	/**
	 * 获取读数据库，数据操作前必须调用
	 * 
	 * @param transaction 是否开启事务
	 * @throws
	 */
	public void startReadableDatabase(boolean transaction) {
		try {
			lock.lock();
			if (db == null || !db.isOpen()) {
				db = this.dbHelper.getReadableDatabase();
			}

			if (db != null && transaction) {
				db.beginTransaction();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

	}

	/**
	 * 操作完成后设置事务成功后才能调用closeDatabase(true);
	 * 
	 * @throws
	 */
	public void setTransactionSuccessful() {
		try {
			lock.lock();
			if (db != null) {
				db.setTransactionSuccessful();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

	}

	/**
	 * 关闭数据库，数据操作后必须调用
	 * 
	 * @param transaction 关闭事务
	 * @throws
	 */
	public void closeDatabase(boolean transaction) {
		try {
			lock.lock();
			if (db != null) {
				if (transaction) {
					db.endTransaction();
				}
				if (db.isOpen()) {
					db.close();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 打印当前sql语句
	 * 
	 * @param sql sql语句，带?
	 * @param args 绑定变量
	 * @return 完整的sql
	 */
	private String getLogSql(String sql, Object[] args) {
		if (args == null || args.length == 0) {
			return sql;
		}
		for (int i = 0; i < args.length; i++) {
			sql = sql.replaceFirst("\\?", "'" + String.valueOf(args[i]) + "'");
		}

		return sql;
	}

	/**
	 * 保存数据
	 *
	 * @param entity
	 * @return
	 */
	public long saveData(T entity) {
		return saveData(entity, false);
	}

	/**
	 * 保存数据
	 *
	 * @param entity
	 * @param transaction 是否开启事务
	 * @return
	 */
	public long saveData(T entity, boolean transaction) {
		if (null == entity) {
			return 0;
		}

		// (1)获取数据库
		this.startWritableDatabase(transaction);
		// (2)执行查询
		long id = this.insert(entity);
		// (3)关闭数据库
		this.closeDatabase(transaction);

		return id;
	}

	/**
	 * 更新数据 描述
	 *
	 * @param entity
	 * @return
	 */
	public long updateData(T entity) {
		return updateData(entity, false);
	}

	/**
	 * 更新数据
	 *
	 * @param entity
	 * @param transaction 是否开启事务
	 * @return
	 */
	public long updateData(T entity, boolean transaction) {
		if (null == entity) {
			return 0;
		}

		// (1)获取写数据库
		this.startWritableDatabase(transaction);

		// (2)更新
		long row = this.update(entity);

		// (3)关闭数据库
		this.closeDatabase(transaction);

		return row;
	}

	/**
	 * 更新数据
	 *
	 * @param entity
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public long updateData(T entity, String whereClause, String[] whereArgs) {
		return updateData(entity, whereClause, whereArgs, false);
	}

	/**
	 * 更新数据
	 *
	 * @param entity
	 * @param whereClause
	 * @param whereArgs
	 * @param transaction
	 * @return
	 */
	public long updateData(T entity, String whereClause, String[] whereArgs, boolean transaction) {
		if (null == entity) {
			return 0;
		}

		// (1)获取写数据库
		this.startWritableDatabase(transaction);

		// (2)更新
		long row = this.update(entity, whereClause, whereArgs);

		// (3)关闭数据库
		this.closeDatabase(transaction);

		return row;
	}

	/**
	 * 更新数据
	 *
	 * @param contentValues
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public long updateData(ContentValues contentValues, String whereClause, String[] whereArgs) {
		return updateData(contentValues, whereClause, whereArgs, false);
	}

	/**
	 * 更新数据
	 *
	 * @param contentValues
	 * @param whereClause
	 * @param whereArgs
	 * @param transaction
	 * @return
	 */
	public long updateData(ContentValues contentValues, String whereClause, String[] whereArgs, boolean transaction) {
		if (null == contentValues || StringUtils.isNull(whereClause) || whereArgs == null || whereArgs.length == 0) {
			return 0;
		}

		// (1)获取写数据库
		this.startWritableDatabase(transaction);

		// (2)更新
		long row = this.update(contentValues, whereClause, whereArgs);

		// (3)关闭数据库
		this.closeDatabase(transaction);

		return row;
	}

	/**
	 * 更新或者保存数据
	 *
	 * @param entity
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public long updateOrSaveData(T entity, String whereClause, String[] whereArgs) {
		return updateOrSaveData(entity, whereClause, whereArgs, false);
	}

	/**
	 * 更新或者保存数据
	 *
	 * @param entity
	 * @param whereClause
	 * @param whereArgs
	 * @param transaction
	 * @return
	 */
	public long updateOrSaveData(T entity, String whereClause, String[] whereArgs, boolean transaction) {
		if (null == entity) {
			return 0;
		}

		// (1)获取写数据库
		this.startWritableDatabase(transaction);

		long row = 0;

		// (2)更新/插入
		if (exists(whereClause, whereArgs)) {
			row = this.update(entity, whereClause, whereArgs);
		} else {
			row = insert(entity);
		}
		// (3)关闭数据库
		this.closeDatabase(transaction);

		return row;
	}

	/**
	 * 根据ID查询数据
	 * 
	 * @param id
	 * @return
	 */
	public T queryDataById(int id) {
		return queryDataById(id, false);
	}

	/**
	 * 根据ID查询数据
	 * 
	 * @param id
	 * @param transaction 是否开启事务
	 * @return
	 */
	public T queryDataById(int id, boolean transaction) {
		// (1)获取读数据库
		this.startReadableDatabase(transaction);

		// (2)查询
		T t = (T) this.queryOne(id);

		// (3)关闭数据库
		this.closeDatabase(transaction);

		return t;
	}

	/**
	 * 查询一条数据
	 *
	 * @param selection
	 * @param selectionArgs
	 * @return
	 */
	public T queryDataOne(String selection, String[] selectionArgs) {
		return queryDataOne(selection, selectionArgs, false);
	}

	/**
	 * 查询一条数据
	 *
	 * @param selection
	 * @param selectionArgs
	 * @param transaction
	 * @return
	 */
	public T queryDataOne(String selection, String[] selectionArgs, boolean transaction) {
		// (1)获取读数据库
		this.startReadableDatabase(transaction);

		// (2)查询
		List<T> list = queryList(null, selection, selectionArgs, null, null, null, null);

		// (3)关闭数据库
		this.closeDatabase(transaction);

		if (null != list && list.size() > 0) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * 所有数据
	 */
	public List<T> queryData() {
		return queryData(false);
	}

	/**
	 * 所有数据
	 * 
	 * @param transaction 是否开启事务
	 */
	public List<T> queryData(boolean transaction) {
		// (1)获取数据库
		this.startReadableDatabase(transaction);
		// (2)执行查询
		List<T> list = this.queryList();
		// (3)关闭数据库
		this.closeDatabase(transaction);

		return list;
	}

	/**
	 * 查询数量
	 * 
	 */
	public int queryDataCount() {
		return queryDataCount(false);
	}

	/**
	 * 查询数量
	 * 
	 * @param transaction 是否开启事务
	 */
	public int queryDataCount(boolean transaction) {
		// (1)获取数据库
		this.startReadableDatabase(transaction);

		// (2)执行查询
		int totalCount = this.queryCount(null, null);

		// (3)关闭数据库
		this.closeDatabase(transaction);

		return totalCount;
	}

	/**
	 * 删除数据
	 * 
	 * @param id
	 */
	public long delData(int id) {
		return delData(id, false);
	}

	/**
	 * 删除数据
	 * 
	 * @param id
	 * @param transaction 是否开启事务
	 */
	public long delData(int id, boolean transaction) {
		// (1)获取数据库
		this.startWritableDatabase(transaction);
		// (2)执行查询
		long row = this.delete(id);
		// (3)关闭数据库
		this.closeDatabase(transaction);

		return row;
	}

	/**
	 * 删除数据
	 * 
	 * @param ids
	 * @return
	 */
	public long delData(int... ids) {
		if (null == ids || ids.length == 0) {
			return -1;
		}
		// (1)获取数据库
		this.startWritableDatabase(false);
		// (2)执行查询
		long row = this.delete(ids);
		// (3)关闭数据库
		this.closeDatabase(false);

		return row;
	}

	/**
	 * 删除数据
	 *
	 * @param transaction 是否开启事务
	 * @param ids
	 * @return
	 */
	public long delData(boolean transaction, int... ids) {
		if (null == ids || ids.length == 0) {
			return -1;
		}
		// (1)获取数据库
		this.startWritableDatabase(transaction);
		// (2)执行查询
		long row = this.delete(ids);
		// (3)关闭数据库
		this.closeDatabase(transaction);

		return row;
	}

	/**
	 * 清空数据
	 *
	 * @return
	 */
	public long cleanupData() {
		return cleanupData(false);
	}

	/**
	 * 清空数据
	 *
	 * @param transaction
	 * @return
	 */
	public long cleanupData(boolean transaction) {
		// (1)获取数据库
		this.startWritableDatabase(transaction);
		// (2)执行查询
		long row = this.deleteAll();
		// (3)关闭数据库
		this.closeDatabase(transaction);

		return row;
	}

}
