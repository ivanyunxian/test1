package com.supermap.realestate.registration.dataExchange.fwsyq;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.StringHelper;

/**
 * 
 * @Description:自然幢_户
 * @author DIAOLIWEI
 * @date 2015年8月28日 下午9:01:32
 * @Copyright SuperMap
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "KTT_FW_H")
public class KTTFWH {
	
	@XmlAttribute(name = "ZRZH")
    protected String zrzh;
    @XmlAttribute(name = "CH")
    protected String ch;
    @XmlAttribute(name = "ZT", required = true)
    protected String zt;
    @XmlAttribute(name = "FCFHT")
    protected String fcfht;
    @XmlAttribute(name = "YSDM", required = true)
    protected String ysdm;
    @XmlAttribute(name = "FWXZ")
    protected String fwxz;
    @XmlAttribute(name = "BDCDYH", required = true)
    protected String bdcdyh;
    @XmlAttribute(name = "FWLX")
    protected String fwlx;
    @XmlAttribute(name = "DYTDMJ")
    protected double dytdmj;
    @XmlAttribute(name = "FTTDMJ")
    protected double fttdmj;
    @XmlAttribute(name = "QXDM", required = true)
    protected String qxdm;
    @XmlAttribute(name = "GYTDMJ")
    protected double gytdmj;
    @XmlAttribute(name = "SCFTXS")
    protected double scftxs;
    @XmlAttribute(name = "SCQTJZMJ")
    protected double scqtjzmj;
    @XmlAttribute(name = "SCDXBFJZMJ")
    protected double scdxbfjzmj;
    @XmlAttribute(name = "SCFTJZMJ")
    protected double scftjzmj;
    @XmlAttribute(name = "SCTNJZMJ")
    protected double sctnjzmj;
    @XmlAttribute(name = "SCJZMJ")
    protected double scjzmj;
    @XmlAttribute(name = "YCFTXS")
    protected double ycftxs;
    @XmlAttribute(name = "YCQTJZMJ")
    protected double ycqtjzmj;
    @XmlAttribute(name = "YCDXBFJZMJ")
    protected double ycdxbfjzmj;
    @XmlAttribute(name = "YCFTJZMJ")
    protected double ycftjzmj;
    @XmlAttribute(name = "YCTNJZMJ")
    protected double yctnjzmj;
    @XmlAttribute(name = "YCJZMJ")
    protected double ycjzmj;
    @XmlAttribute(name = "FWYT3")
    protected String fwyt3;
    @XmlAttribute(name = "FWYT2")
    protected String fwyt2;
    @XmlAttribute(name = "FWYT1")
    protected String fwyt1;
    @XmlAttribute(name = "HXJG")
    protected String hxjg;
    @XmlAttribute(name = "HX")
    protected String hx;
    @XmlAttribute(name = "SHBW", required = true)
    protected String shbw;
    @XmlAttribute(name = "HH")
    protected String hh;
    @XmlAttribute(name = "SJCS")
    protected double sjcs;
    @XmlAttribute(name = "MJDW", required = true)
    protected String mjdw;
    @XmlAttribute(name = "ZL", required = true)
    protected String zl;
    @XmlAttribute(name = "LJZH")
    protected String ljzh;
    @XmlAttribute(name = "FWBM", required = true)
    protected String fwbm;
    @XmlAttribute(name = "BDCDYID")
    protected String bdcdyid;
    @XmlAttribute(name = "ZDBDCDYID")
    protected String zdbdcdyid;
    @XmlAttribute(name = "ZRZBDCDYID")
    protected String zrzbdcdyid;
    
    
	public String getZrzh() {
		return zrzh;
	}
	public void setZrzh(String zrzh) {
		this.zrzh = zrzh;
	}
	public String getCh() {
		return ch;
	}
	public void setCh(String ch) {
		this.ch = ch;
	}
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
	public String getFcfht() {
		return fcfht;
	}
	public void setFcfht(String fcfht) {
		this.fcfht = fcfht;
	}
	public String getYsdm() {
		return ysdm;
	}
	public void setYsdm(String ysdm) {
		this.ysdm = ysdm;
	}
	public String getFwxz() {
		return fwxz;
	}
	public void setFwxz(String fwxz) {
		this.fwxz = fwxz;
		this.fwxz=ConstHelper.getReportValueByValue("FWXZ", fwxz);
	}
	public String getBdcdyh() {
		return bdcdyh;
	}
	public void setBdcdyh(String bdcdyh) {
		this.bdcdyh = bdcdyh;
	}
	public String getFwlx() {
		return fwlx;
	}
	public void setFwlx(String fwlx) {
		this.fwlx = fwlx;
		this.fwlx=ConstHelper.getReportValueByValue("FWLX", fwlx);
	}
	public double getDytdmj() {
		return dytdmj;
	}
	public void setDytdmj(double dytdmj) {
		this.dytdmj = StringHelper.getDouble(StringHelper.cut(dytdmj, 3));
	}
	public double getFttdmj() {
		return fttdmj;
	}
	public void setFttdmj(double fttdmj) {
		this.fttdmj = StringHelper.getDouble(StringHelper.cut(fttdmj, 3));
	}
	public String getQxdm() {
		return qxdm;
	}
	public void setQxdm(String qxdm) {
		this.qxdm = qxdm;
	}
	public double getGytdmj() {
		return gytdmj;
	}
	public void setGytdmj(double gytdmj) {
		this.gytdmj = StringHelper.getDouble(StringHelper.cut(gytdmj, 3));
	}
	public double getScftxs() {
		return scftxs;
	}
	public void setScftxs(double scftxs) {
		this.scftxs = scftxs;
	}
	public double getScqtjzmj() {
		return scqtjzmj;
	}
	public void setScqtjzmj(double scqtjzmj) {
		this.scqtjzmj = StringHelper.getDouble(StringHelper.cut(scqtjzmj, 3));
	}
	public double getScdxbfjzmj() {
		return scdxbfjzmj;
	}
	public void setScdxbfjzmj(double scdxbfjzmj) {
		this.scdxbfjzmj = StringHelper.getDouble(StringHelper.cut(scdxbfjzmj, 3));
	}
	public double getScftjzmj() {
		return scftjzmj;
	}
	public void setScftjzmj(double scftjzmj) {
		this.scftjzmj = StringHelper.getDouble(StringHelper.cut(scftjzmj, 3));
	}
	public double getSctnjzmj() {
		return sctnjzmj;
	}
	public void setSctnjzmj(double sctnjzmj) {
		this.sctnjzmj = StringHelper.getDouble(StringHelper.cut(sctnjzmj, 3));
	}
	public double getScjzmj() {
		return scjzmj;
	}
	public void setScjzmj(double scjzmj) {
		this.scjzmj = StringHelper.getDouble(StringHelper.cut(scjzmj, 3));
	}
	public double getYcftxs() {
		return ycftxs;
	}
	public void setYcftxs(double ycftxs) {
		this.ycftxs = ycftxs;
	}
	public double getYcqtjzmj() {
		return ycqtjzmj;
	}
	public void setYcqtjzmj(double ycqtjzmj) {
		this.ycqtjzmj = StringHelper.getDouble(StringHelper.cut(scjzmj, 2));
	}
	public double getYcdxbfjzmj() {
		return ycdxbfjzmj;
	}
	public void setYcdxbfjzmj(double ycdxbfjzmj) {
		this.ycdxbfjzmj = StringHelper.getDouble(StringHelper.cut(ycdxbfjzmj, 2));
	}
	public double getYcftjzmj() {
		return ycftjzmj;
	}
	public void setYcftjzmj(double ycftjzmj) {
		this.ycftjzmj = StringHelper.getDouble(StringHelper.cut(ycftjzmj, 2));
	}
	public double getYctnjzmj() {
		return yctnjzmj;
	}
	public void setYctnjzmj(double yctnjzmj) {
		this.yctnjzmj = yctnjzmj;
	}
	public double getYcjzmj() {
		return ycjzmj;
	}
	public void setYcjzmj(double ycjzmj) {
		this.ycjzmj = StringHelper.getDouble(StringHelper.cut(ycjzmj, 2));
	}
	public String getFwyt3() {
		return fwyt3;
	}
	public void setFwyt3(String fwyt3) {
		this.fwyt3 = fwyt3;
		this.fwyt3=ConstHelper.getReportValueByValue("FWYT", fwyt3);
	}
	public String getFwyt2() {
		return fwyt2;
	}
	public void setFwyt2(String fwyt2) {
		this.fwyt2 = fwyt2;
		this.fwyt2=ConstHelper.getReportValueByValue("FWYT", fwyt2);
	}
	public String getFwyt1() {
		return fwyt1;
	}
	public void setFwyt1(String fwyt1) {
		this.fwyt1 = fwyt1;
		this.fwyt1=ConstHelper.getReportValueByValue("FWYT", fwyt1);
	}
	public String getHxjg() {
		return hxjg;
	}
	public void setHxjg(String hxjg) {
		this.hxjg = hxjg;
		this.hxjg=ConstHelper.getReportValueByValue("HXJG", hxjg);
	}
	public String getHx() {
		return hx;
	}
	public void setHx(String hx) {
		this.hx = hx;
		this.hx=ConstHelper.getReportValueByValue("HX", hx);
	}
	public String getShbw() {
		return shbw;
	}
	public void setShbw(String shbw) {
		this.shbw = shbw;
	}
	public String getHh() {
		return hh;
	}
	public void setHh(String hh) {
		this.hh = hh;
	}
	public double getSjcs() {
		return sjcs;
	}
	public void setSjcs(double sjcs) {
		this.sjcs = sjcs;
	}
	public String getMjdw() {
		return mjdw;
	}
	public void setMjdw(String mjdw) {
		this.mjdw = mjdw;
		this.mjdw=ConstHelper.getReportValueByValue("MJDW", mjdw);
	}
	public String getZl() {
		return zl;
	}
	public void setZl(String zl) {
		this.zl = zl;
	}
	public String getLjzh() {
		return ljzh;
	}
	public void setLjzh(String ljzh) {
		this.ljzh = ljzh;
	}
	public String getFwbm() {
		return fwbm;
	}
	public void setFwbm(String fwbm) {
		this.fwbm = fwbm;
	}
	
	public String getBdcdyid() {
		return bdcdyid;
	}
	public void setBdcdyid(String bdcdyid) {
		this.bdcdyid = bdcdyid;
	}
	public String getZdbdcdyid() {
		return zdbdcdyid;
	}
	public void setZdbdcdyid(String zdbdcdyid) {
		this.zdbdcdyid = zdbdcdyid;
	}
	public String getZrzbdcdyid() {
		return zrzbdcdyid;
	}
	public void setZrzbdcdyid(String zrzbdcdyid) {
		this.zrzbdcdyid = zrzbdcdyid;
	}
     
	public void replaceEmpty(){
		if(StringHelper.isEmpty(this.bdcdyh)){
			this.bdcdyh=null;
		}
		if(StringHelper.isEmpty(this.ch)){
			this.ch=null;
		}
		if(StringHelper.isEmpty(this.fcfht)){
			this.fcfht=null;
		}
		if(StringHelper.isEmpty(this.fwbm)){
			this.fwbm=null;
		}
		if(StringHelper.isEmpty(this.fwlx)){
			this.fwlx=null;
		}
		if(StringHelper.isEmpty(this.fwxz)){
			this.fwxz=null;
		}
		if(StringHelper.isEmpty(this.fwyt1)){
			this.fwyt1=null;
		}
		if(StringHelper.isEmpty(this.fwyt2)){
			this.fwyt2=null;
		}
		if(StringHelper.isEmpty(this.fwyt3)){
			this.fwyt3=null;
		}
		if(StringHelper.isEmpty(this.hh)){
			this.hh=null;
		}
		if(StringHelper.isEmpty(this.hx)){
			this.hx=null;
		}
		if(StringHelper.isEmpty(this.hxjg)){
			this.hxjg=null;
		}
		if(StringHelper.isEmpty(this.ljzh)){
			this.ljzh=null;
		}
		if(StringHelper.isEmpty(this.mjdw)){
			this.mjdw=null;
		}
		if(StringHelper.isEmpty(this.qxdm)){
			this.qxdm=null;
		}
		if(StringHelper.isEmpty(this.shbw)){
			this.shbw=null;
		}
		if(StringHelper.isEmpty(this.ysdm)){
			this.ysdm=null;
		}
		if(StringHelper.isEmpty(this.zl)){
			this.zl=null;
		}
		if(StringHelper.isEmpty(this.zrzh)){
			this.zrzh=null;
		}
		if(StringHelper.isEmpty(this.zt)){
			this.zt=null;
		}
	}
}
