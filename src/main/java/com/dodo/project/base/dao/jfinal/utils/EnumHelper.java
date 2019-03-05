package com.dodo.project.base.dao.jfinal.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * <b>EnumHelper</b></br>
 *
 * <pre>
 * 枚举辅助类
 * </pre>
 *
 * @Author xqyjjq walk_code@163.com
 * @Date 2018/11/2 15:15
 * @Since JDK 1.8
 */
public class EnumHelper {
	/* 
	* @Description:  
	* @Author: walk_code walk_code@163.com
	* @Param: [clazz, code] 
	* @return: T  
	* @Date: 2018/11/2 15:17
	*/ 
	public static <T extends CustomedEnum> T getEnumBycode(Class<T> clazz, Object code) {
		for (T temp : clazz.getEnumConstants())
			if (String.valueOf(temp.code()).equals(String.valueOf(code)))
				return temp;
		return null;
	}

	public static Object getEnumDescpBycode(Class<? extends CustomedEnum> clazz, Object code) {
		CustomedEnum customedEnum = getEnumBycode(clazz, code);
		if (null != customedEnum) {
			return customedEnum.desc();
		}
		return null;
	}

	public static Map<Object, Object> enumToMapByCode(Class<? extends CustomedEnum> clazz, Object code) {
		CustomedEnum customedEnum = getEnumBycode(clazz, code);
		if (null != customedEnum) {
			Map<Object, Object> enumMap = new HashMap<Object, Object>(2);
			enumMap.put("code", customedEnum.code());
			enumMap.put("descp", customedEnum.desc());
			return enumMap;
		}
		return null;
	}

	public static boolean isInteface(Class<?> enumClass, Class<?> intefaceClazz) {
		for (Class<?> clazz : enumClass.getInterfaces()) {
			if (intefaceClazz == clazz) {
				return true;
			}
		}
		return false;
	}

}
