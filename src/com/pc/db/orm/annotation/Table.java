package com.pc.db.orm.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface Table 表示表
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
	java.lang.annotation.ElementType.TYPE
})
public @interface Table {

	/**
	 * 表名
	 * @return the string
	 */
	public abstract String name();
}
