package com.supermap.realestate.registration.ViewClass;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_H_LS;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_LS;
import com.supermap.realestate.registration.model.BDCS_SLLM_LS;
import com.supermap.realestate.registration.model.BDCS_SYQZD_LS;
import com.supermap.realestate.registration.model.BDCS_ZH_LS;
import com.supermap.realestate.registration.model.BDCS_ZRZ_LS;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
//用于不动产登记台账。以单条登记单元数据为入口，封装了不动产登记台账所需得所有相关数据，方便进行业务判断。 WUZHU
public class DJDY_LS_EX extends BDCS_DJDY_LS{
	//扩展属性
	private Map<String,String> ex;
	//登记单元对应的不动产单元属性
	private Object realUnit;
	//登记单元对应的权利集
   private List<QL_LS_EX> ql_ls_ex;
   //初始化本类
  public void Init(CommonDao dao) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
  {
	  Object u=null;
	  if(BDCDYLX.SYQZD.Value.equals(super.getBDCDYLX()))
		   u = dao.get(BDCS_SYQZD_LS.class, super.getBDCDYID());
	  if(BDCDYLX.SHYQZD.Value.equals(super.getBDCDYLX()))
		   u = dao.get(BDCS_SHYQZD_LS.class, super.getBDCDYID());
	  if(BDCDYLX.ZRZ.Value.equals(super.getBDCDYLX()))
		   u = dao.get(BDCS_ZRZ_LS.class, super.getBDCDYID());
	  if(BDCDYLX.HY.Value.equals(super.getBDCDYLX()))
		   u = dao.get(BDCS_ZH_LS.class, super.getBDCDYID());
	  if(BDCDYLX.LD.Value.equals(super.getBDCDYLX()))
		   u =dao.get(BDCS_SLLM_LS.class, super.getBDCDYID());
	  if(BDCDYLX.H.Value.equals(super.getBDCDYLX()))
		   u = dao.get(BDCS_H_LS.class, super.getBDCDYID());
      this.setRealUnit(u);
      List<BDCS_QL_LS> ql_lss = dao.getDataList(BDCS_QL_LS.class, " DJDYID='"+super.getDJDYID()+"'");
      List<QL_LS_EX> ql_ls_exs=new ArrayList<QL_LS_EX>();
      boolean state_23=false;
      boolean state_800=false;
      boolean state_600=false;
      for(BDCS_QL_LS ql_ls:ql_lss)
      {
    	  QL_LS_EX ql_ls_ex=new QL_LS_EX();
    	  PropertyUtils.copyProperties(ql_ls_ex, ql_ls);
    	  ql_ls_ex.Init(dao);
    	  if(!state_23)
    		  state_23=get23State(ql_ls_ex);
    	  if(!state_600)
    		  state_600=get600State(ql_ls_ex);
    	  if(!state_800)
    		  state_800=get800State(ql_ls_ex);
    	  ql_ls_exs.add(ql_ls_ex);
      }
      this.setQl_ls_ex(ql_ls_exs);
      Map<String,String> ex=new HashMap<String,String>();
      if(state_23)
    	  ex.put("state_23", "有抵押");
      else
    	  ex.put("state_23", "无抵押");
      if(state_600)
    	  ex.put("state_600", "有异议");
      else
    	  ex.put("state_600", "无异议");
      if(state_800)
    	  ex.put("state_800", "有查封");
      else
    	  ex.put("state_800", "无查封");
      this.setEx(ex);
  }
   //判断该抵押权利是否已经注销，TRUE 是 有抵押权，FALSE是抵押权已经注销
   private boolean get23State(QL_LS_EX ql_ex)
   {
	   boolean r=false;
	   if(ConstValue.QLLX.DIYQ.Value.equals(ql_ex.getQLLX()))
	   {
		   if(!StringUtils.isEmpty(ql_ex.getFsql().getZXSJ())&&!StringUtils.isEmpty(ql_ex.getFsql().getZXDBR()))
			   r=false;
		   else
			   r=true;
	   }
		   return r;
   }
   //判断该查封登记是否已经注销，TRUE 是有查封状态 ，FALSE无查封状态
   private boolean get800State(QL_LS_EX ql_ex)
   {
	   boolean r=false;
	   if(ConstValue.DJLX.CFDJ.Value.equals(ql_ex.getDJLX()))
	   {
		   if(!StringUtils.isEmpty(ql_ex.getFsql().getZXSJ())&&!StringUtils.isEmpty(ql_ex.getFsql().getZXDBR()))
			   r=false;
		   else
			   r=true;
	   }
		   return r;
   }
   //判断该异议登记是否已经注销，TRUE 是有异议状态 ，FALSE无异议状态
   private boolean get600State(QL_LS_EX ql_ex)
   {
	   boolean r=false;
	   if(ConstValue.DJLX.YYDJ.Value.equals(ql_ex.getDJLX()))
	   {
		   if(!StringUtils.isEmpty(ql_ex.getFsql().getZXSJ())&&!StringUtils.isEmpty(ql_ex.getFsql().getZXDBR()))
			   r=false;
		   else
			   r=true;
	   }
		   return r;
   }
   public Object getRealUnit() {
	return realUnit;
}
private void setRealUnit(Object realUnit) {
	this.realUnit = realUnit;
}
public List<QL_LS_EX> getQl_ls_ex() {
	return ql_ls_ex;
}
public void setQl_ls_ex(List<QL_LS_EX> ql_ls_ex) {
	this.ql_ls_ex = ql_ls_ex;
}
public Map<String,String> getEx() {
	return ex;
}

public void setEx(Map<String,String> ex) {
	this.ex = ex;
}

}

