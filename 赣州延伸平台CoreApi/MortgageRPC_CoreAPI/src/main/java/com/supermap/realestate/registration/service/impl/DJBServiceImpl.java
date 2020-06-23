package com.supermap.realestate.registration.service.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
















import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.registration.ViewClass.DJBDTO;
import com.supermap.realestate.registration.ViewClass.FDCQDB;
import com.supermap.realestate.registration.ViewClass.HYDB;
import com.supermap.realestate.registration.ViewClass.NYDDB;
import com.supermap.realestate.registration.ViewClass.NoticeBook;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.SLLMDB;
import com.supermap.realestate.registration.ViewClass.SYQZD;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.mapping.HandlerMapping.RegisterWorkFlow;
import com.supermap.realestate.registration.model.BDCS_CONST;
import com.supermap.realestate.registration.model.BDCS_C_XZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_GZY;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_LOG_QZH;
import com.supermap.realestate.registration.model.BDCS_NYD_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_TDYT_GZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_XM_DYXZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.interfaces.Building;
import com.supermap.realestate.registration.model.interfaces.Forest;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.interfaces.TDYT;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.service.DJBService;
import com.supermap.realestate.registration.service.ZSService;
import com.supermap.realestate.registration.tools.NewLogTools;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.DateUtil;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate_gx.registration.util.StringUtil;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.tree.Tree;
import org.springframework.transaction.annotation.Transactional;

@Service("djbService")
public class DJBServiceImpl implements DJBService {

	@Autowired
	private CommonDao baseCommonDao;

	/** 证书service */
	@Autowired
	private ZSService zsService;

	/**
	 * 获取海域登簿信息
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月26日上午5:31:01
	 * @param xmbh
	 * @param qlid
	 * @return
	 */
	@Override
	public HYDB GetHYDB(String xmbh, String qlid) {
		HYDB hydb = new HYDB();
		// 1.权利人相关信息
		BDCS_QLR_GZ qlr = new BDCS_QLR_GZ();
		qlr.setQLID(qlid);
		qlr.setXMBH(xmbh);

		// 根据项目编号和权利ID查询权利人列表
		StringBuilder builder = new StringBuilder();
		builder.append(" XMBH='").append(xmbh).append("' AND QLID='").append(qlid).append("'");
		List<BDCS_QLR_GZ> qlrs = baseCommonDao.getDataList(BDCS_QLR_GZ.class, builder.toString());
		String qlrStr = "";// 权利人
		String zjlx = "";// 证件类型
		String zjhm = "";// 证件号码
		for (int i = 0; i < qlrs.size(); i++) {
			if (i == 0) {
				hydb.setQlrlx(qlrs.get(i).getQLRLXName());// 权利人类型
			}
			if (qlrs.size() - 1 == i) {
				qlrStr += qlrs.get(i).getQLRMC();
				zjlx += qlrs.get(i).getZJZLName(); // 证件类型
				zjhm += qlrs.get(i).getZJH();
			} else {
				qlrStr += qlrs.get(i).getQLRMC() + ",";
				zjlx += qlrs.get(i).getZJZLName() + ",";
				zjhm += qlrs.get(i).getZJH() + ",";
			}
		}
		if (qlrs.size() > 1) {
			hydb.setGyqk("共同共有");
		} else {
			hydb.setGyqk("单独所有");
		}
		hydb.setQlr(qlrStr);
		hydb.setZjlx(zjlx);
		hydb.setZjhm(zjhm);
		// 2、获取宗海的需要信息
		String fulSql = MessageFormat.format(
				"select * from BDCK.BDCS_ZH_GZ WHERE BDCDYID IN(select BDCDYID from  BDCK.BDCS_DJDY_GZ WHERE  DJDYID IN (select DJDYID from  BDCK.BDCS_QL_GZ WHERE QLID=''{0}''))",
				qlid);
		@SuppressWarnings("rawtypes")
		List<Map> lstmap = baseCommonDao.getDataListByFullSql(fulSql);
		if (lstmap != null && lstmap.size() > 0) {
			if (lstmap.get(0).get("ZHMJ") != null)
				hydb.setSyqmj(StringHelper.formatObject(lstmap.get(0).get("ZHMJ")));
			if (lstmap.get(0).get("SYJZE") != null)
				hydb.setSyjze(StringHelper.formatObject(lstmap.get(0).get("SYJZE")));
			if (lstmap.get(0).get("SYJBZYJ") != null)
				hydb.setSyjbzyj(StringHelper.formatObject(lstmap.get(0).get("SYJBZYJ")));
			if (lstmap.get(0).get("SYJJNQK") != null)
				hydb.setSyjjnqk(StringHelper.formatObject(lstmap.get(0).get("SYJJNQK")));
			if (lstmap.get(0).get("ZL") != null)
				hydb.setZl(StringHelper.formatObject(lstmap.get(0).get("ZL")));
		}
		// 3.权利相关信息
		BDCS_QL_GZ ql = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
		if (ql != null) {
			DateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");// 日期向字符串转换，可以设置任意的转换格式format
			if (null != ql.getDJLX()) {
				hydb.setDjlx(ConstHelper.getNameByValue("DJLX", StringHelper.formatObject(ql.getDJLX()))); // 登记类型
			}
			if (null != ql.getDJYY()) {
				hydb.setDjyy(StringHelper.formatObject(ql.getDJYY()));
			}
			if (null != ql.getQLQSSJ() && null != ql.getQLJSSJ()) {
				hydb.setSyqx(sdf.format(ql.getQLQSSJ()) + "/" + sdf.format(ql.getQLJSSJ()));
			}
			if (null != ql.getBDCQZH()) {
				hydb.setBdcqzh(StringHelper.formatObject(ql.getBDCQZH()));
			}
			if (null != ql.getDJSJ()) {
				hydb.setDjsj(StringHelper.FormatYmdhmsByDate(ql.getDJSJ()));
			}
			if (null != ql.getDBR()) {
				hydb.setDbr(StringHelper.formatObject(ql.getDBR()));
			}
			if (null != ql.getFJ()) {
				hydb.setFj(StringHelper.formatObject(ql.getFJ()));
			}
			if (null != ql.getBDCDYH()) {
				hydb.setBdcdyh(StringHelper.formatObject(ql.getBDCDYH()));
			}
			if (null != ql.getQXDM()) {
				hydb.setQxdm(StringHelper.formatObject(ql.getQXDM()));
			}
			if (null != ql.getXMBH()) {
				hydb.setXmbh(StringHelper.formatObject(ql.getXMBH()));
			}
		}
		// 从具体单元获取不动产单元号-sun-2015-11-17
		String bdcdyh = getRealUnitBdcdyh(qlid, xmbh);
		hydb.setBdcdyh(bdcdyh);
		return hydb;
	}

	/**
	 * 获取森林林木登记信息
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月27日下午11:09:20
	 * @param xmbh
	 * @param qlid
	 * @return
	 */
	@Override
	public SLLMDB GetSLLMDB(String xmbh, String qlid) {
		SLLMDB lddb = new SLLMDB();
		// 根据项目编号和权利ID查询权利人列表
		StringBuilder builder = new StringBuilder();
		builder.append(" XMBH='").append(xmbh).append("' AND QLID='").append(qlid).append("'");
		List<BDCS_QLR_GZ> qlrs = baseCommonDao.getDataList(BDCS_QLR_GZ.class, builder.toString());
		String qlrStr = "";// 权利人
		String zjlx = "";// 证件类型
		String zjhm = "";// 证件号码
		for (int i = 0; i < qlrs.size(); i++) {
			if (i == 0) {
				lddb.setQlrlx(qlrs.get(i).getQLRLXName()); // 权利人类型
			}
			if (qlrs.size() - 1 == i) {
				qlrStr += qlrs.get(i).getQLRMC();
				zjlx += qlrs.get(i).getZJZLName(); // 证件类型

				zjhm += StringHelper.FormatByDatatype(qlrs.get(i).getZJH());
			} else {
				qlrStr += qlrs.get(i).getQLRMC() + ",";
				zjlx += qlrs.get(i).getZJZLName() + ",";
				zjhm += StringHelper.FormatByDatatype(qlrs.get(i).getZJH()) + ",";
			}
		}
		if (qlrs.size() > 1) {
			lddb.setGyqk("共同共有");// 林地共有情况
		} else {
			lddb.setGyqk("单独所有");
		}
		lddb.setQlr(qlrStr);// 林地权利人
		lddb.setZjlx(zjlx);// 证件种类
		lddb.setZjh(zjhm);// 证件号
		// 2.权利相关信息
		Rights ql=RightsTools.loadRights(DJDYLY.GZ, qlid);
		if (ql != null) {
			DateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");// 日期向字符串转换，可以设置任意的转换格式format
			if (null != ql.getDJLX()) {
				lddb.setDjlx(ConstHelper.getNameByValue("DJLX", StringHelper.formatObject(ql.getDJLX())));// 登记类型
			}
			if (null != ql.getDJYY()) {
				lddb.setDjyy(StringHelper.formatObject(ql.getDJYY()));// 登记原因
			}
			if (null != ql.getQLQSSJ() && null != ql.getQLJSSJ()) {
				lddb.setSyqx(sdf.format(ql.getQLQSSJ()) + "/" + sdf.format(ql.getQLJSSJ()));// 林地使用(承包)期限
			}
			if (null != ql.getBDCQZH()) {
				lddb.setBdcqzh(StringHelper.formatObject(ql.getBDCQZH()));// 不动产权证书号
			}
			if (null != ql.getDJSJ()) {
				lddb.setDjsj(StringHelper.FormatYmdhmsByDate(ql.getDJSJ()));// 登记时间
			}
			if (null != ql.getDBR()) {
				lddb.setDbr(StringHelper.formatObject(ql.getDBR()));// 登簿人
			}
			if (null != ql.getFJ()) {
				lddb.setFj(StringHelper.formatObject(ql.getFJ()));// 附记
			}
			if (null != ql.getBDCDYH()) {
				lddb.setBdcdyh(StringHelper.formatObject(ql.getBDCDYH()));
			}
			if (null != ql.getQXDM()) {
				lddb.setQxdm(StringHelper.formatObject(ql.getQXDM()));
			}
			if (null != ql.getXMBH()) {
				lddb.setXmbh(StringHelper.formatObject(ql.getXMBH()));
			}
			SubRights fsql=null;
			if(!StringHelper.isEmpty(ql.getFSQLID())){
				fsql=RightsTools.loadSubRights(DJDYLY.GZ, ql.getFSQLID());
			}
			if(fsql==null){
				fsql=RightsTools.loadSubRightsByRightsID(DJDYLY.GZ, qlid);
			}
			if (fsql != null) {
				if (fsql.getLDSYQXZ() != null) {
					lddb.setLdsyqxz(
							ConstHelper.getNameByValue("TDSYQXZ", StringHelper.formatObject(fsql.getLDSYQXZ())));
				}
				if (fsql.getSLLMSYQR1() != null) {
					lddb.setSyq(StringHelper.formatObject(fsql.getSLLMSYQR1()));
				}
				if (fsql.getSLLMSYQR2() != null) {
					lddb.setShyq(StringHelper.formatObject(fsql.getSLLMSYQR2()));
				}
				if ("10".equals(ql.getQLLX())) {
					lddb.setGyqk(fsql.getGYRQK());
				}
			}
			// 3.单元相关信息
			String djdyid=ql.getDJDYID();
			List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "DJDYID='"+djdyid+"' AND XMBH='"+xmbh+"'");
			if(djdys!=null&&djdys.size()>0){
				RealUnit unit=UnitTools.loadUnit(BDCDYLX.initFrom(djdys.get(0).getBDCDYLX()), DJDYLY.initFrom(djdys.get(0).getLY()), djdys.get(0).getBDCDYID());
				if(unit!=null){
					Forest forest=(Forest)unit;
					lddb.setSyqmj(StringHelper.formatObject(forest.getSYQMJ()));
					String strzysz = "";
					if (StringHelper.isEmpty(forest.getZYSZ())) {
						List<BDCS_TDYT_GZ> lsttdyt = baseCommonDao.getDataList(BDCS_TDYT_GZ.class,
								"BDCDYID='" + forest.getId() + "' ");
						boolean flag = true;
						for (TDYT tdyt : lsttdyt) {
							if (!StringHelper.isEmpty(tdyt.getZYSZ())) {
								if (flag ) {
									strzysz = tdyt.getZYSZ();
									flag = false;
								} else {
									strzysz = strzysz + "、" + tdyt.getZYSZ();
								}
							}
						}
						lddb.setZysz(StringHelper.formatObject(strzysz));
					}else {
						lddb.setZysz(StringHelper.formatObject(forest.getZYSZ()));
					}
					lddb.setZs(StringHelper.formatObject(forest.getZS()));
					String lz = ConstHelper.getNameByValue("LZ", StringHelper.formatObject(forest.getLZ()));
					lddb.setLz(lz);
					String qy = ConstHelper.getNameByValue("QY", StringHelper.formatObject(forest.getQY()));
					lddb.setQy(qy);
					lddb.setZlnd(StringHelper.formatObject(forest.getZLND()));
					lddb.setXdm(StringHelper.formatObject(forest.getXDM()));
					lddb.setLb(StringHelper.formatObject(forest.getLB()));
					lddb.setXb(StringHelper.formatObject(forest.getXB()));
					lddb.setBdcdyh(forest.getBDCDYH());
					lddb.setZl(forest.getZL());
				}
			}
		}
		return lddb;
	}

	/**
	 * 集体土地所有权登簿
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月26日下午6:07:43
	 * @param xmbh
	 * @param qlid
	 * @return
	 */
	@Override
	public SYQZD GetJTTDSYQDB(String xmbh, String qlid) {
		SYQZD jttdsyqdb = new SYQZD();
		// 1.权利人相关信息
		BDCS_QLR_GZ qlr = new BDCS_QLR_GZ();
		qlr.setQLID(qlid);
		qlr.setXMBH(xmbh);

		// 根据项目编号和权利ID查询权利人列表
		StringBuilder builder = new StringBuilder();
		builder.append(" XMBH='").append(xmbh).append("' AND QLID='").append(qlid).append("'");
		List<BDCS_QLR_GZ> qlrs = baseCommonDao.getDataList(BDCS_QLR_GZ.class, builder.toString());
		String qlrStr = "";// 权利人
		String zjlx = "";// 证件类型
		String zjhm = "";// 证件号码
		for (int i = 0; i < qlrs.size(); i++) {
			if (qlrs.size() - 1 == i) {
				qlrStr += qlrs.get(i).getQLRMC();
				// 证件类型
				zjlx += qlrs.get(i).getZJZLName();
				zjhm += qlrs.get(i).getZJH();
			} else {
				qlrStr += qlrs.get(i).getQLRMC() + ",";
				zjlx += qlrs.get(i).getZJZLName() + ",";
				zjhm += qlrs.get(i).getZJH() + ",";
			}
		}
		if (qlrs.size() > 1) {
			jttdsyqdb.setGyqk("共同共有");
		} else {
			jttdsyqdb.setGyqk("单独所有");
		}
		jttdsyqdb.setQlr(qlrStr);
		jttdsyqdb.setZjlx(zjlx);
		jttdsyqdb.setZjhm(zjhm);
		// 2、获取宗海的需要信息
		String fulSql = MessageFormat.format(
				"select * from BDCK.BDCS_SYQZD_GZ WHERE BDCDYID IN(select BDCDYID from  BDCK.BDCS_DJDY_GZ WHERE  DJDYID IN (select DJDYID from  BDCK.BDCS_QL_GZ WHERE QLID=''{0}''))",
				qlid);
		@SuppressWarnings("rawtypes")
		List<Map> lstmap = baseCommonDao.getDataListByFullSql(fulSql);
		if (lstmap != null && lstmap.size() > 0) {
			if (lstmap.get(0).get("NYDMJ") != null) {
				jttdsyqdb.setNyd(StringHelper.formatObject(lstmap.get(0).get("NYDMJ")));
			}
			if (lstmap.get(0).get("GDMJ") != null) {
				jttdsyqdb.setGd(StringHelper.formatObject(lstmap.get(0).get("GDMJ")));
			}
			if (lstmap.get(0).get("LDMJ") != null) {
				jttdsyqdb.setLd(StringHelper.formatObject(lstmap.get(0).get("LDMJ")));
			}
			if (lstmap.get(0).get("CDMJ") != null) {
				jttdsyqdb.setCd(StringHelper.formatObject(lstmap.get(0).get("CDMJ")));
			}
			if (lstmap.get(0).get("QTNYDMJ") != null) {
				jttdsyqdb.setQt(StringHelper.formatObject(lstmap.get(0).get("QTNYDMJ")));
			}
			if (lstmap.get(0).get("JSYDMJ") != null) {
				jttdsyqdb.setJsyd(StringHelper.formatObject(lstmap.get(0).get("JSYDMJ")));
			}
			if (lstmap.get(0).get("WLYDMJ") != null) {
				jttdsyqdb.setWlyd(StringHelper.formatObject(lstmap.get(0).get("WLYDMJ")));
			}

		}
		// 3.权利相关信息
		BDCS_QL_GZ ql = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
		if (ql != null) {
			// DateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");//
			// 日期向字符串转换，可以设置任意的转换格式format
			if (null != ql.getDJLX()) {
				// 登记类型
				jttdsyqdb.setDjlx(ConstHelper.getNameByValue("DJLX", StringHelper.formatObject(ql.getDJLX())));
			}
			if (null != ql.getDJYY()) {
				jttdsyqdb.setDjyy(StringHelper.formatObject(ql.getDJYY()));
			}
			if (null != ql.getBDCQZH()) {
				jttdsyqdb.setBdcqzh(StringHelper.formatObject(ql.getBDCQZH()));
			}
			if (null != ql.getDJSJ()) {
				jttdsyqdb.setDjsj(StringHelper.FormatYmdhmsByDate(ql.getDJSJ()));
			}
			if (null != ql.getDBR()) {
				jttdsyqdb.setDbr(StringHelper.formatObject(ql.getDBR()));
			}
			if (null != ql.getFJ()) {
				jttdsyqdb.setFj(StringHelper.formatObject(ql.getFJ()));
			}
			if (null != ql.getBDCDYH()) {
				jttdsyqdb.setBdcdyh(StringHelper.formatObject(ql.getBDCDYH()));
			}
			if (null != ql.getQXDM()) {
				jttdsyqdb.setQxdm(StringHelper.formatObject(ql.getQXDM()));
			}
			if (null != ql.getXMBH()) {
				jttdsyqdb.setXmbh(StringHelper.formatObject(ql.getXMBH()));
			}
		}
		// 从具体单元获取不动产单元号-sun-2015-11-17
		String bdcdyh = getRealUnitBdcdyh(qlid, xmbh);
		jttdsyqdb.setBdcdyh(bdcdyh);
		return jttdsyqdb;
	}

	@Override
	public NYDDB GetNYDSHYQDB(String xmbh, String qlid) {
		NYDDB nyddb = new NYDDB();
		// 1.权利人相关信息
		BDCS_QLR_GZ qlr = new BDCS_QLR_GZ();
		qlr.setQLID(qlid);
		qlr.setXMBH(xmbh);

		// 根据项目编号和权利ID查询权利人列表
		StringBuilder builder = new StringBuilder();
		builder.append(" XMBH='").append(xmbh).append("' AND QLID='").append(qlid).append("'");
		List<BDCS_QLR_GZ> qlrs = baseCommonDao.getDataList(BDCS_QLR_GZ.class, builder.toString());

		String qlrStr = "";// 权利人
		String zjlx = "";// 证件类型
		String zjhm = "";// 证件号码
		for (int i = 0; i < qlrs.size(); i++) {
			if (i == 0) {
				// 权利人类型
				nyddb.setQlrlx(ConstHelper.getNameByValue("QLRLX", qlrs.get(i).getQLRLX()));
			}
			if (qlrs.size() - 1 == i) {
				qlrStr += qlrs.get(i).getQLRMC();
				zjlx += ConstHelper.getNameByValue("ZJLX", qlrs.get(i).getZJZL());// 证件类型
				if (qlrs.get(i).getZJH() != null) {
					zjhm += qlrs.get(i).getZJH();
				}
			} else {
				qlrStr += qlrs.get(i).getQLRMC() + ",";
				zjlx += ConstHelper.getNameByValue("ZJLX", qlrs.get(i).getZJZL()) + ",";
				if (qlrs.get(i).getZJH() != null) {
					zjhm += qlrs.get(i).getZJH() + ",";
				}
			}
		}
		if (zjhm.length() > 0 && zjhm.endsWith(",")) {
			zjhm = zjhm.substring(0, zjhm.length() - 1);
		}
		if (qlrs.size() > 1) {
			nyddb.setGyqk("共同共有");
		} else {
			if (qlrs.size() == 1) {
				nyddb.setGyqk("单独所有");
			}
		}
		nyddb.setQlr(qlrStr);
		nyddb.setZjlx(zjlx);
		nyddb.setZjhm(zjhm);

		// 2、获取农用地的需要信息
		String fulSql = "";
		BDCS_QL_GZ ql_1  = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
		if(ql_1 != null){
			if(ConstValue.DJLX.ZYDJ.Value.equals(ql_1.getDJLX())|| ConstValue.DJLX.GZDJ.Value.equals(ql_1.getDJLX())){
				
				fulSql = MessageFormat.format(
						"select * from BDCK.BDCS_NYD_XZ WHERE BDCDYID IN(select BDCDYID from  BDCK.BDCS_DJDY_GZ WHERE  DJDYID IN (select DJDYID from  BDCK.BDCS_QL_GZ WHERE QLID=''{0}''))",
						qlid);
			}else{
				fulSql = MessageFormat.format(
						"select * from BDCK.BDCS_NYD_GZ WHERE BDCDYID IN(select BDCDYID from  BDCK.BDCS_DJDY_GZ WHERE  DJDYID IN (select DJDYID from  BDCK.BDCS_QL_GZ WHERE QLID=''{0}''))",
						qlid);
			}
		}else{
			fulSql = MessageFormat.format(
					"select * from BDCK.BDCS_NYD_GZ WHERE BDCDYID IN(select BDCDYID from  BDCK.BDCS_DJDY_GZ WHERE  DJDYID IN (select DJDYID from  BDCK.BDCS_QL_GZ WHERE QLID=''{0}''))",
					qlid);
		}
		@SuppressWarnings("rawtypes")
		List<Map> lstmap = baseCommonDao.getDataListByFullSql(fulSql);
		if (lstmap != null && lstmap.size() > 0) {
			if (lstmap.get(0).get("CBMJ") != null) {
				nyddb.setCbmj(StringHelper.formatObject(lstmap.get(0).get("CBMJ")));
			}
			if (lstmap.get(0).get("TDSYQXZ") != null) {
				nyddb.setTdsyqxz(ConstHelper.getNameByValue("TDSYQXZ",StringHelper.formatObject(lstmap.get(0).get("TDSYQXZ"))));
			}
			if (lstmap.get(0).get("SYTTLX") != null) {
				nyddb.setSyttlx(ConstHelper.getNameByValue("SYTTLX",StringHelper.formatObject(lstmap.get(0).get("SYTTLX"))));
			}
			if (lstmap.get(0).get("YZYFS") != null) {
				nyddb.setYzyfs(ConstHelper.getNameByValue("YZYFS",StringHelper.formatObject(lstmap.get(0).get("YZYFS"))));
			}
			if (lstmap.get(0).get("CYZL") != null) {
				nyddb.setCyzl(StringHelper.formatObject(lstmap.get(0).get("CYZL")));
			}
			if (lstmap.get(0).get("SYZCL") != null) {
				nyddb.setSyzcl(StringHelper.formatObject(lstmap.get(0).get("SYZCL")));
			}
			if (lstmap.get(0).get("FBFMC") != null) {
				nyddb.setFbf(StringHelper.formatObject(lstmap.get(0).get("FBFMC")));
			}
			if (lstmap.get(0).get("CBQSSJ") != null ||lstmap.get(0).get("CBJSSJ") != null ) {
				nyddb.setSyqx(StringHelper.FormatByDatetime(lstmap.get(0).get("CBQSSJ"))+"起"+StringHelper.FormatByDatetime(lstmap.get(0).get("CBJSSJ"))+"止");
			}
			
		}

		// 3.权利相关信息
		BDCS_QL_GZ ql = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
		if (ql != null) {
			// DateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");//
			// 日期向字符串转换，可以设置任意的转换格式format
			if (null != ql.getDJLX()) {
				// 登记类型
				nyddb.setDjlx(ConstHelper.getNameByValue("DJLX", StringHelper.formatObject(ql.getDJLX())));
			}
			if (null != ql.getDJYY()) {
				nyddb.setDjyy(StringHelper.formatObject(ql.getDJYY()));
			}
			if (null != ql.getQLQSSJ() && null != ql.getQLJSSJ()) {
				DateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");// 日期向字符串转换，可以设置任意的转换格式format
				nyddb.setSyqx(sdf.format(ql.getQLQSSJ()) + "/" + sdf.format(ql.getQLJSSJ()));
			}
			if (null != ql.getBDCQZH()) {
				nyddb.setBdcqzh(StringHelper.formatObject(ql.getBDCQZH()));
			}
			if (null != ql.getDJSJ()) {
				nyddb.setDjsj(StringHelper.FormatYmdhmsByDate(ql.getDJSJ()));
			}
			if (null != ql.getDBR()) {
				nyddb.setDbr(StringHelper.formatObject(ql.getDBR()));
			}
			if (null != ql.getFJ()) {
				nyddb.setFj(StringHelper.formatObject(ql.getFJ()));
			}
			if (null != ql.getBDCDYH()) {
				nyddb.setBdcdyh(StringHelper.formatObject(ql.getBDCDYH()));
			}
			if (null != ql.getQXDM()) {
				nyddb.setQxdm(StringHelper.formatObject(ql.getQXDM()));
			}
			if (null != ql.getXMBH()) {
				nyddb.setXmbh(StringHelper.formatObject(ql.getXMBH()));
			}
		}
		// 从具体单元获取不动产单元号-sun-2015-11-17
		String bdcdyh = getRealUnitBdcdyh(qlid, xmbh);		
		nyddb.setBdcdyh(bdcdyh);
		return nyddb;
	}

	/**
	 * 获取建设用地使用权登记簿页信息
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月22日下午5:41:35
	 * @param xmbh
	 * @param qlid
	 * @return
	 */
	@Override
	public DJBDTO GetDJB(String xmbh, String qlid) {
		DJBDTO djbDTO = new DJBDTO();
		// 1.权利人相关信息
		BDCS_QLR_GZ qlr = new BDCS_QLR_GZ();
		qlr.setQLID(qlid);
		qlr.setXMBH(xmbh);

		// 根据项目编号和权利ID查询权利人列表
		StringBuilder builder = new StringBuilder();
		builder.append(" XMBH='").append(xmbh).append("' AND QLID='").append(qlid).append("'");
		List<BDCS_QLR_GZ> qlrs = baseCommonDao.getDataList(BDCS_QLR_GZ.class, builder.toString());
		// 2.权利相关信息
		BDCS_QL_GZ ql = new BDCS_QL_GZ();
		ql.setId(qlid);
		djbDTO = this.GetDJBDTOFromQL(ql);
		BDCS_QL_GZ ql_1 = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
		// 不动产单元号获取
		if (ql_1 != null && !StringHelper.isEmpty(ql_1.getDJDYID())) {
			List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(BDCS_DJDY_GZ.class,
					"DJDYID='" + ql_1.getDJDYID() + "' AND XMBH='" + xmbh + "'");
			if (djdys != null && djdys.size() > 0) {
				RealUnit unit = UnitTools.loadUnit(BDCDYLX.initFrom(djdys.get(0).getBDCDYLX()),
						DJDYLY.initFrom(djdys.get(0).getLY()), djdys.get(0).getBDCDYID());
				if (unit != null) {
					djbDTO.setSyqmj(StringHelper.formatDouble(unit.getMJ()));
					djbDTO.setZl(unit.getZL());
				}
			}
		}

		String qlrStr = "";// 权利人
		String zjlx = "";// 证件类型
		String zjhm = "";// 证件号码
		for (int i = 0; i < qlrs.size(); i++) {
			if (i == 0) {
				// 权利人类型
				djbDTO.setQlrlx(ConstHelper.getNameByValue("QLRLX", qlrs.get(i).getQLRLX()));
			}
			if (qlrs.size() - 1 == i) {
				qlrStr += qlrs.get(i).getQLRMC();
				zjlx += ConstHelper.getNameByValue("ZJLX", qlrs.get(i).getZJZL());// 证件类型
				if (qlrs.get(i).getZJH() != null) {
					zjhm += qlrs.get(i).getZJH();
				}
			} else {
				qlrStr += qlrs.get(i).getQLRMC() + ",";
				zjlx += ConstHelper.getNameByValue("ZJLX", qlrs.get(i).getZJZL()) + ",";
				if (qlrs.get(i).getZJH() != null) {
					zjhm += qlrs.get(i).getZJH() + ",";
				}
			}
		}
		if (zjhm.length() > 0 && zjhm.endsWith(",")) {
			zjhm = zjhm.substring(0, zjhm.length() - 1);
		}
		//String gyfs = "";
		if (qlrs.size() > 0) {
			String gyfs = qlrs.get(0).getGYFS();
			if(gyfs == null||gyfs.trim().isEmpty()){
				djbDTO.setGyqk("");
			}
			else if(gyfs.equals("0")){
				djbDTO.setGyqk("单独所有");
			}
			else if(gyfs.equals("1")){
				djbDTO.setGyqk("共同共有");
			}
			else if(gyfs.equals("2")){
				djbDTO.setGyqk("按份共有");
			}
			 
		}
		djbDTO.setQlr(qlrStr);
		djbDTO.setZjlx(zjlx);
		djbDTO.setZjhm(zjhm);
		// 从具体单元获取不动产单元号-sun-2015-11-17
		String bdcdyh = getRealUnitBdcdyh(qlid, xmbh);
		djbDTO.setSyqx(GetMLandYT(qlid, xmbh));
		djbDTO.setBdcdyh(bdcdyh);
		if(ql_1.getGYRQK() != null){
			djbDTO.setGyrqk(ql_1.getGYRQK());
		}
		return djbDTO;
	}

	/**
	 * 登记簿上显示的多个土地使用权
	 * 
	 * @作者 海豹
	 * @创建时间 2015年11月21日下午10:27:39
	 * @param qlid
	 * @param xmbh
	 * @return
	 */
	private String GetMLandYT(String qlid, String xmbh) {
		String sj = "";
		Rights right = RightsTools.loadRights(DJDYLY.GZ, qlid);
		if (!StringHelper.isEmpty(right)) {
			String hqlCondition = ProjectHelper.GetXMBHCondition(xmbh) + "and DJDYID ='" + right.getDJDYID() + "'";
			List<BDCS_DJDY_GZ> lstdjdy = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, hqlCondition);
			if (lstdjdy != null && lstdjdy.size() > 0) {
				BDCS_DJDY_GZ bdcs_djdy_gz = lstdjdy.get(0);
				BDCDYLX bdcdylx = null;
				if (!StringHelper.isEmpty(bdcs_djdy_gz.getBDCDYLX()))
					bdcdylx = BDCDYLX.initFrom(bdcs_djdy_gz.getBDCDYLX());
				DJDYLY dyly = null;
				if (!StringHelper.isEmpty(bdcs_djdy_gz.getLY())) {
					dyly = DJDYLY.initFrom(bdcs_djdy_gz.getLY());
				}
				String bdcdyid = bdcs_djdy_gz.getBDCDYID();
				RealUnit unit = UnitTools.loadUnit(bdcdylx, dyly, bdcdyid);
				boolean flag = false;
				if (unit instanceof UseLand) {
					UseLand land = (UseLand) unit;

					String myt = "";
					if (!StringHelper.isEmpty(land)) {
						if (!StringHelper.isEmpty(land.getTDYTS())) {
							for (TDYT tdyt : land.getTDYTS()) {

								String qssj = "";
								String jssj = "";
								if (!flag) {
									if (!StringHelper.isEmpty(tdyt.getTDYTMC())) {
										myt = tdyt.getTDYTMC();
									}
									qssj = StringHelper.FormatByDatetime(tdyt.getQSRQ());
									jssj = StringHelper.FormatByDatetime(tdyt.getZZRQ());
									if (StringHelper.isEmpty(qssj)) {
										qssj = "";
									} else {
										qssj = qssj + "起";
									}
									if (StringHelper.isEmpty(jssj)) {
										jssj = "----";
									}
									if (land.getTDYTS().size() > 1)// 判断是多个用途时，添加用途及对应的时间
									{
										sj = myt + " " + qssj + jssj + "止";
									} else {
										sj = qssj + jssj + "止";
									}
									flag = true;
								} else {
									if (!StringHelper.isEmpty(tdyt.getTDYTMC())) {
										myt = tdyt.getTDYTMC();
									}
									qssj = StringHelper.FormatByDatetime(tdyt.getQSRQ());
									jssj = StringHelper.FormatByDatetime(tdyt.getZZRQ());
									if (StringHelper.isEmpty(qssj)) {
										qssj = "";
									} else {
										qssj = qssj + "起";
									}
									if (StringHelper.isEmpty(jssj)) {
										jssj = "----";
									}
									sj = sj + "<br/>" + myt + " " + qssj + jssj + "止";
								}

							}
						}
					}
				}
			}
		}
		return sj;
	}

	/**
	 * 根据权利信息生成DJBDTO对象
	 * 
	 * @param ql_GZ
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private DJBDTO GetDJBDTOFromQL(BDCS_QL_GZ ql_GZ) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT (SELECT JG FROM BDCS_SHYQZD_GZ WHERE bDCDYID IN");
		sqlBuilder.append(" (SELECT T.BDCDYID FROM BDCS_DJDY_GZ T WHERE T.XMBH=? AND T.DJDYID IN");
		sqlBuilder.append(" (SELECT DJDYID FROM BDCS_QL_GZ WHERE id = ?))) AS jG,");
		sqlBuilder.append(
				" DJLX,QL.DJYY,QL.QLQSSJ,QL.QLJSSJ,QL.BDCQZH,QL.DJSJ,QL.DBR,QL.FJ,QL.BDCDYH,QL.QXDM,QL.XMBH FROM BDCS_QL_GZ QL WHERE id = ?");
		Query query = baseCommonDao.getCurrentSession().createQuery(sqlBuilder.toString()).setString(0, ql_GZ.getXMBH())
				.setString(1, ql_GZ.getId()).setString(2, ql_GZ.getId());
		List<Object[]> list = query.list();
		DJBDTO dto = new DJBDTO();
		DateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");// 日期向字符串转换，可以设置任意的转换格式format
		for (Object[] object : list) {
			if (null != object[0]) {
				dto.setQdjg(StringHelper.formatObject(object[0]));
			}
			if (null != object[1]) {
				// 登记类型
				dto.setDjlx(ConstHelper.getNameByValue("DJLX", StringHelper.formatObject(object[1])));
			}
			if (null != object[2]) {
				dto.setDjyy(StringHelper.formatObject(object[2]));
			}
			if (null != object[3] && null != object[4]) {
				dto.setSyqx(sdf.format(object[3]) + "/" + sdf.format(object[4]));
			}
			if (null != object[5]) {
				dto.setBdcqzh(StringHelper.formatObject(object[5]));
			}
			if (null != object[6]) {
				dto.setDjsj(StringHelper.FormatYmdhmsByDate(object[6]));
			}
			if (null != object[7]) {
				dto.setDbr(StringHelper.formatObject(object[7]));
			}
			if (null != object[8]) {
				dto.setFj(StringHelper.formatObject(object[8]));
			}
			if (null != object[9]) {
				dto.setBdcdyh(StringHelper.formatObject(object[9]));
			}
			if (null != object[10]) {
				dto.setQxdm(StringHelper.formatObject(object[10]));
			}
			if (null != object[11]) {
				dto.setXmbh(StringHelper.formatObject(object[11]));
			}
		}
		ql_GZ = baseCommonDao.get(BDCS_QL_GZ.class, ql_GZ.getId());
		BDCS_FSQL_GZ fsql = baseCommonDao.get(BDCS_FSQL_GZ.class, ql_GZ.getFSQLID());
		//价格danwei
		String jgdw="";
		if (fsql != null) {
			String syqmj = fsql.getSYQMJ() == null ? "" : StringHelper.formatObject(fsql.getSYQMJ());
			dto.setSyqmj(syqmj);// 使用权面积			
			dto.setJgdw(ConstHelper.getNameByValue("JEDW", fsql.getZQDW()));
		}
		
		String qdjg = ql_GZ.getQDJG() == null ? "0" : StringHelper.formatDouble(ql_GZ.getQDJG());
		String qdjg1 = qdjg ;
		dto.setQdjg(qdjg1);
		return dto;
	}

	/**
	 * 获取房地产权登记簿页信息
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月22日下午5:42:04
	 * @param xmbh
	 * @param djdyid
	 * @return
	 */
	@Override
	public FDCQDB getFDCQDB(String xmbh, String djdyid) {
		List<BDCS_DJDY_GZ> djdys = this.getDJdys(xmbh, djdyid);
		FDCQDB fdcqdb = new FDCQDB();
		if (null != djdys && djdys.size() > 0) {
			BDCS_DJDY_GZ djdy = djdys.get(0);
			// 登记原因，登簿人，附记，登记类型，土地使用期限，登记时间，项目编号，区划代码
			String qllxarray = "('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','28','29','30','99')";
			List<Rights> qls = RightsTools.loadRightsByCondition(DJDYLY.GZ,
					"XMBH='" + xmbh + "' AND DJDYID='" + djdyid + "' AND QLLX IN " + qllxarray);
			if (qls != null && qls.size() > 0) {
				Rights ql = qls.get(0);
				if (null != ql) {
					fdcqdb.setDJYY(ql.getDJYY());
					fdcqdb.setDBR(ql.getDBR());
					fdcqdb.setFJ(ql.getFJ());
					fdcqdb.setDJLX(ConstHelper.getNameByValue("DJLX", ql.getDJLX()));
					fdcqdb.setQDJG(StringHelper.formatObject(ql.getQDJG()));
					String qssj = StringHelper.FormatByDatetime(ql.getQLQSSJ());
					String jssj = StringHelper.FormatByDatetime(ql.getQLJSSJ());
					//通过本地化配置设置时间，取土地用途的时间 liangq
				//	if(!"1".equals(ConfigHelper.getNameByValue("GetSYQXFrom"))){
					//通过qssj和jssj是否为空判断
					if("".equals(qssj) && "".equals(jssj)){
						//获取tdyt表中的时间
						BDCDYLX lx = null;
						DJDYLY ly = null;
						String bdcdyid = "";
						if (djdy != null) {
							lx = BDCDYLX.initFrom(djdy.getBDCDYLX());// 不动产单元 类型
							ly = DJDYLY.initFrom(djdy.getLY());// 不动产单元来源
							bdcdyid = djdy.getBDCDYID();// 不动产单元ID
						}
						RealUnit unit = UnitTools.loadUnit(lx, ly, bdcdyid);
						List<TDYT> tdytlist = new ArrayList<TDYT>();
						if (unit != null && BDCDYLX.SHYQZD.equals(lx)) {
							UseLand land = (UseLand) unit;
							if (land != null) {
								String tdsyqx = "";
								tdytlist = land.getTDYTS();
								if(tdytlist.size()>0){
									for(TDYT tdyt : tdytlist){
										tdsyqx += tdyt.getTDYTMC()+" ";
										tdsyqx += StringHelper.FormatByDatetime(tdyt.getQSRQ())+"起";
										tdsyqx += StringHelper.FormatByDatetime(tdyt.getZZRQ())+"止";
										tdsyqx += "</br>";
									}
									if(tdsyqx.contains("</br>")){
										tdsyqx = tdsyqx.substring(0, tdsyqx.length()-5);
									}
								}
								fdcqdb.setTDSYQX(tdsyqx);
							}
						} 
					}else{
						fdcqdb.setTDSYQX(qssj + "起" + jssj + "止");
					}
					fdcqdb.setDJSJ(StringHelper.FormatYmdhmsByDate(ql.getDJSJ()));
					fdcqdb.setXMBH(ql.getXMBH());
					fdcqdb.setQHDM(ql.getQXDM());
//					fdcqdb.setBDCQZH(ql.getBDCQZH());
					if ((DJLX.ZYDJ.Value.equals(ql.getDJLX()) || DJLX.BGDJ.Value.equals(ql.getDJLX()))
							&& QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(ql.getQLLX()))// 转移登记或变更登记取权利中的取得价格
					{
						fdcqdb.setFDCJYJG(StringHelper.isEmpty(ql.getQDJG()) ? "0" : StringHelper.formatDouble(ql.getQDJG()));
					}
				}
				// 权利人列表
				List<RightsHolder> qlrlst = RightsHolderTools.loadRightsHolders(DJDYLY.GZ, ql.getId());
				List<String> qlrmclst = new ArrayList<String>();
				List<String> zjzllst = new ArrayList<String>();
				List<String> zjhlst = new ArrayList<String>();
				List<String> qlrlxlst = new ArrayList<String>();
				List<String> tdsyqrlst= new ArrayList<String>();
				List<String> qzhlist = new ArrayList<String>();
				boolean flag = false;
				
				// 证件种类，证件号，房屋所有权人，土地使用权人，权利人类型，
				for (RightsHolder qlr : qlrlst) {
					String zjzl = ConstHelper.getNameByValue("ZJLX", qlr.getZJZL());
					zjzllst.add(zjzl);
					qlrmclst.add(qlr.getQLRMC());
					zjhlst.add(qlr.getZJH());
					String qlrlx = ConstHelper.getNameByValue("QLRLX", qlr.getQLRLX());
					qlrlxlst.add(qlrlx);
					if(!StringHelper.isEmpty(qlr.getBDCQZH())){
						if(!qzhlist.contains(qlr.getBDCQZH())){
							qzhlist.add(qlr.getBDCQZH());
							flag = true;
						}
					}
				}
				if (flag) {
					fdcqdb.setBDCQZH(StringHelper.formatList(qzhlist));
				} else {
					fdcqdb.setBDCQZH(ql.getBDCQZH());
				}
				tdsyqrlst.add(ql.getTDSHYQR());//从权利表里面获取土地使用权人,因为权利信息页面保存了土地使用权人。
				fdcqdb.setZJZL(StringHelper.formatList(zjzllst));
				fdcqdb.setZJH(StringHelper.formatList(zjhlst).equals("null") ? "" : StringHelper.formatList(zjhlst));
				fdcqdb.setFWSYQR(StringHelper.formatList(qlrmclst));
				if (!StringHelper.isEmpty(tdsyqrlst.get(0))) {
					fdcqdb.setTDSYQR(StringHelper.formatList(tdsyqrlst));
				}
				fdcqdb.setQLRLX(StringHelper.formatList(qlrlxlst));
				// 共有情况
				String gyqk = "";
				if (qlrlst != null && qlrlst.size() > 0) {
					if (qlrlst.get(0) != null) {
						if (qlrlst.get(0).getGYFS() != null) {
							String gyfs = "";
							gyfs = qlrlst.get(0).getGYFS();
							if ("2".equals(gyfs)) {
								gyqk = "按份共有";
								StringBuilder result = new StringBuilder();
								flag = false;
								for (RightsHolder rightsholder : qlrlst) {
									if (flag) {
										result.append(",");
									} else {
										flag = true;
									}
									result.append(rightsholder.getQLBL());
								}
								if (!result.toString().equals("")) {
									gyqk = "按份共有，其中共有份额分别为：" + result.toString();
								}
								fdcqdb.setFWGYQK(gyqk);
							} else if ("1".equals(gyfs)) {
								fdcqdb.setFWGYQK("共同共有");
							} else if ("0".equals(gyfs)) {
								fdcqdb.setFWGYQK("单独所有");
							} else if("3".equals(gyfs)){
								fdcqdb.setFWGYQK("其它共有");
							}
						}
					}
				}
				// 单元信息
				fdcqdb = this.getUnit(djdy, fdcqdb, xmbh, djdyid);
				// 从具体单元获取不动产单元号-sun-2015-11-17
				String bdcdyh = getRealUnitBdcdyh(ql.getId(), xmbh);
				fdcqdb.setBDCDYH(bdcdyh);
			}
		}
		return fdcqdb;
	}

	/**
	 * 获取房地产权登记簿页信息:组合业务：初始登记+_房屋所有权_在建工程转现登记（移除抵押权对应的权利人信息）
	 * 
	 * @作者 海豹
	 * @创建时间 2015年7月26日上午2:03:09
	 * @param xmbh
	 * @param djdyid
	 * @return
	 */
	@Override
	public FDCQDB getCombFDCQDB(String xmbh, String djdyid) {
		List<BDCS_DJDY_GZ> djdys = this.getDJdys(xmbh, djdyid);
		FDCQDB fdcqdb = new FDCQDB();
		if (null != djdys && djdys.size() > 0) {
			BDCS_DJDY_GZ djdy = djdys.get(0);
			// 权利人列表
			String sql = MessageFormat.format(
					"QLID in (select id from BDCS_QL_GZ where DJDYID=''{0}'' and XMBH=''{1}'' and QLLX !=''23'' )",
					djdyid, xmbh);
			List<BDCS_QLR_GZ> qlrs = baseCommonDao.getDataList(BDCS_QLR_GZ.class, sql);
			List<String> qlrmclst = new ArrayList<String>();
			List<String> zjzllst = new ArrayList<String>();
			List<String> zjhlst = new ArrayList<String>();
			List<String> qlrlxlst = new ArrayList<String>();
			// 证件种类，证件号，房屋所有权人，土地使用权人，权利人类型，
			for (RightsHolder qlr : qlrs) {
				String zjzl = ConstHelper.getNameByValue("ZJLX", qlr.getZJZL());
				zjzllst.add(zjzl);
				qlrmclst.add(qlr.getQLRMC());
				zjhlst.add(qlr.getZJH());
				String qlrlx = ConstHelper.getNameByValue("QLRLX", qlr.getQLRLX());
				qlrlxlst.add(qlrlx);
			}
			fdcqdb.setZJZL(StringHelper.formatList(zjzllst));
			fdcqdb.setZJH(StringHelper.formatList(zjhlst));
			fdcqdb.setFWSYQR(StringHelper.formatList(qlrmclst));
			fdcqdb.setTDSYQR(StringHelper.formatList(qlrmclst));
			fdcqdb.setQLRLX(StringHelper.formatList(qlrlxlst));
			// 共有情况
			if (qlrs.size() > 1) {
				fdcqdb.setFWGYQK("共同共有");
			} else if (qlrs.size() == 1) {
				fdcqdb.setFWGYQK("单独所有");
			}

			Rights ql = null;
			// 登记原因，登簿人，附记，登记类型，土地使用期限，登记时间，项目编号，区划代码
			// 权利中可能有多个，这里只取房屋所有权
			String hqlCondition = MessageFormat.format(" DJDYID=''{0}'' AND XMBH=''{1}'' AND QLLX = ''{2}''", djdyid,
					xmbh, QLLX.GYJSYDSHYQ_FWSYQ.Value);
			List<BDCS_QL_GZ> qls = baseCommonDao.getDataList(BDCS_QL_GZ.class, hqlCondition);
			if (null != qls && qls.size() > 0) {
				ql = qls.get(0);
			}
			if (null != ql) {
				fdcqdb.setDJYY(ql.getDJYY());
				fdcqdb.setDBR(ql.getDBR());
				fdcqdb.setFJ(ql.getFJ());
				fdcqdb.setDJLX(ConstHelper.getNameByValue("DJLX", ql.getDJLX()));
				String qssj = StringHelper.FormatByDatetime(ql.getQLQSSJ());
				String jssj = StringHelper.FormatByDatetime(ql.getQLJSSJ());
				fdcqdb.setTDSYQX(qssj + "起" + jssj + "止");
				fdcqdb.setDJSJ(StringHelper.FormatYmdhmsByDate(ql.getDJSJ()));
				fdcqdb.setXMBH(ql.getXMBH());
				fdcqdb.setQHDM(ql.getQXDM());
				fdcqdb.setBDCQZH(ql.getBDCQZH());
			}
			// 单元信息
			fdcqdb = this.getUnit(djdy, fdcqdb, xmbh, djdyid);
			// 从具体单元获取不动产单元号-sun-2015-11-17
			if (!StringHelper.isEmpty(ql)) {
				String bdcdyh = getRealUnitBdcdyh(ql.getId(), xmbh);
				fdcqdb.setBDCDYH(bdcdyh);
			}
		}
		return fdcqdb;
	}

	/**
	 * 组装单元信息
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年7月18日上午12:26:28
	 * @param djdy
	 *            登记单元
	 * @param fdcqdb
	 *            房地产权登记
	 * @param xmbh
	 * @param djdyid
	 * @return
	 */
	private FDCQDB getUnit(BDCS_DJDY_GZ djdy, FDCQDB fdcqdb, String xmbh, String djdyid) {
		ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
		String bdcdylx = djdy.getBDCDYLX();
		BDCDYLX bDCDYLX = BDCDYLX.initFrom(bdcdylx);
		String djdyly = djdy.getLY();
		DJDYLY dJDYLY = DJDYLY.initFrom(djdyly);
		// 相应的单元信息
		RealUnit unit = UnitTools.loadUnit(bDCDYLX, dJDYLY, djdy.getBDCDYID());
		fdcqdb.setZL(unit.getZL());
		if (ConstValue.BDCDYLX.H.Value.equals(bdcdylx)) { // 户
			House house = (House) unit;
			if (null != house) {
				fdcqdb.setGHYT(ConstHelper.getNameByValue("FWYT", house.getGHYT()));
				// 土地使用权人要取权利里边的 2015.10.12 刘树峰
				// fdcqdb.setTDSYQR(house.getTDSYQR());
				fdcqdb.setDYTDMJ(StringHelper.FormatByDatatype(StringHelper.formatDouble(house.getDYTDMJ())));
				fdcqdb.setFTTDMJ(StringHelper.FormatByDatatype(StringHelper.formatDouble(house.getFTTDMJ())));
				if (!((ConstValue.DJLX.ZYDJ.Value.equals(info.getDjlx())
						|| ConstValue.DJLX.BGDJ.Value.equals(info.getDjlx()))
						&& QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(info.getQllx())))// 转移登记/变更登记不用，用权利的中的取得价格
				{
					fdcqdb.setFDCJYJG(StringHelper.FormatByDatatype(house.getFDCJYJG()));
				}
				fdcqdb.setFWJG(ConstHelper.getNameByValue("FWJG", house.getFWJG()));
				fdcqdb.setBDCDYH(house.getBDCDYH());
				String zcs = StringHelper.FormatByDatatype(house.getZCS());
				String szc = StringHelper.FormatByDatatype(house.getSZC());
				fdcqdb.setSZC_ZCS(szc + "/" + zcs);
				fdcqdb.setJZMJ(house == null ? "" : StringHelper.formatObject(StringHelper.formatDouble(house.getSCJZMJ())));
				fdcqdb.setZYJZMJ(StringHelper.FormatByDatatype(StringHelper.formatDouble(house.getSCTNJZMJ())));
				fdcqdb.setFTJZMJ(StringHelper.FormatByDatatype(StringHelper.formatDouble(house.getSCFTJZMJ())));
				fdcqdb.setJGSJ(StringHelper.FormatByDatetime(house.getJGSJ()));
			}
		}else if (ConstValue.BDCDYLX.YCH.Value.equals(bdcdylx)) { // 户
			House house = (House) unit;
			if (null != house) {
				fdcqdb.setGHYT(ConstHelper.getNameByValue("FWYT", house.getGHYT()));
				// 土地使用权人要取权利里边的 2015.10.12 刘树峰
				// fdcqdb.setTDSYQR(house.getTDSYQR());
				fdcqdb.setDYTDMJ(StringHelper.FormatByDatatype(StringHelper.formatDouble(house.getDYTDMJ())));
				fdcqdb.setFTTDMJ(StringHelper.FormatByDatatype(StringHelper.formatDouble(house.getFTTDMJ())));
				if (!((ConstValue.DJLX.ZYDJ.Value.equals(info.getDjlx())
						|| ConstValue.DJLX.BGDJ.Value.equals(info.getDjlx()))
						&& QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(info.getQllx())))// 转移登记/变更登记不用，用权利的中的取得价格
				{
					fdcqdb.setFDCJYJG(StringHelper.FormatByDatatype(house.getFDCJYJG()));
				}
				fdcqdb.setFWJG(ConstHelper.getNameByValue("FWJG", house.getFWJG()));
				fdcqdb.setBDCDYH(house.getBDCDYH());
				String zcs = StringHelper.FormatByDatatype(house.getZCS());
				String szc = StringHelper.FormatByDatatype(house.getSZC());
				fdcqdb.setSZC_ZCS(szc + "/" + zcs);
				fdcqdb.setJZMJ(house == null ? "" : StringHelper.formatObject(StringHelper.formatDouble((house.getYCJZMJ()))));
				fdcqdb.setZYJZMJ(StringHelper.FormatByDatatype(StringHelper.formatDouble(house.getYCTNJZMJ())));
				fdcqdb.setFTJZMJ(StringHelper.FormatByDatatype(StringHelper.formatDouble(house.getYCFTJZMJ())));
				fdcqdb.setJGSJ(StringHelper.FormatByDatetime(house.getJGSJ()));
			}
		} else if (ConstValue.BDCDYLX.ZRZ.Value.equals(bdcdylx)) { // ZRZ
			Building building = (Building) unit;
			if (null != building) {
				fdcqdb.setGHYT(ConstHelper.getNameByValue("FWYT", building.getGHYT()));
				fdcqdb.setTDSYQR(building.getTDSYQR());
				fdcqdb.setBDCDYH(building.getBDCDYH());
				fdcqdb.setDYTDMJ(StringHelper.FormatByDatatype(building.getDYTDMJ()));
				fdcqdb.setFTTDMJ(StringHelper.FormatByDatatype(building.getFTTDMJ()));
				fdcqdb.setFDCJYJG(StringHelper.FormatByDatatype(building.getFDCJYJG()));
				fdcqdb.setFWJG(ConstHelper.getNameByValue("FWJG", building.getFWJG()));
				fdcqdb.setSZC_ZCS(StringHelper.FormatByDatatype(building.getZCS()));
				fdcqdb.setJGSJ(StringHelper.FormatByDatetime(building.getJGRQ()));
				if (DJDYLY.GZ.Value.equals(djdyly)) { // 这里只有GZ的时候才赋下面这些值？？？
					fdcqdb.setJZMJ(StringHelper.formatObject(building.getSCJZMJ()));
					// TODO 找不到对应的字段
					// fdcqdb.setZYJZMJ(StringHelper.formatObject(building
					// .getSCJZMJ()));
					// fdcqdb.setFTJZMJ(StringHelper.formatObject(building
					// .getSCJZMJ()));
				}
			}
			if (DJDYLY.GZ.Value.equals(djdyly) && "03".equals(info.getSllx1())) { // 如果是项目内多幢添加zrzlist返回到前台
																					// diaoliwei
																					// 2015-6-29
				List<BDCS_ZRZ_GZ> zrzs = new ArrayList<BDCS_ZRZ_GZ>();
				BDCS_ZRZ_GZ zrz = (BDCS_ZRZ_GZ) unit;
				zrzs.add(zrz);
				fdcqdb.setZrzList(zrzs);
			}
		}
		return fdcqdb;
	}

	/**
	 * 根据项目编号和登记单元id获取登记单元
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年7月17日下午9:37:59
	 * @param xmbh
	 * @param djdyid
	 * @return
	 */
	private List<BDCS_DJDY_GZ> getDJdys(String xmbh, String djdyid) {
		String hql = MessageFormat.format(" XMBH=''{0}'' AND DJDYID=''{1}''", xmbh, djdyid);
		List<BDCS_DJDY_GZ> djdylst = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, hql);
		return djdylst;
	}

	/**
	 * 通过行政区划代码获取不动产权证明号or 不动产登记证明
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月15日上午7:13:51
	 * @param qhdm
	 *            区划代码
	 * @param type
	 *            区分不动产权证明号还是不动产登记证明
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private ZHInfo GetBDCZM(String qhdm, String type, Integer count, String xmbh,JSONObject jsonObject) {
		ZHInfo strbdcqzh = null;
		String createqzhtype=ConfigHelper.getNameByValue("CREATEQZHTYPE");
		if (!StringUtils.isEmpty(qhdm)) {
			String hql = MessageFormat.format(
					" CONSTSLSID IN (SELECT CONSTSLSID FROM BDCS_CONSTCLS WHERE CONSTCLSTYPE=''{0}'') AND CONSTVALUE=''{1}''",
					"SS", qhdm.substring(0, 2) + "0000");

			List<BDCS_CONST> lst1 = baseCommonDao.getDataList(BDCS_CONST.class, hql);
			String jc = "";
			String qhmc = ConstHelper.getNameByValue("CONSTCLSTYPE", qhdm);
			if (lst1 != null && lst1.size() > 0) {
				jc = lst1.get(0).getBZ();
			}
			qhmc = ConfigHelper.getNameByValue("XZQHMC");
			strbdcqzh = CreatFile_Number(qhdm, type, count);
			strbdcqzh.setQhjc(jc);
			strbdcqzh.setQhmc(qhmc);

//			BDCS_LOG_QZH log_qzh = new BDCS_LOG_QZH();
//			String id = SuperHelper.GeneratePrimaryKey();
//			log_qzh.setId(id);
			StringBuilder builderqzh = new StringBuilder();
			builderqzh.append(strbdcqzh.getQhjc());
			builderqzh.append("(");
			builderqzh.append(strbdcqzh.getsYear());
			builderqzh.append(")");
			builderqzh.append(strbdcqzh.getQhmc());
			if (("GETBDCQZH").equals(type)) {
				builderqzh.append("不动产权第");
			} else {
				builderqzh.append("不动产证明第");
			}
			if("2".equals(createqzhtype)||"3".equals(createqzhtype)){
				String qhstr="00";
				if(qhdm.length()>=6){
					qhstr=qhdm.substring(4, 6);
				}
				builderqzh.append(qhstr);
				builderqzh.append(StringHelper.PadLeft(StringHelper.formatDouble(strbdcqzh.getMinzh()), 5, '0'));
			}else{
				builderqzh.append(StringHelper.PadLeft(StringHelper.formatDouble(strbdcqzh.getMinzh()), 7, '0'));
			}
			
			builderqzh.append("号");
			String bdcqzh = builderqzh.toString();
//			log_qzh.setSTARTQZH(bdcqzh);
//			log_qzh.setXMBH(xmbh);
//			log_qzh.setSTARTXH(strbdcqzh.getMinzh());
//			log_qzh.setQZLX(type);
//			log_qzh.setGS(count);
//			baseCommonDao.save(log_qzh);
			JSONObject jsonobj=new JSONObject();
			jsonobj.put("起始序号", strbdcqzh.getMinzh());
			jsonobj.put("起始证号", bdcqzh);
			jsonobj.put("权证号个数", count);
			jsonobj.put("权证类型", type);
			jsonObject.put("获取权证号日志", jsonobj);
		}
		return strbdcqzh;
	}

	/**
	 * 调用存储过程生成不动产权证明号or 不动产登记证明
	 * 
	 * @作者 海豹
	 * @创建时间 2015年7月17日下午4:57:50
	 * @param qhjc
	 * @param qhmc
	 * @param qhdm
	 * @param tableName
	 * @return
	 */
	private ZHInfo CreatFile_Number(String qhdm, String tableName, Integer count) {
		ZHInfo zhinfo =baseCommonDao.createFile_Number(qhdm, tableName, count);
		return zhinfo;
	}

	/**
	 * 获取现状库中使用权宗地信息
	 */
	@Override
	public BDCS_SHYQZD_XZ GetSHYQZDInfo(String bdcdyid) {
		// TODO 这个方法貌似不用了 diaoliwei 2015-7-15
		return baseCommonDao.get(BDCS_SHYQZD_XZ.class, bdcdyid);
	}

	/**
	 * 获取现状库中层信息
	 */
	@Override
	public BDCS_C_XZ GetCInfo(String cid) {
		// TODO 这个方法貌似不用了 diaoliwei 2015-7-15
		return baseCommonDao.get(BDCS_C_XZ.class, cid);
	}

	/**
	 * 获取现状库中自然幢信息
	 */
	@Override
	public BDCS_ZRZ_XZ GetZRZInfo(String bdcdyid) {
		// TODO 这个方法貌似不用了 diaoliwei 2015-7-15
		return baseCommonDao.get(BDCS_ZRZ_XZ.class, bdcdyid);
	}

	/**
	 * 获取现状库中户信息
	 */
	@Override
	public BDCS_H_XZ GetHInfo(String bdcdyid) {
		// TODO 这个方法貌似不用了 diaoliwei 2015-7-15
		return baseCommonDao.get(BDCS_H_XZ.class, bdcdyid);
	}

	/**
	 * 生成不动产权证号
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月14日上午5:19:33
	 * @param xmbh
	 * @param qlid
	 * @return
	 */
	@Override
	public String createBDCQZH(String xmbh, String qlid2, String type) {
		BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);
		//已经登簿的业务，无法再次重新获取权证号 liangqin
		if("1".equals(xmxx.getSFDB())){
			NewLogTools.saveLog("重新获取权证号失败，"+xmxx.getYWLSH()+"该项目已经登簿", xmbh, "6", "获取权证号日志及权证号赋值信息");
			return "";
		}
		JSONObject jsonObject = NewLogTools.getJSONByXMBH(xmbh);
		jsonObject.put("OperateType", "获取权证号日志及权证号赋值信息");
		if (xmxx.getSFHBZS() != null && xmxx.getSFHBZS().equals(SF.YES.Value)) {
			jsonObject.put("IsMulti_Units", "true");
			return getbdcqzh(xmbh, qlid2, type,jsonObject);
		}
		String qhdm = ConfigHelper.getNameByValue("XZQHDM");
		jsonObject.put("IsMulti_Units", "false");
		List<BDCS_ZS_GZ> zss = baseCommonDao.getDataList(BDCS_ZS_GZ.class, " XMBH='" + xmbh + "'");
		List<String> listqzh = new ArrayList<String>();
		if (zss != null && zss.size() > 0) {
			String createqzhtype=ConfigHelper.getNameByValue("CREATEQZHTYPE");
			if("2".equals(createqzhtype)){
				qhdm="";
				StringBuilder builder=new StringBuilder();
				builder.append("SELECT TUSER.AREACODE FROM SMWB_FRAMEWORK.T_USER TUSER ");
				builder.append("LEFT JOIN BDC_WORKFLOW.WFI_PROINST PROINST  ON TUSER.ID=PROINST.STAFF_ID ");
				builder.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON PROINST.FILE_NUMBER=XMXX.PROJECT_ID ");
				builder.append("WHERE XMBH='");
				builder.append(xmbh);
				builder.append("'");
				List<Map> list_user=baseCommonDao.getDataListByFullSql(builder.toString());
				if(list_user!=null&&list_user.size()>0){
					qhdm=StringHelper.formatObject(list_user.get(0).get("AREACODE"));
					
				}
			}if("3".equals(createqzhtype)){
				qhdm=Global.getCurrentUserInfo().getAreaCode();
			}
			ZHInfo zhinfo = this.GetBDCZM(qhdm, type, zss.size(), xmbh,jsonObject);
			if (zhinfo != null && !StringHelper.isEmpty(zhinfo.getMinzh()) && zhinfo.getMinzh() > 0
					&& !StringHelper.isEmpty(zhinfo.getsYear()) && !StringHelper.isEmpty(zhinfo.getQhjc())
					&& !StringHelper.isEmpty(zhinfo.getQhmc())) {
				for (int izs = 0; izs < zss.size(); izs++) {
					StringBuilder builderqzh = new StringBuilder();
					builderqzh.append(zhinfo.getQhjc());
					builderqzh.append("(");
					builderqzh.append(zhinfo.getsYear());
					builderqzh.append(")");
					builderqzh.append(zhinfo.getQhmc());
					builderqzh.append("不动产权第");
					if("2".equals(createqzhtype)||"3".equals(createqzhtype)){
						String qhstr="00";
						if(qhdm.length()>=6){
							qhstr=qhdm.substring(4, 6);
						}
						builderqzh.append(qhstr);
						builderqzh.append(StringHelper.PadLeft(StringHelper.formatDouble(zhinfo.getMinzh() + izs), 5, '0'));
					}else{
						builderqzh.append(StringHelper.PadLeft(StringHelper.formatDouble(zhinfo.getMinzh() + izs), 7, '0'));
					}
					builderqzh.append("号");
					String bdcqzh = builderqzh.toString();
					listqzh.add(bdcqzh);
				}
			}
		}
		List<BDCS_QL_GZ> qls = baseCommonDao.getDataList(BDCS_QL_GZ.class, " XMBH='" + xmbh + "'");
		String newcqzh = "";
		if (qls != null && qls.size() > 0) {
			JSONObject jsonqls=new JSONObject();
			for (int ii = 0; ii < qls.size(); ii++) {
				String qlid = qls.get(ii).getId();
				JSONObject jsonql=new JSONObject();
				jsonql.put("QLID", qlid);
				String cqzh = "";
				String sql = MessageFormat.format(
						" id IN (SELECT ZSID FROM BDCS_QDZR_GZ WHERE XMBH=''{0}'' AND QLID=''{1}'')", xmbh, qlid);
				List<BDCS_ZS_GZ> list = baseCommonDao.getDataList(BDCS_ZS_GZ.class, sql);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						 JSONObject jsonzs=new JSONObject();
						BDCS_ZS_GZ zs = list.get(i);
						  jsonzs.put("ZSID", zs.getId());
						if (!StringUtils.isEmpty(zs.getBDCQZH())
								&& !qls.get(ii).getDJLX().equals(ConstValue.DJLX.GZDJ.Value)) {
							cqzh += zs.getBDCQZH() + ",";
							jsonzs.put("证书描述", "证书的不动产权证号有值，跳过权利及权利人赋值");
							jsonql.put("QL_ZSAndQlrInfo:序号("+(ii+1)+")", jsonzs);
							continue;
						}
						String bdcqzh = listqzh.get(0);
						listqzh.remove(0);
						newcqzh += bdcqzh;
						zs.setBDCQZH(bdcqzh);
						cqzh += bdcqzh + ",";
						String hql = MessageFormat.format(
								" id IN(SELECT QLRID FROM BDCS_QDZR_GZ WHERE XMBH=''{0}'' AND QLID=''{1}'' AND ZSID=''{2}'')",
								xmbh, qlid, zs.getId());
						List<BDCS_QLR_GZ> qlrList = baseCommonDao.getDataList(BDCS_QLR_GZ.class, hql);
						if (qlrList != null && qlrList.size() > 0) {
							for (int j = 0; j < qlrList.size(); j++) {
								JSONObject jsonqlr=new JSONObject();
								BDCS_QLR_GZ qlr = qlrList.get(j);
								qlr.setBDCQZH(bdcqzh);
								qlr.setBDCQZHXH(getBDCQZHXH(bdcqzh));
								jsonqlr.put("QLRID", qlr.getId());
								jsonqlr.put("QLR-BDCQZH", bdcqzh);
								jsonzs.put("ZS_QlrInfo:序号("+(j+1)+")", jsonqlr);
								baseCommonDao.update(qlr);
							}
						}
						baseCommonDao.update(zs);
						jsonql.put("QL_ZsInfo:序号("+(i+1)+")", jsonzs);
					}
					jsonql.put("QL-BDCQZH", cqzh.substring(0, cqzh.length() - 1));
					qls.get(ii).setBDCQZH(cqzh.substring(0, cqzh.length() - 1));
					baseCommonDao.update(qls.get(ii));

				}
				jsonqls.put("QL_ZsAndQlrInfo:序号("+(ii+1)+")", jsonql);
			}
			jsonObject.put("权证号赋值信息日志", jsonqls);
		}
		NewLogTools.saveLog(jsonObject.toString(), xmbh, "6", "获取权证号日志及权证号赋值信息");
		baseCommonDao.flush();
		return newcqzh;
	}

	public String getBDCQZHXH(String BDCQZH) {
		String BDCQZHXH = "";
		String regex = "(.*)[(](.*)[)](.*)不动产.*第(.*)号";
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(BDCQZH);
		while (m.find()) {
			BDCQZHXH = m.group(2) + m.group(4);
		}
		if (StringHelper.isEmpty(BDCQZHXH)) {
			BDCQZHXH = BDCQZH;
		}
		return BDCQZHXH;
	}

	/**
	 * 多个单元一本证
	 * 
	 * @Title: getbdcqzh
	 * @author:liushufeng
	 * @date：2015年11月14日 下午5:37:57
	 * @param xmbh
	 * @param qlid2
	 * @param type
	 * @param ownership
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(rollbackFor={Exception.class})
	private String getbdcqzh(String xmbh, String qlid2, String type,JSONObject jsonObject) {
		String newcqzh = "";
		// 获取证书个数
		List<Tree> list = zsService.getZsTreeEx(xmbh);
		
		String qhdm = ConfigHelper.getNameByValue("XZQHDM");
		List<String> listqzh = new ArrayList<String>();
		String createqzhtype=ConfigHelper.getNameByValue("CREATEQZHTYPE");
		if("2".equals(createqzhtype)){
			qhdm="";
			StringBuilder builder=new StringBuilder();
			builder.append("SELECT TUSER.AREACODE FROM SMWB_FRAMEWORK.T_USER TUSER ");
			builder.append("LEFT JOIN BDC_WORKFLOW.WFI_PROINST PROINST  ON TUSER.ID=PROINST.STAFF_ID ");
			builder.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON PROINST.FILE_NUMBER=XMXX.PROJECT_ID ");
			builder.append("WHERE XMBH='");
			builder.append(xmbh);
			builder.append("'");
			List<Map> list_user=baseCommonDao.getDataListByFullSql(builder.toString());
			if(list_user!=null&&list_user.size()>0){
				qhdm=StringHelper.formatObject(list_user.get(0).get("AREACODE"));
				
			}
		}if("3".equals(createqzhtype)){
			qhdm=Global.getCurrentUserInfo().getAreaCode();
		}
		// 通过存储过程获取证号
		ZHInfo zhinfo = this.GetBDCZM(qhdm, type, list.size(), xmbh,jsonObject);
		// 证号封装
		if (zhinfo != null && !StringHelper.isEmpty(zhinfo.getMinzh()) && zhinfo.getMinzh() > 0
				&& !StringHelper.isEmpty(zhinfo.getsYear()) && !StringHelper.isEmpty(zhinfo.getQhjc())
				&& !StringHelper.isEmpty(zhinfo.getQhmc())) {
			for (int izs = 0; izs < list.size(); izs++) {
				StringBuilder builderqzh = new StringBuilder();
				builderqzh.append(zhinfo.getQhjc());
				builderqzh.append("(");
				builderqzh.append(zhinfo.getsYear());
				builderqzh.append(")");
				builderqzh.append(zhinfo.getQhmc());
				if (("GETBDCQZH").equals(type)) {
					builderqzh.append("不动产权第");
				} else {
					builderqzh.append("不动产证明第");
				}
				if("2".equals(createqzhtype)||"3".equals(createqzhtype)){
					String qhstr="00";
					if(qhdm.length()>=6){
						qhstr=qhdm.substring(4, 6);
					}
					builderqzh.append(qhstr);
					builderqzh.append(StringHelper.PadLeft(StringHelper.formatDouble(zhinfo.getMinzh() + izs), 5, '0'));
				}else{
					builderqzh.append(StringHelper.PadLeft(StringHelper.formatDouble(zhinfo.getMinzh() + izs), 7, '0'));
				}
				builderqzh.append("号");
				String bdcqzh = builderqzh.toString();
				listqzh.add(bdcqzh);
			}
		}
		if (listqzh.size() <= 0) {
			return "";
		}
		// 获取djdyid-groupid-qlid-qllx-qlrid-qlrmc-zsid关系，用于封装证书
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT DJDY.DJDYID,DJDY.GROUPID,QL.QLID,QL.QLLX,QLR.QLRID,QLR.QLRMC,ZS.ZSID ");
		builder.append("FROM BDCK.BDCS_QDZR_GZ QDZR ");
		builder.append("LEFT JOIN BDCK.BDCS_QLR_GZ QLR ON QLR.QLRID=QDZR.QLRID AND QLR.XMBH=QDZR.XMBH ");
		builder.append("LEFT JOIN BDCK.BDCS_QL_GZ QL ON QLR.QLID=QL.QLID AND QDZR.QLID=QL.QLID AND QL.XMBH=QDZR.XMBH ");
		builder.append("LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON QL.DJDYID=DJDY.DJDYID AND DJDY.XMBH=QDZR.XMBH ");
		builder.append("LEFT JOIN BDCK.BDCS_ZS_GZ ZS ON ZS.ZSID=QDZR.ZSID AND ZS.XMBH=QDZR.XMBH ");
		builder.append("WHERE QL.QLID IS NOT NULL AND QLR.QLRID IS NOT NULL AND DJDY.DJDYID IS NOT NULL ");
		builder.append("AND QDZR.XMBH='");
		builder.append(xmbh);
		builder.append("' AND (QL.ISCANCEL!=1 OR QL.ISCANCEL IS NULL) ORDER BY QL.QLID");//权利注销时候不需要重新更新产权证号wuzhu
		List<Map> listQL=baseCommonDao.getDataListByFullSql(builder.toString());
		if(listQL==null||listQL.size()<=0){
			return "";
		}

		HashMap<String, String> qlrid_zsid = new HashMap<String, String>();// 权利人-证书关系
		HashMap<String, List<String>> qlid_zsid = new HashMap<String, List<String>>();// 权利-证书关系
		HashMap<String, String> zsid_bdcqzh = new HashMap<String, String>();// 证书-证号关系
		HashMap<String, String> zsid_bdcqzhxh = new HashMap<String, String>();// 证书-证号序号关系
		HashMap<String, String> groupid_qllx_qlrmc_bdcqzh = new HashMap<String, String>();// 单元分组-权利类型-权利人名称-证号关系
		HashMap<String, String> groupid_qllx_qlrmc_bdcqzhxh = new HashMap<String, String>();// 单元分组-权利类型-权利人名称-证号序号关系
		// 封装各种关系，用于最后赋值
		for (Map ql : listQL) {
			String groupid = StringHelper.formatObject(ql.get("GROUPID"));
			String qlid = StringHelper.formatObject(ql.get("QLID"));
			String qllx = StringHelper.formatObject(ql.get("QLLX"));
			String qlrid = StringHelper.formatObject(ql.get("QLRID"));
			String qlrmc = StringHelper.formatObject(ql.get("QLRMC"));
			String zsid = StringHelper.formatObject(ql.get("ZSID"));
			String bdcqzh = "";
			String bdcqzhxh = "";
			if (zsid_bdcqzh.containsKey(zsid)) {
				bdcqzh = zsid_bdcqzh.get(zsid);
				if (zsid_bdcqzhxh.containsKey(zsid)) {
					bdcqzhxh = zsid_bdcqzhxh.get(zsid);
				}
			} else {
				if (groupid_qllx_qlrmc_bdcqzh.containsKey(groupid + "&&" + qllx + "&&" + qlrmc)) {
					bdcqzh = groupid_qllx_qlrmc_bdcqzh.get(groupid + "&&" + qllx + "&&" + qlrmc);
					if (groupid_qllx_qlrmc_bdcqzhxh.containsKey(groupid + "&&" + qllx + "&&" + qlrmc)) {
						bdcqzhxh = groupid_qllx_qlrmc_bdcqzhxh.get(groupid + "&&" + qllx + "&&" + qlrmc);
					}
				}
			}

			if (StringHelper.isEmpty(bdcqzh) && listqzh.size() > 0) {
				bdcqzh = listqzh.get(0);
				bdcqzhxh = getBDCQZHXH(bdcqzh);
				listqzh.remove(0);
				if (StringHelper.isEmpty(newcqzh)) {
					newcqzh = bdcqzh;
				}
			}
			if (!zsid_bdcqzh.containsKey(zsid)) {
				zsid_bdcqzh.put(zsid, bdcqzh);
			}
			if (!zsid_bdcqzhxh.containsKey(zsid)) {
				zsid_bdcqzhxh.put(zsid, bdcqzhxh);
			}
			if (!qlrid_zsid.containsKey(qlrid)) {
				qlrid_zsid.put(qlrid, zsid);
			}
			if (qlid_zsid.containsKey(qlid)) {
				List<String> list_zsid = qlid_zsid.get(qlid);
				if (!list_zsid.contains(zsid)) {
					list_zsid.add(zsid);
					qlid_zsid.remove(qlid);
					qlid_zsid.put(qlid, list_zsid);
				}
			} else {
				List<String> list_zsid = new ArrayList<String>();
				list_zsid.add(zsid);
				qlid_zsid.remove(qlid);
				qlid_zsid.put(qlid, list_zsid);
			}

			if (!groupid_qllx_qlrmc_bdcqzh.containsKey(groupid + "&&" + qllx + "&&" + qlrmc)) {
				groupid_qllx_qlrmc_bdcqzh.put(groupid + "&&" + qllx + "&&" + qlrmc, bdcqzh);
			}
			if (!groupid_qllx_qlrmc_bdcqzhxh.containsKey(groupid + "&&" + qllx + "&&" + qlrmc)) {
				groupid_qllx_qlrmc_bdcqzhxh.put(groupid + "&&" + qllx + "&&" + qlrmc, bdcqzhxh);
			}
		}
		JSONObject json=new JSONObject();
		// 对权利表中证号、证号序号赋值
		List<BDCS_QL_GZ> rights = baseCommonDao.getDataList(BDCS_QL_GZ.class, "XMBH='" + xmbh + "'");
		if (rights != null && rights.size() > 0) {
			JSONObject jsonqls=new JSONObject();
			int temp=0;
			for (BDCS_QL_GZ right : rights) {				
				String qlid = right.getId();
				if (!qlid_zsid.containsKey(qlid)) {
					continue;
				}
				List<String> listzsid = qlid_zsid.get(qlid);
				if (listzsid == null || listzsid.size() <= 0) {
					continue;
				}
				List<String> list_bdcqzh = new ArrayList<String>();
				List<String> list_bdcqzhxh = new ArrayList<String>();
				for (String zsid : listzsid) {
					if (zsid_bdcqzh.containsKey(zsid)) {
						String bdqzh = zsid_bdcqzh.get(zsid);
						if (!list_bdcqzh.contains(bdqzh)) {
							list_bdcqzh.add(bdqzh);
						}
					}
					if (zsid_bdcqzhxh.containsKey(zsid)) {
						String bdqzhxh = zsid_bdcqzhxh.get(zsid);
						if (!list_bdcqzhxh.contains(bdqzhxh)) {
							list_bdcqzhxh.add(bdqzhxh);
						}
					}
				}
				JSONObject jsonql=new JSONObject();
				temp++;
				Collections.sort(list_bdcqzh);
				Collections.sort(list_bdcqzhxh);
				right.setBDCQZH(StringHelper.formatList(list_bdcqzh, ","));
				right.setBDCQZHXH(StringHelper.formatList(list_bdcqzhxh, ","));
				jsonql.put("QLID", qlid);
				jsonql.put("QL_BDCQZH", StringHelper.formatList(list_bdcqzh, ","));
				jsonqls.put("QL_INFO:序号（"+temp+")", jsonql);
				baseCommonDao.update(right);
			}
			json.put("QL_INFOS", jsonqls);
		}
		// 对权利人表中证号、证号序号赋值
		List<BDCS_QLR_GZ> qlrs = baseCommonDao.getDataList(BDCS_QLR_GZ.class, "XMBH='" + xmbh + "'");
		if (qlrs != null && qlrs.size() > 0) {
			JSONObject jsonqlrs=new JSONObject();
			int temp=0;
			for (BDCS_QLR_GZ qlr : qlrs) {
				String qlrid = qlr.getId();
				if (!qlrid_zsid.containsKey(qlrid)) {
					continue;
				}
				JSONObject jsonqlr=new JSONObject();
				temp++;
				String zsid = qlrid_zsid.get(qlrid);
				if (zsid_bdcqzh.containsKey(zsid)) {
					String bdqzh = zsid_bdcqzh.get(zsid);
					qlr.setBDCQZH(bdqzh);
					jsonqlr.put("QLR_BDCQZH",bdqzh);
					jsonqlr.put("QLR_ZSID", zsid);
					jsonqlr.put("QLR_QLID", qlr.getQLID());
					jsonqlr.put("QLRID", qlr.getId());
					jsonqlrs.put("QLR_INFO:序号（"+temp+")", jsonqlr);
				}
				if (zsid_bdcqzhxh.containsKey(zsid)) {
					String bdqzhxh = zsid_bdcqzhxh.get(zsid);
					jsonqlr.put("QLR_ZSID", zsid);
					jsonqlr.put("QLR_QLID", qlr.getQLID());
					jsonqlr.put("QLRID", qlr.getId());
					jsonqlrs.put("QLR_INFO:序号（"+temp+")", jsonqlr);
					qlr.setBDCQZHXH(bdqzhxh);
				}
				baseCommonDao.update(qlr);
			}
			json.put("QLR_INFOS", jsonqlrs);
		}
		// 对证书表中证号赋值
		List<BDCS_ZS_GZ> zss = baseCommonDao.getDataList(BDCS_ZS_GZ.class, "XMBH='" + xmbh + "'");
		if (qlrs != null && qlrs.size() > 0) {
			JSONObject jsonzss=new JSONObject();
			int temp =0;
			for (BDCS_ZS_GZ zs : zss) {
				String zsid = zs.getId();
				if (zsid_bdcqzh.containsKey(zsid)) {
					JSONObject jsonzs=new JSONObject();
					temp++;
					String bdqzh = zsid_bdcqzh.get(zsid);
					zs.setBDCQZH(bdqzh);
					jsonzs.put("ZSID", zsid);
					jsonzs.put("ZS_BDCQZH", bdqzh);
					jsonzss.put("ZS_INFO:序号（"+temp+")", jsonzs);
				}
				baseCommonDao.update(zs);
			}
			json.put("ZS_INFOS", jsonzss);
		}
		jsonObject.put("权证号赋值信息日志", json);
		NewLogTools.saveLog(jsonObject.toString(), xmbh, "6", "获取权证号日志及权证号赋值信息");
		baseCommonDao.flush();
		String result = checkBDCQZH(xmbh);
		newcqzh = "true";
		if(!StringHelper.isEmpty(result)) {
			newcqzh = result;
		}
		return newcqzh;
	}
	
	/**
	 * 检查是否多个单元中所有权利生成权证号
	 * @param xmbh
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String checkBDCQZH(String xmbh) {
		String message = new String();
		//查询权利权证号不为空,为空返回
		StringBuilder h_xz_sql = new StringBuilder("select h.bdcdyh,h.zl from bdck.bdcs_h_xz h ");
			h_xz_sql.append(" left join bdck.bdcs_djdy_gz djdy on h.bdcdyid =djdy.bdcdyid ");
			h_xz_sql.append(" left join bdck.bdcs_ql_gz  ql  on ql.djdyid=djdy.djdyid ");
			h_xz_sql.append(" left join bdck.bdcs_qlr_gz  qlr  on qlr.qlid=ql.qlid ");
			h_xz_sql.append(" where  ql.xmbh='"+xmbh+"' and (  NVL2(ql.bdcqzh,1,0)=0 or NVL2(qlr.bdcqzh,1,0)=0 )");
		StringBuilder h_gz_sql = new StringBuilder("select h.bdcdyh,h.zl from bdck.bdcs_h_gz h ");
			h_gz_sql.append(" left join bdck.bdcs_djdy_gz djdy on h.bdcdyid =djdy.bdcdyid ");
			h_gz_sql.append(" left join bdck.bdcs_ql_gz  ql  on ql.djdyid=djdy.djdyid ");
			h_gz_sql.append(" left join bdck.bdcs_qlr_gz  qlr  on qlr.qlid=ql.qlid ");
			h_gz_sql.append(" where  ql.xmbh='"+xmbh+"' and (  NVL2(ql.bdcqzh,1,0)=0 or NVL2(qlr.bdcqzh,1,0)=0  )");
		StringBuilder h_xzy_sql = new StringBuilder("select h.bdcdyh,h.zl from bdck.bdcs_h_xzy h ");
			h_xzy_sql.append(" left join bdck.bdcs_djdy_gz djdy on h.bdcdyid =djdy.bdcdyid ");
			h_xzy_sql.append(" left join bdck.bdcs_ql_gz  ql  on ql.djdyid=djdy.djdyid ");
			h_xzy_sql.append(" left join bdck.bdcs_qlr_gz  qlr  on qlr.qlid=ql.qlid ");
			h_xzy_sql.append(" where  ql.xmbh='"+xmbh+"' and (  NVL2(ql.bdcqzh,1,0)=0 or NVL2(qlr.bdcqzh,1,0)=0 )");
		List<Map> xzList = baseCommonDao.getDataListByFullSql(h_xz_sql.toString());
		if(xzList != null && xzList.size()>0) {
			for(Map h : xzList) {
				String bdcdyh = (String) h.get("BDCDYH");
				String zl = (String) h.get("ZL");
				message=message+"单元号: "+bdcdyh+" 坐落: "+zl+",";
			}
			 message = message.substring(0, message.length()-1);
		}else {
			List<Map> gzList = baseCommonDao.getDataListByFullSql(h_gz_sql.toString());
			if(gzList != null && gzList.size() >0) {
				for (Map h : gzList) {
					String bdcdyh = (String) h.get("BDCDYH");
					String zl = (String) h.get("ZL");
					message=message+"单元号: "+bdcdyh+" 坐落: "+zl+",";	
				}
				 message = message.substring(0, message.length()-1);
			}else {
				List<Map> xzyList = baseCommonDao.getDataListByFullSql(h_xzy_sql.toString());
				if(xzyList != null && xzyList.size() >0) {
					for (Map h : xzyList) {
						String bdcdyh = (String) h.get("BDCDYH");
						String zl = (String) h.get("ZL");
						message=message+"单元号: "+bdcdyh+" 坐落: "+zl+",";	
					}
					 message = message.substring(0, message.length()-1);
				}
			}
		}
		return message ;
	}

	/**
	 * 多个单元一本证
	 * 
	 * @Title: getbdcqzh
	 * @author:liushufeng
	 * @date：2015年11月14日 下午5:37:57
	 * @param xmbh
	 * @param qlid2
	 * @param type
	 * @param ownership
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String getbdcqzhbyqllx(String xmbh, String type, String _qllx,JSONObject jsonObject) {
		String newcqzh = "";
		// 获取证书个数 
		List<Tree> list_all = new ArrayList<Tree>();
		boolean comboDJFlag=Global.SfComboDJ(xmbh);
		if(comboDJFlag){
			list_all = zsService.getZsTreeCombo(xmbh, _qllx);	
		}else{
			list_all = zsService.getZsTreeEx(xmbh);
		}
		List<Tree> list = new ArrayList<Tree>();
		for (Tree tree : list_all) {
			if (tree.getTag2().equals(_qllx)) {
				list.add(tree);
			}
		}

		String qhdm = ConfigHelper.getNameByValue("XZQHDM");
		List<String> listqzh = new ArrayList<String>();
		String createqzhtype=ConfigHelper.getNameByValue("CREATEQZHTYPE");
		if("2".equals(createqzhtype)){
			qhdm="";
			StringBuilder builder=new StringBuilder();
			builder.append("SELECT TUSER.AREACODE FROM SMWB_FRAMEWORK.T_USER TUSER ");
			builder.append("LEFT JOIN BDC_WORKFLOW.WFI_PROINST PROINST  ON TUSER.ID=PROINST.STAFF_ID ");
			builder.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON PROINST.FILE_NUMBER=XMXX.PROJECT_ID ");
			builder.append("WHERE XMBH='");
			builder.append(xmbh);
			builder.append("'");
			List<Map> list_user=baseCommonDao.getDataListByFullSql(builder.toString());
			if(list_user!=null&&list_user.size()>0){
				qhdm=StringHelper.formatObject(list_user.get(0).get("AREACODE"));
				
			}
		}if("3".equals(createqzhtype)){
			qhdm=Global.getCurrentUserInfo().getAreaCode();
		}
		// 通过存储过程获取证号
		
		ZHInfo zhinfo = this.GetBDCZM(qhdm, type, list.size(), xmbh,jsonObject);
		// 证号封装
		if (zhinfo != null && !StringHelper.isEmpty(zhinfo.getMinzh()) && zhinfo.getMinzh() > 0
				&& !StringHelper.isEmpty(zhinfo.getsYear()) && !StringHelper.isEmpty(zhinfo.getQhjc())
				&& !StringHelper.isEmpty(zhinfo.getQhmc())) {
			for (int izs = 0; izs < list.size(); izs++) {
				StringBuilder builderqzh = new StringBuilder();
				builderqzh.append(zhinfo.getQhjc());
				builderqzh.append("(");
				builderqzh.append(zhinfo.getsYear());
				builderqzh.append(")");
				builderqzh.append(zhinfo.getQhmc());
				if (("GETBDCQZH").equals(type)) {
					builderqzh.append("不动产权第");
				} else {
					builderqzh.append("不动产证明第");
				}
				if("2".equals(createqzhtype)||"3".equals(createqzhtype)){
					String qhstr="00";
					if(qhdm.length()>=6){
						qhstr=qhdm.substring(4, 6);
					}
					builderqzh.append(qhstr);
					builderqzh.append(StringHelper.PadLeft(StringHelper.formatDouble(zhinfo.getMinzh() + izs), 5, '0'));
				}else{
					builderqzh.append(StringHelper.PadLeft(StringHelper.formatDouble(zhinfo.getMinzh() + izs), 7, '0'));
				}
				builderqzh.append("号");
				String bdcqzh = builderqzh.toString();
				listqzh.add(bdcqzh);
			}
		}
		if (listqzh.size() <= 0) {
			return "";
		}
		// 获取djdyid-groupid-qlid-qllx-qlrid-qlrmc-zsid关系，用于封装证书
		String sqlgroupid="DJDY.GROUPID";
		if(comboDJFlag){
			if(QLLX.DIYQ.Value.equals(_qllx)){
				sqlgroupid="QL.GROUPID";
			}
		}
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT DJDY.DJDYID,"+sqlgroupid+",QL.QLID,QL.QLLX,QLR.QLRID,QLR.QLRMC,ZS.ZSID ");
		builder.append("FROM BDCK.BDCS_QDZR_GZ QDZR ");
		builder.append("LEFT JOIN BDCK.BDCS_QLR_GZ QLR ON QLR.QLRID=QDZR.QLRID AND QLR.XMBH=QDZR.XMBH ");
		builder.append("LEFT JOIN BDCK.BDCS_QL_GZ QL ON QLR.QLID=QL.QLID AND QDZR.QLID=QL.QLID AND QL.XMBH=QDZR.XMBH ");
		builder.append("LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON QL.DJDYID=DJDY.DJDYID AND DJDY.XMBH=QDZR.XMBH ");
		builder.append("LEFT JOIN BDCK.BDCS_ZS_GZ ZS ON ZS.ZSID=QDZR.ZSID AND ZS.XMBH=QDZR.XMBH ");
		builder.append("WHERE QL.QLID IS NOT NULL AND QLR.QLRID IS NOT NULL AND DJDY.DJDYID IS NOT NULL ");
		builder.append("AND QDZR.XMBH='");
		builder.append(xmbh);
		builder.append("' ");
		builder.append("AND QL.QLLX='");
		builder.append(_qllx);
		builder.append("' ORDER BY QL.QLID");
		List<Map> listQL = baseCommonDao.getDataListByFullSql(builder.toString());
		if (listQL == null || listQL.size() <= 0) {
			return "";
		}

		HashMap<String, String> qlrid_zsid = new HashMap<String, String>();// 权利人-证书关系
		HashMap<String, List<String>> qlid_zsid = new HashMap<String, List<String>>();// 权利-证书关系
		HashMap<String, String> zsid_bdcqzh = new HashMap<String, String>();// 证书-证号关系
		HashMap<String, String> zsid_bdcqzhxh = new HashMap<String, String>();// 证书-证号序号关系
		HashMap<String, String> groupid_qllx_qlrmc_bdcqzh = new HashMap<String, String>();// 单元分组-权利类型-权利人名称-证号关系
		HashMap<String, String> groupid_qllx_qlrmc_bdcqzhxh = new HashMap<String, String>();// 单元分组-权利类型-权利人名称-证号序号关系
		// 封装各种关系，用于最后赋值
		for (Map ql : listQL) {
			String groupid = StringHelper.formatObject(ql.get("GROUPID"));
			String qlid = StringHelper.formatObject(ql.get("QLID"));
			String qllx = StringHelper.formatObject(ql.get("QLLX"));
			if (!qllx.equals(_qllx)) {
				continue;
			}
			String qlrid = StringHelper.formatObject(ql.get("QLRID"));
			String qlrmc = StringHelper.formatObject(ql.get("QLRMC"));
			String zsid = StringHelper.formatObject(ql.get("ZSID"));
			String bdcqzh = "";
			String bdcqzhxh = "";
			if (zsid_bdcqzh.containsKey(zsid)) {
				bdcqzh = zsid_bdcqzh.get(zsid);
				if (zsid_bdcqzhxh.containsKey(zsid)) {
					bdcqzhxh = zsid_bdcqzhxh.get(zsid);
				}
			} else {
				if (groupid_qllx_qlrmc_bdcqzh.containsKey(groupid + "&&" + qllx + "&&" + qlrmc)) {
					bdcqzh = groupid_qllx_qlrmc_bdcqzh.get(groupid + "&&" + qllx + "&&" + qlrmc);
					if (groupid_qllx_qlrmc_bdcqzhxh.containsKey(groupid + "&&" + qllx + "&&" + qlrmc)) {
						bdcqzhxh = groupid_qllx_qlrmc_bdcqzhxh.get(groupid + "&&" + qllx + "&&" + qlrmc);
					}
				}
			}

			if (StringHelper.isEmpty(bdcqzh) && listqzh.size() > 0) {
				bdcqzh = listqzh.get(0);
				bdcqzhxh = getBDCQZHXH(bdcqzh);
				listqzh.remove(0);
				if (StringHelper.isEmpty(newcqzh)) {
					newcqzh = bdcqzh;
				}
			}
			if (!zsid_bdcqzh.containsKey(zsid)) {
				zsid_bdcqzh.put(zsid, bdcqzh);
			}
			if (!zsid_bdcqzhxh.containsKey(zsid)) {
				zsid_bdcqzhxh.put(zsid, bdcqzhxh);
			}
			if (!qlrid_zsid.containsKey(qlrid)) {
				qlrid_zsid.put(qlrid, zsid);
			}
			if (qlid_zsid.containsKey(qlid)) {
				List<String> list_zsid = qlid_zsid.get(qlid);
				if (!list_zsid.contains(zsid)) {
					list_zsid.add(zsid);
					qlid_zsid.remove(qlid);
					qlid_zsid.put(qlid, list_zsid);
				}
			} else {
				List<String> list_zsid = new ArrayList<String>();
				list_zsid.add(zsid);
				qlid_zsid.remove(qlid);
				qlid_zsid.put(qlid, list_zsid);
			}

			if (!groupid_qllx_qlrmc_bdcqzh.containsKey(groupid + "&&" + qllx + "&&" + qlrmc)) {
				groupid_qllx_qlrmc_bdcqzh.put(groupid + "&&" + qllx + "&&" + qlrmc, bdcqzh);
			}
			if (!groupid_qllx_qlrmc_bdcqzhxh.containsKey(groupid + "&&" + qllx + "&&" + qlrmc)) {
				groupid_qllx_qlrmc_bdcqzhxh.put(groupid + "&&" + qllx + "&&" + qlrmc, bdcqzhxh);
			}
		}
		JSONObject json=new JSONObject();
		// 对权利表中证号、证号序号赋值
		List<BDCS_QL_GZ> rights = baseCommonDao.getDataList(BDCS_QL_GZ.class, "XMBH='" + xmbh + "'");
		if (rights != null && rights.size() > 0) {
			JSONObject jsonqls=new JSONObject();
			int temp=0;
			for (BDCS_QL_GZ right : rights) {
			     JSONObject jsonql=new JSONObject();
			     temp++;
				String qlid = right.getId();
				if (!qlid_zsid.containsKey(qlid)) {
					continue;
				}
				List<String> listzsid = qlid_zsid.get(qlid);
				if (listzsid == null || listzsid.size() <= 0) {
					continue;
				}
				List<String> list_bdcqzh = new ArrayList<String>();
				List<String> list_bdcqzhxh = new ArrayList<String>();
				for (String zsid : listzsid) {
					if (zsid_bdcqzh.containsKey(zsid)) {
						String bdqzh = zsid_bdcqzh.get(zsid);
						if (!list_bdcqzh.contains(bdqzh)) {
							list_bdcqzh.add(bdqzh);
						}
					}
					if (zsid_bdcqzhxh.containsKey(zsid)) {
						String bdqzhxh = zsid_bdcqzhxh.get(zsid);
						if (!list_bdcqzhxh.contains(bdqzhxh)) {
							list_bdcqzhxh.add(bdqzhxh);
						}
					}
				}
				Collections.sort(list_bdcqzh);
				Collections.sort(list_bdcqzhxh);
				right.setBDCQZH(StringHelper.formatList(list_bdcqzh, ","));
				right.setBDCQZHXH(StringHelper.formatList(list_bdcqzhxh, ","));
				jsonql.put("QLID", qlid);
				jsonql.put("QL_BDCQZH", StringHelper.formatList(list_bdcqzh, ","));
				jsonqls.put("QL_INFO:序号（"+temp+")", jsonql);
				baseCommonDao.update(right);
			}
			json.put("QL_INFOS", jsonqls);
		}
		// 对权利人表中证号、证号序号赋值
		List<BDCS_QLR_GZ> qlrs = baseCommonDao.getDataList(BDCS_QLR_GZ.class, "XMBH='" + xmbh + "'");
		if (qlrs != null && qlrs.size() > 0) {
			JSONObject jsonqlrs=new JSONObject();
			int temp=0;
			for (BDCS_QLR_GZ qlr : qlrs) {
				JSONObject jsonqlr=new JSONObject();
				temp++;
				String qlrid = qlr.getId();
				if (!qlrid_zsid.containsKey(qlrid)) {
					continue;
				}
				String zsid = qlrid_zsid.get(qlrid);
				if (zsid_bdcqzh.containsKey(zsid)) {
					String bdqzh = zsid_bdcqzh.get(zsid);
					jsonqlr.put("QLR_BDCQZH",bdqzh);
					jsonqlr.put("QLR_ZSID", zsid);
					jsonqlr.put("QLR_QLID", qlr.getQLID());
					jsonqlr.put("QLRID", qlr.getId());
					jsonqlrs.put("QLR_INFO:序号（"+temp+")", jsonqlr);
					qlr.setBDCQZH(bdqzh);
				}
				if (zsid_bdcqzhxh.containsKey(zsid)) {
					String bdqzhxh = zsid_bdcqzhxh.get(zsid);
					qlr.setBDCQZHXH(bdqzhxh);
					jsonqlr.put("QLR_BDCQZHXH", bdqzhxh);
					jsonqlr.put("QLR_QLID", qlr.getQLID());
					jsonqlr.put("QLRID", qlr.getId());
					jsonqlrs.put("QLR_INFO:序号（"+temp+")", jsonqlr);
				}
				baseCommonDao.update(qlr);
			}
			json.put("QLR_INFOS", jsonqlrs);
		}
		// 对证书表中证号赋值
		List<BDCS_ZS_GZ> zss = baseCommonDao.getDataList(BDCS_ZS_GZ.class, "XMBH='" + xmbh + "'");
		if (qlrs != null && qlrs.size() > 0) {
			JSONObject jsonzss=new JSONObject();
			int temp =0;
			for (BDCS_ZS_GZ zs : zss) {
				String zsid = zs.getId();
				JSONObject jsonzs= new JSONObject();
				if (zsid_bdcqzh.containsKey(zsid)) {
					String bdqzh = zsid_bdcqzh.get(zsid);
					zs.setBDCQZH(bdqzh);
					temp++;
					jsonzs.put("ZSID", zsid);
					jsonzs.put("ZS_BDCQZH", bdqzh);
					jsonzss.put("ZS_INFO:序号（"+temp+")", jsonzs);
				}
				baseCommonDao.update(zs);
			}
			json.put("ZS_INFOS", jsonzss);
		}
		jsonObject.put("权证号赋值信息日志", json);
		NewLogTools.saveLog(jsonObject.toString(), xmbh, "6", "获取权证号日志及权证号赋值信息");
		baseCommonDao.flush();
		return newcqzh;
	}

	/**
	 * 获取地役权信息
	 * @param xmbh
	 * @param qlid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map getDyqDjbInfos(String xmbh,String qlid){
		Map<String, String> map = new HashMap<String, String>();
		String djlx="";
		String djyy="";
		String dyqnr="";
		String dyqlysj="";
		String bdcqzh="";
		String djsj="";
		String dbr="";
		String fj="";
		/*String zxsj="";
		String zxbbr="";
		String zxfj="";*/
		Rights rights=RightsTools.loadRights(DJDYLY.GZ, qlid);
		if(rights !=null){
			BDCS_QL_GZ ql_gz=(BDCS_QL_GZ)rights;
			String qssj = StringHelper.FormatDateOnType(ql_gz.getQLQSSJ(), "yyyy年MM月dd日")+ "起";
			String jssj = StringHelper.FormatDateOnType(ql_gz.getQLJSSJ(), "yyyy年MM月dd日") + "止";
			dyqlysj=qssj+jssj;
			djyy=StringHelper.FormatByDatatype(ql_gz.getDJYY());
			bdcqzh=StringHelper.FormatByDatatype(ql_gz.getBDCQZH());
			djsj=StringHelper.FormatDateOnType(ql_gz.getDJSJ(), "yyyy年MM月dd日");
			dbr=StringHelper.FormatByDatatype(ql_gz.getDBR());
			fj=StringHelper.FormatByDatatype(ql_gz.getFJ());
			SubRights subrights=RightsTools.loadSubRights(DJDYLY.GZ, rights.getFSQLID());
			if(subrights !=null){
				dyqnr=StringHelper.FormatByDatatype(subrights.getDYQNR());
			}
		}
		//获取供役地信息
		map=getGYDInfo(rights,xmbh,map);
		//获取需役地信息
		map=getXYDInfo(rights,xmbh,map);
	   BDCS_XMXX xmxx=Global.getXMXXbyXMBH(xmbh);
	   if(xmxx !=null){
		   DJLX lx=DJLX.initFrom(xmxx.getDJLX());
		   djlx= lx.Name;
	   }
		map.put("DJLX", djlx);
		map.put("DJYY", djyy);
		map.put("DYQNR", dyqnr);
		map.put("DYQLYSJ", dyqlysj);
		map.put("BDCQZH", bdcqzh);
		map.put("DJSJ", djsj);
		map.put("DBR", dbr);
		map.put("FJ", fj);
		return map;
	}
	/**
	 * 获取供役地信息
	 * @param ql_gz
	 * @param xmbh
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Map getGYDInfo(Rights rights ,String xmbh,Map<String, String> map){
		String gydbdcdyh="";
		String gydzl="";
		String gydqlr="";
		String gydzjzl="";
		String gydzjhm="";
		if(rights !=null){
			BDCS_QL_GZ ql_gz=(BDCS_QL_GZ)rights;
			String gydbdcdyid=ql_gz.getGYDBDCDYID();
			String gydbdcdylx=ql_gz.getGYDBDCDYLX();
			RealUnit unit=UnitTools.loadUnit(BDCDYLX.initFrom(gydbdcdylx), DJDYLY.LS, gydbdcdyid);
			if(unit ==null){
				unit=UnitTools.loadUnit(BDCDYLX.initFrom(gydbdcdylx), DJDYLY.GZ, gydbdcdyid);
			}
			if(unit !=null){
				gydbdcdyh=StringHelper.FormatByDatatype(unit.getBDCDYH());
				 gydzl=StringHelper.FormatByDatatype(unit.getZL());
				 List<String> ywrmcList = new ArrayList<String>();
				 List<BDCS_SQR> ywrLists = baseCommonDao.getDataList(BDCS_SQR.class,"xmbh = '" + xmbh + "' AND SQRLB='" + SQRLB.YF.Value + "'");
					if(ywrLists != null && ywrLists.size()>0){
						for(BDCS_SQR ywr : ywrLists){
							String ywrmc =StringHelper.FormatByDatatype(ywr.getSQRXM());
							String ywrzjh=StringHelper.FormatByDatatype(ywr.getZJH());
							String ywrzjzl=StringHelper.FormatByDatatype(ywr.getZJLX());
							String mc=ywrmc+"&"+ywrzjh+"&"+ywrzjzl;
							if (!StringHelper.isEmpty(mc)) {
								if (!ywrmcList.contains(mc)) {
									ywrmcList.add(mc);
									gydqlr +=ywrmc+",";
									gydzjhm +=ywrzjh+",";
									gydzjzl+=ConstHelper.getNameByValue("ZJLX", ywrzjzl)+",";
								}
							}
						}
						if(gydqlr.endsWith(",")){
							gydqlr =gydqlr.substring(0, gydqlr.length()-1);
						}
						if(gydzjhm.endsWith(",")){
							gydzjhm=gydzjhm.substring(0, gydzjhm.length()-1);
						}
						if(gydzjzl.endsWith(",")){
							gydzjzl=gydzjzl.substring(0, gydzjzl.length()-1);
						}
					}
			}
		}
		map.put("GYDBDCDYH", gydbdcdyh);
		map.put("GYDZL", gydzl);
		map.put("GYDQLR", gydqlr);
		map.put("GYDZJZL", gydzjzl);
		map.put("GYDZJHM", gydzjhm);
		return map;
	}
	
	/**
	 * 需役地信息
	 * @param rights
	 * @param xmbh
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Map getXYDInfo(Rights rights,String xmbh,Map<String, String> map){
		String xydbdcdyh="";
		String xydzl="";
		String xydqlr="";
		String xydzjzl="";
		String xydzjhm="";
		if(rights !=null){
			List<RightsHolder> lst=	RightsHolderTools.loadRightsHolders(DJDYLY.GZ, rights.getId());
			if(lst !=null && lst.size()>0){
				 List<String> ywrmcList = new ArrayList<String>();
				 for(RightsHolder holder :lst){
					 String qlrmc =StringHelper.FormatByDatatype(holder.getQLRMC());
						String qlrzjh=StringHelper.FormatByDatatype(holder.getZJH());
						String qlrzjzl=StringHelper.FormatByDatatype(holder.getZJZL());
						String mc=qlrmc+"&"+qlrzjh+"&"+qlrzjzl;
						if (!StringHelper.isEmpty(mc)) {
							if (!ywrmcList.contains(mc)) {
								ywrmcList.add(mc);
								xydqlr=xydqlr+qlrmc+",";
								xydzjhm=xydzjhm+qlrzjh+",";
								xydzjzl=xydzjzl+ConstHelper.getNameByValue("ZJLX", qlrzjzl)+",";
							}
						}
					}
					if(xydqlr.endsWith(",")){
						xydqlr=xydqlr.substring(0, xydqlr.length()-1);
					}
					if(xydzjhm.endsWith(",")){
						xydzjhm=xydzjhm.substring(0, xydzjhm.length()-1);
					}
					if(xydzjzl.endsWith(",")){
						xydzjzl=xydzjzl.substring(0, xydzjzl.length()-1);
					}
				 }
			List<BDCS_DJDY_GZ> lstdjdy=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, ProjectHelper.GetXMBHCondition(xmbh)+" AND DJDYID='"+rights.getDJDYID()+"'");
			if(lstdjdy !=null && lstdjdy.size()>0){
		        BDCDYLX lx=BDCDYLX.initFrom(lstdjdy.get(0).getBDCDYLX());
		        String bdcdyid=lstdjdy.get(0).getBDCDYID();
				RealUnit unit=UnitTools.loadUnit(lx, DJDYLY.LS, bdcdyid);
				if(unit ==null){
					unit=UnitTools.loadUnit(lx, DJDYLY.GZ, bdcdyid);
				}
				if(unit !=null){
					 xydbdcdyh=StringHelper.FormatByDatatype(unit.getBDCDYH());
					 xydzl=StringHelper.FormatByDatatype(unit.getZL());
				}
			}
			}
		map.put("XYDBDCDYH", xydbdcdyh);
		map.put("XYDZL", xydzl);
		map.put("XYDQLR", xydqlr);
		map.put("XYDZJZL", xydzjzl);
		map.put("XYDZJHM", xydzjhm);
		return map;
	}
	/**
	 * 获取抵押权登簿信息
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map getDYQDJBInfo(String xmbh, String qlid) {
		// 信息
		ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
		Map<String, String> map = new HashMap<String, String>();
		CommonDao dao = baseCommonDao;
		BDCS_QL_GZ ql = dao.get(BDCS_QL_GZ.class, qlid);
		BDCS_FSQL_GZ fsql = dao.get(BDCS_FSQL_GZ.class, ql == null ? "": ql.getFSQLID());
		if (!StringHelper.isEmpty(ql) && !StringHelper.isEmpty(fsql)) {
        /*------start--------------抵押登簿增加字段抵押人证件种类证件号码----------------------------------------------------------------*/
		String dyrzjzl = "";
		String dyrzjhm = "";
		String dyr_zjlx = "";
		String dyr_zjhm ="";
		String [] alldyr = null;
			String dyr = !StringHelper.isEmpty(fsql) ? ((fsql.getDYR() == null) ? "" : fsql.getDYR()) : "";
		//多个抵押人的情况,逗号分割
		if(dyr!=""){
			if(dyr.contains(",") || dyr.contains("，")|| dyr.contains("、")){
				if(dyr.contains(",") && !dyr.contains("，")&& !dyr.contains("、")){
					 alldyr = dyr.split(",");
				}
			    if(dyr.contains("，") && !dyr.contains(",")&& !dyr.contains("、")){
			    	 alldyr = dyr.split("，");
			    }
			    if(!dyr.contains("，") && !dyr.contains(",")&&dyr.contains("、")){
			    	 alldyr = dyr.split("、");
			    }
				List<String> alldyr_list=java.util.Arrays.asList(alldyr);
				for(int i =0; i<alldyr_list.size();i++ ){
					String dyr_one = alldyr_list.get(i).toString().trim();
					BDCS_SQR sqr_dyr = new BDCS_SQR();
					//暂时不考虑 该项目中重名的情况（名字相同 证件号不同）
					List<BDCS_SQR> sqrlist = dao.getDataList(BDCS_SQR.class, " SQRXM = '"+dyr_one+"' AND XMBH= '"+xmbh+"'");
					if(sqrlist!=null && sqrlist.size()>0){
						sqr_dyr = sqrlist.get(0);
					    dyr_zjlx = (sqr_dyr.getZJLX()== null ? "" : sqr_dyr.getZJLX().toString());
					    dyr_zjhm = (sqr_dyr.getZJH()== null ? "" : sqr_dyr.getZJH().toString());
						List<BDCS_CONST> const_zjlx= dao.getDataList(BDCS_CONST.class, " CONSTSLSID='30' AND CONSTVALUE='"+dyr_zjlx+"'");//30指证件类型
						if(const_zjlx!=null && const_zjlx.size()>0){
							dyr_zjlx = const_zjlx.get(0).getCONSTTRANS();
						}
						if(i+1==alldyr_list.size()){
							dyrzjzl += dyr_zjlx;
							dyrzjhm += dyr_zjhm;
						}else{
							dyrzjzl += dyr_zjlx +",";
							dyrzjhm += dyr_zjhm +",";
						}
						
					}
				}
			}else{  
					List<BDCS_SQR> sqrlist_ = dao.getDataList(BDCS_SQR.class, " SQRXM = '"+dyr+"' AND XMBH= '"+xmbh+"'");
					if(sqrlist_!=null && sqrlist_.size()>0){
						BDCS_SQR sqrdyr=sqrlist_.get(0);
						 dyr_zjlx = (sqrdyr.getZJLX()== null ? "" : sqrdyr.getZJLX().toString());
						    dyr_zjhm = (sqrdyr.getZJH()== null ? "" : sqrdyr.getZJH().toString());
							List<BDCS_CONST> const_zjlx= dao.getDataList(BDCS_CONST.class, " CONSTSLSID='30' AND CONSTVALUE='"+dyr_zjlx+"'");//30指证件类型
							if(const_zjlx!=null && const_zjlx.size()>0){
								dyr_zjlx = const_zjlx.get(0).getCONSTTRANS();
							}
							dyrzjzl += dyr_zjlx ;
							dyrzjhm += dyr_zjhm ;
				}
			}
		}
        /*-------end------------------------------------------------------------------------------------------------------------------------*/
		
		/*-------start--------------判断当前登记单元类型返回给前台，前台判断是否是期房------------------------------------------*/
		// 参数： xmbh， djdyid  // 结果给出String lx值
		String lx="";
		List<BDCS_DJDY_GZ> djdy_gz = dao.getDataList(BDCS_DJDY_GZ.class, " DJDYID='"+ql.getDJDYID()+"' AND XMBH='"+xmbh+"'");
		lx = (djdy_gz!=null && djdy_gz.size()>0) ? (djdy_gz.get(0).getBDCDYLX()) : null ;
		String bdcdyid = (djdy_gz!=null && djdy_gz.size()>0) ? (djdy_gz.get(0).getBDCDYID()) : null ;
//		if (lx==null || lx==""){
//			List<BDCS_DJDY_XZ> djdy_xz = dao.getDataList(BDCS_DJDY_XZ.class, " DJDYID='"+ql.getDJDYID()+"'");
//			lx = (djdy_xz!=null && djdy_xz.size()>0) ? (djdy_xz.get(0).getBDCDYLX()) : null ;
//		}
		/*-------end----------------------------------------------------------------------------------------------------------*/

		
		BDCS_QLR_GZ qlr = new BDCS_QLR_GZ();
		List<BDCS_QLR_GZ> listqlr = dao.getDataList(BDCS_QLR_GZ.class, " QLID='" + qlid + "'");
		String qlrmc = "";
		String zjzlName = "";
		String zjh = "";
		for (int i = 0; i < listqlr.size(); i++) {
			qlr = listqlr.get(i);
			if (i + 1 == listqlr.size()) {
				qlrmc += (qlr.getQLRMC() == null ? "" : qlr.getQLRMC());
				zjzlName += (qlr.getZJZLName() == null ? "" : qlr.getZJZLName());
				zjh += (qlr.getZJH() == null ? "" : qlr.getZJH());
			} else {
				qlrmc += (qlr.getQLRMC() == null ? "" : qlr.getQLRMC()) + ",";
				zjzlName += (qlr.getZJZLName() == null ? "" : qlr.getZJZLName()) + ",";
				zjh += (qlr.getZJH() == null ? "" : qlr.getZJH()) + ",";
			}
		}
		map.put("bdcdyh", (ql.getBDCDYH() == null ? "" : ql.getBDCDYH()));
		map.put("bdcdylx", lx);
		map.put("dywlx", (fsql.getDYWLXName() == null ? "" : fsql.getDYWLXName()));
		map.put("dyqr", qlrmc);
		map.put("zjzl", zjzlName);
		map.put("zjhm", zjh);
		map.put("dyr", dyr);
		map.put("dyrzjzl", dyrzjzl);
		map.put("dyrzjhm",dyrzjhm);
		map.put("dyfs", (fsql.getDYFSName() == null ? "" : fsql.getDYFSName()));
		if(info.getBaseworkflowcode().equals("ZY007")||info.getBaseworkflowcode().equals("ZY118")
					|| info.getBaseworkflowcode().equals("ZY016") || info.getBaseworkflowcode().equals("ZY316")
					|| info.getBaseworkflowcode().equals("ZY116")) {
			map.put("djlx", ConstHelper.getNameByValue("DJLX", ql.getDJLX()));
		}else{
			map.put("djlx", ConstHelper.getNameByValue("DJLX", info.getDjlx()));
		}
		map.put("djyy", (ql.getDJYY() == null ? "" : ql.getDJYY()));
		map.put("zjjzwdyfw", (fsql.getZJJZWDYFW() == null ? "" : fsql.getZJJZWDYFW()));
		BDCDYLX dylx = BDCDYLX.initFrom(lx);
		DJDYLY ly = DJDYLY.initFromByEnumName("LS");
		String zl = "";
		RealUnit unit = UnitTools.loadUnit(dylx, ly, bdcdyid);
		if (unit != null) {
			zl = unit.getZL();
		}
		map.put("zjjzwzl", (fsql.getZJJZWZL() == null ? zl : fsql.getZJJZWZL()));
		if (!StringHelper.isEmpty(fsql.getDYFS())) {
			if (fsql.getDYFS().equals("1")) {
				map.put("bdbzzqse", fsql.getBDBZZQSE() == null ? ""
						: StringHelper.formatDouble(fsql.getBDBZZQSE()));
				/*
				 * if (!fsql.getZQDW().isEmpty()) { if
				 * (fsql.getZQDW().equals("1") || fsql.getZQDW().equals("2")) {
				 * map.put("bdbzzqse", fsql.getBDBZZQSE() == null ? "" : String
				 * .format("%.2f", new BigDecimal(fsql .getBDBZZQSE()))); } else
				 * { map.put("bdbzzqse", fsql.getBDBZZQSE() == null ? "" :
				 * StringHelper .formatObject(new BigDecimal(fsql
				 * .getBDBZZQSE()))); }
				 * 
				 * } else { map.put("bdbzzqse", fsql.getBDBZZQSE() == null ? ""
				 * : StringHelper .formatObject(new BigDecimal(fsql
				 * .getBDBZZQSE()))); }
				 */
			} else if (fsql.getDYFS().equals("2")) {
				map.put("bdbzzqse",
						fsql.getZGZQSE() == null ? "" : StringHelper.formatDouble(fsql.getZGZQSE()));
				/*
				 * if (!fsql.getZQDW().isEmpty()) { if
				 * (fsql.getZQDW().equals("1") || fsql.getZQDW().equals("2")) {
				 * map.put("bdbzzqse", fsql.getZGZQSE() == null ? "" : String
				 * .format("%.2f", new BigDecimal(fsql.getZGZQSE()))); } else {
				 * map.put("bdbzzqse", fsql.getZGZQSE() == null ? "" :
				 * StringHelper .formatObject(new
				 * BigDecimal(fsql.getZGZQSE()))); } } else {
				 * map.put("bdbzzqse", fsql.getZGZQSE() == null ? "" :
				 * StringHelper .formatObject(new
				 * BigDecimal(fsql.getZGZQSE()))); }
				 */
			}
		}
		String qssj = DateUtil.FormatByDatetime(ql.getQLQSSJ());
		String jssj = DateUtil.FormatByDatetime(ql.getQLJSSJ());
		map.put("zwlxqx", qssj + "起" + jssj + "止");
		map.put("zgzqqdss", (fsql.getZGZQQDSS() == null ? "" : fsql.getZGZQQDSS()));
		map.put("bdcdjzmh", (ql.getBDCQZH() == null ? "" : ql.getBDCQZH()));
		/*
		 * 抵押登记注销的登簿页面应该有两个登簿人，初始和注销
		 * 以下这三个值应该是获取初始合并登记的信息
		 * heks  20170320 10:46:00
		 */
		map.put("djsj", StringHelper.FormatYmdhmsByDate(ql.getDJSJ()));
		map.put("dbr", (ql.getDBR() == null ? "" : ql.getDBR()));
		map.put("fj", (ql.getFJ() == null ? "" : ql.getFJ()));
		
		map.put("zxdyywh", (fsql.getZXDYYWH() == null ? "" : fsql.getZXDYYWH()));
		map.put("zxdyyy", (fsql.getZXDYYY() == null ? "" : fsql.getZXDYYY()));
		map.put("zxsj", StringHelper.FormatYmdhmsByDate(fsql.getZXSJ()));
		map.put("zxfj", (fsql.getZXFJ() == null ? "" : fsql.getZXFJ()));
		map.put("zxdbr", (fsql.getZXDBR() == null ? "" : fsql.getZXDBR()));
		String bdcdyh = getRealUnitBdcdyh(qlid, xmbh);
		map.put("bdcdyh", bdcdyh);
		map.put("zqdw", ConstHelper.getNameByValue("JEDW", fsql.getZQDW()));
		}
		// 刘树峰：权利中应该有两个登簿人，初始和注销
		return map;
	}

	/**
	 * 生成不动产登记证明号
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月22日下午5:43:00
	 * @param xmbh
	 * @param qlid2
	 * @return
	 */
	@Override
	public String createBDCZMH(String xmbh, String qlid2, String type) {
		BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);
		//已经登簿的业务，无法再次重新获取权证号 liangqin
		if("1".equals(xmxx.getSFDB())){
			NewLogTools.saveLog("重新获取权证号失败，"+xmxx.getYWLSH()+"该项目已经登簿", xmbh, "6", "获取权证号日志及权证号赋值信息");
			return "";
		}
		JSONObject jsonObject = NewLogTools.getJSONByXMBH(xmbh);
		jsonObject.put("OperateType", "获取权证号日志及权证号赋值信息");
		if (xmxx.getSFHBZS() != null && xmxx.getSFHBZS().equals(SF.YES.Value)) {
			jsonObject.put("IsMulti_Units", "true");
			return getbdcqzh(xmbh, qlid2, type,jsonObject);
		}
		jsonObject.put("IsMulti_Units", "false");
		String qhdm = ConfigHelper.getNameByValue("XZQHDM");
		List<BDCS_ZS_GZ> zss = baseCommonDao.getDataList(BDCS_ZS_GZ.class, " XMBH='" + xmbh + "'");
		List<String> listqzh = new ArrayList<String>();
		if (zss != null && zss.size() > 0) {
			String createqzhtype=ConfigHelper.getNameByValue("CREATEQZHTYPE");
			if("2".equals(createqzhtype)){
				qhdm="";
				StringBuilder builder=new StringBuilder();
				builder.append("SELECT TUSER.AREACODE FROM SMWB_FRAMEWORK.T_USER TUSER ");
				builder.append("LEFT JOIN BDC_WORKFLOW.WFI_PROINST PROINST  ON TUSER.ID=PROINST.STAFF_ID ");
				builder.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON PROINST.FILE_NUMBER=XMXX.PROJECT_ID ");
				builder.append("WHERE XMBH='");
				builder.append(xmbh);
				builder.append("'");
				List<Map> list_user=baseCommonDao.getDataListByFullSql(builder.toString());
				if(list_user!=null&&list_user.size()>0){
					qhdm=StringHelper.formatObject(list_user.get(0).get("AREACODE"));
					
				}
			}if("3".equals(createqzhtype)){
				qhdm=Global.getCurrentUserInfo().getAreaCode();
			}
			ZHInfo zhinfo = this.GetBDCZM(qhdm, type, zss.size(), xmbh,jsonObject);
			if (zhinfo != null && !StringHelper.isEmpty(zhinfo.getMinzh()) && zhinfo.getMinzh() > 0
					&& !StringHelper.isEmpty(zhinfo.getsYear()) && !StringHelper.isEmpty(zhinfo.getQhjc())
					&& !StringHelper.isEmpty(zhinfo.getQhmc())) {
				for (int izs = 0; izs < zss.size(); izs++) {
					StringBuilder builderqzh = new StringBuilder();
					builderqzh.append(zhinfo.getQhjc());
					builderqzh.append("(");
					builderqzh.append(zhinfo.getsYear());
					builderqzh.append(")");
					builderqzh.append(zhinfo.getQhmc());
					builderqzh.append("不动产证明第");
					if("2".equals(createqzhtype)||"3".equals(createqzhtype)){
						String qhstr="00";
						if(qhdm.length()>=6){
							qhstr=qhdm.substring(4, 6);
						}
						builderqzh.append(qhstr);
						builderqzh.append(StringHelper.PadLeft(StringHelper.formatDouble(zhinfo.getMinzh() + izs), 5, '0'));
					}else{
						builderqzh.append(StringHelper.PadLeft(StringHelper.formatDouble(zhinfo.getMinzh() + izs), 7, '0'));
					}
					builderqzh.append("号");
					String bdcqzh = builderqzh.toString();
					listqzh.add(bdcqzh);
				}
			}
		}

		String newcqzh = "";
		List<BDCS_QL_GZ> qls = baseCommonDao.getDataList(BDCS_QL_GZ.class, " XMBH='" + xmbh + "'");
		if (qls != null && qls.size() > 0) {
			JSONObject jsonqls=new JSONObject();
			for (int ii = 0; ii < qls.size(); ii++) {
				String qlid = qls.get(ii).getId();
				JSONObject jsonql=new JSONObject();
				jsonql.put("QLID", qlid);
				String cqzh = "";
				String sql = MessageFormat.format(
						" id IN (SELECT ZSID FROM BDCS_QDZR_GZ WHERE XMBH=''{0}'' AND QLID=''{1}'')", xmbh, qlid);
				List<BDCS_ZS_GZ> list = baseCommonDao.getDataList(BDCS_ZS_GZ.class, sql);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						BDCS_ZS_GZ zs = list.get(i);
						JSONObject jsonzs=new JSONObject();
                        jsonzs.put("ZSID", zs.getId());
						if (!StringUtils.isEmpty(zs.getBDCQZH())) {
							cqzh += zs.getBDCQZH() + ",";
							jsonzs.put("证书描述", "证书的不动产权证号有值，跳过权利及权利人赋值");
							jsonql.put("QL_ZSAndQlrInfo:序号("+(ii+1)+")", jsonzs);
							continue;
						}
						String bdcqzh = listqzh.get(0);
						listqzh.remove(0);
						//bdcqzh = bdcqzh.replace("权", "证明");
						zs.setBDCQZH(bdcqzh);
						cqzh += bdcqzh + ",";
						newcqzh += bdcqzh + ",";
						String hql = MessageFormat.format(
								" id IN(SELECT QLRID FROM BDCS_QDZR_GZ WHERE XMBH=''{0}'' AND QLID=''{1}'' AND ZSID=''{2}'')",
								xmbh, qlid, zs.getId());
						List<BDCS_QLR_GZ> qlrList = baseCommonDao.getDataList(BDCS_QLR_GZ.class, hql);
						if (qlrList != null && qlrList.size() > 0) {
							for (int j = 0; j < qlrList.size(); j++) {
								JSONObject jsonqlr=new JSONObject();
								BDCS_QLR_GZ qlr = qlrList.get(j);
								qlr.setBDCQZH(bdcqzh);
								qlr.setBDCQZHXH(getBDCQZHXH(bdcqzh));
								jsonqlr.put("QLRID", qlr.getId());
								jsonqlr.put("QLR-BDCQZH", bdcqzh);
								jsonzs.put("ZS_QlrInfo:序号("+(j+1)+")", jsonqlr);
								baseCommonDao.update(qlr);
							}
						}
						baseCommonDao.update(zs);
						jsonql.put("QL_ZsInfo:序号("+(i+1)+")", jsonzs);
					}
					BDCS_QL_GZ ql = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
					if (ql != null) {
						ql.setBDCQZH(cqzh.substring(0, cqzh.length() - 1));
						jsonql.put("QL-BDCQZH", cqzh.substring(0, cqzh.length() - 1));
						baseCommonDao.update(ql);
					}
				}
				jsonqls.put("QL_ZsAndQlrInfo:序号("+(ii+1)+")", jsonql);
			}
			jsonObject.put("权证号赋值信息日志", jsonqls);
		}
		NewLogTools.saveLog(jsonObject.toString(), xmbh, "6", "获取权证号日志及权证号赋值信息");
		baseCommonDao.flush();
		return newcqzh;
	}

	/**
	 * 获得查封登记登记簿信息
	 * 
	 * @Title: getCFDJBInfo
	 * @author:liushufeng
	 * @date：2015年7月19日 上午12:58:29
	 * @param xmbh
	 * @param qlid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map getCFDJBInfo(String xmbh, String qlid) {
		BDCS_FSQL_GZ fsql_gz = new BDCS_FSQL_GZ();
		BDCS_QL_GZ ql_gz = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
		Map<String, String> map = new HashMap<String, String>();
		String cfjg = ""; // 查封机关
		String cflx = ""; // 查封类型
		String cfwh = ""; // 查封文号
		String cffw = ""; // 查封范围
		String cfsj = ""; // 查封时间
		String cfwj = ""; // 查封文件
		String cfqx = ""; // 查封期限
		String cf_djsj = "";// 查封登记时间
		String cf_dbr = "";// 查封登薄人
		String jfjg = ""; // 解封机关
		String jfwh = ""; // 解封文号
		String jfwj = ""; // 解封文件
		String fj = ""; // 附件
		String jfywh = ""; // 解封业务号
		String jf_djsj = "";// 解封登记时间
		String jf_dbr = "";// 解封登薄人
		//添加不动产基本信息
		String bdcdyh = ""; //不动产单元号
		String zl = ""; //坐落
		BDCS_XMXX xmxx=Global.getXMXXbyXMBH(xmbh);
		if (ql_gz != null) {
			bdcdyh = ql_gz.getBDCDYH();
			fj = ql_gz.getFJ();
			// jfywh=ql_gz.getYWH();
			if(xmxx!=null){
				if(SF.YES.Value.equals(xmxx.getSFDB())){
					cf_djsj = StringHelper.FormatYmdhmsByDate(ql_gz.getDJSJ());
					cf_dbr = ql_gz.getDBR();
				}
			}
			cfsj = StringHelper.FormatYmdhmsByDate(ql_gz.getDJSJ());
			cfqx = StringHelper.FormatByDatetime(ql_gz.getQLQSSJ()) + " 起 "
					+ StringHelper.FormatByDatetime(ql_gz.getQLJSSJ()) + " 止";
			//获取坐落
			if(!"".equals(xmbh)){
				List<BDCS_DJDY_GZ> djdy_gz = baseCommonDao.getDataList(BDCS_DJDY_GZ.class,
						" xmbh = '"+xmbh+"' and bdcdyh = '"+ql_gz.getBDCDYH()+"'");
				if(djdy_gz.size()>0){
					if("031".equals(djdy_gz.get(0).getBDCDYLX())){
						//现房
						List<BDCS_H_XZ> H_XZ = baseCommonDao.getDataList(BDCS_H_XZ.class,
								" bdcdyid = '"+djdy_gz.get(0).getBDCDYID()+"'");
						if(H_XZ.size()>0){
							zl = H_XZ.get(0).getZL();
						}
						
					}else if("032".equals(djdy_gz.get(0).getBDCDYLX())){
						//期房
						List<BDCS_H_XZY> H_XZY = baseCommonDao.getDataList(BDCS_H_XZY.class,
								" bdcdyid = '"+djdy_gz.get(0).getBDCDYID()+"'");
						if(H_XZY.size()>0){
							zl = H_XZY.get(0).getZL();
						}
						
					}else if("02".equals(djdy_gz.get(0).getBDCDYLX())){
						//使用权宗地
						List<BDCS_SHYQZD_XZ> SHYQZD_XZ = baseCommonDao.getDataList(BDCS_SHYQZD_XZ.class,
								" bdcdyid = '"+djdy_gz.get(0).getBDCDYID()+"'");
						if(SHYQZD_XZ.size()>0){
							zl = SHYQZD_XZ.get(0).getZL();
						}
						
					}else if("09".equals(djdy_gz.get(0).getBDCDYLX())){
						//农用地
						List<BDCS_NYD_GZ> nyd = baseCommonDao.getDataList(BDCS_NYD_GZ.class, " BDCDYID='"+djdy_gz.get(0).getBDCDYID()+"'");
						if(nyd.size()>0){
							zl = nyd.get(0).getZL();
						}
						//宗海
					}else if("04".equals(djdy_gz.get(0).getBDCDYLX())){
						List<Map> m=baseCommonDao.getDataListByFullSql("select zl from bdcdck.bdcs_zh_gz zh where zh.bdcdyid='"+djdy_gz.get(0).getBDCDYID()+"'");
						if(m.size()>0){
							zl=StringUtil.valueOf(m.get(0).get("ZL"));
						}
					}else{
						
					}
					
					
				}
			}
			
			fsql_gz = baseCommonDao.get(BDCS_FSQL_GZ.class, ql_gz.getFSQLID());
			if (fsql_gz != null) {
				cfjg = fsql_gz.getCFJG();
				cflx = fsql_gz.getCFLX();
				cfwh = fsql_gz.getCFWH();
				cfwj = fsql_gz.getCFWJ();
				cffw = fsql_gz.getCFFW();
				// cfsj = StringHelper.FormatByDatetime(fsql_gz.getCFSJ());
				jfjg = fsql_gz.getJFJG();
				jfwh = fsql_gz.getJFWH();
				jfwj = fsql_gz.getJFWJ();
			}
		}
		
		map.put("bdcdyh", bdcdyh);
		map.put("zl", zl);
		map.put("cfjg", cfjg);
		map.put("cflx", ConstHelper.getNameByValue("CFLX", cflx));
		map.put("cfwh", cfwh);
		map.put("cffw", cffw);
		map.put("cfsj", cfsj);
		map.put("cfwj", cfwj);
		map.put("cfqx", cfqx);
		map.put("cf_djsj", cf_djsj);
		map.put("cf_dbr", cf_dbr);
		map.put("jfjg", jfjg);
		map.put("jfwh", jfwh);
		map.put("jfwj", jfwj);
		map.put("fj", fj);
		map.put("jfywh", jfywh);
		map.put("jf_djsj", jf_djsj);
		map.put("jf_dbr", jf_dbr);
		return map;
	}

	/**
	 * 获得解封登记登记簿信息
	 * 
	 * @Title: getJFDJBInfo
	 * @author:liushufeng
	 * @date：2015年7月19日 上午12:58:41
	 * @param xmbh
	 * @param qlid
	 * @param oldqlid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map getJFDJBInfo(String xmbh, String qlid, String oldqlid) {
		BDCS_FSQL_GZ fsql_gz = new BDCS_FSQL_GZ();
		BDCS_QL_GZ ql_gz = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
		// BDCS_QL_XZ ql_xz = baseCommonDao.get(BDCS_QL_XZ.class, oldqlid);
		Map<String, String> map = new HashMap<String, String>();
		String cfjg = ""; // 查封机关
		String cflx = ""; // 查封类型
		String cfwh = ""; // 查封文号
		String cffw = ""; // 查封范围
		String cfsj = ""; // 查封时间
		String cfwj = ""; // 查封文件
		String cfqx = ""; // 查封期限
		String cf_djsj = "";// 查封登记时间
		String cf_dbr = "";// 查封登薄人
		String jfjg = ""; // 解封机关
		String jfwh = ""; // 解封文号
		String jfwj = ""; // 解封文件
		String fj = ""; // 附件
		String jfywh = ""; // 解封业务号
		String jf_djsj = "";// 解封登记时间
		String jf_dbr = "";// 解封登薄人
		String bdcdyh = ql_gz.getBDCDYH();//获取不动产单元号
		String zl = "";
		if(!"".equals(xmbh)){
			List<BDCS_DJDY_GZ> djdy_gz = baseCommonDao.getDataList(BDCS_DJDY_GZ.class,
					" xmbh = '"+xmbh+"' and bdcdyh = '"+ql_gz.getBDCDYH()+"'");
			if(djdy_gz.size()>0){
				if("031".equals(djdy_gz.get(0).getBDCDYLX())){
					//现房
					List<BDCS_H_XZ> H_XZ = baseCommonDao.getDataList(BDCS_H_XZ.class,
							" bdcdyid = '"+djdy_gz.get(0).getBDCDYID()+"'");
					if(H_XZ.size()>0){
						zl = H_XZ.get(0).getZL();
					}
					
				}else if("032".equals(djdy_gz.get(0).getBDCDYLX())){
					//期房
					List<BDCS_H_XZY> H_XZY = baseCommonDao.getDataList(BDCS_H_XZY.class,
							" bdcdyid = '"+djdy_gz.get(0).getBDCDYID()+"'");
					if(H_XZY.size()>0){
						zl = H_XZY.get(0).getZL();
					}
					
				}else if("02".equals(djdy_gz.get(0).getBDCDYLX())){
					//使用权宗地
					List<BDCS_SHYQZD_XZ> SHYQZD_XZ = baseCommonDao.getDataList(BDCS_SHYQZD_XZ.class,
							" bdcdyid = '"+djdy_gz.get(0).getBDCDYID()+"'");
					if(SHYQZD_XZ.size()>0){
						zl = SHYQZD_XZ.get(0).getZL();
					}
					
				}else if("09".equals(djdy_gz.get(0).getBDCDYLX())){
					//农用地
					List<BDCS_NYD_GZ> nyd = baseCommonDao.getDataList(BDCS_NYD_GZ.class, " BDCDYID='"+djdy_gz.get(0).getBDCDYID()+"'");
					zl = nyd.get(0).getZL();
				}else{
					
				}
				
			}
		}
		
		if (ql_gz != null) {
			cf_dbr = ql_gz.getDBR();
			cf_djsj = StringHelper.FormatYmdhmsByDate(ql_gz.getDJSJ());

			cfqx = StringHelper.FormatByDatetime(ql_gz.getQLQSSJ()) + " 起 "
					+ StringHelper.FormatByDatetime(ql_gz.getQLJSSJ()) + " 止";
			fsql_gz = baseCommonDao.get(BDCS_FSQL_GZ.class, ql_gz.getFSQLID());
			if (fsql_gz != null) {
				fj = fsql_gz.getZXFJ();
				cfjg = fsql_gz.getCFJG();
				jf_djsj = StringHelper.FormatYmdhmsByDate(fsql_gz.getZXSJ());
				jf_dbr = fsql_gz.getZXDBR();
				cflx = fsql_gz.getCFLX();
				cfwh = fsql_gz.getCFWH();
				cfwj = fsql_gz.getCFWJ();
				cffw = fsql_gz.getCFFW();
				cfsj = StringHelper.FormatYmdhmsByDate(fsql_gz.getCFSJ());

				jfjg = fsql_gz.getJFJG();
				jfwh = fsql_gz.getJFWH();
				jfwj = fsql_gz.getJFWJ();
				jfywh = fsql_gz.getZXDYYWH();
			}
		}
		map.put("cfjg", cfjg);
		map.put("cflx", ConstHelper.getNameByValue("CFLX", cflx));
		map.put("cfwh", cfwh);
		map.put("cffw", cffw);
		map.put("cfsj", cfsj);
		map.put("cfwj", cfwj);
		map.put("cfqx", cfqx);
		map.put("cf_djsj", cf_djsj);
		map.put("cf_dbr", cf_dbr);
		map.put("jfjg", jfjg);
		map.put("jfwh", jfwh);
		map.put("jfwj", jfwj);
		map.put("fj", fj);
		map.put("jfywh", jfywh);
		map.put("jf_djsj", jf_djsj);
		map.put("jf_dbr", jf_dbr);
		map.put("bdcdyh", bdcdyh);
		map.put("zl", zl);
		return map;
	}

	/**
	 * 获取预告登记登记簿信息
	 * 
	 * @作者 李想
	 * @创建时间 2015年6月24日上午14:34:29
	 * @param xmbh
	 * @param djdyid
	 * @return
	 */
	public NoticeBook getNoticeBook(String xmbh, String djdyid) {
		ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
		List<BDCS_DJDY_GZ> djdys = this.getDJdys(xmbh, djdyid);
		NoticeBook nBook = new NoticeBook();

		nBook.setXMBH(xmbh);// 项目内码
		nBook.setACCEPT_CODE(info.getProject_id());// 业务号
		if (djdys.size() == 0) {
			return null;
		}

		// 1.权利人相关
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" id in (SELECT QLRID FROM BDCS_QDZR_GZ WHERE ");
		hqlBuilder.append(" XMBH='").append(xmbh).append("' AND DJDYID='").append(djdyid).append("') ORDER BY SXH");
		List<BDCS_QLR_GZ> qlrlist = baseCommonDao.getDataList(BDCS_QLR_GZ.class, hqlBuilder.toString());
		StringBuilder qlrMcSring = new StringBuilder();
		StringBuilder qlrZjlxSring = new StringBuilder();
		StringBuilder qlrZjhSring = new StringBuilder();
		for (int i = 0; i < qlrlist.size(); i++) {
			BDCS_QLR_GZ qlr = qlrlist.get(i);
			if (i == 0) {
				qlrMcSring.append(qlr.getQLRMC());
				qlrZjlxSring.append(ConstHelper.getNameByValue("ZJLX", qlr.getZJZL()));
				qlrZjhSring.append(qlr.getZJH());
			} else {
				qlrMcSring.append("/").append(qlr.getQLRMC());
				qlrZjlxSring.append("/").append(ConstHelper.getNameByValue("ZJLX", qlr.getZJZL()));
				qlrZjhSring.append("/").append(qlr.getZJH());
			}
		}
		nBook.setQLR(qlrMcSring.toString());// 权利人
		nBook.setQLRZJZL(qlrZjlxSring.toString());// 证件类型
		nBook.setQLRZJH(qlrZjhSring.toString());// 证件号
		//nBook.setTDSYQR(qlrMcSring.toString());// 土地使用权人

		// 2.附属权利相关
		StringBuilder hqlCondition = new StringBuilder();
		hqlCondition.append(" xmbh='").append(xmbh)
				.append("' and QLID IN (SELECT QLID FROM BDCS_QDZR_GZ WHERE DJDYID = '").append(djdyid).append("') ");
		List<BDCS_FSQL_GZ> fsqlList = baseCommonDao.getDataList(BDCS_FSQL_GZ.class, hqlCondition.toString());
		// 取得价格，被担保主债权数额
		String YGDJLX = "";
		String QDJG_BDBZZQSE = "";
		// 义务人相关
		if (fsqlList.size() > 0) {
			BDCS_FSQL_GZ fsql = fsqlList.get(0);
			nBook.setYWR(fsql.getYWR()); // 义务人
			// 证件类型
			if (!StringUtils.isEmpty(fsql.getYWRZJZL())) {
				String[] zjlxStrings = fsql.getYWRZJZL().split("/");
				if (zjlxStrings.length > 1) {
					StringBuilder zjlxStringBuilder = new StringBuilder();
					for (int i = 0; i < zjlxStrings.length; i++) {
						if (i != 0) {
							zjlxStringBuilder.append("/");
						}
						zjlxStringBuilder.append(ConstHelper.getNameByValue("ZJLX", zjlxStrings[i]));
					}
					nBook.setYWRZJZL(zjlxStringBuilder.toString());
				} else {
					nBook.setYWRZJZL(ConstHelper.getNameByValue("ZJLX", fsql.getYWRZJZL()));
				}
			}
			nBook.setYWRZJH(fsql.getYWRZJH());// 证件号
			nBook.setYGDJLX(ConstHelper.getNameByValue("YGDJZL", fsql.getYGDJZL()));// 预告登记种类
			YGDJLX = fsql.getYGDJZL();
			// 被担保主债权数额
			if ((ConstValue.YGDJLX.YSSPFDYQYGDJ.Value.toString()).equals(YGDJLX)
					|| (ConstValue.YGDJLX.ZQTBDCDYQYGDJ.Value.toString()).equals(YGDJLX)) {
				if (fsql.getBDBZZQSE() != null) {
					QDJG_BDBZZQSE = StringHelper.formatDouble(fsql.getBDBZZQSE());
				}
			}
		}
		// 登记类型
		nBook.setDJLX(ConstHelper.getNameByValue("DJLX", info.getDjlx()));
  		
		// 3.权利相关
		StringBuilder hqlCondition2 = new StringBuilder();
		hqlCondition2.append(" xmbh='").append(xmbh)
				.append("' and id IN (SELECT QLID FROM BDCS_QDZR_GZ WHERE DJDYID = '").append(djdyid).append("') ");
		List<BDCS_QL_GZ> qlList = baseCommonDao.getDataList(BDCS_QL_GZ.class, hqlCondition2.toString());
		if (qlList.size() > 0) {
			BDCS_QL_GZ ql = qlList.get(0);
			nBook.setDJYY(ql.getDJYY());// 登记原因
			nBook.setFJ(ql.getFJ());// 附记
			nBook.setDJSJ(StringHelper.FormatYmdhmsByDate(ql.getDJSJ()));// 登记时间
			nBook.setDBR(ql.getDBR());// 登簿人
			nBook.setBDCDJZMH(ql.getBDCQZH()); // 不动产登记证明号
			nBook.setTDSYQR(ql.getTDSHYQR());
			// 取得价格
			if (ConstValue.YGDJLX.YSSPFMMYGDJ.Value.toString().equals(YGDJLX)
					|| ConstValue.YGDJLX.QTBDCMMYGDJ.Value.toString().equals(YGDJLX)) {
				if (ql.getQDJG() != null) {
					QDJG_BDBZZQSE = StringHelper.formatDouble(ql.getQDJG());
				}
			}
			//修改预测户的登记类型
			if ("4".equals(ql.getQLLX()) &&  "032".equals(djdys.get(0).getBDCDYLX())) {
  				String selector = info.getBaseworkflowcode();
  				//基准流程  4/032
  	  			//String[] codes_cs = {"CS013","CS013"}; 
  	  			String[] codes_zy = {"ZY032"};  
  	  			String[] codes_bg = {"BG043","BG049","BG201"};  
  	  			String[] codes_gz = {"GZ011","GZ017"};  
  	  			//String[] codes_cf = {"CF003","CF010"};  
  	  			//String[] codes_jf = {"JF003"};  
  	  			String[] codes_zx = {"ZX005"};  
  	  			String[] codes_bhz = {"BZ013"};  
  	  			String[] codes_qt = {"QT003","XZ003"}; 
  	  			
  	  			if(Arrays.asList(codes_zy).contains(selector)) { 			
  	  				nBook.setDJLX("转移登记");		
  				}else if (Arrays.asList(codes_bg).contains(selector)) {
  					nBook.setDJLX("变更登记");
  				}else if (Arrays.asList(codes_gz).contains(selector)) {
  					nBook.setDJLX("更正登记");
  				}else if (Arrays.asList(codes_zx).contains(selector)) {
  					nBook.setDJLX("注销登记");
  				}else if (Arrays.asList(codes_bhz).contains(selector) || Arrays.asList(codes_qt).contains(selector)) {
  					nBook.setDJLX("其他登记");
  				}		
			} 
		}
		nBook.setQDJGBDBZZQSE(QDJG_BDBZZQSE);
		// 4.单元信息
		nBook = packageNoticeUnitInfo(xmbh, djdyid, nBook, info);
		return nBook;
	}

	/**
	 * 根据qlid获取预告登记登记簿信息
	 * 
	 * @作者 俞学斌
	 * @创建时间 2015年9月1日上午01:31:29
	 * @param xmbh
	 * @param qlid
	 * @return
	 */
	public NoticeBook getNoticeBookByQLID(String xmbh, String qlid) {
		ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
		Rights ql = RightsTools.loadRights(DJDYLY.GZ, qlid);
		SubRights fsql = RightsTools.loadSubRightsByRightsID(DJDYLY.GZ, qlid);
		List<RightsHolder> qlrs = RightsHolderTools.loadRightsHolders(DJDYLY.GZ, qlid);
		NoticeBook nBook = new NoticeBook();
		nBook.setXMBH(xmbh);// 项目内码
		nBook.setACCEPT_CODE(info.getProject_id());// 业务号
		// 1.权利人相关
		StringBuilder qlrMcSring = new StringBuilder();
		StringBuilder qlrZjlxSring = new StringBuilder();
		StringBuilder qlrZjhSring = new StringBuilder();
		String qxdm_ = ConfigHelper.getNameByValue("XZQHDM");
		for (int i = 0; i < qlrs.size(); i++) {
			RightsHolder qlr = qlrs.get(i);
			if (i == 0) {
				qlrMcSring.append(qlr.getQLRMC());
				qlrZjlxSring.append(ConstHelper.getNameByValue("ZJLX", qlr.getZJZL()));
				qlrZjhSring.append(qlr.getZJH());
			} else {
				qlrMcSring.append("/").append(qlr.getQLRMC());
				qlrZjlxSring.append("/").append(ConstHelper.getNameByValue("ZJLX", qlr.getZJZL()));
				qlrZjhSring.append("/").append(qlr.getZJH());
			}
		}
		nBook.setQLR(qlrMcSring.toString());// 权利人
		nBook.setQLRZJZL(qlrZjlxSring.toString());// 证件类型
		nBook.setQLRZJH(qlrZjhSring.toString());// 证件号
		if(qxdm_.indexOf("45")==0){//（桂林）权利页面土地使用权人不填的话，登簿页面不显示
			nBook.setTDSYQR(ql.getTDSHYQR());// 土地使用权人
		}else{
			nBook.setTDSYQR(qlrMcSring.toString());// 土地使用权人
		}

		// 2.附属权利相关
		// 取得价格，被担保主债权数额
		String YGDJLX = "";
		String QDJG_BDBZZQSE = "";
		// 义务人相关
		if (fsql != null) {
			nBook.setYWR(fsql.getYWR()); // 义务人
			// 证件类型
			if (!StringUtils.isEmpty(fsql.getYWRZJZL())) {
				String[] zjlxStrings = fsql.getYWRZJZL().split("/");
				if (zjlxStrings.length > 1) {
					StringBuilder zjlxStringBuilder = new StringBuilder();
					for (int i = 0; i < zjlxStrings.length; i++) {
						if (i != 0) {
							zjlxStringBuilder.append("/").append(ConstHelper
									.getNameByValue("ZJLX", zjlxStrings[i]));
						} else {
							zjlxStringBuilder.append(ConstHelper.getNameByValue("ZJLX", zjlxStrings[i]));
						}
					}
					nBook.setYWRZJZL((zjlxStringBuilder.toString()));
				} else {
					nBook.setYWRZJZL(ConstHelper.getNameByValue("ZJLX", fsql.getYWRZJZL()));
				}
			}
			
			/*String ywr_zjhm ="";
			String [] allywr = null;
			String ywr = (fsql.getYWR() == null ? "" : fsql.getYWR());
			if(ywr!=""){
				if(ywr.contains("/")){
					allywr = ywr.split("/");
					List<String> allywr_list=java.util.Arrays.asList(allywr);
					for(int i =0; i<allywr_list.size();i++ ){
						String ywr_one = allywr_list.get(i).toString().trim();
						BDCS_SQR sqr_ywr = new BDCS_SQR();
						List<BDCS_SQR> sqrlist = baseCommonDao.getDataList(BDCS_SQR.class, " SQRXM = '"+ywr_one+"' AND XMBH= '"+xmbh+"'");
						if(sqrlist!=null && sqrlist.size()>0){
							sqr_ywr = sqrlist.get(0);
						    ywr_zjhm = (sqr_ywr.getZJH()== null ? "" : sqr_ywr.getZJH().toString());
							if(i+1==allywr_list.size()){
								ywr_zjhm += ywr_zjhm;
							}else{
								ywr_zjhm += ywr_zjhm +"/";
							}
							
						}
					}
					nBook.setYWRZJH(ywr_zjhm);// 证件号
				}else{
					nBook.setYWRZJH(fsql.getYWRZJH());// 证件号
				}
			}*/
			
			nBook.setYWRZJH(fsql.getYWRZJH());// 证件号
			
			nBook.setYGDJLX(ConstHelper.getNameByValue("YGDJZL", fsql.getYGDJZL()));// 预告登记种类
			YGDJLX = fsql.getYGDJZL();
			// 被担保主债权数额
			if (YGDJLX != null && (YGDJLX.equals(ConstValue.YGDJLX.YSSPFDYQYGDJ.Value.toString())
					|| YGDJLX.equals(ConstValue.YGDJLX.ZQTBDCDYQYGDJ.Value.toString()))) {
				if (fsql.getBDBZZQSE() != null) {
					QDJG_BDBZZQSE = StringHelper.formatDouble(fsql.getBDBZZQSE());
				}
			}
		}
		// 登记类型
		nBook.setDJLX(ConstHelper.getNameByValue("DJLX", info.getDjlx()));
		// 3.权利相关
		if (ql != null) {
			nBook.setDJYY(ql.getDJYY());// 登记原因
			nBook.setFJ(ql.getFJ());// 附记
			nBook.setDJSJ(StringHelper.FormatYmdhmsByDate(ql.getDJSJ()));// 登记时间
			nBook.setDBR(ql.getDBR());// 登簿人
			nBook.setBDCDJZMH(ql.getBDCQZH()); // 不动产登记证明号
			nBook.setTDSYQR(ql.getTDSHYQR());// 从权利表里面获取土地使用权人因为权利信息页面保存了土地使用权人但登博页面没显示出来。wuzhu
												// 2010-09-02
			// 取得价格
			if (YGDJLX != null && (YGDJLX.equals(ConstValue.YGDJLX.YSSPFMMYGDJ.Value.toString())
					|| YGDJLX.equals(ConstValue.YGDJLX.QTBDCMMYGDJ.Value.toString()))) {
				if (ql.getQDJG() != null) {
					QDJG_BDBZZQSE  = StringHelper.formatDouble(ql.getQDJG()) ;
				}
			}
			nBook.setZQDW( ConstHelper.getNameByValue("JEDW", fsql.getZQDW()));
			nBook.setZXDBR(fsql.getZXDBR());
			nBook.setZXDJSJ(StringHelper.FormatYmdhmsByDate(fsql.getZXSJ()));
			nBook.setZXFJ(fsql.getZXFJ());
			nBook.setZXDJYY(fsql.getZXDYYY());
			nBook.setQDJGBDBZZQSE(QDJG_BDBZZQSE);
			// 4.单元信息
			nBook = packageNoticeUnitInfo(xmbh, ql.getDJDYID(), nBook, info);
		}
		
		return nBook;
	}

	/**
	 * 组装预告登记的单元信息
	 * 
	 * @author diaoliwei
	 * @date 2015-8-4
	 * @return
	 */
	private NoticeBook packageNoticeUnitInfo(String xmbh, String djdyid, NoticeBook nBook, ProjectInfo info) {
		// 单元信息
		String hql2 = MessageFormat.format(
				" id IN (SELECT BDCDYID FROM BDCS_DJDY_GZ WHERE XMBH=''{0}'' AND DJDYID=''{1}'')", xmbh, djdyid);
		if (BDCDYLX.H.Value.equals(info.getBdcdylx())) {
			List<BDCS_H_XZ> hlst = baseCommonDao.getDataList(BDCS_H_XZ.class, hql2);
			if (hlst != null && hlst.size() > 0) {
				BDCS_H_XZ h = hlst.get(0);
				nBook.setBDCDYH(h.getBDCDYH());// 不动产单元号
				nBook.setZL(h.getZL());// 坐落
				nBook.setGHYT(ConstHelper.getNameByValue("FWYT", h.getGHYT()));// 规划用途
				nBook.setFWXZ(ConstHelper.getNameByValue("FWXZ", h.getFWXZ()));// 房屋性质
				String zcs = StringHelper.formatObject(h.getZCS());// 所在层/总层数
				String szc = StringHelper.FormatByDatatype(h.getSZC());
				nBook.setSZCZCS(szc + "/" + zcs);
				nBook.setJZMJ(hlst.get(0) == null ? "" : StringHelper.formatObject(h.getSCJZMJ()));// 建筑面积
			}
			else{//兼容工作层wuzhu 2016-11-14
				List<BDCS_H_GZ> hlst_gz = baseCommonDao.getDataList(BDCS_H_GZ.class, hql2);
				if (hlst_gz != null && hlst_gz.size() > 0) {
					BDCS_H_GZ h = hlst_gz.get(0);
					nBook.setBDCDYH(h.getBDCDYH());// 不动产单元号
					nBook.setZL(h.getZL());// 坐落
					nBook.setGHYT(ConstHelper.getNameByValue("FWYT", h.getGHYT()));// 规划用途
					nBook.setFWXZ(ConstHelper.getNameByValue("FWXZ", h.getFWXZ()));// 房屋性质
					String zcs = StringHelper.formatObject(h.getZCS());// 所在层/总层数
					String szc = StringHelper.FormatByDatatype(h.getSZC());
					nBook.setSZCZCS(szc + "/" + zcs);
					nBook.setJZMJ(hlst_gz.get(0) == null ? "" : StringHelper.formatObject(h.getSCJZMJ()));// 建筑面积
				}
			}
		} else if (BDCDYLX.YCH.Value.equals(info.getBdcdylx())) {
			List<BDCS_H_XZY> hlst = baseCommonDao.getDataList(BDCS_H_XZY.class, hql2);
			if (hlst != null && hlst.size() > 0) {
				BDCS_H_XZY h = hlst.get(0);
				nBook.setBDCDYH(h.getBDCDYH());// 不动产单元号
				nBook.setZL(h.getZL());// 坐落
				nBook.setGHYT(ConstHelper.getNameByValue("FWYT", h.getGHYT()));// 规划用途
				nBook.setFWXZ(ConstHelper.getNameByValue("FWXZ", h.getFWXZ()));// 房屋性质
				String zcs = StringHelper.formatObject(h.getZCS());// 所在层/总层数
				String szc = StringHelper.FormatByDatatype(h.getSZC());
				nBook.setSZCZCS(szc + "/" + zcs);
				nBook.setJZMJ(hlst.get(0) == null ? "" : StringHelper.formatObject(h.getYCJZMJ()));// 建筑面积
				// nBook.setTDSYQR(h.getTDSYQR());//
				// sunhb-2015-09-15预告登记的土地使用权人（登记簿规范）改 从权利表中获取 wuzhu 2016-09-02
			}
			else//兼容工作层wuzhu 2016-11-14
			{
				List<BDCS_H_GZY> h_gzy = baseCommonDao.getDataList(BDCS_H_GZY.class, hql2);
				if (h_gzy != null && h_gzy.size() > 0) {
					BDCS_H_GZY h = h_gzy.get(0);
					nBook.setBDCDYH(h.getBDCDYH());// 不动产单元号
					nBook.setZL(h.getZL());// 坐落
					nBook.setGHYT(ConstHelper.getNameByValue("FWYT", h.getGHYT()));// 规划用途
					nBook.setFWXZ(ConstHelper.getNameByValue("FWXZ", h.getFWXZ()));// 房屋性质
					String zcs = StringHelper.formatObject(h.getZCS());// 所在层/总层数
					String szc = StringHelper.FormatByDatatype(h.getSZC());
					nBook.setSZCZCS(szc + "/" + zcs);
					nBook.setJZMJ(h_gzy.get(0) == null ? "" : StringHelper.formatObject(h.getYCJZMJ()));// 建筑面积
					// nBook.setTDSYQR(h.getTDSYQR());//
					// sunhb-2015-09-15预告登记的土地使用权人（登记簿规范）改 从权利表中获取 wuzhu 2016-09-02
				}
			}
		} else if (BDCDYLX.SHYQZD.Value.equals(info.getBdcdylx())) {
			List<BDCS_SHYQZD_XZ> hlst = baseCommonDao.getDataList(BDCS_SHYQZD_XZ.class, hql2);
			if (hlst != null && hlst.size() > 0) {
				BDCS_SHYQZD_XZ shyqzd = hlst.get(0);
				nBook.setBDCDYH(shyqzd.getBDCDYH());// 不动产单元号
				nBook.setZL(shyqzd.getZL());// 坐落
				nBook.setJZMJ(hlst.get(0) == null ? "" : StringHelper.formatObject(shyqzd.getZDMJ()));// 建筑面积）
			}
			else//兼容工作层wuzhu 2016-11-14
			{
				List<BDCS_SHYQZD_GZ> hlst_gz = baseCommonDao.getDataList(BDCS_SHYQZD_GZ.class, hql2);
				if (hlst_gz != null && hlst_gz.size() > 0) {
					BDCS_SHYQZD_GZ shyqzd = hlst_gz.get(0);
					nBook.setBDCDYH(shyqzd.getBDCDYH());// 不动产单元号
					nBook.setZL(shyqzd.getZL());// 坐落
					nBook.setJZMJ(hlst_gz.get(0) == null ? "" : StringHelper.formatObject(shyqzd.getZDMJ()));// 建筑面积）
				}
			}
		}

		return nBook;
	}

	/**
	 * 获取异议登记权利的登记簿信息
	 * 
	 * @作者：俞学斌
	 * @创建时间 2015年6月30日 下午5:10:01
	 * @param xmbh
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	public Map<String, String> getYYDJBInfo(String xmbh, String qlid) {
		HashMap<String, String> map = new HashMap<String, String>();
		BDCS_QL_GZ ql = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
		if (ql != null) {
			BDCS_FSQL_GZ fsql = baseCommonDao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
			if (fsql != null) {
				map.put("yysx", fsql.getYYSX());
				map.put("zxywh", fsql.getZXDYYWH());
				map.put("zxyy", fsql.getZXDYYY());
				if (fsql.getZXSJ() != null) {
					map.put("zxsj", StringHelper.FormatYmdhmsByDate(fsql.getZXSJ()));
				} else {
					map.put("zxsj", "");
				}
				map.put("zxdbr", fsql.getZXDBR());
			}
			BDCS_QL_XZ xz_ql = baseCommonDao.get(BDCS_QL_XZ.class, ql.getLYQLID());
			if (xz_ql != null) {
				map.put("bdcqzh", xz_ql.getBDCQZH());
			} else {
				map.put("bdcqzh", "");
			}
			map.put("djsj", StringHelper.FormatYmdhmsByDate(ql.getDJSJ()));
			map.put("dbr", ql.getDBR());
			map.put("fj", ql.getFJ());
			map.put("bdcqzh", ql.getBDCQZH());
		} else {
			map.put("yysx", "");
			map.put("bdcqzh", "");
			map.put("djsj", "");
			map.put("dbr", "");
			map.put("fj", "");
			map.put("zxywh", "");
			map.put("zxyy", "");
			map.put("zxsj", "");
			map.put("zxdbr", "");
		}
		List<BDCS_SQR> sqrs = baseCommonDao.getDataList(BDCS_SQR.class, " XMBH='" + xmbh + "'");
		String sqr = "";
		String zjlx = "";
		String zjh = "";
		if (sqrs != null && sqrs.size() > 0) {
			for (int isqr = 0; isqr < sqrs.size(); isqr++) {
				sqr = sqr + sqrs.get(isqr).getSQRXM() + "、";
				if (!StringUtils.isEmpty(ConstHelper.getNameByValue("ZJLX", sqrs.get(isqr).getZJLX()))
						&& !zjlx.contains(ConstHelper.getNameByValue("ZJLX", sqrs.get(isqr).getZJLX()))) {
					zjlx = zjlx + ConstHelper.getNameByValue("ZJLX", sqrs.get(isqr).getZJLX()) + "、";
				}
				zjh = zjh + sqrs.get(isqr).getZJH() + "、";
			}
			if (sqr.endsWith("、")) {
				sqr = sqr.substring(0, sqr.length() - 1);
				zjlx = zjlx.substring(0, zjlx.length() - 1);
				zjh = zjh.substring(0, zjh.length() - 1);
			}
		}
		map.put("sqr", sqr);
		map.put("zjzl", zjlx);
		map.put("zjh", zjh);
		return map;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String createBDCQZHByQLLX(String xmbh, String qllx, String type) {
		BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);
		//已经登簿的业务，无法再次重新获取权证号 liangqin
		if("1".equals(xmxx.getSFDB())){
			NewLogTools.saveLog("重新获取权证号失败，"+xmxx.getYWLSH()+"该项目已经登簿", xmbh, "6", "获取权证号日志及权证号赋值信息");
			return "";
		}
		JSONObject jsonObject = NewLogTools.getJSONByXMBH(xmbh);
		jsonObject.put("OperateType", "获取权证号日志及权证号赋值信息");
		jsonObject.put("QLLX",qllx);//重新赋值
		if (xmxx.getSFHBZS() != null && xmxx.getSFHBZS().equals(SF.YES.Value)) {
			jsonObject.put("IsMulti_Units", "true");
			return getbdcqzhbyqllx(xmbh, type, qllx,jsonObject);
		}
		jsonObject.put("IsMulti_Units", "false");
		String qhdm = ConfigHelper.getNameByValue("XZQHDM");
		List<BDCS_ZS_GZ> zss = baseCommonDao.getDataList(BDCS_ZS_GZ.class,
				" XMBH='" + xmbh
						+ "' AND id IN (SELECT ZSID FROM BDCS_QDZR_GZ WHERE QLID IN (SELECT id FROM BDCS_QL_GZ WHERE QLLX='"
						+ qllx + "'))");
		List<String> listqzh = new ArrayList<String>();
		if (zss != null && zss.size() > 0) {
			String createqzhtype=ConfigHelper.getNameByValue("CREATEQZHTYPE");
			if("2".equals(createqzhtype)){
				qhdm="";
				StringBuilder builder=new StringBuilder();
				builder.append("SELECT TUSER.AREACODE FROM SMWB_FRAMEWORK.T_USER TUSER ");
				builder.append("LEFT JOIN BDC_WORKFLOW.WFI_PROINST PROINST  ON TUSER.ID=PROINST.STAFF_ID ");
				builder.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON PROINST.FILE_NUMBER=XMXX.PROJECT_ID ");
				builder.append("WHERE XMBH='");
				builder.append(xmbh);
				builder.append("'");
				List<Map> list_user=baseCommonDao.getDataListByFullSql(builder.toString());
				if(list_user!=null&&list_user.size()>0){
					qhdm=StringHelper.formatObject(list_user.get(0).get("AREACODE"));
					
				}
			}if("3".equals(createqzhtype)){
				qhdm=Global.getCurrentUserInfo().getAreaCode();
			}
			
			ZHInfo zhinfo = this.GetBDCZM(qhdm, type, zss.size(), xmbh,jsonObject);
			if (zhinfo != null && !StringHelper.isEmpty(zhinfo.getMinzh()) && zhinfo.getMinzh() > 0
					&& !StringHelper.isEmpty(zhinfo.getsYear()) && !StringHelper.isEmpty(zhinfo.getQhjc())
					&& !StringHelper.isEmpty(zhinfo.getQhmc())) {
				for (int izs = 0; izs < zss.size(); izs++) {
					StringBuilder builderqzh = new StringBuilder();
					builderqzh.append(zhinfo.getQhjc());
					builderqzh.append("(");
					builderqzh.append(zhinfo.getsYear());
					builderqzh.append(")");
					builderqzh.append(zhinfo.getQhmc());
					builderqzh.append("不动产权第");
					if("2".equals(createqzhtype)||"3".equals(createqzhtype)){
						String qhstr="00";
						if(qhdm.length()>=6){
							qhstr=qhdm.substring(4, 6);
						}
						builderqzh.append(qhstr);
						builderqzh.append(StringHelper.PadLeft(StringHelper.formatDouble(zhinfo.getMinzh() + izs), 5, '0'));
					}else{
						builderqzh.append(StringHelper.PadLeft(StringHelper.formatDouble(zhinfo.getMinzh() + izs), 7, '0'));
					}
					builderqzh.append("号");
					String bdcqzh = builderqzh.toString();
					listqzh.add(bdcqzh);
				}
			}
		}

		List<BDCS_QL_GZ> qls = new ArrayList<BDCS_QL_GZ>();
		qls = baseCommonDao.getDataList(BDCS_QL_GZ.class, " XMBH='" + xmbh + "' AND QLLX ='" + qllx + "'");
		String newcqzh = "";
		if (qls != null && qls.size() > 0) {
			JSONObject jsonqls=new JSONObject();
			for (int ii = 0; ii < qls.size(); ii++) {
				JSONObject jsonql=new JSONObject();
				String qlid = qls.get(ii).getId();
				jsonql.put("QLID", qlid);
				String cqzh = "";
				String sql = MessageFormat.format(
						" id IN (SELECT ZSID FROM BDCS_QDZR_GZ WHERE XMBH=''{0}'' AND QLID=''{1}'')", xmbh, qlid);
				List<BDCS_ZS_GZ> list = baseCommonDao.getDataList(BDCS_ZS_GZ.class, sql);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						 JSONObject jsonzs=new JSONObject();
						BDCS_ZS_GZ zs = list.get(i);
						jsonzs.put("ZSID", zs.getId());
						if (!StringUtils.isEmpty(zs.getBDCQZH())
								&& !qls.get(ii).getDJLX().equals(ConstValue.DJLX.GZDJ.Value)) {
							cqzh += zs.getBDCQZH() + ",";
							jsonzs.put("证书描述", "证书的不动产权证号有值，跳过权利及权利人赋值");
							jsonql.put("QL_ZSAndQlrInfo:序号("+(ii+1)+")", jsonzs);
							continue;
						}
						String bdcqzh = listqzh.get(0);
						listqzh.remove(0);
						newcqzh += bdcqzh;
						zs.setBDCQZH(bdcqzh);
						cqzh += bdcqzh + ",";
						String hql = MessageFormat.format(
								" id IN(SELECT QLRID FROM BDCS_QDZR_GZ WHERE XMBH=''{0}'' AND QLID=''{1}'' AND ZSID=''{2}'')",
								xmbh, qlid, zs.getId());
						List<BDCS_QLR_GZ> qlrList = baseCommonDao.getDataList(BDCS_QLR_GZ.class, hql);
						if (qlrList != null && qlrList.size() > 0) {
							for (int j = 0; j < qlrList.size(); j++) {
								JSONObject jsonqlr=new JSONObject();
								BDCS_QLR_GZ qlr = qlrList.get(j);
								qlr.setBDCQZH(bdcqzh);
								qlr.setBDCQZHXH(getBDCQZHXH(bdcqzh));
								jsonqlr.put("QLRID", qlr.getId());
								jsonqlr.put("QLR-BDCQZH", bdcqzh);
								jsonzs.put("ZS_QlrInfo:序号（"+(j+1)+")", jsonqlr);
								baseCommonDao.update(qlr);
							}
						}
						jsonql.put("QL_ZsInfo:序号（"+(i+1)+")", jsonzs);
						baseCommonDao.update(zs);
					}
					qls.get(ii).setBDCQZH(cqzh.substring(0, cqzh.length() - 1));
					jsonql.put("QL-BDCQZH", cqzh.substring(0, cqzh.length() - 1));
					baseCommonDao.update(qls.get(ii));
				}
				jsonqls.put("QL_ZsAndQlrInfo:序号（"+(ii+1)+")", jsonql);
			}
			jsonObject.put("权证号赋值信息日志", jsonqls);
		}
		NewLogTools.saveLog(jsonObject.toString(), xmbh, "6", "获取权证号日志及权证号赋值信息");
		baseCommonDao.flush();
		return newcqzh;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String createBDCZMHByQLLX(String xmbh, String qllx, String type) {
		BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);
		//已经登簿的业务，无法再次重新获取权证号 liangqin
		if("1".equals(xmxx.getSFDB())){
			NewLogTools.saveLog("重新获取权证号失败，"+xmxx.getYWLSH()+"该项目已经登簿", xmbh, "6", "获取权证号日志及权证号赋值信息");
			return "";
		}
		JSONObject jsonObject = NewLogTools.getJSONByXMBH(xmbh);
		jsonObject.put("OperateType", "获取权证号日志及权证号赋值信息");
		jsonObject.put("QLLX",qllx);//重新赋值
		if (xmxx.getSFHBZS() != null && xmxx.getSFHBZS().equals(SF.YES.Value)) {
			jsonObject.put("IsMulti_Units", "true");
			return getbdcqzhbyqllx(xmbh, type, qllx,jsonObject);
		}
		jsonObject.put("IsMulti_Units", "false");
		String qhdm = ConfigHelper.getNameByValue("XZQHDM");
		List<BDCS_ZS_GZ> zss = baseCommonDao.getDataList(BDCS_ZS_GZ.class,
				" XMBH='" + xmbh
						+ "' AND id IN (SELECT ZSID FROM BDCS_QDZR_GZ WHERE QLID IN (SELECT id FROM BDCS_QL_GZ WHERE QLLX='"
						+ qllx + "'))");
		List<String> listqzh = new ArrayList<String>();
		if (zss != null && zss.size() > 0) {
			String createqzhtype=ConfigHelper.getNameByValue("CREATEQZHTYPE");
			if("2".equals(createqzhtype)){
				qhdm="";
				StringBuilder builder=new StringBuilder();
				builder.append("SELECT TUSER.AREACODE FROM SMWB_FRAMEWORK.T_USER TUSER ");
				builder.append("LEFT JOIN BDC_WORKFLOW.WFI_PROINST PROINST  ON TUSER.ID=PROINST.STAFF_ID ");
				builder.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON PROINST.FILE_NUMBER=XMXX.PROJECT_ID ");
				builder.append("WHERE XMBH='");
				builder.append(xmbh);
				builder.append("'");
				List<Map> list_user=baseCommonDao.getDataListByFullSql(builder.toString());
				if(list_user!=null&&list_user.size()>0){
					qhdm=StringHelper.formatObject(list_user.get(0).get("AREACODE"));
					
				}
			}if("3".equals(createqzhtype)){
				qhdm=Global.getCurrentUserInfo().getAreaCode();
			}
			ZHInfo zhinfo = this.GetBDCZM(qhdm, type, zss.size(), xmbh,jsonObject);
			if (zhinfo != null && !StringHelper.isEmpty(zhinfo.getMinzh()) && zhinfo.getMinzh() > 0
					&& !StringHelper.isEmpty(zhinfo.getsYear()) && !StringHelper.isEmpty(zhinfo.getQhjc())
					&& !StringHelper.isEmpty(zhinfo.getQhmc())) {
				for (int izs = 0; izs < zss.size(); izs++) {
					StringBuilder builderqzh = new StringBuilder();
					builderqzh.append(zhinfo.getQhjc());
					builderqzh.append("(");
					builderqzh.append(zhinfo.getsYear());
					builderqzh.append(")");
					builderqzh.append(zhinfo.getQhmc());
					builderqzh.append("不动产证明第");
					if("2".equals(createqzhtype)||"3".equals(createqzhtype)){
						String qhstr="00";
						if(qhdm.length()>=6){
							qhstr=qhdm.substring(4, 6);
						}
						builderqzh.append(qhstr);
						builderqzh.append(StringHelper.PadLeft(StringHelper.formatDouble(zhinfo.getMinzh() + izs), 5, '0'));
					}else{
						builderqzh.append(StringHelper.PadLeft(StringHelper.formatDouble(zhinfo.getMinzh() + izs), 7, '0'));
					}
					builderqzh.append("号");
					String bdcqzh = builderqzh.toString();
					listqzh.add(bdcqzh);
				}
			}
		}

		String newcqzh = "";
		List<BDCS_QL_GZ> qls = baseCommonDao.getDataList(BDCS_QL_GZ.class,
				" XMBH='" + xmbh + "' AND QLLX='" + qllx + "'");
		if (qls != null && qls.size() > 0) {
			JSONObject jsonqls=new JSONObject();
			for (int ii = 0; ii < qls.size(); ii++) {
				JSONObject jsonql=new JSONObject();
				String qlid = qls.get(ii).getId();
				jsonql.put("QLID", qlid);
				String cqzh = "";
				String sql = MessageFormat.format(
						" id IN (SELECT ZSID FROM BDCS_QDZR_GZ WHERE XMBH=''{0}'' AND QLID=''{1}'')", xmbh, qlid);
				List<BDCS_ZS_GZ> list = baseCommonDao.getDataList(BDCS_ZS_GZ.class, sql);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						BDCS_ZS_GZ zs = list.get(i);
                         JSONObject jsonzs=new JSONObject();
                         jsonzs.put("ZSID", zs.getId());
						if (!StringUtils.isEmpty(zs.getBDCQZH())) {
							cqzh += zs.getBDCQZH() + ",";
							jsonzs.put("证书描述", "证书的不动产权证号有值，跳过权利及权利人赋值");
							jsonql.put("QL_ZSAndQlrInfo:序号("+(ii+1)+")", jsonzs);
							continue;
						}
						String bdcqzh = listqzh.get(0);
						listqzh.remove(0);
						//bdcqzh = bdcqzh.replace("权", "证明");
						zs.setBDCQZH(bdcqzh);
						cqzh += bdcqzh + ",";
						newcqzh += bdcqzh + ",";
						String hql = MessageFormat.format(
								" id IN(SELECT QLRID FROM BDCS_QDZR_GZ WHERE XMBH=''{0}'' AND QLID=''{1}'' AND ZSID=''{2}'')",
								xmbh, qlid, zs.getId());
						List<BDCS_QLR_GZ> qlrList = baseCommonDao.getDataList(BDCS_QLR_GZ.class, hql);
						if (qlrList != null && qlrList.size() > 0) {
							for (int j = 0; j < qlrList.size(); j++) {
								JSONObject jsonqlr=new JSONObject();
								BDCS_QLR_GZ qlr = qlrList.get(j);
								jsonqlr.put("QLRID", qlr.getId());
								jsonqlr.put("QLR-BDCQZH", bdcqzh);
								jsonzs.put("ZS_QlrInfo:序号("+(j+1)+")", jsonqlr);
								qlr.setBDCQZH(bdcqzh);
								qlr.setBDCQZHXH(getBDCQZHXH(bdcqzh));
								baseCommonDao.update(qlr);
							}
						}
						baseCommonDao.update(zs);
						jsonql.put("QL_ZsInfo:序号("+(i+1)+")", jsonzs);
					}
					BDCS_QL_GZ ql = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
					if (ql != null) {
						ql.setBDCQZH(cqzh.substring(0, cqzh.length() - 1));
						jsonql.put("QL-BDCQZH", cqzh.substring(0, cqzh.length() - 1));
						baseCommonDao.update(ql);
					}
				}
				jsonqls.put("QL_ZsAndQlrInfo:序号("+(ii+1)+")", jsonql);
			}
			jsonObject.put("权证号赋值信息日志", jsonqls);
		}		
		NewLogTools.saveLog(jsonObject.toString(), xmbh, "6", "获取权证号日志及权证号赋值信息");
		baseCommonDao.flush();
		return newcqzh;
	}

	/**
	 * 根据qlid跟xmbh获取具体单元上的不动产单元号
	 * 
	 * @作者 海豹
	 * @创建时间 2015年11月17日下午4:06:59
	 * @param qlid
	 *            权利ID
	 * @param xmbh
	 *            项目编号
	 * @return
	 */
	public String getRealUnitBdcdyh(String qlid, String xmbh) {
		CommonDao dao = baseCommonDao;
		String bdcdyh = "";
		Rights right = RightsTools.loadRights(DJDYLY.GZ, qlid);
		if (!StringHelper.isEmpty(right)) {
			String hqlCondition = ProjectHelper.GetXMBHCondition(xmbh) + "and DJDYID ='" + right.getDJDYID() + "'";
			List<BDCS_DJDY_GZ> lstdjdy = dao.getDataList(BDCS_DJDY_GZ.class, hqlCondition);
			if (lstdjdy != null && lstdjdy.size() > 0) {
				BDCS_DJDY_GZ bdcs_djdy_gz = lstdjdy.get(0);
				BDCDYLX bdcdylx = null;
				if (!StringHelper.isEmpty(bdcs_djdy_gz.getBDCDYLX()))
					bdcdylx = BDCDYLX.initFrom(bdcs_djdy_gz.getBDCDYLX());
				DJDYLY dyly = null;
				if (!StringHelper.isEmpty(bdcs_djdy_gz.getLY())) {
					dyly = DJDYLY.initFrom(bdcs_djdy_gz.getLY());
				}
				String bdcdyid = bdcs_djdy_gz.getBDCDYID();
				RealUnit unit = UnitTools.loadUnit(bdcdylx, dyly, bdcdyid);
				if (!StringHelper.isEmpty(unit)) {
					bdcdyh = unit.getBDCDYH();
				}
			}
		}
		return bdcdyh;
	}

	public static class ZHInfo {
		private Integer minzh;

		public Integer getMinzh() {
			return minzh;
		}

		public void setMinzh(Integer minzh) {
			this.minzh = minzh;
		}

		public String getsYear() {
			return sYear;
		}

		public void setsYear(String sYear) {
			this.sYear = sYear;
		}

		private String sYear;
		private String qhjc;

		public String getQhjc() {
			return qhjc;
		}

		public void setQhjc(String qhjc) {
			this.qhjc = qhjc;
		}

		public String getQhmc() {
			return qhmc;
		}

		public void setQhmc(String qhmc) {
			this.qhmc = qhmc;
		}

		private String qhmc;
		
		private String success;
		
		private String msg;
	}

	/**
	 * 获取解除限制登记簿预览信息
	 * 
	 * @作者 yuxuebin
	 * @param xmbh
	 * @param bdcdyid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> getZXXZDJBInfo(String xmbh, String bdcdyid) {
		List<Map> list = new ArrayList<Map>();
		List<BDCS_XM_DYXZ> xmdyxzs = baseCommonDao.getDataList(BDCS_XM_DYXZ.class,
				"XMBH='" + xmbh + "' AND BDCDYID='" + bdcdyid + "'");
		if (xmdyxzs != null && xmdyxzs.size() > 0) {
			for (BDCS_XM_DYXZ xmdyxz : xmdyxzs) {
				String dyxzid = xmdyxz.getDYXZID();
				BDCS_DYXZ dyxz = baseCommonDao.get(BDCS_DYXZ.class, dyxzid);
				if (dyxz != null) {
					HashMap<String, String> m = new HashMap<String, String>();
					m.put("bdcdyh", StringHelper.formatObject(dyxz.getBDCDYH()));
					m.put("bdcqzh", StringHelper.formatObject(dyxz.getBDCQZH()));
					m.put("bxzrmc", StringHelper.formatObject(dyxz.getBXZRMC()));
					m.put("bxzrzjzl", StringHelper.formatObject(dyxz.getBXZRZJZLName()));
					m.put("bxzrzjhm", StringHelper.formatObject(dyxz.getBXZRZJHM()));
					m.put("xzwjhm", StringHelper.formatObject(dyxz.getXZWJHM()));
					m.put("xzdw", StringHelper.formatObject(dyxz.getXZDW()));
					m.put("sdtzrq", StringHelper.formatObject(dyxz.getSDTZRQName()));
					m.put("xzqsrq", StringHelper.formatObject(dyxz.getXZQSRQName()));
					m.put("xzzzrq", StringHelper.formatObject(dyxz.getXZZZRQName()));
					m.put("slryj", StringHelper.formatObject(dyxz.getSLRYJ()));
					m.put("xzlx", StringHelper.formatObject(dyxz.getXZLXName()));
					m.put("xzfw", StringHelper.formatObject(dyxz.getXZFW()));
					m.put("bz", StringHelper.formatObject(dyxz.getBZ()));
					m.put("djsj", StringHelper.formatObject(dyxz.getDJSJName()));
					m.put("dbr", StringHelper.formatObject(dyxz.getDBR()));
					m.put("zxxzwjhm", StringHelper.formatObject(xmdyxz.getZXXZWJHM()));
					m.put("zxxzdw", StringHelper.formatObject(xmdyxz.getZXXZDW()));
					m.put("zxyj", StringHelper.formatObject(xmdyxz.getZXYJ()));
					m.put("zxbz", StringHelper.formatObject(xmdyxz.getZXBZ()));
					m.put("zxdjsj", StringHelper.formatObject(dyxz.getZXDJSJName()));
					m.put("zxdbr", StringHelper.formatObject(dyxz.getZXDBR()));
					list.add(m);
				}
			}
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ResultMessage getQZHORZMHByXMBH(String xmbh) {
		ResultMessage msg = new ResultMessage();
		msg.setMsg("获取成功！");
		msg.setSuccess("true");
		List<String> list_djlx_qllx = new ArrayList<String>();
		String type_QZH = "GETBDCQZH";
		String type_ZMH = "GETBDCDJZM";
		List<String> list_qllx_main = new ArrayList<String>();
		list_qllx_main.add("1");
		list_qllx_main.add("2");
		list_qllx_main.add("3");
		list_qllx_main.add("4");
		list_qllx_main.add("5");
		list_qllx_main.add("6");
		list_qllx_main.add("7");
		list_qllx_main.add("8");
		list_qllx_main.add("8");
		list_qllx_main.add("9");
		list_qllx_main.add("11");
		list_qllx_main.add("12");
		list_qllx_main.add("13");
		list_qllx_main.add("14");
		list_qllx_main.add("15");
		list_qllx_main.add("16");
		list_qllx_main.add("17");
		list_qllx_main.add("18");
		HashMap<String, String> qllx_type = new HashMap<String, String>();
		List<Rights> list = RightsTools.loadRightsByCondition(DJDYLY.GZ,
				"BDCQZH IS NULL AND (ISCANCEL IS NULL OR ISCANCEL<>'1') AND XMBH='" + xmbh + "'");
		if (list != null && list.size() > 0) {
			for (Rights ql : list) {
				String djlx = StringHelper.formatObject(ql.getDJLX());
				String qllx = StringHelper.formatObject(ql.getQLLX());
				String djlx_qllx = djlx + "-" + qllx;
				if (!list_djlx_qllx.contains(djlx_qllx)) {
					list_djlx_qllx.add(djlx_qllx);
					if (DJLX.CFDJ.Value.endsWith(djlx) || DJLX.ZXDJ.Value.endsWith(djlx)) {
						continue;
					}
					if (list_qllx_main.contains(qllx) && !DJLX.YGDJ.Value.equals(djlx)
							&& !qllx_type.containsKey(qllx)) {
						qllx_type.put(qllx, type_QZH);
					} else if (!qllx_type.containsKey(qllx)) {
						qllx_type.put(qllx, type_ZMH);
					}
				}
			}
		}
		Set set = qllx_type.keySet();
		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			String qllx = StringHelper.formatObject(iterator.next());
			String type = StringHelper.formatObject(qllx_type.get(qllx));
			if (type_QZH.equals(type)) {
				createBDCQZHByQLLX(xmbh, qllx, type);
			} else {
				createBDCZMHByQLLX(xmbh, qllx, type);
			}
		}
		return msg;
	}

	/**
	 *  通过项目编号检查权利表是否有对应的权利人
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResultMessage checkQlrByQl(String xmbh) {
		 ResultMessage msg=new ResultMessage();
		 String bdcdyhs="";
		 int count=0;
		 StringBuilder fullsql=new StringBuilder();
		 fullsql.append("  SELECT ql.qlid,qlr.qlrid,ql.bdcqzh qlbdcqzh, qlr.bdcqzh qlrbdcqzh, ql.qllx, djdy.bdcdyh FROM BDCK.BDCS_QL_GZ ql  ");
		 fullsql.append("  LEFT JOIN BDCK.BDCS_QLR_GZ qlr ON ql.QLID=qlr.QLID   ");
		 fullsql.append("  LEFT JOIN BDCK.BDCS_DJDY_GZ djdy   ON djdy.djdyid=ql.djdyid  AND djdy.xmbh=ql.xmbh ");
		 fullsql.append(" WHERE (ql.ISCANCEL IS NULL OR ql.ISCANCEL<>'1')  ");
//		 fullsql.append(" left join bdck.bdcs_qlr_gz qlr ");
//		 fullsql.append(" on ql.qlid = qlr.qlid ");
		 fullsql.append(" AND ql.XMBH='").append(xmbh).append("'");
		 List<Map> lst=baseCommonDao.getDataListByFullSql(fullsql.toString());
		 if(lst !=null && lst.size()>0){
			 for(Map map :lst){
				 String qlrid=StringHelper.FormatByDatatype(map.get("QLRID")); 
				 if(StringHelper.isEmpty(qlrid)){
					 String bdcdyh=StringHelper.FormatByDatatype(map.get("BDCDYH"));
					 count++;
					 if(!bdcdyhs.equals("")){
						 bdcdyhs +="<br/>"+ bdcdyh;
					 }else{
						 bdcdyhs = bdcdyh;
					 }
				 }
			 }
		 }
		 if(count ==0){
			msg.setSuccess("1"); 
		 }else{
			 msg.setSuccess("2");
			 msg.setMsg("共有"+count+"单元没有对应的权利人信息：<br/>"+bdcdyhs);
		 }
		return msg;
	}

	/**
	 * 根据project_id获取 流程编号进行判断  登簿是否重新生成权证号
	 * @author luml  2018-2-2  10:37
	 *
	 */
	@Override
	public ResultMessage isNewQZH(String project_id) {
		ResultMessage msg=new ResultMessage();
		String workflowCode =ProjectHelper.getWorkflowCodeByProjectID(project_id);
		List<Map> isNewQZH =baseCommonDao.getDataListByFullSql("select newqzh  from bdc_workflow.wfd_mapping  where workflowcode='"
				+ workflowCode +"'");
		ProjectInfo info = ProjectHelper.GetPrjInfoByPrjID(project_id);
		if(info != null && info.getSfdb().equals("1")){
			msg.setSuccess("false");
			msg.setMsg("已经存在不动产证明号，不能重新生成！");
		}else{
			if (!StringHelper.isEmpty(isNewQZH)&& isNewQZH.size() > 0 ) {
				if ("0".equals(isNewQZH.get(0).get("NEWQZH"))) {
					msg.setSuccess("false");
				}else {
					msg.setSuccess("true");
				}
			}
		}
		
		return msg;
	}
	/**注销页面补充登记簿信息
	 * @param xmbh
	 * @param qlid
	 * @return
	 */
	public Map getZXDJBInfo(String xmbh, String qlid) {
		List<Map> Mapfsql =baseCommonDao.getDataListByFullSql("select fsqlid,djlx from BDCK.BDCS_QL_GZ where qlid='"+qlid+"' ");
		if(Mapfsql.size()>0){
		String	djlx=StringUtil.valueOf(Mapfsql.get(0).get("DJLX"));
		if((DJLX.ZXDJ.Value).equals(djlx)){
		Map<String,Object> m=new HashMap<String,Object>();
		List<Map>map=baseCommonDao.getDataListByFullSql("select zxdyyy,zxsj,zxfj,zxdbr from BDCK.BDCS_FSQL_GZ where fsqlid='"+Mapfsql.get(0).get("FSQLID")+"' ");
		m.put("zxdyyy", StringUtil.valueOf(map.get(0).get("ZXDYYY")));
		m.put("zxsj", StringHelper.FormatYmdhmsByDate(map.get(0).get("ZXSJ")));
		m.put("zxfj", StringUtil.valueOf(map.get(0).get("ZXFJ")));
		m.put("zxdbr", StringUtil.valueOf(map.get(0).get("ZXDBR")));
		return m;
			}
		}
		return null;
	}
	

}