package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/12 
//* ----------------------------------------
//* Public Entity rt_unit_field 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateT_UNIT_FIELD;

@Entity
@Table(name = "t_unit_field", schema = "bdck")
public class T_UNIT_FIELD extends GenerateT_UNIT_FIELD {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "bdcdylx")
	public String getBDCDYLX() {
		return super.getBDCDYLX();
	}

	@Override
	@Column(name = "fieldname")
	public String getFIELDNAME() {
		return super.getFIELDNAME();
	}

	@Override
	@Column(name = "fielddescription")
	public String getFIELDDESCRIPTION() {
		return super.getFIELDDESCRIPTION();
	}

	@Override
	@Column(name = "fieldtype")
	public String getFIELDTYPE() {
		return super.getFIELDTYPE();
	}

	@Override
	@Column(name = "fieldoption")
	public String getFIELDOPTION() {
		return super.getFIELDOPTION();
	}

	@Override
	@Column(name = "number_l")
	public Integer getNUMBER_L() {
		return super.getNUMBER_L();
	}

	@Override
	@Column(name = "number_r")
	public Integer getNUMBER_R() {
		return super.getNUMBER_R();
	}
}
