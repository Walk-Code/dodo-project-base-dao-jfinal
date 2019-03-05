package com.dodo.project.base.dao.jfinal.base;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.wall.WallFilter;
import com.dodo.project.base.dao.jfinal.config.DbConfigurationInfoBean;
import com.dodo.project.base.dao.jfinal.fiter.CustomDruidFilter;
import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.IDataSourceProvider;
import com.jfinal.plugin.druid.DruidPlugin;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.druid.filter.logging.Log4jFilter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/*
 * <b>AbstractBaseJfinalDbConfiguration</b></br>
 *
 * <pre>
 * 数据库配置
 * </pre>
 *
 * @Author xqyjjq walk_code@163.com
 * @Date 2018/10/30 20:30
 * @Since JDK 1.8
 */
public abstract class AbstractBaseJfinalDbConfiguration {
	private static final Logger        logger  = LoggerFactory.getLogger(AbstractBaseJfinalDbConfiguration.class);
	private              List<IPlugin> plugins = new ArrayList<IPlugin>();

	/*
	 * @Description: 配置数据库连接基本信息
	 * @Author: walk_code walk_code@163.com
	 * @Param: []
	 * @return: com.dodo.project.base.dao.jfinal.config.DbConfigurationInfoBean
	 * @Date: 2018/10/30 20:36
	 */
	protected abstract DbConfigurationInfoBean configDbConfigurationInfoBean();

	protected ActiveRecordPlugin configAndStartDbPlugin(Class<?> mappingKitClass) {
		DbConfigurationInfoBean dbConfigurationInfoBean = this.configDbConfigurationInfoBean();
		DruidPlugin             druidPlugin             = new DruidPlugin(dbConfigurationInfoBean.getUrl(), dbConfigurationInfoBean.getUserName(), dbConfigurationInfoBean.getPassword());
		druidPlugin.setRemoveAbandoned(true);
		druidPlugin.setLogAbandoned(true);
		Log4jFilter logFilter = new Log4jFilter();
		logFilter.setStatementLogEnabled(false);
		logFilter.setConnectionLogEnabled(false);
		logFilter.setStatementLogErrorEnabled(true);
		logFilter.setStatementExecutableSqlLogEnable(true);
		druidPlugin.addFilter((Filter) logFilter);
		WallFilter wallFilter = new WallFilter();
		wallFilter.setLogViolation(true);
		wallFilter.setThrowException(false);
		druidPlugin.addFilter((Filter) wallFilter);
		druidPlugin.addFilter((Filter) new CustomDruidFilter());
		ActiveRecordPlugin activeRecordPlugin = null;
		activeRecordPlugin = StringUtils.isNotBlank((CharSequence) dbConfigurationInfoBean.getConfigName()) ? new ActiveRecordPlugin(dbConfigurationInfoBean.getConfigName(), (IDataSourceProvider) druidPlugin) : new ActiveRecordPlugin((IDataSourceProvider) druidPlugin);
		activeRecordPlugin.setDialect(dbConfigurationInfoBean.getDialect());
		try {
			Object mappingKit       = mappingKitClass.newInstance();
			Method mappingKitMethod = mappingKitClass.getMethod("mapping", ActiveRecordPlugin.class);
			mappingKitMethod.invoke(mappingKit, new Object[]{activeRecordPlugin});
		} catch (Exception e) {
			logger.error("调用Model与表结构自动映射逻辑时发生错误.", (Throwable) e);
		}
		this.plugins.add((IPlugin) druidPlugin);
		this.plugins.add((IPlugin) activeRecordPlugin);
		this.startDbPlugin();
		return activeRecordPlugin;
	}

	protected void stopDbPlugin() {
		for (IPlugin plugin : this.plugins) {
			plugin.stop();
		}
		this.plugins.clear();
	}

	private void startDbPlugin() {
		for (IPlugin plugin : this.plugins) {
			plugin.start();
		}
	}


}
