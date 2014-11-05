/**
 * @(#)TestPullParser.java   2014-8-11
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 * Pull解析器的运行方式与 SAX 解析器相似。它提供了类似的事件，
 * 如：开始元素和结束元素事件，使用parser.next()可以进入下一个元素并触发相应事件。跟SAX不同的是， Pull解析器产生的事件是一个数字，而非方法，
 * 因此可以使用一个switch对感兴趣的事件进行处理。当元素开始解析时，调用parser.nextText()方法可以获取下一个Text类型节点的值。
 */

package com.pc.xml.test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.res.AssetManager;

/**
  * 
  * @author chenj
  * @date 2014-8-11
  */

public class TestPullParser {

	/**
	 * 采用XmlPullParser来解析XML文件
	 *
	 * @param inStream
	 * @return
	 * @throws Throwable
	 */
	public static List<Student> getStudents(Context context) throws Throwable {
		List<Student> students = null;
		Student mStudent = null;

		AssetManager asset = context.getAssets();
		InputStream inStream = asset.open("student.xml");

		// StringReader reader = new StringReader("xmlStr");
		// InputSource is = new InputSource(reader);
		// is.setCharacterStream(reader);

		// 创建XmlPullParser,有两种方式
		// 方式一:使用工厂类XmlPullParserFactory
		XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();
		XmlPullParser parser = pullFactory.newPullParser();

		// 方式二:使用Android提供的实用工具类android.util.Xml
		// XmlPullParser parser = Xml.newPullParser();

		// 解析文件输入流
		parser.setInput(inStream, "UTF-8");

		// 产生第一个事件
		int eventType = parser.getEventType();

		// 只要不是文档结束事件，就一直循环
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			// 触发开始文档事件
				case XmlPullParser.START_DOCUMENT:
					students = new ArrayList<Student>();
					break;

				// 触发开始元素事件
				case XmlPullParser.START_TAG:
					// 获取解析器当前指向的元素的名称
					String name = parser.getName();
					if ("student".equals(name)) {
						// 通过解析器获取id的元素值，并设置student的id
						mStudent = new Student();
						mStudent.setId(parser.getAttributeValue(0));
					}

					if (mStudent != null) {
						if ("name".equals(name)) {
							// 获取解析器当前指向元素的下一个文本节点的值
							mStudent.setName(parser.nextText());
						}
						if ("age".equals(name)) {
							// 获取解析器当前指向元素的下一个文本节点的值
							mStudent.setAge(new Short(parser.nextText()));
						}
						if ("sex".equals(name)) {
							// 获取解析器当前指向元素的下一个文本节点的值
							mStudent.setSex(parser.nextText());
						}
					}
					break;

				// 触发结束元素事件
				case XmlPullParser.END_TAG:
					if ("student".equals(parser.getName())) {
						students.add(mStudent);
						mStudent = null;
					}
					break;

				default:
					break;
			}

			eventType = parser.next();
		}
		return students;
	}
}
