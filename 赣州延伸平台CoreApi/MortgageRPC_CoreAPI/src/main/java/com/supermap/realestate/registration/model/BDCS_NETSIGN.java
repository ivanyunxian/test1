package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-11 
//* ----------------------------------------
//* Public Entity bdcs_constcls 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_NETSIGN;

@Entity
@Table(name = "bdcs_netsign", schema = "bdcdck")
public class BDCS_NETSIGN extends GenerateBDCS_NETSIGN {

	
	@Override
	@Id
	@Column(name = "id")
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "bdcdyid")
	public String getBDCDYID() {
		return super.getBDCDYID();
	}

	@Override
	@Column(name = "hth")
	public String getHTH() {
		return super.getHTH();
	}

	@Override
	@Column(name = "msr")
	public String getMSR() {
		return super.getMSR();
	}

}
