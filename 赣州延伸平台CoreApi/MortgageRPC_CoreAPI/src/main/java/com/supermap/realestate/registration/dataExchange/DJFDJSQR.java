package com.supermap.realestate.registration.dataExchange;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.StringHelper;

/**
 * 
 * @Description:申请人属性
 * @author diaoliwei
 * @date 2015年8月28日 下午8:38:01
 * @Copyright SuperMap
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "DJF_DJ_SQR")
public class DJFDJSQR {
	
	@XmlAttribute(name = "QXDM", required = true)
	protected String qxdm;
	@XmlAttribute(name = "YSDM", required = true)
	protected String ysdm;
    @XmlAttribute(name = "YWH", required = true)
    protected String ywh;
    @XmlAttribute(name = "QLRZJZL", required = true)
    protected String qlrzjzl;
    @XmlAttribute(name = "BZ")
    protected String bz;
	@XmlAttribute(name = "QLRYB")
	protected String qlryb;
	@XmlAttribute(name = "QLRZJH")
	protected String qlrzjh;
	@XmlAttribute(name = "QLRDLJG")
	protected String qlrdljg;
	@XmlAttribute(name = "QLRDLRDH")
	protected String qlrdlrdh;
	@XmlAttribute(name = "QLRDLRMC")
	protected String qlrdlrmc;
	@XmlAttribute(name = "QLRFRDH")
	protected String qlrfrdh;
	@XmlAttribute(name = "QLRFRMC")
	protected String qlrfrmc;
	@XmlAttribute(name = "QLRTXDZ")
	protected String qlrtxdz;
	@XmlAttribute(name = "QLRMC", required = true)
	protected String qlrmc;
	@XmlAttribute(name = "YWRMC")
	protected String ywrmc;
	@XmlAttribute(name = "YWRZJZL")
	protected String ywrzjzl;
	@XmlAttribute(name = "YWRZJH")
	protected String ywrzjh;
	@XmlAttribute(name = "YWRTXDZ")
	protected String ywrtxdz;
	@XmlAttribute(name = "YWRYB")
	protected String ywryb;
	@XmlAttribute(name = "YWRFRMC")
	protected String ywrfrmc;
	@XmlAttribute(name = "YWRFRDH")
	protected String ywrfrdh;
	@XmlAttribute(name = "YWRDLRMC")
	protected String ywrdlrmc;
	@XmlAttribute(name = "YWRDLRDH")
	protected String ywrdlrdh;
	@XmlAttribute(name = "YWRDLJG")
	protected String ywrdljg;
	
	@XmlAttribute(name = "SQRLB")
	protected String sqrlb;
	@XmlAttribute(name = "QLRDH")
	protected String qlrdh;
	@XmlAttribute(name = "QLRLX")
	protected String qlrlx;
	@XmlAttribute(name = "SQRID")
	protected String sqrid;
	@XmlAttribute(name = "XMBH")
	protected String xmbh;
	
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
	public String getYwh() {
		return ywh;
	}
	public void setYwh(String ywh) {
		this.ywh = ywh;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getQlrmc() {
		return qlrmc;
	}
	public void setQlrmc(String qlrmc) {
		this.qlrmc = qlrmc;
	}
	public String getQlrzjzl() {
		return qlrzjzl;
	}
	public void setQlrzjzl(String qlrzjzl) {
		this.qlrzjzl = qlrzjzl;
		this.qlrzjzl=ConstHelper.getReportValueByValue("ZJLX", qlrzjzl);
	}
	public String getQlryb() {
		return qlryb;
	}
	public void setQlryb(String qlryb) {
		this.qlryb = qlryb;
	}
	public String getQlrzjh() {
		return qlrzjh;
	}
	public void setQlrzjh(String qlrzjh) {
		this.qlrzjh = qlrzjh;
	}
	public String getQlrdljg() {
		return qlrdljg;
	}
	public void setQlrdljg(String qlrdljg) {
		this.qlrdljg = qlrdljg;
	}
	public String getQlrdlrdh() {
		return qlrdlrdh;
	}
	public void setQlrdlrdh(String qlrdlrdh) {
		this.qlrdlrdh = qlrdlrdh;
	}
	public String getQlrdlrmc() {
		return qlrdlrmc;
	}
	public void setQlrdlrmc(String qlrdlrmc) {
		this.qlrdlrmc = qlrdlrmc;
	}
	public String getQlrfrdh() {
		return qlrfrdh;
	}
	public void setQlrfrdh(String qlrfrdh) {
		this.qlrfrdh = qlrfrdh;
	}
	public String getQlrfrmc() {
		return qlrfrmc;
	}
	public void setQlrfrmc(String qlrfrmc) {
		this.qlrfrmc = qlrfrmc;
	}
	public String getQlrtxdz() {
		return qlrtxdz;
	}
	public void setQlrtxdz(String qlrtxdz) {
		this.qlrtxdz = qlrtxdz;
	}
	public String getYwrmc() {
		return ywrmc;
	}
	public void setYwrmc(String ywrmc) {
		this.ywrmc = ywrmc;
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
	public String getYwrtxdz() {
		return ywrtxdz;
	}
	public void setYwrtxdz(String ywrtxdz) {
		this.ywrtxdz = ywrtxdz;
	}
	public String getYwryb() {
		return ywryb;
	}
	public void setYwryb(String ywryb) {
		this.ywryb = ywryb;
	}
	public String getYwrfrmc() {
		return ywrfrmc;
	}
	public void setYwrfrmc(String ywrfrmc) {
		this.ywrfrmc = ywrfrmc;
	}
	public String getYwrfrdh() {
		return ywrfrdh;
	}
	public void setYwrfrdh(String ywrfrdh) {
		this.ywrfrdh = ywrfrdh;
	}
	public String getYwrdlrmc() {
		return ywrdlrmc;
	}
	public void setYwrdlrmc(String ywrdlrmc) {
		this.ywrdlrmc = ywrdlrmc;
	}
	public String getYwrdlrdh() {
		return ywrdlrdh;
	}
	public void setYwrdlrdh(String ywrdlrdh) {
		this.ywrdlrdh = ywrdlrdh;
	}
	public String getYwrdljg() {
		return ywrdljg;
	}
	public void setYwrdljg(String ywrdljg) {
		this.ywrdljg = ywrdljg;
	}
	
	public String getSqrlb() {
		return sqrlb;
	}
	public void setSqrlb(String sqrlb) {
		this.sqrlb = sqrlb;
	}
	
	public String getQlrdh() {
		return qlrdh;
	}
	public void setQlrdh(String qlrdh) {
		this.qlrdh = qlrdh;
	}
	public String getQlrlx() {
		return qlrlx;
	}
	public void setQlrlx(String qlrlx) {
		this.qlrlx = qlrlx;
	}
	
	public String getSqrid() {
		return sqrid;
	}
	public void setSqrid(String sqrid) {
		this.sqrid = sqrid;
	}
	public String getXmbh() {
		return xmbh;
	}
	public void setXmbh(String xmbh) {
		this.xmbh = xmbh;
	}
	
	public void replaceEmpty(){
		if(StringHelper.isEmpty(this.bz)){
			this.bz=null;
		}
		if(StringHelper.isEmpty(this.qlrdljg)){
			this.qlrdljg=null;
		}
		if(StringHelper.isEmpty(this.qlrdlrdh)){
			this.qlrdlrdh=null;
		}
		if(StringHelper.isEmpty(this.qlrdlrmc)){
			this.qlrdlrmc=null;
		}
		if(StringHelper.isEmpty(this.qlrmc)){
			this.qlrmc=null;
		}
		if(StringHelper.isEmpty(this.qlrtxdz)){
			this.qlrtxdz=null;
		}
		if(StringHelper.isEmpty(this.qlryb)){
			this.qlryb=null;
		}
		if(StringHelper.isEmpty(this.qlrzjh)){
			this.qlrzjh=null;
		}
		if(StringHelper.isEmpty(this.qlrzjzl)){
			this.qlrzjzl=null;
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
		if(StringHelper.isEmpty(this.ywrdljg)){
			this.ywrdljg=null;
		}
		if(StringHelper.isEmpty(this.ywrdlrdh)){
			this.ywrdlrdh=null;
		}
		if(StringHelper.isEmpty(this.ywrdlrmc)){
			this.ywrdlrmc=null;
		}
		if(StringHelper.isEmpty(this.ywrfrdh)){
			this.ywrfrdh=null;
		}
		if(StringHelper.isEmpty(this.ywrfrmc)){
			this.ywrfrmc=null;
		}
		if(StringHelper.isEmpty(this.ywrmc)){
			this.ywrmc=null;
		}
		if(StringHelper.isEmpty(this.ywrtxdz)){
			this.ywrtxdz=null;
		}
		if(StringHelper.isEmpty(this.ywryb)){
			this.ywryb=null;
		}
		if(StringHelper.isEmpty(this.ywrzjh)){
			this.ywryb=null;
		}
		if(StringHelper.isEmpty(this.ywrzjh)){
			this.ywrzjh=null;
		}
		if(StringHelper.isEmpty(this.ywrzjzl)){
			this.ywrzjzl=null;
		}
		if(StringHelper.isEmpty(this.sqrlb)){
			this.sqrlb=null;
		}
	}
}
