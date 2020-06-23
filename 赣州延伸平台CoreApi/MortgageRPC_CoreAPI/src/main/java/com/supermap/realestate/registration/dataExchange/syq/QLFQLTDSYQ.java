package com.supermap.realestate.registration.dataExchange.syq;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.StringHelper;


/**
 * 土地所有权表
 * @author diaoliwei
 * @date 2015-9-17 下午11:48:12
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "QLF_QL_TDSYQ")
public class QLFQLTDSYQ {

	 @XmlAttribute(name = "YSDM", required = true)
	 private String ysdm;
	 @XmlAttribute(name = "ZDDM", required = true)
	 private String zddm;
	 @XmlAttribute(name = "BDCDYH", required = true)
	 private String bdcdyh;
	 @XmlAttribute(name = "YWH", required = true)
	 private String ywh;
	 @XmlAttribute(name = "QLLX", required = true)
	 private String qllx;
	 @XmlAttribute(name = "DJLX", required = true)
	 private String djlx;
	 @XmlAttribute(name = "DJYY", required = true)
	 private String djyy;
	 @XmlAttribute(name = "MJDW", required = true)
	 private String mjdw;
	 @XmlAttribute(name = "NYDMJ")
	 private double nydmj;
	 @XmlAttribute(name = "GDMJ")
	 private double gdmj;
	 @XmlAttribute(name = "LDMJ")
	 private double ldmj;
	 @XmlAttribute(name = "CDMJ")
	 private double cdmj;
	 @XmlAttribute(name = "QTNYDMJ")
	 private double qtnydmj;
	 @XmlAttribute(name = "JSYDMJ")
	 private double jsydmj;
	 @XmlAttribute(name = "WLYDMJ")
	 private double wlydmj;
	 @XmlAttribute(name = "BDCQZH" , required = true)
	 private String bdcqzh;
	 @XmlAttribute(name = "QXDM" , required = true)
	 private String qxdm;
	 @XmlAttribute(name = "DJJG" , required = true)
	 private String djjg;
	 @XmlAttribute(name = "DBR" , required = true)
	 private String dbr;
	 @XmlAttribute(name = "DJSJ" , required = true)
	 private String djsj;
	 @XmlAttribute(name = "FJ")
	 private String fj;
	 @XmlAttribute(name = "QSZT")
	 private String qszt;
	 
	 @XmlAttribute(name = "QLQSSJ", required = true)
	 private String qlqssj;
	 @XmlAttribute(name = "QLJSSJ", required = true)
	 private String qljssj;
	 @XmlAttribute(name = "QLID")
	 private String qlid;
	 @XmlAttribute(name = "XMBH")
	 private String xmbh;
	 @XmlAttribute(name = "BDCDYID")
	 private String bdcdyid;
	 
	public String getYsdm() {
		return ysdm;
	}
	public void setYsdm(String ysdm) {
		this.ysdm = ysdm;
	}
	public String getZddm() {
		return zddm;
	}
	public void setZddm(String zddm) {
		this.zddm = zddm;
	}
	public String getBdcdyh() {
		return bdcdyh;
	}
	public void setBdcdyh(String bdcdyh) {
		this.bdcdyh = bdcdyh;
	}
	public String getYwh() {
		return ywh;
	}
	public void setYwh(String ywh) {
		this.ywh = ywh;
	}
	public String getQllx() {
		return qllx;
	}
	public void setQllx(String qllx) {
		this.qllx = qllx;
		this.qllx=ConstHelper.getReportValueByValue("QLLX", qllx);
	}
	public String getDjlx() {
		return djlx;
	}
	public void setDjlx(String djlx) {
		this.djlx = djlx;
		this.djlx=ConstHelper.getReportValueByValue("DJLX", djlx);
	}
	public String getDjyy() {
		return djyy;
	}
	public void setDjyy(String djyy) {
		this.djyy = djyy;
	}
	public String getMjdw() {
		return mjdw;
	}
	public void setMjdw(String mjdw) {
		this.mjdw = mjdw;
		this.mjdw=ConstHelper.getReportValueByValue("MJDW", mjdw);
	}
	public double getNydmj() {
		return nydmj;
	}
	public void setNydmj(double nydmj) {
		this.nydmj = StringHelper.getDouble(StringHelper.cut(nydmj, 4));
	}
	public double getGdmj() {
		return gdmj;
	}
	public void setGdmj(double gdmj) {
		this.gdmj = StringHelper.getDouble(StringHelper.cut(gdmj, 4));
	}
	public double getLdmj() {
		return ldmj;
	}
	public void setLdmj(double ldmj) {
		this.ldmj = StringHelper.getDouble(StringHelper.cut(ldmj, 4));
	}
	public double getCdmj() {
		return cdmj;
	}
	public void setCdmj(double cdmj) {
		this.cdmj =StringHelper.getDouble(StringHelper.cut(cdmj, 4));
	}
	public double getQtnydmj() {
		return qtnydmj;
	}
	public void setQtnydmj(double qtnydmj) {
		this.qtnydmj = StringHelper.getDouble(StringHelper.cut(qtnydmj, 4));
	}
	public double getJsydmj() {
		return jsydmj;
	}
	public void setJsydmj(double jsydmj) {
		this.jsydmj =StringHelper.getDouble(StringHelper.cut(jsydmj, 4));
	}
	public double getWlydmj() {
		return wlydmj;
	}
	public void setWlydmj(double wlydmj) {
		this.wlydmj = StringHelper.getDouble(StringHelper.cut(wlydmj, 4));
	}
	public String getBdcqzh() {
		return bdcqzh;
	}
	public void setBdcqzh(String bdcqzh) {
		this.bdcqzh = bdcqzh;
	}
	public String getQxdm() {
		return qxdm;
	}
	public void setQxdm(String qxdm) {
		this.qxdm = qxdm;
	}
	public String getDjjg() {
		return djjg;
	}
	public void setDjjg(String djjg) {
		this.djjg = djjg;
	}
	public String getDbr() {
		return dbr;
	}
	public void setDbr(String dbr) {
		this.dbr = dbr;
	}
	public String getDjsj() {
		return djsj;
	}
	public void setDjsj(String djsj) {
		this.djsj = djsj;
	}
	public String getFj() {
		return fj;
	}
	public void setFj(String fj) {
		this.fj = fj;
	}
	public String getQszt() {
		return qszt;
	}
	public void setQszt(String qszt) {
		this.qszt = qszt;
	}
	
	public String getQlqssj() {
		return qlqssj;
	}
	public void setQlqssj(String qlqssj) {
		this.qlqssj = qlqssj;
	}
	public String getQljssj() {
		return qljssj;
	}
	public void setQljssj(String qljssj) {
		this.qljssj = qljssj;
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
		if(StringHelper.isEmpty(this.bdcqzh)){
			this.bdcqzh=null;
		}
		if(StringHelper.isEmpty(this.dbr)){
			this.dbr=null;
		}
		if(StringHelper.isEmpty(this.djjg)){
			this.djjg=null;
		}
		if(StringHelper.isEmpty(this.djlx)){
			this.djlx=null;
		}
		if(StringHelper.isEmpty(this.djsj)){
			this.djsj=null;
		}
		if(StringHelper.isEmpty(this.djyy)){
			this.djyy=null;
		}
		if(StringHelper.isEmpty(this.fj)){
			this.fj=null;
		}
		if(StringHelper.isEmpty(this.mjdw)){
			this.mjdw=null;
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
		if(StringHelper.isEmpty(this.zddm)){
			this.zddm=null;
		}
	}
}
