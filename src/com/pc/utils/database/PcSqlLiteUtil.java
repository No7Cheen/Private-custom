package com.pc.utils.database;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.pc.utils.reflect.FieldUtil;
import com.pc.utils.reflect.FieldUtils;
import com.pc.utils.reflect.PcBSBeanUtils;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class PcSqlLiteUtil {

	@SuppressWarnings("rawtypes")
	public static void setStatement(Collection queryParams, SQLiteStatement ps) throws Exception {
		if ((queryParams == null) || (queryParams.size() == 0)) return;

		int i = 1;
		Object key = null;

		Iterator iter = queryParams.iterator();
		while (iter.hasNext()) {
			key = iter.next();
			if (key != null) {
				convertType(i, key, ps);
			} else {
				ps.bindString(i, "");
			}

			i++;
		}
	}

	/**
	 * 转换Type
	 * @param i
	 * @param key
	 * @param ps
	 */
	private static void convertType(int i, Object key, SQLiteStatement ps) {
		if (key == null) {
			ps.bindNull(i);
			return;
		}

		// Bind a Type value to this statement.
		try {
			if (key instanceof java.lang.String) {
				String keyStrs = (String) key;
				ps.bindString(i, keyStrs);
			} else if (key instanceof Integer) {
				ps.bindLong(i, ((Integer) key).intValue());
			} else if (key instanceof Float) {
				ps.bindDouble(i, ((Float) key).floatValue());
			} else if (key instanceof Double) {
				ps.bindDouble(i, ((Double) key).doubleValue());
			} else if (key instanceof Long) {
				ps.bindLong(i, ((Long) key).longValue());
			} else if (key instanceof Boolean) {
				boolean b = ((Boolean) key).booleanValue();
				if (b) {
					ps.bindLong(i, 1);
				} else {
					ps.bindLong(i, 0);
				}
			} else if (key instanceof Date) {
				ps.bindLong(i, ((Date) key).getTime());
			} else {

			}
		} catch (Exception e) {
			Log.i("DB", "[Framework]setQueryParams error " + e + "in parameter order=" + i + " its value=" + key);
		}
	}

	private static Object convertType_(int i, Object key, Cursor ps) {
		if (key == null || ps == null) {
			return null;
		}
		Log.i("convertType", "key=" + key);

		try {
			if (key instanceof java.lang.String) {
				return ps.getString(i);
			} else if (key instanceof Integer) {
				return ps.getInt(i);
			} else if (key instanceof Float) {
				return ps.getFloat(i);
			} else if (key instanceof Double) {
				return ps.getDouble(i);
			} else if (key instanceof Long) {
				return ps.getLong(i);
			} else if (key instanceof Boolean) {
				Boolean b = false;
				String r = ps.getString(i);
				if (r != null && r.length() > 0
						&& (r.equalsIgnoreCase("true") || r.equalsIgnoreCase("yes") || r.equalsIgnoreCase("1"))) {
					b = true;
				}
				return b;
			} else if (key instanceof Date) {
				return new Date(ps.getLong(i));
			} else {

			}
		} catch (Exception e) {
			Log.i("DB", "[Framework]setQueryParams error " + e + "in parameter order=" + i + " its value=" + key);

		}

		return null;
	}

	private static Object convertType(int i, Class classType, Cursor ps) {
		if (classType == null || ps == null) {
			return null;
		}
		try {
			if (classType.getName().equals(FieldUtil.TYPE_STRING)) {
				return ps.getString(i);
			} else if (classType.getName().equals(FieldUtil.TYPE_INTEGER) || classType.equals(Integer.TYPE)) {
				return ps.getInt(i);
			} else if (classType.getName().equals(FieldUtil.TYPE_LONG) || classType.equals(Long.TYPE)) {
				return ps.getLong(i);
			} else if (classType.getName().equals(FieldUtil.TYPE_FLOAT) || classType.equals(Float.TYPE)) {
				return ps.getFloat(i);
			} else if (classType.getName().equals(FieldUtil.TYPE_DOUBLE) || classType.equals(Double.TYPE)) {
				return ps.getDouble(i);
			} else if (classType.getName().equals(FieldUtil.TYPE_BOOLEAN) || classType.equals(Boolean.TYPE)) {
				Boolean b = false;
				String r = ps.getString(i);
				if (r != null && r.length() > 0
						&& (r.equalsIgnoreCase("true") || r.equalsIgnoreCase("yes") || r.equalsIgnoreCase("1"))) {
					b = true;
				}
				return b;
			} else if (classType.getName().equals(FieldUtil.TYPE_DATE)) {
				return new Date(ps.getLong(i));
			}

		} catch (Exception e) {
			Log.i("DB", "[Framework]setQueryParams error " + e + "in parameter order=" + i + " its value=" + classType);

		}

		return null;
	}

	public static Object extract(Class cls, Cursor rs) throws Exception {
		if (cls == null || rs == null) return null;
		String[] names = rs.getColumnNames();
		int i = 0;
		Object o = cls.newInstance();
		for (String columName : names) {

			Field field = null;
			try {
				field = PcBSBeanUtils.getDeclaredField(o, columName);
			} catch (Exception e1) {
				continue;

			}
			try {

				Object v = convertType(i, field.getType(), rs);
				FieldUtils.setFieldValue(o, columName, v);
			} catch (Exception e) {
				e.printStackTrace();
				// buf.append(e.getMessage() + "\n");
			}
			i++;
		}

		return o;
	}

}
