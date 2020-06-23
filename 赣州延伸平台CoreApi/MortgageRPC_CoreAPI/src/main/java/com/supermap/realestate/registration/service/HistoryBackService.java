package com.supermap.realestate.registration.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.supermap.realestate.registration.ViewClass.UnitInfo;
import com.supermap.wisdombusiness.web.Message;

/**
 * 
 * @Description:历史回溯服务类
 * @author yuxuebin
 * @date 2017年02月28日 10:11:43
 * @Copyright SuperMap
 */
public interface HistoryBackService {

	List<UnitInfo> getUnitHistoryList(HttpServletRequest request)
			throws Exception;

	List<List<HashMap<String, String>>> getRightHistoryList(
			HttpServletRequest request) throws Exception;
	
	List<UnitInfo> getUnitHistoryListEx(HttpServletRequest request)
			throws Exception;
	/**
	 * 仅获取单元历史回溯信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<UnitInfo> getOnlyUnit(HttpServletRequest request)throws Exception;
	Message querybuilding(int page, int rows, String zdbdcdyid);
}
