/**
 * @(#)Id.java 2014-5-15 Copyright 2014 it.kedacom.com, Inc. All rights
 *             reserved.
 */

package com.pc.db.orm.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface Id. 主键
 * @author chenjian
 * @date 2014-5-15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
	java.lang.annotation.ElementType.FIELD
})
public @interface Id {

}
