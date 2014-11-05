package com.pc.utils.gson;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

/**
 * @(#)GSonMain.java 2014-3-29 Copyright 2014 it.kedacom.com, Inc. All rights
 *                   reserved.
 */

/**
 * @author chenjian
 * @date 2014-3-29
 */

public class GSonMain {

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		testAndroidJson();
	}

	private static void testAndroidJson() {
		try {
			String s = "{\"lines\":{\"lincontent\":[{\"ss\":\"13467890\"},{\"ff\":\"苏州科达\"}]}}";
			JSONObject jo = new JSONObject(s);
			JSONObject jo2 = (JSONObject) jo.get("lines");
			System.out.println(jo2);
			JSONArray jo3 = jo2.getJSONArray("lincontent");
			System.out.println(jo3);

			for (int i = 0; i < jo3.length(); i++) {
				JSONObject j = jo3.getJSONObject(i);
				System.out.println(j);
			}
		} catch (Exception e) {
		}
	}

	private static void test1() {
		int[] numbers = {
				1, 1, 2, 3, 5, 8, 13
		};
		String[] days = {
				"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
		};

		Gson gson = new Gson();
		String numbersJson = gson.toJson(numbers);

		String daysJson = gson.toJson(days);
		System.out.println("numbersJson = " + numbersJson);
		System.out.println("daysJson = " + daysJson);
	}

	private static void test2() {
		String s = "{\"lines\":{\"lincontent\":[{\"ss\":\"13467890\"},{\"ff\":\"苏州科达\"}]}}";
		System.out.println(s);

		JsonObject jo = new Gson().fromJson(s, new TypeToken<JsonObject>() {
		}.getType());
		System.out.println(jo);
		System.out.println(jo.getAsJsonObject("lines"));

		JsonArray jArray = jo.getAsJsonObject("lines").getAsJsonArray("lincontent");
		System.out.println(jArray);
		for (int i = 0; i < jArray.size(); i++) {
			JsonObject elt = jArray.get(i).getAsJsonObject();
			System.out.println(elt + "\t");

		}
	}

}
