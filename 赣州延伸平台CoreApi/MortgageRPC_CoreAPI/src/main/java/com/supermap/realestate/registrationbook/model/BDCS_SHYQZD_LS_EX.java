package com.supermap.realestate.registrationbook.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.model.BDCS_SHYQZD_LS;
import com.supermap.realestate.registration.model.BDCS_ZDBHQK_LS;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.StringHelper;

/**
 * @author WUZ  已不用 可删除
 * 不动产使用权宗地表扩展，用在宗地基本信息表中 已不用 可删除
 * @param <BDCS_ZDBHQK_LS>
 *
 */
public class BDCS_SHYQZD_LS_EX extends BDCS_SHYQZD_LS {
	private String fj;
	private String djsj;
	
	private String bdcdylx;
	
	private String dor;
	
	private List<BDCS_ZDBHQK_LS> bhqk;
	
	@SuppressWarnings("unused")
	private String djmc2;
	
	@SuppressWarnings("unused")
	private String qllxmc2;
	
	@SuppressWarnings("unused")
	private String ytmc2;
	
	@SuppressWarnings("unused")
	private String qlxzmc2;
	
	public String getDjsj() {
		return djsj;
	}
	
	public void setDjsj(String djsj) {
		this.djsj = djsj;
	}
	
	public String getDor() {
		return dor;
	}
	
	public void setDor(String dor) {
		this.dor = dor;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getBhqk() {
		if (bhqk == null)
		bhqk = new ArrayList<BDCS_ZDBHQK_LS>();
		List<Map> maps=new ArrayList<Map>();
        for(BDCS_ZDBHQK_LS zdbhqk:bhqk)
        {
        	 Map<String, String> map = new HashMap<String, String>();
        	 map.put("bhyh", zdbhqk.getBHYH());
        	 map.put("bhnr", zdbhqk.getBHNR());
        	 map.put("djsj", StringHelper.FormatByDatetime(zdbhqk.getDJSJ()));
        	 map.put("dbr", zdbhqk.getDBR());
        	 maps.add(map);
        	//zdbhqk.setDJSJ(StringHelper.FormatByDatetime(zdbhqk.getDJSJ())));
        }
		return maps;
	}
	
	public void setBhqk(List<BDCS_ZDBHQK_LS> bhqk) {
		this.bhqk = bhqk;
	}
	
	public String getDjmc2() {
		return ConstHelper.getNameByValue("TDDJ",super.getDJ());
	}
	
	public void setDjmc2(String djmc2) {
		this.djmc2 = djmc2;
	}
	
	public String getQllxmc2() {
		return ConstHelper.getNameByValue("QLLX",super.getQLLX());
	}
	
	public void setQllxmc2(String qllxmc2) {
		this.qllxmc2 = qllxmc2;
	}
	
	public String getYtmc2() {
		return ConstHelper.getNameByValue("TDYT",super.getYT());
	}
	
	public void setYtmc2(String ytmc2) {
		this.ytmc2 = ytmc2;
	}
	
	public String getQlxzmc2() {
		return ConstHelper.getNameByValue("QLXZ",super.getQLXZ());
	}
	
	public void setQlxzmc2(String qlxzmc2) {
		this.qlxzmc2 = qlxzmc2;
	}

	public String getBdcdylx() {
		return bdcdylx;
	}

	public void setBdcdylx(String bdcdylx) {
		this.bdcdylx = bdcdylx;
	}

	public String getFj() {
		return fj;
	}

	public void setFj(String fj) {
		this.fj = fj;
	}
}
