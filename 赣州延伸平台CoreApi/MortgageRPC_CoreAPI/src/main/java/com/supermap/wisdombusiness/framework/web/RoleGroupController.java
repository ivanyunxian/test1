package com.supermap.wisdombusiness.framework.web;

import java.util.HashMap;
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

import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.framework.model.RoleGroup;
import com.supermap.wisdombusiness.framework.service.RoleGroupService;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;

/**
 * 
 * @Description:角色分组controller
 * @author 杨鹏
 * @date 2015年7月10日 下午1:36:32
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/framework")
public class RoleGroupController {

	private static final Log logger = LogFactory.getLog(RoleGroupController.class);

	//角色分组service
	@Autowired
	private RoleGroupService roleGroupService;
	
	//跳转路径
	private final String prefix = "/framework/roleGroup/";

	/**
	 * 角色分组首页
	 * @作者 杨鹏
	 * @创建时间 2015年7月10日下午1:37:08
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/roleGroup/index", method = RequestMethod.GET)
	public String ShowManageIndex(Model model) {
		//model.addAttribute("roleGroupattribute", new RoleGroup());
		YwLogUtil.addYwLog("角色分组列表", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return prefix+"manage";
	}

	/**
	 * 获取角色分组列表
	 * 
	 * @param page
	 *            当前页
	 * @param rows
	 *            每页显示条数
	 * */
	@RequestMapping(value = "/roleGroups", method = RequestMethod.GET)
	public @ResponseBody Message getRoleGroupList(
			HttpServletRequest request,
			HttpServletResponse response) {
		Integer page = 1;
		if(request.getParameter("page")!=null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if(request.getParameter("rows")!=null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		// String catId = "";
		// if (request.getParameter("catId") != null) {
		// catId = request.getParameter("catId");
		// mapCondition.put("cat_id", catId);
		// }
		Page articlePaged = roleGroupService.getPagedRoleGroup(page, rows, mapCondition);
		Message m = new Message();
		m.setTotal(articlePaged.getTotalCount());
		m.setRows(articlePaged.getResult());
		return m;
	}

	/**
	 * 提交并保存角色分组
	 * */
	@RequestMapping(value = "/roleGroup",method = RequestMethod.POST)
	public @ResponseBody ResultMessage saveRoleGroup(
			@ModelAttribute("roleGroupattribute") RoleGroup roleGroup, BindingResult result,
			SessionStatus status, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (result.hasErrors()) {
			logger.error(result);
			msg.setMsg(result.toString());
		} else {
			try {
				roleGroupService.saveRoleGroup(roleGroup);
				status.setComplete();
				msg.setMsg(roleGroup.getId());
				msg.setSuccess("true");
				YwLogUtil.addYwLog("添加角色分组", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
			} catch (Exception e) {
				logger.error(e);
				msg.setMsg(e.getMessage());
				YwLogUtil.addYwLog("添加角色分组", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
			}
		}
		return msg;
	}

	/**
	 * 更新角色分组
	 * 
	 * */
	@RequestMapping(value = "/roleGroup/{id}",method = RequestMethod.PUT)
	public @ResponseBody ResultMessage updateRoleGroup(
			@PathVariable("id") String id,
			@ModelAttribute("roleGroupattribute") RoleGroup roleGroup, BindingResult result,SessionStatus status,
			HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (result.hasErrors()) {
			logger.error(result);
			msg.setMsg(result.toString());
		} else {
			try {
		    roleGroup.setId(id);
			roleGroupService.updateRoleGroup(roleGroup);
			msg.setMsg(id);
			msg.setSuccess("true");
			YwLogUtil.addYwLog("更新角色分组", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
		} catch (Exception e) {
			logger.error(e);
			msg.setMsg(e.getMessage());
			YwLogUtil.addYwLog("更新角色分组", ConstValue.SF.NO.Value,ConstValue.LOG.UPDATE);
		}
	   }
		return msg;
	}

	/**
	 * 删除角色分组
	 * 
	 * */
	@RequestMapping(value = "/roleGroup/{id}",method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage deleteRoleGroup(@PathVariable("id") String id,
			HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (StringUtils.hasLength(id)) {
			try {
				roleGroupService.deleteRoleGroupById(id);
				msg.setSuccess("true");
				YwLogUtil.addYwLog("删除角色分组", ConstValue.SF.YES.Value,ConstValue.LOG.DELETE);
			} catch (Exception e) {
				logger.error(e);
				msg.setMsg(e.getMessage());
				YwLogUtil.addYwLog("删除角色分组", ConstValue.SF.NO.Value,ConstValue.LOG.DELETE);
			}
		} else {
			msg.setMsg("无该用户");
		}
		return msg;
	}
	
	/**
	 * 跳转编辑页面
	 * @param model
	 * @author 
	 * @return
	 */
	@RequestMapping(value="/roleGroup/edit",method = RequestMethod.GET)
	public String ShowEditPage(Model model, HttpServletRequest request, HttpServletResponse response) {
		RoleGroup roleGroup = new RoleGroup();
		String id = request.getParameter("id");
		String url = request.getParameter("url");
		String method = "post";
		if(!StringUtils.isEmpty(id)){
			roleGroup = roleGroupService.findById(id);
			method = "put";
		}
		model.addAttribute("roleGroupattribute", roleGroup);
		model.addAttribute("url",url);
		model.addAttribute("method",method);
		return prefix + "editRoleGroup";
	}
	
}
