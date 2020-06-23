package com.supermap.yingtanothers.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.mapping.HandlerMapping;
import com.supermap.realestate.registration.mapping.HandlerMapping.RegisterWorkFlow;
import com.supermap.realestate.registration.model.BDCS_CONST;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.DCS_H_GZ;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.xmlmodel.BDCQLR;
import com.supermap.realestate.registration.service.DYService;
import com.supermap.realestate.registration.service.InsertDataService;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.DA_DBHelper;
import com.supermap.realestate.registration.util.JH_DBHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.gxjyk.Gxjhxm;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYH;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYXZDJ;
import com.supermap.wisdombusiness.framework.model.gxjyk.MATERDATA;
import com.supermap.wisdombusiness.framework.model.gxjyk.PROMATER;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.model.Wfi_MaterData;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProMater;
import com.supermap.yingtanothers.model.BGQH;
import com.supermap.yingtanothers.model.CFDJ;
import com.supermap.yingtanothers.model.DYAQ;
import com.supermap.yingtanothers.model.FDCQ;
import com.supermap.yingtanothers.model.GXJHXM;
import com.supermap.yingtanothers.model.H;
import com.supermap.yingtanothers.model.QLR;
import com.supermap.yingtanothers.model.R_FW_QL;
import com.supermap.yingtanothers.model.YGDJ;
import com.supermap.yingtanothers.model.YYDJ;
import com.supermap.yingtanothers.service.QueryShareXxService;

/**
 * 
 * 作者： 苗利涛 
 * 时间： 2016年6月20日 上午11:20:42 
 * 功能：鹰潭市一键式提取房管局共享信息控制实现类
 */
@Service("QueryShareXxService")
public class QueryShareXxServiceImpl implements QueryShareXxService {

	@Autowired
	private InsertDataService insertDataService;
	@Autowired
	private com.supermap.wisdombusiness.framework.dao.impl.CommonDaoJY CommonDaoJY;
	private com.supermap.realestate.registration.handlerImpl.DYBGDJHandler DYBGDJHandler;
	private com.supermap.realestate.registration.handlerImpl.GZDJHandler GZDJHandler;
	@Autowired
	private CommonDao baseCommonDao;
	@Autowired
	private DYService dyservice;
	
	/**
	 * 登记流程列表
	 */
	private List<RegisterWorkFlow> workflows = new ArrayList<HandlerMapping.RegisterWorkFlow>();

	// 房屋状态，1为期房，2现房
	protected String fwzt = "2";
	// 所有义务人
	private String ywrs = "";

	@Override
	public String ExtractSXFromZJK(String casenum, String xmbh, boolean bool) {
		String flag = "false";
		try {
			// connection = JH_DBHelper.getConnect_jy();
			// 从房产库中获取他项权利表中其他必要信息
			Map<BDCS_QL_GZ, BDCS_FSQL_GZ> rightMap = GetQLData(casenum, xmbh);
			BDCS_QL_GZ ql = null;
			BDCS_FSQL_GZ fsql = null;
			if (rightMap.size() > 0) {
				for (Map.Entry<BDCS_QL_GZ, BDCS_FSQL_GZ> entry : rightMap.entrySet()) {
					ql = entry.getKey();
					fsql = entry.getValue();
				}
			}
			// 获取权利人
			List<BDCQLR> QlrList = GetQLR(casenum);
			List<QLR> JYQlrList = GetYWR(casenum);
			// 拼接所有义务人
			getAllYWR(JYQlrList);

			// 业务类型判断
			String xmbhcond = ProjectHelper.GetXMBHCondition(xmbh);
			List<BDCS_XMXX> xmxx = baseCommonDao.getDataList(BDCS_XMXX.class, xmbhcond);
			String PROJECT_ID = xmxx.get(0).getPROJECT_ID();
			HandlerMapping _mapping = HandlerFactory.getMapping();
			String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(PROJECT_ID);
			String _handleClassName = _mapping.getHandlerClassName(workflowcode);// "CSDJHandler";
			String qllx = xmxx.get(0).getQLLX();
//			String _handleClassName = null;
//			RegisterWorkFlow _flow = _mapping.getWorkflow(workflowcode);
//			if (_flow != null){
//				_handleClassName = _flow.getHandlername();
//			}	
			// 如果是抵押变更、所有权变更、更正登记、初始登记
				if (_handleClassName.equals("BGDJHandler")) { // _handleClassName.contains("DYBGDJHandler")||
																// _handleClassName.contains("GZDJHandler")
					// 获取在办数据的RealtionID list
					List<String> bdcdyhList = GetRealtionIDList(casenum);// GetbdcdyhList(casenum);
					if (bdcdyhList == null || bdcdyhList.size() < 1) {
						flag = "nofwztorh";
						return flag;
					}
					if (bdcdyhList.isEmpty() || bdcdyhList == null) {
						flag = "warning";
						return flag;
					}
					// 变更、更正、初始登记外的单元信息是直接作为工作层，故在此传取单元list
					List<H> HList = GetH(casenum);
					List<BGQH> bgqhList = GetBGQH(casenum);
					flag = InsertSXFromZJKBG(ql, fsql, xmbhcond, casenum, QlrList, JYQlrList, bgqhList, HList, bool, fwzt);
					return flag;
				} else {
					// 其他登记业务
					// 获取在办数据的RealtionID list
					// String xzqdm =ConfigHelper.getNameByValue("XZQHDM");
					List<String> bdcdyhList = new ArrayList<String>();
					bdcdyhList = GetRealtionIDList(casenum);// GetbdcdyhList(casenum);
					
					if (bdcdyhList.isEmpty() || bdcdyhList == null) {
						flag = "warning";
						return flag;
					}
					
					if (bdcdyhList.get(0).equals("nofwzt")) {
						flag = "nofwztorh";
						return flag;
					}
					// }
					

					// 因除变更、更正、初始登记外的单元信息是从现状层拷贝到工作层，故在此传空单元list
					List<H> HList = new ArrayList<H>();
					flag = InsertSXFromZJK(ql, fsql, xmbh, casenum, QlrList, JYQlrList, bdcdyhList, HList, bool, fwzt);
					return flag;
				}
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return flag;
	}

	// 得到所有变更前户
	private List<BGQH> GetBGQH(String casenum) {
		List<BGQH> Hlist = new ArrayList<BGQH>();
		//扫描枪扫描 改为模糊查询 王帅2016/7/9
		List<GXJHXM> Gxjhxms = baseCommonDao.getDataList(GXJHXM.class, "casenum like '%" + casenum + "%'");
		String gxxmbh;
		// 该案卷号对应一个单元或多个单元
		if (Gxjhxms != null && Gxjhxms.size() > 0) {
			for (GXJHXM GXJHXM : Gxjhxms) {
				gxxmbh = GXJHXM.getGxxmbh();
				List<BGQH> Hs = baseCommonDao.getDataList(BGQH.class, "GXXMBH = '" + gxxmbh + "'");
				// 当前单元可能为一个或多个
				if (Hs != null && Hs.size() > 0) {
					for (BGQH H : Hs) {
						Hlist.add(H);
					}
				}
			}
		}
		return Hlist;
	}

	@Override
	public String AddDYBDCDY(String ywh, String xmbh) {
		String flag = "false";
		// 从房产库中获取权利人信息
		flag = ExtractSXFromZJK(ywh, xmbh, false);
		return flag;
	}

	// 权利人
	private List<BDCQLR> GetQLR(String casenum) throws SQLException {
		List<BDCQLR> Qlrs = new ArrayList<BDCQLR>();
		List<GXJHXM> Gxjhxms = baseCommonDao.getDataList(GXJHXM.class, "casenum like '%" + casenum + "%' and (GXLX !='99' or GXLX is null)");
		String gxxmbh;
		// 不重复添加权利人
		List<String> zjhList = new ArrayList<String>();
		// 该案卷号对应一个单元或多个单元
		if (Gxjhxms != null && Gxjhxms.size() > 0) {
			for (GXJHXM GXJHXM : Gxjhxms) {
				gxxmbh = GXJHXM.getGxxmbh();
				// and qlrlx ='1'
				List<Map> JYQlrs = baseCommonDao.getDataListByFullSql("select * from gxfck.qlr where GXXMBH ='" + gxxmbh + "'");
				if (JYQlrs != null && JYQlrs.size() > 0) {
					for (Map jyqlr : JYQlrs) {
						
						String zjhString = (String)jyqlr.get("ZJH");
						String qlrmcString =(String)jyqlr.get("QLRMC"); 
					
						if (StringHelper.isEmpty(zjhString)) {
							zjhString = "0";
							//(String)jyqlr.get("ZJH") = "0";
						}
						if (zjhList.contains(zjhString)) {
							continue;
						}
						
			      //权利人证件号有多个则按顿号分隔字符串  ws 7/13
						
						String[] zjh =zjhString.split("、");
						String[] qlrmc = qlrmcString.split("、");
						
						
					//	LinkedHashMap<String, String> map =  new LinkedHashMap<String, String>();
						 for(int i=0;i<zjh.length;i++){
							 //再按逗号分隔字符串
						     String[] zjh1 = zjh[i].split(",");
						     String[] qlrmc1 = qlrmc[i].split(","); 
						     for(int j=0;j<zjh1.length;j++){
						        BDCQLR bdcqlr = new BDCQLR();

								bdcqlr.setBDCQZH(jyqlr.get("BDCQZH") == null ? "" : jyqlr.get("BDCQZH").toString());
								bdcqlr.setSXH(jyqlr.get("SXH") == null ? "" : jyqlr.get("SXH").toString());	
								bdcqlr.setQLRMC(jyqlr.get("QLRMC") == null ? "" : qlrmc1[j]);	
								bdcqlr.setBDCQZH(jyqlr.get("BDCQZH") == null ? "" : jyqlr.get("BDCQZH").toString());
								bdcqlr.setZJZL(jyqlr.get("ZJZL") == null ? "" : jyqlr.get("ZJZL").toString());
								bdcqlr.setZJH(jyqlr.get("ZJH") == null ? "" : zjh1[j]);
								bdcqlr.setFZJG(jyqlr.get("FZJG") == null ? "" : jyqlr.get("FZJG").toString());
								bdcqlr.setSSHY(jyqlr.get("SSHY") == null ? "" : jyqlr.get("SSHY").toString());
								bdcqlr.setGJ(jyqlr.get("GJ") == null ? "" : jyqlr.get("GJ").toString());
								bdcqlr.setHJSZSS(jyqlr.get("HJSZSS") == null ? "" : jyqlr.get("HJSZSS").toString());
								bdcqlr.setXB(jyqlr.get("XB") == null ? "" : jyqlr.get("XB").toString());
								bdcqlr.setDH(jyqlr.get("DH") == null ? "" : jyqlr.get("DH").toString());
								bdcqlr.setDZ(jyqlr.get("DZ") == null ? "" : jyqlr.get("DZ").toString());
								bdcqlr.setYB(jyqlr.get("YB") == null ? "" : jyqlr.get("YB").toString());
								bdcqlr.setGZDW(jyqlr.get("GZDW") == null ? "" : jyqlr.get("GZDW").toString());
								bdcqlr.setDZYJ(jyqlr.get("DZYJ") == null ? "" : jyqlr.get("DZYJ").toString());
								bdcqlr.setQLRLX(jyqlr.get("QLRLX") == null ? "" : jyqlr.get("QLRLX").toString());
								bdcqlr.setQLBL(jyqlr.get("QLBL") == null ? "" : jyqlr.get("QLBL").toString());
								bdcqlr.setGYFS(jyqlr.get("GYFS") == null ? "" : jyqlr.get("GYFS").toString());
								bdcqlr.setGYQK(jyqlr.get("GYQK") == null ? "" : jyqlr.get("GYQK").toString());
								bdcqlr.setBZ(jyqlr.get("BZ") == null ? "" : jyqlr.get("BZ").toString());
								bdcqlr.setQLRID(jyqlr.get("QLRID") == null ? "" : jyqlr.get("QLRID").toString());
								bdcqlr.setQLID(jyqlr.get("QLID") == null ? "" : jyqlr.get("QLID").toString());
								Qlrs.add(bdcqlr);
							
                         /* for(String zjh1 : zjh){
                          	zjhList.add(zjh1);
						 }*/
						     }
						}
						    zjhList.add(zjhString);
					}
				}
			}
		}
		return Qlrs;
	}

	// 义务人
	private List<QLR> GetYWR(String casenum) throws SQLException {
		List<QLR> JYQlrs = new ArrayList<QLR>();
		List<GXJHXM> Gxjhxms = baseCommonDao.getDataList(GXJHXM.class, "casenum like '%" + casenum + "%'  and (GXLX !='99' or GXLX is null)");
		String gxxmbh;
		// 该案卷号对应一个单元或多个单元
		if (Gxjhxms != null && Gxjhxms.size() > 0) {
			for (GXJHXM GXJHXM : Gxjhxms) {
				gxxmbh = GXJHXM.getGxxmbh();
				JYQlrs = baseCommonDao.getDataList(QLR.class, "GXXMBH = '" + gxxmbh + "'");
				/*if(JYQlrs != null && JYQlrs.size()>0){
					for(QLR jyqlrs :JYQlrs){
						String[] qlrmc = jyqlrs.getQLRMC().split("、");
						String[] zjh = jyqlrs.getZJH().split("、");
					}
				}*/
			}
		}
		return JYQlrs;
	}

	// 单元
	private List<H> GetH(String casenum) throws SQLException {
		List<H> Hlist = new ArrayList<H>();
		List<GXJHXM> Gxjhxms = baseCommonDao.getDataList(GXJHXM.class, "casenum like '%" + casenum + "%' and (GXLX !='99' or GXLX is null)");
		String gxxmbh;
		// 该案卷号对应一个单元或多个单元
		if (Gxjhxms != null && Gxjhxms.size() > 0) {
			for (GXJHXM GXJHXM : Gxjhxms) {
				gxxmbh = GXJHXM.getGxxmbh();
				List<H> Hs = baseCommonDao.getDataList(H.class, "GXXMBH = '" + gxxmbh + "'");
				// 当前单元可能为一个或多个
				if (Hs != null && Hs.size() > 0) {
					for (H H : Hs) {
						Hlist.add(H);
					}
				}
			}
		}
		return Hlist;
	}

	// 南宁(几家公司已经处理，在GXJYK户表中存在bdcdyh)不动产单元号list
	private List<String> NNGetbdcdyhList(String casenum) throws SQLException {
		List<String> bdcdyhList = new ArrayList<String>();
		List<GXJHXM> Gxjhxms = baseCommonDao.getDataList(GXJHXM.class, "casenum like '%" + casenum + "%' and (GXLX !='99' or GXLX is null)");
		String gxxmbh;
		// 该案卷号对应一个单元或多个单元
		if (Gxjhxms != null && Gxjhxms.size() > 0) {
			for (GXJHXM GXJHXM : Gxjhxms) {
				gxxmbh = GXJHXM.getGxxmbh();
				List<JYH> Hs = baseCommonDao.getDataList(JYH.class, "GXXMBH = '" + gxxmbh + "'");
				// 当前单元可能为一个或多个
				if (Hs != null && Hs.size() > 0) {
					for (JYH H : Hs) {
						bdcdyhList.add(H.getBDCDYH());
						fwzt = H.getFWZT();
					}
				}
			}
		}
		return bdcdyhList;
	}

	// 通用（自贡）不动产单元号list
	private List<String> GetbdcdyhList(String casenum) throws SQLException {
		List<String> bdcdyhList = new ArrayList<String>();
		List<GXJHXM> Gxjhxms = baseCommonDao.getDataList(GXJHXM.class, "casenum like '%" + casenum + "%' and (GXLX !='99' or GXLX is null)");
		String gxxmbh;
		// 该案卷号对应一个单元或多个单元
		if (Gxjhxms != null && Gxjhxms.size() > 0) {
			for (GXJHXM GXJHXM : Gxjhxms) {
				gxxmbh = GXJHXM.getGxxmbh();
				List<JYH> Hs = baseCommonDao.getDataList(JYH.class, "GXXMBH = '" + gxxmbh + "'");
				// 当前单元可能为一个或多个
				if (Hs != null && Hs.size() > 0) {
					for (JYH H : Hs) {
						List<BDCS_H_XZ> HXZS = baseCommonDao.getDataList(BDCS_H_XZ.class, "RELATIONID = '" + H.getRELATIONID() + "'");
						if (HXZS.size() > 0 && HXZS != null) {
							bdcdyhList.add(HXZS.get(0).getBDCDYH());
						}
						fwzt = H.getFWZT();
					}
				}
			}
		}
		return bdcdyhList;
	}

	// 权利
	private Map<BDCS_QL_GZ, BDCS_FSQL_GZ> GetQLData(String casenum, String xmbh) throws SQLException {
		Map<BDCS_QL_GZ, BDCS_FSQL_GZ> rightMap = new HashMap<BDCS_QL_GZ, BDCS_FSQL_GZ>();
		/************************************************* 石家庄房产交易库抽取方法分界线 ***************************************************************/
		/************************************************* 共享交易库抽取分界线 ***************************************************************/
		FDCQ Fdcq = new FDCQ();
		DYAQ Dyaq = new DYAQ();
		CFDJ Cfdj = new CFDJ();
		YGDJ Ygdj = new YGDJ();
		// XZDJ Xzdj = new XZDJ();
		YYDJ Yydj = new YYDJ();		
		String sql = "casenum like '%" + casenum + "%'  and (GXLX !='99' or GXLX is null)";
		List<GXJHXM> Gxjhxms = baseCommonDao.getDataList(GXJHXM.class, sql);// JYGXJHXM
		String gxxmbh;
		if (Gxjhxms != null && Gxjhxms.size() > 0) {
			for (GXJHXM GXJHXM : Gxjhxms) {
				gxxmbh = GXJHXM.getGxxmbh();
				// 房地产权
				List<FDCQ> Fdcqs = baseCommonDao.getDataList(FDCQ.class, "GXXMBH = '" + gxxmbh + "'");
				if (Fdcqs != null && Fdcqs.size() > 0) {
					Fdcq = Fdcqs.get(0);
				}
				// 抵押权
				List<DYAQ> Dyaqs = baseCommonDao.getDataList(DYAQ.class, "GXXMBH = '" + gxxmbh + "'");
				if (Dyaqs != null && Dyaqs.size() > 0) {
					Dyaq = Dyaqs.get(0);
				}
				// 查封登记
				List<CFDJ> Cfdjs = baseCommonDao.getDataList(CFDJ.class, "GXXMBH = '" + gxxmbh + "'");
				if (Cfdjs != null && Cfdjs.size() > 0) {
					Cfdj = Cfdjs.get(0);
				}
				// 预告登记
				List<YGDJ> Ygdjs = baseCommonDao.getDataList(YGDJ.class, "GXXMBH = '" + gxxmbh + "'");
				if (Ygdjs != null && Ygdjs.size() > 0) {
					Ygdj = Ygdjs.get(0);
				}
				// 限制登记
				// List<JYXZDJ> Xzdjs = baseCommonDao.getDataList(JYXZDJ.class,
				// "GXXMBH = '" + gxxmbh + "'");
				// if (Xzdjs != null && Xzdjs.size() > 0) {
				// Xzdj = Xzdjs.get(0);
				// }
				// 异议登记
				List<YYDJ> Yydjs = baseCommonDao.getDataList(YYDJ.class, "GXXMBH = '" + gxxmbh + "'");
				if (Yydjs != null && Yydjs.size() > 0) {
					Yydj = Yydjs.get(0);
				}
				// String xzqdm = ConfigHelper.getNameByValue("XZQHDM");

				String xmbhcond = ProjectHelper.GetXMBHCondition(xmbh);
				List<BDCS_XMXX> xmxx = baseCommonDao.getDataList(BDCS_XMXX.class, xmbhcond);
				String PROJECT_ID = xmxx.get(0).getPROJECT_ID();
				HandlerMapping _mapping = HandlerFactory.getMapping();
				String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(PROJECT_ID);
				String _handleClassName = _mapping.getHandlerClassName(workflowcode);// "CSDJHandler";
//				String _handleClassName = null;
//				RegisterWorkFlow _flow = _mapping.getWorkflow(workflowcode);
//				if (_flow != null){
//					_handleClassName = _flow.getHandlername();
//				}				
				String qllx = xmxx.get(0).getQLLX();
				if (_handleClassName.contains("DYBGDJHandler")) {
					// 抵押变更登记
					rightMap = setDYAQ(Dyaq, casenum);
				} else if (_handleClassName.contains("BGDJHandler") || _handleClassName.contains("GZDJHandler")) {
					// 变更、更正
					rightMap = setFDCQ(Fdcq, casenum);

				} else if (_handleClassName.contains("BZDJHandler") || _handleClassName.contains("YSBZGGDJHandler")) {
					// 补证
					if (qllx.equals(QLLX.DIYQ.Value)) {
						rightMap = setDYAQ(Dyaq, casenum);
					} else {
						rightMap = setFDCQ(Fdcq, casenum);
					}
				} else if (_handleClassName.contains("HZDJHandler")) {
					// 换证、
					if (qllx.equals(QLLX.DIYQ.Value)) {
						rightMap = setDYAQ(Dyaq, casenum);
					} else {
						rightMap = setFDCQ(Fdcq, casenum);
					}
				} else if (_handleClassName.contains("YCFDJ_HouseHandler")) {
					// 在建工程查封（预测）
					rightMap = setCFDJ(Cfdj, casenum);
				} else if (_handleClassName.contains("CFDJ_HouseHandler") || _handleClassName.contains("CFDJ_XF_HouseHandler")) {
					// 查封
					rightMap = setCFDJ(Cfdj, casenum);
				} else if (_handleClassName.contains("CFDJ_ZX_HouseHandler")) {
					// 解封
					rightMap = setCFDJ(Cfdj, casenum);
				} else if (_handleClassName.contains("CSDJHandler")) {
					// 初始登记
					rightMap = setFDCQ(Fdcq, casenum);
				} else if (_handleClassName.contains("ZY_YDYTODY_DJHandler")) {
					// 转移+预抵押登记

				} else if (_handleClassName.contains("ZYYGDYYGDJHandler")) {
					// 转移预告+抵押预告登记
					rightMap = setYGDJ(Ygdj, casenum);
				} else if (_handleClassName.contains("DYYGZXDJHandler") || _handleClassName.contains("DYZXDJHandler")
						|| _handleClassName.contains("YDYZXDYDJHandler")) {
					// 抵押注销、抵押预告注销登记，预抵押注销
					rightMap = setDYAQ(Dyaq, casenum);
				} else if (_handleClassName.contains("YCDYDJHandler")) {
					// 在建工程抵押登记（预测）
					rightMap = setDYAQ(Dyaq, casenum);
				} else if (_handleClassName.contains("YCSCDYDJHandler")) {
					// 在建工程抵押转现房抵押
					rightMap = setDYAQ(Dyaq, casenum);
				} else if (_handleClassName.contains("YGYDYDJHandler")) {
					// 预告预抵押登记
					rightMap = setYGDJ(Ygdj, casenum);
				} else if (_handleClassName.contains("CSDYHandler") || _handleClassName.contains("DYDJHandler") || _handleClassName.contains("DYYGDJHandler")
						|| _handleClassName.contains("YDYDJHandler")) {
					// 抵押登记、预抵押
					rightMap = setDYAQ(Dyaq, casenum);
				}
				// else if (_handleClassName.contains("DYLimitedDJHandler")
				// || _handleClassName.contains("DYLimitLiftedDJHandler")
				// || _handleClassName.contains("XZDJHandler")) {
				// // 单元限制登记
				// rightMap = setXZDJ(Xzdj,casenum);
				// }
				else if (_handleClassName.contains("ZYYGZXDJHandler")) {
					// 转移预告注销
					rightMap = setYGDJ(Ygdj, casenum);
				} else if (_handleClassName.contains("YYZXDJHandler")) {
					// 异议注销登记
					rightMap = setYYDJ(Yydj, casenum);
				} else if (_handleClassName.contains("ZXDJHandler")) {
					// 所有权注销登记
					rightMap = setFDCQ(Fdcq, casenum);
				} else if (_handleClassName.contains("DYZYDJHandler")) {
					// 抵押转移登记
					rightMap = setDYAQ(Dyaq, casenum);
				} else if (_handleClassName.contains("ZYDJHandler")) {
					// 转移登记 
					rightMap = setFDCQ(Fdcq, casenum);
				} else if (_handleClassName.contains("YGDJHandler")) {
					// 预告登记
					rightMap = setYGDJ(Ygdj, casenum);
				} else if (_handleClassName.contains("YYDJHandler")) {
					// 异议登记
					rightMap = setYYDJ(Yydj, casenum);
				} else if (_handleClassName.contains("ZY_DY_DJHandler")) {
					// 转移+抵押登记

				}
			}
		}
		return rightMap;
	}

	// 设置房地产权
	private Map<BDCS_QL_GZ, BDCS_FSQL_GZ> setFDCQ(FDCQ fdcq, String casenum) {
		Map<BDCS_QL_GZ, BDCS_FSQL_GZ> rightMap = new HashMap<BDCS_QL_GZ, BDCS_FSQL_GZ>();
		BDCS_QL_GZ ql = new BDCS_QL_GZ();
		BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
		if (fdcq != null) {
			ql.setBDCDYH(fdcq.getBDCDYH());
			ql.setYWH(fdcq.getYWH());
			ql.setQLLX(fdcq.getQLLX());
			ql.setDJLX(fdcq.getDJLX());
			ql.setDJYY(fdcq.getDJYY());
			ql.setBDCQZH(fdcq.getBDCQZH());
			ql.setQXDM(fdcq.getQXDM());
			ql.setDJJG(fdcq.getDJJG());
			ql.setDBR(fdcq.getDBR());
			ql.setDJSJ(fdcq.getDJSJ());
			ql.setFJ(fdcq.getFJ());
			if (fdcq.getQSZT() != null) {
				ql.setQSZT(Integer.parseInt(fdcq.getQSZT()));
			}			
//			ql.setTDSHYQR(fdcq.getTDSHYQR());
			ql.setQDJG(fdcq.getFDCJYJG());
			ql.setCASENUM(casenum);
			fsql.setFDZL(fdcq.getFDZL());
//			fsql.setTDSYQR(fdcq.getTDSHYQR());
//			fsql.setDYTDMJ(fdcq.getDYTDMJ());
//			fsql.setFTTDMJ(fdcq.getFTTDMJ());
			fsql.setFDCJYJG(fdcq.getFDCJYJG());
			fsql.setGHYT(fdcq.getGHYT());
			fsql.setFWXZ(fdcq.getFWXZ());
			fsql.setFWJG(fdcq.getFWJG());
			if (fdcq.getSZC() != null) {
				fsql.setSZC(Integer.parseInt(fdcq.getSZC()));
			}			
			fsql.setZCS(fdcq.getZCS());
			fsql.setJZMJ(fdcq.getJZMJ());
			fsql.setZYJZMJ(fdcq.getZYJZMJ());
			fsql.setFTJZMJ(fdcq.getFTJZMJ());
			fsql.setJGSJ(fdcq.getJGSJ());
			fsql.setQLID(fdcq.getQLID());
			fsql.setCASENUM(casenum);
		}
		rightMap.put(ql, fsql);
		return rightMap;
	}

	// 设置抵押权
	private Map<BDCS_QL_GZ, BDCS_FSQL_GZ> setDYAQ(DYAQ dyaq, String casenum) {
		Map<BDCS_QL_GZ, BDCS_FSQL_GZ> rightMap = new HashMap<BDCS_QL_GZ, BDCS_FSQL_GZ>();
		BDCS_QL_GZ ql = new BDCS_QL_GZ();
		BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
		if (dyaq != null) {
			ql.setBDCDYH(dyaq.getBDCDYH());
			ql.setYWH(dyaq.getYWH());
			ql.setDJLX(dyaq.getDJLX());
			ql.setDJYY(dyaq.getDJYY());
			ql.setQLQSSJ(dyaq.getZWLXQSSJ());
			ql.setQLJSSJ(dyaq.getZWLXJSSJ());
			ql.setQXDM(dyaq.getQXDM());
			ql.setDJJG(dyaq.getDJJG());
			ql.setDBR(dyaq.getDBR());
			ql.setDJSJ(dyaq.getDJSJ());
			ql.setFJ(dyaq.getFJ());
			int qszt = 0;
			if (dyaq.getQSZT() != null) {
				qszt = Integer.parseInt(dyaq.getQSZT());
			}
			ql.setQSZT(qszt);
			ql.setLYQLID(dyaq.getLYQLID());
			// ql.setZSBH(dyaq.getZSBH());
			// ql.setBZ(dyaq.getBZ());
			fsql.setDYBDCLX(dyaq.getDYBDCLX());
			fsql.setDYR(dyaq.getDYR());
			fsql.setDYFS(dyaq.getDYFS());
			fsql.setZJJZWZL(dyaq.getZJJZWZL());
			fsql.setZJJZWDYFW(dyaq.getZJJZWDYFW());
			fsql.setBDBZZQSE(dyaq.getBDBZZQSE());
			fsql.setZGZQQDSS(dyaq.getZGZQQDSS());
			fsql.setZGZQSE(dyaq.getZGZQSE());
			fsql.setZXDYYWH(dyaq.getZXDYYWH());
			fsql.setZXDYYY(dyaq.getZXDYYY());
			fsql.setZXSJ(dyaq.getZXSJ());
			fsql.setQLID(dyaq.getQLID());
			// fsql.setDYMJ(dyaq.getDYMJ());
			ql.setCASENUM(casenum);
			fsql.setCASENUM(casenum);
			// //拼接所有义务人
			// fsql.setYWR(ywrs);
		}
		rightMap.put(ql, fsql);
		return rightMap;
	}

	// 设置预告权
	private Map<BDCS_QL_GZ, BDCS_FSQL_GZ> setYGDJ(YGDJ ygdj, String casenum) {
		Map<BDCS_QL_GZ, BDCS_FSQL_GZ> rightMap = new HashMap<BDCS_QL_GZ, BDCS_FSQL_GZ>();
		BDCS_QL_GZ ql = new BDCS_QL_GZ();
		BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
		if (ygdj != null) {
			ql.setBDCDYH(ygdj.getBdcdyh());
			ql.setYWH(ygdj.getYwh());
			ql.setDJLX(ygdj.getDjlx());
			ql.setDJYY(ygdj.getDjyy());
			ql.setDBR(ygdj.getDbr());
			ql.setDJSJ(ygdj.getDjsj());
			ql.setFJ(ygdj.getFj());
			int qszt = 0;
			if (ygdj.getQszt() != null) {
				qszt = Integer.parseInt(ygdj.getQszt());
			}
			ql.setQSZT(qszt);
			// ql.setZSBH(ygdj.getZcs());
			// ql.setBZ(ygdj.getbz);
			ql.setQDJG(ygdj.getQdjg());
			fsql.setBDCZL(ygdj.getBdczl());
			fsql.setYWR(ygdj.getYwr());
			fsql.setYWRZJZL(ygdj.getYwrzjzl());
			fsql.setYWRZJH(ygdj.getYwrzjh());
			fsql.setYGDJZL(ygdj.getYgdjzl());
			fsql.setTDSYQR(ygdj.getTdsyqr());
			fsql.setGHYT(ygdj.getGhyt());
			fsql.setFWXZ(ygdj.getFwxz());
			fsql.setFWJG(ygdj.getFwjg());
			fsql.setSZC(Integer.parseInt(ygdj.getSzc()));
			fsql.setZCS(ygdj.getZcs());
			fsql.setJZMJ(ygdj.getJzmj());
			// fsql.setBDBZZQSE(ygdj.getbdb);
			fsql.setQLID(ygdj.getQlid());
			ql.setCASENUM(casenum);
			fsql.setCASENUM(casenum);
			// 拼接所有义务人
			if (fsql.getYWR() == null || fsql.getYWR().equals("")) {
				fsql.setYWR(ywrs);
			}

		}
		rightMap.put(ql, fsql);
		return rightMap;
	}

	// 设置查封权
	private Map<BDCS_QL_GZ, BDCS_FSQL_GZ> setCFDJ(CFDJ cfdj, String casenum) {
		Map<BDCS_QL_GZ, BDCS_FSQL_GZ> rightMap = new HashMap<BDCS_QL_GZ, BDCS_FSQL_GZ>();
		BDCS_QL_GZ ql = new BDCS_QL_GZ();
		BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
		if (cfdj != null) {
			ql.setBDCDYH(cfdj.getBDCDYH());
			ql.setYWH(cfdj.getYWH());
			// ql.setQLQSSJ(cfdj.getQLQSSJ());
			// ql.setQLJSSJ(cfdj.getQLJSSJ());
			ql.setQXDM(cfdj.getQXDM());
			ql.setDJJG(cfdj.getDJJG());
			ql.setDBR(cfdj.getDBR());
			ql.setDJSJ(cfdj.getDJSJ());
			ql.setDBR(cfdj.getJFDBR());
			ql.setFJ(cfdj.getFJ());
			int qszt = 0;
			if (cfdj.getQSZT() != null) {
				qszt = Integer.parseInt(cfdj.getQSZT());
			}
			ql.setQSZT(qszt);
			ql.setLYQLID(cfdj.getLYQLID());
			// ql.setBZ(cfdj.getBZ());
			fsql.setCFJG(cfdj.getCFJG());
			fsql.setCFLX(cfdj.getCFLX());
			// fsql.setCFWJ(Cfdj.getCFWJ().toString());
			fsql.setCFWH(cfdj.getCFWH());
			fsql.setCFFW(cfdj.getCFFW());
			fsql.setJFJG(cfdj.getJFJG());
			// fsql.setJFWJ(Cfdj.getJFWJ().toString());
			fsql.setJFWH(cfdj.getJFWH());
			fsql.setQLID(cfdj.getQLID());
		}
		rightMap.put(ql, fsql);
		return rightMap;
	}

	// 设置异议
	private Map<BDCS_QL_GZ, BDCS_FSQL_GZ> setYYDJ(YYDJ yydj, String casenum) {
		Map<BDCS_QL_GZ, BDCS_FSQL_GZ> rightMap = new HashMap<BDCS_QL_GZ, BDCS_FSQL_GZ>();
		BDCS_QL_GZ ql = new BDCS_QL_GZ();
		BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
		if (yydj != null) {
			ql.setBDCDYH(yydj.getBDCDYH());
			ql.setYWH(yydj.getYWH());
			ql.setQXDM(yydj.getQXDM());
			ql.setDJJG(yydj.getDJJG());
			ql.setDBR(yydj.getDBR());
			ql.setDJSJ(yydj.getDJSJ());
			ql.setFJ(yydj.getFJ());
			int qszt = 0;
			if (yydj.getQSZT() != null) {
				qszt = Integer.parseInt(yydj.getQSZT());
			}
			ql.setQSZT(qszt);
			// ql.setBZ(yydj.getBZ());
			fsql.setYYSX(yydj.getYYSX());
			fsql.setZXDYYWH(yydj.getZXYYYWH());
			fsql.setZXYYYY(yydj.getZXYYYY());
			fsql.setQLID(yydj.getQLID());
			ql.setCASENUM(casenum);
			fsql.setCASENUM(casenum);
		}
		rightMap.put(ql, fsql);
		return rightMap;
	}

	// 设置限制登记
	private Map<BDCS_QL_GZ, BDCS_FSQL_GZ> setXZDJ(JYXZDJ Xzdj, String casenum) {
		Map<BDCS_QL_GZ, BDCS_FSQL_GZ> rightMap = new HashMap<BDCS_QL_GZ, BDCS_FSQL_GZ>();
		BDCS_QL_GZ ql = new BDCS_QL_GZ();
		BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
		if (Xzdj != null) {
			ql.setBDCDYH(Xzdj.getBDCDYH());
			ql.setYWH(Xzdj.getYWH());
			ql.setQXDM(Xzdj.getQXDM());
			ql.setDJJG(Xzdj.getDJJG());
			ql.setDBR(Xzdj.getDBR());
			ql.setDJSJ(Xzdj.getDJSJ());
			ql.setFJ(Xzdj.getFJ());
			int qszt = 0;
			if (Xzdj.getQSZT() != null) {
				qszt = Integer.parseInt(Xzdj.getQSZT());
			}
			ql.setQSZT(qszt);
			ql.setBZ(Xzdj.getBZ());
			fsql.setCFJG(Xzdj.getCFJG());
			fsql.setCFWJ(Xzdj.getCFWJ());
			fsql.setCFWH(Xzdj.getCFWH());
			fsql.setCFFW(Xzdj.getCFFW());
			fsql.setJFJG(Xzdj.getJFJG());
			fsql.setJFWJ(Xzdj.getJFWJ());
			fsql.setJFWH(Xzdj.getJFWH());
			fsql.setQLID(Xzdj.getQLID());
			ql.setCASENUM(casenum);
			fsql.setCASENUM(casenum);
		}
		rightMap.put(ql, fsql);
		return rightMap;
	}

	@Override
	public boolean ExtractFJFromZJK(String proinstId, String caseNum, String configFilePath) {
		boolean flag = false;
		Connection connection = null;
		try {
			connection = JH_DBHelper.getConnect_jy();
			// 从“图像表”取得当前合同号（案卷号）所有图片属性信息
			String imageScanSql = "select IMAGEID,FILEID from arcuser.img_imagescan where casenum = '" + caseNum + "' order by fileid, pageinnumber ";
			ResultSet imageScanResultSet = JH_DBHelper.excuteQuery(connection, imageScanSql);
			int i = 1;
			while (imageScanResultSet.next()) {
				// 根据“图像ID”进行循环
				String imageid = imageScanResultSet.getString("IMAGEID");
				String fileid = imageScanResultSet.getString("FILEID");
				String imageSql = "select IMAGEDATA from arcuser.img_imagedatajpg where IMAGEID='" + imageid + "'";
				ResultSet imageResultSet = JH_DBHelper.excuteQuery(connection, imageSql);
				String fileidSql = "select fileid from house.opr_filereceived where CASENUM='" + caseNum + "' and FILETYPE = '" + fileid + "'";
				ResultSet fileResultSet = JH_DBHelper.excuteQuery(connection, fileidSql);
				if (imageResultSet.next() && fileResultSet.next()) {
					Blob blob = imageResultSet.getBlob("IMAGEDATA");
					String fileName = fileResultSet.getString("FILEID");
					InputStream inStream = blob.getBinaryStream();
					byte[] buf = InputStreamToByte(inStream);
					insertDataService.InsertFJFromZJK(proinstId, fileName, configFilePath, buf, i);
					flag = true;
					i++;
				}
			}
		} catch (Exception ex) {
			flag = false;
		}
		try {
			connection.close();
		} catch (Exception e) {

		}
		return flag;
	}

	protected byte[] InputStreamToByte(InputStream is) throws Exception {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		int ch;
		while ((ch = is.read()) != -1) {
			byteStream.write(ch);
		}
		byte imgData[] = byteStream.toByteArray();
		byteStream.close();
		return imgData;
	}

	/**
	 * 李堃 从共享交易库中抽取附件
	 * 
	 * @param proinstId流程实例ID
	 * @param 房产业务号
	 */
	public boolean ExtractFJFromZJK(String proinstId, String caseNum) {
		boolean flag = false;
		try {
			// 按顺序查出共享交易库中所有收件种类
			List<PROMATER> gxjy_promaters = baseCommonDao.getDataList(PROMATER.class, "PROINST_ID = '" + caseNum + "' order by MATERIAL_INDEX");
			if (gxjy_promaters != null && gxjy_promaters.size() > 0) {
				Properties props = new Properties();
				// 协同站点基准URL
				String shareURL = ConfigHelper.getNameByValue("URL_SHARE");
				int m = 1;
				// 循环每一个中间库中的收件种类
				for (int i = 0; i < gxjy_promaters.size(); i++) {
					String condt = "PROINST_ID='" + proinstId + "' and MATERIAL_NAME='" + gxjy_promaters.get(i).getMATERIAL_NAME() + "'";
					List<Wfi_ProMater> wfi_promaters = baseCommonDao.getDataList(Wfi_ProMater.class, condt);
					if (wfi_promaters != null && wfi_promaters.size() > 0) {
						// 获取workflow库中对应附件类型的ID
						String materilinstID = wfi_promaters.get(0).getMaterilinst_Id();
						// 插入附件前，先删除该模板下的原有附件
						baseCommonDao.deleteEntitysByHql(Wfi_MaterData.class, "MATERILINST_ID='" + materilinstID + "'");

						// 获取中间库每个种类下的所有文件
						List<MATERDATA> gxjy_materdatas = baseCommonDao.getDataList(MATERDATA.class, "MATERILINST_ID = '"
								+ gxjy_promaters.get(i).getMATERILINST_ID() + "'");
						if (gxjy_materdatas != null && gxjy_materdatas.size() > 0) {
							for (int k = 0; k < gxjy_materdatas.size(); k++) {
								String imgURL = "";
								String path = gxjy_materdatas.get(k).getPATH();
								String relativeURL = gxjy_materdatas.get(k).getRELATIVEURL();
								String fileName = gxjy_materdatas.get(k).getFILE_PATH();
								if (shareURL != null && !shareURL.equals("") && relativeURL != null && !relativeURL.equals("")) {
									// 从站点外通过封装的服务取图片
									imgURL = shareURL + relativeURL + "/1";
								} else {
									// 从站点下相对路径取图片
									imgURL = shareURL + path + "\\" + fileName;
								}

								// 插入到workflow库
								insertDataService.InsertFJFromZJKEx(proinstId, fileName, materilinstID, imgURL, m,null);
								m++;
							}
						}
					}
				}
			}
		} catch (Exception ex) {
		}

		return flag;
	}

	/*
	 * 根据图片的URL获取图片字节数组
	 */
	public byte[] getImgByte(String srcimgUrl) { // ,String destfileURL
		byte[] buf = null;
		try {
			URL url = new URL(srcimgUrl);
			InputStream stream = url.openStream();
			// 创建流
			BufferedInputStream in = new BufferedInputStream(stream);
			// 生成图片名
			int index = srcimgUrl.lastIndexOf("/");
			// String sName = srcimgUrl.substring(index+1, srcimgUrl.length());
			// // 存放地址
			File img = new File("c:\\aa.jpg");
			// // 生成图片
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(img));
			buf = new byte[2048];
			// int len=readInt(in);
			// buf = new byte[len];
			int length = in.read(buf);
			while (length != -1) {
				out.write(buf, 0, length);
				length = in.read(buf);
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buf;
	}

	/**
	 * 通过项目编号获取proinstid
	 * 
	 * @作者 李堃
	 * @创建时间 2016年4月16日下午12:35:58
	 * @param projectid
	 * @return
	 */
	public String getProinstID(String projectid) {
		String sql = "select PROINST_ID from bdc_workflow.wfi_proinst where FILE_NUMBER='" + projectid + "'";
		Connection connection = null;
		String proinstid = null;
		try {
			connection = DA_DBHelper.getConnect_da();
			ResultSet resultSet = DA_DBHelper.excuteQuery(connection, sql);
			if (resultSet.next()) {
				proinstid = resultSet.getString("PROINST_ID");
				// System.out.println("查到数据...");
			}
		} catch (Exception ex) {

		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return proinstid;
	}

	public int readInt(InputStream in) throws IOException {
		int ch1 = in.read();
		int ch2 = in.read();
		int ch3 = in.read();
		int ch4 = in.read();
		if ((ch1 | ch2 | ch3 | ch4) < 0)
			throw new EOFException();
		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}

	// 获取在办数据的RealtionID list
	private List<String> GetRealtionIDList(String casenum) throws SQLException {
		List<String> bdcdyhList = new ArrayList<String>();
		List<GXJHXM> Gxjhxms = baseCommonDao.getDataList(GXJHXM.class, "casenum like '%" + casenum + "%' and (GXLX !='99' or GXLX is null)");
		String gxxmbh;
		// 该案卷号对应一个单元或多个单元
		if (Gxjhxms != null && Gxjhxms.size() > 0) {
			for (GXJHXM GXJHXM : Gxjhxms) {
				
				gxxmbh = GXJHXM.getGxxmbh();
				List<R_FW_QL> r_fw_ql  = baseCommonDao.getDataList(R_FW_QL.class, "GXXMBH = '" + gxxmbh + "'");
				String houseid;
				if(r_fw_ql != null && r_fw_ql.size()>0){
					for(R_FW_QL rfwql : r_fw_ql ){
						houseid = rfwql.getHouseid();
				List<H> Hs = baseCommonDao.getDataList(H.class, "relationid = '" + houseid + "'");
				// 当前单元可能为一个或多个
				if (!Hs.isEmpty() && Hs.size() > 0) {
					for (H H : Hs) {
						bdcdyhList.add(H.getRelationid());
						fwzt = H.getFwzt();
						if (fwzt == null || fwzt.isEmpty()) {
							List<String> nofwzt = new ArrayList<String>();
							nofwzt.add("nofwzt");
							return nofwzt;
						}
						// fwzt="2";
					}
				}
			 }
			}
		 }
		}
		return bdcdyhList;

}

	// 将所有义务人连接起来
	private void getAllYWR(List<QLR> jYQlrList) {
		if (jYQlrList != null && jYQlrList.size() > 0) {
			for (int i = 0; i < jYQlrList.size(); i++) {
				ywrs += jYQlrList.get(i).getQLRMC() + ",";
			}
			ywrs = ywrs.substring(0, ywrs.length() - 1);
		}
	}

	// 变更业务GXJYK抽取权利、权利人、户信息存入不动产库
	public String InsertSXFromZJKBG(BDCS_QL_GZ ql, BDCS_FSQL_GZ fsql, String xmbh, String casenum, List<BDCQLR> qlrList, List<QLR> jYQlrList,
			List<BGQH> bgqhList, List<H> hList, boolean bool, String fwzt) throws Exception {
		String flag = "false";
		String xmbhcond = ProjectHelper.GetXMBHCondition(xmbh);
		List<BDCS_XMXX> xmxx = baseCommonDao.getDataList(BDCS_XMXX.class, xmbhcond);
		if (xmxx.size() > 0) {
			String qllx = xmxx.get(0).getQLLX();
			String djlx = xmxx.get(0).getDJLX();
			// 增加变更、更正、初始登记业务时，调用对应Handler增加单元的方法更新【石家庄判断单元信息为空不更新】
			// 业务类型判断
			String PROJECT_ID = xmxx.get(0).getPROJECT_ID();
			HandlerMapping _mapping = HandlerFactory.getMapping();
			String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(PROJECT_ID);
			String _handleClassName = _mapping.getHandlerClassName(workflowcode);// "CSDJHandler";
//			String _handleClassName = null;
//			RegisterWorkFlow _flow = _mapping.getWorkflow(workflowcode);
//			if (_flow != null){
//				_handleClassName = _flow.getHandlername();
//			}	
			if (bool) {
				String bdcdyidArr = "";
				// 检查是否落宗
				for (BGQH bgqh : bgqhList) {
					String bdcdyh = bgqh.getRelationid();
					String bdcdyid1 = "";
					String hql = " relationid = '" + bdcdyh + "' ";
					if (fwzt.equals("2")) {
						List<BDCS_H_XZ> h_xzs = baseCommonDao.getDataList(BDCS_H_XZ.class, hql);
						if (h_xzs.size() < 1) {
							flag = "warning" + bdcdyh;
							return flag;
						}
						bdcdyid1 = h_xzs.get(0).getId();
					} else if (fwzt.equals("1")) {
						List<BDCS_H_XZY> h_xzs = baseCommonDao.getDataList(BDCS_H_XZY.class, hql);
						if (h_xzs.size() < 1) {
							flag = "warning" + bdcdyh;
							return flag;
						}
						bdcdyid1 = h_xzs.get(0).getId();
					}

					BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
					RealUnit _srcUnit = UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, bdcdyid1);
					if (_srcUnit == null) {
						continue;
					}
					String bdcdyid = _srcUnit.getId();
					if (bdcdyidArr.equals("")) {
						bdcdyidArr += bdcdyid;
					} else {
						bdcdyidArr += "," + bdcdyid;
					}
				}
				// 增加审核环节，检查是否有抵押、查封等
				ResultMessage rltMessage = dyservice.checkAcceptable(xmbh, bdcdyidArr);
				if (rltMessage.getSuccess().equals("false")) {
					return rltMessage.getMsg();
				} else if (rltMessage.getSuccess().equals("warning")) {
					return "警告：" + rltMessage.getMsg();
				}
			}
			// 将权利人保存到申请人表中
			Object[] sqrids = SaveBDCS_SQR(qlrList, xmbh);
			for (BGQH bgqh : bgqhList) {
				if (_handleClassName.contains("DYBGDJHandler") || _handleClassName.contains("BGDJHandler") || _handleClassName.contains("GZDJHandler")) {
					/************** 此处为除变更、更正、初始登记业务的单元添加方法 **************/
					String bdcdyid = "", bdcdyid2 = "";
					for (H H : hList) {
						// 先从共享交易库中查出所有的relationID，然后去调查库关联查出所有的bdcdyid
						List<DCS_H_GZ> dcs_H_GZs = baseCommonDao.getDataList(DCS_H_GZ.class, "relationid='" + H.getRelationid() + "'");
						bdcdyid += dcs_H_GZs.get(0).getId() + ",";
						bdcdyid2 += "'" + dcs_H_GZs.get(0).getId() + "',";
					}
					bdcdyid = bdcdyid.substring(0, bdcdyid.length() - 1);
					bdcdyid2 = bdcdyid2.substring(0, bdcdyid2.length() - 1);
					if (_handleClassName.contains("DYBGDJHandler")) {
						DYBGDJHandler.addBDCDY(bdcdyid);
					} else if (_handleClassName.contains("BGDJHandler")) {
						dyservice.addBDCDYNoCheck(xmbh, bdcdyid);
						// BGDJHandler.addBDCDY(bdcdyid);
					} else if (_handleClassName.contains("GZDJHandler")) {
						GZDJHandler.addBDCDY(bdcdyid);
					} else {
					}

					/********************************************************************/
					DJHandler handler = HandlerFactory.createDJHandler(xmbh);
					String str = MessageFormat.format("XMBH=''{0}'' AND BDCDYID in ({1}) )", xmbh, bdcdyid2);
					List<BDCS_DJDY_GZ> listdjdy = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, str);
					Rights _rights = RightsTools.loadRightsByDJDYID(DJDYLY.GZ, xmbh, listdjdy.get(0).getDJDYID());
					if (_rights == null) {
						continue;
					}
					handler.addQLRbySQRArray(_rights.getId(), sqrids);
					SubRights _subRights = RightsTools.loadSubRightsByRightsID(DJDYLY.GZ, _rights.getId());
					if (_subRights == null) {
						continue;
					}
					List<GXJHXM> Gxjhxms = baseCommonDao.getDataList(GXJHXM.class, "casenum like '%" + casenum + "%'");
					String gxxmbh = null;
					if(Gxjhxms !=null && Gxjhxms.size()>0){
					 for(GXJHXM gxjhxm : Gxjhxms){
						gxxmbh = gxjhxm.getGxxmbh();
					 }
					 
					}
					SaveQL(ql, fsql, gxxmbh, _rights, _subRights);
					SaveJYYWRDLR(xmbh, jYQlrList);
					flag = "true";
				}
			}

		}
		return flag;
	}

	// 统一GXJYK抽取权利、权利人、户信息存入不动产库
	public String InsertSXFromZJK(BDCS_QL_GZ ql, BDCS_FSQL_GZ fsql, String xmbh, String casenum, List<BDCQLR> qlrList, List<QLR> jYQlrList,
			List<String> bdcdyhList, List<H> hList, boolean bool, String fwzt) throws Exception {
		String flag = "false";
		String xmbhcond = ProjectHelper.GetXMBHCondition(xmbh);
		List<BDCS_XMXX> xmxx = baseCommonDao.getDataList(BDCS_XMXX.class, xmbhcond);
		// 业务类型判断
		String PROJECT_ID = xmxx.get(0).getPROJECT_ID();
		HandlerMapping _mapping = HandlerFactory.getMapping();
		String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(PROJECT_ID);
		String _handleClassName = _mapping.getHandlerClassName(workflowcode);// "CSDJHandler";
//		String _handleClassName = null;
//		RegisterWorkFlow _flow = _mapping.getWorkflow(workflowcode);
//		if (_flow != null){
//			_handleClassName = _flow.getHandlername();
//		}	
		if (xmxx.size() > 0) {
			String qllx = xmxx.get(0).getQLLX();
			String djlx = xmxx.get(0).getDJLX();
			// 注销类业务、异议登记处理
			// if
			// ((djlx.equals(DJLX.ZXDJ.Value)&&qllx.equals(QLLX.DIYQ.Value))||djlx.equals(DJLX.YYDJ.Value)&&
			// ql != null) {
			if (_handleClassName.contains("DYYGZXDJHandler") || _handleClassName.contains("DYZXDJHandler") || _handleClassName.contains("DYZYDJHandler")
					|| _handleClassName.contains("YYDJHandler") || _handleClassName.contains("YYZXDJHandler")
					|| _handleClassName.contains("ZYYG_YDY_DJHandler") || _handleClassName.contains("ZYYGDJHandler")
					|| _handleClassName.contains("ZYYGDYYGDJHandler") || _handleClassName.contains("ZYYGZXDJHandler")
					|| _handleClassName.contains("DYBGDJHandler")) {
				flag = InsertDYZX(ql, xmbh, casenum);
				// SaveBDCS_SQR()
			} else if (_handleClassName.contains("CFDJ_ZX_HouseHandler")) {// djlx.equals(DJLX.CFDJ.Value)&&qllx.equals("98")
				// 解封
				flag = InsertJF(fsql, xmbh, casenum, hList);
			} else if (_handleClassName.contains("CSDJHandler")) {
				// else
				// if(djlx.equals(DJLX.CSDJ.Value)&&(qllx.equals("1")||qllx.equals("2")||qllx.equals("3")||qllx.equals("4")||
				// qllx.equals("5")||qllx.equals("6")||qllx.equals("7")||qllx.equals("8")))
				// {
				// 初始登记
				flag = InsertCSDJ(xmbh, casenum);
				// 保存申请人到登记库
				Object[] sqrids = SaveBDCS_SQR(qlrList, xmbh);
			} else {
				// 增加变更、更正、初始登记业务时，调用对应Handler增加单元的方法更新【石家庄判断单元信息为空不更新】

				if (bool) {
					String bdcdyidArr = "";
					for (String bdcdyh : bdcdyhList) {
						String bdcdyid1 = "";
						String hql = " relationid = '" + bdcdyh + "' ";
						if (fwzt.equals("2")) {
							List<BDCS_H_XZ> h_xzs = baseCommonDao.getDataList(BDCS_H_XZ.class, hql);
							if (h_xzs.size() < 1) {
								flag = "warning" + bdcdyh;
								return flag;
							}
							bdcdyid1 = h_xzs.get(0).getId();
						} else if (fwzt.equals("1")) {
							List<BDCS_H_XZY> h_xzs = baseCommonDao.getDataList(BDCS_H_XZY.class, hql);
							if (h_xzs.size() < 1) {
								flag = "warning" + bdcdyh;
								return flag;
							}
							bdcdyid1 = h_xzs.get(0).getId();
						}

						BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
						RealUnit _srcUnit = UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, bdcdyid1);
						if (_srcUnit == null) {
							continue;
						}
						String bdcdyid = _srcUnit.getId();
						if (bdcdyidArr.equals("")) {
							bdcdyidArr += bdcdyid;
						} else {
							bdcdyidArr += "," + bdcdyid;
						}
					}
					// 增加审核环节
					ResultMessage rltMessage = dyservice.checkAcceptable(xmbh, bdcdyidArr);
					if (rltMessage.getSuccess().equals("false")) {
						return rltMessage.getMsg();
					} else if (rltMessage.getSuccess().equals("warning")) {
						return "警告：" + rltMessage.getMsg();
					}
				}
				// 将权利人保存到申请人表中
				Object[] sqrids = SaveBDCS_SQR(qlrList, xmbh);
				for (String bdcdyh : bdcdyhList) {
					if (_handleClassName.contains("DYBGDJHandler") || _handleClassName.contains("BGDJHandler") || _handleClassName.contains("GZDJHandler")
							|| _handleClassName.contains("CSDJHandler")) {
						/************** 此处为除变更、更正、初始登记业务的单元添加方法 **************/
						// String bdcdyid="";
						// for(JYH H:HList){
						// if (_handleClassName.contains("DYBGDJHandler")){
						// DYBGDJHandler.addBDCDY(H.getBDCDYID());
						// }else if(_handleClassName.contains("BGDJHandler")){
						// BGDJHandler.addBDCDY(H.getBDCDYID());
						// }else if(_handleClassName.contains("GZDJHandler")){
						// GZDJHandler.addBDCDY(H.getBDCDYID());
						// }else if(_handleClassName.contains("CSDJHandler")){
						// CSDJHandler.addBDCDY(H.getBDCDYID());
						// }else{}
						// }
						// /********************************************************************/
						// DJHandler handler = HandlerFactory
						// .createDJHandler(xmbh);
						// String str = MessageFormat.format(
						// "XMBH=''{0}'' AND BDCDYID=''{1}''", xmbh,
						// bdcdyid);
						// List<BDCS_DJDY_GZ> listdjdy = baseCommonDao
						// .getDataList(BDCS_DJDY_GZ.class, str);
						// Rights _rights = RightsTools.loadRightsByDJDYID(
						// DJDYLY.GZ, xmbh, listdjdy.get(0).getDJDYID());
						// if (_rights == null) {
						// continue;
						// }
						// handler.addQLRbySQRArray(_rights.getId(), sqrids);
						// SubRights _subRights = RightsTools
						// .loadSubRightsByRightsID(DJDYLY.GZ,
						// _rights.getId());
						// if (_subRights == null) {
						// continue;
						// }
						// SaveQL(ql, fsql, casenum, _rights, _subRights);
						// SaveJYYWRDLR(xmbh, jyqlrList);
						// flag = "true";
					} else {
						String hql = " relationid = '" + bdcdyh + "' ";
						String bdcdyid1 = "";
						// List<BDCS_H_XZ> h_xzs = baseCommonDao.getDataList(
						// BDCS_H_XZ.class, hql);
						// if (h_xzs.size() < 1) {
						// flag = "warning";
						// return flag;
						// }
						// String bdcdyid1 = h_xzs.get(0).getId();
						if (fwzt.equals("2")) {
							List<BDCS_H_XZ> h_xzs = baseCommonDao.getDataList(BDCS_H_XZ.class, hql);
							if (h_xzs.size() < 1) {
								flag = "warning";
								return flag;
							}
							bdcdyid1 = h_xzs.get(0).getId();
						} else if (fwzt.equals("1")) {
							List<BDCS_H_XZY> h_xzs = baseCommonDao.getDataList(BDCS_H_XZY.class, hql);
							if (h_xzs.size() < 1) {
								flag = "warning";
								return flag;
							}
							bdcdyid1 = h_xzs.get(0).getId();
						}
						BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
						RealUnit _srcUnit = UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, bdcdyid1);
						if (_srcUnit == null) {
							continue;
						}
						String bdcdyid = _srcUnit.getId();
						dyservice.removeBDCDY(xmbh, bdcdyid);
						dyservice.addBDCDYNoCheck(xmbh, bdcdyid);
						DJHandler handler = HandlerFactory.createDJHandler(xmbh);
						String str = MessageFormat.format("XMBH=''{0}'' AND BDCDYID=''{1}''", xmbh, bdcdyid);
						List<BDCS_DJDY_GZ> listdjdy = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, str);
						boolean bLoadQLR = true;// 是否加载权利人，换补证addBDCDYNoCheck时自动加载权利人了，不能重复添加
						// 如果用bdcdyid加载不到，要用qlid
						if (listdjdy == null || listdjdy.size() == 0) {
							List<BDCS_DJDY_XZ> djdy_XZs = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "BDCDYH='" + _srcUnit.getBDCDYH() + "'");
							if (djdy_XZs != null && djdy_XZs.size() > 0) {
								List<BDCS_QL_XZ> ql_XZs = baseCommonDao.getDataList(BDCS_QL_XZ.class, "BDCQZH='" + ql.getBDCQZH() + "'");
								if (ql_XZs != null && ql_XZs.size() > 0) {
									dyservice.addBDCDYNoCheck(xmbh, ql_XZs.get(0).getId());
									listdjdy = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, str);
									bLoadQLR = false;
								}

							}
						}

						Rights _rights = RightsTools.loadRightsByDJDYID(DJDYLY.GZ, xmbh, listdjdy.get(0).getDJDYID());
						if (_rights == null) {
							continue;
						}
						if (bLoadQLR) {
							// 加载权利人
							handler.addQLRbySQRArray(_rights.getId(), sqrids);
						}

						SubRights _subRights = RightsTools.loadSubRightsByRightsID(DJDYLY.GZ, _rights.getId());
						if (_subRights == null) {
							continue;
						}
						//ws 8/2 update
						List<GXJHXM> Gxjhxms = baseCommonDao.getDataList(GXJHXM.class, "casenum like '%" + casenum + "%'");
						String gxxmbh = null;
						if(Gxjhxms !=null && Gxjhxms.size()>0){
						 for(GXJHXM gxjhxm : Gxjhxms){
							gxxmbh = gxjhxm.getGxxmbh();
						 }
						 
						}
						SaveQL(ql, fsql, gxxmbh, _rights, _subRights);
						// 保存义务人到申请人表
						//SaveJYYWRDLR(xmbh, jYQlrList);
						flag = "true";
					}
				}
			}
		}
		return flag;
	}

	private Object[] SaveBDCS_SQR(List<BDCQLR> qlrs, String xmbh) {
		List<String> sqrids = new ArrayList<String>();
		String xmbhcond = ProjectHelper.GetXMBHCondition(xmbh);
		baseCommonDao.deleteEntitysByHql(BDCS_SQR.class, xmbhcond);
		baseCommonDao.flush();
		for (BDCQLR _qlr : qlrs) {
			BDCS_SQR sqr = new BDCS_SQR();
			sqr.setSQRXM(_qlr.getQLRMC());
			sqr.setTXDZ(_qlr.getDZ());
			sqr.setFDDBR(_qlr.getFDDBR());
			sqr.setXB(_qlr.getXB());
			sqr.setSQRLB("1");
			sqr.setSQRLX(_qlr.getQLRLX());
			sqr.setISCZR(_qlr.getISCZR());
			sqr.setZJH(_qlr.getZJH());// 证件号码
			sqr.setZJLX(_qlr.getZJZL());
			sqr.setGYFS(_qlr.getGYFS());
			sqr.setLXDH(_qlr.getDH());
			sqr.setXMBH(xmbh);
			sqr.setDLRXM(_qlr.getDLRXM());
			sqr.setDLRLXDH(_qlr.getDLRLXDH());
			sqr.setDLRZJHM(_qlr.getDLRZJHM());
			sqr.setDLRZJLX(_qlr.getDLRZJLX());
			baseCommonDao.save(sqr);
			baseCommonDao.flush();
			sqrids.add(sqr.getId());
		}
		return sqrids.toArray();
	}

	private void SaveQL(BDCS_QL_GZ ql, BDCS_FSQL_GZ fsql, String gxxmbh, Rights _rights, SubRights _subRights) {
		_rights.setCASENUM(gxxmbh);
		_subRights.setCASENUM(gxxmbh);
		if (ql != null) {
			_rights.setQLQSSJ(ql.getQLQSSJ());
			_rights.setQLJSSJ(ql.getQLJSSJ());
			_rights.setQDJG(ql.getQDJG());
			_rights.setFJ(ql.getFJ());
		}
		if (fsql != null) {
			_subRights.setDYR(fsql.getDYR());
			_subRights.setZJJZWDYFW(fsql.getZJJZWDYFW());
			_subRights.setDYFS(fsql.getDYFS());
			_subRights.setZXSJ(fsql.getZXSJ());
			_subRights.setFDCJYJG(fsql.getFDCJYJG());
			_subRights.setCFWH(fsql.getCFWH());
			_subRights.setCFJG(fsql.getCFJG());
			if (fsql.getZGZQSE() != null && fsql.getZGZQSE() > 0) {
				_subRights.setZGZQSE(fsql.getZGZQSE());
			}
			if (fsql.getBDBZZQSE() != null && fsql.getBDBZZQSE() > 0) {
				_subRights.setBDBZZQSE(fsql.getBDBZZQSE());
			}
		}
		baseCommonDao.save(_rights);
		baseCommonDao.save(_subRights);
		baseCommonDao.flush();
	}

	// 交易统一存义务人
	private void SaveJYYWRDLR(String xmbh, List<QLR> jYQlrList) {
		if (jYQlrList != null && jYQlrList.size() > 0) {
			for (QLR jyqlr : jYQlrList) {
				String sqrHql = MessageFormat.format("XMBH=''{0}'' AND ZJH=''{1}''", xmbh, jyqlr.getZJH());
				List<BDCS_SQR> sqr = baseCommonDao.getDataList(BDCS_SQR.class, sqrHql);
				// 登记库如果查到了，用中间库更新登记，如果没有，新增进登记库
				if (sqr != null && sqr.size() > 0) {
					sqr.get(0).setDLRXM(jyqlr.getQLRMC());
					sqr.get(0).setDLRLXDH(jyqlr.getDH());
					sqr.get(0).setDLRZJHM(jyqlr.getZJH());
					sqr.get(0).setDLRZJLX(jyqlr.getZJZL());
					baseCommonDao.update(sqr.get(0));
					baseCommonDao.flush();
				} else {
					BDCS_SQR sqr2 = new BDCS_SQR();
					sqr2.setSQRLB("2");
					sqr2.setXMBH(xmbh);
					sqr2.setSQRXM(jyqlr.getQLRMC());
					sqr2.setZJH(jyqlr.getZJH());
					sqr2.setXB(jyqlr.getXB());
					sqr2.setZJLX(jyqlr.getZJZL());
					sqr2.setGJDQ(jyqlr.getGJ());
					sqr2.setHJSZSS(jyqlr.getHJSZSS());
					sqr2.setGZDW(jyqlr.getGZDW());
					sqr2.setLXDH(jyqlr.getDH());
					sqr2.setTXDZ(jyqlr.getDZ());
					sqr2.setYZBM(jyqlr.getYB());
					sqr2.setDZYJ(jyqlr.getDZYJ());
					sqr2.setSQRLX(jyqlr.getQLRLX());
					sqr2.setQLBL(jyqlr.getQLBL());
					sqr2.setGYFS(jyqlr.getGYFS());
					baseCommonDao.save(sqr2);
					baseCommonDao.flush();
				}
			}
		}
	}

	// 抵押注销
	private String InsertDYZX(BDCS_QL_GZ ql, String xmbh, String casenum) {
		String flag = "false";
		// 获取行政区名称和简称
		String xzqhdm = ConfigHelper.getNameByValue("XZQHDM");
		String xzqmc = ConfigHelper.getNameByValue("XZQHMC");
		String xzqjc = getXZQJC(xzqhdm);
		String qzhFull = ql.getBDCQZH();
		if (qzhFull != null) {
			String[] qzhs = qzhFull.split(",");
			if (qzhs != null && qzhs.length > 0) {
				String condition = "1=1 ";
				for (int i = 0; i < qzhs.length; i++) {
					condition += "and BDCQZH like '%" + qzhs[i] + "%' ";
				}
				List<Rights> _rightsList = RightsTools.loadRightsByCondition(DJDYLY.XZ, condition);
				if (_rightsList == null || _rightsList.size() < 1) {
					condition = " BDCQZH =" + xzqjc + "+ '(" + ql.getBDCQZH().substring(0, 4) + ")" + xzqmc + "不动产证明第" + ql.getBDCQZH().substring(4) + "号'";
					_rightsList = RightsTools.loadRightsByCondition(DJDYLY.XZ, condition);
				}

				if (_rightsList != null && _rightsList.size() > 0) {
					for (Rights rights : _rightsList) {
						String qlid = rights.getId();
						DJHandler handler = HandlerFactory.createDJHandler(xmbh);
						handler.addBDCDY(qlid);
						baseCommonDao.flush();
						List<Rights> list = RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='" + xmbh + "'");
						if (list != null && list.size() > 0) {
							for (Rights tmepQl : list) {
								tmepQl.setCASENUM(casenum);
								baseCommonDao.update(tmepQl);
							}
						}
						flag = "true";
					}
				}
			}
		}
		return flag;
	}

	// 解封s
	private String InsertJF(BDCS_FSQL_GZ fsql, String xmbh, String casenum, List<H> hList) {
		String flag = "false";
		// 获取行政区名称和简称
		String xzqhdm = ConfigHelper.getNameByValue("XZQHDM");
		String xzqmc = ConfigHelper.getNameByValue("XZQHMC");
		String xzqjc = getXZQJC(xzqhdm);
		String condition = " CFWH = '" + fsql.getCFWH() + "'";
		List<SubRights> subRights = RightsTools.loadSubRightsByCondition(DJDYLY.XZ, condition);

		if (subRights != null && subRights.size() > 0) {
			for (SubRights subright : subRights) {
				Rights rights = RightsTools.loadRights(DJDYLY.XZ, subright.getQLID());
				DJHandler handler = HandlerFactory.createDJHandler(xmbh);
				handler.addBDCDY(rights.getId());
				baseCommonDao.flush();
				List<Rights> list = RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='" + xmbh + "'");
				if (list != null && list.size() > 0) {
					for (Rights tmepQl : list) {
						tmepQl.setCASENUM(casenum);
						baseCommonDao.update(tmepQl);
					}
				}
				flag = "true";
			}

			// 如果解封的户数和查封户数不一致，去掉多余的
			if (hList.size() != subRights.size()) {
				String relationids = "", bdcdyhs = "", djdyids = "";
				String fwzt = "2";
				for (H jyh : hList) {
					relationids += "'" + jyh.getRelationid() + "',";
					fwzt = jyh.getFwzt();
				}
				// 查出要解封的户
				String sql = "relationid  in (" + relationids.substring(0, relationids.length() - 1) + ") and xmbh='" + xmbh + "'";
				if (fwzt.equals("1")) {
					// 预测户
					// List<BDCS_H_GZY>
					// h_GZs=baseCommonDao.getDataList(BDCS_H_GZY.class, sql);
					// if(h_GZs!=null ){
					// for(BDCS_H_GZ h_GZ:h_GZs){
					// bdcdyhs+="'"+h_GZ.getBDCDYH()+"',";
					// }
					// }
				} else {
					// 实测户
					List<BDCS_H_GZ> h_GZs = baseCommonDao.getDataList(BDCS_H_GZ.class, sql);
					if (h_GZs != null) {
						for (BDCS_H_GZ h_GZ : h_GZs) {
							bdcdyhs += "'" + h_GZ.getBDCDYH() + "',";
						}
						// 删除户
						sql = "relationid NOT in (" + relationids.substring(0, relationids.length() - 1) + ") and xmbh='" + xmbh + "'";
						baseCommonDao.deleteEntitysByHql(BDCS_H_GZ.class, sql);
					}
				}
				sql = "BDCDYH  in (" + bdcdyhs.substring(0, bdcdyhs.length() - 1) + ") and xmbh='" + xmbh + "'";
				List<BDCS_DJDY_GZ> djdy_GZs = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, sql);
				if (djdy_GZs != null) {
					for (BDCS_DJDY_GZ h_GZ : djdy_GZs) {
						djdyids += "'" + h_GZ.getDJDYID() + "',";
					}
					// 删除登记单元
					sql = "DJDYID NOT in (" + djdyids.substring(0, djdyids.length() - 1) + ") and xmbh='" + xmbh + "'";
					baseCommonDao.deleteEntitysByHql(BDCS_DJDY_GZ.class, sql);
					// 删除权利
					sql = "DJDYID NOT in (" + djdyids.substring(0, djdyids.length() - 1) + ") and xmbh='" + xmbh + "'";
					baseCommonDao.deleteEntitysByHql(BDCS_QL_GZ.class, sql);
					// 删除附属权利
					sql = "DJDYID NOT in (" + djdyids.substring(0, djdyids.length() - 1) + ") and xmbh='" + xmbh + "'";
					baseCommonDao.deleteEntitysByHql(BDCS_FSQL_GZ.class, sql);
				}
			}

		}
		return flag;
	}

	// 初始登记
	private String InsertCSDJ(String xmbh, String casenum) {
		String flag = "false";
		List<GXJHXM> gxjhxms = baseCommonDao.getDataList(GXJHXM.class, "casenum like '%" + casenum + "%'");
		if (gxjhxms != null && gxjhxms.size() > 0) {
			// 先从GXJYK中查出该casenum所有的relationID
			List<String> relationids = new ArrayList<String>();
			/*for (int i = 0; i < gxjhxms.size(); i++) {
				List<JYH> jyhs = baseCommonDao.getDataList(JYH.class, "GXXMBH='" + gxjhxms.get(i).getGxxmbh() + "'");
				if (!relationids.contains(jyhs.get(0).getRELATIONID())) {
					relationids.add(jyhs.get(0).getRELATIONID());
				}
			}*/
			//ws 16/8/1 update
			 for (int i = 0; i<gxjhxms.size(); i++) {
				List<R_FW_QL> R_FW_QL = baseCommonDao.getDataList(R_FW_QL.class, "GXXMBH='" + gxjhxms.get(i).getGxxmbh() + "'");
				for(int j = 0; j<R_FW_QL.size(); j++){
					List<H> hs = baseCommonDao.getDataList(H.class, "RELATIONID='" + R_FW_QL.get(j).getHouseid() + "'");
					if (!relationids.contains(hs.get(0).getRelationid())) {
						relationids.add(hs.get(0).getRelationid());
					}
				}
			 }
			// 从调查库中查出所有的bdcdyid
			String bdcdyidString = "";
			for (int i = 0; i < relationids.size(); i++) {
				List<DCS_H_GZ> dcs_H_GZs = baseCommonDao.getDataList(DCS_H_GZ.class, "relationid='" + relationids.get(i) + "'");
				bdcdyidString += dcs_H_GZs.get(0).getId() + ",";
			}
			if (bdcdyidString.length() > 1) {
				bdcdyidString = bdcdyidString.substring(0, bdcdyidString.length() - 1);
				// 添加单元到登记库
				dyservice.addBDCDYNoCheck(xmbh, bdcdyidString);
				flag = "true";
			}
		}
		return flag;
	}

	protected String getXZQJC(String xzqhdm) {
		String jc = "";
		if (xzqhdm != null && !xzqhdm.equals("")) {
			String hql = MessageFormat.format(" CONSTSLSID IN (SELECT CONSTSLSID FROM BDCS_CONSTCLS WHERE CONSTCLSTYPE=''{0}'') AND CONSTVALUE=''{1}''", "SS",
					xzqhdm.substring(0, 2) + "0000");
			List<BDCS_CONST> lst1 = baseCommonDao.getDataList(BDCS_CONST.class, hql);

			if (lst1 != null && lst1.size() > 0) {
				jc = lst1.get(0).getBZ();
			}
		}
		return jc;
	}
	
}
