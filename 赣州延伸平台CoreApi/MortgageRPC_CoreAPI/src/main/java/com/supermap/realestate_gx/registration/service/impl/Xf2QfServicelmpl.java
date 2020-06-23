package com.supermap.realestate_gx.registration.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_H_LS;
import com.supermap.realestate.registration.model.BDCS_H_LSY;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_LS;
import com.supermap.realestate.registration.model.BDCS_ZRZ_LSY;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZY;
import com.supermap.realestate.registration.model.YC_SC_H_LS;
import com.supermap.realestate.registration.model.YC_SC_H_XZ;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate_gx.registration.service.Xf2QfService;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;

@Service("xf2qfService")
public class Xf2QfServicelmpl implements Xf2QfService {
	@Autowired
	private CommonDao baseCommonDao;
	private static Logger logger = Logger.getLogger(GX_ServiceImpl.class);//用log4j写日志
	
	/**
	 * liangc现房转换回期房
	 */
	@Override
	public Message putxf2qf(String bdcdyid,String bdcdyh) {
		Message msg = new Message();
		BDCS_H_XZ xzh = baseCommonDao.get(BDCS_H_XZ.class, bdcdyid);
		BDCS_H_LS lsh = baseCommonDao.get(BDCS_H_LS.class, bdcdyid);
		List<Map> xzyh = baseCommonDao.getDataListByFullSql(" select * from bdck.bdcs_h_xzy where zl='"+StringHelper.formatObject(xzh.getZL())+"'");
		List<Map> lsyh = baseCommonDao.getDataListByFullSql(" select * from bdck.bdcs_h_lsy where zl='"+StringHelper.formatObject(lsh.getZL())+"'");
		BDCS_H_XZY  bdcs_h_xzy = new BDCS_H_XZY();
		BDCS_H_LSY  bdcs_h_lsy = new BDCS_H_LSY();
		String ycbdcdyid = SuperHelper.GeneratePrimaryKey().toString();
		String yczrzbdcdyid = SuperHelper.GeneratePrimaryKey().toString();
		String ycdjdy = SuperHelper.GeneratePrimaryKey().toString();
		String scycid = SuperHelper.GeneratePrimaryKey().toString();
		String zdh = "";
		String ychbdcdyh="";
		String yczrzcbdcdyh="";
		List<Map> xzyzrz_0 = null;
		List<Map> xzyzrz_1 = null;
		if(xzh.getZDBDCDYID() != null){
			BDCS_SHYQZD_XZ xzzd = baseCommonDao.get(BDCS_SHYQZD_XZ.class, (String)xzh.getZDBDCDYID());
			if(xzzd.getBDCDYH() != null){
				zdh = xzzd.getBDCDYH().substring(0, 19);
			}
			if(xzh.getBDCDYH() !=null&&xzh.getBDCDYH() == null){
				zdh = xzh.getBDCDYH().substring(0, 19);
			}
			
			xzyzrz_1 = baseCommonDao.getDataListByFullSql(" select * from bdck.bdcs_zrz_xz where bdcdyid='"+StringHelper.formatObject(xzh.getZRZBDCDYID())+"'");
			xzyzrz_0 = baseCommonDao.getDataListByFullSql(" select * from bdck.bdcs_zrz_xzy where zl='"+StringHelper.formatObject(xzyzrz_1.get(0).get("ZL"))+"'");
			if(xzyzrz_0.size()>0&&xzyzrz_0.get(0).get("BDCDYH")!=null){
				yczrzcbdcdyh = xzyzrz_0.get(0).get("BDCDYH").toString();
			}else{
				yczrzcbdcdyh = ProjectHelper.CreatBDCDYH(zdh,"03");
			}
			if(!StringHelper.isEmpty(yczrzcbdcdyh)&&yczrzcbdcdyh.length()>25){
				zdh = yczrzcbdcdyh.substring(0, 19);
				ychbdcdyh =yczrzcbdcdyh.substring(0, 24)+StringHelper.PadLeft( ProjectHelper.getMaxnum(zdh,"04"), 4, '0');
			}
		}
		if(xzh.getZRZBDCDYID() != null&&!(xzyzrz_0.size()>0)){
			BDCS_ZRZ_XZ xzzrz = baseCommonDao.get(BDCS_ZRZ_XZ.class,(String)xzh.getZRZBDCDYID());
			BDCS_ZRZ_XZY  bdcs_zrz_xzy = new BDCS_ZRZ_XZY();
			if(xzzrz != null){
				ObjectHelper.copyObject(xzzrz, bdcs_zrz_xzy);
				bdcs_zrz_xzy.setYCJZMJ(xzzrz.getSCJZMJ());
				bdcs_zrz_xzy.setDJZT("01");
				bdcs_zrz_xzy.setId(yczrzbdcdyid);
				bdcs_zrz_xzy.setBDCDYH(yczrzcbdcdyh);
				baseCommonDao.save(bdcs_zrz_xzy);
				baseCommonDao.flush(); 
			}
			BDCS_ZRZ_LS lszrz = baseCommonDao.get(BDCS_ZRZ_LS.class,(String)xzh.getZRZBDCDYID());
			BDCS_ZRZ_LSY  bdcs_zrz_lsy = new BDCS_ZRZ_LSY();
			if(lszrz != null){
				ObjectHelper.copyObject(lszrz, bdcs_zrz_lsy);
				bdcs_zrz_lsy.setYCJZMJ(lszrz.getSCJZMJ());
				bdcs_zrz_lsy.setDJZT("01");
				bdcs_zrz_lsy.setId(yczrzbdcdyid);
				bdcs_zrz_lsy.setBDCDYH(yczrzcbdcdyh);
				baseCommonDao.save(bdcs_zrz_lsy);
				baseCommonDao.flush(); 
			}
		}
		if(((xzyh!=null&&xzyh.size()>0)&&(lsyh!=null&&lsyh.size()>0))||(xzyh!=null&&xzyh.size()>0)){
			msg.setMsg("该单元已有期房或已转为期房"+xzh.getBDCDYH()+" "+xzh.getZL());
			msg.setSuccess("false");
		}else{
			if(xzh != null){
				ObjectHelper.copyObject(xzh, bdcs_h_xzy);
				bdcs_h_xzy.setYCDXBFJZMJ(xzh.getSCDXBFJZMJ());
				bdcs_h_xzy.setYCFTJZMJ(xzh.getSCFTJZMJ());
				bdcs_h_xzy.setYCFTXS(xzh.getSCFTXS());
				bdcs_h_xzy.setYCJZMJ(xzh.getSCJZMJ());
				bdcs_h_xzy.setYCQTJZMJ(xzh.getSCQTJZMJ());
				bdcs_h_xzy.setYCTNJZMJ(xzh.getSCTNJZMJ());
				bdcs_h_xzy.setSCDXBFJZMJ(0.0);
				bdcs_h_xzy.setSCFTJZMJ(0.0);
				bdcs_h_xzy.setSCFTXS(0.0);
				bdcs_h_xzy.setSCJZMJ(0.0);
				bdcs_h_xzy.setSCQTJZMJ(0.0);
				bdcs_h_xzy.setSCTNJZMJ(0.0);
				bdcs_h_xzy.setId(ycbdcdyid);
				if(xzyzrz_0.size()>0){
					bdcs_h_xzy.setZRZBDCDYID(StringHelper.formatObject(xzyzrz_0.get(0).get("BDCDYID")));
				}else{
					bdcs_h_xzy.setZRZBDCDYID(yczrzbdcdyid);
				}
				bdcs_h_xzy.setBDCDYH(ychbdcdyh);
				baseCommonDao.save(bdcs_h_xzy);
				baseCommonDao.flush(); 
				
				BDCS_DJDY_XZ djdyxz = baseCommonDao.get(BDCS_DJDY_XZ.class, bdcdyid);
				BDCS_DJDY_XZ djdyycxz = new BDCS_DJDY_XZ();
				if(djdyxz != null){
					ObjectHelper.copyObject(djdyxz, djdyycxz);
					djdyycxz.setId(ycdjdy);
					djdyycxz.setBDCDYID(ycbdcdyid);
					djdyycxz.setLY("04");
					baseCommonDao.save(djdyycxz);
					baseCommonDao.flush(); 
				}
				
				YC_SC_H_XZ qxglxz = new YC_SC_H_XZ();
				qxglxz.setId(scycid);
				qxglxz.setSCBDCDYID(bdcdyid);
				qxglxz.setYCBDCDYID(ycbdcdyid);
				baseCommonDao.save(qxglxz);
				
				
				//baseCommonDao.delete(BDCS_H_XZ.class, bdcdyid);
				baseCommonDao.flush(); 
			}
			if(lsh != null){
				ObjectHelper.copyObject(lsh, bdcs_h_lsy);
				bdcs_h_lsy.setYCDXBFJZMJ(lsh.getSCDXBFJZMJ());
				bdcs_h_lsy.setYCFTJZMJ(lsh.getSCFTJZMJ());
				bdcs_h_lsy.setYCFTXS(lsh.getSCFTXS());
				bdcs_h_lsy.setYCJZMJ(lsh.getSCJZMJ());
				bdcs_h_lsy.setYCQTJZMJ(lsh.getSCQTJZMJ());
				bdcs_h_lsy.setYCTNJZMJ(lsh.getSCTNJZMJ());
				bdcs_h_lsy.setSCDXBFJZMJ(0.0);
				bdcs_h_lsy.setSCFTJZMJ(0.0);
				bdcs_h_lsy.setSCFTXS(0.0);
				bdcs_h_lsy.setSCJZMJ(0.0);
				bdcs_h_lsy.setSCQTJZMJ(0.0);
				bdcs_h_lsy.setSCTNJZMJ(0.0);
				bdcs_h_lsy.setId(ycbdcdyid);
				if(xzyzrz_0.size()>0){
					bdcs_h_lsy.setZRZBDCDYID(StringHelper.formatObject(xzyzrz_0.get(0).get("BDCDYID")));
				}else{
					bdcs_h_lsy.setZRZBDCDYID(yczrzbdcdyid);
				}
				bdcs_h_lsy.setBDCDYH(ychbdcdyh);
				baseCommonDao.save(bdcs_h_lsy);
				baseCommonDao.flush(); 
				
				BDCS_DJDY_LS djdyls = baseCommonDao.get(BDCS_DJDY_LS.class, bdcdyid);
				BDCS_DJDY_LS djdyycls = new BDCS_DJDY_LS();
				if(djdyls != null){
					ObjectHelper.copyObject(djdyls, djdyycls);
					djdyycls.setId(ycdjdy);
					djdyycls.setBDCDYID(ycbdcdyid);
					djdyycls.setLY("04");
					baseCommonDao.save(djdyycls);
					baseCommonDao.flush();
				}
				
				YC_SC_H_LS qxglls = new YC_SC_H_LS();
				qxglls.setId(scycid);
				qxglls.setSCBDCDYID(bdcdyid);
				qxglls.setYCBDCDYID(ycbdcdyid);
				baseCommonDao.save(qxglls);
				baseCommonDao.flush(); 
			}
			msg.setMsg("转换成功");
			msg.setSuccess("true");
		}
		
		return msg;
	}
	
	/**
	 * 查询需要转换的现房liangc
	 */
	@Override
	public Message getxf(Map<String, String> queryvalues, Integer page,
			Integer rows) {
		Message msg = new Message();
		long count = 0;
		StringBuilder builderWhereW = new StringBuilder();
		StringBuilder builderSelectS = new StringBuilder();
		String bdcdyh = "";
		String zl = "";
		String zrzzrzh = "";
		String zrzbdcdyh = "";
		String zrzzl = "";
		for (Entry<String, String> ent : queryvalues.entrySet()) {
			String name = ent.getKey();
			String value = ent.getValue();
			if (StringHelper.isEmpty(value)){
				builderWhereW.append("");
			}
			if (!StringHelper.isEmpty(value)){
				if (name.equals("BDCDYH")){
					bdcdyh = ent.getValue();
					try {
						bdcdyh =  new String(bdcdyh.getBytes("iso8859-1"), "utf-8");
					} catch (UnsupportedEncodingException e) {
						bdcdyh = "";
					}
					builderWhereW.append(" and  H.BDCDYH like '%" + bdcdyh + "%'");
				}
				if (name.equals("ZL")){
					zl = ent.getValue();
					try {
						zl =  new String(zl.getBytes("iso8859-1"), "utf-8");
					} catch (UnsupportedEncodingException e) {
						zl = "";
					}
					builderWhereW.append(" and  H.ZL like '%" + zl + "%'");
				}
				if (name.equals("ZRZZRZH")){
					zrzzrzh = ent.getValue();
					try {
						zrzzrzh =  new String(zrzzrzh.getBytes("iso8859-1"), "utf-8");
					} catch (UnsupportedEncodingException e) {
						zrzzrzh= "";
					}
					builderWhereW.append(" and  ZRZ.ZRZH like '%" + zrzzrzh + "%'");
				}
				if (name.equals("ZRZBDCDYH")){
					zrzbdcdyh = ent.getValue();
					try {
						zrzbdcdyh =  new String(zrzbdcdyh.getBytes("iso8859-1"), "utf-8");
					} catch (UnsupportedEncodingException e) {
						zrzbdcdyh= "";
					}
					builderWhereW.append(" and  ZRZ.BDCDYH like '%" + zrzbdcdyh + "%'");
				}
				if (name.equals("ZRZZL")){
					zrzzl = ent.getValue();
					try {
						zrzzl =  new String(zrzzl.getBytes("iso8859-1"), "utf-8");
					} catch (UnsupportedEncodingException e) {
						zrzzl= "";
					}
					builderWhereW.append(" and  ZRZ.ZL like '%" + zrzzl + "%'");
				}
			}
		}
		String fromSql = " from BDCK.BDCS_H_XZ H left join BDCK.BDCS_ZRZ_XZ ZRZ ON H.ZRZBDCDYID=ZRZ.BDCDYID "
				+ " where 1=1 "
				+ builderWhereW.toString();
		builderSelectS.append("select h.*,zrz.zrzh as zrzzrzh,zrz.bdcdyh as zrzbdcdyh,zrz.zl as zrzzl ");
		String fullSql = builderSelectS.toString() + fromSql;
		count = baseCommonDao.getCountByFullSql("from ("+fullSql+") t");
		List<Map> Result = new ArrayList<Map>();
		Result= baseCommonDao.getPageDataByFullSql(fullSql, page, rows);
		/*if(Result==null||Result.size()==0){
			msg.setMsg("该单元不是现房或已转为期房");
			msg.setSuccess("false");
		}*/
		msg.setTotal(count);
		msg.setRows(Result);
		return msg;
	}
	
	/**
	 * liangc现房批量转换回期房
	 */
	@Override
	public Message plputxf2qf(String bdcdyids) {
		Message msg = new Message();
		BDCS_H_XZY  bdcs_h_xzy = null;
		BDCS_H_LSY  bdcs_h_lsy = null;
		StringBuilder  msgs = new StringBuilder();
		if (StringHelper.isEmpty(bdcdyids)) {
			msg.setMsg("不动产单元id不能为空！");
			return msg;
		}
		String[] ids = bdcdyids.split(",");
		String bdcdyid_0 = ids[0];
		String yczrzcbdcdyh="";
		String zdh = "";
		String yczrzbdcdyid = SuperHelper.GeneratePrimaryKey().toString();
		String ychbdcdyh="";
		BDCS_H_XZ xzh_0 = baseCommonDao.get(BDCS_H_XZ.class, bdcdyid_0);
		List<Map> xzyzrz_0 = null;
		List<Map> xzyzrz_1 = null;
		if(xzh_0.getZDBDCDYID() != null){
			BDCS_SHYQZD_XZ xzzd_0 = baseCommonDao.get(BDCS_SHYQZD_XZ.class, (String)xzh_0.getZDBDCDYID());
			if(xzzd_0.getBDCDYH() != null){
				zdh = xzzd_0.getBDCDYH().substring(0, 19);
			}
			if(xzh_0.getBDCDYH() !=null&&xzzd_0.getBDCDYH() == null){
				zdh = xzh_0.getBDCDYH().substring(0, 19);
			}
			xzyzrz_1 = baseCommonDao.getDataListByFullSql(" select * from bdck.bdcs_zrz_xz where bdcdyid='"+StringHelper.formatObject(xzh_0.getZRZBDCDYID())+"'");
			xzyzrz_0 = baseCommonDao.getDataListByFullSql(" select * from bdck.bdcs_zrz_xzy where zl='"+StringHelper.formatObject(xzyzrz_1.get(0).get("ZL"))+"'");
			if(xzyzrz_0.size()>0&&xzyzrz_0.get(0).get("BDCDYH")!=null){
				yczrzcbdcdyh = xzyzrz_0.get(0).get("BDCDYH").toString();
			}else{
				yczrzcbdcdyh = ProjectHelper.CreatBDCDYH(zdh,"03");
			}
		}
		if(xzh_0.getZRZBDCDYID() != null&&!(xzyzrz_0.size()>0)){
			BDCS_ZRZ_XZ xzzrz = baseCommonDao.get(BDCS_ZRZ_XZ.class,(String)xzh_0.getZRZBDCDYID());
			BDCS_ZRZ_XZY  bdcs_zrz_xzy = new BDCS_ZRZ_XZY();
			if(xzzrz != null){
				ObjectHelper.copyObject(xzzrz, bdcs_zrz_xzy);
				bdcs_zrz_xzy.setYCJZMJ(xzzrz.getSCJZMJ());
				bdcs_zrz_xzy.setDJZT("01");
				bdcs_zrz_xzy.setId(yczrzbdcdyid);
				bdcs_zrz_xzy.setBDCDYH(yczrzcbdcdyh);
				baseCommonDao.save(bdcs_zrz_xzy);
				baseCommonDao.flush(); 
			}
			BDCS_ZRZ_LS lszrz = baseCommonDao.get(BDCS_ZRZ_LS.class,(String)xzh_0.getZRZBDCDYID());
			BDCS_ZRZ_LSY  bdcs_zrz_lsy = new BDCS_ZRZ_LSY();
			if(lszrz != null){
				ObjectHelper.copyObject(lszrz, bdcs_zrz_lsy);
				bdcs_zrz_lsy.setYCJZMJ(lszrz.getSCJZMJ());
				bdcs_zrz_lsy.setDJZT("01");
				bdcs_zrz_lsy.setId(yczrzbdcdyid);
				bdcs_zrz_lsy.setBDCDYH(yczrzcbdcdyh);
				baseCommonDao.save(bdcs_zrz_lsy);
				baseCommonDao.flush(); 
			}
		}
		for (String bdcdyid : ids) {
			if (StringHelper.isEmpty(bdcdyid)) {
				continue;
			}
		
		BDCS_H_XZ xzh = baseCommonDao.get(BDCS_H_XZ.class, bdcdyid);
		BDCS_H_LS lsh = baseCommonDao.get(BDCS_H_LS.class, bdcdyid);
		List<Map> xzyh = baseCommonDao.getDataListByFullSql(" select * from bdck.bdcs_h_xzy where zl='"+StringHelper.formatObject(xzh.getZL())+"'");
		List<Map> lsyh = baseCommonDao.getDataListByFullSql(" select * from bdck.bdcs_h_lsy where zl='"+StringHelper.formatObject(lsh.getZL())+"'");
		bdcs_h_xzy = new BDCS_H_XZY();
		bdcs_h_lsy = new BDCS_H_LSY();
		String ycbdcdyid = SuperHelper.GeneratePrimaryKey().toString();
		String ycdjdy = SuperHelper.GeneratePrimaryKey().toString();
		String scycid = SuperHelper.GeneratePrimaryKey().toString();
	
		if(!StringHelper.isEmpty(yczrzcbdcdyh)&&yczrzcbdcdyh.length()>25){
			zdh = yczrzcbdcdyh.substring(0, 19);
			ychbdcdyh =yczrzcbdcdyh.substring(0, 24)+StringHelper.PadLeft( ProjectHelper.getMaxnum(zdh,"04"), 4, '0');
		}
		if(((xzyh.size()>0&&xzyh!=null)&&(lsyh.size()>0&&lsyh!=null))||(xzyh.size()>0&&xzyh!=null)){
			/*if(((xzyh.size()>0&&xzyh!=null)&&(lsyh.size()>0&&lsyh!=null))||(xzyh.size()>0&&xzyh!=null)){
				msgs.append("该单元已有期房或已转为期房"+xzh.getBDCDYH()+" "+xzh.getZL());
				//msg.setMsg("该单元已有期房或已转为期房"+xzh.getBDCDYH()+" "+xzh.getZL());
				msg.setSuccess("false");
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
			}*/
			continue;
		}else{
			if(xzh != null){
				ObjectHelper.copyObject(xzh, bdcs_h_xzy);
				bdcs_h_xzy.setYCDXBFJZMJ(xzh.getSCDXBFJZMJ());
				bdcs_h_xzy.setYCFTJZMJ(xzh.getSCFTJZMJ());
				bdcs_h_xzy.setYCFTXS(xzh.getSCFTXS());
				bdcs_h_xzy.setYCJZMJ(xzh.getSCJZMJ());
				bdcs_h_xzy.setYCQTJZMJ(xzh.getSCQTJZMJ());
				bdcs_h_xzy.setYCTNJZMJ(xzh.getSCTNJZMJ());
				bdcs_h_xzy.setSCDXBFJZMJ(0.0);
				bdcs_h_xzy.setSCFTJZMJ(0.0);
				bdcs_h_xzy.setSCFTXS(0.0);
				bdcs_h_xzy.setSCJZMJ(0.0);
				bdcs_h_xzy.setSCQTJZMJ(0.0);
				bdcs_h_xzy.setSCTNJZMJ(0.0);
				bdcs_h_xzy.setId(ycbdcdyid);
				if(xzyzrz_0.size()>0){
					bdcs_h_xzy.setZRZBDCDYID(StringHelper.formatObject(xzyzrz_0.get(0).get("BDCDYID")));
				}else{
					bdcs_h_xzy.setZRZBDCDYID(yczrzbdcdyid);
				}
				bdcs_h_xzy.setBDCDYH(ychbdcdyh);
				baseCommonDao.save(bdcs_h_xzy);
				baseCommonDao.flush(); 
				
				
				
				BDCS_DJDY_XZ djdyxz = baseCommonDao.get(BDCS_DJDY_XZ.class, bdcdyid);
				BDCS_DJDY_XZ djdyycxz = new BDCS_DJDY_XZ();
				if(djdyxz != null){
					ObjectHelper.copyObject(djdyxz, djdyycxz);
					djdyycxz.setId(ycdjdy);
					djdyycxz.setBDCDYID(ycbdcdyid);
					djdyycxz.setLY("04");
					baseCommonDao.save(djdyycxz);
					baseCommonDao.flush(); 
				}
				
				YC_SC_H_XZ qxglxz = new YC_SC_H_XZ();
				qxglxz.setId(scycid);
				qxglxz.setSCBDCDYID(bdcdyid);
				qxglxz.setYCBDCDYID(ycbdcdyid);
				baseCommonDao.save(qxglxz);
				baseCommonDao.flush(); 
			}
			if(lsh != null){
				ObjectHelper.copyObject(lsh, bdcs_h_lsy);
				bdcs_h_lsy.setYCDXBFJZMJ(lsh.getSCDXBFJZMJ());
				bdcs_h_lsy.setYCFTJZMJ(lsh.getSCFTJZMJ());
				bdcs_h_lsy.setYCFTXS(lsh.getSCFTXS());
				bdcs_h_lsy.setYCJZMJ(lsh.getSCJZMJ());
				bdcs_h_lsy.setYCQTJZMJ(lsh.getSCQTJZMJ());
				bdcs_h_lsy.setYCTNJZMJ(lsh.getSCTNJZMJ());
				bdcs_h_lsy.setSCDXBFJZMJ(0.0);
				bdcs_h_lsy.setSCFTJZMJ(0.0);
				bdcs_h_lsy.setSCFTXS(0.0);
				bdcs_h_lsy.setSCJZMJ(0.0);
				bdcs_h_lsy.setSCQTJZMJ(0.0);
				bdcs_h_lsy.setSCTNJZMJ(0.0);
				bdcs_h_lsy.setId(ycbdcdyid);
				if(xzyzrz_0.size()>0){
					bdcs_h_lsy.setZRZBDCDYID(StringHelper.formatObject(xzyzrz_0.get(0).get("BDCDYID")));
				}else{
					bdcs_h_lsy.setZRZBDCDYID(yczrzbdcdyid);
				}
				bdcs_h_lsy.setBDCDYH(ychbdcdyh);
				baseCommonDao.save(bdcs_h_lsy);
				baseCommonDao.flush(); 
				
				
				
				BDCS_DJDY_LS djdyls = baseCommonDao.get(BDCS_DJDY_LS.class, bdcdyid);
				BDCS_DJDY_LS djdyycls = new BDCS_DJDY_LS();
				if(djdyls != null){
					ObjectHelper.copyObject(djdyls, djdyycls);
					djdyycls.setId(ycdjdy);
					djdyycls.setBDCDYID(ycbdcdyid);
					djdyycls.setLY("04");
					baseCommonDao.save(djdyycls);
					baseCommonDao.flush();
				}
				
				YC_SC_H_LS qxglls = new YC_SC_H_LS();
				qxglls.setId(scycid);
				qxglls.setSCBDCDYID(bdcdyid);
				qxglls.setYCBDCDYID(ycbdcdyid);
				baseCommonDao.save(qxglls);
				baseCommonDao.flush(); 
			}
			
			msg.setMsg("转换成功");
			msg.setSuccess("true");
			
		}
		
	 }
		msg.setMsg(msgs.toString());
		return msg;
	}
}
