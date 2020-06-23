package com.supermap.realestate.registration.web;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import parsii.eval.Scope;
import parsii.eval.Variable;

import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.mapping.CheckConfig;
import com.supermap.realestate.registration.mapping.CheckConfig.CheckGroup;
import com.supermap.realestate.registration.mapping.CheckConfig.CheckItem;
import com.supermap.realestate.registration.mapping.HandlerMapping.RegisterWorkFlow;
import com.supermap.realestate.registration.mapping.ICheckItem;
import com.supermap.realestate.registration.model.BDCS_CONSTRAINT;
import com.supermap.realestate.registration.model.BDCS_CONSTRAINTRT;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.ConstraintsType;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.workflow.model.Wfd_Pass_Condition;

/**
 * 配置Controller，流程相关的配置放在这里边
 * @ClassName: ConfigController
 * @author liushufeng
 * @date 2015年7月23日 下午11:10:04
 */
@Controller
@RequestMapping("/config")
public class ConfigController {

	private boolean realtimesave = true;

	@Autowired
	private CommonDao dao;

	// @Autowired
	// private ConfigService configservice;

	private final String prefix = "/realestate/registration/";

	/**
	 * 约束管理类index页
	 * @Title: getConstraintsIndex
	 * @author:liushufeng
	 * @date：2015年7月23日 下午11:34:28
	 * @return
	 */
	@RequestMapping(value = "/constraints/index/")
	public String getConstraintsIndex() {
		return prefix + "config/constraints";
	}

	/**
	 * 流程约束配置index页
	 * @Title: getConstraintsConfigIndex
	 * @author:liushufeng
	 * @date：2015年7月24日 上午12:33:51
	 * @return
	 */
	@RequestMapping(value = "/constraintsconfig/index/")
	public String getConstraintsConfigIndex() {
		return prefix + "config/constraintsconfig";
	}

	/**
	 * 获取系统已定义的所有约束
	 * @Title: getConstraints
	 * @author:liushufeng
	 * @date：2015年7月23日 下午11:34:03
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/constraints/", method = RequestMethod.GET)
	public @ResponseBody List<BDCS_CONSTRAINT> getConstraints(HttpServletRequest request, HttpServletResponse response) {
		List<BDCS_CONSTRAINT> list = dao.getDataList(BDCS_CONSTRAINT.class, "1>0");
		return list;
	}

	/**
	 * 获取流程对应的前置约束
	 * @Title: getWorkflowConstraints
	 * @author:liushufeng
	 * @date：2015年7月24日 下午4:08:01
	 * @param request
	 * @param response
	 * @param workflowid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/constraints/{workflowid}", method = RequestMethod.GET)
	public @ResponseBody List<Map> getWorkflowConstraints(HttpServletRequest request, HttpServletResponse response, @PathVariable("workflowid") String workflowid) {
		String sql = "SELECT  rt.Workflowid,rt.constraintid as id,rt.constrainttype,t.name,t.code  FROM bdck.bdcs_constraintrt rt left join bdck.bdcs_constraint t ON rt.constraintid=t.id where workflowid='"
				+ workflowid + "'";
		List<Map> listmap = dao.getDataListByFullSql(sql);
		return listmap;
	}

	/**
	 * 保存流程配置的前置约束条件
	 * @Title: updateWorkflowConstraints
	 * @author:liushufeng
	 * @date：2015年7月24日 下午4:37:38
	 * @param request
	 * @param response
	 * @param workflowid
	 * @return
	 */
	@RequestMapping(value = "/preconstraints/{workflowid}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage updateWorkflowPreConstraints(HttpServletRequest request, HttpServletResponse response, @PathVariable("workflowid") String workflowid) {
		ResultMessage msg = new ResultMessage();
		// 先把所有的都删除了
		if (!StringHelper.isEmpty(workflowid)) {
			dao.deleteEntitysByHql(BDCS_CONSTRAINTRT.class, "WORKFLOWID='" + workflowid + "' and CONSTRAINTTYPE='" + ConstraintsType.PRE.Value + "'");
		}
		// 再依次添加
		String strConstraintids = request.getParameter("consids");
		if (!StringHelper.isEmpty(strConstraintids)) {
			String[] ids = strConstraintids.split(",");
			for (String id : ids) {
				BDCS_CONSTRAINTRT rt = new BDCS_CONSTRAINTRT();
				rt.setCONSTRAINTID(id);
				rt.setWORKFLOWID(workflowid);
				rt.setCONSTRAINTTYPE(ConstraintsType.PRE.Value);
				dao.save(rt);
			}
		}
		dao.flush();
		msg.setSuccess("true");
		YwLogUtil.addYwLog("保存流程配置的前置约束条件", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
		return msg;
	}

	/**
	 * 保存流程的当前约束配置
	 * @Title: updateWorkflowCurConstraints
	 * @author:liushufeng
	 * @date：2015年7月24日 下午10:38:49
	 * @param request
	 * @param response
	 * @param workflowid
	 * @return
	 */
	@RequestMapping(value = "/curconstraints/{workflowid}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage updateWorkflowCurConstraints(HttpServletRequest request, HttpServletResponse response, @PathVariable("workflowid") String workflowid) {
		ResultMessage msg = new ResultMessage();
		// 先把所有的都删除了
		if (!StringHelper.isEmpty(workflowid)) {
			dao.deleteEntitysByHql(BDCS_CONSTRAINTRT.class, "WORKFLOWID='" + workflowid + "' and CONSTRAINTTYPE='" + ConstraintsType.CUR.Value + "'");
		}
		// 再依次添加
		String strConstraintids = request.getParameter("consids");
		if (!StringHelper.isEmpty(strConstraintids)) {
			String[] ids = strConstraintids.split(",");
			for (String id : ids) {
				BDCS_CONSTRAINTRT rt = new BDCS_CONSTRAINTRT();
				rt.setCONSTRAINTID(id);
				rt.setWORKFLOWID(workflowid);
				rt.setCONSTRAINTTYPE(ConstraintsType.CUR.Value);
				dao.save(rt);
			}
		}
		dao.flush();
		msg.setSuccess("true");
		YwLogUtil.addYwLog("保存流程的当前约束配置", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
		return msg;
	}

	/**
	 * 测试计算
	 * @Title: calculate
	 * @author:liushufeng
	 * @date：2015年8月8日 下午12:50:52
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/calculate", method = RequestMethod.GET)
	public @ResponseBody ResultMessage calculate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Scope scope = Scope.create();
		Variable result = scope.getVariable("result");
		Variable SFJS = scope.getVariable("SFJS");
		Variable MJ = scope.getVariable("MJ");
		Variable MJJS = scope.getVariable("MJJS");
		Variable SFZL = scope.getVariable("SFZL");
		Variable MJZL = scope.getVariable("MJZL");
		result.setValue(0);
		SFJS.setValue(100);
		MJ.setValue(130);
		MJJS.setValue(100);
		SFZL.setValue(5);
		MJZL.setValue(10);
		return null;
	}

	/************* 转出控制、登簿检查等检查项管理 **************/

	/**
	 * 检查项管理页面（URL:"/checkconfig/index/",Method:无限制）
	 * @Title: getCheckItems
	 * @author:liushufeng
	 * @date：2015年8月8日 下午6:33:11
	 * @return
	 */
	@RequestMapping(value = "/checkconfig/index/")
	public String checkConfigIndex() {
		return prefix + "config/checkitems";
	}

	/**
	 * 检查组管理页面（URL:"/checkconfig/group/index/",Method:无限制）
	 * @Title: checkGroupIndex
	 * @author:liushufeng
	 * @date：2015年8月8日 下午10:43:26
	 * @return
	 */
	@RequestMapping(value = "/checkconfig/group/index/", method = RequestMethod.GET)
	public String checkGroupIndex() {
		return prefix + "config/checkgroups";
	}

	/**
	 * 检查组数据列表（URL:"/checkconfig/group/groups/",Method:GET）
	 * @Title: getCheckGroups
	 * @author:liushufeng
	 * @date：2015年8月8日 下午6:35:23
	 * @return
	 */
	@RequestMapping(value = "/checkconfig/group/groups/", method = RequestMethod.GET)
	public @ResponseBody List<CheckGroup> getCheckGroups(HttpServletRequest request, HttpServletResponse response) {
		CheckConfig configs = HandlerFactory.getCheckConfig();
		if (configs != null) {
			YwLogUtil.addYwLog("检查组数据列表", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
			return configs.getCheckgroups();
		}
		return null;
	}

	/**
	 * 删除检查组（URL:"/checkconfig/group/groups/",Method:DELETE）
	 * @Title: deleteCheckGroup
	 * @author:liushufeng
	 * @date：2015年8月8日 下午11:12:11
	 * @param request
	 * @param response
	 * @param groupid
	 * @return
	 */
	@RequestMapping(value = "/checkconfig/group/groups/{groupid}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage deleteCheckGroup(HttpServletRequest request, HttpServletResponse response, @PathVariable("groupid") String groupid) {
		ResultMessage msg = new ResultMessage();
		if (!StringHelper.isEmpty(groupid)) {
			CheckConfig config = HandlerFactory.getCheckConfig();
			CheckGroup group = config.getCheckGroupByID(groupid);
			config.getCheckgroups().remove(group);
			if (realtimesave) {
				config.saveConfig();
			}
			msg.setSuccess("true");
			msg.setMsg("删除成功");
			YwLogUtil.addYwLog("删除检查组", ConstValue.SF.YES.Value,ConstValue.LOG.DELETE);
		}
		return msg;
	}

	/**
	 * 保存或者更新检查组数据（URL:"/checkconfig/group/groups/",Method:POST）
	 * @Title: saveOrUpdateCheckGroups
	 * @author:liushufeng
	 * @date：2015年8月8日 下午11:06:48
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/checkconfig/group/groups/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage saveOrUpdateCheckGroups(HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		String groupid = request.getParameter("groupid");
		String groupname = request.getParameter("groupname");
		String description = request.getParameter("description");
		String workflowcodes = request.getParameter("workflowcodes");
		CheckGroup group = null;
		CheckConfig config = HandlerFactory.getCheckConfig();
		if (StringHelper.isEmpty(groupid)) {
			group = new CheckGroup();
			group.setGroupid((String) SuperHelper.GeneratePrimaryKey());
			config.getCheckgroups().add(group);

		} else {

			if (config != null) {
				group = config.getCheckGroupByID(groupid);
			}
		}
		if (group != null) {
			group.setGroupname(groupname);
			group.setDescription(description);
			group.setWorkflowcodes(workflowcodes);
		}
		if (realtimesave) {
			config.saveConfig();
		}
		msg.setMsg("保存成功");
		msg.setSuccess("true");
		YwLogUtil.addYwLog("保存或者更新检查组数据", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
		return msg;
	}

	/**
	 * 删除检查项（URL:"/checkconfig/{itemid}",Method:POST）
	 * @Title: deleteCheckItem
	 * @author:liushufeng
	 * @date：2015年8月8日 下午11:35:18
	 * @param request
	 * @param response
	 * @param itemid
	 * @return
	 */
	@RequestMapping(value = "/checkconfig/{itemid}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage deleteCheckItem(HttpServletRequest request, HttpServletResponse response, @PathVariable("itemid") String itemid) {
		ResultMessage msg = new ResultMessage();
		if (!StringHelper.isEmpty(itemid)) {
			CheckConfig config = HandlerFactory.getCheckConfig();
			ICheckItem item = config.getCheckItem(itemid);
			if (item != null) {
				config.getCheckitems().remove(item);
			}
			if (realtimesave) {
				config.saveConfig();
			}
			msg.setSuccess("true");
			msg.setMsg("删除成功");
			YwLogUtil.addYwLog("删除检查项", ConstValue.SF.YES.Value,ConstValue.LOG.DELETE);
		}

		return msg;
	}

	/**
	 * 检查项数据列表（URL:"/checkconfig/",Method:GET）
	 * @Title: getCheckGroups
	 * @author:liushufeng
	 * @date：2015年8月8日 下午6:33:26
	 * @return
	 */
	@RequestMapping(value = "/checkconfig/", method = RequestMethod.GET)
	public @ResponseBody List<CheckItem> getCheckItems() {
		CheckConfig configs = HandlerFactory.getCheckConfig();
		if (configs != null) {
			return configs.getCheckitems();
		}
		return null;
	}

	/**
	 * 保存或更新检查项（URL:"/checkconfig/checkitem/",Method:POST）
	 * @Title: saveOrUpdateCheckItem
	 * @author:liushufeng
	 * @date：2015年8月8日 下午6:36:01
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/checkconfig/checkitem/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage saveOrUpdateCheckItem(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		ResultMessage msg = new ResultMessage();
		String itemid = request.getParameter("id");
		String itemname = request.getParameter("itemname");
		String sqlexpr = request.getParameter("sqlexpr");
		String resultexpr = request.getParameter("resultexpr");
		String checklevel = request.getParameter("checklevel");
		String errorinfo = request.getParameter("errorinfo");
		String questioninfo = request.getParameter("questioninfo");
		String creator = request.getParameter("creator");
		String description = request.getParameter("description");
		ICheckItem item = null;
		CheckConfig config = HandlerFactory.getCheckConfig();
		if (StringHelper.isEmpty(itemid)) {
			item = new CheckItem();
			item.setId((String) SuperHelper.GeneratePrimaryKey());
			config.getCheckitems().add((CheckItem) item);

		} else {

			if (config != null) {
				item = config.getCheckItem(itemid);
			}
		}
		if (item != null) {
			item.setITEMNAME(itemname);
			item.setSQLEXPR(sqlexpr);
			item.setRESULTEXPR(resultexpr);
			item.setCHECKLEVEL(checklevel);
			item.setERRORINFO(errorinfo);
			item.setQUESTIONINFO(questioninfo);
			item.setCREATOR(creator);
			item.setDESCRIPTION(description);
		}
		if (realtimesave) {
			config.saveConfig();
		}
		msg.setMsg("保存成功");
		msg.setSuccess("true");
		YwLogUtil.addYwLog("保存或更新检查项", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
		return msg;
	}

	/**
	 * 保存配置到XML文件（URL:"/checkconfig/save/",Method:GET）
	 * @Title: saveConfig
	 * @author:liushufeng
	 * @date：2015年8月8日 下午6:39:16
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/checkconfig/save/", method = RequestMethod.GET)
	public @ResponseBody ResultMessage saveConfig(HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		CheckConfig config = HandlerFactory.getCheckConfig();
		config.saveConfig();
		msg.setSuccess("true");
		msg.setMsg("保存成功");
		YwLogUtil.addYwLog("保存配置到XML文件-checkconfig", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
		return msg;
	}

	/**
	 * 往检查组中添加检查项（URL:"/checkconfig/group/groups/{groupid}/items/{itemid}",
	 * Method:POST）
	 * @Title: addItemToGroup
	 * @author:liushufeng
	 * @date：2015年8月8日 下午11:57:01
	 * @param request
	 * @param response
	 * @param groupid
	 * @param itemid
	 * @return
	 */
	@RequestMapping(value = "/checkconfig/group/groups/{groupid}/items/{itemid}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage addItemToGroup(HttpServletRequest request, HttpServletResponse response, @PathVariable("groupid") String groupid,
			@PathVariable("itemid") String itemid) {
		ResultMessage msg = new ResultMessage();
		if (!StringHelper.isEmpty(groupid) && !StringHelper.isEmpty(itemid)) {
			CheckConfig config = HandlerFactory.getCheckConfig();
			if (config != null) {
				CheckGroup group = config.getCheckGroupByID(groupid);
				if (group != null) {
					if (!group.containItem(itemid)) {
						group.getItems().add(itemid);
						msg.setSuccess("true");
						msg.setMsg("添加检查项成功");
						YwLogUtil.addYwLog("往检查组中添加检查项", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
					}
				}
				if (realtimesave) {
					config.saveConfig();
				}
			}
		}
		return msg;
	}

	/**
	 * 移除组中的检查项（URL:"/checkconfig/group/groups/{groupid}/items/{itemid}",Method:
	 * POST）
	 * @Title: removeItemFromGroup
	 * @author:liushufeng
	 * @date：2015年8月8日 下午11:56:43
	 * @param request
	 * @param response
	 * @param groupid
	 * @param itemid
	 * @return
	 */
	@RequestMapping(value = "/checkconfig/group/groups/{groupid}/items/delete/{itemid}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage removeItemFromGroup(HttpServletRequest request, HttpServletResponse response, @PathVariable("groupid") String groupid,
			@PathVariable("itemid") String itemid) {
		ResultMessage msg = new ResultMessage();
		if (!StringHelper.isEmpty(groupid) && !StringHelper.isEmpty(itemid)) {
			CheckConfig config = HandlerFactory.getCheckConfig();
			if (config != null) {
				CheckGroup group = config.getCheckGroupByID(groupid);
				if (group != null) {
					if (group.containItem(itemid)) {
						group.removeItem(itemid);
						msg.setSuccess("true");
						YwLogUtil.addYwLog("移除组中的检查项", ConstValue.SF.YES.Value,ConstValue.LOG.DELETE);
					}
				}
				if (realtimesave) {
					config.saveConfig();
				}
			}
		}
		return msg;
	}

	/**
	 * 保存检查项和路由添加关系
	 * @author diaoliwei
	 * @date 2015-8-9
	 * @param request
	 * @param response
	 * @param groupid
	 *            条件
	 * @param prodefID
	 *            路由id
	 * @return
	 */
	@RequestMapping(value = "/checkconfig/{groupid}/{Route_Id}/{Route_Type}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage addCheckRelation(HttpServletRequest request, @PathVariable("groupid") String groupid, @PathVariable("Route_Id") String Route_Id
			, @PathVariable("Route_Type") String Route_Type) {
		ResultMessage msg = new ResultMessage();
		msg.setMsg("保存失败");
		msg.setSuccess("false");
		String type = request.getParameter("type");

		StringBuilder delHqlBuilder = new StringBuilder();
		if (!StringUtils.isEmpty(type) && !StringUtils.isEmpty(Route_Id)&& !StringUtils.isEmpty(Route_Type)) {
			delHqlBuilder.append(" CONDITION_TYPE = '").append(type).append("' ");
			delHqlBuilder.append(" and ROUTE_ID= '").append(Route_Id).append("' ");
			delHqlBuilder.append(" and ROUTE_TYPE= '").append(Route_Type).append("' ");
			dao.deleteEntitysByHql(Wfd_Pass_Condition.class, delHqlBuilder.toString());

			if(!"null".equals(groupid) && !StringUtils.isEmpty(groupid)){
				String[] ids = groupid.split(",");
				for (int i = 0; i < ids.length; i++) {
					Wfd_Pass_Condition condition = new Wfd_Pass_Condition();
					condition.setCondition_Type(type); // 类型
					condition.setRoute_Id(Route_Id);
					String id = ids[i];
					condition.setCondition_Name(id); // 检查项或检查项组的id
					condition.setRoute_Type(Route_Type);
					dao.save(condition); // 保存
				}
			}
			dao.flush();
			msg.setMsg("保存成功");
			msg.setSuccess("true");
			YwLogUtil.addYwLog("保存检查项和路由添加关系", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
		}
		return msg;
	}
	
	/**
	 * 查询路由已经包含的检查项和检查组
	 * @author diaoliwei
	 * @date 2015-8-10
	 * @param request
	 * @param Route_Id
	 * @return
	 */
	@RequestMapping(value = "/checkconfig/{Route_Id}/{Route_Type}", method = RequestMethod.GET)
	public @ResponseBody Message showCheckRelation(HttpServletRequest request, @PathVariable("Route_Id") String Route_Id
			, @PathVariable("Route_Type") String Route_Type) {
		Message msg = new Message();
		String hql = " Route_Id  = '" + Route_Id + "' AND Route_Type='"+Route_Type+"'";
		Page p = new Page();
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10000;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		p = dao.getPageDataByHql(Wfd_Pass_Condition.class, hql, page, rows);
		msg.setRows(p.getResult());
		msg.setTotal(p.getTotalCount());
		return msg;
	}
	/**
	 * 返回页面控件配置
	 * @author wuzhu
	 * @date 2015-8-10
	 * @param request
	 * @param Route_Id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/pagecontrolsconfig/{project_id}/{pagetype}", method = RequestMethod.GET)
	public @ResponseBody Map getPageControlsFromConfig(HttpServletRequest request
			, @PathVariable("project_id") String project_id
			, @PathVariable("pagetype") String pagetype) {
 	RegisterWorkFlow flow = HandlerFactory.getWorkflow(project_id);
	Map config=new HashMap<String, String>();
 	if(flow!=null)
	 config =HandlerFactory.getPageControlsConfig(flow.getName().hashCode(),pagetype);
	return config;
	}
	
	/**
	 * 获取行政区代码配置
	 * @Title: 
	 * @author:likun
	 * @date：2015年12月3日 下午11:34:28
	 * @return
	 */
	@RequestMapping(value = "/checkconfig/xzqhdm/", method = RequestMethod.GET)
	public @ResponseBody String getXZQHDM(HttpServletRequest request, HttpServletResponse response) {
		return ConfigHelper.getNameByValue("XZQHDM");
	}
}
