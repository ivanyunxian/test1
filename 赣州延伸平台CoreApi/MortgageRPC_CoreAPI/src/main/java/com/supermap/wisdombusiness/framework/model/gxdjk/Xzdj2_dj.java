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
 * Xzdj generated by hbm2java
 */
@Entity
@Table(name = "XZDJ2", schema = "GXDJK")
public class Xzdj2_dj implements java.io.Serializable {

    private String bsm;
    private String gxxmbh;
    private String bdcdyid;
    private String bdcdylx;
    private String bdcqzh;
    private String bdcdyh;
    private String xmbh;
    private String bxzrmc;
    private String bxzrzjzl;
    private String bxzrzjhm;
    private String xzdw;
    private Date sdtzrq;
    private Date xzqsrq;
    private Date xzzzrq;
    private String slr;
    private String slryj;
    private String xzlx;
    private String yxbz;
    private String lsxz;
    private String xzfw;
    private Date createtime;
    private Date modifytime;
    private Date djsj;
    private String dbr;
    private String ywh;
    private String bz;
    private Date zxdjsj;
    private String zxdbr;
    private String zxywh;
    private String zxbz;
    private String zxyj;
    private String zxxzwjhm;
    private String zxxzdw;
    private Date tssj;
    
    public Xzdj2_dj() {
    }

    public Xzdj2_dj(String bsm) {
	this.bsm = bsm;
    }

    public Xzdj2_dj( String bsm, String gxxmbh, String bdcdyid, String bdcdylx, String bdcqzh, String bdcdyh, String xmbh, String bxzrmc, String bxzrzjzl, String bxzrzjhm, String xzdw, Date sdtzrq, Date xzqsrq,
    		 Date xzzzrq, String slr, String slryj, String xzlx, String yxbz, String lsxz, String xzfw, Date createtime, Date modifytime, Date djsj, String dbr, String ywh, String bz, Date zxdjsj, String zxdbr,
    		 String zxywh, String zxbz, String zxyj, String zxxzwjhm, String zxxzdw,Date tssj) {
	this.bsm = bsm;
    this.gxxmbh=gxxmbh;
    this.bdcdyid=bdcdyid;
    this.bdcdylx=bdcdylx;
    this.bdcqzh=bdcqzh;
    this.bdcdyh=bdcdyh;
    this.xmbh=xmbh;
    this.bxzrmc=bxzrmc;
    this.bxzrzjzl=bxzrzjzl;
    this.bxzrzjhm=bxzrzjhm;
    this.xzdw=xzdw;
    this.sdtzrq=sdtzrq;
    this.xzqsrq=xzqsrq;
    this.xzzzrq=xzzzrq;
    this.slr=slr;
    this.slryj=slryj;
    this.xzlx=xzlx;
    this.yxbz=yxbz;
    this.lsxz=lsxz;
    this.xzfw=xzfw;
    this.createtime=createtime;
    this.modifytime=modifytime;
    this.djsj=djsj;
    this.dbr=dbr;
    this.ywh=ywh;
    this.bz=bz;
    this.zxdjsj=zxdjsj;
    this.zxdbr=zxdbr;
    this.zxywh=zxywh;
    this.zxbz=zxbz;
    this.zxyj=zxyj;
    this.zxxzwjhm=zxxzwjhm;
    this.zxxzdw=zxxzdw;
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

    @Column(name = "GXXMBH", length = 100)
    public String getGxxmbh() {
	return this.gxxmbh;
    }

    public void setGxxmbh(String gxxmbh) {
	this.gxxmbh = gxxmbh;
    }
    
    @Column(name = "BXZRZJZL", length = 100)
    public String getBxzrzjzl() {
	return this.bxzrzjzl;
    }

    public void setBxzrzjzl(String bxzrzjzl) {
	this.bxzrzjzl = bxzrzjzl;
    }
    @Column(name = "BDCDYID", length = 100)
    public String getBdcdyid() {
	return this.bdcdyid;
    }

    public void setBdcdyid(String bdcdyid) {
	this.bdcdyid = bdcdyid;
    }
    
    @Column(name = "BDCDYLX", length = 100)
    public String getBdcdylx() {
	return this.bdcdylx;
    }

    public void setBdcdylx(String bdcdylx) {
	this.bdcdylx = bdcdylx;
    }
    
    @Column(name = "BDCQZH", length = 100)
    public String getBdcqzh() {
	return this.bdcqzh;
    }

    public void setBdcqzh(String bdcqzh) {
	this.bdcqzh = bdcqzh;
    }
    
    @Column(name = "BDCDYH", length = 100)
    public String getBdcdyh() {
	return this.bdcdyh;
    }

    public void setBdcdyh(String bdcdyh) {
	this.bdcdyh = bdcdyh;
    }

    @Column(name = "XMBH", length = 100)
    public String getXmbh() {
	return this.xmbh;
    }

    public void setXmbh(String xmbh) {
	this.xmbh = xmbh;
    }
    
    @Column(name = "BXZRMC", length = 100)
    public String getBxzrmc() {
	return this.bxzrmc;
    }
	 
    public void setBxzrmc(String bxzrmc) {
	this.bxzrmc = bxzrmc;
    }
    
    @Column(name = "BXZRZJHM", length = 100)
    public String getBxzrzjhm() {
	return this.bxzrzjhm;
    }
	 
    public void setBxzrzjhm(String bxzrzjhm) {
	this.bxzrzjhm = bxzrzjhm;
    }
    
    @Column(name = "XZDW", length = 100)
    public String getXzdw() {
	return this.xzdw;
    }
	 
    public void setXzdw(String xzdw) {
	this.xzdw = xzdw;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
   	@DateTimeFormat(style = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "SDTZRQ", length = 7)
    public Date getSdtzrq() {
	return this.sdtzrq;
    }

    public void setSdtzrq(Date sdtzrq) {
	this.sdtzrq = sdtzrq;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
   	@DateTimeFormat(style = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "XZQSRQ", length = 7)
    public Date getXzqsrq() {
	return this.xzqsrq;
    }

    public void setXzqsrq(Date xzqsrq) {
	this.xzqsrq = xzqsrq;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
   	@DateTimeFormat(style = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "XZZZRQ", length = 7)
    public Date getXzzzrq() {
	return this.xzzzrq;
    }

    public void setXzzzrq(Date xzzzrq) {
	this.xzzzrq = xzzzrq;
    }
    
    @Column(name = "SLR", length = 100)
    public String getSlr() {
	return this.slr;
    }

    public void setSlr(String slr) {
	this.slr = slr;
    }
    
    @Column(name = "SLRYJ", length = 100)
    public String getSlryj() {
	return this.slryj;
    }

    public void setSlryj(String slryj) {
	this.slryj = slryj;
    }
    
    @Column(name = "XZLX", length = 100)
    public String getXzlx() {
	return this.xzlx;
    }

    public void setXzlx(String xzlx) {
	this.xzlx = xzlx;
    }
    
    @Column(name = "YXBZ", length = 100)
    public String getYxbz() {
	return this.yxbz;
    }

    public void setYxbz(String yxbz) {
	this.yxbz = yxbz;
    }

    @Column(name = "LSXZ", length = 100)
    public String getLsxz() {
	return this.lsxz;
    }

    public void setLsxz(String lsxz) {
	this.lsxz = lsxz;
    }
    
    
    @Column(name = "XZFW", length = 100)
    public String getXzfw() {
	return this.xzfw;
    }

    public void setXzfw(String xzfw) {
	this.xzfw = xzfw;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
   	@DateTimeFormat(style = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "CREATETIME", length = 7)
    public Date getCreatetime() {
	return this.createtime;
    }

    public void setCreatetime(Date createtime) {
	this.createtime = createtime;
    }
    @Temporal(TemporalType.TIMESTAMP)
   	@DateTimeFormat(style = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "MODIFYTIME", length = 7)
    public Date getModifytime() {
	return this.modifytime;
    }

    public void setModifytime(Date modifytime) {
	this.modifytime = modifytime;
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
    
    @Column(name = "DBR", length = 100)
    public String getDbr() {
	return this.dbr;
    }

    public void setDbr(String dbr) {
	this.dbr = dbr;
    }
    
    @Column(name = "YWH", length = 100)
    public String getYwh() {
	return this.ywh;
    }

    public void setYwh(String ywh) {
	this.ywh = ywh;
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
    @Column(name = "ZXDJSJ", length = 7)
    public Date getZxdjsj() {
	return this.zxdjsj;
    }

    public void setZxdjsj(Date zxdjsj) {
	this.zxdjsj = zxdjsj;
    }
    
    @Column(name = "ZXDBR", length = 4000)
    public String getZxdbr() {
	return this.zxdbr;
    }

    public void setZxdbr(String zxdbr) {
	this.zxdbr = zxdbr;
    }
    
    @Column(name = "ZXYWH", length = 4000)
    public String getZxywh() {
	return this.zxywh;
    }

    public void setZxywh(String zxywh) {
	this.zxywh = zxywh;
    }
    
    @Column(name = "ZXBZ", length = 4000)
    public String getZxbz() {
	return this.zxbz;
    }

    public void setZxbz(String zxbz) {
	this.zxbz = zxbz;
    }
    
    @Column(name = "ZXYJ", length = 4000)
    public String getZxyj() {
	return this.zxyj;
    }

    public void setZxyj(String zxyj) {
	this.zxyj = zxyj;
    }
    
    @Column(name = "ZXXZWJHM", length = 4000)
    public String getZxxzwjhm() {
	return this.zxxzwjhm;
    }

    public void setZxxzwjhm(String zxxzwjhm) {
	this.zxxzwjhm = zxxzwjhm;
    }
    
    @Column(name = "ZXXZDW", length = 4000)
    public String getZxxzdw() {
	return this.zxxzdw;
    }

    public void setZxxzdw(String zxxzdw) {
	this.zxxzdw = zxxzdw;
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
