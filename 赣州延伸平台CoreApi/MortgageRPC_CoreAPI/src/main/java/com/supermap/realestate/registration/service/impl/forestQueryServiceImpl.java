package com.supermap.realestate.registration.service.impl;

import java.beans.PropertyDescriptor;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.hibernate.cfg.annotations.Nullability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.QueryClass.QLInfo;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.realestate.registration.model.BDCS_PARTIALLIMIT;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_LS;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SYQZD_LS;
import com.supermap.realestate.registration.model.BDCS_SYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.interfaces.Building;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.service.forestQueryService;
import com.supermap.realestate.registration.tools.ConverIdCard;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.DateUtil;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.YXBZ;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;

/**
 * @author chenbo
 * 2017年5月9日
 */
@Service("forestQueryService")
public class forestQueryServiceImpl implements forestQueryService{
	@Autowired
	private CommonDao baseCommonDao;
	
	/**
	 * 林地信息查询
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Message queryForest(Map<String, String> queryvalues, int page, int rows, boolean iflike, String sort, String order) {
		Message msg = new Message(); 
		String fsqlcfwh = queryvalues.get("FSQL.CFWH");
		String cxzt = queryvalues.get("CXZT");
		queryvalues.remove("CXZT");
		long count = 0;
		List<Map> listresult = null;
		/* ===============1、先获取实体对应的表名=================== */
		String unitentityName = "BDCK.BDCS_SLLM_XZ";

		/* ===============2、再获取表名+'_'+字段名=================== */
		String dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,'05' AS BDCDYLX ,'林地' AS BDCDYLXMC ";
		String qlfieldsname = "QL.QLID,QL.BDCQZH,QL.DJSJ";
		String fsqlfieldsname = "FSQL.FSQLID,FSQL.CFWH";

		/* ===============3、构造查询语句=================== */
		/* SELECT 字段部分 */
		StringBuilder builder2 = new StringBuilder();
		builder2.append("select ").append(dyfieldsname).append(",").append(qlfieldsname);
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
				if ((name.equals("CFZT") && value.equals("0")) || (name.equals("DYZT") && value.equals("0"))) {
					continue;
				}

				// 权利人，权利人的条件比较特殊，where里边又套了where
				if (name.startsWith("QLR.")) {
					if (haveqlr)
						qlrbuilder.append(" and ");
					if (iflike) {
						qlrbuilder.append(" instr(" + name + ",'" + value + "')>0 ");
					} else {
						if (name.equals("QLR.ZJH") && value.length() == 18) {
							String oldCard = ConverIdCard.getOldIDCard(value);
							if (oldCard.length() != 15) {
								qlrbuilder.append(" " + name + "='" + value + "' ");
							} else {
								qlrbuilder.append(" (" + name + "='" + value + "' or ");
								qlrbuilder.append(" " + name + "='" + oldCard + "') ");
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
					if (name.equals("DJSJ_Q")) {
						builder3.append("  QL.DJSJ >=to_date('"+queryvalues.get("DJSJ_Q")+"','yyyy-mm-dd') ");
					}else if (name.equals("DJSJ_Z")) {
						builder3.append("   QL.DJSJ <=(to_date('"+queryvalues.get("DJSJ_Z")+"','yyyy-mm-dd')+1) ");
					}else
					builder3.append(" instr(" + name + ",'" + value + "')>0 ");
				}else if (name.equals("DJSJ_Q")) {
					builder3.append("  QL.DJSJ >=to_date('"+queryvalues.get("DJSJ_Q")+"','yyyy-mm-dd') ");
				}else if (name.equals("DJSJ_Z")) {
					builder3.append("   QL.DJSJ <=(to_date('"+queryvalues.get("DJSJ_Z")+"','yyyy-mm-dd')+1) ");
				}else {
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
			builder3.append(" exists(select 1 from BDCK.BDCS_QLR_XZ QLR  where "+ qlrbuilder.toString()+"   AND QLR.QLID=QL.QLID)  ");
			havecondition = true;
		}

		String wherestr = builder3.toString();
		if (wherestr.trim().toUpperCase().endsWith("WHERE")) {
			wherestr = "";
		}
		String fromSql = " from (select ql.qlid " + fromstr + wherestr + ")";
		String fullSql = selectstr + fromstr + wherestr;
		/* 排序 条件语句 */
		if(!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order))
		{
			if(sort.toUpperCase().equals("ZL"))
				sort = "DY.ZL";
			if(sort.toUpperCase().equals("BDCDYH"))
				sort = "DY.BDCDYH";
			if(sort.toUpperCase().equals("BDCQZH"))
				sort = "QL.BDCQZH";
			if(sort.toUpperCase().equals("BDCDYID"))
				sort = "DY.BDCDYID";
			fullSql = fullSql + " ORDER BY " + sort + " " + order;
		}
		/* 为了提高效率，先不查询符合条件的记录总数，将来改为异步获取 */
		CommonDao dao = baseCommonDao;
		Long test1 = System.currentTimeMillis(); 
		if("2".equals(cxzt)){
			fromSql = fromSql.replaceAll("_xz", "_ls").replaceAll("_XZ", "_LS");
			fullSql = fullSql.replaceAll("_xz", "_ls").replaceAll("_XZ", "_LS");
		}
		count = dao.getCountByFullSql(fromSql);
		listresult = dao.getPageDataByFullSql(fullSql, page, rows);
		addRightsHolderInfo(listresult);
		addLimitZDStatus(listresult);
		addDyCfDetails(listresult);
		Long test8 = System.currentTimeMillis();
		System.out.println("消耗时间为："+(test8-test1));
		// 格式化结果中的登簿时间
		for (Map map : listresult) {
			if (map.containsKey("DJSJ")) {
				map.put("DJSJ",StringHelper.FormatYmdhmsByDate(map.get("DJSJ")));
			}
		}
		msg.setTotal(count);
		msg.setRows(listresult);
		return msg;
	}
	
	@SuppressWarnings("unchecked")
	private void addRightsHolderInfo(@SuppressWarnings("rawtypes") List<Map> result) {
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
	
	/**
	 * 林地的状态赋值
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addLimitZDStatus(List<Map> result) {
		if (result != null && result.size() > 0) {
			for (Map map : result) {
				if (map.containsKey("DJDYID")) {
					String djdyid = (String) map.get("DJDYID");
					if (djdyid != null) {
						String sqlMortgage = MessageFormat.format(" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and qllx=''23''", djdyid);
						String sqlSeal = MessageFormat.format(" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and " + "djlx=''800'' and qllx=''99''", djdyid);
						String sqlObjection = MessageFormat.format("  from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''600'' ", djdyid);
						long mortgageCount = baseCommonDao.getCountByFullSql(sqlMortgage);
						long SealCount = baseCommonDao.getCountByFullSql(sqlSeal);
						long ObjectionCount = baseCommonDao.getCountByFullSql(sqlObjection);

						String mortgageStatus = mortgageCount > 0 ? "已抵押": "无抵押";
						String sealStatus = SealCount > 0 ? "已查封" : "无查封";

						// 判断完现状层中的查封信息，接着判断办理中的查封信息
						if (!(SealCount > 0)) {
							String sqlSealing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and  b.djdyid=c.djdyid WHERE c.djlx='800' AND c.qllx='99' and c.djdyid='"
									+ djdyid + "' and a.sfdb='0' ";
							long count = baseCommonDao.getCountByFullSql(sqlSealing);
							sealStatus = count > 0 ? "查封办理中" : "无查封";
						}

						// 判断完现状层中的查封信息，接着判断办理中的查封信息
						if (!(mortgageCount > 0)) {
							String sqlmortgageing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='100' AND c.qllx='23' and c.djdyid='"
									+ djdyid + "' and a.sfdb='0' ";
							long count = baseCommonDao.getCountByFullSql(sqlmortgageing);
							mortgageStatus = count > 0 ? "抵押办理中" : "无抵押";
						}

						String objectionStatus = ObjectionCount > 0 ? "有异议" : "无异议";
						map.put("DYZT", mortgageStatus);
						map.put("CFZT", sealStatus);
						map.put("YYZT", objectionStatus);
						
						//增加限制状态
						String sqlLimit = MessageFormat.format("  from BDCK.BDCS_DYXZ where bdcdyid=''{0}'' and yxbz=''1'' ", map.get("BDCDYID"));

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
	
	//登记查封状态的详情信息
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void addDyCfDetails(List<Map> result){
    	if (result != null && result.size() > 0) {
			for (Map map : result) {
				if(!StringUtils.isEmpty(map.get("BDCDYH"))&&!StringUtils.isEmpty(map.get("DJDYID")))
				{
					String dycfdetailssql = MessageFormat.format("select fsql.cfjg,fsql.cfwh,fsql.dyr,ql.qlqssj,ql.qljssj,ql.qllx,ql.djlx "
							+ " from bdck.bdcs_ql_xz ql left join bdck.bdcs_fsql_xz fsql on ql.qlid=fsql.qlid where ql.bdcdyh=''{0}'' and ql.djdyid=''{1}'' and ql.qllx in(''99'',''23'') ",
							map.get("BDCDYH"),map.get("DJDYID"));
					
					List<Map> dycfs=baseCommonDao.getDataListByFullSql(dycfdetailssql);
					for (Map dycf : dycfs) {
						if("800".equals(dycf.get("DJLX"))&&"99".equals(dycf.get("QLLX"))){
							map.put("CFJG", dycf.get("CFJG"));
							map.put("CFWH", dycf.get("CFWH"));
							map.put("CFQX", DateUtil.FormatByDatetime(dycf.get("QLQSSJ"))+" 至  "+DateUtil.FormatByDatetime(dycf.get("QLJSSJ")));
						}
						if("23".equals(dycf.get("QLLX"))){
							map.put("DYR", dycf.get("DYR"));
							map.put("DYQX", DateUtil.FormatByDatetime(dycf.get("QLQSSJ"))+" 至  "+DateUtil.FormatByDatetime(dycf.get("QLJSSJ")));
						}
					}
				}
			}
    	}
    }
    
	/* 
	 * 获取单元信息
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Map> getForestInfo(String bdcdyid, String bdcdylx) {
		List<Map> list = new ArrayList();
		RealUnit unit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.LS, bdcdyid);
		if (unit == null) {
			unit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.GZ, bdcdyid);
		}
		if (unit != null && BDCDYLX.LD.equals(BDCDYLX.initFrom(bdcdylx))) {
			list.add(beanToMap(unit));
		}
		String zdtype = null;
		String landbdcdyid = null;
		if (list != null && list.size() > 0) {
			if (list.get(0).get("zdbdcdyid") != null && list.get(0).get("zdbdcdyid") != "") {
				landbdcdyid = list.get(0).get("zdbdcdyid").toString();
			}
		}
		List<BDCS_DJDY_XZ> djdys = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='" + bdcdyid + "'");
		if (djdys != null && djdys.size() > 0) {
			String djdyid = djdys.get(0).getDJDYID();
			StringBuilder builderXZ = new StringBuilder();
			builderXZ.append("SELECT QLLX FROM BDCK.BDCS_QL_LS WHERE DJDYID='");
			builderXZ.append(djdyid);
			builderXZ.append("'");
			List<Map> xzqls = baseCommonDao.getDataListByFullSql(builderXZ.toString());
			if (xzqls != null && xzqls.size() > 0) {
				for (int iql = 0; iql < xzqls.size(); iql++) {
					String qllx = StringHelper.formatObject(xzqls.get(iql).get("QLLX"));
					if(QLLX.TDCBJYQ_SLLMSYQ.Value.equals(qllx) || QLLX.LDSHYQ_SLLMSYQ.Value.equals(qllx)){
						zdtype = "01"; //SYQZD
					}else if(QLLX.LDSYQ.Value.equals(qllx) || QLLX.LDSYQ_SLLMSYQ.Value.equals(qllx)){
						zdtype = "02"; //SHYQZD
					}
				}
			}
		}
		if (landbdcdyid != null && zdtype != null) {
			List<Map> landlist = GetLandInfo(landbdcdyid, zdtype);
			list.add(landlist.get(0));
		}
		return list;
	}
	
	/** 根据宗地不动产单元ID获取宗地单元信息
	 * @param bdcdyid
	 * @param bdcdylx
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> GetLandInfo(String bdcdyid, String bdcdylx) {
		List<Map> list = new ArrayList();
		RealUnit landUnit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx),DJDYLY.LS, bdcdyid);
		if (landUnit == null) {
			landUnit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.GZ,bdcdyid);
		}
		list.add(beanToMap(landUnit));
		return list;
	}
	
	/* 
	 * 获取权利信息
	 */
	@Override
	public QLInfo GetQLInfo_XZ(String bdcdyid) {
		QLInfo qlinfo = new QLInfo();
		Rights ql = getQLInfo_XZ(bdcdyid);
		qlinfo.setql(ql);
		if (ql != null) {
			SubRights fsql = RightsTools.loadSubRightsByRightsID(DJDYLY.XZ, ql.getId());
			qlinfo.setfsql(fsql);
			List<RightsHolder> qlrlist = RightsHolderTools.loadRightsHolders(DJDYLY.XZ, ql.getId());
			qlinfo.setqlrlist(qlrlist);
		}
		return qlinfo;
	}
	
	private Rights getQLInfo_XZ(String bdcdyid) {
		Rights ql = null;
		List<BDCS_DJDY_XZ> djdys = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='" + bdcdyid + "'");
		if (djdys != null && djdys.size() > 0) {
			String djdyid = djdys.get(0).getDJDYID();
			BDCDYLX lx = BDCDYLX.initFrom(djdys.get(0).getBDCDYLX());
			if (BDCDYLX.LD.equals(lx)) {
				List<Rights> qls = RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='" + djdyid + "' and QLLX IN ('10','11','12','36')");
				if (!StringHelper.isEmpty(qls) && qls.size() > 0) {
					ql = qls.get(0);
				}
			}
		}
		return ql;
	}
	
	/* 
	 * 获取抵押信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<List<Map>> GetDYDJList_XZ(String bdcdyid) {
		List<List<Map>> result = new ArrayList<List<Map>>();
		List<Map> qllist = new ArrayList();
		List<BDCS_DJDY_XZ> djdys = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='" + bdcdyid + "'");
		// 查询历史权利信息
		if (djdys != null && djdys.size() > 0) {
			String djdyid = djdys.get(0).getDJDYID();
			StringBuilder builderXZ = new StringBuilder();
			builderXZ.append("SELECT QLID,YWH,DJLX,QLLX,DJSJ FROM BDCK.BDCS_QL_XZ WHERE DJDYID='");
			builderXZ.append(djdyid);
			builderXZ.append("' AND QLLX='23' ORDER BY DJSJ ASC NULLS FIRST");
			List<Map> xzqls = baseCommonDao.getDataListByFullSql(builderXZ.toString());
			int sx = 1;
			if (xzqls != null && xzqls.size() > 0) {
				for (int iql = 0; iql < xzqls.size(); iql++) {
					String qlid = StringHelper.formatObject(xzqls.get(iql).get("QLID"));
					String djlx = StringHelper.formatObject(xzqls.get(iql).get("DJLX"));
					String ywh = StringHelper.formatObject(xzqls.get(iql).get("YWH"));
					Map<String, String> map = new HashMap<String, String>();
					map.put("QLID", qlid);
					map.put("YWH", ywh);
					map.put("CANCLED", "false");
					map.put("DJLX",StringHelper.isEmpty(djlx) ? "首次登记" : DJLX.initFrom(djlx).Name);
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
	
	/* 
	 * 获取查封信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<List<Map>> GetCFDJList_XZ(String bdcdyid) {
		List<List<Map>> result = new ArrayList<List<Map>>();
		List<Map> qllist = new ArrayList();
		List<BDCS_DJDY_XZ> djdys = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='" + bdcdyid + "'");
		// 查询历史权利信息
		if (djdys != null && djdys.size() > 0) {
			String djdyid = djdys.get(0).getDJDYID();
			StringBuilder builderXZ = new StringBuilder();
			builderXZ.append("SELECT QLID,YWH,DJLX,QLLX FROM BDCK.BDCS_QL_XZ WHERE DJDYID='");
			builderXZ.append(djdyid);
			builderXZ.append("' AND QLLX='99' AND DJLX='800' ORDER BY DJSJ");
			List<Map> xzqls = baseCommonDao.getDataListByFullSql(builderXZ.toString());
			if (xzqls != null && xzqls.size() > 0) {
				for (int iql = 0; iql < xzqls.size(); iql++) {
					String qlid = StringHelper.formatObject(xzqls.get(iql).get("QLID"));
					String ywh = StringHelper.formatObject(xzqls.get(iql).get("YWH"));
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
	
	/* 
	 * 获取异议登记信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<List<Map>> GetYYDJList_XZ(String bdcdyid) {
		List<List<Map>> result = new ArrayList<List<Map>>();
		List<Map> qllist = new ArrayList();
		List<BDCS_DJDY_XZ> djdys = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='" + bdcdyid + "'");
		// 查询历史权利信息
		if (djdys != null && djdys.size() > 0) {
			String djdyid = djdys.get(0).getDJDYID();
			StringBuilder builderXZ = new StringBuilder();
			builderXZ.append("SELECT QLID,YWH,DJLX,QLLX FROM BDCK.BDCS_QL_XZ WHERE DJDYID='");
			builderXZ.append(djdyid);
			builderXZ.append("' AND QLLX='99' AND DJLX='600' ORDER BY DJSJ");
			List<Map> xzqls = baseCommonDao.getDataListByFullSql(builderXZ.toString());
			if (xzqls != null && xzqls.size() > 0) {
				for (int iql = 0; iql < xzqls.size(); iql++) {
					String qlid = StringHelper.formatObject(xzqls.get(iql).get("QLID"));
					String ywh = StringHelper.formatObject(xzqls.get(iql).get("YWH"));
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
	
	/* 
	 * 获取其他限制登记信息
	 */
	@Override
	public List<BDCS_DYXZ> GetDYXZList_XZ(String bdcdyid) {
		List<BDCS_DYXZ> dyxzs = baseCommonDao.getDataList(BDCS_DYXZ.class,"BDCDYID='" + bdcdyid + "' AND YXBZ IN ('1','2')");
		return dyxzs;
	}
	
	/* 
	 * 获取历史产权信息
	 */
	@SuppressWarnings("rawtypes")
	public List<List<Map>> GetQLList(String bdcdyid) {
		List<List<Map>> list = new ArrayList<List<Map>>();
		String JudgeDJZ = ConfigHelper.getNameByValue("JudgeDJZ");// 值为SFDB,则判断是否项目办理中使用xmxx中SFDB（是否登簿）判断；若值为SFBJ,则使用xmxx中的SFBJ（是否办结）判断
		if (StringHelper.isEmpty(JudgeDJZ)) {
			JudgeDJZ = "SFDB";
		}
		if (!StringHelper.isEmpty(bdcdyid)) {
			String djdyid = "";
			BDCDYLX dylx = null;
			List<BDCS_DJDY_LS> djdys_ls = baseCommonDao.getDataList(BDCS_DJDY_LS.class, "BDCDYID='" + bdcdyid + "'");
			if (djdys_ls != null && djdys_ls.size() > 0) {
				djdyid = djdys_ls.get(0).getDJDYID();
				dylx = BDCDYLX.initFrom(djdys_ls.get(0).getBDCDYLX());
			} else {
				List<BDCS_DJDY_GZ> djdys_gz = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "BDCDYID='" + bdcdyid + "'");
				if (djdys_gz != null && djdys_gz.size() > 0) {
					djdyid = djdys_gz.get(0).getDJDYID();
					dylx = BDCDYLX.initFrom(djdys_gz.get(0).getBDCDYLX());
				}
			}
			HashMap<String, String> bdcdyid_djdyid = new HashMap<String, String>();
			HashMap<String, BDCDYLX> bdcdyid_bdcdylx = new HashMap<String, BDCDYLX>();
			if (BDCDYLX.LD.equals(dylx)) {
				bdcdyid_djdyid.put(bdcdyid, djdyid);
				bdcdyid_bdcdylx.put(bdcdyid, dylx);
			}
			List<Map> qlinfo_list_all = new ArrayList<Map>();
			List<String> list_ywh_blz = new ArrayList<String>();
			for (Entry<String, String> m : bdcdyid_djdyid.entrySet()) {
				List<String> list_ywh = GetYWHListInBLZ(m.getValue(), bdcdyid_bdcdylx.get(m.getKey()), JudgeDJZ);
				for (String ywh : list_ywh) {
					list_ywh_blz.add(ywh);
				}
			}
			for (Entry<String, String> m : bdcdyid_djdyid.entrySet()) {
				List<HashMap<String, String>> qlinfo_list = GetRightInfoList(m.getValue(), bdcdyid_bdcdylx.get(m.getKey()), JudgeDJZ, list_ywh_blz);
				for (HashMap<String, String> qlinfo : qlinfo_list) {
					qlinfo_list_all.add(qlinfo);
				}
			}
			qlinfo_list_all = SortRightInfoList(qlinfo_list_all);
			list.add(qlinfo_list_all);
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
	private List<String> GetYWHListInBLZ(String djdyid, BDCDYLX dylx, String JudgeDJZ) {
		List<String> list_ywh_blz = new ArrayList<String>();
		if (StringHelper.isEmpty(djdyid)) {
			return list_ywh_blz;
		}
		StringBuilder builderGZ = new StringBuilder();
		builderGZ.append("SELECT QL.XMBH,QL.QLID,QL.YWH,QL.DJLX,QL.QLLX,QL.DJSJ ");
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
		List<Map> gzqls = baseCommonDao.getDataListByFullSql(builderGZ.toString());
		if (gzqls != null && gzqls.size() > 0) {
			for (int iql = 0; iql < gzqls.size(); iql++) {
				String xmbh = StringHelper.formatObject(gzqls.get(iql).get("XMBH"));
				ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
				list_ywh_blz.add(info.getProject_id());
			}
		}
		return list_ywh_blz;
	}
	
	/** 根据登记单元ID获取权利列表
	 * @param djdyid
	 * @param dylx
	 * @param JudgeDJZ
	 * @param list_ywh_blz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private List<HashMap<String, String>> GetRightInfoList(String djdyid, BDCDYLX dylx, String JudgeDJZ, List<String> list_ywh_blz) {
		List<HashMap<String, String>> list_qlinfo = new ArrayList<HashMap<String, String>>();
		if (StringHelper.isEmpty(djdyid)) {
			return list_qlinfo;
		}
		
		StringBuilder builderLS = new StringBuilder();
		builderLS.append("SELECT QLID,YWH,DJLX,QLLX,DJSJ,CASE WHEN EXISTS(SELECT 1 FROM BDCK.BDCS_QL_XZ XZQL WHERE XZQL.QLID=LSQL.QLID) THEN 'YGD' ELSE 'YZX' END AS DJZT FROM BDCK.BDCS_QL_LS LSQL WHERE DJDYID='");
		builderLS.append(djdyid);
		builderLS.append("'");
		builderLS.append(" ORDER BY DJSJ");
		List<Map> lsqls = baseCommonDao.getDataListByFullSql(builderLS.toString());
		if (lsqls != null && lsqls.size() > 0) {
			for (int iql = 0; iql < lsqls.size(); iql++) {
				String qlid = StringHelper.formatObject(lsqls.get(iql).get("QLID"));
				String qllx = StringHelper.formatObject(lsqls.get(iql).get("QLLX"));
				String djlx = StringHelper.formatObject(lsqls.get(iql).get("DJLX"));
				String project_id = StringHelper.formatObject(lsqls.get(iql).get("YWH"));
				String DJZT = StringHelper.formatObject(lsqls.get(iql).get("DJZT"));
				Object objDJSJ = lsqls.get(iql).get("DJSJ");
				String ywh = "";
				if (!StringHelper.isEmpty(project_id)) {
					BDCS_XMXX XMXX = Global.getXMXX(project_id);
					if (XMXX != null) {
						if (("SFDB").equals(JudgeDJZ) && !("1").equals(XMXX.getSFDB())) {
							continue;
						} else if (("SFBJ").equals(JudgeDJZ) && !("1").equals(XMXX.getSFBJ())) {
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
				SubRights lsfsql = RightsTools.loadSubRightsByRightsID(DJDYLY.LS, qlid);
				if (lsfsql != null) {
					String ZXYWH = lsfsql.getZXDYYWH();
					if (!StringHelper.isEmpty(ZXYWH)) {
						BDCS_XMXX XMXX = Global.getXMXX(ZXYWH);
						if (XMXX != null) {
							if (("SFDB").equals(JudgeDJZ) && !("1").equals(XMXX.getSFDB())) {
								continue;
							} else if (("SFBJ").equals(JudgeDJZ) && !("1").equals(XMXX.getSFBJ())) {
								if(list_ywh_blz.contains(XMXX.getPROJECT_ID())){
									DJZT="YGD";
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
				String djsj_value = StringHelper.FormatDateOnType(objDJSJ, "yyyy-MM-dd HH-mm-ss");
				String djsj = StringHelper.FormatDateOnType(objDJSJ, "yyyy年MM月dd日");
				map.put("DJSJ", djsj_value);
				map.put("DJZT", DJZT);

				String ywlx = GetYWLX(djlx, qllx, dylx);
				String fulSql = "SELECT DISTINCT PRO.PRODEF_NAME,BASE.DJLX FROM BDCK.BDCS_QL_LS LSQL INNER JOIN BDC_WORKFLOW.WFI_PROINST PRO ON PRO.FILE_NUMBER=LSQL.YWH "
						+ " INNER JOIN BDC_WORKFLOW.WFD_MAPPING M ON M.WORKFLOWCODE = PRO.PROINST_CODE "
						+ " INNER JOIN BDCK.T_BASEWORKFLOW BASE ON BASE.ID=M.WORKFLOWNAME "
						+ " WHERE LSQL.YWH IS NOT NULL AND LSQL.QLLX=23 AND PRO.FILE_NUMBER IS NOT NULL "
						+ " AND (BASE.DJLX='500' OR BASE.DJLX='300') AND BASE.QLLX='23' AND BASE.UNITTYPE='05' AND LSQL.QLID=''{0}''";
				fulSql = MessageFormat.format(fulSql, qlid);
				List<Map> list = baseCommonDao.getDataListByFullSql(fulSql);
				if(list!=null && list.size() > 0 && ywlx.indexOf("-") == -1){
					ywlx = DJLX.initFrom(list.get(0).get("DJLX")+"").Name + "-"  + ywlx;
				}
				String title = ywlx + "(" + ywh + ")-(" + djsj + ")";// 业务类型（业务号）-（登记时间）
				map.put("TITLE", title);
				String qlType = GetQLType(djlx, qllx);
				map.put("QLTYPE", qlType);
				list_qlinfo.add(map);
			}
		}
		
		StringBuilder builderGZ = new StringBuilder();
		builderGZ.append("SELECT QL.XMBH,QL.QLID,QL.YWH,QL.DJLX,QL.QLLX,QL.DJSJ,XMXX.YXBZ ");
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
		List<Map> gzqls = baseCommonDao.getDataListByFullSql(builderGZ.toString());
		if (gzqls != null && gzqls.size() > 0) {
			for (int iql = 0; iql < gzqls.size(); iql++) {
				String qlid = StringHelper.formatObject(gzqls.get(iql).get("QLID"));
				String qllx = StringHelper.formatObject(gzqls.get(iql).get("QLLX"));
				String xmbh = StringHelper.formatObject(gzqls.get(iql).get("XMBH"));
				String xmyxbz = StringHelper.formatObject(gzqls.get(iql).get("YXBZ"));
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
				map.put("SLSJ", StringHelper.FormatDateOnType(info.getSlsj(),"yyyy-MM-dd HH-mm-ss"));
				map.put("YWH", ywh);
				map.put("DJZT", "DJZ");
				if(YXBZ.无效.Value.equals(xmyxbz)){
					map.put("DJZT", "YTJ");
				}
				String djsj_value = StringHelper.FormatDateOnType(objDJSJ,"yyyy-MM-dd HH-mm-ss");
				map.put("DJSJ", djsj_value);
				String slsj = StringHelper.FormatDateOnType(info.getSlsj(),"yyyy年MM月dd日");
				String ywlx = GetYWLX(djlx, qllx, dylx);
				String fulSql = "SELECT DISTINCT PRO.PRODEF_NAME,BASE.DJLX FROM BDCK.BDCS_QL_LS LSQL INNER JOIN BDC_WORKFLOW.WFI_PROINST PRO ON PRO.FILE_NUMBER=LSQL.YWH "
						+ " INNER JOIN BDC_WORKFLOW.WFD_MAPPING M ON M.WORKFLOWCODE = PRO.PROINST_CODE "
						+ " INNER JOIN BDCK.T_BASEWORKFLOW BASE ON BASE.ID=M.WORKFLOWNAME "
						+ " WHERE LSQL.YWH IS NOT NULL AND LSQL.QLLX=23 AND PRO.FILE_NUMBER IS NOT NULL "
						+ " AND (BASE.DJLX='500' OR BASE.DJLX='300') AND BASE.QLLX='23' AND BASE.UNITTYPE='05' AND LSQL.QLID=''{0}''";
				fulSql = MessageFormat.format(fulSql, qlid);
				List<Map> list = baseCommonDao.getDataListByFullSql(fulSql);
				if(list != null && list.size() > 0 && ywlx.indexOf("-") == -1){
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
		return list_qlinfo;
	}
	
	/** 获取业务类型
	 * @param djlx
	 * @param qllx
	 * @param dylx
	 * @return
	 */
	private String GetYWLX(String djlx, String qllx, BDCDYLX dylx) {
		String ywlx = "";
		if (DJLX.CFDJ.Value.equals(djlx) || DJLX.YYDJ.Value.equals(djlx)) {
			ywlx = DJLX.initFrom(djlx).Name;
		} else {
			if (DJLX.YGDJ.Value.equals(djlx) && "99".equals(qllx)) {
				ywlx = "转移预告登记";
			} else if (DJLX.YGDJ.Value.equals(djlx) && QLLX.DIYQ.Value.equals(qllx)) {
				ywlx = "抵押预告登记";
			} else if (QLLX.LDSYQ.Value.equals(qllx) || QLLX.LDSYQ_SLLMSYQ.Value.equals(qllx) || QLLX.TDCBJYQ_SLLMSYQ.Value.equals(qllx) || QLLX.LDSHYQ_SLLMSYQ.Value.equals(qllx)) {
				if(StringHelper.isEmpty(DJLX.initFrom(djlx))){
					ywlx = "----" +"-" + QLLX.initFrom(qllx).Name;
				}else{
					if(djlx != null && !djlx.equals("")){
					   ywlx = DJLX.initFrom(djlx).Name + "-" +QLLX.initFrom(qllx).Name;
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
			} else {
				if(StringHelper.isEmpty(DJLX.initFrom(djlx))){
					ywlx = "----" ;
				}else{
					if(djlx != null && !djlx.equals("")){
					  ywlx = DJLX.initFrom(djlx).Name;
					}
				}
				if(StringHelper.isEmpty(QLLX.initFrom(qllx))){
					ywlx = ywlx + "-" + "----" ;
				}else{
					ywlx = ywlx + QLLX.initFrom(qllx).Name;
				}
			}
		}
		return ywlx;
	}
	
	/** 获取权利类型
	 * @param djlx
	 * @param qllx
	 * @return
	 */
	private String GetQLType(String djlx, String qllx) {
		String type = "SYQ";
		if(QLLX.LDSYQ.Value.equals(qllx) || QLLX.LDSYQ_SLLMSYQ.Value.equals(qllx)){
			type = "SHYQ";
		}else if (QLLX.DIYQ.Value.equals(qllx)) {
			type = "DYQ";
		} else if (DJLX.CFDJ.Value.equals(djlx)) {
			type = "CFDJ";
		} else if (DJLX.YYDJ.Value.equals(djlx)) {
			type = "YYDJ";
		}
		return type;
	}
	
	/** 对权利列表进行排序
	 * @param qlinfo_list_all
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private List<Map> SortRightInfoList(List<Map> qlinfo_list_all) {
		// 查询登记过程按照时间排序 ，正在进行的放在最上面，没时间的放在最下面
		if (qlinfo_list_all != null && qlinfo_list_all.size() > 0) {
			// 2.对不为空的进行排序
			final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
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
						if (judge_slsj_zt.contains(djzti) && !judge_slsj_zt.contains(djztj)) {
							return -1;
						}
						if (judge_slsj_zt.contains(djztj) && !judge_slsj_zt.contains(djzti)) {
							return 1;
						}
					}
					if (judge_slsj_zt.contains(djzti) && judge_slsj_zt.contains(djztj)) {
						java.util.Date slsji = null;
						java.util.Date slsjj = null;
						try {
							if (!StringHelper.isEmpty(mapi.get("SLSJ").toString())) {
								slsji = sdf1.parse(mapi.get("SLSJ").toString());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							if (!StringHelper.isEmpty(mapj.get("SLSJ").toString())) {
								slsjj = sdf1.parse(mapj.get("SLSJ").toString());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (StringHelper.isEmpty(slsji) || (!StringHelper.isEmpty(datej) && (StringHelper.isEmpty(datei)||datei.before(slsjj)))) {
							return 1;
						} else if (!StringHelper.isEmpty(datei)&&datei.equals(slsjj)) {
							return 0;
						}
						return -1;
					}
					if (StringHelper.isEmpty(datei) || (!StringHelper.isEmpty(datej) && datei.before(datej))) {
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
	
	/* 
	 * 根据权利ID获取所有权的信息
	 */
	@Override
	public QLInfo GetQLInfo(String qlid, String djzt) {
		QLInfo qlinfo = new QLInfo();
		if (("DJZ").equals(djzt)) {
			Rights ql = RightsTools.loadRights(DJDYLY.GZ, qlid);
			qlinfo.setql(ql);
			SubRights fsql = RightsTools.loadSubRightsByRightsID(DJDYLY.GZ, qlid);
			qlinfo.setfsql(fsql);
			List<RightsHolder> qlrlist = RightsHolderTools.loadRightsHolders(DJDYLY.GZ, qlid);
			qlinfo.setqlrlist(qlrlist);
		} else {
			Rights ql = RightsTools.loadRights(DJDYLY.LS, qlid);
			qlinfo.setql(ql);
			SubRights fsql = RightsTools.loadSubRightsByRightsID(DJDYLY.LS,qlid);
			qlinfo.setfsql(fsql);
			List<RightsHolder> qlrlist = RightsHolderTools.loadRightsHolders(DJDYLY.LS, qlid);
			qlinfo.setqlrlist(qlrlist);
			if("1".equals(ql.getISPARTIAL())){
				List<BDCS_PARTIALLIMIT> partiallimit = baseCommonDao.getDataList(BDCS_PARTIALLIMIT.class, " LIMITQLID='"+ qlid +"' AND LIMITTYPE='800'");
				if(partiallimit!=null && partiallimit.size() > 0){
					qlinfo.setLimitqlr(RightsHolderTools.loadRightsHolder(DJDYLY.LS, partiallimit.get(0).getQLRID()));
				}
			}
		}
		return qlinfo;
	}
	
	/* 
	 * 根据权利ID获取限制信息
	 */
	@Override
	public BDCS_DYXZ GetDYXZInfo(String id) {
		BDCS_DYXZ dyxz = baseCommonDao.get(BDCS_DYXZ.class, id);
		return dyxz;
	}
	
	//将实体bean转换为Map类型
	public static Map<String, Object> beanToMap(Object obj) {
		Map<String, Object> params = new HashMap<String, Object>(0);
		try {
			PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
			PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
			for (int i = 0; i < descriptors.length; i++) {
				String name = descriptors[i].getName();
				if (!"class".equals(name)) {
					params.put(name.toLowerCase(), propertyUtilsBean.getNestedProperty(obj, name));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return params;
	}	
}
