package com.pc.utils.database;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import com.pc.utils.StringUtils;

/**
 * DELETE FROM "表格名" WHERE {条件1 AND 条件2}
 * @author ChenJian
 */
public class PcDeleteUtil extends PcOperatorUtil {

	private Class cls;
	private Object bean;

	private String tableName;

	private List whereParamsList;
	private StringBuffer whereBuf;

	private String dateFormat = "yyyy-MM-dd HH:mm:ss";
	private String target = "MYSQL";

	public PcDeleteUtil(Class cls) throws Exception {
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

	public void addDelParam(String columName, String value) throws Exception {
		addParam(columName, "=", value);
	}

	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	public void addParam(String columName, String operator, String value) throws Exception {
		if (StringUtils.isNull(columName) || StringUtils.isNull(operator) || StringUtils.isNull(value)) {
			throw new Exception("illegal params");
		}
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
		if (whereParamsList.contains(columName)) {
			return;
		}

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
			"rawtypes", "unchecked"
	})
	public String genSql(List param) throws Exception {
		if (whereBuf == null || whereBuf.length() == 0 || whereParamsList == null || whereParamsList.isEmpty()
				|| whereParamsList.size() == 0) {
			return "delete from " + tableName;
		}

		String sql = "";
		String sql1 = whereBuf.toString();
		if (sql1.endsWith(",")) {
			sql1 = sql1.substring(0, sql1.length() - 1);
		}
		sql = "DELETE FROM " + tableName + " WHERE " + sql1.replaceAll(",", " AND ");

		param.addAll(whereParamsList);

		return sql;
	}

	private String sql;

	public String getSql() {
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
		sql = genSql(params);

		// System.out.println(sql);
		if (StringUtils.isNull(sql)) {
			return b;
		}

		try {
			b = PcSqlLiteTemp.operate(myDatabase, params, sql);
		} catch (Exception e) {
			e.printStackTrace();
		}

		whereParamsList = null;
		params = null;
		whereBuf = null;

		return b;
	}

}
