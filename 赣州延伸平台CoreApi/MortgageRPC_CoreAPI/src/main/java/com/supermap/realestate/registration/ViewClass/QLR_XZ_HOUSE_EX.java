/**
 * 
 */
package com.supermap.realestate.registration.ViewClass;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/**
 * @author WUZHU
 * 现状库权利人的扩展表，用于房屋信息查询。以单条权利人表数据为入口，封装了和房屋查询相关得所有数据。
 */
public class QLR_XZ_HOUSE_EX  extends BDCS_QLR_XZ{
	   //扩展属性
		private Map<String,String> ex;
		//权利人对应的房屋单元
		private House house;
	    //对应的权利
		private BDCS_QL_XZ ql;
		//对应的登记单元，该单元对象包含了权利数据及房产权利人数据
	    private DJDY_XZ_EX djdy;
	    //初始化扩展类
	public void Init(CommonDao dao) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	  {
		BDCS_QL_XZ _ql=dao.get(BDCS_QL_XZ.class, super.getQLID());//初始化权利属性
		if(_ql!=null)
			this.setQl(_ql);
		//初始化登记单元属性
		List<BDCS_DJDY_XZ> _djdys=dao.getDataList(BDCS_DJDY_XZ.class, " DJDYID='"+this.getQl().getDJDYID()+"' AND BDCDYLX='"+ConstValue.BDCDYLX.H.Value+"'");
        if(_djdys!=null&&_djdys.size()>0)
        {

        	DJDY_XZ_EX djdy_xz_ex = new DJDY_XZ_EX();
			  PropertyUtils.copyProperties(djdy_xz_ex, _djdys.get(0));
			  djdy_xz_ex.Init(dao);
			  RealUnit _unit=UnitTools.loadUnit(ConstValue.BDCDYLX.initFrom(djdy_xz_ex.getBDCDYLX()), ConstValue.DJDYLY.XZ,djdy_xz_ex.getBDCDYID());
			  if (_unit instanceof House)//初始化房屋单元属性
				  this.setHouse((House)_unit);
        }
        boolean state_23=false;//抵押状态
        boolean state_800=false;//查封状态
        boolean state_600=false;//异议状态
        for(BDCS_QL_XZ ql_xz:this.getDjdy().getQl_xzs())//遍历权利类型判断状态
        {
        	if(!state_23)//只判断一次
      		  state_23=get23State(ql_xz);
      	  if(!state_600)
      		  state_600=get600State(ql_xz);
      	  if(!state_800)
      		  state_800=get800State(ql_xz);
        }
        //判断该权利人状态
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
    private boolean get23State(BDCS_QL_XZ ql_xz)
    {
 	   boolean r=false;
 	   if(ConstValue.QLLX.DIYQ.Value.equals(ql_xz.getQLLX()))
 	   {
            r=true;
            return r;
 	   }
 		   return r;
    }
    //判断该查封登记是否已经注销，TRUE 是有查封状态 ，FALSE无查封状态
    private boolean get800State(BDCS_QL_XZ ql_xz)
    {
 	   boolean r=false;
 	   if(ConstValue.DJLX.CFDJ.Value.equals(ql_xz.getDJLX()))
 	   {
 		    r=true;
            return r;
 	   }
 		   return r;
    }
    //判断该异议登记是否已经注销，TRUE 是有异议状态 ，FALSE无异议状态
    private boolean get600State(BDCS_QL_XZ ql_xz)
    {
 	   boolean r=false;
 	   if(ConstValue.DJLX.YYDJ.Value.equals(ql_xz.getDJLX()))
 	   {
 		    r=true;
            return r;
 	   }
 		   return r;
    }
	public House getHouse() {
		if(house==null)
			return new BDCS_H_XZ();
		return house;
	}
	public BDCS_QL_XZ getQl() {
		if(ql==null)
			return new BDCS_QL_XZ();
		return ql;
	}
	public void setQl(BDCS_QL_XZ ql) {
		this.ql = ql;
	}
	public void setHouse(House house) {
		this.house = house;
	}
	public DJDY_XZ_EX getDjdy() {
		if(djdy==null)
			return new DJDY_XZ_EX();
		return djdy;
	}
	public void setDjdy(DJDY_XZ_EX djdy) {
		this.djdy = djdy;
	}
	public Map<String,String> getEx() {
		return ex;
	}
	public void setEx(Map<String,String> ex) {
		this.ex = ex;
	}
	//现状的登记单元扩展类，以单条登记单元数据为入口，封装了房屋查询需要
	public  class DJDY_XZ_EX extends BDCS_DJDY_XZ{
		//该登记单元包含的权利列表
		private List<BDCS_QL_XZ> ql_xzs; 
		//该登记单元的国有建设用地使用权/房屋（构筑物）所有权 包含的权利人列表
		private List<BDCS_QLR_XZ> qlrs_4;
		public void Init(CommonDao dao)
		  {
			List<BDCS_QL_XZ> _qls=dao.getDataList(BDCS_QL_XZ.class, " DJDYID='"+super.getDJDYID()+"'");
			this.setQl_xzs(_qls);
			for(BDCS_QL_XZ ql_xz:_qls)
			{
				//只初始化房屋的权利人
				if(ConstValue.QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(ql_xz.getQLLX()))
				{
					List<BDCS_QLR_XZ> _qlrs=dao.getDataList(BDCS_QLR_XZ.class, " QLID='"+ql_xz.getId()+"'");
					this.setQlrs_4(_qlrs);
				}
			}
		  }
		
		 public List<BDCS_QL_XZ> getQl_xzs() {
			 if(ql_xzs==null)
			  return new ArrayList<BDCS_QL_XZ>();
			return ql_xzs;
		}
		public List<BDCS_QLR_XZ> getQlrs_4() {
			if(qlrs_4==null)
				  return new ArrayList<BDCS_QLR_XZ>();
			return qlrs_4;
		}
		public void setQlrs_4(List<BDCS_QLR_XZ> qlrs_4) {
			this.qlrs_4 = qlrs_4;
		}
		public void setQl_xzs(List<BDCS_QL_XZ> ql_xzs) {
			this.ql_xzs = ql_xzs;
		}
	
	}
}

