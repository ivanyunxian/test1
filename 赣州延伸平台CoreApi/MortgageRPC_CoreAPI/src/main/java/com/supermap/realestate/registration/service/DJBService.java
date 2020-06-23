package com.supermap.realestate.registration.service;

import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.ViewClass.DJBDTO;
import com.supermap.realestate.registration.ViewClass.FDCQDB;
import com.supermap.realestate.registration.ViewClass.HYDB;
import com.supermap.realestate.registration.ViewClass.NYDDB;
import com.supermap.realestate.registration.ViewClass.NoticeBook;
import com.supermap.realestate.registration.ViewClass.SLLMDB;
import com.supermap.realestate.registration.ViewClass.SYQZD;
import com.supermap.realestate.registration.model.BDCS_C_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * 
 * @Description:登记薄service 登记簿相关的服务都放到里边
 * @author diaoliwei
 * @date 2015年6月12日 下午3:11:56
 * @Copyright SuperMap
 */
public interface DJBService {

	/**
	 * 获取海域登簿信息
	 * @作者 海豹
	 * @创建时间 2015年6月26日上午5:31:01
	 * @param xmbh
	 * @param qlid
	 * @return
	 */
	public HYDB GetHYDB(String xmbh, String qlid);
	
	/**
	 * 获取森林林木登记信息
	 * @作者 海豹
	 * @创建时间 2015年6月27日下午11:09:20
	 * @param xmbh
	 * @param qlid
	 * @return
	 */
	public SLLMDB GetSLLMDB(String xmbh, String qlid);
	
	/**
	 * 集体土地所有权登簿
	 * @作者 海豹
	 * @创建时间 2015年6月26日下午6:07:43
	 * @param xmbh
	 * @param qlid
	 * @return
	 */
	public SYQZD GetJTTDSYQDB(String xmbh, String qlid);
	
	/**
	 * 国有农用地使用权登簿
	 * @param xmbh
	 * @param qlid
	 * @return
	 */
	public NYDDB GetNYDSHYQDB(String xmbh, String qlid);
	
	
	
	/**
	 * 获取某个单元的登记簿信息
	 * 
	 * @author diaoliwei
	 * @return
	 */
	public DJBDTO GetDJB(String xmbh, String qlid);

	/**
	 * 获取房地产权登记信息（独幢、层、套、间房间）
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月9日上午1:49:29
	 * @param xmbh
	 * @param djdyid
	 * @return
	 */
	public FDCQDB getFDCQDB(String xmbh, String djdyid);

	/**
	 * 获取房地产权登记簿页信息:组合业务：初始登记+_房屋所有权_在建工程转现登记（移除抵押权对应的权利人信息）
	 * @作者 海豹
	 * @创建时间 2015年7月26日上午2:03:09
	 * @param xmbh
	 * @param djdyid
	 * @return
	 */
	public FDCQDB getCombFDCQDB(String xmbh, String djdyid);
	/**
	 * 获取现状库中使用权宗地信息
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月9日上午5:10:03
	 * @param bdcdyid
	 * @return
	 */
	public BDCS_SHYQZD_XZ GetSHYQZDInfo(String bdcdyid);

	/**
	 * 获取现状库中层信息
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月9日上午5:14:36
	 * @param cid
	 * @return
	 */
	public BDCS_C_XZ GetCInfo(String cid);

	/**
	 * 获取现状库中自然幢信息
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月9日上午5:15:06
	 * @param bdcdyid
	 * @return
	 */
	public BDCS_ZRZ_XZ GetZRZInfo(String bdcdyid);

	/**
	 * 获取现状库中的户信息
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月9日上午5:16:00
	 * @param bdcdyid
	 * @return
	 */
	public BDCS_H_XZ GetHInfo(String bdcdyid);

	/**
	 * 生成不动产权证号
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月14日上午5:17:07
	 * @param xmbh
	 * @param qlid
	 * @return
	 */
	public String createBDCQZH(String xmbh, String qlid, String type);

	/**
	 * 生成不动产登记证明号
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月15日上午4:56:09
	 * @param xmbh
	 * @param qlid
	 * @return
	 */
	public String createBDCZMH(String xmbh, String qlid, String type);

	/**
	 * 获取抵押权登记簿信息
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月15日上午3:15:37
	 * @param xmbh
	 * @param djdyid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map getDYQDJBInfo(String xmbh, String qlid);
	
	/**
	 * 获取地役权登记簿信息
	 * @param xmbh
	 * @param qlid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map getDyqDjbInfos(String xmbh, String qlid);
	
	/**
	 * 获取查封登记簿信息
	 * 
	 * @作者 wuz
	 * @创建时间 2015年6月15日上午3:15:37
	 * @param xmbh
	 * @param qlid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map getCFDJBInfo(String xmbh, String qlid);

	/**
	 * 获取解封登记登记簿信息
	 * @param xmbh
	 * @param qlid
	 * @param oldqlid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map getJFDJBInfo(String xmbh, String qlid, String oldqlid);
	
	/**
	 * 获取预告登记登记簿信息
	 * 
	 * @作者 李想
	 * @创建时间 2015年9月1日上午1:35:29
	 * @param xmbh
	 * @param qlid
	 * @return
	 */
	public NoticeBook getNoticeBookByQLID(String xmbh, String qlid);
	
	/**
	 * 获取预告登记登记簿信息
	 * 
	 * @作者 李想
	 * @创建时间 2015年6月24日上午14:34:29
	 * @param xmbh
	 * @param djdyid
	 * @return
	 */
	public NoticeBook getNoticeBook(String xmbh, String djdyid);

	/**
	 * 获取异议登记权利的登记簿信息
	 * @作者：俞学斌
	 * @创建时间 2015年6月30日 下午5:10:01 
	 * @param xmbh
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map getYYDJBInfo(String xmbh, String qlid);

	/**
	 * 
	* @Description: 根据权利类型生成不动产权证号
	* @date 2015年9月2日 上午1:03:47
	* @author yuxuebin
	* @param xmbh
	* @param qllx
	* @param type
	* @return
	 */
	public String createBDCQZHByQLLX(String xmbh, String qllx, String type);

	/**
	 * 
	* @Description: 根据权利类型生成不动产登记证明号
	* @date 2015年9月2日 上午1:04:45
	* @author yuxuebin
	* @param xmbh
	* @param qllx
	* @param type
	* @return
	 */
	public String createBDCZMHByQLLX(String xmbh, String qllx, String type);

	/**
	 * 获取解除限制登记簿预览信息
	 * 
	 * @作者 yuxuebin
	 * @param xmbh
	 * @param bdcdyid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getZXXZDJBInfo(String xmbh, String bdcdyid);

	public ResultMessage getQZHORZMHByXMBH(String xmbh);
	
	/**
	 * 通过项目编号检查权利表是否有对应的权利人
	 * @param xmbh
	 * @return
	 */
	public  ResultMessage checkQlrByQl(String xmbh);

	/**
	 * 根据project_id获取 流程编号进行判断  登簿是否重新生成权证号
	 * @param project_id
	 * @return  msg
	 */
	public ResultMessage isNewQZH(String project_id);
	
	public Map getZXDJBInfo(String xmbh, String bdcdyid);

}
