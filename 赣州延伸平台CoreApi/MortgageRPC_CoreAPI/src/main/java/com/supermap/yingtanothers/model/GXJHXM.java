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
 * 功能：鹰潭不动产共享中间库共享交换项目属性结构表
 */
@Entity
@Table(name = "gxjhxm", schema = "gxfck")
public class GXJHXM{
	    
    private String gxxmbh;
    private String djdl;
    private String djxl;
    private String qllx;
    private String gxlx;
    private String xmmc;
    private String zl;
    private String bdcdyh;
    private String sqr;
    private Date slsj;
    private String slry;
    private String zt;
    private String bljd;
    private String casenum;
    private String qlsdfs;
    private String project_id;
    private Date tssj;
    private String gxjhxm;
        
	
	
	@Column(name = "bdcdyh")
	public String getBdcdyh() {
		return bdcdyh;
	}
	public void setBdcdyh(String bdcdyh) {
		this.bdcdyh = bdcdyh;
	}
	@Column(name = "qllx")
	public String getQllx() {
		return qllx;
	}
	public void setQllx(String qllx) {
		this.qllx = qllx;
	}
	@Column(name = "gxlx")
	public String getGxlx() {
		return gxlx;
	}
	public void setGxlx(String gxlx) {
		this.gxlx = gxlx;
	}
	@Column(name = "xmmc")
	public String getXmmc() {
		return xmmc;
	}
	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}
	@Column(name = "zl")
	public String getZl() {
		return zl;
	}
	public void setZl(String zl) {
		this.zl = zl;
	}
	@Column(name = "gxxmbh")
	public String getGxxmbh() {
		return gxxmbh;
	}
	public void setGxxmbh(String gxxmbh) {
		this.gxxmbh = gxxmbh;
	}
	@Column(name = "djdl")
	public String getDjdl() {
		return djdl;
	}
	public void setDjdl(String djdl) {
		this.djdl = djdl;
	}
	@Column(name = "djxl")
	public String getDjxl() {
		return djxl;
	}
	public void setDjxl(String djxl) {
		this.djxl = djxl;
	}
	@Column(name = "sqr")
	public String getSqr() {
		return sqr;
	}
	public void setSqr(String sqr) {
		this.sqr = sqr;
	}
	@Column(name = "slsj")
	public Date getSlsj() {
		return slsj;
	}
	public void setSlsj(Date slsj) {
		this.slsj = slsj;
	}
	@Column(name = "slry")
	public String getSlry() {
		return slry;
	}
	public void setSlry(String slry) {
		this.slry = slry;
	}
	@Column(name = "zt")
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
	@Column(name = "bljd")
	public String getBljd() {
		return bljd;
	}
	public void setBljd(String bljd) {
		this.bljd = bljd;
	}
	@Column(name = "casenum")
	public String getCasenum() {
		return casenum;
	}
	public void setCasenum(String casenum) {
		this.casenum = casenum;
	}
	@Column(name = "qlsdfs")
	public String getQlsdfs() {
		return qlsdfs;
	}
	public void setQlsdfs(String qlsdfs) {
		this.qlsdfs = qlsdfs;
	}
	@Column(name = "project_id")
	public String getProject_id() {
		return project_id;
	}
	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}
	@Column(name = "tssj")
	public Date getTssj() {
		return tssj;
	}
	public void setTssj(Date tssj) {
		this.tssj = tssj;
	}
	@Id
	@Column(name = "gxjhxm")
	public String getGxjhxm() {
		return gxjhxm;
	}
	public void setGxjhxm(String gxjhxm) {
		this.gxjhxm = gxjhxm;
	}
		    
}
