package com.supermap.wisdombusiness.framework.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.web.ui.tree.Tree;
import com.supermap.wisdombusiness.workflow.model.Wfd_Actdef;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInstStaff;
import com.supermap.wisdombusiness.workflow.model.Wfi_PassWork;

/**
 * 
 * @Description:用户Service
 * @author chenhl
 * @date 2015年7月10日 下午1:54:13
 * @Copyright SuperMap
 */
public interface UserService {
	
	/**
	 * 获取全部用户信息list
	 * @作者 chenhl
	 * @创建时间 2015年7月10日下午1:54:35
	 * @return
	 */
	public List<User> findAll();
	
	/**
	 * 分页查询用信息
	 * @作者 chenhl
	 * @创建时间 2015年7月10日下午1:54:58
	 * @param pageIndex
	 * @param pageSize
	 * @param mapCondition
	 * @return
	 */
	public Page getPagedUser(int pageIndex, int pageSize,
			Map<String, Object> mapCondition);
	
	/**
	 * 保存用户信息
	 * @作者 chenhl
	 * @创建时间 2015年7月10日下午1:55:11
	 * @param user
	 */
	public void saveUser(User user);
	
	/**
	 * 根据id删除用户
	 * @作者 chenhl
	 * @创建时间 2015年7月10日下午1:55:25
	 * @param id
	 */
	public void deleteUserById(String id);
	
	/**
	 * 更新用户信息
	 * @作者 chenhl
	 * @创建时间 2015年7月10日下午1:55:48
	 * @param user
	 */
	public void updateUser(User user);
	
	/**
	 * 根据登录名获取用户信息
	 * @作者 chenhl
	 * @创建时间 2015年7月10日下午1:56:02
	 * @param loginName
	 * @return
	 */
	public User findByLoginName(String loginName);
	
	/**
	 * 根据id查询用户信息
	 * @作者 chenhl
	 * @创建时间 2015年7月10日下午1:56:21
	 * @param id
	 * @return
	 */
	public User findById(String id);
	
	/**
	 * 重置密码
	 * @作者 chenhl
	 * @创建时间 2015年7月10日下午1:56:47
	 * @param user
	 */
	public void resetPassword(User user);
	
	/**
	 * 设置角色tree
	 * @作者 chenhl
	 * @创建时间 2015年7月10日下午1:57:04
	 * @param id
	 * @return
	 */
	public List<Tree>setRoleTree(String id);
	
	/**
	 * 保存用户角色信息
	 * @作者 chenhl
	 * @创建时间 2015年7月10日下午1:57:15
	 * @param user
	 * @param roleIds
	 */
	public void saveUserRole(User user,String roleIds );
	
	/**
	 * 根据部门获取用户列表
	 * @作者 chenhl
	 * @创建时间 2015年7月10日下午1:57:34
	 * @param departmentId
	 * @return
	 */
	public List<User> findUserByDepartmentId(String departmentId);
	
	/**
	 * 获取没有部门的用户列表
	 * @作者 chenhl
	 * @创建时间 2015年7月10日下午1:57:52
	 * @return
	 */
	public List<User> findUserWithNoDepartmentId();
	
	/**
	 * 得到当前登录用户的信息
	 * @作者 diaoliwei
	 * @创建时间 2015年7月6日上午10:23:30
	 * @return
	 */
	public User getCurrentUserInfo();
/**
 * 增加或者删除权限
 * @param staffid
 * @return
 */
 
	/**
	 * 根据认证cn获取用户
	 * @author dff
	 * @date 2017-3-28
	 * @param userCN
	 * @return User
	 */
	public User findUserByCN(String userCN);
	
	
	ResultMessage addRelation(String staffid);

    void delRelation(String staffid);
    
    public List<User> getLeaderByStaffid(String staffid);
    
    public List<Wfi_PassWork> GetPassWorkByStaffID(String staffid);
    
    public String isPassWork(String staffid, Wfd_Actdef def);
    /**
     * 对比人员拥有的角色差异
     * @author zhangp
     * @data 2017年3月29日下午4:49:50
     * @param staffid
     * @param roleids
     * @return
     */
    public Map<String, List<String>> CompareDiffRoleInStaff(String staffid,String roleids);

	public Message certinfodownload(HttpServletRequest request,
			HttpServletResponse response);

	/**
	 * 进行md5加盐验证
	 */
	public String getPassWord(String loginName, String password);

}
