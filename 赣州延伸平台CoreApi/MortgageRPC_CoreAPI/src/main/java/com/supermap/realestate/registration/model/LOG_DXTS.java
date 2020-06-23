package com.supermap.realestate.registration.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "LOG_DXTS",schema = "LOG")
public class LOG_DXTS  implements Serializable{

	private static final long serialVersionUID = 1L;
	private String ID;
	private String XMBH;
	private String SQRID;
	private String SQRXM;
	private String LXDH;
	private String DLRXM;
	private String DLRLXDH;
	private String YWLSH;
	private String XMMC;
	private String ZSBH;
	private String BDCQZH;
	private Date DJSJ;
	private String DJLX;
	private String QLLX;
	private Date SZSJ;
	private Date CREATETIME;
	private String USERID;
	private String USERNAME;
	private String RECEIVE_NAME;
	private String RECEIVE_PHONE;
	private String RECEIVE_INFO;
	private Date SEND_DATE;
	private Integer SEND_NUMBER;
	private String SEND_TYPE;
	private String SEND_STATUS;
	private String YXBZ;
	private String BZ;
	private String TENANT_ID;

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

	@Column(name = "SQRXM")
	public String getSQRXM() {
		return SQRXM;
	}

	public void setSQRXM(String SQRXM) {
		this.SQRXM = SQRXM;
	}

	@Column(name = "LXDH")
	public String getLXDH() {
		return LXDH;
	}

	public void setLXDH(String LXDH) {
		this.LXDH = LXDH;
	}

	@Column(name = "DLRXM")
	public String getDLRXM() {
		return DLRXM;
	}

	public void setDLRXM(String DLRXM) {
		this.DLRXM = DLRXM;
	}

	@Column(name = "DLRLXDH")
	public String getDLRLXDH() {
		return DLRLXDH;
	}

	public void setDLRLXDH(String DLRLXDH) {
		this.DLRLXDH = DLRLXDH;
	}

	@Column(name = "YWLSH")
	public String getYWLSH() {
		return YWLSH;
	}

	public void setYWLSH(String YWLSH) {
		this.YWLSH = YWLSH;
	}

	@Column(name = "XMMC")
	public String getXMMC() {
		return XMMC;
	}

	public void setXMMC(String XMMC) {
		this.XMMC = XMMC;
	}

	@Column(name = "ZSBH")
	public String getZSBH() {
		return ZSBH;
	}

	public void setZSBH(String ZSBH) {
		this.ZSBH = ZSBH;
	}

	@Column(name = "BDCQZH")
	public String getBDCQZH() {
		return BDCQZH;
	}

	public void setBDCQZH(String BDCQZH) {
		this.BDCQZH = BDCQZH;
	}

	@Column(name = "DJSJ")
	public Date getDJSJ() {
		return DJSJ;
	}

	public void setDJSJ(Date DJSJ) {
		this.DJSJ = DJSJ;
	}

	@Column(name = "DJLX")
	public String getDJLX() {
		return DJLX;
	}

	public void setDJLX(String DJLX) {
		this.DJLX = DJLX;
	}

	@Column(name = "QLLX")
	public String getQLLX() {
		return QLLX;
	}

	public void setQLLX(String QLLX) {
		this.QLLX = QLLX;
	}

	@Column(name = "SZSJ")
	public Date getSZSJ() {
		return SZSJ;
	}

	public void setSZSJ(Date SZSJ) {
		this.SZSJ = SZSJ;
	}

	@Column(name = "CREATETIME")
	public Date getCREATETIME() {
		return CREATETIME;
	}

	public void setCREATETIME(Date CREATETIME) {
		this.CREATETIME = CREATETIME;
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
