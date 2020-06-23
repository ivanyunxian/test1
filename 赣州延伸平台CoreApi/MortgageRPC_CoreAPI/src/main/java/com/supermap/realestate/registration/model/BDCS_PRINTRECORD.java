package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2016-3-3 
//* ----------------------------------------
//* Public Entity bdcs_printrecord 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.supermap.realestate.registration.util.ConstHelper;
import javax.persistence.Transient;
import java.util.Date;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_PRINTRECORD;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "bdcs_printrecord", schema = "bdck")
public class BDCS_PRINTRECORD extends GenerateBDCS_PRINTRECORD {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "printtype")
	public String getPRINTTYPE() {
		return super.getPRINTTYPE();
	}

	@Override
	@Column(name = "printtime")
	public Date getPRINTTIME() {
		return super.getPRINTTIME();
	}

	@Override
	@Column(name = "printstaff")
	public String getPRINTSTAFF() {
		return super.getPRINTSTAFF();
	}

	@Override
	@Column(name = "remark")
	public String getREMARK() {
		return super.getREMARK();
	}

	@Override
	@Column(name = "project_id")
	public String getPROJECT_ID() {
		return super.getPROJECT_ID();
	}

	@Override
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}
}
