package com.dodo.project.base.dao.jfinal.utils;

import com.dodo.project.base.dao.jfinal.annotation.JFinalORMDtoField;
import com.dodo.project.base.exception.utils.AssertHelper;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <b>JFinalORMConvertDTOHelper</b></br>
 *
 * <pre>
 * record、model转换工具
 * </pre>
 *
 * @Author xqyjjq walk_code@163.com
 * @Date 2018/11/2 15:07
 * @Since JDK 1.8
 */
public class JFinalORMConvertDTOHelper {
	private static final Logger logger = LoggerFactory.getLogger(JFinalORMConvertDTOHelper.class);

	/*
	 * @Description: map转bean
	 * @Author: walk_code walk_code@163.com
	 * @Param: [recordToMap, classz]
	 * @return: T
	 * @Date: 2018/11/2 15:31
	 */
	public static <T> T mapToBean(Map<String, Object> recordToMap, Class<T> classz) {
		AssertHelper.notNull(recordToMap, "换成成bean时发生错误，bean object不允许未null。");
		try {
			Field[] fields     = classz.getDeclaredFields();
			T       beanObject = classz.newInstance();
			try {
				if (beanObject instanceof Model) {
					Method method = Model.class.getMethod("put", Map.class);
					method.invoke(beanObject, recordToMap);
				}
			} catch (Exception method) {
				// empty catch block
			}
			HashMap<String, Object> resultMap = new HashMap<String, Object>(recordToMap);
			for (Map.Entry<String, Object> entry : recordToMap.entrySet()) {
				resultMap.put(entry.getKey().toLowerCase().replace("_", ""), entry.getValue());
			}
			String            fieldName         = null;
			String            fieldType         = null;
			JFinalORMDtoField jfinalORMDtoField = null;
			for (Field field : fields) {
				field.setAccessible(true);
				fieldName = field.getName().toLowerCase();
				fieldType = field.getGenericType().toString();
				Object valueObj = resultMap.get(fieldName);
				jfinalORMDtoField = field.getAnnotation(JFinalORMDtoField.class);
				if (null != jfinalORMDtoField) {
					if (StringUtils.isNotBlank((CharSequence) jfinalORMDtoField.name())) {
						valueObj = resultMap.get(jfinalORMDtoField.name());
					}
					Class   customedEnum = null;
					boolean isInteface   = EnumHelper.isInteface((Class) jfinalORMDtoField.enumTypeClass(), CustomedEnum.class);
					if (isInteface) {
						customedEnum = jfinalORMDtoField.enumTypeClass();
						Object value = null;
						value = jfinalORMDtoField.returnAllEnumField() ? EnumHelper.enumToMapByCode((Class) customedEnum, (Object) valueObj) : EnumHelper.getEnumDescpBycode((Class) customedEnum, (Object) valueObj);
						field.set(beanObject, value);
						continue;
					}
				}
				if (valueObj == null) continue;
				if (fieldType.toLowerCase().contains("object")) {
					field.set(beanObject, valueObj);
					continue;
				}
				String valueToStr = String.valueOf(valueObj);
				if (StringUtils.isEmpty((CharSequence) valueToStr)) continue;
				if (fieldType.toLowerCase().contains("string")) {
					field.set(beanObject, valueToStr);
					continue;
				}
				if (fieldType.toLowerCase().contains("double")) {
					field.setDouble(beanObject, Double.valueOf(valueToStr));
					continue;
				}
				if (fieldType.toLowerCase().contains("int")) {
					field.setInt(beanObject, Integer.valueOf(valueToStr));
					continue;
				}
				if (fieldType.toLowerCase().contains("boolean")) {
					field.setBoolean(beanObject, Boolean.valueOf(valueToStr));
					continue;
				}
				if (fieldType.toLowerCase().contains("byte")) {
					field.setByte(beanObject, Byte.valueOf(valueToStr));
					continue;
				}
				if (fieldType.toLowerCase().contains("long")) {
					field.setLong(beanObject, Long.valueOf(valueToStr));
					continue;
				}
				if (fieldType.toLowerCase().contains("short")) {
					field.setShort(beanObject, Short.valueOf(valueToStr));
					continue;
				}
				if (!fieldType.toLowerCase().contains("float")) continue;
				field.setFloat(beanObject, Float.valueOf(valueToStr).floatValue());
			}
			return beanObject;
		} catch (Exception e) {
			logger.error("转化Record对象为对应Bean对象报错.param.classz=[" + classz + "]", (Throwable) e);
			return null;
		}
	}

	/*
	 * @Description: record转bean
	 * @Author: walk_code walk_code@163.com
	 * @Param: [record, classz]
	 * @return: T
	 * @Date: 2018/11/2 15:33
	 */
	public static <T> T recordToBean(Record record, Class<T> classz) {
		return mapToBean(record.getColumns(), classz);
	}

	/*
	 * @Description: List<record>转bean
	 * @Author: walk_code walk_code@163.com
	 * @Param: [records, classz]
	 * @return: T
	 * @Date: 2018/11/2 15:34
	 */
	public static <T> List<T> recordListToBeanList(List<Record> records, Class<T> classz) {
		if (CollectionUtils.isEmpty(records)) {
			return null;
		}

		return records.stream().map(record -> recordToBean(record, classz)).collect(Collectors.toList());
	}

	/*
	 * @Description: List<record>转map
	 * @Author: walk_code walk_code@163.com
	 * @Param: [records, classz]
	 * @return: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @Date: 2018/11/2 15:48
	 */
	public static List<Map<String, Object>> recordListToMapList(List<Record> records) {
		if (CollectionUtils.isEmpty(records)) {
			return null;
		}

		return records.stream().map(record -> record.getColumns()).collect(Collectors.toList());
	}

	/*
	 * @Description: recordPageList转beanPageList
	 * @Author: walk_code walk_code@163.com
	 * @Param: [pageList, classz]
	 * @return: com.jfinal.plugin.activerecord.Page<T>
	 * @Date: 2018/11/2 15:51
	 */
	public static <T> Page<T> recordPageListToBeanPageList(Page<Record> pageList, Class<T> classz) {
		if (null == pageList) {
			return null;
		}

		List<T> beanList = recordListToBeanList(pageList.getList(), classz);

		return new Page<>(beanList, pageList.getPageNumber(), pageList.getPageSize(), pageList.getTotalPage(), pageList.getTotalRow());
	}

	/*
	 * @Description: recordPageList转MapPageList
	 * @Author: walk_code walk_code@163.com
	 * @Param: [pageList]
	 * @return: com.jfinal.plugin.activerecord.Page<java.util.Map<java.lang.String,java.lang.Object>>
	 * @Date: 2018/11/2 16:11
	 */
	public static Page<Map<String, Object>> recordPageListToMapPageList(Page<Record> pageList) {
		if (null == pageList) {
			return new Page();
		}

		List<Map<String, Object>> mapList = recordListToMapList(pageList.getList());

		return new Page(mapList, pageList.getPageNumber(), pageList.getPageSize(), pageList.getTotalPage(), pageList.getTotalRow());

	}

	/*
	 * @Description: model转bean
	 * @Author: walk_code walk_code@163.com
	 * @Param: [model, classz]
	 * @return: T
	 * @Date: 2018/11/2 16:11
	 */
	public static <T> T modelToBean(Model model, Class<T> classz) {
		return recordToBean(model.toRecord(), classz);
	}

	public static <T> List<T> modelListToBeanList(List<?> modelList, Class<T> classz) {
		if (CollectionUtils.isEmpty(modelList)) {
			return new ArrayList();
		}

		return modelList.stream().map(record -> modelToBean((Model) record, classz)).collect(Collectors.toList());
	}

	public static <T> Page<T> modelPageListToBeanPageList(Page<?> pageList, Class<T> classz) {
		if (null == pageList) {
			return null;
		}

		List<T> beanList = modelListToBeanList(pageList.getList(), classz);

		return new Page(beanList, pageList.getPageNumber(), pageList.getPageSize(), pageList.getTotalPage(), pageList.getTotalRow());
	}

	public static Map<String, Object> modelToMap(Model model) {
		return model.toRecord().getColumns();
	}

	public static List<Map<String, Object>> modelListToMapList(List<?> modelList) {
		if (CollectionUtils.isEmpty(modelList)) {
			return null;
		}

		return modelList.stream().map(model -> modelToMap((Model) model)).collect(Collectors.toList());
	}

	public static Page<Map<String, Object>> modelPageListToMapPageList(Page<?> pageList) {
		if (null == pageList) {
			return null;
		}

		List<Map<String, Object>> mapList = modelListToMapList(pageList.getList());

		return new Page(mapList, pageList.getPageNumber(), pageList.getPageSize(), pageList.getTotalPage(), pageList.getTotalRow());
	}


}
