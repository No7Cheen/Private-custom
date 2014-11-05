/**
 * @(#)GenericsUtils.java 2014-1-13 Copyright 2014 it.kedacom.com, Inc. All
 *                        rights reserved.
 */

package com.pc.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 泛型帮助类
 * @author chenjian
 * @date 2014-1-13
 */
@SuppressWarnings("rawtypes")
public class GenericsUtils {

	private GenericsUtils() {
	}

	public static Class getSuperClassGenricType(Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	public static Class getSuperClassGenricType(Class clazz, int index) {
		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			// System.out.println(clazz.getSimpleName() +
			// "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			// System.out.println("Index: " + index + ", Size of " +
			// clazz.getSimpleName() + "'s Parameterized Type: "
			// + params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			// System.out.println(clazz.getSimpleName() +
			// " not set the actual class on superclass generic parameter");
			return Object.class;
		}
		return (Class) params[index];
	}
}
