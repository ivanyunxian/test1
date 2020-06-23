package com.supermap.realestate.registrationbook.model;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_H_LS;
import com.supermap.realestate.registration.model.BDCS_H_LSY;
import com.supermap.realestate.registration.model.BDCS_NYD_LS;
import com.supermap.realestate.registration.model.BDCS_QLR_LS;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_LS;
import com.supermap.realestate.registration.model.BDCS_SLLM_LS;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_SYQZD_LS;
import com.supermap.realestate.registration.model.BDCS_TDYT_LS;
import com.supermap.realestate.registration.model.BDCS_ZH_LS;
import com.supermap.realestate.registration.model.BDCS_ZRZ_LS;
import com.supermap.realestate.registration.model.BDCS_ZRZ_LSY;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.TDYT;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate_gx.registration.util.StringUtil;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
//以不动产单元ID为入口，获取该不动产单元相关的登记信息。
//包括不动产单元信息、登记单元信息、权利信息、附属权利信息、权利人信息、申请人信息。WUZHU
public class Unit_EX {
	/**
	 * 初始化类
	 * 
	 * @作者：WUZHU
	 * @param dao
	 *            
	 * @param bdcdylx
	 *            不动产单元类型
	 * @param bdcdyid
	 *            不动产单元ID
	 * @param djlx
	 *            登记类型，可空
	 * @param qllx
	 *           权利类型，可空
	 * @param page 第几页，用于权利的分页
	 * @param pagesize 每页大小，用于权利的分页
	 * @return
	 */
	 public void Init(CommonDao dao,BDCDYLX bdcdylx, String bdcdyid,DJLX djlx,List<QLLX> qllxs, long page, long pagesize)
	  {
		 
		 RealUnit u=null;
		 if(bdcdylx!=null&&!StringUtils.isEmpty(bdcdyid))
		    u=UnitTools.loadUnit(bdcdylx, DJDYLY.LS, bdcdyid);//初始化不动产单元信息
		 if(u!=null)
		 {
			 if (BDCDYLX.SYQZD.equals(bdcdylx))// 所有权宗地不动产单元
				{
				 BDCS_SYQZD_LS _syqzd = (BDCS_SYQZD_LS) u;
				 Map<String, Object> syqzdMap= transBean2Map(_syqzd);
				 syqzdMap.put("SYQLX", ConstHelper.getNameByValue("SYQLX", String.valueOf(syqzdMap.get("SYQLX"))));
				 syqzdMap.put("PZYT", ConstHelper.getNameByValue("PZYT", String.valueOf(syqzdMap.get("PZYT"))));
				 syqzdMap.put("QLXZ", ConstHelper.getNameByValue("QLXZ", String.valueOf(syqzdMap.get("QLXZ"))));
				 syqzdMap.put("QLLX", ConstHelper.getNameByValue_new("QLLX", String.valueOf(syqzdMap.get("QLLX")),null));
				 syqzdMap.put("QLSDFS", ConstHelper.getNameByValue("QLSDFS", String.valueOf(syqzdMap.get("QLSDFS"))));
				 syqzdMap.put("DJZT", ConstHelper.getNameByValue("DJZT", String.valueOf(syqzdMap.get("DJZT"))));
				 syqzdMap.put("DJ", ConstHelper.getNameByValue("TDDJ", String.valueOf(syqzdMap.get("DJ"))));
				 
				 syqzdMap.put("SHRQ", StringHelper.FormatByDatetime(syqzdMap.get("SHRQ")));
				 syqzdMap.put("CLRQ", StringHelper.FormatByDatetime(syqzdMap.get("CLRQ")));
				 syqzdMap.put("MODIFYTIME", StringHelper.FormatByDatetime(syqzdMap.get("MODIFYTIME")));
				 syqzdMap.put("CREATETIME", StringHelper.FormatByDatetime(syqzdMap.get("CREATETIME")));
				 syqzdMap.put("DCRQ", StringHelper.FormatByDatetime(syqzdMap.get("DCRQ")));
				 this.setUnit(syqzdMap);
				}
			 if (BDCDYLX.SHYQZD.equals(bdcdylx))//使用权宗地不动产单元
				{
				 BDCS_SHYQZD_LS _shyqzd = (BDCS_SHYQZD_LS) u;
				 UseLand land=(UseLand) u;
				 boolean flag=false;
				 String yts="";//用途集合
				 String djs="";//等级集合
				 String zyt="";//主用途
				 String zdj="";//主等级
				 boolean zytflag=false;//主用途标记
				 if(land !=null)
				 {
					 if(land.getTDYTS() !=null && land.getTDYTS().size()>0)
					 {
						for (TDYT tdyt : land.getTDYTS()) {
							if(!zytflag)
							if(!StringHelper.isEmpty(tdyt.getSFZYT()) && "1".equals(tdyt.getSFZYT()))
							{   
								zyt=tdyt.getTDYTMC();
								zdj=ConstHelper.getNameByValue("TDDJ",tdyt.getTDDJ());
								zytflag=true;
							}
							if (!flag) {
								if (!StringHelper.isEmpty(tdyt.getTDYTMC())) {
									yts = tdyt.getTDYTMC();
								}
								if (!StringHelper.isEmpty(tdyt.getTDDJ())) {
									djs = ConstHelper.getNameByValue("TDDJ",tdyt.getTDDJ());
								}
								flag = true;
							} else {
								if (!StringHelper.isEmpty(tdyt.getTDYTMC())) {
									yts = yts + " " + tdyt.getTDYTMC();
								}
								if (!StringHelper.isEmpty(tdyt.getTDDJ())) {
									djs= djs+ " "+ ConstHelper.getNameByValue("TDDJ", tdyt.getTDDJ());
								}
							}
						}
						if(!zytflag)//当没有主用途时，默认取第一个用途
						{
							 TDYT tdyt=land.getTDYTS().get(0);
							  zyt=tdyt.getTDYTMC();
							  zdj=ConstHelper.getNameByValue("TDDJ", tdyt.getTDDJ());
						}
						 
					 }
				 }
				 Map<String, Object> shyqzdMap= transBean2Map(_shyqzd);
				// shyqzdMap.put("BGZT", ConstHelper.getNameByValue("BGZT", String.valueOf(shyqzdMap.get("BGZT"))));//变更状态不知道字典值
				 shyqzdMap.put("ZDTZM", land.getZDTZM());
				 shyqzdMap.put("SYQLX", ConstHelper.getNameByValue("SYQLX", String.valueOf(shyqzdMap.get("SYQLX"))));
				 shyqzdMap.put("PZYT", ConstHelper.getNameByValue("TDYT", String.valueOf(shyqzdMap.get("PZYT"))));
				 shyqzdMap.put("QLXZ", ConstHelper.getNameByValue("QLXZ", String.valueOf(shyqzdMap.get("QLXZ"))));
				 shyqzdMap.put("QLLX", ConstHelper.getNameByValue_new("QLLX", String.valueOf(shyqzdMap.get("QLLX")),null));
				 shyqzdMap.put("QLSDFS", ConstHelper.getNameByValue("QLSDFS", String.valueOf(shyqzdMap.get("QLSDFS"))));
				 shyqzdMap.put("YT", zyt);
				 shyqzdMap.put("DJZT", ConstHelper.getNameByValue("DJZT", String.valueOf(shyqzdMap.get("DJZT"))));
				 shyqzdMap.put("DJ",zdj);				 
				 shyqzdMap.put("SHRQ", StringHelper.FormatByDatetime(shyqzdMap.get("SHRQ")));
				 shyqzdMap.put("CLRQ", StringHelper.FormatByDatetime(shyqzdMap.get("CLRQ")));
				 shyqzdMap.put("MODIFYTIME", StringHelper.FormatByDatetime(shyqzdMap.get("MODIFYTIME")));
				 shyqzdMap.put("CREATETIME", StringHelper.FormatByDatetime(shyqzdMap.get("CREATETIME")));
				 shyqzdMap.put("DCRQ", StringHelper.FormatByDatetime(shyqzdMap.get("DCRQ")));
				 if(flag)
				 {
					 shyqzdMap.put("FJ", yts+"<br/>"+ djs);
				 }
				 this.setUnit(shyqzdMap);
				}
			 if (BDCDYLX.ZRZ.equals(bdcdylx))// 自然幢不动产单元
				{
				 BDCS_ZRZ_LS _zrz = (BDCS_ZRZ_LS) u;
				 Map<String, Object> zrzMap= transBean2Map(_zrz);
				 zrzMap.put("FWJG", ConstHelper.getNameByValue("FWJG", String.valueOf(zrzMap.get("FWJG"))));
				 zrzMap.put("DJZT", ConstHelper.getNameByValue("DJZT", String.valueOf(zrzMap.get("DJZT"))));
				 zrzMap.put("GHYT", ConstHelper.getNameByValue("FWYT", String.valueOf(zrzMap.get("GHYT"))));
				 
				 zrzMap.put("MODIFYTIME", StringHelper.FormatByDatetime(zrzMap.get("MODIFYTIME")));
				 zrzMap.put("CREATETIME", StringHelper.FormatByDatetime(zrzMap.get("CREATETIME")));
				 zrzMap.put("JGRQ", StringHelper.FormatByDatetime(zrzMap.get("JGRQ")));
				 this.setUnit(zrzMap);
				}
			 if (BDCDYLX.YCZRZ.equals(bdcdylx))// 预测自然幢不动产单元
				{
				 BDCS_ZRZ_LSY _zrzy = (BDCS_ZRZ_LSY) u;
				 Map<String, Object> zrzyMap= transBean2Map(_zrzy);
				 zrzyMap.put("FWJG", ConstHelper.getNameByValue("FWJG", String.valueOf(zrzyMap.get("FWJG"))));
				 zrzyMap.put("DJZT", ConstHelper.getNameByValue("DJZT", String.valueOf(zrzyMap.get("DJZT"))));
				 zrzyMap.put("GHYT", ConstHelper.getNameByValue("FWYT", String.valueOf(zrzyMap.get("GHYT"))));
				 
				 zrzyMap.put("MODIFYTIME", StringHelper.FormatByDatetime(zrzyMap.get("MODIFYTIME")));
				 zrzyMap.put("CREATETIME", StringHelper.FormatByDatetime(zrzyMap.get("CREATETIME")));
				 this.setUnit(zrzyMap);
				}
			 if (BDCDYLX.HY.equals(bdcdylx))// 海域不动产单元
				{
				 BDCS_ZH_LS _zh = (BDCS_ZH_LS) u;
				 Map<String, Object> zhMap= transBean2Map(_zh);
				 zhMap.put("ZHTZM", ConstHelper.getNameByValue("TZM", String.valueOf(zhMap.get("ZHTZM"))));
				 zhMap.put("ZT", ConstHelper.getNameByValue("BDCDYZT", String.valueOf(zhMap.get("ZT"))));
				 zhMap.put("YHLXA", ConstHelper.getNameByValue("HYSYLXA", String.valueOf(zhMap.get("YHLXA"))));
				 zhMap.put("YHLXB", ConstHelper.getNameByValue("HYSYLXB", String.valueOf(zhMap.get("YHLXB"))));
				 zhMap.put("DJZT", ConstHelper.getNameByValue("DJZT", String.valueOf(zhMap.get("DJZT"))));
				 zhMap.put("DB", ConstHelper.getNameByValue("HYDB", String.valueOf(zhMap.get("DB"))));
				 zhMap.put("XMXX", ConstHelper.getNameByValue("XMXZ", String.valueOf(zhMap.get("XMXX"))));
				 
				 zhMap.put("MODIFYTIME", StringHelper.FormatByDatetime(zhMap.get("MODIFYTIME")));
				 zhMap.put("CREATETIME", StringHelper.FormatByDatetime(zhMap.get("CREATETIME")));
				 zhMap.put("SHRQ", StringHelper.FormatByDatetime(zhMap.get("SHRQ")));
				 zhMap.put("HCRQ", StringHelper.FormatByDatetime(zhMap.get("HCRQ")));
				 zhMap.put("CLRQ", StringHelper.FormatByDatetime(zhMap.get("CLRQ")));
				 this.setUnit(zhMap);
				}
			 if (BDCDYLX.LD.equals(bdcdylx))// 林地不动产单元
				{
				 BDCS_SLLM_LS _sllm = (BDCS_SLLM_LS) u;
				 Map<String, Object> sllmMap= transBean2Map(_sllm);
				 sllmMap.put("LZ", ConstHelper.getNameByValue("LZ", String.valueOf(sllmMap.get("LZ"))));
				 sllmMap.put("DJZT", ConstHelper.getNameByValue("DJZT", String.valueOf(sllmMap.get("DJZT"))));
				 sllmMap.put("QY", ConstHelper.getNameByValue("QY", String.valueOf(sllmMap.get("QY"))));
				 
				 sllmMap.put("MODIFYTIME", StringHelper.FormatByDatetime(sllmMap.get("MODIFYTIME")));
				 sllmMap.put("CREATETIME", StringHelper.FormatByDatetime(sllmMap.get("CREATETIME")));
				 sllmMap.put("SHRQ", StringHelper.FormatByDatetime(sllmMap.get("SHRQ")));
				 this.setUnit(sllmMap);
				}
			 if (BDCDYLX.NYD.equals(bdcdylx))// 农用地不动产单元
				{
				 BDCS_NYD_LS _nyd = (BDCS_NYD_LS) u;
				 Map<String, Object> nydMap= transBean2Map(_nyd);
				 nydMap.put("LZ", ConstHelper.getNameByValue("LZ", String.valueOf(nydMap.get("LZ"))));
				 nydMap.put("DJZT", ConstHelper.getNameByValue("DJZT", String.valueOf(nydMap.get("DJZT"))));
				 nydMap.put("QY", ConstHelper.getNameByValue("QY", String.valueOf(nydMap.get("QY"))));
				 
				 nydMap.put("MODIFYTIME", StringHelper.FormatByDatetime(nydMap.get("MODIFYTIME")));
				 nydMap.put("CREATETIME", StringHelper.FormatByDatetime(nydMap.get("CREATETIME")));
				 nydMap.put("SHRQ", StringHelper.FormatByDatetime(nydMap.get("SHRQ")));
				 this.setUnit(nydMap);
				}
			 //注销的是因为还没有该实体类
			 if (BDCDYLX.GZW.equals(bdcdylx))// 构筑物不动产单元
				{
//				 BDCS_GZW_LS _gzw = (BDCS_GZW_LS) u;
//				 Map<String, Object> gzwMap= transBean2Map(_gzw);
//				 gzwMap.put("MODIFYTIME", StringHelper.FormatByDatetime(gzwMap.get("MODIFYTIME")));
//				 gzwMap.put("CREATETIME", StringHelper.FormatByDatetime(gzwMap.get("CREATETIME")));
//				 gzwMap.put("JGSJ", StringHelper.FormatByDatetime(gzwMap.get("JGSJ")));
//				 this.setUnit(gzwMap);
				}
			 if (BDCDYLX.QTDZW.equals(bdcdylx))// 其他定着物不动产单元
				{
//				 BDCS_QTDZW_LS _qtdzw = (BDCS_QTDZW_LS) u;
//				 Map<String, Object> qtdzwMap= transBean2Map(_qtdzw);
//				 this.setUnit(qtdzwMap);
				}
			 if (BDCDYLX.DZDZW.equals(bdcdylx))// 点状定着物不动产单元
				{
//				 BDCS_DZDZW_LS _dzdzw = (BDCS_DZDZW_LS) u;
//				 Map<String, Object> dzdzwMap= transBean2Map(_dzdzw);
//				 this.setUnit(dzdzwMap);
				}
			 if (BDCDYLX.XZDZW.equals(bdcdylx))// 线状定着物不动产单元
				{
//				 BDCS_XZDZW_LS _xzdzw = (BDCS_XZDZW_LS) u;
//				 Map<String, Object> xzdzwMap= transBean2Map(_xzdzw);
//				 this.setUnit(xzdzwMap);
				}
			 if (BDCDYLX.MZDZW.equals(bdcdylx))// 面状定着物不动产单元
				{
//				 BDCS_MZDZW_LS _mzdzw = (BDCS_MZDZW_LS) u;
//				 Map<String, Object> mzdzwMap= transBean2Map(_mzdzw);
//				 this.setUnit(mzdzwMap);
				}
			 if (BDCDYLX.H.equals(bdcdylx))//户不动产单元
				{
				 BDCS_H_LS _h = (BDCS_H_LS) u;
				 Map<String, Object> hMap= transBean2Map(_h);
				 //hMap.put("BGZT", ConstHelper.getNameByValue("BGZT", String.valueOf(hMap.get("BGZT"))));//变更状态不知道字典值
				 hMap.put("HX", ConstHelper.getNameByValue("HX", String.valueOf(hMap.get("HX"))));
				 hMap.put("HXJG", ConstHelper.getNameByValue("HXJG", String.valueOf(hMap.get("HXJG"))));
				 hMap.put("FWXZ", ConstHelper.getNameByValue("FWXZ", String.valueOf(hMap.get("FWXZ"))));
				 hMap.put("FWYT1", ConstHelper.getNameByValue("FWYT", String.valueOf(hMap.get("FWYT1"))));
				 hMap.put("FWYT2", ConstHelper.getNameByValue("FWYT", String.valueOf(hMap.get("FWYT2"))));
				 hMap.put("FWYT3", ConstHelper.getNameByValue("FWYT", String.valueOf(hMap.get("FWYT3"))));
				 hMap.put("FWLX", ConstHelper.getNameByValue("FWLX", String.valueOf(hMap.get("FWLX"))));
				 hMap.put("FWJG", ConstHelper.getNameByValue("FWJG", String.valueOf(hMap.get("FWJG"))));
				 hMap.put("ZT", ConstHelper.getNameByValue("BDCDYZT", String.valueOf(hMap.get("ZT"))));
				 hMap.put("DJZT", ConstHelper.getNameByValue("DJZT", String.valueOf(hMap.get("DJZT"))));
				 hMap.put("GHYT", ConstHelper.getNameByValue("FWYT", String.valueOf(hMap.get("GHYT"))));
				 hMap.put("FWTDYT", ConstHelper.getNameByValue("FWTDYT", String.valueOf(hMap.get("TDYT"))));
				 hMap.put("MODIFYTIME", StringHelper.FormatByDatetime(hMap.get("MODIFYTIME")));
				 hMap.put("CREATETIME", StringHelper.FormatByDatetime(hMap.get("CREATETIME")));
				 hMap.put("JGSJ", StringHelper.FormatByDatetime(hMap.get("JGSJ")));
				 hMap.put("TDSYQQSRQ", StringHelper.FormatByDatetime(hMap.get("TDSYQQSRQ")));
				 hMap.put("TDSYQZZRQ", StringHelper.FormatByDatetime(hMap.get("TDSYQZZRQ")));
				 this.setUnit(hMap);
				}
			 if (BDCDYLX.YCH.equals(bdcdylx))//预测户不动产单元
				{
				 BDCS_H_LSY _ych = (BDCS_H_LSY) u;
				 Map<String, Object> ychMap= transBean2Map(_ych);
				//hMap.put("BGZT", ConstHelper.getNameByValue("BGZT", String.valueOf(hMap.get("BGZT"))));//变更状态不知道字典值
				 ychMap.put("HX", ConstHelper.getNameByValue("HX", String.valueOf(ychMap.get("HX"))));
				 ychMap.put("HXJG", ConstHelper.getNameByValue("HXJG", String.valueOf(ychMap.get("HXJG"))));
				 ychMap.put("FWXZ", ConstHelper.getNameByValue("FWXZ", String.valueOf(ychMap.get("FWXZ"))));
				 ychMap.put("FWYT1", ConstHelper.getNameByValue("FWYT", String.valueOf(ychMap.get("FWYT1"))));
				 ychMap.put("FWYT2", ConstHelper.getNameByValue("FWYT", String.valueOf(ychMap.get("FWYT2"))));
				 ychMap.put("FWYT3", ConstHelper.getNameByValue("FWYT", String.valueOf(ychMap.get("FWYT3"))));
				 ychMap.put("FWLX", ConstHelper.getNameByValue("FWLX", String.valueOf(ychMap.get("FWLX"))));
				 ychMap.put("FWJG", ConstHelper.getNameByValue("FWJG", String.valueOf(ychMap.get("FWJG"))));
				 ychMap.put("ZT", ConstHelper.getNameByValue("BDCDYZT", String.valueOf(ychMap.get("ZT"))));
				 ychMap.put("DJZT", ConstHelper.getNameByValue("DJZT", String.valueOf(ychMap.get("DJZT"))));
				 ychMap.put("GHYT", ConstHelper.getNameByValue("FWYT", String.valueOf(ychMap.get("GHYT"))));
				 
				 ychMap.put("MODIFYTIME", StringHelper.FormatByDatetime(ychMap.get("MODIFYTIME")));
				 ychMap.put("CREATETIME", StringHelper.FormatByDatetime(ychMap.get("CREATETIME")));
				 ychMap.put("JGSJ", StringHelper.FormatByDatetime(ychMap.get("JGSJ")));
				 this.setUnit(ychMap);
				}
			
		 }
		 List<BDCS_DJDY_LS> djdys = dao.getDataList(BDCS_DJDY_LS.class, " BDCDYID='"+bdcdyid+"'");//初始化登记单元信息
		  if(djdys!=null&&djdys.size()>0)
		  {
			  BDCS_DJDY_LS _djdy=djdys.get(0);
			  Map<String, Object> djdyMap=transBean2Map(_djdy);
			  djdyMap.put("MODIFYTIME", StringHelper.FormatByDatetime(djdyMap.get("MODIFYTIME")));
			  djdyMap.put("CREATETIME", StringHelper.FormatByDatetime(djdyMap.get("CREATETIME")));
			  this.setDidy(djdyMap);
			  StringBuilder ql_sql=new StringBuilder(" DJDYID='"+_djdy.getDJDYID()+"' ");
			  if(qllxs!=null)
			  {		
				  if(QLLX.QTQL.toString().equals(StringHelper.formatObject(qllxs.get(0)))){
					  ql_sql=new StringBuilder(" BDCDYID='"+bdcdyid+"' ");
				  }
				  ql_sql.append(" AND QLLX IN (");
				  int index=0;
				  for(QLLX qllx:qllxs)
				  {
					  index+=1;
					  ql_sql.append("'"+qllx.Value+"'");
					  if(index<qllxs.size())
					    ql_sql.append(",");
				
				  }
				  ql_sql.append(")");
			  }else{
				  String XZQHDM=ConfigHelper.getNameByValue("XZQHDM");
				  if(XZQHDM.indexOf("4503")==0){
					  if (BDCDYLX.SHYQZD.equals(bdcdylx)||BDCDYLX.SYQZD.equals(bdcdylx)||BDCDYLX.NYD.equals(bdcdylx)){
						  ql_sql.append(" AND QLLX IN ('1','2','3','5','7','24') ");
					  }///app/book/{bdcdyid}/zdxx 获取宗地信息时只传bdcdylx和bdcdyid 现在有个情况是：补录的数据出现使用权或者所有权的登记时间比抵押权还晚的情况
					  //而查询的时候是order by djdj 获取djsj,dbr,djyy等字段信息的时候是get(0)，这样 宗地信息获取到了抵押权那条记录，应该怎么处理好？（liangcheng）
				  }
			  }
//			  String Baseworkflow_ID =ProjectHelper.GetPrjInfoByXMBH(_djdy.getXMBH()).getBaseworkflowcode();
			  if(djlx!=null){
				  if("700".equals(djlx.Value)){//广西要求显示预购商品房预告登记注销登记的信息
					  ql_sql.append(" AND DJLX in('700','400','300')");
				  }else{
					  ql_sql.append(" AND DJLX='"+djlx.Value+"' ");
				  }
			  }else if(qllxs!=null&&QLLX.QTQL.toString().equals(StringHelper.formatObject(qllxs.get(0)))){
				  ql_sql.append(" AND (DJLX='500' OR DJLX='600' OR DJLX='900') ");
			  }else{
				  ql_sql.append(" AND DJLX<> '"+DJLX.YYDJ.Value+"' AND DJLX<> '"+DJLX.CFDJ.Value+"' AND DJLX<> '"+DJLX.YGDJ.Value+"'");//所有权利类型过滤掉异议登记和 查封登记以及 预告登记
			  }
			  ql_sql.append("  ORDER BY DJSJ NULLS FIRST ");
			  int start = 0;
				int end = 0;
				start = ((int)page - 1) * (int)pagesize + 1;
				end = (int)page * (int)pagesize;
				String djjg="";
			  List<BDCS_QL_LS> _qls= dao.getDataList(BDCS_QL_LS.class,ql_sql.toString());//初始化权利信息
			  for(BDCS_QL_LS ql:_qls)
			  {
				  if(!StringUtils.isEmpty(ql.getDJJG()))
				  {
					  djjg=ql.getDJJG();
					  break;
				  }
			  }
			    if(_qls.size()<=end)
			    	end=_qls.size();
			    List<QL_EX> _qlExs=new ArrayList<QL_EX>();          
			  for (int i = start-1; i < end; i++)//分页
			  {
				  QL_EX _ql_ex=new QL_EX();
				  _qls.get(i).setDJJG(djjg);
				  ObjectHelper.copyObject(_qls.get(i),_ql_ex);
				  _ql_ex.Init(dao);
				  _qlExs.add(_ql_ex);
			  }
				this.setQls(_qlExs);   
		  }
	  }

private	Map<String,Object> unit;//单条登记的不动产
  private   Map<String,Object> didy;//该不动产单元对应的登记单元
  private   List<QL_EX> qls;//不动产单元对应的权利集合
  public class QL_EX extends BDCS_QL_LS
  {
	  public void Init(CommonDao dao)
	  {
	  	BDCS_QL_LS _ql=(BDCS_QL_LS)this;
  		Map<String, Object> qlMap= transBean2Map(_ql);
  		List<RightsHolder> holders = RightsHolderTools.loadRightsHolders(DJDYLY.LS, _ql.getId());
  		List<String> bdcqzhs = new ArrayList<String>();
  		for (RightsHolder rightsHolder : holders) {
  			if(!StringHelper.isEmpty(rightsHolder.getBDCQZH())){
  				if(!bdcqzhs.contains(rightsHolder.getBDCQZH())){
  					bdcqzhs.add(rightsHolder.getBDCQZH());
  				}
  			}
		}
  		if(bdcqzhs.size()>0){
  			qlMap.put("BDCQZH", StringHelper.formatList(bdcqzhs));
  		}
  		//权利表字典转换相应字段
  		qlMap.put("CZFS", ConstHelper.getNameByValue("CZFS",String.valueOf(qlMap.get("CZFS"))));
  		qlMap.put("QLLX", ConstHelper.getNameByValue("QLLX",String.valueOf(qlMap.get("QLLX"))));
  		qlMap.put("QLJSSJ", StringHelper.FormatByDatetime(qlMap.get("QLJSSJ")));
  		qlMap.put("QLQSSJ", StringHelper.FormatByDatetime(qlMap.get("QLQSSJ")));
  		qlMap.put("QSZT", ConstHelper.getNameByValue("QSZT",String.valueOf(qlMap.get("QSZT"))));
  		qlMap.put("DJSJ", StringHelper.FormatByDatetime(qlMap.get("DJSJ")));
  		
  		qlMap.put("DJLX", ConstHelper.getNameByValue("DJLX",String.valueOf(qlMap.get("DJLX"))));
  		//判断预测户的djlx
		List<BDCS_DJDY_XZ> list =dao.getDataList(BDCS_DJDY_XZ.class, "DJDYID='" + _ql.getDJDYID() + "'");
  		if ("4".equals(_ql.getQLLX())||"23".equals(_ql.getQLLX()) ||("4".equals(_ql.getQLLX())&&  "032".equals(list.get(0).getBDCDYLX())) || "99".equals(_ql.getQLLX())) {
  			if (!StringHelper.isEmpty(_ql.getYWH())) {
  				ProjectInfo projectInfo = ProjectHelper.GetPrjInfoByPrjID(_ql.getYWH());
  				if(projectInfo!=null){
  					String selector = projectInfo.getBaseworkflowcode();
  	  				//基准流程  4/032
  	  	  			//String[] codes_cs = {"CS013","CS013"}; 
  	  	  			String[] codes_zy = {"ZY032"};  
  	  	  			String[] codes_bg = {"BG043","BG049","BG201"};  
  	  	  			String[] codes_gz = {"GZ011","GZ017","GZ007","GZ013","GZ014","GZ015","GZ016","GZ201"};  
  	  	  			//String[] codes_cf = {"CF003","CF010"};  
  	  	  			//String[] codes_jf = {"JF003"};  
  	  	  			String[] codes_zx = {"ZX005"};  
  	  	  			String[] codes_bhz = {"BZ013"};  
  	  	  			String[] codes_qt = {"QT003","XZ003"}; 
  	  	  			
  	  	  			if(Arrays.asList(codes_zy).contains(selector)) { 			
  	  			  		qlMap.put("DJLX","转移登记");		
  	  				}else if (Arrays.asList(codes_bg).contains(selector)) {
  	  					qlMap.put("DJLX","变更登记");
  	  				}else if (Arrays.asList(codes_gz).contains(selector)) {
  	  					qlMap.put("DJLX","更正登记");
  	  				}else if (Arrays.asList(codes_zx).contains(selector)) {
  	  					qlMap.put("DJLX","注销登记");
  	  				}else if (Arrays.asList(codes_bhz).contains(selector) || Arrays.asList(codes_qt).contains(selector)) {
  	  					qlMap.put("DJLX","其他登记");
  	  				}
  				}		
			} 
  		}
  		
  		qlMap.put("MODIFYTIME", StringHelper.FormatByDatetime(qlMap.get("MODIFYTIME")));
  		qlMap.put("CREATETIME", StringHelper.FormatByDatetime(qlMap.get("CREATETIME")));
  		qlMap.put("ZSEWM","");//证书二维码不要
  		qlMap.put("ZSBS", ConstHelper.getNameByValue("ZSBS",String.valueOf(qlMap.get("ZSBS"))));


  	
  		this.setQl(qlMap);
  		 BDCS_QL_XZ  _ql_xz =dao.get(BDCS_QL_XZ.class, super.getId());//看现状层是否有该权利信息
  		 Map<String, Object> fsqlMap=new HashMap<String, Object>();
	  	if(!StringUtils.isEmpty(super.getFSQLID())){
		 BDCS_FSQL_LS  _fsql =dao.get(BDCS_FSQL_LS.class, super.getFSQLID());//初始化附属权利信息
	
		 fsqlMap= transBean2Map(_fsql);
	  	}
		//附属权利表字典转换相应字段
	  	if (!StringUtils.isEmpty(fsqlMap.get("YWRZJZL"))) {
			String[] zjlxStrings = fsqlMap.get("YWRZJZL").toString().split("/");
			if (zjlxStrings.length > 1) {
				StringBuilder zjlxStringBuilder = new StringBuilder();
				for (int i = 0; i < zjlxStrings.length; i++) {
					if (i != 0) {
						zjlxStringBuilder.append("/").append(ConstHelper.getNameByValue(
								"ZJLX", zjlxStrings[i]));
					}else{
						zjlxStringBuilder.append(ConstHelper.getNameByValue(
								"ZJLX", zjlxStrings[i]));
					}
				}
				fsqlMap.put("YWRZJZL", zjlxStringBuilder.toString());
			} else {
				fsqlMap.put("YWRZJZL", ConstHelper.getNameByValue("ZJLX",String.valueOf(fsqlMap.get("YWRZJZL"))));
			}
		}
		 //fsqlMap.put("YWRZJZL", ConstHelper.getNameByValue("ZJLX",String.valueOf(fsqlMap.get("YWRZJZL"))));
		 fsqlMap.put("GYDQLRZJZL", ConstHelper.getNameByValue("ZJLX",String.valueOf(fsqlMap.get("GYDQLRZJZL"))));
		 fsqlMap.put("YZYFS", ConstHelper.getNameByValue("YZYFS",String.valueOf(fsqlMap.get("YZYFS"))));
		 fsqlMap.put("TDSYQXZ", ConstHelper.getNameByValue("TDSYQXZ",String.valueOf(fsqlMap.get("TDSYQXZ"))));
		 fsqlMap.put("FWXZ", ConstHelper.getNameByValue("FWXZ",String.valueOf(fsqlMap.get("FWXZ"))));
		 fsqlMap.put("FWJG", ConstHelper.getNameByValue("FWJG",String.valueOf(fsqlMap.get("FWJG"))));
		 fsqlMap.put("DYBDCLX_CODE", fsqlMap.get("DYBDCLX"));
		 fsqlMap.put("DYBDCLX_CODE2", fsqlMap.get("DYWLX"));	//liangq
		 fsqlMap.put("DYBDCLX", ConstHelper.getNameByValue("DYBDCLX",String.valueOf(fsqlMap.get("DYBDCLX"))));
		 fsqlMap.put("DYFS", ConstHelper.getNameByValue("DYFS",String.valueOf(fsqlMap.get("DYFS"))));
		 fsqlMap.put("DYWLX", ConstHelper.getNameByValue("DYBDCLX",String.valueOf(fsqlMap.get("DYWLX"))));
		 fsqlMap.put("GJZWLX", ConstHelper.getNameByValue("GZWLX",String.valueOf(fsqlMap.get("GJZWLX"))));
		 fsqlMap.put("LDSYQXZ", ConstHelper.getNameByValue("TDSYQXZ",String.valueOf(fsqlMap.get("LDSYQXZ"))));
		 fsqlMap.put("CFLX", ConstHelper.getNameByValue("CFLX",String.valueOf(fsqlMap.get("CFLX"))));
		 fsqlMap.put("SYTTLX", ConstHelper.getNameByValue("SYTTLX",String.valueOf(fsqlMap.get("SYTTLX"))));
		 fsqlMap.put("GHYT", ConstHelper.getNameByValue("FWYT",String.valueOf(fsqlMap.get("GHYT"))));
		 fsqlMap.put("QY", ConstHelper.getNameByValue("QY",String.valueOf(fsqlMap.get("QY"))));
		 fsqlMap.put("YGDJZL", ConstHelper.getNameByValue("YGDJZL",String.valueOf(fsqlMap.get("YGDJZL"))));
			   
		 fsqlMap.put("CFSJ", StringHelper.FormatByDatetime(fsqlMap.get("CFSJ")));
		 fsqlMap.put("ZXSJ", StringHelper.FormatByDatetime(fsqlMap.get("ZXSJ")));
		 fsqlMap.put("JGSJ", StringHelper.FormatByDatetime(fsqlMap.get("JGSJ")));
		 fsqlMap.put("MODIFYTIME", StringHelper.FormatByDatetime(fsqlMap.get("MODIFYTIME")));
		 fsqlMap.put("CREATETIME", StringHelper.FormatByDatetime(fsqlMap.get("CREATETIME")));
		 if(!StringUtils.isEmpty(fsqlMap.get("ZXFJ"))){
			  if(!StringUtils.isEmpty(qlMap.get("FJ")))
				  qlMap.put("FJ",String.valueOf(qlMap.get("FJ"))+"  <br>"+"注销附记:"+String.valueOf(fsqlMap.get("ZXFJ")));
			  else
				  qlMap.put("FJ","注销附记:"+String.valueOf(fsqlMap.get("ZXFJ")));  
		  }

  	    //用来判断权利是否注销。
		if(_ql_xz!=null){
			fsqlMap.put("SFZX","该权利未注销");
		}else{
  			fsqlMap.put("SFZX","该权利已注销");
  			//添加注销时间、注销原因和注销登簿人
  			if (StringHelper.isEmpty(fsqlMap.get("ZXDYYY"))) {
				if (!StringHelper.isEmpty(fsqlMap.get("ZXDYYWH"))) {
					//获取下一首业务的权利信息
					 List<BDCS_QL_LS>  _ql_nextls =dao.getDataList(BDCS_QL_LS.class, " YWH='"+String.valueOf(fsqlMap.get("ZXDYYWH"))+"' AND QLLX='"+super.getQLLX()+"'");
					if (_ql_nextls!=null && _ql_nextls.size()>0) {						
						fsqlMap.put("ZXDYYY", _ql_nextls.get(0).getDJYY());
					}
					 
				}
			}
  			
  		}
		 this.setFsql(fsqlMap);
		//初始化权利人信息
		 List<BDCS_QLR_LS> _qlrs = dao.getDataList(BDCS_QLR_LS.class, " QLID='"+super.getId()+"'");
		 List<Map<String,Object>>  qlrsMap=new ArrayList<Map<String,Object>>();
		 for(BDCS_QLR_LS qlr:_qlrs)
		 {
			 Map<String, Object> qlrMap=transBean2Map(qlr);
			 if(_qlrs.size()>1)
			 {
				 String gyfs=_qlrs.get(0).getGYFSName();
				 qlrMap.put("GYQK", gyfs);
			 }			
			 qlrMap.put("DLRZJLX", ConstHelper.getNameByValue("ZJLX",String.valueOf(qlrMap.get("DLRZJLX"))));
			 //qlrMap.put("GYFS", ConstHelper.getNameByValue("GYFS",String.valueOf(qlrMap.get("GYFS"))));
			 qlrMap.put("GJ", ConstHelper.getNameByValue("GJDQ",String.valueOf(qlrMap.get("GJ"))));
			 qlrMap.put("XB", ConstHelper.getNameByValue("XB",String.valueOf(qlrMap.get("XB"))));
			 qlrMap.put("HJSZSS", ConstHelper.getNameByValue("SS",String.valueOf(qlrMap.get("HJSZSS"))));
			 qlrMap.put("SSHY", ConstHelper.getNameByValue("SSHY",String.valueOf(qlrMap.get("SSHY"))));
			 qlrMap.put("QLRLX", ConstHelper.getNameByValue("QLRLX",String.valueOf(qlrMap.get("QLRLX"))));
			 qlrMap.put("FDDBRZJLX", ConstHelper.getNameByValue("ZJLX",String.valueOf(qlrMap.get("FDDBRZJLX"))));
			 qlrMap.put("ZJZL", ConstHelper.getNameByValue("ZJLX",String.valueOf(qlrMap.get("ZJZL"))));

			 qlrMap.put("MODIFYTIME", StringHelper.FormatByDatetime(qlrMap.get("MODIFYTIME")));
			 qlrMap.put("CREATETIME", StringHelper.FormatByDatetime(qlrMap.get("CREATETIME")));
			 qlrsMap.add(qlrMap);
		 }
		 this.setQlrs(qlrsMap);
		 //单条权利数据
			if (this.getQlrs().size() > 0) {
				Map<String, Object> qlrMap = this.getQlrs().get(0);
				String qlrmcs = "";// 权利人名称
				String zjhs = "";// 证件号
				String zjzls = "";// 证件种类
				String gyfss = "";// 共有方式
				String qllxs = "";// 权利性质
				boolean flag = true;
				boolean flag1 = true;
				for (Map<String, Object> qlr : this.getQlrs()) {
					if (!StringHelper.isEmpty(qlr.get("GYFS"))) {
						if (ConstValue.GYFS.AFGY.Value.equals(qlr.get("GYFS")))// 按份共有
						{
							if (flag) {
								flag = false;
								gyfss = "按份共有：";
								if (!StringHelper.isEmpty(qlr.get("QLBL"))) {
									if (qlr.get("QLBL").toString()
											.contains("/")) {
										gyfss = gyfss
												+ qlr.get("QLBL").toString();
									} else if (qlr.get("QLBL").toString()
											.lastIndexOf("%") > 0) {
										gyfss = gyfss
												+ qlr.get("QLBL").toString();
									} else {
										gyfss = gyfss
												+ qlr.get("QLBL").toString()
												+ "%";
									}
								} else {
									gyfss = gyfss + "-----";
								}
							} else {
								if (!StringHelper.isEmpty(qlr.get("QLBL"))) {
									if (qlr.get("QLBL").toString()
											.contains("/")) {
										gyfss = gyfss + "、"
												+ qlr.get("QLBL").toString();
									} else if (qlr.get("QLBL").toString()
											.lastIndexOf("%") > 0) {
										gyfss = gyfss + "、"
												+ qlr.get("QLBL").toString();
									} else {
										gyfss = gyfss + "、"
												+ qlr.get("QLBL").toString()
												+ "%";
									}
								} else {
									gyfss = gyfss + "、-----";
								}
							}
						} else// 单独所有、共同共有
						{
							if (flag) {
								gyfss = ConstHelper.getNameByValue("GYFS",
										String.valueOf(qlrMap.get("GYFS")));
								flag = false;
							}
						}
					}
					if (flag1) {
						if (!StringHelper.isEmpty(qlr.get("QLRMC"))) {
							qlrmcs = qlr.get("QLRMC").toString();
						} else {
							qlrmcs = "-----";
						}
						if (!StringHelper.isEmpty(qlr.get("ZJH"))) {
							zjhs = qlr.get("ZJH").toString();
						} else {
							zjhs = "-----";
						}
						if (!StringHelper.isEmpty(qlr.get("ZJZL"))) {
							zjzls = qlr.get("ZJZL").toString();
						} else {
							zjzls = "-----";
						}
						if (!StringHelper.isEmpty(qlr.get("QLRLX"))) {
							qllxs = qlr.get("QLRLX").toString();
						} else {
							qllxs = "-----";
						}
						flag1=false;
					} else {
						if (!StringHelper.isEmpty(qlr.get("QLRMC"))) {
							qlrmcs = qlrmcs + "," + qlr.get("QLRMC").toString();
						} else {
							qlrmcs = qlrmcs + "," + "-----";
						}
						if (!StringHelper.isEmpty(qlr.get("ZJH"))) {
							zjhs = zjhs + "," + qlr.get("ZJH").toString();
						} else {
							zjhs = zjhs + "," + "-----";
						}
						if (!StringHelper.isEmpty(qlr.get("ZJZL"))) {
							zjzls = zjzls + "," + qlr.get("ZJZL").toString();
						} else {
							zjzls = zjzls + "," + "-----";
						}
						if (!StringHelper.isEmpty(qlr.get("QLRLX"))) {
							qllxs = qllxs + "," + qlr.get("QLRLX").toString();
						} else {
							qllxs = qllxs + "," + "-----";
						}
					}
				}
			qlrMap.put("QLRMC", qlrmcs);
			qlrMap.put("ZJH", zjhs);
			qlrMap.put("ZJZL", zjzls);
			qlrMap.put("QLRLX", qllxs);
			qlrMap.put("GYQK", gyfss);
			this.setQlr(qlrMap);
		 }
			//初始化申请人信息
		 List<Map<String,Object>>  sqrsMap=new ArrayList<Map<String,Object>>();
	   if(!StringUtils.isEmpty(super.getXMBH())){
		 List<BDCS_SQR> _sqls = dao.getDataList(BDCS_SQR.class, " XMBH='" + super.getXMBH() + "'");
		 for(BDCS_SQR _sqr:_sqls)
		 {
			 Map<String, Object> sqrMap=transBean2Map(_sqr);
			 sqrsMap.add(sqrMap);
		 }
		}
		 this.setSqrs(sqrsMap);
		//单条申请人数据
		 if(this.getSqrs().size()>0)
		 {
			 Map<String,Object> sqrMap=this.getSqrs().get(0);
			 StringBuilder sqrmcs = new StringBuilder();
			 int count=0;
			 for(Map<String,Object> _sqr:this.getSqrs())
			 {
				 sqrmcs.append(_sqr.get("SQRXM"));
				 if(count<(this.getSqrs().size()-1))//不是最后一条
					 sqrmcs.append("、");
					count+=1;
			 }
			 sqrMap.put("SQRXM", sqrmcs.toString());
			this.setSqr(sqrMap);
		 }
	  }
		private Map<String,Object> ql;//单条权利数据
	  	private Map<String,Object> fsql;//该权利对应的附属权利
	  	private List<Map<String,Object>> qlrs;//该权利对应的权利人集合
	  	private Map<String,Object> qlr;//显示需要的权利人集合中的第一条权利人
	  	private List<Map<String,Object>> sqrs;//该权利对应的申请人集合
	  	private Map<String,Object> sqr;//显示需要的申请人集合中的第一条申请人
	  	
  	public Map<String,Object> getQl() {
  		if(ql==null)
			return new HashMap<String, Object>();
  		return ql;
	}
	public void setQl(Map<String,Object> ql) {
		this.ql = ql;
	}
	public Map<String,Object> getFsql() {
		if(fsql==null)
			return new HashMap<String, Object>(); 
		return fsql;
	}
	public void setFsql(Map<String,Object> fsql) {
		this.fsql = fsql;
	}
	public List<Map<String,Object>> getQlrs() {
		if(qlrs==null)
			return new ArrayList<Map<String,Object>>();
		return qlrs;
	}
	public void setQlrs(List<Map<String,Object>> qlrs) {
		this.qlrs = qlrs;
	}
	public Map<String,Object> getQlr() {
		if(qlr==null)
			return new HashMap<String, Object>(); 
		return qlr;
	}
	public void setQlr(Map<String,Object> qlr) {
		this.qlr = qlr;
	}
	public List<Map<String,Object>> getSqrs() {
		return sqrs;
	}
	public void setSqrs(List<Map<String,Object>> sqrs) {
		this.sqrs = sqrs;
	}
	public Map<String,Object> getSqr() {
		return sqr;
	}
	public void setSqr(Map<String,Object> sqr) {
		this.sqr = sqr;
	}
  }
  public Map<String,Object> getUnit() {
		if(unit==null)
			return new HashMap<String, Object>(); 
		return unit;
	}
	public void setUnit(Map<String,Object> unit) {
		this.unit = unit;
	}
public Map<String,Object> getDidy() {
	if(didy==null)
		return new HashMap<String, Object>(); 
	return didy;
	}
	public void setDidy(Map<String,Object> didy) {
		this.didy = didy;
	}
public List<QL_EX> getQls() {
	if(qls==null)
		return  new ArrayList<QL_EX>();         
		return qls;
	}
	public void setQls(List<QL_EX> qls) {
		this.qls = qls;
	}
	/**
	 * 将对象转换成MAP
	 * @Title: transBean2Map
	 * @author:wuzhu
	 * @date：
	 * @param obj
	 * @return
	 *///将对象转换成MAP
	  public  Map<String, Object> transBean2Map(Object obj) {  
	    if(obj == null){  
	        return new HashMap<String, Object>();  
	    }          
	    Map<String, Object> map = new HashMap<String, Object>();  
	    try {  
	        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());  
	        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();  
	        for (PropertyDescriptor property : propertyDescriptors) {  
	            String key = property.getName();  

	            // 过滤class属性  
	            if (!key.equals("class")) {
	                // 得到property对应的getter方法  
	                Method getter = property.getReadMethod();  
	                if (StringHelper.isEmpty(getter)) {
	                	continue;
					}
	                Object value = getter.invoke(obj);  
	                map.put(key, value);  
	            }  

	        }  
	    } catch (Exception e) {  
	        System.out.println("对象转换为MAP失败： " + e);  
	    }  
	    return map;  
	  }
	  
}
