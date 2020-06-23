package com.supermap.realestate.registration.dataExchange.shyq;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.StringHelper;

/**
 * 
 * @Description:界址线
 * @author diaoliwei
 * @date 2015年10月17日 下午8:38:01
 * @Copyright SuperMap
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "KTT_GY_JZX")
public class KTTGYJZX {
	
	@XmlAttribute(name = "BSM", required = true)
	protected int bsm;
	@XmlAttribute(name = "ZDZHDM", required = true)
	protected String zdzhdm;
	@XmlAttribute(name = "YSDM", required = true)
	protected String ysdm;
    @XmlAttribute(name = "JZXCD", required = true)
    protected double jzxcd;
    @XmlAttribute(name = "JZXLB", required = true)
    protected String jzxlb;
    @XmlAttribute(name = "JZXWZ", required = true)
    protected String jzxwz;
	@XmlAttribute(name = "JXXZ", required = true)
	protected String jxxz;
	@XmlAttribute(name = "QSJXXYSBH")
	protected String qsjxxysbh;
	@XmlAttribute(name = "QSJXXYS")
	protected String qsjxxys;
	@XmlAttribute(name = "QSZYYYSBH")
	protected String qszyyysbh;
	@XmlAttribute(name = "QSZYYYS")
	protected String qszyyys;
	
	
	public String getZdzhdm() {
		return zdzhdm;
	}
	public void setZdzhdm(String zdzhdm) {
		this.zdzhdm = zdzhdm;
	}
	public int getBsm() {
		return bsm;
	}
	public void setBsm(int bsm) {
		this.bsm = bsm;
	}
	public String getYsdm() {
		return ysdm;
	}
	public void setYsdm(String ysdm) {
		this.ysdm = ysdm;
	}
	public double getJzxcd() {
		return jzxcd;
	}
	public void setJzxcd(double jzxcd) {
		this.jzxcd = jzxcd;
	}
	public String getJzxlb() {
		return jzxlb;
	}
	public void setJzxlb(String jzxlb) {
		this.jzxlb = jzxlb;
		this.jzxlb=ConstHelper.getReportValueByValue("JZXLB", jzxlb);
	}
	public String getJxxz() {
		return jxxz;
	}
	public void setJxxz(String jxxz) {
		this.jxxz = jxxz;
		this.jxxz=ConstHelper.getReportValueByValue("JXXZ", jxxz);
	}
	public String getQsjxxysbh() {
		return qsjxxysbh;
	}
	public void setQsjxxysbh(String qsjxxysbh) {
		this.qsjxxysbh = qsjxxysbh;
	}
	public String getQsjxxys() {
		return qsjxxys;
	}
	public void setQsjxxys(String qsjxxys) {
		this.qsjxxys = qsjxxys;
	}
	public String getQszyyysbh() {
		return qszyyysbh;
	}
	public void setQszyyysbh(String qszyyysbh) {
		this.qszyyysbh = qszyyysbh;
	}
	public String getQszyyys() {
		return qszyyys;
	}
	public void setQszyyys(String qszyyys) {
		this.qszyyys = qszyyys;
	}
	public String getJzxwz() {
		return jzxwz;
	}
	public void setJzxwz(String jzxwz) {
		this.jzxwz = jzxwz;
		this.jzxwz=ConstHelper.getReportValueByValue("JZXWZ", jzxwz);
	}
	public void replaceEmpty(){
		if(StringHelper.isEmpty(this.jxxz)){
			this.jxxz=null;
		}
		if(StringHelper.isEmpty(this.jzxlb)){
			this.jzxlb=null;
		}
		if(StringHelper.isEmpty(this.jzxwz)){
			this.jzxwz=null;
		}
		if(StringHelper.isEmpty(this.qsjxxys)){
			this.qsjxxys=null;
		}
		if(StringHelper.isEmpty(this.qsjxxysbh)){
			this.qsjxxysbh=null;
		}
		if(StringHelper.isEmpty(this.qszyyys)){
			this.qszyyys=null;
		}
		if(StringHelper.isEmpty(this.qszyyysbh)){
			this.qszyyysbh=null;
		}
	}

}
