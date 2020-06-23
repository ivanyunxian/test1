package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2016/8/18 
//* ----------------------------------------
//* Public Entity bdcs_partiallimit 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_PARTIALLIMIT;

@Entity
@Table(name = "bdcs_partiallimit", schema = "bdck")
public class BDCS_PARTIALLIMIT extends GenerateBDCS_PARTIALLIMIT {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "bdcdyid")
	public String getBDCDYID() {
		return super.getBDCDYID();
	}

	@Override
	@Column(name = "qlid")
	public String getQLID() {
		return super.getQLID();
	}

	@Override
	@Column(name = "qlrid")
	public String getQLRID() {
		return super.getQLRID();
	}

	@Override
	@Column(name = "qlrmc")
	public String getQLRMC() {
		return super.getQLRMC();
	}

	@Override
	@Column(name = "zjh")
	public String getZJH() {
		return super.getZJH();
	}

	@Override
	@Column(name = "limitqlid")
	public String getLIMITQLID() {
		return super.getLIMITQLID();
	}

	@Override
	@Column(name = "limittype")
	public String getLIMITTYPE() {
		return super.getLIMITTYPE();
	}

	@Override
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}

	@Override
	@Column(name = "yxbz")
	public String getYXBZ() {
		return super.getYXBZ();
	}
}
