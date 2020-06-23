package com.supermap.yingtanothers.service.impl;

import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.yingtanothers.pool.ConnectionDao;
import com.supermap.yingtanothers.service.QueryQLXXCService;
import com.supermap.wisdombusiness.workflow.util.Message;

/**
 * 
 * 作者： 苗利涛
 * 时间： 2016年1月19日 上午10:09:04
 * 功能：鹰潭市不动产通过不动产单元号和共享项目编号查询权利信息实现类
 */
@Service("QueryQLXXCService")
public class QueryQLXXCServiceImpl implements QueryQLXXCService{

	

	@Override
	public Message queryQlxxInfoByGXXMBH(String sqrxm, String bdcdyh, String gxxmbh, Integer page, Integer rows) {
		Message m = null;
		try {
			 m = new Message();

			StringBuilder conditionBuilder = new StringBuilder("1 = 1");
			if (!StringHelper.isEmpty(sqrxm)) {
				conditionBuilder.append(" AND TDSHYQR LIKE '%").append(sqrxm)
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
			Page p = CommonDao.getPageDataByHql("FDCQ", strQuery, page, rows);
			
			m.setTotal(p.getTotalCount());
			m.setRows(p.getResult());
			} catch (Exception e) {
				System.err.println("查询发生异常！");
			}	
		return m;
	}

}
