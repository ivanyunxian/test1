package com.supermap.realestate.registration.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.supermap.realestate.registration.ViewClass.QueryClass.QLInfo;
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
 
import com.supermap.wisdombusiness.web.Message;

/**
 * 
 * @Description:查询service 查询相关的服务都放到里边
 * @author Yangx
 * @date 2015年8月25日 下午09:43:22
 */
public interface QueryQlsjService {
//  产权预计时间查询
	public Message queryYjHouse(Map<String, String> conditionmap, int page, int rows,String qssj,String zl, String bdcqzh, String bdcdyh);
	
	public Message queryYjTdxx(Map<String, String> conditionmap, int page, int rows,String qssj,String zl, String bdcqzh, String bdcdyh);
//  抵押预计时间查询
    public Message queryDyYjHouse(Map<String, String> conditionmap, int page, int rows,String qssj,String zl, String bdcqzh, String bdcdyh);
	
	public Message queryDyYjTdxx(Map<String, String> conditionmap, int page, int rows,String qssj,String zl, String bdcqzh, String bdcdyh);
//  查封预计时间查询
    public Message queryCfYjHouse(Map<String, String> conditionmap, int page, int rows,String qssj,String zl, String bdcqzh, String bdcdyh);
	
	public Message queryCfYjTdxx(Map<String, String> conditionmap, int page, int rows,String qssj,String zl, String bdcqzh, String bdcdyh);


}
