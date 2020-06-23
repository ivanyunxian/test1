package com.supermap.realestate_gx.registration.model;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.wisdombusiness.web.ui.tree.Tree;



//数据模型：包括不动产单元信息、登记单元信息、权利信息、附属权利信息、权利人信息。WUZHU
public class Unit_EX {
	
private	RealUnit unit;//单条登记的不动产
  private BDCS_SQR sqr;
  private   BDCS_DJDY_XZ didy;//该不动产单元对应的登记单元
  private   List<QL_EX> qls;//不动产单元对应的权利集合
  public class QL_EX
  {
	
		private BDCS_QL_XZ ql;//单条权利数据
	  	private BDCS_FSQL_XZ fsql;//该权利对应的附属权利
	  	private List<BDCS_QLR_XZ> qlrs;//该权利对应的权利人集合
		public BDCS_QL_XZ getQl() {
			return ql;
		}
		public void setQl(BDCS_QL_XZ ql) {
			this.ql = ql;
		}
		public BDCS_FSQL_XZ getFsql() {
			return fsql;
		}
		public void setFsql(BDCS_FSQL_XZ fsql) {
			this.fsql = fsql;
		}
		public List<BDCS_QLR_XZ> getQlrs() {
			return qlrs;
		}
		public void setQlrs(List<BDCS_QLR_XZ> qlrs) {
			  Collections.sort(qlrs,new Comparator<BDCS_QLR_XZ>() {
				  @Override
			public int compare(BDCS_QLR_XZ qlri, BDCS_QLR_XZ qlrj) {
					 if(!StringUtils.isEmpty(qlri.getQLRMC())&&!StringUtils.isEmpty(qlrj.getQLRMC()))
					    return qlri.getQLRMC().compareTo(qlrj.getQLRMC());
					 else 
						 return 0;
					  
				  }
			  });  
			this.qlrs = qlrs;
		}
	  	
  
  }
 
public List<QL_EX> getQls() {
	if(qls==null)
		return  new ArrayList<QL_EX>();         
		return qls;
	}
	public void setQls(List<QL_EX> qls) {
		  Collections.sort(qls,new Comparator<QL_EX>() {
			  @Override
		public int compare(QL_EX qli, QL_EX qlj) {
				  if(!StringUtils.isEmpty(qli.getQl().getBDCQZH())&&!StringUtils.isEmpty(qlj.getQl().getBDCQZH()))
				 return qli.getQl().getBDCQZH().compareTo(qlj.getQl().getBDCQZH());
				  else
					  return 0;
				  
			  }
		  });  
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
	                Object value = getter.invoke(obj);  

	                map.put(key, value);  
	            }  

	        }  
	    } catch (Exception e) {  
	        System.out.println("对象转换为MAP失败： " + e);  
	    }  
	    return map;  
	  }
	public RealUnit getUnit() {
		return unit;
	}
	public void setUnit(RealUnit unit) {
		this.unit = unit;
	}
	public BDCS_DJDY_XZ getDidy() {
		return didy;
	}
	public void setDidy(BDCS_DJDY_XZ didy) {
		this.didy = didy;
	}
	public BDCS_SQR getSqr() {
		return sqr;
	}
	public void setSqr(BDCS_SQR sqr) {
		this.sqr = sqr;
	}
	  
}
