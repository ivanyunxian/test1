package com.supermap.realestate.registration.maintain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.supermap.realestate.registration.maintain.Unit.Holder;
import com.supermap.realestate.registration.maintain.Unit.RightClass;
import com.supermap.realestate.registration.maintain.Unit.Value;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_MAINTAINUNIT;
import com.supermap.realestate.registration.model.BDCS_QDZR_GZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_LS;
import com.supermap.realestate.registration.model.BDCS_QDZR_XZ;
import com.supermap.realestate.registration.model.BDCS_TDYT_GZ;
import com.supermap.realestate.registration.model.BDCS_TDYT_LS;
import com.supermap.realestate.registration.model.BDCS_TDYT_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_LS;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.interfaces.TDYT;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

public class HisInfoMaintenanceTools {

	private static com.supermap.wisdombusiness.framework.dao.impl.CommonDao commonDao;
	static {
		if (commonDao == null) {
			WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
			commonDao = wac.getBean(CommonDao.class);
		}
	}

	/*
	 * 数据维护统一入口
	 */
	public static void MaintenanceInfo(Unit unitinfo,BDCS_XMXX xmxx) {
		Value baseinfo = unitinfo.baseinfo;
		String bdcdylx = getValueFromMapByName("BDCDYLX", baseinfo.newvalue);
		if(StringHelper.isEmpty(bdcdylx)){
			return;
		}
		String bdcdyid = getValueFromMapByName("BDCDYID", baseinfo.newvalue);
		if(StringHelper.isEmpty(bdcdyid)){
			return;
		}
		if(BDCDYLX.SHYQZD.Value.equals(bdcdylx)){
			List<String> list_buildingid=unitinfo.buildingids;
			if(list_buildingid!=null&&list_buildingid.size()>0){
				for(String buildingid:list_buildingid){
					try {
						commonDao.excuteQueryNoResult("UPDATE BDCK.BDCS_ZRZ_XZ SET ZDBDCDYID='"+bdcdyid+"' WHERE BDCDYID='"+buildingid+"'");
						commonDao.excuteQueryNoResult("UPDATE BDCK.BDCS_ZRZ_LS SET ZDBDCDYID='"+bdcdyid+"' WHERE BDCDYID='"+buildingid+"'");
						commonDao.excuteQueryNoResult("UPDATE BDCK.BDCS_ZRZ_GZ SET ZDBDCDYID='"+bdcdyid+"' WHERE BDCDYID='"+buildingid+"'");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}else if(BDCDYLX.ZRZ.Value.equals(bdcdylx)){
			List<String> list_houseid=unitinfo.buildingids;
			if(list_houseid!=null&&list_houseid.size()>0){
				for(String houseid:list_houseid){
					try {
						commonDao.excuteQueryNoResult("UPDATE BDCK.BDCS_H_XZ SET ZRZBDCDYID='"+bdcdyid+"' WHERE BDCDYID='"+houseid+"'");
						commonDao.excuteQueryNoResult("UPDATE BDCK.BDCS_H_LS SET ZRZBDCDYID='"+bdcdyid+"' WHERE BDCDYID='"+houseid+"'");
						commonDao.excuteQueryNoResult("UPDATE BDCK.BDCS_H_GZ SET ZRZBDCDYID='"+bdcdyid+"' WHERE BDCDYID='"+houseid+"'");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}else if(BDCDYLX.YCZRZ.Value.equals(bdcdylx)){
			List<String> list_houseid=unitinfo.buildingids;
			if(list_houseid!=null&&list_houseid.size()>0){
				for(String houseid:list_houseid){
					try {
						commonDao.excuteQuery("UPDATE BDCK.BDCS_H_XZY SET ZRZBDCDYID='"+bdcdyid+"' WHERE BDCDYID='"+houseid+"'");
						commonDao.excuteQuery("UPDATE BDCK.BDCS_H_LSY SET ZRZBDCDYID='"+bdcdyid+"' WHERE BDCDYID='"+houseid+"'");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		String operate_baseinfo = OperateBaseInfo(baseinfo);
		String djdyid=OperateDJDY(baseinfo);
		List<RightClass> list_right = unitinfo.rights;
		list_right=CorrectCertAndBdcdyhOnRights(list_right,unitinfo);
		List<RightClass> list_mortgage = unitinfo.mortgages;
		list_mortgage=CorrectCertAndBdcdyhOnRights(list_mortgage,unitinfo);
		List<RightClass> list_other = unitinfo.others;
		list_other=CorrectCertAndBdcdyhOnRights(list_other,unitinfo);
		List<RightClass> list_seal = unitinfo.seals;
		list_seal=CorrectCertAndBdcdyhOnRights(list_seal,unitinfo);
		if ("3".equals(operate_baseinfo)) {
			DeleteRights(list_right);
			DeleteRights(list_mortgage);
			DeleteRights(list_other);
			DeleteRights(list_seal);
		} else if ("1".equals(operate_baseinfo)) {
			AddRights(list_right,djdyid);
			AddRights(list_mortgage,djdyid);
			AddRights(list_other,djdyid);
			AddRights(list_seal,djdyid);
		} else if ("0".equals(operate_baseinfo) || "2".equals(operate_baseinfo)||StringHelper.isEmpty(operate_baseinfo)) {
			OperateRights(list_right,djdyid);
			OperateRights(list_mortgage,djdyid);
			OperateRights(list_other,djdyid);
			OperateRights(list_seal,djdyid);
		}
		BDCS_MAINTAINUNIT maintainunit=new BDCS_MAINTAINUNIT();
		if(unitinfo.baseinfo!=null&&unitinfo.baseinfo.oldvalue!=null){
			maintainunit.setLBDCDYH(getValueFromMapByName("BDCDYH",unitinfo.baseinfo.oldvalue));
			List<String> list_bdcqzh=new ArrayList<String>();
			List<String> list_cqr=new ArrayList<String>();
			maintainunit.setLZL(getValueFromMapByName("ZL",unitinfo.baseinfo.oldvalue));
			if(unitinfo.rights!=null&&unitinfo.rights.size()>0){
				RightClass right = unitinfo.rights.get(0);
				if(right.oldvalue.getHolders()!=null&&right.oldvalue.getHolders().size()>0){
					for(int i=0;i<right.oldvalue.getHolders().size();i++){
						Holder qlr=new Holder(right.oldvalue.getHolders().get(i));
						list_cqr.add(getValueFromMapByName("QLRMC",qlr));
						String bdcqzh=getValueFromMapByName("BDCQZH",qlr);
						if(!StringHelper.isEmpty(bdcqzh)&&!list_bdcqzh.contains(bdcqzh)){
							list_bdcqzh.add(bdcqzh);
						}
					}
				}
			}
			maintainunit.setLBDCQZH(StringHelper.formatList(list_bdcqzh, "、"));
			maintainunit.setLCQR(StringHelper.formatList(list_cqr, "、"));
		}
		if(unitinfo.baseinfo!=null&&unitinfo.baseinfo.newvalue!=null){
			maintainunit.setXBDCDYH(getValueFromMapByName("BDCDYH",unitinfo.baseinfo.newvalue));
			maintainunit.setXZL(getValueFromMapByName("ZL",unitinfo.baseinfo.newvalue));
			List<String> list_bdcqzh=new ArrayList<String>();
			List<String> list_cqr=new ArrayList<String>();
			maintainunit.setLZL(getValueFromMapByName("ZL",unitinfo.baseinfo.oldvalue));
			if(unitinfo.rights!=null&&unitinfo.rights.size()>0){
				RightClass right = unitinfo.rights.get(0);
				if(right.newvalue.getHolders()!=null&&right.newvalue.getHolders().size()>0){
					for(int i=0;i<right.newvalue.getHolders().size();i++){
						Holder qlr=new Holder(right.newvalue.getHolders().get(i));
						String operate_qlr=getValueFromMapByName("OPERATE",qlr);
						if("3".equals(operate_qlr)){
							continue;
						}
						list_cqr.add(getValueFromMapByName("QLRMC",qlr));
						String bdcqzh=getValueFromMapByName("BDCQZH",qlr);
						if(!StringHelper.isEmpty(bdcqzh)&&!list_bdcqzh.contains(bdcqzh)){
							list_bdcqzh.add(bdcqzh);
						}
					}
				}
			}
			maintainunit.setXBDCQZH(StringHelper.formatList(list_bdcqzh, "、"));
			maintainunit.setXCQR(StringHelper.formatList(list_cqr, "、"));
		}
		maintainunit.setXGR(xmxx.getSLRY());
		maintainunit.setXGRQ(xmxx.getSLSJ());
		maintainunit.setXMBH(xmxx.getId());
		
		commonDao.save(maintainunit);
		
		if(unitinfo.units!=null&&unitinfo.units.size()>0){
			for(Unit unitinfo_children:unitinfo.units){
				MaintenanceInfo(unitinfo_children,xmxx);
			}
		}
	}
	
	/******************************************单元***********************************/
	
	/******************************************维护证书******************************************/
	private static List<RightClass> CorrectCertAndBdcdyhOnRights(List<RightClass> list_right,Unit unitinfo){
		List<RightClass> list_right_new=new ArrayList<Unit.RightClass>();
		if(list_right!=null&&list_right.size()>0){
			for(RightClass right:list_right){
				RightClass right_new=CorrectCertOnRight(right,unitinfo);
				list_right_new.add(right_new);
			}
		}
		return list_right_new;
	}
	@SuppressWarnings("rawtypes")
	private static RightClass CorrectCertOnRight(RightClass right,Unit unitinfo){
		if(right.newvalue!=null){
			String qlid=getValueFromMapByName("QLID",right.newvalue);
			String operate_right=getValueFromMapByName("OPERATE",right.newvalue);
			String bdcdyh_right=getValueFromMapByName("BDCDYH",right.newvalue);
			if(unitinfo.baseinfo!=null&&unitinfo.baseinfo.newvalue!=null){
				String bdcdyh=getValueFromMapByName("BDCDYH",unitinfo.baseinfo.newvalue);
				if(!"3".equals(operate_right)){
					boolean b=false;
					if(StringHelper.isEmpty(bdcdyh_right)&&StringHelper.isEmpty(bdcdyh)){
						b=false;
					}else if(StringHelper.isEmpty(bdcdyh_right)&&!StringHelper.isEmpty(bdcdyh)){
						b=true;
					}else if(bdcdyh_right.equals(bdcdyh)){
						b=true;
					}
					if(b){
						right.newvalue.put("BDCDYH", bdcdyh);
						if(!"1".equals(operate_right)){
							right.newvalue.put("OPERATE", "2");
						}
					}
				}
			}
			List<Holder> list_holder=right.newvalue.getHolders();
			if(list_holder!=null&&list_holder.size()>0){
				List<String> list_bdcqzh=new ArrayList<String>();
				List<String> list_bdcqzhxh=new ArrayList<String>();
				List<Holder> list_holder_new=new ArrayList<Unit.Holder>();
				for(int i=0;i<list_holder.size();i++){
					Holder holder=new Holder(list_holder.get(i));
					String operate=getValueFromMapByName("OPERATE",holder);
					if(!"3".equals(operate)){
						String bdcqzh=getValueFromMapByName("BDCQZH",holder);
						String bdcqzhxh=getBDCQZHXH(bdcqzh);
						if(!"0".equals(operate)||StringHelper.isEmpty(getValueFromMapByName("BDCQZHXH",holder))){
							holder.put("BDCQZHXH", bdcqzhxh);
						}
						if(!list_bdcqzh.contains(bdcqzh)){
							list_bdcqzh.add(bdcqzh);
							list_bdcqzhxh.add(bdcqzhxh);
						}
					}
					list_holder_new.add(holder);
				}
				HashMap<String,String> zsid_bdcqzh=new HashMap<String, String>();
				List<Holder> list_holder_new2=new ArrayList<Unit.Holder>();
				if(list_bdcqzh.size()>1){
					right.newvalue.put("CZFS", "02");
					for(int i = 0;i<list_holder_new.size();i++){
						Holder holder=new Holder(list_holder_new.get(i));
						String operate=getValueFromMapByName("OPERATE",holder);
						String bdcqzh=getValueFromMapByName("BDCQZH",holder);
						if(!"3".equals(operate)){
							String zsid=SuperHelper.GeneratePrimaryKey();
							holder.put("ZSID", zsid);
							zsid_bdcqzh.put(zsid, bdcqzh);
						}
						list_holder_new2.add(holder);
					}
					
				}else{
					right.newvalue.put("CZFS", "01");
					String zsid=SuperHelper.GeneratePrimaryKey();
					for(int i = 0;i<list_holder_new.size();i++){
						Holder holder=new Holder(list_holder_new.get(i));
						String operate=getValueFromMapByName("OPERATE",holder);
						String bdcqzh=getValueFromMapByName("BDCQZH",holder);
						if(!"3".equals(operate)){
							holder.put("ZSID", zsid);
							if(zsid_bdcqzh.size()==0){
								zsid_bdcqzh.put(zsid, bdcqzh);
							}
						}
						list_holder_new2.add(holder);
					}
				}
				right.newvalue.setHolders(list_holder_new2);
				if(list_bdcqzh.contains("")){
					list_bdcqzh.remove("");
				}
				if(list_bdcqzhxh.contains("")){
					list_bdcqzhxh.remove("");
				}
				right.newvalue.put("BDCQZH", StringHelper.formatList(list_bdcqzh, ","));
				right.newvalue.put("BDCQZHXH", StringHelper.formatList(list_bdcqzhxh, ","));
				
				List<BDCS_ZS_XZ> certs_old=commonDao.getDataList(BDCS_ZS_XZ.class, " id in (SELECT ZSID FROM BDCS_QDZR_XZ WHERE QLID='"+qlid+"')");
				if(zsid_bdcqzh!=null&&zsid_bdcqzh.size()>0){
					Set set = zsid_bdcqzh.keySet();
					Iterator iterator = set.iterator();
					while (iterator.hasNext()) {
						Object obj = iterator.next();
						Object val = zsid_bdcqzh.get(obj);
						String zsid=StringHelper.formatObject(obj);
						String bdcqzh=StringHelper.formatObject(val);
						BDCS_ZS_XZ cert_src=null;
						if(certs_old!=null&&certs_old.size()>0){
							for(BDCS_ZS_XZ cert_old:certs_old){
								boolean b=false;
								if(StringHelper.isEmpty(bdcqzh)){
									if(StringHelper.isEmpty(cert_old.getBDCQZH())){
										b=true;
									}
								}else if(bdcqzh.equals(cert_old.getBDCQZH())){
									b=true;
								}
								if(b){
									cert_src=cert_old;
									break;
								}
							}
						}
						BDCS_ZS_XZ cert_new=new BDCS_ZS_XZ();
						if(cert_src!=null){
							cert_new.setCFDAGH(cert_src.getCFDAGH());
							cert_new.setSZSJ(cert_src.getSZSJ());
							cert_new.setXMBH(cert_src.getXMBH());
							cert_new.setZSBH(cert_src.getZSBH());
							cert_new.setZSDATA(cert_src.getZSDATA());
						}
						cert_new.setBDCQZH(bdcqzh);
						cert_new.setId(zsid);
						commonDao.save(cert_new);
						BDCS_ZS_LS cert_new_ls=new BDCS_ZS_LS();
						if(cert_src!=null){
							cert_new_ls.setCFDAGH(cert_src.getCFDAGH());
							cert_new_ls.setSZSJ(cert_src.getSZSJ());
							cert_new_ls.setXMBH(cert_src.getXMBH());
							cert_new_ls.setZSBH(cert_src.getZSBH());
							cert_new_ls.setZSDATA(cert_src.getZSDATA());
						}
						cert_new_ls.setBDCQZH(bdcqzh);
						cert_new_ls.setId(zsid);
						commonDao.save(cert_new_ls);
						Rights right_gz=RightsTools.loadRights(DJDYLY.GZ, qlid);
						if(right_gz!=null){
							BDCS_ZS_GZ cert_new_gz=new BDCS_ZS_GZ();
							if(cert_new_gz!=null){
								cert_new_gz.setCFDAGH(cert_src.getCFDAGH());
								cert_new_gz.setSZSJ(cert_src.getSZSJ());
								cert_new_gz.setXMBH(right_gz.getXMBH());
								cert_new_gz.setZSBH(cert_src.getZSBH());
								cert_new_gz.setZSDATA(cert_src.getZSDATA());
							}
							cert_new_gz.setBDCQZH(bdcqzh);
							cert_new_gz.setId(zsid);
							commonDao.save(cert_new_gz);
						}
					}
				}
				if(certs_old!=null&&certs_old.size()>0){
					for(BDCS_ZS_XZ cert_old:certs_old){
						commonDao.deleteEntity(cert_old);
					}
				}
				List<BDCS_ZS_LS> certs_old_ls=commonDao.getDataList(BDCS_ZS_LS.class, " id in (SELECT ZSID FROM BDCS_QDZR_LS WHERE QLID='"+qlid+"')");
				if(certs_old_ls!=null&&certs_old_ls.size()>0){
					for(BDCS_ZS_LS cert_old:certs_old_ls){
						commonDao.deleteEntity(cert_old);
					}
				}
				List<BDCS_ZS_GZ> certs_old_gz=commonDao.getDataList(BDCS_ZS_GZ.class, " id in (SELECT ZSID FROM BDCS_QDZR_GZ WHERE QLID='"+qlid+"')");
				if(certs_old_gz!=null&&certs_old_gz.size()>0){
					for(BDCS_ZS_GZ cert_old:certs_old_gz){
						commonDao.deleteEntity(cert_old);
					}
				}
			}
		}
		return right;
	}
	/******************************************维护证书******************************************/
	/*
	 * 维护单元信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static String OperateBaseInfo(Value baseinfo) {
		String operate = "";
		if (baseinfo != null) {
			String bdcdyid = getValueFromMapByName("BDCDYID", baseinfo.newvalue);
			String bdcdylx = getValueFromMapByName("BDCDYLX", baseinfo.newvalue);
			operate = getValueFromMapByName("OPERATE", baseinfo.newvalue);
			BDCDYLX lx=BDCDYLX.initFrom(bdcdylx);
			if("3".equals(operate)){
				if(BDCDYLX.SHYQZD.equals(lx)|| BDCDYLX.ZRZ.equals(lx)){
                	 String tablename= lx.getTableName(DJDYLY.GZ);
                 	tablename=tablename.replaceFirst("BDCS_", "BDCK_");
                 	String dataalias=realestateWebHelper.loadconfig("URL_ISERVER_DATAALIAS");//获取数据源别名
                 	
                 	//	ProjectHelper.deletegeo(bdcdyid, dataalias, tablename);
				}
			}
			else{
                 if(BDCDYLX.SHYQZD.equals(lx)|| BDCDYLX.ZRZ.equals(lx)){
                	 Map map=new HashMap<String, String>();
                	 map.put("BDCDYID", bdcdyid);
                	 String geo=getValueFromMapByName("geo", baseinfo.newvalue);
                	String tablename= lx.getTableName(DJDYLY.GZ);
                	tablename=tablename.replaceFirst("BDCS_", "BDCK_");
                	String dataalias=realestateWebHelper.loadconfig("URL_ISERVER_DATAALIAS");//获取数据源别名
                	// ProjectHelper.updategeo(geo, map, bdcdyid, dataalias, tablename);
				}				
			}
			if ("0".equals(operate)) {
			} else if ("1".equals(operate)) {
				RealUnit unit = UnitTools.newRealUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.XZ);
				StringHelper.setValue(baseinfo.newvalue, unit);
				unit.setId(bdcdyid);
				commonDao.save(unit);
				RealUnit unit_ls = UnitTools.newRealUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.LS);
				StringHelper.setValue(baseinfo.newvalue, unit_ls);
				unit_ls.setId(bdcdyid);
				commonDao.save(unit_ls);
			} else if ("2".equals(operate)) {
				boolean bchange = IsChange(baseinfo.newvalue, baseinfo.oldvalue);
				if (bchange) {
					RealUnit unit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.XZ, bdcdyid);
					if (unit != null) {
						StringHelper.setValue(baseinfo.newvalue, unit);
						commonDao.update(unit);
					}
					RealUnit unit_ls = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.LS, bdcdyid);
					if (unit_ls != null) {
						StringHelper.setValue(baseinfo.newvalue, unit_ls);
						commonDao.update(unit_ls);
					}
					RealUnit unit_gz = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.GZ, bdcdyid);
					if (unit_gz != null) {
						StringHelper.setValue(baseinfo.newvalue, unit_gz);
						commonDao.update(unit_gz);
					}
				}
			} else if ("3".equals(operate)) {
				RealUnit unit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.XZ, bdcdyid);
				if (unit != null) {
					commonDao.deleteEntity(unit);
				}
				commonDao.deleteEntitysByHql(BDCS_DJDY_XZ.class, "BDCDYID='" + bdcdyid + "' AND BDCDYLX='" + bdcdylx + "'");
				RealUnit unit_ls = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.LS, bdcdyid);
				if (unit_ls != null) {
					commonDao.deleteEntity(unit_ls);
				}
				commonDao.deleteEntitysByHql(BDCS_DJDY_LS.class, "BDCDYID='" + bdcdyid + "' AND BDCDYLX='" + bdcdylx + "'");
				RealUnit unit_gz = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.GZ, bdcdyid);
				if (unit_gz != null) {
					commonDao.deleteEntity(unit_gz);
				}
				commonDao.deleteEntitysByHql(BDCS_DJDY_GZ.class, "BDCDYID='" + bdcdyid + "' AND BDCDYLX='" + bdcdylx + "'");
			}
			
			List<Map<String, Object>> tdyts_add=new ArrayList<Map<String,Object>>();
			List<Map<String, Object>> tdyts_delete=new ArrayList<Map<String,Object>>();
			List<Map<String, Object>> tdyts_operate=new ArrayList<Map<String,Object>>();
			List<Map<String, Object>> tdyts=baseinfo.newvalue.getTdyts();
			if(tdyts!=null&&tdyts.size()>0){
				for(Map<String, Object> tdytinfo:tdyts){
					String tdyt_operate = getValueFromMapByName("OPERATE", tdytinfo);
					if("1".equals(tdyt_operate)){
						tdyts_add.add(tdytinfo);
					}else if("2".equals(tdyt_operate)){
						tdyts_operate.add(tdytinfo);
					}else if("3".equals(tdyt_operate)){
						tdyts_delete.add(tdytinfo);
					}
				}
			}
			addTDYTs(tdyts_add,bdcdyid);
			deleteTDYTs(tdyts_delete);
			operateTDYTs(tdyts_operate);
		}
		return operate;
	}
	/******************************************单元******************************************/
	
	/******************************************土地用途******************************************/
	private static void addTDYTs(List<Map<String, Object>> list_tdyt,String bdcdyid){
		if(list_tdyt!=null&&list_tdyt.size()>0){
			for(Map<String, Object> tdytinfo:list_tdyt){
				String id=SuperHelper.GeneratePrimaryKey();
				TDYT tdyt=UnitTools.newTDYTUnit(DJDYLY.XZ);
				StringHelper.setValue(tdytinfo, tdyt);
				tdyt.setId(id);
				tdyt.setBDCDYID(bdcdyid);
				commonDao.save(tdyt);
				TDYT tdyt_ls=UnitTools.newTDYTUnit(DJDYLY.LS);
				StringHelper.setValue(tdytinfo, tdyt_ls);
				tdyt_ls.setId(id);
				tdyt_ls.setBDCDYID(bdcdyid);
				commonDao.save(tdyt_ls);
				RealUnit unit=UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.GZ, bdcdyid);
				if(unit!=null){
					TDYT tdyt_gz=UnitTools.newTDYTUnit(DJDYLY.GZ);
					StringHelper.setValue(tdytinfo, tdyt_gz);
					tdyt_gz.setId(id);
					tdyt_gz.setBDCDYID(bdcdyid);
					commonDao.save(tdyt_gz);
				}
			}
		}
	}
	
	private static void deleteTDYTs(List<Map<String, Object>> list_tdyt){
		if(list_tdyt!=null&&list_tdyt.size()>0){
			for(Map<String, Object> tdytinfo:list_tdyt){
				String id=getValueFromMapByName("ID", tdytinfo);
				BDCS_TDYT_XZ tdyt=commonDao.get(BDCS_TDYT_XZ.class, id);
				if(tdyt!=null){
					commonDao.deleteEntity(tdyt);
				}
				
				BDCS_TDYT_LS tdyt_ls=commonDao.get(BDCS_TDYT_LS.class, id);
				if(tdyt_ls!=null){
					commonDao.deleteEntity(tdyt_ls);
				}
				
				BDCS_TDYT_GZ tdyt_gz=commonDao.get(BDCS_TDYT_GZ.class, id);
				if(tdyt_gz!=null){
					commonDao.deleteEntity(tdyt_gz);
				}
			}
		}
	}
	
	private static void operateTDYTs(List<Map<String, Object>> list_tdyt){
		if(list_tdyt!=null&&list_tdyt.size()>0){
			for(Map<String, Object> tdytinfo:list_tdyt){
				
				String id=getValueFromMapByName("ID", tdytinfo);
				BDCS_TDYT_XZ tdyt=commonDao.get(BDCS_TDYT_XZ.class, id);
				if(tdyt!=null){
					StringHelper.setValue(tdytinfo, tdyt);
					commonDao.update(tdyt);
				}
				BDCS_TDYT_LS tdyt_ls=commonDao.get(BDCS_TDYT_LS.class, id);
				if(tdyt_ls!=null){
					StringHelper.setValue(tdytinfo, tdyt_ls);
					commonDao.update(tdyt_ls);
				}
				BDCS_TDYT_GZ tdyt_gz=commonDao.get(BDCS_TDYT_GZ.class, id);
				if(tdyt_gz!=null){
					StringHelper.setValue(tdytinfo, tdyt_gz);
					commonDao.update(tdyt_gz);
				}
			}
		}
	}
	/******************************************土地用途******************************************/
	
	/******************************************登记单元******************************************/
	/*
	 * 维护单元信息
	 */
	private static String OperateDJDY(Value baseinfo) {
		String djdyid = "";
		if (baseinfo != null) {
			String bdcdyid = getValueFromMapByName("BDCDYID", baseinfo.newvalue);
			String bdcdylx = getValueFromMapByName("BDCDYLX", baseinfo.newvalue);
			List<BDCS_DJDY_XZ> djdys_xz=commonDao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='"+bdcdyid+"' AND BDCDYLX='"+bdcdylx+"'");
			if(djdys_xz!=null&&djdys_xz.size()>0){
				djdyid=djdys_xz.get(0).getDJDYID();
			}else{
				List<BDCS_DJDY_LS> djdys_ls=commonDao.getDataList(BDCS_DJDY_LS.class, "BDCDYID='"+bdcdyid+"' AND BDCDYLX='"+bdcdylx+"'");
				if(djdys_ls!=null&&djdys_ls.size()>0){
					djdyid=djdys_ls.get(0).getDJDYID();
				}
			}
			if(StringHelper.isEmpty(djdyid)){
				String bdcdyh = getValueFromMapByName("BDCDYH", baseinfo.newvalue);
				BDCS_DJDY_XZ djdy_xz=new BDCS_DJDY_XZ();
				djdy_xz.setBDCDYH(bdcdyh);
				djdy_xz.setBDCDYID(bdcdyid);
				djdy_xz.setBDCDYLX(bdcdylx);
				djdyid=SuperHelper.GeneratePrimaryKey();
				djdy_xz.setDJDYID(djdyid);
				commonDao.save(djdy_xz);
				
				BDCS_DJDY_LS djdy_ls=new BDCS_DJDY_LS();
				djdy_ls.setBDCDYH(bdcdyh);
				djdy_ls.setBDCDYID(bdcdyid);
				djdy_ls.setBDCDYLX(bdcdylx);
				djdy_ls.setDJDYID(djdyid);
				commonDao.save(djdy_ls);
			}
		}
		return djdyid;
	}
	/******************************************登记单元******************************************/
	
	/******************************************权利******************************************/
	/*
	 * 维护权利信息集合
	 */
	private static String OperateRights(List<RightClass> list_right,String djdyid) {
		if (list_right != null && list_right.size() > 0) {
			for (RightClass rightinfo : list_right) {
				String operate = OperateRight(rightinfo,djdyid);
				if ("0".equals(operate) || "2".equals(operate)||StringHelper.isEmpty(operate)) {
					List<Holder> list_holder = rightinfo.newvalue.getHolders();
					OperateHolers(list_holder, rightinfo,djdyid);
				}
			}
		}
		return "";
	}

	/*
	 * 维护权利信息
	 */
	@SuppressWarnings("rawtypes")
	private static String OperateRight(RightClass rightinfo,String djdyid) {
		String operate = "";
		if (rightinfo != null) {
			operate = getValueFromMapByName("OPERATE", rightinfo.newvalue);
			if ("0".equals(operate)) {
			} else if ("1".equals(operate)) {
				AddRight(rightinfo,djdyid, DJDYLY.XZ);
				AddRight(rightinfo,djdyid, DJDYLY.LS);
			} else if ("2".equals(operate)) {
				boolean bchange = IsChange(rightinfo.newvalue, rightinfo.oldvalue);
				if (bchange) {
					String qlid = getValueFromMapByName("QLID", rightinfo.newvalue);
					Rights right = RightsTools.loadRights(DJDYLY.XZ, qlid);
					if (right != null) {
						StringHelper.setValue(rightinfo.newvalue, right);
						commonDao.update(right);
					}
					Rights right_ls = RightsTools.loadRights(DJDYLY.LS, qlid);
					if (right_ls != null) {
						StringHelper.setValue(rightinfo.newvalue, right_ls);
						commonDao.update(right_ls);
					}
					Rights right_gz = RightsTools.loadRights(DJDYLY.GZ, qlid);
					if (right_gz != null) {
						StringHelper.setValue(rightinfo.newvalue, right_gz);
						commonDao.update(right_gz);
					}
				}
				Map fsql = rightinfo.newvalue.getFsql();
				String fsqlid="";
				if(fsql!=null){
					fsqlid = getValueFromMapByName("FSQLID", fsql);
				}
				if(StringHelper.isEmpty(fsqlid)){
					fsqlid=SuperHelper.GeneratePrimaryKey();
					String qlid = getValueFromMapByName("QLID", rightinfo.newvalue);
					SubRights subright_xz=RightsTools.newSubRights(DJDYLY.XZ);
					if(fsql!=null){
						StringHelper.setValue(fsql, subright_xz);
					}
					subright_xz.setDJDYID(djdyid);
					subright_xz.setQLID(qlid);
					subright_xz.setId(fsqlid);
					commonDao.save(subright_xz);
					
					SubRights subright_ls=RightsTools.newSubRights(DJDYLY.LS);
					if(fsql!=null){
						StringHelper.setValue(fsql, subright_ls);
					}
					subright_ls.setDJDYID(djdyid);
					subright_ls.setQLID(qlid);
					subright_ls.setId(fsqlid);
					commonDao.save(subright_ls);
				}else{
					SubRights subright = RightsTools.loadSubRights(DJDYLY.XZ, fsqlid);
					if (subright != null) {
						StringHelper.setValue(fsql, subright);
						subright.setDJDYID(djdyid);
						commonDao.update(subright);
					}
					SubRights subright_ls = RightsTools.loadSubRights(DJDYLY.LS, fsqlid);
					if (subright_ls != null) {
						StringHelper.setValue(fsql, subright_ls);
						subright_ls.setDJDYID(djdyid);
						commonDao.update(subright_ls);
					}
					SubRights subright_gz = RightsTools.loadSubRights(DJDYLY.GZ, fsqlid);
					if (subright_gz != null) {
						StringHelper.setValue(fsql, subright_gz);
						subright_gz.setDJDYID(djdyid);
						commonDao.update(subright_gz);
					}
				}
			} else if ("3".equals(operate)) {
				DeleteRight(rightinfo, DJDYLY.XZ);
				DeleteRight(rightinfo, DJDYLY.LS);
				DeleteRight(rightinfo, DJDYLY.GZ);
			}
		}
		return operate;
	}

	/*
	 * 删除权利集合
	 */
	private static void DeleteRights(List<RightClass> list_right) {
		if (list_right != null && list_right.size() > 0) {
			for (RightClass right : list_right) {
				DeleteRight(right, DJDYLY.XZ);
				DeleteRight(right, DJDYLY.LS);
				DeleteRight(right, DJDYLY.GZ);
			}
		}
	}

	/*
	 * 删除权利
	 */
	private static void DeleteRight(RightClass right, DJDYLY ly) {
		String qlid = getValueFromMapByName("QLID", right.newvalue);
		Rights right_xz = RightsTools.deleteRightsAll(ly, qlid);
		if (right_xz != null) {
			commonDao.deleteEntity(right_xz);
		}
	}

	/*
	 * 添加权利集合
	 */
	private static void AddRights(List<RightClass> list_right,String djdyid) {
		if (list_right != null && list_right.size() > 0) {
			for (RightClass rightinfo : list_right) {
				AddRight(rightinfo,djdyid, DJDYLY.XZ);
				AddRight(rightinfo,djdyid, DJDYLY.LS);
			}
		}
	}

	/*
	 * 添加权利
	 */
	private static void AddRight(RightClass rightinfo,String djdyid, DJDYLY ly) {
		Rights right = RightsTools.newRights(ly);
		String qlid = getValueFromMapByName("QLID", rightinfo.newvalue);
		StringHelper.setValue(rightinfo.newvalue, right);
		right.setId(qlid);
		right.setDJDYID(djdyid);
		
		Map<String, Object> subrightinfo = rightinfo.newvalue.getFsql();
		SubRights subright = RightsTools.newSubRights(ly);
		String fsqlid = getValueFromMapByName("FSQLID", subrightinfo);
		StringHelper.setValue(subrightinfo, subright);
		if(StringHelper.isEmpty(fsqlid)){
			subright.setId(fsqlid);
		}else{
			right.setFSQLID(subright.getId());
			subright.setQLID(qlid);
		}
		subright.setQLID(qlid);
		subright.setDJDYID(djdyid);
		commonDao.save(subright);
		commonDao.save(right);
		
		List<Holder> rightholders = rightinfo.newvalue.getHolders();
		AddRightHolders(rightholders,rightinfo, ly);
		AddQDZRs(rightholders, rightinfo,djdyid, ly);
	}
	/******************************************权利******************************************/
	
	/******************************************权利人******************************************/
	/*
	 * 添加权利人集合
	 */
	private static void AddRightHolders(List<Holder> list_rightholder,RightClass rightinfo, DJDYLY ly) {
		if (list_rightholder != null && list_rightholder.size() > 0) {
			for (int i = 0; i < list_rightholder.size(); i++) {
				Holder rightholderinfo = new Holder(list_rightholder.get(i));
				AddRightHolder(rightholderinfo,rightinfo, ly);
			}
		}
	}

	/*
	 * 添加权利人
	 */
	private static void AddRightHolder(Holder rightholderinfo,RightClass rightinfo, DJDYLY ly) {
		String qlid = getValueFromMapByName("QLID", rightinfo.newvalue);
		Rights right=RightsTools.loadRights(DJDYLY.GZ, qlid);
		if(DJDYLY.GZ.equals(ly)&&right==null){
			return;
		}
		RightsHolder rightholder = RightsHolderTools.newRightsHolder(ly);
		String qlrid = getValueFromMapByName("QLRID", rightholderinfo);
		StringHelper.setValue(rightholderinfo, rightholder);
		rightholder.setId(qlrid);
		rightholder.setQLID(qlid);
		if(DJDYLY.GZ.equals(ly)){
			rightholder.setXMBH(right.getXMBH());
		}
		commonDao.save(rightholder);
	}
	/******************************************权利人******************************************/
	
	/******************************************权地证人******************************************/
	/*
	 * 添加权地证人集合
	 */
	private static void AddQDZRs(List<Holder> list_rightholder, RightClass rightinfo,String djdyid, DJDYLY ly) {
		if (list_rightholder != null && list_rightholder.size() > 0) {
			for (int i = 0; i < list_rightholder.size(); i++) {
				Holder holderinfo = new Holder(list_rightholder.get(i));
				AddQDZR(holderinfo, rightinfo,djdyid, ly);
			}
		}
	}

	/*
	 * 添加权地证人
	 */
	private static void AddQDZR(Holder rightholderinfo, RightClass rightinfo,String djdyid, DJDYLY ly) {
		String bdcdyh = getValueFromMapByName("BDCDYH", rightinfo.newvalue);
		String qlid = getValueFromMapByName("QLID", rightinfo.newvalue);
		String fsqlid = getValueFromMapByName("FSQLID", rightinfo.newvalue);
		String qlrid = getValueFromMapByName("QLRID", rightholderinfo);
		String zsid = getValueFromMapByName("ZSID", rightholderinfo);
		String tenant_id = getValueFromMapByName("TENANT_ID", rightholderinfo);
		if(DJDYLY.XZ.equals(ly)){
			BDCS_QDZR_XZ qdzr = new BDCS_QDZR_XZ();
			qdzr.setBDCDYH(bdcdyh);
			qdzr.setDJDYID(djdyid);
			qdzr.setFSQLID(fsqlid);
			qdzr.setQLID(qlid);
			qdzr.setQLRID(qlrid);
			qdzr.setZSID(zsid);
			commonDao.save(qdzr);
		}else if(DJDYLY.GZ.equals(ly)){
			Rights right=RightsTools.loadRights(DJDYLY.GZ, qlid);
			if(DJDYLY.GZ.equals(ly)&&right==null){
				return;
			}
			BDCS_QDZR_GZ qdzr = new BDCS_QDZR_GZ();
			qdzr.setBDCDYH(bdcdyh);
			qdzr.setDJDYID(djdyid);
			qdzr.setFSQLID(fsqlid);
			qdzr.setQLID(qlid);
			qdzr.setQLRID(qlrid);
			qdzr.setZSID(zsid);
			qdzr.setXMBH(right.getXMBH());
			commonDao.save(qdzr);
		}else{
			BDCS_QDZR_LS qdzr = new BDCS_QDZR_LS();
			qdzr.setBDCDYH(bdcdyh);
			qdzr.setDJDYID(djdyid);
			qdzr.setFSQLID(fsqlid);
			qdzr.setQLID(qlid);
			qdzr.setQLRID(qlrid);
			qdzr.setZSID(zsid);
			commonDao.save(qdzr);
		}
	}
	
	/*
	 * 维护权利人集合
	 */
	private static void OperateHolers(List<Holder> list_holder, RightClass rightinfo,String djdyid) {
		List<Holder> list_holder_add = new ArrayList<Holder>();
		List<Holder> list_holder_delete = new ArrayList<Holder>();
		List<Holder> list_holder_operate = new ArrayList<Holder>();
		if (list_holder != null && list_holder.size() > 0) {
			for (int i = 0; i < list_holder.size(); i++) {
				Holder holderinfo = new Holder(list_holder.get(i));
				String operate = getValueFromMapByName("OPERATE", list_holder.get(i));
				if("1".equals(operate)){
					list_holder_add.add(holderinfo);
				}else if("3".equals(operate)){
					list_holder_delete.add(holderinfo);
				}else if("0".equals(operate)||"2".equals(operate)||StringHelper.isEmpty(operate)){
					list_holder_operate.add(holderinfo);
				}
			}
		}
		for (int i = 0; i < list_holder_add.size(); i++) {
			Holder holderinfo = new Holder(list_holder_add.get(i));
			AddRightHolder(holderinfo,rightinfo, DJDYLY.XZ);
			AddRightHolder(holderinfo,rightinfo, DJDYLY.LS);
			AddRightHolder(holderinfo,rightinfo, DJDYLY.GZ);

		}
		AddQDZRs(list_holder_add, rightinfo,djdyid, DJDYLY.XZ);
		AddQDZRs(list_holder_add, rightinfo,djdyid, DJDYLY.LS);
		AddQDZRs(list_holder_add, rightinfo,djdyid, DJDYLY.GZ);

		for (int i = 0; i < list_holder_delete.size(); i++) {
			Holder holderinfo = new Holder(list_holder_delete.get(i));
			String qlrid = getValueFromMapByName("QLRID", holderinfo);
			RightsHolderTools.deleteRightsHolder(DJDYLY.XZ, qlrid);
			RightsHolderTools.deleteRightsHolder(DJDYLY.LS, qlrid);
			RightsHolderTools.deleteRightsHolder(DJDYLY.GZ, qlrid);
			commonDao.deleteEntitysByHql(BDCS_QDZR_XZ.class, "QLRID='" + qlrid + "'");
			commonDao.deleteEntitysByHql(BDCS_QDZR_LS.class, "QLRID='" + qlrid + "'");
			commonDao.deleteEntitysByHql(BDCS_QDZR_GZ.class, "QLRID='" + qlrid + "'");
		}
		for (int i = 0; i < list_holder_operate.size(); i++) {
			Holder holderinfo = new Holder(list_holder_operate.get(i));
			String qlrid = getValueFromMapByName("QLRID", holderinfo);
			RightsHolder rightholder = RightsHolderTools.loadRightsHolder(DJDYLY.XZ, qlrid);
			if (rightholder != null) {
				StringHelper.setValue(holderinfo, rightholder);
				commonDao.update(rightholder);
			}
			RightsHolder rightholder_ls = RightsHolderTools.loadRightsHolder(DJDYLY.LS, qlrid);
			if (rightholder_ls != null) {
				StringHelper.setValue(holderinfo, rightholder_ls);
				commonDao.update(rightholder_ls);
			}
			RightsHolder rightholder_gz = RightsHolderTools.loadRightsHolder(DJDYLY.GZ, qlrid);
			if (rightholder_gz != null) {
				StringHelper.setValue(holderinfo, rightholder_gz);
				commonDao.update(rightholder_gz);
			}

			String zsid = getValueFromMapByName("ZSID", holderinfo);
			List<BDCS_QDZR_XZ> list_qdzr = commonDao.getDataList(BDCS_QDZR_XZ.class, "QLRID='" + qlrid + "'");
			if (list_qdzr != null && list_qdzr.size() > 0) {
				BDCS_QDZR_XZ qdzr = list_qdzr.get(0);
				qdzr.setZSID(zsid);
				commonDao.update(qdzr);
			}

			List<BDCS_QDZR_LS> list_qdzr_ls = commonDao.getDataList(BDCS_QDZR_LS.class, "QLRID='" + qlrid + "'");
			if (list_qdzr_ls != null && list_qdzr_ls.size() > 0) {
				BDCS_QDZR_LS qdzr_ls = list_qdzr_ls.get(0);
				qdzr_ls.setZSID(zsid);
				commonDao.update(qdzr_ls);
			}
			
			List<BDCS_QDZR_GZ> list_qdzr_gz = commonDao.getDataList(BDCS_QDZR_GZ.class, "QLRID='" + qlrid + "'");
			if (list_qdzr_gz != null && list_qdzr_gz.size() > 0) {
				BDCS_QDZR_GZ qdzr_gz = list_qdzr_gz.get(0);
				qdzr_gz.setZSID(zsid);
				commonDao.update(qdzr_gz);
			}
		}
	}
	/******************************************权地证人******************************************/
	
	
	/******************************************通用方法******************************************/
	/*
	 * 判断map属性是否变化
	 */
	@SuppressWarnings("rawtypes")
	private static boolean IsChange(Map destmap, Map srcmap) {
		boolean b = false;
		if (srcmap == null) {
			return true;
		}
		Set set_dy = srcmap.keySet();
		Iterator iterator_dy = set_dy.iterator();
		while (iterator_dy.hasNext()) {
			Object obj = iterator_dy.next();
			String name = StringHelper.formatObject(obj);
			Object val = srcmap.get(obj);
			Object val_old = destmap.get(name);
			if ("OPERATE".equals(name)) {
				continue;
			}
			if (val instanceof Map) {
				continue;
			}
			if (StringHelper.isEmpty(val_old) && !StringHelper.isEmpty(val)) {
				b = true;
				break;
			}
			if (!StringHelper.isEmpty(val_old) && StringHelper.isEmpty(val)) {
				b = true;
				break;
			}
			if (!StringHelper.isEmpty(val_old) && !StringHelper.isEmpty(val)) {
				if (!val_old.equals(val)) {
					b = true;
					break;
				}
			}
		}
		return b;
	}

	/*
	 * 获取map中指定属性值
	 */
	@SuppressWarnings("rawtypes")
	private static String getValueFromMapByName(String name, Map m) {
		String str = "";
		if(StringHelper.isEmpty(m)){
			return str;
		}
		if (!StringHelper.isEmpty(name) && m.containsKey(name)) {
			str = StringHelper.formatObject(m.get(name));
		}
		return str;
	}
	
	/*
	 * 根据不动产权证号获取不动产权证号序号
	 */
	public static String getBDCQZHXH(String BDCQZH){
		String BDCQZHXH="";
		String regex = "(.*)[(](.*)[)](.*)不动产.*第(.*)号";
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(BDCQZH);
		while (m.find()) {
			BDCQZHXH=m.group(2)+m.group(4);
		}
		if(StringHelper.isEmpty(BDCQZHXH)){
			BDCQZHXH=BDCQZH;
		}
		return BDCQZHXH;
	}
	/******************************************通用方法******************************************/
}
