package com.dodo.project.base.dao.jfinal.service;

import com.jfinal.plugin.activerecord.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * <b>BaseDaoService</b></br>
 *
 * <pre>
 * 基础ORM操作方法
 * </pre>
 *
 * @Author xqyjjq walk_code@163.com
 * @Date 2018/10/31 15:54
 * @Since JDK 1.8
 */
public interface BaseDaoService<M extends Model> {
	public static final Logger logger = LoggerFactory.getLogger(BaseDaoService.class);

	/*
	 * @Description: 保存或者修改整个model
	 * @Author: walk_code walk_code@163.com
	 * @Param: [model]
	 * @return: boolean
	 * @Date: 2018/11/1 20:45
	 */
	default public boolean save(M model) {
		if (null == model.get("id")) {
			return model.save();
		}

		model.remove("update_time");
		return model.update();
	}

	default public boolean delete(M model) {
		return model.delete();
	}

	default public M findById(Class<? extends Model> model, Object idValue) {
		M modelObj = getModelObject(model);
		if (null == modelObj) {
			return null;
		}

		return (M) modelObj.dao().findById(idValue);
	}

	/*
	 * @Description: 获取Model对象
	 * @Author: walk_code walk_code@163.com
	 * @Param: [model]
	 * @return: M
	 * @Date: 2018/11/2 14:13
	 */
	default public M getModelObject(Class<? extends Model> model) {
		try {
			return (M) model.newInstance();
		} catch (Exception e) {
			logger.error("实例化Model对象失败。Model=[" + model.getClass() + "]", e);
			return null;
		}
	}
}
