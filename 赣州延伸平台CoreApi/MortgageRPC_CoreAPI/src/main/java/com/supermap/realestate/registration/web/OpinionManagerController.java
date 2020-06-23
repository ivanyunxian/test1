package com.supermap.realestate.registration.web;

import java.util.List;
import java.util.Map;

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

import com.supermap.realestate.registration.model.BDCS_OPINION;
import com.supermap.realestate.registration.model.BDCS_TEMPLATECLASS;
import com.supermap.realestate.registration.service.OpinionManagerService;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.TreeInfo;
import com.supermap.wisdombusiness.workflow.util.Message;
import com.supermap.wisdombusiness.workflow.util.Page;

/**
 * 意见模板管理Controller
 * @ClassName: OpinionManagerController
 * @author taochunda
 * @date 2017年8月25日 下午09:23:04
 */
@Controller
@RequestMapping("/opinion")
public class OpinionManagerController {

	@Autowired
	private OpinionManagerService _OpinionManagerService;
	
	private final String prefix = "/realestate/registration/opinion";
	
	@RequestMapping(value = "/opinionmain", method = RequestMethod.GET)
	public String ShowOpinionMain(Model model) {
		//YwLogUtil.addYwLog("访问：意见模板功能", ConstValue.SF.YES.Value, ConstValue.LOG.SEARCH);
		return prefix + "/opinionMain";
	}
	
	/**
	 * 添加模板页面地址映射
	 * */
	@RequestMapping(value = "/addoruptpl/{id}", method = RequestMethod.GET)
	public String addOrUpTpl(Model model,@PathVariable String id) {
		
		BDCS_OPINION data = new BDCS_OPINION();
		BDCS_OPINION bdcs_opinion = _OpinionManagerService.GetOpinionById(id);
		
		if(StringUtils.isEmpty(bdcs_opinion)){//如果是新增
			data.setOPINIONTYPE_ID(id);
			model.addAttribute("data",data);
		}else {
			model.addAttribute("data",bdcs_opinion);
		}
		
		return prefix + "/addOrUpTpl";
	}
	
	@RequestMapping(value = "/addoruptpl/{id}", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo addOrUpTpl(Model model,
			@PathVariable("id") String id,
			@ModelAttribute("opinion") BDCS_OPINION opinion,
			HttpServletRequest request, HttpServletResponse response) {
		return _OpinionManagerService.SaveOrUpdate(opinion);
	}
	
	/**
	 * 获取模板分类树
	 */
	@RequestMapping(value = "/tpltree", method = RequestMethod.GET)
	public @ResponseBody List<TreeInfo> getTplTree(
			HttpServletRequest request, HttpServletResponse response) {
		return _OpinionManagerService.TplTree();
	}
	
	@RequestMapping(value = "/tplclass/{id}", method = RequestMethod.GET)
	public String GetTplClass(Model model, @PathVariable("id") String id,
			HttpServletRequest request, HttpServletResponse response) {
		if (id.equals("0") || id == null) {
			model.addAttribute("TplClass", new BDCS_TEMPLATECLASS());
		} else {
			model.addAttribute("TplClass",
					_OpinionManagerService.GetTplClassById(id));
		}
		return "/workflow/opinion/tplclass";
	}

	@RequestMapping(value = "/tplclass/{id}", method = RequestMethod.POST)
	public String SaveOrUpdate(Model model, @PathVariable("id") String id,
			@ModelAttribute("Opinion") BDCS_TEMPLATECLASS templateclass,
			HttpServletRequest request, HttpServletResponse response) {
		_OpinionManagerService.SaveOrUpdate(templateclass);
		return "redirect:/app/opinion/opinionclass/"
				+ templateclass.getTYPE_ID();
	}
	
	//新建树节点
	@RequestMapping(value = "/newtplclass", method = RequestMethod.POST)
	public @ResponseBody String CreateNewTplclass(
			HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("name");
		String pid = request.getParameter("pid");
		int index = Integer.parseInt(request.getParameter("index"));
		return _OpinionManagerService.CreateTplClassByName(name, pid, index);
	}
	
	//删除目录节点
	@RequestMapping(value = "/deltplclass", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo DeleteOpinionClass(
			HttpServletRequest request, HttpServletResponse response) {
		SmObjInfo obinfo = new SmObjInfo();
		String id = request.getParameter("id");
		_OpinionManagerService.DeleteTplClassById(id);
		return obinfo;
	}
	
	//重命名
	@RequestMapping(value = "/tplclass/rename", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo RemaneTplClass(HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		return _OpinionManagerService.RenameTplClass(id, name);

	}
	
	//删除节点
	@RequestMapping(value = "/delopinion", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo DeleteOpinion(HttpServletRequest request,
			HttpServletResponse response) {
		SmObjInfo obinfo = new SmObjInfo();
		String ids = request.getParameter("id");
		String[] id = ids.split(",");
		for(int i = 0; i < id.length; i++){
			_OpinionManagerService.DeleteOpinionById(id[i]);
			
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
	@RequestMapping(value = "/opinions/{id}", method = RequestMethod.GET)
	public @ResponseBody Message GetOpinions(Model model,
			@PathVariable("id") String id, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String currpage = request.getParameter("currpage");
		String pagesize = request.getParameter("pagesize");
		String opinion_name = request.getParameter("opinion_name");
		
		if (StringUtils.isEmpty(currpage)) {
			currpage = "1";
		}
		if (!StringUtils.isEmpty(opinion_name)) {
			opinion_name = new String(opinion_name.getBytes("ISO-8859-1"), "utf-8");
		}
		
		int page = Integer.parseInt(currpage);
		
		return _OpinionManagerService.FindAllOpinionInfo(id, page, Integer.parseInt(pagesize), opinion_name);
	}
	
	/**
	 * 获取BDCS_OPINION By ID
	 * 
	 * @param model
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opinioninfo/{id}", method = RequestMethod.GET)
	public @ResponseBody BDCS_OPINION GetOpinion(Model model,
			@PathVariable("id") String id, HttpServletRequest request,
			HttpServletResponse response) {
		BDCS_OPINION opinion = _OpinionManagerService.GetOpinionById(id);
		return opinion;
	}
	
	/**
	 * BDCS_OPINION 保存或更新
	 * 
	 * @param model
	 * @param id
	 * @param mater
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/opinioninfo/{id}", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo SaveOrUpdate(Model model,
			@PathVariable("id") String id,
			@ModelAttribute("opinion") BDCS_OPINION opinion,
			HttpServletRequest request, HttpServletResponse response) {
		return _OpinionManagerService.SaveOrUpdate(opinion);
	}
	
	@RequestMapping(value = "/tplopinions", method = RequestMethod.GET)
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
		
		String OPINIONTYPE_ID = request.getParameter("typeid");
		
		return _OpinionManagerService.FindAllOpinions(page, rows, OPINIONTYPE_ID);
	}
	
	/**
	 * 获取模板类别下拉框
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/listbox", method = RequestMethod.GET)
	@ResponseBody
	public List<Map> GetTplListbox( HttpServletRequest request, HttpServletResponse response) {
		
		return _OpinionManagerService.FindListbox();
	}
	
	
}
