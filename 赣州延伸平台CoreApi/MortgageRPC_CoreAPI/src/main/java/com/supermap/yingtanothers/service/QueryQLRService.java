package com.supermap.yingtanothers.service;

import org.springframework.stereotype.Component;

import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.util.Message;

/**
 * 
 * 作者： 苗利涛
 * 时间： 2016年1月18日 下午8:27:10
 * 功能：根据权利人姓名查询权利人接口
 */
@Component("QueryQLRService")
public interface QueryQLRService {


	public Message QueryQLRByQlrxm(String sqrxm,Integer page,Integer rows);
	public ResultMessage QueryQLRByGxxmbh(String gxxmbh, String xmbh);
	public ResultMessage QueryDYByGxxmbh(String gxxmbh, String xmbh);
}
