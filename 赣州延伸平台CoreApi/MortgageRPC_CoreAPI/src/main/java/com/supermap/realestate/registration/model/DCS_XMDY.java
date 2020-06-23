package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-3 
//* ----------------------------------------
//* Public Entity BDCS_XMDY 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateDCS_XMDY;

@Entity
@Table(name = "BDCS_XMDY", schema = "BDCDCK")
public class DCS_XMDY extends GenerateDCS_XMDY {

	@Override
	@Id
	@Column(name = "XMDYID", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "XMBH")
	public String getXMBH() {
		return super.getXMBH();
	}

	@Override
	@Column(name = "LDYID")
	public String getLDYID() {
		return super.getLDYID();
	}

	@Override
	@Column(name = "LDYBH")
	public String getLDYBH() {
		return super.getLDYBH();
	}

	@Override
	@Column(name = "LQLRID")
	public String getLQLRID() {
		return super.getLQLRID();
	}

	@Override
	@Column(name = "LQLID")
	public String getLQLID() {
		return super.getLQLID();
	}

	@Override
	@Column(name = "XDYID")
	public String getXDYID() {
		return super.getXDYID();
	}

	@Override
	@Column(name = "XDYBH")
	public String getXDYBH() {
		return super.getXDYBH();
	}

	@Override
	@Column(name = "XQLRID")
	public String getXQLRID() {
		return super.getXQLRID();
	}

	@Override
	@Column(name = "XQLID")
	public String getXQLID() {
		return super.getXQLID();
	}

	@Override
	@Column(name = "DJSJ")
	public Date getDJSJ() {
		return super.getDJSJ();
	}

	@Override
	@Column(name = "SQRID")
	public String getSQRID() {
		return super.getSQRID();
	}

	@Override
	@Column(name = "BDCDYLX")
	public Integer getBDCDYLX() {
		return super.getBDCDYLX();
	}

	@Override
	@Column(name = "BDCDYLXMC")
	public String getBDCDYLXMC() {
		return super.getBDCDYLXMC();
	}
}
