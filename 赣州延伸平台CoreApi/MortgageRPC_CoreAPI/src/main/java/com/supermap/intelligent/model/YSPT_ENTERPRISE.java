package com.supermap.intelligent.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="YSPT_ENTERPRISE",schema="bdc_mrpc")
public class YSPT_ENTERPRISE {
	
	private String ID;
	private String ENTERPRISE_NAME;
	private String ENTERPRISE_CODE;
	private String ENTERPRISE_ADDRESS;
	private String FRDBXM;
	private String FRDBZJHM;
	private String REGISTER_NAME;
	private String REGISTER_ZJHM;
	private String REGISTER_PHONE;
	private String STATUS;
	private String DEL_FLAG;
	private String CREATE_BY;
	private Date CREATE_TIME;
	private String UPDATE_BY;
	private Date UPDATE_TIME;
	private String MSG;
	private String BH_YZM;
	
	@Id
	@Column(name="id")
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	
	@Column(name="ENTERPRISE_NAME")
	public String getENTERPRISE_NAME() {
		return ENTERPRISE_NAME;
	}
	public void setENTERPRISE_NAME(String eNTERPRISE_NAME) {
		ENTERPRISE_NAME = eNTERPRISE_NAME;
	}
	
	@Column(name="ENTERPRISE_CODE")
	public String getENTERPRISE_CODE() {
		return ENTERPRISE_CODE;
	}
	public void setENTERPRISE_CODE(String eNTERPRISE_CODE) {
		ENTERPRISE_CODE = eNTERPRISE_CODE;
	}
	
	@Column(name="ENTERPRISE_ADDRESS")
	public String getENTERPRISE_ADDRESS() {
		return ENTERPRISE_ADDRESS;
	}
	public void setENTERPRISE_ADDRESS(String eNTERPRISE_ADDRESS) {
		ENTERPRISE_ADDRESS = eNTERPRISE_ADDRESS;
	}
	
	@Column(name="FRDBXM")
	public String getFRDBXM() {
		return FRDBXM;
	}
	public void setFRDBXM(String fRDBXM) {
		FRDBXM = fRDBXM;
	}
	
	@Column(name="FRDBZJHM")
	public String getFRDBZJHM() {
		return FRDBZJHM;
	}
	public void setFRDBZJHM(String fRDBZJHM) {
		FRDBZJHM = fRDBZJHM;
	}
	
	@Column(name="REGISTER_NAME")
	public String getREGISTER_NAME() {
		return REGISTER_NAME;
	}
	public void setREGISTER_NAME(String rEGISTER_NAME) {
		REGISTER_NAME = rEGISTER_NAME;
	}
	
	@Column(name="REGISTER_ZJHM")
	public String getREGISTER_ZJHM() {
		return REGISTER_ZJHM;
	}
	public void setREGISTER_ZJHM(String rEGISTER_ZJHM) {
		REGISTER_ZJHM = rEGISTER_ZJHM;
	}
	
	@Column(name="REGISTER_PHONE")
	public String getREGISTER_PHONE() {
		return REGISTER_PHONE;
	}
	public void setREGISTER_PHONE(String rEGISTER_PHONE) {
		REGISTER_PHONE = rEGISTER_PHONE;
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
	
	@Column(name="MSG")
	public String getMSG() {
		return MSG;
	}
	public void setMSG(String mSG) {
		MSG = mSG;
	}
	
	@Column(name="BH_YZM")
	public String getBH_YZM() {
		return BH_YZM;
	}
	public void setBH_YZM(String bH_YZM) {
		BH_YZM = bH_YZM;
	}

}
