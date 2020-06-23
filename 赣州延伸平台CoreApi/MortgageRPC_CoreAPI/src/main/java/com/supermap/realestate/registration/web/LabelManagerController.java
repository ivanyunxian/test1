package com.supermap.realestate.registration.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.model.BDCS_LABEL;
import com.supermap.realestate.registration.model.BDCS_TEMPLATECLASS;
import com.supermap.realestate.registration.service.LabelManagerService;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.TreeInfo;
import com.supermap.wisdombusiness.workflow.util.Message;
import com.supermap.wisdombusiness.workflow.util.Page;

/**
 * 标签模板管理Controller
 * @ClassName: LabelManagerController
 * @author taochunda
 * @date 2017年9月05日 下午14:23:04
 */
@Controller
@RequestMapping("/label")
public class LabelManagerController {

	@Autowired
	private LabelManagerService _LabelManagerService;
	
	private final String prefix = "/realestate/registration/label";
	
	@RequestMapping(value = "/labelmain", method = RequestMethod.GET)
	public String ShowLabelMain(Model model) {
		//YwLogUtil.addYwLog("访问：标签模板功能", ConstValue.SF.YES.Value, ConstValue.LOG.SEARCH);
		return prefix + "/labelMain";
	}
	
	/**
	 * 添加模板页面地址映射
	 * */
	@RequestMapping(value = "/addoruptpl/{id}", method = RequestMethod.GET)
	public String addTemplate(Model model,@PathVariable String id) {
		BDCS_LABEL data = new BDCS_LABEL();
		BDCS_LABEL label = _LabelManagerService.GetLabelInfoById(id);
		if(StringUtils.isEmpty(label)){
			data.setLABELTYPEID(id);
			model.addAttribute("data",data);
		}else {
			model.addAttribute("data",label);
		}
		return prefix + "/addOrUpTpl";
	}
	
	@RequestMapping(value = "/addoruptpl/{id}", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo addTemplate(Model model,
			@PathVariable("id") String id,
			@ModelAttribute("label") BDCS_LABEL label,
			HttpServletRequest request, HttpServletResponse response) {
		return _LabelManagerService.SaveOrUpdate(label);
	}
	
	/**
	 * 获取模板分类树
	 */
	@RequestMapping(value = "/tpltree", method = RequestMethod.GET)
	public @ResponseBody List<TreeInfo> getTplTree(
			HttpServletRequest request, HttpServletResponse response) {
		return _LabelManagerService.TplTree();
	}
	
	@RequestMapping(value = "/tplclass/{id}", method = RequestMethod.GET)
	public String GetTplClass(Model model, @PathVariable("id") String id,
			HttpServletRequest request, HttpServletResponse response) {
		if (id.equals("0") || id == null) {
			model.addAttribute("TplClass", new BDCS_TEMPLATECLASS());
		} else {
			model.addAttribute("TplClass",
					_LabelManagerService.GetTemplateClassById(id));
		}
		return "/workflow/label/tplclass";
	}

	@RequestMapping(value = "/tplclass/{id}", method = RequestMethod.POST)
	public String SaveOrUpdate(Model model, @PathVariable("id") String id,
			@ModelAttribute("TplClass") BDCS_TEMPLATECLASS templateclass,
			HttpServletRequest request, HttpServletResponse response) {
		_LabelManagerService.SaveOrUpdate(templateclass);
		return "redirect:/app/label/tplclass/"
				+ templateclass.getTYPE_ID();
	}
	
	//新建树节点
	@RequestMapping(value = "/newtplclass", method = RequestMethod.POST)
	public @ResponseBody String CreateNewTplclass(
			HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("name");
		String pid = request.getParameter("pid");
		int index = Integer.parseInt(request.getParameter("index"));
		return _LabelManagerService.CreateTplClassByName(name, pid, index);
	}
	
	//删除目录节点
	@RequestMapping(value = "/deltplclass", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo DelTplClass(
			HttpServletRequest request, HttpServletResponse response) {
		SmObjInfo obinfo = new SmObjInfo();
		String id = request.getParameter("id");
		_LabelManagerService.DelTplClassById(id);
		return obinfo;
	}
	
	//重命名
	@RequestMapping(value = "/tplclass/rename", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo RemaneTplClass(HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		return _LabelManagerService.RenameTplClass(id, name);

	}
	
	//删除节点
	@RequestMapping(value = "/dellabelinfo", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo DeleteLabelInfo(HttpServletRequest request,
			HttpServletResponse response) {
		SmObjInfo obinfo = new SmObjInfo();
		String ids = request.getParameter("id");
		String[] id = ids.split(",");
		for(int i = 0; i < id.length; i++){
			_LabelManagerService.DeleteLabelInfoById(id[i]);
			
		}
		obinfo.setDesc("删除成功！");
		return obinfo;
	}
	
	/**
	 * 
	 * @param model
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/labels/{id}", method = RequestMethod.GET)
	public @ResponseBody Message GetLabels(Model model,
			@PathVariable("id") String id, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String currpage = request.getParameter("currpage");
		String pagesize = request.getParameter("pagesize");
		String bqmc = request.getParameter("label_name");
		
		if (currpage == null || currpage.equals("")) {
			currpage = "1";
		}
		if (!StringUtils.isEmpty(bqmc)) {
			bqmc = new String(bqmc.getBytes("ISO-8859-1"), "utf-8");
		}
		int page = Integer.parseInt(currpage);
		
		return _LabelManagerService.FindAllLabelInfo(id, page, Integer.parseInt(pagesize), bqmc);
	}
	
	@RequestMapping(value = "/labels", method = RequestMethod.GET)
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
		
		return _LabelManagerService.FindAllLabels(page , rows);
	}
	
	/**
	 * 获取BDCS_LABEL By ID
	 * 
	 * @param model
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/labelinfo/{id}", method = RequestMethod.GET)
	public @ResponseBody BDCS_LABEL GetLabelInfo(Model model,
			@PathVariable("id") String id, HttpServletRequest request,
			HttpServletResponse response) {
		BDCS_LABEL label = _LabelManagerService.GetLabelInfoById(id);
		return label;
	}
	
	/**
	 * BDCS_LABEL 保存或更新
	 * 
	 * @param model
	 * @param id
	 * @param mater
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/labelinfo/{id}", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo SaveOrUpdate(Model model,
			@PathVariable("id") String id,
			@ModelAttribute("label") BDCS_LABEL label,
			HttpServletRequest request, HttpServletResponse response) {
		return _LabelManagerService.SaveOrUpdate(label);
	}
	
}
