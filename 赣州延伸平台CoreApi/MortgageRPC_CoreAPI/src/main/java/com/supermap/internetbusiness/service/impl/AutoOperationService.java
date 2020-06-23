package com.supermap.internetbusiness.service.impl;

import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.T_CONFIG;
import com.supermap.realestate.registration.service.ProjectService;
import com.supermap.realestate.registration.service.impl.ProjectServiceImpl;
import com.supermap.realestate.registration.tools.HttpRequestTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue.YXBZ;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.web.DBController;
import com.supermap.wisdombusiness.framework.model.Role;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.service.RoleService;
import com.supermap.wisdombusiness.utility.Helper;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.*;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.SmProInfo;
import com.supermap.wisdombusiness.workflow.service.common.WFConst;
import com.supermap.wisdombusiness.workflow.service.wfd.*;
import com.supermap.wisdombusiness.workflow.service.wfi.SmAbnormal;
import com.supermap.wisdombusiness.workflow.service.wfi.SmActInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.service.wfm.SendProjectService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProInstService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProSPService;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.*;

@Service("autoOperationService")
public class AutoOperationService {
	@Autowired
	private AutoSmActInst autoSmActInst;
	@Autowired
	private SmProInst smProInst;
	@Autowired
	private SmStaff smStaff;
	@Autowired
	private SmAbnormal smAbnormal;
	@Autowired
	private SmRouteDef smRouteDef;
	@Autowired
	private SmRoleDef smRoleDef;
	@Autowired
	private SmActDef smActDef;
	@Autowired
	private DBController dbController;

	@Autowired
	private SmProDef smProDef;
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private SendProjectService sendProjectService;
	@Autowired
	private ProjectService projectService;


	@Autowired
	private SmProInstService smProInstService;
	@Autowired
	private ProjectServiceImpl projectServiceImpl;

	@Autowired
	private RoleService roleService;
	@Autowired
	private SmHoliday smHoliday;
	@Autowired
	private SmProSPService smProSPService;
	/**
	 * 活动转出前
	 *
	 * */
	public boolean BeforePassOver() {

		return true;
	}

	public SmObjInfo PassOver(String routeid, String actinstid, List<SmObjInfo> staffids, String staffid, String Msg) {
		return PassOver(routeid, actinstid, staffids, staffid, Msg, false);
	}

	/**
	 * 案卷的转出
	 *
	 * @param routeid
	 *            String 路由主键
	 * @param staffids
	 *            SmObjInfo 转出人员集合
	 * @param staffid
	 *            String 操作人ID
	 * @param Msg
	 *            String 转出附言
	 * */
	@SuppressWarnings("unused")
	public SmObjInfo PassOver(String routeid, String actinstid, List<SmObjInfo> staffids, String staffid, String Msg, boolean more) {
		SmObjInfo smObjInfomsg = new SmObjInfo();
		String proinstid = smProInst.GetProInstIDByActInstId(actinstid);
		Wfi_ActInst inst = autoSmActInst.GetActInst(actinstid);
		
		Wfd_Actdef nextactdef = smActDef.GetNextActDef(routeid);//判断是否是按角色转出
		if(nextactdef.getPassover_Type().equals(WFConst.PassOverType.Role.value)){
			//获取角色下的人员信息
			staffids  = new ArrayList<SmObjInfo>();//将原来存进的信息信息清除，再根据角色进行赋值；
			String arearange = smStaff.GetCurrentAreaCode(staffid);
			if(nextactdef!=null){
				String turnoutrange = nextactdef.getTurnOutRange();
				if(turnoutrange!=null&&!turnoutrange.trim().equals("")){
					if(turnoutrange.equals("0")){
						arearange=arearange.substring(0,2)+"0000";
					}else if(turnoutrange.equals("1")){
						arearange=arearange.substring(0,4)+"00";
					}
				}
			}
			List<User> users = roleService.findUsersByRoleIdAndCode(nextactdef.getRole_Id(), arearange);
			if (users != null && users.size() > 0) {
				for (User user : users) {
					SmObjInfo objInfo = new SmObjInfo();
					objInfo.setID(user.getId());// 设置staffid
					objInfo.setName(user.getUserName());// 设置staffName
					staffids.add(objInfo);
				}
				
			}
			
		}
		

		if (staffid == null || staffid.equals("")) {
			staffid = inst.getStaff_Id();
		}
		//在受理转出时推送受理信息到共享登记库
		PUSHTOGXDJK(actinstid);
		if (inst != null && !more) {
			if (inst.getActinst_Status() == WFConst.Instance_Status.Instance_Passing.value) {
				smObjInfomsg.setID(WFConst.Instance_Status.Instance_Passing.value + "");
				smObjInfomsg.setDesc("案卷已经转出,不能重复流转");
				return smObjInfomsg;
			}
		}
		if (routeid.equals("")) {
			// 项目办结
			try {
				Wfi_ProInst proinst = smProInst.GetProInstByActInstId(actinstid);
				projectService.finishProject(proinst.getFile_Number());
			} catch (Exception e) {

			}
			smProInst.DoneProInst(actinstid, staffid);
			// DelActinstStaff(actinstid, staffid);
			smObjInfomsg.setID(WFConst.Instance_Status.Instance_havedone.value + "");
			smObjInfomsg.setName(actinstid);
			smObjInfomsg.setDesc("项目已经办结");
			// //YwLogUtil.addYwLog("流程转出-项目已经办结",
			// ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
			return smObjInfomsg;

		}
		// 正常流转
		for (SmObjInfo smObjInfo : staffids) {
			String id = smObjInfo.getID();
			if (id != null) {
				// 检查是否有委托
				List<Wfi_PassWork> staffIDs = smStaff.GetPassWorkByStaffID(id);
				if (staffIDs != null && staffIDs.size()>0) {
					Wfd_Actdef def=smActDef.GetNextActDef(routeid);
					smAbnormal.AddPassWorkInfo(def,smObjInfo);
				}
			}
		}
		//去重
		List<SmObjInfo> staffs = new ArrayList<SmObjInfo>();
		List<String> mark = new ArrayList<String>();
		for(SmObjInfo smObjInfo : staffids){
			if(!mark.contains(smObjInfo.getID())){
				staffs.add(smObjInfo);
				mark.add(smObjInfo.getID());
			}
		}
		// 调用转出
		List<Wfi_ActInst> actidlist = autoSmActInst.PassOver(staffid, routeid, actinstid, staffs, Msg);
		if (actidlist != null && actidlist.size() > 0) {
			for (Wfi_ActInst wfi_ActInst : actidlist) {
				String instid = wfi_ActInst.getActinst_Id();
				// 是否派件
				sendProject(instid);
				//是否自动锁定
				if(null!=autoSmActInst.GetActDef(instid).getAutoLock()&&autoSmActInst.GetActDef(instid).getAutoLock()>0){
					delLockProject(instid,"转出自动锁定","2");
				}
			}

			smObjInfomsg.setName(actinstid);
			smObjInfomsg.setID(WFConst.Instance_Status.Instance_Success.value + "");
			smObjInfomsg.setDesc("转出成功");
		}
		return smObjInfomsg;
	}

	/**
	 * 根据流程定义设置案卷派件
	 *
	 * @param newactinstid
	 */
	private void sendProject(String newactinstid) {
		Wfd_Actdef actdef = autoSmActInst.GetActDef(newactinstid);
		if (actdef != null) {
			int sendtype = actdef.getSend_Type();
			if (sendtype == WFConst.Send_Type.Average.value) {
				sendProjectService.AverageSendProject(newactinstid);
			} else if (sendtype == WFConst.Send_Type.LessFirst.value) {
				sendProjectService.LessSendProject(newactinstid);

			} else if (sendtype == WFConst.Send_Type.LessWorkFirst.value) {
				sendProjectService.LessWorkSendProject(newactinstid);
			} else if (sendtype == WFConst.Send_Type.Efficient.value) {
				sendProjectService.EfficientSendProject(newactinstid);
			}
		}

	}

	/**
	 * 活动转出后
	 *
	 * */
	public SmObjInfo AfterPassOver(String actinstid) {
//		System.out.println("1");
		boolean issuccess = false;
		System.out.println(actinstid);
		// 发送MQ消息
		SmObjInfo smObjInfomsg = new SmObjInfo();
		if (actinstid != null && !actinstid.equals("")) {
			try {
				// 当调用类已经注入，且共享信息已开启的时候，才会发送共享信息
				Wfi_ActInst actinset = autoSmActInst.GetActInst(actinstid);
				T_ROLEGROUP tgroup = new T_ROLEGROUP();
				//staff_id 角色id
				String staff_id = actinset.getStaff_Id();
				String sql = "userid = '"+staff_id+"'";
				List<RT_USERROLE> list = commonDao.findList(RT_USERROLE.class, sql);
				if(list!=null && list.size()>0){
				  String roleid = list.get(0).getROLEID();
				  T_ROLE trole = commonDao.get(T_ROLE.class, roleid);
				  String groupid = trole.getGROUPID();
				  tgroup = commonDao.get(T_ROLEGROUP.class, groupid);

				  System.out.println(tgroup.getGROUPNAME());
				  System.out.println(tgroup.getREMARK());
				}
				//鹰潭发短信  王帅
				String ac_name =ConfigHelper.getNameByValue("XZSEND");//发送短信环节名称
				String SENDMESSAG =ConfigHelper.getNameByValue("SENDMESSAGE");//是否发送短信

				if(SENDMESSAG.equals("1")){
					Wfi_ProInst inst = smProInst.GetProInstByActInstId(actinstid);
					List<BDCS_XMXX> xmxx = commonDao.findList(BDCS_XMXX.class, " PROJECT_ID='" + inst.getFile_Number() + "'");
					//证书证明需要缮证后，注销类登簿后发送短信
					System.out.print(actinset.getActinst_Name());
					if(actinset.getActinst_Name().contains(ac_name)){
						try {
							issuccess = smProInstService.Senddx(inst.getFile_Number(),inst.getProject_Name(),tgroup.getGROUPNAME(),tgroup.getREMARK());
						  } catch (Exception e) {
						}
				    }
		        }
				if(issuccess == true){
					smObjInfomsg.setID(UUID.randomUUID().toString());
					smObjInfomsg.setName(actinset.getActinst_Name());
					smObjInfomsg.setDesc("短信发送成功");
				}
//				System.out.println("2");

				// 专版后更改流程实力的operation_Type 为空：
				Wfi_ProInst proinst = smProInst.GetProInstByActInstId(actinstid);
				if (proinst.getOperation_Type() != null) {
					if (proinst.getOperation_Type().equals(WFConst.Operate_Type.WithDraw.value + "")) {
						proinst.setOperation_Type(WFConst.Operate_Type.WithDraw.value + "");
						commonDao.update(proinst);
						commonDao.flush();
					}
				}

			} catch (Exception ex) {
			}
		}
		return smObjInfomsg;
	}

	/*
	 * 把登记系统受理转出推送数据到共享登记库的方法独立出来，避免批量转出导致未推送在办数据
	 * 2017年1月5日 19:46:58 buxiaobo
	 * */
	public void PUSHTOGXDJK(String actinstid){
		// 当调用类已经注入，且共享信息已开启的时候，才会发送共享信息
		Wfi_ActInst actinset = autoSmActInst.GetActInst(actinstid);
		if(ConfigHelper.getNameByValue("WRITETOZJK")!=null&&!"".equals(ConfigHelper.getNameByValue("WRITETOZJK"))){
			int zjk = Integer.parseInt(ConfigHelper.getNameByValue("WRITETOZJK"));
			// 推送到中间库,0不推送，1中间库模式推送（受理和登薄都推）,2MQ模式推送,3接口模式推送（受理和登薄都推）,4中间库模式（仅登薄推）,5接口模式（仅登薄推）,6中间库模式（受理+登薄+缮证）,7接口模式（受理+登薄+缮证）,8中间库模式推送（受理和归档都推）,9接口模式推送（受理和归档都推）
			if (dbController != null && zjk > 0) {
//				System.out.println("3");
				System.out.println(zjk);
				if (actinset != null) {
//					System.out.println("4");
					//受理环节
					if (actinset.getActdef_Type().equals("1010")&& (zjk==1||zjk==3||zjk==6||zjk==7||zjk==8||zjk==9||zjk==21)) {
//						System.out.println("5");
						System.out.println(actinset.getActdef_Type());
						Wfi_ProInst inst = smProInst.GetProInstByActInstId(actinstid);
						try {
							dbController.SendMsg(inst.getFile_Number(), "1");
						} catch (Exception e) {
						}
					}
					else if (actinset.getActinst_Name().equals("缮证")&& (zjk==6||zjk==7)) {
						//缮证环节
						System.out.println(actinset.getActdef_Type());
						Wfi_ProInst inst = smProInst.GetProInstByActInstId(actinstid);
						try {
							dbController.SendMsg(inst.getFile_Number(), "3");
						} catch (Exception e) {
						}
					}
					else if (IsNextActdefArchives(actinset.getActinst_Id())&& (zjk==8||zjk==9)) {//actinset.getActdef_Type().equals("5010")
						//下一个环节为归档环节时
//						System.out.println("5");
						System.out.println(actinset.getActdef_Type());
						Wfi_ProInst inst = smProInst.GetProInstByActInstId(actinstid);
						try {
							dbController.SendMsg(inst.getFile_Number(), "4");
						} catch (Exception e) {
						}
					}
					if(actinset.getActinst_Name().equals("缮证")){
						Wfi_ProInst inst = smProInst.GetProInstByActInstId(actinstid);
						try {
							dbController.SendSmsMsg(inst.getFile_Number(), "localhost:8080");
						} catch (Exception e) {

						}
					}
				}
			}
		}
	}
	private void DelActinstStaff(String actinstid, String staff_id) {
		int a = commonDao.deleteQuery("delete " + Common.WORKFLOWDB + "wfi_actinststaff where actinst_id='" + actinstid + "' and staff_id<>'" + staff_id + "'");
		commonDao.flush();
	}

	/**
	 * 判断是否可以驳回
	 *
	 * @param actinstid
	 *            String 活动实例ID
	 * */
	public SmObjInfo DirectOverRule(String actinstid) {
		SmObjInfo smObjInfo = new SmObjInfo();
		try {
			Wfi_ActInst actInst = autoSmActInst.GetActInst(actinstid);
			Wfd_Actdef actdef = autoSmActInst.GetActDef(actinstid);
			if (actInst == null) {
				throw new Exception("不存在ID为" + actinstid + "的活动实例。");
			}
			if (smActDef.IsStartAct(actdef)) {// 是否起始活动
				smObjInfo.setID(WFConst.SmActInstOverRuleReturnType.CannotOverRuleStartAct.value + "");
				smObjInfo.setDesc("初始活动不允许驳回");
			}
			if (smActDef.CanOverRuleActDef(actdef)) {
				smObjInfo.setID(WFConst.SmActInstOverRuleReturnType.CannotOverRuleActDef.value + "");
				smObjInfo.setDesc("活动设置不可直接驳回");
			}
			if (actInst.getInstance_Type() == WFConst.Instance_Status.Instance_Passing.value) {
				smObjInfo.setID(WFConst.Instance_Status.Instance_Passing.value + "");
				smObjInfo.setName("已经驳回，请刷新列表");
			}

			// 判断是否可以驳回，驳回检查配置
			List<Wfd_Route> routedefList = smRouteDef.GetPreRouteByActinstID(actinstid);
			if (routedefList != null && routedefList.size() > 0) {
				Wfd_Route route = routedefList.get(0);
				String Project_ID = smProInst.GetProInstByActInstId(actinstid).getFile_Number();
				Map<String, String> map = smRouteDef.ValiteRoute_Reject(route.getRoute_Id(), Project_ID);
				if (map != null) {
					if (!("1").equals(map.get("BM"))) {
						smObjInfo.setName(map.get("BM"));
						smObjInfo.setDesc(map.get("MS"));
						smObjInfo.setID(route.getRoute_Id());
					}
				}

				// 前一个活动是否设置了拦截驳回
				String preactdefid = route.getActdef_Id();
				if (preactdefid != null && !preactdefid.equals("")) {
					Wfd_Actdef predef = smActDef.GetActDefByID(preactdefid);
					if (predef != null && predef.getRejectpassback() == 1) {
						smObjInfo.setID("1");
						smObjInfo.setDesc("不允许继续向前驳回");
					}
				}

			}

		} catch (Exception e) {
		}
		return smObjInfo;
	}

	/**
	 * 案卷驳回
	 *
	 * @param
	 * */
	public SmObjInfo OverRuleDirectly(String proactdefid, String actinstid, List<SmObjInfo> Actinfo, String reson) {
		return autoSmActInst.PassBack(proactdefid, actinstid, Actinfo, reson);

	}

	/**
	 * 通过实例ID获取转办人员
	 *
	 * */
	public List<SmObjInfo> GetTurnStaffByActinstID(String actinstid) {
		List<SmObjInfo> list = null;
		if (actinstid != null && !"".equals(actinstid)) {
			list = new ArrayList<SmObjInfo>();
			SmObjInfo info = new SmObjInfo();
			info.setName("转给");
			info.setID(actinstid);
			info.setDesc("Desc");
			info.setChildren(smStaff.GetTurnStaffByActInstID(actinstid));
			list.add(info);
		}
		return list;
	}

	/**
	 * 案卷转办
	 * */
	public SmObjInfo TurnProjectByactinstid(String actinstid, String staffid, String staffname) {
		SmObjInfo smInfo = new SmObjInfo();
		Wfi_ActInst actInst = autoSmActInst.GetActInst(actinstid);
		if (actInst != null) {
			//检测转办人员是否有委托
			SmObjInfo info=new SmObjInfo();
			info.setID(staffid);
			info.setName(staffname);
			List<Wfi_PassWork> passwork = smStaff.GetPassWorkByStaffID(info.getID());
			if (passwork != null && passwork.size()>0) {
				Wfd_Actdef def = autoSmActInst.GetActDef(actinstid);
				smAbnormal.AddPassWorkInfo( def,info);
			}

			Wfi_TurnList list=new Wfi_TurnList();
			list.setTurnList_Id(Common.CreatUUID());
			list.setActInst_Id(actinstid);
			list.setFromStaffId(smStaff.getCurrentWorkStaffID());
			list.setToStaffId(info.getID());
			list.setTurnDate(new Date());
			list.setStatus("1");
			actInst.setStaff_Id(info.getID());
			actInst.setDept_Code(smStaff.GetStaffDeptID(info.getID()));
			actInst.setStaff_Name(info.getName());
			actInst.setOperation_Type(WFConst.Operate_Type.HaveTurn.value + "");// 转办
			//TODO:转办后更新环节实例中当前环节的信息：
			Wfi_ProInst proisnt =smProInst.GetProInstByActInstId(actinstid);
			if(proisnt!=null){
				proisnt.setOperation_Type_Nact(actInst.getOperation_Type());
				proisnt.setStaff_Id_Nact(actInst.getStaff_Id());
				proisnt.setStaff_Name_Nact(actInst.getStaff_Name());
			}
			commonDao.update(proisnt);
			commonDao.save(list);
			commonDao.update(actInst);
			smInfo.setID(info.getID());
			smInfo.setName(info.getName());
			smInfo.setDesc("案卷已经成功移交给" + info.getName());

			// 专版后更改流程实力的operation_Type 为空：
			Wfi_ProInst proinst = smProInst.GetProInstByActInstId(actinstid);
			if (proinst.getOperation_Type() != null) {
				if (proinst.getOperation_Type().equals(WFConst.Operate_Type.WithDraw.value + "")) {
					proinst.setOperation_Type(WFConst.Operate_Type.WithDraw.value + "");
					commonDao.update(proinst);
				}
			}
			commonDao.flush();
		}
		return smInfo;
	}

	/**
	 * 案卷转办给多人
	 *
	 */
	public SmObjInfo turnToMoreStaff(String actinstid, String[] staffids , String[] staffnames){
		return turnToMoreStaffExt(actinstid, staffids , staffnames,null);
	}
	public SmObjInfo turnToMoreStaffExt(String actinstid, String[] staffids , String[] staffnames,String msg){
		SmObjInfo result = new SmObjInfo();
		Wfi_ActInst actinst = autoSmActInst.GetActInst(actinstid);
		if(null!=actinst){
			actinst.setActinst_Status(WFConst.Instance_Status.Instance_NotAccept.value);
			actinst.setStaff_Id(null);
			actinst.setStaff_Name(null);
			actinst.setOperation_Type(WFConst.Operate_Type.HaveTurn.value+"");
			commonDao.update(actinst);
			Wfi_ProInst proinst = smProInst.GetProInstByActInstId(actinstid);
			proinst.setActinst_Status(WFConst.Instance_Status.Instance_NotAccept.value);
			proinst.setOperation_Type_Nact(WFConst.Operate_Type.HaveTurn.value+"");
			proinst.setStaff_Id_Nact(null);
			proinst.setStaff_Name_Nact(null);
			commonDao.update(proinst);
			List<Wfi_ActInstStaff> actstaffs = commonDao.findList(Wfi_ActInstStaff.class, " ACTINST_ID = '"+actinstid+"'");
			Wfd_Actdef actdef = autoSmActInst.GetActDef(actinstid);
			String currentStaff = smStaff.getCurrentWorkStaffID();
			for(Wfi_ActInstStaff actstaff : actstaffs){
				commonDao.delete(actstaff);
			}
			for(int i=1;i<staffids.length;i++){
				String staffid = staffids[i];
				//检查委托
				SmObjInfo info = new SmObjInfo();
				info.setID(staffid);
				List<Wfi_PassWork> passwork = smStaff.GetPassWorkByStaffID(staffid);
				if (passwork != null && passwork.size()>0) {
					smAbnormal.AddPassWorkInfo( actdef,info);
				}
				//添加actstaff
				Wfi_ActInstStaff newActStaff = new Wfi_ActInstStaff();
				newActStaff.setActinst_Id(actinstid);
				newActStaff.setStaff_Id(info.getID());
				newActStaff.setRole_Id(actdef.getRole_Id());
				newActStaff.setStaff_Name(smStaff.GetStaffName(staffids[i]));
				commonDao.save(newActStaff);
				//添加转办记录
				Wfi_TurnList turnlist = new Wfi_TurnList();
				turnlist.setActInst_Id(actinstid);
				turnlist.setFromStaffId(currentStaff);
				turnlist.setToStaffId(info.getID());
				turnlist.setTurnDate(new Date());
				turnlist.setStatus("0");
				turnlist.setTurnMsg(msg);
				commonDao.save(turnlist);
				commonDao.flush();
				result.setID(actinstid);
				result.setDesc("案卷已成功移交!");
			}
		}
		return result;
	}
	/**
	 * 被驳回环节转出到下一个环节添加转出意见
	 * @author zhangp
	 * @data 2017年4月5日下午2:37:14
	 * @param actinstid
	 * @return
	 */
	public Wfi_ActInst passovermsg(String actinstid){
/*		List<Object> list = new ArrayList<Object>();
		List<SmObjInfo> infolist = new ArrayList<SmObjInfo>();
		Map<String,String> map = new HashMap<String, String>();
		String msg = "";
		infolist = this.passbackmsg(actinstid);
		list.add(infolist);
		msg = smActInst.GetActInst(actinstid).getActinst_Msg();
		if(msg!=null&&!msg.equals("")&&!msg.equals("转出附言") && !msg.equals("受理项目备注")&& !msg.equals("受理项目备注来自在线受理")&&!msg.equals("批量转出")){
			map.put("currmsg", msg);
			list.add(map);
		}
		return list;*/
		String proinstid = smProInst.GetProInstIDByActInstId(actinstid);
		List<Wfi_ActInst> actinsts = autoSmActInst.GetActInstsbyproinstid(proinstid);
		if(null!=actinsts&&actinsts.size()>0){
			String msg = "";
			for(int i=actinsts.size()-1;i>=0;i--){
				msg = actinsts.get(i).getActinst_Msg();
				if(msg!=null&&!msg.equals("")&&!msg.equals("转出附言") && !msg.equals("受理项目备注")&& !msg.equals("受理项目备注来自在线受理")&&!msg.equals("批量转出")&&!msg.equals("【驳回意见】")){
					return actinsts.get(i);
				}
			}
		}
		return null;
	}

	/**
	 * 获取驳回意见
	 * */

	public List<SmObjInfo> passbackmsg(String actinstid) {
		List<SmObjInfo> infolist=null;
		Wfi_ActInst inst = autoSmActInst.GetActInst(actinstid);
		if (inst != null) {
			infolist = new ArrayList<SmObjInfo>();
			if (inst.getOperation_Type() != null && inst.getOperation_Type().equals(WFConst.Operate_Type.PRODUCT_anglesPicture.value + "")) {

				List<Wfi_ActInst> list=commonDao.getDataList(Wfi_ActInst.class,
						"select * from "+Common.WORKFLOWDB +"wfi_actinst where actdef_id='"+inst.getActdef_Id()+
						"' and proinst_id='"+inst.getProinst_Id()+"' order by actinst_start desc");
				String msg;
				for(int i=0;i<list.size();i++){
					SmObjInfo info = new SmObjInfo();
					msg =list.get(i).getActinst_Msg();
					if(msg!=null&&!msg.equals("")&&!msg.equals("转出附言") && !msg.equals("受理项目备注")&& !msg.equals("受理项目备注来自在线受理")&&!msg.equals("批量转出")){
						info.setID(WFConst.Operate_Type.PRODUCT_anglesPicture.value + "");
						info.setDesc(msg);
							infolist.add(info);
					}
				}
			}else{
				SmObjInfo info = new SmObjInfo();
				List<Wfi_ActInst> backInsts = autoSmActInst.GetProActInsts(actinstid);
/*				List<Wfi_ActInst> list = smActInst.getActInstForCommonActdef(actinstid);
				for(Wfi_ActInst act : list){
					if(act!=null){
						backInsts.addAll(smActInst.GetProActInsts(act.getActinst_Id()));
					}
				}*/
				if (backInsts != null && backInsts.size() > 0) {
					Wfi_ActInst backInst = backInsts.get(0);
					if (backInst.getOperation_Type() != null && backInst.getActinst_Msg() != null && !backInst.getActinst_Msg().equals("转出附言") && !backInst.getActinst_Msg().equals("受理项目备注")&& !backInst.getActinst_Msg().equals("受理项目备注来自在线受理")&&!backInst.getActinst_Msg().equals("批量转出")) {
						info.setID(WFConst.Operate_Type.PRODUCT_anglesPicture.value + "");
						info.setDesc(backInst.getActinst_Msg());
						infolist.add(info);
					}else{
						List<Wfi_ActInst> list = autoSmActInst.getActInstForCommonActdef(backInst.getActinst_Id());
						String actdef = autoSmActInst.GetActInst(actinstid).getActdef_Id();
						for(Wfi_ActInst act : list){
							List<Wfi_ActInst> wfi_actinsts = autoSmActInst.GetProActInsts(act.getActinst_Id());
							if(null==wfi_actinsts||0>=wfi_actinsts.size()){
								continue;
							}
							Wfi_ActInst ainst=wfi_actinsts.get(0);
							if(ainst!=null&&ainst.getActdef_Id().equals(actdef)){
								if (act.getOperation_Type() != null && act.getActinst_Msg() != null && !act.getActinst_Msg().equals("转出附言") && !act.getActinst_Msg().equals("受理项目备注")&& !act.getActinst_Msg().equals("受理项目备注来自在线受理")&&!act.getActinst_Msg().equals("批量转出")) {
									info.setID(WFConst.Operate_Type.PRODUCT_anglesPicture.value + "");
									info.setDesc(act.getActinst_Msg());
									infolist.add(info);
									break;
								}
							}
						}
					}
				}
			}
		}
		return infolist;
	}

	/**
	 * 项目督办的一些操作
	 * */

	public SmObjInfo ProjetDB(String filenumber) {
		SmObjInfo info = new SmObjInfo();
		if (filenumber != null && !"".equals(filenumber)) {
			Wfi_ProInst proInst = smProInst.GetProInstByFileNumber(filenumber);
			if (proInst != null) {
				proInst.setOperation_Type(WFConst.Operate_Type.HaveSupervised.value + "");
				//proInst.setProinst_Status(WFConst.Instance_Type.Instance_Supervised.value);
				proInst.setProinst_Weight(WFConst.Proinst_Weight.Supervised.value);
				proInst.setProinst_Status(WFConst.Instance_Status.Instance_DB.value);
				info.setID(WFConst.Operate_Type.HaveSupervised.value + "");
				info.setDesc("督办成功");
				info.setName("");
				Wfi_DbList db = new Wfi_DbList();
				db.setProinst_Id(proInst.getProinst_Id());
				db.setStart_Time(new Date());
				User user = smStaff.getCurrentWorkStaff();
				db.setStaffId(user.getId());
				db.setStaffName(user.getUserName());
				commonDao.save(db);
				// YwLogUtil.addYwLog("项目督办,项目名称：" + proInst.getProject_Name(),
				// ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
				commonDao.update(proInst);
				commonDao.flush();
			}
		}
		return info;

	}
	public SmObjInfo ProjectCancelDB(String filenumber){
		SmObjInfo info = new SmObjInfo();
		if(filenumber != null&&!"".equals(filenumber)){
			Wfi_ProInst proInst = smProInst.GetProInstByFileNumber(filenumber);
			if(proInst != null){
				proInst.setOperation_Type(WFConst.Operate_Type.NormalOperate.value+"");
				proInst.setProinst_Status(WFConst.Instance_Type.Instance_Normal.value);
				proInst.setProinst_Weight(WFConst.Proinst_Weight.Normal.value);
				info.setID(WFConst.Operate_Type.NormalOperate.value+"");
				info.setDesc("取消成功");
				info.setName("");
				commonDao.update(proInst);
				commonDao.flush();
			}
		}

		return info;
	}

	/**
	 * 处理驳回的一些操作 什么时候是跳跃驳回，什么时候是直接驳回
	 * */
	public List<SmObjInfo> PassBackStaff(String actinst_id) {
		Wfd_Actdef actdef = autoSmActInst.GetActDef(actinst_id);
		if (actdef != null) {
			if (actdef.getActdef_Type().equals(WFConst.ActDef_Type.Activity.value + "")
					|| actdef.getActdef_Type().equals(WFConst.ActDef_Type.ProcessEnd.value + "")
					||actdef.getActdef_Type().equals(WFConst.ActDef_Type.ConditionAnd.value + "")) {
				int type = actdef.getCanrejectornot();
				if (type == WFConst.SmActInstOverRuleReturnType.CanOverRuleSkipActDef.value) {
					return smStaff.GetJumpOverRuleStaffbyActinst(actinst_id);
				} else {
					return smStaff.GetOverRuleStaffbyActinst_LSF(actinst_id, 1);
				}
			} else if (actdef.getActdef_Type().equals(WFConst.ActDef_Type.ConditionAnd.value + "")) {// 并发

			} else if (actdef.getActdef_Type().equals(WFConst.ActDef_Type.ActivityMerge.value + "")) {// 汇聚
				int type = actdef.getCanrejectornot();
				//并发路由之后只能跟汇聚活动，汇聚或的的驳回只能使用跳跃驳回
				//跳跃驳回至并发活动的前一个环节
				if (type == WFConst.SmActInstOverRuleReturnType.CanOverRuleSkipActDef.value) {
					return smStaff.GetJumpOverRuleStaffbyActinst2(actinst_id);
				}
				/*else {
					return smStaff.GetOverRuleStaffbyActinst_LSF(actinst_id, 1);
				}*/
			}

		}
		return null;
	}

	/**
	 * 设置协办状态
	 *
	 * @param actinstid
	 * @param status
	 * @return
	 */
	public boolean OpenCoDeal(String actinstid, Integer status) {
		Wfi_ActInst inst = commonDao.get(Wfi_ActInst.class, actinstid);
		if (inst != null) {
			inst.setCodeal(status);
		}
		commonDao.update(inst);
		commonDao.flush();
		return true;

	}

	/**
	 * 批量转出
	 *
	 * @param actinst_id
	 */
	public List<SmObjInfo> BatchPassover(String actinst_id) {
		if (actinst_id != null && !actinst_id.equals("")) {
			String[] actinst_ids = actinst_id.split(",");
			if (actinst_ids != null && actinst_ids.length > 0) {
				for (String id : actinst_ids) {
					try {
						List<SmObjInfo> infos = smStaff.GetActStaffByActInst(id);
						if (infos != null && infos.size() > 0) {

						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}
		return null;
	}

	public boolean CancelOver(String lsh,String bhlb ) {
		if(bhlb.equals("S")){
			return cancelbj(lsh);
		}else if(bhlb.equals("D")){
			StringBuilder sb = new StringBuilder();
			sb.append("select lsh from "
					+ Common.WORKFLOWDB
					+ "wfi_transferlist list left join "
					+ Common.WORKFLOWDB
					+ "wfi_dossiertransfer dossier on dossier.transferid=list.transferid where");
			sb.append(" DDH='");
			sb.append(lsh);
			sb.append("'");
			List<Map> lshs=commonDao.getDataListByFullSql(sb.toString());
			if(lshs.size()>0){
				for(int i=0;i<lshs.size();i++){
					String onelsh=(String) lshs.get(i).get("LSH");
					cancelbj(onelsh);
				}
				return true;
			}
		}
		return false;

	}

	public SmObjInfo ValidateWithDraw(String actinstid) {
		// actinstid="efce94fa56374cd1be25881765b98ff2";
		SmObjInfo info = new SmObjInfo();
		Wfd_Actdef actdef = autoSmActInst.GetActDef(actinstid);
		List<Wfi_ActInst> list=autoSmActInst.GetNextActInstList(actinstid, actdef.getActdef_Id());
		if (list != null && !list.isEmpty()) {
			for (Wfi_ActInst inst:list){
				if (inst.getActinst_Status().equals(WFConst.Instance_Status.Instance_NotAccept.value)) {
					// 等待受理，可以撤回
				} else {
					// 已经转出，无法撤回
					info.setDesc("项目已由" + inst.getStaff_Name() + "办理，无法撤回");
					break;
				}
			}
		}

		return info;

	}

	/**
	 * @author JHX 案卷撤回之后需要更改对应流程实例的opertion_Type '11' 2016-6-16
	 *         案卷撤回会后完善撤回件的办理信息：完善内容（staff_ID staff_name actinst_end）
	 *
	 * */
	public SmObjInfo WithDraw(String actinstid) {
		SmObjInfo info = new SmObjInfo();
		Wfi_ProInst proinst = smProInst.GetProInstByActInstId(actinstid);
		Wfd_Actdef actdef = autoSmActInst.GetActDef(actinstid);
		Wfi_ActInst actinst = autoSmActInst.GetActInst(actinstid);
		List<Wfi_ActInst> nextactinsts = autoSmActInst.GetNextActInstList(actinstid, actdef.getActdef_Id());
		if (nextactinsts != null &&!nextactinsts.isEmpty()) {
				Wfi_ActInst newInst = new Wfi_ActInst();
				newInst.setActdef_Id(actinst.getActdef_Id());
				newInst.setActdef_Type(actinst.getActdef_Type());
				newInst.setActinst_End(null);
				newInst.setActinst_Id(Common.CreatUUID());
				newInst.setActinst_Start(actinst.getActinst_Start());
				newInst.setActinst_Status(WFConst.Instance_Status.Instance_doing.value);
				newInst.setActinst_End(null);
				newInst.setActinst_Msg(actinst.getActinst_Msg());
				newInst.setActinst_Name(actinst.getActinst_Name());
				newInst.setActinst_Willfinish(actinst.getActinst_Willfinish());
				newInst.setCodeal(actinst.getCodeal());
				newInst.setDept_Code(actinst.getDept_Code());
				newInst.setInstance_Type(actinst.getInstance_Type());
				newInst.setIstransfer(actinst.getIstransfer());
				newInst.setOperation_Type(actinst.getOperation_Type());
				newInst.setProdef_Id(actinst.getProdef_Id());
				newInst.setProinst_Id(actinst.getProinst_Id());
				newInst.setStaff_Id(actinst.getStaff_Id());
				newInst.setStaff_Name(actinst.getStaff_Name());
				newInst.setTransfer_Time(actinst.getTransfer_Time());
				newInst.setUploadfile(actinst.getUploadfile());
				newInst.setMsg("主动撤回");
				commonDao.save(newInst);
				Wfi_ActInstStaff actstaff = new Wfi_ActInstStaff();
				actstaff.setActinst_Id(newInst.getActinst_Id());
				actstaff.setActstaffid(Common.CreatUUID());
				actstaff.setStaff_Id(actinst.getStaff_Id());
				commonDao.save(actstaff);
				//TODO:更新环节实例中当前环节的信息：
				proinst.setActdef_Type(newInst.getActdef_Type());
				proinst.setActinst_Id(newInst.getActinst_Id());
				proinst.setIsApplyHangup(newInst.getIsApplyHandup());
				proinst.setStatusext(newInst.getStatusExt());
				proinst.setOperation_Type_Nact(newInst.getOperation_Type());
				proinst.setStaff_Id_Nact(newInst.getStaff_Id());
				proinst.setActinst_Status(newInst.getActinst_Status());
				proinst.setActinst_Willfinish(newInst.getActinst_Willfinish());
				proinst.setActinst_Name(newInst.getActinst_Name());
				proinst.setMsg(newInst.getMsg());
				proinst.setCodeal(newInst.getCodeal());
				proinst.setACTINST_START(newInst.getActinst_Start());
				proinst.setACTINST_END(newInst.getActinst_End());
				proinst.setStaff_Name_Nact(newInst.getStaff_Name());
				//commonDao.saveOrUpdate(proinst);
				// 撤回成功后：
				proinst.setOperation_Type(WFConst.Operate_Type.WithDraw.value + "");
				commonDao.update(proinst);
				boolean flag=true;
				String msg="";
				for(Wfi_ActInst inst : nextactinsts){
					if (inst.getActinst_Status().equals(WFConst.Instance_Status.Instance_Passing.value)) {
						flag=false;
						break;
					}
					inst.setActinst_Status(WFConst.Instance_Status.WithDraw.value);
					// 完善撤回案卷的的信息 staff_id、staff_name、actinst_end
					inst.setStaff_Id(actinst.getStaff_Id());
					inst.setActinst_End(new Date());
					inst.setStaff_Name("撤回人员-" + actinst.getStaff_Name());
					msg= actinst.getStaff_Name() + "把编号为" + proinst.getProlsh() + "案卷由活动" + inst.getActinst_Name() + "撤回到" + actinst.getActinst_Name();
					commonDao.update(inst);
					Wfi_NowActInst nowActInst = autoSmActInst.GetNowActinst(inst.getActinst_Id());
						commonDao.delete(nowActInst);
				}
				Wfi_NowActInst nowActinst = autoSmActInst.CreatNowActInst(newInst);
				commonDao.save(nowActinst);
				if(flag){
					//复制一条路由用于撤回后驳回时找到驳回人员
					//TODO ny
					List<Wfi_Route> routes = commonDao.findList(Wfi_Route.class, " next_actinst_id='"+actinstid+"'");
					if(routes != null && routes.size() > 0){
						for(Wfi_Route route : routes){
							Wfi_Route routeNew = new Wfi_Route();
							try {
								BeanUtils.copyProperties(routeNew, route);
							} catch (Exception e) {
								e.printStackTrace();
							}
							routeNew.setRouteinst_Id(Common.CreatUUID());
							routeNew.setNext_Actinst_Id(newInst.getActinst_Id());
							commonDao.save(routeNew);
						}
					}
					info.setID(actinstid);
					info.setDesc("撤回成功");
					commonDao.flush();
					smAbnormal.AddAbnormInfo(actinst.getStaff_Id(), actinst.getStaff_Name(), "", actinstid, actinst.getProinst_Id(), msg, WFConst.Abnormal_Type.WithDraw.value);
				}

		}

		return info;

	}

	/**
	 *
	 * @param ywh
	 */
	public SmObjInfo AcceptProjectByMapping(String ywh) {
		SmObjInfo info = new SmObjInfo();
		//首先远程访问回获取项目基本信息
		String url=GetProperties.getConstValueByKey("RemoteXZSPService");
		Object obj;
		try {
			//判断业务号是否已经受理
			List<Wfi_ProInst> list_proinst = commonDao.getDataList(Wfi_ProInst.class,
					"select * from "+Common.WORKFLOWDB+"Wfi_ProInst where PROLSH = '"+ywh+"'");
			if(list_proinst.size()>0){
				info.setDesc("业务已经受理，不能二次受理！");
				return info;
			}
			obj = Helper.WebService2(url,ywh);
			StringReader sr= new  StringReader(obj.toString());
			InputSource  si = new InputSource(sr);
			//创建流程
			Document DOC = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(si);
			NodeList nodelist = DOC.getElementsByTagName("ns1:out");
			Node node = nodelist.item(0);
			NodeList childnodelist = node.getChildNodes();
			SmProInfo  smobjinfo  = new SmProInfo();
			if(childnodelist.getLength()>0){
				for (int i=0;i<childnodelist.getLength();i++){
					//受理编码
					if(childnodelist.item(i).getNodeName().equals("APPROVECODE")){
						smobjinfo.setProLSH(childnodelist.item(i).getTextContent());
					}
					String name ="";
					//受理名称
					if(childnodelist.item(i).getNodeName().equals("APPROVENAME")){
						//根据受理名称查询流程定义；
						List<Wfd_Prodef> list = commonDao.getDataList(Wfd_Prodef.class,
								"select * from "+Common.WORKFLOWDB+"Wfd_Prodef where PRODEF_MAPPING = '"+childnodelist.item(i).getTextContent().trim()+"'");
						if(list.size()==1){
							smobjinfo.setProDef_ID(list.get(0).getProdef_Id());
							name=getProName (list.get(0).getProdefclass_Id());
							String str = name.substring(0, name.lastIndexOf(","));
							String[] straar = str.split(",");
							String nameend ="";
							for (int k=straar.length-1;k>=0;k--){
								nameend+=straar[k]+",";
							}
							nameend+=list.get(0).getProdef_Name();
							smobjinfo.setProDef_Name(nameend);
						}else if(list.size()>1){
							info.setDesc("映射流程不唯一无法受理！");
							break;
						}else {
							info.setDesc("没有对应的流程映射！");
							break;
						}
						//无项目名称暂时不用设置
					}
					//受理备注
					if(childnodelist.item(i).getNodeName().equals("APPROVEREMARK")){
						smobjinfo.setRemark(childnodelist.item(i).getTextContent());
					}
				/*	//法人
					if(childnodelist.item(i).getNodeName().equals("ARTIFICIALPERSON")){
						//projectHK.setARTIFICIALPERSON(childnodelist.item(i).getTextContent());
					}*/
					//附件ID
					if(childnodelist.item(i).getNodeName().equals("ATTACHIDS")){
						//projectHK.setATTACHIDS(childnodelist.item(i).getTextContent());
					}
				/*	//附件相关
					if(childnodelist.item(i).getNodeName()==" "){
						//projectHK.setAPPROVECODE(childnodelist.item(i).getTextContent());
					}*/
					//申请人
					if(childnodelist.item(i).getNodeName().equals("CUSTOMERPERSON")){
						smobjinfo.setProInst_Name(childnodelist.item(i).getTextContent());
						//smobjinfo.setProjectName(childnodelist.item(i).getTextContent());
					}


					//申请单位
				/*	if(childnodelist.item(i).getNodeName().equals("CUSTOMER")){
						//projectHK.setCUSTOMER(childnodelist.item(i).getTextContent());
					}
					//申请单位联系电话
					if(childnodelist.item(i).getNodeName().equals("CUSTOMERTEL")){
						///projectHK.setCUSTOMERTEL(childnodelist.item(i).getTextContent());
					}
					//单位地址
					if(childnodelist.item(i).getNodeName().equals("LINKADDRESS")){
						//projectHK.setLINKADDRESS(childnodelist.item(i).getTextContent());
					}
					//联系人
					if(childnodelist.item(i).getNodeName().equals("LINKMAN")){
						//projectHK.setLINKMAN(childnodelist.item(i).getTextContent());
					}
					//联系人电话
					if(childnodelist.item(i).getNodeName().equals("LINKTEL")){
						//projectHK.setLINKTEL(childnodelist.item(i).getTextContent());
					}
					//承诺办结日期
					if(childnodelist.item(i).getNodeName().equals("PROMISESTIME")){
						//projectHK.setPROMISESTIME(childnodelist.item(i).getTextContent());
					}
					//备注材料
					if(childnodelist.item(i).getNodeName().equals("RECEIVESTUFF")){
						//projectHK.setRECEIVESTUFF(childnodelist.item(i).getTextContent());
					}
					//受理经办人
					if(childnodelist.item(i).getNodeName().equals("UNITRECEIVER")){
						//projectHK.setUNITRECEIVER(childnodelist.item(i).getTextContent());
					}
					//办理状态
					if(childnodelist.item(i).getNodeName().equals("approveStatus")){
						//projectHK.setApproveStatus(childnodelist.item(i).getTextContent());
					}*/
				}
			}
			if(smobjinfo.getProDef_ID()!=null&&!smobjinfo.getProDef_ID().equals("")){
				info=smProInstService.Accept(smobjinfo);
			}else{
				info.setDesc("映射流程不唯一无法受理！");
			}
		} catch (Exception e) {
		}
		return info;

	}

	public String getProName (String  prodefclassid){
		String result = "";
		List<Wfd_ProClass> list = commonDao.getDataList(Wfd_ProClass.class,
				"select * from "+Common.WORKFLOWDB+"Wfd_Proclass where PRODEFCLASS_ID= '"+prodefclassid+"'");
		if(list.size()>0){
			result =list.get(0).getProdefclass_Name()+",";
			if(list.get(0).getProdefclass_Pid()!=null&&!list.get(0).getProdefclass_Pid().equals("")){
				result+= getProName (list.get(0).getProdefclass_Pid());
			}
		}
		return result;


	}

	public Map<Object, Object> getSQRXX (String ywh){


		Map<Object, Object> result = new HashMap<Object, Object>();
		List<Map<String,String>> infos = new ArrayList<Map<String,String>>();
		//首先远程访问回获取项目基本信息
		String url=GetProperties.getConstValueByKey("RemoteXZSPService");
		Object obj;
		Map<String,String> processmapping = new HashMap<String, String>();
		try {

			//判断业务号是否已经受理
			List<Wfi_ProInst> list_proinst = commonDao.getDataList(Wfi_ProInst.class,
					"select * from "+Common.WORKFLOWDB+"Wfi_ProInst where PROLSH = '"+ywh+"'");
			if(list_proinst.size()>0){
				result.put("desc","业务已经受理，不能二次受理");
				return result;
			}

			obj = Helper.WebService2(url,ywh);
			StringReader sr= new  StringReader(obj.toString());
			InputSource  si = new InputSource(sr);
			//创建流程
			Document DOC = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(si);
			NodeList nodelist = DOC.getElementsByTagName("ns1:out");
			Node node = nodelist.item(0);
			NodeList childnodelist = node.getChildNodes();

			Node itemnode =null;
			if(childnodelist.getLength()>0){
				for (int i=0;i<childnodelist.getLength();i++){
					itemnode = childnodelist.item(i);
					if(itemnode.getNodeName().equals("APPROVENAME")){
						//根据受理名称查询流程定义；
						Map<String ,String> map = new HashMap<String, String>();
						map.put("name", "流程名称");
						map.put("key",   "LCMC");
						map.put("value", itemnode.getTextContent());
						infos.add(map);
						//无项目名称暂时不用设置
						processmapping.put("processmapping", itemnode.getTextContent());

						result.put("ProName", itemnode.getTextContent());
					}
					//受理备注
					if(itemnode.getNodeName().equals("APPROVEREMARK")){
						Map<String ,String> map = new HashMap<String, String>();
						map.put("name", "备注");
						map.put("key", "BZ");
						map.put("value", itemnode.getTextContent());
						infos.add(map);
					}
					if(itemnode.getNodeName().equals("ARTIFICIALPERSON")){
						Map<String ,String> map = new HashMap<String, String>();
						map.put("name", "法定代表人");
						map.put("key", "ARTIFICIALPERSON");
						map.put("value", itemnode.getTextContent());
						infos.add(map);
					}
					//申请单位负责人
					if(itemnode.getNodeName().equals("CUSTOMERPERSON")){
						Map<String ,String> map = new HashMap<String, String>();
						map.put("name", "申请单位负责人");
						map.put("key", "SQR");
						map.put("value", itemnode.getTextContent());
						infos.add(map);
					}
					//申请单位
				  if(itemnode.getNodeName().equals("CUSTOMER")){
				      Map<String ,String> map = new HashMap<String, String>();
						map.put("name", "申请单位");
						map.put("key", "SQDW");
						map.put("value", itemnode.getTextContent());
						infos.add(map);
					}
					//申请单位联系电话
					if(itemnode.getNodeName().equals("CUSTOMERTEL")){
					    Map<String ,String> map = new HashMap<String, String>();
						map.put("name", "单位联系电话");
						map.put("key", "DWLXDH");
						map.put("value", itemnode.getTextContent());
						infos.add(map);
					}
					//单位地址
					if(itemnode.getNodeName().equals("LINKADDRESS")){
						  Map<String ,String> map = new HashMap<String, String>();
							map.put("name", "单位地址");
							map.put("key", "DWDZ");
							map.put("value", itemnode.getTextContent());
							infos.add(map);;
					}
					//联系人
					if(itemnode.getNodeName().equals("LINKMAN")){
						    Map<String ,String> map = new HashMap<String, String>();
							map.put("name", "联系人");
							map.put("key", "LXR");
							map.put("value", itemnode.getTextContent());
							infos.add(map);
					}
					//联系人电话
					if(itemnode.getNodeName().equals("LINKTEL")){
						Map<String ,String> map = new HashMap<String, String>();
						map.put("name", "联系人电话");
						map.put("key", "LXRDH");
						map.put("value", itemnode.getTextContent());
						infos.add(map);
					}
					//受理时间
					if(itemnode.getNodeName().equals("ACCEPTTIME")){
						Map<String ,String> map = new HashMap<String, String>();
						map.put("name", "受理时间");
						map.put("key", "ACCEPTTIME");
						map.put("value", itemnode.getTextContent());
						infos.add(map);
					}
					//承诺办理日期
					if(itemnode.getNodeName().equals("PROMISESTIME")){
						Map<String ,String> map = new HashMap<String, String>();
						map.put("name", "承诺办结日期");
						map.put("key", "PROMISESTIME");
						map.put("value", itemnode.getTextContent());
						infos.add(map);
					}

				}
				result.put("content", infos);
				infos.add(processmapping);
			}

		} catch (Exception e) {
		}
		return result;

	}

	public boolean cancelbj(String lsh){
		boolean result=false;
		if (lsh != null && !lsh.equals("")) {
			List<Wfi_ProInst> insts = commonDao.getDataList(Wfi_ProInst.class, "select * from " + Common.WORKFLOWDB + "Wfi_ProInst where prolsh='" + lsh + "'");
			if (insts != null && insts.size() > 0) {
				Wfi_ProInst inst = insts.get(0);
				if (inst != null) {
					inst.setProinst_Status(1);
					inst.setActinst_Status(WFConst.Instance_Status.Instance_doing.value);
					commonDao.update(inst);
					List<Wfi_ActInst> actinsts = commonDao.getDataList(Wfi_ActInst.class, "select * from " + Common.WORKFLOWDB + "Wfi_ActInst where actdef_type='5010' and proinst_id='" + inst.getProinst_Id() + "' order by actinst_end desc");
					if (actinsts != null && actinsts.size() > 0) {
						Wfi_ActInst actinst = actinsts.get(0);
						actinst.setActinst_Status(WFConst.Instance_Status.Instance_doing.value);
						actinst.setActinst_End(null);
						commonDao.update(actinst);
						Wfi_NowActInst nowact = autoSmActInst.CreatNowActInst(actinst);
						commonDao.save(nowact);
						// 如果是批量归档，撤销批量记录
						commonDao.deleteQuery("delete " + Common.WORKFLOWDB + "wfi_transferlist where file_number='" + inst.getFile_Number() + "' and receive_status=4 and fromactinst_name='归档'");
						commonDao.flush();
						result=true;

					}else{
						result=false;
						System.out.println("未找到要撤销的件");
					}

				}
			}
		}
		return result;
	}
	/**
	 * 获取本地化配置中的行政区划代码
	 *
	 *
	 * */
	public String getNativeAreaCodeConfig(){
		//海口版本：审批意见需要进行真实性的校验//查询行政区配置
		String area = null;
		List<T_CONFIG> map =  commonDao.getDataList(T_CONFIG.class,"select * from smwb_support.t_config where name = 'XZQHDM' AND CLASSNAME = '基本参数类'");
		if(map!=null&&map.size()>0){
			area=map.get(0).getVALUE()==null?null:map.get(0).getVALUE();
		}
		return area;
	}
	/**
	 * 海口保存项目和用户信息
	 *
	 *
	 * */
	public boolean SaveInfo(Wfi_ProUserInfo info){
		List<Wfi_ProUserInfo> _info=commonDao.getDataList(Wfi_ProUserInfo.class,"select * from "+ Common.WORKFLOWDB + "wfi_prouserinfo where file_number='"+info.getFile_Number()+"'");
		if(_info.size()>0){
			Wfi_ProUserInfo info1=_info.get(0);
			info1.setAddress(info.getAddress());
			info1.setArea(info.getArea());
			info1.setBdcdyzl(info.getBdcdyzl());
			info1.setBdcqzh(info.getBdcqzh());
			info1.setFile_Number(info.getFile_Number());
			info1.setProType(info.getProType());
			info1.setTel(info.getTel());
			info1.setUserName(info.getUserName());
			commonDao.update(info1);
		}else{
			commonDao.save(info);
		}
		commonDao.flush();
		return true;
	}
	/**
	 * 通过环节ID获取转办人
	 *
	 *
	 * */
	public String getDbName(String actinst_id){
		Wfi_ActInst actinst = autoSmActInst.GetActInst(actinst_id);
		List<Wfi_DbList> dblist = commonDao.findList(Wfi_DbList.class, "proinst_id='"+actinst.getProinst_Id()+"' order by start_time desc");
		String staffname="";
		if(null!=dblist && dblist.size()>0){
			staffname=dblist.get(0).getStaffName();
		}else{

		}
		return staffname;
	}

	/**
	 * 通过环节ID获取督办人
	 *
	 *
	 * */
	public String GetTurnName(String actinst_id){
		String currentStaff = smStaff.getCurrentWorkStaffID();
		List<Wfi_TurnList> act = commonDao.findList(Wfi_TurnList.class, "actinst_id='"+actinst_id+"' and tostaffid = '"+currentStaff+"' order by turndate");
		String staffname="";
		if(act.size()>0){
			String staffid=act.get(act.size()-1).getFromStaffId();
			staffname=smStaff.GetStaffName(staffid);
		}else{

		}
		return staffname;
	}

	/**
	 * 转出后必须移交档案才能计入下一环节
	 * 转出后修正环节状态，档案移交后为真正转出时间
	 *
	 * */
	public void updateActinstStatus(String actinstid){
		Wfd_Actdef currrentActDef=autoSmActInst.GetActDef(actinstid);
		JSONObject obj = JSONObject.fromObject(currrentActDef.getCustomeParamsSet());
		if (!obj.isNullObject()) {
			if(obj.containsKey("ZCHYJDA")){
				String zchy = obj.getString("ZCHYJDA");
				if(zchy!=null&&!zchy.equals("")){
					Wfi_ActInst inst=autoSmActInst.GetActInst(actinstid);
					inst.setActinst_End(null);
					inst.setActinst_Status(WFConst.Instance_Status.WaitAccept.value);
					Wfi_ActInst nextInst=autoSmActInst.GetNextActInst(actinstid, currrentActDef.getActdef_Id());
					nextInst.setActinst_Start(null);
					nextInst.setActinst_Status(WFConst.Instance_Status.BeforAccept.value);
					commonDao.update(inst);
					commonDao.update(nextInst);
					commonDao.flush();
				}
			}
		}
	}

	/**
	 * 判断下一个活动是否是归档
	 * @param actinst 活动id
	 * @return
	 */
	public  Boolean IsNextActdefArchives(String actinst){
		Boolean result=false;
		Wfd_Actdef def=smActDef.GetNextActDefByactinst(actinst);
		if(def!=null&&def.getActdef_Name().equals("归档")){
			result=true;
		}
		return result;
	}

	/**
	 * 检查actinststaff表是否有记录，如果没有则添加
	 * @param actinst 活动id staffid  办件人员ID
	 *
	 */
	public void checkActinststaff(String actinstid,String staffid){
		StringBuilder sql=new StringBuilder();
		sql.append(" ACTINST_ID='"+actinstid);
		sql.append("' and STAFF_ID='"+staffid+"'");
		 List<Wfi_ActInstStaff> list=commonDao.getDataList(Wfi_ActInstStaff.class,"BDC_WORKFLOW.WFI_ACTINSTSTAFF",sql.toString());
		if(list.isEmpty()){
			Wfi_ActInstStaff inststaff=new Wfi_ActInstStaff();
			inststaff.setActstaffid(Common.CreatUUID());
			inststaff.setActinst_Id(actinstid);
			inststaff.setStaff_Id(staffid);
			inststaff.setStaff_Name(smStaff.GetStaffName(staffid));
			commonDao.save(inststaff);
			commonDao.flush();
		}
	}
	/**
	 * 批量转出转出按照人员转出
	 * @date   2016年11月16日 上午11:19:06
	 * @author JHX
	 * @return List<SmObjInfo>
	 */
	public List<SmObjInfo> getBatchPassOverStaff(String actinstids){
		List<SmObjInfo> res = new ArrayList<SmObjInfo>();
		List<SmObjInfo> obj = new ArrayList<SmObjInfo>();
		SmObjInfo main = new SmObjInfo();
		String[] strs = actinstids.split(",");
		if(strs!=null&&strs.length>0){
			String ids = "'"+actinstids+"'";
			//办理角色去除重复
			String sql = "select distinct(role_id) ROLEID from "+Common.WORKFLOWDB+"wfd_actdef where actdef_id in ( select c.next_actdef_id from "+Common.WORKFLOWDB+"wfd_actdef A left join "+Common.WORKFLOWDB+"wfi_actinst B"
					+ " ON A.ACTDEF_ID = B.ACTDEF_ID left join "+Common.WORKFLOWDB+"wfd_route C ON a.actdef_id = c.actdef_id  where B.actinst_id in ("+ids+"))";
			List<Map> map  = commonDao.getDataListByFullSql(sql);
			if(map!=null&&!map.isEmpty()){
				String roleid ="";
				Object object = null;
				Role role = null;
				for(int i=0;i<map.size();i++){
					object = map.get(i).get("ROLEID");
					if(object!=null){
						roleid = object.toString();
						role = roleService.findById(roleid);
						SmObjInfo tempobj = new SmObjInfo();
						tempobj.setDesc("Role");
						tempobj.setID(roleid);
						tempobj.setName("");
						if(role!=null){
							tempobj.setName(role.getRoleName());
						}
						List<User> users = roleService.findUsersByRoleId(roleid);
						List<SmObjInfo> tem = new ArrayList<SmObjInfo>();
						if(users!=null&&users.size()>0){
							for(int n=0;n<users.size();n++){
								SmObjInfo userobj = new SmObjInfo();
								userobj.setDesc("staff");
								userobj.setID(users.get(n).getId());
								userobj.setName(users.get(n).getUserName());
								userobj.setText(users.get(n).getUserName());
								tem.add(userobj);
							}
						}
						tempobj.setChildren(tem);
						obj.add(tempobj);
					}

				}
			}
		}
		main.setDesc("转出角色");
		main.setID("");
		main.setText("");
		main.setName("转给");
		if(obj.size()>0){
			main.setChildren(obj);
		}else{
			List<SmObjInfo> tem = new ArrayList<SmObjInfo>();
			SmObjInfo tempobj = new SmObjInfo();
			tempobj.setDesc("Role");
			tempobj.setID("ROLEID");
			tempobj.setName("办结");
			List<SmObjInfo> tem2 = new ArrayList<SmObjInfo>();
			SmObjInfo userobj = new SmObjInfo();
			userobj.setDesc("staff");
			userobj.setID("BJ");
			userobj.setName("直接办结");
			tem2.add(userobj);
			tempobj.setChildren(tem2);
			tem.add(tempobj);
			main.setChildren(tem);
		}

		res.add(main);
		return res;
	}

	public boolean delLockProject(String actinstid,String msg,String type){
		Wfd_Actdef def = autoSmActInst.GetActDef(actinstid);
		Date lockDate = new Date();
		GregorianCalendar cal =new GregorianCalendar();
		cal.setTime(lockDate);
		Date unLockDate = smHoliday.addDateByWorkDay(cal, Integer.parseInt(def.getLockTime()));
		return delLockProject(actinstid, msg, type, unLockDate);
	}
	public boolean delLockProject(String actinstid,String msg,String type ,Date unLockDate){
		if(!StringHelper.isEmpty(actinstid)){
			Wfi_ActInst inst = autoSmActInst.GetActInst(actinstid);
			if(null!=inst){
				User user = smStaff.getCurrentWorkStaff();
				inst.setLockDate(new Date());
				inst.setLockStaffId(user.getId());
				inst.setLockStaffName(user.getUserName());
				inst.setLockMsg(msg);
				inst.setLockType(type);
				inst.setInstance_Type(WFConst.Instance_Type.Instance_Locked.value);
				inst.setOperation_Type(WFConst.Operate_Type.HaveLock.value + "");
				inst.setUnLockDate(unLockDate);
				Wfi_ProInst proinst = smProInst.GetProInstByActInstId(actinstid);
				proinst.setOperation_Type_Nact(WFConst.Operate_Type.HaveLock.value + "");
				commonDao.update(inst);
				commonDao.update(proinst);
				commonDao.flush();
				return true;
			}
		}
		return false;
	}
	public boolean cancleLock(String actinstid){
		return cancleLock(actinstid,null);
	}
	public boolean cancleLock (String actinstid , Date date){
		if(!StringHelper.isEmpty(actinstid)){
			Wfi_ActInst inst = autoSmActInst.GetActInst(actinstid);
			Wfd_Actdef actdef = autoSmActInst.GetActDef(actinstid);
			Wfi_ProInst proinst =smProInst.GetProInstByActInstId(actinstid);
			if(null!=inst){
				Date unLockDate = null==date?inst.getUnLockDate():date;
				inst.setUnLockDate(unLockDate);
				inst.setOperation_Type(WFConst.Operate_Type.NormalOperate.value + "");
				inst.setInstance_Type(WFConst.Instance_Type.Instance_Normal.value);
				proinst.setOperation_Type_Nact(inst.getOperation_Type());
				GregorianCalendar cal2 = new GregorianCalendar();
				if (actdef.getActdef_Time() != null) {
					double diff = (double)(smHoliday.ComputerDiff(inst.getLockDate(), inst.getActinst_Willfinish())) / (double) (1000 * 60 * 60 * 24);
					cal2.setTime(unLockDate);
					inst.setActinst_Willfinish(smHoliday.addDateByWorkDay(cal2, diff));
					//TODO:解挂后更新环节实例中当前环节的信息：
					if(proinst!=null){
						proinst.setActinst_Willfinish(inst.getActinst_Willfinish());
					}
				}
				if (proinst.getProinst_Time() != null) {
					double diff = (double)(smHoliday.ComputerDiff(inst.getLockDate(), proinst.getProinst_Willfinish())) / (double) (1000 * 60 * 60 * 24);
					cal2.setTime(unLockDate);
					proinst.setProinst_Willfinish(smHoliday.addDateByWorkDay(cal2, diff));
				}

				commonDao.update(inst);
				commonDao.update(proinst);
				commonDao.flush();
				return true;
			}
		}
		return false;

	}
	/**
	 * 驳回操作之后，将驳回之前最新环节的前一个环节审批意见复制到驳回之后环节
	 * @author zhangp
	 * @data 2017年3月22日下午5:30:06
	 * @param actinst_id 驳回操作之前的最新活动环节id
	 * @param actdef_id 驳回操作之后最新环节的环节定义id
	 */
	public void afterPassbackCopySpyj(String actinst_id,String actdef_id){
		String proinst_id = smProInst.GetProInstIDByActInstId(actinst_id);
		StringBuilder nowhereSql = new StringBuilder();
		nowhereSql.append(" proinst_id='");
		nowhereSql.append(proinst_id);
		nowhereSql.append("' and actdef_id='");
		nowhereSql.append(actdef_id);
		nowhereSql.append("' order by actinst_start desc");
		List<Wfi_ActInst> actInsts = commonDao.findList(Wfi_ActInst.class, nowhereSql.toString());
		if(actInsts!=null&&actInsts.size()>=2){
			Wfi_ActInst actinst = actInsts.get(1);//获取驳回之前时，前一个环节实例
			String preOneActinstid = actinst.getActinst_Id();
			if(!StringHelper.isEmpty(preOneActinstid)){
				StringBuilder sql=new StringBuilder();
				sql.append(" ACTINST_ID='"+preOneActinstid+"'");
				List<Wfi_Spyj> spyjs = commonDao.findList(Wfi_Spyj.class, sql.toString());
				if(spyjs!=null&&spyjs.size()>0){
					Wfi_Spyj spyj = spyjs.get(0);
					Wfi_ActInst currActinst= smProInstService.GetNewActInst(proinst_id);
					String currActinstid = currActinst.getActinst_Id();
					//用proinst_id和actinst_id查找所有审批意见
					//将每条意见中的actinst_id修改为当前环节ID
						for(int k = 0; k < spyjs.size(); k++){
							Wfi_Spyj oneSpyj =  spyjs.get(k);
							Wfi_Spyj tmpspyj = new Wfi_Spyj();
							tmpspyj.setSpyj_Id(Common.CreatUUID());
							tmpspyj.setSpdy_Id(oneSpyj.getSpdy_Id());
							tmpspyj.setActinst_Id(currActinstid);
							tmpspyj.setSplx(oneSpyj.getSplx());
							tmpspyj.setSpyj(oneSpyj.getSpyj());
							tmpspyj.setSpr_Name(oneSpyj.getSpr_Name());
							tmpspyj.setSpr_Id(oneSpyj.getSpr_Id());
							tmpspyj.setSpsj(oneSpyj.getSpsj());
							tmpspyj.setStatus(oneSpyj.getStatus());
							tmpspyj.setProinst_Id(oneSpyj.getProinst_Id());
							tmpspyj.setBDCDYH(oneSpyj.getBDCDYH());
							commonDao.save(tmpspyj);
						}
					commonDao.flush();
				}
			}
		}
	}

	/**
	 * 转出后调用环节定义的url
	 *
	 */
	public void sendGetUrl(String actinstid){

		Wfd_Actdef actDef = autoSmActInst.GetActDef(actinstid);
		Wfi_ProInst proInst = smProInst.GetProInstByActInstId(actinstid);
		String url = actDef.getPassUrl();
		if(!StringHelper.isEmpty(url)){
			String param = "project_id="+proInst.getFile_Number()
					+"&lsh="+proInst.getProlsh();
			String[] str = url.split("\\?");
			if(str.length>1){
				param += "&" + str[1];
			}
			try {
				HttpRequestTools.sendGet(str[0], param);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	/**
	 * 退件后，设置项目状态
	 *
	 */
	public void setProState(String actinstid){
		Wfd_Actdef actdef = autoSmActInst.GetActDef(actinstid);
		if(actdef.getIsReturnAct()!=null&&actdef.getIsReturnAct()>0){
			Wfi_ProInst proinst = smProInst.GetProInstByActInstId(actinstid);
			String Project_ID = proinst.getFile_Number();
			ResultMessage message=projectServiceImpl.setXMXX_STATUS(Project_ID,YXBZ.无效);
		}
	}
	public SmObjInfo BatchAcceptProject(String prodefid,String batch,String staffid, HttpServletRequest request, HttpServletResponse response) {
		if (prodefid != null && !prodefid.equals("")) {
			SmProInfo info = new SmProInfo();
			info.setProDef_ID(prodefid);
			info.setBatch(batch);
			Wfd_Prodef prodef = smProDef.GetProdefById(prodefid);
			String proDefName = smProDef.getproDefName(prodefid);
			info.setProDef_Name(proDefName);
			info.setLCBH(prodef.getProdef_Code());
			info.setProInst_Name("批量受理-批次号：" + batch);
			SmObjInfo smObjInfo = new SmObjInfo();
			smObjInfo.setID(staffid);
			smObjInfo.setName(smStaff.GetStaffName(smObjInfo.getID()));
			List<SmObjInfo> staffList = new ArrayList<SmObjInfo>();
			staffList.add(smObjInfo);
			info.setAcceptor(smObjInfo.getName());
			info.setStaffID(staffid);
			info.setFile_Urgency("1");
			return smProInstService.Accept(info, staffList);
		} else {
			return null;
		}

	}

}
