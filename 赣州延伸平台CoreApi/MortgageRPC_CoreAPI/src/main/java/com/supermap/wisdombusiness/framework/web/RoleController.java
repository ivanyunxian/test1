package com.supermap.wisdombusiness.framework.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.springframework.web.multipart.MultipartFile;

import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.framework.model.Department;
import com.supermap.wisdombusiness.framework.model.Role;
import com.supermap.wisdombusiness.framework.model.RoleGroup;
import com.supermap.wisdombusiness.framework.model.SystemModule;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.model.System;
import com.supermap.wisdombusiness.framework.service.DepartmentService;
import com.supermap.wisdombusiness.framework.service.RoleGroupService;
import com.supermap.wisdombusiness.framework.service.RoleService;
import com.supermap.wisdombusiness.framework.service.SystemModuleService;
import com.supermap.wisdombusiness.framework.service.SystemService;
import com.supermap.wisdombusiness.framework.service.UserService;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.web.ui.tree.Tree;
import com.supermap.wisdombusiness.workflow.service.relationmaintain.impl.RecordsMaintian;

/**
 * 
 * @Description:角色controller
 * @author 杨鹏
 * @date 2015年7月10日 下午12:57:03
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/framework")
public class RoleController {

	private static final Log logger = LogFactory.getLog(RoleController.class);

	//角色service
	@Autowired
	private RoleService roleService;
	
	//角色分组service
	@Autowired
	private RoleGroupService roleGroupService;
	
	//系统service
	@Autowired
	private SystemService systemService;
	
	//系统模块service
	@Autowired
	private SystemModuleService systemModuleService;
	
	//部门service
	@Autowired
	private DepartmentService departmentService;
	
	//用户service
	@Autowired
	private UserService userService;
	@Autowired
	private RecordsMaintian recordsMaintian;

	//跳转地址
	private final String prefix = "/framework/role/";

	/**
	 * 角色首页面
	 * @作者 杨鹏
	 * @创建时间 2015年7月10日下午12:58:18
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/role/index", method = RequestMethod.GET)
	public String ShowManageIndex(Model model) {
		// model.addAttribute("roleGroupattribute", new RoleGroup());
		// model.addAttribute("roleattribute", new Role());
		YwLogUtil.addYwLog("访问:角色管理功能", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return prefix + "manage";
	}

	/**
	 * 获取角色列表
	 * 
	 * @param page
	 *            当前页
	 * @param rows
	 *            每页显示条数
	 * */
	@RequestMapping(value = "/roles", method = RequestMethod.GET)
	public @ResponseBody Message getRoleList(HttpServletRequest request,
			HttpServletResponse response) {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}

		Map<String, Object> mapCondition = new HashMap<String, Object>();

		// String catId = "";
		// if (request.getParameter("catId") != null) {
		// catId = request.getParameter("catId");
		// mapCondition.put("cat_id", catId);
		// }
		Page articlePaged = roleService.getPagedRole(page, rows, mapCondition);
		Message m = new Message();
		m.setTotal(articlePaged.getTotalCount());
		m.setRows(articlePaged.getResult());
		return m;
	}

	/**
	 * 提交并保存角色
	 * */
	@RequestMapping(value = "/role", method = RequestMethod.POST)
	public @ResponseBody ResultMessage saveRole(
			@ModelAttribute("roleattribute") Role role, BindingResult result,
			SessionStatus status, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (result.hasErrors()) {
			logger.error(result);
			msg.setMsg(result.toString());
		} else {
			try {
				String groupId = role.getGroupId();
				if (groupId != null && groupId != "") {
					role.setRoleGroup(roleGroupService.findById(groupId));
				}
				Date createTime = new Date();
				role.setCreateTime(createTime);
				//TODO:校验角色编码的唯一性
				String roleCode = role.getRoleCode();
				if(roleCode!=null&&!roleCode.equals("")){
					List<Role> roles = roleService.getRoleByRoleCode(roleCode);
					if(roles.size()>0){
						msg.setMsg("角色编码重复!");
						msg.setSuccess("false");
						return msg;
					}
				}
				roleService.saveRole(role);
				status.setComplete();
				msg.setMsg(role.getId());
				msg.setSuccess("true");
				YwLogUtil.addYwLog("添加角色", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
			} catch (Exception e) {
				logger.error(e);
				msg.setMsg(e.getMessage());
				YwLogUtil.addYwLog("添加角色", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
			}
		}
		return msg;
	}

	/**
	 * 更新角色
	 * 
	 * */
	@RequestMapping(value = "/role/{id}", method = RequestMethod.PUT)
	public @ResponseBody ResultMessage updateRole(
			@PathVariable("id") String id, @ModelAttribute("roleattribute") Role role, BindingResult result,
			SessionStatus status, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (result.hasErrors()) {
			logger.error(result);
			msg.setMsg(result.toString());
		} else {
			try {
				role.setId(id);
				String groupId = role.getGroupId();
				if (groupId != null && groupId != "") {
					role.setRoleGroup(roleGroupService.findById(groupId));
				}
				//TODO:校验角色编码的唯一性
				String roleCode = role.getRoleCode();
				if(roleCode!=null&&!roleCode.equals("")){
					List<Role> roles = roleService.getRoleByRoleCode(roleCode);
					if(roles.size()>=1){
						msg.setMsg("角色编码重复!");
						msg.setSuccess("false");
						return msg;
					}
				}
				roleService.updateRole(role);
				msg.setMsg(id);
				msg.setSuccess("true");
				YwLogUtil.addYwLog("更新角色", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
			} catch (Exception e) {
				logger.error(e);
				msg.setMsg(e.getMessage());
				YwLogUtil.addYwLog("更新角色", ConstValue.SF.NO.Value,ConstValue.LOG.UPDATE);
			}
		}
		return msg;
	}

	/**
	 * 删除角色
	 * 
	 * */
	@RequestMapping(value = "/role/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage deleteRole(
			@PathVariable("id") String id, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (StringUtils.hasLength(id)) {
			try {
				roleService.deleteRoleById(id);
				msg.setSuccess("true");
				YwLogUtil.addYwLog("删除角色", ConstValue.SF.YES.Value,ConstValue.LOG.DELETE);
			} catch (Exception e) {
				logger.error(e);
				msg.setMsg(e.getMessage());
				YwLogUtil.addYwLog("删除角色", ConstValue.SF.NO.Value,ConstValue.LOG.DELETE);
			}
		} else {
			msg.setMsg("无该角色");
		}
		return msg;
	}

	/**
	 * 根据角色ID获取用户列表
	 * 
	 * @param page
	 *            当前页
	 * @param rows
	 *            每页显示条数
	 * */
	@RequestMapping(value = "role/getPagedUserByRole", method = RequestMethod.POST)
	public @ResponseBody Message getUserByRoleList(HttpServletRequest request,
			HttpServletResponse response) {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		String roleId = "";
		if (request.getParameter("id") != null) {
			roleId = request.getParameter("id");
			mapCondition.put("role_id", roleId);
		}
		Page articlePaged = roleService.getPagedUserByRole(page, rows, mapCondition);
		Message m = new Message();
		m.setTotal(articlePaged.getTotalCount());
		m.setRows(articlePaged.getResult());
		return m;
	}

	/**
	 * 获取用户列表
	 * 
	 * @param page
	 *            当前页
	 * @param rows
	 *            每页显示条数
	 * */
	@RequestMapping(value = "role/getPagedUser", method = RequestMethod.POST)
	public @ResponseBody Message getUserList(HttpServletRequest request,
			HttpServletResponse response) {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		// String roleId = "";
		// if (request.getParameter("id") != null) {
		// roleId = request.getParameter("id");
		// mapCondition.put("role_id", roleId);
		// }
		// Page articlePaged = roleService.getPagedUser(page, rows,
		// mapCondition);
		Page articlePaged = userService.getPagedUser(page, rows, mapCondition);
		Message m = new Message();
		m.setTotal(articlePaged.getTotalCount());
		m.setRows(articlePaged.getResult());
		return m;
	}

	/**
	 * 获取角色组列表
	 * 
	 * @param request
	 * @param response
	 * @author
	 * @return
	 */
	@RequestMapping(value = "role/getRoleGroup", method = RequestMethod.POST)
	public @ResponseBody List<RoleGroup> queryRoleGroups(
			HttpServletRequest request, HttpServletResponse response) {
		List<RoleGroup> list = roleGroupService.findAll();
		return list;
	}

	/**
	 * 获取系统模块列表
	 * @作者 杨鹏
	 * @创建时间 2015年7月10日下午1:03:28
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "role/getSystemModule", method = RequestMethod.GET)
	public @ResponseBody List<Tree> getSystemModuleList(
			HttpServletRequest request, HttpServletResponse response) {
		String roleId = "";
		List<String> modules = null;
		if (request.getParameter("roleId") != null) {
			roleId = request.getParameter("roleId");
			modules = roleService.findModuleIdByRoleId(roleId);
		}
		// 获取所有系统
		List<System> systems = systemService.findAll();
		List<Tree> eTrees = new ArrayList<Tree>();
		for (System system : systems) {
			Tree eTree = new Tree();
			eTree.setId(system.getId());
			eTree.setText(system.getSysName());
			eTree.setType("system");
			eTree.setChecked(false);

			List<SystemModule> systemModules = systemModuleService.getSysModules(system.getId());
			createSysModuleTree(systemModules, eTree, modules);
			eTrees.add(eTree);
		}

		return eTrees;
	}
	
	/**
	 * 组装SysModuleTree
	 * @作者 杨鹏
	 * @创建时间 2015年7月10日下午1:27:18
	 * @param list
	 * @param currentNode
	 * @param modules
	 */
	@SuppressWarnings("unchecked")
	private void createSysModuleTree(List<SystemModule> list, Tree currentNode, List<String> modules) {
		for (int i = 0; i < list.size(); i++) {
			SystemModule resource = list.get(i);
			
			Tree eTree = new Tree();
			eTree.setId(resource.getId());
			eTree.setText(resource.getSysModuleName());
			eTree.setType("module");
			eTree.setChecked(false);
			if (modules != null) {
				for (int j = 0; j < modules.size(); j++) {
					String modulesID = modules.get(j);
					if (modulesID.equals(resource.getId())) {
						eTree.setChecked(true);
						break;
					}
				}
			}
			if (resource.getParentId() == null || resource.getParentId().equals("")) {
				createTree(list, eTree, modules);
				if (currentNode.getChildren() == null) {
					currentNode.setChildren(new ArrayList<Tree>());
				}
				currentNode.children.add(eTree);
			}
		}
	}

	/**
	 * 组装Tree
	 * @作者 杨鹏
	 * @创建时间 2015年7月10日下午1:29:17
	 * @param list
	 * @param currentNode
	 * @param modules
	 */
	@SuppressWarnings("unchecked")
	private void createTree(List<SystemModule> list, Tree currentNode, List<String> modules) {
		for (int i = 0; i < list.size(); i++) {
			SystemModule newNode = list.get(i);
			Tree eTree = new Tree();
			eTree.setId(newNode.getId());
			eTree.setText(newNode.getSysModuleName());
			eTree.setType("module");
			eTree.setChecked(false);
			if (modules != null) {
				for (int j = 0; j < modules.size(); j++) {
					String modulesID = modules.get(j);
					if (modulesID.equals(newNode.getId())) {
						eTree.setChecked(true);
						break;
					}
				}
			}
			if (newNode.getParentId() != null
					&& newNode.getParentId().compareTo(currentNode.getId()) == 0) {
				if (currentNode.children == null) {
					currentNode.children = new ArrayList<Tree>();
				}
				currentNode.children.add(eTree);
				createTree(list, eTree, modules);
			}
		}
	}

	/*
	 * 关联角色和用户
	 */
	@RequestMapping(value = "role/createRoleUserRecords", method = RequestMethod.POST)
	public @ResponseBody ResultMessage createRoleUserRecords(
			HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		String roleId = request.getParameter("roleId");
		if (roleId != null && !"".equals(roleId)) {
			try {
				String userIds = request.getParameter("userIds");
				if (userIds != null && !"".equals(userIds)) {
					recordsMaintian.MaintainRoleStaff(roleId, roleService.CompareDiffStaffInRole(roleId, userIds));
					roleService.createRoleUserRecords(roleId, userIds);
				
				}
				msg.setSuccess("true");
				YwLogUtil.addYwLog("关联角色和用户", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
			} catch (Exception e) {
				logger.error(e);
				msg.setMsg(e.getMessage());
				YwLogUtil.addYwLog("关联角色和用户-失败", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
			}
		} else {
			msg.setMsg("无该角色");
		}
		return msg;
	}

	/*
	 * 关联角色和系统模块
	 */
	@RequestMapping(value = "role/createRoleModuleRecords", method = RequestMethod.POST)
	public @ResponseBody ResultMessage createRoleModuleRecords(
			HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		String roleId = request.getParameter("roleId");
		if (roleId != null && !"".equals(roleId)) {
			try {
				String moduleIds = request.getParameter("moduleIds");
				if (moduleIds != null && !"".equals(moduleIds)) {
					roleService.createRoleModuleRecords(roleId, moduleIds);
				}
				msg.setSuccess("true");
				YwLogUtil.addYwLog("关联角色和系统模块-成功", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
			} catch (Exception e) {
				logger.error(e);
				msg.setMsg(e.getMessage());
				YwLogUtil.addYwLog("关联角色和系统模块-失败", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
			}
		} else {
			msg.setMsg("无该角色");
		}
		return msg;
	}

	/***
	 * 创建用户tree
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "role/getUserTree", method = RequestMethod.GET)
	public @ResponseBody List<Tree> getUserTree(HttpServletRequest request,
			HttpServletResponse response) {
		String roleId = "";
		List<String> users = null;
		if (request.getParameter("roleId") != null) {
			roleId = request.getParameter("roleId");
			users = roleService.findUserIdByRoleId(roleId);
		}
		// 部门用户Tree对象
		List<Tree> eTrees = new ArrayList<Tree>();
		// 1. 获取所有部门下用户
		List<Department> departments = departmentService.findAll();
		for (Department department : departments) {
			Tree dTree = new Tree();
			String departmentId = department.getId();
			dTree.setId(department.getId());
			dTree.setText(department.getDepartmentName());
			dTree.setType("department");
			dTree.setChecked(false);

			if (department.getParentId() == null || department.getParentId().equals("")) {
				createDepartmentTree(departments, dTree, users);

				// 部门下用户
				List<User> departUsers = userService.findUserByDepartmentId(departmentId);
				for (User dUser : departUsers) {
					Tree uTree = new Tree();
					uTree.setId(dUser.getId());
					uTree.setText(dUser.getUserName());
					uTree.setType("user");
					uTree.setChecked(false);
					if (users != null) {
						for (String userID : users) {
							if (userID.equals(dUser.getId())) {
								uTree.setChecked(true);
								break;
							}
						}
					}
					if (dTree.children == null) {
						dTree.children = new ArrayList<Tree>();
					}
					dTree.children.add(uTree);
				}
				eTrees.add(dTree);
			}
		}

		// 2. 获取无部门下用户
		// 部门下用户
		List<User> noDepartUsers = userService.findUserWithNoDepartmentId();
		for (User dUser : noDepartUsers) {
			Tree uTree = new Tree();
			uTree.setId(dUser.getId());
			uTree.setText(dUser.getUserName());
			uTree.setType("user");
			uTree.setChecked(false);
			if (users != null) {
				for (String userID : users) {
					if (userID.equals(dUser.getId())) {
						uTree.setChecked(true);
						break;
					}
				}
			}
			eTrees.add(uTree);
		}
		return eTrees;
	}

	/**
	 * 创建部门tree
	 * @作者 杨鹏
	 * @创建时间 2015年7月10日下午1:34:56
	 * @param list
	 * @param currentNode
	 * @param checks
	 */
	@SuppressWarnings("unchecked")
	private void createDepartmentTree(List<Department> list, Tree currentNode,
			List<String> checks) {
		for (int i = 0; i < list.size(); i++) {
			Department newNode = list.get(i);
			String departmentId = newNode.getId();
			Tree eTree = new Tree();
			eTree.setId(newNode.getId());
			eTree.setText(newNode.getDepartmentName());
			eTree.setType("department");
			eTree.setChecked(false);
			if (newNode.getParentId() != null
					&& newNode.getParentId().compareTo(currentNode.getId()) == 0) {
				if (currentNode.children == null) {
					currentNode.children = new ArrayList<Tree>();
				}
				currentNode.children.add(eTree);
				createDepartmentTree(list, eTree, checks);
				// 部门下用户
				List<User> departUsers = userService.findUserByDepartmentId(departmentId);
				for (User dUser : departUsers) {
					Tree uTree = new Tree();
					uTree.setId(dUser.getId());
					uTree.setText(dUser.getUserName());
					uTree.setType("user");
					uTree.setChecked(false);
					if (checks != null) {
						for (String userID : checks) {
							if (userID.equals(dUser.getId())) {
								uTree.setChecked(true);
								break;
							}
						}
					}

					if (eTree.children == null) {
						eTree.children = new ArrayList<Tree>();
					}
					eTree.children.add(uTree);
				}

			}
		}
	}

	/***
	 * 创建用户tree
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "role/getSingleUserTree", method = RequestMethod.GET)
	public @ResponseBody List<Tree> getSingleUserTree(
			HttpServletRequest request, HttpServletResponse response) {
		String roleId = "";
		List<String> users = null;
		if (request.getParameter("roleId") != null) {
			roleId = request.getParameter("roleId");
			users = roleService.findUserIdByRoleId(roleId);
		}

		// 部门用户Tree对象
		List<Tree> eTrees = new ArrayList<Tree>();

		// 1. 获取所有用户
		List<User> allUsers = userService.findAll();

		for (User user : allUsers) {
			Tree uTree = new Tree();
			uTree.setId(user.getId());
			uTree.setText(user.getUserName());
			uTree.setType("user");
			uTree.setChecked(false);
			if (users != null) {
				for (String userID : users) {
					if (userID.equals(user.getId())) {
						uTree.setChecked(true);
						break;
					}
				}
			}
			eTrees.add(uTree);
		}
		return eTrees;
	}

	/**
	 * 角色tree
	 * @作者 杨鹏
	 * @创建时间 2015年7月10日下午1:35:51
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/roleTree", method = RequestMethod.GET)
	public @ResponseBody List<Tree> getroleTree(HttpServletRequest request,
			HttpServletResponse response) {
		List<Tree> roleTrees = roleService.getRoleTree();
		return roleTrees;
	}

	/**
	 * 跳转编辑页面
	 * 
	 * @param model
	 * @author
	 * @return
	 */
	@RequestMapping(value = "/role/edit", method = RequestMethod.GET)
	public String ShowEditPage(Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Role role = new Role();
		String id = request.getParameter("id");
		String url = request.getParameter("url");
		String method = "post";
		if (!StringUtils.isEmpty(id)) {
			role = roleService.findById(id);
			method = "put";
		}
		model.addAttribute("roleattribute", role);
		model.addAttribute("roleGroupattribute", new RoleGroup());
		model.addAttribute("url", url);
		model.addAttribute("method", method);
		return prefix + "editRole";
	}

	
	/**
	 * 上传图片
	 * 
	 * @param model
	 * @author
	 * @return
	 */
 
	@RequestMapping(value = "/role/showsign", method = RequestMethod.POST)
	public void showsign(HttpServletRequest request,
			HttpServletResponse response,MultipartFile file)  throws Exception {
		    String id=request.getParameter("id");
	        String qlrinfopath = "%s\\resources\\workflow\\signimg\\%s.%s"; 
	        String type=file.getContentType();
	        try {
				FileOutputStream fs = new FileOutputStream(new File(String.format(qlrinfopath, request.getRealPath("/"), id, "png")));
				fs.write(file.getBytes());
				fs.close();
				User user=userService.findById(id);
				user.setSIGN("1");
				userService.updateUser(user);
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write("true");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write("false");
			}
	        
	        return;
	    }
	
 
	@RequestMapping(value = "/role/maintainRole", method = RequestMethod.GET)
	public String maintainRole() {
		 
		return prefix + "maintainRole";
	}
	
	@RequestMapping(value = "/role/getMaintainRole", method = RequestMethod.GET)
	public ResultMessage getMaintainRole(HttpServletRequest request,HttpServletResponse response) {
		String staffid=request.getParameter("staffid");
		userService.delRelation(staffid);
		return  userService.addRelation(staffid);
		
	}
	//通过用户ID 获取用户的所有角色
	@RequestMapping(value = "/roles/{staffid}", method = RequestMethod.GET)
	@ResponseBody
	public List<Role> getRolesByStaffid(HttpServletRequest request,HttpServletResponse response,@PathVariable("staffid") String staffid) {
		return roleService.getAllRoleByUserId(staffid);		
	}
	//檢查用戶的有效性
	@RequestMapping(value = "/getusers", method = RequestMethod.GET)
	@ResponseBody
	public List<User> getUsers(HttpServletRequest request,HttpServletResponse response) {
		return roleService.getUsers();		
	}
	
}