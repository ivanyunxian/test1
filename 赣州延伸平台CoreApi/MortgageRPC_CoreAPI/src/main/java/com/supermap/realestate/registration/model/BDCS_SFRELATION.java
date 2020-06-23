package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-7-22 
//* ----------------------------------------
//* Public Entity bdcs_sfrelation 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_SFRELATION;

@Entity
@Table(name = "bdcs_sfrelation", schema = "bdck")
public class BDCS_SFRELATION extends GenerateBDCS_SFRELATION {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "prodef_id")
	public String getPRODEF_ID() {
		return super.getPRODEF_ID();
	}

	@Override
	@Column(name = "sfdyid")
	public String getSFDYID() {
		return super.getSFDYID();
	}

	@Override
	@Column(name = "creat_time")
	public Date getCREAT_TIME() {
		return super.getCREAT_TIME();
	}
}
