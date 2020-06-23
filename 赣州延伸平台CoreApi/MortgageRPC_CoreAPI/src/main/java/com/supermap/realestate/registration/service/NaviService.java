package com.supermap.realestate.registration.service;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import com.supermap.realestate.registration.ViewClass.ZS;

/**
 * 
 * @Description:导航service
 * @author 刘树峰
 * @date 2015年6月12日 下午3:09:10
 * @Copyright SuperMap
 */
public interface NaviService {

	/**
	 * 根据project_id获取单元信息页
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月10日下午10:44:35
	 * @param project_id
	 * @return
	 */
	public String GetDYXXPageUrl(String project_id);

	/**
	 * 根据project_id获取权利信息页
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月10日下午10:45:49
	 * @param project_id
	 * @return
	 */
	public String GetQLXXPageUrl(String project_id);
	
	/**
	 * 根据project_id获取登薄页
	 * @作者 diaoliwei
	 * @创建时间 2015年6月13日下午8:23:37
	 * @param project_id
	 * @return
	 */
	public String GetDBPageUrl(String project_id);
	
	/**
	 * 预览证书
	 * @author diaoliwei
	 * @date 2016年1月14号下午3:20:22
	 * @param request
	 * @return
	 */
	public ZS GetInfo(HttpServletRequest request) throws UnsupportedEncodingException;
	
}
