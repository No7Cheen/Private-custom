package com.pc.utils.database;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.database.sqlite.SQLiteDatabase;

import com.pc.utils.StringUtils;

/**
 * UPDATE "tableName" SET "columName1" = [newValue] WHERE {条件1 AND 条件2}
 * @author ChenJian
 */
public class PcUpdateUtil extends PcOperatorUtil {

	private Map<String, String> updateParamMap;
	private List paramList;

	private Map<String, String> whereParamMap;
	private List paramList2;

	private String dateFormat = "yyyy-MM-dd HH:mm:ss";
	private String target = "MYSQL";

	private Class cls;

	private String keyField;
	private String tableName;

	private Object bean;
	private List<Object> paramsList;
	private List whereParamsList;
	private StringBuffer sqlBuf;
	private StringBuffer whereBuf;

	@SuppressWarnings("rawtypes")
	public PcUpdateUtil(Class cls) throws Exception {
		if (cls == null) {
			throw new Exception("class is null");
		}
		this.cls = cls;

		bean = cls.newInstance();
		tableName = getTableName(bean);

		if (StringUtils.isNull(tableName)) {
			throw new Exception("table is null");
		}
	}

	@SuppressWarnings("rawtypes")
	public void addUpdateParam(String columName, String value) throws Exception {
		if (StringUtils.isNull(columName)) {
			return;
		}

		if (StringUtils.isNULL(value)) {
			value = "";
		}

		if (paramsList == null) {
			paramsList = new ArrayList<Object>();
		}
		if (sqlBuf == null) {
			sqlBuf = new StringBuffer();
		}
		// columName = columName.toLowerCase();
		if (paramsList.contains(columName)) {
			return;
		}

		// Field field = cls.getField(columName);
		Field field = cls.getDeclaredField(columName);
		if (field != null) {
			Class classType = field.getType();
			String typeString = classType.getName();
			sqlBuf.append(columName + "=" + setFieldValue(typeString) + ",");
			Object object = setFieldType(classType, value);
			paramsList.add(object);
		}
	}

	public void addWhereParam(String columName, String value) throws Exception {
		addWhereParam(columName, "=", value);
	}

	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	public void addWhereParam(String columName, String operator, String value) throws Exception {
		if (StringUtils.isNull(columName) || StringUtils.isNull(operator) || StringUtils.isNULL(value))
			throw new Exception("illegal params");
		if (!validOperator(operator)) {
			throw new Exception("illegal operator");
		}

		if (whereParamsList == null) {
			whereParamsList = new ArrayList();
		}
		if (whereBuf == null) {
			whereBuf = new StringBuffer();
		}

		// columName = columName.toLowerCase();
		// Field field = cls.getField(columName);
		Field field = cls.getDeclaredField(columName);
		if (field != null) {
			Class classType = field.getType();
			String typeString = classType.getName();
			String ss = columName + operator + setFieldValue(typeString) + ",";
			if (operator.equals("in")) {
				ss = columName + " " + operator + " (" + value + "),";
			} else if (operator.equals("like")) {
				ss = columName + " " + operator + " ?,";
				value = "%" + value + "%";
			}
			if (!operator.equals("in")) {
				whereParamsList.add(setFieldType(classType, value));
			}

			whereBuf.append(ss);
		}

	}

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	public String genSql(List param) throws Exception {
		if (paramsList == null || paramsList.isEmpty() || paramsList.size() == 0 || sqlBuf == null
				|| sqlBuf.length() == 0 || whereBuf == null || whereBuf.length() == 0 || whereParamsList == null
				|| whereParamsList.size() == 0 || StringUtils.isNull(tableName)) return null;

		String sql = "";
		String sql1 = sqlBuf.toString();
		if (sql1.endsWith(",")) sql1 = sql1.substring(0, sql1.length() - 1);
		sql = "UPDATE " + tableName + " SET " + sql1;

		String sql2 = whereBuf.toString();
		if (sql2.endsWith(",")) {
			sql2 = sql2.substring(0, sql2.length() - 1);
		}

		sql += " WHERE " + sql2.replaceAll(",", " AND ");

		paramsList.addAll(whereParamsList);

		param.addAll(paramsList);

		return sql;
	}

	/**
	 * Execute Insert SQL
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean exe(SQLiteDatabase myDatabase) throws Exception {
		if (myDatabase == null) {
			throw new Exception("Database is null");
		}

		boolean b = false;
		ArrayList params = new ArrayList();
		String sql = genSql(params);

		// System.out.println("sql=" + sql);
		if (StringUtils.isNull(sql)) {
			return b;
		}
		try {
			// b = SqlLiteTemp.operate(myDatabase,params, sql,true);
			b = PcSqlLiteTemp.operate(myDatabase, params, sql);
		} catch (Exception e) {
		}

		paramsList = null;
		whereParamsList = null;
		params = null;
		sqlBuf = null;
		whereBuf = null;

		return b;
	}

}
