package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-3 
//* ----------------------------------------
//* Public Entity BDCS_LJZC_GZ 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateDCS_LJZC_GZ;

@Entity
@Table(name = "BDCS_LJZC_GZ", schema = "BDCDCK")
public class DCS_LJZC_GZ extends GenerateDCS_LJZC_GZ {

	@Override
	@Id
	@Column(name = "ID", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "CID")
	public String getCID() {
		return super.getCID();
	}

	@Override
	@Column(name = "LJZID")
	public String getLJZID() {
		return super.getLJZID();
	}

	@Override
	@Column(name = "XMBH")
	public String getXMBH() {
		return super.getXMBH();
	}

	@Override
	@Column(name = "DCXMID")
	public String getDCXMID() {
		return super.getDCXMID();
	}
}
