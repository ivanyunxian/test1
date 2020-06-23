package com.supermap.wisdombusiness.synchroinline.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.synchroinline.task.SynchroPreMDataTask;

@Service("extractDataZhengsService")
public class ExtractDataZhengsService {
	@Autowired
	private CommonDao dao;
	private static Logger logger = Logger.getLogger(SynchroPreMDataTask.class);
	
	@SuppressWarnings("rawtypes")
	public List<HashMap<String,Object>> getZSInfos(String[] ywlshs){
		List<HashMap<String,Object>> listzs=new ArrayList<HashMap<String,Object>>();
		HashMap<String,Object> mapzs=null;
		if(ywlshs!=null&&ywlshs.length>0){
			for(String ywlsh:ywlshs){
				List<BDCS_XMXX> xmxxs=dao.getDataList(BDCS_XMXX.class, " PROJECT_ID='"+ywlsh+"'");
				if(xmxxs!=null&&xmxxs.size()>0){
					for(int i=0;i<xmxxs.size();i++){
						String xmbh=xmxxs.get(i).getId();
						if(xmbh!=null&&!xmbh.equals("")){
							//List<BDCS_ZS_XZ> zhs=dao.getDataList(BDCS_ZS_XZ.class, " XMBH='"+xmbh+"' AND ZSDATA IS NOT NULL");
							//添加权利人证件号bdck.bdcs_qlr_xz ZJG字段
							String sql = ""
									+ " select A.ZSID ,A.XMBH ,A.ZSBH ,A.BDCQZH ,\n"
									+ "                  A.SZSJ ,A.CREATETIME ,A.MODIFYTIME ,\n"
									+ "                  A.CFDAGH ,A.ZSDATA from bdck.BDCS_ZS_XZ A  where A.XMBH='"+xmbh+"' AND A.ZSDATA IS NOT NULL";
							List<Map> map =  dao.getDataListByFullSql(sql);
							if(map!=null&&map.size()>0){
								SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

								for (int m=0;m<map.size();m++){
									mapzs=new HashMap<String,Object>();
									String ZSID = "";
									if(map.get(m).get("ZSID")!=null){
										ZSID = map.get(m).get("ZSID").toString();
									}
									mapzs.put("ZSID", ZSID);
									String XMBH = "";
									if(map.get(m).get("XMBH")!=null){
										XMBH = map.get(m).get("XMBH").toString();
									}
									mapzs.put("XMBH", XMBH);
									
									String BDCQZH = "";
									if(map.get(m).get("BDCQZH")!=null){
										BDCQZH = map.get(m).get("BDCQZH").toString();
									}
									mapzs.put("BDCQZH", BDCQZH);
									String ZSBH = "";
									if(map.get(m).get("ZSBH")!=null){
										ZSBH = map.get(m).get("ZSBH").toString();
									}
									mapzs.put("ZSBH", ZSBH);
									Date SZSJ = null ;
									if(map.get(m).get("SZSJ")!=null){
										try {
											SZSJ = sf.parse(map.get(m).get("SZSJ").toString());
										} catch (ParseException e) {
											e.printStackTrace();
										}
									}
									mapzs.put("SZSJ", SZSJ);
									Date CREATETIME = null ;
									if(map.get(m).get("CREATETIME")!=null){
										try {
											CREATETIME = sf.parse(map.get(m).get("CREATETIME").toString());
										} catch (ParseException e) {
											e.printStackTrace();
										}
									}
									mapzs.put("CREATETIME", CREATETIME);
									Date MODIFYTIME = null ;
									if(map.get(m).get("MODIFYTIME")!=null){
										try {
											MODIFYTIME = sf.parse(map.get(m).get("MODIFYTIME").toString());
										} catch (ParseException e) {
											e.printStackTrace();
										}
									}
									mapzs.put("MODIFYTIME", MODIFYTIME);
									String CFDAGH = "";
									if(map.get(m).get("CFDAGH")!=null){
										CFDAGH = map.get(m).get("CFDAGH").toString();
									}
									mapzs.put("CFDAGH", CFDAGH);
									String ZSDATA = "";
									if(map.get(m).get("ZSDATA")!=null){
										ZSDATA = map.get(m).get("ZSDATA").toString();
									}
									mapzs.put("ZSDATA", ZSDATA);
									String ZJH = "";
									String qlrmc="";
									String zjh_sql = "SELECT QLR.ZJH,QLR.QLRMC  \n" +
											"  FROM BDCK.BDCS_QLR_GZ QLR\n" + 
											"  LEFT JOIN BDCK.BDCS_QDZR_GZ QDZR ON QLR.QLRID = QDZR.QLRID\n" + 
											" WHERE QDZR.ZSID = '"+ZSID+"'\n" + 
											" ORDER BY QLR.SXH ";
							        List<Map> map_zjh=  dao.getDataListByFullSql(zjh_sql);
									if(map_zjh!=null&&map_zjh.size()>0){
										for(int zj =0;zj<map_zjh.size();zj++){
											ZJH += map_zjh.get(zj).get("ZJH").toString()+",";
											qlrmc+=map_zjh.get(zj).get("QLRMC").toString()+",";
										}
										if(!StringHelper.isEmpty(ZJH)){
											ZJH = ZJH.substring(0,ZJH.length()-1);
											qlrmc=qlrmc.substring(0,qlrmc.length()-1);
										}
									}
									mapzs.put("ZJH", ZJH);
									mapzs.put("QLRMC",qlrmc);
									listzs.add(mapzs);
								}
								
							}else{								
								logger.info("证书表里面没有查到数据！");
							}
						}else{							
							logger.info("查询到的项目编号不能为空值！");
						}
					}
				}else{
					logger.info("项目信息表没有查询到数据！");				
				}
			}
		}else{
			logger.info("流水号不能为空！");
		}
		
		return listzs;
	}
}
