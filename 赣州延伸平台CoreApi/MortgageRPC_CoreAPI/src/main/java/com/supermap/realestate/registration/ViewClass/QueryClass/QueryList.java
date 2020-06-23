package com.supermap.realestate.registration.ViewClass.QueryClass;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.model.BDCS_CX_LOG;
import com.supermap.realestate.registration.model.BDCS_CX_PRINT_LOG;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.web.Message;

public class QueryList {

	private static CommonDao baseCommonDao = SuperSpringContext.getContext()
			.getBean(CommonDao.class);

	public static String addCxLog(String operateContent, String type,
			String xmbh, String cxlx) {
		String wherestr = " XMBH=''{0}'' AND CXLX=''{1}'' AND OPERATCONTEXT=''{2}''";
		wherestr = MessageFormat.format(wherestr, xmbh,cxlx,operateContent);
		List<BDCS_CX_LOG> loglist = baseCommonDao.getDataList(BDCS_CX_LOG.class, wherestr);
		if(loglist!=null&&loglist.size()>0){
			loglist.get(0).setOPERATETIME(new Date());
			baseCommonDao.update(loglist.get(0));
			baseCommonDao.flush();
			return loglist.get(0).getId();
		}
		BDCS_CX_LOG Log = new BDCS_CX_LOG();
		Log.setId((String) SuperHelper.GeneratePrimaryKey());
		Log.setOPERATETIME(new Date());
		User user = Global.getCurrentUserInfo();
		if (user != null) {
			Log.setOPERATEUSER(user.getUserName());
		}
		Log.setXMBH(xmbh);
		Log.setCXLX(cxlx);
		Log.setOPERATETYPE(type);
		Log.setOPERATCONTEXT(operateContent);
		baseCommonDao.save(Log);
		baseCommonDao.flush();
		return Log.getId();
	}

	public static void addCxPrintLog(String operateContent, String type,
			String xmbh, String cxlx) {
		BDCS_CX_PRINT_LOG Log = new BDCS_CX_PRINT_LOG();
		Log.setId((String) SuperHelper.GeneratePrimaryKey());
		Log.setOPERATETIME(new Date());
		User user = Global.getCurrentUserInfo();
		if (user != null) {
			Log.setOPERATEUSER(user.getUserName());
		}
		Log.setXMBH(xmbh);
		Log.setCXLX(cxlx);
		Log.setOPERATETYPE(type);
		Log.setOPERATCONTEXT(operateContent);
		baseCommonDao.save(Log);
		baseCommonDao.flush();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Message QueryHouseList(String qlrmc, String zjh,
			String bdcqzh, Integer page, Integer rows, String type) {
		String SelectSql = " SELECT QL.QLLX,QL.DJLX,QL.DJDYID,QL.QLID,QL.DJYY,QL.YWH,H.* ";
		String FromSql = "  FROM "
				+ " (SELECT H.ZRZH,H.FH,H.FWJG,H.SZC,H.ZCS,H.SCJZMJ AS JZMJ,H.GHYT,H.ZL,H.BDCDYID FROM BDCK.BDCS_H_XZ H "
				+ " UNION ALL SELECT H.ZRZH,H.FH,H.FWJG,H.SZC,H.ZCS,H.YCJZMJ AS JZMJ,H.GHYT,H.ZL,H.BDCDYID FROM BDCK.BDCS_H_XZY H ";

		String WhereSql = " WHERE EXISTS (SELECT 1 FROM BDCK.BDCS_QLR_XZ QLR WHERE QLR.QLID=QL.QLID  ";

		FromSql += " WHERE NOT EXISTS(SELECT 1 FROM BDCK.YC_SC_H_XZ YX WHERE YX.YCBDCDYID=H.BDCDYID)";
		FromSql += " ) H"
				+ "  LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON DJDY.BDCDYID=H.BDCDYID LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=DJDY.DJDYID  ";
		if(!"---".equals(qlrmc)){
			WhereSql += " AND QLR.QLRMC ='" + qlrmc + "' ";
		}
		if(!"---".equals(zjh)){
			WhereSql += " AND QLR.ZJH='" + zjh + "' ";
		}
		if(!"---".equals(bdcqzh)){
			WhereSql += " AND QLR.BDCQZH='" + bdcqzh + "' ";
		}
		WhereSql += ")";
	
		// if("dy".equals(type)){
		// WhereSql +=
		// " AND DJDY.DJDYID IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_XZ DYQ WHERE DYQ.DJDYID=DJDY.DJDYID AND DYQ.QLLX='23')";
		// }
		String fulSql = SelectSql + FromSql + WhereSql;

		List<Map> list = new ArrayList<Map>();
		long count = 0;
		if ("fc".equals(type)) {
			list = baseCommonDao.getPageDataByFullSql(fulSql, page, rows);
			count = baseCommonDao.getCountByFullSql(FromSql + WhereSql);
		}
		Message msg = new Message();
		if ("dy".equals(type)) {
			List<Map> IDlist = baseCommonDao
					.getDataListByFullSql(" SELECT QL.DJDYID " + FromSql
							+ WhereSql);
			if (IDlist.size() == 0)
				return msg;
			String ids = "";
			for (Map map : IDlist) {
				ids += "'" + map.get("DJDYID") + "',";
			}
			ids = ids.substring(0, ids.length() - 1);
			String sql = SelectSql + FromSql
					+ " WHERE QL.QLLX='23' AND DJDY.DJDYID IN (" + ids + ")";
			count = baseCommonDao.getCountByFullSql(FromSql
					+ " WHERE QL.QLLX='23' AND DJDY.DJDYID IN (" + ids + ")");
			list = baseCommonDao.getPageDataByFullSql(sql, page, rows);
		}
		if (list.size() == 0){
			msg.setRows(list);
			return msg;
		}
		for (Map map : list) {
			String qlid = StringHelper.formatObject(map.get("QLID"));
			ArrayList<String> qlrxms = new ArrayList<String>();
			ArrayList<String> zjhs = new ArrayList<String>();
			if("fc".equals(type)&&!StringHelper.isEmpty(qlid)){
				List<RightsHolder> holders = RightsHolderTools.loadRightsHolders(DJDYLY.XZ, qlid);
				if(holders!=null){
					for (RightsHolder rightsHolder : holders) {
						qlrxms.add(rightsHolder.getQLRMC());
						zjhs.add(rightsHolder.getZJH());
					}
				}
				String project_id = StringHelper.formatObject(map.get("YWH"));
				String Sql = "SELECT PROISNT.PRODEF_NAME FROM BDC_WORKFLOW.WFI_PROINST PROISNT WHERE PROISNT.FILE_NUMBER = ''{0}''";
				Sql = MessageFormat.format(Sql, project_id);
				List<Map> workflownames = baseCommonDao.getDataListByFullSql(Sql);
				map.put("WORKFLOWNAME","");
				if(workflownames!=null&&!workflownames.isEmpty()){
					String workflowname = "";
					if(!StringHelper.isEmpty(StringHelper.formatObject(workflownames.get(0).get("PRODEF_NAME")))){
						workflowname = StringHelper.formatObject(workflownames.get(0).get("PRODEF_NAME")).replaceAll(",", "-");
					}
					map.put("WORKFLOWNAME",workflowname);
				}
			}
			if("dy".equals(type)&&!StringHelper.isEmpty(qlid)){
				SubRights subRight = RightsTools.loadSubRightsByRightsID(DJDYLY.XZ, qlid);
				if(subRight!=null){
					BDCS_FSQL_XZ sub = (BDCS_FSQL_XZ) subRight;
					map.put("DYWLX",StringHelper.formatObject(sub.getDYWLXName()));
				}
			}
			map.put("QLRXMS",StringHelper.formatList(qlrxms));
			map.put("ZJHS",StringHelper.formatList(zjhs));
			
			if (!StringHelper.isEmpty(map.get("FWJG"))) {
				String jg = ConstHelper.getNameByValue("FWJG", map.get("FWJG")
						.toString());
				map.put("FWJG", jg);
			}
			if (!StringHelper.isEmpty(map.get("GHYT"))) {
				String yt = ConstHelper.getNameByValue("FWYT", map.get("GHYT")
						.toString());
				map.put("GHYT", yt);
			}
			String zt = "";
			long isdy = baseCommonDao
					.getCountByFullSql(" FROM BDCK.BDCS_QL_XZ WHERE DJDYID='"
							+ map.get("DJDYID") + "' AND QLLX='23' ");
			if (isdy > 0) {
				zt = "已抵押、";

			} else {
				zt = "未抵押、";
			}
			long iscf = baseCommonDao
					.getCountByFullSql(" FROM BDCK.BDCS_QL_XZ WHERE DJDYID='"
							+ map.get("DJDYID") + "' AND DJLX='800' ");
			if (iscf > 0) {
				zt += "已查封";
			} else {
				zt += "未查封";
			}
			map.put("FWZT", zt);
		}
		msg.setRows(list);
		msg.setTotal(count);
		return msg;
	}

	public static Message GetQuery(HttpServletRequest request)
			throws IOException {
		// TODO Auto-generated method stub

		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String qlrmc = RequestHelper.getParam(request, "name");
		String zjh = RequestHelper.getParam(request, "zjh");
		String bdcqzh = RequestHelper.getParam(request, "qzh");
		String xmbh = RequestHelper.getParam(request, "xmbh");
		String cxlx = RequestHelper.getParam(request, "cxlx");
		String operateContent = "";
		String type = "";
		if ("---".equals(bdcqzh)) {
			operateContent += qlrmc + "-" + zjh;
			type += "权利人名称+证件号";
		} else {
			operateContent += bdcqzh;
			type += "不动产权证号";
		}
		String logid = addCxLog(operateContent, type, xmbh, cxlx);
		return QueryHouseList(qlrmc, zjh, bdcqzh, page, rows, cxlx);
	}

	public static List<Map<String, String>> GetHead(HttpServletRequest request)
			throws UnsupportedEncodingException {
		String type = RequestHelper.getParam(request, "type");
		String xmbh = RequestHelper.getParam(request, "xmbh");
		String cxlx = RequestHelper.getParam(request, "cxlx");
		if (!StringHelper.isEmpty(RequestHelper.getParam(request, "text"))) {
			addCxPrintLog(RequestHelper.getParam(request, "text"), type, xmbh,
					cxlx);
			return null;
		}
		String id = RequestHelper.getParam(request, "ids");
		String[] ids = id.split(",");
		if ("fczm".equals(type)) {
			return GetFCHead(ids);
		} else if ("dyzm".equals(type)) {
			return GetDYHead(ids);
		} else
			return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static List<Map<String, String>> GetDYHead(String[] djdyids) {
		String SelectSql = "  ";
		String FromSql = "  ";
		String WhereSql = "  ";
		String ids = "";
		for (String string : djdyids) {
			ids += "'" + string + "',";
		}
		ids = ids.substring(0, ids.length() - 1);
		SelectSql = " SELECT QL.BDCQZH ";
		FromSql += " FROM BDCK.BDCS_QL_XZ QL ";
		WhereSql += " WHERE QL.DJDYID IN ( " + ids
				+ ") AND QL.QLLX='23' GROUP BY QL.BDCQZH";
		List<Map> zmhlist = baseCommonDao.getDataListByFullSql(SelectSql
				+ FromSql + WhereSql);
		List<Map<String, String>> Head = new ArrayList<Map<String, String>>();
		if (zmhlist.isEmpty()) {
			Map<String, String> err = new HashMap();
			err.put("err", "他项证号为空");
			Head.add(err);
			return Head;
		}
		for (Map map : zmhlist) {
			// HEAD
			String sql1 = "SELECT QLR.QLRMC AS DYQR ,FSQL.DYR,QL.DJDYID,QL.DJLX,"
					+ "FSQL.DYFS,"
					+ "  "
					+ " (CASE WHEN FSQL.DYFS=1 THEN FSQL.BDBZZQSE ELSE FSQL.ZGZQSE END) AS ZQSE,"
					+ " QL.QLJSSJ,QL.QLQSSJ, QL.DJSJ"
					+ "  "
					+ " FROM BDCK.BDCS_QL_XZ QL "
					+ " LEFT JOIN BDCK.BDCS_QLR_XZ QLR ON QL.QLID=QLR.QLID"
					+ " LEFT JOIN BDCK.BDCS_FSQL_XZ FSQL ON FSQL.QLID=QL.QLID "
					+ " WHERE QL.BDCQZH='"
					+ map.get("BDCQZH")
					+ "' AND QL.DJDYID IN (" + ids + ") AND QL.QLLX='23'";
			List<Map> part1 = baseCommonDao.getDataListByFullSql(sql1);
			Map head = part1.get(0);
			Map<String, String> newmap1 = new HashMap();
			// 字典转换
			String dyfsList = ConstHelper.getNameByValue("DYFS", part1.get(0)
					.get("DYFS") + "");
			newmap1.put("DYFS", dyfsList);
			// 组装
			String dyqrList = head.get("DYQR") + "";
			newmap1.put("DYQR", dyqrList);
			String dyrList = head.get("DYR") + "";
			newmap1.put("DYR", dyrList);
			String zqselist = head.get("ZQSE") + "";
			newmap1.put("ZQSE", zqselist);
			String qssjlist = StringHelper.FormatByDatetime(head.get("QLJSSJ"));
			newmap1.put("QLJSSJ", qssjlist);
			String jssjlist = StringHelper.FormatByDatetime(head.get("QLQSSJ"));
			newmap1.put("QLQSSJ", jssjlist);
			String djsjlist = StringHelper.FormatByDatetime(head.get("DJSJ"));
			newmap1.put("DJSJ", djsjlist);
			String bdczmhlist = map.get("BDCQZH") + "";
			newmap1.put("ZMH", bdczmhlist);
			// String zt=map.get("DJLX")+"";
			// String dyztlist=zt=="800"?"已抵押、已查封":"已抵押、未查封";
			// newmap1.put("DYZT",dyztlist);
			List<String> newmap2 = new ArrayList<String>();
			for (Map id : part1) {
				newmap2.add(id.get("DJDYID") + "");
			}
			HashSet<String> hs = new HashSet<String>(newmap2);
			String filter_ids = "";
			for (String map2 : hs) {
				filter_ids += "'" + map2 + "',";
			}
			filter_ids = filter_ids.substring(0, filter_ids.length() - 1);
			String sql2 = "SELECT QL.BDCQZH" + " FROM BDCK.BDCS_QL_XZ QL "
					+ " WHERE QL.DJDYID IN (" + filter_ids
					+ ") AND QL.QLLX='4'";
			List<Map> part2 = baseCommonDao.getDataListByFullSql(sql2);
			newmap2 = new ArrayList<String>();
			long count = 0;
			for (Map id : part2) {
				newmap2.add(id.get("BDCQZH") + "");
			}
			hs = new HashSet<String>(newmap2);
			String qzhList = "";
			for (String map2 : hs) {
				if (count < 4)
					qzhList += map2 + ",";
				else
					break;
				count++;
			}
			if (!StringHelper.isEmpty(qzhList))
				qzhList = qzhList.substring(0, qzhList.length() - 1);
			if (count > 4)
				qzhList += " 等" + hs.size() + "个";
			newmap1.put("QZH", qzhList);
			String sql3 = " SELECT ZL FROM (SELECT ZL,BDCDYID FROM BDCK.BDCS_H_XZ UNION ALL SELECT ZL,BDCDYID FROM BDCK.BDCS_H_XZY) H "
					+ " LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON DJDY.BDCDYID=H.BDCDYID WHERE DJDY.DJDYID IN ("
					+ filter_ids + ")";
			List<Map> part3 = baseCommonDao.getDataListByFullSql(sql3);
			String zllist = "";
			newmap2 = new ArrayList<String>();
			for (Map map2 : part3) {
				newmap2.add(map2.get("ZL") + "");
			}
			hs = new HashSet<String>(newmap2);
			count = 0;
			for (String map2 : hs) {
				if (count < 4)
					zllist += map2 + ",";
				else
					break;
				count++;
			}
			zllist = zllist.substring(0, zllist.length() - 1);
			if (count > 4)
				zllist += " 等" + hs.size() + "户";
			newmap1.put("zl", zllist);
			newmap1.put("filter_ids", filter_ids);
			/* jieshu */
			Head.add(newmap1);
		}
		return Head;
	}

	@SuppressWarnings("rawtypes")
	private static List<Map<String, String>> GetFCHead(String[] qlids) {

		List<Map> map = new ArrayList<Map>();
		Map<String, String> head = new HashMap<String, String>();
		// QLRMC ZJH ZJZL
		String ids = "";
		for (String string : qlids) {
			ids += "'" + string + "',";
		}
		ids = ids.substring(0, ids.length() - 1);
		if (ids != null) {
			String sql = " SELECT QLR.QLRMC,QLR.ZJH,QLR.ZJZL"
					+ " FROM BDCK.BDCS_QLR_XZ QLR WHERE QLR.QLID IN (" + ids
					+ ") " + " GROUP BY QLR.QLRMC,QLR.ZJH,QLR.ZJZL";
			map.addAll(baseCommonDao.getDataListByFullSql(sql));
		}
		HashSet<Map> QLRMC_ZJH_ZJZL = new HashSet<Map>(map);
		String qlrmcList = "";
		String zjhList = "";
		String zjzlcList = "";
		int count = 0;
		for (Map map2 : QLRMC_ZJH_ZJZL) {
			if (count < 4) {
				qlrmcList += map2.get("QLRMC") + ",";
				count++;
			} else {
				qlrmcList = qlrmcList.substring(0, qlrmcList.length() - 1);
				qlrmcList += "  等" + (QLRMC_ZJH_ZJZL.size()) + "人,";
				break;
			}

		}
		qlrmcList = qlrmcList.substring(0, qlrmcList.length() - 1);
		head.put("qlrmcList", qlrmcList);
		count = 0;
		for (Map map2 : QLRMC_ZJH_ZJZL) {
			if (count < 4) {
				zjhList += map2.get("ZJH") + ",";
				count++;
			} else {
				zjhList = zjhList.substring(0, zjhList.length() - 1);
				zjhList += "  等" + (QLRMC_ZJH_ZJZL.size()) + "个,";
				break;
			}

		}
		zjhList = zjhList.substring(0, zjhList.length() - 1);
		head.put("zjhList", zjhList);
		count = 0;
		for (Map map2 : QLRMC_ZJH_ZJZL) {
			if (count < 4) {
				zjzlcList += ConstHelper.getNameByValue("ZJLX",
						map2.get("ZJZL") + "")
						+ ",";
				count++;
			} else {
				zjzlcList = zjzlcList.substring(0, zjzlcList.length() - 1);
				zjzlcList += "  等" + (QLRMC_ZJH_ZJZL.size()) + "个,";
				break;
			}

		}
		zjzlcList = zjzlcList.substring(0, zjzlcList.length() - 1);
		head.put("zjzlList", zjzlcList);
		// GYFS
		map.clear();
		if (ids != null) {
			String sql = " SELECT QLR.GYFS, QLR.QLBL"
					+ " FROM BDCK.BDCS_QLR_XZ QLR WHERE QLR.QLID IN(" + ids
					+ ") " + " GROUP BY QLR.GYFS, QLR.QLBL";
			map.addAll(baseCommonDao.getDataListByFullSql(sql));
		}
		HashSet<Map> GYFS = new HashSet<Map>(map);
		String gyfsList = "";
		count = 0;
		for (Map map2 : GYFS) {
			if (count < 4) {
				gyfsList += ConstHelper.getNameByValue("GYFS", map2.get("GYFS")+"");
				if("2".equals(map2.get("GYFS"))){
					gyfsList +="("+map2.get("QLBL")+")";
				}
				gyfsList+= ",";
				count++;
			} else {
				gyfsList = gyfsList.substring(0, gyfsList.length() - 1);
				gyfsList += "  等" + (GYFS.size()) + "个,";
				break;
			}
		}
		gyfsList = gyfsList.substring(0, gyfsList.length() - 1);
		head.put("gyfsList", gyfsList);
		// FWXX
		map.clear();
		if (ids != null) {
			String sql = " SELECT QL.BDCQZH" + " FROM BDCK.BDCS_QL_XZ QL "
					+ " WHERE QL.QLID IN (" + ids + ") GROUP BY QL.BDCQZH";
			map.addAll(baseCommonDao.getDataListByFullSql(sql));
		}
		HashSet<Map> BDCQZH = new HashSet<Map>(map);
		// 过滤 BDCQZH FWCB FWXZ FWZL
		count = 0;
		String qlhList = "";
		for (Map maps : BDCQZH) {
			if (count < 4) {
				qlhList += maps.get("BDCQZH") + ",";
				count++;
			} else {
				qlhList = qlhList.substring(0, qlhList.length() - 1);
				qlhList += "  等" + (BDCQZH.size()) + "个,";
				break;
			}

		}
		qlhList = qlhList.substring(0, qlhList.length() - 1);
		head.put("qlhList", qlhList);
		map.clear();
		if (ids != null) {
			String sql = " SELECT QL.DJSJ" + " FROM BDCK.BDCS_QL_XZ QL "
					+ " WHERE QL.QLID IN (" + ids + ") GROUP BY QL.DJSJ";
			map.addAll(baseCommonDao.getDataListByFullSql(sql));
		}
		HashSet<Map> DJSJ = new HashSet<Map>(map);
		count = 0;
		String djsjList = "";
		for (Map maps : DJSJ) {
			if (count < 4) {
				djsjList += StringHelper.FormatByDatetime(maps.get("DJSJ"))
						+ ",";
				count++;
			} else {
				djsjList = djsjList.substring(0, djsjList.length() - 1);
				djsjList += "  等" + (DJSJ.size()) + "个,";
				break;
			}

		}
		djsjList = djsjList.substring(0, djsjList.length() - 1);
		head.put("djsjList", djsjList);
		map.clear();
		if (ids != null) {
			String sql = " SELECT H.FWCB"
					+ " FROM (SELECT FWCB,ZL,BDCDYID FROM BDCK.BDCS_H_XZ UNION ALL SELECT FWCB,ZL,BDCDYID FROM BDCK.BDCS_H_XZY) H "
					+ " LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON H.BDCDYID=DJDY.BDCDYID "
					+ " LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=DJDY.DJDYID "
					+ " WHERE QL.QLID IN(" + ids + ") GROUP BY H.FWCB";
			map.addAll(baseCommonDao.getDataListByFullSql(sql));
		}
		HashSet<Map> FWCB = new HashSet<Map>(map);
		String fwcbList = "";
		count = 0;
		for (Map maps : FWCB) {
			if (count < 4) {
				fwcbList += ConstHelper.getNameByValue("FWCB", maps.get("FWCB")
						+ "")
						+ ",";
				count++;
			} else {
				fwcbList = fwcbList.substring(0, fwcbList.length() - 1);
				fwcbList += "  等" + (FWCB.size()) + "个,";
				break;
			}

		}
		fwcbList = fwcbList.substring(0, fwcbList.length() - 1);
		head.put("fwcbList", fwcbList);
		map.clear();
		if (ids != null) {
			String sql = " SELECT H.FWXZ"
					+ " FROM (SELECT FWXZ,ZL,BDCDYID FROM BDCK.BDCS_H_XZ UNION ALL SELECT FWXZ,ZL,BDCDYID FROM BDCK.BDCS_H_XZY) H "
					+ " LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON H.BDCDYID=DJDY.BDCDYID "
					+ " LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=DJDY.DJDYID "
					+ " WHERE QL.QLID IN (" + ids + ") GROUP BY H.FWXZ";
			map.addAll(baseCommonDao.getDataListByFullSql(sql));
		}
		HashSet<Map> FWXZ = new HashSet<Map>(map);
		String fwxzList = "";
		count = 0;
		for (Map maps : FWXZ) {
			if (count < 4) {
				fwxzList += ConstHelper.getNameByValue("FWXZ", maps.get("FWXZ")
						+ "")
						+ ",";
				count++;
			} else {
				fwxzList = fwxzList.substring(0, fwxzList.length() - 1);
				fwxzList += "  等" + (FWXZ.size()) + "个,";
				break;
			}

		}
		fwxzList = fwxzList.substring(0, fwxzList.length() - 1);
		head.put("fwxzList", fwxzList);
		map.clear();
		if (ids != null) {
			String sql = " SELECT H.ZL"
					+ " FROM (SELECT FWXZ,ZL,BDCDYID FROM BDCK.BDCS_H_XZ UNION ALL SELECT FWXZ,ZL,BDCDYID FROM BDCK.BDCS_H_XZY) H "
					+ " LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON H.BDCDYID=DJDY.BDCDYID "
					+ " LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=DJDY.DJDYID "
					+ " WHERE QL.QLID IN(" + ids + ") GROUP BY H.ZL";
			map.addAll(baseCommonDao.getDataListByFullSql(sql));
		}
		HashSet<Map> ZL = new HashSet<Map>(map);
		count = 0;
		String zlList = "";
		for (Map maps : ZL) {
			if (count < 1) {
				zlList += maps.get("ZL") + ",";
				count++;
			} else {
				zlList = zlList.substring(0, zlList.length() - 1);
				zlList += "  等" + (ZL.size()) + "户,";
				break;
			}
		}
		zlList = zlList.substring(0, zlList.length() - 1);
		head.put("fwzlList", zlList);
		List<Map<String, String>> Head = new ArrayList<Map<String, String>>();
		Head.add(head);
		return Head;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Message GetCxTree(String xmbh, String cxlx) {
		// TODO Auto-generated method stub
		List<BDCS_CX_LOG> cxList = baseCommonDao.getDataList(BDCS_CX_LOG.class,
				" CXLX='" + cxlx + "' AND " + " XMBH='" + xmbh
						+ "' ORDER BY OPERATETIME DESC");
		List<Map> List = new ArrayList();
		HashSet<String> hs = new HashSet<String>();
		hs.add("---");
		for (BDCS_CX_LOG cx : cxList) {
			Map map = new HashMap<String, String>();
			if ("不动产权证号".equals(cx.getOPERATETYPE())) {
				map.put("id", cx.getId());// 不动产权证号
				map.put("name", "");
				map.put("zjh", "");
				map.put("qzh", cx.getOPERATCONTEXT());
				map.put("text", cx.getOPERATCONTEXT());
				if (hs.add(cx.getOPERATCONTEXT())) {
					List.add(map);
				}
			} else {
				String[] valuse = cx.getOPERATCONTEXT().split("-");
				if (valuse.length == 2) {
					map.put("id", cx.getId());// 不动产权证号
					map.put("name", valuse[0]);
					map.put("zjh", valuse[1]);
					map.put("qzh", "");
					map.put("text", cx.getOPERATCONTEXT());
					if (hs.add(cx.getOPERATCONTEXT())) {
						List.add(map);
					}
				}else if(valuse.length == 1){
					map.put("id", cx.getId());// 不动产权证号
					map.put("name", valuse[0]);
					map.put("zjh", "");
					map.put("qzh", "");
					map.put("text", cx.getOPERATCONTEXT());
					if (hs.add(cx.getOPERATCONTEXT())) {
						List.add(map);
					}
				}
			}
		}
		if("wfcx".equals(cxlx)){
			for (BDCS_CX_LOG cx : cxList) {
				String[] valuse = cx.getOPERATCONTEXT().split("---");
				Map map = new HashMap<String, String>();
				map.put("name", valuse[0]);
				if(valuse.length<2)
					map.put("zjh", "");
				else
					map.put("zjh", valuse[1]);
				map.put("text", cx.getOPERATCONTEXT());
				map.put("dylsh", cx.getLSH());
				if (hs.add(cx.getOPERATCONTEXT())) {
					List.add(map);
				}
			}
		}
		Message message = new Message();
		message.setRows(List);
		message.setTotal(List.size());
		return message;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Message GetPrnTree(String xmbh, String cxlx) {
		// TODO Auto-generated method stub
		List<BDCS_CX_PRINT_LOG> cxList = baseCommonDao.getDataList(
				BDCS_CX_PRINT_LOG.class, " CXLX='" + cxlx + "' AND "
						+ " XMBH='" + xmbh + "' ORDER BY OPERATETIME DESC");
		List<Map> List = new ArrayList();
		for (BDCS_CX_PRINT_LOG cx : cxList) {
			Map map = new HashMap<String, String>();
			String date = StringHelper.FormatYmdhmsByDate(cx.getOPERATETIME());
			map.put("id", cx.getId());
			map.put("type", cx.getOPERATETYPE());
			map.put("values", cx.getOPERATCONTEXT());
			map.put("text", date + "---" + cx.getOPERATETYPE());
			List.add(map);
		}
		Message message = new Message();
		message.setRows(List);
		message.setTotal(List.size());
		return message;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Message QueryPrint(HttpServletRequest request) {

		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String ids = "";
		if (request.getParameter("ids") != null) {
			String[] id = request.getParameter("ids").split(",");

			for (String string : id) {
				ids += "'" + string + "',";
			}
			ids = ids.substring(0, ids.length() - 1);
		} else
			return null;

		String SelectSql = " SELECT QL.QLID,H.ZRZH,H.FH,H.FWJG,H.SZC,H.ZCS,H.SCJZMJ AS JZMJ,H.GHYT,QL.QLLX,QL.DJLX,QL.DJDYID,H.ZL";
		String FromSql = " FROM BDCK.BDCS_H_XZ H LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON DJDY.BDCDYID=H.BDCDYID LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=DJDY.DJDYID  ";
		String WhereSql = " WHERE EXISTS (SELECT QLR.QLID FROM BDCK.BDCS_QLR_XZ QLR WHERE QLR.QLID=QL.QLID AND ";
		WhereSql += " QL.DJDYID IN (" + ids + "))";
		String fulSql = SelectSql + FromSql + WhereSql;

		List<Map> list = baseCommonDao.getPageDataByFullSql(fulSql, page, rows);
		long count = baseCommonDao.getCountByFullSql(FromSql + WhereSql);
		Message msg = new Message();
		for (Map map : list) {
			if (!StringHelper.isEmpty(map.get("FWJG"))) {
				String jg = ConstHelper.getNameByValue("FWJG", map.get("FWJG")
						.toString());
				map.put("FWJG", jg);
			}
			if (!StringHelper.isEmpty(map.get("GHYT"))) {
				String yt = ConstHelper.getNameByValue("FWYT", map.get("GHYT")
						.toString());
				map.put("GHYT", yt);
			}
			String zt = "";
			if (("23").equals(map.get("QLLX"))) {
				zt = "已抵押、";
			} else {
				zt = "未抵押、";
			}
			if (("800").equals(map.get("DJLX"))) {
				zt += "已查封";
			} else {
				zt += "未查封";
			}
			map.put("FWZT", zt);
		}
		msg.setRows(list);
		msg.setTotal(count);
		return msg;
	}

	/**
	 * @Title: NohouseInfo
	 * @Description: 查询qlr产权信息
	 * @Author：赵梦帆
	 * @Data：2016年10月27日 下午3:06:03
	 * @param request
	 * @param xmbh
	 * @return
	 * @return Message
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	public static Message NohouseInfo(HttpServletRequest request, String xmbh) {

		Message msg = new Message();

		List<Map> sqrs= new ArrayList<Map>();
		
		String data = "";
		
		String operateContent = "";
		if (!StringHelper.isEmpty(request.getParameter("qlrs"))) {
			String json = request.getParameter("qlrs");
			JSONObject jsonObject = JSONObject.fromObject(json);
			JSONArray qlrArry = jsonObject.getJSONArray("qlrs");
			for (Object object : qlrArry) {
				JSONObject obj = (JSONObject) object;
				data += "'" + obj.getString("qlrmc") + obj.getString("zjh")
						+ "',";
				operateContent  = obj.getString("qlrmc") + "---" + obj.getString("zjh");
				addCxLog(operateContent, "", xmbh, "wfcx");
			}
			data = data.substring(0, data.length() - 1);
		} else {
			msg.setMsg("error");
			msg.setSuccess("false");
			return msg;
		}
		String fulSql = " SELECT QLID,QLRMC,ZJZL,ZJH FROM BDCK.BDCS_QLR_LS WHERE QLRMC||ZJH IN ("
				+ data + ")";
		List<Map> qlrs = baseCommonDao.getDataListByFullSql(fulSql);
		HashSet<String> qlids = new HashSet<String>();
		if (qlrs != null && qlrs.size() > 0) {
			HashSet<String> sqrStr = new HashSet<String>();
			for (Map map : qlrs) {
				qlids.add((String) map.get("QLID"));
				if(sqrStr.add((String)map.get("QLRMC")+(String)map.get("ZJZL")+(String)map.get("ZJH"))){
					Map<String,String> sqr = new HashMap<String, String>();
					sqr.put("QLRMC", (String) map.get("QLRMC"));
					sqr.put("ZJZL", ConstHelper.getNameByValue("ZJLX", map.get("ZJZL")+""));
					sqr.put("ZJH", (String) map.get("ZJH"));
					sqrs.add(sqr);
				}
			}
		}
		
		/*
		 * 保存当前权利人的权利状况 
		 * 1.保存qlid,djdyid
		 * 2.ql现势（xz）或者历史（ls）状态
		 */
		List<Map> lsm = new ArrayList<Map>();
		if (!qlids.isEmpty()) {
			String qlid = "";
			Object[] qlidRrry = qlids.toArray();
			for (Object object : qlidRrry) {
				qlid = object.toString();
				if (!StringHelper.isEmpty(qlid)) {
					Map<String, String> holder = new HashMap<String, String>();
					// 先判断ls
					List<BDCS_QL_LS> lsql = baseCommonDao.getDataList(
							BDCS_QL_LS.class,
							" QLLX IN ('4','6','8') AND QLID = '" + qlid + "'");
					if (lsql != null && lsql.size() > 0) {
						holder.put("qlid", qlid);
						holder.put("bdcqzh", lsql.get(0).getBDCQZH());
						// 再判断xz
						List<BDCS_QL_XZ> xzql = baseCommonDao.getDataList(
								BDCS_QL_XZ.class,
								" QLLX IN ('4','6','8') AND QLID = '" + qlid
										+ "'");
						if (xzql != null && xzql.size() > 0) {
							holder.put("djdyid", xzql.get(0).getDJDYID());
							holder.put("status", "xz");
						} else {
							holder.put("djdyid", lsql.get(0).getDJDYID());
							holder.put("status", "ls");
						}
						lsm.add(holder);
					} 
				}
			}
		} else {
			msg.setMsg("未查询到房产信息");
			msg.setRows(new ArrayList<Map>());
			msg.setSuccess("false");
		}
		BDCS_XMXX xm = Global.getXMXXbyXMBH(xmbh);
		
		Map<String, List<Map>> Res = new HashMap<String, List<Map>>();
		Res.put("sqr", sqrs);
		List<Map> house = new ArrayList<Map>();
		if(lsm!=null&&lsm.size()>0){
			house = Result(lsm);
			Res.put("house", house);
		}
		Map<String, String> tp = new HashMap<String, String>();
		List<Map> tps= new ArrayList<Map>();
		tp.put("ywzh", xm.getYWLSH());
		tp.put("sqsj", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format((xm.getSLSJ())));
		List<BDCS_SQR> sqr = baseCommonDao.getDataList(BDCS_SQR.class, " XMBH='"+ xmbh +"'");
		if(sqr!=null&&sqr.size()>0){
			tp.put("sqr", sqr.get(0).getSQRXM());
			tp.put("lxdh", sqr.get(0).getLXDH());
			tp.put("cxsj", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()));
		}
		tp.put("total", house.size()+"");
		
		tps.add(tp);
		Res.put("tps", tps);
		
		List<Map> Result = new ArrayList<Map>();
		Result.add(Res);
		
		msg.setRows(Result);
		msg.setSuccess("true");
		msg.setTotal(house.size());
		
		return msg;
	}

	/**
	 * @Title: convertResult
	 * @Description: 查询单元与权利人信息 并组装
	 * @Author：赵梦帆
	 * @Data：2016年10月28日 下午5:10:04
	 * @param lsm
	 * @param lsh 
	 * @return 
	 * @return void
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static List<Map> Result(List<Map> lsm) {
		List<Map> result = new ArrayList<Map>();
		if(lsm!=null&&lsm.size()>0){
			for (Map map : lsm) {
				HashMap<String, String> re = new HashMap<String, String>();
				/*
				 * 查询信息 
				 */
				String bdcqzh = "";
				String ywlb = "";
				String qlrs = "";
				String zl = "";
				double jzmj = 0.0;
				String yt ="";
				String qlzt ="";
				String mqlid = (String) map.get("qlid");
				String mdjdyid = (String) map.get("djdyid");
				String mststus = (String) map.get("status");
				List<BDCS_DJDY_LS> dy = baseCommonDao.getDataList(BDCS_DJDY_LS.class, " DJDYID = '"+ mdjdyid +"'");
				String bdcdyid = "";
				if(dy!=null&&dy.size()>0){
					bdcdyid = dy.get(0).getBDCDYID();
				}
				RealUnit units = null;
				List<RightsHolder> rightsholders = new ArrayList<RightsHolder>();
				Rights rights = null;
				if(DJDYLY.LS.Name.equals(mststus)){
					/*
					 * 查询h信息
					 */
					RealUnit units_h = UnitTools.loadUnit(BDCDYLX.H, DJDYLY.LS, bdcdyid);
					if(units_h!=null){
						units = units_h;
					}
					RealUnit units_ych = UnitTools.loadUnit(BDCDYLX.YCH, DJDYLY.LS, bdcdyid);
					if(units_ych!=null){
						units = units_ych;
					}
					/*
					 * 查询权利对应qlr
					 */
					rightsholders = RightsHolderTools.loadRightsHolders(DJDYLY.LS, mqlid);
					/*
					 * 查询权利对应ql
					 */
					rights = RightsTools.loadRights(DJDYLY.LS, mqlid);
				}else{
					/*
					 * 查询h信息
					 */
					RealUnit units_h = UnitTools.loadUnit(BDCDYLX.H, DJDYLY.XZ, bdcdyid);
					if(units_h!=null){
						units = units_h;
					}
					RealUnit units_ych = UnitTools.loadUnit(BDCDYLX.YCH, DJDYLY.XZ, bdcdyid);
					if(units_ych!=null){
						units = units_ych;
					}
					/*
					 * 查询权利对应qlr
					 */
					rightsholders = RightsHolderTools.loadRightsHolders(DJDYLY.XZ, mqlid);
					rights = RightsTools.loadRights(DJDYLY.XZ, mqlid);
				}
				/*
				 * 赋值
				 */
				if(rightsholders!=null&&rightsholders.size()>0){
					bdcqzh = rightsholders.get(0).getBDCQZH();
					re.put("bdcqzh", bdcqzh);
					ywlb = ConstHelper.getNameByValue("QLLX", rights.getQLLX());
					re.put("ywlb", ywlb);
					for (RightsHolder rightsHolder : rightsholders) {
						qlrs += rightsHolder.getQLRMC() + " ";
					} 
					re.put("qlrs", qlrs);
					zl = units.getZL();
					re.put("zl", zl);
					jzmj = units.getMJ();
					re.put("jzmj", StringHelper.formatDouble(jzmj));
					qlzt = rights.getClass().toString().toUpperCase().indexOf("LS")>-1?"历史":"现势";
					re.put("qlzt", qlzt);
					House house = (House)units;
					re.put("yt", ConstHelper.getNameByValue("FWYT", house.getFWYT1()));
				}
				result.add(re);
			}
		}
		return result;
	}
}

