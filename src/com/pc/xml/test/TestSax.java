package com.pc.xml.test;
import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
  * @(#)TestSax.java   2014-8-11
  * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
  * 
  * SAX(Simple API for XML)解析器是一种基于事件的解析器，它的核心是事件处理模式，主要是围绕着事件源以及事件处理器来工作的。当事件源产生事件后，
  * 调用事件处理器相应的处理方法，一个事件就可以得到处理。在事件源调用事件处理器中特定方法的时候，还要传递给事件处理器相应事件的状态信息，这样事
  * 件处理器才能够根据提供的事件信息来决定自己的行为。SAX解析器的优点是解析速度快，占用内存少。非常适合在Android移动设备中使用。
  */

/**
 * 
 * @author chenj
 * @date 2014-8-11
 */

public class TestSax extends DefaultHandler {

	// @formatter:off
	static String xmlStr = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
			+ "<books id=\"16392\">"
			+ 		"<book id=\"12\">  "
			+			"<name>thinking in java</name>"
			+			"<price>85.5</price>"
			+		"</book>"
			+ 		"<book id=\"15\">"
			+ 			"<name>Spring in Action</name>"
			+ 			"<price>39.0</price>"
			+ 		"</book>"
			+ "</books>";
	// @formatter:on

	public static void main(String[] args) {
		System.out.println(xmlStr);

		try {
			// 解析文件
			// SAXParserFactory factory = SAXParserFactory.newInstance();
			// SAXParser parser = factory.newSAXParser();
			// TestSax handler = new TestSax();
			// parser.parse(new File(xmlStr), handler);

			// 对字符串解析
			{
				StringReader reader = new StringReader(xmlStr);
				InputSource is = new InputSource(reader);
				is.setCharacterStream(reader);

				// 获取SAX解析
				SAXParserFactory saxFactory = SAXParserFactory.newInstance();
				SAXParser parser = saxFactory.newSAXParser();

				// 从SAXParser获取XMLReader
				XMLReader xmlReader = parser.getXMLReader();

				TestSax parserHandler = new TestSax();
				xmlReader.setContentHandler(parserHandler);
				xmlReader.parse(is);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String preTag = null;// 作用是记录解析时的上一个节点名称

	/**
	 * 当读入<?xml.....>时，会调用startDocument()方法
	 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
	 */
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();

		System.out.println("------startDocument------");
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();

		System.out.println("------endDocument------");
	}

	/**
	 * 注意：由于有些环境不一样，有时候第二个参数有可能为空，所以可以使用第三个参数，因此在解析前，先调用一下看哪个参数能用，第4个参数是这个节点的属性
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		System.out.print("startElement\t " + qName + "\t");

		if ("books".equals(qName)) {
			System.out.print(Integer.parseInt(attributes.getValue(0)) + "\t");
		}

		if ("book".equals(qName)) {
			System.out.print(Integer.parseInt(attributes.getValue(0)) + "\t");
		}

		if ("name".equals(qName)) {
			System.out.print(attributes.getValue(0) + "\t");
		}

		preTag = qName;// 将正在解析的节点名称赋给preTag

		System.out.println();
	}

	/**
	 * 当解析结束时置为空。这里很重要，
	 * 例如，当图中画3的位置结束后，会调用这个方法，如果这里不把preTag置为null，根据startElement(....)方法，preTag的值还是book，当文档顺序读到图
	 * 中标记4的位置时，会执行characters(char[] ch, int start, int length)这个方法，而characters(....)方法判断preTag!=null，会执行if判断的代码，
	 * 这样就会把空值赋值给book，这不是我们想要的
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		System.out.print("endElement\t " + qName + "\t");

		if ("book".equals(qName)) {
		}
		preTag = null;

		System.out.println();
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		System.out.print("characters\t ");

		if (preTag != null) {
			String content = new String(ch, start, length);
			if ("name".equals(preTag)) {
				System.out.println(content);
			} else if ("price".equals(preTag)) {
				System.out.println(Float.parseFloat(content));
			}
		}

		System.out.println();
	}
}
