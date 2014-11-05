package com.pc.utils.database;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import com.pc.utils.StringUtils;

/**
 * INSERT INTO "表格名" ("columName1", "columName2", ...) VALUES ("columValue1",
 * "columValue2", ...)
 * @author ChenJian
 */
public class PcAddUtil extends PcOperatorUtil {

	private Class cls;

	private String dateFormat = "yyyy-MM-dd HH:mm:ss";
	private String keyField;
	private String tableName; // 数据表name

	private Object bean;
	private List<Object> paramsList;
	private StringBuffer insertColumNameBuf;
	private StringBuffer insertValueBuf;

	@SuppressWarnings("rawtypes")
	public PcAddUtil(Class cls) throws Exception {
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

	public Object getBean() {
		return bean;
	}

	/**
	 * 添加数据表的属性
	 * @param columName 字段Name
	 * @param value
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void addParam(String columName, String value) throws Exception {
		if (StringUtils.isNull(columName)) {
			return;
		}

		if (StringUtils.isNULL(value)) {
			value = "";
		}

		if (paramsList == null) paramsList = new ArrayList<Object>();

		if (insertColumNameBuf == null || insertValueBuf == null) {
			insertColumNameBuf = new StringBuffer();
			insertValueBuf = new StringBuffer();
		}
		// columName = columName.toLowerCase();
		if (paramsList.contains(columName)) return;

		// 返回一个bean的 Field 对象 columName为字段名 可以为private类型
		// Field field = cls.getField(columName);
		Field field = cls.getDeclaredField(columName);
		if (field != null) {
			// 返回一个字段的声明类型
			Class classType = field.getType();
			String typeString = classType.getName();

			// insert columName
			insertColumNameBuf.append(columName + ",");
			Object object = setFieldType(classType, value);
			paramsList.add(object);
			// value SQL
			insertValueBuf.append(setFieldValue(typeString) + ",");
		}
	}

	/**
	 * 解析insert SQL
	 */
	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	public String genSql(List param) {
		if (paramsList == null || paramsList.isEmpty() || paramsList.size() == 0 || insertColumNameBuf == null
				|| insertColumNameBuf.length() == 0 || insertValueBuf == null || insertValueBuf.length() == 0)
			return null;

		String sql = "";
		String sql1 = insertColumNameBuf.toString();
		String sql2 = insertValueBuf.toString();
		// 去除insertColumNameBuf中最后一个","
		if (sql1.endsWith(",")) {
			sql1 = sql1.substring(0, sql1.length() - 1);
		}
		// 去除insertValueBuf中最后一个","
		if (sql2.endsWith(",")) {
			sql2 = sql2.substring(0, sql2.length() - 1);
		}
		if (!StringUtils.isNull(tableName)) {
			sql = "INSERT INTO " + tableName + " ( " + sql1 + ") VALUES (" + sql2 + ")";
		}

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
		// System.out.println("params=" + params.size());

		if (StringUtils.isNull(sql)) {
			return b;
		}

		try {
			b = PcSqlLiteTemp.operate(myDatabase, params, sql, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		paramsList = null;
		params = null;
		insertColumNameBuf = null;
		insertValueBuf = null;

		return b;
	}

}
