package com.supermap.realestate.registration.service.impl.share;

import java.io.InputStream;
import java.net.InetAddress;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.xmlmodel.BDCQLR;
import com.supermap.realestate.registration.service.InsertDataService;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.JH_DBHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.ConstValue.GYFS;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProInstService;

@Service("extractDataForSJZService")
public class ExtractDataForSJZServiceImpl extends ExtractDataServiceImpl {
	private static final Log logger = LogFactory.getLog(ExtractDataForSJZServiceImpl.class); 
	@Autowired
	private InsertDataService insertDataService;
	@Autowired
	private SmProInstService smProInstService;
	@Autowired
	private com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao;

	@Override
	public String ExtractSXFromZJK(String casenum, String xmbh, boolean bool) {
		String flag = "false";
		Connection connection = null;
		try {
//			logger.info("开始抽取属性。。。");
			connection = JH_DBHelper.getConnect_jy();
			// 从房产库中获取他项权利表中其他必要信息
			Map<BDCS_QL_GZ, BDCS_FSQL_GZ> rightMap = GetQLData(casenum,
					connection);
			BDCS_QL_GZ ql = null;
			BDCS_FSQL_GZ fsql = null;
			if (rightMap.size() > 0) {
				for (Map.Entry<BDCS_QL_GZ, BDCS_FSQL_GZ> entry : rightMap
						.entrySet()) {
					ql = entry.getKey();
					fsql = entry.getValue();
				}
			}
			// 从房产库中获取权利人信息
			// 获取抵押权利人
			Map<String, List<BDCQLR>> dyQlrList = new HashMap<String, List<BDCQLR>>();
			dyQlrList.put("0", GetDYBDCQLR(casenum, connection));
			// 获取转移权利人
			Map<String, List<BDCQLR>> zyQlrList = new HashMap<String, List<BDCQLR>>();
			zyQlrList = GetSCZYBDCQLR(casenum, connection);
			String sql1 = "select HOUSEID from house.pro_houseinfo where CASENUM ='"
					+ casenum + "'";
			ResultSet resultSet1 = null;
			PreparedStatement pstmt = connection.prepareStatement(sql1);
			 resultSet1 = pstmt.executeQuery();
//			resultSet1 = JH_DBHelper.excuteQuery(connection, sql1);
//			logger.info("获取houseid。。。");
			List<String> bdcdyhList = new ArrayList<String>();
			while (resultSet1.next()) {
				long houseid = resultSet1.getLong("HOUSEID");
				String sql2 = "select BDCDYH from HOUSEPROPERTY.REL_HOUSE_REALESTATE where HOUSEID ='"
						+ houseid + "'";
				PreparedStatement pstmt2 = connection.prepareStatement(sql2);
				ResultSet resultSet2 = pstmt2.executeQuery();
//				ResultSet resultSet2 = JH_DBHelper.excuteQuery(connection, sql2);
				if (!resultSet2.next()) {
					flag = "warning";
					return flag;
				}
				String bdcdyh = resultSet2.getString("BDCDYH");
				bdcdyhList.add(bdcdyh);
//				logger.info("获取到不动单元号："+bdcdyh);
				pstmt2.close();
				if(resultSet2!=null){
					resultSet2.close();
				}
				
			}
			pstmt.close();
			if(resultSet1!=null){
				resultSet1.close();
			}
			flag = insertDataService.InsertSXFromZJK(ql, fsql, xmbh, casenum,
					dyQlrList, zyQlrList, bdcdyhList, bool);
			return flag;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {

		}
		return flag;
	}

	@Override
	public String AddDYBDCDY(String ywh, String xmbh) {
		String flag = "false";
		// 从房产库中获取权利人信息
		flag = ExtractSXFromZJK(ywh, xmbh, false);
		return flag;
	}

	// 抵押通过casenum，从房产系统中获取权利人的相关信息
	private List<BDCQLR> GetDYBDCQLR(String casenum, Connection connection)
			throws SQLException {
		List<BDCQLR> dyqlrlist = new ArrayList<BDCQLR>();
		// 抵押权利人信息OPR_LIENERINFO
		String DYQRsql = "select * from HOUSE.OPR_LIENERINFO where casenum ='"
				+ casenum + "'";
		PreparedStatement pstmt = connection.prepareStatement(DYQRsql);
		ResultSet  DYQRRSet= pstmt.executeQuery();
//		ResultSet DYQRRSet = JH_DBHelper.excuteQuery(connection, DYQRsql);
		//名称相同的权利人不重复添加
		List<String> qlrmcList=new ArrayList<String>();
		if(DYQRRSet!=null){
		while (DYQRRSet.next()) {
			String mc=DYQRRSet.getString("LIENER");
			if(!qlrmcList.contains(mc)){
				qlrmcList.add(mc);
				BDCQLR qlr = new BDCQLR();
				qlr.setQLRMC(DYQRRSet.getString("LIENER"));// 权利人名称
				qlr.setFDDBR(DYQRRSet.getString("MANAGERNAME"));// 法定代表人
				qlr.setDZ(DYQRRSet.getString("ORGOWNERADDRESS"));// 地址
				qlr.setSSHY(DYQRRSet.getString("ORGOWNERSIC"));// 所属行业
				qlr.setDH(DYQRRSet.getString("TEL"));// 电话
				qlr.setZJH(DYQRRSet.getString("LIENERIDNUM"));// 证件号码
				qlr.setZJZL("7");// 证件种类
				qlr.setQLRLX("2");
				dyqlrlist.add(qlr);
			}
		}
		pstmt.close();
		DYQRRSet.close();
		}
		return dyqlrlist;
	}

	// 【转移业务】（多个）【权利人从OPR_OWNERINFO业务_所有权人信息表、OPR_OTHEROWNER业务_共有权人信息表】两种业务取数据
	private Map<String, List<BDCQLR>> GetSCZYBDCQLR(String casenum,
			Connection connection) throws SQLException {
		Map<String, List<BDCQLR>> qlrMap = new HashMap<String, List<BDCQLR>>();
		List<BDCQLR> zyqlrlist = new ArrayList<BDCQLR>();
		List<BDCQLR> zyydlrlist = null;// 转移义务人代理人
		// 所有权利人
		String SYQRsql = "select * from HOUSE.OPR_OWNERINFO where casenum ='"
				+ casenum + "'";
		PreparedStatement pstmt = connection.prepareStatement(SYQRsql);
		ResultSet SYRRSet = pstmt.executeQuery();
//		ResultSet SYRRSet = JH_DBHelper.excuteQuery(connection, SYQRsql);// 所有权利人信息结果集
		if (SYRRSet.next()) {
			BDCQLR syqr = new BDCQLR();
			BDCQLR ydlr = new BDCQLR(); // 原代理人
			zyydlrlist = new ArrayList<BDCQLR>();
			syqr.setDLRXM(SYRRSet.getString("AGENTNAME"));// 代理人姓名
			syqr.setDLRLXDH(SYRRSet.getString("AGENTTEL"));// 代理人联系电话
			syqr.setDLRZJHM(SYRRSet.getString("AGENTIDNUM"));// 代理人证件号码
			String dlrZjzl = SYRRSet.getString("AGENTIDTYPE");// 代理人证件类型
			if (dlrZjzl != null) {
				dlrZjzl = dlrZjzl.trim();
				if (dlrZjzl.equals("0101")) {
					dlrZjzl = "1";
				} else if (dlrZjzl.equals("0107")) {
					dlrZjzl = "3";
				} else if (dlrZjzl.equals("0102")) {
					dlrZjzl = "4";
				} else if (dlrZjzl.equals("0104")) {
					dlrZjzl = "5";
				} else if (dlrZjzl.equals("0106")) {
					dlrZjzl = "5";
				} else {
					dlrZjzl = "99";
				}
				syqr.setDLRZJLX(dlrZjzl);// 证件种类
			}
			ydlr.setDLRXM(SYRRSet.getString("AGENTCOOUPATION"));// 原代理人姓名
			ydlr.setDLRLXDH(SYRRSet.getString("AGENTMAIL"));// 原代理人联系电话
			ydlr.setDLRZJHM(SYRRSet.getString("AGENTFAX"));// 原代理人证件号码
			String ydlrZjzl = SYRRSet.getString("AGENTTYPE");// 原代理人证件类型
			if (dlrZjzl != null) {
				dlrZjzl = dlrZjzl.trim();
				if (dlrZjzl.equals("0101")) {
					dlrZjzl = "1";
				} else if (dlrZjzl.equals("0107")) {
					dlrZjzl = "3";
				} else if (dlrZjzl.equals("0102")) {
					dlrZjzl = "4";
				} else if (dlrZjzl.equals("0104")) {
					dlrZjzl = "5";
				} else if (dlrZjzl.equals("0106")) {
					dlrZjzl = "5";
				} else {
					dlrZjzl = "99";
				}
				ydlr.setDLRZJLX(ydlrZjzl);// 证件种类
			}
			zyydlrlist.add(ydlr);
			syqr.setGYFS(GYFS.DYSY.Value);// 共有方式
			syqr.setGZDW(SYRRSet.getString("OWNERCOMPANY"));// 工作单位
			String syqrxb = SYRRSet.getString("OWNERSEX");
			if (syqrxb != null) {
				syqrxb = syqrxb.trim();
				if (syqrxb.equals("0")) {
					syqrxb = "1";
				} else if (syqrxb.equals("1")) {
					syqrxb = "2";
				} else {
					syqrxb = "3";
				}
				syqr.setXB(syqrxb);// 性别
			}
			syqr.setQLRMC(SYRRSet.getString("OWNERNAME"));// 权利人名称
			syqr.setDZ(SYRRSet.getString("OWNERADD"));// 地址
			syqr.setDH(SYRRSet.getString("OWNERTEL"));// 电话
			syqr.setZJH(SYRRSet.getString("OWNERIDNUM"));// 证件号
			String ZJZL = SYRRSet.getString("OWNERIDTYPE");
			if (ZJZL != null) {
				ZJZL = ZJZL.trim();
				if (ZJZL.equals("0101")) {
					ZJZL = "1";
				} else if (ZJZL.equals("0107")) {
					ZJZL = "3";
				} else if (ZJZL.equals("0102")) {
					ZJZL = "4";
				} else if (ZJZL.equals("0104")) {
					ZJZL = "5";
				} else if (ZJZL.equals("0106")) {
					ZJZL = "5";
				} else {
					ZJZL = "99";
				}
				syqr.setZJZL(ZJZL);// 证件种类
			}
			syqr.setISCZR(SF.YES.Value);
			syqr.setQLRLX("1");
			// 共有权利人
			String GYQRsql = "select * from HOUSE.OPR_OTHEROWNER where casenum ='"
					+ casenum + "'";
			PreparedStatement pstmt3 = connection.prepareStatement(GYQRsql);
			ResultSet GYRRSet = pstmt3.executeQuery();
//			ResultSet GYRRSet = JH_DBHelper.excuteQuery(connection, GYQRsql);// 共有权利人信息结果集
			while (GYRRSet.next()) {
				BDCQLR gyqr = new BDCQLR();
				syqr.setGYFS(GYFS.GTGY.Value);// 共有方式
				gyqr.setDLRXM(SYRRSet.getString("AGENTNAME"));// 代理人姓名
				gyqr.setDLRLXDH(SYRRSet.getString("AGENTTEL"));// 代理人联系电话
				gyqr.setDLRZJHM(SYRRSet.getString("AGENTIDNUM"));// 代理人证件号码
				gyqr.setDLRZJLX(dlrZjzl);// 证件种类
				// 权利人=1人 单独所有0、权利人>=2 共同共有1
				gyqr.setGYFS(GYFS.GTGY.Value);// 共有方式
				gyqr.setQLRMC(GYRRSet.getString("OTHEROWNERNAME"));// 权利人名称
				gyqr.setDZ(GYRRSet.getString("OTHEROWNERADDRESS"));// 地址
				gyqr.setDH(GYRRSet.getString("OTHEROWNERTEL"));// 电话
				String gyrZjzl = GYRRSet.getString("OTHEROWNERIDTYPE");// 证件种类
				if (gyrZjzl != null) {
					gyrZjzl = gyrZjzl.trim();
					if (gyrZjzl.equals("0101")) {
						gyrZjzl = "1";
					} else if (gyrZjzl.equals("0107")) {
						gyrZjzl = "3";
					} else if (gyrZjzl.equals("0102")) {
						gyrZjzl = "4";
					} else if (gyrZjzl.equals("0104")) {
						gyrZjzl = "5";
					} else if (gyrZjzl.equals("0106")) {
						gyrZjzl = "5";
					} else {
						gyrZjzl = "99";
					}
					gyqr.setZJZL(gyrZjzl);// 证件种类
				}
				gyqr.setZJH(GYRRSet.getString("OTHEROWNERIDNUM"));// 证件号
				String gyqrxb = GYRRSet.getString("OTHEROWNERSEX");
				if (gyqrxb != null) {
					gyqrxb = gyqrxb.trim();
					if (gyqrxb.equals("0")) {
						gyqrxb = "1";
					} else if (gyqrxb.equals("1")) {
						gyqrxb = "2";
					} else {
						gyqrxb = "3";
					}
					gyqr.setXB(gyqrxb);// 性别
				}
				gyqr.setISCZR(SF.YES.Value);
				gyqr.setQLRLX("1");
				zyqlrlist.add(gyqr);
			}
			pstmt3.close();
			GYRRSet.close();
			zyqlrlist.add(syqr);
			pstmt.close();
			SYRRSet.close();
		} else {
			String DWCsql = "select * from HOUSE.opr_representative where casenum ='"
					+ casenum + "'";
			PreparedStatement pstmt2 = connection.prepareStatement(DWCsql);
			ResultSet DWCRSet = pstmt2.executeQuery();
//			ResultSet DWCRSet = JH_DBHelper.excuteQuery(connection, DWCsql);// 所有权利人信息结果集
			if (DWCRSet.next()) {
				BDCQLR syqr = new BDCQLR();
				syqr.setGYFS(GYFS.DYSY.Value);// 共有方式
				syqr.setQLRMC(DWCRSet.getString("OWNERCOMPANY"));// 权利人名称
				syqr.setDZ(DWCRSet.getString("COMPANYADDRESS"));// 地址
				syqr.setDH(DWCRSet.getString("COMPANYTEL"));// 电话
				syqr.setDLRXM(DWCRSet.getString("CLERK"));// 代理人姓名
				syqr.setDLRLXDH(DWCRSet.getString("CLERKPHONE"));// 代理人联系电话
				syqr.setDLRZJHM(DWCRSet.getString("CLERKCARDNUM"));// 代理人证件号码
				String dlrZjzl = DWCRSet.getString("CLERKCARDTYPE");// 代理人证件类型
				if (dlrZjzl != null) {
					dlrZjzl = dlrZjzl.trim();
					if (dlrZjzl.equals("0101")) {
						dlrZjzl = "1";
					} else if (dlrZjzl.equals("0107")) {
						dlrZjzl = "3";
					} else if (dlrZjzl.equals("0102")) {
						dlrZjzl = "4";
					} else if (dlrZjzl.equals("0104")) {
						dlrZjzl = "5";
					} else if (dlrZjzl.equals("0106")) {
						dlrZjzl = "5";
					} else {
						dlrZjzl = "";
					}
					syqr.setDLRZJLX(dlrZjzl);// 证件种类
				}
				syqr.setISCZR(SF.YES.Value);
				syqr.setQLRLX("1");
				zyqlrlist.add(syqr);
			}
			pstmt2.close();
			DWCRSet.close();
		}
		qlrMap.put("0", zyqlrlist);
		qlrMap.put("1", zyydlrlist);
		return qlrMap;
	}

	// 从房产库中获取他项权利表中其他必要信息
	private Map<BDCS_QL_GZ, BDCS_FSQL_GZ> GetQLData(String casenum,
			Connection connection) throws SQLException {
		Map<BDCS_QL_GZ, BDCS_FSQL_GZ> rightMap = new HashMap<BDCS_QL_GZ, BDCS_FSQL_GZ>();
		BDCS_QL_GZ ql = new BDCS_QL_GZ();
		BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
		String otheritemSql = "select * from HOUSE.OPR_OTHERITEM where casenum ='"
				+ casenum + "'";
		PreparedStatement pstmt = connection.prepareStatement(otheritemSql);
		ResultSet otheritemRSet = pstmt.executeQuery();
//		ResultSet otheritemRSet = JH_DBHelper.excuteQuery(connection,otheritemSql);
		if (otheritemRSet.next()) {
			String SYQRsql = "select * from HOUSE.OPR_OWNERINFO where casenum ='"
					+ casenum + "'";
			String GYQRsql = "select * from HOUSE.OPR_OTHEROWNER where casenum ='"
					+ casenum + "'";
			String DWCRsql = "select * from house.opr_representative where casenum ='"
					+ casenum + "'";
			PreparedStatement pstmt2 = connection.prepareStatement(SYQRsql);
			ResultSet SYQRSet = pstmt2.executeQuery();
//			ResultSet SYQRSet = JH_DBHelper.excuteQuery(connection, SYQRsql);
			PreparedStatement pstmt3 = connection.prepareStatement(GYQRsql);
			ResultSet GYQRSet = pstmt3.executeQuery();
//			ResultSet GYQRSet = JH_DBHelper.excuteQuery(connection, GYQRsql);
			PreparedStatement pstmt4 = connection.prepareStatement(DWCRsql);
			ResultSet DWCRSet = pstmt4.executeQuery();
//			ResultSet DWCRSet = JH_DBHelper.excuteQuery(connection, DWCRsql);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			if (otheritemRSet.getString("SETDATE") != null
					&& !otheritemRSet.getString("SETDATE").isEmpty()) {
				try {
					Date qssj = sdf.parse(otheritemRSet.getString("SETDATE"));
					ql.setQLQSSJ(qssj);
				} catch (Exception ex) {

				}
			}
			if (otheritemRSet.getString("APPDATE") != null
					&& !otheritemRSet.getString("APPDATE").isEmpty()) {
				try {
					Date jssj = sdf.parse(otheritemRSet.getString("APPDATE"));
					ql.setQLJSSJ(jssj);
				} catch (Exception ex) {

				}
			}
			if (otheritemRSet.getString("DESTROYDATE") != null
					&& !otheritemRSet.getString("DESTROYDATE").isEmpty()) {
				try {
					Date zxsj = sdf.parse(otheritemRSet
							.getString("DESTROYDATE"));
					fsql.setZXSJ(zxsj);
				} catch (Exception ex) {

				}
			}
			fsql.setZJJZWDYFW(otheritemRSet.getString("DYFW"));
			ql.setFJ(otheritemRSet.getString("REMARK"));
			ql.setBDCQZH(otheritemRSet.getString("OTHERITEMOWNERNUM"));// 不动产权证号
			Double zqse = otheritemRSet.getDouble("OTHERITEMVALUE");
			String txqzl = otheritemRSet.getString("OTHERITEMTYPE");
			String dyfs;
			if (txqzl != null) {
				txqzl = txqzl.trim();
				if (txqzl.equals("1205")) {
					dyfs = "2";
					fsql.setZGZQSE(zqse);
				} else {
					dyfs = "1";
					fsql.setBDBZZQSE(zqse);
				}
				fsql.setDYFS(dyfs);
			}
			String DYR = "";
			if (SYQRSet.next()) {
				DYR += SYQRSet.getString("OWNERNAME") + ",";
			}
			if (GYQRSet.next()) {
				DYR += GYQRSet.getString("OTHEROWNERNAME") + ",";
			}
			if (DYR.contains(",")) {
				DYR = DYR.substring(0, DYR.length() - 1);
			} else if (DWCRSet.next() && (DYR.isEmpty() || DYR.equals(""))) {
				DYR = DWCRSet.getString("OWNERCOMPANY");
			}
			pstmt2.close();
			pstmt3.close();
			pstmt4.close();
			DWCRSet.close();
			GYQRSet.close();
			SYQRSet.close();
			fsql.setDYR(DYR);
			rightMap.put(ql, fsql);
		}
		pstmt.close();
		otheritemRSet.close();
		String transformSql = "select * from HOUSE.opr_transform where casenum ='"
				+ casenum + "'";
		PreparedStatement pstmt5 =null;
		ResultSet transformRSet =null;
		try{
			 pstmt5 = connection.prepareStatement(transformSql);
			 transformRSet = pstmt5.executeQuery();
			if (transformRSet.next()) {
				ql.setQDJG(transformRSet.getDouble("PRICETOT"));
			}
			pstmt5.close();
			transformRSet.close();
		}
		catch(Exception ex){
		}
		finally{
			if(pstmt5!=null){
				pstmt5.close();
			}
			if(transformRSet!=null){
				transformRSet.close();
			}
			rightMap.put(ql, fsql);
		}
		
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
					if(blob!=null){
						String fileName = fileResultSet.getString("FILEID");
						InputStream inStream = blob.getBinaryStream();
						byte[] buf = InputStreamToByte(inStream);
						insertDataService.InsertFJFromZJK(proinstId, fileName,
								configFilePath, buf, i);
						flag = true;
						i++;
					}
					
				}
				pstmt3.close();
				pstmt2.close();
				imageResultSet.close();
				fileResultSet.close();
			}
			pstmt.close();
			imageScanResultSet.close();
		} catch (Exception ex) {
			flag = false;
		}
		try {
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	@Override
	public String GetBatchProject(String prodefid, String batchNumber,
			HttpServletRequest request, String number) {
		String flag = "false";
		Connection connection = null;
		try {
//			logger.info("开始创建大宗件");
			connection = JH_DBHelper.getConnect_jy();
			flag = GetCasenum(prodefid, batchNumber, batchNumber, connection,
					null, null, request, number, "0");
		} catch (Exception ex) {
			flag = "false";
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {

			}
		}
		return flag;
	}

	@Override
	public String GetProject(String prodefid, String xmbh, String batchNumber,
			String casenum, String proinstid, String count, String num,
			HttpServletRequest request) {
		String flag = "false";
		Connection connection = null;
		try {
			connection = JH_DBHelper.getConnect_jy();
			flag = GetCasenum(prodefid, batchNumber, casenum, connection, xmbh,
					proinstid, request, num, count);
		} catch (Exception ex) {
			flag = "false";
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {

			}
		}
		return flag;
	}

	private String GetCasenum(String prodefid, String batchNumber,
			String casenum, Connection connection, String xmbh, String id,
			HttpServletRequest request, String number, String con)
			throws Exception {
		String flag = "false";
		int num = Integer.parseInt(number);
		int count = Integer.parseInt(con);
		String getCasenumSql = "select 1 from HOUSE.PRO_HOUSEINFO where casenum ='"
				+ casenum + "'";
		PreparedStatement pstmt = connection.prepareStatement(getCasenumSql);
		ResultSet isExistCasenum = pstmt.executeQuery();
//		ResultSet isExistCasenum = JH_DBHelper.excuteQuery(connection,getCasenumSql);
		String file_Path = request.getSession().getServletContext()
				.getRealPath("/");
		String servername = StringHelper.formatObject(InetAddress
				.getLocalHost().getHostAddress());
		String basePath = request.getScheme() + "://" + servername + ":"
				+ request.getLocalPort() + request.getContextPath() + "/";
		String staffid = Global.getCurrentUserInfo().getId();
		String url = basePath + "app/operation/batch/sjzacceptproject/" + prodefid
				+ "/" + batchNumber + "/" + staffid;
		Map<String, String> map = new HashMap<String, String>();
		while ( count < num) {
			flag="warning："+casenum+"此案卷号后被中断";
			if(isExistCasenum.next()){
				String project_id = null;
			boolean isCheckDY = false;
			if (xmbh == null && id == null) {
				isCheckDY = true;
				// 调用易大师创建项目方法（工作流）
				// 1、URL : /app/operation/batch/acceptproject/{prodefid}/{batch}
				// 2、请求方式 POST
				// 3、参数 prodefid 流程定义id batch 批次号
				String jsonresult = ProjectHelper.httpGet(url, map);
				JSONObject object = JSON.parseObject(jsonresult);
				// 返回值
				// id :产生流程实例iD
				id = object.containsKey("id") ? StringHelper
						.formatObject(object.get("id")) : "";
				// desc:消息 如果受理成功是 “受理成功”
				String desc = object.containsKey("desc") ? StringHelper
						.formatObject(object.get("desc")) : "";
				if (!desc.equals("受理成功")) {
					continue;
				}
				// 刘树峰 2016.3.17 创建项目 获取xmbh
				Wfi_ProInst proinst = baseCommonDao.get(Wfi_ProInst.class, id);
				if (proinst != null) {
					project_id = proinst.getFile_Number();
				}
				if (!StringHelper.isEmpty(project_id)) {
					ProjectInfo info = ProjectHelper.GetProjectFromRest(
							project_id, request);
					xmbh = info.getXmbh();
				}
			}
			// 调用抽取数据方法
			ExtractFJFromZJK(id, casenum, file_Path);
			flag = ExtractSXFromZJK(casenum, xmbh, isCheckDY);
			//用坐落更新项目名称
			updateXMMCByZL(project_id);
			if (flag.contains("警告：")) {
				return flag + '$' + xmbh + '$' + batchNumber + '$' + casenum
						+ '$' + id + '$' + count + '$' + num;
			} else if (flag.equals("warning")) {
				return flag;
			} else if (!flag.equals("true")) {
				smProInstService.deleteProInst(id);
			}
			xmbh = null;
			id = null;
			casenum = String.valueOf(Long.parseLong(casenum) + 1);
			count += 1;
			getCasenumSql = "select 1 from HOUSE.PRO_HOUSEINFO where casenum ='"
					+ casenum + "'";
			isExistCasenum = JH_DBHelper.excuteQuery(connection, getCasenumSql);
//			isExistCasenum.close();
		}
		}
		baseCommonDao.flush();
		pstmt.close();
		isExistCasenum.close();
		return flag;
	}

	/**
	 * 石家庄房产交易库数据检查方法 2016年10月9日 19:31:20
	 * */
	public String checkData(String casenum, String xmbh) {
		String resultString="true";
		Connection connection = null;
		try {
			connection = JH_DBHelper.getConnect_jy();
			Map<BDCS_QL_GZ, BDCS_FSQL_GZ> rightMap = GetQLData(casenum,connection);
			if(rightMap.size()==0){
				resultString="错误：房产未推送“权利”数据";
				return resultString;
			}
			// 从房产库中获取权利人信息
			// 获取抵押权利人
			Map<String, List<BDCQLR>> dyQlrList = new HashMap<String, List<BDCQLR>>();
			dyQlrList.put("0", GetDYBDCQLR(casenum, connection));
			// 获取转移权利人
			Map<String, List<BDCQLR>> zyQlrList = new HashMap<String, List<BDCQLR>>();
			zyQlrList = GetSCZYBDCQLR(casenum, connection);
			if(dyQlrList==null && dyQlrList.size()==0 && zyQlrList==null && zyQlrList.size()==0){
				resultString="错误：房产未推送“权利人”数据。   ";
			}
			//检查relationid和bdcdyh关联表HOUSEPROPERTY.REL_HOUSE_REALESTATE关联关系
			String sql1 = "select HOUSEID from house.pro_houseinfo where CASENUM ='"
					+ casenum + "'";
			ResultSet resultSet1 = null;
			resultSet1 = JH_DBHelper.excuteQuery(connection, sql1);
			if (!resultSet1.next()) {
				resultString+="错误：房产未推送房产房屋ID和登记系统不动产单元号关联关系   ";
				return resultString;
			}
		} catch (SQLException e) {
		}
		return resultString;
	}

	//大宗件将项目名称默认置为权利人名称
	private void updateXMMCByQLR(String projectid) {
//		logger.info("开始修改项目名称");
		List<BDCS_XMXX> xmxxs =baseCommonDao.getDataList(BDCS_XMXX.class, "project_id='"+projectid+"'");
		if(xmxxs!=null&&xmxxs.size()>0){
			BDCS_XMXX xmxx=xmxxs.get(0);
//			logger.info(projectid);
			List<BDCS_SQR> qls=baseCommonDao.getDataList(BDCS_SQR.class, "xmbh='"+xmxx.getId()+"'");
			if(qls!=null &&qls.size()>0){
//				logger.info(qls.get(0).getSQRXM());
				//用其中一个权利人名称更新项目名称
				if(qls.get(0).getSQRXM()!=null&&!"".equals(qls.get(0).getSQRXM())){
					List<Wfi_ProInst> proInsts =baseCommonDao.getDataList(Wfi_ProInst.class, "file_number='"+projectid+"'");
					if(proInsts!=null && proInsts.size()>0){
						Wfi_ProInst proInst=proInsts.get(0);
						proInst.setProject_Name(qls.get(0).getSQRXM());
						baseCommonDao.update(proInst);
					}
					
				}
			}
		}
	}
	//大宗件将项目名称默认置为权利人名称
		private void updateXMMCByZL(String projectid) {
			logger.info("开始修改项目名称");
			List<BDCS_XMXX> xmxxs =baseCommonDao.getDataList(BDCS_XMXX.class, "project_id='"+projectid+"'");
			if(xmxxs!=null&&xmxxs.size()>0){
				BDCS_XMXX xmxx=xmxxs.get(0);
				logger.info(projectid);
				List<BDCS_DJDY_GZ> djdy_GZs=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "xmbh='"+xmxx.getId()+"'");
				if(djdy_GZs!=null &&djdy_GZs.size()>0){
					logger.info(djdy_GZs.get(0).getBDCDYID());
					List<BDCS_H_XZ> hs=baseCommonDao.getDataList(BDCS_H_XZ.class, "BDCDYID='"+djdy_GZs.get(0).getBDCDYID()+"'");
					//用其中一个权利人名称更新项目名称
					if(hs.get(0).getZL()!=null&&!"".equals(hs.get(0).getZL())){
						List<Wfi_ProInst> proInsts =baseCommonDao.getDataList(Wfi_ProInst.class, "file_number='"+projectid+"'");
						if(proInsts!=null && proInsts.size()>0){
							Wfi_ProInst proInst=proInsts.get(0);
							proInst.setProject_Name(hs.get(0).getZL());
							baseCommonDao.update(proInst);
						}
					}
				}
				baseCommonDao.flush();
			}
		}
}
