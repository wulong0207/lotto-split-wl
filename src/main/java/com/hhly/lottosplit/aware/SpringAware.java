package com.hhly.lottosplit.aware;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
/**
 * 
 * @ClassName: SpringAware 
 * @Description:spring 上下文工具
 * @author wuLong
 * @date 2017年12月7日 下午4:28:51 
 *
 */
@Component
public class SpringAware implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext = null;

	@Override
	public void setApplicationContext(ApplicationContext ac) throws BeansException {
		applicationContext = ac;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name){
		return (T) applicationContext.getBean(name);
	}
	
	public static void rollBack() { 
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	}
}
