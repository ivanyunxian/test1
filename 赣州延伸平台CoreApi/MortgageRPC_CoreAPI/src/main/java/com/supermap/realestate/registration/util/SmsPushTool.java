package com.supermap.realestate.registration.util;

import com.supermap.realestate.registration.model.LOG_DXTS;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @Description:短信推送工具类
 * @author taochunda
 * @date 2019年04月23日 16:18:00
 */
public class SmsPushTool {
	/*
	 * 待推送短信记录
	 */
	public static List<LOG_DXTS> list_dxtsinfo = new ArrayList<LOG_DXTS>();
	
	/*
	 * 初始化待处理相应报文记录
	 */
	public static void InitDxtsInfoList() {
		CommonDao dao = SuperSpringContext.getContext()
				.getBean(CommonDao.class);
		// 获取短信推送最大次数
		int count = 2;
		StringBuilder builder = new StringBuilder();
		builder.append("YXBZ='0' ");
		builder.append("AND SEND_STATUS='0' ");
		builder.append("AND SEND_NUMBER<=" + count );
		SmsPushTool.list_dxtsinfo = dao.getDataList(LOG_DXTS.class,
				builder.toString());
	}
}
