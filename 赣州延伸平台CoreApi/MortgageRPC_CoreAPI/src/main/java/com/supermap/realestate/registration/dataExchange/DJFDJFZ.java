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
 * 登记发证信息
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "DJF_DJ_FZ")
public class DJFDJFZ {

    @XmlAttribute(name = "FZSJ", required = true)
    protected String fzsj;
    @XmlAttribute(name = "LZRYB")
    protected String lzryb;
    @XmlAttribute(name = "YWH")
    protected String ywh;
    @XmlAttribute(name = "LZRZJHM", required = true)
    protected String lzrzjhm;
    @XmlAttribute(name = "FZSL", required = true)
    protected int fzsl;
    @XmlAttribute(name = "LZRDH")
    protected String lzrdh;
    @XmlAttribute(name = "LZRZJLB", required = true)
    protected String lzrzjlb;
    @XmlAttribute(name = "BZ")
    protected String bz;
    @XmlAttribute(name = "YSDM")
    protected String ysdm;
    @XmlAttribute(name = "HFZSH", required = true)
    protected String hfzsh;
    @XmlAttribute(name = "FZRY", required = true)
    protected String fzry;
    @XmlAttribute(name = "FZMC", required = true)
    protected String fzmc;
    @XmlAttribute(name = "QXDM")
    protected String qxdm;
    @XmlAttribute(name = "LZRDZ")
    protected String lzrdz;
    @XmlAttribute(name = "LZRXM", required = true)
    protected String lzrxm;
    @XmlAttribute(name = "XMBH")
	protected String xmbh;

    /**
     * Gets the value of the fzsj property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public String getFZSJ() {
        return fzsj;
    }

    /**
     * Sets the value of the fzsj property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFZSJ(String value) {
        this.fzsj = value;
    }

    /**
     * Gets the value of the lzryb property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLZRYB() {
        return lzryb;
    }

    /**
     * Sets the value of the lzryb property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLZRYB(String value) {
        this.lzryb = value;
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
     * Gets the value of the lzrzjhm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLZRZJHM() {
        return lzrzjhm;
    }

    /**
     * Sets the value of the lzrzjhm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLZRZJHM(String value) {
        this.lzrzjhm = value;
    }

    /**
     * Gets the value of the fzsl property.
     * 
     * @return
     *     possible object is
     *     {@link int }
     *     
     */
    public int getFZSL() {
        return fzsl;
    }

    /**
     * Sets the value of the fzsl property.
     * 
     * @param value
     *     allowed object is
     *     {@link int }
     *     
     */
    public void setFZSL(int value) {
        this.fzsl = value;
    }

    /**
     * Gets the value of the lzrdh property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLZRDH() {
        return lzrdh;
    }

    /**
     * Sets the value of the lzrdh property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLZRDH(String value) {
        this.lzrdh = value;
    }

    /**
     * Gets the value of the lzrzjlb property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLZRZJLB() {
        return lzrzjlb;
    }

    /**
     * Sets the value of the lzrzjlb property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLZRZJLB(String value) {
        this.lzrzjlb = value;
        this.lzrzjlb=ConstHelper.getReportValueByValue("ZJLX", lzrzjlb);
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
     * Gets the value of the hfzsh property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHFZSH() {
        return hfzsh;
    }

    /**
     * Sets the value of the hfzsh property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHFZSH(String value) {
        this.hfzsh = value;
    }

    /**
     * Gets the value of the fzry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFZRY() {
        return fzry;
    }

    /**
     * Sets the value of the fzry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFZRY(String value) {
        this.fzry = value;
    }

    /**
     * Gets the value of the fzmc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFZMC() {
        return fzmc;
    }

    /**
     * Sets the value of the fzmc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFZMC(String value) {
        this.fzmc = value;
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
     * Gets the value of the lzrdz property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLZRDZ() {
        return lzrdz;
    }

    /**
     * Sets the value of the lzrdz property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLZRDZ(String value) {
        this.lzrdz = value;
    }

    /**
     * Gets the value of the lzrxm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLZRXM() {
        return lzrxm;
    }

    /**
     * Sets the value of the lzrxm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLZRXM(String value) {
        this.lzrxm = value;
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
    	if(StringHelper.isEmpty(this.fzmc)){
    		this.fzmc=null;
    	}
    	if(StringHelper.isEmpty(this.fzry)){
    		this.fzry=null;
    	}
    	if(StringHelper.isEmpty(this.fzsj)){
    		this.fzsj=null;
    	}
    	if(StringHelper.isEmpty(this.hfzsh)){
    		this.hfzsh=null;
    	}
    	if(StringHelper.isEmpty(this.lzrdh)){
    		this.lzrdh=null;
    	}
    	if(StringHelper.isEmpty(this.lzrxm)){
    		this.lzrxm=null;
    	}
    	if(StringHelper.isEmpty(this.lzryb)){
    		this.lzryb=null;
    	}
    	if(StringHelper.isEmpty(this.lzrzjhm)){
    		this.lzrzjhm=null;
    	}
    	if(StringHelper.isEmpty(this.lzrzjlb)){
    		this.lzrzjlb=null;
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
    }
}
