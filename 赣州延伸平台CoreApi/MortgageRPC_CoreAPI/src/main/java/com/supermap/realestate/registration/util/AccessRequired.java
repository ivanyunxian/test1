package com.supermap.realestate.registration.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @Description:自定义注解
 * @author diaoliwei
 * @date 2015年6月14日 下午10:11:43
 * @Copyright SuperMap
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessRequired {
	String[] value() default {};

	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface UnBoardRequired {
		String[] value() default {};
	}
}