package com.supermap.realestate.registration.ViewClass;

import java.util.Date;


/**
 * 历史回溯是权利对应的单元信息
 * 
 * @author 孙海豹
 *
 */
public class HistoryUnitInfo {
	private String bdcdyid;
	public String getBdcdyid() {
		return bdcdyid;
	}
	public void setBdcdyid(String bdcdyid) {
		this.bdcdyid = bdcdyid;
	}
	public String getXmbh() {
		return xmbh;
	}
	public void setXmbh(String xmbh) {
		this.xmbh = xmbh;
	}
	public Date getDjsj() {
		return djsj;
	}
	public void setDjsj(Date djsj) {
		this.djsj = djsj;
	}
	private Date slsj;
	public Date getSlsj() {
		return slsj;
	}
	public void setSlsj(Date slsj) {
		this.slsj = slsj;
	}
	private String xmbh;
	private Date djsj;
	
	/**
	 * 是否是更正登记
	 */
	private String Isgzdj;
	
	public String getIsgzdj() {
		return Isgzdj;
	}
	public void setIsgzdj(String isgzdj) {
		Isgzdj = isgzdj;
	}
	public String getIsgzdjasfdytdj() {
		return Isgzdjasfdytdj;
	}
	public void setIsgzdjasfdytdj(String isgzdjasfdytdj) {
		Isgzdjasfdytdj = isgzdjasfdytdj;
	}
	/**
	 * 是否是更正登记对应的房地一体登记
	 */
	private String Isgzdjasfdytdj;
}
