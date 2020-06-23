package com.supermap.realestate_gx.registration.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/*
 * 抵押业务数据推送表
 */
@Entity
@Table(name = "DYYWTSB", schema = "GLSZFGJJ")
public class Dyywtsb {
	@Id
	@Column(name = "ID", length = 64)
	private String id;
	private String gjjywlsh;
	private String bdcywlsh;
	private String ywzt;
	private String bljg;
	private String bzxx;
	private Date fhsj;
	private String bdcqzh;
	private String bdcdyh;
	private Date dkkssj;
	private Date dkzzsj;
	private String dkje;
	private String dyfs;
	private String qllx;
	private String dkhtbh;
	private String qlrmc;
	private String zjzl;
	private String zjh;
	private String sqrlx;
	private String dh;
	private String dz;
	private String fddbr;
	private String fddbrdh;
	private String fddbrzjhm;
	private String dlrxm;
	private String dlrzjhm;
	private String dlrlxdh;
	private Date dysqsj;
	private String ywlx;
	private String slzt;
	
	
	
	public String getSlzt() {
		return slzt;
	}
	public void setSlzt(String slzt) {
		this.slzt = slzt;
	}
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
	public String getYwzt() {
		return ywzt;
	}
	public void setYwzt(String ywzt) {
		this.ywzt = ywzt;
	}
	public String getBljg() {
		return bljg;
	}
	public void setBljg(String bljg) {
		this.bljg = bljg;
	}
	public String getBzxx() {
		return bzxx;
	}
	public void setBzxx(String bzxx) {
		this.bzxx = bzxx;
	}
	public Date getFhsj() {
		return fhsj;
	}
	public void setFhsj(Date fhsj) {
		this.fhsj = fhsj;
	}
	public String getBdcqzh() {
		return bdcqzh;
	}
	public void setBdcqzh(String bdcqzh) {
		this.bdcqzh = bdcqzh;
	}
	public String getBdcdyh() {
		return bdcdyh;
	}
	public void setBdcdyh(String bdcdyh) {
		this.bdcdyh = bdcdyh;
	}
	public Date getDkkssj() {
		return dkkssj;
	}
	public void setDkkssj(Date dkkssj) {
		this.dkkssj = dkkssj;
	}
	public Date getDkzzsj() {
		return dkzzsj;
	}
	public void setDkzzsj(Date dkzzsj) {
		this.dkzzsj = dkzzsj;
	}
	public String getDkje() {
		return dkje;
	}
	public void setDkje(String dkje) {
		this.dkje = dkje;
	}
	public String getDyfs() {
		return dyfs;
	}
	public void setDyfs(String dyfs) {
		this.dyfs = dyfs;
	}
	public String getQllx() {
		return qllx;
	}
	public void setQllx(String qllx) {
		this.qllx = qllx;
	}
	public String getDkhtbh() {
		return dkhtbh;
	}
	public void setDkhtbh(String dkhtbh) {
		this.dkhtbh = dkhtbh;
	}
	public String getQlrmc() {
		return qlrmc;
	}
	public void setQlrmc(String qlrmc) {
		this.qlrmc = qlrmc;
	}
	public String getZjzl() {
		return zjzl;
	}
	public void setZjzl(String zjzl) {
		this.zjzl = zjzl;
	}
	public String getZjh() {
		return zjh;
	}
	public void setZjh(String zjh) {
		this.zjh = zjh;
	}
	public String getSqrlx() {
		return sqrlx;
	}
	public void setSqrlx(String sqrlx) {
		this.sqrlx = sqrlx;
	}
	public String getDh() {
		return dh;
	}
	public void setDh(String dh) {
		this.dh = dh;
	}
	public String getDz() {
		return dz;
	}
	public void setDz(String dz) {
		this.dz = dz;
	}
	public String getFddbr() {
		return fddbr;
	}
	public void setFddbr(String fddbr) {
		this.fddbr = fddbr;
	}
	public String getFddbrdh() {
		return fddbrdh;
	}
	public void setFddbrdh(String fddbrdh) {
		this.fddbrdh = fddbrdh;
	}
	public String getFddbrzjhm() {
		return fddbrzjhm;
	}
	public void setFddbrzjhm(String fddbrzjhm) {
		this.fddbrzjhm = fddbrzjhm;
	}
	public String getDlrxm() {
		return dlrxm;
	}
	public void setDlrxm(String dlrxm) {
		this.dlrxm = dlrxm;
	}
	public String getDlrzjhm() {
		return dlrzjhm;
	}
	public void setDlrzjhm(String dlrzjhm) {
		this.dlrzjhm = dlrzjhm;
	}
	public String getDlrlxdh() {
		return dlrlxdh;
	}
	public void setDlrlxdh(String dlrlxdh) {
		this.dlrlxdh = dlrlxdh;
	}
	public Date getDysqsj() {
		return dysqsj;
	}
	public void setDysqsj(Date dysqsj) {
		this.dysqsj = dysqsj;
	}
	public String getYwlx() {
		return ywlx;
	}
	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}
	
}
