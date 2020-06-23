package com.supermap.realestate.registration.dataExchange.shyq;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.StringHelper;

/**
 * 
 * 宗地基本信息表
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "KTT_ZDJBXX")
public class KTTZDJBXX {

	@XmlAttribute(name = "MJDW", required = true)
	protected String mjdw;
	@XmlAttribute(name = "TFH")
	protected String tfh;
	@XmlAttribute(name = "DJH")
	protected String djh;
	@XmlAttribute(name = "QLSDFS")
	protected String qlsdfs;
	@XmlAttribute(name = "ZDSZN", required = true)
	protected String zdszn;
	@XmlAttribute(name = "ZL", required = true)
	protected String zl;
	@XmlAttribute(name = "FJ")
	protected String fj;
	@XmlAttribute(name = "ZDSZD", required = true)
	protected String zdszd;
	@XmlAttribute(name = "ZDSZB", required = true)
	protected String zdszb;
	@XmlAttribute(name = "DJ", required = true)
	protected String dj;
	@XmlAttribute(name = "ZT", required = true)
	protected String zt;
	@XmlAttribute(name = "QLLX", required = true)
	protected String qllx;
	@XmlAttribute(name = "JZXG")
	protected BigDecimal jzxg;
	@XmlAttribute(name = "ZDSZX", required = true)
	protected String zdszx;
	@XmlAttribute(name = "ZDT", required = true)
	protected String zdt;
	@XmlAttribute(name = "RJL")
	protected BigDecimal rjl;
	@XmlAttribute(name = "YSDM", required = true)
	protected String ysdm;
	@XmlAttribute(name = "ZDTZM", required = true)
	protected String zdtzm;
	@XmlAttribute(name = "BDCDYH", required = true)
	protected String bdcdyh;
	@XmlAttribute(name = "QLXZ", required = true)
	protected String qlxz;
	@XmlAttribute(name = "DBR", required = true)
	protected String dbr;
	@XmlAttribute(name = "ZDDM", required = true)
	protected String zddm;
	@XmlAttribute(name = "QXDM", required = true)
	protected String qxdm;
	@XmlAttribute(name = "ZDMJ", required = true)
	protected BigDecimal zdmj;
	@XmlAttribute(name = "DJSJ", required = true)
	protected String djsj;
	@XmlAttribute(name = "JG", required = true)
	protected BigDecimal jg;
	@XmlAttribute(name = "YT", required = true)
	protected String yt;
	@XmlAttribute(name = "JZMD")
	protected BigDecimal jzmd;
	
	@XmlAttribute(name = "BSM", required = true)
	protected int bsm;
	@XmlAttribute(name = "DAH", required = true)
	protected String dah;
	@XmlAttribute(name = "BZ")
	protected String bz;
	@XmlAttribute(name = "DJJGBM", required = true)
	protected String djjgbm;
	@XmlAttribute(name = "DJJGMC", required = true)
	protected String djjgmc;
	@XmlAttribute(name = "JDH", required = true)
	protected String jdh;
	@XmlAttribute(name = "JFH", required = true)
	protected String jfh;
	@XmlAttribute(name = "ZH", required = true)
	protected String zh;
	
	@XmlAttribute(name = "BDCDYID")
	protected String bdcdyid;
	
	public String getZH() {
		return zh;
	}
	
	public void setZH(String value) {
		this.zh = value;
	}
	
	public String getJFH() {
		return jfh;
	}
	
	public void setJFH(String value) {
		this.jfh = value;
	}
	
	public String getJDH() {
		return jdh;
	}
	
	public void setJDH(String value) {
		this.jdh = value;
	}
	
	public String getDJJGMC() {
		return djjgmc;
	}
	
	public void setDJJGMC(String value) {
		this.djjgmc = value;
	}
	
	public String getDJJGBM() {
		return djjgbm;
	}
	
	public void setDJJGBM(String value) {
		this.djjgbm = value;
	}
	
	public String getBZ() {
		return bz;
	}
	
	public void setBZ(String value) {
		this.bz = value;
	}
	
	public String getDAH() {
		return dah;
	}
	
	public void setDAH(String value) {
		this.dah = value;
	}
	
	public int getBSM() {
		return bsm;
	}
	
	public void setBSM(int value) {
		this.bsm = value;
	}

	/**
	 * Gets the value of the mjdw property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMJDW() {
		return mjdw;
	}

	/**
	 * Sets the value of the mjdw property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMJDW(String value) {
		this.mjdw = value;
		//this.mjdw=ConstHelper.getReportValueByValue("MJDW", mjdw);
//		System.out.println(mjdw);
	}

	/**
	 * Gets the value of the tfh property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTFH() {
		return tfh;
	}

	/**
	 * Sets the value of the tfh property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTFH(String value) {
		this.tfh = value;
	}

	/**
	 * Gets the value of the djh property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDJH() {
		return djh;
	}

	/**
	 * Sets the value of the djh property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDJH(String value) {
		this.djh = value;
	}

	/**
	 * Gets the value of the qlsdfs property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getQLSDFS() {
		return qlsdfs;
	}

	/**
	 * Sets the value of the qlsdfs property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setQLSDFS(String value) {
		this.qlsdfs = value;
		this.qlsdfs=ConstHelper.getReportValueByValue("QLSDFS", qlsdfs);
	}

	/**
	 * Gets the value of the zdszn property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getZDSZN() {
		return zdszn;
	}

	/**
	 * Sets the value of the zdszn property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setZDSZN(String value) {
		this.zdszn = value;
	}

	/**
	 * Gets the value of the zl property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getZL() {
		return zl;
	}

	/**
	 * Sets the value of the zl property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setZL(String value) {
		this.zl = value;
	}

	/**
	 * Gets the value of the fj property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFJ() {
		return fj;
	}

	/**
	 * Sets the value of the fj property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setFJ(String value) {
		this.fj = value;
	}

	/**
	 * Gets the value of the zdszd property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getZDSZD() {
		return zdszd;
	}

	/**
	 * Sets the value of the zdszd property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setZDSZD(String value) {
		this.zdszd = value;
	}

	/**
	 * Gets the value of the zdszb property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getZDSZB() {
		return zdszb;
	}

	/**
	 * Sets the value of the zdszb property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setZDSZB(String value) {
		this.zdszb = value;
	}

	/**
	 * Gets the value of the dj property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDJ() {
		return dj;
	}

	/**
	 * Sets the value of the dj property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDJ(String value) {
		this.dj = value;
		this.dj=ConstHelper.getReportValueByValue("TDDJ", dj);
	}

	/**
	 * Gets the value of the zt property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getZT() {
		return zt;
	}

	/**
	 * Sets the value of the zt property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setZT(String value) {
		this.zt = value;
	}

	/**
	 * Gets the value of the qllx property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getQLLX() {
		return qllx;
	}

	/**
	 * Sets the value of the qllx property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setQLLX(String value) {
		this.qllx = value;
		this.qllx=ConstHelper.getReportValueByValue("QLLX", qllx);
	}

	/**
	 * Gets the value of the jzxg property.
	 * 
	 * @return possible object is {@link double }
	 * 
	 */
	public BigDecimal getJZXG() {
		return jzxg;
	}

	/**
	 * Sets the value of the jzxg property.
	 * 
	 * @param value
	 *            allowed object is {@link double }
	 * 
	 */
	public void setJZXG(BigDecimal value) {
		this.jzxg = value;
	}

	/**
	 * Gets the value of the zdszx property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getZDSZX() {
		return zdszx;
	}

	/**
	 * Sets the value of the zdszx property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setZDSZX(String value) {
		this.zdszx = value;
	}

	/**
	 * Gets the value of the zdt property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getZDT() {
		return zdt;
	}

	/**
	 * Sets the value of the zdt property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setZDT(String value) {
		this.zdt = value;
	}

	/**
	 * Gets the value of the rjl property.
	 * 
	 * @return possible object is {@link double }
	 * 
	 */
	public BigDecimal getRJL() {
		return rjl;
	}

	/**
	 * Sets the value of the rjl property.
	 * 
	 * @param value
	 *            allowed object is {@link double }
	 * 
	 */
	public void setRJL(BigDecimal value) {
		this.rjl = value;
	}

	/**
	 * Gets the value of the ysdm property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getYSDM() {
		return ysdm;
	}

	/**
	 * Sets the value of the ysdm property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setYSDM(String value) {
		this.ysdm = value;
	}

	/**
	 * Gets the value of the zdtzm property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getZDTZM() {
		return zdtzm;
	}

	/**
	 * Sets the value of the zdtzm property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setZDTZM(String value) {
		this.zdtzm = value;
		this.zdtzm=ConstHelper.getReportValueByValue("TZM", zdtzm);
	}

	/**
	 * Gets the value of the bdcdyh property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getBDCDYH() {
		return bdcdyh;
	}

	/**
	 * Sets the value of the bdcdyh property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setBDCDYH(String value) {
		this.bdcdyh = value;
	}

	/**
	 * Gets the value of the qlxz property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getQLXZ() {
		return qlxz;
	}

	/**
	 * Sets the value of the qlxz property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setQLXZ(String value) {
		this.qlxz = value;
		this.qlxz=ConstHelper.getReportValueByValue("QLXZ", qlxz);
	}

	/**
	 * Gets the value of the dbr property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDBR() {
		return dbr;
	}

	/**
	 * Sets the value of the dbr property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDBR(String value) {
		this.dbr = value;
	}

	/**
	 * Gets the value of the zddm property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getZDDM() {
		return zddm;
	}

	/**
	 * Sets the value of the zddm property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setZDDM(String value) {
		this.zddm = value;
	}

	/**
	 * Gets the value of the qxdm property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getQXDM() {
		return qxdm;
	}

	/**
	 * Sets the value of the qxdm property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setQXDM(String value) {
		this.qxdm = value;
	}

	/**
	 * Gets the value of the zdmj property.
	 * 
	 * @return possible object is {@link double }
	 * 
	 */
	public BigDecimal getZDMJ() {
		return zdmj;
	}

	/**
	 * Sets the value of the zdmj property.
	 * 
	 * @param value
	 *            allowed object is {@link double }
	 * 
	 */
	public void setZDMJ(BigDecimal value) {
		this.zdmj = StringHelper.cutBigDecimal(value, 4);
	}

	/**
	 * Gets the value of the djsj property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public String getDJSJ() {
		return djsj;
	}

	/**
	 * Sets the value of the djsj property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setDJSJ(String value) {
		this.djsj = value;
	}

	/**
	 * Gets the value of the jg property.
	 * 
	 * @return possible object is {@link double }
	 * 
	 */
	public BigDecimal getJG() {
		return jg;
	}

	/**
	 * Sets the value of the jg property.
	 * 
	 * @param value
	 *            allowed object is {@link double }
	 * 
	 */
	public void setJG(BigDecimal value) {
		this.jg = StringHelper.cutBigDecimal(value, 4);
	}

	/**
	 * Gets the value of the yt property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getYT() {
		return yt;
	}

	/**
	 * Sets the value of the yt property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setYT(String value) {
		this.yt = value;
		this.yt=ConstHelper.getReportValueByValue("TDYT", yt);
	}

	/**
	 * Gets the value of the jzmd property.
	 * 
	 * @return possible object is {@link double }
	 * 
	 */
	public BigDecimal getJZMD() {
		return jzmd;
	}

	/**
	 * Sets the value of the jzmd property.
	 * 
	 * @param value
	 *            allowed object is {@link double }
	 * 
	 */
	public void setJZMD(BigDecimal value) {
		this.jzmd = value;
	}
	
	public String getBDCDYID() {
		return bdcdyid;
	}
	public void setBDCDYID(String bdcdyid) {
		this.bdcdyid = bdcdyid;
	}
	
    public void replaceEmpty(){
    	if(StringHelper.isEmpty(this.bdcdyh)){
    		this.bdcdyh=null;
    	}
//    	if(StringHelper.isEmpty(this.bsm)){
//    		this.bsm=null;
//    	}
    	if(StringHelper.isEmpty(this.bz)){
    		this.bz=null;
    	}
    	if(StringHelper.isEmpty(this.dah)){
    		this.dah=null;
    	}
    	if(StringHelper.isEmpty(this.dbr)){
    		this.dbr=null;
    	}
    	if(StringHelper.isEmpty(this.dj)){
    		this.dj=null;
    	}
    	if(StringHelper.isEmpty(this.djh)){
    		this.djh=null;
    	}
    	if(StringHelper.isEmpty(this.djjgbm)){
    		this.djjgbm=null;
    	}
    	if(StringHelper.isEmpty(this.djjgmc)){
    		this.djjgmc=null;
    	}
    	if(StringHelper.isEmpty(this.djsj)){
    		this.djsj=null;
    	}
    	if(StringHelper.isEmpty(this.fj)){
    		this.fj=null;
    	}
    	if(StringHelper.isEmpty(this.jdh)){
    		this.jdh=null;
    	}
    	if(StringHelper.isEmpty(this.jfh)){
    		this.jfh=null;
    	}
    	if(StringHelper.isEmpty(this.mjdw)){
    		this.mjdw=null;
    	}
    	if(StringHelper.isEmpty(this.qllx)){
    		this.qllx=null;
    	}
    	if(StringHelper.isEmpty(this.qlsdfs)){
    		this.qlsdfs=null;
    	}
    	if(StringHelper.isEmpty(this.qlxz)){
    		this.qlxz=null;
    	}
    	if(StringHelper.isEmpty(this.qxdm)){
    		this.qxdm=null;
    	}
    	if(StringHelper.isEmpty(this.tfh)){
    		this.tfh=null;
    	}
    	if(StringHelper.isEmpty(this.ysdm)){
    		this.ysdm=null;
    	}
    	if(StringHelper.isEmpty(this.yt)){
    		this.yt=null;
    	}
    	if(StringHelper.isEmpty(this.zddm)){
    		this.zddm=null;
    	}
    	if(StringHelper.isEmpty(this.zdszb)){
    		this.zdszb=null;
    	}
    	if(StringHelper.isEmpty(this.zdszd)){
    		this.zdszd=null;
    	}
    	if(StringHelper.isEmpty(this.zdszn)){
    		this.zdszn=null;
    	}
    	if(StringHelper.isEmpty(this.zdszx)){
    		this.zdszx=null;
    	}
    	if(StringHelper.isEmpty(this.zh)){
    		this.zh=null;
    	}
    	if(StringHelper.isEmpty(this.zdt)){
    		this.zdt=null;
    	}
    	if(StringHelper.isEmpty(this.zdtzm)){
    		this.zdtzm=null;
    	}
    	if(StringHelper.isEmpty(this.zl)){
    		this.zl=null;
    	}
    	if(StringHelper.isEmpty(this.zt)){
    		this.zt=null;
    	}
    }
}
