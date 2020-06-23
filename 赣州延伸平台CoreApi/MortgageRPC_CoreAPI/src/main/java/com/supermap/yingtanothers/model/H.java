package com.supermap.yingtanothers.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 作者： 苗利涛
 * 时间： 2016年6月20日 下午5:16:13
 * 功能：鹰潭不动产共享中间库户表
 */
@Entity
@Table(name = "H", schema = "GXFCK")
public class H {

	private String GXXMBH;
	private String BDCDYH;
	private String FWBM;
	private String ZRZH;
	private String LJZH;
	private String CH;
	private String SZC;
	private String QSC;
	private String ZZC;
	private String ZL;
	private String MJDW;
	private String HH;
	private String FWYT1;
	private String FWYT2;
	private String FWYT3;
	private Double ycjzmj;
	private Double yctnjzmj;
	private Double ycftjzmj;
	private Double ycdxbfjzmj;
	private Double ycqtjzmj;
	private Double ycftxs;
	private Double scjzmj;
	private Double sctnjzmj;
	private Double scftjzmj;
	private Double scdxbfjzmj;
	private Double scqtjzmj;
	private Double scftxs;
	private String fwlx;
	private String fwxz;
	private String fcfht;
	private String zt;
	private String cid;
	private String ljzid;
	private String zrzbdcdyid;
	private String fwzt;
	private String relationid;
	private String zcs;
	private Date jgsj;
	private String dyh;
	private String fh;
	private String fwjg;
	private String hid;
	private String ywh;
	
	
	@Column(name = "GXXMBH")
	public String getGXXMBH() {
		return GXXMBH;
	}
	public void setGXXMBH(String gXXMBH) {
		GXXMBH = gXXMBH;
	}
	@Column(name = "BDCDYH")
	public String getBDCDYH() {
		return BDCDYH;
	}
	public void setBDCDYH(String bDCDYH) {
		BDCDYH = bDCDYH;
	}
	@Column(name = "FWBM")
	public String getFWBM() {
		return FWBM;
	}
	public void setFWBM(String fWBM) {
		FWBM = fWBM;
	}
	@Column(name = "ZRZH")
	public String getZRZH() {
		return ZRZH;
	}
	public void setZRZH(String zRZH) {
		ZRZH = zRZH;
	}
	@Column(name = "LJZH")
	public String getLJZH() {
		return LJZH;
	}
	public void setLJZH(String lJZH) {
		LJZH = lJZH;
	}
	@Column(name = "CH")
	public String getCH() {
		return CH;
	}
	public void setCH(String cH) {
		CH = cH;
	}
	@Column(name = "SZC")
	public String getSZC() {
		return SZC;
	}
	public void setSZC(String sZC) {
		SZC = sZC;
	}
	@Column(name = "QSC")
	public String getQSC() {
		return QSC;
	}
	public void setQSC(String qSC) {
		QSC = qSC;
	}
	@Column(name = "ZZC")
	public String getZZC() {
		return ZZC;
	}
	public void setZZC(String zZC) {
		ZZC = zZC;
	}
	@Column(name = "ZL")
	public String getZL() {
		return ZL;
	}
	public void setZL(String zL) {
		ZL = zL;
	}
	@Column(name = "MJDW")
	public String getMJDW() {
		return MJDW;
	}
	public void setMJDW(String mJDW) {
		MJDW = mJDW;
	}
	@Column(name = "HH")
	public String getHH() {
		return HH;
	}
	public void setHH(String hH) {
		HH = hH;
	}
	@Column(name = "FWYT1")
	public String getFWYT1() {
		return FWYT1;
	}
	public void setFWYT1(String fWYT1) {
		FWYT1 = fWYT1;
	}
	@Column(name = "FWYT2")
	public String getFWYT2() {
		return FWYT2;
	}
	public void setFWYT2(String fWYT2) {
		FWYT2 = fWYT2;
	}
	@Column(name = "FWYT3")
	public String getFWYT3() {
		return FWYT3;
	}
	public void setFWYT3(String fWYT3) {
		FWYT3 = fWYT3;
	}
	@Column(name = "ycjzmj")
	public Double getYcjzmj() {
		return ycjzmj;
	}
	public void setYcjzmj(Double ycjzmj) {
		this.ycjzmj = ycjzmj;
	}
	@Column(name = "yctnjzmj")
	public Double getYctnjzmj() {
		return yctnjzmj;
	}
	public void setYctnjzmj(Double yctnjzmj) {
		this.yctnjzmj = yctnjzmj;
	}
	@Column(name = "ycftjzmj")
	public Double getYcftjzmj() {
		return ycftjzmj;
	}
	public void setYcftjzmj(Double ycftjzmj) {
		this.ycftjzmj = ycftjzmj;
	}
	@Column(name = "ycdxbfjzmj")
	public Double getYcdxbfjzmj() {
		return ycdxbfjzmj;
	}
	public void setYcdxbfjzmj(Double ycdxbfjzmj) {
		this.ycdxbfjzmj = ycdxbfjzmj;
	}
	@Column(name = "ycqtjzmj")
	public Double getYcqtjzmj() {
		return ycqtjzmj;
	}
	public void setYcqtjzmj(Double ycqtjzmj) {
		this.ycqtjzmj = ycqtjzmj;
	}
	@Column(name = "ycftxs")
	public Double getYcftxs() {
		return ycftxs;
	}
	public void setYcftxs(Double ycftxs) {
		this.ycftxs = ycftxs;
	}
	@Column(name = "scjzmj")
	public Double getScjzmj() {
		return scjzmj;
	}
	public void setScjzmj(Double scjzmj) {
		this.scjzmj = scjzmj;
	}
	@Column(name = "sctnjzmj")
	public Double getSctnjzmj() {
		return sctnjzmj;
	}
	public void setSctnjzmj(Double sctnjzmj) {
		this.sctnjzmj = sctnjzmj;
	}
	@Column(name = "scftjzmj")
	public Double getScftjzmj() {
		return scftjzmj;
	}
	public void setScftjzmj(Double scftjzmj) {
		this.scftjzmj = scftjzmj;
	}
	@Column(name = "scdxbfjzmj")
	public Double getScdxbfjzmj() {
		return scdxbfjzmj;
	}
	public void setScdxbfjzmj(Double scdxbfjzmj) {
		this.scdxbfjzmj = scdxbfjzmj;
	}
	@Column(name = "scqtjzmj")
	public Double getScqtjzmj() {
		return scqtjzmj;
	}
	public void setScqtjzmj(Double scqtjzmj) {
		this.scqtjzmj = scqtjzmj;
	}
	@Column(name = "scftxs")
	public Double getScftxs() {
		return scftxs;
	}
	public void setScftxs(Double scftxs) {
		this.scftxs = scftxs;
	}
	@Column(name = "fwlx")
	public String getFwlx() {
		return fwlx;
	}
	public void setFwlx(String fwlx) {
		this.fwlx = fwlx;
	}
	@Column(name = "fwxz")
	public String getFwxz() {
		return fwxz;
	}
	public void setFwxz(String fwxz) {
		this.fwxz = fwxz;
	}
	@Column(name = "fcfht")
	public String getFcfht() {
		return fcfht;
	}
	public void setFcfht(String fcfht) {
		this.fcfht = fcfht;
	}
	@Column(name = "zt")
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
	@Column(name = "cid")
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	@Column(name = "ljzid")
	public String getLjzid() {
		return ljzid;
	}
	public void setLjzid(String ljzid) {
		this.ljzid = ljzid;
	}
	@Column(name = "zrzbdcdyid")
	public String getZrzbdcdyid() {
		return zrzbdcdyid;
	}
	public void setZrzbdcdyid(String zrzbdcdyid) {
		this.zrzbdcdyid = zrzbdcdyid;
	}
	@Column(name = "fwzt")
	public String getFwzt() {
		return fwzt;
	}
	public void setFwzt(String fwzt) {
		this.fwzt = fwzt;
	}
	@Column(name = "relationid")
	public String getRelationid() {
		return relationid;
	}
	public void setRelationid(String relationid) {
		this.relationid = relationid;
	}
	@Column(name = "zcs")
	public String getZcs() {
		return zcs;
	}
	public void setZcs(String zcs) {
		this.zcs = zcs;
	}
	@Column(name = "jgsj")
	public Date getJgsj() {
		return jgsj;
	}
	public void setJgsj(Date jgsj) {
		this.jgsj = jgsj;
	}
	@Column(name = "dyh")
	public String getDyh() {
		return dyh;
	}
	public void setDyh(String dyh) {
		this.dyh = dyh;
	}
	@Column(name = "fh")
	public String getFh() {
		return fh;
	}
	public void setFh(String fh) {
		this.fh = fh;
	}
	@Column(name = "fwjg")
	public String getFwjg() {
		return fwjg;
	}
	public void setFwjg(String fwjg) {
		this.fwjg = fwjg;
	}
	@Id
	@Column(name = "hid")
	public String getHid() {
		return hid;
	}
	public void setHid(String hid) {
		this.hid = hid;
	}
	@Column(name = "ywh")
	public String getYwh() {
		return ywh;
	}
	public void setYwh(String ywh) {
		this.ywh = ywh;
	}
	
	
	
}
