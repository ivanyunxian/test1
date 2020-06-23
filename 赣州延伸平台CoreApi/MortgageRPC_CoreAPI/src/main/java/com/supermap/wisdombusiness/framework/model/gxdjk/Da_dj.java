package com.supermap.wisdombusiness.framework.model.gxdjk;

// Generated 2016-3-28 10:42:41 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Da generated by hbm2java
 */
@Entity
@Table(name = "DA", schema = "GXDJK")
public class Da_dj implements java.io.Serializable {

    private String bsm;
    private String gxxmbh;
    private String flmc;
    private String fjmc;
    private String wjmc;
    private BigDecimal sxh;
    private Date scsj;
    private String dabh;
    private Blob fjnr;
    private String fjlx;

    public Da_dj() {
    }

    public Da_dj(String bsm) {
	if (bsm == null) bsm = UUID.randomUUID().toString().replace("-", ""); 
	this.bsm = bsm;
    }

    public Da_dj(String bsm, String gxxmbh, String flmc, String fjmc, String wjmc,
	    BigDecimal sxh, Date scsj, String dabh, Blob fjnr, String fjlx) {
	this.bsm = bsm;
	this.gxxmbh = gxxmbh;
	this.flmc = flmc;
	this.fjmc = fjmc;
	this.wjmc = wjmc;
	this.sxh = sxh;
	this.scsj = scsj;
	this.dabh = dabh;
	this.fjnr = fjnr;
	this.fjlx = fjlx;
    }

    @Id
    @Column(name = "BSM", unique = true, nullable = false, length = 200)
    public String getBsm() {
	return this.bsm;
    }

    public void setBsm(String bsm) {
	this.bsm = bsm;
    }

    @Column(name = "GXXMBH", length = 200)
    public String getGxxmbh() {
	return this.gxxmbh;
    }

    public void setGxxmbh(String gxxmbh) {
	this.gxxmbh = gxxmbh;
    }

    @Column(name = "FLMC", length = 200)
    public String getFlmc() {
	return this.flmc;
    }

    public void setFlmc(String flmc) {
	this.flmc = flmc;
    }

    @Column(name = "FJMC", length = 400)
    public String getFjmc() {
	return this.fjmc;
    }

    public void setFjmc(String fjmc) {
	this.fjmc = fjmc;
    }

    @Column(name = "WJMC", length = 400)
    public String getWjmc() {
	return this.wjmc;
    }

    public void setWjmc(String wjmc) {
	this.wjmc = wjmc;
    }

    @Column(name = "SXH", precision = 22, scale = 0)
    public BigDecimal getSxh() {
	return this.sxh;
    }

    public void setSxh(BigDecimal sxh) {
	this.sxh = sxh;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "SCSJ", length = 7)
    public Date getScsj() {
	return this.scsj;
    }

    public void setScsj(Date scsj) {
	this.scsj = scsj;
    }

    @Column(name = "DABH", length = 200)
    public String getDabh() {
	return this.dabh;
    }

    public void setDabh(String dabh) {
	this.dabh = dabh;
    }

    @Column(name = "FJNR")
    public Blob getFjnr() {
	return this.fjnr;
    }

    public void setFjnr(Blob fjnr) {
	this.fjnr = fjnr;
    }

    @Column(name = "FJLX", length = 40)
    public String getFjlx() {
	return this.fjlx;
    }

    public void setFjlx(String fjlx) {
	this.fjlx = fjlx;
    }

}
