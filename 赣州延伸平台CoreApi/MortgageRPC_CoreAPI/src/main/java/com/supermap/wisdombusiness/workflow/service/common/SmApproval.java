package com.supermap.wisdombusiness.workflow.service.common;

public class SmApproval {
    private String spdyid;
    private String spmc;
    private String splx;
    private String sign;
    private String actdefspdyid;
    private String actdefid;
    private int readonly;
    private int index;
    private String mryj;
    public String getSpdyid() {
		return spdyid;
	}
	public void setSpdyid(String spdyid) {
		this.spdyid = spdyid;
	}
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
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getActdefspdyid() {
		return actdefspdyid;
	}
	public void setActdefspdyid(String actdefspdyid) {
		this.actdefspdyid = actdefspdyid;
	}
	public String getActdefid() {
		return actdefid;
	}
	public void setActdefid(String actdefid) {
		this.actdefid = actdefid;
	}
	
	public SmApproval(){}
	/**
	 * @return the readonly
	 */
	public int getReadonly() {
		return readonly;
	}
	/**
	 * @param readonly the readonly to set
	 */
	public void setReadonly(int readonly) {
		this.readonly = readonly;
	}
	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	public String getMryj() {
		return mryj;
	}
	public void setMryj(String mryj) {
		this.mryj = mryj;
	}
	
}
