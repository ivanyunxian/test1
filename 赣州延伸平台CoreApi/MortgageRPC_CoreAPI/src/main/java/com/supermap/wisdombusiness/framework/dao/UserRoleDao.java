package com.supermap.wisdombusiness.framework.dao;

import java.util.List;

import com.supermap.wisdombusiness.framework.model.Role;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.model.UserRole;

/**
 * 
 * @Description:用户角色管理dao接口
 * @author 杨鹏
 * @date 2015年7月10日 下午2:08:56
 * @Copyright SuperMap
 */
public interface UserRoleDao extends GenericDao<UserRole, String> {
	
	/**
	 * 根据用户id获取角色id列表
	 * @作者 杨鹏
	 * @创建时间 2015年7月10日下午2:09:16
	 * @param userId
	 * @return
	 */
	public List<String> findRoleIdByUserId(String userId);
	
	/**
	 * 根据用户id获取用户角色关系id列表
	 * @作者 杨鹏
	 * @创建时间 2015年7月10日下午2:09:37
	 * @param userId
	 * @return
	 */
	public List<String> findUserRoleIdByUserId(String userId);
	
	/**
	 * 根据角色id获取用户角色关系id列表
	 * @作者 杨鹏
	 * @创建时间 2015年7月10日下午2:10:10
	 * @param roleId
	 * @return
	 */
	public List<String> findUserRoleIdByRoleId(String roleId);
	
	/**
	 * 根据角色id获取用户id列表
	 * @作者 杨鹏
	 * @创建时间 2015年7月10日下午2:10:29
	 * @param roleId
	 * @return
	 */
	public List<String> findUserIdByRoleId(String roleId);
	
	/**
	 * 根据角色id获取用户列表
	 * @作者 diaoliwei
	 * @创建时间 2015年7月6日上午10:15:16
	 * @param roleId
	 * @return
	 */
	public List<User> findUsersByRoleId(String roleId);
	public List<User> findUsersByRoleIdAndCode(String roleId,String Area_code);
	public List<Role> findAllRoleByUserId(String userid);
}