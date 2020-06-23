package com.supermap.realestate.registration.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BDCS_CRHT", schema = "bdck")
public class BDCS_CRHT {
	
	private String ID;
	private String dyh;
	private String zddm;
	private String zdszx;
	private Double zdmj;
	private String zdzl;
	private Double fttdmj;
	private Double dytdmj;
	private String qllx;
	private String zrzh;
	private String zdszn;
	private Double gytdmj;
	private String fh;
	private String djh;
	private Double jzmj;
	private String qlxz;
	private String zdszb;
	private String qzh;
	private String zdszd;
	private String filenumber;
	private String crf;
	private String crffrdb;
	
	private String srf;
	private String srffrdb;
	
	
	private String crfdz;//出让方地址
	private String srfdz;//受让方地址
	private String th;//房号
	private String yqzh;//土地证号
	private Double sfjzmj;//售房建筑面积
	private Double crmj;//出让面积
	private String crtddj;//出让土地等级
	private String crqx;//出让期限
	private String crjbz;//出让金标准
	private String tdyt;//土地用途
	private String crjze;//出让金额
	private String rmb;//人民币
	private String bz;//备注
	private String slbh;//受理编号
	
	@Column(name = "CRF")
	public String getCrf() {
		return crf;
	}

	public void setCrf(String crf) {
		this.crf = crf;
	}

	@Column(name = "CRFFRDB")
	public String getCrffrdb() {
		return crffrdb;
	}

	public void setCrffrdb(String crffrdb) {
		this.crffrdb = crffrdb;
	}
	@Column(name = "SRF")
	public String getSrf() {
		return srf;
	}

	public void setSrf(String srf) {
		this.srf = srf;
	}
	@Column(name = "SRFFRDB")
	public String getSrffrdb() {
		return srffrdb;
	}

	public void setSrffrdb(String srffrdb) {
		this.srffrdb = srffrdb;
	}

	@Id
	@Column(name = "id")
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}
	@Column(name = "DYH")

	public String getDyh() {
		return dyh;
	}

	public void setDyh(String dyh) {
		this.dyh = dyh;
	}
	@Column(name = "ZDDM")

	public String getZddm() {
		return zddm;
	}

	public void setZddm(String zddm) {
		this.zddm = zddm;
	}
	@Column(name = "ZDSZX" )

	public String getZdszx() {
		return zdszx;
	}

	public void setZdszx(String zdszx) {
		this.zdszx = zdszx;
	}
	@Column(name = "ZDMJ" )

	public Double getZdmj() {
		return zdmj;
	}

	public void setZdmj(Double zdmj) {
		this.zdmj = zdmj;
	}
	@Column(name = "ZDZL" )

	public String getZdzl() {
		return zdzl;
	}

	public void setZdzl(String zdzl) {
		this.zdzl = zdzl;
	}
	@Column(name = "FTTDMJ" )

	public Double getFttdmj() {
		return fttdmj;
	}

	public void setFttdmj(Double fttdmj) {
		this.fttdmj = fttdmj;
	}
	@Column(name = "DYTDMJ" )

	public Double getDytdmj() {
		return dytdmj;
	}

	public void setDytdmj(Double dytdmj) {
		this.dytdmj = dytdmj;
	}
	@Column(name = "QLLX" )

	public String getQllx() {
		return qllx;
	}

	public void setQllx(String qllx) {
		this.qllx = qllx;
	}
	@Column(name = "ZRZH" )

	public String getZrzh() {
		return zrzh;
	}

	public void setZrzh(String zrzh) {
		this.zrzh = zrzh;
	}
	@Column(name = "ZDSZN" )

	public String getZdszn() {
		return zdszn;
	}

	public void setZdszn(String zdszn) {
		this.zdszn = zdszn;
	}
	@Column(name = "GYTDMJ" )

	public Double getGytdmj() {
		return gytdmj;
	}

	public void setGytdmj(Double gytdmj) {
		this.gytdmj = gytdmj;
	}
	@Column(name = "FH" )

	public String getFh() {
		return fh;
	}

	public void setFh(String fh) {
		this.fh = fh;
	}
	@Column(name = "DJH" )

	public String getDjh() {
		return djh;
	}

	public void setDjh(String djh) {
		this.djh = djh;
	}
	@Column(name = "JZMJ" )

	public Double getJzmj() {
		return jzmj;
	}

	public void setJzmj(Double jzmj) {
		this.jzmj = jzmj;
	}
	@Column(name = "QLXZ" )

	public String getQlxz() {
		return qlxz;
	}

	public void setQlxz(String qlxz) {
		this.qlxz = qlxz;
	}
	@Column(name = "ZDSZB" )

	public String getZdszb() {
		return zdszb;
	}

	public void setZdszb(String zdszb) {
		this.zdszb = zdszb;
	}
	@Column(name = "QZH" )

	public String getQzh() {
		return qzh;
	}

	public void setQzh(String qzh) {
		this.qzh = qzh;
	}
	@Column(name = "ZDSZD" )

	public String getZdszd() {
		return zdszd;
	}

	public void setZdszd(String zdszd) {
		this.zdszd = zdszd;
	}
	@Column(name = "FILENUMBER" )

	public String getFilenumber() {
		return filenumber;
	}

	public void setFilenumber(String filenumber) {
		this.filenumber = filenumber;
	}
	@Column(name = "CRFDZ" )

	public String getCrfdz() {
		return crfdz;
	}

	public void setCrfdz(String crfdz) {
		this.crfdz = crfdz;
	}
	@Column(name = "SRFDZ" )

	public String getSrfdz() {
		return srfdz;
	}

	public void setSrfdz(String srfdz) {
		this.srfdz = srfdz;
	}
	@Column(name = "TH" )

	public String getTh() {
		return th;
	}

	public void setTh(String th) {
		this.th = th;
	}
	@Column(name = "YQZH" )

	public String getYqzh() {
		return yqzh;
	}

	public void setYqzh(String yqzh) {
		this.yqzh = yqzh;
	}
	@Column(name = "SFJZMJ" )

	public Double getSfjzmj() {
		return sfjzmj;
	}

	public void setSfjzmj(Double sfjzmj) {
		this.sfjzmj = sfjzmj;
	}
	@Column(name = "CRMJ" )

	public Double getCrmj() {
		return crmj;
	}

	public void setCrmj(Double crmj) {
		this.crmj = crmj;
	}
	@Column(name = "CRTDDJ" )

	public String getCrtddj() {
		return crtddj;
	}

	public void setCrtddj(String crtddj) {
		this.crtddj = crtddj;
	}
	@Column(name = "CRQX" )

	public String getCrqx() {
		return crqx;
	}

	public void setCrqx(String crqx) {
		this.crqx = crqx;
	}
	@Column(name = "CRJBZ" )

	public String getCrjbz() {
		return crjbz;
	}

	public void setCrjbz(String crjbz) {
		this.crjbz = crjbz;
	}
	@Column(name = "TDYT" )

	public String getTdyt() {
		return tdyt;
	}

	public void setTdyt(String tdyt) {
		this.tdyt = tdyt;
	}
	@Column(name = "CRJZE" )

	public String getCrjze() {
		return crjze;
	}

	public void setCrjze(String crjze) {
		this.crjze = crjze;
	}
	@Column(name = "RMB" )

	public String getRmb() {
		return rmb;
	}

	public void setRmb(String rmb) {
		this.rmb = rmb;
	}
	@Column(name = "BZ" )

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getSlbh() {
		return slbh;
	}

	public void setSlbh(String slbh) {
		this.slbh = slbh;
	}
    
	
	
}
