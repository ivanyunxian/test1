package com.supermap.realestate.registration.dataExchange.ygdj;

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
 * @Description:预告登记表
 * @author diaoliwei
 * @date 2015年9月15日 下午1:55:50
 * @Copyright SuperMap
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "QLF_QL_YGDJ")
public class QLFQLYGDJ {
	
	 @XmlAttribute(name = "YSDM", required = true)
	 private String ysdm;
	 @XmlAttribute(name = "BDCDYH", required = true)
	 private String bdcdyh;
	 @XmlAttribute(name = "YWH")
	 private String ywh;
	 @XmlAttribute(name = "BDCZL", required = true)
	 private String bdczl;
	 @XmlAttribute(name = "YWR", required = true)
	 private String ywr;
	 @XmlAttribute(name = "YWRZJZL")
	 private String ywrzjzl;
	 @XmlAttribute(name = "YWRZJH")
	 private String ywrzjh;
	 @XmlAttribute(name = "YGDJZL", required = true)
	 private String ygdjzl;
	 @XmlAttribute(name = "DJLX", required = true)
	 private String djlx;
	 @XmlAttribute(name = "DJYY", required = true)
	 private String djyy;
	 @XmlAttribute(name = "TDSYQR")
	 private String tdsyqr;
	 @XmlAttribute(name = "GHYT" )
	 private String ghyt;
	 @XmlAttribute(name = "FWXZ")
	 private String fwxz;
	 @XmlAttribute(name = "FWJG")
	 private String fwjg;
	 @XmlAttribute(name = "SZC", required = true)
	 private int szc;
	 @XmlAttribute(name = "ZCS", required = true)
	 private int zcs;
	 @XmlAttribute(name = "JZMJ", required = true)
	 private double jzmj;
	 @XmlAttribute(name = "QDJG")
	 private BigDecimal qdjg;
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
	 @XmlAttribute(name = "SCYWH", required = true)
	 private String scywh;
	 
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
	public String getBdczl() {
		return bdczl;
	}
	public void setBdczl(String bdczl) {
		this.bdczl = bdczl;
	}
	public String getYwr() {
		return ywr;
	}
	public void setYwr(String ywr) {
		this.ywr = ywr;
	}
	public String getYwrzjzl() {
		return ywrzjzl;
	}
	public void setYwrzjzl(String ywrzjzl) {
		this.ywrzjzl = ywrzjzl;
		this.ywrzjzl=ConstHelper.getReportValueByValue("ZJLX", ywrzjzl);
	}
	public String getYwrzjh() {
		return ywrzjh;
	}
	public void setYwrzjh(String ywrzjh) {
		this.ywrzjh = ywrzjh;
	}
	public String getYgdjzl() {
		return ygdjzl;
	}
	public void setYgdjzl(String ygdjzl) {
		this.ygdjzl = ygdjzl;
		this.ygdjzl=ConstHelper.getReportValueByValue("YGDJZL", ygdjzl);
	}
	public String getTdsyqr() {
		return tdsyqr;
	}
	public void setTdsyqr(String tdsyqr) {
		this.tdsyqr = tdsyqr;
	}
	public String getGhyt() {
		return ghyt;
	}
	public void setGhyt(String ghyt) {
		this.ghyt = ghyt;
		this.ghyt=ConstHelper.getReportValueByValue("FWYT", ghyt);
	}
	public String getFwxz() {
		return fwxz;
	}
	public void setFwxz(String fwxz) {
		this.fwxz = fwxz;
		this.fwxz=ConstHelper.getReportValueByValue("FWXZ", fwxz);
	}
	public String getFwjg() {
		return fwjg;
	}
	public void setFwjg(String fwjg) {
		this.fwjg = fwjg;
		this.fwjg=ConstHelper.getReportValueByValue("FWJG", fwjg);
	}
	public int getSzc() {
		return szc;
	}
	public void setSzc(int szc) {
		this.szc = szc;
	}
	public int getZcs() {
		return zcs;
	}
	public void setZcs(int zcs) {
		this.zcs = zcs;
	}
	public double getJzmj() {
		return jzmj;
	}
	public void setJzmj(double jzmj) {
		this.jzmj = StringHelper.getDouble(StringHelper.cut(jzmj, 2));
	}
	public BigDecimal getQdjg() {
		return qdjg;
	}
	public void setQdjg(BigDecimal qdjg) {
		this.qdjg = StringHelper.cutBigDecimal(qdjg, 4);
	}
	public String getScywh() {
		return scywh;
	}
	public void setScywh(String scywh) {
		this.scywh = scywh;
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
		if(StringHelper.isEmpty(this.bdczl)){
			this.bdczl=null;
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
		if(StringHelper.isEmpty(this.fwjg)){
			this.fwjg=null;
		}
		if(StringHelper.isEmpty(this.fwxz)){
			this.fwxz=null;
		}
		if(StringHelper.isEmpty(this.ghyt)){
			this.ghyt=null;
		}
		if(StringHelper.isEmpty(this.qszt)){
			this.qszt=null;
		}
		if(StringHelper.isEmpty(this.qxdm)){
			this.qxdm=null;
		}
		if(StringHelper.isEmpty(this.scywh)){
			this.scywh=null;
		}
		if(StringHelper.isEmpty(this.tdsyqr)){
			this.tdsyqr=null;
		}
		if(StringHelper.isEmpty(this.ygdjzl)){
			this.ygdjzl=null;
		}
		if(StringHelper.isEmpty(this.ysdm)){
			this.ysdm=null;
		}
		if(StringHelper.isEmpty(this.ywh)){
			this.ywh=null;
		}
		if(StringHelper.isEmpty(this.ywr)){
			this.ywr=null;
		}
		if(StringHelper.isEmpty(this.ywrzjh)){
			this.ywrzjh=null;
		}
		if(StringHelper.isEmpty(this.ywrzjzl)){
			this.ywrzjzl=null;
		}
	}
}
