package com.supermap.yingtanothers.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 
 * 作者： 苗利涛
 * 时间： 2016年6月20日 下午4:02:07
 * 功能：鹰潭不动产共享中间库预告登记属性结构表
 */
@Entity
@Table(name = "YGDJ", schema = "GXFCK")
public class YGDJ {

	  private String ysdm;
	  private String bdcdyh;
	  private String ywh;
	  private String bdczl;
	  private String ywr;
	  private String ywrzjzl;
	  private String ywrzjh;
	  private String ygdjzl;
	  private String djlx;
	  private String djyy;
	  private String tdsyqr;
	  private String ghyt;
	  private String fwxz;
	  private String fwjg;
	  private String szc;
	  private int zcs;
	  private Double jzmj;
	  private Double qdjg;
	  private String bdcdjzmh;
	  private String qxdm;
	  private String djjg;
	  private String dbr;
	  private Date djsj;
	  private String fj;
	  private String qszt;
	  private String qlid;
	  private String relationid;
	  private String gxxmbh;
	  private String ygdjid;
	  
	@Column(name = "YSDM")  
	public String getYsdm() {
		return ysdm;
	}
	public void setYsdm(String ysdm) {
		this.ysdm = ysdm;
	}
	@Column(name = "BDCDYH")
	public String getBdcdyh() {
		return bdcdyh;
	}
	public void setBdcdyh(String bdcdyh) {
		this.bdcdyh = bdcdyh;
	}
	@Column(name = "YWH")
	public String getYwh() {
		return ywh;
	}
	public void setYwh(String ywh) {
		this.ywh = ywh;
	}
	@Column(name = "BDCZL")
	public String getBdczl() {
		return bdczl;
	}
	public void setBdczl(String bdczl) {
		this.bdczl = bdczl;
	}
	@Column(name = "YWR")
	public String getYwr() {
		return ywr;
	}
	public void setYwr(String ywr) {
		this.ywr = ywr;
	}
	@Column(name = "YWRZJZL")
	public String getYwrzjzl() {
		return ywrzjzl;
	}
	public void setYwrzjzl(String ywrzjzl) {
		this.ywrzjzl = ywrzjzl;
	}
	@Column(name = "YWRZJH")
	public String getYwrzjh() {
		return ywrzjh;
	}
	public void setYwrzjh(String ywrzjh) {
		this.ywrzjh = ywrzjh;
	}
	@Column(name = "YGDJZL")
	public String getYgdjzl() {
		return ygdjzl;
	}
	public void setYgdjzl(String ygdjzl) {
		this.ygdjzl = ygdjzl;
	}
	@Column(name = "DJLX")
	public String getDjlx() {
		return djlx;
	}
	public void setDjlx(String djlx) {
		this.djlx = djlx;
	}
	@Column(name = "DJYY")
	public String getDjyy() {
		return djyy;
	}
	public void setDjyy(String djyy) {
		this.djyy = djyy;
	}
	@Column(name = "TDSYQR")
	public String getTdsyqr() {
		return tdsyqr;
	}
	public void setTdsyqr(String tdsyqr) {
		this.tdsyqr = tdsyqr;
	}
	@Column(name = "GHYT")
	public String getGhyt() {
		return ghyt;
	}
	public void setGhyt(String ghyt) {
		this.ghyt = ghyt;
	}
	@Column(name = "FWXZ")
	public String getFwxz() {
		return fwxz;
	}
	public void setFwxz(String fwxz) {
		this.fwxz = fwxz;
	}
	@Column(name = "FWJG")
	public String getFwjg() {
		return fwjg;
	}
	public void setFwjg(String fwjg) {
		this.fwjg = fwjg;
	}
	@Column(name = "SZC")
	public String getSzc() {
		return szc;
	}
	public void setSzc(String szc) {
		this.szc = szc;
	}
	@Column(name = "ZCS")
	public int getZcs() {
		return zcs;
	}
	public void setZcs(int zcs) {
		this.zcs = zcs;
	}
	@Column(name = "JZMJ")
	public Double getJzmj() {
		return jzmj;
	}
	public void setJzmj(Double jzmj) {
		this.jzmj = jzmj;
	}
	@Column(name = "QDJG")
	public Double getQdjg() {
		return qdjg;
	}
	public void setQdjg(Double qdjg) {
		this.qdjg = qdjg;
	}
	@Column(name = "BDCDJZMH")
	public String getBdcdjzmh() {
		return bdcdjzmh;
	}
	public void setBdcdjzmh(String bdcdjzmh) {
		this.bdcdjzmh = bdcdjzmh;
	}
	@Column(name = "QXDM")
	public String getQxdm() {
		return qxdm;
	}
	public void setQxdm(String qxdm) {
		this.qxdm = qxdm;
	}
	@Column(name = "DJJG")
	public String getDjjg() {
		return djjg;
	}
	public void setDjjg(String djjg) {
		this.djjg = djjg;
	}
	@Column(name = "DBR")
	public String getDbr() {
		return dbr;
	}
	public void setDbr(String dbr) {
		this.dbr = dbr;
	}
	@Column(name = "DJSJ")
	public Date getDjsj() {
		return djsj;
	}
	public void setDjsj(Date djsj) {
		this.djsj = djsj;
	}
	@Column(name = "FJ")
	public String getFj() {
		return fj;
	}
	public void setFj(String fj) {
		this.fj = fj;
	}
	@Column(name = "QSZT")
	public String getQszt() {
		return qszt;
	}
	public void setQszt(String qszt) {
		this.qszt = qszt;
	}
	@Column(name = "QLID")
	public String getQlid() {
		return qlid;
	}
	public void setQlid(String qlid) {
		this.qlid = qlid;
	}
	@Column(name = "RELATIONID")
	public String getRelationid() {
		return relationid;
	}
	public void setRelationid(String relationid) {
		this.relationid = relationid;
	}
	@Column(name = "GXXMBH")
	public String getGxxmbh() {
		return gxxmbh;
	}
	public void setGxxmbh(String gxxmbh) {
		this.gxxmbh = gxxmbh;
	}
	@Id
	@Column(name = "YGDJID")
	public String getYgdjid() {
		return ygdjid;
	}
	public void setYgdjid(String ygdjid) {
		this.ygdjid = ygdjid;
	}
	  	  
}
