package com.supermap.realestate.registration.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.model.RT_BASEBOARDCHECK;
import com.supermap.realestate.registration.model.RT_BASECONSTRAINT;
import com.supermap.realestate.registration.model.RT_BOARDCHECK;
import com.supermap.realestate.registration.model.RT_BOARDCHECKEXP;
import com.supermap.realestate.registration.model.RT_CONSTRAINT;
import com.supermap.realestate.registration.model.RT_CONSTRAINTEXP;
import com.supermap.realestate.registration.model.RT_ROUTECONDITION;
import com.supermap.realestate.registration.model.T_BASEWORKFLOW;
import com.supermap.realestate.registration.model.T_CHECKRULE;
import com.supermap.realestate.registration.model.T_CONFIG;
import com.supermap.realestate.registration.model.T_CONSTRAINT;
import com.supermap.realestate.registration.model.T_HANDLER;
import com.supermap.realestate.registration.model.T_SELECTOR;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CHECKLEVEL;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;

/**
 * 登簿检查Controller，流程相关的登簿检查放在这里边
 * @ClassName: ConfigController
 * @author 俞学斌
 * @date 2015年11月07日 16:52:28
 */
@Controller
@RequestMapping("/checkmanager")
public class CheckManagerController {

	@Autowired
	private CommonDao basecommondao;

	private final String prefix = "/realestate/registration/";

	/*****************************************检查规则管理*****************************************/
	
	/**
	 * 登簿检查规则管理页面(URL:"/rulemanager/index/",Method：POST)
	 * @Title: getRuleManagerIndex
	 * @author:俞学斌
	 * @date：2015年11月07日 16:52:28
	 * @return
	 */
	@RequestMapping(value = "/rulemanager/index/")
	public String getRuleManagerIndex() {
		return prefix + "checkrulemanage/rulemanager";
	}
	
	/**
	 * 新增或保存检查规则 (URL:"/checkrule/",Method：POST)
	 * @Title: AddOrUpdateCheckRule
	 * @author:俞学斌
	 * @date：2015年11月07日 18:51:31
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/checkrule/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddOrUpdateCheckRule(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String classname = request.getParameter("classname");
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		String excutetype = request.getParameter("executetype");
		String excuteclassname = request.getParameter("executeclassname");
		String executesql = request.getParameter("executesql");
		String sqlresultexp = request.getParameter("sqlresultexp");
		String resulttip = request.getParameter("resulttip");
		String userdefine = request.getParameter("userdefine");
		T_CHECKRULE rule = null;
		boolean bnew = true;
		if (!StringHelper.isEmpty(id)) {
			rule = basecommondao.get(T_CHECKRULE.class, id);
			bnew = false;
		}
		if (rule == null) {
			rule = new T_CHECKRULE();
			rule.setId((String) SuperHelper.GeneratePrimaryKey());
			rule.setCREATOR(Global.getCurrentUserInfo().getLoginName());
			rule.setCREATETIME(new Date());
		}else{
			rule.setLASTMODIFIER(Global.getCurrentUserInfo().getLoginName());
			rule.setMODIFYTIME(new Date());
		}
		rule.setCLASSNAME(classname);
		rule.setDESCRIPTION(description);
		rule.setEXECUTECLASSNAME(excuteclassname);
		rule.setEXECUTESQL(executesql);
		rule.setEXECUTETYPE(excutetype);
		rule.setNAME(name);
		rule.setRESULTTIP(resulttip);
		rule.setSQLRESULTEXP(sqlresultexp);
		rule.setUSERDEFINE(userdefine);
		if (bnew)
			basecommondao.save(rule);
		else
			basecommondao.update(rule);
		basecommondao.flush();
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("true");
		msg.setMsg("保存成功");
		return msg;
	}
	
	/**
	 * 删除检查规则 (URL:"/checkrule/{id}",Method：DELETE)
	 * @Title: RemoveCheckRule
	 * @author:俞学斌
	 * @date：2015年11月07日 20:27:31
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/checkrule/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage RemoveCheckRule(@PathVariable("id") String id,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("删除失败");
		T_CHECKRULE rule = basecommondao.get(T_CHECKRULE.class, id);
		if(rule!=null){
			
			if(SF.YES.Value.equals(rule.getUSERDEFINE())){
				basecommondao.deleteEntity(rule);
				basecommondao.flush();
				msg.setSuccess("true");
				msg.setMsg("删除成功");
			}else{
				msg.setSuccess("false");
				msg.setMsg("只能删除用户自定义规则！");
			}
		}else{
			msg.setSuccess("false");
			msg.setMsg("未查询到要删除规则！");
		}
		return msg;
	}
	
	/**
	 * 分页获取检查规则(URL:"/checkrule/",Method：GET)
	 * @Title: LoadCheckRule
	 * @author:俞学斌
	 * @date：2015年11月07日 19:10:50
	 * @return
	 */
	@RequestMapping(value = "/checkrule/", method = RequestMethod.GET)
	public @ResponseBody Message LoadCheckRule(HttpServletRequest request, HttpServletResponse response) {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String filter="";
		try {
			filter = RequestHelper.getParam(request, "filter");
		} catch (Exception e) {
		}
		
		
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 ");
		if (!StringHelper.isEmpty(filter)) {
			hqlBuilder.append(" and (CLASSNAME LIKE '%").append(filter).append("%'");
			hqlBuilder.append(" or NAME LIKE '%").append(filter).append("%'");
			hqlBuilder.append(" or DESCRIPTION LIKE '%").append(filter).append("%')");
		}
		hqlBuilder.append(" ORDER BY CLASSNAME,NAME ");
		Page p = basecommondao.getPageDataByHql(T_CHECKRULE.class, hqlBuilder.toString(), page, rows);
		Message m=new Message();
		m.setSuccess("true");
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		m.setMsg("成功！");
		return m;
	}
	
	/*****************************************检查规则管理*****************************************/
	
	
	/*****************************************受理约束管理*****************************************/
	
	/**
	 * 登簿检查规则管理页面(URL:"/constraintmanager/index/",Method：POST)
	 * @Title: getConstraintManagerIndex
	 * @author:俞学斌
	 * @date：2015年11月07日 16:52:28
	 * @return
	 */
	@RequestMapping(value = "/constraintmanager/index/")
	public String getConstraintManagerIndex() {
		return prefix + "checkrulemanage/constraintmanager";
	}
	
	/**
	 * 新增或保存检查规则 (URL:"/constraint/",Method：POST)
	 * @Title: AddOrUpdateConstraint
	 * @author:俞学斌
	 * @date：2015年11月07日 18:51:31
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/constraint/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddOrUpdateConstraint(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String classname = request.getParameter("classname");
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		String excutetype = request.getParameter("executetype");
		String excuteclassname = request.getParameter("executeclassname");
		String executesql = request.getParameter("executesql");
		String sqlresultexp = request.getParameter("sqlresultexp");
		String resulttip = request.getParameter("resulttip");
		String userdefine = request.getParameter("userdefine");
		T_CONSTRAINT rule = null;
		boolean bnew = true;
		if (!StringHelper.isEmpty(id)) {
			rule = basecommondao.get(T_CONSTRAINT.class, id);
			bnew = false;
		}
		if (rule == null) {
			rule = new T_CONSTRAINT();
			rule.setId((String) SuperHelper.GeneratePrimaryKey());
			rule.setCREATOR(Global.getCurrentUserInfo().getLoginName());
			rule.setCREATETIME(new Date());
		}else{
			rule.setLASTMODIFIER(Global.getCurrentUserInfo().getLoginName());
			rule.setMODIFYTIME(new Date());
		}
		rule.setCLASSNAME(classname);
		rule.setDESCRIPTION(description);
		rule.setEXECUTECLASSNAME(excuteclassname);
		rule.setEXECUTESQL(executesql);
		rule.setEXECUTETYPE(excutetype);
		rule.setNAME(name);
		rule.setRESULTTIP(resulttip);
		rule.setSQLRESULTEXP(sqlresultexp);
		rule.setUSERDEFINE(userdefine);
		if (bnew)
			basecommondao.save(rule);
		else
			basecommondao.update(rule);
		basecommondao.flush();
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("true");
		msg.setMsg("保存成功");
		return msg;
	}
	
	/**
	 * 删除检查规则 (URL:"/constraint/{id}",Method：DELETE)
	 * @Title: RemoveCheckRule
	 * @author:俞学斌
	 * @date：2015年11月07日 20:27:31
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/constraint/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage RemoveConstraint(@PathVariable("id") String id,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("删除失败");
		T_CONSTRAINT rule = basecommondao.get(T_CONSTRAINT.class, id);
		if(rule!=null){
			
			if(SF.YES.Value.equals(rule.getUSERDEFINE())){
				basecommondao.deleteEntity(rule);
				basecommondao.flush();
				msg.setSuccess("true");
				msg.setMsg("删除成功");
			}else{
				msg.setSuccess("false");
				msg.setMsg("只能删除用户自定义约束！");
			}
		}else{
			msg.setSuccess("false");
			msg.setMsg("未查询到要删除约束！");
		}
		return msg;
	}
	
	/**
	 * 分页获取检查规则(URL:"/constraint/",Method：GET)
	 * @Title: LoadCheckRule
	 * @author:俞学斌
	 * @date：2015年11月07日 19:10:50
	 * @return
	 */
	@RequestMapping(value = "/constraint/", method = RequestMethod.GET)
	public @ResponseBody Message LoadConstraint(HttpServletRequest request, HttpServletResponse response) {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String filter="";
		try {
			filter = RequestHelper.getParam(request, "filter");
		} catch (Exception e) {
		}
		
		
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 ");
		if (!StringHelper.isEmpty(filter)) {
			hqlBuilder.append(" and (CLASSNAME LIKE '%").append(filter).append("%'");
			hqlBuilder.append(" or NAME LIKE '%").append(filter).append("%'");
			hqlBuilder.append(" or DESCRIPTION LIKE '%").append(filter).append("%')");
		}
		Page p = basecommondao.getPageDataByHql(T_CONSTRAINT.class, hqlBuilder.toString(), page, rows);
		Message m=new Message();
		m.setSuccess("true");
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		m.setMsg("成功！");
		return m;
	}
	
	/*****************************************受理约束管理*****************************************/
	
	
	
	/*****************************************基准流程配置*****************************************/
	
	/**
	 * 基础流程配置页面(URL:"/baseboardcheck/index/",Method：GET)
	 * @Title: getBaseBoardcheckIndex
	 * @author:俞学斌
	 * @date：2015年11月10日 11:10:28
	 * @return
	 */
	@RequestMapping(value = "/baseboardcheck/index/")
	public String getBaseBoardcheckIndex() {
		return prefix + "checkrulemanage/baseworkflowconfig";
	}
	
	/**
	 * 获取基准流程树(URL:"/loadbaseworkflows/",Method：GET)
	 * @Title: LoadBaseWorkflows
	 * @author:俞学斌
	 * @date：2015年11月10日 11:15:50
	 * @return
	 */
	@RequestMapping(value = "/loadbaseworkflows/", method = RequestMethod.GET)
	public @ResponseBody List<BaseWorkFlowClass> LoadBaseWorkflows(HttpServletRequest request, HttpServletResponse response) {
		List<BaseWorkFlowClass> tree=new ArrayList<CheckManagerController.BaseWorkFlowClass>();
		List<T_BASEWORKFLOW> baseworkflows=basecommondao.getDataList(T_BASEWORKFLOW.class, "1>0 ORDER BY DJLX,ID");
		if(baseworkflows==null||baseworkflows.size()<=0){
			return tree;
		}
		int i=1;
		for(T_BASEWORKFLOW baseworkflow:baseworkflows){
			if(StringHelper.isEmpty(baseworkflow.getCLASS())){
				continue;
			}
			boolean bExist=false;
			for(BaseWorkFlowClass classes:tree){
				if(classes.getText().equals(baseworkflow.getCLASS())){
					BASEWORKFLOW flow=new BASEWORKFLOW();
					flow.setId(baseworkflow.getId());
					flow.setText(baseworkflow.getId()+"-"+baseworkflow.getNAME());
					classes.children.add(flow);
					bExist=true;
					break;
				}
			}
			if(!bExist){
				BaseWorkFlowClass classes=new BaseWorkFlowClass();
				BASEWORKFLOW flow=new BASEWORKFLOW();
				flow.setId(baseworkflow.getId());
				flow.setText(baseworkflow.getId()+"-"+baseworkflow.getNAME());
				classes.children.add(flow);
				classes.setId(StringHelper.formatDouble(i));
				classes.setText(baseworkflow.getCLASS());
				i++;
				tree.add(classes);
			}
		}
		return tree;
	}
	
	/**
	 * 获取基准流程信息(URL:"/loadbaseworkflowinfo/",Method：GET)
	 * @Title: LoadBaseWorkflowInfo
	 * @author:俞学斌
	 * @date：2015年11月10日 11:15:50
	 * @return
	 */
	@RequestMapping(value = "/loadbaseworkflowinfo/", method = RequestMethod.GET)
	public @ResponseBody HashMap<String,Object> LoadBaseWorkflowInfo(HttpServletRequest request, HttpServletResponse response) {
		String id="";
		try {
			id = RequestHelper.getParam(request, "id");
		} catch (Exception e) {
		}
		HashMap<String,Object> m=new HashMap<String, Object>();
		if(StringHelper.isEmpty(id)){
			return m;
		}
		T_BASEWORKFLOW baseworkflow=basecommondao.get(T_BASEWORKFLOW.class, id);
		if(baseworkflow!=null){
			m.put("ID", baseworkflow.getId());
			m.put("CLASS", baseworkflow.getCLASS());
			m.put("NAME", baseworkflow.getNAME());
			m.put("DESCRIPTION", baseworkflow.getDESCRIPTION());
			String selectorid=baseworkflow.getSELECTORID();
			m.put("SELECTORID", selectorid);
			String selectorname="";
			if(!StringHelper.isEmpty(selectorid)){
				T_SELECTOR selector=basecommondao.get(T_SELECTOR.class, selectorid);
				if(selector!=null){
					selectorname=selector.getNAME();
				}
			}
			m.put("SELECTORNAME", selectorid+"-"+selectorname);
			String handlerid=baseworkflow.getHANDLERID();
			m.put("HANDLERID", handlerid);
			String handlername="";
			if(!StringHelper.isEmpty(handlerid)){
				T_HANDLER handler=basecommondao.get(T_HANDLER.class, handlerid);
				if(handler!=null){
					handlername=handler.getNAME();
				}
			}
			m.put("HANDLERNAME", handlerid+"-"+handlername);
			String djlx=baseworkflow.getDJLX();
			String djlxname="";
			if(DJLX.initFrom(baseworkflow.getDJLX())!=null){
				djlxname=DJLX.initFrom(baseworkflow.getDJLX()).Name;
			}
			m.put("DJLX", djlx+"-"+djlxname);
			String qllx=baseworkflow.getQLLX();
			String qllxname="";
			if(QLLX.initFrom(baseworkflow.getQLLX())!=null){
				qllxname=QLLX.initFrom(baseworkflow.getQLLX()).Name;
			}
			m.put("QLLX", qllx+"-"+qllxname);
			String bdcdylx=baseworkflow.getUNITTYPE();
			String bdcdylxname="";
			if(BDCDYLX.initFrom(baseworkflow.getUNITTYPE())!=null){
				bdcdylxname=BDCDYLX.initFrom(baseworkflow.getUNITTYPE()).Name;
			}
			m.put("BDCDYLX", bdcdylx+"-"+bdcdylxname);
			m.put("UNITPAGEJSP", baseworkflow.getUNITPAGEJSP());
			m.put("RIGHTSPAGEJSP", baseworkflow.getRIGHTSPAGEJSP());
			m.put("BOOKPAGEJSP", baseworkflow.getBOOKPAGEJSP());
		}
		return m;
	}
	
	/************************基准流程登簿检查配置************************/
	
	/**
	 * 分页获取基准流程登簿检查规则(URL:"/baseboardrule/",Method：GET)
	 * @Title: LoadBaseBoardRule
	 * @author:俞学斌
	 * @date：2015年11月10日 14:36:50
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/baseboardrule/", method = RequestMethod.GET)
	public @ResponseBody Message LoadBaseBoardRule(HttpServletRequest request, HttpServletResponse response) {
		Message m=new Message();
		m.setSuccess("false");
		m.setTotal(0);
		m.setMsg("失败！");
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String baseworkflowid="";
		try {
			baseworkflowid = RequestHelper.getParam(request, "baseworkflowid");
		} catch (Exception e) {
		}
		
		if(StringHelper.isEmpty(baseworkflowid)){
			return m;
		}
		
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0");
		if (!StringHelper.isEmpty(baseworkflowid)) {
			hqlBuilder.append(" AND BASEWORKFLOWID='");
			hqlBuilder.append(baseworkflowid);
			hqlBuilder.append("'");
		}
		String fullSql = "SELECT CONFIG.ID,CONFIG.CHECKRULEID,RULE.CLASSNAME,RULE.NAME,RULE.DESCRIPTION,CONFIG.CHECKLEVEL FROM BDCK.RT_BASEBOARDCHECK CONFIG LEFT JOIN BDCK.T_CHECKRULE RULE ON CONFIG.CHECKRULEID=RULE.ID WHERE RULE.ID IS NOT NULL AND CONFIG.BASEWORKFLOWID='"+baseworkflowid+"' ORDER BY RULE.ID, CONFIG.CHECKRULEID";
		List<Map> listmap = basecommondao.getPageDataByFullSql(fullSql, page, rows);
		long count = basecommondao.getCountByFullSql("from BDCK.RT_BASEBOARDCHECK WHERE BASEWORKFLOWID='"+baseworkflowid+"'");
		m.setTotal(count);
		m.setRows(listmap);
		m.setSuccess("true");
		m.setMsg("成功！");
		return m;
	}
	
	/**
	 * 添加基准流程登簿检查规则 (URL:"/baseboardrule/",Method：POST)
	 * @Title: AddBaseBoardRule
	 * @author:俞学斌
	 * @date：2015年11月09日 20:40:31
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/baseboardrule/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddBaseBoardRule(HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("添加失败");
		String ids="";
		if (request.getParameter("ids") != null) {
			ids = request.getParameter("ids");
		}
		String baseworkflowid="";
		if (request.getParameter("baseworkflowid") != null) {
			baseworkflowid = request.getParameter("baseworkflowid");
		}
		if(StringHelper.isEmpty(baseworkflowid)){
			return msg;
		}
		if(StringHelper.isEmpty(ids)){
			return msg;
		}
		String[] checkruleids=ids.split(",");
		if(checkruleids==null||checkruleids.length<=0){
			return msg;
		}
		for(String checkruleid:checkruleids){
			RT_BASEBOARDCHECK boardcheck=new RT_BASEBOARDCHECK();
			String _id = SuperHelper.GeneratePrimaryKey();
			boardcheck.setId(_id);
			boardcheck.setCHECKRULEID(checkruleid);
			boardcheck.setCHECKLEVEL(CHECKLEVEL.ERROR.Value);
			boardcheck.setCREATETIME(new Date());
			boardcheck.setCREATOR(Global.getCurrentUserInfo().getLoginName());
			boardcheck.setBASEWORKFLOWID(baseworkflowid);
			basecommondao.save(boardcheck);
		}
		basecommondao.flush();
		msg.setSuccess("true");
		msg.setMsg("添加成功！");
		return msg;
	}
	
	/**
	 * 移除基准流程登簿检查规则(URL:"/baseboardrule/{id}/",Method：DELETE)
	 * @Title: RemoveBaseBoardRule
	 * @author:俞学斌
	 * @date：2015年11月10日 14:36:50
	 * @return
	 */
	@RequestMapping(value = "/baseboardrule/{id}/", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage RemoveBaseBoardRule(@PathVariable("id") String id,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage ms=new ResultMessage();
		ms.setMsg("删除基准流程登簿检查规则失败！");
		ms.setSuccess("false");
		if(StringHelper.isEmpty(id)){
			return ms;
		}
		RT_BASEBOARDCHECK baseboradcheck=basecommondao.get(RT_BASEBOARDCHECK.class, id);
		if(baseboradcheck==null){
			return ms;
		}
		basecommondao.deleteEntity(baseboradcheck);
		basecommondao.flush();
		ms.setMsg("删除基准流程登簿检查规则成功！");
		ms.setSuccess("true");
		return ms;
	}
	
	/**
	 * 更换基准流程登簿检查规则检查级别 (URL:"/basechecklevel/{id}/",Method：POST)
	 * @Title: BaseCheckLevel
	 * @author:俞学斌
	 * @date：2015年11月07日 20:27:31
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/basechecklevel/{id}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage BaseCheckLevel(@PathVariable("id") String id,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("更改失败");
		RT_BASEBOARDCHECK rule=basecommondao.get(RT_BASEBOARDCHECK.class, id);
		if(rule!=null){
			if(CHECKLEVEL.WARNING.Value.equals(rule.getCHECKLEVEL())){
				rule.setCHECKLEVEL(CHECKLEVEL.ERROR.Value);
			}else{
				rule.setCHECKLEVEL(CHECKLEVEL.WARNING.Value);
			}
			basecommondao.update(rule);
			basecommondao.flush();
			msg.setSuccess("true");
			msg.setMsg("更改成功");
		}else{
			msg.setSuccess("false");
			msg.setMsg("未查询到要更改规则！");
		}
		return msg;
	}
	
	/**
	 * 分页获取检查规则数据(不还包含当前基准流程登簿检查规则)(URL:"/querycheckrule",Method：GET)
	 * @Title: QueryCheckRule
	 * @author:俞学斌
	 * @date：2015年11月07日 19:10:50
	 * @return
	 */
	@RequestMapping(value = "/querycheckrule/", method = RequestMethod.GET)
	public @ResponseBody Message QueryCheckRule(HttpServletRequest request, HttpServletResponse response) {

		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String filter="";
		try {
			filter = RequestHelper.getParam(request, "filter");
		} catch (Exception e) {
		}
		
		String baseworkflowid="";
		try {
			baseworkflowid = RequestHelper.getParam(request, "baseworkflowid");
		} catch (Exception e) {
		}
		
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 ");
		if (!StringHelper.isEmpty(filter)) {
			hqlBuilder.append(" AND (CLASSNAME LIKE '%").append(filter).append("%'");
			hqlBuilder.append(" OR NAME LIKE '%").append(filter).append("%'");
			hqlBuilder.append(" OR DESCRIPTION LIKE '%").append(filter).append("%')");
		}
		if (!StringHelper.isEmpty(baseworkflowid)) {
			hqlBuilder.append(" AND ID NOT IN (SELECT CHECKRULEID FROM RT_BASEBOARDCHECK WHERE BASEWORKFLOWID='");
			hqlBuilder.append(baseworkflowid);
			hqlBuilder.append("')");
		}
		Page p = basecommondao.getPageDataByHql(T_CHECKRULE.class, hqlBuilder.toString(), page, rows);
		Message m=new Message();
		m.setSuccess("true");
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		m.setMsg("成功！");
		return m;
	}
	
	/************************基准流程登簿检查配置************************/
	
	/************************基准流程受理约束配置************************/
	
	/**
	 * 分页获取基准流程受理约束(URL:"/baseconstraint/{constrainttype}/",Method：GET)
	 * @Title: LoadBaseConstraint
	 * @author:俞学斌
	 * @date：2015年11月10日 14:36:50
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/baseconstraint/{constrainttype}/", method = RequestMethod.GET)
	public @ResponseBody Message LoadBaseConstraint(@PathVariable("constrainttype") String constrainttype,HttpServletRequest request, HttpServletResponse response) {
		Message m=new Message();
		m.setSuccess("false");
		m.setTotal(0);
		m.setMsg("失败！");
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String baseworkflowid="";
		try {
			baseworkflowid = RequestHelper.getParam(request, "baseworkflowid");
		} catch (Exception e) {
		}
		
		if(StringHelper.isEmpty(baseworkflowid)){
			return m;
		}
		
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0");
		if (!StringHelper.isEmpty(baseworkflowid)) {
			hqlBuilder.append(" AND BASEWORKFLOWID='");
			hqlBuilder.append(baseworkflowid);
			hqlBuilder.append("'");
		}
		String fullSql = "SELECT CONFIG.ID,CONFIG.CONSTRAINTID,RULE.CLASSNAME,RULE.NAME,RULE.DESCRIPTION,CONFIG.CHECKLEVEL,CONFIG.RESULTTIP FROM BDCK.RT_BASECONSTRAINT CONFIG LEFT JOIN BDCK.T_CONSTRAINT RULE ON CONFIG.CONSTRAINTID=RULE.ID WHERE CONFIG.CONSTRAINTTYPE='"+constrainttype+"' AND RULE.ID IS NOT NULL AND CONFIG.BASEWORKFLOWID='"+baseworkflowid+"' ORDER BY CONFIG.CONSTRAINTID";
		List<Map> listmap = basecommondao.getPageDataByFullSql(fullSql, page, rows);
		long count = basecommondao.getCountByFullSql("from BDCK.RT_BASECONSTRAINT WHERE BASEWORKFLOWID='"+baseworkflowid+"'");
		m.setTotal(count);
		m.setRows(listmap);
		m.setSuccess("true");
		m.setMsg("成功！");
		return m;
	}
	
	/**
	 * 添加基准流程受理约束 (URL:"/baseconstraint/{constrainttype}/",Method：POST)
	 * @Title: AddBaseConstraint
	 * @author:俞学斌
	 * @date：2015年11月09日 20:40:31
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/baseconstraint/{constrainttype}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddBaseConstraint(@PathVariable("constrainttype") String constrainttype,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("添加失败");
		String ids="";
		if (request.getParameter("ids") != null) {
			ids = request.getParameter("ids");
		}
		String baseworkflowid="";
		if (request.getParameter("baseworkflowid") != null) {
			baseworkflowid = request.getParameter("baseworkflowid");
		}
		if(StringHelper.isEmpty(baseworkflowid)){
			return msg;
		}
		if(StringHelper.isEmpty(ids)){
			return msg;
		}
		String[] checkruleids=ids.split(",");
		if(checkruleids==null||checkruleids.length<=0){
			return msg;
		}
		for(String checkruleid:checkruleids){
			T_CONSTRAINT constraint=basecommondao.get(T_CONSTRAINT.class, checkruleid);
			if(constraint==null){
				continue;
			}
			RT_BASECONSTRAINT boardcheck=new RT_BASECONSTRAINT();
			String _id = SuperHelper.GeneratePrimaryKey();
			boardcheck.setId(_id);
			boardcheck.setCONSTRAINTID(checkruleid);
			boardcheck.setCHECKLEVEL(CHECKLEVEL.ERROR.Value);
			boardcheck.setCONSTRAINTTYPE(constrainttype);
			boardcheck.setRESULTTIP(constraint.getRESULTTIP());
			boardcheck.setCREATETIME(new Date());
			boardcheck.setCREATOR(Global.getCurrentUserInfo().getLoginName());
			boardcheck.setBASEWORKFLOWID(baseworkflowid);
			basecommondao.save(boardcheck);
		}
		basecommondao.flush();
		msg.setSuccess("true");
		msg.setMsg("添加成功！");
		return msg;
	}
	
	/**
	 * 移除基准流程受理约束(URL:"/baseconstraint/{id}",Method：DELETE)
	 * @Title: RemoveBaseConstraint
	 * @author:俞学斌
	 * @date：2015年11月10日 14:36:50
	 * @return
	 */
	@RequestMapping(value = "/baseconstraint/{id}/", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage RemoveBaseConstraint(@PathVariable("id") String id,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage ms=new ResultMessage();
		ms.setMsg("删除基准流程受理约束失败！");
		ms.setSuccess("false");
		if(StringHelper.isEmpty(id)){
			return ms;
		}
		RT_BASECONSTRAINT baseboradcheck=basecommondao.get(RT_BASECONSTRAINT.class, id);
		if(baseboradcheck==null){
			return ms;
		}
		basecommondao.deleteEntity(baseboradcheck);
		basecommondao.flush();
		ms.setMsg("删除基准流程受理约束成功！");
		ms.setSuccess("true");
		return ms;
	}
	
	/**
	 * 更换基准流程受理约束检查级别 (URL:"/baseconstraintlevel/{id}/",Method：POST)
	 * @Title: BaseConstraintLevel
	 * @author:俞学斌
	 * @date：2015年11月07日 20:27:31
	 * @param request
	 * @param response
	 * @return
	 */
	@Transactional
	@RequestMapping(value = "/baseconstraintlevel/{id}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage BaseConstraintLevel(@PathVariable("id") String id,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("更改失败");
		RT_BASECONSTRAINT rule=basecommondao.get(RT_BASECONSTRAINT.class, id);
		if(rule!=null){
			if(CHECKLEVEL.WARNING.Value.equals(rule.getCHECKLEVEL())){
				rule.setCHECKLEVEL(CHECKLEVEL.ERROR.Value);
			}else{
				rule.setCHECKLEVEL(CHECKLEVEL.WARNING.Value);
			}
			basecommondao.update(rule);
			basecommondao.flush();
			msg.setSuccess("true");
			msg.setMsg("更改成功");
		}else{
			msg.setSuccess("false");
			msg.setMsg("未查询到要更改约束！");
		}
		return msg;
	}
	
	/**
	 * 修改基准流程受理约束结果提示信息 (URL:"/editbaseconstraint/",Method：POST)
	 * @Title: EditBaseConstraint
	 * @author:俞学斌
	 * @date：2015年11月07日 20:27:31
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/editbaseconstraint/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage EditBaseConstraint(HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("更改失败");
		String ID="";
		try {
			ID = request.getParameter("ID");
		} catch (Exception e) {
		}
		String RESULTTIP="";
		try {
			RESULTTIP=request.getParameter("RESULTTIP");
		} catch (Exception e) {
		}
		RT_BASECONSTRAINT rule=basecommondao.get(RT_BASECONSTRAINT.class, ID);
		if(rule!=null){
			rule.setRESULTTIP(RESULTTIP);
			basecommondao.update(rule);
			basecommondao.flush();
			msg.setSuccess("true");
			msg.setMsg("更改成功");
		}else{
			msg.setSuccess("false");
			msg.setMsg("未查询到要更改约束！");
		}
		return msg;
	}
	
	/**
	 * 分页获取受理约束数据(不还包含当前基准流程受理约束)(URL:"/queryconstraint/",Method：GET)
	 * @Title: QueryConstraint
	 * @author:俞学斌
	 * @date：2015年11月07日 19:10:50
	 * @return
	 */
	@RequestMapping(value = "/queryconstraint/", method = RequestMethod.GET)
	public @ResponseBody Message QueryConstraint(HttpServletRequest request, HttpServletResponse response) {

		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String filter="";
		try {
			filter = RequestHelper.getParam(request, "filter");
		} catch (Exception e) {
		}
		
		String baseworkflowid="";
		try {
			baseworkflowid = RequestHelper.getParam(request, "baseworkflowid");
		} catch (Exception e) {
		}
		
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 ");
		if (!StringHelper.isEmpty(filter)) {
			hqlBuilder.append(" AND (CLASSNAME LIKE '%").append(filter).append("%'");
			hqlBuilder.append(" OR NAME LIKE '%").append(filter).append("%'");
			hqlBuilder.append(" OR DESCRIPTION LIKE '%").append(filter).append("%')");
		}
		if (!StringHelper.isEmpty(baseworkflowid)) {
			hqlBuilder.append(" AND ID NOT IN (SELECT CONSTRAINTID FROM RT_BASECONSTRAINT WHERE BASEWORKFLOWID='");
			hqlBuilder.append(baseworkflowid);
			hqlBuilder.append("')");
		}
		Page p = basecommondao.getPageDataByHql(T_CONSTRAINT.class, hqlBuilder.toString(), page, rows);
		Message m=new Message();
		m.setSuccess("true");
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		m.setMsg("成功！");
		return m;
	}
	
	/************************基准流程受理约束配置************************/
	
	/*****************************************基准流程配置*****************************************/
	
	
	
	/*****************************************业务流程配置*****************************************/
	
	/************************业务流程登簿检查配置************************/
	
	/**
	 * 分页获取基准流程登簿检查规则(URL:"/{proid}/baseboardrule/",Method：GET)
	 * @Title: LoadBaseBoardRule
	 * @author:俞学斌
	 * @date：2015年11月08日 15:47:50
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/{proid}/baseboardrule/", method = RequestMethod.GET)
	public @ResponseBody Message LoadBaseBoardRule(@PathVariable("proid") String proid,HttpServletRequest request, HttpServletResponse response) {
		Message m=new Message();
		m.setSuccess("true");
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Wfd_Prodef processData = basecommondao.get(Wfd_Prodef.class, proid);
		if(processData!=null&&!StringHelper.isEmpty(processData.getProdef_Code())){
			List<WFD_MAPPING> listMap=basecommondao.getDataList(WFD_MAPPING.class, "workflowcode='"+processData.getProdef_Code()+"'");
			if(listMap!=null&&listMap.size()>0&&!StringHelper.isEmpty(listMap.get(0).getWORKFLOWNAME())){
				String fullSql = "SELECT CONFIG.ID,CONFIG.CHECKRULEID,RULE.CLASSNAME,RULE.NAME,RULE.DESCRIPTION,CONFIG.CHECKLEVEL FROM BDCK.RT_BASEBOARDCHECK CONFIG LEFT JOIN BDCK.T_CHECKRULE RULE ON CONFIG.CHECKRULEID=RULE.ID WHERE RULE.ID IS NOT NULL AND CONFIG.BASEWORKFLOWID='"+listMap.get(0).getWORKFLOWNAME()+"' ORDER BY CONFIG.CHECKRULEID";
				List<Map> listmap = basecommondao.getPageDataByFullSql(fullSql, page, rows);
				if (listmap != null && listmap.size() > 0) {
					for (Map mp : listmap) {
						String ruleid=StringHelper.formatObject(mp.get("CHECKRULEID"));
						List<RT_BOARDCHECKEXP> listExp=basecommondao.getDataList(RT_BOARDCHECKEXP.class, "CHECKRULEID='"+ruleid+"' AND WORKFLOWCODE='"+processData.getProdef_Code()+"'");
						if(listExp!=null&&listExp.size()>0){
							mp.put("ISEXP", "是");
						}else{
							mp.put("ISEXP", "否");
						}
						mp.put("WORKFLOWCODE", processData.getProdef_Code());
					}
				}
				long count = basecommondao.getCountByFullSql("from BDCK.RT_BASEBOARDCHECK WHERE BASEWORKFLOWID='"+listMap.get(0).getWORKFLOWNAME()+"'");
				m.setTotal(count);
				m.setRows(listmap);
			}
		}
		m.setMsg("成功！");
		return m;
	}
	
	/**
	 * 分页自定义登簿检查规则(URL:"/{proid}/defineboardrule/",Method：GET)
	 * @Title: LoadDefineBoardRule
	 * @author:俞学斌
	 * @date：2015年11月08日 15:47:50
	 * @return
	 */
	@SuppressWarnings({ "rawtypes"})
	@RequestMapping(value = "/{proid}/defineboardrule/", method = RequestMethod.GET)
	public @ResponseBody Message LoadDefineBoardRule(@PathVariable("proid") String proid,HttpServletRequest request, HttpServletResponse response) {
		Message m=new Message();
		m.setSuccess("true");
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Wfd_Prodef processData = basecommondao.get(Wfd_Prodef.class, proid);
		if(processData!=null&&!StringHelper.isEmpty(processData.getProdef_Code())){
			String fullSql = "SELECT CONFIG.ID,RULE.CLASSNAME,RULE.NAME,RULE.DESCRIPTION,CONFIG.CHECKLEVEL FROM BDCK.RT_BOARDCHECK CONFIG LEFT JOIN BDCK.T_CHECKRULE RULE ON CONFIG.CHECKRULEID=RULE.ID WHERE RULE.ID IS NOT NULL AND CONFIG.WORKFLOWCODE='"+processData.getProdef_Code()+"' ORDER BY CONFIG.CHECKRULEID";
			List<Map> listmap = basecommondao.getPageDataByFullSql(fullSql, page, rows);
			long count = basecommondao.getCountByFullSql("from BDCK.RT_BOARDCHECK WHERE WORKFLOWCODE='"+processData.getProdef_Code()+"'");
			m.setTotal(count);
			m.setRows(listmap);
		}
		m.setMsg("成功！");
		return m;
	}
	
	/**
	 * 生效基础流程登簿检查规则 (URL:"/baseboardrule/{checkruleid}/{workflowcode}/",Method：POST)
	 * @Title: EffectBaseBoardRule
	 * @author:俞学斌
	 * @date：2015年11月09日 02:42:31
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/baseboardrule/{checkruleid}/{workflowcode}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage EffectBaseBoardRule(@PathVariable("checkruleid") String checkruleid,@PathVariable("workflowcode") String workflowcode,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("生效规则失败");
		List<RT_BOARDCHECKEXP> listExp = basecommondao.getDataList(RT_BOARDCHECKEXP.class, "CHECKRULEID='"+checkruleid+"' AND WORKFLOWCODE='"+workflowcode+"'");
		if(listExp!=null&&listExp.size()>0){
			basecommondao.deleteEntity(listExp.get(0));
			basecommondao.flush();
			msg.setSuccess("true");
			msg.setMsg("生效规则成功！");
		}else{
			msg.setSuccess("false");
			msg.setMsg("未查询到要生效规则！");
		}
		return msg;
	}
	
	/**
	 * 忽略基础流程登簿检查规则 (URL:"/baseboardrule/{checkruleid}/{workflowcode}/",Method.DELETE)
	 * @Title: IgnoreBaseBoardRule
	 * @author:俞学斌
	 * @date：2015年11月09日 02:42:31
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/baseboardrule/{checkruleid}/{workflowcode}/", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage IgnoreBaseBoardRule(@PathVariable("checkruleid") String checkruleid,@PathVariable("workflowcode") String workflowcode,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("忽略规则失败");
		List<RT_BOARDCHECKEXP> listExp = basecommondao.getDataList(RT_BOARDCHECKEXP.class, "CHECKRULEID='"+checkruleid+"' AND WORKFLOWCODE='"+workflowcode+"'");
		if(listExp!=null&&listExp.size()>0){
			msg.setSuccess("false");
			msg.setMsg("改规则已被忽略，不要重复操作！");
		}else{
			RT_BOARDCHECKEXP exp=new RT_BOARDCHECKEXP();
			String _id = SuperHelper.GeneratePrimaryKey();
			exp.setId(_id);
			exp.setCHECKRULEID(checkruleid);
			exp.setCREATETIME(new Date());
			exp.setCREATOR(Global.getCurrentUserInfo().getLoginName());
			exp.setWORKFLOWCODE(workflowcode);
			basecommondao.save(exp);
			basecommondao.flush();
			msg.setSuccess("true");
			msg.setMsg("忽略规则成功！");
		}
		return msg;
	}
	
	/**
	 * 移除自定义登簿检查规则 (URL:"/defineboardrule/{id}/",Method：DELETE)
	 * @Title: RemoveDefineBoardRule
	 * @author:俞学斌
	 * @date：2015年11月09日 02:42:31
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/defineboardrule/{id}/", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage RemoveDefineBoardRule(@PathVariable("id") String id,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("删除规则失败");
		RT_BOARDCHECK definerule=basecommondao.get(RT_BOARDCHECK.class, id);
		if(definerule!=null){
			basecommondao.deleteEntity(definerule);
			basecommondao.flush();
			msg.setSuccess("true");
			msg.setMsg("删除规则成功！");
		}else{
			msg.setSuccess("false");
			msg.setMsg("未查询到要删除规则！");
		}
		return msg;
	}
	
	/**
	 * 添加自定义登簿检查规则 (URL:"/defineboardrule/",Method：POST)
	 * @Title: AddDefineBoardRule
	 * @author:俞学斌
	 * @date：2015年11月09日 20:40:31
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/defineboardrule/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddDefineBoardRule(HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("添加失败");
		String ids="";
		if (request.getParameter("ids") != null) {
			ids = request.getParameter("ids");
		}
		String proid="";
		if (request.getParameter("proid") != null) {
			proid = request.getParameter("proid");
		}
		if(StringHelper.isEmpty(proid)){
			return msg;
		}
		if(StringHelper.isEmpty(ids)){
			return msg;
		}
		String workflowcode="";
		Wfd_Prodef processData = basecommondao.get(Wfd_Prodef.class, proid);
		if(processData!=null&&!StringHelper.isEmpty(processData.getProdef_Code())){
			workflowcode=processData.getProdef_Code();
		}
		if(StringHelper.isEmpty(workflowcode)){
			return msg;
		}
		String[] checkruleids=ids.split(",");
		if(checkruleids==null||checkruleids.length<=0){
			return msg;
		}
		for(String checkruleid:checkruleids){
			RT_BOARDCHECK boardcheck=new RT_BOARDCHECK();
			String _id = SuperHelper.GeneratePrimaryKey();
			boardcheck.setId(_id);
			boardcheck.setCHECKRULEID(checkruleid);
			boardcheck.setCHECKLEVEL(CHECKLEVEL.ERROR.Value);
			boardcheck.setCREATETIME(new Date());
			boardcheck.setCREATOR(Global.getCurrentUserInfo().getLoginName());
			boardcheck.setWORKFLOWCODE(workflowcode);
			basecommondao.save(boardcheck);
		}
		basecommondao.flush();
		msg.setSuccess("true");
		msg.setMsg("添加成功！");
		return msg;
	}
	
	/**
	 * 分页获取登簿检查规则数据(不包含基准流程登簿检查规则，且不包含自定义登簿检查规则)(URL:"/{proid}/querycheckrule/",Method：GET)
	 * @Title: QueryCheckRule
	 * @author:俞学斌
	 * @date：2015年11月07日 19:10:50
	 * @return
	 */
	@RequestMapping(value = "/{proid}/querycheckrule/", method = RequestMethod.GET)
	public @ResponseBody Message QueryCheckRule(@PathVariable("proid") String proid,HttpServletRequest request, HttpServletResponse response) {

		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String filter="";
		try {
			filter = RequestHelper.getParam(request, "filter");
		} catch (Exception e) {
		}
		String workflowcode="";
		String workflowname="";
		if(!StringHelper.isEmpty(proid)){
			Wfd_Prodef processData = basecommondao.get(Wfd_Prodef.class, proid);
			if(processData!=null&&!StringHelper.isEmpty(processData.getProdef_Code())){
				workflowcode=processData.getProdef_Code();
				List<WFD_MAPPING> listMap=basecommondao.getDataList(WFD_MAPPING.class, "workflowcode='"+processData.getProdef_Code()+"'");
				if(listMap!=null&&listMap.size()>0&&!StringHelper.isEmpty(listMap.get(0).getWORKFLOWNAME())){
					workflowname=listMap.get(0).getWORKFLOWNAME();
				}
			}
		}
		
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 ");
		if (!StringHelper.isEmpty(filter)) {
			hqlBuilder.append(" AND (CLASSNAME LIKE '%").append(filter).append("%'");
			hqlBuilder.append(" OR NAME LIKE '%").append(filter).append("%'");
			hqlBuilder.append(" OR DESCRIPTION LIKE '%").append(filter).append("%')");
		}
		if (!StringHelper.isEmpty(workflowcode)) {
			hqlBuilder.append(" AND id NOT IN (SELECT CHECKRULEID FROM RT_BOARDCHECK WHERE WORKFLOWCODE='");
			hqlBuilder.append(workflowcode);
			hqlBuilder.append("')");
		}
		if (!StringHelper.isEmpty(workflowname)) {
			hqlBuilder.append(" AND id NOT IN (SELECT CHECKRULEID FROM RT_BASEBOARDCHECK WHERE BASEWORKFLOWID='");
			hqlBuilder.append(workflowname);
			hqlBuilder.append("')");
		}
		Page p = basecommondao.getPageDataByHql(T_CHECKRULE.class, hqlBuilder.toString(), page, rows);
		Message m=new Message();
		m.setSuccess("true");
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		m.setMsg("成功！");
		return m;
	}
	
	/**
	 * 更换自定义登簿检查规则检查级别 (URL:"/definechecklevel/{id}/",Method：POST)
	 * @Title: DefineCheckLevel
	 * @author:俞学斌
	 * @date：2015年11月07日 20:27:31
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/definechecklevel/{id}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage DefineCheckLevel(@PathVariable("id") String id,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("更改失败");
		RT_BOARDCHECK rule=basecommondao.get(RT_BOARDCHECK.class, id);
		if(rule!=null){
			if(CHECKLEVEL.WARNING.Value.equals(rule.getCHECKLEVEL())){
				rule.setCHECKLEVEL(CHECKLEVEL.ERROR.Value);
			}else{
				rule.setCHECKLEVEL(CHECKLEVEL.WARNING.Value);
			}
			basecommondao.update(rule);
			basecommondao.flush();
			msg.setSuccess("true");
			msg.setMsg("更改成功");
		}else{
			msg.setSuccess("false");
			msg.setMsg("未查询到要更改规则！");
		}
		return msg;
	}
	
	/************************业务流程登簿检查配置************************/
	
	/************************业务流程受理约束配置************************/
	
	/**
	 * 分页获取基准流程受理约束(URL:"/{proid}/baseconstraint/{constrainttype}/",Method：GET)
	 * @Title: LoadBaseConstraint
	 * @author:俞学斌
	 * @date：2015年11月08日 15:47:50
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/{proid}/baseconstraint/{constrainttype}/", method = RequestMethod.GET)
	public @ResponseBody Message LoadBaseConstraint(@PathVariable("proid") String proid,@PathVariable("constrainttype") String constrainttype,HttpServletRequest request, HttpServletResponse response) {
		Message m=new Message();
		m.setSuccess("true");
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Wfd_Prodef processData = basecommondao.get(Wfd_Prodef.class, proid);
		if(processData!=null&&!StringHelper.isEmpty(processData.getProdef_Code())){
			List<WFD_MAPPING> listMap=basecommondao.getDataList(WFD_MAPPING.class, "workflowcode='"+processData.getProdef_Code()+"'");
			if(listMap!=null&&listMap.size()>0&&!StringHelper.isEmpty(listMap.get(0).getWORKFLOWNAME())){
				String fullSql = "SELECT CONFIG.ID,CONFIG.CONSTRAINTID,RULE.CLASSNAME,RULE.NAME,RULE.DESCRIPTION,CONFIG.CHECKLEVEL,CONFIG.RESULTTIP FROM BDCK.RT_BASECONSTRAINT CONFIG LEFT JOIN BDCK.T_CONSTRAINT RULE ON CONFIG.CONSTRAINTID=RULE.ID WHERE CONSTRAINTTYPE='"+constrainttype+"' AND RULE.ID IS NOT NULL AND CONFIG.BASEWORKFLOWID='"+listMap.get(0).getWORKFLOWNAME()+"' ORDER BY CONFIG.CONSTRAINTID";
				List<Map> listmap = basecommondao.getPageDataByFullSql(fullSql, page, rows);
				if (listmap != null && listmap.size() > 0) {
					for (Map mp : listmap) {
						String ruleid=StringHelper.formatObject(mp.get("CONSTRAINTID"));
						List<RT_CONSTRAINTEXP> listExp=basecommondao.getDataList(RT_CONSTRAINTEXP.class, "CONSTRAINTID='"+ruleid+"' AND WORKFLOWCODE='"+processData.getProdef_Code()+"'");
						if(listExp!=null&&listExp.size()>0){
							mp.put("ISEXP", "是");
						}else{
							mp.put("ISEXP", "否");
						}
						mp.put("WORKFLOWCODE", processData.getProdef_Code());
					}
				}
				long count = basecommondao.getCountByFullSql("from BDCK.RT_BASECONSTRAINT WHERE BASEWORKFLOWID='"+listMap.get(0).getWORKFLOWNAME()+"' AND CONSTRAINTTYPE='"+constrainttype+"'");
				m.setTotal(count);
				m.setRows(listmap);
			}
		}
		m.setMsg("成功！");
		return m;
	}
	
	/**
	 * 分页获取自定义受理约束(URL:"/{proid}/defineconstraint/{constrainttype}/",Method：GET)
	 * @Title: LoadDefineConstraint
	 * @author:俞学斌
	 * @date：2015年11月08日 15:47:50
	 * @return
	 */
	@SuppressWarnings({ "rawtypes"})
	@RequestMapping(value = "/{proid}/defineconstraint/{constrainttype}/", method = RequestMethod.GET)
	public @ResponseBody Message LoadDefineConstraint(@PathVariable("proid") String proid,@PathVariable("constrainttype") String constrainttype,HttpServletRequest request, HttpServletResponse response) {
		Message m=new Message();
		m.setSuccess("true");
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Wfd_Prodef processData = basecommondao.get(Wfd_Prodef.class, proid);
		if(processData!=null&&!StringHelper.isEmpty(processData.getProdef_Code())){
			String fullSql = "SELECT CONFIG.ID,RULE.CLASSNAME,RULE.NAME,RULE.DESCRIPTION,CONFIG.CHECKLEVEL,CONFIG.RESULTTIP FROM BDCK.RT_CONSTRAINT CONFIG LEFT JOIN BDCK.T_CONSTRAINT RULE ON CONFIG.CONSTRAINTID=RULE.ID WHERE CONFIG.CONSTRAINTTYPE='"+constrainttype+"' AND RULE.ID IS NOT NULL AND CONFIG.WORKFLOWCODE='"+processData.getProdef_Code()+"' ORDER BY CONFIG.CONSTRAINTID";
			List<Map> listmap = basecommondao.getPageDataByFullSql(fullSql, page, rows);
			long count = basecommondao.getCountByFullSql("from BDCK.RT_CONSTRAINT WHERE WORKFLOWCODE='"+processData.getProdef_Code()+"'");
			m.setTotal(count);
			m.setRows(listmap);
		}
		m.setMsg("成功！");
		return m;
	}
	
	/**
	 * 生效基础流程受理约束 (URL:"/baseconstraint/{constraintid}/{workflowcode}/",Method：POST)
	 * @Title: EffectBaseConstraint
	 * @author:俞学斌
	 * @date：2015年11月09日 02:42:31
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/baseconstraint/{constraintid}/{workflowcode}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage EffectBaseConstraint(@PathVariable("constraintid") String constraintid,@PathVariable("workflowcode") String workflowcode,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("生效约束失败");
		List<RT_CONSTRAINTEXP> listExp = basecommondao.getDataList(RT_CONSTRAINTEXP.class, "CONSTRAINTID='"+constraintid+"' AND WORKFLOWCODE='"+workflowcode+"'");
		if(listExp!=null&&listExp.size()>0){
			basecommondao.deleteEntity(listExp.get(0));
			basecommondao.flush();
			msg.setSuccess("true");
			msg.setMsg("生效约束成功！");
		}else{
			msg.setSuccess("false");
			msg.setMsg("未查询到要生效约束！");
		}
		return msg;
	}
	
	/**
	 * 忽略基础流程受理约束 (URL:"/baseconstraint/{constraintid}/{workflowcode}/",Method.DELETE)
	 * @Title: IgnoreBaseConstraint
	 * @author:俞学斌
	 * @date：2015年11月09日 02:42:31
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/baseconstraint/{constraintid}/{workflowcode}/", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage IgnoreBaseConstraint(@PathVariable("constraintid") String constraintid,@PathVariable("workflowcode") String workflowcode,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("忽略约束失败");
		List<RT_CONSTRAINTEXP> listExp = basecommondao.getDataList(RT_CONSTRAINTEXP.class, "CONSTRAINTID='"+constraintid+"' AND WORKFLOWCODE='"+workflowcode+"'");
		if(listExp!=null&&listExp.size()>0){
			msg.setSuccess("false");
			msg.setMsg("该约束已被忽略，不要重复操作！");
		}else{
			RT_CONSTRAINTEXP exp=new RT_CONSTRAINTEXP();
			String _id = SuperHelper.GeneratePrimaryKey();
			exp.setId(_id);
			exp.setCONSTRAINTID(constraintid);
			exp.setCREATETIME(new Date());
			exp.setCREATOR(Global.getCurrentUserInfo().getLoginName());
			exp.setWORKFLOWCODE(workflowcode);
			basecommondao.save(exp);
			basecommondao.flush();
			msg.setSuccess("true");
			msg.setMsg("忽略约束成功！");
		}
		return msg;
	}
	
	/**
	 * 移除自定义受理约束 (URL:"/defineconstraint/{id}/",Method：DELETE)
	 * @Title: RemoveDefineConstraint
	 * @author:俞学斌
	 * @date：2015年11月09日 02:42:31
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/defineconstraint/{id}/", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage RemoveDefineConstraint(@PathVariable("id") String id,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("删除约束失败");
		RT_CONSTRAINT definerule=basecommondao.get(RT_CONSTRAINT.class, id);
		if(definerule!=null){
			basecommondao.deleteEntity(definerule);
			basecommondao.flush();
			msg.setSuccess("true");
			msg.setMsg("删除约束成功！");
		}else{
			msg.setSuccess("false");
			msg.setMsg("未查询到要删除约束！");
		}
		return msg;
	}
	
	/**
	 * 添加自定义受理约束 (URL:"/defineconstraint/{constrainttype}/",Method：POST)
	 * @Title: AddDefineConstraint
	 * @author:俞学斌
	 * @date：2015年11月09日 20:40:31
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/defineconstraint/{constrainttype}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddDefineConstraint(@PathVariable("constrainttype") String constrainttype,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("添加失败");
		String ids="";
		if (request.getParameter("ids") != null) {
			ids = request.getParameter("ids");
		}
		String proid="";
		if (request.getParameter("proid") != null) {
			proid = request.getParameter("proid");
		}
		if(StringHelper.isEmpty(proid)){
			return msg;
		}
		if(StringHelper.isEmpty(ids)){
			return msg;
		}
		String workflowcode="";
		Wfd_Prodef processData = basecommondao.get(Wfd_Prodef.class, proid);
		if(processData!=null&&!StringHelper.isEmpty(processData.getProdef_Code())){
			workflowcode=processData.getProdef_Code();
		}
		if(StringHelper.isEmpty(workflowcode)){
			return msg;
		}
		String[] checkruleids=ids.split(",");
		if(checkruleids==null||checkruleids.length<=0){
			return msg;
		}
		for(String checkruleid:checkruleids){
			T_CONSTRAINT constraint=basecommondao.get(T_CONSTRAINT.class, checkruleid);
			if(constraint==null){
				continue;
			}
			RT_CONSTRAINT boardcheck=new RT_CONSTRAINT();
			String _id = SuperHelper.GeneratePrimaryKey();
			boardcheck.setId(_id);
			boardcheck.setCONSTRAINTID(checkruleid);
			boardcheck.setCHECKLEVEL(CHECKLEVEL.ERROR.Value);
			boardcheck.setCONSTRAINTTYPE(constrainttype);
			boardcheck.setRESULTTIP(constraint.getRESULTTIP());
			boardcheck.setCREATETIME(new Date());
			boardcheck.setCREATOR(Global.getCurrentUserInfo().getLoginName());
			boardcheck.setWORKFLOWCODE(workflowcode);
			basecommondao.save(boardcheck);
		}
		basecommondao.flush();
		msg.setSuccess("true");
		msg.setMsg("添加成功！");
		return msg;
	}
	
	/**
	 * 分页获取受理约束(不包含基准流程受理约束，且不包含自定义受理约束)(URL:"/{proid}/queryconstraint/",Method：GET)
	 * @Title: QueryConstraint
	 * @author:俞学斌
	 * @date：2015年11月07日 19:10:50
	 * @return
	 */
	@RequestMapping(value = "/{proid}/queryconstraint/", method = RequestMethod.GET)
	public @ResponseBody Message QueryConstraint(@PathVariable("proid") String proid,HttpServletRequest request, HttpServletResponse response) {

		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String filter="";
		try {
			filter = RequestHelper.getParam(request, "filter");
		} catch (Exception e) {
		}
		String workflowcode="";
		String workflowname="";
		if(!StringHelper.isEmpty(proid)){
			Wfd_Prodef processData = basecommondao.get(Wfd_Prodef.class, proid);
			if(processData!=null&&!StringHelper.isEmpty(processData.getProdef_Code())){
				workflowcode=processData.getProdef_Code();
				List<WFD_MAPPING> listMap=basecommondao.getDataList(WFD_MAPPING.class, "workflowcode='"+processData.getProdef_Code()+"'");
				if(listMap!=null&&listMap.size()>0&&!StringHelper.isEmpty(listMap.get(0).getWORKFLOWNAME())){
					workflowname=listMap.get(0).getWORKFLOWNAME();
				}
			}
		}
		
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 ");
		if (!StringHelper.isEmpty(filter)) {
			hqlBuilder.append(" AND (CLASSNAME LIKE '%").append(filter).append("%'");
			hqlBuilder.append(" OR NAME LIKE '%").append(filter).append("%'");
			hqlBuilder.append(" OR DESCRIPTION LIKE '%").append(filter).append("%')");
		}
		if (!StringHelper.isEmpty(workflowcode)) {
			hqlBuilder.append(" AND id NOT IN (SELECT CONSTRAINTID FROM RT_CONSTRAINT WHERE WORKFLOWCODE='");
			hqlBuilder.append(workflowcode);
			hqlBuilder.append("')");
		}
		if (!StringHelper.isEmpty(workflowname)) {
			hqlBuilder.append(" AND id NOT IN (SELECT CONSTRAINTID FROM RT_BASECONSTRAINT WHERE BASEWORKFLOWID='");
			hqlBuilder.append(workflowname);
			hqlBuilder.append("')");
		}
		Page p = basecommondao.getPageDataByHql(T_CONSTRAINT.class, hqlBuilder.toString(), page, rows);
		Message m=new Message();
		m.setSuccess("true");
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		m.setMsg("成功！");
		return m;
	}
	
	/**
	 * 更换自定义受理约束检查级别 (URL:"/defineconstraintlevel/{id}/",Method：POST)
	 * @Title: DefineConstraintLevel
	 * @author:俞学斌
	 * @date：2015年11月07日 20:27:31
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/defineconstraintlevel/{id}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage DefineConstraintLevel(@PathVariable("id") String id,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("更改失败");
		RT_CONSTRAINT rule=basecommondao.get(RT_CONSTRAINT.class, id);
		if(rule!=null){
			if(CHECKLEVEL.WARNING.Value.equals(rule.getCHECKLEVEL())){
				rule.setCHECKLEVEL(CHECKLEVEL.ERROR.Value);
			}else{
				rule.setCHECKLEVEL(CHECKLEVEL.WARNING.Value);
			}
			basecommondao.update(rule);
			basecommondao.flush();
			msg.setSuccess("true");
			msg.setMsg("更改成功");
		}else{
			msg.setSuccess("false");
			msg.setMsg("未查询到要更改约束！");
		}
		return msg;
	}
	
	/**
	 * 修改基准流程受理约束结果提示信息 (URL:"/editdefineconstraint/",Method：POST)
	 * @Title: EditDefineConstraint
	 * @author:俞学斌
	 * @date：2015年11月07日 20:27:31
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/editdefineconstraint/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage EditDefineConstraint(HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("更改失败");
		String ID="";
		try {
			ID = request.getParameter("ID");
		} catch (Exception e) {
		}
		String RESULTTIP="";
		try {
			RESULTTIP=request.getParameter("RESULTTIP");
		} catch (Exception e) {
		}
		RT_CONSTRAINT rule=basecommondao.get(RT_CONSTRAINT.class, ID);
		if(rule!=null){
			rule.setRESULTTIP(RESULTTIP);
			basecommondao.update(rule);
			basecommondao.flush();
			msg.setSuccess("true");
			msg.setMsg("更改成功");
		}else{
			msg.setSuccess("false");
			msg.setMsg("未查询到要更改约束！");
		}
		return msg;
	}
	/************************业务流程受理约束配置************************/
	
	
	/************************业务流程转出驳回配置************************/
	/**
	 * 分页获取转出驳回控制(URL:"/{routeid}/routecondition/{routetype}/",Method：GET)
	 * @Title: LoadRouteCondition
	 * @author:俞学斌
	 * @date：2015年11月08日 15:47:50
	 * @return
	 */
	@SuppressWarnings({ "rawtypes"})
	@RequestMapping(value = "/{routeid}/routecondition/{routetype}/", method = RequestMethod.GET)
	public @ResponseBody Message LoadRouteCondition(@PathVariable("routeid") String routeid,@PathVariable("routetype") String routetype,HttpServletRequest request, HttpServletResponse response) {
		Message m=new Message();
		m.setSuccess("true");
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String fullSql = "SELECT CONFIG.ID,RULE.CLASSNAME,RULE.NAME,RULE.DESCRIPTION,CONFIG.CHECKLEVEL FROM BDCK.RT_ROUTECONDITION CONFIG LEFT JOIN BDCK.T_CHECKRULE RULE ON CONFIG.CHECKRULEID=RULE.ID WHERE CONFIG.ROUTETYPE='"+routetype+"' AND CONFIG.ROUTEID='"+routeid+"' AND RULE.ID IS NOT NULL";
		List<Map> listmap = basecommondao.getPageDataByFullSql(fullSql, page, rows);
		long count = basecommondao.getCountByFullSql("from BDCK.RT_ROUTECONDITION WHERE ROUTEID='"+routeid+"' AND ROUTETYPE='"+routetype+"'");
		m.setTotal(count);
		m.setRows(listmap);
		m.setMsg("成功！");
		return m;
	}
	
	
	/**
	 * 移除转出驳回控制 (URL:"/routecondition/{id}/",Method：DELETE)
	 * @Title: RemoveRouteCondition
	 * @author:俞学斌
	 * @date：2015年12月18日 18:10:31
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/routecondition/{id}/", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage RemoveRouteCondition(@PathVariable("id") String id,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("删除控制失败！");
		RT_ROUTECONDITION routecondition=basecommondao.get(RT_ROUTECONDITION.class, id);
		if(routecondition!=null){
			basecommondao.deleteEntity(routecondition);
			basecommondao.flush();
			msg.setSuccess("true");
			msg.setMsg("删除控制成功！");
		}else{
			msg.setSuccess("false");
			msg.setMsg("未查询到要删除控制！");
		}
		return msg;
	}
	
	/**
	 * 添加转出驳回控制 (URL:"/routecondition/{routetype}/",Method：POST)
	 * @Title: AddRouteCondition
	 * @author:俞学斌
	 * @date：2015年11月09日 20:40:31
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/routecondition/{routetype}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddRouteCondition(@PathVariable("routetype") String routetype,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("添加失败");
		String ids="";
		if (request.getParameter("ids") != null) {
			ids = request.getParameter("ids");
		}
		String routeid="";
		if (request.getParameter("routeid") != null) {
			routeid = request.getParameter("routeid");
		}
		if(StringHelper.isEmpty(routeid)){
			return msg;
		}
		if(StringHelper.isEmpty(ids)){
			return msg;
		}
		String[] checkruleids=ids.split(",");
		if(checkruleids==null||checkruleids.length<=0){
			return msg;
		}
		for(String checkruleid:checkruleids){
			T_CHECKRULE checkrule=basecommondao.get(T_CHECKRULE.class, checkruleid);
			if(checkrule==null){
				continue;
			}
			RT_ROUTECONDITION routecondition=new RT_ROUTECONDITION();
			String _id = SuperHelper.GeneratePrimaryKey();
			routecondition.setId(_id);
			routecondition.setCHECKRULEID(checkruleid);
			routecondition.setCHECKLEVEL(CHECKLEVEL.ERROR.Value);
			routecondition.setROUTETYPE(routetype);
			routecondition.setCREATETIME(new Date());
			routecondition.setCREATOR(Global.getCurrentUserInfo().getLoginName());
			routecondition.setROUTEID(routeid);
			basecommondao.save(routecondition);
		}
		basecommondao.flush();
		msg.setSuccess("true");
		msg.setMsg("添加成功！");
		return msg;
	}
	
	
	/**
	 * 分页获取转出驳回控制(不包含路由已有转出驳回控制)(URL:"/{routeid}/queryroute/{routetype}/",Method：GET)
	 * @Title: QueryRoute
	 * @author:俞学斌
	 * @date：2015年11月07日 19:10:50
	 * @return
	 */
	@RequestMapping(value = "/{routeid}/queryroute/{routetype}/", method = RequestMethod.GET)
	public @ResponseBody Message QueryRoute(@PathVariable("routeid") String routeid,@PathVariable("routetype") String routetype,HttpServletRequest request, HttpServletResponse response) {

		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String filter="";
		try {
			filter = RequestHelper.getParam(request, "filter");
		} catch (Exception e) {
		}
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 ");
		if (!StringHelper.isEmpty(filter)) {
			hqlBuilder.append(" AND (CLASSNAME LIKE '%").append(filter).append("%'");
			hqlBuilder.append(" OR NAME LIKE '%").append(filter).append("%'");
			hqlBuilder.append(" OR DESCRIPTION LIKE '%").append(filter).append("%')");
		}
		hqlBuilder.append(" AND id NOT IN (SELECT CHECKRULEID FROM RT_ROUTECONDITION WHERE ROUTEID='");
		hqlBuilder.append(routeid);
		hqlBuilder.append("' AND ROUTETYPE='");
		hqlBuilder.append(routetype);
		hqlBuilder.append("')");
		Page p = basecommondao.getPageDataByHql(T_CHECKRULE.class, hqlBuilder.toString(), page, rows);
		Message m=new Message();
		m.setSuccess("true");
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		m.setMsg("成功！");
		return m;
	}
	
	/**
	 * 更换转出驳回控制检查级别 (URL:"/routelevel/{id}/",Method：POST)
	 * @Title: DefineConstraintLevel
	 * @author:俞学斌
	 * @date：2015年11月07日 20:27:31
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/routelevel/{id}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage RouteLevel(@PathVariable("id") String id,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("更改失败");
		RT_ROUTECONDITION rule=basecommondao.get(RT_ROUTECONDITION.class, id);
		if(rule!=null){
			if(CHECKLEVEL.WARNING.Value.equals(rule.getCHECKLEVEL())){
				rule.setCHECKLEVEL(CHECKLEVEL.ERROR.Value);
			}else{
				rule.setCHECKLEVEL(CHECKLEVEL.WARNING.Value);
			}
			basecommondao.update(rule);
			basecommondao.flush();
			msg.setSuccess("true");
			msg.setMsg("更改成功");
		}else{
			msg.setSuccess("false");
			msg.setMsg("未查询到要更改控制！");
		}
		return msg;
	}
	
	/************************业务流程转出驳回配置************************/
	

	/*****************************************业务流程配置*****************************************/
	
	
	/*****************************************登记系统配置*****************************************/
	
	/**
	 * 本地化配置页面(URL:"/configmanager/index/",Method：POST)
	 * @Title: getConfigManagerIndex
	 * @author:俞学斌
	 * @date：2015年11月07日 16:52:28
	 * @return
	 */
	@RequestMapping(value = "/configmanager/index/")
	public String getConfigManagerIndex() {
		return prefix + "config/configmanager";
	}
	
	/**
	 * 获取登记系统配置(URL:"/configmanager/",Method：GET)
	 * @Title: LoadConfigManager
	 * @author:俞学斌
	 * @date：2015年12月22日 17:38:50
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/configmanager/", method = RequestMethod.GET)
	public @ResponseBody Map LoadConfigManager(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String,String> map=new HashMap<String,String>();
		List<T_CONFIG> configs=basecommondao.getDataList(T_CONFIG.class, "1>0");
		if(configs!=null&&configs.size()>0){
			for(T_CONFIG config:configs){
				if(!StringHelper.isEmpty(config.getNAME())){
					if(!map.containsKey(config.getNAME())){
						map.put(config.getNAME(), config.getVALUE());
					}
					if(!map.containsKey(config.getNAME()+"_Url")){
						map.put(config.getNAME()+"_Url", StringHelper.formatObject(config.getURL()));
					}
				}
			}
		}
		return map;
	}
	
	/**
	 * 更新登记系统配置 (URL:"/configmanager/",Method：POST)
	 * @Title: UpdateConfigManager
	 * @author:俞学斌
	 * @date：2015年12月22日 17:38:50
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/configmanager/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateConfigManager(HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("保存失败！");
		try
		{
			request.setCharacterEncoding("UTF-8");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Map map=transToMAP(request.getParameterMap());
		Set set = map.keySet();
		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			Object obj = iterator.next();
			String name=StringHelper.formatObject(obj);
			Object val = map.get(obj);
			String value=StringHelper.formatObject(val);
			if(!StringHelper.isEmpty(name)){
				List<T_CONFIG> configs=basecommondao.getDataList(T_CONFIG.class, " NAME='"+name+"'");
				if(configs!=null&&configs.size()>0){
					T_CONFIG config=configs.get(0);
					config.setVALUE(value);
					basecommondao.update(config);
				}
			}
		}
		basecommondao.flush();
		ConfigHelper.reload();
		msg.setSuccess("true");
		msg.setMsg("保存成功！");
		return msg;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map transToMAP(Map parameterMap){
	      // 返回值Map
	      Map returnMap = new HashMap();
	      Iterator entries = parameterMap.entrySet().iterator();
	      Map.Entry entry;
	      String name = "";
	      String value = "";
	      while (entries.hasNext()) {
	          entry = (Map.Entry) entries.next();
	          name = (String) entry.getKey();
	          Object valueObj = entry.getValue();
	          if(null == valueObj){
	              value = "";
	          }else if(valueObj instanceof String[]){
	              String[] values = (String[])valueObj;
	              for(int i=0;i<values.length;i++){
	                  value = values[i] + ",";
	              }
	              value = value.substring(0, value.length()-1);
	          }else{
	              value = valueObj.toString();
	          }
	          returnMap.put(name, value);
	      }
	      return  returnMap;
	  }
	
	/*****************************************登记系统配置*****************************************/
	
	public class BaseWorkFlowClass{
		private String id="";
		private String text="";
		private String state="closed";
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		private List<BASEWORKFLOW> children=new ArrayList<CheckManagerController.BASEWORKFLOW>();
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public List<BASEWORKFLOW> getChildren() {
			return children;
		}
		public void setChildren(List<BASEWORKFLOW> children) {
			this.children = children;
		}
	}
	
	public class BASEWORKFLOW{
		private String id="";
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		private String text="";
	}
}
