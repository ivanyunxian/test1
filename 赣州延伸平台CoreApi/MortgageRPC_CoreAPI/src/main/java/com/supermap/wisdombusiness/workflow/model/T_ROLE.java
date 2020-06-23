package com.supermap.wisdombusiness.workflow.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_ROLE",schema = "SMWB_FRAMEWORK")
public class T_ROLE {
	
	private String ID;
	private String GROUPID;
	private String ROLENAME;
	private String ROLETYPE;
	private String AREACODE;
	private String DESCRIPTION;
	private Date CREATETIME;
	private String REMARK;
	
	@Id
	@Column(name = "ID", length = 32)
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	@Column(name = "GROUPID", length = 32)
	public String getGROUPID() {
		return GROUPID;
	}
	public void setGROUPID(String gROUPID) {
		GROUPID = gROUPID;
	}
	@Column(name = "ROLENAME", length = 50)
	public String getROLENAME() {
		return ROLENAME;
	}
	public void setROLENAME(String rOLENAME) {
		ROLENAME = rOLENAME;
	}
	@Column(name = "ROLETYPE", length = 32)
	public String getROLETYPE() {
		return ROLETYPE;
	}
	public void setROLETYPE(String rOLETYPE) {
		ROLETYPE = rOLETYPE;
	}
	@Column(name = "AREACODE", length = 50)
	public String getAREACODE() {
		return AREACODE;
	}
	public void setAREACODE(String aREACODE) {
		AREACODE = aREACODE;
	}
	@Column(name = "DESCRIPTION", length = 200)
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}
	@Column(name = "CREATETIME")
	public Date getCREATETIME() {
		return CREATETIME;
	}
	public void setCREATETIME(Date cREATETIME) {
		CREATETIME = cREATETIME;
	}
	@Column(name = "REMARK", length = 200)
	public String getREMARK() {
		return REMARK;
	}
	public void setREMARK(String rEMARK) {
		REMARK = rEMARK;
	}

}
