package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/13 
//* ----------------------------------------
//* Public Entity rt_unit_modulemanager 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateRT_UNIT_MODULEMANAGER;

@Entity
@Table(name = "rt_unit_modulemanager", schema = "bdck")
public class RT_UNIT_MODULEMANAGER extends GenerateRT_UNIT_MODULEMANAGER {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "moduleid")
	public String getMODULEID() {
		return super.getMODULEID();
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

	@Override
	@Column(name = "visible")
	public String getVISIBLE() {
		return super.getVISIBLE();
	}

	@Override
	@Column(name = "editable")
	public String getEDITABLE() {
		return super.getEDITABLE();
	}

	@Override
	@Column(name = "requierd")
	public String getREQUIERD() {
		return super.getREQUIERD();
	}

	@Override
	@Column(name = "sxh")
	public Integer getSXH() {
		return super.getSXH();
	}

	@Override
	@Column(name = "colspan")
	public Integer getCOLSPAN() {
		return super.getCOLSPAN();
	}
}
