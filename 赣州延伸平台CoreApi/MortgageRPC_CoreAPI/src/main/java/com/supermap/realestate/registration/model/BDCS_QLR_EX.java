package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-7-11 
//* ----------------------------------------
//* Public Entity bdcs_qlr_gz 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_QLR_EX;

@Entity
@Table(name = "bdcs_qlr_ex", schema = "bdck")
public class BDCS_QLR_EX extends GenerateBDCS_QLR_EX {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
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
	@Column(name = "indexname")
	public String getINDEXNAME() {
		return super.getINDEXNAME();
	}
}
