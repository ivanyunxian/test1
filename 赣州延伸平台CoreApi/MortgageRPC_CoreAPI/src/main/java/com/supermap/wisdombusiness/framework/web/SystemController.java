package com.supermap.wisdombusiness.framework.web;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.Clob;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;





import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.framework.model.SystemModule;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.model.GlobalConfig;
import com.supermap.wisdombusiness.framework.model.System;
import com.supermap.wisdombusiness.framework.service.GlobalConfigService;
import com.supermap.wisdombusiness.framework.service.SystemModuleService;
import com.supermap.wisdombusiness.framework.service.SystemService;
import com.supermap.wisdombusiness.utility.Helper;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;

/**
 * 
 * @Description:系统定义控制器
 * @author 刘树峰
 * @date 2015年7月10日 下午1:38:46
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/framework/system")
public class SystemController {

	private static final Log logger = LogFactory.getLog(SystemController.class);

	//系统定义service
	@Autowired
	private SystemService systemService;

	//系统模板service
	@Autowired
	private SystemModuleService moduleService;
	
	
	//系统模板service
	@Autowired
	private GlobalConfigService gloabalConfigService;

	//跳转路径
	private final String prefix = "/framework/system/";

	/**
	 * 系统配置页
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String ShowManageIndex(Model model) {
		model.addAttribute("systemattribute", new System());
		YwLogUtil.addYwLog("访问:系统配置管理功能", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return prefix + "manage";
	}
	/**
	 * 前端配置页
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/front", method = RequestMethod.GET)
	public String SystemFront(Model model) {
		return "/framework/sysconfig/index";
	}
	/**
	 * 获取系统列表
	 * */
	@RequestMapping(value = "/getSystems", method = RequestMethod.POST)
	public @ResponseBody Message getSystemList(@RequestParam("page") Integer page,
			@RequestParam("rows") Integer rows, HttpServletRequest request,
			HttpServletResponse response) {
		String condition = "1>0";
		Page p = systemService.getPagedSystem(page, rows, condition);
		Message m = new Message();
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		return m;
	}
	
	/**
	 * 编辑系统信息页
	 * @作者 diaoliwei
	 * @创建时间 2015年7月2日上午11:16:59
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String ShowEditPage(Model model, HttpServletRequest request, HttpServletResponse response){
		System system = new System();
		String id = request.getParameter("id");
		String url = request.getParameter("url");
		String method = "post";
		if(!StringUtils.isEmpty(id)){
			system = systemService.getSystem(id);
			method = "put";
		}
		model.addAttribute("systemattribute", system);
		model.addAttribute("url",url);
		model.addAttribute("method",method);
		return prefix + "editSystem";
	}
	
	/**
	 * 获取系统名称导航
	 * @param request
	 * @param response
	 * @author diaoliwei
	 * @return
	 */
	@RequestMapping(value = "/getSystemList", method = RequestMethod.POST)
	public @ResponseBody Message getSystemList( HttpServletRequest request,
			HttpServletResponse response) {
		Subject user = SecurityUtils.getSubject();
		Message m = new Message();
		if (null != user) {
			User u =  Global.getCurrentUserInfo();
			if(null != u){
				List<System> list = systemService.getSystemsByUser(u);
				m.setRows(list);
				m.setTotal(list.size());
			}
		}
		return m;
	}

	/**
	 * 提交并保存系统定义
	 * */
	@RequestMapping(value = "/addSystem", method = RequestMethod.POST)
	public @ResponseBody ResultMessage addSystem(@ModelAttribute("systemattribute") System system,
			BindingResult result, SessionStatus status,
			HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (result.hasErrors()) {
			logger.error(result);
			msg.setMsg(result.toString());
		} else {
			try {
				systemService.saveSystem(system);
				status.setComplete();
				msg.setMsg(system.getId());
				msg.setSuccess("true");
				YwLogUtil.addYwLog("新增系统定义", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
			} catch (Exception e) {
				logger.error(e);
				msg.setMsg(e.getMessage());
				YwLogUtil.addYwLog("新增系统定义", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
			}
		}
		return msg;
	}

	/**
	 * 更新系统定义
	 * 
	 **/
	@RequestMapping(value = "/updateSystem/{id}", method = RequestMethod.PUT)
	public @ResponseBody ResultMessage updateSystem(@PathVariable("id") String id,
			@ModelAttribute("systemattribute") System system,
			BindingResult result, SessionStatus status,
			HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (result.hasErrors()) {
			logger.error(result);
			msg.setMsg(result.toString());
		} else {
			try {
				system.setId(id);
				systemService.updateSystem(system);
				msg.setMsg(id);
				msg.setSuccess("true");
				YwLogUtil.addYwLog("更新系统定义", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
			} catch (Exception e) {
				logger.error(e);
				msg.setMsg(e.getMessage());
				YwLogUtil.addYwLog("更新系统定义", ConstValue.SF.NO.Value,ConstValue.LOG.UPDATE);
			}
		}
		return msg;
	}

	/**
	 * 删除系统定义
	 * 
	 * */
	@RequestMapping(value = "/deleteSystem/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage deleteSystem(@PathVariable("id") String id,
			HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (StringUtils.hasLength(id)) {
			try {
				systemService.deleteSystemIncludeModuleTree(id);
				msg.setSuccess("true");
				YwLogUtil.addYwLog("删除系统定义", ConstValue.SF.YES.Value,ConstValue.LOG.DELETE);
			} catch (Exception e) {
				logger.error(e);
				msg.setMsg(e.getMessage());
				YwLogUtil.addYwLog("删除系统定义", ConstValue.SF.NO.Value,ConstValue.LOG.DELETE);
			}
		} else {
			msg.setMsg("无该系统");
		}
		return msg;
	}

	/**
	 * 转到系统功能模块树页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{id}/modules", method = RequestMethod.GET)
	public String showSystemModuleTree(Model model,
			@PathVariable("id") String id, HttpServletRequest request,
			HttpServletResponse response) {
		System system = systemService.getSystem(id);
		SystemModule module = new SystemModule();
		model.addAttribute("system", system);
		model.addAttribute("sysId", id);
		model.addAttribute("module", module);
		return "/framework/module/manage";
	}
	
	/**
	 * 编辑系统模型信息
	 * @作者 diaoliwei
	 * @创建时间 2015年7月2日下午3:08:53
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/editModule/{parentId}", method = RequestMethod.GET)
	public String ShowEditSystemModulePage(@PathVariable("parentId") String parentId, Model model, HttpServletRequest request,
			HttpServletResponse response){
		SystemModule systemModule = new SystemModule();
		String id = request.getParameter("id");
		String url = request.getParameter("url");
		String method = "post";
		if(!StringUtils.isEmpty(id)){
			systemModule = moduleService.getSysModule(id);
			method = "put";
		}
		model.addAttribute("module", systemModule);
		model.addAttribute("url",url);
		model.addAttribute("method",method);
		if("null".equals(parentId)){
			parentId = "";
		}
		model.addAttribute("parentId",parentId);
		return "/framework/module/editModule";
	}

	/**
	 * 获取系统功能模块树,返回构建成树结构的json对象列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{sysid}/moduletree", method = RequestMethod.POST)
	public @ResponseBody List<SystemModule> getSysModuleTree(
			@PathVariable("sysid") String id, HttpServletRequest request,
			HttpServletResponse response) {
		List<SystemModule> modules = moduleService.getSysModules(id);
		List<SystemModule> resultmodules = Helper.createSysModuleTree(modules);
		return resultmodules;
	}
	
	/**
	 * 新增一个新模块
	 * */
	@RequestMapping(value = "/{sysid}/addModule")
	public @ResponseBody ResultMessage saveModule(@PathVariable("sysid") String sysid,
			@ModelAttribute("module") SystemModule module,
			BindingResult result, SessionStatus status,
			HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (result.hasErrors()) {
			logger.error(result);
			msg.setMsg(result.toString());
		} else {
			try {
				System system = systemService.getSystem(sysid);
				module.setSystem(system);
				moduleService.saveModule(module);
				status.setComplete();
				msg.setMsg(module.getId());
				msg.setSuccess("true");
				YwLogUtil.addYwLog("新增系统定义子模块", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
			} catch (Exception e){
				logger.error(e);
				msg.setMsg(e.getMessage());
				YwLogUtil.addYwLog("新增系统定义子模块", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
			}
		}
		return msg;
	}

	/**
	 * 更新一个模块
	 * */
	@RequestMapping(value = "/{sysid}/modules/{id}", method = RequestMethod.PUT)
	public @ResponseBody ResultMessage updateModule(
			@PathVariable("sysid") String sysid, @PathVariable("id") String id,
			@ModelAttribute("module") SystemModule module,
			BindingResult result, SessionStatus status,
			HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (result.hasErrors()) {
			logger.error(result);
			msg.setMsg(result.toString());
		} else {
			try {
				System system = systemService.getSystem(sysid);
				module.setSystem(system);
				module.setId(id);
				moduleService.updateModule(module);
				msg.setMsg(id);
				msg.setSuccess("true");
				YwLogUtil.addYwLog("更新系统定义子模块", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
			} catch (Exception e) {
				logger.error(e);
				msg.setMsg(e.getMessage());
				YwLogUtil.addYwLog("更新系统定义子模块", ConstValue.SF.NO.Value,ConstValue.LOG.UPDATE);
			}
		}
		return msg;
	}

	/**
	 * 删除一个模块
	 * */
	@RequestMapping(value = "/{sysid}/modules/{id}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage deleteModule(
			@PathVariable("sysid") String sysid, @PathVariable("id") String id,
			HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (StringUtils.hasLength(id)) {
			try {
				moduleService.deleteModuleTree(id);
				msg.setSuccess("true");
				YwLogUtil.addYwLog("删除系统定义子模块", ConstValue.SF.YES.Value,ConstValue.LOG.DELETE);
			} catch (Exception e) {
				logger.error(e);
				msg.setMsg(e.getMessage());
				YwLogUtil.addYwLog("删除系统定义子模块", ConstValue.SF.NO.Value,ConstValue.LOG.DELETE);
			}
		} else {
			msg.setMsg("无该模块");
		}
		return msg;
	}
	/**
	 * @authorJHX
	 * 增加系统配置信息入库功能界面
	 * @创建时间：2016-07-13
	 * */
	@RequestMapping(value = "/config", method = RequestMethod.GET)
	public String systemConfigInstance(Model model) {
		model.addAttribute("systemattribute", new System());
		YwLogUtil.addYwLog("访问:系统配置管理功能", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return prefix + "configuration";
	}
	/**
	 * @authorJHX
	 * 执行入库操作
	 * @创建时间：2016-07-13
	 * */
	@RequestMapping(value = "/cfgInstance", method = RequestMethod.POST)
	@ResponseBody
	public ResultMessage configInfoInstance(Model model,
			String configureation) {
		   ResultMessage msg = new ResultMessage();
			String res = gloabalConfigService.excuteConfigurationStorage(configureation);
			if(res.equals("true")){
				msg.setMsg("入库成功");
			}else{
				msg.setMsg("入库失败");
			}
		    return msg;
	}
	/**
	 * @authorJHX
	 * 执行入库操作
	 * @创建时间：2016-07-13
	 * 判断配置信息是否已经入库。
	 * */
	@RequestMapping(value = "/checkIsStorage", method = RequestMethod.POST)
	@ResponseBody
	public ResultMessage checkIsStorage(Model model) {
		    ResultMessage msg = new ResultMessage();
			boolean res = gloabalConfigService.chekIsStorage();
			if(res){
				msg.setMsg("true");
			}else{
				msg.setMsg("false");
			}
		    return msg;
	}
	/**
	 * @authorJHX
	 * 执行入库操作
	 * @创建时间：2016-07-13
	 * */
	@RequestMapping(value = "/getConfiguration", method = RequestMethod.POST)
	@ResponseBody
	public String getConfiguration(Model model) {
		ResultMessage msg = new ResultMessage();
		List<Map> map = gloabalConfigService.getConfiguration();
		GlobalConfig cfg = new GlobalConfig();
		String reString = ""; 
		if(map!=null&&map.size()>0){
			reString =(String)map.get(0).get("CONTENT_CONFIG");
			try{
			/*	Reader is = clob.getCharacterStream();// 得到流 
				BufferedReader br = new BufferedReader(is); 
				String s = br.readLine(); 
				StringBuffer sb = new StringBuffer(); 
				while (s != null) {// 执行循环将字符串全部取出付值给 StringBuffer由StringBuffer转成STRING 
				    sb.append(s); 
				    s = br.readLine(); 
				} 
				reString = sb.toString(); */
				cfg.setConfigContent(reString);
				cfg.setId(map.get(0).get("ID").toString());
				cfg.setVersion(map.get(0).get("VERSION").toString());
			}catch(Exception e){
				
			}
		}
		return reString;
	}
	
	
	
	@RequestMapping(value = "/getConfig", method = RequestMethod.POST)
	@ResponseBody
	public GlobalConfig getConfig(Model model) {
		ResultMessage msg = new ResultMessage();
		List<Map> map = gloabalConfigService.getConfiguration();
		GlobalConfig cfg = new GlobalConfig();
		String reString = ""; 
		if(map!=null&&map.size()>0){
			reString =(String)map.get(0).get("CONTENT_CONFIG");
			try{
				/*Reader is = clob.getCharacterStream();// 得到流 
				BufferedReader br = new BufferedReader(is); 
				String s = br.readLine(); 
				StringBuffer sb = new StringBuffer(); 
				while (s != null) {// 执行循环将字符串全部取出付值给 StringBuffer由StringBuffer转成STRING 
				    sb.append(s); 
				    s = br.readLine(); 
				} 
				reString = sb.toString(); */
				cfg.setConfigContent(reString);
				cfg.setId(map.get(0).get("ID").toString());
				cfg.setVersion(map.get(0).get("VERSION").toString());
			}catch(Exception e){
				
			}
		}
		return cfg;
	}
	@RequestMapping(value = "/saveConfig", method = RequestMethod.POST)
	@ResponseBody
	public ResultMessage saveConfig(Model model,HttpServletRequest request,GlobalConfig cfg){
		 ResultMessage msg = new ResultMessage();
		 try{
			 gloabalConfigService.saveOrUpdateConfiguration(cfg);
			 msg.setMsg("保存成功！");
			 msg.setSuccess("OK");
		 }catch(Exception e){
			 msg.setMsg("保存失败！");
			 msg.setSuccess("ERROR");
		 }
		return msg;
	}
}
