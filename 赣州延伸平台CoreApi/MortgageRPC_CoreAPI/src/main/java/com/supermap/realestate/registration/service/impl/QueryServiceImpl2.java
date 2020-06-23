package com.supermap.realestate.registration.service.impl;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.QueryClass.QLInfo;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.YC_SC_H_XZ;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.service.QueryService;
import com.supermap.realestate.registration.service.QueryService2;
import com.supermap.realestate.registration.tools.ConverIdCard;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.DateUtil;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;

@Service("queryService2")
public class QueryServiceImpl2 implements QueryService2 {

	@Autowired
	private CommonDao baseCommonDao;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Message queryHouse(Map<String, String> queryvalues, int page,
			int rows, boolean iflike, String fwzt,String sort,String order,String zjdy) {
		Message msg = new Message();
		long count = 0;
		List<Map> listresult = null;
		/* ===============1、先获取实体对应的表名=================== */
		String unitentityName = "BDCK.BDCS_H_XZ";

		/* ===============2、再获取表名+'_'+字段名=================== */
		String dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.FH,DY.GHYT,DY.FWBM,DY.SCJZMJ,DY.SCTNJZMJ,DY.SCFTJZMJ,'031' AS BDCDYLX ,'现房' AS BDCDYLXMC ";
		String qlfieldsname = "QL.QLID,QL.BDCQZH";

		if (fwzt != null && fwzt.equals("2")) {
			unitentityName = "BDCK.BDCS_H_XZY";
			dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.FH,DY.GHYT,DY.FWBM,DY.YCJZMJ AS SCJZMJ,DY.YCTNJZMJ AS SCTNJZMJ,DY.YCFTJZMJ AS SCFTJZMJ,'032' AS BDCDYLX,'期房' AS BDCDYLXMC ";
		}

		// 统一期现房 2015年10月28日
		if (fwzt != null && fwzt.equals("3")) {
			unitentityName = "(SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ ,'031' AS BDCDYLX ,'现房' AS BDCDYLXMC FROM BDCK.BDCS_H_XZ UNION ALL SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ  ,'032' AS BDCDYLX ,'期房' AS BDCDYLXMC FROM BDCK.BDCS_H_XZY)";
			dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.FH,DY.GHYT,DY.JZMJ AS SCJZMJ,DY.TNJZMJ AS SCTNJZMJ,DY.FTJZMJ AS SCFTJZMJ,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC";
		}

		/* ===============3、构造查询语句=================== */
		/* SELECT 字段部分 */
		StringBuilder builder2 = new StringBuilder();
		builder2.append("select ").append(dyfieldsname).append(",")
				.append(qlfieldsname);
		String selectstr = builder2.toString();

		/* FROM 后边的表语句 */
		StringBuilder builder = new StringBuilder();
		builder.append(" from {0} DY")
				.append(" left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid")
				.append(" left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid  ");
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
					/*
					 如果通过不动产权证查询，且是精确查询时,
					其中如果不动产权证号是否纯数字的，直接在权利人表中通过BDCQZHXH条件查询，
					否则从权利表中通过BDCQZH条件查询
					*/
					if (name.contains("BDCQZH")) {
						//验证不动产权证号是否为纯数字的
						boolean flag = StringHelper.isNumericByString(value);
						if (flag) {
							//判断是否已经有查询权利人的条件了
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
			builder3.append(" EXISTS (SELECT 1 FROM BDCK.BDCS_QLR_XZ QLR WHERE QLR.QLID=QL.QLID AND "
					+ qlrbuilder.toString() + ")");
			havecondition = true;
		}
		// 有抵押人查询条件
		if (!StringHelper.isEmpty(dyrbuilder.toString())) {
			if (havecondition) {
				builder3.append(" and ");
			}
			builder3.append(" QL.QLID IN (SELECT QLID FROM BDCK.BDCS_FSQL_XZ DYR WHERE "
					+ dyrbuilder.toString() + ")");
			havecondition = true;
		}

		if (fwzt == null || !fwzt.equals("2")) {
			if (havecondition) {
				// builder3.append(" and ");
			}
			// builder3.append(" ql.qllx='4'");
		}
		// 在建工程抵押
		if (zjdy!= null&&!"".equals(zjdy)) {
			if (havecondition&&!"0".equals(zjdy)) {
				builder3.append(" and ");
			}if("1".equals(zjdy)) {
			builder3.append("   (ql.DJLX= '100' and ql.QLLX= '23'  and djdy.BDCDYLX= '032') ");
			havecondition=true;
			}
		}
		String wherestr = builder3.toString();
		if (wherestr.trim().toUpperCase().endsWith("WHERE")) {
			wherestr = "";
		}
		
		String fromSql = " from (select ql.qlid " + fromstr + wherestr + ")";
		String fullSql = selectstr + fromstr + wherestr;
		
		
		/* 为了提高效率，先不查询符合条件的记录总数，将来改为异步获取 */
		CommonDao dao = baseCommonDao;

		count = dao.getCountByFullSql(fromSql);
		listresult = dao.getPageDataByFullSql(fullSql, page, rows);
		
		addLimitStatus(listresult);
		addscdjStatus(listresult); 
		addzjgcStatus(listresult);
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
		if(!StringUtils.isEmpty(sort)&&!StringUtils.isEmpty(order))
		{
			if(!sort.toUpperCase().equals("BDCDYID")){
				listresult=bullderSort(listresult,sort.toUpperCase(),order);
			}
		}
		msg.setTotal(count);
		msg.setRows(listresult);
		return msg;
	}

 private List<Map> bullderSort(List<Map> lst,final String fieldName,final String sortname){	 
	 Collections.sort(lst, new Comparator<Map>(){
		@Override
		public int compare(Map m1, Map m2) {
			String fieldvalue1= StringHelper.FormatByDatatype(m1.get(fieldName));
			String fieldvalue2= StringHelper.FormatByDatatype(m2.get(fieldName));
			if(sortname.toLowerCase().equals("asc")){
				return fieldvalue1.compareTo(fieldvalue2);
			}else{
				return fieldvalue2.compareTo(fieldvalue1);
			}
		}
	 });
	 return lst;
 }
	
	private void addLimitStatus(List<Map> result) {
		if (result != null && result.size() > 0) {
			for (Map map : result) {
				if (map.containsKey("DJDYID")) {
					String djdyid = (String) map.get("DJDYID");
					String ycdjdyid = "";
					String scdjdyid = "";
					if (djdyid != null) {
						List<BDCS_DJDY_XZ> listdjdy = baseCommonDao
								.getDataList(BDCS_DJDY_XZ.class, "djdyid='"
										+ djdyid + "'");
						if (listdjdy != null && listdjdy.size() > 0) {
							BDCDYLX lx = BDCDYLX.initFrom(listdjdy.get(0)
									.getBDCDYLX());
							if (lx.Value.equals("031")) {
								String valuedyxz = "";
								String valuecfxz = "";
								String valueyyxz = "";
								
								List<YC_SC_H_XZ> listycsc = baseCommonDao
										.getDataList(YC_SC_H_XZ.class,
												"scbdcdyid='"
														+ listdjdy.get(0)
																.getBDCDYID()
														+ "'");
								if (listycsc != null
										&& listycsc.size() > 0
										&& listycsc.get(0).getYCBDCDYID() != null) {
									List<BDCS_DJDY_XZ> listdjdyyc = baseCommonDao
											.getDataList(
													BDCS_DJDY_XZ.class,
													"djdyid='"
															+ listycsc
																	.get(0)
																	.getYCBDCDYID()
															+ "'");
									if (listdjdyyc != null
											&& listdjdyyc.size() > 0) {
										ycdjdyid = listdjdyyc.get(0)
												.getDJDYID();
										Map<String, String> mapxzy = new HashMap<String, String>();
										mapxzy = getDYandCFandYY_XZY(ycdjdyid);
										for (Entry<String, String> ent : mapxzy
												.entrySet()) {
											String name = ent.getKey();
											if (name.equals("DYZT")) {
												if(StringHelper.isEmpty(valuedyxz)){
													valuedyxz = ent.getValue();
												}else{
													valuedyxz = valuedyxz+"、"+ent.getValue();
												}
											} else if (name.equals("CFZT")) {
												if(StringHelper.isEmpty(valuecfxz)){
													valuecfxz = ent.getValue();
												}else{
													valuecfxz = valuecfxz+"、"+ent.getValue();
												}
											} else {
												if(StringHelper.isEmpty(valueyyxz)){
													valueyyxz = ent.getValue();
												}else{
													valueyyxz = valueyyxz+"、"+ent.getValue();
												}
											}
										}
									}
								} 
								Map<String, String> mapxz = new HashMap<String, String>();
								mapxz = getDYandCFandYY_XZ(djdyid);
								for (Entry<String, String> ent : mapxz
										.entrySet()) {
									String name = ent.getKey();
									if (name.equals("DYZT")) {
										if(StringHelper.isEmpty(valuedyxz)){
											valuedyxz = ent.getValue();
										}else{
											valuedyxz = valuedyxz+" "+ent.getValue();
										}
									} else if (name.equals("CFZT")) {
										if(StringHelper.isEmpty(valuecfxz)){
											valuecfxz = ent.getValue();
										}else{
											valuecfxz = valuecfxz+" "+ent.getValue();
										}
									} else {
										if(StringHelper.isEmpty(valueyyxz)){
											valueyyxz = ent.getValue();
										}else{
											valueyyxz = valueyyxz+" "+ent.getValue();
										}
									}
								}
								map.put("DYZT", valuedyxz);
								map.put("CFZT", valuecfxz);
								map.put("YYZT", valueyyxz);

							} else {
								List<YC_SC_H_XZ> listycsc = baseCommonDao
										.getDataList(YC_SC_H_XZ.class,
												"ycbdcdyid='"
														+ listdjdy.get(0)
																.getBDCDYID()
														+ "'");
								if (listycsc != null
										&& listycsc.size() > 0
										&& listycsc.get(0).getSCBDCDYID() != null) {
									List<BDCS_DJDY_XZ> listdjdysc = baseCommonDao
											.getDataList(
													BDCS_DJDY_XZ.class,
													"djdyid='"
															+ listycsc
																	.get(0)
																	.getSCBDCDYID()
															+ "'");
									if (listdjdysc != null
											&& listdjdysc.size() > 0) {
										scdjdyid = listdjdysc.get(0)
												.getDJDYID();
										Map<String, String> mapxz = new HashMap<String, String>();
										mapxz = getDYandCFandYY_XZ(scdjdyid);
										String valuedyxz = "";
										String valuecfxz = "";
										String valueyyxz = "";
										for (Entry<String, String> ent : mapxz
												.entrySet()) {
											String name = ent.getKey();
											if (name.equals("DYZT")) {
												if(StringHelper.isEmpty(valuedyxz)){
													valuedyxz = ent.getValue();
												}else{
													valuedyxz = valuedyxz+" "+ent.getValue();
												}
											} else if (name.equals("CFZT")) {
												if(StringHelper.isEmpty(valuecfxz)){
													valuecfxz = ent.getValue();
												}else{
													valuecfxz = valuecfxz+" "+ent.getValue();
												}
											} else {
												if(StringHelper.isEmpty(valueyyxz)){
													valueyyxz = ent.getValue();
												}else{
													valueyyxz = valueyyxz+" "+ent.getValue();
												}
											}
										}
									}
								}
								Map<String, String> mapxzy = new HashMap<String, String>();
								mapxzy = getDYandCFandYY_XZY(djdyid);
								String valuedyxz = "";
								String valuecfxz = "";
								String valueyyxz = "";
								for (Entry<String, String> ent : mapxzy
										.entrySet()) {
									String name = ent.getKey();
									if (name.equals("DYZT")) {
										if(StringHelper.isEmpty(valuedyxz)){
											valuedyxz = ent.getValue();
										}else{
											valuedyxz = valuedyxz+" "+ent.getValue();
										}
									} else if (name.equals("CFZT")) {
										if(StringHelper.isEmpty(valuecfxz)){
											valuecfxz = ent.getValue();
										}else{
											valuecfxz = valuecfxz+" "+ent.getValue();
										}
									} else {
										if(StringHelper.isEmpty(valueyyxz)){
											valueyyxz = ent.getValue();
										}else{
											valueyyxz = valueyyxz+" "+ent.getValue();
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
	}

	public Map<String, String> getDYandCFandYY_XZY(String djdyid) {
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

		long mortgageCount = baseCommonDao.getCountByFullSql(sqlMortgage);
		long SealCount = baseCommonDao.getCountByFullSql(sqlSeal);
		long ObjectionCount = baseCommonDao.getCountByFullSql(sqlObjection);

		String mortgageStatus = mortgageCount > 0 ? "期房已抵押" : "期房无抵押";
		String sealStatus = SealCount > 0 ? "期房已查封" : "期房无查封";

		// 判断完现状层中的查封信息，接着判断办理中的查封信息
		if (!(SealCount > 0)) {
			String sqlSealing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and  b.djdyid=c.djdyid WHERE c.djlx='800' AND c.qllx='99' and c.djdyid='"
					+ djdyid + "' and a.sfdb='0' ";
			long count = baseCommonDao.getCountByFullSql(sqlSealing);
			sealStatus = count > 0 ? "期房查封办理中" : "期房无查封";
		}

		// 判断完现状层中的查封信息，接着判断办理中的查封信息
		if (!(mortgageCount > 0)) {
			String sqlmortgageing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='100' AND c.qllx='23' and c.djdyid='"
					+ djdyid + "' and a.sfdb='0' ";
			long count = baseCommonDao.getCountByFullSql(sqlmortgageing);
			mortgageStatus = count > 0 ? "期房抵押办理中" : "期房无抵押";
		}

		String objectionStatus = ObjectionCount > 0 ? "期房有异议" : "期房无异议";
		map.put("DYZT", mortgageStatus);
		map.put("CFZT", sealStatus);
		map.put("YYZT", objectionStatus);

		return map;

	}

	public Map<String, String> getDYandCFandYY_XZ(String djdyid) {
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

		long mortgageCount = baseCommonDao.getCountByFullSql(sqlMortgage);
		long SealCount = baseCommonDao.getCountByFullSql(sqlSeal);
		long ObjectionCount = baseCommonDao.getCountByFullSql(sqlObjection);

		String mortgageStatus = mortgageCount > 0 ? "现房已抵押" : "现房无抵押";
		String sealStatus = SealCount > 0 ? "现房已查封" : "现房无查封";

		// 判断完现状层中的查封信息，接着判断办理中的查封信息
		if (!(SealCount > 0)) {
			String sqlSealing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and  b.djdyid=c.djdyid WHERE c.djlx='800' AND c.qllx='99' and c.djdyid='"
					+ djdyid + "' and a.sfdb='0' ";
			long count = baseCommonDao.getCountByFullSql(sqlSealing);
			sealStatus = count > 0 ? "现房查封办理中" : "现房无查封";
		}

		// 判断完现状层中的查封信息，接着判断办理中的查封信息
		if (!(mortgageCount > 0)) {
			String sqlmortgageing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='100' AND c.qllx='23' and c.djdyid='"
					+ djdyid + "' and a.sfdb='0' ";
			long count = baseCommonDao.getCountByFullSql(sqlmortgageing);
			mortgageStatus = count > 0 ? "现房抵押办理中" : "现房无抵押";
		}

		String objectionStatus = ObjectionCount > 0 ? "现房有异议" : "现房无异议";

		map.put("DYZT", mortgageStatus);
		map.put("CFZT", sealStatus);
		map.put("YYZT", objectionStatus);

		return map;
	}

	@SuppressWarnings("unchecked")
	private void adddyrxzInfo(
			@SuppressWarnings("rawtypes") Map map) {
		
				 if (map.containsKey("QLID")) {
					String qlid = (String) map.get("QLID");
					if (qlid != null) {
						List<BDCS_FSQL_XZ> listql = baseCommonDao
								.getDataList(BDCS_FSQL_XZ.class, "QLID='"
										+ qlid + "'"); 
						if (listql != null&&listql.size()>0) {
							if(!StringHelper.isEmpty(listql.get(0).getDYR())){
								String dyr = listql.get(0).getDYR();
								map.put("DYRMC", dyr);
							}
						
						}
						RightsHolder holder = RightsHolderTools
								.getUnionRightsHolder(DJDYLY.XZ, qlid);
						if (holder != null) {
							map.put("DYQRMC", holder.getQLRMC());
						}
					}
				}
		 
	}
	
	@SuppressWarnings("unchecked")
	private void adddyrgzInfo(
			@SuppressWarnings("rawtypes") Map map) {
				 if (map.containsKey("QLID")) {
					String qlid = (String) map.get("QLID");
					if (qlid != null) {
						List<BDCS_FSQL_GZ> listql = baseCommonDao
								.getDataList(BDCS_FSQL_GZ.class, "QLID='"
										+ qlid + "'"); 
						if (listql != null&&listql.size()>0) {
							if(!StringHelper.isEmpty(listql.get(0).getDYR())){
								String dyr = listql.get(0).getDYR();
								map.put("DYRMC", dyr);
							}
						
						}
						List<BDCS_QLR_GZ> listqlr = baseCommonDao
								.getDataList(BDCS_QLR_GZ.class, "QLID='"
										+ qlid + "'"); 
						if (listqlr != null&&listqlr.size()>0) {
							if(!StringHelper.isEmpty(listqlr.get(0).getQLRMC()))
							map.put("DYQRMC",listqlr.get(0).getQLRMC() );
						}
					}
				}
	}

	private void addscdjStatus(List<Map> result) {
		if (result != null && result.size() > 0) {
			for (Map map : result) {
				if (map.containsKey("DJDYID")) {
					String djdyid = (String) map.get("DJDYID");
					if(!StringHelper.isEmpty(djdyid)){

						List<BDCS_DJDY_XZ> listdjdy = baseCommonDao
								.getDataList(BDCS_DJDY_XZ.class, "djdyid='"
										+ djdyid + "'");
						if (listdjdy != null && listdjdy.size() > 0) {
							String scdjdyid = null;
							BDCDYLX lx = BDCDYLX.initFrom(listdjdy.get(0)
									.getBDCDYLX());
							if (lx.Value.equals("031")) { 
							    scdjdyid=listdjdy.get(0).getDJDYID();
								String sqlSCDJ = MessageFormat.format(
										" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and qllx=''4''",
										scdjdyid);
								long scdjCount = baseCommonDao.getCountByFullSql(sqlSCDJ);
								String scdjStatus = scdjCount > 0 ? "是" : "否";
								// 判断完现状层中的信息，接着判断办理中的信息
								if (!(scdjCount > 0)) {
									String sqlscdj = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE  c.qllx='4' and c.djdyid='"
											+ scdjdyid + "' and a.sfdb='0' ";
									long count = baseCommonDao.getCountByFullSql(sqlscdj);
									scdjStatus = count > 0 ? "首次登记办理中" : "否";
								}
								
								map.put("SCDJ", scdjStatus);
								 
								
							} else {
								List<YC_SC_H_XZ> listycsc = baseCommonDao
										.getDataList(YC_SC_H_XZ.class,
												"ycbdcdyid='"
														+ listdjdy.get(0)
																.getBDCDYID()
														+ "'");
								if (listycsc != null
										&& listycsc.size() > 0
										&& listycsc.get(0).getSCBDCDYID() != null) {
									List<BDCS_DJDY_XZ> listdjdysc = baseCommonDao
											.getDataList(
													BDCS_DJDY_XZ.class,
													"BDCDYID='"
															+ listycsc
																	.get(0)
																	.getSCBDCDYID()
															+ "'");
									if (listdjdysc != null
											&& listdjdysc.size() > 0) {
										scdjdyid = listdjdysc.get(0)
												.getDJDYID();
										
										String sqlSCDJ = MessageFormat.format(
												" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and qllx=''4''",
												scdjdyid);
										long scdjCount = baseCommonDao.getCountByFullSql(sqlSCDJ);
										String scdjStatus = scdjCount > 0 ? "是" : "否";
										// 判断完现状层中的信息，接着判断办理中的信息
										if (!(scdjCount > 0)) {
											String sqlscdj = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.qllx='4' and c.djdyid='"
													+ scdjdyid + "' and a.sfdb='0' ";
											long count = baseCommonDao.getCountByFullSql(sqlscdj);
											scdjStatus = count > 0 ? "首次登记办理中" : "否";
										}
										map.put("SCDJ", scdjStatus);
 
									}
								}else{
									map.put("SCDJ", "否");
								}
								
								
							}
						}
					
					}
				}
			}
		}
				
	}
	
	
	private void addzjgcStatus(List<Map> result) {
		if (result != null && result.size() > 0) {
			for (Map map : result) {
				if (map.containsKey("DJDYID")) {
					String djdyid = (String) map.get("DJDYID");
					if(!StringHelper.isEmpty(djdyid)){

						List<BDCS_DJDY_XZ> listdjdy = baseCommonDao
								.getDataList(BDCS_DJDY_XZ.class, "djdyid='"
										+ djdyid + "'");
						if (listdjdy != null && listdjdy.size() > 0) {
							String ycdjdyid = null;
							BDCDYLX lx = BDCDYLX.initFrom(listdjdy.get(0)
									.getBDCDYLX());
							if (lx.Value.equals("031")) { 
								List<YC_SC_H_XZ> listycsc = baseCommonDao
										.getDataList(YC_SC_H_XZ.class,
												"scbdcdyid='"
														+ listdjdy.get(0)
																.getBDCDYID()
														+ "'");
								if (listycsc != null
										&& listycsc.size() > 0
										&& listycsc.get(0).getSCBDCDYID() != null) {
									List<BDCS_DJDY_XZ> listdjdyyc = baseCommonDao
											.getDataList(
													BDCS_DJDY_XZ.class,
													"BDCDYID='"
															+ listycsc
																	.get(0)
																	.getYCBDCDYID()
															+ "'");
									if (listdjdyyc != null
											&& listdjdyyc.size() > 0) {
										ycdjdyid = listdjdyyc.get(0)
												.getDJDYID();
										String sqlzjgc = MessageFormat.format(
												" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and qllx=''23'' ",
												ycdjdyid);
										long zjcCount = baseCommonDao.getCountByFullSql(sqlzjgc);
										String sqlzjgc2 = MessageFormat.format(
												" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and qllx=''4'' ",
												ycdjdyid);
										long zjcCount2 = baseCommonDao.getCountByFullSql(sqlzjgc2);
										String	zjgcStatus;
										if(zjcCount > 0 && zjcCount2==0){
												zjgcStatus="是";
										}else{
											zjgcStatus="否";
										}
										// 判断完现状层中的信息，接着判断办理中的信息
										if (!(zjcCount > 0)) {
											String sqlzjgcs = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE  c.qllx='23' and c.djdyid='"
													+ ycdjdyid + "' and a.sfdb='0' ";
											long count = baseCommonDao.getCountByFullSql(sqlzjgcs);
											if(count > 0 && zjcCount2==0){
												zjgcStatus="在建工程抵押登记办理中";
										}else{
											zjgcStatus="否";
										}
											 
										}
										if(zjgcStatus=="是"){
											adddyrxzInfo(map);
										}else if(zjgcStatus=="在建工程抵押登记办理中"){
											adddyrgzInfo(map);
										}
										map.put("ZJGC", zjgcStatus);
									}
								}else{
									map.put("ZJGC", "否");
								}	 
								
							} else {
								        ycdjdyid=listdjdy.get(0).getDJDYID();
										String sqlZJGC = MessageFormat.format(
												" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and qllx=''23''",
												ycdjdyid);
										long zjgcCount = baseCommonDao.getCountByFullSql(sqlZJGC);
										String sqlzjgc2 = MessageFormat.format(
												" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and qllx=''4'' ",
												ycdjdyid);
										long zjcCount2 = baseCommonDao.getCountByFullSql(sqlzjgc2);
										String	zjgcStatus;
										if(zjgcCount > 0 && zjcCount2==0){
												zjgcStatus="是";
										}else{
											zjgcStatus="否";
										}
										 
										// 判断完现状层中的信息，接着判断办理中的信息
										if (!(zjgcCount > 0)) {
											String sqlzjgc = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.qllx='23' and c.djdyid='"
													+ ycdjdyid + "' and a.sfdb='0' ";
											long count = baseCommonDao.getCountByFullSql(sqlzjgc);
											if(count > 0 && zjcCount2==0){
												zjgcStatus="在建工程抵押登记办理中";
										}else{
											zjgcStatus="否";
										}
											 
										}
										if(zjgcStatus=="是"){
											adddyrxzInfo(map);
										}else if(zjgcStatus=="在建工程抵押登记办理中"){
											adddyrgzInfo(map);
										}
										map.put("ZJGC", zjgcStatus);
 
									}
							
						}
					
					}
				}
			}
		}
				
	}
}
