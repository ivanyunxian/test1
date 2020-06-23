package com.supermap.realestate.registration.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.ViewClass.BuildingTalbe;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.BuildingTalbe.BuildingInfo;
import com.supermap.realestate.registration.constraint.ConstraintCheck;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.T_BASEWORKFLOW;
import com.supermap.realestate.registration.model.T_SELECTOR;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.service.BuildingTableService;
import com.supermap.realestate.registration.service.DYService;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.SmProInfo;
import com.supermap.wisdombusiness.workflow.service.wfd.SmProDef;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProInstService;
@Service("buildingtableservice")
public class BuildingTableServicImpl implements BuildingTableService{
	
	@Autowired
    private CommonDao dao;
	
	@Autowired
	private ConstraintCheck constraintcheck;
	
	@Autowired
	private DYService dyService;

	@Autowired
	private SmProDef smProDef;
	
	@Autowired
	private SmStaff smStaff;
	
	@Autowired
	private SmProInstService smProInstService;
	
	/**
	 * 根据户条件查询楼盘表信息
	 * @作者 海豹
	 * @创建时间 2016年2月29日上午10:44:26
	 * @param bdcdyid,户不动产单元ID
	 * @param bdcdylx,不动产单元类型(H：031，YCH:032)
	 * @param ly,来源(调查库：01，现状层：02)
	 */
	@Override
	public BuildingTalbe queryBuildingTableByHouseCond(String bdcdyid, String bdcdylx,
			String ly) {	
		BuildingTalbe bt =new BuildingTalbe();
		bt=bt.queryBuildingTableByHouseCond(bdcdyid, bdcdylx,ly);
		return bt;		
	}
	/**
	 * 根据自然幢条件查询楼盘表信息
	 * @作者 海豹
	 * @创建时间 2016年2月29日上午10:48:01
	 * @param zrzbdcdyid,自然幢不动产单元ID
	 * @param bdcdylx,不动产单元类型(ZRZ：03，YCZRZ:08)
	 * @param ly,来源(调查库：01，现状层：02)
	 */
	@Override
	public BuildingTalbe queryBuildingTableByBuildingCond(String zrzbdcdyid,
			String bdcdylx, String ly) {
		BuildingTalbe bt=new BuildingTalbe();
		bt=bt.queryBuildingTableByBuildingCond(zrzbdcdyid, bdcdylx,ly);
		return bt;
	}
	/**
     * 根据户条件查询幢基本信息
     * @作者 海豹
     * @创建时间 2016年2月29日上午11:04:42
     * @param bdcdyid,户不动产单元ID
     * @param bdcdylx,不动产单元类型(H：031，YCH:032)
     * @param ly,来源(调查库：01，现状层：02)
     */
	@Override
	public BuildingInfo queryBuildingByHouseCond(String bdcdyid, String bdcdylx,
			String ly) {
		return null;
		
		
	}
	/**
     * 根据幢查询幢基本信息
     * @作者 海豹
     * @创建时间 2016年2月29日上午11:06:57
     * @param bdcdyid,自然幢不动产单元ID
     * @param bdcdylx,不动产单元类型(ZRZ：03，YCZRZ:08)
     * @param ly,来源(调查库：01，现状层：02)
     */
	@Override
	public BuildingInfo queryBuildingByBuildingCond(String bdcdyid, String bdcdylx,
			String ly) {
//		BuildingInfo binfo =new BuildingInfo();
		
		return null;
	}
	/**
     * 根据户条件查询期现房关系表
     * @作者 海豹
     * @创建时间 2016年2月29日上午11:09:52
     * @param bdcdyid,户不动产单元ID
     * @param bdcdylx,不动产单元类型(H：031，YCH:032)
     * @param ly,来源(调查库：01，现状层：02)
     */
	@Override
	public void queryYsgxByHouseCond(String bdcdyid, String bdcdylx, String ly) {
		
	}
	 /**
     * 根据自然幢条件查询期现房关系表
     * @作者 海豹
     * @创建时间 2016年2月29日上午11:12:30
     * @param bdcdyid,自然幢不动产单元ID
     * @param bdcdylx,不动产单元类型(ZRZ：03，YCZRZ:08)
     * @param ly,来源(调查库：01，现状层：02)
     */
	@Override
	public void queryYsgxByBuidingCond(String bdcdyid, String bdcdylx, String ly) {
		
	}
	@Override
	public BuildingTalbe queryBuildingTableByBuildingCond_new(
			String zrzbdcdyid, String bdcdylx, String ly, String szc, String hbdcdyid) {
		BuildingTalbe bt=new BuildingTalbe();
		bt=bt.queryBuildingTableByBuildingCond_new(zrzbdcdyid, bdcdylx,ly,szc,hbdcdyid);
		return bt;
	}

	@Override
	public BuildingTalbe queryBuildingTableByHouseCond_new(String bdcdyid,
			String bdcdylx, String ly, String szc, String hbdcdyid,boolean ljz) {	
		BuildingTalbe bt =new BuildingTalbe();
		bt=bt.queryBuildingTableByHouseCond_new(bdcdyid, bdcdylx,ly,szc,hbdcdyid,ljz);
		return bt;		
	}
	
	/* 
	 * 获取在楼盘表上勾选单元对应的权利信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Message getRightsInfo(String prodef_id, String ids) {
		Message msg = new Message();
		String righttype = "";
		Wfd_Prodef prodef = dao.get(Wfd_Prodef.class, prodef_id);
		if (prodef != null) {
			String prodefCode = prodef.getProdef_Code();
			if (!StringHelper.isEmpty(prodefCode)) {
				List<WFD_MAPPING> mps = dao.getDataList(WFD_MAPPING.class, "workflowcode='"+prodefCode+"'");
				if (mps != null) {
					String baseWorkflowName = mps.get(0).getWORKFLOWNAME();
					if (!StringHelper.isEmpty(baseWorkflowName)) {
						List<T_BASEWORKFLOW> wfl = dao.getDataList(T_BASEWORKFLOW.class, "id='"+baseWorkflowName+"'");
						if (wfl != null) {
							righttype = wfl.get(0).getBUILDINGSELECTTYPE();
						}
					}
				}
			}
		}

		List<Map> resultlist = new ArrayList<Map>();
		String slectsql = "";
		String fromsql = "";
		String wheresql = "";
		long count = 0;
		String[] unitids = ids.split(",");
		String houseids = "'" + StringHelper.formatList(StringHelper.Arrary2List(unitids), "','") + "'";
		if (!StringHelper.isEmpty(ids)) {
			if (!StringHelper.isEmpty(righttype)) {
				if (righttype.equals("CQ")) {
					slectsql = "SELECT ql.BDCDYID,ql.QLID,ql.BDCDYH,ql.BDCQZH,ql.QLLX,ql.DJLX,qlr.QLRMC,qlr.ZJH "; 
					fromsql = "FROM BDCK.BDCS_QL_XZ ql "  
					        + "LEFT JOIN BDCK.BDCS_QLR_XZ qlr ON ql.QLID = qlr.QLID ";
					wheresql = "WHERE ql.BDCDYID IN (" + houseids + ") AND (ql.QLLX = '4' OR ql.QLLX = '6' OR ql.QLLX = '8')";
					List<Map> tmplist = dao.getDataListByFullSql(slectsql + fromsql +wheresql);
					List<String> qlidlist = new ArrayList<String>();
					for (int i = 0; i < tmplist.size(); i++) {
						String qlid = StringHelper.formatObject(tmplist.get(i).get("QLID"));
						if (!qlidlist.contains(qlid)) {
							qlidlist.add(qlid);
							resultlist.add(tmplist.get(i));
						}else {
							for (int j = 0; j < resultlist.size(); j++) {
								String qlidn = StringHelper.formatObject(resultlist.get(j).get("QLID"));
								if (qlidn.equals(qlid)) {
									String qlrmc = StringHelper.formatObject(resultlist.get(j).get("QLRMC"));
									String qlrmcn = StringHelper.formatObject(tmplist.get(i).get("QLRMC"));
									String zjh = StringHelper.formatObject(resultlist.get(j).get("ZJH"));
									String zjhn = StringHelper.formatObject(tmplist.get(i).get("ZJH"));
									resultlist.get(j).put("QLRMC", qlrmc +","+ qlrmcn);
									resultlist.get(j).put("ZJH", zjh + "," + zjhn);
								}
							}
						}
					}
					msg.setTotal(resultlist.size());
					msg.setRows(resultlist);
					msg.setMsg("CQ");
				}else if (righttype.equals("DYQ")) {
					slectsql = "SELECT ql.BDCDYID,ql.QLID,ql.BDCDYH,ql.BDCQZH,ql.QLLX,ql.DJLX,fsql.DYR,qlr.QLRMC AS DYQR "; 
					fromsql = "FROM BDCK.BDCS_QL_XZ ql "  
					        + "LEFT JOIN BDCK.BDCS_FSQL_XZ fsql ON ql.FSQLID = fsql.FSQLID "
					        + "LEFT JOIN BDCK.BDCS_QLR_XZ qlr ON ql.QLID = qlr.QLID ";
					wheresql = "WHERE ql.BDCDYID IN (" + houseids + ") AND ql.QLLX = '23'";
					List<Map> tmplist = dao.getDataListByFullSql(slectsql + fromsql +wheresql);
					List<String> qlidlist = new ArrayList<String>();
					for (int i = 0; i < tmplist.size(); i++) {
						String qlid = StringHelper.formatObject(tmplist.get(i).get("QLID"));
						if (!qlidlist.contains(qlid)) {
							qlidlist.add(qlid);
							resultlist.add(tmplist.get(i));
						}else {
							for (int j = 0; j < resultlist.size(); j++) {
								String qlidn = StringHelper.formatObject(resultlist.get(i).get("QLID"));
								if (qlidn.equals(qlid)) {
									String qlrmc = StringHelper.formatObject(resultlist.get(i).get("QLRMC"));
									String qlrmcn = StringHelper.formatObject(tmplist.get(i).get("QLRMC"));
									String zjh = StringHelper.formatObject(resultlist.get(i).get("ZJH"));
									String zjhn = StringHelper.formatObject(tmplist.get(i).get("ZJH"));
									resultlist.get(i).put("QLRMC", qlrmc +","+ qlrmcn);
									resultlist.get(i).put("ZJH", zjh + "," + zjhn);
								}
							}
						}
					}
					msg.setTotal(resultlist.size());
					msg.setRows(resultlist);
					msg.setMsg("DYQ");
				}else if (righttype.equals("CF")) {
					slectsql = "SELECT ql.BDCDYID,ql.QLID,ql.BDCDYH,ql.BDCQZH,ql.QLLX,ql.DJLX,fsql.CFJG,fsql.CFWH "; 
					fromsql = "FROM BDCK.BDCS_QL_XZ ql "  
					        + "LEFT JOIN BDCK.BDCS_FSQL_XZ fsql ON ql.FSQLID = fsql.FSQLID ";
					wheresql = "WHERE ql.BDCDYID IN (" + houseids + ") AND ql.DJLX = '800'";
					count = dao.getCountByFullSql(fromsql + wheresql);
					resultlist = dao.getDataListByFullSql(slectsql + fromsql +wheresql);
					msg.setTotal(count);
					msg.setRows(resultlist);
					msg.setMsg("CF");
				}else if (righttype.equals("YY")) {
					slectsql = "SELECT ql.BDCDYID,ql.QLID,ql.BDCDYH,ql.BDCQZH AS YYZMH,ql.QLLX,ql.DJLX,fsql.YYSX,qlr.QLRMC AS YYQLR "; 
					fromsql = "FROM BDCK.BDCS_QL_XZ ql " 
							+ "LEFT JOIN BDCK.BDCS_FSQL_XZ fsql ON ql.FSQLID = fsql.FSQLID "
							+ "LEFT JOIN BDCK.BDCS_QLR_XZ qlr ON ql.QLID = qlr.QLID ";
					wheresql = "WHERE ql.BDCDYID IN (" + houseids + ") AND ql.DJLX = '600'";
					count = dao.getCountByFullSql(fromsql + wheresql);
					resultlist = dao.getDataListByFullSql(slectsql + fromsql +wheresql);
					msg.setTotal(count);
					msg.setRows(resultlist);
					msg.setMsg("YY");
				}else if (righttype.equals("XZ")) {
					slectsql = "SELECT xz.BDCDYID,xz.BDCDYH,xz.BDCQZH,xz.BXZRMC,xz.BXZRZJHM,xz.XZDW,xz.XZLX "; 
					fromsql = "FROM BDCK.BDCS_DYXZ xz "; 
					wheresql = "WHERE xz.BDCDYID IN (" + houseids + ")";
					count = dao.getCountByFullSql(fromsql + wheresql);
					resultlist = dao.getDataListByFullSql(slectsql + fromsql +wheresql);
					msg.setTotal(count);
					msg.setRows(resultlist);
					msg.setMsg("XZ");
				}else if (righttype.equals("DY")) {
					msg.setMsg("DY");
				}else if (righttype.equals("DCDY")) {
					msg.setMsg("DCDY");
				}else if (righttype.equals("ZYYG")) {
					slectsql = "SELECT ql.BDCDYID,ql.QLID,ql.BDCDYH,ql.BDCQZH,ql.QLLX,ql.DJLX,qlr.QLRMC,qlr.ZJH  "; 
					fromsql = "FROM BDCK.BDCS_QL_XZ ql " 
							+ "LEFT JOIN BDCK.BDCS_QLR_XZ qlr ON ql.QLID = qlr.QLID ";
					wheresql = "WHERE ql.BDCDYID IN (" + houseids + ") AND ql.DJLX = '700' AND ql.QLLX = '4'";
					count = dao.getCountByFullSql(fromsql + wheresql);
					resultlist = dao.getDataListByFullSql(slectsql + fromsql +wheresql);
					msg.setTotal(count);
					msg.setRows(resultlist);
					msg.setMsg("ZYYG");
				}else if (righttype.equals("DYYG")) {
					slectsql = "SELECT ql.BDCDYID,ql.QLID,ql.BDCDYH,ql.BDCQZH,ql.QLLX,ql.DJLX,fsql.DYR,qlr.QLRMC AS DYQR "; 
					fromsql = "FROM BDCK.BDCS_QL_XZ ql "
							+ "LEFT JOIN BDCK.BDCS_FSQL_XZ fsql ON ql.FSQLID = fsql.FSQLID "
							+ "LEFT JOIN BDCK.BDCS_QLR_XZ qlr ON ql.QLID = qlr.QLID ";
					wheresql = "WHERE ql.BDCDYID IN (" + houseids + ") AND ql.DJLX = '700' AND ql.QLLX = '23'";
					count = dao.getCountByFullSql(fromsql + wheresql);
					resultlist = dao.getDataListByFullSql(slectsql + fromsql +wheresql);
					msg.setTotal(count);
					msg.setRows(resultlist);
					msg.setMsg("DYYG");
				}
			}
		}
		return msg;
	}
	
	/* 
	 * 楼盘表受理
	 */
	@Override
	public HashMap<String,Object> buildingAccept(String bdcdyids, String qlids, String prodef_id, HttpServletRequest request, HttpServletResponse response) {
		HashMap<String,Object> result = new HashMap<String, Object>();
		String prodefCode = "";
		String idfieldname = "";
		String id = "";
		Wfd_Prodef prodef = dao.get(Wfd_Prodef.class, prodef_id);
		if (prodef != null) {
			prodefCode = prodef.getProdef_Code();
			if (!StringHelper.isEmpty(prodefCode)) {
				List<WFD_MAPPING> mps = dao.getDataList(WFD_MAPPING.class, "workflowcode='"+prodefCode+"'");
				if (mps != null) {
					String baseWorkflowName = mps.get(0).getWORKFLOWNAME();
					if (!StringHelper.isEmpty(baseWorkflowName)) {
						List<T_BASEWORKFLOW> wfl = dao.getDataList(T_BASEWORKFLOW.class, "id='"+baseWorkflowName+"'");
						if (wfl != null) {
							String selectorid = wfl.get(0).getSELECTORID();
							if (!StringHelper.isEmpty(selectorid)) {
								T_SELECTOR selector = dao.get(T_SELECTOR.class, selectorid);
								if (selector != null) {
									idfieldname = selector.getIDFIELDNAME();
									if (!StringHelper.isEmpty(idfieldname)) {
										if (idfieldname.equals("BDCDYID")) {
											id = bdcdyids;
										}else if (idfieldname.equals("QLID")) {
											id = qlids;
										}
									}
								}
							}
						}
					}
				}else {
					result.put("msg", "流程配置错误！");
					result.put("success", "false");
					return result;
				}
			}else {
				result.put("msg", "流程解析错误！");
				result.put("success", "false");
				return result;
			}
		}else {
			result.put("msg", "流程解析错误！");
			result.put("success", "false");
			return result;
		}
		result = CheckUnitstByBaseWorkflowName(prodef_id, prodefCode, id, request, response);
		return result;
	}
	
	/** 单元校验
	 * @param prodef_id
	 * @param prodefCode
	 * @param baseWorkflowName
	 * @param idfieldname
	 * @param id
	 * @return
	 */
	private HashMap<String, Object> CheckUnitstByBaseWorkflowName(String prodef_id, String prodefCode, String id, HttpServletRequest request, HttpServletResponse response) {
		HashMap<String,Object> result = new HashMap<String, Object>();
		ResultMessage ms = constraintcheck.acceptCheckByBDCDYIDByWorkflowCode(id, prodefCode);
		if(("false").equals(ms.getSuccess())) {
			result.put("状态", "严重"); 
			result.put("说明", ms.getMsg());
			return result;
		}else if (("warning").equals(ms.getSuccess())) {
			result.put("状态", "警告");
			result.put("说明", ms.getMsg());
			result.put("prodef_id", prodef_id);
			result.put("id", id);
			return result;
		}else{
			result.put("状态", "校验通过");
			result.put("说明", "可受理");
			result = AcceptProjectByBaseWorkflowName(prodef_id, id, request, response);
		}
		return result;
	}
	
	/** 单元受理
	 * @param prodef_id
	 * @param baseWorkflowName
	 * @param id
	 * @return
	 */
	@Override
	public HashMap<String, Object> AcceptProjectByBaseWorkflowName(String prodef_id, String id, HttpServletRequest request, HttpServletResponse response) {
		HashMap<String,Object> result = new HashMap<String, Object>();
		///创建项目
		BDCS_XMXX xmxx= CreateProject(prodef_id, request, response);
		String actinst_id = "";
		String project_id = xmxx.getPROJECT_ID();
		if (!StringHelper.isEmpty(prodef_id)) {
			List<Wfi_ProInst> proinst = dao.getDataList(Wfi_ProInst.class, "file_number='"+project_id+"'");
			if (proinst != null) {
				String proinst_id = proinst.get(0).getProinst_Id();
				if (!StringHelper.isEmpty(proinst_id)) {
					List<Wfi_ActInst> actinst = dao.getDataList(Wfi_ActInst.class, "proinst_id='"+proinst_id+"'");
					if (actinst != null) {
						actinst_id = actinst.get(0).getActinst_Id();
					}
				}
			}
		}
		result.put("actinst_id", actinst_id);
		//添加单元
		dyService.addBDCDYNoCheck(xmxx.getId(), id);
		result.put("状态", "受理成功");
		return result;
	}
	
	/*
	 * 根据流程定义id创建项目信息
	 */
	@SuppressWarnings("unused")
	private BDCS_XMXX CreateProject(String prodef_id, HttpServletRequest request, HttpServletResponse response){
		try{
			String staffid = Global.getCurrentUserInfo().getId();
			SmObjInfo object = BatchAcceptProject(prodef_id, staffid);
			String id = object.getID();
			String desc = object.getDesc();
			if (!"受理成功".equals(desc)) {
				return null;
			}
			// 刘树峰 2016.3.17 创建项目 获取xmbh
			String project_id = null;
			Wfi_ProInst proinst = dao.get(Wfi_ProInst.class, id);
			if (proinst != null) {
				project_id = proinst.getFile_Number();
			}
			//创建项目
			if (!StringHelper.isEmpty(project_id)) {
				ProjectInfo info = ProjectHelper.GetProjectFromRest(project_id, request);
			}
			BDCS_XMXX xmxx = Global.getXMXX(project_id);
			return xmxx;
		}catch(Exception ex){
			return null;
		}
	}
	
	public SmObjInfo BatchAcceptProject(String prodefid,String staffid) {
		if (prodefid != null && !prodefid.equals("")) {
			SmProInfo info = new SmProInfo();
			info.setProDef_ID(prodefid);
			Wfd_Prodef prodef = smProDef.GetProdefById(prodefid);
			String proDefName = smProDef.getproDefName(prodefid);
			info.setProDef_Name(proDefName);
			info.setLCBH(prodef.getProdef_Code());
			info.setProInst_Name("楼盘表受理");
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
