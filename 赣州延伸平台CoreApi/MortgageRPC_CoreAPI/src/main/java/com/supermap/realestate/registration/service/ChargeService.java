package com.supermap.realestate.registration.service;

import parsii.tokenizer.ParseException;

/**
 * 计费服务类
 * @ClassName: ChargeService
 * @author liushufeng
 * @date 2015年7月26日 上午3:53:23
 */

public interface ChargeService {

	public boolean calculate(String xmbh) throws ParseException;

	/**
	 * 根据收费定义ID添加收费记录
	 * @Title: addSFfromDY
	 * @author:liushufeng
	 * @date：2015年7月28日 上午5:14:07
	 * @param xmbh
	 * @param sfdyids
	 * @return
	 */
	public boolean addSFfromDY(String xmbh, String sfdyids);
}
