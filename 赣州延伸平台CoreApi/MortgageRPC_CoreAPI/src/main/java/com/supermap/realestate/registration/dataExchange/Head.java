package com.supermap.realestate.registration.dataExchange;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"bizMsgID", "asid","areaCode","isRoutine","recType","rightType",
		"regType","createDate","recFlowID","regOrgID","parcelID","estateNum","mark","preestatenum",
		"preCertId","preProofId","certCount","proofCount","prebdcdyid","preqlid"
})
@XmlRootElement(name = "Head")
public class Head {

	@XmlElement(name = "RegType", required = true)
    protected String regType;
    @XmlElement(name = "ASID", required = true)
    protected String asid;
    @XmlElement(name = "RightType", required = true)
    protected String rightType;
    @XmlElement(name = "RegOrgID", required = true)
    protected String regOrgID;
    @XmlElement(name = "CreateDate", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createDate;
    @XmlElement(name = "RecType", required = true)
    protected String recType;
    @XmlElement(name = "AreaCode", required = true)
    protected String areaCode;
    @XmlElement(name = "Mark", required = true)
    protected String mark;
    @XmlElement(name = "BizMsgID", required = true)
    protected String bizMsgID;
    @XmlElement(name = "ParcelID", required = true)
    protected String parcelID;
    @XmlElement(name = "RecFlowID", required = true)
    protected String recFlowID;
    @XmlElement(name = "IsRoutine", required = true)
    protected String isRoutine;
    @XmlElement(name = "EstateNum", required = true)
    protected String estateNum;
    @XmlElement(name = "PreEstateNum")
    protected String preestatenum;
    @XmlElement(name = "PreCertID")
    protected String preCertId;
    @XmlElement(name = "PreProofID")
    protected String preProofId;
    @XmlElement(name = "CertCount")
    protected int certCount;
    @XmlElement(name = "ProofCount")
    protected int proofCount;
    @XmlElement(name = "PreBdcdyid")
    protected String prebdcdyid;
    @XmlElement(name = "PreQlid")
    protected String preqlid;
    
    
    public String getPreCertId() {
		return preCertId;
	}

	public void setPreCertId(String preCertId) {
		this.preCertId = preCertId;
	}

	public String getPreProofId() {
		return preProofId;
	}

	public void setPreProofId(String preProofId) {
		this.preProofId = preProofId;
	}

	public int getCertCount() {
		return certCount;
	}

	public void setCertCount(int certCount) {
		this.certCount = certCount;
	}

	public int getProofCount() {
		return proofCount;
	}

	public void setProofCount(int proofCount) {
		this.proofCount = proofCount;
	}

	public String getPreEstateNum() {
        return preestatenum;
    }

    public void setPreEstateNum(String value) {
        this.preestatenum = value;
    }

    /**
     * Gets the value of the regType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegType() {
        return regType;
    }

    /**
     * Sets the value of the regType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegType(String value) {
        this.regType = value;
    }

    /**
     * Gets the value of the asid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getASID() {
        return asid;
    }

    /**
     * Sets the value of the asid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setASID(String value) {
        this.asid = value;
    }

    /**
     * Gets the value of the rightType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRightType() {
        return rightType;
    }

    /**
     * Sets the value of the rightType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRightType(String value) {
        this.rightType = value;
    }

    /**
     * Gets the value of the regOrgID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegOrgID() {
        return regOrgID;
    }

    /**
     * Sets the value of the regOrgID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegOrgID(String value) {
        this.regOrgID = value;
    }

    /**
     * Gets the value of the createDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreateDate() {
        return createDate;
    }

    /**
     * Sets the value of the createDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreateDate(XMLGregorianCalendar value) {
        this.createDate = value;
    }

    /**
     * Gets the value of the recType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecType() {
        return recType;
    }

    /**
     * Sets the value of the recType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecType(String value) {
        this.recType = value;
    }

    /**
     * Gets the value of the areaCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAreaCode() {
        return areaCode;
    }

    /**
     * Sets the value of the areaCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAreaCode(String value) {
        this.areaCode = value;
    }

    /**
     * Gets the value of the mark property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMark() {
        return mark;
    }

    /**
     * Sets the value of the mark property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMark(String value) {
        this.mark = value;
    }

    /**
     * Gets the value of the bizMsgID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBizMsgID() {
        return bizMsgID;
    }

    /**
     * Sets the value of the bizMsgID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBizMsgID(String value) {
        this.bizMsgID = value;
    }

    /**
     * Gets the value of the parcelID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParcelID() {
        return parcelID;
    }

    /**
     * Sets the value of the parcelID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParcelID(String value) {
        this.parcelID = value;
    }

    /**
     * Gets the value of the recFlowID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecFlowID() {
        return recFlowID;
    }

    /**
     * Sets the value of the recFlowID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecFlowID(String value) {
        this.recFlowID = value;
    }

    /**
     * Gets the value of the isRoutine property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsRoutine() {
        return isRoutine;
    }

    /**
     * Sets the value of the isRoutine property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsRoutine(String value) {
        this.isRoutine = value;
    }

    /**
     * Gets the value of the estateNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstateNum() {
        return estateNum;
    }

    /**
     * Sets the value of the estateNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstateNum(String value) {
        this.estateNum = value;
    }
    
    public String getPreBdcdyid() {
        return prebdcdyid;
    }

    public void setPreBdcdyid(String value) {
        this.prebdcdyid = value;
    }
    
    public String getPreQlid() {
        return preqlid;
    }

    public void setPreQlid(String value) {
        this.preqlid = value;
    }

}
