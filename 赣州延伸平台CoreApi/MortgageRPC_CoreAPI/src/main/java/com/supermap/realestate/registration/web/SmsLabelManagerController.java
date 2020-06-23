package com.supermap.realestate.registration.web;

import com.supermap.realestate.registration.model.SMS_LABEL;
import com.supermap.realestate.registration.model.SMS_TEMPLATECLASS;
import com.supermap.realestate.registration.service.SmsLabelManagerService;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.TreeInfo;
import com.supermap.wisdombusiness.workflow.util.Message;
import com.supermap.wisdombusiness.workflow.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author taochunda
 * @Description 短信标签定义管理
 * @Date 2019-07-02 15:38
 * @Param
 * @return
 **/
@Controller
@RequestMapping("/smslabel")
public class SmsLabelManagerController {

	@Autowired
	private SmsLabelManagerService smsLabelManagerService;
	
	private final String prefix = "/realestate/registration/label";
	
	@RequestMapping(value = "/smslabelmain", method = RequestMethod.GET)
	public String ShowLabelMain(Model model) {
		return prefix + "/smsLabelMain";
	}
	
	/**
	 * 添加模板页面地址映射
	 * */
	@RequestMapping(value = "/addorupsmstpl/{id}", method = RequestMethod.GET)
	public String addTemplate(Model model,@PathVariable String id) {
		SMS_LABEL sms_label = new SMS_LABEL();
		SMS_LABEL label = smsLabelManagerService.GetLabelInfoById(id);
		if(StringUtils.isEmpty(label)){
			sms_label.setCLASSID(id);
			model.addAttribute("data", sms_label);
		}else {
			model.addAttribute("data",label);
		}
		return prefix + "/addOrUpSmsTpl";
	}
	
	@RequestMapping(value = "/addorupsmstpl/{id}", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo addTemplate(Model model,
			@PathVariable("id") String id,
			@ModelAttribute("label") SMS_LABEL label,
			HttpServletRequest request, HttpServletResponse response) {
		return smsLabelManagerService.SaveOrUpdate(label);
	}
	
	/**
	 * 获取模板分类树
	 */
	@RequestMapping(value = "/smstpltree", method = RequestMethod.GET)
	public @ResponseBody List<TreeInfo> getTplTree(
			HttpServletRequest request, HttpServletResponse response) {
		return smsLabelManagerService.TplTree();
	}
	
	@RequestMapping(value = "/smstplclass/{id}", method = RequestMethod.GET)
	public String GetTplClass(Model model, @PathVariable("id") String id,
			HttpServletRequest request, HttpServletResponse response) {
		if ("0".equals(id) || id == null) {
			model.addAttribute("TplClass", new SMS_TEMPLATECLASS());
		} else {
			model.addAttribute("TplClass",
					smsLabelManagerService.GetTemplateClassById(id));
		}
		return "/workflow/label/smstplclass";
	}

	@RequestMapping(value = "/smstplclass/{id}", method = RequestMethod.POST)
	public String SaveOrUpdate(Model model, @PathVariable("id") String id,
			@ModelAttribute("TplClass") SMS_TEMPLATECLASS templateclass,
			HttpServletRequest request, HttpServletResponse response) {
		smsLabelManagerService.SaveOrUpdate(templateclass);
		return "redirect:/app/smslabel/smstplclass/"
				+ templateclass.getCLASSID();
	}
	
	//新建树节点
	@RequestMapping(value = "/newsmstplclass", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo CreateNewTplclass(
			HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("name");
		String pid = request.getParameter("pid");
		Long index = StringHelper.getLong(request.getParameter("index"));
		return smsLabelManagerService.CreateTplClassByName(name, pid, index);
	}
	
	//删除目录节点
	@RequestMapping(value = "/delsmstplclass", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo DelTplClass(
			HttpServletRequest request, HttpServletResponse response) {
		SmObjInfo obinfo = new SmObjInfo();
		String id = request.getParameter("id");
		smsLabelManagerService.DelTplClassById(id);
		return obinfo;
	}
	
	//重命名
	@RequestMapping(value = "/smstplclass/rename", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo RemaneTplClass(HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		return smsLabelManagerService.RenameTplClass(id, name);

	}
	
	//删除节点
	@RequestMapping(value = "/delsmslabelinfo", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo DeleteLabelInfo(HttpServletRequest request,
			HttpServletResponse response) {
		SmObjInfo obinfo = new SmObjInfo();
		String ids = request.getParameter("id");
		String[] id = ids.split(",");
		for(int i = 0; i < id.length; i++){
			smsLabelManagerService.DeleteLabelInfoById(id[i]);
			
		}
		obinfo.setDesc("删除成功！");
		return obinfo;
	}
	
	/**
	 * @param model
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/smslabels/{id}", method = RequestMethod.GET)
	public @ResponseBody Message GetLabels(Model model,
			@PathVariable("id") String id, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String currpage = request.getParameter("currpage");
		String pagesize = request.getParameter("pagesize");
		String label_name = RequestHelper.getParam(request, "label_name");
		
		if (StringHelper.isEmpty(currpage)) {
			currpage = "1";
		}
		if (StringHelper.isEmpty(pagesize)) {
			pagesize = "10";
		}
		int pageindex = Integer.parseInt(currpage);
		
		return smsLabelManagerService.FindAllLabelInfo(id, pageindex, Integer.parseInt(pagesize), label_name);
	}
	
	@RequestMapping(value = "/smslabels", method = RequestMethod.GET)
	@ResponseBody
	public Page GetLabels( HttpServletRequest request, HttpServletResponse response) {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		
		return smsLabelManagerService.FindAllLabels(page , rows);
	}
	
	/**
	 * 获取SMS_LABEL By ID
	 * 
	 * @param model
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/smslabelinfo/{id}", method = RequestMethod.GET)
	public @ResponseBody SMS_LABEL GetLabelInfo(Model model,
			@PathVariable("id") String id, HttpServletRequest request) {
		SMS_LABEL label = smsLabelManagerService.GetLabelInfoById(id);
		return label;
	}
	
	/**
	 * SMS_LABEL 保存或更新
	 * 
	 * @param model
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/smslabelinfo/{id}", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo SaveOrUpdate(Model model,
			@PathVariable("id") String id,
			@ModelAttribute("label") SMS_LABEL label,
			HttpServletRequest request, HttpServletResponse response) {
		return smsLabelManagerService.SaveOrUpdate(label);
	}
	
}
