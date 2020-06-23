package com.supermap.realestate.registration.check;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.T_CHECKRULE;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * 登簿检查类
 * @ClassName: BoardCheck
 * @author liushufeng
 * @date 2015年11月7日 下午6:56:30
 */
@Component
@Aspect
public class BoardBookCheck {

	@Autowired
	private CommonDao baseCommonDao;

	/**
	 * 切入点定义
	 * @Title: boardBookPointcut
	 * @author:liushufeng
	 * @date：2015年11月7日 下午9:22:09
	 */
	@Pointcut("execution(* com.supermap.realestate.registration.service.DBService.BoardBook(..))")
	public void boardBookPointcut() {
	}

	/**
	 * 切入点方法
	 * @Title: boardBookCheck
	 * @author:liushufeng
	 * @date：2015年11月7日 下午9:21:54
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around("boardBookPointcut()")
	public Object boardBookCheck(ProceedingJoinPoint pjp) throws Throwable {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("true");
		msg.setMsg("检查通过");

		Object[] params = pjp.getArgs();
		if (params == null || params.length <= 0 || StringHelper.isEmpty(params[0])) {
			msg.setSuccess("false");
			msg.setMsg("参数不正确！");
			return msg;
		}

		// 获取项目编号
		String xmbh = StringHelper.formatObject(params[0]);
		
		BDCS_XMXX xmxx=Global.getXMXXbyXMBH(xmbh);
		if(xmxx!=null)
		{
			if(SF.YES.Value.equals(xmxx.getSFDB()))
			{
				msg.setSuccess("false");
				msg.setMsg("项目已经登簿，不能重复登簿！");
				return msg;
			}
		}
		
		List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "xmbh='"+xmbh+"'");
		if(djdys != null && djdys.size() >1) {
			msg = checkEX(xmbh);
		}else {
			msg = check(xmbh);
		}
		if ("TRUE".equals(msg.getSuccess().toUpperCase())) {
			msg = (ResultMessage) pjp.proceed();
		}
		return msg;
	}

	/**
	 * 执行登簿检查
	 * @Title: Check
	 * @author:liushufeng
	 * @date：2015年11月7日 下午7:00:56
	 * @param xmbh
	 * @return
	 */
	public ResultMessage check(String xmbh) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("true");
		ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
		if (info == null) {
			msg.setSuccess("false");
			return msg;
		}
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("XMBH", xmbh);
		paramMap.put("PROJECT_ID", info.getProject_id());
		List<T_CHECKRULE> checkRules = getCheckRules(info);
		ResultMessage warningmsg = new ResultMessage();
		ResultMessage errormsg = new ResultMessage();
		boolean berror = true;
		boolean bwarning = true;
		for (CheckRule rule : checkRules) {
			ResultMessage checkMsg=CheckEngine.executeCheck(baseCommonDao, rule, paramMap);
			if ("false".equals(checkMsg.getSuccess())) {
				errormsg.setSuccess("false");
				berror = false;
				if(StringHelper.isEmpty(checkMsg.getMsg())){
					if (errormsg.getMsg() != null)
						errormsg.setMsg(errormsg.getMsg() + "\n" + rule.getRESULTTIP() == null ? "" : rule.getId()+":"+rule.getRESULTTIP());
					else
						errormsg.setMsg(rule.getRESULTTIP() == null ? "" : rule.getId()+":"+rule.getRESULTTIP());
				}else{
					if (errormsg.getMsg() != null)
						errormsg.setMsg(errormsg.getMsg() + "\n" + checkMsg.getMsg());
					else
						errormsg.setMsg(checkMsg.getMsg());
				}
			}else if ("warn".equals(checkMsg.getSuccess())) {
				warningmsg.setSuccess("false");
				bwarning=false;
				if(StringHelper.isEmpty(checkMsg.getMsg())){
					if (warningmsg.getMsg() != null)
						warningmsg.setMsg(warningmsg.getMsg() + "\n" + rule.getRESULTTIP() == null ? "" : rule.getId()+":"+rule.getRESULTTIP());
					else
						warningmsg.setMsg(rule.getRESULTTIP() == null ? "" : rule.getId()+":"+rule.getRESULTTIP());
				}else{
					if (warningmsg.getMsg() != null)
						warningmsg.setMsg(warningmsg.getMsg() + "\n" + checkMsg.getMsg());
					else
						warningmsg.setMsg(checkMsg.getMsg());
				}
			}
		}
		if (berror) {
			List<T_CHECKRULE> checkRulesWarning = getCheckRulesWarning(info);
			if (checkRulesWarning != null && checkRulesWarning.size() > 0) {
				for (CheckRule rule : checkRulesWarning) {
					ResultMessage checkMsg=CheckEngine.executeCheck(baseCommonDao, rule, paramMap);
					if ("false".equals(checkMsg.getSuccess())||"warn".equals(checkMsg.getSuccess())) {
						warningmsg.setSuccess("warning");
						bwarning=false;
						if(StringHelper.isEmpty(checkMsg.getMsg())){
							if (warningmsg.getMsg() != null)
								warningmsg.setMsg(warningmsg.getMsg() + "\n" + rule.getRESULTTIP() == null ? "" : rule.getId()+":"+rule.getRESULTTIP());
							else
								warningmsg.setMsg(rule.getRESULTTIP() == null ? "" : rule.getId()+":"+rule.getRESULTTIP());
						}else{
							if (warningmsg.getMsg() != null)
								warningmsg.setMsg(warningmsg.getMsg() + "\n" + checkMsg.getMsg());
							else
								warningmsg.setMsg(checkMsg.getMsg());
						}
					}
				}
			}
		}
		if(!berror){
			msg.setSuccess(errormsg.getSuccess());
			msg.setMsg(errormsg.getMsg());
		}else if(!bwarning){
			msg.setSuccess(warningmsg.getSuccess());
			msg.setMsg(warningmsg.getMsg());
		}
		return msg;
	}

	/**
	 * 获取流程对应的检查项列表,这个获取的是严重的
	 * @Title: getCheckRules
	 * @author:liushufeng
	 * @date：2015年11月7日 下午9:22:23
	 * @param workflowcode
	 * @return
	 */
	public List<T_CHECKRULE> getCheckRules(ProjectInfo info) {
		String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(info.getProject_id());
		String baseworkflowname = HandlerFactory.getWorkflow(info.getProject_id()).getName();
		String hql = MessageFormat
				.format(" (id IN (SELECT CHECKRULEID FROM RT_BASEBOARDCHECK WHERE CHECKLEVEL='1' AND BASEWORKFLOWID=''{0}'') AND id NOT IN(SELECT CHECKRULEID FROM RT_BOARDCHECKEXP WHERE WORKFLOWCODE=''{1}'')) OR id IN (SELECT CHECKRULEID FROM RT_BOARDCHECK WHERE (CHECKLEVEL IS NULL OR CHECKLEVEL<>'2') AND WORKFLOWCODE=''{1}'')",
						baseworkflowname, workflowcode);
		List<T_CHECKRULE> checkRules = baseCommonDao.getDataList(T_CHECKRULE.class, hql);
		return checkRules;
	}

	/**
	 * 获取流程对应的检查项列表,这个获取的是警告的
	 * @Title: getCheckRulesWarning
	 * @author:liushufeng
	 * @date：2016年1月11日 下午2:35:15
	 * @param info
	 * @return
	 */
	public List<T_CHECKRULE> getCheckRulesWarning(ProjectInfo info) {
		String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(info.getProject_id());
		String baseworkflowname = HandlerFactory.getWorkflow(info.getProject_id()).getName();
		String hql = MessageFormat
				.format(" (id IN (SELECT CHECKRULEID FROM RT_BASEBOARDCHECK WHERE CHECKLEVEL='2' AND BASEWORKFLOWID=''{0}'') AND id NOT IN(SELECT CHECKRULEID FROM RT_BOARDCHECKEXP WHERE WORKFLOWCODE=''{1}'')) OR id IN (SELECT CHECKRULEID FROM RT_BOARDCHECK WHERE CHECKLEVEL='2' AND WORKFLOWCODE=''{1}'')",
						baseworkflowname, workflowcode);
		List<T_CHECKRULE> checkRules = baseCommonDao.getDataList(T_CHECKRULE.class, hql);
		return checkRules;
	}
	
	
	/**
	 * 执行转出（驳回）登簿检查
	 * @Title: Routecheck
	 * @author:yuxuebin
	 * @date：2016年04月01日 10:48:56
	 * @param xmbh
	 * @param route_id
	 * @param route_type
	 * @return
	 */
	public ResultMessage Routecheck(String project_id,String route_id,String route_type) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("true");
		ProjectInfo info = ProjectHelper.GetPrjInfoByPrjID(project_id);
		if (info == null) {
			msg.setSuccess("false");
			return msg;
		}
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("XMBH", info.getXmbh());
		paramMap.put("PROJECT_ID", project_id);
		List<T_CHECKRULE> checkRules = getRouteCheckRules(route_id,route_type);
		boolean berror = true;
		boolean bwarning = true;
		ResultMessage errormsg = new ResultMessage();
		ResultMessage warningmsg = new ResultMessage();
		for (CheckRule rule : checkRules) {
			ResultMessage checkMsg=CheckEngine.executeCheck(baseCommonDao, rule, paramMap);
			if ("false".equals(checkMsg.getSuccess())) {
				errormsg.setSuccess("false");
				berror = false;
				if(StringHelper.isEmpty(checkMsg.getMsg())){
					if (errormsg.getMsg() != null)
						errormsg.setMsg(errormsg.getMsg() + "\n" + rule.getRESULTTIP() == null ? "" : rule.getId()+":"+rule.getRESULTTIP());
					else
						errormsg.setMsg(rule.getRESULTTIP() == null ? "" : rule.getId()+":"+rule.getRESULTTIP());
				}else{
					if (errormsg.getMsg() != null)
						errormsg.setMsg(errormsg.getMsg() + "\n" + checkMsg.getMsg());
					else
						errormsg.setMsg(checkMsg.getMsg());
				}
			}else if ("warn".equals(checkMsg.getSuccess())) {
				warningmsg.setSuccess("warning");
				bwarning = false;
				if(StringHelper.isEmpty(checkMsg.getMsg())){
					if (warningmsg.getMsg() != null)
						warningmsg.setMsg(warningmsg.getMsg() + "\n" + rule.getRESULTTIP() == null ? "" : rule.getId()+":"+rule.getRESULTTIP());
					else
						warningmsg.setMsg(rule.getRESULTTIP() == null ? "" : rule.getId()+":"+rule.getRESULTTIP());
				}else{
					if (warningmsg.getMsg() != null)
						warningmsg.setMsg(warningmsg.getMsg() + "\n" + checkMsg.getMsg());
					else
						warningmsg.setMsg(checkMsg.getMsg());
				}
			}
		}
		if (berror) {
			List<T_CHECKRULE> checkRulesWarning = getRouteCheckRulesWarning(route_id,route_type);
			if (checkRulesWarning != null && checkRulesWarning.size() > 0) {
				for (CheckRule rule : checkRulesWarning) {
					ResultMessage checkMsg=CheckEngine.executeCheck(baseCommonDao, rule, paramMap);
					if ("false".equals(checkMsg.getSuccess())) {
						warningmsg.setSuccess("warning");
						bwarning = false;
						if(StringHelper.isEmpty(checkMsg.getMsg())){
							if (warningmsg.getMsg() != null)
								warningmsg.setMsg(warningmsg.getMsg() + "\n" + rule.getRESULTTIP() == null ? "" : rule.getId()+":"+rule.getRESULTTIP());
							else
								warningmsg.setMsg(rule.getRESULTTIP() == null ? "" : rule.getId()+":"+rule.getRESULTTIP());
						}else{
							if (warningmsg.getMsg() != null)
								warningmsg.setMsg(warningmsg.getMsg() + "\n" + checkMsg.getMsg());
							else
								warningmsg.setMsg(checkMsg.getMsg());
						}
					}
				}
			}
		}
		if(!berror){
			msg.setMsg(errormsg.getMsg());
			msg.setSuccess(errormsg.getSuccess());
		}else if (!bwarning){
			msg.setMsg(warningmsg.getMsg());
			msg.setSuccess(warningmsg.getSuccess());
		}
		return msg;
	}
	
	/**
	 * 获取活动转出（驳回）检查项列表,这个获取的是严重的
	 * @Title: getRouteCheckRules
	 * @author:yuxuebin
	 * @date：2016年04月01日 10:48:15
	 * @param route_id
	 * @param route_type
	 * @return
	 */
	public List<T_CHECKRULE> getRouteCheckRules(String route_id,String route_type) {
		String hql = MessageFormat
				.format(" id IN (SELECT CHECKRULEID FROM RT_ROUTECONDITION WHERE CHECKLEVEL=''1'' AND ROUTEID=''{0}'' AND ROUTETYPE=''{1}'')",
						route_id, route_type);
		List<T_CHECKRULE> checkRules = baseCommonDao.getDataList(T_CHECKRULE.class, hql);
		return checkRules;
	}

	/**
	 * 获取活动转出（驳回）检查项列表,这个获取的是警告的
	 * @Title: getRouteCheckRulesWarning
	 * @author:yuxuebin
	 * @date：2016年04月01日 10:48:15
	 * @param route_id
	 * @param route_type
	 * @return
	 */
	public List<T_CHECKRULE> getRouteCheckRulesWarning(String route_id,String route_type) {
		String hql = MessageFormat
				.format(" id IN (SELECT CHECKRULEID FROM RT_ROUTECONDITION WHERE CHECKLEVEL=''2'' AND ROUTEID=''{0}'' AND ROUTETYPE=''{1}'')",
						route_id, route_type);
		List<T_CHECKRULE> checkRules = baseCommonDao.getDataList(T_CHECKRULE.class, hql);
		return checkRules;
	}
	
	@SuppressWarnings("rawtypes")
	private ResultMessage checkEX(String xmbh) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("true");
		ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
		if (info == null) {
			msg.setSuccess("false");
			return msg;
		}
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("XMBH", xmbh);
		paramMap.put("PROJECT_ID", info.getProject_id());
		List<T_CHECKRULE> checkRules = getCheckRules(info);
		List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "xmbh='"+xmbh+"'");
		String ly = djdys.get(0).getLY();
		String baseworkflowname = HandlerFactory.getWorkflow(info.getProject_id()).getName();
		if(baseworkflowname.contains("BG")){
			ly="02";
		}
		boolean b = true;
		for (CheckRule rule : checkRules) {
			ResultMessage checkMsg=CheckEngine.executeCheck(baseCommonDao, rule, paramMap);
			if ("false".equals(checkMsg.getSuccess())) {
				msg.setSuccess("false");
				b = false;
				String substring="";
				 BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
				 String dystr="bdck.bdcs_h_gz";
				 if("02".equals(ly)) {
					 dystr="bdck.bdcs_h_xz";
				 }
				 if(bdcdylx.equals(BDCDYLX.SHYQZD)) {
					 dystr="bdck.bdcs_shyqzd_gz";
					 if("02".equals(ly)) {
						dystr="bdck.bdcs_shyqzd_xz";
					 }
				 }else if(bdcdylx.equals(BDCDYLX.YCH)) {
					 dystr="bdck.bdcs_h_xzy";
					 if("02".equals(ly)) {
						 dystr="bdck.bdcs_h_lsy";
					 }
				 }
				 msg.setMsg("");//设置为空字符,不显示null
				 if (msg.getMsg() != null && (msg.getMsg() != "")) {
						msg.setMsg(msg.getMsg() + "\n" + rule.getRESULTTIP() == null ? rule.getRESULTTIP() : msg.getMsg() + "\n" + rule.getRESULTTIP());
				 }else if(rule.getCLASSNAME().contains("单元相关")) {//LZCK609 LZCK610
					 String sql = rule.getEXECUTESQL();
					 String querysql ="select h.bdcdyh,h.zl "+sql;
					 if(dystr.contains("shyqzd")) {
						 querysql ="select zd.bdcdyh,zd.zl "+sql;
					 }
					 List<Map> dataList = baseCommonDao.getDataListByFullSql(querysql, paramMap);
					 if(dataList != null && dataList.size()>0) {
						 StringBuilder result = new StringBuilder();
						 for (Map map : dataList) {
							 String bdcdyh = (String) map.get("BDCDYH");
							 String zl = (String) map.get("ZL");
							 result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
						 }
						 substring =rule.getRESULTTIP()+ "<br/>"+substring;
						 msg.setMsg(rule.getRESULTTIP() == null ? "" : substring );
					 }else {
						 msg.setMsg(msg.getMsg() + "\n" + rule.getRESULTTIP() == null ? rule.getRESULTTIP() : msg.getMsg() + "\n" + rule.getRESULTTIP());
					 }
				} else if(rule.getCLASSNAME().contains("权利人相关")){
					if("CK1001".equals(rule.getId())) {
						//权利人不为空
						 String sql = rule.getEXECUTESQL();
						 sql ="select bdcdyh ,zl from "+dystr+" where bdcdyid in( select  bdcdyid  "+sql+")";
						 List<Map> dataList = baseCommonDao.getDataListByFullSql(sql, paramMap);
						 if(dataList != null && dataList.size()>0) {
							 StringBuilder result = new StringBuilder();
							 for (Map map : dataList) {
								 String bdcdyh = (String) map.get("BDCDYH");
								 String zl = (String) map.get("ZL");
								 result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
							 }
							 substring =rule.getRESULTTIP()+ "<br/>"+substring;
							 msg.setMsg(rule.getRESULTTIP() == null ? "" : substring );
						 }else {
							 msg.setMsg(msg.getMsg() + "\n" + rule.getRESULTTIP() == null ? rule.getRESULTTIP() : msg.getMsg() + "\n" + rule.getRESULTTIP());
						 }
					}else if("CK1012".equals(rule.getId())){
						//持证方式为共同持证并且有多个权利人的情况下必须设置持证人
						String sql = rule.getEXECUTESQL();
						sql ="select bdcdyh,zl from "+dystr+" where bdcdyid in (select  bdcdyid  from bdck.bdcs_ql_gz ql where "
								+ " ql.qlid in (select  qlr.qlid "+sql+"))";
						 List<Map> dataList = baseCommonDao.getDataListByFullSql(sql, paramMap);
						 if(dataList != null && dataList.size()>0) {
							 StringBuilder result = new StringBuilder();
							 for (Map map : dataList) {
								 String bdcdyh = (String) map.get("BDCDYH");
								 String zl = (String) map.get("ZL");
								 result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
							 }
							 substring =rule.getRESULTTIP()+ "<br/>"+substring;
							 msg.setMsg(rule.getRESULTTIP() == null ? "" : substring );
						 }else {
							 msg.setMsg(msg.getMsg() + "\n" + rule.getRESULTTIP() == null ? rule.getRESULTTIP() : msg.getMsg() + "\n" + rule.getRESULTTIP());
						 }
					}else if("CK1008".equals(rule.getId())){
						//多个权利人共有方式必须一致
						 msg.setMsg(rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP() );
					}
					else {
						String sql = rule.getEXECUTESQL();
						sql ="select bdcdyh,zl from "+dystr+" where bdcdyid in (select  bdcdyid  from bdck.bdcs_ql_gz ql where "
								+ " ql.qlid in (select  qlr.qlid "+sql+" ))";
						 List<Map> dataList = baseCommonDao.getDataListByFullSql(sql, paramMap);
						 if(dataList != null && dataList.size()>0) {
							 StringBuilder result = new StringBuilder();
							 for (Map map : dataList) {
								 String bdcdyh = (String) map.get("BDCDYH");
								 String zl = (String) map.get("ZL");
								 result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
							 }
							 substring =rule.getRESULTTIP()+ "<br/>"+substring;
							 msg.setMsg(rule.getRESULTTIP() == null ? "" : substring );
						 }else {
							 msg.setMsg(msg.getMsg() + "\n" + rule.getRESULTTIP() == null ? rule.getRESULTTIP() : msg.getMsg() + "\n" + rule.getRESULTTIP());
						 }
					}
				} else if(rule.getCLASSNAME().contains("权利相关")){
					if("CK2104".equals(rule.getId())) {
						//证书编号不能为空
						msg.setMsg(rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
					}  else {
						 String sql = rule.getEXECUTESQL();
						 sql ="select bdcdyh ,zl from "+dystr+" where bdcdyid in( select  bdcdyid  "+sql+")";
						 List<Map> dataList = baseCommonDao.getDataListByFullSql(sql, paramMap);
						 if(dataList != null && dataList.size()>0) {
							 StringBuilder result = new StringBuilder();
							 for (Map map : dataList) {
								 String bdcdyh = (String) map.get("BDCDYH");
								 String zl = (String) map.get("ZL");
								 result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
							 }
							 substring =rule.getRESULTTIP()+ "<br/>"+substring;
							 msg.setMsg(rule.getRESULTTIP() == null ? "" : substring );
						 }else {
							 msg.setMsg(msg.getMsg() + "\n" + rule.getRESULTTIP() == null ? rule.getRESULTTIP() : msg.getMsg() + "\n" + rule.getRESULTTIP());
						 }
					}
				}  else if(rule.getCLASSNAME().contains("业务逻辑")){
					if("CK4001".equals(rule.getId()) ||"CK4002".equals(rule.getId())) {
						 String sql = rule.getEXECUTESQL();
						 sql ="select bdcdyh ,zl from "+dystr+" where bdcdyid in( select  bdcdyid  "+sql+")";
						 List<Map> dataList = baseCommonDao.getDataListByFullSql(sql, paramMap);
						 if(dataList != null && dataList.size()>0) {
							 StringBuilder result = new StringBuilder();
							 for (Map map : dataList) {
								 String bdcdyh = (String) map.get("BDCDYH");
								 String zl = (String) map.get("ZL");
								 result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
							 }
							 substring =rule.getRESULTTIP()+ "<br/>"+substring;
							 msg.setMsg(rule.getRESULTTIP() == null ? "" : substring );
						 }else {
							 msg.setMsg(msg.getMsg() + "\n" + rule.getRESULTTIP() == null ? rule.getRESULTTIP() : msg.getMsg() + "\n" + rule.getRESULTTIP());
						 }
					}else if("CK4003".equals(rule.getId())) {
						msg.setMsg(rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
					}else {
						 String sql = rule.getEXECUTESQL();
						 String querysql ="select h.bdcdyh,h.zl "+sql;
						 if(dystr.contains("shyqzd")) {
							 querysql ="select zd.bdcdyh,zd.zl "+sql;
						 }
						 List<Map> dataList = baseCommonDao.getDataListByFullSql(querysql, paramMap);
						 if(dataList != null && dataList.size()>0) {
							 StringBuilder result = new StringBuilder();
							 for (Map map : dataList) {
								 String bdcdyh = (String) map.get("BDCDYH");
								 String zl = (String) map.get("ZL");
								 result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
							 }
							 substring =rule.getRESULTTIP()+ "<br/>"+substring;
							 msg.setMsg(rule.getRESULTTIP() == null ? "" : substring );
						 }else {
							 msg.setMsg(msg.getMsg() + "\n" + rule.getRESULTTIP() == null ? rule.getRESULTTIP() : msg.getMsg() + "\n" + rule.getRESULTTIP());
						 }
					}
				}  else if(rule.getCLASSNAME().contains("限制类")){
					 String sql = rule.getEXECUTESQL();
					 String querysql ="select h.bdcdyh,h.zl "+sql;
					 if(dystr.contains("shyqzd")) {
						  querysql ="select zd.bdcdyh,zd.zl "+sql;
					 }
					 List<Map> dataList = baseCommonDao.getDataListByFullSql(  querysql, paramMap);
					 if(dataList != null && dataList.size()>0) {
						 StringBuilder result = new StringBuilder();
						 for (Map map : dataList) {
							 String bdcdyh = (String) map.get("BDCDYH");
							 String zl = (String) map.get("ZL");
							 result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
						 }
						 substring =rule.getRESULTTIP()+ "<br/>"+substring;
						 msg.setMsg(rule.getRESULTTIP() == null ? "" : substring );
					 }else {
						 msg.setMsg(msg.getMsg() + "\n" + rule.getRESULTTIP() == null ? rule.getRESULTTIP() : msg.getMsg() + "\n" + rule.getRESULTTIP());
					 }
				} else {
					msg.setMsg(rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
				}
			}
		}
		if (b) {
			List<T_CHECKRULE> checkRulesWarning = getCheckRulesWarning(info);
			if (checkRulesWarning != null && checkRulesWarning.size() > 0) {
				for (CheckRule rule : checkRulesWarning) {
					ResultMessage checkMsg=CheckEngine.executeCheck(baseCommonDao, rule, paramMap);
					if ("false".equals(checkMsg.getSuccess())) {
						msg.setSuccess("warning");
						String substring="";
						 BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
						 String dystr="bdck.bdcs_h_gz";
						 if("02".equals(ly)) {
							 dystr="bdck.bdcs_h_xz";
						 }
						 if(bdcdylx.equals(BDCDYLX.SHYQZD)) {
							 dystr="bdck.bdcs_shyqzd_gz";
							 if("02".equals(ly)) {
								dystr="bdck.bdcs_shyqzd_xz";
							 }
						 }else if(bdcdylx.equals(BDCDYLX.YCH)) {
							 dystr="bdck.bdcs_h_xzy";
							 if("02".equals(ly)) {
								 dystr="bdck.bdcs_h_lsy";
							 }
						 }
						 msg.setMsg("");//设置为空字符,不显示null
						 if (msg.getMsg() != null && (msg.getMsg() != "")) {
								msg.setMsg(msg.getMsg() + "\n" + rule.getRESULTTIP() == null ? rule.getRESULTTIP() : msg.getMsg() + "\n" + rule.getRESULTTIP());
						 } else if(rule.getCLASSNAME().contains("单元相关")) {//LZCK609 LZCK610
							 String sql = rule.getEXECUTESQL();
							 String querysql ="select bdcdyh,zl "+sql;
							 /*if(dystr.contains("shyqzd")) {
								 querysql ="select zd.bdcdyh,zd.zl "+sql;
							 }*/
							 List<Map> dataList = baseCommonDao.getDataListByFullSql(querysql, paramMap);
							 if(dataList != null && dataList.size()>0) {
								 StringBuilder result = new StringBuilder();
								 for (Map map : dataList) {
									 String bdcdyh = (String) map.get("BDCDYH");
									 String zl = (String) map.get("ZL");
									 result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
								 }
								 substring =rule.getRESULTTIP()+ "<br/>"+substring;
								 msg.setMsg(rule.getRESULTTIP() == null ? "" : substring );
							 }else {
								 msg.setMsg(msg.getMsg() + "\n" + rule.getRESULTTIP() == null ? rule.getRESULTTIP() : msg.getMsg() + "\n" + rule.getRESULTTIP());
							 }
						} else if(rule.getCLASSNAME().contains("权利人相关")){
							if("CK1001".equals(rule.getId())) {
								//权利人不为空
								 String sql = rule.getEXECUTESQL();
								 sql ="select bdcdyh ,zl from "+dystr+" where bdcdyid in( select  bdcdyid  "+sql+")";
								 List<Map> dataList = baseCommonDao.getDataListByFullSql(sql, paramMap);
								 if(dataList != null && dataList.size()>0) {
									 StringBuilder result = new StringBuilder();
									 for (Map map : dataList) {
										 String bdcdyh = (String) map.get("BDCDYH");
										 String zl = (String) map.get("ZL");
										 result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
									 }
									 substring =rule.getRESULTTIP()+ "<br/>"+substring;
									 msg.setMsg(rule.getRESULTTIP() == null ? "" : substring );
								 }else {
									 msg.setMsg(msg.getMsg() + "\n" + rule.getRESULTTIP() == null ? rule.getRESULTTIP() : msg.getMsg() + "\n" + rule.getRESULTTIP());
								 }
							}else if("CK1012".equals(rule.getId())){
								//持证方式为共同持证并且有多个权利人的情况下必须设置持证人
								String sql = rule.getEXECUTESQL();
								sql ="select bdcdyh,zl from "+dystr+" where bdcdyid in (select  bdcdyid  from bdck.bdcs_ql_gz ql where "
										+ " ql.qlid in (select  qlid "+sql+"))";
								 List<Map> dataList = baseCommonDao.getDataListByFullSql(sql, paramMap);
								 if(dataList != null && dataList.size()>0) {
									 StringBuilder result = new StringBuilder();
									 for (Map map : dataList) {
										 String bdcdyh = (String) map.get("BDCDYH");
										 String zl = (String) map.get("ZL");
										 result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
									 }
									 substring =rule.getRESULTTIP()+ "<br/>"+substring;
									 msg.setMsg(rule.getRESULTTIP() == null ? "" : substring );
								 }else {
									 msg.setMsg(msg.getMsg() + "\n" + rule.getRESULTTIP() == null ? rule.getRESULTTIP() : msg.getMsg() + "\n" + rule.getRESULTTIP());
								 }
							}else if("CK1008".equals(rule.getId())){
								//多个权利人共有方式必须一致
								 msg.setMsg(rule.getRESULTTIP() == null ? "" : substring );
							} else {
								String sql = rule.getEXECUTESQL();
								sql ="select bdcdyh,zl from "+dystr+" where bdcdyid in (select  bdcdyid  from bdck.bdcs_ql_gz ql where "
										+ " ql.qlid in (select  qlr.qlid "+sql+" ))";
								 List<Map> dataList = baseCommonDao.getDataListByFullSql(sql, paramMap);
								 if(dataList != null && dataList.size()>0) {
									 StringBuilder result = new StringBuilder();
									 for (Map map : dataList) {
										 String bdcdyh = (String) map.get("BDCDYH");
										 String zl = (String) map.get("ZL");
										 result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
									 }
									 substring =rule.getRESULTTIP()+ "<br/>"+substring;
									 msg.setMsg(rule.getRESULTTIP() == null ? "" : substring );
								 }else {
									 msg.setMsg(msg.getMsg() + "\n" + rule.getRESULTTIP() == null ? rule.getRESULTTIP() : msg.getMsg() + "\n" + rule.getRESULTTIP());
								 }
							}
						} else if(rule.getCLASSNAME().contains("权利相关")){
							if("CK2104".equals(rule.getId())) {
								//证书编号不能为空
								String sql = rule.getEXECUTESQL();
								sql=" select bdcdyh ,zl from "+dystr+" where bdcdyid in (select bdcdyid from bdck.bdcs_qdzr_gz where zsid in (select zsid "+sql+"))";
								 List<Map> dataList = baseCommonDao.getDataListByFullSql(sql, paramMap);
								 if(dataList != null && dataList.size()>0) {
									 StringBuilder result = new StringBuilder();
									 for (Map map : dataList) {
										 String bdcdyh = (String) map.get("BDCDYH");
										 String zl = (String) map.get("ZL");
										 result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
									 }
									 substring =rule.getRESULTTIP()+ "<br/>"+substring;
									 msg.setMsg(rule.getRESULTTIP() == null ? "" : substring );
								 }else {
									 msg.setMsg(msg.getMsg() + "\n" + rule.getRESULTTIP() == null ? rule.getRESULTTIP() : msg.getMsg() + "\n" + rule.getRESULTTIP());
								 }
							}  else {
								 String sql = rule.getEXECUTESQL();
								 sql ="select bdcdyh ,zl from "+dystr+" where bdcdyid in( select  bdcdyid  "+sql+")";
								 List<Map> dataList = baseCommonDao.getDataListByFullSql(sql, paramMap);
								 if(dataList != null && dataList.size()>0) {
									 StringBuilder result = new StringBuilder();
									 for (Map map : dataList) {
										 String bdcdyh = (String) map.get("BDCDYH");
										 String zl = (String) map.get("ZL");
										 result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
									 }
									 substring =rule.getRESULTTIP()+ "<br/>"+substring;
									 msg.setMsg(rule.getRESULTTIP() == null ? "" : substring );
								 }else {
									 msg.setMsg(msg.getMsg() + "\n" + rule.getRESULTTIP() == null ? rule.getRESULTTIP() : msg.getMsg() + "\n" + rule.getRESULTTIP());
								 }
							}
						}  else if(rule.getCLASSNAME().contains("业务逻辑")){
							if("CK4001".equals(rule.getId()) ||"CK4002".equals(rule.getId())) {
								 String sql = rule.getEXECUTESQL();
								 sql ="select bdcdyh ,zl from "+dystr+" where bdcdyid in( select  bdcdyid  "+sql+")";
								 List<Map> dataList = baseCommonDao.getDataListByFullSql(sql, paramMap);
								 if(dataList != null && dataList.size()>0) {
									 StringBuilder result = new StringBuilder();
									 for (Map map : dataList) {
										 String bdcdyh = (String) map.get("BDCDYH");
										 String zl = (String) map.get("ZL");
										 result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
									 }
									 substring =rule.getRESULTTIP()+ "<br/>"+substring;
									 msg.setMsg(rule.getRESULTTIP() == null ? "" : substring );
								 }else {
									 msg.setMsg(msg.getMsg() + "\n" + rule.getRESULTTIP() == null ? rule.getRESULTTIP() : msg.getMsg() + "\n" + rule.getRESULTTIP());
								 }
							}else if("CK4003".equals(rule.getId())) {
								msg.setMsg(rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
							}else {
								 String sql = rule.getEXECUTESQL();
								 String querysql ="select h.bdcdyh,h.zl "+sql;
								 if(dystr.contains("shyqzd")) {
									 querysql ="select zd.bdcdyh,zd.zl "+sql;
								 }
								 List<Map> dataList = baseCommonDao.getDataListByFullSql(  querysql, paramMap);
								 if(dataList != null && dataList.size()>0) {
									 StringBuilder result = new StringBuilder();
									 for (Map map : dataList) {
										 String bdcdyh = (String) map.get("BDCDYH");
										 String zl = (String) map.get("ZL");
										 result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
									 }
									 substring =rule.getRESULTTIP()+ "<br/>"+substring;
									 msg.setMsg(rule.getRESULTTIP() == null ? "" : substring );
								 }else {
									 msg.setMsg(msg.getMsg() + "\n" + rule.getRESULTTIP() == null ? rule.getRESULTTIP() : msg.getMsg() + "\n" + rule.getRESULTTIP());
								 }
							}
						}  else if(rule.getCLASSNAME().contains("限制类")){
							 String sql = rule.getEXECUTESQL();
							 String querysql ="select h.bdcdyh,h.zl "+sql;
							 if(dystr.contains("shyqzd")) {
								  querysql ="select zd.bdcdyh,zd.zl "+sql;
							 }
							 List<Map> dataList = baseCommonDao.getDataListByFullSql(  querysql, paramMap);
							 if(dataList != null && dataList.size()>0) {
								 StringBuilder result = new StringBuilder();
								 for (Map map : dataList) {
									 String bdcdyh = (String) map.get("BDCDYH");
									 String zl = (String) map.get("ZL");
									 result.append(" 单元号: ").append(bdcdyh).append(" 坐落: ").append(zl).append("<br/>");
								 }
								 substring =rule.getRESULTTIP()+ "<br/>"+substring;
								 msg.setMsg(rule.getRESULTTIP() == null ? "" : substring );
							 }else {
								 msg.setMsg(msg.getMsg() + "\n" + rule.getRESULTTIP() == null ? rule.getRESULTTIP() : msg.getMsg() + "\n" + rule.getRESULTTIP());
							 }
						} else {
							msg.setMsg(rule.getRESULTTIP() == null ? "" : rule.getRESULTTIP());
						}
					}
				}
			}
		}
		return msg;
	}
	
}
