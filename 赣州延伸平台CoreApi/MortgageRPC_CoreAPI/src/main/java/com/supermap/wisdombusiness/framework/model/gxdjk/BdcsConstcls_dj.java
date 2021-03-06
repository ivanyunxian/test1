package com.supermap.wisdombusiness.framework.model.gxdjk;

// Generated 2016-3-28 10:42:41 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * BdcsConstcls generated by hbm2java
 */
@Entity
@Table(name = "BDCS_CONSTCLS", schema = "GXDJK")
public class BdcsConstcls_dj implements java.io.Serializable {

    private BigDecimal mbbsm;
    private BigDecimal constslsid;
    private String constclsname;
    private String constclstype;
    private String bz;
    private Date createtime;
    private Date modifytime;

    public BdcsConstcls_dj() {
    }

    public BdcsConstcls_dj(BigDecimal mbbsm) {
	this.mbbsm = mbbsm;
    }

    public BdcsConstcls_dj(BigDecimal mbbsm, BigDecimal constslsid,
	    String constclsname, String constclstype, String bz,
	    Date createtime, Date modifytime) {
	this.mbbsm = mbbsm;
	this.constslsid = constslsid;
	this.constclsname = constclsname;
	this.constclstype = constclstype;
	this.bz = bz;
	this.createtime = createtime;
	this.modifytime = modifytime;
    }

    @Id
    @Column(name = "MBBSM", unique = true, nullable = false, precision = 22, scale = 0)
    public BigDecimal getMbbsm() {
	return this.mbbsm;
    }

    public void setMbbsm(BigDecimal mbbsm) {
	this.mbbsm = mbbsm;
    }

    @Column(name = "CONSTSLSID", precision = 22, scale = 0)
    public BigDecimal getConstslsid() {
	return this.constslsid;
    }

    public void setConstslsid(BigDecimal constslsid) {
	this.constslsid = constslsid;
    }

    @Column(name = "CONSTCLSNAME", length = 200)
    public String getConstclsname() {
	return this.constclsname;
    }

    public void setConstclsname(String constclsname) {
	this.constclsname = constclsname;
    }

    @Column(name = "CONSTCLSTYPE", length = 200)
    public String getConstclstype() {
	return this.constclstype;
    }

    public void setConstclstype(String constclstype) {
	this.constclstype = constclstype;
    }

    @Column(name = "BZ", length = 400)
    public String getBz() {
	return this.bz;
    }

    public void setBz(String bz) {
	this.bz = bz;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATETIME", length = 7)
    public Date getCreatetime() {
	return this.createtime;
    }

    public void setCreatetime(Date createtime) {
	this.createtime = createtime;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "MODIFYTIME", length = 7)
    public Date getModifytime() {
	return this.modifytime;
    }

    public void setModifytime(Date modifytime) {
	this.modifytime = modifytime;
    }

}
