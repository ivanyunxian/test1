package com.supermap.wisdombusiness.framework.dao;

import java.util.List;

import com.supermap.wisdombusiness.framework.model.RoleModule;

/**
 * 
 * @Description:角色模块管理dao接口
 * @author 杨鹏
 * @date 2015年7月10日 下午2:41:16
 * @Copyright SuperMap
 */
public interface RoleModuleDao extends GenericDao<RoleModule, String> {
		
	/**
	 * 根据角色id获取角色模块
	 * @作者 杨鹏
	 * @创建时间 2015年7月10日下午2:41:36
	 * @param roleId
	 * @return
	 */
	public List<String> findRoleModuleIdByRoleId(String roleId);
	
	/**
	 * 根据角色id获取模块
	 * @作者 杨鹏
	 * @创建时间 2015年7月10日下午2:41:49
	 * @param roleId
	 * @return
	 */
	public List<String> findModuleIdByRoleId(String roleId);
	
}
