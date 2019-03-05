package com.dodo.project.base.dao.jfinal.config;

import com.jfinal.plugin.activerecord.dialect.Dialect;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;

/*
 * <b>DbConfigurationInfoBean</b></br>
 *
 * <pre>
 * 数据库配置信息明细
 * </pre>
 *
 * @Author xqyjjq walk_code@163.com
 * @Date 2018/10/30 20:33
 * @Since JDK 1.8
 */
public class DbConfigurationInfoBean {
	private String  configName;
	private Dialect dialect = new MysqlDialect();
	private String  url;
	private String  userName;
	private String  password;
	private String  driverClass;

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public Dialect getDialect() {
		return dialect;
	}

	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}
}
