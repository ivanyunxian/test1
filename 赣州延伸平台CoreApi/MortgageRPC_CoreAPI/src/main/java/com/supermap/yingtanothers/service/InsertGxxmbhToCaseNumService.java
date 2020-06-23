package com.supermap.yingtanothers.service;

import org.springframework.stereotype.Component;

import com.supermap.wisdombusiness.workflow.util.Message;

/**
 * 
 * 作者： 苗利涛
 * 时间： 2016年4月15日 上午11:37:16
 * 功能：和房管局对接，把共享项目编号插入到casenum字段接口
 */
@Component("InsertGxxmbhToCaseNumService")
public interface InsertGxxmbhToCaseNumService {

	public Message InsertGxxmbhToCaseNum(String gxxmbh, String xmbh);
}
