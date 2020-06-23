package com.supermap.realestate.registration.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supemap.mns.common.HttpMethod;
import com.supermap.realestate.registration.ViewClass.HistoryUnitInfo;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.QueryClass.QLInfo;
import com.supermap.realestate.registration.ViewClass.UnitInfo;
import com.supermap.realestate.registration.ViewClass.UnitRelation;
import com.supermap.realestate.registration.model.*;
import com.supermap.realestate.registration.model.interfaces.*;
import com.supermap.realestate.registration.service.JYDJYTHDataSwapService;
import com.supermap.realestate.registration.service.QueryService;
import com.supermap.realestate.registration.tools.*;
import com.supermap.realestate.registration.util.*;
import com.supermap.realestate.registration.util.ConstValue.*;
import com.supermap.wisdombusiness.archives.web.common.Basic;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.service.common.Common;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Method;
import java.sql.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.Map.Entry;
/**
 * @author Administrator
 *
 */
@Service("queryService")
public class QueryServiceImpl implements QueryService {

	@Autowired
	protected CommonDao baseCommonDao; 
	@Autowired
	JYDJYTHDataSwapService jydjythDataSwapService;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Map> GetHInfo(String bdcdyid, String bdcdylx) {
		List<Map> list = new ArrayList();
		RealUnit Hunit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx),
				DJDYLY.LS, bdcdyid);
		if (Hunit == null) {
			Hunit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.GZ,
					bdcdyid);
		}
	
		if (Hunit != null && BDCDYLX.H.equals(BDCDYLX.initFrom(bdcdylx))
				|| BDCDYLX.YCH.equals(BDCDYLX.initFrom(bdcdylx))) {
			list.add(StringHelper.beanToMap(Hunit));
			House h = (House) Hunit;
			String zrzbdcdyid = h.getZRZBDCDYID();
			if (StringHelper.isEmpty(zrzbdcdyid)) {
				list.add(null);
			} else if (bdcdylx.equals(BDCDYLX.YCH.Value)) {
				RealUnit ZRZunit = UnitTools.loadUnit(BDCDYLX.YCZRZ,
						DJDYLY.initFrom("02"), zrzbdcdyid);
				
				list.add(StringHelper.beanToMap(ZRZunit));
			} else {
				RealUnit ZRZunit = UnitTools.loadUnit(BDCDYLX.ZRZ,
						DJDYLY.initFrom("02"), zrzbdcdyid);
				list.add(StringHelper.beanToMap(ZRZunit));
			}
			String zdbdcdyid = h.getZDBDCDYID();
			if (StringHelper.isEmpty(zdbdcdyid)) {
				list.add(null);
			} else {
				RealUnit ZDunit = UnitTools.loadUnit(BDCDYLX.SHYQZD,
						DJDYLY.initFrom("02"), zdbdcdyid);
				if (ZDunit == null) {
					ZDunit = UnitTools.loadUnit(BDCDYLX.SYQZD,
							DJDYLY.initFrom("02"), zdbdcdyid);
					list.add(StringHelper.beanToMap(ZDunit));
				} else {
					list.add(StringHelper.beanToMap(ZDunit));
				}
			}
		}
		else if (Hunit != null
				&& BDCDYLX.ZRZ.equals(BDCDYLX.initFrom(bdcdylx))
				|| BDCDYLX.YCZRZ.equals(BDCDYLX.initFrom(bdcdylx))){
			list.add(null);
		    list.add(StringHelper.beanToMap(Hunit));
		Building zrz = (Building) Hunit;
			String zdbdcdyid = zrz.getZDBDCDYID();
			if (StringHelper.isEmpty(zdbdcdyid)) {
				list.add(null);
			} else {
				RealUnit ZDunit = UnitTools.loadUnit(BDCDYLX.SHYQZD,
						DJDYLY.initFrom("02"), zdbdcdyid);
				if (ZDunit == null) {
					ZDunit = UnitTools.loadUnit(BDCDYLX.SYQZD,
							DJDYLY.initFrom("02"), zdbdcdyid);
					list.add(StringHelper.beanToMap(ZDunit));
				} else {
					list.add(StringHelper.beanToMap(ZDunit));
				}
			}
		}
		else if (Hunit != null
				&& BDCDYLX.SHYQZD.equals(BDCDYLX.initFrom(bdcdylx))
				|| BDCDYLX.SYQZD.equals(BDCDYLX.initFrom(bdcdylx))) {
			String result = getHouseMortgageInfoForZD(bdcdyid,bdcdylx);
			Map map = StringHelper.beanToMap(Hunit);
			map.put("fwinfo_diy", result);
			list.add(map);
		}
		else if(Hunit != null
				&& BDCDYLX.HY.equals(BDCDYLX.initFrom(bdcdylx))
				) {
			list.add(StringHelper.beanToMap(Hunit));
		}
		else if (Hunit != null && BDCDYLX.NYD.equals(BDCDYLX.initFrom(bdcdylx))) {
			Map map = StringHelper.beanToMap(Hunit);
			map.put("zdmj", map.get("mj"));
			map.put("shyqmj", map.get("cbmj"));
			list.add(map);
		}else {
			Hunit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx),
					DJDYLY.XZ, bdcdyid);

			list.add(StringHelper.beanToMap(Hunit));
			House h = (House) Hunit;
			String zrzbdcdyid = h.getZRZBDCDYID();
			if (StringHelper.isEmpty(zrzbdcdyid)) {
				list.add(null);
			} else if (bdcdylx.equals(BDCDYLX.YCH.Value)) {
				RealUnit ZRZunit = UnitTools.loadUnit(BDCDYLX.YCZRZ,
						DJDYLY.initFrom("02"), zrzbdcdyid);
				
				list.add(StringHelper.beanToMap(ZRZunit));
			} else {
				RealUnit ZRZunit = UnitTools.loadUnit(BDCDYLX.ZRZ,
						DJDYLY.initFrom("02"), zrzbdcdyid);
				list.add(StringHelper.beanToMap(ZRZunit));
			}
			String zdbdcdyid = h.getZDBDCDYID();
			if (StringHelper.isEmpty(zdbdcdyid)) {
				list.add(null);
			} else {
				RealUnit ZDunit = UnitTools.loadUnit(BDCDYLX.SHYQZD,
						DJDYLY.initFrom("02"), zdbdcdyid);
				if (ZDunit == null) {
					ZDunit = UnitTools.loadUnit(BDCDYLX.SYQZD,
							DJDYLY.initFrom("02"), zdbdcdyid);
					list.add(StringHelper.beanToMap(ZDunit));
				} else {
					list.add(StringHelper.beanToMap(ZDunit));
				}
			}
		
		}
		return list;
	}

	@Override
	public QLInfo GetQLInfo_XZ(String bdcdyid) {
		QLInfo qlinfo = new QLInfo();
		Rights ql = getQLInfo_XZ(bdcdyid);
		qlinfo.setql(ql);
		if (ql != null) {
			SubRights fsql = RightsTools.loadSubRightsByRightsID(DJDYLY.XZ,
					ql.getId());
			qlinfo.setfsql(fsql);
			List<RightsHolder> qlrlist = RightsHolderTools.loadRightsHolders(
					DJDYLY.XZ, ql.getId());
			qlinfo.setqlrlist(qlrlist);
		}
		return qlinfo;
	}

	@Override
	public QLInfo GetQLInfo_XZ1(String bdcdyid) {
		String sql = "select ycbdcdyid from  bdck.YC_SC_H_XZ  where scbdcdyid = '"+bdcdyid+"'";
		String ycbdcdyid = "";
		List<Map> ycbdcdyidlist =baseCommonDao.getDataListByFullSql(sql);
		if (ycbdcdyidlist != null && ycbdcdyidlist.size() > 0) {
			Map map = ycbdcdyidlist.get(0);
			ycbdcdyid = map.get("YCBDCDYID").toString();
		}
		QLInfo qlinfo = new QLInfo();
		Rights ql = getQLInfo_XZ(ycbdcdyid);
		qlinfo.setql(ql);
		if (ql != null) {
			SubRights fsql = RightsTools.loadSubRightsByRightsID(DJDYLY.XZ,
					ql.getId());
			if(fsql==null){
				fsql= RightsTools.loadSubRightsByRightsID(DJDYLY.LS,
						ql.getId());
			}
			qlinfo.setfsql(fsql);
			List<RightsHolder> qlrlist = RightsHolderTools.loadRightsHolders(
					DJDYLY.XZ, ql.getId());
			if(qlrlist.size()<1){
				qlrlist = RightsHolderTools.loadRightsHolders(
						DJDYLY.LS, ql.getId());
			}
			qlinfo.setqlrlist(qlrlist);
		}
		return qlinfo;
	}

	protected Rights getQLInfo_XZ(String bdcdyid) {
		Rights ql = null;
		//处理变更登记（单元未变）时的产权信息不显示问题
		List<BDCS_DJDY_GZ> djdys_gz = baseCommonDao.getDataList(
				BDCS_DJDY_GZ.class, "BDCDYID='" + bdcdyid + "'");
		String djdyid_xz = "";
		if (djdys_gz != null && djdys_gz.size() > 0) {
			djdyid_xz = djdys_gz.get(0).getDJDYID();
		}
		List<BDCS_DJDY_XZ> djdys_xz = baseCommonDao.getDataList(
				BDCS_DJDY_XZ.class, "DJDYID='" + djdyid_xz + "'");
		String bdcdyid_xz = "";
		if (djdys_xz != null && djdys_xz.size() > 0) {
			bdcdyid_xz = djdys_xz.get(0).getBDCDYID();
		}
		List<BDCS_DJDY_XZ> djdys = baseCommonDao.getDataList(
				BDCS_DJDY_XZ.class, "BDCDYID='" + bdcdyid_xz + "'");
		if(djdys == null || djdys.size() == 0){
			djdys = baseCommonDao.getDataList(
					BDCS_DJDY_XZ.class, "BDCDYID='" + bdcdyid + "'");
		}
		if (djdys != null && djdys.size() > 0) {
			String djdyid = djdys.get(0).getDJDYID();
			BDCDYLX lx = BDCDYLX.initFrom(djdys.get(0).getBDCDYLX());
			if (BDCDYLX.H.equals(lx) || BDCDYLX.YCH.equals(lx)) {
				List<Rights> qls = RightsTools.loadRightsByCondition(DJDYLY.XZ,
						"DJDYID='" + djdyid + "' and QLLX IN ('4','6','8')");
				if (qls != null && qls.size() > 0) {
					ql = qls.get(0);
				}
			} else if (BDCDYLX.SHYQZD.equals(lx) || BDCDYLX.SYQZD.equals(lx)) {
				List<Rights> qls = RightsTools
						.loadRightsByCondition(
								DJDYLY.XZ,
								"DJDYID='"
										+ djdyid
										+ "' and QLLX IN ('1','2','3','4','5','6','7','8')");
				if (qls != null && qls.size() > 0) {
					ql = qls.get(0);
				}
			} else if (BDCDYLX.LD.equals(lx)) {
				List<Rights> qls = RightsTools.loadRightsByCondition(DJDYLY.XZ,
						"DJDYID='" + djdyid + "' and QLLX IN ('10','11','12')");
				if (!StringHelper.isEmpty(qls) && qls.size() > 0) {
					ql = qls.get(0);
				}
			}
			else if (BDCDYLX.HY.equals(lx)) {
				List<Rights> qls = RightsTools.loadRightsByCondition(DJDYLY.XZ,
						"DJDYID='" + djdyid + "' and QLLX IN ('14','15','16','17','18')");
				if (!StringHelper.isEmpty(qls) && qls.size() > 0) {
					ql = qls.get(0);
				}
			}

		}
		return ql;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map GetAllList(String bdcdyid) {
		Map<String, List<Map>> resultmap = new HashMap<String, List<Map>>();
		List<Map> syqlist = new ArrayList();
		List<Map> dyqlist = new ArrayList();
		List<Map> cfdjlist = new ArrayList();
		List<Map> yydjlist = new ArrayList();
		List<String> listQLID = new ArrayList();
		List<BDCS_DJDY_LS> djdys = baseCommonDao.getDataList(
				BDCS_DJDY_LS.class, "BDCDYID='" + bdcdyid + "'");
		if (djdys != null && djdys.size() > 0) {
			String djdyid = djdys.get(0).getDJDYID();

			StringBuilder builderXZ = new StringBuilder();
			builderXZ.append("SELECT QLID FROM BDCK.BDCS_QL_XZ WHERE DJDYID='");
			builderXZ.append(djdyid);
			builderXZ.append("'");
			List<Map> xzqls = baseCommonDao.getDataListByFullSql(builderXZ
					.toString());
			if (xzqls != null && xzqls.size() > 0) {
				for (int iql = 0; iql < xzqls.size(); iql++) {
					listQLID.add(StringHelper.formatObject(xzqls.get(iql).get(
							"QLID")));
				}
			}
			StringBuilder builderLS = new StringBuilder();
			builderLS
					.append("SELECT QLID,YWH,DJLX,QLLX FROM BDCK.BDCS_QL_LS WHERE DJDYID='");
			builderLS.append(djdyid);
			builderLS.append("'");
			builderLS.append(" ORDER BY DJSJ");
			List<Map> lsqls = baseCommonDao.getDataListByFullSql(builderLS
					.toString());
			if (lsqls != null && lsqls.size() > 0) {
				for (int iql = 0; iql < lsqls.size(); iql++) {
					String qlid = StringHelper.formatObject(lsqls.get(iql).get(
							"QLID"));
					String qllx = StringHelper.formatObject(lsqls.get(iql).get(
							"QLLX"));
					String djlx = StringHelper.formatObject(lsqls.get(iql).get(
							"DJLX"));
					String ywh = StringHelper.formatObject(lsqls.get(iql).get(
							"YWH"));
					Map<String, String> map = new HashMap<String, String>();
					map.put("QLID", qlid);
					map.put("YWH", ywh);
					if (listQLID.contains(qlid)) {
						map.put("CANCLED", "false");
					} else {
						map.put("CANCLED", "true");
					}
					map.put("DJLX", DJLX.initFrom(djlx).Name);
					if (qllx.equals(QLLX.GYJSYDSHYQ_FWSYQ.Value)
							|| qllx.equals(QLLX.JTJSYDSYQ_FWSYQ.Value)
							|| qllx.equals(QLLX.ZJDSYQ_FWSYQ.Value)) {
						syqlist.add(map);
					} else if (qllx.equals(QLLX.QTQL.Value)) {
						cfdjlist.add(map);
					} else if (qllx.equals(QLLX.DIYQ.Value)) {
						dyqlist.add(map);
					} else if (djlx.equals(DJLX.YYDJ.Value)) {
						yydjlist.add(map);
					}
				}
			}
		}
		resultmap.put("syq", syqlist);
		resultmap.put("dyq", dyqlist);
		resultmap.put("cfxx", cfdjlist);
		resultmap.put("yyxx", yydjlist);
		return resultmap;
	}

	/**
	 * 根据不动产单元ID获取权利列表
	 * @Title: GetQLListEx
	 * @author:yuxuebin
	 * @param bdcdyid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<List<Map>> GetQLListEx(String bdcdyid) {
		List<List<Map>> list = new ArrayList<List<Map>>();
		String JudgeDJZ = ConfigHelper.getNameByValue("JudgeDJZ");// 值为SFDB,则判断是否项目办理中使用xmxx中SFDB判断；若值为SFBJ,则使用xmxx中的SFBJ判断
		if (StringHelper.isEmpty(JudgeDJZ)) {
			JudgeDJZ = "SFDB";
		}
		if (!StringHelper.isEmpty(bdcdyid)) {
			String djdyid = "";
			BDCDYLX dylx = null;
			List<BDCS_DJDY_LS> djdys_ls = baseCommonDao.getDataList(
					BDCS_DJDY_LS.class, "BDCDYID='" + bdcdyid + "'");
			if (djdys_ls != null && djdys_ls.size() > 0) {
				djdyid = djdys_ls.get(0).getDJDYID();
				dylx = BDCDYLX.initFrom(djdys_ls.get(0).getBDCDYLX());
			} else {
				List<BDCS_DJDY_GZ> djdys_gz = baseCommonDao.getDataList(
						BDCS_DJDY_GZ.class, "BDCDYID='" + bdcdyid + "'");
				if (djdys_gz != null && djdys_gz.size() > 0) {
					djdyid = djdys_gz.get(0).getDJDYID();
					dylx = BDCDYLX.initFrom(djdys_gz.get(0).getBDCDYLX());
					List<BDCS_DJDY_LS> djdys_ls2 = baseCommonDao.getDataList(
							BDCS_DJDY_LS.class, "DJDYID='" + djdyid + "'");
					if (djdys_ls2 != null && djdys_ls2.size() > 0) {
						bdcdyid=djdys_ls2.get(0).getBDCDYID();
					}
				}else{
					List<BDCS_H_XZY> ychs=baseCommonDao.getDataList(
							BDCS_H_XZY.class, "BDCDYID='" + bdcdyid + "'");
					if(ychs!=null&&ychs.size()>0){
						dylx = BDCDYLX.YCH;
					}
				}
			}
			HashMap<String, String> bdcdyid_djdyid = new HashMap<String, String>();
			HashMap<String, BDCDYLX> bdcdyid_bdcdylx = new HashMap<String, BDCDYLX>();
			if (BDCDYLX.H.equals(dylx)) {
				List<YC_SC_H_XZ> list_gx = baseCommonDao.getDataList(
						YC_SC_H_XZ.class, "SCBDCDYID='" + bdcdyid + "'");
				if (list_gx != null && list_gx.size() > 0) {
					for (YC_SC_H_XZ gx : list_gx) {
						String ycbdcdyid = gx.getYCBDCDYID();
						String ycdjdyid = "";
						if (StringHelper.isEmpty(ycbdcdyid)) {
							continue;
						}
						List<BDCS_DJDY_LS> ycdjdys_ls = baseCommonDao
								.getDataList(BDCS_DJDY_LS.class, "BDCDYID='"
										+ ycbdcdyid + "'");
						if (ycdjdys_ls != null && ycdjdys_ls.size() > 0) {
							ycdjdyid = ycdjdys_ls.get(0).getDJDYID();
						} else {
							List<BDCS_DJDY_GZ> ycdjdys_gz = baseCommonDao
									.getDataList(BDCS_DJDY_GZ.class,
											"BDCDYID='" + ycbdcdyid + "'");
							if (ycdjdys_gz != null && ycdjdys_gz.size() > 0) {
								ycdjdyid = ycdjdys_gz.get(0).getDJDYID();
							}
						}
						if (StringHelper.isEmpty(ycdjdyid)) {
							continue;
						}
						bdcdyid_djdyid.put(ycbdcdyid, ycdjdyid);
						bdcdyid_bdcdylx.put(ycbdcdyid, BDCDYLX.YCH);
					}
				}
				bdcdyid_djdyid.put(bdcdyid, djdyid);
				bdcdyid_bdcdylx.put(bdcdyid, dylx);
			} else if (BDCDYLX.YCH.equals(dylx)) {
				bdcdyid_djdyid.put(bdcdyid, djdyid);
				bdcdyid_bdcdylx.put(bdcdyid, dylx);
				List<YC_SC_H_XZ> list_gx = baseCommonDao.getDataList(
						YC_SC_H_XZ.class, "YCBDCDYID='" + bdcdyid + "'");
				if (list_gx != null && list_gx.size() > 0) {
					for (YC_SC_H_XZ gx : list_gx) {
						String scbdcdyid = gx.getSCBDCDYID();
						String scdjdyid = "";
						if (StringHelper.isEmpty(scbdcdyid)) {
							continue;
						}
						List<BDCS_DJDY_LS> scdjdys_ls = baseCommonDao
								.getDataList(BDCS_DJDY_LS.class, "BDCDYID='"
										+ scbdcdyid + "'");
						if (scdjdys_ls != null && scdjdys_ls.size() > 0) {
							scdjdyid = scdjdys_ls.get(0).getDJDYID();
						} else {
							List<BDCS_DJDY_GZ> scdjdys_gz = baseCommonDao
									.getDataList(BDCS_DJDY_GZ.class,
											"BDCDYID='" + scbdcdyid + "'");
							if (scdjdys_gz != null && scdjdys_gz.size() > 0) {
								scdjdyid = scdjdys_gz.get(0).getDJDYID();
							}
						}
						if (StringHelper.isEmpty(scdjdyid)) {
							continue;
						}
						bdcdyid_djdyid.put(scbdcdyid, scdjdyid);
						bdcdyid_bdcdylx.put(scbdcdyid, BDCDYLX.H);
					}
				}
			} else {
				bdcdyid_djdyid.put(bdcdyid, djdyid);
				bdcdyid_bdcdylx.put(bdcdyid, dylx);
			}
			List<Map> qlinfo_list_all = new ArrayList<Map>();
			List<Map> housebook_list_all = new ArrayList<Map>();
			List<String> list_ywh_blz=new ArrayList<String>();
			for (Entry<String, String> m : bdcdyid_djdyid.entrySet()) {
				List<String> list_ywh =GetYWHListInBLZ(m.getValue(), bdcdyid_bdcdylx.get(m.getKey()), JudgeDJZ);
				for (String ywh : list_ywh) {
					list_ywh_blz.add(ywh);
				}
			}
			
			for (Entry<String, String> m : bdcdyid_djdyid.entrySet()) {
				List<HashMap<String, String>> qlinfo_list = GetRightInfoList(
						m.getValue(), bdcdyid_bdcdylx.get(m.getKey()), JudgeDJZ,list_ywh_blz);
				for (HashMap<String, String> qlinfo : qlinfo_list) {
					qlinfo_list_all.add(qlinfo);
				}
				List<Map> housebook_list = getHouseBookList(bdcdyid, dylx);
				for (Map housebook : housebook_list) {
					housebook_list_all.add(housebook);
				}
			}
			//历史产权列表，增加单元限制登记信息
			List<Map> dyxzs = baseCommonDao.getDataListByFullSql(" select (select consttrans from bdck.bdcs_const con "
					+ " where con.constvalue = dyxz.xzlx and constslsid='67' ) as xzlxname,to_char(dyxz.djsj, 'yyyy-mm-dd') "
					+ " as djsjname,dyxz.* from BDCK.BDCS_DYXZ dyxz where dyxz.BDCDYID='" + bdcdyid + "' AND dyxz.YXBZ IN ('1','2')");
			list.add(dyxzs);
			qlinfo_list_all = SortRightInfoList(qlinfo_list_all);
			list.add(qlinfo_list_all);
			list.add(housebook_list_all);
		}

		return list;
	}
	
	/**
	 * 根据登记单元ID获取权利列表
	 * 
	 * @Title: GetRightInfoList
	 * @author:yuxuebin
	 * @param djdyid
	 * @param dylx
	 * @param JudgeDJZ
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected List<String> GetYWHListInBLZ(String djdyid,
			BDCDYLX dylx, String JudgeDJZ) {
		List<String> list_ywh_blz=new ArrayList<String>();
		if (StringHelper.isEmpty(djdyid)) {
			return list_ywh_blz;
		}
		StringBuilder builderGZ = new StringBuilder();
		builderGZ
				.append("SELECT QL.XMBH,QL.QLID,QL.YWH,QL.DJLX,QL.QLLX,QL.DJSJ ");
		builderGZ.append("FROM BDCK.BDCS_QL_GZ QL ");
		builderGZ.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON QL.XMBH=XMXX.XMBH ");
		builderGZ.append("WHERE DJDYID='");
		builderGZ.append(djdyid);
		builderGZ.append("' ");
		builderGZ.append("AND (NVL2(XMXX.");
		builderGZ.append(JudgeDJZ);
		builderGZ.append(",1,0)=0 OR XMXX.");
		builderGZ.append(JudgeDJZ);
		builderGZ.append("<>'1') ");
		builderGZ.append("ORDER BY DJSJ");
		List<Map> gzqls = baseCommonDao.getDataListByFullSql(builderGZ
				.toString());
		if (gzqls != null && gzqls.size() > 0) {
			for (int iql = 0; iql < gzqls.size(); iql++) {
				String qlid = StringHelper.formatObject(gzqls.get(iql).get(
						"QLID"));
				String qllx = StringHelper.formatObject(gzqls.get(iql).get(
						"QLLX"));
				String xmbh = StringHelper.formatObject(gzqls.get(iql).get(
						"XMBH"));
				if (!StringHelper.isEmpty(xmbh)) {
					ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
					Object objDJSJ = gzqls.get(iql).get("DJSJ");
					String djlx = info.getDjlx();
					String ywh = "";
					if (info != null) {
						if (StringHelper.isEmpty(info.getYwlsh())) {
							ywh = info.getProject_id();
						} else {
							ywh = info.getYwlsh();
						}
					}
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("QLID", qlid);
					map.put("SLSJ", StringHelper.FormatDateOnType(info.getSlsj(),
							"yyyy-MM-dd HH-mm-ss"));
					map.put("YWH", ywh);
					map.put("DJZT", "DJZ");
					String djsj_value = StringHelper.FormatDateOnType(objDJSJ,
							"yyyy-MM-dd HH-mm-ss");
					map.put("DJSJ", djsj_value);
					String djsj = StringHelper.FormatDateOnType(objDJSJ,
							"yyyy年MM月dd日");
					String ywlx = GetYWLX(djlx, qllx, dylx);
					String title = ywlx + "(" + ywh + ")-(" + djsj + ")";// 业务类型（业务号）-（登记时间）
					title = String.format(title, ywlx, ywh, djsj);
					map.put("TITLE", title);
					String qlType = GetQLType(djlx, qllx);
					map.put("QLTYPE", qlType);
					list_ywh_blz.add(info.getProject_id());
				}
			}
		}
		return list_ywh_blz;
	}

	/**
	 * 根据登记单元ID获取权利列表
	 * 
	 * @Title: GetRightInfoList
	 * @author:yuxuebin
	 * @param djdyid
	 * @param dylx
	 * @param JudgeDJZ
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected List<HashMap<String, String>> GetRightInfoList(String djdyid,
			BDCDYLX dylx, String JudgeDJZ,List<String> list_ywh_blz) {
		List<HashMap<String, String>> list_qlinfo = new ArrayList<HashMap<String, String>>();
		if (StringHelper.isEmpty(djdyid)) {
			return list_qlinfo;
		}
		
		StringBuilder builderLS = new StringBuilder();
		builderLS
				.append("SELECT QLID,YWH,DJLX,QLLX,DJSJ,CASE WHEN EXISTS(SELECT 1 FROM BDCK.BDCS_QL_XZ XZQL WHERE XZQL.QLID=LSQL.QLID) THEN 'YGD' ELSE 'YZX' END AS DJZT FROM BDCK.BDCS_QL_LS LSQL WHERE DJDYID='");
		builderLS.append(djdyid);
		builderLS.append("'");
		builderLS.append(" ORDER BY DJSJ");
		List<Map> lsqls = baseCommonDao.getDataListByFullSql(builderLS
				.toString());
		if (lsqls != null && lsqls.size() > 0) {
			for (int iql = 0; iql < lsqls.size(); iql++) {
				String qlid = StringHelper.formatObject(lsqls.get(iql).get(
						"QLID"));
				String qllx = StringHelper.formatObject(lsqls.get(iql).get(
						"QLLX"));
				String djlx = StringHelper.formatObject(lsqls.get(iql).get(
						"DJLX"));
				String project_id = StringHelper.formatObject(lsqls.get(iql)
						.get("YWH"));
				String DJZT = StringHelper.formatObject(lsqls.get(iql).get(
						"DJZT"));
				//归档状态维护
				Map<String, String>  mapGD=new HashMap<String, String>();
				mapGD.put("PROJECT_ID",project_id );
//				Boolean flag = true;
//				try {
//					com.supemap.mns.model.Message mesg=Basic.archiveRequest("search/sfgd",mapGD,HttpMethod.GET);
//					JSONObject jsonObject= JSONObject.parseObject(mesg.getMessageBodyAsString());
//					if (jsonObject != null && jsonObject.containsKey("success")) {
//						Object success =jsonObject.get("success");
//						if(success.equals(true)) {
//							DJZT="YGD" ;
//						}if(success.equals(false)) {
//							DJZT="DGD" ;
//						}
//					} else {
//						flag = false;
//						throw new Exception("调用是否归档查询接口异常！请检查本地化配置“档案归档类”的“dossierservice”接口地址配置是否正确。");
//					}
//				} catch (Exception e) {
//					flag = false;
//					e.printStackTrace();
//				}
			Object objDJSJ = lsqls.get(iql).get("DJSJ");
			String ywh = "";
			if (!StringHelper.isEmpty(project_id)) {
				BDCS_XMXX XMXX = Global.getXMXX(project_id);
				if (XMXX != null) {
					if (("SFDB").equals(JudgeDJZ)
							&& !("1").equals(XMXX.getSFDB())) {
						continue;
					} else if (("SFBJ").equals(JudgeDJZ)
							&& !("1").equals(XMXX.getSFBJ())) {
						continue;
					}
					ywh = XMXX.getYWLSH();
					if (StringHelper.isEmpty(ywh)) {
						ywh = project_id;
					}
				} else {
					ywh = project_id;
				}
			}
			SubRights lsfsql = RightsTools.loadSubRightsByRightsID(
					DJDYLY.LS, qlid);
			if (lsfsql != null) {
				String ZXYWH = lsfsql.getZXDYYWH();
				if (!StringHelper.isEmpty(ZXYWH)) {
					BDCS_XMXX XMXX = Global.getXMXX(ZXYWH);
					if (XMXX != null) {
						if (("SFDB").equals(JudgeDJZ)
								&& !("1").equals(XMXX.getSFDB())) {
							continue;
						} else if (("SFBJ").equals(JudgeDJZ)
								&& !("1").equals(XMXX.getSFBJ())) {
								if(list_ywh_blz.contains(XMXX.getPROJECT_ID())){
									DJZT="YGD";
								}else if(list_ywh_blz.contains(XMXX.getPROJECT_ID()) && !StringHelper.isEmpty(ZXYWH)){
									DJZT="YZX";
								}else{
									continue;
								}
							}
						}
					}
				} else {
					continue;
				}
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("QLID", qlid);
				map.put("YWH", ywh);
				map.put("ZXYWH", lsfsql.getZXDYYWH());
				String djsj_value = StringHelper.FormatDateOnType(objDJSJ,
						"yyyy-MM-dd HH-mm-ss");
				String djsj = StringHelper.FormatDateOnType(objDJSJ,
						"yyyy年MM月dd日");
				map.put("DJSJ", djsj_value);
				map.put("DJZT", DJZT);

				String ywlx = GetYWLX(djlx, qllx, dylx);
				if("在建工程抵押登记".equals(ywlx)){
					Rights ls = RightsTools.loadRights(DJDYLY.LS, qlid);
					if(ls!=null){
						String lyql = ls.getLYQLID();
						if(StringHelper.isEmpty(lyql)){
							//nothing to do
						}else{
							Rights lsbyls = RightsTools.loadRights(DJDYLY.LS, ls.getLYQLID());
							if(QLLX.DIYQ.Value.equals(lsbyls.getQLLX())&&DJLX.YGDJ.Value.equals(lsbyls.getDJLX())){
								ywlx = "期房抵押预告登记";
							}
						}
					}
				}
				String fulSql = "SELECT DISTINCT PRO.PRODEF_NAME,BASE.DJLX FROM BDCK.BDCS_QL_LS LSQL INNER JOIN BDC_WORKFLOW.WFI_PROINST PRO ON PRO.FILE_NUMBER=LSQL.YWH "
						+ " INNER JOIN BDC_WORKFLOW.WFD_MAPPING M ON M.WORKFLOWCODE = PRO.PROINST_CODE "
						+ " INNER JOIN BDCK.T_BASEWORKFLOW BASE ON BASE.ID=M.WORKFLOWNAME "
						+ " WHERE LSQL.YWH IS NOT NULL AND LSQL.QLLX=23 AND PRO.FILE_NUMBER IS NOT NULL "
						+ " AND (BASE.DJLX='500' OR BASE.DJLX='300') AND BASE.QLLX='23' AND BASE.UNITTYPE='032' AND LSQL.QLID=''{0}''";
				fulSql = MessageFormat.format(fulSql, qlid);
				List<Map> list = baseCommonDao.getDataListByFullSql(fulSql);
				if(list!=null&&list.size()>0&&ywlx.indexOf("-")==-1){
					ywlx = DJLX.initFrom(list.get(0).get("DJLX")+"").Name + "-"  + ywlx;
				}else if(ywlx.indexOf("-")==-1){
					if(StringHelper.isEmpty(djlx)){
						ywlx = "-----"+ywlx;
					}else{
						ywlx = DJLX.initFrom(djlx).Name + "-"  + ywlx;
					}

				}

				String title = ywlx + "(" + ywh + ")-(" + djsj + ")";// 业务类型（业务号）-（登记时间）
				map.put("TITLE", title);
				String qlType = GetQLType(djlx, qllx);
				map.put("QLTYPE", qlType);
				list_qlinfo.add(map);
			}
		}
		
		StringBuilder builderGZ = new StringBuilder();
		builderGZ
				.append("SELECT QL.XMBH,QL.QLID,QL.YWH,QL.DJLX,QL.QLLX,QL.DJSJ,XMXX.YXBZ ");
		builderGZ.append("FROM BDCK.BDCS_QL_GZ QL ");
		builderGZ.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON QL.XMBH=XMXX.XMBH ");
		builderGZ.append("WHERE DJDYID='");
		builderGZ.append(djdyid);
		builderGZ.append("' ");
		builderGZ.append("AND (NVL2(XMXX.");
		builderGZ.append(JudgeDJZ);
		builderGZ.append(",1,0)=0 OR XMXX.");
		builderGZ.append(JudgeDJZ);
		builderGZ.append("<>'1') ");
		builderGZ.append("ORDER BY DJSJ");
		List<Map> gzqls = baseCommonDao.getDataListByFullSql(builderGZ
				.toString());
		if (gzqls != null && gzqls.size() > 0) {
			for (int iql = 0; iql < gzqls.size(); iql++) {
				String qlid = StringHelper.formatObject(gzqls.get(iql).get(
						"QLID"));
				String qllx = StringHelper.formatObject(gzqls.get(iql).get(
						"QLLX"));
				String xmbh = StringHelper.formatObject(gzqls.get(iql).get(
						"XMBH"));
				String xmyxbz = StringHelper.formatObject(gzqls.get(iql).get(
						"YXBZ"));
				if (!StringHelper.isEmpty(xmbh)) {
				ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
				Object objDJSJ = gzqls.get(iql).get("DJSJ");
				String djlx = info.getDjlx();
				String ywh = "";
				if (info != null) {
					if (StringHelper.isEmpty(info.getYwlsh())) {
						ywh = info.getProject_id();
					} else {
						ywh = info.getYwlsh();
					}
				}
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("QLID", qlid);
				map.put("SLSJ", StringHelper.FormatDateOnType(info.getSlsj(),
						"yyyy-MM-dd HH-mm-ss"));
				map.put("YWH", ywh);
				map.put("DJZT", "DJZ");
				if(YXBZ.无效.Value.equals(xmyxbz)){
					map.put("DJZT", "YTJ");
				}
				String djsj_value = StringHelper.FormatDateOnType(objDJSJ,
						"yyyy-MM-dd HH-mm-ss");
				map.put("DJSJ", djsj_value);
				String djsj = StringHelper.FormatDateOnType(objDJSJ,
						"yyyy年MM月dd日");
				String slsj = StringHelper.FormatDateOnType(info.getSlsj(),
						"yyyy年MM月dd日");
				String ywlx = GetYWLX(djlx, qllx, dylx);
				if("在建工程抵押登记".equals(ywlx)){
					Rights ls = RightsTools.loadRights(DJDYLY.GZ, qlid);
					if(ls!=null){
						String lyql = ls.getLYQLID();
						if(StringHelper.isEmpty(lyql)){
							//nothing to do
						}else{
							Rights lsbyls = RightsTools.loadRights(DJDYLY.LS, ls.getLYQLID());
							if(QLLX.DIYQ.Value.equals(lsbyls.getQLLX())&&DJLX.YGDJ.Value.equals(lsbyls.getDJLX())){
								ywlx = "期房抵押预告登记";
							}
						}
					}
				}
				String fulSql = "SELECT DISTINCT PRO.PRODEF_NAME,BASE.DJLX FROM BDCK.BDCS_QL_LS LSQL INNER JOIN BDC_WORKFLOW.WFI_PROINST PRO ON PRO.FILE_NUMBER=LSQL.YWH "
						+ " INNER JOIN BDC_WORKFLOW.WFD_MAPPING M ON M.WORKFLOWCODE = PRO.PROINST_CODE "
						+ " INNER JOIN BDCK.T_BASEWORKFLOW BASE ON BASE.ID=M.WORKFLOWNAME "
						+ " WHERE LSQL.YWH IS NOT NULL AND LSQL.QLLX=23 AND PRO.FILE_NUMBER IS NOT NULL "
						+ " AND (BASE.DJLX='500' OR BASE.DJLX='300') AND BASE.QLLX='23' AND BASE.UNITTYPE='032' AND LSQL.QLID=''{0}''";
				fulSql = MessageFormat.format(fulSql, qlid);
				List<Map> list = baseCommonDao.getDataListByFullSql(fulSql);
				if(list!=null&&list.size()>0&&ywlx.indexOf("-")==-1){
					ywlx = DJLX.initFrom(list.get(0).get("DJLX")+"").Name + "-"  + ywlx;
				}else if(ywlx.indexOf("-")==-1){
					if(StringHelper.isEmpty(djlx)){
						ywlx = "-----"+ywlx;
					}else{
						ywlx = DJLX.initFrom(djlx).Name + "-"  + ywlx;
					}
					
				}
				
				String title = ywlx + "(" + ywh + ")-(" + slsj + ")";// 业务类型（业务号）-（登记时间）
				title = String.format(title, ywlx, ywh, slsj);
				map.put("TITLE", title);
				String qlType = GetQLType(djlx, qllx);
				map.put("QLTYPE", qlType);
				list_qlinfo.add(map);
			}
			}
		}
		return list_qlinfo;
	}

	/**
	 * 获取业务类型
	 * 
	 * @Title: GetYWLX
	 * @author:yuxuebin
	 * @param djlx
	 * @param qllx
	 * @param dylx
	 * @return
	 */
	protected String GetYWLX(String djlx, String qllx, BDCDYLX dylx) {
		String ywlx = "";
		if (BDCDYLX.YCH.equals(dylx)) {
			if (DJLX.CFDJ.Value.equals(djlx) || DJLX.YYDJ.Value.equals(djlx)) {
				if(StringHelper.isEmpty(DJLX.initFrom(djlx))){
					ywlx = "----" + "-期房";
				}else{
					ywlx = DJLX.initFrom(djlx).Name + "-期房";
				}
			} else {
				if (QLLX.DIYQ.Value.equals(qllx)) {
					if(DJLX.YGDJ.Value.equals(djlx)){
						ywlx = "期房抵押预告登记";
					}else{
						ywlx = "在建工程抵押登记";
					}
					
				} else {
					ywlx = "期房预告登记";
				}
			}
		} else {
			if (DJLX.CFDJ.Value.equals(djlx) || DJLX.YYDJ.Value.equals(djlx)) {
				if (DJLX.CFDJ.Value.equals(djlx) && "98".equals(qllx)) {
					ywlx = "解封登记";
				} else {
					ywlx = DJLX.initFrom(djlx).Name;
				}
			} else {
				if (DJLX.YGDJ.Value.equals(djlx) && "99".equals(qllx)) {
					ywlx = "转移预告登记";
				} else if (DJLX.YGDJ.Value.equals(djlx)
						&& QLLX.DIYQ.Value.equals(qllx)) {
					ywlx = "抵押预告登记";
				} else if (QLLX.GYJSYDSHYQ.Value.equals(qllx)
						|| QLLX.ZJDSYQ.Value.equals(qllx)
						|| QLLX.JTJSYDSYQ.Value.equals(qllx)) {
					if(StringHelper.isEmpty(DJLX.initFrom(djlx))){
						ywlx = "----" + "-使用权";
					}else{
						if(djlx != null && !djlx.equals("")){
						   ywlx = DJLX.initFrom(djlx).Name + "-使用权";
						}
					}
				} else if (QLLX.JTTDSYQ.Value.equals(qllx)
						|| QLLX.GJTDSYQ.Value.equals(qllx)
						|| QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(qllx)
						|| QLLX.ZJDSYQ_FWSYQ.Value.equals(qllx)
						|| QLLX.JTJSYDSYQ_FWSYQ.Value.equals(qllx)) {
					if(StringHelper.isEmpty(DJLX.initFrom(djlx))){
						ywlx = "----" + "-所有权";
					}else{
						if(djlx != null && !djlx.equals("")){
							ywlx = DJLX.initFrom(djlx).Name + "-所有权";
						}	
					}
				} else if (QLLX.DIYQ.Value.equals(qllx)) {
					if(StringHelper.isEmpty(DJLX.initFrom(djlx))){
						ywlx = "----" + "-抵押权";
					}else{
						if(djlx != null && !djlx.equals("")){
						  ywlx = DJLX.initFrom(djlx).Name + "-抵押权";
						}
					}
				}else if ((DJLX.ZYDJ.Value.equals(djlx)||DJLX.BGDJ.Value.equals(djlx))
						&& !QLLX.DIYQ.Value.equals(qllx)) {
					if ("99".equals(qllx)
							&& DJLX.ZYDJ.Value.equals(djlx)) {
						ywlx = "转移预告转移登记";
					}else if ("99".equals(qllx)
							&& DJLX.BGDJ.Value.equals(djlx)) {
						ywlx = "转移预告变更登记";
					}
				} else {
					if(StringHelper.isEmpty(DJLX.initFrom(djlx))){
						ywlx = "----" ;
					}else{
						if(djlx != null && !djlx.equals("")){
						  ywlx = DJLX.initFrom(djlx).Name;
						}
					}
					if(StringHelper.isEmpty(QLLX.initFrom(qllx))){
						ywlx = ywlx+"-"+"----" ;
					}else{
						ywlx = ywlx+QLLX.initFrom(qllx).Name;
					}
				}
			}
		}
		return ywlx;
	}

	/**
	 * 获取权利类型
	 * 
	 * @Title: GetQLType
	 * @author:yuxuebin
	 * @param djlx
	 * @param qllx
	 * @return
	 */
	protected String GetQLType(String djlx, String qllx) {
		String type = "SYQ";
		if (QLLX.DIYQ.Value.equals(qllx)) {
			type = "DYQ";
		} else if (DJLX.CFDJ.Value.equals(djlx)) {
			type = "CFDJ";
		} else if (DJLX.YYDJ.Value.equals(djlx)) {
			type = "YYDJ";
		}
		return type;
	}

	/**
	 * 获取历史登记过程
	 * 
	 * @Title: getHouseBookList
	 * @author:yuxuebin
	 * @param bdcdyid
	 * @param dylx
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected List<Map> getHouseBookList(String bdcdyid, BDCDYLX dylx) {
		List<Map> list = new ArrayList<Map>();
		String sql = " FROM DBA_TABLES WHERE TABLE_NAME=UPPER('HOUSEBOOK') AND OWNER='BDCK'";
		long count = baseCommonDao.getCountByFullSql(sql);
		if (count > 0) {
			if (!StringHelper.isEmpty(dylx)) {
				RealUnit unit = UnitTools.loadUnit(dylx, DJDYLY.LS, bdcdyid);
				if (unit != null) {
					String relationid = unit.getRELATIONID();
					if (!StringHelper.isEmpty(relationid)) {
						String sql2 = MessageFormat
								.format("select * from bdck.housebook where bldroomid=''{0}'' order by commitdatetime",
										relationid);
						List<Map> listmap = baseCommonDao
								.getDataListByFullSql(sql2);
						if (listmap != null && listmap.size() > 0) {
							for (int j = 0; j < listmap.size(); j++) {
								Map m = listmap.get(j);
								Object o = m.get("COMMITDATETIME");
								if (o != null) {
									String createdate = DateUtil
											.FormatByDatetime(o);
									m.remove("COMMITDATETIME");
									m.put("COMMITDATETIME", createdate);
								}
								Object o2 = m.get("CREATEDATE");
								if (o2 != null) {
									String createdate = DateUtil
											.FormatByDatetime(o2);
									m.remove("CREATEDATE");
									m.put("CREATEDATE", createdate);
								}
								list.add(m);
							}

						}

					}
				}
			}
		}
		Collections.reverse(list);
		return list;
	}

	/**
	 * 对权利列表进行排序
	 * 
	 * @Title: SortRightInfoList
	 * @author:yuxuebin
	 * @param qlinfo_list_all
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected List<Map> SortRightInfoList(List<Map> qlinfo_list_all) {
		// 查询登记过程按照时间排序 ，正在进行的放在最上面，没时间的放在最下面
		if (qlinfo_list_all != null && qlinfo_list_all.size() > 0) {
			// 2.对不为空的进行排序
			final SimpleDateFormat sdf1 = new SimpleDateFormat(
					"yyyy-MM-dd HH-mm-ss");
			Collections.sort(qlinfo_list_all, new Comparator<Map>() {
				@Override
				public int compare(Map mapi, Map mapj) {
					java.util.Date datei = null;
					java.util.Date datej = null;
					String djzti = StringHelper.formatObject(mapi.get("DJZT"));
					String djztj = StringHelper.formatObject(mapj.get("DJZT"));
					try {
						if (!StringHelper.isEmpty(mapi.get("DJSJ"))) {
							datei = sdf1.parse(mapi.get("DJSJ").toString());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						if (!StringHelper.isEmpty(mapj.get("DJSJ"))) {
							datej = sdf1.parse(mapj.get("DJSJ").toString());
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
					List<String> judge_slsj_zt=new ArrayList<String>();
					judge_slsj_zt.add("DJZ");
					judge_slsj_zt.add("YTJ");
					if (!djzti.equals(djztj)) {
						if (judge_slsj_zt.contains(djzti)&&!judge_slsj_zt.contains(djztj)) {
							return -1;
						}
						if (judge_slsj_zt.contains(djztj)&&!judge_slsj_zt.contains(djzti)) {
							return 1;
						}
					}
					if (judge_slsj_zt.contains(djzti)&&judge_slsj_zt.contains(djztj)) {
						java.util.Date slsji = null;
						java.util.Date slsjj = null;
						try {
							if (!StringHelper.isEmpty(mapi.get("SLSJ")
									.toString())) {
								slsji = sdf1.parse(mapi.get("SLSJ").toString());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							if (!StringHelper.isEmpty(mapj.get("SLSJ")
									.toString())) {
								slsjj = sdf1.parse(mapj.get("SLSJ").toString());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (StringHelper.isEmpty(slsji)
								|| (!StringHelper.isEmpty(datej) 
								&& (StringHelper.isEmpty(datei)||datei.before(slsjj)))) {
							return 1;
						} else if (!StringHelper.isEmpty(datei)&&datei.equals(slsjj)) {
							return 0;
						}
						return -1;
					}
					if (StringHelper.isEmpty(datei)
							|| (!StringHelper.isEmpty(datej) && datei
									.before(datej))) {
						return 1;
					} else if (datei.equals(datej)) {
						return 0;
					}
					return -1;
				}
			});
		}
		return qlinfo_list_all;
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	@Override
	public List<List<Map>> GetQLList(String ycscbdcdyid) {
		if (true) {
			return GetQLListEx(ycscbdcdyid);
		}
		// QLID
		// DJZT
		// QLLX
		// DJLX
		// QLLXEX
		// YWH
		// DJSJ
		// BASEACTIVITYNAME
		// COMMITDATETIME
		// PEOPLENAME
		// RPNAME
		List<List<Map>> result = new ArrayList<List<Map>>();
		List<Map> qllist = new ArrayList();
		List<Map> newqllist = new ArrayList();
		List<Map> lastqllist = new ArrayList();
		List<String> listQLID = new ArrayList();
		String djdyid = "";
		String bdcdylx = "";
		List<Map> listmap = null;
		List<Map> listmapnew = null;
		String JudgeDJZ = ConfigHelper.getNameByValue("JudgeDJZ");// 值为SFDB,则判断是否项目办理中使用xmxx中SFDB判断；若值为SFBJ,则使用xmxx中的SFBJ判断
		if (StringHelper.isEmpty(JudgeDJZ))
			JudgeDJZ = "SFDB";
		if (ycscbdcdyid != null && ycscbdcdyid != "") {
			String[] bdcdyids = ycscbdcdyid.split(",");
			for (int i = 0; i < bdcdyids.length; i++) {

				List<BDCS_DJDY_LS> djdys_ls = baseCommonDao.getDataList(
						BDCS_DJDY_LS.class, "BDCDYID='" + bdcdyids[i] + "'");
				// 查询历史权利信息
				// 步骤： 1、先从现状权利层查询QLID,放入listQLID列表
				// 2、再从历史权利层查询QLID，放入到lsqls列表里边，并且判断，如果在现状权利ID中存在，就标记CANCELED为false，如果不存在，标记为true
				// 3、
				if (djdys_ls != null && djdys_ls.size() > 0) {
					djdyid = djdys_ls.get(0).getDJDYID();
					bdcdylx = djdys_ls.get(0).getBDCDYLX();
				} else {
					List<BDCS_DJDY_GZ> djdys_gz = baseCommonDao
							.getDataList(BDCS_DJDY_GZ.class, "BDCDYID='"
									+ bdcdyids[i] + "'");
					if (djdys_gz != null && djdys_gz.size() > 0) {
						djdyid = djdys_gz.get(0).getDJDYID();
						bdcdylx = djdys_gz.get(0).getBDCDYLX();
					}
				}
				if (!StringHelper.isEmpty(djdyid)) {
					StringBuilder builderXZ = new StringBuilder();
					builderXZ
							.append("SELECT QLID FROM BDCK.BDCS_QL_XZ WHERE DJDYID='");
					builderXZ.append(djdyid);
					builderXZ.append("'");
					List<Map> xzqls = baseCommonDao
							.getDataListByFullSql(builderXZ.toString());
					if (xzqls != null && xzqls.size() > 0) {
						for (int iql = 0; iql < xzqls.size(); iql++) {
							listQLID.add(StringHelper.formatObject(xzqls.get(
									iql).get("QLID")));
						}
					}
					StringBuilder builderLS = new StringBuilder();
					builderLS
							.append("SELECT QLID,YWH,DJLX,QLLX,DJSJ FROM BDCK.BDCS_QL_LS WHERE DJDYID='");
					builderLS.append(djdyid);
					builderLS.append("'");
					builderLS.append(" ORDER BY DJSJ");
					List<Map> lsqls = baseCommonDao
							.getDataListByFullSql(builderLS.toString());
					List<Map<String, String>> lsqls2 = new ArrayList<Map<String, String>>();

					if (lsqls != null && lsqls.size() > 0) {
						for (int iql = 0; iql < lsqls.size(); iql++) {
							String qlid = StringHelper.formatObject(lsqls.get(
									iql).get("QLID"));
							String qllx = StringHelper.formatObject(lsqls.get(
									iql).get("QLLX"));
							String djlx = StringHelper.formatObject(lsqls.get(
									iql).get("DJLX"));
							String project_id = StringHelper.formatObject(lsqls
									.get(iql).get("YWH"));
							String ywh = "";
							if (!StringHelper.isEmpty(project_id)) {
								BDCS_XMXX XMXX = Global.getXMXX(project_id);
								if (XMXX != null) {
									if (("SFDB").equals(JudgeDJZ)
											&& !("1").equals(XMXX.getSFDB())) {
										continue;
									} else if (("SFBJ").equals(JudgeDJZ)
											&& !("1").equals(XMXX.getSFBJ())) {
										continue;
									}
									ywh = XMXX.getYWLSH();
									if (StringHelper.isEmpty(ywh)) {
										ywh = project_id;
									}
								} else {
									ywh = project_id;
								}
							}
							SubRights lsfsql = RightsTools
									.loadSubRightsByRightsID(DJDYLY.LS, qlid);
							if (lsfsql != null) {
								String ZXYWH = lsfsql.getZXDYYWH();
								if (!StringHelper.isEmpty(ZXYWH)) {
									BDCS_XMXX XMXX = Global.getXMXX(ZXYWH);
									if (XMXX != null) {
										if (("SFDB").equals(JudgeDJZ)
												&& !("1")
														.equals(XMXX.getSFDB())) {
											continue;
										} else if (("SFBJ").equals(JudgeDJZ)
												&& !("1")
														.equals(XMXX.getSFBJ())) {
											continue;
										}
									}
								}
							} else {
								continue;
							}

							Object o = lsqls.get(iql).get("DJSJ");
							Map<String, String> map = new HashMap<String, String>();
							map.put("QLID", qlid);
							map.put("YWH", ywh);
							map.put("ZXYWH", lsfsql.getZXDYYWH());
							if (o != null) {
								String djsj = DateUtil.FormatByDatetime(o);
								map.put("DJSJ", djsj);

								SimpleDateFormat sdf = new SimpleDateFormat(
										"yyyy-MM-dd HH-mm-ss");
								String djsj_value = sdf.format(lsqls.get(iql)
										.get("DJSJ"));
								map.put("DJSJ_VALUE", djsj_value);
							} else {
								map.put("DJSJ", "");
							}
							if (listQLID.contains(qlid)) {
								map.put("CANCLED", "false");
							} else {
								map.put("CANCLED", "true");
							}
							if (listQLID.contains(qlid)) {
								map.put("DJZT", "YGD");
							} else {
								map.put("DJZT", "YZX");
							}
							if (DJLX.YGDJ.Value.equals(djlx)
									&& !QLLX.DIYQ.Value.equals(qllx)
									&& bdcdylx.equals("032")) {
								map.put("DJLX", "期房预告登记");
							} else if (DJLX.YGDJ.Value.equals(djlx)
									&& QLLX.DIYQ.Value.equals(qllx)
									&& bdcdylx.equals("032")) {
								map.put("DJLX", "期房抵押预告登记");
							} else if (DJLX.YGDJ.Value.equals(djlx)
									&& !QLLX.DIYQ.Value.equals(qllx)
									&& bdcdylx.equals("031")) {
								if ("99".equals(qllx)
										&& DJLX.YGDJ.Value.equals(djlx)) {
									map.put("QLLX", "SYQ");
									map.put("DJLX", "现房转移预告登记");
								} else {
									map.put("DJLX", "商品房预告登记");
								}
							} else if (DJLX.YGDJ.Value.equals(djlx)
									&& QLLX.DIYQ.Value.equals(qllx)
									&& bdcdylx.equals("031")) {
								map.put("DJLX", "商品房押预告登记");
							} else if (bdcdylx.equals("031")) {
								map.put("DJLX", "现房" + DJLX.initFrom(djlx).Name);
							} else {
								map.put("DJLX", DJLX.initFrom(djlx).Name);
							}

							if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(qllx)
									|| QLLX.JTJSYDSYQ_FWSYQ.Value.equals(qllx)
									|| QLLX.ZJDSYQ_FWSYQ.Value.equals(qllx)) {
								map.put("QLLX", "SYQ");
								if (DJLX.YGDJ.Value.equals(djlx)) {
									map.put("QLLXEX", "");
								} else {
									map.put("QLLXEX", "SYQ");
								}
							} else if (QLLX.JTTDSYQ.Value.equals(qllx)
									|| QLLX.GJTDSYQ.Value.equals(qllx)) {
								map.put("QLLX", "SYQ");
								if (DJLX.YGDJ.Value.equals(djlx)) {
									map.put("QLLXEX", "");
								} else {
									map.put("QLLXEX", "SYQ");
								}

							} else if (QLLX.GYJSYDSHYQ.Value.equals(qllx)
									|| QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(qllx)
									|| QLLX.ZJDSYQ.Value.equals(qllx)
									|| QLLX.ZJDSYQ_FWSYQ.Value.equals(qllx)
									|| QLLX.JTJSYDSYQ.Value.equals(qllx)
									|| QLLX.JTJSYDSYQ_FWSYQ.Value.equals(qllx)
									|| QLLX.LDSYQ.Value.equals(qllx)
									|| QLLX.LDSYQ_SLLMSYQ.Value.equals(qllx)) {
								map.put("QLLX", "SHYQ");
								if (DJLX.YGDJ.Value.equals(djlx)) {
									map.put("QLLXEX", "");
								} else {
									map.put("QLLXEX", "SHYQ");
								}
							} else if (qllx.equals(QLLX.DIYQ.Value)) {
								map.put("QLLX", "DYQ");
								if (DJLX.YGDJ.Value.equals(djlx)) {
									map.put("QLLXEX", "");
								} else {
									map.put("QLLXEX", "DYQ");
								}
							} else if (djlx.equals(DJLX.CFDJ.Value)) {
								map.put("QLLX", "CFDJ");
								map.put("QLLXEX", "CFDJ");
							} else if (djlx.equals(DJLX.YYDJ.Value)) {
								map.put("QLLX", "YYDJ");
								map.put("QLLXEX", "YYDJ");
							}
							qllist.add(map);
							Map<String, String> newmap = new HashMap<String, String>();
							for (Entry<String, String> ent : map.entrySet()) {
								newmap.put(ent.getKey(), ent.getValue());
							}
							// ShowHistoryType为YWH时，按照业务登记过程显示历史产权信息;为QL时,按照所有的权利显示历史产权信息;未启用
							if (("YWH").equals(ConfigHelper
									.getNameByValue("ShowHistoryType"))) {
								if (qllx.equals(QLLX.DIYQ.Value)) {
									List<BDCS_FSQL_LS> lsfsqls = baseCommonDao
											.getDataList(BDCS_FSQL_LS.class,
													"qlid='" + qlid + "'");

									Object zxsj = lsfsqls.get(0).getZXSJ();
									String zxdbr = lsfsqls.get(0).getZXDBR();
									String zxfj = lsfsqls.get(0).getZXFJ();
									String zxdyyy = lsfsqls.get(0).getZXDYYY();
									String zxdyywh = lsfsqls.get(0)
											.getZXDYYWH();

									if (zxsj != null
											|| !StringHelper.isEmpty(zxdbr)
											|| !StringHelper.isEmpty(zxfj)
											|| !StringHelper.isEmpty(zxdyyy)
											|| !StringHelper.isEmpty(zxdyywh)) {
										String zxsjs = DateUtil
												.FormatByDatetime(zxsj);
										newmap.put("DJSJ", zxsjs);
										SimpleDateFormat sdf = new SimpleDateFormat(
												"yyyy-MM-dd HH-mm-ss");
										String djsj_value = sdf.format(lsfsqls
												.get(0).getZXSJ());
										map.put("DJSJ_VALUE", djsj_value);
										newmap.put("DJZT", "YZX");
										newmap.put("YWH", zxdyywh);
										if (bdcdylx.equals("031")) {
											newmap.put("DJLX", "现房注销登记");
										} else if (bdcdylx.equals("032")) {
											newmap.put("DJLX", "期房注销登记");
										}
										map.put("ZXYWH", zxdyywh);
										qllist.add(newmap);
									}
								} else if (djlx.equals(DJLX.CFDJ.Value)) {
									List<BDCS_FSQL_LS> lsfsqls = baseCommonDao
											.getDataList(BDCS_FSQL_LS.class,
													"qlid='" + qlid + "'");
									String zxdyywh = lsfsqls.get(0)
											.getZXDYYWH();
									String jfjg = lsfsqls.get(0).getJFJG();
									String jfwj = lsfsqls.get(0).getJFWJ();
									String jfwh = lsfsqls.get(0).getJFWH();
									String zxdbr = lsfsqls.get(0).getZXDBR();
									Object zxsj = lsfsqls.get(0).getZXSJ();
									if (zxsj != null
											|| !StringHelper.isEmpty(zxdyywh)
											|| !StringHelper.isEmpty(jfjg)
											|| !StringHelper.isEmpty(jfwj)
											|| !StringHelper.isEmpty(jfwh)
											|| !StringHelper.isEmpty(zxdbr)) {
										String zxsjs = DateUtil
												.FormatByDatetime(zxsj);
										newmap.put("DJSJ", zxsjs);
										SimpleDateFormat sdf = new SimpleDateFormat(
												"yyyy-MM-dd HH-mm-ss");
										String djsj_value = sdf.format(lsfsqls
												.get(0).getZXSJ());
										map.put("DJSJ_VALUE", djsj_value);
										newmap.put("DJZT", "YZX");
										newmap.put("YWH", zxdyywh);
										if (bdcdylx.equals("031")) {
											newmap.put("DJLX", "现房解封登记");
										} else if (bdcdylx.equals("032")) {
											newmap.put("DJLX", "期房解封登记");
										}
										lsqls2.add(newmap);
									}
								}
							}
						}
					}

					StringBuilder builderGZ = new StringBuilder();
					builderGZ
							.append("SELECT QL.XMBH,QL.QLID,QL.YWH,QL.DJLX,QL.QLLX,QL.DJSJ ");
					builderGZ.append("FROM BDCK.BDCS_QL_GZ QL ");
					builderGZ
							.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON QL.XMBH=XMXX.XMBH ");
					builderGZ.append("WHERE DJDYID='");
					builderGZ.append(djdyid);
					builderGZ.append("' ");
					builderGZ.append("AND (NVL2(XMXX.");
					builderGZ.append(JudgeDJZ);
					builderGZ.append(",1,0)=0 OR XMXX.");
					builderGZ.append(JudgeDJZ);
					builderGZ.append("<>'1') ");
					builderGZ.append("ORDER BY DJSJ");
					List<Map> gzqls = baseCommonDao
							.getDataListByFullSql(builderGZ.toString());
					if (gzqls != null && gzqls.size() > 0) {
						for (int iql = 0; iql < gzqls.size(); iql++) {
							String qlid = StringHelper.formatObject(gzqls.get(
									iql).get("QLID"));
							String qllx = StringHelper.formatObject(gzqls.get(
									iql).get("QLLX"));
							String xmbh = StringHelper.formatObject(gzqls.get(
									iql).get("XMBH"));
							ProjectInfo info = ProjectHelper
									.GetPrjInfoByXMBH(xmbh);
							Object o = gzqls.get(iql).get("DJSJ");
							String djlx = info.getDjlx();
							String project_id = info.getProject_id();
							String ywh = "";
							try {
								BDCS_XMXX XMXX = Global.getXMXX(project_id);
								if (XMXX != null) {
									ywh = XMXX.getYWLSH();
									if (StringHelper.isEmpty(ywh)) {
										ywh = project_id;
									}
								} else {
									ywh = project_id;
								}

							} catch (Exception e) {
								e.printStackTrace();
							}

							Map<String, String> map = new HashMap<String, String>();
							map.put("QLID", qlid);
							map.put("YWH", ywh);
							map.put("DJZT", "DJZ");
							if (o != null) {
								String djsj = DateUtil.FormatByDatetime(o);
								map.put("DJSJ", djsj);
								SimpleDateFormat sdf = new SimpleDateFormat(
										"yyyy-MM-dd HH-mm-ss");
								String djsj_value = sdf.format(gzqls.get(iql)
										.get("DJSJ"));
								map.put("DJSJ_VALUE", djsj_value);
							} else {
								map.put("DJSJ", "");
							}

							if (DJLX.YGDJ.Value.equals(djlx)
									&& !QLLX.DIYQ.Value.equals(qllx)
									&& bdcdylx.equals("032")) {
								map.put("DJLX", "期房预告登记");
							} else if (DJLX.YGDJ.Value.equals(djlx)
									&& QLLX.DIYQ.Value.equals(qllx)
									&& bdcdylx.equals("032")) {
								map.put("DJLX", "期房抵押预告登记");
							} else if (DJLX.YGDJ.Value.equals(djlx)
									&& !QLLX.DIYQ.Value.equals(qllx)
									&& bdcdylx.equals("031")) {
								if ("99".equals(qllx)
										&& DJLX.YGDJ.Value.equals(djlx)) {
									map.put("QLLX", "SYQ");
									map.put("DJLX", "现房转移预告登记");
								} else {
									map.put("DJLX", "商品房预告登记");
								}
							} else if (DJLX.YGDJ.Value.equals(djlx)
									&& QLLX.DIYQ.Value.equals(qllx)
									&& bdcdylx.equals("031")) {
								map.put("DJLX", "商品房押预告登记");
							} else if (bdcdylx.equals("031")) {
								map.put("DJLX", "现房" + DJLX.initFrom(djlx).Name);
							} else {
								map.put("DJLX", DJLX.initFrom(djlx).Name);
							}
							if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(qllx)
									|| QLLX.JTJSYDSYQ_FWSYQ.Value.equals(qllx)
									|| QLLX.ZJDSYQ_FWSYQ.Value.equals(qllx)) {
								map.put("QLLX", "SYQ");
								if (DJLX.YGDJ.Value.equals(djlx)) {
									map.put("QLLXEX", "");
								} else {
									map.put("QLLXEX", "SYQ");
								}
							} else if (QLLX.JTTDSYQ.Value.equals(qllx)
									|| QLLX.GJTDSYQ.Value.equals(qllx)) {
								map.put("QLLX", "SYQ");
								if (DJLX.YGDJ.Value.equals(djlx)) {
									map.put("QLLXEX", "");
								} else {
									map.put("QLLXEX", "SYQ");
								}

							} else if (QLLX.GYJSYDSHYQ.Value.equals(qllx)
									|| QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(qllx)
									|| QLLX.ZJDSYQ.Value.equals(qllx)
									|| QLLX.ZJDSYQ_FWSYQ.Value.equals(qllx)
									|| QLLX.JTJSYDSYQ.Value.equals(qllx)
									|| QLLX.JTJSYDSYQ_FWSYQ.Value.equals(qllx)
									|| QLLX.LDSYQ.Value.equals(qllx)
									|| QLLX.LDSYQ_SLLMSYQ.Value.equals(qllx)) {
								map.put("QLLX", "SHYQ");
								if (DJLX.YGDJ.Value.equals(djlx)) {
									map.put("QLLXEX", "");
								} else {
									map.put("QLLXEX", "SHYQ");
								}
							} else if (qllx.equals(QLLX.DIYQ.Value)) {
								map.put("QLLX", "DYQ");
								if (DJLX.YGDJ.Value.equals(djlx)) {
									map.put("QLLXEX", "");
								} else {
									map.put("QLLXEX", "DYQ");
								}
							} else if (djlx.equals(DJLX.CFDJ.Value)) {
								map.put("QLLX", "CFDJ");
								map.put("QLLXEX", "CFDJ");
							} else if (djlx.equals(DJLX.YYDJ.Value)) {
								map.put("QLLX", "YYDJ");
								map.put("QLLXEX", "YYDJ");
							}
							qllist.add(map);
						}
					}
				}
				String sql = " FROM DBA_TABLES WHERE TABLE_NAME=UPPER('HOUSEBOOK') AND OWNER='BDCK'";
				long count = baseCommonDao.getCountByFullSql(sql);
				if (count > 0) {
					if (!StringHelper.isEmpty(bdcdylx)) {
						BDCDYLX dylx = BDCDYLX.initFrom(bdcdylx);
						RealUnit unit = UnitTools.loadUnit(dylx, DJDYLY.LS,
								bdcdyids[i]);
						if (unit != null) {
							String relationid = unit.getRELATIONID();
							if (!StringHelper.isEmpty(relationid)) {
								String sql2 = MessageFormat
										.format("select * from bdck.housebook where bldroomid=''{0}'' order by commitdatetime",
												relationid);
								if (listmap != null && listmap.size() > 0) {
									listmapnew = baseCommonDao
											.getDataListByFullSql(sql2);
									for (int j = 0; j < listmapnew.size(); j++) {

										listmap.add(listmapnew.get(j));

									}
									Collections.reverse(listmap);
								} else {

									listmap = baseCommonDao
											.getDataListByFullSql(sql2);
									Collections.reverse(listmap);
								}

							}
						}
					}
				}

			}
			if (listmap != null && listmap.size() > 0) {
				for (Map m : listmap) {
					// long l = 0;
					Object o = m.get("COMMITDATETIME");
					if (o != null) {
						String createdate = DateUtil.FormatByDatetime(o);
						m.put("COMMITDATETIME", createdate);
					}
					Object o2 = m.get("CREATEDATE");
					if (o2 != null) {
						String createdate = DateUtil.FormatByDatetime(o2);
						m.put("CREATEDATE", createdate);
					}
				}
			} else {

			}

			// 查询登记过程按照时间排序 ，正在进行的放在最上面，没时间的放在最下面
			if (qllist.size() > 0) {
				// 1.取出djsj不为空的
				for (int i = 0; i < qllist.size(); i++) {

					if (!StringHelper.isEmpty(qllist.get(i).get("DJSJ")
							.toString())) {
						newqllist.add(qllist.get(i));
					}
				}
				// 2.对不为空的进行排序
				final SimpleDateFormat sdf1 = new SimpleDateFormat(
						"yyyy-MM-dd HH-mm-ss");
				Collections.sort(newqllist, new Comparator<Map>() {

					@Override
					public int compare(Map mapi, Map mapj) {
						java.util.Date datei = null;

						java.util.Date datej = null;
						try {
							if (!StringHelper.isEmpty(mapi.get("DJSJ_VALUE")
									.toString())) {

								datei = sdf1.parse(mapi.get("DJSJ_VALUE")
										.toString());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							if (!StringHelper.isEmpty(mapj.get("DJSJ_VALUE")
									.toString())) {

								datej = sdf1.parse(mapj.get("DJSJ_VALUE")
										.toString());
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
						if (StringHelper.isEmpty(datei)
								|| (!StringHelper.isEmpty(datej) && datei
										.before(datej))) {
							return 1;
						} else if (datei.equals(datej)) {
							return 0;
						}
						return -1;
					}

				});
				// 3.把部位空的加上
				for (int i = 0; i < qllist.size(); i++) {

					if (StringHelper.isEmpty(qllist.get(i).get("DJSJ")
							.toString())) {
						newqllist.add(qllist.get(i));
					}
				}
				// 4.把正在办理中的放在最上面
				if (newqllist.size() > 0) {

					for (int i = 0; i < newqllist.size(); i++) {
						if (newqllist.get(i).get("DJZT").equals("DJZ")) {
							lastqllist.add(0, newqllist.get(i));
						} else {
							lastqllist.add(lastqllist.size(), newqllist.get(i));
						}
					}

				}

			}

			result.add(lastqllist);
			if (listmap != null) {
				result.add(listmap);
			}
		}
		return result;
	}

	@Override
	public QLInfo GetQLInfo(String qlid, String djzt) {
		QLInfo qlinfo = new QLInfo();
		if (("DJZ").equals(djzt)) {
			Rights ql = RightsTools.loadRights(DJDYLY.GZ, qlid);
			qlinfo.setql(ql);
			SubRights fsql = RightsTools.loadSubRightsByRightsID(DJDYLY.GZ,
					qlid);
			qlinfo.setfsql(fsql);
			List<RightsHolder> qlrlist = RightsHolderTools.loadRightsHolders(
					DJDYLY.GZ, qlid);
			qlinfo.setqlrlist(qlrlist);
		} else {
			Rights ql = RightsTools.loadRights(DJDYLY.LS, qlid);
			qlinfo.setql(ql);
			SubRights fsql = RightsTools.loadSubRightsByRightsID(DJDYLY.LS,
					qlid);
			if (!StringHelper.isEmpty(ql) && !StringHelper.isEmpty(ql.getXMBH())) {
				BDCS_XMXX xmxx = Global.getXMXXbyXMBH(ql.getXMBH());
				if(xmxx != null){
					if("800".equals(xmxx.getDJLX())){
						try {
							fsql.setCFSJ(StringHelper.FormatByDate(xmxx.getSLSJ()));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}
			}
			qlinfo.setfsql(fsql);
			List<RightsHolder> qlrlist = RightsHolderTools.loadRightsHolders(
					DJDYLY.LS, qlid);
			qlinfo.setqlrlist(qlrlist);
			if("1".equals(ql.getISPARTIAL())){
				List<BDCS_PARTIALLIMIT> partiallimit = baseCommonDao.getDataList(BDCS_PARTIALLIMIT.class, 
						" LIMITQLID='"+ qlid +"' AND LIMITTYPE='800'");
				if(partiallimit!=null&&partiallimit.size()>0){
					qlinfo.setLimitqlr(RightsHolderTools.loadRightsHolder(
							DJDYLY.LS, partiallimit.get(0).getQLRID()));
				}
			}
		}
		return qlinfo;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Message queryMessage(Map<String, String> queryvalues, int page,
			int rows, boolean iflike) {
		Message msg = new Message();
		//long count = 10;
		int pageCount = 0;
		ResultSet result= null;
		List<Map> listresult = null;
		List flightPageList = new ArrayList();
		String fszt = queryvalues.get("FSZT");//发送状态
		String sqrmc = queryvalues.get("SQRMC");//申请人名称
		String dhhm = queryvalues.get("DHHM");//电话号码
	    String xzqh = queryvalues.get("XZQH");//行政区划
	    String FSKSSJ = queryvalues.get("FSKSSJ");//发送开始时间
	    if(FSKSSJ==null){
	    	FSKSSJ="";
	    }
	    String FSJSSJ = queryvalues.get("FSJSSJ");//发送结束时间
	    if(FSJSSJ==null){
	    	FSJSSJ="";
	    }
	    String FSSJ = queryvalues.get("FSSJ");
	    
	    
	    
	    StringBuffer sb=new StringBuffer();	  
	  //  StringBuffer sb1=new StringBuffer();	  
	    String sql = "";
	      
	    
	    if(!fszt.equals("") && fszt!=null){
	    	if(fszt.equals("1")){
	    		sb.append(" and status = '发送成功'");
	    	}else if(fszt.equals("2")){
	    		sb.append(" and status = '发送失败'");
	    	}
	    }
	    if(!sqrmc.equals("") && sqrmc!=null){
	    	if(iflike){
	    		sb.append(" and sxrmc like '%"+sqrmc+ "%'");
	    	}else{
	    	    sb.append(" and sxrmc = "+sqrmc);}
	    }
	    if(!dhhm.equals("") && dhhm!=null){
	    	if(iflike){
	    		sb.append(" and sqrdh like '%"+dhhm+ "%'");
	    	}else{
	    		
	    	    sb.append(" and sqrdh = "+dhhm);
	    	}	   
	    }
	    if(FSSJ!=null && !FSSJ.equals("undefined") && !FSSJ.equals("")){
	    	    sb.append(" and to_char(DXFSSJ,'yyyy') = "+ FSSJ);
	    }
	    
	 //  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  // Date date = sdf.parse();
		   
	      if(!FSKSSJ.equals("") && FSJSSJ.equals("")){
		   
			 sb.append(" and dxfssj> to_date('"+FSKSSJ+"','YYYY-MM-DD HH24:mi:ss')");
	      }
	      if(!FSJSSJ.equals("") && FSKSSJ.equals("") ){
	    	 sb.append(" and dxfssj< to_date('"+FSJSSJ+"','YYYY-MM-DD HH24:mi:ss')");
	      }
	      if(!FSJSSJ.equals("") && !FSKSSJ.equals("")){
	    	 sb.append(" and dxfssj between to_date('"+FSKSSJ+"','YYYY-MM-DD HH24:mi:ss') and to_date('"+FSJSSJ+"','YYYY-MM-DD HH24:mi:ss')");
	      }
	            
	      if(xzqh.equals("0")){
	    	  sql = "select SXRMC AS SQRMC, SQRDH AS DHHM, STATUS AS FSZT, to_char(DXFSSJ,'yyyy-mm-dd') AS FSSJ from sharesearch.dhxx where 0=0 "+sb +"union all " 
	    	  		+ " select SXRMC AS SQRMC, SQRDH AS DHHM, STATUS AS FSZT, to_char(DXFSSJ,'yyyy-mm-dd') AS FSSJ from sharesearch.dhxx @to_orcl6_sharesearch "
	    	  		/*+ " select SXRMC AS SQRMC, SQRDH AS DHHM, STATUS AS FSZT, DXFSSJ AS FSSJ from sharesearch.dhxx @to_orcl7_sharesearch"*/
	    	  		+ " where 0=0 "+sb;
	      }
	      if(xzqh.equals("1")){
	    	  sql = "select SXRMC AS SQRMC, SQRDH AS DHHM, STATUS AS FSZT, to_char(DXFSSJ,'yyyy-mm-dd') AS FSSJ from sharesearch.dhxx where 0=0 "+sb;
	      }
	      if(xzqh.equals("2")){
	    	  sql = "select SXRMC AS SQRMC, SQRDH AS DHHM, STATUS AS FSZT, to_char(DXFSSJ,'yyyy-mm-dd') AS FSSJ from sharesearch.dhxx @to_orcl6_sharesearch where 0=0 "+sb;
	      }
	      if(xzqh.equals("3")){
	    	  sql = "select SXRMC AS SQRMC, SQRDH AS DHHM, STATUS AS FSZT, to_char(DXFSSJ,'yyyy-mm-dd') AS FSSJ from sharesearch.dhxx @to_orcl7_sharesearch where 0=0 "+sb;
	      }
	    
	    //sb.append(sql);
	    
	      Connection jyConnection =  JH_DBHelper.getConnect_CXGXK();
	      
		   try {
		      result = JH_DBHelper.excuteQuery(jyConnection, sql.toString());
			 //  result = JH_DBHelper.excutepageQuery(jyConnection, sb.toString(), page, rows);
			//   result.beforeFirst();
			  
			//   System.out.println("result.next():"+result.next());
		       listresult = resultSetToList(result);
		       
              
               if(listresult.size()%rows==0){
            	   pageCount=listresult.size()/rows;
            	   }else{
            	   pageCount=(listresult.size()/rows)+1;
            	   }
             /*  if(rows>pageCount){
            	   rows=pageCount;
            	   } 
		       */
               
              for (int i = ((page-1) * rows);
               i < listresult.size() && i < ((page) * rows) && page > 0; i++) {
               flightPageList.add(listresult.get(i)); 
               }
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
			}
		  
		   msg.setTotal(pageCount);
		   msg.setRows(flightPageList);
		   
		return msg;
	}
	protected List<Map> resultSetToList(ResultSet rs)  {   
        if (rs == null)   
            return Collections.EMPTY_LIST;   
        ResultSetMetaData md = null;
        int columnCount = 0;
        List<Map> list = new ArrayList<Map>(); 
        Map rowData = new HashMap();
		try {
			
			
			while (rs.next()) {  
				md = rs.getMetaData();//得到结果集(rs)的结构信息，比如字段数、字段名等
				columnCount = md.getColumnCount();
				rowData = new HashMap(columnCount);   
				for (int i = 1; i <= columnCount; i++) {  
					rowData.put(md.getColumnName(i), rs.getObject(i));   
				}   
				list.add(rowData);//返回此 ResultSet 对象中的列数 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				if(rs!=null)
					rs.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}   
        
      /*  ResultSetMetaData md;
        int num;
        List listOfRows = new ArrayList();
		try {
			md = rs.getMetaData();
			num = md.getColumnCount();
			while (rs.next()) {
				Map mapOfColValues = new HashMap(num);
				for (int i = 1; i <= num; i++) {
				mapOfColValues.put(md.getColumnName(i), rs.getObject(i));
				}
				listOfRows.add(mapOfColValues);
				}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        return list;   
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Message queryHouse(Map<String, String> queryvalues, int page,
			int rows, boolean iflike, String fwzt,String sort,String order) {
		Message msg = new Message();
		long count = 0;
		List<Map> listresult = null;
		//结果筛选——是否只显示产权
		String jgsx=queryvalues.get("JGSX");
		queryvalues.remove("JGSX");
		if ("2".equals(jgsx)) {
			return queryHouseForUnit(queryvalues, page,
					 rows,  iflike, fwzt,sort,order);
		}
		String cxzt=queryvalues.get("CXZT");
		queryvalues.remove("CXZT");			
		//区域筛选——只查询有权限查询的区域
		String searchstates=queryvalues.get("SEARCHSTATES");
		queryvalues.remove("SEARCHSTATES");
		
		String fsqlcfwh=queryvalues.get("FSQL.CFWH");
		/* ===============1、先获取实体对应的表名=================== */
		
		String unitentityName = "BDCK.BDCS_H_XZ";
		if ("2".equals(cxzt)) {
			unitentityName = "BDCK.BDCS_H_LS";
		}

		/* ===============2、再获取表名+'_'+字段名=================== */
		String dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.FH,DY.GHYT,DY.FWBM,DY.SCJZMJ,DY.SCTNJZMJ,DY.SCFTJZMJ,DY.FTTDMJ,'031' AS BDCDYLX ,'现房' AS BDCDYLXMC ,DY.ZRZH,DY.SEARCHSTATE,DY.BZ";
		String qlfieldsname = "XM.YWLSH,QL.QLID,QL.BDCQZH,QL.QLLX,QL.DJSJ";
		String fsqlfieldsname = "FSQL.FSQLID,FSQL.CFWH";

		if (fwzt != null && fwzt.equals("2")) {
			unitentityName = "BDCK.BDCS_H_XZY";
			if ("2".equals(cxzt)) {
				unitentityName = "BDCK.BDCS_H_LSY";
			}
			dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.FH,DY.GHYT,DY.FWBM,DY.YCJZMJ AS SCJZMJ,DY.YCTNJZMJ AS SCTNJZMJ,DY.YCFTJZMJ AS SCFTJZMJ,DY.FTTDMJ,'032' AS BDCDYLX,'期房' AS BDCDYLXMC,DY.ZRZH,DY.SEARCHSTATE,DY.BZ ";
		}

		// 统一期现房 2015年10月28日
		if (fwzt != null && fwzt.equals("3")) {
			unitentityName = "(SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ,FTTDMJ,'031' AS BDCDYLX ,'现房' AS BDCDYLXMC, ZRZH, SEARCHSTATE, BZ  FROM BDCK.BDCS_H_XZ UNION ALL SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,FTTDMJ,'032' AS BDCDYLX ,'期房' AS BDCDYLXMC, ZRZH, SEARCHSTATE, BZ  FROM BDCK.BDCS_H_XZY)";
			
			if ("2".equals(cxzt)) {
				unitentityName = "(SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ,FTTDMJ,'031' AS BDCDYLX ,'现房' AS BDCDYLXMC, ZRZH, SEARCHSTATE, BZ FROM BDCK.BDCS_H_LS UNION ALL SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,FTTDMJ,'032' AS BDCDYLX ,'期房' AS BDCDYLXMC , ZRZH, SEARCHSTATE, BZ FROM BDCK.BDCS_H_LSY)";
		    
			}
			dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.FH,DY.GHYT,DY.JZMJ AS SCJZMJ,DY.TNJZMJ AS SCTNJZMJ,DY.FTJZMJ AS SCFTJZMJ,DY.FTTDMJ,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC,DY.ZRZH,DY.SEARCHSTATE,DY.BZ  ";
			
		}

		 /*===============3、构造查询语句=================== */
		 /* SELECT 字段部分 */
		StringBuilder builder2 = new StringBuilder();
		builder2.append("select ").append(dyfieldsname).append(",")
				.append(qlfieldsname);
		if(!StringHelper.isEmpty(fsqlcfwh)){
			builder2.append(",").append(fsqlfieldsname);
		}
		String selectstr = builder2.toString();

		/* FROM 后边的表语句 */
		StringBuilder builder = new StringBuilder();
		if ("2".equals(cxzt)) {
			builder.append(" from {0} DY")
					.append(" left join BDCK.BDCS_DJDY_LS DJDY on dy.bdcdyid=djdy.bdcdyid")
					.append(" left join BDCK.bdcs_ql_ls ql on ql.djdyid=djdy.djdyid  ")
					.append(" left join BDCK.BDCS_XMXX XM on XM.XMBH = QL.XMBH");
					
		}else{
			builder.append(" from {0} DY")
					.append(" left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid")
					.append(" left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid ")
					.append(" left join BDCK.BDCS_XMXX XM on XM.XMBH = QL.XMBH ");
		}
		if(!StringHelper.isEmpty(fsqlcfwh)){
			builder.append(" left join BDCK.bdcs_fsql_xz fsql on ql.fsqlid=fsql.fsqlid  ");
		}
		String fromstr = builder.toString();
		fromstr = MessageFormat.format(fromstr, unitentityName);

		 /* WHERE 条件语句 */
		StringBuilder builder3 = new StringBuilder();
		builder3.append(" where  ");
		StringBuilder qlrbuilder = new StringBuilder();
		StringBuilder dyrbuilder = new StringBuilder();
		boolean havecondition = false;
		boolean haveqlr = false;
		boolean havedyr = false;
		boolean havefdc = false;//加个房开查询条件
		for (Entry<String, String> ent : queryvalues.entrySet()) {
			String name = ent.getKey();
			String value = ent.getValue();
			if (!StringHelper.isEmpty(value)) {
				// 抵押状态和查封状态，为‘0’的时候表示不过滤，查询全部
				if ((name.equals("CFZT") && value.equals("0"))
						|| (name.equals("DYZT") && value.equals("0"))
							||name.equals("YYZT") && value.equals("0")) {
					continue;
				}
                if ((name.equals("SWCDBZ") && value.equals("cd"))) {
					continue;
				}
                if(name.equals("CDYT") && value.equals("0")){
					continue;
				}
				if(name.equals("FDCCX")){
					havefdc = true;
					continue;
				}
				if(name.equals("BH") || name.equals("BZ")){
					continue;
				}
				// 权利人，权利人的条件比较特殊，where里边又套了where
				if (name.startsWith("QLR.")) {
					if (haveqlr)
						qlrbuilder.append(" and ");
					if (iflike) {
						qlrbuilder.append(" instr(" + name + ",'" + value.trim()
								+ "')>0 ");
					} else {
						if (name.equals("QLR.ZJH") && value.replace(" ", "").length() == 18) {
							String oldCard = ConverIdCard.getOldIDCard(value);
							if (oldCard.length() != 15) {
								qlrbuilder.append(" " + name + "='" + value.replace(" ", "").toUpperCase()
										+ "' ");
							} else {
								qlrbuilder.append(" (" + name + "='" + value.replace(" ", "").toUpperCase()
										+ "' or ");
								qlrbuilder.append(" " + name + "='" + oldCard.replace(" ", "").toUpperCase()
										+ "') ");
							}
						} else {
							qlrbuilder.append(" " + name + "='" + value.replace(" ", "") + "' ");
						}
					}
					haveqlr = true;
					continue;
				}
				// 抵押人判断
				if (name.startsWith("DYR.")) {
					if (havedyr)
						dyrbuilder.append(" and ");
					if (iflike) {
						dyrbuilder.append(" instr(" + name + ",'" + value
								+ "')>0 ");
					} else {
						dyrbuilder.append(" " + name + "='" + value.replace(" ", "") + "' ");
					}
					havedyr = true;
					continue;
				}
				if (havecondition) {
					builder3.append(" and ");
				}

				// 抵押状态
				if (name.equals("DYZT")) {
					if (value.equals("1")) {
						if ("2".equals(cxzt)) {
							builder3.append(" djdy.djdyid NOT IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_LS DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
						}else{
							builder3.append(" djdy.djdyid NOT IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_XZ DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
						}
					} else {
						if ("2".equals(cxzt)) {
							builder3.append(" djdy.djdyid IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_LS DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
						}else{
							builder3.append(" djdy.djdyid IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_XZ DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
						}
					}
					havecondition = true;
					continue;
				}
				// 查封状态
				if (name.equals("CFZT")) {
					if (value.equals("1")) {
						if ("2".equals(cxzt)) {
							builder3.append("  djdy.DJDYID NOT IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_LS CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99') ");
						}else{
							builder3.append("  djdy.DJDYID NOT IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_XZ CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99') ");
						}
					} else {
						if ("2".equals(cxzt)) {
							builder3.append("  djdy.DJDYID IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_LS CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99' AND CF.DJLX='800') ");
						
						}else{
							builder3.append("  djdy.DJDYID IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_XZ CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99' AND CF.DJLX='800') ");
						
						}
					}
					havecondition = true;
					continue;
				}
				// 异议状态
				if (name.equals("YYZT")) {
					if (value.equals("1")) {
						if ("2".equals( cxzt)) {
							builder3.append("  djdy.DJDYID NOT IN (SELECT YY.DJDYID FROM BDCK.BDCS_QL_LS YY WHERE YY.DJDYID=DJDYID AND YY.djlx = '600') ");
						}else{
							builder3.append("  djdy.DJDYID NOT IN (SELECT YY.DJDYID FROM BDCK.BDCS_QL_XZ YY WHERE YY.DJDYID=DJDYID AND YY.djlx = '600') ");
						}
					} else {
						if ("2".equals(cxzt)) {
							builder3.append("  djdy.DJDYID IN (SELECT YY.DJDYID FROM BDCK.BDCS_QL_LS YY WHERE YY.DJDYID=DJDYID AND  YY.djlx = '600') ");
						}else{
							builder3.append("  djdy.DJDYID IN (SELECT YY.DJDYID FROM BDCK.BDCS_QL_XZ YY WHERE YY.DJDYID=DJDYID AND  YY.djlx = '600') ");
						}
					}
					havecondition = true;
					continue;
				}

				if (iflike) {
					if (name.contains("BDCQZH")) {
						// 验证不动产权证号是否为纯数字的
						boolean flag = StringHelper.isNumericByString(value);
						if (flag) {
							builder3.append(" instr(" + name + ",'" + value + "')>0 ");
						} else {
							builder3.append(" instr(upper(" + name + "),'" + value.toUpperCase() + "')>0 ");
						}
					}else if (name.contains("QL.YWH")) {
						builder3.append(" (QL.YWH LIKE '%"+value+"%' OR XM.YWLSH LIKE '%"+value+"%') ");
					}else if (name.equals("DJSJ_Q")) {
						builder3.append("  QL.DJSJ >=to_date('"+queryvalues.get("DJSJ_Q")+"','yyyy-mm-dd') ");
					}else if (name.equals("DJSJ_Z")) {
						builder3.append("   QL.DJSJ <=(to_date('"+queryvalues.get("DJSJ_Z")+"','yyyy-mm-dd')+1) ");
					}else{
					    builder3.append(" instr(" + name + ",'" + value + "')>0 ");
					}
					
					
					
				} else {
					/*
					 * 如果通过不动产权证查询，且是精确查询时,
					 * 其中如果不动产权证号是否纯数字的，直接在权利人表中通过BDCQZHXH条件查询，
					 * 否则从权利表中通过BDCQZH条件查询
					 */
					 
					if (name.contains("BDCQZH")) {
						String cdbz = queryvalues.get("SWCDBZ");//
						// 验证不动产权证号是否为纯数字的
						boolean flag = StringHelper.isNumericByString(value);
						if (flag) {
							// 判断是否已经有查询权利人的条件了
							if (haveqlr) {
								qlrbuilder.append(" and ");
							}
							qlrbuilder.append(" QLR.BDCQZHXH " + "='" + value
									+ "' ");
							haveqlr = true;
							continue;
						} else {
							if(cdbz != null && !cdbz.equals("") && cdbz.equals("cd")){
								builder3.append(" " + name + " like '%" + value + "%' ");
							}else{
								builder3.append(" " + name + " ='" + value + "' ");
							}
						}
					}else if (name.contains("QL.YWH")) {
						builder3.append(" (QL.YWH='"+value+"' OR XM.YWLSH='"+value+"') ");
					}else if (name.equals("DJSJ_Q")) {
						builder3.append("  QL.DJSJ >=to_date('"+queryvalues.get("DJSJ_Q")+"','yyyy-mm-dd') ");
					}else if (name.equals("DJSJ_Z")) {
						builder3.append("   QL.DJSJ <=(to_date('"+queryvalues.get("DJSJ_Z")+"','yyyy-mm-dd')+1) ");
					}else {
						builder3.append(" " + name + "='" + value.replace(" ", "") + "' ");
					}
				}
				havecondition = true;
			}
		}
		// 有权利人查询条件
		if (!StringHelper.isEmpty(qlrbuilder.toString())) {
			if (havecondition) {
				builder3.append(" and ");
			}
			if ("2".equals(cxzt)) {
				builder3.append(" EXISTS (SELECT 1 FROM BDCK.BDCS_QLR_LS QLR WHERE QLR.QLID=QL.QLID AND "
						+ qlrbuilder.toString() + ")");
			}else{
				builder3.append(" EXISTS (SELECT 1 FROM BDCK.BDCS_QLR_XZ QLR WHERE QLR.QLID=QL.QLID AND "
						+ qlrbuilder.toString() + ")");
			}
			havecondition = true;
		}

		// 有抵押人查询条件
		if (!StringHelper.isEmpty(dyrbuilder.toString())) {
			if (havecondition) {
				builder3.append(" and ");
			}
			if ("2".equals(cxzt)) {
				builder3.append(" QL.QLID IN (SELECT QLID FROM BDCK.BDCS_FSQL_LS DYR WHERE "
						+ dyrbuilder.toString() + ")");
			}else{
				builder3.append(" QL.QLID IN (SELECT QLID FROM BDCK.BDCS_FSQL_XZ DYR WHERE "
						+ dyrbuilder.toString() + ")");
			}
			havecondition = true;
		}

		if (fwzt == null || !fwzt.equals("2")) {
			if (havecondition) {
				// builder3.append(" and ");
			}
			// builder3.append(" ql.qllx='4'");
		}
//		if ("2".equals(jgsx)) {
//			if (havecondition) {
//				 builder3.append(" and ");
//			}
//			 builder3.append(" ql.qllx in ('4','6','8')");
//			 havecondition=true;
//		}
		if (!StringHelper.isEmpty(searchstates)) {
			String[] states = searchstates.split("-");
			String state = "";
			for (int i = 0; i < states.length-1; i++) {
				state += "'"+states[i]+"',";
			}
				state += "'"+states[states.length-1]+"'";
			if (havecondition) {
				 builder3.append(" and ");
			}
			 builder3.append(" DY.SEARCHSTATE in (").append(state).append(")");
		}
		
		String wherestr = builder3.toString();
		if (wherestr.trim().toUpperCase().endsWith("WHERE")) {
			wherestr = "";
		}
		String fromSql = " from (select ql.qlid " + fromstr + wherestr + ")";
		String fullSql = selectstr + fromstr + wherestr;
		/* 排序 条件语句 */
		
		if(!StringUtils.isEmpty(sort)&&!StringUtils.isEmpty(order))
		{
		if(sort.toUpperCase().equals("ZL"))
			sort="DY.ZL";
		if(sort.toUpperCase().equals("BDCDYH"))
			sort="DY.BDCDYH";
		/*if(sort.toUpperCase().equals("QLRMC"))
			sort="QLR.QLRMC";
		if(sort.toUpperCase().equals("ZJH"))
			sort="QLR.ZJH";*/
		
		if(sort.toUpperCase().equals("BDCQZH"))
			sort="QL.BDCQZH";
		if(sort.toUpperCase().equals("FH"))
			sort="DY.FH";
		if(sort.toUpperCase().equals("QLLX"))
			sort="QL.QLLX";
		/*if(sort.toUpperCase().equals("ZJHM"))
			sort="QLR.ZJHM";*/
		if(sort.toUpperCase().equals("GHYTNAME"))
			sort="DY.GHYT";
		if(sort.toUpperCase().equals("BDCDYID"))
			sort="DY.BDCDYID";
		if(sort.toUpperCase().equals("BDCDYLXMC"))
			sort="DY.BDCDYLX";
        if(sort.toUpperCase().equals("ZRZH"))
			sort="DY.ZRZH";		
			fullSql=fullSql+" ORDER BY "+sort+" "+order;
		}
		/* 为了提高效率，先不查询符合条件的记录总数，将来改为异步获取 */
		
		CommonDao dao = baseCommonDao;

		count = dao.getCountByFullSql(fromSql);
		if(havefdc){//查档不分页
			listresult = dao.getDataListByFullSql(fullSql);
			//addRightsHolderInfo(listresult);
			addQLLX(listresult);
		}
		else{
			
			listresult = dao.getPageDataByFullSql(fullSql, page, rows);
			addRightsHolderInfo(listresult);
			String cdbz = queryvalues.get("SWCDBZ");//查档的一个参数
			

			addLimitStatus(listresult,cdbz);
			addDyCfDetails(listresult);
			isGlzd(listresult);
			//添加状态
			addBDCDYZT(listresult);
			//权利状态
			addQLZT(listresult);
			//添加注销状态
			addZXZT(listresult);
			addLimitFwStatusByZd(listresult);  //房屋查询需要新增土地的状态  就解开这句话

			addQLLX(listresult);
			addQXGL(listresult);
			
			ADDXMXX_SIZE(listresult);
		}

		// 格式化结果中的常量值
		for (Map map : listresult) {
			if (map.containsKey("GHYT")) {
				String value = StringHelper.formatObject(StringHelper
						.isEmpty(map.get("GHYT")) ? "" : map.get("GHYT"));
				String name = "";
				if (!StringHelper.isEmpty(value)) {
					name = ConstHelper.getNameByValue("FWYT", value);
				}
				map.put("GHYTname", name);
			}
			if (map.containsKey("DJSJ")) {
				map.put("DJSJ",StringHelper.FormatYmdhmsByDate(map.get("DJSJ")));
			}
		}

		msg.setTotal(count);
		msg.setRows(listresult);
		msg.setMsg(Global.getCurrentUserName());
		
		return msg;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Message queryHouseForUnit(Map<String, String> queryvalues, int page,
			int rows, boolean iflike, String fwzt,String sort,String order) {
		Date start = new Date();
		Message msg = new Message();
		long count = 0;
		List<Map> listresult = null;		
		String cxzt=queryvalues.get("CXZT");
		queryvalues.remove("CXZT");
		//区域筛选——只查询有权限查询的区域
		String searchstates=queryvalues.get("SEARCHSTATES");
		queryvalues.remove("SEARCHSTATES");
		
		String fsqlcfwh=queryvalues.get("FSQL.CFWH");
		/* ===============1、先获取实体对应的表名=================== */
		
		String unitentityName_h = " SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,FTTDMJ,'031' AS BDCDYLX ,'现房' AS BDCDYLXMC , ZRZH, SEARCHSTATE, BZ FROM BDCK.BDCS_H_XZ ";
		String unitentityName_hy = " SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,FTTDMJ,'032' AS BDCDYLX ,'期房' AS BDCDYLXMC , ZRZH, SEARCHSTATE, BZ FROM BDCK.BDCS_H_XZY ";


		String inner_unitentityName_h = " SELECT BDCDYID,BDCDYID RBDCDYID FROM BDCK.BDCS_H_XZ DY";
		String inner_unitentityName_hy = " SELECT BDCDYID,(case when GX.SCBDCDYID is not null then GX.SCBDCDYID else DY.BDCDYID end) RBDCDYID FROM BDCK.BDCS_H_XZY DY LEFT JOIN BDCK.YC_SC_H_XZ GX ON GX.YCBDCDYID = DY.BDCDYID ";

		if ("2".equals(cxzt)) {//历史查询
			unitentityName_h = " SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,FTTDMJ,'031' AS BDCDYLX ,'现房' AS BDCDYLXMC , ZRZH, SEARCHSTATE, BZ FROM BDCK.BDCS_H_LS ";
			unitentityName_hy = " SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,FTTDMJ,'032' AS BDCDYLX ,'期房' AS BDCDYLXMC , ZRZH, SEARCHSTATE, BZ FROM BDCK.BDCS_H_LSY ";
			inner_unitentityName_h = " SELECT BDCDYID,BDCDYID RBDCDYID FROM BDCK.BDCS_H_LS DY";
			inner_unitentityName_hy = " SELECT BDCDYID,(case when GX.SCBDCDYID is not null then GX.SCBDCDYID else DY.BDCDYID end) RBDCDYID FROM BDCK.BDCS_H_LSY DY LEFT JOIN BDCK.YC_SC_H_LS GX ON GX.YCBDCDYID = DY.BDCDYID ";
		}

		/* ===============2、再获取表名+'_'+字段名=================== */
		if (fwzt != null && fwzt.equals("2")) {//期房
			unitentityName_hy = " SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,FTTDMJ,'032' AS BDCDYLX ,'期房' AS BDCDYLXMC , ZRZH, SEARCHSTATE, BZ FROM BDCK.BDCS_H_XZY ";

			if ("2".equals(cxzt)) {
				unitentityName_hy = " SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,FTTDMJ,'032' AS BDCDYLX ,'期房' AS BDCDYLXMC , ZRZH, SEARCHSTATE, BZ FROM BDCK.BDCS_H_LSY ";
			}
		}

		 /*===============3、构造查询语句=================== */
		 /* SELECT 字段部分 */
		StringBuilder builder_Select = new StringBuilder();
		builder_Select.append("{0} DYALL ");
		
		StringBuilder builder_ql = new StringBuilder();

		/* FROM 后边的表语句 */
		StringBuilder builder_where_out = new StringBuilder();
		
		builder_where_out.append(" SELECT RBDCDYID FROM ( {0} DYIN   ");

		boolean flag_ql = false;
		if ("2".equals(cxzt)) {
			builder_ql.append(" DYIN.BDCDYID IN  (SELECT QL.BDCDYID FROM BDCK.BDCS_QL_LS QL ")
					.append(" LEFT JOIN BDCK.BDCS_XMXX XM ON XM.XMBH = QL.XMBH ");
					
		}else{
			builder_ql.append(" DYIN.BDCDYID IN  (SELECT QL.BDCDYID FROM BDCK.BDCS_QL_XZ QL ")
					.append(" LEFT JOIN BDCK.BDCS_XMXX XM ON XM.XMBH = QL.XMBH ");
		}
		if(!StringHelper.isEmpty(fsqlcfwh)){
			builder_ql.append(" LEFT JOIN BDCK.BDCS_FSQL_XZ FSQL ON QL.FSQLID=FSQL.FSQLID ");
		}			

		/* WHERE 条件语句 */
		StringBuilder builder3 = new StringBuilder();
		StringBuilder builder_zt = new StringBuilder();
		StringBuilder dyrbuilder = new StringBuilder();
		StringBuilder dybuilder = new StringBuilder();
		StringBuilder dysxbuilder = new StringBuilder();
		boolean haveql = false;//是否有多个权利的查询条件
		boolean havedyr = false;
		boolean havefdc = false;//加个房开查询条件
		boolean havedy = false;//是否有多个单元的查询条件
		boolean flag_qlr = false;
		boolean flag_zt= false;
		for (Entry<String, String> ent : queryvalues.entrySet()) {
			String name = ent.getKey();
			String value = ent.getValue();
			if (!StringHelper.isEmpty(value)) {
				// 抵押状态和查封状态，为‘0’的时候表示不过滤，查询全部
				if ((name.equals("CFZT") && value.equals("0"))
						|| (name.equals("DYZT") && value.equals("0"))
							||name.equals("YYZT") && value.equals("0")) 
					continue;
				
                if ((name.equals("SWCDBZ") && value.equals("cd"))) 
					continue;
                
                if(name.equals("CDYT") && value.equals("0"))
					continue;
                
				if(name.equals("FDCCX")){
					havefdc = true;
					continue;
				}
				if(name.equals("BH") || name.equals("BZ"))
					continue;
				// 权利人，权利人的条件比较特殊，where里边又套了where
				if (name.startsWith("QLR.")) {
					if (!flag_qlr) {
						if ("2".equals(cxzt))
							builder_ql.append("LEFT JOIN BDCK.BDCS_QLR_LS QLR ON QL.QLID = QLR.QLID ");
						else
							builder_ql.append("LEFT JOIN BDCK.BDCS_QLR_XZ QLR ON QL.QLID = QLR.QLID ");
						flag_qlr = true;
						flag_ql = true;
					}					
					if(haveql)
						builder3.append(" and ");
					
					if (iflike)
						builder3.append(" instr(" + name + ",'" + value.trim() + "')>0 ");
					else {
						if (name.equals("QLR.ZJH") && value.length() == 18) {
							String oldCard = ConverIdCard.getOldIDCard(value);
							if (oldCard.length() != 15) 
								builder3.append(" " + name + "='" + value.trim() + "' ");
							else {
								builder3.append(" (" + name + "='" + value.trim() + "' or ");
								builder3.append(" " + name + "='" + oldCard.trim() + "') ");
							}
						} else 
							builder3.append(" " + name + "='" + value.trim() + "' ");
					}
					haveql = true;
					continue;
				}
				// 抵押人判断
				if (name.startsWith("DYR.")) {
					if (havedyr)
						dyrbuilder.append(" AND ");
					if (iflike) 
						dyrbuilder.append(" instr(" + name + ",'" + value + "')>0 ");
					else
						dyrbuilder.append(" " + name + "='" + value + "' ");
					havedyr = true;
					continue;
				}								
				// 抵押状态--数据较小，使用IN
				if (name.equals("DYZT")) {
					if (flag_zt) {
						builder_zt.append(" AND");
					}
					if (value.equals("1")) {
						if ("2".equals(cxzt))
							builder_zt.append(" NOT EXISTS  (SELECT DYQ.BDCDYID FROM BDCK.BDCS_QL_LS DYQ WHERE DYQ.BDCDYID = DYIN.BDCDYID AND DYQ.QLLX='23') ");
						else
							builder_zt.append(" NOT EXISTS  (SELECT DYQ.BDCDYID FROM BDCK.BDCS_QL_XZ DYQ WHERE DYQ.BDCDYID = DYIN.BDCDYID AND DYQ.QLLX='23') ");
					} else {
						if ("2".equals(cxzt))
							builder_zt.append(" EXISTS  (SELECT DYQ.BDCDYID FROM BDCK.BDCS_QL_LS DYQ WHERE DYQ.BDCDYID = DYIN.BDCDYID AND DYQ.QLLX='23') ");
						else
							builder_zt.append(" EXISTS  (SELECT DYQ.BDCDYID FROM BDCK.BDCS_QL_XZ DYQ WHERE DYQ.BDCDYID = DYIN.BDCDYID AND DYQ.QLLX='23') ");
					}			
					flag_zt = true;
					continue;
				}
				// 查封状态--数据较小，使用IN
				if (name.equals("CFZT")) {
					if (flag_zt) {
						builder_zt.append(" AND");
					}
					if (value.equals("1")) {
						if ("2".equals(cxzt)) 
							builder_zt.append(" NOT EXISTS  (SELECT 1 FROM BDCK.BDCS_QL_LS CFDJ WHERE CFDJ.BDCDYID = DYIN.BDCDYID AND CFDJ.DJLX='800' AND CFDJ.QLLX='99' ) ");
						else
							builder_zt.append(" NOT EXISTS  (SELECT 1 FROM BDCK.BDCS_QL_XZ CFDJ WHERE CFDJ.BDCDYID = DYIN.BDCDYID AND CFDJ.DJLX='800' AND CFDJ.QLLX='99' ) ");
					} else {
						if ("2".equals(cxzt))
							builder_zt.append(" EXISTS  (SELECT 1 FROM BDCK.BDCS_QL_LS CFDJ WHERE CFDJ.BDCDYID = DYIN.BDCDYID AND CFDJ.DJLX='800' AND CFDJ.QLLX='99' ) ");
						else
							builder_zt.append(" EXISTS  (SELECT 1 FROM BDCK.BDCS_QL_XZ CFDJ WHERE CFDJ.BDCDYID = DYIN.BDCDYID AND CFDJ.DJLX='800' AND CFDJ.QLLX='99' ) ");
					}
					flag_zt = true;
					continue;
				}
				//DY查询
				if (name.startsWith("DY.")) {	
					if (havedy) 
						dybuilder.append(" AND ");
					if (iflike) 
						dybuilder.append(" instr(" + name + ",'" + value + "')>0 ");
					else
						dybuilder.append(" " + name + "='" + value + "' ");
					havedy = true;
					continue;
				}
				
				if (iflike) {
					if (haveql)
						builder3.append(" AND ");
					if (name.contains("BDCQZH")) {
						// 验证不动产权证号是否为纯数字的
						boolean flag = StringHelper.isNumericByString(value);
						if (flag) 
							builder3.append(" instr(" + name + ",'" + value + "')>0 ");
						else 
							builder3.append(" instr(upper(" + name + "),'" + value.toUpperCase() + "')>0 ");
					}else if (name.contains("QL.YWH")) 
						builder3.append(" (QL.YWH LIKE '%"+value+"%' OR XM.YWLSH LIKE '%"+value+"%') ");
					else if (name.equals("DJSJ_Q")) 
						builder3.append("  QL.DJSJ >=to_date('"+queryvalues.get("DJSJ_Q")+"','yyyy-mm-dd') ");
					else if (name.equals("DJSJ_Z")) 
						builder3.append("   QL.DJSJ <=(to_date('"+queryvalues.get("DJSJ_Z")+"','yyyy-mm-dd')+1) ");
					else
					    builder3.append(" instr(" + name + ",'" + value + "')>0 ");
					haveql = true;
					flag_ql = true;
				} else {
					/*
					 * 如果通过不动产权证查询，且是精确查询时,
					 * 其中如果不动产权证号是否纯数字的，直接在权利人表中通过BDCQZHXH条件查询，
					 * 否则从权利表中通过BDCQZH条件查询
					 */
					if (haveql) 
						builder3.append(" AND ");					
					if (name.contains("BDCQZH")) {
						String cdbz = queryvalues.get("SWCDBZ");//
						// 验证不动产权证号是否为纯数字的
						boolean flag = StringHelper.isNumericByString(value);
						if (flag) {
							// 判断是否已经有查询权利人的条件了							
							if (!flag_qlr) {
								if ("2".equals(cxzt))
									builder_ql.append("LEFT JOIN BDCK.BDCS_QLR_LS QLR ON QL.QLID = QLR.QLID ");
								else
									builder_ql.append("LEFT JOIN BDCK.BDCS_QLR_XZ QLR ON QL.QLID = QLR.QLID ");
								flag_qlr = true;
							}		
							
							builder3.append(" QLR.BDCQZHXH " + "='" + value + "' ");
							haveql = true;
							continue;
						} else {
							if(cdbz != null && !cdbz.equals("") && cdbz.equals("cd"))
								builder3.append(" " + name + " LIKE '%" + value + "%' ");
							else
								builder3.append(" " + name + " ='" + value + "' ");
						}
					}else if (name.contains("QL.YWH")) 
						builder3.append(" (XM.YWLSH='"+value+"') ");
					else if (name.equals("DJSJ_Q")) 
						builder3.append("  QL.DJSJ >=to_date('"+queryvalues.get("DJSJ_Q")+"','yyyy-mm-dd') ");
					else if (name.equals("DJSJ_Z")) 
						builder3.append("   QL.DJSJ <=(to_date('"+queryvalues.get("DJSJ_Z")+"','yyyy-mm-dd')+1) ");
					else 
						builder3.append(" " + name + "='" + value + "' ");
					haveql = true;
					flag_ql = true;
				}
			}
		}
				// 有抵押人查询条件
		if (!StringHelper.isEmpty(dyrbuilder.toString())) {
			if (haveql) 
				builder3.append(" AND ");
			if ("2".equals(cxzt)) 
				builder3.append(" QL.QLID IN (SELECT QLID FROM BDCK.BDCS_FSQL_LS DYR WHERE "
						+ dyrbuilder.toString() + ")");
			else
				builder3.append(" QL.QLID IN (SELECT QLID FROM BDCK.BDCS_FSQL_XZ DYR WHERE "
						+ dyrbuilder.toString() + ")");
		}

		if (!StringHelper.isEmpty(searchstates)) {
			String[] states = searchstates.split("-");
			String state = "";
			for (int i = 0; i < states.length-1; i++) {
				state += "'"+states[i]+"',";
			}
			state += "'"+states[states.length-1]+"'";
			if (havedy) 
				dybuilder.append(" AND ");
			dybuilder.append(" DY.SEARCHSTATE in (").append(state).append(")");
		}
		//dy及其条件查询
		
		if (fwzt != null && fwzt.equals("1")) {//现房
			dysxbuilder.append(inner_unitentityName_h);
			if (!StringHelper.isEmpty(dybuilder.toString())) {
				dysxbuilder.append(" WHERE ").append(dybuilder);	
			}	
		}else if (fwzt != null && fwzt.equals("2")) {//期房
			dysxbuilder.append(inner_unitentityName_hy);
			if (!StringHelper.isEmpty(dybuilder.toString())) {
				dysxbuilder.append(" WHERE ").append(dybuilder);
			}
		}else if (fwzt != null && fwzt.equals("3")) {//全部
			if (!StringHelper.isEmpty(dybuilder.toString())) {
				dysxbuilder.append(inner_unitentityName_h).append(" WHERE ").append(dybuilder)
				.append(" UNION ")
				.append(inner_unitentityName_hy).append(" WHERE ").append(dybuilder);	
			}else {
				dysxbuilder.append(inner_unitentityName_h)
				.append(" UNION ")
				.append(inner_unitentityName_hy);	
			}	
		}
		
		String where_out_str = builder_where_out.toString();
		where_out_str = MessageFormat.format(where_out_str, dysxbuilder.append(")"));//where DYALL.bdcdyid in ( SELECT RBDCDYID FROM DYIN
		
		
		String where_str = "";
		String where_str_bid = " WHERE DYALL.BDCDYID IN (";
		if (!StringHelper.isEmpty(builder3.toString())) {//是否存在权利
			builder3.append(") ");				
			builder_ql.append(" WHERE ").append(builder3).append(flag_zt? " AND "+ builder_zt.toString():builder_zt);
			where_str += " WHERE " + builder_ql.toString();
		}else if(!StringHelper.isEmpty(builder_zt.toString())){//是否抵押或查封
			where_str += " WHERE " + builder_zt.toString();
		}
		
		String selectstr_h = builder_Select.toString();		
		String selectstr_hy = builder_Select.toString();

		selectstr_h = MessageFormat.format(selectstr_h, unitentityName_h);//select * from dy
		selectstr_hy = MessageFormat.format(selectstr_hy, unitentityName_hy);//select * from dy
		String fullSql_h = selectstr_h;
		String fullSql_hy = selectstr_hy;
		String fromSql_h = " FROM (" + where_out_str + where_str + ")";
		String fromSql_hy = " FROM (" + where_out_str + where_str + ")";
		String fromSql = "";
		fullSql_h += where_str_bid + where_out_str + where_str +  ")"  ;
		fullSql_hy += where_str_bid + where_out_str + where_str +  ")"  ;

		String fullSql = "";
		if (fwzt != null && fwzt.equals("1")) {//全部
			fullSql = fullSql_h;
			fromSql = fromSql_h;
		}
		else if (fwzt != null && fwzt.equals("2")) {//全部
			fullSql = fullSql_hy;
			fromSql = fromSql_hy;

		}
		else if (fwzt != null && fwzt.equals("3")) {//全部
			fullSql = fullSql_h + " UNION " + fullSql_hy;
			fromSql = " FROM (SELECT RBDCDYID  " + fromSql_h + " UNION " + "SELECT RBDCDYID  " + fromSql_hy + ")";
		}
		/* 排序 条件语句 */		
		if(!StringUtils.isEmpty(sort)&&!StringUtils.isEmpty(order)) {
			if(sort.toUpperCase().equals("ZL"))  		sort="ZL";
			if(sort.toUpperCase().equals("BDCDYH")) 	sort="BDCDYH";
			/*if(sort.toUpperCase().equals("QLRMC"))	sort="QLR.QLRMC";
			if(sort.toUpperCase().equals("ZJH"))		sort="QLR.ZJH";*/
//			if(sort.toUpperCase().equals("BDCQZH")) 	sort="QL.BDCQZH";
			if(sort.toUpperCase().equals("FH")) 		sort="FH";
//			if(sort.toUpperCase().equals("QLLX"))		sort="QL.QLLX";
			/*if(sort.toUpperCase().equals("ZJHM"))		sort="QLR.ZJHM";*/
			if(sort.toUpperCase().equals("GHYTNAME")) 	sort="GHYT";
			if(sort.toUpperCase().equals("BDCDYID"))	sort="BDCDYID";
			if(sort.toUpperCase().equals("BDCDYLXMC"))	sort="BDCDYLX";
	        if(sort.toUpperCase().equals("ZRZH"))		sort="ZRZH";		
	        
			fullSql=fullSql+" ORDER BY "+sort+" "+order;
		}
		/* 为了提高效率，先不查询符合条件的记录总数，将来改为异步获取 */
		CommonDao dao = baseCommonDao;
		count = dao.getCountByFullSql(fromSql);
		
		if(havefdc){//查档不分页
			listresult = dao.getDataListByFullSql(fullSql);			
			addOtherInfo(listresult);//获取DJDY.DJDYID、 XM.YWLSH, QL.QLID,QL.BDCQZH,QL.QLLX,QL.DJSJ、 FSQL.FSQLID, FSQL.CFWH
			//addRightsHolderInfo(listresult);
			addQLLX(listresult); //QLLX
		}else{
			listresult = dao.getPageDataByFullSql(fullSql, page, rows);
			Date end = new Date();
			long still = (end.getTime() -start.getTime());
			System.out.println("持续时间1: "+still+" ms");
			addOtherInfo(listresult);		//获取DJDY.DJDYID、 XM.YWLSH, QL.QLID,QL.BDCQZH,QL.QLLX,QL.DJSJ、 FSQL.FSQLID, FSQL.CFWH

			addRightsHolderInfo(listresult);//QLID
			String cdbz = queryvalues.get("SWCDBZ");//查档的一个参数
			addLimitStatus(listresult,cdbz); //DJDYID
			addDyCfDetails(listresult);//BDCDYH && DJDYID
			isGlzd(listresult);//BDCDYID && BDCDYLX
			//添加状态
			addBDCDYZT(listresult); //BDCDYID
			//权利状态
			addQLZT(listresult); //QLID
			//添加注销状态
			addZXZT(listresult); //DJDYID
			addLimitFwStatusByZd(listresult);  //房屋查询需要新增土地的状态  就解开这句话

			addQLLX(listresult); //QLLX
			addQXGL(listresult);//BDCDYID && BDCDYLX
			
			ADDXMXX_SIZE(listresult); //DJDYID
		}

		// 格式化结果中的常量值
		for (Map map : listresult) {
			if (map.containsKey("GHYT")) {
				String value = StringHelper.formatObject(StringHelper
						.isEmpty(map.get("GHYT")) ? "" : map.get("GHYT"));
				String name = "";
				if (!StringHelper.isEmpty(value))
					name = ConstHelper.getNameByValue("FWYT", value);
				map.put("GHYTname", name);
			}
			if (map.containsKey("DJSJ"))
				map.put("DJSJ",StringHelper.FormatYmdhmsByDate(map.get("DJSJ")));
		}

		msg.setTotal(count);
		msg.setRows(listresult);
		msg.setMsg(Global.getCurrentUserName());
		Date end = new Date();
		long still = (end.getTime() -start.getTime());
		System.out.println("持续时间: "+still+" ms");
		return msg;
		
	}
	
//增加房屋查询时候的房屋状态关于所在土地状态的补充
	@SuppressWarnings("unchecked")
	protected void addLimitFwStatusByZd(List<Map> listresult) {
		if(listresult!=null && listresult.size()>0){
			for(Map map : listresult){
				long mortgageCount = 0;
				long mortgagingCount = 0;
				long SealCount = 0;
				long SealingCount = 0;
				long ObjectionCount = 0;
				long ObjectioningCount = 0;
				List<String> lstmortgage = new ArrayList<String>();
				List<String> lstmortgageing = new ArrayList<String>();
				List<String> lstseal = new ArrayList<String>();
				List<String> lstsealing = new ArrayList<String>();
				List<String> lstObjection = new ArrayList<String>();
				List<String> lstObjectioning = new ArrayList<String>();
				long LimitCount = 0,LimitingCount=0;
				if(map.containsKey("BDCDYID")&& map.containsKey("BDCDYID")){
					String LimitStatus = "";
					String mortgageStatus = "";
					String sealStatus = "";
					String objectionStatus = "";
					String zdid = "";
					String fwid = (String)map.get("BDCDYID");
					if(map.get("BDCDYZT")=="现状"){
						if (map.get("BDCDYLXMC").equals("期房")){
							List<BDCS_H_XZY> h_xzy  = baseCommonDao.getDataList(BDCS_H_XZY.class, " BDCDYID='"+fwid+"'");
							if(h_xzy!=null && h_xzy.size()>0){
								zdid = h_xzy.get(0).getZDBDCDYID();
							}
						}else{
							List<BDCS_H_XZ> h_xz  = baseCommonDao.getDataList(BDCS_H_XZ.class, " BDCDYID='"+fwid+"'");
							if(h_xz!=null && h_xz.size()>0){
								zdid = h_xz.get(0).getZDBDCDYID();
							}
						}
						/*-Start------------------CFWH-附属权利表里面的查封文号------------------------*/
						List<BDCS_FSQL_XZ> fsql_xz = baseCommonDao.getDataList(BDCS_FSQL_XZ.class, " QLID='"+map.get("QLID")+"'") ;
						if(fsql_xz!=null && fsql_xz.size()>0){
							map.put("CFWH", fsql_xz.get(0).getCFWH());
						}
						/*--End-------------------CFWH-----------------------------------------------*/
					}else if (map.get("BDCDYZT")=="历史"){
						List<BDCS_H_LS> h_ls  = baseCommonDao.getDataList(BDCS_H_LS.class, " BDCDYID='"+fwid+"'");
						if(h_ls!=null && h_ls.size()>0){
							zdid = h_ls.get(0).getZDBDCDYID();
						}
						List<BDCS_FSQL_LS> fsql_ls = baseCommonDao.getDataList(BDCS_FSQL_LS.class, " QLID='"+map.get("QLID")+"'") ;
						if(fsql_ls!=null && fsql_ls.size()>0){
							map.put("CFWH", fsql_ls.get(0).getCFWH());
						}
					}
					
					if(map.containsKey("SFGLZD") && map.get("SFGLZD")=="是" || zdid!=null){
						
//						String zdid = (String)map.get("ZDBDCDYID");
						// 已办理的业务
						StringBuilder zdsql = new StringBuilder();
						zdsql.append(" SELECT QL.QLLX,QL.DJLX,DY.BDCDYID")
						     .append(" FROM  ( SELECT BDCDYID,'02' BDCDYLX FROM BDCK.BDCS_SHYQZD_XZ UNION ALL ")
						     .append(" SELECT BDCDYID,'01' BDCDYLX FROM BDCK.BDCS_SYQZD_XZ )  DY ")
						     .append(" LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON DJDY.BDCDYID = DY.BDCDYID AND DJDY.BDCDYLX=DY.BDCDYLX ")
						     .append(" LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID = DJDY.DJDYID ")
						     .append(" WHERE QL.QLID IS NOT NULL AND DY.BDCDYID = '")
						     .append(zdid).append("'");
						List<Map> lstinfo=baseCommonDao.getDataListByFullSql(zdsql.toString());
						if(lstinfo!=null && lstinfo.size()>0){
							for(Map m : lstinfo){
								String qllx = (String) m.get("QLLX");
								String djlx = (String) m.get("DJLX");
								String bdcdyid = (String) m.get("BDCDYID");
								
								if(DJLX.CFDJ.Value.equals(djlx) && QLLX.QTQL.Value.equals(qllx)){
									if(!lstseal.contains(bdcdyid)){
										lstseal.add(bdcdyid);
										SealCount++;
									}
								}else if(DJLX.YYDJ.Value.equals(djlx)){
									if(!lstObjection.contains(bdcdyid)){
										lstObjection.add(bdcdyid);
										ObjectionCount++;
									}
								}else if(QLLX.DIYQ.Value.equals(qllx)){
									if(!lstmortgage.contains(bdcdyid)){
										lstmortgage.add(bdcdyid);
										mortgageCount++;
									}
								}
							}
						}
                 		// 正在办理的业务 			
						StringBuilder sqling = new StringBuilder();
						sqling.append(" SELECT DISTINCT QL.QLLX,QL.DJLX,XMXX.QLLX XMQLLX,XMXX.DJLX XMDJLX,DY.BDCDYID ")
						      .append(" FROM ( SELECT BDCDYID,'02' BDCDYLX FROM BDCK.BDCS_SHYQZD_XZ UNION ALL ")
						      .append(" SELECT BDCDYID,'01' BDCDYLX FROM BDCK.BDCS_SYQZD_XZ ) DY ")
						      .append(" LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON DJDY.BDCDYID = DY.BDCDYID AND DJDY.BDCDYLX = DY.BDCDYLX ")
						      .append(" LEFT JOIN BDCK.BDCS_QL_GZ QL ON QL.DJDYID = DJDY.DJDYID ")
						      .append(" LEFT JOIN BDCK.BDCS_XMXX XMXX ON XMXX.XMBH = QL.XMBH ")
						      .append(" WHERE ( XMXX.SFDB IS NULL OR XMXX.SFDB<>'1' ) AND QL.QLID IS NOT NULL ")
						      .append(" AND DY.BDCDYID = '")
						      .append(zdid).append("'");
						List<Map> lstmap=baseCommonDao.getDataListByFullSql(sqling.toString());
						if(lstmap!=null && lstmap.size()>0){
							for(Map m : lstmap){
								String qllx= StringHelper.FormatByDatatype(m.get("XMQLLX"));
								String djlx=StringHelper.FormatByDatatype(m.get("XMDJLX"));
								String bdcdyid= StringHelper.FormatByDatatype(m.get("BDCDYID"));
								if(DJLX.CFDJ.Value.equals(djlx) && QLLX.QTQL.Value.equals(qllx)){
									if(!lstseal.contains(bdcdyid) && !lstsealing.contains(bdcdyid)){
										SealingCount++;
										lstsealing.add(bdcdyid);
									}
								}else if(DJLX.YYDJ.Value.equals(djlx)){
									if(!lstObjection.contains(bdcdyid) && !lstObjectioning.contains(bdcdyid)){
										ObjectioningCount++;
										lstObjectioning.add(bdcdyid);
									}
								}else if(QLLX.DIYQ.Value.equals(qllx) && !DJLX.ZXDJ.Value.equals(djlx)){
									//if(!lstmortgage.contains(bdcdyid) && !lstmortgageing.contains(bdcdyid)){
										mortgagingCount++;
										lstmortgageing.add(bdcdyid);
									//}
								}
							}
						}
						//限制的业务
						StringBuilder strlimit=new StringBuilder();
						strlimit.append("SELECT DYXZ.YXBZ FROM (SELECT BDCDYID FROM BDCK.BDCS_SHYQZD_XZ ) DY   ");
						strlimit.append("LEFT JOIN BDCK.BDCS_DYXZ DYXZ ON DYXZ.BDCDYID=DY.BDCDYID ");
						strlimit.append("WHERE DYXZ.ID IS NOT NULL  ");
						strlimit.append(" AND DY.BDCDYID='").append(zdid).append("'");
						String limitsql=strlimit.toString();
						List<Map> limitmap = baseCommonDao.getDataListByFullSql(limitsql);
						//商品房的土地抵消状态赋值
						if(limitmap != null && limitmap.size() > 0){
							for(Map m :limitmap){
								String yxbz= StringHelper.FormatByDatatype(m.get("YXBZ"));
								if("1".equals(yxbz)){
									LimitCount++;
								}else{
									LimitingCount++;
								}
							}//
							
						}
						
						
						if(mortgageCount!= 0 || mortgagingCount!= 0){
							mortgageStatus =  MessageFormat.format("房屋{0};关联土地已抵押{1}起,正在抵押{2}起",(map.get("DYZT")==null ? map.get("BDCDYLXMC")+"无抵押" : map.get("DYZT")),mortgageCount,mortgagingCount);
						}else{
							mortgageStatus =  MessageFormat.format("房屋{0};关联土地无抵押",(map.get("DYZT")==null ? map.get("BDCDYLXMC")+"无抵押" : map.get("DYZT")));
						}
						
						if(SealCount!= 0 || SealingCount!=0 ){
							sealStatus = MessageFormat.format("房屋{0};关联土地已查封{1}起,正在查封{2}起",(map.get("CFZT")==null ? map.get("BDCDYLXMC")+"无查封" : map.get("CFZT")),SealCount,SealingCount);
						}else{
							sealStatus = MessageFormat.format("房屋{0};关联土地无查封",(map.get("CFZT")==null ? map.get("BDCDYLXMC")+"无查封" : map.get("CFZT")));
						}
						
						if(ObjectionCount!= 0){
							objectionStatus = MessageFormat.format("房屋{0};关联土地有异议{1}起",(map.get("YYZT")==null ? map.get("BDCDYLXMC")+"无异议" : map.get("YYZT")),ObjectionCount);
						}else{
							objectionStatus = MessageFormat.format("房屋{0};关联土地无异议",(map.get("YYZT")==null ? map.get("BDCDYLXMC")+"无异议" : map.get("YYZT")));
						}
						
						if(LimitCount!= 0 || LimitingCount!= 0){
							LimitStatus = MessageFormat.format("房屋{0};关联土地已限制{1}起,正在限制{2}起",(map.get("XZZT")==null ? map.get("BDCDYLXMC")+"无限制" : map.get("XZZT")),LimitCount,LimitingCount);
						}else{
							LimitStatus = MessageFormat.format("房屋{0};关联土地无限制",(map.get("XZZT")==null ? map.get("BDCDYLXMC")+"无限制" : map.get("XZZT")));
						}
						
						
						
					}else{
						// 房屋上无关联土地的情况
					    mortgageStatus =  MessageFormat.format("房屋{0};无关联的土地",(map.get("DYZT")==null ? map.get("BDCDYLXMC")+"无抵押" : map.get("DYZT")));
						sealStatus = MessageFormat.format("房屋{0};无关联的土地",(map.get("CFZT")==null ? map.get("BDCDYLXMC")+"无查封" : map.get("CFZT")));
						objectionStatus = MessageFormat.format("房屋{0};无关联的土地",(map.get("YYZT")==null ? map.get("BDCDYLXMC")+"无异议" : map.get("YYZT")));
						LimitStatus = MessageFormat.format("房屋{0};无关联的土地",(map.get("XZZT")==null ? map.get("BDCDYLXMC")+"无限制" : map.get("XZZT")));
					}
					map.put("DYZT", mortgageStatus);
					map.put("CFZT", sealStatus);
					map.put("YYZT", objectionStatus);
					map.put("XZZT", LimitStatus);
//					System.out.println(mortgageStatus);
//					System.out.println(sealStatus);
//					System.out.println(objectionStatus);
//					System.out.println(LimitStatus);
				}
			}
		}
	}

	/**
	 * 邹彦辉
	 * 不动产单元号查询
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Message queryHouseyt(Map<String, String> queryvalues, int page,
			int rows, boolean iflike, String fwzt,String sort,String order,String zddy,String fw_xzqh,String fw_qlrlx,
			String fw_fwyt,String fw_fwlx,String fw_fwxz, String fw_hx,String fw_hxjg,
			String fw_fwjg,String fw_fwcb,String fw_fzdy,String fzlb) {
		Message msg = new Message();
		//如果等于null则等于""
		String ZDDY = zddy != null ? zddy:"";
		//房屋详情
		if(ZDDY.equals("1")){
			List<Map> listresult = null;
			long count = 0;
			StringBuilder build = new StringBuilder();
			StringBuilder buildcount = new StringBuilder();
			StringBuilder fzdy = new StringBuilder();
			StringBuilder buildqlrlx = new StringBuilder();
			StringBuilder buildfwyt = new StringBuilder();
			StringBuilder buildfwlx = new StringBuilder();
			StringBuilder buildfwxz = new StringBuilder();
			StringBuilder buildhx = new StringBuilder();
			StringBuilder buildhxjg = new StringBuilder();
			StringBuilder buildfwjg = new StringBuilder();
			StringBuilder buildfwcb = new StringBuilder();
			//行政区划
			StringBuilder buildqs = new StringBuilder();
			StringBuilder buildqscou = new StringBuilder();
			StringBuilder buildyt = new StringBuilder();
			StringBuilder buildytcou = new StringBuilder();
			StringBuilder buildgx = new StringBuilder();
			StringBuilder buildgxcou = new StringBuilder();
			StringBuilder buildyj = new StringBuilder();
			StringBuilder buildyjcou = new StringBuilder();
			StringBuilder union = new StringBuilder();
			union.append(" UNION ALL");
			if (fw_fzdy.equals("房屋类型")) {
				fzdy.append("dy.fwlx"); 
			}else if (fw_fzdy.equals("户型结构")) {
				fzdy.append("dy.HX");
			}else if (fw_fzdy.equals("权利人类型")) {
				fzdy.append("qlr.qlrlx");
			}else if (fw_fzdy.equals("房屋性质")) {
				fzdy.append("dy.fwxz");
			}else if (fw_fzdy.equals("房屋结构")) {
				fzdy.append("dy.fwjg");
			}else if (fw_fzdy.equals("房屋用途")){
				fzdy.append("dy.fwyt");
			}else if (fw_fzdy.equals("户型")) {
				fzdy.append("dy.hx");
			}else if (fw_fzdy.equals("产别")) {
				fzdy.append("dy.fwcb");
			}else {
				fzdy.append("qlr.qlrlx");
			}
			
			//行政区划
			
			//权利人类型
			if(fw_qlrlx.length() > 0 && fw_qlrlx!=null && fw_qlrlx!=""){
				String[] value = fw_qlrlx.split(",");
				for (int i = 0; i < value.length; i++) {
					if (!value[i].equals("10000")) {
						buildqlrlx.delete(0, buildqlrlx.length());
						buildqlrlx.append(" and qlr.qlrlx='"+fw_qlrlx+"'");
					}else {
						buildqlrlx.append(" ");
						break;
					}
				}
			}
			
			//房屋用途
			if(fw_fwyt.length() > 0 && fw_fwyt!=null && fw_fwyt!=""){
				String[] value = fw_fwyt.split(",");
				for (int i = 0; i < value.length; i++) {
					if (!value[i].equals("10000")) {
						buildfwyt.delete(0, buildfwyt.length());
						buildfwyt.append(" and dy.fwyt='"+fw_fwyt+"'");
					}else {
						buildfwyt.append(" ");
						break;
					}
				}
			}
			
			//房屋类型
			if(fw_fwlx.length() > 0 && fw_fwlx!=null && fw_fwlx!=""){
				String[] value = fw_fwlx.split(",");
				for (int i = 0; i < value.length; i++) {
					if (!value[i].equals("10000")) {
						buildfwlx.delete(0, buildfwlx.length());
						buildfwlx.append(" and dy.fwlx='"+fw_fwlx+"'");
					}else {
						buildfwlx.append(" ");
						break;
					}
				}
			}
			
			//房屋性质
			if(fw_fwxz.length() > 0 && fw_fwxz!=null && fw_fwxz!=""){
				String[] value = fw_fwxz.split(",");
				for (int i = 0; i < value.length; i++) {
					if (!value[i].equals("10000")) {
						buildfwxz.delete(0, buildfwxz.length());
						buildfwxz.append(" and dy.fwxz='"+fw_fwxz+"'");
					}else {
						buildfwxz.append(" ");
						break;
					}
				}
			}
			
			//户型
			if(fw_hx.length() > 0 && fw_hx!=null && fw_hx!=""){
				String[] value = fw_hx.split(",");
				for (int i = 0; i < value.length; i++) {
					if (!value[i].equals("10000")) {
						buildhx.delete(0, buildhx.length());
						buildhx.append(" and dy.hx='"+fw_hx+"'");
					}else {
						buildhx.append(" ");
						break;
					}
				}
			}
			
			//户型结构
			if(fw_hxjg.length() > 0 && fw_hxjg!=null && fw_hxjg!=""){
				String[] value = fw_hxjg.split(",");
				for (int i = 0; i < value.length; i++) {
					if (!value[i].equals("10000")) {
						buildhxjg.delete(0, buildhxjg.length());
						buildhxjg.append(" and dy.hxjg='"+fw_hxjg+"'");
					}else {
						buildhxjg.append(" ");
						break;
					}
				}
			}
			
			//房屋结构
			if(fw_fwjg.length() > 0 && fw_fwjg!=null && fw_fwjg!=""){
				String[] value = fw_fwjg.split(",");
				for (int i = 0; i < value.length; i++) {
					if (!value[i].equals("10000")) {
						buildfwjg.delete(0, buildfwjg.length());
						buildfwjg.append(" and dy.fwjg='"+fw_fwjg+"'");
					}else {
						buildfwjg.append(" ");
						break;
					}
				}
			}
			
			//产别
			if(fw_fwcb.length() > 0 && fw_fwcb!=null && fw_fwcb!=""){
				String[] value = fw_fwcb.split(",");
				for (int i = 0; i < value.length; i++) {
					if (!value[i].equals("10000")) {
						buildfwcb.delete(0, buildfwcb.length());
						buildfwcb.append(" and dy.fwcb='"+fw_fwcb+"'");
					}else {
						buildfwcb.append(" ");
						break;
					}
				}
			}
			
	 buildqs.append(" select")
			.append("  DJDY.DJDYID,DY.BDCDYID,DY.ZRZBDCDYID,DY.ZDBDCDYID,DY.BDCDYH,")
			.append("  DY.ZL,DY.FH,DY.GHYT,DY.JZMJ AS SCJZMJ,DY.TNJZMJ AS SCTNJZMJ,")
			.append("  DY.FTJZMJ AS SCFTJZMJ,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC,")
			.append("  QL.QLID,QL.BDCQZH,QL.QLLX");
  buildqscou.append(" from(")
			.append("  SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
			.append("    FWJG,FWYT1 AS FWYT,HX,FWCB,'031' AS BDCDYLX,")
			.append("    '现房' AS BDCDYLXMC   FROM BDCK.BDCS_H_XZ")
			.append("  UNION ALL ")
			.append("    SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
			.append("    FWJG,FWYT1 AS FWYT,HX,FWCB,'032' AS BDCDYLX,")
			.append("    '期房' AS BDCDYLXMC  FROM BDCK.BDCS_H_XZY")
			.append("  UNION ALL")
			.append("    SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
			.append("    FWJG,FWYT1 AS FWYT,HX,FWCB,'031' AS BDCDYLX,")
			.append("    '现房' AS BDCDYLXMC   FROM BDCK.BDCS_H_XZ@TO_ORCL6_BDCK")
			.append("  UNION ALL ")
			.append("    SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
			.append("    FWJG,FWYT1 AS FWYT,HX,FWCB,'032' AS BDCDYLX,")
			.append("    '期房' AS BDCDYLXMC  FROM BDCK.BDCS_H_XZY@TO_ORCL6_BDCK ")
			.append("  UNION ALL")
			.append("    SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
			.append("    FWJG,FWYT1 AS FWYT,HX,FWCB,'031' AS BDCDYLX,")
			.append("    '现房' AS BDCDYLXMC   FROM BDCK.BDCS_H_XZ@TO_ORCL7_BDCK")
			.append("  UNION ALL ")
			.append("    SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
			.append("    FWJG,FWYT1 AS FWYT,HX,FWCB,'032' AS BDCDYLX,")
			.append("    '期房' AS BDCDYLXMC  FROM BDCK.BDCS_H_XZY@TO_ORCL7_BDCK   ")  
			.append("    ) DY")
			.append("  left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid ")
			.append("  left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid   AND (QLLX=4 OR qllx=99 or qllx=23 or DJLX=600)")
			.append("  left join BDCK.bdcs_qlr_xz qlr on ql.qlid  = qlr.qlid")
			.append("  WHERE DY.BDCDYID is not null");
	 buildyt.append(" select ")//鹰潭
			.append("  DJDY.DJDYID,DY.BDCDYID,DY.ZRZBDCDYID,DY.ZDBDCDYID,DY.BDCDYH,")
			.append("  DY.ZL,DY.FH,DY.GHYT,DY.JZMJ AS SCJZMJ,DY.TNJZMJ AS SCTNJZMJ,")
			.append("  DY.FTJZMJ AS SCFTJZMJ,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC,")
			.append("  QL.QLID,QL.BDCQZH,QL.QLLX");
  buildytcou.append(" from")
			.append("    (SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
			.append("    FWJG,FWYT1 AS FWYT,HX,FWCB,'031' AS BDCDYLX,")
			.append("    '现房' AS BDCDYLXMC   FROM BDCK.BDCS_H_XZ")
			.append("  UNION ALL ")
			.append("    SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
			.append("    FWJG,FWYT1 AS FWYT,HX,FWCB,'032' AS BDCDYLX,")
			.append("    '期房' AS BDCDYLXMC  FROM BDCK.BDCS_H_XZY")
			.append("    ) DY")
			.append("  left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid ")
			.append("  left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid   AND (QLLX=4 OR qllx=99 or qllx=23 or DJLX=600)")
			.append("  left join BDCK.bdcs_qlr_xz qlr on ql.qlid  = qlr.qlid")
			.append("  WHERE DY.BDCDYID is not null");
			//.append("  ORDER BY DY.BDCDYID asc");
	 buildgx.append(" select ")//贵溪
			.append("  DJDY.DJDYID,DY.BDCDYID,DY.ZRZBDCDYID,DY.ZDBDCDYID,DY.BDCDYH,")
			.append("  DY.ZL,DY.FH,DY.GHYT,DY.JZMJ AS SCJZMJ,DY.TNJZMJ AS SCTNJZMJ,")
			.append("  DY.FTJZMJ AS SCFTJZMJ,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC,")
			.append("  QL.QLID,QL.BDCQZH,QL.QLLX");
  buildgxcou.append(" from")
			.append("    (SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
			.append("    FWJG,FWYT1 AS FWYT,HX,FWCB,'031' AS BDCDYLX,")
			.append("    '现房' AS BDCDYLXMC   FROM BDCK.BDCS_H_XZ@TO_ORCL6_BDCK")
			.append("  UNION ALL ")
			.append("    SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
			.append("    FWJG,FWYT1 AS FWYT,HX,FWCB,'032' AS BDCDYLX,")
			.append("    '期房' AS BDCDYLXMC  FROM BDCK.BDCS_H_XZY@TO_ORCL6_BDCK")
			.append("    ) DY")
			.append("  left join BDCK.BDCS_DJDY_XZ@TO_ORCL6_BDCK DJDY on dy.bdcdyid=djdy.bdcdyid ")
			.append("  left join BDCK.bdcs_ql_xz@TO_ORCL6_BDCK ql on ql.djdyid=djdy.djdyid   AND (QLLX=4 OR qllx=99 or qllx=23 or DJLX=600)")
			.append("  left join BDCK.bdcs_qlr_xz@TO_ORCL6_BDCK qlr on ql.qlid  = qlr.qlid")
			.append("  WHERE DY.BDCDYID is not null");
			//.append("  ORDER BY DY.BDCDYID asc");
     buildyj.append("  select")//余江
			.append("  DJDY.DJDYID,DY.BDCDYID,DY.ZRZBDCDYID,DY.ZDBDCDYID,DY.BDCDYH,")
			.append("  DY.ZL,DY.FH,DY.GHYT,DY.JZMJ AS SCJZMJ,DY.TNJZMJ AS SCTNJZMJ,")
			.append("  DY.FTJZMJ AS SCFTJZMJ,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC,")
			.append("  QL.QLID,QL.BDCQZH,QL.QLLX");
  buildyjcou.append(" from")
			.append("    (SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
			.append("    FWJG,FWYT1 AS FWYT,HX,FWCB,'031' AS BDCDYLX,")
			.append("    '现房' AS BDCDYLXMC   FROM BDCK.BDCS_H_XZ@TO_ORCL7_BDCK")
			.append("  UNION ALL ")
			.append("    SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
			.append("    FWJG,FWYT1 AS FWYT,HX,FWCB,'032' AS BDCDYLX,")
			.append("    '期房' AS BDCDYLXMC  FROM BDCK.BDCS_H_XZY@TO_ORCL7_BDCK")
			.append("    ) DY")
			.append("  left join BDCK.BDCS_DJDY_XZ@TO_ORCL7_BDCK DJDY on dy.bdcdyid=djdy.bdcdyid ")
			.append("  left join BDCK.bdcs_ql_xz@TO_ORCL7_BDCK ql on ql.djdyid=djdy.djdyid   AND (QLLX=4 OR qllx=99 or qllx=23 or DJLX=600)")
			.append("  left join BDCK.bdcs_qlr_xz@TO_ORCL7_BDCK qlr on ql.qlid  = qlr.qlid")
			.append("  WHERE DY.BDCDYID is not null");
			//.append("  ORDER BY DY.BDCDYID asc");
			
				
	   build.append(" select")
			.append(" DJDY.DJDYID,DY.BDCDYID,DY.ZRZBDCDYID,DY.ZDBDCDYID,DY.BDCDYH,")
			.append(" DY.ZL,DY.FH,DY.GHYT,DY.JZMJ AS SCJZMJ,DY.TNJZMJ AS SCTNJZMJ,")
			.append(" DY.FTJZMJ AS SCFTJZMJ,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC,")
			.append(" QL.QLID,QL.BDCQZH,QL.QLLX");
  buildcount.append(" from")
			.append(" (SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
			.append(" FWJG,FWYT1 AS FWYT,HX,FWCB,'031' AS BDCDYLX,XMBH,")
			.append(" '现房' AS BDCDYLXMC   FROM BDCK.BDCS_H_XZ")
			.append(" UNION ALL ")
			.append(" SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
			.append(" FWJG,FWYT1 AS FWYT,HX,FWCB,'032' AS BDCDYLX,XMBH,")
			.append(" '期房' AS BDCDYLXMC  FROM BDCK.BDCS_H_XZY") 
			.append(" ) DY")
	   		.append(" left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid ") 
	   		.append(" left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid   AND (QLLX=4 OR qllx=99 or qllx=23 or DJLX=600)") 
	   		.append(" left join BDCK.bdcs_qlr_xz qlr on ql.qlid  = qlr.qlid")
	   		.append(" left join BDCK.BDCS_XMXX xmxx on  xmxx.xmbh = dy.XMBH")
	   		.append(" left join BDCK.BDCS_SHYQZD_XZ ZD ON DY.ZDBDCDYID=ZD.BDCDYID")
	   		.append(" LEFT JOIN BDCK.BDCS_const const on "+fzdy+" = const.constvalue") 
	   		.append(" LEFT JOIN BDCK.BDCS_CONSTCLS CONSTCLS on const.CONSTSLSID = CONSTCLS.CONSTSLSID") 
	   		.append(" WHERE dy.bdcdyid is not null");
	   		if(!fzlb.equals("null")){
	   			buildcount.append(" and const.CONSTTRANS="+fzlb)
	   			.append(buildqlrlx+""+buildfwyt+""+buildfwlx+""+buildfwxz+""+buildhx+""+buildhxjg+""+buildfwjg+""+buildfwcb);//+" ORDER BY DY.BDCDYID asc");
	   		}else{
	   			buildcount.append(buildqlrlx+""+buildfwyt+""+buildfwlx+""+buildfwxz+""+buildhx+""+buildhxjg+""+buildfwjg+""+buildfwcb);//+" ORDER BY DY.BDCDYID asc");
	   		}
	   		
			CommonDao dao = baseCommonDao;
			
			if (fw_fzdy.equals("行政区划")) {
				if (fzlb.equals("鹰潭市")) {
					count = dao.getCountByFullSql(buildqscou.toString());
					listresult = dao.getPageDataByFullSql(buildqs.toString()+buildqscou.toString(), page, rows);
				}else if(fzlb.equals("市本级")){
					count = dao.getCountByFullSql(buildytcou.toString());
					listresult = dao.getPageDataByFullSql(buildyt.toString()+buildytcou.toString(), page, rows);
				}else if(fzlb.equals("贵溪")){
					count = dao.getCountByFullSql(buildgxcou.toString());
					listresult = dao.getPageDataByFullSql(buildgx.toString()+buildgxcou.toString(), page, rows);
				}else if(fzlb.equals("余江")){
					count = dao.getCountByFullSql(buildyjcou.toString());
					listresult = dao.getPageDataByFullSql(buildyj.toString()+buildyjcou.toString(), page, rows);
				}
			}else{
				
				count = dao.getCountByFullSql(buildcount.toString());
				listresult = dao.getPageDataByFullSql(build.toString()+buildcount.toString(), page, rows);
			}
			
			List<Map> list = new ArrayList<Map>();
			if(listresult.size()>0 && listresult.size()<5){
				for(Map map : listresult){
					if(map.get("BDCDYLXMC").equals("现房")){
						list.add(map);
					}
				}
			}
			if(list.size()>0){
			 addRightsHolderInfo(list);
				addLimitStatus(list,"");
				addDyCfDetails(list);
				isGlzd(list);
		        addQLLX(list);
		        addQXGL(list);
				
				// 格式化结果中的常量值

				for (Map map : list) {
					if (map.containsKey("GHYT")) {
						String value = StringHelper.formatObject(StringHelper
								.isEmpty(map.get("GHYT")) ? "" : map.get("GHYT"));
						String name = "";
						if (!StringHelper.isEmpty(value)) {
							name = ConstHelper.getNameByValue("FWYT", value);
						}
						map.put("GHYTname", name);
					}
				}
				msg.setTotal(count);
				msg.setRows(list);
			}else{
			
			if (fw_fzdy.equals("行政区划")) {
				addRightsHolderInfo(listresult);
				addLimitStatus(listresult,"");
				addDyCfDetails(listresult);
				isGlzd(listresult);
		        addQLLX(listresult);
		        addQXGL(listresult);
				
			}else{
				
				addRightsHolderInfo(listresult);
				addLimitStatus(listresult,"");
				addDyCfDetails(listresult);
				isGlzd(listresult);
		        addQLLX(listresult);
		        addQXGL(listresult);
			}
			// 格式化结果中的常量值
			for (Map map : listresult) {
				if (map.containsKey("GHYT")) {
					String value = StringHelper.formatObject(StringHelper
							.isEmpty(map.get("GHYT")) ? "" : map.get("GHYT"));
						String name = "";
						if (!StringHelper.isEmpty(value)) {
							name = ConstHelper.getNameByValue("FWYT", value);
						}
							map.put("GHYTname", name);
					}
				}
			msg.setTotal(count);
			msg.setRows(listresult);
		}
		//土地详情---------------------------------------------------------------------------------------------------------------
		}else {
			long count = 0;
			List<Map> listresult = null;
			String cxzt=queryvalues.get("CXZT");
			queryvalues.remove("CXZT");
			String fsqlcfwh=queryvalues.get("FSQL.CFWH");
			/* ===============1、先获取实体对应的表名=================== */
			String unitentityName = "BDCK.BDCS_H_XZ";
			if ("2".equals(cxzt)) {
				unitentityName = "BDCK.BDCS_H_LS";
			}

			/* ===============2、再获取表名+'_'+字段名=================== */
			String dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.ZRZBDCDYID,DY.ZDBDCDYID,DY.BDCDYH,DY.ZL,DY.FH,DY.GHYT,DY.FWBM,DY.SCJZMJ,DY.SCTNJZMJ,DY.SCFTJZMJ,'031' AS BDCDYLX ,'现房' AS BDCDYLXMC ";
			String qlfieldsname = "QL.QLID,QL.BDCQZH,QL.QLLX";
			String fsqlfieldsname = "FSQL.FSQLID,FSQL.CFWH";

			if (fwzt != null && fwzt.equals("2")) {
				unitentityName = "BDCK.BDCS_H_XZY";
				if ("2".equals(cxzt)) {
					unitentityName = "BDCK.BDCS_H_LSY";
				}
				dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.ZRZBDCDYID,DY.ZDBDCDYID,DY.BDCDYH,DY.ZL,DY.FH,DY.GHYT,DY.FWBM,DY.YCJZMJ AS SCJZMJ,DY.YCTNJZMJ AS SCTNJZMJ,DY.YCFTJZMJ AS SCFTJZMJ,'032' AS BDCDYLX,'期房' AS BDCDYLXMC ";
			}

			// 统一期现房 2015年10月28日
			if (fwzt != null && fwzt.equals("3")) {
				unitentityName = "(SELECT ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ ,'031' AS BDCDYLX ,'现房' AS BDCDYLXMC FROM BDCK.BDCS_H_XZ UNION ALL SELECT ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ  ,'032' AS BDCDYLX ,'期房' AS BDCDYLXMC  FROM BDCK.BDCS_H_XZY)";
				if ("2".equals(cxzt)) {
					unitentityName = "(SELECT ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ ,'031' AS BDCDYLX ,'现房' AS BDCDYLXMC FROM BDCK.BDCS_H_LS UNION ALL SELECT ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ  ,'032' AS BDCDYLX ,'期房' AS BDCDYLXMC  FROM BDCK.BDCS_H_LSY)";
				}
				dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.ZRZBDCDYID,DY.ZDBDCDYID,DY.BDCDYH,DY.ZL,DY.FH,DY.GHYT,DY.JZMJ AS SCJZMJ,DY.TNJZMJ AS SCTNJZMJ,DY.FTJZMJ AS SCFTJZMJ,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC";
			}

			/* ===============3、构造查询语句=================== */
			/* SELECT 字段部分 */
			StringBuilder builder2 = new StringBuilder();
			builder2.append("select ").append(dyfieldsname).append(",")
					.append(qlfieldsname);
			if(!StringHelper.isEmpty(fsqlcfwh)){
				builder2.append(",").append(fsqlfieldsname);
			}
			String selectstr = builder2.toString();

			/* FROM 后边的表语句 */
			StringBuilder builder = new StringBuilder();
			if ("2".equals(cxzt)) {
				builder.append(" from {0} DY")
						.append(" left join BDCK.BDCS_DJDY_LS DJDY on dy.bdcdyid=djdy.bdcdyid")
						.append(" left join BDCK.bdcs_ql_ls ql on ql.djdyid=djdy.djdyid  ");
			}else{
				builder.append(" from {0} DY")
						.append(" left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid")
						.append(" left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid AND QLLX='4' ");
			}
			if(!StringHelper.isEmpty(fsqlcfwh)){
				builder.append(" left join BDCK.bdcs_fsql_xz fsql on ql.fsqlid=fsql.fsqlid  ");
			}
			String fromstr = builder.toString();
			fromstr = MessageFormat.format(fromstr, unitentityName);

			/* WHERE 条件语句 */
			StringBuilder builder3 = new StringBuilder();
			builder3.append(" where  ");
			StringBuilder qlrbuilder = new StringBuilder();
			StringBuilder dyrbuilder = new StringBuilder();
			boolean havecondition = false;
			boolean haveqlr = false;
			boolean havedyr = false;
			for (Entry<String, String> ent : queryvalues.entrySet()) {
				String name = ent.getKey();
				String value = ent.getValue();
				if (!StringHelper.isEmpty(value)) {
					// 抵押状态和查封状态，为‘0’的时候表示不过滤，查询全部
					if ((name.equals("CFZT") && value.equals("0"))
							|| (name.equals("DYZT") && value.equals("0"))
								||name.equals("YYZT") && value.equals("0")) {
						continue;
					}

					// 权利人，权利人的条件比较特殊，where里边又套了where
					if (name.startsWith("QLR.")) {
						if (haveqlr)
							qlrbuilder.append(" and ");
						if (iflike) {
							qlrbuilder.append(" instr(" + name + ",'" + value
									+ "')>0 ");
						} else {
							if (name.equals("QLR.ZJH") && value.length() == 18) {
								String oldCard = ConverIdCard.getOldIDCard(value);
								if (oldCard.length() != 15) {
									qlrbuilder.append(" " + name + "='" + value
											+ "' ");
								} else {
									qlrbuilder.append(" (" + name + "='" + value
											+ "' or ");
									qlrbuilder.append(" " + name + "='" + oldCard
											+ "') ");
								}
							} else {
								qlrbuilder.append(" " + name + "='" + value + "' ");
							}
						}
						haveqlr = true;
						continue;
					}
					// 抵押人判断
					if (name.startsWith("DYR.")) {
						if (havedyr)
							dyrbuilder.append(" and ");
						dyrbuilder.append(" " + name + "='" + value + "' ");
						havedyr = true;
						continue;
					}
					if (havecondition) {
						builder3.append(" and ");
					}

					// 抵押状态
					if (name.equals("DYZT")) {
						if (value.equals("1")) {
							if ("2".equals(cxzt)) {
								builder3.append(" djdy.djdyid NOT IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_LS DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
							}else{
								builder3.append(" djdy.djdyid NOT IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_XZ DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
							}
						} else {
							if ("2".equals(cxzt)) {
								builder3.append(" djdy.djdyid IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_LS DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
							}else{
								builder3.append(" djdy.djdyid IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_XZ DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
							}
						}
						havecondition = true;
						continue;
					}
					// 查封状态
					if (name.equals("CFZT")) {
						if (value.equals("1")) {
							if ("2".equals(cxzt)) {
								builder3.append("  djdy.DJDYID NOT IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_LS CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99') ");
							}else{
								builder3.append("  djdy.DJDYID NOT IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_XZ CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99') ");
							}
						} else {
							if ("2".equals(cxzt)) {
								builder3.append("  djdy.DJDYID IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_LS CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99') ");
							}else{
								builder3.append("  djdy.DJDYID IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_XZ CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99') ");
							}
						}
						havecondition = true;
						continue;
					}
					// 异议状态
					if (name.equals("YYZT")) {
						if (value.equals("1")) {
							if ("2".equals( cxzt)) {
								builder3.append("  djdy.DJDYID NOT IN (SELECT YY.DJDYID FROM BDCK.BDCS_QL_LS YY WHERE YY.DJDYID=DJDYID AND YY.djlx = '600') ");
							}else{
								builder3.append("  djdy.DJDYID NOT IN (SELECT YY.DJDYID FROM BDCK.BDCS_QL_XZ YY WHERE YY.DJDYID=DJDYID AND YY.djlx = '600') ");
							}
						} else {
							if ("2".equals(cxzt)) {
								builder3.append("  djdy.DJDYID IN (SELECT YY.DJDYID FROM BDCK.BDCS_QL_LS YY WHERE YY.DJDYID=DJDYID AND  YY.djlx = '600') ");
							}else{
								builder3.append("  djdy.DJDYID IN (SELECT YY.DJDYID FROM BDCK.BDCS_QL_XZ YY WHERE YY.DJDYID=DJDYID AND  YY.djlx = '600') ");
							}
						}
						havecondition = true;
						continue;
					}

					if (iflike) {
						builder3.append(" instr(" + name + ",'" + value + "')>0 ");
					} else {
						/*
						 * 如果通过不动产权证查询，且是精确查询时,
						 * 其中如果不动产权证号是否纯数字的，直接在权利人表中通过BDCQZHXH条件查询，
						 * 否则从权利表中通过BDCQZH条件查询
						 */
						if (name.contains("BDCQZH")) {
							// 验证不动产权证号是否为纯数字的
							boolean flag = StringHelper.isNumericByString(value);
							if (flag) {
								// 判断是否已经有查询权利人的条件了
								if (haveqlr) {
									qlrbuilder.append(" and ");
								}
								qlrbuilder.append(" QLR.BDCQZHXH " + "='" + value
										+ "' ");
								haveqlr = true;
								continue;
							} else {
								builder3.append(" " + name + "='" + value + "' ");
							}
						} else {
							builder3.append(" " + name + "='" + value + "' ");
						}
					}
					havecondition = true;
				}
			}
			// 有权利人查询条件
			if (!StringHelper.isEmpty(qlrbuilder.toString())) {
				if (havecondition) {
					builder3.append(" and ");
				}
				if ("2".equals(cxzt)) {
					builder3.append(" EXISTS (SELECT 1 FROM BDCK.BDCS_QLR_LS QLR WHERE QLR.QLID=QL.QLID AND "
							+ qlrbuilder.toString() + ")");
				}else{
					builder3.append(" EXISTS (SELECT 1 FROM BDCK.BDCS_QLR_XZ QLR WHERE QLR.QLID=QL.QLID AND "
							+ qlrbuilder.toString() + ")");
				}
				havecondition = true;
			}

			// 有抵押人查询条件
			if (!StringHelper.isEmpty(dyrbuilder.toString())) {
				if (havecondition) {
					builder3.append(" and ");
				}
				if ("2".equals(cxzt)) {
					builder3.append(" QL.QLID IN (SELECT QLID FROM BDCK.BDCS_FSQL_LS DYR WHERE "
							+ dyrbuilder.toString() + ")");
				}else{
					builder3.append(" QL.QLID IN (SELECT QLID FROM BDCK.BDCS_FSQL_XZ DYR WHERE "
							+ dyrbuilder.toString() + ")");
				}
				havecondition = true;
			}

			if (fwzt == null || !fwzt.equals("2")) {
				if (havecondition) {
					// builder3.append(" and ");
				}
				// builder3.append(" ql.qllx='4'");
			}
			
			String wherestr = builder3.toString();
			if (wherestr.trim().toUpperCase().endsWith("WHERE")) {
				wherestr = "";
			}
			String fromSql = " from (select ql.qlid " + fromstr + wherestr + ")";
			String fullSql = selectstr + fromstr + wherestr;
			/* 排序 条件语句 */
			if(!StringUtils.isEmpty(sort)&&!StringUtils.isEmpty(order))
			{
			if(sort.toUpperCase().equals("ZL"))
				sort="DY.ZL";
			if(sort.toUpperCase().equals("BDCDYH"))
				sort="DY.BDCDYH";
			/*if(sort.toUpperCase().equals("QLRMC"))
				sort="QLR.QLRMC";
			if(sort.toUpperCase().equals("ZJH"))
				sort="QLR.ZJH";*/
			if(sort.toUpperCase().equals("BDCQZH"))
				sort="QL.BDCQZH";
			if(sort.toUpperCase().equals("FH"))
				sort="DY.FH";
			if(sort.toUpperCase().equals("QLLX"))
				sort="QL.QLLX";
			/*if(sort.toUpperCase().equals("ZJHM"))
				sort="QLR.ZJHM";*/
			if(sort.toUpperCase().equals("GHYTNAME"))
				sort="DY.GHYT";
			if(sort.toUpperCase().equals("BDCDYID"))
				sort="DY.BDCDYID";
			if(sort.toUpperCase().equals("BDCDYLXMC"))
				sort="DY.BDCDYLX";		
				fullSql=fullSql+" ORDER BY "+sort+" "+order;
			}
			/* 为了提高效率，先不查询符合条件的记录总数，将来改为异步获取 */
			CommonDao dao = baseCommonDao;

			count = dao.getCountByFullSql(fromSql);
			listresult = dao.getPageDataByFullSql(fullSql, page, rows);
			List<Map> list = new ArrayList<Map>();
			if(listresult.size()>0 && listresult.size()<5){
			 for(Map map : listresult){
				if(map.get("BDCDYLXMC").equals("现房")){
					list.add(map);
				}
			 }
			}
			if(list.size()>0){
			 addRightsHolderInfo(list);
				addLimitStatus(list,"");
				addDyCfDetails(list);
				isGlzd(list);
		        addQLLX(list);
		        addQXGL(list);
				
				// 格式化结果中的常量值

				for (Map map : list) {
					if (map.containsKey("GHYT")) {
						String value = StringHelper.formatObject(StringHelper
								.isEmpty(map.get("GHYT")) ? "" : map.get("GHYT"));
						String name = "";
						if (!StringHelper.isEmpty(value)) {
							name = ConstHelper.getNameByValue("FWYT", value);
						}
						map.put("GHYTname", name);
					}
				}

				msg.setTotal(count);
				msg.setRows(list);
				
			 
			}else{
			
			addRightsHolderInfo(listresult);
			addLimitStatus(listresult,"");
			addDyCfDetails(listresult);
			isGlzd(listresult);
	        addQLLX(listresult);
	        addQXGL(listresult);
			
			// 格式化结果中的常量值

			for (Map map : listresult) {
				if (map.containsKey("GHYT")) {
					String value = StringHelper.formatObject(StringHelper
							.isEmpty(map.get("GHYT")) ? "" : map.get("GHYT"));
					String name = "";
					if (!StringHelper.isEmpty(value)) {
						name = ConstHelper.getNameByValue("FWYT", value);
					}
					map.put("GHYTname", name);
				}
			}

			msg.setTotal(count);
			msg.setRows(listresult);
			
		  }
		}
	
		return msg;
	}
	

	/**
	 * 判断是否关联宗地
	 * 
	 * @param listresult
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void isGlzd(List<Map> listresult) {
		if (listresult != null && listresult.size() > 0) {
			for (Map map : listresult) {
				if (map.containsKey("BDCDYID") && map.containsKey("BDCDYLX")) {
					String bdcdyid = (String) map.get("BDCDYID");
					String bdcdylx = (String) map.get("BDCDYLX");
					if (!StringHelper.isEmpty(bdcdyid)
							&& !StringHelper.isEmpty(bdcdylx)) {
						RealUnit Hunit = UnitTools.loadUnit(
								BDCDYLX.initFrom(bdcdylx),
								DJDYLY.initFrom("02"), bdcdyid);
						if (Hunit != null
								&& (BDCDYLX.H.equals(BDCDYLX.initFrom(bdcdylx))
								|| BDCDYLX.YCH
										.equals(BDCDYLX.initFrom(bdcdylx)))) {
							House h = (House) Hunit;
							String zdbdcdyid = h.getZDBDCDYID();
							if (!StringHelper.isEmpty(zdbdcdyid)) {
								List<BDCS_SHYQZD_XZ> listzd = baseCommonDao
										.getDataList(BDCS_SHYQZD_XZ.class,
												"BDCDYID='" + zdbdcdyid + "'");
								if (listzd != null && listzd.size() > 0) {
									map.put("SFGLZD", "是");
									String zdlx = listzd.get(0).getBDCDYLX().Value;
									String zdzl = listzd.get(0).getZL();
									map.put("ZDBDCDYID", zdbdcdyid);
									map.put("ZDBDCDYLX", zdlx);
									map.put("ZDZL", zdzl);
								} else {
									map.put("SFGLZD", "否");
								}
							} else {
								map.put("SFGLZD", "否");
							}

						}
					}
				}
			}
		}
	}
	/**
	 * 获取期现关联情况
	 * 
	 * @param listresult
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void addQXGL(List<Map> listresult) {
		if (listresult != null && listresult.size() > 0) {
			for (Map map : listresult) {
				if (map.containsKey("BDCDYID") && map.containsKey("BDCDYLX")) {					
					String bdcdylx = (String) map.get("BDCDYLX");
					String bdcdyid = (String) map.get("BDCDYID");
					if (!StringHelper.isEmpty(bdcdyid)
							&& !StringHelper.isEmpty(bdcdylx)) {						
						String fulSql = "";
						long count = 0;
						if(bdcdylx.equals("032")){
							fulSql = MessageFormat
									.format("from BDCK.YC_SC_H_XZ WHERE YCBDCDYID=''{0}'' ",
											bdcdyid);
							List<Map> list = baseCommonDao.getDataListByFullSql("SELECT * "+fulSql);
							if(list!=null){
								count = list.size();
								if (count>0) {
									String SCBDCDYID = StringHelper.formatObject(list.get(0).get("SCBDCDYID"));
									map.put("QXFGL", "已关联现房");
									map.put("QXFGLID", SCBDCDYID);
									//打开登记簿还需要zl和bdcdyh
									List<Map> list_xf = baseCommonDao.getDataListByFullSql("SELECT ZL,BDCDYH FROM BDCK.BDCS_H_XZ WHERE BDCDYID='"+SCBDCDYID+"' ");
									if (list_xf != null && list_xf.size()>0) {
										map.put("QXFGL_ZL", StringHelper.formatObject(list_xf.get(0).get("ZL")));
										map.put("QXFGL_BDCDYH", StringHelper.formatObject(list_xf.get(0).get("BDCDYH")));
									}
								}else {
									map.put("QXFGL", "未关联现房");									
								}
							}	
						}else if (bdcdylx.equals("031")) {
							fulSql = MessageFormat
									.format("from BDCK.YC_SC_H_XZ WHERE  SCBDCDYID=''{0}'' ",
											bdcdyid);
							List<Map> list = baseCommonDao.getDataListByFullSql("SELECT * "+fulSql);
							if(list!=null){
								count = list.size();
								if (count>0) {
									map.put("QXFGL", "已关联期房");
									map.put("QXFGLID", list.get(0).get("YCBDCDYID"));
								}else {
									map.put("QXFGL", "未关联期房");
								}
							}
						}
					
					}else {
						map.put("QXFGL", "未关联");
					}
				}
			}
		}
	}
	
//给权利类型赋值
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void addQLLX(List<Map> result) {
		if (result != null && result.size() > 0) {
			for (Map map : result) 
			{
				String qllxname=ConstHelper.getNameByValue("QLLX",String.valueOf(map.get("QLLX")));
				map.put("QLLX", qllxname);
			}
			}
	}
	//登记查封状态的详情信息
    @SuppressWarnings({ "rawtypes", "unchecked" })
	protected void addDyCfDetails(List<Map> result){
    	if (result != null && result.size() > 0) {
			for (Map map : result) {
				if(!StringUtils.isEmpty(map.get("BDCDYH"))&&!StringUtils.isEmpty(map.get("DJDYID")))
				{
					String dycfdetailssql = MessageFormat.format("select fsql.cfjg,fsql.cfwh,fsql.dyr,ql.qlqssj,ql.qljssj,ql.qllx,ql.djlx,ql.qlid "
							+ ",(CASE WHEN FSQL.DYFS='1' THEN TO_NUMBER(FSQL.BDBZZQSE)  WHEN FSQL.DYFS='2' THEN TO_NUMBER(FSQL.ZGZQSE) ELSE 0 END) AS DYJE       "
							+ " from bdck.bdcs_ql_xz ql left join bdck.bdcs_fsql_xz fsql on ql.qlid=fsql.qlid where ql.bdcdyh=''{0}'' and ql.djdyid=''{1}'' and ql.qllx in(''99'',''23'') ",
							map.get("BDCDYH"),map.get("DJDYID"));
					
					List<Map> dycfs=baseCommonDao.getDataListByFullSql(dycfdetailssql);
					String dyqr ="",dyje = "";
					for (Map dycf : dycfs) {
						if("800".equals(dycf.get("DJLX"))&&"99".equals(dycf.get("QLLX"))){
							map.put("CFJG", dycf.get("CFJG"));
							map.put("CFWH", dycf.get("CFWH"));
							map.put("CFQX", DateUtil.FormatByDatetime(dycf.get("QLQSSJ"))+" 至  "+DateUtil.FormatByDatetime(dycf.get("QLJSSJ")));
						}
						if("23".equals(dycf.get("QLLX"))){
							map.put("DYR", dycf.get("DYR"));
							map.put("DYQX", DateUtil.FormatByDatetime(dycf.get("QLQSSJ"))+" 至  "+DateUtil.FormatByDatetime(dycf.get("QLJSSJ")));
							String dyjeex=StringHelper.formatObject(dycf.get("DYJE"));
							dyje = StringHelper.isEmpty(dyje)?dyjeex:dyje + "/" + dyjeex;
							String qlid =  dycf.get("QLID").toString();
							if (qlid != null) {
								RightsHolder holder = RightsHolderTools
										.getUnionRightsHolder(DJDYLY.XZ, qlid);
								dyqr = StringHelper.isEmpty(dyqr)?holder.getQLRMC():dyqr+"/" + holder.getQLRMC();
							}
						}
					}
					map.put("DYJE",dyje);
					map.put("DYQR",dyqr);
				}
				
			}
    	}
    }
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void addLimitStatus(List<Map> result,String cdbz) {
		if (result != null && result.size() > 0) {
			for (Map map : result) {
				if (map.containsKey("DJDYID")) {
					String djdyid = (String) map.get("DJDYID");
					String ycdjdyid = "";
					String scdjdyid = "";
					if (djdyid != null) {
						List<BDCS_DJDY_XZ> listdjdy = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "djdyid='"
										+ djdyid + "'");
						if (listdjdy != null && listdjdy.size() > 0) {
							BDCDYLX lx = BDCDYLX.initFrom(listdjdy.get(0).getBDCDYLX());
							if (lx!=null&&lx.Value.equals("031")) {
								String valuedyxz = "";
								String valuecfxz = "";
								String valueyyxz = "";
								String valuexzxz = "";	
                                String valueygxz = "";
								
								List<YC_SC_H_XZ> listycsc = baseCommonDao.getDataList(YC_SC_H_XZ.class,
												"scbdcdyid='" + listdjdy.get(0).getBDCDYID() + "'");
								if (listycsc != null && listycsc.size() > 0
										&& listycsc.get(0).getYCBDCDYID() != null) {
									List<BDCS_DJDY_XZ> listdjdyyc = baseCommonDao.getDataList(BDCS_DJDY_XZ.class,
													"BDCDYID='" + listycsc.get(0).getYCBDCDYID() + "'");
									if (listdjdyyc != null && listdjdyyc.size() > 0) {
										ycdjdyid = listdjdyyc.get(0).getDJDYID();
										Map<String, String> mapxzy = new HashMap<String, String>();
										mapxzy = getDYandCFandYY_XZY(ycdjdyid,cdbz);
										for (Entry<String, String> ent : mapxzy.entrySet()) {
											String name = ent.getKey();
											if (name.equals("DYZT")) {
												if (StringHelper.isEmpty(valuedyxz)) {
													valuedyxz = ent.getValue();
												} else {
													valuedyxz = valuedyxz + "、" + ent.getValue();
												}
											} else if (name.equals("CFZT")) {
												if (StringHelper.isEmpty(valuecfxz)) {
													valuecfxz = ent.getValue();
												} else {
													valuecfxz = valuecfxz + "、" + ent.getValue();
												}
											} else if (name.equals("YYZT")){
												if (StringHelper.isEmpty(valueyyxz)) {
													valueyyxz = ent.getValue();
												} else {
													valueyyxz = valueyyxz + "、" + ent.getValue();
												}
											}else if (name.equals("XZZT")){
												if (StringHelper.isEmpty(valuexzxz)) {
													valuexzxz = ent.getValue();
												} else {
													valuexzxz = valuexzxz + "、" + ent.getValue();
												}
											}
										}
									}
								}
								Map<String, String> mapxz = new HashMap<String, String>();
								mapxz = getDYandCFandYY_XZ(djdyid,cdbz);
								for (Entry<String, String> ent : mapxz.entrySet()) {
									String name = ent.getKey();
									if (name.equals("DYZT")) {
										if (StringHelper.isEmpty(valuedyxz)) {
											valuedyxz = ent.getValue();
										} else {
											valuedyxz = valuedyxz + " " + ent.getValue();
										}
									} else if (name.equals("CFZT")) {
										if (StringHelper.isEmpty(valuecfxz)) {
											valuecfxz = ent.getValue();
										} else {
											valuecfxz = valuecfxz + " " + ent.getValue();
										}
									} else if (name.equals("YYZT")){
										if (StringHelper.isEmpty(valueyyxz)) {
											valueyyxz = ent.getValue();
										} else {
											valueyyxz = valueyyxz + " " + ent.getValue();
										}
									}else if (name.equals("YGDJZT")){
										if (StringHelper.isEmpty(valueyyxz)) {
											valueygxz = ent.getValue();
										} else {
											valueygxz = valueygxz + " "
													+ ent.getValue();
										}
									}else if (name.equals("XZZT")){
										if (StringHelper.isEmpty(valuexzxz)) {
											valuexzxz = ent.getValue();
										} else {
											valuexzxz = valuexzxz + " " + ent.getValue();
										}
									}
								}
								map.put("DYZT", valuedyxz);
								map.put("CFZT", valuecfxz);
								map.put("YYZT", valueyyxz);
								map.put("XZZT", valuexzxz);
                                map.put("YGDJZT", valueygxz);
								
							} else {
								List<YC_SC_H_XZ> listycsc = baseCommonDao.getDataList(YC_SC_H_XZ.class,
												"ycbdcdyid='" + listdjdy.get(0).getBDCDYID() + "'");
								String valuedyxz = "";
								String valuecfxz = "";
								String valueyyxz = "";
								String valuexzxz = "";
								String valueygxz = "";//不动产权利查档时需要查预告登记
								if (listycsc != null && listycsc.size() > 0 && listycsc.get(0).getSCBDCDYID() != null) {
									List<BDCS_DJDY_XZ> listdjdysc = baseCommonDao.getDataList(BDCS_DJDY_XZ.class,
													"BDCDYID='" + listycsc.get(0).getSCBDCDYID() + "'");
									if (listdjdysc != null && listdjdysc.size() > 0) {
										scdjdyid = listdjdysc.get(0).getDJDYID();
										Map<String, String> mapxz = new HashMap<String, String>();
										mapxz = getDYandCFandYY_XZ(scdjdyid,cdbz);
										for (Entry<String, String> ent : mapxz.entrySet()) {
											String name = ent.getKey();
											if (name.equals("DYZT")) {
												if (StringHelper.isEmpty(valuedyxz)) {
													valuedyxz = ent.getValue();
												} else {
													valuedyxz = valuedyxz + " " + ent.getValue();
												}
											} else if (name.equals("CFZT")) {
												if (StringHelper.isEmpty(valuecfxz)) {
													valuecfxz = ent.getValue();
												} else {
													valuecfxz = valuecfxz + " " + ent.getValue();
												}
											} else  if (name.equals("YYZT")) {
												if (StringHelper.isEmpty(valueyyxz)) {
													valueyyxz = ent.getValue();
												} else {
													valueyyxz = valueyyxz + " " + ent.getValue();
												}
											}else if (name.equals("XZZT")){
												if (StringHelper.isEmpty(valuexzxz)) {
													valuexzxz = ent.getValue();
												} else {
													valuexzxz = valuexzxz + "、" + ent.getValue();
												}
											}
										}
									}
								}
								Map<String, String> mapxzy = new HashMap<String, String>();
								mapxzy = getDYandCFandYY_XZY(djdyid,cdbz);
								for (Entry<String, String> ent : mapxzy.entrySet()) {
									String name = ent.getKey();
									if (name.equals("DYZT")) {
										if (StringHelper.isEmpty(valuedyxz)) {
											valuedyxz = ent.getValue();
										} else {
											valuedyxz = valuedyxz + " " + ent.getValue();
										}
									} else if (name.equals("CFZT")) {
										if (StringHelper.isEmpty(valuecfxz)) {
											valuecfxz = ent.getValue();
										} else {
											valuecfxz = valuecfxz + " " + ent.getValue();
										}
									} else  if (name.equals("YYZT")) {
										if (StringHelper.isEmpty(valueyyxz)) {
											valueyyxz = ent.getValue();
										} else {
											valueyyxz = valueyyxz + " " + ent.getValue();
										}
									}else  if (name.equals("YGDJZT")) {
										if (StringHelper.isEmpty(valueyyxz)) {
											valueygxz = ent.getValue();
										} else {
											valueygxz = valueygxz + " "
													+ ent.getValue();
										}
									}else if (name.equals("XZZT")){
										if (StringHelper.isEmpty(valuexzxz)) {
											valuexzxz = ent.getValue();
										} else {
											valuexzxz = valuexzxz + " " + ent.getValue();
										}
									}
								}
								map.put("DYZT", valuedyxz);
								map.put("CFZT", valuecfxz);
								map.put("YYZT", valueyyxz);
								map.put("XZZT", valuexzxz);
								map.put("YGDJZT", valueygxz);
							}
						}
					}

				}
			}
		}
	}
	/*--wlb--TODO---------------------------------------------------------------------------------*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void addFWLimitStatusByZD(List<Map> result,String cdbz) {
		if (result != null && result.size() > 0) {
			for (Map map : result) {
				if (map.containsKey("DJDYID")) {
					String djdyid = (String) map.get("DJDYID");
					String ycdjdyid = "";
					String scdjdyid = "";
					if (djdyid != null) {
						List<BDCS_DJDY_XZ> listdjdy = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "djdyid='"
										+ djdyid + "'");
						if (listdjdy != null && listdjdy.size() > 0) {
							BDCDYLX lx = BDCDYLX.initFrom(listdjdy.get(0).getBDCDYLX());
							if (lx.Value.equals("031")) {
								String valuedyxz = "";
								String valuecfxz = "";
								String valueyyxz = "";
								String valuexzxz = "";	
                                String valueygxz = "";
								
								List<YC_SC_H_XZ> listycsc = baseCommonDao.getDataList(YC_SC_H_XZ.class,
												"scbdcdyid='" + listdjdy.get(0).getBDCDYID() + "'");
								if (listycsc != null && listycsc.size() > 0
										&& listycsc.get(0).getYCBDCDYID() != null) {
									List<BDCS_DJDY_XZ> listdjdyyc = baseCommonDao.getDataList(BDCS_DJDY_XZ.class,
													"djdyid='" + listycsc.get(0).getYCBDCDYID() + "'");
									if (listdjdyyc != null && listdjdyyc.size() > 0) {
										ycdjdyid = listdjdyyc.get(0).getDJDYID();
										Map<String, String> mapxzy = new HashMap<String, String>();
										mapxzy = getDYandCFandYY_XZY(ycdjdyid,cdbz);
										for (Entry<String, String> ent : mapxzy.entrySet()) {
											String name = ent.getKey();
											if (name.equals("DYZT")) {
												if (StringHelper.isEmpty(valuedyxz)) {
													valuedyxz = ent.getValue();
												} else {
													valuedyxz = valuedyxz + "、" + ent.getValue();
												}
											} else if (name.equals("CFZT")) {
												if (StringHelper.isEmpty(valuecfxz)) {
													valuecfxz = ent.getValue();
												} else {
													valuecfxz = valuecfxz + "、" + ent.getValue();
												}
											} else if (name.equals("YYZT")){
												if (StringHelper.isEmpty(valueyyxz)) {
													valueyyxz = ent.getValue();
												} else {
													valueyyxz = valueyyxz + "、" + ent.getValue();
												}
											}else if (name.equals("XZZT")){
												if (StringHelper.isEmpty(valuexzxz)) {
													valuexzxz = ent.getValue();
												} else {
													valuexzxz = valuexzxz + "、" + ent.getValue();
												}
											}
										}
									}
								}
								Map<String, String> mapxz = new HashMap<String, String>();
								mapxz = getDYandCFandYY_XZ(djdyid,cdbz);
								for (Entry<String, String> ent : mapxz.entrySet()) {
									String name = ent.getKey();
									if (name.equals("DYZT")) {
										if (StringHelper.isEmpty(valuedyxz)) {
											valuedyxz = ent.getValue();
										} else {
											valuedyxz = valuedyxz + " " + ent.getValue();
										}
									} else if (name.equals("CFZT")) {
										if (StringHelper.isEmpty(valuecfxz)) {
											valuecfxz = ent.getValue();
										} else {
											valuecfxz = valuecfxz + " " + ent.getValue();
										}
									} else if (name.equals("YYZT")){
										if (StringHelper.isEmpty(valueyyxz)) {
											valueyyxz = ent.getValue();
										} else {
											valueyyxz = valueyyxz + " " + ent.getValue();
										}
									}else if (name.equals("YGDJZT")){
										if (StringHelper.isEmpty(valueyyxz)) {
											valueygxz = ent.getValue();
										} else {
											valueygxz = valueygxz + " "
													+ ent.getValue();
										}
									}else if (name.equals("XZZT")){
										if (StringHelper.isEmpty(valuexzxz)) {
											valuexzxz = ent.getValue();
										} else {
											valuexzxz = valuexzxz + " " + ent.getValue();
										}
									}
								}
								map.put("DYZT", valuedyxz);
								map.put("CFZT", valuecfxz);
								map.put("YYZT", valueyyxz);
								map.put("XZZT", valuexzxz);
                                map.put("YGDJZT", valueygxz);
								
							} else {
								List<YC_SC_H_XZ> listycsc = baseCommonDao.getDataList(YC_SC_H_XZ.class,
												"ycbdcdyid='" + listdjdy.get(0).getBDCDYID() + "'");
								if (listycsc != null && listycsc.size() > 0 && listycsc.get(0).getSCBDCDYID() != null) {
									List<BDCS_DJDY_XZ> listdjdysc = baseCommonDao.getDataList(BDCS_DJDY_XZ.class,
													"djdyid='" + listycsc.get(0).getSCBDCDYID() + "'");
									if (listdjdysc != null && listdjdysc.size() > 0) {
										scdjdyid = listdjdysc.get(0).getDJDYID();
										Map<String, String> mapxz = new HashMap<String, String>();
										mapxz = getDYandCFandYY_XZ(scdjdyid,cdbz);
										String valuedyxz = "";
										String valuecfxz = "";
										String valueyyxz = "";
										String valuexzxz = "";
										for (Entry<String, String> ent : mapxz.entrySet()) {
											String name = ent.getKey();
											if (name.equals("DYZT")) {
												if (StringHelper.isEmpty(valuedyxz)) {
													valuedyxz = ent.getValue();
												} else {
													valuedyxz = valuedyxz + " " + ent.getValue();
												}
											} else if (name.equals("CFZT")) {
												if (StringHelper.isEmpty(valuecfxz)) {
													valuecfxz = ent.getValue();
												} else {
													valuecfxz = valuecfxz + " " + ent.getValue();
												}
											} else  if (name.equals("YYZT")) {
												if (StringHelper.isEmpty(valueyyxz)) {
													valueyyxz = ent.getValue();
												} else {
													valueyyxz = valueyyxz + " " + ent.getValue();
												}
											}else if (name.equals("XZZT")){
												if (StringHelper.isEmpty(valuexzxz)) {
													valuexzxz = ent.getValue();
												} else {
													valuexzxz = valuexzxz + "、" + ent.getValue();
												}
											}
										}
									}
								}
								Map<String, String> mapxzy = new HashMap<String, String>();
								mapxzy = getDYandCFandYY_XZY(djdyid,cdbz);
								String valuedyxz = "";
								String valuecfxz = "";
								String valueyyxz = "";
								String valuexzxz = "";
								String valueygxz = "";//不动产权利查档时需要查预告登记
								for (Entry<String, String> ent : mapxzy.entrySet()) {
									String name = ent.getKey();
									if (name.equals("DYZT")) {
										if (StringHelper.isEmpty(valuedyxz)) {
											valuedyxz = ent.getValue();
										} else {
											valuedyxz = valuedyxz + " " + ent.getValue();
										}
									} else if (name.equals("CFZT")) {
										if (StringHelper.isEmpty(valuecfxz)) {
											valuecfxz = ent.getValue();
										} else {
											valuecfxz = valuecfxz + " " + ent.getValue();
										}
									} else  if (name.equals("YYZT")) {
										if (StringHelper.isEmpty(valueyyxz)) {
											valueyyxz = ent.getValue();
										} else {
											valueyyxz = valueyyxz + " " + ent.getValue();
										}
									}else  if (name.equals("YGDJZT")) {
										if (StringHelper.isEmpty(valueyyxz)) {
											valueygxz = ent.getValue();
										} else {
											valueygxz = valueygxz + " "
													+ ent.getValue();
										}
									}else if (name.equals("XZZT")){
										if (StringHelper.isEmpty(valuexzxz)) {
											valuexzxz = ent.getValue();
										} else {
											valuexzxz = valuexzxz + " " + ent.getValue();
										}
									}
								}
								map.put("DYZT", valuedyxz);
								map.put("CFZT", valuecfxz);
								map.put("YYZT", valueyyxz);
								map.put("XZZT", valuexzxz);
								map.put("YGDJZT", valueygxz);
							}
						}
					}

				}
			}
		}
	}
	/*-------------------------------------wlb-------------------------------单独-----------------*/
	public Map<String, String> getDYandCFandYY_XZY(String djdyid,String cdbz) {
		List<BDCS_DJDY_XZ> dy = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, " DJDYID='"+djdyid+"'");
		String bdcdyid = null;
		if(dy!=null&&dy.size()>0){
			bdcdyid = dy.get(0).getBDCDYID();
		}
		Map<String, String> map = new HashMap<String, String>();
		String sqlMortgage = MessageFormat.format(
				" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and qllx=''23''",
				djdyid);
		String sqlSeal = MessageFormat
				.format(" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''800'' and qllx=''99''",
						djdyid);
		String sqlObjection = MessageFormat
				.format("  from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''600'' ",
						djdyid);
		String sqlLimit = MessageFormat
				.format("  from BDCK.BDCS_DYXZ where bdcdyid=''{0}'' and yxbz=''1'' ",
						bdcdyid);
		String qfygdj= MessageFormat
				.format("  from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''700'' ",
						djdyid);

		long mortgageCount = baseCommonDao.getCountByFullSql(sqlMortgage);
		long SealCount = baseCommonDao.getCountByFullSql(sqlSeal);
		long ObjectionCount = baseCommonDao.getCountByFullSql(sqlObjection);
		long LimitCount = baseCommonDao.getCountByFullSql(sqlLimit);
		long qfygdjCount = baseCommonDao.getCountByFullSql(qfygdj)/2;//查预告登记

		String sealStatus = "";
		String mortgageStatus = "";
		String ygdjStatus = "";
		String LimitStatus = "";
		
		if(cdbz != null && cdbz.equals("cd")){
			mortgageStatus = mortgageCount > 0 ? "期房已抵押" +"("+mortgageCount+"笔)" : "期房无抵押";
			sealStatus = SealCount > 0 ? "期房已查封" +"("+SealCount+"笔)" : "期房无查封";
			LimitStatus = LimitCount > 0 ? "期房已限制" : "期房无限制";
			ygdjStatus = qfygdjCount > 0 ? "期房已预告登记" +"("+qfygdjCount+"笔)" : "期房无预告登记";

			// 判断完现状层中的查封信息，接着判断办理中的查封信息
			if (!(SealCount > 0)) {
				String sqlSealing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and  b.djdyid=c.djdyid WHERE c.djlx='800' AND c.qllx='99' and c.djdyid='"
						+ djdyid + "' and a.sfdb='0' ";
				long count = baseCommonDao.getCountByFullSql(sqlSealing);
				sealStatus = count > 0 ? "期房查封办理中" +"("+count+"笔)" : "期房无查封";
			}
		
		// 改为判断完查封 人后判断限制
		
		
		if (!(LimitCount > 0)) {
			String sqlLimiting = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_dyxz c ON b.xmbh=c.xmbh and b.bdcdyid=c.bdcdyid WHERE c.yxbz='0' and c.bdcdyid='"
					+ bdcdyid + "' and a.sfdb='0' ";
			long count = baseCommonDao.getCountByFullSql(sqlLimiting);
			LimitStatus = count > 0 ? "期房限制办理中" : "期房无限制";
		}
		

			// 判断完现状层中的查封信息，接着判断办理中的查封信息
			if (!(mortgageCount > 0)) {
				String sqlmortgageing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='100' AND c.qllx='23' and c.djdyid='"
						+ djdyid + "' and a.sfdb='0' ";
				long count = baseCommonDao.getCountByFullSql(sqlmortgageing);
				mortgageStatus = count > 0 ? "期房抵押办理中" +"("+count+"笔)" : "期房无抵押";
			}
		
        if (!(qfygdjCount > 0)) {
				String sqlmortgageing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='700' AND c.qllx='4' and c.djdyid='"
						+ djdyid + "' and a.sfdb='0' ";
				long count = baseCommonDao.getCountByFullSql(sqlmortgageing);
				ygdjStatus = count > 0 ? "期房预告登记办理中" +"("+count+"笔)" : "期房无办理中的预告登记";
			}
		}else{
			mortgageStatus = mortgageCount > 0 ? "期房已抵押" : "期房无抵押";
			sealStatus = SealCount > 0 ? "期房已查封" : "期房无查封";
			LimitStatus = LimitCount > 0 ? "期房已限制" : "期房无限制";
			ygdjStatus = qfygdjCount > 0 ? "期房已预告登记" : "期房无预告登记";

			// 判断完现状层中的查封信息，接着判断办理中的查封信息
			if (!(SealCount > 0)) {
				String sqlSealing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and  b.djdyid=c.djdyid WHERE c.djlx='800' AND c.qllx='99' and c.djdyid='"
						+ djdyid + "' and a.sfdb='0' ";
				long count = baseCommonDao.getCountByFullSql(sqlSealing);
				sealStatus = count > 0 ? "期房查封办理中" : "期房无查封";
			}
			    // 改为判断完查封 人后判断限制
		if (!(LimitCount > 0)) {
			String sqlLimiting = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_dyxz c ON b.xmbh=c.xmbh and b.bdcdyid=c.bdcdyid WHERE c.yxbz='0' and c.bdcdyid='"
					+ bdcdyid + "' and a.sfdb='0' ";
			long count = baseCommonDao.getCountByFullSql(sqlLimiting);
			LimitStatus = count > 0 ? "期房限制办理中" : "期房无限制";
		}
		
		    // 判断完现状层中的查封信息，接着判断办理中的查封信息
			if (!(mortgageCount > 0)) {
				String sqlmortgageing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='100' AND c.qllx='23' and c.djdyid='"
						+ djdyid + "' and a.sfdb='0' ";
				long count = baseCommonDao.getCountByFullSql(sqlmortgageing);
				mortgageStatus = count > 0 ? "期房抵押办理中" : "期房无抵押";
			}

			if (!(qfygdjCount > 0)) {
				String sqlmortgageing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='700' AND c.qllx='4' and c.djdyid='"
						+ djdyid + "' and a.sfdb='0' ";
				long count = baseCommonDao.getCountByFullSql(sqlmortgageing);
				ygdjStatus = count > 0 ? "期房预告登记办理中" : "期房无办理中的预告登记";
			}
		}	
		
		
		String objectionStatus = ObjectionCount > 0 ? "期房有异议" : "期房无异议";
		map.put("DYZTFLAG", String.valueOf(mortgageCount));
		map.put("CFZTFLAG", String.valueOf(SealCount));
		map.put("DYZT", mortgageStatus);
		map.put("CFZT", sealStatus);
		map.put("YYZT", objectionStatus);
		map.put("XZZT", LimitStatus);
        map.put("YGDJZT", ygdjStatus);
		
		return map;

	}
	public Map<String, String> getDYandCFandYY_XZ(String djdyid,String cdbz) {
		List<BDCS_DJDY_XZ> dy = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, " DJDYID='"+djdyid+"'");
		String bdcdyid = null;
		if(dy!=null&&dy.size()>0){
			bdcdyid = dy.get(0).getBDCDYID();
		}
		Map<String, String> map = new HashMap<String, String>();
		String sqlMortgage = MessageFormat.format(
				" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and qllx=''23''",
				djdyid);
		String sqlSeal = MessageFormat
				.format(" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''800'' and qllx=''99''",
						djdyid);
		String sqlObjection = MessageFormat
				.format("  from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''600'' ",
						djdyid);
		String sqlYgdj = MessageFormat
				.format("  from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''700'' ",
						djdyid);//查预告登记
						
		String sqlLimit = MessageFormat
				.format("  from BDCK.BDCS_DYXZ where bdcdyid=''{0}'' and yxbz=''1'' ",
						bdcdyid);

		long mortgageCount = baseCommonDao.getCountByFullSql(sqlMortgage);
		long SealCount = baseCommonDao.getCountByFullSql(sqlSeal);
		long ObjectionCount = baseCommonDao.getCountByFullSql(sqlObjection);
		long LimitCount = baseCommonDao.getCountByFullSql(sqlLimit);
		long ygdjCount = baseCommonDao.getCountByFullSql(sqlYgdj);

		String sealStatus = "";
		String mortgageStatus = "";
		String ygdjStatus = "";
		String LimitStatus = "";
		if(cdbz != null && cdbz.equals("cd")){
			mortgageStatus = mortgageCount > 0 ? "现房已抵押" +"("+mortgageCount+"笔)" : "现房无抵押";
			sealStatus = SealCount > 0 ? "现房已查封" +"("+SealCount+"笔)" : "现房无查封";
			LimitStatus = LimitCount > 0 ? "现房已限制" : "现房无限制";
			
			// 判断完现状层中的查封信息，接着判断办理中的查封信息
			if (!(SealCount > 0)) {
				String sqlSealing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and  b.djdyid=c.djdyid WHERE c.djlx='800' AND c.qllx='99' and c.djdyid='"
						+ djdyid + "' and a.sfdb='0' ";
				long count = baseCommonDao.getCountByFullSql(sqlSealing);
		
		        sealStatus = count > 0 ? "现房查封办理中" +"("+count+"笔)" : "现房无查封";
			}

			// 改为判断完查封 人后判断限制
			if (!(LimitCount > 0)) {
				String sqlLimiting = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_dyxz c ON b.xmbh=c.xmbh and b.bdcdyid=c.bdcdyid WHERE c.yxbz='0' and c.bdcdyid='"
						+ bdcdyid + "' and a.sfdb='0' ";
				long count = baseCommonDao.getCountByFullSql(sqlLimiting);
				LimitStatus = count > 0 ? "现房限制办理中" : "现房无限制";
			}
			// 判断完现状层中的查封信息，接着判断办理中的查封信息
			if (!(mortgageCount > 0)) {
				String sqlmortgageing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='100' AND c.qllx='23' and c.djdyid='"
						+ djdyid + "' and a.sfdb='0' ";
				long count = baseCommonDao.getCountByFullSql(sqlmortgageing);
				mortgageStatus = count > 0 ? "现房抵押办理中"+"("+count+"笔)" : "现房无抵押";
			}

			// 预告登记
			if (!(ygdjCount > 0)) {
				String sqlygdj = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='700' and c.djdyid='"
						+ djdyid + "' and a.sfdb='0' ";
				long count = baseCommonDao.getCountByFullSql(sqlygdj);
				ygdjStatus = count > 0 ? "现房预告登记"+"("+count+"笔)" : "现房无预告登记";
			}
		}else{
			mortgageStatus = mortgageCount > 0 ? "现房已抵押" : "现房无抵押";
			sealStatus = SealCount > 0 ? "现房已查封" : "现房无查封";
			LimitStatus = LimitCount > 0 ? "现房已限制" : "现房无限制";
			// 判断完现状层中的查封信息，接着判断办理中的查封信息
			if (!(SealCount > 0)) {
				String sqlSealing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and  b.djdyid=c.djdyid WHERE c.djlx='800' AND c.qllx='99' and c.djdyid='"
						+ djdyid + "' and a.sfdb='0' ";
				long count = baseCommonDao.getCountByFullSql(sqlSealing);
				sealStatus = count > 0 ? "现房查封办理中" : "现房无查封";
			}

			// 改为判断完查封 人后判断限制
			if (!(LimitCount > 0)) {
				String sqlLimiting = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_dyxz c ON b.xmbh=c.xmbh and b.bdcdyid=c.bdcdyid WHERE c.yxbz='0' and c.bdcdyid='"
						+ bdcdyid + "' and a.sfdb='0' ";
				long count = baseCommonDao.getCountByFullSql(sqlLimiting);
				LimitStatus = count > 0 ? "现房限制办理中" : "现房无限制";
			}

			// 判断完现状层中的查封信息，接着判断办理中的查封信息
			if (!(mortgageCount > 0)) {
				String sqlmortgageing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='100' AND c.qllx='23' and c.djdyid='"
						+ djdyid + "' and a.sfdb='0' ";
				long count = baseCommonDao.getCountByFullSql(sqlmortgageing);
				mortgageStatus = count > 0 ? "现房抵押办理中" : "现房无抵押";
			}

			// 预告登记
			if (!(ygdjCount > 0)) {
				String sqlygdj = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='700' and c.djdyid='"
						+ djdyid + "' and a.sfdb='0' ";
				long count = baseCommonDao.getCountByFullSql(sqlygdj);
				ygdjStatus = count > 0 ? "现房预告登记中" : "现房无预告登记中";
			}
		}

		String objectionStatus = ObjectionCount > 0 ? "现房有异议" : "现房无异议";
		map.put("DYZTFLAG", String.valueOf(mortgageCount));
		map.put("CFZTFLAG", String.valueOf(SealCount));
		map.put("DYZT", mortgageStatus);
		map.put("CFZT", sealStatus);
		map.put("YYZT", objectionStatus);
		map.put("XZZT", LimitStatus);
		map.put("YGDJZT", ygdjStatus);

		return map;
	}
    public void ADDXMXX_SIZE(List<Map> result) {
		if (result != null && result.size() > 0) {
			for (Map map : result) {
				if (map.containsKey("DJDYID")) {
					String djdyid = "";
					if(StringHelper.isEmpty(map.get("DJDYID"))&&!StringHelper.isEmpty(map.get("BDCDYID"))){//没有做过预告登记就做预查封的情况下，djdy的现状和历史表没有数据
						List<BDCS_DJDY_GZ> djdygz = baseCommonDao.getDataList(BDCS_DJDY_GZ.class," bdcdyid='"+(String)map.get("BDCDYID")+"'" );
						if(djdygz !=null && djdygz.size()>0){
							djdyid = djdygz.get(0).getDJDYID();	
						}
					}else{
						djdyid = (String) map.get("DJDYID");
					}
		            StringBuilder ywlsh=new StringBuilder();
		            int i=0;
		String sqlXmxx = MessageFormat.format(
				"select YWLSH from bdck.bdcs_xmxx xmxx INNER JOIN bdck.bdcs_djdy_gz djdy ON xmxx.xmbh=djdy.xmbh AND xmxx.sfdb='0' and djdy.djdyid=''{0}'' ",
				djdyid);
		List<Map> xmxxs  = baseCommonDao.getDataListByFullSql(sqlXmxx);
		for(Map m:xmxxs){
			i++;
			ywlsh.append(m.get("YWLSH"));
			if(xmxxs.size()!=i)
				ywlsh.append(",");
		}
		//正在办理的项目，相应的业务流水号
		map.put("XMXXS", String.valueOf(xmxxs.size()));
		map.put("YWLSHS", ywlsh.toString());
				}
			}
		}
	}
	@SuppressWarnings("unchecked")
	protected void addRightsHolderInfo(
			@SuppressWarnings("rawtypes") List<Map> result) {
		if (result != null && result.size() > 0) {
			for (@SuppressWarnings("rawtypes")
			Map map : result) {
				if (map.containsKey("QLID")) {
					String qlid = (String) map.get("QLID");
					if (qlid != null) {
						RightsHolder holder = RightsHolderTools
								.getUnionRightsHolder(DJDYLY.XZ, qlid);
						if (holder != null) {
							map.put("QLRMC", holder.getQLRMC());
							map.put("DH", holder.getDH());
							map.put("ZJHM", holder.getZJH());
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	//获取DJDY.DJDYID、 XM.YWLSH, QL.QLID,QL.BDCQZH,QL.QLLX,QL.DJSJ、 FSQL.FSQLID, FSQL.CFWH
	protected void addOtherInfo(List<Map> result) {
		if (result != null && result.size() > 0) {
			for (Map map : result) {
				if (map.containsKey("BDCDYID")) {
					String bdcdyid = (String) map.get("BDCDYID");
					String djdyid = "",qlid = "",qllx="";
					if (bdcdyid != null) {
						List<BDCS_DJDY_XZ> listdjdy = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "bdcdyid='"
										+ bdcdyid + "'");
						if (listdjdy != null && listdjdy.size() > 0) {
							djdyid = listdjdy.get(0).getDJDYID();
							map.put("DJDYID", djdyid);
							
							List<BDCS_QL_XZ> listql = baseCommonDao.getDataList(BDCS_QL_XZ.class, "djdyid='"
									+ djdyid + "' AND QLLX IN ('4','6','8')");
							if (listql != null && listql.size() > 0) {
								qlid = listql.get(0).getId();
								map.put("QLID", qlid);
								map.put("BDCQZH", listql.get(0).getBDCQZH());
								map.put("QLLX", listql.get(0).getQLLX());
								map.put("DJSJ", listql.get(0).getDJSJ());
								
								SubRights fsql = RightsTools.loadSubRightsByRightsID(DJDYLY.XZ, qlid);
								if (fsql != null) {
									map.put("FSQLID", fsql.getId());
									map.put("CFWH", fsql.getCFWH());
								}
								if (!StringHelper.isEmpty(listql.get(0).getXMBH())) {
									BDCS_XMXX xmxx=baseCommonDao.get(BDCS_XMXX.class, listql.get(0).getXMBH());
									if (xmxx != null ) 
										map.put("YWLSH", xmxx.getYWLSH());
								}
							}else {//期房-在建工程抵押时
								
							}
						}
					}
				}
			}
		}
	}			

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<List<Map>> GetDYDJList_XZ(String bdcdyid) {
		List<List<Map>> result = new ArrayList<List<Map>>();
		List<Map> qllist = new ArrayList();
		List<BDCS_DJDY_XZ> djdys = baseCommonDao.getDataList(
				BDCS_DJDY_XZ.class, "BDCDYID='" + bdcdyid + "'");
		// 查询历史权利信息
		if (djdys != null && djdys.size() > 0) {
			String djdyid = djdys.get(0).getDJDYID();
			StringBuilder builderXZ = new StringBuilder();
			builderXZ
					.append("SELECT QLID,YWH,DJLX,QLLX,DJSJ FROM BDCK.BDCS_QL_XZ WHERE DJDYID='");
			builderXZ.append(djdyid);
			builderXZ.append("' AND QLLX='23' ORDER BY DJSJ ASC NULLS FIRST");
			List<Map> xzqls = baseCommonDao.getDataListByFullSql(builderXZ
					.toString());
			int sx = 1;
			if (xzqls != null && xzqls.size() > 0) {
				for (int iql = 0; iql < xzqls.size(); iql++) {
					String qlid = StringHelper.formatObject(xzqls.get(iql).get(
							"QLID"));
					String djlx = StringHelper.formatObject(xzqls.get(iql).get(
							"DJLX"));
					String ywh = StringHelper.formatObject(xzqls.get(iql).get(
							"YWH"));
					Map<String, String> map = new HashMap<String, String>();
					map.put("QLID", qlid);
					map.put("YWH", ywh);
					map.put("CANCLED", "false");
					map.put("DJLX",
							StringHelper.isEmpty(djlx) ? "首次登记" : DJLX
									.initFrom(djlx).Name);
					map.put("QLLX", "DYQ");
					map.put("LHSX", "第"+sx+"轮");
					qllist.add(map);
					sx++;
				}
			}
		}
		result.add(qllist);
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<List<Map>> GetCFDJList_XZ(String bdcdyid) {
		List<List<Map>> result = new ArrayList<List<Map>>();
		List<Map> qllist = new ArrayList();
		List<BDCS_DJDY_XZ> djdys = baseCommonDao.getDataList(
				BDCS_DJDY_XZ.class, "BDCDYID='" + bdcdyid + "'");
		// 查询历史权利信息
		if (djdys != null && djdys.size() > 0) {
			String djdyid = djdys.get(0).getDJDYID();
			StringBuilder builderXZ = new StringBuilder();
			builderXZ
					.append("SELECT QL.QLID,QL.YWH,QL.DJLX,QL.QLLX FROM BDCK.BDCS_QL_XZ QL LEFT JOIN BDCK.BDCS_FSQL_XZ FSQL ON QL.QLID = FSQL.QLID WHERE QL.DJDYID='");
			builderXZ.append(djdyid);
			builderXZ.append("' AND QL.QLLX='99' AND QL.DJLX='800' ORDER BY FSQL.LHSX");
			List<Map> xzqls = baseCommonDao.getDataListByFullSql(builderXZ
					.toString());
			if (xzqls != null && xzqls.size() > 0) {
				for (int iql = 0; iql < xzqls.size(); iql++) {
					String qlid = StringHelper.formatObject(xzqls.get(iql).get(
							"QLID"));
					// String djlx =
					// StringHelper.formatObject(xzqls.get(iql).get("DJLX"));
					String ywh = StringHelper.formatObject(xzqls.get(iql).get(
							"YWH"));
					Map<String, String> map = new HashMap<String, String>();
					map.put("QLID", qlid);
					map.put("YWH", ywh);
					map.put("CANCLED", "false");
					map.put("DJLX", "查封登记");
					map.put("QLLX", "CFDJ");
					qllist.add(map);
				}
			}
		}
		result.add(qllist);
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<List<Map>> GetYYDJList_XZ(String bdcdyid) {
		List<List<Map>> result = new ArrayList<List<Map>>();
		List<Map> qllist = new ArrayList();
		List<BDCS_DJDY_XZ> djdys = baseCommonDao.getDataList(
				BDCS_DJDY_XZ.class, "BDCDYID='" + bdcdyid + "'");
		// 查询历史权利信息
		if (djdys != null && djdys.size() > 0) {
			String djdyid = djdys.get(0).getDJDYID();
			StringBuilder builderXZ = new StringBuilder();
			builderXZ
					.append("SELECT QLID,YWH,DJLX,QLLX FROM BDCK.BDCS_QL_XZ WHERE DJDYID='");
			builderXZ.append(djdyid);
			builderXZ.append("' AND QLLX='99' AND DJLX='600' ORDER BY DJSJ");
			List<Map> xzqls = baseCommonDao.getDataListByFullSql(builderXZ
					.toString());
			if (xzqls != null && xzqls.size() > 0) {
				for (int iql = 0; iql < xzqls.size(); iql++) {
					String qlid = StringHelper.formatObject(xzqls.get(iql).get(
							"QLID"));
					// String djlx =
					// StringHelper.formatObject(xzqls.get(iql).get("DJLX"));
					String ywh = StringHelper.formatObject(xzqls.get(iql).get(
							"YWH"));
					Map<String, String> map = new HashMap<String, String>();
					map.put("QLID", qlid);
					map.put("YWH", ywh);
					map.put("CANCLED", "false");
					map.put("DJLX", "异议登记");
					map.put("QLLX", "YYDJ");
					qllist.add(map);
				}
			}
		}
		result.add(qllist);
		return result;
	}
	

	/**
	 * 土地信息查询
	 */
	
	@SuppressWarnings({ "rawtypes" })
	@Override
	public Message queryLand(Map<String, String> queryvalues, int page,
			int rows, boolean iflike, String tdzt,String sort,String order) {
		Message msg = new Message();
		//结果筛选——是否只显示产权
		String jgsx=queryvalues.get("JGSX");
		queryvalues.remove("JGSX");
		if ("2".equals(jgsx)) {
			return queryLandForUnit(queryvalues, page, rows, iflike, tdzt, sort, order);
		}
		String fsqlcfwh=queryvalues.get("FSQL.CFWH");
		String cxzt=queryvalues.get("CXZT");
		queryvalues.remove("CXZT");
		long count = 0;
		List<Map> listresult = null;
		/* ===============1、先获取实体对应的表名=================== */
		String unitentityName = "BDCK.BDCS_SHYQZD_XZ";

		/* ===============2、再获取表名+'_'+字段名=================== */
		String dyfieldsname = "DY.DJH,DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,DY.ZDMJ,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC,DY.BZ,DY.QLXZ,TDYT.TDYT  ";
		String qlfieldsname = "XM.YWLSH,QL.QLID,QL.BDCQZH,QL.DJSJ,QL.QLLX";
		String fsqlfieldsname = "FSQL.FSQLID,FSQL.CFWH";

		if (tdzt != null && tdzt.equals("2")) {
			unitentityName = "BDCK.BDCS_SYQZD_XZ";
			dyfieldsname = "DY.DJH,DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,DY.ZDMJ,'01' AS BDCDYLX,'所有权宗地' AS BDCDYLXMC,DY.BZ,DY.QLXZ,TDYT.TDYT  ";
		}

		// 集合使用权宗地与所有权宗地
		if (tdzt != null && tdzt.equals("3")) {
			unitentityName = "(SELECT DJH,ZL,ZDDM,ZDMJ,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC,BZ,QLXZ  FROM BDCK.BDCS_SYQZD_XZ UNION ALL SELECT DJH,ZL,ZDDM,ZDMJ,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC,BZ,QLXZ  FROM BDCK.BDCS_SHYQZD_XZ)";
			dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.DJH,DY.ZL,DY.ZDDM,DY.ZDMJ,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC,DY.BZ,DY.QLXZ,TDYT.TDYT ";
		}

		/* ===============3、构造查询语句=================== */
		/* SELECT 字段部分 */
		StringBuilder builder2 = new StringBuilder();
		builder2.append("select ").append(dyfieldsname).append(",")
				.append(qlfieldsname);
		if(!StringHelper.isEmpty(fsqlcfwh)){
			builder2.append(",").append(fsqlfieldsname);
		}
		String selectstr = builder2.toString();

		/* FROM 后边的表语句 */
		StringBuilder builder = new StringBuilder();
		builder.append(" from {0} DY")
				.append(" left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid")
				.append(" left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid  ")
				.append(" left join bdck.bdcs_tdyt_xz tdyt on dy.bdcdyid=tdyt.bdcdyid ")
				.append(" left join BDCK.BDCS_XMXX XM on XM.XMBH = QL.XMBH");
		if(!StringHelper.isEmpty(fsqlcfwh)){
			builder.append(" left join BDCK.bdcs_fsql_xz fsql on ql.fsqlid=fsql.fsqlid  ");
		}
		String fromstr = builder.toString();
		fromstr = MessageFormat.format(fromstr, unitentityName);

		/* WHERE 条件语句 */
		StringBuilder builder3 = new StringBuilder();
		builder3.append(" where  ");
		StringBuilder qlrbuilder = new StringBuilder();
		boolean havecondition = false;
		boolean haveqlr = false;
		for (Entry<String, String> ent : queryvalues.entrySet()) {
			String name = ent.getKey();
			String value = ent.getValue();
			if (!StringHelper.isEmpty(value)) {
				// 抵押状态和查封状态，为‘0’的时候表示不过滤，查询全部
				if ((name.equals("CFZT") && value.equals("0"))
						|| (name.equals("DYZT") && value.equals("0"))) {
					continue;
				}

				// 权利人，权利人的条件比较特殊，where里边又套了where
				if (name.startsWith("QLR.")) {
					if (haveqlr)
						qlrbuilder.append(" and ");
					if (iflike) {
						qlrbuilder.append(" instr(" + name + ",'" + value
								+ "')>0 ");
					} else {
						if (name.equals("QLR.ZJH") && value.replace(" ", "").length() == 18) {
							String oldCard = ConverIdCard.getOldIDCard(value);
							if (oldCard.length() != 15) {
								qlrbuilder.append(" " + name + "='" + value.replace(" ", "").toUpperCase()
										+ "' ");
							} else {
								qlrbuilder.append(" (" + name + "='" + value.replace(" ", "").toUpperCase()
										+ "' or ");
								qlrbuilder.append(" " + name + "='" + oldCard.replace(" ", "").toUpperCase()
										+ "') ");
							}
						} else {
							qlrbuilder.append(" " + name + "='" + value.replace(" ", "") + "' ");
						}
					}
					haveqlr = true;
					continue;
				}

				if (havecondition) {
					builder3.append(" and ");
				}

				// 抵押状态
				if (name.equals("DYZT")) {
					if (value.equals("1")) {
						builder3.append(" djdy.djdyid NOT IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_XZ DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
					} else {
						builder3.append(" djdy.djdyid IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_XZ DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
					}
					havecondition = true;
					continue;
				}
				// 查封状态
				if (name.equals("CFZT")) {
					if (value.equals("1")) {
						builder3.append("  djdy.DJDYID NOT IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_XZ CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99') ");
					} else {
						builder3.append("  djdy.DJDYID IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_XZ CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99') ");
					}
					havecondition = true;
					continue;
				}
				
				// 房地状况
				if (name.equals("FDZK")) {
					if (value.equals("1")) {
						builder3.append(" NOT EXISTS(SELECT 1 FROM BDCK.BDCS_H_XZ H LEFT JOIN BDCK.BDCS_ZRZ_XZ ZRZ ON H.ZRZBDCDYID=ZRZ.BDCDYID  WHERE H.ZDBDCDYID=DY.BDCDYID) ");
					}else if(value.equals("2")){
						builder3.append(" EXISTS(SELECT 1 FROM BDCK.BDCS_H_XZ H LEFT JOIN BDCK.BDCS_ZRZ_XZ ZRZ ON H.ZRZBDCDYID=ZRZ.BDCDYID  WHERE H.ZDBDCDYID=DY.BDCDYID) ");
					}else{
						builder3.append(" 1=1 ");
					}
					havecondition = true;
					continue;
				}
				
				// 权利性质
				if (name.equals("QLXZ")) {
					if (value.equals("1")) {
						builder3.append(" DY.QLXZ='100' ");
					}else if(value.equals("2")){
						builder3.append(" DY.QLXZ='200' ");
					}else if(value.equals("3")){
						builder3.append(" DY.QLXZ='101' ");
					}else if(value.equals("4")){
						builder3.append(" DY.QLXZ='102' ");
					}else if(value.equals("5")){
						builder3.append(" DY.QLXZ='203' ");
					}else{
						builder3.append(" 1=1 ");
					}
					havecondition = true;
					continue;
				}
				
				//土地用途
				if (name.equals("TDYT")) {
					if (value.equals("1")) {
						builder3.append(" TDYT.TDYT='071' ");
					}else if(value.equals("2")){
						builder3.append(" TDYT.TDYT='072' ");
					}else if(value.equals("3")){
						builder3.append(" TDYT.TDYT='051' ");
					}else if(value.equals("4")){
						builder3.append(" TDYT.TDYT='053' ");
					}else if(value.equals("5")){
						builder3.append(" TDYT.TDYT='052' ");
					}else if(value.equals("6")){
						builder3.append(" TDYT.TDYT='054' ");
					}else if(value.equals("7")){
						builder3.append(" TDYT.TDYT='085' ");
					}else if(value.equals("8")){
						builder3.append(" TDYT.TDYT='084' ");
					}else if(value.equals("9")){
						builder3.append(" TDYT.TDYT='081' ");
					}else if(value.equals("10")){
						builder3.append(" TDYT.TDYT='083' ");
					}else if(value.equals("11")){
						builder3.append(" TDYT.TDYT='061' ");
					}else if(value.equals("12")){
						builder3.append(" TDYT.TDYT='062' ");
					}else if(value.equals("13")){
						builder3.append(" TDYT.TDYT='063' ");
					}else{
						builder3.append(" 1=1 ");
					}
					havecondition = true;
					continue;
				}

				if (iflike) {
					if (name.equals("DJSJ_Q")) {
						builder3.append("  QL.DJSJ >=to_date('"+queryvalues.get("DJSJ_Q")+"','yyyy-mm-dd') ");
					}else if (name.equals("DJSJ_Z")) {
						builder3.append("   QL.DJSJ <=(to_date('"+queryvalues.get("DJSJ_Z")+"','yyyy-mm-dd')+1) ");
					}else if (name.contains("QL.YWH")) {
						builder3.append(" (QL.YWH LIKE '%"+value.trim()+"%' OR XM.YWLSH LIKE '%"+value.trim()+"%') ");
					}else
					builder3.append(" instr(" + name + ",'" + value + "')>0 ");
				}else if (name.contains("QL.YWH")) {
					builder3.append(" (QL.YWH='"+value.trim()+"' OR XM.YWLSH='"+value.trim()+"') ");
				}else if (name.equals("DJSJ_Q")) {
					builder3.append("  QL.DJSJ >=to_date('"+queryvalues.get("DJSJ_Q")+"','yyyy-mm-dd') ");
				}else if (name.equals("DJSJ_Z")) {
					builder3.append("   QL.DJSJ <=(to_date('"+queryvalues.get("DJSJ_Z")+"','yyyy-mm-dd')+1) ");
				}else {
					builder3.append(" " + name + "='" + value.replace(" ", "") + "' ");
				}
				havecondition = true;
			}
		}
//		String wherein="";
		// 有权利人查询条件
		if (!StringHelper.isEmpty(qlrbuilder.toString())) {
			if (havecondition) {
				builder3.append(" and ");
			}
//			 wherein=builder3.toString()+" QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ QLR WHERE "+ qlrbuilder.toString() + ")";
//			builder3.append(" QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ QLR WHERE "
//					+ qlrbuilder.toString() + ")");
			builder3.append(" exists(select 1 from BDCK.BDCS_QLR_XZ QLR  where "+ qlrbuilder.toString()+"   AND QLR.QLID=QL.QLID)  ");
			havecondition = true;
		}

		if (tdzt == null || !tdzt.equals("2")) {
			if (havecondition) {
				// builder3.append(" and ");
			}
			// builder3.append(" ql.qllx='4'");
		}
		String wherestr = builder3.toString();
		if (wherestr.trim().toUpperCase().endsWith("WHERE")) {
			wherestr = "";
		}
//		String fromsql1="select ql.qlid " + fromstr + wherestr ;
		String fromSql = " from (select ql.qlid " + fromstr + wherestr + ")";
		String fullSql = selectstr + fromstr + wherestr;
//		wherein=selectstr + fromstr + wherein;
		/* 排序 条件语句 */
		if(!StringUtils.isEmpty(sort)&&!StringUtils.isEmpty(order))
		{
		if(sort.toUpperCase().equals("ZL"))
			sort="DY.ZL";
		if(sort.toUpperCase().equals("BDCDYH"))
			sort="DY.BDCDYH";

		if(sort.toUpperCase().equals("BDCQZH"))
			sort="QL.BDCQZH";

		if(sort.toUpperCase().equals("BDCDYID"))
			sort="DY.BDCDYID";
		if(sort.toUpperCase().equals("BDCDYLXMC"))
			sort="DY.BDCDYLX";
			fullSql=fullSql+" ORDER BY "+sort+" "+order;
//			wherein=wherein+" ORDER BY "+sort+" "+order;
		}
		/* 为了提高效率，先不查询符合条件的记录总数，将来改为异步获取 */
		CommonDao dao = baseCommonDao;
		Long test1=System.currentTimeMillis(); 
		if("2".equals(cxzt)){
			fromSql = fromSql.replaceAll("_xz", "_ls").replaceAll("_XZ", "_LS");
			fullSql = fullSql.replaceAll("_xz", "_ls").replaceAll("_XZ", "_LS");
		}
		
		count = dao.getCountByFullSql(fromSql);
		Long test2=System.currentTimeMillis();
		listresult = dao.getPageDataByFullSql(fullSql, page, rows);
		Long test3=System.currentTimeMillis();
		addRightsHolderInfo(listresult);
		Long test4=System.currentTimeMillis();
		addLimitZDStatus(listresult);
		Long test5=System.currentTimeMillis();
		//国有土地使用权时，显示出土地上房屋状态。
		addLimitZDStausByFw(listresult);
		Long test6=System.currentTimeMillis();
		addDyCfDetails(listresult);
		Long test7=System.currentTimeMillis();
		addZRZCount(listresult);
		Long test8=System.currentTimeMillis();
//		List<Map> m=dao.getDataListByFullSql(fromsql1);
//		Long test9 =System.currentTimeMillis();
//		List<Map> d=dao.getDataListByFullSql(wherein);
//		Long test10=System.currentTimeMillis();
		System.out.println("addZRZCount方法消耗时间为："+(test8-test7));
		System.out.println("addDyCfDetails方法消耗时间为："+(test7-test6));
		System.out.println("addLimitZDStausByFw方法消耗时间为："+(test6-test5));
		System.out.println("addLimitZDStatus方法消耗时间为："+(test5-test4));
		System.out.println("addRightsHolderInfo方法消耗时间为："+(test4-test3));
		System.out.println("datalist修改成exist方法消耗时间为："+(test3-test2));
//		System.out.println("datalist中in方法消耗时间为："+(test10-test9));
		System.out.println("Count方法消耗时间为："+(test2-test1));
//		System.out.println("弃用Count方法消耗的时间为："+(test9-test8)+",弃用Count方法总和为"+m.size()+",共Count方法总和为"+count);
		System.out.println("共消耗时间为："+(test8-test1));
		// 格式化结果中的登簿时间
		for (Map map : listresult) {
			if (map.containsKey("DJSJ")) {
				map.put("DJSJ",StringHelper.FormatYmdhmsByDate(map.get("DJSJ")));
			}
			if (map.containsKey("QLXZ")) {
				map.put("QLXZ",ConstHelper.getNameByValue("QLXZ",(String)map.get("QLXZ")));
			}
			if (map.containsKey("TDYT")) {
				map.put("TDYT",ConstHelper.getNameByValue("TDYT",(String)map.get("TDYT")));			}
		}
		msg.setTotal(count);
		msg.setRows(listresult);
		return msg;
	}
	/**
	 * 土地信息查询
	 */
	
	@SuppressWarnings({ "rawtypes" })
	@Override
	public Message queryLandForUnit(Map<String, String> queryvalues, int page,
			int rows, boolean iflike, String tdzt,String sort,String order) {
		Message msg = new Message();		
		String fsqlcfwh=queryvalues.get("FSQL.CFWH");
		String cxzt=queryvalues.get("CXZT");
		queryvalues.remove("CXZT");
		long count = 0;
		List<Map> listresult = null;
		 //===============1、先获取实体对应的表名=================== 
		String unitentityname_syqzd = "SELECT DJH,ZL,ZDDM,ZDMJ,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC,BZ,QLXZ FROM BDCK.BDCS_SYQZD_XZ DY";
		String unitentityname_shyqzd= "SELECT DJH,ZL,ZDDM,ZDMJ,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC,BZ,QLXZ FROM BDCK.BDCS_SHYQZD_XZ DY";
		// ===============2、再获取表名+'_'+字段名=================== 
//		String dyfieldsname = "DY.DJH,DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,DY.ZDMJ,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC,DY.BZ,DY.QLXZ,TDYT.TDYT  ";
//		String qlfieldsname = "QL.QLID,QL.BDCQZH,QL.DJSJ,QL.QLLX";
//		String fsqlfieldsname = "FSQL.FSQLID,FSQL.CFWH";
		
//		String inner_unitentityname_syqzd = "SELECT DJH,ZL,ZDDM,ZDMJ,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC,BZ,QLXZ  FROM BDCK.BDCS_SYQZD_XZ";
//		String inner_unitentityname_shyqzd= "SELECT DJH,ZL,ZDDM,ZDMJ,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC,BZ,QLXZ  FROM BDCK.BDCS_SHYQZD_XZ";
	
		// ===============3、构造查询语句=================== 
				
		// FROM 后边的表语句 
		StringBuilder builder_ql = new StringBuilder();
		builder_ql.append(" DY.BDCDYID IN  (SELECT QL.BDCDYID FROM  BDCK.BDCS_QL_XZ QL ")
				.append(" LEFT JOIN BDCK.BDCS_TDYT_XZ TDYT ON QL.BDCDYID=TDYT.BDCDYID ");
		if(!StringHelper.isEmpty(fsqlcfwh)){
			builder_ql.append(" LEFT JOIN BDCK.BDCS_FSQL_XZ FSQL ON QL.FSQLID=FSQL.FSQLID  ");
		}

		// WHERE 条件语句 
		StringBuilder builder3 = new StringBuilder();//ql条件
		StringBuilder builder_zt = new StringBuilder();//状态条件
		StringBuilder dybuilder = new StringBuilder();//单元条件
		boolean haveql = false;//是否有多个权利的查询条件
		boolean havedy = false;//是否有多个单元的查询条件
		boolean flag_ql = false;
		boolean flag_qlr = false;
		boolean flag_zt = false;
//		builder3.append(" where  ");
		for (Entry<String, String> ent : queryvalues.entrySet()) {
			String name = ent.getKey();
			String value = ent.getValue();
			if (!StringHelper.isEmpty(value)) {
				// 抵押状态和查封状态，为‘0’的时候表示不过滤，查询全部
				if ((name.equals("CFZT") && value.equals("0"))
						|| (name.equals("DYZT") && value.equals("0"))) {
					continue;
				}

				// 权利人，权利人的条件比较特殊，where里边又套了where
				if (name.startsWith("QLR.")) {
					if (!flag_qlr) {
						builder_ql.append("LEFT JOIN BDCK.BDCS_QLR_XZ QLR ON QL.QLID = QLR.QLID ");
						flag_qlr = true;
						flag_ql = true;
					}
					if(haveql)
						builder3.append(" and ");
					if (iflike) {
						builder3.append(" instr(" + name + ",'" + value
								+ "')>0 ");
					} else {
						if (name.equals("QLR.ZJH") && value.length() == 18) {
							String oldCard = ConverIdCard.getOldIDCard(value);
							if (oldCard.length() != 15) {
								builder3.append(" " + name + "='" + value
										+ "' ");
							} else {
								builder3.append(" (" + name + "='" + value
										+ "' or ");
								builder3.append(" " + name + "='" + oldCard
										+ "') ");
							}
						} else {
							builder3.append(" " + name + "='" + value + "' ");
						}
					}
					haveql = true;
					continue;
				}		

				// 抵押状态
				if (name.equals("DYZT")) {
					if (flag_zt) {
						builder_zt.append(" AND ");
					}
					if (value.equals("1")) {
						builder_zt.append(" NOT EXISTS (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_XZ DYQ WHERE DYQ.BDCDYID = DY.BDCDYID AND DYQ.QLLX='23') ");
					} else {
						builder_zt.append(" EXISTS (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_XZ DYQ WHERE DYQ.BDCDYID = DY.BDCDYID AND DYQ.QLLX='23') ");
					}
					flag_zt = true;
					continue;
				}
				// 查封状态
				if (name.equals("CFZT")) {
					if (flag_zt) {
						builder_zt.append(" AND ");
					}
					if (value.equals("1")) {
						builder_zt.append(" NOT EXISTS (SELECT CF.DJDYID FROM BDCK.BDCS_QL_XZ CF WHERE CF.BDCDYID = DY.BDCDYID AND CF.QLLX='99') ");
					} else {
						builder_zt.append(" EXISTS (SELECT CF.DJDYID FROM BDCK.BDCS_QL_XZ CF WHERE CF.BDCDYID = DY.BDCDYID AND CF.QLLX='99') ");
					}
					flag_zt = true;
					continue;
				}
				
				// 房地状况
				if (name.equals("FDZK")) {
					
					if (value.equals("1")) {
						builder_zt.append(" NOT EXISTS(SELECT 1 FROM BDCK.BDCS_H_XZ H LEFT JOIN BDCK.BDCS_ZRZ_XZ ZRZ ON H.ZRZBDCDYID=ZRZ.BDCDYID  WHERE H.ZDBDCDYID=DY.BDCDYID) ");
					}else if(value.equals("2")){
						builder_zt.append(" EXISTS(SELECT 1 FROM BDCK.BDCS_H_XZ H LEFT JOIN BDCK.BDCS_ZRZ_XZ ZRZ ON H.ZRZBDCDYID=ZRZ.BDCDYID  WHERE H.ZDBDCDYID=DY.BDCDYID) ");
					}else{
						builder_zt.append(" 1=1 ");
					}
					flag_zt = true;
					continue;
				}
				
				//DY查询
				if (name.startsWith("DY.")) {	
					if (havedy) 
						dybuilder.append(" AND ");
					if (iflike) 
						dybuilder.append(" instr(" + name + ",'" + value + "')>0 ");
					else
						dybuilder.append(" " + name + "='" + value + "' ");
					havedy = true;
					continue;
				}
				
				// 权利性质
				if (name.equals("QLXZ")) {
					if (havedy) 
						dybuilder.append(" AND ");
					if (value.equals("1")) {
						dybuilder.append(" DY.QLXZ='100' ");
					}else if(value.equals("2")){
						dybuilder.append(" DY.QLXZ='200' ");
					}else if(value.equals("3")){
						dybuilder.append(" DY.QLXZ='101' ");
					}else if(value.equals("4")){
						dybuilder.append(" DY.QLXZ='102' ");
					}else if(value.equals("5")){
						dybuilder.append(" DY.QLXZ='203' ");
					}else{
						dybuilder.append(" 1=1 ");
					}
					havedy = true;
					continue;
				}
				if (haveql) 
					builder3.append(" AND ");
				//土地用途
				if (name.equals("TDYT")) {					
					if (value.equals("1")) {
						builder3.append(" TDYT.TDYT='071' ");
					}else if(value.equals("2")){
						builder3.append(" TDYT.TDYT='072' ");
					}else if(value.equals("3")){
						builder3.append(" TDYT.TDYT='051' ");
					}else if(value.equals("4")){
						builder3.append(" TDYT.TDYT='053' ");
					}else if(value.equals("5")){
						builder3.append(" TDYT.TDYT='052' ");
					}else if(value.equals("6")){
						builder3.append(" TDYT.TDYT='054' ");
					}else if(value.equals("7")){
						builder3.append(" TDYT.TDYT='085' ");
					}else if(value.equals("8")){
						builder3.append(" TDYT.TDYT='084' ");
					}else if(value.equals("9")){
						builder3.append(" TDYT.TDYT='081' ");
					}else if(value.equals("10")){
						builder3.append(" TDYT.TDYT='083' ");
					}else if(value.equals("11")){
						builder3.append(" TDYT.TDYT='061' ");
					}else if(value.equals("12")){
						builder3.append(" TDYT.TDYT='062' ");
					}else if(value.equals("13")){
						builder3.append(" TDYT.TDYT='063' ");
					}else{
						builder3.append(" 1=1 ");
					}
					haveql = true;
					continue;
				}

				if (iflike) {
					if (name.equals("DJSJ_Q")) {
						builder3.append("  QL.DJSJ >=to_date('"+queryvalues.get("DJSJ_Q")+"','yyyy-mm-dd') ");
					}else if (name.equals("DJSJ_Z")) {
						builder3.append("   QL.DJSJ <=(to_date('"+queryvalues.get("DJSJ_Z")+"','yyyy-mm-dd')+1) ");
					}else
						builder3.append(" instr(" + name + ",'" + value + "')>0 ");
					haveql = true;					
				}
				else if (name.equals("DJSJ_Q")) {
					builder3.append("  QL.DJSJ >=to_date('"+queryvalues.get("DJSJ_Q")+"','yyyy-mm-dd') ");
					haveql = true;
				}else if (name.equals("DJSJ_Z")) {
					builder3.append("   QL.DJSJ <=(to_date('"+queryvalues.get("DJSJ_Z")+"','yyyy-mm-dd')+1) ");
					haveql = true;
				}else {
					builder3.append(" " + name + "='" + value + "' ");
					haveql = true;
				}
			}
		}
		
		String dyString = "";
		if (!StringHelper.isEmpty(dybuilder)) {
			dyString = " AND " + dybuilder.toString();
		}
		String qlString ="";
		if (!StringHelper.isEmpty(builder3)) {
			qlString = " AND " +builder_ql.append(" WHERE　").append(builder3).append(")").toString();
		}
		String ztString ="";
		if (!StringHelper.isEmpty(builder_zt)) {
			ztString = " AND " +builder_zt.toString();
		}
		String condition = dyString + qlString + ztString;
		
		StringBuilder dysxbuilder = new StringBuilder();
		if (tdzt != null && tdzt.equals("1")) {//使用权宗地
			dysxbuilder.append(unitentityname_shyqzd).append(" WHERE 1=1").append(condition);	
		}else if (tdzt != null && tdzt.equals("2")) {//所有权宗地
			dysxbuilder.append(unitentityname_syqzd ).append(" WHERE 1=1").append(condition);
		}else if (tdzt != null && tdzt.equals("3")) {//全部
			dysxbuilder.append(unitentityname_shyqzd).append(" WHERE 1=1 ").append(condition)
					   .append(" UNION ")
					   .append(unitentityname_syqzd ).append(" WHERE 1=1").append(condition);	
		}				
		
		
//		String fromsql1="select ql.qlid " + fromstr + wherestr ;
		String fromSql = " FROM (SELECT BDCDYID FROM (" + dysxbuilder.toString() + "))";
		String fullSql = dysxbuilder.toString();
//		wherein=selectstr + fromstr + wherein;
		// 排序 条件语句 
		if(!StringUtils.isEmpty(sort)&&!StringUtils.isEmpty(order))
		{
		if(sort.toUpperCase().equals("ZL"))
			sort="ZL";
		if(sort.toUpperCase().equals("BDCDYH"))
			sort="BDCDYH";
		if(sort.toUpperCase().equals("BDCDYID"))
			sort="BDCDYID";
		if(sort.toUpperCase().equals("BDCDYLXMC"))
			sort="BDCDYLX";
		fullSql=fullSql+" ORDER BY "+sort+" "+order;
//			wherein=wherein+" ORDER BY "+sort+" "+order;
		}
		// 为了提高效率，先不查询符合条件的记录总数，将来改为异步获取 
		CommonDao dao = baseCommonDao;
		Long test1=System.currentTimeMillis(); 
		if("2".equals(cxzt)){
			fromSql = fromSql.replaceAll("_xz", "_ls").replaceAll("_XZ", "_LS");
			fullSql = fullSql.replaceAll("_xz", "_ls").replaceAll("_XZ", "_LS");
		}
		
		count = dao.getCountByFullSql(fromSql);
		
		Long test2=System.currentTimeMillis();
		listresult = dao.getPageDataByFullSql(fullSql, page, rows);
		
		addOtherInfo(listresult);
		
		Long test3=System.currentTimeMillis();
		addRightsHolderInfo(listresult);
		
		Long test4=System.currentTimeMillis();
		addLimitZDStatus(listresult);
		Long test5=System.currentTimeMillis();
		//国有土地使用权时，显示出土地上房屋状态。
		addLimitZDStausByFw(listresult);
		Long test6=System.currentTimeMillis();
		addDyCfDetails(listresult);
		Long test7=System.currentTimeMillis();
		addZRZCount(listresult);
		Long test8=System.currentTimeMillis();
//		List<Map> m=dao.getDataListByFullSql(fromsql1);
//		Long test9 =System.currentTimeMillis();
//		List<Map> d=dao.getDataListByFullSql(wherein);
//		Long test10=System.currentTimeMillis();
		System.out.println("addZRZCount方法消耗时间为："+(test8-test7));
		System.out.println("addDyCfDetails方法消耗时间为："+(test7-test6));
		System.out.println("addLimitZDStausByFw方法消耗时间为："+(test6-test5));
		System.out.println("addLimitZDStatus方法消耗时间为："+(test5-test4));
		System.out.println("addRightsHolderInfo方法消耗时间为："+(test4-test3));
		System.out.println("datalist修改成exist方法消耗时间为："+(test3-test2));
//		System.out.println("datalist中in方法消耗时间为："+(test10-test9));
		System.out.println("Count方法消耗时间为："+(test2-test1));
//		System.out.println("弃用Count方法消耗的时间为："+(test9-test8)+",弃用Count方法总和为"+m.size()+",共Count方法总和为"+count);
		System.out.println("共消耗时间为："+(test8-test1));
		// 格式化结果中的登簿时间
		for (Map map : listresult) {
			if (map.containsKey("DJSJ")) {
				map.put("DJSJ",StringHelper.FormatYmdhmsByDate(map.get("DJSJ")));
			}
			if (map.containsKey("QLXZ")) {
				map.put("QLXZ",ConstHelper.getNameByValue("QLXZ",(String)map.get("QLXZ")));
			}
			if (map.containsKey("TDYT")) {
				map.put("TDYT",ConstHelper.getNameByValue("TDYT",(String)map.get("TDYT")));			}
		}
		msg.setTotal(count);
		msg.setRows(listresult);
		return msg;
	}
	
    /**
	 * 邹彦辉
	 * 土地信息查询
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public Message queryLandyt(Map<String, String> queryvalues, int page,
			int rows, boolean iflike, String tdzt,String sort,String order,
			String td_xzqh,String td_qllx,String td_qlsdfs,String td_qlxz,String td_tddj,
			String td_qlrlx,String td_tdyt,String td_fzdy,String fzlb,String zddy) {
		Message msg = new Message();
		//如果等于null则等于""
		String ZDDY = zddy != null ? zddy:"";
		//房屋详情
		if(ZDDY.equals("2")){
			
			List<Map> listresult = null;
			long count = 0;
			StringBuilder build = new StringBuilder();
			StringBuilder buildcount = new StringBuilder();
			StringBuilder fzdy = new StringBuilder();
			StringBuilder buildxzqh = new StringBuilder();
			StringBuilder buildqllx = new StringBuilder();
			StringBuilder buildqlsdfs = new StringBuilder();
			StringBuilder buildqlxz = new StringBuilder();
			StringBuilder buildtddj = new StringBuilder();
			StringBuilder buildqlrlx = new StringBuilder();
			StringBuilder buildtdyt = new StringBuilder();
			if (td_fzdy.equals("行政区划")) {
				fzdy.append("ql.qllx");//暂未跨库
			}else if (td_fzdy.equals("权利类型")) {
				fzdy.append("ql.qllx"); 
			}else if (td_fzdy.equals("权利设定方式")) {
				fzdy.append("dy.qlsdfs");
			}else if (td_fzdy.equals("权利性质")) {
				fzdy.append("dy.qlxz");
			}else if (td_fzdy.equals("土地等级")) {
				fzdy.append("tdyt.tddj");
			}else if (td_fzdy.equals("权利人类型")) {
				fzdy.append("qlr.qlrlx");
			}else if (td_fzdy.equals("土地用途")){
				fzdy.append("tdyt.tdyt");
			}else {
				fzdy.append("ql.qllx");
			}
			
			//行政区划
			
			//权利类型
			if(td_qllx.length() > 0 && td_qllx!=null && td_qllx!=""){
				String[] value = td_qllx.split(",");
				for (int i = 0; i < value.length; i++) {
					if (!value[i].equals("10000")) {
						buildqllx.delete(0, buildqllx.length());
						buildqllx.append(" and ql.qllx='"+td_qllx+"'");
					}else {
						buildqllx.append(" ");
						break;
					}
				}
			}
			
			//权利设定方式
			if(td_qlsdfs.length() > 0 && td_qlsdfs!=null && td_qlsdfs!=""){
				String[] value = td_qlsdfs.split(",");
				for (int i = 0; i < value.length; i++) {
					if (!value[i].equals("10000")) {
						buildqlsdfs.delete(0, buildqlsdfs.length());
						buildqlsdfs.append(" and dy.qlsdfs='"+td_qlsdfs+"'");
					}else {
						buildqlsdfs.append(" ");
						break;
					}
				}
			}
			
			//权利性质
			if(td_qlxz.length() > 0 && td_qlxz!=null && td_qlxz!=""){
				String[] value = td_qlxz.split(",");
				for (int i = 0; i < value.length; i++) {
					if (!value[i].equals("10000")) {
						buildqlxz.delete(0, buildqlxz.length());
						buildqlxz.append(" and dy.qlxz='"+td_qlxz+"'");
					}else {
						buildqlxz.append(" ");
						break;
					}
				}
			}
			
			//土地等级
			if(td_tddj.length() > 0 && td_tddj!=null && td_tddj!=""){
				String[] value = td_tddj.split(",");
				for (int i = 0; i < value.length; i++) {
					if (!value[i].equals("10000")) {
						buildtddj.delete(0, buildtddj.length());
						buildtddj.append(" and tdyt.tddj='"+td_tddj+"'");
					}else {
						buildtddj.append(" ");
						break;
					}
				}
			}
			
			//权利人类型
			if(td_qlrlx.length() > 0 && td_qlrlx!=null && td_qlrlx!=""){
				String[] value = td_qlrlx.split(",");
				for (int i = 0; i < value.length; i++) {
					if (!value[i].equals("10000")) {
						buildqlrlx.delete(0, buildqlrlx.length());
						buildqlrlx.append(" and qlr.qlrlx='"+td_qlrlx+"'");
					}else {
						buildqlrlx.append(" ");
						break;
					}
				}
			}
			
			//土地用途
			if(td_tdyt.length() > 0 && td_tdyt!=null && td_tdyt!=""){
				String[] value = td_tdyt.split(",");
				for (int i = 0; i < value.length; i++) {
					if (!value[i].equals("10000")) {
						buildtdyt.delete(0, buildtdyt.length());
						buildtdyt.append(" and tdyt.tdyt='"+td_tdyt+"'");
					}else {
						buildtdyt.append(" ");
						break;
					}
				}
			}
			
	   build.append(" select")
			.append(" DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,DY.BDCDYLX AS BDCDYLX,")
			.append(" DY.BDCDYLXMC AS BDCDYLXMC,QL.QLID,QL.BDCQZH ");
  buildcount.append(" from (")
			.append(" SELECT ")
			.append(" ZL,ZDDM,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC,ZDMJ,QLSDFS,QLXZ")
			.append(" FROM BDCK.BDCS_SYQZD_XZ")
			.append(" UNION ALL")
			.append(" SELECT")
			.append(" ZL,ZDDM,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC ,ZDMJ,QLSDFS,QLXZ")
			.append(" FROM BDCK.BDCS_SHYQZD_XZ) DY")
			.append(" left join BDCK.BDCS_tdyt_xz tdyt on dy.bdcdyid=tdyt.bdcdyid")
			.append(" left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid") 
			.append(" left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid AND (QLLX=3 OR qllx=99 or qllx=23 or DJLX=600)")
	   		.append(" left join BDCK.bdcs_qlr_xz qlr on ql.qlid  = qlr.qlid") 
	   		.append(" left join BDCK.BDCS_const const on "+fzdy+"  = const.constvalue") 
	   		.append(" left join BDCK.BDCS_CONSTCLS CONSTCLS on const.CONSTSLSID = CONSTCLS.CONSTSLSID") 
	   		.append(" WHERE const.CONSTTRANS='"+fzlb+"' "+buildqlrlx+""+buildqlsdfs+""+buildqlxz+""+buildtddj+""+buildqlrlx+""+buildtdyt);//+"   ORDER BY DY.BDCDYID asc");
			CommonDao dao = baseCommonDao;
			count = dao.getCountByFullSql(buildcount.toString());
			listresult = dao.getPageDataByFullSql(build.toString()+buildcount.toString(), page, rows);
			
			addRightsHolderInfo(listresult);
			addLimitZDStatus(listresult);
			//国有土地使用权时，显示出土地上房屋状态。
			addLimitZDStausByFw(listresult);
			addDyCfDetails(listresult);
			addZRZCount(listresult);
			msg.setTotal(count);
			msg.setRows(listresult);
		}else{
			String fsqlcfwh=queryvalues.get("FSQL.CFWH");
			long count = 0;
			List<Map> listresult = null;
			/* ===============1、先获取实体对应的表名=================== */
			String unitentityName = "BDCK.BDCS_SHYQZD_XZ";

			/* ===============2、再获取表名+'_'+字段名=================== */
			String dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC ";
			String qlfieldsname = "QL.QLID,QL.BDCQZH";
			String fsqlfieldsname = "FSQL.FSQLID,FSQL.CFWH";

			if (tdzt != null && tdzt.equals("2")) {
				unitentityName = "BDCK.BDCS_SYQZD_XZ";
				dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,'01' AS BDCDYLX,'所有权宗地' AS BDCDYLXMC ";
			}

			// 集合使用权宗地与所有权宗地
			if (tdzt != null && tdzt.equals("3")) {
				unitentityName = "(SELECT ZL,ZDDM,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC FROM BDCK.BDCS_SYQZD_XZ UNION ALL SELECT ZL,ZDDM,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC FROM BDCK.BDCS_SHYQZD_XZ)";
				dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC";
			}

			/* ===============3、构造查询语句=================== */
			/* SELECT 字段部分 */
			StringBuilder builder2 = new StringBuilder();
			builder2.append("select ").append(dyfieldsname).append(",")
					.append(qlfieldsname);
			if(!StringHelper.isEmpty(fsqlcfwh)){
				builder2.append(",").append(fsqlfieldsname);
			}
			String selectstr = builder2.toString();

			/* FROM 后边的表语句 */
			StringBuilder builder = new StringBuilder();
			builder.append(" from {0} DY")
					.append(" left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid")
					.append(" left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid  ");
			if(!StringHelper.isEmpty(fsqlcfwh)){
				builder.append(" left join BDCK.bdcs_fsql_xz fsql on ql.fsqlid=fsql.fsqlid  ");
			}
			String fromstr = builder.toString();
			fromstr = MessageFormat.format(fromstr, unitentityName);

			/* WHERE 条件语句 */
			StringBuilder builder3 = new StringBuilder();
			builder3.append(" where  ");
			StringBuilder qlrbuilder = new StringBuilder();
			boolean havecondition = false;
			boolean haveqlr = false;
			for (Entry<String, String> ent : queryvalues.entrySet()) {
				String name = ent.getKey();
				String value = ent.getValue();
				if (!StringHelper.isEmpty(value)) {
					// 抵押状态和查封状态，为‘0’的时候表示不过滤，查询全部
					if ((name.equals("CFZT") && value.equals("0"))
							|| (name.equals("DYZT") && value.equals("0"))) {
						continue;
					}

					// 权利人，权利人的条件比较特殊，where里边又套了where
					if (name.startsWith("QLR.")) {
						if (haveqlr)
							qlrbuilder.append(" and ");
						if (iflike) {
							qlrbuilder.append(" instr(" + name + ",'" + value
									+ "')>0 ");
						} else {
							if (name.equals("QLR.ZJH") && value.length() == 18) {
								String oldCard = ConverIdCard.getOldIDCard(value);
								if (oldCard.length() != 15) {
									qlrbuilder.append(" " + name + "='" + value
											+ "' ");
								} else {
									qlrbuilder.append(" (" + name + "='" + value
											+ "' or ");
									qlrbuilder.append(" " + name + "='" + oldCard
											+ "') ");
								}
							} else {
								qlrbuilder.append(" " + name + "='" + value + "' ");
							}
						}
						haveqlr = true;
						continue;
					}

					if (havecondition) {
						builder3.append(" and ");
					}

					// 抵押状态
					if (name.equals("DYZT")) {
						if (value.equals("1")) {
							builder3.append(" djdy.djdyid NOT IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_XZ DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
						} else {
							builder3.append(" djdy.djdyid IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_XZ DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
						}
						havecondition = true;
						continue;
					}
					// 查封状态
					if (name.equals("CFZT")) {
						if (value.equals("1")) {
							builder3.append("  djdy.DJDYID NOT IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_XZ CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99') ");
						} else {
							builder3.append("  djdy.DJDYID IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_XZ CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99') ");
						}
						havecondition = true;
						continue;
					}

					if (iflike) {
						builder3.append(" instr(" + name + ",'" + value + "')>0 ");
						builder3.append(" and ql.djlx!='800'");
					} else {
						builder3.append(" " + name + "='" + value + "' ");
					}
					havecondition = true;
				}
			}
			// 有权利人查询条件
			if (!StringHelper.isEmpty(qlrbuilder.toString())) {
				if (havecondition) {
					builder3.append(" and ");
				}
				builder3.append(" QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ QLR WHERE "
						+ qlrbuilder.toString() + ")");
				havecondition = true;
			}

			if (tdzt == null || !tdzt.equals("2")) {
				if (havecondition) {
					// builder3.append(" and ");
				}
				// builder3.append(" ql.qllx='4'");
			}
			String wherestr = builder3.toString();
			if (wherestr.trim().toUpperCase().endsWith("WHERE")) {
				wherestr = "";
			}
			String fromSql = " from (select ql.qlid " + fromstr + wherestr + ")";
			String fullSql = selectstr + fromstr + wherestr;
			/* 排序 条件语句 */
			if(!StringUtils.isEmpty(sort)&&!StringUtils.isEmpty(order))
			{
			if(sort.toUpperCase().equals("ZL"))
				sort="DY.ZL";
			if(sort.toUpperCase().equals("BDCDYH"))
				sort="DY.BDCDYH";

			if(sort.toUpperCase().equals("BDCQZH"))
				sort="QL.BDCQZH";

			if(sort.toUpperCase().equals("BDCDYID"))
				sort="DY.BDCDYID";
			if(sort.toUpperCase().equals("BDCDYLXMC"))
				sort="DY.BDCDYLX";
				fullSql=fullSql+" ORDER BY "+sort+" "+order;
			}
			/* 为了提高效率，先不查询符合条件的记录总数，将来改为异步获取 */
			CommonDao dao = baseCommonDao;

			count = dao.getCountByFullSql(fromSql);
			listresult = dao.getPageDataByFullSql(fullSql, page, rows);
			addRightsHolderInfo(listresult);
			addLimitZDStatus(listresult);
			//国有土地使用权时，显示出土地上房屋状态。
		//	addLimitZDStausByFw(listresult);
			addDyCfDetails(listresult);
			addZRZCount(listresult);
			msg.setTotal(count);
			msg.setRows(listresult);
		}
		
		return msg;
	}

	/**
	 * 自然幢信息查询
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public Message querybuilding(Map<String, String> conditionmap, int page,String Szl,String Sbdcdyh,String Slpzt,String Sxmmc,int rows,boolean iflike,String kfsmc) {
		Message msg = new Message();
		long count = 0;
		List<Map> listresult = null;
		/* ===============查詢字段=================== */
		String fulsql = "BDCDYID,BDCDYH,ZL,ZRZH,ZZDMJ,DSCS,DXCS,BDCDYLX,LY,XMMC,ZDBDCDYID,BZ,KFSMC  ";
		StringBuilder builder2 = new StringBuilder();
		builder2.append("select ").append(fulsql); 
		String selectstr = builder2.toString(); 
		/* ===============查詢表名=================== */
		
		StringBuilder builder = new StringBuilder();
		StringBuilder builder_qf = new StringBuilder();
		StringBuilder builder_xf = new StringBuilder();
		//08预测自然幢、03自然幢
		builder_qf.append("SELECT BDCDYID,BDCDYH,ZL,ZRZH,ZZDMJ,DSCS,DXCS,'08' BDCDYLX,'02' LY, XMMC,ZDBDCDYID,BZ,KFSMC FROM BDCK.BDCS_ZRZ_XZY");
		builder_qf.append(" UNION ");
		builder_qf.append("SELECT BDCDYID,BDCDYH,ZL,ZRZH,ZZDMJ,DSCS,DXCS,'08' BDCDYLX,'04' LY, XMMC,ZDBDCDYID,BZ,KFSMC FROM BDCDCK.BDCS_ZRZ_GZY DCY WHERE NOT EXISTS (SELECT 1 FROM BDCK.BDCS_ZRZ_XZY XZY WHERE XZY.BDCDYID = DCY.BDCDYID)");
		
		builder_xf.append("SELECT BDCDYID,BDCDYH,ZL,ZRZH,ZZDMJ,DSCS,DXCS,'03' BDCDYLX,'02' LY, XMMC,ZDBDCDYID,BZ,KFSMC FROM BDCK.BDCS_ZRZ_XZ");
		builder_xf.append(" UNION ");
		builder_xf.append("SELECT BDCDYID,BDCDYH,ZL,ZRZH,ZZDMJ,DSCS,DXCS,'03' BDCDYLX,'04' LY, XMMC,ZDBDCDYID,BZ,KFSMC FROM BDCDCK.BDCS_ZRZ_GZ DC WHERE NOT EXISTS (SELECT 1 FROM BDCK.BDCS_ZRZ_XZ XZ WHERE XZ.BDCDYID = DC.BDCDYID)");

		builder.append(" from ( "); 
		
		// 楼盘状态
		if ( "0".equals(Slpzt)) {//全部
			builder.append(builder_qf.toString()).append(" UNION ").append(builder_xf.toString());
		}else if ("1".equals(Slpzt)) {			
			builder.append(builder_qf.toString());
		} else if ("2".equals(Slpzt)){
			builder.append(builder_xf.toString());
		}
		
		builder.append(") ZRZ");
		String fromstr = builder.toString();
		/* ===============篩選=================== */
		StringBuilder builder3 = new StringBuilder();
		builder3.append(" where 1=1 ");
		// 坐落
		if (!StringUtils.isEmpty(Szl)) {
			builder3.append(" and ZL like '%" + Szl + "%'");
		}
		// StrBDCDYH 不动产单元号
		if (!StringUtils.isEmpty(Sbdcdyh)) {
			builder3.append(" and BDCDYH like '%" + Sbdcdyh + "%'");
		}
		//XMMC
		if (!StringUtils.isEmpty(Sxmmc)) {
			builder3.append(" and XMMC like '%" + Sxmmc + "%'");
		}
		//KFSMC
		if (!StringUtils.isEmpty(kfsmc)) {
			builder3.append(" and KFSMC like '%" + kfsmc + "%'");
		}
		builder3.append(" ORDER BY BDCDYLX,ZL");
		String wherestr = builder3.toString();
		String fromSql = fromstr + wherestr;
		String fullSql = selectstr + fromstr + wherestr;
		
		/* 为了提高效率，先不查询符合条件的记录总数，将来改为异步获取 */
		CommonDao dao = baseCommonDao;
		count = dao.getCountByFullSql(fromSql);
		listresult = dao.getPageDataByFullSql(fullSql, page, rows);			
		for (int i = 0; i < listresult.size(); i++) {
			Map resultMap = listresult.get(i);
			long count_zjdy = 0,count_xtoq = 0,count_qtox = 0,mortgageCount = 0, SealCount = 0;
			/****房屋状态****/
			String sql_fw ="select BDCDYLX,LY,BDCDYID,ZRZBDCDYID,BDCDYH,ZL,FWYT,GHYT,JZMJ,FTJZMJ,FWJG,DYTDMJ,FTTDMJ "
					+ "FROM (SELECT '031' BDCDYLX,'02' LY,BDCDYID,ZRZBDCDYID,BDCDYH,ZL,FWYT1 FWYT,GHYT,SCJZMJ JZMJ,SCFTJZMJ FTJZMJ,FWJG,DYTDMJ,FTTDMJ FROM BDCK.BDCS_H_XZ  "
					+ "UNION SELECT '032' BDCDYLX,'02' LY,BDCDYID,ZRZBDCDYID,BDCDYH,ZL,FWYT1 FWYT,GHYT,YCJZMJ JZMJ,YCFTJZMJ FTJZMJ,FWJG,DYTDMJ,FTTDMJ FROM BDCK.BDCS_H_XZY) FW "
					+ "WHERE ZRZBDCDYID='" + resultMap.get("BDCDYID") + "'";
			List<Map> listresult_fw = dao.getDataListByFullSql(sql_fw);
			//是否首次登记
			if (listresult_fw != null && listresult_fw.size()>0) 
				resultMap.put("SFSC", "是");
			else 
				resultMap.put("SFSC", "否");
			
			
			for (int j = 0; j < listresult_fw.size(); j++) {
				Map resultMap_fw = listresult_fw.get(j);				
				//是否关联期现房
				if ("031".equals(resultMap_fw.get("BDCDYLX"))) {
					List<YC_SC_H_XZ> REA = new ArrayList<YC_SC_H_XZ>();					
					REA = baseCommonDao.getDataList(YC_SC_H_XZ.class, " SCBDCDYID='"+resultMap_fw.get("BDCDYID")+"'");
					if(REA.size()>0){
						count_xtoq++;
					}
				}else if ("032".equals(resultMap_fw.get("BDCDYLX"))) {
					List<YC_SC_H_XZ> REA = new ArrayList<YC_SC_H_XZ>();			
					REA = baseCommonDao.getDataList(YC_SC_H_XZ.class, " YCBDCDYID='"+resultMap_fw.get("BDCDYID")+"'");
					if(REA.size()>0){
						count_qtox++;
					}
					//是否在建工程抵押
					//获取预测户现状权利
					String sql_xzql_ych ="SELECT QL.* FROM BDCK.BDCS_QL_XZ QL "
							+ "LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON QL.DJDYID = DJDY.DJDYID "
							+ "LEFT JOIN BDCK.BDCS_H_XZY H ON DJDY.BDCDYID = H.BDCDYID "
							+ "WHERE H.BDCDYID='"+ resultMap_fw.get("BDCDYID") + "' AND QL.QLLX='23'";
					List<Map> listresult_fwql_y = dao.getDataListByFullSql(sql_xzql_ych);				
					if (listresult_fwql_y != null && listresult_fwql_y.size()>0) {
						Map result_fwql_y = listresult_fwql_y.get(0);					
						String ywlx = GetYWLX(result_fwql_y.get("DJLX").toString(), result_fwql_y.get("QLLX").toString(), BDCDYLX.YCH);
						if("在建工程抵押登记".equals(ywlx)){
							if (!StringHelper.isEmpty(result_fwql_y.get("LYQLID"))) {
								Rights lsbyls = RightsTools.loadRights(DJDYLY.LS, result_fwql_y.get("LYQLID").toString());
								if(QLLX.DIYQ.Value.equals(lsbyls.getQLLX())&&DJLX.YGDJ.Value.equals(lsbyls.getDJLX())){
									ywlx = "期房抵押预告登记";
								}
							}
						}
						if("在建工程抵押登记".equals(ywlx)){
							count_zjdy++;
							break;
						}
					}
					
				}
			}			
			//是否关联期现房
			if (count_xtoq > 0 || count_qtox > 0) 
				resultMap.put("SFGLQZF", "是");
			else 
				resultMap.put("SFGLQZF", "否");
			//是否在建工程抵押
			if (count_zjdy > 0) 
				resultMap.put("SFZJDY", "是");
			else 
				resultMap.put("SFZJDY", "否");
			
			/****土地状态****/
			//获取宗地权利
			String sql_xzql_zd ="SELECT QL.* "
					+ "FROM (SELECT DJH,ZL,ZDDM,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC FROM BDCK.BDCS_SYQZD_XZ "
					+ "UNION ALL SELECT DJH,ZL,ZDDM,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC FROM BDCK.BDCS_SHYQZD_XZ) DY "
					+ "INNER JOIN BDCK.BDCS_DJDY_XZ DJDY ON DY.BDCDYID=DJDY.BDCDYID "
					+ "INNER JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=DJDY.DJDYID  "
					+ "WHERE DY.BDCDYID='"+ resultMap.get("ZDBDCDYID") + "'";			
			List<Map> listresult_zdql = dao.getDataListByFullSql(sql_xzql_zd);	
			String qlrmc="";
			for (int j = 0; j < listresult_zdql.size(); j++) {
				Map result_zdql = listresult_zdql.get(j);
				if ("23".equals(result_zdql.get("QLLX"))) {
					mortgageCount++;
				}else if ("99".equals(result_zdql.get("QLLX")) && "800".equals(result_zdql.get("DJLX"))) {
					SealCount++;
				}
				//权利人和土地证号
				String qllxarray[] = {"1","2","3","5","7"};
				int index = Arrays.binarySearch(qllxarray, result_zdql.get("QLLX"));
				if (index != -1) {
					if (StringHelper.isEmpty(result_zdql.get("QLRID"))) {
						String sql_xzqlr_zd ="SELECT QLR.QLRMC,QLR.BDCQZH FROM BDCK.BDCS_QLR_XZ QLR WHERE QLR.QLID ='"
								+ result_zdql.get("QLID")+"'";
						List<Map> listresult_zdqlr = dao.getDataListByFullSql(sql_xzqlr_zd);
						for (int k = 0; k < listresult_zdqlr.size(); k++) {
							if (!StringHelper.isEmpty(listresult_zdqlr.get(k).get("QLRMC"))) {
								qlrmc = StringHelper.isEmpty(qlrmc) ? listresult_zdqlr.get(k).get("QLRMC").toString() :qlrmc + ","+listresult_zdqlr.get(k).get("QLRMC").toString();
							}
							if (!StringHelper.isEmpty(listresult_zdqlr.get(k).get("BDCQZH"))) {
								resultMap.put("TDZH", listresult_zdqlr.get(k).get("BDCQZH").toString());
							}
						} 
					}
				}		
			}
			resultMap.put("TDQLR", qlrmc);
			//宗地是否查封
			if (SealCount > 0) {
				resultMap.put("SFZDCF", "是");
			}else {
				resultMap.put("SFZDCF", "否");
			}
			//宗地时候抵押
			if (mortgageCount > 0) {
				resultMap.put("SFZDDY", "是");
			}else {
				resultMap.put("SFZDDY", "否");
			}
		}
		msg.setTotal(count);
		msg.setRows(listresult);
		return msg;
		
	}
	
	/**
	 * 自然幢关联房屋信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Message queryfwlist(String bdcdyid,Map<String, String> querycondition, int page, int rows) {
		Message msg = new Message();
		long count = 0;
		List<Map> listresult = null;
		/* ===============查詢字段=================== */
		String fulsql = "BDCDYLX,LY,BDCDYID,ZRZBDCDYID,BDCDYH,ZL,FWYT,GHYT,JZMJ,FTJZMJ,FWJG,DYTDMJ,FTTDMJ";
		StringBuilder builder2 = new StringBuilder();
		builder2.append("select ").append(fulsql); 
		String selectstr = builder2.toString(); 
		/* ===============查詢表名=================== */
		StringBuilder builder = new StringBuilder();
		builder.append(" FROM (");
		builder.append("SELECT '031' BDCDYLX,'02' LY,BDCDYID,ZRZBDCDYID,BDCDYH,ZL,FWYT1 FWYT,GHYT,SCJZMJ JZMJ,SCFTJZMJ FTJZMJ,FWJG,DYTDMJ,FTTDMJ FROM BDCK.BDCS_H_XZ ");
		builder.append(" UNION ");
		builder.append("SELECT '032' BDCDYLX,'02' LY,BDCDYID,ZRZBDCDYID,BDCDYH,ZL,FWYT1 FWYT,GHYT,YCJZMJ JZMJ,YCFTJZMJ FTJZMJ,FWJG,DYTDMJ,FTTDMJ FROM BDCK.BDCS_H_XZY");
		builder.append(") FW");
		String fromstr = builder.toString();
		/* ===============篩選=================== */
		StringBuilder builder3 = new StringBuilder();
		builder3.append(" WHERE ZRZBDCDYID='"+bdcdyid+"'");
		
		String wherestr = builder3.toString();
		String fromSql = fromstr + wherestr;
		String fullSql = selectstr + fromstr + wherestr;
		/* 为了提高效率，先不查询符合条件的记录总数，将来改为异步获取 */
		CommonDao dao = baseCommonDao;

		count = dao.getCountByFullSql(fromSql);
		listresult = dao.getPageDataByFullSql(fullSql, page, rows);
		if(listresult!=null&&listresult.size()>0){
			for(Map m:listresult){
				m.put("FWYT", ConstHelper.getNameByValue("FWYT", StringHelper.formatObject(m.get("FWYT"))));
				m.put("GHYT", ConstHelper.getNameByValue("FWYT", StringHelper.formatObject(m.get("GHYT"))));
				m.put("FWJG", ConstHelper.getNameByValue("FWJG", StringHelper.formatObject(m.get("FWJG"))));				
				//加入djdyid，便于调用addLimitStatus方法
				String sql_djdyid = "select djdy.djdyid from bdck.bdcs_djdy_xz djdy  where djdy.bdcdyid='"+m.get("BDCDYID")+"'";
				List<Map> listresult_djdyid = dao.getDataListByFullSql(sql_djdyid);
				if (listresult_djdyid!=null && listresult_djdyid.size()>0) {
					m.put("DJDYID",listresult_djdyid.get(0).get("DJDYID"));
				}
				
				//是否在建工程抵押
				m.put("SFZJDY","否");
				if ("032".equals(m.get("BDCDYLX"))) {			
					String sql_xzql_ych ="SELECT QL.* FROM BDCK.BDCS_QL_XZ QL "
							+ "LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON QL.DJDYID = DJDY.DJDYID "
							+ "LEFT JOIN BDCK.BDCS_H_XZY H ON DJDY.BDCDYID = H.BDCDYID "
							+ "WHERE H.BDCDYID='"+ m.get("BDCDYID") + "' AND QL.QLLX='23'";
					List<Map> listresult_fwql_y = dao.getDataListByFullSql(sql_xzql_ych);	
					if ( listresult_fwql_y != null && listresult_fwql_y.size() > 0) {
						Map result_fwql_y = listresult_fwql_y.get(0);					
						String ywlx = GetYWLX(result_fwql_y.get("DJLX").toString(), result_fwql_y.get("QLLX").toString(), BDCDYLX.YCH);
						if("在建工程抵押登记".equals(ywlx)){
							if (!StringHelper.isEmpty(result_fwql_y.get("LYQLID"))) {
								Rights lsbyls = RightsTools.loadRights(DJDYLY.LS, result_fwql_y.get("LYQLID").toString());
								if(QLLX.DIYQ.Value.equals(lsbyls.getQLLX())&&DJLX.YGDJ.Value.equals(lsbyls.getDJLX())){
									ywlx = "期房抵押预告登记";
								}
							}
						}
						if("在建工程抵押登记".equals(ywlx))
							m.put("SFZJDY","是");
					}
				}
				//是否办理首次登记
				m.put("SFSC","否");
				String sql_sfsc ="SELECT QL.* "
						+ "FROM (SELECT BDCDYID FROM BDCK.BDCS_H_LS "
						+ "UNION ALL SELECT BDCDYID FROM BDCK.BDCS_H_LSY) DY "
						+ "LEFT JOIN BDCK.BDCS_DJDY_LS DJDY ON DY.BDCDYID=DJDY.BDCDYID "
						+ "LEFT JOIN BDCK.BDCS_QL_LS QL ON QL.DJDYID=DJDY.DJDYID  "
						+ "WHERE DY.BDCDYID='"+ m.get("BDCDYID") + "' AND DJLX='100'";	
				List<Map> listresult_sfsc = dao.getDataListByFullSql(sql_sfsc);
				if ( listresult_sfsc != null && listresult_sfsc.size() > 0) {
					m.put("SFSC","是");
				}
				
				
			}
		}
		//期现关链
		addQXGL(listresult);
		//抵押状态
		addLimitStatus(listresult,"");
		//排序
		final String sortString = querycondition.get("sort");
		Collections.sort(listresult,new Comparator<Map>(){
			@Override
			public int compare(Map o1, Map o2) {
				String str1=StringHelper.formatObject(o1.get(sortString));
				String str2=StringHelper.formatObject(o2.get(sortString));
				if(StringHelper.isEmpty(str1)||!StringHelper.isEmpty(str2)&&ObjectHelper.cat(str1,str2)<0)
					return -1;
				if(StringHelper.isEmpty(str2)||!StringHelper.isEmpty(str1)&&ObjectHelper.cat(str1,str2)>0)
					return 1;
				return 0;
			}
			
		});
		
		msg.setTotal(count);
		msg.setRows(listresult);
		return msg;
	}
	
	
	/**
	 * 获取预测与实测id的集合 author:mss
	 */

	@SuppressWarnings({ "rawtypes" })
	@Override
	public String GetScidandYcid(String bdcdyid) {
		BDCDYLX lx = null;
		String ycbdcdyid = "";
		String scbdcdyid = "";
		String ycscbdcdyid = "";
		List<BDCS_H_XZ> listdy = baseCommonDao.getDataList(BDCS_H_XZ.class,
				"bdcdyid='" + bdcdyid + "'");

		if (listdy != null && listdy.size() > 0) {
			lx = listdy.get(0).getBDCDYLX();
		} else {
			List<BDCS_H_XZY> listdyy = baseCommonDao.getDataList(
					BDCS_H_XZY.class, "bdcdyid='" + bdcdyid + "'");
			if (listdyy != null && listdyy.size() > 0) {
				lx = listdyy.get(0).getBDCDYLX();
			} else {
				lx = BDCDYLX.SHYQZD;
			}
		}

		if (lx.Value.equals("031")) {
			StringBuilder builderQX = new StringBuilder();
			builderQX
					.append("SELECT YCBDCDYID FROM BDCK.YC_SC_H_XZ WHERE SCBDCDYID='");
			builderQX.append(bdcdyid);
			builderQX.append("'");
			List<Map> ycList = baseCommonDao.getDataListByFullSql(builderQX
					.toString());
			if (ycList != null && ycList.size() > 0) {
				ycbdcdyid = ycList.get(0).get("YCBDCDYID").toString();
			}
		} else if (lx.Value.equals("032")) {
			StringBuilder builderQX = new StringBuilder();
			builderQX
					.append("SELECT SCBDCDYID FROM BDCK.YC_SC_H_XZ WHERE YCBDCDYID='");
			builderQX.append(bdcdyid);
			builderQX.append("'");
			List<Map> scList = baseCommonDao.getDataListByFullSql(builderQX
					.toString());
			if (scList != null && scList.size() > 0) {
				scbdcdyid = scList.get(0).get("SCBDCDYID").toString();
			}
		}

		if (ycbdcdyid != null && ycbdcdyid != "") {
			ycscbdcdyid = bdcdyid + "," + ycbdcdyid;
		} else if (scbdcdyid != null && scbdcdyid != "") {
			ycscbdcdyid = bdcdyid + "," + scbdcdyid;
		} else {
			ycscbdcdyid = bdcdyid;
		}
		return ycscbdcdyid;

	}
	
	/**
	 * 根据宗地的登记单元ID区分出宗地下的商品房和私房类型
	 * 再根据该宗地下有无抵押房屋状态来给宗地状态赋值
	 * @param result
	 */
	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	protected void addLimitZDStausByFwEx(List<Map> result){

		if(result != null && result.size() > 0){
			for(Map map : result){
				long mortgageCount = 0,mortgagingCount=0;
				long SealCount = 0,SealingCount=0;
				long ObjectionCount = 0,ObjectioningCount=0;
				long LimitCount = 0,LimitingCount=0;
				long housecount = 0;
				//只有使用权宗地才有房屋
				if(map.containsKey("BDCDYID")&&map.get("BDCDYLX").equals("02")){
					String zdbdcdyid = (String) map.get("BDCDYID");
					if(zdbdcdyid != null ){
						String fullSql = MessageFormat.format("select BDCDYH from BDCK.BDCS_H_XZ where zdbdcdyid=''{0}'' union all select BDCDYH from BDCK.BDCS_H_XZY where zdbdcdyid=''{0}''", zdbdcdyid);
						List<Map> bdcdyhs = baseCommonDao.getDataListByFullSql(fullSql);
						housecount=bdcdyhs.size();
						//商品房的土地抵消状态赋值
						if(bdcdyhs != null && bdcdyhs.size() > 0){
							for(Map bdcdyh :bdcdyhs){
								if(bdcdyh.containsKey("BDCDYH")){
									String _bdcdyh = (String) bdcdyh.get("BDCDYH");
									if(_bdcdyh != null ){
										//已办理SQL语句
										String sqlMortgage = MessageFormat.format("SELECT QLID from BDCK.BDCS_QL_XZ where bdcdyh=''{0}'' and qllx=''23''", _bdcdyh);
										String sqlSeal = MessageFormat.format(" SELECT QLID from BDCK.BDCS_QL_XZ where bdcdyh=''{0}'' and djlx=''800'' and qllx=''99''", _bdcdyh);
										String sqlObjection = MessageFormat.format("SELECT QLID  from BDCK.BDCS_QL_XZ where bdcdyh=''{0}'' and djlx=''600'' ", _bdcdyh);
										String sqlLimit = MessageFormat.format("SELECT BDCDYID from BDCK.BDCS_DYXZ where bdcdyh=''{0}'' and yxbz=''1'' ", _bdcdyh);
										//正在办理的SQL语句
										String sqlMortgaging = MessageFormat.format("SELECT a.XMBH FROM bdck.bdcs_xmxx a  LEFT JOIN bdck.bdcs_ql_gz c ON a.xmbh=c.xmbh and  a.sfdb=''0'' WHERE c.djlx=''100'' AND c.qllx=''23'' and c.bdcdyh=''{0}'' and a.sfdb=''0'' ", _bdcdyh);
										String sqlSealing = MessageFormat.format(" SELECT a.XMBH FROM bdck.bdcs_xmxx a  LEFT JOIN bdck.bdcs_ql_gz c ON a.xmbh=c.xmbh and a.sfdb=''0'' WHERE c.djlx=''800'' AND c.qllx=''99'' and c.bdcdyh=''{0}'' and a.sfdb=''0'' ", _bdcdyh);
										String sqlLimiting = "SELECT a.XMBH FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_dyxz c ON b.xmbh=c.xmbh and b.bdcdyid=c.bdcdyid WHERE c.yxbz='0' and c.bdcdyh='"+ _bdcdyh+ "' and a.sfdb='0' ";
//										long mcount = baseCommonDao.getCountByFullSql(sqlMortgage);
										long mcount=getDatalistCount(sqlMortgage);
										if(mcount >0)
										   mortgageCount += 1;
										else{
											
											
//											if(baseCommonDao.getCountByFullSql(sqlMortgaging)>0)
//												mortgagingCount+=1;
											if(getDatalistCount(sqlMortgaging)>0)
												mortgagingCount+=1;
										}
										
//										long scount = baseCommonDao.getCountByFullSql(sqlSeal);
										long scount = getDatalistCount(sqlSeal);
										if(scount >0)
										   SealCount += 1;
										else{
											
											
//											if(baseCommonDao.getCountByFullSql(sqlSealing)>0)
//												SealingCount+=1;
											if(getDatalistCount(sqlSealing)>0)
												SealingCount+=1;
										}
										
//										ObjectionCount= baseCommonDao.getCountByFullSql(sqlObjection);
										ObjectionCount=getDatalistCount(sqlObjection);
										if(ObjectionCount >0)
											ObjectionCount += 1;
										
//										long lcount = baseCommonDao.getCountByFullSql(sqlLimit);
										long lcount =getDatalistCount(sqlLimit);
										if(lcount >0)
											LimitCount += 1;
										else{
//											if(baseCommonDao.getCountByFullSql(sqlLimiting)>0)
//												LimitingCount+=1;
											if(getDatalistCount(sqlLimiting)>0)
												LimitingCount+=1;
									    }
									}
								}
							}
							String mortgageStatus =  MessageFormat.format("土地{0};地上房屋已抵押{1}起,正在抵押{2}起",map.get("DYZT"),mortgageCount,mortgagingCount);
							String sealStatus = MessageFormat.format("土地{0};地上	{1}起,正在查封{2}起",map.get("CFZT"),SealCount,SealingCount);
							String objectionStatus = MessageFormat.format("土地{0};地上房屋有异议{1}起",map.get("YYZT"),ObjectionCount);
							String LimitStatus = MessageFormat.format("土地{0};地上房屋有限制{1}起,正在限制{2}起",map.get("XZZT"),LimitCount,LimitingCount);
							map.put("DYZT", mortgageStatus);
							map.put("CFZT", sealStatus);
							map.put("YYZT", objectionStatus);
							map.put("XZZT", LimitStatus);							
						}	
					}
				}
				map.put("HOUSECOUNT",housecount);
			}
		}		
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void addLimitZDStausByFw(List<Map> result){

		if(result != null && result.size() > 0){
			for(Map map : result){
				long mortgageCount = 0,mortgagingCount=0;
				List<String> lstmortgage=new ArrayList<String>();
				List<String> lstmortgageing=new ArrayList<String>();
				long SealCount = 0,SealingCount=0;
				List<String> lstseal=new ArrayList<String>();
				List<String> lstsealing=new ArrayList<String>();
				long ObjectionCount = 0,ObjectioningCount=0;
				List<String> lstObjection=new ArrayList<String>();
				List<String> lstObjectioning=new ArrayList<String>();
				long LimitCount = 0,LimitingCount=0;
				long housecount = 0;
				//只有使用权宗地才有房屋
				if(map.containsKey("BDCDYID")&&map.get("BDCDYLX").equals("02")){
					String zdbdcdyid = (String) map.get("BDCDYID");
					if(zdbdcdyid != null ){
						//已办理的业务
						StringBuilder strdealed=new StringBuilder();
						strdealed.append("SELECT QL.QLLX,QL.DJLX,DY.BDCDYID FROM  ( ");
						strdealed.append("select BDCDYID,'031' BDCDYLX,ZDBDCDYID from BDCK.BDCS_H_XZ union  all  ");
						strdealed.append("select BDCDYID,'032' BDCDYLX,ZDBDCDYID from BDCK.BDCS_H_XZY ) DY   ");
						strdealed.append("LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON DJDY.BDCDYID=DY.BDCDYID AND DJDY.BDCDYLX=DY.BDCDYLX ");
						strdealed.append("LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=DJDY.DJDYID ");
						strdealed.append("WHERE QL.QLID IS NOT NULL AND DY.ZDBDCDYID='");
						strdealed.append(zdbdcdyid).append("'");
						String dealedSql=strdealed.toString();
						 List<Map> dealedmap=baseCommonDao.getDataListByFullSql(dealedSql);
						 if(!StringHelper.isEmpty(dealedmap) && dealedmap.size()>0){
							 for(Map m :dealedmap){
								String qllx= StringHelper.FormatByDatatype(m.get("QLLX"));
								String djlx=StringHelper.FormatByDatatype(m.get("DJLX"));
								String bdcdyid= StringHelper.FormatByDatatype(m.get("BDCDYID"));
								if(DJLX.CFDJ.Value.equals(djlx) && QLLX.QTQL.Value.equals(qllx)){
									if(!lstseal.contains(bdcdyid)){
										lstseal.add(bdcdyid);
										SealCount++;
									}
								}else if(DJLX.YYDJ.Value.equals(djlx)){
									if(!lstObjection.contains(bdcdyid)){
										lstObjection.add(bdcdyid);
										ObjectionCount++;
									}
									
								}else if(QLLX.DIYQ.Value.equals(qllx)){
									if(!lstmortgage.contains(bdcdyid)){
										lstmortgage.add(bdcdyid);
										mortgageCount++;
									}
									
								}
							 }
						 }
						//正在办理的业务
						StringBuilder strdealing=new StringBuilder();
						strdealing.append("SELECT QL.QLLX,QL.DJLX,DY.BDCDYID,XMXX.QLLX XMQLLX,XMXX.DJLX XMDJLX  FROM  ( ");
						strdealing.append("select BDCDYID,'031' BDCDYLX,ZDBDCDYID from BDCK.BDCS_H_XZ union  all  ");
						strdealing.append("select BDCDYID,'032' BDCDYLX,ZDBDCDYID from BDCK.BDCS_H_XZY ) DY   ");
						strdealing.append("LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON DJDY.BDCDYID=DY.BDCDYID AND DJDY.BDCDYLX=DY.BDCDYLX  ");
						strdealing.append("LEFT JOIN BDCK.BDCS_QL_GZ QL ON QL.DJDYID=DJDY.DJDYID ");
						strdealing.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON XMXX.XMBH=QL.XMBH  ");
						strdealing.append("WHERE (XMXX.SFDB IS NULL OR XMXX.SFDB<>'1') AND QL.QLID IS NOT NULL  ");
						strdealing.append("AND DY.ZDBDCDYID= '");
						strdealing.append(zdbdcdyid).append("'");
						String dealingsql=strdealing.toString();
						List<Map> dealingmap=baseCommonDao.getDataListByFullSql(dealingsql);
						 if(!StringHelper.isEmpty(dealingmap) && dealingmap.size()>0){
							 for(Map m :dealingmap){
								String qllx= StringHelper.FormatByDatatype(m.get("XMQLLX"));
								String djlx=StringHelper.FormatByDatatype(m.get("XMDJLX"));
								String bdcdyid= StringHelper.FormatByDatatype(m.get("BDCDYID"));
								if(DJLX.CFDJ.Value.equals(djlx) && QLLX.QTQL.Value.equals(qllx)){
									if(!lstseal.contains(bdcdyid) && !lstsealing.contains(bdcdyid)){
										SealingCount++;
										lstsealing.add(bdcdyid);
									}
								}else if(DJLX.YYDJ.Value.equals(djlx)){
									if(!lstObjection.contains(bdcdyid) && !lstObjectioning.contains(bdcdyid)){
										ObjectioningCount++;
										lstObjectioning.add(bdcdyid);
									}
								}else if(QLLX.DIYQ.Value.equals(qllx) && !DJLX.ZXDJ.Value.equals(djlx)){
									if(!lstmortgage.contains(bdcdyid) && !lstmortgageing.contains(bdcdyid)){
										mortgagingCount++;
										lstmortgageing.add(bdcdyid);
									}
								}
							 }
						 }
						//限制的业务
						StringBuilder strlimit=new StringBuilder();
						strlimit.append("SELECT DYXZ.YXBZ FROM (SELECT BDCDYID,ZDBDCDYID FROM BDCK.BDCS_H_XZ UNION ALL  ");
						strlimit.append("SELECT BDCDYID,ZDBDCDYID FROM BDCK.BDCS_H_XZY ) DY ");
						strlimit.append("LEFT JOIN BDCK.BDCS_DYXZ DYXZ ON DYXZ.BDCDYID=DY.BDCDYID ");
						strlimit.append("WHERE DYXZ.ID IS NOT NULL  ");
						strlimit.append(" AND DY.ZDBDCDYID='").append(zdbdcdyid).append("'");
						String limitsql=strlimit.toString();
						List<Map> limitmap = baseCommonDao.getDataListByFullSql(limitsql);
						//商品房的土地抵消状态赋值
						if(limitmap != null && limitmap.size() > 0){
							for(Map m :limitmap){
								String yxbz= StringHelper.FormatByDatatype(m.get("YXBZ"));
								if("1".equals(yxbz)){
									LimitCount++;
								}else{
									LimitingCount++;
								}
							}
							
						}
						String mortgageStatus =  MessageFormat.format("土地{0};地上房屋已抵押{1}起,正在抵押{2}起",map.get("DYZT"),mortgageCount,mortgagingCount);
						String sealStatus = MessageFormat.format("土地{0};地上房屋已查封{1}起,正在查封{2}起",map.get("CFZT"),SealCount,SealingCount);
						String objectionStatus = MessageFormat.format("土地{0};地上房屋有异议{1}起",map.get("YYZT"),ObjectionCount);
						String LimitStatus = MessageFormat.format("土地{0};地上房屋有限制{1}起,正在限制{2}起",map.get("XZZT"),LimitCount,LimitingCount);
						map.put("DYZT", mortgageStatus);
						map.put("CFZT", sealStatus);
						map.put("YYZT", objectionStatus);
						map.put("XZZT", LimitStatus);
//						System.out.println(mortgageStatus);
//						System.out.println(sealStatus);
//						System.out.println(objectionStatus);
//						System.out.println(LimitStatus);
					}
				}
				map.put("HOUSECOUNT",housecount);
			}
		}		
	}
	protected Long getDatalistCount(String sql){
		List<Map> counts=baseCommonDao.getPageDataByFullSql(sql, 1, 1);
		if(counts != null && counts.size()>0){
			return 1L;
		}else{
			return 0L;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void addZRZCount(List<Map> result){
		String zrzcount = "0";
		if(result != null && result.size() > 0){
			for(Map map : result){
				if(map.containsKey("BDCDYID")){
						String zdbdcdyid = (String) map.get("BDCDYID");
						if(zdbdcdyid != null ){
							String countsql = MessageFormat.format(" SELECT SUM(C) AS C FROM (SELECT COUNT(0) AS C FROM BDCK.BDCS_ZRZ_XZ WHERE ZDBDCDYID=''{0}'' UNION ALL SELECT COUNT(0) AS C FROM BDCK.BDCS_ZRZ_XZY WHERE ZDBDCDYID=''{0}'')", zdbdcdyid);
							List<Map> counts = baseCommonDao.getDataListByFullSql(countsql);
							zrzcount=String.valueOf(counts.get(0).get("C"));
			            }
				}
				map.put("ZRZCOUNT",zrzcount);
		    }
	    }
	}
	
	/**
	 * 土地的状态赋值
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void addLimitZDStatus(List<Map> result) {
		if (result != null && result.size() > 0) {
			for (Map map : result) {
				if (map.containsKey("DJDYID")) {
					String djdyid = (String) map.get("DJDYID");
					if (djdyid != null) {
						String sqlMortgage = MessageFormat
								.format(" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and qllx=''23''",
										djdyid);
						String sqlSeal = MessageFormat
								.format(" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''800'' and qllx=''99''",
										djdyid);
						String sqlObjection = MessageFormat
								.format("  from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''600'' ",
										djdyid);
						long mortgageCount = baseCommonDao
								.getCountByFullSql(sqlMortgage);
						long SealCount = baseCommonDao
								.getCountByFullSql(sqlSeal);
						long ObjectionCount = baseCommonDao
								.getCountByFullSql(sqlObjection);

						String mortgageStatus = mortgageCount > 0 ? "已抵押"
								: "无抵押";
						String sealStatus = SealCount > 0 ? "已查封" : "无查封";

						// 判断完现状层中的查封信息，接着判断办理中的查封信息
						if (!(SealCount > 0)) {
							String sqlSealing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and  b.djdyid=c.djdyid WHERE c.djlx='800' AND c.qllx='99' and c.djdyid='"
									+ djdyid + "' and a.sfdb='0' ";
							long count = baseCommonDao
									.getCountByFullSql(sqlSealing);
							sealStatus = count > 0 ? "查封办理中" : "无查封";
						}

						// 判断完现状层中的查封信息，接着判断办理中的查封信息
						if (!(mortgageCount > 0)) {
							String sqlmortgageing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='100' AND c.qllx='23' and c.djdyid='"
									+ djdyid + "' and a.sfdb='0' ";
							long count = baseCommonDao
									.getCountByFullSql(sqlmortgageing);
							mortgageStatus = count > 0 ? "抵押办理中" : "无抵押";
						}

						String objectionStatus = ObjectionCount > 0 ? "有异议"
								: "无异议";
						map.put("DYZT", mortgageStatus);
						map.put("CFZT", sealStatus);
						map.put("YYZT", objectionStatus);
						
						//增加限制状态
						String sqlLimit = MessageFormat.format("  from BDCK.BDCS_DYXZ where bdcdyid=''{0}'' and yxbz=''1'' ",
										map.get("BDCDYID"));

						long LimitCount = baseCommonDao.getCountByFullSql(sqlLimit);
						String LimitStatus = LimitCount > 0 ? map.get("BDCDYLXMC") + "已限制" : map.get("BDCDYLXMC") + "无限制";
						// 改为判断完查封 人后判断限制
						if (!(LimitCount > 0)) {
							String sqlLimiting = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_dyxz c ON b.xmbh=c.xmbh and b.bdcdyid=c.bdcdyid WHERE c.yxbz='0' and c.bdcdyid='"
									+ map.get("BDCDYID") + "' and a.sfdb='0' ";
							long countxz = baseCommonDao.getCountByFullSql(sqlLimiting);
							LimitStatus = countxz > 0 ? "限制办理中" : "无限制";
						}
						map.put("XZZT", LimitStatus);
					}
				}
			}
		}
	}

	public List<HashMap<String, String>> HouseStatusQuery_RelationID(
			String RelationID, String bdcdylx) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		List<RealUnit> list_unit = null;
		if (!StringHelper.isEmpty(RelationID)) {
			list_unit = UnitTools.loadUnits(BDCDYLX.initFrom(bdcdylx),
					DJDYLY.XZ, "RelationID='" + RelationID + "' or BDCDYH='"+RelationID + "'",
					new HashMap<String, String>());
		}
		if (list_unit == null || list_unit.size() <= 0) {
			HashMap<String, String> map = new HashMap<String, String>();
			// 初始化状态值，状态值说明；2代表未查询到单元，1代表有此状态，0代表无此状态，-1表示正在办理此状态
			map.put("MortgageStatus", "2");// 抵押状态
			map.put("NoticeStatus", "2");// 期房预告状态
			map.put("PreMortgageStatus", "2");// 期房抵押状态
			map.put("LimitStatus", "2");// 查封状态（期房或现房有一个呗查封，则有此状态）
			map.put("Handleing", "2");
			map.put("RelationID", RelationID);
			list.add(map);
			return list;
		}
		if (BDCDYLX.H.Value.equals(bdcdylx)) {
			for (RealUnit unit : list_unit) {
				HashMap<String, String> map = getStatus_SCH_Mapping(unit);
				list.add(map);
			}
		} else {
			for (RealUnit unit : list_unit) {
				HashMap<String, String> map = getStatus_YCH_Mapping(unit);
				list.add(map);
			}
		}
		return list;
	}
	
	public List<HashMap<String, String>> HouseStatusQuery_BDCDYID(String bdcdyid, String bdcdylx) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		List<RealUnit> list_unit = null;
		if (!StringHelper.isEmpty(bdcdyid)) {
			list_unit = UnitTools.loadUnits(BDCDYLX.initFrom(bdcdylx), DJDYLY.XZ, "BDCDYID='" + bdcdyid + "'", new HashMap<String, String>());
		}
		if (list_unit == null || list_unit.size() <= 0) {
			HashMap<String, String> map = new HashMap<String, String>();
			// 初始化状态值，状态值说明；2代表未查询到单元，1代表有此状态，0代表无此状态，-1表示正在办理此状态
			map.put("MortgageStatus", "2");// 抵押状态
			map.put("NoticeStatus", "2");// 期房预告状态
			map.put("PreMortgageStatus", "2");// 期房抵押状态
			map.put("LimitStatus", "2");// 查封状态（期房或现房有一个呗查封，则有此状态）
			map.put("Handleing", "2");
			map.put("RelationID", "");
			map.put("bdcdyid", bdcdyid);
			list.add(map);
			return list;
		}
		if (BDCDYLX.H.Value.equals(bdcdylx)) {
			for (RealUnit unit : list_unit) {
				HashMap<String, String> map = getStatus_SCH_Mapping(unit);
				list.add(map);
			}
		} else {
			for (RealUnit unit : list_unit) {
				HashMap<String, String> map = getStatus_YCH_Mapping(unit);
				list.add(map);
			}
		}
		return list;
	}

	protected HashMap<String, String> getStatus_SCH_Mapping(RealUnit unit) {
		HashMap<String, String> map = new HashMap<String, String>();
		String MortgageStatus = "0";
		String NoticeStatus = "0";
		String PreMortgageStatus = "0";
		String LimitStatus = "0";
		String bdcdyid = unit.getId();
		String RelationID = unit.getRELATIONID();
		String Handleing = "0";
		if (true) {
			HashMap<String, String> Status_map = GetStatus_SCH(bdcdyid);
			if (Status_map != null) {
				if (Status_map.containsKey("CFZT")) {
					String CFZT = Status_map.get("CFZT").toString();
					if (LimitStatus.equals("0") || LimitStatus.equals("2")) {
						LimitStatus = CFZT;
					} else if (CFZT.equals("1")) {
						if (!LimitStatus.contains(CFZT)) {
							LimitStatus = CFZT + "、" + LimitStatus;
						}
					}
				}
				if (Status_map.containsKey("DYZT")) {
					String DYZT = Status_map.get("DYZT").toString();
					if (MortgageStatus.equals("0")
							|| MortgageStatus.equals("2")) {
						MortgageStatus = DYZT;
					} else if (DYZT.equals("1")) {
						if (!MortgageStatus.contains(DYZT)) {
							MortgageStatus = DYZT + "、" + MortgageStatus;
						}
					}
				}
				if (Status_map.containsKey("CFZT_InProcess")) {
					String CFZT = Status_map.get("CFZT_InProcess").toString();
					if (LimitStatus.equals("0") || LimitStatus.equals("2")) {
						if (LimitStatus.equals("1")) {
							LimitStatus = "-1";
						} else {
							LimitStatus = CFZT;
						}
					} else if (CFZT.equals("1")) {
						if (!LimitStatus.contains("-1")) {
							LimitStatus = LimitStatus + "、" + "-1";
						}
					}
				}
				if (Status_map.containsKey("DYZT_InProcess")) {
					String DYZT = Status_map.get("DYZT_InProcess").toString();
					if (MortgageStatus.equals("0")
							|| MortgageStatus.equals("2")) {
						if (DYZT.equals("1")) {
							MortgageStatus = "-1";
						} else {
							MortgageStatus = DYZT;
						}
					} else if (DYZT.equals("1")) {
						if (!MortgageStatus.contains("-1")) {
							MortgageStatus = MortgageStatus + "、" + "-1";
						}
					}
				}
			}
			HashMap<String, String> Handleing_map = GetHandleing(bdcdyid);
			if (Handleing_map.containsKey("HANDLEING")) {
				String HANDLEING = Handleing_map.get("HANDLEING").toString();
				if (HANDLEING.equals("1")) {
					String DJLXandQLLX = Handleing_map.get("DJLXandQLLX").toString();
					Handleing=DJLXandQLLX;
				} 
			}
		}

		List<YC_SC_H_XZ> list_gx = baseCommonDao.getDataList(YC_SC_H_XZ.class,
				"SCBDCDYID='" + bdcdyid + "'");
		if (list_gx != null && list_gx.size() > 0) {
			for (YC_SC_H_XZ gx : list_gx) {
				String ycbdcdyid = gx.getYCBDCDYID();
				if (StringHelper.isEmpty(ycbdcdyid)) {
					continue;
				}
				HashMap<String, String> Status_map = GetStatus_YCH(ycbdcdyid);
				if (Status_map != null) {
					if (Status_map.containsKey("CFZT")) {
						String CFZT = Status_map.get("CFZT").toString();
						if (LimitStatus.equals("0") || LimitStatus.equals("2")) {
							LimitStatus = CFZT;
						} else if (CFZT.equals("1")) {
							if (!LimitStatus.contains(CFZT)) {
								LimitStatus = CFZT + "、" + LimitStatus;
							}
						}
					}
					if (Status_map.containsKey("DYZT")) {
						String DYZT = Status_map.get("DYZT").toString();
						if (PreMortgageStatus.equals("0")
								|| PreMortgageStatus.equals("2")) {
							PreMortgageStatus = DYZT;
						} else if (DYZT.equals("1")) {
							if (!PreMortgageStatus.contains(DYZT)) {
								PreMortgageStatus = DYZT + "、"
										+ PreMortgageStatus;
							}
						}
					}
					if (Status_map.containsKey("YGZT")) {
						String YGZT = Status_map.get("YGZT").toString();
						if (NoticeStatus.equals("0")
								|| NoticeStatus.equals("2")) {
							NoticeStatus = YGZT;
						} else if (YGZT.equals("1")) {
							if (!NoticeStatus.contains(YGZT)) {
								NoticeStatus = YGZT + "、" + NoticeStatus;
							}
						}
					}
					if (Status_map.containsKey("CFZT_InProcess")) {
						String CFZT = Status_map.get("CFZT_InProcess")
								.toString();
						if (LimitStatus.equals("0") || LimitStatus.equals("2")) {
							if (CFZT.equals("1")) {
								LimitStatus = "-1";
							} else {
								LimitStatus = CFZT;
							}
						} else if (CFZT.equals("1")) {
							if (!LimitStatus.contains("-1")) {
								LimitStatus = LimitStatus + "、" + "-1";
							}
						}
					}
					if (Status_map.containsKey("DYZT_InProcess")) {
						String DYZT = Status_map.get("DYZT_InProcess")
								.toString();
						if (PreMortgageStatus.equals("0")
								|| PreMortgageStatus.equals("2")) {
							if (DYZT.equals("1")) {
								PreMortgageStatus = "-1";
							} else {
								PreMortgageStatus = DYZT;
							}
						} else if (DYZT.equals("1")) {
							if (!PreMortgageStatus.contains("-1")) {
								PreMortgageStatus = PreMortgageStatus + "、"
										+ "-1";
							}
						}
					}

					if (Status_map.containsKey("YGZT_InProcess")) {
						String YGZT = Status_map.get("YGZT_InProcess")
								.toString();
						if (NoticeStatus.equals("0")
								|| NoticeStatus.equals("2")) {
							if (YGZT.equals("1")) {
								NoticeStatus = "-1";
							} else {
								NoticeStatus = YGZT;
							}
						} else if (YGZT.equals("1")) {
							if (!NoticeStatus.contains("-1")) {
								NoticeStatus = NoticeStatus + "、" + "-1";
							}
						}
					}
				}
			}
		}
		map.put("MortgageStatus", MortgageStatus);
		map.put("NoticeStatus", NoticeStatus);
		map.put("PreMortgageStatus", PreMortgageStatus);
		map.put("LimitStatus", LimitStatus);
		map.put("RelationID", RelationID);
		map.put("Handleing", Handleing);
		return map;
	}

	@SuppressWarnings("rawtypes")
	protected HashMap<String, String> GetHandleing(String bdcdyid) {
		HashMap<String, String> Handleing_map = new HashMap<String, String>();
		// 单元当前状态
		StringBuilder builder_status = new StringBuilder();
		builder_status.append("SELECT QL.DJLX,QL.QLLX ");
		builder_status.append("FROM BDCK.BDCS_DJDY_GZ DJDY  JOIN BDCK.BDCS_QL_GZ QL ON DJDY.DJDYID=QL.DJDYID AND DJDY.XMBH=QL.XMBH JOIN BDCK.BDCS_XMXX XMXX ON QL.XMBH=XMXX.XMBH WHERE  NVL2(XMXX.SFDB,XMXX.SFDB,'0')<>'1' AND  DJDY.BDCDYID='");
		builder_status.append(bdcdyid).append("'");
		List<Map> list_result = baseCommonDao
				.getDataListByFullSql(builder_status.toString());
		if (list_result != null && list_result.size() > 0) {
			Handleing_map.put("HANDLEING", "1");
			String DJLXandQLLX = null;
			for(int i=0;i<list_result.size();i++){
				Map m = list_result.get(i);
				if (m.containsKey("DJLX")&&m.containsKey("QLLX")) {
					if(i==0){
						String DJLX = m.get("DJLX").toString();
						String QLLX = m.get("QLLX").toString();
						DJLXandQLLX=DJLX+QLLX;
					}else{
						String DJLX = m.get("DJLX").toString();
						String QLLX = m.get("QLLX").toString();
						DJLXandQLLX=DJLXandQLLX+"+"+DJLX+QLLX;
					}
				}
			}
			Handleing_map.put("DJLXandQLLX", DJLXandQLLX);
		}
		return Handleing_map;
	}

	//泸州获取是否在办节点要返回project_id
	@SuppressWarnings("rawtypes")
	protected HashMap<String, String> GetHandleingProject_id(String bdcdyid) {
		HashMap<String, String> Handleing_map = new HashMap<String, String>();
		// 单元当前状态
		StringBuilder builder_status = new StringBuilder();
		builder_status.append("SELECT XMXX.PROJECT_ID ");
		builder_status.append("FROM BDCK.BDCS_DJDY_GZ DJDY  JOIN BDCK.BDCS_QL_GZ QL ON DJDY.DJDYID=QL.DJDYID AND DJDY.XMBH=QL.XMBH JOIN BDCK.BDCS_XMXX XMXX ON QL.XMBH=XMXX.XMBH WHERE  NVL2(XMXX.SFDB,XMXX.SFDB,'0')<>'1' AND  DJDY.BDCDYID='");
		builder_status.append(bdcdyid).append("'");
		List<Map> list_result = baseCommonDao
				.getDataListByFullSql(builder_status.toString());
		if (list_result != null && list_result.size() > 0) {
			Handleing_map.put("HANDLEING", "1");
			String PROJECT_ID = null;
			Map m = list_result.get(0);
			if (m.containsKey("PROJECT_ID")) {
				PROJECT_ID = m.get("PROJECT_ID").toString();
			}
			Handleing_map.put("PROJECT_ID", PROJECT_ID);
		}
		return Handleing_map;
	}
	
	protected HashMap<String, String> getStatus_YCH_Mapping(RealUnit unit) {
		HashMap<String, String> map = new HashMap<String, String>();
		String MortgageStatus = "0";
		String NoticeStatus = "0";
		String PreMortgageStatus = "0";
		String LimitStatus = "0";
		String bdcdyid = unit.getId();
		String RelationID = unit.getRELATIONID();
		String Handleing = "0";
		if (true) {
			HashMap<String, String> Status_map = GetStatus_YCH(bdcdyid);
			if (Status_map != null) {
				if (Status_map.containsKey("CFZT")) {
					String CFZT = Status_map.get("CFZT").toString();
					if (LimitStatus.equals("0") || LimitStatus.equals("2")) {
						LimitStatus = CFZT;
					} else if (CFZT.equals("1")) {
						if (!LimitStatus.contains(CFZT)) {
							LimitStatus = CFZT + "、" + LimitStatus;
						}
					}
				}
				if (Status_map.containsKey("DYZT")) {
					String DYZT = Status_map.get("DYZT").toString();
					if (PreMortgageStatus.equals("0")
							|| PreMortgageStatus.equals("2")) {
						PreMortgageStatus = DYZT;
					} else if (DYZT.equals("1")) {
						if (!PreMortgageStatus.contains(DYZT)) {
							PreMortgageStatus = DYZT + "、" + PreMortgageStatus;
						}
					}
				}
				if (Status_map.containsKey("YGZT")) {
					String YGZT = Status_map.get("YGZT").toString();
					if (NoticeStatus.equals("0") || NoticeStatus.equals("2")) {
						NoticeStatus = YGZT;
					} else if (YGZT.equals("1")) {
						if (!NoticeStatus.contains(YGZT)) {
							NoticeStatus = YGZT + "、" + NoticeStatus;
						}
					}
				}
				if (Status_map.containsKey("CFZT_InProcess")) {
					String CFZT = Status_map.get("CFZT_InProcess").toString();
					if (LimitStatus.equals("0") || LimitStatus.equals("2")) {
						if (CFZT.equals("1")) {
							LimitStatus = "-1";
						} else {
							LimitStatus = CFZT;
						}
					} else if (CFZT.equals("1")) {
						if (!LimitStatus.contains("-1")) {
							LimitStatus = LimitStatus + "、" + "-1";
						}
					}
				}
				if (Status_map.containsKey("DYZT_InProcess")) {
					String DYZT = Status_map.get("DYZT_InProcess").toString();
					if (PreMortgageStatus.equals("0")
							|| PreMortgageStatus.equals("2")) {
						if (DYZT.equals("1")) {
							PreMortgageStatus = "-1";
						} else {
							PreMortgageStatus = DYZT;
						}
					} else if (DYZT.equals("1")) {
						if (!PreMortgageStatus.contains("-1")) {
							PreMortgageStatus = PreMortgageStatus + "、" + "-1";
						}
					}
				}

				if (Status_map.containsKey("YGZT_InProcess")) {
					String YGZT = Status_map.get("YGZT_InProcess").toString();
					if (NoticeStatus.equals("0") || NoticeStatus.equals("2")) {
						if (YGZT.equals("1")) {
							NoticeStatus = "-1";
						} else {
							NoticeStatus = YGZT;
						}
					} else if (YGZT.equals("1")) {
						if (!NoticeStatus.contains("-1")) {
							NoticeStatus = NoticeStatus + "、" + "-1";
						}
					}
				}
			}
			HashMap<String, String> Handleing_map = GetHandleing(bdcdyid);
			if (Handleing_map.containsKey("HANDLEING")) {
				String HANDLEING = Handleing_map.get("HANDLEING").toString();
				if (HANDLEING.equals("1")) {
					String DJLXandQLLX = Handleing_map.get("DJLXandQLLX").toString();
					Handleing=DJLXandQLLX;
				} 
			}
		}

		List<YC_SC_H_XZ> list_gx = baseCommonDao.getDataList(YC_SC_H_XZ.class,
				"YCBDCDYID='" + bdcdyid + "'");
		if (list_gx != null && list_gx.size() > 0) {
			for (YC_SC_H_XZ gx : list_gx) {
				String scbdcdyid = gx.getSCBDCDYID();
				if (StringHelper.isEmpty(scbdcdyid)) {
					continue;
				}
				HashMap<String, String> Status_map = GetStatus_SCH(scbdcdyid);
				if (Status_map != null) {
					if (Status_map.containsKey("CFZT")) {
						String CFZT = Status_map.get("CFZT").toString();
						if (LimitStatus.equals("0") || LimitStatus.equals("2")) {
							LimitStatus = CFZT;
						} else if (CFZT.equals("1")) {
							if (!LimitStatus.contains(CFZT)) {
								LimitStatus = CFZT + "、" + LimitStatus;
							}
						}
					}
					if (Status_map.containsKey("DYZT")) {
						String DYZT = Status_map.get("DYZT").toString();
						if (MortgageStatus.equals("0")
								|| MortgageStatus.equals("2")) {
							MortgageStatus = DYZT;
						} else if (DYZT.equals("1")) {
							if (!MortgageStatus.contains(DYZT)) {
								MortgageStatus = DYZT + "、" + MortgageStatus;
							}
						}
					}
					if (Status_map.containsKey("CFZT_InProcess")) {
						String CFZT = Status_map.get("CFZT_InProcess")
								.toString();
						if (LimitStatus.equals("0") || LimitStatus.equals("2")) {
							if (LimitStatus.equals("1")) {
								LimitStatus = "-1";
							} else {
								LimitStatus = CFZT;
							}
						} else if (CFZT.equals("1")) {
							if (!LimitStatus.contains("-1")) {
								LimitStatus = LimitStatus + "、" + "-1";
							}
						}
					}
					if (Status_map.containsKey("DYZT_InProcess")) {
						String DYZT = Status_map.get("DYZT_InProcess")
								.toString();
						if (MortgageStatus.equals("0")
								|| MortgageStatus.equals("2")) {
							if (DYZT.equals("1")) {
								MortgageStatus = "-1";
							} else {
								MortgageStatus = DYZT;
							}
						} else if (DYZT.equals("1")) {
							if (!MortgageStatus.contains("-1")) {
								MortgageStatus = MortgageStatus + "、" + "-1";
							}
						}
					}
				}
			}
		}
		map.put("MortgageStatus", MortgageStatus);
		map.put("NoticeStatus", NoticeStatus);
		map.put("PreMortgageStatus", PreMortgageStatus);
		map.put("LimitStatus", LimitStatus);
		map.put("RelationID", RelationID);
		map.put("Handleing", Handleing);
		return map;
	}

	@SuppressWarnings("rawtypes")
	protected HashMap<String, String> GetStatus_SCH(String bdcdyid) {
		HashMap<String, String> Status_map = new HashMap<String, String>();
		// 单元当前状态
		StringBuilder builder_status = new StringBuilder();
		builder_status.append("SELECT DY.BDCDYID,DY.RELATIONID,");
		builder_status.append("CASE WHEN EXISTS(");
		builder_status.append("SELECT 1 FROM BDCK.BDCS_DJDY_XZ DJDY ");
		builder_status
				.append("LEFT JOIN BDCK.BDCS_QL_XZ QL ON DJDY.DJDYID=QL.DJDYID ");
		builder_status
				.append("WHERE QL.DJLX='800' AND DJDY.BDCDYID=DY.BDCDYID");
		builder_status.append(") THEN '1' ELSE '0' END AS CFZT,");
		
		builder_status.append("CASE WHEN EXISTS(");
		builder_status.append("SELECT 1 FROM BDCK.BDCS_DYXZ DYXZ WHERE DYXZ.BDCDYID=DY.BDCDYID  and (YXBZ='0' OR YXBZ='1')");
		builder_status.append(") THEN '1' ELSE '0' END AS XZZT,");
		
		builder_status.append("CASE WHEN EXISTS(");
		builder_status.append("SELECT 1 FROM BDCK.BDCS_DJDY_XZ DJDY ");
		builder_status
				.append("LEFT JOIN BDCK.BDCS_QL_XZ QL ON DJDY.DJDYID=QL.DJDYID ");
		builder_status
				.append("WHERE QL.QLLX='23' AND QL.DJLX<>'700' AND DJDY.BDCDYID=DY.BDCDYID");
		builder_status.append(") THEN '1' ELSE '0' END AS DYZT ");
		builder_status.append("FROM BDCK.BDCS_H_XZ DY WHERE DY.BDCDYID='");
		builder_status.append(bdcdyid).append("'");	
		List<Map> list_result = baseCommonDao
				.getDataListByFullSql(builder_status.toString());
		if (list_result != null && list_result.size() > 0) {
			Map m = list_result.get(0);
			if (m.containsKey("CFZT")) {
				String CFZT = m.get("CFZT").toString();
				Status_map.put("CFZT", CFZT);
			}
			//如果有现在登记
			if (m.containsKey("XZZT")) {
				String XZZT = m.get("XZZT").toString();
				if("1".equals(XZZT)){
					if(Status_map.containsKey("CFZT")){
						Status_map.remove("CFZT");
					}
					Status_map.put("CFZT", XZZT);
				}
			}
			
			if (m.containsKey("DYZT")) {
				String DYZT = m.get("DYZT").toString();
				Status_map.put("DYZT", DYZT);
			}
		}

		// 单元正在办理状态
		StringBuilder builder_status_InProcess = new StringBuilder();	
		builder_status_InProcess.append("SELECT DY.BDCDYID,DY.RELATIONID,");
		builder_status_InProcess.append("CASE WHEN EXISTS(");
		builder_status_InProcess
				.append("SELECT 1 FROM BDCK.BDCS_DJDY_GZ DJDY ");
		builder_status_InProcess
				.append("LEFT JOIN BDCK.BDCS_QL_GZ QL ON DJDY.DJDYID=QL.DJDYID AND DJDY.XMBH=QL.XMBH ");
		builder_status_InProcess
				.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON QL.XMBH=XMXX.XMBH ");
		builder_status_InProcess
				.append("WHERE QL.DJLX='800' AND NVL2(XMXX.SFDB,XMXX.SFDB,'0')<>'1' AND XMXX.QLLX='98' AND DJDY.BDCDYID=DY.BDCDYID");
		builder_status_InProcess.append(") THEN '1' ELSE '0' END AS CFZT,");
		builder_status_InProcess.append("CASE WHEN EXISTS(");
		builder_status_InProcess
				.append("SELECT 1 FROM BDCK.BDCS_DJDY_GZ DJDY ");
		builder_status_InProcess
				.append("LEFT JOIN BDCK.BDCS_QL_GZ QL ON DJDY.DJDYID=QL.DJDYID AND DJDY.XMBH=QL.XMBH ");
		builder_status_InProcess
				.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON QL.XMBH=XMXX.XMBH ");
		builder_status_InProcess
				.append("WHERE QL.QLLX='23' AND QL.DJLX<>'700' AND NVL2(XMXX.SFDB,XMXX.SFDB,'0')<>'1' AND XMXX.DJLX<>'400' AND DJDY.BDCDYID=DY.BDCDYID");
		builder_status_InProcess.append(") THEN '1' ELSE '0' END AS DYZT ");
		builder_status_InProcess
				.append("FROM BDCK.BDCS_H_XZ DY WHERE DY.BDCDYID='");
		builder_status_InProcess.append(bdcdyid).append("'");
		List<Map> list_result_InProcess = baseCommonDao
				.getDataListByFullSql(builder_status_InProcess.toString());
		if (list_result_InProcess != null && list_result_InProcess.size() > 0) {
			Map m = list_result_InProcess.get(0);
			if (m.containsKey("CFZT")) {
				String CFZT = m.get("CFZT").toString();
				Status_map.put("CFZT_InProcess", CFZT);
			}
			if (m.containsKey("DYZT")) {
				String DYZT = m.get("DYZT").toString();
				Status_map.put("DYZT_InProcess", DYZT);
			}
		}
		return Status_map;
	}
	
	@SuppressWarnings("rawtypes")
	protected HashMap<String, String> GetStatus_YCH(String bdcdyid) {
		HashMap<String, String> Status_map = new HashMap<String, String>();
		// 单元当前状态
		StringBuilder builder_status = new StringBuilder();
		builder_status.append("SELECT DY.BDCDYID,DY.RELATIONID,");
		builder_status.append("CASE WHEN EXISTS(");
		builder_status.append("SELECT 1 FROM BDCK.BDCS_DJDY_XZ DJDY ");
		builder_status
				.append("LEFT JOIN BDCK.BDCS_QL_XZ QL ON DJDY.DJDYID=QL.DJDYID ");
		builder_status
				.append("WHERE QL.DJLX='800' AND DJDY.BDCDYID=DY.BDCDYID");
		builder_status.append(") THEN '1' ELSE '0' END AS CFZT,");
		
		builder_status.append("CASE WHEN EXISTS(");
		builder_status.append("SELECT 1 FROM BDCK.BDCS_DYXZ DYXZ WHERE DYXZ.BDCDYID=DY.BDCDYID and (YXBZ='0' OR YXBZ='1')");
		builder_status.append(") THEN '1' ELSE '0' END AS XZZT,");
		
		builder_status.append("CASE WHEN EXISTS(");
		builder_status.append("SELECT 1 FROM BDCK.BDCS_DJDY_XZ DJDY ");
		builder_status
				.append("LEFT JOIN BDCK.BDCS_QL_XZ QL ON DJDY.DJDYID=QL.DJDYID ");
		builder_status.append("WHERE QL.QLLX='23' AND DJDY.BDCDYID=DY.BDCDYID");
		builder_status.append(") THEN '1' ELSE '0' END AS DYZT,");
		builder_status.append("CASE WHEN EXISTS(");
		builder_status.append("SELECT 1 FROM BDCK.BDCS_DJDY_XZ DJDY ");
		builder_status
				.append("LEFT JOIN BDCK.BDCS_QL_XZ QL ON DJDY.DJDYID=QL.DJDYID ");
		builder_status
				.append("WHERE QL.QLLX IN ('4','6','8') AND DJDY.BDCDYID=DY.BDCDYID");
		builder_status.append(") THEN '1' ELSE '0' END AS YGZT ");
		builder_status.append("FROM BDCK.BDCS_H_XZY DY WHERE DY.BDCDYID='");
		builder_status.append(bdcdyid).append("'");
		List<Map> list_result = baseCommonDao
				.getDataListByFullSql(builder_status.toString());
		if (list_result != null && list_result.size() > 0) {
			Map m = list_result.get(0);
			if (m.containsKey("CFZT")) {
				String CFZT = m.get("CFZT").toString();
				Status_map.put("CFZT", CFZT);
			}
			//如果有现在登记
			if (m.containsKey("XZZT")) {
				String XZZT = m.get("XZZT").toString();
				if("1".equals(XZZT)){
					if(Status_map.containsKey("CFZT")){
						Status_map.remove("CFZT");
					}
					Status_map.put("CFZT", XZZT);
				}
			}
			if (m.containsKey("DYZT")) {
				String DYZT = m.get("DYZT").toString();
				Status_map.put("DYZT", DYZT);
			}
			if (m.containsKey("YGZT")) {
				String DYZT = m.get("YGZT").toString();
				Status_map.put("YGZT", DYZT);
			}
		}

		// 单元正在办理状态
		StringBuilder builder_status_InProcess = new StringBuilder();
		builder_status_InProcess.append("SELECT DY.BDCDYID,DY.RELATIONID,");
		builder_status_InProcess.append("CASE WHEN EXISTS(");
		builder_status_InProcess
				.append("SELECT 1 FROM BDCK.BDCS_DJDY_GZ DJDY ");
		builder_status_InProcess
				.append("LEFT JOIN BDCK.BDCS_QL_GZ QL ON DJDY.DJDYID=QL.DJDYID AND DJDY.XMBH=QL.XMBH ");
		builder_status_InProcess
				.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON QL.XMBH=XMXX.XMBH ");
		builder_status_InProcess
				.append("WHERE QL.DJLX='800' AND NVL2(XMXX.SFDB,XMXX.SFDB,'0')<>'1' AND XMXX.QLLX='98' AND DJDY.BDCDYID=DY.BDCDYID");
		builder_status_InProcess.append(") THEN '1' ELSE '0' END AS CFZT,");
		builder_status_InProcess.append("CASE WHEN EXISTS(");
		builder_status_InProcess
				.append("SELECT 1 FROM BDCK.BDCS_DJDY_GZ DJDY ");
		builder_status_InProcess
				.append("LEFT JOIN BDCK.BDCS_QL_GZ QL ON DJDY.DJDYID=QL.DJDYID AND DJDY.XMBH=QL.XMBH ");
		builder_status_InProcess
				.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON QL.XMBH=XMXX.XMBH ");
		builder_status_InProcess
				.append("WHERE QL.QLLX='23' AND NVL2(XMXX.SFDB,XMXX.SFDB,'0')<>'1' AND XMXX.DJLX<>'400' AND DJDY.BDCDYID=DY.BDCDYID");
		builder_status_InProcess.append(") THEN '1' ELSE '0' END AS DYZT,");
		builder_status_InProcess.append("CASE WHEN EXISTS(");
		builder_status_InProcess
				.append("SELECT 1 FROM BDCK.BDCS_DJDY_GZ DJDY ");
		builder_status_InProcess
				.append("LEFT JOIN BDCK.BDCS_QL_GZ QL ON DJDY.DJDYID=QL.DJDYID AND DJDY.XMBH=QL.XMBH ");
		builder_status_InProcess
				.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON QL.XMBH=XMXX.XMBH ");
		builder_status_InProcess
				.append("WHERE QL.QLLX IN ('4','6','8') AND NVL2(XMXX.SFDB,XMXX.SFDB,'0')<>'1' AND XMXX.DJLX<>'400' AND DJDY.BDCDYID=DY.BDCDYID");
		builder_status_InProcess.append(") THEN '1' ELSE '0' END AS YGZT ");
		builder_status_InProcess
				.append("FROM BDCK.BDCS_H_XZY DY WHERE DY.BDCDYID='");
		builder_status_InProcess.append(bdcdyid).append("'");
		List<Map> list_result_InProcess = baseCommonDao
				.getDataListByFullSql(builder_status_InProcess.toString());
		if (list_result_InProcess != null && list_result_InProcess.size() > 0) {
			Map m = list_result_InProcess.get(0);
			if (m.containsKey("CFZT")) {
				String CFZT = m.get("CFZT").toString();
				Status_map.put("CFZT_InProcess", CFZT);
			}
			if (m.containsKey("DYZT")) {
				String DYZT = m.get("DYZT").toString();
				Status_map.put("DYZT_InProcess", DYZT);
			}
			if (m.containsKey("YGZT")) {
				String YGZT = m.get("YGZT").toString();
				Status_map.put("YGZT_InProcess", YGZT);
			}
		}
		return Status_map;
	}

	@Override
	public List<HashMap<String, String>> HouseStatusQuery_ZL(String zl,
			String bdcdylx) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		List<RealUnit> list_unit = null;
		if (!StringHelper.isEmpty(zl)) {
			list_unit = UnitTools
					.loadUnits(BDCDYLX.initFrom(bdcdylx), DJDYLY.XZ, "ZL='"
							+ zl + "'", new HashMap<String, String>());
		}
		if (list_unit == null || list_unit.size() <= 0) {
			HashMap<String, String> map = new HashMap<String, String>();
			// 初始化状态值，状态值说明；2代表未查询到单元，1代表有此状态，0代表无此状态，-1表示正在办理此状态
			map.put("MortgageStatus", "2");// 抵押状态
			map.put("NoticeStatus", "2");// 期房预告状态
			map.put("PreMortgageStatus", "2");// 期房抵押状态
			map.put("LimitStatus", "2");// 查封状态（期房或现房有一个呗查封，则有此状态）
			map.put("Handleing", "2");
			map.put("RelationID", "");
			list.add(map);
			return list;
		}
		if (BDCDYLX.H.Value.equals(bdcdylx)) {
			for (RealUnit unit : list_unit) {
				HashMap<String, String> map = getStatus_SCH_Mapping(unit);
				list.add(map);
			}
		} else {
			for (RealUnit unit : list_unit) {
				HashMap<String, String> map = getStatus_YCH_Mapping(unit);
				list.add(map);
			}
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<HashMap<String, String>> HouseStatusQuery_QZH(String qzh,
			String bdcdylx) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		List<RealUnit> list_unit = null;
		if (!StringHelper.isEmpty(qzh)) {
			StringBuilder builder_dy = new StringBuilder();
			builder_dy
					.append("SELECT DJDY.BDCDYID FROM BDCK.BDCS_DJDY_XZ DJDY ");
			builder_dy
					.append("LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=DJDY.DJDYID ");
			builder_dy
					.append("LEFT JOIN BDCK.BDCS_QLR_XZ QLR ON QLR.QLID=QL.QLID ");
			builder_dy.append("WHERE DJDY.BDCDYLX='");
			builder_dy.append(bdcdylx);
			builder_dy.append("' AND QLR.BDCQZH='");
			builder_dy.append(qzh).append("'");
			List<Map> list_bdcdyid = baseCommonDao
					.getDataListByFullSql(builder_dy.toString());
			if (list_bdcdyid != null && list_bdcdyid.size() > 0) {
				list_unit = new ArrayList<RealUnit>();
				for (Map m : list_bdcdyid) {
					if (m.containsKey("BDCDYID")) {
						String bdcdyid = m.get("BDCDYID").toString();
						if (!StringHelper.isEmpty(bdcdyid)) {
							RealUnit unit = UnitTools.loadUnit(
									BDCDYLX.initFrom(bdcdylx), DJDYLY.XZ,
									bdcdyid);
							if (unit != null) {
								if (!list_unit.contains(unit)) {
									list_unit.add(unit);
								}
							}
						}
					}
				}
			}
		}
		if (list_unit == null || list_unit.size() <= 0) {
			HashMap<String, String> map = new HashMap<String, String>();
			// 初始化状态值，状态值说明；2代表未查询到单元，1代表有此状态，0代表无此状态，-1表示正在办理此状态
			map.put("MortgageStatus", "2");// 抵押状态
			map.put("NoticeStatus", "2");// 期房预告状态
			map.put("PreMortgageStatus", "2");// 期房抵押状态
			map.put("LimitStatus", "2");// 查封状态（期房或现房有一个呗查封，则有此状态）
			map.put("Handleing", "2");
			map.put("RelationID", "");
			list.add(map);
			return list;
		}
		if (BDCDYLX.H.Value.equals(bdcdylx)) {
			for (RealUnit unit : list_unit) {
				HashMap<String, String> map = getStatus_SCH_Mapping(unit);
				list.add(map);
			}
		} else {
			for (RealUnit unit : list_unit) {
				HashMap<String, String> map = getStatus_YCH_Mapping(unit);
				list.add(map);
			}
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<HashMap<String, String>> HouseStatusQuery_ALL(String zl,
			String qzh, String qlrmc, String bdcdylx) {
		double startTime = System.currentTimeMillis();
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		List<RealUnit> list_unit = null;
		HashMap<String, String> djdy_map = new HashMap<String, String>();
		if (!StringHelper.isEmpty(qzh)) {
			StringBuilder builder_dy = new StringBuilder();
			String tableName = "BDCS_H_XZ";
			if ("032".equals(bdcdylx)) {
				tableName = "BDCS_H_XZY";
			}
			builder_dy.append("SELECT DY.BDCDYID,DJDY.DJDYID FROM BDCK."
					+ tableName + " DY ");
			builder_dy
					.append("LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON DJDY.BDCDYID=DY.BDCDYID ");
			builder_dy
					.append("LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=DJDY.DJDYID ");
			builder_dy
					.append("LEFT JOIN BDCK.BDCS_QLR_XZ QLR ON QLR.QLID=QL.QLID ");
			builder_dy.append("WHERE DJDY.BDCDYLX='");
			builder_dy.append(bdcdylx);
			builder_dy.append("' AND DY.ZL='");
			builder_dy.append(zl);
			builder_dy.append("' AND QLR.BDCQZH='");
			builder_dy.append(qzh).append("'");
			List<Map> list_bdcdyid = baseCommonDao
					.getDataListByFullSql(builder_dy.toString());
			System.out.println("测试时间1:"
					+ (System.currentTimeMillis() - startTime));
			if (list_bdcdyid != null && list_bdcdyid.size() > 0) {
				list_unit = new ArrayList<RealUnit>();
				for (Map m : list_bdcdyid) {
					if (m.containsKey("BDCDYID")) {
						String bdcdyid = m.get("BDCDYID").toString();
						String djdyid = m.get("DJDYID").toString();
						if (!StringHelper.isEmpty(bdcdyid)) {
							RealUnit unit = UnitTools.loadUnit(
									BDCDYLX.initFrom(bdcdylx), DJDYLY.XZ,
									bdcdyid);
							if (unit != null) {
								if (!list_unit.contains(unit)) {
									list_unit.add(unit);
								}
								if (!djdy_map.containsKey(bdcdyid)) {
									djdy_map.put(bdcdyid, djdyid);
								}
							}
						}
					}
				}
			}
		}
		if (list_unit == null || list_unit.size() <= 0) {
			HashMap<String, String> map = new HashMap<String, String>();
			// 初始化状态值，状态值说明；2代表未查询到单元，1代表有此状态，0代表无此状态，-1表示正在办理此状态
			map.put("MortgageStatus", "2");// 抵押状态
			map.put("NoticeStatus", "2");// 期房预告状态
			map.put("PreMortgageStatus", "2");// 期房抵押状态
			map.put("LimitStatus", "2");// 查封状态（期房或现房有一个呗查封，则有此状态）
			map.put("RelationID", "");
			map.put("IsOwner", "2");
			map.put("Handleing", "2");
			list.add(map);
			return list;
		}
		System.out.println("测试时间2:" + (System.currentTimeMillis() - startTime));
		if (BDCDYLX.H.Value.equals(bdcdylx)) {
			for (RealUnit unit : list_unit) {
				HashMap<String, String> map = getStatus_SCH_Mapping(unit);
				String IsOwner = "0";
				if (!StringHelper.isEmpty(qlrmc)
						&& djdy_map.containsKey(unit.getId())) {
					String djdyid = djdy_map.get(unit.getId());
					if (!StringHelper.isEmpty(djdyid)) {
						List<Rights> list_ownerRight = RightsTools
								.loadRightsByCondition(DJDYLY.XZ,
										" QLLX IN ('4','6','8') AND DJLX<>'700' AND DJDYID='"
												+ djdyid + "'");
						if (list_ownerRight != null
								&& list_ownerRight.size() > 0) {
							Rights ql = list_ownerRight.get(0);
							List<RightsHolder> list_owner = RightsHolderTools
									.loadRightsHolders(DJDYLY.XZ, ql.getId());
							for (RightsHolder qlr : list_owner) {
								String owner = qlr.getQLRMC();
								if (qlrmc.equals(owner)) {
									IsOwner = "1";
									break;
								}
							}
						}
					}
				}
				map.put("IsOwner", IsOwner);
				list.add(map);
			}
		} else {
			for (RealUnit unit : list_unit) {
				HashMap<String, String> map = getStatus_YCH_Mapping(unit);
				String IsOwner = "0";
				if (!StringHelper.isEmpty(qlrmc)
						&& djdy_map.containsKey(unit.getId())) {
					String djdyid = djdy_map.get(unit.getId());
					if (!StringHelper.isEmpty(djdyid)) {
						List<Rights> list_ownerRight = RightsTools
								.loadRightsByCondition(DJDYLY.XZ,
										" QLLX IN ('4','6','8') AND DJDYID='"
												+ djdyid + "'");
						if (list_ownerRight != null
								&& list_ownerRight.size() > 0) {
							Rights ql = list_ownerRight.get(0);
							List<RightsHolder> list_owner = RightsHolderTools
									.loadRightsHolders(DJDYLY.XZ, ql.getId());
							for (RightsHolder qlr : list_owner) {
								String owner = qlr.getQLRMC();
								if (qlrmc.equals(owner)) {
									IsOwner = "1";
									break;
								}
							}
						}
					}
				}
				map.put("IsOwner", IsOwner);
				list.add(map);
			}
		}
		System.out.println("测试时间3:" + (System.currentTimeMillis() - startTime));
		return list;
	}

	@Override
	public HashMap<String, String> HouseStatusQuery_BDCDYID(String bdcdyid,
			String djdyid, String bdcdylx) {
		HashMap<String, String> map = new HashMap<String, String>();
		RealUnit unit = null;
		if (!StringHelper.isEmpty(bdcdyid) && !StringHelper.isEmpty(bdcdylx)) {
			unit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.XZ,
					bdcdyid);
			if (unit == null) {
				List<BDCS_DJDY_XZ> djdys = baseCommonDao.getDataList(
						BDCS_DJDY_XZ.class, "DJDYID='" + djdyid
								+ "' AND BDCDYLX='" + bdcdylx + "'");
				if (djdys != null && djdys.size() > 0) {
					String xzbdcdyid = djdys.get(0).getBDCDYID();
					if (!StringHelper.isEmpty(xzbdcdyid)) {
						unit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx),
								DJDYLY.XZ, xzbdcdyid);
					}
				}
			}
			if (unit == null) {
				unit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.GZ,
						bdcdyid);
			}
		}
		if (unit == null) {
			// 初始化状态值，状态值说明；2代表未查询到单元，1代表有此状态，0代表无此状态，-1表示正在办理此状态
			map.put("MortgageStatus", "2");// 抵押状态
			map.put("NoticeStatus", "2");// 期房预告状态
			map.put("PreMortgageStatus", "2");// 期房抵押状态
			map.put("LimitStatus", "2");// 查封状态（期房或现房有一个呗查封，则有此状态）
			map.put("RelationID", "");
			map.put("BDCDYID", bdcdyid);
			map.put("UnitLimitStatus", "无限制");
			return map;
		}
		if (BDCDYLX.H.Value.equals(bdcdylx)) {
			map = getStatus_SCH_Mapping(unit);
		} else {
			map = getStatus_YCH_Mapping(unit);
		}
		// 添加单元限制状态
		List<String> list_dyxz = new ArrayList<String>();
		List<BDCS_DYXZ> dyxzs = baseCommonDao.getDataList(BDCS_DYXZ.class,
				"BDCDYID='" + unit.getId() + "' AND YXBZ='1'");
		if (dyxzs != null && dyxzs.size() > 0) {
			for (BDCS_DYXZ dyxz : dyxzs) {
				if (!StringHelper.isEmpty(dyxz.getXZLXName())
						&& !list_dyxz.contains(dyxz.getXZLXName())) {
					list_dyxz.add(dyxz.getXZLXName());
				}
			}
		}
		if (list_dyxz.size() > 0) {
			map.put("UnitLimitStatus", StringHelper.formatList(list_dyxz, "、"));// 单元限制
		} else {
			map.put("UnitLimitStatus", "无限制");// 单元限制
		}
		return map;
	}

	@Override
	public List<BDCS_DYXZ> GetDYXZList_XZ(String bdcdyid) {
		List<BDCS_DYXZ> dyxzs = baseCommonDao.getDataList(BDCS_DYXZ.class,
				"BDCDYID='" + bdcdyid + "' AND YXBZ IN ('1','2')");
		return dyxzs;
	}

	@Override
	public BDCS_DYXZ GetDYXZInfo(String id) {
		BDCS_DYXZ dyxz = baseCommonDao.get(BDCS_DYXZ.class, id);
		if(dyxz != null){
			if(!StringHelper.isEmpty(dyxz.getYWH())){
				//限制流水号
				List<BDCS_XMXX> xmxx = baseCommonDao.getDataList(BDCS_XMXX.class, "project_id='"+dyxz.getYWH()+"'");
				if(xmxx != null && xmxx.size() > 0){
					dyxz.setYWLSH(xmxx.get(0).getYWLSH());
				}
			}
			if(!StringHelper.isEmpty(dyxz.getZXYWH())){
				//解除限制流水号
				List<BDCS_XMXX> xmxx_xz = baseCommonDao.getDataList(BDCS_XMXX.class, "project_id='"+dyxz.getZXYWH()+"'");
				if(xmxx_xz != null && xmxx_xz.size() > 0){
					dyxz.setZXYWLSH(xmxx_xz.get(0).getYWLSH());
				}
			}
		}
		return dyxz;
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	@Override
	public void getFcfhtInfo(String zrzbdcdyid,HttpServletResponse response ,String fileurl) throws UnsupportedEncodingException {
		Map map=new HashMap();
		String bdcdyid = null;
		String bdcdylx = "032";
		List<BDCS_H_XZ> listsxz = baseCommonDao.getDataList(BDCS_H_XZ.class,
				"ZRZBDCDYID='" + zrzbdcdyid + "'");
		if(listsxz.size()>0){
			BDCS_H_XZ h=listsxz.get(0);
			if(h.getId()!=null&&h.getId()!=""){
				bdcdyid=h.getId();
			}
		}else{
			List<BDCS_H_XZY> listsxzy = baseCommonDao.getDataList(BDCS_H_XZY.class,
					"ZRZBDCDYID='" + zrzbdcdyid + "'");
			if(listsxzy.size()>0){
				BDCS_H_XZY h=listsxzy.get(0);
				if(h.getId()!=null&&h.getId()!=""){
					bdcdyid=h.getId();
				}
			}else{
				
			}
		}
		if(bdcdyid==null || bdcdyid==""){
			bdcdyid=zrzbdcdyid;
		}
		List<BDCS_DJDY_XZ> djdys_ls = baseCommonDao.getDataList(
				BDCS_DJDY_XZ.class, "BDCDYID='" +bdcdyid + "'");
		if(djdys_ls.size()>0){
			BDCS_DJDY_XZ djdy=djdys_ls.get(0);
			  bdcdylx=djdy.getBDCDYLX();
		}
		
		if(bdcdylx.equals("031")){
				List<BDCS_H_XZ> lists = baseCommonDao.getDataList(BDCS_H_XZ.class,
						"BDCDYID='" + bdcdyid + "'");
				if(lists.size()>0){
					OutputStream os = null;
					try {
						os = response.getOutputStream();
					} catch (IOException e) {
						e.printStackTrace();
					}
					BDCS_H_XZ  ahouse=lists.get(0);
					try {
						response.reset();
						String fileName = "temp";
						fileName = java.net.URLEncoder.encode(
								new String(fileName.getBytes("ISO-8859-1"), "UTF-8"), "UTF-8");

						response.setHeader("Content-Disposition",
								"attachment; filename=" + fileName);
						response.setContentType("image/jpeg; charset=UTF-8");
						String path=ahouse.getFCFHT();
						File f=new File(fileurl+"\\"+path);
						byte[] fcfht = null;
						try {
							   if (f.exists()) {
								   fcfht=FileUtils.readFileToByteArray(f);
					            } else {
					            }
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						if(fcfht!=null){
							try {
								os.write(fcfht);
							} catch (IOException e) {
								e.printStackTrace();
							}
							try {
								os.flush();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						

					} finally {
						if (os != null) {
							try {
								os.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					
				}else{
					 
				}
			 
			 
		}else {

			List<BDCS_H_XZY> lists = baseCommonDao.getDataList(BDCS_H_XZY.class,
					"BDCDYID='" + bdcdyid + "'");
			if(lists.size()>0){
				OutputStream os = null;
				try {
					os = response.getOutputStream();
				} catch (IOException e) {
					e.printStackTrace();
				}
				BDCS_H_XZY  ahouse=lists.get(0);
				try {
					String fileName = "temp";
					fileName = java.net.URLEncoder.encode(
							new String(fileName.getBytes("ISO-8859-1"), "UTF-8"), "UTF-8");

					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);
					response.setContentType("image/png; charset=UTF-8");
					String path=ahouse.getFCFHT();
					File f=new File(fileurl+"\\"+path);
					
					byte[] fcfht = null;
					try {
						   if (f.exists()) {
							   fcfht=FileUtils.readFileToByteArray(f);
				            } else {
				            }
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					if(fcfht!=null){
						try {
							os.write(fcfht);
						} catch (IOException e) {
							e.printStackTrace();
						}
						try {
							os.flush();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					

				} finally {
					if (os != null) {
						try {
							os.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				
			}else{
				 
			}
		
		}
	 
	}
	/**
	 * 根据传入的宗地不动产单元ID获取该宗地上房屋列表
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Message GetYdyFwInfo(String zdbdcdyid, int page, int rows) {
		Message msg = new Message();
		List<Map> logsResult = new ArrayList<Map>();
		long count = 0;
		if(zdbdcdyid != null ){
			String fullSql = MessageFormat.format("select BDCDYH,BDCDYID,ZL,''031'' AS BDCDYLX from BDCK.BDCS_H_XZ where zdbdcdyid=''{0}'' union all select  BDCDYH,BDCDYID,ZL,''032'' AS BDCDYLX from BDCK.BDCS_H_XZY where zdbdcdyid=''{0}''", zdbdcdyid);
			List<Map> houses = baseCommonDao.getDataListByFullSql(fullSql);
			//根据已抵押房屋不动产单元号（BDCDYH）获取该房屋信息
			if(houses != null && houses.size() > 0){
				for(Map house :houses){
						String _bdcdyh = (String) house.get("BDCDYH");
						if(_bdcdyh != null ){
							String sqlMortgage = MessageFormat
									.format(" from BDCK.BDCS_QL_XZ where bdcdyh=''{0}'' and qllx=''23''",
											_bdcdyh);
							String sqlSeal = MessageFormat
									.format(" from BDCK.BDCS_QL_XZ where bdcdyh=''{0}'' and djlx=''800'' and qllx=''99''",
											_bdcdyh);
							String sqlObjection = MessageFormat
									.format("  from BDCK.BDCS_QL_XZ where bdcdyh=''{0}'' and djlx=''600'' ",
											_bdcdyh);

							long mortgageCount = baseCommonDao
									.getCountByFullSql(sqlMortgage);
							long SealCount = baseCommonDao
									.getCountByFullSql(sqlSeal);
							long ObjectionCount = baseCommonDao
									.getCountByFullSql(sqlObjection);

							String mortgageStatus = mortgageCount > 0 ? "已抵押"
									: "无抵押";
							String sealStatus = SealCount > 0 ? "已查封" : "无查封";

							// 判断完现状层中的查封信息，接着判断办理中的查封信息
							if (!(SealCount > 0)) {
								String sqlSealing = " FROM bdck.bdcs_xmxx a  LEFT JOIN bdck.bdcs_ql_gz c ON a.xmbh=c.xmbh and a.sfdb='0' WHERE c.djlx='800' AND c.qllx='99' and c.bdcdyh='"
										+ _bdcdyh + "' and a.sfdb='0' ";
								long scount = baseCommonDao
										.getCountByFullSql(sqlSealing);
								sealStatus = scount > 0 ? "查封办理中" : "无查封";
							}

							// 判断完现状层中的查封信息，接着判断办理中的查封信息
							if (!(mortgageCount > 0)) {
								String sqlmortgageing = " FROM bdck.bdcs_xmxx a  LEFT JOIN bdck.bdcs_ql_gz c ON a.xmbh=c.xmbh and a.sfdb='0' WHERE c.djlx='100' AND c.qllx='23' and c.bdcdyh='"
										+ _bdcdyh + "' and a.sfdb='0' ";
								long mcount = baseCommonDao
										.getCountByFullSql(sqlmortgageing);
								mortgageStatus = mcount > 0 ? "抵押办理中" : "无抵押";
							}

							String objectionStatus = ObjectionCount > 0 ? "有异议"
									: "无异议";
							house.put("FWZLZT","("+ mortgageStatus+";"+sealStatus+";"+objectionStatus+")"+house.get("ZL"));
						}
						logsResult.add(house);
				}
			
			}
		}
		msg.setTotal(count);
		msg.setRows(logsResult);
		return msg;
	}
	/**
	 * 根据传入的自然幢不动产单元ID获取该宗地上房屋列表
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Message GetFwInfoByZrz(String zrzbdcdyid, int page, int rows) {
		Message msg = new Message();
		List<Map> logsResult = new ArrayList<Map>();
		long count = 0;
		if(zrzbdcdyid != null ){
			String fullSql = MessageFormat.format("select BDCDYH,BDCDYID,ZL,''031'' AS BDCDYLX from BDCK.BDCS_H_XZ WHERE ZRZBDCDYID=''{0}'' union all select  BDCDYH,BDCDYID,ZL,''032'' AS BDCDYLX from BDCK.BDCS_H_XZY WHERE ZRZBDCDYID=''{0}''", zrzbdcdyid);
			List<Map> houses = baseCommonDao.getDataListByFullSql(fullSql);
			//根据已抵押房屋不动产单元号（BDCDYH）获取该房屋信息
			if(houses != null && houses.size() > 0){
				for(Map house :houses){
						String _bdcdyh = (String) house.get("BDCDYH");
						if(_bdcdyh != null ){
							String sqlMortgage = MessageFormat
									.format(" from BDCK.BDCS_QL_XZ where bdcdyh=''{0}'' and qllx=''23''",
											_bdcdyh);
							String sqlSeal = MessageFormat
									.format(" from BDCK.BDCS_QL_XZ where bdcdyh=''{0}'' and djlx=''800'' and qllx=''99''",
											_bdcdyh);
							String sqlObjection = MessageFormat
									.format("  from BDCK.BDCS_QL_XZ where bdcdyh=''{0}'' and djlx=''600'' ",
											_bdcdyh);

							long mortgageCount = baseCommonDao
									.getCountByFullSql(sqlMortgage);
							long SealCount = baseCommonDao
									.getCountByFullSql(sqlSeal);
							long ObjectionCount = baseCommonDao
									.getCountByFullSql(sqlObjection);

							String mortgageStatus = mortgageCount > 0 ? "已抵押"
									: "无抵押";
							String sealStatus = SealCount > 0 ? "已查封" : "无查封";

							// 判断完现状层中的查封信息，接着判断办理中的查封信息
							if (!(SealCount > 0)) {
								String sqlSealing = " FROM bdck.bdcs_xmxx a  LEFT JOIN bdck.bdcs_ql_gz c ON a.xmbh=c.xmbh and a.sfdb='0' WHERE c.djlx='800' AND c.qllx='99' and c.bdcdyh='"
										+ _bdcdyh + "' and a.sfdb='0' ";
								long scount = baseCommonDao
										.getCountByFullSql(sqlSealing);
								sealStatus = scount > 0 ? "查封办理中" : "无查封";
							}

							// 判断完现状层中的查封信息，接着判断办理中的查封信息
							if (!(mortgageCount > 0)) {
								String sqlmortgageing = " FROM bdck.bdcs_xmxx a  LEFT JOIN bdck.bdcs_ql_gz c ON a.xmbh=c.xmbh and a.sfdb='0' WHERE c.djlx='100' AND c.qllx='23' and c.bdcdyh='"
										+ _bdcdyh + "' and a.sfdb='0' ";
								long mcount = baseCommonDao
										.getCountByFullSql(sqlmortgageing);
								mortgageStatus = mcount > 0 ? "抵押办理中" : "无抵押";
							}

							String objectionStatus = ObjectionCount > 0 ? "有异议"
									: "无异议";
							house.put("FWZLZT","("+ mortgageStatus+";"+sealStatus+";"+objectionStatus+")"+house.get("ZL"));
						}
						logsResult.add(house);
				}
			
			}
		}
		msg.setTotal(count);
		msg.setRows(logsResult);
		return msg;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, String> HouseStatusQuery_QZHS(String qzh,String qzlb) {
		Map<String,String> ma=new HashMap<String,String>();
		List<HashMap<String, String>>  mas=new  ArrayList<HashMap<String, String>>();
		List<HashMap<String, String>>  mass=new  ArrayList<HashMap<String, String>>();
		//页面需要的数据，项目名称，坐落，抵押权，查封，异议，排除之前的就是所有权
		mas = HouseStatusQuery_QZHL(qzh,"031");//实测户权利的所有信息
		if (StringHelper.isEmpty(mas.get(0).get("RelationID"))) {
			if(qzlb != null && qzlb.toString().length() > 0 ){
				String qzlbs = "%" + qzlb + "%";
				qzh = qzh.toString().replace("-", qzlbs);
				mas = HouseStatusQuery_QZHL(qzh, "031");//实测户权利的所有信息
				if (StringHelper.isEmpty(mas.get(0).get("RelationID")) && StringHelper.isEmpty(mas.get(0).get("zl")) ) {
					mas = HouseStatusQuery_QZHL(qzh, "032");//预测户权利的所有信息
				}
			}
		}
		String syq="0";//所有权状态：默认0-》未注销
		String dyzt="0";//抵押状态：0：未抵押
		String cfzt="0";//查封状态：0：未查封
		String yyzt="0";//异议状态：0：无异议
		String id = "";
		String zl="";
	    Date date = new Date();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String cxsj = sdf.format(date);
		if(mas!=null && mas.size() > 0){
			for(Map m:mas){
				//初始化状态值，状态值说明；2代表未查询到单元，1代表有此状态，0代表无此状态，-1表示正在办理此状态
				dyzt=m.get("MortgageStatus").toString();
				cfzt=m.get("LimitStatus").toString();
				StringBuilder  stryy=new  StringBuilder();
				if (!StringHelper.isEmpty(m.get("RelationID"))) {   //有relationid就以此为条件，若无则以bdcdyid --陈博
					id = m.get("RelationID").toString();
					stryy.append("select  *  from bdck.bdcs_h_xz a  left join bdck.bdcs_djdy_xz b on a.bdcdyid=b.bdcdyid  left join bdck.bdcs_ql_xz c  on c.djdyid=b.djdyid where  a.relationid='").append(id).append("' and  c.djlx='600' ");
				}else if (!StringHelper.isEmpty(m.get("bdcdyid"))) {
					id = m.get("bdcdyid").toString();
					stryy.append("select  *  from bdck.bdcs_h_xz a  left join bdck.bdcs_djdy_xz b on a.bdcdyid=b.bdcdyid  left join bdck.bdcs_ql_xz c  on c.djdyid=b.djdyid where  a.bdcdyid='").append(id).append("' and  c.djlx='600' ");
				}				
				long yy=baseCommonDao.getCountByFullSql("from ("+stryy.toString()+")");
				if(yy>0){
					yyzt="1";
				}
				if(!dyzt.equals("1")&&!cfzt.equals("1")&&!yyzt.equals("1")){
					StringBuilder  strycyy = new  StringBuilder();
					if (!StringHelper.isEmpty(m.get("RelationID"))) {
						mass=HouseStatusQuery_RelationID(id,"032");//预测户权利的所有信息,根据relationid
						strycyy.append("select  *  from bdck.bdcs_h_xzy a  left join bdck.bdcs_djdy_xz b on a.bdcdyid=b.bdcdyid  left join bdck.bdcs_ql_xz c  on c.djdyid=b.djdyid  where   a.relationid='").append(id).append("' and  c.djlx='600'");
					}else if(!StringHelper.isEmpty(m.get("bdcdyid"))){
						mass=HouseStatusQuery_BDCDYID(id,"032");//预测户权利的所有信息,根据bdcdyid
						strycyy.append("select  *  from bdck.bdcs_h_xzy a  left join bdck.bdcs_djdy_xz b on a.bdcdyid=b.bdcdyid  left join bdck.bdcs_ql_xz c  on c.djdyid=b.djdyid  where   a.relationid='").append(id).append("' and  c.djlx='600'");
					}
					for(Map mm:mass){
						dyzt=mm.get("MortgageStatus").toString();
						cfzt=mm.get("LimitStatus").toString();
						long yys = baseCommonDao.getCountByFullSql("from ("+strycyy.toString()+")");
						if(yys>0){
							yyzt = "1";
						}
					}
				}
				zl=(String) m.get("zl");
			} 
					ma.put("SYQ", syq.equals("1")?"已注销":"未注销");
					ma.put("DYZT", dyzt.equals("1")?"已抵押":"未抵押");
					ma.put("CFZT", cfzt.equals("1")?"已查封":"未查封");
					ma.put("YYZT", yyzt.equals("1")?"有异议":"无异议");
					ma.put("CXSJ", cxsj);
					ma.put("ZL", zl);
		 }else{
					ma.put("SYQ", "查无此信息");
					ma.put("DYZT", "查无此信息");
					ma.put("CFZT", "查无此信息");
					ma.put("YYZT", "查无此信息");
					ma.put("ZL", "查无此信息");
		 }
		return ma;
	}

	@SuppressWarnings("rawtypes")
	protected List<HashMap<String, String>> HouseStatusQuery_QZHL(String qzh,String bdcdylx) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		List<RealUnit> list_unit = null;
		String bdcdyid = "";
		if (!StringHelper.isEmpty(qzh)) {
			StringBuilder builder_dy = new StringBuilder();
			builder_dy
					.append("SELECT DJDY.BDCDYID FROM BDCK.BDCS_DJDY_XZ DJDY ");
			builder_dy
					.append("LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=DJDY.DJDYID ");
			builder_dy
					.append("LEFT JOIN BDCK.BDCS_QLR_XZ QLR ON QLR.QLID=QL.QLID ");
//			builder_dy.append("WHERE DJDY.BDCDYLX='");
//			builder_dy.append(bdcdylx);
			int  indexflag=qzh.indexOf("%");
			if(indexflag>0){
				builder_dy.append("WHERE QLR.BDCQZH like'%");
				builder_dy.append(qzh).append("%'");
			}else{
				builder_dy.append("WHERE QLR.BDCQZH ='");
				builder_dy.append(qzh).append("'");
			}
			List<Map> list_bdcdyid = baseCommonDao.getDataListByFullSql(builder_dy.toString());
			if (list_bdcdyid != null && list_bdcdyid.size() > 0) {
				list_unit = new ArrayList<RealUnit>();
				for (Map m : list_bdcdyid) {
					if (m.containsKey("BDCDYID")) {
						bdcdyid = m.get("BDCDYID").toString();
						if (!StringHelper.isEmpty(bdcdyid)) {
							RealUnit unit = UnitTools.loadUnit(
									BDCDYLX.initFrom(bdcdylx), DJDYLY.XZ,
									bdcdyid);
							if (unit != null) {
								if (!list_unit.contains(unit)) {
									list_unit.add(unit);
								}
							}
						}
					}
				}
			}
		}
		if (list_unit == null || list_unit.size() <= 0) {
			HashMap<String, String> map = new HashMap<String, String>();
			// 初始化状态值，状态值说明；2代表未查询到单元，1代表有此状态，0代表无此状态，-1表示正在办理此状态
			map.put("MortgageStatus", "2");// 抵押状态
			map.put("NoticeStatus", "2");// 期房预告状态
			map.put("PreMortgageStatus", "2");// 期房抵押状态
			map.put("LimitStatus", "2");// 查封状态（期房或现房有一个呗查封，则有此状态）
			map.put("RelationID", "");
			map.put("bdcdyid", bdcdyid);
			list.add(map);
			return list;
		}
		if (BDCDYLX.H.Value.equals(bdcdylx)) {
			for (RealUnit unit : list_unit) {
				HashMap<String, String> map = getStatus_SCH_Mapping(unit);
				map.put("zl", unit.getZL());
				map.put("xmbh", unit.getXMBH());
				map.put("bdcdyid", unit.getId());
				list.add(map);
			}
		} else {
			for (RealUnit unit : list_unit) {
				HashMap<String, String> map = getStatus_YCH_Mapping(unit);
				map.put("zl", unit.getZL());
				map.put("xmbh", unit.getXMBH());
				map.put("bdcdyid", unit.getId());
				list.add(map);
			}
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Message GetZrzList(String zdbdcdyid,String type,
			Map<String, String> querycondition, int page, int rows) {
		List<Map> listresult = null;
		Message m=new Message();
		String zrzyfields=" BDCDYH,BDCDYID,ZL,'08' AS BDCDYLX ",zrzfields=" BDCDYH,BDCDYID,ZL,'03' AS BDCDYLX ";
		StringBuilder zrzyFromBuilderSql = new StringBuilder("from BDCK.BDCS_ZRZ_XZY where zdbdcdyid='"+zdbdcdyid+"'");
		
		StringBuilder zrzFromBuilderSql = new StringBuilder("from BDCK.BDCS_ZRZ_XZ where zdbdcdyid='"+zdbdcdyid+"'");
		/* ===============3、构造查询语句=================== */
		/* SELECT 字段部分 */
		StringBuilder countbuilder = new StringBuilder();
		StringBuilder allsqlbuilder = new StringBuilder();
		
		for (Entry<String, String> ent : querycondition.entrySet()) {
			String name = ent.getKey();
			String value = ent.getValue();
			if (!StringHelper.isEmpty(value)) {
				zrzyFromBuilderSql.append(" AND instr(");
				zrzyFromBuilderSql.append(name);
				zrzyFromBuilderSql.append(",'");
				zrzyFromBuilderSql.append(value);
				zrzyFromBuilderSql.append("')>0 ");
				zrzFromBuilderSql.append(" AND instr(");
				zrzFromBuilderSql.append(name);
				zrzFromBuilderSql.append(",'");
				zrzFromBuilderSql.append(value);
				zrzFromBuilderSql.append("')>0 ");
			}
	   }
		//0表示全部幢
		if(type==null||type.equals("0"))
		{
			countbuilder.append(" SELECT SUM(C) AS C FROM (");
			countbuilder.append("SELECT COUNT(0) AS C ");
			countbuilder.append(zrzyFromBuilderSql);
			countbuilder.append(" UNION ALL ");
			countbuilder.append("SELECT COUNT(0) AS C ");
			countbuilder.append(zrzFromBuilderSql);
			countbuilder.append(")");
			allsqlbuilder.append("SELECT * FROM (");
			allsqlbuilder.append("SELECT ");
			allsqlbuilder.append(zrzfields);
			allsqlbuilder.append(zrzFromBuilderSql);
			allsqlbuilder.append("UNION ALL ");
			allsqlbuilder.append("SELECT ");
			allsqlbuilder.append(zrzyfields);
			allsqlbuilder.append(zrzyFromBuilderSql);
			allsqlbuilder.append(")");
			
		}//1表示实测幢
		else if(type.equals("1"))
		{
			countbuilder.append("SELECT COUNT(0) AS C ");
			countbuilder.append(zrzFromBuilderSql);
			allsqlbuilder.append("SELECT ");
			allsqlbuilder.append(zrzfields);
			allsqlbuilder.append(zrzFromBuilderSql);
		}//2预测幢
		else if(type.equals("2"))
		{
			countbuilder.append("SELECT COUNT(0) AS C ");
			countbuilder.append(zrzyFromBuilderSql);
			allsqlbuilder.append("SELECT ");
			allsqlbuilder.append(zrzyfields);
			allsqlbuilder.append(zrzyFromBuilderSql);
		
		}
		long count =0;
		List<Map> counts=baseCommonDao.getDataListByFullSql(countbuilder.toString());
		count=Long.valueOf(String.valueOf(counts.get(0).get("C")));
		if(count>0)
		 listresult = baseCommonDao.getPageDataByFullSql(allsqlbuilder.toString(), page, rows);
		else
		 return m;
		m.setTotal(count);
		m.setRows(listresult);
	    return m;
	}
	
	public Message GetZrzXx(String zdbdcdyid,String type,
			Map<String, String> querycondition, int page, int rows) {
		List<Map> listresult = null;
		Message m=new Message();
		String zrzyfields=" BDCDYH,BDCDYID,ZL,ZRZH, YCJZMJ AS JZMJ ,ZZDMJ,ZCS,'08' AS BDCDYLX ",zrzfields=" BDCDYH,BDCDYID,ZL,ZRZH,SCJZMJ AS JZMJ,ZZDMJ,ZCS,'03' AS BDCDYLX ";
		StringBuilder zrzyFromBuilderSql = new StringBuilder("from BDCK.BDCS_ZRZ_XZY where bdcdyid='"+zdbdcdyid+"'");
		
		StringBuilder zrzFromBuilderSql = new StringBuilder("from BDCK.BDCS_ZRZ_XZ where bdcdyid='"+zdbdcdyid+"'");
		/* ===============3、构造查询语句=================== */
		/* SELECT 字段部分 */
		StringBuilder countbuilder = new StringBuilder();
		StringBuilder allsqlbuilder = new StringBuilder();
		
		for (Entry<String, String> ent : querycondition.entrySet()) {
			String name = ent.getKey();
			String value = ent.getValue();
			if (!StringHelper.isEmpty(value)) {
				zrzyFromBuilderSql.append(" AND instr(");
				zrzyFromBuilderSql.append(name);
				zrzyFromBuilderSql.append(",'");
				zrzyFromBuilderSql.append(value);
				zrzyFromBuilderSql.append("')>0 ");
				zrzFromBuilderSql.append(" AND instr(");
				zrzFromBuilderSql.append(name);
				zrzFromBuilderSql.append(",'");
				zrzFromBuilderSql.append(value);
				zrzFromBuilderSql.append("')>0 ");
			}
	   }
		//0表示全部幢
		if(type==null||type.equals("0"))
		{
			countbuilder.append(" SELECT SUM(C) AS C FROM (");
			countbuilder.append("SELECT COUNT(0) AS C ");
			countbuilder.append(zrzyFromBuilderSql);
			countbuilder.append(" UNION ALL ");
			countbuilder.append("SELECT COUNT(0) AS C ");
			countbuilder.append(zrzFromBuilderSql);
			countbuilder.append(")");
			allsqlbuilder.append("SELECT * FROM (");
			allsqlbuilder.append("SELECT ");
			allsqlbuilder.append(zrzfields);
			allsqlbuilder.append(zrzFromBuilderSql);
			allsqlbuilder.append("UNION ALL ");
			allsqlbuilder.append("SELECT ");
			allsqlbuilder.append(zrzyfields);
			allsqlbuilder.append(zrzyFromBuilderSql);
			allsqlbuilder.append(")");
			
		}//1表示实测幢
		else if(type.equals("1"))
		{
			countbuilder.append("SELECT COUNT(0) AS C ");
			countbuilder.append(zrzFromBuilderSql);
			allsqlbuilder.append("SELECT ");
			allsqlbuilder.append(zrzfields);
			allsqlbuilder.append(zrzFromBuilderSql);
		}//2预测幢
		else if(type.equals("2"))
		{
			countbuilder.append("SELECT COUNT(0) AS C ");
			countbuilder.append(zrzyFromBuilderSql);
			allsqlbuilder.append("SELECT ");
			allsqlbuilder.append(zrzyfields);
			allsqlbuilder.append(zrzyFromBuilderSql);
		
		}
		long count =0;
		List<Map> counts=baseCommonDao.getDataListByFullSql(countbuilder.toString());
		count=Long.valueOf(String.valueOf(counts.get(0).get("C")));
		if(count>0)
		 listresult = baseCommonDao.getPageDataByFullSql(allsqlbuilder.toString(), page, rows);
		else
		 return m;
		m.setTotal(count);
		m.setRows(listresult);
	    return m;
	}

	@Override
	public HashMap<String,Object> getUnitRelation(String bdcdyid, String bdcdylx) {
		List<UnitRelation> list_unitrelation=new ArrayList<UnitRelation>();
		List<UnitInfo> list_unitinfo=new ArrayList<UnitInfo>();
		List<String> list_bdcdyid=new ArrayList<String>();
		List<String> list_from_to=new ArrayList<String>();
		getUnitRelation(bdcdyid,bdcdylx,list_unitinfo,list_unitrelation,list_bdcdyid,list_from_to);
		HashMap<String,Object> m=new HashMap<String, Object>();
		m.put("units", list_unitinfo);
		m.put("relations", list_unitrelation);
		return m;
	}
	
	protected void getUnitRelation(String bdcdyid, String bdcdylx,List<UnitInfo> list_unitinfo,List<UnitRelation> list_unitrelation,List<String> list_bdcdyid,List<String> list_from_to) {
		if(!list_bdcdyid.contains(bdcdyid)){
			list_bdcdyid.add(bdcdyid);
			RealUnit unit=UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.LS, bdcdyid);
			if(unit!=null){
				UnitInfo info=new UnitInfo();
				info.setBdcdyid(bdcdyid);
				info.setBdcdyh(unit.getBDCDYH());
				info.setZl(unit.getZL());
				info.setBdcdylx(unit.getBDCDYLX().Value);
				info.setMj(StringHelper.formatDouble(unit.getMJ()));
				list_unitinfo.add(info);
				List<BDCS_DYBG> list_dybg_to=baseCommonDao.getDataList(BDCS_DYBG.class, "LBDCDYID='"+bdcdyid+"' AND NVL2(XBDCDYID,1,0)=1 AND XBDCDYID<>'"+bdcdyid+"' AND LDJDYID<>XDJDYID");
				if(list_dybg_to!=null&&list_dybg_to.size()>0){
					for(BDCS_DYBG dybg:list_dybg_to){
						UnitRelation relation=new UnitRelation();
						relation.setFrom(bdcdyid);
						relation.setTo(dybg.getXBDCDYID());
						String from_to=relation.getFrom()+"&&"+relation.getTo();
						if(!list_from_to.contains(from_to)){
							list_unitrelation.add(relation);
							list_from_to.add(from_to);
							getUnitRelation(dybg.getXBDCDYID(),bdcdylx,list_unitinfo,list_unitrelation,list_bdcdyid,list_from_to);
							
						}
						
					}
				}
				List<BDCS_DYBG> list_dybg_from=baseCommonDao.getDataList(BDCS_DYBG.class, "XBDCDYID='"+bdcdyid+"' AND NVL2(LBDCDYID,1,0)=1 AND LBDCDYID<>'"+bdcdyid+"' AND LDJDYID<>XDJDYID");
				if(list_dybg_from!=null&&list_dybg_from.size()>0){
					for(BDCS_DYBG dybg:list_dybg_from){
						UnitRelation relation=new UnitRelation();
						relation.setFrom(dybg.getLBDCDYID());
						relation.setTo(bdcdyid);
						String from_to=relation.getFrom()+"&&"+relation.getTo();
						if(!list_from_to.contains(from_to)){
							list_unitrelation.add(relation);
							list_from_to.add(from_to);
							getUnitRelation(dybg.getLBDCDYID(),bdcdylx,list_unitinfo,list_unitrelation,list_bdcdyid,list_from_to);
						}
					}
				}
			}
		}
	}

	@Override
	public Map<String, Object> GetAchives_HK(String type, String bdcqzh) {
		Map<String, Object> result=new HashMap<String, Object>();
		if("cert".equals(type)){
			List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, " QLLX IN ('1','2','3','4','5','6','7','8') AND id in (SELECT QLID FROM BDCS_QLR_GZ WHERE BDCQZH='"+bdcqzh+"')");
			if(qls==null||qls.size()<=0){
				result.put("success", "false");
				result.put("msg", "未查询到相应产权！");
				return result;
			}else{
				result.put("success", "true");
				List<HashMap<String,Object>> list_achives=new ArrayList<HashMap<String,Object>>();
				for(Rights ql:qls){
					BDCS_XMXX xmxx=Global.getXMXXbyXMBH(ql.getXMBH());
					if(xmxx==null||DJLX.ZXDJ.Value.equals(xmxx.getDJLX())){
						continue;
					}
					List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "DJDYID='"+ql.getDJDYID()+"' AND XMBH='"+ql.getXMBH()+"'");
					if(djdys==null||djdys.size()<=0){
						continue;
					}
					RealUnit unit=UnitTools.loadUnit(BDCDYLX.initFrom(djdys.get(0).getBDCDYLX()), DJDYLY.initFrom(djdys.get(0).getLY()), djdys.get(0).getBDCDYID());
					if(unit==null){
						continue;
					}
					List<RightsHolder> qlrs=RightsHolderTools.loadRightsHolders(DJDYLY.GZ, ql.getId());
					HashMap<String,Object> achives=new HashMap<String, Object>();
					if(BDCDYLX.SHYQZD.Value.equals(djdys.get(0).getBDCDYLX())||BDCDYLX.SYQZD.Value.equals(djdys.get(0).getBDCDYLX())){
						result.put("单元类型", "宗地");
						achives.put("土地权属", ConstHelper.getNameByValue("QLXZ", StringHelper.formatObject(getFieldValueByName("QLXZ",unit))));
						achives.put("土地坐落", getFieldValueByName("ZL",unit));
						achives.put("东至", getFieldValueByName("ZDSZD",unit));
						achives.put("南至", getFieldValueByName("ZDSZN",unit));
						achives.put("西至", getFieldValueByName("ZDSZX",unit));
						achives.put("北至", getFieldValueByName("ZDSZN",unit));
						achives.put("图号", getFieldValueByName("TFH",unit));
						achives.put("宗地代码", getFieldValueByName("ZDDM",unit));
						achives.put("使用权类型", "");
						if(BDCDYLX.SYQZD.Value.equals(djdys.get(0).getBDCDYLX())){
							achives.put("土地用途", ConstHelper.getNameByValue("TDYT", StringHelper.formatObject(getFieldValueByName("YT",unit))));
							achives.put("使用期限", "");
						}else{
							UseLand land=(UseLand)unit;
							if(land!=null&&land.getTDYTS()!=null&&land.getTDYTS().size()>0){
								String tdytmc="";
								String syqx="";
								if (land.getTDYTS().size() == 1) {
									String qssj = StringHelper.formatObject(land.getTDYTS().get(0).getQSRQ()) + "起";
									String jssj = StringHelper.formatObject(land.getTDYTS().get(0).getZZRQ()) + "止";
									syqx = qssj + jssj;
									tdytmc=land.getTDYTS().get(0).getTDYTName();
								} else {
									int iii = 0;
									for (TDYT tdyt : land.getTDYTS()) {
										iii++;
										if (!StringHelper.isEmpty(tdyt.getTDYTName())) {
											syqx = syqx + tdyt.getTDYTName() + ":";
										} else {
											continue;
										}
										String qssj = StringHelper.formatObject(tdyt.getQSRQ()) + "起";
										String jssj = StringHelper.formatObject(tdyt.getZZRQ()) + "止";

										if (iii == land.getTDYTS().size()) {
											syqx = syqx + qssj + jssj;
											tdytmc=tdytmc+tdyt.getTDYTName();
										} else {
											syqx = syqx + qssj + jssj + "、";
											tdytmc=tdytmc+tdyt.getTDYTName()+"、";
										}
									}
								}
								achives.put("土地用途", tdytmc);
								achives.put("使用期限", syqx);
							}else{
								achives.put("土地用途", "");
								achives.put("使用期限", "");
							}
						}
						achives.put("建筑占地", getFieldValueByName("JZZDMJ",unit));
						if(qlrs!=null&&qlrs.size()>0){
							String qlrmc="";
							String zjh="";
							String dz="";
							String lxdh="";
							if (qlrs.size() == 1) {
								qlrmc = qlrs.get(0).getQLRMC();
								zjh=qlrs.get(0).getZJH();
								dz=qlrs.get(0).getDZ();
								lxdh=qlrs.get(0).getDH();
							} else {
								int iii = 0;
								for (RightsHolder qlr : qlrs) {
									iii++;
									if (iii == qlrs.size()) {
										qlrmc = qlrmc + qlr.getQLRMC();
										zjh=zjh+qlr.getZJH();
										dz=dz+qlr.getDZ();
										lxdh=lxdh+qlr.getDH();
									} else {
										qlrmc = qlrmc + qlr.getQLRMC() + "、";
										zjh=zjh+qlr.getZJH()+"、";
										dz=dz+qlr.getDZ()+"、";
										lxdh=lxdh+qlr.getDH()+"、";
									}
								}
							}
							achives.put("权利人", qlrmc);
							achives.put("身份证号", zjh);
							achives.put("单位地址", dz);
							achives.put("联系电话", dz);
						}else{
							achives.put("权利人", "");
							achives.put("身份证号", "");
							achives.put("单位地址", "");
							achives.put("联系电话", "");
						}
						achives.put("土地证号", ql.getBDCQZH());
						List<BDCS_DJFZ> list_djfz=baseCommonDao.getDataList(BDCS_DJFZ.class, "HFZSH='"+bdcqzh+"'");
						if(list_djfz!=null&&list_djfz.size()>0){
							achives.put("发证日期", StringHelper.FormatByDatetime(list_djfz.get(0).getFZSJ()));
						}else{
							achives.put("发证日期", "");
						}
						achives.put("企业性质", "");
						achives.put("地号", "");
						achives.put("旧地号", "");
						achives.put("注销日期", "");
						achives.put("申报表号", "");
						achives.put("用地来源", "");
						achives.put("终止日期", "");
						achives.put("年审有效至", "");
						achives.put("批准机关", "");
						achives.put("批准日期", "");
						achives.put("批准文号", "");
						achives.put("用地面积", "");
						achives.put("共有权面积", "");
						achives.put("分摊地面积", "");
						achives.put("分摊建筑", "");
						achives.put("用地类别", "");
						achives.put("经营项目", "");
						achives.put("经办人", "");
					}else if(BDCDYLX.H.Value.equals(djdys.get(0).getBDCDYLX())||BDCDYLX.YCH.Value.equals(djdys.get(0).getBDCDYLX())){
						result.put("单元类型", "房屋");
						achives.put("要素代码", getFieldValueByName("YSDM",unit));
						achives.put("不动产单元号", getFieldValueByName("BDCDYH",unit));
						achives.put("业务号", ql.getYWH());
						achives.put("权利类型", ConstHelper.getNameByValue("QLLX", ql.getQLLX()));
						achives.put("登记类型", ConstHelper.getNameByValue("DJLX", ql.getDJLX()));
						achives.put("登记原因", ql.getDJYY());
						achives.put("区县代码", getFieldValueByName("QXDM",unit));
						achives.put("区县名称", getFieldValueByName("QXMC",unit));
						achives.put("房地坐落", getFieldValueByName("ZL",unit));
						if(qlrs!=null&&qlrs.size()>0){
							String qlrmc="";
							if (qlrs.size() == 1) {
								qlrmc = qlrs.get(0).getQLRMC();
							} else {
								int iii = 0;
								for (RightsHolder qlr : qlrs) {
									iii++;
									if (iii == qlrs.size()) {
										qlrmc = qlrmc + qlr.getQLRMC();
									} else {
										qlrmc = qlrmc + qlr.getQLRMC() + "、";
									}
								}
							}
							achives.put("土地使用权人", qlrmc);
						}else{
							achives.put("土地使用权人", "");
						}
						achives.put("独用土地面积", getFieldValueByName("DYTDMJ",unit));
						achives.put("分摊土地面积", getFieldValueByName("FTTDMJ",unit));
						achives.put("建筑面积", getFieldValueByName("SCJZMJ",unit));
						achives.put("专有建筑面积", getFieldValueByName("SCTNJZMJ",unit));
						achives.put("分摊建筑面积", getFieldValueByName("SCFTJZMJ",unit));
						achives.put("土地使用起始时间", StringHelper.FormatByDatetime(ql.getQLQSSJ()));
						achives.put("土地使用结束时间", StringHelper.FormatByDatetime(ql.getQLJSSJ()));
						achives.put("房地产交易价格", ql.getQDJG());
						achives.put("不动产权证号", ql.getBDCQZH());
						achives.put("登记机构", ql.getDJJG());
						achives.put("登簿人", ql.getDBR());
						achives.put("登记时间", "");
						achives.put("登记时间", StringHelper.FormatYmdhmsByDate(ql.getDJSJ()));
						achives.put("项目名称", getFieldValueByName("XMMC",unit));
						achives.put("幢号", getFieldValueByName("ZRZH",unit));
						achives.put("房号", getFieldValueByName("FH",unit));
						achives.put("总层数", getFieldValueByName("ZCS",unit));
						achives.put("总套数", "");
						achives.put("所在层", getFieldValueByName("SZC",unit));
						achives.put("起始层", getFieldValueByName("QSC",unit));
						achives.put("终止层", getFieldValueByName("ZZC",unit));
						achives.put("规划用途", ConstHelper.getNameByValue("FWYT", StringHelper.formatObject(getFieldValueByName("GHYT",unit))));
						achives.put("房屋结构", ConstHelper.getNameByValue("FWJG", StringHelper.formatObject(getFieldValueByName("FWJG",unit))));
						achives.put("房屋性质", ConstHelper.getNameByValue("FWXZ", StringHelper.formatObject(getFieldValueByName("FWXZ",unit))));
						achives.put("竣工时间", StringHelper.FormatByDatetime(getFieldValueByName("JGSJ",unit)));
						achives.put("权利其他状况", "");
						achives.put("附记", ql.getFJ());
						achives.put("权属状态", "");
						achives.put("变更时间", "");
						achives.put("变更方式", "");
						achives.put("变更记事", "");
						achives.put("注销时间", "");
						achives.put("注销记事", "");
					}else{
						continue;
					}
					list_achives.add(achives);
				}
				result.put("msg", list_achives);
			}
		}else if("certfy".equals(type)){
			List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, " QLLX IN ('23') AND id in (SELECT QLID FROM BDCS_QLR_GZ WHERE BDCQZH='"+bdcqzh+"')");
			if(qls==null||qls.size()<=0){
				result.put("success", "false");
				result.put("msg", "未查询到相应产权！");
				return result;
			}else{
				result.put("success", "true");
				List<HashMap<String,Object>> list_achives=new ArrayList<HashMap<String,Object>>();
				for(Rights ql:qls){
					BDCS_XMXX xmxx=Global.getXMXXbyXMBH(ql.getXMBH());
					if(xmxx==null||DJLX.ZXDJ.Value.equals(xmxx.getDJLX())){
						continue;
					}
					List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "DJDYID='"+ql.getDJDYID()+"' AND XMBH='"+ql.getXMBH()+"'");
					if(djdys==null||djdys.size()<=0){
						continue;
					}
					RealUnit unit=UnitTools.loadUnit(BDCDYLX.initFrom(djdys.get(0).getBDCDYLX()), DJDYLY.initFrom(djdys.get(0).getLY()), djdys.get(0).getBDCDYID());
					if(unit==null){
						continue;
					}
					SubRights fsql=RightsTools.loadSubRightsByRightsID(DJDYLY.GZ, ql.getId());
					List<RightsHolder> qlrs=RightsHolderTools.loadRightsHolders(DJDYLY.GZ, ql.getId());
					HashMap<String,Object> achives=new HashMap<String, Object>();
					if(BDCDYLX.SHYQZD.Value.equals(djdys.get(0).getBDCDYLX())||BDCDYLX.SYQZD.Value.equals(djdys.get(0).getBDCDYLX())){
					}else if(BDCDYLX.H.Value.equals(djdys.get(0).getBDCDYLX())||BDCDYLX.YCH.Value.equals(djdys.get(0).getBDCDYLX())){
					}else{
						continue;
					}
					if(qlrs!=null&&qlrs.size()>0){
						String qlrmc="";
						if (qlrs.size() == 1) {
							qlrmc = qlrs.get(0).getQLRMC();
						} else {
							int iii = 0;
							for (RightsHolder qlr : qlrs) {
								iii++;
								if (iii == qlrs.size()) {
									qlrmc = qlrmc + qlr.getQLRMC();
								} else {
									qlrmc = qlrmc + qlr.getQLRMC() + "、";
								}
							}
						}
						achives.put("权利人", qlrmc);
					}else{
						achives.put("权利人", "");
					}
					achives.put("义务人", fsql.getDYR());
					achives.put("证明书号", ql.getBDCQZH());
					List<BDCS_DJFZ> list_djfz=baseCommonDao.getDataList(BDCS_DJFZ.class, "HFZSH='"+bdcqzh+"'");
					if(list_djfz!=null&&list_djfz.size()>0){
						achives.put("发证日期", StringHelper.FormatByDatetime(list_djfz.get(0).getFZSJ()));
					}else{
						achives.put("发证日期", "");
					}
					achives.put("注销日期", "");
					
					if(BDCDYLX.SHYQZD.equals(unit.getBDCDYLX())||BDCDYLX.SYQZD.equals(unit.getBDCDYLX())){
						achives.put("土地坐落", unit.getZL());
						achives.put("土地证号", "");
						List<Rights> mainql=RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='"+ql.getDJDYID()+"' AND QLLX IN ('1','2','3','4','5','6','7','8')");
						if(mainql!=null&&mainql.size()>0){
							achives.put("土地证号", mainql.get(0).getBDCQZH());
						}
						achives.put("图号", getFieldValueByName("TFH",unit));
						achives.put("地号", "");
						achives.put("用地面积", getFieldValueByName("JZZDMJ",unit));
						achives.put("土地权属", ConstHelper.getNameByValue("QLXZ", StringHelper.formatObject(getFieldValueByName("QLXZ",unit))));
						if(BDCDYLX.SYQZD.Value.equals(djdys.get(0).getBDCDYLX())){
							achives.put("土地用途", ConstHelper.getNameByValue("TDYT", StringHelper.formatObject(getFieldValueByName("YT",unit))));
							achives.put("使用期限", "");
						}else{
							UseLand land=(UseLand)unit;
							if(land!=null&&land.getTDYTS()!=null&&land.getTDYTS().size()>0){
								String tdytmc="";
								String syqx="";
								if (land.getTDYTS().size() == 1) {
									String qssj = StringHelper.formatObject(land.getTDYTS().get(0).getQSRQ()) + "起";
									String jssj = StringHelper.formatObject(land.getTDYTS().get(0).getZZRQ()) + "止";
									syqx = qssj + jssj;
									tdytmc=land.getTDYTS().get(0).getTDYTName();
								} else {
									int iii = 0;
									for (TDYT tdyt : land.getTDYTS()) {
										iii++;
										if (!StringHelper.isEmpty(tdyt.getTDYTName())) {
											syqx = syqx + tdyt.getTDYTName() + ":";
										} else {
											continue;
										}
										String qssj = StringHelper.formatObject(tdyt.getQSRQ()) + "起";
										String jssj = StringHelper.formatObject(tdyt.getZZRQ()) + "止";

										if (iii == land.getTDYTS().size()) {
											syqx = syqx + qssj + jssj;
											tdytmc=tdytmc+tdyt.getTDYTName();
										} else {
											syqx = syqx + qssj + jssj + "、";
											tdytmc=tdytmc+tdyt.getTDYTName()+"、";
										}
									}
								}
								achives.put("土地用途", tdytmc);
								achives.put("使用期限", syqx);
							}else{
								achives.put("土地用途", "");
								achives.put("使用期限", "");
							}
						}
						achives.put("使用权类型", "");
					}else{
						achives.put("土地坐落", "");
						achives.put("土地证号", "");
						achives.put("图号", "");
						achives.put("地号", "");
						achives.put("用地面积", "");
						achives.put("土地权属", "");
						achives.put("土地用途", "");
						achives.put("使用期限", "");
						achives.put("使用权类型", "");
					}
					
					achives.put("他项权种类", "抵押权");
					achives.put("他项权面积", "");
					achives.put("抵押方式", ConstHelper.getNameByValue("DYFS", fsql.getDYFS()));
					if(DYFS.YBDY.Value.equals(fsql.getDYFS())){
						achives.put("他项权金额", fsql.getBDBZZQSE());
						achives.put("在建建筑物坐落", fsql.getZJJZWZL());
						achives.put("被担保主债权数额", fsql.getBDBZZQSE());
						achives.put("在建建筑物抵押范围", fsql.getZJJZWDYFW());
						achives.put("债务履行起始时间", StringHelper.FormatByDatetime(ql.getQLQSSJ()));
						achives.put("债务履行结束时间", StringHelper.FormatByDatetime(ql.getQLJSSJ()));
						achives.put("最高债权数额", "");
						achives.put("抵押评估价值", fsql.getDYPGJZ());
						achives.put("最高债权确定事实", "");
						achives.put("债权单位", ConstHelper.getNameByValue("JEDW", fsql.getZQDW()));
					}else{
						achives.put("他项权金额", fsql.getZGZQSE());
						achives.put("在建建筑物坐落", fsql.getZJJZWZL());
						achives.put("被担保主债权数额", "");
						achives.put("在建建筑物抵押范围", fsql.getZJJZWDYFW());
						achives.put("债务履行起始时间", StringHelper.FormatByDatetime(ql.getQLQSSJ()));
						achives.put("债务履行结束时间", StringHelper.FormatByDatetime(ql.getQLJSSJ()));
						achives.put("最高债权数额", fsql.getZGZQSE());
						achives.put("抵押评估价值", fsql.getDYPGJZ());
						achives.put("最高债权确定事实", fsql.getZGZQQDSS());
						achives.put("债权单位", ConstHelper.getNameByValue("JEDW", fsql.getZQDW()));
					}
					
					achives.put("基准地价", "");
					achives.put("抵押地价", "");
					achives.put("评估地价", "");
					achives.put("设定日期", "");
					achives.put("存续期限", "");
					achives.put("权利顺序", "");
					achives.put("其它顺序人", "");
					achives.put("变更记事", "");
					achives.put("经办人", "");
					list_achives.add(achives);
				}
				result.put("msg", list_achives);
			}
		}
		return result;
	}
	
	/** 
	 * 根据属性名获取属性值 
	 * */  
	protected Object getFieldValueByName(String fieldName, Object o) {  
		try {    
			String firstLetter = fieldName.substring(0, 1).toUpperCase();    
			String getter = "get" + firstLetter + fieldName.substring(1);    
			Method method = o.getClass().getMethod(getter, new Class[] {});    
			Object value = method.invoke(o, new Object[] {});    
			return value;    
		} catch (Exception e) {    
			return null;    
		}    
	}

	@Override
	public Map<String, Object> SetAchives_HK(String bdcqzh,
			String archives_classno, String archives_bookno, String override) {
		Map<String, Object> result=new HashMap<String, Object>();
		List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, " id in (SELECT QLID FROM BDCS_QLR_GZ WHERE BDCQZH='"+bdcqzh+"')");
		if(qls==null||qls.size()<=0){
			result.put("success", "false");
			result.put("msg", "未查询到相应产权！");
			return result;
		}
		boolean bupdate=false;
		for(Rights ql:qls){
			BDCS_QL_GZ ql_gz=(BDCS_QL_GZ)ql;
			if(ql_gz!=null){
				if("true".equals(override)){
					ql_gz.setARCHIVES_BOOKNO(archives_bookno);
					ql_gz.setARCHIVES_CLASSNO(archives_classno);
					bupdate=true;
				}else{
					if(StringHelper.isEmpty(ql_gz.getARCHIVES_BOOKNO())){
						ql_gz.setARCHIVES_BOOKNO(archives_bookno);
						bupdate=true;
					}
					if(StringHelper.isEmpty(ql_gz.getARCHIVES_CLASSNO())){
						ql_gz.setARCHIVES_CLASSNO(archives_classno);
						bupdate=true;
					}
				}
				baseCommonDao.update(ql_gz);
			}
		}
		List<Rights> qls_xz=RightsTools.loadRightsByCondition(DJDYLY.XZ, " id in (SELECT QLID FROM BDCS_QLR_XZ WHERE BDCQZH='"+bdcqzh+"')");
		if(qls_xz!=null&&qls_xz.size()>0){
			for(Rights ql:qls_xz){
				BDCS_QL_XZ ql_xz=(BDCS_QL_XZ)ql;
				if(ql_xz!=null){
					if("true".equals(override)){
						ql_xz.setARCHIVES_BOOKNO(archives_bookno);
						ql_xz.setARCHIVES_CLASSNO(archives_classno);
						bupdate=true;
					}else{
						if(StringHelper.isEmpty(ql_xz.getARCHIVES_BOOKNO())){
							ql_xz.setARCHIVES_BOOKNO(archives_bookno);
							bupdate=true;
						}
						if(StringHelper.isEmpty(ql_xz.getARCHIVES_CLASSNO())){
							ql_xz.setARCHIVES_CLASSNO(archives_classno);
							bupdate=true;
						}
					}
					baseCommonDao.update(ql_xz);
				}
			}
		}
		List<Rights> qls_ls=RightsTools.loadRightsByCondition(DJDYLY.LS, " id in (SELECT QLID FROM BDCS_QLR_LS WHERE BDCQZH='"+bdcqzh+"')");
		if(qls_ls!=null&&qls_ls.size()>0){
			for(Rights ql:qls_ls){
				BDCS_QL_LS ql_ls=(BDCS_QL_LS)ql;
				if(ql_ls!=null){
					if("true".equals(override)){
						ql_ls.setARCHIVES_BOOKNO(archives_bookno);
						ql_ls.setARCHIVES_CLASSNO(archives_classno);
						bupdate=true;
					}else{
						if(StringHelper.isEmpty(ql_ls.getARCHIVES_BOOKNO())){
							ql_ls.setARCHIVES_BOOKNO(archives_bookno);
							bupdate=true;
						}
						if(StringHelper.isEmpty(ql_ls.getARCHIVES_CLASSNO())){
							ql_ls.setARCHIVES_CLASSNO(archives_classno);
							bupdate=true;
						}
					}
					baseCommonDao.update(ql_ls);
				}
			}
		}
		if(bupdate){
			result.put("success", "true");
			result.put("msg", "更新成功！");
		}else{
			result.put("success", "false");
			result.put("msg", "档案号和案卷号已存在！");
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ResultMessage GetZlFromBDCQZH(String bdcqzh) {
		ResultMessage m=new ResultMessage();
		m.setSuccess("false");
		StringBuilder builder=new StringBuilder();
		builder.append("SELECT BDCDYID,BDCDYLX FROM BDCK.BDCS_DJDY_XZ DJDY ");
		builder.append("WHERE DJDYID IN (SELECT DJDYID FROM  BDCK.BDCS_QL_XZ QL ");
		builder.append("WHERE QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ QLR ");
		builder.append("WHERE QL.QLID=QLR.QLID AND QLR.BDCQZH='");
		builder.append(bdcqzh);
		builder.append("'))");
		List<Map> list=baseCommonDao.getDataListByFullSql(builder.toString());
		if(list==null||list.size()<=0){
			m.setMsg("未找到单元！");
			return m;
		}
		List<String> list_bdcdyid=new ArrayList<String>();
		List<String> list_zl=new ArrayList<String>();
		for(Map djdy:list){
			String bdcdyid=StringHelper.formatObject(djdy.get("BDCDYID"));
			String bdcdylx=StringHelper.formatObject(djdy.get("BDCDYLX"));
			if(StringHelper.isEmpty(bdcdyid)){
				continue;
			}
			if(StringHelper.isEmpty(bdcdylx)){
				continue;
			}
			RealUnit unit=UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.XZ, bdcdyid);
			if(unit==null){
				continue;
			}
			list_bdcdyid.add(bdcdyid);
			String zl=unit.getZL();
			if(StringHelper.isEmpty(zl)){
				continue;
			}
			list_zl.add(zl);
		}
		if(list_bdcdyid.size()<=0){
			m.setMsg("未找到单元！");
			return m;
		}
		m.setSuccess("true");
		m.setMsg(StringHelper.formatList(list_zl, ","));
		return m;
	} 
	
	
	/*
	 * 批量插入数据
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Message Batch(MultipartFile file, HttpServletRequest request) {

		List<Map> qlrlist = new ArrayList<Map>();
		String re = "";
		Long total = 0L;
		if (!file.isEmpty()) {
			String fileName = file.getOriginalFilename();
			String index = SuperHelper.GeneratePrimaryKey();
			re = index;
			String path = request.getSession().getServletContext()
					.getRealPath("/")
					+ "resources/upload/" + fileName;
			File isfile = new File(path);
			if (!isfile.exists()) {
				isfile.mkdirs();
			}
			try {
				file.transferTo(new File(path));
				FileInputStream input = new FileInputStream(path);
				Workbook workbook = null;
				if (fileName.toLowerCase().endsWith("xlsx")) {
					workbook = new XSSFWorkbook(input);
				} else if (fileName.toLowerCase().endsWith("xls")) {
					workbook = new HSSFWorkbook(input);
				}
				Sheet sheet = workbook.getSheetAt(0);
				Row head = sheet.getRow(0);
				sheet.removeRow(head);
				if(("QLRMC").equals(StringHelper.isEmpty(head.getCell(1))
						?"":head.getCell(1).getStringCellValue().trim())
						&&("ZJH").equals(StringHelper.isEmpty(head.getCell(2))
								?"":head.getCell(2).getStringCellValue().trim())){
					for (Row row : sheet) {
						String qlrmc = StringHelper.isEmpty(row.getCell(1))
								?"":row.getCell(1).getStringCellValue().trim();
						String zjh = StringHelper.isEmpty(row.getCell(2))
								?"":row.getCell(2).getStringCellValue().trim();
						if(!StringHelper.isEmpty(qlrmc)){
							Map r = new HashMap<String, String>();
							r.put("qlrmc", qlrmc);
							r.put("zjh", zjh);
							qlrlist.add(r);
						}
					}
				}
				input.close();
				(new File(path)).delete();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//去重
			HashSet<Map> hs = new HashSet<Map>(qlrlist);
			qlrlist.clear();
			qlrlist = new ArrayList<Map>(hs);
			List<BDCS_QLR_EX> exlist = new ArrayList<BDCS_QLR_EX>();
			for (Map qlr : qlrlist) {
				BDCS_QLR_EX ex = new BDCS_QLR_EX();
				String qlrmc = qlr.get("qlrmc")+"";
				String zjh = qlr.get("zjh")+"";
				ex.setQLRMC(qlrmc);
				ex.setZJH(zjh);
				ex.setINDEXNAME(index);
				exlist.add(ex);
			}
			total = (long) qlrlist.size();
			Session session = baseCommonDao.OpenSession();
			Transaction tx = session.beginTransaction();
			Long tmp = 0L;
			for (BDCS_QLR_EX qlr : exlist) {
				tmp++;
				session.save(qlr);
				if (tmp % 500 == 0) {
					session.flush();
					session.clear();
				}
			}
			session.flush();
			tx.commit();
			session.close();
		}
		Message msg = new Message();
		msg.setRows(new ArrayList<String>(Arrays.asList(re)));
		msg.setTotal(total);
		return msg;
	}

	/*
	 * 批量查询
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String BatchQuery(HttpServletRequest request)
			throws UnsupportedEncodingException, IOException {
		String indexname = RequestHelper.getParam(request, "indexname");

		Message msg = new Message();
		long count = 0;
		List<Map> listresult = null;
		/* ===============1、先获取实体对应的表名=================== */
		String unitentityName = "BDCK.BDCS_H_XZ";

		/* ===============2、再获取表名+'_'+字段名=================== */
		String dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.FH,DY.GHYT,DY.FWBM,DY.SCJZMJ,DY.SCTNJZMJ,DY.SCFTJZMJ,'031' AS BDCDYLX ,'现房' AS BDCDYLXMC ";
		String qlfieldsname = "QL.QLID,QL.BDCQZH,QL.QLLX";

		/* ===============3、构造查询语句=================== */
		/* SELECT 字段部分 */
		StringBuilder builder2 = new StringBuilder();
		builder2.append("SELECT ").append(dyfieldsname).append(",")
				.append(qlfieldsname);

		String selectstr = builder2.toString();

		/* FROM 后边的表语句 */
		StringBuilder builder = new StringBuilder();
		builder.append(" FROM {0} DY")
				.append(" LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON DY.BDCDYID=DJDY.BDCDYID")
				.append(" LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=DJDY.DJDYID  ");
		String fromstr = builder.toString();
		fromstr = MessageFormat.format(fromstr, unitentityName);

		/* WHERE 条件语句 */
		StringBuilder builder3 = new StringBuilder();
		builder3.append(" where  ");

		builder3.append(" EXISTS (SELECT 1 FROM BDCK.BDCS_QLR_XZ QLR "
				+ " LEFT JOIN BDCK.BDCS_QLR_EX EX ON EX.QLRMC=QLR.QLRMC AND EX.ZJH=QLR.ZJH "
				+ " WHERE QLR.QLID=QL.QLID AND EX.INDEXNAME= '" + indexname
				+ "')");
		String wherestr = builder3.toString();
		if (wherestr.trim().toUpperCase().endsWith(" where ")) {
			wherestr = "";
		}
		String fromSql = " FROM (SELECT QL.QLID " + fromstr + wherestr + ")";
		String fullSql = selectstr + fromstr + wherestr;

		/* 为了提高效率，先不查询符合条件的记录总数，将来改为异步获取 */
		CommonDao dao = baseCommonDao;

		count = dao.getCountByFullSql(fromSql);
		listresult = dao.getDataListByFullSql(fullSql);
		addRightsHolderInfo(listresult);
		addLimitStatus(listresult,"");
		addDyCfDetails(listresult);
		isGlzd(listresult);
		addQLLX(listresult);
		addQXGL(listresult);
		// 格式化结果中的常量值

		for (Map map : listresult) {
			if (map.containsKey("GHYT")) {
				String value = StringHelper.formatObject(StringHelper
						.isEmpty(map.get("GHYT")) ? "" : map.get("GHYT"));
				String name = "";
				if (!StringHelper.isEmpty(value)) {
					name = ConstHelper.getNameByValue("FWYT", value);
				}
				map.put("GHYTname", name);
			}
		}

		String basePath = request.getSession().getServletContext()
				.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		FileOutputStream outstream = null;
		outpath = basePath + "\\tmp\\housequeryresult.xls";
		url = request.getContextPath()
				+ "\\resources\\PDF\\tmp\\housequeryresult.xls";
		outstream = new FileOutputStream(outpath);

		String tplFullName = request.getSession().getServletContext()
				.getRealPath("/WEB-INF/jsp/wjmb/house.xls");
		InputStream input = new FileInputStream(tplFullName);
		HSSFWorkbook wb = null;// 定义一个新的工作簿
		wb = new HSSFWorkbook(input);
		Sheet sheet = wb.getSheetAt(0);
		Map<String, Integer> MapCol = new HashMap<String, Integer>();
		MapCol.put("序号", 0);
		MapCol.put("房屋座落", 1);
		MapCol.put("不动产单元号", 2);
		MapCol.put("房屋类型", 3);
		MapCol.put("房号", 4);
		MapCol.put("权利人", 5);
		MapCol.put("权利类型", 6);
		MapCol.put("证件号", 7);
		MapCol.put("权证号", 8);
		MapCol.put("规划用途", 9);
		MapCol.put("建筑面积", 10);
		MapCol.put("套内建筑面积", 11);
		MapCol.put("分摊面积", 12);
		MapCol.put("抵押状态", 13);
		MapCol.put("抵押人", 14);
		MapCol.put("抵押期限", 15);
		MapCol.put("查封状态", 16);
		MapCol.put("查封机关", 17);
		MapCol.put("查封文号", 18);
		MapCol.put("查封期限", 19);
		MapCol.put("异议状态", 20);
		MapCol.put("限制状态", 21);
		int rownum = 2;
		for (Map r : listresult) {
			HSSFRow row = (HSSFRow) sheet.createRow(rownum);
			HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
			Cell0.setCellValue(rownum - 1);
			HSSFCell Cell1 = row.createCell(MapCol.get("房屋座落"));
			Cell1.setCellValue(StringHelper.FormatByDatatype(r.get("ZL")));
			HSSFCell Cell2 = row.createCell(MapCol.get("不动产单元号"));
			Cell2.setCellValue(StringHelper.FormatByDatatype(r.get("BDCDYH")));
			HSSFCell Cell3 = row.createCell(MapCol.get("房屋类型"));
			Cell3.setCellValue(StringHelper.FormatByDatatype(r.get("BDCDYLXMC")));
			HSSFCell Cell4 = row.createCell(MapCol.get("房号"));
			Cell4.setCellValue(StringHelper.FormatByDatatype(r.get("FH")));
			HSSFCell Cell5 = row.createCell(MapCol.get("权利人"));
			Cell5.setCellValue(StringHelper.FormatByDatatype(r.get("QLRMC")));

			HSSFCell Cell6 = row.createCell(MapCol.get("证件号"));
			Cell6.setCellValue(StringHelper.FormatByDatatype(r.get("ZJHM")));
			HSSFCell Cell7 = row.createCell(MapCol.get("权证号"));
			Cell7.setCellValue(StringHelper.FormatByDatatype(r.get("BDCQZH")));
			HSSFCell Cell8 = row.createCell(MapCol.get("规划用途"));
			Cell8.setCellValue(StringHelper.FormatByDatatype(r.get("GHYTname")));
			HSSFCell Cell9 = row.createCell(MapCol.get("建筑面积"));
			Cell9.setCellValue(StringHelper.FormatByDatatype(r.get("SCJZMJ")));
			HSSFCell Cell10 = row.createCell(MapCol.get("套内建筑面积"));
			Cell10.setCellValue(StringHelper.FormatByDatatype(r.get("SCTNJZMJ")));
			HSSFCell Cell11 = row.createCell(MapCol.get("分摊面积"));
			Cell11.setCellValue(StringHelper.FormatByDatatype(r.get("SCFTJZMJ")));
			HSSFCell Cell12 = row.createCell(MapCol.get("抵押状态"));
			Cell12.setCellValue(StringHelper.FormatByDatatype(r.get("DYZT")));
			HSSFCell Cell13 = row.createCell(MapCol.get("抵押人"));
			Cell13.setCellValue(StringHelper.FormatByDatatype(r.get("DYR")));
			HSSFCell Cell14 = row.createCell(MapCol.get("抵押期限"));
			Cell14.setCellValue(StringHelper.FormatByDatatype(r.get("DYQX")));
			HSSFCell Cell15 = row.createCell(MapCol.get("查封状态"));
			Cell15.setCellValue(StringHelper.FormatByDatatype(r.get("CFZT")));
			HSSFCell Cell16 = row.createCell(MapCol.get("查封机关"));
			Cell16.setCellValue(StringHelper.FormatByDatatype(r.get("CFJG")));
			HSSFCell Cell17 = row.createCell(MapCol.get("查封文号"));
			Cell17.setCellValue(StringHelper.FormatByDatatype(r.get("CFWH")));
			HSSFCell Cell18 = row.createCell(MapCol.get("查封期限"));
			Cell18.setCellValue(StringHelper.FormatByDatatype(r.get("CFQX")));
			HSSFCell Cell19 = row.createCell(MapCol.get("异议状态"));
			Cell19.setCellValue(StringHelper.FormatByDatatype(r.get("YYZT")));
			HSSFCell Cell16_1 = row.createCell(MapCol.get("权利类型"));
			Cell16_1.setCellValue(StringHelper.FormatByDatatype(r.get("QLLX")));
			HSSFCell Cell20 = row.createCell(MapCol.get("限制状态"));
			Cell20.setCellValue(StringHelper.FormatByDatatype(r.get("XZZT")));
			rownum += 1;
		}

		wb.write(outstream);
		outstream.flush();
		outstream.close();
		outstream = null;
		return url;
	}
	
	/*
	 * 模板下载
	 */
	@SuppressWarnings("unused")
	public String GetTemptepl(HttpServletRequest request) throws IOException {
		String basePath = request.getSession().getServletContext()
				.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		url = request.getContextPath()
				+ "\\resources\\PDF\\tmp\\batch.xls";
		outpath = basePath + "\\tmp\\batch.xls";
		String tplFullName = request.getSession().getServletContext()
				.getRealPath("/WEB-INF/jsp/wjmb/Batch.xls");
		File source = new File(tplFullName);
		File dest = new File(outpath);
		FileUtils.copyFile(source, dest);
		return url;
	}

    java.sql.Connection jyConnection = null;
        
	/*
	 * 批量查询
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getWLMQFW(HttpServletRequest request)
			throws UnsupportedEncodingException, IOException {
		String jsonString="{\"FW\":";
		String sql = RequestHelper.getParam(request, "sql");
		try {
			if (jyConnection == null || jyConnection.isClosed()) {
				jyConnection = JH_DBHelper.getConnect_gxdjk();
			}
			String fwString="";
			String[] flds={"ID","FWCODE","QXDM","FWZL","ZH","DYH","QSSZC","SZC","FH","JZMJ","TNMJ","FTMJ","SYMJ", "JCNF","BZ","MPH","LFWDH","FWDH","SJYT","JZJG","GHYT"};
			PreparedStatement psPreparedStatement=jyConnection.prepareStatement(sql);
			ResultSet rSet= psPreparedStatement.executeQuery();
			while(rSet.next()){
				fwString+="{";
				for (String fld : flds) {
					fwString+="\""+fld+"\":\""+rSet.getString(fld)+"\",";
					
				}
				fwString=fwString.substring(0,fwString.length()-1);
				fwString+="},";
			}
			if(psPreparedStatement!=null){
				psPreparedStatement.close();
			}
			if(rSet!=null){
				rSet.close();
			}
			if(!fwString.equals("")){
				jsonString+="["+fwString.substring(0,fwString.length()-1)+"]}";
			}
			else {
				jsonString+="\"\"}";
			}
		} catch (Exception e1) {
		}
		finally{
			if(jyConnection!=null){
				try {
					jyConnection.close();
				} catch (SQLException e) {
				}
			}
		}
		return jsonString;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Message queryIntegrated(Map<String, String> queryvalues, int page,
			int rows, boolean iflike, String sort, String order) {
		Message msg = new Message();
		long count = 0;
		List<Map> listresult = null;
		/* ===============1、先获取实体对应的表名=================== */
		String unitentityName = "(SELECT ZL,BDCDYH,BDCDYID,SCJZMJ AS MJ, '031' AS BDCDYLX,'现房' AS BDCDYLXMC FROM BDCK.BDCS_H_XZ "
				+ "UNION ALL SELECT ZL, BDCDYH, BDCDYID,YCJZMJ AS MJ,'032' AS BDCDYLX,'期房' AS BDCDYLXMC FROM BDCK.BDCS_H_XZY "
				+ "UNION ALL SELECT ZL, BDCDYH, BDCDYID, ZDMJ AS MJ, '01' AS BDCDYLX, '所有权宗地' AS BDCDYLXMC FROM BDCK.BDCS_SYQZD_XZ "
				+ "UNION ALL SELECT ZL, BDCDYH, BDCDYID, ZDMJ AS MJ, '02' AS BDCDYLX, '使用权宗地' AS BDCDYLXMC FROM BDCK.BDCS_SHYQZD_XZ "
				+ "UNION ALL SELECT ZL, BDCDYH, BDCDYID, SYQMJ AS MJ, '05' AS BDCDYLX, '林地' AS BDCDYLXMC FROM BDCK.BDCS_SLLM_XZ "
				+ "UNION ALL SELECT ZL, BDCDYH, BDCDYID, ZHMJ AS MJ, '04' AS BDCDYLX, '宗海' AS BDCDYLXMC FROM BDCK.BDCS_ZH_XZ "
				+ "UNION ALL SELECT ZL, BDCDYH, BDCDYID, CBMJ AS MJ, '09' AS BDCDYLX, '农用地' AS BDCDYLXMC FROM BDCK.BDCS_NYD_XZ) ";
		
		/* ===============2、再获取表名+'_'+字段名=================== */
		String dyfieldsname = "DJDY.DJDYID, DY.BDCDYID, DY.BDCDYH,DY.ZL, DY.BDCDYLX AS BDCDYLX, DY.BDCDYLXMC AS BDCDYLXMC, DY.MJ";
		//String qlfieldsname = "QL.QLID,QL.BDCQZH,QL.QLLX,QL.QLQSSJ,QL.QLJSSJ ";

		/* ===============3、构造查询语句=================== */
		/* SELECT 字段部分 */
		StringBuilder builder2 = new StringBuilder();
		builder2.append("select ").append(dyfieldsname);//.append(",").append(qlfieldsname);
		
		String selectstr = builder2.toString();

		/* FROM 后边的表语句 */
		StringBuilder builder = new StringBuilder();
	
		builder.append(" from {0} DY")
					.append(" left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid");
					//.append(" left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid  ");
					
		
		String fromstr = builder.toString();
		fromstr = MessageFormat.format(fromstr, unitentityName);

		/* WHERE 条件语句 */
		StringBuilder builder3 = new StringBuilder();
		builder3.append(" where  ");
		StringBuilder qlrbuilder = new StringBuilder();
		qlrbuilder.append("DJDY.DJDYID IN (SELECT DJDYID FROM BDCK.BDCS_QL_XZ QL WHERE QL.QLLX IN('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','24','36') "
				+ "AND ql.qlid in(SELECT qlr.qlid FROM BDCK.BDCS_QLR_XZ QLR WHERE");

		boolean havecondition = false;
		boolean haveqlr = false;
		for (Entry<String, String> ent : queryvalues.entrySet()) {
			String name = ent.getKey();
			String value = ent.getValue();
			if (!StringHelper.isEmpty(value)) {

				// 权利人，权利人的条件比较特殊，where里边又套了where
				if (name.startsWith("QLR.")) {
					if (haveqlr)
						qlrbuilder.append(" and ");
					if (iflike) {
						qlrbuilder.append(" instr(" + name + ",'" + value
								+ "')>0 ");
					} else {
						if (name.equals("QLR.ZJH") && value.length() == 18) {
							String oldCard = ConverIdCard.getOldIDCard(value);
							if (oldCard.length() != 15) {
								qlrbuilder.append(" " + name + "='" + value
										+ "' ");
							} else {
								qlrbuilder.append(" (" + name + "='" + value
										+ "' or ");
								qlrbuilder.append(" " + name + "='" + oldCard
										+ "') ");
							}
						} else {
							qlrbuilder.append(" " + name + "='" + value + "' ");
						}
					}
					haveqlr = true;
					continue;
				}
				
				if (havecondition) {
					builder3.append(" and ");
				}

				if (iflike) {
						builder3.append(" instr(" + name + ",'" + value + "')>0 ");
					}
					
				 else {
						builder3.append(" " + name + "='" + value + "' ");
					}
				havecondition = true;
			}
		}
		// 有权利人查询条件
		if (!StringHelper.isEmpty(qlrbuilder.toString())) {
			if (havecondition) {
				builder3.append(" and ");
			}
			if(qlrbuilder.toString().trim().toUpperCase().endsWith("WHERE")){
				qlrbuilder.append(" 1>0 ");
			}
			builder3.append(qlrbuilder.toString() + "))" );
			havecondition = true;
		}

		
		
		String wherestr = builder3.toString();
		if (wherestr.trim().toUpperCase().endsWith("WHERE")) {
			wherestr = "";
		}
		String fromSql = " from (select 1 " + fromstr + wherestr + ")";
		String fullSql = selectstr + fromstr + wherestr;
		/* 排序 条件语句 */
		if(!StringUtils.isEmpty(sort)&&!StringUtils.isEmpty(order))
		{
		if(sort.toUpperCase().equals("ZL"))
			sort="DY.ZL";
		if(sort.toUpperCase().equals("BDCDYH"))
			sort="DY.BDCDYH";		
//		if(sort.toUpperCase().equals("BDCDYID"))
//			sort="DY.BDCDYID";
		if(sort.toUpperCase().equals("BDCDYLXMC"))
			sort="DY.BDCDYLX";		
			fullSql=fullSql+" ORDER BY "+sort+" "+order;
		}
		/* 为了提高效率，先不查询符合条件的记录总数，将来改为异步获取 */
		CommonDao dao = baseCommonDao;

		count = dao.getCountByFullSql(fromSql);
		listresult = dao.getPageDataByFullSql(fullSql, page, rows);
		if (listresult != null && listresult.size() > 0) {
			for (Map map : listresult) {
				String fulsql ="SELECT QL.QLID, QL.BDCQZH,QL.QLLX,QL.QLQSSJ,QL.QLJSSJ FROM BDCK.BDCS_QL_XZ QL WHERE QL.QLLX IN('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','24','36') AND QL.DJDYID='"+map.get("DJDYID")+"'";
				List<Map> qlList = baseCommonDao.getDataListByFullSql(fulsql);
				if (qlList != null && qlList.size() > 0) {
					map.put("QLID", qlList.get(0).get("QLID"));
					map.put("BDCQZH", qlList.get(0).get("BDCQZH"));
					if (!StringHelper.isEmpty(qlList.get(0).get("QLQSSJ")) && !StringHelper.isEmpty(qlList.get(0).get("QLJSSJ"))) {
						map.put("QLQZSJ", qlList.get(0).get("QLQSSJ").toString().substring(0, 10)+ "/" + qlList.get(0).get("QLJSSJ").toString().substring(0, 10));

					}
					
				}
				List<Map> temp = new ArrayList<Map>();
				temp.add(map);
				if("031".equals(map.get("BDCDYLX")) || "032".equals(map.get("BDCDYLX"))){
					addLimitStatus(temp,"");
				}else{
					addLimitZDStatus(temp);
					//增加限制状态
					String sqlLimit = MessageFormat
							.format("  from BDCK.BDCS_DYXZ where bdcdyid=''{0}'' and yxbz=''1'' ",
									map.get("BDCDYID"));

					long LimitCount = baseCommonDao.getCountByFullSql(sqlLimit);
					String LimitStatus = LimitCount > 0 ? map.get("BDCDYLXMC") + "已限制" : map.get("BDCDYLXMC") + "无限制";
					// 改为判断完查封 人后判断限制
					if (!(LimitCount > 0)) {
						String sqlLimiting = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_dyxz c ON b.xmbh=c.xmbh and b.bdcdyid=c.bdcdyid WHERE c.yxbz='0' and c.bdcdyid='"
								+ map.get("BDCDYID") + "' and a.sfdb='0' ";
						long countxz = baseCommonDao.getCountByFullSql(sqlLimiting);
						LimitStatus = countxz > 0 ? map.get("BDCDYLXMC") +"限制办理中" : map.get("BDCDYLXMC") +"无限制";
						
					}
					map.put("XZZT", LimitStatus);
				}
			}
		}
		addRightsHolderInfo(listresult);
		addDyCfDetails(listresult);
		msg.setTotal(count);
		msg.setRows(listresult);
		return msg;
	}

	/* 
	 * @Description: 小土地证查询
	 * @Author：赵梦帆
	 * @Data：2016年12月14日 下午12:46:09
	 * @param queryvalues
	 * @param page
	 * @param rows
	 * @param iflike
	 * @param sort
	 * @param order
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Message queryXTDZ(Map<String, String> queryvalues, Integer page,
			Integer rows, boolean iflike, String sort, String order, String lx) {
		String zdbdcdyid="";
		String fwbdcdyid="";
		if(queryvalues.containsKey("XTDZ.ZDBDCDYID")){
			zdbdcdyid=queryvalues.get("XTDZ.ZDBDCDYID");
		}
		if(queryvalues.containsKey("XTDZ.FWBDCDYID")){
			fwbdcdyid=queryvalues.get("XTDZ.FWBDCDYID");
		}
		StringBuilder selectStringBuilder = new StringBuilder("SELECT * ");
		StringBuilder fromStringBuilder = new StringBuilder(" FROM (");
		if ("XTDZ".equals(lx)) {
			fromStringBuilder
					.append("SELECT XTDZ.*, TO_NUMBER(XTDZ.YXBZ) AS ZYXBZ,'XTDZ' AS ZLX,XTDZ.BDCDYID AS ZID, "
							+ "CASE WHEN EXISTS (SELECT 1 FROM BDCK.XZCF XZCF WHERE XZCF.TDZID = XTDZ.BDCDYID AND XZCF.YXBZ = '1') THEN '1' ELSE '0' END AS CFZT, "
							+ "CASE WHEN EXISTS (SELECT 1 FROM BDCK.XZDY XZDY WHERE XZDY.TDZID = XTDZ.BDCDYID AND XZDY.YXBZ = '1') THEN '1' ELSE '0' END AS DYZT, ");
			if(!StringHelper.isEmpty(fwbdcdyid)){
				fromStringBuilder.append(" decode(XTDZ.FWBDCDYID,'"+fwbdcdyid+"',1,0) AS XHORDER ");
			}else{
				fromStringBuilder.append(" 1 AS XHORDER ");
			}
			fromStringBuilder.append("FROM BDCK.XTDZ XTDZ ) XTDZ ");
		}
		if ("XZCF".equals(lx)) {
			fromStringBuilder.append("SELECT XZCF.*, TO_NUMBER(XZCF.YXBZ) AS ZYXBZ, 'CF' AS ZLX, XZCF.TDZID AS BDCDYID, XZCF.ID AS ZID, 1 AS XHORDER "
					+ "FROM BDCK.XZCF XZCF ) XTDZ ");
		}
		if ("XZDY".equals(lx)) {
			fromStringBuilder.append("SELECT XZDY.*, TO_NUMBER(XZDY.YXBZ) AS ZYXBZ, 'DY' AS ZLX, XZDY.TDZID AS BDCDYID, XZDY.ID AS ZID, 1 AS XHORDER "
					+ "FROM BDCK.XZDY XZDY ) XTDZ ");
		}
		StringBuilder whereStringBuilder = new StringBuilder(" WHERE 1>0 ");
		if(!StringHelper.isEmpty(zdbdcdyid)&&!StringHelper.isEmpty(fwbdcdyid)){
			whereStringBuilder.append(" AND (XTDZ.ZDBDCDYID='"+zdbdcdyid+"' OR XTDZ.FWBDCDYID='"+fwbdcdyid+"') ");
		}else if(!StringHelper.isEmpty(zdbdcdyid)){
			whereStringBuilder.append(" AND (XTDZ.ZDBDCDYID='"+zdbdcdyid+"') ");
		}else if(!StringHelper.isEmpty(fwbdcdyid)){
			whereStringBuilder.append(" AND (XTDZ.FWBDCDYID='"+fwbdcdyid+"') ");
		}
		
		for(Map.Entry<String, String> entry:queryvalues.entrySet()){  
			String name = entry.getKey();
			String value = entry.getValue();
			
			if("XTDZ.ZDBDCDYID".equals(name)||"XTDZ.FWBDCDYID".equals(name)){
				continue;
			}
			
			if(iflike){
				if(!StringHelper.isEmpty(value)){
					whereStringBuilder.append(" AND INSTR(" + name + ",'" + value.trim() + "')>0 ");
				}
			}else{
				if(!StringHelper.isEmpty(value)){
					whereStringBuilder.append(" AND " + name + "='" + value.trim() + "' ");
				}
			}
		}
		whereStringBuilder.append(" ORDER BY XHORDER DESC ");
		StringBuilder fullSql = selectStringBuilder.append(fromStringBuilder).append(whereStringBuilder);
		long Count = baseCommonDao.getCountByFullSql(fromStringBuilder.append(whereStringBuilder).toString());
		List<Map> listresult = null;
		listresult = baseCommonDao.getPageDataByFullSql(fullSql.toString(), page, rows);
		if(listresult!=null&&listresult.size()>0){
			for (Map map : listresult) {
				Object qllx = map.get("SYQLX");
				if(!StringHelper.isEmpty(qllx)){
					String qllxString = ConstHelper.getNameByValue("QLLX", qllx.toString());
					map.put("SYQLX", qllxString);
				}
			}
		}
		Message msg = new Message();
		msg.setTotal(Count);
		msg.setRows(listresult);
		return msg;
	}

	/* 
	 * @Description: 更新小土地证信息
	 * @Author：赵梦帆
	 * @Data：2016年12月15日 上午10:20:37
	 * @param id
	 * @param lx
	 * @param value
	 * @return
	 */
	@Override
	public Message UpdateXTDZ(String id, String lx, String value) {
		boolean flg = false;
		if(StringHelper.isEmpty(lx)){
			BDCS_XTDZ XTD = baseCommonDao.get(BDCS_XTDZ.class, id);
			if(XTD!=null){
				XTD.setYXBZ(StringHelper.getInt(value));
				baseCommonDao.update(XTD);
			}
			flg = true;
		}else if (lx.equals("DY")) {
			BDCS_XZDY XTD = baseCommonDao.get(BDCS_XZDY.class, id);
			if(XTD!=null){
				XTD.setYXBZ(value);
				baseCommonDao.update(XTD);
			}
			flg = true;
		}else if (lx.equals("CF")) {
			BDCS_XZCF XTD = baseCommonDao.get(BDCS_XZCF.class, id);
			if(XTD!=null){
				XTD.setYXBZ(StringHelper.getDouble(value));
				baseCommonDao.update(XTD);
			}
			flg = true;
		}
		Message msg = new Message();
		msg.setSuccess(StringHelper.formatObject(flg));
		return msg;
	}

	/* 
	 * @Description: 获取小土地证产权信息
	 * @Author：赵梦帆
	 * @Data：2016年12月15日 上午10:20:55
	 * @param bdcdyid
	 * @return
	 */
	@Override
	public Map<String, Map<String, String>> GetXTDZQLInfo(String bdcdyid) {
		Map<String, Map<String, String>> qlinfo = new HashMap<String, Map<String, String>>();
		Map<String, String> ql = new HashMap<String,String>();
		BDCS_XTDZ xz = baseCommonDao.get(BDCS_XTDZ.class, bdcdyid);
		if(xz!=null){
			ql.put("MPH", xz.getMPH());
			ql.put("SFZH", xz.getSFZH());
			ql.put("ZZSJ", StringHelper.formatObject(xz.getZZSJ()));
			ql.put("LH", xz.getLH());
			ql.put("YXBZ", StringHelper.formatObject(xz.getYXBZ()).compareTo("0")>0?"有效":"无效");
			ql.put("SFFZ", xz.getSFFZ());
			ql.put("SYQMJ", StringHelper.formatObject(xz.getSYQMJ()));
			if(!StringHelper.isEmpty(xz.getSYQLX())){
				ql.put("SYQLX", ConstHelper.getNameByValue("QLLX", xz.getSYQLX()));
			}
			ql.put("FCZH", xz.getFCZH());
			ql.put("HH", xz.getHH());
			ql.put("JZMJ", StringHelper.formatObject(xz.getJZMJ()));
			ql.put("ZDMJ", StringHelper.formatObject(xz.getZDMJ()));
			ql.put("XM", xz.getXM());
			ql.put("ZL", xz.getZL());
			ql.put("TDZ", xz.getTDZ());
			ql.put("TDZH", xz.getTDZH());
			ql.put("TDYT", xz.getTDYT());
			ql.put("FZSJ", StringHelper.formatObject(xz.getFZSJ()));
			ql.put("DYH", StringHelper.formatObject(xz.getDYH()));
			ql.put("FTMJ", StringHelper.formatObject(xz.getFTMJ()));
			ql.put("FTXS", StringHelper.formatObject(xz.getFTXS()));
			ql.put("YWZLX", xz.getYWZLX());
		}
		qlinfo.put("ql",ql);
		return qlinfo;
	}

	/* 
	 * @Description: 小土地证抵押列表
	 * @Author：赵梦帆
	 * @Data：2016年12月15日 下午1:06:13
	 * @param bdcdyid
	 * @return
	 */
	@Override
	public List<BDCS_XZDY> GetXTDZdydjlist(String bdcdyid, String id) {
		if(!StringHelper.isEmpty(bdcdyid)&&!bdcdyid.toLowerCase().equals("null")){
			List<BDCS_XZDY> xzdy = baseCommonDao.getDataList(BDCS_XZDY.class, " TDZID='"+bdcdyid+"' ORDER BY BLRQ");
			return xzdy;
		}else{
			List<BDCS_XZDY> xzdy = baseCommonDao.getDataList(BDCS_XZDY.class, " ID='"+id+"' ORDER BY BLRQ");
			return xzdy;
		}
	}

	/* 
	 * @Description: 小土地证查封列表
	 * @Author：赵梦帆
	 * @Data：2016年12月15日 下午1:26:40
	 * @param bdcdyid
	 * @return
	 */
	@Override
	public List<BDCS_XZCF> GetXTDZcfdjlist(String bdcdyid, String id) {
		if(!StringHelper.isEmpty(bdcdyid)&&!bdcdyid.toLowerCase().equals("null")){
			List<BDCS_XZCF> xzcf = baseCommonDao.getDataList(BDCS_XZCF.class, " TDZID='"+bdcdyid+"' ORDER BY DJSJ");
			return xzcf;
		}else{
			List<BDCS_XZCF> xzcf = baseCommonDao.getDataList(BDCS_XZCF.class, " ID='"+id+"' ORDER BY DJSJ");
			return xzcf;
		}
	}

	/* 
	 * @Description: 小土地证抵押信息
	 * @Author：赵梦帆
	 * @Data：2016年12月15日 下午3:34:42
	 * @param id
	 * @return
	 */
	@Override
	public Map GetXTDZdydjinfo(String id) {
		BDCS_XZDY xzdy = baseCommonDao.get(BDCS_XZDY.class, id);
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		Map m = null;
		try {
			json=mapper.writeValueAsString(xzdy);
			try {
				m = mapper.readValue(json, Map.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return m;
	}

	/* 
	 * @Description: 小土地证查封信息
	 * @Author：赵梦帆
	 * @Data：2016年12月15日 下午3:35:09
	 * @param id
	 * @return
	 */
	@Override
	public Map GetXTDZcfdjinfo(String id) {
		BDCS_XZCF xzcf = baseCommonDao.get(BDCS_XZCF.class, id);
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		Map m = null;
		try {
			json=mapper.writeValueAsString(xzcf);
			try {
				m = mapper.readValue(json, Map.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return m;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Map> GetXTDZDetail(HttpServletRequest request) {
		List<Map> list=new ArrayList<Map>();
		String viewtype = request.getParameter("viewtype");
		String tdzid = request.getParameter("tdzid");
		String cfid = request.getParameter("cfid");
		String dyid = request.getParameter("dyid");
		String readonly = request.getParameter("readonly");
		if("XTDZ".equals(viewtype)){
			if(!StringHelper.isEmpty(tdzid)){
				list=baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.XTDZ WHERE BDCDYID='"+tdzid+"'");
			}
		}else if("XZDY".equals(viewtype)){
			if(StringHelper.isEmpty(dyid)){
				if(!StringHelper.isEmpty(tdzid)){
					list=baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.XZDY WHERE TDZID='"+tdzid+"'");
				}
			}else{
				list=baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.XZDY WHERE ID='"+dyid+"'");
			}
		}else if("XZCF".equals(viewtype)){
			if(StringHelper.isEmpty(cfid)){
				if(!StringHelper.isEmpty(tdzid)){
					list=baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.XZCF WHERE TDZID='"+tdzid+"'");
				}
			}else{
				list=baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.XZCF WHERE ID='"+cfid+"'");
			}
		}
		if(list==null||list.size()<=0){
			list=new ArrayList<Map>();
			Map m=new HashMap<String, String>();
			list.add(m);
		}
		for(Map m:list){
			m.put("readonly", readonly);
		}
		return list;
	}

	@Override
	public ResultMessage UpdateXTDZ(HttpServletRequest request) {
		ResultMessage msg=new ResultMessage();
		msg.setMsg("更改失败！");
		msg.setSuccess("false");
		String lx = request.getParameter("lx");
		String id = request.getParameter("id");
		if(StringHelper.isEmpty(lx)||StringHelper.isEmpty(id)){
			return msg;
		}
		if("XTDZ".equals(lx)){
			BDCS_XTDZ xtdz=baseCommonDao.get(BDCS_XTDZ.class, id);
			if(xtdz==null){
				return msg;
			}
			if(1==xtdz.getYXBZ()){
				xtdz.setYXBZ(2);
			}else{
				xtdz.setYXBZ(1);
			}
			baseCommonDao.update(xtdz);
		}else if("XZCF".equals(lx)){
			BDCS_XZCF xzcf=baseCommonDao.get(BDCS_XZCF.class, id);
			if(xzcf==null){
				return msg;
			}
			if(1==xzcf.getYXBZ()){
				xzcf.setYXBZ(2.0);
			}else{
				xzcf.setYXBZ(1.0);
			}
			baseCommonDao.update(xzcf);
		}else if("XZDY".equals(lx)){
			BDCS_XZDY xzdy=baseCommonDao.get(BDCS_XZDY.class, id);
			if(xzdy==null){
				return msg;
			}
			if("1".equals(xzdy.getYXBZ())){
				xzdy.setYXBZ("2");
			}else{
				xzdy.setYXBZ("1");
			}
			baseCommonDao.update(xzdy);
		}else if("XTDZ_show".equals(lx)){
			BDCS_XTDZ xtdz=baseCommonDao.get(BDCS_XTDZ.class, id);
			if(xtdz==null){
				return msg;
			}
			if("1".equals(StringHelper.formatDouble(xtdz.getIFSHOWTDZH()))){
				xtdz.setIFSHOWTDZH(0);
			}else{
				xtdz.setIFSHOWTDZH(1);
			}
			baseCommonDao.update(xtdz);
		}
		baseCommonDao.flush();
		msg.setMsg("更改成功！");
		msg.setSuccess("true");
		return msg;
	}

	@Override
	public Map<String, Object> QLInfoEX(String djdyid, String ql) {
		 Map<String, Object> listmap = new HashMap<String, Object>();
		if(!StringHelper.isEmpty(ql)&&!StringHelper.isEmpty(djdyid)){
			List<Map> qls = null;
			List<Map> fsqls = null;
			List<Map> qlrs = null;
			List<Map> dyqrs = null;
			String bdcdyid = "";
			List<BDCS_QL_XZ> syqls = baseCommonDao.getDataList(BDCS_QL_XZ.class, "QLLX IN ('1','2','3','4','5','6','7','8','9') AND DJDYID='"+djdyid+"'");
			if(ql.equals("dy")){
				fsqls = baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.BDCS_FSQL_XZ WHERE QLID IN (SELECT QLID FROM BDCK.BDCS_QL_XZ WHERE QLLX='23' AND DJDYID='"+djdyid+"')");
				qls = baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.BDCS_QL_XZ WHERE QLLX='23' AND DJDYID='"+djdyid+"'");
				dyqrs = baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.BDCS_QLR_XZ WHERE QLID IN (SELECT QLID FROM BDCK.BDCS_QL_XZ WHERE QLLX='23' AND DJDYID='"+djdyid+"')");
			}
			if(ql.equals("cf")){
				fsqls = baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.BDCS_FSQL_XZ WHERE QLID IN (SELECT QLID FROM BDCK.BDCS_QL_XZ WHERE QLLX='99' AND DJLX='800' AND DJDYID='"+djdyid+"')");
				qls = baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.BDCS_QL_XZ WHERE QLLX='99' AND DJLX='800' AND DJDYID='"+djdyid+"'");
			}
			Map map = null;
			ObjectMapper objectMapper = new ObjectMapper();
			String mapString = "";
			if(syqls !=null&&syqls.size()>0){
				//ql
				BDCS_QL_XZ bdcs_QL_XZ = syqls.get(0);
				try {
					mapString = objectMapper.writeValueAsString(bdcs_QL_XZ);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					map = objectMapper.readValue(mapString, Map.class);
				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				listmap.put("syq", map);
				//fsql
				BDCS_FSQL_XZ fsql = baseCommonDao.get(BDCS_FSQL_XZ.class, bdcs_QL_XZ.getFSQLID());
				try {
					mapString = objectMapper.writeValueAsString(fsql);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					map = objectMapper.readValue(mapString, Map.class);
				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				listmap.put("syqfsql", map);
				//syqlr
				qlrs = baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.BDCS_QLR_XZ WHERE QLID='"+bdcs_QL_XZ.getId()+"'");
			}
			//h
			List<BDCS_DJDY_XZ> dy = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "DJDYID='"+djdyid+"'");
			BDCS_DJDY_XZ dyh = null;
			RealUnit house = null;
			String lx = "";
			if(dy!=null&&dy.size()>0){
				dyh=dy.get(0);
				bdcdyid = dyh.getBDCDYID();
				lx = dyh.getBDCDYLX();
				house = UnitTools.loadUnit(BDCDYLX.initFrom(lx), DJDYLY.XZ, dyh.getBDCDYID());
				List<Map> tdxx = GetHInfo(bdcdyid, lx);
				if (tdxx.size() > 0 && tdxx != null) {
					listmap.put("tdxx", tdxx);
				}
			}
			
			try {
				mapString = objectMapper.writeValueAsString(house);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				map = objectMapper.readValue(mapString, Map.class);
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//查询单元限制表格
			List<BDCS_DYXZ> dyxz = baseCommonDao.getDataList(BDCS_DYXZ.class, "BDCDYID='"+bdcdyid+"'");
			if(dyxz!=null&&dyxz.size()>0) {
				BDCS_DYXZ dyxz1 = dyxz.get(0);
				if(dyxz1.getYXBZ()!=null&&"1".equals(dyxz1.getYXBZ())){
					listmap.put("xzzt", "房屋已限制");
				}else{
					listmap.put("xzzt", "房屋未限制");
				}
				
			}else {
				listmap.put("xzzt", "房屋未限制");
			}
			listmap.putAll(GetDJDYStatus(lx,djdyid,house));
			
			if(!StringHelper.isEmpty(lx)
					&&(BDCDYLX.SHYQZD.equals(BDCDYLX.initFrom(lx))
					||BDCDYLX.SYQZD.equals(BDCDYLX.initFrom(lx)))){
				listmap.put("zd", map);
			}else{
				listmap.put("house", map);
			}
			listmap.put("syqrs", qlrs);
			listmap.put("qlrs", dyqrs);
			
			listmap.put("qls", qls);
			listmap.put("fsqls", fsqls);
		}
		listmap.put("CurrentUserName", Global.getCurrentUserName());
		return listmap;
	}

	protected Map<String, String> GetDJDYStatus(String lx, String djdyid, RealUnit house) {
		List<BDCS_QL_XZ> dyqzt = null;
		List<BDCS_QL_XZ> cfqzt = null;
		List<BDCS_QL_XZ> yyqzt = null;
		Map<String,String> statusmap = new HashMap<String, String>();
		dyqzt = baseCommonDao.getDataList(BDCS_QL_XZ.class, "QLLX='23' AND DJDYID='"+djdyid+"'");
		cfqzt = baseCommonDao.getDataList(BDCS_QL_XZ.class, "QLLX='99' AND DJLX='800' AND DJDYID='"+djdyid+"'");
		yyqzt = baseCommonDao.getDataList(BDCS_QL_XZ.class, "QLLX='99' AND DJLX='600' AND DJDYID='"+djdyid+"'");
		if(BDCDYLX.H.Value.equals(lx)||BDCDYLX.YCH.Value.equals(lx)){
			if(BDCDYLX.H.Value.equals(lx)){
				if(dyqzt!=null&&dyqzt.size()>0){
					statusmap.put("dyzt", "现房已抵押");
				}else{
					statusmap.put("dyzt", "现房未抵押");
				}
				if(cfqzt!=null&&cfqzt.size()>0){
					statusmap.put("cfzt", "现房已查封");
				}else{
					statusmap.put("cfzt", "现房未查封");
				}
				if(yyqzt!=null&&yyqzt.size()>0){
					statusmap.put("yyzt", "现房有异议");
				}else{
					statusmap.put("yyzt", "现房无异议");
				}
				List<YC_SC_H_XZ> REA = new ArrayList<YC_SC_H_XZ>();
				REA = baseCommonDao.getDataList(YC_SC_H_XZ.class, " SCBDCDYID='"+house.getId()+"'");
				if(REA.size()<1){
					statusmap.put("yszt", "未关联期房");
				}else{
					List<BDCS_DJDY_XZ> YCH = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, " BDCDYID ='"+REA.get(0).getYCBDCDYID()+"'");
					if(YCH!=null&&YCH.size()>0){
						dyqzt = baseCommonDao.getDataList(BDCS_QL_XZ.class, "QLLX='23' AND DJDYID='"+YCH.get(0).getDJDYID()+"'");
						cfqzt = baseCommonDao.getDataList(BDCS_QL_XZ.class, "QLLX='99' AND DJLX='800' AND DJDYID='"+YCH.get(0).getDJDYID()+"'");
						yyqzt = baseCommonDao.getDataList(BDCS_QL_XZ.class, "QLLX='99' AND DJLX='600' AND DJDYID='"+YCH.get(0).getDJDYID()+"'");
						if(dyqzt!=null&&dyqzt.size()>0){
							statusmap.put("dyzt", statusmap.get("dyzt")+"、期房已抵押");
						}else{
							statusmap.put("dyzt", statusmap.get("dyzt")+"、期房未抵押");
						}
						if(cfqzt!=null&&cfqzt.size()>0){
							statusmap.put("cfzt", statusmap.get("cfzt")+"、期房已查封");
						}else{
							statusmap.put("cfzt", statusmap.get("cfzt")+"、期房未查封");
						}
						if(cfqzt!=null&&cfqzt.size()>0){
							statusmap.put("yyzt", statusmap.get("yyzt")+"、期房有异议");
						}else{
							statusmap.put("yyzt", statusmap.get("yyzt")+"、期房无异议");
						}
					}
				}
			}
			if(BDCDYLX.YCH.Value.equals(lx)){
				if(dyqzt!=null&&dyqzt.size()>0){
					statusmap.put("dyzt", "期房已抵押");
				}else{
					statusmap.put("dyzt", "期房未抵押");
				}
				if(cfqzt!=null&&cfqzt.size()>0){
					statusmap.put("cfzt", "期房已查封");
				}else{
					statusmap.put("cfzt", "期房未查封");
				}
				if(yyqzt!=null&&yyqzt.size()>0){
					statusmap.put("yyzt", "期房有异议");
				}else{
					statusmap.put("yyzt", "期房无异议");
				}
				List<YC_SC_H_XZ> REA = new ArrayList<YC_SC_H_XZ>();
				REA = baseCommonDao.getDataList(YC_SC_H_XZ.class, " YCBDCDYID='"+house.getId()+"'");
				if(REA.size()<1){
					statusmap.put("yszt", "未关联现房");
				}else{
					List<BDCS_DJDY_XZ> SCH = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, " BDCDYID ='"+REA.get(0).getSCBDCDYID()+"'");
					if(SCH!=null&&SCH.size()>0){
						dyqzt = baseCommonDao.getDataList(BDCS_QL_XZ.class, "QLLX='23' AND DJDYID='"+SCH.get(0).getDJDYID()+"'");
						cfqzt = baseCommonDao.getDataList(BDCS_QL_XZ.class, "QLLX='99' AND DJLX='800' AND DJDYID='"+SCH.get(0).getDJDYID()+"'");
						yyqzt = baseCommonDao.getDataList(BDCS_QL_XZ.class, "QLLX='99' AND DJLX='600' AND DJDYID='"+SCH.get(0).getDJDYID()+"'");
						if(dyqzt!=null&&dyqzt.size()>0){
							statusmap.put("dyzt", statusmap.get("dyzt")+"、现房已抵押");
						}else{
							statusmap.put("dyzt", statusmap.get("dyzt")+"、现房未抵押");
						}
						if(cfqzt!=null&&cfqzt.size()>0){
							statusmap.put("cfzt", statusmap.get("cfzt")+"、现房已查封");
						}else{
							statusmap.put("cfzt", statusmap.get("cfzt")+"、现房未查封");
						}
						if(yyqzt!=null&&yyqzt.size()>0){
							statusmap.put("yyzt", statusmap.get("yyzt")+"、现房有异议");
						}else{
							statusmap.put("yyzt", statusmap.get("yyzt")+"、现房无异议");
						}
					}
				}
			}
		
		}else{
			if(dyqzt!=null&&dyqzt.size()>0){
				statusmap.put("dyzt", "已抵押");
			}else{
				statusmap.put("dyzt", "未抵押");
			}
			if(cfqzt!=null&&cfqzt.size()>0){
				statusmap.put("cfzt", "已查封");
			}else{
				statusmap.put("cfzt", "未查封");
			}
			if(yyqzt!=null&&yyqzt.size()>0){
				statusmap.put("yyzt", "有异议");
			}else{
				statusmap.put("yyzt", "无异议");
			}
		}
		return statusmap;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public QLInfo GetQLInfo_XZEX(String bdcdyid) {
		QLInfo qlinfo = new QLInfo();
		Rights ql = getQLInfo_XZEX(bdcdyid);
		qlinfo.setql(ql);
		if (ql != null) {
			SubRights fsql = RightsTools.loadSubRightsByRightsID(DJDYLY.XZ,
					ql.getId());
			qlinfo.setfsql(fsql);
			List<RightsHolder> qlrlist = RightsHolderTools.loadRightsHolders(
					DJDYLY.XZ, ql.getId());
			qlinfo.setqlrlist(qlrlist);
		}
		List<BDCS_DJDY_LS> djdys = baseCommonDao.getDataList(BDCS_DJDY_LS.class, "BDCDYID='" + bdcdyid + "'");
		if(djdys!=null&&djdys.size()>0){
				List<Map> lsqlids = baseCommonDao.getDataListByFullSql("SELECT QLID FROM BDCK.BDCS_QL_LS LS WHERE "
						+ " LS.QLLX IN ('4', '6', '8','24') AND NOT EXISTS "
						+ "(SELECT 1 FROM BDCK.BDCS_QL_XZ XZ WHERE XZ.QLID = LS.QLID) AND LS.DJDYID='"+djdys.get(0).getDJDYID()+"'");
				if(lsqlids!=null&&lsqlids.size()>0){
					Map<String,String> qlr_id = new HashMap<String,String>();
					List<Map> lsql = new ArrayList<Map>();
					for (Map map : lsqlids) {
						CaseInsensitiveMap m = new CaseInsensitiveMap(map); 
						List<RightsHolder> qlrlist = RightsHolderTools.loadRightsHolders(DJDYLY.LS, m.get("QLID").toString());
						if(qlrlist!=null&&qlrlist.size()>0){
							for (RightsHolder rightsHolder : qlrlist) {
								if(qlr_id.containsKey(rightsHolder.getQLID())){
									qlr_id.put(rightsHolder.getQLID(),qlr_id.get(rightsHolder.getQLID())+"、"+rightsHolder.getQLRMC());
								}else{
									qlr_id.put(rightsHolder.getQLID(), rightsHolder.getQLRMC());
								}
								lsql.add(qlr_id);
							}
						}
					}
					qlinfo.setLsql(qlr_id);
				}
		}
		return qlinfo;
	}

	protected Rights getQLInfo_XZEX(String bdcdyid) {
		Rights ql = null;
		//处理变更登记（单元未变）时的证明信息不显示问题
			List<BDCS_DJDY_GZ> djdys_gz = baseCommonDao.getDataList(
					BDCS_DJDY_GZ.class, "BDCDYID='" + bdcdyid + "'");
			String djdyid_xz = "";
			if (djdys_gz != null && djdys_gz.size() > 0) {
				djdyid_xz = djdys_gz.get(0).getDJDYID();
			}
			List<BDCS_DJDY_XZ> djdys_xz = baseCommonDao.getDataList(
					BDCS_DJDY_XZ.class, "DJDYID='" + djdyid_xz + "'");
			String bdcdyid_xz = "";
			if (djdys_xz != null && djdys_xz.size() > 0) {
				bdcdyid_xz = djdys_xz.get(0).getBDCDYID();
			}
			List<BDCS_DJDY_XZ> djdys = baseCommonDao.getDataList(
					BDCS_DJDY_XZ.class, "BDCDYID='" + bdcdyid_xz + "'");
			if(djdys == null || djdys.size() == 0){
				djdys = baseCommonDao.getDataList(
						BDCS_DJDY_XZ.class, "BDCDYID='" + bdcdyid + "'");
			}
		if (djdys != null && djdys.size() > 0) {
			String djdyid = djdys.get(0).getDJDYID();
			BDCDYLX lx = BDCDYLX.initFrom(djdys.get(0).getBDCDYLX());
			List<Rights> qls = null;
			if (BDCDYLX.H.equals(lx) || BDCDYLX.YCH.equals(lx) 
					|| BDCDYLX.SHYQZD.equals(lx) || BDCDYLX.SYQZD.equals(lx)) {
				qls = RightsTools.loadRightsByCondition(DJDYLY.XZ,
						"DJDYID='" + djdyid + "' and QLLX IN ('1','2','3','4','5','6','7','8','9')");
				if (qls != null && qls.size() > 0) {
					ql = qls.get(0);
				}
			}
			if (BDCDYLX.YCH.equals(lx)&&(qls==null||qls.size()<1)) {
				qls = RightsTools.loadRightsByCondition(DJDYLY.XZ,
						"DJDYID='" + djdyid + "' and QLLX IN ('23')");
				if (qls != null && qls.size() > 0) {
					ql = qls.get(0);
				}
			}

		}
		return ql;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void addBDCDYZT(List<Map> result) {
		if (result != null && result.size() > 0) {
			for (Map map : result) {
				if(!StringUtils.isEmpty(map.get("BDCDYID"))){
					String isexsit_zx_sql = MessageFormat.format(" FROM (SELECT BDCDYID FROM BDCK.BDCS_H_XZ "
							+ "UNION ALL "
							+ "SELECT BDCDYID FROM BDCK.BDCS_H_XZY) DY "
							+ " WHERE DY.BDCDYID=''{0}'' ",
							map.get("BDCDYID"));
					Long count_zx=baseCommonDao.getCountByFullSql(isexsit_zx_sql);
					if (count_zx > 0) {
						map.put("BDCDYZT", "现状");
					}else {
						map.put("BDCDYZT", "历史");
					}
				}
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void addQLZT(List<Map> result) {
		if (result != null && result.size() > 0) {
			for (Map map : result) {
				if(!StringUtils.isEmpty(map.get("QLID"))){
					String quanlizt_sql = MessageFormat.format(" FROM (SELECT QLID FROM BDCK.BDCS_QL_XZ ) QL WHERE QL.QLID=''{0}'' ",map.get("QLID"));
					Long count_qlzt=baseCommonDao.getCountByFullSql(quanlizt_sql);
					if (count_qlzt > 0) {
						map.put("QLZT", "现状");
					}else {
						map.put("QLZT", "历史");
					}
			}
			}
		}
	}
    //添加注销状态
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void addZXZT(List<Map> result) {
		if (result != null && result.size() > 0) {
			for (Map map : result) {
				if (map.containsKey("DJDYID")) {
					String djdyid = (String) map.get("DJDYID");
					
					// 注销登记
					String sqlzxdj = MessageFormat.format(
							" from BDCK.BDCS_QL_LS where djdyid=''{0}'' and djlx=''400''",
							djdyid);
					long zxdjCount = baseCommonDao.getCountByFullSql(sqlzxdj);
					String zxdjStatus = zxdjCount > 0 ? "已注销" : "未注销";
					if(zxdjCount > 0){
						List<Map> list = baseCommonDao.getDataListByFullSql("select djsj" + sqlzxdj + "order by djsj desc");
						map.put("DJSJ", list.get(0).get("DJSJ"));
					}
					// 注销办理中的
					if (!(zxdjCount > 0)) {
						String sqlZXing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='400' and c.djdyid='"
								+ djdyid + "'";
						long count = baseCommonDao.getCountByFullSql(sqlZXing);
						zxdjStatus = count > 0 ? "注销中" : "未注销";
					}
					
					String zxStatus ="";
					if ("已注销".equals(zxdjStatus) ) {
						zxStatus = "已注销";
					}else if ("注销中".equals(zxdjStatus) ) {
						zxStatus = "注销中";
					}else {
						zxStatus = "未注销";
					}
					map.put("ZXZT", "权利"+zxStatus);
				}
			}
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, Object> GetFWinfo(String xmbh, String djdyid, String bdcdyid, String qlid, String showlsh, String logid) {
		/**
		 * 单元所有的信息
		 */
		Map<String,Object> info = new HashMap<String, Object>();
		/**
		 * 项目信息
		 */
		ProjectInfo xminfo = ProjectHelper.GetPrjInfoByXMBH(xmbh);
		info.put("xminfo", xminfo);
		/**
		 * 申请人
		 */
		String sql = ProjectHelper.GetXMBHCondition(xmbh);
		List<BDCS_SQR> sqr = baseCommonDao.getDataList(BDCS_SQR.class, sql + " ORDER BY SXH");
		List<Map> sqrinfo = new ArrayList<Map>();
		if(sqr!=null&&sqr.size()>0){
			String value = null;
			String valuename = null;
			String Json = null;
			Map<String, String> maps = null;
			for (BDCS_SQR bdcs_SQR : sqr) {
				ObjectMapper mapper = new ObjectMapper();
				try {
					Json = mapper.writeValueAsString(bdcs_SQR);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
				try {
					maps = mapper.readValue(Json, Map.class);
					if(maps.containsKey("zjlx")){
						value = maps.get("zjlx");
						valuename = ConstHelper.getNameByValue("ZJLX", value);
						maps.put("zjlx", valuename);
					}
				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sqrinfo.add(maps);
			}
		}
		info.put("sqrinfo", sqrinfo);
	    if(!StringHelper.isEmpty(bdcdyid)){
			/**
			 * 单元信息
			 */
			RealUnit unitinfo = UnitTools.loadUnit(BDCDYLX.H, DJDYLY.XZ, bdcdyid);
			if(unitinfo==null){
				unitinfo = UnitTools.loadUnit(BDCDYLX.YCH, DJDYLY.XZ, bdcdyid);
			}
			info.put("unitinfo", unitinfo);
			if(unitinfo!=null){
				String XZQHDM=ConfigHelper.getNameByValue("XZQHDM");
				if("6501".equals(XZQHDM.length()>4?XZQHDM.substring(0,4):XZQHDM)){
					String cQLY = "";
					if(BDCDYLX.H.equals(unitinfo.getBDCDYLX())){
						BDCS_H_XZ h = (BDCS_H_XZ) unitinfo;
						cQLY = ConstHelper.getNameByValue("CQLY", h.getCQLY());
						if(!StringHelper.isEmpty(cQLY)){
							h.setCQLY(cQLY);
							info.put("unitinfo", h);
						}
					}
					if(BDCDYLX.YCH.equals(unitinfo.getBDCDYLX())){
						BDCS_H_XZY h = (BDCS_H_XZY) unitinfo;
						cQLY = ConstHelper.getNameByValue("CQLY", h.getCQLY());
						if(!StringHelper.isEmpty(cQLY)){
							h.setCQLY(cQLY);
							info.put("unitinfo", h);
						}
					}
				}
			}
			/**
			 * 产权信息
			 */
			QLInfo qlinfo = GetQLInfo_XZ(bdcdyid);
			info.put("qlinfo", qlinfo);
			/**
			 * 抵押信息
			 */
			List<List<Map>> dyinfo = GetDYDJList_XZ(bdcdyid);
			String dyq_qlid = "";
			Rights dyq = null;
			SubRights dyqsub = null;
			for (List<Map> list : dyinfo) {
				for (Map map : list) {
					dyq_qlid = map.get("QLID").toString();
					dyq = RightsTools.loadRights(DJDYLY.XZ, dyq_qlid);
					map.put("dyinfo", dyq);
					dyqsub = RightsTools.loadSubRightsByRightsID(DJDYLY.XZ, dyq_qlid);
					map.put("dysubinfo", dyqsub);
					List<RightsHolder> dyqlr = RightsHolderTools.loadRightsHolders(DJDYLY.XZ, dyq_qlid);
					map.put("dyqlr", dyqlr);
				}
		    }
		    info.put("dyinfo", dyinfo);
			/**
			 * 查封信息
			 */
			List<List<Map>> cfinfo = GetCFDJList_XZ(bdcdyid);
			String cfq_qlid = "";
			Rights cfq = null;
			SubRights cfqsub = null;
			for (List<Map> list : cfinfo) {
				for (Map map : list) {
					cfq_qlid = map.get("QLID").toString();
					cfq = RightsTools.loadRights(DJDYLY.XZ, cfq_qlid);
					map.put("cfinfo", cfq);
					cfqsub = RightsTools.loadSubRightsByRightsID(DJDYLY.XZ, cfq_qlid);
					map.put("cfsubinfo", cfqsub);
					String cflxname = ConstHelper.getNameByValue("CFLX", cfqsub.getCFLX());
					map.put("cflxname", cflxname);
				}
			}
			info.put("cfinfo", cfinfo);
			/**
			 * 限制信息
			 */
			List<BDCS_DYXZ> xzinfo = GetDYXZList_XZ(bdcdyid);
			info.put("xzinfo", xzinfo);
	    }
		
		/**
		 * 业务流水号 bar
		 */
		 if(!StringHelper.isEmpty(showlsh)){
			info.put("dylsh", getLSH(showlsh,logid));
		}
		
		if(!StringHelper.isEmpty(xminfo)&&!StringHelper.isEmpty(xminfo.getYwlsh())){
			BufferedImage img = BarcodeUtil.createBarcodeStream(xminfo.getYwlsh(), "Code128", 240, 60, true);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			try {
				ImageIO.write(img, "png", out);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BASE64Encoder encoder = new BASE64Encoder();  
			String imgString = "";
			if(!StringHelper.isEmpty(out)){
				imgString = encoder.encode(out.toByteArray());
			}
			info.put("img", imgString);
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		info.put("cdsj", df.format(new Date()));
		
		return info;
	}
	
	
	public String getLSH(String lshlx, String logid) {
		//首先判断当前记录是否存在流水号
		BDCS_CX_LOG log = baseCommonDao.get(BDCS_CX_LOG.class, logid);
		String lsh = ""; 
		if(log==null)
			return null;
		if(!StringHelper.isEmpty(log.getLSH())){
			lsh = log.getLSH();
		}else{
			//没有流水号则调用存储过程生产流水号
			List<ProcedureParam> params=new ArrayList<ProcedureParam>();
			ProcedureParam param=new ProcedureParam();
			param.setFieldtype(Types.INTEGER);
			param.setParamtype("out");
			param.setName("SH");
			param.setSxh(1);
			params.add(param);
			param=new ProcedureParam();
			param.setFieldtype(Types.NVARCHAR);
			param.setParamtype("in");
			param.setName("LID");
			param.setValue(logid);
			param.setSxh(2);
			params.add(param);
			param=new ProcedureParam();
			param.setFieldtype(Types.NVARCHAR);
			param.setParamtype("in");
			param.setName("LLX");
			param.setValue(lshlx);
			param.setSxh(3);
			params.add(param);
			HashMap<String,Object> info=ProcedureTools.executeProcedure(params, "GETCXXXLSH", "BDCK");
			lsh = StringHelper.formatObject(info.get("SH"));
		}
		return lsh;
	}
	
	@Override
	public void delLog(String id) {
		//BDCS_CX_LOG
		baseCommonDao.delete(BDCS_CX_LOG.class, id);
		baseCommonDao.flush();
	}
	
    /**
	 * 根据模版里的权利人信息获取该权利人在不动产登记的的房屋信息
	 * @author hks
	 * @Date 2016/6/27 16:42
	 * @param queryvalues
	 * @param iflike
	 * @param fwzt
	 * @param sort
	 * @param order
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Message queryHouseByQlrmcAndZjhm(Map<String, String> queryvalues, boolean iflike, String fwzt,String cdyt,String sort) {
		Message msg = new Message();
		long count = 0;
		List<Map> listresult = null;
		String cxzt=queryvalues.get("CXZT");
		queryvalues.remove("CXZT");
		//结果筛选——是否只显示产权
		String jgsx=queryvalues.get("JGSX");
		queryvalues.remove("JGSX");
		//区域筛选——只查询有权限查询的区域
		String searchstates=queryvalues.get("SEARCHSTATES");
		queryvalues.remove("SEARCHSTATES");
		
		String fsqlcfwh=queryvalues.get("FSQL.CFWH");
		/* ===============1、先获取实体对应的表名=================== */
		
		String unitentityName = "BDCK.BDCS_H_XZ";
		if ("2".equals(cxzt)) {
			unitentityName = "BDCK.BDCS_H_LS";
		}

		/* ===============2、再获取表名+'_'+字段名=================== */
		String dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.FH,DY.GHYT,DY.FWBM,DY.SCJZMJ,DY.SCTNJZMJ,DY.SCFTJZMJ,'031' AS BDCDYLX ,'现房' AS BDCDYLXMC ";
		String qlfieldsname = "XM.YWLSH,QL.QLID,QL.BDCQZH,QL.QLLX,QL.DJSJ,QLR.GYFS,QLR.QLBL";
		String fsqlfieldsname = "FSQL.FSQLID,FSQL.CFWH";

		if (fwzt != null && fwzt.equals("2")) {
			unitentityName = "BDCK.BDCS_H_XZY";
			if ("2".equals(cxzt)) {
				unitentityName = "BDCK.BDCS_H_LSY";
			}
			dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.FH,DY.GHYT,DY.FWBM,DY.YCJZMJ AS SCJZMJ,DY.YCTNJZMJ AS SCTNJZMJ,DY.YCFTJZMJ AS SCFTJZMJ,'032' AS BDCDYLX,'期房' AS BDCDYLXMC,DY.ZRZH,DY.SEARCHSTATE ";
		}

		// 统一期现房 2015年10月28日
		if (fwzt != null && fwzt.equals("3")) {
			unitentityName = "(SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ ,'031' AS BDCDYLX ,'现房' AS BDCDYLXMC, ZRZH, SEARCHSTATE  FROM BDCK.BDCS_H_XZ UNION ALL SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ  ,'032' AS BDCDYLX ,'期房' AS BDCDYLXMC, ZRZH, SEARCHSTATE  FROM BDCK.BDCS_H_XZY)";
			
			if ("2".equals(cxzt)) {
				unitentityName = "(SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ ,'031' AS BDCDYLX ,'现房' AS BDCDYLXMC, ZRZH, SEARCHSTATE FROM BDCK.BDCS_H_LS UNION ALL SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ  ,'032' AS BDCDYLX ,'期房' AS BDCDYLXMC , ZRZH, SEARCHSTATE FROM BDCK.BDCS_H_LSY)";
		    
			}
			dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.FH,DY.GHYT,DY.JZMJ AS SCJZMJ,DY.TNJZMJ AS SCTNJZMJ,DY.FTJZMJ AS SCFTJZMJ,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC,DY.ZRZH,DY.SEARCHSTATE ";
			
		}
		/* ===============3、构造查询语句=================== */
		/* SELECT 字段部分 */
		StringBuilder builder2 = new StringBuilder();
		builder2.append("select ").append(dyfieldsname).append(",")
				.append(qlfieldsname);
		if(!StringHelper.isEmpty(fsqlcfwh)){
			builder2.append(",").append(fsqlfieldsname);
		}
		String selectstr = builder2.toString();

		/* FROM 后边的表语句 */
		StringBuilder builder = new StringBuilder();
		if ("2".equals(cxzt)) {
			builder.append(" from {0} DY")
					.append(" left join BDCK.BDCS_DJDY_LS DJDY on dy.bdcdyid=djdy.bdcdyid")
					.append(" left join BDCK.bdcs_ql_ls ql on ql.djdyid=djdy.djdyid  ")
					.append(" left join BDCK.BDCS_XMXX XM on XM.XMBH = QL.XMBH");
					
		}else{
			builder.append(" from {0} DY")
					.append(" left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid")
					.append(" left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid and ql.qllx = '4'")
					.append(" left join BDCK.bdcs_fsql_xz fsql on fsql.djdyid=djdy.djdyid ")
					.append(" left join BDCK.BDCS_XMXX XM on XM.XMBH = QL.XMBH")
					.append(" left join BDCK.bdcs_qlr_xz qlr on qlr.qlid=ql.qlid ");
		}
		if(!StringHelper.isEmpty(fsqlcfwh)){
			builder.append(" left join BDCK.bdcs_fsql_xz fsql on ql.fsqlid=fsql.fsqlid  ");
		}
		String fromstr = builder.toString();
		fromstr = MessageFormat.format(fromstr, unitentityName);

		/* WHERE 条件语句 */
		StringBuilder builder3 = new StringBuilder();
		builder3.append(" where ");
		StringBuilder qlrbuilder = new StringBuilder();
		StringBuilder dyrbuilder = new StringBuilder();
		boolean havecondition = false;
		boolean haveqlr = false;
		boolean havedyr = false;  
		for (Entry<String, String> ent : queryvalues.entrySet()) {
			String name = ent.getKey();
			String value = ent.getValue();
			if (!StringHelper.isEmpty(value)) {
				// 抵押状态和查封状态，为‘0’的时候表示不过滤，查询全部
				if ((name.equals("CFZT") && value.equals("0"))
						|| (name.equals("DYZT") && value.equals("0"))) {
					continue;
				}
                if ((name.equals("SWCDBZ") && value.equals("cd"))) {
					continue;
				}
				if(name.equals("CDYT") && value.equals("1")){
					continue;
				}
				if(name.equals("BH") || name.equals("BZ")){
					continue;
				}
				// 权利人，权利人的条件比较特殊，where里边又套了where
				if (name.startsWith("QLR.")) {
					if (haveqlr)
						qlrbuilder.append(" and ");
					if (iflike) {
						qlrbuilder.append(" instr(" + name + ",'" + value.trim()
								+ "')>0 ");
					} else {
						if (name.equals("QLR.ZJH") && value.length() == 18) {
							String oldCard = ConverIdCard.getOldIDCard(value);
							if (oldCard.length() != 15) {
								qlrbuilder.append(" " + name + "='" + value.trim()
										+ "' ");
							} else {
								qlrbuilder.append(" (" + name + "='" + value.trim()
										+ "' or ");
								qlrbuilder.append(" " + name + "='" + oldCard.trim()
										+ "') ");
							}
						} else {
							qlrbuilder.append(" " + name + " like '%"+value+"%' ");
						}
					}
					haveqlr = true;
					continue;
				}
				// 抵押人判断
				if (name.startsWith("DYR.")) {
					if (havedyr)
						dyrbuilder.append(" and ");
					if (iflike) {
						dyrbuilder.append(" instr(" + name + ",'" + value
								+ "')>0 ");
					} else {
						dyrbuilder.append(" " + name + "='" + value + "' ");
					}
					havedyr = true;
					continue;
				}
				if (havecondition) {
					builder3.append(" and ");
				}

				// 抵押状态
				if (name.equals("DYZT")) {
					if (value.equals("1")) {
						if ("2".equals(cxzt)) {
							builder3.append(" djdy.djdyid NOT IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_LS DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
						}else{
							builder3.append(" djdy.djdyid NOT IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_XZ DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
						}
					} else {
						if ("2".equals(cxzt)) {
							builder3.append(" djdy.djdyid IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_LS DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
						}else{
							builder3.append(" djdy.djdyid IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_XZ DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
						}
					}
					havecondition = true;
					continue;
				}
				// 查封状态
				if (name.equals("CFZT")) {
					if (value.equals("1")) {
						if ("2".equals(cxzt)) {
							builder3.append("  djdy.DJDYID NOT IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_LS CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99') ");
						}else{
							builder3.append("  djdy.DJDYID NOT IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_XZ CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99') ");
						}
					} else {
						if ("2".equals(cxzt)) {
							builder3.append("  djdy.DJDYID IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_LS CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99' AND CF.DJLX='800') ");
						
						}else{
							builder3.append("  djdy.DJDYID IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_XZ CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99' AND CF.DJLX='800') ");
						
						}
					}
					havecondition = true;
					continue;
				}
				// 异议状态
				if (name.equals("YYZT")) {
					if (value.equals("1")) {
						if ("2".equals( cxzt)) {
							builder3.append("  djdy.DJDYID NOT IN (SELECT YY.DJDYID FROM BDCK.BDCS_QL_LS YY WHERE YY.DJDYID=DJDYID AND YY.djlx = '600') ");
						}else{
							builder3.append("  djdy.DJDYID NOT IN (SELECT YY.DJDYID FROM BDCK.BDCS_QL_XZ YY WHERE YY.DJDYID=DJDYID AND YY.djlx = '600') ");
						}
					} else {
						if ("2".equals(cxzt)) {
							builder3.append("  djdy.DJDYID IN (SELECT YY.DJDYID FROM BDCK.BDCS_QL_LS YY WHERE YY.DJDYID=DJDYID AND  YY.djlx = '600') ");
						}else{
							builder3.append("  djdy.DJDYID IN (SELECT YY.DJDYID FROM BDCK.BDCS_QL_XZ YY WHERE YY.DJDYID=DJDYID AND  YY.djlx = '600') ");
						}
					}
					havecondition = true;
					continue;
				}

				if (iflike) {
					if (name.contains("BDCQZH")) {
						// 验证不动产权证号是否为纯数字的
						boolean flag = StringHelper.isNumericByString(value);
						if (flag) {
							builder3.append(" instr(" + name + ",'" + value + "')>0 ");
						} else {
							builder3.append(" instr(upper(" + name + "),'" + value.toUpperCase() + "')>0 ");
						}
					}else if (name.equals("DJSJ_Q")) {
						builder3.append("  QL.DJSJ >=to_date('"+queryvalues.get("DJSJ_Q")+"','yyyy-mm-dd') ");
					}else if (name.equals("DJSJ_Z")) {
						builder3.append("   QL.DJSJ <=(to_date('"+queryvalues.get("DJSJ_Z")+"','yyyy-mm-dd')+1) ");
					}else{
					    builder3.append(" instr(" + name + ",'" + value + "')>0 ");
					}
					
					
					
				} else {
					/*
					 * 如果通过不动产权证查询，且是精确查询时,
					 * 其中如果不动产权证号是否纯数字的，直接在权利人表中通过BDCQZHXH条件查询，
					 * 否则从权利表中通过BDCQZH条件查询
					 */
					if (name.contains("BDCQZH")) {
						String cdbz = queryvalues.get("SWCDBZ");//
						// 验证不动产权证号是否为纯数字的
						boolean flag = StringHelper.isNumericByString(value);
						if (flag) {
							// 判断是否已经有查询权利人的条件了
							if (haveqlr) {
								qlrbuilder.append(" and ");
							}
							qlrbuilder.append(" QLR.BDCQZHXH " + "='" + value
									+ "' ");
							haveqlr = true;
							continue;
						} else {
							if(cdbz != null && !cdbz.equals("") && cdbz.equals("cd")){
								builder3.append(" " + name + " like '%" + value + "%' ");
							}else{
								builder3.append(" " + name + " ='" + value + "' ");
							}
						}
					}else if (name.equals("DJSJ_Q")) {
						builder3.append("  QL.DJSJ >=to_date('"+queryvalues.get("DJSJ_Q")+"','yyyy-mm-dd') ");
					}else if (name.equals("DJSJ_Z")) {
						builder3.append("   QL.DJSJ <=(to_date('"+queryvalues.get("DJSJ_Z")+"','yyyy-mm-dd')+1) ");
					}else {
						builder3.append(" " + name + "='" + value + "' ");
					}
				}
				havecondition = true;
			}
		}
		// 有权利人查询条件
		if (!StringHelper.isEmpty(qlrbuilder.toString())) {
			if (havecondition) {
				builder3.append(" and ");
			}
			if(cdyt != null && cdyt.equals("1")){
				builder3.append("EXISTS (SELECT 1 FROM BDCK.BDCS_QLR_XZ QLR WHERE QLR.QLID=QL.QLID AND QLR.BDCQZH LIKE '%不动产%' AND"
						+ qlrbuilder.toString() + ")");
			}else if(cdyt != null && cdyt.equals("0")){
				builder3.append("EXISTS (SELECT 1 FROM BDCK.BDCS_QLR_XZ QLR WHERE QLR.QLID=QL.QLID AND"
						+ qlrbuilder.toString() + ")");
			}
			havecondition = true;
		}
		
		String wherestr = builder3.toString();
		if (wherestr.trim().toUpperCase().endsWith("WHERE")) {
			wherestr = "";
		}
		String fromSql = " from (select ql.qlid " + fromstr + wherestr + ")";
		String fullSql = selectstr + fromstr + wherestr;
		/* 排序 条件语句 */
		
		if(!StringUtils.isEmpty(sort))
		{
		if(sort.toUpperCase().equals("ZL"))
			sort="DY.ZL";
		if(sort.toUpperCase().equals("BDCDYH"))
			sort="DY.BDCDYH";
		/*if(sort.toUpperCase().equals("QLRMC"))
			sort="QLR.QLRMC";
		if(sort.toUpperCase().equals("ZJH"))
			sort="QLR.ZJH";*/
		
		if(sort.toUpperCase().equals("BDCQZH"))
			sort="QL.BDCQZH";
		if(sort.toUpperCase().equals("FH"))
			sort="DY.FH";
		if(sort.toUpperCase().equals("QLLX"))
			sort="QL.QLLX";
		/*if(sort.toUpperCase().equals("ZJHM"))
			sort="QLR.ZJHM";*/
		if(sort.toUpperCase().equals("GHYTNAME"))
			sort="DY.GHYT";
		if(sort.toUpperCase().equals("BDCDYID"))
			sort="DY.BDCDYID";
		if(sort.toUpperCase().equals("BDCDYLXMC"))
			sort="DY.BDCDYLX";
        if(sort.toUpperCase().equals("ZRZH"))
			sort="DY.ZRZH";		
			fullSql=fullSql+" ORDER BY "+sort+" "+"asc";
		}
		/* 为了提高效率，先不查询符合条件的记录总数，将来改为异步获取 */
		CommonDao dao = baseCommonDao;

		count = dao.getCountByFullSql(fromSql);
		listresult = dao.getDataListByFullSql(fullSql);//不分页
		//addRightsHolderInfo(listresult);
		addQLLX(listresult);
		/*
		String cdbz = queryvalues.get("SWCDBZ");//查档的一个参数
		
		addLimitStatus(listresult,cdbz);
		addDyCfDetails(listresult);
		isGlzd(listresult);
		//添加状态
      	addBDCDYZT(listresult);
		//权利状态
      	addQLZT(listresult);
      	//添加注销状态
      	addZXZT(listresult);
        
        addQXGL(listresult);*/
		
		//ADDXMXX_SIZE(listresult);
		// 格式化结果中的常量值

		for (Map map : listresult) {
			if (map.containsKey("GHYT")) {
				String value = StringHelper.formatObject(StringHelper
						.isEmpty(map.get("GHYT")) ? "" : map.get("GHYT"));
				String name = "";
				if (!StringHelper.isEmpty(value)) {
					name = ConstHelper.getNameByValue("FWYT", value);
				}
				map.put("GHYTname", name);
			}
			if (map.containsKey("DJSJ")) {
				map.put("DJSJ",StringHelper.FormatYmdhmsByDate(map.get("DJSJ")));
			}
		}

		msg.setTotal(count);
		msg.setRows(listresult);
		return msg;
	}
	
	protected boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

	public boolean isChinese(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 根据申请人（乙方）的姓名和证件号查出不在查询的权利人名下的房产
	 * @author hks
	 * @Date 2016/7/18  13:09	
	 */
	public Message getZyProjectList(String status, String start,
			String end,String actdef_name,String order, String prodefname, 
			String staff, String sqr,String zjh) {
		Message msg = new Message();
		StringBuilder str = new StringBuilder();
		str.append("1>0 ");
		
		if (actdef_name != null && !actdef_name.equals("")) {
			str.append(" and ACTINST_NAME in (");
			str.append(actdef_name);
			str.append(" )");
		}
		if (status != null && !"".equals(status)) {
			str.append(" and ACTINST_STATUS in (");
			str.append(status);
			str.append(")");
		}
		if (start != null && !"".equals(start)) {
			str.append(" and ACTINST_END >= to_date('");
			str.append(start);
			str.append("','yyyy-MM-dd')");
		}
		if (end != null && !"".equals(end)) {
			str.append(" and ACTINST_END < to_date('");
			str.append(end);
			str.append("','yyyy-MM-dd')");
		}
		// 流程名称(*只要转移流程的)
		if (prodefname != null && !"".equals(prodefname)) {
			str.append(" and prodef_name like '");
			str.append(prodefname);
			str.append("%'");
		}
		// 办理人员
		if (staff != null && !"".equals(staff) && status.contains("1")) {
			str.append(" and actstaffname like '%");
			str.append(staff);
			str.append("%' and actinst_status not in (2,3)");
		} else if (staff != null && !"".equals(staff)) {
			str.append(" and staff_name like '%");
			str.append(staff);
			str.append("%'");
		}
		
		if (sqr != null && !"".equals(sqr) && zjh != null && !"".equals(zjh)) {
			if (isChinese(sqr)) {
				str.append(" and  EXISTS (SELECT 1  FROM BDCK.BDCS_XMXX XMXX LEFT JOIN BDCK.bDCS_SQR SQR ON SQR.XMBH=XMXX.XMBH WHERE SQR.SQRXM = '"
						+ sqr + "' AND SQR.ZJH = "+"'"+zjh
						+ "'"+" AND SQR.SQRID IS NOT NULL  AND XMXX.PROJECT_ID=v_projectlist.FILE_NUMBER) ");
			}
		}

		long tatalCount = baseCommonDao
				.getCountByFullSql(" from (select  row_number() over(partition by file_number order by actinst_start "
						//+ order
						+ ") rn,file_number, project_name, PROINST_START,proinst_status,Staff_Name from "
						+ Common.WORKFLOWDB
						+ "V_PROJECTLIST where "
						+ str.toString() + ") where rn=1 ");
		msg.setTotal(tatalCount);
		if (tatalCount > 0) { 
			str.append(" order by ACTINST_START  ");
			msg.setRows(baseCommonDao.getDataListByFullSql(
					"select * from (select  row_number() over(partition by file_number order by actinst_start "
							//+ order
							+ ") rn,file_number,actinst_start,proinst_id,prodef_name,project_name,proinst_status,actinst_id,PROINST_START,Staff_Name,actinst_name,prolsh,actinst_status from "
							+ Common.WORKFLOWDB
							+ "V_PROJECTLIST where "
							+ str.toString()
							+ " ) where rn=1 "
					));
		}
		return msg;
	}
	
	public Message getQlInfo(String ywh){
		Message msg = new Message();
		
		long tatalCount = baseCommonDao
				.getCountByFullSql("from bdck.bdcs_h_ls hls where hls.bdcdyid in "
						+ "(select dyls.bdcdyid from bdck.bdcs_djdy_ls dyls where bdcdylx = '031' AND dyls.djdyid in "
						+ "(select qls.djdyid from bdck.bdcs_ql_ls qls where djlx = '200' AND ywh = '"+ywh+"'))"
									 );
		msg.setTotal(tatalCount);
		if (tatalCount > 0) { 
			String fullsql = "select hls.zl as ZL,qls.bdcqzh as YBDCQZH from bdck.bdcs_h_ls hls join "
					+ " bdck.bdcs_djdy_ls dyls on hls.bdcdyid= dyls.bdcdyid join "
					+ "bdck.bdcs_ql_ls qls on dyls.djdyid=qls.djdyid "
					+ "where dyls.bdcdylx = '031' and qls.djlx = '200' and ywh = '"+ywh+"'";
			
			msg.setRows(baseCommonDao.getDataListByFullSql(fullsql));
		}
		return msg;
	}
	/**
	 * 根据权利人名称和证件号码获取对应的坐落和不动产权证号的房屋下的产权和其他权利
	 * @author heks
	 * @date 2016/12/08 15:05:49
	 * @param qlrmc
	 * @param qlrzjh
	 * @return
	 */
	public List<Map> getAllQlInfoByQlr(String qlrmc,String qlrzjh,String bdcqzh){
		Message msg = new Message();
		StringBuffer qlr_zjhbuilder = new StringBuffer();
		// 权利人，权利人的条件比较特殊，where里边又套了where
		if (qlrmc != null) {

			qlr_zjhbuilder.append(" and d.qlrmc = '" +qlrmc+"'");

			if (qlrzjh != null && qlrzjh.length() == 18) {
				String oldCard = ConverIdCard.getOldIDCard(qlrzjh);
				if (oldCard.length() != 15) {
					qlr_zjhbuilder.append(" and" + " d.zjh = '" + qlrzjh
							+ "' ");
				} else {
					qlr_zjhbuilder.append(" and (" + "d.zjh ='" + qlrzjh
							+ "' or ");
					qlr_zjhbuilder.append(" " + " d.zjh = '" + oldCard
							+ "')");
				}
			} /*else {
				qlr_zjhbuilder.append(" and" + " d.zjh = '" + qlrzjh + "' ");
			}*/

		}
		if(!StringHelper.isEmpty(bdcqzh)){
			qlr_zjhbuilder.append(" and a.bdcqzh like '%" +bdcqzh+"%'");
		}
		String sqlString = "select distinct a.bdcdyh from bdck.bdcs_ql_xz a join bdck.bdcs_qdzr_xz c on "
				+ "a.bdcdyh = c.bdcdyh join bdck.bdcs_qlr_xz d on c.qlrid = d.qlrid where 1=1"+qlr_zjhbuilder.toString();
		
		List<Map> qlList = baseCommonDao.getDataListByFullSql(sqlString);

		List<Map> list = new ArrayList<Map>();
		if(qlList.size() > 0){
			for(Map q : qlList){
				String fullSql = "select a.bdcdyh,a.bdcqzh,a.djlx,a.qlid,b.dymj,b.bdbzzqse,a.djsj,b.cfjg,b.cfwh,b.cffw,a.qllx,b.dyfs,b.zgzqse "
						+ " from bdck.bdcs_ql_xz a join bdck.bdcs_fsql_xz b on a.fsqlid = b.fsqlid  where  a.bdcdyh = '"+q.get("BDCDYH")+"'";

				List<Map> allQqlInfos = baseCommonDao.getDataListByFullSql(fullSql);
				if(allQqlInfos.size() > 0){
					for(Map map : allQqlInfos){
						Map<String, String> ql = new HashMap();
						ql.putAll(map);
						String sql = "select qlrmc from bdck.bdcs_qlr_xz where qlid = '"+map.get("QLID")+"'";
						List<Map> qlrList = baseCommonDao.getDataListByFullSql(sql);
						String _qlrmc = "";
						if(qlrList.size() > 0){
							for(Map qlr : qlrList){
								_qlrmc += qlr.get("QLRMC") + ",";
							}
						}
						if(_qlrmc != null && !_qlrmc.equals("")){
							_qlrmc = _qlrmc.substring(0, _qlrmc.length()-1);
							ql.put("QLRMC", _qlrmc);
							
						}
						list.add(ql);
					}
				}
			}
		}
		return list;
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<List<Map>> GetDYDJList_XZ1(String bdcdyid) {
		String sql = "select ycbdcdyid from  bdck.YC_SC_H_XZ  where scbdcdyid = '"+bdcdyid+"'";
		String ycbdcdyid = "";
		List<Map> ycbdcdyidlist =baseCommonDao.getDataListByFullSql(sql);
		if (ycbdcdyidlist != null && ycbdcdyidlist.size() > 0) {
			Map map = ycbdcdyidlist.get(0);
			ycbdcdyid = map.get("YCBDCDYID").toString();
		}
		List<List<Map>> result = new ArrayList<List<Map>>();
		List<Map> qllist = new ArrayList();
		List<BDCS_DJDY_XZ> djdys = baseCommonDao.getDataList(
				BDCS_DJDY_XZ.class, "BDCDYID='" + ycbdcdyid + "'");
		// 查询历史权利信息
		if (djdys != null && djdys.size() > 0) {
			String djdyid = djdys.get(0).getDJDYID();
			StringBuilder builderXZ = new StringBuilder();
			builderXZ
					.append("SELECT QLID,YWH,DJLX,QLLX,DJSJ FROM BDCK.BDCS_QL_LS WHERE DJDYID='");
			builderXZ.append(djdyid);
			builderXZ.append("' AND QLLX='23' ORDER BY DJSJ ASC NULLS FIRST");
			List<Map> xzqls = baseCommonDao.getDataListByFullSql(builderXZ
					.toString());
			int sx = 1;
			if (xzqls != null && xzqls.size() > 0) {
				for (int iql = 0; iql < xzqls.size(); iql++) {
					String qlid = StringHelper.formatObject(xzqls.get(iql).get(
							"QLID"));
					String djlx = StringHelper.formatObject(xzqls.get(iql).get(
							"DJLX"));
					String ywh = StringHelper.formatObject(xzqls.get(iql).get(
							"YWH"));
					Map<String, String> map = new HashMap<String, String>();
					map.put("QLID", qlid);
					map.put("YWH", ywh);
					map.put("CANCLED", "false");
					map.put("DJLX",
							StringHelper.isEmpty(djlx) ? "首次登记" : DJLX
									.initFrom(djlx).Name);
					map.put("QLLX", "DYQ");
					map.put("LHSX", "第"+sx+"轮");
					qllist.add(map);
					sx++;
				}
			}
		}
		result.add(qllist);
		return result;
	}
		
	//鹰潭贵溪
	protected String getfromSQLGX(String fromSql , String cxzt) {
		
		if ("2".equals(cxzt)) {
			fromSql=fromSql.replaceAll("BDCK.BDCS_FSQL_LS", "BDCK.BDCS_FSQL_LS@TO_ORCL6_BDCK");
			fromSql=fromSql.replaceAll("BDCK.BDCS_H_LS", "BDCK.BDCS_H_LS @TO_ORCL6_BDCK");
			fromSql=fromSql.replaceAll("BDCK.BDCS_H_LS@TO_ORCL6_BDCKY", "BDCK.BDCS_H_LSY@TO_ORCL6_BDCK");
			fromSql=fromSql.replaceAll("BDCK.BDCS_QL_LS", "BDCK.BDCS_QL_LS@TO_ORCL6_BDCK");
			fromSql=fromSql.replaceAll("BDCK.BDCS_DJDY_LS", "BDCK.BDCS_DJDY_LS@TO_ORCL6_BDCK");
			fromSql=fromSql.replaceAll("BDCK.BDCS_QLR_LS", "BDCK.BDCS_QLR_LS@TO_ORCL6_BDCK");
		}else{
			fromSql=fromSql.replaceAll("BDCK.BDCS_H_XZ", "BDCK.BDCS_H_XZ@TO_ORCL6_BDCK");
			fromSql=fromSql.replaceAll("BDCK.BDCS_H_XZ@TO_ORCL6_BDCKY", "BDCK.BDCS_H_XZY @TO_ORCL6_BDCK ");
			fromSql=fromSql.replaceAll("BDCK.BDCS_DJDY_XZ", "BDCK.BDCS_DJDY_XZ@TO_ORCL6_BDCK");
			fromSql=fromSql.replaceAll("BDCK.BDCS_QL_XZ", "BDCK.BDCS_QL_XZ@TO_ORCL6_BDCK");
			fromSql=fromSql.replaceAll("BDCK.BDCS_FSQL_XZ", "BDCK.BDCS_FSQL_XZ@TO_ORCL6_BDCK");
			fromSql=fromSql.replaceAll("BDCK.BDCS_QLR_XZ", "BDCK.BDCS_QLR_XZ@TO_ORCL6_BDCK");
		}
		return fromSql;
		
	}
	//鹰潭余江
	protected String getfromSQLYJ(String fromSql,String cxzt) {
		if ("2".equals(cxzt)) {
			fromSql=fromSql.replaceAll("BDCK.BDCS_FSQL_LS", "BDCK.BDCS_FSQL_LS@TO_ORCL7_BDCK");
			fromSql=fromSql.replaceAll("BDCK.BDCS_H_LS", "BDCK.BDCS_H_LS @TO_ORCL7_BDCK");
			fromSql=fromSql.replaceAll("BDCK.BDCS_H_LS@TO_ORCL7_BDCKY", "BDCK.BDCS_H_LSY@TO_ORCL7_BDCK");
			fromSql=fromSql.replaceAll("BDCK.BDCS_QL_LS", "BDCK.BDCS_QL_LS@TO_ORCL7_BDCK");
			fromSql=fromSql.replaceAll("BDCK.BDCS_DJDY_LS", "BDCK.BDCS_DJDY_LS@TO_ORCL7_BDCK");
			fromSql=fromSql.replaceAll("BDCK.BDCS_QLR_LS", "BDCK.BDCS_QLR_LS@TO_ORCL7_BDCK");
		}else{
			fromSql=fromSql.replaceAll("BDCK.BDCS_H_XZ", "BDCK.BDCS_H_XZ@TO_ORCL7_BDCK");
			fromSql=fromSql.replaceAll("BDCK.BDCS_H_XZ@TO_ORCL7_BDCKY", "BDCK.BDCS_H_XZY@TO_ORCL7_BDCK");
			fromSql=fromSql.replaceAll("BDCK.BDCS_DJDY_XZ", "BDCK.BDCS_DJDY_XZ@TO_ORCL7_BDCK");
			fromSql=fromSql.replaceAll("BDCK.BDCS_QL_XZ", "BDCK.BDCS_QL_XZ@TO_ORCL7_BDCK");
			fromSql=fromSql.replaceAll("BDCK.BDCS_FSQL_XZ", "BDCK.BDCS_FSQL_XZ@TO_ORCL7_BDCK");
			fromSql=fromSql.replaceAll("BDCK.BDCS_QLR_XZ", "BDCK.BDCS_QLR_XZ@TO_ORCL7_BDCK");
		}
		return fromSql;
		
	}
	protected String getfullSQLGX(String fullSql,String cxzt) {
		if ("2".equals(cxzt)) {
			fullSql=fullSql.replaceAll("BDCK.BDCS_FSQL_LS", "BDCK.BDCS_FSQL_LS@TO_ORCL6_BDCK");
			fullSql=fullSql.replaceAll("BDCK.BDCS_H_LS", "BDCK.BDCS_H_LS@TO_ORCL6_BDCK");
			fullSql=fullSql.replaceAll("BDCK.BDCS_H_LS@TO_ORCL6_BDCKY", "BDCK.BDCS_H_LSY@TO_ORCL6_BDCK");
			fullSql=fullSql.replaceAll("BDCK.BDCS_QL_LS", "BDCK.BDCS_QL_LS@TO_ORCL6_BDCK");
			fullSql=fullSql.replaceAll("BDCK.BDCS_DJDY_LS", "BDCK.BDCS_DJDY_LS@TO_ORCL6_BDCK");
			fullSql=fullSql.replaceAll("BDCK.BDCS_QLR_LS", "BDCK.BDCS_QLR_LS@TO_ORCL6_BDCK");
			fullSql=fullSql.replaceAll("BDCK.BDCS_FSQL_XZ", "BDCK.BDCS_FSQL_XZ@TO_ORCL6_BDCK");
		}else{
			fullSql=fullSql.replaceAll("BDCK.BDCS_H_XZ", "BDCK.BDCS_H_XZ@TO_ORCL6_BDCK");
			fullSql=fullSql.replaceAll("BDCK.BDCS_H_XZ@TO_ORCL6_BDCKY", "BDCK.BDCS_H_XZY@TO_ORCL6_BDCK");
			fullSql=fullSql.replaceAll("BDCK.BDCS_QL_XZ", "BDCK.BDCS_QL_XZ@TO_ORCL6_BDCK");
			fullSql=fullSql.replaceAll("BDCK.BDCS_DJDY_XZ", "BDCK.BDCS_DJDY_XZ@TO_ORCL6_BDCK");
			fullSql=fullSql.replaceAll("BDCK.BDCS_QLR_XZ", "BDCK.BDCS_QLR_XZ@TO_ORCL6_BDCK");
		}
		return fullSql;
		
	}
	protected String getfullSQLYJ(String fullSql,String cxzt) {
		if ("2".equals(cxzt)) {
			fullSql=fullSql.replaceAll("BDCK.BDCS_FSQL_LS", "BDCK.BDCS_FSQL_LS@TO_ORCL7_BDCK");
			fullSql=fullSql.replaceAll("BDCK.BDCS_H_LS", "BDCK.BDCS_H_LS@TO_ORCL7_BDCK");
			fullSql=fullSql.replaceAll("BDCK.BDCS_H_LS@TO_ORCL7_BDCKY", "BDCK.BDCS_H_LSY@TO_ORCL7_BDCK");
			fullSql=fullSql.replaceAll("BDCK.BDCS_QL_LS", "BDCK.BDCS_QL_LS@TO_ORCL7_BDCK");
			fullSql=fullSql.replaceAll("BDCK.BDCS_DJDY_LS", "BDCK.BDCS_DJDY_LS@TO_ORCL7_BDCK");
			fullSql=fullSql.replaceAll("BDCK.BDCS_QLR_LS", "BDCK.BDCS_QLR_LS@TO_ORCL7_BDCK");
		}else{
			fullSql=fullSql.replaceAll("BDCK.BDCS_FSQL_XZ", "BDCK.BDCS_FSQL_XZ@TO_ORCL7_BDCK");
			fullSql=fullSql.replaceAll("BDCK.BDCS_H_XZ", "BDCK.BDCS_H_XZ@TO_ORCL7_BDCK");
			fullSql=fullSql.replaceAll("BDCK.BDCS_H_XZ@TO_ORCL7_BDCKY", "BDCK.BDCS_H_XZY@TO_ORCL7_BDCK");
			fullSql=fullSql.replaceAll("BDCK.BDCS_QL_XZ", "BDCK.BDCS_QL_XZ@TO_ORCL7_BDCK");
			fullSql=fullSql.replaceAll("BDCK.BDCS_DJDY_XZ", "BDCK.BDCS_DJDY_XZ@TO_ORCL7_BDCK");
			fullSql=fullSql.replaceAll("BDCK.BDCS_QLR_XZ", "BDCK.BDCS_QLR_XZ@TO_ORCL7_BDCK");
		}
		return fullSql;
		
	}


    /**
	 * 判断是否关联宗地全市监管
	 * 
	 * @param listresult
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void isGlzdyt(List<Map> listresult,String xzqh) {
		String table = "";
		String tableh = "";
		if(xzqh.equals("2")){
			table = "BDCK.BDCS_SHYQZD_XZ@TO_ORCL6_BDCK ";
			tableh = "BDCK.BDCS_h_XZ@TO_ORCL6_BDCK ";
		}else if(xzqh.equals("3")){
			table = "BDCK.BDCS_SHYQZD_XZ@TO_ORCL7_BDCK ";
			tableh = "BDCK.BDCS_h_XZ@TO_ORCL7_BDCK ";
		}else {
			table = "BDCK.BDCS_SHYQZD_XZ ";
			tableh = "BDCK.BDCS_h_XZ ";
		}

		if (listresult != null && listresult.size() > 0) {
			for (Map map : listresult) {
				if (map.containsKey("BDCDYID") && map.containsKey("BDCDYLX")) {
					String bdcdyid = (String) map.get("BDCDYID");
					String bdcdylx = (String) map.get("BDCDYLX");
					if (!StringHelper.isEmpty(bdcdyid)
							&& !StringHelper.isEmpty(bdcdylx)) {
//						RealUnit Hunit = UnitTools.loadUnit(
//								BDCDYLX.initFrom(bdcdylx),
//								DJDYLY.initFrom("02"), bdcdyid);
						String sql ="";
						if(xzqh.equals("0")){
							sql =" select ZDBDCDYID from (select ZDBDCDYID from "+tableh+" union all select ZDBDCDYID from BDCK.BDCS_h_XZ@TO_ORCL6_BDCK "
									+ "union all select ZDBDCDYID from BDCK.BDCS_h_XZ@TO_ORCL7_BDCK) where BDCDYID= '"+bdcdyid+"'";
						}else{
							sql = "select ZDBDCDYID  from "+tableh+"  where BDCDYID= '"+bdcdyid+"' ";
						}
					
						List<Map> Hunit=baseCommonDao.getDataListByFullSql(sql);
						if(Hunit.size() > 0 && Hunit != null){
						if (BDCDYLX.H.equals(BDCDYLX.initFrom(bdcdylx))
								|| BDCDYLX.YCH
										.equals(BDCDYLX.initFrom(bdcdylx))) {
//							HOUSE H = (HOUSE) HUNIT;
//							STRING ZDBDCDYID = H.GETZDBDCDYID();
							String zdbdcdyid = (String) Hunit.get(0).get("ZDBDCDYID");

							if (!StringHelper.isEmpty(zdbdcdyid)) {
								String detailssql ="";
								if(xzqh.equals("0")){
									detailssql =" select BDCDYID,ZL from (select BDCDYID,ZL from "+table+" union all select BDCDYID,ZL from BDCK.BDCS_SHYQZD_XZ@TO_ORCL6_BDCK "
											+ "union all select BDCDYID,ZL from BDCK.BDCS_SHYQZD_XZ@TO_ORCL7_BDCK) where BDCDYID= '"+zdbdcdyid+"'";
								}else{
									detailssql = "select BDCDYID,ZL  from "+table+"  where BDCDYID= '"+zdbdcdyid+"' ";
								}
								List<Map> listzd=baseCommonDao.getDataListByFullSql(detailssql);
							
								if (listzd != null && listzd.size() > 0) {
									map.put("SFGLZD", "是");
									String zdlx =   "02";
									String zdzl = (String) listzd.get(0).get("ZL");
									map.put("ZDBDCDYID", zdbdcdyid);
									map.put("ZDBDCDYLX", zdlx);
									map.put("ZDZL", zdzl);
								} else {
									map.put("SFGLZD", "否");
								}
							} else {
								map.put("SFGLZD", "否");
							}
							}
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void addQXGLYT(List<Map> listresult,String xzqh) {
		String table = "";
		if(xzqh.equals("2")){
			table = "BDCK.YC_SC_H_XZ@TO_ORCL6_BDCK ";
		}else if(xzqh.equals("3")){
			table = "BDCK.YC_SC_H_XZ@TO_ORCL7_BDCK ";
		}else {
			table = "BDCK.YC_SC_H_XZ ";
		} 
		if (listresult != null && listresult.size() > 0) {
			for (Map map : listresult) {
				if (map.containsKey("BDCDYID") && map.containsKey("BDCDYLX")) {					
					String bdcdylx = (String) map.get("BDCDYLX");
					String bdcdyid = (String) map.get("BDCDYID");
					if (!StringHelper.isEmpty(bdcdyid)
							&& !StringHelper.isEmpty(bdcdylx)) {						
						String fulSql = "";
						long count = 0;
						if(bdcdylx.equals("032")){
							if(xzqh.equals("0")){
								fulSql = "from (select YCBDCDYID from "+table+"  "
										+ "union all select YCBDCDYID from BDCK.YC_SC_H_XZ@TO_ORCL6_BDCK ' "
										+ "union all select YCBDCDYID from BDCK.YC_SC_H_XZ@TO_ORCL7_BDCK) WHERE YCBDCDYID='"+bdcdyid+"'";
												
							}else{
								fulSql =" from "+table+" WHERE YCBDCDYID='"+bdcdyid+"' ";
												
							}
							
							
							count = baseCommonDao.getCountByFullSql(fulSql);
							if (count>0) {
								map.put("QXFGL", "已关联现房");
							}else {
								map.put("QXFGL", "未关联现房");
							}	
						}else if (bdcdylx.equals("031")) {
							if(xzqh.equals("0")){
								fulSql = " from (select SCBDCDYID from "+table+" "
										+ "union all select SCBDCDYID from BDCK.YC_SC_H_XZ@TO_ORCL6_BDCK  "
										+ "union all select SCBDCDYID from BDCK.YC_SC_H_XZ@TO_ORCL7_BDCK ) WHERE SCBDCDYID='"+bdcdyid+"'";
												
							}else{
								fulSql =" from "+table+" WHERE SCBDCDYID='"+bdcdyid+"' ";
												
							}
							
							count = baseCommonDao.getCountByFullSql(fulSql);
							if (count>0) {
								map.put("QXFGL", "已关联期房");
							}else {
								map.put("QXFGL", "未关联期房");
							}	 
						}
					
					}else {
						map.put("QXFGL", "未关联");
					}
				}
			}
		}
	}
	 //登记查封状态的详情信息全市监管
    @SuppressWarnings({ "rawtypes", "unchecked" })
	protected void addDyCfDetailsyt(List<Map> result,String xzqh){
    	String table = "";
    	String table1 = "";
		if(xzqh.equals("2")){
			table = "BDCK.BDCS_QL_XZ@TO_ORCL6_BDCK ql";
			table1 = "BDCK.BDCS_FSQL_XZ@TO_ORCL6_BDCK fsql";
		}else if(xzqh.equals("3")){
			table = "BDCK.BDCS_QL_XZ@TO_ORCL7_BDCK ql";
			table1 = "BDCK.BDCS_FSQL_XZ@TO_ORCL7_BDCK fsql";
		}else {
			table = "BDCK.BDCS_QL_XZ ql";
			table1 = "BDCK.BDCS_FSQL_XZ fsql";
		}
    	if (result != null && result.size() > 0) {
			for (Map map : result) {
				if(!StringUtils.isEmpty(map.get("BDCDYH"))&&!StringUtils.isEmpty(map.get("DJDYID")))
				{	
					String dycfdetailssql ="";
					if(xzqh.equals("0")){
						dycfdetailssql = MessageFormat.format("select cfjg,cfwh,dyr,qlqssj,qljssj,qllx,djlx,djdyid,bdcdyh from ("
								+ "select fsql.cfjg,fsql.cfwh,fsql.dyr,ql.qlqssj,ql.qljssj,ql.qllx,ql.djlx,ql.djdyid,ql.bdcdyh "
								+ " from "+table+" left join "+table1+" on ql.qlid=fsql.qlid "
										+ "union all select fsql.cfjg,fsql.cfwh,fsql.dyr,ql.qlqssj,ql.qljssj,ql.qllx,ql.djlx,ql.djdyid,ql.bdcdyh "
								+ " from BDCK.BDCS_QL_XZ@TO_ORCL6_BDCK ql left join BDCK.BDCS_FSQL_XZ@TO_ORCL6_BDCK fsql on ql.qlid=fsql.qlid "
										+ "union all select fsql.cfjg,fsql.cfwh,fsql.dyr,ql.qlqssj,ql.qljssj,ql.qllx,ql.djlx,ql.djdyid,ql.bdcdyh"
								+ " from BDCK.BDCS_QL_XZ@TO_ORCL7_BDCK ql left join BDCK.BDCS_FSQL_XZ@TO_ORCL7_BDCK fsql on ql.qlid=fsql.qlid ) "
										+ " where bdcdyh=''{0}'' and djdyid=''{1}'' and qllx in(''99'',''23'') ",
								map.get("BDCDYH"),map.get("DJDYID"));
					}else{
						dycfdetailssql = MessageFormat.format("select fsql.cfjg,fsql.cfwh,fsql.dyr,ql.qlqssj,ql.qljssj,ql.qllx,ql.djlx "
								+ " from "+table+" left join "+table1+" on ql.qlid=fsql.qlid where ql.bdcdyh=''{0}'' and ql.djdyid=''{1}'' and ql.qllx in(''99'',''23'') ",
								map.get("BDCDYH"),map.get("DJDYID"));
					}
					 
					
					List<Map> dycfs=baseCommonDao.getDataListByFullSql(dycfdetailssql);
					for (Map dycf : dycfs) {
						if(dycf.get("DJLX").equals("800")&&dycf.get("QLLX").equals("99")){
							map.put("CFJG", dycf.get("CFJG"));
							map.put("CFWH", dycf.get("CFWH"));
							map.put("CFQX", DateUtil.FormatByDatetime(dycf.get("QLQSSJ"))+" 至  "+DateUtil.FormatByDatetime(dycf.get("QLJSSJ")));
						}
						if(dycf.get("QLLX").equals("23")){
							map.put("DYR", dycf.get("DYR"));
							map.put("DYQX", DateUtil.FormatByDatetime(dycf.get("QLQSSJ"))+" 至  "+DateUtil.FormatByDatetime(dycf.get("QLJSSJ")));
						}
					}
				}
			}
    	}
    }
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void addLimitStatusyt(List<Map> result , String xzqh) {
		String table = "";
		String table1 = "";
		if(xzqh.equals("2")){
			table = "BDCK.BDCS_DJDY_XZ@TO_ORCL6_BDCK";
			table1 = "BDCK.YC_SC_H_XZ @TO_ORCL6_BDCK";
		}else if(xzqh.equals("3")){
			table = "BDCK.BDCS_DJDY_XZ@TO_ORCL7_BDCK";
			table1 = "BDCK.YC_SC_H_XZ@TO_ORCL7_BDCK";
		}else {
			table = "BDCK.BDCS_DJDY_XZ ";
			table1 = "BDCK.YC_SC_H_XZ";
		}
		if (result != null && result.size() > 0) {
			for (Map map : result) {
				if (map.containsKey("DJDYID")) {
					String djdyid = (String) map.get("DJDYID");
					String ycdjdyid = "";
					String scdjdyid = "";
					String detailssql = "";
					if (djdyid != null) {
						if(xzqh.equals("0")){
							detailssql =" select BDCDYLX,BDCDYID,djdyid from (select BDCDYLX,BDCDYID,djdyid from "+table+" union all select BDCDYLX,BDCDYID,djdyid from BDCK.BDCS_DJDY_XZ@TO_ORCL6_BDCK "
									+ "union all select BDCDYLX,BDCDYID,djdyid from BDCK.BDCS_DJDY_XZ@TO_ORCL7_BDCK) where djdyid= '"+djdyid+"'";
						}else{
							detailssql = "select BDCDYLX,BDCDYID,djdyid  from "+table+"  where djdyid= '"+djdyid+"' ";
						}
						      
				    }		
					   List<Map> listdjdy=baseCommonDao.getDataListByFullSql(detailssql);
//						List<BDCS_DJDY_XZ> listdjdy = baseCommonDao
//								.getDataList(BDCS_DJDY_XZ.class, "djdyid='"
//										+ djdyid + "'");
						if (listdjdy != null && listdjdy.size() > 0) {
							String lx =   (String) listdjdy.get(0).get("BDCDYLX");
							if (lx.equals("031")) {
								String valuedyxz = "";
								String valuecfxz = "";
								String valueyyxz = "";
								String detailssql2 ="";
								if(xzqh.equals("0")){
									detailssql2 =" select YCBDCDYID,SCBDCDYID from (select YCBDCDYID,SCBDCDYID from "+table1+" union all select YCBDCDYID,SCBDCDYID from BDCK.YC_SC_H_XZ @TO_ORCL6_BDCK "
											+ "union all select YCBDCDYID,SCBDCDYID from BDCK.YC_SC_H_XZ @TO_ORCL7_BDCK) where scbdcdyid= '"+listdjdy.get(0).get("BDCDYID")+"'";
								}else{
									detailssql2 = "select *  from "+table1+"  where scbdcdyid= '"+listdjdy.get(0).get("BDCDYID")+"' ";
								}
								List<Map> listycsc = baseCommonDao.getDataListByFullSql(detailssql2);
										String detailssql3 ="";
								if (listycsc != null
										&& listycsc.size() > 0
										&&  !listycsc.get(0).get("YCBDCDYID").equals("")) {
									if(xzqh.equals("0")){
										detailssql3 =" select DJDYID from (select DJDYID from "+table+" union all select DJDYID from BDCK.BDCS_DJDY_XZ@TO_ORCL6_BDCK "
												+ "union all select DJDYID from BDCK.BDCS_DJDY_XZ@TO_ORCL7_BDCK) where djdyid= '"+listycsc.get(0).get("YCBDCDYID")+"";
									}else{
										detailssql3 = "select DJDYID  from "+table+"  where djdyid= '"+listycsc.get(0).get("YCBDCDYID")+"' ";
									}
									List<Map> listdjdyyc = baseCommonDao.getDataListByFullSql(detailssql3);
											
									if (listdjdyyc != null
											&& listdjdyyc.size() > 0) {
										ycdjdyid =  (String) listdjdyyc.get(0).get("DJDYID");
										Map<String, String> mapxzy = new HashMap<String, String>();
										mapxzy = getDYandCFandYY_XZY(ycdjdyid,"");
										for (Entry<String, String> ent : mapxzy
												.entrySet()) {
											String name = ent.getKey();
											if (name.equals("DYZT")) {
												if (StringHelper
														.isEmpty(valuedyxz)) {
													valuedyxz = ent.getValue();
												} else {
													valuedyxz = valuedyxz + "、"
															+ ent.getValue();
												}
											} else if (name.equals("CFZT")) {
												if (StringHelper
														.isEmpty(valuecfxz)) {
													valuecfxz = ent.getValue();
												} else {
													valuecfxz = valuecfxz + "、"
															+ ent.getValue();
												}
											} else if (name.equals("YYZT")){
												if (StringHelper
														.isEmpty(valueyyxz)) {
													valueyyxz = ent.getValue();
												} else {
													valueyyxz = valueyyxz + "、"
															+ ent.getValue();
												}
											}
										}
									}
								}
								Map<String, String> mapxz = new HashMap<String, String>();
								mapxz = getDYandCFandYY_XZ(djdyid,"");
								for (Entry<String, String> ent : mapxz
										.entrySet()) {
									String name = ent.getKey();
									if (name.equals("DYZT")) {
										if (StringHelper.isEmpty(valuedyxz)) {
											valuedyxz = ent.getValue();
										} else {
											valuedyxz = valuedyxz + " "
													+ ent.getValue();
										}
									} else if (name.equals("CFZT")) {
										if (StringHelper.isEmpty(valuecfxz)) {
											valuecfxz = ent.getValue();
										} else {
											valuecfxz = valuecfxz + " "
													+ ent.getValue();
										}
									} else if (name.equals("YYZT")){
										if (StringHelper.isEmpty(valueyyxz)) {
											valueyyxz = ent.getValue();
										} else {
											valueyyxz = valueyyxz + " "
													+ ent.getValue();
										}
									}
								}
								map.put("DYZT", valuedyxz);
								map.put("CFZT", valuecfxz);
								map.put("YYZT", valueyyxz);

							} else {
								String detailssql4 ="";
								if(xzqh.equals("0")){
									detailssql4 =" select ycbdcdyid,scbdcdyid from (select ycbdcdyid,scbdcdyid from "+table1+" union all select ycbdcdyid,scbdcdyid from BDCK.YC_SC_H_XZ @TO_ORCL6_BDCK "
											+ "union all select ycbdcdyid,scbdcdyid from BDCK.YC_SC_H_XZ @TO_ORCL7_BDCK) where ycbdcdyid= '"+listdjdy.get(0).get("BDCDYID")+"";
								}else{
									detailssql4 = "select ycbdcdyid,scbdcdyid  from "+table1+"  where ycbdcdyid= '"+ listdjdy.get(0).get("BDCDYID")+"' ";
								}
								List<Map> listycsc = baseCommonDao.getDataListByFullSql(detailssql4);
								
								
								if (listycsc != null
										&& listycsc.size() > 0
										&&  !listycsc.get(0).get("SCBDCDYID").equals("")) {
									String detailssql5 = "";
									if(xzqh.equals("0")){
										
										detailssql5 =" select DJDYID from (select DJDYID from "+table+" union all select DJDYID from BDCK.BDCS_DJDY_XZ @TO_ORCL6_BDCK "
												+ "union all select DJDYID from BDCK.BDCS_DJDY_XZ @TO_ORCL7_BDCK) where djdyid= '"+listycsc.get(0).get("SCBDCDYID")+"";
									}else{
										detailssql5 = "select DJDYID  from "+table+"  where djdyid= '"+listycsc.get(0).get("SCBDCDYID")+"' ";
									}
									List<Map> listdjdysc = baseCommonDao.getDataListByFullSql(detailssql5);
									
									if (listdjdysc != null
											&& listdjdysc.size() > 0) {
										scdjdyid = (String) listdjdysc.get(0).get("DJDYID");
										Map<String, String> mapxz = new HashMap<String, String>();
										mapxz = getDYandCFandYY_XZ(scdjdyid,"");
										String valuedyxz = "";
										String valuecfxz = "";
										String valueyyxz = "";
										for (Entry<String, String> ent : mapxz
												.entrySet()) {
											String name = ent.getKey();
											if (name.equals("DYZT")) {
												if (StringHelper
														.isEmpty(valuedyxz)) {
													valuedyxz = ent.getValue();
												} else {
													valuedyxz = valuedyxz + " "
															+ ent.getValue();
												}
											} else if (name.equals("CFZT")) {
												if (StringHelper
														.isEmpty(valuecfxz)) {
													valuecfxz = ent.getValue();
												} else {
													valuecfxz = valuecfxz + " "
															+ ent.getValue();
												}
											} else  if (name.equals("YYZT")) {
												if (StringHelper
														.isEmpty(valueyyxz)) {
													valueyyxz = ent.getValue();
												} else {
													valueyyxz = valueyyxz + " "
															+ ent.getValue();
												}
											}
										}
									}
								}
								Map<String, String> mapxzy = new HashMap<String, String>();
								mapxzy = getDYandCFandYY_XZY(djdyid,"");
								String valuedyxz = "";
								String valuecfxz = "";
								String valueyyxz = "";
								for (Entry<String, String> ent : mapxzy
										.entrySet()) {
									String name = ent.getKey();
									if (name.equals("DYZT")) {
										if (StringHelper.isEmpty(valuedyxz)) {
											valuedyxz = ent.getValue();
										} else {
											valuedyxz = valuedyxz + " "
													+ ent.getValue();
										}
									} else if (name.equals("CFZT")) {
										if (StringHelper.isEmpty(valuecfxz)) {
											valuecfxz = ent.getValue();
										} else {
											valuecfxz = valuecfxz + " "
													+ ent.getValue();
										}
									} else  if (name.equals("YYZT")) {
										if (StringHelper.isEmpty(valueyyxz)) {
											valueyyxz = ent.getValue();
										} else {
											valueyyxz = valueyyxz + " "
													+ ent.getValue();
										}
									}
								}
								map.put("DYZT", valuedyxz);
								map.put("CFZT", valuecfxz);
								map.put("YYZT", valueyyxz);
							}
						}
					}

		
				}
			}
		}
	@SuppressWarnings("unchecked")
	protected void addRightsHolderInfoyt(
			@SuppressWarnings("rawtypes") List<Map> result,String xzqh) {
		
		String table = "";
		if(xzqh.equals("2")){
			table = "BDCK.BDCS_QLR_XZ@TO_ORCL6_BDCK ql";
		}else if(xzqh.equals("3")){
			table = "BDCK.BDCS_QLR_XZ@TO_ORCL7_BDCK ql";
		}else {
			table = "BDCK.BDCS_QLR_XZ ql";
		}
		if (result != null && result.size() > 0) {
			for (@SuppressWarnings("rawtypes")
			Map map : result) {
				if (map.containsKey("QLID")) {
					String qlid = (String) map.get("QLID");
					String detailssql = "";
						if(xzqh.equals("0")){
							detailssql = "select QLRMC,DLRXM ,DH ,ZJH,QLID from (select QLRMC,DLRXM ,DH ,ZJH,QLID "
									+ " from "+table+"  union all select QLRMC,DLRXM ,DH ,ZJH,QLID "
									+ " from BDCK.BDCS_QLR_XZ@TO_ORCL6_BDCK union all select QLRMC,DLRXM ,DH ,ZJH,QLID "
									+ " from BDCK.BDCS_QLR_XZ@TO_ORCL7_BDCK) ql where ql.QLID= '"+qlid+"' ";
						}else{
						 detailssql = "select QLRMC,DLRXM ,DH ,ZJH "
								+ " from "+table+"  where ql.QLID= '"+qlid+"' ";
						}		
						List<Map> mapyt=baseCommonDao.getDataListByFullSql(detailssql);
						for(Map maps :mapyt){
							map.put("QLRMC",  maps.get("QLRMC"));
							map.put("DH", maps.get("DH"));
							map.put("ZJHM", maps.get("ZJH"));
						}
					
				}
			}
		}
	}
	
			/**
			 * 房屋信息查询
			 * 罗雨/邹彦辉
			 */
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public Message queryHouseJG(Map<String, String> queryvalues, Integer page, Integer rows, boolean iflike,
					String fwzt, String sort, String order, String xzqh, String zddy, String fw_xzqh, String fw_qlrlx,
					String fw_fwyt, String fw_fwlx, String fw_fwxz, String fw_hx, String fw_hxjg, String fw_fwjg,
					String fw_fwcb, String fw_fzdy, String fzlb,String fw_djqdm) {
				Message msg = new Message();
				String ZDDY = zddy != null ? zddy:"";
				//房屋详情
				if(ZDDY.equals("1")){

					List<Map> listresult = null;
					long count = 0;
					StringBuilder build = new StringBuilder();
					StringBuilder buildcount = new StringBuilder();
					StringBuilder fzdy = new StringBuilder();
					StringBuilder buildqlrlx = new StringBuilder();
					StringBuilder buildfwyt = new StringBuilder();
					StringBuilder buildfwlx = new StringBuilder();
					StringBuilder buildfwxz = new StringBuilder();
					StringBuilder buildhx = new StringBuilder();
					StringBuilder buildhxjg = new StringBuilder();
					StringBuilder buildfwjg = new StringBuilder();
					StringBuilder buildfwcb = new StringBuilder();
					StringBuilder builddjq = new StringBuilder();
					StringBuilder builddjqcount = new StringBuilder();
					StringBuilder builddjqdm = new StringBuilder();
					StringBuilder builddjzqdm = new StringBuilder();
					StringBuilder builddjqmc = new StringBuilder();
					StringBuilder builddjzqmc = new StringBuilder();
					String strdjqdm="";
					String strdjzqdm="";
					String djqby = "";
					//行政区划
					StringBuilder buildqs = new StringBuilder();
					StringBuilder buildqscou = new StringBuilder();
					StringBuilder buildyt = new StringBuilder();
					StringBuilder buildytcou = new StringBuilder();
					StringBuilder buildgx = new StringBuilder();
					StringBuilder buildgxcou = new StringBuilder();
					StringBuilder buildyj = new StringBuilder();
					StringBuilder buildyjcou = new StringBuilder();
					StringBuilder union = new StringBuilder();
					union.append(" UNION ALL");
					if (fw_fzdy.equals("房屋类型")) {
						fzdy.append("dy.fwlx"); 
					}else if (fw_fzdy.equals("户型结构")) {
						fzdy.append("dy.HX");
					}else if (fw_fzdy.equals("权利人类型")) {
						fzdy.append("qlr.qlrlx");
					}else if (fw_fzdy.equals("房屋性质")) {
						fzdy.append("dy.fwxz");
					}else if (fw_fzdy.equals("房屋结构")) {
						fzdy.append("dy.fwjg");
					}else if (fw_fzdy.equals("房屋用途")){
						fzdy.append("dy.fwyt");
					}else if (fw_fzdy.equals("户型")) {
						fzdy.append("dy.hx");
					}else if (fw_fzdy.equals("产别")) {
						fzdy.append("dy.fwcb");
					}else {
						fzdy.append("");
					}
					
					//行政区划
					
					//权利人类型
					if(fw_qlrlx.length() > 0 && fw_qlrlx!=null && fw_qlrlx!=""){
						String[] value = fw_qlrlx.split(",");
						for (int i = 0; i < value.length; i++) {
							if (!value[i].equals("10000")) {
								buildqlrlx.delete(0, buildqlrlx.length());
								buildqlrlx.append(" and qlr.qlrlx='"+fw_qlrlx+"'");
							}else {
								buildqlrlx.append(" ");
								break;
							}
						}
					}
					
					//房屋用途
					if(fw_fwyt.length() > 0 && fw_fwyt!=null && fw_fwyt!=""){
						String[] value = fw_fwyt.split(",");
						for (int i = 0; i < value.length; i++) {
							if (!value[i].equals("10000")) {
								buildfwyt.delete(0, buildfwyt.length());
								buildfwyt.append(" and dy.fwyt='"+fw_fwyt+"'");
							}else {
								buildfwyt.append(" ");
								break;
							}
						}
					}
					
					//房屋类型
					if(fw_fwlx.length() > 0 && fw_fwlx!=null && fw_fwlx!=""){
						String[] value = fw_fwlx.split(",");
						for (int i = 0; i < value.length; i++) {
							if (!value[i].equals("10000")) {
								buildfwlx.delete(0, buildfwlx.length());
								buildfwlx.append(" and dy.fwlx='"+fw_fwlx+"'");
							}else {
								buildfwlx.append(" ");
								break;
							}
						}
					}
					
					//房屋性质
					if(fw_fwxz.length() > 0 && fw_fwxz!=null && fw_fwxz!=""){
						String[] value = fw_fwxz.split(",");
						for (int i = 0; i < value.length; i++) {
							if (!value[i].equals("10000")) {
								buildfwxz.delete(0, buildfwxz.length());
								buildfwxz.append(" and dy.fwxz='"+fw_fwxz+"'");
							}else {
								buildfwxz.append(" ");
								break;
							}
						}
					}
					
					//户型
					if(fw_hx.length() > 0 && fw_hx!=null && fw_hx!=""){
						String[] value = fw_hx.split(",");
						for (int i = 0; i < value.length; i++) {
							if (!value[i].equals("10000")) {
								buildhx.delete(0, buildhx.length());
								buildhx.append(" and dy.hx='"+fw_hx+"'");
							}else {
								buildhx.append(" ");
								break;
							}
						}
					}
					
					//户型结构
					if(fw_hxjg.length() > 0 && fw_hxjg!=null && fw_hxjg!=""){
						String[] value = fw_hxjg.split(",");
						for (int i = 0; i < value.length; i++) {
							if (!value[i].equals("10000")) {
								buildhxjg.delete(0, buildhxjg.length());
								buildhxjg.append(" and dy.hxjg='"+fw_hxjg+"'");
							}else {
								buildhxjg.append(" ");
								break;
							}
						}
					}
					
					//房屋结构
					if(fw_fwjg.length() > 0 && fw_fwjg!=null && fw_fwjg!=""){
						String[] value = fw_fwjg.split(",");
						for (int i = 0; i < value.length; i++) {
							if (!value[i].equals("10000")) {
								buildfwjg.delete(0, buildfwjg.length());
								buildfwjg.append(" and dy.fwjg='"+fw_fwjg+"'");
							}else {
								buildfwjg.append(" ");
								break;
							}
						}
					}
					
					//产别
					if(fw_fwcb.length() > 0 && fw_fwcb!=null && fw_fwcb!=""){
						String[] value = fw_fwcb.split(",");
						for (int i = 0; i < value.length; i++) {
							if (!value[i].equals("10000")) {
								buildfwcb.delete(0, buildfwcb.length());
								buildfwcb.append(" and dy.fwcb='"+fw_fwcb+"'");
							}else {
								buildfwcb.append(" ");
								break;
							}
						}
					}
					
			 buildqs.append(" select")
					.append("  DJDY.DJDYID,DY.BDCDYID,DY.ZRZBDCDYID,DY.ZDBDCDYID,DY.BDCDYH,")
					.append("  DY.ZL,DY.FH,DY.GHYT,DY.JZMJ AS SCJZMJ,DY.TNJZMJ AS SCTNJZMJ,")
					.append("  DY.FTJZMJ AS SCFTJZMJ,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC,")
					.append("  QL.QLID,QL.BDCQZH,QL.QLLX");
		  buildqscou.append(" from(")
					.append("  SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
					.append("    FWJG,FWYT1 AS FWYT,HX,FWCB,'031' AS BDCDYLX,")
					.append("    '现房' AS BDCDYLXMC   FROM BDCK.BDCS_H_XZ")
					.append("  UNION ALL ")
					.append("    SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
					.append("    FWJG,FWYT1 AS FWYT,HX,FWCB,'032' AS BDCDYLX,")
					.append("    '期房' AS BDCDYLXMC  FROM BDCK.BDCS_H_XZY")
					.append("  UNION ALL")
					.append("    SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
					.append("    FWJG,FWYT1 AS FWYT,HX,FWCB,'031' AS BDCDYLX,")
					.append("    '现房' AS BDCDYLXMC   FROM BDCK.BDCS_H_XZ@TO_ORCL6_BDCK")
					.append("  UNION ALL ")
					.append("    SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
					.append("    FWJG,FWYT1 AS FWYT,HX,FWCB,'032' AS BDCDYLX,")
					.append("    '期房' AS BDCDYLXMC  FROM BDCK.BDCS_H_XZY@TO_ORCL6_BDCK ")
					.append("  UNION ALL")
					.append("    SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
					.append("    FWJG,FWYT1 AS FWYT,HX,FWCB,'031' AS BDCDYLX,")
					.append("    '现房' AS BDCDYLXMC   FROM BDCK.BDCS_H_XZ@TO_ORCL7_BDCK")
					.append("  UNION ALL ")
					.append("    SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
					.append("    FWJG,FWYT1 AS FWYT,HX,FWCB,'032' AS BDCDYLX,")
					.append("    '期房' AS BDCDYLXMC  FROM BDCK.BDCS_H_XZY@TO_ORCL7_BDCK   ")  
					.append("    ) DY")
					.append("  left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid ")
					.append("  left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid   AND (QLLX=4 OR qllx=99 or qllx=23 or DJLX=600)")
					.append("  left join BDCK.bdcs_qlr_xz qlr on ql.qlid  = qlr.qlid")
					.append("  WHERE DY.BDCDYID is not null and ql.qlid is not null "+buildqlrlx+""+buildfwyt+""+buildfwlx+""+buildfwxz+""+buildhx+""+buildhxjg+""+buildfwjg+""+buildfwcb+"");
			 buildyt.append(" select ")//鹰潭
					.append("  DJDY.DJDYID,DY.BDCDYID,DY.ZRZBDCDYID,DY.ZDBDCDYID,DY.BDCDYH,")
					.append("  DY.ZL,DY.FH,DY.GHYT,DY.JZMJ AS SCJZMJ,DY.TNJZMJ AS SCTNJZMJ,")
					.append("  DY.FTJZMJ AS SCFTJZMJ,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC,")
					.append("  QL.QLID,QL.BDCQZH,QL.QLLX");
		  buildytcou.append(" from")
					.append("    (SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
					.append("    FWJG,FWYT1 AS FWYT,HX,FWCB,'031' AS BDCDYLX,")
					.append("    '现房' AS BDCDYLXMC   FROM BDCK.BDCS_H_XZ")
					.append("  UNION ALL ")
					.append("    SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
					.append("    FWJG,FWYT1 AS FWYT,HX,FWCB,'032' AS BDCDYLX,")
					.append("    '期房' AS BDCDYLXMC  FROM BDCK.BDCS_H_XZY")
					.append("    ) DY")
					.append("  left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid ")
					.append("  left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid   AND (QLLX=4 OR qllx=99 or qllx=23 or DJLX=600)")
					.append("  left join BDCK.bdcs_qlr_xz qlr on ql.qlid  = qlr.qlid")
					.append("  WHERE DY.BDCDYID is not null and ql.qlid is not null "+buildqlrlx+""+buildfwyt+""+buildfwlx+""+buildfwxz+""+buildhx+""+buildhxjg+""+buildfwjg+""+buildfwcb+"");
					//.append("  ORDER BY DY.BDCDYID asc");
			 buildgx.append(" select ")//贵溪
					.append("  DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.FH,DY.GHYT,")
					.append("  DY.JZMJ AS SCJZMJ,DY.TNJZMJ AS SCTNJZMJ, DY.FTJZMJ AS SCFTJZMJ,")
					.append("  DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC,")
					.append("  QL.QLID,QL.BDCQZH,QL.QLLX,'贵溪' as qhmc");
		  buildgxcou.append(" from")
					.append("    (SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
					.append("    FWJG,FWYT1 AS FWYT,HX,FWCB,'031' AS BDCDYLX,")
					.append("    '现房' AS BDCDYLXMC   FROM BDCK.BDCS_H_XZ@TO_ORCL6_BDCK")
					.append("  UNION ALL ")
					.append("    SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
					.append("    FWJG,FWYT1 AS FWYT,HX,FWCB,'032' AS BDCDYLX,")
					.append("    '期房' AS BDCDYLXMC  FROM BDCK.BDCS_H_XZY@TO_ORCL6_BDCK")
					.append("    ) DY")
					.append("  left join BDCK.BDCS_DJDY_XZ@TO_ORCL6_BDCK DJDY on dy.bdcdyid=djdy.bdcdyid ")
					.append("  left join BDCK.bdcs_ql_xz@TO_ORCL6_BDCK ql on ql.djdyid=djdy.djdyid   AND (QLLX=4 OR qllx=99 or qllx=23 or DJLX=600)")
					.append("  left join BDCK.bdcs_qlr_xz@TO_ORCL6_BDCK qlr on ql.qlid  = qlr.qlid")
					.append("  WHERE DY.BDCDYID is not null and ql.qlid is not null "+buildqlrlx+""+buildfwyt+""+buildfwlx+""+buildfwxz+""+buildhx+""+buildhxjg+""+buildfwjg+""+buildfwcb+"");
					//.append("  ORDER BY DY.BDCDYID asc");
		     buildyj.append("  select")//余江
					.append("  DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.FH,DY.GHYT,")
					.append("  DY.JZMJ AS SCJZMJ,DY.TNJZMJ AS SCTNJZMJ,DY.FTJZMJ AS SCFTJZMJ,")
					.append("  DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC,")
					.append("  QL.QLID,QL.BDCQZH,QL.QLLX,'余江' as qhmc");
		  buildyjcou.append(" from")
					.append("    (SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
					.append("    FWJG,FWYT1 AS FWYT,HX,FWCB,'031' AS BDCDYLX,")
					.append("    '现房' AS BDCDYLXMC   FROM BDCK.BDCS_H_XZ@TO_ORCL7_BDCK")
					.append("  UNION ALL ")
					.append("    SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
					.append("    FWJG,FWYT1 AS FWYT,HX,FWCB,'032' AS BDCDYLX,")
					.append("    '期房' AS BDCDYLXMC  FROM BDCK.BDCS_H_XZY@TO_ORCL7_BDCK")
					.append("    ) DY")
					.append("  left join BDCK.BDCS_DJDY_XZ@TO_ORCL7_BDCK DJDY on dy.bdcdyid=djdy.bdcdyid ")
					.append("  left join BDCK.bdcs_ql_xz@TO_ORCL7_BDCK ql on ql.djdyid=djdy.djdyid   AND (QLLX=4 OR qllx=99 or qllx=23 or DJLX=600)")
					.append("  left join BDCK.bdcs_qlr_xz@TO_ORCL7_BDCK qlr on ql.qlid  = qlr.qlid")
					.append("  WHERE DY.BDCDYID is not null and ql.qlid is not null "+buildqlrlx+""+buildfwyt+""+buildfwlx+""+buildfwxz+""+buildhx+""+buildhxjg+""+buildfwjg+""+buildfwcb+"");
					//.append("  ORDER BY DY.BDCDYID asc");
					
					//地籍区
						   if(fw_djqdm.length() >0 && fw_djqdm !=null && fw_djqdm != ""){
							   String[] value = fw_djqdm.split(",");
							   for(int i =0; i<value.length;i++){
								if (value[i].length() == 9) {
									strdjqdm+="'"+value[i]+"',";
							}else{
								strdjzqdm+="'"+value[i]+"',";
							}
							  }
							if (strdjqdm.length()>0) {
								strdjqdm=strdjqdm.substring(0, strdjqdm.length()-1);
						}
							if (strdjzqdm.length()>0) {
								strdjzqdm=strdjzqdm.substring(0, strdjzqdm.length()-1);
						}
							if (strdjqdm!="") {
								builddjqdm.append("and bdcdy.DJQDM in("+strdjqdm+")");
						}
							if (strdjzqdm!="") {
								builddjzqdm.append("and bdcdy.DJZQDM in("+strdjzqdm+")");
						}
						 }
						
			   build.append(" select")
					.append(" DJDY.DJDYID,DY.BDCDYID,DY.ZRZBDCDYID,DY.ZDBDCDYID,DY.BDCDYH,")
					.append(" DY.ZL,DY.FH,DY.GHYT,DY.JZMJ AS SCJZMJ,DY.TNJZMJ AS SCTNJZMJ,")
					.append(" DY.FTJZMJ AS SCFTJZMJ,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC,")
					.append(" QL.QLID,QL.BDCQZH,QL.QLLX");
		  buildcount.append(" from")
					.append(" (SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
					.append(" FWJG,FWYT1 AS FWYT,HX,FWCB,'031' AS BDCDYLX,")
					.append(" '现房' AS BDCDYLXMC   FROM BDCK.BDCS_H_XZ")
					.append(" UNION ALL ")
					.append(" SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
					.append(" FWJG,FWYT1 AS FWYT,HX,FWCB,'032' AS BDCDYLX,")
					.append(" '期房' AS BDCDYLXMC  FROM BDCK.BDCS_H_XZY") 
					.append(" ) DY")
			   		.append(" left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid ") 
			   		.append(" left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid   AND (QLLX=4 OR qllx=99 or qllx=23 or DJLX=600)") 
			   		.append(" left join BDCK.bdcs_qlr_xz qlr on ql.qlid  = qlr.qlid") 
			   		.append(" LEFT JOIN BDCK.BDCS_const const on "+fzdy+" = const.constvalue") 
			   		.append(" LEFT JOIN BDCK.BDCS_CONSTCLS CONSTCLS on const.CONSTSLSID = CONSTCLS.CONSTSLSID") 
			   		.append(" WHERE const.CONSTTRANS='"+fzlb+"' and ql.qlid is not null "+buildqlrlx+""+buildfwyt+""+buildfwlx+""+buildfwxz+""+buildhx+""+buildhxjg+""+buildfwjg+""+buildfwcb);//+" ORDER BY DY.BDCDYID asc");
					  
		  			  if(fw_fzdy.equals("地籍区")){
		  				builddjqmc.append("and djqmc='"+fzlb+"'");
		  			  }else if (fw_fzdy.equals("地籍子区")) {
		  				builddjzqmc.append("and djzqmc='"+fzlb+"'"); 
		  			  }else{
		  				builddjqmc.append("");
		  				builddjzqmc.append("");
		  			  }
		      builddjq.append(" select")
					  .append("	DJDY.DJDYID,DY.BDCDYID,DY.ZRZBDCDYID,DY.ZDBDCDYID,DY.BDCDYH,")
					  .append("  DY.ZL,DY.FH,DY.GHYT,DY.JZMJ AS SCJZMJ,DY.TNJZMJ AS SCTNJZMJ,")
					  .append("	DY.FTJZMJ AS SCFTJZMJ,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC,")
					  .append("	QL.QLID,QL.BDCQZH,QL.QLLX,zd.djqmc,zd.djzqmc");
		 builddjqcount.append(" from(")
					  .append("   SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
					  .append("	 FWJG,FWYT1 AS FWYT,HX,FWCB,'031' AS BDCDYLX,")
					  .append("	 '现房' AS BDCDYLXMC   FROM BDCK.BDCS_H_XZ")
					  .append(" UNION ALL ")
					  .append("	 SELECT  ZL,BDCDYH,BDCDYID,ZRZBDCDYID,ZDBDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,FWLX,HXJG,FWXZ,")
					  .append("	 FWJG,FWYT1 AS FWYT,HX,FWCB,'032' AS BDCDYLX,")
					  .append("	 '期房' AS BDCDYLXMC  FROM BDCK.BDCS_H_XZY ")
					  .append("		) DY")
					  .append("	 left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid  ")
					  .append("	 left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid   AND (QLLX=4 OR qllx=99 or qllx=23 or DJLX=600) ")
					  .append("	 left join BDCK.bdcs_qlr_xz qlr on ql.qlid  = qlr.qlid ")
					  .append("  left join BDCK.BDCS_SHYQZD_XZ ZD ON DY.ZDBDCDYID=ZD.BDCDYID")
					  .append("  WHERE DY.BDCDYID is not null and ql.qlid is not null "+builddjqmc+" "+builddjzqmc);//+" ORDER BY DY.BDCDYID asc");
		 			CommonDao dao = baseCommonDao;
					
					if (fw_fzdy.equals("行政区划")) {
						if (fzlb.equals("全市")) {
							count = dao.getCountByFullSql(buildqscou.toString());
							listresult = dao.getPageDataByFullSql(buildqs.toString()+buildqscou.toString(), page, rows);
						}else if(fzlb.equals("市本级")){
							count = dao.getCountByFullSql(buildytcou.toString());
							listresult = dao.getPageDataByFullSql(buildyt.toString()+buildytcou.toString(), page, rows);
						}else if(fzlb.equals("贵溪")){
							count = dao.getCountByFullSql(buildgxcou.toString());
							listresult = dao.getPageDataByFullSql(buildgx.toString()+buildgxcou.toString(), page, rows);
						}else if(fzlb.equals("余江")){
							count = dao.getCountByFullSql(buildyjcou.toString());
							listresult = dao.getPageDataByFullSql(buildyj.toString()+buildyjcou.toString(), page, rows);
						}
					}else if(fw_fzdy.equals("地籍区") || fw_fzdy.equals("地籍子区")){
						count = dao.getCountByFullSql(builddjqcount.toString());
						listresult = dao.getPageDataByFullSql(builddjq.toString()+builddjqcount.toString(), page, rows);
					}else {
						count = dao.getCountByFullSql(buildcount.toString());
						listresult = dao.getPageDataByFullSql(build.toString()+buildcount.toString(), page, rows);
					}
					
					List<Map> list = new ArrayList<Map>();
					if(listresult.size()>0 && listresult.size()<5){
						for(Map map : listresult){
							if(map.get("BDCDYLXMC").equals("现房")){
								list.add(map);
							}
						}
					}
					if(list.size()>0){
					 addRightsHolderInfo(list);
						addLimitStatus(list,"");
						addDyCfDetails(list);
						isGlzd(list);
				        addQLLX(list);
				        addQXGL(list);
						
						// 格式化结果中的常量值

						for (Map map : list) {
							if (map.containsKey("GHYT")) {
								String value = StringHelper.formatObject(StringHelper
										.isEmpty(map.get("GHYT")) ? "" : map.get("GHYT"));
								String name = "";
								if (!StringHelper.isEmpty(value)) {
									name = ConstHelper.getNameByValue("FWYT", value);
								}
								map.put("GHYTname", name);
							}
						}
						msg.setTotal(count);
						msg.setRows(list);
					}else{
					
					if (fzlb.equals("鹰潭市") || fzlb.equals("市本级")) {
						addRightsHolderInfo(listresult);
						addLimitStatus(listresult,"");
						addDyCfDetails(listresult);
						isGlzd(listresult);
				        addQLLX(listresult);
				        addQXGL(listresult);
						
					}else{
						String XZQH = "";
						if(fzlb.equals("贵溪")){
							XZQH = "2";
						}else if(fzlb.equals("余江")){
							XZQH = "3";
						}
						addRightsHolderInfoyt(listresult,XZQH);
						addLimitStatusyt(listresult,XZQH);
						addDyCfDetailsyt(listresult,XZQH);
						isGlzdyt(listresult,XZQH);
						addQLLX(listresult);
						addQXGLYT(listresult,XZQH);
						
						/*addRightsHolderInfo(listresult);
						addLimitStatus(listresult);
						addDyCfDetails(listresult);
						isGlzd(listresult);
				        addQLLX(listresult);
				        addQXGL(listresult);*/
					}
					// 格式化结果中的常量值
					for (Map map : listresult) {
						if (map.containsKey("GHYT")) {
							String value = StringHelper.formatObject(StringHelper
									.isEmpty(map.get("GHYT")) ? "" : map.get("GHYT"));
								String name = "";
								if (!StringHelper.isEmpty(value)) {
									name = ConstHelper.getNameByValue("FWYT", value);
								}
									map.put("GHYTname", name);
							}
						}
						msg.setTotal(count);
						msg.setRows(listresult);
					}
				}else{//------------------------------------------------------------------------分割线------------------------------------------------------------------------
				long count = 0;
				String XZQH = xzqh != null ? xzqh:"";
				List<Map> listresult = null;
				String cxzt=queryvalues.get("CXZT");
				queryvalues.remove("CXZT");
				String fsqlcfwh=queryvalues.get("FSQL.CFWH");
				
				/* ===============1、先获取实体对应的表名=================== */
				String unitentityName = "BDCK.BDCS_H_XZ";
				if ("2".equals(cxzt)) {
					unitentityName = "BDCK.BDCS_H_LS";
				}

				/* ===============2、再获取表名+'_'+字段名=================== */
				String dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.FH,DY.GHYT,DY.FWBM,DY.SCJZMJ,DY.SCTNJZMJ,DY.SCFTJZMJ,'031' AS BDCDYLX ,'现房' AS BDCDYLXMC ";
				String qlfieldsname = "QL.QLID,QL.BDCQZH,QL.QLLX";
				String fsqlfieldsname = "FSQL.FSQLID,FSQL.CFWH";

				if (fwzt != null && fwzt.equals("2")) {
					unitentityName = "BDCK.BDCS_H_XZY";
					if ("2".equals(cxzt)) {
						unitentityName = "BDCK.BDCS_H_LSY";
					}
					dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.FH,DY.GHYT,DY.FWBM,DY.YCJZMJ AS SCJZMJ,DY.YCTNJZMJ AS SCTNJZMJ,DY.YCFTJZMJ AS SCFTJZMJ,'032' AS BDCDYLX,'期房' AS BDCDYLXMC ";
				}

				// 统一期现房 2015年10月28日
				if (fwzt != null && fwzt.equals("3")) {
					unitentityName = "(SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ ,'031' AS BDCDYLX ,'现房' AS BDCDYLXMC FROM BDCK.BDCS_H_XZ  UNION ALL SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ  ,'032' AS BDCDYLX ,'期房' AS BDCDYLXMC FROM BDCK.BDCS_H_XZY)";
					if ("2".equals(cxzt)) {
						unitentityName = "(SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ ,'031' AS BDCDYLX ,'现房' AS BDCDYLXMC FROM BDCK.BDCS_H_LS  UNION ALL SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ  ,'032' AS BDCDYLX ,'期房' AS BDCDYLXMC FROM BDCK.BDCS_H_LSY)";
					}
					dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.FH,DY.GHYT,DY.JZMJ AS SCJZMJ,DY.TNJZMJ AS SCTNJZMJ,DY.FTJZMJ AS SCFTJZMJ,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC";
				}

				/* ===============3、构造查询语句=================== */
				/* SELECT 字段部分 */
				StringBuilder builder2 = new StringBuilder();
				builder2.append("select ").append(dyfieldsname).append(",")
						.append(qlfieldsname);
				if(!StringHelper.isEmpty(fsqlcfwh)){
					builder2.append(",").append(fsqlfieldsname);
				}
				//全市监管
				if(XZQH.equals("2")){
					builder2.append(",").append("'贵溪' as qhmc ");
				}else if(XZQH.equals("3")){
					builder2.append(",").append("'余江' as qhmc ");
				}else{
					builder2.append(",").append("'市本级' as qhmc ");
				}
				String selectstr = builder2.toString();

				/* FROM 后边的表语句 */
				StringBuilder builder = new StringBuilder();
				if ("2".equals(cxzt)) {
					builder.append(" from {0} DY")
							.append(" left join BDCK.BDCS_DJDY_LS DJDY on dy.bdcdyid=djdy.bdcdyid")
							.append(" left join BDCK.BDCS_QL_LS ql on ql.djdyid=djdy.djdyid  ");
				}else{
					builder.append(" from {0} DY")
							.append(" left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid")
							.append(" left join BDCK.BDCS_QL_XZ ql on ql.djdyid=djdy.djdyid  ");
				}
				if(!StringHelper.isEmpty(fsqlcfwh)){
					builder.append(" left join BDCK.BDCS_FSQL_XZ fsql on ql.fsqlid=fsql.fsqlid  ");
				}
				String fromstr = builder.toString();
				fromstr = MessageFormat.format(fromstr, unitentityName);

				/* WHERE 条件语句 */
				StringBuilder builder3 = new StringBuilder();
				builder3.append(" where  ");
				StringBuilder qlrbuilder = new StringBuilder();
				StringBuilder dyrbuilder = new StringBuilder();
				boolean havecondition = false;
				boolean haveqlr = false;
				boolean havedyr = false;
				for (Entry<String, String> ent : queryvalues.entrySet()) {
					String name = ent.getKey();
					String value = ent.getValue();
					if (!StringHelper.isEmpty(value)) {
						// 抵押状态和查封状态，为‘0’的时候表示不过滤，查询全部
						if ((name.equals("CFZT") && value.equals("0"))
								|| (name.equals("DYZT") && value.equals("0"))
									||name.equals("YYZT") && value.equals("0")) {
							continue;
						}

						// 权利人，权利人的条件比较特殊，where里边又套了where
						if (name.startsWith("QLR.")) {
							if (haveqlr)
								qlrbuilder.append(" and ");
							if (iflike) {
								qlrbuilder.append(" instr(" + name + ",'" + value
										+ "')>0 ");
							} else {
								if (name.equals("QLR.ZJH") && value.length() == 18) {
									String oldCard = ConverIdCard.getOldIDCard(value);
									if (oldCard.length() != 15) {
										qlrbuilder.append(" " + name + "='" + value
												+ "' ");
									} else {
										qlrbuilder.append(" (" + name + "='" + value
												+ "' or ");
										qlrbuilder.append(" " + name + "='" + oldCard
												+ "') ");
									}
								} else {
									qlrbuilder.append(" " + name + "='" + value + "' ");
								}
							}
							haveqlr = true;
							continue;
						}
						// 抵押人判断
						if (name.startsWith("DYR.")) {
							if (havedyr)
								dyrbuilder.append(" and ");
							dyrbuilder.append(" " + name + "='" + value + "' ");
							havedyr = true;
							continue;
						}
						if (havecondition) {
							builder3.append(" and ");
						}

						// 抵押状态
						if (name.equals("DYZT")) {
							if (value.equals("1")) {
								if ("2".equals(cxzt)) {
									builder3.append(" djdy.djdyid NOT IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_LS DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
								}else{
									builder3.append(" djdy.djdyid NOT IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_XZ DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
								}
							} else {
								if ("2".equals(cxzt)) {
									builder3.append(" djdy.djdyid IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_LS DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
								}else{
									builder3.append(" djdy.djdyid IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_XZ DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
								}
							}
							havecondition = true;
							continue;
						}
						// 查封状态
						if (name.equals("CFZT")) {
							if (value.equals("1")) {
								if ("2".equals(cxzt)) {
									builder3.append("  djdy.DJDYID NOT IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_LS CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99') ");
								}else{
									builder3.append("  djdy.DJDYID NOT IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_XZ CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99') ");
								}
							} else {
								if ("2".equals(cxzt)) {
									builder3.append("  djdy.DJDYID IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_LS CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99') ");
								}else{
									builder3.append("  djdy.DJDYID IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_XZ CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99') ");
								}
							}
							havecondition = true;
							continue;
						}
						// 异议状态
						if (name.equals("YYZT")) {
							if (value.equals("1")) {
								if ("2".equals( cxzt)) {
									builder3.append("  djdy.DJDYID NOT IN (SELECT YY.DJDYID FROM BDCK.BDCS_QL_LS YY WHERE YY.DJDYID=DJDYID AND YY.djlx = '600') ");
								}else{
									builder3.append("  djdy.DJDYID NOT IN (SELECT YY.DJDYID FROM BDCK.BDCS_QL_XZ YY WHERE YY.DJDYID=DJDYID AND YY.djlx = '600') ");
								}
							} else {
								if ("2".equals(cxzt)) {
									builder3.append("  djdy.DJDYID IN (SELECT YY.DJDYID FROM BDCK.BDCS_QL_LS YY WHERE YY.DJDYID=DJDYID AND  YY.djlx = '600') ");
								}else{
									builder3.append("  djdy.DJDYID IN (SELECT YY.DJDYID FROM BDCK.BDCS_QL_XZ YY WHERE YY.DJDYID=DJDYID AND  YY.djlx = '600') ");
								}
							}
							havecondition = true;
							continue;
						}
						if (iflike) {
							builder3.append(" instr(" + name + ",'" + value + "')>0 ");
						} else {
							/*
							 * 如果通过不动产权证查询，且是精确查询时,
							 * 其中如果不动产权证号是否纯数字的，直接在权利人表中通过BDCQZHXH条件查询，
							 * 否则从权利表中通过BDCQZH条件查询
							 */
							if (name.contains("BDCQZH")) {
								// 验证不动产权证号是否为纯数字的
								boolean flag = StringHelper.isNumericByString(value);
								if (flag) {
									// 判断是否已经有查询权利人的条件了
									if (haveqlr) {
										qlrbuilder.append(" and ");
									}
									qlrbuilder.append(" QLR.BDCQZHXH " + "='" + value
											+ "' ");
									haveqlr = true;
									continue;
								} else {
									builder3.append(" " + name + "='" + value + "' ");
								}
							} else {
								builder3.append(" " + name + "='" + value + "' ");
							}
						}
						havecondition = true;
					}
				}
				// 有权利人查询条件
				if (!StringHelper.isEmpty(qlrbuilder.toString())) {
					if (havecondition) {
						builder3.append(" and ");
					}
					if ("2".equals(cxzt)) {
						builder3.append(" EXISTS (SELECT 1 FROM BDCK.BDCS_QLR_LS QLR WHERE QLR.QLID=QL.QLID AND "
								+ qlrbuilder.toString() + ")");
					}else{
						builder3.append(" EXISTS (SELECT 1 FROM BDCK.BDCS_QLR_XZ QLR WHERE QLR.QLID=QL.QLID AND "
								+ qlrbuilder.toString() + ")");
					}
					havecondition = true;
				}

				// 有抵押人查询条件
				if (!StringHelper.isEmpty(dyrbuilder.toString())) {
					if (havecondition) {
						builder3.append(" and ");
					}
					if ("2".equals(cxzt)) {
						builder3.append(" QL.QLID IN (SELECT QLID FROM BDCK.BDCS_FSQL_LS DYR WHERE "
								+ dyrbuilder.toString() + ")");
					}else{
						builder3.append(" QL.QLID IN (SELECT QLID FROM BDCK.BDCS_FSQL_XZ DYR WHERE "
								+ dyrbuilder.toString() + ")");
					}
					havecondition = true;
				}

				if (fwzt == null || !fwzt.equals("2")) {
					if (havecondition) {
						// builder3.append(" and ");
					}
					// builder3.append(" ql.qllx='4'");
				}
				
				String wherestr = builder3.toString();
				if (wherestr.trim().toUpperCase().endsWith("WHERE")) {
					wherestr = "";
				}
				String fromSql = " from (select ql.qlid " + fromstr + wherestr + ")";
				String fullSql = selectstr + fromstr + wherestr;
//				/* 排序 条件语句 */
//				if(!StringUtils.isEmpty(sort)&&!StringUtils.isEmpty(order))
//				{
//				if(sort.toUpperCase().equals("ZL"))
//					sort="DY.ZL";
//				if(sort.toUpperCase().equals("BDCDYH"))
//					sort="DY.BDCDYH";
//				/*if(sort.toUpperCase().equals("QLRMC"))
//					sort="QLR.QLRMC";
//				if(sort.toUpperCase().equals("ZJH"))
//					sort="QLR.ZJH";*/
//				if(sort.toUpperCase().equals("BDCQZH"))
//					sort="QL.BDCQZH";
//				if(sort.toUpperCase().equals("FH"))
//					sort="DY.FH";
//				if(sort.toUpperCase().equals("QLLX"))
//					sort="QL.QLLX";
//				/*if(sort.toUpperCase().equals("ZJHM"))
//					sort="QLR.ZJHM";*/
//				if(sort.toUpperCase().equals("GHYTNAME"))
//					sort="DY.GHYT";
//				if(sort.toUpperCase().equals("BDCDYID"))
//					sort="DY.BDCDYID";
//				if(sort.toUpperCase().equals("BDCDYLXMC"))
//					sort="DY.BDCDYLX";		
//					fullSql=fullSql;
//				}
				/* 为了提高效率，先不查询符合条件的记录总数，将来改为异步获取 */
				CommonDao dao = baseCommonDao;
				String fromSqlgx = "";
				String fromSqlyj = "";
				String fullSqlgx = "";
				String fullSqlyj = "";
				long countgx = 0;
				long countyj = 0;
				if(XZQH.equals("2")){
				
						fromSql = getfromSQLGX(fromSql ,  cxzt);
						fullSql = getfullSQLGX(fullSql ,  cxzt);

				}else if(XZQH.equals("3")){
					fromSql = getfromSQLYJ(fromSql , cxzt);
					fullSql = getfullSQLYJ(fullSql , cxzt);

				}else if(XZQH.equals("0")){
					fromSqlgx = getfromSQLGX(fromSql ,  cxzt);
					fromSqlyj = getfromSQLYJ(fromSql ,  cxzt);
					fullSqlgx = getfullSQLGX(fullSql ,  cxzt);
					fullSqlgx = fullSqlgx.replaceAll("市本级", "贵溪");
					fullSqlyj = getfullSQLYJ(fullSql ,  cxzt);
					fullSqlyj = fullSqlyj.replaceAll("市本级", "余江");
					countgx = dao.getCountByFullSql(fromSqlgx);
					countyj = dao.getCountByFullSql(fromSqlyj);
					fullSql = fullSql +" union all "+ fullSqlgx +" union all "+ fullSqlyj;
				}

				count = dao.getCountByFullSql(fromSql)+countgx+countyj;
				listresult = dao.getPageDataByFullSql(fullSql, page, rows);
				if(XZQH.equals("1")){
					addRightsHolderInfo(listresult);
					addLimitStatus(listresult,"");
					addDyCfDetails(listresult);
					isGlzd(listresult);
					addQLLX(listresult);
					addQXGL(listresult);
				}else{
					//鹰潭全市监管
					addRightsHolderInfoyt(listresult,XZQH);
					addLimitStatusyt(listresult,XZQH);
					addDyCfDetailsyt(listresult,XZQH);
					isGlzdyt(listresult,XZQH);
					addQLLX(listresult);
					addQXGLYT(listresult,XZQH);
					
				}	
				// 格式化结果中的常量值

				for (Map map : listresult) {
					if (map.containsKey("GHYT")) {
						String value = StringHelper.formatObject(StringHelper
								.isEmpty(map.get("GHYT")) ? "" : map.get("GHYT"));
						String name = "";
						if (!StringHelper.isEmpty(value)) {
							name = ConstHelper.getNameByValue("FWYT", value);
						}
						map.put("GHYTname", name);
					}
				}
					msg.setTotal(count);
					msg.setRows(listresult);
				}
				return msg;
			}
	
			/**
			 * 土地信息查询
			 * 罗雨/邹彦辉
			 */
			@SuppressWarnings({ "rawtypes" })
			@Override
			public Message queryLandJG(Map<String, String> queryvalues, Integer page, Integer rows, boolean iflike,
					String tdzt, String sort, String order, String xzqh, String td_xzqh, String td_qllx, String td_qlsdfs,
					String td_qlxz, String td_tddj, String td_qlrlx, String td_tdyt, String td_fzdy, String fzlb,
					String zddy,String td_djqdm){
				Message msg = new Message();
				//如果等于null则等于""
				String ZDDY = zddy != null ? zddy:"";
				//房屋详情
				if(ZDDY.equals("2")){
					List<Map> listresult = null;
					long count = 0;
					StringBuilder build = new StringBuilder();
					StringBuilder buildcount = new StringBuilder();
					StringBuilder fzdy = new StringBuilder();
					StringBuilder buildxzqh = new StringBuilder();
					StringBuilder buildqllx = new StringBuilder();
					StringBuilder buildqlsdfs = new StringBuilder();
					StringBuilder buildqlxz = new StringBuilder();
					StringBuilder buildtddj = new StringBuilder();
					StringBuilder buildqlrlx = new StringBuilder();
					StringBuilder buildtdyt = new StringBuilder();
					StringBuilder builddjq = new StringBuilder();
					StringBuilder builddjqcount = new StringBuilder();
					StringBuilder builddjqdm = new StringBuilder();
					StringBuilder builddjzqdm = new StringBuilder();
					StringBuilder builddjqmc = new StringBuilder();
					StringBuilder builddjzqmc = new StringBuilder();
					String strdjqdm="";
					String strdjzqdm="";
					//行政区划
					StringBuilder buildqs = new StringBuilder();
					StringBuilder buildqscou = new StringBuilder();
					StringBuilder buildyt = new StringBuilder();
					StringBuilder buildytcou = new StringBuilder();
					StringBuilder buildgx = new StringBuilder();
					StringBuilder buildgxcou = new StringBuilder();
					StringBuilder buildyj = new StringBuilder();
					StringBuilder buildyjcou = new StringBuilder();
					StringBuilder union = new StringBuilder();
					union.append(" UNION ALL");
					if (td_fzdy.equals("行政区划")) {
						fzdy.append("ql.qllx");
					}else if (td_fzdy.equals("权利类型")) {
						fzdy.append("ql.qllx"); 
					}else if (td_fzdy.equals("权利设定方式")) {
						fzdy.append("dy.qlsdfs");
					}else if (td_fzdy.equals("权利性质")) {
						fzdy.append("dy.qlxz");
					}else if (td_fzdy.equals("土地等级")) {
						fzdy.append("tdyt.tddj");
					}else if (td_fzdy.equals("权利人类型")) {
						fzdy.append("qlr.qlrlx");
					}else if (td_fzdy.equals("土地用途")){
						fzdy.append("tdyt.tdyt");
					}else {
						fzdy.append("ql.qllx");
					}
					
					//权利类型
					if(td_qllx.length() > 0 && td_qllx!=null && td_qllx!=""){
						String[] value = td_qllx.split(",");
						for (int i = 0; i < value.length; i++) {
							if (!value[i].equals("10000")) {
								buildqllx.delete(0, buildqllx.length());
								buildqllx.append(" and ql.qllx='"+td_qllx+"'");
							}else {
								buildqllx.append(" ");
								break;
							}
						}
					}
					
					//权利设定方式
					if(td_qlsdfs.length() > 0 && td_qlsdfs!=null && td_qlsdfs!=""){
						String[] value = td_qlsdfs.split(",");
						for (int i = 0; i < value.length; i++) {
							if (!value[i].equals("10000")) {
								buildqlsdfs.delete(0, buildqlsdfs.length());
								buildqlsdfs.append(" and dy.qlsdfs='"+td_qlsdfs+"'");
							}else {
								buildqlsdfs.append(" ");
								break;
							}
						}
					}
					
					//权利性质
					if(td_qlxz.length() > 0 && td_qlxz!=null && td_qlxz!=""){
						String[] value = td_qlxz.split(",");
						for (int i = 0; i < value.length; i++) {
							if (!value[i].equals("10000")) {
								buildqlxz.delete(0, buildqlxz.length());
								buildqlxz.append(" and dy.qlxz='"+td_qlxz+"'");
							}else {
								buildqlxz.append(" ");
								break;
							}
						}
					}
					
					//土地等级
					if(td_tddj.length() > 0 && td_tddj!=null && td_tddj!=""){
						String[] value = td_tddj.split(",");
						for (int i = 0; i < value.length; i++) {
							if (!value[i].equals("10000")) {
								buildtddj.delete(0, buildtddj.length());
								buildtddj.append(" and tdyt.tddj='"+td_tddj+"'");
							}else {
								buildtddj.append(" ");
								break;
							}
						}
					}
					
					//权利人类型
					if(td_qlrlx.length() > 0 && td_qlrlx!=null && td_qlrlx!=""){
						String[] value = td_qlrlx.split(",");
						for (int i = 0; i < value.length; i++) {
							if (!value[i].equals("10000")) {
								buildqlrlx.delete(0, buildqlrlx.length());
								buildqlrlx.append(" and qlr.qlrlx='"+td_qlrlx+"'");
							}else {
								buildqlrlx.append(" ");
								break;
							}
						}
					}
					
					//土地用途
					if(td_tdyt.length() > 0 && td_tdyt!=null && td_tdyt!=""){
						String[] value = td_tdyt.split(",");
						for (int i = 0; i < value.length; i++) {
							if (!value[i].equals("10000")) {
								buildtdyt.delete(0, buildtdyt.length());
								buildtdyt.append(" and tdyt.tdyt='"+td_tdyt+"'");
							}else {
								buildtdyt.append(" ");
								break;
							}
						}
					}
					
					
			 buildqs.append(" select")
					.append("    DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,")
					.append("    DY.BDCDYLXMC AS BDCDYLXMC,QL.QLID,QL.BDCQZH,QL.QLLX AS QLLX,QL.DJLX,DY.BDCDYLX AS BDCDYLX,")
					.append("    DY.ZDMJ AS ZDMJ,QL.QDJG,DY.QLSDFS,DY.QLXZ,TDYT.TDDJ AS TDDJ,QLR.QLRLX as QLRLX,TDYT.TDYT AS TDYT");
		  buildqscou.append(" from (")
					.append("   SELECT ")
					.append("      ZL,ZDDM,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC,ZDMJ,QLSDFS,QLXZ")
					.append("   FROM BDCK.BDCS_SYQZD_XZ")
					.append("   UNION ALL")
					.append("   SELECT ")
					.append("      ZL,ZDDM,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC ,ZDMJ,QLSDFS,QLXZ")
					.append("   FROM BDCK.BDCS_SHYQZD_XZ")
					.append("   UNION ALL")
					.append("   ")
					.append("   SELECT")
					.append("      ZL,ZDDM,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC,ZDMJ,QLSDFS,QLXZ")
					.append("   FROM BDCK.BDCS_SYQZD_XZ@TO_ORCL6_BDCK")
					.append("   UNION ALL")
					.append("   SELECT ")
					.append("      ZL,ZDDM,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC ,ZDMJ,QLSDFS,QLXZ")
					.append("   FROM BDCK.BDCS_SHYQZD_XZ@TO_ORCL6_BDCK")
					.append("   ")
					.append("   UNION ALL")
					.append("   SELECT ")
					.append("      ZL,ZDDM,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC,ZDMJ,QLSDFS,QLXZ")
					.append("   FROM BDCK.BDCS_SYQZD_XZ@TO_ORCL7_BDCK")
					.append("   UNION ALL")
					.append("   SELECT ")
					.append("      ZL,ZDDM,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC ,ZDMJ,QLSDFS,QLXZ")
					.append("   FROM BDCK.BDCS_SHYQZD_XZ@TO_ORCL7_BDCK")
					.append("   ) DY")
					.append("    left join BDCK.bdcs_tdyt_xz tdyt on dy.bdcdyid=tdyt.bdcdyid")
					.append("    left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid")
					.append("    left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid AND (QLLX=3 OR qllx=99 or qllx=23 or DJLX=600)")
					.append("    left join BDCK.bdcs_qlr_xz qlr on ql.qlid  = qlr.qlid")
					.append("    WHERE DY.BDCDYID is not null and ql.qlid is not null "+buildqllx+""+buildqlsdfs+""+buildqlxz+""+buildtddj+""+buildqlrlx+""+buildtdyt+"");
			 buildyt.append(" select")//鹰潭
					.append("    DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,")
					.append("    DY.BDCDYLXMC AS BDCDYLXMC,QL.QLID,QL.BDCQZH,QL.QLLX AS QLLX,QL.DJLX,DY.BDCDYLX AS BDCDYLX,")
					.append("    DY.ZDMJ AS ZDMJ,QL.QDJG,DY.QLSDFS,DY.QLXZ,TDYT.TDDJ AS TDDJ,QLR.QLRLX as QLRLX,TDYT.TDYT AS TDYT");
		  buildytcou.append(" from ( ")
					.append(" SELECT ")
					.append("      ZL,ZDDM,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC,ZDMJ,QLSDFS,QLXZ")
					.append("   FROM BDCK.BDCS_SYQZD_XZ")
					.append("   UNION ALL")
					.append("   SELECT ")
					.append("      ZL,ZDDM,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC ,ZDMJ,QLSDFS,QLXZ")
					.append("   FROM BDCK.BDCS_SHYQZD_XZ")
					.append("   ) DY")
					.append("    left join BDCK.bdcs_tdyt_xz tdyt on dy.bdcdyid=tdyt.bdcdyid")
					.append("    left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid")
					.append("    left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid AND (QLLX=3 OR qllx=99 or qllx=23 or DJLX=600)")
					.append("    left join BDCK.bdcs_qlr_xz qlr on ql.qlid  = qlr.qlid")
					.append("    WHERE DY.BDCDYID is not null and ql.qlid is not null "+buildqllx+""+buildqlsdfs+""+buildqlxz+""+buildtddj+""+buildqlrlx+""+buildtdyt+"");
					//.append("    ORDER BY DY.BDCDYID asc");
		     buildgx.append(" select ")//贵溪
					.append("    DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,")
					.append("    DY.BDCDYLXMC AS BDCDYLXMC,QL.QLID,QL.BDCQZH,QL.QLLX AS QLLX,QL.DJLX,DY.BDCDYLX AS BDCDYLX,")
					.append("    DY.ZDMJ AS ZDMJ,QL.QDJG,DY.QLSDFS,DY.QLXZ,TDYT.TDDJ AS TDDJ,QLR.QLRLX as QLRLX,TDYT.TDYT AS TDYT,'贵溪' as qhmc");
		  buildgxcou.append(" from ( ")
					.append(" SELECT ")
					.append("      ZL,ZDDM,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC,ZDMJ,QLSDFS,QLXZ")
					.append("   FROM BDCK.BDCS_SYQZD_XZ@TO_ORCL6_BDCK")
					.append("   UNION ALL")
					.append("   SELECT ")
					.append("      ZL,ZDDM,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC ,ZDMJ,QLSDFS,QLXZ")
					.append("   FROM BDCK.BDCS_SHYQZD_XZ@TO_ORCL6_BDCK")
					.append("   ) DY")
					.append("    left join BDCK.bdcs_tdyt_xz@TO_ORCL6_BDCK tdyt on dy.bdcdyid=tdyt.bdcdyid")
					.append("    left join BDCK.BDCS_DJDY_XZ@TO_ORCL6_BDCK DJDY on dy.bdcdyid=djdy.bdcdyid")
					.append("    left join BDCK.bdcs_ql_xz@TO_ORCL6_BDCK ql on ql.djdyid=djdy.djdyid AND (QLLX=3 OR qllx=99 or qllx=23 or DJLX=600)")
					.append("    left join BDCK.bdcs_qlr_xz@TO_ORCL6_BDCK qlr on ql.qlid  = qlr.qlid")
					.append("    WHERE DY.BDCDYID is not null and ql.qlid is not null "+buildqllx+""+buildqlsdfs+""+buildqlxz+""+buildtddj+""+buildqlrlx+""+buildtdyt+"");
					//.append("    ORDER BY DY.BDCDYID asc");
			 buildyj.append(" select ")//余江
					.append("    DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,")
					.append("    DY.BDCDYLXMC AS BDCDYLXMC,QL.QLID,QL.BDCQZH,QL.QLLX AS QLLX,QL.DJLX,DY.BDCDYLX AS BDCDYLX,")
					.append("    DY.ZDMJ AS ZDMJ,QL.QDJG,DY.QLSDFS,DY.QLXZ,TDYT.TDDJ AS TDDJ,QLR.QLRLX as QLRLX,TDYT.TDYT AS TDYT,'余江' as qhmc");
		  buildyjcou.append(" from ( ")
					.append(" SELECT ")
					.append("      ZL,ZDDM,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC,ZDMJ,QLSDFS,QLXZ")
					.append("   FROM BDCK.BDCS_SYQZD_XZ@TO_ORCL7_BDCK")
					.append("   UNION ALL")
					.append("   SELECT ")
					.append("      ZL,ZDDM,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC ,ZDMJ,QLSDFS,QLXZ")
					.append("   FROM BDCK.BDCS_SHYQZD_XZ@TO_ORCL7_BDCK")
					.append("   ) DY")
					.append("    left join BDCK.bdcs_tdyt_xz@TO_ORCL7_BDCK tdyt on dy.bdcdyid=tdyt.bdcdyid")
					.append("    left join BDCK.BDCS_DJDY_XZ@TO_ORCL7_BDCK DJDY on dy.bdcdyid=djdy.bdcdyid")
					.append("    left join BDCK.bdcs_ql_xz@TO_ORCL7_BDCK ql on ql.djdyid=djdy.djdyid AND (QLLX=3 OR qllx=99 or qllx=23 or DJLX=600)")
					.append("    left join BDCK.bdcs_qlr_xz@TO_ORCL7_BDCK qlr on ql.qlid  = qlr.qlid")
					.append("    WHERE DY.BDCDYID is not null and ql.qlid is not null "+buildqllx+""+buildqlsdfs+""+buildqlxz+""+buildtddj+""+buildqlrlx+""+buildtdyt+"");
					//.append("    ORDER BY DY.BDCDYID asc");
			
						//地籍区
						  if(td_djqdm.length() >0 && td_djqdm !=null && td_djqdm != ""){
							   String[] value = td_djqdm.split(",");
							   for(int i =0; i<value.length;i++){
								if (value[i].length() == 9) {
									strdjqdm+="'"+value[i]+"',";
							}else{
								strdjzqdm+="'"+value[i]+"',";
							}
							  }
							if (strdjqdm.length()>0) {
								strdjqdm=strdjqdm.substring(0, strdjqdm.length()-1);
						}
							if (strdjzqdm.length()>0) {
								strdjzqdm=strdjzqdm.substring(0, strdjzqdm.length()-1);
						}
							if (strdjqdm!="") {
								builddjqdm.append("and bdcdy.DJQDM in("+strdjqdm+")");
						}
							if (strdjzqdm!="") {
								builddjzqdm.append("and bdcdy.DJZQDM in("+strdjzqdm+")");
						}
					}
		  
		  
			   build.append(" select")
					.append(" DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,DY.BDCDYLX AS BDCDYLX,")
					.append(" DY.BDCDYLXMC AS BDCDYLXMC,QL.QLID,QL.BDCQZH ");
		  buildcount.append(" from (")
					.append(" SELECT ")
					.append(" ZL,ZDDM,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC,ZDMJ,QLSDFS,QLXZ")
					.append(" FROM BDCK.BDCS_SYQZD_XZ")
					.append(" UNION ALL")
					.append(" SELECT")
					.append(" ZL,ZDDM,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC ,ZDMJ,QLSDFS,QLXZ")
					.append(" FROM BDCK.BDCS_SHYQZD_XZ) DY")
					.append(" left join BDCK.BDCS_tdyt_xz tdyt on dy.bdcdyid=tdyt.bdcdyid")
					.append(" left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid") 
					.append(" left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid AND (QLLX=3 OR qllx=99 or qllx=23 or DJLX=600)")
			   		.append(" left join BDCK.bdcs_qlr_xz qlr on ql.qlid  = qlr.qlid") 
			   		.append(" left join BDCK.BDCS_const const on "+fzdy+"  = const.constvalue") 
			   		.append(" left join BDCK.BDCS_CONSTCLS CONSTCLS on const.CONSTSLSID = CONSTCLS.CONSTSLSID") 
			   		.append(" WHERE const.CONSTTRANS='"+fzlb+"' and ql.qlid is not null "+buildqllx+""+buildqlsdfs+""+buildqlxz+""+buildtddj+""+buildqlrlx+""+buildtdyt);//+"   ORDER BY DY.BDCDYID asc");
					  
		  		     if(td_fzdy.equals("地籍区")){
						builddjqmc.append("and djqmc='"+fzlb+"'");
					  }else if (td_fzdy.equals("地籍子区")) {
						builddjzqmc.append("and djzqmc='"+fzlb+"'"); 
					  }else{
						builddjqmc.append("");
						builddjzqmc.append("");
					  }		
		  
			  builddjq.append("select")
					  .append("	DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,DY.BDCDYLX AS BDCDYLX,")
					  .append("	DY.BDCDYLXMC AS BDCDYLXMC,QL.QLID,QL.BDCQZH");
		 builddjqcount.append("  from (")
					  .append("	 SELECT ")
					  .append("	 ZL,ZDDM,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC,ZDMJ,QLSDFS,QLXZ,DJQMC,DJZQMC")
					  .append("	FROM BDCK.BDCS_SYQZD_XZ")
					  .append("		UNION ALL")
					  .append("	SELECT")
					  .append("	 ZL,ZDDM,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC ,ZDMJ,QLSDFS,QLXZ,DJQMC,DJZQMC")
					  .append(" FROM BDCK.BDCS_SHYQZD_XZ) DY")
					  .append("  left join BDCK.BDCS_tdyt_xz tdyt on dy.bdcdyid=tdyt.bdcdyid")
					  .append("  left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid ")
					  .append("  left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid AND (QLLX=3 OR qllx=99 or qllx=23 or DJLX=600)")
					  .append("  left join BDCK.bdcs_qlr_xz qlr on ql.qlid  = qlr.qlid")
					  .append("  WHERE DY.Bdcdyid is not null and ql.qlid is not null "+builddjqmc+" "+builddjzqmc);//+" ORDER BY DY.BDCDYID asc");
		  
		 			CommonDao dao = baseCommonDao;
					
					if (td_fzdy.equals("行政区划")) {
						if (fzlb.equals("鹰潭市")) {
							count = dao.getCountByFullSql(buildqscou.toString());
							listresult = dao.getPageDataByFullSql(buildqs.toString()+buildqscou.toString(), page, rows);
						}else if(fzlb.equals("市本级")){
							count = dao.getCountByFullSql(buildytcou.toString());
							listresult = dao.getPageDataByFullSql(buildyt.toString()+buildytcou.toString(), page, rows);
						}else if(fzlb.equals("贵溪")){
							count = dao.getCountByFullSql(buildgxcou.toString());
							listresult = dao.getPageDataByFullSql(buildgx.toString()+buildgxcou.toString(), page, rows);
						}else if(fzlb.equals("余江")){
							count = dao.getCountByFullSql(buildyjcou.toString());
							listresult = dao.getPageDataByFullSql(buildyj.toString()+buildyjcou.toString(), page, rows);
						}
					}else if(td_fzdy.equals("地籍区") || td_fzdy.equals("地籍子区")){
						count = dao.getCountByFullSql(builddjqcount.toString());
						listresult = dao.getPageDataByFullSql(builddjq.toString()+builddjqcount.toString(), page, rows);
					}else{
						count = dao.getCountByFullSql(buildcount.toString());
						listresult = dao.getPageDataByFullSql(build.toString()+buildcount.toString(), page, rows);
						
					}
					
					if (fzlb.equals("市本级")) {
					addRightsHolderInfo(listresult);
					addLimitZDStatus(listresult);
					//国有土地使用权时，显示出土地上房屋状态。
					//addLimitZDStausByFw(listresult);
					addDyCfDetails(listresult);
					addZRZCount(listresult);
					}else{
					String XZQH = "";
					if(fzlb.equals("贵溪")){
						XZQH = "2";
					}else if(fzlb.equals("余江")){
						XZQH = "3";
					}
					addRightsHolderInfoyt(listresult,XZQH);
					addLimitZDStatusyt(listresult,XZQH);
					//国有土地使用权时，显示出土地上房屋状态。
				//	addLimitZDStausByFwyt(listresult,XZQH);
					addDyCfDetailsyt(listresult,XZQH);
					addZRZCountyt(listresult,XZQH);
					}
					msg.setTotal(count);
					msg.setRows(listresult);
					
				}else{//---------------------------------------------------------------------分割线--------------------------------------------------
				String XZQH = xzqh != null ? xzqh:"";
				String fsqlcfwh=queryvalues.get("FSQL.CFWH");
				long count = 0;
				List<Map> listresult = null;
				/* ===============1、先获取实体对应的表名=================== */
				String unitentityName = "BDCK.BDCS_SHYQZD_XZ";

				/* ===============2、再获取表名+'_'+字段名=================== */
				String dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC ";
				String qlfieldsname = "QL.QLID,QL.BDCQZH";
				String fsqlfieldsname = "FSQL.FSQLID,FSQL.CFWH";

				if (tdzt != null && tdzt.equals("2")) {
					unitentityName = "BDCK.BDCS_SYQZD_XZ";
					dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,'01' AS BDCDYLX,'所有权宗地' AS BDCDYLXMC ";
				}

				// 集合使用权宗地与所有权宗地
				if (tdzt != null && tdzt.equals("3")) {
					unitentityName = "(SELECT ZL,ZDDM,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC FROM BDCK.BDCS_SYQZD_XZ UNION ALL SELECT ZL,ZDDM,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC FROM BDCK.BDCS_SHYQZD_XZ)";
					dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC";
				}

				/* ===============3、构造查询语句=================== */
				/* SELECT 字段部分 */
				StringBuilder builder2 = new StringBuilder();
				builder2.append("select ").append(dyfieldsname).append(",")
						.append(qlfieldsname);
				if(!StringHelper.isEmpty(fsqlcfwh)){
					builder2.append(",").append(fsqlfieldsname);
				}
				//全市监管
				if(XZQH.equals("2")){
					builder2.append(",").append("'贵溪' as qhmc ");
				}else if(XZQH.equals("3")){
					builder2.append(",").append("'余江' as qhmc ");
				}else{
					builder2.append(",").append("'市本级' as qhmc ");
				}
				String selectstr = builder2.toString();

				/* FROM 后边的表语句 */
				StringBuilder builder = new StringBuilder();
				builder.append(" from {0} DY")
						.append(" left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid")
						.append(" left join BDCK.BDCS_QL_XZ ql on ql.djdyid=djdy.djdyid  ");
				if(!StringHelper.isEmpty(fsqlcfwh)){
					builder.append(" left join BDCK.BDCS_FSQL_XZ fsql on ql.fsqlid=fsql.fsqlid  ");
				}
				String fromstr = builder.toString();
				fromstr = MessageFormat.format(fromstr, unitentityName);

				/* WHERE 条件语句 */
				StringBuilder builder3 = new StringBuilder();
				builder3.append(" where  ");
				StringBuilder qlrbuilder = new StringBuilder();
				boolean havecondition = false;
				boolean haveqlr = false;
				for (Entry<String, String> ent : queryvalues.entrySet()) {
					String name = ent.getKey();
					String value = ent.getValue();
					if (!StringHelper.isEmpty(value)) {
						// 抵押状态和查封状态，为‘0’的时候表示不过滤，查询全部
						if ((name.equals("CFZT") && value.equals("0"))
								|| (name.equals("DYZT") && value.equals("0"))) {
							continue;
						}

						// 权利人，权利人的条件比较特殊，where里边又套了where
						if (name.startsWith("QLR.")) {
							if (haveqlr)
								qlrbuilder.append(" and ");
							if (iflike) {
								qlrbuilder.append(" instr(" + name + ",'" + value
										+ "')>0 ");
							} else {
								if (name.equals("QLR.ZJH") && value.length() == 18) {
									String oldCard = ConverIdCard.getOldIDCard(value);
									if (oldCard.length() != 15) {
										qlrbuilder.append(" " + name + "='" + value
												+ "' ");
									} else {
										qlrbuilder.append(" (" + name + "='" + value
												+ "' or ");
										qlrbuilder.append(" " + name + "='" + oldCard
												+ "') ");
									}
								} else {
									qlrbuilder.append(" " + name + "='" + value + "' ");
								}
							}
							haveqlr = true;
							continue;
						}

						if (havecondition) {
							builder3.append(" and ");
						}

						// 抵押状态
						if (name.equals("DYZT")) {
							if (value.equals("1")) {
								builder3.append(" djdy.djdyid NOT IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_XZ DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
							} else {
								builder3.append(" djdy.djdyid IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_XZ DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
							}
							havecondition = true;
							continue;
						}
						// 查封状态
						if (name.equals("CFZT")) {
							if (value.equals("1")) {
								builder3.append("  djdy.DJDYID NOT IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_XZ CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99') ");
							} else {
								builder3.append("  djdy.DJDYID IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_XZ CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99') ");
							}
							havecondition = true;
							continue;
						}

						if (iflike) {
							builder3.append(" instr(" + name + ",'" + value + "')>0 ");
						} else {
							builder3.append(" " + name + "='" + value + "' ");
						}
						havecondition = true;
					}
				}
				// 有权利人查询条件
				if (!StringHelper.isEmpty(qlrbuilder.toString())) {
					if (havecondition) {
						builder3.append(" and ");
					}
					builder3.append(" QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ QLR WHERE "
							+ qlrbuilder.toString() + ")");
					havecondition = true;
				}

				if (tdzt == null || !tdzt.equals("2")) {
					if (havecondition) {
						// builder3.append(" and ");
					}
					// builder3.append(" ql.qllx='4'");
				}
				String wherestr = builder3.toString();
				if (wherestr.trim().toUpperCase().endsWith("WHERE")) {
					wherestr = "";
				}
				String fromSql = " from (select ql.qlid " + fromstr + wherestr + ")";
				String fullSql = selectstr + fromstr + wherestr;
//					/* 排序 条件语句 */
//					if(!StringUtils.isEmpty(sort)&&!StringUtils.isEmpty(order))
//					{
//					if(sort.toUpperCase().equals("ZL"))
//						sort="DY.ZL";
//					if(sort.toUpperCase().equals("BDCDYH"))
//						sort="DY.BDCDYH";
		//
//					if(sort.toUpperCase().equals("BDCQZH"))
//						sort="QL.BDCQZH";
		//
//					if(sort.toUpperCase().equals("BDCDYID"))
//						sort="DY.BDCDYID";
//					if(sort.toUpperCase().equals("BDCDYLXMC"))
//						sort="DY.BDCDYLX";
//						fullSql=fullSql+" ORDER BY "+sort+" "+order;
//					}
				/* 为了提高效率，先不查询符合条件的记录总数，将来改为异步获取 */
				CommonDao dao = baseCommonDao;
				String fromSqlgx = "";
				String fromSqlyj = "";
				String fullSqlgx = "";
				String fullSqlyj = "";
				long countgx = 0;
				long countyj = 0;
				if(XZQH.equals("2")){
				
						fromSql = getfromSQLGXZD(fromSql );
						fullSql = getfullSQLGXZD(fullSql );

				}else if(XZQH.equals("3")){
					fromSql = getfromSQLYJZD(fromSql);
					fullSql = getfullSQLYJZD(fullSql);

				}else if(XZQH.equals("0")){
					fromSqlgx = getfromSQLGXZD(fromSql);
					fromSqlyj = getfromSQLYJZD(fromSql);
					fullSqlgx = getfullSQLGXZD(fullSql);
					fullSqlgx = fullSqlgx.replaceAll("市本级", "贵溪");
					fullSqlyj = getfullSQLYJZD(fullSql);
					fullSqlyj = fullSqlyj.replaceAll("市本级", "贵溪");
					countgx = dao.getCountByFullSql(fromSqlgx);
					countyj = dao.getCountByFullSql(fromSqlyj);
					fullSql = fullSql +" union all "+ fullSqlgx +" union all "+ fullSqlyj;
				}
				count = dao.getCountByFullSql(fromSql)+countgx+countyj;;
				listresult = dao.getPageDataByFullSql(fullSql, page, rows);
				if(XZQH.equals("1")){
					addRightsHolderInfo(listresult);
					addLimitZDStatus(listresult);
					//国有土地使用权时，显示出土地上房屋状态。
//					addLimitZDStausByFw(listresult);
					addDyCfDetails(listresult);
					addZRZCount(listresult);
				}else{
					addRightsHolderInfoyt(listresult,XZQH);
					addLimitZDStatusyt(listresult,XZQH);
					//国有土地使用权时，显示出土地上房屋状态。
//					addLimitZDStausByFwyt(listresult,XZQH);
					addDyCfDetailsyt(listresult,XZQH);
					addZRZCountyt(listresult,XZQH);
				}
				
					msg.setTotal(count);
					msg.setRows(listresult);
				}
				return msg;
			}
	//鹰潭贵溪
	protected String getfromSQLGXZD(String fromSql ) {
	
			fromSql=fromSql.replaceAll("BDCK.BDCS_SHYQZD_XZ", "BDCK.BDCS_SHYQZD_XZ@TO_ORCL6_BDCK");
			fromSql=fromSql.replaceAll("BDCK.BDCS_SYQZD_XZ", "BDCK.BDCS_SYQZD_XZ@TO_ORCL6_BDCK ");
			fromSql=fromSql.replaceAll("BDCK.BDCS_DJDY_XZ", "BDCK.BDCS_DJDY_XZ@TO_ORCL6_BDCK");
			fromSql=fromSql.replaceAll("BDCK.BDCS_QL_XZ", "BDCK.BDCS_QL_XZ@TO_ORCL6_BDCK");
			fromSql=fromSql.replaceAll("BDCK.BDCS_FSQL_XZ", "BDCK.BDCS_FSQL_XZ@TO_ORCL6_BDCK");
			fromSql=fromSql.replaceAll("BDCK.BDCS_QLR_XZ", "BDCK.BDCS_QLR_XZ@TO_ORCL6_BDCK");
		
		return fromSql;
		
	}
	//鹰潭余江
	protected String getfromSQLYJZD(String fromSql) {
	
			fromSql=fromSql.replaceAll("BDCK.BDCS_SHYQZD_XZ", "BDCK.BDCS_SHYQZD_XZ@TO_ORCL7_BDCK");
			fromSql=fromSql.replaceAll("BDCK.BDCS_SYQZD_XZ@TO_ORCL7_BDCKY ", "BDCK.BDCS_SYQZD_XZ@TO_ORCL7_BDCK");
			fromSql=fromSql.replaceAll("BDCK.BDCS_DJDY_XZ", "BDCK.BDCS_DJDY_XZ@TO_ORCL7_BDCK");
			fromSql=fromSql.replaceAll("BDCK.BDCS_QL_XZ", "BDCK.BDCS_QL_XZ@TO_ORCL7_BDCK");
			fromSql=fromSql.replaceAll("BDCK.BDCS_FSQL_XZ", "BDCK.BDCS_FSQL_XZ@TO_ORCL7_BDCK");
			fromSql=fromSql.replaceAll("BDCK.BDCS_QLR_XZ", "BDCK.BDCS_QLR_XZ@TO_ORCL7_BDCK");
		
		return fromSql;
		
	}
	protected String getfullSQLGXZD(String fullSql) {
		
			fullSql=fullSql.replaceAll("BDCK.BDCS_SHYQZD_XZ", "BDCK.BDCS_SHYQZD_XZ@TO_ORCL6_BDCK");
			fullSql=fullSql.replaceAll("BDCK.BDCS_SYQZD_XZ", "BDCK.BDCS_SYQZD_XZ@TO_ORCL6_BDCK");
			fullSql=fullSql.replaceAll("BDCK.BDCS_QL_XZ", "BDCK.BDCS_QL_XZ@TO_ORCL6_BDCK");
			fullSql=fullSql.replaceAll("BDCK.BDCS_DJDY_XZ", "BDCK.BDCS_DJDY_XZ@TO_ORCL6_BDCK");
			fullSql=fullSql.replaceAll("BDCK.BDCS_QLR_XZ", "BDCK.BDCS_QLR_XZ@TO_ORCL6_BDCK");
	
		return fullSql;
		
	}
	protected String getfullSQLYJZD(String fullSql) {
		
			fullSql=fullSql.replaceAll("BDCK.BDCS_FSQL_XZ", "BDCK.BDCS_FSQL_XZ@TO_ORCL7_BDCK");
			fullSql=fullSql.replaceAll("BDCK.BDCS_SHYQZD_XZ", "BDCK.BDCS_SHYQZD_XZ@TO_ORCL7_BDCK");
			fullSql=fullSql.replaceAll("BDCK.BDCS_SYQZD_XZ@TO_ORCL7_BDCKY", "BDCK.BDCS_SYQZD_XZ@TO_ORCL7_BDCK");
			fullSql=fullSql.replaceAll("BDCK.BDCS_QL_XZ", "BDCK.BDCS_QL_XZ@TO_ORCL7_BDCK");
			fullSql=fullSql.replaceAll("BDCK.BDCS_DJDY_XZ", "BDCK.BDCS_DJDY_XZ@TO_ORCL7_BDCK");
			fullSql=fullSql.replaceAll("BDCK.BDCS_QLR_XZ", "BDCK.BDCS_QLR_XZ@TO_ORCL7_BDCK");
		
		return fullSql;
		
	}
	
	/**
	 * 根据宗地的登记单元ID区分出宗地下的商品房和私房类型
	 * 再根据该宗地下有无抵押房屋状态来给宗地状态赋值，全市监管
	 * @param result
	 */
	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	protected void addLimitZDStausByFwyt(List<Map> result,String xzqh){
		String tableql1 = "";
		String tableql2 = "";
		String tableh = "";
		String tablehy ="";
		String tablexm = "";
		if(xzqh.equals("2")){
			tableql1 = "BDCK.BDCS_QL_XZ@TO_ORCL6_BDCK ";
			tableql2 = "BDCK.BDCS_QL_GZ@TO_ORCL6_BDCK ";
			tablexm = "BDCK.BDCS_XMXX@TO_ORCL6_BDCK";
			tableh = "BDCK.BDCS_H_XZ@TO_ORCL6_BDCK";
			tablehy = "BDCK.BDCS_H_XZY@TO_ORCL6_BDCK";
		}else if(xzqh.equals("3")){
			tableql1 = "BDCK.BDCS_QL_XZ@TO_ORCL7_BDCK ";
			tableql2 = "BDCK.BDCS_QL_GZ@TO_ORCL7_BDCK ";
			tablexm = "BDCK.BDCS_XMXX@TO_ORCL7_BDCK";
			tableh = "BDCK.BDCS_H_XZ@TO_ORCL7_BDCK";
			tablehy = "BDCK.BDCS_H_XZY@TO_ORCL7_BDCK";
		}else {
			tableql1 = "BDCK.BDCS_QL_XZ ";
			tableql2 = "BDCK.BDCS_QL_GZ ";
			tablexm = "BDCK.BDCS_XMXX";
			tableh = "BDCK.BDCS_H_XZ";
			tablehy = "BDCK.BDCS_H_XZY";
		}
		if(result != null && result.size() > 0){
			for(Map map : result){
				long mortgageCount = 0,mortgagingCount=0;
				long SealCount = 0,SealingCount=0;
				long ObjectionCount = 0,ObjectioningCount=0;
				long housecount = 0;
				//只有使用权宗地才有房屋
				if(map.containsKey("BDCDYID")&&map.get("BDCDYLX").equals("02")){
					String zdbdcdyid = (String) map.get("BDCDYID");
					if(zdbdcdyid != null ){
							String fullSql ="";
						if(xzqh.equals("0")){
							fullSql = MessageFormat.format("select BDCDYH,zdbdcdyid from (select BDCDYH,zdbdcdyid from "+tableh+"  where zdbdcdyid=''{0}'' union all select BDCDYH,zdbdcdyid from "+tablehy+" where zdbdcdyid=''{0}''  "
									+ "union all select BDCDYH,zdbdcdyid from BDCK.BDCS_H_XZ@TO_ORCL6_BDCK where zdbdcdyid=''{0}'' union all select BDCDYH,zdbdcdyid from BDCK.BDCS_H_XZY@TO_ORCL6_BDCK where zdbdcdyid=''{0}''"
									+ "union all select BDCDYH,zdbdcdyid from BDCK.BDCS_H_XZ@TO_ORCL7_BDCK  where zdbdcdyid=''{0}'' union all select BDCDYH,zdbdcdyid from BDCK.BDCS_H_XZY@TO_ORCL7_BDCK where zdbdcdyid=''{0}'' )"
									+ " where zdbdcdyid=''{0}''", zdbdcdyid);
						}else{
							 fullSql = MessageFormat.format("select BDCDYH from "+tableh+" where zdbdcdyid=''{0}'' union all select BDCDYH from "+tablehy+" where zdbdcdyid=''{0}''", zdbdcdyid);
						}
						
						List<Map> bdcdyhs = baseCommonDao.getDataListByFullSql(fullSql);
						housecount=bdcdyhs.size();
						//商品房的土地抵消状态赋值
						if(bdcdyhs != null && bdcdyhs.size() > 0){
							for(Map bdcdyh :bdcdyhs){
								if(bdcdyh.containsKey("BDCDYH")){
									String _bdcdyh = (String) bdcdyh.get("BDCDYH");
									if(_bdcdyh != null ){
										String sqlMortgage = "";
										String sqlSeal = "";
										String sqlObjection ="";
										String sqlMortgaging ="";
										String sqlSealing ="";
										//已办理SQL语句
										if(xzqh.equals("0")){
											 sqlMortgage = MessageFormat.format(" from(select bdcdyh , qllx from "+tableql1+" union all select bdcdyh , qllx from BDCK.BDCS_QL_XZ@TO_ORCL6_BDCK union all select bdcdyh , qllx from BDCK.BDCS_QL_XZ@TO_ORCL7_BDCK) "
											 		+ "where bdcdyh=''{0}'' and qllx=''23''", _bdcdyh);
											 sqlSeal = MessageFormat.format(" from(select bdcdyh , qllx,djlx from "+tableql1+" union all select bdcdyh , qllx,djlx from BDCK.BDCS_QL_XZ@TO_ORCL6_BDCK union all select bdcdyh , qllx,djlx from BDCK.BDCS_QL_XZ@TO_ORCL7_BDCK)"
											 		+ " where bdcdyh=''{0}'' and djlx=''800'' and qllx=''99''", _bdcdyh);
											 sqlObjection = MessageFormat.format("  from(select bdcdyh ,djlx from "+tableql1+" union all select bdcdyh ,djlx from BDCK.BDCS_QL_XZ@TO_ORCL6_BDCK union all select bdcdyh ,djlx from BDCK.BDCS_QL_XZ@TO_ORCL7_BDCK)"
											 		+ " where bdcdyh=''{0}'' and djlx=''600'' ", _bdcdyh);
											//正在办理的SQL语句
											 sqlMortgaging = MessageFormat.format("from(select c.bdcdyh,a.sfdb,c.djlx,c.qllx FROM "+tablexm+" a  LEFT JOIN "+tableql2+" c ON a.xmbh=c.xmbh and  a.sfdb=''0'' "
											 		+ "union all select c.bdcdyh,a.sfdb,c.djlx,c.qllx FROM BDCK.BDCS_XMXX@TO_ORCL6_BDCK a  LEFT JOIN BDCK.BDCS_QL_GZ@TO_ORCL6_BDCK c ON a.xmbh=c.xmbh and  a.sfdb=''0'' "
											 		+ "union all select c.bdcdyh,a.sfdb,c.djlx,c.qllx FROM BDCK.BDCS_XMXX@TO_ORCL6_BDCK a  LEFT JOIN BDCK.BDCS_QL_GZ@TO_ORCL6_BDCK c ON a.xmbh=c.xmbh and  a.sfdb=''0'')"
											 		+ "WHERE djlx=''100'' AND qllx=''23'' and bdcdyh=''{0}'' and sfdb=''0'' ", _bdcdyh);
											 sqlSealing = MessageFormat.format(" from (select c.bdcdyh,a.sfdb,c.djlx,c.qllx FROM "+tablexm+" a  LEFT JOIN "+tableql2+" c ON a.xmbh=c.xmbh and a.sfdb=''0'' "
											 		+ "union all select c.bdcdyh,a.sfdb,c.djlx,c.qllx FROM BDCK.BDCS_XMXX@TO_ORCL6_BDCK a  LEFT JOIN BDCK.BDCS_QL_GZ@TO_ORCL6_BDCK c ON a.xmbh=c.xmbh and  a.sfdb=''0'' "
											 		+ " union all select c.bdcdyh,a.sfdb,c.djlx,c.qllx FROM BDCK.BDCS_XMXX@TO_ORCL6_BDCK a  LEFT JOIN BDCK.BDCS_QL_GZ@TO_ORCL6_BDCK c ON a.xmbh=c.xmbh and  a.sfdb=''0'' )"
											 		+ "WHERE djlx=''800'' AND qllx=''99'' and bdcdyh=''{0}'' and sfdb=''0'' ", _bdcdyh);
										}else{
											 sqlMortgage = MessageFormat.format(" from "+tableql1+" where bdcdyh=''{0}'' and qllx=''23''", _bdcdyh);
											 sqlSeal = MessageFormat.format(" from "+tableql1+" where bdcdyh=''{0}'' and djlx=''800'' and qllx=''99''", _bdcdyh);
											 sqlObjection = MessageFormat.format("  from "+tableql1+" where bdcdyh=''{0}'' and djlx=''600'' ", _bdcdyh);
											//正在办理的SQL语句
											 sqlMortgaging = MessageFormat.format("FROM "+tablexm+" a  LEFT JOIN "+tableql2+" c ON a.xmbh=c.xmbh and  a.sfdb=''0'' WHERE c.djlx=''100'' AND c.qllx=''23'' and c.bdcdyh=''{0}'' and a.sfdb=''0'' ", _bdcdyh);
											 sqlSealing = MessageFormat.format(" FROM "+tablexm+" a  LEFT JOIN "+tableql2+" c ON a.xmbh=c.xmbh and a.sfdb=''0'' WHERE c.djlx=''800'' AND c.qllx=''99'' and c.bdcdyh=''{0}'' and a.sfdb=''0'' ", _bdcdyh);
										}

										long mcount = baseCommonDao.getCountByFullSql(sqlMortgage);
										if(mcount >0)
										   mortgageCount += 1;
										else
										{
											if(baseCommonDao.getCountByFullSql(sqlMortgaging)>0)
												mortgagingCount+=1;
										}
										long scount = baseCommonDao.getCountByFullSql(sqlSeal);
										if(scount >0)
										   SealCount += 1;
										else
										{
											if(baseCommonDao.getCountByFullSql(sqlSealing)>0)
												SealingCount+=1;
										}
										ObjectionCount= baseCommonDao.getCountByFullSql(sqlObjection);
										if(ObjectionCount >0)
											ObjectionCount += 1;

									}
								}
							}
							String mortgageStatus =  MessageFormat.format("土地{0};地上房屋已抵押{1}起,正在抵押{2}起",map.get("DYZT"),mortgageCount,mortgagingCount);
							String sealStatus = MessageFormat.format("土地{0};地上房屋已查封{1}起,正在查封{2}起",map.get("CFZT"),SealCount,SealingCount);
							String objectionStatus = MessageFormat.format("土地{0};地上房屋有异议{1}起",map.get("YYZT"),ObjectionCount);
							map.put("DYZT", mortgageStatus);
							map.put("CFZT", sealStatus);
							map.put("YYZT", objectionStatus);
							
						}
	
					}
				}
				map.put("HOUSECOUNT",housecount);
			}
		}
	}
	
	/**
	 * 土地的状态赋值
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void addLimitZDStatusyt(List<Map> result,String xzqh) {
		String tableql1 = "";
		String tableql2 = "";
		String tabledj = "";
		String tablexm = "";
		if(xzqh.equals("2")){
			tableql1 = "BDCK.BDCS_QL_XZ@TO_ORCL6_BDCK ";
			tableql2 = "BDCK.BDCS_QL_GZ@TO_ORCL6_BDCK ";
			tablexm = "BDCK.BDCS_XMXX@TO_ORCL6_BDCK";
			tabledj = "BDCK.BDCS_DJDY_GZ@TO_ORCL6_BDCK";
		}else if(xzqh.equals("3")){
			tableql1 = "BDCK.BDCS_QL_XZ@TO_ORCL7_BDCK ";
			tableql2 = "BDCK.BDCS_QL_GZ@TO_ORCL7_BDCK ";
			tablexm = "BDCK.BDCS_XMXX@TO_ORCL7_BDCK";
			tabledj = "BDCK.BDCS_DJDY_GZ@TO_ORCL7_BDCK";
		}else {
			tableql1 = "BDCK.BDCS_QL_XZ ";
			tableql2 = "BDCK.BDCS_QL_GZ ";
			tablexm = "BDCK.BDCS_XMXX";
			tabledj = "BDCK.BDCS_DJDY_GZ";
		}
		if (result != null && result.size() > 0) {
			for (Map map : result) {
				if (map.containsKey("DJDYID")) {
					String djdyid = (String) map.get("DJDYID");
					if (djdyid != null) {
						String sqlMortgage ="";
						String sqlSeal = "";
						String sqlObjection = "";
						if(xzqh.equals("0")){
							sqlMortgage = MessageFormat
									.format(" from(select djdyid,qllx from "+tableql1+" "
											+ " union all select djdyid,qllx from BDCK.BDCS_QL_XZ@TO_ORCL6_BDCK "
											+ " union all select djdyid,qllx from BDCK.BDCS_QL_XZ@TO_ORCL7_BDCK )"
											+ "where djdyid=''{0}'' and qllx=''23''",
											djdyid);
							sqlSeal = MessageFormat
									.format(" from(select djdyid,qllx,djlx from "+tableql1+" "
											+ " union all select djdyid,qllx,djlx from BDCK.BDCS_QL_XZ@TO_ORCL6_BDCK "
											+ " union all select djdyid,qllx,djlx from BDCK.BDCS_QL_XZ@TO_ORCL7_BDCK )"
											+ "where djdyid=''{0}'' and djlx=''800'' and qllx=''99''",
											djdyid);
							sqlObjection = MessageFormat
									.format(" from(select djdyid,qllx,djlx from "+tableql1+" "
											+ " union all select djdyid,qllx,djlx from BDCK.BDCS_QL_XZ@TO_ORCL6_BDCK "
											+ " union all select djdyid,qllx,djlx from BDCK.BDCS_QL_XZ@TO_ORCL7_BDCK )"
											+ "where djdyid=''{0}'' and djlx=''600''",
											djdyid);
						}else{
							 sqlMortgage = MessageFormat
									.format(" from "+tableql1+" where djdyid=''{0}'' and qllx=''23''",
											djdyid);
							 sqlSeal = MessageFormat
									.format(" from "+tableql1+" where djdyid=''{0}'' and djlx=''800'' and qllx=''99''",
											djdyid);
							 sqlObjection = MessageFormat
									.format("  from "+tableql1+" where djdyid=''{0}'' and djlx=''600'' ",
											djdyid);
						}
						

						long mortgageCount = baseCommonDao
								.getCountByFullSql(sqlMortgage);
						long SealCount = baseCommonDao
								.getCountByFullSql(sqlSeal);
						long ObjectionCount = baseCommonDao
								.getCountByFullSql(sqlObjection);

						String mortgageStatus = mortgageCount > 0 ? "已抵押"
								: "无抵押";
						String sealStatus = SealCount > 0 ? "已查封" : "无查封";

						// 判断完现状层中的查封信息，接着判断办理中的查封信息
						if (!(SealCount > 0)) {
							String sqlSealing = "";
							if(xzqh.equals("0")){
								sqlSealing = " from(select a.sfdb,c.djdyid,c.qllx,c.djlx FROM "+tablexm+" a LEFT JOIN "+tabledj+" b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN "+tableql2+" c ON b.xmbh=c.xmbh and  b.djdyid=c.djdyid  "
										+ "union all select a.sfdb,c.djdyid,c.qllx,c.djlx FROM BDCK.BDCS_XMXX@TO_ORCL6_BDCK a LEFT JOIN BDCK.BDCS_DJDY_GZ@TO_ORCL6_BDCK b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN BDCK.BDCS_QL_GZ@TO_ORCL6_BDCK c ON b.xmbh=c.xmbh and  b.djdyid=c.djdyid  "
										+ "union all select a.sfdb,c.djdyid,c.qllx,c.djlx FROM BDCK.BDCS_XMXX@TO_ORCL7_BDCK a LEFT JOIN BDCK.BDCS_DJDY_GZ@TO_ORCL7_BDCK b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN BDCK.BDCS_QL_GZ@TO_ORCL7_BDCK c ON b.xmbh=c.xmbh and  b.djdyid=c.djdyid )WHERE djlx='800' AND qllx='99' and djdyid='"
										+ djdyid + "' and sfdb='0'";
							}else{
								 sqlSealing = " FROM "+tablexm+" a LEFT JOIN "+tabledj+" b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN "+tableql2+" c ON b.xmbh=c.xmbh and  b.djdyid=c.djdyid WHERE c.djlx='800' AND c.qllx='99' and c.djdyid='"
										+ djdyid + "' and a.sfdb='0' ";
							}
							
							long count = baseCommonDao
									.getCountByFullSql(sqlSealing);
							sealStatus = count > 0 ? "查封办理中" : "无查封";
						}

						// 判断完现状层中的查封信息，接着判断办理中的查封信息
						if (!(mortgageCount > 0)) {
							String sqlmortgageing ="";
							if(xzqh.equals("0")){
								sqlmortgageing = " from(select a.sfdb,c.djdyid,c.qllx,c.djlx FROM "+tablexm+" a LEFT JOIN "+tabledj+" b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN "+tableql2+" c ON b.xmbh=c.xmbh and  b.djdyid=c.djdyid  "
										+ "union all select a.sfdb,c.djdyid,c.qllx,c.djlx FROM BDCK.BDCS_XMXX@TO_ORCL6_BDCK a LEFT JOIN BDCK.BDCS_DJDY_GZ@TO_ORCL6_BDCK b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN BDCK.BDCS_QL_GZ@TO_ORCL6_BDCK c ON b.xmbh=c.xmbh and  b.djdyid=c.djdyid  "
										+ "union all select a.sfdb,c.djdyid,c.qllx,c.djlx FROM BDCK.BDCS_XMXX@TO_ORCL7_BDCK a LEFT JOIN BDCK.BDCS_DJDY_GZ@TO_ORCL7_BDCK b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN BDCK.BDCS_QL_GZ@TO_ORCL7_BDCK c ON b.xmbh=c.xmbh and  b.djdyid=c.djdyid )WHERE djlx='100' AND qllx='23' and djdyid='"+ djdyid + "' and sfdb='0'";
							}else{
								sqlmortgageing = " FROM "+tablexm+" a LEFT JOIN "+tabledj+" b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN "+tableql2+" c ON b.xmbh=c.xmbh and  b.djdyid=c.djdyid WHERE c.djlx='100' AND c.qllx='23' and c.djdyid='"+ djdyid + "' and a.sfdb='0' ";
							}
							
							long count = baseCommonDao
									.getCountByFullSql(sqlmortgageing);
							mortgageStatus = count > 0 ? "抵押办理中" : "无抵押";
						}

						String objectionStatus = ObjectionCount > 0 ? "有异议"
								: "无异议";
						map.put("DYZT", mortgageStatus);
						map.put("CFZT", sealStatus);
						map.put("YYZT", objectionStatus);
					}
				}
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void addZRZCountyt(List<Map> result,String xzqh){
		String table = "";
		String table1 = "";
		if(xzqh.equals("2")){
			table = "BDCK.BDCS_ZRZ_XZ@TO_ORCL6_BDCK ";
			table1 = "BDCK.BDCS_ZRZ_XZY@TO_ORCL6_BDCK ";
		}else if(xzqh.equals("3")){
			table = "BDCK.BDCS_ZRZ_XZ@TO_ORCL7_BDCK ";
			table1 = "BDCK.BDCS_ZRZ_XZY@TO_ORCL7_BDCK ";
		}else {
			table = "BDCK.BDCS_ZRZ_XZ ";
			table1 = "BDCK.BDCS_ZRZ_XZY";
		}
		String zrzcount = "0";
		if(result != null && result.size() > 0){
			for(Map map : result){
				if(map.containsKey("BDCDYID")){
						String zdbdcdyid = (String) map.get("BDCDYID");
						if(zdbdcdyid != null ){
							String countsql ="";
							if(xzqh.equals("0")){
								 countsql = MessageFormat.format(" SELECT SUM(C) AS C FROM (SELECT COUNT(0) AS C FROM "+table+" WHERE ZDBDCDYID=''{0}'' UNION ALL SELECT COUNT(0) AS C FROM "+table1+" "
								 		+ " union all SELECT COUNT(0) AS C FROM BDCK.BDCS_ZRZ_XZ@TO_ORCL6_BDCK WHERE ZDBDCDYID=''{0}'' UNION ALL SELECT COUNT(0) AS C FROM BDCK.BDCS_ZRZ_XZY@TO_ORCL6_BDCK "
								 		+ " union all SELECT COUNT(0) AS C FROM BDCK.BDCS_ZRZ_XZ@TO_ORCL7_BDCK WHERE ZDBDCDYID=''{0}'' UNION ALL SELECT COUNT(0) AS C FROM BDCK.BDCS_ZRZ_XZY@TO_ORCL7_BDCK WHERE ZDBDCDYID=''{0}'')", zdbdcdyid);
							}else{
								 countsql = MessageFormat.format(" SELECT SUM(C) AS C FROM (SELECT COUNT(0) AS C FROM "+table+" WHERE ZDBDCDYID=''{0}'' UNION ALL SELECT COUNT(0) AS C FROM "+table1+" WHERE ZDBDCDYID=''{0}'')", zdbdcdyid);

							}
							List<Map> counts = baseCommonDao.getDataListByFullSql(countsql);
							zrzcount=String.valueOf(counts.get(0).get("C"));
			            }
				}
				map.put("ZRZCOUNT",zrzcount);
		
	        }
        }
	
    }

	@Override
	public Map<String, Object> LSQLInfoEX(String qlid) {
		Map<String, Object> listmap = new HashMap<String, Object>();
		Rights ql = RightsTools.loadRights(DJDYLY.LS, qlid);
		listmap.put("CurrentUserName", Global.getCurrentUserName());
		listmap.put("syq", ql);
		listmap.put("syqfsql", RightsTools.loadSubRightsByRightsID(DJDYLY.LS, qlid));
		listmap.put("syqrs", baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.BDCS_QLR_LS WHERE QLID='"+qlid+"'"));
		if(ql!=null){
			List<BDCS_DJDY_LS> dy = baseCommonDao.getDataList(BDCS_DJDY_LS.class, "DJDYID='"+ql.getDJDYID()+"'");
			BDCS_DJDY_LS dyh = null;
			RealUnit house = null;
			String lx = "";
			if(dy!=null&&dy.size()>0){
				dyh=dy.get(0);
				lx = dyh.getBDCDYLX();
				house = UnitTools.loadUnit(BDCDYLX.initFrom(lx), DJDYLY.LS, dyh.getBDCDYID());
				List<Map> tdxx = GetHInfo(dyh.getBDCDYID(), lx);
				if (tdxx.size() > 0 && tdxx != null) {
					listmap.put("tdxx", tdxx);
				}
			}
			listmap.put("house", house);
			listmap.put("qlzt", "该权利已注销");
		}
		return listmap;
	}

	@Override
	public Message blacklist(MultipartFile file, HttpServletRequest request) {
		List<Map<String,String>> blacklist = new ArrayList<Map<String,String>>();
		Long total = 0L;
		if (!file.isEmpty()) {
			String fileName = file.getOriginalFilename();
			String path = request.getSession().getServletContext()
					.getRealPath("/")
					+ "resources/upload/" + fileName;
			File isfile = new File(path);
			if (!isfile.exists()) {
				isfile.mkdirs();
			}
			try {
				file.transferTo(new File(path));
				FileInputStream input = new FileInputStream(path);
				Workbook workbook = null;
				if (fileName.toLowerCase().endsWith("xlsx")) {
					workbook = new XSSFWorkbook(input);
				} else if (fileName.toLowerCase().endsWith("xls")) {
					workbook = new HSSFWorkbook(input);
				}
				Sheet sheet = workbook.getSheetAt(0);
				Row head = sheet.getRow(2);
				Row tmp1 = sheet.getRow(0);
				Row tmp2 = sheet.getRow(1);
				sheet.removeRow(head);
				sheet.removeRow(tmp1);
				sheet.removeRow(tmp2);
				if(("被执行人").equals(StringHelper.isEmpty(head.getCell(2))
						?"":head.getCell(2).getStringCellValue().trim())){
					for (Row row : sheet) {
						String qlrmc = StringHelper.isEmpty(row.getCell(2))
								?"":row.getCell(2).getStringCellValue().trim();
						if(!StringHelper.isEmpty(qlrmc)){
							HashMap<String, String> r = new HashMap<String, String>();
							HashMap<String, String> details = new HashMap<String, String>();
							for (int i = 1; i <= head.getLastCellNum(); i++) {
								if(i==2||i==3||i==16){
									continue;
								}
								if(!StringHelper.isEmpty(head.getCell(i))){
									details.put(head.getCell(i).getStringCellValue(), 
											StringHelper.formatObject(row.getCell(i)));
								}
							}
							r.put("qlrmc", qlrmc);
							r.put("zjh", "");
							r.put("yxbz", StringHelper.isEmpty(row.getCell(16))?"":row.getCell(16).getStringCellValue().trim());
							r.put("qzh", StringHelper.isEmpty(row.getCell(3))?"":row.getCell(3).getStringCellValue().trim());
							r.put("detail", JSONObject.toJSONString(details));
							blacklist.add(r);
						}else{
							break;
						}
					}
				}
				input.close();
				(new File(path)).delete();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//去重
			HashSet<Map<String,String>> hs = new HashSet<Map<String,String>>(blacklist);
			blacklist.clear();
			blacklist = new ArrayList<Map<String,String>>(hs);
			Session session = baseCommonDao.OpenSession();
			Transaction tx = session.beginTransaction();
			for (Map<String,String> qlr : blacklist) {
				BDCS_BLACKLIST ex = new BDCS_BLACKLIST();
				String qlrmc = StringHelper.formatObject(qlr.get("qlrmc"));
				String detail = StringHelper.formatObject(qlr.get("detail"));
				String qzh = StringHelper.formatObject(qlr.get("qzh"));
				String yxbz = StringHelper.formatObject(qlr.get("yxbz"));
				ex.setQLRMC(qlrmc);
				ex.setDETAIL(detail);
				ex.setQZH(qzh);
				ex.setYXBZ(yxbz);
				session.save(ex);
			}
			session.flush();
			tx.commit();
			session.close();
		}
		Message msg = new Message();
		return msg;
	}

	@Override
	public Message Getblacklist(HttpServletRequest request) throws UnsupportedEncodingException {
		String qlrmc = RequestHelper.getParam(request, "qlrmc");
		String zjh = RequestHelper.getParam(request, "zjh");
		String qzh = RequestHelper.getParam(request, "qzh");
		int page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		int rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Message msg = new Message();
		StringBuilder fulSql = new StringBuilder("SELECT * FROM BDCK.BDCS_BLACKLIST WHERE 1>0");
		List<Map> Alllist = baseCommonDao.getDataListByFullSql(fulSql.toString());
		if(!StringHelper.isEmpty(zjh)){
			fulSql.append(" AND ZJH like '%").append(zjh).append("%' ");
		}
		if(!StringHelper.isEmpty(qlrmc)){
			fulSql.append(" AND QLRMC like '%").append(qlrmc).append("%' ");
		}
		if(!StringHelper.isEmpty(qzh)){
			fulSql.append(" AND QZH like '%").append(qzh).append("%' ");
		}
		List<Map> list = baseCommonDao.getPageDataByFullSql(fulSql.toString(), page, rows);
		msg.setRows(list);
		msg.setTotal(Alllist.size());
		return msg;
	}

	@Override
	public Message Delblacklist(HttpServletRequest request) {
		Message msg = new Message();
		try {
			String ids = RequestHelper.getParam(request, "ids");
			if(!StringHelper.isEmpty(ids)){
				String[] idArr = ids.split(",");
				for (String id : idArr) {
					baseCommonDao.delete(BDCS_BLACKLIST.class, id);
				}
				baseCommonDao.flush();
			}
		} catch (UnsupportedEncodingException e) {
			msg.setMsg("删除失败");
			msg.setSuccess("false");
			e.printStackTrace();
		}
		
		msg.setMsg("删除成功");
		msg.setSuccess("true");
		return msg;
	}

	@Override
	public Message Addblacklist(HttpServletRequest request) {
		BDCS_BLACKLIST ex = new BDCS_BLACKLIST();
		Message msg = new Message();
		try {
			String qlrmc = RequestHelper.getParam(request, "qlrmc");
			String zjh = RequestHelper.getParam(request, "zjh");
			String qzh = RequestHelper.getParam(request, "qzh");
			String yxbz = RequestHelper.getParam(request, "yxbz");
			String detail = RequestHelper.getParam(request, "details");
			ex.setQLRMC(qlrmc);
			ex.setDETAIL(detail);
			ex.setQZH(qzh);
			ex.setZJH(zjh);
			ex.setYXBZ(yxbz);
			baseCommonDao.save(ex);
			baseCommonDao.flush();
		} catch (UnsupportedEncodingException e) {
			msg.setMsg("添加失败");
			msg.setSuccess("false");
			e.printStackTrace();
		}
		
		msg.setMsg("添加成功");
		msg.setSuccess("true");
		return msg;
	}

	@Override
	public Message Updateblacklist(HttpServletRequest request) {
		Message msg = new Message();
		try {
			String id = RequestHelper.getParam(request, "id");
			String qlrmc = RequestHelper.getParam(request, "qlrmc");
			String zjh = RequestHelper.getParam(request, "zjh");
			String qzh = RequestHelper.getParam(request, "qzh");
			String detail = RequestHelper.getParam(request, "details");
			String yxbz = RequestHelper.getParam(request, "yxbz");
			BDCS_BLACKLIST balck = baseCommonDao.get(BDCS_BLACKLIST.class, id);
			if(balck!=null){
				balck.setQLRMC(qlrmc);
				balck.setDETAIL(detail);
				balck.setQZH(qzh);
				balck.setZJH(zjh);
				balck.setYXBZ(yxbz);
				baseCommonDao.update(balck);
				baseCommonDao.flush();
			}
		} catch (UnsupportedEncodingException e) {
			msg.setMsg("更新失败");
			msg.setSuccess("false");
			e.printStackTrace();
		}
		msg.setMsg("更新成功");
		msg.setSuccess("true");
		return msg;
	}

	@Override
	public Message Duplicate(HttpServletRequest request) {
		Message msg = new Message();
		try {
			String xm = RequestHelper.getParam(request, "sqrmc");
			String zjh = RequestHelper.getParam(request, "zjh");
			StringBuilder hqlCondition = new StringBuilder(" 1>0 and yxbz='有效' ");
			if(!StringHelper.isEmpty(xm)){
				hqlCondition.append(" and QLRMC LIKE '%").append(xm).append("%' ");
			}
//			if(!StringHelper.isEmpty(zjh)){
//				hqlCondition.append(" and ZJH LIKE '%").append(zjh).append("%' ");
//			}
			
			List<BDCS_BLACKLIST> list = baseCommonDao.getDataList(BDCS_BLACKLIST.class, hqlCondition.toString());
			if(list!=null&&list.size()>0){
				msg.setSuccess("false");
				msg.setRows(list);
			}else{
				msg.setSuccess("true");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public Message queryHouseByZRZBDCDYH(Map<String, String> conditionmap, int page, int rows, boolean iflike,String sort,String order) {
		Message m = new Message();
		long count = 0;
		StringBuilder str = new StringBuilder();
		str.append("SELECT ROWNUM,a.bdcdyh,a.zl,b.bdcqzh,a.scjzmj,a.fh,a.ghyt,a.yyzt from bdck.bdcs_djdy_xz c left join bdck.bdcs_h_xz a on c.bdcdyid=a.bdcdyid left join (SELECT * FROM (SELECT row_number() over(PARTITION BY ql.djdyid,bdcdyh ORDER BY ql.qlid DESC)rn,ql.bdcqzh,ql.djdyid FROM bdck.bdcs_ql_xz ql )WHERE rn=1) b on c.djdyid=b.djdyid where a.bdcdyid is not null");
		String fwbm = conditionmap.get("DY.FWBM");
		String bdcdyh = conditionmap.get("DY.BDCDYH");
		String zl = conditionmap.get("DY.ZL");
		String fh = conditionmap.get("DY.FH");
		if(!StringHelper.isEmpty(fwbm)){
			str.append(" and a.fwbm='").append(fwbm).append("'");
		}
		if(!StringHelper.isEmpty(bdcdyh)){
			if(iflike)
				str.append(" and a.bdcdyh like'%").append(bdcdyh).append("%'");
			else
				str.append(" and a.bdcdyh='").append(bdcdyh).append("'");
		}
		if(!StringHelper.isEmpty(zl)){
			if(iflike)
				str.append(" and a.zl like '%").append(zl).append("%'");
			else
				str.append(" and a.zl='").append(zl).append("'");
		}
		if(!StringHelper.isEmpty(fh)){
			if(iflike)
				str.append(" and a.fh like '%").append(fh).append("%'");
			else
				str.append(" and a.fh='").append(fh).append("'");
		}
		if(!StringUtils.isEmpty(sort)&&!StringUtils.isEmpty(order))
		{
		if(sort.toUpperCase().equals("ZL"))
			sort="a.zl";
		if(sort.toUpperCase().equals("FH"))
			sort="a.fh";
		if(sort.toUpperCase().equals("BDCDYH"))
			sort="a.bdcdyh";
		if(sort.toUpperCase().equals("SCJZMJ"))
			sort="a.scjzmj";		
			str.append(" ORDER BY ").append(sort).append(" ").append(order);
		}
		String fromsql = " from ("+str.toString()+")";
		count = baseCommonDao.getCountByFullSql(fromsql.toString());
		List<Map> list = baseCommonDao.getPageDataByFullSql(str.toString(), page, rows);
		if(list!=null&&list.size()>0){
			for (Map<String, String> map : list) {
				if (map.containsKey("GHYT")) {
					String value = StringHelper.formatObject(StringHelper
							.isEmpty(map.get("GHYT")) ? "" : map.get("GHYT"));
					String name = "";
					if (!StringHelper.isEmpty(value)) {
						name = ConstHelper.getNameByValue("FWYT", value);
					}
					map.put("GHYT", name);
				}
			}
		}
		m.setTotal(count);
		m.setRows(list);
		return m;
	}

	/* 
	 * 指认图查询模块 （并与H表中ZRZBDCDYID做对比）
	 */
	@Override
	public Message queryHouseByTime(Map<String, String> conditionmap, int page, int rows, boolean iflike, String sort) {
		Message m = new Message();
		long count = 0;
		StringBuilder str = new StringBuilder();
		str.append("SELECT * FROM BDCK.BDCS_GEO geo WHERE geo.id is not null");
		String fwbm = conditionmap.get("DY.FWBM");
		String bdcdyh = conditionmap.get("DY.BDCDYH");
		String zl = conditionmap.get("DY.ZL");
		String ddsj_q = conditionmap.get("DY.DDSJ_Q");
		String ddsj_z = conditionmap.get("DY.DDSJ_Z");
		if(!StringHelper.isEmpty(fwbm)){
			if(iflike)
				str.append(" and geo.fwbm like'%").append(fwbm).append("%'");
			else
				str.append(" and geo.fwbm='").append(fwbm).append("'");
		}
		if(!StringHelper.isEmpty(bdcdyh)){
			if(iflike)
				str.append(" and geo.bdcdyh like'%").append(bdcdyh).append("%'");
			else
				str.append(" and geo.bdcdyh='").append(bdcdyh).append("'");
		}
		if(!StringHelper.isEmpty(zl)){
			if(iflike)
				str.append(" and geo.zl like '%").append(zl).append("%'");
			else
				str.append(" and geo.zl='").append(zl).append("'");
		}
		if(!StringHelper.isEmpty(ddsj_q)){
			str.append(" and geo.time >=to_date('"+ddsj_q+"','yyyy-mm-dd') ");
		}
		if(!StringHelper.isEmpty(ddsj_z)){
			str.append(" and geo.time <=(to_date('"+ddsj_z+"','yyyy-mm-dd')+1) ");
		}
		str.append(" ORDER BY TIME desc");
		String fromsql = " from ("+str.toString()+")";
		count = baseCommonDao.getCountByFullSql(fromsql.toString());
//		List<Map> list = baseCommonDao.getPageDataByFullSql(str.toString(), page, rows);
		List<Map> list = baseCommonDao.getDataListByFullSql(str.toString());
		List<String> ids = new ArrayList<String>();
		List<Map> dyList = new ArrayList<Map>();
		List<Map> hList = new ArrayList<Map>();
		String zrzbdcdyid_h = "";
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				String bdcdyid_geo = StringHelper.formatObject(list.get(i).get("BDCDYID"));
				if (!ids.contains(bdcdyid_geo)) {
					ids.add(bdcdyid_geo);
				}
			}
		}
		if (ids != null && ids.size() > 0) {
			count = ids.size();
			for (int i = 0; i < ids.size(); i++) {
				String strh = "SELECT h.ZRZBDCDYID FROM BDCK.BDCS_H_XZ h WHERE bdcdyid = '" + ids.get(i) + "' ";
				hList = baseCommonDao.getDataListByFullSql(strh);
				if (hList != null && hList.size() > 0) {
					zrzbdcdyid_h = StringHelper.formatObject(hList.get(0).get("ZRZBDCDYID"));
				}
				for (int j = 0; j < list.size(); j++) {
					if (StringHelper.formatObject(ids.get(i)).equals(StringHelper.formatObject(list.get(j).get("BDCDYID")))) {
						if(zrzbdcdyid_h != ""){
							if (zrzbdcdyid_h.equals(StringHelper.formatObject(list.get(j).get("ZRZBDCDYID")))) {
								list.get(j).put("GL", "正确");
								BDCS_GEO geo = baseCommonDao.get(BDCS_GEO.class, StringHelper.formatObject(list.get(j).get("ID")));
								if (geo != null) {
									geo.setGL("正确");
									baseCommonDao.update(geo);
								}
							}
						}
						dyList.add(list.get(j));
						break;
					}
				}
			}
		}
		
		//排序
		if (dyList != null && dyList.size() > 0) {
			final String sortString = sort;
			if(!StringUtils.isEmpty(sortString) && sort.equals("BDCDYH")){
				Collections.sort(dyList,new Comparator<Map>(){
					@Override
					public int compare(Map o1, Map o2) {
						String str1 = StringHelper.formatObject(o1.get(sortString));
						String str2 = StringHelper.formatObject(o2.get(sortString));
						if(StringHelper.isEmpty(str1) || !StringHelper.isEmpty(str2) && ObjectHelper.cat(str1,str2) < 0)
							return -1;
						if(StringHelper.isEmpty(str2) || !StringHelper.isEmpty(str1) && ObjectHelper.cat(str1,str2) > 0)
							return 1;
						return 0;
					}
				});
			}else if (!StringUtils.isEmpty(sortString) && sort.equals("GL")) {
				Collections.sort(dyList,new Comparator<Map>(){
					@Override
					public int compare(Map o2, Map o1) {
						String str1 = StringHelper.formatObject(o1.get(sortString));
						String str2 = StringHelper.formatObject(o2.get(sortString));
						if(StringHelper.isEmpty(str1) || !StringHelper.isEmpty(str2) && ObjectHelper.cat(str1,str2) < 0)
							return -1;
						if(StringHelper.isEmpty(str2) || !StringHelper.isEmpty(str1) && ObjectHelper.cat(str1,str2) > 0)
							return 1;
						return 0;
					}
				});
			}
		}
		m.setTotal(count);
		m.setRows(dyList);
		return m;
	}
	
	@Override
	public Message queryAndPLXZ(Map<String, String> conditionmap, int page, int rows, boolean iflike, String sort,
			String order,String hzt) {
		Message m = new Message();
		long count = 0;
		StringBuilder str = new StringBuilder();
		StringBuilder strhzt = new StringBuilder();
		String zrzh = conditionmap.get("DY.ZRZH");
		String qlrmc = conditionmap.get("QLR.QLRMC");
		String qzh = conditionmap.get("QL.BDCQZH");
		String zjh = conditionmap.get("QLR.ZJH");
		String fwbm = conditionmap.get("DY.FWBM");
		String bdcdyh = conditionmap.get("DY.BDCDYH");
		String zl = conditionmap.get("DY.ZL");
		String fh = conditionmap.get("DY.FH");
		if(!StringHelper.isEmpty(hzt)&&"现房".equals(hzt)){
			strhzt.append(" BDCK.BDCS_H_XZ h ");
		}
		else if(!StringHelper.isEmpty(hzt)&&"期房".equals(hzt)){
			strhzt.append(" BDCK.BDCS_H_XZY h ");
		}
		str.append("SELECT h.ysdm,ql.iscancel,ROWNUM,h.bdcdyid,h.bdcdyh,ql.bdcqzh,h.fwbm,qlr.qlrmc,h.zl,h.fh,h.scjzmj FROM ").append(strhzt).append(" left join bdck.bdcs_djdy_xz djdy").append(" on h.bdcdyid=djdy.bdcdyid ")
		.append("LEFT JOIN BDCK.BDCS_QL_XZ ql ").append("on djdy.djdyid=ql.djdyid LEFT JOIN BDCK.BDCS_QLR_XZ qlr on ql.qlid=qlr.qlid where h.bdcdyid is not null");
		if(!StringHelper.isEmpty(fwbm)){
			str.append(" and h.fwbm='").append(fwbm).append("'");
		}
		if(!StringHelper.isEmpty(bdcdyh)){
			
			if(iflike)
				str.append(" and h.bdcdyh like'%").append(bdcdyh).append("%'");
			else
				str.append(" and h.bdcdyh ='").append(bdcdyh).append("'");
		}
		if(!StringHelper.isEmpty(zl)){
			if(iflike)
				str.append(" and h.zl like '%").append(zl).append("%'");
			else
				str.append(" and h.zl ='").append(zl).append("'");
		}
		if(!StringHelper.isEmpty(fh)){
			if(iflike)
				str.append(" and h.fh like '%").append(fh).append("%'");
			else
				str.append(" and h.fh ='").append(fh).append("'");
		}
		if(!StringHelper.isEmpty(qlrmc)){
			if(iflike)
				str.append(" and qlr.qlrmc like '%").append(qlrmc).append("%'");
			else
				str.append(" and qlr.qlrmc ='").append(qlrmc).append("'");
		}
		if(!StringHelper.isEmpty(qzh)){
			if(iflike)
				str.append(" and ql.bdcqzh like '%").append(qzh).append("%'");
			else
				str.append(" and ql.bdcqzh ='").append(qzh).append("'");
		}
		if(!StringHelper.isEmpty(zrzh)){
			if(iflike)
				str.append(" and h.zrzh like '%").append(zrzh).append("%'");
			else
				str.append(" and h.zrzh ='").append(zrzh).append("'");
		}
		if(!StringHelper.isEmpty(zjh)){
			str.append(" and qlr.zjh='").append(zjh).append("'");
		}
		if(!StringUtils.isEmpty(sort)&&!StringUtils.isEmpty(order))
		{
		if(sort.toUpperCase().equals("ZL"))
			sort="h.zl";
		if(sort.toUpperCase().equals("FH"))
			sort="h.fh";
		if(sort.toUpperCase().equals("BDCDYH"))
			sort="h.bdcdyh";
		if(sort.toUpperCase().equals("SCJZMJ"))
			sort="h.scjzmj";		
			str.append(" ORDER BY ").append(sort).append(" ").append(order);
		}
		String fromsql = " from ("+str.toString()+")";
		count = baseCommonDao.getCountByFullSql(fromsql.toString());
		List<Map> list = baseCommonDao.getPageDataByFullSql(str.toString(), page, rows);
		m.setTotal(count);
		m.setRows(list);
		return m;
	}	
	//获取宗地上的房屋抵押信息
	@SuppressWarnings({ "rawtypes"})
	protected String getHouseMortgageInfoForZD(String bdcdyid,String bdcdylx){
		long housecount = 0,mortgageCount = 0,mortgagingCount=0,notmortgage=0;
		String result = "房屋总共0套";
		//只有使用权宗地才有房屋
		if(!StringHelper.isEmpty(bdcdyid) && "02".equals(bdcdylx)){
			String fullSql = MessageFormat.format("select BDCDYH from BDCK.BDCS_H_XZ where zdbdcdyid=''{0}'' union all select BDCDYH from BDCK.BDCS_H_XZY where zdbdcdyid=''{0}''", bdcdyid);
			List<Map> bdcdyhs = baseCommonDao.getDataListByFullSql(fullSql);
			housecount=bdcdyhs.size();
					//已办理的业务
			        List<String> lstmortgage=new ArrayList<String>();
			        List<String> lstmortgageing=new ArrayList<String>();
					StringBuilder strdealed=new StringBuilder();
					strdealed.append("SELECT QL.QLLX,QL.DJLX,DY.BDCDYID FROM  ( ");
					strdealed.append("select BDCDYID,'031' BDCDYLX,ZDBDCDYID from BDCK.BDCS_H_XZ union  all  ");
					strdealed.append("select BDCDYID,'032' BDCDYLX,ZDBDCDYID from BDCK.BDCS_H_XZY ) DY   ");
					strdealed.append("LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON DJDY.BDCDYID=DY.BDCDYID AND DJDY.BDCDYLX=DY.BDCDYLX ");
					strdealed.append("LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=DJDY.DJDYID ");
					strdealed.append("WHERE QL.QLID IS NOT NULL AND DY.ZDBDCDYID='");
					strdealed.append(bdcdyid).append("'");
					String dealedSql=strdealed.toString();
					 List<Map> dealedmap=baseCommonDao.getDataListByFullSql(dealedSql);
					 if(!StringHelper.isEmpty(dealedmap) && dealedmap.size()>0){
						 for(Map m :dealedmap){
							String qllx= StringHelper.FormatByDatatype(m.get("QLLX"));
//							String djlx=StringHelper.FormatByDatatype(m.get("DJLX"));
							String hbdcdyid= StringHelper.FormatByDatatype(m.get("BDCDYID"));
							if(!lstmortgage.contains(hbdcdyid)){
								if(QLLX.DIYQ.Value.equals(qllx)){
									lstmortgage.add(hbdcdyid);
									mortgageCount++;
									}
								}
						 }
					 }
					//正在办理的业务
					StringBuilder strdealing=new StringBuilder();
					strdealing.append("SELECT QL.QLLX,QL.DJLX,DY.BDCDYID,XMXX.QLLX XMQLLX,XMXX.DJLX XMDJLX FROM  ( ");
					strdealing.append("select BDCDYID,'031' BDCDYLX,ZDBDCDYID from BDCK.BDCS_H_XZ union  all  ");
					strdealing.append("select BDCDYID,'032' BDCDYLX,ZDBDCDYID from BDCK.BDCS_H_XZY ) DY   ");
					strdealing.append("LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON DJDY.BDCDYID=DY.BDCDYID AND DJDY.BDCDYLX=DY.BDCDYLX  ");
					strdealing.append("LEFT JOIN BDCK.BDCS_QL_GZ QL ON QL.DJDYID=DJDY.DJDYID ");
					strdealing.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON XMXX.XMBH=QL.XMBH  ");
					strdealing.append("WHERE (XMXX.SFDB IS NULL OR XMXX.SFDB<>'1') AND QL.QLID IS NOT NULL  ");
					strdealing.append(" AND DY.ZDBDCDYID= '");
					strdealing.append(bdcdyid).append("'");
					String dealingsql=strdealing.toString();
					List<Map> dealingmap=baseCommonDao.getDataListByFullSql(dealingsql);
					 if(!StringHelper.isEmpty(dealingmap) && dealingmap.size()>0){
						 for(Map m :dealingmap){
							String qllx= StringHelper.FormatByDatatype(m.get("XMQLLX"));
							String djlx=StringHelper.FormatByDatatype(m.get("XMDJLX"));
							String hbdcdyid= StringHelper.FormatByDatatype(m.get("BDCDYID"));
								if(!lstmortgage.contains(hbdcdyid) && !lstmortgageing.contains(hbdcdyid)){
									if(!DJLX.ZXDJ.Value.equals(djlx) && QLLX.DIYQ.Value.equals(qllx)){
										mortgagingCount++;
										lstmortgageing.add(hbdcdyid);
									}
								}
						 }
					 }
			}
		notmortgage = housecount - mortgageCount - mortgagingCount;
		result =  MessageFormat.format("房屋总共{0}套：房屋已抵押{1}起,正在抵押{2}起,未抵押{3}起",housecount,mortgageCount,mortgagingCount,notmortgage);
		return result;
	}
	
	protected String getHouseMortgageInfoForZDEx(String bdcdyid,String bdcdylx){
		long housecount = 0,mortgageCount = 0,mortgagingCount=0,notmortgage=0;
		String result = "房屋总共0套";
		//只有使用权宗地才有房屋
		if(!StringHelper.isEmpty(bdcdyid) && "02".equals(bdcdylx)){
				String fullSql = MessageFormat.format("select BDCDYH from BDCK.BDCS_H_XZ where zdbdcdyid=''{0}'' union all select BDCDYH from BDCK.BDCS_H_XZY where zdbdcdyid=''{0}''", bdcdyid);
				List<Map> bdcdyhs = baseCommonDao.getDataListByFullSql(fullSql);
				housecount=bdcdyhs.size();
				//商品房的土地抵消状态赋值
				if(bdcdyhs != null && bdcdyhs.size() > 0){
					for(Map bdcdyh :bdcdyhs){
						if(bdcdyh.containsKey("BDCDYH")){
							String _bdcdyh = (String) bdcdyh.get("BDCDYH");
							if(_bdcdyh != null ){
								//已办理SQL语句
								String sqlMortgage = MessageFormat.format(" from BDCK.BDCS_QL_XZ where bdcdyh=''{0}'' and qllx=''23''", _bdcdyh);
								//正在办理的SQL语句
								String sqlMortgaging = MessageFormat.format("FROM bdck.bdcs_xmxx a  LEFT JOIN bdck.bdcs_ql_gz c ON a.xmbh=c.xmbh and  a.sfdb=''0'' WHERE c.qllx=''23'' and c.bdcdyh=''{0}'' and a.sfdb=''0'' ", _bdcdyh);
								
								long mcount = baseCommonDao.getCountByFullSql(sqlMortgage);
								if(mcount >0)
								   mortgageCount += 1;
								else if(baseCommonDao.getCountByFullSql(sqlMortgaging)>0)
										mortgagingCount+=1;
							}
						}
					}
					notmortgage = housecount - mortgageCount - mortgagingCount;
				}
				result =  MessageFormat.format("房屋总共{0}套：房屋已抵押{1}起,正在抵押{2}起,未抵押{3}起",housecount,mortgageCount,mortgagingCount,notmortgage);
			}			
		return result;
	}

	/**
	 * 通过权利ID获取权利信息
	 */
	@Override
	public QLInfo GetQLInfo(String qlid) {
		QLInfo qlinfo = new QLInfo();
		Rights ql=null;
		DJDYLY djdyly=DJDYLY.LS;
		ql=RightsTools.loadRights(djdyly, qlid);
		if(ql ==null){
			djdyly=DJDYLY.GZ;
			ql=RightsTools.loadRights(djdyly, qlid);
		}
		qlinfo.setql(ql);
		if (ql != null) {
			SubRights fsql = RightsTools.loadSubRightsByRightsID(djdyly,
					ql.getId());
			qlinfo.setfsql(fsql);
			List<RightsHolder> qlrlist = RightsHolderTools.loadRightsHolders(
					djdyly, ql.getId());
			qlinfo.setqlrlist(qlrlist);
		}
		return qlinfo; 
	}

	/**
	 * 获取单元信息通过权利ID和不动产单元类型
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> queryUnitInfo(String qlid,String bdcdylx) {
		HashMap<String, RealUnit> list=HistoryBackdateTools.QueryUnitInfoByQlid(qlid);
//		list.containsKey(key)
		List<Map> map=new ArrayList<Map>();
		if(list !=null && list.size()>0){
			if(list.size()==1){
				for(Entry<String, RealUnit> entry :list.entrySet()){
					map.add(StringHelper.beanToMap(entry.getValue()));
				}
			}else{				
				if(list.containsKey(BDCDYLX.H.Value) || list.containsKey(BDCDYLX.YCH.Value)){
					for(Entry<String, RealUnit> entry :list.entrySet()){
						if(BDCDYLX.H.Value.equals(entry.getKey()) || BDCDYLX.YCH.Value.equals(entry.getKey())){
							map.add(StringHelper.beanToMap(entry.getValue()));
							list.remove(entry.getKey());
							break;
						}
					}
				}
				if(list.containsKey(BDCDYLX.ZRZ.Value) || list.containsKey(BDCDYLX.YCZRZ.Value)){
					for(Entry<String, RealUnit> entry :list.entrySet()){
						if(BDCDYLX.ZRZ.Value.equals(entry.getKey()) || BDCDYLX.YCZRZ.Value.equals(entry.getKey())){
							map.add(StringHelper.beanToMap(entry.getValue()));
							list.remove(entry.getKey());
							break;
						}
					}
				}
				if(list.containsKey(BDCDYLX.SHYQZD.Value) || list.containsKey(BDCDYLX.SYQZD.Value)){
					for(Entry<String, RealUnit> entry :list.entrySet()){
						if(BDCDYLX.SHYQZD.Value.equals(entry.getKey()) || BDCDYLX.SYQZD.Value.equals(entry.getKey())){
							map.add(StringHelper.beanToMap(entry.getValue()));
							list.remove(entry.getKey());
							break;
						}
					}
				}
				for(Entry<String, RealUnit> entry :list.entrySet()){
						map.add(StringHelper.beanToMap(entry.getValue()));
						list.remove(entry.getKey());
				}
			}
			
			return map;
		}
		String housebdcdyid="";
		String landbdcdyid="";
		String buildingbdcdyid="";
		boolean lyflag=false;
		Rights rights=RightsTools.loadRights(DJDYLY.LS, qlid);
		if(rights ==null){
			rights=RightsTools.loadRights(DJDYLY.GZ, qlid);
			if(rights!=null){
				List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "DJDYID='"+rights.getDJDYID()+"'");
				if(djdys!=null&&djdys.size()>0){
					bdcdylx=djdys.get(0).getBDCDYLX();
				}
			}
			lyflag=true;
		}else{
			List<BDCS_DJDY_XZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "DJDYID='"+rights.getDJDYID()+"'");
			if(djdys!=null&&djdys.size()>0){
				bdcdylx=djdys.get(0).getBDCDYLX();
			}
		}
		if(rights !=null){
			String djdyid=rights.getDJDYID();
			Date qldjsj=rights.getDJSJ();
			String qlxmbh=rights.getXMBH();
			StringBuilder sql=new StringBuilder();
			sql.append(" XDJDYID = LDJDYID ");
			sql.append(" AND  XDJDYID='").append(djdyid).append("'");
			List<BDCS_DYBG> lstdybg=baseCommonDao.getDataList(BDCS_DYBG.class, sql.toString());
			List<HistoryUnitInfo> lsths= getHistoryUnits(lstdybg);
			lsths=sortDateByAsc(lsths);
			if(BDCDYLX.H.Value.equals(bdcdylx) || BDCDYLX.YCH.Value.equals(bdcdylx)){
				if(lyflag){
					if(lsths !=null && lsths.size()>0){
						housebdcdyid=lsths.get(lsths.size()-1).getBdcdyid();
					}
				}
				 if(StringHelper.isEmpty(housebdcdyid)){
					 housebdcdyid= getBDCDYID(lsths,qldjsj,qlxmbh);
					 if(StringHelper.isEmpty(housebdcdyid)){
						 housebdcdyid= getBDCDYIDbyDJDY(djdyid,qlxmbh);
					 }	 
				 }
				 RealUnit house=null;
				 if(BDCDYLX.H.Value.equals(bdcdylx)){
					 house= UnitTools.loadUnit(BDCDYLX.H, DJDYLY.LS, housebdcdyid);
					 if(house==null){
						 house=UnitTools.loadUnit(BDCDYLX.H, DJDYLY.GZ, housebdcdyid);
					 }
				 }else{
					 house= UnitTools.loadUnit(BDCDYLX.YCH, DJDYLY.LS, housebdcdyid);
					 if(house==null){
						 house=UnitTools.loadUnit(BDCDYLX.YCH, DJDYLY.XZ, housebdcdyid);
					 }
				 }
				 if(!StringHelper.isEmpty(house)){
					 House h=(House) house;
					String zdbdcdyid= h.getZDBDCDYID();
					String zrzbdcdyid= h.getZRZBDCDYID();
					if(lyflag){
						landbdcdyid=zdbdcdyid;
						buildingbdcdyid=zrzbdcdyid;
					}else{
						landbdcdyid=getLandBDCDYID(zdbdcdyid,qldjsj,qlxmbh);
						if(StringHelper.isEmpty(landbdcdyid)){
							landbdcdyid=zdbdcdyid;
						}
						buildingbdcdyid=getBuildingBDCDYID(zrzbdcdyid,qldjsj,qlxmbh);
						if(StringHelper.isEmpty(buildingbdcdyid)){
							buildingbdcdyid=zrzbdcdyid;
						}
					}
				 }
				 map= getuintByQl(housebdcdyid,buildingbdcdyid,landbdcdyid,bdcdylx);
			}
			else if(BDCDYLX.SHYQZD.Value.equals(bdcdylx) || BDCDYLX.SYQZD.Value.equals(bdcdylx)){
				if(lyflag){
					if(lsths !=null && lsths.size()>0){
						landbdcdyid=lsths.get(lsths.size()-1).getBdcdyid();
					}
				}
				if(StringHelper.isEmpty(landbdcdyid)){
					landbdcdyid= getBDCDYID(lsths,qldjsj,qlxmbh);
					 if(StringHelper.isEmpty(landbdcdyid)){
						 landbdcdyid= getBDCDYIDbyDJDY(djdyid,qlxmbh);
					 }
				}
				map=getuintByQl(landbdcdyid,buildingbdcdyid,landbdcdyid,bdcdylx);
			}
		}
		return map;
	}
	
	/**
	 * 通过DJDY获取不动产单元id
	 * @param djdyid ：登记单元ID
	 * @param qlxmbh:项目编号
	 * @return
	 */
	protected String getBDCDYIDbyDJDY(String djdyid,String qlxmbh){
		String bdcdyid="";
		 List<BDCS_DJDY_XZ> lstdjdy=baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "DJDYID='"+djdyid+"'");
		 if(lstdjdy !=null && lstdjdy.size()>0){
			 bdcdyid=lstdjdy.get(0).getBDCDYID(); 
		 }else{
			 List<BDCS_DJDY_GZ> lstdjdygz=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "DJDYID='"+djdyid+"' and XMBH='"+qlxmbh+"'");
			 if(lstdjdygz !=null && lstdjdygz.size()>0){
				 bdcdyid=lstdjdygz.get(0).getBDCDYID(); 
			 }
		 }
		 return bdcdyid;
	}
	/**
	 * 按照时间升序排序
	 * @param lsths
	 * @return
	 */
	protected List<HistoryUnitInfo> sortDateByAsc(List<HistoryUnitInfo> lsths){
		if(lsths !=null && lsths.size()>1){
			Collections.sort(lsths, new Comparator<HistoryUnitInfo>(){
				@Override
				public int compare(HistoryUnitInfo h1,HistoryUnitInfo h2){
				Date dt1=h1.getDjsj();
				Date dt2=h2.getDjsj();
				int flag=0;
				if(!StringHelper.isEmpty(dt1) && !StringHelper.isEmpty(dt2)){
					if(dt1.after(dt2)){
						flag= 1;
					}else{
						flag=-1;
					}
				}
				else if(!StringHelper.isEmpty(dt1) && StringHelper.isEmpty(dt2)){
					flag= -1;
				}else if( StringHelper.isEmpty(dt1) && !StringHelper.isEmpty(dt2)){
					flag= 1;
				}else{
					flag= 1;
				}
				return flag;
				}
			});
		}
		return lsths;
	}
	/**
	 * 获取不动产单元ID
	 * @param lsths:存放xmbh,djsj,bdcdyid集合
	 * @param qlsj：当前权利对应的时间
	 * @param qlxmbh：当前权利对应的项目编号
	 * @return
	 */
	protected String getBDCDYID(List<HistoryUnitInfo> lsths,Date qlsj,String qlxmbh){
		String bdcdyid="";
		if(lsths !=null && lsths.size()>0){
			 if(!StringHelper.isEmpty(qlxmbh)){
				for(int i=0;i<lsths.size();i++){
					if(qlxmbh.equals(lsths.get(i).getXmbh())){
						if(i!=lsths.size()-1){
							bdcdyid=lsths.get(i+1).getBdcdyid();
						}
						break;
					}
				} 
			 }
			 if(StringHelper.isEmpty(bdcdyid)){
				 if(StringHelper.isEmpty(qlsj)){
						bdcdyid=lsths.get(0).getBdcdyid();
					}else{
						for(int k=0;k<lsths.size();k++){
							if(k==lsths.size()-1){
								bdcdyid=lsths.get(k).getBdcdyid();
								break;
							}else{
								if(!StringHelper.isEmpty(lsths.get(k).getDjsj()) && qlsj.equals(lsths.get(k).getDjsj())){
									bdcdyid=lsths.get(k+1).getBdcdyid();
									break;
								}else{
									if(!StringHelper.isEmpty(lsths.get(k).getDjsj()) && !StringHelper.isEmpty(lsths.get(k+1).getDjsj())){
										if(qlsj.after(lsths.get(k).getDjsj()) && lsths.get(k+1).getDjsj().after(qlsj)){
											bdcdyid=lsths.get(k+1).getBdcdyid();
											break;
										}
									}else if(!StringHelper.isEmpty(lsths.get(k).getDjsj()) && StringHelper.isEmpty(lsths.get(k+1).getDjsj())){
										if(qlsj.before((lsths.get(k).getDjsj()))){
											bdcdyid=lsths.get(k).getBdcdyid();
											break;
										}else{
											bdcdyid=lsths.get(k+1).getBdcdyid();
											break;
										}
									}else{
										bdcdyid=lsths.get(k+1).getBdcdyid();
										break;
									}
								}
							}
						}
					} 
			 }
		}
		return bdcdyid;
	}
	
	/**
	 * 通过单元变更获取对应的历史单元信息
	 * @param lstdybg ：单元变更集合
	 * @return
	 */
	protected List<HistoryUnitInfo> getHistoryUnits(List<BDCS_DYBG> lstdybg){
		List<HistoryUnitInfo> lsths= new ArrayList<HistoryUnitInfo>();
		if(lstdybg !=null && lstdybg.size()>0){
			for(int i=0;i<lstdybg.size();i++){
				BDCS_DYBG dybg=lstdybg.get(i);
				HistoryUnitInfo hinfo= new HistoryUnitInfo();
				hinfo.setBdcdyid(dybg.getLBDCDYID());
				hinfo.setXmbh(dybg.getXMBH());
				lsths.add(hinfo);
				if(i==lstdybg.size()-1){
					HistoryUnitInfo hinfo1= new HistoryUnitInfo();
					hinfo1.setBdcdyid(dybg.getXBDCDYID());
					lsths.add(hinfo1);
				}
			}	
		}
		if(lsths !=null && lsths.size()>0){
			for(int j=0;j<lsths.size();j++){
				if(j!=lsths.size()-1){
					String xmbh=lsths.get(j).getXmbh();
					if(!StringHelper.isEmpty(xmbh)){
						List<BDCS_XMXX> lstxmxx=baseCommonDao.getDataList(BDCS_XMXX.class, "XMBH='"+xmbh+"'");
						if(lstxmxx !=null && lstxmxx.size()>0){
							lsths.get(j).setDjsj(lstxmxx.get(0).getDJSJ());
						}
					}
				}
			}				
		}
		return lsths;
	}
	
	/**
	 *  获取单元信息
	 * @param bdcdyid ：不动产单元id
	 * @param zrzbdcdyid：自然幢不动产单元ID
	 * @param zdbdcdyid:宗地不动产单元ID 
	 * @param bdcdylx:不动产单元类型
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected List<Map> getuintByQl(String bdcdyid,String zrzbdcdyid,String zdbdcdyid,String bdcdylx){
		List<Map> list = new ArrayList();
		RealUnit Hunit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx),
				DJDYLY.LS, bdcdyid);
		if (Hunit == null) {
			Hunit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.GZ,
					bdcdyid);
		}
		if (Hunit != null && BDCDYLX.H.equals(BDCDYLX.initFrom(bdcdylx))
				|| BDCDYLX.YCH.equals(BDCDYLX.initFrom(bdcdylx))) {
			list.add(StringHelper.beanToMap(Hunit));
			if (StringHelper.isEmpty(zrzbdcdyid)) {
				list.add(null);
			} else if (bdcdylx.equals(BDCDYLX.YCH.Value)) {
				RealUnit ZRZunit = UnitTools.loadUnit(BDCDYLX.YCZRZ,
						DJDYLY.LS, zrzbdcdyid);
				if(ZRZunit ==null){
					ZRZunit = UnitTools.loadUnit(BDCDYLX.YCZRZ,
							DJDYLY.XZ, zrzbdcdyid);
				}
				list.add(StringHelper.beanToMap(ZRZunit));
			} else {
				RealUnit ZRZunit = UnitTools.loadUnit(BDCDYLX.ZRZ,
						DJDYLY.LS, zrzbdcdyid);
				if(ZRZunit ==null){
					ZRZunit = UnitTools.loadUnit(BDCDYLX.ZRZ,
							DJDYLY.GZ, zrzbdcdyid);
				}
				list.add(StringHelper.beanToMap(ZRZunit));
			}
			if (StringHelper.isEmpty(zdbdcdyid)) {
				list.add(null);
			} else {
				RealUnit ZDunit = UnitTools.loadUnit(BDCDYLX.SHYQZD,
						DJDYLY.LS, zdbdcdyid);
				if (ZDunit == null) {
					ZDunit = UnitTools.loadUnit(BDCDYLX.SHYQZD,
							DJDYLY.GZ, zdbdcdyid);
					if(ZDunit ==null){
						ZDunit = UnitTools.loadUnit(BDCDYLX.SYQZD,
								DJDYLY.LS, zdbdcdyid);
						if(ZDunit ==null){
							ZDunit = UnitTools.loadUnit(BDCDYLX.SYQZD,
									DJDYLY.GZ, zdbdcdyid);
						}
					}
					list.add(StringHelper.beanToMap(ZDunit));
				} else {
					list.add(StringHelper.beanToMap(ZDunit));
				}
			}
		}
		else if (Hunit != null
				&& BDCDYLX.ZRZ.equals(BDCDYLX.initFrom(bdcdylx))
				|| BDCDYLX.YCZRZ.equals(BDCDYLX.initFrom(bdcdylx))){
			list.add(null);
		    list.add(StringHelper.beanToMap(Hunit));
			if (StringHelper.isEmpty(zdbdcdyid)) {
				list.add(null);
			} else {
				RealUnit ZDunit = UnitTools.loadUnit(BDCDYLX.SHYQZD,
						DJDYLY.initFrom("02"), zdbdcdyid);
				if (ZDunit == null) {
					ZDunit = UnitTools.loadUnit(BDCDYLX.SYQZD,
							DJDYLY.initFrom("02"), zdbdcdyid);
					list.add(StringHelper.beanToMap(ZDunit));
				} else {
					list.add(StringHelper.beanToMap(ZDunit));
				}
			}
		
		}
		else if (Hunit != null
				&& BDCDYLX.SHYQZD.equals(BDCDYLX.initFrom(bdcdylx))
				|| BDCDYLX.SYQZD.equals(BDCDYLX.initFrom(bdcdylx))) {
			String result = getHouseMortgageInfoForZD(bdcdyid,bdcdylx);
			Map map = StringHelper.beanToMap(Hunit);
			map.put("fwinfo_diy", result);
			list.add(map);
		}
		return list;
	}
	/**
	 * 获取自然幢的BDCDYID
	 * @return
	 */
	protected String getBuildingBDCDYID(String housezrzbdcdyid,Date qldjsj,String qlxmbh){
		StringBuilder sql=new StringBuilder();
		sql.append("  XBDCDYID='").append(housezrzbdcdyid).append("'");
		List<BDCS_DYBG> lstdybg=baseCommonDao.getDataList(BDCS_DYBG.class, sql.toString());
		List<HistoryUnitInfo> lsths= getHistoryUnits(lstdybg);
		lsths=sortDateByAsc(lsths);
		return getBDCDYID(lsths,qldjsj,qlxmbh);
	}
	/**
	 * 获取宗地的BDCDYID
	 * @return
	 */
	protected String getLandBDCDYID(String housezrzbdcdyid,Date qldjsj,String qlxmbh){
		StringBuilder sql=new StringBuilder();
		sql.append(" XDJDYID = LDJDYID ");
		sql.append(" AND  XBDCDYID='").append(housezrzbdcdyid).append("'");
		List<BDCS_DYBG> lstdybg=baseCommonDao.getDataList(BDCS_DYBG.class, sql.toString());
		List<HistoryUnitInfo> lsths= getHistoryUnits(lstdybg);
		lsths=sortDateByAsc(lsths);
		return getBDCDYID(lsths,qldjsj,qlxmbh);
	}

	/**
	 * @Description: 通过zrzbdcdyid获取权利人
	 * @Title: GetQlrmcByZrz
	 * @Author: zhaomengfan
	 * @Date: 2017年5月15日下午2:51:01
	 * @param zrzbdcdyid
	 * @return
	 * 
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, String> GetQlrmcByZrz(String zrzbdcdyid,String bdcdylx) {
		String fulSql = " SELECT H.BDCDYID,QLR.QLRMC ";
		if("031".equals(bdcdylx)||"032".equals(bdcdylx)){
			 RealUnit unit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.LS, zrzbdcdyid);
			 if(unit==null)
				 unit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.GZ, zrzbdcdyid);
			 if(unit==null)
				 unit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.DC, zrzbdcdyid);
			 if(unit!=null)
				 zrzbdcdyid=((House) unit).getZRZBDCDYID();
		}
		if("08".equals(bdcdylx)||"032".equals(bdcdylx)){
			fulSql+=" FROM BDCK.BDCS_H_XZY H ";
		}else{
			fulSql+=" FROM BDCK.BDCS_H_XZ H ";
		}
		fulSql+=" LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.BDCDYID=H.BDCDYID "
				+ " LEFT JOIN BDCK.BDCS_QLR_XZ QLR ON QLR.QLID=QL.QLID "
				+ " WHERE H.ZRZBDCDYID='"+zrzbdcdyid+"' AND QL.QLLX IN ('4','6','8')";
		List<Map> map = new ArrayList<Map>();
		map = baseCommonDao.getDataListByFullSql(fulSql);
		Map<String,String> qlrmcs = new HashMap<String, String>();
		for (Map m : map) {
			String bdcdyid = StringHelper.formatObject(m.get("BDCDYID"));
			String qlrmc = StringHelper.formatObject(m.get("QLRMC"));
			if(qlrmcs.containsKey(bdcdyid)){
				if(!StringHelper.isEmpty(qlrmc)){
					qlrmcs.put(bdcdyid, qlrmcs.get(bdcdyid)+","+qlrmc);
				}
			}else{
				if(!StringHelper.isEmpty(qlrmc)){
					qlrmcs.put(bdcdyid, qlrmc);
				}
			}
		}
		return qlrmcs;
	}
	
	/** 根据户的BDCDYID获取所在自然幢的BDCDYID
	 * @param hBdcdyid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String getZrzBdcdyid(String hBdcdyid){
		String zBdcdyid = "";
		String fulSql = "SELECT H.ZRZBDCDYID FROM BDCK.BDCS_H_XZ H WHERE BDCDYID='" + hBdcdyid + "'";
		List<Map> hlist = baseCommonDao.getDataListByFullSql(fulSql);
		if (hlist != null && hlist.size() > 0) {
			if (hlist.get(0) != null) {
				Map h = hlist.get(0);
				if (h.get("ZRZBDCDYID") != null && h.get("ZRZBDCDYID") != "") {
					zBdcdyid = StringHelper.formatObject(h.get("ZRZBDCDYID"));
				}
			}
		}
		return zBdcdyid;
	}
	
		/**
	 * 海域查询
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Message querySea(Map<String, String> queryvalues, int page,
			int rows, boolean iflike,String sort,String order) {
		Message msg = new Message();
		long count = 0;
		List<Map> listresult = null;
		String cxzt=queryvalues.get("CXZT");
		queryvalues.remove("CXZT");
		//结果筛选 --- 是否只显示产权
		String jgsx=queryvalues.get("JGSX");
		queryvalues.remove("JGSX");
		//区域筛选——只查询有权限查询的区域
		String searchstates=queryvalues.get("SEARCHSTATES");
		queryvalues.remove("SEARCHSTATES");
		
		String fsqlcfwh=queryvalues.get("FSQL.CFWH");
		/* ===============1、先获取实体对应的表名=================== */
		
		String unitentityName = "BDCK.BDCS_ZH_XZ";
		if ("2".equals(cxzt)) {
			unitentityName = "BDCK.BDCS_ZH_LS";
		}

		/* ===============2、再获取表名+'_'+字段名=================== */
		String dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.ZL,DY.BDCDYH,DY.ZHDM,DY.QXDM,DY.XMMC,DY.XMXX,DY.YHZMJ,DY.ZHMJ,DY.DB,'04' AS BDCDYLX,DY.ZHAX,DY.YHLXA,DY.YHLXB,DY.YHWZSM,DY.SYJZE,DY.SYJBZYJ,"
				+"DY.SYJJNQK,DY.HCJS,DY.HCR,DY.HCRQ,DY.CLJS,DY.CLR,DY.CLRQ,DY.SHYJ,DY.SHR,DY.SHRQ";
		String qlfieldsname = "XM.YWLSH,QL.QLID,QL.BDCQZH,QL.QLLX,QL.DJSJ";
		String fsqlfieldsname = "FSQL.FSQLID,FSQL.CFWH";
		//String yhzkfieldsname = "YHZK.YHFS";


		 /*===============3、构造查询语句=================== */
		 /* SELECT 字段部分 */
		StringBuilder builder2 = new StringBuilder();
		builder2.append("select ").append(dyfieldsname).append(",")
				.append(qlfieldsname);
		if(!StringHelper.isEmpty(fsqlcfwh)){
			builder2.append(",").append(fsqlfieldsname);
		}
		String selectstr = builder2.toString();

		/* FROM 后边的表语句 */
		StringBuilder builder = new StringBuilder();
		if ("2".equals(cxzt)) {
			builder.append(" from {0} DY")
					.append(" left join BDCK.BDCS_DJDY_LS DJDY on dy.bdcdyid=djdy.bdcdyid")
					.append(" left join BDCK.bdcs_ql_ls ql on ql.djdyid=djdy.djdyid  ")
					.append(" left join BDCK.BDCS_XMXX XM on XM.XMBH = QL.XMBH");
					
		}else{
			builder.append(" from {0} DY")
					.append(" left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid")
					.append(" left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid ")
					.append(" left join BDCK.BDCS_XMXX XM on XM.XMBH = QL.XMBH");
		}
		if(!StringHelper.isEmpty(fsqlcfwh)){
			builder.append(" left join BDCK.bdcs_fsql_xz fsql on ql.fsqlid=fsql.fsqlid  ");
		}
		String fromstr = builder.toString();
		fromstr = MessageFormat.format(fromstr, unitentityName);

		 /* WHERE 条件语句 */
		StringBuilder builder3 = new StringBuilder();
		builder3.append(" where  ");
		StringBuilder qlrbuilder = new StringBuilder();
		StringBuilder dyrbuilder = new StringBuilder();
		boolean havecondition = false;
		boolean haveqlr = false;
		boolean havedyr = false;
		for (Entry<String, String> ent : queryvalues.entrySet()) {
			String name = ent.getKey();
			String value = ent.getValue();
			if (!StringHelper.isEmpty(value)) {
				// 抵押状态和查封状态，为‘0’的时候表示不过滤，查询全部
				if ((name.equals("CFZT") && value.equals("0"))
						|| (name.equals("DYZT") && value.equals("0"))
							||name.equals("YYZT") && value.equals("0")) {
					continue;
				}
				// 权利人，权利人的条件比较特殊，where里边又套了where
				if (name.startsWith("QLR.")) {
					if (haveqlr)
						qlrbuilder.append(" and ");
					if (iflike) {
						qlrbuilder.append(" instr(" + name + ",'" + value.trim()
								+ "')>0 ");
					} else {
						if (name.equals("QLR.ZJH") && value.length() == 18) {
							String oldCard = ConverIdCard.getOldIDCard(value);
							if (oldCard.length() != 15) {
								qlrbuilder.append(" " + name + "='" + value.trim()
										+ "' ");
							} else {
								qlrbuilder.append(" (" + name + "='" + value.trim()
										+ "' or ");
								qlrbuilder.append(" " + name + "='" + oldCard.trim()
										+ "') ");
							}
						} else {
							qlrbuilder.append(" " + name + "='" + value.trim() + "' ");
						}
					}
					haveqlr = true;
					continue;
				}
				// 抵押人判断
				if (name.startsWith("DYR.")) {
					if (havedyr)
						dyrbuilder.append(" and ");
					if (iflike) {
						dyrbuilder.append(" instr(" + name + ",'" + value
								+ "')>0 ");
					} else {
						dyrbuilder.append(" " + name + "='" + value + "' ");
					}
					havedyr = true;
					continue;
				}
				if (havecondition) {
					builder3.append(" and ");
				}

				// 抵押状态
				if (name.equals("DYZT")) {
					if (value.equals("1")) {
						if ("2".equals(cxzt)) {
							builder3.append(" djdy.djdyid NOT IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_LS DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
						}else{
							builder3.append(" djdy.djdyid NOT IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_XZ DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
						}
					} else {
						if ("2".equals(cxzt)) {
							builder3.append(" djdy.djdyid IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_LS DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
						}else{
							builder3.append(" djdy.djdyid IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_XZ DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
						}
					}
					havecondition = true;
					continue;
				}
				// 查封状态
				if (name.equals("CFZT")) {
					if (value.equals("1")) {
						if ("2".equals(cxzt)) {
							builder3.append("  djdy.DJDYID NOT IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_LS CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99') ");
						}else{
							builder3.append("  djdy.DJDYID NOT IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_XZ CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99') ");
						}
					} else {
						if ("2".equals(cxzt)) {
							builder3.append("  djdy.DJDYID IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_LS CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99' AND CF.DJLX='800') ");
						
						}else{
							builder3.append("  djdy.DJDYID IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_XZ CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99' AND CF.DJLX='800') ");
						
						}
					}
					havecondition = true;
					continue;
				}
				// 异议状态
				if (name.equals("YYZT")) {
					if (value.equals("1")) {
						if ("2".equals( cxzt)) {
							builder3.append("  djdy.DJDYID NOT IN (SELECT YY.DJDYID FROM BDCK.BDCS_QL_LS YY WHERE YY.DJDYID=DJDYID AND YY.djlx = '600') ");
						}else{
							builder3.append("  djdy.DJDYID NOT IN (SELECT YY.DJDYID FROM BDCK.BDCS_QL_XZ YY WHERE YY.DJDYID=DJDYID AND YY.djlx = '600') ");
						}
					} else {
						if ("2".equals(cxzt)) {
							builder3.append("  djdy.DJDYID IN (SELECT YY.DJDYID FROM BDCK.BDCS_QL_LS YY WHERE YY.DJDYID=DJDYID AND  YY.djlx = '600') ");
						}else{
							builder3.append("  djdy.DJDYID IN (SELECT YY.DJDYID FROM BDCK.BDCS_QL_XZ YY WHERE YY.DJDYID=DJDYID AND  YY.djlx = '600') ");
						}
					}
					havecondition = true;
					continue;
				}

				if (iflike) {
					if (name.contains("BDCQZH")) {
						// 验证不动产权证号是否为纯数字的
						boolean flag = StringHelper.isNumericByString(value);
						if (flag) {
							builder3.append(" instr(" + name + ",'" + value + "')>0 ");
						} else {
							builder3.append(" instr(upper(" + name + "),'" + value.toUpperCase() + "')>0 ");
						}
					}else if (name.equals("DJSJ_Q")) {
						builder3.append("  QL.DJSJ >=to_date('"+queryvalues.get("DJSJ_Q")+"','yyyy-mm-dd') ");
					}else if (name.equals("DJSJ_Z")) {
						builder3.append("   QL.DJSJ <=(to_date('"+queryvalues.get("DJSJ_Z")+"','yyyy-mm-dd')+1) ");
					}else{
					    builder3.append(" instr(" + name + ",'" + value + "')>0 ");
					}
					
					
					
				} else {
					/*
					 * 如果通过不动产权证查询，且是精确查询时,
					 * 其中如果不动产权证号是否纯数字的，直接在权利人表中通过BDCQZHXH条件查询，
					 * 否则从权利表中通过BDCQZH条件查询
					 */
					 
					if (name.contains("BDCQZH")) {
						String cdbz = queryvalues.get("SWCDBZ");//
						// 验证不动产权证号是否为纯数字的
						boolean flag = StringHelper.isNumericByString(value);
						if (flag) {
							// 判断是否已经有查询权利人的条件了
							if (haveqlr) {
								qlrbuilder.append(" and ");
							}
							qlrbuilder.append(" QLR.BDCQZHXH " + "='" + value
									+ "' ");
							haveqlr = true;
							continue;
						} else {
							if(cdbz != null && !cdbz.equals("") && cdbz.equals("cd")){
								builder3.append(" " + name + " like '%" + value + "%' ");
							}else{
								builder3.append(" " + name + " ='" + value + "' ");
							}
						}
					}else if (name.equals("DJSJ_Q")) {
						builder3.append("  QL.DJSJ >=to_date('"+queryvalues.get("DJSJ_Q")+"','yyyy-mm-dd') ");
					}else if (name.equals("DJSJ_Z")) {
						builder3.append("   QL.DJSJ <=(to_date('"+queryvalues.get("DJSJ_Z")+"','yyyy-mm-dd')+1) ");
					}else {
						builder3.append(" " + name + "='" + value + "' ");
					}
				}
				havecondition = true;
			}
		}
		// 有权利人查询条件
		if (!StringHelper.isEmpty(qlrbuilder.toString())) {
			if (havecondition) {
				builder3.append(" and ");
			}
			if ("2".equals(cxzt)) {
				builder3.append(" EXISTS (SELECT 1 FROM BDCK.BDCS_QLR_LS QLR WHERE QLR.QLID=QL.QLID AND "
						+ qlrbuilder.toString() + ")");
			}else{
				builder3.append(" EXISTS (SELECT 1 FROM BDCK.BDCS_QLR_XZ QLR WHERE QLR.QLID=QL.QLID AND "
						+ qlrbuilder.toString() + ")");
			}
			havecondition = true;
		}

		// 有抵押人查询条件
		if (!StringHelper.isEmpty(dyrbuilder.toString())) {
			if (havecondition) {
				builder3.append(" and ");
			}
			if ("2".equals(cxzt)) {
				builder3.append(" QL.QLID IN (SELECT QLID FROM BDCK.BDCS_FSQL_LS DYR WHERE "
						+ dyrbuilder.toString() + ")");
			}else{
				builder3.append(" QL.QLID IN (SELECT QLID FROM BDCK.BDCS_FSQL_XZ DYR WHERE "
						+ dyrbuilder.toString() + ")");
			}
			havecondition = true;
		}

		if ("2".equals(jgsx)) {
			if (havecondition) {
				 builder3.append(" and ");
			}
			 builder3.append(" ql.qllx in ('15','6','8')");
			 havecondition=true;
		}
		if (!StringHelper.isEmpty(searchstates)) {
			String[] states = searchstates.split("-");
			String state = "";
			for (int i = 0; i < states.length-1; i++) {
				state += "'"+states[i]+"',";
			}
				state += "'"+states[states.length-1]+"'";
			if (havecondition) {
				 builder3.append(" and ");
			}
			 builder3.append(" DY.SEARCHSTATE in (").append(state).append(")");
		}
		
		String wherestr = builder3.toString();
		if (wherestr.trim().toUpperCase().endsWith("WHERE")) {
			wherestr = "";
		}
		String fromSql = " from (select ql.qlid " + fromstr + wherestr + ")";
		String fullSql = selectstr + fromstr + wherestr;
		/* 排序 条件语句 */
		
		if(!StringUtils.isEmpty(sort)&&!StringUtils.isEmpty(order))
		{
		if(sort.toUpperCase().equals("ZL"))
			sort="DY.ZL";
		if(sort.toUpperCase().equals("BDCDYH"))
			sort="DY.BDCDYH";
		/*if(sort.toUpperCase().equals("QLRMC"))
			sort="QLR.QLRMC";
		if(sort.toUpperCase().equals("ZJH"))
			sort="QLR.ZJH";*/
		
		if(sort.toUpperCase().equals("BDCQZH"))
			sort="QL.BDCQZH";
		if(sort.toUpperCase().equals("FH"))
			sort="DY.FH";
		if(sort.toUpperCase().equals("QLLX"))
			sort="QL.QLLX";
		if(sort.toUpperCase().equals("YWLSH"))
			sort="XM.YWLSH";
		/*if(sort.toUpperCase().equals("ZJHM"))
			sort="QLR.ZJHM";*/
		/*if(sort.toUpperCase().equals("GHYTNAME"))
			sort="DY.GHYT";
		if(sort.toUpperCase().equals("BDCDYID"))
			sort="DY.BDCDYID";
		if(sort.toUpperCase().equals("BDCDYLXMC"))
			sort="DY.BDCDYLX";
        if(sort.toUpperCase().equals("ZRZH"))
			sort="DY.ZRZH";		*/
			fullSql=fullSql+" ORDER BY "+sort+" "+order;
		}
		/* 为了提高效率，先不查询符合条件的记录总数，将来改为异步获取 */
		
		CommonDao dao = baseCommonDao;

		count = dao.getCountByFullSql(fromSql);

		
		listresult = dao.getPageDataByFullSql(fullSql, page, rows);
		addRightsHolderInfo(listresult);
		addDyCfDetails(listresult);
		//添加状态
		addBDCDYZT_SEA(listresult);
		addQLLX(listresult);
		addQLZT(listresult);
		addDyCfDetails(listresult);
		ADDXMXX_SIZE(listresult);
		ADDYHLXA(listresult);
		ADDYHLXB(listresult);
		ADDYHFS(listresult,cxzt);
		
		addLimitStatusSea(listresult,"");
		
		// 格式化结果中的常量值
		for (Map map : listresult) {
			if (map.containsKey("DJSJ")) {
				map.put("DJSJ",StringHelper.FormatYmdhmsByDate(map.get("DJSJ")));
			}
		}
	
		msg.setTotal(count);
		msg.setRows(listresult);
		
		return msg;
	}
	/**
	 * 用海类型（YHLXA）
	 * @param result
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void ADDYHLXA(List<Map> result) {
		if (result != null && result.size() > 0) {
			for (Map<String, String> map : result) {
				if(!StringUtils.isEmpty(map.get("YHLXA"))){
					String yhlx_A = ConstHelper.getNameByValue("HYSYLXA", (String)map.get("YHLXA"));
					map.put("YHLXA",yhlx_A);
				}
			}
		}
	}
	
	/**
	 * 用海类型（YHLXB）
	 * @param result
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void ADDYHLXB(List<Map> result) {
		if (result != null && result.size() > 0) {
			for (Map<String, String> map : result) {
				
				if(!StringUtils.isEmpty(map.get("YHLXB"))){
					String yhlx_B = ConstHelper.getNameByValue("HYSYLXB", (String)map.get("YHLXB"));
					map.put("YHLXB",yhlx_B);
				}
			}
		}
	}
	
	/**
	 * 用海方式
	 * @param result
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void ADDYHFS(List<Map> result,String cxzt) {
		CommonDao dao = baseCommonDao;
		String Sql = "";
		if (result != null && result.size() > 0) {
			for (Map<String, String> map : result) {
				if ("2".equals(cxzt)) {
					Sql = "select * from BDCK.BDCS_YHZK_XZ where bdcdyid = '"+map.get("BDCDYID")+"'";
				}else{
					Sql = "select * from BDCK.BDCS_YHZK_LS where bdcdyid = '"+map.get("BDCDYID")+"'";
				}
				List<Map> list = dao.getDataListByFullSql(Sql);
				String yhfs = "";
				int i = 0;
				for(Map<String,String> yhzk : list){
					if(i > 0){
						yhfs += "、";
					}
					if(!StringUtils.isEmpty(yhzk.get("YHFS"))){
						yhfs += ConstHelper.getNameByValue("YHFS", (String)yhzk.get("YHFS"));
					}
					i++;
				}
				map.put("YHFS",yhfs);
			}
		}
	}
	
	/**
	 * 海域状态
	 * @param result
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void addBDCDYZT_SEA(List<Map> result) {
		if (result != null && result.size() > 0) {
			for (Map map : result) {
				if(!StringUtils.isEmpty(map.get("BDCDYID"))){
					String isexsit_zx_sql = MessageFormat.format(" FROM (SELECT BDCDYID FROM BDCK.BDCS_ZH_XZ) DY "
							+ " WHERE DY.BDCDYID=''{0}'' ",
							map.get("BDCDYID"));
					Long count_zx=baseCommonDao.getCountByFullSql(isexsit_zx_sql);
					if (count_zx > 0) {
						map.put("BDCDYZT", "现状");
					}else {
						map.put("BDCDYZT", "历史");
					}
			}
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void addLimitStatusSea(List<Map> result,String cdbz) {
		if (result != null && result.size() > 0) {
			for (Map map : result) {
				if (map.containsKey("DJDYID")) {
					String djdyid = (String) map.get("DJDYID");
					if (djdyid != null) {
						List<BDCS_DJDY_XZ> listdjdy = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "djdyid='"
										+ djdyid + "'");
						if (listdjdy != null && listdjdy.size() > 0) {
							BDCDYLX lx = BDCDYLX.initFrom(listdjdy.get(0).getBDCDYLX());
							if (lx.Value.equals("04")) {
								String valuedyxz = "";
								String valuecfxz = "";
								String valueyyxz = "";
								String valuexzxz = "";
								List<BDCS_ZH_XZ> listzh = baseCommonDao.getDataList(BDCS_ZH_XZ.class,
										"bdcdyid='" + listdjdy.get(0).getBDCDYID() + "'");
								if (listzh != null && listzh.size() > 0 && listzh.get(0).getId()!= null) {
									Map<String, String> mapxz = new HashMap<String, String>();
									mapxz = getDYandCFandYY_XZ_SEA(djdyid,cdbz);
									
									for (Entry<String, String> ent : mapxz.entrySet()) {
										String name = ent.getKey();
										if (name.equals("DYZT")) {
											if (StringHelper.isEmpty(valuedyxz)) {
												valuedyxz = ent.getValue();
											} else {
												valuedyxz = valuedyxz + " " + ent.getValue();
											}
										} else if (name.equals("CFZT")) {
											if (StringHelper.isEmpty(valuecfxz)) {
												valuecfxz = ent.getValue();
											} else {
												valuecfxz = valuecfxz + " " + ent.getValue();
											}
										} else  if (name.equals("YYZT")) {
											if (StringHelper.isEmpty(valueyyxz)) {
												valueyyxz = ent.getValue();
											} else {
												valueyyxz = valueyyxz + " " + ent.getValue();
											}
										}else if (name.equals("XZZT")){
											if (StringHelper.isEmpty(valuexzxz)) {
												valuexzxz = ent.getValue();
											} else {
												valuexzxz = valuexzxz + "、" + ent.getValue();
											}
										}
									}
								
								}
								/*Map<String, String> mapxzy = new HashMap<String, String>();
								mapxzy = getDYandCFandYY_XZY(djdyid,cdbz);
								String valuedyxz = "";
								String valuecfxz = "";
								String valueyyxz = "";
								String valuexzxz = "";
								String valueygxz = "";//不动产权利查档时需要查预告登记
								for (Entry<String, String> ent : mapxzy.entrySet()) {
									String name = ent.getKey();
									if (name.equals("DYZT")) {
										if (StringHelper.isEmpty(valuedyxz)) {
											valuedyxz = ent.getValue();
										} else {
											valuedyxz = valuedyxz + " " + ent.getValue();
										}
									} else if (name.equals("CFZT")) {
										if (StringHelper.isEmpty(valuecfxz)) {
											valuecfxz = ent.getValue();
										} else {
											valuecfxz = valuecfxz + " " + ent.getValue();
										}
									} else  if (name.equals("YYZT")) {
										if (StringHelper.isEmpty(valueyyxz)) {
											valueyyxz = ent.getValue();
										} else {
											valueyyxz = valueyyxz + " " + ent.getValue();
										}
									}else  if (name.equals("YGDJZT")) {
										if (StringHelper.isEmpty(valueyyxz)) {
											valueygxz = ent.getValue();
										} else {
											valueygxz = valueygxz + " "
													+ ent.getValue();
										}
									}else if (name.equals("XZZT")){
										if (StringHelper.isEmpty(valuexzxz)) {
											valuexzxz = ent.getValue();
										} else {
											valuexzxz = valuexzxz + " " + ent.getValue();
										}
									}
								}*/
								map.put("DYZT", valuedyxz);
								map.put("CFZT", valuecfxz);
								map.put("YYZT", valueyyxz);
								map.put("XZZT", valuexzxz);
							} 
						}
					}

				}
			}
		}
	}
	
	public Map<String, String> getDYandCFandYY_XZ_SEA(String djdyid,String cdbz) {
		List<BDCS_DJDY_XZ> dy = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, " DJDYID='"+djdyid+"'");
		String bdcdyid = null;
		if(dy!=null&&dy.size()>0){
			bdcdyid = dy.get(0).getBDCDYID();
		}
		Map<String, String> map = new HashMap<String, String>();
		String sqlMortgage = MessageFormat.format(
				" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and qllx=''23''",
				djdyid);
		String sqlSeal = MessageFormat
				.format(" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''800'' and qllx=''99''",
						djdyid);
		String sqlObjection = MessageFormat
				.format("  from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''600'' ",
						djdyid);
		String sqlYgdj = MessageFormat
				.format("  from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''700'' ",
						djdyid);//查预告登记
						
		String sqlLimit = MessageFormat
				.format("  from BDCK.BDCS_DYXZ where bdcdyid=''{0}'' and yxbz=''1'' ",
						bdcdyid);

		long mortgageCount = baseCommonDao.getCountByFullSql(sqlMortgage);
		long SealCount = baseCommonDao.getCountByFullSql(sqlSeal);
		long ObjectionCount = baseCommonDao.getCountByFullSql(sqlObjection);
		long LimitCount = baseCommonDao.getCountByFullSql(sqlLimit);
		long ygdjCount = baseCommonDao.getCountByFullSql(sqlYgdj);

		String sealStatus = "";
		String mortgageStatus = "";
		String ygdjStatus = "";
		String LimitStatus = "";
		if(cdbz != null && cdbz.equals("cd")){
			mortgageStatus = mortgageCount > 0 ? "海域已抵押" +"("+mortgageCount+"笔)" : "海域无抵押";
			sealStatus = SealCount > 0 ? "海域已查封" +"("+SealCount+"笔)" : "海域无查封";
			LimitStatus = LimitCount > 0 ? "海域已限制" : "海域无限制";
			
			// 判断完现状层中的查封信息，接着判断办理中的查封信息
			if (!(SealCount > 0)) {
				String sqlSealing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and  b.djdyid=c.djdyid WHERE c.djlx='800' AND c.qllx='99' and c.djdyid='"
						+ djdyid + "' and a.sfdb='0' ";
				long count = baseCommonDao.getCountByFullSql(sqlSealing);
		
		        sealStatus = count > 0 ? "海域查封办理中" +"("+count+"笔)" : "海域无查封";
			}

			// 改为判断完查封 人后判断限制
			if (!(LimitCount > 0)) {
				String sqlLimiting = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_dyxz c ON b.xmbh=c.xmbh and b.bdcdyid=c.bdcdyid WHERE c.yxbz='0' and c.bdcdyid='"
						+ bdcdyid + "' and a.sfdb='0' ";
				long count = baseCommonDao.getCountByFullSql(sqlLimiting);
				LimitStatus = count > 0 ? "海域限制办理中" : "海域无限制";
			}
			// 判断完现状层中的查封信息，接着判断办理中的查封信息
			if (!(mortgageCount > 0)) {
				String sqlmortgageing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='100' AND c.qllx='23' and c.djdyid='"
						+ djdyid + "' and a.sfdb='0' ";
				long count = baseCommonDao.getCountByFullSql(sqlmortgageing);
				mortgageStatus = count > 0 ? "海域抵押办理中"+"("+count+"笔)" : "海域无抵押";
			}

			// 预告登记
			if (!(mortgageCount > 0)) {
				String sqlygdj = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='700' and c.djdyid='"
						+ djdyid + "' and a.sfdb='0' ";
				long count = baseCommonDao.getCountByFullSql(sqlygdj);
				ygdjStatus = count > 0 ? "海域预告登记"+"("+count+"笔)" : "海域无预告登记";
			}
		}else{
			mortgageStatus = mortgageCount > 0 ? "海域已抵押" : "海域无抵押";
			sealStatus = SealCount > 0 ? "海域已查封" : "海域无查封";
			LimitStatus = LimitCount > 0 ? "海域已限制" : "海域无限制";
			// 判断完现状层中的查封信息，接着判断办理中的查封信息
			if (!(SealCount > 0)) {
				String sqlSealing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and  b.djdyid=c.djdyid WHERE c.djlx='800' AND c.qllx='99' and c.djdyid='"
						+ djdyid + "' and a.sfdb='0' ";
				long count = baseCommonDao.getCountByFullSql(sqlSealing);
				sealStatus = count > 0 ? "海域查封办理中" : "海域无查封";
			}

			// 改为判断完查封 人后判断限制
			if (!(LimitCount > 0)) {
				String sqlLimiting = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_dyxz c ON b.xmbh=c.xmbh and b.bdcdyid=c.bdcdyid WHERE c.yxbz='0' and c.bdcdyid='"
						+ bdcdyid + "' and a.sfdb='0' ";
				long count = baseCommonDao.getCountByFullSql(sqlLimiting);
				LimitStatus = count > 0 ? "海域限制办理中" : "海域无限制";
			}

			// 判断完现状层中的查封信息，接着判断办理中的查封信息
			if (!(mortgageCount > 0)) {
				String sqlmortgageing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='100' AND c.qllx='23' and c.djdyid='"
						+ djdyid + "' and a.sfdb='0' ";
				long count = baseCommonDao.getCountByFullSql(sqlmortgageing);
				mortgageStatus = count > 0 ? "海域抵押办理中" : "海域无抵押";
			}

			// 预告登记
			if (!(mortgageCount > 0)) {
				String sqlygdj = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='700' and c.djdyid='"
						+ djdyid + "' and a.sfdb='0' ";
				long count = baseCommonDao.getCountByFullSql(sqlygdj);
				ygdjStatus = count > 0 ? "海域预告登记中" : "海域无预告登记中";
			}
		}

		String objectionStatus = ObjectionCount > 0 ? "海域有异议" : "海域无异议";
		map.put("DYZTFLAG", String.valueOf(mortgageCount));
		map.put("CFZTFLAG", String.valueOf(SealCount));
		map.put("DYZT", mortgageStatus);
		map.put("CFZT", sealStatus);
		map.put("YYZT", objectionStatus);
		map.put("XZZT", LimitStatus);
		map.put("YGDJZT", ygdjStatus);

		return map;
	}

	@Override
	public Object queryhousestate(String srcParam, String bdclx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message getPropertyInfoQuery(String name, String zjh, int pageIndex, int pageSize) {
		Message msg = new Message();
		msg.setTotal(0);
		msg.setWhether(false);
		msg.setMsg("未匹配到任何权利人！");
		
		List<Map> qlrListMap = new ArrayList<Map>();
		//一、查询权利人数据
		String whereSQL = " ";
		Map<String,String> map = new HashMap<String, String>();
		if(!StringHelper.isEmpty(name)) {
			whereSQL += " and qlrmc = :name ";
			map.put("name", name);
		}
		if(!StringHelper.isEmpty(zjh)) {
			whereSQL += " and zjh = :zjh ";
			map.put("zjh", zjh);
		}
		String sql = "select distinct qlrmc,zjh,QLRMC||'<br>'||ZJH as qlr from bdck.BDCS_QLR_XZ qlr " 
				+ "where exists (select 1 from bdck.BDCS_QL_XZ where qllx not in ('19','20','21','22','23','98','99') " 
				+ " and qlid = qlr.qlid) "+ whereSQL;
		long total = baseCommonDao.getCountByFullSql(" from ("+sql+")", map);
		if (total >0) {
			List<Map> qlrList = baseCommonDao.getPageDataByFullSql(sql, map, pageIndex, pageSize);
			//二、查询权利人得到对应的房、地、林木、海洋等数量
			if (qlrList != null && qlrList.size() > 0) {
				String qlWhereSQL = " and (";
				for (Map qlr : qlrList) {
					String tj = "";
					if (StringHelper.isEmpty(qlr.get("QLRMC"))) {
						tj += "(C.QLRMC is null and ";
					} else {
						tj += "(C.QLRMC = '" + qlr.get("QLRMC").toString() + "' and ";
					}
					if (StringHelper.isEmpty(qlr.get("ZJH"))) {
						tj += " C.ZJH is null) or";
					} else {
						tj += " C.ZJH = '" + qlr.get("ZJH").toString() + "') or";
					}
					qlWhereSQL += tj;
				}
				qlWhereSQL = qlWhereSQL.substring(0, qlWhereSQL.length() - 2) + ")";
				List<Map> dyList = baseCommonDao.getDataListByFullSql(
						"select distinct a.BDCDYID,a.DJDYID,a.BDCDYLX,c.QLRMC||'<br>'||c.ZJH as qlr,b.BDCDYH "
								+ "from bdck.BDCS_DJDY_LS a, bdck.BDCS_QL_LS b, bdck.BDCS_QLR_LS c "
								+ "where a.DJDYID = b.DJDYID and b.QLID = c.QLID "
								+ "and qllx not in ('19','20','21','22','23','98','99') " + qlWhereSQL);
				//三、分组权利人的房地
				if (dyList != null && dyList.size() > 0) {
					Map qlr_list = new HashMap<String, List<Map>>();
					for (Map dy : dyList) {
						String qlr = dy.get("QLR").toString();
						if (qlr_list.containsKey(qlr)) {
							List<Map> dy_list = (List<Map>) qlr_list.get(qlr);
							dy_list.add(dy);
						} else {
							List<Map> dy_list = new ArrayList<Map>();
							dy_list.add(dy);
							qlr_list.put(qlr, dy_list);
						}
					}
					for (Object qlrObject : qlr_list.keySet().toArray()) {
						Map qlr_list_return = new HashMap<String, List<Map>>();
						qlr_list_return.put(qlrObject, qlr_list.get(qlrObject));
						qlrListMap.add(qlr_list_return);
					}
				}
			} 
			msg.setTotal(total);
			msg.setWhether(true);
			msg.setMsg(null);
			msg.setRows(qlrListMap);
		}
		return msg;
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	@Override
	public Map<String, Object> QLInfoFSQLInfo(String djdyid) {
		 Map<String, Object> listmap = new HashMap<String, Object>();
		if(!StringHelper.isEmpty(djdyid)){
			List<Map> dyqls = null;
			List<Map> dyfsqls = null;
			List<Map> qlrs = null;
			List<Map> dyqrs = null;
			List<Map> cfqls = null;
			List<Map> cffsqls = null;
			String bdcdyid = "";
			List<BDCS_QL_XZ> syqls = baseCommonDao.getDataList(BDCS_QL_XZ.class, "QLLX IN ('1','2','3','4','5','6','7','8','9') AND DJDYID='"+djdyid+"'");
			//抵押权
			dyfsqls = baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.BDCS_FSQL_XZ WHERE QLID IN (SELECT QLID FROM BDCK.BDCS_QL_XZ WHERE QLLX='23' AND DJDYID='"+djdyid+"')");
			dyqls = baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.BDCS_QL_XZ WHERE QLLX='23' AND DJDYID='"+djdyid+"'");
			dyqrs = baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.BDCS_QLR_XZ WHERE QLID IN (SELECT QLID FROM BDCK.BDCS_QL_XZ WHERE QLLX='23' AND DJDYID='"+djdyid+"')");
			//查封
			cffsqls = baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.BDCS_FSQL_XZ WHERE QLID IN (SELECT QLID FROM BDCK.BDCS_QL_XZ WHERE QLLX='99' AND DJLX='800' AND DJDYID='"+djdyid+"')");
			cfqls = baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.BDCS_QL_XZ WHERE QLLX='99' AND DJLX='800' AND DJDYID='"+djdyid+"'");
			Map map = null;
			ObjectMapper objectMapper = new ObjectMapper();
			String mapString = "";
			if(syqls !=null&&syqls.size()>0){
				//ql
				BDCS_QL_XZ bdcs_QL_XZ = syqls.get(0);
				try {
					mapString = objectMapper.writeValueAsString(bdcs_QL_XZ);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					map = objectMapper.readValue(mapString, Map.class);
				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				listmap.put("syq", map);
				//fsql
				BDCS_FSQL_XZ fsql = baseCommonDao.get(BDCS_FSQL_XZ.class, bdcs_QL_XZ.getFSQLID());
				try {
					mapString = objectMapper.writeValueAsString(fsql);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					map = objectMapper.readValue(mapString, Map.class);
				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				listmap.put("syqfsql", map);
				//syqlr
				qlrs = baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.BDCS_QLR_XZ WHERE QLID='"+bdcs_QL_XZ.getId()+"'");
				
				String _xmbh = bdcs_QL_XZ.getXMBH();
				if(!StringHelper.isEmpty(_xmbh)){
					String ywlxsql = "SELECT YWLSH,SUBSTR(PO.PRODEF_NAME,1,instr(PO.PRODEF_NAME, ',',1,1)-1) AS DJDL,SUBSTR(PO.PRODEF_NAME,instr(PO.PRODEF_NAME, ',',1,1)+1,length(PO.PRODEF_NAME)) AS DJXL "
							+ " FROM BDCK.BDCS_XMXX XMXX LEFT JOIN BDC_WORKFLOW.WFI_PROINST PO ON XMXX.PROJECT_ID = PO.FILE_NUMBER "
							+ " WHERE XMXX.XMBH='"+_xmbh+"'";
					List<Map> ywlxs = baseCommonDao.getDataListByFullSql(ywlxsql);
					if(ywlxs != null && ywlxs.size()>0){
						listmap.put("djdl", StringHelper.formatObject(ywlxs.get(0).get("DJDL")));
						listmap.put("djxl", StringHelper.formatObject(ywlxs.get(0).get("DJXL")));
						listmap.put("ywlsh", StringHelper.formatObject(ywlxs.get(0).get("YWLSH")));
					}
				}
			}
			//h
			List<BDCS_DJDY_XZ> dy = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "DJDYID='"+djdyid+"'");
			BDCS_DJDY_XZ dyh = null;
			RealUnit house = null;
			String lx = "";
			if(dy!=null&&dy.size()>0){
				dyh=dy.get(0);
				bdcdyid = dyh.getBDCDYID();
				lx = dyh.getBDCDYLX();
				house = UnitTools.loadUnit(BDCDYLX.initFrom(lx), DJDYLY.XZ, dyh.getBDCDYID());
				List<Map> tdxx = GetHInfo(bdcdyid, lx);
				if (tdxx.size() > 0 && tdxx != null) {
					listmap.put("tdxx", tdxx);
				}
			}
			
			try {
				mapString = objectMapper.writeValueAsString(house);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				map = objectMapper.readValue(mapString, Map.class);
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//查询单元限制表格
			List<BDCS_DYXZ> dyxz = baseCommonDao.getDataList(BDCS_DYXZ.class, "BDCDYID='"+bdcdyid+"'");
			long cfqztcount = baseCommonDao.getCountByFullSql("from bdck.bdcs_ql_xz where QLLX='99' AND DJLX='800' AND DJDYID='"+djdyid+"'");
			long yyqztcount = baseCommonDao.getCountByFullSql("from bdck.bdcs_ql_xz where QLLX='99' AND DJLX='600' AND DJDYID='"+djdyid+"'");
			long cfqztcount_qf = 0;
			long yyqztcount_qf = 0;
			List<YC_SC_H_XZ> REA = new ArrayList<YC_SC_H_XZ>();
			REA = baseCommonDao.getDataList(YC_SC_H_XZ.class, " SCBDCDYID='"+house.getId()+"'");
			if(REA != null && REA.size()>0){
				List<BDCS_DJDY_XZ> YCH = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, " BDCDYID ='"+REA.get(0).getYCBDCDYID()+"'");
				if(YCH != null && YCH.size()>0){
					cfqztcount_qf = baseCommonDao.getCountByFullSql("from bdck.bdcs_ql_xz where QLLX='99' AND DJLX='800' AND DJDYID='"+StringHelper.formatObject(YCH.get(0).getDJDYID())+"'");
					yyqztcount_qf = baseCommonDao.getCountByFullSql("from bdck.bdcs_ql_xz where QLLX='99' AND DJLX='600' AND DJDYID='"+StringHelper.formatObject(YCH.get(0).getDJDYID())+"'");
					dyfsqls.addAll(baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.BDCS_FSQL_XZ WHERE QLID IN (SELECT QLID FROM BDCK.BDCS_QL_XZ WHERE QLLX='23' AND DJDYID='"+StringHelper.formatObject(YCH.get(0).getDJDYID())+"')"));
					dyqls.addAll(baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.BDCS_QL_XZ WHERE QLLX='23' AND DJDYID='"+StringHelper.formatObject(YCH.get(0).getDJDYID())+"'"));
					dyqrs.addAll(baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.BDCS_QLR_XZ WHERE QLID IN (SELECT QLID FROM BDCK.BDCS_QL_XZ WHERE QLLX='23' AND DJDYID='"+StringHelper.formatObject(YCH.get(0).getDJDYID())+"')"));
					//查封
					cffsqls.addAll(baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.BDCS_FSQL_XZ WHERE QLID IN (SELECT QLID FROM BDCK.BDCS_QL_XZ WHERE QLLX='99' AND DJLX='800' AND DJDYID='"+StringHelper.formatObject(YCH.get(0).getDJDYID())+"')"));
					cfqls.addAll(baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.BDCS_QL_XZ WHERE QLLX='99' AND DJLX='800' AND DJDYID='"+StringHelper.formatObject(YCH.get(0).getDJDYID())+"'"));
				}
			}else {
				REA = baseCommonDao.getDataList(YC_SC_H_XZ.class, " YCBDCDYID='"+house.getId()+"'");
				if(REA != null && REA.size()>0){
					List<BDCS_DJDY_XZ> SCH = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, " BDCDYID ='"+REA.get(0).getSCBDCDYID()+"'");
					if(SCH != null && SCH.size()>0){
						cfqztcount_qf = baseCommonDao.getCountByFullSql("from bdck.bdcs_ql_xz where QLLX='99' AND DJLX='800' AND DJDYID='"+StringHelper.formatObject(SCH.get(0).getDJDYID())+"'");
						yyqztcount_qf = baseCommonDao.getCountByFullSql("from bdck.bdcs_ql_xz where QLLX='99' AND DJLX='600' AND DJDYID='"+StringHelper.formatObject(SCH.get(0).getDJDYID())+"'");
						dyfsqls.addAll(baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.BDCS_FSQL_XZ WHERE QLID IN (SELECT QLID FROM BDCK.BDCS_QL_XZ WHERE QLLX='23' AND DJDYID='"+StringHelper.formatObject(SCH.get(0).getDJDYID())+"')"));
						dyqls.addAll(baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.BDCS_QL_XZ WHERE QLLX='23' AND DJDYID='"+StringHelper.formatObject(SCH.get(0).getDJDYID())+"'"));
						dyqrs.addAll(baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.BDCS_QLR_XZ WHERE QLID IN (SELECT QLID FROM BDCK.BDCS_QL_XZ WHERE QLLX='23' AND DJDYID='"+StringHelper.formatObject(SCH.get(0).getDJDYID())+"')"));
						//查封
						cffsqls.addAll(baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.BDCS_FSQL_XZ WHERE QLID IN (SELECT QLID FROM BDCK.BDCS_QL_XZ WHERE QLLX='99' AND DJLX='800' AND DJDYID='"+StringHelper.formatObject(SCH.get(0).getDJDYID())+"')"));
						cfqls.addAll(baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.BDCS_QL_XZ WHERE QLLX='99' AND DJLX='800' AND DJDYID='"+StringHelper.formatObject(SCH.get(0).getDJDYID())+"'"));
					}
				}
			}
			listmap.put("xzzt", "房屋未限制");
			if(cfqztcount>0||yyqztcount>0||cfqztcount_qf>0||yyqztcount_qf>0){
				listmap.put("xzzt", "房屋已限制");
			}
			if(dyxz!=null&&dyxz.size()>0) {
				BDCS_DYXZ dyxz1 = dyxz.get(0);
				if(dyxz1.getYXBZ()!=null&&"1".equals(dyxz1.getYXBZ())){
					listmap.put("xzzt", "房屋已限制");
				}
			}
			listmap.putAll(GetDJDYStatus(lx,djdyid,house));
			
			if(!StringHelper.isEmpty(lx)
					&&(BDCDYLX.SHYQZD.equals(BDCDYLX.initFrom(lx))
					||BDCDYLX.SYQZD.equals(BDCDYLX.initFrom(lx)))){
				listmap.put("zd", map);
			}else{
				listmap.put("house", map);
			}
			listmap.put("syqrs", qlrs);
			
			listmap.put("dyqlrs", dyqrs);
			listmap.put("dyqls", dyqls);
			listmap.put("dyfsqls", dyfsqls);
			
			listmap.put("cfqls", cfqls);
			listmap.put("cffsqls", cffsqls);
		}
		listmap.put("CurrentUserName", Global.getCurrentUserName());
		return listmap;
	}
	
	protected Map<String, String> GetDJDYStatus_dy(String lx, String djdyid, RealUnit house) {
		List<BDCS_QL_XZ> dyqzt = null;
		List<BDCS_QL_XZ> cfqzt = null;
		List<BDCS_QL_XZ> yyqzt = null;
		Map<String,String> statusmap = new HashMap<String, String>();
		dyqzt = baseCommonDao.getDataList(BDCS_QL_XZ.class, "QLLX='23' AND DJDYID='"+djdyid+"'");
		cfqzt = baseCommonDao.getDataList(BDCS_QL_XZ.class, "QLLX='99' AND DJLX='800' AND DJDYID='"+djdyid+"'");
		yyqzt = baseCommonDao.getDataList(BDCS_QL_XZ.class, "QLLX='99' AND DJLX='600' AND DJDYID='"+djdyid+"'");
		
		if(dyqzt!=null&&dyqzt.size()>0){
			statusmap.put("dyzt", "已抵押");
		}else{
			statusmap.put("dyzt", "未抵押");
		}
		if(cfqzt!=null&&cfqzt.size()>0){
			statusmap.put("cfzt", "已查封");
		}else{
			statusmap.put("cfzt", "未查封");
		}
		if(yyqzt!=null&&yyqzt.size()>0){
			statusmap.put("yyzt", "有异议");
		}else{
			statusmap.put("yyzt", "无异议");
		}
		return statusmap;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Message queryHouseJYYT(Map<String, Object> params) {
		Message message = new Message();
		Integer page = 1; 
		if (params.get("page") != null) { 
			page = StringHelper.getInt(params.get("page"));
		}
		String querytype = StringHelper.formatObject(params.get("QUERYTYPE"));
		boolean iflike = false;
		if (querytype != null && querytype.toLowerCase().equals("true")) {
			iflike = true;
		}
		Integer rows = 10;
		if (params.get("rows") != null) {
			rows = StringHelper.getInt(params.get("rows"));
		}
		Map<String, String> queryvalues = new HashMap<String, String>();
		String sort =  StringHelper.formatObject(params.get("sort"));// 排序字段
		String order =  StringHelper.formatObject(params.get("order"));// 排序Order
		String qlrmc =  StringHelper.formatObject(params.get("QLRMC"));// 权利人名称
		String dyr =  StringHelper.formatObject(params.get("DYR"));// 抵押人名称
		String qlrzjh =  StringHelper.formatObject(params.get("ZJH"));// 权利人证件号
		String fwzl =  StringHelper.formatObject(params.get("ZL"));// 房屋坐落
		String bdcqzh =  StringHelper.formatObject(params.get("BDCQZH"));// 不动产权证号
		String fwbm =  StringHelper.formatObject(params.get("FWBM"));// 房屋编码
		String fh =  StringHelper.formatObject(params.get("FH"));// 房号
		String ywbh =  StringHelper.formatObject(params.get("YWBH"));// 业务编号
		String dyzt =  StringHelper.formatObject(params.get("DYZT"));// 抵押状态
		String cfzt =  StringHelper.formatObject(params.get("CFZT"));// 查封状态
		String cxzt =  StringHelper.formatObject(params.get("CXZT"));// 查询状态
		String yyzt =  StringHelper.formatObject(params.get("YYZT"));// 异议状态
		String fwzt= StringHelper.formatObject(params.get("FWZT"));
		String bdcdyh= StringHelper.formatObject(params.get("BDCDYH"));
		String bdcqzhxh= StringHelper.formatObject(params.get("BDCQZHXH"));
		String cfwh= StringHelper.formatObject(params.get("CFWH"));
		String djsj_q= StringHelper.formatObject(params.get("DJSJ_Q"));
		String djsj_z= StringHelper.formatObject(params.get("DJSJ_Z"));
		String dh= StringHelper.formatObject(params.get("DH"));//栋号
		String jgsx= StringHelper.formatObject(params.get("JGSX"));//结果筛选；"1"：全部显示，"2"：仅显示产权
		String searchstates= StringHelper.formatObject(params.get("SEARCHSTATES"));//区域筛选
		
		
		String BUYERNAME = StringHelper.formatObject(params.get("buyer_name"));
		String BUYERCODE = StringHelper.formatObject(params.get("buyer_code"));
		String COMPACT_CODE = StringHelper.formatObject(params.get("compact_code")); 
		
		//String res=getfwlogmsg(qlrmc,dyr,qlrzjh,fwzl,bdcqzh,fwbm,fh,ywbh,dyzt,cfzt,cxzt,fwzt,bdcdyh,bdcqzhxh,cfwh,querytype,"","");
		//YwLogUtil.addYwLog("房屋查询功能："+res, ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		queryvalues.put("DY.ZL", fwzl);
		queryvalues.put("DY.BDCDYH", bdcdyh);
		queryvalues.put("QLR.QLRMC", qlrmc);
		queryvalues.put("DYR.DYR", dyr);
		queryvalues.put("QLR.ZJH", qlrzjh);
		queryvalues.put("QL.BDCQZH", bdcqzh);
		queryvalues.put("QLR.BDCQZHXH", bdcqzhxh);
		queryvalues.put("FSQL.CFWH", cfwh);
		queryvalues.put("DY.FWBM", fwbm);
		queryvalues.put("DY.FH", fh);
		queryvalues.put("QL.YWH", ywbh);
		queryvalues.put("DYZT", dyzt);
		queryvalues.put("CFZT", cfzt);
		queryvalues.put("CXZT", cxzt);
		queryvalues.put("DJSJ_Q", djsj_q);
		queryvalues.put("DJSJ_Z", djsj_z);
		queryvalues.put("DY.ZRZH", dh);//栋号(自然幢号)
		queryvalues.put("YYZT", yyzt);
		queryvalues.put("JGSX", jgsx);//结果筛选；"1"：全部显示，"2"：仅显示产权
		queryvalues.put("SEARCHSTATES", searchstates);//区域筛选
		queryvalues.put("SWCDBZ", "cd");
		params.put("house_address", fwzl);
		params.put("fuzzyQuery", iflike);
		if ("3".equals(jgsx)) {//只显示备案  
			message = queryHouseCompactBA(params);
			return message;
		}else if ("4".equals(jgsx)){//综合查询，权利+合同备案
			
 			List<Map> resultList = new  ArrayList<Map>();
 			int total = 0; 
 			
			Message compatcQueryMessage = queryHouseCompactBA(params); 
			
			if ("true".equals(compatcQueryMessage.getSuccess())&&compatcQueryMessage.getTotal()>0) {
				List<Map> compactInfoList = (List<Map>) compatcQueryMessage.getRows();
				resultList.addAll(compactInfoList);
				total +=compatcQueryMessage.getTotal();
			}
			Message houseQueryMessage = queryHouseNew(queryvalues, page, rows, iflike, fwzt, sort, order);
			if (houseQueryMessage.getTotal()>0) {
				List<Map> queryInfoList = (List<Map>) houseQueryMessage.getRows(); 
				resultList.addAll(queryInfoList);
				total +=houseQueryMessage.getTotal();
			}
			message.setTotal(total);
 			message.setRows(resultList);
			message.setSuccess("true");
		}else {
 			message = queryHouseNew(queryvalues, page, rows, iflike, fwzt, sort, order);
		}
		
		return message;
	}
	private Message queryHouseNew(Map<String, String> queryvalues, Integer page, Integer rows, boolean iflike,
			String fwzt, String sort, String order) { 
		
		Message message = new Message(); 
//		StringBuffer selectSqlBuffer = new StringBuffer(); 
//		StringBuffer fromSqlBuffer = new StringBuffer(); 
//		StringBuffer countSqlBuffer = new StringBuffer(); 
//		StringBuffer whereSqlBuffer = new StringBuffer();  
		//结果筛选——是否只显示产权
		String jgsx=queryvalues.get("JGSX");
		queryvalues.remove("JGSX");
		if ("2".equals(jgsx)) {
			return queryHouseForUnit(queryvalues, page, rows,  iflike, fwzt,sort,order);
		} 
		long count = 0;
		List<Map> listresult = null;
		
		//查询现状层还是历史层
		String cxzt=queryvalues.get("CXZT");
		//去掉这个参数
		queryvalues.remove("CXZT");		
		
		//区域筛选——只查询有权限查询的区域
		String searchstates=queryvalues.get("SEARCHSTATES");
		queryvalues.remove("SEARCHSTATES");
		
		String fsqlcfwh=queryvalues.get("FSQL.CFWH");
		/* ===============1、先获取实体对应的表名=================== */
		
		String unitentityName = "BDCK.BDCS_H_XZ";
		if ("2".equals(cxzt)) {
			unitentityName = "BDCK.BDCS_H_LS";
		}

		/* ===============2、再获取表名+'_'+字段名=================== */
		String dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.FH,DY.GHYT,DY.FWBM,DY.SCJZMJ,DY.SCTNJZMJ,DY.SCFTJZMJ,DY.FTTDMJ,'031' AS BDCDYLX ,'现房' AS BDCDYLXMC ,DY.ZRZH,DY.SEARCHSTATE,DY.BZ";
		String qlfieldsname = "XM.YWLSH,QL.QLID,QL.BDCQZH,QL.QLLX,QL.DJSJ";
		String fsqlfieldsname = "FSQL.FSQLID,FSQL.CFWH";

		if (fwzt != null && fwzt.equals("2")) {
			unitentityName = "BDCK.BDCS_H_XZY";
			if ("2".equals(cxzt)) {
				unitentityName = "BDCK.BDCS_H_LSY";
			}
			dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.FH,DY.GHYT,DY.FWBM,DY.YCJZMJ AS SCJZMJ,DY.YCTNJZMJ AS SCTNJZMJ,DY.YCFTJZMJ AS SCFTJZMJ,DY.FTTDMJ,'032' AS BDCDYLX,'期房' AS BDCDYLXMC,DY.ZRZH,DY.SEARCHSTATE,DY.BZ ";
		}

		// 统一期现房 2015年10月28日
		if (fwzt != null && fwzt.equals("3")) {
			unitentityName = "(SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ,FTTDMJ,'031' AS BDCDYLX ,'现房' AS BDCDYLXMC, ZRZH, SEARCHSTATE, BZ  FROM BDCK.BDCS_H_XZ UNION ALL SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,FTTDMJ,'032' AS BDCDYLX ,'期房' AS BDCDYLXMC, ZRZH, SEARCHSTATE, BZ  FROM BDCK.BDCS_H_XZY)";
			
			if ("2".equals(cxzt)) {
				unitentityName = "(SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ,FTTDMJ,'031' AS BDCDYLX ,'现房' AS BDCDYLXMC, ZRZH, SEARCHSTATE, BZ FROM BDCK.BDCS_H_LS UNION ALL SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,FTTDMJ,'032' AS BDCDYLX ,'期房' AS BDCDYLXMC , ZRZH, SEARCHSTATE, BZ FROM BDCK.BDCS_H_LSY)";
		    
			}
			dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.FH,DY.GHYT,DY.JZMJ AS SCJZMJ,DY.TNJZMJ AS SCTNJZMJ,DY.FTJZMJ AS SCFTJZMJ,DY.FTTDMJ,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC,DY.ZRZH,DY.SEARCHSTATE,DY.BZ  ";
			
		}

		 /*===============3、构造查询语句=================== */
		 /* SELECT 字段部分 */
		StringBuilder builder2 = new StringBuilder();
		builder2.append("select ").append(dyfieldsname).append(",")
				.append(qlfieldsname);
		if(!StringHelper.isEmpty(fsqlcfwh)){
			builder2.append(",").append(fsqlfieldsname);
		}
		String selectstr = builder2.toString();

		/* FROM 后边的表语句 */
		StringBuilder builder = new StringBuilder();
		if ("2".equals(cxzt)) {
			builder.append(" from {0} DY")
					.append(" left join BDCK.BDCS_DJDY_LS DJDY on dy.bdcdyid=djdy.bdcdyid")
					.append(" left join BDCK.bdcs_ql_ls ql on ql.djdyid=djdy.djdyid  ")
					.append(" left join BDCK.BDCS_XMXX XM on XM.XMBH = QL.XMBH");
					
		}else{
			builder.append(" from {0} DY")
					.append(" left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid")
					.append(" left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid ")
					.append(" left join BDCK.BDCS_XMXX XM on XM.XMBH = QL.XMBH");
		}
		if(!StringHelper.isEmpty(fsqlcfwh)){
			builder.append(" left join BDCK.bdcs_fsql_xz fsql on ql.fsqlid=fsql.fsqlid  ");
		}
		
		 /* WHERE 条件语句 */
		StringBuilder builder3 = new StringBuilder();
		builder3.append(" where  ");
		StringBuilder qlrbuilder = new StringBuilder();
		StringBuilder dyrbuilder = new StringBuilder();
		boolean havecondition = false;
		boolean haveqlr = false;
		boolean havedyr = false;
		boolean havefdc = false;//加个房开查询条件
		for (Entry<String, String> ent : queryvalues.entrySet()) {
			String name = ent.getKey();
			String value = ent.getValue();
			if (!StringHelper.isEmpty(value)) {
				// 抵押状态和查封状态，为‘0’的时候表示不过滤，查询全部
				if ((name.equals("CFZT") && value.equals("0"))
						|| (name.equals("DYZT") && value.equals("0"))
							||name.equals("YYZT") && value.equals("0")) {
					continue;
				}
                if ((name.equals("SWCDBZ") && value.equals("cd"))) {
					continue;
				}
                if(name.equals("CDYT") && value.equals("0")){
					continue;
				}
				if(name.equals("FDCCX")){
					havefdc = true;
					continue;
				}
				if(name.equals("BH") || name.equals("BZ")){
					continue;
				}
				// 权利人，权利人的条件比较特殊，where里边又套了where
				if (name.startsWith("QLR.")) {
					if (haveqlr)
						qlrbuilder.append(" and ");
					if (iflike) {
						qlrbuilder.append(" instr(" + name + ",'" + value.trim()
								+ "')>0 ");
					} else {
						if (name.equals("QLR.ZJH") && value.replace(" ", "").length() == 18) {
							String oldCard = ConverIdCard.getOldIDCard(value);
							if (oldCard.length() != 15) {
								qlrbuilder.append(" " + name + "='" + value.replace(" ", "").toUpperCase()
										+ "' ");
							} else {
								qlrbuilder.append(" (" + name + "='" + value.replace(" ", "").toUpperCase()
										+ "' or ");
								qlrbuilder.append(" " + name + "='" + oldCard.replace(" ", "").toUpperCase()
										+ "') ");
							}
						} else {
							qlrbuilder.append(" " + name + "='" + value.replace(" ", "") + "' ");
						}
					}
					haveqlr = true;
					continue;
				}
				// 抵押人判断
				if (name.startsWith("DYR.")) {
					if (havedyr)
						dyrbuilder.append(" and ");
					if (iflike) {
						dyrbuilder.append(" instr(" + name + ",'" + value
								+ "')>0 ");
					} else {
						dyrbuilder.append(" " + name + "='" + value.replace(" ", "") + "' ");
					}
					havedyr = true;
					continue;
				}
				
				
				if (havecondition) {
					builder3.append(" and ");
				}

				// 抵押状态
				if (name.equals("DYZT")) {
					if (value.equals("1")) {
						if ("2".equals(cxzt)) {
							builder3.append(" djdy.djdyid NOT IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_LS DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
						}else{
							builder3.append(" djdy.djdyid NOT IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_XZ DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
						}
					} else {
						if ("2".equals(cxzt)) {
							builder3.append(" djdy.djdyid IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_LS DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
						}else{
							builder3.append(" djdy.djdyid IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_XZ DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
						}
					}
					havecondition = true;
					continue;
				}
				// 查封状态
				if (name.equals("CFZT")) {
					if (value.equals("1")) {
						if ("2".equals(cxzt)) {
							builder3.append("  djdy.DJDYID NOT IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_LS CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99') ");
						}else{
							builder3.append("  djdy.DJDYID NOT IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_XZ CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99') ");
						}
					} else {
						if ("2".equals(cxzt)) {
							builder3.append("  djdy.DJDYID IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_LS CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99' AND CF.DJLX='800') ");
						
						}else{
							builder3.append("  djdy.DJDYID IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_XZ CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99' AND CF.DJLX='800') ");
						
						}
					}
					havecondition = true;
					continue;
				}
				// 异议状态
				if (name.equals("YYZT")) {
					if (value.equals("1")) {
						if ("2".equals( cxzt)) {
							builder3.append("  djdy.DJDYID NOT IN (SELECT YY.DJDYID FROM BDCK.BDCS_QL_LS YY WHERE YY.DJDYID=DJDYID AND YY.djlx = '600') ");
						}else{
							builder3.append("  djdy.DJDYID NOT IN (SELECT YY.DJDYID FROM BDCK.BDCS_QL_XZ YY WHERE YY.DJDYID=DJDYID AND YY.djlx = '600') ");
						}
					} else {
						if ("2".equals(cxzt)) {
							builder3.append("  djdy.DJDYID IN (SELECT YY.DJDYID FROM BDCK.BDCS_QL_LS YY WHERE YY.DJDYID=DJDYID AND  YY.djlx = '600') ");
						}else{
							builder3.append("  djdy.DJDYID IN (SELECT YY.DJDYID FROM BDCK.BDCS_QL_XZ YY WHERE YY.DJDYID=DJDYID AND  YY.djlx = '600') ");
						}
					}
					havecondition = true;
					continue;
				}

				if (iflike) {
					if (name.contains("BDCQZH")) {
						// 验证不动产权证号是否为纯数字的
						boolean flag = StringHelper.isNumericByString(value);
						if (flag) {
							builder3.append(" instr(" + name + ",'" + value + "')>0 ");
						} else {
							builder3.append(" instr(upper(" + name + "),'" + value.toUpperCase() + "')>0 ");
						}
					}else if (name.contains("QL.YWH")) {
						builder3.append(" (QL.YWH LIKE '%"+value+"%' OR XM.YWLSH LIKE '%"+value+"%') ");
					}else if (name.equals("DJSJ_Q")) {
						builder3.append("  QL.DJSJ >=to_date('"+queryvalues.get("DJSJ_Q")+"','yyyy-mm-dd') ");
					}else if (name.equals("DJSJ_Z")) {
						builder3.append("   QL.DJSJ <=(to_date('"+queryvalues.get("DJSJ_Z")+"','yyyy-mm-dd')+1) ");
					}else{
					    builder3.append(" instr(" + name + ",'" + value + "')>0 ");
					}
					
					
					
				} else {
					/*
					 * 如果通过不动产权证查询，且是精确查询时,
					 * 其中如果不动产权证号是否纯数字的，直接在权利人表中通过BDCQZHXH条件查询，
					 * 否则从权利表中通过BDCQZH条件查询
					 */
					 
					if (name.contains("BDCQZH")) {
						String cdbz = queryvalues.get("SWCDBZ");//
						// 验证不动产权证号是否为纯数字的
						boolean flag = StringHelper.isNumericByString(value);
						if (flag) {
							// 判断是否已经有查询权利人的条件了
							if (haveqlr) {
								qlrbuilder.append(" and ");
							}
							qlrbuilder.append(" QLR.BDCQZHXH " + "='" + value
									+ "' ");
							haveqlr = true;
							continue;
						} else {
							if(cdbz != null && !cdbz.equals("") && cdbz.equals("cd")){
								builder3.append(" " + name + " like '%" + value + "%' ");
							}else{
								builder3.append(" " + name + " ='" + value + "' ");
							}
						}
					}else if (name.contains("QL.YWH")) {
						builder3.append(" (QL.YWH='"+value+"' OR XM.YWLSH='"+value+"') ");
					}else if (name.equals("DJSJ_Q")) {
						builder3.append("  QL.DJSJ >=to_date('"+queryvalues.get("DJSJ_Q")+"','yyyy-mm-dd') ");
					}else if (name.equals("DJSJ_Z")) {
						builder3.append("   QL.DJSJ <=(to_date('"+queryvalues.get("DJSJ_Z")+"','yyyy-mm-dd')+1) ");
					}else {
						builder3.append(" " + name + "='" + value.replace(" ", "") + "' ");
					}
				}
				havecondition = true;
			}
		}
		// 有权利人查询条件
		if (!StringHelper.isEmpty(qlrbuilder.toString())) {
			if (havecondition) {
				builder3.append(" and ");
			}
			if ("2".equals(cxzt)) {
				builder3.append(qlrbuilder);
			}else{
				builder3.append(qlrbuilder);
			}
			havecondition = true;
		}

		// 有抵押人查询条件
		if (!StringHelper.isEmpty(dyrbuilder.toString())) {
			if (havecondition) {
				builder3.append(" and ");
			}
			if ("2".equals(cxzt)) {
				builder3.append(" QL.QLID IN (SELECT QLID FROM BDCK.BDCS_FSQL_LS DYR WHERE "
						+ dyrbuilder.toString() + ")");
			}else{
				builder3.append(" QL.QLID IN (SELECT QLID FROM BDCK.BDCS_FSQL_XZ DYR WHERE "
						+ dyrbuilder.toString() + ")");
			}
			havecondition = true;
		}

		if (fwzt == null || !fwzt.equals("2")) {
			if (havecondition) {
				// builder3.append(" and ");
			}
			// builder3.append(" ql.qllx='4'");
		}
//		if ("2".equals(jgsx)) {
//			if (havecondition) {
//				 builder3.append(" and ");
//			}
//			 builder3.append(" ql.qllx in ('4','6','8')");
//			 havecondition=true;
//		}
		if (!StringHelper.isEmpty(searchstates)) {
			String[] states = searchstates.split("-");
			String state = "";
			for (int i = 0; i < states.length-1; i++) {
				state += "'"+states[i]+"',";
			}
				state += "'"+states[states.length-1]+"'";
			if (havecondition) {
				 builder3.append(" and ");
			}
			 builder3.append(" DY.SEARCHSTATE in (").append(state).append(")");
		}
		if (haveqlr||havedyr) {
			if("2".equals(cxzt)) {
				builder.append(" left join BDCK.BDCS_QLR_LS QLR on QLR.QLID=QL.QLID  ");
			}else {
				builder.append(" left join BDCK.BDCS_QLR_XZ QLR on QLR.QLID=QL.QLID  ");
			} 
		}
		String wherestr = builder3.toString();
		if (wherestr.trim().toUpperCase().endsWith("WHERE")) {
			wherestr = "";
		}
		String fromstr = builder.toString();
		fromstr = MessageFormat.format(fromstr, unitentityName); 
		String fromSql = " from (select ql.qlid " + fromstr + wherestr + ")";
		String fullSql = selectstr + fromstr + wherestr;
		/* 排序 条件语句 */
		
		if(!StringUtils.isEmpty(sort)&&!StringUtils.isEmpty(order))
		{
		if(sort.toUpperCase().equals("ZL"))
			sort="DY.ZL";
		if(sort.toUpperCase().equals("BDCDYH"))
			sort="DY.BDCDYH";
		/*if(sort.toUpperCase().equals("QLRMC"))
			sort="QLR.QLRMC";
		if(sort.toUpperCase().equals("ZJH"))
			sort="QLR.ZJH";*/
		
		if(sort.toUpperCase().equals("BDCQZH"))
			sort="QL.BDCQZH";
		if(sort.toUpperCase().equals("FH"))
			sort="DY.FH";
		if(sort.toUpperCase().equals("QLLX"))
			sort="QL.QLLX";
		/*if(sort.toUpperCase().equals("ZJHM"))
			sort="QLR.ZJHM";*/
		if(sort.toUpperCase().equals("GHYTNAME"))
			sort="DY.GHYT";
		if(sort.toUpperCase().equals("BDCDYID"))
			sort="DY.BDCDYID";
		if(sort.toUpperCase().equals("BDCDYLXMC"))
			sort="DY.BDCDYLX";
        if(sort.toUpperCase().equals("ZRZH"))
			sort="DY.ZRZH";		
			fullSql=fullSql+" ORDER BY "+sort+" "+order;
		}
		/* 为了提高效率，先不查询符合条件的记录总数，将来改为异步获取 */
		
		CommonDao dao = baseCommonDao;

		count = dao.getCountByFullSql(fromSql);
		if(havefdc){//查档不分页
			listresult = dao.getDataListByFullSql(fullSql); 
			addQLLX(listresult);
		}
		else{
			
			listresult = dao.getPageDataByFullSql(fullSql, page, rows);
			addRightsHolderInfo(listresult);
			String cdbz = queryvalues.get("SWCDBZ");//查档的一个参数
			

			addLimitStatus(listresult,cdbz);
			addDyCfDetails(listresult);
			isGlzd(listresult);
			//添加状态
			addBDCDYZT(listresult);
			//权利状态
			addQLZT(listresult);
			//添加注销状态
			addZXZT(listresult);
			addLimitFwStatusByZd(listresult);  //房屋查询需要新增土地的状态  就解开这句话

			addQLLX(listresult);
			addQXGL(listresult);
			
			ADDXMXX_SIZE(listresult);
		}

		// 格式化结果中的常量值
		for (Map map : listresult) {
			if (map.containsKey("GHYT")) {
				String value = StringHelper.formatObject(StringHelper
						.isEmpty(map.get("GHYT")) ? "" : map.get("GHYT"));
				String name = "";
				if (!StringHelper.isEmpty(value)) {
					name = ConstHelper.getNameByValue("FWYT", value);
				}
				map.put("GHYTname", name);
			}
			if (map.containsKey("DJSJ")) {
				map.put("DJSJ",StringHelper.FormatYmdhmsByDate(map.get("DJSJ")));
			}
		}

		message.setTotal(count);
		message.setRows(listresult);
		message.setMsg(Global.getCurrentUserName());
		
		return message;
	
	}

	@SuppressWarnings("unchecked")
	private Message queryHouseCompactBA(Map params) {
		Message message = new Message();
		List<Map<String,Object>> resultMapList = new ArrayList<Map<String,Object>>();
		Message queryMessage = jydjythDataSwapService.queryHouseCompact(params);
		if ("fale".equals(queryMessage.getSuccess())) {
			return queryMessage;
		}
		if (queryMessage.getTotal()>0) {
			List<Map> compactHouseList = (List<Map>) queryMessage.getRows();
			for (Map compactHouseMap : compactHouseList) {
				Map<String,Object> row = new HashMap<String,Object>();
				String BDCDYID = StringHelper.formatObject(compactHouseMap.get("BDCDYID"));// 不动产单元id 
				String BUILD_AREA = StringHelper.formatObject(compactHouseMap.get("BUILD_AREA"));// 建筑面积
				String BUILD_AREA_INSIDE = StringHelper.formatObject(compactHouseMap.get("BUILD_AREA_INSIDE"));// 套内建筑面积
				String BUILD_AREA_SHARE = StringHelper.formatObject(compactHouseMap.get("BUILD_AREA_SHARE"));// 分摊共用面积 
				String BUYER_CODE = StringHelper.formatObject(compactHouseMap.get("BUYER_CODE"));// 买受人证件号
				String BUYER_NAME = StringHelper.formatObject(compactHouseMap.get("BUYER_NAME"));// 买方姓名
				String CHHOUSE_ID = StringHelper.formatObject(compactHouseMap.get("CHHOUSE_ID"));
				String COMPACTRECORDS_DATE = StringHelper.formatObject(compactHouseMap.get("COMPACTRECORDS_DATE"));// 备案时间
				String COMPACTRECORDS_NAME = StringHelper.formatObject(compactHouseMap.get("COMPACTRECORDS_NAME"));// 备案人员
				String COMPACT_RECORDSTATE = StringHelper.formatObject(compactHouseMap.get("COMPACT_RECORDSTATE"));// 备案状态
				String COMPACT_CODE = StringHelper.formatObject(compactHouseMap.get("COMPACT_CODE"));// 合同号
				String COMPACT_ID = StringHelper.formatObject(compactHouseMap.get("COMPACT_ID"));// 合同ID
				String HOUSE_ADDRESS = StringHelper.formatObject(compactHouseMap.get("HOUSE_ADDRESS"));//房屋坐落
				String ROOM_NUMBER = StringHelper.formatObject(compactHouseMap.get("ROOM_NUMBER"));//房号
				String USE_FACT = StringHelper.formatObject(compactHouseMap.get("USE_FACT"));//房屋用途
				String BUILDING_NUMBER = StringHelper.formatObject(compactHouseMap.get("BUILDING_NUMBER"));// 幢号 
				String JY_HOUSE_ID = StringHelper.formatObject(compactHouseMap.get("HOUSE_ID"));// 交易房屋内码
				String PRICE_TOTAL = StringHelper.formatObject(compactHouseMap.get("PRICE_TOTAL"));// 合同金额
				String ONLINEAPPLY_ID = StringHelper.formatObject(compactHouseMap.get("ONLINEAPPLY_ID"));// 合同金额

				
				//交易的数据
				row.put("BUYER_CODE",BUYER_CODE);
				row.put("BUYER_NAME",BUYER_NAME);
				row.put("COMPACT_CODE",COMPACT_CODE);
				row.put("COMPACT_ID",COMPACT_ID); 
				row.put("PRICE_TOTAL",PRICE_TOTAL);  
				row.put("JY_HOUSE_ID",JY_HOUSE_ID);  
				row.put("ONLINEAPPLY_ID",ONLINEAPPLY_ID);  
				
				row.put("BDCDYID",BDCDYID);
				row.put("ZL",HOUSE_ADDRESS);
				row.put("SCJZMJ",BUILD_AREA);
				row.put("SCTNJZMJ",BUILD_AREA_INSIDE);
				row.put("SCFTJZMJ",BUILD_AREA_SHARE);
				row.put("ZRZH",BUILDING_NUMBER);
				row.put("XMXXS","");
				row.put("DJSJ",COMPACTRECORDS_DATE);
				row.put("FH",ROOM_NUMBER);
				row.put("GHYT",USE_FACT);
				row.put("GHYTname",ConstHelper.getNameByValue("FWYT", USE_FACT));
				row.put("QLZT","合同备案");
				row.put("QLLX","新建商品房网签备案");
				row.put("QLRMC",BUYER_NAME);
				row.put("ZJHM",BUYER_CODE);
				row.put("BDCQZH",COMPACT_CODE);
				row.put("dataType", "COMPACT");
				row.put("compactType", "presale");
				if(!StringUtils.isEmpty(BDCDYID)){
					String querybdcdyhSql = MessageFormat.format("select BDCDYID,BDCDYH,BDCDYLX,BDCDYLXMC FROM (SELECT BDCDYID,BDCDYH,''031'' AS BDCDYLX ,''现房'' AS BDCDYLXMC FROM BDCK.BDCS_H_XZ "
							+ "UNION ALL "
							+ "SELECT BDCDYID,BDCDYH,''032'' AS BDCDYLX,''期房'' AS BDCDYLXMC FROM BDCK.BDCS_H_XZY) DY "
							+ " WHERE DY.BDCDYID=''{0}'' ",
							BDCDYID);
					List<Map> resultHList =baseCommonDao.getDataListByFullSql(querybdcdyhSql);
					if (resultHList!=null&&resultHList.size()>0) {
						row.put("BDCDYZT", "现状");
						row.put("BDCDYLX", StringHelper.formatObject(resultHList.get(0).get("BDCDYLX")));
						row.put("BDCDYLXMC", StringHelper.formatObject(resultHList.get(0).get("BDCDYLXMC")));
						row.put("BDCDYH", StringHelper.formatObject(resultHList.get(0).get("BDCDYH")));
					}else {
						row.put("BDCDYZT", "历史");
						row.put("BDCDYH","");
					}
				}
				
				/////查询是否关联宗地///////////////////
				String bdcdylx = (String) row.get("BDCDYLX");
				String bdcdyid = (String) row.get("BDCDYID");
				if (!StringHelper.isEmpty(bdcdyid)
						&& !StringHelper.isEmpty(bdcdylx)) {						
					String fulSql = "";
					long count = 0;
					if(bdcdylx.equals("032")){
						fulSql = MessageFormat
								.format("from BDCK.YC_SC_H_XZ WHERE YCBDCDYID=''{0}'' ",
										bdcdyid);
						List<Map> list = baseCommonDao.getDataListByFullSql("SELECT * "+fulSql);
						if(list!=null){
							count = list.size();
							if (count>0) {
								String SCBDCDYID = list.get(0).get("SCBDCDYID").toString();
								row.put("QXFGL", "已关联现房");
								row.put("QXFGLID", SCBDCDYID);
								//打开登记簿还需要zl和bdcdyh
								List<Map> list_xf = baseCommonDao.getDataListByFullSql("SELECT ZL,BDCDYH FROM BDCK.BDCS_H_XZ WHERE BDCDYID='"+SCBDCDYID+"' ");
								if (list_xf != null && list_xf.size()>0) {
									row.put("QXFGL_ZL", list_xf.get(0).get("ZL").toString());
									row.put("QXFGL_BDCDYH", list_xf.get(0).get("BDCDYH").toString());
								}
							}else {
								row.put("QXFGL", "未关联现房");									
							}
						}	
					}else if (bdcdylx.equals("031")) {
						fulSql = MessageFormat
								.format("from BDCK.YC_SC_H_XZ WHERE  SCBDCDYID=''{0}'' ",
										bdcdyid);
						List<Map> list = baseCommonDao.getDataListByFullSql("SELECT * "+fulSql);
						if(list!=null){
							count = list.size();
							if (count>0) {
								row.put("QXFGL", "已关联期房");
								row.put("QXFGLID", list.get(0).get("YCBDCDYID"));
							}else {
								row.put("QXFGL", "未关联期房");
							}
						}
					}
				
				}else {
					row.put("QXFGL", "未关联");
				}
				//////////////
				resultMapList.add(row); 
			}//for end
		} 
 		message.setRows(resultMapList);
 		message.setSuccess("true");
 		message.setTotal(queryMessage.getTotal());
		return message;
	}

}