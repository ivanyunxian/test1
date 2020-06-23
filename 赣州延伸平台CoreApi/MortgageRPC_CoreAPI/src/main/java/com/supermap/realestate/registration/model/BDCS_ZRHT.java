package com.supermap.realestate.registration.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BDCS_ZRHT", schema = "bdck")
public class BDCS_ZRHT {
	private String id;
	private String th;
	private String shyqlx;
	private String zdh;
	private String tdyt;
	private String tddj;
	private Date crqssj;
	private Date crzzsj;
	private String srf;
	private String srffrdb;
	private String srfdz;
	private String zrf;
	private String zrffrdb;
	private String zrfdz;
	private String tdzh;
	private String qsxz;
	private String fwsyqzh;
	private String zdszx;
	private Double zdmj;
	private Double fttdmj;
	private Double dytdmj;
	private String zdszn;
	private Double gytdmj;
	private Double jzmj;
	private String zdszb;
	private String qzh;
	private String zdszd;
	private String filenumber;
	
	private Double zhdmj;
	private String ytdyt;
	private String crjdj;
	private String crjzj;
	private String lglfbz;
	private String yjlgf;
	private String dahcqk;
	private String jbr;
	private String jbryj;
	private Date jbsj;
	private String shr;
	private Date shsj;
	private String shyj;
	private String bz;
	private String spyj;
	private String spr;
	private Date spsj;
	private String xmkfztz;
	private String gctz;
	@Id
	@Column(name = "ID")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name = "TH")

	public String getTh() {
		return th;
	}
	public void setTh(String th) {
		this.th = th;
	}
	@Column(name = "SHYQLX")

	public String getShyqlx() {
		return shyqlx;
	}
	public void setShyqlx(String shyqlx) {
		this.shyqlx = shyqlx;
	}
	@Column(name = "ZDH")

	public String getZdh() {
		return zdh;
	}
	public void setZdh(String zdh) {
		this.zdh = zdh;
	}
	@Column(name = "TDYT")

	public String getTdyt() {
		return tdyt;
	}
	public void setTdyt(String tdyt) {
		this.tdyt = tdyt;
	}
	@Column(name = "TDDJ")

	public String getTddj() {
		return tddj;
	}
	public void setTddj(String tddj) {
		this.tddj = tddj;
	}
	@Column(name = "CRQSSJ")

	public Date getCrqssj() {
		return crqssj;
	}
	public void setCrqssj(Date crqssj) {
		this.crqssj = crqssj;
	}
	@Column(name = "CRZZSJ")

	public Date getCrzzsj() {
		return crzzsj;
	}
	public void setCrzzsj(Date crzzsj) {
		this.crzzsj = crzzsj;
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
	@Column(name = "SRFDZ")

	public String getSrfdz() {
		return srfdz;
	}
	public void setSrfdz(String srfdz) {
		this.srfdz = srfdz;
	}
	@Column(name = "ZRF")

	public String getZrf() {
		return zrf;
	}
	public void setZrf(String zrf) {
		this.zrf = zrf;
	}
	@Column(name = "ZRFFRDB")

	public String getZrffrdb() {
		return zrffrdb;
	}
	public void setZrffrdb(String zrffrdb) {
		this.zrffrdb = zrffrdb;
	}
	@Column(name = "ZRFDZ")

	public String getZrfdz() {
		return zrfdz;
	}
	public void setZrfdz(String zrfdz) {
		this.zrfdz = zrfdz;
	}
	@Column(name = "TDZH")

	public String getTdzh() {
		return tdzh;
	}
	public void setTdzh(String tdzh) {
		this.tdzh = tdzh;
	}
	@Column(name = "QSXZ")

	public String getQsxz() {
		return qsxz;
	}
	public void setQsxz(String qsxz) {
		this.qsxz = qsxz;
	}
	@Column(name = "FWSYQZH")

	public String getFwsyqzh() {
		return fwsyqzh;
	}
	public void setFwsyqzh(String fwsyqzh) {
		this.fwsyqzh = fwsyqzh;
	}
	@Column(name = "ZDSZX")

	public String getZdszx() {
		return zdszx;
	}
	public void setZdszx(String zdszx) {
		this.zdszx = zdszx;
	}
	@Column(name = "ZDMJ")

	public Double getZdmj() {
		return zdmj;
	}
	public void setZdmj(Double zdmj) {
		this.zdmj = zdmj;
	}
	@Column(name = "FTTDMJ")

	public Double getFttdmj() {
		return fttdmj;
	}
	public void setFttdmj(Double fttdmj) {
		this.fttdmj = fttdmj;
	}
	@Column(name = "DYTDMJ")

	public Double getDytdmj() {
		return dytdmj;
	}
	public void setDytdmj(Double dytdmj) {
		this.dytdmj = dytdmj;
	}
	@Column(name = "ZDSZN")

	public String getZdszn() {
		return zdszn;
	}
	public void setZdszn(String zdszn) {
		this.zdszn = zdszn;
	}
	@Column(name = "GYTDMJ")

	public Double getGytdmj() {
		return gytdmj;
	}
	public void setGytdmj(Double gytdmj) {
		this.gytdmj = gytdmj;
	}
	@Column(name = "JZMJ")

	public Double getJzmj() {
		return jzmj;
	}
	public void setJzmj(Double jzmj) {
		this.jzmj = jzmj;
	}
	@Column(name = "ZDSZB")

	public String getZdszb() {
		return zdszb;
	}
	public void setZdszb(String zdszb) {
		this.zdszb = zdszb;
	}
	@Column(name = "QZH")

	public String getQzh() {
		return qzh;
	}
	public void setQzh(String qzh) {
		this.qzh = qzh;
	}
	@Column(name = "ZDSZD")

	public String getZdszd() {
		return zdszd;
	}
	public void setZdszd(String zdszd) {
		this.zdszd = zdszd;
	}
	@Column(name = "FILENUMBER")

	public String getFilenumber() {
		return filenumber;
	}
	public void setFilenumber(String filenumber) {
		this.filenumber = filenumber;
	}
	@Column(name = "ZHDMJ")

	public Double getZhdmj() {
		return zhdmj;
	}
	public void setZhdmj(Double zhdmj) {
		this.zhdmj = zhdmj;
	}
	@Column(name = "YTDYT")

	public String getYtdyt() {
		return ytdyt;
	}
	public void setYtdyt(String ytdyt) {
		this.ytdyt = ytdyt;
	}
	@Column(name = "CRJDJ")

	public String getCrjdj() {
		return crjdj;
	}
	public void setCrjdj(String crjdj) {
		this.crjdj = crjdj;
	}
	@Column(name = "CRJZJ")

	public String getCrjzj() {
		return crjzj;
	}
	public void setCrjzj(String crjzj) {
		this.crjzj = crjzj;
	}
	@Column(name = "LGLFBZ")

	public String getLglfbz() {
		return lglfbz;
	}
	public void setLglfbz(String lglfbz) {
		this.lglfbz = lglfbz;
	}
	@Column(name = "YJLGF")

	public String getYjlgf() {
		return yjlgf;
	}
	public void setYjlgf(String yjlgf) {
		this.yjlgf = yjlgf;
	}
	@Column(name = "DAHCQK")

	public String getDahcqk() {
		return dahcqk;
	}
	public void setDahcqk(String dahcqk) {
		this.dahcqk = dahcqk;
	}
	@Column(name = "JBR")

	public String getJbr() {
		return jbr;
	}
	public void setJbr(String jbr) {
		this.jbr = jbr;
	}
	@Column(name = "JBRYJ")

	public String getJbryj() {
		return jbryj;
	}
	public void setJbryj(String jbryj) {
		this.jbryj = jbryj;
	}
	@Column(name = "JBSJ")

	public Date getJbsj() {
		return jbsj;
	}
	public void setJbsj(Date jbsj) {
		this.jbsj = jbsj;
	}
	@Column(name = "SHR")

	public String getShr() {
		return shr;
	}
	public void setShr(String shr) {
		this.shr = shr;
	}
	@Column(name = "SHSJ")

	public Date getShsj() {
		return shsj;
	}
	public void setShsj(Date shsj) {
		this.shsj = shsj;
	}
	@Column(name = "SHYJ")

	public String getShyj() {
		return shyj;
	}
	public void setShyj(String shyj) {
		this.shyj = shyj;
	}
	@Column(name = "BZ")

	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	@Column(name = "SPYJ")

	public String getSpyj() {
		return spyj;
	}
	public void setSpyj(String spyj) {
		this.spyj = spyj;
	}
	@Column(name = "SPR")

	public String getSpr() {
		return spr;
	}
	public void setSpr(String spr) {
		this.spr = spr;
	}
	@Column(name = "SPSJ")

	public Date getSpsj() {
		return spsj;
	}
	public void setSpsj(Date spsj) {
		this.spsj = spsj;
	}
	@Column(name = "XMKFZTZ")

	public String getXmkfztz() {
		return xmkfztz;
	}
	public void setXmkfztz(String xmkfztz) {
		this.xmkfztz = xmkfztz;
	}
	@Column(name = "GCTZ")

	public String getGctz() {
		return gctz;
	}
	public void setGctz(String gctz) {
		this.gctz = gctz;
	}
	
	
	
	
	
}
