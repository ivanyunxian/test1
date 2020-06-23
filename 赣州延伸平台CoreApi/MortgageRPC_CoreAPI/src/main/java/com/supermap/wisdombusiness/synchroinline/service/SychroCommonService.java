package com.supermap.wisdombusiness.synchroinline.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.synchroinline.dao.synchroDao;
import com.supermap.wisdombusiness.synchroinline.model.EstateInlineLog;

@Service
public class SychroCommonService {
	@Autowired
	private synchroDao dao;
	
	public Date getEstateInlineLogs(String sql){
		List<EstateInlineLog> logs = null;
		Date date = null;
		if(!StringHelper.isEmpty(sql)){
			logs = dao.getDataList(EstateInlineLog.class,sql);
			if (logs != null&&logs.size()>0) {
				date = logs.get(0).getOperation_Date();
			}
		}
		return date;
	}
	public List<BDCS_XMXX> getBDCS_XMXXS(String sql){
		List<BDCS_XMXX> xmxms = null;
		if(!StringHelper.isEmpty(sql)){
			xmxms = dao.getDataList(BDCS_XMXX.class,sql);
		}
		return xmxms;
	}
	
	@SuppressWarnings("rawtypes")
	public List<Map> getObjectList(String sql){
		List<Map>  lists = null;
		if(!StringHelper.isEmpty(sql)){
		   lists = dao.getDataListByFullSql(sql);
		}
		return lists;
	}
}
