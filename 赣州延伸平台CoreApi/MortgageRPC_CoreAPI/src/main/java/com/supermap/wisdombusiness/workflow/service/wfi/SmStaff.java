package com.supermap.wisdombusiness.workflow.service.wfi;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//import oracle.net.aso.l;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.realestate.registration.util.SystemConfig;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.service.UserService;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfd_Actdef;
import com.supermap.wisdombusiness.workflow.model.Wfd_Route;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInstStaff;
import com.supermap.wisdombusiness.workflow.model.Wfi_PassWork;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_Route;
import com.supermap.wisdombusiness.workflow.model.Wfi_Spdy;
import com.supermap.wisdombusiness.workflow.model.Wfi_Tr_ActDefToSpdy;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.SmActInfo;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.WFConst;
import com.supermap.wisdombusiness.workflow.service.wfd.SmActDef;
import com.supermap.wisdombusiness.workflow.service.wfd.SmRoleDef;
import com.supermap.wisdombusiness.workflow.service.wfd.SmRouteDef;
import com.supermap.wisdombusiness.workflow.service.wfm.OperationService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProInstService;

import net.sf.json.JSONObject;

@Component("smStaff")
public class SmStaff {

	@Autowired
	private CommonDao dao;

	@Autowired
	private SmActDef smActDef;
	@Autowired
	private SmRoleDef smRoleDef;
	@Autowired
	private SmRouteDef smRouteDef;
	@Autowired
	private SmActInst smActInst;
	@Autowired
	private SmRouteInst smRouteInst;

	@Autowired
	private UserService userService;

	@Autowired
	private SmProInst smProInst;

	@Autowired
	private OperationService operaionService;

	public List<Wfi_PassWork> GetPassWorkByStaffID(String StaffID) {
		String staffId = "";
		//
		List<Wfi_PassWork> list = userService.GetPassWorkByStaffID(StaffID);
		return list;

	}

	public String getDistIdByStaffIDString(String StaffID) {
		// String staffId = "";
		//
		String lshConfig = ConfigHelper.getNameByValue("lshConfig");
		try {
			if ("2".equals(lshConfig)) {
				String area = GetCurrentAreaCode();
				if (null != area && !area.isEmpty()) {
					return area;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return ConfigHelper.getNameByValue("XZQHDM");
	}

	/**
	 * 获取当前操作人员的ID
	 * 
	 */
	public String getCurrentWorkStaffID() {
		try {
			User user = userService.getCurrentUserInfo();
			if (user != null) {
				return user.getId();
			} else {
				return null;
			}

		} catch (Exception e) {
			return "";
		}

	}

	/*
	 * 获取在线用户信息
	 */
	public User getCurrentWorkStaff() {
		try {
			return userService.getCurrentUserInfo();
		} catch (Exception e) {
			return null;
		}

	}

	public String GetStaffName(String StaffID) {
		User user = userService.findById(StaffID);
		if (user != null) {
			return user.getUserName();
		} else {
			return "";
		}

	}

	// 获取员工所在的部门ID
	public String GetStaffDeptID(String StaffID) {

		return userService.findById(StaffID).getDepartment().getId();
	}

	// 获取员工所在AreaCode
	public String GetAreaCode(String StaffID) {

		return userService.findById(StaffID).getAreaCode();
	}

	// 获取员工所在AreaCode
	public String GetCurrentAreaCode() {
		String id = getCurrentWorkStaffID();
		if (null != id) {
			return userService.findById(id).getAreaCode();
		} else {
			return null;
		}

	}

	public String GetCurrentAreaCode(String staffid) {
		return userService.findById(staffid).getAreaCode();
	}

	/**
	 * 通过活动实例ID 获取下一步可以转出人员列表
	 * 
	 * @param actinstid
	 *            String 当前活动实例ID
	 * @return List<SmObjInfo> 路由和人员集合 这个列表的信息层级 ----Route 路由信息 ID 路由ID name
	 *         路由名称 cell 活动 ------ActDef 活动定义信息 ID 定义ID Name 活动名称 cell 部门
	 *         --------Depetment 部门信息 ID 部门ID Name 部门名称 cell 员工 -----------Staff
	 *         员工信息 ID 员工ID Name 员工姓名 cell null
	 * @throws Exception
	 */
	public List<SmObjInfo> GetActStaffByActInst(String actinstid) throws Exception {
		List<SmObjInfo> list = new ArrayList<SmObjInfo>();
		List<SmObjInfo> listtemp = new ArrayList<SmObjInfo>();
		Wfd_Actdef currrentActDef = smActInst.GetActDef(actinstid);
		// 根据当前活动找到下一步路由 是一个集合
		List<Wfd_Route> routedefList = smRouteDef.GetNextRouteByActinstID(actinstid);
		if (routedefList != null && routedefList.size() > 0)// 一个交互活动只有一个指出
		{
			// 判断是否是跳跃驳回前置
			Wfi_Route route = smRouteInst.IsSkipPassOver(actinstid, routedefList);
			if (route != null && !currrentActDef.getActdef_Type().equals(WFConst.ActDef_Type.ConditionAnd.value + "")) {
				list.add(GetSkipPassBaskInfo(route));
			}
			// 判断当前环节是否是分支环节；
			/// 判断当前环节的下一个环节的actdef定义是否是分之环节
			// String actdef_type = currrentActDef.getActdef_Type() ;
			for (int i = 0; i < routedefList.size(); i++) {
				Wfd_Route route2 = routedefList.get(i);
				String nextactid = route2.getNext_Actdef_Id();
				Wfd_Actdef actdef = smActDef.GetActDefByID(nextactid);
				if (actdef.getActdef_Type().equals(WFConst.ActDef_Type.ConditionOr.value + "")) {
					List<Wfd_Route> routedefList2 = smRouteDef.GetNextRoute(actdef.getActdef_Id());
					for (int j = 0; j < routedefList2.size(); j++) {
						listtemp = GetStaffByRoutes(routedefList2.get(j), actinstid);
						if (listtemp.size() > 0) {
							list.add(listtemp.get(0));
						}
					}
					// 若果这个活动是跳跃驳回则需要
					// Wfi_Route route = smRouteInst.IsSkipPassOver(actinstid,
					// routedefList.get(i).getNext_Actdef_Id());
					// if (route != null) {
					// list.add(GetSkipPassBaskInfo(route));
					// }
				} else {
					listtemp = GetStaffByRoutes(routedefList.get(i), actinstid);
					if (listtemp.size() > 0) {
						list.add(listtemp.get(0));
					}
					// 若果这个活动是跳跃驳回则需要
					// Wfi_Route route = smRouteInst.IsSkipPassOver(actinstid,
					// routedefList.get(i).getNext_Actdef_Id());
					// if (route != null) {
					// list.add(GetSkipPassBaskInfo(route));
					// }
					// 判断驳回转出控制，驳回后只能转给驳回人
					JSONObject obj = JSONObject.fromObject(currrentActDef.getCustomeParamsSet());
					if (!obj.isNullObject()) {
						if (obj.containsKey("BHZCKZ")) {
							String zckz = obj.getString("BHZCKZ");
							if (zckz != null && !zckz.equals("")) {
								Wfi_Route route1 = smRouteInst.PassOver(actinstid);
								if (route1 != null) {
									List<SmObjInfo> list1 = new ArrayList<SmObjInfo>();
									list1.add(GetSkipPassBaskInfo(route1));
									list = list1;
								}
							}
						}
					}
					// 海口判断驳回之前是否有审批意见未删除，如果未删除则只能转出给驳回人
					String area = operaionService.getNativeAreaCodeConfig();
					if (area != null && area.equals("460100")) {
						Wfi_Route route1 = smRouteInst.IsPassOver(actinstid, routedefList.get(i).getNext_Actdef_Id());
						if (route1 != null) {
							List<SmObjInfo> list1 = new ArrayList<SmObjInfo>();
							list1.add(GetSkipPassBaskInfo(route1));
							list = list1;
						}
					}
				}
			}
			// 并发活动使用
			if (currrentActDef.getActdef_Type().equals(WFConst.ActDef_Type.ConditionAnd.value + "")
					&& list.size() > 1) {
				list.remove(0);
			}

			/*
			 * if(actdef_type.equals(WFConst.ActDef_Type.ConditionOr.value+"")){
			 * for (int i = 0;i<routedefList.size();i++){
			 * listtemp=GetStaffByRoutes(routedefList.get(i), actinstid);
			 * if(listtemp.size()>0){ list.add(listtemp.get(0)); } //
			 * 若果这个活动是跳跃驳回则需要 Wfi_Route route =
			 * smRouteInst.IsSkipPassOver(actinstid,
			 * routedefList.get(i).getNext_Actdef_Id()); if (route != null) {
			 * list.add(GetSkipPassBaskInfo(route)); } } }else{ list =
			 * GetStaffByRoutes(routedefList.get(0), actinstid); //
			 * 若果这个活动是跳跃驳回则需要 Wfi_Route route =
			 * smRouteInst.IsSkipPassOver(actinstid,
			 * routedefList.get(0).getNext_Actdef_Id()); if (route != null) {
			 * list.add(GetSkipPassBaskInfo(route)); } }
			 */
		} else {
			// 没有后续路由办结
			list = new ArrayList<SmObjInfo>();
			SmObjInfo endInfo = new SmObjInfo();
			endInfo.setID("");
			endInfo.setName("办结");
			Wfd_Actdef actdef = smActInst.GetActDef(actinstid);
			if (actdef != null) {
				List<SmObjInfo> actdefList = new ArrayList<SmObjInfo>();
				SmObjInfo Act = new SmObjInfo();
				Act.setID(actdef.getActdef_Id());
				Act.setName(actdef.getActdef_Name());
				Act.setDesc("ActDef");
				actdefList.add(Act);
				endInfo.setChildren(actdefList);
			}
			list.add(endInfo);
		}

		return list;
	}

	/**
	 * 通过路由集合获取员工数据
	 * 
	 * @throws Exception
	 *             ----Route 路由信息 ID 路由ID name 路由名称 cell 活动
	 * 
	 *             ------ActDef 活动定义信息 ID 定义ID Name 活动名称 cell 部门
	 *             --------Depetment 部门信息 ID 部门ID Name 部门名称 cell 员工
	 *             -----------Staff 员工信息 ID 员工ID Name 员工姓名 cell null
	 */
	@SuppressWarnings("unused")
	public List<SmObjInfo> GetStaffByRoutes(Wfd_Route wfd_Route, String actinstid) throws Exception {
		List<SmObjInfo> list = null;
		if (wfd_Route != null) {
			// 遍历集合根据路由找到所属的活动定义
			list = new ArrayList<SmObjInfo>();
			// 路由信息
			// 添加活动信息
			String nextActivedef = wfd_Route.getNext_Actdef_Id();
			if (nextActivedef != null && nextActivedef != "") {
				SmObjInfo ActdefInfo = new SmObjInfo();//

				Wfd_Actdef actdef = smActDef.GetActDefByID(wfd_Route.getActdef_Id());
				int actdef_type = Integer.parseInt(actdef.getActdef_Type());
				Wfd_Actdef nextActdef = smActDef.GetActDefByID(nextActivedef);
				if (nextActdef != null) {

					SmObjInfo RouteInfo = new SmObjInfo();

					RouteInfo.setName(wfd_Route.getRoute_Name());
					RouteInfo.setDesc("Route");

					List<SmObjInfo> ActdefInfos = null;
					// 判断下一个活动是什么类型 1.交互活动 2、条件活动 3并发活动 4 汇聚活动
					// 自动活动
					// 自动跳过寻找下一级
					// 自动互动正常处理只有在转出的时候在自动转出一遍
					if (actdef_type == WFConst.ActDef_Type.ConditionAnd.value) {// 并发活动
						// 获取所有并发路由
						RouteInfo.setConfirm(WFConst.ActDef_Type.ConditionAnd.value + "");
						List<Wfd_Route> nextRoutes = smRouteDef.GetAllConcurrentRoutesByActdef(actdef);
						if (nextRoutes != null && nextRoutes.size() > 0) {
							// 并发活动的路由可以认为是一个路由发给多个活动
							List<SmObjInfo> concurrenInfos = new ArrayList<SmObjInfo>();
							for (Wfd_Route wfd_Route2 : nextRoutes) {
								Wfd_Actdef concurrent = smActDef.GetActDefByID(wfd_Route2.getNext_Actdef_Id());
								if (concurrent != null) {
									SmObjInfo info = GetNextStaffInfoByRoute(concurrent, actinstid);
									info.setRouteid(wfd_Route2.getRoute_Id());
									concurrenInfos.add(info);
								}
							}
							RouteInfo.setChildren(concurrenInfos);

							list.add(RouteInfo);// 一个路由多个活动
						}
					} else if (actdef_type == WFConst.ActDef_Type.ConditionOr.value) {// 条件分支活动
						// 多个路由
						Wfd_Actdef conditionorActdef = smActDef.GetActDefByID(wfd_Route.getNext_Actdef_Id());
						/*
						 * List<Wfd_Route> routes =
						 * smRouteDef.GetNextRoute(conditionorActdef.
						 * getActdef_Id()); if (routes != null && routes.size()
						 * > 0) { for (Wfd_Route wfd_Route2 : routes) {
						 * RouteInfo = new SmObjInfo();
						 * RouteInfo.setID(wfd_Route2.getRoute_Id());
						 * RouteInfo.setName(wfd_Route2.getRoute_Name());
						 * ActdefInfos = new ArrayList<SmObjInfo>(); Wfd_Actdef
						 * nextActdef1 =
						 * smActDef.GetActDefByID(wfd_Route2.getNext_Actdef_Id()
						 * );
						 * ActdefInfos.add(GetNextStaffInfoByRoute(nextActdef1,
						 * actinstid)); RouteInfo.setChildren(ActdefInfos);
						 * list.add(RouteInfo); } }
						 */
						RouteInfo = new SmObjInfo();
						RouteInfo.setID(wfd_Route.getRoute_Id());
						RouteInfo.setName(wfd_Route.getRoute_Name());
						RouteInfo.setDesc("Route");
						ActdefInfos = new ArrayList<SmObjInfo>();
						Wfd_Actdef nextActdef1 = smActDef.GetActDefByID(wfd_Route.getNext_Actdef_Id());
						SmObjInfo inf = GetNextStaffInfoByRoute(nextActdef1, actinstid);
						inf.setRouteid(wfd_Route.getRoute_Id());
						ActdefInfos.add(inf);
						RouteInfo.setChildren(ActdefInfos);
						RouteInfo.setRouteid(wfd_Route.getRoute_Id());
						RouteInfo.setConfirm(WFConst.ActDef_Type.ConditionOr.value + "");
						list.add(RouteInfo);
					} else if (actdef_type == WFConst.ActDef_Type.AllDoAct.value) {// 协办活动

					} else {// 一般交互互动
						RouteInfo.setConfirm(WFConst.ActDef_Type.Activity.value + "");
						RouteInfo.setID(wfd_Route.getRoute_Id());
						ActdefInfos = new ArrayList<SmObjInfo>();
						ActdefInfos.add(GetNextStaffInfoByRoute(nextActdef, actinstid));
						RouteInfo.setChildren(ActdefInfos);
						list.add(RouteInfo);
					}

				}

			}
		}
		return list;
	}

	/**
	 * 通过路由集合获取下一级可执行角色 SmObjInfo 第一级保存路由信息 cell保存可执行人员列表 这个列表的信息层级 ------ActDef
	 * 活动定义信息 ID 定义ID Name 活动名称 cell 部门 --------Depetment 部门信息 ID 部门ID Name 部门名称
	 * cell 员工 -----------Staff 员工信息 ID 员工ID Name 员工姓名 cell null
	 * 
	 * @throws Exception
	 */
	public SmObjInfo GetNextStaffInfoByRoute(Wfd_Actdef nextActdef, String actinstid) throws Exception {
		SmObjInfo ActdefInfo = new SmObjInfo();//
		if (nextActdef != null) {
			ActdefInfo.setID(nextActdef.getActdef_Id());
			ActdefInfo.setName(nextActdef.getActdef_Name());
			ActdefInfo.setDesc("ActDef");
			// 判断是否是关联活动 “0” 代表没有关联活动
			String glhd = nextActdef.getDeal_Type();
			Wfi_ActInst actinst = smActInst.GetActInst(actinstid);
			if (glhd != null && !glhd.equals("0") && !glhd.equals("")) {
				SmActInfo actInfo = smActInst.GetActInfo(actinstid);
				if (actInfo != null) {
					String proinst_idString = actInfo.getProInst_ID();
					if (proinst_idString != null && !proinst_idString.equals("")) {
						Wfi_ActInst inst = smActInst.GetBackActInst(glhd, proinst_idString);
						if (inst != null) {
							List<SmObjInfo> list = new ArrayList<SmObjInfo>();
							SmObjInfo info = new SmObjInfo();
							info.setDesc("Staff");
							info.setID(inst.getStaff_Id());
							info.setName(inst.getStaff_Name());
							list.add(info);
							ActdefInfo.setChildren(list);
						}
					}

				}
			} else {
				if (nextActdef.getPassover_Type() == WFConst.PassOverType.Role.value) {
					List<SmObjInfo> staffInfo = GetStaffByActDef(nextActdef, actinstid);
					if (null != staffInfo && staffInfo.size() > 0) {
						SmObjInfo info = staffInfo.get(0);
						List<SmObjInfo> staffList = info.getChildren();
						if (staffList.size() > 0) {
							ActdefInfo.setChildren(GetRoleInfoByactdefid(nextActdef));
						} else {
							return null;
						}
					}
				} else {
					ActdefInfo.setChildren(GetStaffByActDef(nextActdef, actinstid));
				}

			}

		}

		return ActdefInfo;
	}

	/*
	 * 关联活动，获取关联活动的办理人员
	 */
	public SmObjInfo getDealStaffByactdefID(String actdefid) {
		if (actdefid != null && !"".equals(actdefid)) {

		}
		return null;
	}

	/**
	 * 通过交互活动获取执行人员 * 这个列表的信息层级 --------Depetment 部门信息 ID 部门ID Name 部门名称 cell 员工
	 * -----------Staff 员工信息 ID 员工ID Name 员工姓名 cell null
	 */

	public List<SmObjInfo> GetStaffByActDef(Wfd_Actdef actdef, String actinstid) {
		List<SmObjInfo> list = new ArrayList<SmObjInfo>();
		if (actdef != null) {
			actdef.getActdef_Dept_Id();
			SmObjInfo deptInfo = new SmObjInfo();
			deptInfo.setID(actdef.getActdef_Dept_Id());
			deptInfo.setName(actdef.getActdef_Dept());
			deptInfo.setDesc("Dept");
			if (actdef.getPassover_Type() == WFConst.PassOverType.Dept.value) {
				Wfi_ProInst proinst = smProInst.GetProInstByActInstId(actinstid);
				String area = null != proinst.getAreaCode() ? proinst.getAreaCode()
						: GetAreaCode(proinst.getStaff_Id());
				List<User> users = getUserByDepidAndAreacode(actdef.getActdef_Dept_Id(), area);
				if (null == users || users.size() < 1) {
					return null;
				}
			} else {
				List<SmObjInfo> userInfo = GetActStaffByActDefID(actdef.getActdef_Id(), actinstid);
				if (null == userInfo || userInfo.size() < 1) {
					return null;
				}
				deptInfo.setChildren(userInfo);
			}

			list.add(deptInfo);
		}
		return list;
	}

	/* 获取上一步的驳回人员 */
	public List<SmObjInfo> GetPassbackStaffByActDef(Wfd_Actdef actdef, String actinstid) {
		List<SmObjInfo> list = new ArrayList<SmObjInfo>();
		if (actdef != null) {
			SmObjInfo deptInfo = new SmObjInfo();
			deptInfo.setID(actdef.getActdef_Dept_Id());
			deptInfo.setName(actdef.getActdef_Dept());
			deptInfo.setDesc("Dept");
			if (actdef.getPassover_Type() == WFConst.PassOverType.Dept.value) {

			} else {
				SmObjInfo info = new SmObjInfo();
				List<SmObjInfo> stafflist = new ArrayList<SmObjInfo>();
				Wfi_Route route = smRouteInst.GetProActDefByActinstID(actinstid);
				if (route != null) {
					Wfi_ActInst inst2 = smActInst.GetActInst(route.getFromactinst_Id());
					info.setID(inst2.getStaff_Id());
					info.setName(inst2.getStaff_Name());
					info.setDesc("Staff");
					stafflist.add(info);
					deptInfo.setChildren(stafflist);
				}
			}
			list.add(deptInfo);
		}
		return list;
	}

	/**
	 * 通过活动定义ID获取活动执行人员集合
	 **/
	public List<SmObjInfo> GetActStaffByActDefID(String actdefid, String actinstid) {
		String roleid = smActDef.GetRoleByActDefId(actdefid);
		if (roleid != null && roleid != "") {
			// 根据执行角色获取可执行人员
			// 需要考虑扩展字段转出范围是否有值
			Wfd_Actdef actdef = smActDef.GetActDefByID(actdefid);
			Wfi_ProInst wfi_proinst = smProInst.GetProInstByActInstId(actinstid);
			// 受理人员的行政区编码
			String acceptorid = null;
			String area = GetCurrentAreaCode();
			if (wfi_proinst != null) {
				acceptorid = wfi_proinst.getStaff_Id();
			}
			if (acceptorid != null && !acceptorid.equals("")) {
				User user = userService.findById(acceptorid);
				area = user.getAreaCode();
			}
			String areaCode = smProInst.GetProInstByActInstId(actinstid).getAreaCode();
			if (areaCode != null && !areaCode.isEmpty()) {
				area = areaCode;
			}
			if (actdef != null) {
				String turnoutrange = actdef.getTurnOutRange();
				if (turnoutrange != null && !turnoutrange.trim().equals("")) {
					if (turnoutrange.equals("0")) {
						turnoutrange = area.substring(0, 2) + "0000";
					} else if (turnoutrange.equals("1")) {
						turnoutrange = area.substring(0, 4) + "00";
					} else {
						turnoutrange = area;
					}
					return smRoleDef.GetStaffByRoleID(roleid, turnoutrange);
				}
			}
			return smRoleDef.GetStaffByRoleID(roleid);
		}
		return null;
	}

	/*
	 * 获取流程可以转出的角色
	 */
	public List<SmObjInfo> GetRoleInfoByactdefid(Wfd_Actdef actdef) {
		List<SmObjInfo> list = null;
		if (actdef != null) {
			list = new ArrayList<SmObjInfo>();
			SmObjInfo info = new SmObjInfo();
			info.setID(actdef.getRole_Id());
			info.setName(actdef.getRole_Name());
			info.setDesc("Role");
			list.add(info);
		}
		return list;

	}

	/**
	 * 单步驳回， 通过活动实例找到驳回上一步的人员列表 -----前置活动定义 ---------科室 -----------员工
	 * 
	 * @param actinstid
	 *            String 活动实例ID
	 * @param type
	 *            int 类型 1、 直接驳回 2 跳跃驳回
	 **/

	public List<SmObjInfo> GetOverRuleStaffbyActinst(String actinstid, int type) {
		List<SmObjInfo> list = new ArrayList<SmObjInfo>();
		if (!"".equals(actinstid)) {
			Wfi_ActInst actInst = smActInst.GetActInst(actinstid);

			List<Wfi_Route> routes = smRouteInst.GetRouteInstByProinstid(actInst.getProinst_Id(), type);
			Wfd_Actdef actdef = null;
			SmObjInfo info = null;
			if (routes != null && routes.size() > 0) {
				// 获取前置活动实例
				Wfi_Route currentRoute = null;
				for (Wfi_Route route : routes) {
					if (route.getNext_Actdef_Id().equals(actInst.getActdef_Id())
							&& route.getInstance_Type().equals(WFConst.Instance_Type.Instance_Normal.value)) {
						currentRoute = route;
						break;
					}
				}
				//判断路由中是否有驳回约束不能驳回的,移除掉
				Wfi_ProInst proinst = smProInst.GetProInstByActInstId(actinstid);
				Map<String, String> map = smRouteDef.ValiteRoute_Reject(currentRoute.getRoute_Id(), proinst.getFile_Number());
				if (map != null) {
					if (!("1").equals(map.get("BM"))) {
						info = new SmObjInfo();
						info.setID("3");
						info.setName(currentRoute.getRoute_Name());
						info.setDesc("当前流程已设置驳回限制，不允许驳回");
						list.add(info);
						return list;
					}
				}

				// 直接驳回
				actdef = smActDef.GetActDefByID(currentRoute.getActdef_Id());
				Wfi_ActInst newactInst = smActInst.GetActInst(currentRoute.getFromactinst_Id());
				info = new SmObjInfo();
				if (actdef != null) {
					info.setID(actdef.getActdef_Id());
					info.setName(actdef.getActdef_Name());
					info.setChildren(GetOverRuleDeptAndStaff(newactInst, actdef));
					info.setDesc("ActDef");
					list.add(info);
				}
			}
			// List<Wfi_ActInst>
			// aclist=smActInst.getActInstForCommonActdef(actinstid);
			// if(!aclist.isEmpty()){
			// actinstid=aclist.get(0).getActinst_Id();
			// }
			// List<Wfi_ActInst> actinstList= new ArrayList<Wfi_ActInst>();
			// actinstList=GetActinstbyActinst(actinstid,actinstList);
			// for(Wfi_ActInst actinst:actinstList){
			// Wfd_Actdef actdef=smActDef.GetActDefByID(actinst.getActdef_Id());
			// SmObjInfo info = new SmObjInfo();
			// info.setID(actinst.getActdef_Id());
			// info.setName(actdef.getActdef_Name());
			// info.setDesc("ActDef");
			// info.setChildren(GetOverRuleDeptAndStaff(actinst, actdef));
			// list.add(info);
			// }
		}

		return list;
	}

	/*
	 * 根据actinstid找前置环节
	 */
	// public List<Wfi_ActInst> GetActinstbyActinst(String
	// actinstid,List<Wfi_ActInst> list) {
	// if (!"".equals(actinstid)) {
	// Wfi_ActInst actInst = smActInst.GetActInst(actinstid);
	// List<Wfi_Route> routes = smRouteInst.GetRouteInstsByActinstID(actinstid);
	// if (routes != null && routes.size() > 0) {
	// for (Wfi_Route route : routes) {
	// Wfi_ActInst inst=smActInst.GetActInst(route.getFromactinst_Id());
	// Wfd_Actdef def=smActDef.GetActDefByID(inst.getActdef_Id());
	// if(def==null){
	// GetActinstbyActinst(inst.getActinst_Id(),list);
	// }else{
	// list.add(inst);
	// }
	// }
	// }
	// }
	// return list;
	// }
	/*
	 * 跳跃驳回
	 */
	public List<SmObjInfo> GetJumpOverRuleStaffbyActinst(String actinstid) {
		List<SmObjInfo> list = new ArrayList<SmObjInfo>();
		if (!"".equals(actinstid)) {
			Wfi_ActInst actInst = smActInst.GetActInst(actinstid);
			// 获取当前流程实例
			Wfi_ProInst proinst = smProInst.GetProInstByActInstId(actinstid);
			// 获取流程的实际路由按照actdef和proinst分组取最新的路由，过滤掉当前环节的前置环节
			// 或当前环节的后续环节定义；
			Wfd_Actdef wfda = smActInst.GetActDef(actinstid);
			String sql2 = "select * from " + Common.WORKFLOWDB + "wfd_route where prodef_id = '" + wfda.getProdef_Id()
					+ "'" + " start  with next_actdef_id = '" + wfda.getActdef_Id() + "'\n"
					+ " connect by prior next_actdef_id =  actdef_id ";
			List<Wfd_Route> listroutedef = dao.getDataList(Wfd_Route.class, sql2);

			String sql = "select * from (\n"
					+ "select row_number() over (partition by proinst_id, actdef_id  order BY creat_time desc) as ord,"
					+ "t.* from " + Common.WORKFLOWDB + "wfi_route t " + "where proinst_id = '"
					+ proinst.getProinst_Id() + "' and next_actinst_id <>'" + actinstid
					+ "' and instance_type=1 and actdef_id<>'" + wfda.getActdef_Id() + "' )\n" + "where ord =1";
			List<Wfi_Route> routelist = dao.getDataList(Wfi_Route.class, sql);
			List<Wfd_Actdef> actdefs = smActDef.GetAllPreActdefByActdefID(actInst.getActdef_Id(), null);
			// 分支1环节走完被驳回之后改走分支2 再次驳回以上查询sql就会出现重复的情况；路由根据next_actdef_id去除重复;
			// 缺失的前置活动环节
			List<Wfd_Actdef> misactdefs = new ArrayList<Wfd_Actdef>();
			// 判断前置环节跟实际产生路由质检的差异，补充缺少的实际环节
			if (routelist != null && routelist.size() > 0) {
				// 循环路由
				for (int i = 0; i < routelist.size(); i++) {

					boolean flag = false;
					for (int j = 0; j < actdefs.size(); j++) {
						if (routelist.get(i).getNext_Actdef_Id().equals(actdefs.get(j).getActdef_Id())) {
							// routelist.remove(i);
							flag = true;
							break;
						}
					}
					if (!flag) {
						Wfd_Actdef missActdef = smActDef.GetActDefByID(routelist.get(i).getNext_Actdef_Id());
						if (missActdef != null) {
							misactdefs.add(missActdef);
						}
					}
				}
			}
			// 合并前置活动
			int i = 0;
			while (i < misactdefs.size()) {
				actdefs.add(misactdefs.get(i));
				i++;
			}
			// 去除重复，
			for (int k = 0; k < actdefs.size(); k++) {
				boolean in = false;
				for (Wfd_Route route : listroutedef) {
					if (actdefs.get(k).getActdef_Id().equals(route.getNext_Actdef_Id())) {
						in = true;
						break;
					}
				}
				if (in) {
					actdefs.remove(k);
				}
			}
			if (actdefs != null && actdefs.size() > 0) {
				actdefs.add(0, smActInst.GetActDef(actinstid));
				for (Wfd_Actdef wfd_Actdef : actdefs) {

					// 检测活动定义是否是拦截活动
					if (wfd_Actdef.getRejectpassback() == null || wfd_Actdef.getRejectpassback() == 0) {
						List<Wfi_ActInst> actInsts = smActInst.QueryActInst(wfd_Actdef.getActdef_Id(),
								actInst.getProinst_Id());
						if (actInsts != null && actInsts.size() > 0) {
							Wfi_ActInst currentActInst = actInsts.get(0);
							if (currentActInst != null && !currentActInst.getActdef_Type()
									.equals(WFConst.ActDef_Type.ProcessStart.value + "")) {
								List<SmObjInfo> resultInfos = GetOverRuleStaffbyActinst(currentActInst.getActinst_Id(),
										1);
								if("3".equals(resultInfos.get(0).getID())){
									//驳回控制限制，不允许驳回
									continue;
								} else {
									list.add(resultInfos.get(0));
								}
							}
						}
					} else {
						break;
					}

				}
			}
		}
		return list;
	}

	public List<SmObjInfo> GetOverRuleStaffbyActinst_LSF(String actinstid, int type) {
		List<SmObjInfo> list = new ArrayList<SmObjInfo>();
		if (!"".equals(actinstid)) {
			List<Wfi_Route> routes = smRouteInst.GetRouteInstsByActinstID(actinstid);
			if (routes != null && routes.size() > 0) {
				type = routes.get(0).getInstance_Type();
				if (type == WFConst.Instance_Type.Instance_Normal.value) {
					return GetOverRuleStaffbyActinst(actinstid, type);
				} else {
					// 获取前置活动实例,就是这块出问题了，当前活动如果是从后面的环节驳回回来的，那么当前活动实例的前置活动就是后面的活动了，而不是前面的活动
					// 首先找到当前的活动定义
					// 找到最后一个到当前活动定义的正常或者转出到驳回的路由
					Wfi_ActInst inst = smActInst.GetActInst(actinstid);
					String sql = "select * from  " + Common.WORKFLOWDB
							+ "Wfi_actinst where proinst_id=''{0}'' and actdef_id=''{1}'' order by actinst_end asc";
					// String sql =
					// "SELECT ROUTE.Fromactinst_Id as ACTINSTID,DEF.ACTDEF_ID
					// AS ACTDEFID FROM BDC_WORKFLOW.WFI_ROUTE ROUTE LEFT JOIN
					// BDC_WORKFLOW.WFI_ACTINST INST ON
					// ROUTE.NEXT_ACTINST_ID=INST.ACTINST_ID LEFT JOIN
					// BDC_WORKFLOW.WFD_ACTDEF DEF ON
					// INST.ACTDEF_ID=DEF.ACTDEF_ID LEFT JOIN
					// BDC_WORKFLOW.WFI_ACTINST INST2 ON
					// DEF.ACTDEF_ID=INST2.ACTDEF_ID WHERE
					// INST2.ACTINST_ID=''{0}'' AND
					// ROUTE.NEXT_ACTINST_ID<>''{0}'' AND ROUTE.INSTANCE_TYPE=1
					// ORDER BY CREAT_TIME DESC";
					sql = MessageFormat.format(sql, inst.getProinst_Id(), inst.getActdef_Id());
					@SuppressWarnings("rawtypes")
					List<Map> maps = dao.getDataListByFullSql(sql);
					if (maps != null && maps.size() > 0) {
						@SuppressWarnings("rawtypes")
						Map map = maps.get(0);
						if (map.containsKey("ACTINST_ID")) {
							// 活动定义ID
							String instid = (String) map.get("ACTINST_ID");
							return GetOverRuleStaffbyActinst(actinstid, 1);
						}
					}
				}

			}
		}
		return list;
	}

	/*
	 * 获取驳回部门和员工
	 * 
	 * *
	 */

	public List<SmObjInfo> GetOverRuleDeptAndStaff(Wfi_ActInst actInst, Wfd_Actdef actdef) {
		List<SmObjInfo> deptList = new ArrayList<SmObjInfo>();
		SmObjInfo info = new SmObjInfo();
		info.setID(actdef.getActdef_Dept_Id());
		info.setName(actdef.getActdef_Dept());
		info.setDesc("Dept");
		List<SmObjInfo> listStaffInfos = new ArrayList<SmObjInfo>();

		SmObjInfo Staff = new SmObjInfo();
		Staff.setID(actInst.getStaff_Id());
		Staff.setName(GetStaffName(actInst.getStaff_Id()));
		Staff.setDesc("Staff");
		listStaffInfos.add(Staff);
		info.setChildren(listStaffInfos);
		deptList.add(info);
		return deptList;

	}

	/**
	 * 通过活动ID获取可以转办的员工列表
	 * 
	 */
	@SuppressWarnings("unused")
	public List<SmObjInfo> GetTurnStaffByActInstID(String actinstid) {
		List<SmObjInfo> list = new ArrayList<SmObjInfo>();
		String CurrentStaffID = getCurrentWorkStaffID();
		Wfd_Actdef actdef = smActInst.GetActDef(actinstid);
		SmObjInfo deptInfo = new SmObjInfo();
		deptInfo.setID(actdef.getActdef_Dept_Id());
		deptInfo.setName(actdef.getActdef_Dept());
		deptInfo.setDesc("Dept");
		deptInfo.setChildren(smActInst.GetTurnStaff(actinstid));
		list.add(deptInfo);
		return list;

	}

	/**
	 * @作者：jhx
	 * @时间:2016-08-21 并发活动之后的聚合活动的驳回使用跳跃驳回，驳回至并发活动的上一个环节
	 * 
	 */
	public List<SmObjInfo> GetJumpOverRuleStaffbyActinst2(String actinstid) {
		List<SmObjInfo> list = new ArrayList<SmObjInfo>();
		if (!"".equals(actinstid)) {
			Wfi_ActInst actInst = smActInst.GetActInst(actinstid);

			List<Wfd_Actdef> actdefs = smActDef.GetAllPreActdefByActdefID(actInst.getActdef_Id(), null);
			if (actdefs != null && actdefs.size() > 0) {
				// actdefs.add(0, smActInst.GetActDef(actinstid));
				for (Wfd_Actdef wfd_Actdef : actdefs) {
					// 检测活动定义是否是拦截活动
					if (wfd_Actdef.getRejectpassback() == null || wfd_Actdef.getRejectpassback() == 0) {
						List<Wfi_ActInst> actInsts = smActInst.QueryActInst(wfd_Actdef.getActdef_Id(),
								actInst.getProinst_Id());
						if (actInsts != null && actInsts.size() > 0) {
							Wfi_ActInst currentActInst = actInsts.get(0);
							if (currentActInst != null && !currentActInst.getActdef_Type()
									.equals(WFConst.ActDef_Type.ProcessStart.value + "")) {
								List<SmObjInfo> resultInfos = GetOverRuleStaffbyActinst(currentActInst.getActinst_Id(),
										1);
								list.add(resultInfos.get(0));
							}
						}
					} else {
						break;
					}

				}
			}
		}
		return list;
	}

	/*
	 * 通过驳回的路由，获取转出到驳回的相关信息
	 * 
	 * @param route 驳回的路由 *
	 */
	public SmObjInfo GetSkipPassBaskInfo(Wfi_Route route) {
		SmObjInfo routeinfo = new SmObjInfo();
		// 获取下一个活动信息
		Wfd_Actdef actdef = dao.get(Wfd_Actdef.class, route.getActdef_Id());
		if (actdef != null) {
			// 虚拟路由信息
			routeinfo.setName("到驳回：" + actdef.getActdef_Name());
			routeinfo.setID("-1");
			routeinfo.setDesc("Route");
			routeinfo.setConfirm(WFConst.ActDef_Type.Activity.value + "");
			List<SmObjInfo> roulist = new ArrayList<SmObjInfo>();

			SmObjInfo ActdefInfo = new SmObjInfo();//
			ActdefInfo.setName(actdef.getActdef_Name());
			ActdefInfo.setID(actdef.getActdef_Id());
			ActdefInfo.setDesc("ActDef");
			Wfi_ActInst actInst = dao.get(Wfi_ActInst.class, route.getFromactinst_Id());
			if (actInst != null) {
				List<SmObjInfo> list = new ArrayList<SmObjInfo>();
				SmObjInfo staffinfo = new SmObjInfo();
				staffinfo.setDesc("Staff");
				staffinfo.setID(actInst.getStaff_Id());
				staffinfo.setName(actInst.getStaff_Name());
				list.add(staffinfo);
				ActdefInfo.setChildren(list);
			}
			roulist.add(ActdefInfo);
			routeinfo.setChildren(roulist);
		}
		return routeinfo;
	}

	public String GetActStaffs(String actinst) {
		String Result = "";
		if (actinst != null && !actinst.equals("")) {
			List<Wfi_ActInstStaff> actstaffs = dao.getDataList(Wfi_ActInstStaff.class,
					Common.WORKFLOWDB + "Wfi_ActInstStaff", "actinst_id='" + actinst + "'");
			if (actstaffs != null && actstaffs.size() > 0) {
				for (Wfi_ActInstStaff staff : actstaffs) {
					Result += staff.getStaff_Name() + ",";
				}
				Result = Result.substring(0, Result.length() - 1);
			}
		}
		return Result;
	}

	/**
	 * 根据departmentid和areacode获取用户
	 */
	public List<User> getUserByDepidAndAreacode(String depid, String areacode) {
		List<User> users = userService.findUserByDepartmentId(depid);
		if (null != users && users.size() > 0) {
			Iterator<User> it = users.iterator();
			while (it.hasNext()) {
				User temp = it.next();
				if (!temp.getAreaCode().equals(areacode)) {
					it.remove();
				}
			}
		}
		return users;
	}
}
