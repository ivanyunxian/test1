package com.supermap.realestate.registration.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.registration.constraint.ConstraintCheck;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.xmlmodel.BDCQLR;
import com.supermap.realestate.registration.model.xmlmodel.Message;
import com.supermap.realestate.registration.service.UploadAttachment;
import com.supermap.realestate.registration.service.XMLService;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.JH_DBHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;

@Component("xmlService")
public class XMLServiceImpl implements XMLService {

	@Autowired
	private CommonDao baseCommonDao;
	
	@Autowired
	private ConstraintCheck constraint_check;
	
	@Autowired
	private UploadAttachment upAttachment;
	@Override
	public boolean getXMLFromXTGX(String ywh, String xmbh) throws Exception {
		List<Message> message = readXMLFromXTGX(ywh);
		//将查封登记类型单独取出来
		StringBuilder strcf=new StringBuilder();
		strcf.append("select  djlx  from bdck.bdcs_xmxx   a   where  a.xmbh='").append(xmbh).append("'");
		ResultSet res=baseCommonDao.excuteQuery(strcf.toString());
		String djlx="";
		while(res.next()){
			djlx=res.getString("DJLX");
		}
		PreparedStatement PS = res.unwrap(PreparedStatement.class);
		res.close();
		if(PS!=null) {
			PS.close();
		}
		boolean flag = false;
		for(int i=0;i<message.size();i++){
			if(djlx.equals("800")){
				flag = saveMessage3DB(message.get(i), xmbh,ywh);
			}else{
				flag = saveMessage2DB(message.get(i), xmbh);
			}
		}
		return flag;
	}

	@SuppressWarnings("null")
	@Override
	public List<Message> readXMLFromXTGX(String ywh) throws Exception {
		// 读取xml内容的url
		String contractUrl =ConfigHelper.getNameByValue("JYDJ_CONTRACTURL");//改为从配置表读取 刘树峰2015年12月26日2点
		// 实例一个URL资源
		URL url = new URL(contractUrl + ywh);
		// 实例一个HTTP CONNECT
		HttpURLConnection connet = null;
		List<Message> message = new ArrayList<Message>() ;
		try {
			connet = (HttpURLConnection) url.openConnection();
			connet.setRequestProperty("Accept-Charset", "utf-8");
			connet.setRequestProperty("contentType", "utf-8");
			if (connet.getResponseCode() != 200) {
				throw new IOException(connet.getResponseMessage());
			}
			// 将返回的值存入到String中
			BufferedReader brd = new BufferedReader(new InputStreamReader(
					connet.getInputStream(), "utf-8"));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = brd.readLine()) != null) {
				sb.append(line);
			}
			brd.close();
			com.alibaba.fastjson.JSONArray jsonobj  = JSON.parseArray(sb.toString());
			if(jsonobj != null){
				for(int i=0;i<jsonobj.size();i++){
					Message mess = new Message();
					JSONObject obj =(JSONObject) jsonobj.get(i);
					mess = JSON.parseObject(obj.toJSONString(), Message.class);
					message.add(mess);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connet.disconnect();
		}
		return message;
	}

	public boolean saveMessage2DB(Message message, String xmbh) {
		String relationid = message.getHead().getRelationID(),bdcdyid1="";
		boolean flag = false;
		BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
		StringBuilder sb = new StringBuilder();
		sb.append(" relationid='"+ relationid +"'");
		List<BDCS_H_XZ> hs = baseCommonDao.getDataList(BDCS_H_XZ.class, sb.toString());
		if(hs !=null && hs.size()>0){
			bdcdyid1=hs.get(0).getId();
		}
		RealUnit _srcUnit = UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, bdcdyid1);

		if (_srcUnit != null) {
			Object[] sqrids = saveBDCS_SQR(message.getData().getBDCQLRS(), xmbh);
			String bdcdyid = _srcUnit.getId();
			ResultMessage ms = constraint_check.acceptCheckByBDCDYID(bdcdyid, xmbh);
			if("true".equals(ms.getSuccess())){
				DJHandler handler = HandlerFactory.createDJHandler(xmbh);
				handler.addBDCDY(bdcdyid);
				String str = MessageFormat.format(
						"XMBH=''{0}'' AND BDCDYID=''{1}''", xmbh, bdcdyid);
				List<BDCS_DJDY_GZ> listdjdy = baseCommonDao.getDataList(
						BDCS_DJDY_GZ.class, str);
				Rights _rights = RightsTools.loadRightsByDJDYID(DJDYLY.GZ, xmbh,
						listdjdy.get(0).getDJDYID());
				System.out.println(_rights.getId());
				if (_rights != null) {
					handler.addQLRbySQRArray(_rights.getId(), sqrids);
				}
				flag = true;
			}
		}
		return flag;
	}
	private String replaceBrackets(String strTarget){
		String strNew = "";
		if("".equals(strTarget) || strTarget == null)
			return strNew;
		strNew = strTarget.replace("[", "").replace("]", "").replace("【", "").replace("】", "");
		return strNew;
	}	
	
	/**
	 * 根据xml中的权利人信息保存申请人信息
	 * 
	 * @作者 OuZhanrong
	 * @创建时间 2015年8月8日下午4:46:17
	 * @param qlrs
	 * @param xmbh
	 */
	private Object[] saveBDCS_SQR(List<BDCQLR> qlrs, String xmbh) {
		List<String> sqrids = new ArrayList<String>();
		for (BDCQLR _qlr : qlrs) {
			BDCS_SQR sqr = new BDCS_SQR();
			String qlrmc = replaceBrackets(_qlr.getQLRMC());
			String zjh = replaceBrackets(_qlr.getZJH());
			if(qlrmc.contains("/")){
				String[] qlrmcs = qlrmc.split("/");				
				String[] zjhs = zjh.split("/");
				for(int i=0;i<qlrmcs.length;i++){
					sqr = new BDCS_SQR();
					sqr.setSQRXM(qlrmcs[i]);
					sqr.setXB(_qlr.getXB());
					sqr.setSQRLB("1");
					sqr.setSQRLX(_qlr.getQLRLX());
					sqr.setZJH(zjhs[i]);
					sqr.setZJLX("1");
					sqr.setGYFS("1");
					sqr.setXMBH(xmbh);
					sqr.setTXDZ(_qlr.getDZ());
					sqr.setLXDH(_qlr.getDH());
					baseCommonDao.save(sqr);
					baseCommonDao.flush();
					sqrids.add(sqr.getId());
				}
			}else{
				sqr.setSQRXM(qlrmc);
				sqr.setXB(_qlr.getXB());
				sqr.setSQRLB("1");
				sqr.setSQRLX(_qlr.getQLRLX());
				sqr.setZJH(zjh);
				sqr.setZJLX("1");
				sqr.setGYFS("0");
				sqr.setXMBH(xmbh);					
				sqr.setTXDZ(_qlr.getDZ());
				sqr.setLXDH(_qlr.getDH());
				baseCommonDao.save(sqr);
				baseCommonDao.flush();
				sqrids.add(sqr.getId());
			}
		}
		return sqrids.toArray();
	}
	//读取预查封时，买房人的基本信息
	public boolean saveMessage3DB(Message message, String xmbh,String hth) {
		String relationid = message.getHead().getRelationID(),
			   bdcdyid1 = "";
		boolean flag = false;
		BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
		StringBuilder sb = new StringBuilder();
		sb.append(" relationid='"+ relationid +"'");
		List<BDCS_H_XZY> hs = baseCommonDao.getDataList(BDCS_H_XZY.class, sb.toString());
		if(hs !=null && hs.size()>0){
			bdcdyid1=hs.get(0).getId();
		}
		RealUnit _srcUnit = UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, bdcdyid1);
		if (_srcUnit != null) {
			String bdcdyid = _srcUnit.getId();
			DJHandler handler = HandlerFactory.createDJHandler(xmbh);
			handler.addBDCDY(bdcdyid);
			String str = MessageFormat.format(
					"XMBH=''{0}'' AND BDCDYID=''{1}''", xmbh, bdcdyid);
			List<BDCS_DJDY_GZ> listdjdy = baseCommonDao.getDataList(
					BDCS_DJDY_GZ.class, str);
			Rights _rights = RightsTools.loadRightsByDJDYID(DJDYLY.GZ, xmbh,
					listdjdy.get(0).getDJDYID());
			Object[] sqrids = saveBDCS_SQRYCF(message.getData().getBDCQLRS(), xmbh,hth);
			if (_rights != null) {
				handler.addQLRbySQRArray(_rights.getId(), sqrids);
			}
			flag = true;
		}
		return flag;
	}

	private Object[] saveBDCS_SQRYCF(List<BDCQLR> qlrs, String xmbh,String hth) {
		List<String> sqrids = new ArrayList<String>();
		for (BDCQLR _qlr : qlrs) {
			BDCS_SQR sqr = new BDCS_SQR();
			String qlrmc = replaceBrackets(_qlr.getQLRMC());
			String zjh = replaceBrackets(_qlr.getZJH());
			if(qlrmc.contains("/")){
				String[] qlrmcs = qlrmc.split("/");				
				String[] zjhs = zjh.split("/");
				for(int i=0;i<qlrmcs.length;i++){
					sqr = new BDCS_SQR();
					sqr.setSQRXM(qlrmcs[i]);
					sqr.setXB(_qlr.getXB());
					sqr.setSQRLB("1");
					sqr.setSQRLX(_qlr.getQLRLX());
					sqr.setZJH(zjhs[i]);
					sqr.setZJLX("1");
					sqr.setGYFS("1");
					sqr.setXMBH(xmbh);
					sqr.setTXDZ(_qlr.getDZ());
					sqr.setLXDH(_qlr.getDH());
					sqr.setDLRZJHM("(合同号)"+hth);
					sqr.setDLRXM("买房人");
					baseCommonDao.save(sqr);
					baseCommonDao.flush();
					sqrids.add(sqr.getId());
				}
			}else{
				sqr.setSQRXM(qlrmc);
				sqr.setXB(_qlr.getXB());
				sqr.setSQRLB("1");
				sqr.setSQRLX(_qlr.getQLRLX());
				sqr.setZJH(zjh);
				sqr.setZJLX("1");
				sqr.setGYFS("0");
				sqr.setXMBH(xmbh);					
				sqr.setTXDZ(_qlr.getDZ());
				sqr.setLXDH(_qlr.getDH());
				sqr.setDLRZJHM("(合同号)"+hth);
				sqr.setDLRXM("买房人");
				baseCommonDao.save(sqr);
				baseCommonDao.flush();
				sqrids.add(sqr.getId());
			}
		}
		return sqrids.toArray();
	}
	
	/**
	 * 加载单元信息
	 */
	@Override
	public boolean CFDYFromShareDB(String bdcdyid1,String bdcdylx,String ywh,String xmbh)throws Exception{
		boolean flag = false;
		RealUnit _srcUnit = null;
		if(bdcdylx !=null){
			_srcUnit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.XZ, bdcdyid1);
			if (_srcUnit != null) {
				String bdcdyid = _srcUnit.getId();
				DJHandler handler = HandlerFactory.createDJHandler(xmbh);
				handler.addBDCDY(bdcdyid);
				String str = MessageFormat.format(
						"XMBH=''{0}'' AND BDCDYID=''{1}''", xmbh, bdcdyid);
				List<BDCS_DJDY_GZ> listdjdy = baseCommonDao.getDataList(
						BDCS_DJDY_GZ.class, str);
				Rights _rights = RightsTools.loadRightsByDJDYID(DJDYLY.GZ, xmbh,
						listdjdy.get(0).getDJDYID());
				Map<String,Object> map = getMCAndZH(ywh);
				Date cfkssj = new Date();
				Date cfjssj = new Date();
				String zxah = "";
				String cfjg = "";
				if(map!=null&&map.size()>0){
					cfkssj = (Date)map.get("KSSJ");
					cfjssj = (Date)map.get("JSSJ");
					zxah = map.get("ZXAH")==null?"":map.get("ZXAH").toString();
					cfjg = map.get("CFJG")==null?"":map.get("CFJG").toString();
				}
				_rights.setQLQSSJ(cfkssj);
				_rights.setQLJSSJ(cfjssj);
				_rights.setCASENUM(ywh+"&"+xmbh);
				baseCommonDao.save(_rights);
				SubRights subrights = RightsTools.loadSubRightsByRightsID(DJDYLY.GZ, _rights.getId());
				subrights.setCFJG(cfjg);
				subrights.setCFWH(zxah);
				baseCommonDao.save(subrights);
				BDCS_XMXX xmxx = baseCommonDao.get(BDCS_XMXX.class, xmbh);
				//System.out.println(_rights.getId());
				upAttachment.abstractFJFromZJK(xmxx.getYWLSH(), ywh);
				flag = true;
			}
	}
		return flag;
	}
	/**
	 * 通过合同号查找到的权利人信息，查找不动产单元类型和id
	 */
	@Override
	public Map<String,String> getLXAndID(String qlrmc,String qzh){
		String bdcdylx = "";
		String bdcdyid = "";
		StringBuilder str1 = new StringBuilder();
		str1.append("select djdy.bdcdylx,djdy.bdcdyid from bdck.bdcs_djdy_xz djdy ")
		.append(" left join (select hxz.bdcdyid as bdcdyid,'031' as bdcdylx from bdck.bdcs_h_xz hxz union ")
		.append(" select hxzy.bdcdyid as bdcdyid,'032' as bdcdylx from bdck.bdcs_h_xzy hxzy ")
		.append(")h on h.bdcdyid=djdy.bdcdyid and h.bdcdylx=djdy.bdcdylx left join bdck.bdcs_ql_xz ql on djdy.djdyid=ql.djdyid left join bdck.bdcs_qlr_xz qlr")
		.append(" on qlr.qlid=ql.qlid where h.bdcdyid is not null and qlr.qlrmc='").append(qlrmc).append("' and qlr.bdcqzh='").append(qzh).append("'");
		List<Map> list = baseCommonDao.getDataListByFullSql(str1.toString());
		if(list!=null&&list.size()>0){
			bdcdylx = list.get(0).get("BDCDYLX")==null?"":list.get(0).get("BDCDYLX").toString();
			bdcdyid = list.get(0).get("BDCDYID")==null?"":list.get(0).get("BDCDYID").toString();
		}
		Map<String ,String> m = new HashMap<String,String>();
		m.put("BDCDYLX", bdcdylx);
		m.put("BDCDYID", bdcdyid);
		return m;
		
		}
	/**
	 * 通过合同号查找到的权利人信息，查找不动产单元类型和id
	 */
	@Override
	public Map<String,Object> getMCAndZH(String htbh){
		Connection conn=null;
		conn=JH_DBHelper.getConnect_CXGXK();
		String qlrmc = "";
		String qzh = "";
		Date cfkssj = new Date();
		Date cfjssj = new Date();
		String zxah = "";
		String cfjg = "";
		StringBuilder str1 = new StringBuilder();
		str1.append("select bcxrxm,bdcqzh,SQCFKSSJ,SQCFJSSJ,xzzxtzswh,cxrssjgmc from sharesearch.queryapprove where rwlsh='")
		.append(htbh).append("'");
		ResultSet rs = null;
		try {
			rs = JH_DBHelper.excuteQuery(conn, str1.toString());
			while(rs.next()){
				qlrmc = rs.getString(1);
				qzh = rs.getString(2);
				cfkssj = rs.getDate(3);
				cfjssj = rs.getDate(4);
				zxah = rs.getString(5);
				cfjg = rs.getString(6);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			try {
				if(rs!=null)
					rs.close();
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		Map<String,Object> maps = new HashMap<String,Object>();
		maps.put("QLRMC", qlrmc);
		maps.put("QZH", qzh);
		maps.put("KSSJ", cfkssj);
		maps.put("JSSJ", cfjssj);
		maps.put("ZXAH", zxah);
		maps.put("CFJG", cfjg);
		return maps;
	}
	/**
	 * 判断项目是否为自动创建
	 */
	@Override
	public String pdAutoCreate(String xmbh){
		String casenum = "";
		StringBuilder str = new StringBuilder();
		str.append("select casenum from bdck.bdcs_ql_gz ql ")
		.append(" where xmbh='").append(xmbh).append("'");
		List<Map> m = baseCommonDao.getDataListByFullSql(str.toString());
		if(m!=null&&m.size()>0){
			casenum = m.get(0).get("CASENUM")==null?"":m.get(0).get("CASENUM").toString();
		}
		return casenum;
	}
}
