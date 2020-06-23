package com.supermap.realestate.registration.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.ViewClass.DJDY_LS_EX;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.QLR_XZ_HOUSE_EX;
import com.supermap.realestate.registration.ViewClass.QLXXXZ;
import com.supermap.realestate.registration.ViewClass.QL_LS_EX;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QLR_LS;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SFDY;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_TDYT_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.T_GRIDCONFIG_USERDEFINEBOOK;
import com.supermap.realestate.registration.model.T_QUERYCONFIG_USERDEFINEBOOK;
import com.supermap.realestate.registration.model.T_USERDEFINEBOOK;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.service.TJCXFXService;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.Department;
import com.supermap.wisdombusiness.framework.model.Role;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.workflow.model.Wfd_Route;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;

@Service("TJCXFXService")
public class TJCXFXServiceImpl implements TJCXFXService {

	@Autowired
	private CommonDao baseCommonDao;
	List<Map> resultsub =  new ArrayList<Map>() ;

	/**
	 * 获取行政区信息
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Message GetQueryxzq_info() {
		// StringBuilder hqlCondition=new StringBuilder();
		// List<bdck_xzq>
		// list=baseCommonDao.getDataList(bdck_xzq.class,hqlCondition.toString());
		String fulSql = "select *from BDCK.bdck_xzq";
		List<Map> ListObj = baseCommonDao.getDataListByFullSql(fulSql);
		Message msg = new Message();
		msg.setTotal(1);
		msg.setRows(ListObj);
		return msg;
	}

	/**
	 * 通过行政区，房屋类型，统计方式，统计单位，起始时间，终止时间对房屋交易分析统计
	 * 
	 * @param strxzq
	 *            行政区
	 * @param fwlx
	 *            房屋类型 商品房买卖、存量房买卖
	 * @param tjfs
	 *            统计方式 按面积统计[mj]、按成交价格【没有对应的字段】、按成交套数[gs]
	 * @param tjdw
	 *            统计单位 按日统计[dd]、按月统计[mm]、按年统计[yyyy]
	 * @param kssj
	 *            起始时间
	 * @param jssj
	 *            终止时间
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Message Getxz_fwdj_info(String strxzq, String fwlx, String tjfs,
			String tjdw, String kssj, String jssj) {
		StringBuilder queryfield = new StringBuilder();
		StringBuilder fromsql = new StringBuilder();
		StringBuilder where = new StringBuilder();
		StringBuilder groupby = new StringBuilder();
		/*
		 * select to_char(ql.DJSJ,'yyyy-mm') as djsj,sum(h.SCTNJZMJ) as sctnhzmj
		 * from BDCK.BDCS_H_XZ h left join BDCK.BDCS_DJDY_XZ d on h.bdcdyid
		 * =d.bdcdyid left join BDCK.BDCS_QL_XZ ql on d.djdyid=ql.djdyid where
		 * ql.djlx='100' group by to_char(ql.DJSJ,'yyyy-mm')
		 */
		queryfield.append("select ");
		// 统计单位 按日统计[dd]、按月统计[mm]、按年统计[yyyy]
		if (tjdw.equals("dd")) {
			queryfield.append(" to_char(ql.DJSJ,'yyyy-mm-dd') as djsj, ");
			groupby.append(" group by to_char(ql.DJSJ,'yyyy-mm-dd')   order by  to_char(ql.DJSJ, 'yyyy-mm-dd') ");
		}
		if (tjdw.equals("mm")) {
			queryfield.append(" to_char(ql.DJSJ,'yyyy-mm') as djsj, ");
			groupby.append(" group by to_char(ql.DJSJ,'yyyy-mm')   order by  to_char(ql.DJSJ, 'yyyy-mm')");
		}
		if (tjdw.equals("yyyy")) {
			queryfield.append(" to_char(ql.DJSJ,'yyyy') as djsj, ");
			groupby.append(" group by to_char(ql.DJSJ,'yyyy')  order by  to_char(ql.DJSJ, 'yyyy')");
		}
		// 统计方式 按面积统计[mj]、按成交价格【没有对应的字段】、按成交套数[gs]
		if (tjfs.equals("mj")) {
			queryfield.append(" sum(h.SCJZMJ) as sctnhzmj  ");
		}
		if (tjfs.equals("gs")) {
			queryfield.append(" count(*) as  sctnhzmj ");
		}
		// 按成交价格【没有对应的字段】
		// if(!tjfs.equals("yyyy"))
		// {
		// queryfield.append(" to_char(ql.DJSJ,'yyyy') ");
		// }
		fromsql.append(" from BDCK.BDCS_H_XZ h left join  BDCK.BDCS_DJDY_XZ d on "
				+ "h.bdcdyid =d.bdcdyid left join  BDCK.BDCS_QL_XZ ql on d.djdyid=ql.djdyid ");

		where.append(" where   ql.QLLX IN ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18')");
		where.append(" and  ql.djlx='" + fwlx + "'");
		// where.append(" and  h.qxdm='"+strxzq+"'");
		if (kssj != null) {
			where.append(" and ql.DJSJ >= to_date('" + kssj
					+ " 00:00:00','yy-mm-dd hh24:mi:ss') ");
		}
		if (jssj != null) {
			where.append(" and ql.DJSJ <=to_date('" + jssj
					+ " 23:59:59','yy-mm-dd hh24:mi:ss') ");
		}

		System.out.println(queryfield.toString() + fromsql.toString()
				+ where.toString() + groupby.toString());
		List<Map> ListObj = baseCommonDao.getDataListByFullSql(queryfield
				.toString()
				+ fromsql.toString()
				+ where.toString()
				+ groupby.toString());
		Message msg = new Message();
		msg.setTotal(1);
		msg.setRows(ListObj);
		return msg;

	}

	/**
	 * 通过行政区，土地类型，统计方式，统计单位，起始时间，终止时间对土地交易分析统计
	 * 
	 * @param strxzq
	 *            行政区
	 * @param fwlx
	 *            土地类型
	 * @param tjfs
	 *            统计方式 按面积统计[mj]、按成交价格【没有对应的字段】、按成交套数[gs]
	 * @param tjdw
	 *            统计单位 按日统计[dd]、按月统计[mm]、按年统计[yyyy]
	 * @param kssj
	 *            起始时间
	 * @param jssj
	 *            终止时间
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Message Getxz_tdfxdj_info(String strxzq, String fwlx, String tjfs,
			String tjdw, String kssj, String jssj) {
		StringBuilder queryfield = new StringBuilder();
		StringBuilder fromsql = new StringBuilder();
		StringBuilder where = new StringBuilder();
		StringBuilder groupby = new StringBuilder();
		/*
		 * select to_char(ql.DJSJ,'yyyy-mm') as djsj,sum(h.SCTNJZMJ) as sctnhzmj
		 * from BDCK.BDCS_H_XZ h left join BDCK.BDCS_DJDY_XZ d on h.bdcdyid
		 * =d.bdcdyid left join BDCK.BDCS_QL_XZ ql on d.djdyid=ql.djdyid where
		 * ql.djlx='100' group by to_char(ql.DJSJ,'yyyy-mm')
		 */
		queryfield.append("select ");
		// 统计单位 按日统计[dd]、按月统计[mm]、按年统计[yyyy]
		if (tjdw.equals("dd")) {
			queryfield.append(" to_char(ql.DJSJ,'yyyy-mm-dd') as djsj, ");
			groupby.append(" group by to_char(ql.DJSJ,'yyyy-mm-dd')   order by  to_char(ql.DJSJ, 'yyyy-mm-dd') ");
		}
		if (tjdw.equals("mm")) {
			queryfield.append(" to_char(ql.DJSJ,'yyyy-mm') as djsj, ");
			groupby.append(" group by to_char(ql.DJSJ,'yyyy-mm')   order by  to_char(ql.DJSJ, 'yyyy-mm')");
		}
		if (tjdw.equals("yyyy")) {
			queryfield.append(" to_char(ql.DJSJ,'yyyy') as djsj, ");
			groupby.append(" group by to_char(ql.DJSJ,'yyyy')  order by  to_char(ql.DJSJ, 'yyyy')");
		}
		// 统计方式 按面积统计[mj]、按成交价格【没有对应的字段】、按成交套数[gs]
		if (tjfs.equals("mj")) {
			queryfield.append(" sum(h.ZDMJ) as sctnhzmj  ");
		}
		if (tjfs.equals("gs")) {
			queryfield.append(" count(*) as  sctnhzmj ");
		}
		// 按成交价格【没有对应的字段】
		// if(!tjfs.equals("yyyy"))
		// {
		// queryfield.append(" to_char(ql.DJSJ,'yyyy') ");
		// }
		fromsql.append(" from BDCK.BDCS_SHYQZD_XZ h left join  BDCK.BDCS_DJDY_XZ d on "
				+ "h.bdcdyid =d.bdcdyid left join  BDCK.BDCS_QL_XZ ql on d.djdyid=ql.djdyid ");
		// 用登记类型测试
		where.append(" where   ql.QLLX IN ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18')");
		where.append(" and ql.djlx='" + fwlx + "'");
		// where.append(" and  h.qxdm='"+strxzq+"'");
		if (kssj != null) {
			where.append(" and ql.DJSJ >= to_date('" + kssj
					+ " 00:00:00','yy-mm-dd hh24:mi:ss') ");
		}
		if (jssj != null) {
			where.append(" and ql.DJSJ <=to_date('" + jssj
					+ " 23:59:59','yy-mm-dd hh24:mi:ss') ");
		}
		System.out.println(queryfield.toString() + fromsql.toString()
				+ where.toString() + groupby.toString());
		List<Map> ListObj = baseCommonDao.getDataListByFullSql(queryfield
				.toString()
				+ fromsql.toString()
				+ where.toString()
				+ groupby.toString());
		Message msg = new Message();
		msg.setTotal(1);
		msg.setRows(ListObj);
		return msg;
	}

	/**
	 * 通过行政区，登陆类型，统计单位，起始时间，终止时间对登记 业务统计
	 * 
	 * @param strxzq
	 *            行政区
	 * @param fwlx
	 *            土地类型 商品房买卖、存量房买卖
	 * @param tjdw
	 *            统计单位 按日统计、按月统计、按年统计
	 * @param kssj
	 *            起始时间
	 * @param jssj
	 *            终止时间
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Message Getxz_djywtj_info(String strxzq, String fwlx, String tjdw,
			String kssj, String jssj) {
		StringBuilder queryfield = new StringBuilder();
		StringBuilder fromsql = new StringBuilder();
		StringBuilder where = new StringBuilder();
		StringBuilder groupby = new StringBuilder();
		/*
		 * select to_char(ql.DJSJ,'yyyy-mm') as djsj,sum(h.SCTNJZMJ) as sctnhzmj
		 * from BDCK.BDCS_H_XZ h left join BDCK.BDCS_DJDY_XZ d on h.bdcdyid
		 * =d.bdcdyid left join BDCK.BDCS_QL_XZ ql on d.djdyid=ql.djdyid where
		 * ql.djlx='100' group by to_char(ql.DJSJ,'yyyy-mm')
		 */
		queryfield.append("select ");
		// 统计单位 按日统计[dd]、按月统计[mm]、按年统计[yyyy]
		if (tjdw.equals("dd")) {
			queryfield
					.append(" to_char(ql.DJSJ,'yyyy-mm-dd') as djsj,count(1) as sctnhzmj ");
			groupby.append(" group by to_char(ql.DJSJ,'yyyy-mm-dd')   order by  to_char(ql.DJSJ, 'yyyy-mm-dd') ");
		}
		if (tjdw.equals("mm")) {
			queryfield
					.append(" to_char(ql.DJSJ,'yyyy-mm') as djsj,count(1) as sctnhzmj  ");
			groupby.append(" group by to_char(ql.DJSJ,'yyyy-mm')   order by  to_char(ql.DJSJ, 'yyyy-mm')");
		}
		if (tjdw.equals("yyyy")) {
			queryfield
					.append(" to_char(ql.DJSJ,'yyyy') as djsj,count(1) as sctnhzmj  ");
			groupby.append(" group by to_char(ql.DJSJ,'yyyy')  order by  to_char(ql.DJSJ, 'yyyy')");
		}

		fromsql.append(" from  BDCK.BDCS_QL_XZ ql  ");
		where.append(" where 1=1  and ql.QLLX IN ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18') ");
		// 用登记类型测试
		if (fwlx != null) {
			where.append(" and  ql.djlx='" + fwlx + "'");
		}
		// where.append(" and  ql.qxdm='"+strxzq+"'");
		if (kssj != null) {
			where.append(" and ql.DJSJ >= to_date('" + kssj
					+ " 00:00:00','yy-mm-dd hh24:mi:ss') ");
		}
		if (jssj != null) {
			where.append(" and ql.DJSJ <=to_date('" + jssj
					+ " 23:59:59','yy-mm-dd hh24:mi:ss') ");
		}
		System.out.println(queryfield.toString() + fromsql.toString()
				+ where.toString() + groupby.toString());
		List<Map> ListObj = baseCommonDao.getDataListByFullSql(queryfield
				.toString()
				+ fromsql.toString()
				+ where.toString()
				+ groupby.toString());
		Message msg = new Message();
		msg.setTotal(1);
		msg.setRows(ListObj);
		return msg;

	}

	@SuppressWarnings("rawtypes")
	@Override
	public Message Getxz_fwjybb_info(String strxzq, String kssj, String jssj) {
		StringBuilder queryfield = new StringBuilder();
		StringBuilder fromsql = new StringBuilder();
		StringBuilder where = new StringBuilder();
		queryfield
				.append("        select h.FWLX,ql.DJLX,sum(SCJZMJ) as cymj ,sum(FDCJYJG) as cjzj,count(1) as cjts, sum(FDCJYJG)*10000/sum(SCJZMJ)  as cjjj ");
		fromsql.append("    from BDCK.BDCS_H_XZ h left join  BDCK.BDCS_DJDY_XZ d on h.bdcdyid =d.bdcdyid ");
		fromsql.append("	left join  BDCK.BDCS_QL_XZ ql on d.djdyid=ql.djdyid");
		where.append(" where ql.DJLX in ('100','200') and ql.QLLX IN ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18')");
		// where.append(" and  ql.qxdm='"+strxzq+"'");
		if (kssj != null) {
			where.append(" and ql.DJSJ >= to_date('" + kssj
					+ " 00:00:00','yy-mm-dd hh24:mi:ss') ");
		}
		if (jssj != null) {
			where.append(" and ql.DJSJ <=to_date('" + jssj
					+ " 23:59:59','yy-mm-dd hh24:mi:ss') ");
		}
		StringBuilder groupby = new StringBuilder();
		groupby.append("  group by h.FWLX,ql.DJLX");
		groupby.append("  order by h.FWLX,ql.DJLX");
		System.out.println(queryfield.toString() + fromsql.toString()
				+ where.toString() + groupby.toString());

		List<Map> ListObj = baseCommonDao.getDataListByFullSql(queryfield
				.toString()
				+ fromsql.toString()
				+ where.toString()
				+ groupby.toString());
		// for(int i=0;i<ListObj.size();i++)
		// {
		// Map objMap=ListObj.get(i);
		// float zzj=(Float) objMap.get("CJZJ");
		// float zmj=(Float) objMap.get("CJMJ");
		// float fcjjj=0.0f;
		// try{
		// fcjjj=zzj/zmj;
		// }catch(Exception e){
		//
		// }
		// objMap.put("CJJJ", fcjjj);
		// }
		Message msg = new Message();
		msg.setTotal(1);
		msg.setRows(ListObj);
		return msg;
	}

	/**
	 * 获取权利信息
	 */
	@Override
	public Message Getxz_QLXXXZ(String qlid) {
		QLXXXZ obj = QLXXXZ.Create(qlid);
		List<QLXXXZ> QLXXXZList = new ArrayList<QLXXXZ>();
		QLXXXZList.add(obj);
		Message msg = new Message();
		msg.setTotal(1);
		msg.setRows(QLXXXZList);
		return msg;
	}

	/**
	 * 通过不动产单元ID获取现状登记单元信息
	 * 
	 * @param BDCDYID
	 * @return
	 */
	private List<BDCS_DJDY_XZ> GetBDCS_DJDY_XZ(String BDCDYID) {
		String hql = MessageFormat.format(" BDCDYID=''{0}''", BDCDYID);
		List<BDCS_DJDY_XZ> ListObj = baseCommonDao.getDataList(
				BDCS_DJDY_XZ.class, hql);
		return ListObj;
	}

	/**
	 * 通过登记单元ID获取权利信息以及附属权利以及相关权利人
	 * 
	 * @param StrDJDYID
	 */
	private List<QLXXXZ> GetDBCS_QL_XS_All(String StrDJDYID) {
		List<QLXXXZ> obj = QLXXXZ.Create_DJDYID(StrDJDYID);
		return obj;
	}

	/**
	 * 通过不动产单元号获取权利信息列表
	 * 
	 * @param bdcdyh
	 *            不动产单元号
	 * @param BDCDYTYPE
	 *            不动产单元类型
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Message Get_BDCDYID_ALLLC(String bdcdyh, String BDCDYTYPE) {
		List<Map> MapList = new ArrayList<Map>();
		Map map = new HashMap();
		String BDCDYID = null;
		// 户
		if (BDCDYTYPE.equals("h")) {
			String hql = MessageFormat.format(" bdcdyh=''{0}''", bdcdyh);
			List<BDCS_H_XZ> BDCS_H_XZList = baseCommonDao.getDataList(
					BDCS_H_XZ.class, hql);
			if (BDCS_H_XZList.size() > 0) {
				BDCS_H_XZ BDCS_H_XZObj = BDCS_H_XZList.get(0);
				BDCDYID = BDCS_H_XZObj.getId();
				map.put("BDCS_H_XZ", BDCS_H_XZObj);
			}
		}
		// 使用权宗地
		if (BDCDYTYPE.equals("syqzd")) {
			String hql = MessageFormat.format(" bdcdyh=''{0}''", bdcdyh);
			List<BDCS_SHYQZD_XZ> BDCS_SHYQZD_XZList = baseCommonDao
					.getDataList(BDCS_SHYQZD_XZ.class, hql);
			if (BDCS_SHYQZD_XZList.size() > 0) {
				BDCS_SHYQZD_XZ BDCS_SHYQZD_XZObj = BDCS_SHYQZD_XZList.get(0);
				BDCDYID = BDCS_SHYQZD_XZObj.getId();
				map.put("BDCS_SHYQZD_XZ", BDCS_SHYQZD_XZObj);
			}
		}

		if (!StringUtils.isEmpty(BDCDYID)) {
			List<BDCS_DJDY_XZ> BDCS_DJDY_XZ_list = GetBDCS_DJDY_XZ(BDCDYID);
			if (BDCS_DJDY_XZ_list.size() > 0) {
				BDCS_DJDY_XZ BDCS_DJDY_XZ_OBJ = BDCS_DJDY_XZ_list.get(0);
				String StrDJDYID = BDCS_DJDY_XZ_OBJ.getDJDYID();
				List<QLXXXZ> QLXXXZList = GetDBCS_QL_XS_All(StrDJDYID);
				map.put("qlxx", QLXXXZList);
			}
		}
		MapList.add(map);
		Message msg = new Message();
		msg.setTotal(1);
		msg.setRows(MapList);
		return msg;
	}

	/**
	 * 通过不同产单元号查询项目信息 历史
	 * 
	 * @param strBDCDYH
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Message GetProject_History(String strBDCDYH) {
		StringBuilder hqlCondition = new StringBuilder();
		hqlCondition
				.append(" DJDYID in (select DJDYID from BDCS_DJDY_LS where BDCDYH='"
						+ strBDCDYH + "')   ");

		// hqlCondition.append(" and QLLX IN ('1','2','3','4','5','6','7','8','9','10','11',"
		// + "'12','13','14','15','16','17','18') ");
		hqlCondition.append(" order by djsj desc");
		List<BDCS_QL_LS> list = baseCommonDao.getDataList(BDCS_QL_LS.class,
				hqlCondition.toString());
		List<Map> Maplist = new ArrayList<Map>();
		for (int i = 0; i < list.size(); i++) {
			BDCS_QL_LS ObjBDCS_QL_LS = (BDCS_QL_LS) list.get(i);
			Map<String, String> mBDCS_QL_LS = new HashMap<String, String>();

			mBDCS_QL_LS.put("djlxname", ObjBDCS_QL_LS.getDJLXName());
			mBDCS_QL_LS.put("qllxName", ObjBDCS_QL_LS.getQLLXName());
			mBDCS_QL_LS.put("dbr", ObjBDCS_QL_LS.getDBR());
			mBDCS_QL_LS.put("djsjdate", ObjBDCS_QL_LS.getdjsjdate());
			mBDCS_QL_LS.put("xmbh", ObjBDCS_QL_LS.getXMBH());
			mBDCS_QL_LS.put("qlid", ObjBDCS_QL_LS.getId());

			// 通过项目编号获取到项目信息表
			BDCS_XMXX BDCS_XMXXObj = Getproject(ObjBDCS_QL_LS.getXMBH());
			if (BDCS_XMXXObj == null) {
				continue;
			}

			// 流程编号
			mBDCS_QL_LS.put("project_id", BDCS_XMXXObj == null ? ""
					: BDCS_XMXXObj.getPROJECT_ID());
			// 项目名称
			mBDCS_QL_LS.put("project_name", BDCS_XMXXObj == null ? ""
					: BDCS_XMXXObj.getXMMC());
			String StrQLID = ObjBDCS_QL_LS.getId();
			String StrQLRNAME = Get_qlr_ls_info(StrQLID);
			mBDCS_QL_LS.put("qlrmcs", StrQLRNAME);

			Maplist.add(mBDCS_QL_LS);
		}
		System.out.println("查询结果为" + list.size());
		Message msg = new Message();
		msg.setTotal(1);
		msg.setRows(Maplist);
		return msg;
	}

	private String Get_qlr_ls_info(String QLID) {
		StringBuilder hqlCondition = new StringBuilder();
		StringBuilder qlrNames = new StringBuilder();
		String Names = "";
		hqlCondition.append(" QLID ='" + QLID + "'");
		List<BDCS_QLR_LS> list = baseCommonDao.getDataList(BDCS_QLR_LS.class,
				hqlCondition.toString());
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				BDCS_QLR_LS BDCS_QLR_LSObj = list.get(i);
				qlrNames.append("," + BDCS_QLR_LSObj.getQLRMC());
			}
			Names = qlrNames.substring(1);
		}
		return Names;
	}

	/**
	 * 通过不动产单元号获取抵押信息
	 * 
	 * @return true 是 false 否
	 */
	private boolean getxz_zd_dy(String strbdcdyh) {
		String hql = "QLLX='23' ";
		return getxzzd_dy_qs_fc_yy(strbdcdyh, hql);
	}

	/**
	 * 通过不动产单元号获取查封信息
	 * 
	 * @return true 是 false 否
	 */
	private boolean getxz_zd_cf(String strbdcdyh) {
		String hql = "DJLX='" + ConstValue.DJLX.CFDJ.Value + "' ";
		return getxzzd_dy_qs_fc_yy(strbdcdyh, hql);
	}

	/**
	 * 通过不动产单元号获取异议信息
	 * 
	 * @return true 是 false 否
	 */
	private boolean getxz_zd_yy(String strbdcdyh) {
		String hql = "DJLX='" + ConstValue.DJLX.YYDJ.Value + "' ";
		return getxzzd_dy_qs_fc_yy(strbdcdyh, hql);
	}

	/**
	 * 通过不动产单元号获取权属信息
	 * 
	 * @return true 是 false 否
	 */
	private boolean getxz_zd_qs(String strbdcdyh) {
		String hql = "QLLX IN ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18') ";
		return getxzzd_dy_qs_fc_yy(strbdcdyh, hql);
	}

	/**
	 * 通过不动产单元号获取权属信息，抵押信息，查封信息，异议信息
	 * 
	 * @return true 是 false 否
	 */
	private boolean getxzzd_dy_qs_fc_yy(String strbdcdyh, String strwhere) {
		String fulsql = "from  BDCK.BDCS_QL_XZ q where " + strwhere;
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(fulsql);
		Map<String, String> paramMap = new HashMap<String, String>();
		if (!StringUtils.isEmpty(strbdcdyh)) {
			hqlBuilder.append(" and BDCDYH =:BDCDYH ");
			paramMap.put("BDCDYH", strbdcdyh);
		}
		Long longcount = baseCommonDao.getCountByFullSql(hqlBuilder.toString(),
				paramMap);
		if (longcount > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 通过不动产单元号获取权利人名称和不动产权证号 历史
	 * 
	 * @param strbdcdyh
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Message GetQLR_CQZInfo_LS(String strbdcdyh) {
		String fulsql = "select b.qlrmc,b.Bdcqzh,z.QLLX from BDCK.BDCS_QL_LS z  left join BDCK.BDCS_QLR_LS b on b.qlid=z.qlid ";
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(fulsql);
		hqlBuilder.append(" where 1=1 ");
		// hqlBuilder.append(" and QLLX IN ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18')   ");
		Map<String, String> paramMap = new HashMap<String, String>();
		if (!StringUtils.isEmpty(strbdcdyh)) {
			hqlBuilder.append(" and z.BDCDYH =:BDCDYH ");
			paramMap.put("BDCDYH", strbdcdyh);
		}
		// 按时间排序
		hqlBuilder.append(" order by z.DJSJ desc ");
		// List<Map> atxt = new ArrayList<Map>();
		// 权利人
		StringBuilder qlrsBuilder = new StringBuilder();
		// 证书号
		StringBuilder qzhBuilder = new StringBuilder();
		// 获取权利人和证书号
		List<Map> resultRows = baseCommonDao.getDataListByFullSql(
				hqlBuilder.toString(), paramMap);
		if (resultRows.size() > 0) {
			/** 获取最新一次登记权证号，以及权利人 */
			Map mapObject = resultRows.get(0);
			String mapObject_BDCQZH = "";
			if (mapObject.get("BDCQZH") == null) {
				qzhBuilder.append("  ");
			} else {
				mapObject_BDCQZH = mapObject.get("BDCQZH").toString();
				qzhBuilder.append(mapObject_BDCQZH);
			}
			for (Iterator iterator = resultRows.iterator(); iterator.hasNext();) {
				Map mapRows = (Map) iterator.next();
				if (mapObject_BDCQZH.equals(mapRows.get("BDCQZH"))) {
					qlrsBuilder.append("," + mapRows.get("QLRMC"));
				}
				// qlrsBuilder.append("," + mapRows.get("QLRMC"));
				// qzhBuilder.append("," + mapRows.get("BDCQZH"));
			}
		}
		// 组装输出页面结果信息，包括权利人，证书号，抵押，权属，异议，查封信息
		List<Map> pageRows = new ArrayList<Map>();
		Map<String, String> resMap = new HashMap<String, String>();
		if (qlrsBuilder.length() > 0) {
			resMap.put("qlrmcs", qlrsBuilder.substring(1));
		}
		if (qzhBuilder.length() > 0) {
			resMap.put("bdcqzhs", qzhBuilder.toString());
		}
		// 抵押
		resMap.put("bdcdy", getxz_zd_dy(strbdcdyh) ? "抵押" : "未抵押");
		// 查封
		resMap.put("bdccf", getxz_zd_cf(strbdcdyh) ? "查封" : "未查封");
		// 异议
		resMap.put("bdcyy", getxz_zd_yy(strbdcdyh) ? "有异议" : "无异议");
		// 权属
		resMap.put("bdcqs", getxz_zd_qs(strbdcdyh) ? "有效" : "无效");
		pageRows.add(resMap);
		Message m = new Message();
		m.setTotal(1);
		m.setRows(pageRows);
		return m;
	}

	/**
	 * 获取项目信息 现状
	 * 
	 * @param page
	 * @param rows
	 * @param StrZL
	 *            坐落
	 * @param Strbdcqzh
	 *            不动产权证号
	 * @param StrBDCDYH
	 *            不动产单元号
	 * @param StrType
	 *            类型
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Message Getxzzs_Info(int page, int rows, String StrZL,
			String Strbdcqzh, String StrBDCDYH, String StrType, String StrXm,
			String StrZjh) {
		// String strSql = "select x.Bdcdyh, x.BDCQZH , x.DJJG, z.zl";
		String strSql = "";// "select distinct x.Bdcdyh,  x.DJJG, z.zl";
		StringBuilder FormSqlBuilder = new StringBuilder();
		FormSqlBuilder
				.append(" from  BDCK.BDCS_QL_XZ x left join BDCK.BDCS_DJDY_XZ b on b.djdyid = x.djdyid");
		// 宗地
		if (BDCDYLX.SHYQZD.Value.equals(StrType)) {
			FormSqlBuilder
					.append(" right join BDCK.BDCS_SHYQZD_XZ z  on b.bdcdyid = z.bdcdyid where x.qllx in ('3','5','7') ");
			// strSql ="select distinct x.Bdcdyh,  x.DJJG, z.zl,z.ZDMJ ";
			strSql = "select distinct x.Bdcdyh,  z.zl,z.ZDMJ,b.djdyid,x.qlid,x.DJJG ";
		}
		// 户
		if (BDCDYLX.H.Value.equals(StrType)) {
			FormSqlBuilder
					.append(" right join BDCK.BDCS_H_XZ z  on b.bdcdyid = z.bdcdyid where x.qllx in ('4','6','8') ");
			// strSql
			// ="select distinct x.Bdcdyh,  x.DJJG, z.zl,z.HH,z.SCJZMJ,z.SCTNJZMJ,z.SCFTJZMJ,z.FTTDMJ,z.FWYT1 ";
			strSql = "select distinct x.Bdcdyh,  z.zl,z.HH,z.SCJZMJ,z.SCTNJZMJ,z.SCFTJZMJ,z.FTTDMJ,z.FWYT1,"
					+ "b.djdyid,x.qlid,x.DJJG ";
		}
		// where
		// FormSqlBuilder.append(" where x.QLLX in ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18')");
		//FormSqlBuilder.append(" where 1=1 ");
		// 坐落
		if (!StringUtils.isEmpty(StrZL)) {
			FormSqlBuilder.append(" and z.zl like '%" + StrZL + "%'");
		}
		// Strbdcqzh 不动产权证号
		if (!StringUtils.isEmpty(Strbdcqzh)) {
			FormSqlBuilder.append(" and x.BDCQZH  like '%" + Strbdcqzh + "%'");
		}
		// StrBDCDYH 不动产单元号
		if (!StringUtils.isEmpty(StrBDCDYH)) {
			FormSqlBuilder.append(" and x.Bdcdyh  like '%" + StrBDCDYH + "%'");
		}
		// StrXM 姓名
		if (!StringUtils.isEmpty(StrXm) || !StringUtils.isEmpty(StrZjh)) {
			// StrXM 姓名
			String xm = " 1=1 ";
			if (!StringUtils.isEmpty(StrXm)) {
				xm = " qlr.QLRMC like '%" + StrXm + "%'";
			}
			// StrZjh 证件号
			String zjh = "";
			if (!StringUtils.isEmpty(StrZjh)) {
				zjh = " and qlr.ZJH like '%"+ StrZjh + "%' ";
			}
			FormSqlBuilder.append(" and x.qlid in (select qlr.qlid from BDCK.BDCS_QLR_XZ qlr where "+ xm+zjh+ ")");
		}
		

		System.out.print(strSql + FormSqlBuilder.toString());
		Long total = baseCommonDao.getCountByFullSql("from (" + strSql
				+ FormSqlBuilder.toString() + ") t");
		System.out.println(total);

		Message msg = new Message();
		if(total >0) {
			List<Map> list = baseCommonDao.getPageDataByFullSql(strSql
					+ FormSqlBuilder.toString(), page, rows);
			// 获取权利人信息，获取
			String fromSql_qlid = "";
			String fromSql_djdyid = "";
			for (int i = 0; i < list.size(); i++) {
				Map map = list.get(i);
				String StrBDZDYID_Code = map.get("BDCDYH") == null ? "" : map.get(
						"BDCDYH").toString();
				if (BDCDYLX.H.Value.equals(StrType)) {
					String StrFWLX1 = map.get("FWYT1") == null ? "" : map.get(
							"FWYT1").toString();
					String StrFWLX1Name = "";
					if (!StringUtils.isEmpty(StrFWLX1)) {
						StrFWLX1Name = ConstHelper.getNameByValue("FWLX", StrFWLX1);
					}
					map.put("FWYT1", StrFWLX1Name);
				}
				if(StringHelper.isEmpty(fromSql_djdyid)) {
					fromSql_djdyid = " djdyid = '"+map.get("DJDYID").toString()+"' ";
				}else {
					fromSql_djdyid += " or djdyid = '"+map.get("DJDYID").toString()+"' ";
				}
				if(StringHelper.isEmpty(fromSql_qlid)) {
					fromSql_qlid = " QLID = '"+map.get("QLID").toString()+"' ";
				}else {
					fromSql_qlid += " or QLID = '"+map.get("QLID").toString()+"' ";
				}
//				Message p = GetQLR_CQZInfo(StrBDZDYID_Code);
//				List<Map> cdw = (List<Map>) p.getRows();
//				if (cdw.size() == 1) {
//					Map Mapobj = cdw.get(0);
//					map.put("qlrmcs", Mapobj.get("qlrmcs"));
//					map.put("bdcqzhs", Mapobj.get("bdcqzhs"));
//					map.put("bdcdy", Mapobj.get("bdcdy"));
//					map.put("bdccf", Mapobj.get("bdccf"));
//					map.put("bdcyy", Mapobj.get("bdcyy"));
//					map.put("qlrzjlx", Mapobj.get("qlrzjlx"));
//					map.put("qlrzjh", Mapobj.get("qlrzjh"));
//					// 最后一次登记的登记机构
//					map.put("DJJG", Mapobj.get("DJJG"));
//				}
				
			}
			if(!StringHelper.isEmpty(fromSql_qlid)) {
				List<Map> qlrList = baseCommonDao.getDataListByFullSql(
						"select b.qlrmc,b.Bdcqzh,b.ZJZL,b.ZJH,b.qlid "
						+ "from  BDCK.BDCS_QLR_XZ b where "+fromSql_qlid);
				if(qlrList != null && qlrList.size() >0) {
					Map<String, Map<String,String>> qlrs = new HashMap<String, Map<String,String>>();
					for (Map qlr : qlrList) {
						Map<String, String> map = new HashMap<String, String>();
						if(qlrs != null || !StringHelper.isEmpty(qlrs.get(qlr.get("QLID").toString()))) {
							map.put("qlrmcs", null);
							map.put("bdcqzhs", null);
							map.put("qlrzjh", null);
							map.put("qlrzjlx", null);
							qlrs.put(qlr.get("QLID").toString(), map);
						}
						String qlrmc = StringHelper.formatMerge(qlrs.get(qlr.get("QLID").toString()).get("qlrmcs"), qlr.get("QLRMC"));
						map.put("qlrmcs", qlrmc);
						String bdcqzh = StringHelper.formatMerge(qlrs.get(qlr.get("QLID").toString()).get("bdcqzhs"), qlr.get("BDCQZH"));
						map.put("bdcqzhs", bdcqzh);
						String zjh = StringHelper.formatMerge(qlrs.get(qlr.get("QLID").toString()).get("qlrzjh"), qlr.get("ZJH"));
						map.put("qlrzjh", zjh);
						if(!StringHelper.isEmpty(qlr.get("ZJZL"))) {
							String zjname = ConstHelper.getNameByValue("ZJLX", qlr.get("ZJZL").toString());
							String zjzl = StringHelper.formatMerge(qlrs.get(qlr.get("QLID").toString()).get("qlrzjlx"), zjname);
							map.put("qlrzjlx", zjzl);
						}
						qlrs.put(qlr.get("QLID").toString(), map);
					}
					if (qlrs != null) {
						for (Map ql : list) {
							ql.putAll(qlrs.get(ql.get("QLID")));
						} 
					}
				}
			}
			if(!StringHelper.isEmpty(fromSql_djdyid)) {
				List<Map> ztList = baseCommonDao.getDataListByFullSql(
						"SELECT distinct DJDYID, (CASE WHEN QLLX = '23' THEN '抵押' ELSE '未抵押' END) AS BDCDY, "
						+ "(CASE WHEN DJLX = '800' AND QLLX = '99'THEN '查封'ELSE '未查封'END)AS BDCCF ,"
						+ "(CASE WHEN DJLX = '600'THEN '有异议'ELSE '无异议'END)AS BDCYY ,"
						+ "(CASE WHEN QLLX IN('3', '5', '7', '4', '6', '8')THEN '有效'ELSE '无效'END)AS BDCQS "
						+ " FROM BDCK.BDCS_QL_XZ WHERE "+fromSql_djdyid);
				if(ztList != null && ztList.size() >0) {
					Map<String, Map<String,String>> zss = new HashMap<String, Map<String,String>>();
					for (Map zt : ztList) {
						String djdyid = zt.get("DJDYID").toString();
						Map<String, String> zt2 = new HashMap<String, String>();
						zt2.put("bdcdy", zt.get("BDCDY").toString());
						zt2.put("bdccf", zt.get("BDCCF").toString());
						zt2.put("bdcyy", zt.get("BDCYY").toString());
						zt2.put("bdcqs", zt.get("BDCQS").toString());
						zss.put(djdyid, zt);
					}
					for (Map map : list) {
						map.putAll(zss.get(map.get("DJDYID").toString()));
					}
				}
			}
			msg.setTotal(total);
			msg.setRows(list);
		}else {
			
			msg.setTotal(total);
		}
		return msg;
	}

	/**
	 * 通过不动产单元号获取不动产权证号，权利人名称 现状
	 * 
	 * @param strbdcdyh
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Message GetQLR_CQZInfo(String strbdcdyh) {
		String fulsql = "select b.qlrmc,b.Bdcqzh,z.QLLX,b.ZJZL,b.ZJH,z.DJJG from BDCK.BDCS_QL_XZ z  left join BDCK.BDCS_QLR_XZ b on b.qlid=z.qlid ";
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(fulsql);
		// hqlBuilder.append(" where 1=1 and QLLX IN ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18')");
		hqlBuilder.append(" where 1=1");
		Map<String, String> paramMap = new HashMap<String, String>();
		if (!StringUtils.isEmpty(strbdcdyh)) {
			hqlBuilder.append(" and z.BDCDYH =:BDCDYH ");
			paramMap.put("BDCDYH", strbdcdyh);
		}

		hqlBuilder.append(" order by z.DJSJ desc ");
		// List<Map> atxt = new ArrayList<Map>();
		// 权利人
		StringBuilder qlrsBuilder = new StringBuilder();
		// 证书号
		StringBuilder qzhBuilder = new StringBuilder();
		// 证件号
		StringBuilder zjhBuilder = new StringBuilder();
		// 证件类型
		StringBuilder zjlxBuilder = new StringBuilder();

		// 记录最新的登记机构信息
		String Strdjjg = "";
		// 获取权利人和证书号
		List<Map> resultRows = baseCommonDao.getDataListByFullSql(
				hqlBuilder.toString(), paramMap);
		if (resultRows.size() > 0) {
			Map BDCQZHMap = new HashMap();
			for (int nI = 0; nI < resultRows.size(); nI++) {
				Map mapRows = resultRows.get(nI);
				qlrsBuilder.append("," + mapRows.get("QLRMC"));
				// qzhBuilder.append("," + mapRows.get("BDCQZH"));
				if (nI == 0) {
					Strdjjg = mapRows.get("DJJG") == null ? "" : mapRows.get(
							"DJJG").toString();
				}
				String StrBDCQZH = mapRows.get("BDCQZH") == null ? "" : mapRows
						.get("BDCQZH").toString();
				// 设置证书号
				BDCQZHMap.put(StrBDCQZH, StrBDCQZH);
				String zjzl = mapRows.get("ZJZL") == null ? "" : mapRows.get(
						"ZJZL").toString();
				String zjzlName = "";
				if (!StringUtils.isEmpty(zjzl)) {
					zjzlName = ConstHelper.getNameByValue("ZJLX", zjzl);
				}
				zjlxBuilder.append("," + zjzlName);
				zjhBuilder.append("," + mapRows.get("ZJH"));
			}
			if (BDCQZHMap.size() > 0) {
				Set set = BDCQZHMap.keySet();// 用接口实例接口
				Iterator iter = set.iterator();
				while (iter.hasNext()) {// 遍历二次,速度慢
					String k = (String) iter.next();
					qzhBuilder.append("," + k);
				}
			}
		}
		// 组装输出页面结果信息，包括权利人，证书号，抵押，权属，异议，查封信息
		List<Map> pageRows = new ArrayList<Map>();
		Map<String, String> resMap = new HashMap<String, String>();

		// 登记机构
		resMap.put("DJJG", Strdjjg);

		if (qlrsBuilder.length() > 0) {
			resMap.put("qlrmcs", qlrsBuilder.substring(1));
		} else {
			resMap.put("qlrmcs", "");
		}
		if (qzhBuilder.length() > 0) {
			resMap.put("bdcqzhs", qzhBuilder.substring(1));
		} else {
			resMap.put("bdcqzhs", "");
		}
		if (zjlxBuilder.length() > 0) {
			resMap.put("qlrzjlx", zjlxBuilder.substring(1));
		} else {
			resMap.put("qlrzjlx", "");
		}

		if (zjhBuilder.length() > 0) {
			resMap.put("qlrzjh", zjhBuilder.substring(1));
		} else {
			resMap.put("qlrzjh", "");
		}

		// 抵押
		resMap.put("bdcdy", getxz_zd_dy(strbdcdyh) ? "抵押" : "未抵押");
		// 查封
		resMap.put("bdccf", getxz_zd_cf(strbdcdyh) ? "查封" : "未查封");
		// 异议
		resMap.put("bdcyy", getxz_zd_yy(strbdcdyh) ? "有异议" : "无异议");
		// 权属
		resMap.put("bdcqs", getxz_zd_qs(strbdcdyh) ? "有效" : "无效");

		pageRows.add(resMap);
		Message m = new Message();
		m.setTotal(1);
		m.setRows(pageRows);
		return m;
	}

	/**
	 * 通过不同产单元号查询项目信息 现状
	 * 
	 * @param strBDCDYH
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Message GetProject_xz(String strBDCDYH) {
		StringBuilder hqlCondition = new StringBuilder();
		hqlCondition
				.append(" DJDYID in (select DJDYID from BDCS_DJDY_XZ where BDCDYH='"
						+ strBDCDYH
						+ "') and QLLX IN ('1','2','3','4','5','6','7','8','9','10','11',"
						+ "'12','13','14','15','16','17','18') order by djsj desc  ");
		List<BDCS_QL_XZ> list = baseCommonDao.getDataList(BDCS_QL_XZ.class,
				hqlCondition.toString());
		List<Map> Maplist = new ArrayList<Map>();
		for (int i = 0; i < list.size(); i++) {
			BDCS_QL_XZ ObjBDCS_QL_XZ = (BDCS_QL_XZ) list.get(i);
			Map<String, String> mBDCS_QL_XZ = new HashMap<String, String>();

			mBDCS_QL_XZ.put("djlxname", ObjBDCS_QL_XZ.getDJLXName());
			mBDCS_QL_XZ.put("qllxName", ObjBDCS_QL_XZ.getQLLXName());
			mBDCS_QL_XZ.put("dbr", ObjBDCS_QL_XZ.getDBR());
			mBDCS_QL_XZ.put("djsjdate", ObjBDCS_QL_XZ.getdjsjdate());
			mBDCS_QL_XZ.put("xmbh", ObjBDCS_QL_XZ.getXMBH());
			mBDCS_QL_XZ.put("qlid", ObjBDCS_QL_XZ.getId());
			// 通过项目编号获取到项目信息表
			BDCS_XMXX BDCS_XMXXObj = Getproject(ObjBDCS_QL_XZ.getXMBH());
			// 流程编号
			mBDCS_QL_XZ.put("project_id", BDCS_XMXXObj == null ? ""
					: BDCS_XMXXObj.getPROJECT_ID());
			// 项目名称
			mBDCS_QL_XZ.put("project_name", BDCS_XMXXObj == null ? ""
					: BDCS_XMXXObj.getXMMC());
			String StrQLID = ObjBDCS_QL_XZ.getId();
			String StrQLRNAME = Get_qlr_ls_info(StrQLID);
			mBDCS_QL_XZ.put("qlrmcs", StrQLRNAME);
			Maplist.add(mBDCS_QL_XZ);
		}
		System.out.println("查询结果为" + list.size());
		Message msg = new Message();
		msg.setTotal(1);
		msg.setRows(Maplist);
		return msg;
	}

	/**
	 * 通过项目编号获取项目信息表
	 * 
	 * @param xmbh
	 * @return
	 */
	private BDCS_XMXX Getproject(String xmbh) {
		StringBuilder hqlCondition = new StringBuilder();
		hqlCondition.append("xmbh='" + xmbh + "'");
		List<BDCS_XMXX> list = baseCommonDao.getDataList(BDCS_XMXX.class,
				hqlCondition.toString());
		if (list.size() > 0) {
			BDCS_XMXX obj = list.get(0);
			// project_id=obj.getPROJECT_ID();
			return obj;
		}
		return null;
	}

	/**
	 * 获取项目信息 历史
	 * 
	 * @param page
	 * @param rows
	 * @param StrZL
	 *            坐落
	 * @param Strbdcqzh
	 *            不动产权证号
	 * @param StrBDCDYH
	 *            不动产单元号
	 * @param StrType
	 *            类型
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Message Getlszs_Info(int page, int rows, String StrZL,
			String Strbdcqzh, String StrBDCDYH, String StrType) {
		// String strSql = "select x.Bdcdyh, x.BDCQZH , x.DJJG, z.zl";
		String strSql = "select distinct x.Bdcdyh,  x.DJJG, z.zl";
		StringBuilder FormSqlBuilder = new StringBuilder();
		FormSqlBuilder
				.append(" from  BDCK.BDCS_QL_LS x left join BDCK.BDCS_DJDY_LS b on b.djdyid = x.djdyid");
		// 宗地
		if (BDCDYLX.SHYQZD.Value.equals(StrType)) {
			FormSqlBuilder
					.append(" right join BDCK.BDCS_SHYQZD_LS z  on b.bdcdyid = z.bdcdyid");
		}
		// 宗地
		if (BDCDYLX.H.Value.equals(StrType)) {
			FormSqlBuilder
					.append(" right join BDCK.BDCS_H_LS z  on b.bdcdyid = z.bdcdyid");
		}
		// where
		// FormSqlBuilder.append(" where x.QLLX in ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18')");
		FormSqlBuilder.append(" where 1=1 ");
		// 坐落
		if (!StringUtils.isEmpty(StrZL)) {
			FormSqlBuilder.append(" and z.zl like '%" + StrZL + "%'");
		}
		// Strbdcqzh 不动产权证号
		if (!StringUtils.isEmpty(Strbdcqzh)) {
			FormSqlBuilder.append(" and x.BDCQZH  like '%" + Strbdcqzh + "%'");
		}
		// StrBDCDYH 不动产单元号
		if (!StringUtils.isEmpty(StrBDCDYH)) {
			FormSqlBuilder.append(" and x.Bdcdyh  like '%" + StrBDCDYH + "%'");
		}
		// FormSqlBuilder.append(" order by x.djsj desc");
		System.out.print(strSql + FormSqlBuilder.toString());
		List<Map> list = baseCommonDao.getPageDataByFullSql(strSql
				+ FormSqlBuilder.toString(), page, rows);

		Long total = baseCommonDao.getCountByFullSql("from (" + strSql
				+ FormSqlBuilder.toString() + ") t");
		System.out.println(total);

		for (int i = 0; i < list.size(); i++) {
			Map map = list.get(i);
			String StrBDZDYID_Code = map.get("BDCDYH") == null ? "" : map.get(
					"BDCDYH").toString();

			Message p = GetQLR_CQZInfo_LS(StrBDZDYID_Code);
			List<Map> cdw = (List<Map>) p.getRows();
			if (cdw.size() == 1) {
				Map obj = cdw.get(0);
				map.put("qlrmcs", obj.get("qlrmcs"));
				map.put("bdcqzhs", obj.get("bdcqzhs"));

			}
		}
		Message msg = new Message();
		msg.setTotal(total);
		msg.setRows(list);
		// m.setTotal(p.getTotalCount());
		// m.setRows(p.getResult());
		return msg;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Message GetWorkBookLand(Map<String, String> conditionParameter,
			int currentpage, int pageSize) throws Exception {
		Map<String, String> newpara = new HashMap<String, String>();
		StringBuilder fromsql = new StringBuilder(
				" FROM  BDCK.BDCS_DJDY_LS DJDY INNER JOIN BDCK.BDCS_QL_LS QL ON DJDY.DJDYID=QL.DJDYID WHERE 1=1 ");

		if (!StringUtils.isEmpty(conditionParameter.get("DJSJ_Q"))) {
			fromsql.append("  AND QL.DJSJ >=to_date(:DJSJ_Q,'yyyy-mm-dd') ");
			newpara.put("DJSJ_Q", new String(conditionParameter.get("DJSJ_Q")
					.getBytes("iso8859-1"), "utf-8"));
		}
		if (!StringUtils.isEmpty(conditionParameter.get("DJSJ_Z"))) {
			fromsql.append("  AND QL.DJSJ <=to_date(:DJSJ_Z,'yyyy-mm-dd')+1 ");
			newpara.put("DJSJ_Z", new String(conditionParameter.get("DJSJ_Z")
					.getBytes("iso8859-1"), "utf-8"));
		}
		if (!StringUtils.isEmpty(conditionParameter.get("DJLX"))) {
			fromsql.append("  AND QL.DJLX =:DJLX ");
			newpara.put("DJLX", new String(conditionParameter.get("DJLX")
					.getBytes("iso8859-1"), "utf-8"));
		}
		if (!StringUtils.isEmpty(conditionParameter.get("BDCDYH"))) {
			fromsql.append("  AND DJDY.BDCDYH LIKE:BDCDYH ");
			newpara.put(
					"BDCDYH",
					"%"
							+ new String(conditionParameter.get("BDCDYH")
									.getBytes("iso8859-1"), "utf-8") + "%");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("LX"))) {
			if (conditionParameter.get("LX").equals("house"))
				fromsql.append("  AND DJDY.BDCDYLX IN("
						+ ConstValue.BDCDYLX.H.Value + ") ");
			if (conditionParameter.get("LX").equals("land"))
				fromsql.append("  AND DJDY.BDCDYLX IN("
						+ ConstValue.BDCDYLX.SYQZD.Value + ","
						+ ConstValue.BDCDYLX.SHYQZD.Value + ") ");
		}
		Long total = baseCommonDao.getCountByFullSql(fromsql.toString(),
				newpara);
		if (total == 0)
			return new Message();
		List<Map> result = baseCommonDao.getPageDataByFullSql(
				" SELECT DJDY.ID,QL.QLID,QL.DJSJ,QL.DJLX,QL.BDCDYH,QL.XMBH,QL.QLLX "
						+ fromsql.toString() + " ORDER BY QL.DJSJ DESC ",
				newpara, currentpage, pageSize);
		List<Map> newResult = new ArrayList<Map>();
		for (Map<String, String> r : result) {
			int qlrs = 0;
			String zl = "";
			String ywr = "";
			String xmbh = r.get("XMBH") + "";
			String fulSql = "SELECT SQR.SQRXM FROM BDCK.BDCS_SQR SQR WHERE SQR.SQRLB='2' "
					+ " AND SQR.XMBH='" + xmbh + "'";
			BDCS_DJDY_LS djdy = baseCommonDao.get(BDCS_DJDY_LS.class,
					r.get("ID"));
			if (djdy != null) {
				DJDY_LS_EX djdy_ls_ex = new DJDY_LS_EX();
				PropertyUtils.copyProperties(djdy_ls_ex, djdy);// 拷贝属性方法
				djdy_ls_ex.Init(baseCommonDao);
				for (QL_LS_EX ql_ls_ex : djdy_ls_ex.getQl_ls_ex()) {
					if (ql_ls_ex.getId().equals(r.get("QLID")))
						qlrs = ql_ls_ex.getQlr_lss().size();
				}
				RealUnit h = (RealUnit) djdy_ls_ex.getRealUnit();
				if (h != null)
					zl = h.getZL();
			}
			List<Map> sqrxms = baseCommonDao.getDataListByFullSql(fulSql);
			if (sqrxms != null && sqrxms.size() > 0) {
				for (Map map : sqrxms) {
					ywr += map.get("SQRXM") + ",";
				}
				if (!StringHelper.isEmpty(ywr)) {
					ywr = ywr.substring(0, ywr.length() - 1);
				}
			}

			r.put("ZL", zl);
			r.put("QLRS", String.valueOf(qlrs));
			r.put("YWR", ywr);
			r.put("DJLX", ConstHelper.getNameByValue("DJLX", r.get("DJLX")));
			r.put("QLLX", ConstHelper.getNameByValue("QLLX", r.get("QLLX")));
			r.put("DJSJ", StringHelper.FormatByDatetime(r.get("DJSJ")));
			r.put("BDCDYH", StringHelper.formatObject(r.get("BDCDYH")));
			newResult.add(r);
		}
		Message msg = new Message();
		msg.setRows(newResult);
		msg.setTotal(total);
		return msg;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Message GetDiyInfo(Map<String, String> conditionParameter,
			int currentpage, int pageSize) throws Exception {
		Map<String, String> newpara = new HashMap<String, String>();
		String temptable = StringHelper.randomTemporaryTableName(Global.getCurrentUserName());//创建一个临时表
		String exeCreatSql="";//创建临时表
		String exeDropSql="";//删除创建的临时表
		String bjsj = ""; //用于存储
		StringBuilder fromsql = new StringBuilder(" FROM  BDCK.BDCS_QL_XZ QL ")
				//.append("LEFT JOIN bdck.bdcs_qdzr_xz qdzr ON ql.qlid=qdzr.qlid ")
				.append("LEFT JOIN BDCK.BDCS_FSQL_XZ FSQL ON QL.QLID=FSQL.QLID ")
				.append("LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON QL.DJDYID=DJDY.DJDYID ").append("LEFT JOIN ")
				.append("(SELECT BDCDYID,ZL,BDCDYH,'032' AS BDCDYLX,GHYT,FWXZ,(NVL(FTTDMJ,0)+NVL(DYTDMJ,0)) AS SYQMJ,YCJZMJ AS JZMJ,YCFTJZMJ AS FTJZMJ,YCTNJZMJ AS ZYJZMJ,NULL AS ZDMJ,FWTDYT AS TDYT,QLXZ,FTTDMJ FROM BDCK.BDCS_H_XZY ")
				.append("UNION  ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'031' AS BDCDYLX,GHYT,FWXZ,(NVL(FTTDMJ,0)+NVL(DYTDMJ,0)) AS SYQMJ,SCJZMJ AS JZMJ,SCFTJZMJ AS FTJZMJ,SCTNJZMJ AS ZYJZMJ,NULL AS ZDMJ,FWTDYT AS TDYT,QLXZ,FTTDMJ FROM BDCK.BDCS_H_XZ ")
				.append("UNION  ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'02' AS BDCDYLX,NULL AS GHYT, NULL AS FWXZ,NULL AS SYQMJ,NULL AS JZMJ,NULL AS FTJZMJ,NULL AS ZYJZMJ , ZDMJ,YT AS TDYT,QLXZ,NULL AS FTTDMJ FROM BDCK.BDCS_SHYQZD_XZ ")
				.append("UNION  ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'01' AS BDCDYLX,NULL AS GHYT, NULL AS FWXZ,NULL AS SYQMJ,NULL AS JZMJ,NULL AS FTJZMJ,NULL AS ZYJZMJ,ZDMJ,YT AS TDYT,QLXZ,NULL AS FTTDMJ FROM BDCK.BDCS_SYQZD_XZ ") 
				.append("UNION  ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'04' AS BDCDYLX,NULL AS GHYT, NULL AS FWXZ,YHZMJ AS SYQMJ,NULL AS JZMJ,NULL AS FTJZMJ,NULL AS ZYJZMJ,ZHMJ AS ZDMJ,NULL AS TDYT,NULL AS QLXZ,NULL AS FTTDMJ FROM BDCK.BDCS_ZH_XZ ")
				.append("UNION  ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'05' AS BDCDYLX,NULL AS GHYT, NULL AS FWXZ,SYQMJ,NULL AS JZMJ,NULL AS FTJZMJ,NULL AS ZYJZMJ,NULL AS ZDMJ,NULL AS TDYT,NULL AS QLXZ,NULL AS FTTDMJ FROM BDCK.BDCS_SLLM_XZ ")
				.append("UNION  ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'09' AS BDCDYLX,NULL AS GHYT, NULL AS FWXZ,CBMJ AS SYQMJ,NULL AS JZMJ,NULL AS FTJZMJ,NULL AS ZYJZMJ,NULL AS ZDMJ,YT AS TDYT,QLXZ,NULL AS FTTDMJ FROM BDCK.BDCS_NYD_XZ)DY ON DY.BDCDYID=DJDY.BDCDYID AND DY.BDCDYLX=DJDY.BDCDYLX ")
				.append("LEFT JOIN (SELECT DISTINCT TO_CHAR(WM_CONCAT(TO_CHAR(QLRMC))) AS QLRMC,QLID FROM BDCK.BDCS_QLR_XZ GROUP BY QLID) QLR ON QLR.QLID=QL.QLID  ");
				if (!StringUtils.isEmpty(conditionParameter.get("DJLX")) && conditionParameter.get("DJLX").equals("400")) {
					fromsql.append("LEFT JOIN (SELECT a.djdyid, a.bdcqzh FROM bdck.bdcs_ql_ls a left join bdck.bdcs_fsql_ls b on a.qlid = b.qlid WHERE a.qllx IN ('23') and b.zxsj is not null) syql ON djdy.djdyid=syql.djdyid ");
					bjsj = "to_char(FSQL.ZXSJ,'yyyy-mm-dd HH24:mi:ss') AS djsj";
				}else {
					fromsql.append("LEFT JOIN (SELECT a.djdyid,a.bdcqzh FROM bdck.bdcs_ql_xz a WHERE a.qllx IN ('3','4','7','8','11','15','24')) syql ON djdy.djdyid=syql.djdyid ");
					bjsj = "to_char(QL.DJSJ,'yyyy-mm-dd HH24:mi:ss') AS djsj";
				}
				fromsql.append("LEFT JOIN ( ")
				.append("SELECT con.constvalue,con.consttrans FROM bdck.bdcs_const con LEFT JOIN bdck.bdcs_constcls const ON con.constslsid=const.constslsid ")
				.append("WHERE const.constclstype = 'DYFS') dyfs ON fsql.dyfs=dyfs.constvalue ").append("LEFT JOIN ( ")
				.append("SELECT con.constvalue,con.consttrans FROM bdck.bdcs_const con LEFT JOIN bdck.bdcs_constcls const ON con.constslsid=const.constslsid ")
				.append("WHERE const.constclstype = 'BDCDYLX')  dylx ON djdy.bdcdylx=dylx.constvalue ");
				if (!StringUtils.isEmpty(conditionParameter.get("DJLX")) && conditionParameter.get("DJLX").equals("400")) {
					//  LEFT JOIN (SELECT sqr1.xmbh, wm_concat(TO_CHAR(sqr1.sqrxm) ) AS sqrxm, wm_concat(TO_CHAR(sqr1.zjh) ) AS zjh, wm_concat(TO_CHAR(sqr1.sqrlx) ) AS sqrlx, sqr1.sqrlb, wm_concat(TO_CHAR(sqr1.LXDH) ) AS LXDH FROM bdck.bdcs_sqr sqr1  GROUP BY sqr1.xmbh, sqr1.sqrlb) SQR on XMXX.XMBH = SQR.XMBH  AND SQR.SQRLB ='2' and xmxx.xmbh is not null 
					fromsql.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON XMXX.PROJECT_ID = FSQL.ZXDYYWH LEFT JOIN (SELECT con.constvalue,con.consttrans FROM bdck.bdcs_const con LEFT JOIN bdck.bdcs_constcls const ON con.constslsid=const.constslsid WHERE const.constclstype = 'DJLX') djlx ON XMXX.djlx=djlx.constvalue ").append(" WHERE  ql.qllx='23' ");
				}else{//  LEFT JOIN (SELECT sqr1.xmbh, wm_concat(TO_CHAR(sqr1.sqrxm) ) AS sqrxm, wm_concat(TO_CHAR(sqr1.zjh) ) AS zjh, wm_concat(TO_CHAR(sqr1.sqrlx) ) AS sqrlx, sqr1.sqrlb, wm_concat(TO_CHAR(sqr1.LXDH) ) AS LXDH FROM bdck.bdcs_sqr sqr1  GROUP BY sqr1.xmbh, sqr1.sqrlb) SQR on XMXX.XMBH = SQR.XMBH  AND SQR.SQRLB ='2' and xmxx.xmbh is not null 
					fromsql.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON XMXX.XMBH = QL.XMBH LEFT JOIN (SELECT con.constvalue,con.consttrans FROM bdck.bdcs_const con LEFT JOIN bdck.bdcs_constcls const ON con.constslsid=const.constslsid WHERE const.constclstype = 'DJLX') djlx ON XMXX.djlx=djlx.constvalue ").append(" WHERE  ql.qllx='23' ");
				}

		if (!StringUtils.isEmpty(conditionParameter.get("SQRLX"))&&!"0".equals(conditionParameter.get("SQRLX").trim())) {
			fromsql.append("  AND instr(nvl(SQR.SQRLX,'null'),:SQRLX)>0 ");
			newpara.put("SQRLX",conditionParameter.get("SQRLX").trim()); 
		}
		if (!StringUtils.isEmpty(conditionParameter.get("DJSJ_Q"))) {
			fromsql.append("  AND QL.DJSJ >=to_date(:DJSJ_Q,'YYYY-MM-DD HH24:MI:SS') ");
			newpara.put("DJSJ_Q",
					new String(conditionParameter.get("DJSJ_Q").getBytes("iso8859-1"), "utf-8") + " 00:00:00");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("DJSJ_Z"))) {
			fromsql.append("  AND QL.DJSJ <=to_date(:DJSJ_Z,'YYYY-MM-DD HH24:MI:SS') ");
			newpara.put("DJSJ_Z",
					new String(conditionParameter.get("DJSJ_Z").getBytes("iso8859-1"), "utf-8") + " 23:59:59");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("BDCQZH"))) {
			fromsql.append("  AND QL.BDCQZH LIKE:BDCQZH ");
			newpara.put("BDCQZH",
					"%" + new String(conditionParameter.get("BDCQZH").getBytes("iso8859-1"), "utf-8") + "%");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("YWLSH"))) {
			fromsql.append("  AND XMXX.YWLSH LIKE:YWLSH ");
			newpara.put("YWLSH",
					"%" + new String(conditionParameter.get("YWLSH").getBytes("iso8859-1"), "utf-8") + "%");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("BDCDYH"))) {
			fromsql.append("  AND DJDY.BDCDYH LIKE:BDCDYH ");
			newpara.put("BDCDYH",
					"%" + new String(conditionParameter.get("BDCDYH").getBytes("iso8859-1"), "utf-8") + "%");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("TJLX"))) {
			if (conditionParameter.get("TJLX").equals("1"))// 全部
				fromsql.append("  AND DJDY.BDCDYLX IN('01','02','04','05','09','031','032')");
			else if (conditionParameter.get("TJLX").equals("2"))// 房
				fromsql.append("  AND DJDY.BDCDYLX IN('031','032')");
			else if (conditionParameter.get("TJLX").equals("3"))// 宗地
				fromsql.append("  AND DJDY.BDCDYLX IN('01','02')");
			else if (conditionParameter.get("TJLX").equals("4"))// 使用权
				fromsql.append("  AND DJDY.BDCDYLX IN('02')");
			else if (conditionParameter.get("TJLX").equals("5"))// 所有权宗地
				fromsql.append("  AND DJDY.BDCDYLX IN('01')");
			else if (conditionParameter.get("TJLX").equals("6"))// 预测户
				fromsql.append("  AND DJDY.BDCDYLX IN('032')");
			else if (conditionParameter.get("TJLX").equals("7"))// 实测户
				fromsql.append("  AND DJDY.BDCDYLX IN('031')");
			else if (conditionParameter.get("TJLX").equals("8"))// 海域
				fromsql.append("  AND DJDY.BDCDYLX IN('04')");
			else if (conditionParameter.get("TJLX").equals("9"))// 林地
				fromsql.append("  AND DJDY.BDCDYLX IN('05')");
			else if (conditionParameter.get("TJLX").equals("10"))// 农用地
				fromsql.append("  AND DJDY.BDCDYLX IN('09')");
			else
				fromsql.append("  AND DJDY.BDCDYLX IN('031','032') ");

		}
		// liangc 增加登记类型、坐落、抵押人、受理时间查询条件
		if (!StringUtils.isEmpty(conditionParameter.get("DJLX"))) {
			if (conditionParameter.get("DJLX").equals("000"))// 全部登记类型
				fromsql.append(" ");
			else if (conditionParameter.get("DJLX").equals("100")) {// 首次登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "100");
			} else if (conditionParameter.get("DJLX").equals("200")) {// 转移登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "200");
			} else if (conditionParameter.get("DJLX").equals("300")) {// 变更登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "300");
			} else if (conditionParameter.get("DJLX").equals("400")) {// 注销登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "400");
			} else if (conditionParameter.get("DJLX").equals("500")) {// 更正登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "500");
			} else if (conditionParameter.get("DJLX").equals("600")) {// 异议登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "600");
			} else if (conditionParameter.get("DJLX").equals("700")) {// 预告 登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "700");
			} else if (conditionParameter.get("DJLX").equals("800")) {// 查封登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "800");
			} else if (conditionParameter.get("DJLX").equals("900")) {// 其他登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "900");
			} else {
				fromsql.append(" ");
			}
		}

		if (!StringUtils.isEmpty(conditionParameter.get("ZL"))) {
			fromsql.append("  AND DY.ZL LIKE:ZL ");
			try {
				newpara.put("ZL", "%" + new String(conditionParameter.get("ZL").getBytes("iso8859-1"), "utf-8") + "%");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//新增查抵押权人--方便银行统计
		if (!StringUtils.isEmpty(conditionParameter.get("QLRMC"))) {
			fromsql.append("  AND QLR.QLRMC LIKE:QLRMC ");
			try {
				newpara.put("QLRMC", "%" + new String(conditionParameter.get("QLRMC").getBytes("iso8859-1"), "utf-8") + "%");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (!StringUtils.isEmpty(conditionParameter.get("DYR"))) {
			fromsql.append("  AND FSQL.DYR LIKE:DYR ");
			try {
				newpara.put("DYR",
						"%" + new String(conditionParameter.get("DYR").getBytes("iso8859-1"), "utf-8") + "%");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if ("0".equals(conditionParameter.get("TDYT"))) {// 土地用途
			fromsql.append(" ");
		} else {
			fromsql.append("  AND (DY.TDYT LIKE:TDYT OR BDCDYID IN(SELECT TDYTS.BDCDYID FROM BDCK.BDCS_TDYT_XZ TDYTS WHERE TDYTS.TDYT =:TDYT)) ");
			newpara.put("TDYT", conditionParameter.get("TDYT"));
		}

		if (!StringUtils.isEmpty(conditionParameter.get("SLSJ_Q"))) {
			fromsql.append("  AND XMXX.SLSJ >=to_date('" + conditionParameter.get("SLSJ_Q") + " 00:00:00"
					+ "','YYYY-MM-DD HH24:MI:SS') ");
			newpara.put("SLSJ_Q",
					new String(conditionParameter.get("SLSJ_Q").getBytes("iso8859-1"), "utf-8") + " 00:00:00");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("SLSJ_Z"))) {
			fromsql.append("  AND XMXX.SLSJ <=to_date('" + conditionParameter.get("SLSJ_Z") + " 23:59:59"
					+ "','YYYY-MM-DD HH24:MI:SS') ");
			newpara.put("SLSJ_Z",
					new String(conditionParameter.get("SLSJ_Z").getBytes("iso8859-1"), "utf-8") + " 23:59:59");
		}
		String fromsql1 = fromsql.toString();
		//如果登记类型是注销登记，查询历史层
		if (!StringUtils.isEmpty(conditionParameter.get("DJLX")) && conditionParameter.get("DJLX").equals("400")) {
			fromsql1 = fromsql1.replaceAll("_xz", "_ls").replaceAll("_XZ", "_LS").replaceAll("_xZ", "_LS");
		}
		Long total = baseCommonDao.getCountByFullSql(fromsql1, newpara);
		if (total == 0)
			return new Message();
		StringBuilder selectSql = new StringBuilder();
		//SQR.LXDH AS DH,sqr.sqrlx,sqr.zjh DYRZJH,sqr.sqrlx,sqr.zjh DYRZJH,
		selectSql
				.append(" SELECT "+bjsj+",DY.ZL,DY.BDCDYH,syql.bdcqzh AS syqzh,dylx.consttrans BDCDYLX,QLR.QLRMC,QL.BDCQZH,djlx.consttrans AS djlx,to_char(XMXX.SLSJ,'yyyy-mm-dd HH24:mi:ss') AS slsj,XMXX.YWLSH,XMXX.XMBH,");
		if (!StringUtils.isEmpty(conditionParameter.get("DJLX")) && conditionParameter.get("DJLX").equals("400")) {
			selectSql.append("fsql.dyr,dyfs.consttrans AS dyfs,to_char(ql.qlqssj,'yyyy-mm-dd') AS qlqssj,to_char(ql.qljssj,'yyyy-mm-dd') AS qljssj,FSQL.ZXDYYY as DJYY,DY.ZDMJ,DY.SYQMJ,DY.JZMJ,fsql.dypgjz as pgjz,DY.GHYT,DY.TDYT,DY.BDCDYID,DY.QLXZ,DY.FTTDMJ,");	
		}else {
			selectSql.append("fsql.dyr,dyfs.consttrans AS dyfs,to_char(ql.qlqssj,'yyyy-mm-dd') AS qlqssj,to_char(ql.qljssj,'yyyy-mm-dd') AS qljssj,QL.DJYY ,DY.ZDMJ,DY.SYQMJ,DY.JZMJ,fsql.dypgjz as pgjz,DY.GHYT,DY.TDYT,DY.BDCDYID,DY.QLXZ,DY.FTTDMJ,");	
		}
				
		selectSql.append("nvl(fsql.zgzqse,0)+nvl(fsql.bdbzzqse,0) AS dye,djdy.djdyid,row_number() over(ORDER BY QL.BDCQZH ASC) AS xh ");
		
//		List<Map> result = baseCommonDao.getPageDataByFullSql(selectSql.append(fromsql1).toString(), newpara,
//				currentpage, pageSize);

		 /**
         *传入完整的sql语句，在数据库中创建临时表
         */
		 String initfromsql=StringHelper.formatSqlByMapParameter(selectSql.append(fromsql1).toString(),newpara);
        exeCreatSql=StringHelper.createTable("BDCK", temptable, initfromsql);
        exeDropSql=StringHelper.dropTable("BDCK", temptable);
        try {
			baseCommonDao.excuteQueryNoResult(exeCreatSql);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("创建临时表出现错误");
			e.printStackTrace();
		}
        List<Map> result=baseCommonDao.getPageDataByFullSql("SELECT * FROM  BDCK."+temptable,currentpage, pageSize);
        
        //获取房屋、宗地相关信息
		 String houseandlandsql="SELECT SHYQZD.QLXZ,SHYQZD.ZDMJ,A.BDCDYLX,A.BDCDYID,TDYT.Tdyt,TDYT.tdytmc,TDYT.ID ,H.ZDBDCDYID FROM BDCK."+temptable+" A "
		 		     + " LEFT JOIN BDCK.BDCS_H_XZ H "
		 		     + " ON H.BDCDYID=A.BDCDYID "
		 		     + " LEFT JOIN BDCK.BDCS_SHYQZD_XZ SHYQZD "
		 		     + " ON H.ZDBDCDYID=SHYQZD.BDCDYID "
		 		     + " LEFT JOIN BDCK.BDCS_TDYT_XZ TDYT "
		 		     + " ON TDYT.BDCDYID= SHYQZD.BDCDYID"
		 		  //   + " WHERE A.BDCDYLX='031' "
		 		     + " UNION "
		 		     + " SELECT SHYQZD.QLXZ,SHYQZD.ZDMJ,A.BDCDYLX,A.BDCDYID,TDYT.Tdyt,TDYT.tdytmc,TDYT.ID ,YCH.ZDBDCDYID  FROM BDCK."+temptable+" A "
		 		     + " LEFT JOIN BDCK.BDCS_H_XZY YCH "
		 		     + " ON YCH.BDCDYID=A.BDCDYID "
		 		     + " LEFT JOIN BDCK.BDCS_SHYQZD_XZ SHYQZD "
		 		     + " LEFT JOIN BDCK.BDCS_TDYT_XZ TDYT "
		 		     + " ON TDYT.BDCDYID= SHYQZD.BDCDYID"
		 		     + " ON YCH.ZDBDCDYID =SHYQZD.BDCDYID ";
		 		  //   + " WHERE A.BDCDYLX='032' ";
		
		 //获取土地用途数据
		 String landsql= " SELECT A.BDCDYID,TDYT.Tdyt,TDYT.tdytmc ,TDYT.ID FROM BDCK."+temptable+" A "
		 		       + " LEFT JOIN BDCK.BDCS_TDYT_XZ TDYT "
		 		       + " ON TDYT.BDCDYID=A.BDCDYID "
		 		       + " WHERE A.BDCDYLX IN('使用权宗地','所有权宗地')";
        
		 List<Map> houseandlandmap=baseCommonDao.getDataListByFullSql(houseandlandsql);
		 Map<String,Map<String,String>> houseandlandmaps=joinHouseandlandInfos(houseandlandmap);
			
		 List<Map> landmap=baseCommonDao.getDataListByFullSql(landsql);//获取土地用途数据
		 Map<String,Map<String,String>> comlandmap=joinLandTdyts(landmap);
        				 		 
		for (Map m : result) {
			String xmbh=StringHelper.FormatByDatatype(m.get("XMBH"));
			//TODO comhousemap
			String bdcdylx=StringHelper.FormatByDatatype(m.get("BDCDYLX"));
			if(bdcdylx.equals("户") || bdcdylx.equals("预测户")){
				String bdcdyid=StringHelper.FormatByDatatype(m.get("BDCDYID"));
				String tdyt = StringHelper.formatObject(m.get("TDYT"));
				m.put("TDYT", ConstHelper.getNameByValue("TDYT", tdyt));
				if(!StringHelper.isEmpty(bdcdyid)){
					if(!StringHelper.isEmpty(houseandlandmaps) && houseandlandmaps.size()>0){
						Map<String,String> temphouseandlandmap=houseandlandmaps.get(bdcdyid);
						if(!StringHelper.isEmpty(temphouseandlandmap) && temphouseandlandmap.size()>0){
							String tempqlxz=StringHelper.FormatByDatatype(temphouseandlandmap.get("QLXZ"));
							String tempzdmj=StringHelper.FormatByDatatype(temphouseandlandmap.get("ZDMJ"));
							m.put("QLXZ", tempqlxz);
							m.put("ZDMJ", tempzdmj);
							String tdyt1=StringHelper.FormatByDatatype(m.get("TDYT"));
							if(StringHelper.isEmpty(tdyt1)){
								m.put("TDYT", StringHelper.FormatByDatatype(temphouseandlandmap.get("TDYT")));
							}
						}
						
					}
				}
			}else if(bdcdylx.equals("使用权宗地") || bdcdylx.equals("所有权宗地")){
				String bdcdyid=StringHelper.FormatByDatatype(m.get("BDCDYID"));
				if(!StringHelper.isEmpty(bdcdyid)){
					if(!StringHelper.isEmpty(comlandmap) && comlandmap.size()>0){
						if(!StringHelper.isEmpty(bdcdyid)){
							Map<String,String> templandmap=comlandmap.get(bdcdyid);
							if(!StringHelper.isEmpty(templandmap) && templandmap.size()>0){
								String temptdyt=StringHelper.FormatByDatatype(templandmap.get("TDYT"));
								m.put("QLXZ", ConstHelper.getNameByValue("QLXZ", (String) m.get("QLXZ")));
								m.put("TDYT", temptdyt);
							}
						}
					}
				}
			}else{
				m.put("ZDMJ", StringHelper.formatObject(m.get("ZDMJ")));
				m.put("TDYT", ConstHelper.getNameByValue("TDYT", (String) m.get("TDYT")));
				if (m.containsKey("QLXZ")) {
					m.put("QLXZ", ConstHelper.getNameByValue("QLXZ", (String) m.get("QLXZ")));
				}
			}
			
			m.put("FWYT", ConstHelper.getNameByValue("FWYT", (String) m.get("GHYT")));
			if (!StringUtils.isEmpty(conditionParameter.get("DJLX")) && conditionParameter.get("DJLX").equals("400")) {
				m.put("DJLX", ConstHelper.getNameByValue("DJLX", "400"));
			}
			String syqmjZd=StringHelper.formatObject(m.get("SYQMJ"));
			if ("0".equals(syqmjZd)) {
				m.put("SYQMJ","-");//为0，显示-
			}
//			String SQRLXs = StringHelper.formatObject(m.get("SQRLX"));
//			String sqrlxStr = "";
//			if (!"".equals(SQRLXs) ) {
//				if (SQRLXs.contains(",")) {
//					String[] sqrlxArr = SQRLXs.split(",");
//					for (String sqrlx : sqrlxArr) {
//						sqrlxStr = sqrlxStr+ConstHelper.getNameByValue("QLRLX", StringHelper.formatObject(sqrlx))+",";
//					}
//					if("".equals(sqrlxStr)) {
//						m.put("SQRLX","");
//					}else {
//						m.put("SQRLX",sqrlxStr.substring(0,sqrlxStr.length()-1));
//					}
//				}else {
//					m.put("SQRLX",);
//				}
//				 
//			}
			
			if(!StringHelper.isEmpty(m.get("DYR"))){
				if(m.get("DYR").toString().contains(",")){
					String[] arrStr = m.get("DYR").toString().split(",");
					String sqrlx ="";
					String lxdh = "";
					String zjh= "";
					String sqrlx_str="";
					String lxdh_str="";
					String zjh_str="";
					for(int i=0;i<arrStr.length;i++){
						List<BDCS_SQR> sqrInfo = baseCommonDao.getDataList(BDCS_SQR.class, "SQRXM = '" + arrStr[i] +"' and XMBH = '" +StringHelper.formatObject(m.get("XMBH"))+ "'");
						if(!StringHelper.isEmpty(sqrInfo)  && sqrInfo.size()>0){
							if(sqrInfo.get(0).getSQRLX() != null){
								sqrlx+=ConstHelper.getNameByValue("QLRLX", StringHelper.formatObject(sqrInfo.get(0).getSQRLX()))+",";
							}
							if(sqrInfo.get(0).getLXDH() != null){
								lxdh+=sqrInfo.get(0).getLXDH()+",";
							}
							if(sqrInfo.get(0).getZJH() != null){
								zjh+=sqrInfo.get(0).getZJH()+",";
							}
						}
					}
					if(sqrlx !="") sqrlx_str=sqrlx.substring(0, sqrlx.length()-1);
					if(lxdh !="") lxdh_str=lxdh.substring(0, lxdh.length()-1);
					if(zjh !="") zjh_str=zjh.substring(0, zjh.length()-1);
					m.put("SQRLX", sqrlx_str);
					m.put("DH", lxdh_str);
					m.put("DYRZJH", zjh_str);
				} else if(m.get("DYR").toString().contains("、")){
					String[] arrStr = m.get("DYR").toString().split("、");
					String sqrlx ="";
					String lxdh = "";
					String zjh= "";
					String sqrlx_str="";
					String lxdh_str="";
					String zjh_str="";
					for(int i=0;i<arrStr.length;i++){
						List<BDCS_SQR> sqrInfo = baseCommonDao.getDataList(BDCS_SQR.class, "SQRXM = '" + arrStr[i] +"' and XMBH = '" +StringHelper.formatObject(m.get("XMBH"))+ "'");
						if(!StringHelper.isEmpty(sqrInfo)  && sqrInfo.size()>0){
							if(sqrInfo.get(0).getSQRLX() != null){
								sqrlx+=ConstHelper.getNameByValue("QLRLX", StringHelper.formatObject(sqrInfo.get(0).getSQRLX()))+",";
							}
							if(sqrInfo.get(0).getLXDH() != null){
								lxdh+=sqrInfo.get(0).getLXDH()+",";
							}
							if(sqrInfo.get(0).getZJH() != null){
								zjh+=sqrInfo.get(0).getZJH()+",";
							}
						}
					}
					if(sqrlx !="") sqrlx_str=sqrlx.substring(0, sqrlx.length()-1);
					if(lxdh !="") lxdh_str=lxdh.substring(0, lxdh.length()-1);
					if(zjh !="") zjh_str=zjh.substring(0, zjh.length()-1);
					m.put("SQRLX", sqrlx_str);
					m.put("DH", lxdh_str);
					m.put("DYRZJH", zjh_str);
				}else{
					List<BDCS_SQR> sqrInfo = baseCommonDao.getDataList(BDCS_SQR.class, "SQRXM = '" + m.get("DYR").toString() +"' and XMBH = '" +StringHelper.formatObject(m.get("XMBH"))+ "'");
					if(!StringHelper.isEmpty(sqrInfo) && sqrInfo.size()>0 ){
						m.put("SQRLX", ConstHelper.getNameByValue("QLRLX", StringHelper.formatObject(sqrInfo.get(0).getSQRLX())));
						m.put("DH", sqrInfo.get(0).getLXDH());
						m.put("DYRZJH", sqrInfo.get(0).getZJH());
					}
				}
			}
			
			
		}
		//导出完成后，干掉临时创建的表，要不然数据库会有临时表存在
		 try {
				baseCommonDao.excuteQueryNoResult(exeDropSql);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("删除临时表出现错误");
				e.printStackTrace();
			}		
		Message msg = new Message();
		msg.setRows(result);
		msg.setTotal(total);
		return msg;
	}
	
	@SuppressWarnings({ "rawtypes" })
	@Override
	public HashMap<String, String> GetDiyInfo_HZ(Map<String, String> conditionParameter) throws Exception {
		Map<String, String> newpara = new HashMap<String, String>();
		HashMap<String, String> result = new HashMap<String, String>();
		StringBuilder hzsql = new StringBuilder()
				.append("SELECT SUM(HJZMJ) AS HJZMJ,SUM(YHJZMJ) AS YHJZMJ,SUM(SHYQMJ) AS SHYQMJ,SUM(SYQMJ) AS SYQMJ,SUM(DYJE) AS DYJE ")
				.append("FROM ( ")
				.append("SELECT QL.BDCQZH,SUM(DY.SCJZMJ) AS HJZMJ,SUM(DY.YCJZMJ) AS YHJZMJ,SUM(DY.SHYQMJ) AS SHYQMJ,SUM(DY.SYQMJ) AS SYQMJ,MAX(nvl(fsql.zgzqse, 0) + nvl(fsql.bdbzzqse, 0)) AS DYJE ")
				.append("FROM  BDCK.BDCS_QL_XZ QL ")
				.append("LEFT JOIN BDCK.BDCS_FSQL_xZ FSQL ON QL.QLID=FSQL.QLID ")
				.append("LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON QL.DJDYID=DJDY.DJDYID ").append("LEFT JOIN ")
				.append("(SELECT 0 AS SCJZMJ,YCJZMJ AS YCJZMJ,0 AS SHYQMJ,0 AS SYQMJ,BDCDYID,ZL,FWTDYT AS TDYT,'032' AS BDCDYLX FROM BDCK.BDCS_H_XZY ")
				.append("UNION ")
				.append("SELECT SCJZMJ AS SCJZMJ,0 AS YCJZMJ,0 AS SHYQMJ,0 AS SYQMJ,BDCDYID,ZL,FWTDYT AS TDYT,'031' AS BDCDYLX FROM BDCK.BDCS_H_XZ ")
				.append("UNION  ")
				.append("SELECT 0 AS SCJZMJ,0 AS YCJZMJ,ZDMJ AS SHYQMJ,0 AS SYQMJ,BDCDYID,ZL,YT AS TDYT,'02' AS BDCDYLX FROM BDCK.BDCS_SHYQZD_XZ ")
				.append("UNION  ")
				.append("SELECT 0 AS SCJZMJ,0 AS YCJZMJ,0 AS SHYQMJ,ZDMJ AS SYQMJ,BDCDYID,ZL,YT AS TDYT,'01' AS BDCDYLX FROM BDCK.BDCS_SYQZD_XZ)DY ON DY.BDCDYID=DJDY.BDCDYID AND DY.BDCDYLX=DJDY.BDCDYLX ")
				.append("LEFT JOIN (SELECT DISTINCT TO_CHAR(WM_CONCAT(TO_CHAR(QLRMC))) AS QLRMC,QLID FROM BDCK.BDCS_QLR_XZ GROUP BY QLID) QLR ON QLR.QLID=QL.QLID  ");
		if (!StringUtils.isEmpty(conditionParameter.get("DJLX")) && conditionParameter.get("DJLX").equals("400")) {
			hzsql.append("LEFT JOIN (SELECT a.djdyid, a.bdcqzh FROM bdck.bdcs_ql_ls a left join bdck.bdcs_fsql_ls b on a.qlid = b.qlid WHERE a.qllx IN ('23') and b.zxsj is not null) syql ON djdy.djdyid=syql.djdyid ");
		}else {
			hzsql.append("LEFT JOIN (SELECT a.djdyid,a.bdcqzh FROM bdck.bdcs_ql_xz a WHERE a.qllx IN ('3','4','7','8','11','15','24')) syql ON djdy.djdyid=syql.djdyid ");
		}
		
		hzsql.append("LEFT JOIN ( ")
		.append("SELECT con.constvalue,con.consttrans FROM bdck.bdcs_const con LEFT JOIN bdck.bdcs_constcls const ON con.constslsid=const.constslsid ")
		.append("WHERE const.constclstype = 'DYFS') dyfs ON fsql.dyfs=dyfs.constvalue ").append("LEFT JOIN ( ")
		.append("SELECT con.constvalue,con.consttrans FROM bdck.bdcs_const con LEFT JOIN bdck.bdcs_constcls const ON con.constslsid=const.constslsid ")
		.append("WHERE const.constclstype = 'BDCDYLX')  dylx ON djdy.bdcdylx=dylx.constvalue ");
		if (!StringUtils.isEmpty(conditionParameter.get("DJLX")) && conditionParameter.get("DJLX").equals("400")) {
			//LEFT JOIN (SELECT sqr1.xmbh, wm_concat(TO_CHAR(sqr1.sqrxm) ) AS sqrxm, wm_concat(TO_CHAR(sqr1.zjh) ) AS zjh, wm_concat(TO_CHAR(sqr1.sqrlx) ) AS sqrlx, sqr1.sqrlb, wm_concat(TO_CHAR(sqr1.LXDH) ) AS LXDH FROM bdck.bdcs_sqr sqr1  GROUP BY sqr1.xmbh, sqr1.sqrlb) SQR on XMXX.XMBH = SQR.XMBH  AND SQR.SQRLB ='2' and xmxx.xmbh is not null
			hzsql.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON XMXX.PROJECT_ID = FSQL.ZXDYYWH LEFT JOIN (SELECT con.constvalue,con.consttrans FROM bdck.bdcs_const con LEFT JOIN bdck.bdcs_constcls const ON con.constslsid=const.constslsid WHERE const.constclstype = 'DJLX') djlx ON XMXX.djlx=djlx.constvalue ").append(" WHERE  ql.qllx='23' ");
		}else{
			// LEFT JOIN (SELECT sqr1.xmbh, wm_concat(TO_CHAR(sqr1.sqrxm) ) AS sqrxm, wm_concat(TO_CHAR(sqr1.zjh) ) AS zjh, wm_concat(TO_CHAR(sqr1.sqrlx) ) AS sqrlx, sqr1.sqrlb, wm_concat(TO_CHAR(sqr1.LXDH) ) AS LXDH FROM bdck.bdcs_sqr sqr1  GROUP BY sqr1.xmbh, sqr1.sqrlb) SQR on XMXX.XMBH = SQR.XMBH  AND SQR.SQRLB ='2' and xmxx.xmbh is not null 
			hzsql.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON XMXX.XMBH = QL.XMBH LEFT JOIN (SELECT con.constvalue,con.consttrans FROM bdck.bdcs_const con LEFT JOIN bdck.bdcs_constcls const ON con.constslsid=const.constslsid WHERE const.constclstype = 'DJLX') djlx ON XMXX.djlx=djlx.constvalue  ").append(" WHERE  ql.qllx='23' ");
		}
//				.append("LEFT JOIN (SELECT a.djdyid,a.bdcqzh FROM bdck.bdcs_ql_xz a WHERE a.qllx IN ('3','4','7','8','11','15','24')) syql ON djdy.djdyid=syql.djdyid ")
//				.append("LEFT JOIN ( ")
//				.append("SELECT con.constvalue,con.consttrans FROM bdck.bdcs_const con LEFT JOIN bdck.bdcs_constcls const ON con.constslsid=const.constslsid ")
//				.append("WHERE const.constclstype = 'DJLX') djlx ON ql.djlx=djlx.constvalue ").append("LEFT JOIN ( ")
//				.append("SELECT con.constvalue,con.consttrans FROM bdck.bdcs_const con LEFT JOIN bdck.bdcs_constcls const ON con.constslsid=const.constslsid ")
//				.append("WHERE const.constclstype = 'DYFS') dyfs ON fsql.dyfs=dyfs.constvalue ").append("LEFT JOIN ( ")
//				.append("SELECT con.constvalue,con.consttrans FROM bdck.bdcs_const con LEFT JOIN bdck.bdcs_constcls const ON con.constslsid=const.constslsid ")
//				.append("WHERE const.constclstype = 'BDCDYLX')  dylx ON djdy.bdcdylx=dylx.constvalue ")
//				.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON XMXX.XMBH = QL.XMBH LEFT JOIN (SELECT con.constvalue,con.consttrans FROM bdck.bdcs_const con LEFT JOIN bdck.bdcs_constcls const ON con.constslsid=const.constslsid WHERE const.constclstype = 'DJLX') djlx ON XMXX.djlx=djlx.constvalue  LEFT JOIN BDCK.BDCS_SQR SQR on XMXX.XMBH = SQR.XMBH AND SQR.SQRLB ='2' ").append("WHERE  ql.qllx='23' ");

		if (!StringUtils.isEmpty(conditionParameter.get("DJSJ_Q"))) {
			hzsql.append("  AND QL.DJSJ >=to_date(:DJSJ_Q,'YYYY-MM-DD HH24:MI:SS') ");
			newpara.put("DJSJ_Q",
					new String(conditionParameter.get("DJSJ_Q").getBytes("iso8859-1"), "utf-8") + " 00:00:00");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("DJSJ_Z"))) {
			hzsql.append("  AND QL.DJSJ <=to_date(:DJSJ_Z,'YYYY-MM-DD HH24:MI:SS') ");
			newpara.put("DJSJ_Z",
					new String(conditionParameter.get("DJSJ_Z").getBytes("iso8859-1"), "utf-8") + " 23:59:59");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("BDCQZH"))) {
			hzsql.append("  AND QL.BDCQZH LIKE:BDCQZH ");
			newpara.put("BDCQZH",
					"%" + new String(conditionParameter.get("BDCQZH").getBytes("iso8859-1"), "utf-8") + "%");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("YWLSH"))) {
			hzsql.append("  AND XMXX.YWLSH LIKE:YWLSH ");
			newpara.put("YWLSH",
					"%" + new String(conditionParameter.get("YWLSH").getBytes("iso8859-1"), "utf-8") + "%");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("BDCDYH"))) {
			hzsql.append("  AND DJDY.BDCDYH LIKE:BDCDYH ");
			newpara.put("BDCDYH",
					"%" + new String(conditionParameter.get("BDCDYH").getBytes("iso8859-1"), "utf-8") + "%");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("TJLX"))) {
			if (conditionParameter.get("TJLX").equals("1"))// 房地都查
				hzsql.append("  AND DJDY.BDCDYLX IN('01','02','031','032')");
			else if (conditionParameter.get("TJLX").equals("2"))// 房
				hzsql.append("  AND DJDY.BDCDYLX IN('031','032')");
			else if (conditionParameter.get("TJLX").equals("3"))// 宗地
				hzsql.append("  AND DJDY.BDCDYLX IN('01','02')");
			else if (conditionParameter.get("TJLX").equals("4"))// 使用权
				hzsql.append("  AND DJDY.BDCDYLX IN('02')");
			else if (conditionParameter.get("TJLX").equals("5"))// 所有权宗地
				hzsql.append("  AND DJDY.BDCDYLX IN('01')");
			else if (conditionParameter.get("TJLX").equals("6"))// 预测户
				hzsql.append("  AND DJDY.BDCDYLX IN('032')");
			else if (conditionParameter.get("TJLX").equals("7"))// 实测户
				hzsql.append("  AND DJDY.BDCDYLX IN('031')");
			else if (conditionParameter.get("TJLX").equals("8"))// 海域
				hzsql.append("  AND DJDY.BDCDYLX IN('04')");
			else if (conditionParameter.get("TJLX").equals("9"))// 林地
				hzsql.append("  AND DJDY.BDCDYLX IN('05')");
			else if (conditionParameter.get("TJLX").equals("10"))// 农用地
				hzsql.append("  AND DJDY.BDCDYLX IN('09')");
			else
				hzsql.append("  AND DJDY.BDCDYLX IN('031','032') ");

		}
		// liangc 增加登记类型、坐落、抵押人、受理时间查询条件
		if (!StringUtils.isEmpty(conditionParameter.get("DJLX"))) {
			if (conditionParameter.get("DJLX").equals("000"))// 全部登记类型
				hzsql.append(" ");
			else if (conditionParameter.get("DJLX").equals("100")) {// 首次登记
				hzsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "100");
			} else if (conditionParameter.get("DJLX").equals("200")) {// 转移登记
				hzsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "200");
			} else if (conditionParameter.get("DJLX").equals("300")) {// 变更登记
				hzsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "300");
			} else if (conditionParameter.get("DJLX").equals("400")) {// 注销登记
				hzsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "400");
			} else if (conditionParameter.get("DJLX").equals("500")) {// 更正登记
				hzsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "500");
			} else if (conditionParameter.get("DJLX").equals("600")) {// 异议登记
				hzsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "600");
			} else if (conditionParameter.get("DJLX").equals("700")) {// 预告 登记
				hzsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "700");
			} else if (conditionParameter.get("DJLX").equals("800")) {// 查封登记
				hzsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "800");
			} else if (conditionParameter.get("DJLX").equals("900")) {// 其他登记
				hzsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "900");
			} else {
				hzsql.append(" ");
			}
		}

		if (!StringUtils.isEmpty(conditionParameter.get("ZL"))) {
			hzsql.append("  AND DY.ZL LIKE:ZL ");
			try {
				newpara.put("ZL", "%" + new String(conditionParameter.get("ZL").getBytes("iso8859-1"), "utf-8") + "%");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (!StringUtils.isEmpty(conditionParameter.get("DYR"))) {
			hzsql.append("  AND FSQL.DYR LIKE:DYR ");
			try {
				newpara.put("DYR",
						"%" + new String(conditionParameter.get("DYR").getBytes("iso8859-1"), "utf-8") + "%");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (conditionParameter.get("TDYT").equals("0")) {// 土地用途
			hzsql.append(" ");
		} else {
			hzsql.append("  AND (DY.TDYT LIKE:TDYT OR BDCDYID IN(SELECT TDYTS.BDCDYID FROM BDCK.BDCS_TDYT_XZ TDYTS WHERE TDYTS.TDYT =:TDYT)) ");
			newpara.put("TDYT", conditionParameter.get("TDYT"));
		}

		if (!StringUtils.isEmpty(conditionParameter.get("SLSJ_Q"))) {
			hzsql.append("  AND XMXX.SLSJ >=to_date('" + conditionParameter.get("SLSJ_Q") + " 00:00:00"
					+ "','YYYY-MM-DD HH24:MI:SS') ");
			newpara.put("SLSJ_Q",
					new String(conditionParameter.get("SLSJ_Q").getBytes("iso8859-1"), "utf-8") + " 00:00:00");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("SLSJ_Z"))) {
			hzsql.append("  AND XMXX.SLSJ <=to_date('" + conditionParameter.get("SLSJ_Z") + " 23:59:59"
					+ "','YYYY-MM-DD HH24:MI:SS') ");
			newpara.put("SLSJ_Z",
					new String(conditionParameter.get("SLSJ_Z").getBytes("iso8859-1"), "utf-8") + " 23:59:59");
		}
		
		hzsql.append(" GROUP BY QL.BDCQZH ) b");
		String fromsql = hzsql.toString();
		//如果登记类型是注销登记，查询历史层
		if (!StringUtils.isEmpty(conditionParameter.get("DJLX")) && conditionParameter.get("DJLX").equals("400")) {
			fromsql = fromsql.replaceAll("_xz", "_ls").replaceAll("_XZ", "_LS");
		}
		List<Map> hz = baseCommonDao.getDataListByFullSql(fromsql,newpara); // 调用sql查询
		Map map = hz.get(0); // 取查询结果第一行
		result.put("HJZMJ", StringHelper.formatDouble(map.get("HJZMJ"))); // put()方法，a是要保存到map集合中的键名，b保存到map集合中对应键名的键值对象
		result.put("YHJZMJ", StringHelper.formatDouble(map.get("YHJZMJ"))); // double转字符串并获取
		result.put("SHYQMJ", StringHelper.formatDouble(map.get("SHYQMJ")));
		result.put("SYQMJ", StringHelper.formatDouble(map.get("SYQMJ")));
		result.put("DYJE", StringHelper.formatDouble(map.get("DYJE")));
		
		return result;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public List<Map> DiyInfoDownload(Map<String, String> conditionParameter)
			throws Exception {
		Map<String, String> newpara = new HashMap<String, String>();
		StringBuilder fromsql = new StringBuilder(" FROM  BDCK.BDCS_QL_XZ QL ")
				.append("LEFT JOIN bdck.bdcs_qdzr_xz qdzr ON ql.qlid=qdzr.qlid ")
				.append("LEFT JOIN BDCK.BDCS_FSQL_xZ FSQL ON QL.QLID=FSQL.QLID ")
				.append("LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON qdzr.DJDYID=DJDY.DJDYID ")
				.append("LEFT JOIN ")
				.append("(SELECT BDCDYID,ZL,BDCDYH,'032' AS BDCDYLX,GHYT,FWXZ,(NVL(FTTDMJ,0)+NVL(DYTDMJ,0)) AS SYQMJ,YCJZMJ AS JZMJ,YCFTJZMJ AS FTJZMJ,YCTNJZMJ AS ZYJZMJ,NULL AS ZDMJ,FWTDYT AS TDYT,QLXZ,FTTDMJ FROM BDCK.BDCS_H_XZY ")
				.append("UNION  ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'031' AS BDCDYLX,GHYT,FWXZ,(NVL(FTTDMJ,0)+NVL(DYTDMJ,0)) AS SYQMJ,SCJZMJ AS JZMJ,SCFTJZMJ AS FTJZMJ,SCTNJZMJ AS ZYJZMJ,NULL AS ZDMJ,FWTDYT AS TDYT,QLXZ,FTTDMJ FROM BDCK.BDCS_H_XZ ")
				.append("UNION  ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'02' AS BDCDYLX,NULL AS GHYT, NULL AS FWXZ,NULL AS SYQMJ,NULL AS JZMJ,NULL AS FTJZMJ,NULL AS ZYJZMJ , ZDMJ,NULL AS TDYT,QLXZ,NULL AS FTTDMJ FROM BDCK.BDCS_SHYQZD_XZ ")
				.append("UNION  ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'01' AS BDCDYLX,NULL AS GHYT, NULL AS FWXZ,NULL AS SYQMJ,NULL AS JZMJ,NULL AS FTJZMJ,NULL AS ZYJZMJ,NULL AS ZDMJ,NULL AS TDYT,QLXZ,NULL AS FTTDMJ FROM BDCK.BDCS_SYQZD_XZ ")
				.append("UNION  ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'04' AS BDCDYLX,NULL AS GHYT, NULL AS FWXZ,YHZMJ AS SYQMJ,NULL AS JZMJ,NULL AS FTJZMJ,NULL AS ZYJZMJ,NULL AS ZDMJ,NULL AS TDYT,NULL AS QLXZ,NULL AS FTTDMJ FROM BDCK.BDCS_ZH_XZ ")
				.append("UNION  ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'05' AS BDCDYLX,NULL AS GHYT, NULL AS FWXZ,SYQMJ,NULL AS JZMJ,NULL AS FTJZMJ,NULL AS ZYJZMJ,NULL AS ZDMJ,NULL AS TDYT,NULL AS QLXZ,NULL AS FTTDMJ FROM BDCK.BDCS_SLLM_XZ ")
				.append("UNION  ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'09' AS BDCDYLX,NULL AS GHYT, NULL AS FWXZ,CBMJ AS SYQMJ,NULL AS JZMJ,NULL AS FTJZMJ,NULL AS ZYJZMJ,NULL AS ZDMJ,YT AS TDYT,QLXZ,NULL AS FTTDMJ FROM BDCK.BDCS_NYD_XZ)DY ON DY.BDCDYID=DJDY.BDCDYID AND DY.BDCDYLX=DJDY.BDCDYLX ")
				.append("LEFT JOIN bdck.bdcs_qlr_xz qlr ON ql.qlid=qlr.qlid ")
				.append("LEFT JOIN (SELECT a.djdyid,a.bdcqzh FROM bdck.bdcs_ql_xz a WHERE a.qllx IN ('3','4','7','8','11','15','24')) syql ON djdy.djdyid=syql.djdyid ")
				.append("LEFT JOIN ( ")
				.append("SELECT con.constvalue,con.consttrans FROM bdck.bdcs_const con LEFT JOIN bdck.bdcs_constcls const ON con.constslsid=const.constslsid ")
				.append("WHERE const.constclstype = 'DJLX') djlx ON ql.djlx=djlx.constvalue ")
				.append("LEFT JOIN ( ")
				.append("SELECT con.constvalue,con.consttrans FROM bdck.bdcs_const con LEFT JOIN bdck.bdcs_constcls const ON con.constslsid=const.constslsid ")
				.append("WHERE const.constclstype = 'DYFS') dyfs ON fsql.dyfs=dyfs.constvalue ")
				.append("LEFT JOIN ( ")
				.append("SELECT con.constvalue,con.consttrans FROM bdck.bdcs_const con LEFT JOIN bdck.bdcs_constcls const ON con.constslsid=const.constslsid ")
				.append("WHERE const.constclstype = 'BDCDYLX')  dylx ON djdy.bdcdylx=dylx.constvalue ")
				.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON XMXX.XMBH = QL.XMBH  LEFT JOIN BDCK.BDCS_SQR SQR on XMXX.XMBH = SQR.XMBH  AND SQR.SQRLB ='2' ")
				.append("WHERE  ql.qllx='23' ");

		if (!StringUtils.isEmpty(conditionParameter.get("DJSJ_Q"))) {
			fromsql.append("  AND QL.DJSJ >=to_date('"
					+ conditionParameter.get("DJSJ_Q") + "','yyyy-mm-dd  HH24:MI:SS') ");
			newpara.put("DJSJ_Q", new String(conditionParameter.get("DJSJ_Q")
					.getBytes("iso8859-1"), "utf-8") + " 00:00:00");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("DJSJ_Z"))) {
			fromsql.append("  AND QL.DJSJ <=to_date('"
					+ conditionParameter.get("DJSJ_Z") + "','yyyy-mm-dd  HH24:MI:SS') ");
			newpara.put("DJSJ_Z", new String(conditionParameter.get("DJSJ_Z")
					.getBytes("iso8859-1"), "utf-8") + " 23:59:59");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("BDCQZH"))) {
			fromsql.append("  AND QL.BDCQZH LIKE:BDCQZH ");
			newpara.put(
					"BDCQZH",
					"%"
							+ new String(conditionParameter.get("BDCQZH")
									.getBytes("iso8859-1"), "utf-8") + "%");
		}
		// if (!StringUtils.isEmpty(conditionParameter.get("SLBH"))) {
		// fromsql.append("  AND QL.BDCQZH LIKE:BDCQZH ");
		// newpara.put("BDCQZH", "%"+ new
		// String(conditionParameter.get("BDCQZH")
		// .getBytes("iso8859-1"), "utf-8")+ "%");
		// }
		if (!StringUtils.isEmpty(conditionParameter.get("BDCDYH"))) {
			fromsql.append("  AND DJDY.BDCDYH LIKE '%"
					+ conditionParameter.get("BDCDYH") + "%' ");
			newpara.put(
					"BDCDYH",
					"%"
							+ new String(conditionParameter.get("BDCDYH")
									.getBytes("iso8859-1"), "utf-8") + "%");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("TJLX"))) {
			if (conditionParameter.get("TJLX").equals("1"))// 房地都查
				fromsql.append("  AND DJDY.BDCDYLX IN('01','02','09','031','032') ");
			else if (conditionParameter.get("TJLX").equals("2"))// 房
				fromsql.append("  AND DJDY.BDCDYLX IN('031','032')  ");
			else if (conditionParameter.get("TJLX").equals("3"))// 地
				fromsql.append("  AND DJDY.BDCDYLX IN('01','02','09')  ");
			else
				fromsql.append("  AND DJDY.BDCDYLX IN('031','032')  ");
		}

		// liangc 增加登记类型、坐落、抵押人、受理时间查询条件
		if (!StringUtils.isEmpty(conditionParameter.get("DJLX"))) {
			if (conditionParameter.get("DJLX").equals("000"))// 全部登记类型
				fromsql.append(" ");
			else if (conditionParameter.get("DJLX").equals("100")) {// 首次登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "100");
			} else if (conditionParameter.get("DJLX").equals("200")) {// 转移登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "200");
			} else if (conditionParameter.get("DJLX").equals("300")) {// 变更登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "300");
			} else if (conditionParameter.get("DJLX").equals("400")) {// 注销登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "400");
			} else if (conditionParameter.get("DJLX").equals("500")) {// 更正登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "500");
			} else if (conditionParameter.get("DJLX").equals("600")) {// 异议登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "600");
			} else if (conditionParameter.get("DJLX").equals("700")) {// 预告 登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "700");
			} else if (conditionParameter.get("DJLX").equals("800")) {// 查封登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "800");
			} else if (conditionParameter.get("DJLX").equals("900")) {// 其他登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "900");
			} else {
				fromsql.append(" ");
			}
		}

		if (!StringUtils.isEmpty(conditionParameter.get("ZL"))) {
			fromsql.append("  AND DY.ZL LIKE:ZL ");
			try {
				newpara.put(
						"ZL",
						"%"
								+ new String(conditionParameter.get("ZL")
										.getBytes("iso8859-1"), "utf-8") + "%");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (!StringUtils.isEmpty(conditionParameter.get("DYR"))) {
			fromsql.append("  AND FSQL.DYR LIKE:DYR ");
			try {
				newpara.put("DYR",
						"%"
								+ new String(conditionParameter.get("DYR")
										.getBytes("iso8859-1"), "utf-8") + "%");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (conditionParameter.get("TDYT").equals("0")) {// 土地用途
			fromsql.append(" ");
		} else {
			fromsql.append("  AND (DY.TDYT LIKE:TDYT OR BDCDYID IN(SELECT TDYTS.BDCDYID FROM BDCK.BDCS_TDYT_XZ TDYTS WHERE TDYTS.TDYT =:TDYT)) ");
			newpara.put("TDYT", conditionParameter.get("TDYT"));
		}
		if (!StringUtils.isEmpty(conditionParameter.get("SLSJ_Q"))) {
			fromsql.append("  AND XMXX.SLSJ >=to_date('"
					+ conditionParameter.get("SLSJ_Q") + "','yyyy-mm-dd') ");
			newpara.put("SLSJ_Q", new String(conditionParameter.get("SLSJ_Q")
					.getBytes("iso8859-1"), "utf-8"));
		}
		if (!StringUtils.isEmpty(conditionParameter.get("SLSJ_Z"))) {
			fromsql.append("  AND XMXX.SLSJ <=to_date('"
					+ conditionParameter.get("SLSJ_Z") + "','yyyy-mm-dd') ");
			newpara.put("SLSJ_Z", new String(conditionParameter.get("SLSJ_Z")
					.getBytes("iso8859-1"), "utf-8"));
		}

		String fromsql1 = fromsql.toString();
		//如果登记类型是注销登记，查询历史层
		if (!StringUtils.isEmpty(conditionParameter.get("DJLX")) && conditionParameter.get("DJLX").equals("400")) {
			fromsql1 = fromsql1.replaceAll("_xz", "_ls").replaceAll("_XZ", "_LS");
		}
		StringBuilder selectSql = new StringBuilder();
		selectSql
				.append(" SELECT to_char(QL.DJSJ,'yyyy-mm-dd HH24:mi:ss') AS djsj,DY.ZL,DY.BDCDYH,syql.bdcqzh AS syqzh,dylx.consttrans BDCDYLX,QLR.QLRMC,SQR.LXDH AS DH,QL.BDCQZH,djlx.consttrans AS djlx,to_char(XMXX.SLSJ,'yyyy-mm-dd HH24:mi:ss') AS slsj,XMXX.YWLSH,")
				.append("fsql.dyr,dyfs.consttrans AS dyfs,to_char(ql.qlqssj,'yyyy-mm-dd') AS qlqssj,to_char(ql.qljssj,'yyyy-mm-dd') AS qljssj,QL.DJYY , DY.ZDMJ AS SYQMJ,DY.JZMJ,fsql.dypgjz as pgjz,DY.GHYT,DY.TDYT,DY.BDCDYID,DY.QLXZ,DY.FTTDMJ,")
				.append("nvl(fsql.zgzqse,0)+nvl(fsql.bdbzzqse,0) AS dye,fsql.zqdw,djdy.djdyid,row_number() over(ORDER BY QL.BDCQZH ASC) AS xh ");

		List<Map> result = baseCommonDao.getDataListByFullSql(
				selectSql.append(fromsql1).toString(), newpara);
		for (Map map : result) {
			/*
			 * String bdcdylx = StringHelper.formatObject(map.get("BDCDYLX"));
			 * if (map.get("BDCDYLX").equals("032")) { map.put("BDCDYLX",
			 * "预测户"); } else { map.put("BDCDYLX",
			 * ConstHelper.getNameByValue("BDCDYLX", bdcdylx)); }
			 */
			if (map.get("BDCDYLX") != null) {
				if (map.get("BDCDYLX").equals("户")
						|| map.get("BDCDYLX").equals("预测户")) {
					// 户关联到宗地获取宗地面积 liangq
					String sql = "select zrzbdcdyid from bdck.bdcs_h_xz where bdcdyh = '"
							+ map.get("BDCDYH") + "'";
					List<Map> h = baseCommonDao.getDataListByFullSql(sql);
					if (h.size() > 0) {
						sql = "select ZDBDCDYID from bdck.bdcs_zrz_xz where bdcdyid='"
								+ h.get(0).get("ZRZBDCDYID") + "'";
						List<Map> zrz = baseCommonDao.getDataListByFullSql(sql);
						if (zrz.size() > 0) {
							sql = "select ZDMJ,QLXZ from bdck.bdcs_shyqzd_xz where bdcdyid='"
									+ zrz.get(0).get("ZDBDCDYID") + "'";
							List<Map> zd = baseCommonDao
									.getDataListByFullSql(sql);
							if (zd.size() > 0) {
								map.put("SYQMJ", zd.get(0).get("ZDMJ"));
								map.put("QLXZ", zd.get(0).get("QLXZ"));
							}
						}
					}
					map.put("FWYT",
							ConstHelper.getNameByValue("FWYT",
									(String) map.get("GHYT")));
					map.put("TDYT",
							ConstHelper.getNameByValue("TDYT",
									(String) map.get("TDYT")));
					if ("".equals(map.get("TDYT"))) {
						if (map.get("BDCDYLX").equals("户")) {
							if (!StringHelper.isEmpty(map.get("BDCDYID"))) {
								String hsql = " bdcdyid ='"
										+ map.get("BDCDYID") + "'";
								List<BDCS_H_XZ> hs = baseCommonDao.getDataList(
										BDCS_H_XZ.class, hsql);
								if (hs.size() > 0) {
									hsql = " bdcdyid ='"
											+ hs.get(0).getZDBDCDYID() + "'";
									List<BDCS_TDYT_XZ> tdyts = baseCommonDao
											.getDataList(BDCS_TDYT_XZ.class,
													hsql);
									if (!StringHelper.isEmpty(tdyts)
											&& tdyts.size() > 0) {
										StringBuilder tdyt = new StringBuilder();
										if (tdyts.size() > 0) {
											for (int j = 0; j < tdyts.size(); j++) {
												tdyt.append(tdyts.get(j)
														.getTDYTMC());
												if (j < tdyts.size() - 1)
													tdyt.append(",");
											}
											if (!"null".equals(tdyt.toString())) {
												map.put("TDYT", tdyt.toString());
											}
										}
									}
								}
							}
						}
					}

					if ("".equals(map.get("TDYT"))) {
						if (map.get("BDCDYLX").equals("预测户")) {
							if (!StringHelper.isEmpty(map.get("BDCDYID"))) {
								String hsql = " bdcdyid ='"
										+ map.get("BDCDYID") + "'";
								List<BDCS_H_XZY> hys = baseCommonDao
										.getDataList(BDCS_H_XZY.class, hsql);
								if (hys.size() > 0) {
									hsql = " bdcdyid ='"
											+ hys.get(0).getZDBDCDYID() + "'";
									List<BDCS_TDYT_XZ> tdyts = baseCommonDao
											.getDataList(BDCS_TDYT_XZ.class,
													hsql);
									if (!StringHelper.isEmpty(tdyts)
											&& tdyts.size() > 0) {
										StringBuilder tdyt = new StringBuilder();
										if (tdyts.size() > 0) {
											for (int j = 0; j < tdyts.size(); j++) {
												tdyt.append(tdyts.get(j)
														.getTDYTMC());
												if (j < tdyts.size() - 1)
													tdyt.append(",");
											}
											if (!"null".equals(tdyt.toString())) {
												map.put("TDYT", tdyt.toString());
											}
										}
									}
								}
							}
						}
					}

				}

				if (map.get("BDCDYLX").equals("使用权宗地")
						|| map.get("BDCDYLX").equals("所有权宗地")) {
					// liangc 宗地用途取TDYT表
					if (!StringHelper.isEmpty(map.get("BDCDYID"))) {
						StringBuilder zdhql = new StringBuilder(" bdcdyid ='"
								+ map.get("BDCDYID") + "'");
						List<BDCS_TDYT_XZ> tdyts = baseCommonDao.getDataList(
								BDCS_TDYT_XZ.class, zdhql.toString());
						if ( tdyts.size() > 0 &&!StringHelper.isEmpty(tdyts)) {
							StringBuilder tdyt = new StringBuilder();
							if (tdyts.size() > 0) {
								for (int j = 0; j < tdyts.size(); j++) {
									tdyt.append(ConstHelper.getNameByValue(
											"TDYT", tdyts.get(j).getTDYT()));
									if (j < tdyts.size() - 1)
										tdyt.append(",");
								}
								map.put("TDYT", tdyt.toString());
							}
						}
					}
				} else {

					// 户关联到宗地获取宗地面积 liangq
					String sql = "select zdbdcdyid from bdck.bdcs_h_xzy where bdcdyh = '"
							+ map.get("BDCDYH") + "'";
					List<Map> hy = baseCommonDao.getDataListByFullSql(sql);
					if (hy.size() > 0) {
						sql = "select ZDMJ,QLXZ from bdck.bdcs_shyqzd_xz where bdcdyid='"
								+ hy.get(0).get("ZDBDCDYID") + "'";
						List<Map> zd = baseCommonDao.getDataListByFullSql(sql);
						if (zd.size() > 0) {
							map.put("SYQMJ", zd.get(0).get("ZDMJ"));
							map.put("QLXZ", zd.get(0).get("QLXZ"));
						}
					}

				}
			} else {
				map.put("FWYT",
						ConstHelper.getNameByValue("FWYT",
								(String) map.get("GHYT")));
				map.put("TDYT",
						ConstHelper.getNameByValue("TDYT",
								(String) map.get("TDYT")));
			}

			if (map.get("DJLX").equals("预告登记")) {

				// 户关联到宗地获取宗地面积 liangq
				String sql = "select zdbdcdyid from bdck.bdcs_h_xzy where bdcdyh = '"
						+ map.get("BDCDYH") + "'";
				List<Map> hy = baseCommonDao.getDataListByFullSql(sql);
				if (hy.size() > 0) {
					sql = "select ZDMJ,QLXZ from bdck.bdcs_shyqzd_xz where bdcdyid='"
							+ hy.get(0).get("ZDBDCDYID") + "'";
					List<Map> zd = baseCommonDao.getDataListByFullSql(sql);
					if (zd.size() > 0) {
						map.put("SYQMJ", zd.get(0).get("ZDMJ"));
						map.put("QLXZ", zd.get(0).get("QLXZ"));
					}
				}

			}

			/*if (map.containsKey("SLSJ")) {
				map.put("SLSJ",
						StringHelper.FormatYmdhmsByDate(map.get("SLSJ")));
			}*/
			if (map.containsKey("QLXZ")) {
				map.put("QLXZ",
						ConstHelper.getNameByValue("QLXZ",(String)map.get("QLXZ")));
			}
		}

		return result;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Message GetCFInfo(Map<String, String> conditionParameter,
			int currentpage, int pageSize) throws Exception {
		Map<String, String> newpara = new HashMap<String, String>();
		StringBuilder fromsql = new StringBuilder();
				// .append("LEFT JOIN bdck.bdcs_qdzr_xz qdzr ON
				// ql.qlid=qdzr.qlid ")
		if (conditionParameter.get("CXZT").equals("2")){
			fromsql.append(" FROM  BDCK.BDCS_QL_LS QL ")
			.append("LEFT JOIN BDCK.BDCS_FSQL_LS FSQL ON QL.QLID=FSQL.QLID ")
			.append("LEFT JOIN BDCK.BDCS_DJDY_LS DJDY ON QL.DJDYID=DJDY.DJDYID ").append("LEFT JOIN ")
			.append("(SELECT BDCDYID,ZL,BDCDYH,'032' AS BDCDYLX FROM BDCK.BDCS_H_LSY ").append("UNION  ")
			.append("SELECT BDCDYID,ZL,BDCDYH,'031' AS BDCDYLX FROM BDCK.BDCS_H_LS ").append("UNION  ")
			.append("SELECT BDCDYID,ZL,BDCDYH,'02' AS BDCDYLX FROM BDCK.BDCS_SHYQZD_LS ").append("UNION  ")
			.append("SELECT BDCDYID,ZL,BDCDYH,'04' AS BDCDYLX FROM BDCK.BDCS_ZH_LS ").append("UNION  ")
			.append("SELECT BDCDYID,ZL,BDCDYH,'05' AS BDCDYLX FROM BDCK.BDCS_NYD_LS ").append("UNION  ")
			.append("SELECT BDCDYID,ZL,BDCDYH,'09' AS BDCDYLX FROM BDCK.BDCS_SLLM_LS ").append("UNION  ")
			.append("SELECT BDCDYID,ZL,BDCDYH,'01' AS BDCDYLX FROM BDCK.BDCS_SYQZD_LS) DY ON DY.BDCDYID=DJDY.BDCDYID AND DY.BDCDYLX=DJDY.BDCDYLX ")
			.append("LEFT JOIN (SELECT a.djdyid,a.bdcqzh,a.qlid FROM bdck.bdcs_ql_xz a WHERE a.qllx IN ('3','4','7','8','11','15','24')) syql ON djdy.djdyid=syql.djdyid ")
			.append("LEFT JOIN (SELECT DISTINCT TO_CHAR(WM_CONCAT(TO_CHAR(QLRMC))) AS QLRMC,QLID FROM BDCK.BDCS_QLR_LS GROUP BY QLID) QLR ON QLR.QLID=SYQL.QLID ")
			//.append("LEFT JOIN (SELECT a.djdyid,a.xmbh,a.qlqssj,a.qljssj,b.cflx,a.djsj,b.cfjg,b.cfwh,b.cffw,a.fj,b.cfwj,b.zxsj,a.qlid FROM bdck.bdcs_ql_ls a LEFT JOIN bdck.bdcs_fsql_ls b  ")
			//.append("ON a.qlid=b.qlid  WHERE a.djlx='800' AND a.qllx='99' ) cfql ON ql.qlid = cfql.qlid ")
			.append("LEFT JOIN ( ")
			.append("SELECT con.constvalue,con.consttrans FROM bdck.bdcs_const con LEFT JOIN bdck.bdcs_constcls const ON con.constslsid=const.constslsid ")
			.append("WHERE const.constclstype = 'CFLX') cflx ON fsql.cflx=cflx.constvalue ").append("LEFT JOIN ( ")
			.append("SELECT con.constvalue,con.consttrans FROM bdck.bdcs_const con LEFT JOIN bdck.bdcs_constcls const ON con.constslsid=const.constslsid ")
			.append("WHERE const.constclstype = 'BDCDYLX')  dylx ON djdy.bdcdylx=dylx.constvalue ")
			.append("WHERE  ql.qllx='99' AND ql.djdyid IS NOT NULL ");
		}else{
			fromsql.append(" FROM  BDCK.BDCS_QL_XZ QL ")
			.append("LEFT JOIN BDCK.BDCS_FSQL_xZ FSQL ON QL.QLID=FSQL.QLID ")
			.append("LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON QL.DJDYID=DJDY.DJDYID ").append("LEFT JOIN ")
			.append("(SELECT BDCDYID,ZL,BDCDYH,'032' AS BDCDYLX FROM BDCK.BDCS_H_XZY ").append("UNION  ")
			.append("SELECT BDCDYID,ZL,BDCDYH,'031' AS BDCDYLX FROM BDCK.BDCS_H_XZ ").append("UNION  ")
			.append("SELECT BDCDYID,ZL,BDCDYH,'02' AS BDCDYLX FROM BDCK.BDCS_SHYQZD_XZ ").append("UNION  ")
			.append("SELECT BDCDYID,ZL,BDCDYH,'04' AS BDCDYLX FROM BDCK.BDCS_ZH_LS ").append("UNION  ")
			.append("SELECT BDCDYID,ZL,BDCDYH,'05' AS BDCDYLX FROM BDCK.BDCS_NYD_LS ").append("UNION  ")
			.append("SELECT BDCDYID,ZL,BDCDYH,'09' AS BDCDYLX FROM BDCK.BDCS_SLLM_LS ").append("UNION  ")
			.append("SELECT BDCDYID,ZL,BDCDYH,'01' AS BDCDYLX FROM BDCK.BDCS_SYQZD_XZ) DY ON DY.BDCDYID=DJDY.BDCDYID AND DY.BDCDYLX=DJDY.BDCDYLX ")
			.append("LEFT JOIN (SELECT a.djdyid,a.bdcqzh,a.qlid FROM bdck.bdcs_ql_xz a WHERE a.qllx IN ('3','4','7','8','11','15','24')) syql ON djdy.djdyid=syql.djdyid ")
			.append("LEFT JOIN (SELECT DISTINCT TO_CHAR(WM_CONCAT(TO_CHAR(QLRMC))) AS QLRMC,QLID FROM BDCK.BDCS_QLR_LS GROUP BY QLID) QLR ON QLR.QLID=SYQL.QLID ")
		//	.append("LEFT JOIN (SELECT a.djdyid,a.xmbh,a.qlqssj,a.qljssj,b.cflx,a.djsj,b.cfjg,b.cfwh,b.cffw,a.fj,b.cfwj,b.zxsj FROM bdck.bdcs_ql_xz a LEFT JOIN bdck.bdcs_fsql_xz b  ")
		//	.append("ON a.qlid=b.qlid  WHERE a.djlx='800' AND a.qllx='99' ) cfql ON djdy.djdyid=cfql.djdyid ")
			.append("LEFT JOIN ( ")
			.append("SELECT con.constvalue,con.consttrans FROM bdck.bdcs_const con LEFT JOIN bdck.bdcs_constcls const ON con.constslsid=const.constslsid ")
			.append("WHERE const.constclstype = 'CFLX') cflx ON fsql.cflx=cflx.constvalue ").append("LEFT JOIN ( ")
			.append("SELECT con.constvalue,con.consttrans FROM bdck.bdcs_const con LEFT JOIN bdck.bdcs_constcls const ON con.constslsid=const.constslsid ")
			.append("WHERE const.constclstype = 'BDCDYLX')  dylx ON djdy.bdcdylx=dylx.constvalue ")
			.append("WHERE  ql.qllx='99' AND ql.djdyid IS NOT NULL ");
		}

		if (!StringUtils.isEmpty(conditionParameter.get("DJSJ_Q"))) {
			fromsql.append("  AND QL.DJSJ >=to_date(:DJSJ_Q,'YYYY-MM-DD HH24:MI:SS') ");
			newpara.put("DJSJ_Q",
					new String(conditionParameter.get("DJSJ_Q").getBytes("iso8859-1"), "utf-8") + " 00:00:00");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("DJSJ_Z"))) {
			fromsql.append("  AND QL.DJSJ <=to_date(:DJSJ_Z,'YYYY-MM-DD HH24:MI:SS') ");
			newpara.put("DJSJ_Z",
					new String(conditionParameter.get("DJSJ_Z").getBytes("iso8859-1"), "utf-8") + " 23:59:59");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("BDCQZH"))) {
			fromsql.append("  AND QL.BDCQZH LIKE:BDCQZH ");
			newpara.put("BDCQZH",
					"%" + new String(conditionParameter.get("BDCQZH").getBytes("iso8859-1"), "utf-8") + "%");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("BDCDYH"))) {
			fromsql.append("  AND DJDY.BDCDYH LIKE:BDCDYH ");
			newpara.put("BDCDYH",
					"%" + new String(conditionParameter.get("BDCDYH").getBytes("iso8859-1"), "utf-8") + "%");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("TJLX"))) {
			if (conditionParameter.get("TJLX").equals("1"))// 全部
				fromsql.append("  AND DJDY.BDCDYLX IN('01','02','04','05','09','031','032')");
			else if (conditionParameter.get("TJLX").equals("2"))// 房
				fromsql.append("  AND DJDY.BDCDYLX IN('031','032')");
			else if (conditionParameter.get("TJLX").equals("3"))// 宗地
				fromsql.append("  AND DJDY.BDCDYLX IN('01','02')");
			else if (conditionParameter.get("TJLX").equals("4"))// 使用权
				fromsql.append("  AND DJDY.BDCDYLX IN('02')");
			else if (conditionParameter.get("TJLX").equals("5"))// 所有权宗地
				fromsql.append("  AND DJDY.BDCDYLX IN('01')");
			else if (conditionParameter.get("TJLX").equals("6"))// 预测户
				fromsql.append("  AND DJDY.BDCDYLX IN('032')");
			else if (conditionParameter.get("TJLX").equals("7"))// 实测户
				fromsql.append("  AND DJDY.BDCDYLX IN('031')");
			else if (conditionParameter.get("TJLX").equals("8"))// 海域
				fromsql.append("  AND DJDY.BDCDYLX IN('04')");
			else if (conditionParameter.get("TJLX").equals("9"))// 林地
				fromsql.append("  AND DJDY.BDCDYLX IN('05')");
			else if (conditionParameter.get("TJLX").equals("10"))// 农用地
				fromsql.append("  AND DJDY.BDCDYLX IN('09')");
			else
				fromsql.append("  AND DJDY.BDCDYLX IN('031','032') ");

		}
		if (!StringUtils.isEmpty(conditionParameter.get("CFZT"))&&!StringUtils.isEmpty(conditionParameter.get("CXZT"))) {
			if(conditionParameter.get("CXZT").equals("2")){
				if (conditionParameter.get("CFZT").equals("2"))
					fromsql.append("  AND NOT EXISTS(SELECT 1 FROM BDCK.BDCS_QL_XZ XZ WHERE XZ.QLID=QL.QLID) ");
			}
			else
				fromsql.append("  ");
		}
		Long total = baseCommonDao.getCountByFullSql(fromsql.toString(), newpara);
		if (total == 0)
			return new Message();
		StringBuilder selectSql = new StringBuilder();
		selectSql.append("select  ql.djdyid, to_char(ql.djsj, 'YYYY-MM-DD HH24:MI:SS') AS djsj,to_char(fsql.zxsj, 'YYYY-MM-DD HH24:MI:SS') AS jfdjsj,DY.ZL,")
				.append(" DY.BDCDYH,syql.bdcqzh, dylx.consttrans BDCDYLX, QLR.QLRMC,fsql.cfjg,fsql.cflx, fsql.cfwh,fsql.cffw,fsql.cfwj,")
				.append("ql.fj,to_char(ql.qlqssj, 'yyyy-mm-dd') AS qlqssj,to_char(ql.qljssj, 'yyyy-mm-dd') AS qljssj,'' AS slsj,")
				.append(" row_number() over(ORDER BY QL.BDCQZH ASC) AS xh ");
		List<Map> result = baseCommonDao.getPageDataByFullSql(selectSql.append(fromsql).toString(), newpara,
				currentpage, pageSize);

		Message msg = new Message();
		msg.setRows(result);
		msg.setTotal(total);
		return msg;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public List<Map> CFInfoDownload(Map<String, String> conditionParameter)
			throws Exception {
		Map<String, String> newpara = new HashMap<String, String>();
		StringBuilder fromsql = new StringBuilder();
		// .append("LEFT JOIN bdck.bdcs_qdzr_xz qdzr ON
		// ql.qlid=qdzr.qlid ")
		if (conditionParameter.get("CXZT").equals("2")){
			fromsql.append(" FROM  BDCK.BDCS_QL_LS QL ")
			.append("LEFT JOIN BDCK.BDCS_FSQL_LS FSQL ON QL.QLID=FSQL.QLID ")
			.append("LEFT JOIN BDCK.BDCS_DJDY_LS DJDY ON QL.DJDYID=DJDY.DJDYID ").append("LEFT JOIN ")
			.append("(SELECT BDCDYID,ZL,BDCDYH,'032' AS BDCDYLX FROM BDCK.BDCS_H_LSY ").append("UNION  ")
			.append("SELECT BDCDYID,ZL,BDCDYH,'031' AS BDCDYLX FROM BDCK.BDCS_H_LS ").append("UNION  ")
			.append("SELECT BDCDYID,ZL,BDCDYH,'02' AS BDCDYLX FROM BDCK.BDCS_SHYQZD_LS ").append("UNION  ")
			.append("SELECT BDCDYID,ZL,BDCDYH,'04' AS BDCDYLX FROM BDCK.BDCS_ZH_LS ").append("UNION  ")
			.append("SELECT BDCDYID,ZL,BDCDYH,'05' AS BDCDYLX FROM BDCK.BDCS_NYD_LS ").append("UNION  ")
			.append("SELECT BDCDYID,ZL,BDCDYH,'09' AS BDCDYLX FROM BDCK.BDCS_SLLM_LS ").append("UNION  ")
			.append("SELECT BDCDYID,ZL,BDCDYH,'01' AS BDCDYLX FROM BDCK.BDCS_SYQZD_LS) DY ON DY.BDCDYID=DJDY.BDCDYID AND DY.BDCDYLX=DJDY.BDCDYLX ")
			.append("LEFT JOIN (SELECT a.djdyid,a.bdcqzh,a.qlid FROM bdck.bdcs_ql_xz a WHERE a.qllx IN ('3','4','7','8','11','15','24')) syql ON djdy.djdyid=syql.djdyid ")
			.append("LEFT JOIN (SELECT DISTINCT TO_CHAR(WM_CONCAT(TO_CHAR(QLRMC))) AS QLRMC,QLID FROM BDCK.BDCS_QLR_LS GROUP BY QLID) QLR ON QLR.QLID=SYQL.QLID ")
			//.append("LEFT JOIN (SELECT a.djdyid,a.xmbh,a.qlqssj,a.qljssj,b.cflx,a.djsj,b.cfjg,b.cfwh,b.cffw,a.fj,b.cfwj,b.zxsj,a.qlid FROM bdck.bdcs_ql_ls a LEFT JOIN bdck.bdcs_fsql_ls b  ")
			//.append("ON a.qlid=b.qlid  WHERE a.djlx='800' AND a.qllx='99' ) cfql ON ql.qlid = cfql.qlid ")
			.append("LEFT JOIN ( ")
			.append("SELECT con.constvalue,con.consttrans FROM bdck.bdcs_const con LEFT JOIN bdck.bdcs_constcls const ON con.constslsid=const.constslsid ")
			.append("WHERE const.constclstype = 'CFLX') cflx ON fsql.cflx=cflx.constvalue ").append("LEFT JOIN ( ")
			.append("SELECT con.constvalue,con.consttrans FROM bdck.bdcs_const con LEFT JOIN bdck.bdcs_constcls const ON con.constslsid=const.constslsid ")
			.append("WHERE const.constclstype = 'BDCDYLX')  dylx ON djdy.bdcdylx=dylx.constvalue ")
			.append("WHERE  ql.qllx='99' AND ql.djdyid IS NOT NULL ");
		}else{
			fromsql.append(" FROM  BDCK.BDCS_QL_XZ QL ")
			.append("LEFT JOIN BDCK.BDCS_FSQL_xZ FSQL ON QL.QLID=FSQL.QLID ")
			.append("LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON QL.DJDYID=DJDY.DJDYID ").append("LEFT JOIN ")
			.append("(SELECT BDCDYID,ZL,BDCDYH,'032' AS BDCDYLX FROM BDCK.BDCS_H_XZY ").append("UNION  ")
			.append("SELECT BDCDYID,ZL,BDCDYH,'031' AS BDCDYLX FROM BDCK.BDCS_H_XZ ").append("UNION  ")
			.append("SELECT BDCDYID,ZL,BDCDYH,'02' AS BDCDYLX FROM BDCK.BDCS_SHYQZD_XZ ").append("UNION  ")
			.append("SELECT BDCDYID,ZL,BDCDYH,'04' AS BDCDYLX FROM BDCK.BDCS_ZH_LS ").append("UNION  ")
			.append("SELECT BDCDYID,ZL,BDCDYH,'05' AS BDCDYLX FROM BDCK.BDCS_NYD_LS ").append("UNION  ")
			.append("SELECT BDCDYID,ZL,BDCDYH,'09' AS BDCDYLX FROM BDCK.BDCS_SLLM_LS ").append("UNION  ")
			.append("SELECT BDCDYID,ZL,BDCDYH,'01' AS BDCDYLX FROM BDCK.BDCS_SYQZD_XZ) DY ON DY.BDCDYID=DJDY.BDCDYID AND DY.BDCDYLX=DJDY.BDCDYLX ")
			.append("LEFT JOIN (SELECT a.djdyid,a.bdcqzh,a.qlid FROM bdck.bdcs_ql_xz a WHERE a.qllx IN ('3','4','7','8','11','15','24')) syql ON djdy.djdyid=syql.djdyid ")
			.append("LEFT JOIN (SELECT DISTINCT TO_CHAR(WM_CONCAT(TO_CHAR(QLRMC))) AS QLRMC,QLID FROM BDCK.BDCS_QLR_LS GROUP BY QLID) QLR ON QLR.QLID=SYQL.QLID ")
		//	.append("LEFT JOIN (SELECT a.djdyid,a.xmbh,a.qlqssj,a.qljssj,b.cflx,a.djsj,b.cfjg,b.cfwh,b.cffw,a.fj,b.cfwj,b.zxsj FROM bdck.bdcs_ql_xz a LEFT JOIN bdck.bdcs_fsql_xz b  ")
		//	.append("ON a.qlid=b.qlid  WHERE a.djlx='800' AND a.qllx='99' ) cfql ON djdy.djdyid=cfql.djdyid ")
			.append("LEFT JOIN ( ")
			.append("SELECT con.constvalue,con.consttrans FROM bdck.bdcs_const con LEFT JOIN bdck.bdcs_constcls const ON con.constslsid=const.constslsid ")
			.append("WHERE const.constclstype = 'CFLX') cflx ON fsql.cflx=cflx.constvalue ").append("LEFT JOIN ( ")
			.append("SELECT con.constvalue,con.consttrans FROM bdck.bdcs_const con LEFT JOIN bdck.bdcs_constcls const ON con.constslsid=const.constslsid ")
			.append("WHERE const.constclstype = 'BDCDYLX')  dylx ON djdy.bdcdylx=dylx.constvalue ")
			.append("WHERE  ql.qllx='99' AND ql.djdyid IS NOT NULL ");
		}
		
		if (!StringUtils.isEmpty(conditionParameter.get("DJSJ_Q"))) {
			fromsql.append("  AND QL.DJSJ >=to_date('"
					+ conditionParameter.get("DJSJ_Q") + " 00:00:00','yyyy-mm-dd HH24:MI:SS') ");
			newpara.put("DJSJ_Q", new String(conditionParameter.get("DJSJ_Q")
					.getBytes("iso8859-1"), "utf-8") + " 00:00:00");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("DJSJ_Z"))) {
			fromsql.append("  AND QL.DJSJ <=to_date('"
					+ conditionParameter.get("DJSJ_Z") + " 23:59:59','yyyy-mm-dd HH24:MI:SS') ");
			newpara.put("DJSJ_Z", new String(conditionParameter.get("DJSJ_Z")
					.getBytes("iso8859-1"), "utf-8") + " 23:59:59");
		}
		
		if (!StringUtils.isEmpty(conditionParameter.get("BDCQZH"))) {
			fromsql.append("  AND QL.BDCQZH LIKE:BDCQZH ");
			newpara.put(
					"BDCQZH",
					"%"
							+ new String(conditionParameter.get("BDCQZH")
									.getBytes("iso8859-1"), "utf-8") + "%");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("BDCDYH"))) {
			fromsql.append("  AND DJDY.BDCDYH LIKE '%"
					+ conditionParameter.get("BDCDYH") + "%' ");
			newpara.put(
					"BDCDYH",
					"%"
							+ new String(conditionParameter.get("BDCDYH")
									.getBytes("iso8859-1"), "utf-8") + "%");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("TJLX"))) {
			if (conditionParameter.get("TJLX").equals("1"))// 全部
				fromsql.append("  AND DJDY.BDCDYLX IN('01','02','04','05','09','031','032')");
			else if (conditionParameter.get("TJLX").equals("2"))// 房
				fromsql.append("  AND DJDY.BDCDYLX IN('031','032')");
			else if (conditionParameter.get("TJLX").equals("3"))// 宗地
				fromsql.append("  AND DJDY.BDCDYLX IN('01','02')");
			else if (conditionParameter.get("TJLX").equals("4"))// 使用权
				fromsql.append("  AND DJDY.BDCDYLX IN('02')");
			else if (conditionParameter.get("TJLX").equals("5"))// 所有权宗地
				fromsql.append("  AND DJDY.BDCDYLX IN('01')");
			else if (conditionParameter.get("TJLX").equals("6"))// 预测户
				fromsql.append("  AND DJDY.BDCDYLX IN('032')");
			else if (conditionParameter.get("TJLX").equals("7"))// 实测户
				fromsql.append("  AND DJDY.BDCDYLX IN('031')");
			else if (conditionParameter.get("TJLX").equals("8"))// 海域
				fromsql.append("  AND DJDY.BDCDYLX IN('04')");
			else if (conditionParameter.get("TJLX").equals("9"))// 林地
				fromsql.append("  AND DJDY.BDCDYLX IN('05')");
			else if (conditionParameter.get("TJLX").equals("10"))// 农用地
				fromsql.append("  AND DJDY.BDCDYLX IN('09')");
			else
				fromsql.append("  AND DJDY.BDCDYLX IN('031','032') ");

		}
		
		if (!StringUtils.isEmpty(conditionParameter.get("CFZT"))&&!StringUtils.isEmpty(conditionParameter.get("CXZT"))) {
			if(conditionParameter.get("CXZT").equals("2")){
				if (conditionParameter.get("CFZT").equals("2"))
					fromsql.append("  AND NOT EXISTS(SELECT 1 FROM BDCK.BDCS_QL_XZ XZ WHERE XZ.QLID=QL.QLID) ");
			}
			else
				fromsql.append("  ");
		}

		StringBuilder selectSql = new StringBuilder();
		selectSql.append("select  ql.djdyid, to_char(ql.djsj, 'YYYY-MM-DD HH24:MI:SS') AS djsj,to_char(fsql.zxsj, 'YYYY-MM-DD HH24:MI:SS') AS jfdjsj,DY.ZL,")
				 .append(" DY.BDCDYH,syql.bdcqzh, dylx.consttrans BDCDYLX, QLR.QLRMC,fsql.cfjg,fsql.cflx, fsql.cfwh,fsql.cffw,fsql.cfwj,")
				 .append("ql.fj,to_char(ql.qlqssj, 'yyyy-mm-dd') AS qlqssj,to_char(ql.qljssj, 'yyyy-mm-dd') AS qljssj,'' AS slsj,")
				 .append(" row_number() over(ORDER BY QL.BDCQZH ASC) AS xh ");
		List<Map> result = baseCommonDao.getDataListByFullSql(selectSql.append(fromsql).toString(), newpara);

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Message GetHouseQueryList(Map<String, String> conditionParameter,
			int currentpage, int pageSize) throws Exception {
		Map<String, String> newpara = new HashMap<String, String>();
		StringBuilder fromsql = new StringBuilder(
				" FROM  BDCK.BDCS_QLR_XZ QLR INNER JOIN BDCK.BDCS_QL_LS QL ON QLR.QLID=QL.QLID WHERE QL.QLLX='"
						+ ConstValue.QLLX.GYJSYDSHYQ_FWSYQ.Value + "'");
		if (!StringUtils.isEmpty(conditionParameter.get("QLRMC"))) {
			fromsql.append("  AND QLR.QLRMC LIKE:QLRMC ");
			newpara.put(
					"QLRMC",
					"%"
							+ new String(conditionParameter.get("QLRMC")
									.getBytes("iso8859-1"), "utf-8") + "%");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("ZJH"))) {
			fromsql.append("  AND QL.DJLX =:DJLX ");
			newpara.put("DJLX", new String(conditionParameter.get("DJLX")
					.getBytes("iso8859-1"), "utf-8"));
		}
		Long total = baseCommonDao.getCountByFullSql(fromsql.toString(),
				newpara);
		if (total == 0)
			return new Message();
		@SuppressWarnings("rawtypes")
		List<Map> result = baseCommonDao.getPageDataByFullSql(
				" SELECT QLR.QLRID " + fromsql.toString()
						+ " ORDER BY QL.DJSJ DESC ", newpara, currentpage,
				pageSize);
		List<BDCS_QLR_XZ> newResult = new ArrayList<BDCS_QLR_XZ>();
		for (Map<String, String> r : result) {
			BDCS_QLR_XZ qlr = baseCommonDao.get(BDCS_QLR_XZ.class,
					r.get("QLRID"));
			if (qlr != null) {
				QLR_XZ_HOUSE_EX qlr_xz_house_ex = new QLR_XZ_HOUSE_EX();
				PropertyUtils.copyProperties(qlr_xz_house_ex, qlr);
				qlr_xz_house_ex.Init(baseCommonDao);
				newResult.add(qlr_xz_house_ex);
			}
		}
		Message msg = new Message();
		msg.setRows(newResult);
		msg.setTotal(total);
		return msg;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<DJDY_LS_EX> WorkBookDownload(
			Map<String, String> conditionParameter, int currentpage,
			int pageSize) throws Exception {
		Map<String, String> newpara = new HashMap<String, String>();
		StringBuilder fromsql = new StringBuilder(
				" FROM  BDCK.BDCS_DJDY_LS DJDY INNER JOIN BDCK.BDCS_QL_LS QL ON DJDY.DJDYID=QL.DJDYID WHERE 1=1 ");
		if (!StringUtils.isEmpty(conditionParameter.get("DJSJ_Q"))) {
			fromsql.append("  AND QL.DJSJ >=to_date(:DJSJ_Q,'yyyy-mm-dd') ");
			newpara.put("DJSJ_Q", new String(conditionParameter.get("DJSJ_Q")
					.getBytes("iso8859-1"), "utf-8"));
		}
		if (!StringUtils.isEmpty(conditionParameter.get("DJSJ_Z"))) {
			fromsql.append("  AND QL.DJSJ <=to_date(:DJSJ_Z,'yyyy-mm-dd')+1 ");
			newpara.put("DJSJ_Z", new String(conditionParameter.get("DJSJ_Z")
					.getBytes("iso8859-1"), "utf-8"));
		}
		if (!StringUtils.isEmpty(conditionParameter.get("DJLX"))) {
			fromsql.append("  AND QL.DJLX =:DJLX ");
			newpara.put("DJLX", new String(conditionParameter.get("DJLX")
					.getBytes("iso8859-1"), "utf-8"));
		}
		if (!StringUtils.isEmpty(conditionParameter.get("BDCDYH"))) {
			fromsql.append("  AND DJDY.BDCDYH LIKE:BDCDYH ");
			newpara.put(
					"BDCDYH",
					"%"
							+ new String(conditionParameter.get("BDCDYH")
									.getBytes("iso8859-1"), "utf-8") + "%");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("LX"))) {
			if (conditionParameter.get("LX").equals("house"))
				fromsql.append("  AND DJDY.BDCDYLX IN("
						+ ConstValue.BDCDYLX.H.Value + ") ");
			if (conditionParameter.get("LX").equals("land"))
				fromsql.append("  AND DJDY.BDCDYLX IN("
						+ ConstValue.BDCDYLX.SYQZD.Value + ","
						+ ConstValue.BDCDYLX.SHYQZD.Value + ") ");
		}
		List<Map> result = baseCommonDao.getPageDataByFullSql(
				" SELECT DISTINCT ID FROM (SELECT  DJDY.ID "
						+ fromsql.toString() + " ORDER BY QL.DJSJ DESC) ",
				newpara, currentpage, pageSize);
		List<DJDY_LS_EX> newResult = new ArrayList<DJDY_LS_EX>();
		for (Map<String, String> r : result) {
			BDCS_DJDY_LS djdy = baseCommonDao.get(BDCS_DJDY_LS.class,
					r.get("ID"));
			DJDY_LS_EX djdy_ls_ex = new DJDY_LS_EX();
			PropertyUtils.copyProperties(djdy_ls_ex, djdy);// 拷贝
			djdy_ls_ex.Init(baseCommonDao);// 初始化
			newResult.add(djdy_ls_ex);
		}
		return newResult;
	}

	// 各种建筑面积
	@SuppressWarnings("rawtypes")
	public HashMap<String, String> GetStandingBook_HZ(
			Map<String, String> mapCondition) { // HashMap类型
		HashMap<String, String> result = new HashMap<String, String>();
		StringBuilder hdjzmj = new StringBuilder(
				"SELECT SUM(DY.SCJZMJ) AS HJZMJ, SUM(DY.YCJZMJ) AS YHJZMJ,SUM(DY.SHYQMJ) AS SHYQMJ,SUM(DY.SYQMJ) AS SYQMJ FROM BDCK.BDCS_QL_GZ QL ")
				.append("LEFT JOIN BDCK.BDCS_ZS_GZ ZS ON QL.XMBH=ZS.XMBH  ")
				.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON ZS.XMBH=XMXX.XMBH  ")
				.append("LEFT JOIN BDC_WORKFLOW.WFI_PROINST PROINST ON XMXX.PROJECT_ID=PROINST.FILE_NUMBER ")
				.append("LEFT JOIN BDCK.BDCS_FSQL_GZ FSQL ON QL.QLID=FSQL.QLID ")
				.append("LEFT JOIN BDCK.BDCS_DJDY_LS DJDY ON QL.DJDYID=DJDY.DJDYID ")
				.append("LEFT JOIN ")
				.append("(SELECT 0 AS SCJZMJ,YCJZMJ AS YCJZMJ,0 AS SHYQMJ,0 AS SYQMJ,BDCDYID,'032' AS BDCDYLX,ZL,DJQDM,FWYT1 AS FWYT,FWTDYT AS TDYT,QLXZ,NULL AS QLLX FROM BDCK.BDCS_H_XZY ")
				.append("UNION ")
				.append("SELECT SCJZMJ AS SCJZMJ,0 AS YCJZMJ,0 AS SHYQMJ,0 AS SYQMJ,BDCDYID,'031' AS BDCDYLX,ZL,DJQDM,FWYT1 AS FWYT,FWTDYT AS TDYT,QLXZ,NULL AS QLLX FROM BDCK.BDCS_H_XZ ")
				.append("UNION  ")
				.append("SELECT 0 AS SCJZMJ,0 AS YCJZMJ,ZDMJ AS SHYQMJ,0 AS SYQMJ,BDCDYID,'02' AS BDCDYLX,ZL,DJQDM,NULL AS FWYT,YT AS TDYT,QLXZ,QLLX FROM BDCK.BDCS_SHYQZD_XZ ")
				.append("UNION  ")
				.append("SELECT 0 AS SCJZMJ,0 AS YCJZMJ,0 AS SHYQMJ,0 AS SYQMJ,BDCDYID,'04' AS BDCDYLX,ZL,NULL AS DJQDM,NULL AS FWYT,NULL AS TDYT,NULL AS QLXZ,NULL AS QLLX FROM BDCK.BDCS_ZH_XZ ")
				.append("UNION ")
				.append("SELECT 0 AS SCJZMJ,0 AS YCJZMJ,0 AS SHYQMJ,0 AS SYQMJ,BDCDYID,'09' AS BDCDYLX,ZL,DJQDM,NULL AS FWYT,YT AS TDYT,QLXZ,QLLX FROM BDCK.BDCS_NYD_XZ ")
				.append("UNION  ")
				.append("SELECT 0 AS SCJZMJ,0 AS YCJZMJ,0 AS SHYQMJ,0 AS SYQMJ,BDCDYID,'05' AS BDCDYLX,ZL,DJQDM,NULL AS FWYT,TDYT,QLXZ,NULL AS QLLX FROM BDCK.BDCS_SLLM_XZ ")
				.append("UNION  ")
				.append("SELECT 0 AS SCJZMJ,0 AS YCJZMJ,0 AS SHYQMJ,ZDMJ AS SYQMJ,BDCDYID,'01' AS BDCDYLX,ZL,DJQDM,NULL AS FWYT,YT AS TDYT,QLLX,QLXZ FROM BDCK.BDCS_SYQZD_XZ) DY ")
				.append("ON DY.BDCDYID=DJDY.BDCDYID AND DY.BDCDYLX=DJDY.BDCDYLX ")
				.append("LEFT JOIN ")
				.append("(SELECT distinct GYFS,LISTAGG(TO_CHAR(DH), ',') WITHIN group(order by DH) AS LXDH,LISTAGG(TO_CHAR(QLRMC), ',') within group(order by bdcqzhxh) AS QLRMC,")
				.append("LISTAGG(TO_CHAR(BDCQZH), ',') within group(order by BDCQZH) AS BDCQZH, LISTAGG(TO_CHAR(ZJH), ',') within group(order by ZJH) AS ZJH,LISTAGG(TO_CHAR(QLRLX), ',') within group(order by QLRLX) AS QLRLX,")
				.append("LISTAGG(TO_CHAR(DLRXM), ',') within group(order by DLRXM) AS QLDLR, LISTAGG(TO_CHAR(DLRZJHM), ',') within group(order by DLRZJHM) AS QLDLRZJH,LISTAGG(TO_CHAR(DLRLXDH), ',') within group(order by DLRLXDH) AS QLDLRLXDH, QLID FROM BDCK.BDCS_QLR_GZ GROUP BY GYFS, QLID) QLR")
				.append(" ON QLR.QLID=QL.QLID ")
				.append("LEFT JOIN BDCK.BDCS_DJFZ DJFZ ON DJFZ.HFZSH=QL.BDCQZH ")
				
				
//				.append(" LEFT JOIN  BDCK.BDCS_DJFZ DJFZ ON XMXX.XMBH =  DJFZ.XMBH ")
				.append("WHERE 1=1 ");

		Map<String, String> paramMap = new HashMap<String, String>();
		
		String TDYT_VALUE = "";//土地用途
		String  DJQ = "";//地籍区
		String DJLX = "";//登记类型
		String SFFZ = "";//是否发证
		String FWYT_VALUE = "";//房屋用途
		String QLLX = "";//权利类型
		String QLXZ = "";//权利性质
		String ZL = "";//坐落
		for(String key :mapCondition.keySet()){
			 TDYT_VALUE = mapCondition.get("TDYT");
			 DJQ = mapCondition.get("DJQDM");
			 DJLX = mapCondition.get("DJLX");
			 SFFZ = mapCondition.get("SFFZ");
			 QLLX = mapCondition.get("QLLX");
			 QLXZ = mapCondition.get("QLXZ");
			 ZL = mapCondition.get("ZL");
			 FWYT_VALUE =mapCondition.get("FWYT");
		}
		//登记类型
		if(Integer.parseInt(DJLX) != 0){
			hdjzmj.append("  AND XMXX.DJLX = '"+DJLX+ "'");
		}
		//房屋用途
		if(Integer.parseInt(FWYT_VALUE) != 0){
			hdjzmj.append("  AND DY.FWYT = '"+FWYT_VALUE+ "'");
		}
		//坐落
		if(!StringHelper.isEmpty(ZL)){
			hdjzmj.append("  AND DY.ZL = '"+ ZL +"'");
		}
		//权利性质
		if(Integer.parseInt(QLXZ)!=0){
			hdjzmj.append("  AND DY.QLXZ = '"+ QLXZ +"'");
		}
		//权利类型
		if(Integer.parseInt(QLLX)!=0){
			hdjzmj.append("  AND DY.QLLX = '"+ QLLX +"'");
		}
		//是否发证
		if(Integer.parseInt(SFFZ)!=2){
			if(Integer.parseInt(SFFZ) ==1)//已发证,根据发证时间判断
				hdjzmj.append("  AND DJFZ.FZSJ IS NOT NULL");
			if(Integer.parseInt(SFFZ) ==0)
				hdjzmj.append("  AND DJFZ.FZSJ IS  NULL");
		}
		//地籍区
		if(Integer.parseInt(DJQ) !=0){
			hdjzmj.append("  AND DY.DJQDM = '"+ DJQ +"'");
		}
		//土地用途
		if(Integer.parseInt(TDYT_VALUE) != 0){		
			hdjzmj.append(" AND DY.TDYT= '"+ TDYT_VALUE + "'");
		}
		
		//办理环节
		if ("2".equals(mapCondition.get("BLHJ"))) {
			if (!StringUtils.isEmpty(mapCondition.get("DJSJ_Q"))) {
				hdjzmj.append("  AND QL.DJSJ >=to_date(:DJSJ_Q,'YYYY-MM-DD HH24:MI:SS') ");
				String djsj_q = "";
				try {
					djsj_q = new String(mapCondition.get("DJSJ_Q").getBytes(
							"iso8859-1"), "utf-8");
					djsj_q = djsj_q + " 00:00:00";
					paramMap.put("DJSJ_Q", djsj_q);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			if (!StringUtils.isEmpty(mapCondition.get("DJSJ_Z"))) {
				hdjzmj.append("  AND QL.DJSJ <=to_date(:DJSJ_Z,'YYYY-MM-DD HH24:MI:SS') ");
				String djsj_z = "";
				try {
					djsj_z = new String(mapCondition.get("DJSJ_Z").getBytes(
							"iso8859-1"), "utf-8");
					djsj_z = djsj_z + " 23:59:59";
					paramMap.put("DJSJ_Z", djsj_z);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		} else {
			if (!StringUtils.isEmpty(mapCondition.get("DJSJ_Q"))) {
				hdjzmj.append("  AND XMXX.SLSJ >=to_date(:DJSJ_Q,'YYYY-MM-DD HH24:MI:SS') ");
				String djsj_q = "";
				try {
					djsj_q = new String(mapCondition.get("DJSJ_Q").getBytes(
							"iso8859-1"), "utf-8");
					djsj_q = djsj_q + " 00:00:00";
					paramMap.put("DJSJ_Q", djsj_q);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			if (!StringUtils.isEmpty(mapCondition.get("DJSJ_Z"))) {
				hdjzmj.append("  AND XMXX.SLSJ <=to_date(:DJSJ_Z,'YYYY-MM-DD HH24:MI:SS') ");
				String djsj_z = "";
				try {
					djsj_z = new String(mapCondition.get("DJSJ_Z").getBytes(
							"iso8859-1"), "utf-8");
					djsj_z = djsj_z + " 23:59:59";
					paramMap.put("DJSJ_Z", djsj_z);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		
		//（统计）类型
		if (!StringUtils.isEmpty(mapCondition.get("TJLX"))) {
			if (mapCondition.get("TJLX").equals("1"))// 全部
				hdjzmj.append("  AND DY.BDCDYLX IN('01','02','04','05','09','031','032')");
			else if (mapCondition.get("TJLX").equals("2"))// 房
				hdjzmj.append("  AND DY.BDCDYLX IN('031','032')");
			else if (mapCondition.get("TJLX").equals("3"))// 宗地
				hdjzmj.append("  AND DY.BDCDYLX IN('01','02')");
			else if (mapCondition.get("TJLX").equals("4"))// 使用权
				hdjzmj.append("  AND DY.BDCDYLX IN('02')");
			else if (mapCondition.get("TJLX").equals("5"))// 所有权宗地
				hdjzmj.append("  AND DY.BDCDYLX IN('01')");
			else if (mapCondition.get("TJLX").equals("6"))// 预测户
				hdjzmj.append("  AND DY.BDCDYLX IN('032')");
			else if (mapCondition.get("TJLX").equals("7"))// 实测户
				hdjzmj.append("  AND DY.BDCDYLX IN('031')");
			else if (mapCondition.get("TJLX").equals("8"))// 海域
				hdjzmj.append("  AND DY.BDCDYLX IN('04')");
			else if (mapCondition.get("TJLX").equals("9"))// 林地
				hdjzmj.append("  AND DY.BDCDYLX IN('05')");
			else if (mapCondition.get("TJLX").equals("10"))// 农用地
				hdjzmj.append("  AND DY.BDCDYLX IN('09')");
			else
				hdjzmj.append("  AND DY.BDCDYLX IN('031','032') ");

		}

		List<Map> hz = baseCommonDao.getDataListByFullSql(hdjzmj.toString(),
				paramMap); // 调用sql查询
		Map map = hz.get(0); // 取查询结果第一行
		result.put("HJZMJ", StringHelper.formatDouble(map.get("HJZMJ"))); // put()方法，a是要保存到map集合中的键名，b保存到map集合中对应键名的键值对象
		result.put("YHJZMJ", StringHelper.formatDouble(map.get("YHJZMJ"))); // double转字符串
																			// 并获取
		result.put("SHYQMJ", StringHelper.formatDouble(map.get("SHYQMJ")));
		result.put("SYQMJ", StringHelper.formatDouble(map.get("SYQMJ")));
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Message GetStandingBook(Map<String, String> mapCondition,
			Integer page, Integer rows) {
		Map<String, String> newpara = new HashMap<String, String>();
		StringBuilder fromsql = new StringBuilder("FROM BDCK.BDCS_QL_GZ QL ")
				//确定一对一关系,防止重复
//				.append("LEFT JOIN BDCK.BDCS_QDZR_GZ QDZR ON QDZR.QLID=QL.QLID ")
//				.append("LEFT JOIN BDCK.BDCS_ZS_GZ ZS ON ZS.ZSID=QDZR.ZSID ")
//				.append("LEFT JOIN BDCK.BDCS_ZS_GZ ZS ON QL.XMBH=ZS.XMBH ")
				.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON QL.XMBH=XMXX.XMBH ")
				.append("LEFT JOIN BDC_WORKFLOW.WFI_PROINST PROINST ON XMXX.PROJECT_ID=PROINST.FILE_NUMBER ")
				.append("LEFT JOIN BDCK.BDCS_FSQL_GZ FSQL ON QL.QLID=FSQL.QLID ")
				.append("LEFT JOIN BDCK.BDCS_DJDY_LS DJDY ON QL.DJDYID=DJDY.DJDYID ")
				.append("LEFT JOIN ")
				.append("(SELECT BDCDYID,ZL,BDCDYH,'032' AS BDCDYLX, DJQDM, FWTDYT AS TDYT,QLXZ,GHYT AS FWYT,FWXZ,(NVL(FTTDMJ,0)+NVL(DYTDMJ,0)) AS SYQMJ,YCJZMJ AS JZMJ,YCFTJZMJ AS FTJZMJ,YCTNJZMJ AS ZYJZMJ,ZDMJ,FWJG,SZC AS SZC,ZCS AS ZCS FROM BDCK.BDCS_H_XZY ")
				.append("UNION ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'031' AS BDCDYLX, DJQDM, FWTDYT AS TDYT,QLXZ,GHYT AS FWYT,FWXZ,(NVL(FTTDMJ,0)+NVL(DYTDMJ,0)) AS SYQMJ,SCJZMJ AS JZMJ,SCFTJZMJ AS FTJZMJ,SCTNJZMJ AS ZYJZMJ,ZDMJ,FWJG,SZC AS SZC,ZCS AS ZCS FROM BDCK.BDCS_H_XZ ")
				.append("UNION  ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'02' AS BDCDYLX, DJQDM, YT AS TDYT,QLXZ,NULL AS FWYT, NULL AS FWXZ,NULL AS SYQMJ,NULL AS JZMJ,NULL AS FTJZMJ,NULL AS ZYJZMJ,ZDMJ,NULL AS FWJG,NULL AS SZC,NULL AS ZCS FROM BDCK.BDCS_SHYQZD_XZ ")
				.append("UNION  ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'01' AS BDCDYLX, DJQDM, YT AS TDYT,QLXZ,NULL AS FWYT, NULL AS FWXZ,NULL AS SYQMJ,NULL AS JZMJ,NULL AS FTJZMJ,NULL AS ZYJZMJ,ZDMJ,NULL AS FWJG,NULL AS SZC,NULL AS ZCS FROM BDCK.BDCS_SYQZD_XZ ")
				.append("UNION  ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'04' AS BDCDYLX, NULL AS DJQDM, NULL AS TDYT, NULL AS QLXZ, NULL AS FWYT, NULL AS FWXZ,YHZMJ AS SYQMJ,NULL AS JZMJ,NULL AS FTJZMJ,NULL AS ZYJZMJ,ZHMJ AS ZDMJ,NULL AS FWJG,NULL AS SZC,NULL AS ZCS FROM BDCK.BDCS_ZH_XZ ")
				.append("UNION  ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'05' AS BDCDYLX, DJQDM,TDYT,QLXZ,NULL AS FWYT, NULL AS FWXZ,SYQMJ,NULL AS JZMJ,NULL AS FTJZMJ,NULL AS ZYJZMJ,NULL AS ZDMJ,NULL AS FWJG,NULL AS SZC,NULL AS ZCS FROM BDCK.BDCS_SLLM_XZ ")
				.append("UNION  ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'09' AS BDCDYLX, DJQDM, YT AS TDYT,QLXZ,NULL AS FWYT, NULL AS FWXZ,CBMJ AS SYQMJ,NULL AS JZMJ,NULL AS FTJZMJ,NULL AS ZYJZMJ,NULL AS ZDMJ,NULL AS FWJG,NULL AS SZC,NULL AS ZCS FROM BDCK.BDCS_NYD_XZ) DY ")
				.append("ON DY.BDCDYID=DJDY.BDCDYID AND DY.BDCDYLX=DJDY.BDCDYLX ")
				.append("LEFT JOIN ")
				.append("(SELECT distinct GYFS,LISTAGG(TO_CHAR(DH), ',') WITHIN group(order by DH) AS LXDH,LISTAGG(TO_CHAR(QLRMC), ',') within group(order by bdcqzhxh) AS QLRMC,")
				.append("LISTAGG(TO_CHAR(BDCQZH), ',') within group(order by BDCQZH) AS BDCQZH, LISTAGG(TO_CHAR(ZJH), ',') within group(order by ZJH) AS ZJH,LISTAGG(TO_CHAR(QLRLX), ',') within group(order by QLRLX) AS QLRLX,")
				.append("LISTAGG(TO_CHAR(DLRXM), ',') within group(order by DLRXM) AS QLDLR, LISTAGG(TO_CHAR(DLRZJHM), ',') within group(order by DLRZJHM) AS QLDLRZJH,LISTAGG(TO_CHAR(DLRLXDH), ',') within group(order by DLRLXDH) AS QLDLRLXDH, QLID FROM BDCK.BDCS_QLR_GZ GROUP BY GYFS, QLID) QLR")
				.append(" ON QLR.QLID=QL.QLID ")
				.append("LEFT JOIN BDCK.BDCS_DJFZ DJFZ ON DJFZ.HFZSH=QL.BDCQZH ")
				//分别持证时,证书编号合并
				.append("LEFT JOIN ")
				.append("(select TO_CHAR(WM_CONCAT(DISTINCT TO_CHAR(ZSBH))) AS ZSBH,QLID from (select QDZR.QLID,ZS.ZSBH from BDCK.BDCS_QDZR_GZ qdzr left join BDCK.BDCS_ZS_GZ zs on ZS.ZSID=QDZR.ZSID) group by QLID) ZS ")
				.append("ON ZS.QLID=QL.QLID ")
				.append("WHERE 1=1 ");
		if ("2".equals(mapCondition.get("BLHJ"))) {
			if (!StringUtils.isEmpty(mapCondition.get("DJSJ_Q"))) {
				fromsql.append("  AND QL.DJSJ >=to_date(:DJSJ_Q,'YYYY-MM-DD HH24:MI:SS') ");
				String djsj_q = "";
				try {
					djsj_q = new String(mapCondition.get("DJSJ_Q").getBytes(
							"iso8859-1"), "utf-8");
					djsj_q = djsj_q + " 00:00:00";
					newpara.put("DJSJ_Q", djsj_q);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			if (!StringUtils.isEmpty(mapCondition.get("DJSJ_Z"))) {
				fromsql.append("  AND QL.DJSJ <=to_date(:DJSJ_Z,'YYYY-MM-DD HH24:MI:SS') ");
				String djsj_z = "";
				try {
					djsj_z = new String(mapCondition.get("DJSJ_Z").getBytes(
							"iso8859-1"), "utf-8");
					djsj_z = djsj_z + " 23:59:59";
					newpara.put("DJSJ_Z", djsj_z);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}else if ("3".equals(mapCondition.get("BLHJ"))) {
			if (!StringUtils.isEmpty(mapCondition.get("DJSJ_Q"))) {
				fromsql.append("  AND DJFZ.FZSJ >=to_date(:DJSJ_Q,'YYYY-MM-DD HH24:MI:SS') ");
				String djsj_q = "";
				try {
					djsj_q = new String(mapCondition.get("DJSJ_Q").getBytes(
							"iso8859-1"), "utf-8");
					djsj_q = djsj_q + " 00:00:00";
					newpara.put("DJSJ_Q", djsj_q);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			if (!StringUtils.isEmpty(mapCondition.get("DJSJ_Z"))) {
				fromsql.append("  AND DJFZ.FZSJ <=to_date(:DJSJ_Z,'YYYY-MM-DD HH24:MI:SS') ");
				String djsj_z = "";
				try {
					djsj_z = new String(mapCondition.get("DJSJ_Z").getBytes(
							"iso8859-1"), "utf-8");
					djsj_z = djsj_z + " 23:59:59";
					newpara.put("DJSJ_Z", djsj_z);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}  else {
			if (!StringUtils.isEmpty(mapCondition.get("DJSJ_Q"))) {
				fromsql.append("  AND XMXX.SLSJ >=to_date(:DJSJ_Q,'YYYY-MM-DD HH24:MI:SS') ");
				String djsj_q = "";
				try {
					djsj_q = new String(mapCondition.get("DJSJ_Q").getBytes(
							"iso8859-1"), "utf-8");
					djsj_q = djsj_q + " 00:00:00";
					newpara.put("DJSJ_Q", djsj_q);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			if (!StringUtils.isEmpty(mapCondition.get("DJSJ_Z"))) {
				fromsql.append("  AND XMXX.SLSJ <=to_date(:DJSJ_Z,'YYYY-MM-DD HH24:MI:SS') ");
				String djsj_z = "";
				try {
					djsj_z = new String(mapCondition.get("DJSJ_Z").getBytes(
							"iso8859-1"), "utf-8");
					djsj_z = djsj_z + " 23:59:59";
					newpara.put("DJSJ_Z", djsj_z);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}

		if (!StringUtils.isEmpty(mapCondition.get("TJLX"))) {
			if (mapCondition.get("TJLX").equals("1"))// 全部
				fromsql.append("  AND DJDY.BDCDYLX IN('01','02','04','05','09','031','032')");
			else if (mapCondition.get("TJLX").equals("2"))// 房
				fromsql.append("  AND DJDY.BDCDYLX IN('031','032')");
			else if (mapCondition.get("TJLX").equals("3"))// 宗地
				fromsql.append("  AND DJDY.BDCDYLX IN('01','02')");
			else if (mapCondition.get("TJLX").equals("4"))// 使用权
				fromsql.append("  AND DJDY.BDCDYLX IN('02')");
			else if (mapCondition.get("TJLX").equals("5"))// 所有权宗地
				fromsql.append("  AND DJDY.BDCDYLX IN('01')");
			else if (mapCondition.get("TJLX").equals("6"))// 预测户
				fromsql.append("  AND DJDY.BDCDYLX IN('032')");
			else if (mapCondition.get("TJLX").equals("7"))// 实测户
				fromsql.append("  AND DJDY.BDCDYLX IN('031')");
			else if (mapCondition.get("TJLX").equals("8"))// 海域
				fromsql.append("  AND DJDY.BDCDYLX IN('04')");
			else if (mapCondition.get("TJLX").equals("9"))// 林地
				fromsql.append("  AND DJDY.BDCDYLX IN('05')");
			else if (mapCondition.get("TJLX").equals("10"))// 农用地
				fromsql.append("  AND DJDY.BDCDYLX IN('09')");
			else
				fromsql.append("  AND DJDY.BDCDYLX IN('031','032') ");

		}

		// liangc 增加登记类型和坐落查询条件
		if (!StringUtils.isEmpty(mapCondition.get("DJLX"))) {
			if (mapCondition.get("DJLX").equals("000"))// 全部登记类型
				fromsql.append(" ");
			else if (mapCondition.get("DJLX").equals("100")) {// 首次登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "100");
			} else if (mapCondition.get("DJLX").equals("200")) {// 转移登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "200");
			} else if (mapCondition.get("DJLX").equals("300")) {// 变更登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "300");
			} else if (mapCondition.get("DJLX").equals("400")) {// 注销登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "400");
			} else if (mapCondition.get("DJLX").equals("500")) {// 更正登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "500");
			} else if (mapCondition.get("DJLX").equals("600")) {// 异议登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "600");
			} else if (mapCondition.get("DJLX").equals("700")) {// 预告 登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "700");
			} else if (mapCondition.get("DJLX").equals("800")) {// 查封登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "800");
			} else if (mapCondition.get("DJLX").equals("900")) {// 其他登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "900");
			} else {
				fromsql.append(" ");
			}
		}

		if (!StringUtils.isEmpty(mapCondition.get("ZL"))) {
			fromsql.append("  AND DY.ZL LIKE:ZL ");
			try {
				newpara.put("ZL","%"+ new String(mapCondition.get("ZL").getBytes(
										"iso8859-1"), "utf-8") + "%");
//				newpara.put("ZL","%"+ mapCondition.get("ZL").toString() + "%");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// luml 增加权利类型、土地用途、房屋用途等查询条件
		if (!StringUtils.isEmpty(mapCondition.get("QLLX"))) {
			if (mapCondition.get("QLLX").equals("0"))// 全部权利类型
				fromsql.append(" ");
			else {
				fromsql.append("  AND XMXX.QLLX =:QLLX ");
				newpara.put("QLLX", mapCondition.get("QLLX"));
			}
		}

		if (mapCondition.get("TDYT").equals("0")) {// 全部土地用途
			fromsql.append(" ");
		} else {
			fromsql.append("  AND (DY.TDYT LIKE:TDYT OR BDCDYID IN(SELECT TDYTS.BDCDYID FROM BDCK.BDCS_TDYT_XZ TDYTS WHERE TDYTS.TDYT =:TDYT)) ");
			newpara.put("TDYT", mapCondition.get("TDYT"));
		}

		if (mapCondition.get("FWYT").equals("0")){// 全部房屋用途
			fromsql.append(" ");
		}else{
			fromsql.append("  AND DY.FWYT LIKE:FWYT ");
			newpara.put("FWYT", mapCondition.get("FWYT"));
		}
		
		//chenbo 增加权利性质、地籍区查询条件
		if (mapCondition.get("QLXZ").equals("0")) {// 全部权利性质
			fromsql.append(" ");
		} else {
			fromsql.append("  AND DY.QLXZ LIKE:QLXZ ");
			newpara.put("QLXZ", mapCondition.get("QLXZ"));
		}
		
		if (mapCondition.get("DJQDM").equals("0")) {// 全部地籍区
			fromsql.append(" ");
		} else {
			fromsql.append("  AND DY.DJQDM LIKE:DJQDM ");
			newpara.put("DJQDM", mapCondition.get("DJQDM"));
		}
		
		if (!StringUtils.isEmpty(mapCondition.get("SFFZ"))) {
			if (mapCondition.get("SFFZ").equals("2"))
				fromsql.append(" ");
			else if(mapCondition.get("SFFZ").equals("1")){
				fromsql.append(" AND EXISTS(SELECT 1 FROM BDCK.BDCS_DJFZ DJFZ WHERE DJFZ.XMBH=XMXX.XMBH) ");
			}
			else if(mapCondition.get("SFFZ").equals("0")){
				fromsql.append(" AND NOT EXISTS(SELECT 1 FROM BDCK.BDCS_DJFZ DJFZ WHERE DJFZ.XMBH=XMXX.XMBH) ");
			}else{
				fromsql.append(" ");
			}
		}
		
		if (mapCondition.get("ZSZM").equals("0")) {// 全部证书证明
			fromsql.append(" ");
		} else if(mapCondition.get("ZSZM").equals("1")){
			fromsql.append("  AND QLR.BDCQZH LIKE '%证明%' ");
			newpara.put("ZSZM", mapCondition.get("ZSZM"));
		}else {
			fromsql.append("  AND QLR.BDCQZH LIKE '%产权%' ");
			newpara.put("ZSZM", mapCondition.get("ZSZM"));
		}
		
		if (!StringUtils.isEmpty(mapCondition.get("SFSZ"))) {
			if (mapCondition.get("SFSZ").equals("2"))
				fromsql.append(" ");
			else if(mapCondition.get("SFSZ").equals("1")){
				fromsql.append(" AND EXISTS(SELECT 1 FROM BDCK.BDCS_DJSZ SFSZ WHERE SFSZ.XMBH=XMXX.XMBH) ");
			}
			else if(mapCondition.get("SFSZ").equals("0")){
				fromsql.append(" AND NOT EXISTS(SELECT 1 FROM BDCK.BDCS_DJSZ SFSZ WHERE SFSZ.XMBH=XMXX.XMBH) ");
			}else{
				fromsql.append(" ");
			}
		}

		StringBuilder selectSql = new StringBuilder();
		selectSql
				.append(" SELECT DISTINCT QLR.QLID,QL.QDJG,QL.FJ,QLR.LXDH,XMXX.FCYWH,PROINST.PRODEF_NAME YWLX,XMXX.XMBH, XMXX.SLRY, XMXX.YWLSH,ZS.ZSBH,"
						+ "XMXX.DJLX,QL.DJSJ AS DJSJ,to_char(FSQL.ZXSJ,'YYYY-MM-DD HH24:MI:SS') AS ZXSJ,DY.ZL,DY.BDCDYH,DJDY.BDCDYLX,QLR.QLRMC,QLR.ZJH,QLR.QLDLR,QLR.QLDLRZJH,QLR.QLDLRLXDH,QLR.QLRLX,QLR.BDCQZH,QL.QLLX,QLR.GYFS,")
				.append("DY.TDYT,DY.QLXZ,DY.FWYT,DY.FWXZ,DY.SYQMJ,DY.JZMJ,DY.FTJZMJ,DY.ZYJZMJ,QL.DJYY,DY.ZDMJ,QL.LYQLID,FSQL.DYR,XMXX.SLSJ,DY.BDCDYID,DY.FWJG,DJFZ.FZSJ,QL.CZFS,"
						+ "FSQL.BDBZZQSE ,FSQL.ZGZQSE,DY.SZC,DY.ZCS   ");
		
		Long total = baseCommonDao.getCountByFullSql("from ("+selectSql+fromsql.toString()+")",newpara);
		if (total == 0)
			return new Message();
		if (!StringUtils.isEmpty(mapCondition.get("sort"))) {
			if (mapCondition.get("sort").equals("ZL")) {
				fromsql.append(" ORDER BY DY.ZL " + mapCondition.get("order"));
			} else if (mapCondition.get("sort").equals("YWLSH")) {
				fromsql.append(" ORDER BY XMXX.YWLSH "
						+ mapCondition.get("order"));
			} else if (mapCondition.get("sort").equals("BDCDYH")) {
				fromsql.append(" ORDER BY DY.BDCDYH "
						+ mapCondition.get("order"));
			} else if (mapCondition.get("sort").equals("SLSJ")) {
				fromsql.append(" ORDER BY XMXX.SLSJ "
						+ mapCondition.get("order"));
			}else if (mapCondition.get("sort").equals("DJSJ")) {
				fromsql.append(" ORDER BY QL.DJSJ "
						+ mapCondition.get("order"));
			}else if (mapCondition.get("sort").equals("ZXSJ")) {
				fromsql.append(" ORDER BY FSQL.ZXSJ "
						+ mapCondition.get("order"));
			}
		}
		List<Map> result =null;
		if(rows>=total){
			result = baseCommonDao.getDataListByFullSql(
					selectSql.append(fromsql).toString(), newpara);
		}else{
			result = baseCommonDao.getPageDataByFullSql(
					selectSql.append(fromsql).toString(), newpara, page, rows);
		}
		List<Map> result_new = new ArrayList<Map>();
		int i = 1 + (page - 1) * rows;
		if (result != null && result.size() > 0) {
			for (Map m : result) {
				if (m.get("LXDH") == null) {
					List<BDCS_QLR_XZ> qlrdhs = baseCommonDao.getDataList(
							BDCS_QLR_XZ.class, " qlid='" + m.get("QLID") + "'");
					if (qlrdhs.size() > 0) {
						m.put("LXDH", qlrdhs.get(0).getDH());
					}
				}
				// liangc 添加列“宗地面积”、“受理时间”、“转移前权利人”、“转移前权利人证件号、“转移前不动产权证号”、“抵押人”
				if (m.containsKey("SLSJ")) {
					m.put("SLSJ", StringHelper.FormatDateOnType(m.get("SLSJ"),"yyyy-MM-dd  HH:mm:ss"));
				}

				String djlx = StringHelper.formatObject(m.get("DJLX"));
				m.put("DJLX", ConstHelper.getNameByValue("DJLX", djlx));
				if ("200".equals(djlx)) {
					String qlid = StringHelper
							.FormatByDatatype(m.get("LYQLID"));
					Rights rights = RightsTools.loadRights(DJDYLY.LS, qlid);
					if (!StringHelper.isEmpty(rights)) {
						m.put("ZYQBDCQZH", StringHelper.FormatByDatatype(rights
								.getBDCQZH()));
					}
					List<RightsHolder> list = RightsHolderTools
							.loadRightsHolders(DJDYLY.LS, qlid);
					if (!StringHelper.isEmpty(list) && list.size() > 0) {
						String ZYQQLR = "";
						String ZYQQLRZJH = "";
						String ZYQQLRLXDH = "";
						for (int j = 0; j < list.size(); j++) {
							if (list.get(j).getQLRMC() != null) {
								ZYQQLR += StringHelper.FormatByDatatype(list
										.get(j).getQLRMC()) + ",";
							}
							if (list.get(j).getZJH() != null) {
								ZYQQLRZJH += StringHelper.FormatByDatatype(list
										.get(j).getZJH()) + ",";
							}
							if (list.get(j).getDH() != null) {
								ZYQQLRLXDH += StringHelper.formatObject(list
										.get(j).getDH()) + ",";
							}
						}
						if (ZYQQLR.length() > 0) {
							ZYQQLR = ZYQQLR.substring(0, ZYQQLR.length() - 1);
						}
						if (ZYQQLRZJH.length() > 0) {
							ZYQQLRZJH = ZYQQLRZJH.substring(0,
									ZYQQLRZJH.length() - 1);
						}
						if (ZYQQLRLXDH.length() > 0) {
							ZYQQLRLXDH = ZYQQLRLXDH.substring(0,
									ZYQQLRLXDH.length() - 1);
						}
						m.put("ZYQQLR", ZYQQLR);
						m.put("ZYQQLRZJH", ZYQQLRZJH);
						m.put("ZYQQLRLXDH", ZYQQLRLXDH);
					}
				}
				
				if ("300".equals(djlx) || "900".equals(djlx) ) {
					String qlid = StringHelper.FormatByDatatype(m.get("LYQLID"));
					Rights rights = RightsTools.loadRights(DJDYLY.LS, qlid);
					if (!StringHelper.isEmpty(rights)) {
						m.put("YQZH", StringHelper.FormatByDatatype(rights.getBDCQZH()));
					}
				}
				String xmbh = m.get("XMBH") + "";
				String ywr = "";
				String ywrzjh = "";
				String ywrdh = "";
				String fulSql = "SELECT TO_CHAR(WM_CONCAT(TO_CHAR(SQR.SQRXM))) AS SQRXM, TO_CHAR(WM_CONCAT(TO_CHAR(SQR.LXDH))) AS LXDH, "
						+ "TO_CHAR(WM_CONCAT(TO_CHAR(SQR.ZJH))) AS ZJH FROM BDCK.BDCS_SQR SQR WHERE SQR.SQRLB='2' "
						+ " AND SQR.XMBH='" + xmbh + "'";
				List<Map> sqrxms = baseCommonDao.getDataListByFullSql(fulSql);
				if (sqrxms != null && sqrxms.size() > 0) {
					ywr = StringHelper.formatObject(sqrxms.get(0).get("SQRXM"));
					ywrzjh = StringHelper.formatObject(sqrxms.get(0).get("ZJH"));
					ywrdh = StringHelper.formatObject(sqrxms.get(0).get("LXDH"));
				}
				m.put("YWR", ywr);
				m.put("YWRZJH", ywrzjh);
				m.put("YWRDH", ywrdh);
				if (m.get("BDBZZQSE")!= null) {
					m.put("DYJE", m.get("BDBZZQSE"));
				}
				if (m.get("ZGZQSE")!= null) {
					m.put("DYJE", m.get("ZGZQSE"));
				}
				// liangc 当户的权利性质为空时，取使用权宗地的权利性质;户的宗地面积取关联宗地的宗地面积
				if (m.get("BDCDYLX") != null) {

					if (m.get("BDCDYLX").equals("031")) {
						if (!StringHelper.isEmpty(m.get("BDCDYID"))) {
							StringBuilder hhql = new StringBuilder(
									" bdcdyid ='" + m.get("BDCDYID") + "'");
							List<BDCS_H_XZ> zdbdcdyids = baseCommonDao
									.getDataList(BDCS_H_XZ.class,
											hhql.toString());
							if (!StringHelper.isEmpty(zdbdcdyids)
									&& zdbdcdyids.size() > 0) {
								String zdbdcdyid = zdbdcdyids.get(0)
										.getZDBDCDYID();
								StringBuilder zdhql = new StringBuilder(
										" bdcdyid ='" + zdbdcdyid + "'");
								List<BDCS_SHYQZD_XZ> qlxzs = baseCommonDao
										.getDataList(BDCS_SHYQZD_XZ.class,
												zdhql.toString());
								if (!StringHelper.isEmpty(qlxzs)
										&& qlxzs.size() > 0) {
									if (StringUtils.isEmpty(m.get("QLXZ"))) {
										m.put("QLXZ", qlxzs.get(0).getQLXZ());
									}
									m.put("ZDMJ", qlxzs.get(0).getZDMJ());
								}
							}
						}
					}
					if (m.get("BDCDYLX").equals("032")) {
						if (!StringHelper.isEmpty(m.get("BDCDYID"))) {
							StringBuilder hhql = new StringBuilder(
									" bdcdyid ='" + m.get("BDCDYID") + "'");
							List<BDCS_H_XZY> zdbdcdyids = baseCommonDao
									.getDataList(BDCS_H_XZY.class,
											hhql.toString());
							if (!StringHelper.isEmpty(zdbdcdyids)
									&& zdbdcdyids.size() > 0) {
								String zdbdcdyid = zdbdcdyids.get(0)
										.getZDBDCDYID();
								StringBuilder zdhql = new StringBuilder(
										" bdcdyid ='" + zdbdcdyid + "'");
								List<BDCS_SHYQZD_XZ> qlxzs = baseCommonDao
										.getDataList(BDCS_SHYQZD_XZ.class,
												zdhql.toString());
								if (!StringHelper.isEmpty(qlxzs)
										&& qlxzs.size() > 0) {
									if (StringUtils.isEmpty(m.get("QLXZ"))) {
										m.put("QLXZ", qlxzs.get(0).getQLXZ());
									}
									m.put("ZDMJ", qlxzs.get(0).getZDMJ());
								}
							}
						}
					}
					// liangc 宗地用途取TDYT表
					if (m.get("BDCDYLX").equals("01")
							|| m.get("BDCDYLX").equals("02")) {
						if (!StringHelper.isEmpty(m.get("BDCDYID"))) {
							StringBuilder zdhql = new StringBuilder(
									" bdcdyid ='" + m.get("BDCDYID") + "'");
							List<BDCS_TDYT_XZ> tdyts = baseCommonDao
									.getDataList(BDCS_TDYT_XZ.class,
											zdhql.toString());
							if ( tdyts.size() > 0&& !StringHelper.isEmpty(tdyts)) {
								StringBuilder tdyt = new StringBuilder();
								if (tdyts.size() > 0) {
									for (int j = 0; j < tdyts.size(); j++) {
										tdyt.append(tdyts.get(j).getTDYTMC());
										if (j < tdyts.size() - 1)
											tdyt.append(",");
									}
									m.put("TDYT", tdyt.toString());
								}
							}
						}
					}

					if (m.get("BDCDYLX").equals("032")
							|| m.get("BDCDYLX").equals("031")|| m.get("BDCDYLX").equals("09")) {
						// System.out.println(m.get("TDYT"));
						String tdyt = StringHelper.formatObject(m.get("TDYT"));
						m.put("TDYT", ConstHelper.getNameByValue("TDYT", tdyt));
					}

					if ("".equals(m.get("TDYT"))) {
						if (m.get("BDCDYLX").equals("031")) {
							if (!StringHelper.isEmpty(m.get("BDCDYID"))) {
								String hsql = " bdcdyid ='" + m.get("BDCDYID")
										+ "'";
								List<BDCS_H_XZ> hs = baseCommonDao.getDataList(
										BDCS_H_XZ.class, hsql);
								if (hs.size() > 0) {
									hsql = " bdcdyid ='"
											+ hs.get(0).getZDBDCDYID() + "'";
									List<BDCS_TDYT_XZ> tdyts = baseCommonDao
											.getDataList(BDCS_TDYT_XZ.class,
													hsql);
									if (!StringHelper.isEmpty(tdyts)
											&& tdyts.size() > 0) {
										// if(tdyts.get(0).getSFZYT().equals("1")){
										StringBuilder tdyt = new StringBuilder();
										if (tdyts.size() > 0) {
											for (int j = 0; j < tdyts.size(); j++) {
												tdyt.append(tdyts.get(j)
														.getTDYTMC());
												if (j < tdyts.size() - 1)
													tdyt.append(",");
											}
											if (!"null".equals(tdyt.toString())) {
												m.put("TDYT", tdyt.toString());
											}
										}
										// }
									}
								}
							}
						}
					}

					if ("".equals(m.get("TDYT"))) {
						if (m.get("BDCDYLX").equals("032")) {
							if (!StringHelper.isEmpty(m.get("BDCDYID"))) {
								String hsql = " bdcdyid ='" + m.get("BDCDYID")
										+ "'";
								List<BDCS_H_XZY> hys = baseCommonDao
										.getDataList(BDCS_H_XZY.class, hsql);
								if (hys.size() > 0) {
									hsql = " bdcdyid ='"
											+ hys.get(0).getZDBDCDYID() + "'";
									List<BDCS_TDYT_XZ> tdyts = baseCommonDao
											.getDataList(BDCS_TDYT_XZ.class,
													hsql);
									if (!StringHelper.isEmpty(tdyts)
											&& tdyts.size() > 0) {
										StringBuilder tdyt = new StringBuilder();
										if (tdyts.size() > 0) {
											for (int j = 0; j < tdyts.size(); j++) {
												tdyt.append(tdyts.get(j)
														.getTDYTMC());
												if (j < tdyts.size() - 1)
													tdyt.append(",");
											}
											if (!"null".equals(tdyt.toString())) {
												m.put("TDYT", tdyt.toString());
											}
										}
									}
								}
							}
						}
					}

					String bdcdylx = StringHelper
							.formatObject(m.get("BDCDYLX"));
					if (m.get("BDCDYLX").equals("032")) {
						m.put("BDCDYLX", "预测户");
					} else {
						m.put("BDCDYLX",
								ConstHelper.getNameByValue("BDCDYLX", bdcdylx));
					}
				}

				/*
				 * String bdcdylx=StringHelper.formatObject(m.get("BDCDYLX"));
				 * m.put("BDCDYLX", ConstHelper.getNameByValue("BDCDYLX",
				 * bdcdylx));
				 */

				if (m.get("QLRLX") != null) {
					if (m.get("QLRLX").toString().contains(",")) {
						String[] qlrlxs = m.get("QLRLX").toString().split(",");
						String qlr = "";
						for (int a = 0; a < qlrlxs.length; a++) {
							qlr += ConstHelper.getNameByValue("QLRLX",
									qlrlxs[a]) + ",";
						}
						qlr = qlr.substring(0, qlr.length() - 1);
						m.put("QLRLX", qlr);
					} else {
						String qlrlx = StringHelper
								.formatObject(m.get("QLRLX"));
						m.put("QLRLX",
								ConstHelper.getNameByValue("QLRLX", qlrlx));
					}
				}
				
				if(StringHelper.isEmpty(m.get("FZSJ"))){
					m.put("SFFZ", "否");
				}else if(!StringHelper.isEmpty(m.get("FZSJ"))){
					m.put("SFFZ", "是");
				}
				
				if(!StringHelper.isEmpty(m.get("CZFS"))&&m.get("CZFS").equals("01")){
					if(!StringHelper.isEmpty(m.get("BDCQZH"))&&m.get("BDCQZH").toString().contains(",")){
						String bdcqzh[] = m.get("BDCQZH").toString().split(",");
						m.put("BDCQZH", bdcqzh[0]);
					}
				}

				String qllx = StringHelper.formatObject(m.get("QLLX"));
				m.put("QLLX", ConstHelper.getNameByValue("QLLX", qllx));

				// String tdyt=StringHelper.formatObject(m.get("TDYT"));
				// m.put("TDYT", ConstHelper.getNameByValue("TDYT", tdyt));

				String qlxz = StringHelper.formatObject(m.get("QLXZ"));
				m.put("QLXZ", ConstHelper.getNameByValue("QLXZ", qlxz));

				String fwyt = StringHelper.formatObject(m.get("FWYT"));
				m.put("FWYT", ConstHelper.getNameByValue("FWYT", fwyt));

				String fwxz = StringHelper.formatObject(m.get("FWXZ"));
				m.put("FWXZ", ConstHelper.getNameByValue("FWXZ", fwxz));

				m.put("XH", StringHelper.formatObject(i));

				m.put("QDJG", StringHelper.formatObject(m.get("QDJG")));

				// liangc
				String fwjg = StringHelper.formatObject(m.get("FWJG"));
				m.put("FWJG", ConstHelper.getNameByValue("FWJG", fwjg));
				String gyfs = StringHelper.formatObject(m.get("GYFS"));
				m.put("GYFS", ConstHelper.getNameByValue("GYFS", gyfs));
				if (!StringHelper.isEmpty(m.get("DJSJ"))&& !"null".equals(xmbh)) {
					ProjectInfo proInfo = ProjectHelper.GetPrjInfoByXMBH(xmbh);
					if (proInfo != null) {
						String Baseworkflow_ID = proInfo.getBaseworkflowcode();
						if (!StringHelper.isEmpty(Baseworkflow_ID) && Baseworkflow_ID.indexOf("JF") != -1) { // 解封登记 ，登记时间获取解封登记的登簿时间

							if (m.containsKey("DJSJ")) {
								m.put("DJSJ", StringHelper.FormatDateOnType(m.get("ZXSJ"),"yyyy-MM-dd  HH:mm:ss"));
							}
						} else {
							m.put("DJSJ", StringHelper.FormatDateOnType(m.get("DJSJ"),"yyyy-MM-dd  HH:mm:ss"));
						} 
					}else {
						m.put("DJSJ", StringHelper.FormatDateOnType(m.get("DJSJ"),"yyyy-MM-dd  HH:mm:ss"));
					} 
				}else if("400".equals(m.get("DJLX"))){
					m.put("DJSJ", StringHelper.FormatDateOnType(m.get("ZXSJ"),"yyyy-MM-dd  HH:mm:ss"));
				}else {
					m.put("DJSJ", StringHelper.FormatDateOnType(m.get("DJSJ"),"yyyy-MM-dd  HH:mm:ss"));
				} 
				i++;
				result_new.add(m);
			}

		}

		Message msg = new Message();
		msg.setRows(result_new);
		msg.setTotal(total);
		return msg;
	}

	/**
	 * 不动产登记台账(云南玉溪)
	 * 
	 * @param mapCondition
	 * @param page
	 * @param rows
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Message GetStandingBookYuxi(Map<String, String> mapCondition,
			Integer page, Integer rows) {
		Map<String, String> newpara = new HashMap<String, String>();
		StringBuilder fromsql = new StringBuilder("FROM BDCK.BDCS_QL_GZ QL ")
				.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON QL.XMBH=XMXX.XMBH ")
				.append("LEFT JOIN BDCK.BDCS_FSQL_GZ FSQL ON QL.QLID=FSQL.QLID ")
				.append("LEFT JOIN BDCK.BDCS_DJDY_LS DJDY ON QL.DJDYID=DJDY.DJDYID ")
				.append("LEFT JOIN ")
				.append("(SELECT BDCDYID,ZL,BDCDYH,'032' AS BDCDYLX,FWTDYT AS TDYT,QLXZ,GHYT AS FWYT,FWXZ,(NVL(FTTDMJ,0)+NVL(DYTDMJ,0)) AS SYQMJ,YCJZMJ AS JZMJ,YCFTJZMJ AS FTJZMJ,YCTNJZMJ AS ZYJZMJ FROM BDCK.BDCS_H_XZY ")
				.append("UNION ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'031' AS BDCDYLX,FWTDYT AS TDYT,QLXZ,GHYT AS FWYT,FWXZ,(NVL(FTTDMJ,0)+NVL(DYTDMJ,0)) AS SYQMJ,SCJZMJ AS JZMJ,SCFTJZMJ AS FTJZMJ,SCTNJZMJ AS ZYJZMJ FROM BDCK.BDCS_H_XZ ")
				.append("UNION  ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'02' AS BDCDYLX,YT AS TDYT,QLXZ,NULL AS FWYT, NULL AS FWXZ,NULL AS SYQMJ,NULL AS JZMJ,NULL AS FTJZMJ,NULL AS ZYJZMJ FROM BDCK.BDCS_SHYQZD_XZ ")
				.append("UNION  ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'01' AS BDCDYLX,YT AS TDYT,QLXZ,NULL AS FWYT, NULL AS FWXZ,NULL AS SYQMJ,NULL AS JZMJ,NULL AS FTJZMJ,NULL AS ZYJZMJ FROM BDCK.BDCS_SYQZD_XZ) DY ")
				.append("ON DY.BDCDYID=DJDY.BDCDYID AND DY.BDCDYLX=DJDY.BDCDYLX ")
				.append("LEFT JOIN ")
				.append("(SELECT TO_CHAR(WM_CONCAT(TO_CHAR(QLRMC))) AS QLRMC,TO_CHAR(WM_CONCAT(TO_CHAR(ZJH))) AS ZJH,TO_CHAR(WM_CONCAT(TO_CHAR(QLRLX))) AS QLRLX,QLID FROM BDCK.BDCS_QLR_GZ GROUP BY QLID) QLR ")
				.append("ON QLR.QLID=QL.QLID ").append("WHERE XMXX.SFDB='1' ");

		if (!StringUtils.isEmpty(mapCondition.get("DJSJ_Q"))) {
			fromsql.append("  AND QL.DJSJ >=to_date(:DJSJ_Q,'yyyy-mm-dd') ");
			try {
				newpara.put("DJSJ_Q", new String(mapCondition.get("DJSJ_Q")
						.getBytes("iso8859-1"), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (!StringUtils.isEmpty(mapCondition.get("DJSJ_Z"))) {
			fromsql.append("  AND QL.DJSJ <=to_date(:DJSJ_Z,'yyyy-mm-dd') ");
			try {
				newpara.put("DJSJ_Z", new String(mapCondition.get("DJSJ_Z")
						.getBytes("iso8859-1"), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		Long total = baseCommonDao.getCountByFullSql(fromsql.toString(),
				newpara);
		if (total == 0)
			return new Message();
		StringBuilder selectSql = new StringBuilder();
		selectSql
				.append(" SELECT XMXX.DJLX,TO_CHAR(QL.DJSJ,'yyyy-mm-dd') AS DJSJ,DY.ZL,DY.BDCDYH,QLR.QLRMC,QL.BDCQZH,");
		selectSql
				.append(" CASE WHEN QL.QLLX = '23' THEN '抵押' ELSE '' END DYZT,");
		selectSql
				.append(" CASE WHEN XMXX.DJLX = '800' THEN '查封' ELSE '' END CFZT,");
		selectSql
				.append(" CASE WHEN XMXX.DJLX = '600' THEN '异议' ELSE '' END YYZT,");
		selectSql
				.append(" CASE WHEN FSQL.DYR IS NOT NULL THEN to_char(FSQL.DYR) WHEN FSQL.YWR IS NOT NULL THEN to_char(FSQL.YWR) ELSE '' END YWR ");
		List<Map> result = baseCommonDao.getPageDataByFullSql(
				selectSql.append(fromsql).append(" ORDER BY XMXX.YWLSH")
						.toString(), newpara, page, rows);
		List<Map> result_new = new ArrayList<Map>();
		int i = 1 + (page - 1) * rows;
		if (result != null && result.size() > 0) {
			for (Map m : result) {
				String djlx = StringHelper.formatObject(m.get("DJLX"));
				m.put("DJLX", ConstHelper.getNameByValue("DJLX", djlx));

				m.put("XH", StringHelper.formatObject(i));
				i++;
				result_new.add(m);
			}
		}
		Message msg = new Message();
		msg.setRows(result_new);
		msg.setTotal(total);
		return msg;
	}

	/**
	 * 导出不动产登记台账(云南玉溪)
	 * 
	 * @param mapCondition
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Map> GetStandingBookYuxi(Map<String, String> mapCondition) {
		Map<String, String> newpara = new HashMap<String, String>();
		StringBuilder fromsql = new StringBuilder("FROM BDCK.BDCS_QL_GZ QL ")
				.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON QL.XMBH=XMXX.XMBH ")
				.append("LEFT JOIN BDCK.BDCS_FSQL_GZ FSQL ON QL.QLID=FSQL.QLID ")
				.append("LEFT JOIN BDCK.BDCS_DJDY_LS DJDY ON QL.DJDYID=DJDY.DJDYID ")
				.append("LEFT JOIN ")
				.append("(SELECT BDCDYID,ZL,BDCDYH,'032' AS BDCDYLX,FWTDYT AS TDYT,QLXZ,GHYT AS FWYT,FWXZ,(NVL(FTTDMJ,0)+NVL(DYTDMJ,0)) AS SYQMJ,YCJZMJ AS JZMJ,YCFTJZMJ AS FTJZMJ,YCTNJZMJ AS ZYJZMJ FROM BDCK.BDCS_H_XZY ")
				.append("UNION ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'031' AS BDCDYLX,FWTDYT AS TDYT,QLXZ,GHYT AS FWYT,FWXZ,(NVL(FTTDMJ,0)+NVL(DYTDMJ,0)) AS SYQMJ,SCJZMJ AS JZMJ,SCFTJZMJ AS FTJZMJ,SCTNJZMJ AS ZYJZMJ FROM BDCK.BDCS_H_XZ ")
				.append("UNION  ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'02' AS BDCDYLX,YT AS TDYT,QLXZ,NULL AS FWYT, NULL AS FWXZ,NULL AS SYQMJ,NULL AS JZMJ,NULL AS FTJZMJ,NULL AS ZYJZMJ FROM BDCK.BDCS_SHYQZD_XZ ")
				.append("UNION  ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'01' AS BDCDYLX,YT AS TDYT,QLXZ,NULL AS FWYT, NULL AS FWXZ,NULL AS SYQMJ,NULL AS JZMJ,NULL AS FTJZMJ,NULL AS ZYJZMJ FROM BDCK.BDCS_SYQZD_XZ) DY ")
				.append("ON DY.BDCDYID=DJDY.BDCDYID AND DY.BDCDYLX=DJDY.BDCDYLX ")
				.append("LEFT JOIN ")
				.append("(SELECT TO_CHAR(WM_CONCAT(TO_CHAR(QLRMC))) AS QLRMC,TO_CHAR(WM_CONCAT(TO_CHAR(ZJH))) AS ZJH,TO_CHAR(WM_CONCAT(TO_CHAR(QLRLX))) AS QLRLX,QLID FROM BDCK.BDCS_QLR_GZ GROUP BY QLID) QLR ")
				.append("ON QLR.QLID=QL.QLID ").append("WHERE XMXX.SFDB='1' ");

		if (!StringUtils.isEmpty(mapCondition.get("DJSJ_Q"))) {
			fromsql.append("  AND QL.DJSJ >=to_date(:DJSJ_Q,'yyyy-mm-dd') ");
			try {
				newpara.put("DJSJ_Q", new String(mapCondition.get("DJSJ_Q")
						.getBytes("iso8859-1"), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (!StringUtils.isEmpty(mapCondition.get("DJSJ_Z"))) {
			fromsql.append("  AND QL.DJSJ <=to_date(:DJSJ_Z,'yyyy-mm-dd') ");
			try {
				newpara.put("DJSJ_Z", new String(mapCondition.get("DJSJ_Z")
						.getBytes("iso8859-1"), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		Long total = baseCommonDao.getCountByFullSql(fromsql.toString(),
				newpara);
		if (total == 0)
			return new ArrayList<Map>();
		StringBuilder selectSql = new StringBuilder();
		selectSql
				.append(" SELECT XMXX.DJLX,TO_CHAR(QL.DJSJ,'yyyy-mm-dd') AS DJSJ,DY.ZL,DY.BDCDYH,QLR.QLRMC,QL.BDCQZH,NVL( CASE WHEN FSQL.DYR IS NOT NULL THEN FSQL.DYR WHEN FSQL.YWR IS NOT NULL THEN FSQL.YWR ELSE NULL END,'') YWR ");
		List<Map> result = baseCommonDao.getDataListByFullSql(
				selectSql.append(fromsql).append(" ORDER BY XMXX.YWLSH")
						.toString(), newpara);
		List<Map> result_new = new ArrayList<Map>();
		int i = 1;
		if (result != null && result.size() > 0) {
			for (Map m : result) {
				String djlx = StringHelper.formatObject(m.get("DJLX"));
				m.put("DJLX", ConstHelper.getNameByValue("DJLX", djlx));

				m.put("XH", StringHelper.formatObject(i));
				i++;
				result_new.add(m);
			}
		}
		return result_new;
	}

	/**
	 * 到处不动产登记台账
	 * 
	 * @作者 俞学斌
	 * @创建时间 2016年4月11日18:21:40
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Map> GetStandingBook(Map<String, String> mapCondition) {
		Map<String, String> newpara = new HashMap<String, String>();
		String temptable = StringHelper.randomTemporaryTableName(Global.getCurrentUserName());//创建一个临时表
		String exeCreatSql="";//创建临时表
		String exeDropSql="";//删除创建的临时表
		StringBuilder fromsql = new StringBuilder("FROM BDCK.BDCS_QL_GZ QL ")
				//确定一对一关系,防止重复
//				.append("LEFT JOIN BDCK.BDCS_QDZR_GZ QDZR ON QDZR.QLID=QL.QLID ")
//				.append("LEFT JOIN BDCK.BDCS_ZS_GZ ZS ON ZS.ZSID=QDZR.ZSID ")
//				.append("LEFT JOIN BDCK.BDCS_ZS_GZ ZS ON QL.XMBH=ZS.XMBH ")
				.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON QL.XMBH=XMXX.XMBH ")
				.append("LEFT JOIN BDC_WORKFLOW.WFI_PROINST PROINST ON XMXX.PROJECT_ID=PROINST.FILE_NUMBER ")
				.append("LEFT JOIN BDCK.BDCS_FSQL_GZ FSQL ON QL.QLID=FSQL.QLID ")
				.append("LEFT JOIN BDCK.BDCS_DJDY_LS DJDY ON QL.DJDYID=DJDY.DJDYID ")
				.append("LEFT JOIN ")
				.append("(SELECT BDCDYID,ZL,BDCDYH,'032' AS BDCDYLX, DJQDM, FWTDYT AS TDYT,QLXZ,GHYT AS FWYT,FWXZ,(NVL(FTTDMJ,0)+NVL(DYTDMJ,0)) AS SYQMJ,YCJZMJ AS JZMJ,YCFTJZMJ AS FTJZMJ,YCTNJZMJ AS ZYJZMJ,ZDMJ,FWJG,SZC AS SZC,ZCS AS ZCS FROM BDCK.BDCS_H_XZY ")
				.append("UNION ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'031' AS BDCDYLX, DJQDM, FWTDYT AS TDYT,QLXZ,GHYT AS FWYT,FWXZ,(NVL(FTTDMJ,0)+NVL(DYTDMJ,0)) AS SYQMJ,SCJZMJ AS JZMJ,SCFTJZMJ AS FTJZMJ,SCTNJZMJ AS ZYJZMJ,ZDMJ,FWJG,SZC AS SZC,ZCS AS ZCS FROM BDCK.BDCS_H_XZ ")
				.append("UNION  ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'02' AS BDCDYLX, DJQDM, YT AS TDYT,QLXZ,NULL AS FWYT, NULL AS FWXZ,NULL AS SYQMJ,NULL AS JZMJ,NULL AS FTJZMJ,NULL AS ZYJZMJ,ZDMJ,NULL AS FWJG,NULL AS SZC,NULL AS ZCS FROM BDCK.BDCS_SHYQZD_XZ ")
				.append("UNION  ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'01' AS BDCDYLX, DJQDM, YT AS TDYT,QLXZ,NULL AS FWYT, NULL AS FWXZ,NULL AS SYQMJ,NULL AS JZMJ,NULL AS FTJZMJ,NULL AS ZYJZMJ,ZDMJ,NULL AS FWJG,NULL AS SZC,NULL AS ZCS FROM BDCK.BDCS_SYQZD_XZ ")
				.append("UNION  ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'04' AS BDCDYLX, NULL AS DJQDM, NULL AS TDYT, NULL AS QLXZ, NULL AS FWYT, NULL AS FWXZ,YHZMJ AS SYQMJ,NULL AS JZMJ,NULL AS FTJZMJ,NULL AS ZYJZMJ,ZHMJ AS ZDMJ,NULL AS FWJG,NULL AS SZC,NULL AS ZCS FROM BDCK.BDCS_ZH_XZ ")
				.append("UNION  ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'05' AS BDCDYLX, DJQDM,TDYT,QLXZ,NULL AS FWYT, NULL AS FWXZ,SYQMJ,NULL AS JZMJ,NULL AS FTJZMJ,NULL AS ZYJZMJ,NULL AS ZDMJ,NULL AS FWJG,NULL AS SZC,NULL AS ZCS FROM BDCK.BDCS_SLLM_XZ ")
				.append("UNION  ")
				.append("SELECT BDCDYID,ZL,BDCDYH,'09' AS BDCDYLX, DJQDM, YT AS TDYT,QLXZ,NULL AS FWYT, NULL AS FWXZ,CBMJ AS SYQMJ,NULL AS JZMJ,NULL AS FTJZMJ,NULL AS ZYJZMJ,NULL AS ZDMJ,NULL AS FWJG,NULL AS SZC,NULL AS ZCS FROM BDCK.BDCS_NYD_XZ) DY ")
				.append("ON DY.BDCDYID=DJDY.BDCDYID AND DY.BDCDYLX=DJDY.BDCDYLX ")
				.append("LEFT JOIN ")
				.append("(SELECT distinct GYFS,LISTAGG(TO_CHAR(DH), ',') WITHIN group(order by DH) AS LXDH,LISTAGG(TO_CHAR(QLRMC), ',') within group(order by bdcqzhxh) AS QLRMC,")
				.append("LISTAGG(TO_CHAR(BDCQZH), ',') within group(order by BDCQZH) AS BDCQZH, LISTAGG(TO_CHAR(ZJH), ',') within group(order by ZJH) AS ZJH,LISTAGG(TO_CHAR(QLRLX), ',') within group(order by QLRLX) AS QLRLX,")
				.append("LISTAGG(TO_CHAR(DLRXM), ',') within group(order by DLRXM) AS QLDLR, LISTAGG(TO_CHAR(DLRZJHM), ',') within group(order by DLRZJHM) AS QLDLRZJH,LISTAGG(TO_CHAR(DLRLXDH), ',') within group(order by DLRLXDH) AS QLDLRLXDH, QLID FROM BDCK.BDCS_QLR_GZ GROUP BY GYFS, QLID) QLR")
				.append(" ON QLR.QLID=QL.QLID ")
				.append("LEFT JOIN BDCK.BDCS_DJFZ DJFZ ON DJFZ.HFZSH=QL.BDCQZH ")
				//分别持证时,证书编号合并
				.append("LEFT JOIN ")
				.append("(select TO_CHAR(WM_CONCAT(DISTINCT TO_CHAR(ZSBH))) AS ZSBH,QLID from (select QDZR.QLID,ZS.ZSBH from BDCK.BDCS_QDZR_GZ qdzr left join BDCK.BDCS_ZS_GZ zs on ZS.ZSID=QDZR.ZSID) group by QLID) ZS ")
				.append("ON ZS.QLID=QL.QLID ")
				.append("WHERE 1=1 ");
		if ("2".equals(mapCondition.get("BLHJ"))) {
			if (!StringUtils.isEmpty(mapCondition.get("DJSJ_Q"))) {
				fromsql.append("  AND QL.DJSJ >=to_date(:DJSJ_Q,'YYYY-MM-DD HH24:MI:SS') ");
				String djsj_q = "";
				try {
					djsj_q = new String(mapCondition.get("DJSJ_Q").getBytes(
							"iso8859-1"), "utf-8");
					djsj_q = djsj_q + " 00:00:00";
					newpara.put("DJSJ_Q", djsj_q);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			if (!StringUtils.isEmpty(mapCondition.get("DJSJ_Z"))) {
				fromsql.append("  AND QL.DJSJ <=to_date(:DJSJ_Z,'YYYY-MM-DD HH24:MI:SS') ");
				String djsj_z = "";
				try {
					djsj_z = new String(mapCondition.get("DJSJ_Z").getBytes(
							"iso8859-1"), "utf-8");
					djsj_z = djsj_z + " 23:59:59";
					newpara.put("DJSJ_Z", djsj_z);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}else if ("3".equals(mapCondition.get("BLHJ"))) {
			if (!StringUtils.isEmpty(mapCondition.get("DJSJ_Q"))) {
				fromsql.append("  AND DJFZ.FZSJ >=to_date(:DJSJ_Q,'YYYY-MM-DD HH24:MI:SS') ");
				String djsj_q = "";
				try {
					djsj_q = new String(mapCondition.get("DJSJ_Q").getBytes(
							"iso8859-1"), "utf-8");
					djsj_q = djsj_q + " 00:00:00";
					newpara.put("DJSJ_Q", djsj_q);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			if (!StringUtils.isEmpty(mapCondition.get("DJSJ_Z"))) {
				fromsql.append("  AND DJFZ.FZSJ <=to_date(:DJSJ_Z,'YYYY-MM-DD HH24:MI:SS') ");
				String djsj_z = "";
				try {
					djsj_z = new String(mapCondition.get("DJSJ_Z").getBytes(
							"iso8859-1"), "utf-8");
					djsj_z = djsj_z + " 23:59:59";
					newpara.put("DJSJ_Z", djsj_z);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		} else {
			if (!StringUtils.isEmpty(mapCondition.get("DJSJ_Q"))) {
				fromsql.append("  AND XMXX.SLSJ >=to_date(:DJSJ_Q,'YYYY-MM-DD HH24:MI:SS') ");
				String djsj_q = "";
				try {
					djsj_q = new String(mapCondition.get("DJSJ_Q").getBytes(
							"iso8859-1"), "utf-8");
					djsj_q = djsj_q + " 00:00:00";
					newpara.put("DJSJ_Q", djsj_q);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			if (!StringUtils.isEmpty(mapCondition.get("DJSJ_Z"))) {
				fromsql.append("  AND XMXX.SLSJ <=to_date(:DJSJ_Z,'YYYY-MM-DD HH24:MI:SS') ");
				String djsj_z = "";
				try {
					djsj_z = new String(mapCondition.get("DJSJ_Z").getBytes(
							"iso8859-1"), "utf-8");
					djsj_z = djsj_z + " 23:59:59";
					newpara.put("DJSJ_Z", djsj_z);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		if (!StringUtils.isEmpty(mapCondition.get("TJLX"))) {
			if (mapCondition.get("TJLX").equals("1"))// 全部
				fromsql.append("  AND DJDY.BDCDYLX IN('01','02','04','05','09','031','032')");
			else if (mapCondition.get("TJLX").equals("2"))// 房
				fromsql.append("  AND DJDY.BDCDYLX IN('031','032')");
			else if (mapCondition.get("TJLX").equals("3"))// 宗地
				fromsql.append("  AND DJDY.BDCDYLX IN('01','02')");
			else if (mapCondition.get("TJLX").equals("4"))// 使用权
				fromsql.append("  AND DJDY.BDCDYLX IN('02')");
			else if (mapCondition.get("TJLX").equals("5"))// 所有权宗地
				fromsql.append("  AND DJDY.BDCDYLX IN('01')");
			else if (mapCondition.get("TJLX").equals("6"))// 预测户
				fromsql.append("  AND DJDY.BDCDYLX IN('032')");
			else if (mapCondition.get("TJLX").equals("7"))// 实测户
				fromsql.append("  AND DJDY.BDCDYLX IN('031')");
			else if (mapCondition.get("TJLX").equals("8"))// 海域
				fromsql.append("  AND DJDY.BDCDYLX IN('04')");
			else if (mapCondition.get("TJLX").equals("9"))// 林地
				fromsql.append("  AND DJDY.BDCDYLX IN('05')");
			else if (mapCondition.get("TJLX").equals("10"))// 农用地
				fromsql.append("  AND DJDY.BDCDYLX IN('09')");
			else
				fromsql.append("  AND DJDY.BDCDYLX IN('031','032') ");

		}

		// liangc 增加登记类型和坐落查询条件
		if (!StringUtils.isEmpty(mapCondition.get("DJLX"))) {
			if (mapCondition.get("DJLX").equals("000"))// 全部登记类型
				fromsql.append(" ");
			else if (mapCondition.get("DJLX").equals("100")) {// 首次登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "100");
			} else if (mapCondition.get("DJLX").equals("200")) {// 转移登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "200");
			} else if (mapCondition.get("DJLX").equals("300")) {// 变更登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "300");
			} else if (mapCondition.get("DJLX").equals("400")) {// 注销登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "400");
			} else if (mapCondition.get("DJLX").equals("500")) {// 更正登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "500");
			} else if (mapCondition.get("DJLX").equals("600")) {// 异议登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "600");
			} else if (mapCondition.get("DJLX").equals("700")) {// 预告 登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "700");
			} else if (mapCondition.get("DJLX").equals("800")) {// 查封登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "800");
			} else if (mapCondition.get("DJLX").equals("900")) {// 其他登记
				fromsql.append("  AND XMXX.DJLX =:DJLX ");
				newpara.put("DJLX", "900");
			} else {
				fromsql.append(" ");
			}
		}

		if (!StringUtils.isEmpty(mapCondition.get("ZL"))) {
			fromsql.append("  AND DY.ZL LIKE:ZL ");
			try {
				newpara.put(
						"ZL",
						"%"
								+ new String(mapCondition.get("ZL").getBytes(
										"iso8859-1"), "utf-8") + "%");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// luml 增加权利类型、土地用途、房屋用途等查询条件
		if (!StringUtils.isEmpty(mapCondition.get("QLLX"))) {
			if (mapCondition.get("QLLX").equals("0"))// 全部权利类型
				fromsql.append(" ");
			else {
				fromsql.append("  AND XMXX.QLLX =:QLLX ");
				newpara.put("QLLX", mapCondition.get("QLLX"));
			}
		}

		if (mapCondition.get("TDYT").equals("0")) {// 全部土地用途
			fromsql.append(" ");
		} else {
			fromsql.append("  AND (DY.TDYT LIKE:TDYT OR BDCDYID IN(SELECT TDYTS.BDCDYID FROM BDCK.BDCS_TDYT_XZ TDYTS WHERE TDYTS.TDYT =:TDYT)) ");
			newpara.put("TDYT", mapCondition.get("TDYT"));
		}

		if (mapCondition.get("FWYT").equals("0")) {// 全部房屋用途
			fromsql.append(" ");
		} else {
			fromsql.append("  AND DY.FWYT LIKE:FWYT ");
			newpara.put("FWYT", mapCondition.get("FWYT"));
		}
		
		//chenbo 增加权利性质、地籍区查询条件
		if (mapCondition.get("QLXZ").equals("0")) {// 全部权利性质
			fromsql.append(" ");
		} else {
			fromsql.append("  AND DY.QLXZ LIKE:QLXZ ");
			newpara.put("QLXZ", mapCondition.get("QLXZ"));
		}
		
		if (mapCondition.get("DJQDM").equals("0")) {// 全部地籍区
			fromsql.append(" ");
		} else {
			fromsql.append("  AND DY.DJQDM LIKE:DJQDM ");
			newpara.put("DJQDM", mapCondition.get("DJQDM"));
		}

		if (!StringUtils.isEmpty(mapCondition.get("SFFZ"))) {
			if (mapCondition.get("SFFZ").equals("2"))
				fromsql.append(" ");
			else if(mapCondition.get("SFFZ").equals("1")){
				fromsql.append(" AND EXISTS(SELECT 1 FROM BDCK.BDCS_DJFZ DJFZ WHERE DJFZ.XMBH=XMXX.XMBH) ");
			}
			else if(mapCondition.get("SFFZ").equals("0")){
				fromsql.append(" AND NOT EXISTS(SELECT 1 FROM BDCK.BDCS_DJFZ DJFZ WHERE DJFZ.XMBH=XMXX.XMBH) ");
			}else{
				fromsql.append(" ");
			}
		}
	if (!StringUtils.isEmpty(mapCondition.get("SFSZ"))) {//是否缮证
			if (mapCondition.get("SFSZ").equals("2"))
				fromsql.append(" ");
			else if(mapCondition.get("SFSZ").equals("1")){
				fromsql.append(" AND EXISTS(SELECT 1 FROM BDCK.BDCS_DJSZ SFSZ WHERE SFSZ.XMBH=XMXX.XMBH) ");
			}
			else if(mapCondition.get("SFSZ").equals("0")){
				fromsql.append(" AND NOT EXISTS(SELECT 1 FROM BDCK.BDCS_DJSZ SFSZ WHERE SFSZ.XMBH=XMXX.XMBH) ");
			}else{
				fromsql.append(" ");
			}
		}
//		Long total = baseCommonDao.getCountByFullSql(fromsql.toString(),
//				newpara);
//		if (total == 0)
//			return new ArrayList<Map>();
		StringBuilder selectSql = new StringBuilder();

		selectSql
		 .append(" SELECT DISTINCT QLR.QLID,QL.QDJG,QL.FJ,QLR.LXDH,XMXX.FCYWH,PROINST.PRODEF_NAME YWLX,ZS.ZSBH,XMXX.XMBH, XMXX.SLRY, XMXX.YWLSH,XMXX.DJLX,QL.DJSJ AS DJSJ,to_char(FSQL.ZXSJ,'YYYY-MM-DD HH24:MI:SS') AS ZXSJ,DY.ZL,DY.BDCDYH,DJDY.BDCDYLX,QLR.QLRMC,QLR.ZJH,QLR.QLRLX,QLR.QLDLR,QLR.QLDLRZJH,QLR.QLDLRLXDH,QLR.BDCQZH,QL.QLLX,QLR.GYFS,")
	      .append("DY.TDYT,DY.QLXZ,DY.FWYT,DY.FWXZ,DY.SYQMJ,DY.JZMJ,DY.FTJZMJ,DY.ZYJZMJ,QL.DJYY,DY.ZDMJ,QL.LYQLID,FSQL.DYR,XMXX.SLSJ,DY.BDCDYID,DY.FWJG,DJFZ.FZSJ,QL.CZFS, FSQL.BDBZZQSE ,FSQL.ZGZQSE,DY.SZC,DY.ZCS  ");
        if (!StringUtils.isEmpty(mapCondition.get("sort"))) {
			if (mapCondition.get("sort").equals("ZL")) {
				fromsql.append(" ORDER BY DY.ZL " + mapCondition.get("order"));
			} else if (mapCondition.get("sort").equals("YWLSH")) {
				fromsql.append(" ORDER BY XMXX.YWLSH "
						+ mapCondition.get("order"));
			} else if (mapCondition.get("sort").equals("BDCDYH")) {
				fromsql.append(" ORDER BY DY.BDCDYH "
						+ mapCondition.get("order"));
			} else if (mapCondition.get("sort").equals("SLSJ")) {
				fromsql.append(" ORDER BY XMXX.SLSJ "
						+ mapCondition.get("order"));
			}else if (mapCondition.get("sort").equals("DJSJ")) {
				fromsql.append(" ORDER BY QL.DJSJ "
						+ mapCondition.get("order"));
			}
		}
       
        String initfromsql=StringHelper.formatSqlByMapParameter(fromsql.toString(),newpara);
        /**
         *传入完整的sql语句，在数据库中创建临时表
         */
        String fullSql =  selectSql.toString() + " " + initfromsql;
        exeCreatSql=StringHelper.createTable("BDCK", temptable, fullSql);
        exeDropSql=StringHelper.dropTable("BDCK", temptable);
        try {
			baseCommonDao.excuteQueryNoResult(exeCreatSql);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("创建临时表出现错误");
			e.printStackTrace();
		}
        List<Map> result=baseCommonDao.getDataListByFullSql("SELECT * FROM  BDCK."+temptable);

        
		 //获取转移前数据
		 String zyqsql="SELECT A.QLID,QLR.DH,QLR.QLRMC,QLR.BDCQZH,QLR.ZJH,QLR.QLRLX FROM BDCK."+temptable+" A "
		 				+ " LEFT JOIN BDCK.BDCS_QLR_LS QLR "
		 				+ " ON QLR.QLID=A.LYQLID "
		 				+ " WHERE A.LYQLID IS NOT NULL AND A.DJLX='200' ";
		 //获取义务人数据
		 String ywsql="SELECT SQR.SQRXM QLRMC,SQR.LXDH DH,SQR.ZJH,A.QLID FROM BDCK."+temptable+" A "
		 			  + " LEFT JOIN BDCK.BDCS_SQR SQR "
		 			  + " ON SQR.XMBH=A.XMBH "
		 			  + " WHERE SQR.SQRLB='2'  AND A.XMBH IS NOT NULL ";
		 //获取房屋数据
		 String housesql="SELECT SHYQZD.QLXZ,SHYQZD.ZDMJ,A.BDCDYLX,A.BDCDYID,TDYT.Tdyt,TDYT.tdytmc,TDYT.ID ,H.ZDBDCDYID FROM BDCK."+temptable+" A "
		 		     + " LEFT JOIN BDCK.BDCS_H_XZ H "
		 		     + " ON H.BDCDYID=A.BDCDYID "
		 		     + " LEFT JOIN BDCK.BDCS_SHYQZD_XZ SHYQZD "
		 		     + " ON H.ZDBDCDYID=SHYQZD.BDCDYID "
		 		     + " LEFT JOIN BDCK.BDCS_TDYT_XZ TDYT "
		 		     + " ON TDYT.BDCDYID= SHYQZD.BDCDYID"
		 		     + " WHERE A.BDCDYLX='031' "
		 		     + " UNION "
		 		     + " SELECT SHYQZD.QLXZ,SHYQZD.ZDMJ,A.BDCDYLX,A.BDCDYID,TDYT.Tdyt,TDYT.tdytmc,TDYT.ID ,YCH.ZDBDCDYID  FROM BDCK."+temptable+" A "
		 		     + " LEFT JOIN BDCK.BDCS_H_XZY YCH "
		 		     + " ON YCH.BDCDYID=A.BDCDYID "
		 		     + " LEFT JOIN BDCK.BDCS_SHYQZD_XZ SHYQZD "
		 		     + " LEFT JOIN BDCK.BDCS_TDYT_XZ TDYT "
		 		     + " ON TDYT.BDCDYID= SHYQZD.BDCDYID"
		 		     + " ON YCH.ZDBDCDYID =SHYQZD.BDCDYID "
		 		     + " WHERE A.BDCDYLX='032' ";
		 //获取土地用途数据
		 String landsql= " SELECT A.BDCDYID,TDYT.Tdyt,TDYT.tdytmc ,TDYT.ID FROM BDCK."+temptable+" A "
		 		       + " LEFT JOIN BDCK.BDCS_TDYT_XZ TDYT "
		 		       + " ON TDYT.BDCDYID=A.BDCDYID "
		 		       + " WHERE A.BDCDYLX IN('01','02')";
		 
		 //获取原不动产权证号
		 String otherSql = " SELECT QL.BDCQZH,QL.QLID,A.LYQLID,A.BDCDYID FROM BDCK."+temptable+" A "
		 				 + " LEFT JOIN BDCK.BDCS_QL_LS QL ON QL.QLID = A.LYQLID "
		 				 + " WHERE A.DJLX IN ('300','900')";
				 
		 
		List<Map> sqrmap=baseCommonDao.getDataListByFullSql(ywsql);//获取申请人数据
		Map <String,Map<String,String>> comsqrmap=joinQlrInfos(sqrmap);
		List<Map> zyqqlrmap=baseCommonDao.getDataListByFullSql(zyqsql);//获取转移前数据
		Map<String,Map<String,String>> comzyqqlrmap=joinQlrInfos(zyqqlrmap);
		
		List<Map> ywrmap=baseCommonDao.getDataListByFullSql(ywsql);//获取义务人数据
		Map<String,Map<String,String>> comywrmap=joinYwrInfos(ywrmap);
		
		List<Map> housemap=baseCommonDao.getDataListByFullSql(housesql);//获取房屋数据
		Map<String,Map<String,String>> comhousemap=joinHouseInfos(housemap);
		
		List<Map> landmap=baseCommonDao.getDataListByFullSql(landsql);//获取土地用途数据
		Map<String,Map<String,String>> comlandmap=joinLandTdyts(landmap);
        
		List<Map> othermap=baseCommonDao.getDataListByFullSql(otherSql);//获取原不动产权证号
		Map<String,Map<String,String>> comothermap=joinother(othermap);
        
		if (result == null||result.size()<=0) {//没有查到数据,结束方法
			return new ArrayList<Map>();
		}		
		List<Map> result_new = new ArrayList<Map>();
		int i = 1;
		if (result != null && result.size() > 0) {
			for (Map m : result) {
				//获取义务人
				if(true){
					String qlid=StringHelper.FormatByDatatype(m.get("QLID"));
					if(!StringHelper.isEmpty(comsqrmap) && comsqrmap.size()>0)
					{
						Map<String,String> tempywrmap=comsqrmap.get(qlid);
						if(!StringHelper.isEmpty(tempywrmap) && tempywrmap.size()>0){
							String dh=StringHelper.FormatByDatatype(tempywrmap.get("LXDH"));
							String qlrmc=StringHelper.FormatByDatatype(tempywrmap.get("QLRMC"));
							String zjh = StringHelper.FormatByDatatype(tempywrmap.get("ZJH"));
							m.put("YWR", qlrmc);
							m.put("YWRDH", dh);
							m.put("YWRZJH", zjh);
						}
					}
				}			
				// liangc 添加列“宗地面积”、“受理时间”、“转移前权利人”、“转移前权利人证件号、“转移前不动产权证号”、“抵押人”
				if (m.containsKey("SLSJ")) {
					m.put("SLSJ", StringHelper.FormatDateOnType(m.get("SLSJ"),"yyyy-MM-dd  HH:mm:ss"));
				}
				String djlx = StringHelper.formatObject(m.get("DJLX"));
				m.put("DJLX", ConstHelper.getNameByValue("DJLX", djlx));
				
				//TODO 对应comothermap
				if(DJLX.BGDJ.Value.equals(djlx) || DJLX.QTDJ.Value.equals(djlx)){
					String bdcdyid=StringHelper.FormatByDatatype(m.get("BDCDYID"));
					if(!StringHelper.isEmpty(comothermap) && comothermap.size()>0){
						Map<String,String> tempothermap=comothermap.get(bdcdyid);
						if(!StringHelper.isEmpty(tempothermap) && tempothermap.size()>0){
							String yqzh=StringHelper.FormatByDatatype(tempothermap.get("YQZH"));
							m.put("YQZH", yqzh);
						}
					}
				}
				
				//TODO 对应comzyqqrlmap
				if(DJLX.ZYDJ.Value.equals(djlx)){
					String qlid=StringHelper.FormatByDatatype(m.get("QLID"));
					if(!StringHelper.isEmpty(comzyqqlrmap) && comzyqqlrmap.size()>0)
					{
						Map<String,String> tempzyqqlrmap=comzyqqlrmap.get(qlid);
						if(!StringHelper.isEmpty(tempzyqqlrmap) && tempzyqqlrmap.size()>0){
							String dh=StringHelper.FormatByDatatype(tempzyqqlrmap.get("LXDH"));
							String qlrmc=StringHelper.FormatByDatatype(tempzyqqlrmap.get("QLRMC"));
							String bdcqzh=StringHelper.FormatByDatatype(tempzyqqlrmap.get("BDCQZH"));
							String zjh=StringHelper.FormatByDatatype(tempzyqqlrmap.get("ZJH"));
							m.put("ZYQQLR", qlrmc);
							m.put("ZYQQLRZJH", zjh);
							m.put("ZYQQLRLXDH", dh);
							if(!StringHelper.isEmpty(bdcqzh)){
								List<String> listqzh=Arrays.asList(bdcqzh.split(","));
								if(listqzh!=null&&listqzh.size()>1){
									List<String> listqzh_new=new ArrayList<String>();
									for(String newqzh:listqzh){
										if(!StringHelper.isEmpty(newqzh)&&!listqzh_new.contains(newqzh)){
											listqzh_new.add(newqzh);
										}
									}
									bdcqzh=StringHelper.formatList(listqzh_new, ",");
								}
							}
							m.put("ZYQBDCQZH", bdcqzh);
						}
					}
				}
				//TODO 对应comywrmap
				String xmbh=StringHelper.FormatByDatatype(m.get("XMBH"));
				if(!StringHelper.isEmpty(xmbh)){
					if(!StringHelper.isEmpty(comywrmap) && comywrmap.size()>0)
					{
						Map<String,String> tempywrmap=comywrmap.get(xmbh);
						if(!StringHelper.isEmpty(tempywrmap) && tempywrmap.size()>0){
							String templxdh=StringHelper.FormatByDatatype(tempywrmap.get("LXDH"));
							String tempsqrxm=StringHelper.FormatByDatatype(tempywrmap.get("SQRXM"));
							m.put("YWR", tempsqrxm);
							m.put("YWRDH", templxdh);
						}
						
					}
				}		
				
				
				//TODO comhousemap
				String bdcdylx=StringHelper.FormatByDatatype(m.get("BDCDYLX"));
				if(BDCDYLX.H.Value.equals(bdcdylx) || BDCDYLX.YCH.Value.equals(bdcdylx)){
					String bdcdyid=StringHelper.FormatByDatatype(m.get("BDCDYID"));
					String tdyt = StringHelper.formatObject(m.get("TDYT"));
					m.put("TDYT", ConstHelper.getNameByValue("TDYT", tdyt));
					if(!StringHelper.isEmpty(bdcdyid)){
						if(!StringHelper.isEmpty(comhousemap) && comhousemap.size()>0)
						{
							Map<String,String> temphousemap=comhousemap.get(bdcdyid);
							if(!StringHelper.isEmpty(temphousemap) && temphousemap.size()>0){
								String tempqlxz=StringHelper.FormatByDatatype(temphousemap.get("QLXZ"));
								String tempzdmj=StringHelper.FormatByDatatype(temphousemap.get("ZDMJ"));
								m.put("QLXZ", tempqlxz);
								m.put("ZDMJ", tempzdmj);
								String tdyt1=StringHelper.FormatByDatatype(m.get("TDYT"));
								if(StringHelper.isEmpty(tdyt1)){
									m.put("TDYT", StringHelper.FormatByDatatype(temphousemap.get("TDYT")));
								}
							}
							
						}
					}
				}
					if(BDCDYLX.SHYQZD.Value.equals(bdcdylx) ||BDCDYLX.SYQZD.Value.equals(bdcdylx)){
						if(!StringHelper.isEmpty(comlandmap) && comlandmap.size()>0)
						{
							String bdcdyid=StringHelper.FormatByDatatype(m.get("BDCDYID"));
							if(!StringHelper.isEmpty(bdcdyid)){
								Map<String,String> templandmap=comlandmap.get(bdcdyid);
								if(!StringHelper.isEmpty(templandmap) && templandmap.size()>0){
									String temptdyt=StringHelper.FormatByDatatype(templandmap.get("TDYT"));
									m.put("TDYT", temptdyt);
								}
							}
						}
					}	
					if (m.get("BDCDYLX").equals("09")) {
						String tdyt = StringHelper.formatObject(m.get("TDYT"));
						m.put("TDYT", ConstHelper.getNameByValue("TDYT", tdyt));
					}

					if (m.get("BDCDYLX").equals("032")) {
						m.put("BDCDYLX", "预测户");
					} else {
						m.put("BDCDYLX",
								ConstHelper.getNameByValue("BDCDYLX", bdcdylx));
					}

				if (m.get("QLRLX") != null) {
					if (m.get("QLRLX").toString().contains(",")) {
						String[] qlrlxs = m.get("QLRLX").toString().split(",");
						String qlr = "";
						for (int a = 0; a < qlrlxs.length; a++) {
							qlr += ConstHelper.getNameByValue("QLRLX",
									qlrlxs[a]) + ",";
						}
						qlr = qlr.substring(0, qlr.length() - 1);
						m.put("QLRLX", qlr);
					} else {
						String qlrlx = StringHelper
								.formatObject(m.get("QLRLX"));
						m.put("QLRLX",
								ConstHelper.getNameByValue("QLRLX", qlrlx));
					}
				}
				
				if(StringHelper.isEmpty(m.get("FZSJ"))){
					m.put("SFFZ", "否");
				}else if(!StringHelper.isEmpty(m.get("FZSJ"))){
					m.put("SFFZ", "是");
				}
				if(!StringHelper.isEmpty(m.get("CZFS"))&&m.get("CZFS").equals("01")){
					if(!StringHelper.isEmpty(m.get("BDCQZH"))&&m.get("BDCQZH").toString().contains(",")){
						String bdcqzh[] = m.get("BDCQZH").toString().split(",");
						m.put("BDCQZH", bdcqzh[0]);
					}
				}
				

				String qllx = StringHelper.formatObject(m.get("QLLX"));
				m.put("QLLX", ConstHelper.getNameByValue("QLLX", qllx));

				String qlxz = StringHelper.formatObject(m.get("QLXZ"));
				m.put("QLXZ", ConstHelper.getNameByValue("QLXZ", qlxz));

				String fwyt = StringHelper.formatObject(m.get("FWYT"));
				m.put("FWYT", ConstHelper.getNameByValue("FWYT", fwyt));

				String fwxz = StringHelper.formatObject(m.get("FWXZ"));
				m.put("FWXZ", ConstHelper.getNameByValue("FWXZ", fwxz));

				String YWLX = StringHelper.formatObject(m.get("YWLX"));
				if (!StringHelper.isEmpty(YWLX)) {
					m.put("YWLX", YWLX.replace('.', '-'));
				}
				
				m.put("XH", StringHelper.formatObject(i));

				m.put("QDJG", StringHelper.formatObject(m.get("QDJG")));
				if (m.get("BDBZZQSE")!= null) {
					m.put("DYJE", m.get("BDBZZQSE"));
				}
				if (m.get("ZGZQSE")!= null) {
					m.put("DYJE", m.get("ZGZQSE"));
				}
				// liangc
				String fwjg = StringHelper.formatObject(m.get("FWJG"));
				m.put("FWJG", ConstHelper.getNameByValue("FWJG", fwjg));
				String gyfs = StringHelper.formatObject(m.get("GYFS"));
				m.put("GYFS", ConstHelper.getNameByValue("GYFS", gyfs));
				
				if (!StringHelper.isEmpty(m.get("DJSJ"))&& !"null".equals(xmbh)) {
					ProjectInfo proInfo = ProjectHelper.GetPrjInfoByXMBH(xmbh);
					if (proInfo != null) {
						String Baseworkflow_ID = proInfo.getBaseworkflowcode();
						if (!StringHelper.isEmpty(Baseworkflow_ID) && Baseworkflow_ID.indexOf("JF") != -1) { // 解封登记 ，登记时间获取解封登记的登簿时间

							if (m.containsKey("DJSJ")) {
								m.put("DJSJ", StringHelper.FormatDateOnType(m.get("ZXSJ"),"yyyy-MM-dd  HH:mm:ss"));
							}
						} else {
							m.put("DJSJ", StringHelper.FormatDateOnType(m.get("DJSJ"),"yyyy-MM-dd  HH:mm:ss"));
						} 
					}else {
						m.put("DJSJ", StringHelper.FormatDateOnType(m.get("DJSJ"),"yyyy-MM-dd  HH:mm:ss"));
					} 
				}else if("400".equals(m.get("DJLX"))){
					m.put("DJSJ", StringHelper.FormatDateOnType(m.get("ZXSJ"),"yyyy-MM-dd  HH:mm:ss"));
				}else {
					m.put("DJSJ", StringHelper.FormatDateOnType(m.get("DJSJ"),"yyyy-MM-dd  HH:mm:ss"));
				}
				
				i++;
				result_new.add(m);
			}
		}
		//导出完成后，干掉临时创建的表，要不然数据库会有临时表存在
		 try {
				baseCommonDao.excuteQueryNoResult(exeDropSql);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("删除临时表出现错误");
				e.printStackTrace();
			}
		 
		return result_new;
	}

	/**
	 * 自定义查询统计获取数据服务
	 * 
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Message GetUserDefineBook(String id, HttpServletRequest request) {
		Message m = new Message();
		// 获取page和rows参数
		Integer pageIndex = RequestHelper.getPage(request);
		Integer pageSize = RequestHelper.getRows(request);
		T_USERDEFINEBOOK book = baseCommonDao.get(T_USERDEFINEBOOK.class, id);
		String configsql = "";
		if (book != null) {
			configsql = book.getCONFIGSQL();
		}
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		List<T_QUERYCONFIG_USERDEFINEBOOK> list_queryconfig = baseCommonDao
				.getDataList(T_QUERYCONFIG_USERDEFINEBOOK.class, " BOOKID='"
						+ id + "' ORDER BY VIEWORDER");
		if (list_queryconfig != null && list_queryconfig.size() > 0) {
			for (int i = 0; i < list_queryconfig.size(); i++) {
				String name = list_queryconfig.get(i).getFIELDNAME();
				String hqltype = list_queryconfig.get(i).getHQLTYPE();
				if (!configsql.contains(":" + name + " ")
						&& !configsql.contains(":" + name + ",")) {
					continue;
				}
				String value = "";
				try {
					value = RequestHelper.getParam(request, name);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				value = StringHelper.formatObject(list_queryconfig.get(i)
						.getHQLPRE())
						+ value
						+ StringHelper.formatObject(list_queryconfig.get(i)
								.getHQLSUFFIX());
				if ("double".equals(hqltype)) {
					paramMap.put(name, StringHelper.getDouble(value));
				} else if ("integer".equals(hqltype)) {
					paramMap.put(name, StringHelper.getInt(value));
				} else if ("date".equals(hqltype)) {
					try {
						paramMap.put(name,
								StringHelper.FormatByDate(value, "yyyy-MM-dd"));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else if ("datetime".equals(hqltype)) {
					try {
						paramMap.put(name, StringHelper.FormatByDate(value,
								"yyyy-MM-dd HH:mm:ss"));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else {
					paramMap.put(name, value);
				}

			}
		}
		long l = baseCommonDao.getCountByFullSqlEx(" from (" + configsql + ")",
				paramMap);
		List<Map> list = new ArrayList<Map>();
		if (l > 0) {
			list = baseCommonDao.getPageDataByFullSqlEx(configsql, paramMap,
					pageIndex, pageSize);
			List<T_GRIDCONFIG_USERDEFINEBOOK> list_gridconfig = baseCommonDao
					.getDataList(T_GRIDCONFIG_USERDEFINEBOOK.class, " BOOKID='"
							+ id + "' AND NVL2(CONSTTYPE,1,0)=1");
			if (list_gridconfig != null && list_gridconfig.size() > 0) {
				if (list != null && list.size() > 0) {
					for (Map map : list) {
						for (T_GRIDCONFIG_USERDEFINEBOOK grid_config : list_gridconfig) {
							String name = grid_config.getFIELDNAME();
							String consttype = grid_config.getCONSTTYPE();
							if (map.containsKey(name)) {
								String value = StringHelper.formatObject(map
										.get(name));
								map.remove(name);
								if ("date".equals(consttype)) {
									try {
										map.put(name, StringHelper
												.FormatDateOnType(StringHelper
														.FormatByDate(value,
																"yyyy-MM-dd"),
														"yyyy-MM-dd"));
									} catch (ParseException e) {
										e.printStackTrace();
									}
								} else if ("datetime".equals(consttype)) {
									try {
										map.put(name,
												StringHelper.FormatDateOnType(
														StringHelper
																.FormatByDate(
																		value,
																		"yyyy-MM-dd HH:mm:ss"),
														"yyyy-MM-dd HH:mm:ss"));
									} catch (ParseException e) {
										e.printStackTrace();
									}
								} else {
									if(!StringHelper.isEmpty(value)){
										String[] values = value.split(",");
										List<String> namelist = new ArrayList<String>();
										for (String string : values) {
											if(!StringHelper.isEmpty(string))
												namelist.add(ConstHelper.getNameByValue(consttype, string));
										}
										map.put(name, StringHelper.formatList(namelist));
									}
								}

							}
						}
					}
				}
			}
		}
		m.setRows(list);
		m.setSuccess("true");
		m.setTotal(l);
		m.setMsg("查询完成！");
		return m;
	}

	/**
	 * 获取自定义查询统计配置信息
	 * 
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Map> UserDefineBookALL(String id, HttpServletRequest request) {
		T_USERDEFINEBOOK book = baseCommonDao.get(T_USERDEFINEBOOK.class, id);
		String configsql = "";
		if (book != null) {
			configsql = book.getCONFIGSQL();
		}
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		List<T_QUERYCONFIG_USERDEFINEBOOK> list_queryconfig = baseCommonDao
				.getDataList(T_QUERYCONFIG_USERDEFINEBOOK.class, " BOOKID='"
						+ id + "' ORDER BY VIEWORDER");
		if (list_queryconfig != null && list_queryconfig.size() > 0) {
			for (int i = 0; i < list_queryconfig.size(); i++) {
				String name = list_queryconfig.get(i).getFIELDNAME();
				String hqltype = list_queryconfig.get(i).getHQLTYPE();
				if (!configsql.contains(":" + name + " ")
						&& !configsql.contains(":" + name + ",")) {
					continue;
				}
				String value = "";
				try {
					value = RequestHelper.getParam(request, name);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				value = StringHelper.formatObject(list_queryconfig.get(i)
						.getHQLPRE())
						+ value
						+ StringHelper.formatObject(list_queryconfig.get(i)
								.getHQLSUFFIX());
				if ("double".equals(hqltype)) {
					paramMap.put(name, StringHelper.getDouble(value));
				} else if ("integer".equals(hqltype)) {
					paramMap.put(name, StringHelper.getInt(value));
				} else if ("date".equals(hqltype)) {
					try {
						paramMap.put(name,
								StringHelper.FormatByDate(value, "yyyy-MM-dd"));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else if ("datetime".equals(hqltype)) {
					try {
						paramMap.put(name, StringHelper.FormatByDate(value,
								"yyyy-MM-dd HH:mm:ss"));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else {
					paramMap.put(name, value);
				}

			}
		}
		List<Map> list = baseCommonDao.getDataListByFullSqlEx(configsql,
				paramMap);
		List<T_GRIDCONFIG_USERDEFINEBOOK> list_gridconfig = baseCommonDao
				.getDataList(T_GRIDCONFIG_USERDEFINEBOOK.class, " BOOKID='"
						+ id + "' AND NVL2(CONSTTYPE,1,0)=1");
		if (list_gridconfig != null && list_gridconfig.size() > 0) {
			if (list != null && list.size() > 0) {
				for (Map map : list) {
					for (T_GRIDCONFIG_USERDEFINEBOOK grid_config : list_gridconfig) {
						String name = grid_config.getFIELDNAME();
						String consttype = grid_config.getCONSTTYPE();
						if (map.containsKey(name)) {
							String value = StringHelper.formatObject(map
									.get(name));
							map.remove(name);
							if (StringHelper.isEmpty(value)) {
								map.put(name, "");
								continue;
							}
							if ("date".equals(consttype)) {
								try {
									map.put(name, StringHelper
											.FormatDateOnType(StringHelper
													.FormatByDate(value,
															"yyyy-MM-dd"),
													"yyyy-MM-dd"));
								} catch (ParseException e) {
									e.printStackTrace();
								}
							} else if ("datetime".equals(consttype)) {
								try {
									map.put(name,
											StringHelper.FormatDateOnType(
													StringHelper
															.FormatByDate(
																	value,
																	"yyyy-MM-dd HH:mm:ss"),
													"yyyy-MM-dd HH:mm:ss"));
								} catch (ParseException e) {
									e.printStackTrace();
								}
							} else {
								map.put(name, ConstHelper.getNameByValue(
										consttype, value));
							}

						}
					}
				}
			}
		}
		return list;
	}

	/**
	 * 自定义查询统计定义管理页面
	 * 
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Override
	public HashMap<String, Object> GetUserDefineBookConfig(String id) {
		HashMap<String, Object> m = new HashMap<String, Object>();
		T_USERDEFINEBOOK book = baseCommonDao.get(T_USERDEFINEBOOK.class, id);
		if (book != null) {
			m.put("title", book.getNAME());
		}
		List<T_QUERYCONFIG_USERDEFINEBOOK> list_queryconfig = baseCommonDao
				.getDataList(T_QUERYCONFIG_USERDEFINEBOOK.class, " BOOKID='"
						+ id + "' ORDER BY VIEWORDER");
		if (list_queryconfig != null && list_queryconfig.size() > 0) {
			m.put("queryfields", list_queryconfig);
		}
		List<T_GRIDCONFIG_USERDEFINEBOOK> list_gridconfig = baseCommonDao
				.getDataList(T_GRIDCONFIG_USERDEFINEBOOK.class, " BOOKID='"
						+ id + "' ORDER BY VIEWORDER");
		if (list_gridconfig != null && list_gridconfig.size() > 0) {
			m.put("resultfields", list_gridconfig);
		}
		m.put("bookid", id);
		return m;
	}

	/**
	 * 自定义查询统计定义列表
	 * 
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Override
	public Message GetBookDefineList(HttpServletRequest request) {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String filter = "";
		try {
			filter = RequestHelper.getParam(request, "filter");
		} catch (Exception e) {
		}

		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 ");
		if (!StringHelper.isEmpty(filter)) {
			hqlBuilder.append(" AND NAME LIKE '%").append(filter)
					.append("%' OR ID LIKE '%").append(filter).append("%'");
		}
		hqlBuilder.append(" ORDER BY ID ");
		Page p = baseCommonDao.getPageDataByHql(T_USERDEFINEBOOK.class,
				hqlBuilder.toString(), page, rows);
		Message m = new Message();
		m.setSuccess("true");
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		m.setMsg("成功！");
		return m;
	}

	/**
	 * 添加或更新自定义查询统计定义
	 * 
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResultMessage AddOrUpdateBookDefine(HttpServletRequest request) {
		ResultMessage msg = new ResultMessage();
		String id = request.getParameter("id");
		String operate = request.getParameter("operate");
		String name = request.getParameter("name");
		String configsql = request.getParameter("configsql");
		boolean bnew = true;
		T_USERDEFINEBOOK book = null;
		if ("add".endsWith(operate)) {
			book = baseCommonDao.get(T_USERDEFINEBOOK.class, id);
			if (book != null) {
				msg.setSuccess("false");
				msg.setMsg("模块字段标识已存在！");
				return msg;
			}
			book = new T_USERDEFINEBOOK();
			book.setId(id);
			bnew = false;
		} else {
			book = baseCommonDao.get(T_USERDEFINEBOOK.class, id);
			if (book == null) {
				msg.setSuccess("false");
				msg.setMsg("模块定义不存在！");
				return msg;
			}
		}
		book.setCONFIGSQL(configsql);
		book.setNAME(name);
		if (bnew)
			baseCommonDao.save(book);
		else
			baseCommonDao.update(book);
		baseCommonDao.flush();
		msg.setSuccess("true");
		msg.setMsg("保存成功");
		return msg;
	}

	/**
	 * 删除自定义查询统计定义
	 * 
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResultMessage RemoveBookDefine(String id) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("删除失败");
		T_USERDEFINEBOOK module = baseCommonDao.get(T_USERDEFINEBOOK.class, id);
		if (module != null) {
			baseCommonDao.deleteEntity(module);
			baseCommonDao.flush();
			msg.setSuccess("true");
			msg.setMsg("删除成功");
		} else {
			msg.setSuccess("false");
			msg.setMsg("未查询到要删除模块定义！");
		}
		return msg;
	}

	/**
	 * 自定义查询统计查询条件列表
	 * 
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Override
	public Message GetQueryManager(String bookid) {
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 AND BOOKID='" + bookid + "'");
		hqlBuilder.append(" ORDER BY VIEWORDER ");
		List<T_QUERYCONFIG_USERDEFINEBOOK> list = baseCommonDao.getDataList(
				T_QUERYCONFIG_USERDEFINEBOOK.class, hqlBuilder.toString());
		long l = 0;
		if (list != null && list.size() > 0) {
			l = list.size();
		}
		Message m = new Message();
		m.setSuccess("true");
		m.setTotal(l);
		m.setRows(list);
		m.setMsg("成功！");
		return m;
	}

	/**
	 * 添加或更新自定义查询统计查询条件
	 * 
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResultMessage AddOrUpdateQueryManager(String bookid,
			HttpServletRequest request) {
		ResultMessage msg = new ResultMessage();
		if (!StringHelper.isEmpty(bookid)) {
			msg.setSuccess("false");
			msg.setMsg("保存失败！未获取到查询统计模块定义！");
		}
		String id = request.getParameter("id");
		String fieldname = request.getParameter("fieldname").toUpperCase();
		String fieldcaption = request.getParameter("fieldcaption");
		String fieldtype = request.getParameter("fieldtype");
		String consttype = request.getParameter("consttype").toUpperCase();
		String hqltype = request.getParameter("hqltype");
		String hqlpre = request.getParameter("hqlpre");
		String hqlsuffix = request.getParameter("hqlsuffix");
		String vieworder = request.getParameter("vieworder");
		int int_vieworder = StringHelper.getInt(vieworder);

		T_QUERYCONFIG_USERDEFINEBOOK querymanager = null;
		boolean bnew = true;
		if (!StringHelper.isEmpty(id)) {
			querymanager = baseCommonDao.get(
					T_QUERYCONFIG_USERDEFINEBOOK.class, id);
			if (int_vieworder > 0) {
				querymanager.setVIEWORDER(int_vieworder);
			}
			bnew = false;
		}
		if (querymanager == null) {
			querymanager = new T_QUERYCONFIG_USERDEFINEBOOK();
			id = SuperHelper.GeneratePrimaryKey();
			querymanager.setId(id);
			querymanager.setBOOKID(bookid);
			if (int_vieworder > 0) {
				querymanager.setVIEWORDER(int_vieworder);
			} else {
				List<Map> list_sxh = baseCommonDao
						.getDataListByFullSql("SELECT MAX(VIEWORDER) AS VIEWORDER FROM BDCK.T_QUERYCONFIG_USERDEFINEBOOK WHERE BOOKID='"
								+ bookid + "'");
				Integer sxh = 1;
				if (list_sxh != null
						&& list_sxh.size() > 0
						&& !StringHelper.isEmpty(list_sxh.get(0).get(
								"VIEWORDER"))) {
					sxh = StringHelper.getInt(list_sxh.get(0).get("VIEWORDER"));
					sxh = sxh + 1;
				}
				querymanager.setVIEWORDER(sxh);
			}
		}
		querymanager.setFIELDNAME(fieldname);
		querymanager.setFIELDCAPTION(fieldcaption);
		querymanager.setFIELDTYPE(fieldtype);
		querymanager.setCONSTTYPE(consttype);
		querymanager.setHQLTYPE(hqltype);
		querymanager.setHQLPRE(hqlpre);
		querymanager.setHQLSUFFIX(hqlsuffix);
		if (bnew)
			baseCommonDao.save(querymanager);
		else
			baseCommonDao.update(querymanager);
		baseCommonDao.flush();
		msg.setSuccess("true");
		msg.setMsg("保存成功");
		return msg;
	}

	/**
	 * 删除自定义查询统计查询条件
	 * 
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResultMessage RemoveQueryManager(String id) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("删除失败");
		T_QUERYCONFIG_USERDEFINEBOOK querymanager = baseCommonDao.get(
				T_QUERYCONFIG_USERDEFINEBOOK.class, id);
		if (querymanager != null) {
			baseCommonDao.deleteEntity(querymanager);
			baseCommonDao.flush();
			msg.setSuccess("true");
			msg.setMsg("删除成功");
		} else {
			msg.setSuccess("false");
			msg.setMsg("未查询到要删除单元字段定义！");
		}
		return msg;
	}

	/**
	 * 自定义查询统计结果字段列表
	 * 
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Override
	public Message GetResultManager(String bookid) {
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 AND BOOKID='" + bookid + "'");
		hqlBuilder.append(" ORDER BY VIEWORDER ");
		List<T_GRIDCONFIG_USERDEFINEBOOK> list = baseCommonDao.getDataList(
				T_GRIDCONFIG_USERDEFINEBOOK.class, hqlBuilder.toString());
		long l = 0;
		if (list != null && list.size() > 0) {
			l = list.size();
		}
		Message m = new Message();
		m.setSuccess("true");
		m.setTotal(l);
		m.setRows(list);
		m.setMsg("成功！");
		return m;
	}

	/**
	 * 添加或更新自定义查询统计结果字段
	 * 
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResultMessage AddOrUpdateResultManager(String bookid,
			HttpServletRequest request) {
		ResultMessage msg = new ResultMessage();
		if (!StringHelper.isEmpty(bookid)) {
			msg.setSuccess("false");
			msg.setMsg("保存失败！未获取到查询统计模块定义！");
		}
		String id = request.getParameter("id");
		String fieldname = request.getParameter("fieldname").toUpperCase();
		String columntext = request.getParameter("columntext");
		String width = request.getParameter("width");
		String consttype = request.getParameter("consttype");
		String vieworder = request.getParameter("vieworder");
		int int_vieworder = StringHelper.getInt(vieworder);

		T_GRIDCONFIG_USERDEFINEBOOK querymanager = null;
		boolean bnew = true;
		if (!StringHelper.isEmpty(id)) {
			querymanager = baseCommonDao.get(T_GRIDCONFIG_USERDEFINEBOOK.class,
					id);
			if (int_vieworder > 0) {
				querymanager.setVIEWORDER(int_vieworder);
			}
			bnew = false;
		}
		if (querymanager == null) {
			querymanager = new T_GRIDCONFIG_USERDEFINEBOOK();
			id = SuperHelper.GeneratePrimaryKey();
			querymanager.setId(id);
			querymanager.setBOOKID(bookid);
			if (int_vieworder > 0) {
				querymanager.setVIEWORDER(int_vieworder);
			} else {
				List<Map> list_sxh = baseCommonDao
						.getDataListByFullSql("SELECT MAX(VIEWORDER) AS VIEWORDER FROM BDCK.T_GRIDCONFIG_USERDEFINEBOOK WHERE BOOKID='"
								+ bookid + "'");
				Integer sxh = 1;
				if (list_sxh != null
						&& list_sxh.size() > 0
						&& !StringHelper.isEmpty(list_sxh.get(0).get(
								"VIEWORDER"))) {
					sxh = StringHelper.getInt(list_sxh.get(0).get("VIEWORDER"));
					sxh = sxh + 1;
				}
				querymanager.setVIEWORDER(sxh);
			}
		}
		querymanager.setFIELDNAME(fieldname);
		querymanager.setCOLUMNTEXT(columntext);
		querymanager.setWIDTH(width);
		querymanager.setCONSTTYPE(consttype);
		if (bnew)
			baseCommonDao.save(querymanager);
		else
			baseCommonDao.update(querymanager);
		baseCommonDao.flush();
		msg.setSuccess("true");
		msg.setMsg("保存成功");
		return msg;
	}

	/**
	 * 删除自定义查询统计结果字段
	 * 
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResultMessage RemoveResultManager(String id) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("删除失败");
		T_GRIDCONFIG_USERDEFINEBOOK resultmanager = baseCommonDao.get(
				T_GRIDCONFIG_USERDEFINEBOOK.class, id);
		if (resultmanager != null) {
			baseCommonDao.deleteEntity(resultmanager);
			baseCommonDao.flush();
			msg.setSuccess("true");
			msg.setMsg("删除成功");
		} else {
			msg.setSuccess("false");
			msg.setMsg("未查询到要删除单元字段定义！");
		}
		return msg;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Message getSFTJ(String tjsjks, String tjsjjz, String sfry,
			String sfbmmc) {
		StringBuilder builder = new StringBuilder();
		Message m = new Message();
		try {
			builder.append("SELECT T.SFBMMC,ROUND(SUM(NVL(SSJE,0)),2) AS SSJE, ROUND(SUM(NVL(YSJE,0)),2) AS YSJE,COUNT(DISTINCT T.YWH) AS XMGS,");
			builder.append("TO_CHAR(CONCAT(Y.SFXLMC, Y.SFKMMC)) AS SFMC,TO_CHAR(T.SFLX),T.SFDYID,'0' AS TB ");
			builder.append("FROM BDCK.BDCS_DJSF T ");
			builder.append("LEFT JOIN BDCK.BDCS_SFDY Y ON T.SFDYID=Y.ID ");
			builder.append("WHERE T.SSJE IS NOT NULL AND Y.TJBZ<>0 ");
			builder.append("AND T.YWH IN( ");

			builder.append("SELECT INFO.FILE_NUMBER FROM ");
			builder.append("(SELECT PROINST.FILE_NUMBER,ACTINST.STAFF_NAME,MIN(ACTINST.ACTINST_END) ACTINST_END ");
			builder.append("FROM BDC_WORKFLOW.WFI_PROINST PROINST ");
			builder.append("LEFT JOIN BDC_WORKFLOW.WFI_ACTINST ACTINST ON ACTINST.PROINST_ID=PROINST.PROINST_ID ");
			builder.append("WHERE ACTINST.ACTINST_NAME LIKE '%收费%' AND ACTINST.ACTINST_STATUS=3 AND ACTINST.OPERATION_TYPE NOT IN ('9','30') ");
			builder.append("GROUP BY PROINST.FILE_NUMBER,ACTINST.STAFF_NAME) INFO ");
			builder.append("WHERE 1>0");
			if (!StringHelper.isEmpty(sfry) && !"ALL".equals(sfry)) {

				String str = StringHelper.formatList(
						Arrays.asList(sfry.split(",")), "','");
				builder.append("AND INFO.STAFF_NAME IN ('" + str + "')");
			}
			if (!StringHelper.isEmpty(tjsjks)) {
				builder.append("AND INFO.ACTINST_END>=TO_DATE('" + tjsjks
						+ " 00:00:00" + "','YYYY-MM-DD HH24:MI:SS') ");
			}
			if (!StringHelper.isEmpty(tjsjjz)) {
				builder.append("AND INFO.ACTINST_END<=TO_DATE('" + tjsjjz
						+ " 23:59:59" + "','YYYY-MM-DD HH24:MI:SS') ");
			}
			builder.append(") ");

			if (!StringHelper.isEmpty(sfbmmc) && !"ALL".equals(sfbmmc)) {
				builder.append(" AND T.SFBMMC='" + sfbmmc + "' ");
			}

			builder.append("GROUP BY T.SFBMMC,CONCAT(Y.SFXLMC, Y.SFKMMC),T.SFLX,T.SFDYID ORDER BY T.SFBMMC,CONCAT(Y.SFXLMC, Y.SFKMMC),T.SFLX");
			List<Map> maps = baseCommonDao.getDataListByFullSql(builder
					.toString());
			String result_sfbmmc_all = "全部";
			Double result_hjysje_all = 0.0;
			Double result_hjssje_all = 0.0;
			DecimalFormat df = new DecimalFormat("#.####");
			df.setRoundingMode(RoundingMode.HALF_UP);
			HashMap<String, Integer> list_sfbmmc = new HashMap<String, Integer>();
			List<Map> result_maps = new ArrayList<Map>();
			if (maps != null && maps.size() > 0) {
				String result_sfbmmc = "";
				Double result_hjysje = 0.0;
				Double result_hjssje = 0.0;
				for (int i = 0; i < maps.size(); i++) {
					Map map = maps.get(i);
					Double ysje = 0.0, ssje = 0.0;
					String m_sfbmmc = "";
					if (map.get("YSJE") != null) {
						ysje = StringHelper.getDouble(map.get("YSJE"));
					}
					if (map.get("SSJE") != null) {
						ssje = StringHelper.getDouble(map.get("SSJE"));
					}
					if (map.get("SFBMMC") != null) {
						m_sfbmmc = StringHelper.formatObject(map.get("SFBMMC"));
					}
					if (StringHelper.isEmpty(m_sfbmmc)) {
						m_sfbmmc = "未设置部门";
					}
					String sfmc = "";
					if (map.get("SFMC") != null) {
						sfmc = StringHelper.formatObject(map.get("SFMC"));
					}
					map.put("CRMJ", "");
					if (!StringHelper.isEmpty(sfmc) && sfmc.contains("出让")) {
						String sfdyid = StringHelper.formatObject(map
								.get("SFDYID"));
						if (!StringHelper.isEmpty(sfdyid)) {
							StringBuilder builder_cr = new StringBuilder();
							builder_cr
									.append("SELECT NVL(SUM(NVL(MJ,0)),0) CRMJ FROM ");
							builder_cr.append("BDCK.BDCS_DJDY_GZ DJDY ");
							builder_cr.append("LEFT JOIN ");
							builder_cr
									.append("(SELECT '031' BDCDYLX,'01' LY,BDCDYID,NVL(FTTDMJ,0) MJ FROM BDCK.BDCS_H_GZ ");
							builder_cr.append("UNION ");
							builder_cr
									.append("SELECT '031' BDCDYLX,'02' LY,BDCDYID,NVL(FTTDMJ,0) MJ FROM BDCK.BDCS_H_XZ ");
							builder_cr.append("UNION ");
							builder_cr
									.append("SELECT '031' BDCDYLX,'03' LY,BDCDYID,NVL(FTTDMJ,0) MJ FROM BDCK.BDCS_H_LS ");
							builder_cr.append("UNION ");
							builder_cr
									.append("SELECT '02' BDCDYLX,'01' LY,BDCDYID,NVL(ZDMJ,0) MJ FROM BDCK.BDCS_SHYQZD_GZ ");
							builder_cr.append("UNION ");
							builder_cr
									.append("SELECT '02' BDCDYLX,'02' LY,BDCDYID,NVL(ZDMJ,0) MJ FROM BDCK.BDCS_SHYQZD_XZ ");
							builder_cr.append("UNION ");
							builder_cr
									.append("SELECT '02' BDCDYLX,'03' LY,BDCDYID,NVL(ZDMJ,0) MJ FROM BDCK.BDCS_SHYQZD_LS ");
							builder_cr
									.append(") DY ON DY.BDCDYLX=DJDY.BDCDYLX AND DY.BDCDYID=DJDY.BDCDYID AND DY.LY=DJDY.LY ");
							builder_cr.append("WHERE DJDY.XMBH IN ");
							builder_cr
									.append("(SELECT DJSF.XMBH FROM BDCK.BDCS_DJSF DJSF ");
							builder_cr
									.append("LEFT JOIN BDC_WORKFLOW.WFI_PROINST PROINST ON DJSF.YWH=PROINST.FILE_NUMBER ");
							builder_cr
									.append("LEFT JOIN BDC_WORKFLOW.WFI_ACTINST ACTINST ON ACTINST.PROINST_ID=PROINST.PROINST_ID ");
							builder_cr
									.append("WHERE ACTINST.ACTINST_NAME LIKE '%收费%' AND ACTINST.ACTINST_STATUS=3 AND ACTINST.OPERATION_TYPE NOT IN ('9','30') ");
							builder_cr.append("AND DJSF.SFDYID='" + sfdyid
									+ "' AND DJSF.SSJE IS NOT NULL ");
							if (!StringHelper.isEmpty(sfry)
									&& !"ALL".equals(sfry)) {
								String str = StringHelper.formatList(
										Arrays.asList(sfry.split(",")), "','");
								builder_cr
										.append("AND ACTINST.STAFF_NAME IN ('"
												+ str + "')");
							}
							if (!StringHelper.isEmpty(tjsjks)) {
								builder_cr
										.append("AND ACTINST.ACTINST_END>=TO_DATE('"
												+ tjsjks
												+ " 00:00:00"
												+ "','YYYY-MM-DD HH24:MI:SS') ");
							}
							if (!StringHelper.isEmpty(tjsjjz)) {
								builder_cr
										.append("AND ACTINST.ACTINST_END<=TO_DATE('"
												+ tjsjjz
												+ " 23:59:59"
												+ "','YYYY-MM-DD HH24:MI:SS') ");
							}
							builder_cr.append(") ");

							List<Map> maps_cr = baseCommonDao
									.getDataListByFullSql(builder_cr.toString());
							if (maps_cr != null && maps_cr.size() > 0) {
								String crmj = StringHelper
										.formatDouble(StringHelper
												.getDouble(maps_cr.get(0).get(
														"CRMJ")));
								map.put("CRMJ", crmj);
							}
						}
					}

					if (list_sfbmmc.containsKey(m_sfbmmc)) {
						list_sfbmmc
								.put(m_sfbmmc, list_sfbmmc.get(m_sfbmmc) + 1);
					} else {
						list_sfbmmc.put(m_sfbmmc, 1);
					}
					result_hjysje_all += ysje;
					result_hjssje_all += ssje;

					if (m_sfbmmc.equals(result_sfbmmc) || i == 0) {
						result_sfbmmc = m_sfbmmc;
						result_hjysje += ysje;
						result_hjssje += ssje;
					} else {
						HashMap<String, Object> hj_m = new HashMap<String, Object>();
						hj_m.put("SFBMMC", result_sfbmmc);
						hj_m.put("SSJE", StringHelper.getDouble(df
								.format(result_hjssje)));
						hj_m.put("YSJE", StringHelper.getDouble(df
								.format(result_hjysje)));
						hj_m.put("SFMC", "合计");
						hj_m.put("SFLX", "");
						hj_m.put("CRMJ", "");
						result_maps.add(hj_m);
						result_sfbmmc = m_sfbmmc;
						result_hjysje = ysje;
						result_hjssje = ssje;
					}
					result_maps.add(map);
					if (i == maps.size() - 1) {
						HashMap<String, Object> hj_m = new HashMap<String, Object>();
						hj_m.put("SFBMMC", result_sfbmmc);
						hj_m.put("SSJE", StringHelper.getDouble(df
								.format(result_hjssje)));
						hj_m.put("YSJE", StringHelper.getDouble(df
								.format(result_hjysje)));
						hj_m.put("SFMC", "合计");
						hj_m.put("SFLX", "");
						hj_m.put("CRMJ", "");
						result_maps.add(hj_m);
					}
				}
			}
			HashMap<String, Object> hj_m_all = new HashMap<String, Object>();
			hj_m_all.put("SFBMMC", result_sfbmmc_all);
			hj_m_all.put("SSJE",
					StringHelper.getDouble(df.format(result_hjssje_all)));
			hj_m_all.put("YSJE",
					StringHelper.getDouble(df.format(result_hjysje_all)));
			hj_m_all.put("SFMC", "合计");
			hj_m_all.put("SFLX", "");
			hj_m_all.put("CRMJ", "");
			result_maps.add(hj_m_all);
			if (result_maps != null && result_maps.size() > 0) {
				String mmm_sfbmmc = "";
				for (Map mmm : result_maps) {
					String mmmm_sfbmmc = StringHelper.formatObject(mmm
							.get("SFBMMC"));
					if (!mmmm_sfbmmc.equals(mmm_sfbmmc)
							&& !"全部".equals(mmmm_sfbmmc)) {
						mmm.put("ROWSPAN", list_sfbmmc.get(mmmm_sfbmmc) + 1);
						mmm_sfbmmc = mmmm_sfbmmc;
					}
				}
			}
			m.setRows(result_maps);
			m.setTotal(maps.size());
		} catch (Exception ex) {
			m = null;
		}
		return m;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Message getSFMXTJ(String tjsjks, String tjsjjz, String slry,
			String sfbmmc, String sflx, String sfry,String sfdl) {
		StringBuilder builder = new StringBuilder();
		Message m = new Message();
		try {
			builder.append("SELECT PROINST.PROINST_ID,PROINST.PROLSH,SFDY.SFBMMC,(SFDY.SFXLMC||'-'||SFDY.SFKMMC) SFLX,");
			builder.append("(CASE WHEN DJSF.TS IS NOT NULL THEN DJSF.TS ELSE 1 END) GS,");
			builder.append("DJSF.YSJE,DJSF.SSJE,PROINST.ACCEPTOR,");
			builder.append("(SELECT TO_CHAR(WM_CONCAT(TO_CHAR(SQRXM))) FROM BDCK.BDCS_SQR WHERE SQRLB='1' AND XMBH=DJSF.XMBH) QLRMC,");
			builder.append("(SELECT TO_CHAR(WM_CONCAT(TO_CHAR(SQRXM))) FROM BDCK.BDCS_SQR WHERE SQRLB='2' AND XMBH=DJSF.XMBH) YWRMC,");
			builder.append("(SELECT TO_CHAR(WM_CONCAT(DISTINCT TO_CHAR(STAFF_NAME))) FROM BDC_WORKFLOW.WFI_ACTINST WHERE  ACTINST_NAME LIKE '%收费%' AND ACTINST_STATUS=3 AND PROINST_ID=PROINST.PROINST_ID) SFRY ");
			builder.append("FROM BDCK.BDCS_DJSF DJSF ");
			builder.append("LEFT JOIN BDCK.BDCS_SFDY SFDY ON DJSF.SFDYID=SFDY.ID ");
			builder.append("LEFT JOIN BDC_WORKFLOW.WFI_PROINST PROINST ON PROINST.FILE_NUMBER=DJSF.YWH ");
			builder.append("WHERE 1>0 AND SFDY.TJBZ<>0 ");
			if (!StringHelper.isEmpty(sfbmmc)) {
				String str_sfbmmc = StringHelper.formatList(
						Arrays.asList(sfbmmc.split(",")), "','");
				builder.append(" AND SFDY.SFBMMC IN ('" + str_sfbmmc + "') ");
			}
			if (!StringHelper.isEmpty(sflx)) {
				String str_sflx = StringHelper.formatList(
						Arrays.asList(sflx.split(",")), "','");
				builder.append(" AND SFDY.ID IN ('" + str_sflx + "') ");
			}
			if (!StringHelper.isEmpty(slry)) {
				String str_slry = StringHelper.formatList(
						Arrays.asList(slry.split(",")), "','");
				builder.append("AND PROINST.ACCEPTOR IN ('" + str_slry + "')");
			}
			if (!StringHelper.isEmpty(tjsjks) || !StringHelper.isEmpty(tjsjjz)
					|| !StringHelper.isEmpty(sfry)) {
				builder.append("AND PROINST.PROINST_ID IN( ");
				builder.append("SELECT PROINST_ID FROM BDC_WORKFLOW.WFI_ACTINST AC ");
				builder.append("WHERE  ACTINST_NAME LIKE '%收费%' AND ACTINST_STATUS=3 ");
				if (!StringHelper.isEmpty(sfry)) {
					String str_sfry = StringHelper.formatList(
							Arrays.asList(sfry.split(",")), "','");
					builder.append("AND AC.STAFF_NAME IN ('" + str_sfry + "') ");
				}
				if (!StringHelper.isEmpty(tjsjks)) {
					builder.append("AND AC.ACTINST_END>=TO_DATE('" + tjsjks
							+ " 00:00:00" + "','YYYY-MM-DD HH24:MI:SS') ");
				}
				if (!StringHelper.isEmpty(tjsjjz)) {
					builder.append("AND AC.ACTINST_END<=TO_DATE('" + tjsjjz
							+ " 23:59:59" + "','YYYY-MM-DD HH24:MI:SS') ");
				}
				builder.append(")");
			}
			if(sfdl!=null){// wlb
				if(sfdl.equals("住房")){
					builder.append(" AND (SFDY.SFXLMC || '-' || SFDY.SFKMMC) like'%"+sfdl+"%' AND (SFDY.SFXLMC || '-' || SFDY.SFKMMC) not like'%非%'");
				}else{
					builder.append(" AND (SFDY.SFXLMC || '-' || SFDY.SFKMMC) like'%"+sfdl+"%'");
				}
				
			}
			builder.append("ORDER BY PROINST.PROLSH,SFDY.SFBMMC,SFDY.SFXLMC");
		
			List<Map> maps = baseCommonDao.getDataListByFullSql(builder
					.toString());
			Date d_qsj = null;
			if (!StringHelper.isEmpty(tjsjks)) {
				try {
					d_qsj = StringHelper.FormatByDate(tjsjks + " 00:00:00",
							"yyyy-MM-dd HH:mm:ss");
				} catch (Exception e) {
				}
			}
			Date d_zsj = null;
			if (!StringHelper.isEmpty(tjsjjz)) {
				try {
					d_zsj = StringHelper.FormatByDate(tjsjjz + " 23:59:59",
							"yyyy-MM-dd HH:mm:ss");
				} catch (Exception e) {
				}
			}
			List<Map> result_maps = new ArrayList<Map>();
			Double result_hjysje_all = 0.0;
			Double result_hjssje_all = 0.0;
			DecimalFormat df = new DecimalFormat("#.####");
			df.setRoundingMode(RoundingMode.HALF_UP);
			if (maps != null && maps.size() > 0) {
				for (int i = 0; i < maps.size(); i++) {
					Map map = maps.get(i);
					String proinst_id = StringHelper.formatObject(map
							.get("PROINST_ID"));
					List<Wfi_ActInst> list_actinst = baseCommonDao
							.getDataList(
									Wfi_ActInst.class,
									"ACTINST_NAME LIKE '%收费%' AND ACTINST_STATUS=3 AND OPERATION_TYPE NOT IN ('9','30') AND PROINST_ID='"
											+ proinst_id
											+ "' ORDER BY ACTINST_END");
					if (list_actinst == null || list_actinst.size() <= 0) {
						continue;
					}
					Wfi_ActInst actinst = list_actinst.get(0);
					if (!StringHelper.isEmpty(sfry)
							&& (!Arrays.asList(sfry.split(",")).contains(
									actinst.getStaff_Name()))) {
						continue;
					}
					if (StringHelper.isEmpty(actinst.getActinst_End())) {
						continue;
					}
					if (!StringHelper.isEmpty(d_qsj)
							&& d_qsj.after(actinst.getActinst_End())) {
						continue;
					}
					if (!StringHelper.isEmpty(d_zsj)
							&& d_zsj.before(actinst.getActinst_End())) {
						continue;
					}
					Double ysje = 0.0, ssje = 0.0;
					if (map.get("YSJE") != null) {
						ysje = StringHelper.getDouble(map.get("YSJE"));
					}
					if (map.get("SSJE") != null) {
						ssje = StringHelper.getDouble(map.get("SSJE"));
					}
					result_hjysje_all += ysje;
					result_hjssje_all += ssje;
					result_maps.add(map);
				}
			}
			HashMap<String, Object> hj_m_all = new HashMap<String, Object>();
			hj_m_all.put("PROLSH", "合计");
			hj_m_all.put("SSJE",
					StringHelper.getDouble(df.format(result_hjssje_all)));
			hj_m_all.put("YSJE",
					StringHelper.getDouble(df.format(result_hjysje_all)));
			hj_m_all.put("SFBMMC", "");
			hj_m_all.put("SFLX", "");
			hj_m_all.put("GS", "");
			hj_m_all.put("ACCEPTOR", "");
			hj_m_all.put("SFRY", "");
			hj_m_all.put("QLRMC", "");
			hj_m_all.put("YWRMC", "");
			result_maps.add(hj_m_all);
			m.setRows(result_maps);
			m.setTotal(result_maps.size());
		} catch (Exception ex) {
			m = null;
		}
		return m;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> getSFRYList() {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT DISTINCT STAFF_NAME VALUE,STAFF_NAME TEXT FROM BDC_WORKFLOW.WFI_ACTINST AC ");
		builder.append("WHERE  ACTINST_NAME LIKE '%收费%' AND ACTINST_STATUS=3 AND STAFF_NAME IS NOT NULL ORDER BY STAFF_NAME");
		List<Map> maps = baseCommonDao.getDataListByFullSql(builder.toString());
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("VALUE", "ALL");
		map.put("TEXT", "全部");

		maps.add(0, map);
		return maps;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> getSLRYList() {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT DISTINCT ACCEPTOR VALUE,ACCEPTOR TEXT FROM BDC_WORKFLOW.WFI_PROINST ");
		builder.append("WHERE ACCEPTOR IS NOT NULL ORDER BY ACCEPTOR");
		List<Map> maps = baseCommonDao.getDataListByFullSql(builder.toString());
		return maps;
	}

	@Override
	public List<HashMap<String, Object>> getSFDYList(String sfbmmc) {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		List<BDCS_SFDY> sfdylist = null;
		if (StringHelper.isEmpty(sfbmmc) || "ALL".endsWith(sfbmmc)) {
			sfdylist = baseCommonDao.getDataList(BDCS_SFDY.class,
					" 1>0 AND TJBZ<>0 ORDER BY SFXLMC,SFKMMC");
		} else {
			String str_sfbmmc = StringHelper.formatList(
					Arrays.asList(sfbmmc.split(",")), "','");
			sfdylist = baseCommonDao.getDataList(BDCS_SFDY.class,
					" SFBMMC IN ('" + str_sfbmmc + "') AND TJBZ<>0 ORDER BY SFXLMC,SFKMMC");
		}
		if (sfdylist != null && sfdylist.size() > 0) {
			String sfxlmc = "";
			List<HashMap<String, Object>> list_sfkmmc = new ArrayList<HashMap<String, Object>>();
			int i = 0;
			for (BDCS_SFDY sfdy : sfdylist) {
				i++;
				if (!sfxlmc.equals(sfdy.getSFXLMC())) {
					if (list_sfkmmc != null && list_sfkmmc.size() > 0) {
						HashMap<String, Object> m1 = new HashMap<String, Object>();
						m1.put("id", "sfxlmc" + i);
						m1.put("text", sfxlmc);
						m1.put("checked", false);
						m1.put("children", list_sfkmmc);
						list.add(m1);
					}
					sfxlmc = sfdy.getSFXLMC();
					list_sfkmmc = new ArrayList<HashMap<String, Object>>();
				}
				HashMap<String, Object> m = new HashMap<String, Object>();
				m.put("id", sfdy.getId());
				if (StringHelper.isEmpty(sfdy.getSFKMMC())) {
					m.put("text", sfxlmc);
				} else {
					m.put("text", sfdy.getSFKMMC());
				}
				m.put("checked", false);
				list_sfkmmc.add(m);
				if (i == sfdylist.size()) {
					if (list_sfkmmc != null && list_sfkmmc.size() > 0) {
						HashMap<String, Object> m1 = new HashMap<String, Object>();
						m1.put("id", "sfxlmc" + i + 1);
						m1.put("text", sfxlmc);
						m1.put("checked", false);
						m1.put("children", list_sfkmmc);
						list.add(m1);
					}
					sfxlmc = sfdy.getSFXLMC();
					list_sfkmmc = new ArrayList<HashMap<String, Object>>();
				}
			}
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> getSFBMMCList() {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT DISTINCT SFBMMC VALUE,SFBMMC TEXT FROM BDCK.BDCS_SFDY ");
		builder.append("WHERE SFBMMC IS NOT NULL ORDER BY SFBMMC");
		List<Map> maps = baseCommonDao.getDataListByFullSql(builder.toString());
		return maps;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Map> getDepartmentlistList() {
		List<Map> list = new ArrayList<Map>();
		List<Department> department_list = baseCommonDao.getDataList(
				Department.class, " PARENTID IS NOT NULL ORDER BY AREACODE");
		if (department_list != null && department_list.size() > 0) {
			for (Department department : department_list) {
				Map m = new HashMap<String, String>();
				m.put("id", department.getId());
				m.put("text", department.getDepartmentName());
				list.add(m);
			}
		}
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Map> getRoleList() {
		List<Map> list = new ArrayList<Map>();
		List<Role> role_list = baseCommonDao.getDataList(Role.class,
				" ROLETYPE='YB' ORDER BY AREACODE");
		if (role_list != null && role_list.size() > 0) {
			for (Role role : role_list) {
				Map m = new HashMap<String, String>();
				m.put("id", role.getId());
				m.put("text", role.getRoleName());
				list.add(m);
			}
		}
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Map> getUserList(HttpServletRequest request) {
		String departmentid = "";
		try {
			departmentid = RequestHelper.getParam(request, "departmentid");
		} catch (Exception e) {
		}
		List<Map> list = new ArrayList<Map>();
		List<User> user_list = baseCommonDao.getDataList(User.class,
				"DEPARTMENTID='" + departmentid + "'");
		if (user_list != null && user_list.size() > 0) {
			Map m1 = new HashMap<String, String>();
			m1.put("id", "");
			m1.put("text", "    ");
			list.add(m1);
			for (User user : user_list) {
				Map m = new HashMap<String, String>();
				m.put("id", user.getId());
				m.put("text", user.getUserName());
				list.add(m);
			}
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Message getBJMXTJ(String qsj, String zsj, String departmentid,
			String userid, String isdyinfo) {
		Message msg = new Message();
		List<Map> list = new ArrayList<Map>();
		msg.setSuccess("false");
		msg.setTotal(list.size());
		msg.setRows(list);
		if (!StringHelper.isEmpty(qsj)) {
			qsj = qsj + " 00:00:00";
		}
		if (!StringHelper.isEmpty(zsj)) {
			zsj = zsj + " 23:59:59";
		}
		HashMap<String, String> user_id_name = new HashMap<String, String>();
		List<String> list_userid = new ArrayList<String>();
		if (!StringHelper.isEmpty(userid)) {
			list_userid.add(userid);
			User user = baseCommonDao.get(User.class, userid);
			if (user != null) {
				user_id_name.put(userid, user.getUserName());
			}
		} else if (!StringHelper.isEmpty(departmentid)) {
			List<User> user_list = baseCommonDao.getDataList(User.class,
					"DEPARTMENTID='" + departmentid + "'");
			if (user_list != null && user_list.size() > 0) {
				for (User user : user_list) {
					String str_userid = user.getId();
					String str_username = user.getUserName();
					list_userid.add(str_userid);
					user_id_name.put(str_userid, str_username);
				}
			} else {
				return msg;
			}
		} else {
			List<User> list_user = baseCommonDao
					.getDataList(User.class, " 1>0");
			if (list_user != null && list_user.size() > 0) {
				for (User user : list_user) {
					String str_userid = user.getId();
					String str_username = user.getUserName();
					user_id_name.put(str_userid, str_username);
				}
			}
		}

		StringBuilder builder = new StringBuilder();
		builder.append("SELECT XMXX.DJLX,PROINST.PROINST_ID,PROINST.PROLSH,PROINST.FILE_NUMBER,TO_CHAR(PROINST.PROINST_END,'YYYY-MM-DD HH24:MI:SS') PROINST_END,");
		builder.append("ACTINST.STAFF_ID,TO_CHAR(ACTINST.ACTINST_START,'YYYY-MM-DD HH24:MI:SS') ACTINST_START,ACTINST.ACTINST_ID,ACTINST.ACTDEF_ID ");
		builder.append("FROM BDC_WORKFLOW.WFI_PROINST PROINST ");
		builder.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON XMXX.PROJECT_ID=PROINST.FILE_NUMBER ");
		builder.append("LEFT JOIN BDC_WORKFLOW.WFI_ACTINST ACTINST ON PROINST.PROINST_ID=ACTINST.PROINST_ID ");
		builder.append("WHERE EXISTS(SELECT 1 FROM BDC_WORKFLOW.WFI_ACTINST ");
		builder.append("WHERE PROINST.PROINST_ID=PROINST_ID ");
		if (!StringHelper.isEmpty(qsj)) {
			builder.append("AND ACTINST_START>=TO_DATE('" + qsj
					+ "','YYYY-MM-DD HH24:MI:SS') ");
		}
		if (!StringHelper.isEmpty(zsj)) {
			builder.append("AND ACTINST_START<=TO_DATE('" + zsj
					+ "','YYYY-MM-DD HH24:MI:SS') ");
		}

		if (list_userid != null && list_userid.size() > 0) {
			String str_userids = StringHelper.formatList(list_userid, "','");
			builder.append("AND STAFF_ID IN ('" + str_userids + "')");
		}
		builder.append(")");
		builder.append("ORDER BY PROINST.PROINST_ID,ACTINST.STAFF_ID,ACTINST.ACTINST_START");
		List<Map> list_proinst = baseCommonDao.getDataListByFullSql(builder
				.toString());
		if (list_proinst == null || list_proinst.size() <= 0) {
			return msg;
		}
		Date d_qsj = null;
		if (!StringHelper.isEmpty(qsj)) {
			try {
				d_qsj = StringHelper.FormatByDate(qsj, "yyyy-MM-dd HH:mm:ss");
			} catch (Exception e) {
			}
		}
		Date d_zsj = null;
		if (!StringHelper.isEmpty(zsj)) {
			try {
				d_zsj = StringHelper.FormatByDate(zsj, "yyyy-MM-dd HH:mm:ss");
			} catch (Exception e) {
			}
		}

		int xh = 1;
		List<String> list_proinst_staff = new ArrayList<String>();
		String str_split = SuperHelper.GeneratePrimaryKey();
		HashMap<String, List<String>> proinst_actinst = new HashMap<String, List<String>>();
		for (Map proinst : list_proinst) {
			String proinst_id = StringHelper.formatObject(proinst
					.get("PROINST_ID"));
			String actdef_id = StringHelper.formatObject(proinst
					.get("ACTDEF_ID"));
			if (proinst_actinst.containsKey(proinst_id)) {
				List<String> list_actdef = proinst_actinst.get(proinst_id);
				if (!list_actdef.contains(actdef_id)) {
					list_actdef.add(actdef_id);
				}
				proinst_actinst.put(proinst_id, list_actdef);
			} else {
				List<String> list_actdef = new ArrayList<String>();
				list_actdef.add(actdef_id);
				proinst_actinst.put(proinst_id, list_actdef);
			}
		}
		HashMap<String, HashMap<String, String>> proinst_dyinfo = new HashMap<String, HashMap<String, String>>();
		for (Map proinst : list_proinst) {
			String djlx = StringHelper.formatObject(proinst.get("DJLX"));
			String proinst_id = StringHelper.formatObject(proinst
					.get("PROINST_ID"));
			String prolsh = StringHelper.formatObject(proinst.get("PROLSH"));
			String file_number = StringHelper.formatObject(proinst
					.get("FILE_NUMBER"));
			Date proinst_end = null;
			if (!StringHelper.isEmpty(proinst.get("PROINST_END"))) {
				try {
					proinst_end = StringHelper.FormatByDate(
							proinst.get("PROINST_END"), "yyyy-MM-dd HH:mm:ss");
				} catch (Exception e) {
				}
			}
			String staff_id = StringHelper
					.formatObject(proinst.get("STAFF_ID"));
			Date actinst_start = null;
			if (!StringHelper.isEmpty(proinst.get("ACTINST_START"))) {
				try {
					actinst_start = StringHelper
							.FormatByDate(proinst.get("ACTINST_START"),
									"yyyy-MM-dd HH:mm:ss");
				} catch (Exception e) {
				}
			}
			String str_actinst_start = StringHelper.FormatDateOnType(
					actinst_start, "yyyy-MM-dd HH:mm:ss");
			// String
			// actinst_id=StringHelper.formatObject(proinst.get("ACTINST_ID"));
			String actdef_id = StringHelper.formatObject(proinst
					.get("ACTDEF_ID"));
			String proinst_staff = proinst_id + "&" + str_split + "&"
					+ staff_id;
			if (!user_id_name.containsKey(staff_id)) {
				continue;
			}
			if (list_proinst_staff.contains(proinst_staff)) {
				continue;
			}
			if (!StringHelper.isEmpty(d_qsj) && d_qsj.after(actinst_start)) {
				list_proinst_staff.add(proinst_staff);
				continue;
			}
			if (!StringHelper.isEmpty(d_zsj) && d_zsj.before(actinst_start)) {
				list_proinst_staff.add(proinst_staff);
				continue;
			}
			HashMap<String, Object> m = new HashMap<String, Object>();
			m.put("djlx", ConstHelper.getNameByValue("DJLX", djlx));
			m.put("xh", xh);
			m.put("ywh", prolsh);
			m.put("blry", "");
			if (user_id_name.containsKey(staff_id)) {
				m.put("blry", user_id_name.get(staff_id));
			}
			m.put("blsj", str_actinst_start);
			m.put("sfzc", "0");
			if ("1".equals(isdyinfo)) {
				HashMap<String, String> dyinfo = new HashMap<String, String>();
				if (proinst_dyinfo.containsKey(proinst_id)) {
					dyinfo = proinst_dyinfo.get(proinst_id);
				} else {
					dyinfo = getDyInfo(file_number);
					proinst_dyinfo.put(proinst_id, dyinfo);
				}
				m.put("dyh", dyinfo.get("dyh"));
				m.put("sfgl", dyinfo.get("sfgl"));
			}
			m.put("sfzc", "0");
			if (proinst_end != null) {
				m.put("sfzc", "1");
			} else {
				List<Wfd_Route> list_route = baseCommonDao.getDataList(
						Wfd_Route.class, " ACTDEF_ID='" + actdef_id + "'");
				if (list_route != null && list_route.size() > 0) {
					if (proinst_actinst.containsKey(proinst_id)
							&& proinst_actinst.get(proinst_id).contains(
									list_route.get(0).getNext_Actdef_Id())) {
						m.put("sfzc", "1");
					}
				}
			}
			list.add(m);
			list_proinst_staff.add(proinst_staff);
			xh++;
		}
		msg.setSuccess("true");
		msg.setTotal(list.size());
		msg.setRows(list);
		return msg;
	}

	@SuppressWarnings("rawtypes")
	private HashMap<String, String> getDyInfo(String file_number) {
		HashMap<String, String> m = new HashMap<String, String>();
		StringBuilder builder_djdy = new StringBuilder();
		builder_djdy.append("SELECT DJDY.BDCDYLX,DJDY.LY,DJDY.BDCDYID ");
		builder_djdy.append("FROM BDCK.BDCS_XMXX XMXX ");
		builder_djdy
				.append("LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON DJDY.XMBH=XMXX.XMBH ");
		builder_djdy.append("WHERE XMXX.PROJECT_ID='");
		builder_djdy.append(file_number);
		builder_djdy.append("'");
		List<Map> list_djdy = baseCommonDao.getDataListByFullSql(builder_djdy
				.toString());
		m.put("dyh", "");
		m.put("sfgl", "");
		if (list_djdy != null && list_djdy.size() > 0) {
			List<String> list_bdcdyh = new ArrayList<String>();
			String sfgl = "2";
			for (Map djdy : list_djdy) {
				String bdcdyid = StringHelper.formatObject(djdy.get("BDCDYID"));
				String bdcdylx = StringHelper.formatObject(djdy.get("BDCDYLX"));
				String ly = StringHelper.formatObject(djdy.get("LY"));
				DJDYLY t_ly = DJDYLY.XZ;
				if (!StringHelper.isEmpty(ly)) {
					t_ly = DJDYLY.initFrom(ly);
				}
				RealUnit unit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx),
						t_ly, bdcdyid);
				if (unit != null) {
					if (StringHelper.isEmpty(unit.getBDCDYH())) {
						sfgl = "1";
					}
					if (!StringHelper.isEmpty(unit.getBDCDYH())
							&& !list_bdcdyh.contains(unit.getBDCDYH())) {
						list_bdcdyh.add(unit.getBDCDYH());
					}
				}
			}
			if (list_bdcdyh == null || list_bdcdyh.size() <= 0) {
				sfgl = "0";
				m.put("dyh", "");
			} else {
				if (list_bdcdyh.size() < 3) {
					m.put("dyh", StringHelper.formatList(list_bdcdyh, "、"));
				} else {
					m.put("dyh",
							StringHelper.formatList(list_bdcdyh.subList(0, 2),
									"、") + "等");
				}
			}
			m.put("sfgl", sfgl);
		}
		return m;
	}

	@Override
	public Message getCertificateStatistics(String SZSJ_Q, String SZSJ_Z, String SZRY, String SearchStates) {
			StringBuilder sqlBuilder = new StringBuilder();              
			sqlBuilder.append("SELECT SZR, SUM(CASE WHEN QZLX='0' THEN 1 ELSE 0 END) ZSGS,SUM(CASE WHEN QZLX='1' THEN 1 ELSE 0 END) ZMGS, COUNT(QZB.ID) AS ZGS ")
						.append(" FROM BDCK.BDCS_RKQZB QZB WHERE SFSZ='1' ");			
			
			StringBuilder sqlBuilder_fixedValue = new StringBuilder();			        
			sqlBuilder_fixedValue.append("SELECT CREATOR,SUM(CASE WHEN FIXEDLX='djsztj-zs' THEN (TO_NUMBER(FIXEDCONTEXT)) ELSE 0 END) ZSGS,SUM(CASE WHEN FIXEDLX='djsztj-zm' THEN (TO_NUMBER(FIXEDCONTEXT)) ELSE 0 END) ZMGS FROM BDCK.RESULT_FIXED T WHERE FIXEDLX LIKE 'djsztj%' ");
			
			if (!StringHelper.isEmpty(SZSJ_Q)) {				
				sqlBuilder.append(" AND TO_CHAR(QZB.SZSJ, 'yyyy-MM-DD') >= '").append(SZSJ_Q).append("' ");
				sqlBuilder_fixedValue.append(" AND TO_CHAR(FIXEDSTARTTIME, 'yyyy-MM-DD') >= '").append(SZSJ_Q).append("' ");

			}	
			if (!StringHelper.isEmpty(SZSJ_Z)) {				
				sqlBuilder.append(" AND TO_CHAR(QZB.SZSJ, 'yyyy-MM-DD') <= '").append(SZSJ_Z).append("' ");
				sqlBuilder_fixedValue.append(" AND TO_CHAR(FIXEDENDTIME, 'yyyy-MM-DD') <= '").append(SZSJ_Z).append("' ");
			}
//			if (!StringHelper.isEmpty(SZRY)) {				
//				sqlBuilder.append("AND SZR= '").append(SZRY).append("' ");
//			}
			if (!StringHelper.isEmpty(SearchStates)) {
				String[] states = SearchStates.split("-");
				String state = "";
				for (int i = 0; i < states.length; i++) {
					state += "'"+states[i]+"',";
				}
				state = state.substring(0, state.length()-1);
				String  statesql = " WHERE SEARCHSTATE in (" + state + ")  ";
				sqlBuilder.append(" AND SLBH IN (SELECT distinct XMXX.YWLSH ")
				.append(" FROM BDCK.BDCS_XMXX XMXX LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON DJDY.XMBH = XMXX.XMBH")
				.append(" WHERE BDCDYID IN(")
				.append(" SELECT BDCDYID FROM BDCK.BDCS_H_XZ").append(statesql)
				.append(" UNION ALL")
				.append(" SELECT BDCDYID FROM BDCK.BDCS_H_XZY").append(statesql)
				.append(" UNION ALL")
				.append(" SELECT BDCDYID FROM BDCK.BDCS_SYQZD_XZ").append(statesql)
				.append(" UNION ALL")
				.append(" SELECT BDCDYID FROM BDCK.BDCS_SHYQZD_XZ").append(statesql).append("))");
			}
			sqlBuilder.append(" GROUP BY SZR ORDER BY SZR ");
			List<Map> list_SZTJ = baseCommonDao.getDataListByFullSql(sqlBuilder.toString());
			sqlBuilder_fixedValue.append(" GROUP BY CREATOR");
			List<Map> list_fixValue = baseCommonDao.getDataListByFullSql(sqlBuilder_fixedValue.toString());
			int zzs = 0,zzm = 0,all = 0;
			int zzs_fixed = 0,zzm_fixed = 0,all_fixed = 0;
			List<Map> list_newfixedMap = new ArrayList();
			List<String> lstSZR = new ArrayList<String>();
			
			if (list_SZTJ!=null && list_SZTJ.size()>0) {
				for(Map szMap :list_SZTJ){	
					if(!lstSZR.contains(szMap.get("SZR")) && !StringHelper.isEmpty(szMap.get("SZR"))){
						lstSZR.add(szMap.get("SZR").toString());								
					}
				}
			
				for(Map szMap :list_SZTJ){				
					//求总和
					zzs += Integer.parseInt(szMap.get("ZSGS").toString());
					zzm += Integer.parseInt(szMap.get("ZMGS").toString());
					all += Integer.parseInt(szMap.get("ZGS").toString());
						
					//添加基准值
					for(Map fixedValue :list_fixValue){					
						if (!StringHelper.isEmpty(szMap.get("SZR")) && !StringHelper.isEmpty(fixedValue.get("CREATOR"))) {
							int fzs = 0,fzm = 0,fall = 0;
							if(lstSZR.contains(fixedValue.get("CREATOR"))){
								if (szMap.get("SZR").equals(fixedValue.get("CREATOR"))) {							
									String ffString = "";	
									fzs = Integer.parseInt(fixedValue.get("ZSGS").toString());
									fzm = Integer.parseInt(fixedValue.get("ZMGS").toString());
									fall = fzs + fzm;
									//求总和
									zzs_fixed += fzs;
									zzm_fixed += fzm;
									if (fzs > 0) 	ffString = "+" + fzs + "(修正值)";
									String ZSGS = szMap.get("ZSGS") + ffString;
								
									if (fzm > 0) 	ffString = "+" + fzm + "(修正值)";
									else 			ffString = "";							
									String ZMGS = szMap.get("ZMGS") + ffString;
									
									if (fall > 0)  	ffString = "+" + fall + "(修正值)";
									else 		   	ffString = "";
									String ZGS = szMap.get("ZGS") + ffString;	
									
									szMap.put("ZSGS",ZSGS);
									szMap.put("ZMGS",ZMGS);
									szMap.put("ZGS",ZGS);
								}
							}else if(!lstSZR.contains(fixedValue.get("CREATOR"))){
								lstSZR.add(fixedValue.get("CREATOR").toString());
								fzs = Integer.parseInt(fixedValue.get("ZSGS").toString());
								fzm = Integer.parseInt(fixedValue.get("ZMGS").toString());
								fall = fzs + fzm;
								//求总和
								zzs_fixed += fzs;
								zzm_fixed += fzm;
								//新建一条记录
								Map newMap = new HashMap<String, String>();
								newMap.put("SZR",fixedValue.get("CREATOR")+ "(修正值)");
								newMap.put("ZSGS",fzs + "(修正值)");
								newMap.put("ZMGS",fzm + "(修正值)");
								newMap.put("ZGS",fall + "(修正值)");
								list_newfixedMap.add(newMap);							
							}
						}
					}
					all_fixed = zzs_fixed + zzm_fixed;
				}
			}else {
				//添加基准值
				for(Map fixedValue :list_fixValue){					
					if(!lstSZR.contains(fixedValue.get("CREATOR"))){
						int fzs = 0,fzm = 0,fall = 0;
						lstSZR.add(fixedValue.get("CREATOR").toString());
						fzs = Integer.parseInt(fixedValue.get("ZSGS").toString());
						fzm = Integer.parseInt(fixedValue.get("ZMGS").toString());
						fall = fzs + fzm;
						//求总和
						zzs_fixed += fzs;
						zzm_fixed += fzm;
						//新建一条记录
						Map newMap = new HashMap<String, String>();
						newMap.put("SZR",fixedValue.get("CREATOR")+ "(修正值)");
						newMap.put("ZSGS",fzs + "(修正值)");
						newMap.put("ZMGS",fzm + "(修正值)");
						newMap.put("ZGS",fall + "(修正值)");
						list_newfixedMap.add(newMap);							
					}
				}
				all_fixed = zzs_fixed + zzm_fixed;
			}
			list_SZTJ.addAll(list_newfixedMap);	
			Map total= new HashMap();
			total.put("HJ", "合计");
			String ffString = "";
			//证书
			if (zzs_fixed > 0)	ffString =  "(修正值:"+ zzs_fixed+")" ;
			total.put("ZZS", (zzs + zzs_fixed) + ffString );
			//证明
			if (zzm_fixed > 0)  ffString = "(修正值:"+ zzm_fixed+")";
			else 		 		ffString = "";
			total.put("ZZM", (zzm + zzm_fixed)+ ffString);
			//全部
			if (all_fixed > 0) 	ffString =  "(修正值:"+ all_fixed+")";
			else 		   		ffString = "";
			total.put("ALL", (all+all_fixed) + ffString);			
			list_SZTJ.add(total);	
			
			Message msg = new Message();
			msg.setRows(list_SZTJ);
			return msg;
	}

	@Override
	public Message getIssueStatistics(String FZSJ_Q, String FZSJ_Z,
			String SearchStates) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT DJFZ.FZRY, SUM(CASE WHEN QZB.QZLX='0' THEN 1 ELSE 0 END) ZSGS,SUM(CASE WHEN QZB.QZLX='1' THEN 1 ELSE 0 END) ZMGS, COUNT(QZB.ID) AS ZGS ")
					.append(" FROM BDCK.BDCS_RKQZB QZB  ").append("LEFT JOIN BDCK.BDCS_DJFZ DJFZ ON QZB.BDCQZH = DJFZ.HFZSH WHERE SFFZ='1'");
		
		StringBuilder sqlBuilder_fixedValue = new StringBuilder();			        
		sqlBuilder_fixedValue.append("SELECT CREATOR,SUM(CASE WHEN FIXEDLX='djfztj-zs' THEN (TO_NUMBER(FIXEDCONTEXT)) ELSE 0 END) ZSGS,SUM(CASE WHEN FIXEDLX='djfztj-zm' THEN (TO_NUMBER(FIXEDCONTEXT)) ELSE 0 END) ZMGS FROM BDCK.RESULT_FIXED T WHERE FIXEDLX LIKE 'djfztj%' ");
	
		if (!StringHelper.isEmpty(FZSJ_Q)) {				
			sqlBuilder.append("AND TO_CHAR(QZB.FZSJ, 'yyyy-MM-DD') >= '").append(FZSJ_Q).append("' ");
			sqlBuilder_fixedValue.append(" AND TO_CHAR(FIXEDSTARTTIME, 'yyyy-MM-DD') >= '").append(FZSJ_Q).append("' ");

		}	
		if (!StringHelper.isEmpty(FZSJ_Z)) {				
			sqlBuilder.append("AND TO_CHAR(QZB.FZSJ, 'yyyy-MM-DD') <= '").append(FZSJ_Z).append("' ");
			sqlBuilder_fixedValue.append(" AND TO_CHAR(FIXEDENDTIME, 'yyyy-MM-DD') <= '").append(FZSJ_Z).append("' ");

		}
		
		if (!StringHelper.isEmpty(SearchStates)) {
			String[] states = SearchStates.split("-");
			String state = "";
			for (int i = 0; i < states.length; i++) {
				state += "'"+states[i]+"',";
			}
			state = state.substring(0, state.length()-1);
			String  statesql = " WHERE SEARCHSTATE in (" + state + ")  ";
			sqlBuilder.append(" AND QZB.SLBH IN (SELECT distinct XMXX.YWLSH ")
			.append(" FROM BDCK.BDCS_XMXX XMXX LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON DJDY.XMBH = XMXX.XMBH")
			.append(" WHERE BDCDYID IN(")
			.append(" SELECT BDCDYID FROM BDCK.BDCS_H_XZ").append(statesql)
			.append(" UNION ALL")
			.append(" SELECT BDCDYID FROM BDCK.BDCS_H_XZY").append(statesql)
			.append(" UNION ALL")
			.append(" SELECT BDCDYID FROM BDCK.BDCS_SYQZD_XZ").append(statesql)
			.append(" UNION ALL")
			.append(" SELECT BDCDYID FROM BDCK.BDCS_SHYQZD_XZ").append(statesql).append("))");
		}
		sqlBuilder.append(" GROUP BY DJFZ.FZRY ORDER BY  DJFZ.FZRY ");
		List<Map> list_FZTJ = baseCommonDao.getDataListByFullSql(sqlBuilder.toString());
		sqlBuilder_fixedValue.append(" GROUP BY CREATOR");
		List<Map> list_fixValue = baseCommonDao.getDataListByFullSql(sqlBuilder_fixedValue.toString());
		int zzs = 0,zzm = 0,all = 0;
		int zzs_fixed = 0,zzm_fixed = 0,all_fixed = 0;
		List<Map> list_newfixedMap = new ArrayList();
		List<String> lstSZR = new ArrayList<String>();
		
		if (list_FZTJ!=null && list_FZTJ.size()>0) {
			for(Map szMap :list_FZTJ){	
				if(!lstSZR.contains(szMap.get("FZRY")) && !StringHelper.isEmpty(szMap.get("FZRY"))){
					lstSZR.add(szMap.get("FZRY").toString());								
				}
			}
		
			for(Map szMap :list_FZTJ){				
				//求总和
				zzs += Integer.parseInt(szMap.get("ZSGS").toString());
				zzm += Integer.parseInt(szMap.get("ZMGS").toString());
				all += Integer.parseInt(szMap.get("ZGS").toString());
				//添加基准值
				for(Map fixedValue :list_fixValue){					
					if (!StringHelper.isEmpty(szMap.get("FZRY")) && !StringHelper.isEmpty(fixedValue.get("CREATOR"))) {
						int fzs = 0,fzm = 0,fall = 0;
						if(lstSZR.contains(fixedValue.get("CREATOR"))){
							if (szMap.get("FZRY").equals(fixedValue.get("CREATOR"))) {							
								String ffString = "";	
								fzs = Integer.parseInt(fixedValue.get("ZSGS").toString());
								fzm = Integer.parseInt(fixedValue.get("ZMGS").toString());
								fall = fzs + fzm;
								//求总和
								zzs_fixed += fzs;
								zzm_fixed += fzm;
								if (fzs > 0) 	ffString = "+" + fzs + "(修正值)";
								String ZSGS = szMap.get("ZSGS") + ffString;
							
								if (fzm > 0) 	ffString = "+" + fzm + "(修正值)";
								else 			ffString = "";							
								String ZMGS = szMap.get("ZMGS") + ffString;
								
								if (fall > 0)  	ffString = "+" + fall + "(修正值)";
								else 		   	ffString = "";
								String ZGS = szMap.get("ZGS") + ffString;	
								
								szMap.put("ZSGS",ZSGS);
								szMap.put("ZMGS",ZMGS);
								szMap.put("ZGS",ZGS);
							}
						}else if(!lstSZR.contains(fixedValue.get("CREATOR"))){
							lstSZR.add(fixedValue.get("CREATOR").toString());
							fzs = Integer.parseInt(fixedValue.get("ZSGS").toString());
							fzm = Integer.parseInt(fixedValue.get("ZMGS").toString());
							fall = fzs + fzm;
							//求总和
							zzs_fixed += fzs;
							zzm_fixed += fzm;
							//新建一条记录
							Map newMap = new HashMap<String, String>();
							newMap.put("FZRY",fixedValue.get("CREATOR")+ "(修正值)");
							newMap.put("ZSGS",fzs + "(修正值)");
							newMap.put("ZMGS",fzm + "(修正值)");
							newMap.put("ZGS",fall + "(修正值)");
							list_newfixedMap.add(newMap);							
						}
					}
				}
				all_fixed = zzs_fixed + zzm_fixed;
			}
		}else {
			//添加基准值
			for(Map fixedValue :list_fixValue){					
				if(!lstSZR.contains(fixedValue.get("CREATOR"))){
					int fzs = 0,fzm = 0,fall = 0;
					lstSZR.add(fixedValue.get("CREATOR").toString());
					fzs = Integer.parseInt(fixedValue.get("ZSGS").toString());
					fzm = Integer.parseInt(fixedValue.get("ZMGS").toString());
					fall = fzs + fzm;
					//求总和
					zzs_fixed += fzs;
					zzm_fixed += fzm;
					//新建一条记录
					Map newMap = new HashMap<String, String>();
					newMap.put("FZRY",fixedValue.get("CREATOR")+ "(修正值)");
					newMap.put("ZSGS",fzs + "(修正值)");
					newMap.put("ZMGS",fzm + "(修正值)");
					newMap.put("ZGS",fall + "(修正值)");
					list_newfixedMap.add(newMap);							
				}
			}
			all_fixed = zzs_fixed + zzm_fixed;
		}
		list_FZTJ.addAll(list_newfixedMap);	
		Map total= new HashMap();
		total.put("HJ", "合计");
		String ffString = "";
		//证书
		if (zzs_fixed > 0)	ffString =  "(修正值:"+ zzs_fixed+")" ;
		total.put("ZZS", (zzs + zzs_fixed) + ffString );
		//证明
		if (zzm_fixed > 0)  ffString = "(修正值:"+ zzm_fixed+")";
		else 		 		ffString = "";
		total.put("ZZM", (zzm + zzm_fixed)+ ffString);
		//全部
		if (all_fixed > 0) 	ffString =  "(修正值:"+ all_fixed+")";
		else 		   		ffString = "";
		total.put("ALL", (all+all_fixed) + ffString);
		list_FZTJ.add(total);	
		
		Message msg = new Message();
		msg.setRows(list_FZTJ);
		return msg;
	}
	
	private Map<String,Map<String,String>> joinother(List<Map> othermaps){
		Map <String,Map<String,String>> comothermap=new HashMap<String, Map<String,String>>();	
		if(!StringHelper.isEmpty(othermaps) && othermaps.size()>0){
			for(Map m :othermaps){
				String bdcdyid = StringHelper.FormatByDatatype(m.get("BDCDYID"));
				String yqzh="";
				if(!StringHelper.isEmpty(bdcdyid)){
					if(!comothermap.containsKey(bdcdyid)){
						Map<String,String> otheryt=new HashMap<String, String>();
						yqzh = StringHelper.FormatByDatatype(m.get("BDCQZH"));
						otheryt.put("YQZH", yqzh);
						otheryt.put("BDCDYID", bdcdyid);
						comothermap.remove(bdcdyid);
						comothermap.put(bdcdyid, otheryt);
					}else{
						Map<String,String> otheryt=new HashMap<String, String>();
						otheryt.put("YQZH", yqzh);
						otheryt.put("BDCDYID", bdcdyid);
						comothermap.put(bdcdyid, otheryt);
					}
				}
			}
		}
		return comothermap;
		
	}
	
	
	/**
	 * 
	 * @param landmaps
	 * @return 土地多用途
	 */
	private Map<String,Map<String,String>> joinLandTdyts(List<Map> landmaps){
		Map <String,Map<String,String>> comlandmap=new HashMap<String, Map<String,String>>();	
		if(!StringHelper.isEmpty(landmaps) && landmaps.size()>0){
			for(Map m: landmaps){
				String bdcdyid=StringHelper.FormatByDatatype(m.get("BDCDYID"));
				String tdyt=StringHelper.FormatByDatatype(m.get("TDYT"));
				String tdytid=StringHelper.FormatByDatatype(m.get("ID"));
				String tdytname="";
				if(!StringHelper.isEmpty(tdyt)){
					tdytname=ConstHelper.getNameByValue("TDYT",tdyt);
				}
				if(!StringHelper.isEmpty(bdcdyid)){
					if(comlandmap.containsKey(bdcdyid)){
						Map<String,String> yt=comlandmap.get(bdcdyid);
						if(!yt.containsValue(tdytid)){
							String temptdytname=StringHelper.FormatByDatatype(yt.get("TDYT"));
							String id=StringHelper.FormatByDatatype(yt.get("ID"));
							temptdytname=StringHelper.joinStr(tdytname,temptdytname);
							id=StringHelper.joinStr(tdytid,id);
							Map<String,String> temyt=new HashMap<String, String>();
							temyt.put("TDYT", temptdytname);
							temyt.put("ID", id);
							comlandmap.remove(bdcdyid);
							comlandmap.put(bdcdyid, temyt);
						}
					}else{
						Map<String,String> temyt=new HashMap<String, String>();
						temyt.put("TDYT", tdytname);
						temyt.put("ID", tdytid);
						comlandmap.put(bdcdyid, temyt);
					}
				}
			}
		}
		return comlandmap;
	}
	
	
	private Map<String,Map<String,String>> joinHouseandlandInfos(List<Map> houseandlandmaps){
		Map <String,Map<String,String>> houseandlandmap=new HashMap<String, Map<String,String>>();	
		if(!StringHelper.isEmpty(houseandlandmaps) && houseandlandmaps.size()>0){
			for(Map m: houseandlandmaps){
				String bdcdyid=StringHelper.FormatByDatatype(m.get("BDCDYID"));
				String tdyt=StringHelper.FormatByDatatype(m.get("TDYT"));
				String tdytid=StringHelper.FormatByDatatype(m.get("ID"));
				String qlxz=StringHelper.FormatByDatatype(m.get("QLXZ"));
				String zdmj=StringHelper.formatDouble(m.get("ZDMJ"));
				String zdbdcdyid=StringHelper.FormatByDatatype(m.get("ZDBDCDYID"));
				String qlxzmc="";
				String tdytname="";
				if(!StringHelper.isEmpty(qlxz)){
					qlxzmc=ConstHelper.getNameByValue("QLXZ",qlxz);
				}
				if(!StringHelper.isEmpty(tdyt)){
					tdytname=ConstHelper.getNameByValue("TDYT",tdyt);
				}
				if(!StringHelper.isEmpty(bdcdyid) && !StringHelper.isEmpty(zdbdcdyid)){
					if(houseandlandmap.containsKey(bdcdyid)){
						Map<String,String> house=houseandlandmap.get(bdcdyid);
						if(!house.containsValue(tdytid)){
							String temptdytname=StringHelper.FormatByDatatype(house.get("TDYT"));
							String id=StringHelper.FormatByDatatype(house.get("ID"));
							temptdytname=StringHelper.joinStr(tdytname,temptdytname);
							id=StringHelper.joinStr(tdytid,id);
							Map<String,String> temyt=new HashMap<String, String>();
							temyt.put("TDYT", temptdytname);
							temyt.put("ID", id);
							temyt.put("ZDMJ", zdmj);
							temyt.put("QLXZ", qlxzmc);
							houseandlandmap.remove(bdcdyid);
							houseandlandmap.put(bdcdyid, temyt);
						}
					}else{
						Map<String,String> temyt=new HashMap<String, String>();
						temyt.put("TDYT", tdytname);
						temyt.put("ID", tdytid);
						temyt.put("ZDMJ", zdmj);
						temyt.put("QLXZ", qlxzmc);
						houseandlandmap.put(bdcdyid, temyt);
					}
				}
			}
		}
		return houseandlandmap;
		
	}
	/**
	 * 
	 * @param housemaps
	 * @return 房屋信息
	 */
	private Map<String,Map<String,String>> joinHouseInfos(List<Map> housemaps){
		Map <String,Map<String,String>> comhousemap=new HashMap<String, Map<String,String>>();	
		if(!StringHelper.isEmpty(housemaps) && housemaps.size()>0){
			for(Map m: housemaps){
				String bdcdyid=StringHelper.FormatByDatatype(m.get("BDCDYID"));
				String tdyt=StringHelper.FormatByDatatype(m.get("TDYT"));
				String tdytid=StringHelper.FormatByDatatype(m.get("ID"));
				String qlxz=StringHelper.FormatByDatatype(m.get("QLXZ"));
				String zdmj=StringHelper.formatDouble(m.get("ZDMJ"));
				String zdbdcdyid=StringHelper.FormatByDatatype(m.get("ZDBDCDYID"));
				String qlxzmc="";
				String tdytname="";
				if(!StringHelper.isEmpty(qlxz)){
					qlxzmc=ConstHelper.getNameByValue("QLXZ",qlxz);
				}
				if(!StringHelper.isEmpty(tdyt)){
					tdytname=ConstHelper.getNameByValue("TDYT",tdyt);
				}
				if(!StringHelper.isEmpty(bdcdyid)){
					if(comhousemap.containsKey(bdcdyid)){
						Map<String,String> house=comhousemap.get(bdcdyid);
						if(!house.containsValue(tdytid)){
							String temptdytname=StringHelper.FormatByDatatype(house.get("TDYT"));
							String id=StringHelper.FormatByDatatype(house.get("ID"));
							temptdytname=StringHelper.joinStr(tdytname,temptdytname);
							id=StringHelper.joinStr(tdytid,id);
							Map<String,String> temyt=new HashMap<String, String>();
							temyt.put("TDYT", temptdytname);
							temyt.put("ID", id);
							temyt.put("ZDMJ", zdmj);
							temyt.put("QLXZ", qlxzmc);
							comhousemap.remove(bdcdyid);
							comhousemap.put(bdcdyid, temyt);
						}
					}else{
						Map<String,String> temyt=new HashMap<String, String>();
						temyt.put("TDYT", tdytname);
						temyt.put("ID", tdytid);
						temyt.put("ZDMJ", zdmj);
						temyt.put("QLXZ", qlxzmc);
						comhousemap.put(bdcdyid, temyt);
					}
				}
			}
		}
		return comhousemap;
	}
	
	/**
	 * 
	 * @param ywrmap
	 * @return 义务人列表
	 */
	private Map<String,Map<String,String>> joinYwrInfos(List<Map> ywrmap){
		Map <String,Map<String,String>> comywrmap=new HashMap<String, Map<String,String>>();
		if(!StringHelper.isEmpty(ywrmap) && ywrmap.size()>0){
			for(Map m:ywrmap){
				//SQR.SQRXM,SQR.LXDH ,A.XMBH
				String xmbh=StringHelper.FormatByDatatype(m.get("XMBH"));
				String lxdh=StringHelper.FormatByDatatype(m.get("DH"));
				String sqrxm=StringHelper.FormatByDatatype(m.get("SQRXM"));
				String zjh = StringHelper.FormatByDatatype(m.get("ZJH"));
				if(!StringHelper.isEmpty(xmbh)){
					if(comywrmap.containsKey(xmbh)){
						Map<String,String> ywr=comywrmap.get(xmbh);
						String templxdh=StringHelper.FormatByDatatype(ywr.get("DH"));
						String tempsqrxm=StringHelper.FormatByDatatype(ywr.get("SQRXM"));
						String tempsqrzjh=StringHelper.FormatByDatatype(ywr.get("ZJH"));
						templxdh=StringHelper.joinStr(lxdh,templxdh);
						tempsqrxm=StringHelper.joinStr(sqrxm,tempsqrxm);
						Map<String,String> sqr=new HashMap<String, String>();
						sqr.put("LXDH", templxdh);
						sqr.put("QLRMC", tempsqrxm);
						comywrmap.remove(xmbh);
						comywrmap.put(xmbh, sqr);
					}else{
						Map<String,String> sqr=new HashMap<String, String>();
						sqr.put("LXDH", lxdh);
						sqr.put("QLRMC", sqrxm);
						comywrmap.put(xmbh, sqr);
					}
				}
				
			}
		}
		
		return comywrmap;
	}
	
	/**
	 * 
	 * @param sqrmap
	 * @return 权利人列表值
	 */
	private  Map <String,Map<String,String>> joinQlrInfos(List<Map> sqrmap){
		Map <String,Map<String,String>> comsqrmap=new HashMap<String, Map<String,String>>();
		if(!StringHelper.isEmpty(sqrmap) && sqrmap.size()>0){
			for(Map m :sqrmap){
				String qlid=StringHelper.FormatByDatatype(m.get("QLID"));
				String dh=StringHelper.FormatByDatatype(m.get("DH"));
				String qlrmc=StringHelper.FormatByDatatype(m.get("QLRMC"));
				String bdcqzh=StringHelper.FormatByDatatype(m.get("BDCQZH"));
				String zjh=StringHelper.FormatByDatatype(m.get("ZJH"));
				String qlrlx=StringHelper.FormatByDatatype(m.get("QLRLX"));
				String qlrlxname="";
				if(!StringHelper.isEmpty(qlrlx)){
					qlrlxname=ConstHelper.getNameByValue("QLRLX",qlrlx);
				}
				if(StringHelper.isEmpty(qlrlxname)){
					qlrlxname=qlrlx;
				}
				if(comsqrmap.containsKey(qlid)){
					Map<String,String> sqr=comsqrmap.get(qlid);
					String tempdh=StringHelper.FormatByDatatype(sqr.get("DH"));
					String tempqlrmc=StringHelper.FormatByDatatype(sqr.get("QLRMC"));
					String tempbdcqzh=StringHelper.FormatByDatatype(sqr.get("BDCQZH"));
					String tempzjh=StringHelper.FormatByDatatype(sqr.get("ZJH"));
					String tempqlrlx=StringHelper.FormatByDatatype(sqr.get("QLRLX"));
					String tempqlrlxname="";
					if(!StringHelper.isEmpty(tempqlrlx)){
						tempqlrlxname=ConstHelper.getNameByValue("QLRLX",tempqlrlx);
					}
					if(StringHelper.isEmpty(tempqlrlxname)){
						tempqlrlxname=tempqlrlx;
					}
					tempdh=StringHelper.joinStr(dh,tempdh);
					tempqlrmc=StringHelper.joinStr(qlrmc,tempqlrmc);
					tempbdcqzh=StringHelper.joinStr(bdcqzh,tempbdcqzh);
					tempzjh=StringHelper.joinStr(zjh,tempzjh);
					tempqlrlxname=StringHelper.joinStr(qlrlxname,tempqlrlxname);
					comsqrmap.remove(qlid);
					Map<String,String> qlr=new HashMap<String, String>();
					qlr.put("LXDH", tempdh);
					qlr.put("QLRMC", tempqlrmc);
					qlr.put("BDCQZH", tempbdcqzh);
					qlr.put("ZJH", tempzjh);
					qlr.put("QLRLX", tempqlrlxname);
					comsqrmap.put(qlid, qlr);
				}else{
					Map<String,String> sqr=new HashMap<String, String>();
					sqr.put("LXDH", dh);
					sqr.put("QLRMC", qlrmc);
					sqr.put("BDCQZH", bdcqzh);
					sqr.put("ZJH", zjh);
					sqr.put("QLRLX", qlrlxname);
					comsqrmap.put(qlid, sqr);
				}
			}
		}
		return comsqrmap;
	}
	/*
	 * 不动产登记台账数据修复
	 */
	public List<Map> updateTZ(List<Map> result) {
		List<Map> result_new = new ArrayList<Map>();
		int i = 1;
		if(result!=null&&result.size()>0) {
			int test=1;
			for (Map m : result) {
				test++;
				if(test==1000) {
					break;
				}
				if (m.get("LXDH") == null) {
					List<BDCS_QLR_XZ> qlrdhs = baseCommonDao.getDataList(
							BDCS_QLR_XZ.class, " qlid='" + m.get("QLID") + "'");
					if (qlrdhs.size() > 0) {
						m.put("LXDH", qlrdhs.get(0).getDH());
					}
				}
				// liangc 添加列“宗地面积”、“受理时间”、“转移前权利人”、“转移前权利人证件号、“转移前不动产权证号”、“抵押人”
				if (m.containsKey("SLSJ")) {
					m.put("SLSJ",
							StringHelper.FormatYmdhmsByDate(m.get("SLSJ")));
				}

				String djlx = StringHelper.formatObject(m.get("DJLX"));
				m.put("DJLX", ConstHelper.getNameByValue("DJLX", djlx));
				if ("200".equals(djlx)) {
					String qlid = StringHelper
							.FormatByDatatype(m.get("LYQLID"));
					Rights rights = RightsTools.loadRights(DJDYLY.LS, qlid);
					if (!StringHelper.isEmpty(rights)) {
						m.put("ZYQBDCQZH", StringHelper.FormatByDatatype(rights
								.getBDCQZH()));
					}
					List<RightsHolder> list = RightsHolderTools
							.loadRightsHolders(DJDYLY.LS, qlid);
					if (!StringHelper.isEmpty(list) && list.size() > 0) {
						String ZYQQLR = "";
						String ZYQQLRZJH = "";
						String ZYQQLRLXDH = "";
						for (int j = 0; j < list.size(); j++) {
							if (list.get(j).getQLRMC() != null) {
								ZYQQLR += StringHelper.FormatByDatatype(list
										.get(j).getQLRMC()) + ",";
							}
							if (list.get(j).getZJH() != null) {
								ZYQQLRZJH += StringHelper.FormatByDatatype(list
										.get(j).getZJH()) + ",";
							}
							if (list.get(j).getDH() != null) {
								ZYQQLRLXDH += StringHelper.formatObject(list
										.get(j).getDH()) + ",";
							}
						}
						if (ZYQQLR.length() > 0) {
							ZYQQLR = ZYQQLR.substring(0, ZYQQLR.length() - 1);
						}
						if (ZYQQLRZJH.length() > 0) {
							ZYQQLRZJH = ZYQQLRZJH.substring(0,
									ZYQQLRZJH.length() - 1);
						}
						if (ZYQQLRLXDH.length() > 0) {
							ZYQQLRLXDH = ZYQQLRLXDH.substring(0,
									ZYQQLRLXDH.length() - 1);
						}
						m.put("ZYQQLR", ZYQQLR);
						m.put("ZYQQLRZJH", ZYQQLRZJH);
						m.put("ZYQQLRLXDH", ZYQQLRLXDH);
					}
				}
				if ("300".equals(djlx) || "900".equals(djlx) ) {
					String qlid = StringHelper.FormatByDatatype(m.get("LYQLID"));
					Rights rights = RightsTools.loadRights(DJDYLY.LS, qlid);
					if (!StringHelper.isEmpty(rights)) {
						m.put("YQZH", StringHelper.FormatByDatatype(rights.getBDCQZH()));
					}
				}
				String xmbh = m.get("XMBH") + "";
				String ywr = "";
				String ywrzjh = "";
				String ywrdh = "";
				String fulSql = "SELECT TO_CHAR(WM_CONCAT(TO_CHAR(SQR.SQRXM))) AS SQRXM, (WM_CONCAT(TO_CHAR(SQR.LXDH))) AS LXDH, "
						+ "TO_CHAR(WM_CONCAT(TO_CHAR(SQR.ZJH))) AS ZJH FROM BDCK.BDCS_SQR SQR WHERE SQR.SQRLB='2' "
						+ " AND SQR.XMBH='" + xmbh + "'";
				List<Map> sqrxms = baseCommonDao.getDataListByFullSql(fulSql);
				if (sqrxms != null && sqrxms.size() > 0) {
					ywr = StringHelper.formatObject(sqrxms.get(0).get("SQRXM"));
					ywrzjh = StringHelper.formatObject(sqrxms.get(0).get("ZJH"));
					ywrdh = StringHelper.formatObject(sqrxms.get(0).get("LXDH"));
				}
				m.put("YWR", ywr);
				m.put("YWRZJH", ywrzjh);
				m.put("YWRDH", ywrdh);
				if (m.get("BDBZZQSE")!= null) {
					m.put("DYJE", m.get("BDBZZQSE"));
				}
				if (m.get("ZGZQSE")!= null) {
					m.put("DYJE", m.get("ZGZQSE"));
				}
				m.put("YWRDH", ywrdh);
				// liangc 当户的权利性质为空时，取使用权宗地的权利性质;户的宗地面积取关联宗地的宗地面积
				if (m.get("BDCDYLX") != null) {

					if (m.get("BDCDYLX").equals("031")) {
						if (!StringHelper.isEmpty(m.get("BDCDYID"))) {
							StringBuilder hhql = new StringBuilder(
									" bdcdyid ='" + m.get("BDCDYID") + "'");
							List<BDCS_H_XZ> zdbdcdyids = baseCommonDao
									.getDataList(BDCS_H_XZ.class,
											hhql.toString());
							if (!StringHelper.isEmpty(zdbdcdyids)
									&& zdbdcdyids.size() > 0) {
								String zdbdcdyid = zdbdcdyids.get(0)
										.getZDBDCDYID();
								StringBuilder zdhql = new StringBuilder(
										" bdcdyid ='" + zdbdcdyid + "'");
								List<BDCS_SHYQZD_XZ> qlxzs = baseCommonDao
										.getDataList(BDCS_SHYQZD_XZ.class,
												zdhql.toString());
								if (!StringHelper.isEmpty(qlxzs)
										&& qlxzs.size() > 0) {
									if (StringUtils.isEmpty(m.get("QLXZ"))) {
										m.put("QLXZ", qlxzs.get(0).getQLXZ());
									}
									m.put("ZDMJ", qlxzs.get(0).getZDMJ());
								}
							}
						}
					}
					if (m.get("BDCDYLX").equals("032")) {
						if (!StringHelper.isEmpty(m.get("BDCDYID"))) {
							StringBuilder hhql = new StringBuilder(
									" bdcdyid ='" + m.get("BDCDYID") + "'");
							List<BDCS_H_XZY> zdbdcdyids = baseCommonDao
									.getDataList(BDCS_H_XZY.class,
											hhql.toString());
							if (!StringHelper.isEmpty(zdbdcdyids)
									&& zdbdcdyids.size() > 0) {
								String zdbdcdyid = zdbdcdyids.get(0)
										.getZDBDCDYID();
								StringBuilder zdhql = new StringBuilder(
										" bdcdyid ='" + zdbdcdyid + "'");
								List<BDCS_SHYQZD_XZ> qlxzs = baseCommonDao
										.getDataList(BDCS_SHYQZD_XZ.class,
												zdhql.toString());
								if (!StringHelper.isEmpty(qlxzs)
										&& qlxzs.size() > 0) {
									if (StringUtils.isEmpty(m.get("QLXZ"))) {
										m.put("QLXZ", qlxzs.get(0).getQLXZ());
									}
									m.put("ZDMJ", qlxzs.get(0).getZDMJ());
								}
							}
						}
					}
					// liangc 宗地用途取TDYT表
					if (m.get("BDCDYLX").equals("01")
							|| m.get("BDCDYLX").equals("02")) {
						if (!StringHelper.isEmpty(m.get("BDCDYID"))) {
							StringBuilder zdhql = new StringBuilder(
									" bdcdyid ='" + m.get("BDCDYID") + "'");
							List<BDCS_TDYT_XZ> tdyts = baseCommonDao
									.getDataList(BDCS_TDYT_XZ.class,
											zdhql.toString());
							if ( tdyts.size() > 0&& !StringHelper.isEmpty(tdyts)) {
								StringBuilder tdyt = new StringBuilder();
								if (tdyts.size() > 0) {
									for (int j = 0; j < tdyts.size(); j++) {
										tdyt.append(tdyts.get(j).getTDYTMC());
										if (j < tdyts.size() - 1)
											tdyt.append(",");
									}
										if (!"null".equals(tdyt.toString())) {
											m.put("TDYT", tdyt.toString());
										}
									}
							}
						}
					}

					if (m.get("BDCDYLX").equals("032")
							|| m.get("BDCDYLX").equals("031")|| m.get("BDCDYLX").equals("09")) {
						// System.out.println(m.get("TDYT"));
						String tdyt = StringHelper.formatObject(m.get("TDYT"));
						m.put("TDYT", ConstHelper.getNameByValue("TDYT", tdyt));
					}

					if ("".equals(m.get("TDYT"))) {
						if (m.get("BDCDYLX").equals("031")) {
							if (!StringHelper.isEmpty(m.get("BDCDYID"))) {
								String hsql = " bdcdyid ='" + m.get("BDCDYID")
										+ "'";
								List<BDCS_H_XZ> hs = baseCommonDao.getDataList(
										BDCS_H_XZ.class, hsql);
								if (hs.size() > 0) {
									hsql = " bdcdyid ='"
											+ hs.get(0).getZDBDCDYID() + "'";
									List<BDCS_TDYT_XZ> tdyts = baseCommonDao
											.getDataList(BDCS_TDYT_XZ.class,
													hsql);
									if (!StringHelper.isEmpty(tdyts)
											&& tdyts.size() > 0) {
										// if(tdyts.get(0).getSFZYT().equals("1")){
										StringBuilder tdyt = new StringBuilder();
										if (tdyts.size() > 0) {
											for (int j = 0; j < tdyts.size(); j++) {
												tdyt.append(tdyts.get(j)
														.getTDYTMC());
												if (j < tdyts.size() - 1)
													tdyt.append(",");
											}
											if (!"null".equals(tdyt.toString())) {
												m.put("TDYT", tdyt.toString());
											}
										}
										// }
									}
								}
							}
						}
					}

					if ("".equals(m.get("TDYT"))) {
						if (m.get("BDCDYLX").equals("032")) {
							if (!StringHelper.isEmpty(m.get("BDCDYID"))) {
								String hsql = " bdcdyid ='" + m.get("BDCDYID")
										+ "'";
								List<BDCS_H_XZY> hys = baseCommonDao
										.getDataList(BDCS_H_XZY.class, hsql);
								if (hys.size() > 0) {
									hsql = " bdcdyid ='"
											+ hys.get(0).getZDBDCDYID() + "'";
									List<BDCS_TDYT_XZ> tdyts = baseCommonDao
											.getDataList(BDCS_TDYT_XZ.class,
													hsql);
									if (!StringHelper.isEmpty(tdyts)
											&& tdyts.size() > 0) {
										StringBuilder tdyt = new StringBuilder();
										if (tdyts.size() > 0) {
											for (int j = 0; j < tdyts.size(); j++) {
												tdyt.append(tdyts.get(j)
														.getTDYTMC());
												if (j < tdyts.size() - 1)
													tdyt.append(",");
											}
											if (!"null".equals(tdyt.toString())) {
												m.put("TDYT", tdyt.toString());
											}
										}
									}
								}
							}
						}
					}

					String bdcdylx = StringHelper
							.formatObject(m.get("BDCDYLX"));
					if (m.get("BDCDYLX").equals("032")) {
						m.put("BDCDYLX", "预测户");
					} else {
						m.put("BDCDYLX",
								ConstHelper.getNameByValue("BDCDYLX", bdcdylx));
					}
				}

				/*
				 * String bdcdylx=StringHelper.formatObject(m.get("BDCDYLX"));
				 * m.put("BDCDYLX", ConstHelper.getNameByValue("BDCDYLX",
				 * bdcdylx));
				 */

				if (m.get("QLRLX") != null) {
					if (m.get("QLRLX").toString().contains(",")) {
						String[] qlrlxs = m.get("QLRLX").toString().split(",");
						String qlr = "";
						for (int a = 0; a < qlrlxs.length; a++) {
							qlr += ConstHelper.getNameByValue("QLRLX",
									qlrlxs[a]) + ",";
						}
						qlr = qlr.substring(0, qlr.length() - 1);
						m.put("QLRLX", qlr);
					} else {
						String qlrlx = StringHelper
								.formatObject(m.get("QLRLX"));
						m.put("QLRLX",
								ConstHelper.getNameByValue("QLRLX", qlrlx));
					}
				}
				
				if(StringHelper.isEmpty(m.get("FZSJ"))){
					m.put("SFFZ", "否");
				}else if(!StringHelper.isEmpty(m.get("FZSJ"))){
					m.put("SFFZ", "是");
				}
				
				if(!StringHelper.isEmpty(m.get("CZFS"))&&m.get("CZFS").equals("01")){
					if(!StringHelper.isEmpty(m.get("BDCQZH"))&&m.get("BDCQZH").toString().contains(",")){
						String bdcqzh[] = m.get("BDCQZH").toString().split(",");
						m.put("BDCQZH", bdcqzh[0]);
					}
				}

				String qllx = StringHelper.formatObject(m.get("QLLX"));
				m.put("QLLX", ConstHelper.getNameByValue("QLLX", qllx));

				// String tdyt=StringHelper.formatObject(m.get("TDYT"));
				// m.put("TDYT", ConstHelper.getNameByValue("TDYT", tdyt));

				String qlxz = StringHelper.formatObject(m.get("QLXZ"));
				m.put("QLXZ", ConstHelper.getNameByValue("QLXZ", qlxz));

				String fwyt = StringHelper.formatObject(m.get("FWYT"));
				m.put("FWYT", ConstHelper.getNameByValue("FWYT", fwyt));

				String fwxz = StringHelper.formatObject(m.get("FWXZ"));
				m.put("FWXZ", ConstHelper.getNameByValue("FWXZ", fwxz));

				m.put("XH", StringHelper.formatObject(i));

				m.put("QDJG", StringHelper.formatObject(m.get("QDJG")));
				//所在层  huangmingh
				String szc = StringHelper.formatObject(m.get("SZC"));
				m.put("SZC", szc);
				//总层数
				String zcs = StringHelper.FormatByDatatype(m.get("ZCS"));
				m.put("ZCS", zcs);
				// liangc
				String fwjg = StringHelper.formatObject(m.get("FWJG"));
				m.put("FWJG", ConstHelper.getNameByValue("FWJG", fwjg));
				String gyfs = StringHelper.formatObject(m.get("GYFS"));
				m.put("GYFS", ConstHelper.getNameByValue("GYFS", gyfs));
				if (!StringHelper.isEmpty(m.get("DJSJ"))&&!xmbh.equals("null")&&xmbh.length()>0) {
					String Baseworkflow_ID =ProjectHelper.GetPrjInfoByXMBH(xmbh).getBaseworkflowcode();
					if (!StringHelper.isEmpty(Baseworkflow_ID) && Baseworkflow_ID.indexOf("JF")!= -1) { // 解封登记 ，登记时间获取 解封登记的登簿时间
						if (m.containsKey("DJSJ")) {
							m.put("DJSJ",StringHelper.FormatYmdhmsByDate(m.get("ZXSJ")));
						}
					}else {
						m.put("DJSJ", StringHelper.FormatYmdhmsByDate(m.get("DJSJ")));
					}
				}
				String YWLX = StringHelper.formatObject(m.get("YWLX"));
				if (!StringHelper.isEmpty(YWLX)) {
					m.put("YWLX", YWLX.replace('.', '-'));
				}
				//i++;
				result_new.add(m);
			}
	  
		}
		return result_new;
		
	}

}