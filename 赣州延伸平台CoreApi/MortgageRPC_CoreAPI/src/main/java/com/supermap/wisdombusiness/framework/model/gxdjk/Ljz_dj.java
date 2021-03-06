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
import javax.persistence.UniqueConstraint;

/**
 * Ljz generated by hbm2java
 */
@Entity
@Table(name = "LJZ", schema = "GXDJK", uniqueConstraints = @UniqueConstraint(columnNames = "LJZID"))
public class Ljz_dj implements java.io.Serializable {

    private String bsm;
    private String ljzid;
    private String ysdm;
    private String ljzh;
    private String zrzbdcdyid;
    private String zrzh;
    private String mph;
    private BigDecimal ycjzmj;
    private BigDecimal ycdxmj;
    private BigDecimal ycqtmj;
    private BigDecimal scjzmj;
    private BigDecimal scdxmj;
    private BigDecimal scqtmj;
    private String fwjg1;
    private String fwjg2;
    private String fwjg3;
    private String jzwzt;
    private String fwyt1;
    private String fwyt2;
    private String fwyt3;
    private BigDecimal zcs;
    private BigDecimal dscs;
    private BigDecimal dxcs;
    private String bz;
    private Date jgrq;
    private String gxxmbh;
    private String relationid;

    public Ljz_dj() {
    }

    public Ljz_dj(String bsm, String ljzid) {
	this.bsm = bsm;
	this.ljzid = ljzid;
    }

    public Ljz_dj(String bsm, String ljzid, String ysdm, String ljzh,
	    String zrzbdcdyid, String zrzh, String mph, BigDecimal ycjzmj,
	    BigDecimal ycdxmj, BigDecimal ycqtmj, BigDecimal scjzmj,
	    BigDecimal scdxmj, BigDecimal scqtmj, String fwjg1, String fwjg2,
	    String fwjg3, String jzwzt, String fwyt1, String fwyt2,
	    String fwyt3, BigDecimal zcs, BigDecimal dscs, BigDecimal dxcs,
	    String bz, Date jgrq, String gxxmbh, String relationid) {
	this.bsm = bsm;
	this.ljzid = ljzid;
	this.ysdm = ysdm;
	this.ljzh = ljzh;
	this.zrzbdcdyid = zrzbdcdyid;
	this.zrzh = zrzh;
	this.mph = mph;
	this.ycjzmj = ycjzmj;
	this.ycdxmj = ycdxmj;
	this.ycqtmj = ycqtmj;
	this.scjzmj = scjzmj;
	this.scdxmj = scdxmj;
	this.scqtmj = scqtmj;
	this.fwjg1 = fwjg1;
	this.fwjg2 = fwjg2;
	this.fwjg3 = fwjg3;
	this.jzwzt = jzwzt;
	this.fwyt1 = fwyt1;
	this.fwyt2 = fwyt2;
	this.fwyt3 = fwyt3;
	this.zcs = zcs;
	this.dscs = dscs;
	this.dxcs = dxcs;
	this.bz = bz;
	this.jgrq = jgrq;
	this.gxxmbh = gxxmbh;
	this.relationid = relationid;
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

    @Column(name = "LJZID", unique = true, nullable = false, length = 100)
    public String getLjzid() {
	return this.ljzid;
    }

    public void setLjzid(String ljzid) {
	this.ljzid = ljzid;
    }

    @Column(name = "YSDM", length = 20)
    public String getYsdm() {
	return this.ysdm;
    }

    public void setYsdm(String ysdm) {
	this.ysdm = ysdm;
    }

    @Column(name = "LJZH", length = 40)
    public String getLjzh() {
	return this.ljzh;
    }

    public void setLjzh(String ljzh) {
	this.ljzh = ljzh;
    }

    @Column(name = "ZRZBDCDYID", length = 100)
    public String getZrzbdcdyid() {
	return this.zrzbdcdyid;
    }

    public void setZrzbdcdyid(String zrzbdcdyid) {
	this.zrzbdcdyid = zrzbdcdyid;
    }

    @Column(name = "ZRZH", length = 40)
    public String getZrzh() {
	return this.zrzh;
    }

    public void setZrzh(String zrzh) {
	this.zrzh = zrzh;
    }

    @Column(name = "MPH", length = 100)
    public String getMph() {
	return this.mph;
    }

    public void setMph(String mph) {
	this.mph = mph;
    }

    @Column(name = "YCJZMJ", precision = 38, scale = 16)
    public BigDecimal getYcjzmj() {
	return this.ycjzmj;
    }

    public void setYcjzmj(BigDecimal ycjzmj) {
	this.ycjzmj = ycjzmj;
    }

    @Column(name = "YCDXMJ", precision = 38, scale = 16)
    public BigDecimal getYcdxmj() {
	return this.ycdxmj;
    }

    public void setYcdxmj(BigDecimal ycdxmj) {
	this.ycdxmj = ycdxmj;
    }

    @Column(name = "YCQTMJ", precision = 38, scale = 16)
    public BigDecimal getYcqtmj() {
	return this.ycqtmj;
    }

    public void setYcqtmj(BigDecimal ycqtmj) {
	this.ycqtmj = ycqtmj;
    }

    @Column(name = "SCJZMJ", precision = 38, scale = 16)
    public BigDecimal getScjzmj() {
	return this.scjzmj;
    }

    public void setScjzmj(BigDecimal scjzmj) {
	this.scjzmj = scjzmj;
    }

    @Column(name = "SCDXMJ", precision = 38, scale = 16)
    public BigDecimal getScdxmj() {
	return this.scdxmj;
    }

    public void setScdxmj(BigDecimal scdxmj) {
	this.scdxmj = scdxmj;
    }

    @Column(name = "SCQTMJ", precision = 38, scale = 16)
    public BigDecimal getScqtmj() {
	return this.scqtmj;
    }

    public void setScqtmj(BigDecimal scqtmj) {
	this.scqtmj = scqtmj;
    }

    @Column(name = "FWJG1", length = 20)
    public String getFwjg1() {
	return this.fwjg1;
    }

    public void setFwjg1(String fwjg1) {
	this.fwjg1 = fwjg1;
    }

    @Column(name = "FWJG2", length = 20)
    public String getFwjg2() {
	return this.fwjg2;
    }

    public void setFwjg2(String fwjg2) {
	this.fwjg2 = fwjg2;
    }

    @Column(name = "FWJG3", length = 20)
    public String getFwjg3() {
	return this.fwjg3;
    }

    public void setFwjg3(String fwjg3) {
	this.fwjg3 = fwjg3;
    }

    @Column(name = "JZWZT", length = 20)
    public String getJzwzt() {
	return this.jzwzt;
    }

    public void setJzwzt(String jzwzt) {
	this.jzwzt = jzwzt;
    }

    @Column(name = "FWYT1", length = 20)
    public String getFwyt1() {
	return this.fwyt1;
    }

    public void setFwyt1(String fwyt1) {
	this.fwyt1 = fwyt1;
    }

    @Column(name = "FWYT2", length = 20)
    public String getFwyt2() {
	return this.fwyt2;
    }

    public void setFwyt2(String fwyt2) {
	this.fwyt2 = fwyt2;
    }

    @Column(name = "FWYT3", length = 20)
    public String getFwyt3() {
	return this.fwyt3;
    }

    public void setFwyt3(String fwyt3) {
	this.fwyt3 = fwyt3;
    }

    @Column(name = "ZCS", precision = 38, scale = 0)
    public BigDecimal getZcs() {
	return this.zcs;
    }

    public void setZcs(BigDecimal zcs) {
	this.zcs = zcs;
    }

    @Column(name = "DSCS", precision = 38, scale = 0)
    public BigDecimal getDscs() {
	return this.dscs;
    }

    public void setDscs(BigDecimal dscs) {
	this.dscs = dscs;
    }

    @Column(name = "DXCS", precision = 38, scale = 0)
    public BigDecimal getDxcs() {
	return this.dxcs;
    }

    public void setDxcs(BigDecimal dxcs) {
	this.dxcs = dxcs;
    }

    @Column(name = "BZ", length = 200)
    public String getBz() {
	return this.bz;
    }

    public void setBz(String bz) {
	this.bz = bz;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "JGRQ", length = 7)
    public Date getJgrq() {
	return this.jgrq;
    }

    public void setJgrq(Date jgrq) {
	this.jgrq = jgrq;
    }

    @Column(name = "GXXMBH", length = 40)
    public String getGxxmbh() {
	return this.gxxmbh;
    }

    public void setGxxmbh(String gxxmbh) {
	this.gxxmbh = gxxmbh;
    }

    @Column(name = "RELATIONID", length = 100)
    public String getRelationid() {
	return this.relationid;
    }

    public void setRelationid(String relationid) {
	this.relationid = relationid;
    }

}
