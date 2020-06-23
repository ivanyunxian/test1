package com.supermap.realestate.registration.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Administrator
 *
 */
@Entity
@Table(name = "SMS_PARAM_TEMPLATE", schema = "BDCK")
public class SMS_PARAM_TEMPLATE implements Serializable{

	private static final long serialVersionUID = 1L;
	private String ID;
	private String CONTENT;
	private String STAFF_ID;
	private Long XH;
	private String TENANTID;
	private String YXBZ;
	private Date CREATEDATE;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	@Id
	@Column(name = "ID")
	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	@Column(name = "CONTENT")
	public String getCONTENT() {
		return CONTENT;
	}

	public void setCONTENT(String CONTENT) {
		this.CONTENT = CONTENT;
	}

	@Column(name = "STAFF_ID")
	public String getSTAFF_ID() {
		return STAFF_ID;
	}

	public void setSTAFF_ID(String STAFF_ID) {
		this.STAFF_ID = STAFF_ID;
	}

	@Column(name = "XH")
	public Long getXH() {
		return XH;
	}

	public void setXH(Long XH) {
		this.XH = XH;
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
