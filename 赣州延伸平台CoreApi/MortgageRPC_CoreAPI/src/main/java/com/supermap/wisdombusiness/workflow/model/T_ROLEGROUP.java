package com.supermap.wisdombusiness.workflow.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_ROLEGROUP",schema = "SMWB_FRAMEWORK")
public class T_ROLEGROUP {

	private String ID;
	private String GROUPNAME;
	private String REMARK;
	
	@Id
	@Column(name = "ID", length = 32)
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	@Column(name = "GROUPNAME", length = 200)
	public String getGROUPNAME() {
		return GROUPNAME;
	}
	public void setGROUPNAME(String gROUPNAME) {
		GROUPNAME = gROUPNAME;
	}
	@Column(name = "REMARK", length = 200)
	public String getREMARK() {
		return REMARK;
	}
	public void setREMARK(String rEMARK) {
		REMARK = rEMARK;
	}
}
