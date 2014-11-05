package com.pc.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库操作类
 */
public class MyDBHelper extends SQLiteOpenHelper {

	/** The Constant DBNAME. */
	private static final String DBNAME = "pcbase.db";

	/** The Constant VERSION. */
	private static final int VERSION = 1;

	/**
	 * @param context the context
	 */
	public MyDBHelper(Context context) {
		super(context, DBNAME, null, VERSION);
	}

	/**
	 * 表的创建
	 * @param db the db
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS filedown (_id integer primary key autoincrement,icon text,name text,description text, pakagename text ,downurl text,downpath text,state integer,downlength integer,totallength integer,downsuffix text)");
	}

	/**
	 * 表的重建
	 * @param db the db
	 * @param oldVersion the old version
	 * @param newVersion the new version
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase,
	 *      int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS filedown");
		onCreate(db);
	}

}
