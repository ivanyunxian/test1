package com.supermap.yingtanothers.service;

import org.springframework.stereotype.Component;

import com.supermap.wisdombusiness.workflow.util.Message;

/**
 * 
 * 作者： 苗利涛
 * 时间： 2016年1月19日 上午10:21:45
 * 功能：鹰潭市不动产中间库通过不动产单元号和共享项目编号查询抵押权信息接口
 */
@Component("QueryDYAQXXService")
public interface QueryDYAQXXService {

	public Message QueryDyaqxxInfoByGXXMBH(String sqrxm,String bdcdyh, String gxxmbh,Integer page,Integer rows);
}
