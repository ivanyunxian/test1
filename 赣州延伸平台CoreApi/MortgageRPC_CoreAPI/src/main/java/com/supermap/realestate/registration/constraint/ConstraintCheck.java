package com.supermap.realestate.registration.constraint;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.eval.Variable;
import parsii.tokenizer.ParseException;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.check.CheckItem;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.mapping.HandlerMapping.RegisterWorkFlow;
import com.supermap.realestate.registration.mapping.SelectorConfig;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.RT_BASECONSTRAINT;
import com.supermap.realestate.registration.model.RT_CONSTRAINT;
import com.supermap.realestate.registration.model.T_CONSTRAINT;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.util.ConstValue.CHECKLEVEL;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * 登簿检查类
 * 
 * @ClassName: BoardCheck
 * @author liushufeng
 * @date 2015年11月7日 下午6:56:30
 */
@Component
@Aspect
public class ConstraintCheck {

	@Autowired
	private CommonDao dao;

	/**** 受理约束检查:开始 *************/

	/**
	 * 插入检查切入点
	 * @Title: acceptCheckPointcut
	 * @author:liushufeng
	 * @date：2016年3月28日 下午3:56:32
	 */
	@Pointcut("execution(* com.supermap.realestate.registration.service.DYService.addBDCDY(..))||execution(* com.supermap.realestate.registration.service.DYService.checkAcceptable(..))")
	public void acceptCheckPointcut() {
		System.out.println("我是选择单元的时候的切入点");
	}

	/**
	 * 检查拦截器方法
	 * @Title: acceptCheck
	 * @author:liushufeng
	 * @date：2016年3月28日 下午3:56:45
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around("acceptCheckPointcut()")
	public Object acceptCheck(ProceedingJoinPoint pjp) throws Throwable {
		Object result = null;
		ResultMessage msg = new ResultMessage();
		Object[] params = pjp.getArgs();// 参数，包括了项目编号，权利ID或者不动产单元ID
		if (params != null && params.length > 0) {
			String bdcdyid = "";
			String qlid = "";
			String xmbh = StringHelper.formatObject(params[0]);
			String paramids = StringHelper.formatObject(params[1]);// 参数中的权利ID或者单元ID或者，有可能是多个
			ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);// 项目信息ProjectInfo

			if (info != null) {
				SelectorConfig config = HandlerFactory.getSelectorByName(info.getSelectorname());// 选择器
				String fieldname = config.getIdfieldname().toUpperCase(); // 选择器中配置的返回字段名
				String baseworkflowname = HandlerFactory.getWorkflow(info.getProject_id()).getName();// 基准流程ID
				String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(info.getProject_id()); // 业务流程编码

				// 做抵押变更的时候，选择的既不是QLID也不是BDCDYID，而是他项权证号，所以先把他项权证号转换为不动产单元ID
				if (fieldname.equals("TXQZH")) {
					List<String> list_ids = new ArrayList<String>();
					if (StringHelper.isEmpty(paramids)) {
						paramids = "";
					} else {
						List<Rights> _rightss = RightsTools.loadRightsByCondition(DJDYLY.XZ, "BDCQZH='" + paramids + "' AND QLLX='23'");
						if (_rightss != null && _rightss.size() > 0) {
							for (Rights _rights : _rightss) {
								List<BDCS_DJDY_XZ> djdys = dao.getDataList(BDCS_DJDY_XZ.class, "DJDYID='" + _rights.getDJDYID() + "'");
								if (djdys != null && djdys.size() > 0) {
									String bdcdyid_convert = djdys.get(0).getBDCDYID();
									if (!StringHelper.isEmpty(bdcdyid_convert)) {
										list_ids.add(bdcdyid_convert);
									}
								}
							}
						}
						paramids = StringHelper.formatList(list_ids, ",");
					}
				}

				// 以上代码得到一个用逗号连接的多个BDCDYID或者QLID组合的字符串,下面把他们放到List里边
				List<String> listbdcdyid = new ArrayList<String>();
				List<String> listqlid = new ArrayList<String>();

				// 如果是多个ID，循环，并且找出BDCDYID和QLID
				if (!StringHelper.isEmpty(paramids)) {
					String[] dyorqlids = paramids.split(",");
					for (int ii = 0; ii < dyorqlids.length; ii++) {
						String dyorqlid = dyorqlids[ii];
						if (!StringHelper.isEmpty(dyorqlid)) {
							if (fieldname.equals("QLID")) {
								qlid = dyorqlid;
								bdcdyid = getBdcdyidByQlid(qlid);
							} else {
								bdcdyid = dyorqlid;
							}
							listbdcdyid.add(bdcdyid);
							listqlid.add(qlid);
						}
					}
				}

				// 执行检查
				msg = executeMultiConstraints(listbdcdyid, listqlid, baseworkflowname, workflowcode, xmbh);
				if (msg.getSuccess().equals("false") || msg.getSuccess().equals("warning")) {
					return msg;
				}
			}
		}
		result = pjp.proceed();
		return result;
	}

	/**
	 * 根据权利ID获取不动产单元ID
	 * @Title: getBdcdyidByQlid
	 * @author:liushufeng
	 * @date：2016年3月28日 下午3:54:50
	 * @param qlid
	 *            权利ID
	 * @return 不动产单元ID
	 */
	private String getBdcdyidByQlid(String qlid) {
		String bdcdyid = null;
		Rights _rights = RightsTools.loadRights(DJDYLY.XZ, qlid);
		if (_rights != null) {
			List<BDCS_DJDY_XZ> djdys = dao.getDataList(BDCS_DJDY_XZ.class, "DJDYID='" + _rights.getDJDYID() + "'");
			if (djdys != null && djdys.size() > 0) {
				bdcdyid = djdys.get(0).getBDCDYID();
			}
		}
		return bdcdyid;
	}

	/**
	 * 拦截器方法2，不从选择器里判断是权利ID还是不动产单元ID，因为抵押变更实际上对应了两个选择器
	 * @Title: acceptCheckByBDCDYID
	 * @author:liushufeng
	 * @date：2016年3月28日 下午3:58:56
	 * @param bdcdyids
	 * @param xmbh
	 * @return
	 */
	public ResultMessage acceptCheckByBDCDYID(String bdcdyids, String xmbh) {
		ResultMessage msg = new ResultMessage();
		// 选择对应的单元ID和权利ID
		String bdcdyid = "";
		// 参数中的权利ID或者单元ID，有可能是多个
		String paramids = bdcdyids;
		// 项目信息ProjectInfo
		ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
		// 基准流程ID
		String baseworkflowname = HandlerFactory.getWorkflow(info.getProject_id()).getName();
		// 业务流程编码
		String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(info.getProject_id());

		// 以上代码得到一个用逗号连接的多个BDCDYID或者QLID组合的字符串,下面把他们放到List里边
		List<String> listbdcdyid = new ArrayList<String>();
		List<String> listqlid = new ArrayList<String>();

		// 如果是多个ID，循环，并且找出BDCDYID和QLID
		if (!StringHelper.isEmpty(paramids)) {
			String[] dyorqlids = paramids.split(",");
			for (int ii = 0; ii < dyorqlids.length; ii++) {
				String dyorqlid = dyorqlids[ii];
				if (!StringHelper.isEmpty(dyorqlid)) {
					bdcdyid = dyorqlid;
					listbdcdyid.add(bdcdyid);
					listqlid.add(null);
				}
			}
		}

		// 执行检查
		msg = executeMultiConstraints(listbdcdyid, listqlid, baseworkflowname, workflowcode, xmbh);
		if (msg.getSuccess().equals("false") || msg.getSuccess().equals("warning")) {
			return msg;
		}
		return msg;
	}

	/**
	 * 拦截器方法3，直接通过BDCDYID和流程编码进行检查
	 * @Title: acceptCheckByBDCDYIDByWorkflowCode
	 * @author:yuxuebin
	 * @date：2016年4月26日 22:19:56
	 * @param bdcdyids
	 * @param xmbh
	 * @return
	 */
	public ResultMessage acceptCheckByBDCDYIDByWorkflowCode(String bdcdyids, String workflowcode) {
		ResultMessage msg = new ResultMessage();
		// 选择对应的单元ID和权利ID
		String bdcdyid = "";
		String qlid = "";
		// 参数中的权利ID或者单元ID，有可能是多个
		String paramids = bdcdyids;
		RegisterWorkFlow baseWorkflow=HandlerFactory.getMapping().getWorkflow(workflowcode);
		SelectorConfig config = HandlerFactory.getSelectorByName(baseWorkflow.getSelector());// 选择器
		String fieldname = config.getIdfieldname().toUpperCase(); // 选择器中配置的返回字段名
		String baseworkflowname = baseWorkflow.getName();// 基准流程ID

		// 做抵押变更的时候，选择的既不是QLID也不是BDCDYID，而是他项权证号，所以先把他项权证号转换为不动产单元ID
		if (fieldname.equals("TXQZH")) {
			List<String> list_ids = new ArrayList<String>();
			if (StringHelper.isEmpty(paramids)) {
				paramids = "";
			} else {
				List<Rights> _rightss = RightsTools.loadRightsByCondition(DJDYLY.XZ, "BDCQZH='" + paramids + "' AND QLLX='23'");
				if (_rightss != null && _rightss.size() > 0) {
					for (Rights _rights : _rightss) {
						List<BDCS_DJDY_XZ> djdys = dao.getDataList(BDCS_DJDY_XZ.class, "DJDYID='" + _rights.getDJDYID() + "'");
						if (djdys != null && djdys.size() > 0) {
							String bdcdyid_convert = djdys.get(0).getBDCDYID();
							if (!StringHelper.isEmpty(bdcdyid_convert)) {
								list_ids.add(bdcdyid_convert);
							}
						}
					}
				}
				paramids = StringHelper.formatList(list_ids, ",");
			}
		}

		// 以上代码得到一个用逗号连接的多个BDCDYID或者QLID组合的字符串,下面把他们放到List里边
		List<String> listbdcdyid = new ArrayList<String>();
		List<String> listqlid = new ArrayList<String>();

		// 如果是多个ID，循环，并且找出BDCDYID和QLID
		if (!StringHelper.isEmpty(paramids)) {
			String[] dyorqlids = paramids.split(",");
			for (int ii = 0; ii < dyorqlids.length; ii++) {
				String dyorqlid = dyorqlids[ii];
				if (!StringHelper.isEmpty(dyorqlid)) {
					if (fieldname.equals("QLID")) {
						qlid = dyorqlid;
						bdcdyid = getBdcdyidByQlid(qlid);
					} else {
						bdcdyid = dyorqlid;
					}
					listbdcdyid.add(bdcdyid);
					listqlid.add(qlid);
				}
			}
		}

		// 执行检查
		msg = executeMultiConstraints(listbdcdyid, listqlid, baseworkflowname, workflowcode, "");
		if (msg.getSuccess().equals("false") || msg.getSuccess().equals("warning")) {
			return msg;
		}
		return msg;
	}
	
	public ResultMessage acceptCheckByBDCDYIDByProdefId(String bdcdyids,String prodef_Code) {
		ResultMessage msg = new ResultMessage();
		// 选择对应的单元ID和权利ID
		String bdcdyid = "";
		String qlid = "";
		// 参数中的权利ID或者单元ID，有可能是多个
		String paramids = bdcdyids;
//		RegisterWorkFlow baseWorkflow=HandlerFactory.getMapping().getWorkflow(workflowcode);
//		SelectorConfig config = HandlerFactory.getSelectorByName(baseWorkflow.getSelector());// 选择器
//		String fieldname = config.getIdfieldname().toUpperCase(); // 选择器中配置的返回字段名
//		String baseworkflowname = baseWorkflow.getName();// 基准流程ID
		List<Map> workflownameList = dao.getDataListByFullSql("select WORKFLOWNAME from bdc_workflow.WFD_MAPPING a "
				+ "  where WORKFLOWCODE = '"+prodef_Code+"' and WORKFLOWNAME is not null");
		if (workflownameList != null && workflownameList.size()>0) {
			String baseworkflowname = workflownameList.get(0).get("WORKFLOWNAME").toString();
			

			// 以上代码得到一个用逗号连接的多个BDCDYID或者QLID组合的字符串,下面把他们放到List里边
			List<String> listbdcdyid = new ArrayList<String>();
			List<String> listqlid = new ArrayList<String>();

			// 如果是多个ID，循环，并且找出BDCDYID和QLID
			if (!StringHelper.isEmpty(paramids)) {
				String[] dyorqlids = paramids.split(",");
				for (int ii = 0; ii < dyorqlids.length; ii++) {
					String dyorqlid = dyorqlids[ii];
					if (!StringHelper.isEmpty(dyorqlid)) {
						listbdcdyid.add(dyorqlid);
						listqlid.add(qlid);
					}
				}
			}

			// 执行检查
			msg = executeMultiConstraints(listbdcdyid, listqlid, baseworkflowname, prodef_Code, "");
			if (msg.getSuccess().equals("false") || msg.getSuccess().equals("warning")) {
				return msg;
			}
		}
		return msg;
		
	}
	
	/**
	 * 根据不动产单元ID数组和权利ID数组，对多个单元，多个约束项进行逐一检查
	 * @Title: executeMultiConstraints
	 * @author:liushufeng
	 * @date：2016年3月28日 下午3:35:52
	 * @param listbdcdyid
	 *            不动产单元ID数组
	 * @param listqlid
	 *            权利ID数组
	 * @param baseworkflowname
	 *            基准流程ID
	 * @param workflowcode
	 *            流程编码
	 * @param xmbh
	 *            项目编号
	 * @return ResultMessage
	 */
	public ResultMessage executeMultiConstraints(List<String> listbdcdyid, List<String> listqlid, String baseworkflowname, String workflowcode, String xmbh) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("true");
		boolean havewarning = false;
		boolean haveerror = false;
		String warningMessage = "";
		String errorMessage = "";

		List<RTCONSTRAINT> list = getConstraints(baseworkflowname, workflowcode);
		if (list != null && list.size() > 0) {
			// 第一层循环，循环选中的N个不动产单元
			for (int i = 0; i < listbdcdyid.size(); i++) {
				String bdcdyid = listbdcdyid.get(i);
				String qlid = listqlid.get(i);
				// 循环选中的N个不动产单元时都需要验证，不能存放在循环体外
				List<String> rtids = new ArrayList<String>();
				List<String> tids = new ArrayList<String>();
				// 第二层循环，循环流程配置的M个受理约束条件
				for (RTCONSTRAINT rt : list) {
					// 去重
					String rtid = rt.getId();
					if (!rtids.contains(rtid))
						rtids.add(rtid);
					else
						continue;

					String constraintid = rt.getCONSTRAINTID();

					// 去重
					if (!tids.contains(constraintid))
						tids.add(constraintid);
					else
						continue;
					T_CONSTRAINT constraint = dao.get(T_CONSTRAINT.class, constraintid);
					if (constraint == null)
						continue;

					// 对某个单元，根据某个给定的约束项进行检查，返回结果为true表示通过，结果为false标识不通过
					ResultMessage result = executeSingleConstraint(constraint, bdcdyid, qlid, xmbh);

					// 结果为false，检查不通过，接下来判断是警告还是严重
					if (!"true".equals(result.getSuccess())) {
						String tipsql = rt.getTIPSQL();
						String tipSqlResult = getSqlTip(tipsql, bdcdyid, qlid);
						if (CHECKLEVEL.WARNING.Value.equals(rt.getCHECKLEVEL())) {
							havewarning = true;
							if("CLASS".equals(constraint.getEXECUTETYPE())){
								if(StringHelper.isEmpty(result.getMsg())){
									warningMessage += StringHelper.isEmpty(rt.getRESULTTIP()) ? constraint.getId()+":"+constraint.getRESULTTIP() :  rt.getCONSTRAINTID()+":"+rt.getRESULTTIP();
									warningMessage = warningMessage + tipSqlResult + "<br/>";
								}else{
									warningMessage = warningMessage + result.getMsg() + "<br/>";
								}
							}else{
								warningMessage += StringHelper.isEmpty(rt.getRESULTTIP()) ? constraint.getId()+":"+constraint.getRESULTTIP() :  rt.getCONSTRAINTID()+":"+rt.getRESULTTIP();
								warningMessage = warningMessage + tipSqlResult + "<br/>";
							}
							
						} else if (CHECKLEVEL.ERROR.Value.equals(rt.getCHECKLEVEL())) {
							if("warn".equals(result.getSuccess())){
								havewarning = true;
								if("CLASS".equals(constraint.getEXECUTETYPE())){
									if(StringHelper.isEmpty(result.getMsg())){
										warningMessage += StringHelper.isEmpty(rt.getRESULTTIP()) ? constraint.getId()+":"+constraint.getRESULTTIP() :  rt.getCONSTRAINTID()+":"+rt.getRESULTTIP();
										warningMessage = warningMessage + tipSqlResult + "<br/>";
									}else{
										warningMessage = warningMessage + result.getMsg() + "<br/>";
									}
								}else{
									warningMessage += StringHelper.isEmpty(rt.getRESULTTIP()) ? constraint.getId()+":"+constraint.getRESULTTIP() :  rt.getCONSTRAINTID()+":"+rt.getRESULTTIP();
									warningMessage = warningMessage + tipSqlResult + "<br/>";
								}
							}else{
								haveerror = true;
								if("CLASS".equals(constraint.getEXECUTETYPE())){
									if(StringHelper.isEmpty(result.getMsg())){
										errorMessage += StringHelper.isEmpty(rt.getRESULTTIP()) ? constraint.getId()+":"+constraint.getRESULTTIP() : rt.getCONSTRAINTID()+":"+rt.getRESULTTIP();
										errorMessage = errorMessage + tipSqlResult + "<br/>";
									}else{
										errorMessage = errorMessage + result.getMsg() + "<br/>";
									}
								}else{
									errorMessage += StringHelper.isEmpty(rt.getRESULTTIP()) ? constraint.getId()+":"+constraint.getRESULTTIP() : rt.getCONSTRAINTID()+":"+rt.getRESULTTIP();
									errorMessage = errorMessage + tipSqlResult + "<br/>";
								}
							}
						}
					}
				}
			}
		}
		if (haveerror) {
			msg.setSuccess("false");
			msg.setMsg(errorMessage);
			return msg;
		}
		if (havewarning) {
			msg.setSuccess("warning");
			msg.setMsg(warningMessage);
			return msg;
		}
		return msg;
	}

	/**
	 * 对于未通过的约束项检查，执行配置的提示信息语句获取提示信息
	 * @Title: getSqlTip
	 * @author:liushufeng
	 * @date：2016年3月28日 下午3:38:08
	 * @param tipSql
	 *            配置的提示信息语句，记录在RT_BASECONSTRAINT表或者RT_BASECONSTRAINT表
	 * @param bdcdyid
	 *            不动产单元ID
	 * @param qlid
	 *            权利ID
	 * @return 结果提示信息
	 */
	private String getSqlTip(String tipSql, String bdcdyid, String qlid) {
		String strResult = "";
		HashMap<String, String> mapparams = new HashMap<String, String>();
		mapparams.put("BDCDYID", bdcdyid);
		mapparams.put("QLID", qlid);
		if (!StringHelper.isEmpty(tipSql)) {
			try {
				@SuppressWarnings("rawtypes")
				List<Map> mps = dao.getDataListByFullSql(tipSql, mapparams);
				if (mps != null && mps.size() > 0) {
					@SuppressWarnings("rawtypes")
					Map mp = mps.get(0);
					if (mp != null && mp.keySet() != null && mp.keySet().size() > 0) {
						for (Object keyname : mp.keySet()) {
							if (!StringHelper.isEmpty(keyname)) {
								String tipvalue = mp.get(keyname) == null ? "" : mp.get(keyname).toString();
								if (!StringHelper.isEmpty(tipvalue)) {
									strResult += "(" + tipvalue + ")";
								}
							}
						}
					}
				}
			} catch (Exception ee) {
			}
		}

		return strResult;
	}

	/**
	 * 获取约束集合，取的是对应关系的集合而不是约束的集合，因为约束的级别是配置在关系上的。
	 * RTCONSTRAINT是RT_BASECONSTRAINT和RT_CONSTRAINT都实现的接口
	 * @Title: getPostConstraints
	 * @author:liushufeng
	 * @date：2016年3月28日 下午2:06:25
	 * @param baseworkflowname
	 *            基准流程名称（CS001,CS002等）
	 * @param workflowcode
	 *            业务流程编码
	 * @return ist<RTCONSTRAINT>
	 */
	public List<RTCONSTRAINT> getConstraints(String baseworkflowname, String workflowcode) {
		String sql2 = MessageFormat.format("  BASEWORKFLOWID=''{0}'' AND CONSTRAINTID NOT IN(SELECT CONSTRAINTID FROM RT_CONSTRAINTEXP WHERE WORKFLOWCODE=''{1}'')",
				baseworkflowname, workflowcode);
		String sql3 = MessageFormat.format("   WORKFLOWCODE=''{0}''", workflowcode);
		Class<?> rtbaseclass = RT_BASECONSTRAINT.class;
		Class<?> rtbusiclass = RT_CONSTRAINT.class;
		@SuppressWarnings("unchecked")
		List<RTCONSTRAINT> listbase = (List<RTCONSTRAINT>) dao.getDataList(rtbaseclass, sql2);
		@SuppressWarnings("unchecked")
		List<RTCONSTRAINT> listbusi = (List<RTCONSTRAINT>) dao.getDataList(rtbusiclass, sql3);
		listbase.addAll(listbusi);
		return listbase;
	}

	/**
	 * 执行单个的受理约束检查
	 * @Title: executeSingleConstraint
	 * @author:liushufeng
	 * @date：2016年3月28日 下午2:54:03
	 * @param constraint
	 *            受理约束项
	 * @param bdcdyid
	 *            不动产单元ID
	 * @param qlid
	 *            权利ID
	 * @param xmbh
	 *            项目编号
	 * @return true或false
	 */
	private ResultMessage executeSingleConstraint(T_CONSTRAINT constraint, String bdcdyid, String qlid, String xmbh) {
		ResultMessage ms=new ResultMessage();
		ms.setSuccess("true");
		if ("SQL".equals(constraint.getEXECUTETYPE())) {
			boolean checkresult = executeSQLConstraint(bdcdyid, qlid, constraint.getEXECUTESQL(), constraint.getSQLRESULTEXP());
			if(checkresult){
				ms.setSuccess("true");
			}else{
				ms.setSuccess("false");
			}
		} else if ("CLASS".equals(constraint.getEXECUTETYPE())) {
			HashMap<String, String> mapparams = new HashMap<String, String>();
			mapparams.put("BDCDYID", bdcdyid);
			mapparams.put("QLID", qlid);
			mapparams.put("XMBH", xmbh);
			ms = executeCLASSConstraint(constraint.getEXECUTECLASSNAME(), mapparams);
		}

		return ms;
	}

	/**
	 * 执行SQL类型的受理约束检查
	 * @Title: executeSQLConstraint
	 * @author:liushufeng
	 * @date：2016年3月28日 下午3:28:50
	 * @param bdcdyid
	 *            不动产单元ID
	 * @param qlid
	 *            权利ID
	 * @param sqlexp
	 *            SQL语句
	 * @param resultexp
	 *            结果表达式
	 * @return true或false
	 */
	private boolean executeSQLConstraint(String bdcdyid, String qlid, String sqlexp, String resultexp) {
		boolean bresult = true;
		Map<String, String> mapparams = new HashMap<String, String>();
		mapparams.put("BDCDYID", bdcdyid);
		mapparams.put("QLID", qlid);
		sqlexp = sqlexp.replaceAll(":BDCDYID", "'" + bdcdyid + "'").replaceAll(":QLID", "'" + qlid + "'");
		long count = dao.getCountByFullSql(sqlexp);// dao.getCountByFullSql(sqlexp,
													// mapparams);
		try {
			bresult = calculateExpr(count, resultexp);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return bresult;
	}

	/**
	 * 执行"类"类型的受理约束检查
	 * @Title: executeCLASSConstraint
	 * @author:liushufeng
	 * @date：2016年3月28日 下午4:19:50
	 * @param bdcdyid
	 *            不动产单元ID
	 * @param qlid
	 *            权利ID
	 * @param className
	 *            可执行类名称
	 * @param xmbh
	 *            项目编号
	 * @return
	 */
	@SuppressWarnings("finally")
	private ResultMessage executeCLASSConstraint(String className, HashMap<String, String> mapparams) {
		ResultMessage ms=new ResultMessage();
		ms.setSuccess("false");
		try {
			CheckItem cons = (CheckItem) Class.forName(className).newInstance();
			if (cons != null) {
				ms = cons.check(mapparams);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			return ms;
		}
	}

	/**
	 * 将SQL语句的执行结果与配置的约束项的结果表达式进行对比计算，得到一个true或者false的结果
	 * @Title: calculateExpr
	 * @author:liushufeng
	 * @date：2016年3月28日 上午11:55:56
	 * @param result
	 *            SQL语句的执行结果
	 * @param strexpr
	 *            受理约束项配置的结果表达式
	 * @return 
	 *         如果结果跟结果表达式匹配，就返回true，否则返回false，例如：结果为1，结果表达式为">0",那么就返回true，如果表达式为
	 *         "<1"就返回false
	 * @throws ParseException
	 */
	public static boolean calculateExpr(long result, String strexpr) throws ParseException {
		Scope scope = Scope.create();
		Expression expr = (Expression) Parser.parse("RESULT" + strexpr, scope);
		Variable sqlresult = scope.getVariable("RESULT");
		sqlresult.setValue(result);
		// 此处为计算表达式，如果是数学表达式，那么返回计算后的数学结果，例如表达式为"1+1",返回的结果为2，
		// 如果是逻辑表达式，例如"1>0"那么，当结果为真的时候返回1，结果为假的时候返回-1
		double dresult = expr.evaluate();
		return dresult > 0;
	}
}
