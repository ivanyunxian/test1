package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
	/**
	  * 通过用户账号查询用户信息
	 * @param username
	 * @return
	 */
	public SysUser getUserByName(@Param("username") String username);

	/**
	 *  根据部门Id查询用户信息
	 * @param page
	 * @param departId
	 * @return
	 */
	IPage<SysUser> getUserByDepId(Page page, @Param("departId") String departId, @Param("username") String username);

	/**
	 * 根据角色Id查询用户信息
	 * @param page
	 * @param
	 * @return
	 */
	IPage<SysUser> getUserByRoleId(Page page, @Param("roleId") String roleId, @Param("username") String username);
	
	/**
	 * 根据用户名设置部门ID
	 * @param username
	 * @param orgCode
	 * @param deptId
	 */
	void updateUserDepart(@Param("username") String username,@Param("orgCode") String orgCode, @Param("deptId") String deptId);
	
	/**
	 * 根据手机号查询用户信息
	 * @param phone
	 * @return
	 */
	public SysUser getUserByPhone(@Param("phone") String phone);
	
	
	/**
	 * 根据邮箱查询用户信息
	 * @param email
	 * @return
	 */
	public SysUser getUserByEmail(@Param("email")String email);

	/**
	 * @Author taochunda
	 * @Description 根据部门Id查询该部门所有用户及所有子部门用户信息
	 * @Date 2019-08-08 15:15
	 * @Param [page, departId, username]
	 * @return com.baomidou.mybatisplus.core.metadata.IPage<SysUser>
	 **/
	IPage<SysUser> getUserByDepIdAll(Page page, @Param("departId") String departId);
	
	/**
	 *  根据自然幢Id查询用户信息
	 * @param page
	 * @param departId
	 * @return
	 */
	IPage<SysUser> getUserByZrzId(Page page, @Param("zrzId") String zrzId, @Param("realname") String realname);
}
