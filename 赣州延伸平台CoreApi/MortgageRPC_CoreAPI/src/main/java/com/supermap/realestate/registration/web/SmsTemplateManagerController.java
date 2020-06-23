package com.supermap.realestate.registration.web;

import com.supermap.realestate.registration.model.SMS_TEMPLATE;
import com.supermap.realestate.registration.model.SMS_TEMPLATECLASS;
import com.supermap.realestate.registration.service.SmsTemplateManagerService;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.TreeInfo;
import com.supermap.wisdombusiness.web.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * @Author taochunda
 * @Description 自定义短信模板管理Controller
 * @Date 2019-07-03 14:45
 * @Param
 * @return
 **/
@Controller
@RequestMapping("/smstemplate")
public class SmsTemplateManagerController {

	@Autowired
	private SmsTemplateManagerService smsTemplateManagerService;

	private final String prefix = "/realestate/registration/smsmanage";
	
	@RequestMapping(value = "/smstemplatemain", method = RequestMethod.GET)
	public String ShowTemplateMain(Model model) {
		return prefix + "/smsTemplateMain";
	}
	
	/**
	 * 添加模板页面地址映射
	 * */
	@RequestMapping(value = "/addorupsmstpl/{id}", method = RequestMethod.GET)
	public String addOrUpTpl(Model model,@PathVariable String id) {
		SMS_TEMPLATE template = new SMS_TEMPLATE();
		SMS_TEMPLATE sms_template = smsTemplateManagerService.GetTemplateById(id);
		if(sms_template == null){//如果是新增
			template.setCLASSID(id);
			model.addAttribute("data",template);
		}else {
			model.addAttribute("data",sms_template);
		}
		
		return prefix + "/addOrUpSmsTpl";
	}
	
	@RequestMapping(value = "/addorupsmstpl/{id}", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo addOrUpTpl(@ModelAttribute("opinion") SMS_TEMPLATE template) {
		return smsTemplateManagerService.SaveOrUpdate(template);
	}
	
	/**
	 * 获取模板分类树
	 */
	@RequestMapping(value = "/smstpltree", method = RequestMethod.GET)
	public @ResponseBody List<TreeInfo> getTplTree() {
		return smsTemplateManagerService.TplTree();
	}
	
	@RequestMapping(value = "/smstplclass/{id}", method = RequestMethod.GET)
	public String GetTplClass(Model model, @PathVariable("id") String id) {
		if (id.equals("0") || id == null) {
			model.addAttribute("TplClass", new SMS_TEMPLATECLASS());
		} else {
			model.addAttribute("TplClass",
					smsTemplateManagerService.GetTplClassById(id));
		}
		return "/workflow/smsmanage/smstplclass";
	}

	@RequestMapping(value = "/smstplclass/{id}", method = RequestMethod.POST)
	public String SaveOrUpdate(@ModelAttribute("template") SMS_TEMPLATECLASS templateclass) {
		smsTemplateManagerService.SaveOrUpdate(templateclass);
		return "redirect:/app/smstemplate/smstemplateclass/"
				+ templateclass.getCLASSID();
	}
	
	//新建树节点
	@RequestMapping(value = "/newsmstplclass", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo CreateNewSmsTplclass(HttpServletRequest request) {
		String name = request.getParameter("name");
		String pid = request.getParameter("pid");
		return smsTemplateManagerService.CreateTplClassByName(name, pid);
	}
	
	//删除目录节点
	@RequestMapping(value = "/delsmstplclass", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo DeleteTplClass(HttpServletRequest request) {
		SmObjInfo obinfo = new SmObjInfo();
		String id = request.getParameter("id");
		smsTemplateManagerService.DeleteTplClassById(id);
		return obinfo;
	}
	
	//重命名
	@RequestMapping(value = "/smstplclass/rename", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo RemaneTplClass(HttpServletRequest request) {
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		return smsTemplateManagerService.RenameTplClass(id, name);
	}
	
	//删除节点
	@RequestMapping(value = "/delsmstemplate", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo DeleteTemplate(HttpServletRequest request) {
		SmObjInfo obinfo = new SmObjInfo();
		String ids = request.getParameter("id");
		String[] id = ids.split(",");
		for(int i = 0; i < id.length; i++){
			smsTemplateManagerService.DeleteTemplateById(id[i]);
		}
		obinfo.setDesc("删除成功！");
		return obinfo;
	}
	
	/**
	 * 
	 * @param id
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/smstemplates/{id}", method = RequestMethod.GET)
	public @ResponseBody Message GetTemplates(@PathVariable("id") String id, HttpServletRequest request) throws Exception {
		String currpage = request.getParameter("currpage");
		String pagesize = request.getParameter("pagesize");
		String template_name = RequestHelper.getParam(request, "template_name");
		
		if (StringUtils.isEmpty(currpage)) {
			currpage = "1";
		}
		if (StringHelper.isEmpty(pagesize)) {
			pagesize = "10";
		}
		int page = Integer.parseInt(currpage);
		
		return smsTemplateManagerService.FindAllTemplateInfo(id, page, Integer.parseInt(pagesize), template_name);
	}

	/**
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/alltemplates", method = RequestMethod.GET)
	public @ResponseBody Message FindAllTemplates(HttpServletRequest request) throws Exception {
		String currpage = request.getParameter("currpage");
		String pagesize = request.getParameter("pagesize");
		String template_name = RequestHelper.getParam(request, "template_name");

		if (StringUtils.isEmpty(currpage)) {
			currpage = "1";
		}
		if (StringHelper.isEmpty(pagesize)) {
			pagesize = "10";
		}
		int page = Integer.parseInt(currpage);

		return smsTemplateManagerService.FindAllTemplates( page, Integer.parseInt(pagesize), template_name);
	}

	/**
	 * 获取sms_template By ID
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/smstemplateinfo/{id}", method = RequestMethod.GET)
	public @ResponseBody SMS_TEMPLATE GetOpinion(@PathVariable("id") String id) {
		SMS_TEMPLATE sms_template = smsTemplateManagerService.GetTemplateById(id);
		return sms_template;
	}
	
	/**
	 * SMS_TEMPLATE 保存或更新
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/smstemplateinfo/{id}", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo SaveOrUpdate(@PathVariable("id") String id,
			@ModelAttribute("template") SMS_TEMPLATE template) {
		return smsTemplateManagerService.SaveOrUpdate(template);
	}

	/**
	 * 获取模板类别下拉
	 * @return
	 */
	@RequestMapping(value = "/tplclassdata", method = RequestMethod.POST)
	@ResponseBody
	public List<Map> GetTplClassData() {
		
		return smsTemplateManagerService.GetTplClassData();
	}
	/**
	 * @Author taochunda
	 * @Description 获取模板下拉
	 * @Date 2019-07-08 10:54
	 * @Param
	 * @return
	 **/
	@RequestMapping(value = "/templatedata", method = RequestMethod.POST)
	@ResponseBody
	public List<Map> GetTemplateData( HttpServletRequest request) throws UnsupportedEncodingException {
		String classId = RequestHelper.getParam(request, "classId");
		return smsTemplateManagerService.GetTemplateData(classId);
	}

	/****************************************常用参数模板相关 START********************************************/
	/**
	 * @Author taochunda
	 * @Description 获取当前人员可以调用的所有参数模板
	 * @Date 2019-07-15 16:30
	 * @Param [request]
	 * @return com.supermap.wisdombusiness.workflow.util.Page
	 **/
	@RequestMapping(value = "/paramtpl/", method = RequestMethod.GET)
	@ResponseBody
	public Message GetParamTpls(HttpServletRequest request) {
		return smsTemplateManagerService.GetParamTpl(request);
	}

	/**
	 * 新增
	 * */
	@RequestMapping(value = "/addparamtpl/", method = RequestMethod.POST)
	@ResponseBody
	public Message AddParamTpl(HttpServletRequest request) {
		return smsTemplateManagerService.AddParamTpl(request);
	}
	/**
	 * 更新
	 * */
	@RequestMapping(value = "/upparamtpl/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Message ModifyParamTpl(@PathVariable("id") String id, HttpServletRequest request) {
		return smsTemplateManagerService.ModifyParamTpl(id, request);
	}
	/**
	 * 新增
	 * */
	@RequestMapping(value = "/delparamtpl/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Message DeleteParamTpl(@PathVariable("id") String id, HttpServletRequest request) {
		return smsTemplateManagerService.DeleteParamTpl(id, request);
	}
	/****************************************常用参数模板相关 END********************************************/
}
