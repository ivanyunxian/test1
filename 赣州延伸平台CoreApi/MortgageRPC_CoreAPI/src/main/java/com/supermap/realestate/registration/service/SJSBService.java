package com.supermap.realestate.registration.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * 
 * @Description:数据上报结果查询
 * @author diaoliwei
 * @date 2016年1月17日 下午3:12:22
 * @Copyright SuperMap
 */
public interface SJSBService {

	/**
	 * 查询分页列表
	 * @param pageIndex
	 * @param pageSize
	 * @param mapCondition
	 * @author diaoliwei
	 * @return
	 */
	public Message getPagedList(int page, int rows, Map<String, Object> mapCondition);
	public Message getPagedList1(int page, int rows, Map<String, Object> mapCondition,boolean iflike);
	/**
	 * 分页获取数据上报项目记录
	 * @param request
	 * @param response
	 * @author yuxuebin
	 * @date 2016年04月10日 13:51:10
	 * @return
	 */
	public Message getSJSBData(HttpServletRequest request);

	public ResultMessage Report(String xmbhs,HttpServletRequest request);
	
	public ResultMessage branchDelete(String ids);

	public Message GetReportInfoList(HttpServletRequest request);

	public Map<String,String> getTJSBQK(HttpServletRequest request);
	
	public HashMap<String, String> Report1(String xmbh,
			HttpServletRequest request);

	@SuppressWarnings("rawtypes")
	public List<Map> GetReportDetailList(HttpServletRequest request);

	public List<String> GetAutoReportList(HttpServletRequest request);
	 
}
