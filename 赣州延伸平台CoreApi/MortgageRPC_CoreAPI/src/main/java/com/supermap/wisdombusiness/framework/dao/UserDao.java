package com.supermap.wisdombusiness.framework.dao;

import java.util.List;
import java.util.Map;

import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.web.ui.Page;

/**
 * 
 * @Description:用户管理dao接口
 * @author chenhl
 * @date 2015年7月10日 下午2:11:12
 * @Copyright SuperMap
 */
public interface UserDao extends GenericDao<User, String> {
	
	/**
	 * 获取分页信息
	 * @作者 chenhl
	 * @创建时间 2015年7月10日下午2:11:29
	 * @param pageIndex
	 * @param pageSize
	 * @param mapCondition
	 * @return
	 */
	public Page getPagedUser(int pageIndex, int pageSize,
			Map<String, Object> mapCondition);
	
	/**
	 * 根据登录名获取用户信息
	 * @作者 chenhl
	 * @创建时间 2015年7月10日下午2:11:40
	 * @param loginName
	 * @return
	 */
	public User findByLoginName(String loginName);
	
	/**
	 * 更新用户信息
	 * @作者 chenhl
	 * @创建时间 2015年7月10日下午2:36:15
	 * @param user
	 */
	public void update(User user);
	
	/**
	 * 查询部门下是否包含用户
	 * @param departmentId
	 * @author diaoliwei
	 * @return
	 */
	public int findByDepartment(String departmentId);
	
	/**
	 * 根据部门获取用户列表
	 * @作者 chenhl
	 * @创建时间 2015年7月10日下午2:36:27
	 * @param departmentId
	 * @return
	 */
	public List<User> findUserByDepartmentId(String departmentId);
	
	/**
	 * 获取没有所属部门的用户列表
	 * @作者 chenhl
	 * @创建时间 2015年7月10日下午2:36:47
	 * @return
	 */
	public List<User> findUserWithNoDepartmentId();
	
	/**
	 * 根据认证cn获取用户
	 * @author dff
	 * @param userCN
	 * @return
	 */
	public User findUserByCN(String userCN);

}
