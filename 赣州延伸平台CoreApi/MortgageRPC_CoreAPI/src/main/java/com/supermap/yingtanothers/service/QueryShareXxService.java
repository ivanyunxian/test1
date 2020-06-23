package com.supermap.yingtanothers.service;

import org.springframework.stereotype.Component;

/**
 * 
 * 作者： 苗利涛
 * 时间： 2016年6月20日 上午11:17:45
 * 功能：鹰潭市一键式提取房管局共享信息控制接口
 */
@Component("QueryShareXxService")
public interface QueryShareXxService {

	public String ExtractSXFromZJK(String ywh, String xmbh, boolean bool);

	public String AddDYBDCDY(String ywh, String xmbh);

	public boolean ExtractFJFromZJK(String proinstId, String caseNum, String configFilePath);
}
