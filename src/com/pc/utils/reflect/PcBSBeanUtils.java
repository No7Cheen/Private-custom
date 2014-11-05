package com.pc.utils.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.pc.utils.StringUtils;

public class PcBSBeanUtils {

	private PcBSBeanUtils() {
	}

	/**
	 * 循环向上转型,获取对象的public字段(DeclaredField)
	 *
	 * @param object
	 * @param propertyName 属性
	 * @return
	 * @throws NoSuchFieldException 如果没有该Field时抛 NoSuchFieldException
	 */
	public static Field getDeclaredField(Object object, String propertyName) throws NoSuchFieldException {
		if (object == null || StringUtils.isNull(propertyName)) return null;

		return getDeclaredField(object.getClass(), propertyName);
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField
	 *
	 * @param clazz
	 * @param propertyName 属性
	 * @return
	 * @throws NoSuchFieldException 如果没有该Field时抛 NoSuchFieldException
	 */
	@SuppressWarnings("rawtypes")
	public static Field getDeclaredField(Class clazz, String propertyName) throws NoSuchFieldException {
		if (clazz == null || StringUtils.isNull(propertyName)) return null;

		for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				return superClass.getDeclaredField(propertyName);
			} catch (NoSuchFieldException e) {
			}
		}
		throw new NoSuchFieldException("No such field: " + clazz.getName() + '.' + propertyName);
	}

	/**
	 * 暴力获取对象变量，忽略private,protected修饰符的限制
	 *
	 * @param object
	 * @param propertyName 属性
	 * @return
	 * @throws NoSuchFieldException 如果没有该Field时抛NoSuchFieldException
	 */
	public static Object forceGetProperty(Object object, String propertyName) throws NoSuchFieldException {
		if (object == null || StringUtils.isNull(propertyName)) return null;

		Field field = getDeclaredField(object, propertyName);
		boolean accessible = field.isAccessible();
		field.setAccessible(true);

		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			// logger.info("error wont' happen");
		}
		field.setAccessible(accessible);

		return result;
	}

	/**
	 * 暴力设置对象变量,忽略private,protected修饰符的限制
	 *
	 * @param object
	 * @param propertyName 属性
	 * @param newValue
	 * @throws NoSuchFieldException 如果没有该Field时抛NoSuchFieldException
	 */
	public static void forceSetProperty(Object object, String propertyName, Object newValue) throws NoSuchFieldException {
		if (object == null || StringUtils.isNull(propertyName)) return;
		Field field = getDeclaredField(object, propertyName);
		boolean accessible = field.isAccessible();
		field.setAccessible(true);
		try {
			field.set(object, newValue);
		} catch (IllegalAccessException e) {
			// logger.info("Error won't happen");
		}
		field.setAccessible(accessible);
	}

	/**
	 * 暴力调用对象函数,忽略private,protected修饰符的限制
	 *
	 * @param object
	 * @param methodName 方法名
	 * @param params 方法属性
	 * @return
	 * @throws NoSuchMethodException 如果没有该Method时抛NoSuchMethodException
	 */
	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	public static Object invokePrivateMethod(Object object, String methodName, Object... params) throws NoSuchMethodException {
		if (object == null || StringUtils.isNull(methodName)) return null;

		Class[] types = new Class[params.length];
		for (int i = 0; i < params.length; i++) {
			types[i] = params[i].getClass();
		}

		Class clazz = object.getClass();
		Method method = null;
		for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				method = superClass.getDeclaredMethod(methodName, types);
				break;
			} catch (NoSuchMethodException e) {
				// 方法不在当前类, 继续向上转型
			}
		}

		if (method == null) throw new NoSuchMethodException("No Such Method:" + clazz.getSimpleName() + methodName);

		boolean accessible = method.isAccessible();
		method.setAccessible(true);
		Object result = null;
		try {
			result = method.invoke(object, params);
		} catch (Exception e) {
			// ReflectionUtils.handleReflectionException(e);
		}
		method.setAccessible(accessible);
		return result;
	}

	/**
	 * 按Filed的类型取得Field列表
	 *
	 * @param object
	 * @param type
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<Field> getFieldsByType(Object object, Class type) {
		List<Field> list = new ArrayList<Field>();
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.getType().isAssignableFrom(type)) {
				list.add(field);
			}
		}
		return list;
	}

	/**
	 * 按FiledName获得Field的类
	 *
	 * @param type
	 * @param name
	 * @return
	 * @throws NoSuchFieldException
	 */
	@SuppressWarnings("rawtypes")
	public static Class getPropertyType(Class type, String name) throws NoSuchFieldException {
		return getDeclaredField(type, name).getType();
	}

	/**
	 * 获得field的getter函数名称
	 *
	 * @param type
	 * @param fieldName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getGetterName(Class type, String fieldName) {
		if (type == null || StringUtils.isNull(fieldName)) return null;
		if (type.getName().equals("boolean")) {
			return "is" + getIsName(fieldName);
		} else {
			return "get" + getSetterName(fieldName);
		}
	}

	/**
	 * 
	 *
	 * @param propertyName
	 * @return
	 */
	private static String getGetterName(String propertyName) {
		String method = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
		return method;
	}

	/**
	 * 
	 *
	 * @param propertyName
	 * @return
	 */
	private static String getIsName(String propertyName) {
		String method = "is" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
		return method;
	}

	/**
	 * 
	 *
	 * @param propertyName
	 * @return
	 */
	private static String getSetterName(String propertyName) {
		String method = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
		return method;
	}

	/**
	 * 获得field的getter函数,如果找不到该方法,返回null
	 *
	 * @param type
	 * @param fieldName
	 * @return
	 */
	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	public static Method getGetterMethod(Class type, String fieldName) {
		try {
			return type.getMethod(getGetterName(type, fieldName));
		} catch (NoSuchMethodException e) {
			// logger.error(e.getMessage(), e);
		}
		return null;
	}

}
