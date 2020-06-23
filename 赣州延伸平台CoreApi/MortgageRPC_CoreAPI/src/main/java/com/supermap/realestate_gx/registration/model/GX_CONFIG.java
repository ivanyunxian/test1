package com.supermap.realestate_gx.registration.model;

import com.supermap.wisdombusiness.core.SuperHelper;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "GX_CONFIG", schema = "SMWB_SUPPORT")
public class GX_CONFIG {

	@Id
	@Column(name = "ID", length = 50)
	private String Id;
	private String Xmbh;
	private String FILE_NUMBER;
	private String TD_STATUS;
	private String DJDYID;
	private String BDCDYH;
	private String QLLX_DJLX;
	private String BDCDYID;
	private String cdbh;
	private String cdmd;
	private String cdrmc;
	private String cdrzjh;
	private String cdtime;
	
	
	public String getCdtime() {
		return cdtime;
	}

	public void setCdtime(String cdtime) {
		this.cdtime = cdtime;
	}

	public String getCdrmc() {
		return cdrmc;
	}

	public void setCdrmc(String cdrmc) {
		this.cdrmc = cdrmc;
	}

	public String getCdrzjh() {
		return cdrzjh;
	}

	public void setCdrzjh(String cdrzjh) {
		this.cdrzjh = cdrzjh;
	}

	
	
	public GX_CONFIG() {
		this.Id = ((String) SuperHelper.GeneratePrimaryKey());
	}

	public String getCdbh() {
		return cdbh;
	}

	public void setCdbh(String cdbh) {
		this.cdbh = cdbh;
	}

	public String getCdmd() {
		return cdmd;
	}

	public void setCdmd(String cdmd) {
		this.cdmd = cdmd;
	}

	public String getId() {
		return this.Id;
	}

	public void setId(String id) {
		this.Id = id;
	}

	public String getXmbh() {
		return this.Xmbh;
	}

	public void setXmbh(String xmbh) {
		this.Xmbh = xmbh;
	}

	public String getFILE_NUMBER() {
		return this.FILE_NUMBER;
	}

	public void setFILE_NUMBER(String fILE_NUMBER) {
		this.FILE_NUMBER = fILE_NUMBER;
	}

	public String getTD_STATUS() {
		return this.TD_STATUS;
	}

	public void setTD_STATUS(String tD_STATUS) {
		this.TD_STATUS = tD_STATUS;
	}

	public String getDJDYID() {
		return this.DJDYID;
	}

	public void setDJDYID(String dJDYID) {
		this.DJDYID = dJDYID;
	}

	public String getBDCDYH() {
		return this.BDCDYH;
	}

	public void setBDCDYH(String bDCDYH) {
		this.BDCDYH = bDCDYH;
	}

	public String getQLLX_DJLX() {
		return this.QLLX_DJLX;
	}

	public void setQLLX_DJLX(String qLLX_DJLX) {
		this.QLLX_DJLX = qLLX_DJLX;
	}

	public String getBDCDYID() {
		return this.BDCDYID;
	}

	public void setBDCDYID(String bDCDYID) {
		this.BDCDYID = bDCDYID;
	}
}
