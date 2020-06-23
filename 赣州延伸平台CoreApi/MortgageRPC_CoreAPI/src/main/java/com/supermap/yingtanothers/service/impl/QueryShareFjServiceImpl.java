package com.supermap.yingtanothers.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.workflow.util.Message;
import com.supermap.yingtanothers.pool.ConnectionDao;
import com.supermap.yingtanothers.service.QueryShareFjService;

/**
 * 
 * 作者： 苗利涛
 * 时间： 2016年4月13日 下午12:49:03
 * 功能：鹰潭市查询共享附件实现类
 */
@Service("QueryShareFjService")
public class QueryShareFjServiceImpl implements QueryShareFjService{

	@Autowired
	private CommonDao dao;
	
	@Override
	public Message queryQlxxInfoByGXXMBH(String file_number,String qlr, String ywh, Integer page, Integer rows) {
		Message m = null;
		ConnectionDao CommonDao = null;
		Page p = null;
		String djlx = null;
		try {
			m = new Message();
			
			if(!StringHelper.isEmpty(file_number)){
				djlx = QueryDJLXByProjectId(file_number);
			}
			StringBuilder conditionBuilder = new StringBuilder("1 = 1");
			if(!StringHelper.isEmpty(qlr) && !StringHelper.isEmpty(ywh) && !StringHelper.isEmpty(djlx)){
																
				// 首次登记--抵押
				if (djlx.equals("100")) {
					conditionBuilder.append(" AND DYR LIKE '%").append(qlr).append("%' AND GXXMBH LIKE '%").append(ywh).append("%'");
					CommonDao = new ConnectionDao();
					String strQuery = conditionBuilder.toString();
					p = CommonDao.getShareFjDataByHql("DYAQ", strQuery, page, rows);
					
					if(p.getTotalCount() == 0){
						conditionBuilder = new StringBuilder("1 = 1");
						conditionBuilder.append(" AND QLRMC LIKE '%").append(qlr).append("%' AND GXXMBH LIKE '%").append(ywh).append("%'");
						CommonDao = new ConnectionDao();
						String strQuery1 = conditionBuilder.toString();
						p = CommonDao.getShareFjDataByHql("QLR", strQuery1, page, rows);
					}
					
				// 转移登记
				}else if(djlx.equals("200")){
					conditionBuilder.append(" AND QLRMC LIKE '%").append(qlr).append("%' AND GXXMBH LIKE '%").append(ywh).append("%'");
					CommonDao = new ConnectionDao();
					String strQuery = conditionBuilder.toString();
					p = CommonDao.getShareFjDataByHql("QLR", strQuery, page, rows);
				
				// 变更登记 或  其他登记
				}else if(djlx.equals("300") || djlx.equals("900")){
					conditionBuilder.append(" AND QLRMC LIKE '%").append(qlr).append(" AND GXXMBH LIKE '%").append(ywh).append("%'");
					CommonDao = new ConnectionDao();
					String strQuery = conditionBuilder.toString();
					p = CommonDao.getShareFjDataByHql("QLR", strQuery, page, rows);	
				
				//预告登记
				}else if(djlx.equals("700")){
					conditionBuilder.append(" AND YWR LIKE '%").append(qlr).append("%' AND GXXMBH LIKE '%").append(ywh).append("%'");
					CommonDao = new ConnectionDao();
					String strQuery = conditionBuilder.toString();
					p = CommonDao.getShareFjDataByHql("YGDJ", strQuery, page, rows);
				}
			}else if (!StringHelper.isEmpty(qlr) && StringHelper.isEmpty(ywh) && !StringHelper.isEmpty(djlx)) {
								
				// 首次登记--抵押
				if (djlx.equals("100")) {
					conditionBuilder.append(" AND DYR LIKE '%").append(qlr).append("%'");
					CommonDao = new ConnectionDao();
					String strQuery = conditionBuilder.toString();
					p = CommonDao.getShareFjDataByHql("DYAQ", strQuery, page, rows);
					
					if(p.getTotalCount() == 0){
						conditionBuilder = new StringBuilder("1 = 1");
						conditionBuilder.append(" AND QLRMC LIKE '%").append(qlr).append("%'");
						CommonDao = new ConnectionDao();
						String strQuery1 = conditionBuilder.toString();
						p = CommonDao.getShareFjDataByHql("QLR", strQuery1, page, rows);
					}
					
				// 转移登记
				}else if(djlx.equals("200")){
					conditionBuilder.append(" AND QLRMC LIKE '%").append(qlr).append("%'");
					CommonDao = new ConnectionDao();
					String strQuery = conditionBuilder.toString();
					p = CommonDao.getShareFjDataByHql("QLR", strQuery, page, rows);
					
				// 变更登记 或 其他登记
				}else if(djlx.equals("300") || djlx.equals("900")){
					conditionBuilder.append(" AND GXXMBH LIKE '%").append(ywh).append("%'");
					CommonDao = new ConnectionDao();
					String strQuery = conditionBuilder.toString();
					p = CommonDao.getShareFjDataByHql("QLR", strQuery, page, rows);	
					
				//预告登记
				}else if(djlx.equals("700")){
					conditionBuilder.append(" AND YWR LIKE '%").append(qlr).append("%'");
					CommonDao = new ConnectionDao();
					String strQuery = conditionBuilder.toString();
					p = CommonDao.getShareFjDataByHql("YGDJ", strQuery, page, rows);
				}
				
			}else if (StringHelper.isEmpty(qlr) && !StringHelper.isEmpty(ywh) && !StringHelper.isEmpty(djlx)) {
				
				// 首次登记--抵押
				if (djlx.equals("100")) {
					conditionBuilder.append(" AND GXXMBH LIKE '%").append(ywh).append("%'");
					CommonDao = new ConnectionDao();
					String strQuery = conditionBuilder.toString();
					p = CommonDao.getShareFjDataByHql("DYAQ", strQuery, page, rows);	
					
					if(p.getTotalCount() == 0){
						conditionBuilder = new StringBuilder("1 = 1");
						conditionBuilder.append(" AND GXXMBH LIKE '%").append(ywh).append("%'");
						CommonDao = new ConnectionDao();
						String strQuery1 = conditionBuilder.toString();
						p = CommonDao.getShareFjDataByHql("QLR", strQuery1, page, rows);
					}
					
				// 转移登记
				}else if(djlx.equals("200")){
					conditionBuilder.append(" AND GXXMBH LIKE '%").append(ywh).append("%'");
					CommonDao = new ConnectionDao();
					String strQuery = conditionBuilder.toString();
					p = CommonDao.getShareFjDataByHql("QLR", strQuery, page, rows);
				
				// 变更登记 或 其他登记
				}else if(djlx.equals("300") || djlx.equals("900")){
					conditionBuilder.append(" AND GXXMBH LIKE '%").append(ywh).append("%'");
					CommonDao = new ConnectionDao();
					String strQuery = conditionBuilder.toString();
					p = CommonDao.getShareFjDataByHql("QLR", strQuery, page, rows);	
					
					
				//预告登记
				}else if(djlx.equals("700")){
					conditionBuilder.append(" AND GXXMBH LIKE '%").append(ywh).append("%'");
					CommonDao = new ConnectionDao();
					String strQuery = conditionBuilder.toString();
					p = CommonDao.getShareFjDataByHql("YGDJ", strQuery, page, rows);
				}
				
			}								
			if (p != null) {
				m.setTotal(p.getTotalCount());				
				m.setRows(p.getResult());
			}			
			
		} catch (Exception e) {
			System.err.println("查询共享附件发生异常");
		}
		
		return m;
	}

	//私有的查询登记类型的方法
	private String QueryDJLXByProjectId(String file_number) {
		String djlx = null;
		try {
			StringBuilder conditionBuilder = new StringBuilder("1 = 1");
			conditionBuilder.append(" AND PROJECT_ID = '").append(file_number).append("'");
			String strQuery = conditionBuilder.toString();
			List<BDCS_XMXX> xmxxlist = dao.getDataList(BDCS_XMXX.class, strQuery);
			if (xmxxlist.size()>0) {
				djlx = xmxxlist.get(0).getDJLX();
			}
		} catch (Exception e) {
			System.out.println("查询登记类型失败");
		}
		
		
		return djlx;		
	}
	
	@Override
	public ResultMessage saveCasenumByYwh(String gxxmbh, String ywh) {
		ResultMessage msg = null;
		try {
			msg = new ResultMessage();			
			StringBuilder conditionBuilder = new StringBuilder("1 = 1");
			if (!StringHelper.isEmpty(ywh)) {
				conditionBuilder.append(" AND YWH = '").append(ywh).append("'");
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
						StringBuilder conditionBuilder1 = new StringBuilder("1 = 1");
						conditionBuilder1.append(" AND FSQLID = '").append(fSQLID).append("'");
						String strQuery1 = conditionBuilder1.toString();
						List<BDCS_FSQL_GZ> fsqllist = dao.getDataList(BDCS_FSQL_GZ.class, strQuery1);
						if (fsqllist.size() > 0) {
							for (BDCS_FSQL_GZ bdcs_fsql_gz : fsqllist) {
								bdcs_fsql_gz.setCASENUM(gxxmbh);
								dao.update(bdcs_fsql_gz);
								dao.flush();
							}
							
						}						
					}				
					msg.setSuccess("true");
					msg.setMsg("推送房产库成功！");	
				}else {
					msg.setSuccess("false");
					msg.setMsg("推送房产库失败！");
				}				
			}
			
		} catch (Exception e) {
			YwLogUtil.addYwLog("更新BDCS_QL_GZ和BDCS_FSQL_GZ的casenum字段-失败", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
			msg.setSuccess("false");
			msg.setMsg("推送房产库失败！");
		}								
	
		return msg;
	}

}
