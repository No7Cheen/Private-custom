/**
 * @(#)Column.java 2014-5-15 Copyright 2014 it.kedacom.com, Inc. All rights
 *                 reserved.
 */

package com.pc.db.orm.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface Column. 表示列
 * @author chenjian
 * @date 2014-5-15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
	java.lang.annotation.ElementType.FIELD
})
public @interface Column {

	/**
	 * 列名
	 * @return the string
	 */
	public abstract String name();

	/**
	 * 列类型
	 * @return the string
	 */
	public abstract String type() default "";

	/**
	 * 长度
	 * @return the int
	 */
	public abstract int length() default 0;
}
