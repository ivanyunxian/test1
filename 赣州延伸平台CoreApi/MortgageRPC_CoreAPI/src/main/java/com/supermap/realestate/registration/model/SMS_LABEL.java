package com.supermap.realestate.registration.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * @author Administrator
 *
 */
@Entity
@Table(name = "SMS_LABEL", schema = "BDCK")
public class SMS_LABEL implements Serializable{

	private static final long serialVersionUID = 1L;
	private String LABELID;
	private String CLASSID;
	private String LABELNAME;
	private String LABELCONTENT;
	private Long LABELINDEX;
	private String LABELTYPE;
	private String LABELDESC;
	private String TENANTID;
	private String YXBZ;
	private Date CREATEDATE;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	@Id
	@Column(name = "LABEL_ID")
	public String getLABELID() {
		if (LABELID == null || "".equals(LABELID)) {
			LABELID = UUID.randomUUID().toString().replace("-", "");
		}
		return LABELID;
	}

	public void setLABELID(String LABELID) {
		this.LABELID = LABELID;
	}

	@Column(name = "CLASS_ID")
	public String getCLASSID() {
		return CLASSID;
	}

	public void setCLASSID(String CLASSID) {
		this.CLASSID = CLASSID;
	}

	@Column(name = "LABEL_NAME")
	public String getLABELNAME() {
		return LABELNAME;
	}

	public void setLABELNAME(String LABELNAME) {
		this.LABELNAME = LABELNAME;
	}

	@Column(name = "LABEL_CONTENT")
	public String getLABELCONTENT() {
		return LABELCONTENT;
	}

	public void setLABELCONTENT(String LABELCONTENT) {
		this.LABELCONTENT = LABELCONTENT;
	}

	@Column(name = "LABEL_INDEX")
	public Long getLABELINDEX() {
		if (LABELINDEX == null || "".equals(LABELINDEX)) {
			LABELINDEX = System.currentTimeMillis();
		}
		return LABELINDEX;
	}

	public void setLABELINDEX(Long LABELINDEX) {
		this.LABELINDEX = LABELINDEX;
	}

	@Column(name = "LABEL_TYPE")
	public String getLABELTYPE() {
		if (LABELTYPE == null || "".equals(LABELTYPE)) {
			LABELTYPE = "2";
		}
		return LABELTYPE;
	}

	public void setLABELTYPE(String LABELTYPE) {
		this.LABELTYPE = LABELTYPE;
	}

	@Column(name = "LABEL_DESC")
	public String getLABELDESC() {
		return LABELDESC;
	}

	public void setLABELDESC(String LABELDESC) {
		this.LABELDESC = LABELDESC;
	}

	@Column(name = "TENANT_ID")
	public String getTENANTID() {
		return TENANTID;
	}

	public void setTENANTID(String TENANTID) {
		this.TENANTID = TENANTID;
	}

	@Column(name = "YXBZ")
	public String getYXBZ() {
		if (YXBZ == null || "".equals(YXBZ)) {
			YXBZ = "0";
		}
		return YXBZ;
	}

	public void setYXBZ(String YXBZ) {
		this.YXBZ = YXBZ;
	}

	@Column(name = "CREATEDATE")
	public Date getCREATEDATE() {
		if (CREATEDATE == null || "".equals(CREATEDATE)) {
			CREATEDATE = new Date();
		}
		return CREATEDATE;
	}

	public void setCREATEDATE(Date CREATEDATE) {
		this.CREATEDATE = CREATEDATE;
	}
}
