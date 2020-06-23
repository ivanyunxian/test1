package com.supermap.realestate.registrationbook.model;
// 已不用 可删除
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_QLR_LS;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.StringHelper;

/**
 * 
 * @Description:整合了的权利表，包含了附属权利信息
 * @author wuzhu 
 * @date 2015年7月10日 上午11:23:01
 * @Copyright SuperMap
 */
public class QLInfo {
	
  //权利表
  private BDCS_QL_LS QL=null;
  
  //附属权利表
  private BDCS_FSQL_LS FSQL=null;
  
  //权利人集合
  private List<BDCS_QLR_LS> QLR=null;
  
  //扩展的属性
  private Map<String,String> Attribute=new HashMap<String, String>() ;
  
  public Map<String,String> getQl() throws Exception {
	if(QL==null)
		QL=new BDCS_QL_LS();
	Map<String,String> map_ql=transBean2Map(QL);//将对象转换成MAP
	//字典转换相应字段
   map_ql.put("CZFS", ConstHelper.getNameByValue("CZFS",QL.getCZFS()));
   map_ql.put("QLLX", ConstHelper.getNameByValue("QLLX",QL.getQLLX()));
   map_ql.put("QLJSSJ", StringHelper.FormatByDatetime(QL.getQLJSSJ()));
   map_ql.put("QLQSSJ", StringHelper.FormatByDatetime(QL.getQLQSSJ()));
   map_ql.put("QSZT", ConstHelper.getNameByValue("QSZT",String.valueOf(QL.getQSZT())));
   map_ql.put("DJSJ", StringHelper.FormatByDatetime(QL.getDJSJ()));
   map_ql.put("DJLX", ConstHelper.getNameByValue("DJLX",QL.getDJLX()));
   map_ql.put("MODIFYTIME", StringHelper.FormatByDatetime(QL.getMODIFYTIME()));
   map_ql.put("CREATETIME", StringHelper.FormatByDatetime(QL.getCREATETIME()));
   map_ql.put("ZSEWM","");
   map_ql.put("ZSBS", ConstHelper.getNameByValue("ZSBS",QL.getZSBS()));
	return (Map<String, String>) map_ql;
  }
  
  public void setQl(BDCS_QL_LS QL) {
	this.QL = QL;
  }
  
  public Map<String,String> getFsql() throws Exception {
	if(FSQL==null)
		FSQL=new BDCS_FSQL_LS();
	Map<String,String> map_fsql=transBean2Map(FSQL);
   map_fsql.put("YWRZJZL", ConstHelper.getNameByValue("ZJLX",FSQL.getYWRZJZL()));
   map_fsql.put("GYDQLRZJZL", ConstHelper.getNameByValue("ZJLX",FSQL.getGYDQLRZJZL()));
   map_fsql.put("YZYFS", ConstHelper.getNameByValue("YZYFS",FSQL.getYZYFS()));
   map_fsql.put("TDSYQXZ", ConstHelper.getNameByValue("TDSYQXZ",FSQL.getTDSYQXZ()));
   map_fsql.put("FWXZ", ConstHelper.getNameByValue("FWXZ",FSQL.getFWXZ()));
   map_fsql.put("FWJG", ConstHelper.getNameByValue("FWJG",FSQL.getFWJG()));
   map_fsql.put("DYBDCLX", ConstHelper.getNameByValue("DYBDCLX",FSQL.getDYBDCLX()));
   map_fsql.put("DYFS", ConstHelper.getNameByValue("DYFS",FSQL.getDYFS()));
   map_fsql.put("DYWLX", ConstHelper.getNameByValue("DYBDCLX",FSQL.getDYWLX()));
   map_fsql.put("GJZWLX", ConstHelper.getNameByValue("GZWLX",FSQL.getGJZWLX()));
   map_fsql.put("LDSYQXZ", ConstHelper.getNameByValue("TDSYQXZ",FSQL.getLDSYQXZ()));
   map_fsql.put("CFLX", ConstHelper.getNameByValue("CFLX",FSQL.getCFLX()));
   map_fsql.put("SYTTLX", ConstHelper.getNameByValue("SYTTLX",FSQL.getSYTTLX()));
   map_fsql.put("GHYT", ConstHelper.getNameByValue("FWYT",FSQL.getGHYT()));
   map_fsql.put("QY", ConstHelper.getNameByValue("QY",FSQL.getQY()));
   map_fsql.put("YGDJZL", ConstHelper.getNameByValue("YGDJZL",FSQL.getYGDJZL()));
   
   map_fsql.put("CFSJ", StringHelper.FormatByDatetime(FSQL.getCFSJ()));
   map_fsql.put("ZXSJ", StringHelper.FormatByDatetime(FSQL.getZXSJ()));
   map_fsql.put("JGSJ", StringHelper.FormatByDatetime(FSQL.getJGSJ()));
   map_fsql.put("MODIFYTIME", StringHelper.FormatByDatetime(FSQL.getMODIFYTIME()));
   map_fsql.put("CREATETIME", StringHelper.FormatByDatetime(FSQL.getCREATETIME()));
   return (Map<String, String>) map_fsql;
  }
  
  public void setFsql(BDCS_FSQL_LS FSQL) {
	this.FSQL = FSQL;
  }
  
  public Map<String,String> getQlr() throws Exception {//只获取第一行的权利人
	 StringBuilder qlrmcs = new StringBuilder();
	 BDCS_QLR_LS r=null;
	 if(QLR!=null&&!QLR.isEmpty())
	 {
		 for(int i = 0; i < QLR.size(); i++)
		 {
			 qlrmcs.append(QLR.get(i).getQLRMC());
			 if(i<(QLR.size()-1))//不是最后一条
				 qlrmcs.append("，");
		 }

	  r=QLR.get(0);
	 }
	 if(r==null)
			r=new BDCS_QLR_LS();	
	   Map<String,String> map_qlr=transBean2Map(r);
	   if(QLR!=null&&!QLR.isEmpty()) {
	   if(QLR.size()>1)
	   map_qlr.put("GYQK", "共同共有");
	   else
		  map_qlr.put("GYQK", "单独所有");
		 }
	   map_qlr.put("DLRZJLX", ConstHelper.getNameByValue("ZJLX",r.getDLRZJLX()));
	   map_qlr.put("GYFS", ConstHelper.getNameByValue("GYFS",r.getGYFS()));
	   map_qlr.put("GJ", ConstHelper.getNameByValue("GJDQ",r.getGJ()));
	   map_qlr.put("XB", ConstHelper.getNameByValue("XB",r.getXB()));
	   map_qlr.put("HJSZSS", ConstHelper.getNameByValue("SS",r.getHJSZSS()));
	   map_qlr.put("SSHY", ConstHelper.getNameByValue("SSHY",r.getSSHY()));
	   map_qlr.put("QLRLX", ConstHelper.getNameByValue("QLRLX",r.getQLRLX()));
	   map_qlr.put("FDDBRZJLX", ConstHelper.getNameByValue("ZJLX",r.getFDDBRZJLX()));
	   map_qlr.put("ZJZL", ConstHelper.getNameByValue("ZJLX",r.getZJZL()));
	   
	   map_qlr.put("MODIFYTIME", StringHelper.FormatByDatetime(r.getMODIFYTIME()));
	   map_qlr.put("CREATETIME", StringHelper.FormatByDatetime(r.getCREATETIME()));
	   
	   map_qlr.put("QLRSNAME", qlrmcs.toString());
	   return (Map<String, String>) map_qlr;
  }
  
  public void setQlr(List<BDCS_QLR_LS> QLR) {
	this.QLR = QLR;
  }
  
  public Map<String,String> getAttribute() {
	return Attribute;
  }
  
  public void setAttribute(Map<String,String> attribute) {
	Attribute = attribute;
  }
  
  //用反射将对象转换成MAP
  private Map<String, String> transBean2Map(Object obj) {  
    if(obj == null){  
        return null;  
    }          
    Map<String, String> map = new HashMap<String, String>();  
    try {  
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());  
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();  
        for (PropertyDescriptor property : propertyDescriptors) {  
            String key = property.getName();  

            // 过滤class属性  
            if (!key.equals("class")) {  
                // 得到property对应的getter方法  
                Method getter = property.getReadMethod();  
                Object value = getter.invoke(obj);  

                map.put(key, String.valueOf(value));  
            }  

        }  
    } catch (Exception e) {  
        System.out.println("transBean2Map Error " + e);  
    }  
    return map;  
  }
  
}
