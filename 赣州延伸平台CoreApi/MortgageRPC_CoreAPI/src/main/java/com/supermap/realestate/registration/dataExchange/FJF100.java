package com.supermap.realestate.registration.dataExchange;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.supermap.realestate.registration.util.StringHelper;

/**
 * 
 * 非结构化文档
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "fjmc", "fjlx", "fjnr" })
@XmlRootElement(name = "FJ_F_100")
public class FJF100 {

	@XmlAttribute(name = "FJMC", required = true)
	protected String fjmc;
	@XmlAttribute(name = "FJNR", required = true)
	protected String fjnr;
	@XmlAttribute(name = "FJLX", required = true)
	protected String fjlx;

	/**
	 * Gets the value of the fjmc property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFJMC() {
		return fjmc;
	}

	/**
	 * Sets the value of the fjmc property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setFJMC(String value) {
		this.fjmc = value;
	}

	/**
	 * Gets the value of the fjnr property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFJNR() {
		return fjnr;
	}

	/**
	 * Sets the value of the fjnr property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setFJNR(String value) {
		this.fjnr = value;
	}

	/**
	 * Gets the value of the fjlx property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFJLX() {
		return fjlx;
	}

	/**
	 * Sets the value of the fjlx property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setFJLX(String value) {
		this.fjlx = value;
	}

	public void replaceEmpty(){
		if(StringHelper.isEmpty(this.fjlx)){
			this.fjlx=null;
		}
		if(StringHelper.isEmpty(this.fjmc)){
			this.fjmc=null;
		}
		if(StringHelper.isEmpty(this.fjnr)){
			this.fjnr=null;
		}
	}
}
