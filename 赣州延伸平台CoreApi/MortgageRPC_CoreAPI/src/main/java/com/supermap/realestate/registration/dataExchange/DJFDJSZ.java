package com.supermap.realestate.registration.dataExchange;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import com.supermap.realestate.registration.util.StringHelper;


/**
 * 
 * 登记缮证信息
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "DJF_DJ_SZ")
public class DJFDJSZ {

    @XmlAttribute(name = "YWH")
    protected String ywh;
    @XmlAttribute(name = "SZMC", required = true)
    protected String szmc;
    @XmlAttribute(name = "YSXLH", required = true)
    protected String ysxlh;
    @XmlAttribute(name = "QXDM")
    protected String qxdm;
    @XmlAttribute(name = "SZZH", required = true)
    protected String szzh;
    @XmlAttribute(name = "SZSJ")
    protected String szsj;
    @XmlAttribute(name = "BZ")
    protected String bz;
    @XmlAttribute(name = "SZRY", required = true)
    protected String szry;
    @XmlAttribute(name = "YSDM")
    protected String ysdm;
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
     * Gets the value of the szmc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSZMC() {
        return szmc;
    }

    /**
     * Sets the value of the szmc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSZMC(String value) {
        this.szmc = value;
    }

    /**
     * Gets the value of the ysxlh property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getYSXLH() {
        return ysxlh;
    }

    /**
     * Sets the value of the ysxlh property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setYSXLH(String value) {
        this.ysxlh = value;
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
     * Gets the value of the szzh property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSZZH() {
        return szzh;
    }

    /**
     * Sets the value of the szzh property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSZZH(String value) {
        this.szzh = value;
    }

    /**
     * Gets the value of the szsj property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public String getSZSJ() {
        return szsj;
    }

    /**
     * Sets the value of the szsj property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSZSJ(String value) {
        this.szsj = value;
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
     * Gets the value of the szry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSZRY() {
        return szry;
    }

    /**
     * Sets the value of the szry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSZRY(String value) {
        this.szry = value;
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
    
    public String getXmbh() {
		return xmbh;
	}
	public void setXmbh(String xmbh) {
		this.xmbh = xmbh;
	}

    public void replayEmpty(){
    	if(StringHelper.isEmpty(this.bz)){
    		this.bz=null;
    	}
    	if(StringHelper.isEmpty(this.qxdm)){
    		this.qxdm=null;
    	}
    	if(StringHelper.isEmpty(this.szmc)){
    		this.szmc=null;
    	}
    	if(StringHelper.isEmpty(this.szry)){
    		this.szry=null;
    	}
    	if(StringHelper.isEmpty(this.szsj)){
    		this.szsj=null;
    	}
    	if(StringHelper.isEmpty(this.szzh)){
    		this.szzh=null;
    	}
    	if(StringHelper.isEmpty(this.ysdm)){
    		this.ysdm=null;
    	}
    	if(StringHelper.isEmpty(this.ysxlh)){
    		this.ysxlh=null;
    	}
    	if(StringHelper.isEmpty(this.ywh)){
    		this.ywh=null;
    	}
    }
}
