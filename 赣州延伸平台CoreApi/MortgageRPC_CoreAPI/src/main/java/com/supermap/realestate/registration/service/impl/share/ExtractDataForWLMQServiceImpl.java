package com.supermap.realestate.registration.service.impl.share;

import java.io.InputStream;
import java.net.InetAddress;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
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
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.mapping.HandlerMapping.RegisterWorkFlow;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.xmlmodel.BDCQLR;
import com.supermap.realestate.registration.service.InsertDataService;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.JH_DBHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYH;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYJYBGQH;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYQLR;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProInstService;

import net.sf.ehcache.search.Results;

@Service("extractDataForWLMQService")
public class ExtractDataForWLMQServiceImpl extends ExtractDataServiceImpl {
	private static final Log logger = LogFactory.getLog(ExtractDataForWLMQServiceImpl.class);
	@Autowired
	private InsertDataService insertDataService;
	@Autowired
	private SmProInstService smProInstService;
	@Autowired
	private com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao;
	Map<String, String> ghytMap = new HashMap<String, String>();
	Map<String, String> fwxzMap = new HashMap<String, String>();
	@Override
	public String ExtractSXFromZJK(String casenum, String xmbh, boolean bool) {
		String flag = "false";
		//应乌鲁木齐强烈需求增加房产业务号内容格式检查方法
		flag=CheckCasenum(casenum);
		if(!flag.equals("true")){
			return flag;
		}
		Connection connection = null;
		String inst_business = "wfms_inst_business";
		//大宗件时获取前台传来的登记原因
		String djyy="";
		String[] ywh_djyy= casenum.split("&&");
		if(ywh_djyy!=null&&ywh_djyy.length>0){
			casenum=ywh_djyy[0];
		}
		djyy="";
		if(ywh_djyy!=null&&ywh_djyy.length>1){
			djyy=ywh_djyy[1];
		}
		//先判断选择的业务流程跟单元来源是否匹配
		BDCS_XMXX xmxx = baseCommonDao.get(BDCS_XMXX.class, xmbh);
		String unitetype="";
		if(xmxx!=null){
			RegisterWorkFlow flow = HandlerFactory.getWorkflow(xmxx.getPROJECT_ID());
			 unitetype=flow.getUnittype();
		}
		initialMap();
		try {
			
			//乌鲁木齐抽取时 项目信息页面显示房产业务号  同时将抽取的房产业务号保存到当前项目信息表中
			xmxx.setFCYWH(casenum);
			baseCommonDao.update(xmxx);
			List<Wfi_ProInst> proinsts=baseCommonDao.getDataList(Wfi_ProInst.class,"File_Number='"+xmxx.getPROJECT_ID()+"'");
			if(proinsts!=null&&proinsts.size()>0){
				Wfi_ProInst proinst=proinsts.get(0);
				proinst.setYwh(casenum);
				baseCommonDao.update(proinst);
				baseCommonDao.flush();
			}
			// logger.info("开始抽取属性。。。");
			connection = JH_DBHelper.getConnect_jy();
			List<BDCS_QL_GZ> listql = new ArrayList<BDCS_QL_GZ>();// 房管权利只有一条，为对应多个房屋ID，直接复制权利
			List<BDCS_FSQL_GZ> listfsql = new ArrayList<BDCS_FSQL_GZ>();// 房管权利只有一条，为对应多个房屋ID，直接复制权利
			List<String> listcasnum = new ArrayList<String>();// 房屋业务号集合
			List<BDCQLR> qlrList = new ArrayList<BDCQLR>();// 权利人
			List<JYQLR> ywrList = new ArrayList<JYQLR>();// 申请人信息，只有查封业务才装信息，其他业务都没有数据
															// 义务人
			List<String> bdcdyhList = new ArrayList<String>();// 房管的房屋ID集合
			List<JYH> HList = new ArrayList<JYH>();// 现房屋ID只在变更业务区分用到，其他不用
			String isBG = "";// 区分的是否是变更业务
			List<JYJYBGQH> bgqhList = new ArrayList<JYJYBGQH>();// 变更前房屋ID只在变更区分用到，其他不用
			String ywlx = "select * from wfms_inst_business where instprocessid in ( select instprocessid from wfms_inst_business t where acceptcode='"
					+ casenum + "')";
			PreparedStatement ywlxsPS = connection.prepareStatement(ywlx);
			ResultSet ywlxs = ywlxsPS.executeQuery();
			List<Object> object = new ArrayList<Object>();

			if (ywlxs.next()) {
				// ywlxs = JH_DBHelper.excuteQuery(connection, ywlx);
				ywlxs = ywlxsPS.executeQuery();
			} else {
				inst_business = "WFMS_INST_BUSINESS_HISTORY";
				ywlx = "select * from " + inst_business + " where instprocessid in ( select instprocessid from "
						+ inst_business + " t where acceptcode='" + casenum + "')";
				// ywlxs = JH_DBHelper.excuteQuery(connection, ywlx);
				ywlxs = ywlxsPS.executeQuery(ywlx);
			}
			if(unitetype.equals("031")){
				fwzt="2";
			}else if(unitetype.equals("032")){
				fwzt="1";
			}
			while (ywlxs.next()) {
				casenum = ywlxs.getString(
						"ACCEPTCODE");/* casenum=16103210-161201-1531042 */

				String code = ywlxs.getString("BPCATEGORYCODE");
				//
				// StringBuffer b=new StringBuffer(casenum);
				// String code=b.substring(0,casenum.indexOf("-"));
				String otheritemSql = "select * from WFD_PRODEF t where t.code='" + code + "'";
				PreparedStatement otheritemRSetPS = connection.prepareStatement(otheritemSql);
				ResultSet otheritemRSet = otheritemRSetPS.executeQuery();
				if (otheritemRSet.next() && null != otheritemRSet.getString("NAME")) {
					// 从房产库中获取权利人信息
					// dyQlrList= GetDYBDCQLR(casenum, connection);
					String name = otheritemRSet
							.getString("NAME");/* 经济适用房性质调整（完全产权） */
					if (name.indexOf("抵押") != -1) {
						// 封装权利、附属权利
						String sqrsql = "select * from " + inst_business + " b "
								+ "join TPF_TXQDJYW t on t.ywslid=b.businessid "
								+ "left join TPF_BDC_DYQRS f on f.ywslid=b.businessid " + "where b.acceptcode='"
								+ casenum + "' ";
						if (name.lastIndexOf("注销") != -1) {// 抵押注销可以没有权利
							sqrsql = "select * from " + inst_business + " b "
									+ "left join TPF_TXQDJYW t on t.ywslid=b.businessid "
									+ "left join TPF_BDC_DYQRS f on f.ywslid=b.businessid " + "where b.acceptcode='"
									+ casenum + "' ";
						}
						// ResultSet SQRSet =
						// JH_DBHelper.excuteQuery(connection, sqrsql);
						PreparedStatement ps = connection.prepareStatement(sqrsql);
						ResultSet SQRSet = ps.executeQuery();
						if (SQRSet.next()) {
							object.addAll(rightMapDY(SQRSet, name, connection, casenum, inst_business));// 权利人
						}
						if (ps != null) {
							ps.close();
						}
						if (SQRSet != null) {
							SQRSet.close();
						}

						// 查房管的房屋ID
						String bdcdyhsql = "select f.FWID as ID from " + inst_business + " b "
								+ "join TPF_BDC_DYQRS fd on fd.YWSLID=b.BUSINESSID "
								+ "join TPF_BDC_DYQRS_ZJFW f on f.DYQRDID=fd.id " + "where b.acceptcode='" + casenum
								+ "'";
						bdcdyhList.addAll(bdcdyhList(connection, bdcdyhsql));
						if (bdcdyhList.size() > 0) {
//							fwzt = GetFWZT(bdcdyhList.get(0));
						}

					} else if (!name.equals("一般抵押转移") && !name.equals("最高额抵押转移") && !name.equals("在建工程抵押转移")
							&& !name.equals("在建工程最高额抵押转移") && name.indexOf("转移") != -1
							|| name.indexOf("预告") != -1 && !name.equals("预购商品房抵押预告") && !name.equals("预购商品房抵押预告注销")
									&& !name.equals("商品房预告+抵押")) {
						// 封装权利、附属权利TPF_BDC_JYQRS
						String sqrsql = "select d.*,fw.* from " + inst_business + " b "
								+ "join TPF_ZYDJYW d on d.YWSLID=b.BUSINESSID   "
								+ "join TPF_BDC_JYQRS_FW fw on fw.YWSLID=b.BUSINESSID   " + "where acceptcode='"
								+ casenum + "'";
						// ResultSet SQRSet =
						// JH_DBHelper.excuteQuery(connection, sqrsql);
						PreparedStatement pstmt = connection.prepareStatement(sqrsql);
						ResultSet SQRSet = null;
						SQRSet = pstmt.executeQuery();
						if (SQRSet.next()) {
							object.addAll(rightMapZY(SQRSet, name, connection, casenum, inst_business));// 权利人
						}
						if (pstmt != null) {
							pstmt.close();
						}
						if (SQRSet != null) {
							SQRSet.close();
						}

						// 查房管的房屋ID

						String bdcdyhsql = "select f.FWID as ID from " + inst_business + " b "
								+ "join TPF_BDC_JYQRS_FW f on f.YWSLID=b.BUSINESSID " + "where b.acceptcode='" + casenum
								+ "'";
						bdcdyhList.addAll(bdcdyhList(connection, bdcdyhsql));
						if (bdcdyhList.size() > 0) {
//							fwzt = GetFWZT(bdcdyhList.get(0));
						}
					} else if (name.indexOf("变更") != -1 && !name.equals("一般抵押变更") && !name.equals("最高额抵押变更")
							&& !name.equals("在建工程抵押变更") && !name.equals("在建工程最高额抵押变更") || name.indexOf("楼盘") != -1
							|| name.indexOf("调整") != 1) {
						if (name.indexOf("分割、合并房屋") != -1 || name.indexOf("面积增加或减少") != -1) {
							// 分割合并类型&面积变化
							String fcidX = "select * from " + inst_business + " b "
									+ "join TPF_BDC_LPBXJQRD_ZRZXX  xx on xx.YWSLID= b.businessid "
									+ "left join TPS_FWBG fwbg on fwbg.XFWID=xx.fwid where b.acceptcode='" + casenum
									+ "'";// 先查出变更类型
							PreparedStatement bdcdyhSetps = connection.prepareStatement(fcidX); // ，变更序号
							ResultSet bdcdyhSet = bdcdyhSetps.executeQuery();
							if (bdcdyhSet.next()) {
								isBG = bdcdyhSet.getString("BGLX");// 2，合并 3分割
								bdcdyhSet = bdcdyhSetps.executeQuery();
								List<Object> objects = getHList(bdcdyhSet, isBG);// 现房屋ID跟原房屋ID集合，现下标0，原下标1
								if (bdcdyhSetps != null) {
									bdcdyhSetps.close();
								}
								if (bdcdyhSet != null) {
									bdcdyhSet.close();
								}
								HList.addAll((List<JYH>) objects.get(0));// 现房屋ID

								bgqhList.addAll((List<JYJYBGQH>) objects.get(1));// 原房屋ID
								if (bgqhList.size() < 1) {// 如果房产的yfwid没有，则不是分割合并的
									JYJYBGQH y = new JYJYBGQH();
									y.setRELATIONID(HList.get(0).getRELATIONID());
									bgqhList.add(y);
								}
								bdcdyhList.addAll((List<String>) objects.get(2));// 现房屋ID
																					// 如果变更类型是1
																					// Hlist、bgqhList作废，只用到bdcdyhList

							}
							String yfwid = bgqhList.get(0).getRELATIONID();
//							fwzt = GetFWZT(yfwid);
							listcasnum.add(casenum);
							if (bdcdyhList == null || bdcdyhList.size() == 0) {
								bdcdyhList.add(yfwid);
							}

						} /**
						 * 首次登记业务
						 * 
						 * @作者 Kong
						 * @创建时间 2017年2月13日上午10:11:35
						 * @return
						 */
						else if ( name.indexOf("新建") != -1) {
							isBG = "1";
							String fwidsql = "select xx.ywslid,xx.zrzid,fw.id,fw.xqmc,fw.dqzrzid,fw.fwsmzq,fw.fwzl,fw.fwxz，fw.*  from "
									+ inst_business + " b " + "join TPF_BDC_LPBXJQRD lp  on b.businessid=lp.ywslid "
									+ "join TPF_BDC_LPBXJQRD_ZRZXX xx on b.businessid= xx.ywslid "
									+ "left join   TPS_FW   fw   on   fw.dqzrzid=xx.zrzid "
									+ "left join   TPS_FWBG   fwbg   on   fwbg.xfwid=fw.id " + "where b.acceptcode='"
									+ casenum + "'";
							PreparedStatement pstmt = connection.prepareStatement(fwidsql);
							ResultSet SQRSet = pstmt.executeQuery();
							if (SQRSet.next()) {
								object.addAll(rightMapSC(SQRSet, name, connection, inst_business));// 权利人
							}
							if (pstmt != null) {
								pstmt.close();
							}
							if (SQRSet != null) {
								SQRSet.close();
							}
							// 查房管的房屋ID
							bdcdyhList.addAll(bdcdyhList(connection, fwidsql));
						}else {
							// 非分割合并业务
							String fcidX = "select * from " + inst_business + " b "
									+ "join TPF_BDC_LPBXJQRD_ZRZXX  xx on xx.YWSLID= b.businessid "
									+ "left join TPS_FWBG fwbg on fwbg.XFWID=xx.fwid where b.acceptcode='" + casenum
									+ "'";// 先查出变更类型
							PreparedStatement bdcdyhSetps = connection.prepareStatement(fcidX); // 变更序号
							ResultSet bdcdyhSet = bdcdyhSetps.executeQuery();
							List<Object> objects = getHList(bdcdyhSet, "");// 现房屋ID跟原房屋ID集合，现下标0，原下标1
							if (bdcdyhSetps != null) {
								bdcdyhSetps.close();
							}
							if (bdcdyhSet != null) {
								bdcdyhSet.close();
							}
							HList.addAll((List<JYH>) objects.get(0));// 现房屋ID
							// 更正业务时，获取房产最新属性
							String sql = "select * from TPS_FW WHERE id='" + HList.get(0).getRELATIONID() + "'";
							PreparedStatement ps = connection.prepareStatement(sql);
							ResultSet fwRs = ps.executeQuery();

							if (fwRs != null) {
								if (fwRs.next()) {
									HList.get(0).setZL(fwRs.getString("FWZL"));
									HList.get(0).setSCJZMJ(fwRs.getDouble("JZMJ"));
									HList.get(0).setGHYT(ghytMap.get(fwRs.getString("GHYT")));
									HList.get(0).setFWXZ(fwxzMap.get(fwRs.getString("FWXZ")));
								}
							}
							if (ps != null) {
								ps.close();
							}
							if (fwRs != null) {
								fwRs.close();
							}
							String xfwid = HList.get(0).getRELATIONID();
//							fwzt = GetFWZT(xfwid);
							listcasnum.add(casenum);
							if (bdcdyhList == null || bdcdyhList.size() == 0) {
								bdcdyhList.add(xfwid);
							}

						}
					} else if (name.indexOf("查封") != -1) {
						String bdcdyhsql = "select cf.fwid as id,CFWJJWH,CFDJSJ,JFWJJWH,JFJG,CFJG,CFLXH from "
								+ inst_business + " ib " + "join TPF_CFDJB cf on cf.ywslid=ib.businessid "
								+ "where ib.acceptcode='" + casenum + "'";
						// 房管房屋ID集合
						bdcdyhList.addAll(bdcdyhList(connection, bdcdyhsql));
						if (bdcdyhList.size() > 0) {
//							fwzt = GetFWZT(bdcdyhList.get(0));
							// 权利人、权利、附属权利
							PreparedStatement ps = connection.prepareStatement(bdcdyhsql);
							ResultSet fcqlfsqlSet = ps.executeQuery();
							if (fcqlfsqlSet.next()) {
								object.addAll(rightMapCF(fcqlfsqlSet, connection, casenum, inst_business));
							}
							if (ps != null) {
								ps.close();
							}
							if (fcqlfsqlSet != null) {
								fcqlfsqlSet.close();
							}

						}
						// 权利人、权利、附属权利
						// String fcql="select * from "+inst_business+" b join
						// TPF_CFDJB t on t.ywslid=b.BUSINESSID where
						// b.acceptcode='"+casenum+"'";
						// ResultSet fcqlfsqlSet =
						// JH_DBHelper.excuteQuery(connection, fcql);
						// if(fcqlfsqlSet.next()){
						// object.addAll(rightMapCF(fcqlfsqlSet,connection,casenum));
						// }
					}
					if (otheritemRSetPS != null) {
						otheritemRSetPS.close();
					}
					if (otheritemRSet != null) {
						otheritemRSet.close();
					}

				}
				// 从房产库中获取他项权利表中其他必要信息
				// JH_DBHelper.excuteUpdate(connection,"uptate BDCS_XMXX set
				// FCYWH='"+casenum+"' where ywlsh='"+xmbh+"'");
			}
			inst_business = "wfms_inst_business";// 变动表名改回
			for (int i = 0; i < object.size(); i++) {// 能进来第二次的说明是合并办理业务
				if (i % 4 == 0) {
					listql.add((BDCS_QL_GZ) object.get(i));
					listfsql.add(object.size() > (i + 1) ? (BDCS_FSQL_GZ) object.get(i + 1) : null);
					qlrList.addAll(object.size() > (i + 2) ? (List<BDCQLR>) object.get(i + 2) : null);
					ywrList.addAll(object.size() > (i + 3) ? (List<JYQLR>) object.get(i + 3) : null);
					listcasnum.add(casenum);
				}
			}
			if ((bdcdyhList == null || bdcdyhList.size() < 1) && HList.size() < 1) {
				flag = "nofwztorh";
				return flag;
			}
			if ((bdcdyhList.isEmpty() || bdcdyhList == null) && HList.size() < 1) {
				flag = "warning";
				return flag;
			}
//			List<BDCS_XMXX> xmxxs = baseCommonDao.getDataList(BDCS_XMXX.class, "xmbh='" + xmbh + "'");
			if (xmxx != null) {
//				BDCS_XMXX xmxx = xmxxs.get(0);
				xmxx.setFCYWH(casenum);
				baseCommonDao.update(xmxx);
			}
			if (listcasnum.size() < 2) {// 有两个以上说明是合并办理业务，合并办理不考虑多户的情况。如果不是合并办理，权利要复制有多少的房屋ID就有多少个权利
				for (int i = 1; i < bdcdyhList.size(); i++) {
					listql.add(listql.size() > 0 ? listql.get(0) : null);
				}
				for (int i = 1; i < bdcdyhList.size(); i++) {
					listfsql.add(listfsql.size() > 0 ? listfsql.get(0) : null);
				}
			}
			// 权利人有重复的，去掉重复
			List<BDCQLR> qlr = new ArrayList<BDCQLR>();// 权利人
			List<String> qlrmcList=new ArrayList<String>();
			for (int i = 0; i < qlrList.size(); i++) {
				if (!qlrmcList.contains(qlrList.get(i).getQLRMC())) {
					qlr.add(qlrList.get(i));
					qlrmcList.add(qlrList.get(i).getQLRMC());
				}
			}
			//判断受理的业务类型与单元来源是否匹配
//			if((unitetype.equals("031")&& !"2".equals(fwzt))||(unitetype.equals("032")&& !"1".equals(fwzt))){
//				flag ="受理的业务流程类型与房屋状态不匹配，或房屋id无法关联";
//				return flag;
//			}
			
			if ("2".equals(isBG) || "3".equals(isBG)) { // 分隔合并变更
				BDCS_QL_GZ ql = new BDCS_QL_GZ();
				ql.setXMBH(xmbh);
				BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
				fsql.setQLID(ql.getId());
				flag = insertDataService.InsertSXFromZJKBG(ql, fsql, xmbh, casenum, qlr, ywrList, bgqhList, HList, bool,
						fwzt);
			} else if ("1".equals(isBG)) {// 初始登记
				flag = insertDataService.InsertSXFromZJK(listql, listfsql, xmbh, casenum, qlrList, ywrList, bdcdyhList,
						bool, fwzt);
			} else {
				flag = insertDataService.InsertSXFromZJK(listql, listfsql, xmbh+"&&"+djyy, listcasnum, qlr, ywrList, bdcdyhList,
						HList, bool, fwzt);
			}

			if (ywlxsPS != null) {
				ywlxsPS.close();
			}
			if (ywlxs != null) {
				ywlxs.close();
			}
			return flag;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}

			return flag;
		}
	}

	@Override
	public String AddDYBDCDY(String ywh, String xmbh) {
		String flag = "false";
		// 从房产库中获取权利人信息
		flag = ExtractSXFromZJK(ywh, xmbh, false);
		return flag;
	}

	// 抵押通过casenum，从房产系统中获取权利人的相关信息
	private List<BDCQLR> GetDYBDCQLR(ResultSet DYQRRSet, String qlid, String lx) throws SQLException {
		List<BDCQLR> dyqlrlist = new ArrayList<BDCQLR>();
		while (DYQRRSet.next()) {
			BDCQLR qlr = new BDCQLR();
			qlr.setQLID(qlid);
			qlr.setQLRMC(DYQRRSet.getString("QLRMC"));// 权利人名称
			qlr.setFDDBR(DYQRRSet.getString("FDDBRMC"));// 法定代表人
			qlr.setDZ(DYQRRSet.getString("DZ"));// 地址
			// qlr.setSSHY(DYQRRSet.getString(""));//
			// ------------------------------------------------所属行业 没有
			qlr.setDH(DYQRRSet.getString("LXDH"));// 电话
			qlr.setZJH(DYQRRSet.getString("ZJHM"));// 证件号码、

			String fczjlx = DYQRRSet.getString("ZJLX");
			if ("2000".equals(fczjlx)) {
				qlr.setZJZL("1");// 证件种类
			} else if ("2001".equals(fczjlx)) {
				qlr.setZJZL("4");
			} else if ("2002".equals(fczjlx)) {
				qlr.setZJZL("3");
			} else if ("2003".equals(fczjlx) || "2004".equals(fczjlx)) {
				qlr.setZJZL("5");
			} else if (null == fczjlx || "".equals(fczjlx)) {
				// 房产没有数据，这里就空着
			} else {
				qlr.setZJZL("99");
			}
			if ("2601".equals(DYQRRSet.getString("QLRXZ"))||"4".equals(DYQRRSet.getString("QLRXZ"))) {// --------------------------房管只有两种类型
				qlr.setQLRLX("1");//个人
			} else if ("2606".equals(DYQRRSet.getString("QLRXZ"))||"2602".equals(DYQRRSet.getString("QLRXZ"))) {
				qlr.setQLRLX("2");//企业
			} else if ("0".equals(DYQRRSet.getString("QLRXZ"))&&qlr.getQLRMC()!=null&&(qlr.getQLRMC().contains("银行")||qlr.getQLRMC().contains("公司"))) {
				qlr.setQLRLX("2");//企业
			} else {
				qlr.setQLRLX("99");
			}
			// -----------------------------------------
			qlr.setGJ(DYQRRSet.getString("GJ"));// 国家
			qlr.setHJSZSS("HJSZD");// 户籍所在地
			// qlr.setXB("");//--------------------------------------------------性别
			// 在 购房人表
			qlr.setYB(DYQRRSet.getString("YZBM"));// 邮编
			// qlr.setGZDW("");//工作单位
			qlr.setDZYJ(DYQRRSet.getString("EMAIL"));// 邮箱
			// qlr.setQLRLX("");//权利人类型----------------------------------权利人性质
			// 房产没有字典表
			// qlr.setQLBL(DYQRRSet.getString("CQFE"));//-----------------房产
			// 产权份额
			if ("zy".equals(lx)) {
				qlr.setQLBL(String.valueOf(DYQRRSet.getInt("ZYFE")));
			} else {
				qlr.setQLBL(String.valueOf(DYQRRSet.getInt("DYBL")));

			}
			// qlr.setGYFS("");//-----------------------------------------表
			// qlr.setGYQK("");//-----------------------------------------共有情况
			// qlr.setBZ("");//-------------------------------------------没有

			dyqlrlist.add(qlr);
		}
		return dyqlrlist;
	}

	// 抵押业务 权利、附属权利封装
	public List<Object> rightMapDY(ResultSet SQRSet, String name, Connection connection, String casenum,
			String inst_business) {
		BDCS_QL_GZ ql = new BDCS_QL_GZ();
		BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
		Map<BDCS_QL_GZ, BDCS_FSQL_GZ> rightMap = new HashMap<BDCS_QL_GZ, BDCS_FSQL_GZ>();
		List<Object> object = new ArrayList<Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		List<BDCQLR> BDCQLR = new ArrayList<BDCQLR>();
		List<JYQLR> JYQLR = new ArrayList<JYQLR>();
		try {
			if (null != SQRSet.getString("ZWLXQXS")) {
				ql.setQLQSSJ(SQRSet.getDate("ZWLXQXS"));
			}
			if (null != SQRSet.getString("ZWLXQXM")) {
				ql.setQLJSSJ(SQRSet.getDate("ZWLXQXM"));
			}
			ql.setQLLX("23");
			// if(null!=SQRSet.getString("ZXSJ")){
			// Date ZXSJ = sdf.parse(SQRSet.getString("ZXSJ"));
			// fsql.setZXSJ(ZXSJ);//注销时间
			// }
			String dyfs = "1";
			if (name.indexOf("最高额抵押") != -1) {
				dyfs = "2";
				fsql.setZGZQSE(SQRSet.getDouble("BDBZZWSE"));
			} else {
				fsql.setBDBZZQSE(SQRSet.getDouble("BDBZZWSE"));
			}
			fsql.setDYFS(dyfs);// 抵押方式
			fsql.setDBFW(SQRSet.getString("DBFW"));// 担保范围
			// ql.setFJ(SQRSet.getString("BZ"));
			ql.setQLLXMC(SQRSet.getString("DYQR"));// 抵押权人
			ql.setBDCQZH(SQRSet.getString("BDCQZH"));// 不动产权证号
			fsql.setDYR(SQRSet.getString("DYR"));// 义务人、抵押人
			ql.setQDJG(SQRSet.getDouble("BDBZZWSE"));// 取得价格-----------------------------------抵押物价值
			fsql.setDYPGJZ(SQRSet.getDouble("PGJZ"));// 评估价值
			fsql.setDYMJ(SQRSet.getDouble("DYMJ"));// 抵押面积
			fsql.setYWR(SQRSet.getString("DYR"));// 义务人、抵押人
			if (name.indexOf("抵押转移") != -1) {
				ql.setDJLX("200");
			} else if (name.indexOf("抵押变更") != -1) {
				ql.setDJLX("300");
			} else if (name.indexOf("注销") != -1) {
				ql.setDJLX("400");
			} else if (name.indexOf("抵押预告") != -1) {
				ql.setDJLX("700");
			} else {
				ql.setDJLX("100");
			}
			fsql.setQLID(ql.getId());
			/**
			 * 房产权利人类型 1---抵押人 2---抵押权人 3---义务人 4---所有权人 41--原所有权人 5---申请人
			 * 6---担保人 7---债权人 8---债务人 9---被查封人 10--异议权利人
			 */
			String qlrsql = "select qlr.*,ql.* from " + inst_business + " b "
					+ "left join TPF_QLR_GZK qlr on qlr.ywslid=b.businessid "
					+ "left join TPF_TXQDJYW ql on ql.ywslid=b.businessid " + "where b.acceptcode='" + casenum
					+ "' and (qlr.QLRLX='2' OR qlr.QLRLX='4' OR qlr.QLRLX='7' OR qlr.QLRLX='10' OR qlr.QLRLX='2601' OR qlr.QLRLX='2606' )";
			ResultSet qlrSET = JH_DBHelper.excuteQuery(connection, qlrsql);
			BDCQLR.addAll(GetDYBDCQLR(qlrSET, ql.getId(), "dy"));
			qlrSET.close();
			String ywrsql = "select qlr.*,ql.* from " + inst_business + " b "
					+ "left join TPF_QLR_GZK qlr on qlr.ywslid=b.businessid "
					+ "left join TPF_TXQDJYW ql on ql.ywslid=b.businessid " + "where b.acceptcode='" + casenum + "' "
					+ "and (qlr.QLRLX!='2' AND qlr.QLRLX!='4' AND qlr.QLRLX!='7' AND qlr.QLRLX!='10')";
			ResultSet ywrSET = JH_DBHelper.excuteQuery(connection, ywrsql);
			JYQLR.addAll(GetFcYwr(ywrSET, "dy"));
			ywrSET.close();
			object.add(ql);
			object.add(fsql);
			object.add(BDCQLR);
			object.add(JYQLR);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return object;
	}

	// 从房产系统中获取权利人的相关信息
	private List<JYQLR> GetFcYwr(ResultSet DYQRRSet, String lx) throws SQLException {
		List<JYQLR> dyqlrlist = new ArrayList<JYQLR>();
		if (DYQRRSet != null) {
			while (DYQRRSet.next()) {
				JYQLR jyqlr = new JYQLR();
				jyqlr.setSQRLB("2");
				jyqlr.setQLRMC(DYQRRSet.getString("QLRMC"));// 权利人名称
				// jyqlr.setFDDBR(DYQRRSet.getString("FDDBRMC"));// 法定代表人
				jyqlr.setDZ(DYQRRSet.getString("DZ"));// 地址
				// qlr.setSSHY(DYQRRSet.getString(""));//
				// ------------------------------------------------所属行业 没有
				jyqlr.setDH(DYQRRSet.getString("LXDH"));// 电话
				jyqlr.setZJH(DYQRRSet.getString("ZJHM"));// 证件号码、
				if ("zy".equals(lx)) {
					jyqlr.setQLBL(DYQRRSet.getString("ZYFE"));// 权利比例
				} else if ("dy".equals(lx)) {
					jyqlr.setQLBL(DYQRRSet.getString("DYBL"));// 权利比例
					jyqlr.setBZ(DYQRRSet.getString("DYYT"));// 抵押用途
				}
				String fczjlx = DYQRRSet.getString("ZJLX");
				if ("2000".equals(fczjlx)) {
					jyqlr.setZJZL("1");// 证件种类
				} else if ("2001".equals(fczjlx)) {
					jyqlr.setZJZL("4");
				} else if ("2002".equals(fczjlx)) {
					jyqlr.setZJZL("3");
				} else if ("2003".equals(fczjlx) || "2004".equals(fczjlx)) {
					jyqlr.setZJZL("5");
				} else if (null == fczjlx || "".equals(fczjlx)) {
					// 房产没有数据，这里就空着
				} else {
					jyqlr.setZJZL("99");
				}
				if ("2601".equals(DYQRRSet.getString("QLRLX"))) {// --------------------------房管只有两种类型
					jyqlr.setQLRLX("1");
				} else if ("2602".equals(DYQRRSet.getString("QLRLX"))) {
					jyqlr.setQLRLX("2");
				} else {
					jyqlr.setQLRLX("99");
				}
				// -----------------------------------------
				jyqlr.setGJ("中国");// 国家
				jyqlr.setHJSZSS("HJSZD");// 户籍所在地
				// qlr.setXB("");//--------------------------------------------------性别
				// 在 购房人表
				jyqlr.setYB(DYQRRSet.getString("YZBM"));// 邮编
				// qlr.setGZDW("");//工作单位
				jyqlr.setDZYJ(DYQRRSet.getString("EMAIL"));// 邮箱
				// qlr.setQLRLX("");//权利人类型----------------------------------权利人性质
				// 房产没有字典表
				jyqlr.setQLBL(DYQRRSet.getString("CQFE"));// -----------------房产
															// 产权份额
				// qlr.setGYFS("");//-----------------------------------------表
				// qlr.setGYQK("");//-----------------------------------------共有情况
				// qlr.setBZ("");//-------------------------------------------没有

				dyqlrlist.add(jyqlr);
			}
		}
		return dyqlrlist;
	}

	// 转移业务 权利、附属权利封装
	public List<Object> rightMapZY(ResultSet SQRSet, String name, Connection connection, String casenum,
			String inst_business) {
		BDCS_QL_GZ ql = new BDCS_QL_GZ();
		BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
		List<Object> object = new ArrayList<Object>();
		List<BDCQLR> BDCQLR = new ArrayList<BDCQLR>();// 权利人集合
		List<JYQLR> JYQLR = new ArrayList<JYQLR>();
		new SimpleDateFormat("yyyyMMdd");
		try {
			fsql.setHTBH(SQRSet.getString("HTBH"));// 合同编号
			// ql.setFJ(SQRSet.getString("BZ"));//---------------------转移确认表
			ql.setQDJG(SQRSet.getDouble("CJZJ"));
			ql.setBDCQZH(SQRSet.getString("BDCQZH"));// 不动产权证号
			fsql.setDYPGJZ(SQRSet.getDouble("PGJ"));// 评估价值
			fsql.setQLID(ql.getId());
			if (name.indexOf("预告") != -1) {// 转移 qllx全部是4 预告
				if (name.indexOf("现房") != -1) {
					ql.setQLLX("99");
				} else {
					ql.setQLLX("4");// 预告现房99 期房4
				}
			} else {
				ql.setQLLX("4");// 预告现房99 期房4
			}
			if (name.endsWith("预告")) {
				ql.setDJLX("700");
			} else if (name.indexOf("预告注销") != -1) {
				ql.setDJLX("400");
			} else if (name.indexOf("预告") == -1 && name.indexOf("转移") != -1) {
				ql.setDJLX("200");
			}
			String qlrsql = "select qlr.*,ql.* from " + inst_business
					+ " b left join TPF_QLR_GZK qlr on qlr.ywslid=b.businessid left join TPF_ZYDJYW ql on ql.ywslid=b.businessid where b.acceptcode='"
					+ casenum + "' and (qlr.QLRLX='2' OR qlr.QLRLX='4' OR qlr.QLRLX='7' OR qlr.QLRLX='10')";
			ResultSet qlrSET = JH_DBHelper.excuteQuery(connection, qlrsql);
			BDCQLR.addAll(GetDYBDCQLR(qlrSET, ql.getId(), "zy"));// 封装权利人
																	// 跟抵押公用方法
			String ywrsql = "select qlr.*,ql.* from " + inst_business
					+ " b left join TPF_QLR_GZK qlr on qlr.ywslid=b.businessid left join TPF_ZYDJYW ql on ql.ywslid=b.businessid where b.acceptcode='"
					+ casenum + "' and (qlr.QLRLX!='2' AND qlr.QLRLX!='4' AND qlr.QLRLX!='7' AND qlr.QLRLX!='10')";
			ResultSet ywrSET = JH_DBHelper.excuteQuery(connection, ywrsql);
			JYQLR.addAll(GetFcYwr(ywrSET, "zy"));
			qlrSET.close();
			ywrSET.close();
			object.add(ql);
			object.add(fsql);
			object.add(BDCQLR);
			object.add(JYQLR);
			// rightMap.put(ql, fsql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				SQRSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return object;
	}

	public List<String> bdcdyhList(Connection connection, String bdcdyhsql) throws Exception {
		List<String> bdcdyhList = new ArrayList<String>();
		try {
			ResultSet bdcdyhSet = JH_DBHelper.excuteQuery(connection, bdcdyhsql);
			while (bdcdyhSet.next()) {
				bdcdyhList.add(bdcdyhSet.getString("ID"));
			}
			bdcdyhSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bdcdyhList;
	}

	@Override
	public boolean ExtractFJFromZJK(String proinstId, String caseNum, String configFilePath) {
		boolean flag = false;
		Connection connection = null;
		try {
			connection = JH_DBHelper.getConnect_jy();
			// 从“图像表”取得当前合同号（案卷号）所有图片属性信息
			String imageScanSql = "select IMAGEID,FILEID from arcuser.img_imagescan where casenum = '" + caseNum
					+ "' order by fileid, pageinnumber ";
			ResultSet imageScanResultSet = JH_DBHelper.excuteQuery(connection, imageScanSql);
			int i = 1;
			while (imageScanResultSet.next()) {
				// 根据“图像ID”进行循环
				String imageid = imageScanResultSet.getString("IMAGEID");
				String fileid = imageScanResultSet.getString("FILEID");
				String imageSql = "select IMAGEDATA from arcuser.img_imagedatajpg where IMAGEID='" + imageid + "'";
				ResultSet imageResultSet = JH_DBHelper.excuteQuery(connection, imageSql);
				String fileidSql = "select fileid from house.opr_filereceived where CASENUM='" + caseNum
						+ "' and FILETYPE = '" + fileid + "'";
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
				imageResultSet.close();
				fileResultSet.close();
			}
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

	@Override
	public String GetBatchProject(String prodefid, String batchNumber, HttpServletRequest request, String number) {
		String flag = "false";
		Connection connection = null;
		try {
			connection = JH_DBHelper.getConnect_jy();
			flag = GetCasenum(prodefid, batchNumber, batchNumber, connection, null, null, request, number, "0");
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
	public String GetProject(String prodefid, String xmbh, String batchNumber, String casenum, String proinstid,
			String count, String num, HttpServletRequest request) {
		String flag = "false";
		Connection connection = null;
		try {
			connection = JH_DBHelper.getConnect_jy();
			flag = GetCasenum(prodefid, batchNumber, casenum, connection, xmbh, proinstid, request, num, count);
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

	private String GetCasenum(String prodefid, String batchNumber, String casenum, Connection connection, String xmbh,
			String id, HttpServletRequest request, String number, String con) throws Exception {
		String flag = "false";
		int num = Integer.parseInt(number);
		int count = Integer.parseInt(con);
		String getCasenumSql = "select 1 from HOUSE.PRO_HOUSEINFO where casenum ='" + casenum + "'";
		ResultSet isExistCasenum = JH_DBHelper.excuteQuery(connection, getCasenumSql);
		String file_Path = request.getSession().getServletContext().getRealPath("/");
		String servername = StringHelper.formatObject(InetAddress.getLocalHost().getHostAddress());
		String basePath = request.getScheme() + "://" + servername + ":" + request.getLocalPort()
				+ request.getContextPath() + "/";
		String staffid = Global.getCurrentUserInfo().getId();
		String url = basePath + "app/operation/batch/acceptproject/" + prodefid + "/" + batchNumber + "/" + staffid;
		Map<String, String> map = new HashMap<String, String>();
		while (isExistCasenum.next() && count < num) {
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
				id = object.containsKey("id") ? StringHelper.formatObject(object.get("id")) : "";
				// desc:消息 如果受理成功是 “受理成功”
				String desc = object.containsKey("desc") ? StringHelper.formatObject(object.get("desc")) : "";
				if (!desc.equals("受理成功")) {
					continue;
				}
				// 刘树峰 2016.3.17 创建项目 获取xmbh
				String project_id = null;
				Wfi_ProInst proinst = baseCommonDao.get(Wfi_ProInst.class, id);
				if (proinst != null) {
					project_id = proinst.getFile_Number();
				}
				if (!StringHelper.isEmpty(project_id)) {
					ProjectInfo info = ProjectHelper.GetProjectFromRest(project_id, request);
					xmbh = info.getXmbh();
				}
			}
			// 调用抽取数据方法
			ExtractFJFromZJK(id, casenum, file_Path);
			flag = ExtractSXFromZJK(casenum, xmbh, isCheckDY);
			if (flag.contains("警告：")) {
				return flag + '$' + xmbh + '$' + batchNumber + '$' + casenum + '$' + id + '$' + count + '$' + num;
			} else if (flag.equals("warning")) {
				return flag;
			} else if (!flag.equals("true")) {
				smProInstService.deleteProInst(id);
			}
			xmbh = null;
			id = null;
			casenum = String.valueOf(Long.parseLong(casenum) + 1);
			count += 1;
			getCasenumSql = "select 1 from HOUSE.PRO_HOUSEINFO where casenum ='" + casenum + "'";
			isExistCasenum = JH_DBHelper.excuteQuery(connection, getCasenumSql);
			// isExistCasenum.close();
		}
		isExistCasenum.close();
		return flag;
	}

	@Override
	public String getFcStatus(String casenum) {
		StringBuffer sb = new StringBuffer(casenum);
		String code = sb.substring(0, casenum.indexOf("-"));
		Connection connection = null;
		connection = JH_DBHelper.getConnect_jy();
		String names = "select * from WFD_PRODEF where code='" + code + "'";
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(names);
			ResultSet nameSet = pstmt.executeQuery();
			while (nameSet.next()) {
				String name = nameSet.getString("NAME");
				String sql = "";
				if (name.indexOf("转移") != -1 && name.indexOf("抵押") == -1) {// 转移
					sql = "select QRZT from TPF_BDC_JYQRS jy join WFMS_INST_BUSINESS ib on ib.businessid=jy.ywslid where acceptcode='"
							+ casenum + "'";
				} else {// 抵押
					sql = "select QRZT from TPF_BDC_DYQRS jy join WFMS_INST_BUSINESS ib on ib.businessid=jy.ywslid where acceptcode='"
							+ casenum + "'";
				}
				PreparedStatement pstmt2 = connection.prepareStatement(sql);
				ResultSet statusSet = pstmt2.executeQuery();
				while (statusSet.next()) {
					Integer status = statusSet.getInt("QRZT");
					List<BDCS_XMXX> xmxxs = baseCommonDao.getDataList(BDCS_XMXX.class, "FCYWH='" + casenum + "'");
					if (xmxxs != null && xmxxs.size() > 0) {
						BDCS_XMXX xmxx = xmxxs.get(0);
						xmxx.setFCJYZT(status.toString());
						baseCommonDao.update(xmxx);
					}
					// JH_DBHelper.excuteUpdate(connection,"update BDCS_XMXX set
					// FCJYZ='"+String.valueOf(status)+"' where
					// FCYWH='"+casenum+"'");
					code = String.valueOf(status);
				}
				if (pstmt2 != null) {
					pstmt2.close();
				}
				if (statusSet != null) {
					statusSet.close();
				}
			}
			if (nameSet != null) {
				nameSet.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != pstmt) {
					pstmt.close();
				}
				if (null != connection) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return code;
	}

	// 查封登记附属权利封装
	public List<Object> rightMapCF(ResultSet SQRSet, Connection connection, String casenum, String inst_business) {
		BDCS_QL_GZ ql = new BDCS_QL_GZ();
		BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
		List<BDCQLR> BDCQLR = new ArrayList<BDCQLR>();// 权利人集合
		List<JYQLR> JYQLR = new ArrayList<JYQLR>();
		List<Object> object = new ArrayList<Object>();
		String SQRsql = "select * from " + inst_business
				+ " b join TPU_YWSQR t on t.ywslid=b.BUSINESSID where b.acceptcode='" + casenum + "'";

		try {
			ResultSet bdcdyhSet = JH_DBHelper.excuteQuery(connection, SQRsql);
			// 申请人信息集合
			while (bdcdyhSet.next()) {
				JYQLR jyqlr = new JYQLR();
				jyqlr.setSQRLB("2");
				jyqlr.setQLRMC(bdcdyhSet.getString("SQRMC"));
				String fczjlx = bdcdyhSet.getString("SQRMC");
				if ("2000".equals(fczjlx)) {
					jyqlr.setZJZL("1");// 证件种类
				} else if ("2001".equals(fczjlx)) {
					jyqlr.setZJZL("4");
				} else if ("2002".equals(fczjlx)) {
					jyqlr.setZJZL("3");
				} else if ("2003".equals(fczjlx) || "2004".equals(fczjlx)) {
					jyqlr.setZJZL("5");
				} else if (null == fczjlx || "".equals(fczjlx)) {
					// 房产没有数据，这里就空着
				} else {
					jyqlr.setZJZL("99");
				}
				jyqlr.setZJH(bdcdyhSet.getString("ZJHM"));
				jyqlr.setGZDW(bdcdyhSet.getString("GZDWMC"));
				jyqlr.setDH(bdcdyhSet.getString("LXDH"));
				JYQLR.add(jyqlr);
			}
			bdcdyhSet.close();
			fsql.setQLID(ql.getId());
			// fsql.setCFWJ(SQRSet.getString("CFWJJWH"));
			fsql.setCFWH(SQRSet.getString("CFWJJWH"));
			fsql.setCFSJ(SQRSet.getDate("CFDJSJ"));// 查封时间
			fsql.setCFLX("3");// 查封类型，默认预查封，3
			// fsql.setCFFW(cFFW);//查封范围
			fsql.setJFWH(SQRSet.getString("JFWJJWH"));// 解封文号
			// fsql.setJFWH();//解封文件
			fsql.setJFJG(SQRSet.getString("JFJG"));// 解封机构
			fsql.setCFJG(SQRSet.getString("CFJG"));// 查封机关
			fsql.setLHSX(SQRSet.getInt("CFLXH"));// 查封轮序号
			ql.setQLLX("99");
			ql.setDJLX("800");

			object.add(ql);
			object.add(fsql);
			object.add(BDCQLR);
			object.add(JYQLR);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return object;
	}

	// 变更房屋ID集合 房管的变更类型:0新增, 1一对一变更, 2合并, 3分割 ,4多对多变更,-1删除
	public List<Object> getHList(ResultSet bdcdyhSet2, String bglx) throws Exception {
		List<Object> object = new ArrayList<Object>();
		List<JYH> HList = new ArrayList<JYH>();
		List<JYJYBGQH> yfwidlist = new ArrayList<JYJYBGQH>();
		List<String> list = new ArrayList<String>();
		try {
			// String fwids="select * from tps_fwbg where bgxh='"+bgxh+"'";
			// ResultSet ids = JH_DBHelper.excuteQuery(connection, fwids);
			while (bdcdyhSet2.next()) {
				JYH y = new JYH();
				JYJYBGQH yfwid = new JYJYBGQH();

				if (null != bdcdyhSet2.getString("FWID") && !"".equals(bdcdyhSet2.getString("FWID"))) {
					String fwid = bdcdyhSet2.getString("FWID");// 600686204
					y.setRELATIONID(fwid);
					HList.add(y);
				}
				if (null != bdcdyhSet2.getString("YFWID") && !"".equals(bdcdyhSet2.getString("YFWID"))) {
					yfwid.setRELATIONID(bdcdyhSet2.getString("YFWID"));
					yfwidlist.add(yfwid);
				}
				if ("1".equals(bglx)) {// 如果是期房转现房，只需要现房的房屋ID,调用公用方法时候传参数是List<String>
										// 这里只为调用公用方法才要放List<String>
					list.add(bdcdyhSet2.getString("XFWID"));
				}
			}
			bdcdyhSet2.close();
			object.add(HList);
			object.add(yfwidlist);
			object.add(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return object;
	}

	// 原来房屋ID集合 变更大类用到
	public List<JYJYBGQH> yfwid(Connection connection, String bdcdyhsql) throws Exception {
		List<JYJYBGQH> fcidYlist = new ArrayList<JYJYBGQH>();
		try {
			ResultSet bdcdyhSet = JH_DBHelper.excuteQuery(connection, bdcdyhsql);
			while (bdcdyhSet.next()) {
				JYJYBGQH y = new JYJYBGQH();
				y.setRELATIONID(bdcdyhSet.getString("YFWID"));
				fcidYlist.add(y);
			}
			bdcdyhSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fcidYlist;
	}

	//初始化map常量
	protected void initialMap() {
		try {
			if (ghytMap.size() == 0) {
				ghytMap.put("9", "30");
				ghytMap.put("10", "10");
				ghytMap.put("11", "11");
				ghytMap.put("12", "12");
				ghytMap.put("13", "13");
				ghytMap.put("14", "");
				ghytMap.put("15", "");
				ghytMap.put("16", "");
				ghytMap.put("17", "84");
				ghytMap.put("18", "");
				ghytMap.put("19", "");
				ghytMap.put("20", "");
				ghytMap.put("21", "");
				ghytMap.put("22", "22");
				ghytMap.put("23", "23");
				ghytMap.put("24", "24");
				ghytMap.put("25", "25");
				ghytMap.put("26", "26");
				ghytMap.put("27", "27");
				ghytMap.put("28", "");
				ghytMap.put("29", "");
				ghytMap.put("30", "");
				ghytMap.put("31", "31");
				ghytMap.put("32", "32");
				ghytMap.put("33", "33");
				ghytMap.put("34", "34");
				ghytMap.put("35", "35");
				ghytMap.put("36", "");
				ghytMap.put("37", "");
				ghytMap.put("41", "41");
				ghytMap.put("42", "40");
				ghytMap.put("43", "40");
				ghytMap.put("51", "51");
				ghytMap.put("52", "52");
				ghytMap.put("53", "53");
				ghytMap.put("54", "54");
				ghytMap.put("55", "55");
				ghytMap.put("61", "60");
				ghytMap.put("71", "70");
				ghytMap.put("81", "81");
				ghytMap.put("82", "82");
				ghytMap.put("83", "83");
				ghytMap.put("91", "");
				ghytMap.put("92", "");

			}
			if (fwxzMap.size() == 0) {
				fwxzMap.put("3901", "0");
				fwxzMap.put("3902", "6");
				fwxzMap.put("3903", "3");
				fwxzMap.put("3904", "4");
				fwxzMap.put("3905", "");// 存量房產
				fwxzMap.put("3906", "");
				fwxzMap.put("3907", "");
				fwxzMap.put("3908", "");// 农居公寓
				fwxzMap.put("3909", "");
				fwxzMap.put("3910", "");// 房改房
				fwxzMap.put("3911", "5");
				fwxzMap.put("3912", "");// 直管公房
				fwxzMap.put("3914", "8");
				fwxzMap.put("3915", "");// 解困房
				fwxzMap.put("3916", "");// 安居房
				fwxzMap.put("3917", "");// 單位自建房
				fwxzMap.put("3918", "");// 集体土地个人自建房
				fwxzMap.put("3919", "");// 国有土地个人自建房
				fwxzMap.put("3999", "99");// 其他
				fwxzMap.put("9999", "");// 待定

			}
		} catch (Exception ex) {
		}
	}

	String bljcString;
	public String getBljcString() {
		return bljcString;
	}

	public void setBljcString(String bljcString) {
		this.bljcString = bljcString;
	}

	// 楼盘表确认业务
	public List<Object> rightMapSC(ResultSet SQRSet, String name, Connection connection, String inst_business) {
		BDCS_QL_GZ ql = new BDCS_QL_GZ();
		BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
		List<Object> object = new ArrayList<Object>();
		List<BDCQLR> BDCQLR = new ArrayList<BDCQLR>();// 权利人集合
		List<BDCQLR> dyqlrlist = new ArrayList<BDCQLR>();

		List<JYQLR> JYQLR = new ArrayList<JYQLR>();
		List<JYQLR> JYQLRlist = new ArrayList<JYQLR>();
		JYQLR jyqlr = new JYQLR();
		new SimpleDateFormat("yyyyMMdd");
		try {
			fsql.setQLID(ql.getId());
			ql.setQLLX("99");// 预告现房99 期房4
			ql.setDJLX("100");
			String qlrsql = "select * from tpf_qlr_gzk  qlr where qlr.ywslid='" + SQRSet.getString("ywslid") + "'";
			PreparedStatement pstmt = connection.prepareStatement(qlrsql);
			ResultSet qlrSET = null;
			qlrSET = pstmt.executeQuery();
			String qlrID = "";
			while (qlrSET.next()) {

				if (qlrID.equals(qlrSET.getString("SQRID"))) {
					break;
				} else {
					qlrID = qlrSET.getString("SQRID");
					BDCQLR qlr = new BDCQLR();
					qlr.setQLID(ql.getId());
					qlr.setQLRMC(qlrSET.getString("QLRMC"));// 权利人名称
					qlr.setFDDBR(qlrSET.getString("FDDBRMC"));// 法定代表人
					qlr.setDZ(qlrSET.getString("DZ"));// 地址
					qlr.setDH(qlrSET.getString("LXDH"));// 电话
					qlr.setZJH(qlrSET.getString("ZJHM"));// 证件号码、
					String fczjlx = qlrSET.getString("ZJLX");
					String zyfe = "100";
					if ("2000".equals(fczjlx)) {
						qlr.setZJZL("1");// 证件种类
					} else if ("2013".equals(fczjlx)) {
						qlr.setZJZL("7");
					} else if ("2014".equals(fczjlx)) {
						qlr.setZJZL("6");
					} else if ("2001".equals(fczjlx)) {
						qlr.setZJZL("4");
					} else if ("2002".equals(fczjlx)) {
						qlr.setZJZL("3");
					} else if ("2003".equals(fczjlx) || "2004".equals(fczjlx)) {
						qlr.setZJZL("5");
					} else if (null == fczjlx || "".equals(fczjlx)) {
						// 房产没有数据，这里就空着
					} else {
						qlr.setZJZL("99");
					}
					if ("2601".equals(qlrSET.getString("QLRXZ")) || "4".equals(qlrSET.getString("QLRXZ"))) {// --------------------------房管只有两种类型
						qlr.setQLRLX("1");// 个人

					} else if ("2606".equals(qlrSET.getString("QLRXZ")) || "2602".equals(qlrSET.getString("QLRXZ"))) {
						qlr.setQLRLX("2");// 企业

					} else if ("0".equals(qlrSET.getString("QLRXZ")) && qlr.getQLRMC() != null
							&& (qlr.getQLRMC().contains("银行") || qlr.getQLRMC().contains("公司"))) {
						qlr.setQLRLX("2");// 企业
					} else {
						qlr.setQLRLX("99");
					}
					qlr.setGJ(qlrSET.getString("GJ"));// 国家
					qlr.setHJSZSS("HJSZD");// 户籍所在地
					qlr.setYB(qlrSET.getString("YZBM"));// 邮编
					qlr.setDZYJ(qlrSET.getString("EMAIL"));// 邮箱

					dyqlrlist.add(qlr);

				}
			}
			String ywrsql = "select qlr.*,ql.* from " + inst_business
					+ " b left join TPF_QLR_GZK qlr on qlr.ywslid=b.businessid left join TPF_ZYDJYW ql on ql.ywslid=b.businessid where b.businessid='"
					+ SQRSet.getString("ywslid")
					+ "' and (qlr.QLRLX!='2' AND qlr.QLRLX!='4' AND qlr.QLRLX!='7' AND qlr.QLRLX!='10')";
			ResultSet ywrSET = JH_DBHelper.excuteQuery(connection, ywrsql);
			
			JYQLR.addAll(GetFcYwr(ywrSET, "zy"));

			BDCQLR.addAll(dyqlrlist);// 封装权利人
			object.add(ql);
			object.add(fsql);
			object.add(BDCQLR);
			object.add(JYQLR);
			qlrSET.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}

	/** 
	* @author  buxiaobo
	* @date 创建时间：2017年4月14日 下午3:08:10 
	* @version 校验内容为字符个数、符号类型，“-”符号为英文 房产业务号格式：16102120-170201-1652571”
	* @parameter  
	* @since  
	* @return  
	*/
	private String CheckCasenum(String casenum) {
		String flag="true";
		String xzqhdm=ConfigHelper.getNameByValue("XZQHDM");
		if(xzqhdm.equals("650100")&&casenum!=null){
			int s=casenum.indexOf("&");
			casenum=casenum.substring(0,s);
			if(casenum.length()!="16102120-170201-1652571".length()||!casenum.substring(8,9).equals("-")||!casenum.substring(15,16).equals("-")){
				flag="formalerror";
				return flag;
			}
		}
		return flag;
	}
}
