package com.supermap.realestate.registration.service;

import java.util.Map;

import com.supermap.realestate.registration.util.LogConstValue.DLMSG;
import com.supermap.realestate.registration.util.LogConstValue.LOGINTYPE;
import com.supermap.realestate.registration.util.LogConstValue.MTOPRTYPE;
import com.supermap.realestate.registration.util.LogConstValue.MTTYPE;
import com.supermap.realestate.registration.util.LogConstValue.QUERYTYPE;

public interface LogInfoService {

	/** 数据维护日志
	 * @param description
	 * @param content
	 */
	public void dataMaintenaceLog(String content);

	/** 系统查询日志
	 * @param querytype
	 * @param queryvalues
	 */
	public void queryLog(Boolean iflike, Map<String, String> queryvalues, QUERYTYPE querytype);

	/** 系统登录日志
	 * @param querytype
	 */
	public void loginLog(LOGINTYPE querytype, DLMSG msg);

}
