package com.supermap.intelligent.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="bdc_zrz",schema="bdc_mrpc")
public class BDC_ZRZ {
	
	private String ID;
	private String ENTERPRISEID;
	private String BDCDYID;
	private String BDCDYH;
	private String ZL;
	private String ZRZH;
	private String OPERATOR;
	private Date CREATETIME; 
	private String ZDID;
	
	@Id
	@Column(name="ID")
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	
	@Column(name="ENTERPRISEID")
	public String getENTERPRISEID() {
		return ENTERPRISEID;
	}
	public void setENTERPRISEID(String eNTERPRISEID) {
		ENTERPRISEID = eNTERPRISEID;
	}
	
	@Column(name="BDCDYID")
	public String getBDCDYID() {
		return BDCDYID;
	}
	public void setBDCDYID(String bDCDYID) {
		BDCDYID = bDCDYID;
	}
	
	@Column(name="BDCDYH")
	public String getBDCDYH() {
		return BDCDYH;
	}
	public void setBDCDYH(String bDCDYH) {
		BDCDYH = bDCDYH;
	}
	
	@Column(name="ZL")
	public String getZL() {
		return ZL;
	}
	public void setZL(String zL) {
		ZL = zL;
	}
	
	@Column(name="ZRZH")
	public String getZRZH() {
		return ZRZH;
	}
	public void setZRZH(String zRZH) {
		ZRZH = zRZH;
	}
	
	@Column(name="OPERATOR")
	public String getOPERATOR() {
		return OPERATOR;
	}
	public void setOPERATOR(String oPERATOR) {
		OPERATOR = oPERATOR;
	}
	
	@Column(name="CREATETIME")
	public Date getCREATETIME() {
		return CREATETIME;
	}
	public void setCREATETIME(Date cREATETIME) {
		CREATETIME = cREATETIME;
	}
	
	@Column(name="ZDID")
	public String getZDID() {
		return ZDID;
	}
	public void setZDID(String zDID) {
		ZDID = zDID;
	}

}
