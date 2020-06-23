package com.supermap.realestate.registration.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.jcraft.jsch.ChannelSftp;
import com.supermap.realestate.registration.ViewClass.DJInfo;
import com.supermap.realestate.registration.ViewClass.JKSInfo;
import com.supermap.realestate.registration.ViewClass.JKSInfo.SRXM;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.SHB;
import com.supermap.realestate.registration.ViewClass.SQSPB;
import com.supermap.realestate.registration.ViewClass.SQSPBex;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.mapping.HandlerMapping.RegisterWorkFlow;
import com.supermap.realestate.registration.model.BDCS_CRHT;
import com.supermap.realestate.registration.model.BDCS_DBJS;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJFZ;
import com.supermap.realestate.registration.model.BDCS_DJGD;
import com.supermap.realestate.registration.model.BDCS_DJGDFS;
import com.supermap.realestate.registration.model.BDCS_DJSF;
import com.supermap.realestate.registration.model.BDCS_DJSZ;
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_IDCARD_PIC;
import com.supermap.realestate.registration.model.BDCS_QDZR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_LS;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_REPORTINFO;
import com.supermap.realestate.registration.model.BDCS_SFDY;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_LS;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_TDYT_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZRHT;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.interfaces.TDYT;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.service.DBService;
import com.supermap.realestate.registration.service.ProjectService_DA;
import com.supermap.realestate.registration.service.Sender.dataReport;
import com.supermap.realestate.registration.tools.QueryTools;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.DJZT;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.ConstValue.SFDB;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.ConstValue.YXBZ;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.web.ui.tree.Tree;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.wfd.SmHoliday;
import com.supermap.wisdombusiness.workflow.service.wfi.SmActInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProMater;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProSPService;
import com.supermap.wisdombusiness.workflow.util.DesHelper;

@Service("projectService_da")
public class ProjectServiceImpl_DA implements ProjectService_DA {

	@Autowired
	private SmProMater _SmProMater;

	@Autowired
	private CommonDao baseCommonDao;

	@Autowired
	private SmActInst smActInst;

	@Autowired
	private SmStaff smStaff;

	private static final Log logger = LogFactory
			.getLog(ProjectServiceImpl_DA.class);

	/** 登薄service */
	@Autowired
	private DBService dbService;
	@Autowired
	private SmHoliday smHoliday;

	/**
	 * mass
	 */
	@Override
	public ProjectInfo getProjectInfo(String project_id,
			HttpServletRequest request) throws IOException {
		/*
		 * 处理逻辑：首先去BDCS_XMXX表里边根据project_id去查找，如果能够查找到，
		 * 就根据XMXX初始化一个ProjectInfo，否则，调用工作流提供的rest接口读取
		 * 项目相关信息，生成BDCS_XMXX记录，保存到数据库，然后初始化ProjectInfo
		 */
		ProjectInfo projectInfo = null;
		BDCS_XMXX xmxx = Global.getXMXX(project_id);
		if (xmxx != null) {
			projectInfo = ProjectHelper.GetPrjInfoByPrjID(project_id);
		} else {
			projectInfo = ProjectHelper.GetProjectFromRest(project_id, request);
		}
		// 根据流程ID获取房屋信息是否可编辑
		// 获取流程 编号
		String workflowcode = ProjectHelper
				.getWorkflowCodeByProjectID(project_id);
		RegisterWorkFlow flow = HandlerFactory.getWorkflow(project_id);
		if (!StringHelper.isEmpty(workflowcode) && flow != null) {
			if (flow.getHouseeditmap() != null
					&& flow.getHouseeditmap().containsKey(workflowcode)) {
				String houseedit = flow.getHouseeditmap().get(workflowcode);
				if (SF.YES.Value.equals(houseedit)) {
					projectInfo.setHouseedit(true);
				}
				String landedit = flow.getLandeditmap().get(workflowcode);
				if (SF.YES.Value.equals(landedit)) {
					projectInfo.setLandedit(true);
				}
				String buildingtable = flow.getBuildingTablemap().get(
						workflowcode);
				if (SF.YES.Value.equals(buildingtable)) {
					projectInfo.setShowBuildingTable(true);
				}
				// 本地化配置里的数据上报按钮的控制-总开关
				String isOpen = ConfigHelper.getNameByValue("ISOPEN");
				// 业务流程配置里的开关
				String dataReportBtn = flow.getDataReportBtneditmap().get(
						workflowcode); 
				if ("1".equals(isOpen)) { 
					if (SF.YES.Value.equals(dataReportBtn)) {
						projectInfo.setShowDataReportBtn("1");
					}
				}else if ("2".equals(isOpen)) {
					if (SF.YES.Value.equals(dataReportBtn)) {
						projectInfo.setShowDataReportBtn("2");
					}
				}else {
					projectInfo.setShowDataReportBtn("0");
				}
				String dyfs = flow.getDyfsmap().get(workflowcode);
				if (!StringHelper.isEmpty(dyfs)) {
					projectInfo.setDyfs(dyfs);
				}

				String unitpageid = flow.getUnitpageidmap().get(workflowcode);
				if (!StringHelper.isEmpty(unitpageid)) {
					projectInfo.setUnitpageid(unitpageid);
				}

				String rightpageid = flow.getRightpageidmap().get(workflowcode);
				if (!StringHelper.isEmpty(rightpageid)) {
					projectInfo.setRightpageid(rightpageid);
				}
			}

		}
		projectInfo.setXzqhdm(ConfigHelper.getNameByValue("XZQHDM"));
		String isenable=ConfigHelper.getNameByValue("IS_ENABLE");		
		projectInfo.setIsEnableNewGis(isenable.equals("1"));
		String isenableredflag=ConfigHelper.getNameByValue("IS_ENABLEREDFLAG");		
		projectInfo.setIsEnableRedFlag(isenableredflag.equals("1"));
		return projectInfo;
	}

	@Autowired
	private SmProSPService smProSPService;
 
  
    /**
     * mass
     */
	@Override
	public BDCS_DJGD getDjgdInfo(String xmbh, String projectid) {
		BDCS_DJGD djgd = null;
		StringBuilder hqlCondition = new StringBuilder();
		hqlCondition.append("  XMBH ='").append(xmbh).append("' ");
		hqlCondition.append(" AND YWH ='").append(projectid).append("' ");
		List<BDCS_DJGD> list = baseCommonDao.getDataList(BDCS_DJGD.class,
				hqlCondition.toString());
		if (list != null && list.size() > 0) {
			djgd = list.get(0);
		} else {
			StringBuilder hql = new StringBuilder();
			StringBuilder qzh = new StringBuilder();
			hql.append(" id in (SELECT QLID FROM BDCS_QDZR_GZ WHERE XMBH = '")
					.append(xmbh).append("') ");
			List<BDCS_QL_GZ> qlList = baseCommonDao.getDataList(
					BDCS_QL_GZ.class, hql.toString());
			// for (int i = 0; i < qlList.size(); i++) {
			// BDCS_QL_XZ ql = qlList.get(0);
			// if(qlList.size() - 1 == i){
			// qzh.append(ql.getBDCQZH());
			// }else{
			// qzh.append(ql.getBDCQZH()).append(",");
			// }
			// }
			if (qlList != null && qlList.size() > 0) {
				BDCS_QL_GZ ql = qlList.get(0);
				if (!StringUtils.isEmpty(ql.getBDCQZH())) {
					String[] bdcqzh = ql.getBDCQZH().split(",");
					if (bdcqzh.length > 0) {
						qzh.append(bdcqzh[0]);
						djgd = new BDCS_DJGD();
						djgd.setQZHM(qzh.toString());
					}
				}else{
					djgd = new BDCS_DJGD();
					djgd.setQZHM("");
				}
			} else {
				djgd = new BDCS_DJGD();
				djgd.setQZHM("");
			}
		}
		
		List<Wfi_ProInst> listproinst=baseCommonDao.getDataList(Wfi_ProInst.class, "FILE_NUMBER='"+projectid+"'");
		if(listproinst.size()>0){
			Wfi_ProInst proinst=listproinst.get(0);
			String proef_name=proinst.getProdef_Name();
			StringBuffer sb=new StringBuffer();
			if(!StringHelper.isEmpty(proef_name)){
				String[] arrname=proef_name.split(",");
				String lastname=proef_name.replaceAll(",", "-");
				if(arrname!=null&&arrname.length>1){
					djgd.setDJDL(arrname[0]);
					djgd.setDJXL(lastname.substring(5, lastname.length()));
				}else{
					djgd.setDJDL(arrname[0]);
					djgd.setDJXL(arrname[0]);
				}
			
			}
			
		}
		return djgd;
	}

	 
	/**
	 * mass
	 */
	@Override
	public SQSPBex GetSQSPBex(String xmbh, String acinstid,
			HttpServletRequest request) {
		SQSPBex sqspb = new SQSPBex().build(xmbh, acinstid, smProSPService,
				request);
		return sqspb;// .Create(xmbh,acinstid,
						// baseCommonDao,smProSPService,request);
	}

	 
 

	/**
	 * 获取申请审核表
	 * 
	 * 
	 * @author:mss
	 * @param xmbh
	 * @return
	 */
	@Override
	public SHB GetSHB(String project_id) {
		SHB shb = new SHB();
		return shb.createSHB(project_id);

	}

	 
	/**
	 * 获取登簿信息
	 * 
	 * @author:mss
	 * @param project_id
	 * @return
	 */

	@SuppressWarnings("unchecked")
	public Map<String, Object> GetDBXX(String project_id) {

		Map<String, Object> map = new HashMap<String, Object>();
		// 1,不动产单元号
		// String bdcdyh="" ;
		// 2、坐落
		// String zl="" ;
		// 3、权利类型
		String qllx = "";
		// 4、权证号
		// String bdcqzh = "";
		// 5 权利人
		// String qlrmc = "";
		// 6 项目编号
		String xmbh = "";
		// 7、 单元类型
		// String dylx="" ;
		// 8 登记类型
		String djlx = "";
		// 权利类型名称
		String qllxmc = "";
		// 单元类型名称
		String dylxmc = "";
		// 登记类型名称
		String djlxmc = "";
		// old项目编号
		String oldxmbh = "";

		// 1.通过project_id获取项目信息
		ProjectInfo info = ProjectHelper.GetPrjInfoByPrjID(project_id);
		if (!StringHelper.isEmpty(info)) {
			xmbh = info.getXmbh();
			djlx = info.getDjlx();
			djlxmc = info.getDjlxmc();
			map.put("xmbh", xmbh);
			map.put("djlx", djlx);
			map.put("djlxmc", djlxmc);

			List<Map> lstdjdy = new ArrayList<Map>();
			// 获取登记单元信息
			List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(
					BDCS_DJDY_GZ.class, ProjectHelper.GetXMBHCondition(xmbh));
			String bdcdylxmc = "";
			for (BDCS_DJDY_GZ bdcs_djdy_gz : djdys) {
				Map dy = new HashMap();
				BDCDYLX bdcdylx = BDCDYLX.initFrom(bdcs_djdy_gz.getBDCDYLX());
				DJDYLY dyly = DJDYLY.initFrom(bdcs_djdy_gz.getLY());
				RealUnit unit = UnitTools.loadUnit(bdcdylx, dyly,
						bdcs_djdy_gz.getBDCDYID());
				if (!StringHelper.isEmpty(unit)) {
					dy.put("bdcdyh", unit.getBDCDYH());
					dy.put("zl", unit.getZL());
					dy.put("bdcdylx", unit.getBDCDYLX().Value);
					bdcdylxmc = ConstHelper.getNameByValue("BDCDYLX",
							unit.getBDCDYLX().Value);
					dy.put("bdcdylxmc", bdcdylxmc);
					dy.put("bdcdyid", bdcs_djdy_gz.getBDCDYID());

				}
				Rights rights = RightsTools.loadRightsByDJDYID(DJDYLY.GZ, xmbh,
						bdcs_djdy_gz.getDJDYID());
				// Rights rights =RightsTools.loadRightsByCondition(DJDYLY.XZ,
				// "DJDYID='"+bdcs_djdy_gz.getDJDYID()+"'");

				Map ql = new HashMap();
				if (!StringHelper.isEmpty(rights)) {
					qllxmc = ConstHelper.getNameByValue("QLLX",
							rights.getQLLX());
					ql.put("qllx", rights.getQLLX());
					ql.put("qllxmc", qllxmc);
					ql.put("dbsj", rights.getDJSJ());
					if (djlx.equals("400")
							|| (djlx.equals("800") && ql.get("qllx").equals(
									"98"))
							|| (djlx.equals("500") && ql.get("qllx").equals(
									"23"))) {
						String lyqlid = rights.getLYQLID();
						BDCS_QL_LS dyql = baseCommonDao.get(BDCS_QL_LS.class,
								lyqlid);
						oldxmbh = dyql.getXMBH();
						dy.put("oldxmbh", oldxmbh);
					}
					List<Map> lstqlr = new ArrayList<Map>();
					List<RightsHolder> lstrh = RightsHolderTools
							.loadRightsHolders(DJDYLY.GZ, rights.getId());
					{
						if (lstrh.size() == 0) {
							List<BDCS_SQR> listsqr = baseCommonDao.getDataList(
									BDCS_SQR.class, "XMBH='" + xmbh
											+ "' AND SQRLB='1'");
							if (listsqr.size() > 0) {
								for (BDCS_SQR holder : listsqr) {
									Map qlr = new HashMap();
									qlr.put("qlrmc", holder.getSQRXM());
									qlr.put("zjlx", holder.getZJLX());
									qlr.put("zjh", holder.getZJH());
									qlr.put("bdcqzh", rights.getBDCQZH());
									List<BDCS_QDZR_GZ> qdzrs = baseCommonDao
											.getDataList(BDCS_QDZR_GZ.class,
													"QLID ='" + rights.getId()
															+ "' AND XMBH  ='"
															+ xmbh + "'");
									if (!StringHelper.isEmpty(qdzrs)) {
										if (qdzrs.size() > 0) {
											BDCS_ZS_GZ zs = baseCommonDao.get(
													BDCS_ZS_GZ.class, qdzrs
															.get(0).getZSID());
											if (!StringHelper.isEmpty(zs)) {
												qlr.put("bdcqzh",
														zs.getBDCQZH());
											}
										}
									}

									lstqlr.add(qlr);
								}
							}
						} else {
							for (RightsHolder holder : lstrh) {
								Map qlr = new HashMap();
								qlr.put("qlrmc", holder.getQLRMC());
								qlr.put("zjlx", holder.getZJZL());
								qlr.put("zjh", holder.getZJH());
								qlr.put("bdcqzh", holder.getBDCQZH());
								List<BDCS_QDZR_GZ> qdzrs = baseCommonDao
										.getDataList(BDCS_QDZR_GZ.class,
												"QLID ='" + rights.getId()
														+ "' AND QLRID  ='"
														+ holder.getId() + "'");
								if (!StringHelper.isEmpty(qdzrs)) {
									if (qdzrs.size() > 0) {
										BDCS_ZS_GZ zs = baseCommonDao.get(
												BDCS_ZS_GZ.class, qdzrs.get(0)
														.getZSID());
										if (!StringHelper.isEmpty(zs)) {
											qlr.put("bdcqzh", zs.getBDCQZH());
										}
									}
								}

								lstqlr.add(qlr);
							}
						}

					}
					ql.put("qlr", lstqlr);
				} else {
					List<Map> lstqlr = new ArrayList<Map>();
					List<BDCS_DYXZ> listsqr = baseCommonDao.getDataList(
							BDCS_DYXZ.class, "XMBH='" + xmbh + "'");
					if (listsqr.size() > 0) {
						for (BDCS_DYXZ holder : listsqr) {
							Map qlr = new HashMap();
							qlr.put("qlrmc", holder.getBXZRMC());
							qlr.put("zjlx", holder.getBXZRZJZL());
							qlr.put("zjh", holder.getBXZRZJHM());
							qlr.put("bdcqzh", holder.getBDCQZH());
							lstqlr.add(qlr);
						}

					}
					ql.put("qlr", lstqlr);
				}
				dy.put("ql", ql);
				lstdjdy.add(dy);
			}
			map.put("dys", lstdjdy);
		}

		return map;
	}

	 
	 
	/**
	 * mass
	 */
	@Override
	public HashMap<String, List<HashMap<String, Object>>> GetDBXX2(
			String project_id) {
		// TODO Auto-generated method stub
		return QueryTools.GetXMInfo(project_id);
	}

	 
	 
	/**
	 * mass
	 */
	@Override
	public void UpdateXMXX(String lsh,String ajh) {
		
		List<BDCS_XMXX> listxxs=baseCommonDao.getDataList(BDCS_XMXX.class, "YWLSH='"+lsh+"'");
        if(listxxs.size()>0){
      	  BDCS_XMXX onexmxx=listxxs.get(0);
      	  onexmxx.setAJH(ajh);
      	  baseCommonDao.saveOrUpdate(onexmxx);
        }
	}


	@Override
	public HashMap<String, List<HashMap<String, Object>>> GetDBXX3(String ywlsh) {
		List<BDCS_XMXX> listxxs=baseCommonDao.getDataList(BDCS_XMXX.class, "YWLSH='"+ywlsh+"'");
		if(listxxs==null||listxxs.size()<=0){
			return new HashMap<String, List<HashMap<String, Object>>>();
		}
		return QueryTools.GetXMInfo(listxxs.get(0).getPROJECT_ID());
	}

 
}
