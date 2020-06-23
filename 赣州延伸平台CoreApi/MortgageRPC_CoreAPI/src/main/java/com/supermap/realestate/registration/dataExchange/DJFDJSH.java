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
 * 登记审核信息
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "DJF_DJ_SH")
public class DJFDJSH {

    @XmlAttribute(name = "SHYJ", required = true)
    protected String shyj;
    @XmlAttribute(name = "YWH")
    protected String ywh;
    @XmlAttribute(name = "SHJSSJ", required = true)
    protected String shjssj;
    @XmlAttribute(name = "JDMC")
    protected String jdmc;
    @XmlAttribute(name = "QXDM")
    protected String qxdm;
    @XmlAttribute(name = "CZJG", required = true)
    protected String czjg;
    @XmlAttribute(name = "SXH", required = true)
    protected int sxh;
    @XmlAttribute(name = "SHRYXM", required = true)
    protected String shryxm;
    @XmlAttribute(name = "SHKSSJ", required = true)
    protected String shkssj;
    @XmlAttribute(name = "YSDM")
    protected String ysdm;
    @XmlAttribute(name = "XMBH")
	protected String xmbh;

    /**
     * Gets the value of the shyj property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSHYJ() {
        return shyj;
    }

    /**
     * Sets the value of the shyj property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSHYJ(String value) {
        this.shyj = value;
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
     * Gets the value of the shjssj property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public String getSHJSSJ() {
        return shjssj;
    }

    /**
     * Sets the value of the shjssj property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSHJSSJ(String value) {
        this.shjssj = value;
    }

    /**
     * Gets the value of the jdmc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJDMC() {
        return jdmc;
    }

    /**
     * Sets the value of the jdmc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJDMC(String value) {
        this.jdmc = value;
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
     * Gets the value of the czjg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCZJG() {
        return czjg;
    }

    /**
     * Sets the value of the czjg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCZJG(String value) {
        this.czjg = value;
        this.czjg=ConstHelper.getReportValueByValue("SHYJCZJG", czjg);
    }

    public int getSXH() {
        return sxh;
    }

    public void setSXH(int value) {
        this.sxh = value;
    }

    /**
     * Gets the value of the shryxm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSHRYXM() {
        return shryxm;
    }

    /**
     * Sets the value of the shryxm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSHRYXM(String value) {
        this.shryxm = value;
    }

    /**
     * Gets the value of the shkssj property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public String getSHKSSJ() {
        return shkssj;
    }

    /**
     * Sets the value of the shkssj property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSHKSSJ(String value) {
        this.shkssj = value;
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

    public void replaceEmpty(){
    	if(StringHelper.isEmpty(this.czjg)){
    		this.czjg=null;
    	}
    	if(StringHelper.isEmpty(this.jdmc)){
    		this.jdmc=null;
    	}
    	if(StringHelper.isEmpty(this.qxdm)){
    		this.qxdm=null;
    	}
    	if(StringHelper.isEmpty(this.shjssj)){
    		this.shjssj=null;
    	}
    	if(StringHelper.isEmpty(this.shkssj)){
    		this.shkssj=null;
    	}
    	if(StringHelper.isEmpty(this.shryxm)){
    		this.shryxm=null;
    	}
    	if(StringHelper.isEmpty(this.shyj)){
    		this.shyj=null;
    	}
    	if(StringHelper.isEmpty(this.ysdm)){
    		this.ysdm=null;
    	}
    	if(StringHelper.isEmpty(this.ywh)){
    		this.ywh=null;
    	}
    }
}
