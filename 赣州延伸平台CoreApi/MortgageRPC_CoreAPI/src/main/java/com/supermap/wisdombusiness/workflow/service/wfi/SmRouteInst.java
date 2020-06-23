package com.supermap.wisdombusiness.workflow.service.wfi;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfd_Actdef;
import com.supermap.wisdombusiness.workflow.model.Wfd_Route;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_Route;
import com.supermap.wisdombusiness.workflow.model.Wfi_Spyj;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.WFConst;

import net.sf.json.JSONObject;

@Component("smRouteInst")
public class SmRouteInst {

	@Autowired
    private CommonDao commonDao;
	@Autowired
	private SmActInst smActInst;
	/**
	 * 创建转出路由实例
	 * 
	 * @param routeid 路由ID
	 */
	public Wfi_Route CreateRouteInst(String routeid, String actinstid,
			String Proinstid) {

		Wfi_Route route = new Wfi_Route();
		route.setRouteinst_Id(Common.CreatUUID());
		route.setRoute_Id(routeid);
		route.setFromactinst_Id(actinstid);
		route.setProinst_Id(Proinstid);
		route.setRoute_Status(WFConst.Instance_Status.Instance_Passing.value);
		route.setCraet_Time(new Date());
		return route;
	}
	/**
	 * 创建驳回路由实例
	 * 
	 * @param routeid 路由ID
	 */
	public Wfi_Route CreateOverRuleRouteInst(String Proactdefid, String actinstid,
			String Proinstid) {
		Wfi_Route route = new Wfi_Route();
		StringBuilder sbBuilder=new StringBuilder();
		sbBuilder.append("select * from ");
		sbBuilder.append(Common.WORKFLOWDB+"Wfi_Route ");
		sbBuilder.append("where next_actinst_id='");
		sbBuilder.append(actinstid);
		sbBuilder.append("' and actdef_id='");
		sbBuilder.append(Proactdefid);
		sbBuilder.append("'");
		Wfi_Route routeinst=null;
		List<Wfi_Route> wfi_Route=commonDao.getDataList(Wfi_Route.class, sbBuilder.toString());
        if(wfi_Route!=null&&wfi_Route.size()>0){
        	routeinst=wfi_Route.get(0);
        	route.setRoute_Id(routeinst.getRoute_Id());
        }
        commonDao.clear();
        route.setRouteinst_Id(Common.CreatUUID());
		route.setFromactinst_Id(actinstid);
		route.setProinst_Id(Proinstid);
		route.setRoute_Status(WFConst.Instance_Status.Instance_Passing.value);
		route.setInstance_Type(WFConst.Instance_Type.Instance_OverRule.value);
		route.setCraet_Time(new Date());
		route.setNext_Actdef_Id(Proactdefid);
	
		return route;
	}
	/**
     * 向路由实例中添加一个下一个活动实例的指向
     * @param actinstid String 活动实例ID
     * 
     * */
	public void AddRouteInstToActInst(String actinstid) {
		

	}
	/**
	 * 通过活动ID获取驳回路由
	 * */
	public List<Wfi_Route> GetRouteInstsByActinstID(String actinstid){
		if(actinstid!=null&&actinstid!=""){
			return commonDao.getDataList(Wfi_Route.class, "select * from "+Common.WORKFLOWDB+"Wfi_Route where next_actinst_id='"+actinstid+"'");
		}
		else {
			return null;
		}
	}
	/**
	 * 获得当前活动实例
	 * 
	 * */
	public Wfi_Route  GetProActDefByActinstID(String  actinstid){
		Wfi_Route route=null;
		if(actinstid!=null&&!"".equals(actinstid)){
			List<Wfi_Route> Wfi_Routes=commonDao.getDataList(Wfi_Route.class, Common.WORKFLOWDB+"Wfi_Route", "next_actinst_id='"+actinstid+"'");
			if(Wfi_Routes!=null&&Wfi_Routes.size()>0){
				 route=Wfi_Routes.get(0);
				
			}
		}
		return route;
	}
	/**
	 * 返回流程的某种状态的所有理由实例
	 * **/
	public List<Wfi_Route>  GetRouteInstByProinstid(String proinstid,int type){
		 List<Wfi_Route> list=null;
			 list=commonDao.getDataList(Wfi_Route.class, "select * from "+Common.WORKFLOWDB+"Wfi_Route where proinst_id='"+proinstid+"' and instance_type='"+type+"' and route_id<>'-1' order by creat_time desc"); 
		 return list;
		 
	}
	
	public void DelSmRouteInst(String proinstid){
		if(proinstid!=null&&!"".equals(proinstid)){
			StringBuilder str=new StringBuilder();
			str.append(" Proinst_Id='");
			str.append(proinstid);
			str.append("'");
			List<Wfi_Route> list=commonDao.findList(Wfi_Route.class, str.toString());
			for(int i=0;i<list.size();i++)	
				commonDao.delete(list.get(i));
		}
	}

    /**
     * 判断一个活动是不是跳跃驳回
     * 返回跳跃驳回的记录
     * */
	public Wfi_Route IsSkipPassOver(String actinst_id,List<Wfd_Route> routedefList){
		//检测是否可以跳跃转出
		Wfd_Actdef currrentActDef= smActInst.GetActDef(actinst_id);
		Wfi_ActInst inst=smActInst.GetActInst(actinst_id);
		Integer canpassover= inst.getIsSkipPassOver();
		if(canpassover!=null&&canpassover>0){
				return null;
			}
		//1、查找这个活动最新的历史路由记录
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("select * from ");
		stringBuilder.append(Common.WORKFLOWDB);
		stringBuilder.append("Wfi_Route where next_actinst_id='");
		stringBuilder.append(actinst_id);
		stringBuilder.append("' and instance_type=");
		stringBuilder.append(WFConst.Instance_Type.Instance_OverRule.value);
		stringBuilder.append(" order by CREAT_TIME desc");
		List<Wfi_Route> lastRoute=commonDao.getDataList(Wfi_Route.class,stringBuilder.toString());
		if(lastRoute!=null&&lastRoute.size()>0){
			//存在驳回记录
			Wfi_Route last=lastRoute.get(0);
			if(last!=null){
				 for (int i = 0;i<routedefList.size();i++){
					 String actdef_id=routedefList.get(i).getNext_Actdef_Id();
				if(last.getActdef_Id().equals(actdef_id)){
					//非单步驳回（跳跃驳回）
					//获取转出到驳回的定义
					return null;
					
					}
				 }
				 return last;	
			}
				
			
		}
		return null;
		
	}
	
	//public Wfi_Route get
	 /**
     * 海口判断一个活动驳回前是否有审批意见未删除
     * 返回驳回前有审批意见未删除的记录
     * */
	public Wfi_Route IsPassOver(String actinst_id,String Next_actdef_id){
		//1、查找这个活动最新的历史路由记录
		StringBuilder stringBuilder=new StringBuilder();
		StringBuilder sql=new StringBuilder();
		stringBuilder.append("select * from ");
		stringBuilder.append(Common.WORKFLOWDB);
		stringBuilder.append("Wfi_Route where next_actinst_id='");
		stringBuilder.append(actinst_id);
		stringBuilder.append("' and instance_type=");
		stringBuilder.append(WFConst.Instance_Type.Instance_OverRule.value);
		stringBuilder.append(" order by CREAT_TIME desc");
		List<Wfi_Route> lastRoute=commonDao.getDataList(Wfi_Route.class,stringBuilder.toString());
		if(lastRoute!=null&&lastRoute.size()>0){
			//存在驳回记录
			Wfi_Route last=lastRoute.get(0);
			if(last!=null){
				sql.append("select * from ");
				sql.append(Common.WORKFLOWDB);
				sql.append("Wfi_spyj where actinst_id='");
				sql.append(last.getFromactinst_Id());
				sql.append("' and spyj is not null");
				List<Wfi_Spyj> spyj=commonDao.getDataList(Wfi_Spyj.class,sql.toString());
				if(spyj!=null&&spyj.size()>0){
					return last;	
				}
			}			
		}
		return null;
	}
	 /**
     * 判断一个活动是不是驳回
     * 返回驳回的记录
     * */
	public Wfi_Route PassOver(String actinst_id){
		//1、查找这个活动最新的历史路由记录
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("select * from ");
		stringBuilder.append(Common.WORKFLOWDB);
		stringBuilder.append("Wfi_Route where next_actinst_id='");
		stringBuilder.append(actinst_id);
		stringBuilder.append("' and instance_type=");
		stringBuilder.append(WFConst.Instance_Type.Instance_OverRule.value);
		stringBuilder.append(" order by CREAT_TIME desc");
		List<Wfi_Route> lastRoute=commonDao.getDataList(Wfi_Route.class,stringBuilder.toString());
		if(lastRoute!=null&&lastRoute.size()>0){
			//存在驳回记录
			Wfi_Route last=lastRoute.get(0);		
					return last;			
		}
		return null;
	}
    
}
