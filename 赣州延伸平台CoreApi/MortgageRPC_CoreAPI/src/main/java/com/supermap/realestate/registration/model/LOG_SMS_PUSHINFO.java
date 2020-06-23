package com.supermap.realestate.registration.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "LOG_SMS_PUSHINFO",schema = "LOG")
public class LOG_SMS_PUSHINFO implements Serializable{

	private static final long serialVersionUID = 1L;
	private String ID;
	private String XMBH;
	private String SQRID;
	private String TEMPLATE_ID;
	private String USERID;
	private String USERNAME;
	private String RECEIVE_NAME;
	private String RECEIVE_PHONE;
	private String RECEIVE_INFO;
	private Date CREATETIME;
	private Date SEND_DATE;
	private Integer SEND_NUMBER;
	private String SEND_TYPE;
	private String SEND_STATUS;
	private String ISDLR;
	private String SQRLB;
	private String YXBZ;
	private String BZ;
	private String TENANT_ID;
	private String SEND_CONTENT;
	private String RESULT_CODE;
	private String RESULT_MSG;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	@Id
	@Column(name = "ID")
	public String getID() {
		if (ID == null) {
			ID = UUID.randomUUID().toString().replace("-", "");
		}
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	@Column(name = "XMBH")
	public String getXMBH() {
		return XMBH;
	}

	public void setXMBH(String XMBH) {
		this.XMBH = XMBH;
	}

	@Column(name = "SQRID")
	public String getSQRID() {
		return SQRID;
	}

	public void setSQRID(String SQRID) {
		this.SQRID = SQRID;
	}

	@Column(name = "TEMPLATE_ID")
	public String getTEMPLATE_ID() {
		return TEMPLATE_ID;
	}

	public void setTEMPLATE_ID(String TEMPLATE_ID) {
		this.TEMPLATE_ID = TEMPLATE_ID;
	}

	@Column(name = "USERID")
	public String getUSERID() {
		return USERID;
	}

	public void setUSERID(String USERID) {
		this.USERID = USERID;
	}

	@Column(name = "USERNAME")
	public String getUSERNAME() {
		return USERNAME;
	}

	public void setUSERNAME(String USERNAME) {
		this.USERNAME = USERNAME;
	}

	@Column(name = "RECEIVE_NAME")
	public String getRECEIVE_NAME() {
		return RECEIVE_NAME;
	}

	public void setRECEIVE_NAME(String RECEIVE_NAME) {
		this.RECEIVE_NAME = RECEIVE_NAME;
	}

	@Column(name = "RECEIVE_PHONE")
	public String getRECEIVE_PHONE() {
		return RECEIVE_PHONE;
	}

	public void setRECEIVE_PHONE(String RECEIVE_PHONE) {
		this.RECEIVE_PHONE = RECEIVE_PHONE;
	}

	@Column(name = "RECEIVE_INFO")
	public String getRECEIVE_INFO() {
		return RECEIVE_INFO;
	}

	public void setRECEIVE_INFO(String RECEIVE_INFO) {
		this.RECEIVE_INFO = RECEIVE_INFO;
	}

	@Column(name = "CREATETIME")
	public Date getCREATETIME() {
		return CREATETIME;
	}

	public void setCREATETIME(Date CREATETIME) {
		this.CREATETIME = CREATETIME;
	}

	@Column(name = "SEND_DATE")
	public Date getSEND_DATE() {
		return SEND_DATE;
	}

	public void setSEND_DATE(Date SEND_DATE) {
		this.SEND_DATE = SEND_DATE;
	}

	@Column(name = "SEND_NUMBER")
	public Integer getSEND_NUMBER() {
		if (SEND_NUMBER == null) {
			SEND_NUMBER = 0;
		}
		return SEND_NUMBER;
	}

	public void setSEND_NUMBER(Integer SEND_NUMBER) {
		this.SEND_NUMBER = SEND_NUMBER;
	}

	@Column(name = "SEND_TYPE")
	public String getSEND_TYPE() {
		return SEND_TYPE;
	}

	public void setSEND_TYPE(String SEND_TYPE) {
		this.SEND_TYPE = SEND_TYPE;
	}

	@Column(name = "SEND_STATUS")
	public String getSEND_STATUS() {
		return SEND_STATUS;
	}

	public void setSEND_STATUS(String SEND_STATUS) {
		this.SEND_STATUS = SEND_STATUS;
	}

	@Column(name = "ISDLR")
	public String getISDLR() {
		return ISDLR;
	}

	public void setISDLR(String ISDLR) {
		this.ISDLR = ISDLR;
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

	@Column(name = "SEND_CONTENT")
	public String getSEND_CONTENT() {
		return SEND_CONTENT;
	}

	public void setSEND_CONTENT(String SEND_CONTENT) {
		this.SEND_CONTENT = SEND_CONTENT;
	}

	@Column(name = "RESULT_CODE")
	public String getRESULT_CODE() {
		return RESULT_CODE;
	}

	public void setRESULT_CODE(String RESULT_CODE) {
		this.RESULT_CODE = RESULT_CODE;
	}

	@Column(name = "RESULT_MSG")
	public String getRESULT_MSG() {
		return RESULT_MSG;
	}

	public void setRESULT_MSG(String RESULT_MSG) {
		this.RESULT_MSG = RESULT_MSG;
	}

	@Column(name = "SQRLB")
	public String getSQRLB() {
		return SQRLB;
	}

	public void setSQRLB(String SQRLB) {
		this.SQRLB = SQRLB;
	}
}
