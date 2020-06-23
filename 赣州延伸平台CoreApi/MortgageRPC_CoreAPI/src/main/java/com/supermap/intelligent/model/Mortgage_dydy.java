package com.supermap.intelligent.model;

/**
 * 抵押单元实体类
 * @author lx
 * 
 *
 */
public class Mortgage_dydy {
	//不动产单元号
	private String bdcdyh;
	//不动产产权证号/证明号
	private String  bdcqzh;
	//坐落
	private String  ZL;
	//抵押起始时间
	private String  dyqlqssj;
	//抵押结束时间
	private String  dyqljssj;
	//单个单元债券数额
	private String  dgzqse;
	public String getBdcdyh() {
		return bdcdyh;
	}
	public void setBdcdyh(String bdcdyh) {
		this.bdcdyh = bdcdyh;
	}
	public String getBdcqzh() {
		return bdcqzh;
	}
	public void setBdcqzh(String bdcqzh) {
		this.bdcqzh = bdcqzh;
	}
	public String getZL() {
		return ZL;
	}
	public void setZL(String zL) {
		ZL = zL;
	}
	public String getDyqlqssj() {
		return dyqlqssj;
	}
	public void setDyqlqssj(String dyqlqssj) {
		this.dyqlqssj = dyqlqssj;
	}
	public String getDyqljssj() {
		return dyqljssj;
	}
	public void setDyqljssj(String dyqljssj) {
		this.dyqljssj = dyqljssj;
	}
	public String getDgzqse() {
		return dgzqse;
	}
	public void setDgzqse(String dgzqse) {
		this.dgzqse = dgzqse;
	}
	
	
}
