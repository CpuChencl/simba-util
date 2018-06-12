package com.simba.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 代码生成器的描述注解
 * 
 * @author caozj
 *
 */
@Target({ ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DBFieldAnnotation {

	String field() default "";
	String desc() default "";
	String oper() default "=";
}
