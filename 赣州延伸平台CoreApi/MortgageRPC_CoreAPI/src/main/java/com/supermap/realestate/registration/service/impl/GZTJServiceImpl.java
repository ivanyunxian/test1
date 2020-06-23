package com.supermap.realestate.registration.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;



import java.math.RoundingMode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.realestate.registration.model.BDCS_H_LS;
import com.supermap.realestate.registration.model.BDCS_H_LSY;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.EasyUiTree;
import com.supermap.realestate.registration.model.xmlmodel.BDCQLR;
/**
 * 赣州统计
 */
import com.supermap.realestate.registration.service.GZTJService;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.JH_DBHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ui.tree.Tree;

@Service("gztjService")
public class GZTJServiceImpl implements GZTJService {
	@Autowired
	private CommonDao baseCommonDao;
	
	@Override
	public Message GetYGDJ() {
		return null;
	}
	@Override
	public Message GetKSTJ(String tjsjks,String tjsjjz,String ywbh,Integer tjkg,String outsl,String outdb,String fwOrtd) {
		StringBuilder build = new StringBuilder();
		Message m = new Message();
		try{
			StringBuilder builderslsj = new StringBuilder();//受理时间
			StringBuilder builderdbsj = new StringBuilder();//登薄时间
			StringBuilder builderzxsj = new StringBuilder();//注销时间
			//由于注销流程的时间是放到附属权利的注销时间里面，关于注销的统计需要切换，增加ISZXLC判断
			boolean ISZXLC = false;//是否包含注销流程
			if(ywbh.contains("400")){
				ISZXLC = true;
			}
			String condition_strBdcdylx="";//统计的是地还是房屋过滤条件
			if(fwOrtd != null && fwOrtd.equals("1")){
				condition_strBdcdylx = " AND ( b.bdcdylx='01' OR b.bdcdylx='02') ";//所有权宗地、使用权宗地
			}else if(fwOrtd != null && fwOrtd.equals("2")){
				condition_strBdcdylx = " AND ( b.bdcdylx='01' OR b.bdcdylx='02' or b.bdcdylx='031' OR b.bdcdylx='032') ";//户、预测户
			}else{
				condition_strBdcdylx = " AND ( b.bdcdylx='031' OR b.bdcdylx='032') ";//户、预测户
			}
			String condition_kg=" and 1>1 ";//根据tjkg的配置，受理或登薄统计条件加上一个非条件,使对应的统计项值为空
			
			if(tjsjks != null || !"".equals(tjsjks)){
				builderslsj.append(" AND to_char(a.slsj,'yyyy-mm-dd') between '"+tjsjks+"' AND '"+tjsjjz+"'");
				builderdbsj.append(" AND to_char(c.djsj,'yyyy-mm-dd') between '"+tjsjks+"' AND '"+tjsjjz+"'");
				builderzxsj.append(" AND to_char(d.zxsj,'yyyy-mm-dd') between '"+tjsjks+"' AND '"+tjsjjz+"'");
			}
			
			StringBuilder builder = getBH(ywbh,"=");//受理统计的编码是一致的
			StringBuilder builderdb = getBH(ywbh,"=",false);//登薄需要区分注销与非注销，此处统计非注销登记
			StringBuilder builderzx = getBH(ywbh,"=",ISZXLC);//此处统计注销登记
			StringBuilder builder1 = getBH(outsl,"<>");//受理例外部分
			StringBuilder builder2 = getBH(outdb,"<>");//登薄例外部分
			
			build.append("SELECT a1.qllx,a1.djlx,b1.qllx AS dbqllx,b1.djlx AS dbdjlx,a1.gs AS slgs,b1.gs AS dbgs,a1.js AS sljs,b1.js AS dbjs FROM (");
			build.append("SELECT count(a.xmbh) as GS,count(DISTINCT a.project_id) as JS,aa.consttrans AS djlx,bb.consttrans AS qllx,a.djlx AS djlxn,a.qllx AS qllxn FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh ");
			build.append("LEFT JOIN  bdck.bdcs_const aa ON a.djlx = aa.constvalue AND aa.constslsid='21' LEFT JOIN  bdck.bdcs_const bb ON a.qllx = bb.constvalue AND bb.constslsid='8' ");
			build.append("where b.bdcdyid IS NOT NULL ");
			if(tjkg == 1){
				build.append(condition_kg);
			}else{
				build.append(builderslsj).append(builder).append(builder1).append(condition_strBdcdylx);
			}
			build.append(" GROUP BY aa.consttrans,bb.consttrans,a.qllx,a.djlx ) a1 ");
			build.append(" full join ( ");
			build.append("SELECT count(a.xmbh) as GS,count(DISTINCT a.project_id) as JS,aa.consttrans AS djlx,bb.consttrans AS qllx,a.djlx AS djlxn,a.qllx AS qllxn FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh ");
			build.append("LEFT JOIN  bdck.bdcs_ql_gz c ON b.djdyid=c.djdyid  AND a.xmbh=c.xmbh LEFT JOIN  bdck.bdcs_const aa ON a.djlx = aa.constvalue AND aa.constslsid='21' LEFT JOIN  bdck.bdcs_const bb ON a.qllx = bb.constvalue AND bb.constslsid='8' ");
			build.append("where c.djdyid IS NOT NULL ");
			if(tjkg == 2){
				build.append(condition_kg);
			}else{
				build.append(builderdbsj).append(builderdb).append(builder2).append(condition_strBdcdylx).append(" AND a.sfdb=1 ");;
//				build.append(builderdbsj).append(builderdb).append(builder2).append(condition_strBdcdylx).append(" AND a.sfdb=1 ");
			}
			build.append(" GROUP BY aa.consttrans,bb.consttrans,a.qllx,a.djlx ");
			if(ISZXLC){
				build.append(" union all ");
				build.append("SELECT count(a.xmbh) as GS,count(DISTINCT a.project_id) as JS,aa.consttrans AS djlx,bb.consttrans AS qllx,a.djlx AS djlxn,a.qllx AS qllxn FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh ");
				build.append("LEFT JOIN  bdck.bdcs_ql_gz c ON b.djdyid=c.djdyid  AND a.xmbh=c.xmbh LEFT JOIN  bdck.bdcs_fsql_gz d ON c.qlid=d.qlid LEFT JOIN  bdck.bdcs_const aa ON a.djlx = aa.constvalue AND aa.constslsid='21' LEFT JOIN  bdck.bdcs_const bb ON a.qllx = bb.constvalue AND bb.constslsid='8' ");
				build.append("where c.djdyid IS NOT NULL ");
				if(tjkg == 2){
					build.append(condition_kg);
				}else{
					build.append(builderzxsj).append(builderzx).append(builder2).append(condition_strBdcdylx).append(" AND a.sfdb=1 ");
				}
				build.append(" GROUP BY aa.consttrans,bb.consttrans,a.qllx,a.djlx ");
			}
			build.append(" ) b1 on a1.djlxn=b1.djlxn AND a1.qllxn=b1.qllxn  ORDER BY to_number(a1.qllxn) ASC,a1.djlxn");
			List<Map> maps = baseCommonDao.getDataListByFullSql(build.toString());
			m.setRows(maps);
			m.setTotal(maps.size());
		}catch(Exception ex){
			
		}
		return m;
	}
	private StringBuilder getBH(String ywbh,String equal){
		StringBuilder builder = new StringBuilder();
		
		if(ywbh != null && !"".equals(ywbh)){
			builder.append(" and (");
			if(ywbh.contains(";")){
				String[] gzlist = ywbh.split(";");
				for(int i=0;i<gzlist.length;i++){
					StringBuilder builder1 = getYWBH(gzlist[i],equal);
					builder.append(builder1);
					if(i<gzlist.length-1){
						if(equal.equals("<>")){
							builder.append(" and ");
						}else{
						builder.append(" or ");
						}
					}
				}
			}else{
				StringBuilder builder2 = getYWBH(ywbh,equal);
				builder.append(builder2);
			}
			builder.append(")");
		}
		return builder;
	}
	private StringBuilder getYWBH(String ywbh,String equal){
		StringBuilder builder = new StringBuilder();
		if("".equals(ywbh)){
			return builder;
		}
		String subindex= "", strYwbh="";
		if(ywbh.contains("-")){
			strYwbh = ywbh.split("-")[0];
			subindex = ywbh.split("-")[1];
		}else{
			strYwbh = ywbh;
			subindex = "13";
		}
		if(strYwbh.contains(",")){
			String[] bhs = strYwbh.split(",");
			for(int i=0; i < bhs.length; i++){
				String bh = bhs[i];
				int length=bh.length();
				if(length>9){//业务编号最大值9
					length=9;
				}
				if(i>0){
					if(equal.equals("<>")){
						builder.append(" and ");
					}else{
						builder.append(" or ");
					}
				}
				builder.append(" substr(a.project_id,").append(subindex).append(",").append(bh.length()+")");
				builder.append(equal).append("'").append(bh).append("'");
			}
		}else{
			builder.append(" substr(a.project_id,").append(subindex).append(",").append(strYwbh.length()+")");
			builder.append(equal).append("'").append(strYwbh).append("'");
		}
		
		return builder;
	}
	private StringBuilder getBH(String ywbh,String equal,boolean iszxlc){
		StringBuilder builder = new StringBuilder();
		
		if(ywbh != null && !"".equals(ywbh)){
			builder.append(" and (");
			if(ywbh.contains(";")){
				String[] gzlist = ywbh.split(";");
				for(int i=0;i<gzlist.length;i++){
					StringBuilder builder1 = getYWBH(gzlist[i],equal,iszxlc);
					builder.append(builder1);
					if(i<gzlist.length-1){
						if(equal.equals("<>")){
							builder.append(" and ");
						}else{
							builder.append(" or ");
						}
					}
				}
			}else{
				StringBuilder builder2 = getYWBH(ywbh,equal,iszxlc);
				builder.append(builder2);
			}
			builder.append(")");
		}
		return builder;
	}
	private StringBuilder getYWBH(String ywbh,String equal,boolean iszxlc){
		StringBuilder builder = new StringBuilder();
		if("".equals(ywbh)){
			return builder;
		}
		String subindex= "", strYwbh="";
		if(ywbh.contains("-")){
			strYwbh = ywbh.split("-")[0];
			subindex = ywbh.split("-")[1];
		}else{
			strYwbh = ywbh;
			subindex = "13";
		}
		if(strYwbh.contains(",")){
			String[] bhs = strYwbh.split(",");
			for(int i=0; i < bhs.length; i++){
				String bh = bhs[i];
				//为了前台配置不那么麻烦，后台ywbh时自动过滤，
				if(iszxlc){//注销流程只统计流程编号400的
					if(!bh.contains("400")){
						continue;
					}
					int length=bh.length();
					if(length>9){//业务编号最大值9
						length=9;
					}
					
					builder.append(" substr(a.project_id,").append(subindex).append(",").append(bh.length()+")");
					builder.append(equal).append("'").append(bh).append("'");
				
				}else{
					if(bh.contains("400")){
						continue;
					}
					int length=bh.length();
					if(length>9){//业务编号最大值9
						length=9;
					}
					if(i>0){
						if(equal.equals("<>")){
							builder.append(" and ");
						}else{
							builder.append(" or ");
						}
					}
					builder.append(" substr(a.project_id,").append(subindex).append(",").append(bh.length()+")");
					builder.append(equal).append("'").append(bh).append("'");
				}
			}
		}else{
			builder.append(" substr(a.project_id,").append(subindex).append(",").append(strYwbh.length()+")");
			builder.append(equal).append("'").append(strYwbh).append("'");
		}
		
		return builder;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Message GetSFXX(String tjsjks, String tjsjjz) {
		StringBuilder builder = new StringBuilder();
		Message m = new Message();
		try {
			if ("".equals(tjsjks) || tjsjks == null) {
				return m;
			}

			builder.append("SELECT 0 AS ssje,0 AS ysje,'合计' AS sfmc,'1' AS sflx,'0'AS tb FROM dual UNION ALL");
			builder.append(" SELECT sum(ssje)/10000 AS ssje, sum(ysje)/10000 AS ysje,to_char(CONCAT(y.sfxlmc, y.sfkmmc)) AS sfmc,to_char(t.sflx),'0' AS tb  ");
			builder.append(" FROM bdck.BDCS_DJSF t LEFT JOIN bdck.bdcs_sfdy y ON t.sfdyid=y.id WHERE t.ssje IS NOT NULL AND t.ywh IN(");
			builder.append(" SELECT DISTINCT file_number AS project_id from bdc_workflow.wfi_proinst  ");
			builder.append(" where proinst_id in(select proinst_id from bdc_workflow.wfi_actinst ac where  actinst_name='收费' and actinst_status=3 ");
			builder.append(" AND to_char(ac.actinst_end,'yyyy-mm-dd') BETWEEN ")
					.append("'").append(tjsjks).append("' AND '")
					.append(tjsjjz).append("' )) ")
					.append(" AND SUBSTR(t.ywh,18,1)<>'6' ")	//需要开发区是，需要上面的判断来判断
					.append("  GROUP BY CONCAT(y.sfxlmc, y.sfkmmc),t.sflx");
			List<Map> maps = baseCommonDao.getDataListByFullSql(builder
					.toString());

			Double hjysje = 0.0, hjssje = 0.0;
			Map map1 = null;

			if (maps != null && maps.size() > 0) {
				for (int i = 0; i < maps.size(); i++) {
					Map map = maps.get(i);
					if (map != null && map.get("SFMC").toString().equals("合计")) {
						map1 = map;
					}
					Double ysje = 0.0, ssje = 0.0;

					if (map.get("YSJE") != null) {
						ysje = Double.valueOf(map.get("YSJE").toString());
					}
					if (map.get("SSJE") != null) {
						ssje = Double.valueOf(map.get("SSJE").toString());
					}
					hjysje += ysje;
					hjssje += ssje;

					DecimalFormat df = new DecimalFormat("#.####");
					df.setRoundingMode(RoundingMode.HALF_UP);
					if (i == maps.size() - 1) { // 增加合计行
						map1.put("SSJE", Double.parseDouble(df.format(hjssje)));
						map1.put("YSJE", Double.parseDouble(df.format(hjysje)));
						map1.put("SFMC", "合计");
						map1.put("SFLX", "");
					}
				}
			}
			m.setRows(maps);
			m.setTotal(maps.size());
		} catch (Exception ex) {
			m = null;
		}
		return m;
	}

	@Override
	/**
	 * sfsj统计时间（月份）
	 * fwOrtd统计的是房屋或者土地（0是房屋，1是土地）
	 */
	public Message GetFWDJ(String sfsj,String fwOrtd) {
		StringBuilder builder = new StringBuilder();
		Message m = new Message();
		try{
			if("".equals(sfsj) || sfsj==null){
				return m;
			}
			String condition_strBdcdylx="";//统计的是地还是房屋过滤条件
			if(fwOrtd != null && fwOrtd.equals("1")){
				condition_strBdcdylx = " AND ( c.bdcdylx='01' OR c.bdcdylx='02') ";//所有权宗地、使用权宗地
			}else{
				condition_strBdcdylx = " AND ( c.bdcdylx='031' OR c.bdcdylx='032') ";//户、预测户
			}
			builder.append("SELECT SUM(gs) AS gs,SUM(zgs) AS zgs,SUM(mj) AS mj,SUM(zmj) AS zmj,SUM(sf) AS sf,SUM(zsf) AS zsf,djlx,qllx FROM ( ");
			builder.append("SELECT 0 AS gs , COUNT(c.id) AS zgs,0 AS mj ,round(SUM(z.scjzmj/10000),2) AS zmj,0 AS sf ,AVG(f.ssje) AS zsf, a.consttrans AS djlx,b.consttrans AS qllx,t.djlx AS djlxn,t.qllx AS qllxn  from bdck.BDCS_XMXX t  ");
			builder.append("LEFT JOIN  bdck.bdcs_const a ON t.djlx = a.constvalue AND a.constslsid='21' ");
			builder.append("LEFT JOIN  bdck.bdcs_const b ON t.qllx = b.constvalue AND b.constslsid='8' ");
			builder.append("LEFT JOIN  bdck.bdcs_djdy_gz c ON t.xmbh = c.xmbh ").append(condition_strBdcdylx);
			builder.append("LEFT JOIN bdck.bdcs_h_xz z ON c.bdcdyid=z.bdcdyid LEFT JOIN  ( ");
			builder.append(" SELECT round(SUM(bb.ssje/10000),2) AS ssje,aa.djlx,aa.qllx FROM bdck.bdcs_xmxx aa LEFT JOIN  bdck.bdcs_djsf bb ON aa.project_id=bb.ywh ")
					.append(" LEFT JOIN ( select file_number AS project_id from bdc_workflow.wfi_proinst where proinst_id in(select proinst_id from bdc_workflow.wfi_actinst ")
					.append(" where  actinst_name='收费' and actinst_status=3)) gg ON aa.project_id=gg.project_id ")
					.append(" WHERE aa.xmbh IN( ")
					.append(" SELECT  DISTINCT cc.xmbh FROM bdck.bdcs_djdy_gz cc ")
					.append(" WHERE  ( cc.bdcdylx='031' OR cc.bdcdylx='032')  and cc.bdcdylx IS NOT NULL ")
					.append(" ) AND bb.id IS NOT NULL  AND gg.project_id IS NOT NULL GROUP BY aa.djlx,aa.qllx ");
			builder.append(") f ON t.djlx = f.djlx AND t.qllx=f.qllx ");
			builder.append(" WHERE c.bdcdylx IS NOT NULL AND z.bdcdyid IS NOT NULL  GROUP BY a.consttrans,b.consttrans ,t.djlx,t.qllx ");
			builder.append(" UNION ALL ");
			builder.append(" SELECT COUNT(c.id) AS gs ,0 AS zgs ,round(SUM(z.scjzmj/10000),2) AS mj,0 AS zmj ,AVG(f.ssje) AS sf,0 AS zsf , a.consttrans AS djlx,b.consttrans AS qllx ,t.djlx AS djlxn,t.qllx AS qllxn from bdck.bdcs_XMXX t  ");
			builder.append(" LEFT JOIN  bdck.bdcs_const a ON t.djlx = a.constvalue AND a.constslsid='21' ");
			builder.append(" LEFT JOIN  bdck.bdcs_const b ON t.qllx = b.constvalue AND b.constslsid='8' ");
			builder.append(" LEFT JOIN bdck.bdcs_djdy_gz c ON t.xmbh = c.xmbh  ").append(condition_strBdcdylx);
			builder.append(" LEFT JOIN bdck.bdcs_h_xz z ON c.bdcdyid=z.bdcdyid LEFT JOIN  (");
			builder.append(" SELECT round(SUM(bb.ssje/10000),2) AS ssje,aa.djlx,aa.qllx FROM bdck.bdcs_xmxx aa LEFT JOIN  bdck.bdcs_djsf bb ON aa.project_id=bb.ywh ")
					.append(" LEFT JOIN ( select file_number AS project_id from bdc_workflow.wfi_proinst where proinst_id in(select proinst_id from bdc_workflow.wfi_actinst ")
					.append(" where  actinst_name='收费' and actinst_status=3 ")
					.append(" AND to_char(actinst_end,'yyyy-mm')='").append(sfsj).append("' ")
					.append(")) gg ON aa.project_id=gg.project_id ")
					.append(" WHERE aa.xmbh IN( ")
					.append(" SELECT  DISTINCT cc.xmbh FROM bdck.bdcs_djdy_gz cc ")
					.append(" WHERE  ( cc.bdcdylx='031' OR cc.bdcdylx='032')  and cc.bdcdylx IS NOT NULL ")
					.append(" ) AND bb.id IS NOT NULL  AND gg.project_id IS NOT NULL ");
			builder.append(" GROUP BY aa.djlx,aa.qllx ");
			builder.append(") f ON t.djlx = f.djlx AND t.qllx=f.qllx ");
			builder.append(" WHERE c.bdcdylx IS NOT NULL AND z.bdcdyid IS NOT NULL  AND to_char(t.slsj,'yyyy-mm')='").append(sfsj).append("' ");
			builder.append(" GROUP BY a.consttrans,b.consttrans,t.djlx,t.qllx ");
			builder.append(") GROUP BY djlx,qllx,djlxn,qllxn ORDER BY to_number(qllxn) ASC,djlxn ASC ");
			String sql = builder.toString();
			if(fwOrtd.equals("1")){//土地业务统计
				sql=sql.replaceAll("031", "01").replaceAll("032", "02").replaceAll("scjzmj", "zdmj").replaceAll("bdcs_h_xz", "bdcs_shyqzd_xz");
			}
			List<Map> maps = baseCommonDao.getDataListByFullSql(sql);

			m.setRows(maps);
			m.setTotal(maps.size());
		}
		catch(Exception ex){
			m=null;
		}
		return m;
	}
	
	
	public Message GetDepts(){
		StringBuilder builder = new StringBuilder();
		builder.append("select distinct departmentname from smwb_framework.t_department");
		Message m = new Message();
		List<Map> maps = baseCommonDao.getDataListByFullSql(builder.toString());
		m.setRows(maps);
		m.setTotal(maps.size());
		return m;
	}
	/**
	 * 部门统计
	 */
	public Message GetDEPT(String qsj,String zsj,String fwOrtd,String selDept) {
		StringBuilder builder = new StringBuilder();
		Message m = new Message();
		try{
			String sfsj="";
			if(qsj != null || !"".equals(qsj)){
				sfsj =" AND XM.SLSJ BETWEEN TO_DATE('"+qsj+" "+"00:00:00"+"','yyyy-mm-dd hh24:mi:ss') AND TO_DATE('"+zsj+" "+"23:59:59"+"','yyyy-mm-dd hh24:mi:ss') ";
			}
			String condition_strBdcdylx="";//统计的是地还是房屋过滤条件
			if(fwOrtd != null && fwOrtd.equals("1")){
				condition_strBdcdylx = " AND ( c.bdcdylx='01' OR c.bdcdylx='02') ";//所有权宗地、使用权宗地
			}else if(fwOrtd != null && fwOrtd.equals("2")){
				condition_strBdcdylx = " AND ( c.bdcdylx='01' OR c.bdcdylx='02' or c.bdcdylx='031' OR c.bdcdylx='032') ";//户、预测户
			}else{
				condition_strBdcdylx = " AND ( c.bdcdylx='031' OR c.bdcdylx='032') ";//户、预测户
			}
			
			builder.append("SELECT SUM(gs) AS gs,SUM(zgs) AS zgs,djlx,qllx,lc,bm FROM ( ");
			builder.append("SELECT 0 AS gs , COUNT(c.id) AS zgs, a.consttrans AS djlx,b.consttrans AS qllx,w.prodef_name AS lc,de.departmentname AS bm, xm.djlx AS djlxn, xm.qllx AS qllxn  from bdck.BDCS_XMXX xm  ");
			builder.append(" LEFT JOIN bdck.bdcs_const a ON xm.djlx=a.constvalue AND a.constslsid='21' ");
			builder.append("LEFT JOIN bdck.bdcs_const b ON xm.qllx = b.constvalue AND b.constslsid = '8' ");
			builder.append("LEFT JOIN bdc_workflow.wfi_proinst w ON xm.project_id = w.file_number ");
			builder.append("  LEFT JOIN smwb_framework.t_user u ON w.staff_id=u.id ");
			builder.append(" LEFT JOIN smwb_framework.t_department de ON u.departmentid = de.id ");
			builder.append(" LEFT JOIN bdck.bdcs_djdy_gz c ON xm.xmbh=c.xmbh ").append(condition_strBdcdylx);
			if(selDept.isEmpty()||selDept=="全部"){
				builder.append(" WHERE c.bdcdylx IS NOT NULL  GROUP BY a.consttrans,b.consttrans ,w.prodef_name ,de.departmentname ,xm.djlx,xm.qllx ");
			}
			else{
				builder.append(" WHERE c.bdcdylx IS NOT NULL  AND de.departmentname='"+selDept+"' GROUP BY a.consttrans,b.consttrans ,w.prodef_name ,de.departmentname ,xm.djlx,xm.qllx ");

			}
			builder.append(" UNION ALL ");
			builder.append(" SELECT COUNT(c.id) AS gs ,0 AS zgs ,a.consttrans AS djlx,b.consttrans AS qllx ,w.prodef_name AS lc,de.departmentname AS bm , xm.djlx AS djlxn,xm.qllx AS qllxn from bdck.bdcs_XMXX xm  ");
			builder.append(" LEFT JOIN  bdck.bdcs_const a ON xm.djlx = a.constvalue AND a.constslsid='21' ");
			builder.append(" LEFT JOIN  bdck.bdcs_const b ON xm.qllx = b.constvalue AND b.constslsid='8' ");
			builder.append(" LEFT JOIN bdc_workflow.wfi_proinst w ON xm.project_id = w.file_number ");
			builder.append("  LEFT JOIN smwb_framework.t_user u ON w.staff_id=u.id ");
			builder.append(" LEFT JOIN smwb_framework.t_department de ON u.departmentid = de.id ");
			builder.append(" LEFT JOIN bdck.bdcs_djdy_gz c ON xm.xmbh = c.xmbh  ").append(condition_strBdcdylx);
			builder.append(" WHERE c.bdcdylx IS NOT NULL "+sfsj+" ");
			builder.append(" GROUP BY a.consttrans,b.consttrans,w.prodef_name,de.departmentname,xm.djlx,xm.qllx ");
			builder.append(") GROUP BY djlx,qllx,lc,bm,djlxn,qllxn ORDER BY to_number(qllxn) ASC,djlxn ASC ");
			
			String sql = builder.toString();
			if(fwOrtd.equals("1")){//土地业务统计
				sql=sql.replaceAll("031", "01").replaceAll("032", "02").replaceAll("bdcs_h_xz", "bdcs_shyqzd_xz");
			}
			List<Map> maps = baseCommonDao.getDataListByFullSql(sql);

			m.setRows(maps);
			m.setTotal(maps.size());
		}
		catch(Exception ex){
			m=null;
		}
		return m;
	}
	
	//部门统计按时间导出
//		@SuppressWarnings("rawtypes")
//		@Override
//		public Message Getdepttj(String qsj,String zsj,String deptname,String fwortd) {
//			StringBuilder build = new StringBuilder();
//			Message m = new Message();
//			String sfsj="";
//			if(qsj != null || !"".equals(qsj)){
//				sfsj =" AND XM.SLSJ BETWEEN TO_DATE('"+qsj+" "+"00:00:00"+"','yyyy-mm-dd hh24:mi:ss') AND TO_DATE('"+zsj+" "+"23:59:59"+"','yyyy-mm-dd hh24:mi:ss') ";
//			}
//			
//			String condition_strBdcdylx="";//统计的是地还是房屋过滤条件
//			if(fwortd != null && fwortd.equals("1")){
//				condition_strBdcdylx = " AND C.BDCDYLX='01' OR C.BDCDYLX='02'";//所有权宗地、使用权宗地
//			}else if(fwortd != null && fwortd.equals("2")){
//				condition_strBdcdylx = " AND C.BDCDYLX='01' OR C.BDCDYLX='02' or C.BDCDYLX='031' OR C.BDCDYLX='032'";//户、预测户
//			}else{
//				condition_strBdcdylx = " AND C.BDCDYLX='031' OR C.BDCDYLX='032'";//户、预测户
//			}
//			try{
//				String sql="SELECT SUM(ZGS) AS ZGS, DJLX, QLLX, LC, BM" 
//						 + " FROM (SELECT COUNT(C.ID) AS ZGS, "
//						 + " A.CONSTTRANS as DJLX, "
//						 + " B.CONSTTRANS AS QLLX,"
//						 + " TO_CHAR(SLSJ, 'YYYY-MM') AS SLSJ,"
//						 + " W.PRODEF_NAME AS LC,"
//						 + " DE.DEPARTMENTNAME AS BM "
//						 + " FROM "
//						 + " BDCK.BDCS_XMXX XM "
//						 + " LEFT JOIN BDCK.BDCS_CONST A "
//						 + " ON XM.DJLX = A.CONSTVALUE AND A.CONSTSLSID = '21'"
//						 + " LEFT JOIN BDCK.BDCS_CONST B "
//						 + " ON XM.QLLX = B.CONSTVALUE AND B.CONSTSLSID = '8' "
//						 + " LEFT JOIN BDC_WORKFLOW.WFI_PROINST W "
//						 + " ON XM.PROJECT_ID = W.FILE_NUMBER "
//						 + " LEFT JOIN SMWB_FRAMEWORK.T_USER U "
//						 + " ON W.STAFF_ID=U.ID "
//						 + " LEFT JOIN SMWB_FRAMEWORK.T_DEPARTMENT DE "
//						 + " ON U.DEPARTMENTID = DE.ID "
//						 + " LEFT JOIN BDCK.BDCS_DJDY_GZ C "
//						 + " ON XM.XMBH = C.XMBH "+condition_strBdcdylx+""
//						 + " WHERE C.BDCDYLX IS NOT NULL "
//						 + " "+sfsj+""
//						 + " AND DE.DEPARTMENTNAME = '"+deptname+"'"
//						 + " GROUP BY A.CONSTTRANS,B.CONSTTRANS,W.PRODEF_NAME,DE.DEPARTMENTNAME,XM.DJLX,XM.QLLX,SLSJ)"
//						 + " GROUP BY DJLX, QLLX, LC, BM "
//						 + " ORDER BY QLLX ASC, DJLX ASC";
//				List<Map> maps = baseCommonDao.getDataListByFullSql(sql);
//				m.setRows(maps);
//				m.setTotal(maps.size());
//			}catch(Exception e){
//				m=null;
//			}
//			
//			return m;
//		}
	
	
	@Override
	public Message GetDZSF() {
		return null;
	}
	@Override
	public Message GetDYDJ(String sjq, String sjz, String fwOrtd) {
		Message m = new Message();
		try{
			StringBuilder build = new StringBuilder();
			StringBuilder builddjsj=new StringBuilder();
			//StringBuilder buildersj = new StringBuilder();//查询时间
			if(sjq != null || !"".equals(sjq)){
				builddjsj.append(" AND to_char(b.djsj,'yyyy-mm-dd') between '"+sjq+"' AND '"+sjz+"'");
			}
			String condition_strBdcdylx="";//统计的是地还是房屋过滤条件 fwOrtd=0,不区分；=1为土地；=2为房屋
			if(fwOrtd.equals("1")){
				condition_strBdcdylx = " AND ( a.bdcdylx='01' OR a.bdcdylx='02') ";//所有权宗地、使用权宗地
			}else if(fwOrtd.equals("2")){
				condition_strBdcdylx = " AND ( a.bdcdylx='031' OR a.bdcdylx='032') ";//户、预测户
			}else if(fwOrtd.equals("3")){
				condition_strBdcdylx = " AND ( a.bdcdylx='031') ";//户
			}else if(fwOrtd.equals("4")){
				condition_strBdcdylx = " AND ( a.bdcdylx='032') ";//预测户
			}else if(fwOrtd.equals("5")){
				condition_strBdcdylx = " AND ( a.bdcdylx='04') ";//宗海
			}else if(fwOrtd.equals("6")){
				condition_strBdcdylx = " AND ( a.bdcdylx='09') ";//农用地
			}else if(fwOrtd.equals("7")){
				condition_strBdcdylx = " AND ( a.bdcdylx='05') ";//林地
			}else{
				condition_strBdcdylx="";
			}
			
			build.append("SELECT COUNT(project_id) AS GS,SUM(bdbe)/10000 AS BDBE,SUM(zge)/10000 AS ZGE,qlrlx ");
			build.append(" FROM( ");
			build.append(" SELECT DISTINCT t.project_id, case when dw.constvalue>1 then z.bdbzzqse*10000 else z.bdbzzqse end bdbe,case when dw.constvalue>1 then z.zgzqse*10000 else z.zgzqse end zge ,con.consttrans as qlrlx ");
			build.append(" FROM bdck.bdcs_xmxx t LEFT JOIN bdck.bdcs_djdy_gz a ON a.xmbh=t.xmbh ");
			build.append("LEFT JOIN bdck.bdcs_ql_xz b ON a.djdyid=b.djdyid AND a.xmbh=b.xmbh ");
			build.append(" LEFT JOIN bdck.BDCS_FSQL_xz z ON b.qlid=z.qlid ");
			build.append("left join bdck.bdcs_sqr sqr on t.xmbh=sqr.xmbh ");
			build.append("left join (" );
			build.append("select a.constvalue,a.consttrans from bdck.bdcs_const a ");
			build.append("left join bdck.bdcs_constcls b on a.constslsid=b.constslsid where b.constclstype='QLRLX' "); 
			build.append(")  con on sqr.sqrlx=con.constvalue ");
			build.append("left join (select a.constvalue, a.consttrans from bdck.bdcs_const a left join bdck.bdcs_constcls b on a.constslsid = b.constslsid where b.constclstype = 'JEDW') dw on z.zqdw = dw.constvalue ");
			build.append("WHERE z.fsqlid IS NOT NULL ");
			build.append(" AND b.qllx='23'  AND SUBSTR(t.project_id,18,1)<>'6' AND t.sfdb=1 ");
			//build.append("and sqr.sqrlb=2 ");
			build.append("and  z.dyr like '%'||sqr.sqrxm||'%'");
			
			build.append(builddjsj);
			build.append(condition_strBdcdylx).append(" ) aa ");
			build.append("group by  aa.qlrlx ");
		
			List<Map> maps = baseCommonDao.getDataListByFullSql(build.toString());
			m.setRows(maps);
			m.setTotal(maps.size());
		}catch(Exception e){
			m=null;
		}
		
		return m;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Message GetDJYWTJ(String tjsjks, String tjsjjz) {
		StringBuilder build = new StringBuilder();
		Message m = new Message();
		try{
			String sql="SELECT NVL(YWINFO.DEPARTMENTNAME,'无对应部门') as DEPARTMENTNAME,YWINFO.DJLX, YWINFO.QLLX, YWINFO.ZBGS, YWINFO.DBGS, YWINFO.BJGS, NVL(FZINFO.FZZMGS, 0) AS FZZMGS," 
					 + "NVL(FZINFO.FZZSGS, 0) AS FZZSGS, NVL(SZINFO.SZZMGS, 0) AS SZZMGS, NVL(SZINFO.SZZSGS, 0) AS SZZSGS "
					 +" FROM "
					 //业务受理统计
					 +"(SELECT DEPARTMENT.DEPARTMENTNAME,DJLX,  QLLX, "
					 +" SUM(CASE WHEN SFBJ = '1' THEN 1 ELSE 0 END) AS BJGS,　"
					 +" SUM(CASE WHEN SFDB = '1' AND (SFBJ IS NULL OR SFBJ <> '1') THEN 1 ELSE 0 END) AS DBGS, " 
					 +" SUM(CASE  WHEN (SFDB IS NULL OR SFDB <> '1') AND (SFBJ IS NULL OR SFBJ <> '1') THEN  1  ELSE  0  END) AS ZBGS " 
					 +" FROM BDCK.BDCS_XMXX XMXX " 
					 + "LEFT JOIN (select tuser.USERNAME,TDEPARTMENT.DEPARTMENTNAME from SMWB_FRAMEWORK.T_USER tuser, "
					 + "SMWB_FRAMEWORK.T_DEPARTMENT TDEPARTMENT where tuser.DEPARTMENTID =  TDEPARTMENT.ID) DEPARTMENT "
					 + "ON DEPARTMENT.USERNAME = XMXX.SLRY "
					 +" WHERE XMXX.SLSJ BETWEEN "
					 +" TO_DATE('"+tjsjks+" 00:00:01', 'yyyy-mm-dd hh24:mi:ss') AND "
					 +" TO_DATE('"+tjsjjz+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') "
					 +" GROUP BY DEPARTMENT.DEPARTMENTNAME,DJLX, QLLX  " 
					 +" ORDER BY DJLX, TO_NUMBER(QLLX)) YWINFO "
					 //发证统计
					 +" LEFT JOIN "
					 +" (SELECT DEPARTMENT.DEPARTMENTNAME,XMXX.DJLX,XMXX.QLLX, "
					 +" SUM(CASE  WHEN DJFZ.HFZSH LIKE '%不动产证明%' THEN 1   ELSE  0  END) AS FZZMGS, "
					 +" SUM(CASE  WHEN DJFZ.HFZSH LIKE '%不动产权%' THEN 1  ELSE  0  END) AS FZZSGS, "
					 +" COUNT(*) AS FZGS FROM (SELECT DISTINCT XMBH, HFZSH, MIN(FZSJ) AS FZSJ "
					 +" FROM BDCK.BDCS_DJFZ  GROUP BY XMBH, HFZSH) DJFZ "
					 +" LEFT JOIN BDCK.BDCS_XMXX XMXX  ON XMXX.XMBH = DJFZ.XMBH "
					 + "LEFT JOIN (select tuser.USERNAME,TDEPARTMENT.DEPARTMENTNAME from SMWB_FRAMEWORK.T_USER tuser, "
					 + "SMWB_FRAMEWORK.T_DEPARTMENT TDEPARTMENT where tuser.DEPARTMENTID =  TDEPARTMENT.ID) DEPARTMENT "
					 + "ON DEPARTMENT.USERNAME = XMXX.SLRY "
					 +" WHERE DJFZ.FZSJ BETWEEN  "
					 +" TO_DATE('"+tjsjks+" 00:00:01', 'yyyy-mm-dd hh24:mi:ss') AND "  
					 +" TO_DATE('"+tjsjjz +" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') "
					 +" GROUP BY DEPARTMENT.DEPARTMENTNAME,XMXX.DJLX, XMXX.QLLX) FZINFO  "
					 + " ON YWINFO.DJLX = FZINFO.DJLX   AND YWINFO.QLLX = FZINFO.QLLX  AND YWINFO.DEPARTMENTNAME = FZINFO.DEPARTMENTNAME "
					 //缮证统计
					 +" LEFT JOIN "
					 +" (SELECT DEPARTMENT.DEPARTMENTNAME,XMXX.DJLX,  XMXX.QLLX, "
					 +" SUM(CASE  WHEN ZS.BDCQZH LIKE '%不动产证明%' THEN 1   ELSE 0  END) AS SZZMGS, "
					 +" SUM(CASE  WHEN ZS.BDCQZH LIKE '%不动产权%' THEN 1  ELSE   0  END) AS SZZSGS, "
					 +" COUNT(*) AS SZGS "
					 +" FROM BDCK.BDCS_DJSZ DJSZ "
					 +" INNER JOIN BDCK.BDCS_XMXX XMXX ON XMXX.XMBH = DJSZ.XMBH "
					 +" INNER JOIN (SELECT DISTINCT XMBH, BDCQZH FROM BDCK.BDCS_ZS_GZ) ZS ON XMXX.XMBH = ZS.XMBH "
					 + "LEFT JOIN (select tuser.USERNAME,TDEPARTMENT.DEPARTMENTNAME from SMWB_FRAMEWORK.T_USER tuser, "
					 + "SMWB_FRAMEWORK.T_DEPARTMENT TDEPARTMENT where tuser.DEPARTMENTID =  TDEPARTMENT.ID) DEPARTMENT "
					 + "ON DEPARTMENT.USERNAME = XMXX.SLRY "
					 +" WHERE DJSZ.SZSJ BETWEEN "
					 +" TO_DATE('"+tjsjks+" 00:00:01', 'yyyy-mm-dd hh24:mi:ss') AND "
					 +" TO_DATE('"+tjsjjz+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') "
					 +" GROUP BY DEPARTMENT.DEPARTMENTNAME,XMXX.DJLX, XMXX.QLLX) SZINFO "
					 +" ON YWINFO.DJLX = SZINFO.DJLX  AND YWINFO.QLLX = SZINFO.QLLX AND YWINFO.DEPARTMENTNAME = SZINFO.DEPARTMENTNAME"
					 +" ORDER BY DJLX, DEPARTMENTNAME";
			List<Map> maps = baseCommonDao.getDataListByFullSql(sql);
			if(!StringHelper.isEmpty(maps) && maps.size()>0)
			{
				for(Map map :maps)
				{
					String strdjlx=StringHelper.FormatByDatatype(map.get("DJLX"));
				    map.put("DJLX", ConstHelper.getNameByValue("DJLX", strdjlx));
					String strqllx=StringHelper.FormatByDatatype(map.get("QLLX"));
					if(DJLX.YGDJ.Value.equals(strdjlx) && QLLX.QTQL.Value.equals(strqllx))
					{
						map.put("QLLX", "转移预告");
					}
					else if(DJLX.CFDJ.Value.equals(strdjlx) && QLLX.QTQL.Value.equals(strqllx))
					{
						map.put("QLLX", "查封");
					}
					else if(DJLX.CFDJ.Value.equals(strdjlx) && QLLX.CFZX.Value.equals(strqllx))
					{
						map.put("QLLX", "解封");
					}
					else
					{
					map.put("QLLX", ConstHelper.getNameByValue("QLLX", strqllx));
					}
				}
			}
			m.setRows(maps);
			m.setTotal(maps.size());
		}catch(Exception e){
			m=null;
		}
		
		return m;
	}
	
	
	
	@Override
	public Message GetDJZXYWTJ(String tjsjks, String tjsjjz) {
		StringBuilder build = new StringBuilder();
		Message m = new Message();
		try{
//			StringBuilder builder_start = new StringBuilder();//流程开始时间
			StringBuilder builder_end = new StringBuilder();//流程开始时间
			if(tjsjks != null || !"".equals(tjsjks)){
				builder_end.append(" AND V.ACTINST_END BETWEEN TO_DATE(");
				builder_end.append("'"+tjsjks+" "+"00:00:00"+"','yyyy-mm-dd hh24:mi:ss') AND TO_DATE( ");
				builder_end.append("'"+tjsjjz+" "+"23:59:59"+"','yyyy-mm-dd hh24:mi:ss')");
			}
			
			build.append("SELECT B.DEPARTMENTNAME,COUNT( V.ACTINST_ID) AS YBJS ");
			build.append(" FROM ( SELECT * FROM (SELECT ROW_NUMBER() OVER(PARTITION BY B.PROINST_ID, B.ACTDEF_ID ORDER BY B.ACTINST_START ASC) RN,B.* ");
			build.append(" FROM BDC_WORKFLOW.WFI_ACTINST B) WHERE RN = 1) V "
					+ " LEFT JOIN SMWB_FRAMEWORK.T_USER A ON V.STAFF_ID=A.ID "
					+ " LEFT JOIN  SMWB_FRAMEWORK.T_DEPARTMENT B ON A.DEPARTMENTID=B.ID "
					+ " WHERE (V.ACTINST_STATUS = 3 OR V.ACTINST_STATUS=0 ) ");
					//+ " AND V.ACTINST_END BETWEEN TO_DATE('2016-09-02 00:00:00','YYYY-MM-DD HH24:MI:SS') AND TO_DATE( '2016-09-07 23:59:59','YYYY-MM-DD HH24:MI:SS') "
			build.append(builder_end);
			build.append(" GROUP BY B.DEPARTMENTNAME   ");
			List<Map> maps = baseCommonDao.getDataListByFullSql(build.toString());
			Map countMap = new HashMap();
			int count = 0;
			if(!StringHelper.isEmpty(maps) && maps.size()>0){
				for(Map map :maps){
					if (!StringHelper.isEmpty(map.get("YBJS"))) {
						 count += Integer.parseInt(map.get("YBJS").toString());
					}
				}
			}
			countMap.put("DEPARTMENTNAME", "总计");
			countMap.put("YBJS",count);
			maps.add(countMap);
			m.setRows(maps);
			m.setTotal(maps.size());
		}catch(Exception e){
			m=null;
		}
		return m;
	}
	
	@Override
	public Message GetKSYWTJ(String tjsjks, String tjsjjz, String deptid) {
		StringBuilder build = new StringBuilder();
		Message m = new Message();
		try{
			StringBuilder builder_start = new StringBuilder();//流程开始时间
			StringBuilder builder_end = new StringBuilder();//流程开始时间
			StringBuilder con_mc= new StringBuilder();//科室名称
			if(!"".equals(deptid) && deptid != null){
				con_mc.append(" AND B.ID ='").append(deptid).append("' ");
			}
			if(tjsjks != null || !"".equals(tjsjks)){
//				builder_start.append(" AND v.actinst_start between to_date(");
//				builder_start.append("'"+tjsjks+" "+"00:00:00"+"','yyyy-mm-dd hh24:mi:ss')and to_date( ");
//				builder_start.append("'"+tjsjjz+" "+"23:59:59"+"','yyyy-mm-dd hh24:mi:ss')");
				builder_end.append(" AND V.ACTINST_END BETWEEN TO_DATE(");
				builder_end.append("'"+tjsjks+" "+"00:00:00"+"','yyyy-mm-dd hh24:mi:ss') AND TO_DATE( ");
				builder_end.append("'"+tjsjjz+" "+"23:59:59"+"','yyyy-mm-dd hh24:mi:ss')");
			}
//			build.append(" SELECT staff_name, SUM(zbjs) AS zbjs,SUM(ybjs) AS ybjs FROM(");
//			build.append(" select v.staff_name,COUNT( v.file_number) AS zbjs,0 AS ybjs from (");
//			build.append(" select * from (select row_number() over(partition by b.proinst_id, b.actdef_id order by b.actinst_start asc) rn,b.* from BDC_WORKFLOW.v_projectlist b)");
//			build.append(" where rn = 1) v LEFT JOIN smwb_framework.t_user a ON v.staff_id=a.id LEFT JOIN  smwb_framework.t_department b ON a.departmentid=b.id");
//			build.append(" WHERE a.id IS NOT NULL").append(con_mc);
//			build.append(" AND v.ACTINST_STATUS = 2 ").append(builder_start);
//			build.append(" GROUP BY v.staff_name ");
//			build.append(" UNION ALL");
			if("0".equals(ConfigHelper.getNameByValue("STATISTICS"))){
				build.append(" SELECT V.STAFF_NAME,COUNT( V.FILE_NUMBER) AS YBJS  FROM (");
				build.append(" SELECT * FROM (SELECT ROW_NUMBER() OVER(PARTITION BY B.PROINST_ID, B.ACTDEF_ID ORDER BY B.ACTINST_START ASC) RN,B.* FROM BDC_WORKFLOW.V_PROJECTLIST B)");
			}else{
				build.append(" SELECT V.STAFF_NAME,COUNT( DY.ID) AS YBJS  FROM (");
				build.append(" SELECT * FROM (SELECT ROW_NUMBER() OVER(PARTITION BY B.PROINST_ID, B.STAFF_ID ORDER BY B.ACTINST_START ASC) RN,B.* FROM BDC_WORKFLOW.V_PROJECTLIST B)");
			}
			build.append(" WHERE RN = 1) V LEFT JOIN SMWB_FRAMEWORK.T_USER A ON V.STAFF_ID=A.ID LEFT JOIN  SMWB_FRAMEWORK.T_DEPARTMENT B ON A.DEPARTMENTID=B.ID");
			if("1".equals(ConfigHelper.getNameByValue("STATISTICS"))){
				build.append(" LEFT JOIN BDCK.BDCS_XMXX XX ON XX.PROJECT_ID=V.FILE_NUMBER LEFT JOIN BDCK.BDCS_DJDY_GZ DY ON DY.XMBH=XX.XMBH ");
			}
			//build.append(" WHERE V.ACTINST_STATUS = 3  AND V.OPERATION_TYPE =1 ").append(con_mc).append(builder_end);
			build.append(" WHERE (V.ACTINST_STATUS = 3 OR ( V.PROINST_STATUS=0 AND V.ACTINST_STATUS=0 )) ").append(con_mc).append(builder_end);
			//build.append(" GROUP BY V.STAFF_NAME )");			
			build.append(" GROUP BY STAFF_NAME");			
			@SuppressWarnings("rawtypes")
			List<Map> maps = baseCommonDao.getDataListByFullSql(build.toString());
			Map countMap = new HashMap();
			int count = 0;
			if(!StringHelper.isEmpty(maps) && maps.size()>0){
				for(Map map :maps){
					if (!StringHelper.isEmpty(map.get("YBJS"))) {
						 count += Integer.parseInt(map.get("YBJS").toString());
					}
				}
			}
			countMap.put("STAFF_NAME", "总计");
			countMap.put("YBJS",count);
			maps.add(countMap);
			m.setRows(maps);
			m.setTotal(maps.size());
		
			}catch(Exception e){
			m=null;
		}
		return m;
	}

    /**
	 * 鹰潭全市总数据统计
	 */
	@Override
	public Message GetTJALL(){
		StringBuilder build = new StringBuilder();
		StringBuilder build1 = new StringBuilder();
		Message m = new Message();
		try{			
			build1.append("select consttrans,(select count(*) from (select * from BDCK.BDCS_H_XZ union all select * from BDCK.BDCS_H_XZY) )as bdcdys,"
					+ "(select count(*) from bdck.bdcs_xmxx) as bjl,(select count(*) from bdck.bdcs_sjsb) as sbs,"
					+ "(select count(*) from sharesearch.dhxx where status is not null )as dxfsl from bdck.bdcs_const t where t.constslsid = '65'and t.bz = '1' "
					+ "union all "
					+ "select consttrans,(select count(*) from (select * from BDCK.BDCS_H_XZ@to_orcl6_bdck union all select * from BDCK.BDCS_H_XZY@to_orcl6_bdck) )as bdcdys,"
					+ "(select count(*) from bdck.bdcs_xmxx@to_orcl6_bdck) as bjl,"
					+ "(select count(*) from bdck.bdcs_sjsb@to_orcl6_bdck) as sbs,"
					+ "(select count(*) from sharesearch.dhxx@to_orcl6_sharesearch where status is not null )as dxfsl from bdck.bdcs_const t where t.constslsid = '65'and t.bz = '2' "
					+ "union all "
					+ "select consttrans,(select count(*) from (select * from BDCK.BDCS_H_XZ@to_orcl7_bdck union all select * from BDCK.BDCS_H_XZY@to_orcl7_bdck) )as bdcdys,"
					+ "(select count(*) from bdck.bdcs_xmxx@to_orcl7_bdck) as bjl,"
					+ "(select count(*) from bdck.bdcs_sjsb@to_orcl7_bdck) as sbs,"
					+ "(select count(*) from sharesearch.dhxx@to_orcl7_sharesearch where status is not null )as dxfsl from bdck.bdcs_const t where t.constslsid = '65'and t.bz = '3' ");
			
			build.append("select '全市' as consttrans, sum(bdcdys) as bdcdys, sum(bjl) as bjl, sum(sbs) as sbs, sum(dxfsl) dxfsl from ( "+build1+" )");
					
			List<Map> maps = baseCommonDao.getDataListByFullSql(build.toString());
			List<Map> maps1 = baseCommonDao.getDataListByFullSql(build1.toString());
			for(Map ma:maps1){
				maps.add(ma);
			}
			m.setRows(maps);
			m.setTotal(maps.size());
		}catch(Exception e){
			m=null;
		}
		return m;
	}
	/**
	 * 数据上报统计sql
	 */
	@Override
	public Message GetSJSBTJ(String tjsjks, String tjsjjz, String deptid ,String dedjlx, String tjlx) {
		StringBuilder build = new StringBuilder();
		Message m = new Message();
		try{
			StringBuilder builder_dxfssj = new StringBuilder();
			if((tjsjks != null || !"".equals(tjsjks)) && (tjsjjz != null || !"".equals(tjsjjz))){
				builder_dxfssj.append(" AND t.operatetime between to_date(");
				builder_dxfssj.append("'"+tjsjks+" "+"00:00:00"+"','yyyy-mm-dd hh24:mi:ss')and to_date( ");
				builder_dxfssj.append("'"+tjsjjz+" "+"23:59:59"+"','yyyy-mm-dd hh24:mi:ss')");
			}
			
				StringBuilder builder_yt = new StringBuilder();
				builder_yt.append("select consttrans,");
				builder_yt.append("(select count(*) from bdck.bdcs_sjsb t "
						+ "left join bdc_workflow.wfi_proinst h on t.proinstid = h.proinst_id "
						+ "left join bdck.bdcs_xmxx k on k.PROJECT_ID = h.FILE_NUMBER where t.successflag = '0' AND k.SFDB = '1'");
				if(dedjlx!=null && !dedjlx.equals("")){
					builder_yt.append(" and  k.djlx in ("+dedjlx+")");
				}
				builder_yt.append(builder_dxfssj+") AS SBSB,");
				builder_yt.append("(select count(*) from bdck.bdcs_xmxx k  "
						+ "left join bdc_workflow.wfi_proinst h on k.PROJECT_ID = h.FILE_NUMBER "
						+ "left join bdck.bdcs_sjsb t on h.proinst_id = t.proinstid  where t.id is null ");
				if(dedjlx!=null && !dedjlx.equals("")){
					builder_yt.append(" and  k.djlx in ("+dedjlx+")");
				}
				builder_yt.append(" and k.slsj between to_date('"+tjsjks+" "+"00:00:00"+"','yyyy-mm-dd hh24:mi:ss')and to_date('"+tjsjjz+" "+"23:59:59"+"','yyyy-mm-dd hh24:mi:ss')) AS WSB,");
				
				builder_yt.append("(select count(*) from bdck.bdcs_sjsb t "
						+ "left join bdc_workflow.wfi_proinst h on t.proinstid = h.proinst_id "
						+ "left join bdck.bdcs_xmxx k on k.PROJECT_ID = h.FILE_NUMBER where t.successflag = '1' AND k.SFDB = '1'");
				if(dedjlx!=null && !dedjlx.equals("")){
					builder_yt.append(" and  k.djlx in ("+dedjlx+")");
				}
				builder_yt.append(builder_dxfssj+") AS YSB");
				builder_yt.append(" from bdck.bdcs_const p where p.constslsid = (select constslsid from bdck.BDCS_CONSTCLS where constclstype ='XZQH')");
				builder_yt.append(" and p.bz='1'");
				
				/**
				 * 贵溪
				 */
				StringBuilder builder_gx = new StringBuilder();
				builder_gx.append("select consttrans,");
				builder_gx.append("(select count(*) from bdck.bdcs_sjsb@to_orcl6_bdck t "
						+ "left join bdc_workflow.wfi_proinst@to_orcl6_bdc_workflow h on t.proinstid = h.proinst_id "
						+ "left join bdck.bdcs_xmxx@to_orcl6_bdck k on k.PROJECT_ID = h.FILE_NUMBER where t.successflag = '0' AND k.SFDB = '1'");
				if(dedjlx!=null && !dedjlx.equals("")){
					builder_gx.append(" and  k.djlx in ("+dedjlx+")");
				}
				builder_gx.append(builder_dxfssj+") AS SBSB,");
				builder_gx.append("(select count(*) from bdck.bdcs_xmxx@to_orcl6_bdck k  "
						+ "left join bdc_workflow.wfi_proinst@to_orcl6_bdc_workflow h on k.PROJECT_ID = h.FILE_NUMBER "
						+ "left join bdck.bdcs_sjsb@to_orcl6_bdck t on h.proinst_id = t.proinstid  where t.id is null ");
				if(dedjlx!=null && !dedjlx.equals("")){
					builder_gx.append(" and  k.djlx in ("+dedjlx+")");
				}
				builder_gx.append(" and k.slsj between to_date('"+tjsjks+" "+"00:00:00"+"','yyyy-mm-dd hh24:mi:ss')and to_date('"+tjsjjz+" "+"23:59:59"+"','yyyy-mm-dd hh24:mi:ss')) AS WSB,");
				
				builder_gx.append("(select count(*) from bdck.bdcs_sjsb@to_orcl6_bdck t "
						+ "left join bdc_workflow.wfi_proinst@to_orcl6_bdc_workflow h on t.proinstid = h.proinst_id "
						+ "left join bdck.bdcs_xmxx@to_orcl6_bdck k on k.PROJECT_ID = h.FILE_NUMBER where t.successflag = '1' AND k.SFDB = '1'");
				if(dedjlx!=null && !dedjlx.equals("")){
					builder_gx.append(" and  k.djlx in ("+dedjlx+")");
				}
				builder_gx.append(builder_dxfssj+") AS YSB");
				builder_gx.append(" from bdck.bdcs_const p where p.constslsid = (select constslsid from bdck.BDCS_CONSTCLS where constclstype ='XZQH')");
				builder_gx.append(" and p.bz='2'");
				
				
				/**
				 * 余江
				 */
				StringBuilder builder_yj = new StringBuilder();
				builder_yj.append("select consttrans,");
				builder_yj.append("(select count(*) from bdck.bdcs_sjsb@to_orcl7_bdck t "
						+ "left join bdc_workflow.wfi_proinst@to_orcl7_bdc_workflow h on t.proinstid = h.proinst_id "
						+ "left join bdck.bdcs_xmxx@to_orcl7_bdck k on k.PROJECT_ID = h.FILE_NUMBER where t.successflag = '0' AND k.SFDB = '1'");
				if(dedjlx!=null && !dedjlx.equals("")){
					builder_yj.append(" and  k.djlx in ("+dedjlx+")");
				}
				builder_yj.append(builder_dxfssj+") AS SBSB,");
				builder_yj.append("(select count(*) from bdck.bdcs_xmxx@to_orcl7_bdck k  "
						+ "left join bdc_workflow.wfi_proinst@to_orcl7_bdc_workflow h on k.PROJECT_ID = h.FILE_NUMBER "
						+ "left join bdck.bdcs_sjsb@to_orcl7_bdck t on h.proinst_id = t.proinstid  where t.id is null ");
				if(dedjlx!=null && !dedjlx.equals("")){
					builder_yj.append(" and  k.djlx in ("+dedjlx+")");
				}
				builder_yj.append(" and k.slsj between to_date('"+tjsjks+" "+"00:00:00"+"','yyyy-mm-dd hh24:mi:ss')and to_date('"+tjsjjz+" "+"23:59:59"+"','yyyy-mm-dd hh24:mi:ss')) AS WSB,");
				
				builder_yj.append("(select count(*) from bdck.bdcs_sjsb@to_orcl7_bdck t "
						+ "left join bdc_workflow.wfi_proinst@to_orcl7_bdc_workflow h on t.proinstid = h.proinst_id "
						+ "left join bdck.bdcs_xmxx@to_orcl7_bdck k on k.PROJECT_ID = h.FILE_NUMBER where t.successflag = '1' AND k.SFDB = '1'");
				if(dedjlx!=null && !dedjlx.equals("")){
					builder_yj.append(" and  k.djlx in ("+dedjlx+")");
				}
				builder_yj.append(builder_dxfssj+") AS YSB");
				builder_yj.append(" from bdck.bdcs_const p where p.constslsid = (select constslsid from bdck.BDCS_CONSTCLS where constclstype ='XZQH')");
				builder_yj.append(" and p.bz='3'");
				
				
				
				StringBuilder builder_count_yt= new StringBuilder();
				builder_count_yt.append(" SUM(CASE WHEN aa.successflag = '0' THEN 1 ELSE 0 END) AS SBSB,"
						/*+ "SUM(CASE WHEN aa.status = '未上报' THEN 1 ELSE 0 END) AS WSB,"*/
						+ "SUM(CASE WHEN aa.successflag = '1' THEN 1 ELSE 0 END) AS YSB from (");
				builder_count_yt.append(" select k.DJLX, k.QLLX,t.successflag,t.operatetime from bdck.bdcs_xmxx k  "
						+ "left join bdc_workflow.wfi_proinst h on k.PROJECT_ID = h.FILE_NUMBER "
						+ "left join bdck.bdcs_sjsb t on h.proinst_id = t.proinstid  where t.id is null "
						+ "and t.operatetime is not null AND k.SFDB = '1'");
				if(dedjlx!=null && !dedjlx.equals("")){
					builder_count_yt.append(" and  k.djlx in ("+dedjlx+")");
				}
				builder_count_yt.append(" and k.slsj between to_date('"+tjsjks+" "+"00:00:00"+"','yyyy-mm-dd hh24:mi:ss')and to_date('"+tjsjjz+" "+"23:59:59"+"','yyyy-mm-dd hh24:mi:ss')");
				builder_count_yt.append(" union all "
						+ "select k.DJLX, k.QLLX,t.successflag,t.operatetime from bdck.bdcs_xmxx k  "
						+ "left join bdc_workflow.wfi_proinst h on k.PROJECT_ID = h.FILE_NUMBER "
						+ "left join bdck.bdcs_sjsb t on h.proinst_id = t.proinstid  where t.id is not null "
						+ "and t.operatetime is not null AND k.SFDB = '1'");
				if(dedjlx!=null && !dedjlx.equals("")){
					builder_count_yt.append(" and  k.djlx in ("+dedjlx+")");
				}
				builder_count_yt.append(builder_dxfssj);
				
				/**
				 * guixi
				 */
				StringBuilder builder_count_gx= new StringBuilder();
			/*	builder_count_gx.append(" SUM(CASE WHEN aa.successflag = '0' THEN 1 ELSE 0 END) AS SBSB,"
						+ "SUM(CASE WHEN aa.status = '未上报' THEN 1 ELSE 0 END) AS WSB,"
						+ "SUM(CASE WHEN aa.status = '已上报' THEN 1 ELSE 0 END) AS YSB from (");*/
				builder_count_gx.append(" select k.DJLX, k.QLLX, t.successflag,t.operatetime from bdck.bdcs_xmxx@to_orcl6_bdck k  "
						+ "left join bdc_workflow.wfi_proinst@to_orcl6_bdc_workflow h on k.PROJECT_ID = h.FILE_NUMBER "
						+ "left join bdck.bdcs_sjsb@to_orcl6_bdck t on h.proinst_id = t.proinstid  where t.id is null "
						+ "and t.operatetime is not null AND k.SFDB = '1'");
				if(dedjlx!=null && !dedjlx.equals("")){
					builder_count_gx.append(" and  k.djlx in ("+dedjlx+")");
				}
				builder_count_gx.append(" and k.slsj between to_date('"+tjsjks+" "+"00:00:00"+"','yyyy-mm-dd hh24:mi:ss')and to_date('"+tjsjjz+" "+"23:59:59"+"','yyyy-mm-dd hh24:mi:ss')");
				builder_count_gx.append(" union all "
						+ "select k.DJLX, k.QLLX, t.successflag,t.operatetime from bdck.bdcs_xmxx@to_orcl6_bdck k  "
						+ "left join bdc_workflow.wfi_proinst@to_orcl6_bdc_workflow h on k.PROJECT_ID = h.FILE_NUMBER "
						+ "left join bdck.bdcs_sjsb@to_orcl6_bdck t on h.proinst_id = t.proinstid  where t.id is not null "
						+ "and t.operatetime is not null AND k.SFDB = '1'");
				if(dedjlx!=null && !dedjlx.equals("")){
					builder_count_gx.append(" and  k.djlx in ("+dedjlx+")");
				}
				builder_count_gx.append(builder_dxfssj);
				
				//余江
				StringBuilder builder_count_yj= new StringBuilder();
				/*	builder_count_gx.append(" SUM(CASE WHEN aa.successflag = '0' THEN 1 ELSE 0 END) AS SBSB,"
							+ "SUM(CASE WHEN aa.status = '未上报' THEN 1 ELSE 0 END) AS WSB,"
							+ "SUM(CASE WHEN aa.status = '已上报' THEN 1 ELSE 0 END) AS YSB from (");*/
				builder_count_yj.append(" select k.DJLX, k.QLLX, t.successflag,t.operatetime from bdck.bdcs_xmxx@to_orcl7_bdck k  "
							+ "left join bdc_workflow.wfi_proinst@to_orcl7_bdc_workflow h on k.PROJECT_ID = h.FILE_NUMBER "
							+ "left join bdck.bdcs_sjsb@to_orcl7_bdck t on h.proinst_id = t.proinstid  where t.id is null "
							+ "and t.operatetime is not null AND k.SFDB = '1'");
					if(dedjlx!=null && !dedjlx.equals("")){
						builder_count_yj.append(" and  k.djlx in ("+dedjlx+")");
					}
					builder_count_yj.append(" and k.slsj between to_date('"+tjsjks+" "+"00:00:00"+"','yyyy-mm-dd hh24:mi:ss')and to_date('"+tjsjjz+" "+"23:59:59"+"','yyyy-mm-dd hh24:mi:ss')");
					builder_count_yj.append(" union all "
							+ "select k.DJLX, k.QLLX, t.successflag,t.operatetime from bdck.bdcs_xmxx@to_orcl7_bdck k  "
							+ "left join bdc_workflow.wfi_proinst@to_orcl7_bdc_workflow h on k.PROJECT_ID = h.FILE_NUMBER "
							+ "left join bdck.bdcs_sjsb@to_orcl7_bdck t on h.proinst_id = t.proinstid  where t.id is not null "
							+ "and t.operatetime is not null AND k.SFDB = '1'");
					if(dedjlx!=null && !dedjlx.equals("")){
						builder_count_yj.append(" and  k.djlx in ("+dedjlx+")");
					}
					builder_count_yj.append(builder_dxfssj);
				
			    	    
			if(tjlx.equals("0")){
				//行政区划分类
				if(deptid.equals("0")){
					//全市
  				    build.append(builder_yt);
  				    build.append(" UNION ALL ");
					build.append(builder_gx);
					build.append(" UNION ALL ");
					build.append(builder_yj);
				}
				if(deptid.equals("1")){
					//鹰潭
					build.append(builder_yt);
				}
				if(deptid.equals("2")){
					//贵溪
					
					build.append(builder_gx);
				}
				if(deptid.equals("3")){
					//余江
					
					build.append(builder_yj);
				}
				if(deptid.equals("2,1")){
					build.append(builder_yt);
  				    build.append(" UNION ALL ");
					build.append(builder_gx);
				}
				if(deptid.equals("3,1")){
					build.append(builder_yt);
  				    build.append(" UNION ALL ");
					build.append(builder_yj);
				}
				if(deptid.equals("3,2")){
					build.append(builder_gx);
  				    build.append(" UNION ALL ");
					build.append(builder_yj);
				}
			}
			if(tjlx.equals("1")){
				//登记类型分类
				if(deptid.equals("0")){
					//全市
  				    build.append(" SELECT aa.DJLX, aa.QLLX,");
  				    build.append(builder_count_yt);
  				    build.append(" union all ");
  				    build.append(builder_count_gx);
  				    build.append(" union all ");
  				//    build.append(" ) aa group by aa.DJLX, aa.QLLX ORDER BY aa.DJLX, TO_NUMBER(aa.QLLX)");
  				    build.append(builder_count_yj);
				    build.append(" ) aa group by aa.DJLX, aa.QLLX ORDER BY aa.DJLX, TO_NUMBER(aa.QLLX)");
  				   
				}
				if(deptid.equals("1")){
					//鹰潭
  				    build.append(" SELECT aa.DJLX, aa.QLLX,");
  				    build.append(builder_count_yt);
				    build.append(" ) aa group by aa.DJLX, aa.QLLX ORDER BY aa.DJLX, TO_NUMBER(aa.QLLX)");
				}
				if(deptid.equals("2")){
					//贵溪
  				    build.append(" SELECT aa.DJLX, aa.QLLX,");
  				    build.append(" SUM(CASE WHEN aa.successflag = '0' THEN 1 ELSE 0 END) AS SBSB,"
					+ "SUM(CASE WHEN aa.status = '未上报' THEN 1 ELSE 0 END) AS WSB,"
					+ "SUM(CASE WHEN aa.status = '已上报' THEN 1 ELSE 0 END) AS YSB from (");
  				    build.append(builder_count_gx);
				    build.append(" ) aa group by aa.DJLX, aa.QLLX ORDER BY aa.DJLX, TO_NUMBER(aa.QLLX)");
				}
				if(deptid.equals("3")){
					//余江
  				    build.append(" SELECT aa.DJLX, aa.QLLX,");
  				    build.append(" SUM(CASE WHEN aa.successflag = '0' THEN 1 ELSE 0 END) AS SBSB,"
					+ "SUM(CASE WHEN aa.status = '未上报' THEN 1 ELSE 0 END) AS WSB,"
					+ "SUM(CASE WHEN aa.status = '已上报' THEN 1 ELSE 0 END) AS YSB from (");
  				    build.append(builder_count_yj);
				    build.append(" ) aa group by aa.DJLX, aa.QLLX ORDER BY aa.DJLX, TO_NUMBER(aa.QLLX)");
				}
				if(deptid.equals("2,1")){
					build.append(" SELECT aa.DJLX, aa.QLLX,");
  				    build.append(builder_count_yt);
  				    build.append(" union all ");
  				    build.append(builder_count_gx);
  				    build.append(" ) aa group by aa.DJLX, aa.QLLX ORDER BY aa.DJLX, TO_NUMBER(aa.QLLX)");
				}
				if(deptid.equals("3,1")){
					build.append(" SELECT aa.DJLX, aa.QLLX,");
  				    build.append(builder_count_yt);
  				    build.append(" union all ");
  				    build.append(builder_count_yj);
  				    build.append(" ) aa group by aa.DJLX, aa.QLLX ORDER BY aa.DJLX, TO_NUMBER(aa.QLLX)");
				}
				if(deptid.equals("3,2")){
					build.append(" SELECT aa.DJLX, aa.QLLX,");
  				    build.append(builder_count_yj);
  				    build.append(" union all ");
  				    build.append(builder_count_gx);
  				    build.append(" ) aa group by aa.DJLX, aa.QLLX ORDER BY aa.DJLX, TO_NUMBER(aa.QLLX)");
				}
			}
			if(tjlx.equals("2")){
				//年度
				if(deptid.equals("0")){
					//全市
				    build.append(" select TO_CHAR(aa.operatetime,'YYYY') YEAR,");
				    build.append(builder_count_yt);
				    build.append(" UNION ALL ");
				    build.append(builder_count_gx);
				    build.append(" UNION ALL ");
				    build.append(builder_count_yj);
				    build.append(" ) aa GROUP BY TO_CHAR(aa.operatetime,'YYYY') ORDER BY TO_CHAR(aa.operatetime,'YYYY')");
	    
				}
				if(deptid.equals("1")){
					//鹰潭
				    build.append(" select TO_CHAR(aa.operatetime,'YYYY') YEAR,");
				    build.append(builder_count_yt);
				    build.append(" ) aa GROUP BY TO_CHAR(aa.operatetime,'YYYY') ORDER BY TO_CHAR(aa.operatetime,'YYYY')");
				}
				if(deptid.equals("2")){
					//贵溪
				    build.append(" select TO_CHAR(aa.operatetime,'YYYY') YEAR,");
				    build.append(" SUM(CASE WHEN aa.successflag = '0' THEN 1 ELSE 0 END) AS SBSB,"
							+ "SUM(CASE WHEN aa.status = '未上报' THEN 1 ELSE 0 END) AS WSB,"
							+ "SUM(CASE WHEN aa.status = '已上报' THEN 1 ELSE 0 END) AS YSB from (");
				    build.append(builder_count_gx);
				    build.append(" ) aa GROUP BY TO_CHAR(aa.operatetime,'YYYY') ORDER BY TO_CHAR(aa.operatetime,'YYYY')");
				}
				if(deptid.equals("3")){
					//余江
				    build.append(" select TO_CHAR(aa.operatetime,'YYYY') YEAR,");
				    build.append(" SUM(CASE WHEN aa.successflag = '0' THEN 1 ELSE 0 END) AS SBSB,"
							+ "SUM(CASE WHEN aa.status = '未上报' THEN 1 ELSE 0 END) AS WSB,"
							+ "SUM(CASE WHEN aa.status = '已上报' THEN 1 ELSE 0 END) AS YSB from (");
				    build.append(builder_count_yj);
				    build.append(" ) aa GROUP BY TO_CHAR(aa.operatetime,'YYYY') ORDER BY TO_CHAR(aa.operatetime,'YYYY')");
				}
				if(deptid.equals("2,1")){
					build.append(" select TO_CHAR(aa.operatetime,'YYYY') YEAR,");
				    build.append(builder_count_yt);
				    build.append(" UNION ALL ");
				    build.append(builder_count_gx);
				    build.append(" ) aa GROUP BY TO_CHAR(aa.operatetime,'YYYY') ORDER BY TO_CHAR(aa.operatetime,'YYYY')");
				}
				if(deptid.equals("3,1")){
					build.append(" select TO_CHAR(aa.operatetime,'YYYY') YEAR,");
				    build.append(builder_count_yt);
				    build.append(" UNION ALL ");
				    build.append(builder_count_yj);
				    build.append(" ) aa GROUP BY TO_CHAR(aa.operatetime,'YYYY') ORDER BY TO_CHAR(aa.operatetime,'YYYY')");
				}
				if(deptid.equals("3,2")){
					build.append(" select TO_CHAR(aa.operatetime,'YYYY') YEAR,");
				    build.append(builder_count_yj);
				    build.append(" UNION ALL ");
				    build.append(builder_count_gx);
				    build.append(" ) aa GROUP BY TO_CHAR(aa.operatetime,'YYYY') ORDER BY TO_CHAR(aa.operatetime,'YYYY')");
				}
			}
			if(tjlx.equals("3")){
			   //季度
				if(deptid.equals("0")){
					//全市
				    build.append(" select TO_CHAR(aa.operatetime,'YYYY') YEAR,TO_CHAR(aa.operatetime,'Q') TIME,");
				    build.append(builder_count_yt);
				    build.append(" UNION ALL ");   
				    build.append(builder_count_gx);
				    build.append(" UNION ALL ");   
				    build.append(builder_count_yj);
				    build.append(" ) aa GROUP BY TO_CHAR(aa.operatetime,'Q'),TO_CHAR(aa.operatetime,'YYYY') ORDER BY TO_CHAR(aa.operatetime,'YYYY') ,TO_CHAR(aa.operatetime,'Q')");
	    
				}
				if(deptid.equals("1")){
					//鹰潭
				    build.append(" select TO_CHAR(aa.operatetime,'YYYY') YEAR,TO_CHAR(aa.operatetime,'Q') TIME,");
				    build.append(builder_count_yt);
				    build.append(" ) aa GROUP BY TO_CHAR(aa.operatetime,'Q'),TO_CHAR(aa.operatetime,'YYYY') ORDER BY TO_CHAR(aa.operatetime,'YYYY') ,TO_CHAR(aa.operatetime,'Q')");
				}
				if(deptid.equals("2")){
					//贵溪
				    build.append(" select TO_CHAR(aa.operatetime,'YYYY') YEAR,TO_CHAR(aa.operatetime,'Q') TIME,");
				    build.append(" SUM(CASE WHEN aa.successflag = '0' THEN 1 ELSE 0 END) AS SBSB,"
							+ "SUM(CASE WHEN aa.status = '未上报' THEN 1 ELSE 0 END) AS WSB,"
							+ "SUM(CASE WHEN aa.status = '已上报' THEN 1 ELSE 0 END) AS YSB from (");
				    build.append(builder_count_gx);
				    build.append(" ) aa GROUP BY TO_CHAR(aa.operatetime,'Q'),TO_CHAR(aa.operatetime,'YYYY') ORDER BY TO_CHAR(aa.operatetime,'YYYY') ,TO_CHAR(aa.operatetime,'Q')");
				}
				if(deptid.equals("3")){
					//余江
				    build.append(" select TO_CHAR(aa.operatetime,'YYYY') YEAR,TO_CHAR(aa.operatetime,'Q') TIME,");
				    build.append(" SUM(CASE WHEN aa.successflag = '0' THEN 1 ELSE 0 END) AS SBSB,"
							+ "SUM(CASE WHEN aa.status = '未上报' THEN 1 ELSE 0 END) AS WSB,"
							+ "SUM(CASE WHEN aa.status = '已上报' THEN 1 ELSE 0 END) AS YSB from (");
				    build.append(builder_count_yj);
				    build.append(" ) aa GROUP BY TO_CHAR(aa.operatetime,'Q'),TO_CHAR(aa.operatetime,'YYYY') ORDER BY TO_CHAR(aa.operatetime,'YYYY') ,TO_CHAR(aa.operatetime,'Q')");
				}
				if(deptid.equals("2,1")){
					
					build.append(" select TO_CHAR(aa.operatetime,'YYYY') YEAR,TO_CHAR(aa.operatetime,'Q') TIME,");
				    build.append(builder_count_yt);
				    build.append(" UNION ALL ");   
				    build.append(builder_count_gx);
				    build.append(" ) aa GROUP BY TO_CHAR(aa.operatetime,'Q'),TO_CHAR(aa.operatetime,'YYYY') ORDER BY TO_CHAR(aa.operatetime,'YYYY') ,TO_CHAR(aa.operatetime,'Q')");
	    
				}
				if(deptid.equals("3,1")){
									
									build.append(" select TO_CHAR(aa.operatetime,'YYYY') YEAR,TO_CHAR(aa.operatetime,'Q') TIME,");
								    build.append(builder_count_yt);
								    build.append(" UNION ALL ");   
								    build.append(builder_count_yj);
								    build.append(" ) aa GROUP BY TO_CHAR(aa.operatetime,'Q'),TO_CHAR(aa.operatetime,'YYYY') ORDER BY TO_CHAR(aa.operatetime,'YYYY') ,TO_CHAR(aa.operatetime,'Q')");
					    
								}
				if(deptid.equals("3,2")){
					
					build.append(" select TO_CHAR(aa.operatetime,'YYYY') YEAR,TO_CHAR(aa.operatetime,'Q') TIME,");
				    build.append(builder_count_yj);
				    build.append(" UNION ALL ");   
				    build.append(builder_count_gx);
				    build.append(" ) aa GROUP BY TO_CHAR(aa.operatetime,'Q'),TO_CHAR(aa.operatetime,'YYYY') ORDER BY TO_CHAR(aa.operatetime,'YYYY') ,TO_CHAR(aa.operatetime,'Q')");
				
				}
			}
			if(tjlx.equals("4")){
				//月份 
				if(deptid.equals("0")){
					//全市
					build.append(" select TO_CHAR(aa.operatetime,'YYYY') YEAR,TO_CHAR(aa.operatetime,'Q') TIME,TO_CHAR(aa.operatetime,'mm') MONTH,");
					build.append(builder_count_yt);	
					build.append(" UNION ALL ");
					build.append(builder_count_gx);	
					build.append(" UNION ALL ");
					build.append(builder_count_yj);	
					build.append(" ) aa GROUP BY TO_CHAR(aa.operatetime,'mm'),TO_CHAR(aa.operatetime,'Q'),TO_CHAR(aa.operatetime,'YYYY')");
					build.append(" ORDER BY TO_CHAR(aa.operatetime,'YYYY') ,TO_CHAR(aa.operatetime,'Q'),TO_CHAR(aa.operatetime,'mm')");
	
				}
				if(deptid.equals("1")){
					//鹰潭
					build.append(" select TO_CHAR(aa.operatetime,'YYYY') YEAR,TO_CHAR(aa.operatetime,'Q') TIME,TO_CHAR(aa.operatetime,'mm') MONTH,");
					build.append(builder_count_yt);	
					build.append(" ) aa GROUP BY TO_CHAR(aa.operatetime,'mm'),TO_CHAR(aa.operatetime,'Q'),TO_CHAR(aa.operatetime,'YYYY')");
					build.append(" ORDER BY TO_CHAR(aa.operatetime,'YYYY') ,TO_CHAR(aa.operatetime,'Q'),TO_CHAR(aa.operatetime,'mm')");
				}
				if(deptid.equals("2")){
					//贵溪
					build.append(" select TO_CHAR(aa.operatetime,'YYYY') YEAR,TO_CHAR(aa.operatetime,'Q') TIME,TO_CHAR(aa.operatetime,'mm') MONTH,");
					build.append(" SUM(CASE WHEN aa.successflag = '0' THEN 1 ELSE 0 END) AS SBSB,"
								+ "SUM(CASE WHEN aa.status = '未上报' THEN 1 ELSE 0 END) AS WSB,"
								+ "SUM(CASE WHEN aa.status = '已上报' THEN 1 ELSE 0 END) AS YSB from (");
					build.append(builder_count_gx);	
					build.append(" ) aa GROUP BY TO_CHAR(aa.operatetime,'mm'),TO_CHAR(aa.operatetime,'Q'),TO_CHAR(aa.operatetime,'YYYY')");
					build.append(" ORDER BY TO_CHAR(aa.operatetime,'YYYY') ,TO_CHAR(aa.operatetime,'Q'),TO_CHAR(aa.operatetime,'mm')");
				}
				if(deptid.equals("3")){
					//余江
					build.append(" select TO_CHAR(aa.operatetime,'YYYY') YEAR,TO_CHAR(aa.operatetime,'Q') TIME,TO_CHAR(aa.operatetime,'mm') MONTH,");
					build.append(" SUM(CASE WHEN aa.successflag = '0' THEN 1 ELSE 0 END) AS SBSB,"
								+ "SUM(CASE WHEN aa.status = '未上报' THEN 1 ELSE 0 END) AS WSB,"
								+ "SUM(CASE WHEN aa.status = '已上报' THEN 1 ELSE 0 END) AS YSB from (");
					build.append(builder_count_yj);	
					build.append(" ) aa GROUP BY TO_CHAR(aa.operatetime,'mm'),TO_CHAR(aa.operatetime,'Q'),TO_CHAR(aa.operatetime,'YYYY')");
					build.append(" ORDER BY TO_CHAR(aa.operatetime,'YYYY') ,TO_CHAR(aa.operatetime,'Q'),TO_CHAR(aa.operatetime,'mm')");
				}
				if(deptid.equals("2,1")){
					build.append(" select TO_CHAR(aa.operatetime,'YYYY') YEAR,TO_CHAR(aa.operatetime,'Q') TIME,TO_CHAR(aa.operatetime,'mm') MONTH,");
					build.append(builder_count_yt);	
					build.append(" UNION ALL ");
					build.append(builder_count_gx);	
					build.append(" ) aa GROUP BY TO_CHAR(aa.operatetime,'mm'),TO_CHAR(aa.operatetime,'Q'),TO_CHAR(aa.operatetime,'YYYY')");
					build.append(" ORDER BY TO_CHAR(aa.operatetime,'YYYY') ,TO_CHAR(aa.operatetime,'Q'),TO_CHAR(aa.operatetime,'mm')");
	
				}
				if(deptid.equals("3,1")){
					build.append(" select TO_CHAR(aa.operatetime,'YYYY') YEAR,TO_CHAR(aa.operatetime,'Q') TIME,TO_CHAR(aa.operatetime,'mm') MONTH,");
					build.append(builder_count_yt);	
					build.append(" UNION ALL ");
					build.append(builder_count_yj);	
					build.append(" ) aa GROUP BY TO_CHAR(aa.operatetime,'mm'),TO_CHAR(aa.operatetime,'Q'),TO_CHAR(aa.operatetime,'YYYY')");
					build.append(" ORDER BY TO_CHAR(aa.operatetime,'YYYY') ,TO_CHAR(aa.operatetime,'Q'),TO_CHAR(aa.operatetime,'mm')");
	
				}
				if(deptid.equals("3,2")){
					build.append(" select TO_CHAR(aa.operatetime,'YYYY') YEAR,TO_CHAR(aa.operatetime,'Q') TIME,TO_CHAR(aa.operatetime,'mm') MONTH,");
					build.append(builder_count_yj);	
					build.append(" UNION ALL ");
					build.append(builder_count_gx);	
					build.append(" ) aa GROUP BY TO_CHAR(aa.operatetime,'mm'),TO_CHAR(aa.operatetime,'Q'),TO_CHAR(aa.operatetime,'YYYY')");
					build.append(" ORDER BY TO_CHAR(aa.operatetime,'YYYY') ,TO_CHAR(aa.operatetime,'Q'),TO_CHAR(aa.operatetime,'mm')");
	
				}
			}
			
			List<Map> maps = baseCommonDao.getDataListByFullSql(build.toString());
			
			if(!StringHelper.isEmpty(maps) && maps.size()>0)
			{
				for(Map map :maps)
				{
					String strdjlx=StringHelper.FormatByDatatype(map.get("DJLX"));
				    map.put("DJLX", ConstHelper.getNameByValue("DJLX", strdjlx));
					String strqllx=StringHelper.FormatByDatatype(map.get("QLLX"));
					if(DJLX.YGDJ.Value.equals(strdjlx) && QLLX.QTQL.Value.equals(strqllx))
					{
						map.put("QLLX", "转移预告");
					}
					else if(DJLX.CFDJ.Value.equals(strdjlx) && QLLX.CFZX.Value.equals(strqllx))
					{
						map.put("QLLX", "解封");
					}
					else if(DJLX.CFDJ.Value.equals(strdjlx) && QLLX.QTQL.Value.equals(strqllx))
					{
						map.put("QLLX", "查封");
					}
					else
					{
					map.put("QLLX", ConstHelper.getNameByValue("QLLX", strqllx));
					}
				}
			}
			m.setRows(maps);
			m.setTotal(maps.size());
		}catch(Exception e){
			m=null;
		}
		return m;
	}
	/**
	 * 
	 *短信统计sql
	 */
	@Override
	public Message GetDXTJ(String tjsjks, String tjsjjz, String deptid , String tjlx) {
		StringBuilder build = new StringBuilder();
		StringBuilder build1 = new StringBuilder();
		Message m = new Message();
		try{
			StringBuilder builder_dxfssj = new StringBuilder();//流程开始时间
			//StringBuilder builder_end = new StringBuilder();//流程开始时间
			
			if(tjsjks != null || !"".equals(tjsjks)){
				builder_dxfssj.append(" AND v.dxfssj between to_date(");
				builder_dxfssj.append("'"+tjsjks+" "+"00:00:00"+"','yyyy-mm-dd hh24:mi:ss')and to_date( ");
				builder_dxfssj.append("'"+tjsjjz+" "+"23:59:59"+"','yyyy-mm-dd hh24:mi:ss')");
				/*builder_end.append(" AND v.actinst_end between to_date(");
				builder_end.append("'"+tjsjks+" "+"00:00:00"+"','yyyy-mm-dd hh24:mi:ss')and to_date( ");
				builder_end.append("'"+tjsjjz+" "+"23:59:59"+"','yyyy-mm-dd hh24:mi:ss')");*/
			}
			
			StringBuilder build_yt = new StringBuilder();//鹰潭
			build_yt.append("(select count(*) from sharesearch.dhxx v where status is not null ");
			build_yt.append(builder_dxfssj+") as HJ,");
			build_yt.append("(select count(*) from sharesearch.dhxx v where status = '发送成功' ");
			build_yt.append(builder_dxfssj+") as FSCG,");
			build_yt.append("(select count(*) from sharesearch.dhxx v where status = '发送失败' ");
			build_yt.append(builder_dxfssj+") as FSSB,");
			build_yt.append("(select count(*) from sharesearch.dhxx v where status is not null) as LHJ ,");
			build_yt.append("(select count(*) from sharesearch.dhxx v where status = '发送成功') as LFSCG,");
			build_yt.append("(select count(*) from sharesearch.dhxx v where status = '发送失败') as LFSSB ");
			
			StringBuilder build_gx = new StringBuilder();//贵溪
			build_gx.append("(select count(*) from sharesearch.dhxx@to_orcl6_sharesearch v where status is not null ");
			build_gx.append(builder_dxfssj+") as HJ,");
			build_gx.append("(select count(*) from sharesearch.dhxx@to_orcl6_sharesearch v where status = '发送成功' ");
			build_gx.append(builder_dxfssj+") as FSCG,");
			build_gx.append("(select count(*) from sharesearch.dhxx@to_orcl6_sharesearch v where status = '发送失败' ");
			build_gx.append(builder_dxfssj+") as FSSB,");
			build_gx.append("(select count(*) from sharesearch.dhxx@to_orcl6_sharesearch v where status is not null) as LHJ,");
			build_gx.append("(select count(*) from sharesearch.dhxx@to_orcl6_sharesearch v where status = '发送成功') as LFSCG,");
			build_gx.append("(select count(*) from sharesearch.dhxx@to_orcl6_sharesearch v where status = '发送失败') as LFSSB ");
			
			StringBuilder build_yj = new StringBuilder();//余江
			build_yj.append("(select count(*) from sharesearch.dhxx@to_orcl7_sharesearch v where status is not null ");
			build_yj.append(builder_dxfssj+") as HJ,");
			build_yj.append("(select count(*) from sharesearch.dhxx@to_orcl7_sharesearch v where status = '发送成功' ");
			build_yj.append(builder_dxfssj+") as FSCG,");
			build_yj.append("(select count(*) from sharesearch.dhxx@to_orcl7_sharesearch v where status = '发送失败' ");
			build_yj.append(builder_dxfssj+") as FSSB,");
			build_yj.append("(select count(*) from sharesearch.dhxx@to_orcl7_sharesearch v where status is not null) as LHJ,");
			build_yj.append("(select count(*) from sharesearch.dhxx@to_orcl7_sharesearch v where status = '发送成功') as LFSCG,");
			build_yj.append("(select count(*) from sharesearch.dhxx@to_orcl7_sharesearch v where status = '发送失败') as LFSSB ");
			
			
			StringBuilder build_year = new StringBuilder();//年份
			build_year.append("select to_char(v.dxfssj,'yyyy') as YEARS,");
			build_year.append(" SUM(CASE WHEN status is not null "+builder_dxfssj+"THEN 1 ELSE 0 END) as HJ,");
			build_year.append(" SUM(CASE WHEN status = '发送成功' "+builder_dxfssj+" THEN 1 ELSE 0 END) as FSCG,");
			build_year.append(" SUM(CASE WHEN status = '发送失败' "+builder_dxfssj+" THEN 1 ELSE 0 END) as FSSB,");
			build_year.append(" SUM(CASE WHEN status is not null THEN 1 ELSE 0 END) as LHJ,");
			build_year.append(" SUM(CASE WHEN status = '发送成功' THEN 1 ELSE 0 END) as LFSCG,");
			build_year.append(" SUM(CASE WHEN status = '发送失败' THEN 1 ELSE 0 END) as LFSSB ");
			
			StringBuilder build_q = new StringBuilder();//季度
			build_q.append("select TO_CHAR(v.dxfssj,'YYYY') YEAR,TO_CHAR(v.dxfssj,'Q') TIME,");
			build_q.append(" SUM(CASE WHEN status is not null "+builder_dxfssj+"THEN 1 ELSE 0 END) as HJ,");
			build_q.append(" SUM(CASE WHEN status = '发送成功' "+builder_dxfssj+" THEN 1 ELSE 0 END) as FSCG,");
			build_q.append(" SUM(CASE WHEN status = '发送失败' "+builder_dxfssj+" THEN 1 ELSE 0 END) as FSSB,");
			build_q.append(" SUM(CASE WHEN status is not null THEN 1 ELSE 0 END) as LHJ,");
			build_q.append(" SUM(CASE WHEN status = '发送成功' THEN 1 ELSE 0 END) as LFSCG,");
			build_q.append(" SUM(CASE WHEN status = '发送失败' THEN 1 ELSE 0 END) as LFSSB ");
			
			StringBuilder build_month = new StringBuilder();//月份
			build_month.append("select TO_CHAR(v.dxfssj,'YYYY') YEAR,TO_CHAR(v.dxfssj,'Q') TIME,TO_CHAR(v.dxfssj,'mm') MONTH,");
			build_month.append(" SUM(CASE WHEN status is not null "+builder_dxfssj+"THEN 1 ELSE 0 END) as HJ,");
			build_month.append(" SUM(CASE WHEN status = '发送成功' "+builder_dxfssj+" THEN 1 ELSE 0 END) as FSCG,");
			build_month.append(" SUM(CASE WHEN status = '发送失败' "+builder_dxfssj+" THEN 1 ELSE 0 END) as FSSB,");
			build_month.append(" SUM(CASE WHEN status is not null THEN 1 ELSE 0 END) as LHJ,");
			build_month.append(" SUM(CASE WHEN status = '发送成功' THEN 1 ELSE 0 END) as LFSCG,");
			build_month.append(" SUM(CASE WHEN status = '发送失败' THEN 1 ELSE 0 END) as LFSSB ");
			
			if(tjlx.equals("0")){
			//按行政区划分类
				
				String[] str = deptid.split(",");
				for(int i=0;i<str.length;i++){
					
					if(str[i].equals("0")){
						//全市	
						 build.append("select * from(");
						 build.append("select consttrans,");
					     build.append(build_yt);
					     build.append(" from bdck.bdcs_const t where t.constslsid = (select constslsid from bdck.BDCS_CONSTCLS where constclstype ='XZQH')");
					     build.append(" and t.bz='1'");
					     build.append(" UNION ALL ");
					     build.append(" select consttrans,");
					     build.append(build_gx);
					     build.append(" from bdck.bdcs_const t where t.constslsid = (select constslsid from bdck.BDCS_CONSTCLS where constclstype ='XZQH')");
					     build.append(" and t.bz='2'");
					     build.append(" UNION ALL ");
					     build.append(" select consttrans,");
					     build.append(build_yj);
					     build.append(" from bdck.bdcs_const t where t.constslsid = (select constslsid from bdck.BDCS_CONSTCLS where constclstype ='XZQH')");
					     build.append(" and t.bz='3')");
					}else{
									
						if(str[i].equals("1")){
						 build.append("select consttrans,");
						 build.append(build_yt);
						 build.append(" from bdck.bdcs_const t where t.constslsid = (select constslsid from bdck.BDCS_CONSTCLS where constclstype ='XZQH')");
						 build.append(" and t.bz ='"+str[i]+"'");
						 if(str.length>1 && i!=str.length-1){
								build.append(" UNION ALL ");
							}
						}
						
						if(str[i].equals("2")){
						 build.append("select consttrans,");
						 build.append(build_gx);
						 build.append(" from bdck.bdcs_const t where t.constslsid = (select constslsid from bdck.BDCS_CONSTCLS where constclstype ='XZQH')");
						 build.append(" and t.bz ='"+str[i]+"'");
						 if(str.length>1 && i!=str.length-1){
								build.append(" UNION ALL ");
							}
						}
						if(str[i].equals("3")){
							 build.append("select consttrans,");
							 build.append(build_yj);
							 build.append(" from bdck.bdcs_const t where t.constslsid = (select constslsid from bdck.BDCS_CONSTCLS where constclstype ='XZQH')");
							 build.append(" and t.bz ='"+str[i]+"'");
							 if(str.length>1 && i!=str.length-1){
									build.append(" UNION ALL ");
								}
						}
					}
										
				}
		
			}else if(tjlx.equals("1")){
				//按年度分类
				String[] str = deptid.split(",");
				for(int i=0;i<str.length;i++){
					if(str[i].equals("0")){
						build.append("select years, sum(HJ) as HJ,sum(FSCG) as FSCG,sum(FSSB) as FSSB,sum(LHJ) as LHJ,sum(LFSCG) as LFSCG,sum(LFSSB) as LFSSB from (");
						build.append(build_year);
						build.append(" from sharesearch.dhxx v where v.dxfssj is not null "+builder_dxfssj+"  group by to_char(v.dxfssj,'yyyy') ");
						build.append(" UNION ALL ");
						build.append(build_year);
						build.append(" from sharesearch.dhxx@to_orcl6_sharesearch v where v.dxfssj is not null "+builder_dxfssj+"  group by to_char(v.dxfssj,'yyyy') ");
						build.append(" UNION ALL ");
						build.append(build_year);
						build.append(" from sharesearch.dhxx@to_orcl7_sharesearch v where v.dxfssj is not null "+builder_dxfssj+"  group by to_char(v.dxfssj,'yyyy') ) group by YEARS ORDER BY YEARS ");
				   }else{
					
						if(str[i].equals("1")){
							//build.append("select years, sum(HJ) as HJ,sum(FSCG) as FSCG,sum(FSSB) as FSSB,sum(LHJ) as LHJ,sum(LFSCG) as LFSCG,sum(LFSSB) as LFSSB from (");
							build.append(build_year);
							build.append(" from sharesearch.dhxx v where v.dxfssj is not null "+builder_dxfssj+"  group by to_char(v.dxfssj,'yyyy') ");
							 if(str.length>1 && i!=str.length-1){
									build.append(" UNION ALL ");
								}else{
									build.append(" ORDER BY YEARS");
								}
						}
							
						if(str[i].equals("2")){
							
							build.append(build_year);
							build.append(" from sharesearch.dhxx@to_orcl6_sharesearch v where v.dxfssj is not null "+builder_dxfssj+"  group by to_char(v.dxfssj,'yyyy') ");
							 if(str.length>1 && i!=str.length-1){
									build.append(" UNION ALL ");
								}else{
									build.append(" ORDER BY YEARS");
								}
						}
						
	                    if(str[i].equals("3")){
							
							build.append(build_year);
							build.append(" from sharesearch.dhxx@to_orcl7_sharesearch v where v.dxfssj is not null "+builder_dxfssj+"  group by to_char(v.dxfssj,'yyyy') ");
							 if(str.length>1 && i!=str.length-1){
									build.append(" UNION ALL ");
								}else{
									build.append(" ORDER BY YEARS");
								}
						}
                  }
				}
				
			}else if(tjlx.equals("2")){
				//按季度分类
				String[] str = deptid.split(",");
				for(int i=0;i<str.length;i++){
					if(str[i].equals("0")){
						build.append("select YEAR,TIME ,sum(HJ) as HJ,sum(FSCG) as FSCG,sum(FSSB) as FSSB,sum(LHJ) as LHJ,sum(LFSCG) as LFSCG,sum(LFSSB) as LFSSB from (");
						build.append(build_q);
						build.append(" from sharesearch.dhxx v where v.dxfssj is not null "+builder_dxfssj+"  GROUP BY TO_CHAR(v.dxfssj,'Q'),TO_CHAR(v.dxfssj,'YYYY') ");
						build.append(" UNION ALL ");
						build.append(build_q);
						build.append(" from sharesearch.dhxx@to_orcl6_sharesearch v where v.dxfssj is not null "+builder_dxfssj+"  GROUP BY TO_CHAR(v.dxfssj,'Q'),TO_CHAR(v.dxfssj,'YYYY') ");
						build.append(" UNION ALL ");
						build.append(build_q);
						build.append(" from sharesearch.dhxx@to_orcl7_sharesearch v where v.dxfssj is not null "+builder_dxfssj+"  GROUP BY TO_CHAR(v.dxfssj,'Q'),TO_CHAR(v.dxfssj,'YYYY') ) GROUP BY TIME,YEAR ORDER BY YEAR ,TIME");
					}else{
					
						if(str[i].equals("1")){
							
							build.append(build_q);
							build.append(" from sharesearch.dhxx v where v.dxfssj is not null "+builder_dxfssj+"  GROUP BY TO_CHAR(v.dxfssj,'Q'),TO_CHAR(v.dxfssj,'YYYY') ");
							 if(str.length>1 && i!=str.length-1){
									build.append(" UNION ALL ");
								}else{
									build.append(" ORDER BY YEAR ,TIME");
								}
						}
							
						if(str[i].equals("2")){
							
							build.append(build_q);
							build.append(" from sharesearch.dhxx@to_orcl6_sharesearch v where v.dxfssj is not null  "+builder_dxfssj+"  GROUP BY TO_CHAR(v.dxfssj,'Q'),TO_CHAR(v.dxfssj,'YYYY') ");
							 if(str.length>1 && i!=str.length-1){
									build.append(" UNION ALL ");
								}else{
									build.append(" ORDER BY YEAR ,TIME");
								}
						}
	                    if(str[i].equals("3")){
							
							build.append(build_q);
							build.append(" from sharesearch.dhxx@to_orcl7_sharesearch v where v.dxfssj is not null  "+builder_dxfssj+"  GROUP BY TO_CHAR(v.dxfssj,'Q'),TO_CHAR(v.dxfssj,'YYYY') ");
							 if(str.length>1 && i!=str.length-1){
									build.append(" UNION ALL ");
								}else{
									build.append(" ORDER BY YEAR ,TIME");
								}
						}
                  }
				}
				
			}else if(tjlx.equals("3")){
				 //按月份分类
				String[] str = deptid.split(",");
				for(int i=0;i<str.length;i++){
					if(str[i].equals("0")){
						build.append("select YEAR,TIME,MONTH,sum(HJ) as HJ,sum(FSCG) as FSCG,sum(FSSB) as FSSB,sum(LHJ) as LHJ,sum(LFSCG) as LFSCG,sum(LFSSB) as LFSSB from (");
						build.append(build_month);
						build.append(" from sharesearch.dhxx v where v.dxfssj is not null "+builder_dxfssj+"  GROUP BY TO_CHAR(v.dxfssj,'mm'),TO_CHAR(v.dxfssj,'Q'),TO_CHAR(v.dxfssj,'YYYY') ");
						build.append(" UNION ALL ");
						build.append(build_month);
						build.append(" from sharesearch.dhxx@to_orcl6_sharesearch v where v.dxfssj is not null "+builder_dxfssj+"  GROUP BY TO_CHAR(v.dxfssj,'mm'),TO_CHAR(v.dxfssj,'Q'),TO_CHAR(v.dxfssj,'YYYY') ");
						build.append(" UNION ALL ");
						build.append(build_month);
						build.append(" from sharesearch.dhxx@to_orcl7_sharesearch v where v.dxfssj is not null "+builder_dxfssj+"  GROUP BY TO_CHAR(v.dxfssj,'mm'),TO_CHAR(v.dxfssj,'Q'),TO_CHAR(v.dxfssj,'YYYY') ) GROUP BY MONTH,TIME,YEAR ORDER BY YEAR ,TIME,MONTH");
					}else{
					
						if(str[i].equals("1")){
							
							build.append(build_month);
							build.append(" from sharesearch.dhxx v where v.dxfssj is not null "+builder_dxfssj+"  GROUP BY TO_CHAR(v.dxfssj,'mm'),TO_CHAR(v.dxfssj,'Q'),TO_CHAR(v.dxfssj,'YYYY') ");
							 if(str.length>1 && i!=str.length-1){
									build.append(" UNION ALL ");
								}else{
									build.append(" ORDER BY YEAR ,TIME,MONTH ");
								}
						}
							
						if(str[i].equals("2")){
							
							build.append(build_month);
							build.append(" from sharesearch.dhxx@to_orcl6_sharesearch v where v.dxfssj is not null"+builder_dxfssj+" GROUP BY TO_CHAR(v.dxfssj,'mm'),TO_CHAR(v.dxfssj,'Q'),TO_CHAR(v.dxfssj,'YYYY') ");
							 if(str.length>1 && i!=str.length-1){
									build.append(" UNION ALL ");
								}else{
									build.append("  ORDER BY YEAR ,TIME,MONTH ");
								}
						}
						
	                    if(str[i].equals("3")){
							
							build.append(build_month);
							build.append(" from sharesearch.dhxx@to_orcl7_sharesearch v where v.dxfssj is not null"+builder_dxfssj+" GROUP BY TO_CHAR(v.dxfssj,'mm'),TO_CHAR(v.dxfssj,'Q'),TO_CHAR(v.dxfssj,'YYYY') ");
							 if(str.length>1 && i!=str.length-1){
									build.append(" UNION ALL ");
								}else{
									build.append("  ORDER BY YEAR ,TIME,MONTH ");
								}
						}                    
                  }
				}			
	
			}
			
			List<Map> maps = baseCommonDao.getDataListByFullSql(build.toString());
			m.setRows(maps);
			m.setTotal(maps.size());
		}catch(Exception e){
			m=null;
		}
		return m;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public List<Map> GetDeptInfo() {
		StringBuilder build = new StringBuilder();
		List<Map> maps = null;
		try{
			build.append(" SELECT a.id,a.departmentname as text FROM smwb_framework.t_department a");
			maps = baseCommonDao.getDataListByFullSql(build.toString());
		}
		catch(Exception e){
			maps = null;
		}
		return maps;
	}
    /**
	 * 读取鹰潭行政区划
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public List<Tree> GetXzqhInfo() {
		StringBuilder build = new StringBuilder();
		List<Map> maps = null;
		try{
			build.append("SELECT b.consttrans as text,b.bz as id FROM bdck.bdcs_const b where b.constslsid = ");
			build.append(" (SELECT a.constslsid FROM  bdck.bdcs_constcls a where a.constclstype = 'XZQH')");
			maps = baseCommonDao.getDataListByFullSql(build.toString());
		}
		catch(Exception e){
			maps = null;
		}
		List<Map> dept = maps;
    	List<Tree> eTrees = new ArrayList<Tree>();
    	Tree dTree = new Tree();
    	
    	String departments = "鹰潭市";
    	dTree.setId("0");
    	dTree.setText(departments);
    	dTree.setChecked(false);
    	if(dept != null){
			for (Map map : dept) {
					Tree uTree = new Tree();
					Object key = map.get("ID") != null ? map.get("ID"):"";
					Object value = map.get("TEXT") != null ? map.get("TEXT"):"";
					uTree.setId(key.toString());
					uTree.setText(value.toString());
					uTree.setChecked(false); 
					if (dTree.children == null) {
						dTree.children = new ArrayList<Tree>();
					}
					dTree.children.add(uTree);
				}
    	}
			eTrees.add(dTree);
		return eTrees;
	}
   /**
	* 读取所有登记类型
	*/
@SuppressWarnings({ "rawtypes" })
	@Override
	public List<Tree> GetDjlxInfo() {
		StringBuilder build = new StringBuilder();
		List<Map> maps = null;
		try{
			build.append("SELECT b.consttrans as text,b.constvalue as id FROM bdck.bdcs_const b where b.constslsid = ");
			build.append(" (SELECT a.constslsid FROM  bdck.bdcs_constcls a where a.constclstype = 'DJLX')");
			maps = baseCommonDao.getDataListByFullSql(build.toString());
		}
		catch(Exception e){
			maps = null;
		}
		List<Map> dept = maps;
    	List<Tree> eTrees = new ArrayList<Tree>();
    	Tree dTree = new Tree();
    	
    	String departments = "全部";
    	dTree.setId("10000");
    	dTree.setText(departments);
    	dTree.setChecked(false);
    	if(dept != null){
			for (Map map : dept) {
					Tree uTree = new Tree();
					Object key = map.get("ID") != null ? map.get("ID"):"";
					Object value = map.get("TEXT") != null ? map.get("TEXT"):"";
					uTree.setId(key.toString());
					uTree.setText(value.toString());
					uTree.setChecked(false); 
					if (dTree.children == null) {
						dTree.children = new ArrayList<Tree>();
					}
					dTree.children.add(uTree);
				}
    	}
			eTrees.add(dTree);
		return eTrees;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Message GetHotTJ(String slsjq, String slsjz, String statistics_type) {
		Message m = new Message();
		try{
			StringBuilder build = new StringBuilder();
			build.append("SELECT "+statistics_type+" AS DM,COUNT(*) AS GS FROM BDCK.V_HOT WHERE 1>0 ");
			if(!StringHelper.isEmpty(slsjq)&&!StringHelper.isEmpty(slsjz)){
				build.append(" AND TO_CHAR(SLSJ,'yyyy-mm-dd') between '"+slsjq+"' AND '"+slsjz+"'");
			}
			build.append(" GROUP BY "+statistics_type+" ORDER BY GS DESC");
			List<Map> maps = baseCommonDao.getDataListByFullSql(build.toString());
			
			String xzqtable="BDCK_DJZQ";
			if("DJQDM".equals(statistics_type)){
				xzqtable="BDCK_DJQ";
			}
			String strsql="SELECT XZQDM,XZQMC FROM BDCK."+xzqtable;
			List<Map> list_xzq = baseCommonDao.getDataListByFullSql(strsql);
			HashMap<String,String> map_xzq=new HashMap<String, String>();
			if(list_xzq!=null&&list_xzq.size()>0){
				for(Map xzq:list_xzq){
					String dm=StringHelper.formatObject(xzq.get("XZQDM"));
					String mc=StringHelper.formatObject(xzq.get("XZQMC"));
					if(!StringHelper.isEmpty(dm)&&!map_xzq.containsKey(dm)){
						map_xzq.put(dm, mc);
					}
				}
			}
			List<Map> maps_new=new ArrayList<Map>();
			if(maps!=null&&maps.size()>0){
				for(Map m_tj:maps){
					String dm=StringHelper.formatObject(m_tj.get("DM"));
					String mc=dm;
					if(map_xzq.containsKey(dm)){
						mc=map_xzq.get(dm);
					}
					m_tj.put("MC", mc);
					maps_new.add(m_tj);
				}
			}
			m.setRows(maps_new);
			m.setTotal(maps_new.size());
		}catch(Exception e){
			m=null;
		}
		return m;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Message GetDJFZTJ(String tjsjks, String tjsjjz, String statistics_type) {
		StringBuilder build = new StringBuilder();
		Message m = new Message();
		try{
			StringBuilder builder_djsj = new StringBuilder();//登记时间
			StringBuilder builder_szsj = new StringBuilder();//缮证时间
			StringBuilder builder_ljdjsj = new StringBuilder();//登记时间
			StringBuilder builder_ljszsj = new StringBuilder();//缮证时间
			//statistics_type：1是按月份统计，2是按年份统计
			if(statistics_type.equals("1")){//按月份统计
				builder_djsj.append(" AND to_char(ql.djsj,'yyyy-mm') = '"+tjsjjz+"' ");
				builder_szsj.append(" AND to_char(zs.szsj,'yyyy-mm') = '"+tjsjjz+"' ");
				builder_ljdjsj.append(" AND to_char(ql.djsj,'yyyy-mm') between '"+tjsjks+"' AND '"+tjsjjz+"' ");
				builder_ljszsj.append(" AND to_char(zs.szsj,'yyyy-mm') between '"+tjsjks+"' AND '"+tjsjjz+"' ");
			}else if(statistics_type.equals("2")){//
				builder_djsj.append(" AND to_char(ql.djsj,'yyyy') = '"+tjsjjz+"' ");
				builder_szsj.append(" AND to_char(zs.szsj,'yyyy') = '"+tjsjjz+"' ");
				builder_ljdjsj.append(" AND to_char(ql.djsj,'yyyy') between '"+tjsjks+"' AND '"+tjsjjz+"' ");
				builder_ljszsj.append(" AND to_char(zs.szsj,'yyyy') between '"+tjsjks+"' AND '"+tjsjjz+"' ");
			}else{
				builder_djsj.append(" AND to_char(ql.djsj,'yyyy-mm') = '"+tjsjjz+"' ");
				builder_szsj.append(" AND to_char(zs.szsj,'yyyy-mm') = '"+tjsjjz+"' ");
				builder_ljdjsj.append(" AND to_char(ql.djsj,'yyyy-mm') between '"+tjsjks+"' AND '"+tjsjjz+"' ");
				builder_ljszsj.append(" AND to_char(zs.szsj,'yyyy-mm') between '"+tjsjks+"' AND '"+tjsjjz+"' ");
			}
			build.append("SELECT SUM(zsljdbs) AS zsljdbs,SUM(zmljdbs) AS zmljdbs,SUM(zsdqdbs) AS zsdqdbs,SUM(zmdqdbs) AS zmdqdbs,zsdbr,zmdbr,")
			.append(" SUM(zsdqszl) AS zsdqszl,SUM(zmdqszl) AS zmdqszl,SUM(zsljszl) AS zsljszl,SUM(zmljszl) AS zmljszl,zsszr,zmszr FROM (")
			.append(" SELECT SUM(zsljdbs) AS zsljdbs,SUM(zmljdbs) AS zmljdbs,SUM(zsdqdbs) AS zsdqdbs,SUM(zmdqdbs) AS zmdqdbs,zsdbr,zmdbr,")
				.append(" 0 AS zsdqszl,0 AS zmdqszl,0 AS zsljszl,0 AS zmljszl,'' AS zsszr,'' AS zmszr")
				.append(" FROM(")
					.append(" SELECT COUNT(ljy.bdcqzh) AS zsljdbs,0 AS zmljdbs,0 AS zsdqdbs,0 AS zmdqdbs ,to_char(dbr) AS zsdbr,'' AS zmdbr FROM (")
						.append(" SELECT DISTINCT ql.dbr,zs.bdcqzh FROM bdck.bdcs_xmxx xmxx ")
						.append(" LEFT JOIN  bdck.bdcs_qdzr_gz qdzr ON xmxx.xmbh=qdzr.xmbh")
						.append(" LEFT JOIN bdck.bdcs_zs_gz zs ON qdzr.zsid=zs.zsid ")
						.append(" LEFT JOIN bdck.bdcs_ql_gz ql ON qdzr.qlid=ql.qlid ")
						.append(" WHERE  xmxx.sfdb=1 AND zs.bdcqzh LIKE '%不动产权第%'")
						.append(builder_ljdjsj)
					.append(" ) ljy GROUP BY ljy.dbr")
					.append(" UNION")
					.append(" SELECT 0 AS zsljdbs,COUNT(ljy.bdcqzh) AS zmljdbs,0 AS zsdqdbs,0 AS zmdqdbs ,'' AS zsdbr,to_char(dbr) AS zmdbr FROM (")
						.append(" SELECT DISTINCT ql.dbr,zs.bdcqzh FROM bdck.bdcs_xmxx xmxx ")
						.append(" LEFT JOIN  bdck.bdcs_qdzr_gz qdzr ON xmxx.xmbh=qdzr.xmbh")
						.append(" LEFT JOIN bdck.bdcs_zs_gz zs ON qdzr.zsid=zs.zsid ")
						.append(" LEFT JOIN bdck.bdcs_ql_gz ql ON qdzr.qlid=ql.qlid ")
						.append(" WHERE  xmxx.sfdb=1 AND zs.bdcqzh LIKE '%不动产证明%' ")
						.append(builder_ljdjsj)
					.append(" ) ljy GROUP BY ljy.dbr")
					.append(" UNION")
					.append(" SELECT 0 AS zsljdbs,0 AS zmljdbs,COUNT(dqy.bdcqzh) AS zsdqdbs,0 AS zmdqdbs,to_char(dbr) AS zsdbr,'' AS zmdbr FROM (")
						.append(" SELECT DISTINCT ql.dbr,zs.bdcqzh FROM bdck.bdcs_xmxx xmxx  ")
						.append(" LEFT JOIN  bdck.bdcs_qdzr_gz qdzr ON xmxx.xmbh=qdzr.xmbh")
						.append(" LEFT JOIN bdck.bdcs_zs_gz zs ON qdzr.zsid=zs.zsid ")
						.append(" LEFT JOIN bdck.bdcs_ql_gz ql ON qdzr.qlid=ql.qlid ")
						.append(" WHERE  xmxx.sfdb=1 AND zs.bdcqzh LIKE '%不动产权第%' ")
						.append(builder_djsj)
					.append(" ) dqy GROUP BY dqy.dbr")
					.append(" UNION")
					.append(" SELECT 0 AS zsljdbs,0 AS zmljdbs,0 AS zsdqdbs,COUNT(dqy.bdcqzh) AS zmdqdbs,'' AS zsdbr,to_char(dbr) AS zmdbr FROM (")
						.append(" SELECT DISTINCT ql.dbr,zs.bdcqzh FROM bdck.bdcs_xmxx xmxx  ")
						.append(" LEFT JOIN  bdck.bdcs_qdzr_gz qdzr ON xmxx.xmbh=qdzr.xmbh ")
						.append(" LEFT JOIN bdck.bdcs_zs_gz zs ON qdzr.zsid=zs.zsid ")
						.append(" LEFT JOIN bdck.bdcs_ql_gz ql ON qdzr.qlid=ql.qlid ")
						.append(" WHERE  xmxx.sfdb=1 AND zs.bdcqzh LIKE '%不动产证明%' ")
						.append(builder_djsj)
					.append(" ) dqy GROUP BY dqy.dbr ")
				.append(" ) zsdbl GROUP BY zsdbr,zmdbr ")
				.append(" UNION ALL ")
				.append(" SELECT 0 AS zsljdbs,0 AS zmljdbs,0 AS zsdqdbs,0 AS zmdqdbs,'' AS zsdbr,'' AS zmdbr,")
				.append(" SUM(zsdqszl) AS zsdqszl,SUM(zmdqszl) AS zmdqszl,SUM(zsljszl) AS zsljszl,SUM(zmljszl) AS zmljszl,zsszr,zmszr FROM( ")
					.append(" SELECT COUNT(bdcqzh) AS zsdqszl,0 AS zmdqszl,0 AS zsljszl,0 AS zmljszl,to_char(szr) AS zsszr,'' AS zmszr FROM ( ")
						.append(" SELECT DISTINCT zs.bdcqzh,szr.staff_name AS szr FROM bdck.bdcs_xmxx xmxx  ")
						.append(" LEFT JOIN bdck.bdcs_zs_gz zs ON xmxx.xmbh=zs.xmbh ")
						.append(" LEFT JOIN  ")
						.append(" ( ")
							.append(" SELECT DISTINCT pro.file_number,act.staff_name FROM bdc_workflow.wfi_proinst pro ") 
							.append(" LEFT JOIN bdc_workflow.wfi_actinst act ON pro.proinst_id=act.proinst_id  ")
							.append(" WHERE act.actinst_name='缮证' AND act.staff_name IS NOT NULL ")
						.append(" ) szr ON xmxx.project_id=szr.file_number ")
						.append(" WHERE zs.bdcqzh LIKE '%不动产权第%' AND szr.staff_name IS NOT NULL ")
						.append(builder_szsj)
					.append(" ) dqy GROUP BY dqy.szr  ")
					.append(" UNION  ")
					.append(" SELECT 0  AS zsdqszl,COUNT(bdcqzh) AS zmdqszl,0 AS zsljszl,0 AS zmljszl,'' AS zsszr,to_char(szr) AS zmszr FROM ( ")
						.append(" SELECT DISTINCT zs.bdcqzh,szr.staff_name AS szr FROM bdck.bdcs_xmxx xmxx  ")
						.append(" LEFT JOIN bdck.bdcs_zs_gz zs ON xmxx.xmbh=zs.xmbh ")
						.append(" LEFT JOIN  ")
						.append(" ( ")
							.append(" SELECT DISTINCT pro.file_number,act.staff_name FROM bdc_workflow.wfi_proinst pro ") 
							.append(" LEFT JOIN bdc_workflow.wfi_actinst act ON pro.proinst_id=act.proinst_id  ")
							.append(" WHERE act.actinst_name='缮证' AND act.staff_name IS NOT NULL ")
						.append(" ) szr ON xmxx.project_id=szr.file_number ")
						.append(" WHERE  xmxx.sfdb=1 AND zs.bdcqzh LIKE '%不动产证明%'  AND szr.staff_name IS NOT NULL ")
						.append(builder_szsj)
					.append(" ) dqy GROUP BY dqy.szr  ")
					.append(" UNION  ")
					.append(" SELECT 0  AS zsdqszl,0  AS zmdqszl,COUNT(bdcqzh) zsljszl,0 AS zmljszl,to_char(szr) AS zsszr,'' AS zmszr FROM ( ")
						.append(" SELECT DISTINCT zs.bdcqzh,szr.staff_name AS szr FROM bdck.bdcs_xmxx xmxx  ")
						.append(" LEFT JOIN bdck.bdcs_zs_gz zs ON xmxx.xmbh=zs.xmbh ")
						.append(" LEFT JOIN  ")
						.append(" ( ")
							.append(" SELECT DISTINCT pro.file_number,act.staff_name FROM bdc_workflow.wfi_proinst pro ") 
							.append(" LEFT JOIN bdc_workflow.wfi_actinst act ON pro.proinst_id=act.proinst_id  ")
							.append(" WHERE act.actinst_name='缮证' AND act.staff_name IS NOT NULL ")
						.append(" ) szr ON xmxx.project_id=szr.file_number ")
						.append(" WHERE  xmxx.sfdb=1 AND zs.bdcqzh LIKE '%不动产权第%'  AND szr.staff_name IS NOT NULL ")
						.append(builder_ljszsj)
					.append(" ) ljy GROUP BY ljy.szr  ")
					.append(" UNION  ")
					.append(" SELECT 0  AS zsdqszl,0  AS zmdqszl,0 AS zsljszl,COUNT(bdcqzh) AS zmljszl,'' AS zsszr,to_char(szr) AS zmszr  FROM ( ")
						.append(" SELECT DISTINCT zs.bdcqzh,szr.staff_name AS szr FROM bdck.bdcs_xmxx xmxx  ")
						.append(" LEFT JOIN bdck.bdcs_zs_gz zs ON xmxx.xmbh=zs.xmbh ")
						.append(" LEFT JOIN  ")
						.append(" ( ")
							.append(" SELECT DISTINCT pro.file_number,act.staff_name FROM bdc_workflow.wfi_proinst pro ") 
							.append(" LEFT JOIN bdc_workflow.wfi_actinst act ON pro.proinst_id=act.proinst_id  ")
							.append(" WHERE act.actinst_name='缮证' AND act.staff_name IS NOT NULL ")
						.append(" ) szr ON xmxx.project_id=szr.file_number ")
						.append(" WHERE  xmxx.sfdb=1 AND zs.bdcqzh LIKE '%不动产证明%' AND szr.staff_name IS NOT NULL ")
						.append(builder_ljszsj)
					.append(" ) ljy GROUP BY ljy.szr ")
				.append(" ) szl GROUP BY zsszr,zmszr ")
			.append(" ) zsl GROUP BY zsdbr,zmdbr,zsszr,zmszr ");

			List<Map> maps = baseCommonDao.getDataListByFullSql(build.toString());
			m.setRows(maps);
			m.setTotal(maps.size());
		}catch(Exception e){
			m=null;
		}
		
		return m;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Message GetQZDJ(Map<String,String> conditionParameter,Integer currentpage,Integer pageSize) {
		Map<String, String> newpara = new HashMap<String, String>();
		StringBuilder fromsql = new StringBuilder();
		String xhq="0",xhz="0",qzlx = "0";
		StringBuilder sbsj = new StringBuilder();
		if (!StringUtils.isEmpty(conditionParameter.get("id_sjq"))) {			
			try {
				String sjq = new String(conditionParameter.get("id_sjq")
						.getBytes("iso8859-1"), "utf-8");
				sbsj.append("  AND szsj >=to_date('"+sjq+"','yyyy-mm-dd hh24:mi:ss') ");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!StringUtils.isEmpty(conditionParameter.get("id_sjz"))) {
			
			try {
				String sjz = new String(conditionParameter.get("id_sjz")
						.getBytes("iso8859-1"), "utf-8");
				sbsj.append("  AND szsj <=to_date('"+sjz+"','yyyy-mm-dd hh24:mi:ss') ");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!StringUtils.isEmpty(conditionParameter.get("id_xhq"))){
			try {
				xhq = new String(conditionParameter.get("id_xhq").getBytes("iso8859-1"), "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		if (!StringUtils.isEmpty(conditionParameter.get("id_xhz"))){
			try {
				xhz = new String(conditionParameter.get("id_xhz").getBytes("iso8859-1"), "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}					
		}
		if (!StringUtils.isEmpty(conditionParameter.get("id_tjlx"))){			
			try {
				qzlx = new String(conditionParameter.get("id_tjlx").getBytes("iso8859-1"), "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}				
		}
		String subZmh = "to_number(Substr(LTRIM( rtrim(a.bdcqzh,' '),' '),17,7))",
			   subZsh = "to_number(Substr(LTRIM( rtrim(a.bdcqzh,' '),' '),16,7))";	
		StringBuilder sbzs = new  StringBuilder();
		sbzs.append("select '证书' as qzlx, c.ywlsh,c.xmbh,a.zsbh,a.bdcqzh,a.szsj from  bdck.bdcs_zs_gz a ")
			.append("left join bdck.bdcs_qdzr_gz  b  on a.zsid=b.zsid  ") 
			.append("left join bdck.bdcs_xmxx c on a.xmbh=c.xmbh  ") 
			.append("where a.bdcqzh LIKE '%(____)%不动产权第_______号' and a.szsj is  not  NULL");
		StringBuilder sbzm = new  StringBuilder();
		sbzm.append("select '证明' as qzlx, c.ywlsh,c.xmbh,a.zsbh,a.bdcqzh,a.szsj from  bdck.bdcs_zs_gz a ")
			.append("left join bdck.bdcs_qdzr_gz  b  on a.zsid=b.zsid  ") 
			.append("left join bdck.bdcs_xmxx c on a.xmbh=c.xmbh  ") 
			.append("where a.bdcqzh  LIKE '%(____)%不动产证明第_______号' and a.szsj is  not  NULL");
		if(qzlx.equals("1")){
		//select rownum,abc.*
			fromsql.append(" from ( ").append(sbzs);
			if(!xhq.equals("0") ){
				fromsql.append(" AND " + subZsh + " >=" + xhq);					
			}
			if(!xhz.equals("0") ){
				fromsql.append(" AND " + subZsh + " <=" + xhz);					
			}			
			fromsql.append(" order by BDCQZH) abc where 1=1  ");
			
		}else if(qzlx.equals("2")){
			fromsql.append(" from ( ").append(sbzm);
			if(!xhq.equals("0") ){
				fromsql.append(" AND " + subZmh + " >=" + xhq);					
			}
			if(!xhz.equals("0") ){
				fromsql.append(" AND " + subZmh + " <=" + xhz);					
			}
			fromsql.append(" order by BDCQZH) abc where 1=1 ");
			
		}else{
			fromsql.append(" from ( ").append(sbzs);
			if(!xhq.equals("0") ){
				fromsql.append(" AND " + subZsh + " >=" + xhq);					
			}
			if(!xhz.equals("0") ){
				fromsql.append(" AND " + subZsh + " <=" + xhz);					
			}
			fromsql.append(" union ");
			fromsql.append(sbzm);
			if(!xhq.equals("0") ){
				fromsql.append(" AND " + subZmh + " >=" + xhq);					
			}
			if(!xhz.equals("0") ){
				fromsql.append(" AND " + subZmh + " <=" + xhz);					
			}
			fromsql.append(" order  by  BDCQZH) abc where 1=1 ");			
		}
		fromsql.append(sbsj);
		
		Long total = baseCommonDao.getCountByFullSql(fromsql.toString());
		if (total == 0)
			return new Message();
		StringBuilder selectSql = new StringBuilder();//查询结果展示
		selectSql.append("select rownum,abc.*");
		List<Map> result = baseCommonDao.getPageDataByFullSql(
				selectSql.append(fromsql).toString(),  currentpage,	pageSize);
		//获取qlr和ywr
		if (result != null && result.size() > 0) {
			for (Map map :result) {
				if(!StringUtils.isEmpty(map.get("XMBH"))){
					//权利人
					StringBuilder qlr = new  StringBuilder();
					qlr.append("SELECT QLR.SQRXM AS QLRMC")
					   .append(" FROM (")
					   .append("SELECT WM_CONCAT(TO_CHAR(S.SQRXM)) AS SQRXM,S.XMBH FROM BDCK.BDCS_SQR S WHERE S.SQRLB='1'  AND XMBH IS NOT NULL GROUP  BY XMBH ")
					   .append(") QLR ")
					   .append("WHERE XMBH='" + map.get("XMBH") + "'");
					//义务人
					StringBuilder ywr = new  StringBuilder();
					ywr.append("SELECT YWR.SQRXM AS YWRMC")
					   .append(" FROM (")
					   .append("SELECT WM_CONCAT(TO_CHAR(S.SQRXM)) AS SQRXM,S.XMBH FROM BDCK.BDCS_SQR S WHERE S.SQRLB='2'  AND XMBH IS NOT NULL GROUP  BY XMBH ")
					   .append(") YWR ")
					   .append("WHERE XMBH='" + map.get("XMBH")+ "'");
					List<Map> list_qlr = baseCommonDao.getDataListByFullSql(qlr.toString());
					List<Map> list_ywr = baseCommonDao.getDataListByFullSql(ywr.toString());
					if (list_qlr != null && list_qlr.size() > 0) {
						map.put("QLRMC", list_qlr.get(0).get("QLRMC"));
					}
					if (list_ywr != null && list_ywr.size() > 0) {
						map.put("YWRMC", list_ywr.get(0).get("YWRMC"));
					}
				}			
			}
		}
		Message msg = new Message();
		msg.setRows(result);
		msg.setTotal(total);
		return msg;
	}
		
	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> QZYJDownload(Map<String,String> conditionParameter){
		Map<String, String> newpara = new HashMap<String, String>();
		StringBuilder fromsql = new StringBuilder();
		
		//select rownum,abc.*
		String xhq="0",xhz="0",qzlx = "0";
		StringBuilder sbsj = new StringBuilder();
		if (!StringUtils.isEmpty(conditionParameter.get("id_sjq"))) {			
			try {
				String sjq = new String(conditionParameter.get("id_sjq")
						.getBytes("iso8859-1"), "utf-8");
				sbsj.append("  AND szsj >=to_date('"+sjq+"','yyyy-mm-dd hh24:mi:ss') ");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!StringUtils.isEmpty(conditionParameter.get("id_sjz"))) {
			
			try {
				String sjz = new String(conditionParameter.get("id_sjz")
						.getBytes("iso8859-1"), "utf-8");
				sbsj.append("  AND szsj <=to_date('"+sjz+"','yyyy-mm-dd hh24:mi:ss') ");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!StringUtils.isEmpty(conditionParameter.get("id_xhq"))){
			try {
				xhq = new String(conditionParameter.get("id_xhq").getBytes("iso8859-1"), "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		if (!StringUtils.isEmpty(conditionParameter.get("id_xhz"))){
			try {
				xhz = new String(conditionParameter.get("id_xhz").getBytes("iso8859-1"), "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}					
		}
		if (!StringUtils.isEmpty(conditionParameter.get("id_tjlx"))){			
			try {
				qzlx = new String(conditionParameter.get("id_tjlx").getBytes("iso8859-1"), "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}				
		}
		String subZmh = "to_number(Substr(LTRIM( rtrim(a.bdcqzh,' '),' '),17,7))",
			   subZsh = "to_number(Substr(LTRIM( rtrim(a.bdcqzh,' '),' '),16,7))";	
		StringBuilder sbzs = new  StringBuilder();
		sbzs.append("select '证书' as qzlx, c.ywlsh,a.zsbh,a.bdcqzh,a.szsj,qlr.sqrxm AS qlrmc,ywr.sqrxm AS ywrmc  from  bdck.bdcs_zs_gz a ")
			.append("left join bdck.bdcs_qdzr_gz  b  on a.zsid=b.zsid  ") 
			.append("left join bdck.bdcs_xmxx c on a.xmbh=c.xmbh  ") 
			.append("LEFT JOIN ( ")
			.append("SELECT WM_CONCAT(to_char(s.sqrxm)) AS sqrxm,s.xmbh FROM bdck.bdcs_sqr s WHERE s.sqrlb='1'  AND xmbh IS NOT NULL GROUP  BY xmbh ")
			.append(") qlr ON c.xmbh=qlr.xmbh ")
			.append("LEFT JOIN ( ")
			.append("SELECT WM_CONCAT(TO_CHAR(s.sqrxm)) AS sqrxm,s.xmbh FROM bdck.bdcs_sqr s WHERE s.sqrlb='2' AND xmbh IS NOT NULL GROUP  BY xmbh ")
			.append(") ywr ON c.xmbh=ywr.xmbh  ")
			.append("where a.bdcqzh  like '%不动产权%' and a.szsj is  not  NULL ");
		StringBuilder sbzm = new  StringBuilder();
		sbzm.append("select '证明' as qzlx, c.ywlsh,a.zsbh,a.bdcqzh,a.szsj,qlr.sqrxm AS qlrmc,ywr.sqrxm AS ywrmc  from  bdck.bdcs_zs_gz a ")
			.append("left join bdck.bdcs_qdzr_gz  b  on a.zsid=b.zsid  ") 
			.append("left join bdck.bdcs_xmxx c on a.xmbh=c.xmbh  ") 
			.append("LEFT JOIN ( ")
			.append("SELECT WM_CONCAT(to_char(s.sqrxm)) AS sqrxm,s.xmbh FROM bdck.bdcs_sqr s WHERE s.sqrlb='1'  AND xmbh IS NOT NULL GROUP  BY xmbh ")
			.append(") qlr ON c.xmbh=qlr.xmbh ")
			.append("LEFT JOIN ( ")
			.append("SELECT WM_CONCAT(TO_CHAR(s.sqrxm)) AS sqrxm,s.xmbh FROM bdck.bdcs_sqr s WHERE s.sqrlb='2' AND xmbh IS NOT NULL GROUP  BY xmbh ")
			.append(") ywr ON c.xmbh=ywr.xmbh  ")
			.append("where a.bdcqzh  like '%不动产证明%' and a.szsj is  not  NULL ");
		if(qzlx.equals("1")){
			//select rownum,abc.*
				fromsql.append(" from ( ").append(sbzs).append(" )");
				if(!xhq.equals("0") ){
					fromsql.append(" AND " + subZsh + " >=" + xhq);					
				}
				if(!xhz.equals("0") ){
					fromsql.append(" AND " + subZsh + " <=" + xhz);					
				}			
				fromsql.append(" order by BDCQZH) abc where 1=1");
				
			}else if(qzlx.equals("2")){
				fromsql.append(" from ( ").append(sbzm).append(" )");
				if(!xhq.equals("0") ){
					fromsql.append(" AND " + subZmh + " >=" + xhq);					
				}
				if(!xhz.equals("0") ){
					fromsql.append(" AND " + subZmh + " <=" + xhz);					
				}
				fromsql.append(" order  by  BDCQZH) abc where 1=1");
				
			}else{
				fromsql.append(" from ( ").append(sbzs);
				if(!xhq.equals("0") ){
					fromsql.append(" AND " + subZsh + " >=" + xhq);					
				}
				if(!xhz.equals("0") ){
					fromsql.append(" AND " + subZsh + " <=" + xhz);					
				}
				fromsql.append(" union ");
				fromsql.append(sbzm);
				if(!xhq.equals("0") ){
					fromsql.append(" AND " + subZmh + " >=" + xhq);					
				}
				if(!xhz.equals("0") ){
					fromsql.append(" AND " + subZmh + " <=" + xhz);					
				}
				fromsql.append(" order  by  BDCQZH)"
					+ " abc where 1=1 ");			
		}
		fromsql.append(sbsj);
		
		StringBuilder selectSql = new StringBuilder();//查询结果展示
		
		selectSql.append("select rownum,abc.*");
		List<Map> result = baseCommonDao.getDataListByFullSql(selectSql.append(fromsql).toString() );			
		return result;			
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Message GetBdcdjZB(String sjq, String sjz) {
		Message m = new Message();
		try{
			StringBuilder buildslsj = new StringBuilder();
			StringBuilder buildzcsj = new StringBuilder();
			StringBuilder buildzcsj1 = new StringBuilder();
			StringBuilder buildfzsj = new StringBuilder();
			StringBuilder build = new StringBuilder();
			String  qhmc=ConfigHelper.getNameByValue("XZQHMC");

			if(!StringHelper.isEmpty(sjq)&&!StringHelper.isEmpty(sjz)){
				buildslsj.append(" AND TO_CHAR(a.SLSJ,'yyyy-mm-dd') between '"+sjq+"' AND '"+sjz+"'");
				buildzcsj.append(" AND TO_CHAR(act.actinst_end,'yyyy-mm-dd') between '"+sjq+"' AND '"+sjz+"'");
				buildzcsj1.append(" AND TO_CHAR(a.actinst_end,'yyyy-mm-dd') between '"+sjq+"' AND '"+sjz+"'");
				buildfzsj.append(" AND TO_CHAR(djfz.fzsj,'yyyy-mm-dd') between '"+sjq+"' AND '"+sjz+"'");
			}
			build.append("select sum(sll) as sll, sum(dysll) as dysll,sum(bjl) as bjl,sum(dybjl) as dybjl,sum(zsfzl) as zsfzl,sum(zmfzl) as zmfzl,qhmc ,sum(ljzsfzl) as ljzsfzl ,sum(ljzmfzl) as ljzmfzl,sum(ljsll) as ljsll,sum(ljdysll) as ljdysll,sum(ljbjl) as ljbjl,sum(ljdybjl) as ljdybjl ")
				.append( "  ,round(sum(scts),3) as scts,round(sum(zybgts),3) as zybgts,round(sum(dyts),3) as dyts,round(sum(qtts),3) as qtts from ( ")
				.append("select count(*) as sll,0 as dysll,0 as bjl,0 as dybjl,0 as zsfzl,0 as zmfzl,'"+qhmc+"'as qhmc ,0 as ljzsfzl ,0 as ljzmfzl,0 as ljsll,0 as ljdysll,0 as ljbjl,0 as ljdybjl  ")
				.append(",0 as scts,0 as zybgts,0 as dyts ,0 as qtts from bdck.bdcs_xmxx a where 1=1 ")
				.append(buildslsj)
				.append("union all ")
				.append("select 0 as sll,count(*) as dysll,0 as bjl,0 as dybjl,0 as zsfzl,0 as zmfzl,'"+qhmc+"'as qhmc ,0 as ljzsfzl ,0 as ljzmfzl,0 as ljsll,0 as ljdysll,0 as ljbjl,0 as ljdybjl ")
				.append(",0 as scts,0 as zybgts,0 as dyts ,0 as qtts from bdck.bdcs_xmxx a where  a.djlx <> '400'and qllx='23' ")
				.append(buildslsj)
				.append("union all ")
				//办结量
				.append("select 0 as sll,0 as dysll,count(*) as bjl,0 as dybjl,0 as zsfzl,0 as zmfzl,'"+qhmc+"'as qhmc ,0 as ljzsfzl ,0 as ljzmfzl,0 as ljsll,0 as ljdysll,0 as ljbjl,0 as ljdybjl ")
				.append(",0 as scts,0 as zybgts,0 as dyts ,0 as qtts from bdck.bdcs_xmxx a ") 
				.append("left join( ")
				.append("select * from ( ")
				.append("select row_number() over(partition BY b.proinst_id, act.actinst_name order by act.actinst_end DESC) rn,b.file_number ")
				.append(" from BDC_WORKFLOW.Wfi_Proinst b LEFT JOIN bdc_workflow.wfi_actinst act ON act.proinst_id=b.proinst_id  where ")
				.append("act.actinst_name ='登簿' and act.actinst_status=3  ")
				.append(buildzcsj)
				.append(") v where rn=1  ")
				.append(")z on z.file_number=a.project_id ")
				.append("where z.file_number is not null ")
				.append("union all ")
				//抵押办结量（周）
				.append("select 0 as sll,0 as dysll,0 as bjl,count(*) as dybjl,0 as zsfzl,0 as zmfzl,'"+qhmc+"'as qhmc ,0 as ljzsfzl ,0 as ljzmfzl,0 as ljsll,0 as ljdysll,0 as ljbjl,0 as ljdybjl ")
				.append(",0 as scts,0 as zybgts,0 as dyts ,0 as qtts from bdck.bdcs_xmxx a ") 
				
				.append("left join( ")
				.append("select * from ( ")
				.append("select row_number() over(partition BY b.proinst_id, act.actinst_name order by act.actinst_end DESC) rn ,b.file_number ")
				.append("from BDC_WORKFLOW.Wfi_Proinst b LEFT JOIN bdc_workflow.wfi_actinst act ON act.proinst_id=b.proinst_id ")
				.append("where 1=1 ")
				.append(buildzcsj)
				.append("and act.actinst_name ='登簿' and act.actinst_status=3  ")
				.append(") v ").append("where rn = 1 ")  
				.append(") z on a.project_id=z.file_number  ")
				.append("where z.file_number is not null and a.djlx <> '400'and qllx='23' ")
				
				.append("union all ")
				.append("select 0 as sll,0 as dysll,0 as bjl,0 as dybjl,0 as zsfzl,count(*) as zmfzl,'"+qhmc+"'as qhmc ,0 as ljzsfzl ,0 as ljzmfzl,0 as ljsll,0 as ljdysll,0 as ljbjl,0 as ljdybjl ")
				.append(",0 as scts,0 as zybgts,0 as dyts ,0 as qtts from bdck.bdcs_xmxx xmxx ") 
				.append("left join bdck.bdcs_djfz djfz on xmxx.project_id=djfz.ywh where  djfz.hfzsh like'%不动产证明%' ")
				.append(buildfzsj)
				.append("union all ")
				.append("select  0 as sll,0 as dysll,0 as bjl,0 as dybjl,count(*) as zsfzl,0 as zmfzl,'"+qhmc+"'as qhmc ,0 as ljzsfzl ,0 as ljzmfzl,0 as ljsll,0 as ljdysll,0 as ljbjl,0 as ljdybjl ")
				.append(",0 as scts,0 as zybgts,0 as dyts ,0 as qtts from bdck.bdcs_xmxx xmxx ") 
				.append("left join bdck.bdcs_djfz djfz on xmxx.project_id=djfz.ywh where  djfz.hfzsh like '%不动产权第%' ")
				.append(buildfzsj)
				.append("union all ")
				.append("select 0 as sll,0 as dysll,0 as bjl,0 as dybjl,0 as zsfzl, 0 as zmfzl,'"+qhmc+"'as qhmc,count(*) as ljzsfzl ,0 as ljzmfzl ,0 as ljsll,0 as ljdysll,0 as ljbjl,0 as ljdybjl ")
				.append(",0 as scts,0 as zybgts,0 as dyts ,0 as qtts from bdck.bdcs_xmxx a ")
			//	.append("left join bdck.bdcs_zs_gz zs on a.xmbh=zs.xmbh ")
				.append("left join (select distinct bdcqzh,xmbh  from bdck.bdcs_zs_xz) zs on a.xmbh=zs.xmbh  ")
				.append("where a.sfdb='1'and zs.bdcqzh like '%不动产权第%' and a.djlx not in('400') ")
				.append("union all ")
				.append("select 0 as sll,0 as dysll,0 as bjl,0 as dybjl,0 as zsfzl, 0 as zmfzl,'"+qhmc+"'as qhmc,0 as ljzsfzl ,count(*) as ljzmfzl,0 as ljsll,0 as ljdysll,0 as ljbjl,0 as ljdybjl ")
				.append(",0 as scts,0 as zybgts,0 as dyts ,0 as qtts from bdck.bdcs_xmxx a ")
			//	.append("left join bdck.bdcs_zs_gz zs on a.xmbh=zs.xmbh ") 
				.append("left join (select distinct bdcqzh,xmbh  from bdck.bdcs_zs_xz) zs on a.xmbh=zs.xmbh  ")
				.append("where a.sfdb='1'and zs.bdcqzh like '%不动产证明%' and a.djlx not in('400') ")
				.append("union all ")
				.append("select 0 as sll,0 as dysll,0 as bjl,0 as dybjl,0 as zsfzl,0 as zmfzl,'"+qhmc+"'as qhmc ,0 as ljzsfzl ,0 as ljzmfzl,count(*) as ljsll,0 as ljdysll,0 as ljbjl,0 as ljdybjl ")
				.append(",0 as scts,0 as zybgts,0 as dyts ,0 as qtts from bdck.bdcs_xmxx ")
				.append("union all ")
				.append("select 0 as sll,0 as dysll,0 as bjl,0 as dybjl,0 as zsfzl,0 as zmfzl,'"+qhmc+"'as qhmc ,0 as ljzsfzl ,0 as ljzmfzl ,0 as ljsll,count(*) as ljdysll,0 as ljbjl,0 as ljdybjl ")
				.append(",0 as scts,0 as zybgts,0 as dyts ,0 as qtts from bdck.bdcs_xmxx a ")
				.append("where a.djlx <> '400'and qllx='23' ")
				.append("union all ")
				//累计办结量
				.append("select 0 as sll,0 as dysll,0 as bjl,0 as dybjl,0 as zsfzl,0 as zmfzl,'"+qhmc+"'as qhmc ,0 as ljzsfzl ,0 as ljzmfzl ,0 as ljsll,0 as ljdysll,count(*) as ljbjl,0 as ljdybjl ")
				.append(",0 as scts,0 as zybgts,0 as dyts ,0 as qtts from bdck.bdcs_xmxx a ")
				.append("left join( ")
				.append("select * from ( ")
				.append("select row_number() over(partition BY b.proinst_id, act.actinst_name order by act.actinst_end DESC) rn,b.file_number ")
				.append("from BDC_WORKFLOW.Wfi_Proinst b LEFT JOIN bdc_workflow.wfi_actinst act ON act.proinst_id=b.proinst_id ")
				 .append("where act.actinst_name ='登簿' and act.actinst_status=3 )v  ")
				 .append("where rn=1 ")
				 .append(" ) z on a.project_id=z.file_number ")
				 .append("where z.file_number is not null ")

				.append("union all ")
				//累计抵押办结量
				.append("select 0 as sll,0 as dysll,0 as bjl,0 as dybjl,0 as zsfzl,0 as zmfzl,'"+qhmc+"'as qhmc ,0 as ljzsfzl ,0 as ljzmfzl ,0 as ljsll,0 as ljdysll,0 as ljbjl,count(*) as ljdybjl ")
				.append(",0 as scts,0 as zybgts,0 as dyts ,0 as qtts from bdck.bdcs_xmxx a ")
				
				.append("left join( ")
				.append("select * from ( ")
				.append("select row_number() over(partition BY b.proinst_id, ac.actinst_name order by ac.actinst_end DESC) rn,b.file_number ")
				.append("from BDC_WORKFLOW.Wfi_Proinst b LEFT JOIN bdc_workflow.wfi_actinst ac ON ac.proinst_id=b.proinst_id ")
				.append("where  '1'='1' and ac.actinst_name ='登簿' and ac.actinst_status=3 ) v ")
				.append("where rn=1 ")
				.append(") z on a.project_id=z.file_number  ")
				.append("where z.file_number is not null and a.djlx <> '400'and qllx='23' ")
				
				.append("union all ")
				.append("select  0 as sll,0 as dysll,0 as bjl,0 as dybjl,0 as zsfzl,0 as zmfzl,'"+qhmc+"'as qhmc , ")
				.append("0 as ljzsfzl ,0 as ljzmfzl,0 as ljsll,0 as ljdysll,0 as ljbjl,0 as ljdybjl,avg(zts) as scts,0 as zybgts,0 as dyts,0 as qtts  from( ")
				.append("SELECT a.proinst_id,substr(sum(TO_NUMBER(a.actinst_end - a.actinst_start)),1,3) as zts ")
				.append("FROM bdc_workflow.wfi_actinst a ") 
				.append("left join bdc_workflow.wfi_proinst p on a.proinst_id=p.proinst_id ")
				.append("left join bdck.bdcs_xmxx xm on p.prolsh=xm.ywlsh ")
				.append("where  xm.djlx='100' ")
				.append("and xm.sfdb='1' and a.actinst_name not in('收费','发证') ") 
				.append(buildzcsj1 )
				.append("GROUP BY a.proinst_id) ") 
				.append("union all ")
				
				.append("select  0 as sll,0 as dysll,0 as bjl,0 as dybjl,0 as zsfzl,0 as zmfzl,'"+qhmc+"'as qhmc , ")
				.append("0 as ljzsfzl ,0 as ljzmfzl,0 as ljsll,0 as ljdysll,0 as ljbjl,0 as ljdybjl,0 as scts,avg(zts) as zybgts,0 as dyts,0 as qtts from( ")
				.append("SELECT a.proinst_id,sum(TO_NUMBER(a.actinst_end - a.actinst_start)) as zts ")
				.append("FROM bdc_workflow.wfi_actinst a ") 
				.append("left join bdc_workflow.wfi_proinst p on a.proinst_id=p.proinst_id ")
				.append("left join bdck.bdcs_xmxx xm on p.prolsh=xm.ywlsh ")
				.append("where  xm.djlx in('200' ,'300') ")
				.append(buildzcsj1)
				.append("and xm.sfdb='1' and a.actinst_name not in('收费','发证') ") 
				.append("GROUP BY a.proinst_id ) ")
				.append("union all ")
				
				.append("select  0 as sll,0 as dysll,0 as bjl,0 as dybjl,0 as zsfzl,0 as zmfzl,'"+qhmc+"'as qhmc , ")
				.append("0 as ljzsfzl ,0 as ljzmfzl,0 as ljsll,0 as ljdysll,0 as ljbjl,0 as ljdybjl,0 as scts,0 as zybgts,avg(zts) as dyts,0 as qtts from( ")
				.append("SELECT a.proinst_id,count (*),sum(TO_NUMBER(a.actinst_end - a.actinst_start)) as zts ")
				.append("FROM bdc_workflow.wfi_actinst a ") 
				.append("left join bdc_workflow.wfi_proinst p on a.proinst_id=p.proinst_id ")
				.append("left join bdck.bdcs_xmxx xm on p.prolsh=xm.ywlsh ")
				.append("left join bdck.bdcs_ql_gz ql on xm.xmbh=ql.xmbh ")
				.append("where  ql.qllx='23'  ")
				.append(buildzcsj1)
				.append("and xm.sfdb='1' and a.actinst_name not in('收费','发证') ") 
				.append("GROUP BY a.proinst_id) ") 
				.append("union all ")
				
				.append("select  0 as sll,0 as dysll,0 as bjl,0 as dybjl,0 as zsfzl,0 as zmfzl,'"+qhmc+"'as qhmc ,0 as ljzsfzl , ")
				.append("0 as ljzmfzl,0 as ljsll,0 as ljdysll,0 as ljbjl,0 as ljdybjl,0 as scts,0 as zybgts,0 as dyts ,avg(zts) as qtts from ( ")
				.append("SELECT a.proinst_id, sum(TO_NUMBER(a.actinst_end - a.actinst_start)) as zts ")
				.append("FROM bdc_workflow.wfi_actinst a ") 
				.append("left join bdc_workflow.wfi_proinst p on a.proinst_id=p.proinst_id ")
				.append("left join bdck.bdcs_xmxx xm on p.prolsh=xm.ywlsh ")
				.append("left join bdck.bdcs_ql_gz ql on xm.xmbh=ql.xmbh ")
				.append("where  xm.djlx not in('100','200','300') and ql.qllx !='23' ")
				.append("and xm.sfdb='1' and a.actinst_name not in('收费','发证')  ")
				.append(buildzcsj1)
				.append("GROUP BY a.proinst_id) ")

				
				.append(")h group by qhmc "); 
				List<Map> maps = baseCommonDao.getDataListByFullSql(build.toString());
						
			m.setRows(maps);
			m.setTotal(maps.size());
		}catch(Exception e){
			m=null;
		}
		return m;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Message Getefficiency(String sjq,String sjz,String user){
		Message m = new Message();
		
			try {
				StringBuilder buildhjsjq = new StringBuilder();
				StringBuilder buildhjsjz=new StringBuilder();
				StringBuilder builduser=new StringBuilder();
				
				StringBuilder build = new StringBuilder();
				if(!StringHelper.isEmpty(sjq)&&!StringHelper.isEmpty(sjz)){
					buildhjsjq.append(" and to_char(v.actinst_start,'yyyy-mm-dd') between '"+sjq+"' AND '"+sjz+"' ");
					buildhjsjz.append(" and to_char(v.actinst_end,'yyyy-mm-dd') between '"+sjq+"' AND '"+sjz+"' ");
										
				}
				if(!user.equals("()")){
				builduser.append(" and  u.id in "+user+" ");
				}
				build.append("SELECT SUM(ybjs) AS ybjs,SUM(zbjs)+sum(ybjs) AS bjl,sum(zbjs) AS zbjs,SUM(cqj) AS cqj,sum(zbcqj) AS zbcqj,(SUM(zbjs)+sum(ybjs)-SUM(cqj)) AS wcqj, username,dd.id  FROM ( ")
				.append("SELECT COUNT(v.actinst_id) AS ybjs,0 AS bjl,0 AS zbjs,0 AS cqj,0 AS zbcqj,u.username,u.id FROM ( ")
				.append("select * from ( ")
				.append(" select row_number() over(partition BY b.proinst_id, a.staff_id order by a.actinst_end DESC) rn,a.* ,b.prodef_name ")
				.append("from BDC_WORKFLOW.Wfi_Proinst b LEFT JOIN bdc_workflow.wfi_actinst a ON a.proinst_id=b.proinst_id) ") 
				.append("where rn = 1 AND actinst_status=3 ")  
				.append(") v ") 
				.append("LEFT JOIN smwb_framework.t_user u ON v.staff_id=u.id ") 
				.append("WHERE u.id IS NOT NULL ") 
				.append(buildhjsjz).append(builduser)
				.append("GROUP BY u.username,u.id ")  
				.append("UNION ")
				.append("SELECT 0 AS ybjs,0 AS bjl, COUNT(v.actinst_id) AS zbjs,0 AS cqj,0 AS zbcqj,u.username,u.id FROM ( ")
				.append("select * from ( ")
				.append(" select row_number() over(partition BY b.proinst_id, a.staff_id order by a.actinst_end DESC) rn,a.* ,b.prodef_name AS d_staff_id ")
				.append("from BDC_WORKFLOW.Wfi_Proinst b LEFT JOIN bdc_workflow.wfi_actinst a ON a.proinst_id=b.proinst_id  ")
				.append(" ) ") 
				.append("where rn = 1  AND actinst_end IS NULL AND actinst_status<>3 ")
				.append(" ) v ") 
				.append("LEFT JOIN smwb_framework.t_user u ON v.staff_id=u.id ") 
				.append("WHERE u.id IS NOT NULL  ")
				.append(buildhjsjq).append(builduser)
				.append("GROUP BY u.username,u.id   ")
				.append("UNION ")
				.append("SELECT 0 AS ybjs,0 AS bjl,0 AS zbjs,COUNT(v.actinst_id) AS cqj,0 AS zbcqj ,u.username,u.id FROM ( ")
				.append("select * from ( ")
				.append("select row_number() over(partition BY b.proinst_id, a.staff_id order by a.actinst_end DESC) rn,a.* ,b.prodef_name ")
				.append("from BDC_WORKFLOW.Wfi_Proinst b LEFT JOIN bdc_workflow.wfi_actinst a ON a.proinst_id=b.proinst_id) ") 
				.append("where rn = 1 AND ((actinst_status=3 AND actinst_end > actinst_willfinish) ") 
				.append("OR (actinst_status<> 3 AND actinst_end IS NULL AND Sysdate> actinst_willfinish)) ") 
				.append(") v ") 
				.append("LEFT JOIN smwb_framework.t_user u ON v.staff_id=u.id ") 
				.append("WHERE u.id IS NOT NULL ") 
				.append(buildhjsjq).append(builduser)
				.append("GROUP BY u.username,u.id ")
				.append("UNION ")
				.append("SELECT 0 AS ybjs,0 AS bjl,0 AS zbjs,0 AS cqj,COUNT(v.actinst_id) AS zbcqj, u.username,u.id FROM ( ")
				.append("select * from ( ")
				.append("select row_number() over(partition BY b.proinst_id, a.staff_id order by a.actinst_end DESC) rn,a.* ,b.prodef_name ")
				.append("from BDC_WORKFLOW.Wfi_Proinst b LEFT JOIN bdc_workflow.wfi_actinst a ON a.proinst_id=b.proinst_id) ") 
				.append("where rn = 1 AND (actinst_status<> 3 AND actinst_end IS NULL AND Sysdate> actinst_willfinish) ")
				.append(") v ") 
				.append("LEFT JOIN smwb_framework.t_user u ON v.staff_id=u.id ") 
				.append("WHERE u.id IS NOT NULL  ") 
				.append(buildhjsjq).append(builduser)
				.append("GROUP BY u.username,u.id ")  
				.append(")dd  GROUP BY username ,ID ");
				
					List<Map> maps = baseCommonDao.getDataListByFullSql(build.toString());
				
				m.setRows(maps);
				m.setTotal(maps.size());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				m=null;
			}
			return m;
	
	}
	
	@Override
	public Message GetFZMJTJ(String sjq, String sjz, String tjmc) {
		Message m = new Message();
		//String mc = null;
		try {
			StringBuilder buliderslsj = new StringBuilder();
			StringBuilder bulid = new StringBuilder();
			StringBuilder bulidtd = new StringBuilder();
			StringBuilder bulidsy = new StringBuilder();
			StringBuilder bulidfw = new StringBuilder();
			StringBuilder buliddy = new StringBuilder();

			if (!StringHelper.isEmpty(sjq) && !StringHelper.isEmpty(sjz)) {
				buliderslsj
						.append(" AND to_char(ql.djsj,'YYYY-MM-DD') between '"
								+ sjq + "' and '" + sjz + "'");
			}

			bulidsy.append("select '所有发证面积' as tjmc ,sum(h.scjzmj) as scjzmj from bdck.bdcs_xmxx xmxx  ")
					.append(" left join  bdck.bdcs_djdy_gz djdy on  xmxx.xmbh = djdy.xmbh  ")
					.append(" left join (    ")
					.append(" SELECT BDCDYID,ZL,BDCDYH,'032' AS BDCDYLX,yfwyt,scjzmj,'1' AS cxjg FROM BDCK.BDCS_H_XZY ") 
					.append(" UNION   ")
					.append(" SELECT BDCDYID,ZL,BDCDYH,'031' AS BDCDYLX,yfwyt,scjzmj,'1' AS cxjg FROM BDCK.BDCS_H_XZ ") 
					.append(" union  ")
					.append(" SELECT BDCDYID,ZL,BDCDYH,'02'  AS BDCDYLX,yt AS yfwyt,zdmj AS scjzmj,'1' AS cxjg FROM BDCK.BDCS_SHYQZD_XZ ") 
					.append(" UNION   ")
					.append(" SELECT BDCDYID,ZL,BDCDYH,'01'  AS BDCDYLX,yt AS yfwyt,zdmj AS scjzmj,'1' AS cxjg FROM BDCK.BDCS_SYQZD_XZ ")
					.append(" ) H ON H.BDCDYID=DJDY.BDCDYID AND H.BDCDYLX=DJDY.BDCDYLX   ")
					.append("  left join bdck.bdcs_ql_gz ql on djdy.djdyid = ql.djdyid and xmxx.xmbh = ql.xmbh where xmxx.djlx <> '400' and xmxx.djlx <> '800' ") 
					.append(buliderslsj);
			bulidfw.append(" select '所有房屋发证面积' as tjmc ,sum(h.scjzmj) as scjzmj from bdck.bdcs_xmxx xmxx  ")
					.append(" left join  bdck.bdcs_djdy_gz djdy on  xmxx.xmbh = djdy.xmbh  ")
					.append(" left join (    ")
					.append(" SELECT BDCDYID,ZL,BDCDYH,'032' AS BDCDYLX,yfwyt,scjzmj,'1' AS cxjg FROM BDCK.BDCS_H_XZY ") 
					.append(" UNION   ")
					.append(" SELECT BDCDYID,ZL,BDCDYH,'031' AS BDCDYLX,yfwyt,scjzmj,'1' AS cxjg FROM BDCK.BDCS_H_XZ ") 
					.append("  ) H ON H.BDCDYID=DJDY.BDCDYID AND H.BDCDYLX=DJDY.BDCDYLX   ")
					.append("  left join bdck.bdcs_ql_gz ql on djdy.djdyid = ql.djdyid and xmxx.xmbh = ql.xmbh where xmxx.djlx <> '400' and xmxx.djlx <> '800' ") 
					.append(buliderslsj);
			bulidtd.append(" select '所有土地发证面积' as tjmc , sum(h.scjzmj) as scjzmj from bdck.bdcs_xmxx xmxx  ")
					.append(" left join  bdck.bdcs_djdy_gz djdy on  xmxx.xmbh = djdy.xmbh  ")
					.append(" left join (    ")
					.append(" SELECT BDCDYID,ZL,BDCDYH,'02'  AS BDCDYLX,yt AS yfwyt,zdmj AS scjzmj,'1' AS cxjg FROM BDCK.BDCS_SHYQZD_XZ ") 
					.append(" UNION   ")
					.append("  SELECT BDCDYID,ZL,BDCDYH,'01'  AS BDCDYLX,yt AS yfwyt,zdmj AS scjzmj,'1' AS cxjg FROM BDCK.BDCS_SYQZD_XZ ")
					.append("   ) H ON H.BDCDYID=DJDY.BDCDYID AND H.BDCDYLX=DJDY.BDCDYLX   ")
					.append("   left join bdck.bdcs_ql_gz ql on djdy.djdyid = ql.djdyid and xmxx.xmbh = ql.xmbh where xmxx.djlx <> '400' and xmxx.djlx <> '800' ") 
					.append(buliderslsj);
			
			if (tjmc.equals("0")) {
				buliddy.append(bulidfw).append("union all").append(bulidtd);
				//mc = "所有发证面积";
			} else if (tjmc.equals("1")) {
				buliddy.append(bulidfw);
				//mc = "所有房屋发证面积";
			} else {
				buliddy.append(bulidtd);
				//mc = "所有土地发证面积";
			}
			
			

			List<Map> maps = baseCommonDao.getDataListByFullSql(buliddy
					.toString());
			m.setRows(maps);
			m.setTotal(maps.size());
		} catch (Exception e) {
			m = null;
		}
		return m;
	}
	
	@Override
	public Message getKSCXTJ(String tjsjks, String tjsjjs) {
		StringBuilder str = new StringBuilder();
		Message msg = new Message();
		str.append("select sum(gs) as gs,cx_type from (")
		.append("select count(distinct cxxm.ywh) as gs,'证件号查询' as cx_type from bdcdck.bdcs_cxxm cxxm left join ")
		.append("bdcdck.bdcs_cxxm_qlrinfo info on cxxm.cxxmid=info.cxxmid where info.infoid is not null and ")
		.append("to_char(slsj,'yyyy-mm-dd') between '")
		.append(tjsjks).append("' and '").append(tjsjjs).append("' union ")
		.append("select count(cxxm.ywh) as gs,'权证号查询' as cx_type from bdcdck.bdcs_cxxm cxxm left join ")
		.append("bdcdck.bdcs_cxxm_qzhinfo info on cxxm.cxxmid=info.cxxmid where infoid is not null and ")
		.append("to_char(slsj,'yyyy-mm-dd') between '")
		.append(tjsjks).append("' and '").append(tjsjjs).append("' union ")
		.append("select count(1) as gs,'公租房查询' as cx_type from bdck.bdcs_gzf gzf").append(" union ")
		.append("select count(1) as gs,'领导人查询' as cx_type from bdck.bdcs_ldrcx ldr) group by cx_type");
		@SuppressWarnings("rawtypes")
		List<Map> map = baseCommonDao.getDataListByFullSql(str.toString());
		msg.setRows(map);
		return msg;
	}	
	
	@SuppressWarnings("rawtypes")
	@Override
	public Message GetLpbTJ(Map<String, String> mapCondition, Integer page, Integer rows) {
		Message m = new Message();
		StringBuilder builder = new StringBuilder();
		Map<String, String> newpara = new HashMap<String, String>();
		if (!StringUtils.isEmpty(mapCondition.get("zl"))) {
			try {
				newpara.put("zl", new String(mapCondition.get("zl")
						.getBytes("iso8859-1"), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (!StringUtils.isEmpty(mapCondition.get("zh"))) {
			try {
				newpara.put("zh", new String(mapCondition.get("zh")
						.getBytes("iso8859-1"), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (!StringUtils.isEmpty(mapCondition.get("bdcdyh"))) {
			try {
				newpara.put("bdcdyh", new String(mapCondition.get("bdcdyh")
						.getBytes("iso8859-1"), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		try {
			builder.append("SELECT distinct aa.zl AS ZL,aa.zrzh AS ZH,aa.fh AS FH,cc.bdcqzh BDCQZH,aa.bdcdyh as BDCDYH ,cc.zsbh ZSBH FROM (")
				   .append("SELECT  c.zl,c.zrzh,c.fh,c.bdcdyh,a.djdyid,d.xmbh FROM bdck.bdcs_xmxx d LEFT JOIN  bdck.bdcs_djdy_gz a ON d.xmbh=a.xmbh LEFT JOIN bdck.bdcs_h_xz c ON a.bdcdyid=c.bdcdyid ")  
				   .append("WHERE  c.bdcdyid IS NOT NULL ");
			if(newpara.get("zl")!=null){
				builder.append("AND c.zl LIKE '%").append(newpara.get("zl")).append("%'");
			}
			builder.append("  and d.qllx=4 ORDER BY c.zrzh,c.fh) aa LEFT JOIN bdck.bdcs_qdzr_gz bb ON aa.djdyid=bb.djdyid  AND bb.xmbh=aa.xmbh left join bdck.bdcs_zs_gz cc on bb.zsid=cc.zsid " ) 
				   .append("WHERE cc.bdcqzh IS NOT NULL  ");
			if(newpara.get("zh")!=null){
				builder.append("and aa.zrzh='").append(newpara.get("zh")).append("'");
			}
			if(newpara.get("bdcdyh")!=null){
				builder.append("  and aa.bdcdyh like '%").append(newpara.get("bdcdyh")).append("%'   ");
			}
			builder.append(" ORDER BY aa.zrzh,aa.fh");
			List<Map> maps = baseCommonDao.getPageDataByFullSql(builder.toString(),page,rows);
			StringBuilder count = new StringBuilder();
			count.append("from (").append(builder).append(")");
			Long total = baseCommonDao.getCountByFullSql(count.toString());
			m.setRows(maps);
			m.setTotal(total);
		}catch(Exception e){
			m = null;
		}
		
		return m;
	}




@SuppressWarnings("rawtypes")
public List<Map> dowenloadLpbTJ(Map<String, String> mapCondition){
		StringBuilder builder = new StringBuilder();
		Map<String, String> newpara = new HashMap<String, String>();
		if (!StringUtils.isEmpty(mapCondition.get("zl"))) {
			try {
				newpara.put("zl", new String(mapCondition.get("zl")
						.getBytes("iso8859-1"), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (!StringUtils.isEmpty(mapCondition.get("zh"))) {
			try {
				newpara.put("zh", new String(mapCondition.get("zh")
						.getBytes("iso8859-1"), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (!StringUtils.isEmpty(mapCondition.get("bdcdyh"))) {
			try {
				newpara.put("bdcdyh", new String(mapCondition.get("bdcdyh")
						.getBytes("iso8859-1"), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		try {
			builder.append("SELECT distinct aa.zl AS ZL,aa.zrzh AS ZH,aa.fh AS FH,cc.bdcqzh BDCQZH,aa.bdcdyh as BDCDYH ,cc.zsbh ZSBH FROM (")
				   .append("SELECT  c.zl,c.zrzh,c.fh,c.bdcdyh,a.djdyid,d.xmbh,d.qllx FROM bdck.bdcs_xmxx d LEFT JOIN  bdck.bdcs_djdy_gz a ON d.xmbh=a.xmbh LEFT JOIN bdck.bdcs_h_xz c ON a.bdcdyid=c.bdcdyid ")  
				   .append("WHERE  c.bdcdyid IS NOT NULL ");
			if(newpara.get("zl")!=null){
				builder.append("AND c.zl LIKE '%").append(newpara.get("zl")).append("%'");
			}
			builder.append("  and d.qllx=4 ORDER BY c.zrzh,c.fh) aa LEFT JOIN bdck.bdcs_qdzr_gz bb ON aa.djdyid=bb.djdyid  AND bb.xmbh=aa.xmbh left join bdck.bdcs_zs_gz cc on bb.zsid=cc.zsid " ) 
				   .append("WHERE cc.bdcqzh IS NOT NULL ");
			if(newpara.get("zh")!=null){
				builder.append("and aa.zrzh='").append(newpara.get("zh")).append("'");
			}
			if(newpara.get("bdcdyh")!=null){
				builder.append("  and aa.bdcdyh like '%").append(newpara.get("bdcdyh")).append("%'   ");
			}
			builder.append(" ORDER BY aa.zrzh,aa.fh");
		}catch(Exception e){
		}
		List<Map> maps = baseCommonDao.getDataListByFullSql(builder.toString());
		return maps;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Message getMesLog(String id_sjq,String id_sjz,String type,String gl,int page,int rows) {
		StringBuilder str = new StringBuilder();
		Message msg = new Message();
		if("0".equals(type)){
			str.append("from (select * ")
			.append(" from bdck.bdcs_msglog where to_char(ywbjsj,'yyyy-mm-dd') between '")
			.append(id_sjq).append("' and '").append(id_sjz).append("' ) where  (djlx='400' and qllx='23') ");
		}
		else if("1".equals(type)){
			str.append("from (select * ")
			.append(" from bdck.bdcs_msglog where to_char(ywbjsj,'yyyy-mm-dd') between '")
			.append(id_sjq).append("' and '").append(id_sjz).append("' ) where djlx<>'400' and substr(project_id,13,9) not in('200046001','200040101') ");
		}
		else if("2".equals(type)){
			str.append("from (select * ")
			.append(" from bdck.bdcs_msglog where to_char(ywbjsj,'yyyy-mm-dd') between '")
			.append(id_sjq).append("' and '").append(id_sjz).append("' ) where djlx<>'400' and substr(project_id,13,9) in('200046001','200040101') ");
		}
		StringBuilder str2 = new StringBuilder();
		str2.append("select id,ywlsh,project_id,xmmc,qllx,djlx,jsdxrmc,jsdh,to_char(dxfssj,'yyyy-mm-dd') as dxfssj, ")
		.append("flag,dxnr,to_char(ywbjsj,'yyyy-mm-dd') as ywbjsj,message,xmbh ");
		StringBuilder str3 = new StringBuilder();
		if("checked".equals(gl)){
			str3.append(" and length(jsdh)=11 ");
		}
		else if(gl==null){
			str3.append("");
		}
		Long total = baseCommonDao.getCountByFullSql(str.append(str3).toString());
		if(total == 0){
			return new Message();
		}
		List<Map> maps = baseCommonDao.getPageDataByFullSql(str2.append(str).append(str3).toString(), page, rows);
		msg.setTotal(total);
		msg.setRows(maps);
		return msg;
		
	}
	@Override
	public Message InsertFzxx(String tjsjks, String tjsjjz){
		StringBuilder build0 = new StringBuilder();
		StringBuilder build1 = new StringBuilder();
		Message m = new Message();
		try {
			StringBuilder buildersj = new StringBuilder();//查询时间
			if(tjsjks != null || !"".equals(tjsjks)){
				buildersj.append(" AND to_char(a.actinst_end,'yyyy-mm-dd') between '"+tjsjks+"' AND '"+tjsjjz+"'");
			}
			build0.append("Insert into bdck.bdcs_msglog  select * from  (")
				  .append("SELECT sqr.sqrid , xm.ywlsh,xm.project_id,xm.xmmc,xm.qllx,xm.djlx,sqr.sqrxm,sqr.lxdh,Sysdate,'0' AS flag,'' AS dxnr,")
				  .append(" pa.actinst_end,'' AS message,xm.xmbh from bdck.bdcs_xmxx xm ")
				  .append("left join bdck.bdcs_sqr sqr on xm.xmbh=sqr.xmbh")
				  .append(" left join (select * from ( select row_number() over(partition by a.proinst_id,a.actinst_name order by a.actinst_end desc) rn,") 
				  .append(" a.*,b.prolsh from bdc_workflow.wfi_proinst b left join bdc_workflow.wfi_actinst a on a.proinst_id =b.proinst_id ")
				  .append(" where a.actinst_name='登簿' ");
			build0.append(buildersj)
				  .append(") where rn=1) pa on pa.prolsh =xm.ywlsh")
				  .append(" where sqr.sqrlb='2'and xm.djlx ='400'and xm.qllx='23' and pa.prolsh is not null ")
				  .append(" AND NOT Exists(SELECT 1 FROM bdck.bdcs_msglog m WHERE m.id=sqr.sqrid))");
			
			int a=baseCommonDao.updateBySql(build0.toString());
			build1.append("Insert INTO bdck.bdcs_msglog SELECT * FROM (")
				  .append("SELECT sqr.sqrid , xm.ywlsh,xm.project_id,xm.xmmc,xm.qllx,xm.djlx,sqr.sqrxm,sqr.lxdh,Sysdate,'0' AS flag,'' AS dxnr,")
				  .append(	  "pa.actinst_end,'' AS message,xm.xmbh from bdck.bdcs_xmxx xm ")
				  .append(		  " left join bdck.bdcs_sqr sqr on xm.xmbh=sqr.xmbh")
				  .append( " left join (")
				  .append(		  "select distinct p.prolsh,a.actinst_end from  bdc_workflow.wfi_proinst p left join bdc_workflow.wfi_actinst a on p.proinst_id=a.proinst_id ")
				  .append( " where a.actinst_name='缮证'").append(buildersj)
				  .append(  ")pa on pa.prolsh =xm.ywlsh")
				  .append(" where sqr.sqrlb='1'and xm.djlx in('100','200','300','700','900') and xm.qllx='23' and pa.prolsh is not null ")
				  .append( " and substr (xm.project_id ,13,3) not in('900','903') ")
				  .append(" AND NOT Exists(SELECT 1 FROM bdck.bdcs_msglog m WHERE m.id=sqr.sqrid))");
			int b=baseCommonDao.updateBySql(build1.toString());
			StringBuilder build2 = new StringBuilder();
			build2.append("Insert INTO bdck.bdcs_msglog SELECT * FROM (")
			  .append("SELECT sqr.sqrid , xm.ywlsh,xm.project_id,xm.xmmc,xm.qllx,xm.djlx,sqr.sqrxm,sqr.lxdh,Sysdate,'0' AS flag,'' AS dxnr,")
			  .append(	  "pa.actinst_end,'' AS message,xm.xmbh from bdck.bdcs_xmxx xm ")
			  .append(		  " left join bdck.bdcs_sqr sqr on xm.xmbh=sqr.xmbh")
			  .append( " left join (")
			  .append(		  "select distinct p.prolsh,a.actinst_end from  bdc_workflow.wfi_proinst p left join bdc_workflow.wfi_actinst a on p.proinst_id=a.proinst_id ")
			  .append( " where a.actinst_name='出图' ").append(buildersj)
			  .append(  ")pa on pa.prolsh =xm.ywlsh")
			  .append(" where sqr.sqrlb='1'and xm.djlx in('100','200','300','900') and xm.qllx<>'23' and xm.qllx<>'99' and pa.prolsh is not null ")
			  .append( " and substr (xm.project_id ,13,3) not in('900','903') ")
			  .append(" AND NOT Exists(SELECT 1 FROM bdck.bdcs_msglog m WHERE m.id=sqr.sqrid))");
		int c=baseCommonDao.updateBySql(build2.toString());
			StringBuilder str = new StringBuilder();
			if(a>0){
				str.append("抵押注销发送成功");
			}
			if(b>0){
				str.append("发证短信发送成功");
			}
			m.setMsg(str.toString());
		}catch(Exception e){
			
		}
		return m;
	}
	public void updateFzxx(String tjsjks, String tjsjjz,String type){
		StringBuilder build0 = new StringBuilder();
		try {
			StringBuilder buildersj = new StringBuilder();//查询时间
			if(tjsjks != null || !"".equals(tjsjks)){
				buildersj.append("  to_char(a.ywbjsj,'yyyy-mm-dd') between '"+tjsjks+"' AND '"+tjsjjz+"' ");
			}
			if("0".equals(type)){
			build0.append("update bdck.bdcs_msglog a set a.flag='1' where  ")
			.append(buildersj).append(" and (djlx='400' and qllx='23') ");
			}
			else if("1".equals(type)){
				build0.append("update bdck.bdcs_msglog a set a.flag='1' where  ")
				.append(buildersj).append(" and djlx<>'400' ").append("and substr(project_id,13,9) not in('200046001','200040101')");
			}
			else if("2".equals(type)){
				build0.append("update bdck.bdcs_msglog a set a.flag='1' where  ")
				.append(buildersj).append(" and djlx<>'400' ").append("and substr(project_id,13,9) in('200046001','200040101')");
			}
			baseCommonDao.updateBySql(build0.toString());
		}catch(Exception e){
		}
	}
	
	public Message certinfodownload(HttpServletRequest request,
			HttpServletResponse response) {
		String qzbh = "";
		if (request.getParameter("qzbh") != null) {
			qzbh = request.getParameter("qzbh");
		}
		
		String qzlx = "";
		if (request.getParameter("qzlx") != null) {
			qzlx = request.getParameter("qzlx");
		}
		String sfzf="";
		if(request.getParameter("sfzf")!=null){
			sfzf = request.getParameter("sfzf");
		}
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append("select * from bdck.bdcs_rkqzb where 1>0 ");
		if (!StringHelper.isEmpty(qzbh)) {
			hqlBuilder.append(" and QZBH LIKE '%").append(qzbh).append("%'");
		}
		if (!StringHelper.isEmpty(qzlx)&&!("2").equals(qzlx)) {
			hqlBuilder.append(" and QZLX='").append(qzlx).append("'");
		}
		if(!StringHelper.isEmpty(sfzf)&&!("2").equals(sfzf)){
			hqlBuilder.append(" and SFZF='").append(sfzf).append("'");
		}
		List<Map> maps = baseCommonDao.getDataListByFullSql(hqlBuilder.toString());
		Message m=new Message();
		m.setSuccess("true");
		m.setRows(maps);
		m.setMsg("成功！");
		return m;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Message getFwjbzt(Map<String, String> mapCondition,int page,int rows) {
		String hth=new String();
		String fwzt=new String();
		if (!StringUtils.isEmpty(mapCondition.get("hth"))&&!StringUtils.isEmpty(mapCondition.get("fwzt"))) {
			try {
				hth= new String(mapCondition.get("hth").getBytes("iso8859-1"), "utf-8");
				fwzt=new String(mapCondition.get("fwzt").getBytes("iso8859-1"), "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		XMLServiceImpl xml=new XMLServiceImpl();
		Message mas=new Message();
		List<com.supermap.realestate.registration.model.xmlmodel.Message> message;
		long a=0;
		try {
			message = xml.readXMLFromXTGX(hth);//获取整个XML信息
			for(int i=0;i<message.size();i++){
				String relationId=message.get(i).getHead().getRealEstateID();//一行查询直到获取到relationId
				List<BDCQLR> qlrs=message.get(i).getData().getBDCQLRS();
				for(BDCQLR qlr:qlrs){
					String qlrmc=qlr.getQLRMC();
					String zjh=qlr.getZJH();
					StringBuilder  strRest=new StringBuilder();
					StringBuilder  strResou=new StringBuilder();
					List<Map> mm=new ArrayList<Map>();
					strRest.append("select bdcdyid,'").append(qlrmc).append("' as qlrmc,'' as zjh,fwbm,zl,yfwyt,scjzmj  ");
					if(relationId!=null){
						if(fwzt!=null&&fwzt.equals("现房")){//现房
								strResou.append(" from bdck.bdcs_h_xz a where a.fwbm='").append(relationId).append("'");
						}else  if(fwzt!=null&&fwzt.equals("期房")){//期房
								strResou.append(" from bdck.bdcs_h_xzy a where a.fwbm='").append(relationId).append("'");
						}
						a=baseCommonDao.getCountByFullSql(strResou.toString());
						mm=baseCommonDao.getDataListByFullSql(strRest.append(strResou).toString());
						for(int j=0;j<mm.size();j++){
							mm.get(j).put("ZJH", zjh);
						}
						mas.setTotal(a);
						mas.setRows(mm);
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mas;
	}
	
	public Message getWeiXins(String id_sjq, String id_sjz, String type,Integer page, Integer rows) {
		StringBuilder str = new StringBuilder();
		StringBuilder str1 = new StringBuilder();
		Message msg = new Message();
		StringBuilder ss=new  StringBuilder();
		if("0".equals(type)){
			str.append("from (select * ")
			   .append(" from bdck.bdcs_cxjd where to_char(cjsj,'yyyy-mm-dd') between '")
			   .append(id_sjq).append("' and '").append(id_sjz).append("'  ")
			   .append(" and   blzt='办理中'  )");
			ss=str;//只查看办件中的办理状态
		}
		else if("1".equals(type)){
			str.append("from (select * ")
			   .append(" from bdck.bdcs_cxjd where to_char(cjsj,'yyyy-mm-dd') between '")
			   .append(id_sjq).append("' and '").append(id_sjz).append("'  ")
			   .append( "  and  blzt='已办结'  )");
		}else if("2".equals(type)){//导出xml信息
			str.append("select id,xmbh,ywlsh,qlrmc,zjh,lxdh,xmmc,fwzt,blzt,to_char(cjsj,'yyyy/mm/dd HH24:mi:ss') as cjsj,to_char(xgsj,'yyyy/mm/dd HH24:mi:ss') as xgsj ")
			   .append(" from bdck.bdcs_cxjd where to_char(xgsj,'yyyy-mm-dd') between '")
			   .append(id_sjq).append("' and '").append(id_sjz).append("'  ");
			List<Map> maps=	baseCommonDao.getDataListByFullSql(str.toString());//已办结
			msg.setRows(maps);
			return msg;
		}
		Long total = baseCommonDao.getCountByFullSql(str.toString());
		if(total == 0){
			return msg;
		}
		StringBuilder str2 = new StringBuilder();
		str2.append("select qlrmc,zjh,lxdh,ywlsh,xmmc,fwzt,blzt ");
		List<Map> maps = baseCommonDao.getPageDataByFullSql(str2.append(str).toString(), page, rows);
		
		msg.setTotal(total);
		msg.setRows(maps);
		return msg;
	}
	//插入所有数据默认为办结中
	@Override
	public Message InsertCxjd(String id_sjq, String id_sjz, String type) {
		StringBuilder str=new StringBuilder();
		Message  mmes=new  Message();
			str.append("Insert into bdck.bdcs_cxjd  select * from  ( ")
				.append(" SELECT distinct sqr.sqrid,xm.xmbh, xm.ywlsh,sqr.sqrxm,sqr.zjh,sqr.lxdh,xm.xmmc,bb.consttrans as fwzt, ");
			str.append("'办理中'  as blzt,");
			str.append("pa.actinst_end as cjsj,pa.actinst_end  as xgsj  ")//将项目创建时间设置为出事的创建时间和修改时间
				.append("from ( select * from ( ")
				.append("select row_number() over(partition BY a.proinst_id, a.actinst_name order by a.actinst_end DESC) rn,a.* ,b.prolsh ")
				.append(" from BDC_WORKFLOW.Wfi_Proinst b ")
				.append("LEFT JOIN bdc_workflow.wfi_actinst a ON a.proinst_id=b.proinst_id ")
				.append("where ");
		    int a=0;
			str.append("a.actinst_name='申请受理'");
			str.append(" AND to_char(a.actinst_end,'yyyy-mm-dd') between '").append(id_sjq).append("' AND '").append(id_sjz).append("' )  where rn = 1 ")
				.append(" ) pa left join bdck.bdcs_xmxx xm on  pa.prolsh =xm.ywlsh left join (select  *  from bdck.bdcs_const  a left join bdck.bdcs_constcls b on a.constslsid=b.constslsid where b.constclstype='QLLX') bb on xm.qllx=bb.constvalue ")
				.append(" left join bdck.bdcs_sqr sqr on xm.xmbh=sqr.xmbh  ");
			StringBuilder str1=new StringBuilder();
			StringBuilder str2=new StringBuilder();
			StringBuilder str3=new StringBuilder(str);
			if(str3!=null){
				str1.append(" where  xm.djlx not in('800')  and  sqr.sqrlb='1' and xm.qllx <> '23' and pa.prolsh is not null  ")
					.append("AND NOT Exists(SELECT 1 FROM bdck.bdcs_cxjd m WHERE m.id=sqr.sqrid ))");
				a=baseCommonDao.updateBySql(str3.append(str1).toString());
			}
			str2.append(" where sqr.sqrlb='2' and  xm.qllx='23' and pa.prolsh is not null  ")
				.append("AND NOT Exists(SELECT 1 FROM bdck.bdcs_cxjd m WHERE m.id=sqr.sqrid ))");
			a=baseCommonDao.updateBySql(str.append(str2).toString());
			updateXmBLZT(id_sjq,id_sjz);//更新办理办理状态
			StringBuilder str9 = new StringBuilder();
			if(a>0){
					str9.append("微信发送成功");
				 	}
			else if(a==0){
					str9.append("数据已存在");
			}
			mmes.setMsg(str9.toString());
			return mmes;
	}
	
	
	public void updateXmBLZT(String id_sjq,String  id_sjz){
		StringBuilder  str1=new StringBuilder();
		str1.append("select distinct ywlsh,'已办结' as blzt,consttrans  as  fwzt,to_char(actinst_end,'yyyy/mm/dd HH24:mi:ss') as xgsj from (select * from ( select row_number() over(partition BY a.proinst_id, a.actinst_name order by a.actinst_end DESC) rn,a.* ,b.prolsh")
			.append(" from BDC_WORKFLOW.Wfi_Proinst b  ") 
			.append("LEFT JOIN bdc_workflow.wfi_actinst a ON a.proinst_id=b.proinst_id ")
			.append("where   ").append("  a.actinst_name='缮证' and a.actinst_end is not null  and to_char(a.actinst_end,'yyyy-mm-dd') between '").append(id_sjq).append("' AND '").append(id_sjz).append(" '  ")
			.append(")  where rn = 1 )  pa   left join bdck.bdcs_xmxx xm   on xm.ywlsh=pa.prolsh ")
			.append("left join (select  *  from bdck.bdcs_const  a left join bdck.bdcs_constcls b on a.constslsid=b.constslsid where b.constclstype='QLLX') bb on xm.qllx=bb.constvalue where xm.djlx in ('100','200','300','500','700','900')     and xm.xmbh  is not  null    and pa.prolsh is not null  and  substr(xm.project_id,13,3)  not in ('900','903') ");
		List<Map>   mas=baseCommonDao.getDataListByFullSql(str1.toString());//新的状态数据
		StringBuilder  str2=new StringBuilder();
		str2.append("select distinct ywlsh,'已办结' as blzt,consttrans  as  fwzt,to_char(actinst_end,'yyyy/mm/dd HH24:mi:ss') as xgsj from (select * from ( select row_number() over(partition BY a.proinst_id, a.actinst_name order by a.actinst_end DESC) rn,a.* ,b.prolsh")
			.append(" from BDC_WORKFLOW.Wfi_Proinst b  ") 
			.append("LEFT JOIN bdc_workflow.wfi_actinst a ON a.proinst_id=b.proinst_id ")
			.append("where   ").append("  a.actinst_name='登簿' and a.actinst_end is not null  and to_char(a.actinst_end,'yyyy-mm-dd') between '").append(id_sjq).append("' AND '").append(id_sjz).append(" '  ")
			.append(")  where rn = 1 )  pa   left join bdck.bdcs_xmxx xm   on xm.ywlsh=pa.prolsh ")
			.append("left join (select  *  from bdck.bdcs_const  a left join bdck.bdcs_constcls b on a.constslsid=b.constslsid where b.constclstype='QLLX') bb on xm.qllx=bb.constvalue where xm.djlx in ('400')     and xm.xmbh  is not  null    and pa.prolsh is not null   ");
		List<Map>   mass=baseCommonDao.getDataListByFullSql(str2.toString());//新的状态数据
		StringBuilder str3 = new StringBuilder();//为业务键的状态
		str3.append("select distinct ywlsh,blzt,fwzt  ").append("from (select * ")
		   	.append(" from bdck.bdcs_cxjd where to_char(cjsj,'yyyy-mm-dd') <= '")
		   	.append(id_sjz).append("'  ")
		   	.append(" and   blzt='办理中'  )");
		List<Map>   ma=baseCommonDao.getDataListByFullSql(str3.toString());//原表中的数据
		if(ma.size()>0&&mas.size()>0){//一般登记类型的
			for( Map m:ma){
				String ywlsh=StringHelper.formatObject(m.get("YWLSH"));
				for(Map mm:mas){
					String ywlsh1=StringHelper.formatObject(mm.get("YWLSH"));
					if(ywlsh!=null&&ywlsh1!=null&&ywlsh.equals(ywlsh1)){//利用业务号来判断同一条数据，在根据办理状态来判断是否系修改
						String blzt=StringHelper.formatObject(m.get("BLZT"));//原表的办理状态
						String blzt1=StringHelper.formatObject(mm.get("BLZT"));//新表业务的办理状态
						String xgsj=StringHelper.formatObject(mm.get("XGSJ"));//修改时间
						if(blzt!=null&&blzt1!=null&&blzt!=blzt1){
							StringBuilder str7=new StringBuilder();//
							StringBuilder str8=new StringBuilder();//
							str8.append(" ");
							str8.append(" , a.xgsj=to_date('").append(xgsj).append("','yyyy/mm/dd HH24:mi:ss')  ");//项目办结修改表中的修改时间字段
							str7.append("update  bdck.bdcs_cxjd    a set a.blzt ='已办结' ").append(str8).append(" where  a.ywlsh='").append(ywlsh).append("'");
							long b=baseCommonDao.updateBySql(str7.toString());
						}
					}
				}
			}
		}
		if(ma.size()>0&&mass.size()>0){//注销和其他登记中的特殊情况没有发证的状况
			for( Map m:ma){
				String ywlsh=StringHelper.formatObject(m.get("YWLSH"));
				for(Map mm:mass){
					String ywlsh1=StringHelper.formatObject(mm.get("YWLSH"));
					if(ywlsh!=null&&ywlsh1!=null&&ywlsh.equals(ywlsh1)){//利用业务号来判断同一条数据，在根据办理状态来判断是否系修改
						String blzt=StringHelper.formatObject(m.get("BLZT"));//原表的办理状态
						String blzt1=StringHelper.formatObject(mm.get("BLZT"));//新表业务的办理状态
						String xgsj=StringHelper.formatObject(mm.get("XGSJ"));
						if(blzt!=null&&blzt1!=null&&blzt!=blzt1){
							StringBuilder str7=new StringBuilder();
							StringBuilder str8=new StringBuilder();
							str8.append(" ");
							str8.append(" , a.xgsj=to_date('").append(xgsj).append("','yyyy/mm/dd HH24:mi:ss')  ");//项目办结修改表中的修改时间字段
							str7.append("update  bdck.bdcs_cxjd    a set a.blzt ='已办结' ").append(str8).append(" where  a.ywlsh='").append(ywlsh).append("'");
							long b=baseCommonDao.updateBySql(str7.toString());
						}
					}
				}
			}
		}
	}

    /**
	 * 不动产档案统计  2016-12-08 luml
	 */
	@Override
	public Message GetBDCDATJ(String tjsjks, String tjsjjz) {
			StringBuilder build = new StringBuilder();
			Message m = new Message();
			try{
				String sql="SELECT distinct GDINFO.QLLX QLLX, GDINFO.DJLX DJLX, NVL(GDINFO.DYGS, 0) AS DYGS,"+
					"NVL(GDINFO.CQGS, 0) AS CQGS, NVL(GDINFO.XZGS, 0) AS XZGS, NVL(GDINFO.YGGS, 0) AS YGGS "+
					"FROM "+ 
					"(SELECT distinct XMXX.QLLX QLLX,XMXX.DJLX DJLX, "+
					"SUM(CASE  WHEN AJXX.AJID =DY.AJID THEN 1   ELSE 0  END) AS DYGS,  "+
					"SUM(CASE  WHEN AJXX.AJID =CQ.AJID THEN 1   ELSE 0  END) AS CQGS,  "+
					"SUM(CASE  WHEN AJXX.AJID =XZ.AJID THEN 1   ELSE 0  END) AS XZGS,  "+
					"SUM(CASE  WHEN AJXX.AJID =YG.AJID THEN 1   ELSE 0  END) AS YGGS, "+
					"COUNT(*) AS GDGS FROM (SELECT  AJID,CDSJ,YWDH "+
					"FROM BDC_DAK.DAS_AJJBXX ) AJXX "+
					" LEFT JOIN (select distinct BDC_DAK.DAS_DY.ajid from BDC_DAK.DAS_DY) DY ON DY.AJID =AJXX.AJID "+
			        " LEFT JOIN (select distinct BDC_DAK.DAS_CQ.ajid from BDC_DAK.DAS_CQ) CQ ON CQ.AJID = AJXX.AJID "+
			        " LEFT JOIN (select distinct BDC_DAK.DAS_XZ.ajid from BDC_DAK.DAS_XZ) XZ ON XZ.AJID = AJXX.AJID " +
			        " LEFT JOIN (select distinct BDC_DAK.DAS_YG.ajid from BDC_DAK.DAS_YG) YG ON YG.AJID = AJXX.AJID "+
			        " LEFT JOIN BDCK.BDCS_XMXX XMXX  ON XMXX.YWLSH = AJXX.YWDH "+
					"WHERE AJXX.CDSJ BETWEEN  "+
					"TO_DATE('"+tjsjks+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss') AND "+
					"TO_DATE('"+tjsjjz+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') "+
					"GROUP BY XMXX.DJLX, XMXX.QLLX) GDINFO       "+
					"ORDER BY DJLX, TO_NUMBER(QLLX) ";
				List<Map> maps = baseCommonDao.getDataListByFullSql(sql);
				if(!StringHelper.isEmpty(maps) && maps.size()>0)
				{
					for(Map map :maps)
					{
						String strdjlx=StringHelper.FormatByDatatype(map.get("DJLX"));
					    map.put("DJLX", ConstHelper.getNameByValue("DJLX", strdjlx));
						String strqllx=StringHelper.FormatByDatatype(map.get("QLLX"));
						if(DJLX.YGDJ.Value.equals(strdjlx) && QLLX.QTQL.Value.equals(strqllx))
						{
							map.put("QLLX", "转移预告");
						}
						else if(DJLX.CFDJ.Value.equals(strdjlx) && QLLX.QTQL.Value.equals(strqllx))
						{
							map.put("QLLX", "查封");
						}
						else if(DJLX.CFDJ.Value.equals(strdjlx) && QLLX.CFZX.Value.equals(strqllx))
						{
							map.put("QLLX", "解封");
						}
						else
						{
						map.put("QLLX", ConstHelper.getNameByValue("QLLX", strqllx));
						}
					}
				}
				m.setRows(maps);
				m.setTotal(maps.size());
			}catch(Exception e){
				m=null;
			}
			return m;
	}


   @Override
	public Message getCFResult(String sjq, String sjz, String conres,int page,int rows) {
		Connection conn = null;
		conn = JH_DBHelper.getConnect_CXGXK();
		Message mes = new Message();
		StringBuilder strcount = new StringBuilder();
		StringBuilder strfy = new StringBuilder();
		StringBuilder strjg = new StringBuilder();
		ResultSet rs = null;
		ResultSet r = null;
		long count = 0;
		if("1".equals(conres)){
			strjg.append(" and kzjg='1')");
		}
		else if("2".equals(conres)){
			strjg.append(" and kzjg='2')");
		}
		else if("0".equals(conres)){
			strjg.append(" and kzjg is null)");
		}
		strcount.append("select count(1) as gs from (select rwlsh,czxz,cxrssjgmc,bcxrxm from sharesearch.queryapprove ")
		.append("where to_char(tjsj,'yyyy-mm-dd') between '").append(sjq).append("' and '").append(sjz).append("'")
		.append(strjg);
		try {
			 rs = JH_DBHelper.excuteQuery(conn, strcount.toString());
			if(rs.next()){
				count = rs.getLong(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
				try {
					if(rs!=null)
						rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		if(count==0){
			return new Message();
		}
		int row = new Integer(rows);
		if (page*rows != rows){
			row = page*rows-1;
		}
		strfy.append("select * from (select rwlsh,czxz,cxrssjgmc,bcxrxm,row_number() over(order by tjsj desc)r ")
		.append("from sharesearch.queryapprove where to_char(tjsj,'yyyy-mm-dd') between '").append(sjq).append("' and '").append(sjz)
		.append("'").append(strjg).append("t where r between ").append((page-1)*rows+1).append(" and ").append(row);
		try {
			r = JH_DBHelper.excuteQuery(conn, strfy.toString());
			List<Map> ls = resultSetToList(r);
			mes.setTotal(count);
			mes.setRows(ls);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(r!=null)
					r.close();
				if(conn!=null)
					conn.close();
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
		return mes;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Map> resultSetToList(ResultSet rs)  {   
        if (rs == null)   
            return Collections.EMPTY_LIST;   
        ResultSetMetaData md = null;
        int columnCount = 0;
        List<Map> list = new ArrayList<Map>(); 
        Map rowData = new HashMap();
		try {
			md = rs.getMetaData();//得到结果集(rs)的结构信息，比如字段数、字段名等
			columnCount = md.getColumnCount();
			while (rs.next()) {   
				rowData = new HashMap(columnCount);   
				for (int i = 1; i <= columnCount; i++) {  
					rowData.put(md.getColumnName(i), rs.getObject(i));   
				}   
				list.add(rowData);//返回此 ResultSet 对象中的列数 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				if(rs!=null)
					rs.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}       
        return list;   
	}
	
	//全局查询统计数据
		@SuppressWarnings("rawtypes")
		@Override
		public Message GetQjCx() {
			Message m = new Message();
			try{
			
				StringBuilder build = new StringBuilder();
				
				build.append("select sum(sll) as sll, sum(dysll) as dysll,sum(bjl) as bjl,sum(dybjl) as dybjl,sum(zsfzl) as zsfzl,sum(zmfzl) as zmfzl,sum(ljzsfzl) as ljzsfzl ,sum(ljzmfzl) as ljzmfzl,sum(ljsll) as ljsll,sum(ljdysll) as ljdysll,sum(ljbjl) as ljbjl,sum(ljdybjl) as ljdybjl ")
					.append( "  ,round(sum(scts),3) as scts,round(sum(zybgts),3) as zybgts,round(sum(dyts),3) as dyts,round(sum(qtts),3) as qtts from ( ")
					.append("select count(*) as sll,0 as dysll,0 as bjl,0 as dybjl,0 as zsfzl,0 as zmfzl,0 as ljzsfzl ,0 as ljzmfzl,0 as ljsll,0 as ljdysll,0 as ljbjl,0 as ljdybjl  ")
					.append(",0 as scts,0 as zybgts,0 as dyts ,0 as qtts from bdck.bdcs_xmxx a where 1=1 ")
				
					.append("union all ")
					.append("select 0 as sll,count(*) as dysll,0 as bjl,0 as dybjl,0 as zsfzl,0 as zmfzl,0 as ljzsfzl ,0 as ljzmfzl,0 as ljsll,0 as ljdysll,0 as ljbjl,0 as ljdybjl ")
					.append(",0 as scts,0 as zybgts,0 as dyts ,0 as qtts from (select distinct xmbh,qllx,djlx  from bdck.bdcs_ql_gz  where xmbh is not null and djlx <> '400'and qllx='23' ) q ")
					.append("left join bdck.bdcs_xmxx a on a.xmbh = q.xmbh  where 1= 1 ")
				
					.append("union all ")
					//办结量
					.append("select 0 as sll,0 as dysll,count(*) as bjl,0 as dybjl,0 as zsfzl,0 as zmfzl ,0 as ljzsfzl ,0 as ljzmfzl,0 as ljsll,0 as ljdysll,0 as ljbjl,0 as ljdybjl ")
					.append(",0 as scts,0 as zybgts,0 as dyts ,0 as qtts from bdck.bdcs_xmxx a ") 
					.append("left join( ")
					.append("select * from ( ")
					.append("select row_number() over(partition BY b.proinst_id, act.actinst_name order by act.actinst_end DESC) rn,b.file_number ")
					.append(" from BDC_WORKFLOW.Wfi_Proinst b LEFT JOIN bdc_workflow.wfi_actinst act ON act.proinst_id=b.proinst_id  where ")
					.append("act.actinst_name ='登簿' and act.actinst_status=3  ")
					
					.append(") v where rn=1  ")
					.append(")z on z.file_number=a.project_id ")
					.append("where z.file_number is not null ")
					.append("union all ")
					//抵押办结量（周）
					.append("select 0 as sll,0 as dysll,0 as bjl,count(*) as dybjl,0 as zsfzl,0 as zmfzl ,0 as ljzsfzl ,0 as ljzmfzl,0 as ljsll,0 as ljdysll,0 as ljbjl,0 as ljdybjl ")
					.append(",0 as scts,0 as zybgts,0 as dyts ,0 as qtts ") 
					.append("from (select distinct xmbh,qllx,djlx  from bdck.bdcs_ql_xz  where xmbh is not null and djlx <> '400'and qllx='23') w ")
					.append(" left join bdck.bdcs_xmxx x  on x.xmbh= w.xmbh ")
					.append(" left join bdc_workflow.wfi_proinst b  on x.project_id= b.file_number ")
					.append(" left join bdc_workflow.wfi_actinst  act  on b.proinst_id=act.proinst_id ")
					.append(" where 1=1 ")
					.append(" and act.actinst_name ='登簿' and act.actinst_status=3  ")
					
					.append("union all ")
					.append("select 0 as sll,0 as dysll,0 as bjl,0 as dybjl,0 as zsfzl,count(*) as zmfzl ,0 as ljzsfzl ,0 as ljzmfzl,0 as ljsll,0 as ljdysll,0 as ljbjl,0 as ljdybjl ")
					.append(",0 as scts,0 as zybgts,0 as dyts ,0 as qtts from bdck.bdcs_xmxx xmxx ") 
					.append("left join bdck.bdcs_djfz djfz on xmxx.project_id=djfz.ywh where  djfz.hfzsh like'%不动产证明%' ")
					
					.append("union all ")
					.append("select  0 as sll,0 as dysll,0 as bjl,0 as dybjl,count(*) as zsfzl,0 as zmfzl ,0 as ljzsfzl ,0 as ljzmfzl,0 as ljsll,0 as ljdysll,0 as ljbjl,0 as ljdybjl ")
					.append(",0 as scts,0 as zybgts,0 as dyts ,0 as qtts from bdck.bdcs_xmxx xmxx ") 
					.append("left join bdck.bdcs_djfz djfz on xmxx.project_id=djfz.ywh where  djfz.hfzsh like '%不动产权第%' ")
			
					.append("union all ")
					.append("select 0 as sll,0 as dysll,0 as bjl,0 as dybjl,0 as zsfzl, 0 as zmfzl,count(*) as ljzsfzl ,0 as ljzmfzl ,0 as ljsll,0 as ljdysll,0 as ljbjl,0 as ljdybjl ")
					.append(",0 as scts,0 as zybgts,0 as dyts ,0 as qtts from bdck.bdcs_xmxx a ")
					.append("left join (select distinct bdcqzh,xmbh  from bdck.bdcs_zs_xz) zs on a.xmbh=zs.xmbh  ")
					.append("where a.sfdb='1'and zs.bdcqzh like '%不动产权第%' and a.djlx not in('400') ")
					.append("union all ")
					.append("select 0 as sll,0 as dysll,0 as bjl,0 as dybjl,0 as zsfzl, 0 as zmfzl,0 as ljzsfzl ,count(*) as ljzmfzl,0 as ljsll,0 as ljdysll,0 as ljbjl,0 as ljdybjl ")
					.append(",0 as scts,0 as zybgts,0 as dyts ,0 as qtts from bdck.bdcs_xmxx a ")
					.append("left join (select distinct bdcqzh,xmbh  from bdck.bdcs_zs_xz) zs on a.xmbh=zs.xmbh ") 
					.append("where a.sfdb='1'and zs.bdcqzh like '%不动产证明%' and a.djlx not in('400') ")
					.append("union all ")
					.append("select 0 as sll,0 as dysll,0 as bjl,0 as dybjl,0 as zsfzl,0 as zmfzl,0 as ljzsfzl ,0 as ljzmfzl,count(*) as ljsll,0 as ljdysll,0 as ljbjl,0 as ljdybjl ")
					.append(",0 as scts,0 as zybgts,0 as dyts ,0 as qtts from bdck.bdcs_xmxx ")
					.append("union all ")
					.append("select 0 as sll,0 as dysll,0 as bjl,0 as dybjl,0 as zsfzl,0 as zmfzl,0 as ljzsfzl ,0 as ljzmfzl ,0 as ljsll,count(*) as ljdysll,0 as ljbjl,0 as ljdybjl ")
					.append(",0 as scts,0 as zybgts,0 as dyts ,0 as qtts  ")
					.append("from (select distinct xmbh,qllx,djlx  from bdck.bdcs_ql_gz  where  xmbh is not null and djlx <> '400'and qllx='23') q ")
					.append("left join bdck.bdcs_xmxx a  on a.xmbh =q.xmbh ")
					.append("union all ")
					//累计办结量
					.append("select 0 as sll,0 as dysll,0 as bjl,0 as dybjl,0 as zsfzl,0 as zmfzl,0 as ljzsfzl ,0 as ljzmfzl ,0 as ljsll,0 as ljdysll,count(*) as ljbjl,0 as ljdybjl ")
					.append(",0 as scts,0 as zybgts,0 as dyts ,0 as qtts from bdck.bdcs_xmxx a ")
					.append("where a.sfdb ='1' and a.xmbh is not null ")
			
					.append("union all ")
					//累计抵押办结量
					.append("select 0 as sll,0 as dysll,0 as bjl,0 as dybjl,0 as zsfzl,0 as zmfzl,0 as ljzsfzl ,0 as ljzmfzl ,0 as ljsll,0 as ljdysll,0 as ljbjl,count(*) as ljdybjl ")
					.append(",0 as scts,0 as zybgts,0 as dyts ,0 as qtts  ")
					.append("from (select distinct xmbh,qllx,djlx  from bdck.bdcs_ql_xz  where xmbh is not null and djlx <> '400'and qllx='23' ) q ")
					.append("left join bdck.bdcs_xmxx a  on a.xmbh = q.xmbh ")
					.append("where  a.sfdb = '1' ")
				
					
					.append("union all ")
					.append("select  0 as sll,0 as dysll,0 as bjl,0 as dybjl,0 as zsfzl,0 as zmfzl, ")
					.append("0 as ljzsfzl ,0 as ljzmfzl,0 as ljsll,0 as ljdysll,0 as ljbjl,0 as ljdybjl,avg(zts) as scts,0 as zybgts,0 as dyts,0 as qtts  from( ")
					.append("SELECT a.proinst_id,substr(sum(TO_NUMBER(a.actinst_end - a.actinst_start)),1,3) as zts ")
					.append("FROM bdc_workflow.wfi_actinst a ") 
					.append("left join bdc_workflow.wfi_proinst p on a.proinst_id=p.proinst_id ")
					.append("left join bdck.bdcs_xmxx xm on p.prolsh=xm.ywlsh ")
					.append("where  xm.djlx='100' ")
					.append("and xm.sfdb='1' and a.actinst_name not in('收费','发证','科员开票','综合科开票','归档','开票','收费发证') ") 
					
					.append("GROUP BY a.proinst_id) ") 
					.append("union all ")
					
					.append("select  0 as sll,0 as dysll,0 as bjl,0 as dybjl,0 as zsfzl,0 as zmfzl, ")
					.append("0 as ljzsfzl ,0 as ljzmfzl,0 as ljsll,0 as ljdysll,0 as ljbjl,0 as ljdybjl,0 as scts,avg(zts) as zybgts,0 as dyts,0 as qtts from( ")
					.append("SELECT a.proinst_id,sum(TO_NUMBER(a.actinst_end - a.actinst_start)) as zts ")
					.append("FROM bdc_workflow.wfi_actinst a ") 
					.append("left join bdc_workflow.wfi_proinst p on a.proinst_id=p.proinst_id ")
					.append("left join bdck.bdcs_xmxx xm on p.prolsh=xm.ywlsh ")
					.append("where  xm.djlx in('200' ,'300') ")
			
					.append("and xm.sfdb='1' and a.actinst_name not in('收费','发证','科员开票','综合科开票','归档','开票','收费发证') ") 
					.append("GROUP BY a.proinst_id ) ")
					.append("union all ")
					
					.append("select  0 as sll,0 as dysll,0 as bjl,0 as dybjl,0 as zsfzl,0 as zmfzl,")
					.append("0 as ljzsfzl ,0 as ljzmfzl,0 as ljsll,0 as ljdysll,0 as ljbjl,0 as ljdybjl,0 as scts,0 as zybgts,avg(zts) as dyts,0 as qtts from( ")
					.append("SELECT a.proinst_id,count (*),sum(TO_NUMBER(a.actinst_end - a.actinst_start)) as zts ")
					.append("FROM bdc_workflow.wfi_actinst a ") 
					.append("left join bdc_workflow.wfi_proinst p on a.proinst_id=p.proinst_id ")
					.append("left join bdck.bdcs_xmxx xm on p.prolsh=xm.ywlsh ")
					.append("left join bdck.bdcs_ql_gz ql on xm.xmbh=ql.xmbh ")
					.append("where  ql.qllx='23'  ")
				
					.append("and xm.sfdb='1' and a.actinst_name not in('收费','发证','科员开票','综合科开票','归档','开票','收费发证') ") 
					.append("GROUP BY a.proinst_id) ") 
					.append("union all ")
					
					.append("select  0 as sll,0 as dysll,0 as bjl,0 as dybjl,0 as zsfzl,0 as zmfzl,0 as ljzsfzl , ")
					.append("0 as ljzmfzl,0 as ljsll,0 as ljdysll,0 as ljbjl,0 as ljdybjl,0 as scts,0 as zybgts,0 as dyts ,avg(zts) as qtts from ( ")
					.append("SELECT a.proinst_id, sum(TO_NUMBER(a.actinst_end - a.actinst_start)) as zts ")
					.append("FROM bdc_workflow.wfi_actinst a ") 
					.append("left join bdc_workflow.wfi_proinst p on a.proinst_id=p.proinst_id ")
					.append("left join bdck.bdcs_xmxx xm on p.prolsh=xm.ywlsh ")
					.append("left join bdck.bdcs_ql_gz ql on xm.xmbh=ql.xmbh ")
					.append("where  xm.djlx not in('100','200','300') and ql.qllx !='23' ")
					.append("and xm.sfdb='1' and a.actinst_name not in('收费','发证','科员开票','综合科开票','归档','开票','收费发证')  ")
				
					.append("GROUP BY a.proinst_id) ")

					
					.append(")h  "); 
					List<Map> maps = baseCommonDao.getDataListByFullSql(build.toString());
							
				m.setRows(maps);
				m.setTotal(maps.size());
			}catch(Exception e){
				m=null;
			}
			return m;
		}
		/**
		 * 业务查询
		 * 罗雨
		 */	
			@SuppressWarnings("rawtypes")
			@Override
			public Message GetYWTJ(String tjsjks, String tjsjjz, String djlxs, String qlrlxs, String xzqh, String fzlb) {
				 String fzlx ="";
				 String fzlx1="";
				 String fzlxyear="";
				 String fzlxyear1="";
				 String lx="";
				 String lx1="";
				 String lx2="";
				 String qh = "#2";
				 String qh1 = "#3";
				 String djlx ="";
				 String qlrlx = ""; 
					 if( djlxs .equals("") || djlxs.equals("null") || djlxs.equals("全部,首次登记,转移登记,变更登记,注销登记,更正登记,异议登记,预告登记,查封登记,其它登记")){
							djlx = "";			
						}else{
							djlx = " AND DJLX in ("+djlxs+")";	
						}
					 if( qlrlxs.equals("") || qlrlxs.equals("null") || qlrlxs.equals("全部,个人,企业,事业单位,国家机关,其它")) {
							qlrlx ="";
					}else {
							qlrlx = " AND qlrlx in ("+qlrlxs+")";
					}         
				if(fzlb.equals("登记类型")){
					fzlx = " DJLX,QLLX";
					fzlx1 = " DJLX,QLLX";
					fzlxyear = " DJLX,QLLX";
					fzlxyear1=fzlxyear;
				}else if(fzlb.equals("权利人类型")){
					fzlx = " QLRLX";
					fzlx1 = " QLRLX as QLRLX";	
					lx2 ="b.qlrlx,";
					fzlxyear = "QLRLX";
					fzlxyear1=fzlxyear;
				}else if(fzlb.equals("年度")){
					fzlx= "year";
					fzlx1 = "to_char(xmxx.slsj,'YYYY')as year";
					lx2 = "to_char(xmxx.slsj,'YYYY') as year,";
					fzlxyear = "to_char(xmxx.slsj,'YYYY')";
					fzlxyear1="year";
				}else if(fzlb.equals("季度")){
					fzlx= "year,jd";
					fzlx1 = "to_char(xmxx.slsj,'YYYY') as year,to_char(xmxx.slsj,'Q') as jd";	
					lx2 = "to_char(xmxx.slsj,'YYYY') as year,to_char(xmxx.slsj,'Q') as jd,";
					fzlxyear = "to_char(xmxx.slsj,'YYYY'),to_char(xmxx.slsj,'Q')";
					fzlxyear1="year,jd";
				}else if(fzlb.equals("月份")){
					fzlx= "year,jd,yd";
					fzlx1 = "to_char(xmxx.slsj,'YYYY')as year,to_char(xmxx.slsj,'Q')as jd,to_char(xmxx.slsj,'MM')as yd";
					lx2 = "to_char(xmxx.slsj,'YYYY') as year,to_char(xmxx.slsj,'Q') as jd,to_char(xmxx.slsj,'MM') as yd,";
					fzlxyear = "to_char(xmxx.slsj,'YYYY'),to_char(xmxx.slsj,'Q'),to_char(xmxx.slsj,'MM')";
					fzlxyear1="year,jd,yd";
				}
			
				lx = "select distinct xmbh ,qlrlx from bdck.bdcs_qlr_xz"+qh;
						
				Message m = new Message();
				StringBuilder build = new StringBuilder();
				build.append("select "+fzlx+",sum(slgs) as slgs, sum(bjgs) as bjgs, MAX(BJL) *100||'%'as BJL,round(sum(bjsj),3) as bjsj,sum(FZZMGS) as FZZMGS,sum(FZZSGS) as FZZSGS,")
				.append("round(sum(bdbe),3) as bdbe,round(sum(zge),3) as zge,round(sum(ysje),3) as ysje,round(sum(ssje),3) as ssje from ( ")
				.append("select "+fzlx1+",count(*) as slgs,0 as bjgs,0 as BJL,0 as bjsj,0 as FZZMGS,0 as FZZSGS,0 as bdbe ,0 as zge,0 as ysje,0 as ssje ")
				.append(" from bdck.bdcs_xmxx "+qh+" xmxx ");
				if( !qlrlxs.equals("")){
					build.append(" LEFT JOIN ("+lx+") b ON xmxx.XMBH =b.xmbh ");
				}
				build.append(" where 1=1 AND TO_CHAR(xmxx.SLSJ,'yyyy-mm-dd') between '"+tjsjks+"' AND '"+tjsjjz+"' ")
				.append( lx1)
				.append( djlx)
				.append( qlrlx)
				.append(" group by "+fzlxyear+" ")
				.append(" union all")
				.append(" select "+fzlx1+",0 as slgs,count(*) as bjgs,0 as BJL,0 as bjsj,0 as FZZMGS,0 as FZZSGS,0 as bdbe ,0 as zge,0 as ysje,0 as ssje ")
				.append(" from bdck.bdcs_xmxx"+qh+" xmxx LEFT JOIN ("+lx+") b ON xmxx.XMBH =b.xmbh where xmxx.sfdb = '1'")
				.append(" AND TO_CHAR(xmxx.SLSJ,'yyyy-mm-dd') between '"+tjsjks+"' AND '"+tjsjjz+"' ")
				.append( lx1)
				.append( djlx)
				.append( qlrlx)
				.append(" group by "+fzlxyear+"")
				.append(" union all")
				.append(" select   "+fzlx+",0 as slgs,0 as bjgs, BJL, 0 as bjsj,0 as FZZMGS,0 as FZZSGS,0 as bdbe ,0 as zge,0 as ysje,0 as ssje from(")
				.append(" select  "+fzlx+",round(BJGS/SLGS,3)as BJL from (SELECT "+fzlx1+" , SUM(CASE WHEN SFDB = '1' THEN 1 ELSE 0 END) AS BJGS,SUM(CASE WHEN xmxx.xmbh is not null THEN 1 ELSE 0 END) AS SLGS ")
				.append(" FROM bdck.bdcs_xmxx"+qh+" xmxx ");
				if( !qlrlxs.equals("") ){
					build.append(" LEFT JOIN ("+lx+") b ON xmxx.XMBH =b.xmbh ");
				}
				build.append(" WHERE TO_CHAR(XMXX.SLSJ,'yyyy-mm-dd') between '"+tjsjks+"' AND '"+tjsjjz+"' ")
				.append( lx1)
				.append( djlx)
				.append( qlrlx)
				.append(" group by "+fzlxyear+"")
				.append(" order by "+fzlxyear1+")   )")
				.append(" union all")
				.append("  select  "+fzlx+",0 as slgs,0 as bjgs,0 as BJL, bjsj,0 as FZZMGS,0 as FZZSGS,0 as bdbe ,0 as zge,0 as ysje,0 as ssje from (")
				.append("  select "+fzlx+" ,sum(BJSJ)/count(xmbh) as bjsj from ( SELECT  "+fzlx1+" ,xmxx.XMBH,a.Actinst_Name , a.actinst_end as dbsj ,")
				.append("  p.proinst_start as jssj ,TO_NUMBER(a.actinst_end-p.proinst_start) as bjsj  FROM bdc_workflow.wfi_actinst"+qh+" a ")
				.append("  left join bdc_workflow.wfi_proinst"+qh+" p on a.proinst_id=p.proinst_id ")
				.append("  left join bdck.bdcs_xmxx"+qh+" xmxx on p.prolsh=xmxx.ywlsh");
				if( !qlrlxs.equals("") ){
					build.append(" LEFT JOIN ("+lx+") b ON xmxx.XMBH =b.xmbh ");
				}
				
				build.append(" where xmxx.sfdb='1'  and a.actinst_name like '%缮证%' and a.actinst_end is not null and a.actinst_start")
				.append(" BETWEEN   TO_DATE('"+tjsjks+"', 'yyyy-mm-dd ') AND  TO_DATE('"+tjsjjz+"', 'yyyy-mm-dd ') ")
				.append(" union all ")
				.append(" SELECT  "+fzlx1+" ,xmxx.XMBH,a.Actinst_Name , a.actinst_end as dbsj , ")
				.append("  p.proinst_start as jssj ,TO_NUMBER(a.actinst_end-p.proinst_start) as bjsj  FROM bdc_workflow.wfi_actinst"+qh+" a ")
				.append("  left join bdc_workflow.wfi_proinst"+qh+" p on a.proinst_id=p.proinst_id ")
				.append("  left join bdck.bdcs_xmxx"+qh+" xmxx on p.prolsh=xmxx.ywlsh");
				if( !qlrlxs.equals("")){
					build.append(" LEFT JOIN ("+lx+") b ON xmxx.XMBH =b.xmbh ");
				}
				
				build.append(" where xmxx.sfdb='1'  and a.actinst_name like '%登簿%' and a.actinst_end is not null and a.actinst_start")
				.append(" BETWEEN   TO_DATE('"+tjsjks+"', 'yyyy-mm-dd ') AND  TO_DATE('"+tjsjjz+"', 'yyyy-mm-dd ') and xmxx.djlx in (400,600,800) ")
				.append( lx1)
				.append( djlx)
				.append( qlrlx)
				.append(" )  group by "+fzlxyear1+" )")
				.append(" union all")
				.append(" select "+fzlx1+",0 as slgs,0 as bjgs,0 as BJL, 0 as bjsj,count(*) as FZZMGS,0 as FZZSGS,0 as bdbe ,0 as zge,0 as ysje,0 as ssje")
				.append(" from bdck.bdcs_xmxx "+qh+" xmxx left join");
				if( !qlrlxs.equals("") ){
					build.append(" (select t.xmbh,t.qlrlx,k.bdcqzh from bdck.bdcs_qlr_xz"+qh+" t left join bdck.bdcs_zs_xz"+qh+" k on t.xmbh=k.xmbh) b ON XMXX.XMBH =b.xmbh  LEFT JOIN ");
				}else{
					build.append(" (select k.xmbh,k.bdcqzh from bdck.bdcs_zs_xz"+qh+" k ) b on xmxx.xmbh=b.xmbh left join ");
				}
				
				//	.append("("+lx+") b ON XMXX.XMBH =b.xmbh  LEFT JOIN ")
				//.append(" (select k.xmbh,k.bdcqzh from bdck.bdcs_zs_xz"+qh+" k ) b on xmxx.xmbh=b.xmbh left join ")
				build.append(" bdck.bdcs_djsz"+qh+" djsz on b.xmbh=djsz.xmbh ")
				.append( "where  xmxx.sfdb='1'and b.bdcqzh like'%不动产证明%'")
				.append(" AND TO_CHAR(djsz.szsj,'yyyy-mm-dd') between '"+tjsjks+"' AND '"+tjsjjz+"'")
				.append( lx1)
				.append( djlx)
				.append( qlrlx)
				.append(" group by "+fzlxyear+"")
				.append(" union all ")
				.append(" select "+fzlx1+",0 as slgs,0 as bjgs,0 as BJL, 0 as bjsj,0 as FZZMGS,count(*) as FZZSGS,0 as bdbe ,0 as zge,0 as ysje,0 as ssje")
				.append("  from bdck.bdcs_xmxx"+qh+" xmxx left join ");
				if( !qlrlxs.equals("") ){
					build.append(" (select t.xmbh,t.qlrlx,k.bdcqzh from bdck.bdcs_qlr_xz"+qh+" t left join bdck.bdcs_zs_xz"+qh+" k on t.xmbh=k.xmbh) b ON XMXX.XMBH =b.xmbh  LEFT JOIN ");
				}else{
					build.append(" (select k.xmbh,k.bdcqzh from bdck.bdcs_zs_xz"+qh+" k ) b on xmxx.xmbh=b.xmbh left join ");
				}
			//	.append(" (select k.xmbh,k.bdcqzh from bdck.bdcs_zs_xz"+qh+" k ) b on xmxx.xmbh=b.xmbh left join ")
				build.append(" bdck.bdcs_djsz"+qh+" djsz on b.xmbh=djsz.xmbh ")
			//	.append("  LEFT JOIN ("+lx+") b ON XMXX.XMBH =b.xmbh ")
				.append(" where  xmxx.sfdb='1'and b.bdcqzh like '%不动产权第%'")
				.append("  AND TO_CHAR(djsz.szsj,'yyyy-mm-dd') between '"+tjsjks+"' AND '"+tjsjjz+"'")
				.append( lx1)
				.append( djlx)
				.append( qlrlx)
				.append("  group by "+fzlxyear+"")
				.append(" union all")
				.append("  select  "+fzlx+",0 as slgs,0 as bjgs,0 as BJL, 0 as bjsj,0 as FZZMGS,0 as FZZSGS, bdbe ,0 as zge,0 as ysje,0 as ssje from (")
				.append("  SELECT  "+fzlx+" ,round(SUM(bdbe)/10000,3) AS BDBE  FROM(")
				.append(" SELECT DISTINCT xmxx.project_id, xmxx.djlx,xmxx.qllx,")
				.append( lx2)
				.append(" nvl(z.bdbzzqse,0) bdbe,nvl(z.zgzqse,0) zge FROM bdck.bdcs_xmxx"+qh+" xmxx")
				.append(" LEFT JOIN bdck.bdcs_djdy_gz"+qh+" a ON a.xmbh=xmxx.xmbh");
				if( !qlrlxs.equals("") ){
					build.append(" LEFT JOIN ("+lx+") b ON XMXX.XMBH =b.xmbh ");
				}
				build.append(" LEFT JOIN bdck.bdcs_ql_gz"+qh+" c ON a.djdyid=c.djdyid AND a.xmbh=c.xmbh ")
				.append(" LEFT JOIN bdck.BDCS_FSQL_GZ"+qh+" z ON c.qlid=z.qlid ")
				.append(" left join bdck.bdcs_sqr"+qh+" sqr on xmxx.xmbh=sqr.xmbh  ")
				.append(" left join (select a.constvalue,a.consttrans from bdck.bdcs_const a  )  con ")
				.append("  on sqr.sqrlx=con.constvalue WHERE z.fsqlid IS NOT NULL  AND xmxx.qllx='23' AND xmxx.djlx in('100','700') AND xmxx.sfdb=1 and sqr.sqrlb=2 ")
				.append(" AND to_char(c.djsj,'yyyy-mm-dd') between '"+tjsjks+"' AND '"+tjsjjz+"'")
				.append( lx1)
				.append( djlx)
				.append( qlrlx)
				.append(" ) group by "+fzlxyear1+"    )")
				.append(" union all")
				.append("  select  "+fzlx+",0 as slgs,0 as bjgs,0 as BJL, 0 as bjsj,0 as FZZMGS,0 as FZZSGS,0 as bdbe , zge,0 as ysje,0 as ssje from (")
				.append(" SELECT "+fzlx+",round(SUM(zge)/10000,3) AS ZGE  FROM( ")
				.append(" SELECT DISTINCT xmxx.project_id, xmxx.djlx,xmxx.qllx, ")
				.append( lx2)
				.append(" nvl(z.bdbzzqse,0) bdbe,nvl(z.zgzqse,0) zge FROM bdck.bdcs_xmxx"+qh+" xmxx");
				if( !qlrlxs.equals("") ){
					build.append(" LEFT JOIN ("+lx+") b ON XMXX.XMBH =b.xmbh ");
				}
				build.append(" LEFT JOIN bdck.bdcs_djdy_gz"+qh+" a ON a.xmbh=xmxx.xmbh ")
				.append(" LEFT JOIN bdck.bdcs_ql_gz"+qh+" c ON a.djdyid=c.djdyid AND a.xmbh=c.xmbh")
				.append("  LEFT JOIN bdck.BDCS_FSQL_GZ"+qh+" z ON c.qlid=z.qlid ")
				.append("  left join bdck.bdcs_sqr"+qh+" sqr on xmxx.xmbh=sqr.xmbh ")
				.append(" left join (select a.constvalue,a.consttrans from bdck.bdcs_const a  )  con ")
				.append(" on sqr.sqrlx=con.constvalue WHERE z.fsqlid IS NOT NULL  AND xmxx.qllx='23' AND xmxx.djlx in('100','700') AND xmxx.sfdb=1 and sqr.sqrlb=2")
				.append("  AND to_char(c.djsj,'yyyy-mm-dd') between '"+tjsjks+"' AND '"+tjsjjz+"' ")
				.append( lx1)
				.append( djlx)
				.append( qlrlx)
				.append(" )")
				.append(" group by "+fzlxyear1+" )")
				.append(" union all")
				.append(" select  "+fzlx+",0 as slgs,0 as bjgs,0 as BJL, 0 as bjsj,0 as FZZMGS,0 as FZZSGS,0 as bdbe， 0 as zge,ysje,0 as ssje from (")
				.append("  SELECT  "+fzlx1+", round(sum(ysje)/10000,4) AS ysje FROM bdck.bdcs_xmxx"+qh+" xmxx");
				if( !qlrlxs.equals("") ){
					build.append(" LEFT JOIN ("+lx+") b ON XMXX.XMBH =b.xmbh ");
				}
				build.append(" left join bdck.bdcs_djsf"+qh+" t on xmxx.project_id = t.ywh")
				.append(" LEFT JOIN bdck.bdcs_sfdy"+qh+" y ON t.sfdyid=y.id ")
				.append(" WHERE t.ssje IS NOT NULL AND y.sfxlmc is not null  AND  y.sfkmmc is not null AND t.ywh IN( ")
				.append(" SELECT DISTINCT file_number AS project_id from bdc_workflow.wfi_proinst"+qh+"  where proinst_id in(")
				.append("  select proinst_id from bdc_workflow.wfi_actinst"+qh+" ac where  actinst_name  in ('收费发证','发证收费','综合科收费','收费','开票','科员开票','综合科开票')")
				.append("  and actinst_status=3  AND to_char(ac.actinst_end,'yyyy-mm-dd') BETWEEN '"+tjsjks+"' AND '"+tjsjjz+"' )")
				
				.append(" )")
				.append( djlx)
				.append( qlrlx)
				.append( lx1)
				.append(" group by "+fzlxyear+" )")
				.append(" union all")
				.append("  select  "+fzlx+",0 as slgs,0 as bjgs,0 as BJL, 0 as bjsj,0 as FZZMGS,0 as FZZSGS,0 as bdbe ，0 as zge,0 as ysje, ssje from (")
				.append("  SELECT  "+fzlx1+", round(sum(ssje)/10000,4) AS ssje  FROM bdck.bdcs_xmxx"+qh+" xmxx ");
				if( !qlrlxs.equals("") ){
					build.append(" LEFT JOIN ("+lx+") b ON XMXX.XMBH =b.xmbh ");
				}
				build.append(" left join bdck.bdcs_djsf"+qh+" t on xmxx.project_id = t.ywh")
				.append(" LEFT JOIN bdck.bdcs_sfdy"+qh+" y ON t.sfdyid=y.id")
				.append(" WHERE t.ssje IS NOT NULL AND y.sfxlmc is not null  AND  y.sfkmmc is not null AND t.ywh IN( ")
				.append(" SELECT DISTINCT file_number AS project_id from bdc_workflow.wfi_proinst"+qh+"  where proinst_id in(")
				.append("  select proinst_id from bdc_workflow.wfi_actinst"+qh+" ac where  actinst_name  in ('收费发证','发证收费','综合科收费','收费','开票','科员开票','综合科开票')")
				.append("  and actinst_status=3  AND to_char(ac.actinst_end,'yyyy-mm-dd') BETWEEN '"+tjsjks+"' AND '"+tjsjjz+"' )")
		 
				.append(" )")
				.append( djlx)
				.append( qlrlx)
				.append( lx1)
				.append(" group by "+fzlxyear+" )")
				.append(" ) t group by "+fzlxyear1 );
						
		List<Map> maps = null;
		if(fzlb.equals("行政区划")){
				StringBuilder build3 = new StringBuilder();
				StringBuilder buildyt1 = new StringBuilder();
				buildyt1.append("select "+qh1+" as qhmc,sum(slgs) as slgs, sum(bjgs) as bjgs, MAX(BJL) *100||'%'as BJL,round(sum(bjsj),3) as bjsj,sum(FZZMGS) as FZZMGS,sum(FZZSGS) as FZZSGS,")
				.append("round(sum(bdbe),3) as bdbe,round(sum(zge),3) as zge,round(sum(ysje),3) as ysje,round(sum(ssje),3) as ssje from ( ")
				.append("select '市本级' as qhmc,count(*) as slgs,0 as bjgs,0 as BJL,0 as bjsj,0 as FZZMGS,0 as FZZSGS,0 as bdbe ,0 as zge,0 as ysje,0 as ssje ")
				.append(" from BDCK.BDCS_XMXX"+qh+" xmxx ")
			//	.append(" LEFT JOIN ("+lx+") b ON xmxx.XMBH =b.xmbh ")
				.append(" where 1=1 AND TO_CHAR(xmxx.SLSJ,'yyyy-mm-dd') between '"+tjsjks+"' AND '"+tjsjjz+"' ")
				.append( lx1)
				.append( djlx)
				.append( qlrlx)
				
				.append(" union all")
				.append(" select "+qh1+" as qhmc,0 as slgs,count(*) as bjgs,0 as BJL,0 as bjsj,0 as FZZMGS,0 as FZZSGS,0 as bdbe ,0 as zge,0 as ysje,0 as ssje ")
				.append(" from BDCK.BDCS_XMXX"+qh+" xmxx ")
			//	.append("  LEFT JOIN ("+lx+") b ON xmxx.XMBH =b.xmbh")
				.append(" where xmxx.sfdb = '1' AND TO_CHAR(xmxx.SLSJ,'yyyy-mm-dd') between '"+tjsjks+"' AND '"+tjsjjz+"' ")
				.append( lx1)
				.append( djlx)
				.append( qlrlx)
				
				.append(" union all")
				.append(" select "+qh1+" as qhmc,0 as slgs,0 as bjgs, BJL, 0 as bjsj,0 as FZZMGS,0 as FZZSGS,0 as bdbe ,0 as zge,0 as ysje,0 as ssje from(")
				.append(" select  round(BJGS/SLGS,3)as BJL from (SELECT SUM(CASE WHEN SFDB = '1' THEN 1 ELSE 0 END) AS BJGS,SUM(CASE WHEN xmxx.xmbh is not null THEN 1 ELSE 0 END) AS SLGS ")
				.append(" FROM BDCK.BDCS_XMXX"+qh+" XMXX ")
			//	.append(" LEFT JOIN ("+lx+") b ON XMXX.XMBH =b.xmbh")
				.append(" WHERE TO_CHAR(XMXX.SLSJ,'yyyy-mm-dd') between '"+tjsjks+"' AND '"+tjsjjz+"' ")
				.append( lx1)
				.append( djlx)
				.append( qlrlx)
				
				.append(" )   )")
				.append(" union all")
				.append("  select "+qh1+" as qhmc,0 as slgs,0 as bjgs,0 as BJL, bjsj,0 as FZZMGS,0 as FZZSGS,0 as bdbe ,0 as zge,0 as ysje,0 as ssje from (")
				.append("  select sum(BJSJ)/count(xmbh) as bjsj from ( SELECT  xmxx.XMBH,a.Actinst_Name , a.actinst_end as dbsj ,")
				.append("  p.proinst_start as jssj ,TO_NUMBER(a.actinst_end-p.proinst_start) as bjsj  FROM bdc_workflow.wfi_actinst"+qh+" a ")
				.append("  left join bdc_workflow.wfi_proinst"+qh+" p on a.proinst_id=p.proinst_id ")
				.append("  left join BDCK.BDCS_XMXX"+qh+" xmxx on p.prolsh=xmxx.ywlsh")
			//	.append("  LEFT JOIN ("+lx+") b ON xmxx.XMBH =b.xmbh ")
				.append(" where xmxx.sfdb='1'  and a.actinst_name like  '%缮证%' and a.actinst_end is not null and a.actinst_start")
				.append(" BETWEEN   TO_DATE('"+tjsjks+"', 'yyyy-mm-dd ') AND  TO_DATE('"+tjsjjz+"', 'yyyy-mm-dd ')")
				.append(" union all ")
				.append("  SELECT xmxx.XMBH,a.Actinst_Name , a.actinst_end as dbsj , ")
				.append("  p.proinst_start as jssj ,TO_NUMBER(a.actinst_end-p.proinst_start) as bjsj  FROM bdc_workflow.wfi_actinst"+qh+" a ")
				.append("  left join bdc_workflow.wfi_proinst"+qh+" p on a.proinst_id=p.proinst_id ")
				.append("  left join BDCK.BDCS_XMXX"+qh+" xmxx on p.prolsh=xmxx.ywlsh")
			//	.append("  LEFT JOIN ("+lx+") b ON xmxx.XMBH =b.xmbh ")
				
				.append(" where xmxx.sfdb='1'  and a.actinst_name like '%登簿%' and a.actinst_end is not null and a.actinst_start")
				.append(" BETWEEN   TO_DATE('"+tjsjks+"', 'yyyy-mm-dd ') AND  TO_DATE('"+tjsjjz+"', 'yyyy-mm-dd ') and xmxx.djlx in (400,600,800) ")
				.append( lx1)
				.append( djlx)
				.append( qlrlx)
				.append(" ) )")
				.append(" union all")
				.append(" select "+qh1+" as qhmc,0 as slgs,0 as bjgs,0 as BJL, 0 as bjsj,count(*) as FZZMGS,0 as FZZSGS,0 as bdbe ,0 as zge,0 as ysje,0 as ssje")
				.append(" from BDCK.BDCS_XMXX"+qh+" xmxx left join")
				.append(" (select k.xmbh,k.bdcqzh from bdck.bdcs_zs_xz"+qh+" k ) b on xmxx.xmbh=b.xmbh left join ")
				.append(" bdck.bdcs_djsz"+qh+" djsz on b.xmbh=djsz.xmbh ")
			//	.append(" LEFT JOIN ("+lx+") b ON XMXX.XMBH =b.xmbh ")
				.append( "where  xmxx.sfdb='1'and b.bdcqzh like'%不动产证明%'")
				.append(" AND TO_CHAR(djsz.szsj,'yyyy-mm-dd') between '"+tjsjks+"' AND '"+tjsjjz+"'")
				.append( lx1)
				.append( djlx)
				.append( qlrlx)
				
				.append(" union all ")
				.append(" select "+qh1+" as qhmc,0 as slgs,0 as bjgs,0 as BJL, 0 as bjsj,0 as FZZMGS,count(*) as FZZSGS,0 as bdbe ,0 as zge,0 as ysje,0 as ssje")
				.append("  from BDCK.BDCS_XMXX"+qh+" xmxx left join ")
				.append(" (select k.xmbh,k.bdcqzh from bdck.bdcs_zs_xz"+qh+" k ) b on xmxx.xmbh=b.xmbh left join ")
				.append(" bdck.bdcs_djsz"+qh+" djsz on b.xmbh=djsz.xmbh ")
			//	.append("  LEFT JOIN ("+lx+") b ON XMXX.XMBH =b.xmbh ")
				.append(" where  xmxx.sfdb='1'and b.bdcqzh like '%不动产权第%'")
				.append("  AND TO_CHAR(djsz.szsj,'yyyy-mm-dd') between '"+tjsjks+"' AND '"+tjsjjz+"'")
				.append( lx1)
				.append( djlx)
				.append( qlrlx)
			
				.append(" union all")
				.append("  select "+qh1+" as qhmc,0 as slgs,0 as bjgs,0 as BJL, 0 as bjsj,0 as FZZMGS,0 as FZZSGS, bdbe ,0 as zge,0 as ysje,0 as ssje from (")
				.append("  SELECT  round(SUM(bdbe)/10000,3) AS BDBE  FROM(")
				.append(" SELECT DISTINCT xmxx.project_id, xmxx.djlx,xmxx.qllx,")
				.append( lx2)
				.append(" nvl(z.bdbzzqse,0) bdbe,nvl(z.zgzqse,0) zge FROM BDCK.BDCS_XMXX"+qh+" xmxx")
				.append(" LEFT JOIN bdck.bdcs_djdy_gz"+qh+" a ON a.xmbh=xmxx.xmbh")
			//	.append( "  LEFT JOIN ("+lx+") b ON xmxx.XMBH =b.xmbh ")
				.append(" LEFT JOIN bdck.bdcs_ql_gz"+qh+" c ON a.djdyid=c.djdyid AND a.xmbh=c.xmbh ")
				.append(" LEFT JOIN bdck.BDCS_FSQL_GZ"+qh+" z ON c.qlid=z.qlid ")
				.append(" left join bdck.bdcs_sqr"+qh+" sqr on xmxx.xmbh=sqr.xmbh  ")
				.append(" left join (select a.constvalue,a.consttrans from bdck.bdcs_const a  )  con ")
				.append("  on sqr.sqrlx=con.constvalue WHERE z.fsqlid IS NOT NULL  AND xmxx.qllx='23' AND xmxx.djlx in('100','700') AND xmxx.sfdb=1 and sqr.sqrlb=2 ")
				.append(" AND to_char(c.djsj,'yyyy-mm-dd') between '"+tjsjks+"' AND '"+tjsjjz+"'")
				.append( lx1)
				.append( djlx)
				.append( qlrlx)
				.append(" )     )")
				.append(" union all")
				.append("  select  "+qh1+" as qhmc,0 as slgs,0 as bjgs,0 as BJL, 0 as bjsj,0 as FZZMGS,0 as FZZSGS,0 as bdbe , zge,0 as ysje,0 as ssje from (")
				.append(" SELECT round(SUM(zge)/10000,3) AS ZGE  FROM( ")
				.append(" SELECT DISTINCT xmxx.project_id, xmxx.djlx,xmxx.qllx, ")
				.append( lx2)
				.append(" nvl(z.bdbzzqse,0) bdbe,nvl(z.zgzqse,0) zge FROM BDCK.BDCS_XMXX"+qh+" xmxx")
			//	.append(" LEFT JOIN ("+lx+") b ON xmxx.XMBH =b.xmbh ")
				.append(" LEFT JOIN bdck.bdcs_djdy_gz"+qh+" a ON a.xmbh=xmxx.xmbh ")
				.append(" LEFT JOIN bdck.bdcs_ql_gz"+qh+" c ON a.djdyid=c.djdyid AND a.xmbh=c.xmbh")
				.append("  LEFT JOIN bdck.BDCS_FSQL_GZ"+qh+" z ON c.qlid=z.qlid ")
				.append("  left join bdck.bdcs_sqr"+qh+" sqr on xmxx.xmbh=sqr.xmbh ")
				.append(" left join (select a.constvalue,a.consttrans from bdck.bdcs_const a  )  con ")
				.append(" on sqr.sqrlx=con.constvalue WHERE z.fsqlid IS NOT NULL  AND xmxx.qllx='23' AND xmxx.djlx in('100','700') AND xmxx.sfdb=1 and sqr.sqrlb=2")
				.append("  AND to_char(c.djsj,'yyyy-mm-dd') between '"+tjsjks+"' AND '"+tjsjjz+"' ")
				.append( lx1)
				.append( djlx)
				.append( qlrlx)
				.append(" )")
				.append("  )")
				.append(" union all")
				.append(" select "+qh1+" as qhmc,0 as slgs,0 as bjgs,0 as BJL, 0 as bjsj,0 as FZZMGS,0 as FZZSGS,0 as bdbe， 0 as zge,ysje,0 as ssje from (")
				.append("  SELECT   round(sum(ysje)/10000,4) AS ysje FROM BDCK.BDCS_XMXX"+qh+" xmxx")
			//	.append("  LEFT JOIN ("+lx+") b ON xmxx.XMBH =b.xmbh ")
				.append(" left join bdck.bdcs_djsf"+qh+" t on xmxx.project_id = t.ywh")
				.append(" LEFT JOIN bdck.bdcs_sfdy"+qh+" y ON t.sfdyid=y.id ")
				.append(" WHERE t.ssje IS NOT NULL AND y.sfxlmc is not null  AND  y.sfkmmc is not null AND t.ywh IN( ")
				.append(" SELECT DISTINCT file_number AS project_id from bdc_workflow.wfi_proinst"+qh+"  where proinst_id in(")
				.append("  select proinst_id from bdc_workflow.wfi_actinst"+qh+" ac where  actinst_name  in ('收费发证','发证收费','综合科收费','收费','开票','科员开票','综合科开票')")
				.append("  and actinst_status=3  AND to_char(ac.actinst_end,'yyyy-mm-dd') BETWEEN '"+tjsjks+"' AND '"+tjsjjz+"' )")
				.append(" )")
				.append( djlx)
				.append( qlrlx)
				.append( lx1)
				.append("  )")
				.append(" union all")
				.append("  select "+qh1+" as qhmc,0 as slgs,0 as bjgs,0 as BJL, 0 as bjsj,0 as FZZMGS,0 as FZZSGS,0 as bdbe ，0 as zge,0 as ysje, ssje from (")
				.append("  SELECT   round(sum(ssje)/10000,4) AS ssje  FROM BDCK.BDCS_XMXX"+qh+" xmxx ")
			//	.append("  LEFT JOIN ("+lx+") b ON xmxx.XMBH =b.xmbh ")
				.append(" left join bdck.bdcs_djsf"+qh+" t on xmxx.project_id = t.ywh")
				.append(" LEFT JOIN bdck.bdcs_sfdy"+qh+" y ON t.sfdyid=y.id")
				.append(" WHERE t.ssje IS NOT NULL AND y.sfxlmc is not null  AND  y.sfkmmc is not null AND t.ywh IN( ")
				.append(" SELECT DISTINCT file_number AS project_id from bdc_workflow.wfi_proinst"+qh+"  where proinst_id in(")
				.append("  select proinst_id from bdc_workflow.wfi_actinst"+qh+" ac where  actinst_name  in ('收费发证','发证收费','综合科收费','收费','开票','科员开票','综合科开票')")
				.append("  and actinst_status=3  AND to_char(ac.actinst_end,'yyyy-mm-dd') BETWEEN '"+tjsjks+"' AND '"+tjsjjz+"' )")
				.append(" )")
				.append( djlx)
				.append( qlrlx)
				.append( lx1)
				.append("  )")
				.append(" ) t group by qhmc  ");
						
			build3.append(" select   qhmc, sum(slgs) as slgs, sum(bjgs) as bjgs, MAX(BJL) as bjl, MAX(bjsj) as bjsj,sum(FZZMGS) as FZZMGS, sum(FZZSGS) as FZZSGS, MAX(bdbe) as bdbe, MAX(zge)as zge,MAX(ysje) as ysje, MAX(ssje) as ssje from ( ");

			   if(xzqh.equals("") || xzqh.equals("null") || xzqh.equals("10000,360622,360681,360600")){
				   for(int q1=0;q1<=2;q1++){
						if(q1==0){
							String xzqyt=buildyt1.toString();
							xzqyt=xzqyt.replaceAll("#2", "");
							xzqyt=xzqyt.replaceAll("#3", "'市本级'");
							build3.append(xzqyt.toString()+" union all ");
						}
						if(q1==1){
							String xzqgx=buildyt1.toString();
							xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
							xzqgx=xzqgx.replaceAll("#3", "'贵溪'");
							build3.append(xzqgx.toString()+" union all ");
						}
						if(q1==2){
							String xzqyj=buildyt1.toString();
							xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
							xzqyj=xzqyj.replaceAll("#3", "'余江'");
							build3.append(xzqyj.toString());
						}
					}		  		
				}else if(xzqh.equals("360600")){
					 String xzqyt=buildyt1.toString();
					 xzqyt=xzqyt.replaceAll("#2", "");
					 xzqyt=xzqyt.replaceAll("#3", "'市本级'");
					 build3.append(xzqyt.toString());
				}else if(xzqh.equals("360681")){
					 String xzqyt=buildyt1.toString();
					 xzqyt=xzqyt.replaceAll("#2", "@TO_ORCL6_BDCK");
					 xzqyt=xzqyt.replaceAll("#3", "'贵溪'");
					 build3.append(xzqyt.toString());
				}else if(xzqh.equals("360622")){
					 String xzqyt=buildyt1.toString();
					 xzqyt=xzqyt.replaceAll("#2", "@TO_ORCL7_BDCK");
					 xzqyt=xzqyt.replaceAll("#3", "'余江'");
					 build3.append(xzqyt.toString());
				}else if(xzqh.equals("360681,360600")){
					for(int q1=0;q1<=1;q1++){
						if(q1==0){
							String xzqyt=buildyt1.toString();
							xzqyt=xzqyt.replaceAll("#2", "");
							xzqyt=xzqyt.replaceAll("#3", "'市本级'");
							build3.append(xzqyt.toString()+" union all ");
						}
						if(q1==1){
							String xzqgx=buildyt1.toString();
							xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
							xzqgx=xzqgx.replaceAll("#3", "'贵溪'");
							build3.append(xzqgx.toString());
						}
					}
				}else if(xzqh.equals("360622,360600")){
					for(int q1=0;q1<=1;q1++){
						if(q1==0){
							String xzqyt=buildyt1.toString();
							xzqyt=xzqyt.replaceAll("#2", "");
							xzqyt=xzqyt.replaceAll("#3", "'市本级'");
							build3.append(xzqyt.toString()+" union all ");
						}
						if(q1==1){
							String xzqgx=buildyt1.toString();
							xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL7_BDCK");
							xzqgx=xzqgx.replaceAll("#3", "'余江'");
							build3.append(xzqgx.toString());
						}
					}
				}else{
					for(int q1=0;q1<=1;q1++){
						if(q1==0){
							String xzqyt=buildyt1.toString();
							xzqyt=xzqyt.replaceAll("#2", "@TO_ORCL6_BDCK");
							xzqyt=xzqyt.replaceAll("#3", "'贵溪'");
							build3.append(xzqyt.toString()+" union all ");
						}
						if(q1==1){
							String xzqgx=buildyt1.toString();
							xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL7_BDCK");
							xzqgx=xzqgx.replaceAll("#3", "'余江'");
							build3.append(xzqgx.toString());
						}
					}
				}
				  build3.append( ")")
						.append(" group by qhmc ");
			  maps= baseCommonDao.getDataListByFullSql(build3.toString());
		}else if(fzlb.equals("权利人类型")){
			StringBuilder buildqlrlx = new StringBuilder();
			buildqlrlx.append("select "+fzlx+",sum(slgs) as slgs, sum(bjgs) as bjgs, MAX(BJL) *100||'%'as BJL,round(sum(bjsj),3) as bjsj,sum(FZZMGS) as FZZMGS,sum(FZZSGS) as FZZSGS,")
			.append("round(sum(bdbe),3) as bdbe,round(sum(zge),3) as zge,round(sum(ysje),3) as ysje,round(sum(ssje),3) as ssje from ( ")
			.append("select "+fzlx1+",count(*) as slgs,0 as bjgs,0 as BJL,0 as bjsj,0 as FZZMGS,0 as FZZSGS,0 as bdbe ,0 as zge,0 as ysje,0 as ssje ")
			.append(" from bdck.bdcs_xmxx "+qh+" xmxx ")
			.append(" LEFT JOIN ("+lx+") b ON xmxx.XMBH =b.xmbh ")
			.append(" where 1=1 AND TO_CHAR(xmxx.SLSJ,'yyyy-mm-dd') between '"+tjsjks+"' AND '"+tjsjjz+"' ")
			.append( lx1)
			.append( djlx)
			.append( qlrlx)
			.append(" group by "+fzlxyear+" ")
			.append(" union all")
			.append(" select "+fzlx1+",0 as slgs,count(*) as bjgs,0 as BJL,0 as bjsj,0 as FZZMGS,0 as FZZSGS,0 as bdbe ,0 as zge,0 as ysje,0 as ssje ")
			.append(" from bdck.bdcs_xmxx"+qh+" xmxx LEFT JOIN ("+lx+") b ON xmxx.XMBH =b.xmbh where xmxx.sfdb = '1'")
			.append(" AND TO_CHAR(xmxx.SLSJ,'yyyy-mm-dd') between '"+tjsjks+"' AND '"+tjsjjz+"' ")
			.append( lx1)
			.append( djlx)
			.append( qlrlx)
			.append(" group by "+fzlxyear+"")
			.append(" union all")
			.append(" select   "+fzlx+",0 as slgs,0 as bjgs, BJL, 0 as bjsj,0 as FZZMGS,0 as FZZSGS,0 as bdbe ,0 as zge,0 as ysje,0 as ssje from(")
			.append(" select  "+fzlx+",round(BJGS/SLGS,3)as BJL from (SELECT "+fzlx1+" , SUM(CASE WHEN SFDB = '1' THEN 1 ELSE 0 END) AS BJGS,SUM(CASE WHEN xmxx.xmbh is not null THEN 1 ELSE 0 END) AS SLGS ")
			.append(" FROM bdck.bdcs_xmxx"+qh+" xmxx ")
			.append(" LEFT JOIN ("+lx+") b ON XMXX.XMBH =b.xmbh ")
			.append(" WHERE TO_CHAR(XMXX.SLSJ,'yyyy-mm-dd') between '"+tjsjks+"' AND '"+tjsjjz+"' ")
			.append( lx1)
			.append( djlx)
			.append( qlrlx)
			.append(" group by "+fzlxyear+"")
			.append(" order by "+fzlxyear1+")   )")
			.append(" union all")
			.append("  select  "+fzlx+",0 as slgs,0 as bjgs,0 as BJL, bjsj,0 as FZZMGS,0 as FZZSGS,0 as bdbe ,0 as zge,0 as ysje,0 as ssje from (")
			.append("  select "+fzlx+" ,sum(BJSJ)/count(xmbh) as bjsj from ( SELECT  "+fzlx1+" ,xmxx.XMBH,a.Actinst_Name , a.actinst_end as dbsj ,")
			.append("  p.proinst_start as jssj ,TO_NUMBER(a.actinst_end-p.proinst_start) as bjsj  FROM bdc_workflow.wfi_actinst"+qh+" a ")
			.append("  left join bdc_workflow.wfi_proinst"+qh+" p on a.proinst_id=p.proinst_id ")
			.append("  left join bdck.bdcs_xmxx"+qh+" xmxx on p.prolsh=xmxx.ywlsh")
			.append("  LEFT JOIN ("+lx+") b ON xmxx.XMBH =b.xmbh ")
			
			.append(" where xmxx.sfdb='1'  and a.actinst_name like '%缮证%' and a.actinst_end is not null and a.actinst_start")
			.append(" BETWEEN   TO_DATE('"+tjsjks+"', 'yyyy-mm-dd ') AND  TO_DATE('"+tjsjjz+"', 'yyyy-mm-dd ') ")
			.append(" union all ")
			.append(" SELECT  "+fzlx1+" ,xmxx.XMBH,a.Actinst_Name , a.actinst_end as dbsj , ")
			.append("  p.proinst_start as jssj ,TO_NUMBER(a.actinst_end-p.proinst_start) as bjsj  FROM bdc_workflow.wfi_actinst"+qh+" a ")
			.append("  left join bdc_workflow.wfi_proinst"+qh+" p on a.proinst_id=p.proinst_id ")
			.append("  left join bdck.bdcs_xmxx"+qh+" xmxx on p.prolsh=xmxx.ywlsh")
			.append("  LEFT JOIN ("+lx+") b ON xmxx.XMBH =b.xmbh ")
			
			.append(" where xmxx.sfdb='1'  and a.actinst_name like '%登簿%' and a.actinst_end is not null and a.actinst_start")
			.append(" BETWEEN   TO_DATE('"+tjsjks+"', 'yyyy-mm-dd ') AND  TO_DATE('"+tjsjjz+"', 'yyyy-mm-dd ') and xmxx.djlx in (400,600,800) ")
			.append( lx1)
			.append( djlx)
			.append( qlrlx)
			.append(" )  group by "+fzlxyear1+" )")
			.append(" union all")
			.append(" select "+fzlx1+",0 as slgs,0 as bjgs,0 as BJL, 0 as bjsj,count(*) as FZZMGS,0 as FZZSGS,0 as bdbe ,0 as zge,0 as ysje,0 as ssje")
			.append(" from bdck.bdcs_xmxx "+qh+" xmxx left join")
			.append(" (select distinct a.xmbh ,a.qlrlx,k.bdcqzh from bdck.bdcs_qlr_xz"+qh+" a left join bdck.bdcs_zs_xz"+qh+" k on a.xmbh=k.xmbh) b on xmxx.xmbh=b.xmbh left join ")
			.append(" bdck.bdcs_djsz"+qh+" djsz on b.xmbh=djsz.xmbh ")
		//	.append(" LEFT JOIN ("+lx+") b ON XMXX.XMBH =b.xmbh ")
			.append( "where  xmxx.sfdb='1'and b.bdcqzh like'%不动产证明%'")
			.append(" AND TO_CHAR(djsz.szsj,'yyyy-mm-dd') between '"+tjsjks+"' AND '"+tjsjjz+"'")
			.append( lx1)
			.append( djlx)
			.append( qlrlx)
			.append(" group by "+fzlxyear+"")
			.append(" union all ")
			.append(" select "+fzlx1+",0 as slgs,0 as bjgs,0 as BJL, 0 as bjsj,0 as FZZMGS,count(*) as FZZSGS,0 as bdbe ,0 as zge,0 as ysje,0 as ssje")
			.append("  from bdck.bdcs_xmxx"+qh+" xmxx left join ")
			.append(" (select distinct a.xmbh ,a.qlrlx,k.bdcqzh from bdck.bdcs_qlr_xz"+qh+" a left join bdck.bdcs_zs_xz"+qh+" k on a.xmbh=k.xmbh) b on xmxx.xmbh=b.xmbh left join ")
			.append(" bdck.bdcs_djsz"+qh+" djsz on b.xmbh=djsz.xmbh ")
		//	.append("  LEFT JOIN ("+lx+") b ON XMXX.XMBH =b.xmbh ")
			.append(" where  xmxx.sfdb='1'and b.bdcqzh like '%不动产权第%'")
			.append("  AND TO_CHAR(djsz.szsj,'yyyy-mm-dd') between '"+tjsjks+"' AND '"+tjsjjz+"'")
			.append( lx1)
			.append( djlx)
			.append( qlrlx)
			.append("  group by "+fzlxyear+"")
			.append(" union all")
			.append("  select  "+fzlx+",0 as slgs,0 as bjgs,0 as BJL, 0 as bjsj,0 as FZZMGS,0 as FZZSGS, bdbe ,0 as zge,0 as ysje,0 as ssje from (")
			.append("  SELECT  "+fzlx+" ,round(SUM(bdbe)/10000,3) AS BDBE  FROM(")
			.append(" SELECT DISTINCT xmxx.project_id, xmxx.djlx,xmxx.qllx,")
			.append( lx2)
			.append(" nvl(z.bdbzzqse,0) bdbe,nvl(z.zgzqse,0) zge FROM bdck.bdcs_xmxx"+qh+" xmxx")
			.append(" LEFT JOIN bdck.bdcs_djdy_gz"+qh+" a ON a.xmbh=xmxx.xmbh")
			.append( "  LEFT JOIN ("+lx+") b ON xmxx.XMBH =b.xmbh ")
			.append(" LEFT JOIN bdck.bdcs_ql_gz"+qh+" c ON a.djdyid=c.djdyid AND a.xmbh=c.xmbh ")
			.append(" LEFT JOIN bdck.BDCS_FSQL_GZ"+qh+" z ON c.qlid=z.qlid ")
			.append(" left join bdck.bdcs_sqr"+qh+" sqr on xmxx.xmbh=sqr.xmbh  ")
			.append(" left join (select a.constvalue,a.consttrans from bdck.bdcs_const a  )  con ")
			.append("  on sqr.sqrlx=con.constvalue WHERE z.fsqlid IS NOT NULL  AND xmxx.qllx='23' AND xmxx.djlx in('100','700') AND xmxx.sfdb=1 and sqr.sqrlb=2 ")
			.append(" AND to_char(c.djsj,'yyyy-mm-dd') between '"+tjsjks+"' AND '"+tjsjjz+"'")
			.append( lx1)
			.append( djlx)
			.append( qlrlx)
			.append(" ) group by "+fzlxyear1+"    )")
			.append(" union all")
			.append("  select  "+fzlx+",0 as slgs,0 as bjgs,0 as BJL, 0 as bjsj,0 as FZZMGS,0 as FZZSGS,0 as bdbe , zge,0 as ysje,0 as ssje from (")
			.append(" SELECT "+fzlx+",round(SUM(zge)/10000,3) AS ZGE  FROM( ")
			.append(" SELECT DISTINCT xmxx.project_id, xmxx.djlx,xmxx.qllx, ")
			.append( lx2)
			.append(" nvl(z.bdbzzqse,0) bdbe,nvl(z.zgzqse,0) zge FROM bdck.bdcs_xmxx"+qh+" xmxx")
			.append(" LEFT JOIN ("+lx+") b ON xmxx.XMBH =b.xmbh ")
			.append(" LEFT JOIN bdck.bdcs_djdy_gz"+qh+" a ON a.xmbh=xmxx.xmbh ")
			.append(" LEFT JOIN bdck.bdcs_ql_gz"+qh+" c ON a.djdyid=c.djdyid AND a.xmbh=c.xmbh")
			.append("  LEFT JOIN bdck.BDCS_FSQL_GZ"+qh+" z ON c.qlid=z.qlid ")
			.append("  left join bdck.bdcs_sqr"+qh+" sqr on xmxx.xmbh=sqr.xmbh ")
			.append(" left join (select a.constvalue,a.consttrans from bdck.bdcs_const a  )  con ")
			.append(" on sqr.sqrlx=con.constvalue WHERE z.fsqlid IS NOT NULL  AND xmxx.qllx='23' AND xmxx.djlx in('100','700') AND xmxx.sfdb=1 and sqr.sqrlb=2")
			.append("  AND to_char(c.djsj,'yyyy-mm-dd') between '"+tjsjks+"' AND '"+tjsjjz+"' ")
			.append( lx1)
			.append( djlx)
			.append( qlrlx)
			.append(" )")
			.append(" group by "+fzlxyear1+" )")
			.append(" union all")
			.append(" select  "+fzlx+",0 as slgs,0 as bjgs,0 as BJL, 0 as bjsj,0 as FZZMGS,0 as FZZSGS,0 as bdbe， 0 as zge,ysje,0 as ssje from (")
			.append("  SELECT  "+fzlx1+", round(sum(ysje)/10000,4) AS ysje FROM bdck.bdcs_xmxx"+qh+" xmxx")
			.append("  LEFT JOIN ("+lx+") b ON xmxx.XMBH =b.xmbh ")
			.append(" left join bdck.bdcs_djsf"+qh+" t on xmxx.project_id = t.ywh")
			.append(" LEFT JOIN bdck.bdcs_sfdy"+qh+" y ON t.sfdyid=y.id ")
			.append(" WHERE t.ssje IS NOT NULL AND y.sfxlmc is not null  AND  y.sfkmmc is not null AND t.ywh IN( ")
			.append(" SELECT DISTINCT file_number AS project_id from bdc_workflow.wfi_proinst"+qh+"  where proinst_id in(")
			.append("  select proinst_id from bdc_workflow.wfi_actinst"+qh+" ac where  actinst_name  in ('收费发证','发证收费','综合科收费','收费','开票','科员开票','综合科开票')")
			.append("  and actinst_status=3  AND to_char(ac.actinst_end,'yyyy-mm-dd') BETWEEN '"+tjsjks+"' AND '"+tjsjjz+"' )")
			
			.append(" )")
			.append( djlx)
			.append( qlrlx)
			.append( lx1)
			.append(" group by "+fzlxyear+" )")
			.append(" union all")
			.append("  select  "+fzlx+",0 as slgs,0 as bjgs,0 as BJL, 0 as bjsj,0 as FZZMGS,0 as FZZSGS,0 as bdbe ，0 as zge,0 as ysje, ssje from (")
			.append("  SELECT  "+fzlx1+", round(sum(ssje)/10000,4) AS ssje  FROM bdck.bdcs_xmxx"+qh+" xmxx ")
			.append("  LEFT JOIN ("+lx+") b ON xmxx.XMBH =b.xmbh ")
			.append(" left join bdck.bdcs_djsf"+qh+" t on xmxx.project_id = t.ywh")
			.append(" LEFT JOIN bdck.bdcs_sfdy"+qh+" y ON t.sfdyid=y.id")
			.append(" WHERE t.ssje IS NOT NULL AND y.sfxlmc is not null  AND  y.sfkmmc is not null AND t.ywh IN( ")
			.append(" SELECT DISTINCT file_number AS project_id from bdc_workflow.wfi_proinst"+qh+"  where proinst_id in(")
			.append("  select proinst_id from bdc_workflow.wfi_actinst"+qh+" ac where  actinst_name  in ('收费发证','发证收费','综合科收费','收费','开票','科员开票','综合科开票')")
			.append("  and actinst_status=3  AND to_char(ac.actinst_end,'yyyy-mm-dd') BETWEEN '"+tjsjks+"' AND '"+tjsjjz+"' )")
	 
			.append(" )")
			.append( djlx)
			.append( qlrlx)
			.append( lx1)
			.append(" group by "+fzlxyear+" )")
			.append(" ) t group by "+fzlxyear1 );
			
			StringBuilder build3 = new StringBuilder();
			build3.append(" select   "+fzlx+", sum(slgs) as slgs, sum(bjgs) as bjgs, MAX(BJL) as bjl, MAX(bjsj) as bjsj,sum(FZZMGS) as FZZMGS, sum(FZZSGS) as FZZSGS, MAX(bdbe) as bdbe, MAX(zge)as zge,MAX(ysje) as ysje, MAX(ssje) as ssje from ( ");
		 
		   if(xzqh.equals("") || xzqh.equals("null") || xzqh.equals("10000,360622,360681,360600")){
			   for(int q1=0;q1<=2;q1++){
					if(q1==0){
						String xzqyt=buildqlrlx.toString();
						xzqyt=xzqyt.replaceAll("#2", "");
						xzqyt=xzqyt.replaceAll("#3", "'市本级'");
						build3.append(xzqyt.toString()+" union all ");
					}
					if(q1==1){
						String xzqgx=buildqlrlx.toString();
						xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
						xzqgx=xzqgx.replaceAll("#3", "'贵溪'");
						build3.append(xzqgx.toString()+" union all ");
					}
					if(q1==2){
						String xzqyj=buildqlrlx.toString();
						xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
						xzqyj=xzqyj.replaceAll("#3", "'余江'");
						build3.append(xzqyj.toString());
					}
				}		  		
			}else if(xzqh.equals("360600")){
				 String xzqyt=buildqlrlx.toString();
				 xzqyt=xzqyt.replaceAll("#2", "");
				 xzqyt=xzqyt.replaceAll("#3", "'市本级'");
				 build3.append(xzqyt.toString());
			}else if(xzqh.equals("360681")){
				 String xzqyt=buildqlrlx.toString();
				 xzqyt=xzqyt.replaceAll("#2", "@TO_ORCL6_BDCK");
				 xzqyt=xzqyt.replaceAll("#3", "'贵溪'");
				 build3.append(xzqyt.toString());
			}else if(xzqh.equals("360622")){
				 String xzqyt=buildqlrlx.toString();
				 xzqyt=xzqyt.replaceAll("#2", "@TO_ORCL7_BDCK");
				 xzqyt=xzqyt.replaceAll("#3", "'余江'");
				 build3.append(xzqyt.toString());
			}else if(xzqh.equals("360681,360600")){
				for(int q1=0;q1<=1;q1++){
					if(q1==0){
						String xzqyt=buildqlrlx.toString();
						xzqyt=xzqyt.replaceAll("#2", "");
						xzqyt=xzqyt.replaceAll("#3", "'市本级'");
						build3.append(xzqyt.toString()+" union all ");
					}
					if(q1==1){
						String xzqgx=buildqlrlx.toString();
						xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
						xzqgx=xzqgx.replaceAll("#3", "'贵溪'");
						build3.append(xzqgx.toString());
					}
				}
			}else if(xzqh.equals("360622,360600")){
				for(int q1=0;q1<=1;q1++){
					if(q1==0){
						String xzqyt=buildqlrlx.toString();
						xzqyt=xzqyt.replaceAll("#2", "");
						xzqyt=xzqyt.replaceAll("#3", "'市本级'");
						build3.append(xzqyt.toString()+" union all ");
					}
					if(q1==1){
						String xzqgx=buildqlrlx.toString();
						xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL7_BDCK");
						xzqgx=xzqgx.replaceAll("#3", "'余江'");
						build3.append(xzqgx.toString());
					}
				}
			}else{
				for(int q1=0;q1<=1;q1++){
					if(q1==0){
						String xzqyt=buildqlrlx.toString();
						xzqyt=xzqyt.replaceAll("#2", "@TO_ORCL6_BDCK");
						xzqyt=xzqyt.replaceAll("#3", "'贵溪'");
						build3.append(xzqyt.toString()+" union all ");
					}
					if(q1==1){
						String xzqgx=buildqlrlx.toString();
						xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL7_BDCK");
						xzqgx=xzqgx.replaceAll("#3", "'余江'");
						build3.append(xzqgx.toString());
					}
				}
			}
			build3.append(" ) where "+fzlx+" is not null");
			build3.append(" group by "+fzlxyear+" ORDER BY "+fzlx);
		  maps= baseCommonDao.getDataListByFullSql(build3.toString());	
		}else{		
			StringBuilder build3 = new StringBuilder();
			build3.append(" select   "+fzlx+", sum(slgs) as slgs, sum(bjgs) as bjgs, MAX(BJL) as bjl, MAX(bjsj) as bjsj,sum(FZZMGS) as FZZMGS, sum(FZZSGS) as FZZSGS, MAX(bdbe) as bdbe, MAX(zge)as zge,MAX(ysje) as ysje, MAX(ssje) as ssje from ( ");
		 
			if(xzqh.equals("360600")){
				String xzqgx=build.toString();
				xzqgx=xzqgx.replaceAll("#2", "");
				maps= baseCommonDao.getDataListByFullSql(xzqgx.toString());
			}else if (xzqh.equals("360681")){
				String xzqgx=build.toString();
				xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
				maps= baseCommonDao.getDataListByFullSql(xzqgx.toString());
			}else if (xzqh.equals("360622")){
				String xzqgx=build.toString();
				xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL7_BDCK");
				maps= baseCommonDao.getDataListByFullSql(xzqgx.toString());	
			}else if(xzqh.equals("10000,360622,360681,360600") ||xzqh.equals("") || xzqh.equals("null")){
					for(int q1=0;q1<=2;q1++){
						if(q1==0){
							String xzqyt=build.toString();
							xzqyt=xzqyt.replaceAll("#2", "");
							build3.append(xzqyt.toString()+" union all ");
						}
						if(q1==1){
							String xzqgx=build.toString();
							xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
							build3.append(xzqgx.toString()+" union all ");
						}
						if(q1==2){
							String xzqyj=build.toString();
							xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
							build3.append(xzqyj.toString());
						}
					}	
					build3.append(" )");
					build3.append(" group by "+fzlxyear1+" ORDER BY "+fzlx);
				 maps= baseCommonDao.getDataListByFullSql(build3.toString());
			}else if(xzqh.equals("360681,360600")){
					for(int q1=0;q1<=1;q1++){
						if(q1==0){
							String xzqyt=build.toString();
							xzqyt=xzqyt.replaceAll("#2", "");
							build3.append(xzqyt.toString()+" union all ");
						}
						if(q1==1){
							String xzqgx=build.toString();
							xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
							build3.append(xzqgx.toString());
						}
					}	
					build3.append(" )");
					build3.append(" group by "+fzlxyear+" ORDER BY "+fzlx1);
				 maps= baseCommonDao.getDataListByFullSql(build3.toString());		
			}else if(xzqh.equals("360622,360600")){
					for(int q1=0;q1<=1;q1++){
						if(q1==0){
							String xzqyt=build.toString();
							xzqyt=xzqyt.replaceAll("#2", "");
							build3.append(xzqyt.toString()+" union all ");
						}
						if(q1==1){
							String xzqgx=build.toString();
							xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL7_BDCK");
							build3.append(xzqgx.toString());
						}
					}	
					build3.append(" )");
					build3.append(" group by "+fzlxyear+" ORDER BY "+fzlx1);
				 maps= baseCommonDao.getDataListByFullSql(build3.toString());		
			}else if(xzqh.equals("360622,360681")){
					for(int q1=0;q1<=1;q1++){
						if(q1==0){
							String xzqyt=build.toString();
							xzqyt=xzqyt.replaceAll("#2", "@TO_ORCL6_BDCK");
							build3.append(xzqyt.toString()+" union all ");
						}
						if(q1==1){
							String xzqgx=build.toString();
							xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL7_BDCK");
							build3.append(xzqgx.toString());
						}
					}	
					build3.append(" )");
					build3.append(" group by "+fzlxyear+" ORDER BY "+fzlx1);
				 maps= baseCommonDao.getDataListByFullSql(build3.toString());
			}
			if(!StringHelper.isEmpty(maps) && maps.size()>0)
			{
				for(Map map :maps)
				{
					String strdjlx=StringHelper.FormatByDatatype(map.get("DJLX"));
					map.put("DJLX", ConstHelper.getNameByValue("DJLX", strdjlx));
					String strqllx=StringHelper.FormatByDatatype(map.get("QLLX"));
					if(DJLX.YGDJ.Value.equals(strdjlx) && QLLX.QTQL.Value.equals(strqllx))
					{
						map.put("QLLX", "转移预告");
					}
					else if(DJLX.CFDJ.Value.equals(strdjlx) && QLLX.CFZX.Value.equals(strqllx))
					{
						map.put("QLLX", "解封");
					}
					else if(DJLX.CFDJ.Value.equals(strdjlx) && QLLX.QTQL.Value.equals(strqllx))
					{
						map.put("QLLX", "查封");
					}
					else
					{
					map.put("QLLX", ConstHelper.getNameByValue("QLLX", strqllx));
					}
				}
			}	
		}	
			m.setRows(maps);
			m.setTotal(maps.size());
			return m;
	}
			
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public Message Getefficiency_yt(String sjq,String sjz,String user,String select,String select_sj){
			Message m = new Message();
			
				try {
					StringBuilder buildhjsjq = new StringBuilder();
					StringBuilder buildhjsjz=new StringBuilder();
					StringBuilder builduser=new StringBuilder();
					
					StringBuilder build = new StringBuilder();
					StringBuilder build1 = new StringBuilder();
					if(!StringHelper.isEmpty(sjq)&&!StringHelper.isEmpty(sjz)){
						buildhjsjq.append(" and to_char(v.actinst_start,'yyyy-mm-dd') between '"+sjq+"' AND '"+sjz+"' ");
						buildhjsjz.append(" and to_char(v.actinst_end,'yyyy-mm-dd') between '"+sjq+"' AND '"+sjz+"' ");
											
					}
					if(!user.equals("()")){
					builduser.append(" and  u.id in "+user+" ");
					}
					String str  = "";
					String str1  = "";
					String str2  = "";
					String str3  = "";
					String str4  = "";
					String str5  = "";
					if(select.equals("人员")){
						str  = "d.departmentname,u.username,";
						str1 = "departmentname,username,";
						str2 = "d.departmentname,u.username";
						str3 = "dd.departmentname,dd.username";
						str4 = "d.departmentname,u.username,";
						str5 = "d.departmentname,u.username";
					}else if(select.equals("科室")){
						str  = "d.departmentname,";
						str1 = "departmentname,";
						str2 = "d.departmentname";
						str3 = "dd.departmentname";
						str4 = "d.departmentname,";
						str5 = "d.departmentname";
						
					}
						if(select_sj.equals("年度")){
							str+="to_char(v.actinst_start,'YYYY') as nd,";
							str1+="nd,";
							str2+=",to_char(v.actinst_start,'YYYY') ";
							str3+=",dd.nd";
							str4+="to_char(v.actinst_end,'YYYY') as nd,";
							str5+=",to_char(v.actinst_end,'YYYY')";
						}else if(select_sj.equals("季度")){
							str+="to_char(v.actinst_start,'YYYY') as nd,to_char(v.actinst_start,'Q') as jd,";
							str1+="nd||'第'||jd||'季度' as nd,";
							str2+=", to_char(v.actinst_start,'YYYY'),to_char(v.actinst_start,'Q')";
							str3+=",dd.nd,dd.Jd";
							str4+="to_char(v.actinst_end,'YYYY') as nd,to_char(v.actinst_end,'Q') as jd,";
							str5+=", to_char(v.actinst_end,'YYYY'),to_char(v.actinst_end,'Q')";
						}else if(select_sj.equals("月份")){
							str+="to_char(v.actinst_start,'YYYY') as nd,to_char(v.actinst_start,'Q') as jd,to_char(v.actinst_start,'MM') as yd,";
							str1+="nd||'第'||yd||'月' as nd,";
							str2+=", to_char(v.actinst_start,'YYYY'),to_char(v.actinst_start,'Q'),to_char(v.actinst_start,'MM')";
							str3+=",dd.nd,dd.Jd,dd.yd";
							str4+="to_char(v.actinst_end,'YYYY') as nd,to_char(v.actinst_end,'Q') as jd,to_char(v.actinst_end,'MM') as yd,";
							str5+=", to_char(v.actinst_end,'YYYY'),to_char(v.actinst_end,'Q'),to_char(v.actinst_end,'MM')";
						}
					
					
					
						build.append("SELECT "+str1+" SUM(ybjs) AS ybjs,SUM(zbjs)+sum(ybjs) AS bjl,sum(zbjs) AS zbjs,SUM(cqj) AS cqj,sum(zbcqj) AS zbcqj,(SUM(zbjs)+sum(ybjs)-SUM(cqj)) AS wcqj  FROM ( ")
							 .append("SELECT "+str4+" COUNT(v.actinst_id) AS ybjs,0 AS bjl,0 AS zbjs,0 AS cqj,0 AS zbcqj FROM ( ")
							 .append("select * from ( ")
							 .append(" select row_number() over(partition BY b.proinst_id, a.staff_id order by a.actinst_end DESC) rn,a.* ,b.prodef_name ")
							 .append("from BDC_WORKFLOW.Wfi_Proinst b LEFT JOIN bdc_workflow.wfi_actinst a ON a.proinst_id=b.proinst_id) ") 
							 .append("where rn = 1 AND actinst_status=3 ")  
							 .append(") v ") 
							 .append("LEFT JOIN smwb_framework.t_user u ON v.staff_id=u.id ") 
							 .append(" LEFT JOIN  smwb_framework.t_department d ON u.departmentid=d.id ")
							 .append("WHERE u.id IS NOT NULL ") 
							 .append(buildhjsjz).append(builduser)
							 .append("GROUP BY "+str5+" ")  
							 .append("UNION ")
							 .append("SELECT "+str+" 0 AS ybjs,0 AS bjl, COUNT(v.actinst_id) AS zbjs,0 AS cqj,0 AS zbcqj FROM ( ")
							 .append("select * from ( ")
							 .append(" select row_number() over(partition BY b.proinst_id, a.staff_id order by a.actinst_end DESC) rn,a.* ,b.prodef_name AS d_staff_id ")
							 .append("from BDC_WORKFLOW.Wfi_Proinst b LEFT JOIN bdc_workflow.wfi_actinst a ON a.proinst_id=b.proinst_id  ")
							 .append(" ) ") 
							 .append("where rn = 1  AND actinst_end IS NULL AND actinst_status<>3 ")
							 .append(" ) v ") 
							 .append("LEFT JOIN smwb_framework.t_user u ON v.staff_id=u.id ") 
							 .append(" LEFT JOIN  smwb_framework.t_department d ON u.departmentid=d.id ")
							 .append("WHERE u.id IS NOT NULL  ")
							 .append(buildhjsjq).append(builduser)
							 .append("GROUP BY "+str2+"   ")
							 .append("UNION ")
							 .append("SELECT "+str+" 0 AS ybjs,0 AS bjl,0 AS zbjs,COUNT(v.actinst_id) AS cqj,0 AS zbcqj  FROM ( ")
							 .append("select * from ( ")
							 .append("select row_number() over(partition BY b.proinst_id, a.staff_id order by a.actinst_end DESC) rn,a.* ,b.prodef_name ")
							 .append("from BDC_WORKFLOW.Wfi_Proinst b LEFT JOIN bdc_workflow.wfi_actinst a ON a.proinst_id=b.proinst_id) ") 
							 .append("where rn = 1 AND ((actinst_status=3 AND actinst_end > actinst_willfinish ) ") 
							 .append("OR (actinst_status<> 3 AND actinst_end IS NULL  AND Sysdate> actinst_willfinish)) AND OPERATION_TYPE <>40 AND INSTANCE_TYPE <>4 ") 
							 .append(") v ") 
							 .append("LEFT JOIN smwb_framework.t_user u ON v.staff_id=u.id ")
							 .append(" LEFT JOIN  smwb_framework.t_department d ON u.departmentid=d.id ")
							 .append("WHERE u.id IS NOT NULL ") 
							 .append(buildhjsjq).append(builduser)
							 .append("GROUP BY "+str2+" ")
							 .append("UNION ")
							 .append("SELECT "+str+" 0 AS ybjs,0 AS bjl,0 AS zbjs,0 AS cqj,COUNT(v.actinst_id) AS zbcqj FROM ( ")
							 .append("select * from ( ")
							 .append("select row_number() over(partition BY b.proinst_id, a.staff_id order by a.actinst_end DESC) rn,a.* ,b.prodef_name ")
							 .append("from BDC_WORKFLOW.Wfi_Proinst b LEFT JOIN bdc_workflow.wfi_actinst a ON a.proinst_id=b.proinst_id) ") 
							 .append("where rn = 1 AND (actinst_status<> 3 AND actinst_end IS NULL AND Sysdate> actinst_willfinish) AND OPERATION_TYPE <>40 AND INSTANCE_TYPE <>4")
							 .append(") v ") 
							 .append("LEFT JOIN smwb_framework.t_user u ON v.staff_id=u.id ")
							 .append(" LEFT JOIN  smwb_framework.t_department d ON u.departmentid=d.id ")
							 .append("WHERE u.id IS NOT NULL  ") 
							 .append(buildhjsjq).append(builduser)
							 .append("GROUP BY "+str2+" ")  
							 .append(")dd  GROUP BY "+str3+"  order by "+str3+"");

					List<Map> maps = baseCommonDao.getDataListByFullSql(build.toString());
						
					m.setRows(maps);
					m.setTotal(maps.size());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					m=null;
				}
				return m;
		
		}
		

	/**
	 * @作者 邹彦辉
     * @创建时间 2016年12月21日17:36:41
	 */
		@Override
		public Message GetZddyTJ(HttpServletRequest request) {
			Message m = new Message();
			try{
				String fzlb = new String(request.getParameter("push").getBytes("iso8859-1"), "utf-8");
				String xzqh = new String(request.getParameter("FW-XZQH").getBytes("iso8859-1"), "utf-8");
				String FW_QLRLX = new String(request.getParameter("FW-QLRLX").getBytes("iso8859-1"), "utf-8");
				String FW_FWYT = new String(request.getParameter("FW-FWYT").getBytes("iso8859-1"), "utf-8");
				String FW_FWLX = new String(request.getParameter("FW-FWLX").getBytes("iso8859-1"), "utf-8");
				String FW_FWXZ = new String(request.getParameter("FW-FWXZ").getBytes("iso8859-1"), "utf-8");
				String FW_HX = new String(request.getParameter("FW-HX").getBytes("iso8859-1"), "utf-8");
				String FW_HXJG = new String(request.getParameter("FW-HXJG").getBytes("iso8859-1"), "utf-8");
				String FW_FWJG = new String(request.getParameter("FW-FWJG").getBytes("iso8859-1"), "utf-8");
				String FW_FWCB = new String(request.getParameter("FW-CB").getBytes("iso8859-1"), "utf-8");
				String djq = new String(request.getParameter("FW-DJQ").getBytes("iso8859-1"), "utf-8");
				List<Map> maps = null;
				StringBuilder builddjqdm = new StringBuilder();
				StringBuilder builddjzqdm = new StringBuilder();
				StringBuilder build = new StringBuilder();
				StringBuilder buildfzqs = new StringBuilder();
				StringBuilder buildhead = new StringBuilder();
				StringBuilder buildend = new StringBuilder();
				StringBuilder buildtime  = new StringBuilder();
				StringBuilder buildtimeend  = new StringBuilder();
				StringBuilder builddjq = new StringBuilder();
				StringBuilder buildxzqh = new StringBuilder();
				StringBuilder buildyt = new StringBuilder();
				StringBuilder buildgx = new StringBuilder();
				StringBuilder buildyj = new StringBuilder();
				StringBuilder union = new StringBuilder();
				union.append(" UNION ALL");
				//分组类别
				StringBuilder buildtop = new StringBuilder();
				StringBuilder buildtjtj = new StringBuilder();
				StringBuilder buildcontname = new StringBuilder();
				StringBuilder buildnotnull = new StringBuilder();
				StringBuilder buildfzlb = new StringBuilder();
				int conts = 0;
				String qhlink = "#2";
				String strdjqdm="";
				String strdjzqdm="";
				String djqby = "";
				String djqfz="";
				String strqlrlx="";
				String strfwyt="";
				String strfwlx="";
				String strfwxz="";
				String strhx="";
				String strhxjg="";
				String strfwjg="";
				String strfwcb="";
				//=======================组织统计条件=========================
				String[] strtj=new String[8];
				strtj[0]=FW_QLRLX;
				strtj[1]=FW_FWYT;
				strtj[2]=FW_FWLX; 
				strtj[3]=FW_FWXZ;
				strtj[4]=FW_HX;
				strtj[5]=FW_HXJG;
				strtj[6]=FW_FWJG;
				strtj[7]=FW_FWCB;
				for(int z=0;z<strtj.length;z++){
					String s1 = strtj[z];
					if (s1 !="" && s1.length()>0) {
						String[] value = s1.split(",");
						for(int d =0; d<value.length;d++){
								if(z==0){
									if (!value[d].equals("10000")) {
										strqlrlx+="'"+value[d]+"',";
									}else{
										strqlrlx="";
										break;
									}
								}else if(z==1){
									if (!value[d].equals("10000")){
										strfwyt+="'"+value[d]+"',";
									}else{
										strfwyt="";
										break;
									}
								}else if(z==2){
									if (!value[d].equals("10000")){
										strfwlx+="'"+value[d]+"',";
									}else{
										strfwlx="";
										break;
									}
								}else if(z==3){
									if(!value[d].equals("10000")){
										strfwxz+="'"+value[d]+"',";
									}else{
										strfwxz="";
										break;
									}
								}else if(z==4){
									if(!value[d].equals("10000")){
										strhx+="'"+value[d]+"',";
									}else{
										strhx="";
										break;
									}
								}else if(z==5){
									if(!value[d].equals("10000")){
										strhxjg+="'"+value[d]+"',";
									}else{
										strhxjg	="";
										break;
									}
								}else if(z==6){
									if(!value[d].equals("10000")){
										strfwjg+="'"+value[d]+"',";
									}else{
										strfwjg	="";
										break;
									}
								}else if(z==7){
									if(!value[d].equals("10000")){
										strfwcb+="'"+value[d]+"',";
									}else{
										strfwcb	="";
										break;
									}
								}
							}
						}	
					}

				String fw_qlrlx="";
				if(strqlrlx.length()>0 && strqlrlx !=""){
					strqlrlx=strqlrlx.substring(0, strqlrlx.length()-1);
					fw_qlrlx=" and bdcdy.qlrlx in("+strqlrlx.toString()+")";
				}
				String fw_fwyt="";
				if(strfwyt.length()>0 && strfwyt !=""){
					strfwyt=strfwyt.substring(0, strfwyt.length()-1);
					fw_fwyt=" and bdcdy.fwyt in("+strfwyt+")";
				}
				String fw_fwlx ="";
				if(strfwlx.length()>0 && strfwlx != ""){
					strfwlx=strfwlx.substring(0, strfwlx.length()-1);
					fw_fwlx=" and bdcdy.fwlx in("+strfwlx+")";
				}
				String fw_fwxz ="";
				if(strfwxz.length()>0 && strfwxz !=""){
					strfwxz=strfwxz.substring(0, strfwxz.length()-1);
					fw_fwxz=" and bdcdy.fwxz in("+strfwxz+")";
				}
				String fw_hx="";
				if(strhx.length()>0 && strhx!=""){
					strhx=strhx.substring(0, strhx.length()-1);
					fw_hx=" and bdcdy.hx in("+strhx+")";
				}
				String fw_hxjg="";
				if(strhxjg.length()>0 && strhxjg != ""){
					strhxjg=strhxjg.substring(0, strhxjg.length()-1);
					fw_hxjg=" and bdcdy.hxjg in("+strhxjg+")";
				}
				String fw_fwjg="";
				if(strfwjg.length()>0 && strfwjg != ""){
					strfwjg=strfwjg.substring(0, strfwjg.length()-1);
					fw_fwjg=" and bdcdy.fwjg in("+strfwjg+")";
				}
				String fw_fwcb="";
				if(strfwcb.length()>0 && strfwcb != ""){
					strfwcb=strfwcb.substring(0, strfwcb.length()-1);
					fw_fwcb=" and bdcdy.fwcb in("+strfwcb+")";
				}
				buildtjtj.append(fw_qlrlx.toString()+fw_fwyt.toString()+fw_fwlx.toString()+fw_fwxz.toString()+fw_hx.toString()+fw_hxjg.toString()+fw_fwjg.toString()+fw_fwcb.toString());
				
				//分组类别
				if (fzlb.equals("行政区划")) {
					conts = 65;
					buildtop.append("");
					buildcontname.append("");
					buildnotnull.append("where bdcdy.BDCDYID is not null");
					buildfzlb.append("");
				}else if (fzlb.equals("权利人类型")) {
					conts = 36;
					buildtop.append("bdcdy.qlrlx,");
					buildcontname.append("and const.constvalue = qlr.qlrlx");
					buildnotnull.append("WHERE  bdcdy.qlrlx is not null");
					buildfzlb.append("GROUP BY bdcdy.qlrlx");
				}else if (fzlb.equals("房屋用途")) {
					conts = 17;
					buildtop.append("bdcdy.fwyt,");
					buildcontname.append("and const.constvalue = dy.fwyt");
					buildnotnull.append("WHERE  bdcdy.fwyt is not null");
					buildfzlb.append("GROUP BY bdcdy.fwyt");
				}else if (fzlb.equals("房屋类型")) {
					conts = 18;
					buildtop.append("bdcdy.fwlx,");
					buildcontname.append("and const.constvalue = dy.fwlx");
					buildnotnull.append("WHERE  bdcdy.fwlx is not null");
					buildfzlb.append("GROUP BY bdcdy.fwlx");
				}else if (fzlb.equals("房屋性质")) {
					conts = 19;
					buildtop.append("bdcdy.fwxz,");
					buildcontname.append("and const.constvalue = dy.fwxz");
					buildnotnull.append("WHERE  bdcdy.fwxz is not null");
					buildfzlb.append("GROUP BY bdcdy.fwxz");
				}else if (fzlb.equals("户型")) {
					conts = 15;
					buildtop.append("bdcdy.hx,");
					buildcontname.append("and const.constvalue = dy.hx");
					buildnotnull.append("WHERE  bdcdy.hx is not null");
					buildfzlb.append("GROUP BY bdcdy.hx");
				}else if (fzlb.equals("户型结构")) {
					conts = 16;
					buildtop.append("bdcdy.hxjg,");
					buildcontname.append("and const.constvalue = dy.hxjg");
					buildnotnull.append("WHERE  bdcdy.hxjg is not null");
					buildfzlb.append("GROUP BY bdcdy.hxjg");
				}else if (fzlb.equals("房屋结构")) {
					conts = 46;
					buildtop.append("bdcdy.fwjg,");
					buildcontname.append("and const.constvalue = dy.fwjg");
					buildnotnull.append("WHERE  bdcdy.fwjg is not null");
					buildfzlb.append("GROUP BY bdcdy.fwjg");
				}else if (fzlb.equals("产别")) {
					conts = 55;
					buildtop.append("bdcdy.fwcb,");
					buildcontname.append("and const.constvalue = dy.fwcb");
					buildnotnull.append("WHERE  bdcdy.fwcb is not null ");
					buildfzlb.append("GROUP BY bdcdy.fwcb");
				}else {
					conts = 65;
					buildtop.append("");
					buildcontname.append("");
					buildnotnull.append("");
					buildfzlb.append("");
				}
				
				  buildyt.append(" select")
						 .append("  '市本级' as FZLB,")
						 .append("  count(*) as HJ,")
						 .append("  SUM((CASE WHEN  to_char(t.QLLX_S) LIKE '%23%' THEN 1 ELSE 0 END)) as SFDY,")
						 .append("  SUM((CASE WHEN to_char(t.QLLX_S) LIKE '%99%' THEN 1 ELSE 0 END)) as SFCF,")
						 .append("  SUM((CASE WHEN to_char(t.DJLX) LIKE '%600%' THEN 1 ELSE 0 END)) as SFYY,")
						 .append("  ROUND(SUM(t.SCJZMJ),2)as SCJZMJ,")
						 .append("  ROUND(SUM(t.SCTNJZMJ),2)as SCTNJZMJ,")
						 .append("  ROUND(SUM(t.SCFTJZMJ),2)as SCFTJZMJ,")
						 .append("  ROUND(SUM(t.QDJG)/10000,2)AS JYJG")
						 .append("   from(")
						 .append("     select max(bdcdy.BDCDYID) as BDCDYID,min(bdcdy.FWLX_CON) as FWLX_CON,")
						 .append("     wm_concat(to_char(bdcdy.qlrmc)) as qlrmc,")
						 .append("     wm_concat(to_char(bdcdy.QLRLX)) as QLRLX_S,")
						 .append("     wm_concat(to_char(bdcdy.QLLX)) as QLLX_S,")
						 .append("     max(bdcdy.DJDYID) as DJDYID,")
						 .append("     max(bdcdy.FWLX) as FWLX,max(bdcdy.FWYT) as FWYT,max(bdcdy.HXJG) as HXJG,max(bdcdy.FWXZ) as FWXZ,max(bdcdy.FWJG) as FWJG,")
						 .append("     max(bdcdy.HX) as HX,max(bdcdy.GHYT) as GHYT,max(bdcdy.FWCB) as FWCB,sum(bdcdy.SCJZMJ) as SCJZMJ,sum(bdcdy.SCTNJZMJ) as SCTNJZMJ,")
						 .append("     sum(bdcdy.SCFTJZMJ) as SCFTJZMJ,max(bdcdy.BDCDYLX) as BDCDYLX,max(bdcdy.BDCDYLXMC) as BDCDYLXMC,max(bdcdy.QLID) as QLID,")
						 .append("     max(bdcdy.BDCQZH) as BDCQZH,max(bdcdy.QDJG) as QDJG,max(bdcdy.DJLX) as DJLX")
						 .append("     from")
						 .append("     (select ")
						 .append("           const.consttrans as FWLX_CON, qlr.qlrmc,qlr.qlrlx as QLRLX,DJDY.DJDYID,DY.BDCDYID,DY.FWLX,FWYT,")
						 .append("           DY.HXJG,DY.FWXZ,DY.FWJG,DY.HX,DY.GHYT,DY.FWCB,DY.JZMJ AS SCJZMJ,DY.TNJZMJ AS SCTNJZMJ,")
						 .append("           DY.FTJZMJ AS SCFTJZMJ,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC,QL.QLID,QL.BDCQZH,QL.QLLX AS QLLX,QL.QDJG, QL.DJLX")
						 .append("     from")
						 .append("     (SELECT  BDCDYID,FWLX,FWXZ,FWJG,HX,HXJG,GHYT,FWCB,FWYT1 as FWYT,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ ,'031' AS BDCDYLX,")
						 .append("           '现房' AS BDCDYLXMC   FROM BDCK.BDCS_H_XZ")
						 .append("           UNION")
						 .append("     SELECT  BDCDYID,FWLX,FWXZ,FWJG,HX,HXJG,GHYT,FWCB,FWYT1 as FWYT,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,'032' AS BDCDYLX,")
						 .append("           '期房' AS BDCDYLXMC  FROM BDCK.BDCS_H_XZY")
						 .append("     ) DY")
						 .append("     left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid ")
						 .append("     left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid AND (QLLX=4 OR qllx=99 or qllx=23 or DJLX=600)  ")
						 .append("     left join BDCK.bdcs_qlr_xz qlr on ql.qlid  = qlr.qlid ")
						 .append("     left join BDCK.BDCS_const const on const.constslsid='65' and const.constvalue = '360600'")
						 .append("     )bdcdy where bdcdy.BDCDYID is not null "+buildtjtj+" GROUP BY bdcdy.BDCDYID")
						 .append("  )t GROUP BY t.FWLX_CON");
				  buildgx.append(" select")
						 .append("  '贵溪' as FZLB,")
						 .append("  count(*) as HJ,")
						 .append("  SUM((CASE WHEN  to_char(t.QLLX_S) LIKE '%23%' THEN 1 ELSE 0 END)) as SFDY,")
						 .append("  SUM((CASE WHEN to_char(t.QLLX_S) LIKE '%99%' THEN 1 ELSE 0 END)) as SFCF,")
						 .append("  SUM((CASE WHEN to_char(t.DJLX) LIKE '%600%' THEN 1 ELSE 0 END)) as SFYY,")
						 .append("  ROUND(SUM(t.SCJZMJ),2)as SCJZMJ,")
						 .append("  ROUND(SUM(t.SCTNJZMJ),2)as SCTNJZMJ,")
						 .append("  ROUND(SUM(t.SCFTJZMJ),2)as SCFTJZMJ,")
						 .append("  ROUND(SUM(t.QDJG)/10000,2)AS JYJG")
						 .append("   from(")
						 .append("     select max(bdcdy.BDCDYID) as BDCDYID,min(bdcdy.FWLX_CON) as FWLX_CON,")
						 .append("     wm_concat(to_char(bdcdy.qlrmc)) as qlrmc,")
						 .append("     wm_concat(to_char(bdcdy.QLRLX)) as QLRLX_S,")
						 .append("     wm_concat(to_char(bdcdy.QLLX)) as QLLX_S,")
						 .append("     max(bdcdy.DJDYID) as DJDYID,")
						 .append("     max(bdcdy.FWLX) as FWLX,max(bdcdy.FWYT) as FWYT,max(bdcdy.HXJG) as HXJG,max(bdcdy.FWXZ) as FWXZ,max(bdcdy.FWJG) as FWJG,")
						 .append("     max(bdcdy.HX) as HX,max(bdcdy.GHYT) as GHYT,max(bdcdy.FWCB) as FWCB,sum(bdcdy.SCJZMJ) as SCJZMJ,sum(bdcdy.SCTNJZMJ) as SCTNJZMJ,")
						 .append("     sum(bdcdy.SCFTJZMJ) as SCFTJZMJ,max(bdcdy.BDCDYLX) as BDCDYLX,max(bdcdy.BDCDYLXMC) as BDCDYLXMC,max(bdcdy.QLID) as QLID,")
						 .append("     max(bdcdy.BDCQZH) as BDCQZH,max(bdcdy.QDJG) as QDJG,max(bdcdy.DJLX) as DJLX")
						 .append("     from")
						 .append("     (select ")
						 .append("           const.consttrans as FWLX_CON, qlr.qlrmc,qlr.qlrlx as QLRLX,DJDY.DJDYID,DY.BDCDYID,DY.FWLX,FWYT,")
						 .append("           DY.HXJG,DY.FWXZ,DY.FWJG,DY.HX,DY.GHYT,DY.FWCB,DY.JZMJ AS SCJZMJ,DY.TNJZMJ AS SCTNJZMJ,")
						 .append("           DY.FTJZMJ AS SCFTJZMJ,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC,QL.QLID,QL.BDCQZH,QL.QLLX AS QLLX,QL.QDJG, QL.DJLX")
						 .append("     from")
						 .append("     (SELECT  BDCDYID,FWLX,FWXZ,FWJG,HX,HXJG,GHYT,FWCB,FWYT1 as FWYT,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ ,'031' AS BDCDYLX,")
						 .append("           '现房' AS BDCDYLXMC   FROM BDCK.BDCS_H_XZ@TO_ORCL6_BDCK")
						 .append("           UNION")
						 .append("     SELECT  BDCDYID,FWLX,FWXZ,FWJG,HX,HXJG,GHYT,FWCB,FWYT1 as FWYT,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,'032' AS BDCDYLX,")
						 .append("           '期房' AS BDCDYLXMC  FROM BDCK.BDCS_H_XZY@TO_ORCL6_BDCK")
						 .append("     ) DY")
						 .append("     left join BDCK.BDCS_DJDY_XZ@TO_ORCL6_BDCK DJDY on dy.bdcdyid=djdy.bdcdyid ")
						 .append("     left join BDCK.bdcs_ql_xz@TO_ORCL6_BDCK ql on ql.djdyid=djdy.djdyid AND (QLLX=4 OR qllx=99 or qllx=23 or DJLX=600)  ")
						 .append("     left join BDCK.bdcs_qlr_xz@TO_ORCL6_BDCK qlr on ql.qlid  = qlr.qlid ")
						 .append("     left join BDCK.BDCS_const@TO_ORCL6_BDCK const on const.constslsid='65' and const.constvalue = '360681'")
						 .append("     )bdcdy where bdcdy.BDCDYID is not null "+buildtjtj+" GROUP BY bdcdy.BDCDYID")
						 .append("  )t GROUP BY t.FWLX_CON");
				  buildyj.append(" select")
						 .append("  '余江' as FZLB,")
						 .append("  count(*) as HJ,")
						 .append("  SUM((CASE WHEN  to_char(t.QLLX_S) LIKE '%23%' THEN 1 ELSE 0 END)) as SFDY,")
						 .append("  SUM((CASE WHEN to_char(t.QLLX_S) LIKE '%99%' THEN 1 ELSE 0 END)) as SFCF,")
						 .append("  SUM((CASE WHEN to_char(t.DJLX) LIKE '%600%' THEN 1 ELSE 0 END)) as SFYY,")
						 .append("  ROUND(SUM(t.SCJZMJ),2)as SCJZMJ,")
						 .append("  ROUND(SUM(t.SCTNJZMJ),2)as SCTNJZMJ,")
						 .append("  ROUND(SUM(t.SCFTJZMJ),2)as SCFTJZMJ,")
						 .append("  ROUND(SUM(t.QDJG)/10000,2)AS JYJG")
						 .append("   from(")
						 .append("     select max(bdcdy.BDCDYID) as BDCDYID,max(bdcdy.FWLX_CON) as FWLX_CON,")
						 .append("     wm_concat(to_char(bdcdy.qlrmc)) as qlrmc,")
						 .append("     wm_concat(to_char(bdcdy.QLRLX)) as QLRLX_S,")
						 .append("     wm_concat(to_char(bdcdy.QLLX)) as QLLX_S,")
						 .append("     max(bdcdy.DJDYID) as DJDYID,")
						 .append("     max(bdcdy.FWLX) as FWLX,max(bdcdy.FWYT) as FWYT,min(bdcdy.HXJG) as HXJG,max(bdcdy.FWXZ) as FWXZ,max(bdcdy.FWJG) as FWJG,")
						 .append("     max(bdcdy.HX) as HX,max(bdcdy.GHYT) as GHYT,max(bdcdy.FWCB) as FWCB,sum(bdcdy.SCJZMJ) as SCJZMJ,sum(bdcdy.SCTNJZMJ) as SCTNJZMJ,")
						 .append("     sum(bdcdy.SCFTJZMJ) as SCFTJZMJ,max(bdcdy.BDCDYLX) as BDCDYLX,max(bdcdy.BDCDYLXMC) as BDCDYLXMC,max(bdcdy.QLID) as QLID,")
						 .append("     max(bdcdy.BDCQZH) as BDCQZH,max(bdcdy.QDJG) as QDJG,max(bdcdy.DJLX) as DJLX")
						 .append("     from")
						 .append("     (select ")
						 .append("           const.consttrans as FWLX_CON, qlr.qlrmc,qlr.qlrlx as QLRLX,DJDY.DJDYID,DY.BDCDYID,DY.FWLX,FWYT,")
						 .append("           DY.HXJG,DY.FWXZ,DY.FWJG,DY.HX,DY.GHYT,DY.FWCB,DY.JZMJ AS SCJZMJ,DY.TNJZMJ AS SCTNJZMJ,")
						 .append("           DY.FTJZMJ AS SCFTJZMJ,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC,QL.QLID,QL.BDCQZH,QL.QLLX AS QLLX,QL.QDJG, QL.DJLX")
						 .append("     from")
						 .append("     (SELECT  BDCDYID,FWLX,FWXZ,FWJG,HX,HXJG,GHYT,FWCB,FWYT1 as FWYT,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ ,'031' AS BDCDYLX,")
						 .append("           '现房' AS BDCDYLXMC   FROM BDCK.BDCS_H_XZ@TO_ORCL7_BDCK")
						 .append("           UNION")
						 .append("     SELECT  BDCDYID,FWLX,FWXZ,FWJG,HX,HXJG,GHYT,FWCB,FWYT1 as FWYT,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,'032' AS BDCDYLX,")
						 .append("           '期房' AS BDCDYLXMC  FROM BDCK.BDCS_H_XZY@TO_ORCL7_BDCK")
						 .append("     ) DY")
						 .append("     left join BDCK.BDCS_DJDY_XZ@TO_ORCL7_BDCK DJDY on dy.bdcdyid=djdy.bdcdyid ")
						 .append("     left join BDCK.bdcs_ql_xz@TO_ORCL7_BDCK ql on ql.djdyid=djdy.djdyid AND (QLLX=4 OR qllx=99 or qllx=23 or DJLX=600)  ")
						 .append("     left join BDCK.bdcs_qlr_xz@TO_ORCL7_BDCK qlr on ql.qlid  = qlr.qlid ")
						 .append("     left join BDCK.BDCS_const@TO_ORCL7_BDCK const on const.constslsid='65' and const.constvalue = '360622'")
						 .append("     )bdcdy where bdcdy.BDCDYID is not null "+buildtjtj+" GROUP BY bdcdy.BDCDYID")
						 .append("  )t GROUP BY t.FWLX_CON");

				 		//性质区划
				 		if(xzqh.length()>0 && xzqh!=null && xzqh!=""){
				 			String[] str = xzqh.split(",");
				 			boolean qs = false;
				 			boolean yt = false;
				 			boolean gx = false;
				 			boolean yj = false;
				 			for(int i = 0; i < str.length; i++){
				 				if (str[i].equals("10000")) {
				 					qs=true;
								}else if (str[i].equals("360600")) {
									yt=true;
								}else if (str[i].equals("360681")) {
									gx=true;
								}else if (str[i].equals("360622")) {
									yj=true;
								}
				 			}
				 			if (qs==true) {
				 				buildxzqh.append(buildyt.toString()+union.toString()+ buildgx.toString()+union.toString()+buildyj.toString());
				 			}
				 			if (qs==false) {
				 				if (yt == true) {
				 					buildxzqh.append(buildyt.toString());
								}
				 				if (gx == true) {
									buildxzqh.append(buildgx.toString());
								}
				 				if (yj == true) {
									buildxzqh.append(buildyj.toString());
								}
				 				if (yt==true && gx==true) {
									buildxzqh.delete(0, buildxzqh.length());
									buildxzqh.append(buildyt.toString()+union.toString()+buildgx.toString());
								}
				 				if (yt==true && yj == true) {
									buildxzqh.delete(0, buildxzqh.length());
									buildxzqh.append(buildyt.toString()+union.toString()+buildyj.toString());
								}
				 				if (gx==true && yj == true) {
									buildxzqh.delete(0, buildxzqh.length());
									buildxzqh.append(buildgx.toString()+union.toString()+buildyj.toString());
								}
				 				if (yt==true && gx==true && yj == true ) {
				 					buildxzqh.append(buildyt.toString()+union.toString()+ buildgx.toString()+union.toString()+buildyj.toString());
								}
							}
				 		}else{
				 			buildxzqh.append(buildyt.toString()+union.toString()+ buildgx.toString()+union.toString()+buildyj.toString());
				 		}
				 		
				 		
				 		//地籍区
					   	   if(djq.length() >0 && djq !=null && djq != ""){
					   		   String[] value = djq.split(",");
					   		   for(int i =0; i<value.length;i++){
					   			if (value[i].length() == 9) {
					   				strdjqdm+="'"+value[i]+"',";
								}else{
									strdjzqdm+="'"+value[i]+"',";
								}
					   		  }
					   		if (strdjqdm.length()>0) {
					   			strdjqdm=strdjqdm.substring(0, strdjqdm.length()-1);
							}
					   		if (strdjzqdm.length()>0) {
					   			strdjzqdm=strdjzqdm.substring(0, strdjzqdm.length()-1);
							}
					   		if (strdjqdm!="") {
					   			builddjqdm.append("and bdcdy.DJQDM in("+strdjqdm+")");
							}
					   		if (strdjzqdm!="") {
					   			builddjzqdm.append("and bdcdy.DJZQDM in("+strdjzqdm+")");
							}
					   	 }
				 		
				 
			  buildhead.append(" select")
					   .append(" t.FWLX_CON as FZLB,")
					   .append(" count(*) as HJ,")
					   .append(" SUM((CASE WHEN  to_char(t.DYQL_S) LIKE '%23%' THEN 1 ELSE 0 END)) as SFDY,")
					   .append(" SUM((CASE WHEN to_char(t.CFQL_S) LIKE '%99%' THEN 1 ELSE 0 END)) as SFCF,")
					   .append(" SUM((CASE WHEN to_char(t.YYDJ) LIKE '%600%' THEN 1 ELSE 0 END)) as SFYY,")
					   .append(" ROUND(SUM(t.SCJZMJ),2)as SCJZMJ,")
					   .append(" ROUND(SUM(t.SCTNJZMJ),2)as SCTNJZMJ,")
					   .append(" ROUND(SUM(t.SCFTJZMJ),2)as SCFTJZMJ,")
					   .append(" ROUND(SUM(t.QDJG)/10000,2)AS JYJG")
					   .append("  from(");
			      build.append(" select max(bdcdy.BDCDYID) as BDCDYID,max(bdcdy.FWLX_CON) as FWLX_CON,wm_concat(to_char(bdcdy.qlrmc)) as qlrmc,")
			    	   .append("  wm_concat(to_char(bdcdy.QLRLX)) as QLRLX_S,wm_concat(to_char(bdcdy.QLLX)) as QLLX_S,wm_concat(to_char(bdcdy.DYQL)) as DYQL_S,")
			    	   .append("  wm_concat(to_char(bdcdy.CFQL)) as CFQL_S,max(bdcdy.DJLX) as YYDJ,max(bdcdy.DJDYID) as DJDYID,max(bdcdy.FWLX) as FWLX,")
			    	   .append("  max(bdcdy.FWYT) as FWYT,max(bdcdy.HXJG) as HXJG,max(bdcdy.FWXZ) as FWXZ,max(bdcdy.FWJG) as FWJG,max(bdcdy.HX) as HX,")
			    	   .append("  max(bdcdy.GHYT) as GHYT,max(bdcdy.FWCB) as FWCB,sum(bdcdy.SCJZMJ) as SCJZMJ,sum(bdcdy.SCTNJZMJ) as SCTNJZMJ,")
			    	   .append("  sum(bdcdy.SCFTJZMJ) as SCFTJZMJ,max(bdcdy.BDCDYLX) as BDCDYLX,max(bdcdy.BDCDYLXMC) as BDCDYLXMC,max(bdcdy.QLID) as QLID,")
			    	   .append("  max(bdcdy.BDCQZH) as BDCQZH,max(bdcdy.QDJG) as QDJG,MAX(bdcdy.DJQDM) AS DJQDM,MAX(bdcdy.DJZQDM) AS DJZQDM,MAX(bdcdy.DJQMC) AS DJQMCM,")
			    	   .append("  MAX(bdcdy.DJZQMC) AS DJZQMC")
					   .append("    from(");
			  buildtime.append(" select")
					   .append("  const.consttrans as FWLX_CON, qlr.qlrmc,qlr.qlrlx as QLRLX,DJDY.DJDYID,DY.BDCDYID,DY.FWLX,FWYT,DY.HXJG,DY.FWXZ,DY.FWJG,")
					   .append("  DY.HX,DY.GHYT,DY.FWCB,DY.JZMJ AS SCJZMJ,DY.TNJZMJ AS SCTNJZMJ,DY.FTJZMJ AS SCFTJZMJ,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC,")
					   .append("  QL.QLID,QL.BDCQZH,QL.QLLX AS QLLX,QL.QDJG,ZD.DJQDM as DJQDM,ZD.DJZQDM as DJZQDM,ZD.DJQMC as DJQMC,ZD.DJZQMC as DJZQMC,")
					   .append("  dyq.qllx as DYQL,cfq.qllx as CFQL,yyq.djlx as DJLX")
					   .append("    from")
					   .append("    (SELECT  BDCDYID,FWLX,FWXZ,FWJG,HX,HXJG,GHYT,FWCB,FWYT1 as FWYT,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ ,'031' AS BDCDYLX,")
					   .append("             ZDBDCDYID, '现房' AS BDCDYLXMC   FROM BDCK.BDCS_H_XZ"+qhlink+"")
					   .append("          UNION")
					   .append("    SELECT   BDCDYID,FWLX,FWXZ,FWJG,HX,HXJG,GHYT,FWCB,FWYT1 as FWYT,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,'032' AS BDCDYLX,")
					   .append("             ZDBDCDYID,'期房' AS BDCDYLXMC  FROM BDCK.BDCS_H_XZY"+qhlink+"")
					   .append("    ) DY")
					   .append("    left join BDCK.BDCS_DJDY_XZ"+qhlink+" DJDY on dy.bdcdyid=djdy.bdcdyid ")
					   .append("    left join BDCK.bdcs_ql_xz"+qhlink+" ql on ql.djdyid=djdy.djdyid AND QLLX=4")//所有权
					   .append("    left join BDCK.bdcs_ql_xz"+qhlink+" dyq on dyq.djdyid=djdy.djdyid and dyq.qllx=23 ")//抵押权
					   .append("    left join BDCK.bdcs_ql_xz"+qhlink+" cfq on cfq.djdyid=djdy.djdyid and cfq.qllx=99 ")//查封
					   .append("    left join BDCK.bdcs_ql_xz"+qhlink+" yyq on yyq.djdyid=djdy.djdyid and yyq.djlx=600")//异议
					   .append("    left join BDCK.bdcs_qlr_xz"+qhlink+" qlr on ql.qlid  = qlr.qlid")
					   .append("    left join BDCK.BDCS_SHYQZD_LS"+qhlink+" ZD ON DY.ZDBDCDYID=ZD.BDCDYID")
					   .append("    left join BDCK.BDCS_const"+qhlink+" const on const.constslsid='"+conts+"' "+buildcontname+"");
		   buildtimeend.append("    )bdcdy where bdcdy.BDCDYID is not null "+builddjqdm+" "+builddjzqdm+" "+buildtjtj+" GROUP BY bdcdy.BDCDYID");
		   	   buildend.append(" )t GROUP BY t.FWLX_CON");
				   		
				   		
		   	if(!fzlb.equals("性质区划") && !fzlb.equals("地籍区") && !fzlb.equals("地籍子区")){
		   		if(xzqh.length()>0 && xzqh!=null && xzqh!=""){
		   			if (xzqh.equals("10000,360600,360681,360622")) {
		   					for(int q1=0;q1<=2;q1++){
								if(q1==0){
									String xzqyt=buildtime.toString();
									xzqyt=xzqyt.replaceAll("#2", "");
									buildfzqs.append(buildhead+build.toString()+xzqyt.toString()+union);
								}
								if(q1==1){
									String xzqgx=buildtime.toString();
									xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
									buildfzqs.append(xzqgx.toString()+union);
								}
								if(q1==2){
									String xzqyj=buildtime.toString();
									xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
									buildfzqs.append(xzqyj.toString()+buildtimeend+buildend);
							}
		   				}
					}else if(xzqh.equals("360600,360681")){
							for(int q2=0;q2<=1;q2++){
								if(q2==0) {
									String xzqyt=buildtime.toString();
									xzqyt=xzqyt.replaceAll("#2", "");
									buildfzqs.append(buildhead+build.toString()+xzqyt.toString()+union);
								}
								if(q2==1) {
									String xzqgx=buildtime.toString();
									xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
									buildfzqs.append(xzqgx.toString()+buildtimeend+buildend);
								}
						}
					}else if(xzqh.equals("360600,360622")){
							for(int q3=0;q3<=1;q3++){
								if(q3==0) {
									String xzqyt=buildtime.toString();
									xzqyt=xzqyt.replaceAll("#2", "");
									buildfzqs.append(buildhead+build.toString()+xzqyt.toString()+union);
								}
								if(q3==1){
									String xzqyj=buildtime.toString();
									xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
									buildfzqs.append(xzqyj.toString()+buildtimeend+buildend);
							}
						}
					}else if(xzqh.equals("360681,360622")){
							for(int q4=0;q4<=1;q4++){
								if(q4==0) {
									String xzqgx=buildtime.toString();
									xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
									buildfzqs.append(buildhead+build.toString()+xzqgx.toString()+union);
								}
								if(q4==1){
									String xzqyj=buildtime.toString();
									xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
									buildfzqs.append(xzqyj.toString()+buildtimeend+buildend);
								}
							}
					}else if(xzqh.equals("360600")){
						String xzqyt=buildtime.toString();
						xzqyt=xzqyt.replaceAll("#2", "");
						buildfzqs.append(buildhead+build.toString()+xzqyt.toString()+buildtimeend+buildend);
					}else if(xzqh.equals("360681")){
						String xzqgx=buildtime.toString();
						xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
						buildfzqs.append(buildhead+build.toString()+xzqgx.toString()+buildtimeend+buildend);
					}else if(xzqh.equals("360622")){
						String xzqyj=buildtime.toString();
						xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
						buildfzqs.append(buildhead+build.toString()+xzqyj.toString()+buildtimeend+buildend);
					}
		   		}
		   	}
				   
				   		if(fzlb.equals("地籍区")){
				   		    djqby = "GROUP BY T.DJQDM";
				   		    djqfz = "max(t.djqmc)";
				   		}else{
				   		    djqby = "GROUP BY T.DJZQDM";
				   		    djqfz = "max(t.djzqmc)";
				   		}
				   
			   builddjq.append("select")
					   .append(" "+djqfz.toString()+" as FZLB, ")
					   .append(" count(*) as HJ,")
					   .append(" SUM((CASE WHEN  to_char(t.QLLX_S) LIKE '%23%' THEN 1 ELSE 0 END)) as SFDY,")
					   .append(" SUM((CASE WHEN to_char(t.QLLX_S) LIKE '%99%' THEN 1 ELSE 0 END)) as SFCF,")
					   .append(" SUM((CASE WHEN to_char(t.DJLX) LIKE '%600%' THEN 1 ELSE 0 END)) as SFYY,")
					   .append(" ROUND(SUM(t.SCJZMJ),2)as SCJZMJ,")
					   .append(" ROUND(SUM(t.SCTNJZMJ),2)as SCTNJZMJ,")
					   .append(" ROUND(SUM(t.SCFTJZMJ),2)as SCFTJZMJ,")
					   .append(" ROUND(SUM(t.QDJG)/10000,2)AS JYJG")
					   .append("  from(")
					   .append("    select max(bdcdy.BDCDYID) as BDCDYID,")
					   .append("    wm_concat(to_char(bdcdy.qlrmc)) as qlrmc,")
					   .append("    wm_concat(to_char(bdcdy.QLRLX)) as QLRLX_S,")
					   .append("    wm_concat(to_char(bdcdy.QLLX)) as QLLX_S,")
					   .append("    max(bdcdy.DJDYID) as DJDYID,")
					   .append("    max(bdcdy.FWLX) as FWLX,max(bdcdy.FWYT) as FWYT,max(bdcdy.HXJG) as HXJG,max(bdcdy.FWXZ) as FWXZ,max(bdcdy.FWJG) as FWJG,")
					   .append("    max(bdcdy.HX) as HX,max(bdcdy.GHYT) as GHYT,max(bdcdy.FWCB) as FWCB,sum(bdcdy.SCJZMJ) as SCJZMJ,sum(bdcdy.SCTNJZMJ) as SCTNJZMJ,")
					   .append("    sum(bdcdy.SCFTJZMJ) as SCFTJZMJ,max(bdcdy.BDCDYLX) as BDCDYLX,max(bdcdy.BDCDYLXMC) as BDCDYLXMC,max(bdcdy.QLID) as QLID,")
					   .append("    max(bdcdy.BDCQZH) as BDCQZH,max(bdcdy.QDJG) as QDJG,max(bdcdy.DJLX) as DJLX,MAX(bdcdy.DJQDM) AS DJQDM,MAX(bdcdy.DJZQDM) AS DJZQDM,")
					   .append("    MAX(bdcdy.DJQMC) AS DJQMC,MAX(bdcdy.DJZQMC) AS DJZQMC")
					   .append("    from")
					   .append("    (select ")
					   .append("          qlr.qlrmc,qlr.qlrlx as QLRLX,DJDY.DJDYID,DY.BDCDYID,DY.FWLX,FWYT,")
					   .append("          DY.HXJG,DY.FWXZ,DY.FWJG,DY.HX,DY.GHYT,DY.FWCB,DY.JZMJ AS SCJZMJ,DY.TNJZMJ AS SCTNJZMJ,")
					   .append("          DY.FTJZMJ AS SCFTJZMJ,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC,QL.QLID,QL.BDCQZH,QL.QLLX AS QLLX,QL.QDJG, QL.DJLX,")
					   .append("          ZD.DJQDM as DJQDM,ZD.DJZQDM as DJZQDM,ZD.DJQMC as DJQMC,ZD.DJZQMC as DJZQMC")
					   .append("    from")
					   .append("    (SELECT  BDCDYID,FWLX,FWXZ,FWJG,HX,HXJG,GHYT,FWCB,FWYT1 as FWYT,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ,SCFTJZMJ AS FTJZMJ ,'031' AS BDCDYLX,")
					   .append("             ZDBDCDYID, '现房' AS BDCDYLXMC   FROM BDCK.BDCS_H_XZ ")
					   .append("          UNION")
					   .append("    SELECT   BDCDYID,FWLX,FWXZ,FWJG,HX,HXJG,GHYT,FWCB,FWYT1 as FWYT,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ,YCFTJZMJ AS FTJZMJ,'032' AS BDCDYLX,")
					   .append("             ZDBDCDYID,'期房' AS BDCDYLXMC  FROM BDCK.BDCS_H_XZY")
					   .append("    ) DY")
					   .append("    left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid ")
					   .append("    left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid AND (QLLX=4 OR qllx=99 or qllx=23 or DJLX=600)  ")
					   .append("    left join BDCK.bdcs_qlr_xz qlr on ql.qlid  = qlr.qlid")
					   .append("    left join BDCK.BDCS_SHYQZD_LS ZD ON DY.ZDBDCDYID=ZD.BDCDYID")
					   .append("    )bdcdy where bdcdy.BDCDYID is not null "+builddjqdm+" "+builddjzqdm+" "+buildtjtj+" GROUP BY bdcdy.BDCDYID")
					   .append(" )t "+djqby.toString()+" ORDER BY FZLB DESC");

				   
				   
				   if (fzlb.equals("行政区划")) {
					   maps = baseCommonDao.getDataListByFullSql(buildxzqh.toString());
				   }else if(fzlb.equals("地籍区")||fzlb.equals("地籍子区")){
					   maps = baseCommonDao.getDataListByFullSql(builddjq.toString());
				   }else {
					   if(xzqh.length()>0 && xzqh!=null && xzqh!=""){
			   				maps = baseCommonDao.getDataListByFullSql(buildfzqs.toString());
			   			}else {
			   				String xzqyt=buildtime.toString();
							xzqyt=xzqyt.replaceAll("#2", "");
			   				maps = baseCommonDao.getDataListByFullSql(buildhead+build.toString()+xzqyt+buildtimeend+buildend);
						}
				   }
							
				m.setRows(maps);
				m.setTotal(maps.size());
			}catch(Exception e){
				m=null;
			}
			return m;

		}
		@Override
		public Message GetZddyTJTD(HttpServletRequest request){
			Message m = new Message();
			try{
				String fzlb = new String(request.getParameter("push").getBytes("iso8859-1"), "utf-8");
				String xzqh = new String(request.getParameter("TD-XZQH").getBytes("iso8859-1"), "utf-8");
				String qllx = new String(request.getParameter("TD-QLLX").getBytes("iso8859-1"), "utf-8");
				String qlsdfs = new String(request.getParameter("TD-QLSDFS").getBytes("iso8859-1"), "utf-8");
				String qlxz = new String(request.getParameter("TD-QLXZ").getBytes("iso8859-1"), "utf-8");
				String tddj = new String(request.getParameter("TD-TDDJ").getBytes("iso8859-1"), "utf-8");
				String qlrlx = new String(request.getParameter("TD-QLRLX").getBytes("iso8859-1"), "utf-8");
				String tdyt = new String(request.getParameter("TD-TDYT").getBytes("iso8859-1"), "utf-8");
				String djq = new String(request.getParameter("TD-DJQ").getBytes("iso8859-1"), "utf-8");
				List<Map> maps = null;
				StringBuilder buildtjtj = new StringBuilder();
				StringBuilder builddjqdm = new StringBuilder();
				StringBuilder builddjzqdm = new StringBuilder();
				StringBuilder build = new StringBuilder();
				StringBuilder buildhead = new StringBuilder();
				StringBuilder buildtime = new StringBuilder();
				StringBuilder buildtimeend = new StringBuilder();
				StringBuilder buildend = new StringBuilder();
				StringBuilder buildfzqs = new StringBuilder();
				StringBuilder builddjq = new StringBuilder();
				StringBuilder buildxzqh = new StringBuilder();
				StringBuilder buildqs = new StringBuilder();
				StringBuilder buildyt = new StringBuilder();
				StringBuilder buildgx = new StringBuilder();
				StringBuilder buildyj = new StringBuilder();
				StringBuilder union = new StringBuilder();
				union.append(" UNION ALL");
				//分组类别
				StringBuilder buildtop = new StringBuilder();
				StringBuilder buildcontname = new StringBuilder();
				StringBuilder buildnotnull = new StringBuilder();
				StringBuilder buildfzlb = new StringBuilder();
				int conts = 0;
				String qhlink = "#2";
				String strdjqdm="";
				String strdjzqdm="";
				String djqby = "";
				String strqllx="";
				String strqlsdfs="";
				String strqlxz="";
				String strtddj="";
				String strqlrlx="";
				String strtdyt="";
				//=======================组织统计条件=========================
				String[] strtj=new String[6];
				strtj[0]=qllx;
				strtj[1]=qlsdfs;
				strtj[2]=qlxz; 
				strtj[3]=tddj;
				strtj[4]=qlrlx;
				strtj[5]=tdyt;
				for(int z=0;z<strtj.length;z++){
					String s1 = strtj[z];
					if (s1 !="" && s1.length()>0) {
						String[] value = s1.split(",");
						for(int d =0; d<value.length;d++){
								if(z==0){
									if (!value[d].equals("10000")) {
										strqllx+="'"+value[d]+"',";
									}else{
										strqllx="";
										break;
									}
								}else if(z==1){
									if (!value[d].equals("10000")){
										strqlsdfs+="'"+value[d]+"',";
									}else{
										strqlsdfs="";
										break;
									}
								}else if(z==2){
									if (!value[d].equals("10000")){
										strqlxz+="'"+value[d]+"',";
									}else{
										strqlxz="";
										break;
									}
								}else if(z==3){
									if(!value[d].equals("10000")){
										strtddj+="'"+value[d]+"',";
									}else{
										strtddj="";
										break;
									}
								}else if(z==4){
									if(!value[d].equals("10000")){
										strqlrlx+="'"+value[d]+"',";
									}else{
										strqlrlx="";
										break;
									}
								}else if(z==5){
									if(!value[d].equals("10000")){
										strtdyt+="'"+value[d]+"',";
									}else{
										strtdyt	="";
										break;
									}
								}
							}	
						}
					}
				String td_qllx="";
				if(strqllx.length()>0 && strqllx !=""){
					strqllx=strqllx.substring(0, strqllx.length()-1);
					td_qllx=" and bdcdy.qllx in("+strqllx.toString()+")";
				}
				String td_qlsdfs="";
				if(strqlsdfs.length()>0 && strqlsdfs !=""){
					strqlsdfs=strqlsdfs.substring(0, strqlsdfs.length()-1);
					td_qlsdfs=" and bdcdy.qlsdfs in("+strqlsdfs+")";
				}
				String td_qlxz ="";
				if(strqlxz.length()>0 && strqlxz != ""){
					strqlxz=strqlxz.substring(0, strqlxz.length()-1);
					td_qlxz=" and bdcdy.qlxz in("+strqlxz+")";
				}
				String td_tddj ="";
				if(strtddj.length()>0 && strtddj !=""){
					strtddj=strtddj.substring(0, strtddj.length()-1);
					td_tddj=" and bdcdy.tddj in("+strtddj+")";
				}
				String td_qlrlx="";
				if(strqlrlx.length()>0 && strqlrlx != ""){
					strqlrlx=strqlrlx.substring(0, strqlrlx.length()-1);
					td_qlrlx=" and bdcdy.qlrlx in("+strqlrlx+")";
				}
				String td_tdyt="";
				if(strtdyt.length()>0 && strtdyt != ""){
					strtdyt=strtdyt.substring(0, strtdyt.length()-1);
					td_tdyt=" and bdcdy.tdyt in("+strtdyt+")";
				}
				buildtjtj.append(td_qllx.toString()+td_qlsdfs.toString()+td_qlxz.toString()+td_tddj.toString()+td_qlrlx.toString()+td_tdyt.toString());
				
				//分组类别
				if (fzlb.equals("行政区划")) {
					conts = 65;
					buildtop.append("");
					buildcontname.append("");
					buildnotnull.append("where bdcdy.BDCDYID is not null");
					buildfzlb.append("");
				}else if (fzlb.equals("权利类型")) {
					conts = 8;
					buildtop.append("bdcdy.qllx,");
					buildcontname.append("and const.constvalue = ql.qllx");
					buildnotnull.append("WHERE  bdcdy.qllx is not null");
					buildfzlb.append("GROUP BY bdcdy.qllx");
				}else if (fzlb.equals("权利设定方式")) {
					conts = 10;
					buildtop.append("bdcdy.qlsdfs,");
					buildcontname.append("and const.constvalue = dy.qlsdfs");
					buildnotnull.append("WHERE  bdcdy.qlsdfs is not null");
					buildfzlb.append("GROUP BY bdcdy.qlsdfs");
				}else if (fzlb.equals("权利性质")) {
					conts = 9;
					buildtop.append("bdcdy.qlxz,");
					buildcontname.append("and const.constvalue = dy.qlxz");
					buildnotnull.append("WHERE  bdcdy.qlxz is not null");
					buildfzlb.append("GROUP BY bdcdy.qlxz");
				}else if (fzlb.equals("土地等级")) {
					conts = 50;
					buildtop.append("bdcdy.tddj,");
					buildcontname.append("and const.constvalue = tdyt.tddj");
					buildnotnull.append("WHERE  bdcdy.tddj is not null");
					buildfzlb.append("GROUP BY bdcdy.tddj");
				}else if (fzlb.equals("权利人类型")) {
					conts = 36;
					buildtop.append("bdcdy.qlrlx,");
					buildcontname.append("and const.constvalue = qlr.qlrlx");
					buildnotnull.append("WHERE  bdcdy.qlrlx is not null");
					buildfzlb.append("GROUP BY bdcdy.qlrlx");
				}else if (fzlb.equals("土地用途")) {
					conts = 54;
					buildtop.append("bdcdy.tdyt,");
					buildcontname.append("and const.constvalue = tdyt.tdyt");
					buildnotnull.append("WHERE  bdcdy.tdyt is not null");
					buildfzlb.append("GROUP BY bdcdy.tdyt");
				}else {
					conts = 65;
					buildtop.append("");
					buildcontname.append("");
					buildnotnull.append("where bdcdy.BDCDYID is not null");
					buildfzlb.append("");
				}
				
				 buildyt.append("select ")
						 .append("  '市本级' as FZLB,")
						 .append("  count(*) as hj,")
						 .append("  SUM((CASE WHEN to_char(t.QLLX) LIKE '%23%' THEN 1 ELSE 0 END)) as SFDY,")
						 .append("  SUM((CASE WHEN to_char(t.QLLX) LIKE '%99%' THEN 1 ELSE 0 END)) as SFCF,")
						 .append("  SUM((CASE WHEN to_char(t.DJLX) LIKE '%600%' THEN 1 ELSE 0 END)) as SFYY,")
						 .append("  ROUND(SUM(t.ZDMJ),2)as ZDMJ,")
						 .append("  ROUND(SUM(t.QDJG)/10000,2)AS JYJG ")
						 .append("  from(")
						 .append("  select ")
						 .append("   max(bdcdy.DJDYID) as DJDYID,max(bdcdy.BDCDYID) as BDCDYID,max(bdcdy.BDCDYH) as BDCDYH,max(bdcdy.ZL) as ZL,max(bdcdy.ZDDM) as ZDDM,")
						 .append("   max(bdcdy.BDCDYLXMC) as BDCDYLXMC,max(bdcdy.QLID) as QLID,max(bdcdy.BDCQZH) as BDCQZH,max(bdcdy.DJLX) as DJLX,max(bdcdy.BDCDYLX) as BDCDYLX,")
						 .append("   sum(bdcdy.ZDMJ) as ZDMJ,sum(bdcdy.QDJG) as QDJG,max(bdcdy.QLSDFS) as QLSDFS,max(bdcdy.QLXZ) as QLXZ,max(bdcdy.TDDJ) as TDDJ,max(bdcdy.TDYT) as TDYT,")
						 .append("   wm_concat(to_char(bdcdy.QLLX)) as QLLX,")
						 .append("   wm_concat(to_char(bdcdy.QLRLX)) as QLRLX")
						 .append("    from (")
						 .append("    select DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,DY.BDCDYLXMC AS BDCDYLXMC,QL.QLID,QL.BDCQZH,QL.QLLX AS QLLX,QL.DJLX,")
						 .append("    DY.BDCDYLX AS BDCDYLX,DY.ZDMJ AS ZDMJ,QL.QDJG as QDJG,DY.QLSDFS,DY.QLXZ,TDYT.TDDJ AS TDDJ,QLR.QLRLX as QLRLX,TDYT.TDYT AS TDYT")
						 .append("    from (")
						 .append("    SELECT      ZL,ZDDM,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC,ZDMJ,QLSDFS,QLXZ   FROM BDCK.BDCS_SYQZD_XZ")
						 .append("    UNION ALL")
						 .append("    SELECT      ZL,ZDDM,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC ,ZDMJ,QLSDFS,QLXZ   FROM BDCK.BDCS_SHYQZD_XZ")
						 .append("    ) DY")
						 .append("    left join BDCK.bdcs_tdyt_xz tdyt on dy.bdcdyid=tdyt.bdcdyid")
						 .append("    left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid")
						 .append("    left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid AND (QLLX=3 OR qllx=99 or qllx=23 or DJLX=600)      ")
						 .append("    left join BDCK.bdcs_qlr_xz qlr on ql.qlid  = qlr.qlid      ")
						 .append("    left join BDCK.BDCS_const const on const.constslsid='65' and const.constvalue = '360600' ")
						 .append("    ) bdcdy WHERE bdcdy.BDCDYID is not null "+buildtjtj+" GROUP BY bdcdy.BDCDYID")
						 .append("  )t");
			      buildgx.append(" select ")
						 .append("  '贵溪' as FZLB,")
						 .append("  count(*) as hj,")
						 .append("  SUM((CASE WHEN to_char(t.QLLX) LIKE '%23%' THEN 1 ELSE 0 END)) as SFDY,")
						 .append("  SUM((CASE WHEN to_char(t.QLLX) LIKE '%99%' THEN 1 ELSE 0 END)) as SFCF,")
						 .append("  SUM((CASE WHEN to_char(t.DJLX) LIKE '%600%' THEN 1 ELSE 0 END)) as SFYY,")
						 .append("  ROUND(SUM(t.ZDMJ),2)as ZDMJ,")
						 .append("  ROUND(SUM(t.QDJG)/10000,2)AS JYJG ")
						 .append("  from(")
						 .append("  select ")
						 .append("   max(bdcdy.DJDYID) as DJDYID,max(bdcdy.BDCDYID) as BDCDYID,max(bdcdy.BDCDYH) as BDCDYH,max(bdcdy.ZL) as ZL,max(bdcdy.ZDDM) as ZDDM,")
						 .append("   max(bdcdy.BDCDYLXMC) as BDCDYLXMC,max(bdcdy.QLID) as QLID,max(bdcdy.BDCQZH) as BDCQZH,max(bdcdy.DJLX) as DJLX,max(bdcdy.BDCDYLX) as BDCDYLX,")
						 .append("   sum(bdcdy.ZDMJ) as ZDMJ,sum(bdcdy.QDJG) as QDJG,max(bdcdy.QLSDFS) as QLSDFS,max(bdcdy.QLXZ) as QLXZ,max(bdcdy.TDDJ) as TDDJ,max(bdcdy.TDYT) as TDYT,")
						 .append("   wm_concat(to_char(bdcdy.QLLX)) as QLLX,")
						 .append("   wm_concat(to_char(bdcdy.QLRLX)) as QLRLX")
						 .append("    from (")
						 .append("    select DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,DY.BDCDYLXMC AS BDCDYLXMC,QL.QLID,QL.BDCQZH,QL.QLLX AS QLLX,QL.DJLX,")
						 .append("    DY.BDCDYLX AS BDCDYLX,DY.ZDMJ AS ZDMJ,QL.QDJG as QDJG,DY.QLSDFS,DY.QLXZ,TDYT.TDDJ AS TDDJ,QLR.QLRLX as QLRLX,TDYT.TDYT AS TDYT")
						 .append("    from (")
						 .append("    SELECT      ZL,ZDDM,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC,ZDMJ,QLSDFS,QLXZ   FROM BDCK.BDCS_SYQZD_XZ@TO_ORCL6_BDCK")
						 .append("    UNION ALL")
						 .append("    SELECT      ZL,ZDDM,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC ,ZDMJ,QLSDFS,QLXZ   FROM BDCK.BDCS_SHYQZD_XZ@TO_ORCL6_BDCK")
						 .append("    ) DY")
						 .append("    left join BDCK.bdcs_tdyt_xz@TO_ORCL6_BDCK tdyt on dy.bdcdyid=tdyt.bdcdyid")
						 .append("    left join BDCK.BDCS_DJDY_XZ@TO_ORCL6_BDCK DJDY on dy.bdcdyid=djdy.bdcdyid")
						 .append("    left join BDCK.bdcs_ql_xz@TO_ORCL6_BDCK ql on ql.djdyid=djdy.djdyid AND (QLLX=3 OR qllx=99 or qllx=23 or DJLX=600)")
						 .append("    left join BDCK.bdcs_qlr_xz@TO_ORCL6_BDCK qlr on ql.qlid  = qlr.qlid")
						 .append("    left join BDCK.BDCS_const@TO_ORCL6_BDCK const on const.constslsid='65' and const.constvalue = '360681' ")
						 .append("    ) bdcdy WHERE bdcdy.BDCDYID is not null "+buildtjtj+" GROUP BY bdcdy.BDCDYID")
						 .append("  )t");
				  buildyj.append(" select ")
						 .append("  '余江' as FZLB,")
						 .append("  count(*) as hj,")
						 .append("  SUM((CASE WHEN to_char(t.QLLX) LIKE '%23%' THEN 1 ELSE 0 END)) as SFDY,")
						 .append("  SUM((CASE WHEN to_char(t.QLLX) LIKE '%99%' THEN 1 ELSE 0 END)) as SFCF,")
						 .append("  SUM((CASE WHEN to_char(t.DJLX) LIKE '%600%' THEN 1 ELSE 0 END)) as SFYY,")
						 .append("  ROUND(SUM(t.ZDMJ),2)as ZDMJ,")
						 .append("  ROUND(SUM(t.QDJG)/10000,2)AS JYJG ")
						 .append("  from(")
						 .append("  select ")
						 .append("   max(bdcdy.DJDYID) as DJDYID,max(bdcdy.BDCDYID) as BDCDYID,max(bdcdy.BDCDYH) as BDCDYH,max(bdcdy.ZL) as ZL,max(bdcdy.ZDDM) as ZDDM,")
						 .append("   max(bdcdy.BDCDYLXMC) as BDCDYLXMC,max(bdcdy.QLID) as QLID,max(bdcdy.BDCQZH) as BDCQZH,max(bdcdy.DJLX) as DJLX,max(bdcdy.BDCDYLX) as BDCDYLX,")
						 .append("   sum(bdcdy.ZDMJ) as ZDMJ,sum(bdcdy.QDJG) as QDJG,max(bdcdy.QLSDFS) as QLSDFS,max(bdcdy.QLXZ) as QLXZ,max(bdcdy.TDDJ) as TDDJ,max(bdcdy.TDYT) as TDYT,")
						 .append("   wm_concat(to_char(bdcdy.QLLX)) as QLLX,")
						 .append("   wm_concat(to_char(bdcdy.QLRLX)) as QLRLX")
						 .append("    from (")
						 .append("    select DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,DY.BDCDYLXMC AS BDCDYLXMC,QL.QLID,QL.BDCQZH,QL.QLLX AS QLLX,QL.DJLX,")
						 .append("    DY.BDCDYLX AS BDCDYLX,DY.ZDMJ AS ZDMJ,QL.QDJG as QDJG,DY.QLSDFS,DY.QLXZ,TDYT.TDDJ AS TDDJ,QLR.QLRLX as QLRLX,TDYT.TDYT AS TDYT")
						 .append("    from (")
						 .append("    SELECT      ZL,ZDDM,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC,ZDMJ,QLSDFS,QLXZ   FROM BDCK.BDCS_SYQZD_XZ@TO_ORCL7_BDCK")
						 .append("    UNION ALL")
						 .append("    SELECT      ZL,ZDDM,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC ,ZDMJ,QLSDFS,QLXZ   FROM BDCK.BDCS_SHYQZD_XZ@TO_ORCL7_BDCK")
						 .append("    ) DY")
						 .append("    left join BDCK.bdcs_tdyt_xz@TO_ORCL7_BDCK tdyt on dy.bdcdyid=tdyt.bdcdyid")
						 .append("    left join BDCK.BDCS_DJDY_XZ@TO_ORCL7_BDCK DJDY on dy.bdcdyid=djdy.bdcdyid")
						 .append("    left join BDCK.bdcs_ql_xz@TO_ORCL7_BDCK ql on ql.djdyid=djdy.djdyid AND (QLLX=3 OR qllx=99 or qllx=23 or DJLX=600)")
						 .append("    left join BDCK.bdcs_qlr_xz@TO_ORCL7_BDCK qlr on ql.qlid  = qlr.qlid")
						 .append("    left join BDCK.BDCS_const@TO_ORCL7_BDCK const on const.constslsid='65' and const.constvalue = '360622' ")
						 .append("    ) bdcdy WHERE bdcdy.BDCDYID is not null "+buildtjtj+" GROUP BY bdcdy.BDCDYID")
						 .append("  )t");
				
				//性质区划
			 		if(xzqh.length()>0 && xzqh!=null && xzqh!=""){
			 			String[] str = xzqh.split(",");
			 			boolean qs = false;
			 			boolean yt = false;
			 			boolean gx = false;
			 			boolean yj = false;
			 			for(int i = 0; i < str.length; i++){
			 				if (str[i].equals("10000")) {
			 					qs=true;
							}else if (str[i].equals("360600")) {
								yt=true;
							}else if (str[i].equals("360681")) {
								gx=true;
							}else if (str[i].equals("360622")) {
								yj=true;
							}
			 			}
			 			if (qs==true) {
			 				buildxzqh.append(buildyt.toString()+union.toString()+buildgx.toString()+union.toString()+buildyj.toString());
			 			}
			 			if (qs==false) {
			 				if (yt == true) {
			 					buildxzqh.append(buildyt.toString());
							}
			 				if (gx == true) {
								buildxzqh.append(buildgx.toString());
							}
			 				if (yj == true) {
								buildxzqh.append(buildyj.toString());
							}
			 				if (yt==true && gx==true) {
								buildxzqh.delete(0, buildxzqh.length());
								buildxzqh.append(buildyt.toString()+union.toString()+buildgx.toString());
							}
			 				if (yt==true && yj == true) {
								buildxzqh.delete(0, buildxzqh.length());
								buildxzqh.append(buildyt.toString()+union.toString()+buildyj.toString());
							}
			 				if (gx==true && yj == true) {
								buildxzqh.delete(0, buildxzqh.length());
								buildxzqh.append(buildgx.toString()+union.toString()+buildyj.toString());
							}
			 				if (yt==true && gx==true && yj == true ) {
			 					buildxzqh.append(buildqs.toString());
							}
						}
			 		}else{
			 			buildxzqh.append(buildyt.toString()+union.toString()+buildgx.toString()+union.toString()+buildyj.toString());
			 		}
				
			 		//地籍区
				   	   if(djq.length() >0 && djq !=null && djq != ""){
				   		   String[] value = djq.split(",");
				   		   for(int i =0; i<value.length;i++){
				   			if (value[i].length() == 9) {
				   				strdjqdm+="'"+value[i]+"',";
							}else{
								strdjzqdm+="'"+value[i]+"',";
							}
				   		  }
				   		if (strdjqdm.length()>0) {
				   			strdjqdm=strdjqdm.substring(0, strdjqdm.length()-1);
						}
				   		if (strdjzqdm.length()>0) {
				   			strdjzqdm=strdjzqdm.substring(0, strdjzqdm.length()-1);
						}
				   		if (strdjqdm!="") {
				   			builddjqdm.append("and bdcdy.DJQDM in("+strdjqdm+")");
						}
				   		if (strdjzqdm!="") {
				   			builddjzqdm.append("and bdcdy.DJZQDM in("+strdjzqdm+")");
						}
				   	 }
				
			  buildhead.append("select ")
					   .append("  max(t.FWLX_CON) as FZLB,")
					   .append("  count(*) as hj,")
					   .append("  SUM((CASE WHEN to_char(t.DYQL_S) LIKE '%23%' THEN 1 ELSE 0 END)) as SFDY,")
					   .append("  SUM((CASE WHEN to_char(t.CFQL_S) LIKE '%99%' THEN 1 ELSE 0 END)) as SFCF,")
					   .append("  SUM((CASE WHEN to_char(t.YYQL_S) LIKE '%600%' THEN 1 ELSE 0 END)) as SFYY,")
					   .append("  ROUND(SUM(t.ZDMJ),2)as ZDMJ,")
					   .append("  ROUND(SUM(t.QDJG)/10000,2)AS JYJG")
					   .append("  from(");
				  build.append(" select")
					   .append("   max(bdcdy.FWLX_CON) as FWLX_CON,max(bdcdy.DJDYID) as DJDYID,max(bdcdy.BDCDYID) as BDCDYID,max(bdcdy.BDCDYH) as BDCDYH,")
					   .append("   max(bdcdy.ZL) as ZL,max(bdcdy.ZDDM) as ZDDM,   max(bdcdy.BDCDYLXMC) as BDCDYLXMC,max(bdcdy.QLID) as QLID,max(bdcdy.BDCQZH) as BDCQZH,")
					   .append("   max(bdcdy.DJLX) as DJLX,max(bdcdy.BDCDYLX) as BDCDYLX,   sum(bdcdy.ZDMJ) as ZDMJ,sum(bdcdy.QDJG) as QDJG,max(bdcdy.QLSDFS) as QLSDFS,")
					   .append("   max(bdcdy.QLXZ) as QLXZ,max(bdcdy.TDDJ) as TDDJ,max(bdcdy.TDYT) as TDYT,max(bdcdy.DJQDM)as DJQDM,max(bdcdy.DJQMC) as DJQMC,")
					   .append("   max(bdcdy.DJZQDM) as DJZQDM,max(bdcdy.DJZQMC) as DJZQMC,wm_concat(to_char(bdcdy.QLRLX)) as QLRLX,wm_concat(to_char(bdcdy.QLLX)) as QLLX,")
					   .append("   wm_concat(to_char(bdcdy.DYQL)) as DYQL_S,wm_concat(to_char(bdcdy.CFQL)) as CFQL_S,wm_concat(to_char(bdcdy.DJLX)) as YYQL_S")
					   .append("   from (");
			  buildtime.append("    select const.consttrans as FWLX_CON,DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,DY.BDCDYLXMC AS BDCDYLXMC,QL.QLID,QL.BDCQZH,")
					   .append("    DY.BDCDYLX AS BDCDYLX,DY.ZDMJ AS ZDMJ,QL.QDJG as QDJG,DY.QLSDFS,DY.QLXZ,TDYT.TDDJ AS TDDJ,")
					   .append("    TDYT.TDYT AS TDYT,dy.DJQDM as DJQDM,dy.DJQMC as DJQMC,dy.DJZQDM as DJZQDM,dy.DJZQMC as DJZQMC,")
					   .append("    QLR.QLRLX as QLRLX,QL.QLLX AS QLLX,dyq.qllx as DYQL,cfq.qllx as CFQL,yyq.djlx as DJLX")
					   .append("    from (")
					   .append("    SELECT  ZL,ZDDM,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC,ZDMJ,QLSDFS,QLXZ,DJQDM,DJZQDM,DJQMC,DJZQMC")
					   .append("              FROM BDCK.BDCS_SYQZD_XZ"+qhlink+"")
					   .append("                union all")
					   .append("    SELECT  ZL,ZDDM,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC ,ZDMJ,QLSDFS,QLXZ,DJQDM,DJZQDM,DJQMC,DJZQMC")  
					   .append("              FROM BDCK.BDCS_SHYQZD_XZ"+qhlink+"")
					   .append("    ) DY ")
					   .append("    left join BDCK.bdcs_tdyt_xz"+qhlink+" tdyt on dy.bdcdyid=tdyt.bdcdyid")
					   .append("    left join BDCK.BDCS_DJDY_XZ"+qhlink+" DJDY on dy.bdcdyid=djdy.bdcdyid ")
					   .append("    left join BDCK.bdcs_ql_xz"+qhlink+" ql on ql.djdyid=djdy.djdyid AND QLLX=3")
					   .append("    left join BDCK.bdcs_ql_xz"+qhlink+" dyq on dyq.djdyid=djdy.djdyid and dyq.qllx=23")
					   .append("    left join BDCK.bdcs_ql_xz"+qhlink+" cfq on cfq.djdyid=djdy.djdyid and cfq.qllx=99")
					   .append("    left join BDCK.bdcs_ql_xz"+qhlink+" yyq on yyq.djdyid=djdy.djdyid and yyq.djlx=600")
					   .append("    left join BDCK.bdcs_qlr_xz"+qhlink+" qlr on ql.qlid  = qlr.qlid      ")
					   .append("    left join BDCK.BDCS_const"+qhlink+" const on const.constslsid='"+conts+"' "+buildcontname+"");
		       buildtimeend.append("    ) bdcdy where bdcdy.BDCDYID is not null "+builddjqdm+" "+builddjzqdm+""+buildtjtj+" GROUP BY bdcdy.BDCDYID ");
			   buildend.append("  )t group by t.FWLX_CON");

				   if(!fzlb.equals("行政区划") && !fzlb.equals("地籍区") && !fzlb.equals("地籍子区")){
				   		if(xzqh.length()>0 && xzqh!=null && xzqh!=""){
				   			if (xzqh.equals("10000,360600,360681,360622")) {
				   					for(int q1=0;q1<=2;q1++){
										if(q1==0){
											String xzqyt=buildtime.toString();
											xzqyt=xzqyt.replaceAll("#2", "");
											buildfzqs.append(buildhead+build.toString()+xzqyt.toString()+union);
										}
										if(q1==1){
											String xzqgx=buildtime.toString();
											xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
											buildfzqs.append(xzqgx.toString()+union);
										}
										if(q1==2){
											String xzqyj=buildtime.toString();
											xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
											buildfzqs.append(xzqyj.toString()+buildtimeend+buildend);
									}
				   				}
							}else if(xzqh.equals("360600,360681")){
									for(int q2=0;q2<=1;q2++){
										if(q2==0) {
											String xzqyt=buildtime.toString();
											xzqyt=xzqyt.replaceAll("#2", "");
											buildfzqs.append(buildhead+build.toString()+xzqyt.toString()+union);
										}
										if(q2==1) {
											String xzqgx=buildtime.toString();
											xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
											buildfzqs.append(xzqgx.toString()+buildtimeend+buildend);
										}
								}
							}else if(xzqh.equals("360600,360622")){
									for(int q3=0;q3<=1;q3++){
										if(q3==0) {
											String xzqyt=buildtime.toString();
											xzqyt=xzqyt.replaceAll("#2", "");
											buildfzqs.append(buildhead+build.toString()+xzqyt.toString()+union);
										}
										if(q3==1){
											String xzqyj=buildtime.toString();
											xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
											buildfzqs.append(xzqyj.toString()+buildtimeend+buildend);
									}
								}
							}else if(xzqh.equals("360681,360622")){
									for(int q4=0;q4<=1;q4++){
										if(q4==0) {
											String xzqgx=buildtime.toString();
											xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
											buildfzqs.append(buildhead+build.toString()+xzqgx.toString()+union);
										}
										if(q4==1){
											String xzqyj=buildtime.toString();
											xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
											buildfzqs.append(xzqyj.toString()+buildtimeend+buildend);
										}
									}
							}else if(xzqh.equals("360600")){
								String xzqyt=buildtime.toString();
								xzqyt=xzqyt.replaceAll("#2", "");
								buildfzqs.append(buildhead+build.toString()+xzqyt.toString()+buildtimeend+buildend);
							}else if(xzqh.equals("360681")){
								String xzqgx=buildtime.toString();
								xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
								buildfzqs.append(buildhead+build.toString()+xzqgx.toString()+buildtimeend+buildend);
							}else if(xzqh.equals("360622")){
								String xzqyj=buildtime.toString();
								xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
								buildfzqs.append(buildhead+build.toString()+xzqyj.toString()+buildtimeend+buildend);
							}
				   		}
				   	}
				   
					   if(fzlb.equals("地籍区")){
				   		    djqby = "GROUP BY bdcdy.DJQDM";
				   		}else{
				   		    djqby = "GROUP BY bdcdy.DJZQDM";
				   		}
				   
			   builddjq.append(" select")
					   .append("    max(bdcdy.DJQDM) as DJQDM,")
					   .append("    max(bdcdy.DJZQDM) as DJZQDM,")
					   .append("    max(bdcdy.DJQMC) as FZLB,")
					   .append("    count(*) as hj,")
					   .append("    wm_concat(distinct to_char(bdcdy.QLLX)) as QLLX_S,")
					   .append("    SUM((CASE WHEN  to_char(bdcdy.QLLX) LIKE '%23%' THEN 1 ELSE 0 END)) as SFDY,")
					   .append("    SUM((CASE WHEN to_char(bdcdy.QLLX) LIKE '%99%' THEN 1 ELSE 0 END)) as SFCF,")
					   .append("    SUM((CASE WHEN to_char(bdcdy.DJLX) LIKE '%600%' THEN 1 ELSE 0 END)) as SFYY,")
					   .append("    ROUND(SUM(bdcdy.ZDMJ),2)as ZDMJ,")
					   .append("    ROUND(SUM(QDJG)/10000)AS JYJG")
					   .append(" from ")
					   .append(" (select")
					   .append("    DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,")
					   .append("    DY.BDCDYLXMC AS BDCDYLXMC,QL.QLID,QL.BDCQZH,QL.QLLX AS QLLX,QL.DJLX,DY.BDCDYLX AS BDCDYLX,")
					   .append("    DY.ZDMJ AS ZDMJ,QL.QDJG,DY.QLSDFS,DY.QLXZ,TDYT.TDDJ AS TDDJ,QLR.QLRLX as QLRLX,TDYT.TDYT AS TDYT,")
					   .append("    DY.DJQDM,DY.DJQMC,DY.DJZQDM,DY.DJZQMC")
					   .append(" from (")
					   .append("   SELECT ")
					   .append("      ZL,ZDDM,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC,ZDMJ,QLSDFS,QLXZ,DJQDM,DJZQDM,DJQMC,DJZQMC")
					   .append("   FROM BDCK.BDCS_SYQZD_XZ")
					   .append("   UNION ALL")
					   .append("   SELECT ")
					   .append("      ZL,ZDDM,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC ,ZDMJ,QLSDFS,QLXZ,DJQDM,DJZQDM,DJQMC,DJZQMC")
					   .append("   FROM BDCK.BDCS_SHYQZD_XZ) DY")
					   .append("      left join BDCK.bdcs_tdyt_xz tdyt on dy.bdcdyid=tdyt.bdcdyid")
					   .append("      left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid")
					   .append("      left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid AND (QLLX=3 OR qllx=99 or qllx=23 or DJLX=600)")
					   .append("      left join BDCK.bdcs_qlr_xz qlr on ql.qlid  = qlr.qlid")
					   .append("  ) bdcdy where DJQMC is not null "+builddjqdm+" "+builddjzqdm+" "+buildtjtj+"")
			   		   .append(" "+djqby.toString()+" ORDER BY FZLB DESC");	   
			   
				   if (fzlb.equals("行政区划")) {
					   maps = baseCommonDao.getDataListByFullSql(buildxzqh.toString());
				   }else if(fzlb.equals("地籍区")||fzlb.equals("地籍子区")){
					   maps = baseCommonDao.getDataListByFullSql(builddjq.toString());
				   }else{
					   if(xzqh.length()>0 && xzqh!=null && xzqh!=""){
			   				maps = baseCommonDao.getDataListByFullSql(buildfzqs.toString());
			   			}else {
			   				String xzqyt=buildtime.toString();
							xzqyt=xzqyt.replaceAll("#2", "");
			   				maps = baseCommonDao.getDataListByFullSql(buildhead+build.toString()+xzqyt+buildtimeend+buildend);
						}
				   }
				   m.setRows(maps);
				   m.setTotal(maps.size());
				}catch(Exception e){
					m=null;
				}
				return m;

			}	
			
		/**
		 * 交易价格统计
		 * 邹彦辉
		 * 
		 */
		@Override
		public Message Getjyjgtjfw(HttpServletRequest request) {
			Message m = new Message();
			try{
				String fw_FZLB = new String(request.getParameter("push").getBytes("iso8859-1"), "utf-8");
				String fW_XZQH = new String(request.getParameter("FW-XZQH").getBytes("iso8859-1"), "utf-8");
				String fW_FWYT = new String(request.getParameter("FW-FWYT").getBytes("iso8859-1"), "utf-8");
				String fW_FWLX = new String(request.getParameter("FW-FWLX").getBytes("iso8859-1"), "utf-8");
				String fW_FWJG = new String(request.getParameter("FW-FWJG").getBytes("iso8859-1"), "utf-8");
				String fW_FWXZ = new String(request.getParameter("FW-FWXZ").getBytes("iso8859-1"), "utf-8");
				String fW_HX = new String(request.getParameter("FW-HX").getBytes("iso8859-1"), "utf-8");
				String fW_HXJG = new String(request.getParameter("FW-HXJG").getBytes("iso8859-1"), "utf-8");
				String fW_KSSJ = new String(request.getParameter("FW-KSSJ").getBytes("iso8859-1"), "utf-8");
				String fW_JSSJ = new String(request.getParameter("FW-JSSJ").getBytes("iso8859-1"), "utf-8");
				String djq = new String(request.getParameter("FW-DJQ").getBytes("iso8859-1"), "utf-8");
				List<Map> maps = null;
				//行政区划
				StringBuilder build = new StringBuilder();
				StringBuilder buildtime  = new StringBuilder();
				StringBuilder buildtimeend  = new StringBuilder();
				StringBuilder buildxzqh = new StringBuilder();
				StringBuilder buildhead = new StringBuilder();
				StringBuilder buildend = new StringBuilder();
				StringBuilder buildyt = new StringBuilder();
				StringBuilder buildfzqs = new StringBuilder();
				StringBuilder union = new StringBuilder();
				union.append(" UNION ALL");
				//分组类别
				StringBuilder buildtop = new StringBuilder();
				StringBuilder buildfzcons = new StringBuilder();
				StringBuilder buildcontname = new StringBuilder();
				StringBuilder buildnotnull = new StringBuilder();
				StringBuilder buildfzlb = new StringBuilder();
				StringBuilder builddjq = new StringBuilder();
				StringBuilder builddjqdm = new StringBuilder();
				StringBuilder builddjzqdm = new StringBuilder();
				//时间条件
				StringBuilder buildTimeNian = new StringBuilder();
				StringBuilder buildTimeJidu = new StringBuilder();
				StringBuilder buildTimeYueFen = new StringBuilder();
				StringBuilder buildGroupTime = new StringBuilder();
				StringBuilder buildoOrderTime = new StringBuilder();
				//统计条件
				StringBuilder buildtjtj = new StringBuilder();
				StringBuilder builddjsj = new StringBuilder();
				String strtime = "";
				String strfwyt ="";
				String strfwlx ="";
				String strfwxz ="";
				String strhx ="";
				String strfwjg ="";
				String strhxjg ="";
				String strdjqdm="";
				String strdjzqdm="";
				String djqby = "";
				int constslsid = 0;
				String constvalue = "";
				String ndjdy = "";
				String qhmc = "#1";
				String qhlink = "#2";
				String qhdm = "#3";
				//=======================组织统计条件=========================
				String[] strtj=new String[6];
				strtj[0]=fW_FWYT;
				strtj[1]=fW_FWLX;
				strtj[2]=fW_FWXZ;
				strtj[3]=fW_HX;
				strtj[4]=fW_FWJG;
				strtj[5]=fW_HXJG;
				for(int z=0;z<strtj.length;z++){
					String s1 = strtj[z];
					if (s1 !="" && s1.length()>0) {
						String[] value = s1.split(",");
						for(int d =0; d<value.length;d++){
								if(z==0){
									if (!value[d].equals("10000")) {
										strfwyt+="'"+value[d]+"',";
									}else{
										strfwyt="";
										break;
									}
								}else if(z==1){
									if (!value[d].equals("10000")){
										strfwlx+="'"+value[d]+"',";
									}else{
										strfwlx="";
										break;
									}
								}else if(z==2){
									if (!value[d].equals("10000")){
										strfwxz+="'"+value[d]+"',";
									}else{
										strfwxz="";
										break;
									}
								}else if(z==3){
									if(!value[d].equals("10000")){
										strhx+="'"+value[d]+"',";
									}else{
										strhx="";
										break;
									}
								}else if(z==4){
									if(!value[d].equals("10000")){
										strfwjg+="'"+value[d]+"',";
									}else{
										strfwjg="";
										break;
									}
								}else if(z==5){
									if(!value[d].equals("10000")){
										strhxjg+="'"+value[d]+"',";
									}else{
										strhxjg	="";
										break;
									}
								}
							}
						}	
					}

				String fwyt="";
				if(strfwyt.length()>0 && strfwyt !=""){
					strfwyt=strfwyt.substring(0, strfwyt.length()-1);
					fwyt=" and bdcdy.fwyt in("+strfwyt.toString()+")";
				}
				String fwlx="";
				if(strfwlx.length()>0 && strfwlx !=""){
					strfwlx=strfwlx.substring(0, strfwlx.length()-1);
					fwlx=" and bdcdy.fwlx in("+strfwlx+")";
				}
				String fwxz ="";
				if(strfwxz.length()>0 && strfwxz != ""){
					strfwxz=strfwxz.substring(0, strfwxz.length()-1);
					fwxz=" and bdcdy.fwxz in("+strfwxz+")";
				}
				String hx ="";
				if(strhx.length()>0 && strhx !=""){
					strhx=strhx.substring(0, strhx.length()-1);
					hx=" and bdcdy.hx in("+strhx+")";
				}
				String fwjg="";
				if(strfwjg.length()>0 && strfwjg!=""){
					strfwjg=strfwjg.substring(0, strfwjg.length()-1);
					fwjg=" and bdcdy.fwjg in("+strfwjg+")";
				}
				String hxjg="";
				if(strhxjg.length()>0 && strhxjg != ""){
					strhxjg=strhxjg.substring(0, strhxjg.length()-1);
					hxjg=" and bdcdy.hxjg in("+strhxjg+")";
				}
				buildtjtj.append(fwyt.toString()+fwlx.toString()+fwxz.toString()+hx.toString()+fwjg.toString()+hxjg.toString());
				
				//=======================判断分组类别=========================
				//登记时间
				if(fW_KSSJ.length() >0 && fW_KSSJ != "" && fW_KSSJ !=null &&
				  fW_JSSJ.length() >0 && fW_JSSJ != "" && fW_JSSJ !=null){
					builddjsj.append("where 1 = 1 and TO_CHAR(ql.djsj,'yyyy-mm-dd') between '"+fW_KSSJ+"' AND '"+fW_JSSJ+"'");
				}
				if (fw_FZLB.equals("房屋类型")) {
					constslsid = 18;
					constvalue = "dy.fwlx";
				}else if (fw_FZLB.equals("房屋用途")) {
					constslsid = 17;
					constvalue = "dy.fwyt";
				}else if (fw_FZLB.equals("房屋结构")) {
					constslsid = 46;
					constvalue = "dy.fwjg";				
				}else if (fw_FZLB.equals("房屋性质")) {
					constslsid = 19;
					constvalue = "dy.fwxz";
				}else if (fw_FZLB.equals("户型")) {
					constslsid = 15;
					constvalue = "dy.hx";
				}else if (fw_FZLB.equals("户型结构")) {
					constslsid = 16;
					constvalue = "dy.hxjg";
				}else if (fw_FZLB.equals("年度")) {
					ndjdy = "YYYY";
				}else if (fw_FZLB.equals("季度")) {
					ndjdy = "Q";
				}else if (fw_FZLB.equals("月份")) {
					ndjdy = "MM";
				}else {
					
				}
				if(fw_FZLB.equals("房屋类型") || fw_FZLB.equals("房屋用途") || fw_FZLB.equals("房屋结构") || fw_FZLB.equals("房屋性质") 
						|| fw_FZLB.equals("户型") || fw_FZLB.equals("户型结构")){
					buildtop.append(" max(bdcdy.FWLX_CON) as FZLB,");
					buildfzcons.append(" const.consttrans as FWLX_CON,");
					buildcontname.append(" left join BDCK.BDCS_const"+qhlink+" const on const.constslsid='"+constslsid+"' and const.constvalue = "+constvalue+"");
					buildfzlb.append("where bdcdy.BDCDYID is not null");
					buildnotnull.append(" GROUP BY bdcdy.BDCDYID");
				}
				if(fw_FZLB.equals("年度") || fw_FZLB.equals("季度") || fw_FZLB.equals("月份")){
					buildTimeNian.append(" to_char(slsj,'"+ndjdy+"') as FZLB,");
					buildGroupTime.append(" group by to_char(slsj,'YYYY'),to_char(slsj,'Q'),to_char(slsj,'MM') ");
					buildoOrderTime.append(" order by to_char(slsj,'YYYY'),to_char(slsj,'Q'),to_char(slsj,'MM')");
					buildfzlb.append(" where bdcdy.bdcdyid is not null");
				}
				
				if(builddjsj.length()>0){
		   	   		strtime=" and (ql.QLLX not in('99','23'))";
		   	   	}else{
		   	   		strtime=" where (ql.QLLX not in('99','23'))";
		   	   	}
				//=======================行政区划语句=========================
		 buildyt.append(" select ")
				.append(" '"+qhmc+"' as FZLB,")
				.append(" sum(t.HJ) AS HJ,")
				.append(" sum(t.DJMJ) as DJMJ,")
				.append(" sum(t.PJMJ) as PJMJ,")
				.append(" sum(t.JYZJ) as JYZJ,")
				.append(" 0 AS JYJG")
				.append(" from(")
				.append("  select ")
				.append("  '"+qhmc+"' as FZLB,")
				.append("  count(*) as HJ,")
				.append("  ROUND(SUM(bdcdy.SCJZMJ),2)as DJMJ,")
				.append("  ROUND(AVG(bdcdy.SCJZMJ),2)as PJMJ,")
				.append("  round(SUM(QDJG)/10000,2)AS JYZJ,")
				.append("  0 AS JYJG")
				.append(" from ")
				.append("  (select ")
				.append("  ql.djsj as slsj,const.consttrans as FWLX_CON, DJDY.DJDYID,DY.BDCDYID,DY.FWLX, FWYT,DY.HXJG,")
				.append("  DY.FWXZ,DY.FWJG,DY.HX,DY.GHYT,DY.FWCB,DY.JZMJ AS SCJZMJ,QL.QLID,QL.BDCQZH,QL.QLLX AS QLLX,QL.QDJG,QL.DJLX")
				.append("  from(")
				.append("  SELECT xmbh, BDCDYID,FWLX,FWXZ,FWJG,HX,HXJG,GHYT,FWCB,FWYT1 AS FWYT,SCJZMJ AS JZMJ,ZDBDCDYID   FROM BDCK.BDCS_H_XZ"+qhlink+"")
				.append("         UNION")
				.append("  SELECT xmbh, BDCDYID,FWLX,FWXZ,FWJG,HX,HXJG,GHYT,FWCB,FWYT1 AS FWYT,YCJZMJ AS JZMJ,ZDBDCDYID  FROM BDCK.BDCS_H_XZY"+qhlink+"")
				.append("  )DY")
				.append("  left join BDCK.BDCS_DJDY_GZ"+qhlink+" DJDY on dy.bdcdyid=djdy.bdcdyid ")
				.append("  left join BDCK.bdcs_ql_xz"+qhlink+" ql on ql.djdyid=djdy.djdyid")
				.append("  left join BDCK.BDCS_const"+qhlink+" const on const.constslsid='65' and const.constvalue = '"+qhdm+"'")
				.append("  "+builddjsj+" ")
				.append("  "+strtime.toString()+"")
				.append("   )bdcdy WHERE  QLLX IS NOT NULL "+buildtjtj+" GROUP BY bdcdy.BDCDYID")
				.append("  )t");

				//======================================================根据行政区划条件组织语句================================================
						if(fW_XZQH.length()>0 && fW_XZQH!=null && fW_XZQH!=""){
							System.out.println(fW_XZQH);
							String[] str = fW_XZQH.split(",");
							boolean qs = false;
							boolean yt = false;
							boolean gx = false;
							boolean yj = false;
							for(int i = 0; i < str.length; i++){
								if (str[i].equals("10000")) {
									qs=true;
								}else if (str[i].equals("360600")) {
									yt=true;
								}else if (str[i].equals("360681")) {
									gx=true;
								}else if (str[i].equals("360622")) {
									yj=true;
								}
							}
							if (qs==true) {
								buildxzqh.delete(0, buildxzqh.length());
								for(int qq=0;qq<=2;qq++){
									if(qq==0){
										String xzqyt=buildyt.toString();
										xzqyt=xzqyt.replaceAll("#1", "鹰潭");
										xzqyt=xzqyt.replaceAll("#2", "");
										xzqyt=xzqyt.replaceAll("#3", "360600");
										buildxzqh.append(xzqyt.toString()+union.toString());
									}if(qq==1){
										String xzqgx=buildyt.toString();
										xzqgx=xzqgx.replaceAll("#1", "贵溪");
										xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
										xzqgx=xzqgx.replaceAll("#3", "360681");
										buildxzqh.append(xzqgx.toString()+union.toString());
									}if(qq==2){
										String xzqyj=buildyt.toString();
										xzqyj=xzqyj.replaceAll("#1", "余江");
										xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
										xzqyj=xzqyj.replaceAll("#3", "360622");
										buildxzqh.append(xzqyj.toString());
									}
								}
							}
							if (qs==false) {
								if (yt == true) {
									String xzqyt=buildyt.toString();
									xzqyt=xzqyt.replaceAll("#1", "鹰潭");
									xzqyt=xzqyt.replaceAll("#2", "");
									xzqyt=xzqyt.replaceAll("#3", "360600");
									buildxzqh.append(xzqyt.toString());
								}
								if (gx == true) {
									String xzqgx=buildyt.toString();
									xzqgx=xzqgx.replaceAll("#1", "贵溪");
									xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
									xzqgx=xzqgx.replaceAll("#3", "360681");
									buildxzqh.append(xzqgx.toString());
								}
								if (yj == true) {
									String xzqyj=buildyt.toString();
									xzqyj=xzqyj.replaceAll("#1",  "余江");
									xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
									xzqyj=xzqyj.replaceAll("#3", "360622");
									buildxzqh.append(xzqyj.toString());
								}
								if (yt == true && gx==true) {
									buildxzqh.delete(0, buildxzqh.length());
									for(int i1=0;i1<=1;i1++){
										if (i1==0) {
											String xzqyt=buildyt.toString();
											xzqyt=xzqyt.replaceAll("#1", "鹰潭");
											xzqyt=xzqyt.replaceAll("#2", "");
											xzqyt=xzqyt.replaceAll("#3", "360600");
											buildxzqh.append(xzqyt.toString()+union.toString());
										}if (i1==1) {
											String xzqgx=buildyt.toString();
											xzqgx=xzqgx.replaceAll("#1", "贵溪");
											xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
											xzqgx=xzqgx.replaceAll("#3", "360681");
											buildxzqh.append(xzqgx.toString());
										}
									}
								}
								if (yj == true && yt == true) {
									buildxzqh.delete(0, buildxzqh.length());
									for(int ee =0;ee<=1;ee++){
										if (ee==0) {
											String xzqyt=buildyt.toString();
											xzqyt=xzqyt.replaceAll("#1", "鹰潭");
											xzqyt=xzqyt.replaceAll("#2", "");
											xzqyt=xzqyt.replaceAll("#3", "360600");
											buildxzqh.append(xzqyt.toString()+union.toString());
										}if (ee==1) {
											String xzqyj=buildyt.toString();
											xzqyj=xzqyj.replaceAll("#1", "余江");
											xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
											xzqyj=xzqyj.replaceAll("#3", "360622");
											buildxzqh.append(xzqyj.toString());
										}
									}
								}
								if (yj == true && gx == true) {
									buildxzqh.delete(0, buildxzqh.length());
									for(int hh =0;hh<=1;hh++){
										if (hh==0) {
											String xzqgx=buildyt.toString();
											xzqgx=xzqgx.replaceAll("#1", "贵溪");
											xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
											xzqgx=xzqgx.replaceAll("#3", "360681");
											buildxzqh.append(xzqgx.toString()+union.toString());
										}if (hh==1) {
											String xzqyj=buildyt.toString();
											xzqyj=xzqyj.replaceAll("#1", "余江");
											xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
											xzqyj=xzqyj.replaceAll("#3", "360622");
											buildxzqh.append(xzqyj.toString());
										}
									}
								}
								if (yt==true && gx==true && yj == true ) {
									buildxzqh.delete(0, buildxzqh.length());
									for(int ff =0;ff<=2;ff++){
										if(ff==0){
											String xzqyt=buildyt.toString();
											xzqyt=xzqyt.replaceAll("#1", "鹰潭");
											xzqyt=xzqyt.replaceAll("#2", "");
											xzqyt=xzqyt.replaceAll("#3", "360600");
											buildxzqh.append(xzqyt.toString()+union.toString());
										}if(ff==1){
											String xzqgx=buildyt.toString();
											xzqgx=xzqgx.replaceAll("#1", "贵溪");
											xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
											xzqgx=xzqgx.replaceAll("#3", "360681");
											buildxzqh.append(xzqgx.toString()+union.toString());
										}if(ff==2){
											String xzqyj=buildyt.toString();
											xzqyj=xzqyj.replaceAll("#1", "余江");
											xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
											xzqyj=xzqyj.replaceAll("#3", "360622");
											buildxzqh.append(xzqyj.toString());
										}
									}
								}
							}
						}else{
							buildxzqh.delete(0, buildxzqh.length());
							for(int ii=0;ii<=2;ii++){
								if(ii==0){
									String xzqyt=buildyt.toString();
									xzqyt=xzqyt.replaceAll("#1", "鹰潭");
									xzqyt=xzqyt.replaceAll("#2", "");
									xzqyt=xzqyt.replaceAll("#3", "360600");
									buildxzqh.append(xzqyt.toString()+union.toString());
								}if(ii==1){
									String xzqgx=buildyt.toString();
									xzqgx=xzqgx.replaceAll("#1", "贵溪");
									xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
									xzqgx=xzqgx.replaceAll("#3", "360681");
									buildxzqh.append(xzqgx.toString()+union.toString());
								}if(ii==2){
									String xzqyj=buildyt.toString();
									xzqyj=xzqyj.replaceAll("#1", "余江");
									xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
									xzqyj=xzqyj.replaceAll("#3", "360622");
									buildxzqh.append(xzqyj.toString());
								}
							}
						}
						
						//==================================地籍区、地籍子区统计条件=====================================
					   	   if(djq.length() >0 && djq !=null && djq != ""){
					   		   String[] value = djq.split(",");
					   		   for(int i =0; i<value.length;i++){
					   			if (value[i].length() == 9) {
					   				strdjqdm+="'"+value[i]+"',";
								}else{
									strdjzqdm+="'"+value[i]+"',";
								}
					   		  }
					   		if (strdjqdm.length()>0) {
					   			strdjqdm=strdjqdm.substring(0, strdjqdm.length()-1);
							}
					   		if (strdjzqdm.length()>0) {
					   			strdjzqdm=strdjzqdm.substring(0, strdjzqdm.length()-1);
							}
					   		if (strdjqdm!="") {
					   			builddjqdm.append("and bdcdy.DJQDM in("+strdjqdm+")");
							}
					   		if (strdjzqdm!="") {
					   			builddjzqdm.append("and bdcdy.DJZQDM in("+strdjzqdm+")");
							}
					   	 }
					   	   
				   buildhead.append("select ")
						   	.append(" t.FZLB,")
						    .append(" SUM(t.HJ) as HJ,")
						    .append(" ROUND(SUM(t.DJMJ),2)as DJMJ,")
						    .append(" ROUND(AVG(t.PJMJ),2)as PJMJ,")
						    .append(" SUM(t.JYZJ) AS JYZJ,")
						    .append(" 0 AS JYJG")
							.append(" from ( ");
					   build.append(" select ")
							.append(" "+buildtop+"")
							.append(" "+buildTimeNian+"")
							.append(" "+buildTimeJidu+"")
							.append(" "+buildTimeYueFen+"")
							.append(" count(*) as HJ,")
							.append(" ROUND(SUM(bdcdy.SCJZMJ),2)as DJMJ,")
							.append(" ROUND(AVG(bdcdy.SCJZMJ),2)as PJMJ,")
							.append(" round(SUM(QDJG)/10000,2)AS JYZJ,")
							.append(" 0 AS JYJG ")
							.append(" from (");
				   buildtime.append(" select ")
					   		.append(" ql.djsj as slsj,"+buildfzcons+" DJDY.DJDYID,DY.BDCDYID,DY.FWLX, FWYT,DY.HXJG,")
					   		.append(" DY.FWXZ,DY.FWJG,DY.HX,DY.GHYT,DY.FWCB,DY.JZMJ AS SCJZMJ,QL.QLID,QL.BDCQZH,QL.QLLX AS QLLX,QL.QDJG, ")
					   		.append(" QL.DJLX, ZD.DJQDM as DJQDM,ZD.DJZQDM as DJZQDM,ZD.DJQMC as DJQMC,ZD.DJZQMC as DJZQMC ")
					   		.append(" from(") 
					   		.append(" SELECT xmbh, BDCDYID,FWLX,FWXZ,FWJG,HX,HXJG,GHYT,FWCB,FWYT1 AS FWYT,SCJZMJ AS JZMJ,ZDBDCDYID  FROM BDCK.BDCS_H_XZ"+qhlink+"") 
					   		.append(" UNION") 
					   		.append(" SELECT xmbh, BDCDYID,FWLX,FWXZ,FWJG,HX,HXJG,GHYT,FWCB,FWYT1 AS FWYT,YCJZMJ AS JZMJ,ZDBDCDYID  FROM BDCK.BDCS_H_XZY"+qhlink+"") 
					   		.append(" ) DY") 
					   		.append(" left join BDCK.BDCS_DJDY_GZ"+qhlink+" DJDY on dy.bdcdyid=djdy.bdcdyid") 
					   		.append(" left join BDCK.bdcs_ql_xz"+qhlink+" ql on ql.djdyid=djdy.djdyid")
							.append(" left join BDCK.BDCS_SHYQZD_XZ"+qhlink+" ZD ON dy.ZDBDCDYID=ZD.BDCDYID")
							.append(" "+buildcontname+"")
							.append(" "+builddjsj+"")
				   			.append(" "+strtime.toString()+"");
			    buildtimeend.append(" )bdcdy "+buildfzlb+" "+builddjqdm+" "+builddjzqdm+" "+buildtjtj+" "+buildnotnull+"")
							.append(" "+buildGroupTime+"")
							.append(" "+buildoOrderTime+"");
					buildend.append(" )t where t.hj is not null GROUP BY t.FZLB");
				   	
					   		
					   	if(!fw_FZLB.equals("性质区划") && !fw_FZLB.equals("地籍区") && !fw_FZLB.equals("地籍子区")){
					   		if(fW_XZQH.length()>0 && fW_XZQH!=null && fW_XZQH!=""){
					   			if (fW_XZQH.equals("10000,360600,360681,360622")) {
					   				if(fw_FZLB.equals("年度")||fw_FZLB.equals("季度")||fw_FZLB.equals("月份")){
					   					for(int n1=0;n1<=2;n1++){
					   						if(n1==0){
					   							String xzqyt=buildtime.toString();
												xzqyt=xzqyt.replaceAll("#2", "");
												buildfzqs.append(buildhead+build.toString()+xzqyt.toString()+union);
					   						}
					   						if(n1==1){
					   							String xzqgx=buildtime.toString();
												xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
												buildfzqs.append(xzqgx.toString()+union);
					   						}
					   						if(n1==2){
					   							String xzqyj=buildtime.toString();
												xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
												buildfzqs.append(xzqyj.toString()+buildtimeend+buildend);
					   						}
					   					}
					   				}else{
					   					for(int q1=0;q1<=2;q1++){
											if(q1==0){
												String xzqyt=buildtime.toString();
												xzqyt=xzqyt.replaceAll("#2", "");
												buildfzqs.append(buildhead+build.toString()+xzqyt.toString()+buildtimeend+union);
											}
											if(q1==1){
												String xzqgx=buildtime.toString();
												xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
												buildfzqs.append(build.toString()+xzqgx.toString()+buildtimeend+union);
											}
											if(q1==2){
												String xzqyj=buildtime.toString();
												xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
												buildfzqs.append(build.toString()+xzqyj.toString()+buildtimeend+buildend);
											}
										}
					   				}
								}else if(fW_XZQH.equals("360600,360681")){
									if (fw_FZLB.equals("年度")||fw_FZLB.equals("季度")||fw_FZLB.equals("月份")) {
										for(int n2=0;n2<=1;n2++){
											if(n2==0){
												String xzqyt=buildtime.toString();
												xzqyt=xzqyt.replaceAll("#2", "");
												buildfzqs.append(buildhead+build.toString()+xzqyt.toString()+union);
											}
											if(n2==1){
												String xzqgx=buildtime.toString();
												xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
												buildfzqs.append(xzqgx.toString()+buildtimeend+buildend);
											}
										}
									}else{
										for(int q2=0;q2<=1;q2++){
											if(q2==0) {
												String xzqyt=buildtime.toString();
												xzqyt=xzqyt.replaceAll("#2", "");
												buildfzqs.append(buildhead+build.toString()+xzqyt.toString()+buildtimeend+union);
											}
											if(q2==1) {
												String xzqgx=buildtime.toString();
												xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
												buildfzqs.append(build.toString()+xzqgx.toString()+buildtimeend+buildend);
											}
										}
									}
								}else if(fW_XZQH.equals("360600,360622")){
									if(fw_FZLB.equals("年度")||fw_FZLB.equals("季度")||fw_FZLB.equals("月份")){
										for(int n3=0;n3<=1;n3++){
											if(n3==0){
												String xzqyt=buildtime.toString();
												xzqyt=xzqyt.replaceAll("#2", "");
												buildfzqs.append(buildhead+build.toString()+xzqyt.toString()+union);
											}
											if(n3==1){
												String xzqyj=buildtime.toString();
												xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
												buildfzqs.append(xzqyj.toString()+buildtimeend+buildend);
											}
										}
									}else{
										for(int q3=0;q3<=1;q3++){
											if(q3==0) {
												String xzqyt=buildtime.toString();
												xzqyt=xzqyt.replaceAll("#2", "");
												buildfzqs.append(buildhead+build.toString()+xzqyt.toString()+buildtimeend+union);
											}
											if(q3==1){
												String xzqyj=buildtime.toString();
												xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
												buildfzqs.append(build.toString()+xzqyj.toString()+buildtimeend+buildend);
											}
										}
									}
								}else if(fW_XZQH.equals("360681,360622")){
									if(fw_FZLB.equals("年度")||fw_FZLB.equals("季度")||fw_FZLB.equals("月份")){
										for(int n4=0;n4<=1;n4++){
											if(n4==0){
												String xzqgx=buildtime.toString();
												xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
												buildfzqs.append(buildhead+build.toString()+xzqgx.toString()+union);
											}
											if(n4==1){
												String xzqyj=buildtime.toString();
												xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
												buildfzqs.append(xzqyj.toString()+buildtimeend+buildend);
											}
										}
									}else{
										for(int q4=0;q4<=1;q4++){
											if(q4==0) {
												String xzqgx=buildtime.toString();
												xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
												buildfzqs.append(buildhead+build.toString()+xzqgx.toString()+buildtimeend+union);
											}
											if(q4==1){
												String xzqyj=buildtime.toString();
												xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
												buildfzqs.append(build.toString()+xzqyj.toString()+buildtimeend+buildend);
											}
										}
									}
								}else if(fW_XZQH.equals("360600")){
									String xzqyt=buildtime.toString();
									xzqyt=xzqyt.replaceAll("#2", "");
									buildfzqs.append(buildhead+build.toString()+xzqyt.toString()+buildtimeend+buildend);
								}else if(fW_XZQH.equals("360681")){
									String xzqgx=buildtime.toString();
									xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
									buildfzqs.append(buildhead+build.toString()+xzqgx.toString()+buildtimeend+buildend);
								}else if(fW_XZQH.equals("360622")){
									String xzqyj=buildtime.toString();
									xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
									buildfzqs.append(buildhead+build.toString()+xzqyj.toString()+buildtimeend+buildend);
								}
					   		}
					   	}
			   
					    if(fw_FZLB.equals("地籍区")){
				   		    djqby = "GROUP BY t.DJQDM";
				   		}else{
				   		    djqby = "GROUP BY t.DJZQDM";
				   		}	
			   builddjq.append("select ")
					   .append(" max(t.DJQDM) as DJQDM,")
					   .append(" max(t.DJZQDM) as DJZQDM,")
					   .append(" max(t.FZLB) as FZLB,")
					   .append(" sum(t.HJ) as HJ,")
					   .append(" ROUND(SUM(t.DJMJ),2)as DJMJ,")
					   .append(" ROUND(AVG(t.PJMJ),2)as PJMJ,")
					   .append(" SUM(t.JYZJ) AS JYZJ,")
					   .append(" 0 AS JYJG")
					   .append(" from (")
					   .append("  select")
					   .append("  max(bdcdy.DJQDM) as DJQDM,")
					   .append("  max(bdcdy.DJZQDM) as DJZQDM,")
					   .append("  max(bdcdy.DJQMC) as FZLB,  ")
					   .append("  count(*) as HJ,")
					   .append("  ROUND(SUM(bdcdy.SCJZMJ),2)as DJMJ, ")
					   .append("  ROUND(AVG(bdcdy.SCJZMJ),2)as PJMJ, ")
					   .append("  round(SUM(QDJG)/10000,2)AS JYZJ, ")
					   .append("  0 AS JYJG ")
					   .append(" from (")
					   .append("  select ql.djsj as slsj,DJDY.DJDYID,DY.BDCDYID,DY.FWLX, FWYT,DY.HXJG,DY.FWXZ,DY.FWJG,DY.HX,DY.GHYT,")
					   .append("  DY.FWCB,DY.JZMJ AS SCJZMJ,DY.TNJZMJ AS SCTNJZMJ,DY.FTJZMJ AS SCFTJZMJ, DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC,QL.QLID,")
					   .append("  QL.BDCQZH,QL.QLLX AS QLLX,QL.QDJG, QL.DJLX,")
					   .append("  ZD.DJQDM as DJQDM,ZD.DJZQDM as DJZQDM,ZD.DJQMC as DJQMC,ZD.DJZQMC as DJZQMC")
					   .append(" from (")
					   .append("  SELECT xmbh, BDCDYID,FWLX,FWXZ,FWJG,HX,HXJG,GHYT,FWCB,FWYT1 AS FWYT,SCJZMJ AS JZMJ,SCTNJZMJ AS TNJZMJ, SCFTJZMJ AS FTJZMJ ,'031' AS BDCDYLX,")
					   .append("  '现房' AS BDCDYLXMC,ZDBDCDYID  FROM BDCK.BDCS_H_XZ ")
					   .append(" UNION ALL ")
					   .append("  SELECT xmbh, BDCDYID,FWLX,FWXZ,FWJG,HX,HXJG,GHYT,FWCB,FWYT1 AS FWYT,YCJZMJ AS JZMJ,YCTNJZMJ AS TNJZMJ, YCFTJZMJ AS FTJZMJ,'032' AS BDCDYLX,")
					   .append("  '期房' AS BDCDYLXMC,ZDBDCDYID  FROM BDCK.BDCS_H_XZY ")
					   .append("  ) DY ")
					   .append("  left join BDCK.BDCS_DJDY_GZ DJDY on dy.bdcdyid=djdy.bdcdyid")
					   .append("  left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid")
					   .append("  left join BDCK.BDCS_SHYQZD_XZ ZD ON dy.ZDBDCDYID=ZD.BDCDYID")
					   .append("  "+builddjsj+"")
					   .append("  "+strtime.toString()+"")
					   .append("  )bdcdy")
					   .append("  where bdcdy.BDCDYID is not null "+builddjqdm+" "+builddjzqdm+" "+buildtjtj+" GROUP BY bdcdy.BDCDYID")
			   		   .append("  )t "+djqby.toString()+" ORDER BY FZLB DESC");
			   
				   if (fw_FZLB.equals("行政区划")) {
					   maps = baseCommonDao.getDataListByFullSql(buildxzqh.toString());
				   	}else if(fw_FZLB.equals("地籍区") || fw_FZLB.equals("地籍子区")){
				   			maps = baseCommonDao.getDataListByFullSql(builddjq.toString());
				   		}else{
				   			if(fW_XZQH.length()>0 && fW_XZQH!=null && fW_XZQH!=""){
				   				maps = baseCommonDao.getDataListByFullSql(buildfzqs.toString());
				   			}else {
				   				String xzqyt=buildtime.toString();
								xzqyt=xzqyt.replaceAll("#2", "");
				   				maps = baseCommonDao.getDataListByFullSql(buildhead+build.toString()+xzqyt+buildtimeend+buildend);
							}
				   		}
			   		m.setRows(maps);
					m.setTotal(maps.size());
				}catch(Exception e){
					m=null;
			}
			return m;
		}
		
		
		@Override
		public Message Getjyjgttd(HttpServletRequest request) {
				Message m = new Message();
			try {
				String fZLB = new String(request.getParameter("push").getBytes("iso8859-1"), "utf-8");
				String td_xzqh = new String(request.getParameter("TD-XZQH").getBytes("iso8859-1"), "utf-8");
				String td_qllx = new String(request.getParameter("TD-QLLX").getBytes("iso8859-1"), "utf-8");
				String td_qlxz = new String(request.getParameter("TD-QLXZ").getBytes("iso8859-1"), "utf-8");
				String td_qlsdfs = new String(request.getParameter("TD-QLSDFS").getBytes("iso8859-1"), "utf-8");
				String td_tddj = new String(request.getParameter("TD-TDDJ").getBytes("iso8859-1"), "utf-8");
				String td_tdyt = new String(request.getParameter("TD-TDYT").getBytes("iso8859-1"), "utf-8");
				String td_kssj = new String(request.getParameter("TD-KSSJ").getBytes("iso8859-1"), "utf-8");
				String td_jssj = new String(request.getParameter("TD-JSSJ").getBytes("iso8859-1"), "utf-8");
				String djq = new String(request.getParameter("TD-DJQ").getBytes("iso8859-1"), "utf-8");
				List<Map> maps = null;
				StringBuilder build = new StringBuilder();
				StringBuilder buildtjtj = new StringBuilder();
				StringBuilder buildhead = new StringBuilder();
				StringBuilder buildend = new StringBuilder();
				StringBuilder buildtime  = new StringBuilder();
				StringBuilder buildtimeend  = new StringBuilder();
				StringBuilder buildfzqs = new StringBuilder();
				//分组类别
				StringBuilder buildtop = new StringBuilder();
				StringBuilder buildfzcons = new StringBuilder();
				StringBuilder buildcontname = new StringBuilder();
				StringBuilder buildnotnull = new StringBuilder();
				StringBuilder buildfzlb = new StringBuilder();
				StringBuilder builddjq = new StringBuilder();
				StringBuilder builddjqdm = new StringBuilder();
				StringBuilder builddjzqdm = new StringBuilder();
				//行政区划
				StringBuilder buildyt = new StringBuilder();
				StringBuilder buildgx = new StringBuilder();
				StringBuilder buildyj = new StringBuilder();
				StringBuilder union = new StringBuilder();
				union.append(" UNION ALL");
				//时间条件
				StringBuilder buildTimeNian = new StringBuilder();
				StringBuilder buildTimeJidu = new StringBuilder();
				StringBuilder buildTimeYueFen = new StringBuilder();
				StringBuilder buildGroupTime = new StringBuilder();
				StringBuilder buildoOrderTime = new StringBuilder();
				//统计条件
				StringBuilder buildxzqh = new StringBuilder();
				StringBuilder builddjsj = new StringBuilder();
				String qhlink = "#2";
				String strtime = "";
				String strdjqdm="";
				String strdjzqdm="";
				String djqby = "";
				String strqllx="";
				String strqlxz="";
				String strqlsdfs="";
				String strtddj="";
				String strtdyt="";
				//=======================组织统计条件=========================
				String[] strtj=new String[5];
				strtj[0]=td_qllx;
				strtj[1]=td_qlxz;
				strtj[2]=td_qlsdfs;
				strtj[3]=td_tddj;
				strtj[4]=td_tdyt;
				for(int z=0;z<strtj.length;z++){
					String s1 = strtj[z];
					if (s1 !="" && s1.length()>0) {
						String[] value = s1.split(",");
						for(int d =0; d<value.length;d++){
								if(z==0){
									if (!value[d].equals("10000")) {
										strqllx+="'"+value[d]+"',";
									}else{
										strqllx="";
										break;
									}
								}else if(z==1){
									if (!value[d].equals("10000")){
										strqlxz+="'"+value[d]+"',";
									}else{
										strqlxz="";
										break;
									}
								}else if(z==2){
									if (!value[d].equals("10000")){
										strqlsdfs+="'"+value[d]+"',";
									}else{
										strqlsdfs="";
										break;
									}
								}else if(z==3){
									if(!value[d].equals("10000")){
										strtddj+="'"+value[d]+"',";
									}else{
										strtddj="";
										break;
									}
								}else if(z==4){
									if(!value[d].equals("10000")){
										strtdyt+="'"+value[d]+"',";
									}else{
										strtdyt="";
										break;
									}
								}
							}	
						}
					}
				String qllx="";
				if(strqllx.length()>0 && strqllx !=""){
					strqllx=strqllx.substring(0, strqllx.length()-1);
					qllx=" and bdcdy.qllx in("+strqllx.toString()+")";
				}
				String qlxz="";
				if(strqlxz.length()>0 && strqlxz !=""){
					strqlxz=strqlxz.substring(0, strqlxz.length()-1);
					qlxz=" and bdcdy.qlxz in("+strqlxz+")";
				}
				String qlsdfs ="";
				if(strqlsdfs.length()>0 && strqlsdfs != ""){
					strqlsdfs=strqlsdfs.substring(0, strqlsdfs.length()-1);
					qlsdfs=" and bdcdy.qlsdfs in("+strqlsdfs+")";
				}
				String tddj ="";
				if(strtddj.length()>0 && strtddj !=""){
					strtddj=strtddj.substring(0, strtddj.length()-1);
					tddj=" and bdcdy.tddj in("+strtddj+")";
				}
				String tdyt="";
				if(strtdyt.length()>0 && strtdyt!=""){
					strtdyt=strtdyt.substring(0, strtdyt.length()-1);
					tdyt=" and bdcdy.tdyt in("+strtdyt+")";
				}
				buildtjtj.append(qllx.toString()+qlxz.toString()+qlsdfs.toString()+tddj.toString()+tdyt.toString());
				
				//登记时间
				if(td_kssj.length() >0 && td_kssj != "" && td_kssj !=null &&
				  td_jssj.length() >0 && td_jssj != "" && td_jssj !=null){
					builddjsj.append("WHERE 1 = 1 and TO_CHAR(ql.djsj,'yyyy-mm-dd') between '"+td_kssj+"' AND '"+td_jssj+"'");
				}
				
				if (fZLB.equals("行政区划")) {
					
				}else if (fZLB.equals("权利类型")) {
					buildtop.append(" max(bdcdy.FWLX_CON) as FZLB,");
					buildfzcons.append(" const.consttrans as FWLX_CON,");
					buildcontname.append("left join BDCK.BDCS_const const on const.constslsid='8' and const.constvalue = ql.qllx");
					buildfzlb.append("where bdcdy.bdcdyid is not null");
					buildnotnull.append("GROUP BY bdcdy.BDCDYID");
					
				}else if (fZLB.equals("权利设定方式")) {
					buildtop.append(" max(bdcdy.FWLX_CON) as FZLB,");
					buildfzcons.append(" const.consttrans as FWLX_CON,");
					buildcontname.append("left join BDCK.BDCS_const const on const.constslsid='10' and const.constvalue = dy.qlsdfs");
					buildfzlb.append("where bdcdy.bdcdyid is not null");
					buildnotnull.append("GROUP BY bdcdy.BDCDYID");
					
				}else if (fZLB.equals("权利性质")) {
					buildtop.append(" max(bdcdy.FWLX_CON) as FZLB,");
					buildfzcons.append(" const.consttrans as FWLX_CON,");
					buildcontname.append("left join BDCK.BDCS_const const on const.constslsid='9' and const.constvalue = dy.qlxz");
					buildfzlb.append("where bdcdy.bdcdyid is not null");
					buildnotnull.append("GROUP BY bdcdy.BDCDYID");
					
				}else if (fZLB.equals("土地等级")) {
					buildtop.append(" max(bdcdy.FWLX_CON) as FZLB,");
					buildfzcons.append(" const.consttrans as FWLX_CON,");
					buildcontname.append("left join BDCK.BDCS_const const on const.constslsid='50' and const.constvalue = tdyt.tddj");
					buildfzlb.append("where bdcdy.bdcdyid is not null");
					buildnotnull.append("GROUP BY bdcdy.BDCDYID");
					
				}else if (fZLB.equals("用途")) {
					buildtop.append(" max(bdcdy.FWLX_CON) as FZLB,");
					buildfzcons.append(" const.consttrans as FWLX_CON,");
					buildcontname.append("left join BDCK.BDCS_const const on const.constslsid='54' and const.constvalue = tdyt.tdyt");
					buildfzlb.append("where bdcdy.bdcdyid is not null");
					buildnotnull.append("GROUP BY bdcdy.BDCDYID");
					
				}else if (fZLB.equals("年度")) {
					buildTimeNian.append("to_char(slsj,'YYYY') as FZLB,");
					buildGroupTime.append("group by to_char(slsj,'YYYY'),to_char(slsj,'Q'),to_char(slsj,'MM') ");
					buildoOrderTime.append("order by to_char(slsj,'YYYY'),to_char(slsj,'Q'),to_char(slsj,'MM')");
					buildfzlb.append("where bdcdy.bdcdyid is not null");
				}else if (fZLB.equals("季度")) {
					buildTimeJidu.append("to_char(slsj,'Q') as FZLB,");
					buildGroupTime.append("group by to_char(slsj,'YYYY'),to_char(slsj,'Q'),to_char(slsj,'MM') ");
					buildoOrderTime.append("order by to_char(slsj,'YYYY'),to_char(slsj,'Q'),to_char(slsj,'MM')");
					buildfzlb.append("where bdcdy.bdcdyid is not null");
				}else if (fZLB.equals("月份")) {
					buildTimeYueFen.append("to_char(slsj,'MM') as FZLB,");
					buildGroupTime.append("group by to_char(slsj,'YYYY'),to_char(slsj,'Q'),to_char(slsj,'MM') ");
					buildoOrderTime.append("order by to_char(slsj,'YYYY'),to_char(slsj,'Q'),to_char(slsj,'MM')");
					buildfzlb.append("where bdcdy.bdcdyid is not null");
				}else {
					
				}
				
				if(builddjsj.length()>0){
		   	   		strtime=" and (ql.QLLX not in('99','23'))";
		   	   	}else{
		   	   		strtime=" where (ql.QLLX not in('99','23'))";
		   	   	}
				
				//===============================================行政区划语句================================================
				buildyt.append(" select ")
					.append(" '鹰潭' as FZLB,")
					.append(" sum(t.HJ) AS HJ,")
					.append(" sum(t.DJMJ) as DJMJ,")
					.append(" sum(t.PJMJ) as PJMJ,")
					.append(" sum(t.JYZJ) as JYZJ,")
					.append(" sum(t.JYJG) as JYJG")
					.append(" from(")
					.append(" select")
					.append(" '鹰潭' as FZLB,")
					.append(" count(*) as hj,")
					.append(" ROUND(SUM(bdcdy.ZDMJ),2)as DJMJ,")
					.append(" ROUND(AVG(bdcdy.ZDMJ),2)as PJMJ,")
					.append(" ROUND(SUM(QDJG)/10000,2) as JYZJ,")
					.append(" ROUND(AVG(QDJG)/10000,2) as JYJG")
					.append(" from ")
					.append(" (select")
					.append(" QL.DJSJ as slsj,DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,")
					.append(" DY.BDCDYLXMC AS BDCDYLXMC,QL.QLID,QL.BDCQZH,QL.QLLX AS QLLX,QL.DJLX,DY.BDCDYLX AS BDCDYLX,")
					.append(" DY.ZDMJ AS ZDMJ,QL.QDJG,DY.QLSDFS,DY.QLXZ,TDYT.TDDJ AS TDDJ,TDYT.TDYT AS TDYT")
					.append(" from (")
					.append(" SELECT ")
					.append("   xmbh,ZL,ZDDM,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC,ZDMJ,QLSDFS,QLXZ")
					.append(" FROM BDCK.BDCS_SYQZD_XZ")
					.append(" UNION ALL")
					.append(" SELECT ")
					.append("   xmbh,ZL,ZDDM,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC ,ZDMJ,QLSDFS,QLXZ")
					.append(" FROM BDCK.BDCS_SHYQZD_XZ) DY")
					.append(" left join BDCK.bdcs_tdyt_xz tdyt on dy.bdcdyid=tdyt.bdcdyid")
					.append(" left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid")
					.append(" left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid")
					.append(" left join BDCK.BDCS_const const on const.constslsid='65' and const.constvalue = '360600'")
					.append(" "+builddjsj+"")
					.append(" "+strtime.toString()+"")
					.append(" )bdcdy WHERE bdcdy.BDCDYID IS NOT NULL "+buildtjtj+" GROUP BY bdcdy.BDCDYID")
					.append(")t");
					
				buildgx.append(" select ")
					.append(" '贵溪' as FZLB,")
					.append(" sum(t.HJ) AS HJ,")
					.append(" sum(t.DJMJ) as DJMJ,")
					.append(" sum(t.PJMJ) as PJMJ,")
					.append(" sum(t.JYZJ) as JYZJ,")
					.append(" sum(t.JYJG) as JYJG")
					.append(" from(")
					.append(" select")
					.append("       '贵溪' as FZLB,")
					.append("       count(*) as hj,")
					.append("       ROUND(SUM(bdcdy.ZDMJ),2)as DJMJ,")
					.append("       ROUND(AVG(bdcdy.ZDMJ),2)as PJMJ,")
					.append("       ROUND(SUM(QDJG)/10000,2) as JYZJ,")
					.append("       ROUND(AVG(QDJG)/10000,2) as JYJG")
					.append(" from ")
					.append(" (select")
					.append("      QL.DJSJ as slsj,DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,")
					.append("      DY.BDCDYLXMC AS BDCDYLXMC,QL.QLID,QL.BDCQZH,QL.QLLX AS QLLX,QL.DJLX,DY.BDCDYLX AS BDCDYLX,")
					.append("      DY.ZDMJ AS ZDMJ,QL.QDJG,DY.QLSDFS,DY.QLXZ,TDYT.TDDJ AS TDDJ,TDYT.TDYT AS TDYT")
					.append(" from (")
					.append("   SELECT ")
					.append("      xmbh,ZL,ZDDM,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC,ZDMJ,QLSDFS,QLXZ")
					.append("   FROM BDCK.BDCS_SYQZD_XZ@TO_ORCL6_BDCK")
					.append("   UNION ALL")
					.append("   SELECT ")
					.append("      xmbh,ZL,ZDDM,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC ,ZDMJ,QLSDFS,QLXZ")
					.append("   FROM BDCK.BDCS_SHYQZD_XZ@TO_ORCL6_BDCK")
					.append("      ) DY")
					.append("      left join BDCK.bdcs_tdyt_xz@TO_ORCL6_BDCK tdyt on dy.bdcdyid=tdyt.bdcdyid")
					.append("      left join BDCK.BDCS_DJDY_XZ@TO_ORCL6_BDCK DJDY on dy.bdcdyid=djdy.bdcdyid")
					.append("      left join BDCK.bdcs_ql_xz@TO_ORCL6_BDCK ql on ql.djdyid=djdy.djdyid")
					.append("      left join BDCK.BDCS_const const on const.constslsid='65' and const.constvalue = '360681'")
					.append("      "+builddjsj+"")
					.append(" 	   "+strtime.toString()+"")
					.append(" )bdcdy WHERE bdcdy.BDCDYID IS NOT NULL "+buildtjtj+" GROUP BY bdcdy.BDCDYID")
					.append(")t");
					
				buildyj.append(" select ")
					.append(" '余江' as FZLB,")
					.append(" sum(t.HJ) AS HJ,")
					.append(" sum(t.DJMJ) as DJMJ,")
					.append(" sum(t.PJMJ) as PJMJ,")
					.append(" sum(t.JYZJ) as JYZJ,")
					.append(" sum(t.JYJG) as JYJG")
					.append(" from(")
					.append(" select")
			        .append("       '余江' as FZLB,")
					.append("       count(*) as hj,")
					.append("       ROUND(SUM(bdcdy.ZDMJ),2)as DJMJ,")
					.append("       ROUND(AVG(bdcdy.ZDMJ),2)as PJMJ,")
					.append("       ROUND(SUM(QDJG)/10000,2) as JYZJ,")
					.append("       ROUND(AVG(QDJG)/10000,2) as JYJG")
					.append(" from ")
					.append(" (select")
					.append("      QL.DJSJ as slsj,DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,")
					.append("      DY.BDCDYLXMC AS BDCDYLXMC,QL.QLID,QL.BDCQZH,QL.QLLX AS QLLX,QL.DJLX,DY.BDCDYLX AS BDCDYLX,")
					.append("      DY.ZDMJ AS ZDMJ,QL.QDJG,DY.QLSDFS,DY.QLXZ,TDYT.TDDJ AS TDDJ,TDYT.TDYT AS TDYT")
					.append(" from (")
					.append("   SELECT ")
					.append("      xmbh,ZL,ZDDM,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC,ZDMJ,QLSDFS,QLXZ")
					.append("   FROM BDCK.BDCS_SYQZD_XZ@TO_ORCL7_BDCK")
					.append("   UNION ALL")
					.append("   SELECT ")
					.append("      xmbh,ZL,ZDDM,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC ,ZDMJ,QLSDFS,QLXZ")
					.append("   FROM BDCK.BDCS_SHYQZD_XZ@TO_ORCL7_BDCK")
					.append("      ) DY")
					.append("      left join BDCK.bdcs_tdyt_xz@TO_ORCL7_BDCK tdyt on dy.bdcdyid=tdyt.bdcdyid")
					.append("      left join BDCK.BDCS_DJDY_XZ@TO_ORCL7_BDCK DJDY on dy.bdcdyid=djdy.bdcdyid")
					.append("      left join BDCK.bdcs_ql_xz@TO_ORCL7_BDCK ql on ql.djdyid=djdy.djdyid")
					.append("      left join BDCK.BDCS_const const on const.constslsid='65' and const.constvalue = '360622'")
					.append("      "+builddjsj+"")
					.append(" 	   "+strtime.toString()+"")
					.append(" )bdcdy WHERE bdcdy.BDCDYID IS NOT NULL "+buildtjtj+" GROUP BY bdcdy.BDCDYID")
					.append(")t");
		   //==============================================组织行政区划条件==============================================
				if(td_xzqh.length()>0 && td_xzqh!=null && td_xzqh!=""){
					String[] str = td_xzqh.split(",");
					boolean qs = false;
					boolean yt = false;
					boolean gx = false;
					boolean yj = false;
					for(int i = 0; i < str.length; i++){
						if (str[i].equals("10000")) {
							qs=true;
						}else if (str[i].equals("360600")) {
							yt=true;
						}else if (str[i].equals("360681")) {
							gx=true;
						}else if (str[i].equals("360622")) {
							yj=true;
						}
					}
					if (qs==true) {
						buildxzqh.append(buildyt.toString()+union.toString()+buildgx.toString()+union.toString()+buildyj.toString());
					}
					if (qs==false) {
						if (yt == true) {
							buildxzqh.append(buildyt.toString());
						}
						if (gx == true) {
							buildxzqh.append(buildgx.toString());
						}
						if (yj == true) {
							buildxzqh.append(buildyj.toString());
						}
						if (yt==true && gx==true) {
							buildxzqh.delete(0, buildxzqh.length());
							buildxzqh.append(buildyt.toString()+union.toString()+buildgx.toString());
						}
						if (yt==true && yj == true) {
							buildxzqh.delete(0, buildxzqh.length());
							buildxzqh.append(buildyt.toString()+union.toString()+buildyj.toString());
						}
						if (gx==true && yj == true) {
							buildxzqh.delete(0, buildxzqh.length());
							buildxzqh.append(buildgx.toString()+union.toString()+buildyj.toString());
						}
						if (yt==true && gx==true && yj == true ) {
							buildxzqh.append(buildyt.toString()+union.toString()+buildgx.toString()+union.toString()+buildyj.toString());
						}
					}
				}else{
					buildxzqh.append(buildyt.toString()+union.toString()+buildgx.toString()+union.toString()+buildyj.toString());
				}
				
				//======================================================组织地籍区条件==========================================
			   	   if(djq.length() >0 && djq !=null && djq != ""){
			   		   String[] value = djq.split(",");
			   		   for(int i =0; i<value.length;i++){
			   			if (value[i].length() == 9) {
			   				strdjqdm+="'"+value[i]+"',";
						}else{
							strdjzqdm+="'"+value[i]+"',";
						}
			   		  }
			   		if (strdjqdm.length()>0) {
			   			strdjqdm=strdjqdm.substring(0, strdjqdm.length()-1);
					}
			   		if (strdjzqdm.length()>0) {
			   			strdjzqdm=strdjzqdm.substring(0, strdjzqdm.length()-1);
					}
			   		if (strdjqdm!="") {
			   			builddjqdm.append("and bdcdy.DJQDM in("+strdjqdm+")");
					}
			   		if (strdjzqdm!="") {
			   			builddjzqdm.append("and bdcdy.DJZQDM in("+strdjzqdm+")");
					}
			   	 }
						
			   	   	
			   	buildhead.append("select ")
				   	.append(" t.FZLB,")
				    .append(" sum(t.hj) as HJ,")
				    .append(" ROUND(SUM(t.DJMJ),2)as DJMJ,")
				    .append(" ROUND(AVG(t.PJMJ),2)as PJMJ,")
				    .append(" SUM(t.JYZJ) AS JYZJ,")
				    .append(" SUM(t.JYJG) AS JYJG")
					.append(" from ( ");
			   build.append(" select ")
					.append(" "+buildtop+"")
					.append(" "+buildTimeNian+"")
					.append(" "+buildTimeJidu+"")
					.append(" "+buildTimeYueFen+"")
					.append(" count(*) as HJ,")
					.append(" ROUND(SUM(bdcdy.ZDMJ),2)as DJMJ,")
					.append(" ROUND(AVG(bdcdy.ZDMJ),2)as PJMJ,")
					.append(" ROUND(SUM(QDJG)/10000,2)AS JYZJ,")
					.append(" round(AVG(QDJG)/10000,2)AS JYJG")
					.append(" from (");
		   buildtime.append(" select ")
			   		.append(" ql.djsj as slsj,"+buildfzcons+"DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,")
			   		.append(" DY.BDCDYLXMC AS BDCDYLXMC,QL.QLID,QL.BDCQZH,QL.QLLX AS QLLX,QL.DJLX,DY.BDCDYLX AS BDCDYLX,")
			   		.append(" DY.ZDMJ AS ZDMJ,QL.QDJG,DY.QLSDFS,DY.QLXZ,TDYT.TDDJ AS TDDJ,TDYT.TDYT AS TDYT,") 
			   		.append(" DY.DJQDM,DY.DJQMC,DY.DJZQDM,DY.DJZQMC")
			   		.append(" from (") 
			   		.append(" SELECT xmbh,ZL,ZDDM,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC,ZDMJ,QLSDFS,QLXZ,DJQDM,DJZQDM,DJQMC,DJZQMC") 
			   		.append(" FROM BDCK.BDCS_SYQZD_XZ"+qhlink+"")
			   		.append(" UNION ALL") 
			   		.append(" SELECT xmbh,ZL,ZDDM,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC ,ZDMJ,QLSDFS,QLXZ,DJQDM,DJZQDM,DJQMC,DJZQMC") 
			   		.append(" FROM BDCK.BDCS_SHYQZD_XZ"+qhlink+"") 
			   		.append(" ) DY") 
			   		.append(" left join BDCK.bdcs_tdyt_xz"+qhlink+" tdyt on dy.bdcdyid=tdyt.bdcdyid") 
			   		.append(" left join BDCK.bdcs_DJDY_XZ"+qhlink+" DJDY on dy.bdcdyid=djdy.bdcdyid")
			   		.append(" left join BDCK.bdcs_ql_xz"+qhlink+" ql on ql.djdyid=djdy.djdyid")
					.append(" "+buildcontname+"")
					.append(" "+builddjsj+"")
					.append(" "+strtime.toString()+"");
	    buildtimeend.append(" ) bdcdy "+buildfzlb+" "+builddjqdm+" "+builddjzqdm+" "+buildtjtj+" "+buildnotnull+"")
					.append(" "+buildGroupTime+"")
					.append(" "+buildoOrderTime+"");
			buildend.append(" )t  GROUP BY t.FZLB");//where t.FZLB is not null
			
			if(!fZLB.equals("性质区划") && !fZLB.equals("地籍区") && !fZLB.equals("地籍子区")){
		   		if(td_xzqh.length()>0 && td_xzqh!=null && td_xzqh!=""){
		   			if (td_xzqh.equals("10000,360600,360681,360622")) {
		   				if(fZLB.equals("年度")||fZLB.equals("季度")||fZLB.equals("月份")){
		   					for(int n1=0;n1<=2;n1++){
		   						if(n1==0){
		   							String xzqyt=buildtime.toString();
									xzqyt=xzqyt.replaceAll("#2", "");
									buildfzqs.append(buildhead+build.toString()+xzqyt.toString()+union);
		   						}
		   						if(n1==1){
		   							String xzqgx=buildtime.toString();
									xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
									buildfzqs.append(xzqgx.toString()+union);
		   						}
		   						if(n1==2){
		   							String xzqyj=buildtime.toString();
									xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
									buildfzqs.append(xzqyj.toString()+buildtimeend+buildend);
		   						}
		   					}
		   				}else{
		   					for(int q1=0;q1<=2;q1++){
								if(q1==0){
									String xzqyt=buildtime.toString();
									xzqyt=xzqyt.replaceAll("#2", "");
									buildfzqs.append(buildhead+build.toString()+xzqyt.toString()+buildtimeend+union);
								}
								if(q1==1){
									String xzqgx=buildtime.toString();
									xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
									buildfzqs.append(build.toString()+xzqgx.toString()+buildtimeend+union);
								}
								if(q1==2){
									String xzqyj=buildtime.toString();
									xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
									buildfzqs.append(build.toString()+xzqyj.toString()+buildtimeend+buildend);
								}
							}
		   				}
					}else if(td_xzqh.equals("360600,360681")){
						if (fZLB.equals("年度")||fZLB.equals("季度")||fZLB.equals("月份")) {
							for(int n2=0;n2<=1;n2++){
								if(n2==0){
									String xzqyt=buildtime.toString();
									xzqyt=xzqyt.replaceAll("#2", "");
									buildfzqs.append(buildhead+build.toString()+xzqyt.toString()+union);
								}
								if(n2==1){
									String xzqgx=buildtime.toString();
									xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
									buildfzqs.append(xzqgx.toString()+buildtimeend+buildend);
								}
							}
						}else{
							for(int q2=0;q2<=1;q2++){
								if(q2==0) {
									String xzqyt=buildtime.toString();
									xzqyt=xzqyt.replaceAll("#2", "");
									buildfzqs.append(buildhead+build.toString()+xzqyt.toString()+buildtimeend+union);
								}
								if(q2==1) {
									String xzqgx=buildtime.toString();
									xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
									buildfzqs.append(build.toString()+xzqgx.toString()+buildtimeend+buildend);
								}
							}
						}
					}else if(td_xzqh.equals("360600,360622")){
						if(fZLB.equals("年度")||fZLB.equals("季度")||fZLB.equals("月份")){
							for(int n3=0;n3<=1;n3++){
								if(n3==0){
									String xzqyt=buildtime.toString();
									xzqyt=xzqyt.replaceAll("#2", "");
									buildfzqs.append(buildhead+build.toString()+xzqyt.toString()+union);
								}
								if(n3==1){
									String xzqyj=buildtime.toString();
									xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
									buildfzqs.append(xzqyj.toString()+buildtimeend+buildend);
								}
							}
						}else{
							for(int q3=0;q3<=1;q3++){
								if(q3==0) {
									String xzqyt=buildtime.toString();
									xzqyt=xzqyt.replaceAll("#2", "");
									buildfzqs.append(buildhead+build.toString()+xzqyt.toString()+buildtimeend+union);
								}
								if(q3==1){
									String xzqyj=buildtime.toString();
									xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
									buildfzqs.append(build.toString()+xzqyj.toString()+buildtimeend+buildend);
								}
							}
						}
					}else if(td_xzqh.equals("360681,360622")){
						if(fZLB.equals("年度")||fZLB.equals("季度")||fZLB.equals("月份")){
							for(int n4=0;n4<=1;n4++){
								if(n4==0){
									String xzqgx=buildtime.toString();
									xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
									buildfzqs.append(buildhead+build.toString()+xzqgx.toString()+union);
								}
								if(n4==1){
									String xzqyj=buildtime.toString();
									xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
									buildfzqs.append(xzqyj.toString()+buildtimeend+buildend);
								}
							}
						}else{
							for(int q4=0;q4<=1;q4++){
								if(q4==0) {
									String xzqgx=buildtime.toString();
									xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
									buildfzqs.append(buildhead+build.toString()+xzqgx.toString()+buildtimeend+union);
								}
								if(q4==1){
									String xzqyj=buildtime.toString();
									xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
									buildfzqs.append(build.toString()+xzqyj.toString()+buildtimeend+buildend);
								}
							}
						}
					}else if(td_xzqh.equals("360600")){
						String xzqyt=buildtime.toString();
						xzqyt=xzqyt.replaceAll("#2", "");
						buildfzqs.append(buildhead+build.toString()+xzqyt.toString()+buildtimeend+buildend);
					}else if(td_xzqh.equals("360681")){
						String xzqgx=buildtime.toString();
						xzqgx=xzqgx.replaceAll("#2", "@TO_ORCL6_BDCK");
						buildfzqs.append(buildhead+build.toString()+xzqgx.toString()+buildtimeend+buildend);
					}else if(td_xzqh.equals("360622")){
						String xzqyj=buildtime.toString();
						xzqyj=xzqyj.replaceAll("#2", "@TO_ORCL7_BDCK");
						buildfzqs.append(buildhead+build.toString()+xzqyj.toString()+buildtimeend+buildend);
					}
		   		}
		   	}
					
				   	if(fZLB.equals("地籍区")){
			   		    djqby = "GROUP BY t.DJQDM";
			   		}else{
			   		    djqby = "GROUP BY t.DJZQDM";
			   		}
				   
				 //===============================================地籍区/地籍子区语句================================================   
		   builddjq.append("select")
				   .append(" max(t.DJQDM) as DJQDM,")
				   .append(" max(t.DJZQDM) as DJZQDM,")
				   .append(" max(t.FZLB) as FZLB, ")
				   .append(" sum(t.HJ) as HJ,")
				   .append(" ROUND(SUM(t.DJMJ),2)as DJMJ,")
				   .append(" ROUND(AVG(t.PJMJ),2)as PJMJ,")
				   .append(" SUM(t.JYZJ) AS JYZJ,")
				   .append(" SUM(t.JYJG) AS JYJG")
				   .append(" from (")
				   .append(" select")
				   .append(" max(bdcdy.DJQDM) as DJQDM,")
				   .append(" max(bdcdy.DJZQDM) as DJZQDM,")
				   .append(" max(bdcdy.DJQMC) as FZLB,")
				   .append(" count(*) as hj,")
				   .append(" ROUND(SUM(bdcdy.ZDMJ),2)as DJMJ,")
				   .append(" ROUND(AVG(bdcdy.ZDMJ),2)as PJMJ,")
				   .append(" ROUND(SUM(QDJG)/10000,2) as JYZJ,")
				   .append(" ROUND(AVG(QDJG)/10000,2) as JYJG")
				   .append(" from ")
				   .append(" (select")
				   .append(" XMXX.SLSJ as slsj,DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,")
				   .append(" DY.BDCDYLXMC AS BDCDYLXMC,QL.QLID,QL.BDCQZH,QL.QLLX AS QLLX,QL.DJLX,DY.BDCDYLX AS BDCDYLX,")
				   .append(" DY.ZDMJ AS ZDMJ,QL.QDJG,DY.QLSDFS,DY.QLXZ,TDYT.TDDJ AS TDDJ,QLR.QLRLX as QLRLX,TDYT.TDYT AS TDYT,")
				   .append(" DY.DJQDM,DY.DJQMC,DY.DJZQDM,DY.DJZQMC")
				   .append(" from (")
				   .append(" SELECT ")
				   .append("  xmbh,ZL,ZDDM,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC,ZDMJ,QLSDFS,QLXZ,DJQDM,DJZQDM,DJQMC,DJZQMC")
				   .append("  FROM BDCK.BDCS_SYQZD_XZ")
				   .append(" UNION ALL")
				   .append("  SELECT ")
				   .append("  xmbh,ZL,ZDDM,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC ,ZDMJ,QLSDFS,QLXZ,DJQDM,DJZQDM,DJQMC,DJZQMC")
				   .append("  FROM BDCK.BDCS_SHYQZD_XZ")
				   .append(" ) DY")
				   .append(" left join BDCK.bdcs_tdyt_xz tdyt on dy.bdcdyid=tdyt.bdcdyid")
				   .append(" left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid")
				   .append(" left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid AND (QLLX=3 OR qllx=99 or qllx=23 )")
				   .append(" left join BDCK.bdcs_qlr_xz qlr on ql.qlid  = qlr.qlid")
				   .append(" left join BDCK.bdcs_xmxx xmxx on dy.xmbh = xmxx.xmbh")
				   .append(" "+buildcontname+"")
				   .append(" "+builddjsj+"")
				   .append(" ) bdcdy where bdcdy.BDCDYID is not null "+builddjqdm+" "+builddjzqdm+" "+buildtjtj+" GROUP BY bdcdy.BDCDYID")
				   .append(" )t "+djqby.toString()+" ORDER BY FZLB DESC");
			   
			   
				   if (fZLB.equals("行政区划")) {
					   maps = baseCommonDao.getDataListByFullSql(buildxzqh.toString());
				   }else if(fZLB.equals("地籍区")|| fZLB.equals("地籍子区")){
					   maps = baseCommonDao.getDataListByFullSql(builddjq.toString());
				   }else{
					   if(td_xzqh.length()>0 && td_xzqh!=null && td_xzqh!=""){
			   				maps = baseCommonDao.getDataListByFullSql(buildfzqs.toString());
			   			}else {
			   				String xzqyt=buildtime.toString();
							xzqyt=xzqyt.replaceAll("#2", "");
			   				maps = baseCommonDao.getDataListByFullSql(buildhead+build.toString()+xzqyt+buildtimeend+buildend);
						}
				   	}
				m.setRows(maps);
				m.setTotal(maps.size());
			} catch (Exception e) {
				m=null;
			}
			return m;
		}
		
	@Override
	public Message setPLXZ(String bdcdyid,String xzyy, String sjq, String sjz, String hzt,String xzwh) throws Exception {
		StringBuilder str = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String xzqhmc = ConfigHelper.getNameByValue("XZQHMC");
		String slr = Global.getCurrentUserName();
		Message msg = new Message();
		String zl="",bdcdylx="",bdcqzh="",bdcdyh="",xmbh="",qlrmc="",zjzl="",zjh="",qlid="",id="";
		Date qsrq = sdf.parse(sjq),zzrq = sdf.parse(sjz);
		if("现房".equals(hzt))
			str.append("select h.bdcdyid,ql.qlid,h.zl,djdy.bdcdylx,ql.bdcqzh,ql.bdcdyh,qlr.qlrmc,qlr.zjzl,qlr.zjh,djdy.xmbh from bdck.bdcs_h_xz h left join bdck.bdcs_djdy_xz djdy on h.bdcdyid=djdy.bdcdyid ")
			.append(" left join bdck.bdcs_ql_xz ql on djdy.djdyid=ql.djdyid left join bdck.bdcs_qlr_xz qlr on ql.qlid=qlr.qlid where ")
			.append("h.bdcdyid in ('").append(bdcdyid).append("')");
		else if("期房".equals(hzt))
			str.append("select h.bdcdyid,ql.qlid,h.zl,djdy.bdcdylx,ql.bdcqzh,ql.bdcdyh,qlr.qlrmc,qlr.zjzl,qlr.zjh,djdy.xmbh from bdck.bdcs_h_xzy h left join bdck.bdcs_djdy_xz djdy on h.bdcdyid=djdy.bdcdyid ")
			.append(" left join bdck.bdcs_ql_xz ql on djdy.djdyid=ql.djdyid left join bdck.bdcs_qlr_xz qlr on ql.qlid=qlr.qlid where ")
			.append("h.bdcdyid in ('").append(bdcdyid).append("')");
		List<Map> map = baseCommonDao.getDataListByFullSql(str.toString());
		if(map!=null&&map.size()>0){
			for(Map m : map){
				id = m.get("BDCDYID")==null?"":m.get("BDCDYID").toString();
				zl = m.get("ZL")==null?"":m.get("ZL").toString();
				bdcdylx = m.get("BDCDYLX")==null?" ":m.get("BDCDYLX").toString();
				bdcqzh = m.get("BDCQZH")==null?" ":m.get("BDCQZH").toString();
				bdcdyh = m.get("BDCDYH")==null?" ":m.get("BDCDYH").toString();
				xmbh = m.get("XMBH")==null?" ":m.get("XMBH").toString();
				qlrmc = m.get("QLRMC")==null?" ":m.get("QLRMC").toString();
				zjzl = m.get("ZJZL")==null?" ":m.get("ZJZL").toString();
				zjh = m.get("ZJH")==null?" ":m.get("ZJH").toString();
				//qlid = m.get("QLID")==null?"":m.get("QLID").toString();
				//BDCS_QL_XZ ql = baseCommonDao.get(BDCS_QL_XZ.class, qlid);
				BDCS_DYXZ dyxz = new BDCS_DYXZ();
				dyxz.setId(UUID.randomUUID().toString());
				dyxz.setBDCDYID(id);
				dyxz.setBDCDYLX(bdcdylx);
				dyxz.setBDCQZH(bdcqzh);
				dyxz.setBDCDYH(bdcdyh);
				dyxz.setXMBH(xmbh);
				dyxz.setBXZRMC(qlrmc);
				dyxz.setBXZRZJZL(zjzl);
				dyxz.setBXZRZJHM(zjh);
				dyxz.setXZQSRQ(qsrq);
				dyxz.setXZZZRQ(zzrq);
				dyxz.setSDTZRQ(qsrq);
				dyxz.setXZWJHM(xzwh);
				dyxz.setXZDW(xzqhmc+"登记中心");
				dyxz.setSLR(slr);
				dyxz.setSLRYJ(xzyy);
				dyxz.setXZLX("03");
				dyxz.setXZFW(zl);
				dyxz.setYXBZ("1");
				baseCommonDao.save(dyxz);
				if("期房".equals(hzt)){
					BDCS_H_XZY hxzy = baseCommonDao.get(BDCS_H_XZY.class,id);
					hxzy.setYSDM("1");
					baseCommonDao.update(hxzy);
				}
				else{
					BDCS_H_XZ hxz = baseCommonDao.get(BDCS_H_XZ.class,id);
					hxz.setYSDM("1");
					baseCommonDao.update(hxz);
				}
				baseCommonDao.flush();
			}
			msg.setMsg("导入成功");
			msg.setSuccess("true");
			YwLogUtil.addYwLog("导入成功", ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
			return msg;
		}
		return msg;
		
	}
	
	@Override
	public Message cqHxx(Map<String, String> mapCondition, int page, int rows) throws Exception {

		String relationid="", fwzt="", htbh="", zl="", xmmc="";
		if (!StringHelper.isEmpty(mapCondition.get("fwzt"))) {
			relationid = mapCondition.get("relationid");
			fwzt = mapCondition.get("fwzt");
			htbh = mapCondition.get("htbh");
			zl = mapCondition.get("zl");
			xmmc = mapCondition.get("xmmc");
		}
		Connection conn = null;
		ResultSet rs = null;
		boolean flag = false;
		conn = JH_DBHelper.getConnect_gxjyk();
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("SELECT * FROM GXJYK.H H ");
		if (!StringHelper.isEmpty(htbh)) {
			sBuilder.append(" LEFT JOIN GXJYK.HT HT ON H.GXXMBH = HT.GXXMBH WHERE  HT.HTBH = '").append(htbh).append("' ");
			flag = true;
		}
		if (!flag) {
			sBuilder.append(" WHERE ");
		}		
		if (!StringHelper.isEmpty(relationid)) {
			if (flag) {
				sBuilder.append(" AND ");
			}
			sBuilder.append(" H.RELATIONID='").append(relationid).append("' ");
			flag = true;
		}
		if (!StringHelper.isEmpty(zl)) {
			if (flag) {
				sBuilder.append(" AND ");
			}
			sBuilder.append(" H.ZL='").append(zl).append("' ");
			flag = true;
		}
		if (!StringHelper.isEmpty(xmmc)) {
			if (flag) {
				sBuilder.append(" AND ");
			}
			sBuilder.append(" H.XMMC='").append(xmmc).append("' ");
			flag = true;
		}		
		String sql = sBuilder.toString();
		List<Map> result  = new ArrayList<Map>();
		long count = 0;
		Message mas=new Message();	
		try {
			rs = JH_DBHelper.excuteQuery(conn, sql);
			if(rs!=null){				
				result = resultSetToList(rs);
				count =(long)result.size();
			}else {
				mas.setMsg("-null-"+sBuilder.toString());	
				mas.setTotal(count);
				mas.setRows(result);
				return mas;
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(rs!=null)
					rs.close();
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(count == 0){
			mas.setMsg("out"+sBuilder.toString());	
			mas.setTotal(count);
			mas.setRows(result);
			return mas;
		}		
		mas.setMsg("ok"+sBuilder.toString());	
		mas.setTotal(count);
		mas.setRows(result);
		return mas;	
	}
	
	@Override
	public Message extractCQHxx(String bdcdyid,String fwzt) throws Exception {			
		Connection conn = null;
		ResultSet rs = null;
		Message mas=new Message();
		List<Map> mm=new ArrayList<Map>();
		String zl="",fh="",zrzbdcdyid="",relationid="";
		double ycjzmj = 0,ycftjzmj = 0,yctnjzmj = 0,scjzmj = 0,sctnjzmj = 0,scftjzmj = 0;
		long count = 0;
		conn = JH_DBHelper.getConnect_gxjyk();
		String sql = "SELECT A.* FROM GXJYK.H A WHERE A.BDCDYID='"+bdcdyid+"'";
		List<Map> result  = new ArrayList<Map>();
		try {
			rs = JH_DBHelper.excuteQuery(conn, sql);
			if(rs!=null){				
				result = resultSetToList(rs);
				count =(long)result.size();
			}
			if (result!=null && result.size()>0) {				
				Map hInfo = result.get(0);
				if("期房".equals(fwzt)){
					relationid = hInfo.get("RELATIONID").toString();
					String sql_qf = " FROM BDCK.BDCS_H_XZY A WHERE A.BDCDYID='"+relationid+"'";
					count = baseCommonDao.getCountByFullSql(sql_qf);
					if (count == 0) {
						BDCS_H_XZY hxy = new BDCS_H_XZY();
						zl = hInfo.get("ZL").toString();
						fh = hInfo.get("FH").toString();
						zrzbdcdyid = hInfo.get("ZRZBDCDYID").toString();
						ycjzmj = StringHelper.getDouble(hInfo.get("YCJZMJ"));
						ycftjzmj = StringHelper.getDouble(hInfo.get("YCFTJZMJ"));
						yctnjzmj = StringHelper.getDouble(hInfo.get("YCTNJZMJ"));
						scjzmj = StringHelper.getDouble(hInfo.get("SCJZMJ"));
						sctnjzmj = StringHelper.getDouble(hInfo.get("SCTNJZMJ"));
						scftjzmj = StringHelper.getDouble(hInfo.get("SCFTJZMJ"));
						hxy.setId(relationid);
						hxy.setZL(zl+fh);
						hxy.setFH(fh);
						hxy.setYCJZMJ(ycjzmj);
						hxy.setFWBM(relationid);
						hxy.setRELATIONID(relationid);
						hxy.setZRZBDCDYID(zrzbdcdyid);
						hxy.setYCTNJZMJ(yctnjzmj);
						hxy.setYCFTJZMJ(ycftjzmj);
						hxy.setSCJZMJ(scjzmj);
						hxy.setSCTNJZMJ(sctnjzmj);
						hxy.setSCFTJZMJ(scftjzmj);
						baseCommonDao.save(hxy);
						BDCS_H_LSY bdcs_h_lsy = new BDCS_H_LSY();
						PropertyUtils.copyProperties(bdcs_h_lsy,hxy);
						baseCommonDao.save(bdcs_h_lsy);
					}else {
						mas.setTotal(0);
						mas.setRows(mm);
						mas.setMsg("fwbm为"+relationid+"的户数据已存在bdck户中，不可再次提取");
						return mas;
					}
					
				}
				else if("现房".equals(fwzt)){
					relationid = hInfo.get("RELATIONID").toString();
					String sql_qf = " FROM BDCK.BDCS_H_XZ A WHERE A.BDCDYID='"+relationid+"'";
					count = baseCommonDao.getCountByFullSql(sql_qf);
					if (count == 0) {
						BDCS_H_XZ hxy = new BDCS_H_XZ();
						zl = hInfo.get("ZL").toString();
						fh = hInfo.get("FH").toString();
						zrzbdcdyid = hInfo.get("ZRZBDCDYID").toString();
						ycjzmj = StringHelper.getDouble(hInfo.get("YCJZMJ"));
						ycftjzmj = StringHelper.getDouble(hInfo.get("YCFTJZMJ"));
						yctnjzmj = StringHelper.getDouble(hInfo.get("YCTNJZMJ"));
						scjzmj = StringHelper.getDouble(hInfo.get("SCJZMJ"));
						sctnjzmj = StringHelper.getDouble(hInfo.get("SCTNJZMJ"));
						scftjzmj = StringHelper.getDouble(hInfo.get("SCFTJZMJ"));
						hxy.setId(relationid);
						hxy.setZL(zl+fh);
						hxy.setFH(fh);
						hxy.setYCJZMJ(ycjzmj);
						hxy.setFWBM(relationid);
						hxy.setRELATIONID(relationid);
						hxy.setZRZBDCDYID(zrzbdcdyid);
						hxy.setYCTNJZMJ(yctnjzmj);
						hxy.setYCFTJZMJ(ycftjzmj);
						hxy.setSCJZMJ(scjzmj);
						hxy.setSCTNJZMJ(sctnjzmj);
						hxy.setSCFTJZMJ(scftjzmj);
						baseCommonDao.save(hxy);
						BDCS_H_LS bdcs_h_ls = new BDCS_H_LS();
						PropertyUtils.copyProperties(bdcs_h_ls,hxy);
						baseCommonDao.save(bdcs_h_ls);
						baseCommonDao.flush();
					}else {
						mas.setTotal(0);
						mas.setRows(mm);
						mas.setMsg("fwbm为"+relationid+"的户数据已存在bdck中，不可再次提取");
						return mas;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
				try {
					if(rs!=null)
						rs.close();
					if(conn!=null)
						conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		StringBuilder  strRest=new StringBuilder();
		StringBuilder  strResou=new StringBuilder();	
		long a=0;
		strRest.append("select *  ");
		if(relationid!=null){
			if(fwzt!=null&&fwzt.equals("现房")){//现房
					strResou.append(" from bdck.bdcs_h_xz a where a.bdcdyid='").append(relationid).append("'");
			}else  if(fwzt!=null&&fwzt.equals("期房")){//期房
					strResou.append(" from bdck.bdcs_h_xzy a where a.bdcdyid='").append(relationid).append("'");
			}
			a=baseCommonDao.getCountByFullSql(strResou.toString());
			mm=baseCommonDao.getDataListByFullSql(strRest.append(strResou).toString());
		}
		mas.setTotal(0);
		mas.setRows(mm);
		mas.setMsg("fwbm为"+relationid+"的户数据提取成功");
		return mas;
	}
	
	
	
	
	@Override
	public Message getSZZFTJ(String sjq, String sjz) {

		Message m = new Message();
		StringBuilder zl = new StringBuilder();
		zl.append("select staff_name as ry,sum(zsl+zml) as zl,nvl(sum(zsl)-sum(zfzsl),0) as zsl,nvl(sum(zml)-sum(zfzml),0) as zml,sum(zfzml) as zfzmzl,sum(zfzsl) as zfzszl from (")
		.append("select count(bdcqzh) as zsl,0 as zml,0 as zfzml,0 as zfzsl,staff_name from(select distinct zs.bdcqzh,szr.staff_name ")
		.append(" from bdck.bdcs_xmxx xx left join bdck.bdcs_zs_gz zs on xx.xmbh=zs.xmbh left join (select distinct pro.file_number,act.staff_name ")
		.append(" from bdc_workflow.wfi_proinst pro left join bdc_workflow.wfi_actinst act on pro.proinst_id=act.proinst_id where act.actinst_name='缮证' ")
		.append(" and act.staff_name is not null)szr on xx.project_id=szr.file_number where xx.sfdb=1 and zs.bdcqzh like '%不动产权第%' and szr.staff_name is not null ")
		.append(" and to_char(zs.szsj,'yyyy-mm-dd')between '").append(sjq).append("' and '").append(sjz).append("')group by staff_name ")
		.append(" union ")
		.append(" select 0 as zsl,count(bdcqzh) as zml,0 as zfzml,0 as zfzsl,staff_name from (select distinct zs.bdcqzh,szr.staff_name from bdck.bdcs_xmxx xx ")
		.append(" left join bdck.bdcs_zs_gz zs on xx.xmbh=zs.xmbh left join (select distinct pro.file_number,act.staff_name from bdc_workflow.wfi_proinst pro ")
		.append(" left join bdc_workflow.wfi_actinst act on pro.proinst_id=act.proinst_id where act.actinst_name='缮证' and act.staff_name is not null)szr on ")
		.append(" xx.project_id=szr.file_number where xx.sfdb=1 and zs.bdcqzh like '%不动产证明%' and to_char(zs.szsj,'yyyy-mm-dd')between '").append(sjq).append("' and '").append(sjz).append("')")
		.append(" group by staff_name ")
		.append(" union ")
		.append("select 0 as zsl,0 as zml,count(r.qzbh) as zfzml,0 as zfzsl,szr as staff_name from bdck.bdcs_rkqzb r where r.sfzf='1' and r.qzlx='1' and to_char(r.zfsj,'yyyy-mm-dd')between '").append(sjq)
		.append("' and '").append(sjz).append("' group by szr ")
		.append(" union ")
		.append("select 0 as zsl,0 as zml,0 as zfzml,count(r.qzbh) as zfzsl,szr as staff_name from bdck.bdcs_rkqzb r where r.sfzf='1' and r.qzlx='0' and to_char(r.zfsj,'yyyy-mm-dd')between '").append(sjq)
		.append("' and '").append(sjz).append("' group by szr ")
		.append(")where staff_name<>'管理员' and substr(staff_name,1,4)<>'撤回人员' group by staff_name");
		List<Map> map = baseCommonDao.getDataListByFullSql(zl.toString());
		m.setRows(map);
		return m;
	
	}
	
	@Override
	public Message getZSBHTJ(String year) {
		Message m = new Message();
		StringBuilder zsbh = new StringBuilder();
		zsbh.append("SELECT MTH, SUM(ZSS) ZSS, SUM(ZMS) ZMS, SUM(ZFZSS) ZFZSS,SUM(ZFZMS) ZFZMS, SUM(RKZSS) RKZSS, SUM(RKZMS) RKZMS, SUM(KCZZSS) KCZZSS,SUM(KCZZMS) KCZZMS ")
//		.append("  FROM (SELECT TO_CHAR(ZS.SZSJ, 'mm') AS MTH, COUNT(TO_CHAR(QZB.SZSJ, 'mm')) AS ZSS, 0 AS ZMS, 0 AS ZFZMS, 0 AS ZFZSS, 0 AS RKZSS, 0 AS RKZMS ,0 AS KCZZSS,0 AS KCZZMS ")
//		.append(" FROM BDCK.BDCS_RKQZB QZB  LEFT JOIN BDCK.BDCS_ZS_GZ ZS ON ZS.ZSBH = TO_CHAR(QZB.QZBH) ")
//		.append(" WHERE QZLX = '0'  AND SFSZ = '1' AND (SFZF = '0' OR SFZF IS NULL) AND TO_CHAR(ZS.SZSJ, 'yyyy') = '").append(year).append("' GROUP BY TO_CHAR(ZS.SZSJ, 'mm') ")
//		.append(" UNION ALL ")
//		.append(" SELECT TO_CHAR(ZS.SZSJ, 'mm') AS MTH, 0 AS ZSS, COUNT(TO_CHAR(QZB.SZSJ, 'mm')) AS ZMS, 0 AS ZFZMS, 0 AS ZFZSS, 0 AS RKZSS, 0 AS RKZMS,0 AS KCZZSS,0 AS KCZZMS ")
//		.append(" FROM BDCK.BDCS_RKQZB QZB   LEFT JOIN BDCK.BDCS_ZS_GZ ZS ON ZS.ZSBH = TO_CHAR(QZB.QZBH) ")
//		.append(" WHERE QZLX = '1'  AND SFSZ = '1' AND (SFZF = '0' OR SFZF IS NULL) AND TO_CHAR(ZS.SZSJ, 'yyyy') = '").append(year).append("' GROUP BY TO_CHAR(ZS.SZSJ, 'mm') ")
		.append(" FROM (SELECT TO_CHAR(ZS.SZSJ, 'mm') AS MTH, 0 AS ZMS, count( DISTINCT ZS.BDCQZH ) AS ZSS, 0 AS ZFZMS, 0 AS ZFZSS, 0 AS RKZSS, 0 AS RKZMS ,0 AS KCZZSS,0 AS KCZZMS ")
		.append(" FROM BDCK.BDCS_ZS_GZ ZS LEFT JOIN BDCK.BDCS_QDZR_GZ QDZR ON ZS.ZSID = QDZR.ZSID LEFT JOIN BDCK.BDCS_XMXX XMXX ON ZS.XMBH = XMXX.XMBH ")
		.append(" WHERE TO_CHAR(ZS.SZSJ, 'yyyy') = '").append(year).append("' AND ZS.BDCQZH LIKE '%不动产权%'  GROUP BY TO_CHAR(ZS.SZSJ, 'mm') ")
		.append(" UNION ALL ")
		.append(" SELECT TO_CHAR(ZS.SZSJ, 'mm') AS MTH, count( DISTINCT ZS.BDCQZH ) AS ZMS,0 AS ZSS, 0 AS ZFZMS, 0 AS ZFZSS, 0 AS RKZSS, 0 AS RKZMS ,0 AS KCZZSS,0 AS KCZZMS ")
		.append(" FROM BDCK.BDCS_ZS_GZ ZS LEFT JOIN BDCK.BDCS_QDZR_GZ QDZR ON ZS.ZSID = QDZR.ZSID LEFT JOIN BDCK.BDCS_XMXX XMXX ON ZS.XMBH = XMXX.XMBH ")
		.append(" WHERE TO_CHAR(ZS.SZSJ, 'yyyy') = '").append(year).append("' AND ZS.BDCQZH LIKE '%不动产证明%'  GROUP BY TO_CHAR(ZS.SZSJ, 'mm') ")
		.append(" UNION ALL ")
		.append(" SELECT TO_CHAR(QZB.ZFSJ, 'mm') AS MTH, 0 AS ZSS, 0 AS ZMS, COUNT(TO_CHAR(QZB.ZFSJ, 'mm')) AS ZFZSS, 0 AS ZFZMS, 0 AS RKZSS, 0 AS RKZMS,0 AS KCZZSS,0 AS KCZZMS ")
		.append(" FROM BDCK.BDCS_RKQZB QZB WHERE QZLX = '0' AND SFZF = '1' AND TO_CHAR(QZB.ZFSJ, 'yyyy') = '").append(year).append("' GROUP BY TO_CHAR(QZB.ZFSJ, 'mm') ")
		.append(" UNION ALL ")
		.append(" SELECT TO_CHAR(QZB.ZFSJ, 'mm') AS MTH, 0 AS ZSS, 0 AS ZMS, 0 AS ZFZSS, COUNT(TO_CHAR(QZB.ZFSJ, 'mm')) AS ZFZMS, 0 AS RKZSS, 0 AS RKZMS,0 AS KCZZSS,0 AS KCZZMS ")
		.append(" FROM BDCK.BDCS_RKQZB QZB WHERE QZLX = '1' AND SFZF = '1' AND TO_CHAR(QZB.ZFSJ, 'yyyy') = '").append(year).append("' GROUP BY TO_CHAR(QZB.ZFSJ, 'mm') ")
		.append(" UNION ALL ")
		.append(" SELECT MTH, 0 AS ZSS, 0 AS ZMS, 0 AS ZFZSS, 0 AS ZFZMS, SUM(RKZSS) AS RKZSS,0 AS RKZMS,0 AS KCZZSS,0 AS KCZZMS ")
		.append(" FROM（SELECT TO_CHAR(RKB.RKSJ, 'mm') AS MTH, (RKB.JSQZBH - RKB.QSQZBH + 1) AS RKZSS ")
		.append(" FROM BDCK.BDCS_QZGLXMB RKB WHERE QZLX = '0' AND SFRK = '1'  AND TO_CHAR(RKB.RKSJ, 'yyyy') = '").append(year).append("' ) RKB  GROUP BY MTH ")
		.append(" UNION ALL ")
		.append(" SELECT MTH, 0 AS ZSS, 0 AS ZMS, 0 AS ZFZSS, 0 AS ZFZMS, 0 AS RKZSS,SUM(RKZMS) AS RKZMS,0 AS KCZZSS,0 AS KCZZMS ")
		.append(" FROM（SELECT TO_CHAR(RKB.RKSJ, 'mm') AS MTH, (RKB.JSQZBH - RKB.QSQZBH + 1) AS RKZMS ")
		.append(" FROM BDCK.BDCS_QZGLXMB RKB WHERE QZLX = '1' AND SFRK = '1'  AND TO_CHAR(RKB.RKSJ, 'yyyy') = '").append(year).append("') RKB  GROUP BY MTH ")
		.append(" UNION ALL ")
		.append(" SELECT '00' AS MTH, 0 AS ZSS, 0 AS ZMS, 0 AS ZFZMS, 0 AS ZFZSS, 0 AS RKZSS, 0 AS RKZMS,COUNT(QZB.ID) AS KCZZSS,0 AS KCZZMS ")
		.append(" FROM BDCK.BDCS_RKQZB QZB  WHERE QZLX = '0' AND (SFSZ = '0' OR SFSZ IS NULL)  AND (SFZF = '0' OR SFZF IS NULL ) AND TO_CHAR(QZB.CJSJ, 'yyyy') < '").append(year).append("' ")
		.append(" UNION ALL ") 	
		.append(" SELECT '00' AS MTH, 0 AS ZSS, 0 AS ZMS, 0 AS ZFZMS, 0 AS ZFZSS, 0 AS RKZSS, 0 AS RKZMS,0 AS KCZZSS,COUNT(QZB.ID) AS KCZZMS ")
		.append(" FROM BDCK.BDCS_RKQZB QZB  WHERE QZLX = '1' AND (SFSZ = '0' OR SFSZ IS NULL)  AND (SFZF = '0' OR SFZF IS NULL ) AND TO_CHAR(QZB.CJSJ, 'yyyy') < '").append(year).append("' ")		
		.append(" ) GROUP BY MTH ORDER BY MTH");		
		List<Map> map = baseCommonDao.getDataListByFullSql(zsbh.toString());		
		
		//获取今年以前的总库存数量
		int KCZS = 0, KCZM = 0;
		for (int i = 0; i < map.size(); i++) {			
			//获取总库存数量
			if ("00".equals(map.get(i).get("MTH"))) {
				KCZS = Integer.parseInt(map.get(i).get("KCZZSS").toString());
				KCZM = Integer.parseInt(map.get(i).get("KCZZMS").toString());
				map.remove(i);
				break;
			}
		}
		for (int i = 0; i < map.size(); i++) {		
			//int KCZSS=0,KCZMS=0; //库存证书
			int KCZZSS = KCZS,KCZZMS = KCZM;
			int mth = Integer.parseInt(map.get(i).get("MTH").toString());//月份
			int zss = Integer.parseInt(map.get(i).get("ZSS").toString());
			int zms = Integer.parseInt(map.get(i).get("ZMS").toString());
			int rkzss = Integer.parseInt(map.get(i).get("RKZSS").toString());
			int rkzms = Integer.parseInt(map.get(i).get("RKZMS").toString());
			int zfzss = Integer.parseInt(map.get(i).get("ZFZSS").toString());
			int zfzms = Integer.parseInt(map.get(i).get("ZFZMS").toString());
			/*KCZSS = rkzss - zss - zfzss;
			KCZMS = rkzms - zms - zfzms;
			map.get(i).put("KCZSS", KCZSS );
			map.get(i).put("KCZMS", KCZMS );*/
			//累计			
			int ZZSS = 0, ZZMS = 0, RKZZSS = 0, RKZZMS = 0, ZFZZSS = 0, ZFZZMS = 0;
			for (int j = 0; j < map.size(); j++) {
				int mth_o = Integer.parseInt(map.get(j).get("MTH").toString());//月份
				int zss_o = Integer.parseInt(map.get(j).get("ZSS").toString());
				int zms_o = Integer.parseInt(map.get(j).get("ZMS").toString());
				int rkzss_o = Integer.parseInt(map.get(j).get("RKZSS").toString());
				int rkzms_o = Integer.parseInt(map.get(j).get("RKZMS").toString());
				int zfzss_o = Integer.parseInt(map.get(j).get("ZFZSS").toString());
				int zfzms_o = Integer.parseInt(map.get(j).get("ZFZMS").toString());
//				KCZSS = rkzss_o - zss_o - zfzss_o;//当月的库存
//				KCZMS = rkzms_o - zms_o - zfzms_o;//当月的库存
				if( mth_o <= mth ){
					ZZSS += zss_o;
					ZZMS += zms_o;
					RKZZSS += rkzss_o;
					RKZZMS += rkzms_o;					
					ZFZZSS += zfzss_o;
					ZFZZMS += zfzms_o;
				/*}
				//总库存加上比该月小的月份的证书
				if( mth_o > mth ){*/
					KCZZSS = KCZZSS + rkzss_o - zss_o - zfzss_o;
					KCZZMS = KCZZMS + rkzms_o - zms_o - zfzms_o;
					
				}				
			}
//			StringBuilder zkc = new StringBuilder();
//			zkc.append(" SELECT SUM(KCZZSS) KCZZSS,SUM(KCZZMS) KCZZMS FROM (")
//			.append(" SELECT COUNT(QZB.ID) AS KCZZSS,0 AS KCZZMS ")
//			.append(" FROM BDCK.BDCS_RKQZB QZB  WHERE QZLX = '0' AND (SFSZ = '0' OR SFSZ IS NULL)  AND (SFZF = '0' OR SFZF IS NULL ) AND TO_CHAR(QZB.CJSJ, 'yyyymm') <= '").append(year + map.get(i).get("MTH").toString()).append("' ")
//			.append(" UNION ALL ") 	
//			.append(" SELECT 0 AS KCZZSS,COUNT(QZB.ID) AS KCZZMS ")
//			.append(" FROM BDCK.BDCS_RKQZB QZB  WHERE QZLX = '1' AND (SFSZ = '0' OR SFSZ IS NULL)  AND (SFZF = '0' OR SFZF IS NULL ) AND TO_CHAR(QZB.CJSJ, 'yyyymm') <= '").append(year + map.get(i).get("MTH").toString()).append("' ")		
//			.append(")"); 
//			List<Map> kcmap = baseCommonDao.getDataListByFullSql(zkc.toString());
//			if (kcmap != null && kcmap.size()>0) {
//				KCZZSS += Integer.parseInt(kcmap.get(0).get("KCZZSS").toString());
//				KCZZMS += Integer.parseInt(kcmap.get(0).get("KCZZMS").toString());
//			}	
						
			map.get(i).put("ZZSS", ZZSS);
			map.get(i).put("ZZMS", ZZMS);
			map.get(i).put("RKZZSS", RKZZSS);
			map.get(i).put("RKZZMS", RKZZMS);
			map.get(i).put("KCZZSS", KCZZSS);
			map.get(i).put("KCZZMS", KCZZMS);
			map.get(i).put("ZFZZSS", ZFZZSS);	
			map.get(i).put("ZFZZMS", ZFZZMS);	
		}
		
		m.setRows(map);
		return m;
	
	}
	
	/* 
	 * 不动产登记进展情况月报---鹰潭
	 * rq
	 */
	@Override
	public Message getDJYWTJByMonth(String month) {
		Message m = new Message();
		StringBuilder djywtj = new StringBuilder();
		String year = month.substring(0, 4);		
		djywtj.append("SELECT YWINFO.DJLX, YWINFO.QLLX, YWINFO.SLGS, YWINFO.BJGS, YWINFO.BDCDYLX, NVL(FZINFO.FZZMGS, 0) AS FZZMGS, NVL(FZINFO.FZZSGS, 0) AS FZZSGS ")
		//YWINFO
		.append(" FROM (SELECT DJLX, QLLX, SUM(CASE WHEN SFBJ = '1' THEN 1 ELSE 0 END) AS BJGS, SUM(CASE WHEN  (SFBJ IS NULL OR SFBJ <> '1') THEN 1 ELSE 0 END) AS SLGS, DJDY.BDCDYLX ")
		.append(" FROM BDCK.BDCS_XMXX XMXX INNER JOIN BDCK.BDCS_DJDY_GZ DJDY  ON XMXX.XMBH = DJDY.XMBH ")
		.append(" WHERE TO_CHAR(XMXX.SLSJ, 'yyyy-mm') = '").append(month).append("'  GROUP BY XMXX.DJLX, XMXX.QLLX,DJDY.BDCDYLX ORDER BY XMXX.DJLX, TO_NUMBER(QLLX)) YWINFO ")
		.append(" LEFT JOIN ( ")
		//FZINFO
		.append(" SELECT XMXX.DJLX, XMXX.QLLX, SUM(CASE WHEN DJFZ.HFZSH LIKE '%(____)%不动产证明第_______号%' THEN 1 ELSE 0 END) AS FZZMGS, SUM(CASE WHEN DJFZ.HFZSH LIKE '%(____)%不动产权第_______号' THEN 1 ELSE 0 END) AS FZZSGS, COUNT(*) AS FZGS  ")
		.append(" FROM (SELECT DISTINCT XMBH, HFZSH, MIN(FZSJ) AS FZSJ ")
		.append(" FROM BDCK.BDCS_DJFZ GROUP BY XMBH, HFZSH) DJFZ LEFT JOIN BDCK.BDCS_XMXX XMXX ON XMXX.XMBH = DJFZ.XMBH ")
		.append(" WHERE TO_CHAR(DJFZ.FZSJ, 'yyyy') = '").append(year).append("' GROUP BY XMXX.DJLX, XMXX.QLLX) FZINFO ")
		.append(" ON YWINFO.DJLX = FZINFO.DJLX AND YWINFO.QLLX = FZINFO.QLLX ");
//		.append(" LEFT JOIN ( ")
//		//SZINFO
//		.append(" SELECT XMXX.DJLX, XMXX.QLLX, SUM(CASE WHEN ZS.BDCQZH LIKE '%不动产证明%' THEN 1 ELSE 0 END) AS SZZMGS, SUM(CASE WHEN ZS.BDCQZH LIKE '%不动产权%' THEN 1 ELSE 0 END) AS SZZSGS, COUNT(*) AS SZGS ")
//		.append(" FROM BDCK.BDCS_DJSZ DJSZ INNER JOIN BDCK.BDCS_XMXX XMXX ON XMXX.XMBH = DJSZ.XMBH ")
//		.append(" INNER JOIN (SELECT DISTINCT XMBH, BDCQZH FROM BDCK.BDCS_ZS_GZ) ZS ON XMXX.XMBH = ZS.XMBH ")
//		.append(" WHERE TO_CHAR(DJSZ.SZSJ, 'yyyy') = '").append(year).append("' GROUP BY XMXX.DJLX, XMXX.QLLX) SZINFO ")
//		.append(" ON YWINFO.DJLX = SZINFO.DJLX AND YWINFO.QLLX = SZINFO.QLLX ORDER BY DJLX, TO_NUMBER(QLLX) ");
		List<Map> map = baseCommonDao.getDataListByFullSql(djywtj.toString());		
		
		StringBuilder zstj = new StringBuilder();
		zstj.append("SELECT SUM(SZZMGS) AS ZMGS,SUM(SZZSGS) AS ZSGS FROM （")
		.append(" SELECT XMXX.DJLX, XMXX.QLLX, SUM(CASE WHEN ZS.BDCQZH LIKE '%不动产证明%' THEN 1 ELSE 0 END) AS SZZMGS, SUM(CASE WHEN ZS.BDCQZH LIKE '%不动产权%' THEN 1 ELSE 0 END) AS SZZSGS, COUNT(*) AS SZGS ")
		.append(" FROM BDCK.BDCS_ZS_GZ ZS LEFT JOIN BDCK.BDCS_QDZR_GZ QDZR ON ZS.ZSID = QDZR.ZSID LEFT JOIN BDCK.BDCS_XMXX XMXX ON ZS.XMBH = XMXX.XMBH ")
		.append(" WHERE TO_CHAR(ZS.SZSJ, 'yyyy-mm') >= '").append(year+"-01").append("'");
		String mth = month.substring(5, 7);		
		if (!"01".equals(mth)) {
			zstj.append(" AND TO_CHAR(ZS.SZSJ, 'yyyy-mm') <= '").append(month).append("'");
		}
		zstj.append(" GROUP BY XMXX.DJLX, XMXX.QLLX) ");
		List<Map> zsmap = baseCommonDao.getDataListByFullSql(zstj.toString());
		Map result = new HashMap();
		//累计发放权证量（本）
		int ZSGS = 0, ZMGS = 0;
		if (zsmap != null && zsmap.size()>0) {
			ZSGS += Integer.parseInt(zsmap.get(0).get("ZSGS").toString());
			ZMGS += Integer.parseInt(zsmap.get(0).get("ZMGS").toString());
		}	
		//本月业务受理量（件）
		int SLZGS = 0, SC_SLGS = 0,ZY_SLGS = 0,BG_SLGS = 0,DY_SLGS = 0,QT_SLGS = 0;
		//本月业务办结量（件）--按登记类型划分
		int BJZGS = 0, SC_BJGS = 0,ZY_BJGS = 0,BG_BJGS = 0,DY_BJGS = 0,QT_BJGS = 0;	
		//本月业务办结量（件）--按登记客体划分
		int TD_BJGS = 0,FC_BJGS = 0,LQ_BJGS = 0,QTKT_BJGS = 0;	
		for (int i = 0; i < map.size(); i++) {			
			SLZGS += Integer.parseInt(map.get(i).get("SLGS").toString());
			BJZGS += Integer.parseInt(map.get(i).get("BJGS").toString());
			
			String djlx = map.get(i).get("DJLX").toString();
			String qllx = map.get(i).get("QLLX").toString();		
			if ("100".equals(djlx)){
				if( !"23".equals(qllx) ) {//首次登记				
					SC_SLGS += Integer.parseInt(map.get(i).get("SLGS").toString());
					SC_BJGS += Integer.parseInt(map.get(i).get("BJGS").toString());
				}else {//抵押登记
					DY_SLGS += Integer.parseInt(map.get(i).get("SLGS").toString());
					DY_BJGS += Integer.parseInt(map.get(i).get("BJGS").toString());
				}
			}else if ("200".equals(djlx) ){
				if( !"23".equals(qllx) ) {//转移登记
					ZY_SLGS += Integer.parseInt(map.get(i).get("SLGS").toString());
					ZY_BJGS += Integer.parseInt(map.get(i).get("BJGS").toString());
				}else {//抵押登记
					DY_SLGS += Integer.parseInt(map.get(i).get("SLGS").toString());
					DY_BJGS += Integer.parseInt(map.get(i).get("BJGS").toString());
				}
			}else if("300".equals(djlx)){
				if( !"23".equals(qllx) ) {//变更登记
					BG_SLGS += Integer.parseInt(map.get(i).get("SLGS").toString());
					BG_BJGS += Integer.parseInt(map.get(i).get("BJGS").toString());
				}else {//抵押登记
					DY_SLGS += Integer.parseInt(map.get(i).get("SLGS").toString());
					DY_BJGS += Integer.parseInt(map.get(i).get("BJGS").toString());
				}
			}else {//其他登记
				QT_SLGS += Integer.parseInt(map.get(i).get("SLGS").toString());
				QT_BJGS += Integer.parseInt(map.get(i).get("BJGS").toString());
			}
			
			String bdcdylx = map.get(i).get("BDCDYLX").toString();	
			if("01".equals(bdcdylx) || "02".equals(bdcdylx)){//土地
				TD_BJGS += Integer.parseInt(map.get(i).get("BJGS").toString());
			}else if ("031".equals(bdcdylx) || "032".equals(bdcdylx)) {//fc
				FC_BJGS += Integer.parseInt(map.get(i).get("BJGS").toString());
			}else if ("05".equals(bdcdylx)) {//林权
				LQ_BJGS += Integer.parseInt(map.get(i).get("BJGS").toString());
			}else {//其他
				QTKT_BJGS += Integer.parseInt(map.get(i).get("BJGS").toString());
			}
		}
		result.put("ZSGS", ZSGS);
		result.put("ZMGS", ZMGS);
		result.put("SLZGS", SLZGS);
		result.put("BJZGS", BJZGS);
		result.put("SC_SLGS", SC_SLGS);
		result.put("SC_BJGS", SC_BJGS);
		result.put("ZY_SLGS", ZY_SLGS);	
		result.put("ZY_BJGS", ZY_BJGS);	
		result.put("BG_SLGS", BG_SLGS);
		result.put("BG_BJGS", BG_BJGS);		
		result.put("DY_SLGS", DY_SLGS);	
		result.put("DY_BJGS", DY_BJGS);	
		result.put("QT_SLGS", QT_SLGS);
		result.put("QT_BJGS", QT_BJGS);
		result.put("TD_BJGS", TD_BJGS);
		result.put("FC_BJGS", FC_BJGS);
		result.put("LQ_BJGS", LQ_BJGS);	
		result.put("QTKT_BJGS", QTKT_BJGS);
		
		String qhmc=ConfigHelper.getNameByValue("XZQHMC");
		result.put("QHMC", qhmc);
		
		List<Map> upshot  = new ArrayList<Map>();
		upshot.add(result);
		m.setRows(upshot);
		return m;
	
	}
}
