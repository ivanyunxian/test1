package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-12-26 
//* ----------------------------------------
//* Public Entity t_configoption 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.supermap.realestate.registration.util.ConstHelper;
import javax.persistence.Transient;

import com.supermap.realestate.registration.model.genrt.GenerateT_CONFIGOPTION;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "t_configoption", schema = "SMWB_SUPPORT")
public class T_CONFIGOPTION extends GenerateT_CONFIGOPTION {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "optionclass")
	public String getOPTIONCLASS() {
		return super.getOPTIONCLASS();
	}

	@Override
	@Column(name = "optionvalue")
	public String getOPTIONVALUE() {
		return super.getOPTIONVALUE();
	}

	@Override
	@Column(name = "optiontext")
	public String getOPTIONTEXT() {
		return super.getOPTIONTEXT();
	}

	@Override
	@Column(name = "yxbz")
	public Integer getYXBZ() {
		return super.getYXBZ();
	}
}
