/**
 * @(#)GSonStringLibs.java 2014-4-8 Copyright 2014 it.kedacom.com, Inc. All
 *                         rights reserved.
 */

package com.pc.utils.gson;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author chenjian
 * @date 2014-4-8
 */

public class GSonListObjectLibs {

	/**
	 * to json
	 * @param list
	 * @return
	 */
	public static String toGson(List<? extends Object> list) {
		try {
			return new Gson().toJson(list);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * @param gson
	 * @return
	 */
	public static List<? extends Object> toList(String gson) {
		try {
			return new Gson().fromJson(gson, new TypeToken<List<? extends Object>>() {
			}.getType());
		} catch (Exception e) {
			return null;
		}
	}

}
