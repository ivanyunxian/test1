package com.supermap.realestate.registrationbook.model;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_YHYDZB_LS;
import com.supermap.realestate.registration.model.BDCS_YHZK_LS;
import com.supermap.realestate.registration.model.BDCS_ZH_LS;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.StringHelper;
// 已不用 可删除
public class ZHJBXXInfo {
	
	//登记单元
	private BDCS_DJDY_LS djdy;
		
	//宗海信息
	private BDCS_ZH_LS zh;
		
	//用海状况信息
	private List<BDCS_YHZK_LS> yhzks;
		
	//用海用岛坐标
	private List<BDCS_YHYDZB_LS> yhydzbs;
		
	//宗海变化情况 (没加MODEL)
	//private List<BDCS_ZHBHQK_LS> zhbhqk;
		
	//权利表
	private BDCS_QL_LS ql;
		
	//附属权利表
	private BDCS_FSQL_LS fsql;
		
	//项目信息
	private BDCS_XMXX xmxx;
		
	public Map<String,String> getDjdy() {
		if(djdy==null)
			djdy=new BDCS_DJDY_LS();
		Map<String,String> map_djdy=transBean2Map(djdy);
		map_djdy.put("BDCDYLXCODE",djdy.getBDCDYLX());
		map_djdy.put("BDCDYLX", ConstHelper.getNameByValue("BDCDYLX",djdy.getBDCDYLX()));
		map_djdy.put("MODIFYTIME", StringHelper.FormatByDatetime(djdy.getModifyTime()));
		map_djdy.put("CREATETIME", StringHelper.FormatByDatetime(djdy.getCreateTime()));
	    return (Map<String, String>) map_djdy;
	}
		
	public void setDjdy(BDCS_DJDY_LS djdy) {
		this.djdy = djdy;
	}
		
	public Map<String, String> getZh() {
		if(zh==null)
			zh=new BDCS_ZH_LS();
		Map<String,String> map_zh=transBean2Map(zh);
		map_zh.put("ZHTZM", ConstHelper.getNameByValue("TZM",zh.getZHTZM()));
		map_zh.put("ZT", ConstHelper.getNameByValue("BDCDYZT",zh.getZT()));
		map_zh.put("YHLXA", ConstHelper.getNameByValue("HYSYLXA",zh.getYHLXA()));
		map_zh.put("YHLXB", ConstHelper.getNameByValue("HYSYLXB",zh.getYHLXB()));
		map_zh.put("DB", ConstHelper.getNameByValue("HYDB",zh.getDB()));
		map_zh.put("XMXX", ConstHelper.getNameByValue("XMXZ",zh.getXMXX()));

		map_zh.put("SHRQ", StringHelper.FormatByDatetime(zh.getSHRQ()));
		map_zh.put("HCRQ", StringHelper.FormatByDatetime(zh.getHCRQ()));
		map_zh.put("CLRQ", StringHelper.FormatByDatetime(zh.getCLRQ()));
		map_zh.put("MODIFYTIME", StringHelper.FormatByDatetime(zh.getMODIFYTIME()));
		map_zh.put("CREATETIME", StringHelper.FormatByDatetime(zh.getCREATETIME()));
		return (Map<String, String>) map_zh;
	}
		
	public void setZh(BDCS_ZH_LS zh) {
		this.zh = zh;
	}
		
	public List<Map<String, String>> getYhzks() {
		List<Map<String, String>> _list=new  ArrayList<Map<String, String>>();
		for(BDCS_YHZK_LS yhzk: yhzks){
			Map<String,String> map_yhzk=transBean2Map(yhzk);
			map_yhzk.put("YHFS", ConstHelper.getNameByValue("YHFS",yhzk.getYHFS()));
			map_yhzk.put("MODIFYTIME", StringHelper.FormatByDatetime(yhzk.getMODIFYTIME()));
			map_yhzk.put("CREATETIME", StringHelper.FormatByDatetime(yhzk.getMODIFYTIME()));
			_list.add(map_yhzk);
		}
		return (List<Map<String, String>>) _list;
	}
		
	public void setYhzks(List<BDCS_YHZK_LS> yhzks) {
		this.yhzks = yhzks;
	}
		
	public Map<String, String> getQl() {
		if(ql==null)
			ql=new BDCS_QL_LS();
		Map<String,String> map_ql=transBean2Map(ql);
	    map_ql.put("CZFS", ConstHelper.getNameByValue("CZFS",ql.getCZFS()));
		map_ql.put("QLLX", ConstHelper.getNameByValue("QLLX",ql.getQLLX()));
		map_ql.put("QLJSSJ", StringHelper.FormatByDatetime(ql.getQLJSSJ()));
		map_ql.put("QLQSSJ", StringHelper.FormatByDatetime(ql.getQLQSSJ()));
		map_ql.put("QSZT", ConstHelper.getNameByValue("QSZT",String.valueOf(ql.getQSZT())));
		map_ql.put("DJSJ", StringHelper.FormatByDatetime(ql.getDJSJ()));
		map_ql.put("DJLX", ConstHelper.getNameByValue("DJLX",ql.getDJLX()));
		map_ql.put("MODIFYTIME", StringHelper.FormatByDatetime(ql.getMODIFYTIME()));
		map_ql.put("CREATETIME", StringHelper.FormatByDatetime(ql.getCREATETIME()));
		map_ql.put("ZSEWM","");
		map_ql.put("ZSBS", ConstHelper.getNameByValue("ZSBS",ql.getZSBS()));
		return (Map<String, String>) map_ql;
	}
		
	public void setQl(BDCS_QL_LS ql) {
		this.ql = ql;
	}
		
	public List<Map<String, String>> getYhydzbs() {
		List<Map<String, String>> _list=new  ArrayList<Map<String, String>>();
		for(BDCS_YHYDZB_LS yhydzb: yhydzbs){
			Map<String,String> map_yhydzb=transBean2Map(yhydzb);
			map_yhydzb.put("MODIFYTIME", StringHelper.FormatByDatetime(yhydzb.getMODIFYTIME()));
			map_yhydzb.put("CREATETIME", StringHelper.FormatByDatetime(yhydzb.getCREATETIME()));
			_list.add(map_yhydzb);
		}
		return (List<Map<String, String>>) _list;
		//return yhydzbs;
	}
		
	public void setYhydzbs(List<BDCS_YHYDZB_LS> yhydzbs) {
		this.yhydzbs = yhydzbs;
	}
		
	public Map<String, String> getFsql() {
		if(fsql==null)
			fsql=new BDCS_FSQL_LS();
		Map<String,String> map_fsql=transBean2Map(fsql);
		map_fsql.put("YWRZJZL", ConstHelper.getNameByValue("ZJLX",fsql.getYWRZJZL()));
		map_fsql.put("GYDQLRZJZL", ConstHelper.getNameByValue("ZJLX",fsql.getGYDQLRZJZL()));
		map_fsql.put("YZYFS", ConstHelper.getNameByValue("YZYFS",fsql.getYZYFS()));
		map_fsql.put("TDSYQXZ", ConstHelper.getNameByValue("TDSYQXZ",fsql.getTDSYQXZ()));
        map_fsql.put("FWXZ", ConstHelper.getNameByValue("FWXZ",fsql.getFWXZ()));
	    map_fsql.put("FWJG", ConstHelper.getNameByValue("FWJG",fsql.getFWJG()));
	    map_fsql.put("DYBDCLX", ConstHelper.getNameByValue("DYBDCLX",fsql.getDYBDCLX()));
	    map_fsql.put("DYFS", ConstHelper.getNameByValue("DYFS",fsql.getDYFS()));
	    map_fsql.put("DYWLX", ConstHelper.getNameByValue("DYBDCLX",fsql.getDYWLX()));
	    map_fsql.put("GJZWLX", ConstHelper.getNameByValue("GZWLX",fsql.getGJZWLX()));
	    map_fsql.put("LDSYQXZ", ConstHelper.getNameByValue("TDSYQXZ",fsql.getLDSYQXZ()));
	    map_fsql.put("CFLX", ConstHelper.getNameByValue("CFLX",fsql.getCFLX()));
	    map_fsql.put("SYTTLX", ConstHelper.getNameByValue("SYTTLX",fsql.getSYTTLX()));
	    map_fsql.put("GHYT", ConstHelper.getNameByValue("FWYT",fsql.getGHYT()));
	    map_fsql.put("QY", ConstHelper.getNameByValue("QY",fsql.getQY()));
	    map_fsql.put("YGDJZL", ConstHelper.getNameByValue("YGDJZL",fsql.getYGDJZL()));

	    map_fsql.put("CFSJ", StringHelper.FormatByDatetime(fsql.getCFSJ()));
		map_fsql.put("ZXSJ", StringHelper.FormatByDatetime(fsql.getZXSJ()));
	    map_fsql.put("JGSJ", StringHelper.FormatByDatetime(fsql.getJGSJ()));
	    map_fsql.put("MODIFYTIME", StringHelper.FormatByDatetime(fsql.getMODIFYTIME()));
	    map_fsql.put("CREATETIME", StringHelper.FormatByDatetime(fsql.getCREATETIME()));
	    return (Map<String, String>) map_fsql;
	}
		
	public void setFsql(BDCS_FSQL_LS fsql) {
		this.fsql = fsql;
	}
		
	public Map<String, String> getXmxx() {
		if(xmxx==null)
			xmxx=new BDCS_XMXX();
		Map<String,String> map_xmxx=transBean2Map(xmxx);
		map_xmxx.put("SFDB", ConstHelper.getNameByValue("SFZD",xmxx.getSFDB()));
		map_xmxx.put("SLSJ", StringHelper.FormatByDatetime(xmxx.getSLSJ()));
		map_xmxx.put("MODIFYTIME", StringHelper.FormatByDatetime(xmxx.getMODIFYTIME()));
		map_xmxx.put("CREATETIME", StringHelper.FormatByDatetime(xmxx.getCREATETIME())); 
		return (Map<String, String>) map_xmxx;
	}
		
	public void setXmxx(BDCS_XMXX xmxx) {
		this.xmxx = xmxx;
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
