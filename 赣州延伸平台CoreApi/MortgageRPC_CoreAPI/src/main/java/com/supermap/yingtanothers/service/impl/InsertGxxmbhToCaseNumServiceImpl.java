package com.supermap.yingtanothers.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.workflow.util.Message;
import com.supermap.yingtanothers.service.InsertGxxmbhToCaseNumService;

/**
 * 
 * 作者： 苗利涛
 * 时间： 2016年4月15日 上午11:40:02
 * 功能：和房管局对接，把共享项目编号插入到casenum字段实现类
 */
@Service("InsertGxxmbhToCaseNumService")
public class InsertGxxmbhToCaseNumServiceImpl implements InsertGxxmbhToCaseNumService{

	@Autowired
	private CommonDao dao;
	
	@Override
	public Message InsertGxxmbhToCaseNum(String gxxmbh, String xmbh) {
		Message m = null;		
		try {
			m = new Message();			
			StringBuilder conditionBuilder = new StringBuilder("1 = 1");
			if (!StringHelper.isEmpty(xmbh)) {
				conditionBuilder.append(" AND XMBH = '").append(xmbh).append("'");
				String strQuery = conditionBuilder.toString();
				List<BDCS_QL_GZ> qllist = dao.getDataList(BDCS_QL_GZ.class, strQuery);
				if(qllist.size() > 0){
					String fSQLID = qllist.get(0).getFSQLID();					
					for (BDCS_QL_GZ bdcs_ql_gz : qllist) {
						bdcs_ql_gz.setCASENUM(gxxmbh);
						dao.update(bdcs_ql_gz);
						dao.flush();
					}					
					if(fSQLID != "" && fSQLID != null){
						List<BDCS_FSQL_GZ> fsqllist = dao.getDataList(BDCS_FSQL_GZ.class, strQuery);
						if (fsqllist.size() > 0) {
							for (BDCS_FSQL_GZ bdcs_fsql_gz : fsqllist) {
								bdcs_fsql_gz.setCASENUM(gxxmbh);
								dao.update(bdcs_fsql_gz);
								dao.flush();
							}
							
						}						
					}
				}
			}
													
		} catch (Exception e) {
			YwLogUtil.addYwLog("更新BDCS_QL_GZ和BDCS_FSQL_GZ的casenum字段-失败", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
		}
		return m;
	}

}
