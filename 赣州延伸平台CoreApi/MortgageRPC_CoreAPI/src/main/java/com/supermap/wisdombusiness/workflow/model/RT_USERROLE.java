package com.supermap.wisdombusiness.workflow.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RT_USERROLE",schema = "SMWB_FRAMEWORK")
public class RT_USERROLE {
   
	private String ID;
	private String USERID;
	private String ROLEID;
	private int  TIMELIMIT;
	private Date  STARTTIME;
	private Date  ENDTIME;
	private String GRANTUSERID;
	private String REMARK;
	
	@Id
	@Column(name = "ID", length = 32)
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	@Column(name = "USERID" , length = 32)
	public String getUSERID() {
		return USERID;
	}
	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}
	@Column(name = "ROLEID" , length = 32)
	public String getROLEID() {
		return ROLEID;
	}
	public void setROLEID(String rOLEID) {
		ROLEID = rOLEID;
	}
	@Column(name = "TIMELIMIT")
	public int getTIMELIMIT() {
		return TIMELIMIT;
	}
	public void setTIMELIMIT(int tIMELIMIT) {
		TIMELIMIT = tIMELIMIT;
	}
	@Column(name = "STARTTIME")
	public Date getSTARTTIME() {
		return STARTTIME;
	}
	public void setSTARTTIME(Date sTARTTIME) {
		STARTTIME = sTARTTIME;
	}
	@Column(name = "ENDTIME")
	public Date getENDTIME() {
		return ENDTIME;
	}
	public void setENDTIME(Date eNDTIME) {
		ENDTIME = eNDTIME;
	}
	@Column(name = "GRANTUSERID" , length = 32)
	public String getGRANTUSERID() {
		return GRANTUSERID;
	}
	public void setGRANTUSERID(String gRANTUSERID) {
		GRANTUSERID = gRANTUSERID;
	}
	@Column(name = "REMARK" , length = 200)
	public String getREMARK() {
		return REMARK;
	}
	public void setREMARK(String rEMARK) {
		REMARK = rEMARK;
	}
}
