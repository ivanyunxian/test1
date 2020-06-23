package com.supermap.wisdombusiness.framework.service;

import java.util.List;
import java.util.Map;

import com.supermap.wisdombusiness.framework.model.Ywlog;
import com.supermap.wisdombusiness.web.ui.Page;

/**
 * 
 * @Description:业务日志管理service接口
 * @author diaoliwei
 * @date 2016年1月15日 上午10:41:59
 * @Copyright SuperMap
 */
public interface YwlogService {
	
	/**
	 * 根据id查询日志
	 * @param id
	 * @author diaoliwei
	 * @return
	 */
	public Ywlog findById(String id);
	
	/**
	 * 查询全部
	 * @author diaoliwei
	 * @return
	 */
	public List<Ywlog> findAll();
	
	/**
	 * 查询分页列表
	 * @param pageIndex
	 * @param pageSize
	 * @param mapCondition
	 * @author diaoliwei
	 * @return
	 */
	public Page getPagedYwlog(int pageIndex, int pageSize, Map<String, Object> mapCondition);
	
	/**
	 * 导出excel
	 * @param mapCondition
	 * @return
	 */
	public List<Ywlog> logList(Map<String, Object> mapCondition);

}
