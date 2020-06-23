package com.supermap.yingtanothers.service;

import org.springframework.stereotype.Component;

import com.supermap.wisdombusiness.workflow.util.Message;

/**
 * 
 * 作者： 苗利涛
 * 时间： 2016年1月22日 下午3:00:39
 * 功能：鹰潭市不动产中间库通过不动产单元号和共享项目编号查询查封信息接口
 */
@Component("QueryCFXXService")
public interface QueryCFXXService {

	public Message QueryCfxxInfoByBDCDYH(String bdcdyh, String gxxmbh,Integer page,Integer rows);
}
