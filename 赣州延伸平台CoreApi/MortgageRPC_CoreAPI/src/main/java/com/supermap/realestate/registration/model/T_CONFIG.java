package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-12-27 
//* ----------------------------------------
//* Public Entity t_config 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.supermap.realestate.registration.model.genrt.GenerateT_CONFIG;


@Entity
@Table(name = "t_config", schema = "SMWB_SUPPORT")
public class T_CONFIG extends GenerateT_CONFIG {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "classname")
	public String getCLASSNAME() {
		return super.getCLASSNAME();
	}

	@Override
	@Column(name = "name")
	public String getNAME() {
		return super.getNAME();
	}

	@Override
	@Column(name = "value")
	public String getVALUE() {
		return super.getVALUE();
	}

	@Override
	@Column(name = "valuedescription")
	public String getVALUEDESCRIPTION() {
		return super.getVALUEDESCRIPTION();
	}

	@Override
	@Column(name = "description")
	public String getDESCRIPTION() {
		return super.getDESCRIPTION();
	}

	@Override
	@Column(name = "url")
	public String getURL() {
		return super.getURL();
	}

	@Override
	@Column(name = "configname")
	public String getCONFIGNAME() {
		return super.getCONFIGNAME();
	}

	@Override
	@Column(name = "valuetype")
	public String getVALUETYPE() {
		return super.getVALUETYPE();
	}

	@Override
	@Column(name = "optionclass")
	public String getOPTIONCLASS() {
		return super.getOPTIONCLASS();
	}

	@Override
	@Column(name = "firstindex")
	public Integer getFIRSTINDEX() {
		return super.getFIRSTINDEX();
	}

	@Override
	@Column(name = "secondindex")
	public Integer getSECONDINDEX() {
		return super.getSECONDINDEX();
	}

	@Override
	@Column(name = "yxbz")
	public Integer getYXBZ() {
		return super.getYXBZ();
	}

	@Override
	@Column(name = "configed")
	public Integer getCONFIGED() {
		return super.getCONFIGED();
	}
}
