package com.supermap.realestate.registration.dataExchange;
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
 * 登记归档信息
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "DJF_DJ_GD")
public class DJFDJGD {

    @XmlAttribute(name = "DJXL")
    protected String djxl;
    @XmlAttribute(name = "YWH")
    protected String ywh;
    @XmlAttribute(name = "QXDM")
    protected String qxdm;
//    @XmlAttribute(name = "JZH", required = true)
//    protected String jzh;
    @XmlAttribute(name = "GDRY")
    protected String gdry;
    @XmlAttribute(name = "ZYS")
    protected int zys;
    @XmlAttribute(name = "GDSJ", required = true)
    protected String gdsj;
    @XmlAttribute(name = "ZL")
    protected String zl;
    @XmlAttribute(name = "WJJS", required = true)
    protected int wjjs;
    @XmlAttribute(name = "QZHM", required = true)
    protected String qzhm;
    @XmlAttribute(name = "BZ")
    protected String bz;
    @XmlAttribute(name = "YSDM")
    protected String ysdm;
    @XmlAttribute(name = "DJDL")
    protected String djdl;
    @XmlAttribute(name = "XMBH")
	protected String xmbh;

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
     * Gets the value of the jzh property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
//    public String getJZH() {
//        return jzh;
//    }

    /**
     * Sets the value of the jzh property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
//    public void setJZH(String value) {
//        this.jzh = value;
//    }

    /**
     * Gets the value of the gdry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGDRY() {
        return gdry;
    }

    /**
     * Sets the value of the gdry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGDRY(String value) {
        this.gdry = value;
    }

    /**
     * Gets the value of the zys property.
     * 
     * @return
     *     possible object is
     *     {@link int }
     *     
     */
    public int getZYS() {
        return zys;
    }

    /**
     * Sets the value of the zys property.
     * 
     * @param value
     *     allowed object is
     *     {@link int }
     *     
     */
    public void setZYS(int value) {
        this.zys = value;
    }

    /**
     * Gets the value of the gdsj property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public String getGDSJ() {
        return gdsj;
    }

    /**
     * Sets the value of the gdsj property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setGDSJ(String value) {
        this.gdsj = value;
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
     * Gets the value of the wjjs property.
     * 
     * @return
     *     possible object is
     *     {@link int }
     *     
     */
    public int getWJJS() {
        return wjjs;
    }

    /**
     * Sets the value of the wjjs property.
     * 
     * @param value
     *     allowed object is
     *     {@link int }
     *     
     */
    public void setWJJS(int value) {
        this.wjjs = value;
    }

    /**
     * Gets the value of the qzhm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQZHM() {
        return qzhm;
    }

    /**
     * Sets the value of the qzhm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQZHM(String value) {
        this.qzhm = value;
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

    public void replaceEmpty(){
    	if(StringHelper.isEmpty(this.bz)){
    		this.bz=null;
    	}
    	if(StringHelper.isEmpty(this.djdl)){
    		this.djdl=null;
    	}
    	if(StringHelper.isEmpty(this.djxl)){
    		this.djxl=null;
    	}
    	if(StringHelper.isEmpty(this.gdry)){
    		this.gdry=null;
    	}
    	if(StringHelper.isEmpty(this.gdsj)){
    		this.gdsj=null;
    	}
    	if(StringHelper.isEmpty(this.qxdm)){
    		this.qxdm=null;
    	}
    	if(StringHelper.isEmpty(this.qzhm)){
    		this.qzhm=null;
    	}
    	if(StringHelper.isEmpty(this.ysdm)){
    		this.ysdm=null;
    	}
    	if(StringHelper.isEmpty(this.ywh)){
    		this.ywh=null;
    	}
    	if(StringHelper.isEmpty(this.zl)){
    		this.zl=null;
    	}
    }
}
