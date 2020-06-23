package com.supermap.realestate.registration.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "LOG_SMS_TEMPLATE",schema = "LOG")
public class LOG_SMS_TEMPLATE implements Serializable{

	private static final long serialVersionUID = 1L;
	private String TEMPLATEID;
	private String TEMPLATENAME;
	private String TEMPLATECONTENT;
	private Date CREATETIME;
	private String YXBZ;
	private String BZ;
	private String TENANT_ID;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	@Id
	@Column(name = "TEMPLATE_ID")
	public String getTEMPLATEID() {
		return TEMPLATEID;
	}

	public void setTEMPLATEID(String TEMPLATEID) {
		this.TEMPLATEID = TEMPLATEID;
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

	@Column(name = "CREATETIME")
	public Date getCREATETIME() {
		return CREATETIME;
	}

	public void setCREATETIME(Date CREATETIME) {
		this.CREATETIME = CREATETIME;
	}

	@Column(name = "YXBZ")
	public String getYXBZ() {
		return YXBZ;
	}

	public void setYXBZ(String YXBZ) {
		this.YXBZ = YXBZ;
	}

	@Column(name = "BZ")
	public String getBZ() {
		return BZ;
	}

	public void setBZ(String BZ) {
		this.BZ = BZ;
	}

	@Column(name = "TENANT_ID")
	public String getTENANT_ID() {
		return TENANT_ID;
	}

	public void setTENANT_ID(String TENANT_ID) {
		this.TENANT_ID = TENANT_ID;
	}
}
