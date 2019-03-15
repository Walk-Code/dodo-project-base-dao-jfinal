package com.dodo.project.base.dao.jfinal.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * <b>RecordToBeanDTOHelper</b></br>
 *
 * <pre>
 * Map 转 bean DTO 辅助类
 * </pre>
 *
 * @Author xqyjjq walk_code@163.com
 * @Date 2019/3/14 16:27
 * @Since JDK 1.8
 */
public class RecordToBeanDTOHelper {
	/*
	 * @Description: 将map 带下划线的key转驼峰
	 * @Author: walk_code walk_code@163.com
	 * @Param: []
	 * @return: java.util.Map<java.lang.String,java.lang.Object>
	 * @Date: 2019/3/14 15:51
	 */
	public static Map<String, Object> convertMap(Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key   = entry.getKey();
			Object value = entry.getValue();
			result.put(mapKey(key), convertValue(value));
		}

		return result;
	}

	/*
	 * @Description: 下划线转小驼峰
	 * @Author: walk_code walk_code@163.com
	 * @Param: [key]
	 * @return: java.lang.String
	 * @Date: 2019/3/14 15:53
	 */
	public static String mapKey(String key) {
		StringBuilder result = new StringBuilder();
		String        newKey = Character.toLowerCase(key.charAt(0)) + key.substring(1);

		String camels[] = key.split("_");
		for (String camel : camels) {
			// 跳过原始字符串中开头、结尾的下换线或双重下划线
			if (camel.isEmpty()) {
				continue;
			}
			// 处理真正的驼峰片段
			if (result.length() == 0) {
				// 第一个驼峰片段，全部字母都小写
				result.append(camel.toLowerCase());
			} else {
				// 其他的驼峰片段，首字母大写
				result.append(camel.substring(0, 1).toUpperCase());
				result.append(camel.substring(1).toLowerCase());
			}
		}

		return result.toString();
	}

	/*
	 * @Description: map中的value如果是List<object> or Map<String, Object>类型继续进行执行 mapKey方法
	 * @Author: walk_code walk_code@163.com
	 * @Param: [object]
	 * @return: java.lang.Object
	 * @Date: 2019/3/14 15:59
	 */
	public static Object convertValue(Object obj) {
		if (obj instanceof Map) {
			return convertMap((Map<String, Object>) obj);
		} else if (obj instanceof List) {
			return convertList((List<Object>) obj);
		} else {
			return obj;
		}
	}

	/*
	 * @Description: 对list中的objec进行二次处理
	 * @Author: walk_code walk_code@163.com
	 * @Param: [list]
	 * @return: java.util.List<java.lang.Object>
	 * @Date: 2019/3/14 16:06
	 */
	public static List<Object> convertList(List<Object> list) {
		List<Object> result = new ArrayList<Object>();
		for (Object obj : list) {
			result.add(convertValue(obj));
		}

		return result;
	}

	/*
	 * @Description: record map 转 bean
	 * @Author: walk_code walk_code@163.com
	 * @Param: []
	 * @return:
	 * @Date:
	 */
	public static <T> T recordMapConvertBean(Map<String, Object> recordMap, Class<T> classs) {
		// 处理map key的值将下划线修改成小驼峰
		Map<String, Object> newMap = convertMap(recordMap);
		// 进行map 转 bean
		ObjectMapper mapper = new ObjectMapper();

		return mapper.convertValue(newMap, classs);
	}
	
	/* 
	* @Description: recordList 转 beanList 
	* @Author: walk_code walk_code@163.com
	* @Param: [records, classs] 
	* @return: java.util.List<T>  
	* @Date: 2019/3/15 11:12
	*/ 
	public static <T> List<T> recordListConvertBeanList(List<Record> records, Class<T> classs){
		return records.stream().map(record -> recordMapConvertBean(record.getColumns(), classs)).collect(Collectors.toList());
	}
	
	/* 
	* @Description: recordPage 转 beanPage
	* @Author: walk_code walk_code@163.com
	* @Param: [] 
	* @return: com.jfinal.plugin.activerecord.Page<T>  
	* @Date: 2019/3/15 11:13
	*/ 
	public static <T> Page<T> recordPageConvertBeanPage(Page<Record> recordPage, Class<T> classs) {
		if (null == recordPage) {
			return new Page<>();
		}

		List<T> beanList = recordListConvertBeanList(recordPage.getList(), classs);

		return new Page<>(beanList, recordPage.getPageNumber(), recordPage.getPageSize(), recordPage.getTotalPage(), recordPage.getTotalRow());
	}
}
