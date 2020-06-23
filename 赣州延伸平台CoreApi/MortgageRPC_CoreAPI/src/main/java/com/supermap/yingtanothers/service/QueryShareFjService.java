package com.supermap.yingtanothers.service;

import org.springframework.stereotype.Component;

import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.util.Message;

/**
 * 
 * 作者： 苗利涛
 * 时间： 2016年4月13日 下午12:46:20
 * 功能：鹰潭市查询共享附件接口
 */
@Component("QueryShareFjService")
public interface QueryShareFjService {

	public Message queryQlxxInfoByGXXMBH(String file_number,String qlr,String ywh,Integer page,Integer rows);
	public ResultMessage saveCasenumByYwh(String gxxmbh,String ywh);
}
