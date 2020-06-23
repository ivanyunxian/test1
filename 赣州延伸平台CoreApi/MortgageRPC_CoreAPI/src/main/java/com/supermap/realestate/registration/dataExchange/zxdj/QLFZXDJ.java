package com.supermap.realestate.registration.dataExchange.zxdj;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import com.supermap.realestate.registration.util.StringHelper;

/**
 * 注销登记
 * @author diaoliwei
 * @date 2015年9月17日 下午8:11:12
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "QLF_ZXDJ")
public class QLFZXDJ {

	 @XmlAttribute(name = "YSDM", required = true)
	 private String ysdm;
	 @XmlAttribute(name = "BDCDYH", required = true)
	 private String bdcdyh;
	 @XmlAttribute(name = "BDCQZH")
	 private String bdcqzh;
	 @XmlAttribute(name = "YWH")
	 private String ywh;
	 @XmlAttribute(name = "ZXYWH", required = true)
	 private String zxywh;
	 @XmlAttribute(name = "ZXSJ", required = true)
	 private String zxsj;
	 @XmlAttribute(name = "QXDM", required = true)
	 private String qxdm;
	 @XmlAttribute(name = "DJJG", required = true)
	 private String djjg;
	 @XmlAttribute(name = "DBR", required = true)
	 private String dbr;
	 @XmlAttribute(name = "DJSJ", required = true)
	 private String djsj;
	 @XmlAttribute(name = "BZ")
	 private String bz;
	 
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
	public String getBdcqzh() {
		return bdcqzh;
	}
	public void setBdcqzh(String bdcqzh) {
		this.bdcqzh = bdcqzh;
	}
	public String getYwh() {
		return ywh;
	}
	public void setYwh(String ywh) {
		this.ywh = ywh;
	}
	public String getZxywh() {
		return zxywh;
	}
	public void setZxywh(String zxywh) {
		this.zxywh = zxywh;
	}
	public String getZxsj() {
		return zxsj;
	}
	public void setZxsj(String zxsj) {
		this.zxsj = zxsj;
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
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
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
		if(StringHelper.isEmpty(this.bz)){
			this.bz=null;
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
		if(StringHelper.isEmpty(this.qxdm)){
			this.qxdm=null;
		}
		if(StringHelper.isEmpty(this.ysdm)){
			this.ysdm=null;
		}
		if(StringHelper.isEmpty(this.ywh)){
			this.ywh=null;
		}
		if(StringHelper.isEmpty(this.zxsj)){
			this.zxsj=null;
		}
		if(StringHelper.isEmpty(this.zxywh)){
			this.zxywh=null;
		}
	}
}
