package com.supermap.wisdombusiness.framework.dao;

import java.util.List;
import java.util.Map;

import com.supermap.wisdombusiness.framework.model.Ywlog;
import com.supermap.wisdombusiness.web.ui.Page;

/**
 * 
 * @Description:业务日志管理dao接口
 * @author diaoliwei
 * @date 2016年1月15日 上午12:46:39
 * @Copyright SuperMap
 */
public interface YwlogDao extends GenericDao<Ywlog, String> {

	/**
	 * 获取日志分页列表
	 * @param pageIndex
	 * @param pageSize
	 * @param mapCondition
	 * @author diaoliwei
	 * @return
	 */
	public Page getPagedYwlog(int pageIndex, int pageSize, Map<String, Object> mapCondition);
	
	/**
	 * 日志导出excel
	 * @param mapCondition
	 * @return
	 */
	public List<Ywlog> logList(Map<String, Object> mapCondition);
	
}
