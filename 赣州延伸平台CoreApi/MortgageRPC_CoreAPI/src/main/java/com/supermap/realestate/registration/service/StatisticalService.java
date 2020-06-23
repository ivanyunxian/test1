package com.supermap.realestate.registration.service;

import java.util.List;
import java.util.Map;

import com.supermap.wisdombusiness.web.StatisticalMessage;
import com.supermap.wisdombusiness.web.ui.tree.Tree;

public interface StatisticalService {
	/**
	 * 	获取城区区划代码树形类别，提供下拉选择框使用 
	 * @return
	 */
	List<Tree> getDivisionCodeTreeList();
	/**
	   *  获取交易日报数据
	 *<pre>
	 *	商品房统计说明（说明不用打印待报表上）以期房的预告登记、现房转移预告为准的，
	 *	时间以登簿时间为准，时间需精确到时分秒	
	 *	二手房转移统计说明（说明不用打印待报表上）以申请人双方都是个人为准的，
	 *	这样就排除开发企业转移类的业务，时间以登簿时间为准，时间需精确到时分秒
	 *</pre>
	 * @param params
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	StatisticalMessage queryFdcjyrbtjData(Map params);
}
