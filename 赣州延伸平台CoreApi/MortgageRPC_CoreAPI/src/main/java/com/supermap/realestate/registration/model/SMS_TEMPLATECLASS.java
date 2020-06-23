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
@Table(name = "SMS_TEMPLATECLASS", schema = "BDCK")
public class SMS_TEMPLATECLASS implements Serializable{

	private static final long serialVersionUID = 1L;
	private String CLASSID;
	private String CLASSPID;
	private String CLASSNAME;
	private Long CLASSINDEX;
	private String CLASSTYPE;
	private String CLASSDESC;
	private String TENANTID;
	private String YXBZ;
	private Date CREATEDATE;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	@Id
	@Column(name = "CLASS_ID")
	public String getCLASSID() {
		if (CLASSID == null || "".equals(CLASSID)) {
			CLASSID = UUID.randomUUID().toString().replace("-", "");
		}
		return CLASSID;
	}

	public void setCLASSID(String CLASSID) {
		this.CLASSID = CLASSID;
	}

	@Column(name = "CLASS_PID")
	public String getCLASSPID() {
		return CLASSPID;
	}

	public void setCLASSPID(String CLASSPID) {
		this.CLASSPID = CLASSPID;
	}

	@Column(name = "CLASS_NAME")
	public String getCLASSNAME() {
		return CLASSNAME;
	}

	public void setCLASSNAME(String CLASSNAME) {
		this.CLASSNAME = CLASSNAME;
	}

	@Column(name = "CLASS_INDEX")
	public Long getCLASSINDEX() {
		if (CLASSINDEX == null || "".equals(CLASSINDEX)) {
			CLASSINDEX = System.currentTimeMillis();
		}
		return CLASSINDEX;
	}

	public void setCLASSINDEX(Long CLASSINDEX) {
		this.CLASSINDEX = CLASSINDEX;
	}

	@Column(name = "CLASS_TYPE")
	public String getCLASSTYPE() {
		return CLASSTYPE;
	}

	public void setCLASSTYPE(String CLASSTYPE) {
		this.CLASSTYPE = CLASSTYPE;
	}

	@Column(name = "CLASS_DESC")
	public String getCLASSDESC() {
		return CLASSDESC;
	}

	public void setCLASSDESC(String CLASSDESC) {
		this.CLASSDESC = CLASSDESC;
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
