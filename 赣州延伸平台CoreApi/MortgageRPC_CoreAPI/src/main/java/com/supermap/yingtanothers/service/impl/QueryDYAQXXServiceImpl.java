package com.supermap.yingtanothers.service.impl;

import org.springframework.stereotype.Service;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.workflow.util.Message;
import com.supermap.yingtanothers.pool.ConnectionDao;
import com.supermap.yingtanothers.service.QueryDYAQXXService;
/**
 * 
 * 作者： 苗利涛
 * 时间： 2016年1月19日 上午10:28:27
 * 功能：鹰潭市不动产中间库通过不动产单元号和共享项目编号查询抵押权信息实现类
 */
@Service("QueryDYAQXXService")
public class QueryDYAQXXServiceImpl implements QueryDYAQXXService{

	
	
	@Override
	public Message QueryDyaqxxInfoByGXXMBH(String sqrxm, String bdcdyh, String gxxmbh, Integer page, Integer rows) {
		Message m = null;
		try {
			 m = new Message();

			StringBuilder conditionBuilder = new StringBuilder("1 = 1");
			if (!StringHelper.isEmpty(sqrxm)) {
				conditionBuilder.append(" AND DYR LIKE '%").append(sqrxm)
						.append("%'");
			}
			if (!StringHelper.isEmpty(gxxmbh)) {
				conditionBuilder.append(" AND GXXMBH LIKE '%").append(gxxmbh)
						.append("%'");
			}
			if (!StringHelper.isEmpty(bdcdyh)) {
				conditionBuilder.append(" AND YWH LIKE '%").append(bdcdyh)
						.append("%'");
			}
			String strQuery = conditionBuilder.toString();
			ConnectionDao CommonDao = new ConnectionDao();
			Page p = CommonDao.getPageDataByHql("DYAQ", strQuery, page, rows);
			
			m.setTotal(p.getTotalCount());
			m.setRows(p.getResult());
			} catch (Exception e) {
				System.err.println("查询发生异常！");
			}	
		return m;
	}

}
