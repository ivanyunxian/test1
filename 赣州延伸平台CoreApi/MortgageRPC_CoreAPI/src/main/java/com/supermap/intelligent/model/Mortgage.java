package com.supermap.intelligent.model;

import java.util.List;
import java.util.Map;

/**
 *抵押接口实体类
 * @author lx
 *
 */
public class Mortgage {
	//请求类型
	private String 	requestcode;
	 //请求时间
	private String  requestseq;
	 // 外网业务流水号
	private String  ywlsh; 
	//行政区划
	private String  xzqdm;    
	//抵押合同号
	private String  dyhth;
	//抵押证明
	private String  dyzmh;
	//申报人员名称
	private String  sbrymc;
	//申报人员证件号
	private String  sbryzjh;
	//申报人员电话
	private String  sbrylxdh;
	//申报机构名称
	private String  sbjgmc;
	//申报机构证件号
	private String  sbjgzjh;
	//抵押方式
	private  String   dyfs;
	//抵押原因
	private  String   djyy;
	//债权数额
	private  String   zqse	;
	//是否合并证书
	private  String   sfhbzs	;
	//持证方式
	private  String   czfs	;
	//申请人集合(权利人集合)
	public List<Mortgage_qlr> qlrlist;
	//抵押权人集合
	public List<Mortgage_dyqr> dyqrlist;
	//抵押单元集合
	public List<Mortgage_dydy>  dydylist;
	//附件集合
	public List< MaterModel> matermodel;
	
	
	public List<Mortgage_qlr> getQlrlist() {
		return qlrlist;
	}
	public void setQlrlist(List<Mortgage_qlr> qlrlist) {
		this.qlrlist = qlrlist;
	}
	public List<Mortgage_dyqr> getDyqrlist() {
		return dyqrlist;
	}
	public void setDyqrlist(List<Mortgage_dyqr> dyqrlist) {
		this.dyqrlist = dyqrlist;
	}
	public List<Mortgage_dydy> getDydylist() {
		return dydylist;
	}
	public void setDydylist(List<Mortgage_dydy> dydylist) {
		this.dydylist = dydylist;
	}
	public List<MaterModel> getMatermodel() {
		return matermodel;
	}
	public void setMatermodel(List<MaterModel> matermodel) {
		this.matermodel = matermodel;
	}
	public String getDyfs() {
		return dyfs;
	}
	public void setDyfs(String dyfs) {
		this.dyfs = dyfs;
	}
	public String getDjyy() {
		return djyy;
	}
	public void setDjyy(String djyy) {
		this.djyy = djyy;
	}
	public String getZqse() {
		return zqse;
	}
	public void setZqse(String zqse) {
		this.zqse = zqse;
	}
	public String getSfhbzs() {
		return sfhbzs;
	}
	public void setSfhbzs(String sfhbzs) {
		this.sfhbzs = sfhbzs;
	}
	public String getCzfs() {
		return czfs;
	}
	public void setCzfs(String czfs) {
		this.czfs = czfs;
	}
	//注销原因
	public String getRequestcode() {
		return requestcode;
	}
	public void setRequestcode(String requestcode) {
		this.requestcode = requestcode;
	}
	public String getRequestseq() {
		return requestseq;
	}
	public void setRequestseq(String requestseq) {
		this.requestseq = requestseq;
	}
	public String getYwlsh() {
		return ywlsh;
	}
	public void setYwlsh(String ywlsh) {
		this.ywlsh = ywlsh;
	}
	public String getXzqdm() {
		return xzqdm;
	}
	public void setXzqdm(String xzqdm) {
		this.xzqdm = xzqdm;
	}
	public String getDyhth() {
		return dyhth;
	}
	public void setDyhth(String dyhth) {
		this.dyhth = dyhth;
	}
	public String getDyzmh() {
		return dyzmh;
	}
	public void setDyzmh(String dyzmh) {
		this.dyzmh = dyzmh;
	}
	public String getSbrymc() {
		return sbrymc;
	}
	public void setSbrymc(String sbrymc) {
		this.sbrymc = sbrymc;
	}
	public String getSbryzjh() {
		return sbryzjh;
	}
	public void setSbryzjh(String sbryzjh) {
		this.sbryzjh = sbryzjh;
	}
	public String getSbrylxdh() {
		return sbrylxdh;
	}
	public void setSbrylxdh(String sbrylxdh) {
		this.sbrylxdh = sbrylxdh;
	}
	public String getSbjgmc() {
		return sbjgmc;
	}
	public void setSbjgmc(String sbjgmc) {
		this.sbjgmc = sbjgmc;
	}
	public String getSbjgzjh() {
		return sbjgzjh;
	}
	public void setSbjgzjh(String sbjgzjh) {
		this.sbjgzjh = sbjgzjh;
	}
		
}
