package com.supermap.realestate.registration.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.supermap.realestate.registration.mapping.HandlerMapping.RegisterWorkFlow;
import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * 
 * @Description:
 * @author mss
 * @Copyright SuperMap
 */
public interface MappingService {

	/**
	 * 通过id获取流程信息的接口
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public Map<String, Object> GetLcxx(String qlid);

	/**
	 * 保存form所有数据
	 * 
	 * @return
	 */
	 

	public  ResultMessage SaveOrUpdate(HttpServletRequest request);

	public List<RegisterWorkFlow> getWorkflows();

	/** 
	* @author  buxiaobo
	* @date 创建时间：2017年4月18日 下午2:27:10 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return  
	*/
	public int getReadcontractbtn(String xMBH);
	/**
	 *  获取业务流程配置的“是否允许使用申请人新增按钮”字段的值
	 *  @param request 
	 *  @param response
	 *  @return "0" (允许) 或   "1" （禁止） 
	 */
	public int getIsAllowAddSqr(String xmbh);
}
