package com.supermap.realestate.registration.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.supermap.realestate.registration.model.*;
import org.apache.poi.hssf.record.formula.functions.False;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.service.DXSendService;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.DXSENDUtil;
import com.supermap.realestate.registration.util.GetTokenUtil;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.services.components.Component;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;


@Service("dXSendService")
public class DXSendServicelmpl implements DXSendService {
	@Autowired
	private CommonDao baseCommonDao;
	
	/**
	 * 查询需要发送短信的项目
	 * @author liangc
	 * @date 2018-09-06 15:57:30
	 */
	@Override
	public Message getdxsendquery(Map<String, String> queryvalues, Integer page,
			Integer rows) {
		Message msg = new Message();
		long count = 0;
		StringBuilder builderWhereW = new StringBuilder();
		StringBuilder builderSelectS = new StringBuilder();
		StringBuilder builderWhereW_log = new StringBuilder();
		String ywlsh = "";
		String zsbh = "";
		String bdcqzh = "";
		for (Entry<String, String> ent : queryvalues.entrySet()) {
			String name = ent.getKey();
			String value = ent.getValue();
			if (StringHelper.isEmpty(value)){
				builderWhereW.append("");
				builderWhereW_log.append("");
			}
			if (!StringHelper.isEmpty(value)){
				if (name.equals("YWLSH")){
					ywlsh = ent.getValue();
					try {
						ywlsh =  new String(ywlsh.getBytes("iso8859-1"), "utf-8");
					} catch (UnsupportedEncodingException e) {
						ywlsh = "";
					}
					builderWhereW.append(" and  XMXX.YWLSH like '%" + ywlsh + "%'");
					builderWhereW_log.append(" and YWLSH like '%" + ywlsh + "%'");
				}
				if (name.equals("ZSBH")){
					zsbh = ent.getValue();
					try {
						zsbh =  new String(zsbh.getBytes("iso8859-1"), "utf-8");
					} catch (UnsupportedEncodingException e) {
						zsbh = "";
					}
					builderWhereW.append(" and  ZS.ZSBH like '%" + zsbh + "%'");
					builderWhereW_log.append(" and  ZSBH like '%" + zsbh + "%'");
				}
				if (name.equals("BDCQZH")){
					bdcqzh = ent.getValue();
					try {
						bdcqzh =  new String(bdcqzh.getBytes("iso8859-1"), "utf-8");
					} catch (UnsupportedEncodingException e) {
						bdcqzh= "";
					}
					builderWhereW.append(" and  QLR.BDCQZH like '%" + bdcqzh + "%'");
					builderWhereW_log.append(" and  BDCQZH like '%" + bdcqzh + "%'");
				}
				if (name.equals("DJSJ_Q")) {
					builderWhereW.append("  and XMXX.DJSJ >=to_date('"+queryvalues.get("DJSJ_Q")+"','yyyy-mm-dd') ");
					builderWhereW_log.append(" and DJSJ >=to_date('"+queryvalues.get("DJSJ_Q")+"','yyyy-mm-dd') ");
				}
				if (name.equals("DJSJ_Z")) {
					builderWhereW.append("   and XMXX.DJSJ <=(to_date('"+queryvalues.get("DJSJ_Z")+"','yyyy-mm-dd')+1) ");
					builderWhereW_log.append("  and DJSJ <=(to_date('"+queryvalues.get("DJSJ_Z")+"','yyyy-mm-dd')+1) ");
				}
				if (name.equals("SZSJ_Q")) {
					builderWhereW.append("  and DJSZ.SZSJ >=to_date('"+queryvalues.get("SZSJ_Q")+"','yyyy-mm-dd') ");
					builderWhereW_log.append("  and SZSJ >=to_date('"+queryvalues.get("SZSJ_Q")+"','yyyy-mm-dd') ");
				}
				if (name.equals("SZSJ_Z")) {
					builderWhereW.append("   and DJSZ.SZSJ <=(to_date('"+queryvalues.get("SZSJ_Z")+"','yyyy-mm-dd')+1) ");
					builderWhereW_log.append("   and SZSJ <=(to_date('"+queryvalues.get("SZSJ_Z")+"','yyyy-mm-dd')+1) ");
				}
			}
		}
		String fromSql = " from from BDCK.BDCS_XMXX XMXX "
				+ " LEFT JOIN BDCK.BDCS_QDZR_GZ QDZR ON QDZR.XMBH = XMXX.XMBH "
				+ " LEFT JOIN BDCK.BDCS_ZS_GZ ZS ON ZS.ZSID = QDZR.ZSID "
				+ " LEFT JOIN BDCK.BDCS_DJSZ DJSZ ON DJSZ.XMBH = XMXX.XMBH "
				+ " LEFT JOIN BDCK.BDCS_QLR_GZ QLR ON QLR.QLRID = QDZR.QLRID"
				+ " LEFT JOIN BDCK.BDCS_SQR SQR ON SQR.SQRID = QLR.SQRID "
				+ " where XMXX.SFDB='1' "
				+ builderWhereW.toString();
		builderSelectS.append("SELECT XMXX.YWLSH,XMXX.XMMC,XMXX.XMBH,XMXX.DJSJ,ZS.ZSBH,QLR.BDCQZH,DJSZ.SZSJ,SQR.SQRXM,SQR.LXDH,SQR.DLRXM,SQR.DLRLXDH,SQR.SQRID ");
		String fullSql = builderSelectS.toString() + fromSql;
		String fullSql_zdfs = "SELECT * FROM LOG.LOG_SENDMSG where SENDLX = '自动发送' "+builderWhereW_log.toString();
		String fullSql_sdfs = "SELECT * FROM LOG.LOG_SENDMSG where SENDLX = '手动发送' "+builderWhereW_log.toString();
		List<Map> Result = new ArrayList<Map>();
		if ("1".equals(queryvalues.get("CXFS"))) {
			count = baseCommonDao.getCountByFullSql("from ("+fullSql_zdfs+") t");
			Result= baseCommonDao.getPageDataByFullSql(fullSql_zdfs, page, rows);
		}else{
			count = baseCommonDao.getCountByFullSql("from ("+fullSql_sdfs+") t");
			if(count < 1){
				count = baseCommonDao.getCountByFullSql("from ("+fullSql+") t");
				Result= baseCommonDao.getPageDataByFullSql(fullSql, page, rows);
			}else{
				Result= baseCommonDao.getPageDataByFullSql(fullSql_sdfs, page, rows);
			}
		}
		
		List<Map> result_new = new ArrayList<Map>();
		if (Result != null && Result.size() > 0) {
			for (Map m : Result) {
				if (m.containsKey("SENDTIME")) {
					m.put("SENDTIME",StringHelper.FormatYmdhmsByDate(m.get("SENDTIME")));
				}
				result_new.add(m);
			}
		}
		
		msg.setTotal(count);
		msg.setRows(result_new);
		return msg;
	}
	/**
	 * 短信推送详情
	 * @author taochunda
	 * @date 2019-04-23 20:40:30
	 */
	@Override
	public Message getDxtsInfo(Map<String, String> queryvalues, Integer page,
			Integer rows) {
		Message msg = new Message();
		long count = 0;
		StringBuilder wheresql = new StringBuilder();
		wheresql.append("1=1");
		String ywlsh = "";
		String zsbh = "";
		String bdcqzh = "";
		for (Entry<String, String> ent : queryvalues.entrySet()) {
			String name = ent.getKey();
			String value = ent.getValue();
			if (!StringHelper.isEmpty(value)){
				if (name.equals("YWLSH")){
					ywlsh = ent.getValue();
					wheresql.append(" AND YWLSH LIKE '%" + ywlsh + "%'");
				}
				if (name.equals("ZSBH")){
					zsbh = ent.getValue();
					wheresql.append(" AND  ZSBH LIKE '%" + zsbh + "%'");
				}
				if (name.equals("BDCQZH")){
					bdcqzh = ent.getValue();
					wheresql.append(" AND  BDCQZH LIKE '%" + bdcqzh + "%'");
				}
				if (name.equals("DJSJ_Q")) {
					wheresql.append(" AND DJSJ >=TO_DATE('"+queryvalues.get("DJSJ_Q")+"','YYYY-MM-DD') ");
				}
				if (name.equals("DJSJ_Z")) {
					wheresql.append("  AND DJSJ <=(TO_DATE('"+queryvalues.get("DJSJ_Z")+"','YYYY-MM-DD')+1) ");
				}
				if (name.equals("SZSJ_Q")) {
					wheresql.append("  AND SZSJ >=TO_DATE('"+queryvalues.get("SZSJ_Q")+"','YYYY-MM-DD') ");
				}
				if (name.equals("SZSJ_Z")) {
					wheresql.append("   AND SZSJ <=(TO_DATE('"+queryvalues.get("SZSJ_Z")+"','YYYY-MM-DD')+1) ");
				}
			}
		}
		List<LOG_DXTS> list = baseCommonDao.findList(LOG_DXTS.class, wheresql.toString());

		msg.setTotal(list.size());
		msg.setRows(list);
		return msg;
	}

	/**
	 * 单条短信发送
	 * @param xmbh
	 * @param sqrid
	 * @param cxfs
	 * @param lxdh
	 * @param dlrlxdh
	 * @return
	 * @throws ConnectException
	 * @throws MalformedURLException
	 */
	public Message dtdxsend(String xmbh,String sqrid,String cxfs) throws ConnectException, MalformedURLException {
			return dxsend(xmbh,sqrid,cxfs);
	}
	
	
	/**
	 * 批量发送短信
	 * @throws MalformedURLException 
	 * @throws ConnectException 
	 */
	public Message pldxsend(String sqrids,String cxfs) throws ConnectException, MalformedURLException {
		Message msg = new Message();
		StringBuilder  msgs = new StringBuilder();
		if (StringHelper.isEmpty(sqrids)) {
			msg.setMsg("申请人不能为空！");
			return msg;
		}
		String[] ids = sqrids.split(",");
		ArrayList list=new ArrayList(); 
	    for(int i=0;i<ids.length;i++){ 
	       if(!list.contains(ids[i])) 
	           list.add(ids[i]);       
	    }
	    if(list != null && list.size()>0){
	    	for(int i=0;i<list.size();i++){ 
				BDCS_SQR sqr = baseCommonDao.get(BDCS_SQR.class, list.get(i).toString());
				msg = dxsend(sqr.getXMBH(),sqr.getId(),cxfs);
				msgs.append(msg.toString());
	 	    }
	    }
			
		
		msg.setMsg(msgs.toString());
		return msg;
	}
	
	
	/**
	 * 发送短信
	 * @author liangc
	 * @throws MalformedURLException 
	 * @throws ConnectException 
	 */
	@Override
	public Message dxsend(String xmbh,String sqrid,String cxfs) throws ConnectException, MalformedURLException {
		Message msg = new Message();
		String tokenstr="";
		try {
			tokenstr = GetTokenUtil.getAccessToken();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		boolean issuccessqlr = false;
		boolean issuccessdlr = false;
		String qhmc =ConfigHelper.getNameByValue("XZQHMC");
		User user = Global.getCurrentUserInfo();
		String senduser="";
		if(user != null){
			senduser = user.getLoginName();
		}
		if(cxfs.equals("1")){
				String s = "SELECT XMXX.YWLSH,XMXX.XMMC,XMXX.XMBH,XMXX.DJSJ,ZS.ZSBH,QLR.BDCQZH,DJSZ.SZSJ,SQR.SQRXM,SQR.LXDH,SQR.DLRXM,SQR.DLRLXDH,SQR.SQRID"
							+ " from BDCK.BDCS_XMXX XMXX "
							+ " LEFT JOIN BDCK.BDCS_QDZR_GZ QDZR ON QDZR.XMBH = XMXX.XMBH "
							+ " LEFT JOIN BDCK.BDCS_ZS_GZ ZS ON ZS.XMBH = QDZR.XMBH "
							+ " LEFT JOIN BDCK.BDCS_DJSZ DJSZ ON DJSZ.XMBH = XMXX.XMBH "
							+ " LEFT JOIN BDCK.BDCS_QLR_GZ QLR ON QLR.QLRID = QDZR.QLRID "
							+ " LEFT JOIN BDCK.BDCS_SQR SQR ON SQR.SQRID = QLR.SQRID "
							+ " WHERE XMXX.XMBH='"+xmbh+"'"+" and SQR.SQRID='"+sqrid+"'";
				List<Map> xms = baseCommonDao.getDataListByFullSql(s);
				if(xms != null && xms.size()>0){
					for(Map xm: xms){
						LOG_SENDMSG dxlog = new LOG_SENDMSG();
						dxlog.setSQRXM(StringHelper.formatObject(xm.get("SQRXM")));
						dxlog.setLXDH(StringHelper.formatObject(xm.get("LXDH")));
						dxlog.setDLRXM(StringHelper.formatObject(xm.get("DLRXM")));
						dxlog.setDLRLXDH(StringHelper.formatObject(xm.get("DLRLXDH")));
						dxlog.setYWLSH(StringHelper.formatObject(xm.get("YWLSH")));
						dxlog.setXMMC(StringHelper.formatObject(xm.get("XMMC")));
						dxlog.setZSBH(StringHelper.formatObject(xm.get("ZSBH")));
						dxlog.setBDCQZH(StringHelper.formatObject(xm.get("BDCQZH")));
						dxlog.setDJSJ((Date) xm.get("DJSJ"));
						dxlog.setSZSJ((Date) xm.get("SZSJ"));
						dxlog.setSENDTIME(new Date());
						dxlog.setSENDSQRSTATUS("发送成功");
						dxlog.setSENDLX("自动发送");
						dxlog.setXMBH(StringHelper.formatObject(xm.get("XMBH")));
						dxlog.setSQRID(StringHelper.formatObject(xm.get("SQRID")));
						dxlog.setSENDUSER(senduser);
						if(!StringHelper.isEmpty(xm.get("LXDH"))){
							String content = "Content=[\""+xm.get("SQRXM")+"\",\""+xm.get("YWLSH")+"\"]";
							content += "&PlanOfSendTime=null";
							content += "&ToStaff="+xm.get("SQRXM");
							content += "&ToStaffPhone="+xm.get("LXDH");
							content += "&Level=4";
							content += "&SendImmediately=true";
							content += "&TemplateCode=287031";
							issuccessqlr = DXSENDUtil.readContentFromPost(content,tokenstr);
							if(issuccessqlr == true){
									dxlog.setSENDSQRSTATUS("发送成功");
									baseCommonDao.save(dxlog);
									baseCommonDao.flush();
							}else{
								dxlog.setSENDSQRSTATUS("发送失败");
								dxlog.setSENDLX("自动发送");
								baseCommonDao.save(dxlog);
								baseCommonDao.flush();
							}			
						}
						if(!StringHelper.isEmpty(xm.get("DLRLXDH"))){
							String content = "Content=[\""+xm.get("DLRXM")+"\",\""+xm.get("YWLSH")+"\"]";
							content += "&PlanOfSendTime=null";
							content += "&ToStaff="+xm.get("DLRXM");
							content += "&ToStaffPhone="+xm.get("DLRLXDH");
							content += "&Level=4";
							content += "&SendImmediately=true";
							content += "&TemplateCode=287031";
							issuccessdlr = DXSENDUtil.readContentFromPost(content,tokenstr);
							if(issuccessdlr == true){
								dxlog.setSENDDLRSTATUS("发送成功");
								baseCommonDao.save(dxlog);
								baseCommonDao.flush();
							}else{
								dxlog.setSENDDLRSTATUS("发送失败");
								baseCommonDao.save(dxlog);
								baseCommonDao.flush();
								
							}
						}
						if(issuccessqlr == true&&issuccessdlr == true){
							msg.setMsg("申请人短信发送成功，代理人短信发送成功！");
							msg.setSuccess("true");
						}else if(issuccessqlr == false&&issuccessdlr == false){
							msg.setMsg("申请人短信发送失败，代理人短信发送失败！");
							msg.setSuccess("false");
						}else if(issuccessqlr == true&&issuccessdlr == false){
							msg.setMsg("申请人短信发送成功，代理人短信发送失败！");
							msg.setSuccess("true");
						}else if(issuccessqlr == false&&issuccessdlr == true){
							msg.setMsg("申请人短信发送失败，代理人短信发送成功！");
							msg.setSuccess("true");
						}
					}
				}
		}else if(cxfs.equals("2")){

			String sqlsqr = "XMBH ='"+xmbh+"' AND SQRID ='"+sqrid+"' AND SENDLX='手动发送' ";
			List<LOG_SENDMSG> dxloglist = baseCommonDao.getDataList(LOG_SENDMSG.class, sqlsqr);
			if(dxloglist != null && dxloglist.size()>0){
					
					if(!StringHelper.isEmpty(dxloglist.get(0).getLXDH())){
						String content = "Content=[\""+dxloglist.get(0).getYWLSH()+"\",\""+qhmc+"不动产登记中心"+"\"]";
						content += "&PlanOfSendTime=null";
						content += "&ToStaff="+dxloglist.get(0).getSQRXM();
						content += "&ToStaffPhone="+dxloglist.get(0).getLXDH();
						content += "&Level=1";
						content += "&SendImmediately=true";
						content += "&TemplateCode=102555";
						issuccessqlr = DXSENDUtil.readContentFromPost(content,tokenstr);
						if(issuccessqlr == true){
							dxloglist.get(0).setSENDTIME(new Date());
							dxloglist.get(0).setBZ("重新发送");
							dxloglist.get(0).setSENDUSER(senduser);
							dxloglist.get(0).setSENDSQRSTATUS("发送成功");
							baseCommonDao.update(dxloglist.get(0));
							baseCommonDao.flush();
							msg.setMsg("发送成功");
							msg.setSuccess("true");
						}else{
							dxloglist.get(0).setSENDTIME(new Date());
							dxloglist.get(0).setBZ("重新发送");
							dxloglist.get(0).setSENDUSER(senduser);
							dxloglist.get(0).setSENDSQRSTATUS("发送失败");
							baseCommonDao.update(dxloglist.get(0));
							baseCommonDao.flush();
							msg.setMsg("发送失败");
							msg.setSuccess("false");
						}			
					}
					if(!StringHelper.isEmpty(dxloglist.get(0).getDLRLXDH())){
						String content = "Content=[\""+dxloglist.get(0).getYWLSH()+"\",\""+qhmc+"不动产登记中心"+"\"]";
						content += "&PlanOfSendTime=null";
						content += "&ToStaff="+dxloglist.get(0).getDLRXM();
						content += "&ToStaffPhone="+dxloglist.get(0).getDLRLXDH();
						content += "&Level=1";
						content += "&SendImmediately=true";
						content += "&TemplateCode=102555";
						issuccessdlr = DXSENDUtil.readContentFromPost(content,tokenstr);
						if(issuccessdlr == true){
							dxloglist.get(0).setSENDTIME(new Date());
							dxloglist.get(0).setBZ("重新发送");
							dxloglist.get(0).setSENDUSER(senduser);
							dxloglist.get(0).setSENDDLRSTATUS("发送成功");
							baseCommonDao.update(dxloglist.get(0));
							baseCommonDao.flush();
							msg.setMsg("发送成功");
							msg.setSuccess("true");
						}else{
							dxloglist.get(0).setSENDTIME(new Date());
							dxloglist.get(0).setBZ("重新发送");
							dxloglist.get(0).setSENDUSER(senduser);
							dxloglist.get(0).setSENDDLRSTATUS("发送失败");
							baseCommonDao.update(dxloglist.get(0));
							baseCommonDao.flush();
							msg.setMsg("发送失败");
							msg.setSuccess("false");
						}
					}
			}else{
				String Sql = " SELECT XMXX.YWLSH,XMXX.XMMC,XMXX.XMBH,XMXX.DJSJ,ZS.ZSBH,QLR.BDCQZH,DJSZ.SZSJ,SQR.SQRXM,SQR.LXDH,SQR.DLRXM,SQR.DLRLXDH,SQR.SQRID"
						+ " from from BDCK.BDCS_XMXX XMXX "
						+ " LEFT JOIN BDCK.BDCS_QDZR_GZ QDZR ON QDZR.XMBH = XMXX.XMBH "
						+ " LEFT JOIN BDCK.BDCS_ZS_GZ ZS ON ZS.ZSID = QDZR.ZSID "
						+ " LEFT JOIN BDCK.BDCS_DJSZ DJSZ ON DJSZ.XMBH = XMXX.XMBH "
						+ " LEFT JOIN BDCK.BDCS_QLR_GZ QLR ON QLR.QLRID = QDZR.QLRID"
						+ " LEFT JOIN BDCK.BDCS_SQR SQR ON SQR.SQRID = QLR.SQRID "
						+ " where XMXX.SFDB='1' and XMXX.XMBH='"+xmbh+"'"+" and SQR.SQRID='"+sqrid+"'";
				List<Map> xms = baseCommonDao.getDataListByFullSql(Sql);
				if(xms != null && xms.size()>0){
					for(Map xm: xms){
						LOG_SENDMSG dxlog = new LOG_SENDMSG();
						dxlog.setSQRXM(StringHelper.formatObject(xm.get("SQRXM")));
						dxlog.setLXDH(StringHelper.formatObject(xm.get("LXDH")));
						dxlog.setDLRXM(StringHelper.formatObject(xm.get("DLRXM")));
						dxlog.setDLRLXDH(StringHelper.formatObject(xm.get("DLRLXDH")));
						dxlog.setYWLSH(StringHelper.formatObject(xm.get("YWLSH")));
						dxlog.setXMMC(StringHelper.formatObject(xm.get("XMMC")));
						dxlog.setZSBH(StringHelper.formatObject(xm.get("ZSBH")));
						dxlog.setBDCQZH(StringHelper.formatObject(xm.get("BDCQZH")));
						dxlog.setDJSJ((Date) xm.get("DJSJ"));
						dxlog.setSZSJ((Date) xm.get("SQRXM"));
						dxlog.setSENDTIME(new Date());
						dxlog.setSENDSQRSTATUS("发送成功");
						dxlog.setSENDLX("手动发送");
						dxlog.setXMBH(StringHelper.formatObject(xm.get("XMBH")));
						dxlog.setSQRID(StringHelper.formatObject(xm.get("SQRID")));
						dxlog.setSENDUSER(senduser);
						if(!StringHelper.isEmpty(xm.get("LXDH"))){
							String content = "Content=[\""+xm.get("YWLSH")+"\",\""+qhmc+"不动产登记中心"+"\"]";
							content += "&PlanOfSendTime=null";
							content += "&ToStaff="+xm.get("SQRXM");
							content += "&ToStaffPhone="+xm.get("LXDH");
							content += "&Level=1";
							content += "&SendImmediately=true";
							content += "&TemplateCode=102555";
							issuccessqlr = DXSENDUtil.readContentFromPost(content,tokenstr);
							if(issuccessqlr == true){
									dxlog.setSENDSQRSTATUS("发送成功");
									baseCommonDao.save(dxlog);
									baseCommonDao.flush();
							}else{
								dxlog.setSENDSQRSTATUS("发送失败");
								dxlog.setSENDLX("手动发送");
								baseCommonDao.save(dxlog);
								baseCommonDao.flush();
							}			
						}
						if(!StringHelper.isEmpty(xm.get("DLRLXDH"))){
							String content = "Content=[\""+xm.get("YWLSH")+"\",\""+qhmc+"不动产登记中心"+"\"]";
							content += "&PlanOfSendTime=null";
							content += "&ToStaff="+xm.get("DLRXM");
							content += "&ToStaffPhone="+xm.get("DLRLXDH");
							content += "&Level=1";
							content += "&SendImmediately=true";
							content += "&TemplateCode=102555";
							issuccessdlr = DXSENDUtil.readContentFromPost(content,tokenstr);
							if(issuccessdlr == true){
								dxlog.setSENDDLRSTATUS("发送成功");
								baseCommonDao.save(dxlog);
								baseCommonDao.flush();
							}else{
								dxlog.setSENDDLRSTATUS("发送失败");
								baseCommonDao.save(dxlog);
								baseCommonDao.flush();
								
							}
						}
						if(issuccessqlr == true&&issuccessdlr == true){
							msg.setMsg("申请人短信发送成功，代理人短信发送成功！");
							msg.setSuccess("true");
						}else if(issuccessqlr == false&&issuccessdlr == false){
							msg.setMsg("申请人短信发送失败，代理人短信发送失败！");
							msg.setSuccess("false");
						}else if(issuccessqlr == true&&issuccessdlr == false){
							msg.setMsg("申请人短信发送成功，代理人短信发送失败！");
							msg.setSuccess("false");
						}else if(issuccessqlr == false&&issuccessdlr == true){
							msg.setMsg("申请人短信发送失败，代理人短信发送成功！");
							msg.setSuccess("false");
						}
					}
				}
			}
		
		}
		
			
		
		return msg;
	}

	@Override
	public void dxsendForHouseAndTax(String file_number,String ywh)
			{
		Message msg = new Message();
		String tokenstr="";

		List<Map> resMap = baseCommonDao.getDataListByFullSql("select WORKFLOWNAME from bdc_workflow.WFD_MAPPING maping where  instr('"+file_number+"','-'||maping.workflowcode||'-')>0");
	    if(resMap!=null&&resMap.size()>0)
	    {
			String htrue= ConfigHelper.getNameByValue("HOUSENETSET-PHONETRUE");
			String ttrue= ConfigHelper.getNameByValue("TAXSET-PHONETRUE");
			if(!StringHelper.formatObject(htrue).equals("1") && !StringHelper.formatObject(ttrue).equals("1")) {
				return ;
			}

			try {
				tokenstr = GetTokenUtil.getAccessToken();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	String wn=resMap.get(0).get("WORKFLOWNAME").toString();
	    	List<String> houseworkflowname=new ArrayList<String>();
	    	List<String> housephone=new ArrayList<String>();
	    	List<String> taxworkflowname=new ArrayList<String>();
	    	List<String> taxphone=new ArrayList<String>();
	    	//需发送的流程编号
			String hcodes= ConfigHelper.getNameByValue("HOUSENETSET-WORKFLOWCODE");
			String tcodes= ConfigHelper.getNameByValue("TAXSET-WORKFLOWCODE");
			//发送的电话号码
			String hphones= ConfigHelper.getNameByValue("HOUSENETSET-PHONE");
			String tphoness= ConfigHelper.getNameByValue("TAXSET-PHONE");
			//是否启动发送短信

			if(hcodes!=null&&hcodes.trim()!="")
			{
				for(String h:hcodes.split(","))
				{
					houseworkflowname.add(h);
				}
			}
			if(tcodes!=null&&tcodes.trim()!="")
			{
				for(String t:tcodes.split(","))
				{
					taxworkflowname.add(t);
				}
			}
			if(hphones!=null&&hphones.trim()!="")
			{
				for(String h:hphones.split(","))
				{
					housephone.add(h);
				}
			}
			if(tphoness!=null&&tphoness.trim()!="")
			{
				for(String t:tphoness.split(","))
				{
					taxphone.add(t);
				}
			}
			
			if(houseworkflowname.contains(wn)&&housephone.size()>0&&StringHelper.formatObject(htrue).equals("1"))
			{
				LOG_SENDMSG dxlog = new LOG_SENDMSG();
				dxlog.setSQRXM("通知房产部门");
				dxlog.setLXDH(org.apache.commons.lang.StringUtils.join(housephone.toArray(), ","));
				dxlog.setDLRXM("通知房产部门");
				dxlog.setDLRLXDH("通知房产部门");
				dxlog.setYWLSH(ywh);
				dxlog.setXMMC(ywh);
				dxlog.setZSBH("无");
				dxlog.setBDCQZH("无");
	
				dxlog.setSENDTIME(new Date());
				dxlog.setSENDSQRSTATUS("发送成功");
				dxlog.setSENDLX("自动发送，通知房产部门");
				dxlog.setXMBH(ywh);
				dxlog.setSQRID("无");
				dxlog.setSENDUSER("不动产登记中心");
				
				String content = "Content=[\""+ywh+"\"]";
				content += "&PlanOfSendTime=null";
				content += "&ToStaff=住建部门";
				content += "&ToStaffPhone="+ org.apache.commons.lang.StringUtils.join(housephone.toArray(), ",");
				content += "&Level=1";
				content += "&SendImmediately=true";
				content += "&TemplateCode=293450";
				try {
					if(DXSENDUtil.readContentFromPost(content,tokenstr)){
						dxlog.setSENDDLRSTATUS("发送成功");
						baseCommonDao.save(dxlog);
						baseCommonDao.flush();
					}else{
						dxlog.setSENDDLRSTATUS("发送失败");
						baseCommonDao.save(dxlog);
						baseCommonDao.flush();
						
					}
				} catch (ConnectException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			
			if(taxworkflowname.contains(wn)&&taxphone.size()>0&&StringHelper.formatObject(ttrue).equals("1"))
			{
				LOG_SENDMSG dxlog = new LOG_SENDMSG();
				dxlog.setSQRXM("通知税务部门");
				dxlog.setLXDH(org.apache.commons.lang.StringUtils.join(taxphone.toArray(), ","));
				dxlog.setDLRXM("通知税务部门");
				dxlog.setDLRLXDH("通知税务部门");
				dxlog.setYWLSH(ywh);
				dxlog.setXMMC(ywh);
				dxlog.setZSBH("无");
				dxlog.setBDCQZH("无");
	
				dxlog.setSENDTIME(new Date());
				dxlog.setSENDSQRSTATUS("发送成功");
				dxlog.setSENDLX("自动发送，通知税务部门");
				dxlog.setXMBH(ywh);
				dxlog.setSQRID("无");
				dxlog.setSENDUSER("不动产登记中心");
				
				String content = "Content=[\""+ywh+"\"]";
				content += "&PlanOfSendTime=null";
				content += "&ToStaff=税务部门";
				content += "&ToStaffPhone="+ org.apache.commons.lang.StringUtils.join(taxphone.toArray(), ",");
				content += "&Level=1";
				content += "&SendImmediately=true";
				content += "&TemplateCode=293451";
				try {
					if(DXSENDUtil.readContentFromPost(content,tokenstr)){
						dxlog.setSENDDLRSTATUS("发送成功");
						baseCommonDao.save(dxlog);
						baseCommonDao.flush();
					}else{
						dxlog.setSENDDLRSTATUS("发送失败");
						baseCommonDao.save(dxlog);
						baseCommonDao.flush();
						
					}
				} catch (ConnectException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	    }
	}
	
}
