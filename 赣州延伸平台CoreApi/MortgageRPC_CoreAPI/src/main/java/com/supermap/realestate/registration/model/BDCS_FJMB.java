package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-9-23 
//* ----------------------------------------
//* Public Entity bdcs_djyymb 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_FJMB;

@Entity
@Table(name = "bdcs_fjmb", schema = "bdck")
public class BDCS_FJMB extends GenerateBDCS_FJMB {

	@Override
	@Id
	@Column(name = "id", length = 4000)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "userid")
	public String getUSERID() {
		return super.getUSERID();
	}

	@Override
	@Column(name = "username")
	public String getUSERNAME() {
		return super.getUSERNAME();
	}

	@Override
	@Column(name = "fj")
	public String getFJ() {
		return super.getFJ();
	}

	@Override
	@Column(name = "createtime")
	public Date getCREATETIME() {
		return super.getCREATETIME();
	}

	@Override
	@Column(name = "modifytime")
	public Date getMODIFYTIME() {
		return super.getMODIFYTIME();
	}

	@Override
	@Column(name = "bz")
	public String getBZ() {
		return super.getBZ();
	}
}
