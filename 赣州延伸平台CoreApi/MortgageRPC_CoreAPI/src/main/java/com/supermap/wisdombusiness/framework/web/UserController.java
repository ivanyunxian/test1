package com.supermap.wisdombusiness.framework.web;

import java.io.UnsupportedEncodingException;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.service.DepartmentService;
import com.supermap.wisdombusiness.framework.service.UserService;
import com.supermap.wisdombusiness.utility.StringHelper;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.ChPassword;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.web.ui.tree.Tree;
import com.supermap.wisdombusiness.workflow.service.relationmaintain.impl.RecordsMaintian;
import com.supermap.wisdombusiness.workflow.util.MD5Util;

/**
 * 
 * @Description:用户controller
 * @author chenhl
 * @date 2015年7月10日 下午1:40:36
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/framework")
public class UserController {

	private static final Log logger = LogFactory.getLog(UserController.class);

	//用户service
	@Autowired
	private UserService userService;
	@Autowired
	private RecordsMaintian recordsMaintian;

	//部门service
	@Autowired
	private DepartmentService departmentService;

	//跳转路径
	private final String prefix = "/framework/user/";

	/**
	 * 用户管理首页
	 * @作者 chenhl
	 * @创建时间 2015年7月10日下午1:41:13
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/user/index", method = RequestMethod.GET)
	public String ShowManageIndex(Model model) {
		Subject user = SecurityUtils.getSubject();
		logger.info("username=============" + user.getPrincipal().toString());
		model.addAttribute("userattribute", new User());
		YwLogUtil.addYwLog("访问:用户管理", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return prefix + "manage";
	}

	/**
	 * 获取用户列表
	 * 
	 * @param page
	 *            当前页
	 * @param rows
	 *            每页显示条数
	 * @throws UnsupportedEncodingException 
	 * */
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public @ResponseBody Message getUserList(HttpServletRequest request,
			HttpServletResponse response, User user) throws UnsupportedEncodingException {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}

		Map<String, Object> mapCondition = new HashMap<String, Object>();
		String loginName = "";
		if (request.getParameter("loginName") != null) {
//			loginName = request.getParameter("loginName");
			loginName =RequestHelper.getParam(request, "loginName");
			mapCondition.put("loginName", loginName);
		}
		String userName = "";
		if (request.getParameter("userName") != null) {
//			userName = request.getParameter("userName");
			userName =RequestHelper.getParam(request, "userName");
			mapCondition.put("userName", userName);
		}
		Page articlePaged = userService.getPagedUser(page, rows, mapCondition);
		Message m = new Message();
		m.setTotal(articlePaged.getTotalCount());
		m.setRows(articlePaged.getResult());
		return m;
	}

	/**
	 * 提交并保存用户
	 * */
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public @ResponseBody ResultMessage saveUser(
			@ModelAttribute("userattribute") User user, BindingResult result,
			SessionStatus status, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (result.hasErrors()) {
			logger.error(result);
			msg.setMsg(result.toString());
		} else {
			try {
				if (userService.findByLoginName(user.getLoginName()) != null) {
					msg.setMsg("登录名重复！");
					msg.setSuccess("false");
				} else {
					user.setDepartment(departmentService.findById(user.getDepartmentId()));
					user.setPassword(MD5Util.generate(user.getPassword()));
					userService.saveUser(user);
					status.setComplete();
					msg.setMsg(user.getId());
					msg.setSuccess("true");
					YwLogUtil.addYwLog("新增用户-成功", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
				}
			} catch (Exception e) {
				logger.error(e);
				msg.setMsg(e.getMessage());
				YwLogUtil.addYwLog("新增用户-失败", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
			}
		}
		return msg;
	}

	/**
	 * 更新用户
	 * 
	 * */
	@RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
	public @ResponseBody ResultMessage updateUser(
			@PathVariable("id") String id,
			@ModelAttribute("userattribute") User user, BindingResult result,
			SessionStatus status, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (result.hasErrors()) {
			logger.error(result);
			msg.setMsg(result.toString());
		} else {
			try {
				if (user == null)
					throw new Exception("更新用户为空");
				User existUser = userService.findById(user.getId());
				if (existUser == null)
					throw new Exception("在数据库中未查找到该用户");
				if (user.getDepartmentId() == null) {
					msg.setSuccess("true");
					msg.setMsg("部门不能为空");
					return msg;
				}
				if (StringUtils.isEmpty(user.getLoginName())) {
					msg.setSuccess("true");
					msg.setMsg("登录名不能为空");
					return msg;
				}
				if (StringUtils.isEmpty(user.getUserName())) {
					msg.setSuccess("true");
					msg.setMsg("用户名不能为空");
					return msg;
				}
				if (StringUtils.isEmpty(user.getAreaCode())) {
					msg.setSuccess("true");
					msg.setMsg("所属地区不能为空");
					return msg;
				}
				// TODO 刘树峰：还要验证用户的行政区代码正确性

				if (existUser != null) {
					existUser.setLoginName(user.getLoginName());
					existUser.setDepartment(departmentService.findById(user.getDepartmentId()));
					existUser.setMale(user.getMale());
					existUser.setMobile(user.getMobile());
					existUser.setAreaCode(user.getAreaCode());
					existUser.setRemark(user.getRemark());
					existUser.setStatus(user.getStatus());
					existUser.setTel(user.getTel());
					existUser.setUserName(user.getUserName());
					existUser.setIsLeader(user.getIsLeader());
					existUser.setIdentifyCn(user.getIdentifyCn());
					userService.updateUser(existUser);
					msg.setMsg(id);
					msg.setSuccess("true");
					YwLogUtil.addYwLog("更新用户-成功", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
				}
			} catch (Exception e) {
				logger.error(e);
				msg.setMsg(e.getMessage());
				YwLogUtil.addYwLog("更新用户-失败", ConstValue.SF.NO.Value,ConstValue.LOG.UPDATE);
			}
		}
		return msg;
	}
	@RequestMapping(value = "/userIp/{id}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage updateUserIp(@PathVariable String id,HttpServletRequest request){
		ResultMessage msg = new ResultMessage();
		String auserIp =request.getParameter("auserIp");
		User existUser = userService.findById(id);
		User currentUser = userService.getCurrentUserInfo();
		if (existUser != null&&currentUser!=null) {
			existUser.setAuserIp(auserIp);
			userService.updateUser(existUser);
			msg.setMsg(id);
			msg.setSuccess("true");
			if(existUser.getId() == currentUser.getId()||existUser.getId().equals(currentUser.getId())){
				currentUser.setAuserIp(auserIp);
			}
		}
		return msg;
		}

	/**
	 * 删除用户
	 * 
	 * */
	@RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage deleteUser(
			@PathVariable("id") String id, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (StringUtils.hasLength(id)) {
			try {
				userService.deleteUserById(id);
				msg.setSuccess("true");
				YwLogUtil.addYwLog("删除用户-成功", ConstValue.SF.YES.Value,ConstValue.LOG.DELETE);
			} catch (Exception e) {
				logger.error(e);
				msg.setMsg(e.getMessage());
				YwLogUtil.addYwLog("删除用户-失败", ConstValue.SF.NO.Value,ConstValue.LOG.DELETE);
			}
		} else {
			msg.setMsg("无该用户");
		}
		return msg;
	}

	/**
	 * 密码重置
	 * 
	 * */
	@RequestMapping(value = "/user/resetPassword/{id}")
	public @ResponseBody ResultMessage resetPassword(
			@PathVariable("id") String id, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (StringUtils.hasLength(id)) {
			try {
				User user = userService.findById(id);
				user.setPassword(MD5Util.generate("123456"));
				userService.resetPassword(user);
				msg.setSuccess("true");
				YwLogUtil.addYwLog("密码重置-成功", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
			} catch (Exception e) {
				logger.error(e);
				msg.setMsg(e.getMessage());
				YwLogUtil.addYwLog("密码重置", ConstValue.SF.NO.Value,ConstValue.LOG.UPDATE);
			}
		} else {
			msg.setMsg("无该用户");
		}
		return msg;
	}
	
	/**
	 * 修改密码
	 * 
	 * */
	@RequestMapping(value = "/user/changePassword")
	public @ResponseBody ResultMessage changePassword(
			HttpServletRequest request, HttpServletResponse response,
			ChPassword chPassword) {
		ResultMessage msg = new ResultMessage();
		
		if (chPassword.getNewpassword().equals(chPassword.getRepassword())) {
			try {
				Subject user = SecurityUtils.getSubject();
				User usersss= userService.getCurrentUserInfo();
				String loginName =usersss.getLoginName(); //user.getPrincipal().toString();
				User user_now = userService.findByLoginName(loginName);		
				if (!MD5Util.verify(chPassword.getPassword(), user_now.getPassword())) {
					msg.setMsg("原密码错误");
					msg.setSuccess("false"); 
				} else {
					user_now.setPassword(MD5Util.generate(chPassword
							.getNewpassword()));
					userService.resetPassword(user_now);
					msg.setSuccess("true");
					YwLogUtil.addYwLog("修改密码", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
				}
			} catch (Exception e) {
				logger.error(e);
				msg.setMsg(e.getMessage());
				YwLogUtil.addYwLog("修改密码", ConstValue.SF.NO.Value,ConstValue.LOG.UPDATE);
			}
		} else {
			msg.setMsg("两次输入密码不一致");
		}
		return msg;
	}

	/**
	 * 获取部门tree
	 * @作者 chenhl
	 * @创建时间 2015年7月10日下午1:42:21
	 * @return
	 */
	@RequestMapping(value = "/user/getDepartment")
	public @ResponseBody List<Tree> getDepartment() {
		List<Tree> list = departmentService.getDepartmentComboTree(null);
		return list;
	}

	/**
	 * 设置部门tree
	 * @作者 chenhl
	 * @创建时间 2015年7月10日下午1:42:53
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/user/setDepartment")
	public @ResponseBody List<Tree> setDepartment(HttpServletRequest request,
			HttpServletResponse response) {
		List<Tree> list = departmentService.setDepartmentComboTree(request.getParameter("departmentId"));
		return list;
	}

	/**
	 * 设置角色
	 * @作者 chenhl
	 * @创建时间 2015年7月10日下午1:43:28
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/user/setRoles")
	public @ResponseBody List<Tree> setRoles(HttpServletRequest request,
			HttpServletResponse response) {
		List<Tree> list = userService.setRoleTree(request.getParameter("id"));
		return list;
	}

	/**
	 * 保存用户角色信息
	 * @作者 chenhl
	 * @创建时间 2015年7月10日下午1:43:48
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/user/saveUserRole", method = RequestMethod.POST)
	public @ResponseBody ResultMessage saveUserRole(HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		String userId = request.getParameter("userId");
		String roleIds = request.getParameter("roleIds");
		if (roleIds != null && !roleIds.equals(""))
			roleIds.substring(0, roleIds.length() - 1);
			recordsMaintian.MaintainStaffRole(userId, userService.CompareDiffRoleInStaff(userId, roleIds));
		try {
			User usercheck = userService.findById(userId);
			userService.saveUserRole(usercheck, roleIds);
			msg.setSuccess("true");
			YwLogUtil.addYwLog("保存用户角色-成功", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
		} catch (Exception e) {
			logger.error(e);
			msg.setMsg(e.getMessage());
			YwLogUtil.addYwLog("保存用户角色-失败", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
		}
		return msg;
	}

	/**
	 * 跳转编辑页面
	 * 
	 * @param model
	 * @author diaoliwei
	 * @return
	 */
	@RequestMapping(value = "/user/edit", method = RequestMethod.GET)
	public String ShowEditPage(Model model, HttpServletRequest request,
			HttpServletResponse response) {
		User user = new User();
		String id = request.getParameter("id");
		String url = request.getParameter("url");
		String method = "post";
		if (!StringUtils.isEmpty(id)) {
			user = userService.findById(id);
			user.setDepartmentId(user.getDepartment().getId());
			user.setDepartmentName(user.getDepartment().getDepartmentName());
			method = "put";
		}
		model.addAttribute("userAttribute", user);
		model.addAttribute("url", url);
		model.addAttribute("method", method);
		return prefix + "editUser";
	}
	/**
	 * 跳转编辑允许登录ip页面
	 * 
	 * @param model
	 * @author liqq
	 * @return
	 */
	@RequestMapping(value = "/user/editauserip", method = RequestMethod.GET)
	public String ShowAipsEditPage(Model model, HttpServletRequest request,
			HttpServletResponse response) {
		User user = new User();
		String id = request.getParameter("id");
		String url = request.getParameter("url");
		String method = "post";
		if (!StringUtils.isEmpty(id)) {
			user = userService.findById(id);
			user.setDepartmentId(user.getDepartment().getId());
			user.setDepartmentName(user.getDepartment().getDepartmentName());
			method = "put";
		}
		model.addAttribute("userAttribute", user);
		model.addAttribute("url", url);
		model.addAttribute("method", method);
		return prefix + "auserIp";
	}
	
	/**
	 * 用户信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/user/Info", method = RequestMethod.POST)
	public @ResponseBody User UserInfo(HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		User user= userService.getCurrentUserInfo();
		return user;
	}
	
	
}
