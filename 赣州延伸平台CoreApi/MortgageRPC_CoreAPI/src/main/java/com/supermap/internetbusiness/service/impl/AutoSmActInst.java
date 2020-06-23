package com.supermap.internetbusiness.service.impl;

import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.service.RoleService;
import com.supermap.wisdombusiness.framework.service.UserService;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.*;
import com.supermap.wisdombusiness.workflow.service.common.*;
import com.supermap.wisdombusiness.workflow.service.wfd.SmActDef;
import com.supermap.wisdombusiness.workflow.service.wfd.SmHoliday;
import com.supermap.wisdombusiness.workflow.service.wfd.SmRoleDef;
import com.supermap.wisdombusiness.workflow.service.wfd.SmRouteDef;
import com.supermap.wisdombusiness.workflow.service.wfi.*;
import com.supermap.wisdombusiness.workflow.service.wfm.SmMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component("autoSmActInst")
public class AutoSmActInst {

	@Autowired
	private CommonDao commonDao;

	@Autowired
	private SmProInst smProInst;

	@Autowired
	private SmStaff smStaff;

	@Autowired
	private SmRouteInst smRouteInst;

	@Autowired
	private SmActDef smActDef;

	@Autowired
	private SmRoleDef smRoleDef;
	@Autowired
	private SmRouteDef smRouteDef;
	@Autowired
	private SmHoliday smHoliday;
	@Autowired
	private SmMaterialService smMaterialService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private SmAbnormal smAbnormal;
	@Autowired
	private UserService userService;
	@Autowired
	private SmActInst _smActInst;

	/**
	 * 自动挂起项目
	 *
	 * @param actinstid
	 * @param msg
	 * @param userid
	 * @param username
	 * @return
	 */
	public boolean setAutoHangUp(String actinstid, String msg, String userid, String username) {
		boolean Result = false;
		if (actinstid != null && !actinstid.equals("")) {
			Wfi_ActInst inst = _smActInst.GetActInst(actinstid);
			if (inst != null) {
				inst.setHangup_Time(new Date());
				if (null == inst.getHangpausetime())
					inst.setHangpausetime(inst.getHangup_Time());
				inst.setHangup_Staff_Id(userid);
				inst.setHangup_Staff_Name(username);
				inst.setMsg(msg);
				inst.setInstance_Type(WFConst.Instance_Type.Instance_HangingUp.value);
				inst.setOperation_Type(WFConst.Operate_Type.ApplyHungUp.value + "");
				//TODO:挂起设置最新的活动实例状态
				Wfi_ProInst proinst = smProInst.GetProInstByActInstId(inst.getActinst_Id());
				if (proinst != null) {
					proinst.setOperation_Type_Nact(inst.getOperation_Type());
					proinst.setMsg(inst.getMsg());
				}
				commonDao.update(inst);
				commonDao.flush();
				Result = true;
			}
		}
		return Result;

	}

	/**
	 * 获取活动信息
	 * 
	 * @param ActInstID
	 *            String 活动实例ID
	 * @return SmActInfo 活动综合信息
	 * */
	public SmActInfo GetActInfo(String ActInstID) {
		SmActInfo info = null;
		if (ActInstID != null && ActInstID != "") {

			info = new SmActInfo();

			Wfi_ActInst actinst = commonDao.load(Wfi_ActInst.class, ActInstID);
			if (actinst != null) {
				info.setActDef_ID(actinst.getActdef_Id());
				info.setActInst_ID(actinst.getActinst_Id());
				info.setActInst_Name(actinst.getActinst_Name());
				info.setProInst_ID(actinst.getProinst_Id());

			}

		}

		return info;
	}

	/**
	 * 通过actinstID 获取活动信息
	 * 
	 * 
	 * */
	public Wfi_ActInst GetActInst(String actistid) {
		if (actistid != null && !actistid.equals("")) {
			return commonDao.get(Wfi_ActInst.class, actistid);
		} else {
			return null;
		}

	}

	/**
	 * 转出当前活动，在当前活动为终止活动情况下
	 * 
	 * @param actinstid
	 *            活动ID;
	 * @param staff_ID
	 *            String 员工ID
	 * 
	 * */

	public boolean PassOver(String actinstid, String staff_ID) {
		boolean success = false;
		try {
			Wfi_ActInst inst = commonDao.get(Wfi_ActInst.class, actinstid);
			if (inst != null) {
				inst.setActinst_Status(WFConst.Instance_Status.Instance_havedone.value);
				inst.setOperation_Type(WFConst.Operate_Type.NormalOperate.value
						+ "");
				inst.setActinst_End(new Date());

				Wfi_ProInst proInst = commonDao.get(Wfi_ProInst.class,
						inst.getProinst_Id());
				proInst.setProinst_End(new Date());
				proInst.setProinst_Status(WFConst.Instance_Status.Instance_havedone.value);
				proInst.setOperation_Type(WFConst.Operate_Type.NormalOperate.value
						+ "");
				//项目办结之后更改流程实例记录中的当前活动实例的信息
				proInst.setActinst_Status(inst.getActinst_Status());
				proInst.setOperation_Type_Nact(inst.getOperation_Type());
				proInst.setACTINST_END(inst.getActinst_End());
				commonDao.delete(Wfi_NowActInst.class, actinstid);
				commonDao.update(inst);
				commonDao.update(proInst);
				DelNowActinst(actinstid, inst.getStaff_Id());
				
				commonDao.flush();
				success = true;
			}

		} catch (Exception e) {
		}
		return success;
	}

	/**
	 * 通过活动实例编号获取活动定义
	 * 
	 * @parma actinstid String 活动实例ID
	 * */
	public Wfd_Actdef GetActDef(String actinstid) {
		Wfi_ActInst actInst = commonDao.get(Wfi_ActInst.class, actinstid);
		if(actInst!=null){
			return commonDao.get(Wfd_Actdef.class, actInst.getActdef_Id());
		}
        return null;
	}

	/**
	 * 新增一个活动实例
	 * 
	 * @param actdef
	 *            Wfd_Actdef 活动定义
	 * @param Staffids
	 *            String 可办理的所有员工
	 * @param Msg
	 *            String 转出附言
	 * @parma Routeid String 路由ID
	 * */
	public Wfi_ActInst AddNewActInst(Wfd_Actdef actdef,
			List<SmObjInfo> staffids, String Msg, Wfi_ProInst wProInst) {
		Wfi_ActInst actInst = null;
		try {
			if (!smActDef.IsAllDoActInst(actdef)) {
				actInst = CreatActDef(actdef, wProInst, Msg);// 判断创建一个实例
				// actInst.setStaff_Id(smStaff.getCurrentWorkStaffID());//
				// 设置没有人员办理，待办件
				if (smActDef.IsStartAct(actdef))// 判断是不是起始活动
				{

				} else {

				}
			} else {

			}
		} catch (Exception e) {
		}
		return actInst;
	}

	/**
	 * 创建一个活动根据活动定义ID 和流程实例
	 * */
	public Wfi_ActInst GetActInst(String actdefid, String proinstid) {// 过滤已经转出的字段

		StringBuilder sbBuilder = new StringBuilder();
		sbBuilder.append("select * from ");
		sbBuilder.append(Common.WORKFLOWDB);
		sbBuilder.append("WFI_ACTINST where status<>");
		sbBuilder.append(WFConst.Instance_Status.Instance_Passing.value);// 3
		sbBuilder.append("And ProInst_ID ='");
		sbBuilder.append(proinstid);
		sbBuilder.append("'");
		sbBuilder.append(" and ACTDEF_ID='");
		sbBuilder.append(actdefid);
		sbBuilder.append("'");

		List<Wfi_ActInst> list = commonDao.getDataList(Wfi_ActInst.class,
				sbBuilder.toString());
		if (list != null && list.size() > 0) {
			return new Wfi_ActInst();

		}
		return null;
	}

	/*
	 * 创建一个活动根据活动定义ID 和流程实例
	 */
	public List<Wfi_ActInst> QueryActInst(String actdefid, String proinstid) {

		StringBuilder sbBuilder = new StringBuilder();
		sbBuilder.append("select * from ");
		sbBuilder.append(Common.WORKFLOWDB);
		sbBuilder.append("WFI_ACTINST where ");

		sbBuilder.append(" ProInst_ID ='");
		sbBuilder.append(proinstid);
		sbBuilder.append("'");
		sbBuilder.append(" and ACTDEF_ID='");
		sbBuilder.append(actdefid);
		sbBuilder.append("' order by actinst_start desc");

		List<Wfi_ActInst> list = commonDao.getDataList(Wfi_ActInst.class,
				sbBuilder.toString());
		return list;
	}
    //获取前置活动实例
	public Wfi_ActInst GetBackActInst(String actdefid, String proinstid) {

		StringBuilder sbBuilder = new StringBuilder();
		sbBuilder.append("select * from ");
		sbBuilder.append(Common.WORKFLOWDB);
		sbBuilder.append("WFI_ACTINST where ");

		sbBuilder.append(" ProInst_ID ='");
		sbBuilder.append(proinstid);
		sbBuilder.append("'");
		sbBuilder.append(" and ACTDEF_ID='");
		sbBuilder.append(actdefid);
		sbBuilder.append("' order by actinst_start desc ");

		List<Wfi_ActInst> list = commonDao.getDataList(Wfi_ActInst.class,
				sbBuilder.toString());
		if (list != null && list.size() > 0) {
			return list.get(0);

		}
		return null;
	}
	//获取下一个活动实例         
	public Wfi_ActInst GetNextActInst(String actinstid,String actdef) {
		List<Wfi_Route> lists=commonDao.getDataList(Wfi_Route.class, "select * from "+Common.WORKFLOWDB+"Wfi_Route where fromactinst_id='"+actinstid+"' and actdef_id='"+actdef+"'");
		if(lists!=null&&lists.size()>0){
			return GetActInst(lists.get(0).getNext_Actinst_Id()) ;
		}		
		return null;
	}
	//获取下一个活动实例    考虑并发环节，获取下一活动是一个list
		public List<Wfi_ActInst> GetNextActInstList(String actinstid,String actdef) {
			List<Wfi_Route> lists=commonDao.getDataList(Wfi_Route.class, "select * from "+Common.WORKFLOWDB+"Wfi_Route where fromactinst_id='"+actinstid+"' and actdef_id='"+actdef+"'");
			if(lists!=null&&lists.size()>0){
				List<Wfi_ActInst> insts= new ArrayList<Wfi_ActInst>();
				for(Wfi_Route r : lists){
					insts.add(GetActInst(r.getNext_Actinst_Id()));
				}
				return insts ;
			}
			return null;
		}
	/**
	 * 更具活动定义创建一个新的活动实例
	 * 
	 * **/
	public Wfi_ActInst CreatActDef(Wfd_Actdef actdef, Wfi_ProInst proinst,
			String Msg) {
		Wfi_ActInst actInst = null;
		if (actdef != null) {
			actInst = new Wfi_ActInst();
			actInst.setActinst_Id(Common.CreatUUID());
			actInst.setActdef_Id(actdef.getActdef_Id());
			actInst.setActinst_Start(new Date());
			actInst.setProinst_Id(proinst.getProinst_Id());
			actInst.setActinst_Name(actdef.getActdef_Name());
			actInst.setActdef_Type(actdef.getActdef_Type());
			actInst.setOperation_Type(WFConst.Operate_Type.NormalOperate.value
					+ "");
			actInst.setInstance_Type(proinst.getInstance_Type());
			actInst.setActinst_Status(WFConst.Instance_Status.Instance_NotAccept.value);
			actInst.setDept_Code(actdef.getActdef_Dept_Id());// 设置单位ID
			//actInst.setOperation_Type("0");
			// actInst.setActinst_Willfinish(DateUtil.addDay(new Date(),
			// actdef.getActdef_Time()));// 计算期望完成时间
			// actInst.setActinst_Willfinish(smHoliday.FactDate(new Date(),
			// actdef.getActdef_Time()));
		//	GregorianCalendar cal = new GregorianCalendar();
			try {
				GregorianCalendar cal = _smActInst.getTime(actdef);
				actInst.setActinst_Willfinish(cal.getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			cal.setTime();
//			actInst.setActinst_Willfinish(smHoliday.addDateByWorkDay(cal,actdef.getActdef_Time()));
			actInst.setActinst_Msg(Msg);
			actInst.setUploadfile(actdef.getUploadfile());
			actInst.setCodeal(actdef.getCodeal());
		}
		return actInst;
	}
	
	/**
	 * 创建一个当前的活动
	 * 
	 * @param atcinst
	 *            Wfi_ActInst 活动实例
	 * @return Wfi_ActInst 一个活动实例
	 * */
	public Wfi_NowActInst CreatNowActInst(Wfi_ActInst atcinst) {// 过滤已经转出的字段
		// 参照已经创建的活动复制一个副本到当前活动
		if (atcinst != null) {
			// 能不能用强制类型转换呢？？？？？？
			Wfi_NowActInst NowactInst = new Wfi_NowActInst();
			NowactInst.setActinst_Id(atcinst.getActinst_Id());
			NowactInst.setActinst_Name(atcinst.getActinst_Name());
			NowactInst.setActdef_Id(atcinst.getActdef_Id());
			NowactInst.setActinst_End(atcinst.getActinst_End());
			NowactInst.setActinst_Start(atcinst.getActinst_Start());
			NowactInst.setActinst_Msg(atcinst.getActinst_Msg());
			NowactInst.setActinst_Willfinish(atcinst.getActinst_Willfinish());
			NowactInst.setDept_Code(atcinst.getDept_Code());
			NowactInst.setInstance_Type(atcinst.getInstance_Type());
			NowactInst.setOperation_Type(atcinst.getOperation_Type());
			NowactInst.setProinst_Id(atcinst.getProinst_Id());
			NowactInst.setStaff_Id(atcinst.getStaff_Id());
			NowactInst.setStaff_Ids(atcinst.getStaff_Ids());
			NowactInst.setActinst_Status(atcinst.getActinst_Status());
			NowactInst.setActdef_Type(atcinst.getActdef_Type());
			NowactInst.setOverruleactinst_Ids(atcinst.getOverruleactinst_Ids());
			NowactInst.setPassedroute_Count(atcinst.getPassedroute_Count());
			NowactInst.setStatus(atcinst.getStatus());
			NowactInst.setUnlockDate(atcinst.getUnLockDate());
			NowactInst.setLockType(atcinst.getLockType());
			NowactInst.setCodeal(atcinst.getCodeal());
			return NowactInst;
		}

		return null;

	}

	/**
	 * 创建活动与员工的关系
	 * 
	 * @param actinstid
	 *            活动ID
	 * @param staffids
	 *            SmObjInfo 员工信息
	 * @param Role
	 *            String 角色ID
	 * @return Wfi_ActInstStaff[] 关系对象组
	 * */
	public Wfi_ActInstStaff[] CreatActStaff(String actinstid,
			List<SmObjInfo> staffids, String Role) {
		if (staffids != null && staffids.size() > 0) {

			Wfi_ActInstStaff[] actInstStaffs = new Wfi_ActInstStaff[staffids
					.size()];
			for (int i = 0; i < staffids.size(); i++) {
				actInstStaffs[i] = CreatActStaff(actinstid, staffids.get(i),
						Role);
			}
			return actInstStaffs;
		}
		return null;
	}

	public Wfi_ActInstStaff CreatActStaff(String actinstid, SmObjInfo staffid,
			String Role) {
		Wfi_ActInstStaff actInstStaffs = null;
		if (staffid != null) {

			actInstStaffs = new Wfi_ActInstStaff();
			actInstStaffs.setActstaffid(Common.CreatUUID());
			actInstStaffs.setStaff_Id(staffid.getID());
			actInstStaffs.setActinst_Id(actinstid);
			actInstStaffs.setRole_Id(Role);
			actInstStaffs.setStaff_Name(staffid.getName());
			return actInstStaffs;
		}
		return actInstStaffs;
	}

	/**
	 * 获取当前实例
	 * 
	 * @param actinst
	 *            String 活动实例主键
	 * */
	public Wfi_NowActInst GetNowActinst(String actinst) {
		if (actinst != null && actinst != "") {
			return commonDao.get(Wfi_NowActInst.class, actinst);
		} else {
			return null;
		}

	}
	
	
	/**
	 * 删除当前实例
	 * 
	 * @param actinst
	 *            String 活动实例主键
	 * */
	public void DelNowActinst(String actinst, String StaffID) {
		if (actinst != null && !actinst.equals("")) {
			commonDao.delete(Wfi_NowActInst.class, actinst);
			// 与当前活动相关的wfi_actstaff 也删除 减少视图数据量
			long count = commonDao.getCountByFullSql(" from "
					+ Common.WORKFLOWDB + "Wfi_ActInstStaff where actinst_id='"
					+ actinst + "' and staff_id<>'" + StaffID + "'");
			if (count > 0) {
				commonDao.deleteQuery("delete " + Common.WORKFLOWDB
						+ "wfi_actinststaff where actinst_id='" + actinst
						+ "' and staff_id<>'" + StaffID + "'");
			}
		}
	}
	/**
	 * 删除活动
	 * */
	public void DelActInst(String actinst) {
		if (actinst != null && actinst != "") {
			commonDao.delete(Wfi_ActInst.class, actinst);
		}
	}

	/**
	 * 根据指定的路由＆办理员工，转出活动
	 * 
	 * @param staffid
	 *            String 员工ID
	 * @param routeid
	 *            String 路由ID
	 * @param actinstid
	 *            活动实例ID
	 * @param 目标集合id
	 * @param nextMsg
	 *            转出附言
	 * */
	@SuppressWarnings({ "unchecked", "unused" })
	@Transactional
	public List<Wfi_ActInst> PassOver(String staffid, String routeid,
			String actinstid, List<SmObjInfo> staffids, String nextMsg) {
		@SuppressWarnings("rawtypes")
		
		List<Wfi_ActInst> newActInst = new ArrayList();
		commonDao.clear();
		Wfi_ActInst actinst = commonDao.get(Wfi_ActInst.class, actinstid);
		String staffName = "";// 获取员工姓名
		if (actinst != null) {
			
			String staffname = smStaff.GetStaffName(staffid);
			Wfi_ProInst proInst = smProInst.GetProInstByActInstId(actinstid);
			Wfd_Actdef curactdef = GetActDef(actinstid);// 当前活动定义
			actinst.setActinst_Status(WFConst.Instance_Status.Instance_Passing.value);
			actinst.setActinst_End(new Date());
			actinst.setOperation_Type(WFConst.Operate_Type.NormalOperate.value
					+ "");
//			actinst.setStaff_Id(staffids.get(0).getID());
//			actinst.setStaff_Name(staffids.get(0).getName());
			if(proInst.getProinst_Weight().intValue()==WFConst.Proinst_Weight.PassBack.value){
				proInst.setProinst_Weight(WFConst.Proinst_Weight.Normal.value);
			}else if(proInst.getProinst_Weight().intValue()==WFConst.Proinst_Weight.Supervised.value){
			}else if(proInst.getProinst_Weight().intValue()>WFConst.Proinst_Weight.PassBack.value){
				proInst.setProinst_Weight(proInst.getProinst_Weight().intValue()-WFConst.Proinst_Weight.PassBack.value);
			}
			Wfd_Actdef nextactdef = null;
			if (routeid.equals("-1")) {// 活动是转出到驳回
				nextactdef = smActDef.getActdefBySkipPassActinstID(actinstid);
			} else {
				nextactdef = smActDef.GetNextActDef(routeid);
			}
			// 创建路由
			Wfi_Route route = smRouteInst.CreateRouteInst(routeid, actinstid,
					proInst.getProinst_Id());
			route.setActdef_Id(curactdef.getActdef_Id());
			route.setNext_Actdef_Id(nextactdef.getActdef_Id());
			route.setRoute_Name("转到" + nextactdef.getActdef_Name());
			String actdefType = curactdef.getActdef_Type();
			if (actdefType == (WFConst.ActDef_Type.ProcessEnd.value + "")) {
				// 办结项目
			} else {
				if (nextactdef.getActdef_Type().equals(
						WFConst.ActDef_Type.ActivityMerge.value + "")) {// 聚合
					//查找该流程实例，聚合活动待办的件
					Wfi_ActInst mergerinst=GetMergerInst(proInst.getProinst_Id());
					if(mergerinst!=null){
						mergerinst.setPassedroute_Count(mergerinst.getPassedroute_Count()+1);	
						List<Wfd_Route> preroutedef=smRouteDef.GetProRoute(nextactdef.getActdef_Id());
						if(preroutedef!=null&&preroutedef.size()==mergerinst.getPassedroute_Count()){
							mergerinst.setPassedroute_Count(null);
							newActInst.add(mergerinst) ;
							route.setNext_Actinst_Id(mergerinst.getActinst_Id());
							route.setInstance_Type(WFConst.Instance_Type.Instance_Normal.value);
							route.setCraet_Time(new Date());
							commonDao.save(route);
							//计算时间
							mergerinst.setActinst_Start(new Date());
							GregorianCalendar cal = new GregorianCalendar();
							cal.setTime(new Date());
							mergerinst.setActinst_Willfinish(smHoliday.addDateByWorkDay(cal,
									nextactdef.getActdef_Time()));
							//TODO:转出后更新流程实例中对应的最新的环节信息
							proInst.setActdef_Type(mergerinst.getActdef_Type());
							proInst.setActinst_Id(mergerinst.getActinst_Id());
							proInst.setIsApplyHangup(mergerinst.getIsApplyHandup());
							proInst.setStatusext(mergerinst.getStatusExt());
							proInst.setOperation_Type_Nact(mergerinst.getOperation_Type());
							proInst.setStaff_Id_Nact(mergerinst.getStaff_Id());
							proInst.setStaff_Name_Nact(mergerinst.getStaff_Name());
							proInst.setActinst_Status(mergerinst.getActinst_Status());
							proInst.setActinst_Willfinish(mergerinst.getActinst_Willfinish());
							proInst.setActinst_Name(mergerinst.getActinst_Name());
							proInst.setMsg(mergerinst.getMsg());
							proInst.setCodeal(mergerinst.getCodeal());
							proInst.setACTINST_START(mergerinst.getActinst_Start());
							proInst.setACTINST_END(mergerinst.getActinst_End());
							List<Wfi_ActInstStaff> actStaffList = commonDao.findList(Wfi_ActInstStaff.class,
									" actinst_id='" + mergerinst.getActinst_Id() + "'");
							DelNowActinst(actinstid, actinst.getStaff_Id());
							commonDao.update(mergerinst);
							commonDao.update(proInst);
							commonDao.flush();
							return newActInst;
						}
					}
				}
				//@author:JHX 2018-08-16
				//TODO:启用是否随机属性，和随机数；1:是随机,随机件数量上的判断
				Integer isRandom = nextactdef.getIsrandom();
				boolean isRandomActinst = false;
				if(isRandom!=null && isRandom==1){
					Double randomValue = nextactdef.getRandomvalue();
					Integer staffNumbers = staffids.size();
					Integer random = null;
					if(randomValue!=null&&staffNumbers!=null && randomValue>staffNumbers){
						random = staffNumbers;
					}else if(randomValue!=null&&staffNumbers!=null && randomValue<staffNumbers){
						if(randomValue.intValue()==0){
							random = staffNumbers;
						}else{
							random =randomValue.intValue();
						}
						
					}else{
						random = staffNumbers;
					}
					long count = 0;
					for(int i = 0;i<staffids.size();i++){
						String fromSql = "from SMWB_FRAMEWORK.T_USER where ispj=1 and status='NORMAL' and id='"+staffids.get(i).getID().toString()+"'";
						count += commonDao.getCountByFullSql(fromSql);
						//count += count;
					}
					int number = new Random().nextInt(random);
					String staffid_1 = "";
					long count_1 = 0;
					if(count>(staffids.size()-1)){
						for(int i = 0;i<staffids.size();i++){
							User user_ = commonDao.get(User.class, staffids.get(i).getID().toString());
//							user_.setIspj("0");
							commonDao.update(user_);
							commonDao.flush();
						}
					}else{
						while(number < staffids.size()){
							staffid_1 = staffids.get(number).getID().toString();
							String fromSql = "from SMWB_FRAMEWORK.T_USER where ispj=1 and status='NORMAL' and id='"+staffid_1+"'";
							count_1 = commonDao.getCountByFullSql(fromSql);
							if(count_1 < 1){
								break;
							}else{
								number = new Random().nextInt(random);
							}
						}
						List<SmObjInfo> randomEndStaffs = new ArrayList<SmObjInfo>();
						randomEndStaffs.add(staffids.get(number));
						if(randomEndStaffs.size()>0){
							staffids = randomEndStaffs;
							isRandomActinst = false;
							User user_ = commonDao.get(User.class, staffids.get(0).getID().toString());
//							user_.setIspj("1");
							commonDao.update(user_);
							commonDao.flush();
						}
					}
				}
				// 正常的一对一活动
				Wfi_ActInst backActInst = AddNewActInst(nextactdef, staffids,
						nextMsg, proInst);
				backActInst
						.setActinst_Status(WFConst.Instance_Status.Instance_NotAccept.value);
				backActInst.setFromActinstid(actinst.getActinst_Id());
				if (nextactdef.getActdef_Type().equals(
						WFConst.ActDef_Type.ActivityMerge.value + "")) {// 聚合{
					if(backActInst.getPassedroute_Count()==null){
						backActInst.setPassedroute_Count(1);
					}
					backActInst.setFromActinstid(actinst.getFromActinstid());
				}
				//判断actinst 是否是驳回
				if(actinst.getOperation_Type().equals(WFConst.Operate_Type.PRODUCT_anglesPicture.value)){
					backActInst.setActinst_Start(actinst.getActinst_Start());
					backActInst.setActinst_Willfinish(actinst.getActinst_Willfinish());
				}
				//TODO:JHX如果当前活动是是驳回活动，并且转出的下个环节定义是聚合活动,
				//那么需要设置下个环节实例的passroute_cout为空
				if(actinst.getInstance_Type()!=null&&
						actinst.getInstance_Type()==WFConst.Instance_Type.Instance_OverRule.value){
					   if( nextactdef.getActdef_Type()
					    .equals(WFConst.ActDef_Type.ActivityMerge.value+"")){
						   backActInst.setPassedroute_Count(null);
					}
				}
				newActInst.add(backActInst);
				commonDao.save(backActInst);
				DelNowActinst(actinstid, actinst.getStaff_Id());

				// 创建活动人员关系
				Wfi_ActInstStaff[] actstaffs = CreatActStaff(
						backActInst.getActinst_Id(), staffids, "");// 暂时不传递角色
				boolean updateProinst=false;
				if (proInst.getInstance_Type() != WFConst.Instance_Type.Instance_Normal.value) {
					proInst.setInstance_Type(WFConst.Instance_Type.Instance_Normal.value);
					//为了防止连续驳回后再转出时权重设置错误，提前更新当前环节操作类型
					proInst.setOperation_Type_Nact(backActInst.getOperation_Type());
					proInst = smProInst.SetWeight(proInst);
					updateProinst=true;
					
				}
				if (actstaffs != null && actstaffs.length > 0) {
					for (Wfi_ActInstStaff wfi_ActInstStaff : actstaffs) {
						commonDao.save(wfi_ActInstStaff);
					}
					// 在路由中记录下一个活动的信息
					route.setNext_Actinst_Id(backActInst.getActinst_Id());
					route.setInstance_Type(WFConst.Instance_Type.Instance_Normal.value);
					route.setCraet_Time(new Date());
					// 判断是否是自动转出
				}
				Wfi_NowActInst nowActInst = CreatNowActInst(backActInst);
				//TODO:转出后更新流程实例中对应的最新的环节信息
				proInst.setActdef_Type(backActInst.getActdef_Type());
				proInst.setActinst_Id(backActInst.getActinst_Id());
				proInst.setIsApplyHangup(backActInst.getIsApplyHandup());
				proInst.setStatusext(backActInst.getStatusExt());
				proInst.setOperation_Type_Nact(backActInst.getOperation_Type());
				proInst.setStaff_Id_Nact(backActInst.getStaff_Id());
				proInst.setStaff_Name_Nact(backActInst.getStaff_Name());
				proInst.setActinst_Status(backActInst.getActinst_Status());
				proInst.setActinst_Willfinish(backActInst.getActinst_Willfinish());
				proInst.setActinst_Name(backActInst.getActinst_Name());
				proInst.setMsg(backActInst.getMsg());
				proInst.setCodeal(backActInst.getCodeal());
				proInst.setACTINST_START(backActInst.getActinst_Start());
				proInst.setACTINST_END(backActInst.getActinst_End());
				//流程时间是开始活动转出再计算
				//流程实例办结时间为什么要重新计算？先备注掉吧
				/*if(actinst.getActdef_Type().equals(WFConst.ActDef_Type.ProcessStart.value+"")){
					if (proInst.getProinst_Time() != null) {
						GregorianCalendar cal = new GregorianCalendar();
						cal.setTime(new Date());
						proInst.setProinst_Willfinish(smHoliday.addDateByWorkDay(cal,
								proInst.getProinst_Time()));
						updateProinst=true;
					}
				}*/
								
				if(updateProinst){
					commonDao.update(proInst);	
				}
				commonDao.save(nowActInst);
			}
			
			commonDao.save(route);
			commonDao.update(actinst);
			commonDao.flush();
		}
		
		return newActInst;
	}

	/**
	 * 产生一个转出附言
	 * 
	 * @param staffName
	 *            操作人姓名
	 * @param isStartAct
	 *            boolean 起始活动
	 * @param nMsg
	 *            String 附言
	 * */
	public String FormCommentLog(String staffName, boolean isStartAct,
			String nMsg) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(staffName);
		sBuilder.append("于");
		sBuilder.append(new Date());
		if (isStartAct) {
			sBuilder.append("受理");
		} else {
			sBuilder.append("转出");

		}
		if (nMsg != null && nMsg.length() > 0) {
			sBuilder.append(",并留言：");
			sBuilder.append(nMsg);

		}
		return sBuilder.toString();

	}

	/**
	 * 活动是否可以被注销
	 * 
	 * @param actdefid
	 *            String 活动实例ID
	 * @return true 可以注销 false 不能注销
	 * */
	public boolean IsLogOutByActInstid(String actinstid) {
		if (actinstid != "") {
			Wfi_ActInst actInst = commonDao.get(Wfi_ActInst.class, actinstid);
			Wfd_Actdef actdef = commonDao.get(Wfd_Actdef.class,
					actInst.getActdef_Id());
			if (actdef != null) {
				if (actdef.getCanlogoutornot() == WFConst.ActDef_Type.CanLonout.value) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 是否是随机活动
	 * 
	 * @param actdefid
	 *            String 活动实例ID
	 * @return 如果传值大于0 则是随机 切值为随机概率 若值是0 则不是随机活动
	 * */
	public double IsRandomByActInstid(String actinstid) {
		if (actinstid != "") {
			Wfi_ActInst actInst = commonDao.get(Wfi_ActInst.class, actinstid);

			Wfd_Actdef actdef = commonDao.get(Wfd_Actdef.class,
					actInst.getActdef_Id());
			if (actdef != null) {
				if (actdef.getIsrandom() != null
						&& actdef.getIsrandom() == WFConst.ActDef_Type.CanRadom.value) {
					return actdef.getRandomvalue();
				}
			}
		}
		return 0;

	}

	/**
	 * 是否自动转出
	 * 
	 * @param actinstid
	 *            Stirng 活动实例ID
	 * @return bool 是否
	 * */

	public boolean IsAutoPass(String actinstid) {
		if (actinstid != "") {
			Wfi_ActInst actInst = commonDao.get(Wfi_ActInst.class, actinstid);
			Wfd_Actdef actdef = commonDao.get(Wfd_Actdef.class,
					actInst.getActdef_Id());
			if (actdef != null && actdef.getIsautopass() != null) {
				if (actdef.getIsautopass() == WFConst.ActDef_Type.ActivityAutomation.value) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断是不是办结活动
	 * */
	public boolean IsLastActInst(String actinst) {
		boolean result = false;
		try {
			Wfi_ActInst actInst2 = commonDao.get(Wfi_ActInst.class, actinst);
			result = IsLastActInst(actInst2);
		} catch (Exception e) {
		}
		return result;

	}

	/**
	 * 判断是不是办结活动
	 * */
	public boolean IsLastActInst(Wfi_ActInst actinst) {
		boolean result = false;
		try {
			if (actinst != null) {
				String actinsttype = actinst.getActdef_Type();
				if (actinsttype != null && !actinsttype.equals("")) {
					result = actinsttype == (WFConst.ActDef_Type.ProcessEnd.value + "") ? true
							: false;
				}
			}
		} catch (Exception e) {
		}
		return result;

	}

	/**
	 * 自动转出
	 *
	 * @param wfi_ActInst
	 *            Wfi_ActInst 活动实例
	 * */
	public void AutoPassOver(Wfi_ActInst wfi_ActInst, String staffid,String AreaCode) {
		List<Wfd_Route> routes = smRouteDef.GetNextRouteByActinstID(wfi_ActInst
				.getActinst_Id());
		if (routes != null && routes.size() == 1) {
			List<SmObjInfo> staffidsInfos = smRoleDef
					.GetStaffByActDefID(wfi_ActInst.getActdef_Id());
			PassOver(staffid, routes.get(0).getRoute_Id(),
					wfi_ActInst.getActinst_Id(), staffidsInfos, "自动转出");
		}
	}

	/**
	 * 设置活动的在办人员
	 * 
	 * @param actinstid
	 *            String 活动ID
	 * @param straffid
	 *            String 员工ID
	 * */

	public SmObjInfo SetActinstWorkStaff(String actinst, String Staffid) {
		Wfi_ActInst inst = GetActInst(actinst);
		ValiteActinst(inst.getActdef_Id(), inst.getProinst_Id(), actinst);
		SmObjInfo objInfo = new SmObjInfo();
		if (inst != null) {
			if (inst.getActinst_Status() != WFConst.Instance_Status.Instance_Passing.value) {
				if (inst.getStaff_Id() == null || inst.getStaff_Id().equals("")
						|| inst.getStaff_Id().equals("0")
						||inst.getActinst_Status()==WFConst.Instance_Status.Instance_NotAccept.value) {
					inst.setStaff_Id(Staffid);
					inst.setDept_Code(smStaff.GetStaffDeptID(Staffid));
					inst.setStaff_Name(smStaff.GetStaffName(Staffid));
					inst.setActinst_Status(WFConst.Instance_Status.Instance_doing.value);
					//inst.setActinst_Start(new Date());
					//TODO:环节又待办更新为再办的时候更新最新的状态。
					Wfi_ProInst proinst = smProInst.GetProInstByActInstId(actinst);
					if(proinst!=null){
						//来自外网受理的项目受理人员为空的情况处理
						String staffid = proinst.getStaff_Id();
						if(StringHelper.isEmpty(staffid)){
							proinst.setStaff_Id(inst.getStaff_Id());
							proinst.setAcceptor(inst.getStaff_Name());
						}
						proinst.setStaff_Id_Nact(inst.getStaff_Id());
						proinst.setActinst_Status(inst.getActinst_Status());
						proinst.setStaff_Name_Nact(inst.getStaff_Name());
					}
					List<Wfi_ActInst> list=GetProActInsts(actinst);
					if(list!=null&&!list.isEmpty()){
						Wfi_ActInst act=list.get(0);
						act.setToStaffName(smStaff.GetStaffName(Staffid));
						commonDao.update(act);
					}
					//检测当前环节是否为转办，如果为转办，更新转办状态
					List<Wfi_TurnList> turnlist = commonDao.findList(Wfi_TurnList.class, " actinst_id='"+actinst
							+"' and TOSTAFFID = '"+Staffid
							+"' and status = '0' ");
					if(turnlist.size()>0){
						for(Wfi_TurnList turn : turnlist){
							turn.setStatus("1");
							commonDao.update(turn);
						}
					}
					commonDao.update(proinst);
					commonDao.update(inst);
					commonDao.flush();
					objInfo.setID(Staffid);
					objInfo.setName(inst.getStaff_Name());
					// objInfo.setDesc("设置成功");
				}  else if (inst.getStaff_Id().equals(Staffid)) {
					objInfo.setID(Staffid);
					objInfo.setName(inst.getStaff_Name());

				} else {
					if (inst.getCodeal() == 1) {// 开启了多人协作模式
						objInfo.setID(Staffid);
						objInfo.setName(smStaff.GetStaffName(Staffid));
						objInfo.setConfirm(inst.getStaff_Id());
					} else {
						objInfo.setID(inst.getStaff_Id());
						objInfo.setDesc("项目已由" + inst.getStaff_Name()
								+ "办理,请刷新待办项");
					}

				}
				Integer codeal = GetActDef(actinst).getCodeal();
				if (codeal != null && codeal == 1) {// 开启了多人协作模式
					objInfo.setConfirm(inst.getStaff_Id());
				}
				Wfi_ProInst pinst = smProInst.GetProInstByActInstId(actinst);
				smMaterialService.setToActinst_Name(Staffid,
						pinst.getFile_Number(), inst.getActinst_Name());
			} else {
				objInfo.setID("0");
				objInfo.setDesc("项目已经处理完毕");
			}

		}
		return objInfo;
	}

	/*
	 * 一个容错设置，在设置员工的时候看看是否多创建了活动
	 * 
	 * *
	 */
	public void ValiteActinst(String actdef_id, String proinst_id,
			String actinst_id) {

		List<Wfi_ActInst> actinst = commonDao.getDataList(Wfi_ActInst.class,
				"select * from " + Common.WORKFLOWDB
						+ "Wfi_ActInst where actdef_id='" + actdef_id
						+ "' and proinst_id='" + proinst_id
						+ "' and actinst_status in ("
						+ WFConst.Instance_Status.Instance_doing.value + ","
						+ WFConst.Instance_Status.Instance_NotAccept.value
						+ ") order by actinst_start asc");
		if (actinst != null && actinst.size() > 1) {
			// 一个项目的木一个活动自能有一个
			for (int a = 0; a < actinst.size(); a++) {
				if (!actinst.get(a).getActinst_Id().equals(actinst_id)) {
					commonDao.delete(actinst.get(a));
				}
			}
			commonDao.flush();
		}
	}

	/**
	 * 
	 * @param _SmProInfo
	 * @param _Wfi_ActInst
	 */
	public void ProInfoFromActInst(SmProInfo _SmProInfo,
			Wfi_ActInst _Wfi_ActInst) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		// 活动名称
		_SmProInfo.setActDef_Name(_Wfi_ActInst.getActinst_Name());
		// 剩余时间
		if (_Wfi_ActInst.getActinst_End() != null)
			_SmProInfo.setFinishDate(formatter.format(_Wfi_ActInst
					.getActinst_End()));
		// 案卷状态
		_SmProInfo.setStatus(_Wfi_ActInst.getActinst_Status().toString());
		// 收件时间
		_SmProInfo.setStartTime(formatter.format(_Wfi_ActInst
				.getActinst_Start()));
		_SmProInfo.setUploadfile(_Wfi_ActInst.getUploadfile());
		_SmProInfo.setCodeal(_Wfi_ActInst.getCodeal());
		// 期望完成时间
		_SmProInfo.setActInst_ID(_Wfi_ActInst.getActinst_Id());
		if (_Wfi_ActInst.getActinst_Willfinish() != null)
			_SmProInfo.setActWillFinishTime(formatter.format(_Wfi_ActInst
					.getActinst_Willfinish()));

		if (_Wfi_ActInst.getStaff_Id() != null
				&& _Wfi_ActInst.getStaff_Id().equals("0")) {
			_SmProInfo.setStaffID(_Wfi_ActInst.getStaff_Id());
		}
		_SmProInfo.setInstance_Type(_Wfi_ActInst.getInstance_Type());
		if (_Wfi_ActInst.getHangup_Time() != null) {
			_SmProInfo.setHangUp_Time(formatter.format(_Wfi_ActInst
					.getHangup_Time()));
		}
		_SmProInfo.setActDef_Type(_Wfi_ActInst.getActdef_Type());

		Wfd_Actdef actdef = smActDef.GetActDefByID(_Wfi_ActInst.getActdef_Id());
		if (actdef != null) {
			_SmProInfo.setActDef_Type(actdef.getActdef_Type());
		}
	}

	/**
	 * 案卷直接驳回
	 * 
	 * @param actinst
	 *            String 活动实例ID
	 * @param staffId
	 *            String 驳回目标员工
	 * @throws Exception
	 * */
	public void OverRuleDirectly(String actinst, String staffId)
			throws Exception {
		if (actinst != null && !actinst.equals("")) {
			Wfi_ActInst actInst = commonDao.get(Wfi_ActInst.class, actinst);
			if (actInst != null) {
				Wfd_Actdef wfd_Actdef = commonDao.get(Wfd_Actdef.class,
						actInst.getActdef_Id());
				if (smActDef.IsStartAct(wfd_Actdef)) {
					throw new Exception("起始活动不能驳回");
				}
				if (smActDef.CanOverRuleActDef(wfd_Actdef)) {
					throw new Exception("活动定义为不能驳回！");
				}
			}
		}

	}

	/**
	 * 获得当前活动实例的前置活动实例
	 * 
	 * */
	public List<Wfi_ActInst> GetProActInsts(String actinst) {
		List<Wfi_ActInst> list = new ArrayList<Wfi_ActInst>();
		// Wfi_ActInst actInst=GetActInst(actinst);
		// Wfd_Actdef actdef=GetActDef(actinst);
		List<Wfi_Route> wfi_Routes = commonDao.findList(Wfi_Route.class,
				" NEXT_ACTINST_ID='" + actinst + "'");
		if (wfi_Routes != null && wfi_Routes.size() > 0) {
			for (Wfi_Route wfi_Route : wfi_Routes) {
				list.add(GetActInst(wfi_Route.getFromactinst_Id()));
			}
		}
		return list;
	}

	/**
	 * 驳回
	 * 
	 * @param route_ID
	 *            String 驳回路由
	 * @param actinstid
	 *            String 活动实例ID
	 * @param staffIDs
	 *            List<SmObjInfo> 驳回接收人员信息
	 * 
	 * */
	@Transactional
	public SmObjInfo PassBack(String proactdefid, String actinstid,
			List<SmObjInfo> staffIDs, String reson) {
		SmObjInfo returnInfo = new SmObjInfo();
		try {
			if (staffIDs != null && staffIDs.size() > 0) {
				// 获取前置活动定义
				Wfd_Actdef actdef = smActDef.GetActDefByID(proactdefid);
				if (staffIDs.size() == 1) {
					//判断被驳回员工是否有委托
					for(int i=0;i<staffIDs.size();i++){
						SmObjInfo info=staffIDs.get(i);
						List<Wfi_PassWork> passwork = smStaff.GetPassWorkByStaffID(info.getID());
						if (passwork != null && passwork.size()>0) {
							smAbnormal.AddPassWorkInfo( actdef,info);
						}
					}
					Wfi_ProInst proInst = smProInst.GetProInstByActInstId(actinstid);
					proInst.setInstance_Type(WFConst.Instance_Type.Instance_OverRule.value);
					proInst = smProInst.SetWeight(proInst);
					// 获取当前活动实例
					Wfi_ActInst curactInst = GetActInst(actinstid);
					// 验证当前实例是不是转出状态
					if (curactInst.getInstance_Type() == WFConst.Instance_Status.Instance_Passing.value) {
						returnInfo.setID("0");
						returnInfo.setName("已经驳回，请刷新列表");
						return returnInfo;
					}
					curactInst
							.setActinst_Status(WFConst.Instance_Status.Instance_Passing.value);
					curactInst.setActinst_End(new Date());
					curactInst.setToStaffName(staffIDs.get(0).getName());
					curactInst
							.setOperation_Type(WFConst.Operate_Type.PRODUCT_smallPicture.value
									+ "");
					Wfi_Route routeinstRoute = smRouteInst
							.CreateOverRuleRouteInst(proactdefid, actinstid,
									proInst.getProinst_Id());
					routeinstRoute.setActdef_Id(curactInst.getActdef_Id());
					routeinstRoute.setRoute_Name("驳回到"
							+ actdef.getActdef_Name());
					commonDao.update(proInst);
					commonDao.update(curactInst);
					Wfi_ActInst ProActInst = AddNewActInst(actdef, staffIDs,
							reson, proInst);
					ProActInst
							.setOperation_Type(WFConst.Operate_Type.PRODUCT_anglesPicture.value
									+ "");// 已经被驳回
					ProActInst
							.setInstance_Type(WFConst.Instance_Type.Instance_OverRule.value);
					ProActInst.setOverruleactinst_Ids(actdef.getActdef_Id());
					// 驳回可以在直接对员工的字段赋值
					ProActInst.setStaff_Id(staffIDs.get(0).getID());
					ProActInst.setStaff_Name(staffIDs.get(0).getName());
					//驳回后新的环节实例起始时间修正
					//ProActInst.setActinst_Start(getOldStartTime(actdef,proInst.getProinst_Id()));
					routeinstRoute.setNext_Actinst_Id(ProActInst
							.getActinst_Id());
					//TODO:驳回后更新proinst表中单签最新的活动信息：
					//更新环节实例中当前环节的信息：
					proInst.setActdef_Type(ProActInst.getActdef_Type());
					proInst.setActinst_Id(ProActInst.getActinst_Id());
					proInst.setIsApplyHangup(ProActInst.getIsApplyHandup());
					proInst.setStatusext(ProActInst.getStatusExt());
					proInst.setOperation_Type_Nact(ProActInst.getOperation_Type());
					proInst.setStaff_Id_Nact(ProActInst.getStaff_Id());
					proInst.setStaff_Name_Nact(ProActInst.getStaff_Name());
					proInst.setActinst_Status(ProActInst.getActinst_Status());
					proInst.setActinst_Willfinish(ProActInst.getActinst_Willfinish());
					proInst.setActinst_Name(ProActInst.getActinst_Name());
					proInst.setMsg(ProActInst.getMsg());
					proInst.setCodeal(ProActInst.getCodeal());
					proInst.setACTINST_START(ProActInst.getActinst_Start());
					proInst.setACTINST_END(ProActInst.getActinst_End());
					commonDao.update(proInst);
					commonDao.save(ProActInst);
					Wfi_NowActInst nowActInst = CreatNowActInst(ProActInst);
					commonDao.save(nowActInst);
					for (SmObjInfo smObjInfo : staffIDs) {
						Wfi_ActInstStaff actInstStaff = new Wfi_ActInstStaff();
						actInstStaff.setActinst_Id(ProActInst.getActinst_Id());
						actInstStaff.setStaff_Id(smObjInfo.getID());
						commonDao.save(actInstStaff);
					}
					routeinstRoute.setNext_Actinst_Id(ProActInst
							.getActinst_Id());
					commonDao.save(routeinstRoute);
					DelNowActinst(actinstid, curactInst.getStaff_Id());
					//驳回之后
					afterPassBackConcurrentActivity(proactdefid ,curactInst);
					returnInfo.setID(nowActInst.getActinst_Id());
					returnInfo.setName("驳回");
					returnInfo.setDesc("成功驳回到" + actdef.getActdef_Name());
				}

				commonDao.flush();
			}
		} catch (Exception e) {
		}
		return returnInfo;
	}
	/**
	 * 并发活动有一个驳回之后其他的实例信息也要相应的
	 * 当前活动的路由：判断路由的定义formactdef的类型是否是并发活动，
	 * 如果是需要经所有并发路由上已经产生的件全部更新状态，聚合活动之后分支，分支之后聚合。
	 * @date   2016年11月14日 下午3:06:29
	 * @author JHX
	 */
	public void afterPassBackConcurrentActivity(String targetActdefid ,Wfi_ActInst curactinst){
		String actdefidtemp = "";
		if(curactinst!=null){
		      String  actdefid = curactinst.getActdef_Id();
		      if(!StringHelper.isEmpty(actdefid)){
		    	  //路由定义只有一个，当前路由不可能为聚合活动
		    	  List<Wfd_Route> routedefs = smRouteDef.GetPreRoute(actdefid);
		    	  Wfd_Actdef curactdef = smActDef.GetActDefByID(actdefid);
		    	  Wfd_Actdef targetactdef = smActDef.GetActDefByID(targetActdefid);
		    	  if(routedefs!=null){
		    		  if(curactinst.getActdef_Type()
			    			  .equals(WFConst.ActDef_Type.Activity.value+"")
			    			  &&routedefs.size()>1){
			    		  //分支后的活动
			    	  }else if(curactinst.getActdef_Type()
			    			  .equals(WFConst.ActDef_Type.ActivityMerge.value+"")){
			    		  //聚合活动跳跃驳回到并发路由：
						  List<Wfi_ActInst> list3 = commonDao.getDataList(Wfi_ActInst.class,
	    							 " select * from "+Common.WORKFLOWDB+
	    							 "Wfi_ActInst where FROMACTINSTID='"+curactinst.getFromActinstid()+"'");
						  if(list3!=null&&list3.size()>0){
							  Wfi_ActInst actinst = null;
							  for(int i=0,j=list3.size();i<j;i++){
								  actinst=list3.get(i);
								  actinst.setActinst_Status(WFConst.Instance_Status.cancelAcinst.value);
								  commonDao.saveOrUpdate(actinst);
							  }
						  }
			    	  } else{
			    		  if(routedefs.size()>0){
			    			  String currStaff = smStaff.getCurrentWorkStaffID();
			    			  Wfd_Route route =   routedefs.get(0);
			    			  actdefidtemp = route.getActdef_Id();
			    			  if(!StringHelper.isEmpty(actdefidtemp)){
			    				  Wfd_Actdef preactdef =  commonDao.get(Wfd_Actdef.class, actdefidtemp);
			    				  if(preactdef.getActdef_Type()!=null&&
			    						  preactdef.getActdef_Type().equals(
			    						WFConst.ActDef_Type.ConditionAnd.value+"")){
			    					  //说明当前或动属于并发出来的分支环节实例，找到最新的并发活动实例
			    					  List<Wfi_ActInst> lists = commonDao.getDataList(Wfi_ActInst.class,
			    							 " select * from "+Common.WORKFLOWDB+
			    							 "Wfi_ActInst where actdef_id='"+preactdef.getActdef_Id()+
			    							 "' and proinst_id='"+curactinst.getProinst_Id()+"' order by actinst_end desc");
			    					  if(lists!=null&&lists.size()>0){
			    						  Wfi_ActInst inst = lists.get(0);
			    						  String instid = inst.getActinst_Id();
			    						  //更新所有流程(fromactinstid为并发路由的最新环节实例)
			    						  List<Wfi_ActInst> lists2 = commonDao.getDataList(Wfi_ActInst.class,
					    							 " select * from "+Common.WORKFLOWDB+
					    							 "Wfi_ActInst where FROMACTINSTID='"+instid+"'");
			    						  if(lists2!=null&&lists2.size()>0){
			    							  Wfi_ActInst actinst = null;
			    							  for(int i=0,j=lists2.size();i<j;i++){
			    								  actinst=lists2.get(i);
			    								  actinst.setActinst_Status(WFConst.Instance_Status.cancelAcinst.value);
			    								  DelNowActinst(actinst.getActinst_Id(), currStaff);
			    								  commonDao.saveOrUpdate(actinst);
			    							  }
			    						  }
			    						  //删除单个活动转出后生成的聚合活动
			    						  List<Wfi_ActInst> actinstList = commonDao.findList(Wfi_ActInst.class, 
			    								  " proinst_id = '" + curactinst.getProinst_Id() + 
			    								  "' and Passedroute_Count is not null"
			    								  );
			    						  for(Wfi_ActInst actinst : actinstList){
			    							  commonDao.delete(actinst);
			    							  DelNowActinst(actinst.getActinst_Id(),currStaff);
			    						  }
			    						  
			    					  }
			    				  }
			    			  }
				    	  }
			    	  }
		    	  }
		      }
		}
	}
    /**
     * @author dff
     * @DATE:2016-10-19
     * 根据环节实例获取该项目最初在该环节的起始时间
     * **/
	public Date getOldStartTime(Wfd_Actdef actdef,String proinstid){
		Date startTime=new Date();
		List<Wfi_ActInst> instlist=commonDao.getDataList(Wfi_ActInst.class,
				"select * from " + Common.WORKFLOWDB
				+ "WFI_ACTINST where proinst_id='"
				+ proinstid+"' and actdef_id='"+actdef.getActdef_Id()+"'");
		for(Wfi_ActInst act : instlist){
			if(act.getActinst_Start().before(startTime)){
				startTime=act.getActinst_Start();
			}
		}
		return startTime;
	}
	public List<Wfi_ActInst> GetActInstsbyactactid(String actinstid) {
		if(!StringHelper.isEmpty(actinstid)){
			return GetActInstsbyproinstid(GetActInst(actinstid).getProinst_Id());
		}
		return null;
	}
    /**
     * @author JHX
     * @DATE:2016-08-11
     * 根据环节实例id，获取环节信息，如果转出未办理：设置代办人员
     * **/
	public List<Wfi_ActInst> GetActInstsbyproinstid(String proinstid) {
		List<Wfi_ActInst> resultlist = new ArrayList<Wfi_ActInst>();
		List<Wfi_ActInst> actInsts = null;
		if (proinstid != null && !proinstid.equals("")) {
				actInsts = commonDao.getDataList(
						Wfi_ActInst.class,
						"select * from " + Common.WORKFLOWDB
								+ "WFI_ACTINST where proinst_id='"
								+ proinstid
								+ "' and actinst_status <> 16 order by actinst_end");
		}
		//循环判断；如果环节没有进行办理，设置办理人员()
		if(actInsts.size()>0){
			Wfd_Actdef wif_def = null;
			Wfi_ActInstStaff actinststaff = null;
			for(int i=0;i<actInsts.size();i++){
				Wfi_ActInst actint = actInsts.get(i);
				String staffname = actint.getStaff_Name();
				String actdefid = actint.getActdef_Id();
				wif_def = commonDao.get(Wfd_Actdef.class, actdefid);
				Integer type = null;
				if(wif_def!=null){
					type = wif_def.getPassover_Type();
				}
				if((staffname==null||staffname.equals(""))&&type!=null){
					//个人
					if(type==1){
						List<Wfi_ActInstStaff> liststaff =commonDao.getDataList(Wfi_ActInstStaff.class,
								"select * from "+Common.WORKFLOWDB+"Wfi_ActInstStaff where actinst_id ='"+actint.getActinst_Id()+"'");
						if(liststaff!=null){
							String names = " ";
							if(liststaff.size()>0){
								for(int h=0;h<liststaff.size();h++){
									names+=liststaff.get(h).getStaff_Name()+"  ";
								}
							}
							actint.setStaff_Name("待办:"+names);
						}
					}else if(type==2){
						actint.setStaff_Name("待办:"+wif_def.getRole_Name());     
						//角色
					}else if(type==3){
						//部门
						actint.setStaff_Name("待办:"+wif_def.getActdef_Dept()); 
					}
				}
				resultlist.add(actint);
			}
		}
		return resultlist;
	}
	
	
	/**
	 *@author:JHX
	 *@DATE:2016-07-19
	 *根据当前环节实例查询流程实例当前的所在的环节
	 * */
	
	public Wfi_ActInst getCurrentHandingAct(String actinstid){
		/*		Wfi_ActInst return_actInst=null;
		Wfi_ActInst actInst = commonDao.get(Wfi_ActInst.class, actinstid);
		String sql = "";
		if(actInst!=null){
			sql="select\n" +
					"  ACTINST_ID,\n" + 
					"  ACTDEF_ID,\n" + 
					"  PROINST_ID,\n" + 
					"  STAFF_ID,\n" + 
					"ACTINST_START,\n" + 
					"ACTINST_END,\n" + 
					"DEPT_CODE,\n" + 
					"ACTINST_WILLFINISH,\n" + 
					"OPERATION_TYPE,\n" + 
					"STAFF_IDS,\n" + 
					"ACTINST_MSG,\n" + 
					"ACTINST_NAME,\n" + 
					"PRODEF_ID,\n" + 
					"ACTINST_STATUS,\n" + 
					"INSTANCE_TYPE,\n" + 
					"ACTDEF_TYPE,\n" + 
					"OVERRULEACTINST_IDS,\n" + 
					"HANGPAUSETIME,\n" + 
					"PASSOVERDESC,\n" + 
					"ASKDELAY_DAYS,\n" + 
					"HUNGUP_DATE,\n" + 
					"PASSEDROUTE_COUNT,\n" + 
					"STATUS,\n" + 
					"STAFF_NAME,\n" + 
					"ISTRANSFER,\n" + 
					"TRANSFER_TIME,\n" + 
					"HANGUP_TIME,\n" + 
					"HANGUP_STAFF_NAME,\n" + 
					"HANGUP_STAFF_ID,\n" + 
					"HANGDOWM_TIME,\n" + 
					"MSG,\n" + 
					"UPLOADFILE,\n" + 
					"CODEAL\n" + 
					" from (\n" + 
					"  select\n" + 
					"  row_number() over (partition by a.proinst_id, a.actdef_id order BY a.actinst_start desc) rn,a.*\n" + 
					"from "+Common.WORKFLOWDB+" wfi_actinst a where proinst_id='"+actInst.getProinst_Id()+"')\n" + 
					"where rn =1 order by actinst_start desc";
			List<Wfi_ActInst> listActInst = commonDao.getDataList(Wfi_ActInst.class, sql);
			
			if(listActInst!=null&&listActInst.size()>0){
				return_actInst=listActInst.get(0);
			}
		}*/
		String actinst = smProInst.GetProInstByActInstId(actinstid).getActinst_Id();
		if(!StringHelper.isEmpty(actinst)){
			return GetActInst(actinst);
		}
		return null;
	}

	/**
	 * 通过活动实例ID获取上一步的活动实例定义
	 * 
	 * @param actinstid
	 *            String 活动实例ID
	 * @return 上一步活动实例
	 * */
	@SuppressWarnings("unused")
	public Wfd_Actdef GetProActInstByactinstid(String actinstid) {
		Wfd_Actdef actdef = null;
		List<Wfi_Route> routeInstsList = smRouteInst
				.GetRouteInstsByActinstID(actinstid);
		if (routeInstsList != null) {
			String proActinstid = routeInstsList.get(0).getFromactinst_Id();
		}
		return actdef;
	}

	/**
	 * 获取转办人员
	 * */
	public List<SmObjInfo> GetTurnStaff(String actinstid) {
		List<SmObjInfo> list = new ArrayList<SmObjInfo>();
		StringBuilder abBuilder = new StringBuilder();
		abBuilder.append("select * from ");
		abBuilder.append(Common.WORKFLOWDB);
		abBuilder.append("Wfi_ActInstStaff where actinst_id='");
		abBuilder.append(actinstid);
		abBuilder.append("' and Staff_id<>'");
		abBuilder.append(smStaff.getCurrentWorkStaffID());
		abBuilder.append("'");
		List<Wfi_ActInstStaff> actstaffActInstStaff = commonDao.getDataList(
				Wfi_ActInstStaff.class, abBuilder.toString());
		if (actstaffActInstStaff != null && actstaffActInstStaff.size() > 0) {
			for (Wfi_ActInstStaff wfi_ActInstStaff : actstaffActInstStaff) {
				SmObjInfo info = new SmObjInfo();
				info.setID(wfi_ActInstStaff.getStaff_Id());
				info.setName(smStaff.GetStaffName(wfi_ActInstStaff
						.getStaff_Id()));
				info.setDesc("Staff");
				list.add(info);
			}
		} else {
			return GetTurnStaffEx(actinstid);
		}
		return list;
	}

	/**
	 * 转办人员，按照角色寻找
	 * */
	public List<SmObjInfo> GetTurnStaffEx(String actinstid) {
		List<SmObjInfo> list = new ArrayList<SmObjInfo>();
		Wfd_Actdef actdef = GetActDef(actinstid);
		if (actdef != null) {
			String actsteffid = smStaff.getCurrentWorkStaffID();
			String roleid = actdef.getRole_Id();
			List<User> users = roleService.findUsersByRoleId(roleid);
			String acceptid=smProInst.GetProInstByActInstId(actinstid).getStaff_Id();
			String areaid= userService.findById(acceptid).getAreaCode();
			if (users != null && users.size() > 0) {
				for (User u : users) {
					if (!u.getId().equals(actsteffid)&&u.getAreaCode().equals(areaid)) {
						SmObjInfo info = new SmObjInfo();
						info.setID(u.getId());
						info.setName(u.getUserName());
						info.setDesc("Staff");
						list.add(info);
						Wfi_ActInstStaff actstaff = new Wfi_ActInstStaff();
						actstaff.setActstaffid(Common.CreatUUID());
						actstaff.setActinst_Id(actinstid);
						actstaff.setStaff_Id(u.getId());
						actstaff.setRole_Id(roleid);
						commonDao.save(actstaff);
					}
				}
				commonDao.flush();
			}
		}

		return list;
	}

	// 计算活动进度
	public String GetProProcess(String proinst) {
		String resultString = null;
		smActDef.GetActDefByProdefID(proinst);
		return resultString;
	}

	/**
	 * 通过状态获取符合条件的非本部门的活动
	 * */
	public String getActinstByStatus(String dept_code,
			Boolean isLoadHavaDoneBoolean) {
		String istransferString = " ";
		if (!isLoadHavaDoneBoolean) {
			istransferString = " and istransfer is null";
		} else {

		}
		String sql = " actinst_id in ("
				+ "select {0}wfi_actinst.actinst_id from {0}wfi_actinst,"
				+ "(select proinst_id,actinst_id from {0}wfi_actinst where actinst_status in({1},{2}) and dept_code<>''{4}'')  tab1 "
				+ " where {0}wfi_actinst.proinst_id =tab1.proinst_id "
				+ "and  actdef_id=(select actdef_id from {0}Wfi_Route where next_actinst_id=tab1.actinst_id  ) "
				+ "and actinst_status={3} {5})";

		sql = MessageFormat.format(sql, Common.WORKFLOWDB,
				WFConst.Instance_Status.Instance_NotAccept.value,
				WFConst.Instance_Status.Instance_doing.value,
				WFConst.Instance_Status.Instance_Passing.value, dept_code,
				istransferString);
		StringBuilder sb = new StringBuilder();
		/*
		 * sb.append(""); String sql=
		 * "select  * from WFI_ACTINST where dept_code='0414597679d24f7dbcd51e5a518869ad' and actinst_status=3 and istransfer is null order by actinst_end desc"
		 */return sql;

	}

	// 获取部门办理的活动实例
	public List<Map> GetActinstNameByStaffId(List<User> users) {

		String sql = "'0'";
		for (User user : users) {
			sql += ",'" + user.getId() + "'";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct(actinst_name),staff_name from "
				+ Common.WORKFLOWDB + "wfi_actinst  where staff_id in (");
		sb.append(sql);
		sb.append(")");
		List<Map> list = commonDao.getDataListByFullSql(sb.toString());
		return list;

	}

	public List<Map> GetAllActDefName() {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct(actdef_name) from " + Common.WORKFLOWDB
				+ "wfd_actdef");
		List<Map> list = commonDao.getDataListByFullSql(sb.toString());
		return list;

	}

	// 获取部门办理的活动实例
	public List<Map> GetProinstNameByStaffId(List<User> users) {
		String sql = "'0'";
		for (User user : users) {
			sql += ",'" + user.getId() + "'";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct( pro.prodef_name),act.staff_name,act.actinst_name from "
				+ Common.WORKFLOWDB
				+ "wfi_actinst act left join "
				+ Common.WORKFLOWDB
				+ "wfi_proinst pro on pro.proinst_id=act.proinst_id where act.staff_id in ( ");
		sb.append(sql);
		sb.append(") order by act.staff_name,act.actinst_name");
		List<Map> list = commonDao.getDataListByFullSql(sb.toString());
		return list;

	}

	// 获取部门办理的活动实例
	public List<Map> GetProinstNameByStaffId(List<User> users, String status) {
		String sql = "'0'";
		for (User user : users) {
			sql += ",'" + user.getId() + "'";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct( pro.prodef_name),act.staff_name,act.actinst_name from "
				+ Common.WORKFLOWDB
				+ " wfi_actinststaff actstaff inner join "+Common.WORKFLOWDB+"wfi_actinst act on actstaff.actinst_id = act.actinst_id left join "
				+ Common.WORKFLOWDB
				+ "wfi_proinst pro on pro.proinst_id=act.proinst_id where act.staff_id in ( ");
		sb.append(sql);
		sb.append(") or ( actstaff.staff_id in ("+sql+") and act.staff_id is null )and act.actinst_status in (" + status
				+ ") order by act.staff_name,act.actinst_name");
		List<Map> list = commonDao.getDataListByFullSql(sb.toString());
		return list;

	}
	//验证项目是否可以撤回
	public SmObjInfo ValidateWithDraw(String actistid){
		if(actistid!=null&&!actistid.equals("")){
			Wfi_ActInst inst=GetActInst(actistid);
		   	//查找下一个
			
		}
		return null;
	}
	
	//获取聚合活动在流程下的待办活动实例
	public Wfi_ActInst GetMergerInst(String proinstid){
		String sql="select * from "+Common.WORKFLOWDB+"wfi_actinst where proinst_id='"+proinstid+"'";
		sql+=" and actinst_status="+WFConst.Instance_Status.Instance_NotAccept.value;
		sql+=" and actdef_type='"+WFConst.ActDef_Type.ActivityMerge.value+"'";
		List<Wfi_ActInst> inst=commonDao.getDataList(Wfi_ActInst.class, sql);
		if(inst!=null&&inst.size()>0){
			return inst.get(0);
		}
		else{
			return null;
		}
	}
	
	/**
	 * @author JHX
	 * 更新流程实例：申请状态更改为1
	 * */
	public void modifyWfi_ActInst(String actinstid,int type){
		Wfi_ActInst wfiactinst = commonDao.get(Wfi_ActInst.class, actinstid);
		Wfi_ProInst proinst = smProInst.GetProInstByActInstId(actinstid);
		if(wfiactinst!=null){
			wfiactinst.setIsApplyHandup(type);
			if(proinst!=null){
				proinst.setIsApplyHangup(type);
			}
			commonDao.update(proinst);
			commonDao.update(wfiactinst);
			commonDao.flush();
		}
		
	}
	/**
	 * @author dff
	 * 获取和该环节实例同流程同环节定义的所有环节实例
	 * @param actinstid
	 * */
	public List<Wfi_ActInst> getActInstForCommonActdef(String actinstid){
		Wfi_ActInst actinst=GetActInst(actinstid);
		String proinstId=actinst.getProinst_Id();
		String actdefid=actinst.getActdef_Id();
		List<Wfi_ActInst> list=commonDao.getDataList(Wfi_ActInst.class,
				"select * from "+Common.WORKFLOWDB+"WFI_ACTINST where Actdef_Id='"+
				actdefid+"' and Proinst_Id='"+proinstId+"' and ACTINST_ID!='"+actinstid+"' order by ACTINST_END");
		return list;
	}
	//根据actinstids获取actinst
	public List<Wfi_ActInst> GetActInstsByActinstids(String actinstids){
		return commonDao.getDataList(Wfi_ActInst.class, "select * from " + Common.WORKFLOWDB + "Wfi_ActInst where ActInst_Id in (" + actinstids + ")");
	}
	
	
}
