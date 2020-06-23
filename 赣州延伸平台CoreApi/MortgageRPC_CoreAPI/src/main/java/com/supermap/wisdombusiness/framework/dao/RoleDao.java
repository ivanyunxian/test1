package com.supermap.wisdombusiness.framework.dao;

import java.util.List;
import java.util.Map;

import com.supermap.wisdombusiness.framework.model.Role;
import com.supermap.wisdombusiness.web.ui.Page;

/**
 * 
 * @Description:角色管理dao接口
 * @author 杨鹏
 * @date 2015年7月10日 下午2:43:04
 * @Copyright SuperMap
 */
public interface RoleDao extends GenericDao<Role, String> {
	
	/**
	 * 分页查询角色信息
	 * @作者 杨鹏
	 * @创建时间 2015年7月10日下午2:43:19
	 * @param pageIndex
	 * @param pageSize
	 * @param mapCondition
	 * @return
	 */
	public Page getPagedRole(int pageIndex, int pageSize,
			Map<String, Object> mapCondition);
	
	/**
	 * 根据登录名获取角色
	 * @作者 杨鹏
	 * @创建时间 2015年7月10日下午2:43:33
	 * @param loginName
	 * @return
	 */
	public List <String> getRolesByLoginName(String loginName);

	/**
	 * 根据角色分页查询用户信息
	 * @作者 杨鹏
	 * @创建时间 2015年7月10日下午2:43:46
	 * @param pageIndex
	 * @param pageSize
	 * @param mapCondition
	 * @return
	 */
	public Page getPagedUserByRole(int pageIndex, int pageSize,
			Map<String, Object> mapCondition);
	
	/**
	 * 根据分组id获取角色列表
	 * @作者 杨鹏
	 * @创建时间 2015年7月10日下午2:44:13
	 * @param id
	 * @return
	 */
	public List <Role> getRolesByGroupId(String id);
	
	/**
	 * 根据角色编码获取角色
	 * @date   2017年2月6日 下午4:26:45
	 * @author JHX
	 * @param roleCode
	 * @return
	 */
	public List<Role> getRoleByRoleCode(String roleCode);
	
}
