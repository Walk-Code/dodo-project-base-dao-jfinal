package com.dodo.project.base.dao.jfinal.fiter;

import com.alibaba.druid.filter.FilterChain;
import com.alibaba.druid.filter.FilterEventAdapter;
import com.alibaba.druid.proxy.jdbc.ConnectionProxy;
import com.alibaba.druid.proxy.jdbc.PreparedStatementProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/*
 * <b>CustomDruidFilter</b></br>
 *
 * <pre>
 * 自定义拦截器ORM层
 * </pre>
 *
 * @Author xqyjjq walk_code@163.com
 * @Date 2018/10/31 10:11
 * @Since JDK 1.8
 */
public class CustomDruidFilter extends FilterEventAdapter {
	private static final Logger logger = LoggerFactory.getLogger(CustomDruidFilter.class);

	@Override
	public PreparedStatementProxy connection_prepareStatement(FilterChain chain, ConnectionProxy connection, String sql) throws SQLException {
		logger.debug("sql拦截器：\n sql:{}", sql);
		return super.connection_prepareStatement(chain, connection, sql);
	}
}
