package com.pc.app.view.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IocView {

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

	public String click() default "";

	public String longClick() default "";

	public String itemClick() default "";

	public String itemLongClick() default "";

	public IocSelect select() default @IocSelect(selected = "");
}
