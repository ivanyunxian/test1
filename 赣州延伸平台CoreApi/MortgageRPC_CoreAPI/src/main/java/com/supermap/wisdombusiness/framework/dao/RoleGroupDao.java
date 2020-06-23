package com.supermap.wisdombusiness.framework.dao;

import java.util.List;
import java.util.Map;

import com.supermap.wisdombusiness.framework.model.RoleGroup;
import com.supermap.wisdombusiness.web.ui.Page;

/**
 * 
 * @Description:角色分组管理dao接口
 * @author 杨鹏
 * @date 2015年7月10日 下午2:42:11
 * @Copyright SuperMap
 */
public interface RoleGroupDao extends GenericDao<RoleGroup, String> {
	
	public List<RoleGroup> findAll();
	
	/**
	 * 分页查询角色分组
	 * @作者 杨鹏
	 * @创建时间 2015年7月10日下午2:42:43
	 * @param pageIndex
	 * @param pageSize
	 * @param mapCondition
	 * @return
	 */
	public Page getPagedRoleGroup(int pageIndex, int pageSize,
			Map<String, Object> mapCondition);

}
