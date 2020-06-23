package com.supermap.realestate.registration.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_TDYT_XZ;
import com.supermap.realestate.registration.service.DealService;
import com.supermap.realestate.registration.tools.OperateFeature;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.services.components.commontypes.Feature;
import com.supermap.services.components.commontypes.Geometry;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

@Service("dealService")
public class DealServiceImpl implements DealService{
	
	@Autowired
	private CommonDao dao;
	
	/**
	 * 获取交易详细信息
	 * @return Map<String,Object>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	 public Map<String,Object> getDealDetails(String BDCDYH,String BDCDYID){
		 
		 String bdcdyid = "";
		 Map<String,Object>  map = new HashMap<String,Object>();
		 Map<String,String> zdinfo = new HashMap<String,String>();
		 List<Map> ytlist = new ArrayList<Map>();
		 List<Map> hlist = new ArrayList<Map>();
		 List<Map> qlrlist = new ArrayList<Map>();
		 List<Map> fwqlrlist = new ArrayList<Map>();
		 List<BDCS_SHYQZD_XZ> listmap = null;
		 if(BDCDYH!=null && !BDCDYH.equals("")){
			 listmap=dao.getDataList(BDCS_SHYQZD_XZ.class," BDCDYH = '"+BDCDYH+"'");
		 }else if(BDCDYID!=null && !BDCDYID.equals("")){
			 listmap=dao.getDataList(BDCS_SHYQZD_XZ.class," BDCDYID = '"+BDCDYID+"'");
		 }
		 //宗地信息
		 String djdyid="";
		 if(listmap!=null && listmap.size()>0){
			 BDCS_SHYQZD_XZ dy  = listmap.get(0);
			 bdcdyid = dy.getId();
			 zdinfo = fillZDInfo(dy);
			 List<BDCS_DJDY_XZ> list_djdy=dao.getDataList(BDCS_DJDY_XZ.class, "BDCDYLX='02' AND BDCDYID='"+bdcdyid+"'");
			 if(list_djdy!=null&&list_djdy.size()>0){
				 djdyid=list_djdy.get(0).getDJDYID();
			 }
			 addLimitStatus(bdcdyid,djdyid,zdinfo);
		 }else{
			 zdinfo =null; 
		 }
		 if(!StringHelper.isEmpty(djdyid)){
			 StringBuilder qlrsql = new StringBuilder();
			 qlrsql.append(" SELECT QLR.QLRMC,QLR.ZJH,QL.BDCQZH,QLR.GYFS")
			       .append(" FROM BDCK.BDCS_QL_XZ QL LEFT JOIN BDCK.BDCS_QLR_XZ QLR ON QLR.QLID = QL.QLID ")
			       .append(" WHERE QL.QLLX='3' AND QL.DJDYID = '")
			       .append(djdyid).append("'");
			 qlrlist = dao.getDataListByFullSql(qlrsql.toString());
		 }
		 // 宗地的土地用途
		 if(zdinfo!=null){
			 List<BDCS_TDYT_XZ> tdyt = dao.getDataList(BDCS_TDYT_XZ.class, " BDCDYID='"+zdinfo.get("BDCDYID")+"'");
			 if(tdyt!=null && tdyt.size()>0){
				 Map<String,Object> m = new HashMap<String,Object>();
				 for(BDCS_TDYT_XZ yt : tdyt){
					 m.put("TDYTMC", yt.getTDYTMC());
					 m.put("TDDJ", yt.getTDDJName());
					 m.put("QSRQ", yt.getQSRQ());
					 m.put("ZZRQ", yt.getZZRQ());
					 m.put("TDMJ", yt.getTDMJ());
					 m.put("SFZYT", yt.getSFZYT().equals("1")?"是":"否");
					 ytlist.add(m);
				 }
			 }
			 ////  地上附着物（房屋）
			 String hid = "";
			 List<BDCS_H_XZ> h = dao.getDataList(BDCS_H_XZ.class, " ZDBDCDYID='"+zdinfo.get("BDCDYID")+"'");
			 if(h !=null && h.size()>0){
				Map<String,Object> m = new HashMap<String,Object>();
				for(BDCS_H_XZ h_: h) {
					m = fillFwInfo(h_);
					hid = h_.getId();
					hlist.add(m);
				}
			 }
		     ////  房屋权利人信息
			 if(!hid.equals("") && hid!=null){
				 List<BDCS_QL_XZ> ql_xz = dao.getDataList(BDCS_QL_XZ.class, " BDCDYID='"+hid+"' AND QLLX ='4'");
				 if(ql_xz !=null && ql_xz.size()>0){
					 BDCS_QL_XZ ql = ql_xz.get(0); 
					 if(ql!=null){
						 List<BDCS_QLR_XZ> qlr_xz = dao.getDataList(BDCS_QLR_XZ.class, " QLID='"+ql.getId()+"'");
						 if(qlr_xz !=null && qlr_xz.size()>0){
							 Map<String,Object> m = new HashMap<String,Object>();
							 for(BDCS_QLR_XZ qlr : qlr_xz){
								 m.put("QLRMC", qlr.getQLRMC());
								 m.put("ZJH", qlr.getZJH());
								 m.put("BDCQZH", qlr.getBDCQZH());
								 m.put("GYFS", qlr.getGYFS());
								 fwqlrlist.add(m);
							 }
						 }
					 }
				 }
			 }
		 }
		 //addZDVector(map,bdcdyid,BDCDYLX.SHYQZD);
		 map.put("ZDInfo", zdinfo);
		 map.put("YTList", ytlist);
		 map.put("QLRList", qlrlist);
		 map.put("FWInfo", hlist);
		 map.put("FWQLRList", fwqlrlist);
		 return map;
	 }
	
	private Map<String,Object> fillFwInfo(BDCS_H_XZ h_){
		Map<String,Object> m = new HashMap<String,Object>();
		if(h_!=null){
			m.put("BDCDYH", h_.getBDCDYH());        
			m.put("ZL", h_.getZL());                  m.put("FWBM", h_.getFWBM());  
			m.put("DH", h_.getDYH() );                m.put("FH", h_.getFH() );
            m.put("SZC", h_.getSZC() );               m.put("HH", h_.getHH() );
            m.put("QSC", h_.getQSC());                m.put("ZZC", h_.getZZC() );
            m.put("HX", h_.getHX() );                 m.put("FWJG", h_.getFWJGName() );
            m.put("FWLX", h_.getFWLXName() );         m.put("FWXZ", h_.getFWXZName() );
            m.put("SCJZMJ", h_.getSCJZMJ() );         m.put("YCJZMJ", h_.getYCJZMJ() );
            m.put("SCTNMJ", h_.getSCTNJZMJ() );       m.put("YCTNMJ", h_.getYCTNJZMJ() );
            m.put("SCFTJZMJ", h_.getSCFTJZMJ() );     m.put("YCFTJZMJ", h_.getYCFTJZMJ() );
            m.put("SCDXBFJZMJ", h_.getSCDXBFJZMJ() ); m.put("YCDXBFJZMJ", h_.getYCDXBFJZMJ() );
            m.put("SCQTJZMJ", h_.getSCQTJZMJ() );     m.put("YCQTJZMJ", h_.getYCQTJZMJ() );
            m.put("SCFTXS", h_.getSCFTXS() );         m.put("YCFTXS", h_.getYCFTXS() );
            m.put("GYTDMJ", h_.getGYTDMJ() );         m.put("FTTDMJ", h_.getFTTDMJ() );
            m.put("DYTDMJ", h_.getDYTDMJ() );         m.put("TDSYQR", h_.getTDSYQR() );
            m.put("ZCS", h_.getZCS() );               m.put("TDSYQQSRQ", h_.getTDSYQQSRQ());
            m.put("TDSYQZZRQ", h_.getTDSYQZZRQ() );   m.put("QLXZ", h_.getQLXZ() ); 
            m.put("CB", h_.getFWCBName());            m.put("FWTDYT", h_.getFWTDYT() );
            m.put("GHYT", h_.getGHYTName() );         m.put("JGSJ", h_.getJGSJ() );
            m.put("DSCS", h_.getDscs_zrz() );         m.put("DXCS", h_.getDxcs_zrz());
		}
		return m;
	}
	
	private Map<String,String> fillZDInfo(BDCS_SHYQZD_XZ dy){
		Map<String,String> zdinfo = new HashMap<String,String>();
		if(dy!=null){
			 zdinfo.put("BDCDYID",dy.getId());
			 zdinfo.put("BDCDYH", dy.getBDCDYH());         zdinfo.put("ZDDM", dy.getZDDM());
			 zdinfo.put("ZL", dy.getZL());                 zdinfo.put("BDCQZH", dy.getZDBDCQZH());
			 zdinfo.put("QXMC", dy.getQXMC());             zdinfo.put("DJQMC", dy.getDJQMC());
			 zdinfo.put("DJZQMC", dy.getDJZQMC());         zdinfo.put("ZDTZM", dy.getZDTZMName());
			 zdinfo.put("QLSDFS", dy.getQLSDFSName());     zdinfo.put("QLXZ", dy.getQLXZName());
			 zdinfo.put("ZDMJ", dy.getZDMJ().toString());  zdinfo.put("TFH", dy.getTFH());
			 zdinfo.put("JZMJ", dy.getJZMJ().toString());  zdinfo.put("RJL", dy.getRJL());
			 zdinfo.put("ZDSZD", dy.getZDSZD());           zdinfo.put("ZDSZN", dy.getZDSZN());
			 zdinfo.put("ZDSZX", dy.getZDSZX());           zdinfo.put("ZDSZB", dy.getZDSZB());
			 zdinfo.put("JZMD", dy.getJZMD()); 
			 zdinfo.put("XZQH", dy.getQXDM());
			 zdinfo.put("DJQDM", dy.getDJQDM());
			 zdinfo.put("DJZQDM", dy.getDJZQDM());
		}
		return zdinfo;
	}
	/*
	 * 向宗地信息加入CFZT，DYZT，XZZT，YYZT
	 */
	private void addLimitStatus(String bdcdyid,String djdyid,Map<String,String> zdinfo){
		zdinfo.put("DYZT","0");
		zdinfo.put("YYZT","0");
		zdinfo.put("CFZT","0");
		if(!StringHelper.isEmpty(djdyid)){
			StringBuilder zdsql = new StringBuilder();
			zdsql.append(" SELECT QL.QLLX,QL.DJLX")
			     .append(" FROM BDCK.BDCS_QL_XZ QL ")
			     .append(" WHERE DJDYID = '")
			     .append(djdyid).append("'");
			List<Map> lst = dao.getDataListByFullSql(zdsql.toString());
			if(lst!=null && lst.size()>0){
				for(Map m : lst){
					String qllx = StringHelper.formatObject(m.get("QLLX"));
					String djlx = StringHelper.formatObject(m.get("DJLX"));
					if(DJLX.CFDJ.Value == djlx && QLLX.QTQL.Value == qllx){
						zdinfo.put("CFZT","1");
					}else{
						zdinfo.put("CFZT","0");
						} 
					if(QLLX.DIYQ.Value.equals(qllx)){
						zdinfo.put("DYZT","1");
					}else{
						zdinfo.put("DYZT","0");
						}  
					if(DJLX.YYDJ.Value.equals(djlx)){
						zdinfo.put("YYZT","1");
					}else{
						zdinfo.put("YYZT","0");
						}
				}
			}
			//正在办理
			StringBuilder sqling = new StringBuilder();
			sqling.append(" SELECT QL.QLLX,QL.DJLX,XMXX.QLLX XMQLX,XMXX.DJLX XMDJLX ")
			      .append(" FROM BDCK.BDCS_QL_GZ QL ")
			      .append(" LEFT JOIN BDCK.BDCS_XMXX XMXX ON XMXX.XMBH = QL.XMBH ")
			      .append(" WHERE ( XMXX.SFDB IS NULL OR XMXX.SFDB<>'1' ) ")
			      .append(" AND  QL.DJDYID = '")
			      .append(djdyid).append("'");
			List<Map> lst2 = dao.getDataListByFullSql(sqling.toString());
			if(lst2!=null && lst2.size()>0){
				for(Map m : lst2){
					String qllx = StringHelper.formatObject(m.get("QLLX"));
					String djlx = StringHelper.formatObject(m.get("DJLX"));
					if(DJLX.CFDJ.Value == djlx && QLLX.QTQL.Value == qllx){
						zdinfo.put("CFZT","1");
					}
					if(QLLX.DIYQ.Value.equals(qllx)){
						zdinfo.put("DYZT","1");
					} 
					if(DJLX.YYDJ.Value.equals(djlx)){
						zdinfo.put("YYZT","1");
					}
				}
			}
		}
		zdinfo.put("XZZT","0");
		if(!StringHelper.isEmpty(bdcdyid)){
			//限制
			StringBuilder strlimit=new StringBuilder();
			strlimit.append(" SELECT DYXZ.YXBZ FROM  BDCK.BDCS_DYXZ DYXZ WHERE DYXZ.BDCDYID='");
			strlimit.append(bdcdyid).append("'");
			List<Map> limitmap = dao.getDataListByFullSql(strlimit.toString());
			if(limitmap != null && limitmap.size() > 0){
				for(Map m : limitmap){
					if(m.get("YXBZ").toString()=="1"){
						zdinfo.put("XZZT","1");
						break;
					}
				}
			}
		}
	}
	
	private void addZDVector(Map<String,Object> map,String bdcdyid,BDCDYLX bdcdylx){
		
		String url_iserver_data=ConfigHelper.getNameByValue("URL_ISERVER_DATA");
		String filterWhere = "BDCDYID='" + bdcdyid + "' ";
		OperateFeature operateFeature=new OperateFeature(url_iserver_data);
		if("1".equals(ConfigHelper.getNameByValue("IS_ENABLE"))){
			//新版图形系统那套格式 ，（要求格式）
			com.supermap.realestate.gis.common.Feature[] features = operateFeature.queryFeatures_GIS("BDCDCK", bdcdylx.GZTableName.replaceAll("BDCS_", "BDCK_"), filterWhere);
			if (features == null || features.length < 0) {
				features = operateFeature.queryFeatures_GIS("BDCK", bdcdylx.XZTableName.replaceAll("BDCS_", "BDCK_"), filterWhere);
				if (features != null && features.length > 0) {
					com.supermap.realestate.gis.common.Feature feature = features[0];
					com.supermap.realestate.gis.common.Geometry geometry = feature.geometry;
					map.put("ZDVector", geometry);
			    }
		    }
	    }else if("0".equals(ConfigHelper.getNameByValue("IS_ENABLE"))){
	    	//iserver 那套格式   //目的：如果哪天 新版图形不好使了， 可以本地化配置切换成 不启用 ， 依然可以获取空间数据
	    	List<Feature> list_feature=operateFeature.queryFeatures_iServer("BDCDCK", bdcdylx.GZTableName.replaceAll("BDCS_", "BDCK_"), filterWhere);
			if(list_feature==null||list_feature.size()<=0){
				list_feature=operateFeature.queryFeatures_iServer("BDCK", bdcdylx.XZTableName.replaceAll("BDCS_", "BDCK_"), filterWhere);
			}
			if(list_feature!=null&&list_feature.size()>0){
				Feature feature=list_feature.get(0);
				Geometry ObjGeometry=feature.geometry;
				map.put("ZDVector", ObjGeometry);
			}
	    }
    }
	
	/**
     * 通过权证号精确获取信息
     * @param QZH  土地的权证号
     * @return Map<String,List<Map>> 
     */
	public Map<String,Object> getZDInfoByQZH(String QZH){
		Map<String,Object> map =  new HashMap<String,Object>();
	  /*List<BDCS_QL_XZ> ql_xz = dao.getDataList(BDCS_QL_XZ.class, " BDCQZH='"+QZH+"");
		 if(ql_xz!=null && ql_xz.size()>0){
			 BDCS_QL_XZ ql = ql_xz.get(0);
			 BDCS_SHYQZD_XZ  shyqzd_xz = dao.get(BDCS_SHYQZD_XZ.class, ql.getBDCDYID());
			 if(shyqzd_xz!=null){
				 zdinfo = fillZDInfo(shyqzd_xz);
			 }
		 }*/
		// 精确获取zdbdcdyid，zl，qlrmc
		/*StringBuilder listsql = new StringBuilder();
		listsql.append(" SELECT ZD.BDCDYID,ZD.ZL,QLR.QLRMC ")
		       .append(" FROM BDCK.BDCS_SHYQZD_XZ ZD LEFT JOIN BDCK.BDCS_QL_XZ QL ON ZD.BDCDYID = QL.BDCDYID LEFT JOIN BDCK.BDCS_QLR_XZ QLR ON QL.QLID = QLR.QLID ")
		       .append(" WHERE QL.BDCQZH='")
		       .append(QZH).append("'");
		list = dao.getDataListByFullSql(listsql.toString());
		map.put("List", list);*/
		 List<BDCS_QL_XZ> qllist = dao.getDataList(BDCS_QL_XZ.class	, " BDCQZH ='"+QZH+"'");
		 if(qllist!=null && qllist.size()>0){
			 BDCS_QL_XZ ql = qllist.get(0);
			 if(ql.getBDCDYID()!=null){
				 map = getDealDetails("",ql.getBDCDYID());//仅用id即可
			 }
		 }
	
		return map;
	}
}
