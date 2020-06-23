package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2016/1/8 
//* ----------------------------------------
//* Public Entity bdcs_xm_dyxz 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_XM_DYXZ;

@Entity
@Table(name = "bdcs_xm_dyxz", schema = "bdck")
public class BDCS_XM_DYXZ extends GenerateBDCS_XM_DYXZ {

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
	@Column(name = "bdcdylx")
	public String getBDCDYLX() {
		return super.getBDCDYLX();
	}

	@Override
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}

	@Override
	@Column(name = "dyxzid")
	public String getDYXZID() {
		return super.getDYXZID();
	}

	@Override
	@Column(name = "zxbz")
	public String getZXBZ() {
		return super.getZXBZ();
	}

	@Override
	@Column(name = "zxyj")
	public String getZXYJ() {
		return super.getZXYJ();
	}

	@Override
	@Column(name = "zxxzwjhm")
	public String getZXXZWJHM() {
		return super.getZXXZWJHM();
	}

	@Override
	@Column(name = "zxxzdw")
	public String getZXXZDW() {
		return super.getZXXZDW();
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
}
