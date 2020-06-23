package com.supermap.realestate.registration.service;

import java.util.List;

import com.supermap.realestate.registration.model.BDCS_SFDY;
import com.supermap.wisdombusiness.web.ui.Page;

/**
 * 
 * @Description:登记收费service
 * @author 郭浩龙
 * @date 2015年7月22日 晚上23:54:12
 * @Copyright SuperMap
 */

public interface DJSFService {

	/**
	 * 获取所有收费定义 不分页
	 * 
	 * @return
	 */
	public List<BDCS_SFDY> GetSFDY();

	/**
	 * 根据流程id得到收费信息
	 * 
	 * @param prodefid
	 * @return
	 */
	public List<BDCS_SFDY> GetSF(String prodefid);

	/**
	 * 获取所有收费定义 分页
	 * 
	 * @return
	 */
	public Page GetSFDY(int page, int rows);

	/**
	 * //根据流程id和所选择的收费项目的id 去更新关系表
	 * 
	 * @作者 郭浩龙
	 * @创建时间 2015年7月25日 下午17:55:12
	 * @param prodefid
	 * @param dyid
	 */
	public boolean addSFRela(String prodefid, String dyid);

	/**
	 * 收费关系表中删除对应关系 预留 目前方式是：在更新关系表时，先删除全部，再执行插入操作
	 * 
	 * @作者 郭浩龙
	 * @创建时间 2015年7月23日 下午15:25:12
	 * @param prodefid
	 * @param dyid
	 */
	public boolean deleteSFRela(String prodefid);

	/**
	 * 维护收费定义表 -添加记录
	 * 
	 * @作者 郭浩龙
	 * @创建时间 2015年7月26日 下午16:38:12
	 * @param
	 * @param
	 */			
	public boolean AddSFDY(BDCS_SFDY sfdy);

	/**
	 * 维护收费定义表 -修改记录
	 * @作者 郭浩龙
	 * @创建时间 2015年7月27日 上午12:03:12
	 * @param sfdlmc
	 * @return
	 */
	public boolean EditSFDY(BDCS_SFDY sfdy);
	
	/**
	 * 删除收费定义表中数据
	 * 
	 * @作者 郭浩龙
	 * @创建时间 2015年7月27日 下午15:25:12
	 * @param dyid
	 */
	public String deleteSFDY(String sfdyid);
	
}


