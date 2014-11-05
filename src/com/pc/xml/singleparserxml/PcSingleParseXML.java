/**
 * @(#)PcSingleParseXML.java   2014-8-11
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.xml.singleparserxml;

import java.io.StringReader;
import java.util.Properties;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.util.Log;

import com.pc.utils.StringUtils;

/**
  * 
  * @author chenj
  * @date 2014-8-11
  */

public class PcSingleParseXML {

	// 定义一个Properties 用来存放标签值
	private Properties props;

	public Properties getProps() {
		return props;
	}

	/**
	 * 解析一个简单的没有重复标签的XML String，并将属性值存入Properties
	 *
	 * @param xml
	 */
	public void parseXML(String xml) {
		if (StringUtils.isNull(xml)) return;

		// 对字符串解析：
		StringReader reader = new StringReader(xml);
		InputSource is = new InputSource(reader);
		is.setCharacterStream(reader);
		try {
			// 获取SAX解析
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser parser = saxFactory.newSAXParser();

			// 从SAXParser获取XMLReader
			XMLReader xmlReader = parser.getXMLReader();

			PcSingleParserHandler parserHandler = new PcSingleParserHandler();
			xmlReader.setContentHandler(parserHandler);
			xmlReader.parse(is);

			props = parserHandler.getProperties();
		} catch (Exception e) {
			Log.e("ParseXML", "parseXML", e);
		} finally {
			if (reader != null) reader.close();
		}
	}

	/**
	 * 获取属性值
	 *
	 * @param keyname
	 * @return
	 */
	public String getSingleObject(String keyname) {
		if (StringUtils.isNull(keyname)) {
			return null;
		}

		if (props == null) {
			return null;
		}

		if (!props.containsKey(keyname)) {
			return null;
		}

		return props.getProperty(keyname);
	}

}
