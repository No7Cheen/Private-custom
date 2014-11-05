/**
 * @(#)IocView.java   2014-8-12
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.app.v4fragment.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
  * 
  * @author chenj
  * @date 2014-8-12
  */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FragmentIocView {

	public int id();

	public int parentId() default 0;

	/**
	 * view.setVisibility(visibility) 
	 *
	 * @return
	 */
	public int visibility() default -1;

	/**
	 * TextView.setText(resid)
	 *
	 * @return
	 */
	public int initTextResid() default 0;

	/**
	 * TextView.setHint(resid)
	 *
	 * @return
	 */
	public int initHintResid() default 0;

	/**
	 * ImageView.setImageResource(resid)
	 *
	 * @return
	 */
	public int initImageresId() default 0;

}
