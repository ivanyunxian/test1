package com.supermap.wisdombusiness.framework.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.supermap.wisdombusiness.framework.model.Role;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.web.ui.tree.Tree;

/**
 * 
 * @Description:角色service接口
 * @author yangpeng
 * @date 2015年7月6日 上午10:51:24
 * @Copyright SuperMap
 */
public interface RoleService {
	
	/**
	 * 根据id获取角色信息
	 * @作者 yangpeng
	 * @param id
	 * @return
	 */
	public Role findById(String id);
	
	/**
	 * 查询全部
	 * @作者 yangpeng
	 * @return
	 */
	public List<Role> findAll();
	
	/**
	 * 获取角色分页信息
	 * @作者 yangpeng
	 * @创建时间 2015年7月6日上午10:50:23
	 * @param pageIndex
	 * @param pageSize
	 * @param mapCondition
	 * @return
	 */
	public Page getPagedRole(int pageIndex, int pageSize,
			Map<String, Object> mapCondition);
	
	/**
	 * 根据角色获取用户分页信息
	 * @作者 yangpeng
	 * @param pageIndex
	 * @param pageSize
	 * @param mapCondition
	 * @return
	 */
	public Page getPagedUserByRole(int pageIndex, int pageSize,
			Map<String, Object> mapCondition);
	
	/**
	 * 新增角色信息
	 * @作者 yangpeng
	 * @param role
	 */
	public void saveRole(Role role);
	
	/**
	 * 根据id删除角色信息
	 * @作者 yangpeng
	 * @param id
	 */
	public void deleteRoleById(String id);
	
	/**
	 * 更新角色信息
	 * @作者 yangpeng
	 * @param role
	 */
	public void updateRole(Role role);
	
	/**
	 * 根据登录名获取所属角色
	 * @作者 yangpeng
	 * @创建时间 2015年7月6日上午10:50:46
	 * @param loginName
	 * @return
	 */
	public List <String> getRolesByLoginName(String loginName);
	
	public void createRoleUserRecords(String roleId, String userIds);
	
	public void createRoleModuleRecords(String roleId, String moduleIds);
	
	/**
	 * 根据角色id获取模型
	 * @作者 yangpeng
	 * @param roleId
	 * @return
	 */
	public List<String> findModuleIdByRoleId(String roleId);
	
	/**
	 * 根据角色id获取用户id列表
	 * @作者 yangpeng
	 * @param roleId
	 * @return
	 */
	public List<String> findUserIdByRoleId(String roleId);
	
	/**
	 * 根据角色id获取用户列表
	 * @作者 diaoliwei
	 * @param roleId
	 * @return
	 */
	public List<User> findUsersByRoleId(String roleId);
	
	/**
	 * 获取角色树
	 * @作者 yangpeng
	 * @return
	 */
	public List<Tree>getRoleTree();
	
	/**
	 * 根据用户id获取角色
	 * @作者 yangpeng
	 * @param id
	 * @return
	 */
	public List <String> getRolesByUserId(String id);

	public List<User> findUsersByRoleIdAndCode(String roleId, String Areacode);
	
	public List<Role> getAllRoleByUserId(String id);
	
	public List<User> getUsers();
	
	/**
	 * 对比角色中人员的差异
	 * @param roleid
	 * @param staffids
	 * @return
	 */
	public Map<String, List<String>> CompareDiffStaffInRole(String roleid,String staffids);
	
	/**
	 * 根据角色编码查询角色
	 * @date   2017年2月6日 下午4:23:55
	 * @author JHX
	 * @param  roleCode
	 * @return
	 */
	public List<Role> getRoleByRoleCode(String roleCode);
}