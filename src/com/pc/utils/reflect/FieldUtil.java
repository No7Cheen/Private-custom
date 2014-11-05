package com.pc.utils.reflect;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.pc.utils.StringUtils;
import com.pc.utils.time.TimeUtils;

/**
 * Field Util
 * 
 * @author chenj
 * @date 2014-7-31
 */
public class FieldUtil {

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
	public static String dateFormat = "yyyy-MM-dd HH:mm:ss";

	public static HashMap<String, String> PrimitiveTypes = new HashMap<String, String>();
	static {
		PrimitiveTypes.put(TYPE_STRING, TYPE_STRING);
		PrimitiveTypes.put(TYPE_INTEGER, TYPE_INTEGER);
		PrimitiveTypes.put(TYPE_DOUBLE, TYPE_DOUBLE);
		PrimitiveTypes.put(TYPE_FLOAT, TYPE_FLOAT);
		PrimitiveTypes.put(TYPE_BOOLEAN, TYPE_BOOLEAN);
		PrimitiveTypes.put(TYPE_CHARACTER, TYPE_CHARACTER);
		PrimitiveTypes.put(TYPE_SHORT, TYPE_SHORT);
		PrimitiveTypes.put(TYPE_LONG, TYPE_LONG);
		PrimitiveTypes.put(TYPE_BYTE, TYPE_BYTE);
		PrimitiveTypes.put(TYPE_DATE, TYPE_DATE);

		PrimitiveTypes.put(TYPE_INTEGER, TYPE_INTEGER);
		PrimitiveTypes.put(TYPE_INTEGER, TYPE_INTEGER);
	}

	/**
	 * @param s the type string, such as java.lang.String
	 * @return whether the type string corresponds to a primitive type
	 */
	public static boolean isPrimitiveType(String s) {

		return PrimitiveTypes.get(s) != null;
	}

	/**
	 * @param f a field from an object
	 * @return whether the type of the field is primitive
	 */
	public static boolean isPrimitiveType(Field f) {
		Class classType = f.getType();
		if (classType.equals(Integer.TYPE) || classType.equals(Long.TYPE) || classType.equals(Byte.TYPE) || classType.equals(Boolean.TYPE) || classType.equals(Character.TYPE)
				|| classType.equals(Double.TYPE) || classType.equals(Float.TYPE) || classType.equals(Short.TYPE)) return true;
		return PrimitiveTypes.get(classType.getName()) != null;
	}

	private static Object setFieldType(Class classType, Object object) {
		if (object == null) return object;
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
			return TimeUtils.parseDate(object.toString(), dateFormat);
		} else {
			if (classType.isArray()) {
				if (object instanceof String) {
					return new String[] {
						object.toString()
					};
				}
				return (String[]) object;
			}
			return object;
		}
	}

	public static void forceSetProperty(Object object, String propertyName, Object newValue) throws NoSuchFieldException {
		if (object == null || StringUtils.isNull(propertyName)) return;
		Field field = PcBSBeanUtils.getDeclaredField(object, propertyName);
		boolean accessible = field.isAccessible();
		field.setAccessible(true);
		try {
			field.set(object, setFieldType(field.getType(), newValue));
		} catch (IllegalAccessException e) {
			// logger.info("Error won't happen");
		}
		field.setAccessible(accessible);
	}
}
