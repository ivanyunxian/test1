package com.supermap.realestate.registration.dataExchange.shyq;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.supermap.realestate.registration.util.StringHelper;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute ref="{}YZB use="required""/>
 *       &lt;attribute ref="{}XZB use="required""/>
 *       &lt;attribute ref="{}ZDX use="required""/>
 *       &lt;attribute ref="{}XH use="required""/>
 *       &lt;attribute ref="{}BDCDYH"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "ZD_K_103")
public class ZDK103 {

	@XmlAttribute(name = "YZB", required = true)
	protected String yzb;
	@XmlAttribute(name = "XZB", required = true)
	protected String xzb;
	@XmlAttribute(name = "ZDX", required = true)
	protected int zdx;
	@XmlAttribute(name = "XH", required = true)
	protected int xh;
	@XmlAttribute(name = "BDCDYH")
	protected String bdcdyh;

	/**
	 * Gets the value of the yzb property.
	 * 
	 * @return possible object is {@link double }
	 * 
	 */
	public String getYZB() {
		return yzb;
	}

	/**
	 * Sets the value of the yzb property.
	 * 
	 * @param value
	 *            allowed object is {@link double }
	 * 
	 */
	public void setYZB(String value) {
		this.yzb = value;
	}

	/**
	 * Gets the value of the xzb property.
	 * 
	 * @return possible object is {@link double }
	 * 
	 */
	public String getXZB() {
		return xzb;
	}

	/**
	 * Sets the value of the xzb property.
	 * 
	 * @param value
	 *            allowed object is {@link double }
	 * 
	 */
	public void setXZB(String value) {
		this.xzb = value;
	}

	/**
	 * Gets the value of the zdx property.
	 * 
	 * @return possible object is {@link int }
	 * 
	 */
	public int getZDX() {
		return zdx;
	}

	/**
	 * Sets the value of the zdx property.
	 * 
	 * @param value
	 *            allowed object is {@link int }
	 * 
	 */
	public void setZDX(int value) {
		this.zdx = value;
	}

	/**
	 * Gets the value of the xh property.
	 * 
	 * @return possible object is {@link int }
	 * 
	 */
	public int getXH() {
		return xh;
	}

	/**
	 * Sets the value of the xh property.
	 * 
	 * @param value
	 *            allowed object is {@link int }
	 * 
	 */
	public void setXH(int value) {
		this.xh = value;
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
	public void replaceEmpty(){
		if(StringHelper.isEmpty(this.bdcdyh)){
			this.bdcdyh=null;
		}
		if(StringHelper.isEmpty(this.xzb)){
			this.xzb=null;
		}
		if(StringHelper.isEmpty(this.yzb)){
			this.yzb=null;
		}
	}

}
