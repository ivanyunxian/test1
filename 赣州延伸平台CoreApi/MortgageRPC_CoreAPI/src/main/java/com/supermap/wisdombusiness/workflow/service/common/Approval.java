package com.supermap.wisdombusiness.workflow.service.common;

import java.util.List;

import com.supermap.wisdombusiness.workflow.model.Wfi_Spyj;

//审批意见
public class Approval {
	public Approval(){
		
	}
    private String spdyid;
	private String spmc;
	private String splx;
	private Integer signtypeString;
	private List<Wfi_Spyj> spyjs;
	private String mryj;
	private Integer readonly;
	private String singjg;
	private String bdcdyh;
	public String getSpmc() {
		return spmc;
	}
	public void setSpmc(String spmc) {
		this.spmc = spmc;
	}
	public String getSplx() {
		return splx;
	}
	public void setSplx(String splx) {
		this.splx = splx;
	}
	public Integer getSigntypeString() {
		return signtypeString;
	}
	public void setSigntypeString(Integer signtypeString) {
		this.signtypeString = signtypeString;
	}
	/**
	 * @return the spyjs
	 */
	public List<Wfi_Spyj> getSpyjs() {
		return spyjs;
	}
	/**
	 * @param spyjs the spyjs to set
	 */
	public void setSpyjs(List<Wfi_Spyj> spyjs) {
		this.spyjs = spyjs;
	}
	/**
	 * @return the readonly
	 */
	public Integer getReadonly() {
		return readonly;
	}
	/**
	 * @param readonly the readonly to set
	 */
	public void setReadonly(Integer readonly) {
		this.readonly = readonly;
	}
	/**
	 * @return the spdyid
	 */
	public String getSpdyid() {
		return spdyid;
	}
	/**
	 * @param spdyid the spdyid to set
	 */
	public void setSpdyid(String spdyid) {
		this.spdyid = spdyid;
	}
	public String getMryj() {
		return mryj;
	}
	public void setMryj(String mryj) {
		this.mryj = mryj;
	}
	public String getSingjg() {
		return singjg;
	}
	public void setSingjg(String singjg) {
		this.singjg = singjg;
	}
	public String getBdcdyh() {
		return bdcdyh;
	}
	public void setBdcdyh(String bdcdyh) {
		this.bdcdyh = bdcdyh;
	}

	

	
}


