package com.supermap.realestate.registrationbook.service.impl;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.RoundingMode;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.model.BDCS_CONST;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_H_LS;
import com.supermap.realestate.registration.model.BDCS_H_LSY;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_LS;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_LS;
import com.supermap.realestate.registration.model.BDCS_SLLM_LS;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_SYQZD_LS;
import com.supermap.realestate.registration.model.BDCS_TDYT_LS;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_YHYDZB_LS;
import com.supermap.realestate.registration.model.BDCS_YHZK_LS;
import com.supermap.realestate.registration.model.BDCS_ZDBHQK_LS;
import com.supermap.realestate.registration.model.BDCS_ZH_LS;
import com.supermap.realestate.registration.model.BDCS_ZRZ_LS;
import com.supermap.realestate.registration.model.BDCS_ZRZ_LSY;
import com.supermap.realestate.registration.model.YC_SC_H_XZ;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.tools.HistoryBackdateTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.DYFS;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registrationbook.model.BDCS_DJDY_LS_EX;
import com.supermap.realestate.registrationbook.model.BDCS_DJDY_LS_EX.DJBMenu;
import com.supermap.realestate.registrationbook.model.BookMenu;
import com.supermap.realestate.registrationbook.model.DJBZD;
import com.supermap.realestate.registrationbook.model.Unit_EX;
import com.supermap.realestate.registrationbook.model.Unit_EX.QL_EX;
import com.supermap.realestate.registrationbook.service.BookService;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ui.tree.State;


@Service("bookService")
public class BookServiceImpl implements BookService {

	@Autowired
	private CommonDao baseCommonDao;
	String PRE_QL = "QL_",// 返回字段的前缀名，用来标识该字段来自哪个表，以后维护方便。
			PRE_QLR = "QLR_", PRE_FSQL = "FSQL_",
			PRE_SQR = "SQR_",
			PRE_UNIT = "UNIT_";

	// 宗地和宗海列表，先分页后再合并，解决数据量大 读取卡问题
	private String CreateZDORZHSQL(int count_SHYQZD, int count_SYQZD,
			int count_ZH,int count_NYD, int currentpage, int pageSize,
			Map<String, String> conditionParameter) throws UnsupportedEncodingException {
		int start = (currentpage - 1) * pageSize;// 分页的开始数据
		int end = start + pageSize;// 分页的结束数据
		int start_shyqzd = 0;// 使用权宗地列表获取的开始数据
		int end_shyqzd = 0;// 使用权宗地列表获取的结束数据
		int start_syqzd = 0;// 所有权宗地列表获取的开始数据
		int end_syqzd = 0;// 所有权宗地列表获取的结束数据
		int start_zh = 0;// 海域列表获取的开始数据
		int end_zh = 0;// 海域列表获取的结束数据、
		int start_nyd = 0;// 海域列表获取的开始数据
		int end_nyd = 0;// 海域列表获取的结束数据、
		//下面是计算根据要求显示的页数和个数，计算使用优先查询那张表的记录行数
		
		if (start <= count_SHYQZD) {
			if (end <= count_SHYQZD) {
				start_shyqzd = start;
				end_shyqzd = end;
			}
			if (end > count_SHYQZD && end <= count_SHYQZD + count_SYQZD) {
				start_shyqzd = start;
				end_shyqzd = count_SHYQZD;
				start_syqzd = 0;
				end_syqzd = end - end_shyqzd;
			}
			if (end > count_SHYQZD + count_SYQZD
					&& end <= count_SHYQZD + count_SYQZD + count_ZH) {
				start_shyqzd = start;
				end_shyqzd = count_SHYQZD;
				start_syqzd = 0;
				end_syqzd = count_SYQZD;
				start_zh = 0;
				end_zh = end - count_SHYQZD - count_SYQZD;
			}
			if (end > count_SHYQZD + count_SYQZD + count_ZH
					&& end <= count_SHYQZD + count_SYQZD + count_ZH + count_NYD) {
				start_shyqzd = start;
				end_shyqzd = count_SHYQZD;
				start_syqzd = 0;
				end_syqzd = count_SYQZD;
				start_zh = 0;
				end_zh = count_ZH;
				start_nyd = 0;
				end_nyd = end - count_SHYQZD - count_ZH;
			}
			if (end > count_SHYQZD + count_SYQZD + count_ZH + count_NYD) {
				start_shyqzd = start;
				end_shyqzd = count_SHYQZD;
				start_syqzd = 0;
				end_syqzd = count_SYQZD;
				start_zh = 0;
				end_zh = count_ZH;
				start_nyd = 0;
				end_nyd = end;
			}
		}
		if (start > count_SHYQZD && start <= count_SHYQZD + count_SYQZD) {
			if (end > count_SHYQZD && end <= count_SHYQZD + count_SYQZD) {
				start_syqzd = start - count_SHYQZD;
				end_syqzd = end;
			}
			if (end > count_SHYQZD + count_SYQZD
					&& end <= count_SHYQZD + count_SYQZD + count_ZH) {
				start_syqzd = start - count_SHYQZD;
				end_syqzd = count_SYQZD;
				start_zh = 0;
				end_zh = end - count_SHYQZD - count_SYQZD;
			}
			if (end > count_SHYQZD + count_SYQZD + count_ZH
					&& end <= count_SHYQZD + count_SYQZD + count_ZH + count_NYD) {
				start_shyqzd = start - count_SHYQZD;
				end_shyqzd = count_SHYQZD;
				start_syqzd = 0;
				end_syqzd = count_SYQZD;
				start_zh = 0;
				end_zh = count_ZH;
				start_nyd = 0;
				end_nyd = end - count_SHYQZD - count_ZH;
			}
			if (end > count_SHYQZD + count_SYQZD + count_ZH + count_NYD) {
				start_shyqzd = start - count_SHYQZD;
				end_shyqzd = count_SHYQZD;
				start_syqzd = 0;
				end_syqzd = count_SYQZD;
				start_zh = 0;
				end_zh = count_ZH;
				start_nyd = 0;
				end_nyd = end;
			}
		}
		if (start > count_SHYQZD + count_SYQZD
				&& start <= count_SHYQZD + count_SYQZD + count_ZH) {
			if (end > count_SHYQZD && end <= count_SHYQZD + count_SYQZD) {
				start_syqzd = start - count_SHYQZD - count_SYQZD;
				end_syqzd = end;
			}
			if (end > count_SHYQZD + count_SYQZD
					&& end <= count_SHYQZD + count_SYQZD + count_ZH) {
				start_syqzd = start - count_SHYQZD - count_SYQZD;
				end_syqzd = count_SYQZD;
				start_zh = 0;
				end_zh = end - count_SHYQZD - count_SYQZD;
			}
			if (end > count_SHYQZD + count_SYQZD + count_ZH
					&& end <= count_SHYQZD + count_SYQZD + count_ZH + count_NYD) {
				start_shyqzd = start - count_SHYQZD - count_SYQZD;
				end_shyqzd = count_SHYQZD;
				start_syqzd = 0;
				end_syqzd = count_SYQZD;
				start_zh = 0;
				end_zh = count_ZH;
				start_nyd = 0;
				end_nyd = end - count_SHYQZD - count_ZH;
			}
			if (end > count_SHYQZD + count_SYQZD + count_ZH + count_NYD) {
				start_shyqzd = start - count_SHYQZD - count_SYQZD;
				end_shyqzd = count_SHYQZD;
				start_syqzd = 0;
				end_syqzd = count_SYQZD;
				start_zh = 0;
				end_zh = count_ZH;
				start_nyd = 0;
				end_nyd = end;
			}
		}
		if(start > count_SHYQZD + count_SYQZD + count_ZH
				&& start <= count_SHYQZD + count_SYQZD + count_ZH + count_NYD) {
			if (end > count_SHYQZD && end <= count_SHYQZD + count_SYQZD) {
				start_syqzd = start - count_SHYQZD - count_SYQZD - count_ZH;
				end_syqzd = end;
			}
			if (end > count_SHYQZD + count_SYQZD
					&& end <= count_SHYQZD + count_SYQZD + count_ZH) {
				start_syqzd = start - count_SHYQZD - count_SYQZD - count_ZH;
				end_syqzd = count_SYQZD;
				start_zh = 0;
				end_zh = end - count_SHYQZD - count_SYQZD;
			}
			if (end > count_SHYQZD + count_SYQZD + count_ZH
					&& end <= count_SHYQZD + count_SYQZD + count_ZH + count_NYD) {
				start_shyqzd = start - count_SHYQZD - count_SYQZD - count_ZH;
				end_shyqzd = count_SHYQZD;
				start_syqzd = 0;
				end_syqzd = count_SYQZD;
				start_zh = 0;
				end_zh = count_ZH;
				start_nyd = 0;
				end_nyd = end - count_SHYQZD - count_ZH;
			}
			if (end > count_SHYQZD + count_SYQZD + count_ZH + count_NYD) {
				start_shyqzd = start - count_SHYQZD - count_SYQZD - count_ZH;
				end_shyqzd = count_SHYQZD;
				start_syqzd = 0;
				end_syqzd = count_SYQZD;
				start_zh = 0;
				end_zh = count_ZH;
				start_nyd = 0;
				end_nyd = end;
			}
		}
		StringBuilder allsql = new StringBuilder();
		StringBuilder shyqzdSql = new StringBuilder(
				" SELECT ZDDM AS ZDDM,ZL,BDCDYID,'"
						+ ConstValue.BDCDYLX.SHYQZD.Value
						+ "' AS ZDORZH,BDCDYH FROM BDCK.BDCS_SHYQZD_XZ WHERE 1>0  ");
		StringBuilder syqzdSql = new StringBuilder(
				" SELECT ZDDM AS ZDDM,ZL,BDCDYID,'"
						+ ConstValue.BDCDYLX.SYQZD.Value
						+ "' AS ZDORZH,BDCDYH  FROM BDCK.BDCS_SYQZD_XZ WHERE  1>0 ");
		StringBuilder zhSql = new StringBuilder(
				" SELECT ZHDM AS ZDDM,ZL,BDCDYID,'"
						+ ConstValue.BDCDYLX.HY.Value
						+ "' AS ZDORZH,BDCDYH  FROM BDCK.BDCS_ZH_XZ WHERE  1>0 ");
		StringBuilder nydSql = new StringBuilder(
				" SELECT ZDDM,ZL,BDCDYID,'"
						+ ConstValue.BDCDYLX.NYD.Value
						+ "' AS ZDORZH,BDCDYH  FROM BDCK.BDCS_NYD_XZ WHERE  1>0 ");
		String h_xz_querysql =" SELECT ZDBDCDYID FROM(SELECT  ZDBDCDYID,BDCDYH,ZL FROM BDCK.BDCS_H_XZ "
				+"UNION SELECT  ZDBDCDYID,BDCDYH,ZL    FROM BDCK.BDCS_H_XZY  "+
				" UNION SELECT  ZDBDCDYID,BDCDYH,ZL    FROM BDCK.BDCS_SLLM_XZ ) WHERE 1>0 %s";
		StringBuilder h_xz_querysql_where = new StringBuilder();
		if (!StringUtils.isEmpty(conditionParameter.get("BDCDYH_FW"))) {
			h_xz_querysql_where.append(" AND BDCDYH LIKE '%"+new String(conditionParameter.get("BDCDYH_FW").getBytes(
					"iso8859-1"), "utf-8"));
			h_xz_querysql_where.append("%'");
			
		}
		if (!StringUtils.isEmpty(conditionParameter.get("ZL_FW"))) {
			h_xz_querysql_where.append(" AND ZL LIKE '%"+new String(conditionParameter.get("ZL_FW").getBytes(
					"iso8859-1"), "utf-8"));
			h_xz_querysql_where.append("%'");
			
		}
		
		if (!StringUtils.isEmpty(conditionParameter.get("QLR_FW"))) {
			h_xz_querysql =" SELECT ZDBDCDYID FROM(SELECT  ZDBDCDYID,BDCDYH,ZL FROM BDCK.BDCS_H_XZ "
					+"UNION SELECT  ZDBDCDYID,BDCDYH,ZL    FROM BDCK.BDCS_H_XZY  "
					+" UNION SELECT  ZDBDCDYID,BDCDYH,ZL    FROM BDCK.BDCS_SLLM_XZ "
					+" UNION SELECT  BDCDYID AS ZDBDCDYID,BDCDYH,ZL    FROM BDCK.BDCS_NYD_XZ "
					+" UNION  SELECT BDCDYID as ZDBDCDYID, BDCDYH, ZL FROM BDCK.BDCS_SHYQZD_XZ) B "
					+ " JOIN BDCK.BDCS_QL_XZ QL ON B.BDCDYH = QL.BDCDYH JOIN BDCK.BDCS_QLR_XZ QLR ON QLR.QLID = QL.QLID "
					+ " WHERE 1>0 %s";
			h_xz_querysql_where.append(" AND QLR.QLRMC LIKE '%"+new String(conditionParameter.get("QLR_FW").getBytes(
					"iso8859-1"), "utf-8"));
			h_xz_querysql_where.append("%'");
			
		}
		if (!StringUtils.isEmpty(h_xz_querysql_where.toString())) 
		{
			h_xz_querysql=String
			.format(h_xz_querysql,h_xz_querysql_where.toString());
			shyqzdSql.append("  AND BDCDYID IN ("+h_xz_querysql+")");
			syqzdSql.append("  AND BDCDYID IN ("+h_xz_querysql+")");
			zhSql.append("  AND BDCDYID IN ("+h_xz_querysql+")");
			nydSql.append("  AND BDCDYID IN ("+h_xz_querysql+")");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("ZL"))) {
			shyqzdSql.append("  AND ZL LIKE:ZL ");
			syqzdSql.append("  AND ZL LIKE:ZL ");
			zhSql.append("  AND ZL LIKE:ZL ");
			nydSql.append("  AND ZL LIKE:ZL ");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("BDCDYH"))) {
			shyqzdSql.append("  AND BDCDYH LIKE:BDCDYH ");
			syqzdSql.append("  AND BDCDYH LIKE:BDCDYH ");
			zhSql.append("  AND BDCDYH LIKE:BDCDYH ");
			nydSql.append("  AND BDCDYH LIKE:BDCDYH ");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("ZDDM"))) {
			shyqzdSql.append("  AND ZDDM LIKE:ZDDM ");
			syqzdSql.append("  AND ZDDM LIKE:ZDDM ");
			zhSql.append("  AND ZHDM LIKE:ZDDM ");
			nydSql.append("  AND ZDDM LIKE:ZDDM ");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("ZDORZH"))) {
			String _zdorzh = conditionParameter.get("ZDORZH");
			if (_zdorzh.equals("01")) {// 表示所有权宗地01和使用权宗地02
				zhSql.append("  AND 1=0 ");
				nydSql.append("  AND 1=0 ");
			}
			if (_zdorzh.equals("04")) {// 表示宗海
				shyqzdSql.append("  AND 1=0 ");
				syqzdSql.append("  AND 1=0 ");
				nydSql.append("  AND 1=0 ");
			}
			if (_zdorzh.equals("09")) {// 表示宗海
				shyqzdSql.append("  AND 1=0 ");
				syqzdSql.append("  AND 1=0 ");
				zhSql.append("  AND 1=0 ");
			}
		}
		shyqzdSql.append("  ORDER BY BDCDYID ");
		syqzdSql.append("  ORDER BY BDCDYID ");
		zhSql.append("  ORDER BY BDCDYID ");
		nydSql.append("  ORDER BY BDCDYID ");
		// 每个表组合成分页语句
		String pagingShyqzdSql = String
				.format(" SELECT * FROM (SELECT A.*, ROWNUM RN FROM (%s) A WHERE ROWNUM < %s ) WHERE RN > %s ",
						shyqzdSql.toString(), end_shyqzd + 1, start_shyqzd);
		String pagingSyqzdSql = String
				.format(" SELECT * FROM (SELECT A.*, ROWNUM RN FROM (%s) A WHERE ROWNUM < %s ) WHERE RN > %s ",
						syqzdSql.toString(), end_syqzd + 1, start_syqzd);
		String pagingZhSql = String
				.format(" SELECT * FROM (SELECT A.*, ROWNUM RN FROM (%s) A WHERE ROWNUM < %s ) WHERE RN > %s ",
						zhSql.toString(), end_zh + 1, start_zh);
		String pagingNYDSql = String
				.format(" SELECT * FROM (SELECT A.*, ROWNUM RN FROM (%s) A WHERE ROWNUM < %s ) WHERE RN > %s ",
						nydSql.toString(), end_nyd + 1, start_nyd);
		// 将各个表分页后的数据合并的语句
		allsql.append(" SELECT ZDDM,ZL,ZDORZH,BDCDYID,BDCDYH FROM ");
		allsql.append("(");
		allsql.append(pagingShyqzdSql);
		allsql.append(" UNION ALL ");
		allsql.append(pagingSyqzdSql);
		allsql.append(" UNION ALL ");
		allsql.append(pagingZhSql);
		allsql.append(" UNION ALL ");
		allsql.append(pagingNYDSql);
		allsql.append(") T");
		return allsql.toString();
	}

	/**
	 * 获取宗地或宗海信息
	 * 
	 * @作者 wuzhu
	 * @创建时间 2015年6月9日下午2:51:52
	 * @param page
	 * @param rows
	 * @param condition
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("rawtypes")
	public Message GetZDORZHList(Map<String, String> conditionParameter,
			int currentpage, int pageSize) throws UnsupportedEncodingException {
		Map<String, String> newpara = new HashMap<String, String>();
		StringBuilder count_allsql = new StringBuilder();
		StringBuilder count_shyqzdSql = new StringBuilder(
				" SELECT COUNT(0) AS C FROM BDCK.BDCS_SHYQZD_XZ WHERE  1>0  ");
		StringBuilder count_syqzdSql = new StringBuilder(
				" SELECT  COUNT(0) AS C   FROM BDCK.BDCS_SYQZD_XZ WHERE  1>0 ");
		StringBuilder count_zhSql = new StringBuilder(
				" SELECT  COUNT(0) AS C   FROM BDCK.BDCS_ZH_XZ WHERE  1>0 ");
		StringBuilder count_nydSql = new StringBuilder(
				" SELECT  COUNT(0) AS C   FROM BDCK.BDCS_NYD_XZ WHERE  1>0 ");
		//不动产单元数SQL语句
		StringBuilder  bdcdyCountSql=new StringBuilder(" SELECT (SUM(C)+1) AS C FROM (");//带上自身的宗海或宗地，加1
		bdcdyCountSql.append(" SELECT COUNT(0) AS C FROM BDCK.BDCS_H_XZ WHERE ZDBDCDYID ='%s' ");
		bdcdyCountSql.append(" UNION ALL ");
		bdcdyCountSql.append(" SELECT COUNT(0) AS C FROM BDCK.BDCS_H_XZY WHERE ZDBDCDYID ='%s' ");
		bdcdyCountSql.append(" UNION ALL ");
		bdcdyCountSql.append(" SELECT COUNT(0) AS C  FROM BDCK.BDCS_SLLM_XZ WHERE ZDBDCDYID ='%s' ");
		bdcdyCountSql.append(" ) T  ");
		//登记机构SQL语句
		String djjgSql = "SELECT DJJG FROM BDCK.BDCS_DJDY_LS LEFT JOIN BDCK.BDCS_QL_LS ON BDCK.BDCS_DJDY_LS.DJDYID=BDCK.BDCS_QL_LS.DJDYID WHERE BDCK.BDCS_DJDY_LS.BDCDYID='%s' AND ROWNUM <2 ";
		//宗地宗海上的单元信息查询语句
		String h_xz_querysql =" SELECT ZDBDCDYID FROM(SELECT  ZDBDCDYID,BDCDYH,ZL FROM BDCK.BDCS_H_XZ "
				+"UNION SELECT  ZDBDCDYID,BDCDYH,ZL    FROM BDCK.BDCS_H_XZY  "+
				" UNION SELECT  ZDBDCDYID,BDCDYH,ZL    FROM BDCK.BDCS_SLLM_XZ ) WHERE 1>0 %s";
		StringBuilder h_xz_querysql_where = new StringBuilder();
		if (!StringUtils.isEmpty(conditionParameter.get("BDCDYH_FW"))) {
			h_xz_querysql_where.append(" AND BDCDYH LIKE '%"+new String(conditionParameter.get("BDCDYH_FW").getBytes(
					"iso8859-1"), "utf-8"));
			h_xz_querysql_where.append("%'");
			
		}
		if (!StringUtils.isEmpty(conditionParameter.get("ZL_FW"))) {
			h_xz_querysql_where.append(" AND ZL LIKE '%"+new String(conditionParameter.get("ZL_FW").getBytes(
					"iso8859-1"), "utf-8"));
			h_xz_querysql_where.append("%'");
			
		}
		
		if (!StringUtils.isEmpty(conditionParameter.get("QLR_FW"))) {
			h_xz_querysql =" SELECT ZDBDCDYID FROM(SELECT  ZDBDCDYID,BDCDYH,ZL FROM BDCK.BDCS_H_XZ "
					+"UNION SELECT  ZDBDCDYID,BDCDYH,ZL    FROM BDCK.BDCS_H_XZY  "+
					" UNION SELECT  ZDBDCDYID,BDCDYH,ZL    FROM BDCK.BDCS_SLLM_XZ "
					+ "Union Select BDCDYID as ZDBDCDYID, BDCDYH, ZL From BDCK.BDCS_SHYQZD_XZ ) B "
					+ "JOIN BDCK.BDCS_QL_XZ QL ON B.BDCDYH = QL.BDCDYH JOIN BDCK.BDCS_QLR_XZ QLR ON QLR.QLID = QL.QLID "
					+ "WHERE 1>0 %s";
			h_xz_querysql_where.append(" AND QLR.QLRMC LIKE '%"+new String(conditionParameter.get("QLR_FW").getBytes(
					"iso8859-1"), "utf-8"));
			h_xz_querysql_where.append("%'");
			
		}
		
		if (!StringUtils.isEmpty(h_xz_querysql_where.toString())) 
		{
			h_xz_querysql=String
			.format(h_xz_querysql,h_xz_querysql_where.toString());
			count_shyqzdSql.append("  AND BDCDYID IN ("+h_xz_querysql+")");
			count_syqzdSql.append("  AND BDCDYID IN ("+h_xz_querysql+")");
			count_zhSql.append("  AND BDCDYID IN ("+h_xz_querysql+")");
			count_nydSql.append("  AND BDCDYID IN ("+h_xz_querysql+")");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("ZL"))) {
			count_shyqzdSql.append("  AND ZL LIKE:ZL ");
			count_syqzdSql.append("  AND ZL LIKE:ZL ");
			count_zhSql.append("  AND ZL LIKE:ZL ");
			count_nydSql.append("  AND ZL LIKE:ZL ");
			newpara.put(
					"ZL",
					"%"
							+ new String(conditionParameter.get("ZL").getBytes(
									"iso8859-1"), "utf-8") + "%");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("BDCDYH"))) {
			count_shyqzdSql.append("  AND BDCDYH LIKE:BDCDYH ");
			count_syqzdSql.append("  AND BDCDYH LIKE:BDCDYH ");
			count_zhSql.append("  AND BDCDYH LIKE:BDCDYH ");
			count_nydSql.append("  AND BDCDYH LIKE:BDCDYH ");
			newpara.put(
					"BDCDYH",
					"%"
							+ new String(conditionParameter.get("BDCDYH")
									.getBytes("iso8859-1"), "utf-8") + "%");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("ZDORZH"))) {
			String _zdorzh = new String(conditionParameter.get("ZDORZH")
					.getBytes("iso8859-1"), "utf-8");
			if (_zdorzh.equals("01")) {// 表示所有权宗地01和使用权宗地02
				count_zhSql.append("  AND 1=0 ");
				count_nydSql.append("  AND 1=0 ");
			}
			if (_zdorzh.equals("04")) {// 表示宗海
				count_shyqzdSql.append("  AND 1=0 ");
				count_syqzdSql.append("  AND 1=0 ");
				count_nydSql.append("  AND 1=0 ");
			}
			if (_zdorzh.equals("09")) {// 表示农用地
				count_shyqzdSql.append("  AND 1=0 ");
				count_syqzdSql.append("  AND 1=0 ");
				count_zhSql.append("  AND 1=0 ");
			}
			
		}
		if (!StringUtils.isEmpty(conditionParameter.get("ZDDM"))) {
			count_shyqzdSql.append("  AND ZDDM LIKE:ZDDM ");
			count_syqzdSql.append("  AND ZDDM LIKE:ZDDM ");
			count_zhSql.append("  AND ZHDM LIKE:ZDDM ");
			count_nydSql.append("  AND ZDDM LIKE:ZDDM ");
			newpara.put(
					"ZDDM",
					"%"
							+ new String(conditionParameter.get("ZDDM")
									.getBytes("iso8859-1"), "utf-8") + "%");
		}
		count_allsql.append(count_shyqzdSql.toString());
		count_allsql.append(" UNION ALL ");
		count_allsql.append(count_syqzdSql.toString());
		count_allsql.append(" UNION ALL ");
		count_allsql.append(count_zhSql.toString());
		count_allsql.append(" UNION ALL ");
		count_allsql.append(count_nydSql.toString());
		List<Map> counts = baseCommonDao.getDataListByFullSql(
				count_allsql.toString(), newpara);
		int count_SHYQZD = 0;
		int count_SYQZD = 0;
		int count_ZH = 0;
		int count_NYD = 0;
		count_SHYQZD = StringHelper.getInt(counts.get(0).get("C"));
		count_SYQZD = StringHelper.getInt(counts.get(1).get("C"));
		count_ZH = StringHelper.getInt(counts.get(2).get("C"));
		count_NYD = StringHelper.getInt(counts.get(3).get("C"));
		if (count_SHYQZD + count_SYQZD + count_ZH + count_NYD == 0)
			return new Message();
		String dataSql = CreateZDORZHSQL(count_SHYQZD, count_SYQZD, count_ZH,count_NYD,
				currentpage, pageSize, conditionParameter);
		List<Map> result = baseCommonDao.getDataListByFullSql(dataSql, newpara);
		List<DJBZD> dbbzds = new ArrayList<DJBZD>();
		for (int i = 0; i < result.size(); i++) {
			@SuppressWarnings("unchecked")
			Map<String, String> m = result.get(i);
			DJBZD d = new DJBZD();
			d.setBdcdyh(StringHelper.formatObject(m.get("BDCDYH")));
			d.setBdcdyid(StringHelper.formatObject(m.get("BDCDYID")));
			d.setZdorzh(StringHelper.formatObject(m.get("ZDORZH")));
			d.setZdzhdm(StringHelper.formatObject(m.get("ZDDM")));
			d.setZl(StringHelper.formatObject(m.get("ZL")));
			List<Map> bdcdys = baseCommonDao.getDataListByFullSql(String
					.format(bdcdyCountSql.toString(), d.getBdcdyid(), d.getBdcdyid(), d.getBdcdyid()));
			//List<Map> djjg = baseCommonDao.getDataListByFullSql(String.format(
			//		djjgSql, d.getBdcdyid()));
			if (bdcdys != null && bdcdys.size() > 0) {
				d.setBdcdys(StringHelper.FormatByDatatype(bdcdys.get(0)
						.get("C")));
			}
			//if (djjg != null && djjg.size() > 0) {
			//	d.setDjjg(StringHelper
			//			.FormatByDatatype(djjg.get(0).get("DJJG")));
			//}
			d.setDjjg(ConfigHelper.getNameByValue("XZQHMC")+ConfigHelper.getNameByValue("DJJGMC"));
			dbbzds.add(d);
		}
		Message msg = new Message();
		msg.setRows(dbbzds);
		msg.setTotal(count_SHYQZD + count_SYQZD + count_ZH + count_NYD);
		return msg;
	}

	/**
	 * @author WUZ 根据宗地或宗海代码 获取扩展的登记单元列表信息等
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	private List<BDCS_DJDY_LS_EX> GetDJDY_EX_List(String zdOrZHbdcdyid) {
		StringBuilder  mysql=new StringBuilder(" SELECT * FROM (");
		mysql.append(" SELECT BDCDYID,'031' AS BDCDYLX FROM BDCK.BDCS_H_XZ WHERE ZDBDCDYID ='"+zdOrZHbdcdyid+"' ");
		mysql.append(" UNION ALL ");
		mysql.append(" SELECT BDCDYID,'032' AS BDCDYLX FROM BDCK.BDCS_H_XZY WHERE ZDBDCDYID ='"+zdOrZHbdcdyid+"' ");
		mysql.append(" UNION ALL ");
		mysql.append(" SELECT BDCDYID,'05' AS BDCDYLX FROM BDCK.BDCS_SLLM_XZ WHERE ZDBDCDYID ='"+zdOrZHbdcdyid+"' ");
		mysql.append(" UNION ALL ");
		mysql.append(" SELECT BDCDYID,'01' AS BDCDYLX FROM  BDCK.BDCS_SYQZD_XZ WHERE BDCDYID='"+zdOrZHbdcdyid+"' ");
		mysql.append(" UNION ALL ");
		mysql.append(" SELECT BDCDYID,'02' AS BDCDYLX FROM BDCK.BDCS_SHYQZD_XZ WHERE BDCDYID='"+zdOrZHbdcdyid+"' ");
		mysql.append(" UNION ALL ");
		mysql.append(" SELECT BDCDYID,'04' AS BDCDYLX FROM BDCK.BDCS_ZH_XZ WHERE BDCDYID='"+zdOrZHbdcdyid+"' ");
		mysql.append(" ) T ORDER BY BDCDYLX, BDCDYID ");
		List<Map> units = baseCommonDao.getDataListByFullSql(mysql.toString());
//		List<BDCS_DJDY_LS> didys_distinct = new ArrayList<BDCS_DJDY_LS>();// 实现DISTINCT
//																			// BDCDYID||BDCDYH
//		Map<String, String> map_distinct = new HashMap<String, String>();
//		for (BDCS_DJDY_LS djdy : djdys) {
//			String _key = djdy.getBDCDYID() + djdy.getBDCDYH();
//			if (!map_distinct.containsKey(_key)) {
//				didys_distinct.add(djdy);
//				map_distinct.put(_key, "");
//			}
//		}
		List<BDCS_DJDY_LS_EX> djdy_exs = new ArrayList<BDCS_DJDY_LS_EX>();
		for (Map<String,String> unit : units) {// 遍历登记单元的每个登记信息，获取的值填充BDCS_DJDY_LS_EX类
			BDCS_DJDY_LS_EX djdy_ex = new BDCS_DJDY_LS_EX();
			//ObjectHelper.copyObject(djdy, djdy_ex);
			djdy_ex.Init(baseCommonDao,unit.get("BDCDYID"),BDCDYLX.initFrom(unit.get("BDCDYLX")));
			djdy_exs.add(djdy_ex);
		}
		return djdy_exs;
	}
	
	/**
	 * @作者 wuz
	 * @创建时间 2015年11月17日下午6:16:06  传递不动产单元ID，获取该不动产单元的权利树形
	 * @param bdcdyid 不动产单元ID
	 * @param bdcdylx 不动产单元类型
	 * @return
	 */
	public List<BookMenu> GetUnitTree(String bdcdyid,String bdcdylx) {
		
		StringBuilder fulSql = new StringBuilder(" SELECT GZDY.BDCDYID AS GZBDCDYID, LSDY.BDCDYID AS LSBDCDYID ");
		fulSql.append(" FROM BDCK.BDCS_DJDY_GZ GZDY ");
		fulSql.append(" LEFT JOIN BDCK.BDCS_DJDY_LS LSDY ON GZDY.DJDYID = LSDY.DJDYID AND GZDY.BDCDYLX = LSDY.BDCDYLX ");
		fulSql.append(" WHERE GZDY.BDCDYLX = '").append(bdcdylx).append("' AND GZDY.BDCDYID = '").append(bdcdyid).append("' ");
		List<Map> lsdy = baseCommonDao.getDataListByFullSql(fulSql.toString());
		if(lsdy!=null&&lsdy.size()>0){
			if(!bdcdyid.equals(lsdy.get(0).get("LSBDCDYID"))){
				if(!StringHelper.isEmpty(lsdy.get(0).get("LSBDCDYID")))
					bdcdyid = lsdy.get(0).get("LSBDCDYID").toString();
			}
		}
		BDCS_DJDY_LS_EX djdy_ex = new BDCS_DJDY_LS_EX();
		djdy_ex.Init(baseCommonDao,bdcdyid,BDCDYLX.initFrom(bdcdylx));
		List<BookMenu> trs = new ArrayList<BookMenu>();
		String uuid_unit = java.util.UUID.randomUUID().toString();
		Map<String, String> attr_unit = new HashMap<String, String>();
		attr_unit.put("url", "#");
		BookMenu node_unit = CreateBookMenu("", uuid_unit,
				djdy_ex.getUnit() == null ? "" : djdy_ex.getUnit().getZL(), attr_unit);
		List<BookMenu> children = new ArrayList<BookMenu>();
		for (BookMenu menu : djdy_ex.getMenus()) {
			menu.setParentid(uuid_unit);// 只需给菜单父节点赋值
			children.add(menu);
		}
		node_unit.setChildren(children);
		trs.add(node_unit);
		return trs;
	}
	// 产生树形数据WUZ 优化前方法
	/*@Override
	public List<BookMenu> GetBookTree(String zdOrZhBdcdyid,String bdcdylx) {
		List<BDCS_DJDY_LS_EX> alldidys = this.GetDJDY_EX_List(zdOrZhBdcdyid);// 本方法只有该处访问数据库
		List<BDCS_DJDY_LS_EX> didys =new ArrayList<BDCS_DJDY_LS_EX>(); 
		for (BDCS_DJDY_LS_EX tmp_djdy : alldidys) //过滤掉登记单元为空的项(暂时不过滤)
		{
			if(tmp_djdy.getDjdy()!=null)
				didys.add(tmp_djdy);
		}
		List<BookMenu> trs = new ArrayList<BookMenu>();
		// 加入基本菜单节点
		Map<String, String> attr0 = new HashMap<String, String>();// 封面
		attr0.put("url", DJBMenu.FM.getUrl());
		attr0.put("tpl", DJBMenu.FM.getPrintTemplate());
		attr0.put("page", String.valueOf(1));
		attr0.put("bdcdylx", bdcdylx);
		if (!StringUtils.isEmpty(zdOrZhBdcdyid))
			attr0.put("bdcdyid", zdOrZhBdcdyid);
		BookMenu m0 = CreateBookMenu("", "", DJBMenu.FM.getMenuName(), attr0);
		trs.add(m0);
		// 宗地基本信息
		Map<String, String> attr1 = new HashMap<String, String>();
		attr1.put("url", DJBMenu.ZDJBXX.getUrl());
		attr1.put("tpl", DJBMenu.ZDJBXX.getPrintTemplate());
		attr1.put("page", String.valueOf(1));
		attr1.put("bdcdylx", bdcdylx);
		if (!StringUtils.isEmpty(zdOrZhBdcdyid))
			attr1.put("bdcdyid", zdOrZhBdcdyid);
		BookMenu m1 = CreateBookMenu("", "", DJBMenu.ZDJBXX.getMenuName(),
				attr1);
		// 宗海基本信息
		Map<String, String> attr11 = new HashMap<String, String>();
		attr11.put("url", DJBMenu.ZHJBXX.getUrl());
		attr11.put("tpl", DJBMenu.ZHJBXX.getPrintTemplate());
		attr11.put("page", String.valueOf(1));
		attr11.put("bdcdylx", bdcdylx);
		if (!StringUtils.isEmpty(zdOrZhBdcdyid))
			attr11.put("bdcdyid", zdOrZhBdcdyid);
		BookMenu m11 = CreateBookMenu("", "", DJBMenu.ZHJBXX.getMenuName(),
				attr11);
		if (bdcdylx.equals("04"))// 宗海
			trs.add(m11);
		else
			trs.add(m1);
		// 不动产权利登记目录，有分页
		int count_ml = (int) Math.ceil(didys.size() / (double) 23);// 四舍五入提取分页书，一页23行
		String suffix_ml = new String();
		for (int j = 0; j < count_ml; j++) {
			Map<String, String> attr2 = new HashMap<String, String>();
			attr2.put("url", DJBMenu.BDCQLDJML.getUrl());
			attr2.put("tpl", DJBMenu.BDCQLDJML.getPrintTemplate());
			attr2.put("page", String.valueOf(j + 1));
			attr2.put("bdcdyid", zdOrZhBdcdyid);
			if (!StringUtils.isEmpty(bdcdylx))
				attr2.put("bdcdylx", bdcdylx);
			if (count_ml > 1)
				suffix_ml = DJBMenu.BDCQLDJML.getMenuName() + (j + 1);
			else
				suffix_ml = DJBMenu.BDCQLDJML.getMenuName();
			BookMenu m2 = CreateBookMenu("", "", suffix_ml, attr2);
			trs.add(m2);
		}
		// 不动产单元号节点信息。
		for (BDCS_DJDY_LS_EX djdy : didys) {
			String uuid_bdcdyh = java.util.UUID.randomUUID().toString();
			Map<String, String> attr_bdcdyh = new HashMap<String, String>();
			attr_bdcdyh.put("url", "#");
			BookMenu node_bdcdyh = CreateBookMenu("", uuid_bdcdyh,
					djdy.getUnit().getZL(), attr_bdcdyh);
			List<BookMenu> children = new ArrayList<BookMenu>();
			for (BookMenu menu : djdy.getMenus()) {
				menu.setParentid(uuid_bdcdyh);// 只需给菜单父节点赋值
				children.add(menu);
			}
			node_bdcdyh.setChildren(children);
			trs.add(node_bdcdyh);
		}
		return trs;
	}*/
	//-----------------------优化登记簿详情表改动方法---------------------------------------------------------------------------
	/**
	 * @author WUZ 根据宗地或宗海代码 获取关联的不动产单元基本信息(优化登记簿详情表加载，第一步先显示单元就是该方法)。
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	private Message GetBDCDYList(String zdOrZHbdcdyid,Map<String, String> mapCondition,long page,long rows) {
		StringBuilder  mysql=new StringBuilder();
		StringBuilder  formsql=new StringBuilder();
		StringBuilder where = new StringBuilder();
		if (!StringUtils.isEmpty(mapCondition.get("BDCDYH"))) {
			where.append(" AND  instr(BDCDYH,'"+mapCondition.get("BDCDYH")+"')>0");	
		}
		if (!StringUtils.isEmpty(mapCondition.get("ZL"))) {
			where.append(" AND  instr(ZL,'"+mapCondition.get("ZL")+"')>0");	
		}
		formsql.append(" FROM ( SELECT BDCDYH,BDCDYID,'031' AS BDCDYLX,ZL,ZRZH AS DH,FH AS MPH,DYH AS DYH FROM BDCK.BDCS_H_XZ WHERE ZDBDCDYID ='"+zdOrZHbdcdyid+"' ");
		formsql.append(where.toString());
		formsql.append(" UNION ALL ");
		formsql.append(" SELECT  BDCDYH,BDCDYID,'032' AS BDCDYLX,ZL,ZRZH AS DH,FH AS MPH,DYH AS DYH FROM BDCK.BDCS_H_XZY FY LEFT JOIN BDCK.YC_SC_H_XZ GL ON FY.BDCDYID =GL.YCBDCDYID WHERE ZDBDCDYID ='"+zdOrZHbdcdyid+"' AND GL.SCBDCDYID IS NULL ");
		formsql.append(where.toString()); 
		formsql.append(" UNION ALL ");
		formsql.append(" SELECT  BDCDYH,BDCDYID,'05' AS BDCDYLX,ZL,NULL AS DH,NULL AS MPH,NULL AS DYH FROM BDCK.BDCS_SLLM_XZ WHERE ZDBDCDYID ='"+zdOrZHbdcdyid+"' ");
		formsql.append(where.toString());
		formsql.append(" UNION ALL ");
		formsql.append(" SELECT  BDCDYH,BDCDYID,'01' AS BDCDYLX,ZL,NULL AS DH,NULL AS MPH,NULL AS DYH FROM  BDCK.BDCS_SYQZD_XZ WHERE BDCDYID='"+zdOrZHbdcdyid+"' ");
		formsql.append(where.toString());
		formsql.append(" UNION ALL ");
		formsql.append(" SELECT  BDCDYH,BDCDYID,'02' AS BDCDYLX,ZL,NULL AS DH,NULL AS MPH,NULL AS DYH FROM BDCK.BDCS_SHYQZD_XZ WHERE BDCDYID='"+zdOrZHbdcdyid+"' ");
		formsql.append(where.toString());
		formsql.append(" UNION ALL ");
		formsql.append(" SELECT  BDCDYH,BDCDYID,'04' AS BDCDYLX,ZL,NULL AS DH,NULL AS MPH,NULL AS DYH FROM BDCK.BDCS_ZH_XZ WHERE BDCDYID='"+zdOrZHbdcdyid+"' ");
		formsql.append(where.toString());
		formsql.append(" UNION ALL ");
		formsql.append(" SELECT  BDCDYH,BDCDYID,'09' AS BDCDYLX,ZL,NULL AS DH,NULL AS MPH,NULL AS DYH FROM BDCK.BDCS_NYD_XZ WHERE BDCDYID='"+zdOrZHbdcdyid+"' ");
		formsql.append(where.toString());
		formsql.append(" ) T ORDER BY BDCDYLX,BDCDYH,DH,MPH,DYH NULLS FIRST");
		mysql.append(" SELECT *  ");
		mysql.append(formsql.toString());
		long total=baseCommonDao.getCountByFullSql(formsql.toString());
		String pagingSql = String
				.format(" SELECT * FROM (SELECT A.*, ROWNUM RN FROM (%s) A WHERE ROWNUM < %s ) WHERE RN > %s ",
						mysql.toString(), page*rows + 1, (page-1)*rows);
		//List<Map> units = baseCommonDao.getPageDataByFullSql(mysql.toString(), (int)page, (int)rows);
		List<Map> units = baseCommonDao.getDataListByFullSql(pagingSql);
		Message msg = new Message();
		msg.setRows(units);
		msg.setTotal(total);
		return msg;
	}
	/* 
	 * 异步加载获取不动产单元的权利类型菜单（优化登记簿详情树形）
	 */
	@Override
	public List<BookMenu> GetBookChildrenByAsync(String parentid, String bdcdyid, String bdcdylx) {
		BDCS_DJDY_LS_EX djdy_ex = new BDCS_DJDY_LS_EX();
		djdy_ex.Init(baseCommonDao,bdcdyid,BDCDYLX.initFrom(bdcdylx));
		List<BookMenu> children = new ArrayList<BookMenu>();
		for (BookMenu menu : djdy_ex.getMenus()) {
			menu.setParentid(parentid);
			children.add(menu);
		}
		return children;
	}
	// 产生树形数据WUZ
		@Override
		public Message GetBookTree(String zdOrZhBdcdyid,String bdcdylx,Map<String, String> mapCondition, long page,long rows) {
			Message bdcdys = this.GetBDCDYList(zdOrZhBdcdyid,mapCondition,page,rows);// 本方法只有该处访问数据库
			List<BookMenu> trs = new ArrayList<BookMenu>();
			// 加入基本菜单节点
			Map<String, String> attr0 = new HashMap<String, String>();// 封面
			attr0.put("url", DJBMenu.FM.getUrl());
			attr0.put("tpl", DJBMenu.FM.getPrintTemplate());
			attr0.put("page", String.valueOf(1));
			attr0.put("bdcdylx", bdcdylx);
			if (!StringUtils.isEmpty(zdOrZhBdcdyid))
				attr0.put("bdcdyid", zdOrZhBdcdyid);
			BookMenu m0 = CreateBookMenu("", "", DJBMenu.FM.getMenuName(), attr0);
			trs.add(m0);
			if(bdcdylx.equals("01") || bdcdylx.equals("02")) {
				// 宗地基本信息
				Map<String, String> attr1 = new HashMap<String, String>();
				attr1.put("url", DJBMenu.ZDJBXX.getUrl());
				attr1.put("tpl", DJBMenu.ZDJBXX.getPrintTemplate());
				attr1.put("page", String.valueOf(1));
				attr1.put("bdcdylx", bdcdylx);
				if (!StringUtils.isEmpty(zdOrZhBdcdyid))
					attr1.put("bdcdyid", zdOrZhBdcdyid);
				BookMenu m1 = CreateBookMenu("", "", DJBMenu.ZDJBXX.getMenuName(),
						attr1);
				
				trs.add(m1);
			} else if (bdcdylx.equals("04")) {
				// 宗海基本信息
				Map<String, String> attr04 = new HashMap<String, String>();
				attr04.put("url", DJBMenu.ZHJBXX.getUrl());
				attr04.put("tpl", DJBMenu.ZHJBXX.getPrintTemplate());
				attr04.put("page", String.valueOf(1));
				attr04.put("bdcdylx", bdcdylx);
				if (!StringUtils.isEmpty(zdOrZhBdcdyid))
					attr04.put("bdcdyid", zdOrZhBdcdyid);
				BookMenu m04 = CreateBookMenu("", "", DJBMenu.ZHJBXX.getMenuName(),
						attr04);
				
				trs.add(m04);
			} else if (bdcdylx.equals("09")) {
				// 宗海基本信息
				Map<String, String> attr09 = new HashMap<String, String>();
				attr09.put("url", DJBMenu.NYDDJXX.getUrl());
				attr09.put("tpl", DJBMenu.NYDDJXX.getPrintTemplate());
				attr09.put("page", String.valueOf(1));
				attr09.put("bdcdylx", bdcdylx);
				if (!StringUtils.isEmpty(zdOrZhBdcdyid))
					attr09.put("bdcdyid", zdOrZhBdcdyid);
				BookMenu m09 = CreateBookMenu("", "", DJBMenu.NYDDJXX.getMenuName(),
						attr09);
				trs.add(m09);
			}
			
			// 不动产权利登记目录，有分页
			int count_ml = (int) Math.ceil(bdcdys.getTotal() / (double) 23);// 四舍五入提取分页书，一页23行
			String suffix_ml = new String();
			for (int j = 0; j < count_ml; j++) {
				Map<String, String> attr2 = new HashMap<String, String>();
				attr2.put("url", DJBMenu.BDCQLDJML.getUrl());
				attr2.put("tpl", DJBMenu.BDCQLDJML.getPrintTemplate());
				attr2.put("page", String.valueOf(j + 1));
				attr2.put("bdcdyid", zdOrZhBdcdyid);
				if (!StringUtils.isEmpty(bdcdylx))
					attr2.put("bdcdylx", bdcdylx);
				if (count_ml > 1)
					suffix_ml = DJBMenu.BDCQLDJML.getMenuName() + (j + 1);
				else
					suffix_ml = DJBMenu.BDCQLDJML.getMenuName();
				BookMenu m2 = CreateBookMenu("", "", suffix_ml, attr2);
				trs.add(m2);
			}
			// 不动产单元号节点信息。
			for (Object bdcdy : bdcdys.getRows()) {
				String uuid_bdcdyh = java.util.UUID.randomUUID().toString();
				Map<String, String> attr_bdcdyh = new HashMap<String, String>();
				String dy_bdcdylx =String.valueOf(((Map)bdcdy).get("BDCDYLX"));
				String dy_bdcdyid =String.valueOf(((Map)bdcdy).get("BDCDYID"));
				if (BDCDYLX.YCH.Value.equals(dy_bdcdylx)) {
					String sql_qzgl =" YCBDCDYID='"+ dy_bdcdyid +"'";
					List<YC_SC_H_XZ> GL= baseCommonDao.getDataList(YC_SC_H_XZ.class, sql_qzgl);
					if (GL != null && GL.size() > 0) {//已关联现房
						continue;
					}
				}
				attr_bdcdyh.put("url", "#");
				attr_bdcdyh.put("bdcdylx", dy_bdcdylx);
				attr_bdcdyh.put("bdcdyid", dy_bdcdyid);
				attr_bdcdyh.put("parentid",uuid_bdcdyh);
				attr_bdcdyh.put("bdcdyh",String.valueOf(((Map)bdcdy).get("BDCDYH")));
				BookMenu node_bdcdyh = CreateBookMenu("", uuid_bdcdyh,
						String.valueOf(((Map)bdcdy).get("ZL")), attr_bdcdyh);
				node_bdcdyh.setState(State.closed);
				trs.add(node_bdcdyh);
			}
			Message msg = new Message();
			msg.setRows(trs);
			msg.setTotal(bdcdys.getTotal());
			return msg;
		}

	// 创建树形节点WUZ
	private BookMenu CreateBookMenu(String parentid, String id, String text,
			Map<String, String> attr) {
		BookMenu node = new BookMenu();
		node.setText(text);
		if (!StringUtils.isEmpty(parentid))
			node.setParentid(parentid);
		if (!StringUtils.isEmpty(id))
			node.setId(id);
		else
			node.setId(java.util.UUID.randomUUID().toString());
		if (attr != null && !attr.isEmpty())
			node.setAttributes(attr);
		return node;
	}

	/**
	 * 根据不动产单元ID获取宗地信息
	 * 
	 * @作者：WUZHU
	 * @param bdcdyid
	 *            不动产单元ID
	 * @return
	 */
	public Map GetZDXX(String bdcdyid) {
		BDCDYLX bdcdylx = ValidateBDCDYLX(bdcdyid);
		if (!(BDCDYLX.SHYQZD.equals(bdcdylx) || BDCDYLX.SYQZD.equals(bdcdylx)))// 只能所有权宗地和使用权宗地
			return null;
		Unit_EX unit = new Unit_EX();
		unit.Init(baseCommonDao, bdcdylx, bdcdyid, null, null, 1, 2);
		Map<String, Object> zdMap = new HashMap<String, Object>();
		zdMap.put(PRE_UNIT + "BDCDYLX", bdcdylx.Value);
		// 来自权利表
		if (unit.getQls() != null && unit.getQls().size() > 0) {
			zdMap.put(PRE_QL + "DJSJ", unit.getQls().get(0).getQl().get("DJSJ"));
			zdMap.put(PRE_QL + "DBR", unit.getQls().get(0).getQl().get("DBR"));
			zdMap.put(PRE_QL + "FJ", unit.getQls().get(0).getQl().get("FJ"));
		} else {
			zdMap.put(PRE_QL + "DJSJ", "");
			zdMap.put(PRE_QL + "DBR", "");
			zdMap.put(PRE_QL + "FJ", "");
		}
		// 来自不动产单元表
		zdMap.put(PRE_UNIT + "BDCDYID", bdcdyid);
		zdMap.put(PRE_UNIT + "MJDW", unit.getUnit().get("MJDW"));
		zdMap.put(PRE_UNIT + "ZL", unit.getUnit().get("ZL"));
		zdMap.put(PRE_UNIT + "ZDMJ", unit.getUnit().get("ZDMJ"));
		zdMap.put(PRE_UNIT + "DJ", unit.getUnit().get("DJ"));
		zdMap.put(PRE_UNIT + "QLLX", unit.getUnit().get("QLLX"));
		zdMap.put(PRE_UNIT + "QLSDFS", unit.getUnit().get("QLSDFS"));
		zdMap.put(PRE_UNIT + "JZMD", unit.getUnit().get("JZMD"));
		zdMap.put(PRE_UNIT + "YT", unit.getUnit().get("YT"));
		zdMap.put(PRE_UNIT + "JG", unit.getUnit().get("JG"));
		zdMap.put(PRE_UNIT + "JZXG", unit.getUnit().get("JZXG"));
		zdMap.put(PRE_UNIT + "ZDSZD", unit.getUnit().get("ZDSZD"));
		zdMap.put(PRE_UNIT + "ZDSZX", unit.getUnit().get("ZDSZX"));
		zdMap.put(PRE_UNIT + "ZDSZN", unit.getUnit().get("ZDSZN"));
		zdMap.put(PRE_UNIT + "ZDSZB", unit.getUnit().get("ZDSZB"));
		zdMap.put(PRE_UNIT + "QLXZ", unit.getUnit().get("QLXZ"));
		zdMap.put(PRE_UNIT + "RJL", unit.getUnit().get("RJL"));
		zdMap.put(PRE_UNIT +"FJ", unit.getUnit().get("FJ"));
		List<BDCS_ZDBHQK_LS> zdbhqks = baseCommonDao.getDataList(
				BDCS_ZDBHQK_LS.class, " BDCDYID='" + bdcdyid + "' ");
		int index = 1;
		for (BDCS_ZDBHQK_LS zdbhqk : zdbhqks) {
			zdMap.put("ZDBHQK_" + "BHYH" + "_" + index,
					unit.getUnit().get("BHYH"));
			zdMap.put("ZDBHQK_" + "BHNR" + "_" + index,
					unit.getUnit().get("BHNR"));
			zdMap.put("ZDBHQK_" + "DJSJ" + "_" + index,
					unit.getUnit().get("DJSJ"));
			zdMap.put("ZDBHQK_" + "DBR" + "_" + index, unit.getUnit()
					.get("DBR"));
			index += 1;
		}
		return zdMap;
	}

	/**
	 * 根据不动产单元ID获取宗海信息
	 * 
	 * @作者：WUZHU
	 * @param bdcdyid
	 *            不动产单元ID
	 * @return
	 */
	public Map GetZHXX(String bdcdyid) {
		BDCDYLX bdcdylx = ValidateBDCDYLX(bdcdyid);
		Unit_EX unit = new Unit_EX();
		unit.Init(baseCommonDao, BDCDYLX.HY, bdcdyid, null, null, 1, 2);
		Map<String, Object> zhMap = new HashMap<String, Object>();
		BDCS_XMXX xmxx = baseCommonDao.get(BDCS_XMXX.class,
				String.valueOf(unit.getDidy().get("XMBH")));
		// 来自项目表
		zhMap.put("XMXX_" + "XMMC", xmxx != null ? xmxx.getXMMC() : "");
		zhMap.put(PRE_UNIT + "BDCDYLX", bdcdylx.Value);
		// 来自权利表
		if (unit.getQls() != null && unit.getQls().size() > 0) {
			zhMap.put(PRE_QL + "DJSJ", unit.getQls().get(0).getQl().get("DJSJ"));
			zhMap.put(PRE_QL + "DBR", unit.getQls().get(0).getQl().get("DBR"));
			zhMap.put(PRE_QL + "FJ", unit.getQls().get(0).getQl().get("FJ"));
		} else {
			zhMap.put(PRE_QL + "DJSJ", "");
			zhMap.put(PRE_QL + "DBR", "");
			zhMap.put(PRE_QL + "FJ", "");
		}
		// 来自不动产单元表
		zhMap.put(PRE_UNIT + "BDCDYID", bdcdyid);
		zhMap.put(PRE_UNIT + "XMXX", unit.getUnit().get("XMXX"));
		zhMap.put(PRE_UNIT + "MJDW", unit.getUnit().get("MJDW"));
		zhMap.put(PRE_UNIT + "YHZMJ", unit.getUnit().get("YHZMJ"));
		zhMap.put(PRE_UNIT + "ZHMJ", unit.getUnit().get("ZHMJ"));
		zhMap.put(PRE_UNIT + "DB", unit.getUnit().get("DB"));
		zhMap.put(PRE_UNIT + "ZHAX", unit.getUnit().get("ZHAX"));
		zhMap.put(PRE_UNIT + "YHLXA", unit.getUnit().get("YHLXA"));
		zhMap.put(PRE_UNIT + "YHLXB", unit.getUnit().get("YHLXB"));
		zhMap.put(PRE_UNIT + "YHWZSM", unit.getUnit().get("YHWZSM"));
		zhMap.put(PRE_UNIT + "HDMC", unit.getUnit().get("HDMC"));
		zhMap.put(PRE_UNIT + "HDDM", unit.getUnit().get("HDDM"));
		zhMap.put(PRE_UNIT + "YDFW", unit.getUnit().get("YDFW"));
		zhMap.put(PRE_UNIT + "YDMJ", unit.getUnit().get("YDMJ"));
		zhMap.put(PRE_UNIT + "HDWZ", unit.getUnit().get("HDWZ"));
		zhMap.put(PRE_UNIT + "HDYT", unit.getUnit().get("HDYT"));

		List<BDCS_YHZK_LS> yhzks = baseCommonDao.getDataList(
				BDCS_YHZK_LS.class, " BDCDYID='" + bdcdyid + "' ");
		int index = 1;
		for (BDCS_YHZK_LS yhzk : yhzks) {
			// 来自用海状况表
			zhMap.put("YHZK_" + "YHFS" + "_" + index,ConstHelper.getNameByValue("YHFS", StringHelper.formatObject(yhzk.getYHFS())));
			zhMap.put("YHZK_" + "YHMJ" + "_" + index, yhzk.getYHMJ());
			zhMap.put("YHZK_" + "JTYT" + "_" + index, yhzk.getJTYT());
			zhMap.put("YHZK_" + "SYJSE" + "_" + index, yhzk.getSYJSE());
			index += 1;
		}
		List<BDCS_YHYDZB_LS> yhydzbs = baseCommonDao.getDataList(
				BDCS_YHYDZB_LS.class, " BDCDYID='" + bdcdyid + "' ");
		int index2 = 1;
		for (BDCS_YHYDZB_LS yhydzb : yhydzbs) {
			// 来自用海坐标表
			zhMap.put("YHYDZB_" + "XH" + "_" + index2, index2);
			zhMap.put("YHYDZB_" + "BHYH" + "_" + index2, yhydzb.getBHYH());
			zhMap.put("YHYDZB_" + "BHNR" + "_" + index2, yhydzb.getBHNR());
			index2 += 1;
		}
		// @宗海变化情况表还没有实体类
		return zhMap;
	}

	/**
	 * 根据不动产单元ID和不动产单元类型获取首页信息
	 * 
	 * @作者：WUZHU
	 * @param bdcdyid
	 *            不动产单元ID
	 * @param bdcdylx
	 *            不动产单元类型
	 * @return
	 */
	public Map GetFMInfo(String bdcdyid, String bdcdylx) {
		Unit_EX unit = new Unit_EX();
		unit.Init(baseCommonDao, ConstValue.BDCDYLX.initFrom(bdcdylx), bdcdyid,
				null, null, 1, 2);
		Map<String, Object> fmMap = new HashMap<String, Object>();
		if (BDCDYLX.initFrom(bdcdylx).equals(BDCDYLX.SHYQZD)
				|| BDCDYLX.initFrom(bdcdylx).equals(BDCDYLX.SYQZD))// 所有权和使用权情况
		{
			fmMap.put("BDCDYID", bdcdyid);
			fmMap.put(
					"SHENGMC",
					ConstHelper.getNameByValue("XZQH",
							String.valueOf(unit.getUnit().get("QXDM"))
									.substring(0, 2) + "0000"));
			fmMap.put(
					"SHIMC",
					ConstHelper.getNameByValue("XZQH",
							String.valueOf(unit.getUnit().get("QXDM"))
									.substring(0, 4) + "00"));
			fmMap.put(
					"QXMC",
					ConstHelper.getNameByValue("XZQH",
							String.valueOf(unit.getUnit().get("QXDM"))));
			fmMap.put(
					"DJQMC",
					ConstHelper.getNameByValue("XZQH",
							String.valueOf(unit.getUnit().get("DJQDM"))));
			fmMap.put(
					"DJZQMC",
					ConstHelper.getNameByValue("XZQH",
							String.valueOf(unit.getUnit().get("DJZQDM"))));
			fmMap.put(
					"ZMC",
					ConstHelper.getNameByValue("XZQH",
							String.valueOf(unit.getUnit().get("ZH"))));
			fmMap.put("ZDZHDM", unit.getUnit().get("ZDDM"));
			if (unit.getQls() != null && unit.getQls().size() > 0)
				fmMap.put("DJJG", unit.getQls().get(0).getQl().get("DJJG"));
			else
				fmMap.put("DJJG", "");
			return fmMap;
		}
		if (BDCDYLX.initFrom(bdcdylx).equals(BDCDYLX.HY))// 海域情况
		{
			fmMap.put("BDCDYID", bdcdyid);
			fmMap.put(
					"SHENGMC",
					ConstHelper.getNameByValue("XZQH",
							String.valueOf(unit.getUnit().get("QXDM"))
									.substring(0, 2) + "0000"));
			fmMap.put(
					"SHIMC",
					ConstHelper.getNameByValue("XZQH",
							String.valueOf(unit.getUnit().get("QXDM"))
									.substring(0, 4) + "00"));
			fmMap.put(
					"QXMC",
					ConstHelper.getNameByValue("XZQH",
							String.valueOf(unit.getUnit().get("QXDM"))));
			fmMap.put("DJQMC", "");
			fmMap.put("DJZQMC", "");
			fmMap.put("ZMC", "");
			fmMap.put("ZDZHDM", unit.getUnit().get("ZHDM"));
			if (unit.getQls() != null && unit.getQls().size() > 0)
				fmMap.put("DJJG", unit.getQls().get(0).getQl().get("DJJG"));
			else
				fmMap.put("DJJG", "");
			return fmMap;
		}
		if (BDCDYLX.initFrom(bdcdylx).equals(BDCDYLX.NYD))// 海域情况
		{
			fmMap.put("BDCDYID", bdcdyid);
			fmMap.put(
					"SHENGMC",
					ConstHelper.getNameByValue("XZQH",
							String.valueOf(unit.getUnit().get("QXDM"))
							.substring(0, 2) + "0000"));
			fmMap.put(
					"SHIMC",
					ConstHelper.getNameByValue("XZQH",
							String.valueOf(unit.getUnit().get("QXDM"))
							.substring(0, 4) + "00"));
			fmMap.put(
					"QXMC",
					ConstHelper.getNameByValue("XZQH",
							String.valueOf(unit.getUnit().get("QXDM"))));
			fmMap.put("DJQMC", unit.getUnit().get("DJQMC"));
			fmMap.put("DJZQMC", unit.getUnit().get("DJZQMC"));
			fmMap.put("ZMC", "");
			fmMap.put("ZDZHDM", unit.getUnit().get("ZDDM"));
			if (unit.getQls() != null && unit.getQls().size() > 0)
				fmMap.put("DJJG", unit.getQls().get(0).getQl().get("DJJG"));
			else
				fmMap.put("DJJG", "");
			return fmMap;
		}
		
		fmMap.put("BDCDYID", "");
		fmMap.put("SHENGMC", "");
		fmMap.put("SHIMC", "");
		fmMap.put("QXMC", "");
		fmMap.put("DJQMC", "");
		fmMap.put("DJZQMC", "");
		fmMap.put("ZMC", "");
		fmMap.put("ZDZHDM", "");
		fmMap.put("DJJG", "");
		return fmMap;
	}

	/**
	 * 根据不动产单元ID和权利类型或登记类型获取权利信息
	 * 
	 * @作者：WUZHU
	 * @param bdcdyid
	 *            不动产单元ID
	 * @param lx
	 *            权利类型或登记类型
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map GetQLInfoList(String bdcdyid, String bdcdylx, String qllxOrDjlx, 
			long page, long pagesize) {
		Unit_EX unit = new Unit_EX();
		List<QLLX> qllxs = new ArrayList<QLLX>();

		if (ConstValue.QLLX.GYJSYDSHYQ.Value.equals(qllxOrDjlx))// 国有建设用地使用权
		{
			qllxs.clear();
			qllxs.add(QLLX.GYJSYDSHYQ);
			qllxs.add(QLLX.ZJDSYQ);
			qllxs.add(QLLX.JTJSYDSYQ);
			ConstValue.BDCDYLX bdcdylxEum = ConstValue.BDCDYLX
					.initFrom(bdcdylx);
			unit.Init(baseCommonDao, bdcdylxEum, bdcdyid, null, qllxs, page,
					pagesize);
			Map<String, Object> gyjsydshyqMap = new HashMap<String, Object>();
			int index = 1;

			for (QL_EX qlex : unit.getQls()) {
				gyjsydshyqMap.put(PRE_UNIT + "BDCDYH",
						unit.getUnit().get("BDCDYH"));
				// 来自于不动产单元
				gyjsydshyqMap.put(PRE_UNIT + "FDZL",
						unit.getUnit().get("ZL"));
				String ywh="";
				if(qlex.getQl().get("YWH") != null){
					ywh=StringHelper.formatObject(qlex.getQl().get("YWH"));
				}
				try{
					if(!StringHelper.isEmpty(ywh)){
						BDCS_XMXX XMXX=Global.getXMXX(ywh);
						if(XMXX!=null){
							ywh=XMXX.getYWLSH();
							if(StringHelper.isEmpty(ywh)){
								ywh=StringHelper.formatObject(qlex.getQl().get("YWH"));
							}
						}else{
							 
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				// 来自权利表
				gyjsydshyqMap.put(PRE_QL + "YWH" + "_" + index, ywh);
				gyjsydshyqMap.put(PRE_QL + "DJLX" + "_" + index, qlex.getQl()
						.get("DJLX"));
				gyjsydshyqMap.put(PRE_QL + "DJYY" + "_" + index, qlex.getQl()
						.get("DJYY"));
				gyjsydshyqMap.put(PRE_QL + "QLJSSJ" + "_" + index, qlex.getQl()
						.get("QLJSSJ"));
				gyjsydshyqMap.put(PRE_QL + "QLQSSJ" + "_" + index, qlex.getQl()
						.get("QLQSSJ"));
				if(!StringHelper.isEmpty(bdcdyid)){
					BDCS_TDYT_LS tdyt=null;
					List<BDCS_TDYT_LS> tdyts=baseCommonDao.getDataList(BDCS_TDYT_LS.class, "BDCDYID='"+bdcdyid+"'");
					if(tdyts!=null&&tdyts.size()>0){
						for(BDCS_TDYT_LS yt:tdyts){
							if(tdyt==null){
								tdyt=yt;
							}
							if("1".equals(yt.getSFZYT())){
								tdyt=yt;
								break;
							}
						}
					}
					if(tdyt!=null){
						gyjsydshyqMap.put(PRE_QL + "QLJSSJ" + "_" + index, StringHelper.FormatDateOnType(tdyt.getZZRQ(), "yyyy年MM月dd日"));
						gyjsydshyqMap.put(PRE_QL + "QLQSSJ" + "_" + index, StringHelper.FormatDateOnType(tdyt.getQSRQ(), "yyyy年MM月dd日"));
					}
				}
				gyjsydshyqMap.put(PRE_QL + "BDCQZH" + "_" + index, qlex.getQl()
						.get("BDCQZH"));
				gyjsydshyqMap.put(PRE_QL + "QDJG" + "_" + index, qlex.getQl()
						.get("QDJG"));
				gyjsydshyqMap.put(PRE_QL + "DBR" + "_" + index, qlex.getQl()
						.get("DBR"));
				gyjsydshyqMap.put(PRE_QL + "FJ" + "_" + index, qlex.getQl()
						.get("FJ"));
				gyjsydshyqMap.put(PRE_QL + "DJSJ" + "_" + index, qlex.getQl()
						.get("DJSJ"));
				gyjsydshyqMap.put(PRE_QL + "GYRQK" + "_" + index, qlex.getQl()
						.get("GYRQK"));
				// 来自附属权利表
				gyjsydshyqMap.put(PRE_FSQL + "SFZX" + "_" + index, qlex
						.getFsql().get("SFZX"));
			//	gyjsydshyqMap.put(PRE_FSQL + "SYQMJ" + "_" + index, unit.getUnit().get("ZDMJ"));
				//添加注销相关信息
				gyjsydshyqMap.put(PRE_FSQL + "ZXSJ" + "_" + index, qlex
						.getFsql().get("ZXSJ"));
				gyjsydshyqMap.put(PRE_FSQL + "ZXDYYY" + "_" + index, qlex
						.getFsql().get("ZXDYYY"));
				gyjsydshyqMap.put(PRE_FSQL + "ZXDBR" + "_" + index, qlex
						.getFsql().get("ZXDBR"));
				// 来自权利人表
				gyjsydshyqMap.put(PRE_QLR + "GYQK" + "_" + index, qlex.getQlr()
						.get("GYQK"));
				gyjsydshyqMap.put(PRE_QLR + "ZJH" + "_" + index, qlex.getQlr()
						.get("ZJH"));
				gyjsydshyqMap.put(PRE_QLR + "QLR" + "_" + index, qlex.getQlr()
						.get("QLRMC"));
				gyjsydshyqMap.put(PRE_QLR + "ZJZL" + "_" + index, qlex.getQlr()
						.get("ZJZL"));
				gyjsydshyqMap.put(PRE_QLR + "QLRLX" + "_" + index, qlex
						.getQlr().get("QLRLX"));
				 String zdmj_sql=" select distinct zd.xmbh,zd.zdmj,zd.bdcdyid from bdck.BDCS_SHYQZD_ls zd "
				 		+ " left join  bdck.BDCS_QL_ls ql on ql.bdcdyh= zd.bdcdyh  where ql.bdcdyh='"+qlex.getBDCDYH()+"' ";
				 List<Map> zdmj=null;
				if (!StringHelper.isEmpty(qlex.getXMBH())) {
					zdmj_sql += " and zd.xmbh ='" + qlex.getXMBH() + "' ";
				} else {
					zdmj_sql += " and zd.xmbh is null ";
				}
				zdmj=baseCommonDao.getDataListByFullSql(zdmj_sql);
				gyjsydshyqMap.put(PRE_FSQL + "SYQMJ" + "_" + index, zdmj.get(0).get("ZDMJ"));
				
				index += 1;
			}
			return gyjsydshyqMap;
		}
		if (ConstValue.QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(qllxOrDjlx))// 国有建设用地使用权/房屋（构筑物）所有权
		{
			qllxs.clear();
			qllxs.add(QLLX.GYJSYDSHYQ_FWSYQ);
			qllxs.add(QLLX.ZJDSYQ_FWSYQ);
			qllxs.add(QLLX.JTJSYDSYQ_FWSYQ);
			ConstValue.BDCDYLX bdcdylxEum = ConstValue.BDCDYLX
					.initFrom(bdcdylx);
			unit.Init(baseCommonDao, bdcdylxEum, bdcdyid, null, qllxs, page,
					pagesize);
			Map<String, Object> fwsyqMap = new HashMap<String, Object>();
			int index = 1;

			for (QL_EX qlex : unit.getQls()) {
				String ywh="";
				if(qlex.getQl().get("YWH") != null){
					ywh=StringHelper.formatObject(qlex.getQl().get("YWH"));
				}
				try{
					if(!StringHelper.isEmpty(ywh)){
						BDCS_XMXX XMXX=Global.getXMXX(ywh);
						if(XMXX!=null){
							ywh=XMXX.getYWLSH();
							if(StringHelper.isEmpty(ywh)){
								ywh=StringHelper.formatObject(qlex.getQl().get("YWH"));
							}
						}else{
							 
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				// 来自权利表
				fwsyqMap.put(PRE_QL + "YWH" + "_" + index,ywh);
				fwsyqMap.put(PRE_QL + "DJLX" + "_" + index,
						qlex.getQl().get("DJLX"));
				fwsyqMap.put(PRE_QL + "DJYY" + "_" + index,
						qlex.getQl().get("DJYY"));
				if (!StringHelper.isEmpty(qlex.getQl().get("QLQSSJ"))) {
					fwsyqMap.put(PRE_QL + "QLQSSJ" + "_" + index, qlex.getQl().get("QLQSSJ"));
				}else {
					fwsyqMap.put(PRE_QL + "QLQSSJ" + "_" + index, unit.getUnit().get("TDSYQQSRQ"));
				}
				
				if (!StringHelper.isEmpty(qlex.getQl().get("QLJSSJ"))) {
					fwsyqMap.put(PRE_QL + "QLJSSJ" + "_" + index, qlex.getQl().get("QLJSSJ"));
				}else {
					fwsyqMap.put(PRE_QL + "QLJSSJ" + "_" + index, unit.getUnit().get("TDSYQZZRQ"));
				}
				
				fwsyqMap.put(PRE_QL + "BDCQZH" + "_" + index,
						qlex.getQl().get("BDCQZH"));
				fwsyqMap.put(PRE_QL + "DJSJ" + "_" + index,
						qlex.getQl().get("DJSJ"));
				fwsyqMap.put(PRE_QL + "DBR" + "_" + index,
						qlex.getQl().get("DBR"));
				fwsyqMap.put(PRE_QL + "FJ" + "_" + index, qlex.getQl()
						.get("FJ"));
				String qlid=StringHelper.FormatByDatatype(qlex.getQl().get("id"));
				if(!StringHelper.isEmpty(qlid)){
					HashMap<String, RealUnit> map =	HistoryBackdateTools.QueryUnitInfoByQlid(qlid);
					if(map !=null && map.size()>0){
						if(map.containsKey(BDCDYLX.H.Value)){
							for(Entry<String, RealUnit> entry :map.entrySet()){
								if(BDCDYLX.H.Value.equals(entry.getKey())){
									RealUnit houseunit=entry.getValue();
									if(houseunit != null){
										fwsyqMap.put(PRE_UNIT + "BDCDYH", houseunit.getBDCDYH());
										fwsyqMap.put(PRE_UNIT + "FDZL", houseunit.getZL());
									
									//看现状层是否有该权利信息  2016-11-30 luml
									//	qlex.Init(baseCommonDao);
									
									//	BDCS_QL_XZ _ql_xz = baseCommonDao.get(BDCS_QL_XZ.class,qlex.getId());
											if("该权利已注销".equals(qlex.getFsql().get("SFZX"))){
												fwsyqMap.put(PRE_UNIT + "TDSYQR" + "_" + index,  HistoryBackdateTools.getFieldValueByName("TDSYQR", houseunit));
											}else{
												fwsyqMap.put(PRE_UNIT + "TDSYQR" + "_" + index, qlex.getQl().get("TDSHYQR"));
											}
//										fwsyqMap.put(PRE_UNIT + "DYTDMJ" + "_" + index, houseunit.getd
//												.get("DYTDMJ"));
										fwsyqMap.put(PRE_UNIT + "FTTDMJ" + "_" + index,  HistoryBackdateTools.getFieldValueByName("FTTDMJ", houseunit));
										/*fwsyqMap.put(PRE_UNIT + "FDCJYJG" + "_" + index, unit.getUnit()
												.get("FDCJYJG"));*/
										fwsyqMap.put(PRE_UNIT + "FDCJYJG" + "_" + index, qlex.getQl().get("QDJG"));//房地产交易价格是在权利信息页面填写的，获取应该是来自权利表吧
										
										Object objghyt=HistoryBackdateTools.getFieldValueByName("GHYT", houseunit);
										String ghyt=ConstHelper.getNameByValue("FWYT", StringHelper.FormatByDatatype(objghyt));
										fwsyqMap.put(PRE_UNIT + "GHYT" + "_" + index, ghyt);
										Object objfwxz=HistoryBackdateTools.getFieldValueByName("FWXZ", houseunit);
										String fwxz=ConstHelper.getNameByValue("FWXZ", StringHelper.FormatByDatatype(objfwxz));
										fwsyqMap.put(PRE_UNIT + "FWXZ" + "_" + index, fwxz);
										Object objfwjg=HistoryBackdateTools.getFieldValueByName("FWJG", houseunit);
										String fwjg=ConstHelper.getNameByValue("FWJG", StringHelper.FormatByDatatype(objfwjg));
										fwsyqMap.put(PRE_UNIT + "FWJG" + "_" + index, fwjg);
										Object objscjzmj=HistoryBackdateTools.getFieldValueByName("SCJZMJ", houseunit);
										fwsyqMap.put(PRE_UNIT + "SCJZMJ" + "_" + index, StringHelper.formatDouble(objscjzmj));
										Object objsctnjzmj=HistoryBackdateTools.getFieldValueByName("SCTNJZMJ", houseunit);
										fwsyqMap.put(PRE_UNIT + "SCTNJZMJ" + "_" + index, StringHelper.formatDouble(objsctnjzmj));
										fwsyqMap.put(PRE_UNIT + "SCJZMJ" + "_" + index, StringHelper.formatDouble(objscjzmj));
										Object objscftjzmj=HistoryBackdateTools.getFieldValueByName("SCFTJZMJ", houseunit);
										fwsyqMap.put(PRE_UNIT + "SCFTJZMJ" + "_" + index, StringHelper.formatDouble(objscftjzmj));
										Object objszc=HistoryBackdateTools.getFieldValueByName("SZC", houseunit);
										Object objzzcs=HistoryBackdateTools.getFieldValueByName("ZCS", houseunit);
										fwsyqMap.put(
												PRE_UNIT + "SZC" + "_" + index,
												StringHelper
														.FormatByDatatype(objszc)
														+ "/"
														+ StringHelper.FormatByDatatype(objzzcs));
										Object objjzmj=HistoryBackdateTools.getFieldValueByName("JZMJ", houseunit);
										fwsyqMap.put(PRE_UNIT + "JZMJ" + "_" + index, objjzmj);
										Object objZYJZMJ=HistoryBackdateTools.getFieldValueByName("ZYJZMJ", houseunit);
										fwsyqMap.put(PRE_UNIT + "ZYJZMJ" + "_" + index, objZYJZMJ);
										Object objFTJZMJ=HistoryBackdateTools.getFieldValueByName("FTJZMJ", houseunit);
										fwsyqMap.put(PRE_UNIT + "FTJZMJ" + "_" + index, objFTJZMJ);
										Object objJGSJ=StringHelper.FormatByDatetime(HistoryBackdateTools.getFieldValueByName("JGSJ", houseunit));
										fwsyqMap.put(PRE_UNIT + "JGSJ" + "_" + index, objJGSJ);
									break;
									}
								}
							}
						}
					
				}else{
					// 来自于不动产单元
					fwsyqMap.put(PRE_UNIT + "BDCDYH", unit.getUnit().get("BDCDYH"));
					fwsyqMap.put(PRE_UNIT + "FDZL", unit.getUnit().get("ZL"));
					//看现状层是否有该权利信息  2016-11-30 luml
					//	qlex.Init(baseCommonDao);
					//	BDCS_QL_XZ _ql_xz = baseCommonDao.get(BDCS_QL_XZ.class,qlex.getId());
							if("该权利已注销".equals(qlex.getFsql().get("SFZX"))){
								fwsyqMap.put(PRE_UNIT + "TDSYQR" + "_" + index, unit.getUnit()
										.get("TDSYQR"));
							}else{
								fwsyqMap.put(PRE_UNIT + "TDSYQR" + "_" + index, qlex.getQl().get("TDSHYQR"));
							}
						fwsyqMap.put(PRE_UNIT + "DYTDMJ" + "_" + index, unit.getUnit()
								.get("DYTDMJ"));
						fwsyqMap.put(PRE_UNIT + "FTTDMJ" + "_" + index, unit.getUnit()
								.get("FTTDMJ"));
						/*fwsyqMap.put(PRE_UNIT + "FDCJYJG" + "_" + index, unit.getUnit()
								.get("FDCJYJG"));*/
						fwsyqMap.put(PRE_UNIT + "FDCJYJG" + "_" + index, qlex.getQl().get("QDJG"));//房地产交易价格是在权利信息页面填写的，获取应该是来自权利表吧
						fwsyqMap.put(PRE_UNIT + "GHYT" + "_" + index, unit.getUnit()
								.get("GHYTName"));
						fwsyqMap.put(PRE_UNIT + "FWXZ" + "_" + index, unit.getUnit()
								.get("FWXZName"));
						fwsyqMap.put(PRE_UNIT + "FWJG" + "_" + index, unit.getUnit()
								.get("FWJG"));
						fwsyqMap.put(PRE_UNIT + "SCJZMJ" + "_" + index, unit.getUnit()
								.get("SCJZMJ"));
						fwsyqMap.put(PRE_UNIT + "SCTNJZMJ" + "_" + index, unit.getUnit()
								.get("SCTNJZMJ"));
						fwsyqMap.put(PRE_UNIT + "SCFTJZMJ" + "_" + index, unit.getUnit()
								.get("SCFTJZMJ"));
						fwsyqMap.put(
								PRE_UNIT + "SZC" + "_" + index,
								StringHelper
										.FormatByDatatype(unit.getUnit().get("SZC"))
										+ "/"
										+ StringHelper.FormatByDatatype(unit.getUnit()
												.get("ZCS")));
						fwsyqMap.put(PRE_UNIT + "JZMJ" + "_" + index, unit.getUnit()
								.get("JZMJ"));
						fwsyqMap.put(PRE_UNIT + "ZYJZMJ" + "_" + index, unit.getUnit()
								.get("ZYJZMJ"));
						fwsyqMap.put(PRE_UNIT + "FTJZMJ" + "_" + index, unit.getUnit()
								.get("FTJZMJ"));
						fwsyqMap.put(PRE_UNIT + "JGSJ" + "_" + index, unit.getUnit()
								.get("JGSJ"));
					}
				}		
				// 来自于权利人表
				fwsyqMap.put(PRE_QLR + "ZJH" + "_" + index,
						qlex.getQlr().get("ZJH"));
				fwsyqMap.put(PRE_QLR + "ZJZL" + "_" + index,
						qlex.getQlr().get("ZJZL"));
				fwsyqMap.put(PRE_QLR + "GYQK" + "_" + index,
						qlex.getQlr().get("GYQK"));
				fwsyqMap.put(PRE_QLR + "QLRLX" + "_" + index, qlex.getQlr()
						.get("QLRLX"));
				fwsyqMap.put(PRE_QLR + "QLR" + "_" + index,
						qlex.getQlr().get("QLRMC"));
				
				//fwsyqMap.put(PRE_UNIT + "TDSYQR" + "_" + index, qlex.getQlr().get("QLRMC"));
				//该权利是否注销
				fwsyqMap.put(PRE_FSQL + "SFZX" + "_" + index, qlex
						.getFsql().get("SFZX"));
				//添加注销相关信息
				fwsyqMap.put(PRE_FSQL + "ZXSJ" + "_" + index, qlex
						.getFsql().get("ZXSJ"));
				fwsyqMap.put(PRE_FSQL + "ZXDYYY" + "_" + index, qlex
						.getFsql().get("ZXDYYY"));
				fwsyqMap.put(PRE_FSQL + "ZXDBR" + "_" + index, qlex
						.getFsql().get("ZXDBR"));
				index += 1;
			}
			return fwsyqMap;
		}
		if (ConstValue.QLLX.DIYQ.Value.equals(qllxOrDjlx))// 抵押权
		{
			qllxs.clear();
			qllxs.add(QLLX.DIYQ);
			ConstValue.BDCDYLX bdcdylxEum = ConstValue.BDCDYLX
					.initFrom(bdcdylx);
			unit.Init(baseCommonDao, bdcdylxEum, bdcdyid, null, qllxs, page,
					pagesize);
			Map<String, Object> diyqMap = new HashMap<String, Object>();
			int index = 1;

			for (QL_EX qlex : unit.getQls()) {
				diyqMap.put(PRE_UNIT + "BDCDYH", unit.getUnit().get("BDCDYH"));
				String ywh="";
				if(qlex.getQl().get("YWH") != null){
					ywh=StringHelper.formatObject(qlex.getQl().get("YWH"));
				}
				try{
					if(!StringHelper.isEmpty(ywh)){
						BDCS_XMXX XMXX=Global.getXMXX(ywh);
						if(XMXX!=null){
							ywh=XMXX.getYWLSH();
							if(StringHelper.isEmpty(ywh)){
								ywh=StringHelper.formatObject(qlex.getQl().get("YWH"));
							}
						}else{
							 
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				// 来自权利表
				diyqMap.put(PRE_QL + "YWH" + "_" + index,ywh);
				diyqMap.put(PRE_QL + "DJLX" + "_" + index,
						qlex.getQl().get("DJLX"));
				diyqMap.put(PRE_QL + "DJYY" + "_" + index,
						qlex.getQl().get("DJYY"));
				diyqMap.put(PRE_QL + "QLQSSJ" + "_" + index,
						qlex.getQl().get("QLQSSJ"));
				diyqMap.put(PRE_QL + "QLJSSJ" + "_" + index,
						qlex.getQl().get("QLJSSJ"));
				diyqMap.put(PRE_QL + "BDCQZH" + "_" + index,
						qlex.getQl().get("BDCQZH"));
				diyqMap.put(PRE_QL + "DJSJ" + "_" + index,
						qlex.getQl().get("DJSJ"));
				diyqMap.put(PRE_QL + "DBR" + "_" + index,
						qlex.getQl().get("DBR"));
				diyqMap.put(PRE_QL + "FJ" + "_" + index, qlex.getQl().get("FJ"));
				// 来自附属权利表
				diyqMap.put(PRE_FSQL + "DYR" + "_" + index,
						qlex.getFsql().get("DYR"));
	  /*--------Start----------------抵押人----ZJH,ZJZL(ZJLX)-------------------------------*/
				String ZJH = ""; String ZJZL =""; 
				List<String> alldyr_list = new ArrayList<String>(); 
				List<String> allsqr_list = new ArrayList<String>();
 				for(int j=0;j<qlex.getSqrs().size();j++){
					String SQRXM = StringHelper.formatObject(qlex.getSqrs().get(j).get("SQRXM"));
					if(SQRXM.contains("、")){
						String[] sqr = null;
							 sqr = SQRXM.split("、");
						allsqr_list = java.util.Arrays.asList(sqr);
					}
					String DYR = StringHelper.formatObject(qlex.getFsql().get("DYR"));
					if(DYR.contains(",")||DYR.contains("，")){
						String[] dyr = null;
						if(DYR.contains(",")&&!DYR.contains("，")){
							 dyr = DYR.split(",");
						}else if(DYR.contains("，")&&!DYR.contains(",")){
							 dyr = DYR.split("，");
						}
					    alldyr_list = java.util.Arrays.asList(dyr);
						for(int i =0; i<alldyr_list.size();i++){
							String dyr_one = alldyr_list.get(i).toString().trim();
							for(int k = 0 ;k < allsqr_list.size();k++){
								String sqr_one = allsqr_list.get(k).toString().trim();
								if(dyr_one.equals(sqr_one)){
									if(i+1==alldyr_list.size()){
										ZJH += StringHelper.formatObject(qlex.getSqrs().get(k).get("ZJH"));
										ZJZL += StringHelper.formatObject(qlex.getSqrs().get(k).get("ZJLX"));
									}else{
										ZJH += StringHelper.formatObject(qlex.getSqrs().get(k).get("ZJH")) +",";
										ZJZL += StringHelper.formatObject(qlex.getSqrs().get(k).get("ZJLX")) +",";
									}
									break;
								}
							}
						}
						break;
					}else{
						if(SQRXM.contains("、")){
							for(int k = 0 ;k < allsqr_list.size();k++){
								String sqr_one = allsqr_list.get(k).toString().trim();
								if(DYR.equals(sqr_one)){
									ZJH += StringHelper.formatObject(qlex.getSqrs().get(k).get("ZJH"));
									ZJZL += StringHelper.formatObject(qlex.getSqrs().get(k).get("ZJLX"));
									break;
								}
							}
						}else{
							System.out.println("与抵押有关的流程，申请人不能只有一个");
						}
						break;
					}
				}
 				List<BDCS_CONST> const_zjlx =null;
 				String ZJLX ="";
 				if(ZJZL.contains(",")){
 					String[] zjzl = ZJZL.split(",");
 					List<String> lst_zjzl = java.util.Arrays.asList(zjzl);
 					int h =0;
 					for(String str : lst_zjzl ){
 						const_zjlx= baseCommonDao.getDataList(BDCS_CONST.class, " CONSTSLSID='30' AND CONSTVALUE='"+str+"'");//30指证件类型
 						if(const_zjlx!=null && const_zjlx.size()>0){
 							if(h+1==lst_zjzl.size()){
 								ZJLX += const_zjlx.get(0).getCONSTTRANS();
 							}else{
 								ZJLX += const_zjlx.get(0).getCONSTTRANS()+",";
 							}
 						}
 						 h++;
 					}
 				}else{
 					const_zjlx= baseCommonDao.getDataList(BDCS_CONST.class, " CONSTSLSID='30' AND CONSTVALUE='"+ZJZL+"'");//30指证件类型
 					if(const_zjlx!=null && const_zjlx.size()>0){
 						ZJLX = const_zjlx.get(0).getCONSTTRANS();
 				    }
				}
				diyqMap.put(PRE_SQR+"ZJH"+"_"+index,ZJH);
				diyqMap.put(PRE_SQR+"ZJZL"+"_"+index,ZJLX);
	  /*--------End-----------------------抵押人----ZJH,ZJZL(ZJLX)---------------------------------*/
				if(qlex.getFsql().get("DYBDCLX_CODE") !=null){		//liangq dywlx
					diyqMap.put(PRE_FSQL + "DYBDCLX_CODE", qlex.getFsql()
							.get("DYBDCLX_CODE"));
				}else{
					diyqMap.put(PRE_FSQL + "DYBDCLX_CODE", qlex.getFsql()
							.get("DYBDCLX_CODE2"));
				}
				
				diyqMap.put(PRE_FSQL + "DYFS" + "_" + index, qlex.getFsql()
						.get("DYFS"));
				
				diyqMap.put(PRE_FSQL + "ZJJZWZL" + "_" + index, StringUtils.isEmpty(qlex.getFsql()
						.get("ZJJZWZL"))?unit.getUnit().get("ZL"):qlex.getFsql()
								.get("ZJJZWZL"));//如果在建建筑物坐落为空 则去单元里面找坐落
				diyqMap.put(PRE_FSQL + "ZJJZWDYFW" + "_" + index, qlex
						.getFsql().get("ZJJZWDYFW"));
				//该权利是否注销
				diyqMap.put(PRE_FSQL + "SFZX" + "_" + index, qlex
						.getFsql().get("SFZX"));
				//添加注销相关信息			
				diyqMap.put(PRE_FSQL + "ZXDBR" + "_" + index, qlex
						.getFsql().get("ZXDBR"));
				String zxdyywh="";
				if(qlex.getFsql().get("ZXDYYWH") != null){
					zxdyywh=StringHelper.formatObject(qlex.getFsql().get("ZXDYYWH"));
				}
			 
				try{
					if(!StringHelper.isEmpty(zxdyywh)){
						BDCS_XMXX XMXX=Global.getXMXX(zxdyywh);
						if(XMXX!=null){
							zxdyywh=XMXX.getYWLSH();
							if(StringHelper.isEmpty(zxdyywh)){
								zxdyywh=StringHelper.formatObject(qlex.getFsql().get("ZXDYYWH"));
							}
						}else{
							 
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				diyqMap.put(PRE_FSQL + "ZXDYYWH" + "_" + index,zxdyywh);
				diyqMap.put(PRE_FSQL + "ZXDYYY" + "_" + index, qlex.getFsql()
						.get("ZXDYYY"));
				diyqMap.put(PRE_FSQL + "ZXSJ" + "_" + index, qlex.getFsql()
						.get("ZXSJ"));
				Object zgzqse="";
//				if(!StringHelper.isEmpty(qlex.getFsql().get("ZGZQSE")))
//				{
//					diyqMap.put(PRE_FSQL + "BDBZZQSE" + "_" + index, qlex.getFsql()
//							.get("ZGZQSE"));
//				}else{
//					diyqMap.put(PRE_FSQL + "BDBZZQSE" + "_" + index, qlex.getFsql()
//							.get("BDBZZQSE"));
//				}
				if(!StringHelper.isEmpty(qlex.getFsql().get("DYFS"))){
					String fs = StringHelper.formatObject(qlex.getFsql().get("DYFS")).trim();
					String dw = ConstHelper.getNameByValue("JEDW", StringHelper.formatObject(qlex.getFsql().get("ZQDW")));
					Object zgse=  qlex.getFsql().get("ZGZQSE");
					Object bdse = qlex.getFsql().get("BDBZZQSE");
					String str_zgse = "";  
					String str_bdse = ""; 
					
					if(fs.contains(DYFS.YBDY.Name)){
						if(!StringHelper.isEmpty(qlex.getFsql().get("BDBZZQSE")))
							str_bdse =StringHelper.formatDouble(bdse); 
							diyqMap.put(PRE_FSQL + "BDBZZQSE" + "_" + index, str_bdse + dw);
					}
					if(fs.contains(DYFS.ZGEDY.Name)){
						if(!StringHelper.isEmpty(qlex.getFsql().get("ZGZQSE")))
							str_zgse = StringHelper.formatDouble(zgse);
							diyqMap.put(PRE_FSQL + "BDBZZQSE" + "_" + index, str_zgse + dw);
					}
				}
				diyqMap.put(
						PRE_FSQL + "ZGZQQDSS" + "_" + index,
						StringHelper.FormatByDatatype(qlex.getFsql().get(
								"ZGZQQDSS"))
								);
				diyqMap.put(PRE_FSQL + "ZXDBR" + "_" + index, qlex.getFsql()
						.get("ZXDBR"));

				// 来自于权利人表
				diyqMap.put(PRE_QLR + "ZJH" + "_" + index,
						qlex.getQlr().get("ZJH"));
				diyqMap.put(PRE_QLR + "ZJZL" + "_" + index,
						qlex.getQlr().get("ZJZL"));
				diyqMap.put(PRE_QLR + "QLR" + "_" + index,
						qlex.getQlr().get("QLRMC"));
				index += 1;
			}
			return diyqMap;
		}
		if (ConstValue.QLLX.GJTDSYQ.Value.equals(qllxOrDjlx))// 国有土地所有权及集体土地所有权
		{
			qllxs.clear();
			qllxs.add(QLLX.JTTDSYQ);// 国有土地所有权
			qllxs.add(QLLX.GJTDSYQ);// 集体土地所有权
			ConstValue.BDCDYLX bdcdylxEum = ConstValue.BDCDYLX
					.initFrom(bdcdylx);
			unit.Init(baseCommonDao, bdcdylxEum, bdcdyid, null, qllxs, page,
					pagesize);
			Map<String, Object> syqMap = new HashMap<String, Object>();
			int index = 1;

			for (QL_EX qlex : unit.getQls()) {
				// 来自于不动产单元
				syqMap.put(PRE_UNIT + "BDCDYH", unit.getUnit().get("BDCDYH"));
				syqMap.put(PRE_UNIT + "NYDMJ" + "_" + index, unit.getUnit()
						.get("NYDMJ"));
				syqMap.put(PRE_UNIT + "GDMJ" + "_" + index,
						unit.getUnit().get("GDMJ"));
				syqMap.put(PRE_UNIT + "LDMJ" + "_" + index,
						unit.getUnit().get("LDMJ"));
				syqMap.put(PRE_UNIT + "CDMJ" + "_" + index,
						unit.getUnit().get("CDMJ"));
				syqMap.put(PRE_UNIT + "QTNYDMJ" + "_" + index, unit.getUnit()
						.get("QTNYDMJ"));
				syqMap.put(PRE_UNIT + "JSYDMJ" + "_" + index, unit.getUnit()
						.get("JSYDMJ"));
				syqMap.put(PRE_UNIT + "WLYDMJ" + "_" + index, unit.getUnit()
						.get("WLYDMJ"));
				syqMap.put(PRE_UNIT + "MJDW", unit.getUnit().get("MJDW"));
				// 来自权利表

                String ywh="";
				if(qlex.getQl().get("YWH") != null){
					ywh=StringHelper.formatObject(qlex.getQl().get("YWH"));
				}

				try{
					if(!StringHelper.isEmpty(ywh)){
						BDCS_XMXX XMXX=Global.getXMXX(ywh);
						if(XMXX!=null){
							ywh=XMXX.getYWLSH();
							if(StringHelper.isEmpty(ywh)){
								ywh=StringHelper.formatObject(qlex.getQl().get("YWH"));
							}
						}else{
							 
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				syqMap.put(PRE_QL + "YWH" + "_" + index,ywh);
				syqMap.put(PRE_QL + "DJLX" + "_" + index,
						qlex.getQl().get("DJLX"));
				syqMap.put(PRE_QL + "DJYY" + "_" + index,
						qlex.getQl().get("DJYY"));
				syqMap.put(PRE_QL + "QLQSSJ" + "_" + index,
						qlex.getQl().get("QLQSSJ"));
				syqMap.put(PRE_QL + "DJSJ" + "_" + index,
						qlex.getQl().get("DJSJ"));
				syqMap.put(PRE_QL + "DBR" + "_" + index, qlex.getQl()
						.get("DBR"));
				syqMap.put(PRE_QL + "BDCQZH" + "_" + index, qlex.getQl().get("BDCQZH"));
				syqMap.put(PRE_QL + "FJ" + "_" + index, qlex.getQl().get("FJ"));
				// 来自权利人表
				syqMap.put(PRE_QLR + "QLRMC" + "_" + index,
						qlex.getQlr().get("QLRMC"));
				syqMap.put(PRE_QLR + "ZJZL" + "_" + index,
						qlex.getQlr().get("ZJZL"));
				syqMap.put(PRE_QLR + "ZJH" + "_" + index,
						qlex.getQlr().get("ZJH"));
				syqMap.put(PRE_QLR + "GYQK" + "_" + index,
						qlex.getQlr().get("GYQK"));
				//该权利是否注销
				syqMap.put(PRE_FSQL + "SFZX" + "_" + index, qlex
						.getFsql().get("SFZX"));
				//添加注销相关信息
				syqMap.put(PRE_FSQL + "ZXSJ" + "_" + index, qlex
						.getFsql().get("ZXSJ"));
				syqMap.put(PRE_FSQL + "ZXDYYY" + "_" + index, qlex
						.getFsql().get("ZXDYYY"));
				syqMap.put(PRE_FSQL + "ZXDBR" + "_" + index, qlex
						.getFsql().get("ZXDBR"));
				index += 1;
			}
			return syqMap;
		}
		if (ConstValue.QLLX.HYSYQ.Value.equals(qllxOrDjlx))// 海域
		{
			qllxs.clear();
			qllxs.add(QLLX.HYSYQ);// 海域使用权
			qllxs.add(QLLX.HYSYQ_GZWSYQ);// 海域使用权/构（建）筑物所有权
			qllxs.add(QLLX.WJMHDSYQ);// 无居民海岛使用权
			qllxs.add(QLLX.WJMHDSYQ_GZWSYQ);// 无居民海岛使用权/构（建）筑物所有权
			ConstValue.BDCDYLX bdcdylxEum = ConstValue.BDCDYLX
					.initFrom(bdcdylx);
			unit.Init(baseCommonDao, bdcdylxEum, bdcdyid, null, qllxs, page,
					pagesize);
			Map<String, Object> hyMap = new HashMap<String, Object>();
			int index = 1;

			for (QL_EX qlex : unit.getQls()) {
				// 来自于不动产单元
				hyMap.put(PRE_UNIT + "BDCDYH", unit.getUnit().get("BDCDYH"));
				if(!StringHelper.isEmpty(qlex.getQl().get("SYQMJ"))&&!qlex.getQl().get("SYQMJ").toString().equals("0.0")){//海域登记，权利信息无使用权面积可填项
					hyMap.put(PRE_UNIT + "SYQMJ" + "_" + index,
							unit.getUnit().get("SYQMJ"));
				}else{
					hyMap.put(PRE_UNIT + "SYQMJ" + "_" + index,
							unit.getUnit().get("ZHMJ"));
				}
				hyMap.put(PRE_UNIT + "SYJZE" + "_" + index,
						unit.getUnit().get("SYJZE"));
				hyMap.put(PRE_UNIT + "SYJBZYJ" + "_" + index, unit.getUnit()
						.get("SYJBZYJ"));
				hyMap.put(PRE_UNIT + "SYJJNQK" + "_" + index, unit.getUnit()
						.get("SYJJNQK"));
				// 来自权利表

                String ywh="";
				if(qlex.getQl().get("YWH") != null){
					ywh=StringHelper.formatObject(qlex.getQl().get("YWH"));
				}

				try{
					if(!StringHelper.isEmpty(ywh)){
						BDCS_XMXX XMXX=Global.getXMXX(ywh);
						if(XMXX!=null){
							ywh=XMXX.getYWLSH();
							if(StringHelper.isEmpty(ywh)){
								ywh=StringHelper.formatObject(qlex.getQl().get("YWH"));
							}
						}else{
							 
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				hyMap.put(PRE_QL + "YWH" + "_" + index, ywh);
				hyMap.put(PRE_QL + "DJLX" + "_" + index,
						qlex.getQl().get("DJLX"));
				hyMap.put(PRE_QL + "DJYY" + "_" + index,
						qlex.getQl().get("DJYY"));
				hyMap.put(PRE_QL + "QLQSSJ" + "_" + index,
						qlex.getQl().get("QLQSSJ"));
				hyMap.put(PRE_QL + "QLJSSJ" + "_" + index,
						qlex.getQl().get("QLJSSJ"));
				hyMap.put(PRE_QL + "BDCQZH" + "_" + index,
						qlex.getQl().get("BDCQZH"));
				hyMap.put(PRE_QL + "DJSJ" + "_" + index,
						qlex.getQl().get("DJSJ"));
				hyMap.put(PRE_QL + "DBR" + "_" + index, qlex.getQl().get("DBR"));
				hyMap.put(PRE_QL + "FJ" + "_" + index, qlex.getQl().get("FJ"));
				// 来自权利人表
				hyMap.put(PRE_QLR + "QLRMC" + "_" + index,
						qlex.getQlr().get("QLRMC"));
				hyMap.put(PRE_QLR + "ZJZL" + "_" + index,
						qlex.getQlr().get("ZJZL"));
				hyMap.put(PRE_QLR + "ZJH" + "_" + index,
						qlex.getQlr().get("ZJH"));
				hyMap.put(PRE_QLR + "GYQK" + "_" + index,
						qlex.getQlr().get("GYQK"));
				hyMap.put(PRE_QLR + "QLRLX" + "_" + index,
						qlex.getQlr().get("QLRLX"));
				
				//该权利是否注销
				hyMap.put(PRE_FSQL + "SFZX" + "_" + index, qlex
						.getFsql().get("SFZX"));
				//添加注销相关信息
				hyMap.put(PRE_FSQL + "ZXSJ" + "_" + index, qlex
						.getFsql().get("ZXSJ"));
				hyMap.put(PRE_FSQL + "ZXDYYY" + "_" + index, qlex
						.getFsql().get("ZXDYYY"));
				hyMap.put(PRE_FSQL + "ZXDBR" + "_" + index, qlex
						.getFsql().get("ZXDBR"));
				index += 1;
			}
			return hyMap;
		}
		if (ConstValue.QLLX.LDSYQ.Value.equals(qllxOrDjlx) || ConstValue.QLLX.TDCBJYQ_SLLMSYQ.Value.equals(qllxOrDjlx) || ConstValue.QLLX.LDSYQ_SLLMSYQ.Value.equals(qllxOrDjlx) || ConstValue.QLLX.LDSHYQ_SLLMSYQ.Value.equals(qllxOrDjlx))// 林权信息
		{
			qllxs.clear();
			qllxs.add(QLLX.LDSYQ);// 林地使用权
			qllxs.add(QLLX.TDCBJYQ_SLLMSYQ);
			qllxs.add(QLLX.LDSYQ_SLLMSYQ);
			qllxs.add(QLLX.LDSHYQ_SLLMSYQ);
			ConstValue.BDCDYLX bdcdylxEum = ConstValue.BDCDYLX
					.initFrom(bdcdylx);
			unit.Init(baseCommonDao, bdcdylxEum, bdcdyid, null, qllxs, page,
					pagesize);
			Map<String, Object> lqMap = new HashMap<String, Object>();
			int index = 1;

			for (QL_EX qlex : unit.getQls()) {
				// 来自于不动产单元
				lqMap.put(PRE_UNIT + "BDCDYH", unit.getUnit().get("BDCDYH"));
				lqMap.put(PRE_UNIT + "SYQMJ" + "_" + index,
						unit.getUnit().get("SYQMJ"));
				lqMap.put(PRE_UNIT + "ZYSZ" + "_" + index,
						unit.getUnit().get("ZYSZ"));
				lqMap.put(PRE_UNIT + "ZS" + "_" + index,
						unit.getUnit().get("ZS"));
				lqMap.put(PRE_UNIT + "LZ" + "_" + index,
						unit.getUnit().get("LZ"));
				lqMap.put(PRE_UNIT + "QY" + "_" + index,
						unit.getUnit().get("QY"));
				lqMap.put(PRE_UNIT + "ZLND" + "_" + index,
						unit.getUnit().get("ZLND"));
				lqMap.put(PRE_UNIT + "XDM" + "_" + index,
						unit.getUnit().get("XDM"));
				lqMap.put(PRE_UNIT + "LB" + "_" + index,
						unit.getUnit().get("LB"));
				lqMap.put(PRE_UNIT + "XB" + "_" + index,
						unit.getUnit().get("XB"));
				// 来自权利表

                String ywh="";
				if(qlex.getQl().get("YWH") != null){
					ywh=StringHelper.formatObject(qlex.getQl().get("YWH"));
				}

				try{
					if(!StringHelper.isEmpty(ywh)){
						BDCS_XMXX XMXX=Global.getXMXX(ywh);
						if(XMXX!=null){
							ywh=XMXX.getYWLSH();
							if(StringHelper.isEmpty(ywh)){
								ywh=StringHelper.formatObject(qlex.getQl().get("YWH"));
							}
						}else{
							 
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				lqMap.put(PRE_QL + "YWH" + "_" + index, ywh);
				lqMap.put(PRE_QL + "DJLX" + "_" + index,
						qlex.getQl().get("DJLX"));
				lqMap.put(PRE_QL + "DJYY" + "_" + index,
						qlex.getQl().get("DJYY"));
				lqMap.put(PRE_QL + "QLQSSJ" + "_" + index,
						qlex.getQl().get("QLQSSJ"));
				lqMap.put(PRE_QL + "QLJSSJ" + "_" + index,
						qlex.getQl().get("QLJSSJ"));
				lqMap.put(PRE_QL + "BDCQZH" + "_" + index,
						qlex.getQl().get("BDCQZH"));
				lqMap.put(PRE_QL + "DJSJ" + "_" + index,
						qlex.getQl().get("DJSJ"));
				lqMap.put(PRE_QL + "DBR" + "_" + index, qlex.getQl().get("DBR"));
				lqMap.put(PRE_QL + "FJ" + "_" + index, qlex.getQl().get("FJ"));
				// 来自附属权利表
				lqMap.put(PRE_FSQL + "LDSYQXZ" + "_" + index, qlex.getFsql()
						.get("LDSYQXZ"));
				lqMap.put(PRE_FSQL + "SLLMSYQR1" + "_" + index, qlex.getFsql()
						.get("SLLMSYQR1"));
				lqMap.put(PRE_FSQL + "SLLMSYQR2" + "_" + index, qlex.getFsql()
						.get("SLLMSYQR2"));
				lqMap.put(PRE_FSQL + "FBF", qlex.getFsql().get("FBF"));
				//该权利是否注销
				lqMap.put(PRE_FSQL + "SFZX" + "_" + index, qlex
						.getFsql().get("SFZX"));
				//添加注销相关信息
				lqMap.put(PRE_FSQL + "ZXSJ" + "_" + index, qlex
						.getFsql().get("ZXSJ"));
				lqMap.put(PRE_FSQL + "ZXDYYY" + "_" + index, qlex
						.getFsql().get("ZXDYYY"));
				lqMap.put(PRE_FSQL + "ZXDBR" + "_" + index, qlex
						.getFsql().get("ZXDBR"));
				// 来自权利人表
				lqMap.put(PRE_QLR + "QLRMC" + "_" + index,
						qlex.getQlr().get("QLRMC"));
				lqMap.put(PRE_QLR + "ZJZL" + "_" + index,
						qlex.getQlr().get("ZJZL"));
				lqMap.put(PRE_QLR + "ZJH" + "_" + index,
						qlex.getQlr().get("ZJH"));
				lqMap.put(PRE_QLR + "GYQK" + "_" + index,
						qlex.getQlr().get("GYQK"));
				lqMap.put(PRE_QLR + "QLRLX" + "_" + index,
						qlex.getQlr().get("QLRLX"));
				index += 1;
			}
			return lqMap;
		}
		if (ConstValue.QLLX.GYNYDSHYQ.Value.equals(qllxOrDjlx))// 农用地信息
		{
			qllxs.clear();
			qllxs.add(QLLX.GYNYDSHYQ);// 24 国有农用地使用权
			ConstValue.BDCDYLX bdcdylxEum = ConstValue.BDCDYLX.initFrom(bdcdylx);
			unit.Init(baseCommonDao, bdcdylxEum, bdcdyid, null, qllxs, page,pagesize);
			Map<String, Object> lqMap = new HashMap<String, Object>();
			int index = 1;

			for (QL_EX qlex : unit.getQls()) {
				
				// 来自于不动产单元
				lqMap.put(PRE_UNIT + "BDCDYH", unit.getUnit().get("BDCDYH"));
				//lqMap.put(PRE_UNIT + "SYQMJ" + "_" + index, unit.getUnit().get("SYQMJ"));
				lqMap.put(PRE_UNIT + "SYQMJ" + "_" + index, unit.getUnit().get("CBMJ"));
				lqMap.put(PRE_UNIT + "ZYSZ" + "_" + index, unit.getUnit().get("ZYSZ"));
				lqMap.put(PRE_UNIT + "ZS" + "_" + index, unit.getUnit().get("ZS"));
				lqMap.put(PRE_UNIT + "LZ" + "_" + index, unit.getUnit().get("LZ"));
				lqMap.put(PRE_UNIT + "QY" + "_" + index, unit.getUnit().get("QY"));
				lqMap.put(PRE_UNIT + "ZLND" + "_" + index, unit.getUnit().get("ZLND"));
				lqMap.put(PRE_UNIT + "XDM" + "_" + index, unit.getUnit().get("XDM"));
				lqMap.put(PRE_UNIT + "LB" + "_" + index, unit.getUnit().get("LB"));
				lqMap.put(PRE_UNIT + "XB" + "_" + index, unit.getUnit().get("XB"));
				
				// 来自权利表
                String ywh = "";
				if(qlex.getQl().get("YWH") != null){
					ywh = StringHelper.formatObject(qlex.getQl().get("YWH"));
				}

				try{
					if(!StringHelper.isEmpty(ywh)){
						BDCS_XMXX XMXX = Global.getXMXX(ywh);
						if(XMXX != null){
							ywh = XMXX.getYWLSH();
							if(StringHelper.isEmpty(ywh)){
								ywh = StringHelper.formatObject(qlex.getQl().get("YWH"));
							}
						}else{
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				lqMap.put(PRE_QL + "YWH" + "_" + index, ywh);
				lqMap.put(PRE_QL + "DJLX" + "_" + index, qlex.getQl().get("DJLX"));
				lqMap.put(PRE_QL + "DJYY" + "_" + index, qlex.getQl().get("DJYY"));
				lqMap.put(PRE_QL + "QLQSSJ" + "_" + index, qlex.getQl().get("QLQSSJ"));
				lqMap.put(PRE_QL + "QLJSSJ" + "_" + index, qlex.getQl().get("QLJSSJ"));
				lqMap.put(PRE_QL + "BDCQZH" + "_" + index, qlex.getQl().get("BDCQZH"));
				lqMap.put(PRE_QL + "DJSJ" + "_" + index, qlex.getQl().get("DJSJ"));
				lqMap.put(PRE_QL + "DBR" + "_" + index, qlex.getQl().get("DBR"));
				lqMap.put(PRE_QL + "FJ" + "_" + index, qlex.getQl().get("FJ"));
				// 来自附属权利表
				lqMap.put(PRE_FSQL + "LDSYQXZ" + "_" + index, qlex.getFsql() .get("LDSYQXZ"));
				lqMap.put(PRE_FSQL + "SLLMSYQR1" + "_" + index, qlex.getFsql() .get("SLLMSYQR1"));
				lqMap.put(PRE_FSQL + "SLLMSYQR2" + "_" + index, qlex.getFsql() .get("SLLMSYQR2"));
				lqMap.put(PRE_FSQL + "FBF", qlex.getFsql().get("FBF"));
				//该权利是否注销
				lqMap.put(PRE_FSQL + "SFZX" + "_" + index, qlex .getFsql().get("SFZX"));
				//添加注销相关信息
				lqMap.put(PRE_FSQL + "ZXSJ" + "_" + index, qlex .getFsql().get("ZXSJ"));
				lqMap.put(PRE_FSQL + "ZXDYYY" + "_" + index, qlex .getFsql().get("ZXDYYY"));
				lqMap.put(PRE_FSQL + "ZXDBR" + "_" + index, qlex .getFsql().get("ZXDBR"));
				// 来自权利人表
				lqMap.put(PRE_QLR + "QLRMC" + "_" + index, qlex.getQlr().get("QLRMC"));
				lqMap.put(PRE_QLR + "ZJZL" + "_" + index, qlex.getQlr().get("ZJZL"));
				lqMap.put(PRE_QLR + "ZJH" + "_" + index, qlex.getQlr().get("ZJH"));
				lqMap.put(PRE_QLR + "GYQK" + "_" + index, qlex.getQlr().get("GYQK"));
				lqMap.put(PRE_QLR + "QLRLX" + "_" + index, qlex.getQlr().get("QLRLX"));
				index += 1;
			}
			return lqMap;
		}
		if (ConstValue.DJLX.YGDJ.Value.equals(qllxOrDjlx))// 预告登记信息
		{
			ConstValue.BDCDYLX bdcdylxEum = ConstValue.BDCDYLX.initFrom(bdcdylx);
			unit.Init(baseCommonDao, bdcdylxEum, bdcdyid, DJLX.YGDJ, null,page, pagesize);
			Map<String, Object> ygdjMap = new HashMap<String, Object>();
			int index = 1;

			for (QL_EX qlex : unit.getQls()) {
				if ("700".equals(qlex.getDJLX())) {
					// 来自于不动产单元
					ygdjMap.put(PRE_UNIT + "BDCDYH", unit.getUnit().get("BDCDYH"));
					ygdjMap.put(PRE_UNIT + "TDSYQR" + "_" + index, unit.getUnit().get("TDSYQR"));
					ygdjMap.put(PRE_UNIT + "GHYT" + "_" + index, unit.getUnit().get("GHYT"));
					ygdjMap.put(PRE_UNIT + "FWXZ" + "_" + index, unit.getUnit().get("FWXZ"));
					ygdjMap.put(PRE_UNIT + "SZC" + "_" + index, unit.getUnit().get("SZC"));
					ygdjMap.put(PRE_UNIT + "ZCS" + "_" + index, unit.getUnit().get("ZCS"));
					if (unit.getDidy().get("BDCDYLX").equals("031")) {
						ygdjMap.put(PRE_UNIT + "SCJZMJ" + "_" + index, unit.getUnit().get("SCJZMJ"));
					} else {
						ygdjMap.put(PRE_UNIT + "SCJZMJ" + "_" + index, unit.getUnit().get("YCJZMJ"));
					}
					ygdjMap.put(PRE_UNIT + "ZL", unit.getUnit().get("ZL"));
					// 来自权利表

					String ywh = "";
					if (qlex.getQl().get("YWH") != null) {
						ywh = StringHelper.formatObject(qlex.getQl().get("YWH"));
					}

					try {
						if (!StringHelper.isEmpty(ywh)) {
							BDCS_XMXX XMXX = Global.getXMXX(ywh);
							if (XMXX != null) {
								ywh = XMXX.getYWLSH();
								if (StringHelper.isEmpty(ywh)) {
									ywh = StringHelper.formatObject(qlex.getQl().get("YWH"));
								}
							} else {

							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					ygdjMap.put(PRE_QL + "YWH" + "_" + index, ywh);
					ygdjMap.put(PRE_QL + "DJLX" + "_" + index, qlex.getQl().get("DJLX"));
					ygdjMap.put(PRE_QL + "DJYY" + "_" + index, qlex.getQl().get("DJYY"));
					ygdjMap.put(PRE_QL + "DJSJ" + "_" + index, qlex.getQl().get("DJSJ"));
					ygdjMap.put(PRE_QL + "DBR" + "_" + index, qlex.getQl().get("DBR"));
					ygdjMap.put(PRE_QL + "FJ" + "_" + index, qlex.getQl().get("FJ"));
					ygdjMap.put(PRE_QL + "BDCQZH" + "_" + index, qlex.getQl().get("BDCQZH"));
					if (!StringHelper.isEmpty(qlex.getQl().get("QDJG"))
							&& !qlex.getQl().get("QDJG").toString().equals("0.0")) {
						ygdjMap.put(PRE_QL + "QDJG" + "_" + index, qlex.getQl().get("QDJG"));
					}
					// 来自附属权利表
					if (qlex.getQLLX() != null && !qlex.getQLLX().equals("23")) {
						ygdjMap.put(PRE_FSQL + "YWR" + "_" + index, qlex.getFsql().get("YWR"));
						ygdjMap.put(PRE_FSQL + "YWRZJZL" + "_" + index, qlex.getFsql().get("YWRZJZL"));
						ygdjMap.put(PRE_FSQL + "YWRZJH" + "_" + index, qlex.getFsql().get("YWRZJH"));
						// 添加预告登记显示不全的问题
						String xzqhdm = ConfigHelper.getNameByValue("XZQHDM");
						if ("420900".equals(xzqhdm) || "420902".equals(xzqhdm)) {
							ygdjMap.put((new StringBuilder(String.valueOf(PRE_FSQL))).append("SFZX").append("_")
									.append(index).toString(), qlex.getFsql().get("SFZX"));
						}
						if (StringHelper.isEmpty(qlex.getXMBH())) {
							List<YC_SC_H_XZ> ycscs = baseCommonDao.getDataList(YC_SC_H_XZ.class,
									"YCBDCDYID='" + qlex.getBDCDYID() + "'");
							List<BDCS_QL_LS> qlLists = null;
							if (ycscs.size() >0) {
								qlLists = baseCommonDao.getDataList(BDCS_QL_LS.class, "BDCDYID='"
										+ ycscs.get(0).getSCBDCDYID()
										+ "' AND QLLX IN('1','2','3','4','5','6','7','8','10','11','12','15','24','36')");
							}
							if (qlLists != null && qlLists.size() > 0) {
								List<BDCS_QLR_LS> ywrlist = baseCommonDao.getDataList(BDCS_QLR_LS.class,
										"QLID='" + qlLists.get(0).getId() + "'");
								StringBuilder ywr = new StringBuilder();
								StringBuilder ywrzjlx = new StringBuilder();
								StringBuilder ywrzjh = new StringBuilder();
								if (ywrlist.size() > 0) {
									for (int j = 0; j < ywrlist.size(); j++) {
										if (j != 0) {
											ywr.append("/");
											ywrzjlx.append("/");
											ywrzjh.append("/");
										}
										ywr.append(ywrlist.get(j).getQLRMC());
										ywrzjlx.append(ConstHelper.getNameByValue("ZJLX",
												StringHelper.formatObject(ywrlist.get(j).getZJZL())));
										ywrzjh.append(ywrlist.get(j).getZJH());
									}
								}
								ygdjMap.put(this.PRE_FSQL + "YWR" + "_" + index, StringHelper.formatObject(ywr));
								ygdjMap.put(this.PRE_FSQL + "YWRZJZL" + "_" + index,
										StringHelper.formatObject(ywrzjlx));
								ygdjMap.put(this.PRE_FSQL + "YWRZJH" + "_" + index, StringHelper.formatObject(ywrzjh));
							}
						}
					} else {
						if (!StringHelper.isEmpty(qlex.getXMBH())) {
							ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(qlex.getXMBH());
							String Baseworkflow_ID = "";
							if (info != null) {
								Baseworkflow_ID = info.getBaseworkflowcode();
							}

							if ("YG002".equals(Baseworkflow_ID)) {
								List<BDCS_QL_LS> qlList = baseCommonDao.getDataList(BDCS_QL_LS.class, "XMBH='"
										+ qlex.getXMBH()
										+ "' AND QLLX IN('1','2','3','4','5','6','7','8','10','11','12','15','24','36')");
								if (qlList.size() > 0) {
									StringBuilder ywr = new StringBuilder();
									StringBuilder ywrzjlx = new StringBuilder();
									StringBuilder ywrzjh = new StringBuilder();
									String syqqlid = "";
									if (!StringHelper.isEmpty(qlList.get(0).getId())) {
										syqqlid = qlList.get(0).getId();
									}
									List<BDCS_QLR_LS> ywrlist = baseCommonDao.getDataList(BDCS_QLR_LS.class,
											"XMBH='" + qlex.getXMBH() + "' AND QLID='" + syqqlid + "'");
									for (int i = 0; i < ywrlist.size(); i++) {
										if (i != 0) {
											ywr.append("/");
											ywrzjlx.append("/");
											ywrzjh.append("/");
										}
										ywr.append(ywrlist.get(i).getQLRMC());
										ywrzjlx.append(ywrlist.get(i).getZJZLName());
										ywrzjh.append(ywrlist.get(i).getZJH());
									}
									ygdjMap.put(this.PRE_FSQL + "YWR" + "_" + index, StringHelper.formatObject(ywr));
									ygdjMap.put(this.PRE_FSQL + "YWRZJZL" + "_" + index,
											StringHelper.formatObject(ywrzjlx));
									ygdjMap.put(this.PRE_FSQL + "YWRZJH" + "_" + index,
											StringHelper.formatObject(ywrzjh));
								}
							} else if ("YG003".equals(Baseworkflow_ID)) {
								List<BDCS_QL_LS> qlList = baseCommonDao.getDataList(BDCS_QL_LS.class, "DJDYID='"
										+ qlex.getDJDYID()
										+ "' AND QLLX IN('1','2','3','4','5','6','7','8','10','11','12','15','24','36')");
								if (qlList.size() > 0) {
									StringBuilder ywr = new StringBuilder();
									StringBuilder ywrzjlx = new StringBuilder();
									StringBuilder ywrzjh = new StringBuilder();
									List<BDCS_SQR> ywrlist = baseCommonDao.getDataList(BDCS_SQR.class,
											"XMBH='" + qlex.getXMBH() + "' AND SQRLB='2'");
									if (ywrlist.size() > 0) {
										for (int i = 0; i < ywrlist.size(); i++) {
											if (i != 0) {
												ywr.append("/");
												ywrzjlx.append("/");
												ywrzjh.append("/");
											}
											ywr.append(ywrlist.get(i).getSQRXM());
											ywrzjlx.append(ConstHelper.getNameByValue("ZJLX",
													StringHelper.formatObject(ywrlist.get(i).getZJLX())));
											ywrzjh.append(ywrlist.get(i).getZJH());
										}
									}
									ygdjMap.put(this.PRE_FSQL + "YWR" + "_" + index, StringHelper.formatObject(ywr));
									ygdjMap.put(this.PRE_FSQL + "YWRZJZL" + "_" + index,
											StringHelper.formatObject(ywrzjlx));
									ygdjMap.put(this.PRE_FSQL + "YWRZJH" + "_" + index,
											StringHelper.formatObject(ywrzjh));
								}
							} else {
								ygdjMap.put(PRE_FSQL + "YWR" + "_" + index, qlex.getFsql().get("DYR"));
								ygdjMap.put(PRE_FSQL + "YWRZJZL" + "_" + index, qlex.getFsql().get("YWRZJZL"));
								ygdjMap.put(PRE_FSQL + "YWRZJH" + "_" + index, qlex.getFsql().get("YWRZJH"));
							}
							ygdjMap.put(PRE_FSQL + "DYBDCLX_CODE", qlex.getFsql().get("DYBDCLX_CODE"));
							ygdjMap.put(PRE_FSQL + "DYFS" + "_" + index, qlex.getFsql().get("DYFS"));
							ygdjMap.put(PRE_FSQL + "ZJJZWZL" + "_" + index, qlex.getFsql().get("ZJJZWZL"));
							ygdjMap.put(PRE_FSQL + "ZJJZWDYFW" + "_" + index, qlex.getFsql().get("ZJJZWDYFW"));
							ygdjMap.put(PRE_QL + "QDJG" + "_" + index, qlex.getFsql().get("BDBZZQSE"));
							String zxdyywh = "";
							if (qlex.getFsql().get("ZXDYYWH") != null) {
								zxdyywh = StringHelper.formatObject(qlex.getFsql().get("ZXDYYWH"));
							}

							try {
								if (!StringHelper.isEmpty(zxdyywh)) {
									BDCS_XMXX XMXX = Global.getXMXX(zxdyywh);
									if (XMXX != null) {
										zxdyywh = XMXX.getYWLSH();
										if (StringHelper.isEmpty(zxdyywh)) {
											zxdyywh = StringHelper.formatObject(qlex.getFsql().get("ZXDYYWH"));
										}
									} else {

									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}

							ygdjMap.put(PRE_FSQL + "ZXDYYWH" + "_" + index, zxdyywh);
							ygdjMap.put(PRE_FSQL + "ZXDYYY" + "_" + index, qlex.getFsql().get("ZXDYYY"));
							ygdjMap.put(PRE_FSQL + "ZXSJ" + "_" + index, qlex.getFsql().get("ZXSJ"));
							if (!StringHelper.isEmpty(qlex.getFsql().get("DYFS"))
									&& qlex.getFsql().get("DYFS").toString().contains("最高额")) {
								Object zgzqse = "";
								if (!StringHelper.isEmpty(qlex.getFsql().get("ZGZQSE"))) {
									DecimalFormat df = new DecimalFormat("#########.##");
									df.setRoundingMode(RoundingMode.HALF_UP);
									zgzqse = df.format(qlex.getFsql().get("ZGZQSE"));
								}
								ygdjMap.put(PRE_QL + "QDJG" + "_" + index,
										StringHelper.FormatByDatatype(qlex.getFsql().get("ZGZQQDSS")) + zgzqse);
							}
							ygdjMap.put(PRE_FSQL + "ZXDBR" + "_" + index, qlex.getFsql().get("ZXDBR"));
						} else {
							List<BDCS_QL_LS> qlLists = baseCommonDao.getDataList(BDCS_QL_LS.class, "DJDYID='"
									+ qlex.getDJDYID()
									+ "' AND QLLX IN('1','2','3','4','5','6','7','8','10','11','12','15','24','36')");
							if (qlLists != null && qlLists.size() > 0) {
								List<BDCS_QLR_LS> ywrlist = baseCommonDao.getDataList(BDCS_QLR_LS.class,
										"QLID='" + qlLists.get(0).getId() + "'");
								StringBuilder ywr = new StringBuilder();
								StringBuilder ywrzjlx = new StringBuilder();
								StringBuilder ywrzjh = new StringBuilder();
								if (ywrlist.size() > 0) {
									for (int j = 0; j < ywrlist.size(); j++) {
										if (j != 0) {
											ywr.append("/");
											ywrzjlx.append("/");
											ywrzjh.append("/");
										}
										ywr.append(ywrlist.get(j).getQLRMC());
										ywrzjlx.append(ConstHelper.getNameByValue("ZJLX",StringHelper.formatObject(ywrlist.get(j).getZJZL())));
										ywrzjh.append(ywrlist.get(j).getZJH());
									}
								}
								ygdjMap.put(this.PRE_FSQL + "YWR" + "_" + index, StringHelper.formatObject(ywr));
								ygdjMap.put(this.PRE_FSQL + "YWRZJZL" + "_" + index,
										StringHelper.formatObject(ywrzjlx));
								ygdjMap.put(this.PRE_FSQL + "YWRZJH" + "_" + index, StringHelper.formatObject(ywrzjh));
							}

						}
						if (!StringHelper.isEmpty(qlex.getFsql().get("BDBZZQSE"))
								&& !qlex.getFsql().get("BDBZZQSE").toString().equals("0.0")) {
							ygdjMap.put(PRE_QL + "QDJG" + "_" + index, qlex.getFsql().get("BDBZZQSE"));
						}
						if (!StringHelper.isEmpty(qlex.getFsql().get("ZGZQSE"))
								&& !qlex.getFsql().get("ZGZQSE").toString().equals("0.0")) {
							ygdjMap.put(PRE_QL + "QDJG" + "_" + index, qlex.getFsql().get("ZGZQSE"));
						}
					}
					// 该权利是否注销
					ygdjMap.put(PRE_FSQL + "SFZX" + "_" + index, qlex.getFsql().get("SFZX"));
					// 添加注销相关信息
					ygdjMap.put(PRE_FSQL + "ZXSJ" + "_" + index, qlex.getFsql().get("ZXSJ"));
					ygdjMap.put(PRE_FSQL + "ZXDYYY" + "_" + index, qlex.getFsql().get("ZXDYYY"));
					ygdjMap.put(PRE_FSQL + "ZXDBR" + "_" + index, qlex.getFsql().get("ZXDBR"));
					// 来自权利人表
					ygdjMap.put(PRE_QLR + "QLRMC" + "_" + index, qlex.getQlr().get("QLRMC"));
					ygdjMap.put(PRE_QLR + "ZJZL" + "_" + index, qlex.getQlr().get("ZJZL"));
					ygdjMap.put(PRE_QLR + "ZJH" + "_" + index, qlex.getQlr().get("ZJH"));
					ygdjMap.put(PRE_FSQL + "YGDJZL" + "_" + index, qlex.getFsql().get("YGDJZL"));
					// ygdjMap.put(PRE_QLR + "YGDJZL" + "_" + index,
					// qlex.getQlr()
					// .get("YGDJZL"));
				}
				index += 1;

			}
			return ygdjMap;
		}
		
		if (ConstValue.DJLX.YYDJ.Value.equals(qllxOrDjlx))// 异议登记信息
		{
			ConstValue.BDCDYLX bdcdylxEum = ConstValue.BDCDYLX
					.initFrom(bdcdylx);
			unit.Init(baseCommonDao, bdcdylxEum, bdcdyid, DJLX.YYDJ, null,
					page, pagesize);
			Map<String, Object> yydjMap = new HashMap<String, Object>();
			int index = 1;

			for (QL_EX qlex : unit.getQls()) {
				yydjMap.put(PRE_UNIT + "BDCDYH", unit.getUnit().get("BDCDYH"));
				// 来自于申请人表
				yydjMap.put(PRE_SQR + "SQRXM" + "_" + index,
						qlex.getSqr().get("SQRXM"));
				yydjMap.put(PRE_SQR + "ZJLX" + "_" + index,
						qlex.getSqr().get("ZJLX"));
				yydjMap.put(PRE_SQR + "ZJH" + "_" + index,
						qlex.getSqr().get("ZJH"));
				// 来自权利表

                String ywh="";
				if(qlex.getQl().get("YWH") != null){
					ywh=StringHelper.formatObject(qlex.getQl().get("YWH"));
				}

				try{
					if(!StringHelper.isEmpty(ywh)){
						BDCS_XMXX XMXX=Global.getXMXX(ywh);
						if(XMXX!=null){
							ywh=XMXX.getYWLSH();
							if(StringHelper.isEmpty(ywh)){
								ywh=StringHelper.formatObject(qlex.getQl().get("YWH"));
							}
						}else{
							 
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				yydjMap.put(PRE_QL + "YWH" + "_" + index,ywh);
				yydjMap.put(PRE_QL + "BDCQZH" + "_" + index,
						qlex.getQl().get("BDCQZH"));
				yydjMap.put(PRE_QL + "DJSJ" + "_" + index,
						qlex.getQl().get("DJSJ"));
				yydjMap.put(PRE_QL + "DBR" + "_" + index,
						qlex.getQl().get("DBR"));
				yydjMap.put(PRE_QL + "FJ" + "_" + index, qlex.getQl().get("FJ"));
				// 来自附属权利表
				yydjMap.put(PRE_FSQL + "YYSX" + "_" + index, qlex.getFsql()
						.get("YYSX"));
				//该权利是否注销
				yydjMap.put(PRE_FSQL + "SFZX" + "_" + index, qlex
						.getFsql().get("SFZX"));
				String zxdyywh="";
				if(qlex.getFsql().get("ZXDYYWH") != null){
					zxdyywh=StringHelper.formatObject(qlex.getFsql().get("ZXDYYWH"));
				}
				try{
					if(!StringHelper.isEmpty(zxdyywh)){
						BDCS_XMXX XMXX=Global.getXMXX(zxdyywh);
						if(XMXX!=null){
							zxdyywh=XMXX.getYWLSH();
							if(StringHelper.isEmpty(zxdyywh)){
								zxdyywh=StringHelper.formatObject(qlex.getFsql().get("ZXDYYWH"));
							}
						}else{
							 
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}

				yydjMap.put(PRE_FSQL + "ZXDYYWH" + "_" + index, zxdyywh);
				yydjMap.put(PRE_FSQL + "ZXYYYY" + "_" + index, qlex.getFsql()
						.get("ZXYYYY"));
				yydjMap.put(PRE_FSQL + "ZXSJ" + "_" + index, qlex.getFsql()
						.get("ZXSJ"));
				yydjMap.put(PRE_FSQL + "ZXDBR" + "_" + index, qlex.getFsql()
						.get("ZXDBR"));
				index += 1;
			}
			return yydjMap;
		}
		if (ConstValue.DJLX.CFDJ.Value.equals(qllxOrDjlx))// 查封登记信息
		{
			unit.Init(baseCommonDao, null, bdcdyid, DJLX.CFDJ, null, page,
					pagesize);
			Map<String, Object> cfdjMap = new HashMap<String, Object>();
			int index = 1;

			for (QL_EX qlex : unit.getQls()) {
				cfdjMap.put(PRE_UNIT + "BDCDYH", qlex.getBDCDYH());
				// 来自于权利表
				String ywh="";
				if(qlex.getQl().get("YWH") != null){
					ywh=StringHelper.formatObject(qlex.getQl().get("YWH"));
				}

				try{
					if(!StringHelper.isEmpty(ywh)){
						BDCS_XMXX XMXX=Global.getXMXX(ywh);
						if(XMXX!=null){
							ywh=XMXX.getYWLSH();
							if(StringHelper.isEmpty(ywh)){
								ywh=StringHelper.formatObject(qlex.getQl().get("YWH"));
							}
						}else{
							 
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				cfdjMap.put(PRE_QL + "YWH" + "_" + index,ywh);
				cfdjMap.put(PRE_QL + "FJ" + "_" + index, qlex.getQl().get("FJ"));
				cfdjMap.put(PRE_QL + "DJLX" + "_" + index,
						qlex.getQl().get("DJLX"));
				cfdjMap.put(PRE_QL + "QLQSSJ" + "_" + index,
						qlex.getQl().get("QLQSSJ"));
				cfdjMap.put(PRE_QL + "QLJSSJ" + "_" + index,
						qlex.getQl().get("QLJSSJ"));
				cfdjMap.put(PRE_QL + "DJSJ" + "_" + index,
						qlex.getQl().get("DJSJ"));
				cfdjMap.put(PRE_QL + "DBR" + "_" + index,
						qlex.getQl().get("DBR"));
				// 来自于附属权利表
				//该权利是否注销
				cfdjMap.put(PRE_FSQL + "SFZX" + "_" + index, qlex
						.getFsql().get("SFZX"));
				cfdjMap.put(PRE_FSQL + "CFJG" + "_" + index, qlex.getFsql()
						.get("CFJG"));
				cfdjMap.put(PRE_FSQL + "CFLX" + "_" + index, qlex.getFsql()
						.get("CFLX"));
				cfdjMap.put(PRE_FSQL + "CFWJ" + "_" + index, qlex.getFsql()
						.get("CFWJ"));
				cfdjMap.put(PRE_FSQL + "CFWH" + "_" + index, qlex.getFsql()
						.get("CFWH"));
				cfdjMap.put(PRE_FSQL + "CFFW" + "_" + index, qlex.getFsql()
						.get("CFFW"));
				
				String zxdyywh="";
				Date zxsjDate = null;
				String zxsj = "";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
				if(qlex.getFsql().get("ZXDYYWH") != null){
					zxdyywh=StringHelper.formatObject(qlex.getFsql().get("ZXDYYWH"));
				}
			 
				try{
					if(!StringHelper.isEmpty(zxdyywh)){
						BDCS_XMXX XMXX=Global.getXMXX(zxdyywh);
						if(XMXX!=null){
							zxdyywh=XMXX.getYWLSH();
							zxsjDate = XMXX.getDJSJ();
							zxsj =sdf.format(zxsjDate);
							if(StringHelper.isEmpty(zxdyywh)){
								zxdyywh=StringHelper.formatObject(qlex.getFsql().get("ZXDYYWH"));
							}
							
							if(StringHelper.isEmpty(zxsj)){
								zxdyywh=StringHelper.formatObject(qlex.getFsql().get("ZXSJ"));
							}
						}else{
							 
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}

				cfdjMap.put(PRE_FSQL + "ZXDYYWH" + "_" + index,zxdyywh);
				cfdjMap.put(PRE_FSQL + "JFJG" + "_" + index, qlex.getFsql()
						.get("JFJG"));
				cfdjMap.put(PRE_FSQL + "JFWJ" + "_" + index, qlex.getFsql()
						.get("JFWJ"));
				cfdjMap.put(PRE_FSQL + "JFWH" + "_" + index, qlex.getFsql()
						.get("JFWH"));
//				cfdjMap.put(PRE_FSQL + "ZXSJ" + "_" + index, qlex.getFsql()
//						.get("ZXSJ"));
				cfdjMap.put(PRE_FSQL + "ZXSJ" + "_" + index, zxsj);
				cfdjMap.put(PRE_FSQL + "ZXDBR" + "_" + index, qlex.getFsql()
						.get("ZXDBR"));
				cfdjMap.put(PRE_FSQL + "ZXDYYY" + "_" + index, qlex.getFsql()
						.get("ZXDYYY"));
				index += 1;
			}
			return cfdjMap;
		}
		
		if (ConstValue.QLLX.QTQL.Value.equals(qllxOrDjlx))// 异议登记信息
		{
			qllxs.clear();
			qllxs.add(QLLX.QTQL);
			ConstValue.BDCDYLX bdcdylxEum = ConstValue.BDCDYLX.initFrom(bdcdylx);
			unit.Init(baseCommonDao, bdcdylxEum, bdcdyid, null, qllxs, page,pagesize);
			Map<String, Object> yydjMap = new HashMap<String, Object>();
			int index = 1;

			for (QL_EX qlex : unit.getQls()) {
				yydjMap.put(PRE_UNIT + "BDCDYH", unit.getUnit().get("BDCDYH"));
				// 来自于申请人表
				yydjMap.put(PRE_SQR + "SQRXM" + "_" + index,
						qlex.getSqr().get("SQRXM"));
				yydjMap.put(PRE_SQR + "ZJLX" + "_" + index,
						qlex.getSqr().get("ZJLX"));
				yydjMap.put(PRE_SQR + "ZJH" + "_" + index,
						qlex.getSqr().get("ZJH"));
				// 来自权利表

	            String ywh="";
				if(qlex.getQl().get("YWH") != null){
					ywh=StringHelper.formatObject(qlex.getQl().get("YWH"));
				}

				try{
					if(!StringHelper.isEmpty(ywh)){
						BDCS_XMXX XMXX=Global.getXMXX(ywh);
						if(XMXX!=null){
							ywh=XMXX.getYWLSH();
							if(StringHelper.isEmpty(ywh)){
								ywh=StringHelper.formatObject(qlex.getQl().get("YWH"));
							}
						}else{
							 
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				yydjMap.put(PRE_QL + "YWH" + "_" + index,ywh);
				yydjMap.put(PRE_QL + "BDCQZH" + "_" + index,
						qlex.getQl().get("BDCQZH"));
				yydjMap.put(PRE_QL + "DJSJ" + "_" + index,
						qlex.getQl().get("DJSJ"));
				yydjMap.put(PRE_QL + "DBR" + "_" + index,
						qlex.getQl().get("DBR"));
				yydjMap.put(PRE_QL + "FJ" + "_" + index, qlex.getQl().get("FJ"));
				// 来自附属权利表
				yydjMap.put(PRE_FSQL + "YYSX" + "_" + index, qlex.getFsql()
						.get("YYSX"));
				//该权利是否注销
				yydjMap.put(PRE_FSQL + "SFZX" + "_" + index, qlex
						.getFsql().get("SFZX"));
				String zxdyywh="";
				if(qlex.getFsql().get("ZXDYYWH") != null){
					zxdyywh=StringHelper.formatObject(qlex.getFsql().get("ZXDYYWH"));
				}
				try{
					if(!StringHelper.isEmpty(zxdyywh)){
						BDCS_XMXX XMXX=Global.getXMXX(zxdyywh);
						if(XMXX!=null){
							zxdyywh=XMXX.getYWLSH();
							if(StringHelper.isEmpty(zxdyywh)){
								zxdyywh=StringHelper.formatObject(qlex.getFsql().get("ZXDYYWH"));
							}
						}else{
							 
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}

				yydjMap.put(PRE_FSQL + "ZXDYYWH" + "_" + index, zxdyywh);
				yydjMap.put(PRE_FSQL + "ZXYYYY" + "_" + index, qlex.getFsql()
						.get("ZXYYYY"));
				yydjMap.put(PRE_FSQL + "ZXSJ" + "_" + index, qlex.getFsql()
						.get("ZXSJ"));
				yydjMap.put(PRE_FSQL + "ZXDBR" + "_" + index, qlex.getFsql()
						.get("ZXDBR"));
				index += 1;
			}
			return yydjMap;
		}
		
		return null;
	}
	
	

	// 根据不动产单元ID判断不动产单元类型 WUZHU
	private BDCDYLX ValidateBDCDYLX(String bdcdyid) {
		RealUnit u = null;
		u = (RealUnit) baseCommonDao.get(BDCS_SYQZD_LS.class, bdcdyid);
		if (u != null)
			return BDCDYLX.SYQZD;
		u = (RealUnit) baseCommonDao.get(BDCS_SHYQZD_LS.class, bdcdyid);
		if (u != null)
			return BDCDYLX.SHYQZD;
		u = (RealUnit) baseCommonDao.get(BDCS_ZRZ_LS.class, bdcdyid);
		if (u != null)
			return BDCDYLX.ZRZ;
		u = (RealUnit) baseCommonDao.get(BDCS_ZRZ_LSY.class, bdcdyid);
		if (u != null)
			return BDCDYLX.YCZRZ;
		u = (RealUnit) baseCommonDao.get(BDCS_ZH_LS.class, bdcdyid);
		if (u != null)
			return BDCDYLX.HY;
		u = (RealUnit) baseCommonDao.get(BDCS_SLLM_LS.class, bdcdyid);
		if (u != null)
			return BDCDYLX.LD;
		// 以下注销是由于还没有相应的实体类
		// u = (RealUnit)baseCommonDao.get(BDCS_GZW_LS.class, bdcdyid);
		// if(u!=null)
		// return BDCDYLX.GZW;
		// u = (RealUnit)baseCommonDao.get(BDCS_QTDZW_LS.class, bdcdyid);
		// if(u!=null)
		// return BDCDYLX.QTDZW;
		// u = (RealUnit)baseCommonDao.get(BDCS_DZDZW_LS.class, bdcdyid);
		// if(u!=null)
		// return BDCDYLX.DZDZW;
		// u = (RealUnit)baseCommonDao.get(BDCS_XZDZW_LS.class, bdcdyid);
		// if(u!=null)
		// return BDCDYLX.XZDZW;
		// u = (RealUnit)baseCommonDao.get(BDCS_MZDZW_LS.class, bdcdyid);
		// if(u!=null)
		// return BDCDYLX.MZDZW;
		u = (RealUnit) baseCommonDao.get(BDCS_H_LS.class, bdcdyid);
		if (u != null)
			return BDCDYLX.H;
		u = (RealUnit) baseCommonDao.get(BDCS_H_LSY.class, bdcdyid);
		if (u != null)
			return BDCDYLX.YCH;

		return null;
	}

	/**
	 * 根据不动产单元号获取单元信息列表,目录。
	 * 
	 * @作者：WUZHU
	 * @param bdcdyid
	 *            不动产单元号
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map GetML(String zdOrZHbdcdyid, long page, long pagesize) {
		HashMap<String, Object> bdcdyhMap = new HashMap<String, Object>();
		StringBuilder  mysql=new StringBuilder(" SELECT * FROM (");
		mysql.append(" SELECT BDCDYID,'031' AS BDCDYLX,BDCDYH FROM BDCK.BDCS_H_XZ WHERE ZDBDCDYID ='"+zdOrZHbdcdyid+"' ");
		mysql.append(" UNION ALL ");
		mysql.append(" SELECT BDCDYID,'032' AS BDCDYLX,BDCDYH FROM BDCK.BDCS_H_XZY FY LEFT JOIN BDCK.YC_SC_H_XZ GL ON FY.BDCDYID =GL.YCBDCDYID WHERE ZDBDCDYID ='"+zdOrZHbdcdyid+"' AND GL.SCBDCDYID IS NULL ");
		mysql.append(" UNION ALL ");
		mysql.append(" SELECT BDCDYID,'05' AS BDCDYLX,BDCDYH  FROM BDCK.BDCS_SLLM_XZ WHERE ZDBDCDYID ='"+zdOrZHbdcdyid+"' ");
		mysql.append(" UNION ALL ");
		mysql.append(" SELECT BDCDYID,'01' AS BDCDYLX,BDCDYH  FROM  BDCK.BDCS_SYQZD_XZ WHERE BDCDYID='"+zdOrZHbdcdyid+"' ");
		mysql.append(" UNION ALL ");
		mysql.append(" SELECT BDCDYID,'02' AS BDCDYLX,BDCDYH  FROM BDCK.BDCS_SHYQZD_XZ WHERE BDCDYID='"+zdOrZHbdcdyid+"' ");
		mysql.append(" UNION ALL ");
		mysql.append(" SELECT BDCDYID,'04' AS BDCDYLX,BDCDYH  FROM BDCK.BDCS_ZH_XZ WHERE BDCDYID='"+zdOrZHbdcdyid+"' ");
		mysql.append(" UNION ALL ");
		mysql.append(" SELECT BDCDYID,'09' AS BDCDYLX,BDCDYH  FROM BDCK.BDCS_NYD_XZ WHERE BDCDYID='"+zdOrZHbdcdyid+"' ");
		mysql.append(" ) T ORDER BY BDCDYLX,BDCDYID ");
		long start = 0;
		long end = 0;
		long index=1;
		start = (page - 1) * pagesize + 1;
		end = page * pagesize;
		index=start;
		String pagingSql = String
				.format(" SELECT * FROM (SELECT A.*, ROWNUM RN FROM (%s) A WHERE ROWNUM <= %s ) WHERE RN >= %s ",
						mysql.toString(), end, start);
			List<Map> DYList = baseCommonDao.getDataListByFullSql(pagingSql);
			for (int idy = 0; idy < DYList.size(); idy++) {
				bdcdyhMap.put("DJDY_" + "XH" + "_" + (idy + 1), index);
				bdcdyhMap.put("DJDY_" + "BDCDYH" + "_" + (idy + 1),
						DYList.get(idy).get("BDCDYH") != null ? DYList.get(idy)
								.get("BDCDYH").toString() : "");
				bdcdyhMap.put("DJDY_" + "BDCDYLX" + "_" + (idy + 1),
						ConstHelper.getNameByValue("BDCDYLX",
								DYList.get(idy).get("BDCDYLX") != null ? DYList
										.get(idy).get("BDCDYLX").toString()
										: ""));
				bdcdyhMap.put("DJDY_" + "SZBS" + "_" + (idy + 1),index);
				bdcdyhMap.put("DJDY_" + "BZ" + "_" + (idy + 1), "");
				index+=1;
			}
			return bdcdyhMap;
	}
	
	@Override
	public Blob getZDTImg(String bdcdyid) throws SQLException{
		StringBuffer sql = new StringBuffer("SELECT ZDTWJ FROM bdcdck.BDCS_SHYQZD_GZ T  WHERE T.BDCDYID ='");
		sql.append(bdcdyid).append("' ");
		List<Map> list = baseCommonDao.getDataListByFullSql(sql.toString());
		Blob b = null;
		if(list != null && list.size() > 0){
			Map map = list.get(0);
			Object object = (Object) map.get("ZDTWJ");
			b = (Blob) object;
			return b;
		}
		return null;
	}

	@Override
	public Blob getFDCImg(String bdcdyh) throws SQLException {
		StringBuffer sql = new StringBuffer("SELECT FCFHTWJ FROM BDCDCK.BDCS_H_GZ T  WHERE T.BDCDYH='");
		sql.append(bdcdyh).append("' ");
		List<Map> list = baseCommonDao.getDataListByFullSql(sql.toString());
		Blob b = null;
		if(list != null && list.size() > 0){
			Map map = list.get(0);
			Object object = (Object) map.get("FCFHTWJ");
			b = (Blob) object;
			return b;
		}
		return null;
	}

	

	
}
