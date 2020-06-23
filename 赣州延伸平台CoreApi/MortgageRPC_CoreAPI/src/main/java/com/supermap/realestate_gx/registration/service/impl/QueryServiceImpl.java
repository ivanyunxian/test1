package com.supermap.realestate_gx.registration.service.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import java.util.UUID;

import net.sf.json.JSONArray;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.supermap.realestate.registration.ViewClass.DJFZXX;
import com.supermap.realestate.registration.ViewClass.SQSPBex.XMXX;
import com.supermap.realestate.registration.model.BDCS_BDCCFXX;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_DJFZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_LS;
import com.supermap.realestate.registration.model.BDCS_H_LSY;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QDZR_GZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_LS;
import com.supermap.realestate.registration.model.BDCS_QDZR_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_LS;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_LS;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_UPLOADFILES;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZRZ_LS;
import com.supermap.realestate.registration.model.BDCS_ZRZ_LSY;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZY;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.YC_SC_H_LS;
import com.supermap.realestate.registration.model.YC_SC_H_XZ;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.tools.OperateFeature;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.CZFS;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate_gx.registration.service.QueryService;
import com.supermap.realestate_gx.registration.util.GX_Util;
import com.supermap.realestate_gx.registration.util.StringUtil;
import com.supermap.services.components.commontypes.Feature;
import com.supermap.services.components.commontypes.Geometry;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.tree.Tree;
import com.supermap.wisdombusiness.workflow.model.Wfi_MaterData;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.DateUtil;
import com.supermap.wisdombusiness.workflow.util.FileUpload;
import com.supermap.realestate.registration.util.ConstValue.SF;
@Service("queryService_gx")
public class QueryServiceImpl implements QueryService {
	@Autowired
	private CommonDao baseCommonDao;
	private static Logger logger = Logger.getLogger(GX_ServiceImpl.class);//用log4j写日志

	@Override
	public Message queryAutoUnlock(Map<String, String> queryvalues, int page,
			int rows, boolean ifunlock) {
		Message msg = new Message();
		long count = 0;
		List<Map> listResult = new ArrayList<Map>();
		StringBuilder builderWhere = new StringBuilder();
		for (Entry<String, String> ent : queryvalues.entrySet()) {
			String name = ent.getKey();
			String value = ent.getValue();
			if (!StringHelper.isEmpty(value) && !name.equals("QLR.QLRMC")) {
				builderWhere.append(" and instr(" + name + ",'" + value
						+ "')>0 ");
			}
			if (!StringHelper.isEmpty(value) && name.equals("QLR.QLRMC")) {
				builderWhere
						.append(" and QL.BDCDYH IN (SELECT QL.BDCDYH FROM BDCK.BDCS_QL_XZ QL LEFT JOIN BDCK.BDCS_QLR_XZ QLR ON QL.QLID=QLR.QLID WHERE  instr(QLR.QLRMC,'"
								+ value
								+ "')>0 AND (QL.QLLX='3' OR QL.QLLX='4')) ");
			}
		}
		if (ifunlock)
			builderWhere
					.append(" AND TRUNC(NVL(QL.QLJSSJ,add_months(QL.QLQSSJ,36)))<TRUNC(SYSDATE) ");

		String fromSql = " FROM BDCK.BDCS_QL_XZ QL INNER JOIN BDCK.BDCS_FSQL_XZ FSQL ON QL.FSQLID=FSQL.FSQLID  LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON QL.DJDYID=DJDY.DJDYID  WHERE QL.QLLX='99' AND QL.DJLX='800' "
				+ builderWhere.toString() + " ORDER BY QL.QLID DESC  ";
		String fullSql = " SELECT CASE WHEN TRUNC(NVL(QL.QLJSSJ,add_months(QL.QLQSSJ,36)))<TRUNC(SYSDATE) THEN '1' ELSE '0' END AS AUTOTIME,DJDY.BDCDYLX,DJDY.BDCDYID,QL.BDCDYH,QL.QLID, TO_CHAR(QL.QLQSSJ,'yyyy-MM-dd') AS QLQSSJ,TO_CHAR(NVL(QL.QLJSSJ,add_months(QL.QLQSSJ,36)),'yyyy-MM-dd')  AS QLJSSJ,QL.QLJSSJ AS OLDQLJSSJ,FSQL.CFSJ,FSQL.CFJG,FSQL.CFWH,FSQL.LHSX "
				+ fromSql;
		count = baseCommonDao.getCountByFullSql(fromSql);
		List<Map> listQL = baseCommonDao.getPageDataByFullSql(fullSql, page,
				rows);
		for (Map ql : listQL) {
			String bdcdyh = String.valueOf(ql.get("BDCDYH"));
			StringBuilder qlrbuilder = new StringBuilder();
			String qlrSql = " SELECT QLR.QLRMC,QL.QLLX FROM BDCK.BDCS_QL_XZ QL LEFT JOIN BDCK.BDCS_QLR_XZ QLR ON QL.QLID=QLR.QLID WHERE (QL.QLLX='3' OR QL.QLLX='4') AND BDCDYH='"
					+ bdcdyh + "'";
			String scdjqllx = "";
			List<Map> listQLR = baseCommonDao.getDataListByFullSql(qlrSql);
			int i = 0;
			// 权利人大于3的 列出二个权利人加等
			for (Map qlr : listQLR) {
				i++;
				if (i < 3) {
					qlrbuilder.append(qlr.get("QLRMC"));

					if (i == 1) {
						scdjqllx = ConstHelper.getNameByValue("QLLX",
								String.valueOf(qlr.get("QLLX")));
						if (listQLR.size() > 1)
							qlrbuilder.append(",");
					} else {
						if (listQLR.size() > 2)
							qlrbuilder.append("等");
					}
				}
			}
			ql.put("QLRMC", qlrbuilder.toString());
			ql.put("BDCDYLXNAME",
					ConstHelper.getNameByValue("BDCDYLX",
							String.valueOf(ql.get("BDCDYLX"))));// 不动产单元类型名

			RealUnit u = UnitTools.loadUnit(ConstValue.BDCDYLX.initFrom(String
					.valueOf(ql.get("BDCDYLX"))), DJDYLY.LS, String.valueOf(ql
					.get("BDCDYID")));
			if (u != null)
				ql.put("ZL", u.getZL());
			else
				ql.put("ZL", "");
			// 默认在
			if (!StringUtils.isEmpty(ql.get("QLJSSJ"))
					&& StringUtils.isEmpty(ql.get("OLDQLJSSJ")))
				ql.put("QLJSSJ", ql.get("QLJSSJ") + "(系统计算结束日期)");
			if (String.valueOf(ql.get("AUTOTIME")).equals("1"))
				ql.put("ZDJF", "<span style='color:red'>到自动解封期限</span>");
			else
				ql.put("ZDJF", "未到解封期限");
			listResult.add(ql);
		}
		msg.setTotal(count);
		msg.setRows(listResult);
		return msg;
	}

	@Override
	public Message Unlock(String qlid, String fj) {
		Message m = new Message();
		Rights ql_xz = RightsTools.loadRights(DJDYLY.XZ, qlid);
		SubRights fsql_xz = RightsTools
				.loadSubRightsByRightsID(DJDYLY.XZ, qlid);
		Rights ql_ls = RightsTools.loadRights(DJDYLY.LS, qlid);
		SubRights fsql_ls = RightsTools
				.loadSubRightsByRightsID(DJDYLY.LS, qlid);
		String dbr = Global.getCurrentUserName();
		//ql_ls.setFJ(fj);
		fsql_ls.setZXFJ(fj);
		fsql_ls.setZXDBR(dbr);
		fsql_ls.setZXSJ(new Date());
		if (fsql_ls != null && ql_ls != null) {
			baseCommonDao.update(fsql_ls);
			baseCommonDao.update(ql_ls);
			if (fsql_xz != null && ql_xz != null) {
				baseCommonDao.deleteEntity(fsql_xz);
				baseCommonDao.deleteEntity(ql_xz);
			} else {
				m.setMsg("现状库找不到该权利。");
				m.setSuccess("false");
				return m;
			}
		} else {
			m.setMsg("登博库找不到该权利。");
			m.setSuccess("false");
			return m;
		}
		baseCommonDao.flush();
		m.setMsg("解封成功。");
		m.setSuccess("true");
		return m;
	}

	@Override
	public Message MortgageList(Map<String, String> queryvalues, int page,
			int rows) {
		Message msg = new Message();
		long count = 0;
		List<Map> listResult = new ArrayList<Map>();
		StringBuilder builderWhere = new StringBuilder();
		for (Entry<String, String> ent : queryvalues.entrySet()) {
			String name = ent.getKey();
			String value = ent.getValue();
			if (!StringHelper.isEmpty(value) && !name.equals("QLR.QLRMC")) {
				builderWhere.append(" and instr(" + name + ",'" + value
						+ "')>0 ");
			}
			if (!StringHelper.isEmpty(value) && name.equals("QLR.QLRMC")) {
				builderWhere
						.append(" and QL.BDCDYH IN (SELECT QL.BDCDYH FROM BDCK.BDCS_QL_XZ QL LEFT JOIN BDCK.BDCS_QLR_XZ QLR ON QL.QLID=QLR.QLID WHERE  instr(QLR.QLRMC,'"
								+ value + "')>0 AND QL.QLLX ='23') ");
			}
		}
		// if(ifunlock)
		// builderWhere.append(" AND TRUNC(NVL(QL.QLJSSJ,add_months(QL.QLQSSJ,36)))<TRUNC(SYSDATE) ");

		String fromSql = " FROM BDCK.BDCS_QL_XZ QL  LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON QL.DJDYID=DJDY.DJDYID  WHERE QL.QLLX='23' "
				+ builderWhere.toString() + " ORDER BY QL.QLID DESC  ";
		String fullSql = " SELECT DJDY.BDCDYLX,DJDY.BDCDYID,QL.BDCQZH,QL.BDCDYH,QL.QLID, TO_CHAR(QL.QLQSSJ,'yyyy-MM-dd') AS QLQSSJ,TO_CHAR(NVL(QL.QLJSSJ,add_months(QL.QLQSSJ,36)),'yyyy-MM-dd')  AS QLJSSJ "
				+ fromSql;
		count = baseCommonDao.getCountByFullSql(fromSql);
		List<Map> listQL = baseCommonDao.getPageDataByFullSql(fullSql, page,
				rows);
		for (Map ql : listQL) {
			String bdcdyh = String.valueOf(ql.get("BDCDYH"));
			String qlid = String.valueOf(ql.get("QLID"));
			StringBuilder qlrbuilder = new StringBuilder();
			String scdjqllx = "";
			List<RightsHolder> listQLR = RightsHolderTools.loadRightsHolders(
					DJDYLY.XZ, qlid);

			int i = 0;
			// 权利人大于3的 列出二个权利人加等
			for (RightsHolder qlr : listQLR) {
				i++;
				if (i < 3) {
					qlrbuilder.append(qlr.getQLRMC());

					if (i == 1) {
						if (listQLR.size() > 1)
							qlrbuilder.append(",");
					} else {
						if (listQLR.size() > 2)
							qlrbuilder.append("等");
					}
				}
			}
			ql.put("QLRMC", qlrbuilder.toString());
			ql.put("BDCDYLXNAME",
					ConstHelper.getNameByValue("BDCDYLX",
							String.valueOf(ql.get("BDCDYLX"))));// 不动产单元类型名

			RealUnit u = UnitTools.loadUnit(ConstValue.BDCDYLX.initFrom(String
					.valueOf(ql.get("BDCDYLX"))), DJDYLY.XZ, String.valueOf(ql
					.get("BDCDYID")));
			if (u != null)
				ql.put("ZL", u.getZL());
			else
				ql.put("ZL", "");
			listResult.add(ql);
		}
		msg.setTotal(count);
		msg.setRows(listResult);
		return msg;
	}

	@Override
	public Message MortgageCancel(String qlid, String zxdjyy, String zxfj,
			String zxywbh) {
		Message msg = new Message();
		try {
			String sqlQL = MessageFormat.format(" QLID=''{0}''", qlid);
			Rights ql_xz = RightsTools.loadRights(DJDYLY.XZ, qlid);
			// 删除权利人
			baseCommonDao.deleteEntitysByHql(BDCS_QLR_XZ.class, sqlQL);
			// 删除权利
			baseCommonDao.deleteEntitysByHql(BDCS_QL_XZ.class, sqlQL);
			// 删除附属权利
			baseCommonDao.deleteEntitysByHql(BDCS_FSQL_XZ.class, sqlQL);
			// 删除证书
			String sqlZS = MessageFormat
					.format(" id IN (SELECT B.ZSID FROM BDCS_QDZR_XZ B WHERE B.QLID=''{0}'')",
							qlid);
			baseCommonDao.deleteEntitysByHql(BDCS_ZS_XZ.class, sqlZS);
			// 删除权利-权利人-证书-单元关系
			baseCommonDao.deleteEntitysByHql(BDCS_QDZR_XZ.class, sqlQL);
			// 更新附属权利
			SubRights fsql_ls = RightsTools.loadSubRightsByRightsID(DJDYLY.LS,
					qlid);
			if (fsql_ls != null) {
				fsql_ls.setZXDYYWH(zxywbh);
				fsql_ls.setZXDYYY(zxdjyy);
				fsql_ls.setZXFJ(zxfj);
				fsql_ls.setZXSJ(new Date());
				fsql_ls.setZXDBR(Global.getCurrentUserName());
				baseCommonDao.update(fsql_ls);
			}

			baseCommonDao.flush();
			if (ql_xz != null) {
				List<BDCS_DJDY_XZ> djdys = baseCommonDao.getDataList(
						BDCS_DJDY_XZ.class, " DJDYID='" + ql_xz.getDJDYID()
								+ "'");
				if (djdys != null && djdys.size() > 0) {
					for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
						BDCS_DJDY_XZ bdcs_djdy_xz = djdys.get(idjdy);
						if (bdcs_djdy_xz != null) {
							String strDYZT = GetDYZT(bdcs_djdy_xz.getDJDYID());
							// 更新单元抵押状态
							SetDYDYZT(
									bdcs_djdy_xz.getBDCDYID(),
									BDCDYLX.initFrom(bdcs_djdy_xz.getBDCDYLX()),
									strDYZT);
						}
					}
				}
			}
			baseCommonDao.flush();
			msg.setMsg("抵押注销成功");
			msg.setSuccess("true");
		} catch (Exception e) {
			System.out.print(e.getMessage());
			msg.setMsg("抵押注销失败");
			msg.setSuccess("true");

		}
		return msg;
	}

	/**
	 * 获取单元抵押状态
	 * 
	 * @Author：俞学斌
	 * @CreateTime 2015年6月22日 下午5:55:53
	 * @param djdyid
	 *            登记单元ID
	 * @return 状态字符串
	 */
	protected String GetDYZT(String djdyid) {
		String strDYZT = "1";
		StringBuilder builder = new StringBuilder();
		builder.append(" DJDYID='").append(djdyid).append("'");
		builder.append(" AND QLLX='23'");
		String strQuery = builder.toString();
		List<BDCS_QL_XZ> qls = baseCommonDao.getDataList(BDCS_QL_XZ.class,
				strQuery);
		if (qls != null && qls.size() > 0) {
			strDYZT = "0";
		}
		return strDYZT;
	}

	/**
	 * 设定不动产单元抵押状态
	 * 
	 * @Author 俞学斌
	 * @创建时间 2015年6月24日下午4:49:09
	 * @param bdcdyid
	 *            不动产单元ID
	 * @param bdcdylx
	 *            不动产单元类型
	 * @param dyzt
	 *            抵押状态
	 * @return 成功返回true，失败返回false
	 */
	protected boolean SetDYDYZT(String bdcdyid, BDCDYLX bdcdylx, String dyzt) {
		if ((bdcdylx.Value).equals(BDCDYLX.SHYQZD.Value)) {
			// 更新使用权宗地
			BDCS_SHYQZD_XZ bdcs_shyqzd_xz = baseCommonDao.get(
					BDCS_SHYQZD_XZ.class, bdcdyid);
			if (bdcs_shyqzd_xz != null) {
				bdcs_shyqzd_xz.setDYZT(dyzt);
				baseCommonDao.update(bdcs_shyqzd_xz);
			}
			BDCS_SHYQZD_LS bdcs_shyqzd_ls = baseCommonDao.get(
					BDCS_SHYQZD_LS.class, bdcdyid);
			if (bdcs_shyqzd_ls != null) {
				bdcs_shyqzd_ls.setDYZT(dyzt);
				baseCommonDao.update(bdcs_shyqzd_ls);
			}
		} else if ((bdcdylx.Value).equals(BDCDYLX.H.Value)) {
			// 更新户
			BDCS_H_XZ bdcs_h_xz = baseCommonDao.get(BDCS_H_XZ.class, bdcdyid);
			if (bdcs_h_xz != null) {
				bdcs_h_xz.setDYZT(dyzt);
				baseCommonDao.update(bdcs_h_xz);
			}
			BDCS_H_LS bdcs_h_ls = baseCommonDao.get(BDCS_H_LS.class, bdcdyid);
			if (bdcs_h_ls != null) {
				bdcs_h_ls.setDYZT(dyzt);
				baseCommonDao.update(bdcs_h_ls);
			}
		} else if ((bdcdylx.Value).equals(BDCDYLX.YCH.Value)) {
			BDCS_H_XZY bdcs_h_xzy = baseCommonDao
					.get(BDCS_H_XZY.class, bdcdyid);
			if (bdcs_h_xzy != null) {
				bdcs_h_xzy.setDYZT(dyzt);
				baseCommonDao.update(bdcs_h_xzy);
				BDCS_H_LSY lsyh = baseCommonDao.get(BDCS_H_LSY.class, bdcdyid);
				if (lsyh != null) {
					lsyh.setDYZT(dyzt);
					baseCommonDao.update(lsyh);
				} else {
					BDCS_H_LSY bdcs_h_lsy = new BDCS_H_LSY();
					bdcs_h_lsy = ObjectHelper.copyH_LSY(bdcs_h_xzy, bdcs_h_lsy);
					baseCommonDao.save(bdcs_h_lsy);
				}
			}
		} else if ((bdcdylx.Value).equals(BDCDYLX.YCZRZ.Value)) {
			BDCS_ZRZ_XZY bdcs_zrz_xzy = baseCommonDao.get(BDCS_ZRZ_XZY.class,
					bdcdyid);
			if (bdcs_zrz_xzy != null) {
				// bdcs_zrz_xzy.setDYZT(dyzt);
				BDCS_ZRZ_LSY lsyzrz = baseCommonDao.get(BDCS_ZRZ_LSY.class,
						bdcdyid);
				if (lsyzrz != null) {
					// bdcs_zrz_xzy.setDYZT(dyzt);
				} else {
					BDCS_ZRZ_LSY bdcs_zrz_lsy = new BDCS_ZRZ_LSY();
					bdcs_zrz_lsy = ObjectHelper.copyZRZ_LSY(bdcs_zrz_xzy,
							bdcs_zrz_lsy);
					baseCommonDao.save(bdcs_zrz_lsy);
				}
			}
		}
		return true;
	}

	/**
	 * @param 抵押注销列表
	 * @return
	 */
	public Message queryDiyazx(Map<String, String> queryvalues, int page,
			int rows) {
		Message msg = new Message();
		long count = 0;
		List<Map> listResult = new ArrayList<Map>();
		StringBuilder builderWhere = new StringBuilder();
		StringBuilder builderWhereSLSJ = new StringBuilder();
		for (Entry<String, String> ent : queryvalues.entrySet()) {
			String name = ent.getKey();
			String value = ent.getValue();
			if (!StringHelper.isEmpty(value)) {
				if (name.equals("XMXX.SLSJ_L")) {
					builderWhereSLSJ.append(" and SLSJ<to_date('" + value
							+ "','yyyy-mm-dd')+1");
				}
				if (name.equals("XMXX.SLSJ_G")) {
					builderWhereSLSJ.append(" and SLSJ>to_date('" + value
							+ "','yyyy-mm-dd')");
				}
				if (builderWhereSLSJ.length() > 0) {
					builderWhere
							.append(" and FSQL.ZXDYYWH IN (SELECT PROJECT_ID FROM BDCK.BDCS_XMXX WHERE 1=1 "
									+ builderWhereSLSJ.toString() + ")");
				}
				if (name.equals("QLR.QLRMC")) {
					builderWhere
							.append(" and QL.BDCDYH IN (SELECT QL.BDCDYH FROM BDCK.BDCS_QL_LS QL LEFT JOIN BDCK.BDCS_QLR_LS QLR ON QL.QLID=QLR.QLID WHERE  instr(QLR.QLRMC,'"
									+ value
									+ "')>0 AND  QL.QLLX NOT IN('19','20','21','22','23','99')  AND QL.DJLX IN('100','700') ) ");
				}
				if (name.equals("UNIT.ZL")) {
					builderWhere
							.append(" AND QL.BDCDYH IN (SELECT BDCDYH FROM BDCK.BDCS_H_LS WHERE instr(ZL,'"
									+ value + "')>0 ");
					builderWhere
							.append(" UNION ALL SELECT BDCDYH FROM BDCK.BDCS_SYQZD_LS WHERE instr(ZL,'"
									+ value + "')>0 ");
					builderWhere
							.append(" UNION ALL SELECT BDCDYH FROM BDCK.BDCS_SHYQZD_LS WHERE instr(ZL,'"
									+ value + "')>0 )");
				}
				if (name.equals("QL.BDCQZH")) {
					builderWhere.append(" and  instr(QL.BDCQZH,'" + value
							+ "')>0 ");
				}
			}
		}

		String fromSql = " FROM BDCK.BDCS_QL_LS QL LEFT JOIN BDCK.BDCS_FSQL_LS FSQL ON QL.QLID=FSQL.QLID LEFT JOIN BDCK.BDCS_DJDY_LS DJDY ON QL.DJDYID=DJDY.DJDYID  WHERE QL.QLLX='23' AND FSQL.ZXSJ IS NOT NULL   "
				+ builderWhere.toString() + " ORDER BY FSQL.ZXSJ DESC ";
		String fullSql = "SELECT FSQL.BDBZZQSE,FSQL.ZGZQSE,TO_CHAR(FSQL.ZXSJ,'yyyy-MM-dd') AS ZXSJ,FSQL.ZXDYYWH,QL.BDCQZH,QL.BDCDYH,DJDY.BDCDYLX,DJDY.BDCDYID   "
				+ fromSql;
		CommonDao dao = baseCommonDao;
		count = dao.getCountByFullSql(fromSql);
		List<Map> list = dao.getPageDataByFullSql(fullSql, page, rows);
		for (Map l : list) {
			StringBuilder qlrbuilder = new StringBuilder();
			String bdcdyh = String.valueOf(l.get("BDCDYH"));
			String bdcdyid = String.valueOf(l.get("BDCDYID"));
			String bdcdylx = String.valueOf(l.get("BDCDYLX"));
			String zxdyywh = String.valueOf(l.get("ZXDYYWH"));
			String qlrSql = " SELECT QLR.QLRMC,QL.QLLX FROM BDCK.BDCS_QL_LS QL LEFT JOIN BDCK.BDCS_QLR_LS QLR ON QL.QLID=QLR.QLID  WHERE QL.QLLX NOT IN('19','20','21','22','23','99')  AND QL.DJLX IN('100','700') AND QL.BDCDYH='"
					+ bdcdyh + "'";
			// String hSql
			// =" SELECT ZL FROM BDCK.BDCS_H_LS WHERE  BDCDYH='"+bdcdyh+"'";
			String slsjSql = " SELECT TO_CHAR(SLSJ,'yyyy-MM-dd') AS SLSJ FROM BDCK.BDCS_XMXX  WHERE  PROJECT_ID='"
					+ zxdyywh + "'";
			List<Map> listQLR = baseCommonDao.getDataListByFullSql(qlrSql);
			int i = 0;
			// 权利人大于3的 列出二个权利人加等
			for (Map qlr : listQLR) {
				i++;
				if (i < 3) {
					qlrbuilder.append(qlr.get("QLRMC"));
					if (i == 1) {
						if (listQLR.size() > 1)
							qlrbuilder.append(",");
					} else {
						if (listQLR.size() > 2)
							qlrbuilder.append("等");
					}
				}
			}
			l.put("QLRMC", qlrbuilder.toString());

			RealUnit u = UnitTools.loadUnit(
					ConstValue.BDCDYLX.initFrom(bdcdylx), ConstValue.DJDYLY.LS,
					bdcdyid);
			if (u != null)
				l.put("ZL", u.getZL());
			else
				l.put("ZL", "");
			List<Map> listSLSJ = baseCommonDao.getDataListByFullSql(slsjSql);
			if (listSLSJ != null && listSLSJ.size() > 0)
				l.put("SLSJ", listSLSJ.get(0).get("SLSJ"));
			else
				l.put("SLSJ", "");
			if (StringUtils.isEmpty(l.get("ZGZQSE"))
					|| Long.parseLong(String.valueOf(l.get("ZGZQSE"))) == 0)
				l.put("ZGZQSE", l.get("BDBZZQSE"));
			listResult.add(l);
		}
		msg.setTotal(count);
		msg.setRows(listResult);
		return msg;
	}

	/**
	 * @param 要拆分权利的列表
	 * @return
	 */
	public Message querySplit(Map<String, String> queryvalues, int page,
			int rows) {
		Message msg = new Message();
		long count = 0;
		List<Map> listResult = new ArrayList<Map>();
		StringBuilder builderWhere = new StringBuilder();
		StringBuilder builderWhereSLSJ = new StringBuilder();
		for (Entry<String, String> ent : queryvalues.entrySet()) {
			String name = ent.getKey();
			String value = ent.getValue();
			if (!StringHelper.isEmpty(value)) {
				/*
				 * if (name.equals("XMXX.SLSJ_L")) {
				 * builderWhereSLSJ.append(" and SLSJ<to_date('"
				 * +value+"','yyyy-mm-dd')+1"); } if
				 * (name.equals("XMXX.SLSJ_G")) {
				 * builderWhereSLSJ.append(" and SLSJ>to_date('"
				 * +value+"','yyyy-mm-dd')"); } if(builderWhereSLSJ.length()>0)
				 * { builderWhere.append(
				 * " and FSQL.ZXDYYWH IN (SELECT PROJECT_ID FROM BDCK.BDCS_XMXX WHERE 1=1 "
				 * +builderWhereSLSJ.toString()+")"); }
				 */
				if (name.equals("QLR.QLRMC")) {
					builderWhere
							.append(" and QL.BDCDYH IN (SELECT QL.BDCDYH FROM BDCK.BDCS_QL_XZ QL LEFT JOIN BDCK.BDCS_QLR_XZ QLR ON QL.QLID=QLR.QLID WHERE  instr(QLR.QLRMC,'"
									+ value
									+ "')>0 AND  QL.QLLX NOT IN('19','20','21','22','23','99')  AND QL.DJLX IN('100','700') ) ");
				}
				if (name.equals("UNIT.ZL")) {
					builderWhere
							.append(" AND QL.BDCDYH IN (SELECT BDCDYH FROM BDCK.BDCS_H_XZ WHERE instr(ZL,'"
									+ value + "')>0 ");
					builderWhere
							.append(" UNION ALL SELECT BDCDYH FROM BDCK.BDCS_SYQZD_XZ WHERE instr(ZL,'"
									+ value + "')>0 ");
					builderWhere
							.append(" UNION ALL SELECT BDCDYH FROM BDCK.BDCS_SHYQZD_XZ WHERE instr(ZL,'"
									+ value + "')>0 )");
				}
				if (name.equals("QL.BDCQZH")) {
					builderWhere.append(" and  instr(QL.BDCQZH,'" + value
							+ "')>0 ");
				}
			}
		}

		String fromSql = " FROM BDCK.BDCS_QL_XZ QL LEFT JOIN BDCK.BDCS_FSQL_XZ FSQL ON QL.QLID=FSQL.QLID LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON QL.DJDYID=DJDY.DJDYID  WHERE substr(QL.QLID,1,6)='ZDZTGY'   "
				+ builderWhere.toString() + " ORDER BY QL.QLID DESC ";
		String fullSql = "SELECT QL.QLID,QL.BDCQZH,QL.BDCDYH,QL.QLLX,QL.DJLX,DJDY.BDCDYLX,DJDY.BDCDYID   "
				+ fromSql;
		CommonDao dao = baseCommonDao;
		count = dao.getCountByFullSql(fromSql);
		List<Map> list = dao.getPageDataByFullSql(fullSql, page, rows);
		for (Map l : list) {
			StringBuilder qlrbuilder = new StringBuilder();
			String bdcdyh = String.valueOf(l.get("BDCDYH"));
			String bdcdyid = String.valueOf(l.get("BDCDYID"));
			String bdcdylx = String.valueOf(l.get("BDCDYLX"));
			String qlid = String.valueOf(l.get("QLID"));
			String qlrSql = " SELECT QLR.QLRMC FROM BDCK.BDCS_QL_XZ QL LEFT JOIN BDCK.BDCS_QLR_XZ QLR ON QL.QLID=QLR.QLID  WHERE QL.QLLX NOT IN('19','20','21','22','23','99')  AND QL.DJLX IN('100','700') AND QL.BDCDYH='"
					+ bdcdyh + "'";
			String zsCountSql = " FROM BDCK.BDCS_QDZR_XZ  WHERE  QLID='" + qlid
					+ "'";
			List<Map> listQLR = baseCommonDao.getDataListByFullSql(qlrSql);
			int i = 0;
			// 权利人大于3的 列出二个权利人加等
			for (Map qlr : listQLR) {
				i++;
			qlrbuilder.append(qlr.get("QLRMC"));
			if (i< listQLR.size())
			  qlrbuilder.append(",");

			}
			l.put("QLRMC", qlrbuilder.toString());
			l.put("BDCDYLXNAME", ConstValue.BDCDYLX.initFrom(bdcdylx).Name);
			RealUnit u = UnitTools.loadUnit(
					ConstValue.BDCDYLX.initFrom(bdcdylx), ConstValue.DJDYLY.XZ,
					bdcdyid);
			if (u != null)
				l.put("ZL", u.getZL());
			else
				l.put("ZL", "");
			Long zsCount = baseCommonDao.getCountByFullSql(zsCountSql);
			l.put("ZSCOUNT", zsCount);
			listResult.add(l);
		}
		msg.setTotal(count);
		msg.setRows(listResult);
		return msg;
	}

	@Override
	public Message SplitQL(String qlid) {
		Message msg = new Message();
		try {
			String sqlQL = MessageFormat.format(" QLID=''{0}''", qlid);
		
			BDCS_QL_XZ ql_xz = baseCommonDao.get(BDCS_QL_XZ.class, qlid);
			BDCS_FSQL_XZ fsql_xz = baseCommonDao.get(BDCS_FSQL_XZ.class,
					ql_xz.getFSQLID());
			String sqlDJDY = MessageFormat.format(" DJDYID=''{0}''", ql_xz.getDJDYID());
			List<BDCS_DJDY_XZ> djdy_xzs =baseCommonDao.getDataList(BDCS_DJDY_XZ.class,sqlDJDY);
			BDCS_DJDY_XZ djdy_xz=djdy_xzs.get(0);
			List<BDCS_QDZR_XZ> zdzr_xz = baseCommonDao.getDataList(
					BDCS_QDZR_XZ.class, sqlQL);
			// 保存对象，用于更新到历史层
			List<Map<String, Object>> objs = new ArrayList<Map<String, Object>>();
			int index = 1;
			// 遍历该权利的权地证人表信息，以证书Id为判断依据进行拆分
			for (BDCS_QDZR_XZ qdzr : zdzr_xz) {
				Map<String, Object> o = new HashMap<String, Object>();
				BDCS_QL_XZ ql_xz_new = new BDCS_QL_XZ();
				BDCS_FSQL_XZ fsql_xz_new = new BDCS_FSQL_XZ();
				BDCS_DJDY_XZ djdy_xz_new = new BDCS_DJDY_XZ();
				String newqlid = MessageFormat.format("CF_{0}_{1}",
						ql_xz.getId(), index);
				String newfsqlid = MessageFormat.format("CF_{0}_{1}",
						fsql_xz.getId(), index);
				//String newdjdyid = MessageFormat.format("CF_{0}_{1}",
				//		qdzr.getDJDYID(), index);
				String newdjdyid=qdzr.getDJDYID();
				BDCS_ZS_XZ zs_xz = baseCommonDao.get(BDCS_ZS_XZ.class,
						qdzr.getZSID());
				BDCS_QLR_XZ qlr_xz = baseCommonDao.get(BDCS_QLR_XZ.class,
						qdzr.getQLRID());
				ObjectHelper.copyObject(djdy_xz, djdy_xz_new);
				ObjectHelper.copyObject(ql_xz, ql_xz_new);
				ObjectHelper.copyObject(fsql_xz, fsql_xz_new);
				//新登记单元重新赋值
				//djdy_xz_new.setId("CF_"+UUID.randomUUID().toString());
				//djdy_xz_new.setDJDYID(newdjdyid);
				// 新权利重新赋值
				ql_xz_new.setId(newqlid);
				ql_xz_new.setFSQLID(newfsqlid);
				ql_xz_new.setBDCQZH(zs_xz.getBDCQZH());
				ql_xz_new.setDJDYID(newdjdyid);
				// 新附属权利重新赋值
				fsql_xz_new.setId(newfsqlid);
				fsql_xz_new.setQLID(newqlid);
				// 更新拆分后的权利人信息
				qlr_xz.setQLID(newqlid);
				// 更新拆分后的权地证人信息
				qdzr.setQLID(newqlid);
				qdzr.setFSQLID(newfsqlid);
				qdzr.setDJDYID(newdjdyid);
				// 更新到数据库
				o.put("ql", ql_xz_new);
				o.put("fsql", fsql_xz_new);
				o.put("qlr", qlr_xz);
				o.put("qdzr", qdzr);
				//o.put("djdy", djdy_xz_new);
				baseCommonDao.save(ql_xz_new);
				baseCommonDao.save(fsql_xz_new);
				//baseCommonDao.save(djdy_xz_new);
				baseCommonDao.saveOrUpdate(qlr_xz);
				baseCommonDao.saveOrUpdate(qdzr);
				index += 1;
				objs.add(o);
			}
			// 将信息同步到历史层
			for (Map<String, Object> obj : objs) {
				BDCS_QL_LS ql_ls_new = new BDCS_QL_LS();
				BDCS_FSQL_LS fsql_ls_new = new BDCS_FSQL_LS();
				BDCS_QLR_LS qlr_ls = new BDCS_QLR_LS();
				BDCS_QDZR_LS qdzr_ls = new BDCS_QDZR_LS();
				BDCS_DJDY_LS djdy_ls_new = new BDCS_DJDY_LS();
				ObjectHelper.copyObject(obj.get("ql"), ql_ls_new);
				ObjectHelper.copyObject(obj.get("fsql"), fsql_ls_new);
				ObjectHelper.copyObject(obj.get("qlr"), qlr_ls);
				ObjectHelper.copyObject(obj.get("qdzr"), qdzr_ls);
				//ObjectHelper.copyObject(obj.get("djdy"), djdy_ls_new);
				baseCommonDao.save(ql_ls_new);
				baseCommonDao.save(fsql_ls_new);
				//baseCommonDao.save(djdy_ls_new);
				baseCommonDao.saveOrUpdate(qlr_ls);
				baseCommonDao.saveOrUpdate(qdzr_ls);
			}

			// 删除拆分前的权利现状层
			baseCommonDao.deleteEntitysByHql(BDCS_QL_XZ.class, sqlQL);
			// 删除拆分前的权利历史层
			baseCommonDao.deleteEntitysByHql(BDCS_QL_LS.class, sqlQL);
			// 删除拆分前的登记单元现状层（可不删除 作为冗余数据）
			//baseCommonDao.deleteEntitysByHql(BDCS_DJDY_XZ.class, sqlDJDY);
			// 删除拆分前的登记单元历史层
			//baseCommonDao.deleteEntitysByHql(BDCS_DJDY_LS.class, sqlDJDY);
			
			baseCommonDao.flush();
			msg.setMsg("拆分权利成功");
			msg.setSuccess("true");
		} catch (Exception e) {
			System.out.print(e.getMessage());
			msg.setMsg("拆分权利失败");
			msg.setSuccess("true");

		}
		return msg;
	}

	@Override
	public Message GetFZList(String xmbh) {
		Message msg = new Message();
		List<DJFZXX> djfzxxList = new ArrayList<DJFZXX>();
		// 证书查询参数
		Map<String, String> map = new HashMap<String, String>();
		map.put("xmbh", xmbh);
		BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);
		String hql = " length(bDCQZH)>0 and xMBH=:xmbh";
		// 获取证书表中信息
		List<BDCS_ZS_GZ> zsList = baseCommonDao.getDataList(BDCS_ZS_GZ.class,
				hql, map);
		List<String> bdcqzhlist = new ArrayList<String>();
		for (BDCS_ZS_GZ zs : zsList) {
			if (!StringHelper.isEmpty(zs.getBDCQZH())
					&& !bdcqzhlist.contains(zs.getBDCQZH())) {
				bdcqzhlist.add(zs.getBDCQZH());
			} else {
				continue;
			}
			DJFZXX djfzxxBo = new DJFZXX();
			String strBDCQZH = zs.getBDCQZH();
			djfzxxBo.setZSBH(zs.getZSBH());
			djfzxxBo.setBDCQZH(strBDCQZH);
			djfzxxBo.setXMBH(zs.getXMBH());
			djfzxxBo.setCfdagh(StringHelper.isEmpty(xmxx.getDAGH()) ? "" : xmxx
					.getDAGH().toString());
			String strXMBH = zs.getXMBH();
			String strZSID = zs.getId();
			djfzxxBo.setCsdjtzbh(StringHelper.isEmpty(xmxx.getYWLSH()) ? ""
					: xmxx.getYWLSH().toString());// 首次登记通知单编号---业务流水号
			// 查询权地证人关系表
			StringBuilder builerqdzr = new StringBuilder();
			builerqdzr.append("XMBH='").append(strXMBH).append("' AND ZSID='")
					.append(strZSID).append("'");
			List<BDCS_QDZR_GZ> gxbList = baseCommonDao.getDataList(
					BDCS_QDZR_GZ.class, builerqdzr.toString());
			if (!gxbList.isEmpty()) {
				BDCS_QDZR_GZ qdzr = gxbList.get(0);
				String strQLRID = qdzr.getQLRID();
				String strBDCDYH = qdzr.getBDCDYH();
				String strQLID = qdzr.getQLID();
				djfzxxBo.setBDCDYH(strBDCDYH);

				// 查询权利数据
				StringBuilder builerql = new StringBuilder();
				builerql.append("XMBH='").append(strXMBH).append("' AND id='")
						.append(strQLID).append("'");
				List<BDCS_QL_GZ> qlList = baseCommonDao.getDataList(
						BDCS_QL_GZ.class, builerql.toString());
				if (!qlList.isEmpty()) {
					BDCS_QL_GZ bdcs_ql_gz = qlList.get(0);
					if (bdcs_ql_gz != null) {
						djfzxxBo.setBDCDYH(bdcs_ql_gz.getBDCDYH());
						if (bdcs_ql_gz.getCZFS().equals(CZFS.GTCZ.Value)) {
							// 权利人
							StringBuilder strBuilder = new StringBuilder();
							StringBuilder builerqlr = new StringBuilder();
							builerqlr.append("XMBH='").append(strXMBH)
									.append("' AND QLID='").append(strQLID)
									.append("'");
							List<BDCS_QLR_GZ> qlrList = baseCommonDao
									.getDataList(BDCS_QLR_GZ.class,
											builerqlr.toString());
							for (BDCS_QLR_GZ qlr : qlrList) { // 权利人可能有多个
								strBuilder.append(qlr.getQLRMC() + ",");
							}
							String strQLRMC = "";
							if (strBuilder.toString().length() > 0) {
								strQLRMC = strBuilder.toString().substring(0,
										strBuilder.length() - 1);// 去除最后一个,号
							}
							djfzxxBo.setQLR(strQLRMC);
						} else {
							// 权利人
							StringBuilder strBuilder = new StringBuilder();
							StringBuilder builerqlr = new StringBuilder();
							builerqlr.append("XMBH='").append(strXMBH)
									.append("' AND id='").append(strQLRID)
									.append("'");
							List<BDCS_QLR_GZ> qlrList = baseCommonDao
									.getDataList(BDCS_QLR_GZ.class,
											builerqlr.toString());
							for (BDCS_QLR_GZ qlr : qlrList) { // 权利人可能有多个
								strBuilder.append(qlr.getQLRMC() + ",");
							}
							String strQLRMC = "";
							if (strBuilder.toString().length() > 0) {
								strQLRMC = strBuilder.toString().substring(0,
										strBuilder.length() - 1);// 去除最后一个,号
							}
							djfzxxBo.setQLR(strQLRMC);
						}
					}
				}

				// 查询发证信息
				StringBuilder builerfz = new StringBuilder();
				builerfz.append("XMBH='").append(strXMBH)
						.append("' AND HFZSH='").append(strBDCQZH).append("'");
				List<BDCS_DJFZ> fzList = baseCommonDao.getDataList(
						BDCS_DJFZ.class, builerfz.toString());
				if (fzList.size() > 0) {
					String lzr = fzList.get(0).getLZRXM();
					Date lzsj = fzList.get(0).getFZSJ();
					if (!lzr.isEmpty() && lzr != null && lzsj != null
							&& !lzsj.toString().isEmpty()) {
					}
					djfzxxBo.setLZR(lzr);
					djfzxxBo.setLZSJ(lzsj);
					djfzxxBo.setSFFZ("是");
					djfzxxBo.setCZLX("撤销");
					djfzxxBo.setZSID(fzList.get(0).getId());
				} else {
					continue;
				}

				// 获取不动产坐落
				StringBuilder builerzl = new StringBuilder();
				builerzl.append("SELECT ZL FROM BDCK.BDCS_H_XZ WHERE BDCDYH='").append(djfzxxBo.getBDCDYH())
						.append("'");
				builerzl.append("UNION ALL SELECT ZL FROM BDCK.BDCS_H_XZY WHERE BDCDYH='").append(djfzxxBo.getBDCDYH())
				.append("'");
				List<Map> houses = baseCommonDao.getDataListByFullSql(builerzl.toString());
				if (houses.size() > 0) {
					Map house = houses.get(0);
					String zl = String.valueOf(house.get("ZL"));
					if (!zl.isEmpty() && zl != null) {
						djfzxxBo.setZl(zl);
					}
				}

				djfzxxList.add(djfzxxBo);

			}

		}
		Long totalCount = baseCommonDao
				.getCountByFullSql(" from BDCK.BDCS_ZS_GZ Where length(bDCQZH)>0 and "
						+ ProjectHelper.GetXMBHCondition(xmbh));
		msg.setTotal(totalCount);
		msg.setRows(djfzxxList);
		return msg;
	}
	
	/**
	 * 登记业务统计(广西)liangc
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Message GetDJYWTJ_GX(String tjsjks, String tjsjjz) {
		StringBuilder build = new StringBuilder();
		Message m = new Message();
		try{
			String sql="SELECT YWINFO.DJLX, YWINFO.QLLX, YWINFO.ZBGS, YWINFO.DBGS,NVL(NVL(YWINFO.ZBGS, 0)+NVL(YWINFO.DBGS, 0),0) AS BJGS,YWINFO.YGDGS,YWINFO.WGDGS,NVL(NVL(YWINFO.YGDGS, 0)+NVL(YWINFO.WGDGS, 0),0) AS GDGS, NVL(FZINFO.FZZMGS, 0) AS FZZMGS," 
					 + "NVL(FZINFO.FZZSGS, 0) AS FZZSGS, NVL(SZINFO.SZZMGS, 0) AS SZZMGS, NVL(SZINFO.SZZSGS, 0) AS SZZSGS "
					 +" FROM "
					 +"(SELECT DJLX,  QLLX, "
					 //+" SUM(CASE WHEN SFBJ = '1' THEN 1 ELSE 0 END) AS BJGS,　"
					 //+" SUM(CASE WHEN SFDB = '1' AND (SFBJ IS NULL OR SFBJ <> '1') THEN 1 ELSE 0 END) AS DBGS, " 
					 +" SUM(CASE WHEN SFDB = '1' THEN 1 ELSE 0 END) AS DBGS, " 
					 +" SUM(CASE WHEN SFDB = '1'  AND (GD.AJID IS NOT NULL ) THEN 1 ELSE 0 END) AS YGDGS, " 
					 +" SUM(CASE WHEN SFDB = '1'  AND (GD.AJID IS NULL ) THEN 1 ELSE 0 END) AS WGDGS, " 
					 +" SUM(CASE  WHEN (SFDB IS NULL OR SFDB <> '1')  THEN  1  ELSE  0  END) AS ZBGS " 
					// +" SUM(CASE  WHEN (SFDB IS NULL OR SFDB <> '1') AND (SFBJ IS NULL OR SFBJ <> '1') THEN  1  ELSE  0  END) AS ZBGS " 
					 +" FROM BDCK.BDCS_XMXX XMXX " 
					 +" LEFT JOIN BDC_DAK.DAS_AJJBXX GD "
					 +" ON GD.YWDH = XMXX.YWLSH "
					 +" WHERE XMXX.SLSJ BETWEEN "
					 +" TO_DATE('"+tjsjks+" 00:00:01', 'yyyy-mm-dd hh24:mi:ss') AND "
					 +" TO_DATE('"+tjsjjz+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') "
					 +" GROUP BY DJLX, QLLX  " 
					 +" ORDER BY DJLX, TO_NUMBER(QLLX)) YWINFO "       
					 +" LEFT JOIN "
					 +" (SELECT XMXX.DJLX,XMXX.QLLX, "
					 +" SUM(CASE  WHEN DJFZ.HFZSH LIKE '%不动产证明%' THEN 1   ELSE  0  END) AS FZZMGS, "
					 +" SUM(CASE  WHEN DJFZ.HFZSH LIKE '%不动产权%' THEN 1  ELSE  0  END) AS FZZSGS, "
					 +" COUNT(*) AS FZGS FROM (SELECT DISTINCT XMBH, HFZSH, MIN(FZSJ) AS FZSJ "
					 +" FROM BDCK.BDCS_DJFZ  GROUP BY XMBH, HFZSH) DJFZ "
					 +" LEFT JOIN BDCK.BDCS_XMXX XMXX  ON XMXX.XMBH = DJFZ.XMBH "
					 +" WHERE DJFZ.FZSJ BETWEEN  "
					 +" TO_DATE('"+tjsjks+" 00:00:01', 'yyyy-mm-dd hh24:mi:ss') AND "  
					 +" TO_DATE('"+tjsjjz +" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') "
					 +" GROUP BY XMXX.DJLX, XMXX.QLLX) FZINFO  ON YWINFO.DJLX = FZINFO.DJLX   AND YWINFO.QLLX = FZINFO.QLLX "
					 +" LEFT JOIN "
					 +" (SELECT XMXX.DJLX,  XMXX.QLLX, "
					 +" SUM(CASE  WHEN ZS.BDCQZH LIKE '%不动产证明%' THEN 1   ELSE 0  END) AS SZZMGS, "
					 +" SUM(CASE  WHEN ZS.BDCQZH LIKE '%不动产权%' THEN 1  ELSE   0  END) AS SZZSGS, "
					 +" COUNT(*) AS SZGS "
					 +" FROM BDCK.BDCS_DJSZ DJSZ "
					 +" INNER JOIN BDCK.BDCS_XMXX XMXX ON XMXX.XMBH = DJSZ.XMBH "
					 +" INNER JOIN (SELECT DISTINCT XMBH, BDCQZH FROM BDCK.BDCS_ZS_GZ) ZS ON XMXX.XMBH = ZS.XMBH "
					 +" WHERE DJSZ.SZSJ BETWEEN "
					 +" TO_DATE('"+tjsjks+" 00:00:01', 'yyyy-mm-dd hh24:mi:ss') AND "
					 +" TO_DATE('"+tjsjjz+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') "
					 +" GROUP BY XMXX.DJLX, XMXX.QLLX) SZINFO "
					 +" ON YWINFO.DJLX = SZINFO.DJLX "
					 +" AND YWINFO.QLLX = SZINFO.QLLX "
					 +" ORDER BY DJLX, TO_NUMBER(QLLX)";
			List<Map> maps = baseCommonDao.getDataListByFullSql(sql);
			if(!StringHelper.isEmpty(maps) && maps.size()>0)
			{
				for(Map map :maps)
				{
					String strdjlx=StringHelper.FormatByDatatype(map.get("DJLX"));
				    map.put("DJLX", ConstHelper.getNameByValue("DJLX", strdjlx));
				    map.put("djlx", strdjlx);
					String strqllx=StringHelper.FormatByDatatype(map.get("QLLX"));
					map.put("qllx", strqllx);
					if(DJLX.YGDJ.Value.equals(strdjlx) && QLLX.QTQL.Value.equals(strqllx))
					{
						map.put("QLLX", "转移预告");
					}
					else if(DJLX.CFDJ.Value.equals(strdjlx) && QLLX.QTQL.Value.equals(strqllx))
					{
						map.put("QLLX", "查封");
					}
					else if(DJLX.CFDJ.Value.equals(strdjlx) && QLLX.CFZX.Value.equals(strqllx))
					{
						map.put("QLLX", "解封");
					}
					else
					{
					map.put("QLLX", ConstHelper.getNameByValue("QLLX", strqllx));
					}
				}
			}
			m.setRows(maps);
			m.setTotal(maps.size());
		}catch(Exception e){
			m=null;
			
		}
		
		return m;
	}
	
	/**
	 * //登记业务统计明细liangc
	 */
	@Override
	public Message djywtj(Map<String, String> queryvalues, Integer page,
			Integer rows) {
		Message msg = new Message();
		long count = 0;
		StringBuilder builderWhereH = new StringBuilder();
		StringBuilder builderFromF = new StringBuilder();
		StringBuilder builderSelectS = new StringBuilder();
		String djlx = "";
		String qllx = "";
		String djsj_q = "";
		String djsj_z = "";
		for (Entry<String, String> ent : queryvalues.entrySet()) {
			String name = ent.getKey();
			String value = ent.getValue();
			if (StringHelper.isEmpty(value)){
				builderWhereH.append("");
			}
			if (!StringHelper.isEmpty(value)){
				if (name.equals("DJLX")){
					djlx = ent.getValue();
					builderWhereH.append(" and XMXX.DJLX='" + djlx + "'");
				}
				if (name.equals("QLLX")){
					qllx = ent.getValue();
					builderWhereH.append(" and XMXX.QLLX='" + qllx + "'");
				}
				
				if (value.equals("zb")) {
					builderSelectS.append(" select  XMXX.YWLSH,XMXX.XMBH,PO.PROJECT_NAME AS XMMC,XMXX.SLRY,TO_CHAR(XMXX.SLSJ,'yyyy-mm-dd hh24:mi:ss') AS SLSJ ");
					builderFromF.append(" LEFT JOIN BDC_WORKFLOW.WFI_PROINST PO ON PO.FILE_NUMBER = XMXX.PROJECT_ID ");
					builderWhereH.append(" AND (XMXX.SFDB IS NULL OR XMXX.SFDB <> '1') AND (XMXX.SLSJ BETWEEN TO_DATE('"+queryvalues.get("DJSJ_Q")+" 00:00:01', 'yyyy-mm-dd hh24:mi:ss') AND TO_DATE('"+queryvalues.get("DJSJ_Z")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss')) ");
				}
				else if (value.equals("db")) {
					
						builderSelectS.append(" select   XMXX.YWLSH,XMXX.XMBH,PO.PROJECT_NAME AS XMMC,QL.DBR,TO_CHAR(QL.DJSJ,'yyyy-mm-dd hh24:mi:ss') AS DJSJ,QL.BDCQZH ");
						
						builderFromF.append(" LEFT JOIN(SELECT XMBH,BDCQZH,DBR,DJSJ FROM BDCK.BDCS_QL_GZ) QL ON QL.XMBH = XMXX.XMBH ");
						builderFromF.append(" LEFT JOIN BDC_WORKFLOW.WFI_PROINST PO ON PO.FILE_NUMBER = XMXX.PROJECT_ID ");
				
					builderWhereH.append(" AND XMXX.SFDB = '1' AND (XMXX.SLSJ BETWEEN TO_DATE('"+queryvalues.get("DJSJ_Q")+" 00:00:01', 'yyyy-mm-dd hh24:mi:ss') AND TO_DATE('"+queryvalues.get("DJSJ_Z")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss')) ");
				}
				else if (value.equals("ygd")) {
					builderSelectS.append(" select  XMXX.YWLSH,XMXX.XMBH,PO.PROJECT_NAME AS XMMC,GD.LRR,TO_CHAR(GD.LRSJ,'yyyy-mm-dd hh24:mi:ss') AS LRSJ ");
					builderFromF.append(" left join BDC_DAK.DAS_AJJBXX GD ON GD.YWDH = XMXX.YWLSH  ");
					builderFromF.append(" LEFT JOIN BDC_WORKFLOW.WFI_PROINST PO ON PO.FILE_NUMBER = XMXX.PROJECT_ID ");
					builderWhereH.append(" AND (XMXX.SFDB = '1' AND GD.AJID IS NOT NULL) AND (XMXX.SLSJ BETWEEN TO_DATE('"+queryvalues.get("DJSJ_Q")+" 00:00:01', 'yyyy-mm-dd hh24:mi:ss') AND TO_DATE('"+queryvalues.get("DJSJ_Z")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss')) ");
				}
				else if (value.equals("wgd")) {
					builderSelectS.append(" select  XMXX.YWLSH,XMXX.XMBH,PO.PROJECT_NAME AS XMMC,XMXX.SLRY,TO_CHAR(XMXX.SLSJ,'yyyy-mm-dd hh24:mi:ss') AS SLSJ ");
					builderFromF.append(" left join BDC_DAK.DAS_AJJBXX GD ON GD.YWDH = XMXX.YWLSH  ");
					builderFromF.append(" LEFT JOIN BDC_WORKFLOW.WFI_PROINST PO ON PO.FILE_NUMBER = XMXX.PROJECT_ID ");
					builderWhereH.append(" AND (XMXX.SFDB = '1' AND GD.AJID IS NULL) AND (XMXX.SLSJ BETWEEN TO_DATE('"+queryvalues.get("DJSJ_Q")+" 00:00:01', 'yyyy-mm-dd hh24:mi:ss') AND TO_DATE('"+queryvalues.get("DJSJ_Z")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss')) ");
				}
				else if (value.equals("szzs")) {
					builderSelectS.append(" select DISTINCT XMXX.YWLSH,XMXX.XMBH,XMXX.XMMC,ZS.BDCQZH,QL.DBR,TO_CHAR(QL.DJSJ,'yyyy-mm-dd hh24:mi:ss') AS DJSJ ");
//					builderFromF.append(" left join BDCK.BDCS_DJSZ DJSZ ON DJSZ.XMBH = XMXX.XMBH  ");
//					builderFromF.append(" LEFT JOIN BDC_WORKFLOW.WFI_PROINST PO ON PO.FILE_NUMBER = XMXX.PROJECT_ID ");
//					builderFromF.append(" INNER JOIN(SELECT  XMBH,DBR,DJSJ FROM BDCK.BDCS_QL_GZ) QL ON QL.XMBH = XMXX.XMBH  ");
//					builderFromF.append(" INNER JOIN (SELECT  XMBH, BDCQZH FROM BDCK.BDCS_ZS_GZ) ZS ON XMXX.XMBH = ZS.XMBH  ");
					
					builderFromF.append(" INNER JOIN BDCK.BDCS_QL_GZ QL ON QL.XMBH=XMXX.XMBH ");
					builderFromF.append(" INNER JOIN BDCK.BDCS_QDZR_GZ QD ON QD.QLID=QL.QLID ");
					builderFromF.append(" INNER JOIN BDCK.BDCS_ZS_GZ ZS ON QD.ZSID=ZS.ZSID ");
					builderFromF.append(" INNER JOIN BDCK.BDCS_DJSZ DJSZ ON DJSZ.YWH=XMXX.PROJECT_ID ");
					
					builderWhereH.append(" AND ZS.BDCQZH LIKE '%不动产权%' AND (DJSZ.SZSJ BETWEEN TO_DATE('"+queryvalues.get("DJSJ_Q")+" 00:00:01', 'yyyy-mm-dd hh24:mi:ss') AND TO_DATE('"+queryvalues.get("DJSJ_Z")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss')) ");
				}
				else if (value.equals("szzm")) {
					builderSelectS.append(" select DISTINCT XMXX.YWLSH,XMXX.XMBH,XMXX.XMMC,ZS.BDCQZH,QL.DBR,TO_CHAR(QL.DJSJ,'yyyy-mm-dd hh24:mi:ss') AS DJSJ ");
//					builderFromF.append(" left join BDCK.BDCS_DJSZ DJSZ ON DJSZ.XMBH = XMXX.XMBH  ");
//					builderFromF.append(" LEFT JOIN BDC_WORKFLOW.WFI_PROINST PO ON PO.FILE_NUMBER = XMXX.PROJECT_ID ");
//					builderFromF.append(" INNER JOIN(SELECT  XMBH,DBR,DJSJ FROM BDCK.BDCS_QL_GZ) QL ON QL.XMBH = XMXX.XMBH  ");
//					builderFromF.append(" INNER JOIN (SELECT  XMBH, BDCQZH FROM BDCK.BDCS_ZS_GZ) ZS ON XMXX.XMBH = ZS.XMBH  ");
				
					builderFromF.append(" INNER JOIN BDCK.BDCS_QL_GZ QL ON QL.XMBH=XMXX.XMBH ");
					builderFromF.append(" INNER JOIN BDCK.BDCS_QDZR_GZ QD ON QD.QLID=QL.QLID ");
					builderFromF.append(" INNER JOIN BDCK.BDCS_ZS_GZ ZS ON QD.ZSID=ZS.ZSID ");
					builderFromF.append(" INNER JOIN BDCK.BDCS_DJSZ DJSZ ON DJSZ.YWH=XMXX.PROJECT_ID ");
					
					builderWhereH.append(" AND ZS.BDCQZH LIKE '%不动产证明%' AND (DJSZ.SZSJ BETWEEN TO_DATE('"+queryvalues.get("DJSJ_Q")+" 00:00:01', 'yyyy-mm-dd hh24:mi:ss') AND TO_DATE('"+queryvalues.get("DJSJ_Z")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss')) ");
				}
				else if (value.equals("fzzs")) {
					builderSelectS.append(" select   XMXX.YWLSH,XMXX.XMBH,PO.PROJECT_NAME AS XMMC,DJFZ.HFZSH AS BDCQZH,TO_CHAR(DJFZ.FZSJ,'yyyy-mm-dd hh24:mi:ss') AS FZSJ,DJFZ.LZRXM,DJFZ.LZRZJHM ");
					builderFromF.append(" left join BDCK.BDCS_DJFZ DJFZ ON DJFZ.XMBH = XMXX.XMBH  ");
					builderFromF.append(" LEFT JOIN BDC_WORKFLOW.WFI_PROINST PO ON PO.FILE_NUMBER = XMXX.PROJECT_ID ");
					
					builderWhereH.append(" AND DJFZ.HFZSH LIKE '%不动产权%' AND (DJFZ.FZSJ BETWEEN TO_DATE('"+queryvalues.get("DJSJ_Q")+" 00:00:01', 'yyyy-mm-dd hh24:mi:ss') AND TO_DATE('"+queryvalues.get("DJSJ_Z")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss'))  ");
				}
				else if (value.equals("fzzm")) {
					builderSelectS.append(" select   XMXX.YWLSH,XMXX.XMBH,PO.PROJECT_NAME AS XMMC,DJFZ.HFZSH AS BDCQZH,TO_CHAR(DJFZ.FZSJ,'yyyy-mm-dd hh24:mi:ss') AS FZSJ,DJFZ.LZRXM,DJFZ.LZRZJHM ");
					builderFromF.append(" left join BDCK.BDCS_DJFZ DJFZ ON DJFZ.XMBH = XMXX.XMBH  ");
					builderFromF.append(" LEFT JOIN BDC_WORKFLOW.WFI_PROINST PO ON PO.FILE_NUMBER = XMXX.PROJECT_ID ");
					
					builderWhereH.append(" AND DJFZ.HFZSH LIKE '%不动产证明%' AND (DJFZ.FZSJ BETWEEN TO_DATE('"+queryvalues.get("DJSJ_Q")+" 00:00:01', 'yyyy-mm-dd hh24:mi:ss') AND TO_DATE('"+queryvalues.get("DJSJ_Z")+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss')) ");
				}
			}
		}
		
		String fromSql = " from BDCK.BDCS_XMXX XMXX "
				
				+ builderFromF.toString() 
				+ " where 1=1 "
				+ builderWhereH.toString() + " ORDER BY XMXX.YWLSH DESC";

		String fullSql = builderSelectS.toString() + fromSql;
		
		count = baseCommonDao.getCountByFullSql("from ("+fullSql+") t");
		List<Map> Result = new ArrayList<Map>();
		Result= baseCommonDao.getPageDataByFullSql(fullSql, page, rows);
		List<Map> result_new=new ArrayList<Map>();
		int i = 1;
		if(Result!=null&&Result.size()>0){
			for(Map m:Result){
				if(queryvalues.get("TJLX").equals("szzm")||queryvalues.get("TJLX").equals("szzs")
						||queryvalues.get("TJLX").equals("fzzm")||queryvalues.get("TJLX").equals("fzzs")){
					
					String qlrsql=" select distinct QLR.QLRMC,QLR.DH,QLR.DLRXM,QLR.DLRLXDH "
							+ " FROM BDCK.BDCS_QLR_GZ QLR "
							+ " left join BDCK.BDCS_XMXX XMXX on XMXX.XMBH = QLR.XMBH  "
							+ " WHERE XMXX.XMBH='"+m.get("XMBH")+"' and QLR.BDCQZH='"+m.get("BDCQZH")+"'";
					
					List<Map> qlrlst=baseCommonDao.getDataListByFullSql(qlrsql);
					if(!StringHelper.isEmpty(qlrlst)&&qlrlst.size()>0){
						StringBuilder qlrmc=new StringBuilder();
						StringBuilder qlr_dh=new StringBuilder();
						StringBuilder dlrxm=new StringBuilder();
						StringBuilder dlrlxdh=new StringBuilder();
						for(Map q:qlrlst){
							if(!StringUtils.isEmpty(q.get("QLRMC")))
							qlrmc.append(q.get("QLRMC")).append(",");
							if(!StringUtils.isEmpty(q.get("DH")))
							qlr_dh.append(q.get("DH")).append(",");
							if(!StringUtils.isEmpty(q.get("DLRXM")))
							dlrxm.append(q.get("DLRXM")).append(",");
							if(!StringUtils.isEmpty(q.get("DLRLXDH")))
							dlrlxdh.append(q.get("DLRLXDH")).append(",");
						
						}
						if(!StringUtils.isEmpty(qlrmc)&&qlrmc.toString().contains(",")){
							m.put("QLRMC", trimLastChar(qlrmc));
						}else{
							m.put("QLRMC", StringHelper.formatObject(qlrmc));
						}
						if(!StringUtils.isEmpty(qlr_dh)&&qlr_dh.toString().contains(",")){
							m.put("QLRDH", trimLastChar(qlr_dh));
						}else{
							m.put("QLRDH", StringHelper.formatObject(qlr_dh));
						}
						if(!StringUtils.isEmpty(dlrxm)&&dlrxm.toString().contains(",")){
							m.put("DLRXM", trimLastChar(dlrxm));
						}else{
							m.put("DLRXM", StringHelper.formatObject(dlrxm));
						}
						if(!StringUtils.isEmpty(dlrlxdh)&&dlrlxdh.toString().contains(",")){
							m.put("DLRLXDH", trimLastChar(dlrlxdh));
						}else{
							m.put("DLRLXDH", StringHelper.formatObject(dlrlxdh));
						}
						/*m.put("QLRDH", trimLastChar(qlr_dh));
						m.put("DLRXM", trimLastChar(dlrxm));
						m.put("DLRLXDH", trimLastChar(dlrlxdh));*/
					}	
				}
			i++;
			result_new.add(m);
			}
		}
		msg.setTotal(count);
		msg.setRows(result_new);
		return msg;
	}
	private String trimLastChar(StringBuilder sb)
	{ 
	    String resultstring="";
	    if(sb.toString().length()>0)
	        resultstring = sb.toString().substring(0,sb.toString().length() - 1);
	    return resultstring;
	}
		/**
	 * 查询是否多单元一本证
	 * liangq
	 * @param xmbh
	 * @return
	 */
	@Override
	public Message getSFHBZS(String xmbh) {
		BDCS_XMXX xmxx = baseCommonDao.get(BDCS_XMXX.class, xmbh);
		Message msg = new Message();
		if (xmxx.getSFHBZS() != null && xmxx.getSFHBZS().equals(SF.YES.Value)){
			msg.setMsg("多单元发一本证");
			msg.setSuccess("1");
			return msg;
		}else{
			msg.setMsg("每个单元单独发证");
			msg.setSuccess("0");
			return msg;
		}
	}
	
	/**
	 * 柳州时空信息云平台-不动产对接接口——宗地
	 * liangq
	 * @param xmbh
	 */
	public void AddLand(String xmbh){
		try{
		//	System.out.println("新增宗地");
			//内容就是打印一句话
			//String xmbh = "";	//获取传递的xmbh
	    	Map<String, Object> map = new HashMap<String, Object>();	//最外层map
			Map<String, Object> _map = new HashMap<String, Object>();	//attributeMap的map
			List<Map> map_points = new ArrayList<Map>(); //points的list
	//		String path = this.getClass().getResource("").getPath();
	//		String [] paths = path.split("WEB-INF");
	//		String configfilepath = String.format(paths[0]+"/resources/FC2DJXT/config/柳州时空信息云平台推送记录_宗地.txt");
	//		String json = StringHelper.readFile(configfilepath);
	//		net.sf.json.JSONObject timeobj = net.sf.json.JSONObject.fromObject(json);
			String sql = "XMBH = '"+xmbh+"'";
			String bdcdyid = "";
			List<BDCS_DJDY_GZ> djdy_gz = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, sql);
			if(djdy_gz.size()>0){
				bdcdyid =	djdy_gz.get(0).getBDCDYID().toString();
			}
			List<BDCS_SHYQZD_XZ> shyqzd_datas = baseCommonDao.getDataList(BDCS_SHYQZD_XZ.class, "BDCDYID='"+bdcdyid+"'");
			List<BDCS_SYQZD_XZ> syqzd_datas = baseCommonDao.getDataList(BDCS_SYQZD_XZ.class, sql);
			BDCS_SHYQZD_XZ zd = new BDCS_SHYQZD_XZ();
			Boolean flag = false;
			if((shyqzd_datas.size()>0)){
			  zd = shyqzd_datas.get(0);
			  flag = true;
			}
			BDCDYLX bdcdylx = flag?shyqzd_datas.get(0).getBDCDYLX():BDCDYLX.SHYQZD;
			BDCS_SYQZD_XZ syqzd = new BDCS_SYQZD_XZ();
			if((syqzd_datas.size()>0)){
			  syqzd = syqzd_datas.get(0);
			}
			_map.put("BDCDYID", zd.getId());//不动产单元ID
			_map.put("BDCDYH", zd.getBDCDYH());//不动产单元号
			_map.put("FJBDCDDYID", null);//父对象的不动产单元id集合
			_map.put("CQZT", zd.getCQZT());	//产权状态
			_map.put("JG", zd.getJG());//价格
			_map.put("QTNYDMJ", syqzd.getQTNYDMJ());//其他农用地面积
			_map.put("NYDMJ", syqzd.getNYDMJ());//农用地面积
			_map.put("BLZT", zd.getBLZT());//办理状态
			_map.put("QXDM", zd.getQXDM());
			_map.put("QXMC", zd.getQXMC());//区县名称
			_map.put("GMJJHYFLDM", zd.getGMJJHYFLDM());//国民经济行业分类代码
			_map.put("TFH", zd.getTFH());//图幅号
			_map.put("TDQSLYZMCL", zd.getTDQSLYZMCL());//土地权属来源证明材料
			_map.put("DJQDM", zd.getDJQDM());//地籍区
			_map.put("DJQMC", zd.getDJQMC());//地籍区mc 
			_map.put("DJH", zd.getDJH());//地籍h
			_map.put("DJZQDM", zd.getDJZQDM());//地籍子区
			_map.put("DJZQMC", zd.getDJZQMC());//地籍子区mc
			_map.put("CLJS", zd.getCLJS());//地籍测量记事
			_map.put("SHYJ", zd.getSHYJ());//地籍调查结果审核意见
			_map.put("ZL", zd.getZL());
			_map.put("ZDSZB", zd.getZDSZB());//宗地北至
			_map.put("ZDSZN", zd.getZDSZN());//宗地南至
			_map.put("ZDT", zd.getZDT());//宗地图
			_map.put("ZDTZM", zd.getZDTZM());//宗地特征码
			_map.put("ZDSZX", zd.getZDSZX());//宗地西至
			_map.put("ZDMJ", zd.getZDMJ());//宗地面积
			_map.put("SHR", zd.getSHR());//审核人
			_map.put("SHRQ", zd.getSHRQ());//审核日期
			_map.put("RJL", zd.getRJL());//容积率
			_map.put("JZZDMJ", zd.getJZZDMJ());//建筑占地面积
			_map.put("JZMD", zd.getJZMD());//建筑密度
			_map.put("JZXG", zd.getJZXG());//建筑限高
			_map.put("JZMJ", zd.getJZMJ());//建筑面积
			_map.put("JSYDMJ", syqzd.getJSYDMJ());//建设用地面积
			_map.put("YYZT", zd.getYYZT());//异议状态
			_map.put("SYQLX", zd.getSYQLX());//A.53 所有权类型字典表
			_map.put("PZYT", zd.getPZYT());//A.54 土地用途字典表
			_map.put("PZMJ", zd.getPZMJ());//批准面积
			_map.put("DYZT", zd.getDYZT());//抵押状态
			_map.put("YXBZ", zd.getYXBZ());//有效标志
			_map.put("WLYDMJ", syqzd.getWLYDMJ());//未利用地面积
			_map.put("QLXZ", zd.getQLXZ());//A.9权利性质字典表
			_map.put("QLLX", zd.getQLLX());//A.8权利类型字典表
			_map.put("QLSDFS", zd.getQLSDFS());//A.10权利设定方式字典表
			_map.put("JZXZXSM", zd.getJZXZXSM());//权属界线走向说明
			_map.put("DCJS", zd.getDCJS());
			_map.put("LDMJ", syqzd.getLDMJ());//林地面积
			_map.put("BLC", zd.getBLC());
			_map.put("CLR", zd.getCLR());
			_map.put("CLRQ", zd.getCLRQ());
			_map.put("ZT", zd.getZT());
			_map.put("YT", zd.getYT());
			_map.put("JZDWSM",syqzd.getJZDWSM());//界址点位说明
			_map.put("DJZT", zd.getDJZT());
			_map.put("DJ", zd.getDJ());
			_map.put("ZH", zd.getZH());
			_map.put("ZM", zd.getZM());
			_map.put("GDMJ",syqzd.getGDMJ());//耕地面积
			_map.put("CDMJ",syqzd.getCDMJ());//草地面积
			_map.put("YSDM", zd.getYSDM());
			_map.put("MODIFYTIME", zd.getMODIFYTIME());
			_map.put("CREATETIME", zd.getCREATETIME());
			_map.put("DCR", zd.getDCR());
			_map.put("DCRQ", zd.getDCRQ());
			_map.put("DCXMID", zd.getDCXMID());
			_map.put("XZZT", zd.getXZZT());
			_map.put("MJDW", zd.getMJDW());
			_map.put("XMBH", zd.getXMBH());
			_map.put("YBZDDM", zd.getYBZDDM());
			map.put("type", "REGION");
		//	String _map_json = JSONObject.toJSONString(_map);
			map.put("attributeMap", _map);
			//在shyqzd表中，需要通过iserver的API获取值
			String filterWhere = "BDCDYID='" + bdcdyid + "' ";
			String url_iserver_data=ConfigHelper.getNameByValue("URL_ISERVER_DATA");
			OperateFeature operateFeature=new OperateFeature(url_iserver_data);
			List<Map> xy_datas = new ArrayList<Map>();
			Map<String, Object> points = new HashMap<String, Object>();	//points的datamap
			if("1".equals(ConfigHelper.getNameByValue("IS_ENABLE"))){
				com.supermap.realestate.gis.common.Feature[] features = operateFeature.queryFeatures_GIS("BDCDCK", bdcdylx.GZTableName.replaceAll("BDCS_", "BDCK_"), filterWhere);
				if (features == null || features.length < 0) {
					features = operateFeature.queryFeatures_GIS("BDCK", bdcdylx.XZTableName.replaceAll("BDCS_", "BDCK_"), filterWhere);
				}
				if (features != null && features.length > 0) {
					com.supermap.realestate.gis.common.Feature feature = features[0];
					com.supermap.realestate.gis.common.Geometry geometry = feature.geometry;
					for (int i = 0; i < geometry.points.length; i++) {
						Map<String, Object> xy = new HashMap<String, Object>();
						xy.put("x", StringHelper.cut(geometry.points[i].x, 4).toString());
						xy.put("y", StringHelper.cut(geometry.points[i].y, 4).toString());
						xy_datas.add(xy);
					}
					points.put("points", xy_datas); //xy格式为[{"x":205.167052,"y":43.828439},{"x":207.167052,"y":43.828439}]
					map_points.add(points);
				}else{
					Map<String, Object> xy = new HashMap<String, Object>();
					xy.put("x", "0");
					xy.put("y", "0");
					xy_datas.add(xy);
					points.put("points", xy_datas);
					map_points.add(points);
				}
			}else if ("0".equals(ConfigHelper.getNameByValue("IS_ENABLE"))) {
				List<Feature> list_feature=operateFeature.queryFeatures_iServer("BDCDCK", bdcdylx.GZTableName.replaceAll("BDCS_", "BDCK_"), filterWhere);
				if(list_feature==null||list_feature.size()<=0){
					list_feature=operateFeature.queryFeatures_iServer("BDCK", bdcdylx.XZTableName.replaceAll("BDCS_", "BDCK_"), filterWhere);
				}
				if(list_feature!=null&&list_feature.size()>0){
					Feature feature=list_feature.get(0);
					Geometry ObjGeometry=feature.geometry;
					for(int i = 0;i<ObjGeometry.points.length;i++){	
						Map<String, Object> xy = new HashMap<String, Object>();
						xy.put("x", StringHelper.cut(ObjGeometry.points[i].x, 4).toString());
			//			System.out.println("新增宗地的X坐标"+i+"："+StringHelper.cut(ObjGeometry.points[i].x, 4).toString());
						xy.put("y", StringHelper.cut(ObjGeometry.points[i].y, 4).toString());
			//			System.out.println("新增宗地的Y坐标"+i+"："+StringHelper.cut(ObjGeometry.points[i].y, 4).toString());
						xy_datas.add(xy);
					}
					points.put("points", xy_datas); //xy格式为[{"x":205.167052,"y":43.828439},{"x":207.167052,"y":43.828439}]
					map_points.add(points);
				}else{
					Map<String, Object> xy = new HashMap<String, Object>();
					xy.put("x", "0");
					xy.put("y", "0");
					xy_datas.add(xy);
					points.put("points", xy_datas);
					map_points.add(points);
				}
			}
			JSONArray jsons = JSONArray.fromObject(map_points);
			map.put("points", jsons);
			JSONObject msg = GX_Util.getHttpURLConnection("NewZD",map);
//			String json = JSONObject.toJSONString(map);
//			System.out.print(json);
			if(msg != null){
				@SuppressWarnings("rawtypes")
				Map _datas = (Map) msg;
				String Success =  _datas.get("success").toString();
				if("true".equals(Success)){
					Map msgdata = msg.getJSONObject("data");
					String id = StringUtil.valueOf(msgdata.get("BDCDYID")) ;
					logger.info(msgdata + ";"+ id + ":" + "成功推送到时空信息云平台");
					//TODO 保存已经推送的数据
					//	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH24:mi:ss");
					//	String time = sdf.format(h.getCREATETIME());
					//	saveTimeConfig("H",time,id);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return;
		}
}
	
	
/**
 * 柳州时空信息云平台-不动产对接接口，建筑物
 * liangq
 */
	public void AddBuilding(String xmbh){
		try{
			//内容就是打印一句话
		//	System.out.println("新增建筑物");
			//String xmbh = ""; //获取传递的xmbh
			Map<String, Object> map = new HashMap<String, Object>();	//最外层
			Map<String, Object> _map = new HashMap<String, Object>();	//attribute的Map
			List<Map> map_points = new ArrayList<Map>();	//points空间信息的
		//	String path = this.getClass().getResource("").getPath();
		//	String [] paths = path.split("WEB-INF");
		//	String configfilepath = String.format(paths[0]+"/resources/FC2DJXT/config/柳州时空信息云平台推送记录_建筑物.txt");
		//	String json = StringHelper.readFile(configfilepath);
		//	net.sf.json.JSONObject timeobj = net.sf.json.JSONObject.fromObject(json);
		//	String htime=timeobj.get("HTIME").toString();
		//	String sql = "xmbh ='"+xmbh+" order by CREATETIME";
		//	if(htime!=null && !"".equals(htime)){
		//		sql = "xmbh = '"+xmbh+"' and CREATETIME> to_date('"+htime+"','yyyy-MM-dd HH24:mi:ss')";
		//	}
			String sql = "xmbh = '"+xmbh+"'";
			String bdcdyid = "";
			List<BDCS_DJDY_GZ> djdy_gz = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, sql);
			if(djdy_gz.size()>0){
				bdcdyid =	djdy_gz.get(0).getBDCDYID().toString();
			}
			List<BDCS_H_XZ> datas_h = baseCommonDao.getDataList(BDCS_H_XZ.class, "BDCDYID='"+bdcdyid+"'");
			if(!(datas_h.size()>0)){
				return;
			}
			sql = "select * from bdck.bdcs_zrz_xz zrz LEFT JOIN bdck.bdcs_h_xz h on zrz.bdcdyid = h.zdbdcdyid where h.bdcdyid='"+datas_h.get(0).getId()+"' ";
			List<Map> datas_zrz = baseCommonDao.getDataListByFullSql(sql);
			BDCS_H_XZ h = datas_h.get(0);
			Map zrz = new HashMap();
			if(datas_zrz.size()>0){
				zrz = datas_zrz.get(0);
			}
			BDCDYLX bdcdylx = BDCDYLX.ZRZ;
			_map.put("BDCDYID", h.getId());//不动产单元ID
			_map.put("BDCDYH", h.getBDCDYH());//不动产单元号
			_map.put("FJBDCDDYID", h.getZRZBDCDYID());//父对象的不动产单元id集合
			_map.put("FCFHT", h.getFCFHT());
			_map.put("FTTDMJ", h.getFTTDMJ());
			_map.put("QXDM", h.getQXDM());
			_map.put("QXMC", h.getQXMC());
			_map.put("TDSYQR", h.getTDSYQR());
			_map.put("DSCS", zrz.get("DSCS"));
			_map.put("DXCS", zrz.get("DXCS"));
			_map.put("DXSD", zrz.get("DXSD"));//地下深度
			_map.put("DJQDM", h.getDJQDM());
			_map.put("DJQMC", h.getDJQMC());
			_map.put("DJZQDM", h.getDJZQDM());
			_map.put("DJZQMC", h.getDJZQMC());
			_map.put("ZL", h.getZL());
			//_map.put("BZ", h.getBZ());
			_map.put("ZDDM", h.getZDDM());
			_map.put("ZDBDCDYID", h.getZDBDCDYID());
			_map.put("SCJZMJ", h.getSCJZMJ());
			_map.put("ZZDMJ", zrz.get("ZZDMJ"));//幢占地面积
			_map.put("ZYDMJ", zrz.get("ZYDMJ"));//幢用地面积
			_map.put("JZWMC", zrz.get("JZWMC"));//建筑物名称
			_map.put("JZWJBYT", zrz.get("JZWJBYT"));//建筑物基本用途
			_map.put("JZWGD",zrz.get("JZWGD"));//建筑物高度
			_map.put("ZTS", zrz.get("ZTS"));//总套数
			_map.put("ZCS", h.getZCS());
			_map.put("FCFHTWJ", zrz.get("FCFHTWJ"));//房产分户图文件
			_map.put("FDCJYJG", zrz.get("FDCJYJG"));//房地产交易价格
			_map.put("FWJG", h.getFWJG());
			_map.put("YXBZ", h.getYXBZ());
			_map.put("ZT", h.getZT());
			_map.put("DYTDMJ", h.getDYTDMJ());
			_map.put("DJZT", h.getDJZT());
			_map.put("JGRQ", h.getJGSJ());//竣工日期
			_map.put("ZRZH", h.getZRZH());
			_map.put("GHYT", h.getGHYT());
			_map.put("MODIFYTIME", h.getMODIFYTIME());
			_map.put("CREATETIME", h.getCREATETIME());
			_map.put("DCXMID", h.getDCXMID());
			_map.put("XMMC", h.getXMMC());
			_map.put("XMBH", h.getXMBH());
			_map.put("YCJZMJ", h.getYCJZMJ());
			map.put("attributeMap", _map);
			String filterWhere = "BDCDYID='" + bdcdyid + "' ";
			//在zrz表中，需要通过iserver的API获取值
			String url_iserver_data=ConfigHelper.getNameByValue("URL_ISERVER_DATA");
			OperateFeature operateFeature=new OperateFeature(url_iserver_data);
			List<Map> xy_datas = new ArrayList<Map>();
			Map<String, Object> points = new HashMap<String, Object>();	//points的datamap
			if("1".equals(ConfigHelper.getNameByValue("IS_ENABLE"))){
				com.supermap.realestate.gis.common.Feature[] features = operateFeature.queryFeatures_GIS("BDCDCK", bdcdylx.GZTableName.replaceAll("BDCS_", "BDCK_"), filterWhere);
				if (features == null || features.length < 0) {
					features = operateFeature.queryFeatures_GIS("BDCK", bdcdylx.XZTableName.replaceAll("BDCS_", "BDCK_"), filterWhere);
				}
				if (features != null && features.length > 0) {
					com.supermap.realestate.gis.common.Feature feature = features[0];
					com.supermap.realestate.gis.common.Geometry geometry = feature.geometry;
					for (int i = 0; i < geometry.points.length; i++) {
						Map<String, Object> xy = new HashMap<String, Object>();
						xy.put("x", StringHelper.cut(geometry.points[i].x, 4).toString());
						xy.put("y", StringHelper.cut(geometry.points[i].y, 4).toString());
						xy_datas.add(xy);
					}
					points.put("points", xy_datas); //xy格式为[{"x":205.167052,"y":43.828439},{"x":207.167052,"y":43.828439}]
					map_points.add(points);
				}
			}else if ("0".equals(ConfigHelper.getNameByValue("IS_ENABLE"))) {
				List<Feature> list_feature=operateFeature.queryFeatures_iServer("BDCDCK", bdcdylx.GZTableName.replaceAll("BDCS_", "BDCK_"), filterWhere);
				if(list_feature==null||list_feature.size()<=0){
					list_feature=operateFeature.queryFeatures_iServer("BDCK", bdcdylx.XZTableName.replaceAll("BDCS_", "BDCK_"), filterWhere);
				}
				if(list_feature!=null&&list_feature.size()>0){
					Feature feature=list_feature.get(0);
					Geometry ObjGeometry=feature.geometry;
					for(int i = 0;i<ObjGeometry.points.length;i++){	
						Map<String, Object> xy = new HashMap<String, Object>();
						xy.put("x", StringHelper.cut(ObjGeometry.points[i].x, 4).toString());
					//	System.out.println("新增建筑物的X坐标"+i+"："+StringHelper.cut(ObjGeometry.points[i].x, 4).toString());
						xy.put("y", StringHelper.cut(ObjGeometry.points[i].y, 4).toString());
					//	System.out.println("新增建筑物的Y坐标"+i+"："+StringHelper.cut(ObjGeometry.points[i].y, 4).toString());
						xy_datas.add(xy);
					}
						points.put("points", xy_datas); //xy格式为[{"x":205.167052,"y":43.828439},{"x":207.167052,"y":43.828439}]
						map_points.add(points);
				}else{
					Map<String, Object> xy = new HashMap<String, Object>();
					xy.put("x", "0");
					xy.put("y", "0");
					xy_datas.add(xy);
					points.put("points", xy_datas);
				}
			}
			JSONArray jsons = JSONArray.fromObject(map_points);
			map.put("points", jsons);
			JSONObject msg = GX_Util.getHttpURLConnection("NewJZW",map);	//msg为接口返回值
			if(msg != null){
				@SuppressWarnings("rawtypes")
				Map _datas = (Map) msg;
				String Success =  _datas.get("success").toString();
				if("true".equals(Success)){
					Map msgdata = msg.getJSONObject("data");
					String id = StringUtil.valueOf(msgdata.get("BDCDYID")) ;
					logger.info(id + ":" + "成功推送到时空信息云平台");
					//TODO 保存已经推送的数据
					//	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH24:mi:ss");
					//	String time = sdf.format(h.getCREATETIME());
					//	saveTimeConfig("H",time,id);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * 办件进度查询
	 * @author liangc
	 * @param queryvalues
	 * @param page
	 * @param rows
	 * @return
	 */
	public Message djjdQuery(Map<String, String> queryvalues, Integer page,
			Integer rows){
		Map<String, String> newpara = new HashMap<String, String>();
		Message msg = new Message();
		long count = 0;
		StringBuilder builderWhereH = new StringBuilder();
		StringBuilder builderFromF = new StringBuilder();
		StringBuilder builderSelectS = new StringBuilder();
		for (Entry<String, String> ent : queryvalues.entrySet()) {
			String name = ent.getKey();
			String value = ent.getValue();
			if (StringHelper.isEmpty(value)){
				builderWhereH.append("");
			}
			if (!StringHelper.isEmpty(value)){
				if (name.equals("YWLSH")){
					builderWhereH.append(" AND XMXX.YWLSH='" + queryvalues.get("YWLSH") + "'");
				}
				if (name.equals("DJSJ_Q")) {
					builderWhereH.append("  AND XMXX.SLSJ >=to_date('"+queryvalues.get("DJSJ_Q")+"','yyyy-mm-dd') ");
				}
				if (name.equals("DJSJ_Z")) {
					builderWhereH.append("  AND XMXX.SLSJ <=(to_date('"+queryvalues.get("DJSJ_Z")+"','yyyy-mm-dd')+1) ");
				}
				if (name.equals("SQR")) {
					try {
						newpara.put(
								"SQRXM",
								"%"
										+ new String(queryvalues.get("SQR").getBytes(
												"iso8859-1"), "utf-8") + "%");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					builderWhereH.append(" AND EXISTS (SELECT 1 FROM BDCK.BDCS_SQR SQR WHERE XMXX.XMBH=SQR.XMBH AND SQR.SQRXM LIKE'"+newpara.get("SQRXM")+"') ");
				}
				if(name.equals("SQRZJHM")){
					builderWhereH.append(" AND EXISTS (SELECT 1 FROM BDCK.BDCS_SQR SQR WHERE XMXX.XMBH=SQR.XMBH AND SQR.ZJH ='"+queryvalues.get("SQRZJHM")+"') ");
				}
				
			}
		}
		builderSelectS.append(" SELECT XMXX.YWLSH,XMXX.DJLX,XMXX.SLSJ,XMXX.SFDB,XMXX.XMBH,PO.PROJECT_NAME ");
		String fromSql = " FROM BDCK.BDCS_XMXX XMXX LEFT JOIN BDC_WORKFLOW.WFI_PROINST PO ON XMXX.PROJECT_ID = PO.FILE_NUMBER "
				+ " WHERE 1=1 "
				+ builderWhereH.toString() + " ORDER BY XMXX.YWLSH DESC";

		String fullSql = builderSelectS.toString() + fromSql;
		
		count = baseCommonDao.getCountByFullSql("FROM ("+fullSql+") t");
		List<Map> Result = new ArrayList<Map>();
		Result= baseCommonDao.getPageDataByFullSql(fullSql, page, rows);
		List<Map> result_new=new ArrayList<Map>();
		int i = 1;
		if(Result!=null&&Result.size()>0){
			for(Map m:Result){
				StringBuilder djsjsql = new StringBuilder();
				StringBuilder sqrsql = new StringBuilder();
				StringBuilder blscsql = new StringBuilder();
				djsjsql.append(" SELECT QL.DJSJ,QL.DJLX,QL.QLLX,FSQL.ZXSJ FROM BDCK.BDCS_QL_GZ QL LEFT JOIN BDCK.BDCS_FSQL_GZ FSQL ON QL.QLID=FSQL.QLID WHERE QL.XMBH='"+m.get("XMBH")+"' ORDER BY QL.DJSJ DESC ") ;
				List<Map> djsjlst=baseCommonDao.getDataListByFullSql(djsjsql.toString());
				if(!StringHelper.isEmpty(djsjlst)&&djsjlst.size()>0){
					for(int ilist=0;ilist<djsjlst.size();ilist++){
						if(djsjlst.get(ilist).get("DJLX").equals("400")||(djsjlst.get(ilist).get("DJLX").equals("800"))&&djsjlst.get(ilist).get("QLLX").equals("98")){
							m.put("DJSJ",StringHelper.FormatYmdhmsByDate(djsjlst.get(ilist).get("ZXSJ")));
						}else{
							m.put("DJSJ",StringHelper.FormatYmdhmsByDate(djsjlst.get(ilist).get("DJSJ")));
						}
					}
				}
				sqrsql.append(" SELECT SQR.SQRXM,SQR.SQRLB,SQR.ZJH FROM BDCK.BDCS_SQR SQR "
						+ " WHERE SQR.XMBH='"+m.get("XMBH")+"'");
				List<Map> sqrlst=baseCommonDao.getDataListByFullSql(sqrsql.toString());
				if(!StringHelper.isEmpty(sqrlst)&&sqrlst.size()>0){
					StringBuilder sqr=new StringBuilder();
					StringBuilder sqrzjh=new StringBuilder();
					int j=0;
					for(Map q:sqrlst){
						j++;
						sqr.append("("+ConstHelper.getNameByValue("SQRLB", (String)q.get("SQRLB"))+")"+q.get("SQRXM"));
						sqrzjh.append("("+ConstHelper.getNameByValue("SQRLB", (String)q.get("SQRLB"))+")"+q.get("ZJH"));
						if(j<sqrlst.size()){
							sqr.append(",");
							sqrzjh.append(",");
						}
							
					}
					m.put("SQR", sqr.toString());
					m.put("ZJH", sqrzjh.toString());
				}	
				i++;
				
				blscsql.append(" with a as  (select h.holiday_startdate, h.holiday_enddate from bdc_workflow.Wfd_HolidayBB h where h.holiday_status = '1' and h.holiday_type <> 1),"
							 +" b as (select p.prolsh, sum(t.hangdowm_time - t.hangup_time - (select count(0) from bdc_workflow.Wfd_HolidayBB h where to_char(h.holiday_enddate, 'yyyy-mm-dd')"
							 +" between to_char(t.hangup_time, 'yyyy-mm-dd')"
							 +" and to_char(t.hangdowm_time, 'yyyy-mm-dd')"
							 +" and h.holiday_status = '1' and h.holiday_type <> 1)) as hanguptime"
							 +" from bdc_workflow.wfi_actinst t"
							 +" inner join bdc_workflow.wfi_proinst p on p.proinst_id = t.proinst_id"
							 +" inner join bdck.bdcs_xmxx x"
							 +" on x.ywlsh = p.prolsh"
							 +" where t.hangup_time is not null"
							 +" and t.hangdowm_time is not null"
							 +" and t.hangdowm_time > t.hangup_time"
							 +" and t.hangdowm_time < x.djsj group by p.prolsh)"
							 +" select round(avg(nvl(to_date(to_char(xx.djsj, 'yyyy-MM-dd hh24:mi:ss'),'yyyy-MM-dd hh24:mi:ss') "
							 +" -to_date(to_char(xx.slsj, 'yyyy-MM-dd hh24:mi:ss'),'yyyy-MM-dd hh24:mi:ss')"
							 +" -(select count(1) from a where A.holiday_enddate > xx.slsj and A.holiday_enddate < xx.djsj) - nvl(b.hanguptime, 0),0)),2) as BLSC"
							 +" from bdck.bdcs_xmxx xx"
							 +" left join b  on b.prolsh = xx.ywlsh "
							 +" where  xx.xmbh='"+m.get("XMBH")+"'");
				List<Map> blscList=baseCommonDao.getDataListByFullSql(blscsql.toString());
				if(!StringHelper.isEmpty(blscList)&&blscList.size()>0){
					for (Map b : blscList) {
						if (b.get("BLSC")!=null) {
							m.put("BLSC", b.get("BLSC"));
						}
					}
				}
				if (m.containsKey("SLSJ")) {
					m.put("SLSJ",StringHelper.FormatYmdhmsByDate(m.get("SLSJ")));
				}
				if (m.containsKey("DJLX")) {
					m.put("DJLX",ConstHelper.getNameByValue("DJLX", (String)m.get("DJLX")));
				}
				if (m.containsKey("SFDB")) {
					if(m.get("SFDB").equals("1")){
						m.put("SFDB","已办结");
					}else if(m.get("SFDB").equals("0")){
						m.put("SFDB","正在办理");
					}else{
						m.put("SFDB","");
					}
				}
				
				result_new.add(m);
				}
		}
		msg.setTotal(count);
		msg.setRows(result_new);
		return msg;
		
	}
	@Override
	public Message queryCFInfoFromExcle(Map<String, String> queryvalues,boolean iflike,int page,int rows,String sort,String order) {
		Message msg = new Message();
		long count = 0;
		List<Map> listResult = new ArrayList<Map>();
		StringBuilder builderWhere = new StringBuilder();
		for (Entry<String, String> ent : queryvalues.entrySet()) {
			String name = ent.getKey();
			String value = ent.getValue();
			if (!StringHelper.isEmpty(value) && name.equals("BCFR")) {
				if(iflike){
					builderWhere.append(" AND BCFR like '%"+value+"%'");
		    	}else{
		    		builderWhere.append(" AND BCFR = '"+value+"'");
		    	}	
				
			}
			
			/*if (!StringHelper.isEmpty(value) && !name.equals("CFWH")) {
				
				builderWhere.append(" and instr(" + name + ",'" + value
						+ "')>0 ");
			}*/
			
			if (!StringHelper.isEmpty(value) && name.equals("CFWH")) {
				if(iflike){
					builderWhere.append(" AND CFWH like '%"+value+"%'");
		    	}else{
		    		builderWhere.append(" AND CFWH = '"+value+"'");
		    	}	
			}
			
			if (!StringHelper.isEmpty(value) && name.equals("TDZH")) {
				if(iflike){
					builderWhere.append(" AND TDZH like '%"+value+"%'");
				}else{
					builderWhere.append(" AND TDZH = '"+value+"'");
				}	
			}
			
			if (!StringHelper.isEmpty(value) && name.equals("FCZH")) {
				if(iflike){
					builderWhere.append(" AND FCZH like '%"+value+"%'");
				}else{
					builderWhere.append(" AND FCZH = '"+value+"'");
				}	
			}
			
			if (!StringHelper.isEmpty(value) && name.equals("CFBDW")) {
				if(iflike){
					builderWhere.append(" AND CFBDW like '%"+value+"%'");
				}else{
					builderWhere.append(" AND CFBDW = '"+value+"'");
				}	
			}
			
			if (!StringHelper.isEmpty(value) && name.equals("BH")) {
				if(iflike){
					builderWhere.append(" AND BH like '%"+value+"%'");
				}else{
					builderWhere.append(" AND BH = "+value.replace(" ", "")+"");
				}	
			}
		}

		String fromSql = " FROM BDCK.BDCS_BDCCFXX WHERE 1=1"+ builderWhere.toString() ;
		String fullSql = " SELECT *"+ fromSql;
		count = baseCommonDao.getCountByFullSql(fromSql);
		
		/* 排序 条件语句 */
		
		if(!StringUtils.isEmpty(sort)&&!StringUtils.isEmpty(order))
		{
		if(sort.toUpperCase().equals("BH"))
			sort="LENGTH(TRIM(BH)), BH ";
//			sort="cast(BH as int)";
		if(sort.toUpperCase().equals("SDTIME"))
			sort="SDTIME";
			fullSql=fullSql+" ORDER BY "+sort+" "+order;
		}
		else{
			fullSql=fullSql+" ORDER BY CREATETIME,SDTIME desc";
		}
		listResult = baseCommonDao.getPageDataByFullSql(fullSql, page, rows);
		msg.setTotal(count);
		if(listResult.size() >0){
			for (Map cfmap : listResult) {
				String sfjf = (String) cfmap.get("BSM");
				if(!StringHelper.isEmpty(sfjf)){
					if(sfjf.equals("0")){
						cfmap.put("BSM", "查封");
					}
					if(sfjf.equals("1")){
						cfmap.put("BSM", "协助过户");
					}
					if(sfjf.equals("2")){
						cfmap.put("BSM", "其他类型");
					}
				}
			}
		}
		msg.setRows(listResult);
		return msg;
	}
	@Override
	public Message deleteCfByCfbh(String cfbh){
		Message msg = new Message();
		
		Subject user = SecurityUtils.getSubject();
		User u =  Global.getCurrentUserInfo();
		String curr_user = u.getLoginName();
		
		BDCS_BDCCFXX cfxx = baseCommonDao.get(BDCS_BDCCFXX.class, cfbh);
		if (curr_user.equals(cfxx.getCJR())) {
			baseCommonDao.delete(BDCS_BDCCFXX.class, cfbh);
			//同时删除该查封信息对应文件详情表记录
			baseCommonDao.deleteEntitysByHql(BDCS_UPLOADFILES.class, "FILE_PID = '" + cfbh + "'");
			baseCommonDao.flush();
			msg.setMsg("成功删除编号为："+cfbh+"的查封信息！");
			msg.setSuccess("true");
		} else {
			msg.setMsg("非录入人(创建人)，无法删除此记录！");
			msg.setSuccess("false");
		}
		
		return msg;
		 
	}
	
	public @ResponseBody ResultMessage CheckCFXX(String bdcqzhs, String xmbh, String bdcdylx, String qlids){
		ResultMessage resultMessage = new ResultMessage();
		
		String[] bdcqzh = bdcqzhs.split(",");
		String[] qlid = qlids.split(",");
		String qzhs = "";
		for (int i = 0; i < bdcqzh.length; i++) {
			if (BDCDYLX.H.Value.equals(bdcdylx)) {
				//拿权证号去本地的查封信息表匹配是否有查封
				List<BDCS_BDCCFXX> sgrl_cf_infos = baseCommonDao.getDataList(BDCS_BDCCFXX.class, " FCZH like '%" +bdcqzh[i]+"%'");
				if(sgrl_cf_infos.size() > 0){
					String sfjf = sgrl_cf_infos.get(0).getSFJF();
					if(!StringHelper.isEmpty(sfjf) && sfjf.equals("已查封")){
						if (StringHelper.isEmpty(qzhs)) {
							qzhs += bdcqzh[i];
						} else {
							qzhs += "、"+bdcqzh[i];
						}
						resultMessage.setMsg("不动产权证号为【"+qzhs+"】的房屋在手工录入表中存在查封情况！");
						resultMessage.setSuccess("true");
					}
				} else {
					if(i<qlid.length){
						List<RightsHolder> list = RightsHolderTools.loadRightsHolders(DJDYLY.XZ, qlid[i]);
						if (list != null && list.size() > 0) {
							String qlrmc = list.get(0).getQLRMC();
							String zjh = list.get(0).getZJH();
							if (!StringHelper.isEmpty(qlrmc) && !StringHelper.isEmpty(zjh)) {
								//拿权利人和身份证去本地的查封信息表匹配“被执行人”OR“被查封人”是否有查封
								sgrl_cf_infos = baseCommonDao.getDataList(BDCS_BDCCFXX.class, " (BZXR='" +qlrmc+"' AND BZXRZJH='" + zjh +"') OR (BCFR='" +qlrmc+"' AND BCFRZJHM='" + zjh +"')");
								if(sgrl_cf_infos.size() > 0){
									String sfjf = sgrl_cf_infos.get(0).getSFJF();
									if(!StringHelper.isEmpty(sfjf) && sfjf.equals("已查封")){
										if (StringHelper.isEmpty(qzhs)) {
											qzhs += bdcqzh[i];
										} else {
											qzhs += "、"+bdcqzh[i];
										}
										resultMessage.setMsg("不动产权证号为【"+qzhs+"】的房屋所有权人【"+qlrmc+"】在手工录入表中存在被执行查封情况！");
										resultMessage.setSuccess("true");
									}
								}
							}
						}
					}
				}
			} else if (BDCDYLX.SHYQZD.Value.equals(bdcdylx)) {
				//拿权证号去本地的查封信息表匹配是否有查封
				List<BDCS_BDCCFXX> sgrl_cf_infos = baseCommonDao.getDataList(BDCS_BDCCFXX.class, "TDZH like '%" +bdcqzh[i]+"%'");
				if(sgrl_cf_infos.size() > 0){
					String sfjf = sgrl_cf_infos.get(0).getSFJF();
					if(!StringHelper.isEmpty(sfjf) && sfjf.equals("已查封")){
						if (StringHelper.isEmpty(qzhs)) {
							qzhs += bdcqzh[i];
						} else {
							qzhs += "、"+bdcqzh[i];
						}
						resultMessage.setMsg("不动产权证号为【"+qzhs+"】的宗地在手工录入表中存在查封情况！");
						resultMessage.setSuccess("true");
					}
				} else {
					if(i<qlid.length){
						List<RightsHolder> list = RightsHolderTools.loadRightsHolders(DJDYLY.XZ, qlid[i]);
						if (list != null && list.size() > 0) {
							String qlrmc = list.get(0).getQLRMC();
							String zjh = list.get(0).getZJH();
							if (!StringHelper.isEmpty(qlrmc) && !StringHelper.isEmpty(zjh)) {
								//拿权利人和身份证去本地的查封信息表匹配“被执行人”OR“被查封人”是否有查封
								sgrl_cf_infos = baseCommonDao.getDataList(BDCS_BDCCFXX.class, " (BZXR='" +qlrmc+"' AND BZXRZJH='" + zjh +"') OR (BCFR='" +qlrmc+"' AND BCFRZJHM='" + zjh +"')");
								if(sgrl_cf_infos.size() > 0){
									String sfjf = sgrl_cf_infos.get(0).getSFJF();
									if(!StringHelper.isEmpty(sfjf) && sfjf.equals("已查封")){
										if (StringHelper.isEmpty(qzhs)) {
											qzhs += bdcqzh[i];
										} else {
											qzhs += "、"+bdcqzh[i];
										}
										resultMessage.setMsg("不动产权证号为【"+qzhs+"】的宗地所有权人【"+qlrmc+"】在手工录入表中存在被执行查封情况！");
										resultMessage.setSuccess("true");
									}
								}
							}
						}
					}
				}
			}
		}
		
		return resultMessage;
	}
	
	@Override
	public String saveFiles(String cfxxbh, List<Map> fileInfos) {
		try {
			BDCS_UPLOADFILES file_add = new BDCS_UPLOADFILES();
			String filepaths ="";
			String staffid = Global.getCurrentUserInfo().getId();
			
			if ( fileInfos != null && fileInfos.size() > 0) {
				String filePath = fileInfos.get(0).get("filepath").toString();
				String fileName = fileInfos.get(0).get("filename").toString();
				System.out.println("fileName="+fileName);
				System.out.println("filePath="+filePath);
				
				String basicPath = ConfigHelper.getNameByValue("material");
				basicPath = basicPath + filePath;			
				String allpath = basicPath + "\\" + "\\"+ fileName;	
				
				file_add.setFile_Id(Common.CreatUUID());
				file_add.setFile_Pid(cfxxbh);
				file_add.setFile_Name(fileName);
				file_add.setFile_Path(filePath);
				file_add.setPath(allpath);
				file_add.setUpload_Date(new Date());
				file_add.setUpload_Id(staffid);
				file_add.setFile_Index(new Date().getTime());//排序
				baseCommonDao.save(file_add);
			}
		
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		return "";
	}

	@Override
	public List<Map> AddSFWS(String cfxxbh, MultipartFile file) {
		CommonsMultipartFile cf = (CommonsMultipartFile) file; //
		DiskFileItem fi = (DiskFileItem) cf.getFileItem();
		File file1 = fi.getStoreLocation();
		if (file1.exists()) {
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
			String fileName;
			
				fileName = df.format(new Date()) + "_" + file.getOriginalFilename();
				String filePath = cfxxbh;	
				return FileUpload.uploadFile(file, fileName, null, filePath);
			
		}else {
			return null;
		}
	}
	@Override
	public List<Tree> getfilesByfolderID(String id){
		List<Tree> childList = new ArrayList<Tree>();
		if (!StringUtils.isEmpty(id)) {
			BDCS_BDCCFXX cfxx = baseCommonDao.get(BDCS_BDCCFXX.class, id);
			//原始版本保存在查封表的图片如果有也要显示
			if(cfxx!=null ){
				if(!StringHelper.isEmpty(cfxx.getFILEPATH())){
					String filepaths[] = cfxx.getFILEPATH().split(",");
					for (String path:filepaths) {
						Tree newtree = new Tree();
						int index = path.lastIndexOf("\\");
						String fileName = path.substring(index + 1);  					
//						newtree.setId(id);
						newtree.setParentid(id);
						newtree.setText(fileName);
						newtree.setType("file");
						newtree.setTag1(path);					
						childList.add(newtree);
					}	
				}			
			}
			//新上传的图片都在文件详情表BDCS_UPLOADFILES里
			String sql = "SELECT * FROM BDCK.BDCS_UPLOADFILES WHERE FILE_PID='" + id + "' ORDER BY FILE_INDEX";
			List<Map> files = baseCommonDao.getDataListByFullSql(sql);
			if (files.size() > 0) {
				for (Map file : files) {
					Tree newtree = new Tree();
					newtree.setId(file.get("FILE_ID").toString());
					newtree.setParentid(id);
					newtree.setText(file.get("FILE_NAME").toString());
					newtree.setTag1(file.get("PATH").toString());					
					newtree.setType("file");
					childList.add(newtree);
				}
			}
		}
		return childList;
	}
	
	
	/**
	 * 登记发证数量和上报数量liangc
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Message GetFZL_SBLYWTJ(String tjsjks, String tjsjjz) {
		StringBuilder build = new StringBuilder();
		Message m = new Message();
		try{
			String sql="SELECT FZSBINFO.DJLX, FZSBINFO.QLLX, FZSBINFO.ZSGS, FZSBINFO.ZMGS,FZSBINFO.SBGS "
					 +" FROM "
					 +" (SELECT XMXX.DJLX,  XMXX.QLLX, "
					 +" SUM(CASE  WHEN ZS.BDCQZH LIKE '%不动产权%' THEN 1  ELSE  0  END) AS ZSGS, "
					 +" SUM(CASE  WHEN ZS.BDCQZH LIKE '%不动产证明%' THEN 1   ELSE  0  END) AS ZMGS, "
					 +" SUM(CASE  WHEN (XMXX.SFSB ='1' OR RP.SUCCESSFLAG='2')  THEN  1  ELSE  0  END) AS SBGS " 
					 +" FROM BDCK.BDCS_ZS_GZ ZS "
					 +" LEFT JOIN (SELECT DISTINCT ZSID,DJDYID FROM BDCK.BDCS_QDZR_GZ)QDZR ON ZS.ZSID=QDZR.ZSID "
					 +" LEFT JOIN BDCK.BDCS_REPORTINFO RP ON QDZR.DJDYID=RP.DJDYID "
					 +" LEFT JOIN BDCK.BDCS_XMXX XMXX ON ZS.XMBH=XMXX.XMBH WHERE XMXX.SFDB='1' "
					 +" AND XMXX.SLSJ BETWEEN "
					 +" TO_DATE('"+tjsjks+" 00:00:01', 'yyyy-mm-dd hh24:mi:ss') AND "
					 +" TO_DATE('"+tjsjjz+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') GROUP BY XMXX.DJLX, XMXX.QLLX) FZSBINFO "
					 +" ORDER BY DJLX, TO_NUMBER(QLLX)";
			List<Map> maps = baseCommonDao.getDataListByFullSql(sql);
			if(!StringHelper.isEmpty(maps) && maps.size()>0)
			{
				for(Map map :maps)
				{
					String strdjlx=StringHelper.FormatByDatatype(map.get("DJLX"));
				    map.put("DJLX", ConstHelper.getNameByValue("DJLX", strdjlx));
				    map.put("djlx", strdjlx);
					String strqllx=StringHelper.FormatByDatatype(map.get("QLLX"));
					map.put("qllx", strqllx);
					if(DJLX.YGDJ.Value.equals(strdjlx) && QLLX.QTQL.Value.equals(strqllx))
					{
						map.put("QLLX", "转移预告");
					}
					else if(DJLX.CFDJ.Value.equals(strdjlx) && QLLX.QTQL.Value.equals(strqllx))
					{
						map.put("QLLX", "查封");
					}
					else if(DJLX.CFDJ.Value.equals(strdjlx) && QLLX.CFZX.Value.equals(strqllx))
					{
						map.put("QLLX", "解封");
					}
					else
					{
					map.put("QLLX", ConstHelper.getNameByValue("QLLX", strqllx));
					}
				}
			}
			m.setRows(maps);
			m.setTotal(maps.size());
		}catch(Exception e){
			m=null;
			
		}
		
		return m;
	}
	
	/**
	 * 注销业务查询
	 * @author liangcheng
	 * @time 2018年4月15日 上午11点34分
	 * @param queryvalues
	 * @param page
	 * @param rows
	 * @return
	 */
	public Message zxywQuery(Map<String, String> queryvalues, Integer page,
			Integer rows){
		Map<String, String> newpara = new HashMap<String, String>();
		Message msg = new Message();
		long count = 0;
		StringBuilder builderWhereH = new StringBuilder();
		StringBuilder builderFromF = new StringBuilder();
		StringBuilder builderSelectS = new StringBuilder();
		for (Entry<String, String> ent : queryvalues.entrySet()) {
			String name = ent.getKey();
			String value = ent.getValue();
			if (StringHelper.isEmpty(value)){
				builderWhereH.append("");
			}
			if (!StringHelper.isEmpty(value)){
				if (name.equals("YWLSH")){
					builderWhereH.append(" AND XMXX.YWLSH='" + queryvalues.get("YWLSH") + "'");
				}
				if (name.equals("BDCDYH")){
					builderWhereH.append(" AND DY.BDCDYH='" + queryvalues.get("BDCDYH") + "'");
				}
				if (name.equals("QLRMC")) {
					try {
						newpara.put(
								"QLRMC",
								"%"
										+ new String(queryvalues.get("QLRMC").getBytes(
												"iso8859-1"), "utf-8") + "%");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					builderWhereH.append(" AND EXISTS (SELECT 1 FROM BDCK.BDCS_QLR_LS QLR WHERE XMXX.XMBH=QLR.XMBH AND QLR.QLRMC LIKE'"+newpara.get("QLRMC")+"') ");
				}
				if (name.equals("ZL")) {
					try {
						newpara.put(
								"ZL",
								"%"
										+ new String(queryvalues.get("ZL").getBytes(
												"iso8859-1"), "utf-8") + "%");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					builderWhereH.append(" AND DY.ZL='" + queryvalues.get("ZL") + "'");
				}
				if (name.equals("DJSJ_Q")) {
					builderWhereH.append("  AND XMXX.DJSJ >=to_date('"+queryvalues.get("DJSJ_Q")+"','yyyy-mm-dd') ");
				}
				if (name.equals("DJSJ_Z")) {
					builderWhereH.append("  AND XMXX.DJSJ <=(to_date('"+queryvalues.get("DJSJ_Z")+"','yyyy-mm-dd')) ");
				}
				
				
			}
		}
		builderSelectS.append(" SELECT PO.PRODEF_NAME,XMXX.YWLSH,XMXX.XMBH,DY.BDCDYH,DY.ZL,QL.BDCQZH,XMXX.DJSJ,FSQL.ZXDYYY,FSQL.ZXFJ ");
		String fromSql = " FROM BDCK.BDCS_XMXX XMXX LEFT JOIN BDC_WORKFLOW.WFI_PROINST PO ON XMXX.PROJECT_ID = PO.FILE_NUMBER "
				+ " LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON DJDY.XMBH = XMXX.XMBH "
				+ " LEFT JOIN BDCK.BDCS_QL_LS QL ON QL.DJDYID= DJDY.DJDYID "
				+ " LEFT JOIN BDCK.BDCS_FSQL_LS FSQL ON FSQL.QLID= QL.QLID "
				+ " LEFT JOIN "
				+ " (SELECT BDCDYID,ZL,BDCDYH,'032' AS BDCDYLX FROM BDCK.BDCS_H_LSY "
				+ " UNION "
				+ " SELECT BDCDYID,ZL,BDCDYH,'031' AS BDCDYLX FROM BDCK.BDCS_H_LS "
				+ " UNION "
				+ " SELECT BDCDYID,ZL,BDCDYH,'02' AS BDCDYLX FROM BDCK.BDCS_SHYQZD_LS "
				+ " UNION "
				+ " SELECT BDCDYID,ZL,BDCDYH,'01' AS BDCDYLX FROM BDCK.BDCS_SYQZD_LS"
				+ " UNION "
				+ " SELECT BDCDYID,ZL,BDCDYH,'04' AS BDCDYLX FROM BDCK.BDCS_ZH_LS"
				+ " UNION  "
				+ " SELECT BDCDYID,ZL,BDCDYH,'05' AS BDCDYLX FROM BDCK.BDCS_SLLM_LS "
				+ " UNION  "
				+ " SELECT BDCDYID,ZL,BDCDYH,'09' AS BDCDYLX FROM BDCK.BDCS_NYD_LS) DY ON DY.BDCDYID=DJDY.BDCDYID AND DY.BDCDYLX=DJDY.BDCDYLX "
				+ " WHERE XMXX.SFDB='1' "
				+ " AND  (XMXX.DJLX='400' OR (QL.DJLX='800' AND QL.QLLX='98')) "
				+ builderWhereH.toString() + " ORDER BY XMXX.YWLSH DESC";

		String fullSql = builderSelectS.toString() + fromSql;
		
		count = baseCommonDao.getCountByFullSql("FROM ("+fullSql+") t");
		List<Map> Result = new ArrayList<Map>();
		Result= baseCommonDao.getPageDataByFullSql(fullSql, page, rows);
		List<Map> result_new=new ArrayList<Map>();
		int i = 1;
		if(Result!=null&&Result.size()>0){
			for(Map m:Result){
				StringBuilder sqrsql = new StringBuilder();
				sqrsql.append(" SELECT QLRMC,ZJH,XMBH FROM BDCK.BDCS_QLR_GZ QLR "
						+ " WHERE QLR.XMBH='"+m.get("XMBH")+"'");
				List<Map> sqrlst=baseCommonDao.getDataListByFullSql(sqrsql.toString());
				if(!StringHelper.isEmpty(sqrlst)&&sqrlst.size()>0){
					StringBuilder qlrmc=new StringBuilder();
					int j=0;
					for(Map q:sqrlst){
						j++;
						qlrmc.append(q.get("QLRMC"));
						if(j<sqrlst.size())
							qlrmc.append(",");
					}
					m.put("QLRMC", qlrmc.toString());
				}	
				i++;
				
				
				if (m.containsKey("DJSJ")) {
					m.put("DJSJ",StringHelper.FormatYmdhmsByDate(m.get("DJSJ")));
				}
				
				result_new.add(m);
				}
		}
		msg.setTotal(count);
		msg.setRows(result_new);
		return msg;
		
	}
	/**
	 * 获取正在做变更且已经做了预关联的宗地信息
	 * @return
	 */
	public Map<String, String> getInfo(House  house){
		if(house!=null&&!StringHelper.isEmpty(house.getNZDBDCDYID())) {
			Map<String, String> map =new  HashMap<String, String>();
			//1.获取宗地权利数据数据
			List<BDCS_QL_GZ> ql_gz_list=baseCommonDao.getDataList(BDCS_QL_GZ.class, "bdcdyid='"+house.getNZDBDCDYID()+"'");
			if (ql_gz_list!=null&&ql_gz_list.size()>0) {
				//2.获取项目信息
				BDCS_XMXX xmxx=null;
				for(BDCS_QL_GZ ql_gz:ql_gz_list) {
					if(!StringHelper.isEmpty(ql_gz.getXMBH())) {
						xmxx=Global.getXMXXbyXMBH(ql_gz.getXMBH());	
						if(xmxx!=null&&"0".equals(xmxx.getSFDB())&&"300".equals(xmxx.getDJLX())) {
							//宗地在做变更登记,未登簿
							map.put("YWLSH", xmxx.getYWLSH());
							map.put("DJLX", xmxx.getDJLX());
							map.put("ZD_BDCDYH",ql_gz.getBDCDYH());
							break;
						}
					}
					
				}
				
			}
			if(map!=null&&map.size()>0) {
				map.put("H_BDCDYH", house.getBDCDYH());
				return map;
			}
		}
		
		return null;
	}
	
}
