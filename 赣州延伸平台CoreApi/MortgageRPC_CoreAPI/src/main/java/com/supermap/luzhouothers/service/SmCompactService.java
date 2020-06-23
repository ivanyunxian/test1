package com.supermap.luzhouothers.service;

import org.springframework.stereotype.Component;

import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * 
 * @Description 本服务主要是完成读取合同、买卖双方、房屋的信息
 * 				读取合同后，将提取的信息存入户、房屋、以及申请人的服务
 * @author lxk
 * @CreateTime 2015年10月29日23:48:50
 * 
 */
@Component("SmCompactService")
public interface SmCompactService {
	
	/**
	/**
	 * 根据合同编号获取合同内容的信息
	 * @author lxk
	 * @CreateTime 2015年10月29日21:47:43
	 * @param compactNo 合同编号
	 * @return
	 
	public ResultMessage GetCompactInfobyCompactNo(String _xmbh,String compactNo);
	*/
	/**
	 * 根据合同编号中的关联字段找到合同买卖房屋的信息
	 * @author lxk
	 * @CreateTime 2015年10月29日21:50:01
	 * @param id 关联编号
	 * @return
	 
	public ResultMessage GetCompact_HInfo(String _xmbh,String id);
	*/
	/**
	 * 根据合同编号的关联字段找到具体合同买卖人员的信息
	 * @author lxk
	 * @CreateTime 2015年10月29日21:58:27
	 * @param id 关联编号
	 * @return
	 	public ResultMessage GetCompact_OwnerInfo(String _xmbh,String id);
	 */
	/**
	 * 
	 * @Description 根据项目编号和合同编号读取合同信息并且同时更新到不动产库中
	 * @author lxk
	 * @param _xmbh 项目编号
	 * @param _compactNo 合同编号
	 * @createTime 2015年10月30日15:47:22
	 */
	public ResultMessage ReadCompactInfo(String xmbh,String compactNo);
}
