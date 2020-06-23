package com.supermap.wisdombusiness.workflow.service.wfm;

import com.supermap.realestate.registration.model.*;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.*;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.TreeInfo;
import com.supermap.wisdombusiness.workflow.service.wfd.*;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("processesService")
public class ProcessesService {

	@Autowired
	private ProDefXmlHelper _ProDefXmlHelper;

	@Autowired
	private SmProClass _SmProClass;

	@Autowired
	private SmActDef _SmActDef;

	@Autowired
	private SmRoleDef _SmRoleDef;

	@Autowired
	private SmProDef _SmProDef;

	@Autowired
	private CommonDao commondao;

	@Autowired
	private SmRouteDef _SmRouteDef;
	@Autowired
	private SmMaterialService smMaterialService;

	public void DataSave(InputStream _InputStream) {
		_ProDefXmlHelper.LoadData(_InputStream);
		_ProDefXmlHelper.Save();
	}

	public void DataSave(String XmlText) {
		_ProDefXmlHelper.LoadData(XmlText);
		_ProDefXmlHelper.Save();
	}

	public String GetData(String ProdefID) {
		return _ProDefXmlHelper.GetXmlData(ProdefID);
	}

	public Wfd_ProClass GetProClassByID(String id) {
		return _SmProClass.GetProClassByID(id);
	}

	public Wfd_Actdef GetActDefById(String id) {
		return _SmActDef.GetActDefByID(id);
	}

	public void UpdateActDef(Wfd_Actdef Actdef) {
		_SmActDef.UpdateActDef(Actdef);
	}

	public List<Wfd_ProClass> FindAllProClass() {
		return _SmProClass.FindAll();
	}

	public List<TreeInfo> GetRoleTree() {
		return _SmRoleDef.GetRoleTree();
	}

	public Wfd_Role GetWfdRoleById(String id) {
		return _SmRoleDef.GetWfdRoleById(id);
	}

	public void SaveOrUpdate(Wfd_Role Role) {
		_SmRoleDef.SaveOrUpdate(Role);
	}

	public Wfd_RoleClass GetWfdRoleClassById(String id) {
		return _SmRoleDef.GetWfdRoleClassById(id);
	}

	public void SaveOrUpdate(Wfd_RoleClass RoleClass) {
		_SmRoleDef.SaveOrUpdate(RoleClass);
	}

	public String CreateWfdRoleClassByName(String name, String pid, int index) {
		return _SmRoleDef.CreateWfdRoleClassByName(name, pid, index);
	}

	public String CreateWfdRoleByName(String name, String pid, int index) {
		return _SmRoleDef.CreateWfdRoleByName(name, pid, index);
	}

	public void DeleteWfdRoleById(String id) {
		_SmRoleDef.DeleteWfdRoleById(id);
	}

	public void DeleteWfdRoleClassById(String id) {
		_SmRoleDef.DeleteWfdRoleClassById(id);
	}

	/**
	 * 
	 * 完成工作流目录的顺序重构
	 * 
	 * @param from
	 *            来源
	 * @param to
	 *            目标
	 * @return 2015年10月15日
	 */
	public boolean RebuildWorkflowNodeIndex(String from, String to) {
		return _SmProClass.RebuildWorkflowNodeIndex(from, to);
	}

	/**
	 * 复制流程
	 * 
	 * @param prodefid
	 *            流程定义ID
	 * @param newprocode
	 *            流程编码
	 * @return
	 */

	public SmObjInfo CopyProDef(String prodefid, String newprocode, String rule) {
		JSONObject RuleObject = JSONObject.fromObject(rule);
		SmObjInfo msg = new SmObjInfo();
		Wfd_Prodef prodef = _SmProDef.GetProdefById(prodefid);
		Wfd_Prodef newprodef = new Wfd_Prodef();
		try {
			PropertyUtils.copyProperties(newprodef, prodef);
			newprodef.setProdef_Id(Common.CreatUUID());
			newprodef.setProdef_Code(newprocode);
			newprodef.setProdef_Name(prodef.getProdef_Name() + "(复制)");
			List<Wfd_Actdef> actlists = _SmActDef.GetActDefByProdefID(prodef
					.getProdef_Id());
			commondao.save(newprodef);
			if (actlists != null && actlists.size() > 0) {
				Map<String, String> RouteMapping = new HashMap<String, String>();
				for (Wfd_Actdef actdef : actlists) {// 复制活动
					Wfd_Actdef newactdef = new Wfd_Actdef();
					PropertyUtils.copyProperties(newactdef, actdef);
					newactdef.setActdef_Id(Common.CreatUUID());
					newactdef.setProdef_Id(newprodef.getProdef_Id());
					// 查找路由
					RouteMapping.put(actdef.getActdef_Id(),
							newactdef.getActdef_Id());
					commondao.save(newactdef);
					// 复制活动组件
					if (RuleObject.get("mod") != null
							&& RuleObject.getBoolean("mod")) {
						List<Wfd_Tr_ActToMod> actmods = commondao.getDataList(
								Wfd_Tr_ActToMod.class, Common.WORKFLOWDB
										+ "Wfd_Tr_ActToMod", "actdef_id='"
										+ actdef.getActdef_Id() + "'");
						if (actmods != null && actmods.size() > 0) {
							for (Wfd_Tr_ActToMod mod : actmods) {
								Wfd_Tr_ActToMod newmod = new Wfd_Tr_ActToMod();
								PropertyUtils.copyProperties(newmod, mod);
								newmod.setActtomodid(Common.CreatUUID());
								newmod.setActdef_Id(newactdef.getActdef_Id());
								commondao.save(newmod);
							}
						}
					}
					if (RuleObject.get("sp") != null
							&& RuleObject.getBoolean("sp")) {
						// 复制审批定义
						List<Wfi_Tr_ActDefToSpdy> spdys = commondao
								.getDataList(Wfi_Tr_ActDefToSpdy.class,
										Common.WORKFLOWDB
												+ "Wfi_Tr_ActDefToSpdy",
										"actdef_id='" + actdef.getActdef_Id()
												+ "'");
						if (spdys != null && spdys.size() > 0) {
							for (Wfi_Tr_ActDefToSpdy spdy : spdys) {
								Wfi_Tr_ActDefToSpdy newspdy = new Wfi_Tr_ActDefToSpdy();
								PropertyUtils.copyProperties(newspdy, spdy);
								newspdy.setActdefspdy_Id(Common.CreatUUID());
								newspdy.setActdef_Id(newactdef.getActdef_Id());
								commondao.save(newspdy);
							}
						}
					}
				}
				// 复制路由
				List<Wfd_Route> routes = _SmRouteDef
						.GetAllRouteByProdefid(prodefid);
				if (routes != null && routes.size() > 0) {
					for (Wfd_Route route : routes) {
						String actdefid = route.getActdef_Id();
						String nextactdefid = route.getNext_Actdef_Id();
						if (RouteMapping.get(actdefid) != null
								&& RouteMapping.get(nextactdefid) != null) {
							Wfd_Route newRoute = new Wfd_Route();
							PropertyUtils.copyProperties(newRoute, route);
							newRoute.setRoute_Id(Common.CreatUUID());
							newRoute.setActdef_Id(RouteMapping.get(actdefid)
									.toString());
							newRoute.setNext_Actdef_Id(RouteMapping.get(
									nextactdefid).toString());
							newRoute.setProdef_Id(newprodef.getProdef_Id());
							commondao.save(newRoute);
							// 复制路由控制条件
							if (RuleObject.get("condition") != null
									&& RuleObject.getBoolean("condition")) {
								List<RT_ROUTECONDITION> routeconditions = commondao
										.getDataList(
												RT_ROUTECONDITION.class,
												"BDCK.RT_ROUTECONDITION",
												"routeid='"
														+ route.getRoute_Id()
														+ "'");
								if (routeconditions != null
										&& routeconditions.size() > 0) {
									for (RT_ROUTECONDITION condition : routeconditions) {
										RT_ROUTECONDITION newcondition = new RT_ROUTECONDITION();
										PropertyUtils.copyProperties(
												newcondition, condition);
										newcondition.setId(Common.CreatUUID());
										newcondition.setROUTEID(newRoute
												.getRoute_Id());
										commondao.save(newcondition);
									}
								}
							}
						}
					}
				}
			}
			// 复制流程资料
			if (RuleObject.get("mater") != null
					&& RuleObject.getBoolean("mater")) {
				List<Wfd_ProMater> maters = commondao.getDataList(
						Wfd_ProMater.class, Common.WORKFLOWDB + "Wfd_ProMater",
						"prodef_id='" + prodef.getProdef_Id() + "'");
				if (maters != null && maters.size() > 0) {
					for (Wfd_ProMater mater : maters) {
						Wfd_ProMater newmater = new Wfd_ProMater();
						PropertyUtils.copyProperties(newmater, mater);
						newmater.setMaterial_Id(Common.CreatUUID());
						newmater.setProdef_Id(newprodef.getProdef_Id());
						commondao.save(newmater);
					}
				}
			}
			// 流程映射 WFD_MAPPING woflow_code外键

			String procode = prodef.getProdef_Code();
			if (RuleObject.get("mapping") != null
					&& RuleObject.getBoolean("mapping")) {
				List<WFD_MAPPING> mappings = commondao.getDataList(
						WFD_MAPPING.class, Common.WORKFLOWDB + "WFD_MAPPING",
						"workflowcode='" + procode + "'");
				if (mappings != null && mappings.size() > 0) {
					for (WFD_MAPPING mapping : mappings) {
						WFD_MAPPING newmap = new WFD_MAPPING();
						PropertyUtils.copyProperties(newmap, mapping);
						newmap.setId(Common.CreatUUID());
						newmap.setWORKFLOWCODE(newprocode);
						commondao.save(newmap);
					}
				}
			}
			// 受理约束 RT_CONSTRAINT 流程约束 RT_CONSTRAINTEXP 排除基准流程约束约束
			if (RuleObject.get("sl") != null && RuleObject.getBoolean("sl")) {
				List<RT_CONSTRAINT> constraints = commondao.getDataList(
						RT_CONSTRAINT.class, "BDCK.RT_CONSTRAINT",
						"workflowcode='" + procode + "'");
				if (constraints != null && constraints.size() > 0) {
					for (RT_CONSTRAINT constr : constraints) {
						RT_CONSTRAINT newconstr = new RT_CONSTRAINT();
						PropertyUtils.copyProperties(newconstr, constr);
						newconstr.setId(Common.CreatUUID());
						newconstr.setWORKFLOWCODE(newprocode);
						commondao.save(newconstr);
					}
				}
				List<RT_CONSTRAINTEXP> constraintexps = commondao.getDataList(
						RT_CONSTRAINTEXP.class, "BDCK.RT_CONSTRAINTEXP",
						"workflowcode='" + procode + "'");
				if (constraintexps != null && constraintexps.size() > 0) {
					for (RT_CONSTRAINTEXP rtexp : constraintexps) {
						RT_CONSTRAINTEXP newrtexp = new RT_CONSTRAINTEXP();
						PropertyUtils.copyProperties(newrtexp, rtexp);
						newrtexp.setId(Common.CreatUUID());
						newrtexp.setWORKFLOWCODE(newprocode);
						commondao.save(newrtexp);
					}
				}
			}
			// 登簿检查 RT_BOARDCHECK RT_BOARDCHECKEXP 排除基准流程
			if (RuleObject.get("borad") != null
					&& RuleObject.getBoolean("borad")) {
				List<RT_BOARDCHECK> boardchecks = commondao.getDataList(
						RT_BOARDCHECK.class, "BDCK.RT_BOARDCHECK",
						"workflowcode='" + procode + "'");
				if (boardchecks != null && boardchecks.size() > 0) {
					for (RT_BOARDCHECK boardcheck : boardchecks) {
						RT_BOARDCHECK newboardcheck = new RT_BOARDCHECK();
						PropertyUtils.copyProperties(newboardcheck, boardcheck);
						newboardcheck.setId(Common.CreatUUID());
						newboardcheck.setWORKFLOWCODE(newprocode);
						commondao.save(newboardcheck);
					}
				}
				List<RT_BOARDCHECKEXP> boardcheckexps = commondao.getDataList(
						RT_BOARDCHECKEXP.class, "BDCK.RT_BOARDCHECKEXP",
						"workflowcode='" + procode + "'");
				if (boardcheckexps != null && boardcheckexps.size() > 0) {
					for (RT_BOARDCHECKEXP boardcheck : boardcheckexps) {
						RT_BOARDCHECKEXP newboardcheckexp = new RT_BOARDCHECKEXP();
						PropertyUtils.copyProperties(newboardcheckexp,
								boardcheck);
						newboardcheckexp.setId(Common.CreatUUID());
						newboardcheckexp.setWORKFLOWCODE(newprocode);
						commondao.save(newboardcheckexp);
					}
				}
			}
			if (RuleObject.get("sf") != null
					&& RuleObject.getBoolean("sf")) {
				// 收费定义 BDCS_SFRELATION
				List<BDCS_SFRELATION> sfrelations = commondao.getDataList(
						BDCS_SFRELATION.class, "BDCK.BDCS_SFRELATION",
						"prodef_id='" + prodef.getProdef_Id() + "'");
				if (sfrelations != null && sfrelations.size() > 0) {
					for (BDCS_SFRELATION sf : sfrelations) {
						BDCS_SFRELATION newsf = new BDCS_SFRELATION();
						PropertyUtils.copyProperties(newsf, sf);
						newsf.setId(Common.CreatUUID());
						newsf.setPRODEF_ID(newprodef.getProdef_Id());
						commondao.save(newsf);
					}
				}
			}
			commondao.flush();
			msg.setDesc("复制成功");
			msg.setID("1");

		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return msg;
	}

	/**
	 * 新增流程版本
	 * @param prodefid
	 * @param verisonrule
	 * @return
	 */
	public List<TreeInfo> createVersion(String prodefid, String verisonrule) {
		List<TreeInfo>  info = new ArrayList<TreeInfo>();
		JSONObject RuleObject = JSONObject.fromObject(verisonrule);
		ResultMessage res = new ResultMessage();
		Wfd_Prodef prodef = _SmProDef.GetProdefById(prodefid);
		List<Wfd_Prodef> counts = commondao.getDataList(Wfd_Prodef.class,Common.WORKFLOWDB+"Wfd_Prodef","prodef_code='"+prodef.getProdef_Code()+"' order by version desc");
		Wfd_Prodef newprodef = new Wfd_Prodef();
		try {
			//复制
			PropertyUtils.copyProperties(newprodef, prodef);
			newprodef.setProdef_Id(Common.CreatUUID());
			newprodef.setProdef_Name(prodef.getProdef_Name());
			//TODO 复制流程产生新的流程版本号
			if(counts.size()>0){
				if(StringHelper.isEmpty(counts.get(0).getVersion())){
					counts.get(0).setVersion("1");
					prodef.setVersion("1");
				}
			}
			newprodef.setVersion(Integer.parseInt(counts.get(0).getVersion())+1+"");
			newprodef.setSourse_Id(prodef.getProdef_Id());
			//TODO:将源流程制成无效流程：
			prodef.setProdef_Status(0);
			commondao.saveOrUpdate(prodef);
			commondao.saveOrUpdate(newprodef);
			//复制流程对应的环节
			List<Wfd_Actdef> actlists = _SmActDef.GetALLActDefByProdefID(prodefid);
			ResultMessage copyActRes = copyActinst(actlists,RuleObject,newprodef.getProdef_Id(),prodefid);
			if(copyActRes.getSuccess().equals("SUCCESS")){
				//复制资料
				if (RuleObject.get("mater") != null && RuleObject.getBoolean("mater")) {
					ResultMessage copyMasterRes = copyMaster( RuleObject,  prodefid,
							newprodef.getProdef_Id());
					if(!copyMasterRes.getSuccess().equals("SUCCESS")){
						res.setMsg("创建过程出现错误!");
						res.setSuccess("ERROR");
						info = getProdefsByVersions(newprodef.getProdef_Id(),false);;
					}
					res.setMsg("创建版本完成!");
					res.setSuccess("SUCCESS");
					commondao.flush();
					info = getProdefsByVersions(newprodef.getProdef_Id(),false);
				}
				//TODO 复制流程映射  、受理约束条件  、登簿检查、收费定义(不需要在工作流中进行配置)
			}else{
				res.setMsg("创建版本过程出现错误!");
				res.setSuccess("ERROR");
				return info = getProdefsByVersions(newprodef.getProdef_Id(),false);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return info;
	}
	public ResultMessage copyActinst(List<Wfd_Actdef> sourceActList,JSONObject RuleObject,String newProdefid,String srcProdefid){
		ResultMessage res = new ResultMessage();
		res.setSuccess("SUCCESS");
		res.setMsg("复制成功!");
		if(sourceActList!=null&&sourceActList.size()>0){
			Map<String, String> RouteMapping = new HashMap<String, String>();
			for(Wfd_Actdef srcActdef :sourceActList){
				try{
					Wfd_Actdef  targetActdef = new Wfd_Actdef();
					PropertyUtils.copyProperties(targetActdef,srcActdef);
					targetActdef.setActdef_Id(Common.CreatUUID());
					targetActdef.setProdef_Id(newProdefid);
					// 查找路由
					RouteMapping.put(srcActdef.getActdef_Id(),
							targetActdef.getActdef_Id());
					commondao.saveOrUpdate(targetActdef);
					if(!RuleObject.isEmpty()){
						// 复制活动组件
						if (RuleObject.get("mod") != null
								&& RuleObject.getBoolean("mod")) {
							List<Wfd_Tr_ActToMod> actmods = commondao.getDataList(
									Wfd_Tr_ActToMod.class, Common.WORKFLOWDB+"Wfd_Tr_ActToMod", "actdef_id='"
											+ srcActdef.getActdef_Id() + "'");
							if (actmods != null && actmods.size() > 0) {
								for (Wfd_Tr_ActToMod mod : actmods) {
									Wfd_Tr_ActToMod newmod = new Wfd_Tr_ActToMod();
									PropertyUtils.copyProperties(newmod, mod);
									newmod.setActtomodid(Common.CreatUUID());
									newmod.setActdef_Id(targetActdef.getActdef_Id());
									commondao.saveOrUpdate(newmod);
								}
							}
						}
						//复制审批组件
						if (RuleObject.get("sp") != null
								&& RuleObject.getBoolean("sp")) {
							// 复制审批定义
							List<Wfi_Tr_ActDefToSpdy> spdys = commondao
									.getDataList(Wfi_Tr_ActDefToSpdy.class,Common.WORKFLOWDB+"Wfi_Tr_ActDefToSpdy",
											"actdef_id='" + srcActdef.getActdef_Id()
													+ "'");
							if (spdys != null && spdys.size() > 0) {
								for (Wfi_Tr_ActDefToSpdy spdy : spdys) {
									Wfi_Tr_ActDefToSpdy newspdy = new Wfi_Tr_ActDefToSpdy();
									PropertyUtils.copyProperties(newspdy, spdy);
									newspdy.setActdefspdy_Id(Common.CreatUUID());
									newspdy.setActdef_Id(targetActdef.getActdef_Id());
									commondao.saveOrUpdate(newspdy);
								}
							}
						}
					}
				}catch(Exception e){
					///复制过程出现错误
					res.setSuccess("ERROR");
					res.setMsg("复制过程出现错误!");
					e.printStackTrace();
				}
			}
			//获取源流程的所有路由
			List<Wfd_Route> routes = _SmRouteDef
					.GetAllRouteByProdefid(srcProdefid);
			if (routes != null && routes.size() > 0) {
				for (Wfd_Route route : routes) {
					try{
						String actdefid = route.getActdef_Id();
						String nextactdefid = route.getNext_Actdef_Id();
						if (RouteMapping.get(actdefid) != null
								&& RouteMapping.get(nextactdefid) != null) {
							Wfd_Route newRoute = new Wfd_Route();
							PropertyUtils.copyProperties(newRoute, route);
							newRoute.setRoute_Id(Common.CreatUUID());
							newRoute.setActdef_Id(RouteMapping.get(actdefid)
									.toString());
							newRoute.setNext_Actdef_Id(RouteMapping.get(
									nextactdefid).toString());
							newRoute.setProdef_Id(newProdefid);
							commondao.saveOrUpdate(newRoute);
						}
					}catch(Exception e ){
						res.setSuccess("ERROR");
						res.setMsg("路由复制过程出现错误!");
						e.printStackTrace();

					}
				}
			}
		}
		return res;
	}
	public ResultMessage copyMaster(JSONObject RuleObject, String srcProdefid,
									String targetPordefid) {
		// 复制流程资料
		ResultMessage res = new ResultMessage();
		List<Wfd_ProMater> maters = commondao.getDataList(
				Wfd_ProMater.class, Common.WORKFLOWDB+"Wfd_ProMater", "prodef_id='" + srcProdefid + "'");
		if (maters != null && maters.size() > 0) {
			for (Wfd_ProMater mater : maters) {
				Wfd_ProMater newmater = new Wfd_ProMater();
				try {
					PropertyUtils.copyProperties(newmater, mater);
					newmater.setMaterial_Id(Common.CreatUUID());
					newmater.setProdef_Id(targetPordefid);
					commondao.saveOrUpdate(newmater);
				} catch (Exception e) {
					res.setMsg("复制流程资料失败！");
					res.setSuccess("ERROR");
					return res;
				}
			}
			res.setMsg("复制流程资料成功！");
			res.setSuccess("SUCCESS");
		}else{
			res.setMsg("复制流程资料成功！");
			res.setSuccess("SUCCESS");
		}
		return res;
	}
	@SuppressWarnings("rawtypes")
	public List<TreeInfo> getProdefsByVersions(String prodefid,boolean flag) {
		List<TreeInfo>  info = new ArrayList<TreeInfo>();
		Wfd_Prodef p = commondao.get(Wfd_Prodef.class, prodefid);
		//查询流程
		String proCopy=
				"SELECT prodef_id,prodef_status, VERSION, case when  prodef_status=0 then  '版本号'||version||':'||prodef_Name ||'(无效)' else  '版本号'||version||':'||prodef_Name||'(有效)' end prodef_name\n" +
						" FROM bdc_workflow.wfd_prodef where prodef_code='"+p.getProdef_Code()+"'\n" +
						" order by version asc";
		List<Map> listCopProcess = commondao.getDataListByFullSql(proCopy);
		if(flag){
			for(int i=0;i<listCopProcess.size();i++){
				TreeInfo tree = new TreeInfo();
				tree.setId(listCopProcess.get(i).get("PRODEF_ID").toString());
				tree.setText(listCopProcess.get(i).get("PRODEF_NAME").toString());
				tree.setType("version");
				tree.setisParent(false);
				tree.setState(listCopProcess.get(i).get("PRODEF_STATUS").toString().toString());
				info.add(tree);
			}
		}else{
			TreeInfo tree = new TreeInfo();
			tree.setId(p.getProdef_Id());
			tree.setText("版本号:"+p.getVersion()+":"+p.getProdef_Name().toString()+"(无效)");
			tree.setType("version");
			tree.setisParent(false);
			tree.setState(p.getProdef_Status().toString());
			info.add(tree);
		}

		return info;
	}

	public List<TreeInfo> getProdefAsyncTree(String id, String isroute,
											 String type) {
		return _SmProDef.GetProdefAsyncTree(id, isroute,type);
	}

	public List<TreeInfo> updateProdefStatus(String params) {
		List<TreeInfo> info = new ArrayList<TreeInfo>();
		ResultMessage msg = new ResultMessage();
		if(StringHelper.isEmpty(params)){
			msg.setSuccess("ERROR");
			msg.setMsg("流程启用或禁用失败");
		}else{
			JSONObject  jsonObj=JSONObject.fromObject(params);
			if(jsonObj.containsKey("enable")){
				String enableids = jsonObj.getString("enable");
				if(!StringHelper.isEmpty(enableids)){
					commondao.updateBySql(" update "+Common.WORKFLOWDB+"wfd_prodef set "
							+ " PRODEF_STATUS=1 where prodef_id in("+enableids+")");
				}
				String disableids = jsonObj.getString("disable");
				if(!StringHelper.isEmpty(disableids)){
					commondao.updateBySql(" update "+Common.WORKFLOWDB+"wfd_prodef set "
							+ " PRODEF_STATUS=0 where prodef_id in("+disableids+")");
				}
				commondao.flush();
				String temp = enableids.split(",")[0];
				if(temp.equals("'_VERSION'")){
					temp = disableids.split(",")[0];
				}
				temp.substring(1, temp.length()-1);
				info = getProdefsByVersions(temp.substring(1, temp.length()-1),true);

			}
		}
		return info;
	}

	public boolean isCanChangeProdef(String prodefid) {
		List<Wfi_ProInst> proisntlist = commondao.findList(Wfi_ProInst.class, " prodef_id='"+prodefid+"'");
		if(proisntlist!=null&&proisntlist.size()>0){
			return false;
		}
		return true;
	}
}
