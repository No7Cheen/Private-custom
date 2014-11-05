/**
 * @(#)GSonLibs.java 2014-4-8 Copyright 2014 it.kedacom.com, Inc. All rights
 *                   reserved.
 */

package com.pc.utils.gson;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author chenjian
 * @date 2014-4-8
 */

public class GSonLibs {

	/**
	 * 将Collection对象转换成JSON
	 */
	public static String listToJson(Object obj) {
		// 生成Json字符串
		return new Gson().toJson(obj);
	}

	/**
	 * gson反序列化，Gson提供了fromJson()方法来实现从Json相关对象到java实体的方法。
	 */
	public static Object gsonToBean(String gson, Class<?> cls) {
		return new Gson().fromJson(gson, cls);
	}

	public static List<Object> gsonToList(String gson, Type typeOfT) {
		return new Gson().fromJson(gson, typeOfT);
	}

	public static List<String> gsonToList(String gson) {
		return new Gson().fromJson(gson, new TypeToken<List<String>>() {
		}.getType());
	}

}
