package com.supermap.realestate.registration.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * 
 * @Description:西安交易共享
 * @author yuxuebin
 * @date 2016年6月25日 15:27:22
 * @Copyright SuperMap
 */
public interface shareService_XIAN {

	public String accept(HttpServletRequest request, String str);
	
	public String createXMLtest(String xmbh);
	public List<String> createXMLList(String xmbh);
	
}
