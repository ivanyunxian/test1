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
@Table(name = "SMS_TEMPLATE", schema = "BDCK")
public class SMS_TEMPLATE implements Serializable{

	private static final long serialVersionUID = 1L;
	private String TEMPLATEID;
	private String CLASSID;
	private String TEMPLATECODE;
	private String TEMPLATENAME;
	private String TEMPLATECONTENT;
	private Long TEMPLATEINDEX;
	private String TEMPLATETYPE;
	private String TEMPLATEDESC;
	private String TENANTID;
	private String YXBZ;
	private Date CREATEDATE;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	@Id
	@Column(name = "TEMPLATE_ID")
	public String getTEMPLATEID() {
		if (TEMPLATEID == null || "".equals(TEMPLATEID)) {
			TEMPLATEID = UUID.randomUUID().toString().replace("-", "");
		}
		return TEMPLATEID;
	}

	public void setTEMPLATEID(String TEMPLATEID) {
		this.TEMPLATEID = TEMPLATEID;
	}

	@Column(name = "CLASS_ID")
	public String getCLASSID() {
		return CLASSID;
	}

	public void setCLASSID(String CLASSID) {
		this.CLASSID = CLASSID;
	}

	@Column(name = "TEMPLATE_CODE")
		public String getTEMPLATECODE() {
		return TEMPLATECODE;
	}

	public void setTEMPLATECODE(String TEMPLATECODE) {
		this.TEMPLATECODE = TEMPLATECODE;
	}

	@Column(name = "TEMPLATE_NAME")
	public String getTEMPLATENAME() {
		return TEMPLATENAME;
	}

	public void setTEMPLATENAME(String TEMPLATENAME) {
		this.TEMPLATENAME = TEMPLATENAME;
	}

	@Column(name = "TEMPLATE_CONTENT")
	public String getTEMPLATECONTENT() {
		return TEMPLATECONTENT;
	}

	public void setTEMPLATECONTENT(String TEMPLATECONTENT) {
		this.TEMPLATECONTENT = TEMPLATECONTENT;
	}

	@Column(name = "TEMPLATE_INDEX")
	public Long getTEMPLATEINDEX() {
		if (TEMPLATEINDEX == null || "".equals(TEMPLATEINDEX)) {
			TEMPLATEINDEX = System.currentTimeMillis();
		}
		return TEMPLATEINDEX;
	}

	public void setTEMPLATEINDEX(Long TEMPLATEINDEX) {
		this.TEMPLATEINDEX = TEMPLATEINDEX;
	}

	@Column(name = "TEMPLATE_TYPE")
	public String getTEMPLATETYPE() {
		if (TEMPLATETYPE == null || "".equals(TEMPLATETYPE)) {
			TEMPLATETYPE = "1";
		}
		return TEMPLATETYPE;
	}

	public void setTEMPLATETYPE(String TEMPLATETYPE) {
		this.TEMPLATETYPE = TEMPLATETYPE;
	}

	@Column(name = "TEMPLATE_DESC")
	public String getTEMPLATEDESC() {
		return TEMPLATEDESC;
	}

	public void setTEMPLATEDESC(String TEMPLATEDESC) {
		this.TEMPLATEDESC = TEMPLATEDESC;
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
