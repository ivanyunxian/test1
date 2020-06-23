package com.supermap.wisdombusiness.workflow.web.wfm.serverlayer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.service.ProjectService;
import com.supermap.realestate.registration.service.impl.ProjectServiceImpl;
import com.supermap.realestate.registration.tools.HttpRequestTools;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.YXBZ;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.framework.model.Department;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.service.RoleService;
import com.supermap.wisdombusiness.framework.service.UserService;
import com.supermap.wisdombusiness.utility.Helper;
import com.supermap.wisdombusiness.utility.StringHelper;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfd_Actdef;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import com.supermap.wisdombusiness.workflow.model.Wfd_Route;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProUserInfo;
import com.supermap.wisdombusiness.workflow.model.Wfi_Route;
import com.supermap.wisdombusiness.workflow.model.Wfi_Spyj;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.SmProInfo;
import com.supermap.wisdombusiness.workflow.service.common.WFConst;
import com.supermap.wisdombusiness.workflow.service.wfd.SmActDef;
import com.supermap.wisdombusiness.workflow.service.wfd.SmProDef;
import com.supermap.wisdombusiness.workflow.service.wfd.SmRouteDef;
import com.supermap.wisdombusiness.workflow.service.wfi.SmActInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmRouteInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.service.wfi.proinstStateModify;
import com.supermap.wisdombusiness.workflow.service.wfm.OperationService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProInstService;

@Controller
@RequestMapping("/operation")
public class OperationController {
	@Autowired
	private OperationService operationService;
	@Autowired
	private SmActInst SmActInst;
	@Autowired
	private SmStaff smStaff;
	@Autowired
	private SmRouteDef smRouteDef;
	@Autowired
	private SmRouteInst smRouteInst;
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userServive;
	@Autowired
	private SmProInstService smProInstService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private SmProDef smProdef;

	@Autowired
	private SmProInst smProInst;
	@Autowired
	private SmActDef smActDef;
	
	
	@Autowired
	private proinstStateModify proinststatemodify;
	@Autowired
	private ProjectServiceImpl projectServiceImpl;
	
//	@Autowired
//	private Wfi_ProUserInfo WFI_PROUSERINFO;
	@Autowired
	private CommonDao commonDao;
	/**
	 * 验证流程是否可以转出
	 * 
	 * */
	@RequestMapping(value = "/canpassover/{actinstid}", method = RequestMethod.GET)
	@ResponseBody
	public SmObjInfo IsCanPassOver(@PathVariable String actinstid, HttpServletRequest request, HttpServletResponse response) {
		SmObjInfo smObjInfo = new SmObjInfo();
		String Project_ID = request.getParameter("filenumber");
		List<Wfd_Route> routedefList = smRouteDef.GetNextRouteByActinstID(actinstid);
		if (routedefList != null && routedefList.size() > 0) {
			Wfd_Route route = routedefList.get(0);
			Map<String, String> map = smRouteDef.ValiteRoute(route.getRoute_Id(), Project_ID);
			if (map != null) {
				smObjInfo.setName(map.get("BM"));
				smObjInfo.setDesc(map.get("MS"));
			}
			//退件环节，退件后设置项目状态
			Wfd_Actdef actdef = SmActInst.GetActDef(actinstid);
			if(actdef.getIsReturnAct()!=null&&actdef.getIsReturnAct()>0){
				ResultMessage message=projectServiceImpl.setXMXX_STATUS(Project_ID,YXBZ.无效);
				if(message.getSuccess().equals("false")){
					smObjInfo.setName("3");
					smObjInfo.setDesc(message.getMsg());
				}
			}
			
			smObjInfo.setID(route.getRoute_Id());

		} else {
			// 办结前验证
			smRouteDef.ValiteProInstOver(actinstid);
			smObjInfo.setID("-1");
		}
		return smObjInfo;
	}

	/**
	 *用密码转出给个人 
	 */
	@RequestMapping(value = "/passtostaff/{actinstid}", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo passtostaff(@PathVariable String actinstid, HttpServletRequest request, HttpServletResponse response) {
		String userId = request.getParameter("staffid");
		String passWord = request.getParameter("password");
		String passMsg = request.getParameter("msg");
		String issend = request.getParameter("issend");
		String routeid = request.getParameter("routeid");
		SmObjInfo result = new SmObjInfo();
		User user = userServive.findById(userId);
		if(StringHelper.isNotNull(passWord)){
			if(passWord.equals(ConfigHelper.getNameByValue("PASSOVERWORD"))){
				SmObjInfo info = new SmObjInfo();
				info.setID(userId);
				info.setName(user.getUserName());
				List<SmObjInfo> objInfos = new ArrayList<SmObjInfo>();
				objInfos.add(info);
				result = exePassover(actinstid, routeid, passMsg, objInfos, false, issend.equals("true"));
			}else{
				result.setDesc("密码错误！");
			}
		}
		return result;
	}
	
	/**
	 * 流程转出控制
	 * 
	 * @param acinstid
	 *            流程实例ID
	 * 
	 * */
	@RequestMapping(value = "/passover/{actinstid}", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo PassOver(@PathVariable String actinstid, HttpServletRequest request, HttpServletResponse response) {
		SmObjInfo successString = null;
		if (actinstid != null && actinstid != "") {
			// String routeidString = request.getParameter("routeid");
			String msg = request.getParameter("msg");
			String info = request.getParameter("info");
			Wfi_ProInst proinst=smProInst.GetProInstByActInstId(actinstid);
			String area=proinst.getAreaCode();
			boolean issend=false;
			boolean more = false;
			// String staffString = request.getParameter("staffs");
			if (info != null && !info.equals("")) {
				JSONArray infos = JSONArray.fromObject(info);
				if (infos != null && infos.size() > 0) {
					if (infos.size() > 1) {
						more = true;
					}
					for (Object obj : infos) {
						List<SmObjInfo> objInfos = new ArrayList<SmObjInfo>();
							JSONObject single = JSONObject.fromObject(obj);
							if(single.containsKey("issend")){
								String issendmsg=single.getString("issend");
								issend=issendmsg.equals("true");
							}
						String routeidString = single.getString("routeid");
						JSONObject jsonstaffs = single.getJSONObject("staffs");
						if (jsonstaffs != null && !jsonstaffs.toString().equals("null")) {
							JSONArray ja = jsonstaffs.getJSONArray("staffs");

							String typeString = jsonstaffs.get("type").toString();
							if (ja.size() > 0) {
								if (typeString.equals("Staff")) {// 按照员工转出
									for (int i = 0; i < ja.size(); i++) {
										SmObjInfo objInfo = new SmObjInfo();
										objInfo.setID(ja.getJSONObject(i).getString("id"));// 设置staffid
										objInfo.setName(ja.getJSONObject(i).getString("name"));// 设置staffName
										objInfos.add(objInfo);
									}
								} else if (typeString.equals("Dept")) {// 按照部门
									for (int i = 0; i < ja.size(); i++) {
										List<User> users = userServive.findUserByDepartmentId(ja.getJSONObject(i).getString("id"));
										if (users != null && users.size() > 0) {
											for (User user : users) {
												if(area!=null&&!area.isEmpty()&&!area.equals(user.getAreaCode())){
													continue;
												}
												SmObjInfo objInfo = new SmObjInfo();
												objInfo.setID(user.getId());// 设置staffid
												objInfo.setName(user.getUserName());// 设置staffName
												objInfos.add(objInfo);
											}
										}
									}
								} else if (typeString.equals("Role")) {// 按照角色
									for (int i = 0; i < ja.size(); i++) {
										String roleId = ja.getJSONObject(i).getString("id");
										Wfd_Actdef actdef = smActDef.GetNextActDef(routeidString);
										String arearange = smStaff.GetCurrentAreaCode(proinst.getStaff_Id());
										if(area!=null&&!area.isEmpty()){
											arearange=area;
										}
										if(actdef!=null){
											String turnoutrange = actdef.getTurnOutRange();
											if(turnoutrange!=null&&!turnoutrange.trim().equals("")){
												if(turnoutrange.equals("0")){
													arearange=arearange.substring(0,2)+"0000";
												}else if(turnoutrange.equals("1")){
													arearange=arearange.substring(0,4)+"00";
												}
											}
										}
										List<User> users = roleService.findUsersByRoleIdAndCode(roleId, arearange);
										if (users != null && users.size() > 0) {
												for (User user : users) {
													SmObjInfo objInfo = new SmObjInfo();
													objInfo.setID(user.getId());// 设置staffid
													objInfo.setName(user.getUserName());// 设置staffName
													objInfos.add(objInfo);
												}
											
										}
									}
								}
							}
						}
						successString = exePassover(actinstid, routeidString, msg, objInfos, more,issend);
					}
				}
			}
		}
		return successString;
	}

	private SmObjInfo exePassover(String actinstid, String routeidString, String msg, List<SmObjInfo> staffobjInfos, boolean more,boolean issend) {
		SmObjInfo successString = null;
		String requestMode = "";
		String operaStaffString = smStaff.getCurrentWorkStaffID();// 当前操作人员
		if (operationService.BeforePassOver()) {// 转出前
			successString = operationService.PassOver(routeidString, actinstid, staffobjInfos, operaStaffString, msg, more);
			if (successString != null && successString.getID().equals(WFConst.Instance_Status.Instance_Success.value + "")) {
				if(issend){
					//TODO:operationService.sendMessage();
				}
				//转出后用于档案移交与环节转出关联
				operationService.updateActinstStatus(actinstid);
				//转出后调用url
				Wfd_Actdef actdef = SmActInst.GetActDef(actinstid);
				String url = actdef.getPassUrl();
				if(StringHelper.isNotNull(url)){
					Matcher accessMatcher = Pattern.compile("mode" + "=(.+?)(?:&|$)").matcher(url);
					if (accessMatcher.find()) {
						requestMode = accessMatcher.group(1);
					}
				}
				if(StringHelper.isNotNull(url)&&"post".equals(requestMode)){
					Wfi_ProInst proinst = smProInst.GetProInstByActInstId(actinstid);
					String actinst_id = proinst.getActinst_Id();
					operationService.sendPostUrl(actinst_id);
				}else{
					operationService.sendGetUrl(actinstid);
				}
			}
		}
		return successString;
	}

	@RequestMapping(value = "/passoversuccess/{actinstid}", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo passoversuccess(@PathVariable String actinstid, HttpServletRequest request, HttpServletResponse response) {
		SmObjInfo successString = null;
		successString = operationService.AfterPassOver(actinstid);// 转出后
		//转出之后插入更新第三方的数据库操作更新流程状态海口项目
		proinststatemodify.modifyProinstState(actinstid,"1");
		return successString;
	}

	/**
	 * 设置在办人员，一个件有待办到在办的转化
	 * 
	 * */
	@RequestMapping(value = "/setworkstaff/{actinstid}", method = RequestMethod.PUT)
	@ResponseBody
	public SmObjInfo SetWorkStaff(@PathVariable String actinstid, HttpServletRequest request, HttpServletResponse response) {
		String operaStaffString = smStaff.getCurrentWorkStaffID();// 当前操作人员
		return SmActInst.SetActinstWorkStaff(actinstid, operaStaffString);
	}
	/**
	 * 批量在办
	 */
	@RequestMapping(value = "/batch/setworkstaff",method = RequestMethod.POST)
	@ResponseBody
	public Map<String ,List<String>> BatchSetworkStaff(HttpServletRequest request, HttpServletResponse response){
		String actinstids = request.getParameter("ids");
		String Staffid = smStaff.getCurrentWorkStaffID();
		Map<String ,List<String>> result = new HashMap<String,List<String>>();
		List<String> success = new ArrayList<String>();
		List<String> error = new ArrayList<String>();
		if(actinstids!=null&&!"".equals(actinstids)){
			String[] ids = actinstids.split(",");	
			for(String id : ids){
				Wfi_ActInst actinst = SmActInst.GetActInst(id);
				Wfi_ProInst proinst = smProInst.GetProInstByActInstId(id);
				if(actinst!=null&&!"".equals(actinst)){
					SmObjInfo info = SmActInst.SetActinstWorkStaff(id, Staffid);
					if("".equals(info.getID())){
						error.add(proinst.getProlsh());
					}else{
						success.add(proinst.getProlsh());
					}
				}else{
					error.add(proinst.getProlsh());
				}
			}
			result.put("success", success);
			result.put("error", error);
			return result;
		}else{
			return result;
		}
	}
	/**
	 * 根据当前活动获取转出人员列表
	 * */

	@RequestMapping(value = "/back/stafflist/{actinst}", method = RequestMethod.GET)
	@ResponseBody
	public List<SmObjInfo> GetBackStaff(@PathVariable String actinst, HttpServletRequest request) {
		// String type=request.getParameter("type");
		List<Wfi_ActInst> lists = smProInstService.GetProInstProcess(actinst);
		Wfi_ProInst proinst = smProInst.GetProInstByActInstId(actinst);
		Wfd_Actdef curactdef = SmActInst.GetActDef(actinst);
		Wfi_ActInst inst=SmActInst.GetActInst(actinst);
		List<String> temp = new ArrayList<String>();
		if (proinst.getOperation_Type() != null) {
			if (proinst.getOperation_Type().equals("11")) {
				for (int i = 0; i < lists.size(); i++) {
					if (lists.get(i).getActdef_Id().equals(curactdef.getActdef_Id())) {
						temp.add(lists.get(i).getActinst_Id());
					}
				}
				if (temp.size() >= 2) {
					actinst = temp.get(temp.size() - 2);
				}
				if (temp.size() == 1) {
					actinst = temp.get(0);
				}
			}	
		}
//		if(inst.getOperation_Type().equals("31")){
//			List<Wfi_ActInst> list=SmActInst.getActInstForCommonActdef(actinst);
//			if(!list.isEmpty()){
//				actinst=list.get(0).getActinst_Id();
//			}
//		}
		return operationService.PassBackStaff(actinst);
	}

	/**
	 * 验证案卷是够可以驳回案卷驳回
	 * 
	 * @param actinstid
	 *            活动ID
	 * @return 返回一个对象，如果对象ID有值就说明有不可转出的情况 ，如果为空值就驳回
	 * */
	@RequestMapping(value = "/directoverrule/{actinstid}", method = RequestMethod.GET)
	@ResponseBody
	public SmObjInfo DirectOverRule(@PathVariable String actinstid, HttpServletRequest request, HttpServletResponse response) {
		// actinstid="dfe3f263d0204d528903b2b32871132a";
		// 根据当前环节获取流程实例的类型，判断popertion_type是否=11，如果是表示当前环节是撤回件；
		// 需要在流程实力表中查询相同的流程 流程定义：的数据并且取出倒数第二行的数据，的流程ID
		// 判断当前流程实力的操作类型：
		/*
		 * List<Wfi_ActInst> lists =
		 * smProInstService.GetProInstProcess(actinstid); Wfi_ProInst proinst =
		 * smProInst.GetProInstByActInstId(actinstid); Wfd_Actdef curactdef =
		 * SmActInst.GetActDef(actinstid); List<String> temp = new
		 * ArrayList<String>(); if(proinst.getOperation_Type().equals("11")){
		 * for(int i=0;i<lists.size();i++){
		 * if(lists.get(i).getActdef_Id().equals(curactdef.getActdef_Id())){
		 * temp.add(lists.get(i).getActinst_Id()); } } if(temp.size()>=2){
		 * actinstid= temp.get(temp.size()-2); } if(temp.size()==1){ actinstid=
		 * temp.get(0); } }
		 */
		SmObjInfo smObjInfo = operationService.DirectOverRule(actinstid);
		return smObjInfo;

	}

	/**
	 * 直接驳回
	 * */
	@RequestMapping(value = "/directover/{actinstid}", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo PassBack(@PathVariable String actinstid, HttpServletRequest request, HttpServletResponse response) {
		SmObjInfo success = null;
		try {
			if (actinstid != null && actinstid != "") {
				String proactdefString = request.getParameter("actdefid");
				String msg = request.getParameter("msg");
				String staffString = request.getParameter("staffs");
				JSONObject jb = JSONObject.fromObject(staffString);
				JSONArray ja = jb.getJSONArray("staffs");
				List<SmObjInfo> objInfos = new ArrayList<SmObjInfo>();
				for (int i = 0; i < ja.size(); i++) {
					SmObjInfo objInfo = new SmObjInfo();
					objInfo.setID(ja.getJSONObject(i).getString("id"));// 设置staffid
					objInfo.setName(ja.getJSONObject(i).getString("name"));// 设置staffName
					objInfos.add(objInfo);
					objInfo.setText(msg);
				}
				success = operationService.OverRuleDirectly(proactdefString, actinstid, objInfos, msg);
				//海口版本：在驳回之后要求更新电子政务的数据库。
				String area = operationService.getNativeAreaCodeConfig();
				if(success!=null&&success.getName().equals("驳回")&&area!=null&&area.equals("460100")){
					//驳回之后插入更新第三方的数据库操作更新流程状态
					proinststatemodify.modifyProinstState(actinstid,"0");
				}
				//在驳回之后是否复制审批意见
				String isCopy = ConfigHelper.getNameByValue("isCopyspyj");
				List<Wfi_Spyj> wfi_spyjs = commonDao.getDataList(Wfi_Spyj.class, "BDC_WORKFLOW.Wfi_Spyj", " Actinst_Id = '" + actinstid + "'");
				if(wfi_spyjs.size() > 0){
					if(success!=null&&success.getName().equals("驳回")&&isCopy!=null&&isCopy.equals("1")){
						/**
						 * 审核意见未填写，并且是驳回状态的，把当前环节产生的数据删除。
						 */
						if(wfi_spyjs.size() !=0){
							Wfi_Spyj wfi_spyj = wfi_spyjs.get(0);
							if(StringHelper.isNotNull(wfi_spyj.getSpyj())){
								operationService.afterPassbackCopySpyj(actinstid, proactdefString);
							}else{
								commonDao.deleteQuery("delete from bdc_workflow.Wfi_Spyj where spyj_id = '" +  wfi_spyj.getSpyj_Id()+ "'");
								commonDao.flush();
							}
						}
					}else{
						Wfi_Spyj wfi_spyj = wfi_spyjs.get(0);
						if(!StringHelper.isNotNull(wfi_spyj.getSpyj())){
							commonDao.deleteQuery("delete from bdc_workflow.Wfi_Spyj where spyj_id = '" +  wfi_spyj.getSpyj_Id()+ "'");
							commonDao.flush();
						}
					}
				}
				if (ja.size() > 0) {
					/*
					 * YwLogUtil.addYwLog("驳回项目成功", ConstValue.SF.YES.Value,
					 * ConstValue.LOG.UPDATE);
					 */
				}
			}

		} catch (Exception e) {
			/*
			 * YwLogUtil.addYwLog("驳回,失败", ConstValue.SF.NO.Value,
			 * ConstValue.LOG.UPDATE);
			 */
		}
		return success;
	}

	/**
	 * 获取可以办理此案卷的人员---- 案卷转办
	 * */
	@RequestMapping(value = "/turn/{actinstid}", method = RequestMethod.GET)
	@ResponseBody
	public List<SmObjInfo> GetTurnStaffByactinstid(@PathVariable String actinstid, HttpServletRequest request, HttpServletResponse response) {
		return operationService.GetTurnStaffByActinstID(actinstid);
	}

	/**
	 * 案卷转办
	 * */
	@RequestMapping(value = "/turnproject/{actinstid}", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo TurnProjectByactinstid(@PathVariable String actinstid, HttpServletRequest request, HttpServletResponse response) {
		String staffidString = request.getParameter("staffid");
		String staffnameString = request.getParameter("staffname");
		String msg = request.getParameter("msg");
		String[] staffids = staffidString.split(",");
		String[] staffnames = staffnameString.split(",");
		if(staffids.length>2){
			if(null!=msg&&!"【转办意见】".equals(msg)){
				return operationService.turnToMoreStaffExt(actinstid, staffids, staffnames, msg);
			}
			return operationService.turnToMoreStaff(actinstid, staffids , staffnames);
		}
		operationService.checkActinststaff(actinstid, staffids[1]);
		if(null!=msg&&!"【转办意见】".equals(msg)){
			return operationService.turnToMoreStaffExt(actinstid, staffids, staffnames, msg);
		}
		return operationService.TurnProjectByactinstid(actinstid, staffids[1], staffnames[1]);
	}
	
	/**
	 * 转办前验证密码
	 * @param password
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/checkpassword", method = RequestMethod.POST)
	@ResponseBody
	public boolean checkPassWord( HttpServletRequest request, HttpServletResponse response) {
		String password = request.getParameter("password");
		return StringHelper.isNotNull(password)
				&&password.equals(ConfigHelper.getNameByValue("PASSOVERWORD"));
	}

	/**
	 * 检测是一个活动是否是驳回活动，是否有驳回意见
	 */
	@RequestMapping(value = "/passback/msg/{actinstid}", method = RequestMethod.GET)
	@ResponseBody
	public Wfi_ActInst passbackmsg(@PathVariable String actinstid, HttpServletRequest request, HttpServletResponse response) {
		//return operationService.passbackmsg(actinstid);
		return operationService.passovermsg(actinstid);
		
	}
	@RequestMapping(value="/actinst/allmsg/{actinstid}",method=RequestMethod.GET)
	@ResponseBody
	public List<Wfi_ActInst> allactinstmsg(@PathVariable String actinstid, HttpServletRequest request, HttpServletResponse response){
		List<Wfi_ActInst> actinsts = smProInstService.GetProInstProcess(actinstid);
		// 检查是否有转办，有转办则添进list
		List<Wfi_ActInst> actinstlist = smProInstService.checkTurnProject(actinsts);
		if(null!=actinstlist&&actinstlist.size()>0){
			List<Wfi_ActInst> wfi_actinsts = new ArrayList<Wfi_ActInst>(); 
			for(int i=actinstlist.size()-1;i>=0;i--){
					String msg = actinstlist.get(i).getActinst_Msg();
					if(msg!=null&&!msg.equals("")&&!msg.equals("转出附言") && !msg.equals("受理项目备注")&& !msg.equals("受理项目备注来自在线受理")&&!msg.equals("批量转出")&&!msg.equals("【驳回意见】")){
						wfi_actinsts.add(actinstlist.get(i));
					}
			}
			actinstlist = wfi_actinsts;
		}
		return actinstlist;
/*		String proinstid = smProInst.GetProInstIDByActInstId(actinstid);
		List<Wfi_ActInst> actInsts = SmActInst.GetActInstsbyproinstid(proinstid);
		if(null!=actInsts&&actInsts.size()>0){
			List<Wfi_ActInst> wfi_actinsts = new ArrayList<Wfi_ActInst>(); 
			for(int i=actInsts.size()-1;i>=0;i--){
					String msg = actInsts.get(i).getActinst_Msg();
					if(msg!=null&&!msg.equals("")&&!msg.equals("转出附言") && !msg.equals("受理项目备注")&& !msg.equals("受理项目备注来自在线受理")&&!msg.equals("批量转出")&&!msg.equals("【驳回意见】")){
						wfi_actinsts.add(actInsts.get(i));
					}
			}
			actInsts = wfi_actinsts;
		}
		return actInsts;*/

	}
	@RequestMapping(value = "/passback/currmsg/{actinstid}",method = RequestMethod.GET)
	@ResponseBody
	 public List<Wfi_ActInst> currActinst(@PathVariable String actinstid,HttpServletRequest request,HttpServletResponse reponse){
		 List<Wfi_ActInst> actinsts = new ArrayList<Wfi_ActInst>();
		 Wfi_ActInst actinst = SmActInst.GetActInst(actinstid);
		String msg = actinst.getActinst_Msg();
		if(msg!=null&&!msg.equals("")&&!msg.equals("转出附言") && !msg.equals("受理项目备注")&& !msg.equals("受理项目备注来自在线受理")&&!msg.equals("批量转出")&&!msg.equals("【驳回意见】")&&!msg.equals("【转办意见】")){
			actinsts.add(actinst);
			return actinsts;
		}
		 return null;
	 }
	/**
	 * 项目督办
	 * */
	@RequestMapping(value = "project/db/{filenumber}", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo ProjectDB(@PathVariable String filenumber, HttpServletRequest request, HttpServletResponse response) {
		return operationService.ProjetDB(filenumber);
	}
	@RequestMapping(value = "project/canceldb/{filenumber}",method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo ProjectCancel(@PathVariable String filenumber, HttpServletRequest request,HttpServletResponse response){
		return operationService.ProjectCancelDB(filenumber);
	}
	@RequestMapping(value = "project/dbzt/{filenumber}",method = RequestMethod.GET)
	@ResponseBody
	public String ProjectDBZT(@PathVariable String filenumber,HttpServletRequest request,HttpServletResponse response){
		Wfi_ProInst proinst = smProInst.GetProInstByFileNumber(filenumber);
		String DBtype = proinst.getProinst_Status()+"";
		return DBtype;
	}
	@RequestMapping(value = "project/status/{actinstid}",method = RequestMethod.GET)
	@ResponseBody
	public String ProjectStatus(@PathVariable String actinstid,HttpServletRequest request,HttpServletResponse response){
		Wfi_ProInst proinst = smProInst.GetProInstByActInstId(actinstid);
		String status = proinst.getProinst_Status()+"";
		return status;
	}
	/**
	 * 根据当前活动获取转出人员列表
	 * 
	 * @throws Exception
	 * */

	@RequestMapping(value = "/selectstafflist/{actinst}", method = RequestMethod.GET)
	@ResponseBody
	public List<SmObjInfo> GetNextStaff(@PathVariable String actinst) throws Exception {
		if (actinst != null && actinst != "") {
			return smStaff.GetActStaffByActInst(actinst);
		} else {
			return null;
		}
	}

	/** 获取当前待办活动可以办理的所有人员 */
	@RequestMapping(value = "/actinststafflist/{actinst}", method = RequestMethod.GET)
	@ResponseBody
	public String GetActinstStaff(@PathVariable String actinst) throws Exception {
		if (actinst != null && !actinst.equals("")) {
			return smStaff.GetActStaffs(actinst);
		} else {
			return "";
		}
	}

	/**
	 * 设置协办服务
	 * 
	 * @param actinst
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/codeal/{actinst}/{status}", method = RequestMethod.POST)
	@ResponseBody
	public boolean OpenCoDeal(@PathVariable String actinst, @PathVariable Integer status) {
		return operationService.OpenCoDeal(actinst, status);

	}

	// 批量转出服务按照角色
	@RequestMapping(value = "/batch/passover", method = RequestMethod.POST)
	@ResponseBody
	public List<SmObjInfo> BatchPassover(HttpServletRequest request, HttpServletResponse response) {
		List<SmObjInfo> Result = new ArrayList<SmObjInfo>();
		String actinstids = request.getParameter("actinstids");
		if (actinstids != null && !actinstids.equals("")) {
			String[] actinst_ids = actinstids.split(",");
			if (actinst_ids != null && actinst_ids.length > 0) {
				for (String id : actinst_ids) {
					try {
						SmObjInfo info = null;
						List<SmObjInfo> infos = smStaff.GetActStaffByActInst(id);
						if (infos != null && infos.size() > 0) {
							// 路由
							SmObjInfo route = infos.get(0);
							String routeidString = route.getID();
							//TODO:根据路由找到下个环节环节定义
							Wfd_Actdef nextActdef=null;
							List<SmObjInfo> objInfos = new ArrayList<SmObjInfo>();
							if(routeidString!=null&&!routeidString.equals("")){
								Wfd_Route nextRouteDef =
										commonDao.get(Wfd_Route.class, routeidString);
								if(nextRouteDef!=null){
									String nextActdefid = nextRouteDef.getNext_Actdef_Id();
									if(nextActdefid!=null&&!nextActdefid.equals("")){
										nextActdef = smActDef.GetActDefByID(nextActdefid);
									}
								}
							}else{
								//TODO:如果routid为空默认办结
								info = exePassover(id, routeidString, "批量转出", objInfos, false,false);
								Result.add(info);
							}
							if(nextActdef!=null){
								String nextActdefRoleId = nextActdef.getRole_Id();
								if(nextActdefRoleId!=null&&!nextActdefRoleId.equals("")){
									Wfi_ProInst proinst = smProInst.GetProInstByActInstId(id);
									String area = proinst.getAreaCode();
									if(null==area||area.isEmpty()){
										area = userServive.findById(proinst.getStaff_Id()).getAreaCode();
									}
									Wfd_Actdef actdef = SmActInst.GetActDef(id);
									if(actdef!=null){
										String turnoutrange = actdef.getTurnOutRange();
										if(turnoutrange!=null&&!turnoutrange.trim().equals("")){
											if(turnoutrange.equals("0")){
												area=area.substring(0,2)+"0000";
											}else if(turnoutrange.equals("1")){
												area=area.substring(0,4)+"00";
											}
										}
									}
									List<User> users = roleService.findUsersByRoleIdAndCode(nextActdefRoleId, area);
									if (users != null && users.size() > 0) {
										for (User user : users) {
											
											SmObjInfo objInfo = new SmObjInfo();
											objInfo.setID(user.getId());// 设置staffid
											objInfo.setName(user.getUserName());// 设置staffName
											objInfos.add(objInfo);
										}
										info = exePassover(id, routeidString, "批量转出", objInfos, false,false);
										Result.add(info);
									}else{
										//角色下没有发现转出人员不能转出
										continue;
									}
								}else{
									//没有发现角色不能执行转出
									continue;
								}
							}
							
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return Result;
	}
	    // 批量转出服务按照人员
		@RequestMapping(value = "/batch/passover/bystaff", method = RequestMethod.POST)
		@ResponseBody
		public List<SmObjInfo> batchPassoverByStaff(HttpServletRequest request, HttpServletResponse response) {
			List<SmObjInfo> Result = new ArrayList<SmObjInfo>();
			String actinstids = request.getParameter("ids");
			String staffs = request.getParameter("staffs");
			if (actinstids != null && !actinstids.equals("")) {
				String[] actinst_ids = actinstids.split(",");
				if (actinst_ids != null && actinst_ids.length > 0) {
					for (String id : actinst_ids) {
						try {
							SmObjInfo info = null;
							List<SmObjInfo> infos = smStaff.GetActStaffByActInst(id);
							if (infos != null && infos.size() > 0) {
								// 路由
								SmObjInfo route = infos.get(0);
								String routeidString = route.getID();
								//TODO:根据路由找到下个环节环节定义
								Wfd_Actdef nextActdef=null;
								List<SmObjInfo> objInfos = new ArrayList<SmObjInfo>();
								if(routeidString!=null&&!routeidString.equals("")){
									Wfd_Route nextRouteDef =
											commonDao.get(Wfd_Route.class, routeidString);
									if(nextRouteDef!=null){
										String nextActdefid = nextRouteDef.getNext_Actdef_Id();
										if(nextActdefid!=null&&!nextActdefid.equals("")){
											nextActdef = smActDef.GetActDefByID(nextActdefid);
										}
									}
								}else{
									//TODO:如果routid为空默认办结
									info = exePassover(id, routeidString, "批量转出", objInfos, false,false);
									Result.add(info);
								}
								if(nextActdef!=null){
									String nextActdefRoleId = nextActdef.getRole_Id();
									if(nextActdefRoleId!=null&&!nextActdefRoleId.equals("")){
										if(staffs!=null&&!staffs.equals("")){
											com.alibaba.fastjson.JSONObject jsonObject = 
													com.alibaba.fastjson.JSONObject.parseObject(staffs);
											if(jsonObject!=null&&!jsonObject.isEmpty()){
												Set<String> set = jsonObject.keySet();
												Iterator<String> it = set.iterator();
												while(it.hasNext()){
													String staffid = it.next();
													if(!staffid.equals("BJ")){
														SmObjInfo objInfo = new SmObjInfo();
														objInfo.setID(staffid);// 设置staffid
														objInfo.setName(jsonObject.getString(staffid));// 设置staffName
														objInfos.add(objInfo);
													}
												}
												info = exePassover(id, routeidString, "批量转出", objInfos, false,false);
												Result.add(info);
											}
										}else{
											continue;
										}
									}else{
										//没有发现角色不能执行转出
										continue;
									}
								}
								
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			return Result;
		}

	// 取消项目办结
	@RequestMapping(value = "/cancel/over/{prolsh}", method = RequestMethod.POST)
	@ResponseBody
	public boolean CancelOver(@PathVariable String prolsh,HttpServletRequest request, HttpServletResponse response) {
		String BHLB = StringHelper.ObjToString(request.getParameter("BHLB"));
		return operationService.CancelOver(prolsh,BHLB);

	}

	// 案卷的撤回
	@RequestMapping(value = "/validate/withdraw/{actinst}", method = RequestMethod.GET)
	@ResponseBody
	public SmObjInfo ValidateWithDraw(@PathVariable String actinst) {
		return operationService.ValidateWithDraw(actinst);
	}

	// 案卷的撤回
	@RequestMapping(value = "/withdraw/{actinst}", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo WithDraw(@PathVariable String actinst) {
		return operationService.WithDraw(actinst);
	}

	//好像是西安干啥用的   批量受理案卷
	@RequestMapping(value = "/batch/acceptproject/{prodefid}/{batch}/{staffid}", method = RequestMethod.GET)
	@ResponseBody
	public SmObjInfo BatchAcceptProject(@PathVariable String prodefid, @PathVariable String batch, @PathVariable String staffid, HttpServletRequest request, HttpServletResponse response) {
		if (prodefid != null && !prodefid.equals("")) {
			final Wfi_ProInst inst = smProInst.GetProinstByBatchDM(batch);
			if (inst != null) {
				SmObjInfo info=new SmObjInfo();
				info.setID(inst.getProinst_Id());
				info.setDesc("受理成功");
				return info;
			} else {
				SmProInfo info = new SmProInfo();
				info.setProDef_ID(prodefid);
				info.setBatch(batch);
				Wfd_Prodef prodef = smProdef.GetProdefById(prodefid);
				String proDefName = smProdef.getproDefName(prodefid);
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
			}
		} else {
			return null;
		}

	}
	// 石家庄批量受理大宗件专用服务  ————批量受理案卷
		@RequestMapping(value = "/batch/sjzacceptproject/{prodefid}/{batch}/{staffid}", method = RequestMethod.GET)
		@ResponseBody
		public SmObjInfo SJZBatchAcceptProject(@PathVariable String prodefid, @PathVariable String batch, @PathVariable String staffid, HttpServletRequest request, HttpServletResponse response) {
			if (prodefid != null && !prodefid.equals("")) {
					SmProInfo info = new SmProInfo();
					info.setProDef_ID(prodefid);
					info.setBatch(batch);
					Wfd_Prodef prodef = smProdef.GetProdefById(prodefid);
					info.setProDef_Name(prodef.getProdef_Name());
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
	//海口电子政务接入
	/**
	 * 0
	 * @param ywh
	 */
	@RequestMapping(value = "/importProject/{ywh}", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo ImportProject(@PathVariable String ywh) {
		 //ywh  ="20151388538";
		 SmObjInfo info=new SmObjInfo();
		//首先远程访问回获取项目基本信息
		 info = operationService.AcceptProjectByMapping(ywh);
		return info;
		
	}
	//海口输入项目和用户信息
	/**
	 * 
	 * @param 
	 */
	@RequestMapping(value = "/saveinfo", method = RequestMethod.POST)
	public @ResponseBody boolean SaveInfo(
			HttpServletRequest request, HttpServletResponse response,
			 String  file_number ,String address,String qzh,String area,
			 String use,String dyzl,String protype,String tel) {
		Wfi_ProUserInfo info = new Wfi_ProUserInfo(null, file_number, use, tel, area, address, protype, qzh, dyzl);
		return	operationService.SaveInfo(info);
	}
	/**
	 * 批量转办，获取该部门下所有用户
	 * */
	@RequestMapping(value = "/turn/{actinstid}/allstaff", method = RequestMethod.GET)
	@ResponseBody
	public List<SmObjInfo> GetTurnStaffByRole(@PathVariable String actinstid, HttpServletRequest request, HttpServletResponse response) {
		Department dept=smStaff.getCurrentWorkStaff().getDepartment();
		List<User> users = userServive.findUserByDepartmentId(dept.getId());
		List<SmObjInfo> userinfo=new ArrayList<SmObjInfo>();
		for(User user : users){
			SmObjInfo info=new SmObjInfo();
			info.setDesc("staff");
			info.setID(user.getId());
			info.setName(user.getUserName());
			userinfo.add(info);
		}
		List<SmObjInfo> deptlist = new ArrayList<SmObjInfo>();
		SmObjInfo deptInfo = new SmObjInfo();
		deptInfo.setID(dept.getId());
		deptInfo.setName(dept.getDepartmentName());
		deptInfo.setDesc("Dept");
		deptInfo.setChildren(userinfo);
		deptlist.add(deptInfo);
		List<SmObjInfo> list = null;
		if (actinstid != null && !"".equals(actinstid)) {
			list = new ArrayList<SmObjInfo>();
			SmObjInfo info = new SmObjInfo();
			info.setName("转给");
			info.setID(actinstid);
			info.setDesc("Desc");
			info.setChildren(deptlist);
			list.add(info);
		}
		return list;
	}
	/**
	 *批量转办
	 */
	@RequestMapping(value = "/project/batchrurn", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo batchRurn( HttpServletRequest request, HttpServletResponse response) {
		String actinsts=request.getParameter("actinsts");
		String staffidString = request.getParameter("staffid");
		String staffnameString = request.getParameter("staffname");
		String[] actinstids = actinsts.split(",");
		boolean success=true;
		SmObjInfo info=new SmObjInfo();
		for(int i=0;i<actinstids.length;i++){
			String actinstid=actinstids[i];
			//检察actinststaff表是否有记录，防止转办后在办箱查不到
			operationService.checkActinststaff(actinstid,staffidString);			
			SmObjInfo obj=operationService.TurnProjectByactinstid(actinstid, staffidString, staffnameString);
			if(obj.getID()==null){
				success=false;
			}
		}
		if(success){
			info.setID(staffidString);
			info.setDesc("批量派件成功！");
		}
		return info;
	}
	
	@RequestMapping(value="/selectstafflist/batchpassover",method = RequestMethod.POST)
	@ResponseBody
	public List<SmObjInfo> getBatchPassOverStaff(HttpServletRequest request, HttpServletResponse response){
		String actinstids = request.getParameter("actinstids");
		List<SmObjInfo> lists = null;
		if(actinstids!=null&&!actinstids.equals("")){
			lists = operationService.getBatchPassOverStaff(actinstids);
		}
		return lists;
	}
	/**
	 * 批量删除项目
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/batch/delprojects",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,List<String>> delprojects(HttpServletRequest request, HttpServletResponse response){
		String lshs = request.getParameter("lshs");
		Map<String,List<String>> result = new HashMap<String, List<String>>();
		List<String> success = new ArrayList<String>();
		List<String> error = new ArrayList<String>();
		List<Wfi_ProInst> insts = smProInst.GetProInstByLshs(lshs);
		for(Wfi_ProInst inst : insts){
			SmObjInfo info = smProInstService.deleteProInst(inst.getProinst_Id(), "批量删除项目");
			if(null!=info.getID()&&!"".equals(info.getID())){
				success.add(inst.getProlsh());
			}else{
				error.add(inst.getProlsh());
			}
		}
		result.put("success", success);
		result.put("error", error);
		return result;
	}
	/**
	 * 批量驳回验证
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/batch/canpassback",method = RequestMethod.GET)
	@ResponseBody
	public SmObjInfo canbatchPassback(HttpServletRequest request, HttpServletResponse response){
		String actinstids = request.getParameter("lshs");
		SmObjInfo info = new SmObjInfo();
		List<Wfi_ActInst> insts = SmActInst.GetActInstsByActinstids(actinstids);
		for(Wfi_ActInst inst : insts){
			info = operationService.DirectOverRule(inst.getActinst_Id());
			if(null!=info.getID()){
				Wfi_ProInst proinst = smProInst.GetProInstByActInstId(inst.getActinst_Id());
				info.setID(proinst.getProlsh());
				return info;
			}
		}
		return info;
	}
	/**
	 * 批量驳回项目
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/batch/passback",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,List<String>> batchPassback(HttpServletRequest request, HttpServletResponse response){
		String actinstids = request.getParameter("lshs");
		String bhyj = request.getParameter("bhyj");
		if(null==bhyj||"".equals(bhyj)){
			bhyj = "批量驳回项目";
		}
		Map<String,List<String>> result = new HashMap<String, List<String>>();
		List<String> success = new ArrayList<String>();
		List<String> error = new ArrayList<String>();
		List<Wfi_ActInst> insts = SmActInst.GetActInstsByActinstids(actinstids);
		for(Wfi_ActInst inst : insts){
			List<Wfi_ActInst> beforeActinst = SmActInst.GetProActInsts(inst.getActinst_Id());
			Wfi_ProInst proinst = smProInst.GetProInstByActInstId(inst.getActinst_Id());
			if(beforeActinst.size()==1){
				Wfi_ActInst actInst = beforeActinst.get(0);
				String proactdefid = actInst.getActdef_Id();
				List<SmObjInfo> staffs = new ArrayList<SmObjInfo>();
				staffs.add(new SmObjInfo(null, actInst.getStaff_Id(), actInst.getStaff_Name()));
				SmObjInfo info = operationService.OverRuleDirectly(proactdefid, inst.getActinst_Id(), staffs, bhyj);
				if("0".equals(info.getID())){
					error.add(proinst.getProlsh());
				}else{
					success.add(proinst.getProlsh());
				}
			}else{
				error.add(proinst.getProlsh());
			}
		}
		result.put("success", success);
		result.put("error", error);
		return result;
	}
	
	/**
	 * 锁定一个项目
	 * @author dff
	 * @param  actinstid
	 * @param msg
	 * @param type
	 * @return boolean
	 */
	@RequestMapping(value="/project/lock",method = RequestMethod.POST)
	@ResponseBody
	public boolean delLockProject(HttpServletRequest request, HttpServletResponse response){
		String prolsh = request.getParameter("prolsh");
		Wfi_ProInst proinst = smProInst.GetProInstByFileNumber(prolsh);
		Wfi_ActInst actinst = smProInstService.GetNewActInst(proinst.getProinst_Id());
		String msg = request.getParameter("msg");
		String type = request.getParameter("type");
		return operationService.delLockProject(actinst.getActinst_Id(), msg, type);
	}
	/**
	 * 取消项目锁定
	 * @author dff 
	 * @param actinstid
	 * @return boolean
	 * 
	 */
	@RequestMapping(value = "/canclelock/{prolsh}", method = RequestMethod.POST)
	@ResponseBody
	public boolean cancleLock(@PathVariable String prolsh, HttpServletRequest request, HttpServletResponse response) {
		Wfi_ProInst proinst = smProInst.GetProInstByFileNumber(prolsh);
		Wfi_ActInst actinst = smProInstService.GetNewActInst(proinst.getProinst_Id());
		return operationService.cancleLock(actinst.getActinst_Id() , new Date());
	}
	@RequestMapping(value = "/canclelockbyat/{actinstid}", method = RequestMethod.POST)
	@ResponseBody
	public boolean canclelock(@PathVariable String actinstid, HttpServletRequest request, HttpServletResponse response) {
		return operationService.cancleLock(actinstid);
	}
	/**
	 * 
	 * @author zhangp
	 * @data 2017年4月1日下午5:59:03
	 * @param actinstid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getoperationtype/{actinstid}",method = RequestMethod.GET)
	@ResponseBody
	public String getActinstOperationType(@PathVariable String actinstid, HttpServletRequest request, HttpServletResponse response){
		String opertionType = "";
		if(null!=actinstid && !"".equals(actinstid)){
			opertionType = SmActInst.GetActInst(actinstid).getOperation_Type();
		}
		return opertionType;
	}
}
