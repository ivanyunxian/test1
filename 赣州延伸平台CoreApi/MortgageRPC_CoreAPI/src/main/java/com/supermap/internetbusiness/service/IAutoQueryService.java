package com.supermap.internetbusiness.service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface IAutoQueryService {
	
	//查询接口
	@SuppressWarnings("rawtypes")
	public Map queryHouse(HttpServletRequest request, String qlrmc, String qlrzjh, String bdcqzh, String bdcdyh);
}
