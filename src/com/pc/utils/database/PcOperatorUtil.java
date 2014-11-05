package com.pc.utils.database;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.pc.utils.reflect.FieldUtils;

import android.database.sqlite.SQLiteDatabase;

public abstract class PcOperatorUtil {

	public static final String KEYFields = "KTRUETOUCH__KEYFIELDS";
	public static final String TABLENAME = "KTRUETOUCH__TABLENAME";

	public static final String TYPE_STRING = "java.lang.String";

	public static final String TYPE_INTEGER = "java.lang.Integer";

	public static final String TYPE_DOUBLE = "java.lang.Double";

	public static final String TYPE_FLOAT = "java.lang.Float";

	public static final String TYPE_BOOLEAN = "java.lang.Boolean";

	public static final String TYPE_CHARACTER = "java.lang.Character";

	public static final String TYPE_SHORT = "java.lang.Short";

	public static final String TYPE_LONG = "java.lang.Long";

	public static final String TYPE_BYTE = "java.lang.Byte";

	public static final String TYPE_DATE = "java.util.Date";

	private static Set<String> operators = new HashSet<String>();
	static {
		operators.add("=");
		operators.add(">");
		operators.add(">=");
		operators.add("<");
		operators.add("<=");
		operators.add("<>");
		operators.add("like");
		operators.add("not like");
		operators.add("in");
		operators.add("not in");
	}

	public boolean validOperator(String operator) {
		return operators.contains(operator);
	}

	public Object setFieldType(Class classType, Object object) {
		if (classType == null || object == null) return null;
		String typeString = classType.getName();
		if (typeString.equals(TYPE_INTEGER) || classType.equals(Integer.TYPE)) {
			return Integer.parseInt(object.toString());
		} else if (typeString.equals(TYPE_STRING)) {
			return object.toString();
		} else if (typeString.equals(TYPE_LONG) || classType.equals(Long.TYPE)) {
			return Long.parseLong(object.toString());
		} else if (typeString.equals(TYPE_BOOLEAN) || classType.equals(Boolean.TYPE)) {
			return Boolean.parseBoolean(object.toString());
		} else if (typeString.equals(TYPE_BYTE) || classType.equals(Byte.TYPE)) {
			return Byte.parseByte(object.toString());
		} else if (typeString.equals(TYPE_CHARACTER) || classType.equals(Character.TYPE)) {
			return object.toString().charAt(0);
		} else if (typeString.equals(TYPE_DOUBLE) || classType.equals(Double.TYPE)) {
			return Double.parseDouble(object.toString());
		} else if (typeString.equals(TYPE_FLOAT) || classType.equals(Float.TYPE)) {
			return Float.parseFloat(object.toString());
		} else if (typeString.equals(TYPE_SHORT) || classType.equals(Short.TYPE)) {
			return Short.parseShort(object.toString());
		} else if (typeString.equals(TYPE_DATE)) {
			// return StringUtils.handleDate(object.toString(), dateFormat);
			return object.toString();
		} else {
			return object.toString();
		}
	}

	public String setFieldValue(String typeString) {
		String s = "?";

		return s;
	}

	public String getKeyFields(Class clazz) {
		try {
			return (String) FieldUtils.getFieldValue(clazz.newInstance(), KEYFields);
		} catch (Exception e) {
			return null;
		}
	}

	public String getKeyFields(Object bean) {
		try {
			return (String) FieldUtils.getFieldValue(bean, KEYFields);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Table name 获取bean对象中TABLENAME字段的value TABLENAME的样式为:
	 * KedaCom__tableName__=""
	 * @param bean
	 * @return
	 */
	public String getTableName(Object bean) {
		return (String) FieldUtils.getFieldValue(bean, TABLENAME);
	}

	/**
	 * 解析SQL
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public abstract String genSql(List param) throws Exception;

	/**
	 * Execution SQL
	 * @param myDatabase
	 * @return
	 * @throws Exception
	 */
	public abstract boolean exe(SQLiteDatabase myDatabase) throws Exception;
}
