package com.supermap.yingtanothers.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.service.ProjectService;
import com.supermap.realestate.registration.service.QLService;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.workflow.util.Message;
import com.supermap.yingtanothers.model.FDCQ;
import com.supermap.yingtanothers.model.QLR;
import com.supermap.yingtanothers.pool.ConnectionDao;
import com.supermap.yingtanothers.service.QueryQLRService;

/**
 * 
 * 作者： 苗利涛
 * 时间： 2016年1月18日 下午8:38:40
 * 功能：根据权利人姓名查询权利人实现类
 */
@Service("QueryQLRService")
public class QueryQLRServiceImpl implements QueryQLRService{

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private CommonDao dao;
	
	@Autowired
	private QLService qlService;
	
	public Message QueryQLRByQlrxm(String sqrxm, Integer page, Integer rows) {
		Message m = null;
		try {
			m = new Message();
			StringBuilder conditionBuilder = new StringBuilder("1 = 1");
			if (!StringHelper.isEmpty(sqrxm)) {
				conditionBuilder.append(" AND QLRMC LIKE '%").append(sqrxm)
						.append("%'");
			}
			String strQuery = conditionBuilder.toString();
			ConnectionDao CommonDao = new ConnectionDao();
			Page p = CommonDao.getPageDataByHql("QLR", strQuery, page, rows);
			
			m.setTotal(p.getTotalCount());
			m.setRows(p.getResult());
		} catch (Exception e) {
			System.err.println("查询发生异常！");
		}
		return m;
	}

	@Override
	public ResultMessage QueryQLRByGxxmbh(String gxxmbh,String xmbh) {
		ResultMessage message = new ResultMessage();
		BDCS_SQR sqr = null;
		ConnectionDao CommonDao = null;
		try {			
			StringBuilder conditionBuilder = new StringBuilder("1 = 1");
			if (!StringHelper.isEmpty(gxxmbh)) {
				conditionBuilder.append(" AND GXXMBH = '").append(gxxmbh)
						.append("'");
			}
			String strQuery = conditionBuilder.toString();
			StringBuilder  sqlBuilder = new StringBuilder("1 = 1");
			if(!StringHelper.isEmpty(xmbh)){
				sqlBuilder.append(" AND XMBH = '").append(xmbh).append("'");
			}
			String strQuery1 = sqlBuilder.toString();
			List<BDCS_SQR> bdcs_sqr = dao.getDataList(BDCS_SQR.class, strQuery1);			
			String qlid = bdcs_sqr.get(0).getGLQLID();
			CommonDao = new ConnectionDao();
			List<QLR> list = CommonDao.GetQlrs("QLR", strQuery);
			if(list.size() > 0){
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) != null) {
					sqr = new BDCS_SQR();
					sqr.setXMBH(xmbh);
					sqr.setSQRXM(list.get(i).getQLRMC());
					sqr.setXB(list.get(i).getXB());
					sqr.setZJLX(list.get(i).getZJZL());
					sqr.setZJH(list.get(i).getZJH());
					sqr.setFZJG(list.get(i).getFZJG());
					sqr.setGJDQ(list.get(i).getGJ());
					sqr.setHJSZSS(list.get(i).getHJSZSS());
					sqr.setGZDW(list.get(i).getGZDW());
					sqr.setSSHY(list.get(i).getSSHY());
					sqr.setLXDH(list.get(i).getDH());
					sqr.setTXDZ(list.get(i).getDZ());
					sqr.setYZBM(list.get(i).getYB());
					sqr.setDZYJ(list.get(i).getDZYJ());
					sqr.setSQRLX(list.get(i).getQLRLX());
					sqr.setSXH(list.get(i).getSXH().toString());
					sqr.setQLBL(list.get(i).getQLBL());
					sqr.setGYFS(list.get(i).getGYFS());
					sqr.setSQRLB("1");
					sqr.setISCZR(list.get(i).getISCZR());
										
					String sqrid = projectService.hasSQR(sqr.getXMBH(), sqr.getSQRXM(), sqr.getSQRLB(),sqr.getZJH());
					if (sqrid != null) {						
						YwLogUtil.addYwLog("从中间库抽取权利人信息-失败", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
					} else {
						sqrid = projectService.addSQRXX(sqr);						
						YwLogUtil.addYwLog("从中间库抽取权利人信息-成功", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
					}
				}
			}
			if(qlid != null && qlid != ""){
				List<FDCQ> fdcqsList = CommonDao.GetFdcqs("FDCQ", strQuery);
				FDCQ fdcq = fdcqsList.get(0);
				double lqdjg = fdcq.getFDCJYJG();
				boolean brebuild = false;
				BDCS_QL_GZ ql = qlService.GetQL(qlid);
				brebuild = !(!StringUtils.isEmpty(ql.getCZFS()));
				ql.setId(qlid);
//				ql.setQLQSSJ(fdcq.getTDSYQSSJ());
				ql.setQDJG(lqdjg);
//				ql.setQLJSSJ(fdcq.getTDSYJSSJ());
				ql.setDJYY(fdcq.getDJYY());
				ql.setFJ(fdcq.getFJ());
				ql.setCZFS("分别持证");
//				ql.setTDSHYQR(fdcq.getTDSHYQR());
				if (brebuild) {
					qlService.UpdateQLandRebuildRelation(ql);
				} else {
					qlService.UpdateQL(ql);
				}								
				YwLogUtil.addYwLog("提取房屋权利信息-成功", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);				
			}
			message.setSuccess("true");	
			message.setMsg("提取信息成功");
			}else {
				message.setSuccess("false");	
				message.setMsg("请输入正确的共享项目编号");
			}
		} catch (Exception e) {
			message.setSuccess("false");	
			message.setMsg("提取信息失败");
			System.err.println("从中间库抽取信息发生异常！");
		}
		return message;
	}

	@Override
	public ResultMessage QueryDYByGxxmbh(String gxxmbh, String xmbh) {
		ResultMessage message = new ResultMessage();
		BDCS_SQR sqr = null;
		ConnectionDao CommonDao = null;
		try {			
			StringBuilder conditionBuilder = new StringBuilder("1 = 1");
			if (!StringHelper.isEmpty(gxxmbh)) {
				conditionBuilder.append(" AND GXXMBH = '").append(gxxmbh)
						.append("'");
			}
			String strQuery = conditionBuilder.toString();
			StringBuilder  sqlBuilder = new StringBuilder("1 = 1");
			if(!StringHelper.isEmpty(xmbh)){
				sqlBuilder.append(" AND XMBH = '").append(xmbh).append("'");
			}
			String strQuery1 = sqlBuilder.toString();
			List<BDCS_SQR> bdcs_sqr = dao.getDataList(BDCS_SQR.class, strQuery1);			
			String qlid = bdcs_sqr.get(0).getGLQLID();
			CommonDao = new ConnectionDao();
			List<QLR> list = CommonDao.GetQlrs("QLR", strQuery);
			if(list.size() > 0){
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) != null) {
					sqr = new BDCS_SQR();
					sqr.setXMBH(xmbh);
					sqr.setSQRXM(list.get(i).getQLRMC());
					sqr.setXB(list.get(i).getXB());
					sqr.setZJLX(list.get(i).getZJZL());
					sqr.setZJH(list.get(i).getZJH());
					sqr.setFZJG(list.get(i).getFZJG());
					sqr.setGJDQ(list.get(i).getGJ());
					sqr.setHJSZSS(list.get(i).getHJSZSS());
					sqr.setGZDW(list.get(i).getGZDW());
					sqr.setSSHY(list.get(i).getSSHY());
					sqr.setLXDH(list.get(i).getDH());
					sqr.setTXDZ(list.get(i).getDZ());
					sqr.setYZBM(list.get(i).getYB());
					sqr.setDZYJ(list.get(i).getDZYJ());
					sqr.setSQRLX(list.get(i).getQLRLX());
					sqr.setSXH(list.get(i).getSXH().toString());
					sqr.setQLBL(list.get(i).getQLBL());
					sqr.setGYFS(list.get(i).getGYFS());
					sqr.setSQRLB("1");
					sqr.setISCZR(list.get(i).getISCZR());
										
					String sqrid = projectService.hasSQR(sqr.getXMBH(), sqr.getSQRXM(), sqr.getSQRLB(),sqr.getZJH());
					if (sqrid != null) {						
						YwLogUtil.addYwLog("从中间库抽取权利人信息-失败", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
					} else {
						sqrid = projectService.addSQRXX(sqr);						
						YwLogUtil.addYwLog("从中间库抽取权利人信息-成功", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
					}
				}
			}
			if(qlid != null && qlid != ""){
				List<FDCQ> fdcqsList = CommonDao.GetFdcqs("FDCQ", strQuery);
				FDCQ fdcq = fdcqsList.get(0);
				double lqdjg = fdcq.getFDCJYJG();
				boolean brebuild = false;
				BDCS_QL_GZ ql = qlService.GetQL(qlid);
				brebuild = !(!StringUtils.isEmpty(ql.getCZFS()));
				ql.setId(qlid);
//				ql.setQLQSSJ(fdcq.getTDSYQSSJ());
				ql.setQDJG(lqdjg);
//				ql.setQLJSSJ(fdcq.getTDSYJSSJ());
				ql.setDJYY(fdcq.getDJYY());
				ql.setFJ(fdcq.getFJ());
				ql.setCZFS("分别持证");
//				ql.setTDSHYQR(fdcq.getTDSHYQR());
				if (brebuild) {
					qlService.UpdateQLandRebuildRelation(ql);
				} else {
					qlService.UpdateQL(ql);
				}								
				YwLogUtil.addYwLog("提取房屋权利信息-成功", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);				
			}
			message.setSuccess("true");	
			message.setMsg("提取信息成功");
			}else {
				message.setSuccess("false");	
				message.setMsg("请输入正确的共享项目编号");
			}
		} catch (Exception e) {
			message.setSuccess("false");	
			message.setMsg("提取信息失败");
			System.err.println("从中间库抽取信息发生异常！");
		}
		return message;
	}
	
}
