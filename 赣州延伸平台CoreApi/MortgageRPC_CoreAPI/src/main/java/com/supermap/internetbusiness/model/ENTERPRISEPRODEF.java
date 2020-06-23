package com.supermap.internetbusiness.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ENTERPRISE_PRODEF",schema="BDCK")
public class ENTERPRISEPRODEF {

	private String ID;
	private String TYPE;
	private String PRODEF_ID;
	private String PRODEF_NAME;
	private String USERID;
	private String USERNAME;
	
	@Id
	@Column(name = "ID", length = 50)
	public String getID() {
		if (ID == null) {
			ID = UUID.randomUUID().toString().replace("-", "");
		}
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	
	@Column(name="TYPE")
	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}
	
	@Column(name="PRODEF_ID")
	public String getPRODEF_ID() {
		return PRODEF_ID;
	}
	public void setPRODEF_ID(String pRODEF_ID) {
		PRODEF_ID = pRODEF_ID;
	}
	
	@Column(name="PRODEF_NAME")
	public String getPRODEF_NAME() {
		return PRODEF_NAME;
	}
	public void setPRODEF_NAME(String pRODEF_NAME) {
		PRODEF_NAME = pRODEF_NAME;
	}
	
	@Column(name="USERID")
	public String getUSERID() {
		return USERID;
	}
	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}
	
	@Column(name="USERNAME")
	public String getUSERNAME() {
		return USERNAME;
	}
	public void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}
	
}
