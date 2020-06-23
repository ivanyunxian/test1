/**
 * 广西统计服务
 */
package com.supermap.realestate_gx.registration.service;


import java.util.List;
import java.util.Map;

import com.supermap.wisdombusiness.web.Message;

public interface GX_GZTJService {
	
	/**
	 * 科室办件量统计报表
	 * @param tjsjks 开始统计时间 
	 * @param tjsjks 截止统计时间  
	 * @return
	 */
	public Message GetKSYWTJ(String tjsjks,String tjsjjz,String deptid, String username);
	/**
	 * 登记中心办件量统计报表
	 * @param tjsjks 开始统计时间 
	 * @param tjsjks 截止统计时间  
	 * @return
	 */
	public Message GetDJZXYWTJ(String tjsjks,String tjsjjz);
	
	/**
	 * @author liangc
	 * 获取部门下的员工
	 * @return
	 */
	public List<Map> GetDeptStaffname(String deptid);
	
	/**
	 * 办件量统计
	 * @return
	 */
	public Message GetBJLTJ(Map<String, String> mapCondition);
	
	/**
	* 获取月业务办理量
	* 
	*@param mapCondition 条件集
	*@author hpf
	*@date 20180328
	*@return
	*/
	public Message getDataYYWBLLTJ(Map<String, Object> mapCondition);
	/**
	* 获取半个月办件量
	* 
	*@param mapCondition 条件集
	*@return
	*@author hpf
	*@date 20180330
	*/
	public Message getDataBYBJLTJ(Map<String, Object> mapCondition);
	/**
	* 获取中心业务登簿量
	* 
	*@param mapCondition 条件集
	*@return
	*@author hpf
	*@date 20180402
	*/
	public Message getDataZXYWDBLTJ(Map<String, Object> mapCondition);
	/**
	* 柳州推送税局、住建统计
	* 
	*@param mapCondition 条件集
	*@author hpf
	*@date 20180402
	*/
	public Message getDataTSSJZJTJ(Map<String, Object> mapCondition);
	/**
	* 柳州报件、自助、批量统计
	* 
	*@param mapCondition 条件集
	*@author hpf
	*@date 20180402
	*/
	public Message getDataBJZZPLTJ(Map<String, Object> mapCondition);
	/**
	* 柳州雒容、柳地、南铁房换证登记统计
	*
	*@author hpf
	*@date 20180402
	*/
	public Message getDataLRLDNTTJ(Map<String, Object> mapCondition);
	
	/**
	 * 办结统计（按月）
	 * @return
	 */
	public Message getBanJie_TJ(String startTime ,String endTime);
	/**
	 * 超期详情
	 * @param startTime
	 * @param endTime
	 * @param djlx
	 * @param qllx
	 * @return
	 */
	public Message getBanJie_CQXQ( String startTime, String endTime,String djlx,String qllx);
	
}
