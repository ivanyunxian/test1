package com.supermap.wisdombusiness.core;

import javax.annotation.PostConstruct;

import org.hibernate.SessionFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.realestate.registration.util.StringHelper;

/**
 * 
 * Hibernate自动注册侦听事件类
 * @ClassName: HibernateEventWiring
 * @author liushufeng
 * @date 2015年11月3日 下午5:53:22
 */
@Component
public class SuperHibernateEventWiring {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private SuperHibernateListener logListener;

	@PostConstruct
	public void registerListeners() {

		String ifwritelog = GetProperties.getConstValueByKey("WRITELOGTODB");
		if (!StringHelper.isEmpty(ifwritelog) && "TRUE".equals(ifwritelog.toUpperCase())) {
			EventListenerRegistry registry = ((SessionFactoryImpl) sessionFactory).getServiceRegistry().getService(EventListenerRegistry.class);
			registry.getEventListenerGroup(EventType.POST_INSERT).appendListener(logListener);
			registry.getEventListenerGroup(EventType.POST_UPDATE).appendListener(logListener);
			registry.getEventListenerGroup(EventType.POST_DELETE).appendListener(logListener);
		}
	}
}