package com.supermap.realestate.registration.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.service.LogInfoQueryService;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;

@Service("LogInfoQueryService")
public class LogInfoQueryServiceImpl implements LogInfoQueryService {
	@Autowired
	private CommonDao dao;
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	@Override
	public Message queryLog(Map<String, String> queryvalues, int page, int rows, boolean iflike, String sort, String order) {
		Message msg = new Message(); 
		long count = 0;
		List<Map> listresult = new ArrayList<Map>();
		List<Map> listmt = new ArrayList<Map>();
		List<Map> listqy = new ArrayList<Map>();
		List<Map> listlin = new ArrayList<Map>();
		StringBuilder builder = new StringBuilder();
		StringBuilder datamaintainBuilder = new StringBuilder();
		StringBuilder queryBuilder = new StringBuilder();
		StringBuilder loginBuilder = new StringBuilder();
		String where_mtrzlx = "";
		String where_mtoprlx = "";
		String where_cxlx = "";
		String where_dllx = "";
		String where_rymc = "";
		String where_ms = "";
		String where_sj_q = "";
		String where_sj_z = "";
		String orderby = "";
		
		String rzlx = queryvalues.get("RZLX");
		String mtrzlx = queryvalues.get("MTRZLX");
		String mtoprlx = queryvalues.get("MTOPRLX");
		String cxlx = queryvalues.get("CXLX");
		String dllx = queryvalues.get("DLLX");
		String rymc = queryvalues.get("RYMC");
		String ms = queryvalues.get("MS");
		String sj_q = queryvalues.get("SJ_Q");
		String sj_z = queryvalues.get("SJ_Z");
		
		builder.append("select * ");
		datamaintainBuilder.append("from log.log_datamaintenace mt where mt.lgtype is not null");
		queryBuilder.append("from log.log_query qy where qy.lgtype is not null");
		loginBuilder.append("from log.log_login lin where lin.lgtype is not null");
		
		
		if(!StringHelper.isEmpty(mtrzlx) && !mtrzlx.equals("0")){
			where_mtrzlx = " and mt.lgtype='" + mtrzlx + "'";
		}
		if (!StringHelper.isEmpty(mtoprlx) && !mtoprlx.equals("0")) {
			where_mtoprlx = " and mt.mtlx='" + mtoprlx + "'";
		}
		if (!StringHelper.isEmpty(cxlx) && !cxlx.equals("0")) {
			where_cxlx = " and qy.lgtype='" + cxlx + "'";
		}
		if (!StringHelper.isEmpty(dllx) && !dllx.equals("0")) {
			where_dllx = " and lin.lgtype='" + dllx + "'";
		}
		if (!StringHelper.isEmpty(rymc)) {
			if (iflike) {
				where_rymc = " and lgrymc like'%" + rymc + "%'";
			}else {
				where_rymc = " and lgrymc='" + rymc + "'";
			}
		}
		if (!StringHelper.isEmpty(ms)) {
			if (iflike) {
				where_ms = " and lgdescription like'%" + ms + "%'";
			}else {
				where_ms = " and lgdescription='" + ms + "'";
			}
		}
		if(!StringHelper.isEmpty(sj_q)){
			where_sj_q = " and lgtime >=to_date('"+sj_q+"','yyyy-mm-dd') ";
		}
		if(!StringHelper.isEmpty(sj_z)){
			where_sj_z = " and lgtime <=(to_date('"+sj_z+"','yyyy-mm-dd')+1) ";
		}
		
		if(!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)){
			if(sort.toUpperCase().equals("LGTIME"))
				sort = "LGTIME";
			orderby = " ORDER BY " + sort + " " + order;
		}

		
		datamaintainBuilder.append(where_mtrzlx).append(where_mtoprlx).append(where_rymc).append(where_ms).append(where_sj_q).append(where_sj_z).append(orderby);
		queryBuilder.append(where_cxlx).append(where_rymc).append(where_ms).append(where_sj_q).append(where_sj_z).append(orderby);
		loginBuilder.append(where_dllx).append(where_rymc).append(where_ms).append(where_sj_q).append(where_sj_z).append(orderby);
		
		if (rzlx.equals("0")) {
			count = dao.getCountByFullSql(datamaintainBuilder.toString()) + dao.getCountByFullSql(queryBuilder.toString()) + dao.getCountByFullSql(loginBuilder.toString());
			listmt = dao.getPageDataByFullSql(builder.toString() + datamaintainBuilder.toString(), page, rows);
			if(listmt.size() == rows){
				listresult = listmt;
			}else if (listmt.size() > 0 && listmt.size() < rows) {
				int p = page-StringHelper.getInt(dao.getCountByFullSql(datamaintainBuilder.toString())/rows);
				listqy = dao.getPageDataByFullSql(builder.toString() + queryBuilder.toString(), p, rows-listmt.size());
				listresult.addAll(listmt);
				listresult.addAll(listqy);
			}else if (listmt.size() == 0) {
				int p = page-StringHelper.getInt(dao.getCountByFullSql(datamaintainBuilder.toString())/rows);
				listqy = dao.getPageDataByFullSql(builder.toString() + queryBuilder.toString(), p, rows);
				if (listqy.size() == rows) {
					listresult = listqy;
				}else if (listqy.size() > 0 && listqy.size() < rows) {
					int pa = page-StringHelper.getInt((dao.getCountByFullSql(datamaintainBuilder.toString()) + dao.getCountByFullSql(queryBuilder.toString()))/rows);
					listlin = dao.getPageDataByFullSql(builder.toString() + loginBuilder.toString(), pa, rows-listqy.size());
					listresult.addAll(listqy);
					listresult.addAll(listlin);
				}else if (listqy.size() == 0) {
					int pa = page-StringHelper.getInt((dao.getCountByFullSql(datamaintainBuilder.toString()) + dao.getCountByFullSql(queryBuilder.toString()))/rows);
					listlin = dao.getPageDataByFullSql(builder.toString() + loginBuilder.toString(), pa, rows);
					listresult = listlin;
				}
			}
		}else if (rzlx.equals("1")) {
			count = dao.getCountByFullSql(datamaintainBuilder.toString());
			listresult = dao.getPageDataByFullSql(builder.toString() + datamaintainBuilder.toString(), page, rows);
		}else if (rzlx.equals("2")) {
			count = dao.getCountByFullSql(queryBuilder.toString());
			listresult = dao.getPageDataByFullSql(builder.toString() + queryBuilder.toString(), page, rows);
		}else if (rzlx.equals("3")) {
			count = dao.getCountByFullSql(loginBuilder.toString());
			listresult = dao.getPageDataByFullSql(builder.toString() + loginBuilder.toString(), page, rows);
		}
		
		// 格式化时间
		for (Map map : listresult) {
			if (map.containsKey("LGTIME")) {
				map.put("LGTIME",StringHelper.FormatYmdhmsByDate(map.get("LGTIME")));
			}
		}
		msg.setTotal(count);
		msg.setRows(listresult);
		return msg;
	}
}
