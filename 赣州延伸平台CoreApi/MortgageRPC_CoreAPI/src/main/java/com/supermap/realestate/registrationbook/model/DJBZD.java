package com.supermap.realestate.registrationbook.model;

/**
 * 
 * @Description:登记薄宗地
 * @author wuzhu
 * @date 2015年7月10日 上午11:22:16
 * @Copyright SuperMap
 */
public class DJBZD {

	/**
	 * 宗地or宗海（值要么是宗地，要么是宗海）
	 */
	private String zdorzh;
	
	/**
	 * 宗地/宗海代码
	 */
	private String zdzhdm;
	
	/**
	 * 坐落
	 */
	private String zl;
	

	/**
	 * 不动产单元数
	 */
	private String bdcdys;
	

	/**
	 * 登记机构
	 */
	private String djjg;
	
	/**
	 * 不动产单元号
	 */
	private String bdcdyh;
	
	/**
	 * 不动产单元ID
	 */
	private String bdcdyid;
	
	public void setBdcdyh(String value) {
		this.bdcdyh = value;
	}
	
	public String getBdcdyh() {
		return this.bdcdyh;
	}
	
	public void setBdcdyid(String value) {
		this.bdcdyid = value;
	}
	
	public String getBdcdyid() {
		return this.bdcdyid;
	}

	public void setZdorzh(String zdorzh) {
		this.zdorzh = zdorzh;
	}
	
	public String getZdorzh() {
		return this.zdorzh;
	}

	public void setZdzhdm(String zdzhdm) {
		this.zdzhdm = zdzhdm;
	}
	
	public String getZdzhdm() {
		return this.zdzhdm ;
	}
	
	public void setZl(String zl) {
		this.zl = zl;
	}
	
	public String getZl() {
		return this.zl;
	}
	
	public void setBdcdys(String bdcdys) {
		this.bdcdys = bdcdys;
	}
	
	public String getBdcdys() {
		return this.bdcdys;
	}

	public void setDjjg(String djjg) {
		this.djjg = djjg;
	}
	
	public String getDjjg() {
		return this.djjg;
	}

}
