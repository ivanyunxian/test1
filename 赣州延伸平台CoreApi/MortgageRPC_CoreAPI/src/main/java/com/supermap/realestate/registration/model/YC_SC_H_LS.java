package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-7-8 
//* ----------------------------------------
//* Public Entity yc_sc_h_ls 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateYC_SC_H_LS;

@Entity
@Table(name = "yc_sc_h_ls", schema = "bdck")
public class YC_SC_H_LS extends GenerateYC_SC_H_LS {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "ycbdcdyid")
	public String getYCBDCDYID() {
		return super.getYCBDCDYID();
	}

	@Override
	@Column(name = "scbdcdyid")
	public String getSCBDCDYID() {
		return super.getSCBDCDYID();
	}
}
