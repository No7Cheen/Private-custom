/**
 * @(#)IniReader.java   2014-9-22
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.utils.rhq.core;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
  * ini文件读取
  * 
  * @author chenj
  * @date 2014-9-22
  */

public class IniReader {

	public Properties ini = null;

	/**
	 * 构造函数
	 * 
	 * @param fileString
	 */
	public IniReader(String fileString) {
		File file = new File(fileString);
		try {
			ini = new Properties();
			ini.load(new FileInputStream(file));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 构造函数
	 * 
	 * @param file
	 */
	public IniReader(File file) {
		try {
			ini = new Properties();
			ini.load(new FileInputStream(file));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 返回值
	 *
	 * @param key
	 * @return
	 */
	public String getIniKey(String key) {
		if (!ini.containsKey(key)) {
			return "";
		}

		try {
			return ini.get(key).toString();
		} catch (Exception e) {
			return "";
		}
	}

	public static void main(String[] args) {
		IniReader ini = new IniReader("D:/sss.ini");
		System.out.println(ini.getIniKey("option"));
	}
}
