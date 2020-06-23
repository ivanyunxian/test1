package com.supermap.intelligent.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="bdc_shyqzd",schema="bdc_mrpc")
public class BDC_SHYQZD {

	private String ID;
	private String ENTERPRISEID;
	private String BDCDYH;
	private String ZL;
	private double PZMJ=0.0;
	private String CFZT;
	private String DYZT;
	private String YYZT;
	private String OPERATOR;
	private Date CREATETIME; 
	private String ZDCLOB;
	private double ZDMJ;
	private String STATUS;
	
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
	
	@Column(name="PZMJ")
	public Double getPZMJ() {
		return PZMJ;
	}
	public void setPZMJ(Double pZMJ) {
		if(pZMJ==null){
			pZMJ =0.0;
		}
		PZMJ = pZMJ;
	}
	
	@Column(name="CFZT")
	public String getCFZT() {
		return CFZT;
	}
	public void setCFZT(String cFZT) {
		CFZT = cFZT;
	}
	
	@Column(name="DYZT")
	public String getDYZT() {
		return DYZT;
	}
	public void setDYZT(String dYZT) {
		DYZT = dYZT;
	}
	
	@Column(name="YYZT")
	public String getYYZT() {
		return YYZT;
	}
	public void setYYZT(String yYZT) {
		YYZT = yYZT;
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
	
	@Column(name="ZDCLOB")
	public String getZDCLOB() {
		return ZDCLOB;
	}
	public void setZDCLOB(String zDCLOB) {
		ZDCLOB = zDCLOB;
	}
	
	@Column(name="ZDMJ")
	public Double getZDMJ() {
		return ZDMJ;
	}
	public void setZDMJ(Double zDMJ) {
		ZDMJ = zDMJ;
	}
	
	@Column(name="STATUS")
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
}
