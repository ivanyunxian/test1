package com.supermap.realestate.registration.ViewClass;

import java.util.List;

import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_QLR_LS;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

//用于不动产登记台账 WUZHU
public class QL_LS_EX  extends BDCS_QL_LS{

	private List<BDCS_QLR_LS> qlr_lss;
	
	private BDCS_FSQL_LS fsql;
	
	public void Init(CommonDao dao) {
	    List<BDCS_QLR_LS> _qlr_lss = dao.getDataList(BDCS_QLR_LS.class, " QLID='"+super.getId()+"'");
	    if(!StringUtils.isEmpty( super.getFSQLID()))
	    {
	    BDCS_FSQL_LS _fsql =dao.get(BDCS_FSQL_LS.class, super.getFSQLID());
	    this.setFsql(_fsql);
	    }
	    else
	    {
	    	 this.setFsql(new BDCS_FSQL_LS());
	    }
	    this.setQlr_lss(_qlr_lss);
	}
	
    public List<BDCS_QLR_LS> getQlr_lss() {
		return qlr_lss;
	}
    
	public void setQlr_lss(List<BDCS_QLR_LS> qlr_lss) {
		this.qlr_lss = qlr_lss;
	}
	
    public BDCS_FSQL_LS getFsql() {
		return fsql;
	}
    
	public void setFsql(BDCS_FSQL_LS fsql) {
		this.fsql = fsql;
	}
}
