package com.supermap.realestate.registration.dataExchange;
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
 * 登记收费信息
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "DJF_DJ_SF")
public class DJFDJSF {

    @XmlAttribute(name = "YWH", required = true)
    protected String ywh;
    @XmlAttribute(name = "SFKMMC", required = true)
    protected String sfkmmc;
    @XmlAttribute(name = "SFEWSF")
    protected String sfewsf;
    @XmlAttribute(name = "ZKHYSJE")
    protected BigDecimal zkhysje;
    @XmlAttribute(name = "SFJS")
    protected double sfjs;
    @XmlAttribute(name = "SJFFR", required = true)
    protected String sjffr;
    @XmlAttribute(name = "JFRY", required = true)
    protected String jfry;
    @XmlAttribute(name = "YSDM", required = true)
    protected String ysdm;
    @XmlAttribute(name = "FFF", required = true)
    protected String fff;
    @XmlAttribute(name = "QXDM", required = true)
    protected String qxdm;
    @XmlAttribute(name = "SFLX", required = true)
    protected String sflx;
    @XmlAttribute(name = "SFRQ", required = true)
    protected String sfrq;
    @XmlAttribute(name = "YSJE", required = true)
    protected BigDecimal ysje;
    @XmlAttribute(name = "SSJE", required = true)
    protected BigDecimal ssje;
    @XmlAttribute(name = "JFRQ", required = true)
    protected String jfrq;
    @XmlAttribute(name = "SFDW", required = true)
    protected String sfdw;
    @XmlAttribute(name = "SFRY", required = true)
    protected String sfry;
    @XmlAttribute(name = "XMBH")
	protected String xmbh;

    /**
     * Gets the value of the ywh property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getYWH() {
        return ywh;
    }

    /**
     * Sets the value of the ywh property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setYWH(String value) {
        this.ywh = value;
    }

    /**
     * Gets the value of the sfkmmc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSFKMMC() {
        return sfkmmc;
    }

    /**
     * Sets the value of the sfkmmc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSFKMMC(String value) {
        this.sfkmmc = value;
    }

    /**
     * Gets the value of the sfewsf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSFEWSF() {
        return sfewsf;
    }

    /**
     * Sets the value of the sfewsf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSFEWSF(String value) {
        this.sfewsf = value;
        this.sfewsf=ConstHelper.getReportValueByValue("SFZD", sfewsf);
    }

    /**
     * Gets the value of the zkhysje property.
     * 
     * @return
     *     possible object is
     *     {@link double }
     *     
     */
    public BigDecimal getZKHYSJE() {
        return zkhysje;
    }

    /**
     * Sets the value of the zkhysje property.
     * 
     * @param value
     *     allowed object is
     *     {@link double }
     *     
     */
    public void setZKHYSJE(BigDecimal value) {
        this.zkhysje = value;
    }

    /**
     * Gets the value of the sfjs property.
     * 
     * @return
     *     possible object is
     *     {@link double }
     *     
     */
    public double getSFJS() {
        return sfjs;
    }

    /**
     * Sets the value of the sfjs property.
     * 
     * @param value
     *     allowed object is
     *     {@link double }
     *     
     */
    public void setSFJS(double value) {
        this.sfjs = value;
    }

    /**
     * Gets the value of the sjffr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSJFFR() {
        return sjffr;
    }

    /**
     * Sets the value of the sjffr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSJFFR(String value) {
        this.sjffr = value;
    }

    /**
     * Gets the value of the jfry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJFRY() {
        return jfry;
    }

    /**
     * Sets the value of the jfry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJFRY(String value) {
        this.jfry = value;
    }

    /**
     * Gets the value of the ysdm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getYSDM() {
        return ysdm;
    }

    /**
     * Sets the value of the ysdm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setYSDM(String value) {
        this.ysdm = value;
    }

    /**
     * Gets the value of the fff property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFFF() {
        return fff;
    }

    /**
     * Sets the value of the fff property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFFF(String value) {
        this.fff = value;
        this.fff=ConstHelper.getReportValueByValue("FFF", fff);
    }

    /**
     * Gets the value of the qxdm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQXDM() {
        return qxdm;
    }

    /**
     * Sets the value of the qxdm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQXDM(String value) {
        this.qxdm = value;
    }

    /**
     * Gets the value of the sflx property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSFLX() {
        return sflx;
    }

    /**
     * Sets the value of the sflx property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSFLX(String value) {
        this.sflx = value;
        this.sflx=ConstHelper.getReportValueByValue("SFLX", sflx);
    }

    /**
     * Gets the value of the sfrq property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public String getSFRQ() {
        return sfrq;
    }

    /**
     * Sets the value of the sfrq property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSFRQ(String value) {
        this.sfrq = value;
    }

    /**
     * Gets the value of the ysje property.
     * 
     * @return
     *     possible object is
     *     {@link double }
     *     
     */
    public BigDecimal getYSJE() {
        return ysje;
    }

    /**
     * Sets the value of the ysje property.
     * 
     * @param value
     *     allowed object is
     *     {@link double }
     *     
     */
    public void setYSJE(BigDecimal value) {
        this.ysje = value;
    }

    /**
     * Gets the value of the ssje property.
     * 
     * @return
     *     possible object is
     *     {@link double }
     *     
     */
    public BigDecimal getSSJE() {
        return ssje;
    }

    /**
     * Sets the value of the ssje property.
     * 
     * @param value
     *     allowed object is
     *     {@link double }
     *     
     */
    public void setSSJE(BigDecimal value) {
        this.ssje = value;
    }

    /**
     * Gets the value of the jfrq property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public String getJFRQ() {
        return jfrq;
    }

    /**
     * Sets the value of the jfrq property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setJFRQ(String value) {
        this.jfrq = value;
    }

    /**
     * Gets the value of the sfdw property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSFDW() {
        return sfdw;
    }

    /**
     * Sets the value of the sfdw property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSFDW(String value) {
        this.sfdw = value;
    }

    /**
     * Gets the value of the sfry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSFRY() {
        return sfry;
    }

    /**
     * Sets the value of the sfry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSFRY(String value) {
        this.sfry = value;
    }
    
    public String getXmbh() {
		return xmbh;
	}
	public void setXmbh(String xmbh) {
		this.xmbh = xmbh;
	}

    public void replayEmpty(){
    	if(StringHelper.isEmpty(this.fff)){
    		this.fff=null;
    	}
    	if(StringHelper.isEmpty(this.jfrq)){
    		this.jfrq=null;
    	}
    	if(StringHelper.isEmpty(this.jfry)){
    		this.jfry=null;
    	}
    	if(StringHelper.isEmpty(this.qxdm)){
    		this.qxdm=null;
    	}
    	if(StringHelper.isEmpty(this.sfdw)){
    		this.sfdw=null;
    	}
    	if(StringHelper.isEmpty(this.sfewsf)){
    		this.sfewsf=null;
    	}
    	if(StringHelper.isEmpty(this.sfkmmc)){
    		this.sfkmmc=null;
    	}
    	if(StringHelper.isEmpty(this.sflx)){
    		this.sflx=null;
    	}
    	if(StringHelper.isEmpty(this.sfrq)){
    		this.sfrq=null;
    	}
    	if(StringHelper.isEmpty(this.sfry)){
    		this.sfry=null;
    	}
    	if(StringHelper.isEmpty(this.sjffr)){
    		this.sjffr=null;
    	}
    	if(StringHelper.isEmpty(this.ysdm)){
    		this.ysdm=null;
    	}
    	if(StringHelper.isEmpty(this.ywh)){
    		this.ywh=null;
    	}
    }
}
