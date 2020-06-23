package com.supermap.wisdombusiness.framework.web;

import java.util.ArrayList;
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

import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.framework.model.Department;
import com.supermap.wisdombusiness.framework.service.DepartmentService;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * 
 * @Description:部门Controller
 * @author diaoliwei
 * @date 2015年7月6日 上午10:48:43
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/framework")
public class DepartmentController {
	
	private static final Log logger = LogFactory.getLog(DepartmentController.class);

	/** 部门service**/
	@Autowired
	private DepartmentService departmentService;
	
	private final String prefix = "/framework/department/";
	
	/**
	 * 部门管理首页
	 * @author diaoliwei
	 * @return
	 */
	@RequestMapping(value="/department/index",method = RequestMethod.GET)
	public String ShowManageIndex() {

		YwLogUtil.addYwLog("访问:部门管理功能", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return prefix + "departmentManage";
	}
	
	/**
	 * 获取部门分页列表
	 * @param request
	 * @param response
	 * @author diaoliwei
	 * @return
	 */
	@RequestMapping(value = "/departments",method = RequestMethod.GET)
	public @ResponseBody Message getDepartmentList(HttpServletRequest request, HttpServletResponse response){
		Integer page = 1;
		if(request.getParameter("page")!=null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if(request.getParameter("rows")!=null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		if(null != request.getParameter("departmentName")){
			String departmentName = request.getParameter("departmentName");
		    mapCondition.put("departmentName", departmentName);
		}
		List<Department> parentDepartments = departmentService.getPagedDepartment(page, rows, mapCondition, true);
		List<Department> departments = departmentService.getPagedDepartment(page, rows, mapCondition, false);
		List<Department> resultdepartments = this.createDepartmentTree(parentDepartments, departments);
		Message m = new Message();
		m.setTotal(departments.size());
		m.setRows(resultdepartments);
		return m;
	}
	
	/**
	 * 包装部门tree对象
	 * @param list
	 * @author diaoliwei
	 * @return
	 */
	private List<Department> createDepartmentTree(List<Department> parents, List<Department> list) {
		List<Department> departments = new ArrayList<Department>();
		for (int i = 0; i < parents.size(); i++) {
			Department resource = parents.get(i);
			if (resource.getParentId() == null || resource.getParentId().equals("")) {
				createTree(list, resource);
				departments.add(resource);
			}
		}
		return departments;
	}

	/**
	 * 组装tree结构
	 * @param list
	 * @param currentNode
	 */
	private void createTree(List<Department> list, Department currentNode) {
		for (int i = 0; i < list.size(); i++) {
			Department newNode = list.get(i);
			if (newNode.getParentId() != null && newNode.getParentId().compareTo(currentNode.getId()) == 0) {
				if (currentNode.children == null) {
					currentNode.children = new ArrayList<Department>();
				}
				currentNode.children.add(newNode);
				createTree(list, newNode);
			}
		}
	}
	
	/**
	 * 新增部门
	 * @param department
	 * @param result
	 * @param status
	 * @param request
	 * @param response
	 * @author diaoliwei
	 * @return
	 */
	@RequestMapping(value = "/department", method = RequestMethod.POST)
	public @ResponseBody ResultMessage onSubmitDepartmentInfoForm(@ModelAttribute("departmentAttribute") Department department, BindingResult result, SessionStatus status,
			HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (result.hasErrors()){
			logger.error(result);
			msg.setMsg(result.toString());
		}else{
			try{
				departmentService.saveDepartment(department);
				status.setComplete();
				msg.setMsg(department.getId());
				msg.setSuccess("true");
				YwLogUtil.addYwLog("新增部门-成功", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
			}catch(Exception e){
				logger.error(e);
				msg.setMsg(e.getMessage());
				YwLogUtil.addYwLog("新增部门-失败", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
			}
		}
		return msg;
	}
	
	/**
	 * 删除部门
	 * @param request
	 * @param response
	 * @author diaoliwei
	 * @return
	 */
	@RequestMapping(value = "/department/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage delDepartment(@PathVariable("id") String id,  HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		int flag = 0;
		if (StringUtils.hasLength(id)) {
			flag = departmentService.delDepartment(id);
		}
		if(flag > 0){
			msg.setSuccess("true");
			YwLogUtil.addYwLog("删除部门-成功", ConstValue.SF.YES.Value,ConstValue.LOG.DELETE);
		}else if(flag == -1){
			msg.setSuccess("related");
			msg.setMsg("该部门已关联用户！暂不可删除");
			YwLogUtil.addYwLog("删除部门-该部门已关联用户！暂不可删除", ConstValue.SF.NO.Value,ConstValue.LOG.DELETE);
		}else{
			msg.setSuccess("无该部门");
		}
		return msg;
	}
	
	/**
	 * 更新
	 * @param id
	 * @param department
	 * @param result
	 * @param status
	 * @param request
	 * @param response
	 * @author diaoliwei
	 * @return
	 */
	@RequestMapping(value = "/department/{id}", method = RequestMethod.PUT)
	public @ResponseBody ResultMessage updateDepartment(@PathVariable("id") String id, @ModelAttribute("departmentAttribute") Department department, BindingResult result,SessionStatus status,
			HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (result.hasErrors()) {
			logger.error(result);
			msg.setMsg(result.toString());
		} else {
			try {
				department.setId(id);
				departmentService.updateDepartment(department);
				msg.setMsg(id);
				msg.setSuccess("true");
				YwLogUtil.addYwLog("更新部门-成功", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
			} catch (Exception e) {
				logger.error(e);
				msg.setMsg(e.getMessage());
				YwLogUtil.addYwLog("更新部门-失败", ConstValue.SF.NO.Value,ConstValue.LOG.UPDATE);
			}
	   }
		return msg;
	}
	
	/**
	 * 获取可以选择的上级部门
	 * @param id
	 * @param request
	 * @param response
	 * @author diaoliwei
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/department/getDepartment/")
	public @ResponseBody List getDepartment(HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("id");
		List list = departmentService.getDepartmentComboTree(id);
		return list;
	}
	
	/**
	 * 跳转编辑页面
	 * @param model
	 * @author diaoliwei
	 * @return
	 */
	@RequestMapping(value="/department/edit",method = RequestMethod.GET)
	public String ShowEditPage(Model model, HttpServletRequest request, HttpServletResponse response) {
		Department department = new Department();
		String id = request.getParameter("id");
		String url = request.getParameter("url");
		String method = "post";
		if(!StringUtils.isEmpty(id)){
			department = departmentService.findById(id);
			method = "put";
		}
		model.addAttribute("departmentAttribute", department);
		model.addAttribute("url",url);
		model.addAttribute("method",method);
		return prefix + "editDepartment";
	}
	
}
