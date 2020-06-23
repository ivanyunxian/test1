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
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute ref="{}YWH"/>
 *       &lt;attribute ref="{}QLLX"/>
 *       &lt;attribute ref="{}SYQQSSJ"/>
 *       &lt;attribute ref="{}DJLX use="required""/>
 *       &lt;attribute ref="{}YSDM"/>
 *       &lt;attribute ref="{}SYQJSSJ"/>
 *       &lt;attribute ref="{}BDCDYH"/>
 *       &lt;attribute ref="{}DJJG use="required""/>
 *       &lt;attribute ref="{}QDJG"/>
 *       &lt;attribute ref="{}DBR"/>
 *       &lt;attribute ref="{}ZDDM"/>
 *       &lt;attribute ref="{}QXDM"/>
 *       &lt;attribute ref="{}DJYY use="required""/>
 *       &lt;attribute ref="{}BDCQZH use="required""/>
 *       &lt;attribute ref="{}QSZT use="required""/>
 *       &lt;attribute ref="{}FJ"/>
 *       &lt;attribute ref="{}DJSJ"/>
 *       &lt;attribute ref="{}SYQMJ"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 建设用地、宅基地使用权表
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "QLF_QL_JSYDSYQ")
public class QLFQLJSYDSYQ {

    @XmlAttribute(name = "YWH")
    protected String ywh;
    @XmlAttribute(name = "QLLX", required = true)
    protected String qllx;
    @XmlAttribute(name = "SYQQSSJ")
    protected String syqqssj;
    @XmlAttribute(name = "DJLX", required = true)
    protected String djlx;
    @XmlAttribute(name = "YSDM", required = true)
    protected String ysdm;
    @XmlAttribute(name = "SYQJSSJ")
    protected String syqjssj;
    @XmlAttribute(name = "BDCDYH", required = true)
    protected String bdcdyh;
    @XmlAttribute(name = "DJJG", required = true)
    protected String djjg;
    @XmlAttribute(name = "QDJG")
    protected BigDecimal qdjg;
    @XmlAttribute(name = "DBR", required = true)
    protected String dbr;
    @XmlAttribute(name = "ZDDM", required = true)
    protected String zddm;
    @XmlAttribute(name = "QXDM", required = true)
    protected String qxdm;
    @XmlAttribute(name = "DJYY", required = true)
    protected String djyy;
    @XmlAttribute(name = "BDCQZH", required = true)
    protected String bdcqzh;
    @XmlAttribute(name = "QSZT", required = true)
    protected String qszt;
    @XmlAttribute(name = "FJ")
    protected String fj;
    @XmlAttribute(name = "DJSJ", required = true)
    protected String djsj;
    @XmlAttribute(name = "SYQMJ")
    protected BigDecimal syqmj;
    
    @XmlAttribute(name = "QLID")
    protected String qlid;
    @XmlAttribute(name = "XMBH")
    protected String xmbh;
    @XmlAttribute(name = "BDCDYID")
    protected String bdcdyid;
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
     * Gets the value of the qllx property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQLLX() {
        return qllx;
    }

    /**
     * Sets the value of the qllx property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQLLX(String value) {
        this.qllx = value;
        this.qllx=ConstHelper.getReportValueByValue("QLLX", qllx);
    }

    /**
     * Gets the value of the syqqssj property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public String getSYQQSSJ() {
        return syqqssj;
    }

    /**
     * Sets the value of the syqqssj property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSYQQSSJ(String value) {
        this.syqqssj = value;
    }

    /**
     * Gets the value of the djlx property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDJLX() {
        return djlx;
    }

    /**
     * Sets the value of the djlx property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDJLX(String value) {
        this.djlx = value;
        this.djlx=ConstHelper.getReportValueByValue("DJLX", djlx);
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
     * Gets the value of the syqjssj property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public String getSYQJSSJ() {
        return syqjssj;
    }

    /**
     * Sets the value of the syqjssj property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSYQJSSJ(String value) {
        this.syqjssj = value;
    }

    /**
     * Gets the value of the bdcdyh property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBDCDYH() {
        return bdcdyh;
    }

    /**
     * Sets the value of the bdcdyh property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBDCDYH(String value) {
        this.bdcdyh = value;
    }

    /**
     * Gets the value of the djjg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDJJG() {
        return djjg;
    }

    /**
     * Sets the value of the djjg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDJJG(String value) {
        this.djjg = value;
    }

    /**
     * Gets the value of the qdjg property.
     * 
     * @return
     *     possible object is
     *     {@link double }
     *     
     */
    public BigDecimal getQDJG() {
        return qdjg;
    }

    /**
     * Sets the value of the qdjg property.
     * 
     * @param value
     *     allowed object is
     *     {@link double }
     *     
     */
    public void setQDJG(BigDecimal value) {
        this.qdjg = StringHelper.cutBigDecimal(value, 4);
    }

    /**
     * Gets the value of the dbr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDBR() {
        return dbr;
    }

    /**
     * Sets the value of the dbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDBR(String value) {
        this.dbr = value;
    }

    /**
     * Gets the value of the zddm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZDDM() {
        return zddm;
    }

    /**
     * Sets the value of the zddm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZDDM(String value) {
        this.zddm = value;
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
     * Gets the value of the djyy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDJYY() {
        return djyy;
    }

    /**
     * Sets the value of the djyy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDJYY(String value) {
        this.djyy = value;
    }

    /**
     * Gets the value of the bdcqzh property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBDCQZH() {
        return bdcqzh;
    }

    /**
     * Sets the value of the bdcqzh property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBDCQZH(String value) {
        this.bdcqzh = value;
    }

    /**
     * Gets the value of the qszt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQSZT() {
        return qszt;
    }

    /**
     * Sets the value of the qszt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQSZT(String value) {
        this.qszt = value;
    }

    /**
     * Gets the value of the fj property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFJ() {
        return fj;
    }

    /**
     * Sets the value of the fj property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFJ(String value) {
        this.fj = value;
    }

    /**
     * Gets the value of the djsj property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public String getDJSJ() {
        return djsj;
    }

    /**
     * Sets the value of the djsj property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDJSJ(String value) {
        this.djsj = value;
    }

    /**
     * Gets the value of the syqmj property.
     * 
     * @return
     *     possible object is
     *     {@link double }
     *     
     */
    public BigDecimal getSYQMJ() {
        return syqmj;
    }

    /**
     * Sets the value of the syqmj property.
     * 
     * @param value
     *     allowed object is
     *     {@link double }
     *     
     */
    public void setSYQMJ(BigDecimal value) {
        this.syqmj = StringHelper.cutBigDecimal(value, 2);
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
    	if(StringHelper.isEmpty(this.qllx)){
    		this.qllx=null;
    	}
    	
    	if(StringHelper.isEmpty(this.ywh)){
    		this.ywh=null;
    	}
    	
    	if(StringHelper.isEmpty(this.syqqssj)){
    		this.syqqssj=null;
    	}
    	
    	if(StringHelper.isEmpty(this.djlx)){
    		this.djlx=null;
    	}
    	
    	if(StringHelper.isEmpty(this.ysdm)){
    		this.ysdm=null;
    	}
    	
    	if(StringHelper.isEmpty(this.syqjssj)){
    		this.syqjssj=null;
    	}
    	
    	if(StringHelper.isEmpty(this.bdcdyh)){
    		this.bdcdyh=null;
    	}
    	
    	if(StringHelper.isEmpty(this.djjg)){
    		this.djjg=null;
    	}
    	
    	if(StringHelper.isEmpty(this.qdjg)){
    		this.qdjg=null;
    	}
    	
    	if(StringHelper.isEmpty(this.dbr)){
    		this.dbr=null;
    	}
    	
    	if(StringHelper.isEmpty(this.zddm)){
    		this.zddm=null;
    	}
    	
    	if(StringHelper.isEmpty(this.qxdm)){
    		this.qxdm=null;
    	}
    	
    	if(StringHelper.isEmpty(this.djyy)){
    		this.djyy=null;
    	}
    	
    	if(StringHelper.isEmpty(this.qllx)){
    		this.qllx=null;
    	}
    }
}
