package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-8-15 
//* ----------------------------------------
//* Public Entity bdcs_constraint 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_CONSTRAINT;

@Entity
@Table(name = "bdcs_constraint", schema = "bdck")
public class BDCS_CONSTRAINT extends GenerateBDCS_CONSTRAINT {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "name")
	public String getNAME() {
		return super.getNAME();
	}

	@Override
	@Column(name = "code")
	public String getCODE() {
		return super.getCODE();
	}

	@Override
	@Column(name = "classname")
	public String getCLASSNAME() {
		return super.getCLASSNAME();
	}

	@Override
	@Column(name = "description")
	public String getDESCRIPTION() {
		return super.getDESCRIPTION();
	}

	@Override
	@Column(name = "executetype")
	public String getEXECUTETYPE() {
		return super.getEXECUTETYPE();
	}

	@Override
	@Column(name = "sqlexp")
	public String getSQLEXP() {
		return super.getSQLEXP();
	}

	@Override
	@Column(name = "resultexp")
	public String getRESULTEXP() {
		return super.getRESULTEXP();
	}
}
