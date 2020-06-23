package com.supermap.wisdombusiness.workflow.service.wfd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.supermap.realestate.registration.check.BoardBookCheck;
import com.supermap.realestate.registration.config.CommonCheck;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfd_Actdef;
import com.supermap.wisdombusiness.workflow.model.Wfd_Pass_Condition;
import com.supermap.wisdombusiness.workflow.model.Wfd_Route;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.WFConst;

/**
 * 关于流程路由的操作
 * */
@Component("smRouteDef")
public class SmRouteDef {

	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private BoardBookCheck boardbook_check;

	/**
	 * 获取一个活动的后续路由
	 * 
	 * @param actdefid
	 *            String 活动定义ID
	 * */
	public List<Wfd_Route> GetNextRoute(String actdefid) {
		if (actdefid != "") {

			List<Wfd_Route> dRoute = commonDao.getDataList(Wfd_Route.class,
					"select * from " + Common.WORKFLOWDB
							+ "Wfd_Route where ACTDEF_ID='" + actdefid + "'");
		//添加判断后续环节是否为分支环节
			if(dRoute.size()>0){
				Wfd_Actdef actdef=commonDao.get(Wfd_Actdef.class,dRoute.get(0).getNext_Actdef_Id());
				if(actdef.getActdef_Type().equals(WFConst.ActDef_Type.ConditionOr.value+"")){
					return GetNextRoute(actdef.getActdef_Id());
				}
				return dRoute;
			}
		}
			return null;
	}
	
	/**
	 * 获取一个活动的前置路由
	 * 
	 * @param actdefid
	 *            String 活动定义ID
	 * */
	public List<Wfd_Route> GetPreRoute(String actdefid) {
		if (actdefid != "") {

			List<Wfd_Route> dRoute = commonDao.getDataList(Wfd_Route.class,
					"select * from " + Common.WORKFLOWDB
							+ "Wfd_Route where NEXT_ACTDEF_ID='" + actdefid + "'");
			return dRoute;
		} else {
			return null;
		}
	}
	
	/**
	 * 根据活动实例获取后续路由
	 * 
	 * @param actinstid
	 *            String 活动实例ID
	 * @return 活动路由集合
	 * */
	public List<Wfd_Route> GetNextRouteByActinstID(String actinstid) {
		List<Wfd_Route> routes = null;
		if (actinstid != "") {
			Wfi_ActInst wfi_ActInst = commonDao.get(Wfi_ActInst.class,
					actinstid);
			if (wfi_ActInst != null) {
				routes = GetNextRoute(wfi_ActInst.getActdef_Id());

			}

		}
		return routes;
	}
	
	/**
	 * 根据活动实例获取前置路由
	 * 
	 * @param actinstid
	 *            String 活动实例ID
	 * @return 活动路由集合
	 * */
	public List<Wfd_Route> GetPreRouteByActinstID(String actinstid) {
		List<Wfd_Route> routes = null;
		if (actinstid != "") {
			Wfi_ActInst wfi_ActInst = commonDao.get(Wfi_ActInst.class,
					actinstid);
			if (wfi_ActInst != null) {
				routes = GetPreRoute(wfi_ActInst.getActdef_Id());
			}
		}
		return routes;
	}

	/**
	 * 获取一个并发后动后的所有并发路由
	 * 
	 * @throws Exception
	 * */

	public List<Wfd_Route> GetAllConcurrentRoutesByActdef(Wfd_Actdef actdef)
			throws Exception {
		List<Wfd_Route> list = null;
		if (actdef != null) {

			String type = actdef.getActdef_Type();
			if (type.equals(WFConst.ActDef_Type.ConditionAnd.value + "")) {
				StringBuilder sBuilder = new StringBuilder();
				sBuilder.append("select * from " + Common.WORKFLOWDB
						+ "WFD_ROUTE where actdef_id='");
				sBuilder.append(actdef.getActdef_Id());
				sBuilder.append("' and prodef_id='");
				sBuilder.append(actdef.getProdef_Id());
				sBuilder.append("'");
				list = commonDao.getDataList(Wfd_Route.class,
						sBuilder.toString());

			} else {
				throw new Exception("此活动不是并发活动");
			}
		}
		return list;
	}

	/**
	 * 获取一个流程定义下的所有路由
	 * */
	public List<Wfd_Route> GetAllRouteByProinstID(String proinstid) {
		List<Wfd_Route> list = null;
		list = commonDao.getDataList(Wfd_Route.class, "select * from  "
				+ Common.WORKFLOWDB + "Wfd_Route where prodef_id='" + proinstid
				+ "'");
		return list;
	}

	/**
	 * 获取一个路由下的条件
	 * */
	public List<Wfd_Pass_Condition> getRouteConditions(String routeid) {
		List<Wfd_Pass_Condition> list = null;
		if (routeid != null && !routeid.equals("")) {
			StringBuilder sbBuilder = new StringBuilder();
			sbBuilder.append(" ROUTE_ID='");
			sbBuilder.append(routeid);
			sbBuilder.append("'");
			list = commonDao.findList(Wfd_Pass_Condition.class,
					sbBuilder.toString());
		}
		return list;
	}

	/*
	 * 验证路由，按照配置验证是否可以转出
	 */
	public Map<String, String> ValiteRoute(String routeid, String Project_ID) {
		Map<String, String> info = new HashMap<String, String>();
		String RouteCheckType=ConfigHelper.getNameByValue("RouteCheckType");
		if(!"1".equals(RouteCheckType)){
			ResultMessage ms=boardbook_check.Routecheck(Project_ID, routeid, "0");
			if(("true").equals(ms.getSuccess())){
				info.put("BM", "1");
				info.put("MS", "");
			}else if(("warning").equals(ms.getSuccess())){
				info.put("BM", "2");
				info.put("MS", ms.getMsg());
			}else{
				info.put("BM", "3");
				info.put("MS", ms.getMsg());
			}
		}else{
			if (routeid != null && !routeid.equals("")) {
				StringBuilder sBuilder = new StringBuilder();
				sBuilder.append("select * from ");
				sBuilder.append(Common.WORKFLOWDB + "Wfd_Pass_Condition where ");
				sBuilder.append("route_id='");
				sBuilder.append(routeid);
				sBuilder.append("'");
				sBuilder.append(" and route_type='0'");
				List<Wfd_Pass_Condition> passcondition = commonDao.getDataList(
						Wfd_Pass_Condition.class, sBuilder.toString());
				List<String> item = new ArrayList<String>();
				List<String> group = new ArrayList<String>();
				if (passcondition != null && passcondition.size() > 0) {
					
					for (Wfd_Pass_Condition wfd_Pass_Condition : passcondition) {

						String typeString = wfd_Pass_Condition.getCondition_Type();
						if (typeString
								.equals(WFConst.PassConditionType.ConditionItem.value)) {
							item.add(wfd_Pass_Condition.getCondition_Name());
						} else if (typeString
								.equals(WFConst.PassConditionType.ConditionGroup.value)) {
							group.add(wfd_Pass_Condition.getCondition_Name());
						}
					}
				}
				try {
					info=CommonCheck.CheckProject(Project_ID, item, group,"0");
				} catch (Exception e) {
					info=new HashMap<String, String>();
					info.put("BM", "-1");
					info.put("MS", "检查规则出现异常，请联系管理员");
				}
			}
		}
		return info;
	}
  
	/*
	 * 验证路由，按照配置验证是否可以驳回
	 */
	public Map<String, String> ValiteRoute_Reject(String routeid, String Project_ID) {
		Map<String, String> info = new HashMap<String, String>();
		String RouteCheckType=ConfigHelper.getNameByValue("RouteCheckType");
		if(!"1".equals(RouteCheckType)){
			ResultMessage ms=boardbook_check.Routecheck(Project_ID, routeid, "1");
			if(("true").equals(ms.getSuccess())){
				info.put("BM", "1");
				info.put("MS", "");
			}else if(("warning").equals(ms.getSuccess())){
				info.put("BM", "2");
				info.put("MS", ms.getMsg());
			}else{
				info.put("BM", "3");
				info.put("MS", ms.getMsg());
			}
		}else{
			if (routeid != null && !routeid.equals("")) {
				StringBuilder sBuilder = new StringBuilder();
				sBuilder.append("select * from ");
				sBuilder.append(Common.WORKFLOWDB + "Wfd_Pass_Condition where ");
				sBuilder.append("route_id='");
				sBuilder.append(routeid);
				sBuilder.append("'");
				sBuilder.append(" and route_type='1'");
				List<Wfd_Pass_Condition> passcondition = commonDao.getDataList(
						Wfd_Pass_Condition.class, sBuilder.toString());
				List<String> item = new ArrayList<String>();
				List<String> group = new ArrayList<String>();
				if (passcondition != null && passcondition.size() > 0) {
					
					for (Wfd_Pass_Condition wfd_Pass_Condition : passcondition) {

						String typeString = wfd_Pass_Condition.getCondition_Type();
						if (typeString
								.equals(WFConst.PassConditionType.ConditionItem.value)) {
							item.add(wfd_Pass_Condition.getCondition_Name());
						} else if (typeString
								.equals(WFConst.PassConditionType.ConditionGroup.value)) {
							group.add(wfd_Pass_Condition.getCondition_Name());
						}
					}
				}
				try {
					info=CommonCheck.CheckProject(Project_ID, item, group,"1");
				} catch (Exception e) {
					info=new HashMap<String, String>();
					info.put("BM", "-1");
					info.put("MS", "检查规则出现异常，请联系管理员");
				}
			}
		}
		return info;
	}
	/**
	 * 验证项目办结
	 * @param actinstid
	 * @return
	 */
	public Map<String, String> ValiteProInstOver(String actinstid){
		Map<String, String> info = null;
		
		return info;
	}

	//获取一个活动的前置路由
	public List<Wfd_Route> GetProRoute(String actdefid){
		StringBuilder sb=new StringBuilder();
		sb.append("select * from ");
		sb.append(Common.WORKFLOWDB);
		sb.append("wfd_route where next_actdef_id='"+actdefid+"'");
		return commonDao.getDataList(Wfd_Route.class, sb.toString());
		
	}
	//
	/**
	 *获取流程定义的所有路由 
	 * @param prodefid
	 * @return
	 */
	public List<Wfd_Route> GetAllRouteByProdefid(String prodefid){
		List<Wfd_Route> list=null;
		StringBuilder sb=new StringBuilder();
		sb.append("select * from ");
		sb.append(Common.WORKFLOWDB);
		sb.append("wfd_route where prodef_id='");
		sb.append(prodefid);
		sb.append("'");
		list=commonDao.getDataList(Wfd_Route.class, sb.toString());
		return list;
	}

	/**
	 * 获取一个活动后的所有路由
	 *
	 * @throws Exception
	 * */

	public List<Wfd_Route> GetAllRoutesByActdef(Wfd_Actdef actdef){
		List<Wfd_Route> list = null;
		if (actdef != null) {
			StringBuilder sBuilder = new StringBuilder();
			sBuilder.append("select * from " + Common.WORKFLOWDB
					+ "WFD_ROUTE where actdef_id='");
			sBuilder.append(actdef.getActdef_Id());
			sBuilder.append("' and prodef_id='");
			sBuilder.append(actdef.getProdef_Id());
			sBuilder.append("'");
			list = commonDao.getDataList(Wfd_Route.class,
					sBuilder.toString());
		}
		return list;
	}
}
