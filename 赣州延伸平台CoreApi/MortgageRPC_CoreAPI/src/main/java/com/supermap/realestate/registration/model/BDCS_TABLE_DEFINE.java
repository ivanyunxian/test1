package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2017/05/16 
//* ----------------------------------------
//* Public Entity bdcs_table_define 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_TABLE_DEFINE;

@Entity
@Table(name = "bdcs_table_define", schema = "bdck")
public class BDCS_TABLE_DEFINE extends GenerateBDCS_TABLE_DEFINE {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "groupid")
	public String getGROUPID() {
		return super.getGROUPID();
	}

	@Override
	@Column(name = "status")
	public String getSTATUS() {
		return super.getSTATUS();
	}

	@Override
	@Column(name = "statusvalue")
	public String getSTATUSVALUE() {
		return super.getSTATUSVALUE();
	}

	@Override
	@Column(name = "description")
	public String getDESCRIPTION() {
		return super.getDESCRIPTION();
	}
}
