package com.supermap.realestate.registration.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.ViewClass.QueryClass.QLInfo;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.wisdombusiness.web.Message;

/**
 * 
 * @Description:查询service 查询相关的服务都放到里边
 * @author 俞学斌
 * @date 2015年8月25日 下午09:43:22
 */
public interface QueryService2 {
 

	public Message queryHouse(Map<String, String> conditionmap, int page, int rows,boolean iflike,String fwzt,String sort,String order,String zjdy);
	
	 

}
