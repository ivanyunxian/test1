package com.supermap.realestate.registration.dataExchange.fwsyq;

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
 * @Description:自然幢
 * @author DIAOLIWEI
 * @date 2015年8月28日 下午9:01:32
 * @Copyright SuperMap
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "KTT_FW_ZRZ")
public class KTTFWZRZ {
	
	@XmlAttribute(name = "BSM", required = true)
	protected int bsm;
	@XmlAttribute(name = "ZRZH", required = true)
    protected String zrzh;
    @XmlAttribute(name = "BZ")
    protected String bz;
    @XmlAttribute(name = "ZT", required = true)
    protected String zt;
    @XmlAttribute(name = "JZWJBYT")
    protected String jzwjbyt;
    @XmlAttribute(name = "YSDM", required = true)
    protected String ysdm;
    @XmlAttribute(name = "ZTS", required = true)
    protected String zts;
    @XmlAttribute(name = "BDCDYH", required = true)
    protected String bdcdyh;
    @XmlAttribute(name = "FWJG", required = true)
    protected String fwjg;
    @XmlAttribute(name = "GHYT", required = true)
    protected String ghyt;
    @XmlAttribute(name = "DXSD")
    protected double dxsd;
    @XmlAttribute(name = "ZDDM", required = true)
    protected String zddm;
    @XmlAttribute(name = "QXDM", required = true)
    protected String qxdm;
    @XmlAttribute(name = "DXCS", required = true)
    protected int dxcs;
    @XmlAttribute(name = "DSCS", required = true)
    protected int dscs;
    @XmlAttribute(name = "ZCS", required = true)
    protected String zcs;
    @XmlAttribute(name = "ZYDMJ", required = true)
    protected double zydmj;
    @XmlAttribute(name = "ZZDMJ", required = true)
    protected double zzdmj;
    @XmlAttribute(name = "JZWGD", required = true)
    protected BigDecimal jzwgd;
    @XmlAttribute(name = "JGRQ")
    protected String jgrq;
    @XmlAttribute(name = "JZWMC", required = true)
    protected String jzwmc;
    @XmlAttribute(name = "XMMC", required = true)
    protected String xmmc;
    @XmlAttribute(name = "YCJZMJ")
    protected double ycjzmj;
    @XmlAttribute(name = "SCJZMJ")
    protected double scjzmj;
    @XmlAttribute(name = "DAH")
    protected String dah;
    
    @XmlAttribute(name = "BDCDYID")
    protected String bdcdyid;
    @XmlAttribute(name = "ZDBDCDYID")
    protected String zdbdcdyid;
    
    public String getDah(){
    	return dah;
    }
    public void setDah(String dah){
    	this.dah = dah;
    }
    
    public int getBsm(){
    	return bsm;
    }
    public void setBsm(int bsm){
    	this.bsm = bsm;
    }
	public double getScjzmj() {
		return scjzmj;
	}
	public void setScjzmj(double scjzmj) {
		this.scjzmj = scjzmj;
	}
	public double getYcjzmj() {
		return ycjzmj;
	}
	public void setYcjzmj(double ycjzmj) {
		this.ycjzmj = ycjzmj;
	}
	public String getZrzh() {
		return zrzh;
	}
	public void setZrzh(String zrzh) {
		this.zrzh = zrzh;
	}
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
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
	public String getZddm() {
		return zddm;
	}
	public void setZddm(String zddm) {
		this.zddm = zddm;
	}
	public String getQxdm() {
		return qxdm;
	}
	public void setQxdm(String qxdm) {
		this.qxdm = qxdm;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getJzwjbyt() {
		return jzwjbyt;
	}
	public void setJzwjbyt(String jzwjbyt) {
		this.jzwjbyt = jzwjbyt;
	}
	public String getZts() {
		return zts;
	}
	public void setZts(String zts) {
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
	public double getDxsd() {
		return dxsd;
	}
	public void setDxsd(double dxsd) {
		this.dxsd = dxsd;
	}
	public int getDxcs() {
		return dxcs;
	}
	public void setDxcs(int dxcs) {
		this.dxcs = dxcs;
	}
	public int getDscs() {
		return dscs;
	}
	public void setDscs(int dscs) {
		this.dscs = dscs;
	}
	public String getZcs() {
		return zcs;
	}
	public void setZcs(String zcs) {
		this.zcs = zcs;
	}
	public double getZydmj() {
		return zydmj;
	}
	public void setZydmj(double zydmj) {
		this.zydmj = StringHelper.getDouble(StringHelper.cut(zydmj, 3));
	}
	public double getZzdmj() {
		return zzdmj;
	}
	public void setZzdmj(double zzdmj) {
		this.zzdmj = StringHelper.getDouble(StringHelper.cut(zzdmj, 3));
	}
	public BigDecimal getJzwgd() {
		return jzwgd;
	}
	public void setJzwgd(BigDecimal jzwgd) {
		this.jzwgd = StringHelper.cutBigDecimal(jzwgd, 2);
	}
	public String getJgrq() {
		return jgrq;
	}
	public void setJgrq(String jgrq) {
		this.jgrq = jgrq;
	}
	public String getJzwmc() {
		return jzwmc;
	}
	public void setJzwmc(String jzwmc) {
		this.jzwmc = jzwmc;
	}
	public String getXmmc() {
		return xmmc;
	}
	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
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
	
    public void replaceEmpty(){
    	if(StringHelper.isEmpty(this.bdcdyh)){
    		this.bdcdyh=null;
    	}
    	if(StringHelper.isEmpty(this.bz)){
    		this.bz=null;
    	}
    	if(StringHelper.isEmpty(this.dah)){
    		this.dah=null;
    	}
    	if(StringHelper.isEmpty(this.fwjg)){
    		this.fwjg=null;
    	}
    	if(StringHelper.isEmpty(this.ghyt)){
    		this.ghyt=null;
    	}
    	if(StringHelper.isEmpty(this.jgrq)){
    		this.jgrq=null;
    	}
    	if(StringHelper.isEmpty(this.jzwjbyt)){
    		this.jzwjbyt=null;
    	}
    	if(StringHelper.isEmpty(this.jzwmc)){
    		this.jzwmc=null;
    	}
    	if(StringHelper.isEmpty(this.qxdm)){
    		this.qxdm=null;
    	}
    	if(StringHelper.isEmpty(this.xmmc)){
    		this.xmmc=null;
    	}
    	if(StringHelper.isEmpty(this.ysdm)){
    		this.ysdm=null;
    	}
    	if(StringHelper.isEmpty(this.zcs)){
    		this.zcs=null;
    	}
    	if(StringHelper.isEmpty(this.zddm)){
    		this.zddm=null;
    	}
    	if(StringHelper.isEmpty(this.zrzh)){
    		this.zrzh=null;
    	}
    	if(StringHelper.isEmpty(this.zt)){
    		this.zt=null;
    	}
    	if(StringHelper.isEmpty(this.zts)){
    		this.zts=null;
    	}
    }
}
