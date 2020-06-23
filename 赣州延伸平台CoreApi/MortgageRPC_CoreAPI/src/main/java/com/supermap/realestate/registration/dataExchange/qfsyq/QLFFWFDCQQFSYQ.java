package com.supermap.realestate.registration.dataExchange.qfsyq;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.StringHelper;

/**
 * 
 * @Description:建筑物区分所有权业主共有部分表
 * @author DIAOLIWEI
 * @date 2015年8月28日 下午9:21:32
 * @Copyright SuperMap
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "QLF_FW_FDCQ_QFSYQ")
public class QLFFWFDCQQFSYQ {
	
    @XmlAttribute(name = "QSZT", required = true)
    protected String qszt;
    @XmlAttribute(name = "YSDM", required = true)
    protected String ysdm;
    @XmlAttribute(name = "BDCDYH", required = true)
    protected String bdcdyh;
    @XmlAttribute(name = "FJ")
    protected String fj;
    @XmlAttribute(name = "DJSJ", required = true)
    protected String djsj;
    @XmlAttribute(name = "DBR", required = true)
    protected String dbr;
    @XmlAttribute(name = "QXDM", required = true)
    protected String qxdm;
    @XmlAttribute(name = "DJJG", required = true)
    protected String djjg;
    @XmlAttribute(name = "JGZWMJ", required = true)
    protected double jgzwmj;
    @XmlAttribute(name = "FTTDMJ")
    protected double fttdmj;
    @XmlAttribute(name = "JGZWSL", required = true)
    protected int jgzwsl;
    @XmlAttribute(name = "JGZWMC")
    protected String jgzwmc;
    @XmlAttribute(name = "JGZWBH", required = true)
    protected String jgzwbh;
    @XmlAttribute(name = "QLLX", required = true)
    protected String qllx;
    @XmlAttribute(name = "YWH")
    protected String ywh;
    
    @XmlAttribute(name = "QLID")
    protected String qlid;
	@XmlAttribute(name = "XMBH")
	protected String xmbh;
	@XmlAttribute(name = "BDCDYID")
	protected String bdcdyid;
    
	public String getYsdm() {
		return ysdm;
	}
	public void setYsdm(String ysdm) {
		this.ysdm = ysdm;
	}
	public String getBdcdyh() {
		return bdcdyh;
	}
	public void setBdcdyh(String bdcdyh) {
		this.bdcdyh = bdcdyh;
	}
	public String getQxdm() {
		return qxdm;
	}
	public void setQxdm(String qxdm) {
		this.qxdm = qxdm;
	}
	public String getQszt() {
		return qszt;
	}
	public void setQszt(String qszt) {
		this.qszt = qszt;
	}
	public String getFj() {
		return fj;
	}
	public void setFj(String fj) {
		this.fj = fj;
	}
	public String getDjsj() {
		return djsj;
	}
	public void setDjsj(String djsj) {
		this.djsj = djsj;
	}
	public String getDbr() {
		return dbr;
	}
	public void setDbr(String dbr) {
		this.dbr = dbr;
	}
	public String getDjjg() {
		return djjg;
	}
	public void setDjjg(String djjg) {
		this.djjg = djjg;
	}
	public double getJgzwmj() {
		return jgzwmj;
	}
	public void setJgzwmj(double jgzwmj) {
		this.jgzwmj = StringHelper.getDouble(StringHelper.cut(jgzwmj, 2));
	}
	public double getFttdmj() {
		return fttdmj;
	}
	public void setFttdmj(double fttdmj) {
		this.fttdmj = StringHelper.getDouble(StringHelper.cut(fttdmj, 2));
	}
	public int getJgzwsl() {
		return jgzwsl;
	}
	public void setJgzwsl(int jgzwsl) {
		this.jgzwsl = jgzwsl;
	}
	public String getJgzwmc() {
		return jgzwmc;
	}
	public void setJgzwmc(String jgzwmc) {
		this.jgzwmc = jgzwmc;
	}
	public String getJgzwbh() {
		return jgzwbh;
	}
	public void setJgzwbh(String jgzwbh) {
		this.jgzwbh = jgzwbh;
	}
	public String getQllx() {
		return qllx;
	}
	public void setQllx(String qllx) {
		this.qllx = qllx;
		this.qllx=ConstHelper.getReportValueByValue("QLLX", qllx);
	}
	public String getYwh() {
		return ywh;
	}
	public void setYwh(String ywh) {
		this.ywh = ywh;
	}
	
	public String getQlid() {
		return qlid;
	}
	public void setQlid(String qlid) {
		this.qlid = qlid;
	}
	public String getXmbh() {
		return xmbh;
	}
	public void setXmbh(String xmbh) {
		this.xmbh = xmbh;
	}
	public String getBdcdyid() {
		return bdcdyid;
	}
	public void setBdcdyid(String bdcdyid) {
		this.bdcdyid = bdcdyid;
	}

	public void replaceEmpty(){
		if(StringHelper.isEmpty(this.bdcdyh)){
			this.bdcdyh=null;
		}
		if(StringHelper.isEmpty(this.dbr)){
			this.dbr=null;
		}
		if(StringHelper.isEmpty(this.djjg)){
			this.djjg=null;
		}
		if(StringHelper.isEmpty(this.djsj)){
			this.djsj=null;
		}
		if(StringHelper.isEmpty(this.fj)){
			this.fj=null;
		}
		if(StringHelper.isEmpty(this.jgzwbh)){
			this.jgzwbh=null;
		}
		if(StringHelper.isEmpty(this.jgzwmc)){
			this.jgzwmc=null;
		}
		if(StringHelper.isEmpty(this.qllx)){
			this.qllx=null;
		}
		if(StringHelper.isEmpty(this.qszt)){
			this.qszt=null;
		}
		if(StringHelper.isEmpty(this.qxdm)){
			this.qxdm=null;
		}
		if(StringHelper.isEmpty(this.ysdm)){
			this.ysdm=null;
		}
		if(StringHelper.isEmpty(this.ywh)){
			this.ywh=null;
		}
	}
}
