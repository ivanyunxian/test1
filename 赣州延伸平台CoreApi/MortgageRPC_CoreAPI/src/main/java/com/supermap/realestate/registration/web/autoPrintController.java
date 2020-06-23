package com.supermap.realestate.registration.web;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.service.ZSService;
import com.supermap.realestate.registration.util.*;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.User.UserStatus;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.workflow.model.Wfd_Actdef;
import com.supermap.wisdombusiness.workflow.model.Wfd_Route;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.WFConst;
import com.supermap.wisdombusiness.workflow.service.wfd.SmActDef;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.service.wfm.OperationService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProInstService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 自动打证服务接口，登簿后自动通过调用该接口可以自动流转到归档环节
 * @author heng
 * @date 2018-11-9
 */

@Controller
@RequestMapping("/autoprint")
public class autoPrintController {
	
	@Autowired
	private CommonDao dao;
	@Autowired
	private ZSService zsService;
	
	@Autowired
	private OperationService operationService;
	//@Autowired
	//private SmActInst SmActInst;
	@Autowired
	private SmStaff smStaff;
	/*
	@Autowired
	private SmRouteDef smRouteDef;
	@Autowired
	private SmRouteInst smRouteInst;
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userServive;
	*/
	@Autowired
	private SmProInstService smProInstService;
	//@Autowired
	//private ProjectService projectService;
	//@Autowired
	//private SmProDef smProdef;

	@Autowired
	private SmProInst smProInst;
	@Autowired
	private SmActDef smActDef;

	private final static Logger log=Logger.getLogger(autoPrintController.class);
	
	/** 自动转出到最后一个流程
	 * @param projectid 业务号
	 */
	@SuppressWarnings("rawtypes")
	private void autoBatchPassover(HttpServletRequest request,HttpServletResponse response, String projectid) {
		StringBuilder builder = new StringBuilder();
		Wfi_ProInst proinst = smProInst.GetProInstByFileNumber(projectid);
		if(proinst==null){
			return;
		}
		while(true){
			Wfi_ActInst act = smProInstService.GetNewActInst(proinst.getProinst_Id());
			if(act==null){
				break;
			}
			String actinstid = act.getActinst_Id();
			if (!StringHelper.isEmpty(actinstid)) {
				//User u=Global.getCurrentUserInfo();
				
				//String param = "actinstids=" + actinstid;
				//HttpRequestTools.sendPost(url, param);
				//SmObjInfo staff=new SmObjInfo("Staff", u.getId(), u.getLoginName());
				//循环转出，直到最后一个环节（归档）
				if(this.passOver(actinstid)==null){
					break;
				}
			}
		}
	}
	private SmObjInfo exePassover(String actinstid, String routeidString, String msg, List<SmObjInfo> staffobjInfos,
			boolean more, boolean issend) {
		SmObjInfo successString = null;
		String operaStaffString = smStaff.getCurrentWorkStaffID();// 当前操作人员
		if (operationService.BeforePassOver()) {// 转出前
			successString = operationService.PassOver(routeidString, actinstid, staffobjInfos, operaStaffString, msg,
					more);
			if (successString != null
					&& successString.getID().equals(WFConst.Instance_Status.Instance_Success.value + "")) {
				if (issend) {
					// TODO:operationService.sendMessage();
				}
				// 转出后用于档案移交与环节转出关联
				operationService.updateActinstStatus(actinstid);
			}
		}
		return successString;
	}
	private void putToList(SmObjInfo root,List<SmObjInfo> out){
		if("Staff".equals(root.getDesc())){
			out.add(root);
		}
		if(root.getChildren()!=null){
			for(SmObjInfo staff:root.getChildren()){
				this.putToList(staff, out);
			}
		}
	}
	protected List<SmObjInfo> getAllStaffs(List<SmObjInfo> infos){
		List<SmObjInfo> staffs=new ArrayList<SmObjInfo>();
		//Staff
		for(SmObjInfo staff:infos){
			putToList(staff,staffs);
		}
		return staffs;
	}
	
	/**
	 * 转出到下个环节
	 * @param actinstid 流程id
	 * @return 转出成功返回 SmObjInfo实例，如果没有下个环节返回null
	 */
	private SmObjInfo passOver(String actinstid){
		SmObjInfo info = null;
		try {
			
			List<SmObjInfo> infos = smStaff.GetActStaffByActInst(actinstid);
			if (infos != null && infos.size() > 0) {
				// 路由
				SmObjInfo route = infos.get(0);
				String routeidString = route.getID();
				// TODO:根据路由找到下个环节环节定义
				Wfd_Actdef nextActdef = null;
				
				List<SmObjInfo> objInfos = new ArrayList<SmObjInfo>();
				if (routeidString != null && !routeidString.equals("")) {
					Wfd_Route nextRouteDef = dao.get(Wfd_Route.class, routeidString);
					if (nextRouteDef != null) {
						String nextActdefid = nextRouteDef.getNext_Actdef_Id();
						if (nextActdefid != null && !nextActdefid.equals("")) {
							nextActdef = smActDef.GetActDefByID(nextActdefid);
						}
					}
				}
				if (nextActdef != null) {
					String nextActdefRoleId = nextActdef.getRole_Id();

					if (nextActdefRoleId != null && !nextActdefRoleId.equals("")) {
						objInfos=this.getAllStaffs(infos);
						info = exePassover(actinstid, routeidString, "自助机转出", objInfos, false, false);
					}
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("转出出现异常", e);
		}
				

		return info;
	}

	/** 是否已有证书编号
	 * @param zsid
	 * @return
	 */
	private long ifZSBH(String zsid){
	    long countzs = dao.getCountByFullSql(" FROM (SELECT GZ.BDCQZH,GZ.ZSBH,GZ.ZSID FROM BDCK.BDCS_ZS_GZ GZ "
					 + " UNION SELECT XZ.BDCQZH,XZ.ZSBH,XZ.ZSID FROM BDCK.BDCS_ZS_XZ XZ "
					 + " UNION SELECT LS.BDCQZH,LS.ZSBH,LS.ZSID FROM BDCK.BDCS_ZS_LS LS)"
					 + " WHERE ZSID='"+ zsid +"' AND ZSBH IS NOT NULL");
		return countzs;
	}
	
	/** 根据XMBH通过基准流程获取BDCDYLX
	 * @param xmbh
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String getBDCDYLX(String xmbh){
		String bdcdylx = "031";
		StringBuilder builder=new StringBuilder();
		builder.append("SELECT BASEWORKFLOW.UNITTYPE FROM BDCK.BDCS_XMXX XMXX ");
		builder.append("LEFT JOIN BDC_WORKFLOW.WFI_PROINST PROINST ON XMXX.PROJECT_ID=PROINST.FILE_NUMBER ");
		builder.append("LEFT JOIN BDC_WORKFLOW.WFD_MAPPING GX ON GX.WORKFLOWCODE=PROINST.PROINST_CODE ");
		builder.append("LEFT JOIN BDCK.T_BASEWORKFLOW BASEWORKFLOW ON BASEWORKFLOW.ID=GX.WORKFLOWNAME ");
		builder.append("WHERE XMXX.XMBH='");
		builder.append(xmbh);
		builder.append("'");
		List<Map> list = dao.getDataListByFullSql(builder.toString());
		if(list != null && list.size() > 0){
			bdcdylx = StringHelper.formatObject(list.get(0).get("UNITTYPE"));
		}
		return bdcdylx;
	} 

	public String getQZLX(ProjectInfo info){
		// 预告登记都是出证明
		if (info.getDjlx().equals(DJLX.YGDJ.Value) || BDCDYLX.YCH.Value.equals(info.getBdcdylx())) {
			return "1";
		}

		// 抵押权
		if (info.getQllx().equals(QLLX.DIYQ.Value)) {
			return "1";
		}

		// 地役权
		if (info.getQllx().equals(QLLX.DYQ.Value)) {
			return "1";
		}

		// 异议登记
		if (info.getDjlx().equals(DJLX.YYDJ.Value)) {
			return "1";
		}
		return "0";
	}
	
	public Map<String,Object> getQZDRXX(String xmbh,String zjzl,String zjh){
		String sql="select t.qlid,t.qlrid,t2.bdcdyid,t.bdcdyh,t.zsid,t1.szsj,t2.bdcqzh  from BDCK.BDCS_QDZR_GZ t, BDCK.bdcs_zs_gz t1, BDCK.bdcs_ql_gz t2, BDCK.bdcs_qlr_gz t3"
				+ " where t.qlid = t2.qlid "
				+ "   and t.qlrid = t3.qlrid"
				+ "   and t.xmbh = t1.xmbh"
				+ "   and t.xmbh = t2.xmbh"
				+ "   and t.xmbh = t3.xmbh"
				+ "   and t.xmbh = '"+xmbh+"'"
				+ "   and t3.zjzl = '"+zjzl+"'"
				+ "   and zjh = '"+zjh+"'";
		if(log.isDebugEnabled()){
			log.debug("查询权地证申请人信息，sql:"+sql);
		}
		List<Map> data = this.dao.getDataListByFullSql(sql);
		if(data.size()>0){
			return data.get(0);
		}
		else{
			return null;
		}
	}
	
	/**
	 * 
	 * @param projectid 业务号
	 * @param zsbh 证书编号
	 * @param zjlx 证件类型，默认为1，身份证号
	 * @param zjh 证件号
	 * @param username 用户名
	 * @param password 密码，md5 hex摘要
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/autoPrintCert")
	@ResponseBody
	public Message autoPrintCert(
			@RequestParam(required=true) String projectid,
			@RequestParam(required=false) String zsbh,
			@RequestParam(required=false) String zjlx,
			@RequestParam(required=false) String zjh,
			@RequestParam(required=true) String username,
			@RequestParam(required=true) String password,
			HttpServletRequest request,HttpServletResponse response){
		
		Message msg = new Message();
		msg.setSuccess("false");
		if(StringUtils.isBlank(zjlx)){
			zjlx="1";
		}
		//登录部分--begin
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		try{
			SecurityUtils.getSubject().login(token);
		}
		catch(AuthenticationException ex){
			msg.setMsg("账号或密码错误");
			YwLogUtil.addYwLog("调用证书打印接口失败,账号或密码错误!", SF.NO.Value, ConstValue.LOG.LOGIN);

			return msg;
		}
		if (Global.getCurrentUserInfo().getStatus() == UserStatus.INVALID) {
			msg.setMsg("调用证书打印接口失败,账号已经停用!");
			YwLogUtil.addYwLog("调用证书打印接口失败,账号已经停用!", SF.NO.Value, ConstValue.LOG.LOGIN);

			return msg;
		}
		//登录--end

		ProjectInfo info = ProjectHelper.GetPrjInfoByPrjID(projectid);
		
		if(info == null){
			String logString="调用证书打印接口失败,未找到相关项目信息！";
			msg.setMsg(logString);
			YwLogUtil.addYwLog(logString, SF.NO.Value, ConstValue.LOG.PRINT);
			return msg;
		}
		if(!info.isReadonly()){
			String logString="调用证书打印接口失败,该项目未审核登簿！";
			msg.setMsg(logString);
			YwLogUtil.addYwLog(logString, SF.NO.Value, ConstValue.LOG.PRINT);
			return msg;
		}
		/*
		Map<String,Object> qzdrxx=this.getQZDRXX(info.getXmbh(), zjlx, zjh);
		if(qzdrxx==null){
			String logString="调用证书打印接口失败,未找到相关申请！";
			msg.setMsg(logString);
			YwLogUtil.addYwLog(logString, ConstValue.SF.NO.Value, ConstValue.LOG.PRINT);
			return msg;
		}
		if(qzdrxx.get("SZSJ")!=null){
			String logString="调用证书打印接口失败,证书已发放！";
			msg.setMsg(logString);
			YwLogUtil.addYwLog(logString, ConstValue.SF.NO.Value, ConstValue.LOG.PRINT);
			return msg;
		}
		String zsid=(String)qzdrxx.get("ZSID");
		String qzzl = "D";
		String qzlx = this.getQZLX(info);
	    if (zsbh.substring(0, 1).matches("[a-zA-Z]")){
		      qzzl = zsbh.substring(0, 1).toUpperCase(); //权证种类
	    }
		long countzs  = zsService.validateZSBH(zsid, zsbh, qzlx);
		
		if(countzs>0){
			String logString="调用证书打印接口失败,证书编号已被使用！";
			msg.setMsg(logString);
			YwLogUtil.addYwLog(logString, ConstValue.SF.NO.Value, ConstValue.LOG.PRINT);
			return msg;
		}
		long count = ifZSBH(zsid); //验证是否已有证书编号
		if (count > 0) {
			String logString="调用证书打印接口失败,证书编号不存在！";
			msg.setMsg(logString);
			YwLogUtil.addYwLog(logString, ConstValue.SF.NO.Value, ConstValue.LOG.PRINT);
			return msg;
		}
		if (SF.YES.Value.equals(ConfigHelper.getNameByValue("ZsbhManager"))){
			ResultMessage resultmsg = zsService.saveZSBHWithManager(zsid, info.getXmbh(), zsbh, qzlx, qzzl, info.getBdcdylx());
			if (resultmsg.getSuccess().equals("true")) {
				msg.setSuccess("true");
				msg.setMsg("success");
				autoBatchPassover(request,response, info.getProject_id()); //自动转出
			}
			else{
				msg.setSuccess("false");
				String logString="调用证书打印接口失败,证书保存失败！";
				msg.setMsg(logString);
				YwLogUtil.addYwLog(logString, ConstValue.SF.NO.Value, ConstValue.LOG.PRINT);
				return msg;
			}
		}else {
			ResultMessage  resultmsg = zsService.saveZSBH(zsid, info.getXmbh(), zsbh, qzlx, qzzl, request,info.getBdcdylx());
			if (resultmsg.getSuccess().equals("true")) {
				msg.setSuccess("true");
				msg.setMsg("success");
				autoBatchPassover(request,response, info.getProject_id()); //自动转出
			}
			else{
				msg.setSuccess("false");
				String logString="调用证书打印接口失败,证书保存失败！";
				msg.setMsg(logString);
				YwLogUtil.addYwLog(logString, ConstValue.SF.NO.Value, ConstValue.LOG.PRINT);
				return msg;
			}
		}
		*/
		autoBatchPassover(request,response, info.getProject_id()); //自动转出
		YwLogUtil.addYwLog("调用证书打印接口成功，项目自动流转到归档", SF.YES.Value, ConstValue.LOG.PRINT);
		msg.setSuccess("success");
		msg.setMsg("调用接口成功，项目自动流转到归档");
		return msg;
	}
}
