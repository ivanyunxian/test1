package com.supermap.wisdombusiness.framework.model.gxdjk;

// Generated 2016-3-28 10:42:41 by Hibernate Tools 4.0.0

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
 * Cfdj generated by hbm2java
 */
@Entity
@Table(name = "CFDJ", schema = "GXDJK")
public class Cfdj_dj implements java.io.Serializable {

    private String bsm;
    private String bdcdyh;
    private String ywh;
    private String qlid;
    private String cflx;
    private String cfjg;
    private String cfwh;
    private String cfwj;
    private String cffw;
    private String jfjg;
    private String jfwh;
    private String jfwj;
    private Date qlqssj;
    private Date qljssj;
    private String qxdm;
    private String djjg;
    private String dbr;
    private Date djsj;
    private String jfywh;
    private String jfdbr;
    private String jfdjsj;
    private String fj;
    private String qszt;
    private String bdcdyid;
    private String gxxmbh;
    private String lyqlid;
    private String zsbh;
    private String bz;
    private Date tssj;
    public Cfdj_dj() {
    }

    public Cfdj_dj(String bsm) {
	this.bsm = bsm;
    }

    public Cfdj_dj(String bsm, String bdcdyh, String ywh, String qlid,
	    String cflx, String cfjg, String cfwh, String cfwj, String cffw,
	    String jfjg, String jfwh, String jfwj, Date qlqssj, Date qljssj,
	    String qxdm, String djjg, String dbr, Date djsj, String jfywh,
	    String jfdbr, String jfdjsj, String fj, String qszt,
	    String bdcdyid, String gxxmbh, String lyqlid, String zsbh, String bz,Date tssj) {
	this.bsm = bsm;
	this.bdcdyh = bdcdyh;
	this.ywh = ywh;
	this.qlid = qlid;
	this.cflx = cflx;
	this.cfjg = cfjg;
	this.cfwh = cfwh;
	this.cfwj = cfwj;
	this.cffw = cffw;
	this.jfjg = jfjg;
	this.jfwh = jfwh;
	this.jfwj = jfwj;
	this.qlqssj = qlqssj;
	this.qljssj = qljssj;
	this.qxdm = qxdm;
	this.djjg = djjg;
	this.dbr = dbr;
	this.djsj = djsj;
	this.jfywh = jfywh;
	this.jfdbr = jfdbr;
	this.jfdjsj = jfdjsj;
	this.fj = fj;
	this.qszt = qszt;
	this.bdcdyid = bdcdyid;
	this.gxxmbh = gxxmbh;
	this.lyqlid = lyqlid;
	this.zsbh = zsbh;
	this.bz = bz;
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

    @Column(name = "BDCDYH", length = 100)
    public String getBdcdyh() {
	return this.bdcdyh;
    }

    public void setBdcdyh(String bdcdyh) {
	this.bdcdyh = bdcdyh;
    }

    @Column(name = "YWH", length = 100)
    public String getYwh() {
	return this.ywh;
    }

    public void setYwh(String ywh) {
	this.ywh = ywh;
    }

    @Column(name = "QLID", length = 100)
    public String getQlid() {
	return this.qlid;
    }

    public void setQlid(String qlid) {
	this.qlid = qlid;
    }

    @Column(name = "CFLX", length = 4)
    public String getCflx() {
	return this.cflx;
    }

    public void setCflx(String cflx) {
	this.cflx = cflx;
    }

    @Column(name = "CFJG", length = 400)
    public String getCfjg() {
	return this.cfjg;
    }

    public void setCfjg(String cfjg) {
	this.cfjg = cfjg;
    }

    @Column(name = "CFWH", length = 200)
    public String getCfwh() {
	return this.cfwh;
    }

    public void setCfwh(String cfwh) {
	this.cfwh = cfwh;
    }

    @Column(name = "CFWJ", length = 400)
    public String getCfwj() {
	return this.cfwj;
    }

    public void setCfwj(String cfwj) {
	this.cfwj = cfwj;
    }

    @Column(name = "CFFW", length = 4000)
    public String getCffw() {
	return this.cffw;
    }

    public void setCffw(String cffw) {
	this.cffw = cffw;
    }

    @Column(name = "JFJG", length = 400)
    public String getJfjg() {
	return this.jfjg;
    }

    public void setJfjg(String jfjg) {
	this.jfjg = jfjg;
    }

    @Column(name = "JFWH", length = 400)
    public String getJfwh() {
	return this.jfwh;
    }

    public void setJfwh(String jfwh) {
	this.jfwh = jfwh;
    }

    @Column(name = "JFWJ", length = 400)
    public String getJfwj() {
	return this.jfwj;
    }

    public void setJfwj(String jfwj) {
	this.jfwj = jfwj;
    }

    @Temporal(TemporalType.TIMESTAMP)
   	@DateTimeFormat(style = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "QLQSSJ", length = 7)
    public Date getQlqssj() {
	return this.qlqssj;
    }

    public void setQlqssj(Date qlqssj) {
	this.qlqssj = qlqssj;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "QLJSSJ", length = 7)
    public Date getQljssj() {
	return this.qljssj;
    }

    public void setQljssj(Date qljssj) {
	this.qljssj = qljssj;
    }

    @Column(name = "QXDM", length = 100)
    public String getQxdm() {
	return this.qxdm;
    }

    public void setQxdm(String qxdm) {
	this.qxdm = qxdm;
    }

    @Column(name = "DJJG", length = 400)
    public String getDjjg() {
	return this.djjg;
    }

    public void setDjjg(String djjg) {
	this.djjg = djjg;
    }

    @Column(name = "DBR", length = 100)
    public String getDbr() {
	return this.dbr;
    }

    public void setDbr(String dbr) {
	this.dbr = dbr;
    }

    @Temporal(TemporalType.TIMESTAMP)
   	@DateTimeFormat(style = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "DJSJ", length = 7)
    public Date getDjsj() {
	return this.djsj;
    }

    public void setDjsj(Date djsj) {
	this.djsj = djsj;
    }

    @Column(name = "JFYWH", length = 400)
    public String getJfywh() {
	return this.jfywh;
    }

    public void setJfywh(String jfywh) {
	this.jfywh = jfywh;
    }

    @Column(name = "JFDBR", length = 100)
    public String getJfdbr() {
	return this.jfdbr;
    }

    public void setJfdbr(String jfdbr) {
	this.jfdbr = jfdbr;
    }

    @Column(name = "JFDJSJ", length = 400)
    public String getJfdjsj() {
	return this.jfdjsj;
    }

    public void setJfdjsj(String jfdjsj) {
	this.jfdjsj = jfdjsj;
    }

    @Column(name = "FJ", length = 4000)
    public String getFj() {
	return this.fj;
    }

    public void setFj(String fj) {
	this.fj = fj;
    }

    @Column(name = "QSZT", length = 4)
    public String getQszt() {
	return this.qszt;
    }

    public void setQszt(String qszt) {
	this.qszt = qszt;
    }

    @Column(name = "BDCDYID", length = 100)
    public String getBdcdyid() {
	return this.bdcdyid;
    }

    public void setBdcdyid(String bdcdyid) {
	this.bdcdyid = bdcdyid;
    }

    @Column(name = "GXXMBH", length = 100)
    public String getGxxmbh() {
	return this.gxxmbh;
    }

    public void setGxxmbh(String gxxmbh) {
	this.gxxmbh = gxxmbh;
    }

    @Column(name = "LYQLID", length = 100)
    public String getLyqlid() {
	return this.lyqlid;
    }

    public void setLyqlid(String lyqlid) {
	this.lyqlid = lyqlid;
    }

    @Column(name = "ZSBH", length = 40)
    public String getZsbh() {
	return this.zsbh;
    }

    public void setZsbh(String zsbh) {
	this.zsbh = zsbh;
    }

    @Column(name = "BZ", length = 4000)
    public String getBz() {
	return this.bz;
    }

    public void setBz(String bz) {
	this.bz = bz;
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
