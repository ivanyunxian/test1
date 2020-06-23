package com.supermap.intelligent.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sys_user",schema="bdc_mrpc")
public class SYS_USER {

	private String ID;
	private String USERNAME;
	private String REALNAME;
	private String PASSWORD;
	private String SALT;
	private String AVATAR;
	private Date BIRTHDAY;
	private String SEX;
	private String EMAIL;
	private String PHONE;
	private String ORG_CODE;
	private String STATUS;
	private String DEL_FLAG;
	private String ACTIVITI_SYNC;
	private String CREATE_BY;
	private Date CREATE_TIME;
	private String UPDATE_BY;
	private Date UPDATE_TIME;
	private String DIVISION_CODE;
	private String ZJLX;
	private String ZJH;
	private String DEPT_ID;
	private String PASSWORD_LEVEL;
	private String STAFF_UUID;
	private String START_UKEY;
	private String ENTERPRISE_ID;
	private String SHZT;
	
	@Id
	@Column(name="ID")
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	
	@Column(name="USERNAME")
	public String getUSERNAME() {
		return USERNAME;
	}
	public void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}
	
	@Column(name="REALNAME")
	public String getREALNAME() {
		return REALNAME;
	}
	public void setREALNAME(String rEALNAME) {
		REALNAME = rEALNAME;
	}
	
	@Column(name="PASSWORD")
	public String getPASSWORD() {
		return PASSWORD;
	}
	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}
	
	@Column(name="SALT")
	public String getSALT() {
		return SALT;
	}
	public void setSALT(String sALT) {
		SALT = sALT;
	}
	
	@Column(name="AVATAR")
	public String getAVATAR() {
		return AVATAR;
	}
	public void setAVATAR(String aVATAR) {
		AVATAR = aVATAR;
	}
	
	@Column(name="BIRTHDAY")
	public Date getBIRTHDAY() {
		return BIRTHDAY;
	}
	public void setBIRTHDAY(Date bIRTHDAY) {
		BIRTHDAY = bIRTHDAY;
	}
	
	@Column(name="SEX")
	public String getSEX() {
		return SEX;
	}
	public void setSEX(String sEX) {
		SEX = sEX;
	}
	
	@Column(name="EMAIL")
	public String getEMAIL() {
		return EMAIL;
	}
	public void setEMAIL(String eMAIL) {
		EMAIL = eMAIL;
	}
	
	@Column(name="PHONE")
	public String getPHONE() {
		return PHONE;
	}
	public void setPHONE(String pHONE) {
		PHONE = pHONE;
	}
	
	@Column(name="ORG_CODE")
	public String getORG_CODE() {
		return ORG_CODE;
	}
	public void setORG_CODE(String oRG_CODE) {
		ORG_CODE = oRG_CODE;
	}
	
	@Column(name="STATUS")
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	
	@Column(name="DEL_FLAG")
	public String getDEL_FLAG() {
		return DEL_FLAG;
	}
	public void setDEL_FLAG(String dEL_FLAG) {
		DEL_FLAG = dEL_FLAG;
	}
	
	@Column(name="ACTIVITI_SYNC")
	public String getACTIVITI_SYNC() {
		return ACTIVITI_SYNC;
	}
	public void setACTIVITI_SYNC(String aCTIVITI_SYNC) {
		ACTIVITI_SYNC = aCTIVITI_SYNC;
	}
	
	@Column(name="CREATE_BY")
	public String getCREATE_BY() {
		return CREATE_BY;
	}
	public void setCREATE_BY(String cREATE_BY) {
		CREATE_BY = cREATE_BY;
	}
	
	@Column(name="CREATE_TIME")
	public Date getCREATE_TIME() {
		return CREATE_TIME;
	}
	public void setCREATE_TIME(Date cREATE_TIME) {
		CREATE_TIME = cREATE_TIME;
	}
	
	@Column(name="UPDATE_BY")
	public String getUPDATE_BY() {
		return UPDATE_BY;
	}
	public void setUPDATE_BY(String uPDATE_BY) {
		UPDATE_BY = uPDATE_BY;
	}
	
	@Column(name="UPDATE_TIME")
	public Date getUPDATE_TIME() {
		return UPDATE_TIME;
	}
	public void setUPDATE_TIME(Date uPDATE_TIME) {
		UPDATE_TIME = uPDATE_TIME;
	}
	
	@Column(name="DIVISION_CODE")
	public String getDIVISION_CODE() {
		return DIVISION_CODE;
	}
	public void setDIVISION_CODE(String dIVISION_CODE) {
		DIVISION_CODE = dIVISION_CODE;
	}
	
	@Column(name="ZJLX")
	public String getZJLX() {
		return ZJLX;
	}
	public void setZJLX(String zJLX) {
		ZJLX = zJLX;
	}
	
	@Column(name="ZJH")
	public String getZJH() {
		return ZJH;
	}
	public void setZJH(String zJH) {
		ZJH = zJH;
	}
	
	@Column(name="DEPT_ID")
	public String getDEPT_ID() {
		return DEPT_ID;
	}
	public void setDEPT_ID(String dEPT_ID) {
		DEPT_ID = dEPT_ID;
	}
	
	@Column(name="PASSWORD_LEVEL")
	public String getPASSWORD_LEVEL() {
		return PASSWORD_LEVEL;
	}
	public void setPASSWORD_LEVEL(String pASSWORD_LEVEL) {
		PASSWORD_LEVEL = pASSWORD_LEVEL;
	}
	
	@Column(name="STAFF_UUID")
	public String getSTAFF_UUID() {
		return STAFF_UUID;
	}
	public void setSTAFF_UUID(String sTAFF_UUID) {
		STAFF_UUID = sTAFF_UUID;
	}
	
	@Column(name="START_UKEY")
	public String getSTART_UKEY() {
		return START_UKEY;
	}
	public void setSTART_UKEY(String sTART_UKEY) {
		START_UKEY = sTART_UKEY;
	}
	
	@Column(name="ENTERPRISE_ID")
	public String getENTERPRISE_ID() {
		return ENTERPRISE_ID;
	}
	public void setENTERPRISE_ID(String eNTERPRISE_ID) {
		ENTERPRISE_ID = eNTERPRISE_ID;
	}
	
	@Column(name="SHZT")
	public String getSHZT() {
		return SHZT;
	}
	public void setSHZT(String sHZT) {
		SHZT = sHZT;
	}
}
