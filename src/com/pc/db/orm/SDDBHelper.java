package com.pc.db.orm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * The Class AbSDDBHelper.
 */
public class SDDBHelper extends SDSQLiteOpenHelper {

	/** The model classes */
	private Class<?>[] modelClasses;

	/**
	 * @param context 应用context
	 * @param path 要放到SDCard下的文件夹路径
	 * @param databaseName 数据库文件名
	 * @param factory 数据库查询的游标工厂
	 * @param databaseVersion 数据库的新版本号
	 * @param modelClasses 要初始化的表的对象
	 */
	public SDDBHelper(Context context, String path, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion, Class<?>[] modelClasses) {
		super(context, path, databaseName, null, databaseVersion);
		this.modelClasses = modelClasses;

	}

	/**
	 * 表的创建
	 * 
	 * @param db 数据库对象
	 * @see com.SDSQLiteOpenHelper.ab.db.orm.AbSDSQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	public void onCreate(SQLiteDatabase db) {
		TableHelper.createTablesByClasses(db, this.modelClasses);
	}

	/**
	 * 表的重建
	 * 
	 * @param db 数据库对象
	 * @param oldVersion 旧版本号
	 * @param newVersion 新版本号
	 * @see com.SDSQLiteOpenHelper.ab.db.orm.AbSDSQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase,
	 *      int, int)
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		TableHelper.dropTablesByClasses(db, this.modelClasses);
		onCreate(db);
	}
}
