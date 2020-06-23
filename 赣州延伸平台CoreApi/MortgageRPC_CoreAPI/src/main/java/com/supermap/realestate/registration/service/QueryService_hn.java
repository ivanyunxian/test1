package com.supermap.realestate.registration.service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface QueryService_hn {
	
	//海南查询接口
	@SuppressWarnings("rawtypes")
	public Map queryHousehn(HttpServletRequest request, String qlrmc, String qlrzjh);
}
