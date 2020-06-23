package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2017-06-01 
//* ----------------------------------------
//* Public Entity bdcs_identifyfigure 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_IDENTIFYFIGURE;

@Entity
@Table(name = "bdcs_identifyfigure", schema = "bdck")
public class BDCS_IDENTIFYFIGURE extends GenerateBDCS_IDENTIFYFIGURE {

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
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}

	@Override
	@Column(name = "bdcdylx")
	public String getBDCDYLX() {
		return super.getBDCDYLX();
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
