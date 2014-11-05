/**
 * @(#)PcSingleParserHandler.java   2014-8-11
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.xml.singleparserxml;

import java.util.Properties;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.pc.utils.StringUtils;

/**
  * 针对简单的没有重复标签的XML String的解析
  * 
  * @author chenj
  * @date 2014-8-11
  */

public class PcSingleParserHandler extends DefaultHandler {

	private Properties mProps;
	// private String currentName;
	private StringBuffer mCurrentValue;

	public PcSingleParserHandler() {
		mProps = new Properties();
		mCurrentValue = new StringBuffer();
	}

	public Properties getProperties() {
		return mProps;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}

	/**
	 * 当遇到开始标签时被调用，比如 <tag attribute="att"> 可以得到标签属性
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		mCurrentValue.delete(0, mCurrentValue.length());
		// currentName = localName;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		mCurrentValue.append(ch, start, length);
		// if (!StringUtils.isNull(currentName)
		// && !StringUtils.isNullNotTrim(currentValue.toString())) {
		// //String v = currentValue.toString().trim();
		// String v = currentValue.toString();
		// props.put(currentName, v);
		// currentName = null;
		// }
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (!StringUtils.isNull(localName) && !StringUtils.isNullNotTrim(mCurrentValue.toString())) {
			String v = mCurrentValue.toString();
			mProps.put(localName, v);
		}
		super.endElement(uri, localName, qName);
	}
}
