package com.supermap.realestate.registration.dataExchange.fwsyq;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.supermap.realestate.registration.util.StringHelper;

/**
 * 
 * @Description:自然幢_层表
 * @author DIAOLIWEI
 * @date 2015年8月28日 下午8:44:59
 * @Copyright SuperMap
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "KTT_FW_C")
public class KTTFWC {

	@XmlAttribute(name = "QXDM", required = true)
	protected String qxdm;
	@XmlAttribute(name = "YSDM", required = true)
	protected String ysdm;
    @XmlAttribute(name = "SPTYMJ")
    protected double sptymj;
    @XmlAttribute(name = "CG")
    protected double cg;
	@XmlAttribute(name = "CBQMJ")
	protected double cbqmj;
	@XmlAttribute(name = "CFTJZMJ")
	protected double cftjzmj;
	@XmlAttribute(name = "CGYJZMJ")
	protected double cgyjzmj;
	@XmlAttribute(name = "CYTMJ")
	protected double cytmj;
	@XmlAttribute(name = "CTNJZMJ")
	protected double ctnjzmj;
	@XmlAttribute(name = "CJZMJ")
	protected double cjzmj;
	@XmlAttribute(name = "MYC")
	protected String myc;
	@XmlAttribute(name = "SJC")
	protected String sjc;
	@XmlAttribute(name = "ZRZH", required = true)
	protected String zrzh;
	@XmlAttribute(name = "CH", required = true)
	protected String ch;
	
	
	public String getQxdm() {
		return qxdm;
	}
	public void setQxdm(String qxdm) {
		this.qxdm = qxdm;
	}
	public String getYsdm() {
		return ysdm;
	}
	public void setYsdm(String ysdm) {
		this.ysdm = ysdm;
	}
	public double getSptymj() {
		return sptymj;
	}
	public void setSptymj(double sptymj) {
		this.sptymj = StringHelper.getDouble(StringHelper.cut(sptymj, 3));
	}
	public double getCg() {
		return cg;
	}
	public void setCg(double cg) {
		this.cg = cg;
	}
	public double getCbqmj() {
		return cbqmj;
	}
	public void setCbqmj(double cbqmj) {
		this.cbqmj = StringHelper.getDouble(StringHelper.cut(cbqmj, 3));
	}
	public double getCftjzmj() {
		return cftjzmj;
	}
	public void setCftjzmj(double cftjzmj) {
		this.cftjzmj = StringHelper.getDouble(StringHelper.cut(cftjzmj, 3));
	}
	public double getCgyjzmj() {
		return cgyjzmj;
	}
	public void setCgyjzmj(double cgyjzmj) {
		this.cgyjzmj =StringHelper.getDouble(StringHelper.cut(cgyjzmj, 3));
	}
	public double getCytmj() {
		return cytmj;
	}
	public void setCytmj(double cytmj) {
		this.cytmj = StringHelper.getDouble(StringHelper.cut(cytmj, 3));
	}
	public double getCtnjzmj() {
		return ctnjzmj;
	}
	public void setCtnjzmj(double ctnjzmj) {
		this.ctnjzmj =StringHelper.getDouble(StringHelper.cut(ctnjzmj, 3));
	}
	public double getCjzmj() {
		return cjzmj;
	}
	public void setCjzmj(double cjzmj) {
		this.cjzmj = StringHelper.getDouble(StringHelper.cut(cjzmj, 3));
	}
	public String getMyc() {
		return myc;
	}
	public void setMyc(String myc) {
		this.myc = myc;
	}
	public String getSjc() {
		return sjc;
	}
	public void setSjc(String sjc) {
		this.sjc = sjc;
	}
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
	public void replaceEmpty(){
		if(StringHelper.isEmpty(this.ch)){
			this.ch=null;
		}
		if(StringHelper.isEmpty(this.myc)){
			this.myc=null;
		}
		if(StringHelper.isEmpty(this.qxdm)){
			this.qxdm=null;
		}
		if(StringHelper.isEmpty(this.sjc)){
			this.sjc=null;
		}
		if(StringHelper.isEmpty(this.ysdm)){
			this.ysdm=null;
		}
		if(StringHelper.isEmpty(this.zrzh)){
			this.zrzh=null;
		}
	}
}
