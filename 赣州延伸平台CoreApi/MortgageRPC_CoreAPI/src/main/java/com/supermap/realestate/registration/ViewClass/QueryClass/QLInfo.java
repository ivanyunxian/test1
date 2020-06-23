package com.supermap.realestate.registration.ViewClass.QueryClass;

import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.model.BDCS_PARTIALLIMIT;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;

public class QLInfo {
	private Rights ql = null;

	private SubRights fsql = null;

	private List<RightsHolder> qlrlist = null;

	private RightsHolder limitqlr = null;
	
	private Map<String, String> lsql = null;

	public Map<String, String> getLsql() {
		return lsql;
	}

	public void setLsql(Map<String, String> qlr_id) {
		this.lsql = qlr_id;
	}

	public Rights getql(){
		return ql;
	}
	
	public SubRights getfsql(){
		return fsql;
	}
	
	public List<RightsHolder> getqlrlist(){
		return qlrlist;
	}
	
	
	public void setql(Rights value){
		ql=value;
	}
	
	public void setfsql(SubRights value){
		fsql=value;
	}
	
	public void setqlrlist(List<RightsHolder> value){
		qlrlist=value;
	}
	
	
	public void setLimitqlr(RightsHolder value) {
		limitqlr = value;
	}
	
	public RightsHolder getLimitqlr(){
		return limitqlr;
	}
}
