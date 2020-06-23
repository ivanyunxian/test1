package com.supermap.realestate.registration.service.impl.share;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oracle.net.aso.h;
import oracle.net.aso.q;
import oracle.sql.BLOB;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.record.PageBreakRecord.Break;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.mapping.HandlerMapping;
import com.supermap.realestate.registration.mapping.HandlerMapping.RegisterWorkFlow;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.DCS_H_GZ;
import com.supermap.realestate.registration.model.DCS_H_GZY;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.xmlmodel.BDCQLR;
import com.supermap.realestate.registration.service.ExtractDataService;
import com.supermap.realestate.registration.service.InsertDataService;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.DA_DBHelper;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.realestate.registration.util.JH_DBHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate_gx.registration.util.StringUtil;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.gxjyk.Gxjhxm;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYCFDJ;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYDYAQ;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYFDCQ;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYH;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYJYBGQH;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYQLR;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYXZDJ;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYYGDJ;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYYYDJ;
import com.supermap.wisdombusiness.framework.model.gxjyk.MATERDATA;
import com.supermap.wisdombusiness.framework.model.gxjyk.PROMATER;
import com.supermap.wisdombusiness.workflow.model.Wfi_MaterData;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProMater;
import com.supermap.wisdombusiness.workflow.service.common.Common;

@Service("extractDataService")
public class ExtractDataServiceImpl implements ExtractDataService {
	protected Logger logger = Logger.getLogger(ExtractDataServiceImpl.class);
	@Autowired
	protected InsertDataService insertDataService;

	@Autowired
	private ShareTool sharetool;
	
	@Autowired
	protected com.supermap.wisdombusiness.framework.dao.impl.CommonDaoJY CommonDaoJY;

	@Autowired
	protected CommonDao baseCommonDao;
	// 房屋状态，1为期房，2现房
	protected String fwzt = "2";
	//所有义务人
    protected String ywrs="";
    //gxxmbh和relationid键值对
    protected Map<String, String> gxxmbhRelationidMap=new HashMap<String, String>();
	@Override
	public String ExtractSXFromZJK(String casenum, String xmbh, boolean bool) {
		String flag = "false";
		try {
			// 业务类型判断
			BDCS_XMXX xmxx = baseCommonDao.get(BDCS_XMXX.class, xmbh);
			String unitetype="";
			if(xmxx!=null){
				RegisterWorkFlow flow = HandlerFactory.getWorkflow(xmxx.getPROJECT_ID());
				 unitetype=flow.getUnittype();
			}
			List<Gxjhxm> Gxjhxms =getGXJHXMList(casenum);
			if(Gxjhxms!=null){
				System.out.println("项目数："+Gxjhxms.size());
			}
			//项目信息页面显示房产业务号  同时将抽取的房产业务号保存到当前项目信息表中
			List<BDCS_XMXX> xmxxs=baseCommonDao.getDataList(BDCS_XMXX.class,"XMBH='"+xmbh+"'");
			xmxxs.get(0).setFCYWH(casenum);
			baseCommonDao.update(xmxxs.get(0));
			List<Wfi_ProInst> proinsts=baseCommonDao.getDataList(Wfi_ProInst.class,"File_Number='"+xmxxs.get(0).getPROJECT_ID()+"'");
			if(proinsts!=null&&proinsts.size()>0){
				Wfi_ProInst proinst=proinsts.get(0);
				proinst.setYwh(casenum);
				baseCommonDao.update(proinst);
				baseCommonDao.flush();
			}
			gxxmbhRelationidMap.clear();
			List<String> bdcdyhList=new ArrayList<String>();
			System.out.println("获取房屋id...");
			bdcdyhList =GetRealtionIDList(Gxjhxms);//GetbdcdyhList(casenum);
//			System.out.println("获取房屋状态...");
//			fwzt = GetFWZT(bdcdyhList.get(0));
			//判断受理的业务类型与单元来源是否匹配
//			if((unitetype.equals("031")&& !"2".equals(fwzt))||(unitetype.equals("032")&& !"1".equals(fwzt))){
//				flag ="受理的业务流程类型与房屋状态不匹配，或房屋id无法关联";
//				return flag;
//			}
			if(unitetype.equals("031")){
				fwzt="2";
			}else if(unitetype.equals("032")){
				fwzt="1";
			}
			// 从房产库中获取他项权利表中其他必要信息
			System.out.println("获取权利...");
			Map<BDCS_QL_GZ, BDCS_FSQL_GZ> rightMap = GetQLData(casenum,xmbh,Gxjhxms);
			List<BDCS_QL_GZ> qlList = new ArrayList<BDCS_QL_GZ>();
			List<BDCS_FSQL_GZ> fsqlList = new ArrayList<BDCS_FSQL_GZ>();
			if (rightMap.size() > 0) {
				for (Map.Entry<BDCS_QL_GZ, BDCS_FSQL_GZ> entry : rightMap
						.entrySet()) {
					qlList .add( entry.getKey());
					fsqlList.add( entry.getValue());
				}
			}
			System.out.println("获取权利人...");
			// 获取权利人
			List<BDCQLR> QlrList = GetQLR(casenum,Gxjhxms);
			List<JYQLR> JYQlrList = GetYWR(casenum,Gxjhxms);
            //拼接所有义务人
			getAllYWR(JYQlrList);
			
			String PROJECT_ID = xmxx.getPROJECT_ID();
			HandlerMapping _mapping = HandlerFactory.getMapping();
			String workflowcode = ProjectHelper
					.getWorkflowCodeByProjectID(PROJECT_ID);
			String _handleClassName = _mapping
					.getHandlerClassName(workflowcode);// "CSDJHandler";
			String qllx = xmxx.getQLLX();
			// --------------------------预告登记多加的判断，如果已经做过预告的不能再重复做，是选择器的方法-----------------------------------------
			if (_handleClassName.contains("handlerImpl.YGDJHandler")
					|| _handleClassName.contains("handlerImpl.YCDYDJHandler")) {
				System.out.println("预告业务抽取...");
				flag = getNoticeStatus(casenum);
				if (flag.contains("warning")) {
					return flag;
				}
			}
	        // ----------------------------------------------------------------------------------------------
			
            //如果是抵押变更、所有权变更、更正登记、初始登记
			if (_handleClassName.contains("BGDJHandler")&&!_handleClassName.contains("DYBGDJHandler")) {  //_handleClassName.contains("DYBGDJHandler")|| _handleClassName.contains("GZDJHandler")
				// 获取在办数据的RealtionID list
				if(bdcdyhList==null || bdcdyhList.size()<1){
					flag = "nofwztorh";
					return flag;
				}
				if (bdcdyhList.isEmpty() || bdcdyhList == null) {
					flag = "warning";
					return flag;
				}
				System.out.println("变更业务抽取...");
				// 变更、更正、初始登记外的单元信息是直接作为工作层，故在此传取单元list
				List<JYH> HList = GetH(casenum);
				List<JYJYBGQH> bgqhList = GetBGQH(casenum);
				flag = insertDataService.InsertSXFromZJKBG(qlList.get(0), fsqlList.get(0), xmbh, casenum, QlrList, JYQlrList, bgqhList, HList, bool, fwzt);
				return flag;
			}
//			else if (_handleClassName.contains("GZDJHandler")) {
//				// 获取在办数据的RealtionID list
//				if(bdcdyhList==null || bdcdyhList.size()<1){
//					flag = "nofwztorh";
//					return flag;
//				}
//				if (bdcdyhList.isEmpty() || bdcdyhList == null) {
//					flag = "warning";
//					return flag;
//				}
//				List<JYH> HList = GetH(casenum);
//				List<JYJYBGQH> bgqhList = GetBGQH(casenum);
//				flag = insertDataService.InsertSXFromZJKGZ(qlList.get(0), fsqlList.get(0), xmbh, casenum, QlrList, JYQlrList, bgqhList, HList, bool, fwzt);
//				return flag;
//			} 
			else {
				System.out.println("其他业务抽取...");
				//其他登记业务
				// 获取在办数据的RealtionID list
				String xzqdm =ConfigHelper.getNameByValue("XZQHDM");
					if(bdcdyhList.get(0).equals("nofwzt")){
						flag = "nofwztorh";
						return flag;
					}
//				}
				if (bdcdyhList.isEmpty() || bdcdyhList == null) {
					flag = "warning";
					return flag;
				}

				// 因除变更、更正、初始登记外的单元信息是从现状层拷贝到工作层，故在此传空单元list
				List<JYH> HList = new ArrayList<JYH>();
				flag = insertDataService.InsertSXFromZJK(qlList, fsqlList, xmbh,
						casenum, QlrList, JYQlrList, bdcdyhList,  bool,
						fwzt);
				//克拉玛依特殊处理，判断是否审核，如果是1，修改PROINST_STATUS=12
				if(xzqdm.contains("6502")&&flag.equals("true")){
					updateProintStatus(casenum,PROJECT_ID,12);
				}
				return flag;
			}
//			flag=demo();
		} 
		catch (Exception ex) {
			System.out.println(ex);
		}
		return flag;
	}


	//得到所有变更前户
	protected List<JYJYBGQH> GetBGQH(String casenum) {
		List<JYJYBGQH> Hlist = new ArrayList<JYJYBGQH>();
		List<Gxjhxm> Gxjhxms = CommonDaoJY
				.getDataList(Gxjhxm.class, "casenum = '" + casenum
						+ "'");
		String gxxmbh;
		// 该案卷号对应一个单元或多个单元
		if (Gxjhxms != null && Gxjhxms.size() > 0) {
			for (Gxjhxm Gxjhxm : Gxjhxms) {
				gxxmbh = Gxjhxm.getGxxmbh();
				List<JYJYBGQH> Hs = CommonDaoJY.getDataList(JYJYBGQH.class, "GXXMBH = '"
						+ gxxmbh + "'");
				// 当前单元可能为一个或多个
				if (Hs != null && Hs.size() > 0) {
					for (JYJYBGQH H : Hs) {
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
	protected List<BDCQLR> GetQLR(String casenum) throws SQLException {
		List<BDCQLR> Qlrs = new ArrayList<BDCQLR>();
		List<Gxjhxm> Gxjhxms = CommonDaoJY
				.getDataList(Gxjhxm.class, "casenum = '" + casenum
						+ "' and (GXLX !='99' or GXLX is null)");
		String gxxmbh;
		//不重复添加权利人
		List<String> zjhList=new ArrayList<String>();
		
		return GetQLR(casenum,Gxjhxms);
	}
	// 获取权利人
	protected List<BDCQLR> GetQLR(String casenum,List<Gxjhxm> Gxjhxms) throws SQLException {
			List<BDCQLR> Qlrs = new ArrayList<BDCQLR>();
			String gxxmbh;
			//不重复添加权利人
			List<String> zjhList=new ArrayList<String>();
			// 该案卷号对应一个单元或多个单元，权利人都一样，取一次就够了,但是，并案流程不行滴，取到4次一样的就跳出了
			int k=0;
			if (Gxjhxms != null && Gxjhxms.size() > 0) {
				for (Gxjhxm Gxjhxm : Gxjhxms) {
					gxxmbh =Gxjhxm.getGxxmbh();// Gxjhxm.getGxxmbh();
					String qllx = Gxjhxm.getQllx();
					//查封不用权利人
				if(!"800".equals(Gxjhxms.get(0).getDjdl())){
					String sql="select * from gxjyk.qlr where qlid not in "
							+ "(select qlid from gxjyk.JSYDSYQ where GXXMBH = '" + gxxmbh + "' ) and GXXMBH='"+gxxmbh+"' and sqrlb ='1'";
					List<Map> JYQlrs = CommonDaoJY.getDataListByFullSql(sql);
					if (JYQlrs != null && JYQlrs.size() > 0) {
						for (Map jyqlr : JYQlrs) {
							if(zjhList.contains(jyqlr.get("QLRMC").toString())){
								k++;
								continue;
							}
							BDCQLR bdcqlr = new BDCQLR();
							bdcqlr.setBDCQZH(jyqlr.get("BDCQZH") == null?"":jyqlr
									.get("BDCQZH").toString());
							bdcqlr.setSXH(jyqlr.get("SXH") == null ? "" : jyqlr
									.get("SXH").toString());
							bdcqlr.setQLRMC(jyqlr.get("QLRMC") ==null?"":jyqlr
									.get("QLRMC").toString());
							bdcqlr.setBDCQZH(jyqlr.get("BDCQZH") ==null?"":jyqlr
									.get("BDCQZH").toString());
							bdcqlr.setZJZL(jyqlr.get("ZJZL") ==null?"":jyqlr
									.get("ZJZL").toString());
							bdcqlr.setZJH(jyqlr.get("ZJH") ==null?"":jyqlr
									.get("ZJH").toString());
							bdcqlr.setFZJG(jyqlr.get("FZJG") ==null?"":jyqlr
									.get("FZJG").toString());
							bdcqlr.setSSHY(jyqlr.get("SSHY") ==null?"":jyqlr
									.get("SSHY").toString());
							bdcqlr.setGJ(jyqlr.get("GJ") ==null?"":jyqlr
									.get("GJ").toString());
							bdcqlr.setHJSZSS(jyqlr.get("HJSZSS") ==null?"":jyqlr
									.get("HJSZSS").toString());
							bdcqlr.setXB(jyqlr.get("XB") ==null?"":jyqlr
									.get("XB").toString());
							bdcqlr.setDH(jyqlr.get("DH") ==null?"":jyqlr
									.get("DH").toString());
							bdcqlr.setDZ(jyqlr.get("DZ") ==null?"":jyqlr
									.get("DZ").toString());
							bdcqlr.setYB(jyqlr.get("YB") ==null?"":jyqlr
									.get("YB").toString());
							bdcqlr.setGZDW(jyqlr.get("GZDW") ==null?"":jyqlr
									.get("GZDW").toString());
							bdcqlr.setDZYJ(jyqlr.get("DZYJ") ==null?"":jyqlr
									.get("DZYJ").toString());
							bdcqlr.setQLRLX(jyqlr.get("QLRLX") ==null?"":jyqlr
									.get("QLRLX").toString());
							bdcqlr.setQLBL(jyqlr.get("QLBL") ==null?"":jyqlr
									.get("QLBL").toString());
							bdcqlr.setGYFS(jyqlr.get("GYFS") ==null?"":jyqlr
									.get("GYFS").toString());
							bdcqlr.setGYQK(jyqlr.get("GYQK") ==null?"":jyqlr
									.get("GYQK").toString());
							bdcqlr.setBZ(jyqlr.get("BZ") ==null?"":jyqlr
									.get("BZ").toString());
							bdcqlr.setQLRID(jyqlr.get("QLRID") ==null?"":jyqlr
									.get("QLRID").toString());
							bdcqlr.setQLID(jyqlr.get("QLID") ==null?"":jyqlr
									.get("QLID").toString());
							Qlrs.add(bdcqlr);
							zjhList.add(jyqlr.get("QLRMC").toString());
						}
						if(k>3){
							break;
						}
					}
				}
				}
			}
			return Qlrs;
		}

	// 义务人
	protected List<JYQLR> GetYWR(String casenum) throws SQLException {
		List<JYQLR> JYQlrs = new ArrayList<JYQLR>();
		List<Gxjhxm> Gxjhxms = CommonDaoJY.getDataList(Gxjhxm.class,
				"casenum = '" + casenum
						+ "'  and (GXLX !='99' or GXLX is null)");
		return GetYWR(casenum,Gxjhxms);
	}
	//获取义务人
	protected List<JYQLR> GetYWR(String casenum,List<Gxjhxm> Gxjhxms) throws SQLException {
			List<JYQLR> JYQlrs = new ArrayList<JYQLR>();
			String gxxmbh;
			// 如果是一种业务多户，取一次，如果是一户的并案业务，都要取
			if (Gxjhxms != null && Gxjhxms.size() > 0) {
				List<JYQLR>  ywrList=new ArrayList<JYQLR>();
				String qllx="";
				for (Gxjhxm Gxjhxm : Gxjhxms) {
					if(qllx.equals(Gxjhxm.getQllx())){
						break;
					}
					qllx=Gxjhxm.getQllx();
					gxxmbh =Gxjhxm.getGxxmbh();// Gxjhxms.get(0).getGxxmbh();
					ywrList = CommonDaoJY.getDataList(JYQLR.class, "GXXMBH = '"
							+ gxxmbh + "'  AND SQRLB='2'");
					JYQlrs.addAll(ywrList);
				}
			}
			return JYQlrs;
		}

	// 单元
	protected List<JYH> GetH(String casenum) throws SQLException {
		List<JYH> Hlist = new ArrayList<JYH>();
		List<Gxjhxm> Gxjhxms = CommonDaoJY
				.getDataList(Gxjhxm.class, "casenum = '" + casenum
						+ "' and (GXLX !='99' or GXLX is null)");
		
		return GetH(casenum,Gxjhxms);
	}
	//获取单元
	protected List<JYH> GetH(String casenum,List<Gxjhxm> Gxjhxms ) throws SQLException {
		List<JYH> Hlist = new ArrayList<JYH>();
		String gxxmbh;
		// 该案卷号对应一个单元或多个单元
		if (Gxjhxms != null && Gxjhxms.size() > 0) {
			for (Gxjhxm Gxjhxm : Gxjhxms) {
				gxxmbh = Gxjhxm.getGxxmbh();
				List<JYH> Hs = CommonDaoJY.getDataList(JYH.class, "GXXMBH = '"
						+ gxxmbh + "'");
				// 当前单元可能为一个或多个
				if (Hs != null && Hs.size() > 0) {
					for (JYH H : Hs) {
						Hlist.add(H);
					}
				}
			}
		}
		return Hlist;
	}

	// 南宁(几家公司已经处理，在GXJYK户表中存在bdcdyh)不动产单元号list
	protected List<String> NNGetbdcdyhList(String casenum) throws SQLException {
		List<String> bdcdyhList = new ArrayList<String>();
		List<Gxjhxm> Gxjhxms = CommonDaoJY
				.getDataList(Gxjhxm.class, "casenum = '" + casenum
						+ "' and (GXLX !='99' or GXLX is null)");
		String gxxmbh;
		// 该案卷号对应一个单元或多个单元
		if (Gxjhxms != null && Gxjhxms.size() > 0) {
			for (Gxjhxm Gxjhxm : Gxjhxms) {
				gxxmbh = Gxjhxm.getGxxmbh();
				List<JYH> Hs = CommonDaoJY.getDataList(JYH.class, "GXXMBH = '"
						+ gxxmbh + "'");
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
	protected List<String> GetbdcdyhList(String casenum) throws SQLException {
			List<String> bdcdyhList = new ArrayList<String>();
			List<Gxjhxm> Gxjhxms = CommonDaoJY
					.getDataList(Gxjhxm.class,
							"casenum = '"+ casenum + "' and (GXLX !='99' or GXLX is null)");
			String gxxmbh;
			// 该案卷号对应一个单元或多个单元
			if (Gxjhxms != null && Gxjhxms.size() > 0) {
				for (Gxjhxm Gxjhxm : Gxjhxms) {
					gxxmbh = Gxjhxm.getGxxmbh();
					List<JYH> Hs = CommonDaoJY.getDataList(JYH.class,
							"GXXMBH = '" + gxxmbh+ "'");
					// 当前单元可能为一个或多个
					if (Hs != null && Hs.size() > 0) {
						for (JYH H : Hs) {
							List<BDCS_H_XZ> HXZS=baseCommonDao.getDataList(BDCS_H_XZ.class, "RELATIONID = '"+ H.getRELATIONID() +"'");
							if(HXZS.size()>0 && HXZS!=null){
								bdcdyhList.add(HXZS.get(0).getBDCDYH());
							}
							fwzt=H.getFWZT();
						}
					}
				}
			}
			return bdcdyhList;
		}
	
	// 权利
	protected Map<BDCS_QL_GZ, BDCS_FSQL_GZ> GetQLData(String casenum,String xmbh) throws SQLException {
		Map<BDCS_QL_GZ, BDCS_FSQL_GZ> rightMap = new HashMap<BDCS_QL_GZ, BDCS_FSQL_GZ>();
		/************************************************* 石家庄房产交易库抽取方法分界线 ***************************************************************/
		/************************************************* 共享交易库抽取分界线 ***************************************************************/
		JYFDCQ Fdcq = new JYFDCQ();
		JYDYAQ Dyaq = new JYDYAQ();
		JYCFDJ Cfdj = new JYCFDJ();
		JYYGDJ Ygdj = new JYYGDJ();
		JYXZDJ Xzdj = new JYXZDJ();
		JYYYDJ Yydj = new JYYYDJ();
		String sql = "casenum = '" + casenum
				+ "'  and (GXLX !='99' or GXLX is null)";
		List<Gxjhxm> Gxjhxms = CommonDaoJY.getDataList(Gxjhxm.class, sql);// JYGXJHXM
		return GetQLData(casenum,xmbh,Gxjhxms);
	}
	//获取权利
		protected Map<BDCS_QL_GZ, BDCS_FSQL_GZ> GetQLData(String casenum,String xmbh,List<Gxjhxm> Gxjhxms) throws SQLException {
			Map<BDCS_QL_GZ, BDCS_FSQL_GZ> rightMap = new HashMap<BDCS_QL_GZ, BDCS_FSQL_GZ>();
			/************************************************* 石家庄房产交易库抽取方法分界线 ***************************************************************/
			/************************************************* 共享交易库抽取分界线 ***************************************************************/
			JYFDCQ Fdcq = new JYFDCQ();
			JYDYAQ Dyaq = new JYDYAQ();
			JYCFDJ Cfdj = new JYCFDJ();
			JYYGDJ Ygdj = new JYYGDJ();
			JYXZDJ Xzdj = new JYXZDJ();
			JYYYDJ Yydj = new JYYYDJ();
			String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
			String xmbhcond = ProjectHelper.GetXMBHCondition(xmbh);
			List<BDCS_XMXX> xmxx = baseCommonDao.getDataList(
					BDCS_XMXX.class, xmbhcond);
			String PROJECT_ID = xmxx.get(0).getPROJECT_ID();
			HandlerMapping _mapping = HandlerFactory.getMapping();
			String workflowcode = ProjectHelper
					.getWorkflowCodeByProjectID(PROJECT_ID);
			String _handleClassName = _mapping
					.getHandlerClassName(workflowcode);// "CSDJHandler";
			String qllx = xmxx.get(0).getQLLX();
			String gxxmbh;
			if (Gxjhxms != null && Gxjhxms.size() > 0) {
				for (Gxjhxm Gxjhxm : Gxjhxms) {
					gxxmbh = Gxjhxm.getGxxmbh();
					String relationidString="";
					if(gxxmbhRelationidMap.containsKey(gxxmbh)){
						relationidString=gxxmbhRelationidMap.get(gxxmbh);
					}
					// 房地产权
					List<JYFDCQ> Fdcqs = CommonDaoJY.getDataList(JYFDCQ.class,
							"GXXMBH = '" + gxxmbh + "'");
					if (Fdcqs != null && Fdcqs.size() > 0) {
						Fdcq = Fdcqs.get(0);
						rightMap.putAll(setFDCQ(Fdcq, casenum,Gxjhxm.getDjdl(),Gxjhxm.getQllx(),relationidString));
					}
					// 抵押权
					List<JYDYAQ> Dyaqs = CommonDaoJY.getDataList(JYDYAQ.class,
							"GXXMBH = '" + gxxmbh + "'");
					if (Dyaqs != null && Dyaqs.size() > 0) {
						Dyaq = Dyaqs.get(0);
						rightMap.putAll(setDYAQ(Dyaq, casenum,Gxjhxm.getDjdl(),Gxjhxm.getQllx(),relationidString));
					}
					// 查封登记
					List<JYCFDJ> Cfdjs = CommonDaoJY.getDataList(JYCFDJ.class,
							"GXXMBH = '" + gxxmbh + "'");
					if (Cfdjs != null && Cfdjs.size() > 0) {
						Cfdj = Cfdjs.get(0);
						rightMap.putAll(setCFDJ(Cfdj, casenum,Gxjhxm.getDjdl(),Gxjhxm.getQllx(),relationidString));
					}
					// 预告登记
					List<JYYGDJ> Ygdjs = CommonDaoJY.getDataList(JYYGDJ.class,
							"GXXMBH = '" + gxxmbh + "'");
					if (Ygdjs != null && Ygdjs.size() > 0) {
						Ygdj = Ygdjs.get(0);
						rightMap.putAll(setYGDJ(Ygdj, casenum,Gxjhxm.getDjdl(),Gxjhxm.getQllx(),relationidString));
					}
					// 限制登记
					List<JYXZDJ> Xzdjs = CommonDaoJY.getDataList(JYXZDJ.class,
							"GXXMBH = '" + gxxmbh + "'");
					if (Xzdjs != null && Xzdjs.size() > 0) {
						Xzdj = Xzdjs.get(0);
						rightMap.putAll(setXZDJ(Xzdj, casenum,Gxjhxm.getDjdl(),Gxjhxm.getQllx(),relationidString));
					}
					// 异议登记
					List<JYYYDJ> Yydjs = CommonDaoJY.getDataList(JYYYDJ.class,
							"GXXMBH = '" + gxxmbh + "'");
					if (Yydjs != null && Yydjs.size() > 0) {
						Yydj = Yydjs.get(0);
						rightMap.putAll(setYYDJ(Yydj, casenum,Gxjhxm.getDjdl(),Gxjhxm.getQllx(),relationidString));
					}
					// 南宁专用，抽取分户图
				//	if (xzqdm != null && xzqdm.contains("4501")) {
				//		fcfhtwj(gxxmbh);
				//	}

//					if (_handleClassName.contains("DYBGDJHandler")) {
//						// 抵押变更登记
//						rightMap = setDYAQ(Dyaq,casenum);
//					} else if (_handleClassName.contains("BGDJHandler")
//							|| _handleClassName.contains("GZDJHandler")) {
//						// 变更、更正
//						rightMap = setFDCQ(Fdcq,casenum);
//
//					} else if (_handleClassName.contains("BZDJHandler")
//							|| _handleClassName.contains("YSBZGGDJHandler")) {
//						// 补证
//						if (qllx.equals(QLLX.DIYQ.Value)) {
//							rightMap = setDYAQ(Dyaq,casenum);
//						} else {
//							rightMap = setFDCQ(Fdcq,casenum);
//						}
//					} else if (_handleClassName.contains("HZDJHandler")) {
//						// 换证、
//						if (qllx.equals(QLLX.DIYQ.Value)) {
//							rightMap = setDYAQ(Dyaq,casenum);
//						} else {
//							rightMap = setFDCQ(Fdcq,casenum);
//						}
//					} else if (_handleClassName.contains("YCFDJ_HouseHandler")) {
//						// 在建工程查封（预测）
//						rightMap = setCFDJ(Cfdj,casenum);
//					} else if (_handleClassName.contains("CFDJ_HouseHandler")
//							|| _handleClassName.contains("CFDJ_XF_HouseHandler")) {
//						// 查封
//						rightMap = setCFDJ(Cfdj,casenum);
//					} else if (_handleClassName.contains("CFDJ_ZX_HouseHandler")) {
//						// 解封
//						rightMap = setCFDJ(Cfdj,casenum);
//					} else if (_handleClassName.contains("CSDJHandler")) {
//						// 初始登记
//						rightMap = setFDCQ(Fdcq,casenum);
//					} else if (_handleClassName.contains("ZY_YDYTODY_DJHandler")) {
//						// 转移+预抵押登记
//						
//					} else if (_handleClassName.contains("ZYYGDYYGDJHandler")||_handleClassName.contains("ZYYG_YDY_DJHandler")) {
//						// 转移预告+抵押预告登记
//						rightMap = setYGDJ(Ygdj,casenum);
//						Map<BDCS_QL_GZ, BDCS_FSQL_GZ> dyaqMap = new HashMap<BDCS_QL_GZ, BDCS_FSQL_GZ>();
//						dyaqMap=setDYAQ(Dyaq, casenum);
//						rightMap.putAll(dyaqMap);
//					} else if (_handleClassName.contains("DYYGZXDJHandler")
//							|| _handleClassName.contains("DYZXDJHandler")
//							|| _handleClassName.contains("YDYZXDYDJHandler")) {
//						// 抵押注销、抵押预告注销登记，预抵押注销
//						rightMap = setDYAQ(Dyaq,casenum);
//					} else if (_handleClassName.contains("YCDYDJHandler")) {
//						// 在建工程抵押登记（预测）
//						rightMap = setDYAQ(Dyaq,casenum);
//					} else if (_handleClassName.contains("YCSCDYDJHandler")) {
//						// 在建工程抵押转现房抵押
//						rightMap = setDYAQ(Dyaq,casenum);
//					} else if (_handleClassName.contains("YGYDYDJHandler")) {
//						// 预告预抵押登记
//						rightMap = setYGDJ(Ygdj,casenum);
//					} else if (_handleClassName.contains("CSDYHandler")
//							|| _handleClassName.contains("DYDJHandler")
//							|| _handleClassName.contains("DYYGDJHandler")
//							|| _handleClassName.contains("YDYDJHandler")) {
//						// 抵押登记、预抵押
//						rightMap = setDYAQ(Dyaq,casenum);
//					} else if (_handleClassName.contains("DYLimitedDJHandler")
//							|| _handleClassName.contains("DYLimitLiftedDJHandler")
//							|| _handleClassName.contains("XZDJHandler")) {
//						// 单元限制登记
//						rightMap = setXZDJ(Xzdj,casenum);
//					} else if (_handleClassName.contains("ZYYGZXDJHandler")) {
//						// 转移预告注销
//						rightMap = setYGDJ(Ygdj,casenum);
//					} else if (_handleClassName.contains("YYZXDJHandler")) {
//						// 异议注销登记
//						rightMap = setYYDJ(Yydj,casenum);
//					} else if (_handleClassName.contains("ZXDJHandler")) {
//						// 所有权注销登记
//						rightMap = setFDCQ(Fdcq,casenum);
//					} else if (_handleClassName.contains("DYZYDJHandler")) {
//						// 抵押转移登记
//						rightMap = setDYAQ(Dyaq,casenum);
//					} else if (_handleClassName.contains("ZYDJHandler")) {
//						// 转移登记
//						rightMap = setFDCQ(Fdcq,casenum);
//					} else if (_handleClassName.contains("YGDJHandler")) {
//						// 预告登记
//						rightMap = setYGDJ(Ygdj,casenum);
//					} else if (_handleClassName.contains("YYDJHandler")) {
//						// 异议登记
//						rightMap = setYYDJ(Yydj,casenum);
//					} else if (_handleClassName.contains("ZY_DY_DJHandler")) {
//						// 转移+抵押登记
//						
//					}
				}
			}
			return rightMap;
		}

	// 设置房地产权
	protected Map<BDCS_QL_GZ, BDCS_FSQL_GZ> setFDCQ(JYFDCQ Fdcq,String casenum,String djlx,String qllx,String relationid) {
		Map<BDCS_QL_GZ, BDCS_FSQL_GZ> rightMap = new HashMap<BDCS_QL_GZ, BDCS_FSQL_GZ>();
		BDCS_QL_GZ ql = new BDCS_QL_GZ();
		BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
		if (Fdcq != null) {
			ql.setId(Common.CreatUUID());
			ql.setBDCDYH(Fdcq.getBDCDYH());
			ql.setYWH(Fdcq.getYWH());
			ql.setQLLX(qllx);//Fdcq.getQLLX()
			ql.setDJLX(djlx);//Fdcq.getDJLX()
			ql.setDJYY(Fdcq.getDJYY());
			ql.setBDCQZH(Fdcq.getBDCQZH());
			ql.setQXDM(Fdcq.getQXDM());
			ql.setDJJG(Fdcq.getDJJG());
			ql.setDBR(Fdcq.getDBR());
			ql.setDJSJ(Fdcq.getDJSJ());
			ql.setFJ(Fdcq.getFJ());
			ql.setQSZT(Fdcq.getQSZT());
			ql.setTDSHYQR(Fdcq.getTDSHYQR());
			ql.setQDJG(Fdcq.getFDCJYJG());
			ql.setCASENUM(casenum);
			ql.setARCHIVES_BOOKNO(relationid);//将relationid临时保存在备注中，后面关联权利用
			fsql.setFDZL(Fdcq.getFDZL());
			fsql.setTDSYQR(Fdcq.getTDSHYQR());
			fsql.setDYTDMJ(Fdcq.getDYTDMJ());
			fsql.setFTTDMJ(Fdcq.getFTTDMJ());
			fsql.setFDCJYJG(Fdcq.getFDCJYJG());
			fsql.setGHYT(Fdcq.getGHYT());
			fsql.setFWXZ(Fdcq.getFWXZ());
			fsql.setFWJG(Fdcq.getFWJG());
//			fsql.setSZC(Fdcq.getSZC());
			fsql.setZCS(Fdcq.getZCS());
			fsql.setJZMJ(Fdcq.getJZMJ());
			fsql.setZYJZMJ(Fdcq.getZYJZMJ());
			fsql.setFTJZMJ(Fdcq.getFTJZMJ());
			fsql.setJGSJ(Fdcq.getJGSJ());
			fsql.setQLID(ql.getId());
			fsql.setHTBH(Fdcq.getTSBH());
			fsql.setCASENUM(casenum);
		}
		rightMap.put(ql, fsql);
		return rightMap;
	}

	// 设置抵押权
	protected Map<BDCS_QL_GZ, BDCS_FSQL_GZ> setDYAQ(JYDYAQ Dyaq,String casenum,String djlx,String qllx,String relationid) {
		Map<BDCS_QL_GZ, BDCS_FSQL_GZ> rightMap = new HashMap<BDCS_QL_GZ, BDCS_FSQL_GZ>();
		BDCS_QL_GZ ql = new BDCS_QL_GZ();
		BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
		if (Dyaq != null) {
			ql.setId(Common.CreatUUID());
			ql.setBDCDYH(Dyaq.getBDCDYH());
			ql.setYWH(Dyaq.getYWH());
			ql.setDJLX(djlx);
			ql.setQLLX(qllx);
			ql.setDJYY(Dyaq.getDJYY());
			ql.setQLQSSJ(Dyaq.getZWLXQSSJ());
			ql.setQLJSSJ(Dyaq.getZWLXJSSJ());
			ql.setQXDM(Dyaq.getQXDM());
			ql.setDJJG(Dyaq.getDJJG());
			ql.setDBR(Dyaq.getDBR());
			ql.setDJSJ(Dyaq.getDJSJ());
			ql.setFJ(Dyaq.getFJ());
			ql.setARCHIVES_BOOKNO(relationid);//将relationid临时保存在备注中，后面关联权利用
			ql.setBDCQZH(Dyaq.getBDCQZH());
			int qszt = 0;
			if (Dyaq.getQSZT() != null) {
				qszt = Integer.parseInt(Dyaq.getQSZT());
			}
			ql.setQSZT(qszt);
			ql.setLYQLID(Dyaq.getLYQLID());
			ql.setZSBH(Dyaq.getZSBH());
			ql.setBZ(Dyaq.getBZ());
			
			fsql.setDYBDCLX(Dyaq.getDYBDCLX());
			fsql.setDYR(Dyaq.getDYR());
			fsql.setDYFS(Dyaq.getDYFS());
			fsql.setZJJZWZL(Dyaq.getZJJZWZL());
			fsql.setZJJZWDYFW(Dyaq.getZJJZWDYFW());
			fsql.setBDBZZQSE(Dyaq.getBDBZZQSE());
			fsql.setZGZQQDSS(Dyaq.getZGZQQDSS());
			fsql.setZGZQSE(Dyaq.getZGZQSE());
			fsql.setZXDYYWH(Dyaq.getZXDYYWH());
			fsql.setZXDYYY(Dyaq.getZXDYYY());
			fsql.setZXSJ(Dyaq.getZXSJ());
			fsql.setQLID(ql.getId());
			fsql.setDYMJ(Dyaq.getDYMJ());
			ql.setCASENUM(casenum);
			fsql.setCASENUM(casenum);
			fsql.setHTBH(Dyaq.getTSBH());
//			//拼接所有义务人
//			fsql.setYWR(ywrs);
		}
		rightMap.put(ql, fsql);
		return rightMap;
	}

	// 设置预告权
	protected Map<BDCS_QL_GZ, BDCS_FSQL_GZ> setYGDJ(JYYGDJ Ygdj,String casenum,String djlx,String qllx,String relationid) {
		Map<BDCS_QL_GZ, BDCS_FSQL_GZ> rightMap = new HashMap<BDCS_QL_GZ, BDCS_FSQL_GZ>();
		BDCS_QL_GZ ql = new BDCS_QL_GZ();
		BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
		if (Ygdj != null) {
			ql.setId(Common.CreatUUID());
			ql.setBDCDYH(Ygdj.getBDCDYH());
			ql.setYWH(Ygdj.getYWH());
			ql.setDJLX(djlx);
			ql.setQLLX(qllx);
			ql.setDJYY(Ygdj.getDJYY());
			ql.setDBR(Ygdj.getDBR());
			ql.setDJSJ(Ygdj.getDJSJ());
			ql.setFJ(Ygdj.getFJ());
			int qszt = 0;
			if (Ygdj.getQSZT() != null) {
				qszt = Integer.parseInt(Ygdj.getQSZT());
			}
			ql.setQSZT(qszt);
			ql.setZSBH(Ygdj.getZSBH());
			ql.setBZ(Ygdj.getBZ());
			ql.setQDJG(Ygdj.getQDJG());
			ql.setARCHIVES_BOOKNO(relationid);//将relationid临时保存在备注中，后面关联权利用
			fsql.setBDCZL(Ygdj.getBDCZL());
			fsql.setYWR(Ygdj.getYWR());
			fsql.setYWRZJZL(Ygdj.getYWRZJZL());
			fsql.setYWRZJH(Ygdj.getYWRZJH());
			fsql.setYGDJZL(Ygdj.getYGDJZL());
			fsql.setTDSYQR(Ygdj.getTDSYQR());
			fsql.setGHYT(Ygdj.getGHYT());
			fsql.setFWXZ(Ygdj.getFWXZ());
			fsql.setFWJG(Ygdj.getFWJG());
//			fsql.setSZC(Ygdj.getSZC());
			fsql.setZCS(Ygdj.getZCS());
			fsql.setJZMJ(Ygdj.getJZMJ());
			fsql.setBDBZZQSE(Ygdj.getQDJG());
			fsql.setQLID(ql.getId());
			ql.setCASENUM(casenum);
			fsql.setCASENUM(casenum);
			fsql.setHTBH(Ygdj.getTSBH());
			//拼接所有义务人
			if(fsql.getYWR()==null||fsql.getYWR().equals("")){
				fsql.setYWR(ywrs);
			}
			
		}
		rightMap.put(ql, fsql);
		return rightMap;
	}

	// 设置查封权
	protected Map<BDCS_QL_GZ, BDCS_FSQL_GZ> setCFDJ(JYCFDJ Cfdj,String casenum,String djlx,String qllx,String relationid) {
		Map<BDCS_QL_GZ, BDCS_FSQL_GZ> rightMap = new HashMap<BDCS_QL_GZ, BDCS_FSQL_GZ>();
		BDCS_QL_GZ ql = new BDCS_QL_GZ();
		BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
		if (Cfdj != null) {
			ql.setId(Common.CreatUUID());
			ql.setBDCDYH(Cfdj.getBDCDYH());
			ql.setYWH(Cfdj.getYWH());
			ql.setQLQSSJ(Cfdj.getQLQSSJ());
			ql.setQLJSSJ(Cfdj.getQLJSSJ());
			ql.setQXDM(Cfdj.getQXDM());
			ql.setDJJG(Cfdj.getDJJG());
			ql.setDBR(Cfdj.getDBR());
			ql.setDJSJ(Cfdj.getDJSJ());
			ql.setDBR(Cfdj.getJFDBR());
			ql.setFJ(Cfdj.getFJ());
			ql.setDJLX(djlx);
			ql.setQLLX(qllx);
			ql.setARCHIVES_BOOKNO(relationid);//将relationid临时保存在备注中，后面关联权利用
			int qszt = 0;
			if (Cfdj.getQSZT() != null) {
				qszt = Integer.parseInt(Cfdj.getQSZT());
			}
			ql.setQSZT(qszt);
			ql.setLYQLID(Cfdj.getLYQLID());
			ql.setBZ(Cfdj.getBZ());
			fsql.setCFJG(Cfdj.getCFJG());
			fsql.setCFLX(Cfdj.getCFLX());
//			fsql.setCFWJ(Cfdj.getCFWJ().toString());
			fsql.setCFWH(Cfdj.getCFWH());
			fsql.setCFFW(Cfdj.getCFFW());
			fsql.setJFJG(Cfdj.getJFJG());
//			fsql.setJFWJ(Cfdj.getJFWJ().toString());
			fsql.setJFWH(Cfdj.getJFWH());
			fsql.setQLID(ql.getId());
		}
		rightMap.put(ql, fsql);
		return rightMap;
	}

	// 设置异议
	protected Map<BDCS_QL_GZ, BDCS_FSQL_GZ> setYYDJ(JYYYDJ Yydj,String casenum,String djlx,String qllx,String relationid) {
		Map<BDCS_QL_GZ, BDCS_FSQL_GZ> rightMap = new HashMap<BDCS_QL_GZ, BDCS_FSQL_GZ>();
		BDCS_QL_GZ ql = new BDCS_QL_GZ();
		BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
		if (Yydj != null) {
			ql.setId(Common.CreatUUID());
			ql.setBDCDYH(Yydj.getBDCDYH());
			ql.setYWH(Yydj.getYWH());
			ql.setQXDM(Yydj.getQXDM());
			ql.setDJJG(Yydj.getDJJG());
			ql.setDBR(Yydj.getDBR());
			ql.setDJSJ(Yydj.getDJSJ());
			ql.setFJ(Yydj.getFJ());
			ql.setQLLX(qllx);
			ql.setDJLX(djlx);
			ql.setARCHIVES_BOOKNO(relationid);//将relationid临时保存在备注中，后面关联权利用
			int qszt = 0;
			if (Yydj.getQSZT() != null) {
				qszt = Integer.parseInt(Yydj.getQSZT());
			}
			ql.setQSZT(qszt);
			ql.setBZ(Yydj.getBZ());
			fsql.setYYSX(Yydj.getYYSX());
			fsql.setZXDYYWH(Yydj.getZXYYYWH());
			fsql.setZXYYYY(Yydj.getZXYYYY());
			fsql.setQLID(ql.getId());
			ql.setCASENUM(casenum);
			fsql.setCASENUM(casenum);
		}
		rightMap.put(ql, fsql);
		return rightMap;
	}

	// 设置限制登记
	protected Map<BDCS_QL_GZ, BDCS_FSQL_GZ> setXZDJ(JYXZDJ Xzdj,String casenum,String djlx,String qllx,String relationid) {
		Map<BDCS_QL_GZ, BDCS_FSQL_GZ> rightMap = new HashMap<BDCS_QL_GZ, BDCS_FSQL_GZ>();
		BDCS_QL_GZ ql = new BDCS_QL_GZ();
		BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
		if (Xzdj != null) {
			ql.setId(Common.CreatUUID());
			ql.setBDCDYH(Xzdj.getBDCDYH());
			ql.setYWH(Xzdj.getYWH());
			ql.setQXDM(Xzdj.getQXDM());
			ql.setDJJG(Xzdj.getDJJG());
			ql.setDBR(Xzdj.getDBR());
			ql.setDJSJ(Xzdj.getDJSJ());
			ql.setFJ(Xzdj.getFJ());
			ql.setDJLX(djlx);
			ql.setQLLX(qllx);
			ql.setARCHIVES_BOOKNO(relationid);//将relationid临时保存在备注中，后面关联权利用
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
			fsql.setQLID(ql.getId());
			ql.setCASENUM(casenum);
			fsql.setCASENUM(casenum);
		}
		rightMap.put(ql, fsql);
		return rightMap;
	}

	@Override
	public boolean ExtractFJFromZJK(String proinstId, String caseNum,
			String configFilePath) {
		boolean flag = false;
		Connection connection = null;
		try {
			connection = JH_DBHelper.getConnect_jy();
			// 从“图像表”取得当前合同号（案卷号）所有图片属性信息
			String imageScanSql = "select IMAGEID,FILEID from arcuser.img_imagescan where casenum = '"
					+ caseNum + "' order by fileid, pageinnumber ";
			PreparedStatement pstmt = connection.prepareStatement(imageScanSql);
			ResultSet imageScanResultSet = pstmt.executeQuery();
//			ResultSet imageScanResultSet = JH_DBHelper.excuteQuery(connection,imageScanSql);
			int i = 1;
			while (imageScanResultSet.next()) {
				// 根据“图像ID”进行循环
				String imageid = imageScanResultSet.getString("IMAGEID");
				String fileid = imageScanResultSet.getString("FILEID");
				String imageSql = "select IMAGEDATA from arcuser.img_imagedatajpg where IMAGEID='"
						+ imageid + "'";
				PreparedStatement pstmt2 = connection.prepareStatement(imageSql);
				ResultSet imageResultSet = pstmt2.executeQuery();
//				ResultSet imageResultSet = JH_DBHelper.excuteQuery(connection,imageSql);
				String fileidSql = "select fileid from house.opr_filereceived where CASENUM='"
						+ caseNum + "' and FILETYPE = '" + fileid + "'";
				PreparedStatement pstmt3 = connection.prepareStatement(fileidSql);
				ResultSet fileResultSet = pstmt3.executeQuery();
//				ResultSet fileResultSet = JH_DBHelper.excuteQuery(connection,fileidSql);
				if (imageResultSet.next() && fileResultSet.next()) {
					Blob blob = imageResultSet.getBlob("IMAGEDATA");
					String fileName = fileResultSet.getString("FILEID");
					InputStream inStream = blob.getBinaryStream();
					byte[] buf = InputStreamToByte(inStream);
					insertDataService.InsertFJFromZJK(proinstId, fileName,
							configFilePath, buf, i);
					flag = true;
					i++;
				}
				pstmt3.close();
				fileResultSet.close();
				pstmt2.close();
				imageResultSet.close();
			}
			pstmt.close();
			imageScanResultSet.close();
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

	@Override
	public String GetBatchProject(String prodefid, String batchNumber,
			HttpServletRequest request, String number) {
		return "";
	}

	@Override
	public String GetProject(String prodefid, String xmbh, String batchNumber,
			String casenum, String proinstid, String count, String num,
			HttpServletRequest request) {
		return "";
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
			List<PROMATER> gxjy_promaters = CommonDaoJY.getDataList(
					PROMATER.class, "PROINST_ID = '" + caseNum
							+ "' order by MATERIAL_INDEX");
			if (gxjy_promaters != null && gxjy_promaters.size() > 0) {
				Properties props = new Properties();
				// 协同站点基准URL
				String shareURL = ConfigHelper.getNameByValue("URL_SHARE");
				int m = 1;
				// 循环每一个中间库中的收件种类
				for (int i = 0; i < gxjy_promaters.size(); i++) {
					String condt = "PROINST_ID='" + proinstId
							+ "' and MATERIAL_NAME='"
							+ gxjy_promaters.get(i).getMATERIAL_NAME() + "'";
					List<Wfi_ProMater> wfi_promaters = baseCommonDao
							.getDataList(Wfi_ProMater.class, condt);
					if (wfi_promaters != null && wfi_promaters.size() > 0) {
						// 获取workflow库中对应附件类型的ID
						String materilinstID = wfi_promaters.get(0)
								.getMaterilinst_Id();
						// 插入附件前，先删除该模板下的原有附件
						baseCommonDao.deleteEntitysByHql(Wfi_MaterData.class,
								"MATERILINST_ID='" + materilinstID + "'");

						// 获取中间库每个种类下的所有文件
						String MATERILINST_ID=gxjy_promaters.get(i).getMATERILINST_ID();
						List<MATERDATA> gxjy_materdatas = CommonDaoJY.getDataList(MATERDATA.class,"MATERILINST_ID = '"+ MATERILINST_ID+ "'");
						if (gxjy_materdatas != null
								&& gxjy_materdatas.size() > 0) {
							for (int k = 0; k < gxjy_materdatas.size(); k++) {
								//通用版抽取 2016年8月30日 15:09:39 
								String TYCQFROMZJK = ConfigHelper.getNameByValue("TYCQFROMZJK");
								//附件抽取3中方式：图片直接存到数据库、图片实际Url、图片存储路径
								String xzqdm=ConfigHelper.getNameByValue("XZQHDM");
								//【利用协同站点从前置机相对路径抽取附件】图片存储路径 齐齐哈尔
								if(TYCQFROMZJK!=null&& TYCQFROMZJK.equals("1") && xzqdm.equals("230200")){
									String imgURL =  gxjy_materdatas.get(k).getRELATIVEURL();
									String fileName = gxjy_materdatas.get(k).getFILE_NAME();
									// 插入到workflow库
									insertDataService.InsertFJFromZJKEx(proinstId,
											fileName, materilinstID, imgURL, m,null);
									m++;
								}
								//【图片服务抽取附件方式】南宁市通过协同站点抽取
								else if(TYCQFROMZJK!=null&& TYCQFROMZJK.equals("1") && xzqdm.equals("450100")){
									String imgURL = "";
									String path = gxjy_materdatas.get(k).getPATH();
									String relativeURL = gxjy_materdatas.get(k)
											.getRELATIVEURL();
									String fileName = gxjy_materdatas.get(k)
											.getFILE_PATH();
//									if (shareURL != null && !shareURL.equals("")
//											&& relativeURL != null
//											&& !relativeURL.equals("")) {
//										// 从站点外通过封装的服务取图片
//										imgURL = shareURL + relativeURL+"/1";
//									} else {
//										// 从站点下相对路径取图片
//										imgURL = shareURL + path + "\\" + fileName;
//									}
									imgURL=relativeURL;
									// 插入到workflow库
									insertDataService.InsertFJFromZJKEx(proinstId,
											fileName, materilinstID, imgURL, m,null);
									m++;
								}
								//【通用版  图片相对路径或BLOB字段自动判别抽取附件方式】图片直接存到数据库  延吉市
								else{
									//图片BLOB
									Blob imgTHUMB =  gxjy_materdatas.get(k).getTHUMB();
									String fileName = gxjy_materdatas.get(k).getFILE_NAME();
									//imgPATH图片绝对路径路径
									String path=gxjy_materdatas.get(k).getPATH();
									String file_path=gxjy_materdatas.get(k).getFILE_PATH();
									String materialString=ConfigHelper.getNameByValue("material");
									String imgPATH=materialString+"\\"+path+"\\"+file_path;
									// 插入到workflow库
									insertDataService.InsertFJFromZJKEx(proinstId,
											fileName, materilinstID, imgPATH, m,imgTHUMB);
									m++;
								}
								
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
			BufferedOutputStream out = new BufferedOutputStream(
					new FileOutputStream(img));
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
		String sql = "select PROINST_ID from bdc_workflow.wfi_proinst where FILE_NUMBER='"
				+ projectid + "'";
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
	protected List<String> GetRealtionIDList(String casenum) throws SQLException {
		List<String> bdcdyhList = new ArrayList<String>();
		List<Gxjhxm> Gxjhxms = CommonDaoJY
				.getDataList(Gxjhxm.class, "casenum = '" + casenum
						+ "' and (GXLX !='99' or GXLX is null)");
		String gxxmbh;
		// 该案卷号对应一个单元或多个单元
		if (Gxjhxms != null && Gxjhxms.size() > 0) {
			for (Gxjhxm Gxjhxm : Gxjhxms) {
				gxxmbh = Gxjhxm.getGxxmbh();
				List<JYH> Hs = CommonDaoJY.getDataList(JYH.class, "GXXMBH = '"
						+ gxxmbh + "'");
				// 当前单元可能为一个或多个
				if (Hs != null && Hs.size() > 0) {
					for (JYH H : Hs) {
						bdcdyhList.add(H.getRELATIONID());
						fwzt = H.getFWZT();
						if(fwzt==null || fwzt.isEmpty()){
							List<String> nofwzt = new ArrayList<String>();
							nofwzt.add("nofwzt");
							return nofwzt;
						}
// 						fwzt="2";
					}
				}
			}
		}
		return bdcdyhList;
	}
	
	// 获取在办数据的RealtionID list
		protected List<String> GetRealtionIDList(List<Gxjhxm> Gxjhxms) throws SQLException {
			List<String> bdcdyhList = new ArrayList<String>();
			String gxxmbh;
			int k=0;
			// 该案卷号对应一个单元或多个单元，如果取到超过3个单元都是一样的，就跳出，提高效率
			if (Gxjhxms != null && Gxjhxms.size() > 0) {
				for (Gxjhxm Gxjhxm : Gxjhxms) {
					gxxmbh = Gxjhxm.getGxxmbh();
					List<JYH> Hs = CommonDaoJY.getDataList(JYH.class, "GXXMBH = '"
							+ gxxmbh + "'");
					// 当前单元可能为一个或多个
					if (Hs != null && Hs.size() > 0) {
						for (JYH H : Hs) {
							if(!bdcdyhList.contains(H.getRELATIONID())){
								bdcdyhList.add(H.getRELATIONID());
								//此处等待新判断房屋状态的方法替换。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。
								fwzt=GetFWZT(H.getRELATIONID());
//								fwzt = H.getFWZT();
								if(fwzt==null || fwzt.isEmpty()){
									List<String> nofwzt = new ArrayList<String>();
									nofwzt.add("nofwzt");
									return nofwzt;
								}
							}
//							else {
//								k++;
//							}
							if(!gxxmbhRelationidMap.containsKey(gxxmbh)){
								gxxmbhRelationidMap.put(gxxmbh, H.getRELATIONID());
							}
						}
					}
					if(k>3){
						break;
					}
				}
			}
			return bdcdyhList;
		}

	/**
	 * 读取分户图 南宁特有方法 作者：梁秦
	 * 
	 * @param gxxmbh
	 * @return
	 */
	protected void fcfhtwj(String gxxmbh) {
		// 根据GXXMBH获取bdcdyid，fcfhtwj
		if (gxxmbh != null) {
			ResultSet gxjy_h = null;
			try {
				gxjy_h = CommonDaoJY
						.excuteQuery("select fcfhtwj,bdcdyid from gxjyk.h where  GXXMBH = '"
								+ gxxmbh + "'");

				if (gxjy_h != null && gxjy_h.next()) {
					// System.out.println("获取的gxxmbh为："+gxxmbh);
					String bdcdyid = gxjy_h.getString("BDCDYID");
					// System.out.println("根据gxxmbh获取不动产单元ID为："+bdcdyid);
					Blob fcfhtwj = gxjy_h.getBlob("FCFHTWJ");
					// System.out.println("根据gxxmbh获取分层分户图为："+fcfhtwj);
					// 将数据存入
					if (fcfhtwj != null) {
						String update = "update bdck.bdcs_h_xz set fcfhtwj = ? where bdcdyid  = '"
								+ bdcdyid + "' ";
					//	CommonDaoJY.excuteuUpdate(update, fcfhtwj);
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	//将所有义务人连接起来
	protected void getAllYWR(List<JYQLR> ywrList) {
		if(ywrList!=null&&ywrList.size()>0){
			for(int i=0;i<ywrList.size();i++){
				ywrs+=ywrList.get(i).getQLRMC()+",";
			}
			ywrs=ywrs.substring(0, ywrs.length()-1);
		}
	}

	/**
	 * 通过房产接口读取json格式数据存入共享交易库 作者：卜晓波
	 * 
	 * @param 房产业务号：casenum     组织机构代码：zzjgdm
	 * @return 接口返回数据保存成功消息，失败原因
	 */
	//@Override
	public String SaveDatabyFCInterface(String casenum){
		String flag="false"; 
		//通过casenum调用房产接口获取json
		String fcinterfaceurl =ConfigHelper.getNameByValue("FCreturnInterface")+casenum;// "http://169.254.127.187:8090/lzservice/ws_GetData?orgcode=L10&jkcode=L1010&ywbh="+casenum+"";
        StringBuilder json = new StringBuilder();
        try {
        	//此处调用房产webservice获取json数据
            URL url = new URL(fcinterfaceurl);
            URLConnection urlcon = url.openConnection();
            BufferedReader bf = new BufferedReader(new InputStreamReader(urlcon.getInputStream(),"UTF-8"));
            String inputLine = null;
            inputLine = bf.readLine();
//            inputLine = test();
//        	String inputLine ="{'errorCode':'1','errorMsg':'操作成功','data':{'GXJHXM':[{'QLLX':'','GXXMBH':'2016010400004623','DJDL':'房产转让备案','BLJD':'发证','CASENUM':'2016010400004623','TSSJ':'2016-06-22 15:34:28'}],'H':[{'QSC':'','ZZC':'','ZCS':'7','CH':'3','SZC':'3','ZL':'西环路5号中山城市花园六区1B栋2单元3-1','MJDW':185.93,'SHBW':'303','FWZT':'2','RELATIONID':'912011071322101','GXXMBH':'2016010400004623'}],'FDCQ':[{'BDCQZH':'D0168559','QLLX':'房产转让备案','DJLX':'200','DJYY':'','FDZL':'西环路5号中山城市花园六区1B栋2单元3-1','TDSYQR':'卢明','TDSYQSSJ':'','TDSYJSSJ':'','GHYT':'住宅','SZC':'3','ZCS':'7','JZMJ':185.93,'DJJG':'柳州市房产交易所','DBR':'陈云玲','DJSJ':'2016-06-12 11:26:54','GXXMBH':'2016010400004623','CZFS':'','FDCJYJG':''}],'DYAQ':[],'QLR':[{'SXH':'1','QLRMC':'石美英','QLBL':' ','GYFS':'','GYQK':'','GXXMBH':'2016010400004623','SQRLB':'2','ZJZL':'身份证','ZJH':'450211197006202224'},{'SXH':'1','QLRMC':'卢明','QLBL':' ','GYFS':'','GYQK':'','GXXMBH':'2016010400004623','SQRLB':'1','ZJZL':'身份证','ZJH':'450202195406050017'}]}}";
            if ( inputLine != null) {
                json.append(inputLine);
                JSONObject jsonObj = JSONObject.fromObject(json.toString());
                //取出房产json串中的data  errorCode  errorMsg
                String errorCode=jsonObj.get("errorCode").toString();
                String errorMsg=jsonObj.get("errorMsg").toString();
                if(errorCode.equals("1")){
                	Object data=jsonObj.get("data");
                    JSONObject dataobj=JSONObject.fromObject(data);
                    JSONArray gxjhxmArray= dataobj.getJSONArray("GXJHXM");
                    JSONArray fdcqArray= dataobj.getJSONArray("FDCQ");
                    JSONArray dyaqArray= dataobj.getJSONArray("DYAQ");
                    JSONArray qlrArray= dataobj.getJSONArray("QLR");
                    /*************将H保存到共享交易库,同时生成对应GXJHXM、FDCQ或DYAQ、QLR记录数并保存*************/
            		JSONArray hArray= dataobj.getJSONArray("H");
            		if(hArray.size()>0){
            			String saveflag=SaveH(hArray,gxjhxmArray,fdcqArray,dyaqArray,qlrArray);
					if (saveflag.equals("true")) {
						flag = "true";
						return flag;
					} else {
						flag = "false";
						return flag;
					}
            		}
//                    bf.close();
                    
                }else{
                	flag="false:errorCode:"+errorCode+",errorMsg:"+errorMsg;
                	return flag;
                }
            }else{
            	 flag="false";
                 return flag;
            }
        } catch (MalformedURLException e) {
        	flag="false:"+e.toString();
        	return flag;
        } catch (IOException e) {
        	flag="false:"+e.toString();
        	return flag;
        }
			return flag;
	}
	
	protected String SaveH(JSONArray hArray,JSONArray gxjhxmArray,JSONArray fdcqArray,JSONArray dyaqArray,JSONArray qlrArray) {
		String saveflag="false";
		try {
			List<String> qlrzjhList=new ArrayList<String>();
			List<String> ywrzjhList=new ArrayList<String>();
			for (int i = 0; i < hArray.size(); i++) {
				JYH H = new JYH();
				String temp = hArray.getString(i);
				JSONObject tempobject = JSONObject.fromObject(temp);
				String gxxmbh=UUID.randomUUID().toString().replace("-", "");
				String id=UUID.randomUUID().toString().replace("-", "");
				H.setId(id);
				H.setGXXMBH(gxxmbh);
//				H.setQSC(!tempobject.getString("QSC").equals(null)?tempobject.getString("QSC"):"");
//				H.setZZC(!tempobject.getString("ZZC").equals(null)?tempobject.getString("ZZC"):"");
				H.setZCS(!tempobject.getString("ZCS").equals(null)?Integer.parseInt(tempobject.getString("ZCS")):null);
				//吉林房产总层数不给改 我们只能特别编译类文件  2016年12月8日 00:07:48  卜晓波 共享交易库 户表 总层数 应该是int但恶心房产只推string
//				H.setZCS(!tempobject.getString("ZCS").equals(null)?tempobject.getString("ZCS"):null);
				H.setCH(!tempobject.getString("CH").equals(null)?tempobject.getString("CH"):"");
				H.setSZC(!tempobject.getString("SZC").equals(null)?tempobject.getString("SZC"):"");
				H.setZL(!tempobject.getString("ZL").equals(null)?tempobject.getString("ZL"):"");
				H.setMJDW(!tempobject.getString("MJDW").equals(null)?tempobject.getString("MJDW"):"");
				H.setSHBW(!tempobject.getString("SHBW").equals(null)?tempobject.getString("SHBW"):"");
				Map<String,String> fwztmap=GetFC2BDCDIR("FWZT");
				H.setFWZT(!tempobject.getString("FWZT").equals(null)?fwztmap.get(tempobject.getString("FWZT")):"");
				H.setRELATIONID(!tempobject.getString("RELATIONID").equals(null)?tempobject.getString("RELATIONID"):"");
				CommonDaoJY.save(H);
				CommonDaoJY.flush();
				String qllx=null;
				if(fdcqArray.size()>0){
					SaveFDCQ(fdcqArray,gxxmbh);
					qllx="4";
				}
				if(dyaqArray.size()>0){
					SaveDYAQ(dyaqArray,gxxmbh);
					qllx="23";
				}
				SaveGXJHXM(gxjhxmArray,gxxmbh,qllx);
				SaveQLR(qlrArray, gxxmbh,qlrzjhList,ywrzjhList);
				saveflag = "true";
//				return saveflag;
			}
		} catch (Exception e) {
			return saveflag;
		}
		return saveflag;
	}
	protected void SaveGXJHXM(JSONArray gxjhxmArray,String gxxmbh,String qllx) {
		try {
				Gxjhxm gxjhxm = new Gxjhxm();
				String temp = gxjhxmArray.getString(0);
				JSONObject tempobject = JSONObject.fromObject(temp);
				String id=UUID.randomUUID().toString().replace("-", "");
				gxjhxm.setBsm(id);
				gxjhxm.setGxxmbh(gxxmbh);
//				Map<String,String> qllxmap=GetFC2BDCDIR("QLLX");
//				gxjhxm.setQllx(!tempobject.getString("QLLX").equals(null)?qllxmap.get(tempobject.getString("QLLX")):"");
				gxjhxm.setQllx(qllx);
//				gxjhxm.setZl(!tempobject.getString("zl").equals(null)?tempobject.getString("zl"):"");
				Map<String,String> djlxmap=GetFC2BDCDIR("DJDL");
				gxjhxm.setDjdl(!tempobject.getString("DJDL").equals(null)?djlxmap.get(tempobject.getString("DJDL")):"");
				gxjhxm.setBljd(!tempobject.getString("BLJD").equals(null)?tempobject.getString("BLJD"):"");
				gxjhxm.setCasenum(!tempobject.getString("CASENUM").equals(null)?tempobject.getString("CASENUM"):"");
				if(!tempobject.getString("TSSJ").equals(null) && !tempobject.getString("TSSJ").equals("")){
					 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					 Date tssj = sdf.parse(tempobject.getString("TSSJ"));
					 gxjhxm.setTssj(tssj);
				}
				CommonDaoJY.save(gxjhxm);
				CommonDaoJY.flush();
		} catch (Exception e) {

		}
	}
	protected void SaveFDCQ(JSONArray fdcqArray,String gxxmbh) {
		try {
				JYFDCQ fdcq = new JYFDCQ();
				String temp = fdcqArray.getString(0);
				JSONObject tempobject = JSONObject.fromObject(temp);
				String id=UUID.randomUUID().toString().replace("-", "");
				fdcq.setBSM(id);
				fdcq.setGXXMBH(gxxmbh);
				fdcq.setBDCQZH(!tempobject.getString("BDCQZH").equals(null)?tempobject.getString("BDCQZH"):"");
//				Map<String,String> qllxmap=GetFC2BDCDIR("QLLX");
//				fdcq.setQLLX(!tempobject.getString("QLLX").equals(null)?qllxmap.get(tempobject.getString("QLLX")):"");
				fdcq.setQLLX("4");
				Map<String,String> djlxmap=GetFC2BDCDIR("DJDL");
				fdcq.setDJLX(!tempobject.getString("DJLX").equals(null)?djlxmap.get(tempobject.getString("DJLX")):"");
				fdcq.setDJYY(!tempobject.getString("DJYY").equals(null)?tempobject.getString("DJYY"):"");
				fdcq.setFDZL(!tempobject.getString("FDZL").equals(null)?tempobject.getString("FDZL"):"");
				fdcq.setTDSHYQR(!tempobject.getString("TDSYQR").equals(null)?tempobject.getString("TDSYQR"):"");
				if(!tempobject.getString("TDSYQSSJ").equals(null) && !tempobject.getString("TDSYQSSJ").equals("")){
					 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					 Date tdsyqssj = sdf.parse(tempobject.getString("TDSYQSSJ"));
					 fdcq.setQLQSSJ(tdsyqssj);
				}
				if(!tempobject.getString("TDSYJSSJ").equals(null) && !tempobject.getString("TDSYJSSJ").equals("")){
					 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					 Date tdsyjssj = sdf.parse(tempobject.getString("TDSYJSSJ"));
					 fdcq.setQLJSSJ(tdsyjssj);
				}
				fdcq.setGHYT(!tempobject.getString("GHYT").equals(null)?tempobject.getString("GHYT"):"");
				fdcq.setSZC(tempobject.getString("SZC"));
				fdcq.setZCS(!tempobject.getString("ZCS").equals(null)?Integer.parseInt(tempobject.getString("ZCS")):null);
				fdcq.setJZMJ(!tempobject.getString("JZMJ").equals(null)?Double.parseDouble(tempobject.getString("JZMJ")):null);
//				fdcq.setZYJZMJ(!tempobject.getString("zyjzmj").equals(null)?Double.parseDouble(tempobject.getString("zyjzmj")):null);
//				fdcq.setFTJZMJ(!tempobject.getString("ftjzmj").equals(null)?Double.parseDouble(tempobject.getString("ftjzmj")):null);
				fdcq.setDJJG(!tempobject.getString("DJJG").equals(null)?tempobject.getString("DJJG"):"");
				fdcq.setDBR(!tempobject.getString("DBR").equals(null)?tempobject.getString("DBR"):"");
				if(!tempobject.getString("DJSJ").equals(null) && !tempobject.getString("DJSJ").equals("")){
					 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					 Date djsj = sdf.parse(tempobject.getString("DJSJ"));
					 fdcq.setDJSJ(djsj);
				}
				fdcq.setCZFS(!tempobject.getString("CZFS").equals(null)?tempobject.getString("CZFS"):"");
//				fdcq.setFDCJYJG(!tempobject.getString("FDCJYJG").equals(null)?Double.parseDouble(tempobject.getString("FDCJYJG")):null);
				CommonDaoJY.save(fdcq);
				CommonDaoJY.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	protected void SaveDYAQ(JSONArray dyaqArray,String gxxmbh) {
		try {
				JYDYAQ dyaq = new JYDYAQ();
				String temp = dyaqArray.getString(0);
				JSONObject tempobject = JSONObject.fromObject(temp);
				String id=UUID.randomUUID().toString().replace("-", "");
				dyaq.setBSM(id);
				dyaq.setGXXMBH(gxxmbh);
				Map<String,String> dybdclxmap=GetFC2BDCDIR("DYBDCLX");
				dyaq.setDYBDCLX(!tempobject.getString("DYBDCLX").equals(null)?dybdclxmap.get(tempobject.getString("DYBDCLX")):"");
				dyaq.setDYR(!tempobject.getString("DYR").equals(null)?tempobject.getString("DYR"):"");
				Map<String,String> dyfsxmap=GetFC2BDCDIR("DYFS");
				dyaq.setDYFS(!tempobject.getString("DYFS").equals(null)?dyfsxmap.get(tempobject.getString("DYFS")):"");
				Map<String,String> djlxmap=GetFC2BDCDIR("DJDL");
				dyaq.setDJLX(!tempobject.getString("DJLX").equals(null)?djlxmap.get(tempobject.getString("DJLX")):"");
				dyaq.setDJYY(!tempobject.getString("DJYY").equals(null)?tempobject.getString("DJYY"):"");
				dyaq.setBDBZZQSE(!tempobject.getString("BDBZZQSE").equals(null)?Double.parseDouble(tempobject.getString("BDBZZQSE")):null);
				if(!tempobject.getString("ZWLXQSSJ").equals(null) && !tempobject.getString("ZWLXQSSJ").equals("")){
					 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					 Date zwlxqssj = sdf.parse(tempobject.getString("ZWLXQSSJ"));
					 dyaq.setZWLXQSSJ(zwlxqssj);
				}
				if(!tempobject.getString("ZWLXJSSJ").equals(null) && !tempobject.getString("ZWLXJSSJ").equals("")){
					 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					 Date zwlxjssj = sdf.parse(tempobject.getString("ZWLXJSSJ"));
					 dyaq.setZWLXJSSJ(zwlxjssj);
				}
				dyaq.setBDCDJZMH(!tempobject.getString("BDCDJZMH").equals(null)?tempobject.getString("BDCDJZMH"):"");
				dyaq.setDJJG(!tempobject.getString("DJJG").equals(null)?tempobject.getString("DJJG"):"");
				dyaq.setDBR(!tempobject.getString("DBR").equals(null)?tempobject.getString("DBR"):"");
				if(!tempobject.getString("DJSJ").equals(null) && !tempobject.getString("DJSJ").equals("")){
					 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					 Date djsj = sdf.parse(tempobject.getString("DJSJ"));
					 dyaq.setDJSJ(djsj);
				}
				dyaq.setLYQLID(!tempobject.getString("LYQLID").equals(null)?tempobject.getString("LYQLID"):"");
//				dyaq.setBDCDYID(!tempobject.getString("bdcdyid").equals(null)?tempobject.getString("bdcdyid"):"");
				dyaq.setDYMJ(!tempobject.getString("DYMJ").equals(null)?Double.parseDouble(tempobject.getString("DYMJ")):null);
//				dyaq.setZSBH(!tempobject.getString("zsbh").equals(null)?tempobject.getString("zsbh"):"");
				dyaq.setZGZQQDSS(!tempobject.getString("ZGZQQDSS").equals(null)?tempobject.getString("ZGZQQDSS"):"");
				dyaq.setZGZQSE(!tempobject.getString("ZGZQSE").equals(null)?Double.parseDouble(tempobject.getString("ZGZQSE")):null);
				CommonDaoJY.save(dyaq);
				CommonDaoJY.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	protected void SaveQLR(JSONArray qlrArray,String gxxmbh,List<String> qlrzjhList,List<String> ywrzjhList) {
		try {
			for (int i = 0; i < qlrArray.size(); i++) {
				JYQLR qlr = new JYQLR();
				String temp = qlrArray.getString(i);
				JSONObject tempobject = JSONObject.fromObject(temp);
				String sqrlb=!tempobject.getString("SQRLB").equals(null)?tempobject.getString("SQRLB"):"";
				String zjh=!tempobject.getString("ZJH").equals(null)?tempobject.getString("ZJH"):"";
				if(sqrlb.equals("1")){
					if(!qlrzjhList.contains(zjh)){
						qlrzjhList.add(zjh);
					}
					else {
						continue;
					}
				}
				if(sqrlb.equals("2")){
					if(!ywrzjhList.contains(zjh)){
						ywrzjhList.add(zjh);
					}
					else {
						continue;
					}
				}
				String id=UUID.randomUUID().toString().replace("-", "");
				qlr.setId(id);
				qlr.setGXXMBH(gxxmbh);
				qlr.setSXH(!tempobject.getString("SXH").equals(null)?Integer.parseInt(tempobject.getString("SXH")):null);
				qlr.setQLRMC(!tempobject.getString("QLRMC").equals(null)?tempobject.getString("QLRMC"):"");
//				qlr.setQLRLX(!tempobject.getString("QLRLX").equals(null)?tempobject.getString("QLRLX"):"");
				qlr.setQLBL(!tempobject.getString("QLBL").equals(null)?tempobject.getString("QLBL"):"");
				qlr.setGYFS(!tempobject.getString("GYFS").equals(null)?tempobject.getString("GYFS"):"");
				qlr.setGYQK(!tempobject.getString("GYQK").equals(null)?tempobject.getString("GYQK"):"");
//				Map<String,String> sqrlbmap=GetFC2BDCDIR("SQRLB");
//				qlr.setSQRLB(!tempobject.getString("SQRLB").equals(null)?sqrlbmap.get(tempobject.getString("SQRLB")):"");
				qlr.setSQRLB(sqrlb);
//				qlr.setQLBL();
				Map<String,String> zjzlbmap=GetFC2BDCDIR("ZJZL");
				qlr.setZJZL(!tempobject.getString("ZJZL").equals(null)?zjzlbmap.get(tempobject.getString("ZJZL")):"");
				qlr.setZJH(zjh);
				CommonDaoJY.save(qlr);
				CommonDaoJY.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected Map<String, String> GetFC2BDCDIR(String dirname)
			throws SQLException {
		Map<String, String> dirmap = new HashMap<String, String>();
		String sql = "select * from gxdjk.BDC2FC_DIRECTORY where BDCDIRNAME='"
				+ dirname + "'";
		Connection jyConnection = JH_DBHelper.getConnect_gxdjk();
		PreparedStatement pstmt = jyConnection.prepareStatement(sql);
		ResultSet rSet = pstmt.executeQuery();
//		ResultSet rSet = JH_DBHelper.excuteQuery(jyConnection, sql);
		if (rSet != null) {
			while (rSet.next()) {
				String fcdirvalue = rSet.getString("fcdirvalue");
				String bdcdirvalue = rSet.getString("bdcdirvalue");
				dirmap.put(fcdirvalue, bdcdirvalue);
			}
			pstmt.close();
			rSet.close();
		}
		// 重新打开一下连接释放游标资源
		jyConnection.close();
		jyConnection = null;
		jyConnection = JH_DBHelper.getConnect_gxdjk();
		return dirmap;
	}
	
	String test()
	{
//		return "{\"GXJHXM\":[{\"QLLX\":\"4\",\"GXXMBH\":\"1\",\"DJDL\":\"200\",\"BLJD\":\"归档\",\"CASENUM\":\"201202280274\",\"TSSJ\":\"2016-11-08 09:09:31\"}],\"H\":[{\"GXXMBH\":\"1\",\"BDCDYH\":\"20120228027000001\",\"QSC\":\"1\",\"ZZC\":\"7\",\"ZCS\":\"7\",\"CH\":\"1\",\"SZC\":\"1\",\"ZL\":\"温泉镇农科所门口垌第一栋南楼A12号商铺\",\"MJDW\":\"1\",\"SHBW\":\"\",\"FWZT\":\"0\",\"RELATIONID\":\"450802001047GB01401F00010024\"}],\"FDCQ\":[{\"GXXMBH\":\"1\",\"QLLX\":\"4\",\"BDCQZH\":\"10019586\",\"DJDL\":\"200\",\"DJYY\":\"\",\"FDZL\":\"温泉镇农科所门口垌第一栋南楼A12号商铺\",\"TDSYQR\":\"\",\"TDSYQSSJ\":\"\",\"TDSYJSSJ\":\"\",\"GHYT\":\"商业用房\",\"SZC\":\"1\",\"ZCS\":\"7\",\"JZMJ\":\"21.37\",\"DJJG\":\"陆川房管局\",\"DBR\":\"\",\"DJSJ\":\"\",\"CZFS\":\"1\",\"FDCJYJG\":\"0\"}],\"DYAQ\":[],\"YGDJ\":[],\"QLR\":[{\"GXXMBH\":\"1\",\"SXH\":\"1\",\"QLRMC\":\"杨思婷\",\"QLRLX\":\"1\",\"QLBL\":\"\",\"GYFS\":\"1\",\"GYQK\":\"\",\"SQRLB\":\"1\",\"ZJZL\":\"身份证\",\"ZJH\":\"450922198208162381\"}]}";
		return "{\"GXJHXM\":[{\"QLLX\":\"4\",\"GXXMBH\":\"64e8c55e-07f3-427c-90ee-573967eda8ef\",\"DJDL\":\"200\",\"BLJD\":\"归档\",\"CASENUM\":\"201205240026\",\"TSSJ\":\"2016-12-22 17:24:37\"},{\"QLLX\":\"4\",\"GXXMBH\":\"ef323b60-7392-442b-a5b2-cc202c9a4af6\",\"DJDL\":\"200\",\"BLJD\":\"归档\",\"CASENUM\":\"201205240026\",\"TSSJ\":\"2016-12-22 17:24:37\"}],\"H\":[{\"GXXMBH\":\"64e8c55e-07f3-427c-90ee-573967eda8ef\",\"QSC\":\"1\",\"ZZC\":\"7\",\"ZCS\":\"7\",\"CH\":\"1\",\"SZC\":\"1\",\"ZL\":\"温泉镇碰搪三队（兴达五巷南边）车库\",\"MJDW\":\"1\",\"SHBW\":\"\",\"FWZT\":\"2\",\"RELATIONID\":\"20120524002600002\"},{\"GXXMBH\":\"ef323b60-7392-442b-a5b2-cc202c9a4af6\",\"QSC\":\"1\",\"ZZC\":\"7\",\"ZCS\":\"7\",\"CH\":\"3\",\"SZC\":\"3\",\"ZL\":\"温泉镇碰搪三队（兴达五巷南边）\",\"MJDW\":\"1\",\"SHBW\":\"\",\"FWZT\":\"2\",\"RELATIONID\":\"20120524002600003\"}],\"FDCQ\":[{\"GXXMBH\":\"64e8c55e-07f3-427c-90ee-573967eda8ef\",\"QLLX\":\"4\",\"BDCQZH\":\"06012794\",\"DJDL\":\"200\",\"DJYY\":\"\",\"FDZL\":\"温泉镇碰搪三队（兴达五巷南边）\",\"TDSYQR\":\"\",\"TDSYQSSJ\":\"\",\"TDSYJSSJ\":\"\",\"GHYT\":\"成套住宅\",\"SZC\":\"3\",\"ZCS\":\"7\",\"JZMJ\":\"98.074\",\"DJJG\":\"陆川房管局\",\"DBR\":\"\",\"DJSJ\":\"\",\"CZFS\":\"0\",\"FDCJYJG\":\"0\"},{\"GXXMBH\":\"ef323b60-7392-442b-a5b2-cc202c9a4af6\",\"QLLX\":\"4\",\"BDCQZH\":\"06012794\",\"DJDL\":\"200\",\"DJYY\":\"\",\"FDZL\":\"温泉镇碰搪三队（兴达五巷南边）\",\"TDSYQR\":\"\",\"TDSYQSSJ\":\"\",\"TDSYJSSJ\":\"\",\"GHYT\":\"成套住宅\",\"SZC\":\"3\",\"ZCS\":\"7\",\"JZMJ\":\"98.074\",\"DJJG\":\"陆川房管局\",\"DBR\":\"\",\"DJSJ\":\"\",\"CZFS\":\"0\",\"FDCJYJG\":\"0\"}],\"DYAQ\":[],\"YGDJ\":[],\"QLR\":[{\"GXXMBH\":\"64e8c55e-07f3-427c-90ee-573967eda8ef\",\"SXH\":\"1\",\"QLRMC\":\"林荣奎\",\"QLRLX\":\"1\",\"QLBL\":\"\",\"GYFS\":\"0\",\"GYQK\":\"\",\"SQRLB\":\"1\",\"ZJZL\":\"身份证\",\"ZJH\":\"450922197608151732\"},{\"GXXMBH\":\"64e8c55e-07f3-427c-90ee-573967eda8ef\",\"SXH\":\"2\",\"QLRMC\":\"林荣奎\",\"QLRLX\":\"1\",\"QLBL\":\"\",\"GYFS\":\"0\",\"GYQK\":\"\",\"SQRLB\":\"1\",\"ZJZL\":\"身份证\",\"ZJH\":\"450922197608151732\"},{\"GXXMBH\":\"ef323b60-7392-442b-a5b2-cc202c9a4af6\",\"SXH\":\"1\",\"QLRMC\":\"林荣奎\",\"QLRLX\":\"1\",\"QLBL\":\"\",\"GYFS\":\"0\",\"GYQK\":\"\",\"SQRLB\":\"1\",\"ZJZL\":\"身份证\",\"ZJH\":\"450922197608151732\"},{\"GXXMBH\":\"ef323b60-7392-442b-a5b2-cc202c9a4af6\",\"SXH\":\"2\",\"QLRMC\":\"林荣奎\",\"QLRLX\":\"1\",\"QLBL\":\"\",\"GYFS\":\"0\",\"GYQK\":\"\",\"SQRLB\":\"1\",\"ZJZL\":\"身份证\",\"ZJH\":\"450922197608151732\"}]}";
	}
	//广西省厅房产推数据到GXJYK测试
	public String demo() {
		String resultString="";
		//待传输的json数据
		String jsonString=test();//"{\"name\":\"张三\"}";
		JSONObject jsondata=JSONObject.fromObject(jsonString);
		//用户名密码
		String uname="u1";
		String pwd="1";
		HttpURLConnection connection;
		try {
			//请求服务地址10.1.2.73
			URL postUrl=new URL("http://10.1.2.73:8080/share/app/sharezjk/fctobdc");
			connection = (HttpURLConnection) postUrl.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			connection.connect();
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			// 正文，正文内容其实跟get的URL中 '? '后的参数字符串一致
			String content = "username=" + URLEncoder.encode(uname, "UTF-8");
			content += "&password="+ URLEncoder.encode(pwd, "UTF-8");
			content += "&jsondata="+ URLEncoder.encode(jsondata.toString(), "UTF-8");
			out.writeBytes(content);
			out.flush();
			out.close();
			JSONObject jsonObj = new JSONObject();
			StringBuilder json = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line = null;
			resultString = reader.readLine();
			reader.close();
			connection.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultString;
	}

	/**
	 * 根据casenum获取在办项目数据
	 * @作者 likun
	 * @创建时间 2016年8月25日上午10:09:44
	 * @param casenum
	 * @return
	 */
	List<Gxjhxm> getGXJHXMList(String casenum){
		String sql="casenum = '" + casenum
				+ "' and (GXLX !='99' or GXLX is null)";
		List<Gxjhxm> Gxjhxms = CommonDaoJY
				.getDataList(Gxjhxm.class, sql);
		return Gxjhxms;
	}

	

	/**
	 * 克拉玛依特殊处理，判断是否审核，如果是1，修改PROINST_STATUS=12
	 * @作者 likun
	 * @创建时间 2016年8月30日下午3:40:11
	 * @param casenum 
	 * @param projectid 
	 * @param status 修改成新的值
	 */
	private void updateProintStatus(String casenum,String projectid,int status) {
			String sql="select SFSH from GXJYK.GXJHXM where casenum='"+casenum+"'";
			Connection connection=JH_DBHelper.getConnect_jy();
			ResultSet rSet=null;
			PreparedStatement pstmt =null;
			try {
				 pstmt = connection.prepareStatement(sql);
				 rSet = pstmt.executeQuery();
//				rSet = JH_DBHelper.excuteQuery(connection, sql);
			if(rSet!=null){
				while(rSet.next()){
					String sfsh=rSet.getString(1);
					if(sfsh!=null&&sfsh.equals("1")){
						List<Wfi_ProInst> proInsts= baseCommonDao.getDataList(Wfi_ProInst.class, "FILE_NUMBER='"+projectid+"'");
					    if(proInsts!=null&&proInsts.size()>0){
					    	Wfi_ProInst proInst=proInsts.get(0);
					    	proInst.setProinst_Status(status);
					    	baseCommonDao.update(proInst);
					    	baseCommonDao.flush();
					    }
					}
					break;
				}
			}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			try {
				pstmt.close();
				rSet.close();
				connection.close();
			} catch (SQLException e) {
			}
	}

	/**
	 * 判断是否做过预告登记
	 * @作者 think
	 * @创建时间 2016年9月2日下午5:35:08
	 * @param casenum
	 * @return
	 */
	 private String getNoticeStatus(String casenum) {
		String flag="";
		// 找到relationid
		String gxh = "select relationid from gxjyk.h where gxxmbh in (select gxxmbh from gxjyk.gxjhxm where casenum = '"
				+ casenum + "' and gxlx = '99')";
		List<Map> data_jy = CommonDaoJY.getDataListByFullSql(gxh);
		if (data_jy.size() > 0 && data_jy != null) {
			String relationid = "";
			relationid = data_jy.get(0).get("RELATIONID")==null?"":data_jy.get(0).get(
							"RELATIONID").toString();
			// 找到bdcdyh
			String sql_hy = "select bdcdyh from  bdck.bdcs_h_xzy where relationid ='"
					+ relationid + "'";
			List<Map> data_bdc = baseCommonDao
					.getDataListByFullSql(sql_hy);
			if (data_bdc.size() > 0 && data_bdc != null) {
				String bdcdyh = "";
				bdcdyh = data_bdc.get(0).get("BDCDYH")==null?"":data_bdc.get(0).get(
								"BDCDYH").toString();
				if ("".equals(bdcdyh)) {

				} else {
					// 判断语句
					String sql = "SELECT DY.BDCDYH from BDCK.BDCS_H_XZY DY LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY "
							+ " ON DJDY.BDCDYID=DY.BDCDYID AND DJDY.DJDYID IS NOT NULL LEFT JOIN BDCK.BDCS_QL_XZ QL"
							+ " ON QL.DJDYID=DJDY.DJDYID  AND QL.QLLX='4' WHERE QL.QLID IS NULL  and DY.BDCDYH like '%"
							+ bdcdyh + "%' ";
					List<Map> data3 = baseCommonDao
							.getDataListByFullSql(sql);
					if (data3.size() > 0) {

					} else {
						flag = "warning:请检查权利信息是否有误";
						return flag;
					}
				}
			}
		}
	return flag;
	}
	 
	@Override
	public String checkData(String casenum, String xmbh) {
		String resultString="true";
		List<Gxjhxm> Gxjhxms =getGXJHXMList(casenum);
		if(Gxjhxms==null||Gxjhxms.size()==0){
			resultString="错误：房产未推送“GXJHXM”数据";
			return resultString;
		}
		System.out.println("项目数据正常");
		try {
			Map<BDCS_QL_GZ, BDCS_FSQL_GZ> rightMap = GetQLData(casenum,xmbh,Gxjhxms);
			if(rightMap.size()==0){
				resultString="错误：房产未推送“权利”数据";
				return resultString;
			}
			System.out.println("权利数据正常");
			// 获取权利人
			List<BDCQLR> QlrList = GetQLR(casenum,Gxjhxms);
//			List<JYQLR> JYQlrList = GetYWR(casenum,Gxjhxms);
			if(QlrList==null||QlrList.size()==0){
				resultString="警告：房产未推送“权利人”数据。   ";
			}
			else {
				String qlidString=QlrList.get(0).getQLID();
				if(qlidString==null||qlidString.equals("")){
					resultString="错误：权利或权利人中缺少QLID。   ";
				}
			}
			System.out.println("权利人数据正常");
			List<JYH> HList = GetH(casenum,Gxjhxms);
			if(HList==null||HList.size()==0){
				resultString=resultString.equals("true")?"":resultString;
				resultString+="错误：房产未推送“户”数据或推送的“竣工时间”字段格式不正确   ";
				return resultString;
			}
			System.out.println("户数据正常");
			//检查relationid
			String sql="";
			for(int i=0;i<HList.size();i++){
				String relationidString=HList.get(i).getRELATIONID();
				if(relationidString==null|| relationidString.equals("")){
					resultString=resultString.equals("true")?"":resultString;
					resultString+="错误：房产未推送“户”表中的relationID字段   ";
				}
				else {
					sql="RELATIONID='"+relationidString+"'";
					String FWZT=GetFWZT(relationidString);
					//判断房屋状态方法改为统一方法 2016年10月27日 09:14:00 卜晓波
					if("2".equals(FWZT)){
//					if("2".equals(HList.get(i).getFWZT())){
						List<BDCS_H_XZ> h_XZs= baseCommonDao.getDataList(BDCS_H_XZ.class,sql);
						if(h_XZs==null||h_XZs.size()==0){
							resultString=resultString.equals("true")?"":resultString;
							resultString+="错误：房产推送的relationID或FWZT值错误，不动产库的现房数据中不存在relationID="+relationidString+"的数据   ";
						}
						else {
							//检查登记单元是否存在
							 sql="BDCDYH ='"+h_XZs.get(0).getBDCDYH()+"'";
							List<BDCS_DJDY_XZ> djdy_XZs= baseCommonDao.getDataList(BDCS_DJDY_XZ.class, sql);
							if(djdy_XZs==null||djdy_XZs.size()==0){
								resultString=resultString.equals("true")?"":resultString;
								resultString+="错误：不动产库中现状登记单元数据不存在   ";
							}
						}
						
					}
					else {
						List<BDCS_H_XZY> h_XZs= baseCommonDao.getDataList(BDCS_H_XZY.class,sql);
						if(h_XZs==null||h_XZs.size()==0){
							List<DCS_H_GZ> dch_gzs= baseCommonDao.getDataList(DCS_H_GZ.class,sql);
							if(dch_gzs==null||dch_gzs.size()==0){
								resultString=resultString.equals("true")?"":resultString;
								resultString+="错误：房产推送的relationID或FWZT值错误，不动产库的期房数据中不存在relationID="+relationidString+"的数据   ";
							}
						}
						else {
							//检查登记单元是否存在
							 sql="BDCDYH ='"+h_XZs.get(0).getBDCDYH()+"'";
							List<BDCS_DJDY_XZ> djdy_XZs= baseCommonDao.getDataList(BDCS_DJDY_XZ.class, sql);
							if(djdy_XZs==null||djdy_XZs.size()==0){
								resultString=resultString.equals("true")?"":resultString;
								resultString+="错误：不动产库中现状登记单元数据不存在   ";
							}
						}
						
					}
				}
			}
			System.out.println("relationid数据正常");
			
			if(resultString.contains("错误")){
				return resultString;
			}
			
			String xmbhcond = ProjectHelper.GetXMBHCondition(xmbh);
			List<BDCS_XMXX> xmxx = baseCommonDao.getDataList(BDCS_XMXX.class,
					xmbhcond);
			String PROJECT_ID = xmxx.get(0).getPROJECT_ID();
			HandlerMapping _mapping = HandlerFactory.getMapping();
			String workflowcode = ProjectHelper
					.getWorkflowCodeByProjectID(PROJECT_ID);
			String _handleClassName = _mapping
					.getHandlerClassName(workflowcode);// "CSDJHandler";
			System.out.println(_handleClassName);
			String qllx = xmxx.get(0).getQLLX();
			// --------------------------预告登记多加的判断，如果已经做过预告的不能再重复做，是选择器的方法-----------------------------------------
						if (_handleClassName.contains("handlerImpl.YGDJHandler")
								|| _handleClassName.contains("handlerImpl.YCDYDJHandler")) {
						String	flag=getNoticeStatus(casenum);
							if(flag.contains("warning")){
								resultString=resultString.equals("true")?"":resultString;
								resultString+="错误：该在办数据已经做过预告登记，不能重复办理";
								return resultString;
							}
						}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return resultString;
	}

	@Override
	public String saveFcywh(String casenum, String xmbh, String bdcdyid, String ly) {
		String flag = "保存失败！";
		//保存功能也得先检查
		String check=CheckCasenum(casenum);
		if(!check.equals("true")){
			flag="保存失败！房产业务号输入格式有误！</br>请检查后重新输入!";
			return flag;
		}
		try {
			BDCS_XMXX xmxx = baseCommonDao.get(BDCS_XMXX.class, xmbh);
			if(xmxx!=null){
				//保存按钮项目信息页面显示房产业务号  同时将抽取的房产业务号保存到当前项目信息表中
				//当一个登记项目因房产系统原因涉及需要多次抽取多个casenum时，按逗号追加保存？那怎么区分单个抽取错误导致多次抽取？   现在先采用最新一个吧 就不注掉了，保存和抽取都一样的处理方式要改记得一起改
				xmxx.setFCYWH(casenum);
				baseCommonDao.update(xmxx);
				List<Wfi_ProInst> proinsts=baseCommonDao.getDataList(Wfi_ProInst.class,"File_Number='"+xmxx.getPROJECT_ID()+"'");
				if(proinsts!=null&&proinsts.size()>0){
					Wfi_ProInst proinst=proinsts.get(0);
					proinst.setYwh(casenum);
					baseCommonDao.update(proinst);
					baseCommonDao.flush();
				}
				if(bdcdyid!=null&&ly!=null){
					List<BDCS_DJDY_GZ> djdy_GZs=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "bdcdyid='"+bdcdyid+"'");
					if(djdy_GZs!=null&&djdy_GZs.size()>0){
						List<BDCS_QL_GZ> ql_GZs=baseCommonDao.getDataList(BDCS_QL_GZ.class, "DJDYID='"+djdy_GZs.get(0).getDJDYID()+"'");
						if(ql_GZs!=null&&ql_GZs.size()>0){
							for(BDCS_QL_GZ ql_GZ:ql_GZs){
								ql_GZ.setCASENUM(casenum);
								baseCommonDao.update(ql_GZ);
							}
							flag="true";
						}
					}
				}
				//这种方式无法处理多户对应多个casenum的情况，会导致全部被最新的覆盖问题
//				List<BDCS_QL_GZ> ql_GZs=baseCommonDao.getDataList(BDCS_QL_GZ.class, "XMBH='"+xmbh+"'");
//				List<BDCS_QL_GZ> ql_GZs=baseCommonDao.getDataList(BDCS_QL_GZ.class, "XMBH='"+xmbh+"'");
//				if(ql_GZs!=null&&ql_GZs.size()>0){
//					for(BDCS_QL_GZ ql_GZ:ql_GZs){
//						ql_GZ.setCASENUM(casenum);
//						baseCommonDao.update(ql_GZ);
//					}
//					flag="true";
//				}else{
//					flag="保存失败！</br>当前未选择单元，请选择单元后再保存房产业务号！";
//				}
			}
		}catch(Exception e){
			flag="保存失败！</br>请联系管理员！";
		}
		return flag;
	}
	private String CheckCasenum(String casenum) {
		String flag="true";
		String xzqhdm=ConfigHelper.getNameByValue("XZQHDM");
		if(xzqhdm.equals("650100")&&casenum!=null){
			if(casenum.length()!="16102120-170201-1652571".length()||!casenum.substring(8,9).equals("-")||!casenum.substring(15,16).equals("-")){
				flag="formalerror";
				return flag;
			}
		}
		return flag;
	}
	/**
	 * 根据relationid或者bdcdyh判断房屋状态通用方法
	 * @author buxiaobo
	 * @param relationidorbdcdyh
	 * @time 2016年10月26日 09:45:41
	 * @return FWZT检查不到返回为null，存在则返回1期房 2现房
	 * */
	public String GetFWZT(String relationidorbdcdyh){
		String FWZT="0";
		String hql="RELATIONID = '"+relationidorbdcdyh+"'";
		List<BDCS_H_XZ> h_xzlist=baseCommonDao.getDataList(BDCS_H_XZ.class, hql);
		if(h_xzlist!=null && h_xzlist.size()>0){
			return FWZT="2";
		}else{
			hql="BDCDYH = '"+relationidorbdcdyh+"'";
			h_xzlist=baseCommonDao.getDataList(BDCS_H_XZ.class, hql);
			if(h_xzlist!=null && h_xzlist.size()>0){
				return FWZT="2";
			}else{
				String hql1="RELATIONID = '"+relationidorbdcdyh+"'";
				List<BDCS_H_XZY> h_xzylist=baseCommonDao.getDataList(BDCS_H_XZY.class, hql1);
				if(h_xzylist!=null && h_xzylist.size()>0){
					return FWZT="1";
				}else{
					hql1="BDCDYH = '"+relationidorbdcdyh+"'";
					h_xzylist=baseCommonDao.getDataList(BDCS_H_XZY.class, hql1);
					if(h_xzylist!=null && h_xzylist.size()>0){
						return FWZT="1";
					}else{
						//初始登记时去不动产调查库中检查单元是否存在
						String hql2="relationid = '"+relationidorbdcdyh+"'";
						List<DCS_H_GZ> dcs_H_GZs=baseCommonDao.getDataList(DCS_H_GZ.class, hql2);
						if(dcs_H_GZs!=null&&dcs_H_GZs.size()>0){
							return FWZT="2";
						}else{
							List<DCS_H_GZY> dcs_H_GZYs=baseCommonDao.getDataList(DCS_H_GZY.class, hql2);
							if(dcs_H_GZYs!=null&&dcs_H_GZYs.size()>0){
								return FWZT="1";
							}else{
								return null;
							}
						}
						
					}
				}
			}
		}
	}

	/**
	 * 读取地籍合同 [直接调用地籍系统抽取webservice2]
	 * 读取地籍合同 【根据项目权利类型判断地籍一二级市场】
	 * @作者 buxiaobo
	 * @创建时间 2016年11月8日 15:13:20
	 * @param xmbh
	 * @param TDCasenum
	 * @return
	 * */
	@SuppressWarnings("deprecation")
	@Override
	public String ExtractSXFromDJ(String xmbh,String TDCasenum) {
		String flag="false";
//		String djinterfaceurl = ConfigHelper.getNameByValue("URL_DJGETDATA") +TDCasenum;// "http://localhost:10451/LRSDJWeb/WebService.asmx/GetZDRelationid?zddh="+030050050281000;
//		//测试用，需增加不动产库基本配置升级脚本
//		djinterfaceurl="http://localhost:10451/LRSDJWeb/WebService.asmx/GetZDRelationid?xmbh=2016-220204-1501-00260";
//        StringBuilder json = new StringBuilder();
		System.out.println("开始调地籍接口...");
		flag = sharetool.BDCKPushIsRK(TDCasenum);
		String regEx="[^0-9]";
		Pattern p = Pattern.compile(regEx);   
		Matcher m = p.matcher(flag);
		flag = m.replaceAll("").trim();
//		String IsRKInterface=ConfigHelper.getNameByValue("IsRKInterface");
//		URL djreturnurl = null;
//		try {
//			djreturnurl = new URL(IsRKInterface);
//		} catch (MalformedURLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		//flag = readContentFromPost(djreturnurl,TDCasenum);
		if(flag.equals("1081")||flag=="1081"){
			try {
	        	List<BDCS_QL_GZ> qls=baseCommonDao.getDataList(BDCS_QL_GZ.class,"xmbh='"+xmbh+"'");
				if(qls!=null&&qls.size()>0){
					for(int i=0;i<qls.size();i++){
						BDCS_QL_GZ ql=qls.get(i);
						ql.setTDCASENUM(TDCasenum);
						baseCommonDao.update(ql);
					}
					flag="true";
				}else{
					flag="true";
				}
//	        	//此处调用房产webservice获取json数据
//	            URL url = new URL(djinterfaceurl);
//	            URLConnection urlcon = url.openConnection();
//	            BufferedReader bf = new BufferedReader(new InputStreamReader(urlcon.getInputStream(),"gb2312"));
//	            String inputLine = null;
//	            inputLine = bf.readLine();
//	            /*************************抽取测试地籍登簿接口用************************************************/
////	          String share_url = ConfigHelper.getNameByValue("URL_SHARE");
////				URL url1 = new URL(share_url + "BDCKPushToDJK/" + xmbh );
////				URLConnection urlcon1 = url1.openConnection();
////				BufferedReader bf1 = new BufferedReader(new InputStreamReader(urlcon1.getInputStream(), "gb2312"));
////				String inputLine1 = null;
////				inputLine1 = bf1.readLine();
//				/****************************下面的地籍权利人、义务人、权利在吉林受理时都已经填好了不再从地籍抽取【别的准备对接地籍的地方：1、一级市场：不走房产需要抽取地籍 2、二级市场走房产的就直接用抽取房产的义务人权利人就行不用重抽地籍的了】*****************************************************/
//	            if ( inputLine != null) {
//	                json.append(inputLine);
//	                JSONObject jsonObj = JSONObject.fromObject(json.toString());
//	                JSONArray qlArray= jsonObj.getJSONArray("QL");//List<Map<String,String>>
//	                JSONArray qlrArray= jsonObj.getJSONArray("QLR");
//	                String xzqdm =ConfigHelper.getNameByValue("XZQHDM");
//	                //吉林地籍因为受理都已经填完义务人权利人权利信息了，涉及使用权宗地的权籍都维护完了，剩下地籍只走转移的业务了，抽取时只更新对应的户现状信息并存储TDCasenum即可
//	                
//	                if(xzqdm!=null && xzqdm.contains("2202")){
////	                	BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
////	        			if(bdcdylx.equals("3") || bdcdylx.equals("5") || bdcdylx.equals("7")){
////	        				flag=updateSHYQZDandQL(TDCasenum,xmbh,qlArray);
////	        			}
//	                	List<Map<String,String>> qllist=new ArrayList<Map<String,String>>();
//	        			for(int i=0;i<qlArray.size();i++){
//	        	        	String qlArraystr=qlArray.getString(i);
//	        	        	ObjectMapper mapper = new ObjectMapper();
//	        	        	 Map<String, String> map = mapper.readValue(qlArraystr, Map.class);
//	        	        	 qllist.add(map);
//	        	        }
//	        			//实际办理地籍都是一户一户的补交出让金，房产也是一户一户办理，此处按单户维护，涉及多户时不更新维护
//	        			if(qllist!=null && qllist.size()==1){
//	        				flag=updateHandQL(TDCasenum,xmbh,qllist.get(0));
//	        				return flag;
//	        			}else{
//	        				return flag;
//	        			}
//	                }else{
//	                	// 从地籍JSON串中取得权利、附属权利
//	        			Map<BDCS_QL_GZ, BDCS_FSQL_GZ> rightMap = GetTDQLData(TDCasenum,xmbh,qlArray);
//	        			List<BDCS_QL_GZ> qlList = new ArrayList<BDCS_QL_GZ>();
//	        			List<BDCS_FSQL_GZ> fsqlList = new ArrayList<BDCS_FSQL_GZ>();
//	        			if (rightMap.size() > 0) {
//	        				for (Map.Entry<BDCS_QL_GZ, BDCS_FSQL_GZ> entry : rightMap
//	        						.entrySet()) {
//	        					qlList .add( entry.getKey());
//	        					fsqlList.add( entry.getValue());
//	        				}
//	        			}
//	                    // 业务类型判断
//	        			String xmbhcond = ProjectHelper.GetXMBHCondition(xmbh);
//	        			List<BDCS_XMXX> xmxx = baseCommonDao.getDataList(BDCS_XMXX.class,xmbhcond);
//	                    List<String> bdcdyhList=new ArrayList<String>();
//	        			bdcdyhList =GetTDRealtionIDList(qlArray);
////	        			// 获取权利人，同时把义务人获取并拼接完成
//	        			List<BDCQLR> QlrList = GetTDQLR(TDCasenum,qlrArray);
//	        			//获取义务人
//	        			List<JYQLR> JYQlrList = GetTDYWR(TDCasenum,qlrArray);
//	        			String PROJECT_ID = xmxx.get(0).getPROJECT_ID();
//	        			HandlerMapping _mapping = HandlerFactory.getMapping();
//	        			String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(PROJECT_ID);
//	        			String _handleClassName = _mapping.getHandlerClassName(workflowcode);
//	                    //如果是抵押变更、所有权变更、更正登记、初始登记
//	        			if (_handleClassName.contains("BGDJHandler")) {
//	        				// 获取在办数据的RealtionID list
//	        				if(bdcdyhList==null || bdcdyhList.size()<1){
//	        					flag = "nofwztorh";
//	        					return flag;
//	        				}
//	        				if (bdcdyhList.isEmpty() || bdcdyhList == null) {
//	        					flag = "warning";
//	        					return flag;
//	        				}
//	        				// 变更、更正、初始登记的单元信息是直接作为工作层，故在此传取单元list
//	        				flag = insertDataService.InsertSXFromTDBG(qlList.get(0), fsqlList.get(0), xmbh, TDCasenum, QlrList, JYQlrList,bdcdyhList);
//	        				return flag;
//	        			}
//	        			else {
//	        				//其他登记业务
//	        				// 获取在办数据的RealtionID list
//	    					if (bdcdyhList.get(0).equals("nofwzt")) {
//	    						flag = "nofwztorh";
//	    						return flag;
//	    					}
//	        				if (bdcdyhList.isEmpty() || bdcdyhList == null) {
//	        					flag = "warning";
//	        					return flag;
//	        				}
	//
//	        				// 因除变更、更正、初始登记外的单元信息是从现状层拷贝到工作层
//	        				flag = insertDataService.InsertSXFromTD(qlList, fsqlList, xmbh,TDCasenum, QlrList, JYQlrList, bdcdyhList);
//	        				return flag;
//	        			}
//	                }
//	            }else{
//	            	 flag="false";
//	            	 System.out.println(flag);
//	                 return flag;
//	            }
				return flag;
	        }catch (Exception e) {
	        	flag="false:"+e.toString();
	        	return flag;
	        }
		}else{
			flag="地籍号不存在或未办结";
		}
		return flag;		
	}
	

	//地籍抽取更新户和权利
	@SuppressWarnings("deprecation")
	private String updateHandQL(String tDCasenum, String xmbh, Map<String, String> map) {
		String flag="";
		List<BDCS_DJDY_GZ> djdylist=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH = '"+xmbh+"'");
		if(djdylist!=null && djdylist.size()>0){
			String bdcdyid =djdylist.get(0).getBDCDYID();
			try{
				Double FTTDMJ=Double.valueOf(map.get("FTTDMJ"));
				Double DYMJ=Double.valueOf(map.get("DYMJ"));
				Double GYMJ=Double.valueOf(map.get("GYMJ"));
				String FWTDYT=map.get("PZYT");
				String SYQLX=map.get("SYQLX");
	            int syqx = Integer.valueOf(map.get("SYQX"));
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date zzrq = sdf.parse(map.get("ZSZFRQ"));
	            String sql = "update bdcs_h_xz set  ";
	            if (FTTDMJ > 0)
	            {
	                sql += "FTTDMJ = " + FTTDMJ + ",";
	            }
	            if (DYMJ > 0)
	            {
	                sql += "DYTDMJ = " + DYMJ + ",";
	            }
	            if (GYMJ > 0)
	            {
	                sql += "GYTDMJ = " + GYMJ + ",";
	            }
	            if (FWTDYT!=null && !FWTDYT.equals(""))
	            {
	                sql += "FWTDYT = '" + FWTDYT + "',";
	            }
	            if (SYQLX!=null && !SYQLX.equals(""))
	            {
	                sql += "QLXZ = '" + SYQLX + "',";
	            }
	            if (syqx > 0)
	            {
	                sql += "TDSYNX = " + syqx + ",";
	            }
	            Calendar cld = Calendar.getInstance();
	            cld.setTime(zzrq);
	            if (cld.get(Calendar.YEAR) > 1910)
	            {
	                if (syqx > 0)
	                {
	                	 Calendar rightNow = Calendar.getInstance();
	                     rightNow.setTime(zzrq);
	                     rightNow.add(Calendar.YEAR,-syqx);
	                     Date qsrq=rightNow.getTime();
	                     rightNow.setTime(qsrq);
	                     rightNow.add(Calendar.DAY_OF_YEAR,1);
	                     qsrq=rightNow.getTime();
	                    sql += "TDSYQQSRQ = to_date('" + qsrq.toString() + "','yyyy-mm-dd'),";
	                }
	                sql += "TDSYQZZRQ = to_date('" + zzrq.toString() + "','yyyy-mm-dd'),";
	            }
	            sql = sql.substring(0, sql.length()-1)+ " where bdcdyid = '" + bdcdyid + "'";
	            baseCommonDao.excuteQueryNoResult(sql);
	            List<BDCS_QL_GZ> qls=baseCommonDao.getDataList(BDCS_QL_GZ.class,"xmbh='"+xmbh+"'");
				if(qls!=null&&qls.size()>0){
					BDCS_QL_GZ ql=qls.get(0);
					ql.setTDCASENUM(tDCasenum);
					baseCommonDao.update(ql);
				}
				flag="true";
			}catch(Exception e){
				logger.error(e.toString()+"地籍抽取出错，项目编号："+xmbh);
				flag="false";
			}
		}
		return flag;
	}

	private Map<BDCS_QL_GZ, BDCS_FSQL_GZ> GetTDQLData(String tDCasenum,String xmbh, JSONArray qlArray) {
		Map<BDCS_QL_GZ, BDCS_FSQL_GZ> rightMap = new HashMap<BDCS_QL_GZ, BDCS_FSQL_GZ>();
		try{
			BDCS_QL_GZ ql = new BDCS_QL_GZ();
			BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
			String xmbhcond = ProjectHelper.GetXMBHCondition(xmbh);
			List<BDCS_XMXX> xmxx = baseCommonDao.getDataList(BDCS_XMXX.class,xmbhcond);
			List<Map<String,String>> qllist=new ArrayList<Map<String,String>>();
			for(int i=0;i<qlArray.size();i++){
	        	String qlArraystr=qlArray.getString(i);
	        	ObjectMapper mapper = new ObjectMapper();
	        	 Map<String, String> map = mapper.readValue(qlArraystr, Map.class);
	        	 qllist.add(map);
	        }
			for(int i=0;i<qllist.size();i++){
				Map<String,String> qlmap=qllist.get(i);
				ql.setId(Common.CreatUUID());
				List<Map> shyqzd_gz=baseCommonDao.getDataListByFullSql("SELECT * FROM BDCDCK.BDCS_SHYQZD_GZ WHERE BDCDYID='"+qlmap.get("ZDID")+"'");
				if(shyqzd_gz!=null && shyqzd_gz.size()>0){
					ql.setBDCDYH(shyqzd_gz.get(0).get("BDCDCH").toString());
				}
				ql.setQXDM(qlmap.get("QXDM"));
				ql.setDJLX(xmxx.get(0).getDJLX());
				ql.setQLLX(xmxx.get(0).getQLLX());
				ql.setTDZSRELATIONID(qlmap.get("ZDQLRID"));	
				ql.setCASENUM(tDCasenum);
				fsql.setCASENUM(tDCasenum);
				fsql.setQLID(ql.getId());
				fsql.setZL(qlmap.get("TDZL"));
				fsql.setZH(qlmap.get("TDZH"));
				fsql.setJZMJ(Double.valueOf(qlmap.get("JZMJ")));
				fsql.setFTJZMJ(Double.valueOf(qlmap.get("FTJZMJ")));
				fsql.setFTTDMJ(Double.valueOf(qlmap.get("FTTDMJ")));;
				}
			rightMap.put(ql, fsql);
		}catch(Exception e){
			
		}
		return rightMap;
	}
	
	private List<String> GetTDRealtionIDList(JSONArray qlArray) {
		List<String> bdcdyhList=new ArrayList<String>();
		try{
			for(int i=0;i<qlArray.size();i++){
	        	String qlArraystr=qlArray.getString(i);
	        	ObjectMapper mapper = new ObjectMapper();
	        	 Map<String, String> map = mapper.readValue(qlArraystr, Map.class);
	        	 String curzdid=map.get("ZDID");
	        	 if(bdcdyhList.indexOf(curzdid)<0){
	        		 bdcdyhList.add(curzdid);
	        	 }
	        }
		}catch(Exception e){
			
		}
		return bdcdyhList;
	}
	
	private List<BDCQLR> GetTDQLR(String tDCasenum, JSONArray qlrArray) {
		List<BDCQLR> Qlrs = new ArrayList<BDCQLR>();
		try{
			for(int i=0;i<qlrArray.size();i++){
				BDCQLR qlr=new BDCQLR();
	        	String qlrArraystr=qlrArray.getString(i);
	        	ObjectMapper mapper = new ObjectMapper();
	        	 Map<String, String> map = mapper.readValue(qlrArraystr, Map.class);
	        	 String sqllb=map.get("SQRLB");
	        	 if(sqllb!=null && sqllb.equals("1")){
	        		 //权利人
	        		 qlr.setQLRID(Common.CreatUUID());
	        		 qlr.setBDCQZH(map.get("TDZH"));
	        		 qlr.setQLRMC(map.get("QLRMC"));
	        		 qlr.setZJZL(map.get("ZJZL"));
	        		 qlr.setZJH(map.get("ZJH"));
	        		 Qlrs.add(qlr);
	        	 }else if(sqllb!=null && sqllb.equals("2")){
	        		 String curywr=map.get("QLRMC");
	        		 if(curywr!=null){
	        			 ywrs+=curywr+",";
	        		 }	     			
	        	 }else{
	        		 continue;
	        	 }
	        }
			//拼接全局义务人
			ywrs=ywrs.substring(0, ywrs.length()-1);
		}catch(Exception e){
			
		}
		return Qlrs;
	}

	private List<JYQLR> GetTDYWR(String tDCasenum, JSONArray qlrArray) {
		List<JYQLR> JYQlrs = new ArrayList<JYQLR>();
		try{
			for(int i=0;i<qlrArray.size();i++){
				JYQLR jyqlr=new JYQLR();
	        	String qlrArraystr=qlrArray.getString(i);
	        	ObjectMapper mapper = new ObjectMapper();
	        	 Map<String, String> map = mapper.readValue(qlrArraystr, Map.class);
	        	 String sqllb=map.get("SQRLB");
	        	 if(sqllb!=null && sqllb.equals("2")){
	        		 //义务人
	        		 jyqlr.setQLRID(Common.CreatUUID());
	        		 jyqlr.setBDCQZH(map.get("TDZH"));
	        		 jyqlr.setQLRMC(map.get("QLRMC"));
	        		 jyqlr.setZJZL(map.get("ZJZL"));
	        		 jyqlr.setZJH(map.get("ZJH"));
	        		 JYQlrs.add(jyqlr);
	        	 }else{
	        		 continue;
	        	 }
	        }
			//拼接全局义务人
			ywrs=ywrs.substring(0, ywrs.length()-1);
		}catch(Exception e){
			
		}
		return JYQlrs;
	}

	@Override
	public String getFcStatus(String casenum) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String ExtractSXFromZJKS(String ywh, String xmbh, boolean bool,
			String projectid, String djyyString) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean ExtractFJFromZJKS(String proinstid, String casenum,
			String file_Path) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
