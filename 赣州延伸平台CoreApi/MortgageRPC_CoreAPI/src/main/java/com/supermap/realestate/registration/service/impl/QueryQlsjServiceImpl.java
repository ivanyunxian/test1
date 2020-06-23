package com.supermap.realestate.registration.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.QueryClass.QLInfo;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.YC_SC_H_XZ;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.service.QueryQlsjService;
import com.supermap.realestate.registration.service.QueryService;
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

@Service("queryqlsjService")
public class QueryQlsjServiceImpl implements QueryQlsjService {

	@Autowired
	private CommonDao baseCommonDao;

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
	
	/**
	 * 判断是否关联宗地
	 * 
	 * @param listresult
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void isGlzd(List<Map> listresult) {
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
								&& BDCDYLX.H.equals(BDCDYLX.initFrom(bdcdylx))
								|| BDCDYLX.YCH
										.equals(BDCDYLX.initFrom(bdcdylx))) {
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
	//查询房屋产权预警日期的方法
	
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@Override
	public Message queryYjHouse(Map<String, String> queryvalues, int page,
			int rows, String qssj,String zl, String bdcqzh, String bdcdyh) {
		
		Message msg = new Message();
		long count = 0;
		List<Map> listresult = null;
		/* ===============1、先获取实体对应的表名=================== */
		String unitentityName = "BDCK.BDCS_H_XZ";

		/* ===============2、再获取表名+'_'+字段名=================== */
		String dysum = "from ";
		String dyqzname = "select DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.FH,DY.GHYT,DY.JZMJ AS SCJZMJ,DY.TNJZMJ AS SCTNJZMJ,DY.FTJZMJ AS SCFTJZMJ,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC,QL.BDCQZH,QLR.QLRMC,to_char(QL.QLQSSJ,'yyyy-MM-dd') QLQSSJ,to_char(QL.QLJSSJ,'yyyy-MM-dd') QLJSSJ,QLR.ZJH from";
		String dyfieldsname = "SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ ,'031' AS BDCDYLX ,'现房' AS BDCDYLXMC FROM BDCK.BDCS_H_XZ UNION ALL SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ  ,'032' AS BDCDYLX ,'期房' AS BDCDYLXMC FROM BDCK.BDCS_H_XZY";
		/* ===============3、构造查询语句=================== */
		/* SELECT 字段部分 */
		StringBuilder builder2 = new StringBuilder();
		StringBuilder builder3 = new StringBuilder();
		builder2.append(dyqzname).append("(").append(dyfieldsname).append(")");
		builder3.append(dysum).append("(").append(dyfieldsname).append(")");
		
		String selectstr = builder2.toString();
		String selectsum = builder3.toString();

		/* FROM 后边的表语句 */
		StringBuilder builder = new StringBuilder();
			 builder.append(" DY")
			.append(" left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid")	
			.append(" inner join (select DJDYID,QLID,QLJSSJ,QLQSSJ,BDCQZH from BDCK.bdcs_ql_xz where QLJSSJ<=to_date('"+qssj+"'"+",'yyyy-mm-dd') AND (QLLX='4' OR QLLX='6' OR QLLX='8')) ql on ql.djdyid=djdy.djdyid")
	        .append(" left join BDCK.BDCS_QLR_XZ qlr on ql.qlid=qlr.qlid ")
			.append(" where 1>0 "); 
				 
				// zl坐落
					if (!StringUtils.isEmpty(zl)) {
						builder.append(" and dy.zl like '%" + zl + "%'");
					}
					// bdcqzh 不动产权证号
					if (!StringUtils.isEmpty(bdcqzh)) {
						builder.append(" and ql.bdcqzh  like '%" + bdcqzh + "%'");
					}
					// bdcdyh 不动产单元号
					if (!StringUtils.isEmpty(bdcdyh)) {
						builder.append(" and dy.bdcdyh  like '%" + bdcdyh + "%'");
					}
					
					System.out.print(builder.toString());
					
					builder.append(" ORDER BY QL.QLJSSJ ");
			 
		
		String fromstr = builder.toString();
		String fullSql = selectstr + fromstr;
		String fullcount = selectsum + fromstr;

		/* 为了提高效率，先不查询符合条件的记录总数，将来改为异步获取 */
		CommonDao dao = baseCommonDao;
		
		Long csum = dao.getCountByFullSql(fullcount);

		listresult = dao.getPageDataByFullSql(fullSql, page, rows);
		addRightsHolderInfo(listresult);
	//	addLimitStatus(listresult);
		isGlzd(listresult);
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

		msg.setTotal(csum);
		msg.setRows(listresult);
		return msg;
	}
	
	//查询土地产权预警日期的方法
	
		@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
		@Override
		public Message queryYjTdxx(Map<String, String> queryvalues, int page,
				int rows, String qssj,String zl, String bdcqzh, String bdcdyh) {
			
			Message msg = new Message();
			long count = 0;
			List<Map> listresult = null;
			/* ===============1、先获取实体对应的表名=================== */
			String unitentityName = "BDCK.BDCS_SHYQZD_XZ";

			/* ===============2、再获取表名+'_'+字段名=================== */
			String dysum = "from ";
			String dyqzname = "select DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC,QL.QLID,QL.BDCQZH,QLR.QLRMC,to_char(QL.QLQSSJ,'yyyy-MM-dd') QLQSSJ,to_char(QL.QLJSSJ,'yyyy-MM-dd') QLJSSJ,QLR.ZJH from";
			String dyfieldsname = "SELECT ZL,ZDDM,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC FROM BDCK.BDCS_SYQZD_XZ UNION ALL SELECT ZL,ZDDM,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC FROM BDCK.BDCS_SHYQZD_XZ";
			/* ===============3、构造查询语句=================== */
			/* SELECT 字段部分 */
			StringBuilder builder2 = new StringBuilder();
			StringBuilder builder3 = new StringBuilder();
			builder2.append(dyqzname).append("(").append(dyfieldsname).append(")");
			builder3.append(dysum).append("(").append(dyfieldsname).append(")");
			
			String selectstr = builder2.toString();
			String selectsum = builder3.toString();

			/* FROM 后边的表语句 */
			StringBuilder builder = new StringBuilder();
				 builder.append(" DY")
				.append(" left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid")
				.append(" inner join (select DJDYID,QLID,QLJSSJ,QLQSSJ,BDCQZH from BDCK.bdcs_ql_xz where QLJSSJ<=to_date('"+qssj+"'"+",'yyyy-mm-dd') AND (QLLX='1' OR QLLX='2' OR QLLX='3' OR QLLX='5' OR QLLX='7')) ql on ql.djdyid=djdy.djdyid")
		        .append(" left join BDCK.BDCS_QLR_XZ qlr on ql.qlid=qlr.qlid ")
				.append(" where 1>0 "); 
					 
					// zl坐落
						if (!StringUtils.isEmpty(zl)) {
							builder.append(" and dy.zl like '%" + zl + "%'");
						}
						// bdcqzh 不动产权证号
						if (!StringUtils.isEmpty(bdcqzh)) {
							builder.append(" and ql.bdcqzh like '%" + bdcqzh + "%'");
						}
						// bdcdyh 不动产单元号
						if (!StringUtils.isEmpty(bdcdyh)) {
							builder.append(" and dy.bdcdyh like '%" + bdcdyh + "%'");
						}
						
						System.out.print(builder.toString());
						
						builder.append(" ORDER BY QL.QLJSSJ ");
						
			String fromstr = builder.toString();
			String fullSql = selectstr + fromstr;
			String fullcount = selectsum + fromstr;

			/* 为了提高效率，先不查询符合条件的记录总数，将来改为异步获取 */
			CommonDao dao = baseCommonDao;
			
			Long csum = dao.getCountByFullSql(fullcount);

			listresult = dao.getPageDataByFullSql(fullSql, page, rows);
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
			msg.setTotal(csum);
			msg.setRows(listresult);
			return msg;
		}
		
		//查询房屋抵押权预警日期的方法
		
		@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
		@Override
		public Message queryDyYjHouse(Map<String, String> queryvalues, int page,
				int rows, String qssj,String zl, String bdcqzh, String bdcdyh) {
			
			Message msg = new Message();
			long count = 0;
			List<Map> listresult = null;
			/* ===============1、先获取实体对应的表名=================== */
			String unitentityName = "BDCK.BDCS_H_XZ";

			/* ===============2、再获取表名+'_'+字段名=================== */
			String dysum = "from ";
			String dyqzname = "select DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.FH,DY.GHYT,DY.JZMJ AS SCJZMJ,DY.TNJZMJ AS SCTNJZMJ,DY.FTJZMJ AS SCFTJZMJ,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC,QL.BDCQZH,QLR.QLRMC,to_char(QL.QLQSSJ,'yyyy-MM-dd') QLQSSJ,to_char(QL.QLJSSJ,'yyyy-MM-dd') QLJSSJ,QLR.ZJH from";
			String dyfieldsname = "SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ ,'031' AS BDCDYLX ,'现房' AS BDCDYLXMC FROM BDCK.BDCS_H_XZ UNION ALL SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ  ,'032' AS BDCDYLX ,'期房' AS BDCDYLXMC FROM BDCK.BDCS_H_XZY";
			/* ===============3、构造查询语句=================== */
			/* SELECT 字段部分 */
			StringBuilder builder2 = new StringBuilder();
			StringBuilder builder3 = new StringBuilder();
			builder2.append(dyqzname).append("(").append(dyfieldsname).append(")");
			builder3.append(dysum).append("(").append(dyfieldsname).append(")");
			
			String selectstr = builder2.toString();
			String selectsum = builder3.toString();

			/* FROM 后边的表语句 */
			StringBuilder builder = new StringBuilder();
				 builder.append(" DY")
				.append(" left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid")	
				.append(" inner join (select DJDYID,QLID,QLJSSJ,QLQSSJ,BDCQZH from BDCK.bdcs_ql_xz where QLJSSJ<=to_date('"+qssj+"'"+",'yyyy-mm-dd') AND QLLX='23') ql on ql.djdyid=djdy.djdyid")
		        .append(" left join BDCK.BDCS_QL_XZ ql on ql.djdyid=djdy.djdyid and qllx in (4,6,8)")  //qllx为4,6,8房屋
			    .append(" left join BDCK.BDCS_QLR_XZ qlr on ql.qlid=qlr.qlid ")
				.append(" where 1>0 "); 
				 
				// zl坐落
					if (!StringUtils.isEmpty(zl)) {
						builder.append(" and dy.zl like '%" + zl + "%'");
					}
					// bdcqzh 不动产权证号
					if (!StringUtils.isEmpty(bdcqzh)) {
						builder.append(" and ql.bdcqzh  like '%" + bdcqzh + "%'");
					}
					// bdcdyh 不动产单元号
					if (!StringUtils.isEmpty(bdcdyh)) {
						builder.append(" and dy.bdcdyh  like '%" + bdcdyh + "%'");
					}
					
					System.out.print(builder.toString());
					
					builder.append(" ORDER BY QL.QLJSSJ ");
					
			String fromstr = builder.toString();
			String fullSql = selectstr + fromstr;
			String fullcount = selectsum + fromstr;

			/* 为了提高效率，先不查询符合条件的记录总数，将来改为异步获取 */
			CommonDao dao = baseCommonDao;
			
			Long csum = dao.getCountByFullSql(fullcount);

			listresult = dao.getPageDataByFullSql(fullSql, page, rows);
			addRightsHolderInfo(listresult);
		//	addLimitStatus(listresult);
			isGlzd(listresult);
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

			msg.setTotal(csum);
			msg.setRows(listresult);
			return msg;
		}
		
		//查询土地抵押权预警日期的方法
		
			@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
			@Override
			public Message queryDyYjTdxx(Map<String, String> queryvalues, int page,
					int rows, String qssj, String zl, String bdcqzh, String bdcdyh) {
				
				Message msg = new Message();
				long count = 0;
				List<Map> listresult = null;
				/* ===============1、先获取实体对应的表名=================== */
				String unitentityName = "BDCK.BDCS_SHYQZD_XZ";

				/* ===============2、再获取表名+'_'+字段名=================== */
				String dysum = "from ";
				String dyqzname = "select DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC,QL.QLID,QL.BDCQZH,QLR.QLRMC,to_char(QL.QLQSSJ,'yyyy-MM-dd') QLQSSJ,to_char(QL.QLJSSJ,'yyyy-MM-dd') QLJSSJ,QLR.ZJH from";
				String dyfieldsname = "SELECT ZL,ZDDM,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC FROM BDCK.BDCS_SYQZD_XZ UNION ALL SELECT ZL,ZDDM,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC FROM BDCK.BDCS_SHYQZD_XZ";
				/* ===============3、构造查询语句=================== */
				/* SELECT 字段部分 */
				StringBuilder builder2 = new StringBuilder();
				StringBuilder builder3 = new StringBuilder();
				builder2.append(dyqzname).append("(").append(dyfieldsname).append(")");
				builder3.append(dysum).append("(").append(dyfieldsname).append(")");
				
				String selectstr = builder2.toString();
				String selectsum = builder3.toString();

				/* FROM 后边的表语句 */
				StringBuilder builder = new StringBuilder();
					 builder.append(" DY")
					.append(" left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid")
					.append(" inner join (select DJDYID,QLID,QLJSSJ,QLQSSJ,BDCQZH from BDCK.bdcs_ql_xz where QLJSSJ<=to_date('"+qssj+"'"+",'yyyy-mm-dd') AND QLLX='23') ql on ql.djdyid=djdy.djdyid")
			        .append(" left join BDCK.BDCS_QL_XZ ql on ql.djdyid=djdy.djdyid and qllx in (1,2,3,5,7)")
					.append(" left join BDCK.BDCS_QLR_XZ qlr on ql.qlid=qlr.qlid ")
					.append(" where 1>0 "); 
					 
					// zl坐落
						if (!StringUtils.isEmpty(zl)) {
							builder.append(" and dy.zl like '%" + zl + "%'");
						}
						// bdcqzh 不动产权证号
						if (!StringUtils.isEmpty(bdcqzh)) {
							builder.append(" and ql.bdcqzh like '%" + bdcqzh + "%'");
						}
						// bdcdyh 不动产单元号
						if (!StringUtils.isEmpty(bdcdyh)) {
							builder.append(" and dy.bdcdyh  like '%" + bdcdyh + "%'");
						}
						
						System.out.print(builder.toString());
						
						builder.append(" ORDER BY QL.QLJSSJ ");
						
				String fromstr = builder.toString();
				String fullSql = selectstr + fromstr;
				String fullcount = selectsum + fromstr;

				/* 为了提高效率，先不查询符合条件的记录总数，将来改为异步获取 */
				CommonDao dao = baseCommonDao;
				
				Long csum = dao.getCountByFullSql(fullcount);

				listresult = dao.getPageDataByFullSql(fullSql, page, rows);
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
				msg.setTotal(csum);
				msg.setRows(listresult);
				return msg;
			}
			
			//查询房屋查封权预警日期的方法
			
			@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
			@Override
			public Message queryCfYjHouse(Map<String, String> queryvalues, int page,
					int rows, String qssj,String zl, String bdcqzh, String bdcdyh) {
				
				Message msg = new Message();
				long count = 0;
				List<Map> listresult = null;
				/* ===============1、先获取实体对应的表名=================== */
				String unitentityName = "BDCK.BDCS_H_XZ";

				/* ===============2、再获取表名+'_'+字段名=================== */
				String dysum = "from ";
				String dyqzname = "select DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.FH,DY.GHYT,DY.JZMJ AS SCJZMJ,DY.TNJZMJ AS SCTNJZMJ,DY.FTJZMJ AS SCFTJZMJ,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC,QL.BDCQZH,QLR.QLRMC,to_char(QLex.QLQSSJ,'yyyy-MM-dd') QLQSSJ,to_char(QLex.QLJSSJ,'yyyy-MM-dd') QLJSSJ,QLR.ZJH from";
				String dyfieldsname = "SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ ,'031' AS BDCDYLX ,'现房' AS BDCDYLXMC FROM BDCK.BDCS_H_XZ UNION ALL SELECT ZL,BDCDYH,BDCDYID,FH,GHYT,FWBM,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ  ,'032' AS BDCDYLX ,'期房' AS BDCDYLXMC FROM BDCK.BDCS_H_XZY";
				/* ===============3、构造查询语句=================== */
				/* SELECT 字段部分 */
				StringBuilder builder2 = new StringBuilder();
				StringBuilder builder3 = new StringBuilder();
				builder2.append(dyqzname).append("(").append(dyfieldsname).append(")");
				builder3.append(dysum).append("(").append(dyfieldsname).append(")");
				
				String selectstr = builder2.toString();
				String selectsum = builder3.toString();

				/* FROM 后边的表语句 */
				StringBuilder builder = new StringBuilder();
					 builder.append(" DY")
					.append(" left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid")
					.append(" inner join (select DJDYID,QLID,QLJSSJ,QLQSSJ,BDCQZH from BDCK.bdcs_ql_xz where QLJSSJ<=to_date('"+qssj+"'"+",'yyyy-mm-dd') AND DJLX='800' AND QLLX='99') qlex on qlex.djdyid=djdy.djdyid")
			        .append(" left join BDCK.BDCS_QL_XZ ql on ql.djdyid=djdy.djdyid and qllx in (4,6,8)")  //qllx为4,6,8房屋
			        .append(" left join BDCK.BDCS_QLR_XZ qlr on ql.qlid=qlr.qlid ")
					.append(" where 1>0 "); 
					
						// zl坐落
						if (!StringUtils.isEmpty(zl)) {
							builder.append(" and dy.zl like '%" + zl + "%'");
						}
						// bdcqzh 不动产权证号
						if (!StringUtils.isEmpty(bdcqzh)) {
							builder.append(" and ql.bdcqzh  like '%" + bdcqzh + "%'");
						}
						// bdcdyh 不动产单元号
						if (!StringUtils.isEmpty(bdcdyh)) {
							builder.append(" and dy.bdcdyh  like '%" + bdcdyh + "%'");
						}
						
						System.out.print(builder.toString());
						
						builder.append(" ORDER BY QLex.QLJSSJ ");
						
						
				String fromstr = builder.toString();
				String fullSql = selectstr + fromstr;
				String fullcount = selectsum + fromstr;

				/* 为了提高效率，先不查询符合条件的记录总数，将来改为异步获取 */
				CommonDao dao = baseCommonDao;
				
				Long csum = dao.getCountByFullSql(fullcount);

				listresult = dao.getPageDataByFullSql(fullSql, page, rows);
				addRightsHolderInfo(listresult);
			//	addLimitStatus(listresult);
				isGlzd(listresult);
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
				msg.setTotal(csum);
				msg.setRows(listresult);
				return msg;
			}
			
			//查询土地查封权预警日期的方法
			
			@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
			public Message queryCfYjTdxx(Map<String, String> queryvalues, int page,
				int rows, String qssj,String zl,String bdcqzh, String bdcdyh) {
				
				Message msg = new Message();
				long count = 0;
				List<Map> listresult = null;
				/* ===============1、先获取实体对应的表名=================== */
				String unitentityName = "BDCK.BDCS_SHYQZD_XZ";

				/* ===============2、再获取表名+'_'+字段名=================== */
				String dysum = "from ";
				String dyqzname = "select DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC,QL.QLID,QL.BDCQZH,QLR.QLRMC,to_char(QLex.QLQSSJ,'yyyy-MM-dd') QLQSSJ,to_char(QLex.QLJSSJ,'yyyy-MM-dd') QLJSSJ,QLR.ZJH from";
				String dyfieldsname = "SELECT ZL,ZDDM,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC FROM BDCK.BDCS_SYQZD_XZ UNION ALL SELECT ZL,ZDDM,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC FROM BDCK.BDCS_SHYQZD_XZ";
				/* ===============3、构造查询语句=================== */
				/* SELECT 字段部分 */
				StringBuilder builder2 = new StringBuilder();
				StringBuilder builder3 = new StringBuilder();
				builder2.append(dyqzname).append("(").append(dyfieldsname).append(")");
				builder3.append(dysum).append("(").append(dyfieldsname).append(")");
				
				String selectstr = builder2.toString();
				String selectsum = builder3.toString();

				/* FROM 后边的表语句 */
				StringBuilder builder = new StringBuilder();
					 builder.append(" DY")
					.append(" left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid")
					.append(" inner join (select DJDYID,QLID,QLJSSJ,QLQSSJ,BDCQZH from BDCK.bdcs_ql_xz where QLJSSJ<=to_date('"+qssj+"'"+",'yyyy-mm-dd') AND DJLX='800' AND QLLX='99') qlex on qlex.djdyid=djdy.djdyid")
			       	.append(" left join BDCK.BDCS_QL_XZ ql on ql.djdyid=djdy.djdyid and qllx in (1,2,3,5,7)") 
					.append(" left join BDCK.BDCS_QLR_XZ qlr on ql.qlid=qlr.qlid ")
					.append(" where 1>0 ");
					 
					// zl坐落
						if (!StringUtils.isEmpty(zl)) {
							builder.append(" and dy.zl like '%" + zl + "%'");
						}
						// bdcqzh 不动产权证号
						if (!StringUtils.isEmpty(bdcqzh)) {
							builder.append(" and ql.bdcqzh  like '%" + bdcqzh + "%'");
						}
						// bdcdyh 不动产单元号
						if (!StringUtils.isEmpty(bdcdyh)) {
							builder.append(" and dy.bdcdyh  like '%" + bdcdyh + "%'");
						}
						
						System.out.print(builder.toString());
						
						builder.append(" ORDER BY QLex.QLJSSJ ");
						
				String fromstr = builder.toString();
				String fullSql = selectstr + fromstr;
				String fullcount = selectsum + fromstr;

				/* 为了提高效率，先不查询符合条件的记录总数，将来改为异步获取 */
				CommonDao dao = baseCommonDao;
				
				Long csum = dao.getCountByFullSql(fullcount);

				listresult = dao.getPageDataByFullSql(fullSql, page, rows);
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
				msg.setTotal(csum);
				msg.setRows(listresult);
				return msg;
			}
				

}


