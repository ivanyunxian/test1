package com.supermap.realestate.registration.dataExchange;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.StringHelper;


/**
 * 登记受理申请信息
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "DJT_DJ_SLSQ")
public class DJTDJSLSQ {

    @XmlAttribute(name = "YWH", required = true)
    protected String ywh;
    @XmlAttribute(name = "TZRDZYJ")
    protected String tzrdzyj;
    @XmlAttribute(name = "TZFS")
    protected String tzfs;
    @XmlAttribute(name = "SLRY", required = true)
    protected String slry;
    @XmlAttribute(name = "TZRDH")
    protected String tzrdh;
    @XmlAttribute(name = "BZ")
    protected String bz;
    @XmlAttribute(name = "YSDM", required = true)
    protected String ysdm;
    @XmlAttribute(name = "DJXL")
    protected String djxl;
    @XmlAttribute(name = "TZRXM")
    protected String tzrxm;
    @XmlAttribute(name = "TZRYDDH")
    protected String tzryddh;
    @XmlAttribute(name = "QXDM", required = true)
    protected String qxdm;
    @XmlAttribute(name = "JSSJ")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar jssj;
    @XmlAttribute(name = "ZL")
    protected String zl;
    @XmlAttribute(name = "AJZT")
    protected String ajzt;
    @XmlAttribute(name = "SLSJ", required = true)
    protected String slsj;
    @XmlAttribute(name = "SFWTAJ")
    protected String sfwtaj;
    @XmlAttribute(name = "DJDL", required = true)
    protected String djdl;
    @XmlAttribute(name = "SQZSBS", required = true)
    protected int sqzsbs;
    @XmlAttribute(name = "SQFBCZ", required = true)
    protected int sqfbcz;
    @XmlAttribute(name = "XMBH")
	protected String xmbh;

    
    public int getSQZSBS() {
        return sqzsbs;
    }

    public void setSQZSBS(int value) {
        this.sqzsbs = value;
    }
    
    public int getSQFBCZ() {
        return sqfbcz;
    }

    public void setSQFBCZ(int value) {
        this.sqfbcz = value;
    }
 
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
     * Gets the value of the tzrdzyj property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTZRDZYJ() {
        return tzrdzyj;
    }

    /**
     * Sets the value of the tzrdzyj property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTZRDZYJ(String value) {
        this.tzrdzyj = value;
    }

    /**
     * Gets the value of the tzfs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTZFS() {
        return tzfs;
    }

    /**
     * Sets the value of the tzfs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTZFS(String value) {
        this.tzfs = value;
        this.tzfs=ConstHelper.getReportValueByValue("TZFS", tzfs);
    }

    /**
     * Gets the value of the slry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSLRY() {
        return slry;
    }

    /**
     * Sets the value of the slry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSLRY(String value) {
        this.slry = value;
    }

    /**
     * Gets the value of the tzrdh property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTZRDH() {
        return tzrdh;
    }

    /**
     * Sets the value of the tzrdh property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTZRDH(String value) {
        this.tzrdh = value;
    }

    /**
     * Gets the value of the bz property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBZ() {
        return bz;
    }

    /**
     * Sets the value of the bz property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBZ(String value) {
        this.bz = value;
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
     * Gets the value of the djxl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDJXL() {
        return djxl;
    }

    /**
     * Sets the value of the djxl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDJXL(String value) {
        this.djxl = value;
    }

    /**
     * Gets the value of the tzrxm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTZRXM() {
        return tzrxm;
    }

    /**
     * Sets the value of the tzrxm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTZRXM(String value) {
        this.tzrxm = value;
    }

    /**
     * Gets the value of the tzryddh property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTZRYDDH() {
        return tzryddh;
    }

    /**
     * Sets the value of the tzryddh property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTZRYDDH(String value) {
        this.tzryddh = value;
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
     * Gets the value of the jssj property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getJSSJ() {
        return jssj;
    }

    /**
     * Sets the value of the jssj property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setJSSJ(XMLGregorianCalendar value) {
        this.jssj = value;
    }

    /**
     * Gets the value of the zl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZL() {
        return zl;
    }

    /**
     * Sets the value of the zl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZL(String value) {
        this.zl = value;
    }

    /**
     * Gets the value of the ajzt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAJZT() {
        return ajzt;
    }

    /**
     * Sets the value of the ajzt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAJZT(String value) {
        this.ajzt = value;
        this.ajzt=ConstHelper.getReportValueByValue("AJZT", ajzt);
    }

    /**
     * Gets the value of the slsj property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public String getSLSJ() {
        return slsj;
    }

    /**
     * Sets the value of the slsj property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSLSJ(String value) {
        this.slsj = value;
    }

    /**
     * Gets the value of the sfwtaj property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSFWTAJ() {
        return sfwtaj;
    }

    /**
     * Sets the value of the sfwtaj property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSFWTAJ(String value) {
        this.sfwtaj = value;
        this.sfwtaj=ConstHelper.getReportValueByValue("SFZD", sfwtaj);
    }

    /**
     * Gets the value of the djdl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDJDL() {
        return djdl;
    }

    /**
     * Sets the value of the djdl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDJDL(String value) {
        this.djdl = value;
        this.djdl=ConstHelper.getReportValueByValue("DJLX", djdl);
    }
    
    public String getXmbh() {
		return xmbh;
	}
	public void setXmbh(String xmbh) {
		this.xmbh = xmbh;
	}

    public void removeEmpty(){
    	if(StringHelper.isEmpty(this.ywh)){
    		this.ywh=null;
    	}
    	
    	if(StringHelper.isEmpty(this.tzrdzyj)){
    		this.tzrdzyj=null;
    	}
    	
    	if(StringHelper.isEmpty(this.tzfs)){
    		this.tzfs=null;
    	}
    	
    	if(StringHelper.isEmpty(this.slry)){
    		this.slry=null;
    	}
    	
    	if(StringHelper.isEmpty(this.tzrdh)){
    		this.tzrdh=null;
    	}
    	
    	if(StringHelper.isEmpty(this.bz)){
    		this.bz=null;
    	}
    	
    	if(StringHelper.isEmpty(this.ysdm)){
    		this.ysdm=null;
    	}
    	
    	if(StringHelper.isEmpty(this.djxl)){
    		this.djxl=null;
    	}
    	
    	if(StringHelper.isEmpty(this.tzrxm)){
    		this.tzrxm=null;
    	}
    	
    	if(StringHelper.isEmpty(this.tzryddh)){
    		this.tzryddh=null;
    	}
    	
    	if(StringHelper.isEmpty(this.qxdm)){
    		this.qxdm=null;
    	}
    	
    	if(StringHelper.isEmpty(this.jssj)){
    		this.jssj=null;
    	}
    	
    	if(StringHelper.isEmpty(this.zl)){
    		this.zl=null;
    	}
    	
    	if(StringHelper.isEmpty(this.ajzt)){
    		this.ajzt=null;
    	}
    	
    	if(StringHelper.isEmpty(this.slsj)){
    		this.slsj=null;
    	}
    	
    	if(StringHelper.isEmpty(this.sfwtaj)){
    		this.sfwtaj=null;
    	}
    	
    	if(StringHelper.isEmpty(this.djdl)){
    		this.djdl=null;
    	}
    }
}
