package com.dodo.project.base.dao.jfinal.aop;

import com.dodo.project.base.dao.jfinal.annotation.JFinalORMTransaction;
import com.dodo.project.base.exception.core.BussinessException;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/*
 * <b>JFinalORMAspect</b></br>
 *
 * <pre>
 * ORM事务切面
 * </pre>
 *
 * @Author xqyjjq walk_code@163.com
 * @Date 2018/10/30 19:23
 * @Since JDK 1.8
 */
@Aspect
@Component
public class JFinalORMAspect {
	private static final Logger              logger                 = LoggerFactory.getLogger(JFinalORMAspect.class);
	private static final ThreadLocal<Object> resultTreadLocal       = new ThreadLocal<Object>();
	private static final String              LOG_MESSAGE_CLASS_NAME = "事务相关";

	@Around(value = "@annotation(jFinalORMTransaction)")
	public Object validate(ProceedingJoinPoint proceedingJoinPoint, JFinalORMTransaction jFinalORMTransaction) {
		String dbConfigName     = jFinalORMTransaction.dbConfigName();
		int    transactionLevel = jFinalORMTransaction.transactionLevel();
		logger.info(LOG_MESSAGE_CLASS_NAME + ": 事务开始。dbConfigName={}, transactionLevel={}", dbConfigName, transactionLevel);
		Db.use(dbConfigName).tx(transactionLevel, new IAtom() {
			public boolean run() {
				try {
					Object object = proceedingJoinPoint.proceed();
					// 保证线程安全
					resultTreadLocal.set(object);
				} catch (Throwable e) {
					if (e instanceof BussinessException) {
						throw new BussinessException(e);
					}

					logger.error("DB事务异常", e);
					throw new RuntimeException(e);
				}

				return true;
			}
		});
		logger.info("事务结束。");
		return resultTreadLocal.get();
	}
}
