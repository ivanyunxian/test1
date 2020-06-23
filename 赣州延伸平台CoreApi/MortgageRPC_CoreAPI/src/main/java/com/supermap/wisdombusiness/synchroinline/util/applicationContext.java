package com.supermap.wisdombusiness.synchroinline.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class applicationContext implements ApplicationContextAware{
	
	private static ApplicationContext appContext;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		appContext = applicationContext;
	}
	public ApplicationContext getAppContext(){
		return appContext;
	}
	public Session getSession(){
		return appContext.getBean(SessionFactory.class).getCurrentSession();
	}
}
