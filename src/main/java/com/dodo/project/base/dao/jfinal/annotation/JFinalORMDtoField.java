package com.dodo.project.base.dao.jfinal.annotation;

import org.apache.poi.util.Internal;
import sun.awt.SunHints;

import java.lang.annotation.*;

/*
 * <b>JFinalORMDtoField</b></br>
 *
 * <pre>
 * 声明ORM字段转换注解
 * </pre>
 *
 * @Author xqyjjq walk_code@163.com
 * @Date 2018/10/30 15:41
 * @Since JDK 1.8
 */
@Target(value = {ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
public @interface JFinalORMDtoField {
	public String name();

	public Class<?> enumTypeClass() default Void.class;

	public boolean returnAllEnumField() default true;
}
