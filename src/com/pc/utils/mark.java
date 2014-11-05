/**
 * @ aa.java 2013-10-23
 */

package com.pc.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.SOURCE)
public @interface mark {

	String values();
}
