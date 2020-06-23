package com.supermap.realestate.registration.service;

import java.util.Map;

import com.supermap.realestate.registration.ViewClass.ZS;

/**
 * 
 * @Description:证书service
 * @author 李桐
 * @date 2015年6月12日 下午3:11:27
 * @Copyright SuperMap
 */
public interface ZSService2 {

	/**
	 * 获取证书表单信息
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月7日下午10:07:49
	 * @param xmbh
	 *            项目编号
	 * @param zsid
	 *            证书ID
	 * @return 证书表单信息
	 */
	public ZS getZSForm(String xmbh, String zsid);

	/**
	 * 多个单元获取不动产登记证明
	 * @Title: getBDCDJZM
	 * @author:liushufeng
	 * @date：2015年11月21日 下午10:55:58
	 * @param xmbh
	 * @param zsid
	 * @return
	 */
	public Map getBDCDJZM(String xmbh, String zsid);
}
