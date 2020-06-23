package com.supermap.realestate_gx.registration.service.impl;

import com.supermap.realestate.registration.ViewClass.DJFZXX;
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
import com.supermap.realestate.registration.model.BDCS_NYD_XZ;
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
import com.supermap.realestate.registration.model.BDCS_SLLM_LS;
import com.supermap.realestate.registration.model.BDCS_SLLM_XZ;
import com.supermap.realestate.registration.model.BDCS_SYQZD_LS;
import com.supermap.realestate.registration.model.BDCS_SYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZH_LS;
import com.supermap.realestate.registration.model.BDCS_ZH_XZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_LS;
import com.supermap.realestate.registration.model.BDCS_ZRZ_LSY;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZY;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_LS;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.YC_SC_H_XZ;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.interfaces.TDYT;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.service.GZTJService;
import com.supermap.realestate.registration.tools.ConverIdCard;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CZFS;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.DateUtil;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.realestate_gx.registration.service.APIService;
import com.supermap.realestate_gx.registration.service.ConverterService;
import com.supermap.realestate_gx.registration.service.GX_Service;
import com.supermap.realestate_gx.registration.util.ConverterUtil;
import com.supermap.realestate_gx.registration.util.GX_Util;
import com.supermap.realestate_gx.registration.util.ConverterUtil.FC_TABLE;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;

import java.sql.SQLException;
import java.text.MessageFormat;

import com.supermap.wisdombusiness.web.ui.tree.State;
import com.supermap.wisdombusiness.web.ui.tree.Tree;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service("apiService")
public class APIServiceImpl implements APIService
{
	public static  JSONArray zdmapobj=null;//映射表的对象
  @Autowired
  private CommonDao baseCommonDao;
@Override
public Message queryLand(Map<String, String> queryvalues, int page,
		int rows, boolean iflike, String tdzt,String sort,String order) {
	Message msg = new Message();
	String fsqlcfwh=queryvalues.get("FSQL.CFWH");
	long count = 0;
	List<Map> listresult = null;
	/* ===============1、先获取实体对应的表名=================== */
	String unitentityName = "BDCK.BDCS_SHYQZD_XZ";

	/* ===============2、再获取表名+'_'+字段名=================== */
	String dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC,DY.ZDMJ,DY.QLXZ,DY.DJQMC ";
	String qlfieldsname = "QL.QLID,QL.BDCQZH,QL.QLLX";
	String fsqlfieldsname = "FSQL.FSQLID,FSQL.CFWH";


	if (tdzt != null && tdzt.equals("2")) {
		unitentityName = "BDCK.BDCS_SYQZD_XZ";
		dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,'01' AS BDCDYLX,'所有权宗地' AS BDCDYLXMC,DY.ZDMJ,DY.QLXZ,DY.DJQMC  ";
	}

	// 集合使用权宗地与所有权宗地
	if (tdzt != null && tdzt.equals("3")) {
		unitentityName = "(SELECT ZL,ZDDM,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC,ZDMJ,QLXZ,DJQMC   FROM BDCK.BDCS_SYQZD_XZ UNION ALL SELECT ZL,ZDDM,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC,ZDMJ,QLXZ,DJQMC FROM BDCK.BDCS_SHYQZD_XZ)";
		dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC,DY.ZDMJ,DY.QLXZ,DY.DJQMC ";
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

	//count = dao.getCountByFullSql(fromSql);
	listresult = dao.getDataListByFullSql(fullSql);
	ConvertBDCDYXX(listresult);
	addLimitStatus(listresult);
	//addRightsHolderInfo(listresult);
	//addLimitZDStatus(listresult);
	//国有土地使用权时，显示出土地上房屋状态。
	//addLimitZDStausByFw(listresult);
	//addDyCfDetails(listresult);
	//addZRZCount(listresult);
	// addQLLX(listresult);
	msg.setTotal(listresult.size());
	msg.setRows(listresult);
	return msg;
}

/**
 * @param 将查出来的权利数据转换成单元信息的数据。
 * @return
 */
private void ConvertBDCDYXX(List<Map> querylist)
{
	Map validate=new HashMap();
	List<Map> result=new ArrayList<Map>();
	for (Map map : querylist) {
		if (!map.containsKey(map.get("BDCDYID"))) {
			result.add(map);
			map.put(map.get("BDCDYID"), "");
		}
	}
	querylist.clear();
	for (Map map : result) {
		String bdcdyid = StringHelper.formatObject(StringHelper
				.isEmpty(map.get("BDCDYID")) ? "" : map.get("BDCDYID"));
		String bdcdylxcode = StringHelper.formatObject(StringHelper
				.isEmpty(map.get("BDCDYLX")) ? "" : map.get("BDCDYLX"));
		String bdcdyh = StringHelper.formatObject(StringHelper
				.isEmpty(map.get("BDCDYH")) ? "" : map.get("BDCDYH"));
		String djdyid = StringHelper.formatObject(StringHelper
				.isEmpty(map.get("DJDYID")) ? "" : map.get("DJDYID"));
		BDCDYLX bdcdylx=BDCDYLX.initFrom(bdcdylxcode);
		Map newMap=new HashMap();
		 RealUnit u=null;
		 if(bdcdylx!=null&&!StringUtils.isEmpty(bdcdyid))
		    u=UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, bdcdyid);//初始化不动产单元信息
		 if(u!=null)
		 {
			 if (BDCDYLX.SYQZD.equals(bdcdylx))// 所有权宗地不动产单元
				{
				 BDCS_SYQZD_XZ _syqzd = (BDCS_SYQZD_XZ) u;
				 Map<String, Object> syqzdMap= transBean2Map(_syqzd);
				 syqzdMap.put("SYQLX", ConstHelper.getNameByValue("SYQLX", String.valueOf(syqzdMap.get("SYQLX"))));
				 syqzdMap.put("PZYT", ConstHelper.getNameByValue("PZYT", String.valueOf(syqzdMap.get("PZYT"))));
				 syqzdMap.put("QLXZ", ConstHelper.getNameByValue("QLXZ", String.valueOf(syqzdMap.get("QLXZ"))));
				 syqzdMap.put("QLLXMC", ConstHelper.getNameByValue("QLLX", String.valueOf(syqzdMap.get("QLLX"))));
				 syqzdMap.put("QLSDFS", ConstHelper.getNameByValue("QLSDFS", String.valueOf(syqzdMap.get("QLSDFS"))));
				 syqzdMap.put("DJZT", ConstHelper.getNameByValue("DJZT", String.valueOf(syqzdMap.get("DJZT"))));
				 syqzdMap.put("DJ", ConstHelper.getNameByValue("TDDJ", String.valueOf(syqzdMap.get("DJ"))));
				 syqzdMap.put("BDCDYLX",BDCDYLX.initFrom(bdcdylxcode).Name);
				 syqzdMap.put("SHRQ", DateUtil.FormatByDatetime(syqzdMap.get("SHRQ")));
				 syqzdMap.put("CLRQ", DateUtil.FormatByDatetime(syqzdMap.get("CLRQ")));
				 syqzdMap.put("MODIFYTIME", DateUtil.FormatByDatetime(syqzdMap.get("MODIFYTIME")));
				 syqzdMap.put("CREATETIME", DateUtil.FormatByDatetime(syqzdMap.get("CREATETIME")));
				 syqzdMap.put("DCRQ", DateUtil.FormatByDatetime(syqzdMap.get("DCRQ")));
				 newMap.put("DY", syqzdMap);
			
				}
			 if (BDCDYLX.SHYQZD.equals(bdcdylx))//使用权宗地不动产单元
				{
				 BDCS_SHYQZD_XZ _shyqzd = (BDCS_SHYQZD_XZ) u;
				 UseLand land=(UseLand) u;
				 boolean flag=false;
				 String yts="";//用途集合
				 String djs="";//等级集合
				 String zyt="";//主用途
				 String zdj="";//主等级
				 boolean zytflag=false;//主用途标记
				 if(land !=null)
				 {
					 if(land.getTDYTS() !=null && land.getTDYTS().size()>0)
					 {
						for (TDYT tdyt : land.getTDYTS()) {
							if(!zytflag)
							if(!StringHelper.isEmpty(tdyt.getSFZYT()) && "1".equals(tdyt.getSFZYT()))
							{   
								zyt=tdyt.getTDYTMC();
								zdj=ConstHelper.getNameByValue("TDDJ",tdyt.getTDDJ());
								zytflag=true;
							}
							if (!flag) {
								if (!StringHelper.isEmpty(tdyt.getTDYTMC())) {
									yts = tdyt.getTDYTMC();
								}
								if (!StringHelper.isEmpty(tdyt.getTDDJ())) {
									djs = ConstHelper.getNameByValue("TDDJ",tdyt.getTDDJ());
								}
								flag = true;
							} else {
								if (!StringHelper.isEmpty(tdyt.getTDYTMC())) {
									yts = yts + " " + tdyt.getTDYTMC();
								}
								if (!StringHelper.isEmpty(tdyt.getTDDJ())) {
									djs= djs+ " "+ ConstHelper.getNameByValue("TDDJ", tdyt.getTDDJ());
								}
							}
						}
						if(!zytflag)//当没有主用途时，默认取第一个用途
						{
							 TDYT tdyt=land.getTDYTS().get(0);
							  zyt=tdyt.getTDYTMC();
							  zdj=ConstHelper.getNameByValue("TDDJ", tdyt.getTDDJ());
						}
						 
					 }
				 }
				 Map<String, Object> shyqzdMap= transBean2Map(_shyqzd);
				// shyqzdMap.put("BGZT", ConstHelper.getNameByValue("BGZT", String.valueOf(shyqzdMap.get("BGZT"))));//变更状态不知道字典值
				 shyqzdMap.put("ZDTZM", land.getZDTZM());
				 shyqzdMap.put("SYQLX", ConstHelper.getNameByValue("SYQLX", String.valueOf(shyqzdMap.get("SYQLX"))));
				 shyqzdMap.put("PZYT", ConstHelper.getNameByValue("TDYT", String.valueOf(shyqzdMap.get("PZYT"))));
				 shyqzdMap.put("QLXZ", ConstHelper.getNameByValue("QLXZ", String.valueOf(shyqzdMap.get("QLXZ"))));
				 shyqzdMap.put("QLLXMC", ConstHelper.getNameByValue("QLLX", String.valueOf(shyqzdMap.get("QLLX"))));
				 shyqzdMap.put("QLSDFS", ConstHelper.getNameByValue("QLSDFS", String.valueOf(shyqzdMap.get("QLSDFS"))));
				 shyqzdMap.put("YT", zyt);
				 shyqzdMap.put("DJZT", ConstHelper.getNameByValue("DJZT", String.valueOf(shyqzdMap.get("DJZT"))));
				 shyqzdMap.put("DJ",zdj);				 
				 shyqzdMap.put("SHRQ", DateUtil.FormatByDatetime(shyqzdMap.get("SHRQ")));
				 shyqzdMap.put("CLRQ", DateUtil.FormatByDatetime(shyqzdMap.get("CLRQ")));
				 shyqzdMap.put("MODIFYTIME", DateUtil.FormatByDatetime(shyqzdMap.get("MODIFYTIME")));
				 shyqzdMap.put("CREATETIME", DateUtil.FormatByDatetime(shyqzdMap.get("CREATETIME")));
				 shyqzdMap.put("DCRQ", DateUtil.FormatByDatetime(shyqzdMap.get("DCRQ")));
				 shyqzdMap.put("BDCDYLX",BDCDYLX.initFrom(bdcdylxcode).Name);
				 if(flag)
				 {
					 shyqzdMap.put("FJ", yts+"<br/>"+ djs);
				 }
				 newMap.put("DY", shyqzdMap);
		
				}
			 if (BDCDYLX.ZRZ.equals(bdcdylx))// 自然幢不动产单元
				{
				 BDCS_ZRZ_XZ _zrz = (BDCS_ZRZ_XZ) u;
				 Map<String, Object> zrzMap= transBean2Map(_zrz);
				 zrzMap.put("FWJG", ConstHelper.getNameByValue("FWJG", String.valueOf(zrzMap.get("FWJG"))));
				 zrzMap.put("DJZT", ConstHelper.getNameByValue("DJZT", String.valueOf(zrzMap.get("DJZT"))));
				 zrzMap.put("GHYT", ConstHelper.getNameByValue("FWYT", String.valueOf(zrzMap.get("GHYT"))));
				 zrzMap.put("BDCDYLX",BDCDYLX.initFrom(bdcdylxcode).Name);
				 zrzMap.put("MODIFYTIME", DateUtil.FormatByDatetime(zrzMap.get("MODIFYTIME")));
				 zrzMap.put("CREATETIME", DateUtil.FormatByDatetime(zrzMap.get("CREATETIME")));
				 zrzMap.put("JGRQ", DateUtil.FormatByDatetime(zrzMap.get("JGRQ")));
				 newMap.put("DY", zrzMap);
		
				}
			 if (BDCDYLX.YCZRZ.equals(bdcdylx))// 预测自然幢不动产单元
				{
				 BDCS_ZRZ_XZY _zrzy = (BDCS_ZRZ_XZY) u;
				 Map<String, Object> zrzyMap= transBean2Map(_zrzy);
				 zrzyMap.put("FWJG", ConstHelper.getNameByValue("FWJG", String.valueOf(zrzyMap.get("FWJG"))));
				 zrzyMap.put("DJZT", ConstHelper.getNameByValue("DJZT", String.valueOf(zrzyMap.get("DJZT"))));
				 zrzyMap.put("GHYT", ConstHelper.getNameByValue("FWYT", String.valueOf(zrzyMap.get("GHYT"))));
				 zrzyMap.put("BDCDYLX",BDCDYLX.initFrom(bdcdylxcode).Name);
				 zrzyMap.put("MODIFYTIME", DateUtil.FormatByDatetime(zrzyMap.get("MODIFYTIME")));
				 zrzyMap.put("CREATETIME", DateUtil.FormatByDatetime(zrzyMap.get("CREATETIME")));
				 newMap.put("DY", zrzyMap);
		
				}
			 if (BDCDYLX.HY.equals(bdcdylx))// 海域不动产单元
				{
				 BDCS_ZH_XZ _zh = (BDCS_ZH_XZ) u;
				 Map<String, Object> zhMap= transBean2Map(_zh);
				 zhMap.put("ZHTZM", ConstHelper.getNameByValue("TZM", String.valueOf(zhMap.get("ZHTZM"))));
				 zhMap.put("ZT", ConstHelper.getNameByValue("BDCDYZT", String.valueOf(zhMap.get("ZT"))));
				 zhMap.put("YHLXA", ConstHelper.getNameByValue("HYSYLXA", String.valueOf(zhMap.get("YHLXA"))));
				 zhMap.put("YHLXB", ConstHelper.getNameByValue("HYSYLXB", String.valueOf(zhMap.get("YHLXB"))));
				 zhMap.put("DJZT", ConstHelper.getNameByValue("DJZT", String.valueOf(zhMap.get("DJZT"))));
				 zhMap.put("DB", ConstHelper.getNameByValue("HYDB", String.valueOf(zhMap.get("DB"))));
				 zhMap.put("XMXX", ConstHelper.getNameByValue("XMXZ", String.valueOf(zhMap.get("XMXX"))));
				 zhMap.put("BDCDYLX",BDCDYLX.initFrom(bdcdylxcode).Name);
				 zhMap.put("MODIFYTIME", DateUtil.FormatByDatetime(zhMap.get("MODIFYTIME")));
				 zhMap.put("CREATETIME", DateUtil.FormatByDatetime(zhMap.get("CREATETIME")));
				 zhMap.put("SHRQ", DateUtil.FormatByDatetime(zhMap.get("SHRQ")));
				 zhMap.put("HCRQ", DateUtil.FormatByDatetime(zhMap.get("HCRQ")));
				 zhMap.put("CLRQ", DateUtil.FormatByDatetime(zhMap.get("CLRQ")));
				 newMap.put("DY", zhMap);
		
				}
			 if (BDCDYLX.LD.equals(bdcdylx))// 林地不动产单元
				{
				 BDCS_SLLM_XZ _sllm = (BDCS_SLLM_XZ) u;
				 Map<String, Object> sllmMap= transBean2Map(_sllm);
				 sllmMap.put("LZ", ConstHelper.getNameByValue("LZ", String.valueOf(sllmMap.get("LZ"))));
				 sllmMap.put("DJZT", ConstHelper.getNameByValue("DJZT", String.valueOf(sllmMap.get("DJZT"))));
				 sllmMap.put("QY", ConstHelper.getNameByValue("QY", String.valueOf(sllmMap.get("QY"))));
				 sllmMap.put("BDCDYLX",BDCDYLX.initFrom(bdcdylxcode).Name);
				 sllmMap.put("MODIFYTIME", DateUtil.FormatByDatetime(sllmMap.get("MODIFYTIME")));
				 sllmMap.put("CREATETIME", DateUtil.FormatByDatetime(sllmMap.get("CREATETIME")));
				 sllmMap.put("SHRQ", DateUtil.FormatByDatetime(sllmMap.get("SHRQ")));
				 newMap.put("DY", sllmMap);
			
				}
			 //注销的是因为还没有该实体类
			 if (BDCDYLX.GZW.equals(bdcdylx))// 构筑物不动产单元
				{
//				 BDCS_GZW_LS _gzw = (BDCS_GZW_LS) u;
//				 Map<String, Object> gzwMap= transBean2Map(_gzw);
//				 gzwMap.put("MODIFYTIME", DateUtil.FormatByDatetime(gzwMap.get("MODIFYTIME")));
//				 gzwMap.put("CREATETIME", DateUtil.FormatByDatetime(gzwMap.get("CREATETIME")));
//				 gzwMap.put("JGSJ", DateUtil.FormatByDatetime(gzwMap.get("JGSJ")));
//				 this.setUnit(gzwMap);
				}
			 if (BDCDYLX.QTDZW.equals(bdcdylx))// 其他定着物不动产单元
				{
//				 BDCS_QTDZW_LS _qtdzw = (BDCS_QTDZW_LS) u;
//				 Map<String, Object> qtdzwMap= transBean2Map(_qtdzw);
//				 this.setUnit(qtdzwMap);
				}
			 if (BDCDYLX.DZDZW.equals(bdcdylx))// 点状定着物不动产单元
				{
//				 BDCS_DZDZW_LS _dzdzw = (BDCS_DZDZW_LS) u;
//				 Map<String, Object> dzdzwMap= transBean2Map(_dzdzw);
//				 this.setUnit(dzdzwMap);
				}
			 if (BDCDYLX.XZDZW.equals(bdcdylx))// 线状定着物不动产单元
				{
//				 BDCS_XZDZW_LS _xzdzw = (BDCS_XZDZW_LS) u;
//				 Map<String, Object> xzdzwMap= transBean2Map(_xzdzw);
//				 this.setUnit(xzdzwMap);
				}
			 if (BDCDYLX.MZDZW.equals(bdcdylx))// 面状定着物不动产单元
				{
//				 BDCS_MZDZW_LS _mzdzw = (BDCS_MZDZW_LS) u;
//				 Map<String, Object> mzdzwMap= transBean2Map(_mzdzw);
//				 this.setUnit(mzdzwMap);
				}
			 if (BDCDYLX.H.equals(bdcdylx))//户不动产单元
				{
				 BDCS_H_XZ h = (BDCS_H_XZ) u;
				 BDCS_SHYQZD_XZ shyqzd=baseCommonDao.get(BDCS_SHYQZD_XZ.class,h.getZDBDCDYID());
			
				 Map<String, Object> hMap= transBean2Map(h);
				 if(shyqzd!=null)
					 hMap.put("ZDMJ", shyqzd.getZDMJ());
				 //hMap.put("BGZT", ConstHelper.getNameByValue("BGZT", String.valueOf(hMap.get("BGZT"))));//变更状态不知道字典值
				 hMap.put("HX", ConstHelper.getNameByValue("HX", String.valueOf(hMap.get("HX"))));
				 hMap.put("HXJG", ConstHelper.getNameByValue("HXJG", String.valueOf(hMap.get("HXJG"))));
				 hMap.put("FWXZ", ConstHelper.getNameByValue("FWXZ", String.valueOf(hMap.get("FWXZ"))));
				 hMap.put("FWYT1", ConstHelper.getNameByValue("FWYT", String.valueOf(hMap.get("FWYT1"))));
				 hMap.put("FWYT2", ConstHelper.getNameByValue("FWYT", String.valueOf(hMap.get("FWYT2"))));
				 hMap.put("FWYT3", ConstHelper.getNameByValue("FWYT", String.valueOf(hMap.get("FWYT3"))));
				 hMap.put("FWLX", ConstHelper.getNameByValue("FWLX", String.valueOf(hMap.get("FWLX"))));
				 hMap.put("FWJG", ConstHelper.getNameByValue("FWJG", String.valueOf(hMap.get("FWJG"))));
				 hMap.put("ZT", ConstHelper.getNameByValue("BDCDYZT", String.valueOf(hMap.get("ZT"))));
				 hMap.put("DJZT", ConstHelper.getNameByValue("DJZT", String.valueOf(hMap.get("DJZT"))));
				 hMap.put("GHYT", ConstHelper.getNameByValue("FWYT", String.valueOf(hMap.get("GHYT"))));
				 hMap.put("FWTDYT", ConstHelper.getNameByValue("FWTDYT", String.valueOf(hMap.get("TDYT"))));
				 hMap.put("MODIFYTIME", DateUtil.FormatByDatetime(hMap.get("MODIFYTIME")));
				 hMap.put("CREATETIME", DateUtil.FormatByDatetime(hMap.get("CREATETIME")));
				 hMap.put("JGSJ", DateUtil.FormatByDatetime(hMap.get("JGSJ")));
				 hMap.put("TDSYQQSRQ", DateUtil.FormatByDatetime(hMap.get("TDSYQQSRQ")));
				 hMap.put("	TDSYQZZRQ", DateUtil.FormatByDatetime(hMap.get("TDSYQZZRQ")));
				 hMap.put("BDCDYLX",BDCDYLX.initFrom(bdcdylxcode).Name);
				 newMap.put("DY", hMap);
			
				}
			 if (BDCDYLX.YCH.equals(bdcdylx))//预测户不动产单元
				{
				 BDCS_H_XZY _ych = (BDCS_H_XZY) u;
				 Map<String, Object> ychMap= transBean2Map(_ych);
				 BDCS_SHYQZD_XZ shyqzd=baseCommonDao.get(BDCS_SHYQZD_XZ.class,_ych.getZDBDCDYID());
				 if(shyqzd!=null)
					 ychMap.put("ZDMJ", shyqzd.getZDMJ());
				//hMap.put("BGZT", ConstHelper.getNameByValue("BGZT", String.valueOf(hMap.get("BGZT"))));//变更状态不知道字典值
				 ychMap.put("HX", ConstHelper.getNameByValue("HX", String.valueOf(ychMap.get("HX"))));
				 ychMap.put("HXJG", ConstHelper.getNameByValue("HXJG", String.valueOf(ychMap.get("HXJG"))));
				 ychMap.put("FWXZ", ConstHelper.getNameByValue("FWXZ", String.valueOf(ychMap.get("FWXZ"))));
				 ychMap.put("FWYT1", ConstHelper.getNameByValue("FWYT", String.valueOf(ychMap.get("FWYT1"))));
				 ychMap.put("FWYT2", ConstHelper.getNameByValue("FWYT", String.valueOf(ychMap.get("FWYT2"))));
				 ychMap.put("FWYT3", ConstHelper.getNameByValue("FWYT", String.valueOf(ychMap.get("FWYT3"))));
				 ychMap.put("FWLX", ConstHelper.getNameByValue("FWLX", String.valueOf(ychMap.get("FWLX"))));
				 ychMap.put("FWJG", ConstHelper.getNameByValue("FWJG", String.valueOf(ychMap.get("FWJG"))));
				 ychMap.put("ZT", ConstHelper.getNameByValue("BDCDYZT", String.valueOf(ychMap.get("ZT"))));
				 ychMap.put("DJZT", ConstHelper.getNameByValue("DJZT", String.valueOf(ychMap.get("DJZT"))));
				 ychMap.put("GHYT", ConstHelper.getNameByValue("FWYT", String.valueOf(ychMap.get("GHYT"))));
				 ychMap.put("BDCDYLX",BDCDYLX.initFrom(bdcdylxcode).Name);
				 ychMap.put("MODIFYTIME", DateUtil.FormatByDatetime(ychMap.get("MODIFYTIME")));
				 ychMap.put("CREATETIME", DateUtil.FormatByDatetime(ychMap.get("CREATETIME")));
				 ychMap.put("JGSJ", DateUtil.FormatByDatetime(ychMap.get("JGSJ")));
				 newMap.put("DY", ychMap);
				
				}
		 }
			 //权利信息
		List<Rights> rightss=RightsTools.loadRightsByCondition(DJDYLY.XZ,"DJDYID='"+djdyid+"'");
		//权利信息Map形态
		List<Map> rightListMap=new ArrayList<Map>();
		for(Rights rights:rightss)
		{
			 BDCS_QL_XZ _ql=(BDCS_QL_XZ)rights;
			Map<String, Object> qlMap= transBean2Map(_ql);
		  	//权利表字典转换相应字段
		  		qlMap.put("CZFS", ConstHelper.getNameByValue("CZFS",String.valueOf(qlMap.get("CZFS"))));
		  		qlMap.put("QLLXMC", ConstHelper.getNameByValue("QLLX",String.valueOf(qlMap.get("QLLX"))));
		  		qlMap.put("QLJSSJ", DateUtil.FormatByDatetime(qlMap.get("QLJSSJ")));
		  		qlMap.put("QLQSSJ", DateUtil.FormatByDatetime(qlMap.get("QLQSSJ")));
		  		qlMap.put("QSZT", ConstHelper.getNameByValue("QSZT",String.valueOf(qlMap.get("QSZT"))));
		  		qlMap.put("DJSJ", DateUtil.FormatByDatetime(qlMap.get("DJSJ")));
		  		qlMap.put("DJLXMC", ConstHelper.getNameByValue("DJLX",String.valueOf(qlMap.get("DJLX"))));
		  		qlMap.put("MODIFYTIME", DateUtil.FormatByDatetime(qlMap.get("MODIFYTIME")));
		  		qlMap.put("CREATETIME", DateUtil.FormatByDatetime(qlMap.get("CREATETIME")));
		  		qlMap.put("ZSEWM","");//证书二维码不要
		  		qlMap.put("ZSBS", ConstHelper.getNameByValue("ZSBS",String.valueOf(qlMap.get("ZSBS"))));
			SubRights subright=RightsTools.loadSubRightsByRightsID(DJDYLY.XZ,rights.getId());
			 BDCS_FSQL_XZ  _fsql=(BDCS_FSQL_XZ)subright;
				Map<String, Object>  fsqlMap= transBean2Map(_fsql);
			//附属权利表字典转换相应字段
			 fsqlMap.put("YWRZJZL", ConstHelper.getNameByValue("ZJLX",String.valueOf(fsqlMap.get("YWRZJZL"))));
			 fsqlMap.put("GYDQLRZJZL", ConstHelper.getNameByValue("ZJLX",String.valueOf(fsqlMap.get("GYDQLRZJZL"))));
			 fsqlMap.put("YZYFS", ConstHelper.getNameByValue("YZYFS",String.valueOf(fsqlMap.get("YZYFS"))));
			 fsqlMap.put("TDSYQXZ", ConstHelper.getNameByValue("TDSYQXZ",String.valueOf(fsqlMap.get("TDSYQXZ"))));
			 fsqlMap.put("FWXZ", ConstHelper.getNameByValue("FWXZ",String.valueOf(fsqlMap.get("FWXZ"))));
			 fsqlMap.put("FWJG", ConstHelper.getNameByValue("FWJG",String.valueOf(fsqlMap.get("FWJG"))));
			 fsqlMap.put("DYBDCLX_CODE", fsqlMap.get("DYBDCLX"));
			 fsqlMap.put("DYBDCLX", ConstHelper.getNameByValue("DYBDCLX",String.valueOf(fsqlMap.get("DYBDCLX"))));
			 fsqlMap.put("DYFS", ConstHelper.getNameByValue("DYFS",String.valueOf(fsqlMap.get("DYFS"))));
			 fsqlMap.put("DYWLX", ConstHelper.getNameByValue("DYBDCLX",String.valueOf(fsqlMap.get("DYWLX"))));
			 fsqlMap.put("GJZWLX", ConstHelper.getNameByValue("GZWLX",String.valueOf(fsqlMap.get("GJZWLX"))));
			 fsqlMap.put("LDSYQXZ", ConstHelper.getNameByValue("TDSYQXZ",String.valueOf(fsqlMap.get("LDSYQXZ"))));
			 fsqlMap.put("CFLX", ConstHelper.getNameByValue("CFLX",String.valueOf(fsqlMap.get("CFLX"))));
			 fsqlMap.put("SYTTLX", ConstHelper.getNameByValue("SYTTLX",String.valueOf(fsqlMap.get("SYTTLX"))));
			 fsqlMap.put("GHYT", ConstHelper.getNameByValue("FWYT",String.valueOf(fsqlMap.get("GHYT"))));
			 fsqlMap.put("QY", ConstHelper.getNameByValue("QY",String.valueOf(fsqlMap.get("QY"))));
			 fsqlMap.put("YGDJZL", ConstHelper.getNameByValue("YGDJZL",String.valueOf(fsqlMap.get("YGDJZL"))));
				   
			 fsqlMap.put("CFSJ", DateUtil.FormatByDatetime(fsqlMap.get("CFSJ")));
			 fsqlMap.put("ZXSJ", DateUtil.FormatByDatetime(fsqlMap.get("ZXSJ")));
			 fsqlMap.put("JGSJ", DateUtil.FormatByDatetime(fsqlMap.get("JGSJ")));
			 fsqlMap.put("MODIFYTIME", DateUtil.FormatByDatetime(fsqlMap.get("MODIFYTIME")));
			 fsqlMap.put("CREATETIME", DateUtil.FormatByDatetime(fsqlMap.get("CREATETIME")));
		  	    //用来判断权利是否注销。
				if(StringUtils.isEmpty(fsqlMap.get("ZXSJ")))
					fsqlMap.put("SFZX","该权利未注销");
		  		else
		  			fsqlMap.put("SFZX","该权利已注销");
				 List<Map<String,Object>>  qlrsMap=new ArrayList<Map<String,Object>>();
			List<RightsHolder> RightsHolders=RightsHolderTools.loadRightsHolders(DJDYLY.XZ, rights.getId());
			 for(RightsHolder rh:RightsHolders)
			 {
				 
				 BDCS_QLR_XZ qlr=(BDCS_QLR_XZ)rh;
				 Map<String, Object> qlrMap=transBean2Map(qlr);
				 if(RightsHolders.size()>1)
				 {
					 String gyfs=RightsHolders.get(0).getGYFSName();
					 qlrMap.put("GYQK", gyfs);
				 }			
				 qlrMap.put("DLRZJLX", ConstHelper.getNameByValue("ZJLX",String.valueOf(qlrMap.get("DLRZJLX"))));
				 //qlrMap.put("GYFS", ConstHelper.getNameByValue("GYFS",String.valueOf(qlrMap.get("GYFS"))));
				 qlrMap.put("GJ", ConstHelper.getNameByValue("GJDQ",String.valueOf(qlrMap.get("GJ"))));
				 qlrMap.put("XB", ConstHelper.getNameByValue("XB",String.valueOf(qlrMap.get("XB"))));
				 qlrMap.put("HJSZSS", ConstHelper.getNameByValue("SS",String.valueOf(qlrMap.get("HJSZSS"))));
				 qlrMap.put("SSHY", ConstHelper.getNameByValue("SSHY",String.valueOf(qlrMap.get("SSHY"))));
				 qlrMap.put("QLRLX", ConstHelper.getNameByValue("QLRLX",String.valueOf(qlrMap.get("QLRLX"))));
				 qlrMap.put("FDDBRZJLX", ConstHelper.getNameByValue("ZJLX",String.valueOf(qlrMap.get("FDDBRZJLX"))));
				 qlrMap.put("ZJZL", ConstHelper.getNameByValue("ZJLX",String.valueOf(qlrMap.get("ZJZL"))));

				 qlrMap.put("MODIFYTIME", DateUtil.FormatByDatetime(qlrMap.get("MODIFYTIME")));
				 qlrMap.put("CREATETIME", DateUtil.FormatByDatetime(qlrMap.get("CREATETIME")));
				 qlrsMap.add(qlrMap);
			 }
			qlMap.put("QL", qlMap);
			qlMap.put("FSQL", fsqlMap);
			qlMap.put("QLR", qlrsMap);
			rightListMap.add(qlMap);
		}
		newMap.put("QLXX", rightListMap);
		querylist.add(newMap);
	}
}
	/**
	 * 将对象转换成MAP
	 * @Title: transBean2Map
	 * @author:wuzhu
	 * @date：
	 * @param obj
	 * @return
	 *///将对象转换成MAP
	  public  Map<String, Object> transBean2Map(Object obj) {  
	    if(obj == null){  
	        return new HashMap<String, Object>();  
	    }          
	    Map<String, Object> map = new HashMap<String, Object>();  
	    try {  
	        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());  
	        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();  
	        for (PropertyDescriptor property : propertyDescriptors) {  
	            String key = property.getName();  

	            // 过滤class属性  
	            if (!key.equals("class")) {  
	                // 得到property对应的getter方法  
	                Method getter = property.getReadMethod();  
	                Object value = getter.invoke(obj);  

	                map.put(key, value);  
	            }  

	        }  
	    } catch (Exception e) {  
	        System.out.println("对象转换为MAP失败： " + e);  
	    }  
	    return map;  
	  }
@Override
public Message queryHouse(Map<String, String> queryvalues, int page,
		int rows, boolean iflike, String fwzt,String sort,String order) {
	Message msg = new Message();
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
		unitentityName = "(SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ ,'031' AS BDCDYLX ,'现房' AS BDCDYLXMC FROM BDCK.BDCS_H_XZ UNION ALL SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ  ,'032' AS BDCDYLX ,'期房' AS BDCDYLXMC FROM BDCK.BDCS_H_XZY)";
		if ("2".equals(cxzt)) {
			unitentityName = "(SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ ,'031' AS BDCDYLX ,'现房' AS BDCDYLXMC FROM BDCK.BDCS_H_LS UNION ALL SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ  ,'032' AS BDCDYLX ,'期房' AS BDCDYLXMC FROM BDCK.BDCS_H_LSY)";
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
				.append(" left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid  ");
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

	//count = dao.getCountByFullSql(fromSql);
	listresult = dao.getDataListByFullSql(fullSql);
	ConvertBDCDYXX(listresult);
	addLimitStatus(listresult);
	//addRightsHolderInfo(listresult);
	//addLimitStatus(listresult);
	//addDyCfDetails(listresult);
	//isGlzd(listresult);
    //addQLLX(listresult);
    //addQXGL(listresult);
    //ADDXMXX_SIZE(listresult);		// 格式化结果中的常量值
	// 格式化结果中的常量值

	/*for (Map map : listresult) {
		if (map.containsKey("GHYT")) {
			String value = StringHelper.formatObject(StringHelper
					.isEmpty(map.get("GHYT")) ? "" : map.get("GHYT"));
			String name = "";
			if (!StringHelper.isEmpty(value)) {
				name = ConstHelper.getNameByValue("FWYT", value);
			}
			map.put("GHYTname", name);
		}
	}*/

	msg.setTotal(listresult.size());
	msg.setRows(listresult);
	return msg;
}
//给权利类型赋值
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addQLLX(List<Map> result) {
		if (result != null && result.size() > 0) {
			for (Map map : result) 
			{
				String qllxname=ConstHelper.getNameByValue("QLLX",String.valueOf(map.get("QLLX")));
				map.put("QLLX", qllxname);
			}
			}
	}
	//给权利类型赋值
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private void ConvertUnitMode(List<Map> result) {
			List<Map> unitMapList = new ArrayList<Map>();
			if (result != null && result.size() > 0) {
				for (Map map : result) 
				{
					String qllxname=ConstHelper.getNameByValue("QLLX",String.valueOf(map.get("QLLX")));
					map.put("QLLX", qllxname);
				}
				}
		}
@SuppressWarnings("unchecked")
private void addRightsHolderInfo(
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
@SuppressWarnings({ "rawtypes", "unchecked" })
private void addLimitStatus(List<Map> result) {
	if (result != null && result.size() > 0) {
		for (Map map : result) {
			List<Map> qlxx=(List<Map>) map.get("QLXX");
			Map dy=(Map)map.get("DY");
			Map ql=(Map)qlxx.get(0).get("QL");
			if (ql.containsKey("DJDYID")) {
				String djdyid = (String) ql.get("DJDYID");
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
							mapxz = getDYandCFandYY_XZ(djdyid);
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
							dy.put("DYZT", valuedyxz);
							dy.put("CFZT", valuecfxz);
							dy.put("YYZT", valueyyxz);

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
							mapxzy = getDYandCFandYY_XZY(djdyid);
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
							dy.put("DYZT", valuedyxz);
							dy.put("CFZT", valuecfxz);
							dy.put("YYZT", valueyyxz);
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
	map.put("DYZTFLAG", String.valueOf(mortgageCount));
	map.put("CFZTFLAG", String.valueOf(SealCount));
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
	map.put("DYZTFLAG", String.valueOf(mortgageCount));
	map.put("CFZTFLAG", String.valueOf(SealCount));
	map.put("DYZT", mortgageStatus);
	map.put("CFZT", sealStatus);
	map.put("YYZT", objectionStatus);

	return map;
}
//登记查封状态的详情信息（这里已经不要了）
@SuppressWarnings({ "rawtypes", "unchecked" })
private void addDyCfDetails(List<Map> result){
	if (result != null && result.size() > 0) {
		for (Map map : result) {
			if(!StringUtils.isEmpty(map.get("QLID")))
			{
				String dycfdetailssql = MessageFormat.format("select fsql.BDBZZQSE,fsql.DYMJ,fsql.DYSW,fsql.ZJJZWZL,fsql.DYFS,fsql.cfjg,fsql.cfwh,fsql.cfwj,fsql.CFFW,fsql.CFLX,fsql.LHSX,fsql.dyr,ql.qlqssj,ql.qljssj,ql.qllx,ql.djlx,ql.djsj,ql.dbr,ql.djyy,ql.fj,ql.bdcqzh,ql.qlid "
						+ " from bdck.bdcs_ql_xz ql left join bdck.bdcs_fsql_xz fsql on ql.qlid=fsql.qlid where ql.qlid=''{0}''  ",
						map.get("QLID"));
				
				List<Map> dycfs=baseCommonDao.getDataListByFullSql(dycfdetailssql);
				Map cf=new HashMap();
				Map dy=new HashMap();
				for (Map dycf : dycfs) {
					if("800".equals(dycf.get("DJLX"))&&"99".equals(dycf.get("QLLX"))){
						
						cf.put("CFJG", dycf.get("CFJG"));
						cf.put("CFWJ", dycf.get("CFWJ"));
						cf.put("CFWH", dycf.get("CFWH"));
						cf.put("QLQSSJ", DateUtil.FormatByDatetime(dycf.get("QLQSSJ")));
						cf.put("QLJSSJ",DateUtil.FormatByDatetime(dycf.get("QLJSSJ")));
						cf.put("CFFW",dycf.get("CFFW"));
						cf.put("CFLX",dycf.get("CFLX"));
						cf.put("LHSX",dycf.get("LHSX"));
						cf.put("DJSJ",dycf.get("DJSJ"));
						cf.put("DBR",dycf.get("DBR"));
						cf.put("DJYY",dycf.get("DJYY"));
						cf.put("FJ",dycf.get("FJ"));

						map.put("CFJG", dycf.get("CFJG"));
						map.put("CFWJ", dycf.get("CFWJ"));
						map.put("CFWH", dycf.get("CFWH"));
						map.put("CFQX", DateUtil.FormatByDatetime(dycf.get("QLQSSJ"))+" 至  "+DateUtil.FormatByDatetime(dycf.get("QLJSSJ")));
						//添加查封信息
						map.put("CFXX", cf);
				
					}
					if("23".equals(dycf.get("QLLX"))){
					
						dy.put("DYR", dycf.get("DYR"));
						dy.put("QLQSSJ", DateUtil.FormatByDatetime(dycf.get("QLQSSJ")));
						dy.put("QLJSSJ", DateUtil.FormatByDatetime(dycf.get("QLJSSJ")));
						dy.put("BDCQZH", dycf.get("BDCQZH"));
						dy.put("ZJJZWZL",dycf.get("ZJJZWZL"));
						dy.put("DYFS", ConstHelper.getNameByValue("DYFS", String.valueOf(dycf.get("DYFS"))));
						dy.put("DJSJ", DateUtil.FormatByDatetime(dycf.get("DJSJ")));
						dy.put("DYMJ", dycf.get("DYMJ"));
						dy.put("BDBZZQSE",dycf.get("BDBZZQSE"));
						List<Map> qlrxx=new ArrayList<Map>();
						List<BDCS_QLR_XZ> qlrs=baseCommonDao.getDataList(BDCS_QLR_XZ.class, "QLID='"+dycf.get("QLID")+"'");
						for(BDCS_QLR_XZ qlr:qlrs)
						{
							Map mapqlr=new HashMap();
							mapqlr.put("QLRMC",qlr.getQLRMC());
							mapqlr.put("QLRLX",ConstHelper.getNameByValue("QLLX",qlr.getQLRLX()));
							mapqlr.put("SXH",qlr.getSXH());
							mapqlr.put("DH",qlr.getDH());
							mapqlr.put("DZ",qlr.getDZ());
							qlrxx.add(mapqlr);
						}
						dy.put("QLRS", qlrxx);
				
						map.put("DYR", dycf.get("DYR"));
						map.put("DYQX", DateUtil.FormatByDatetime(dycf.get("QLQSSJ"))+" 至  "+DateUtil.FormatByDatetime(dycf.get("QLJSSJ")));
						//添加抵押信息
						map.put("DYXX", dy);
					
					}
				}
			
		
			}
		}
	}
}

@Override
public Map updatehth(String houses) {
	 Map<String,String> r=new HashMap<String,String>();
	if(houses!=null&&!houses.equals("{}"))
	{
		try{
			r.put("success","true");
	  	r.put("msg","成功");
		JSONObject projectinfo=JSONObject.parseObject(houses);
	     JSONArray housesinfo = projectinfo.getJSONArray("houses");
	      String ywh = projectinfo.get("ywh").toString();
	     for (int j = 0; j < housesinfo.size(); j++) {
	    	 JSONObject _house = (JSONObject) housesinfo.get(j);
	    	String bdcdyh= _house.get("bdcdyh").toString();
	    	String hth= _house.get("hth").toString();
			List<BDCS_QL_GZ> qls= baseCommonDao.getDataList(BDCS_QL_GZ.class,"bdcdyh='"+bdcdyh+"' and  ywh='"+ ywh+"'");
			if(qls!=null&&qls.size()>0) {
				String updateHTBH = "begin update bdck.bdcs_fsql_gz set htbh = '" + hth + "' where  qlid = '" + qls.get(0).getId() + "';update bdck.bdcs_fsql_gz set htbh = '" + hth + "' where  qlid = '" + qls.get(0).getId() + "';update bdck.bdcs_fsql_gz set htbh = '" + hth + "' where  qlid = '" + qls.get(0).getId() + "';end;";
				baseCommonDao.updateBySql(updateHTBH);
			}
	     }
	  }
	  catch (Exception e) {
	  	r.put("success","false");
	  	r.put("msg",e.getMessage());
	  }
	}
	else{
		r.put("success","false");
	  	r.put("msg","传入参数为空");
	}
	return r;
}
}