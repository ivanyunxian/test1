package com.supermap.wisdombusiness.workflow.web.wfd;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import com.alibaba.druid.util.StringUtils;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.wisdombusiness.framework.service.RoleService;
import com.supermap.wisdombusiness.web.ui.tree.Tree;
import com.supermap.wisdombusiness.workflow.model.Wfd_Actdef;
import com.supermap.wisdombusiness.workflow.model.Wfd_Const_Value;
import com.supermap.wisdombusiness.workflow.model.Wfd_Mater;
import com.supermap.wisdombusiness.workflow.model.Wfd_MaterClass;
import com.supermap.wisdombusiness.workflow.model.Wfd_Pass_Condition;
import com.supermap.wisdombusiness.workflow.model.Wfd_ProClass;
import com.supermap.wisdombusiness.workflow.model.Wfd_ProMater;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import com.supermap.wisdombusiness.workflow.model.Wfd_Role;
import com.supermap.wisdombusiness.workflow.model.Wfd_RoleClass;
import com.supermap.wisdombusiness.workflow.model.Wfd_SysMod;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.TreeInfo;
import com.supermap.wisdombusiness.workflow.service.wfd.SmActDef;
import com.supermap.wisdombusiness.workflow.service.wfd.SmConstHelper;
import com.supermap.wisdombusiness.workflow.service.wfi.SmSysMod;
import com.supermap.wisdombusiness.workflow.service.wfm.ProcessesService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmDropdownService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProDefService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProMaterService;
import com.supermap.wisdombusiness.workflow.util.Message;

/**
 * 
 * @author DreamLi
 *
 */
@Controller
@RequestMapping("/processes")
public class ProcessDesignController {

	@Autowired
	ProcessesService _ProcessesService;
    @Autowired
    private RoleService roleService;
	@Autowired
	SmProDefService _SmProDefService;

	@Autowired
	private SmSysMod _SmSysMod;

	@Autowired
	private SmProMaterService _SmProMaterService;

	@Autowired
	private SmConstHelper _SmConstHelper;

	@Autowired
	private SmDropdownService _SmDropdownService;
	
	
	@Autowired
	private SmActDef smActDef;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String ShowIndex(Model model) {
		return "/workflow/processes/index";
	}
	@RequestMapping(value = "/integrationmain", method = RequestMethod.GET)
	public String Showintegrationmain(Model model) {
		return "/workflow/processes/integrationmain";
	}
	
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String Showmain(Model model) {
		//YwLogUtil.addYwLog("访问：流程列表功能", ConstValue.SF.YES.Value, ConstValue.LOG.SEARCH);
		return "/workflow/processes/main";
	}

	@RequestMapping(value = "/matermain", method = RequestMethod.GET)
	public String ShowMaterMain(Model model) {
		//YwLogUtil.addYwLog("访问：资料模板功能", ConstValue.SF.YES.Value, ConstValue.LOG.SEARCH);
		return "/workflow/processes/matermain";
	}

	@RequestMapping(value = "/sysmodmain", method = RequestMethod.GET)
	public String ShowSysModMain(Model model) {
		return "/workflow/processes/sysmodmain";
	}

	@RequestMapping(value = "/wfdrolemain", method = RequestMethod.GET)
	public String ShowWfdRoleMain(Model model) {
		
		
		return "/workflow/processes/wfdrolemain";
	}
	/**
	 * @author JHX
	 * @DATE 2016-07-29
	 * 按钮控制界面
	 * 路由条件
	 * */
	@RequestMapping(value = "/btndesinger", method = RequestMethod.GET)
	public String btndesinger(Model model,HttpServletRequest request) {
		List<Map> lists = smActDef.getBtnRegiste();
		model.addAttribute("btns", lists);
		String actdefid = request.getParameter("actdefid");
		Wfd_Actdef wdf_actdef=null;
		if(!actdefid.equals("")&&actdefid!=null){
			wdf_actdef=smActDef.GetActDefByID(actdefid);
			if(wdf_actdef!=null&&(wdf_actdef.getBtnPermission()!=null||
					!wdf_actdef.getBtnPermission().equals(""))){
				    model.addAttribute("defaultValue", wdf_actdef.getBtnPermission());
			}
		}
		return "/workflow/processes/btndesinger";
	}
	
	/**
	 * @author JHX
	 * @DATE 2016-08-23 00:14
	 * 用户自定义参数
	 **/
	@RequestMapping(value = "/cusparamset", method = RequestMethod.GET)
	public String cusParamSet(Model model,HttpServletRequest request) {
		//用户自定义参数列表
		List<Map> lists = smActDef.getCommonDicByType("CUSPARAMS");
		model.addAttribute("keys", lists);
		String actdefid = request.getParameter("actdefid");
		Wfd_Actdef wdf_actdef=null;
		if(!actdefid.equals("")&&actdefid!=null){
			wdf_actdef=smActDef.GetActDefByID(actdefid);
			if(wdf_actdef!=null&&(wdf_actdef.getCustomeParamsSet()!=null&&
					!wdf_actdef.getCustomeParamsSet().equals(""))){
				    model.addAttribute("defaultValue", wdf_actdef.getCustomeParamsSet());
			}
		}
		return "/workflow/processes/cusparamset";
	}
	
	
	/**
	 * 按钮控制夜尿
	 * */
	@RequestMapping(value = "/routecondition", method = RequestMethod.GET)
	public String routecondition(Model model) {
		return "/workflow/processes/routecondition";
	}
	/**
	 * 获取路由条件
	 * */
	@RequestMapping(value = "/routecondition/{routeid}", method = RequestMethod.GET)
	@ResponseBody
	public List<Wfd_Pass_Condition> routecondition(@PathVariable String routeid) {
		return _SmProDefService.GetRouteCondition(routeid);
	}
	@RequestMapping(value = "/wfdpromatermain/{id}", method = RequestMethod.GET)
	public String ShowWfdProMaterMain(Model model, @PathVariable String id) {
		model.addAttribute("id", id);
		return "/workflow/processes/wfdpromatermain";
	}
	/**
	 * 加载流程设计器承载页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/processdesign", method = RequestMethod.GET)
	public String ShowProcessDesign(Model model) {
		return "/workflow/processes/processdesign";
	}

	/**
	 * 获取流程数据XML
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getdata", method = RequestMethod.GET)
	public void GetDataGet(HttpServletRequest request,
			HttpServletResponse response) {
		String ProdefID = request.getParameter("ID");
		String data = _ProcessesService.GetData(ProdefID);
		try {
			response.getOutputStream().write(data.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Silverlight数据交互
	 * 
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getdata", method = RequestMethod.POST)
	public void GetDataPost(HttpServletRequest request,
			HttpServletResponse response) {
		try {

			// google用法
			StringBuilder XmlText = new StringBuilder();
			Map<String, String[]> map = request.getParameterMap();
			Set<Entry<String, String[]>> set = map.entrySet();
			Iterator<Entry<String, String[]>> it = set.iterator();
			while (it.hasNext()) {
				Entry<String, String[]> entry = it.next();
				XmlText.append(entry.getKey().toString());
			}
			if (!XmlText.toString().equals("")) {
				XmlText.delete(0, 1);
				_ProcessesService.DataSave(XmlText.toString());
			} else// IE用法
			{
				_ProcessesService.DataSave(request.getInputStream());
			}
			//YwLogUtil.addYwLog("流程列表中保存流程设计操作", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
			// response.getOutputStream().write("保存成功！".getBytes("utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ----------------------------ACTDEF--------------------
	/**
	 * actdefid获取ACTDEF
	 * 
	 * @param model
	 * @param actdefid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/actdef/{actdefid}", method = RequestMethod.GET)
	public String GetActdef(Model model, @PathVariable String actdefid,
			HttpServletRequest request, HttpServletResponse response) {

		if (actdefid.equals("0") || actdefid == null) {
			model.addAttribute("Actdef", new Wfd_Actdef());
		} else {
			model.addAttribute("Actdef",
					_ProcessesService.GetActDefById(actdefid));
		}

		model.addAttribute("ActTypeList",
				_SmConstHelper.GetConstValueList("Act_Type"));
		model.addAttribute("ActList",
				_SmProDefService.getActdefListByActdefId(actdefid));
		//model.addAttribute("RoleList", _SmDropdownService.RoleDropdown());
		
		
		return "/workflow/processes/actdef";
	}

	/**
	 * 更新或保存ACTDEF
	 * 
	 * @param model
	 * @param actdef
	 * @param actdefid
	 * @param result
	 * @param status
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/actdef/{actdefid}", method = RequestMethod.POST)
	public String UpdateWfd_Actdef(Model model,
			@ModelAttribute("Actdef") Wfd_Actdef actdef,
			@PathVariable("actdefid") String actdefid, BindingResult result,
			SessionStatus status, HttpServletRequest request,
			HttpServletResponse response) {
		_ProcessesService.UpdateActDef(actdef);
		model.addAttribute("Actdef", actdef);
		//YwLogUtil.addYwLog("更新活动属性内容：活动名称：" + actdef.getActdef_Name() + ",办理角色："+actdef.getRole_Name()+",办理部门："+actdef.getActdef_Dept(), ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
		return "redirect:/app/processes/actdef/" + actdef.getActdef_Id();
	}

	// ------------------------SysMod-----------------------
	/**
	 * 获取组件树
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/sysmodtree", method = RequestMethod.GET)
	public @ResponseBody List<TreeInfo> getSysModTree(
			HttpServletRequest request, HttpServletResponse response) {
		return _SmProDefService.GetSysModTree();
	}

	/**
	 * 通过ID获取组件信息
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/sysmod/{id}", method = RequestMethod.GET)
	public @ResponseBody Wfd_SysMod GetSysMod(@PathVariable("id") String id,
			HttpServletRequest request, HttpServletResponse response) {
		return _SmSysMod.GetSysModById(id);
	}

	/**
	 * 通过actdefid获取当前关联组件信息
	 * 
	 * @param actdefid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/actdefsysmod/{actdefid}", method = RequestMethod.GET)
	public @ResponseBody Message getActToSysMod(
			@PathVariable("actdefid") String actdefid,
			HttpServletRequest request, HttpServletResponse response) {

		return _SmSysMod.GetActdefMod(actdefid);
	}

	/**
	 * 更新组件信息
	 * 
	 * @param sysmod
	 * @param id
	 * @param result
	 * @param status
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/sysmod/{id}", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo SaveOrUpdate(@ModelAttribute("Sysmod") Wfd_SysMod sysmod,
			@PathVariable("id") String id, BindingResult result,
			SessionStatus status, HttpServletRequest request,
			HttpServletResponse response) {
		return _SmSysMod.SaveOrUpdate(sysmod);
	}

	/**
	 * 删除组件
	 * 
	 * @param id
	 * @param status
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/delsysmod/{id}", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo DelSysMod(@PathVariable("id") String id,
			SessionStatus status, HttpServletRequest request,
			HttpServletResponse response) {
		//YwLogUtil.addYwLog("删除组件", ConstValue.SF.YES.Value,ConstValue.LOG.DELETE);
		return _SmSysMod.DeleteSysMod(id);
	}

	/**
	 * 删除组件目录
	 * 
	 * @param id
	 * @param status
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/delmodclass/{id}", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo DelModClass(@PathVariable("id") String id,
			SessionStatus status, HttpServletRequest request,
			HttpServletResponse response) {
		return _SmSysMod.DeleteModClass(id);
	}

	/**
	 * 新建组件目录
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/newmodclass", method = RequestMethod.POST)
	public @ResponseBody String CreateNewModClass(HttpServletRequest request,
			HttpServletResponse response) {
		String name = request.getParameter("name");
		String pid = request.getParameter("pid");
		int index = Integer.parseInt(request.getParameter("index"));
		return _SmSysMod.CreateModClassByName(name, pid, index);

	}

	/**
	 * 重命名组件目录
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/modclass/rename", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo RemaneSysModClass(HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		return _SmSysMod.RenameModClass(id, name);

	}

	/**
	 * 更新组件关系索引
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/newacttomodindex", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo UpdateWfd_Tr_ActToSysMod(
			HttpServletRequest request, HttpServletResponse response) {
		String json = request.getParameter("modindex");
		String actdefid = request.getParameter("actdefid");
		JSONArray array = JSONArray.fromObject(json);
		_SmSysMod.UpdateActToMod(array, actdefid);
		SmObjInfo obinfo = new SmObjInfo();
		obinfo.setDesc("更新成功！");
		return obinfo;
	}

	/**
	 * 保存Tr_ActToSysMod
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/newacttosysmod", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo CreateWfd_Tr_ActToSysMod(
			HttpServletRequest request, HttpServletResponse response) {
		String sysmodid = request.getParameter("sysmodid");
		String actdefid = request.getParameter("actdefid");
		int index = Integer.parseInt(request.getParameter("sysmodindex"));
		SmObjInfo obInfo = new SmObjInfo();
		obInfo.setID(_SmSysMod.CreateWfd_Tr_ActToSysMod(actdefid, sysmodid,
				index));
		obInfo.setDesc("保存成功");
		//YwLogUtil.addYwLog("访问:流程列表中进行添加业务组件操作", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
		return obInfo;
	}

	/**
	 * 删除Tr_ActToSysMod
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/delacttosysmod", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo DeleteWfd_Tr_ActToSysMod(
			HttpServletRequest request, HttpServletResponse response) {
		//String sysmodid = request.getParameter("sysmodid");
		//String actdefid = request.getParameter("actdefid");
		String acttomodid=request.getParameter("acttomodid");
		SmObjInfo obInfo = new SmObjInfo();
		obInfo.setID(acttomodid);
		obInfo.setDesc(_SmSysMod.DeleteWfd_Tr_ActToSysMod(acttomodid));
		//YwLogUtil.addYwLog("访问:流程列表中进行删除业务组件操作", ConstValue.SF.YES.Value,ConstValue.LOG.DELETE);
		return obInfo;
	}
    /**
     * 设置组件只读
     * */
	@RequestMapping(value = "/setmodreadonly", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo SetModReadonly(
			HttpServletRequest request, HttpServletResponse response) {
		String acttomodid=request.getParameter("acttomodid");
		String readonlyString=request.getParameter("readonly");
		SmObjInfo obInfo = new SmObjInfo();
		obInfo.setID(acttomodid);
		if(readonlyString!=null&&!"".equals(readonlyString))
		obInfo.setDesc(_SmSysMod.setModReadonly(acttomodid,Integer.parseInt(readonlyString)));
		return obInfo;
	}
	// -----------------------PRODEF------------------------

	/**
	 * 获取流程树
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getprodeftree", method = RequestMethod.GET)
	public @ResponseBody List<TreeInfo> getProdefTree(
			HttpServletRequest request, HttpServletResponse response) {
		return _SmProDefService.GetProdefTree();
	}
	@RequestMapping(value = "/getprodefasynctree", method = RequestMethod.GET)
	public @ResponseBody List<TreeInfo> getprodefasytree(
			HttpServletRequest request, HttpServletResponse response) {
	    String iDString=request.getParameter("id");
	    String ISRoute=request.getParameter("route");
	    String showAll=request.getParameter("showall");
		return _SmProDefService.GetProdefAsyncTree(iDString,ISRoute,showAll);
	}
	/**
	 * 获取流程信息
	 * 
	 * @param prodefid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/prodef/{prodefid}", method = RequestMethod.GET)
	@ResponseBody
	public Wfd_Prodef GetProdef(@PathVariable String prodefid,
			HttpServletRequest request, HttpServletResponse response) {
		return _SmProDefService.GetProdefById(prodefid);

	}

	/**
	 * 删除流程定义
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/prodef/delect", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo DeleteProdef(HttpServletRequest request,
			HttpServletResponse response) {
		String prodefid = request.getParameter("id");
		return _SmProDefService.DelectProdef(prodefid);
	}

	/**
	 * 
	 * @param prodef
	 * @param prodefid
	 * @param result
	 * @param status
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/prodef/{prodefid}", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo SaveOrUpdate_Prodef(
			@ModelAttribute("Prodef") Wfd_Prodef prodef,
			@PathVariable("prodefid") String prodefid, BindingResult result,
			SessionStatus status, HttpServletRequest request,
			HttpServletResponse response) {

		return _SmProDefService.SaveOrUpdate_Prodef(prodef);
	}

	/**
	 * 按名称索引新建流程定义
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/newprodef/{name}/{index}", method = RequestMethod.POST)
	public @ResponseBody String CreateNewProdef(HttpServletRequest request,
			HttpServletResponse response) {
		String name = request.getParameter("name");
		String pid = request.getParameter("pid");
		int index = Integer.parseInt(request.getParameter("index"));
		return _SmProDefService.CreateNewProdefByName(name, pid, index);

	}

	// ---------------------------PROCLASS---------------------

	/**
	 * 新建流程定义目录
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/newproclass", method = RequestMethod.POST)
	public @ResponseBody String CreateNewProClass(HttpServletRequest request,
			HttpServletResponse response) {
		String name = request.getParameter("name");
		String pid = request.getParameter("pid");
		int index = Integer.parseInt(request.getParameter("index"));
		return _SmProDefService.CreateNewProClassByName(name, pid, index);

	}

	/**
	 * 获取流程目录
	 * 
	 * @param model
	 * @param proclassid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/proclass/{proclassid}", method = RequestMethod.GET)
	public String GetProClass(Model model, @PathVariable String proclassid,
			HttpServletRequest request, HttpServletResponse response) {

		if (proclassid.equals("0") || proclassid == null) {
			model.addAttribute("ProClass", new Wfd_ProClass());
		} else {
			model.addAttribute("ProClass",
					_ProcessesService.GetProClassByID(proclassid));
		}
		request.setAttribute("proclassList",
				_SmProDefService.ProClassTree(null, null, null));
		return "/workflow/processes/proclass";
	}

	/**
	 * 更新流程目录信息
	 * 
	 * @param model
	 * @param proclass
	 * @param proclassid
	 * @param result
	 * @param status
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/proclass/{proclassid}", method = RequestMethod.POST)
	public String SaveOrUpdate_ProClass(Model model,
			@ModelAttribute("ProClass") Wfd_ProClass proclass,
			@PathVariable("proclassid") String proclassid,
			BindingResult result, SessionStatus status,
			HttpServletRequest request, HttpServletResponse response) {
		_SmProDefService.SaveOrUpdate_ProClass(proclass);
		model.addAttribute("Prodef", proclass);
		request.setAttribute("proclassList",
				_SmProDefService.ProClassTree(null, null, null));
		return "redirect:/app/processes/proclass/"
				+ proclass.getProdefclass_Id();
	}

	/**
	 * 流程目录重命名
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/proclass/rename", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo RemaneProClass(HttpServletRequest request,
			HttpServletResponse response) {
		String proclass_idString = request.getParameter("id");
		String proclass_nameString = request.getParameter("name");
		return _SmProDefService.RenameProClass(proclass_idString,
				proclass_nameString);

	}
	/**
	 * 删除流程分类
	 * */
	@RequestMapping(value = "/proclass/delect", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo delectProClass(HttpServletRequest request,
			HttpServletResponse response) {
		String proclass_idString = request.getParameter("id");
		return _SmProDefService.DelectProClass(proclass_idString);

	}

	@RequestMapping(value = "/treeview", method = RequestMethod.GET)
	public String TreeView() {
		return "/workflow/processes/treeview";
	}

	@RequestMapping(value = "/sysmodindex/{id}", method = RequestMethod.GET)
	public String SysModTree(Model model, @PathVariable String id) {
		model.addAttribute("id", id);
		return "/workflow/processes/sysmod";
	}

	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public String load() {
		return "/workflow/processes/load";
	}

	@RequestMapping(value = "/top", method = RequestMethod.GET)
	public String top() {
		return "/workflow/processes/top";
	}

	// -------------收件资料--------------
	/**
	 * 获取该流程定义的资料列表GET
	 * 
	 * @param model
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	//分页处理
	@RequestMapping(value = "/wfdpromaterlist/{id}", method = RequestMethod.GET)
	public @ResponseBody Message Wfd_ProMaterList(Model model,
			@PathVariable("id") String id,
			@RequestParam(value="page", required=false) String page,
			@RequestParam(value="rows", required=false)String rows ,
			HttpServletRequest request,
			HttpServletResponse response) {
		String currpage = request.getParameter("currpage");
		if (StringUtils.isEmpty(currpage)) {
			currpage = "1";
		}
		if (request.getAttribute("page")!=null ) {
			page=request.getParameter("page");
		}
		if (request.getAttribute("rows")!=null ) {
			rows=request.getParameter("rows");
		}
		return _SmProMaterService.FindAllWfd(id, Integer.parseInt(page), Integer.parseInt(rows));
	}

	/**
	 * 获取该流程定义的资料列表POST
	 * 
	 * @param model
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/wfdpromaterlist/{id}", method = RequestMethod.POST)
	public @ResponseBody Message Wfd_ProMaterList2(Model model,
			@PathVariable("id") String id,  
			@RequestParam(value="page", required=false) String page,
			@RequestParam(value="rows", required=false)String rows ,HttpServletRequest request,
			HttpServletResponse response) {
		String currpage = request.getParameter("currpage");
		if (StringUtils.isEmpty(currpage)) {
			currpage = "1";
		}
		if (request.getAttribute("page")!=null ) {
			page=request.getParameter("page");
		}
		if (request.getAttribute("rows")!=null ) {
			rows=request.getParameter("rows");
		}
		return _SmProMaterService.FindAllWfd(id, Integer.parseInt(page), Integer.parseInt(rows));
	}

	@RequestMapping(value = "/wfdpromater/{id}", method = RequestMethod.GET)
	public String WfdpromaterIndex(Model model, @PathVariable("id") String id,
			HttpServletRequest request, HttpServletResponse response) {
		return "/workflow/processes/wfdpromater";
	}

	/**
	 * 删除单个资料
	 * 
	 * @param model
	 * @param id
	 * @param materid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/wfdpromater/{id}/{materid}", method = RequestMethod.POST)
	public String DeleteWfdpromater(Model model, @PathVariable("id") String id,
			@PathVariable("materid") String materid,
			HttpServletRequest request, HttpServletResponse response) {
		_SmProMaterService.DeleteWfd_ProMaterById(materid);
		return "redirect:/app/processes/wfdpromater/" + id;
	}

	/**
	 * 批量删除流程定义资料
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/delpromaterbyids", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo WfdMaterByIds(HttpServletRequest request,
			HttpServletResponse response) {
		String json = request.getParameter("json");
		JSONArray array = JSONArray.fromObject(json);
		return _SmProMaterService.DeleteProMaterByIDs(array);
	}

	@RequestMapping(value = "/delwfdmater", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo DeleteWfdMater(HttpServletRequest request,
			HttpServletResponse response) {
		SmObjInfo obinfo = new SmObjInfo();
		String id = request.getParameter("id");
		_SmProMaterService.DeleteWfd_MaterById(id);
		return obinfo;
	}

	@RequestMapping(value = "/delwfdmaterclass", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo DeleteWfdMaterClass(
			HttpServletRequest request, HttpServletResponse response) {
		SmObjInfo obinfo = new SmObjInfo();
		String id = request.getParameter("id");
		_SmProMaterService.DeleteWfd_MaterClassById(id);
		return obinfo;
	}

	@RequestMapping(value = "/wfdmatertree", method = RequestMethod.GET)
	public @ResponseBody List<TreeInfo> getWfdmaterTree(
			HttpServletRequest request, HttpServletResponse response) {
		return _SmProMaterService.WfdMaterTree();
	}

	@RequestMapping(value = "/newwfdmater", method = RequestMethod.POST)
	public @ResponseBody String CreateNewWfdmater(HttpServletRequest request,
			HttpServletResponse response) {
		String name = request.getParameter("name");
		String pid = request.getParameter("pid");
		int index = Integer.parseInt(request.getParameter("index"));
		return _SmProMaterService.CreateMaterByName(name, pid, index);
	}

	@RequestMapping(value = "/newwfdmaterclass", method = RequestMethod.POST)
	public @ResponseBody String CreateNewWfdmaterclass(
			HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("name");
		String pid = request.getParameter("pid");
		int index = Integer.parseInt(request.getParameter("index"));
		return _SmProMaterService.CreateMaterClassByName(name, pid, index);
	}

	@RequestMapping(value = "/wfdmaterclass/rename", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo RemaneWfdClass(HttpServletRequest request,
			HttpServletResponse response) {
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		return _SmProMaterService.RenameWfdMaterClass(id, name);

	}

	/**
	 * 获取当前资料信息
	 *
	 * @param id
	 *            String ProdefID
	 * @param materid
	 *            String Wfd_ProMaterID
	 */
	@RequestMapping(value = "/wfdpromateredit/{id}/{materid}", method = RequestMethod.GET)
	public String GetWfd_ProMater(Model model, @PathVariable("id") String id,
			@PathVariable("materid") String materid,
			HttpServletRequest request, HttpServletResponse response) {
		model.addAttribute("prodefid", id);
		if (materid.equals("0") || materid == null) {
			model.addAttribute("Promater", new Wfd_ProMater());
		} else {
			model.addAttribute("Promater",
					_SmProMaterService.GetWfd_ProMaterById(materid));
		}
		model.addAttribute("MaterialTypeList",
				_SmConstHelper.GetConstValueList("Material_Type"));
		model.addAttribute("TransactionTypeList",
				_SmConstHelper.GetConstValueList("Transaction_Type"));
		List<Wfd_Const_Value> consts=_SmConstHelper.GetConstValueList("Material_Need");
		model.addAttribute("MaterialNeedList",consts);
		return "/workflow/processes/wfdpromateredit";
	}

	@RequestMapping(value = "/wfdpromateredit/{id}/{materid}", method = RequestMethod.POST)
	public String SaveOrUpdateWfd_ProMater(Model model,
			@PathVariable("id") String id,
			@PathVariable("materid") String materid,
			@ModelAttribute("Promater") Wfd_ProMater promater,
			HttpServletRequest request, HttpServletResponse response) {
		promater.setProdef_Id(id);
		_SmProMaterService.SaveOrUpdate(promater);
		return "redirect:/app/processes/wfdpromateredit/" + id + "/"
				+ promater.getMaterial_Id();
	}

	/**
	 * 
	 * @param model
	 * @param id
	 *            String prodefid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/matertopromater/{id}", method = RequestMethod.POST)
	public @ResponseBody String MaterToProNater(Model model,
			@PathVariable("id") String id, HttpServletRequest request,
			HttpServletResponse response) {
		String json = request.getParameter("json");
		JSONArray array = JSONArray.fromObject(json);
		_SmProMaterService.MaterToProMater(array, id);
		return "选取成功";
	}

	/**
	 * 获取Wfd_Mater By ID
	 * 
	 * @param model
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/wfdmater/{id}", method = RequestMethod.GET)
	public @ResponseBody Wfd_Mater GetWfd_Mater(Model model,
			@PathVariable("id") String id, HttpServletRequest request,
			HttpServletResponse response) {
		Wfd_Mater wfd_Mater = _SmProMaterService.GetWfd_MaterById(id);
		if(wfd_Mater != null){
			//YwLogUtil.addYwLog("访问：收件资料模版定义：" + wfd_Mater.getMaterial_Name(), ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);	
		}
		return wfd_Mater;
	}

	/**
	 * Wfd_Mater 保存或更新
	 * 
	 * @param model
	 * @param id
	 * @param mater
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/wfdmater/{id}", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo SaveOrUpdate(Model model,
			@PathVariable("id") String id,
			@ModelAttribute("Mater") Wfd_Mater mater,
			HttpServletRequest request, HttpServletResponse response) {
		return _SmProMaterService.SaveOrUpdate(mater);
	}

	@RequestMapping(value = "/wfdmaterclass/{id}", method = RequestMethod.GET)
	public String GetWfd_MaterClass(Model model, @PathVariable("id") String id,
			HttpServletRequest request, HttpServletResponse response) {
		if (id.equals("0") || id == null) {
			model.addAttribute("MaterClass", new Wfd_MaterClass());
		} else {
			model.addAttribute("MaterClass",
					_SmProMaterService.GetWfd_MaterClassById(id));
		}
		model.addAttribute("MaterClassList",
				_SmDropdownService.MaterClassDropdown());
		return "/workflow/processes/wfdmaterclass";
	}

	@RequestMapping(value = "/wfdmaterclass/{id}", method = RequestMethod.POST)
	public String SaveOrUpdate(Model model, @PathVariable("id") String id,
			@ModelAttribute("Mater") Wfd_MaterClass materclass,
			HttpServletRequest request, HttpServletResponse response) {
		_SmProMaterService.SaveOrUpdate(materclass);
		return "redirect:/app/processes/wfdmaterclass/"
				+ materclass.getMaterialtype_Id();
	}

	// --------------------WFD_ROLE----------------------------
	@RequestMapping(value = "/wfdroletree", method = RequestMethod.GET)
	public @ResponseBody List<TreeInfo> getWfdRoleTree(
			HttpServletRequest request, HttpServletResponse response) {
		return _ProcessesService.GetRoleTree();
	}

	@RequestMapping(value = "/wfdrole/{id}", method = RequestMethod.GET)
	public String GetWfdRole(Model model, @PathVariable("id") String id,
			HttpServletRequest request, HttpServletResponse response) {
		if (id.equals("0") || id == null) {
			model.addAttribute("Role", new Wfd_Role());
		} else {
			model.addAttribute("Role", _ProcessesService.GetWfdRoleById(id));
		}
		model.addAttribute("RoleClassList",
				_SmDropdownService.RoleClassDropdown());
		return "/workflow/processes/wfdrole";
	}

	@RequestMapping(value = "/wfdrole/{id}", method = RequestMethod.POST)
	public String SaveOrUpdate(Model model, @PathVariable("id") String id,
			@ModelAttribute("Role") Wfd_Role role, HttpServletRequest request,
			HttpServletResponse response) {
		_ProcessesService.SaveOrUpdate(role);
		return "redirect:/app/processes/wfdrole/" + role.getRole_Id();
	}

	@RequestMapping(value = "/newwfdrole", method = RequestMethod.POST)
	public @ResponseBody String CreateNewWfdRole(HttpServletRequest request,
			HttpServletResponse response) {
		String name = request.getParameter("name");
		String pid = request.getParameter("pid");
		int index = Integer.parseInt(request.getParameter("index"));
		return _ProcessesService.CreateWfdRoleByName(name, pid, index);
	}

	@RequestMapping(value = "/delwfdrole/{id}", method = RequestMethod.POST)
	public @ResponseBody String DeleteWfdRole(@PathVariable("id") String id,
			HttpServletRequest request, HttpServletResponse response) {
		_ProcessesService.DeleteWfdRoleById(id);
		return "true";
	}

	// --------------------------------WFD_ROLECLASS---------------------------
	@RequestMapping(value = "/wfdroleclass/{id}", method = RequestMethod.GET)
	public String GetWfdRoleClass(Model model, @PathVariable("id") String id,
			HttpServletRequest request, HttpServletResponse response) {
		if (id.equals("0") || id == null) {
			model.addAttribute("RoleClass", new Wfd_RoleClass());
		} else {
			model.addAttribute("RoleClass",
					_ProcessesService.GetWfdRoleClassById(id));
		}
		model.addAttribute("RoleClassList",
				_SmDropdownService.RoleClassDropdown());
		return "/workflow/processes/wfdroleclass";
	}

	@RequestMapping(value = "/wfdroleclass/{id}", method = RequestMethod.POST)
	public String SaveOrUpdate(Model model, @PathVariable("id") String id,
			@ModelAttribute("RoleClass") Wfd_RoleClass roleclass,
			HttpServletRequest request, HttpServletResponse response) {
		_ProcessesService.SaveOrUpdate(roleclass);
		return "redirect:/app/processes/wfdroleclass/"
				+ roleclass.getRoleclass_Id();
	}

	@RequestMapping(value = "/newwfdroleclass", method = RequestMethod.POST)
	public @ResponseBody String CreateNewWfdRoleClass(
			HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("name");
		String pid = request.getParameter("pid");
		int index = Integer.parseInt(request.getParameter("index"));
		return _ProcessesService.CreateWfdRoleClassByName(name, pid, index);
	}

	@RequestMapping(value = "/delwfdroleclass/{id}", method = RequestMethod.POST)
	public @ResponseBody String DeleteWfdRoleClass(
			@PathVariable("id") String id, HttpServletRequest request,
			HttpServletResponse response) {
		_ProcessesService.DeleteWfdRoleClassById(id);
		return "true";
	}

	
	/**
	 * 获取角色树
	 * */
	@RequestMapping(value = "/getroletree", method = RequestMethod.GET)
	@ResponseBody
	public List<Tree> GetRoleTree(){
		return  roleService.getRoleTree();
	}
	
	@RequestMapping(value = "/rebuildindex/{from}/{to}", method = RequestMethod.POST)
	@ResponseBody
	public boolean RebuildWorkflowNodeIndex(@PathVariable String from,@PathVariable String to){
		return _ProcessesService.RebuildWorkflowNodeIndex(from,to);
		
	}
	
	
	//流程复制
	@RequestMapping(value = "/copyprodef/{prodefid}/{prodefcode}", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo CopyProDef(@PathVariable String prodefid,@PathVariable String prodefcode,HttpServletRequest request){
		String rule=request.getParameter("rule");
		if(rule!=null&&!rule.equals("")){
			return _ProcessesService.CopyProDef(prodefid,prodefcode,rule);
		}
		else{
			return null;
		}
	}

	/**
	 * 新建版本
	 * @param prodefid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/version/{prodefid}",produces={"application/json"}, method = RequestMethod.POST)
	@ResponseBody
	public List<TreeInfo> createVersion(
			@PathVariable String prodefid,
			HttpServletRequest request,
			HttpServletResponse response){
		String verisonrule = request.getParameter("copyrules");
		List<TreeInfo> res =  _ProcessesService.createVersion(prodefid, verisonrule);
		return res;
	}
	@RequestMapping(value = "/enableordisable",produces={"application/json"}, method = RequestMethod.GET)
	@ResponseBody
	public List<TreeInfo> updateProcessStatus(
			HttpServletRequest request,
			HttpServletResponse response){
		String params = request.getParameter("params");
		List<TreeInfo> info = _ProcessesService.updateProdefStatus(params);
		return info;

	}

	@RequestMapping(value="/isused/{prodefid}",method=RequestMethod.GET)
	@ResponseBody
	public int IsUsed(Model model,@PathVariable String prodefid,HttpServletRequest request,HttpServletResponse response){
		int used=0;
		boolean isUsed = _ProcessesService.isCanChangeProdef(prodefid);
		if(isUsed==false){
			used=1;
		}
		return used;//  0为可以修改；1为不能修改
	}
	
}
