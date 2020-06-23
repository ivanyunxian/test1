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
 * @Description:房地产权_多幢表_项目属性表
 * @author DIAOLIWEI
 * @date 2015年8月28日 下午10:22:30
 * @Copyright SuperMap
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "QLF_FW_FDCQ_DZ_XM")
public class QLFFWFDCQDZXM {
	
	@XmlAttribute(name = "BDCDYH", required = true)
    protected String bdcdyh;
	@XmlAttribute(name = "QXDM", required = true)
    protected String qxdm;
	@XmlAttribute(name = "ZTS")
    protected int zts;
	@XmlAttribute(name = "FWJG")
    protected String fwjg;
	@XmlAttribute(name = "GHYT")
    protected String ghyt;
	@XmlAttribute(name = "ZCS")
    protected int zcs;
	@XmlAttribute(name = "XMMC", required = true)
    protected String xmmc;
	@XmlAttribute(name = "JGSJ")
    protected String jgsj;
	@XmlAttribute(name = "JZMJ")
    protected double jzmj;
	@XmlAttribute(name = "ZH", required = true)
	protected String zh;
	
	@XmlAttribute(name = "QLID")
    protected String qlid;
    @XmlAttribute(name = "XMBH")
    protected String xmbh;
    @XmlAttribute(name = "BDCDYID")
    protected String bdcdyid;

	
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
	public int getZts() {
		return zts;
	}
	public void setZts(int zts) {
		this.zts = zts;
	}
	public String getFwjg() {
		return fwjg;
	}
	public void setFwjg(String fwjg) {
		this.fwjg = fwjg;
		this.fwjg=ConstHelper.getReportValueByValue("FWJG", fwjg);
	}
	public String getGhyt() {
		return ghyt;
	}
	public void setGhyt(String ghyt) {
		this.ghyt = ghyt;
		this.ghyt=ConstHelper.getReportValueByValue("FWYT", ghyt);
	}
	public int getZcs() {
		return zcs;
	}
	public void setZcs(int zcs) {
		this.zcs = zcs;
	}
	public String getXmmc() {
		return xmmc;
	}
	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}
	public String getJgsj() {
		return jgsj;
	}
	public void setJgsj(String jgsj) {
		this.jgsj = jgsj;
	}
	public double getJzmj() {
		return jzmj;
	}
	public void setJzmj(double jzmj) {
		this.jzmj = StringHelper.getDouble(StringHelper.cut(jzmj, 2));
	}
	public String getZh() {
		return zh ;
	}
	public void setZh(String zh) {
		this.zh = zh;
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
		if(StringHelper.isEmpty(this.fwjg)){
			this.fwjg=null;
		}
		if(StringHelper.isEmpty(this.ghyt)){
			this.ghyt=null;
		}
		if(StringHelper.isEmpty(this.jgsj)){
			this.jgsj=null;
		}
		if(StringHelper.isEmpty(this.qxdm)){
			this.qxdm=null;
		}
		if(StringHelper.isEmpty(this.xmmc)){
			this.xmmc=null;
		}
		if(StringHelper.isEmpty(this.zh)){
			this.zh=null;
		}
	}
}
