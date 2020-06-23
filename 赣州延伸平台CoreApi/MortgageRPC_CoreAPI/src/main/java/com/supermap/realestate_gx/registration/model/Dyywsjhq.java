package com.supermap.realestate_gx.registration.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/*
 * 登记结果信息表
 */
@Entity
@Table(name = "DYYWSJHQ", schema = "GLSZFGJJ")
public class Dyywsjhq {

	@Id
	@Column(name = "ID", length = 64)
	private String id;
	private String gjjywlsh;
	private String bdcywlsh;
	private String bzxx;
	private String bdcqzh;
	private String djzmh;
	private String bdcdyh;
	private String zl;
	private String fwxz;
	private String fwjg;
	private String szc;
	private String jzmj;
	private String ftjzmj;
	private String dyqk;
	private String cfbs;
	private String cfsj;
	private String dybjsj;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGjjywlsh() {
		return gjjywlsh;
	}
	public void setGjjywlsh(String gjjywlsh) {
		this.gjjywlsh = gjjywlsh;
	}
	public String getBdcywlsh() {
		return bdcywlsh;
	}
	public void setBdcywlsh(String bdcywlsh) {
		this.bdcywlsh = bdcywlsh;
	}
	public String getBzxx() {
		return bzxx;
	}
	public void setBzxx(String bzxx) {
		this.bzxx = bzxx;
	}
	public String getBdcqzh() {
		return bdcqzh;
	}
	public void setBdcqzh(String bdcqzh) {
		this.bdcqzh = bdcqzh;
	}
	public String getDjzmh() {
		return djzmh;
	}
	public void setDjzmh(String djzmh) {
		this.djzmh = djzmh;
	}
	public String getBdcdyh() {
		return bdcdyh;
	}
	public void setBdcdyh(String bdcdyh) {
		this.bdcdyh = bdcdyh;
	}
	public String getZl() {
		return zl;
	}
	public void setZl(String zl) {
		this.zl = zl;
	}
	public String getFwxz() {
		return fwxz;
	}
	public void setFwxz(String fwxz) {
		this.fwxz = fwxz;
	}
	public String getFwjg() {
		return fwjg;
	}
	public void setFwjg(String fwjg) {
		this.fwjg = fwjg;
	}
	public String getSzc() {
		return szc;
	}
	public void setSzc(String szc) {
		this.szc = szc;
	}
	public String getJzmj() {
		return jzmj;
	}
	public void setJzmj(String jzmj) {
		this.jzmj = jzmj;
	}
	public String getFtjzmj() {
		return ftjzmj;
	}
	public void setFtjzmj(String ftjzmj) {
		this.ftjzmj = ftjzmj;
	}
	public String getDyqk() {
		return dyqk;
	}
	public void setDyqk(String dyqk) {
		this.dyqk = dyqk;
	}
	public String getCfbs() {
		return cfbs;
	}
	public void setCfbs(String cfbs) {
		this.cfbs = cfbs;
	}
	public String getCfsj() {
		return cfsj;
	}
	public void setCfsj(String cfsj) {
		this.cfsj = cfsj;
	}
	public String getDybjsj() {
		return dybjsj;
	}
	public void setDybjsj(String dybjsj) {
		this.dybjsj = dybjsj;
	}
	
	
}
