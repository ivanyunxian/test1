package com.supermap.realestate.registration.service.impl;

import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.YC_SC_H_XZ;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.service.LogInfoService;
import com.supermap.realestate.registration.service.QueryService_hn;
import com.supermap.realestate.registration.tools.ConverIdCard;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.LogConstValue.QUERYTYPE;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service("QueryService_hn")
public class QueryServiceImpl_hn implements QueryService_hn {
	@Autowired
	private CommonDao baseCommonDao;

	@Autowired
	private LogInfoService logInfoService;
	
	/**   
	 * Title: queryHousehn  
	 * Description:  自助查询机服务
	 * @param request
	 * @param qlrmc
	 * @param qlrzjh
	 * @return   
	 * @see com.supermap.realestate.registration.service.QueryService_hn#queryHousehn(HttpServletRequest, String, String)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map queryHousehn(HttpServletRequest request, String qlrmc, String qlrzjh) {
		Map<String, String> queryvalues = new HashMap<String, String>();
		queryvalues.put("QLR.QLRMC", qlrmc);
		queryvalues.put("QLR.ZJH", qlrzjh);
		Map result = new HashMap();
		List<Map> list = new ArrayList<Map>();
		result.put("code", UUID.randomUUID().toString().replaceAll("-", ""));
		
		// 获取BDCDYID
		StringBuilder getbdcdyid = new StringBuilder();
		getbdcdyid.append("SELECT djdy.BDCDYID, djdy.BDCDYLX, djdy.DJDYID FROM BDCK.BDCS_QL_XZ ql ").append("LEFT JOIN BDCK.BDCS_QLR_XZ qlr ON ql.QLID = qlr.QLID ")
		          .append("LEFT JOIN BDCK.BDCS_DJDY_XZ djdy ON ql.DJDYID = djdy.DJDYID WHERE ql.QLLX <> '23' AND ");
		if (qlrzjh.length() == 18) {
			String oldCard = ConverIdCard.getOldIDCard(qlrzjh);
			if (oldCard.length() != 15) {
				getbdcdyid.append("instr(qlr.ZJH,'" + qlrzjh.trim() + "')>0 ");
			} else {
				getbdcdyid.append("instr(qlr.ZJH,'" + qlrzjh.trim() + "')>0 OR ");
				getbdcdyid.append("instr(qlr.ZJH,'" + oldCard.trim() + "')>0 ");
			}
		} else {
			getbdcdyid.append("instr(qlr.ZJH,'" + qlrzjh.trim() + "')>0 ");
		}
//		getbdcdyid.append("AND instr(qlr.QLRMC,'" + qlrmc.trim() + "')>0 ");  //参数传了姓名和证件号，但实际只使用了证件号进行查询，防止权力人姓名录错了的情况，打出无房证明的
		List<Map> bdcdyidList = baseCommonDao.getDataListByFullSql(getbdcdyid.toString());
		List<String> bdcdyids = new ArrayList<String>();
		
		// 获取每个房屋对应的单元信息、权利信息
		if (bdcdyidList != null && bdcdyidList.size() > 0) {
			for (int i = 0; i < bdcdyidList.size(); i++) {
				String jzmj = "";
				String tnjzmj = "";
				Map hMap = new HashMap();
				String lx = StringHelper.formatObject(bdcdyidList.get(i).get("BDCDYLX"));
				String bdcdyid = StringHelper.formatObject(bdcdyidList.get(i).get("BDCDYID"));
				String djdyid = StringHelper.formatObject(bdcdyidList.get(i).get("DJDYID"));
				
				String tablename="BDCS_H_XZ";
				if (!bdcdyids.contains(bdcdyid)) {
					if (lx.equals("031") || lx.equals("032")) {
						bdcdyids.add(bdcdyid);
						hMap.put("id", Integer.toString(bdcdyids.size()));
						if (lx.equals("031")) {
							jzmj = "h.SCJZMJ AS JZMJ ";
							tnjzmj ="h.SCTNJZMJ AS TNJZMJ";
						}else if (lx.equals("032")) {
							jzmj = "h.YCJZMJ AS JZMJ ";
							tablename="BDCS_H_XZY";
							tnjzmj ="h.YCTNJZMJ AS TNJZMJ";
						}
						
						//单元信息
						StringBuilder getunitinfo = new StringBuilder();
						getunitinfo.append("SELECT h.ZL, h.ZRZH, h.FH, h.SZC, h.ZCS, h.GHYT, h.QLXZ, h.ZDBDCDYID, h.FWXZ, ").append(jzmj +","+ tnjzmj).append(" FROM BDCK."+tablename+" h WHERE h.BDCDYID = '")
						           .append(bdcdyid).append("'");
						List<Map> unitinfoList = baseCommonDao.getDataListByFullSql(getunitinfo.toString());
						if (unitinfoList != null && unitinfoList.size() > 0) {
							hMap.put("ZL", StringHelper.formatObject(unitinfoList.get(0).get("ZL")));
							hMap.put("ZRZH", StringHelper.formatObject(unitinfoList.get(0).get("ZRZH")));
							hMap.put("FH", StringHelper.formatObject(unitinfoList.get(0).get("FH")));
							hMap.put("SZC", StringHelper.formatObject(unitinfoList.get(0).get("SZC")));
							hMap.put("ZCS", StringHelper.formatObject(unitinfoList.get(0).get("ZCS")));
							hMap.put("JZMJ", StringHelper.formatObject(unitinfoList.get(0).get("JZMJ")));
							hMap.put("TNJZMJ", StringHelper.formatObject(unitinfoList.get(0).get("TNJZMJ")));
							hMap.put("GHYT", ConstHelper.getNameByValue("FWYT", StringHelper.formatObject(unitinfoList.get(0).get("GHYT"))));
							String tdqlxzval = StringHelper.formatObject(unitinfoList.get(0).get("QLXZ"));
							String zdbdcdyid = StringHelper.formatObject(unitinfoList.get(0).get("ZDBDCDYID"));
							String fwxzval = StringHelper.formatObject(unitinfoList.get(0).get("FWXZ"));
							String qlxz = getQLXZ(tdqlxzval, zdbdcdyid, fwxzval);
							hMap.put("QLXZ", qlxz);
						}
						
						//产权信息
						StringBuilder getcqinfo = new StringBuilder();
						String cqqlrmc = "";
						List<Map<String, String>> qlrxxList = new ArrayList<Map<String,String>>();
						Map<String, String> cqqlrMap = new HashMap<String, String>();
						getcqinfo.append("SELECT ql.DJSJ, qlr.QLRMC, qlr.BDCQZH, qlr.GYFS FROM BDCK.BDCS_QL_XZ ql LEFT JOIN BDCK.BDCS_QLR_XZ qlr ON ql.QLID = qlr.QLID ")
						         .append("LEFT JOIN BDCK.BDCS_DJDY_XZ djdy ON djdy.DJDYID = ql.DJDYID WHERE djdy.BDCDYID ='")
						         .append(bdcdyid).append("' AND (ql.QLLX = '4' OR ql.QLLX = '6' OR ql.QLLX = '8')");
						List<Map> cqinfoList = baseCommonDao.getDataListByFullSql(getcqinfo.toString());
						if (cqinfoList != null && cqinfoList.size() > 0) {
							if (cqinfoList.size() > 1) {
								for (int j = 0; j < cqinfoList.size(); j++) {
									if (StringHelper.isEmpty(cqqlrmc)) {
										cqqlrmc = StringHelper.formatObject(cqinfoList.get(j).get("QLRMC"));
									}else {
										cqqlrmc = cqqlrmc + "," + StringHelper.formatObject(cqinfoList.get(j).get("QLRMC"));
									}
									
								}
							}else {
								cqqlrmc = StringHelper.formatObject(cqinfoList.get(0).get("QLRMC"));
							}
							hMap.put("QLRMC", cqqlrmc);
							hMap.put("GYFS", ConstHelper.getNameByValue("GYFS", StringHelper.formatObject(cqinfoList.get(0).get("GYFS"))));
							cqqlrMap.put("DJSJ", StringHelper.FormatByDatetime(cqinfoList.get(0).get("DJSJ")));
							cqqlrMap.put("BDCQZH", StringHelper.formatObject(cqinfoList.get(0).get("BDCQZH")));
							qlrxxList.add(cqqlrMap);
						}
						hMap.put("qlrxxList", qlrxxList);
						
						//抵押权信息
						StringBuilder getdyqinfo = new StringBuilder();
						List<Map<String, String>> dyaqList = new ArrayList<Map<String,String>>();
						getdyqinfo.append("SELECT ql.QLQSSJ, ql.QLJSSJ, ql.DJSJ AS DYQDJSJ, qlr.QLRMC AS DYQLR, qlr.BDCQZH AS DJZMH, fsql.BDBZZQSE, fsql.ZGZQSE, fsql.DYFS FROM BDCK.BDCS_QL_XZ ql ")
						          .append("LEFT JOIN BDCK.BDCS_QLR_XZ qlr ON ql.QLID = qlr.QLID LEFT JOIN BDCK.BDCS_FSQL_XZ fsql ON ql.FSQLID = fsql.FSQLID ")
						          .append("LEFT JOIN BDCK.BDCS_DJDY_XZ djdy ON djdy.DJDYID = ql.DJDYID WHERE djdy.BDCDYID ='").append(bdcdyid).append("' AND ql.QLLX = '23'");
						List<Map> dyqinfoList = baseCommonDao.getDataListByFullSql(getdyqinfo.toString());
						if (dyqinfoList != null && dyqinfoList.size() > 0) {
							for (int j = 0; j < dyqinfoList.size(); j++) {
								Map<String, String> dyqMap = new HashMap<String, String>();
								dyqMap.put("DYQLR", StringHelper.formatObject(dyqinfoList.get(j).get("DYQLR")));
								dyqMap.put("DJZMH", StringHelper.formatObject(dyqinfoList.get(j).get("DJZMH")));
								dyqMap.put("BDBZZQSE", StringHelper.formatObject(dyqinfoList.get(j).get("BDBZZQSE")));
								dyqMap.put("ZGZQSE", StringHelper.formatObject(dyqinfoList.get(j).get("ZGZQSE")));
								dyqMap.put("DYFS", ConstHelper.getNameByValue("DYFS", StringHelper.formatObject(dyqinfoList.get(j).get("DYFS"))));
								dyqMap.put("DYQDJSJ", StringHelper.FormatByDatetime(dyqinfoList.get(j).get("DYQDJSJ")));
								dyqMap.put("QLQSSJ", StringHelper.FormatByDatetime(dyqinfoList.get(j).get("QLQSSJ")));
								dyqMap.put("QLJSSJ", StringHelper.FormatByDatetime(dyqinfoList.get(j).get("QLJSSJ")));
								dyaqList.add(dyqMap);
							}
						}
						hMap.put("dyaqList", dyaqList);
						
						//查封信息
						StringBuilder getcfinfo = new StringBuilder();
						List<Map<String, String>> cfxxList = new ArrayList<Map<String,String>>();
						getcfinfo.append("SELECT ql.QLQSSJ, ql.QLJSSJ, fsql.CFJG, fsql.CFWH FROM BDCK.BDCS_QL_XZ ql LEFT JOIN BDCK.BDCS_FSQL_XZ fsql ON ql.FSQLID = fsql.FSQLID ")
								 .append("LEFT JOIN BDCK.BDCS_DJDY_XZ djdy ON djdy.DJDYID = ql.DJDYID WHERE djdy.BDCDYID ='").append(bdcdyid).append("' AND ql.DJLX = '800'");
						List<Map> cfinfoList = baseCommonDao.getDataListByFullSql(getcfinfo.toString());
						if (cfinfoList != null && cfinfoList.size() > 0) {
							for (int j = 0; j < cfinfoList.size(); j++) {
								Map<String, String> cfMap = new HashMap<String, String>();
								cfMap.put("CFJG", StringHelper.formatObject(cfinfoList.get(j).get("CFJG")));
								cfMap.put("CFWH", StringHelper.formatObject(cfinfoList.get(j).get("CFWH")));
								cfMap.put("QLQSSJ", StringHelper.FormatByDatetime(cfinfoList.get(j).get("QLQSSJ")));
								cfMap.put("QLJSSJ", StringHelper.FormatByDatetime(cfinfoList.get(j).get("QLJSSJ")));
								cfxxList.add(cfMap);
							}
						}
						hMap.put("cfxxList", cfxxList);
						
						//关联状态，抵押状态、查封状态、异议状态、限制状态
						List<BDCS_DYXZ> dyxz = baseCommonDao.getDataList(BDCS_DYXZ.class, "BDCDYID='"+bdcdyid+"'");
						if(dyxz != null && dyxz.size()>0) {
							BDCS_DYXZ dyxz1 = dyxz.get(0);
							if(dyxz1.getYXBZ()!=null&&"1".equals(dyxz1.getYXBZ())){
								hMap.put("xzzt", "房屋已限制");
							}else{
								hMap.put("xzzt", "房屋未限制");
							}
							
						}else {
							hMap.put("xzzt", "房屋未限制");
						}
						hMap.putAll(GetDJDYStatus(lx, djdyid, bdcdyid));
						
						list.add(hMap);
					}
				}
				result.put("list", list);
					}
				}
		logInfoService.queryLog( false, queryvalues, QUERYTYPE.DJFW);
		return result;
	}
	
	/**   
	 * @Title: getQLXZ   
	 * @Description: 获取权利性质字段值  土地权利性质/房屋权利性质  
	 * @author:chenbo
	 * @date:2018年3月15日 下午5:28:12    
	 * @param tdqlxzval
	 * @param zdbdcdyid
	 * @param fwxzval
	 * @return      
	 */ 
	private String getQLXZ (String tdqlxzval, String zdbdcdyid, String fwxzval){
		String blank = "----";
		String qlxz = "";
		String tdqlxz = "";
		String fwqlxz = "";
		if (SF.NO.Value.equals(ConfigHelper.getNameByValue("GetZDQLXZFrom"))) {
			tdqlxz = getQlxzByFw(tdqlxzval);
		} else if (SF.YES.Value.equals(ConfigHelper.getNameByValue("GetZDQLXZFrom"))) {
			tdqlxz = getQlxzByTd(zdbdcdyid);
		} else if (("2").equals(ConfigHelper.getNameByValue("GetZDQLXZFrom"))) {
			if (!StringHelper.isEmpty(tdqlxzval)) {
				tdqlxz = getQlxzByFw(tdqlxzval);
			} else if (!StringHelper.isEmpty(zdbdcdyid)) {
				tdqlxz = getQlxzByTd(zdbdcdyid);
			}
		}
		if (!StringHelper.isEmpty(fwxzval)) {
			String fwxzmc = ConstHelper.getNameByValue("FWXZ", fwxzval.trim());
			if (fwxzmc != null && !fwqlxz.contains(fwxzmc))
				fwqlxz += fwxzmc + "、";
		}
		if (StringHelper.isEmpty(tdqlxz))
			tdqlxz = blank;
		else
			tdqlxz = tdqlxz.substring(0, tdqlxz.length() - 1);
		if (StringHelper.isEmpty(fwqlxz))
			fwqlxz = blank;
		else
			fwqlxz = fwqlxz.substring(0, fwqlxz.length() - 1);
		qlxz = tdqlxz + "/" + fwqlxz;
		return qlxz;
	}
	
	/**   
	 * @Title: getQlxzByFw   
	 * @Description: 根据房屋获取权利性质 
	 * @author:chenbo
	 * @date:2018年3月15日 下午5:33:57    
	 * @param tdqlxzval
	 * @return      
	 */ 
	private String getQlxzByFw (String tdqlxzval){
		String tdqlxz = "";
		String tdqlxzmc = ConstHelper.getNameByValue("QLXZ", tdqlxzval.trim());
		if (!StringHelper.isEmpty(tdqlxzmc) && !tdqlxz.contains(tdqlxzmc)) {
			tdqlxz += tdqlxzmc + "、";
		}
		return tdqlxz;
	}
	
	/**   
	 * @Title: getQlxzByTd   
	 * @Description: 根据土地获取房屋性质 
	 * @author:chenbo
	 * @date:2018年3月15日 下午5:34:10    
	 * @param zdbdcdyid
	 * @return      
	 */ 
	private String getQlxzByTd (String zdbdcdyid){
		String tdqlxz = "";
		UseLand land = (UseLand) UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ, zdbdcdyid);
		if (land != null && !StringHelper.isEmpty(land.getQLXZ())) {
			String tdqlxzmc = ConstHelper.getNameByValue("QLXZ", land.getQLXZ().trim());
			if (!StringHelper.isEmpty(tdqlxzmc) && !tdqlxz.contains(tdqlxzmc)) {
				tdqlxz += tdqlxzmc + "、";
			}
		}
		return tdqlxz;
	}
	
	/**   
	 * @Title: GetDJDYStatus   
	 * @Description: 获取登记状态 
	 * @author:chenbo
	 * @date:2018年3月15日 下午5:17:02    
	 * @param lx
	 * @param djdyid
	 * @param bdcdyid
	 * @return      
	 */ 
	protected Map<String, String> GetDJDYStatus(String lx, String djdyid, String bdcdyid) {
		List<BDCS_QL_XZ> dyqzt = null;
		List<BDCS_QL_XZ> cfqzt = null;
		List<BDCS_QL_XZ> yyqzt = null;
		Map<String,String> statusmap = new HashMap<String, String>();
		dyqzt = baseCommonDao.getDataList(BDCS_QL_XZ.class, "QLLX='23' AND DJDYID='"+djdyid+"'");
		cfqzt = baseCommonDao.getDataList(BDCS_QL_XZ.class, "QLLX='99' AND DJLX='800' AND DJDYID='"+djdyid+"'");
		yyqzt = baseCommonDao.getDataList(BDCS_QL_XZ.class, "QLLX='99' AND DJLX='600' AND DJDYID='"+djdyid+"'");
		if(BDCDYLX.H.Value.equals(lx)|| BDCDYLX.YCH.Value.equals(lx)){
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
				REA = baseCommonDao.getDataList(YC_SC_H_XZ.class, " SCBDCDYID='"+bdcdyid+"'");
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
				REA = baseCommonDao.getDataList(YC_SC_H_XZ.class, " YCBDCDYID='"+ bdcdyid +"'");
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
	
}
