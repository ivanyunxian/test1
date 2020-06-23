package com.supermap.realestate.registration.util;

import java.util.ArrayList;
import java.util.List;

import com.supermap.realestate.registration.model.BDCS_REPORTINFO;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/**
 * 
 * @Description:数据上报响应信息工具类
 * @author 俞学斌
 * @date 2016年11月12日 10:51:14
 * @Copyright SuperMap
 */
public class ReportTool {
	/*
	 * 是否正在获取报文
	 */
	public static String bResponse = "-1";
	/*
	 * 是否可以连接到sftp
	 */
	public static String bConSFTP = "1";
	/*
	 * 待处理响应报文记录
	 */
	public static List<BDCS_REPORTINFO> list_reportinfo = new ArrayList<BDCS_REPORTINFO>();
	
	/*
	 * 初始化待处理相应报文记录
	 */
	public static void InitReportInfoList() {
		CommonDao dao = SuperSpringContext.getContext()
				.getBean(CommonDao.class);
		// 获取响应报文信息最大次数
		int count = 100;
		StringBuilder builder = new StringBuilder();
		builder.append("SUCCESSFLAG='2' ");
		builder.append("AND YXBZ='1' ");
		builder.append("AND (RESCOUNT IS NULL OR RESCOUNT<=" + count + ") ");
		builder.append(" ORDER BY RESCOUNT,REPORTTIME");
		ReportTool.list_reportinfo = dao.getDataList(BDCS_REPORTINFO.class,
				builder.toString());
	}
}
