/**   
 * @Title: CommonCheck.java 
 * @Package com.supermap.realestate.registration.config 
 * @author liushufeng 
 * @date 2015年7月25日 上午12:33:06 
 * @version V1.0   
 */

package com.supermap.realestate.registration.config;

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
import org.springframework.util.StringUtils;

import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.eval.Variable;
import parsii.tokenizer.ParseException;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.check.CheckItem;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.mapping.CheckConfig;
import com.supermap.realestate.registration.mapping.CheckConfig.CheckGroup;
import com.supermap.realestate.registration.mapping.ICheckItem;
import com.supermap.realestate.registration.mapping.SelectorConfig;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CHECKLEVEL;
import com.supermap.realestate.registration.util.ConstValue.ConstraintsType;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.model.Wfi_Spdy;
import com.supermap.wisdombusiness.workflow.model.Wfi_Spyj;

/**
 * 受理约束，转出控制，登簿检查
 * @ClassName: CommonCheck
 * @author liushufeng
 * @date 2015年7月25日 上午12:33:06
 */
@Component
@Aspect
public class CommonCheck {

	@Autowired
	private CommonDao dao;

	/**** 受理约束检查:开始 *************/

	@Pointcut("execution(* com.supermap.realestate.registration.service.DYService.addBDCDY(..))")
	public void acceptCheckPointcut() {
		System.out.println("我是选择单元的时候的切入点");
	}

	@Around("acceptCheckPointcut()")
	public Object acceptCheck(ProceedingJoinPoint pjp) throws Throwable {
		Object result = null;
		boolean pass = true;
		ResultMessage msg = new ResultMessage();
		// 参数，包括了项目编号，权利ID或者不动产单元ID
		Object[] params = pjp.getArgs();
		if (params != null && params.length > 0) {
			// 选择对应的单元ID和权利ID
			String bdcdyid = "";
			String qlid = "";

			String xmbh = StringHelper.formatObject(params[0]);
			String idd = StringHelper.formatObject(params[1]);
			ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
			SelectorConfig config = HandlerFactory.getSelectorByName(info.getSelectorname());
			String fieldname = config.getIdfieldname().toUpperCase();

			if (!StringHelper.isEmpty(idd)) {

				String[] dyorqlids = idd.split(",");
				for (int ii = 0; ii < dyorqlids.length; ii++) {
					String dyorqlid = dyorqlids[ii];
					if (!StringHelper.isEmpty(dyorqlid)) {
						if (fieldname.equals("QLID")) {
							qlid = dyorqlid;
							Rights _rights = RightsTools.loadRights(DJDYLY.XZ, qlid);
							if (_rights != null) {
								List<BDCS_DJDY_XZ> djdys = dao.getDataList(BDCS_DJDY_XZ.class, "DJDYID='" + _rights.getDJDYID() + "'");
								if (djdys != null && djdys.size() > 0) {
									bdcdyid = djdys.get(0).getBDCDYID();
								}
							}
						} else {
							bdcdyid = dyorqlid;
						}

						// 查询该单元状态是否在登记中 非查封登记类型的
						if (!StringUtils.isEmpty(bdcdyid) && !DJLX.CFDJ.Value.equals(info.getDjlx())) {
							String hql = " XMBH IN (SELECT id FROM BDCS_XMXX WHERE SFDB = '0' ) AND BDCDYID = '" + bdcdyid + "' ";
							List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, hql);
							if (null != djdys && djdys.size() > 0) {
								pass = false;
								msg.setSuccess("false");
								msg.setMsg("该单元正在办理中，不能选择!");
								return msg;
							}
						}

						String project_id = info.getProject_id();
						if (!StringHelper.isEmpty(project_id)) {
							String sql = "SELECT PRODEF_ID FROM BDC_WORKFLOW.WFI_PROINST WHERE FILE_NUMBER ='" + project_id + "'";
							@SuppressWarnings("rawtypes")
							List<Map> maps = dao.getDataListByFullSql(sql);
							if (maps != null && maps.size() > 0) {
								String workflowid = StringHelper.formatObject(maps.get(0).get("PRODEF_ID"));
								String sql2 = "select rt.workflowid,rt.constrainttype,t.* from bdck.BDCS_CONSTRAINTRT rt left join bdck.BDCS_CONSTRAINT t on t.id=rt.constraintid  where rt.workflowid=''{0}''";
								sql2 = MessageFormat.format(sql2, workflowid);
								DJDYLY ly = null;
								ly = DJDYLY.initFrom(config.getLy());
								BDCDYLX lx = null;
								lx = BDCDYLX.initFrom(config.getBdcdylx());

								@SuppressWarnings("rawtypes")
								List<Map> listconstraints = dao.getDataListByFullSql(sql2);
								if (listconstraints != null && listconstraints.size() > 0) {
									// 循环每个前置条件
									for (@SuppressWarnings("rawtypes")
									Map mapconstraint : listconstraints) {
										// 约束对应的类
										String description = StringHelper.formatObject(mapconstraint.get("DESCRIPTION"));
										String constraintname = StringHelper.formatObject(mapconstraint.get("NAME"));
										String className = StringHelper.formatObject(mapconstraint.get("CLASSNAME"));
										boolean b = true;
										if (mapconstraint.get("EXECUTETYPE") == null || !mapconstraint.get("EXECUTETYPE").toString().equals("SQL")) {
											if (StringHelper.isEmpty(className)) {
												pass = false;
												msg.setMsg("找不到约束：\"" + constraintname + "\"对应的类");
												break;
											}
											b = check(ly, lx, xmbh, bdcdyid, qlid, className);
										} else // 执行SQL检查
										{
											String sqlexp = StringHelper.formatObject(mapconstraint.get("SQLEXP"));
											String resultexp = StringHelper.formatObject(mapconstraint.get("RESULTEXP"));
											if (!StringHelper.isEmpty(sqlexp)) {
												b = checkAcceptable(bdcdyid, qlid, sqlexp, resultexp);
											}
										}

										// 前置条件是检查结果为true时通过，当前限制条件是检查结果为false时才通过
										if (StringHelper.formatObject(mapconstraint.get("CONSTRAINTTYPE")).equals(ConstraintsType.PRE.Value)) {
											if (!b) {
												if (description.contains("{BDCDYH}") || description.contains("{ZL}")) {
													RealUnit unit = UnitTools.loadUnit(lx, ly, bdcdyid);
													if (unit != null) {
														String oldchar = "{BDCDYH}";
														if (!StringHelper.isEmpty(unit.getBDCDYH())) {
															description = description.replace(oldchar, unit.getBDCDYH());
														}
														oldchar = "{ZL}";
														if (!StringHelper.isEmpty(unit.getZL())) {
															description = description.replace(oldchar, unit.getZL());
														}
													}
												}

												pass = false;
												msg.setSuccess("false");
												msg.setMsg(description);
												break;
											}
										} else if (StringHelper.formatObject(mapconstraint.get("CONSTRAINTTYPE")).equals(ConstraintsType.CUR.Value)) {
											if (b) {
												if (description.contains("{BDCDYH}") || description.contains("{ZL}")) {
													RealUnit unit = UnitTools.loadUnit(lx, ly, bdcdyid);
													if (unit != null) {
														String oldchar = "{BDCDYH}";
														description = description.replace(oldchar, unit.getBDCDYH());
														oldchar = "{ZL}";
														description = description.replace(oldchar, unit.getZL());
													}
												}
												pass = false;
												msg.setSuccess("false");
												msg.setMsg(description);
												break;
											}
										}
									}
								}
							}
						}
					}
				}
			} else {
				pass = false;
			}
		}
		if (!pass) {
			result = msg;
		} else {
			result = pjp.proceed();
		}
		return result;
	}

	@SuppressWarnings("finally")
	boolean check(DJDYLY ly, BDCDYLX lx, String xmbh, String bdcdyid, String qlid, String classname) {
		boolean bsatisfy = false;
		try {
			Constraint cons = (Constraint) Class.forName(classname).newInstance();
			if (cons != null) {
				bsatisfy = cons.check(dao, xmbh, bdcdyid, qlid, ly, lx);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			return bsatisfy;
		}
	}

	private boolean checkAcceptable(String bdcdyid, String qlid, String sqlexp, String resultexp) {
		boolean bresult = true;
		Map<String, String> mapparams = new HashMap<String, String>();
		mapparams.put("BDCDYID", bdcdyid);
		mapparams.put("QLID", qlid);
		long count = dao.getCountByFullSql(sqlexp, mapparams);

		try {
			bresult = calculateExpr(count, resultexp);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return bresult;
	}

	/**** 受理约束检查:结束 *************/

	/*** 登簿检查：开始 ***************/

	@Pointcut("execution(* com.supermap.realestate.registration.service.DBService.BoardBook(..))")
	public void boardBookPointcut() {
		System.out.println("我是登簿之前的切入点");
	}


	public Object boardBookCheck(ProceedingJoinPoint pjp) throws Throwable {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("true");
		msg.setMsg("检查通过");
		// 获取项目编号
		Object[] params = pjp.getArgs();
		if (params == null || params.length <= 0) {
			msg.setSuccess("false");
			msg.setMsg("全部检查都已经通过了！！");
			return msg;
		}
		// 判断项目info是否存在
		String xmbh = StringHelper.formatObject(params[0]);
		ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
		if (info == null)
			return msg;

		String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(info.getProject_id());
		CheckConfig cconfigs = HandlerFactory.getCheckConfig();
		if (cconfigs != null) {
			Map<String, String> itemmap = new HashMap<String, String>();
			List<CheckGroup> groups = cconfigs.getCheckGroupByCode(workflowcode);
			if (groups != null) {

				for (CheckGroup group : groups) {
					List<String> itemlist = group.getItems();
					for (String itemid : itemlist) {
						if (!itemmap.containsKey(itemid)) {
							msg = checkProjectItem(info.getProject_id(), xmbh, itemid, cconfigs);
							if (msg.getSuccess() != null && msg.getSuccess().toUpperCase().equals("FALSE")) {
								break;
							}
							itemmap.put(itemid, itemid);
						}
					}
					if (msg.getSuccess().toUpperCase().equals("FALSE"))
						break;
				}
			}
		}

		// 检查通过，继续
		if (msg.getSuccess().toUpperCase().equals("TRUE")) {
			msg = (ResultMessage) pjp.proceed();
		}
		return msg;
	}

	/*** 登簿检查：结束 ***************/

	/********************* 流程转出检查和其他检查（登簿检查） **********************/

	public static CommonDao getDao() {
		return SuperSpringContext.getContext().getBean(CommonDao.class);
	}

	public static Map<String, String> CheckProject(String project_id, List<String> itemids, List<String> groupids, String ROUTE_TYPE) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("BM", "1");
		map.put("MS", "");
		List<String> listError = new ArrayList<String>();
		List<String> listWarning = new ArrayList<String>();
		ResultMessage msg = new ResultMessage();
		CheckConfig config = HandlerFactory.getCheckConfig();
		if (config != null) {
			ProjectInfo info = ProjectHelper.GetPrjInfoByPrjID(project_id);
			if (info != null) {
				if (!StringHelper.isEmpty(info.getXmbh())) {
					for (String itemid : itemids) {
						ICheckItem item = config.getCheckItem(itemid);
						if (item != null) {
							msg = checkProjectItem(project_id, info.getXmbh(), itemid, config);
							if (msg.getSuccess().toUpperCase().equals("FALSE")) {
								if (item.getCHECKLEVEL().equals(CHECKLEVEL.ERROR.Value)) {
									listError.add(msg.getMsg());
								} else {
									listWarning.add(msg.getMsg());
								}
							}
						}
					}

					for (String groupid : groupids) {
						CheckGroup group = config.getCheckGroupByID(groupid);
						if (group != null) {
							List<String> listitemid = group.getItems();
							for (String itemid : listitemid) {
								if (!itemids.contains(itemid)) {
									ICheckItem item = config.getCheckItem(itemid);
									if (item != null) {
										msg = checkProjectItem(project_id,  info.getXmbh(),itemid, config);
										if (msg.getSuccess().toUpperCase().equals("FALSE")) {
											if (item.getCHECKLEVEL().equals(CHECKLEVEL.ERROR.Value)) {
												listError.add(msg.getMsg());
											} else {
												listWarning.add(msg.getMsg());
											}
										}
									}
								}
							}
						}
					}
					//添加审批意见的转出控制配置，若该环节配置可编辑意见 未填写或填写为空，则采用警告提示
					String XZQHDM=ConfigHelper.getNameByValue("XZQHDM");
					if(("0").equals(ROUTE_TYPE)&&(!StringHelper.isEmpty(XZQHDM)&&XZQHDM.contains("13"))){
						CommonDao da = getDao();
						StringBuilder builderSPYJ=new StringBuilder();
						builderSPYJ.append("SELECT TR_ACTDEFTOSPDY.SPDY_ID AS SPDY_ID,ACTINST.ACTINST_ID AS ACTINST_ID ");
						builderSPYJ.append("FROM BDC_WORKFLOW.WFI_PROINST PROINST ");
						builderSPYJ.append("LEFT JOIN BDC_WORKFLOW.WFI_ACTINST ACTINST ");
						builderSPYJ.append("ON PROINST.PROINST_ID=ACTINST.PROINST_ID ");
						builderSPYJ.append("LEFT JOIN BDC_WORKFLOW.WFI_TR_ACTDEFTOSPDY TR_ACTDEFTOSPDY ");
						builderSPYJ.append("ON TR_ACTDEFTOSPDY.ACTDEF_ID=ACTINST.ACTDEF_ID ");
						builderSPYJ.append("WHERE PROINST.FILE_NUMBER='");
						builderSPYJ.append(project_id).append("' ");
						builderSPYJ.append("AND ACTINST.ACTINST_STATUS IN (1,2) ");
						builderSPYJ.append("AND TR_ACTDEFTOSPDY.SPDY_ID IS NOT NULL ");
						builderSPYJ.append("AND TR_ACTDEFTOSPDY.SPDY_ID<>' ' ");
						builderSPYJ.append("AND TR_ACTDEFTOSPDY.READONLY=0");
						String strSql=builderSPYJ.toString();
						//查询当前环节标识ACTINST_ID和配置可编辑意见标识SPDY_ID
						@SuppressWarnings("rawtypes")
						List<Map> list=da.getDataListByFullSql(strSql);
						if(list!=null&&list.size()>0){
							String ACTINST_ID=StringHelper.formatObject(list.get(0).get("ACTINST_ID"));
							String SPDY_ID=StringHelper.formatObject(list.get(0).get("SPDY_ID"));
							if(!StringHelper.isEmpty(ACTINST_ID)&&!StringHelper.isEmpty(SPDY_ID)){
								boolean bTip=true;
								List<Wfi_Spyj> listspyj=da.getDataList(Wfi_Spyj.class, "ACTINST_ID='"+ACTINST_ID+"' AND SPDY_ID='"+SPDY_ID+"'");
								if(listspyj!=null&&listspyj.size()>0){
									for(Wfi_Spyj spyj:listspyj){
										if(!StringHelper.isEmpty(spyj.getSpyj())&&!StringHelper.isEmpty(spyj.getSpyj().trim())){
											bTip=false;
										}
									}
								}
								if(bTip){
									Wfi_Spdy spdy=da.get(Wfi_Spdy.class, SPDY_ID);
									if(spdy!=null){
										String spdymc=spdy.getSpmc();
										if(!StringHelper.isEmpty(spdymc)){
											String tipmsg=""+spdymc+"意见为空";
											listWarning.add(tipmsg);
										}
									}
								}
							}
						}
					}
				} else {
					//listError.add("没有选择任何单元");
				}
			} else {
				//listError.add("没有选择任何单元");
			}
			
			if (listError.size() > 0) {
				StringBuilder builder = new StringBuilder();
				for (int i = 0; i < listError.size(); i++) {
					builder.append(listError.get(i)).append("\n");
				}
				map.remove("BM");
				map.remove("MS");
				map.put("BM", "3");
				map.put("MS", builder.toString());
			} else if (listWarning.size() > 0) {
				StringBuilder builder = new StringBuilder();
				for (int i = 0; i < listWarning.size(); i++) {
					builder.append(listWarning.get(i)).append("\n");
				}
				map.remove("BM");
				map.remove("MS");
				map.put("BM", "2");
				map.put("MS", builder.toString());
			}
		}
		return map;
	}

	public static ResultMessage checkProjectItem(String project_id, String xmbh, String itemid, CheckConfig config) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("true");
		CommonDao da = getDao();
		boolean bresult = true;
		HashMap<String, String> mapparams = new HashMap<String, String>();
		mapparams.put("PROJECT_ID", project_id);
		mapparams.put("XMBH", xmbh);
		ICheckItem item = config.getCheckItem(itemid);
		if (item != null) {
			String checkType=item.getCHECKTYPE();
			if("CLASS".equals(checkType)){
				msg=executeClassCheck(item,mapparams);
			}else{
				String sql = item.getSQLEXPR();
				String resultexpr = item.getRESULTEXPR();
				
				long count = da.getCountByFullSql(sql, mapparams);

				try {
					bresult = calculateExpr(count, resultexpr);
					if (!bresult) {
						msg.setSuccess("false");
						msg.setMsg(item.getERRORINFO());
					} else {
						msg.setSuccess("true");
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		return msg;
	}

	public static boolean calculateExpr(long result, String strexpr) throws ParseException {
		boolean b = true;
		Scope scope = Scope.create();
		Expression expr = (Expression) Parser.parse("RESULT" + strexpr, scope);
		Variable SFJS = scope.getVariable("RESULT");
		SFJS.setValue(result);
		double dresult = expr.evaluate();
		if (dresult > 0)
			b = true;
		else
			b = false;
		return b;
	}

	/**
	 * 执行基于ConstraintInterface接口的类检查
	 * @Title: executeClassCheck
	 * @author:yuxuebin
	 * @date：2017年07月21日 14:51:37
	 * @param item
	 * @param paramMap
	 * @return
	 */
	private static ResultMessage executeClassCheck(ICheckItem item, HashMap<String, String> paramMap){
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg(item.getERRORINFO());
		CommonDao dao = getDao();
		String className = item.getSQLEXPR();
		if (StringHelper.isEmpty(className)){
			return msg;
		}
		Class<?> t=null;
		try {
			t = Class.forName(className);
		} catch (Exception e) {
			return msg;
		}
		if (StringHelper.isEmpty(t)){
			return msg;
		}
		CheckItem itemclass=null;
		try {
			itemclass = (CheckItem) t.newInstance();
		} catch (Exception e) {
			return msg;
		}
		if (item != null){
			msg = itemclass.check(paramMap);
		}
		return msg;
	}
}
