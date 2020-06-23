package com.supermap.wisdombusiness.framework.model.gxdjk;

// Generated 2016-3-28 10:42:41 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Qlr generated by hbm2java
 */
@Entity
@Table(name = "BGQQLR", schema = "GXDJK")
public class Bgqqlr_dj implements java.io.Serializable {

    private String bsm;
    private String qlrid;
    private String qlid;
    private BigDecimal sxh;
    private String qlrmc;
    private String bdcqzh;
    private String zjzl;
    private String zjh;
    private String fzjg;
    private String sshy;
    private String gj;
    private String hjszss;
    private String xb;
    private String dh;
    private String dz;
    private String yb;
    private String gzdw;
    private String dzyj;
    private String qlrlx;
    private String qlbl;
    private String gyfs;
    private String gyqk;
    private String bz;
    private String isczr;
    private String ysdm;
    private String bdcdyh;
    private String qzysxlh;
    private String gxlx;
    private String gxxmbh;
    private Date tssj;
    
    public Bgqqlr_dj() {
    }

    public Bgqqlr_dj(String bsm) {
	this.bsm = bsm;
    }

    public Bgqqlr_dj(String bsm, String qlrid, String qlid, BigDecimal sxh,
	    String qlrmc, String bdcqzh, String zjzl, String zjh, String fzjg,
	    String sshy, String gj, String hjszss, String xb, String dh,
	    String dz, String yb, String gzdw, String dzyj, String qlrlx,
	    String qlbl, String gyfs, String gyqk, String bz, String isczr,
	    String ysdm, String bdcdyh, String qzysxlh, String gxlx,
	    String gxxmbh,Date tssj) {
	this.bsm = bsm;
	this.qlrid = qlrid;
	this.qlid = qlid;
	this.sxh = sxh;
	this.qlrmc = qlrmc;
	this.bdcqzh = bdcqzh;
	this.zjzl = zjzl;
	this.zjh = zjh;
	this.fzjg = fzjg;
	this.sshy = sshy;
	this.gj = gj;
	this.hjszss = hjszss;
	this.xb = xb;
	this.dh = dh;
	this.dz = dz;
	this.yb = yb;
	this.gzdw = gzdw;
	this.dzyj = dzyj;
	this.qlrlx = qlrlx;
	this.qlbl = qlbl;
	this.gyfs = gyfs;
	this.gyqk = gyqk;
	this.bz = bz;
	this.isczr = isczr;
	this.ysdm = ysdm;
	this.bdcdyh = bdcdyh;
	this.qzysxlh = qzysxlh;
	this.gxlx = gxlx;
	this.gxxmbh = gxxmbh;
	this.tssj = tssj;
    }

    @Id
    @Column(name = "BSM", unique = true, nullable = false, length = 200)
    public String getBsm() {
	if (bsm == null) bsm = UUID.randomUUID().toString().replace("-", ""); 
	return this.bsm;
    }

    public void setBsm(String bsm) {
	this.bsm = bsm;
    }

    @Column(name = "QLRID", length = 100)
    public String getQlrid() {
	return this.qlrid;
    }

    public void setQlrid(String qlrid) {
	this.qlrid = qlrid;
    }

    @Column(name = "QLID", length = 100)
    public String getQlid() {
	return this.qlid;
    }

    public void setQlid(String qlid) {
	this.qlid = qlid;
    }

    @Column(name = "SXH", precision = 22, scale = 0)
    public BigDecimal getSxh() {
	return this.sxh;
    }

    public void setSxh(BigDecimal sxh) {
	this.sxh = sxh;
    }

    @Column(name = "QLRMC", length = 400)
    public String getQlrmc() {
	return this.qlrmc;
    }

    public void setQlrmc(String qlrmc) {
	this.qlrmc = qlrmc;
    }

    @Column(name = "BDCQZH", length = 400)
    public String getBdcqzh() {
	return this.bdcqzh;
    }

    public void setBdcqzh(String bdcqzh) {
	this.bdcqzh = bdcqzh;
    }

    @Column(name = "ZJZL", length = 20)
    public String getZjzl() {
	return this.zjzl;
    }

    public void setZjzl(String zjzl) {
	this.zjzl = zjzl;
    }

    @Column(name = "ZJH", length = 100)
    public String getZjh() {
	return this.zjh;
    }

    public void setZjh(String zjh) {
	this.zjh = zjh;
    }

    @Column(name = "FZJG", length = 400)
    public String getFzjg() {
	return this.fzjg;
    }

    public void setFzjg(String fzjg) {
	this.fzjg = fzjg;
    }

    @Column(name = "SSHY", length = 20)
    public String getSshy() {
	return this.sshy;
    }

    public void setSshy(String sshy) {
	this.sshy = sshy;
    }

    @Column(name = "GJ", length = 20)
    public String getGj() {
	return this.gj;
    }

    public void setGj(String gj) {
	this.gj = gj;
    }

    @Column(name = "HJSZSS", length = 20)
    public String getHjszss() {
	return this.hjszss;
    }

    public void setHjszss(String hjszss) {
	this.hjszss = hjszss;
    }

    @Column(name = "XB", length = 20)
    public String getXb() {
	return this.xb;
    }

    public void setXb(String xb) {
	this.xb = xb;
    }

    @Column(name = "DH", length = 100)
    public String getDh() {
	return this.dh;
    }

    public void setDh(String dh) {
	this.dh = dh;
    }

    @Column(name = "DZ", length = 400)
    public String getDz() {
	return this.dz;
    }

    public void setDz(String dz) {
	this.dz = dz;
    }

    @Column(name = "YB", length = 20)
    public String getYb() {
	return this.yb;
    }

    public void setYb(String yb) {
	this.yb = yb;
    }

    @Column(name = "GZDW", length = 200)
    public String getGzdw() {
	return this.gzdw;
    }

    public void setGzdw(String gzdw) {
	this.gzdw = gzdw;
    }

    @Column(name = "DZYJ", length = 100)
    public String getDzyj() {
	return this.dzyj;
    }

    public void setDzyj(String dzyj) {
	this.dzyj = dzyj;
    }

    @Column(name = "QLRLX", length = 20)
    public String getQlrlx() {
	return this.qlrlx;
    }

    public void setQlrlx(String qlrlx) {
	this.qlrlx = qlrlx;
    }

    @Column(name = "QLBL", length = 200)
    public String getQlbl() {
	return this.qlbl;
    }

    public void setQlbl(String qlbl) {
	this.qlbl = qlbl;
    }

    @Column(name = "GYFS", length = 20)
    public String getGyfs() {
	return this.gyfs;
    }

    public void setGyfs(String gyfs) {
	this.gyfs = gyfs;
    }

    @Column(name = "GYQK", length = 100)
    public String getGyqk() {
	return this.gyqk;
    }

    public void setGyqk(String gyqk) {
	this.gyqk = gyqk;
    }

    @Column(name = "BZ", length = 1000)
    public String getBz() {
	return this.bz;
    }

    public void setBz(String bz) {
	this.bz = bz;
    }

    @Column(name = "ISCZR", length = 20)
    public String getIsczr() {
	return this.isczr;
    }

    public void setIsczr(String isczr) {
	this.isczr = isczr;
    }

    @Column(name = "YSDM", length = 100)
    public String getYsdm() {
	return this.ysdm;
    }

    public void setYsdm(String ysdm) {
	this.ysdm = ysdm;
    }

    @Column(name = "BDCDYH", length = 100)
    public String getBdcdyh() {
	return this.bdcdyh;
    }

    public void setBdcdyh(String bdcdyh) {
	this.bdcdyh = bdcdyh;
    }

    @Column(name = "QZYSXLH", length = 100)
    public String getQzysxlh() {
	return this.qzysxlh;
    }

    public void setQzysxlh(String qzysxlh) {
	this.qzysxlh = qzysxlh;
    }

    @Column(name = "GXLX", length = 100)
    public String getGxlx() {
	return this.gxlx;
    }

    public void setGxlx(String gxlx) {
	this.gxlx = gxlx;
    }

    @Column(name = "GXXMBH", length = 100)
    public String getGxxmbh() {
	return this.gxxmbh;
    }

    public void setGxxmbh(String gxxmbh) {
	this.gxxmbh = gxxmbh;
    }

    @Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "TSSJ", length = 7)
	public Date getTssj() {
		return this.tssj;
	}

	public void setTssj(Date tssj) {
		this.tssj = tssj;
	}
}
