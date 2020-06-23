package com.supermap.realestate.registration.service;

import java.util.Map;

import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * 
 * @Description:登薄service 登簿相关的服务都放到里边
 * @author 俞学斌
 * @date 2015年6月12日 下午3:12:22
 * @Copyright SuperMap
 */
public interface DBService {

	/**
	 * 登簿调用接口
	 * @作者：俞学斌
	 * @param xmbh
	 *            项目编号
	 * @return
	 */
	public ResultMessage BoardBook(String xmbh) throws Exception;

	/**
	 * 登簿接口，忽略警告
	 * @Title: boardBookIgnorWarning
	 * @author:liushufeng
	 * @date：2016年1月11日 下午10:01:37
	 * @param xmbh
	 * @return
	 * @throws Exception;
	 */
	public ResultMessage boardBookIgnorWarning(String xmbh) throws Exception;

	/**
	 * 共享信息调用接口
	 * @作者：俞学斌
	 * @param xmbh
	 *            项目编号
	 * @return
	 */
	public ResultMessage SendMsg(String xmbh, String bljc) throws Exception;
	
	/**
	 * 打印证书共享信息调用接口
	 * @作者：胡加红
	 * @param xmbh 项目编号
	 * @param zsid 证书id
	 *            
	 * @return
	 */
	public ResultMessage PrintZSSendMsg(String xmbh, String zsid) throws Exception;
	
	/**
	 * 调用移动SMS服务接口
	 * @作者：胡加红
	 * @param project_id 流程编号
	 * @param smsIp sms服务器ip+port
	 *            
	 * @return
	 */
	public ResultMessage SendSmsMsg(String project_id, String smsIp) throws Exception;
	/**
	 * 批量推送数据到中间库
	 * @作者 likun
	 * @创建时间 2016年1月26日下午5:02:28
	 * @param bljc
	 */
	public void PushBatchData();

	/**
	 * 单个推送数据到中间库
	 * @作者 likun
	 * @创建时间 2016年1月26日下午5:02:28
	 * @param bljc
	 */
	public void Pushsingledata(String projectid);
	
	/**
	 * 单个推送数据到中间库
	 * @作者 likun
	 * @创建时间 2016年1月26日下午5:02:28
	 * @param bljc
	 */
	public void PushArchivesState(String projectid);

	/**
	 * 根据活动实例ID获取XMBH
	 * @作者：俞学斌
	 * @param Actinst_Id 活动实例ID
	 * @return
	 */
	public String getXMBHFromActinst_Id(String Actinst_Id);

	/**
	 * 更新推送失败数据
	 * @作者 李堃
	 * @创建时间 2016年5月19日下午4:40:45
	 */
	public void refreshfail() ;

	/**
	 * 供齐齐哈尔房产系统调用回写更新新Relationid
	 * 
	 * @作者：卜晓波  
	 * @param casenum
	 *            项目编号
	 * @param fwzt
	 *            房屋状态
	 * @param relationid
	 *            旧relationid
	 * @param nrelationid
	 *            新relationid
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> UPDATERELATIONID(String fwzt,
			String relationid, String nrelationid);

	public void pushdata(String projectids,String bljd) ;
	/**
	 * nowhere条件
	 * @作者 think
	 * @创建时间 2016年11月8日下午12:48:04
	 * @param nowhere
	 */
	public void PushBatchDataInFail(String nowhere) ;
	/**
	 * 只更新限制类推送失败数据
	 * @作者 李堃
	 * @创建时间 2016年5月19日下午4:40:45
	 */
	public void refreshXZfail() ;

	public ResultMessage BDCKPushToDJK(String projectID, String bljc);

	/** 
	* @author  buxiaobo
	* @date 创建时间：2017年5月8日 下午5:41:35 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return  
	*/
	public ResultMessage Relationdata();

	/** 
	* @author  buxiaobo
	* @date 创建时间：2017年5月24日 上午11:04:12 
	* @version 1.0 
	 * @param xzdj 
	 * @param xzly 
	* @parameter  
	* @since  
	* @return  
	*/
	public ResultMessage qjSendMessage(String xzdj, String xzly);
	public ResultMessage isTheLastDY(String xmbh);
	public Map<String, String> read ();
}
