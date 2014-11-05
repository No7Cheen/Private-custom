package com.pc.utils.database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.pc.utils.StringUtils;

public class PcSqlLiteTemp {

	public static boolean operate(SQLiteDatabase myDatabase, Collection params, String sql) throws Exception {
		if (myDatabase == null) {
			throw new Exception("Database is null");
		}
		return operate(myDatabase, params, sql, false);
	}

	@SuppressWarnings("rawtypes")
	public static boolean operate(SQLiteDatabase myDatabase, Collection params, String sql, boolean isInsert)
			throws Exception {
		if (myDatabase == null) {
			throw new Exception("Database is null");
		}
		if (myDatabase.isReadOnly()) {
			throw new Exception("Database is ReadOnly");
		}

		boolean b = false;
		if (params == null || StringUtils.isNull(sql)) {
			return b;
		}

		// Compiles an SQL statement into a reusable pre-compiled statement
		// object.
		SQLiteStatement statement = myDatabase.compileStatement(sql);
		try {
			PcSqlLiteUtil.setStatement(params, statement);
			if (isInsert) {
				// Execute this SQL statement and return the ID of the row
				// inserted due to this call.
				long resultRowID = statement.executeInsert();
				if (resultRowID > 0) {
					b = true;
				} else {
					b = false;
				}
			} else {
				statement.execute();
				b = true;
			}
		} catch (Exception e) {
			Log.w("exception: ", "sql: " + sql + " params: " + params, e);
		} finally {
			statement.close();
		}

		return b;
	}

	public static boolean transaction(SQLiteDatabase myDatabase, List<PcOperatorUtil> operators) throws Exception {
		if (myDatabase == null) {
			throw new Exception("Database is null");
		}
		boolean b = false;
		if (operators != null) {
			ArrayList<String> sqls = new ArrayList<String>();
			ArrayList<List<String>> params = new ArrayList<List<String>>();

			for (PcOperatorUtil operator : operators) {
				List<String> param = new ArrayList<String>();
				String sql = operator.genSql(param);
				params.add(param);
				sqls.add(sql);
			}
			b = executeAsATransaction(myDatabase, params, sqls);
		}
		return b;
	}

	public static boolean executeAsATransaction(SQLiteDatabase myDatabase, ArrayList params, ArrayList sqls)
			throws Exception {
		if (myDatabase == null) {
			throw new Exception("Database is null");
		}
		boolean b = false;
		if (myDatabase == null) {
			throw new Exception("database is null");
		}
		if (params == null || sqls == null || params.size() != sqls.size()) {
			return b;
		}
		myDatabase.beginTransaction();

		try {
			for (int i = 0; i < sqls.size(); i++) {
				Collection param = (Collection) params.get(i);
				String sql = (String) sqls.get(i);
				SQLiteStatement statement = myDatabase.compileStatement(sql);
				PcSqlLiteUtil.setStatement(param, statement);
				if (sql.startsWith("INSERT")) {
					statement.executeInsert();
				} else {
					statement.execute();
				}
				statement.close();
			}
			myDatabase.setTransactionSuccessful();
			b = true;
		} catch (Exception e) {
			Log.w("SQLiteTemp", "exception:", e);
		} finally {
			myDatabase.endTransaction();
		}
		return b;
	}

	public static void executeAsATransaction(SQLiteDatabase myDatabase, Runnable actions) {

		if (myDatabase == null) return;
		myDatabase.beginTransaction();
		try {
			actions.run();
			myDatabase.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			myDatabase.endTransaction();
		}
	}

	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	public static List query(SQLiteDatabase myDatabase, Class cls, String[] queryParams, String sqlquery)
			throws Exception {
		if (myDatabase == null) {
			throw new Exception("Database is null");
		}

		if (cls == null || StringUtils.isNull(sqlquery)) {
			return null;
		}

		List list = new ArrayList();
		final Cursor cursor = myDatabase.rawQuery(sqlquery, queryParams);
		if (cursor == null) {
			return list;
		}
		while (cursor.moveToNext()) {
			list.add(PcSqlLiteUtil.extract(cls, cursor));
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return list;
	}

	public static Object querySingle(SQLiteDatabase myDatabase, Class cls, String[] queryParams, String sqlquery)
			throws Exception {
		Object o = null;
		if (myDatabase == null) {
			throw new Exception("database is null");
		}
		if (cls == null) {
			return null;
		}
		if (StringUtils.isNull(sqlquery)) {
			return null;
		}

		final Cursor cursor = myDatabase.rawQuery(sqlquery, queryParams);
		if (cursor != null && cursor.moveToNext()) {
			o = PcSqlLiteUtil.extract(cls, cursor);
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return o;
	}

	// public static PageIterator splitPageQuery(SQLiteDatabase myDatabase,
	// Class cls, String[] queryParams, int iPageNumber, int iRowNumber,
	// String countSqlquery, String sqlquery) throws Exception {
	// PageIterator page = null;
	// if (myDatabase == null)
	// throw new Exception("database is null");
	// if (cls == null)
	// return null;
	// if (queryParams == null || StringUtils.isNull(sqlquery))
	// return null;
	// page = new PageIterator();
	// Cursor cursor = null;
	// try {
	// cursor = myDatabase.rawQuery(countSqlquery, queryParams);
	// cursor.moveToFirst();
	// page.setAllCount(cursor.getInt(0));
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// if (cursor != null)
	// cursor.close();
	// }
	//
	// // String[] params = XNoteArrayUtils.createCopy(queryParams,
	// // queryParams.length, queryParams.length + 2);
	// // params[params.length - 2] = (iPageNumber * iRowNumber) + "";
	// // params[params.length - 1] = iRowNumber + "";
	// int startIndex = (iPageNumber - 1) * iRowNumber;
	// sqlquery += " limit " + iRowNumber + " offset " + startIndex;
	//
	// try {
	// cursor = myDatabase.rawQuery(sqlquery, queryParams);
	//
	// List list = new ArrayList();
	// while (cursor.moveToNext()) {
	// list.add(SqlLiteUtil.extract(cls, cursor));
	// }
	// page.setData(list);
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// if (cursor != null)
	// cursor.close();
	// }
	//
	// return page;
	// }

}
