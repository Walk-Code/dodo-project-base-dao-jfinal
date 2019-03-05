package com.dodo.project.base.dao.jfinal.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * <b>JFinalORMTransaction</b></br>
 *
 * <pre>
 * 声明事物注解(方法级别)
 * </pre>
 *
 * @Author xqyjjq walk_code@163.com
 * @Date 2018/10/30 16:05
 * @Since JDK 1.8
 */
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface JFinalORMTransaction {
	public int transactionLevel() default 4;

	public String dbConfigName();
}
