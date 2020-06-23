package com.supermap.realestate.registration.dataExchange.lq;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.StringHelper;

/**
 * 
 * @Description:林权表
 * @author diaoliwei
 * @date 2015年9月21日 下午8:53:46
 * @Copyright SuperMap
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "QLT_QL_LQ")
public class QLTQLLQ {
	
	 @XmlAttribute(name = "YSDM", required = true)
	 private String ysdm;
	 @XmlAttribute(name = "BDCDYH")
	 private String bdcdyh;
	 @XmlAttribute(name = "YWH")
	 private String ywh;
	 @XmlAttribute(name = "QLLX", required = true)
	 private String qllx;
	 @XmlAttribute(name = "DJLX", required = true)
	 private String djlx;
	 @XmlAttribute(name = "DJYY", required = true)
	 private String djyy;
	 @XmlAttribute(name = "FBF", required = true)
	 private String fbf;
	 @XmlAttribute(name = "SYQMJ", required = true)
	 private BigDecimal syqmj;
	 @XmlAttribute(name = "LDSYQSSJ", required = true)
	 private String ldsyqssj;
	 @XmlAttribute(name = "LDSYJSSJ", required = true)
	 private String ldsyjssj;
	 @XmlAttribute(name = "LDSYQXZ")
	 private String ldsyqxz;
	 @XmlAttribute(name = "SLLMSYQR1")
	 private String sllmsyqr1;
	 @XmlAttribute(name = "SLLMSYQR2")
	 private String sllmsyqr2;
	 @XmlAttribute(name = "ZYSZ", required = true)
	 private String zysz;
	 @XmlAttribute(name = "ZS", required = true)
	 private int zs;
	 @XmlAttribute(name = "LZ", required = true)
	 private String lz;
	 @XmlAttribute(name = "QY", required = true)
	 private String qy;
	 @XmlAttribute(name = "ZLND", required = true)
	 private int zlnd;
	 @XmlAttribute(name = "LB", required = true)
	 private String lb;
	 @XmlAttribute(name = "XB", required = true)
	 private String xb;
	 @XmlAttribute(name = "XDM")
	 private String xdm;
	 @XmlAttribute(name = "BDCQZH", required = true)
	 private String bdcqzh;
	 @XmlAttribute(name = "QXDM", required = true)
	 private String qxdm;
	 @XmlAttribute(name = "DJJG")
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
	public String getFbf() {
		return fbf;
	}
	public void setFbf(String fbf) {
		this.fbf = fbf;
	}
	public BigDecimal getSyqmj() {
		return syqmj;
	}
	public void setSyqmj(BigDecimal syqmj) {
		this.syqmj = StringHelper.cutBigDecimal(syqmj, 4);//林权下的
	}
	public String getLdsyqssj() {
		return ldsyqssj;
	}
	public void setLdsyqssj(String ldsyqssj) {
		this.ldsyqssj = ldsyqssj;
	}
	public String getLdsyjssj() {
		return ldsyjssj;
	}
	public void setLdsyjssj(String ldsyjssj) {
		this.ldsyjssj = ldsyjssj;
	}
	public String getLdsyqxz() {
		return ldsyqxz;
	}
	public void setLdsyqxz(String ldsyqxz) {
		this.ldsyqxz = ldsyqxz;
		this.ldsyqxz=ConstHelper.getReportValueByValue("TDSYQXZ", ldsyqxz);
	}
	public String getSllmsyqr1() {
		return sllmsyqr1;
	}
	public void setSllmsyqr1(String sllmsyqr1) {
		this.sllmsyqr1 = sllmsyqr1;
	}
	public String getSllmsyqr2() {
		return sllmsyqr2;
	}
	public void setSllmsyqr2(String sllmsyqr2) {
		this.sllmsyqr2 = sllmsyqr2;
	}
	public String getZysz() {
		return zysz;
	}
	public void setZysz(String zysz) {
		this.zysz = zysz;
	}
	public int getZs() {
		return zs;
	}
	public void setZs(int zs) {
		this.zs = zs;
	}
	public String getLz() {
		return lz;
	}
	public void setLz(String lz) {
		this.lz = lz;
		this.lz=ConstHelper.getReportValueByValue("LZ", lz);
	}
	public String getQy() {
		return qy;
	}
	public void setQy(String qy) {
		this.qy = qy;
		this.qy=ConstHelper.getReportValueByValue("QY", qy);
	}
	public int getZlnd() {
		return zlnd;
	}
	public void setZlnd(int zlnd) {
		this.zlnd = zlnd;
	}
	public String getLb() {
		return lb;
	}
	public void setLb(String lb) {
		this.lb = lb;
	}
	public String getXb() {
		return xb;
	}
	public void setXb(String xb) {
		this.xb = xb;
	}
	public String getXdm() {
		return xdm;
	}
	public void setXdm(String xdm) {
		this.xdm = xdm;
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
			this.djsj=null;
		}
		if(StringHelper.isEmpty(this.djyy)){
			this.djyy=null;
		}
		if(StringHelper.isEmpty(this.fbf)){
			this.fbf=null;
		}
		if(StringHelper.isEmpty(this.fj)){
			this.fj=null;
		}
		if(StringHelper.isEmpty(this.lb)){
			this.lb=null;
		}
		if(StringHelper.isEmpty(this.ldsyjssj)){
			this.ldsyjssj=null;
		}
		if(StringHelper.isEmpty(this.ldsyqssj)){
			this.ldsyqssj=null;
		}
		if(StringHelper.isEmpty(this.ldsyqxz)){
			this.ldsyqxz=null;
		}
		if(StringHelper.isEmpty(this.lz)){
			this.lz=null;
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
		if(StringHelper.isEmpty(this.qy)){
			this.qy=null;
		}
		if(StringHelper.isEmpty(this.sllmsyqr1)){
			this.sllmsyqr1=null;
		}
		if(StringHelper.isEmpty(this.sllmsyqr2)){
			this.sllmsyqr2=null;
		}
		if(StringHelper.isEmpty(this.xb)){
			this.xb=null;
		}
		if(StringHelper.isEmpty(this.xdm)){
			this.xdm=null;
		}
		if(StringHelper.isEmpty(this.ysdm)){
			this.ysdm=null;
		}
		if(StringHelper.isEmpty(this.ywh)){
			this.ywh=null;
		}
		if(StringHelper.isEmpty(this.zysz)){
			this.zysz=null;
		}
	}
}
