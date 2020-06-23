package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2017/05/16 
//* ----------------------------------------
//* Public Entity bdcs_table_color 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_TABLE_COLOR;

@Entity
@Table(name = "bdcs_table_color", schema = "bdck")
public class BDCS_TABLE_COLOR extends GenerateBDCS_TABLE_COLOR {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "groupname")
	public String getGROUPNAME() {
		return super.getGROUPNAME();
	}

	@Override
	@Column(name = "groupcolor")
	public String getGROUPCOLOR() {
		return super.getGROUPCOLOR();
	}

	@Override
	@Column(name = "groupindex")
	public String getGROUPINDEX() {
		return super.getGROUPINDEX();
	}

	@Override
	@Column(name = "description")
	public String getDESCRIPTION() {
		return super.getDESCRIPTION();
	}
}
