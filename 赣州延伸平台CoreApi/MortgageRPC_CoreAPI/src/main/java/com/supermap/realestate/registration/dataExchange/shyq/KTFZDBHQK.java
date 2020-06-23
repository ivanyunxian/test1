package com.supermap.realestate.registration.dataExchange.shyq;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

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
 *       &lt;attribute ref="{}DBR"/>
 *       &lt;attribute ref="{}ZDDM"/>
 *       &lt;attribute ref="{}QXDM"/>
 *       &lt;attribute ref="{}BHYY use="required""/>
 *       &lt;attribute ref="{}FJ"/>
 *       &lt;attribute ref="{}DJSJ"/>
 *       &lt;attribute ref="{}BHNR use="required""/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 *   宗地变化情况表  
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "KTF_ZDBHQK")
public class KTFZDBHQK {

    @XmlAttribute(name = "DBR", required = true)
    protected String dbr;
    @XmlAttribute(name = "ZDDM", required = true)
    protected String zddm;
    @XmlAttribute(name = "QXDM", required = true)
    protected String qxdm;
    @XmlAttribute(name = "BHYY", required = true)
    protected String bhyy;
    @XmlAttribute(name = "FJ")
    protected String fj;
    @XmlAttribute(name = "DJSJ", required = true)
    protected String djsj;
    @XmlAttribute(name = "BHNR", required = true)
    protected String bhnr;

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
     * Gets the value of the bhyy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBHYY() {
        return bhyy;
    }

    /**
     * Sets the value of the bhyy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBHYY(String value) {
        this.bhyy = value;
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
     * Gets the value of the bhnr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBHNR() {
        return bhnr;
    }

    /**
     * Sets the value of the bhnr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBHNR(String value) {
        this.bhnr = value;
    }
   public void replaceEmpty(){
	   if(StringHelper.isEmpty(this.bhnr)){
		   this.bhnr=null;
	   }
	   if(StringHelper.isEmpty(this.bhyy)){
		   this.bhyy=null;
	   }
	   if(StringHelper.isEmpty(this.dbr)){
		   this.dbr=null;
	   }
	   if(StringHelper.isEmpty(this.djsj)){
		   this.djsj=null;
	   }
	   if(StringHelper.isEmpty(this.fj)){
		   this.qxdm=null;
	   }
	   if(StringHelper.isEmpty(this.zddm)){
		   this.zddm=null;
	   }
   }
}
