package com.supermap.realestate.registration.dataExchange.yydj;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import com.supermap.realestate.registration.util.StringHelper;

/**
 * 
 * @Description:异议登记表
 * @author diaoliwei
 * @date 2015年9月15日 下午3:11:12
 * @Copyright SuperMap
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "QLF_QL_YYDJ")
public class QLFQLYYDJ {
	
	 @XmlAttribute(name = "YSDM", required = true)
	 private String ysdm;
	 @XmlAttribute(name = "BDCDYH", required = true)
	 private String bdcdyh;
	 @XmlAttribute(name = "YWH")
	 private String ywh;
	 @XmlAttribute(name = "YYSX", required = true)
	 private String yysx;
	 @XmlAttribute(name = "ZXYYYWH")
	 private String zxyyywh;
	 @XmlAttribute(name = "ZXYYYY")
	 private String zxyyyy;
	 @XmlAttribute(name = "ZXYYDBR")
	 private String zxyydbr;
	 @XmlAttribute(name = "ZXYYDJSJ")
	 private String zxyydjsj;
	 @XmlAttribute(name = "BDCDJZMH", required = true)
	 private String bdcdjzmh;
	 @XmlAttribute(name = "QXDM", required = true)
	 private String qxdm;
	 @XmlAttribute(name = "DJJG", required = true)
	 private String djjg;
	 @XmlAttribute(name = "DBR", required = true)
	 private String dbr;
	 @XmlAttribute(name = "DJSJ", required = true)
	 private String djsj;
	 @XmlAttribute(name = "FJ")
	 private String fj;
	 @XmlAttribute(name = "QSZT", required = true)
	 private String qszt;
	 
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
	public String getBdcdjzmh() {
		return bdcdjzmh;
	}
	public void setBdcdjzmh(String bdcdjzmh) {
		this.bdcdjzmh = bdcdjzmh;
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
	public String getYysx() {
		return yysx;
	}
	public void setYysx(String yysx) {
		this.yysx = yysx;
	}
	public String getZxyyywh() {
		return zxyyywh;
	}
	public void setZxyyywh(String zxyyywh) {
		this.zxyyywh = zxyyywh;
	}
	public String getZxyyyy() {
		return zxyyyy;
	}
	public void setZxyyyy(String zxyyyy) {
		this.zxyyyy = zxyyyy;
	}
	public String getZxyydbr() {
		return zxyydbr;
	}
	public void setZxyydbr(String zxyydbr) {
		this.zxyydbr = zxyydbr;
	}
	public String getZxyydjsj() {
		return zxyydjsj;
	}
	public void setZxyydjsj(String zxyydjsj) {
		this.zxyydjsj = zxyydjsj;
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
		if(StringHelper.isEmpty(this.bdcdjzmh)){
			this.bdcdjzmh=null;
		}
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
		if(StringHelper.isEmpty(this.yysx)){
			this.yysx=null;
		}
		if(StringHelper.isEmpty(this.zxyydbr)){
			this.zxyydbr=null;
		}
		if(StringHelper.isEmpty(this.zxyydjsj)){
			this.zxyydjsj=null;
		}
		if(StringHelper.isEmpty(this.zxyyywh)){
			this.zxyyywh=null;
		}
		if(StringHelper.isEmpty(this.zxyyyy)){
			this.zxyyyy=null;
		}
	}
}
