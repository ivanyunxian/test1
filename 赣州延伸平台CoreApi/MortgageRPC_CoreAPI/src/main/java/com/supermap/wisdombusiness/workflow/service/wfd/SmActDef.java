package com.supermap.wisdombusiness.workflow.service.wfd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.service.RoleService;
import com.supermap.wisdombusiness.framework.service.UserService;
import com.supermap.wisdombusiness.utility.StringHelper;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfd_Actdef;
import com.supermap.wisdombusiness.workflow.model.Wfd_Route;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInstStaff;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_Route;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.WFConst;
import com.supermap.wisdombusiness.workflow.service.relationmaintain.impl.RecordsMaintian;
import com.supermap.wisdombusiness.workflow.service.wfi.SmActInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmRouteInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;

@Component("smActDef")
public class SmActDef {

	@Autowired
	private CommonDao commonDao;
	@Autowired
	private SmRouteInst smRouteInst;
	@Autowired
	private SmProDef smProDef;
	@Autowired
	private SmActInst smActInst;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private SmProInst smProInst;
	
	@Autowired
	private RecordsMaintian recordsMaintian;
	/**
	 * 判断一个活动是否协办
	 * 
	 * @param actdef
	 *            Wfd_Actdef 活动定义
	 * */
	public boolean IsAllDoActInst(Wfd_Actdef actdef) {
		if (actdef.getActdef_Type() == (WFConst.ActDef_Type.AllDoAct.value + "")) {

			return true;
		} else {
			return false;
		}

	}

	/**
	 * 判断是否是一个聚合活动
	 * 
	 * @param actdef
	 *            Wfd_Actdef 活动定义
	 * */
	public boolean IsIntercurrentActInst(Wfd_Actdef actdef) {
		if (actdef.getActdef_Type() == (WFConst.ActDef_Type.ActivityMerge.value + "")) {

			return true;
		} else {
			return false;
		}
	}
	// 判断是否可以转出
	public boolean IsCanPassActInst(Wfd_Actdef actdef) {

		if (actdef.getActdef_Type() == (WFConst.ActDef_Type.CanPass.value + "")) {

			return true;
		} else {
			return false;
		}
	}
	public boolean IsAllDoActIactdefnst(Wfd_Actdef actdef) {

		return false;
	}
	/**
	 * 判断是否是起始活动
	 * 
	 * @param actdef
	 *            Wfd_Actdef 活动定义实体
	 * 
	 * */
	public boolean IsStartAct(Wfd_Actdef actdef) {
		// 还有一种判断方式，可以通过定义节点的类型判断
		if (actdef != null) {
			if (actdef.getActdef_Type().equals(
					WFConst.ActDef_Type.ProcessStart.value + "")) {
				return true;
			}
		}
		return false;
	}
   
	/**
	 * 转出到指定ID的活动的路由集合
	 * 
	 * @param actDef_ID
	 *            活动定义ID
	 * */
	@SuppressWarnings("unused")
	private String[] GetRouteDefIDsTo(String actDef_ID) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("select * from " + Common.WORKFLOWDB
				+ "Wfd_Route where next_actdef_id ='");
		sBuilder.append(actDef_ID);
		sBuilder.append("'");
		List<Wfd_Route> routes = commonDao.getDataList(Wfd_Route.class,
				sBuilder.toString());
		if (routes == null || routes.size() == 0) {
			return null;
		} else {
			String[] routeIDs = new String[routes.size()];
			for (int i = 0; i < routes.size(); i++) {
				routeIDs[i] = routes.get(i).getNext_Actdef_Id();
			}
			return routeIDs;
		}
	}
	/**
	 * 根据活动定义ID获取执行角色
	 * 
	 * @param ActDefid
	 *            String 活动定义ID
	 * */
	public String GetRoleByActDefId(String ActDefid) {
		String roleIDString = null;
		if (ActDefid != null && ActDefid != "") {
			Wfd_Actdef actdef = commonDao.get(Wfd_Actdef.class, ActDefid);
			if (actdef != null) {
				roleIDString = actdef.getRole_Id();
			}
		}
		return roleIDString;
	}

	/**
	 * 通过活动定义ID 和路由ID获取下一级活动定义
	 * 
	 * */
	public Wfd_Actdef GetNextActDef(String actdefid, String routrid) {
		Wfd_Actdef wfd_Actdef = null;
		if (!actdefid.equals("") && !routrid.equals("")) {
			StringBuilder abBuilder = new StringBuilder();
			abBuilder.append("select * from " + Common.WORKFLOWDB
					+ "Wfd_Route where ACTDEF_ID='");
			abBuilder.append(actdefid);
			abBuilder.append("'");
			abBuilder.append(" and ROUTE_ID='");
			abBuilder.append(routrid);
			abBuilder.append("'");
			List<Wfd_Route> drouteRoute = commonDao.getDataList(
					Wfd_Route.class, abBuilder.toString());
			if (drouteRoute != null && drouteRoute.size() > 0) {
				Wfd_Route route = drouteRoute.get(0);
				wfd_Actdef = commonDao.get(Wfd_Actdef.class,
						route.getNext_Actdef_Id());
			}

		}
		return wfd_Actdef;
	}
	public Wfd_Actdef GetNextActDefByactinst(String actinst) {
		Wfd_Actdef wfd_Actdef = null;
		wfd_Actdef=smActInst.GetActDef(actinst);
		String actdefid=wfd_Actdef.getActdef_Id();
		if (!actdefid.equals("")) {
			StringBuilder abBuilder = new StringBuilder();
			abBuilder.append("select * from " + Common.WORKFLOWDB
					+ "Wfd_Route where ACTDEF_ID='");
			abBuilder.append(actdefid);
			abBuilder.append("'");
			List<Wfd_Route> drouteRoute = commonDao.getDataList(
					Wfd_Route.class, abBuilder.toString());
			if (drouteRoute != null && drouteRoute.size() > 0) {
				Wfd_Route route = drouteRoute.get(0);
				wfd_Actdef = commonDao.get(Wfd_Actdef.class,
						route.getNext_Actdef_Id());
			}
		}
		return wfd_Actdef;
	}
	public Wfd_Actdef GetNextActDef(String routrid) {
		Wfd_Actdef wfd_Actdef = null;

		StringBuilder abBuilder = new StringBuilder();
		abBuilder.append("select * from " + Common.WORKFLOWDB
				+ "Wfd_Route where");
		abBuilder.append("  ROUTE_ID='");
		abBuilder.append(routrid);
		abBuilder.append("'");
		List<Wfd_Route> drouteRoute = commonDao.getDataList(Wfd_Route.class,
				abBuilder.toString());
		if (drouteRoute != null && drouteRoute.size() > 0) {
			Wfd_Route route = drouteRoute.get(0);
			wfd_Actdef = commonDao.get(Wfd_Actdef.class,
					route.getNext_Actdef_Id());
		}
		return wfd_Actdef;
	}
	/**
	 * 判断是否可以直接驳回
	 * 
	 * @param actdef
	 *            Wfd_Actdef 活动定义
	 * */
	public boolean CanOverRuleActDef(Wfd_Actdef actdef) {
		if (actdef != null) {
			if (actdef.getCanrejectornot() == WFConst.SmActInstOverRuleReturnType.CannotOverRuleActDef.value) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据流程定义ID获取流程的所有活动
	 * 
	 * @param ProdefID
	 *            String 流程定义ID
	 */
	public List<Wfd_Actdef> GetActDefByProdefID(String ProdefID) {
		StringBuilder WhereSql = new StringBuilder();
		WhereSql.append("select * from ");
		WhereSql.append(Common.WORKFLOWDB);
		WhereSql.append("Wfd_Actdef where ");
		WhereSql.append(" PRODEF_ID='");
		WhereSql.append(ProdefID);
		WhereSql.append("'");
		return commonDao.getDataList(Wfd_Actdef.class,  WhereSql.toString());
	}
	/*获取活动列表，但是不包括本身*/
	public List<Wfd_Actdef> GetActDefByProdefIDNoSelf(String ProdefID,String actdefid) {
		StringBuilder WhereSql = new StringBuilder();
		WhereSql.append("select * from ");
		WhereSql.append(Common.WORKFLOWDB);
		WhereSql.append("Wfd_Actdef where ");
		WhereSql.append(" PRODEF_ID='");
		WhereSql.append(ProdefID);
		WhereSql.append("' and  actdef_id <> '");
		WhereSql.append(actdefid);
		WhereSql.append("'");
		return commonDao.getDataList(Wfd_Actdef.class,  WhereSql.toString());
	}
	//获取一个流程下有多少交互活动
	public List<Wfd_Actdef> GetActiveActDefByProdefID(String ProdefID) {
		StringBuilder noWhereSql = new StringBuilder();
		noWhereSql.append("PRODEF_ID='");
		noWhereSql.append(ProdefID);
		noWhereSql.append("' and actdef_type in ('");
		noWhereSql.append(WFConst.ActDef_Type.ProcessStart.value);
		noWhereSql.append("','");
		noWhereSql.append(WFConst.ActDef_Type.Activity.value);
		noWhereSql.append("','");
		noWhereSql.append(WFConst.ActDef_Type.ProcessEnd.value);
		noWhereSql.append("')");
		return commonDao.getDataList(Wfd_Actdef.class, noWhereSql.toString());
	}
	//计算当前活动在流程中的进度
	@SuppressWarnings("unused")
	public String GetActDefProcess(String CurActdef_id){
		Wfd_Actdef actdef = GetActDefByID(CurActdef_id);
		if(actdef!=null){
			List<Wfd_Actdef> list= GetActDefByProdefID(actdef.getProdef_Id());
			if(list!=null&&list.size()>0){
				int Count=list.size();
				int CurCount=0;
				for (Wfd_Actdef wfd_Actdef : list) {
					String actdef_typeString=wfd_Actdef.getActdef_Type();
					if(actdef_typeString.equals(WFConst.ActDef_Type.ConditionOr.value)||actdef_typeString.equals(WFConst.ActDef_Type.ActivityMerge.value)	){
						Count--;
					}
					else if(actdef_typeString.equals(WFConst.ActDef_Type.AllDoAct.value)) {
						//就算协办活动有几个，多个协办只当做一个
						
					}
					
				}
				
			}
		}
		
		return "";
	}
	/**
	 * 根据活动定义ID获取活动定义
	 * 
	 * */

	public Wfd_Actdef GetActDefByID(String actdefid) {
		Wfd_Actdef wfd_actdef = commonDao.get(Wfd_Actdef.class, actdefid);
		if(wfd_actdef!=null&&(wfd_actdef.getBtnPermission()==null||wfd_actdef.getBtnPermission().equals(""))){
			List<Map> lists = commonDao.getDataListByFullSql("select BTN_ID from "+Common.WORKFLOWDB+"wfd_btnregiste");
			JSONObject object = new JSONObject();
			if(lists.size()>0){
				for(int i=0;i<lists.size();i++){
					String id = lists.get(i).get("BTN_ID").toString();
					if(id.equals("btnrefresh")||id.equals("btnDelFile")
							||id.equals("btnAddFile")||id.equals("btnAddMoreFile")||
							id.equals("print")){
						object.put(id, 1);
					}else{
						object.put(id, 0);
					}
				}
			}
			wfd_actdef.setBtnPermission(object.toJSONString());
		}
		if(wfd_actdef!=null&&(wfd_actdef.getTurnOutRange()==null||wfd_actdef.getTurnOutRange().equals(""))){
			wfd_actdef.setTurnOutRange("3");
		}
		return wfd_actdef;
	}
	/**
	 * 根据路由ID获取前序活动定义
	 * */
	public Wfd_Actdef GetProActDefByRouteID(String RouteID) {
		Wfd_Actdef actdef = null;
		try {
			Wfd_Route route = commonDao.get(Wfd_Route.class, RouteID);
			String actdefidString = route.getActdef_Id();
			actdef = commonDao.get(Wfd_Actdef.class, actdefidString);
		} catch (Exception e) {
		}
		return actdef;

	}

	/**
	  * 
	  */
	public void UpdateActDef(Wfd_Actdef Actdef) {
		// 暂时写死部门
		if (Actdef.getActdef_Dept_Id().equals("1")) {
			Actdef.setActdef_Dept("受理科");
		} else if (Actdef.getActdef_Dept_Id().equals("2")) {
			Actdef.setActdef_Dept("审核科");
		} else if (Actdef.getActdef_Dept_Id().equals("3")) {
			Actdef.setActdef_Dept("权证科");
		} else if (Actdef.getActdef_Dept_Id().equals("4")) {
			Actdef.setActdef_Dept("发证窗口");
		} else if (Actdef.getActdef_Dept_Id().equals("5")) {
			Actdef.setActdef_Dept("档案室");
		}
		else if (Actdef.getActdef_Dept_Id().equals("6")) {
			Actdef.setActdef_Dept("财务科");
		}
		//需要判断环节定义的时候是否需要级联更新环节实例信息
		recordsMaintian.mainTainFile(Actdef);
		
	}

	/**
	 * 获取一个前置活动定义
	 * 
	 * @param actinstid
	 *            活动实例ID
	 * */
	public Wfd_Actdef GetProActDefByActInstID(String actinstid) {
		Wfd_Actdef actdef = null;
		Wfi_Route routeinst = smRouteInst.GetProActDefByActinstID(actinstid);
		if (routeinst != null) {
			actdef = GetActDefByID(routeinst.getActdef_Id());
		}
		return actdef;
	}

	 

	 /**
	  * 按照流程ID 删除活动
	  * */
	 public void delActdef(String Prodef_id){
		 List<Wfd_Actdef> actdefs=commonDao.getDataList(Wfd_Actdef.class, "select * from "+Common.WORKFLOWDB+"Wfd_Actdef where prodef_id='"+Prodef_id+"'");
		 if(actdefs!=null&&actdefs.size()>0){
			 for (Wfd_Actdef wfd_Actdef : actdefs) {
				 delActDefByActdefid(wfd_Actdef.getActdef_Id());
			}
		 }
	 }
	 
	 public void delActDefByActdefid(String actdef_id){
		 if(actdef_id!=null&&!"".equals(actdef_id)){
			 commonDao.delete(Wfd_Actdef.class,actdef_id);
			 commonDao.flush();
		 }
		
	 }

     /**
      * 获取流程的所有定义活动
      * */
	 public List<Wfd_Actdef> GetALLActDefByProdefID(String prodefid) {
		 return commonDao.getDataList(Wfd_Actdef.class, "select * from "+Common.WORKFLOWDB+"Wfd_Actdef where prodef_id='"+prodefid+"'");
		
	}
    /**
     * 获取一个活动的所有前序定义活动
     * 
     * */
	 
	 public List<Wfd_Actdef> GetAllPreActdefByActdefID(String actdef_id, List<Wfd_Actdef> list){
		if(list==null){
			list=new ArrayList<Wfd_Actdef>();
		}
		 Wfd_Actdef actdef= GetPreActDefByActDefID(actdef_id);
		 if(actdef!=null){
			 list.add(actdef);
			 if(!actdef.getActdef_Type().equals(WFConst.ActDef_Type.ProcessStart.value+"")){
				return GetAllPreActdefByActdefID(actdef.getActdef_Id(),list);
			 }
		 }
		 return list;
	 }
     /*
      * 获取一个活动的前置活动
      * */
	 public Wfd_Actdef GetPreActDefByActDefID(String actdef_id){
		List<Wfd_Route> routes=commonDao.getDataList(Wfd_Route.class, "select * from "+Common.WORKFLOWDB+"Wfd_Route where Next_Actdef_ID='"+actdef_id+"'");
		if(routes!=null&&routes.size()>0){
				Wfd_Route route=routes.get(0);
				return commonDao.get(Wfd_Actdef.class, route.getActdef_Id());
		}
		 return null;
	 }

	 
	 /**
	  * 通过一个当前活动的ID获取一个转出（跳跃驳回）的目标活动定义
	  * */
	 public Wfd_Actdef getActdefBySkipPassActinstID(String actinstid){
		 if(actinstid!=null&& !actinstid.equals("")){
			Wfi_Route route= smRouteInst.GetProActDefByActinstID(actinstid);
			if(route!=null){
				if(route.getInstance_Type()==(WFConst.Instance_Type.Instance_OverRule.value)){
					return commonDao.get(Wfd_Actdef.class,route.getActdef_Id());
				}
			}
		 }
		 return null;
	 }
	 
	 public List<Map> GetActDefName(){
		 String sql="select actdef_name, count(*) from "+Common.WORKFLOWDB+"wfd_actdef group by actdef_name having count(*)>1 order by count(*) desc";
		 return commonDao.getDataListByFullSql(sql);
	 }
	 /**
	  * @author JHX
	  * @DATE 2017-08-01
	  * 查询按钮注册表信息
	  * */
	 public List<Map> getBtnRegiste(){
		 String fullsql = "select BTN_ID,BTN_NAME FROM  "+Common.WORKFLOWDB+"wfd_btnregiste";
		 return commonDao.getDataListByFullSql(fullsql);
	 }
	 /**
	  * @author JHX
	  * @DATE 2017-08-23
	  * 查询环节自定义参数key值信息
	  * */
	 public List<Map> getCommonDicByType(String type){
		 String fullsql = "select NAME,CODE FROM  "+Common.WORKFLOWDB+"T_COMMONDIC";
		 return commonDao.getDataListByFullSql(fullsql);
	 }
}
