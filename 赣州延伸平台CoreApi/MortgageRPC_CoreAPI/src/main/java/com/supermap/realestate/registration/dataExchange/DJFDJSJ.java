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
 * 登记收件信息
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "DJF_DJ_SJ")
public class DJFDJSJ {

    @XmlAttribute(name = "SFSYBZ")
    protected String sfsybz;
    @XmlAttribute(name = "YWH", required = true)
    protected String ywh;
    @XmlAttribute(name = "QXDM")
    protected String qxdm;
    @XmlAttribute(name = "SFEWSJ")
    protected String sfewsj;
    @XmlAttribute(name = "SJMC", required = true)
    protected String sjmc;
    @XmlAttribute(name = "SJSL", required = true)
    protected int sjsl;
    @XmlAttribute(name = "SJLX", required = true)
    protected String sjlx;
    @XmlAttribute(name = "SJSJ")
    protected String sjsj;
    @XmlAttribute(name = "BZ")
    protected String bz;
    @XmlAttribute(name = "YS", required = true)
    protected int ys;
    @XmlAttribute(name = "YSDM")
    protected String ysdm;
    @XmlAttribute(name = "SFBCSJ")
    protected String sfbcsj;
    @XmlAttribute(name = "SFSJSY")
    protected String sfsjsy;
    @XmlAttribute(name = "XMBH")
	protected String xmbh;
    
    public String getSFSJSY() {
        return sfsjsy;
    }
 
    public void setSFSJSY(String value) {
        this.sfsjsy = value;
        this.sfsjsy=ConstHelper.getReportValueByValue("SFZD", sfsjsy);
    }

    /**
     * Gets the value of the sfsybz property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSFSYBZ() {
        return sfsybz;
    }

    /**
     * Sets the value of the sfsybz property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSFSYBZ(String value) {
        this.sfsybz = value;
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
     * Gets the value of the sfewsj property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSFEWSJ() {
        return sfewsj;
    }

    /**
     * Sets the value of the sfewsj property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSFEWSJ(String value) {
        this.sfewsj = value;
        this.sfewsj=ConstHelper.getReportValueByValue("SFZD", sfewsj);
    }

    /**
     * Gets the value of the sjmc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSJMC() {
        return sjmc;
    }

    /**
     * Sets the value of the sjmc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSJMC(String value) {
        this.sjmc = value;
    }

    /**
     * Gets the value of the sjsl property.
     * 
     * @return
     *     possible object is
     *     {@link int }
     *     
     */
    public int getSJSL() {
        return sjsl;
    }

    /**
     * Sets the value of the sjsl property.
     * 
     * @param value
     *     allowed object is
     *     {@link int }
     *     
     */
    public void setSJSL(int value) {
        this.sjsl = value;
    }

    /**
     * Gets the value of the sjlx property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSJLX() {
        return sjlx;
    }

    /**
     * Sets the value of the sjlx property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSJLX(String value) {
        this.sjlx = value;
        this.sjlx=ConstHelper.getReportValueByValue("SJLX", sjlx);
    }

    /**
     * Gets the value of the sjsj property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public String getSJSJ() {
        return sjsj;
    }

    /**
     * Sets the value of the sjsj property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSJSJ(String value) {
        this.sjsj = value;
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
     * Gets the value of the ys property.
     * 
     * @return
     *     possible object is
     *     {@link int }
     *     
     */
    public int getYS() {
        return ys;
    }

    /**
     * Sets the value of the ys property.
     * 
     * @param value
     *     allowed object is
     *     {@link int }
     *     
     */
    public void setYS(int value) {
        this.ys = value;
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
     * Gets the value of the sfbcsj property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSFBCSJ() {
        return sfbcsj;
    }

    /**
     * Sets the value of the sfbcsj property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSFBCSJ(String value) {
        this.sfbcsj = value;
        this.sfbcsj=ConstHelper.getReportValueByValue("SFZD", sfbcsj);
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
    	if(StringHelper.isEmpty(this.sfbcsj)){
    		this.sfbcsj=null;
    	}
    	if(StringHelper.isEmpty(this.sfewsj)){
    		this.sfewsj=null;
    	}
    	if(StringHelper.isEmpty(this.sfsjsy)){
    		this.sfsjsy=null;
    	}
    	if(StringHelper.isEmpty(this.sfsybz)){
    		this.sfsybz=null;
    	}
    	if(StringHelper.isEmpty(this.sjlx)){
    		this.sjlx=null;
    	}
    	if(StringHelper.isEmpty(this.sjmc)){
    		this.sjmc=null;
    	}
    	if(StringHelper.isEmpty(this.sjsj)){
    		this.sjsj=null;
    	}
    	if(StringHelper.isEmpty(this.ysdm)){
    		this.ysdm=null;
    	}
    	if(StringHelper.isEmpty(this.ywh)){
    		this.ywh=null;
    	}
    }
}
