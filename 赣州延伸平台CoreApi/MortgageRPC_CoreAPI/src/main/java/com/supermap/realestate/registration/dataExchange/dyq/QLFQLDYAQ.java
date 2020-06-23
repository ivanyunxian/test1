package com.supermap.realestate.registration.dataExchange.dyq;

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
 * @Description:抵押权表
 * @author diaoliwei
 * @date 2015年9月1日 下午4:55:50
 * @Copyright SuperMap
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "QLF_QL_DYAQ")
public class QLFQLDYAQ {
	
	 @XmlAttribute(name = "YSDM", required = true)
	 private String ysdm;
	 @XmlAttribute(name = "BDCDYH", required = true)
	 private String bdcdyh;
	 @XmlAttribute(name = "YWH")
	 private String ywh;
	 @XmlAttribute(name = "DYBDCLX", required = true)
	 private String dybdclx;
	 @XmlAttribute(name = "DYR", required = true)
	 private String dyr;
	 @XmlAttribute(name = "DYFS", required = true)
	 private String dyfs;
	 @XmlAttribute(name = "DJLX", required = true)
	 private String djlx;
	 @XmlAttribute(name = "DJYY", required = true)
	 private String djyy;
	 @XmlAttribute(name = "ZJJZWZL")
	 private String zjjzwzl;
	 @XmlAttribute(name = "ZJJZWDYFW")
	 private String zjjzwdyfw;
	 @XmlAttribute(name = "BDBZZQSE", required = true)
	 private BigDecimal bdbzzqse;
	 @XmlAttribute(name = "ZWLXQSSJ", required = true)
	 private String zwlxqssj;
	 @XmlAttribute(name = "ZWLXJSSJ", required = true)
	 private String zwlxjssj;
	 @XmlAttribute(name = "ZGZQQDSS")
	 private String zgzqqdss;
	 @XmlAttribute(name = "ZGZQSE")
	 private BigDecimal zgzqse;
	 @XmlAttribute(name = "ZXDYYWH")
	 private String zxdyywh;
	 @XmlAttribute(name = "ZXDYYY")
	 private String zxdyyy;
	 @XmlAttribute(name = "ZXSJ")
	 private String zxsj;
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
	 @XmlAttribute(name = "DYJELX",required = true)
	 private String dyjelx;
	 
	 @XmlAttribute(name = "QLID")
	 private String qlid;
	 @XmlAttribute(name = "XMBH")
	 private String xmbh;
	 @XmlAttribute(name = "BDCDYID")
	 private String bdcdyid;
	 
	 public String getDyjelx() {
		return dyjelx;
	}
	public void setDyjelx(String dyjelx) {
		this.dyjelx = dyjelx;
	}
	public String getScywh() {
			return scywh;
	}
	public void setScywh(String scywh) {
		this.scywh = scywh;
	}
		
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
	public String getDybdclx() {
		return dybdclx;
	}
	public void setDybdclx(String dybdclx) {
		this.dybdclx = dybdclx;
		this.dybdclx=ConstHelper.getReportValueByValue("DYBDCLX", dybdclx);
	}
	public String getDyr() {
		return dyr;
	}
	public void setDyr(String dyr) {
		this.dyr = dyr;
	}
	public String getDyfs() {
		return dyfs;
	}
	public void setDyfs(String dyfs) {
		this.dyfs = dyfs;
		this.dyfs=ConstHelper.getReportValueByValue("DYFS", dyfs);
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
	public String getZjjzwzl() {
		return zjjzwzl;
	}
	public void setZjjzwzl(String zjjzwzl) {
		this.zjjzwzl = zjjzwzl;
	}
	public String getZjjzwdyfw() {
		return zjjzwdyfw;
	}
	public void setZjjzwdyfw(String zjjzwdyfw) {
		this.zjjzwdyfw = zjjzwdyfw;
	}
	public BigDecimal getBdbzzqse() {
		return bdbzzqse;
	}
	public void setBdbzzqse(BigDecimal bdbzzqse) {
		this.bdbzzqse = StringHelper.cutBigDecimal(bdbzzqse, 4);
	}
	public String getZwlxqssj() {
		return zwlxqssj;
	}
	public void setZwlxqssj(String zwlxqssj) {
		this.zwlxqssj = zwlxqssj;
	}
	public String getZwlxjssj() {
		return zwlxjssj;
	}
	public void setZwlxjssj(String zwlxjssj) {
		this.zwlxjssj = zwlxjssj;
	}
	public String getZgzqqdss() {
		return zgzqqdss;
	}
	public void setZgzqqdss(String zgzqqdss) {
		this.zgzqqdss = zgzqqdss;
	}
	public BigDecimal getZgzqse() {
		return zgzqse;
	}
	public void setZgzqse(BigDecimal zgzqse) {
		this.zgzqse = StringHelper.cutBigDecimal(zgzqse, 4);
	}
	public String getZxdyywh() {
		return zxdyywh;
	}
	public void setZxdyywh(String zxdyywh) {
		this.zxdyywh = zxdyywh;
	}
	public String getZxdyyy() {
		return zxdyyy;
	}
	public void setZxdyyy(String zxdyyy) {
		this.zxdyyy = zxdyyy;
	}
	public String getZxsj() {
		return zxsj;
	}
	public void setZxsj(String zxsj) {
		this.zxsj = zxsj;
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
    	if(StringHelper.isEmpty(this.djlx)){
    		this.djlx=null;
    	}
    	if(StringHelper.isEmpty(this.djsj)){
    		this.djsj=null;
    	}
    	if(StringHelper.isEmpty(this.djyy)){
    		this.djyy=null;
    	}
    	if(StringHelper.isEmpty(this.dybdclx)){
    		this.dybdclx=null;
    	}
    	if(StringHelper.isEmpty(this.dyfs)){
    		this.dyfs=null;
    	}
    	if(StringHelper.isEmpty(this.dyjelx)){
    		this.dyjelx=null;
    	}
    	if(StringHelper.isEmpty(this.dyr)){
    		this.dyr=null;
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
    	if(StringHelper.isEmpty(this.scywh)){
    		this.scywh=null;
    	}
    	if(StringHelper.isEmpty(this.ysdm)){
    		this.ysdm=null;
    	}
    	if(StringHelper.isEmpty(this.ywh)){
    		this.ywh=null;
    	}
    	if(StringHelper.isEmpty(this.zgzqqdss)){
    		this.zgzqqdss=null;
    	}
    	if(StringHelper.isEmpty(this.zjjzwdyfw)){
    		this.zjjzwdyfw=null;
    	}
    	if(StringHelper.isEmpty(this.zjjzwzl)){
    		this.zjjzwzl=null;
    	}
    	if(StringHelper.isEmpty(this.zwlxjssj)){
    		this.zwlxjssj=null;
    	}
    	if(StringHelper.isEmpty(this.zwlxqssj)){
    		this.zwlxqssj=null;
    	}
    	if(StringHelper.isEmpty(this.zxdyywh)){
    		this.zxdyywh=null;
    	}
    	if(StringHelper.isEmpty(this.zxdyyy)){
    		this.zxdyyy=null;
    	}
    	if(StringHelper.isEmpty(this.zxsj)){
    		this.zxsj=null;
    	}
    }
}
