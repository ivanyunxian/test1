package com.supermap.realestate.registration.service;

/**
 * 不动产登簿与平台接入日志上报上报接口
 * @author weilb
 */
public interface LogExchangeInfoService {
	/**
	 * 登簿与接入日志，生成日志XML报文
	 * @author weilb
	 * @param registrationLogXml 
	 * @return  XML format error  xml格式错误
	 * @return  Missing  AreaCode  element  行政代码元素缺失
	 * @return  AreaCode is null     行政代码元素值为空
	 * @return  Error|firstReg value is illegal  首次登记属性值非法
	 * @return  Error|Register book number wrong,the sum is  登簿数量不等于各业务类型数量和
	 * @return  inconsistent with the items  与项目不一致
	 * @return  success  上报成功
	 */
	public String exchangeInfo (String path); //String registrationLogXml
}
