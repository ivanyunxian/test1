package org.jeecg.modules.system.service;

import java.util.List;
import java.util.Set;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.SysUserCacheInfo;
import org.jeecg.modules.system.entity.SysUser;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
public interface ISysUserService extends IService<SysUser> {
	
	public SysUser getUserByName(String username);
	
	/**
	 * 添加用户和用户角色关系
	 * @param user
	 * @param roles
	 */
	public void addUserWithRole(SysUser user,String roles);
	
	
	/**
	 * 修改用户和用户角色关系
	 * @param user
	 * @param roles
	 */
	public void editUserWithRole(SysUser user,String roles);

	/**
	 * 获取用户的授权角色
	 * @param username
	 * @return
	 */
	public List<String> getRole(String username);
	
	/**
	  * 查询用户信息包括 部门信息
	 * @param username
	 * @return
	 */
	public SysUserCacheInfo getCacheUser(String username);

	/**
	 * 根据部门Id查询
	 * @param
	 * @return
	 */
	public IPage<SysUser> getUserByDepId(Page<SysUser> page, String departId, String username);

	/**
	 * 根据角色Id查询
	 * @param
	 * @return
	 */
	public IPage<SysUser> getUserByRoleId(Page<SysUser> page,String roleId, String username);

	/**
	 * 通过用户名获取用户角色集合
	 *
	 * @param username 用户名
	 * @return 角色集合
	 */
	Set<String> getUserRolesSet(String username);

	/**
	 * 通过用户名获取用户权限集合
	 *
	 * @param username 用户名
	 * @return 权限集合
	 */
	Set<String> getUserPermissionsSet(String username);
	
	/**
	 * 根据用户名设置部门ID
	 * @param username
	 * @param orgCode
	 */
	void updateUserDepart(String username,String orgCode, String deptId);
	
	/**
	 * 根据手机号获取用户名和密码
	 */
	public SysUser getUserByPhone(String phone);


	/**
	 * 根据邮箱获取用户
	 */
	public SysUser getUserByEmail(String email);


	/**
	 * 添加用户和用户部门关系
	 * @param user
	 * @param selectedParts
	 */
	void addUserWithDepart(SysUser user, String selectedParts);

	/**
	 * 编辑用户和用户部门关系
	 * @param user
	 * @param departs
	 */
	void editUserWithDepart(SysUser user, String departs);
	
	/**
	   * 校验用户是否有效
	 * @param sysUser
	 * @return
	 */
	Result checkUserIsEffective(SysUser sysUser);

	/**
	 * 单纯校验用户是否有效
	 * @param sysUser
	 * @return
	 */
	Result checkUserIsEffectiveNoLog(SysUser sysUser);


	/**
	 * @Author taochunda
	 * @Description 根据部门Id查询该部门所有用户及所有子部门用户信息
	 * @Date 2019-08-08 15:14
	 * @Param [page, departId, username]
	 * @return com.baomidou.mybatisplus.core.metadata.IPage<SysUser>
	 **/
	public IPage<SysUser> getUserByDepIdAll(Page<SysUser> page, String deptId);

	public IPage<SysUser> getUserByZrzId(Page<SysUser> page, String zrzId, String realname);
}
