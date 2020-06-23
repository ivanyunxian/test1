package com.supermap.realestate.registration.handler;

import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.ViewClass.UnitTree;
import com.supermap.realestate.registration.config.ChargeParam;

/**
 * 
 * @Description:登记Handler接口
 * @author 刘树峰
 * @date 2015年6月15日 下午4:43:31
 * @Copyright SuperMap
 */
public interface DJHandler {

	/**
	 * 记录项目选中的不动产单元
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月12日下午5:21:39
	 * @param xmbh
	 * @param bdcdyid
	 * @return
	 */
	public boolean addBDCDY(String bdcdyid);

	/**
	 * 写入现状库和登记簿
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月12日下午5:56:11
	 * @return
	 */
	public boolean writeDJB();

	/**
	 * 移除不动产单元
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月12日下午8:12:21
	 * @param bdcdyid
	 * @return
	 */
	public boolean removeBDCDY(String bdcdyid);

	/**
	 * 获取不动产单元列表
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月13日上午12:23:33
	 * @return
	 */
	public List<UnitTree> getUnitTree();

	/**
	 * 根据申请人ID数组添加权利人
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月13日上午3:15:18
	 * @param qlid
	 * @param sqrids
	 */
	public void addQLRbySQRArray(String qlid, Object[] sqrids);

	/**
	 * 移除权利人
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月13日上午4:09:52
	 * @param qlid
	 * @param qlrid
	 */
	public void removeQLR(String qlid, String qlrid);

	/**
	 * 获取错误信息
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月23日下午8:09:51
	 * @return
	 */
	public String getError();

	/**
	 * 导出XML交换文件
	 * @Title: exportXML
	 * @author:liushufeng
	 * @date：2015年7月18日 下午5:21:16
	 * @param path
	 * @return
	 */
	public Map<String, String> exportXML(String path, String actinstID);

	/**
	 * 获取计费参数
	 * @Title: getChargeParam
	 * @author:liushufeng
	 * @date：2015年7月26日 上午3:51:11
	 * @return
	 */
	public ChargeParam getChargeParam();
	
	/**
	 * 共享信息
	 * @Title: getChargeParam
	 * @author:俞学斌
	 * @date：2015年8月16日下午16:37:43
	 * @return
	 */
	public void SendMsg(String bljc);


}
