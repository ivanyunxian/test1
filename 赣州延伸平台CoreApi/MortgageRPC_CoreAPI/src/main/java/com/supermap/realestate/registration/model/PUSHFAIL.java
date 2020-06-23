package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-12-26 
//* ----------------------------------------
//* Public Entity bdcs_xmxx 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_XMXX;
import com.supermap.realestate.registration.model.genrt.GeneratePUSHFAIL;

@Entity
@Table(name = "PUSHFAIL", schema = "bdck")
public class PUSHFAIL extends GeneratePUSHFAIL {

	@Override
	@Id
	@Column(name = "ID", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "PROJECT_ID", length = 50)
	public String getPROJECT_ID() {
		return super.getPROJECT_ID();
	}

	
	@Override
	@Column(name = "xmmc")
	public String getXMMC() {
		return super.getXMMC();
	}

	@Override
	@Column(name = "djlx")
	public String getDJLX() {
		return super.getDJLX();
	}

	@Override
	@Column(name = "qllx")
	public String getQLLX() {
		return super.getQLLX();
	}

	

	@Override
	@Column(name = "slry")
	public String getSLRY() {
		return super.getSLRY();
	}

	@Override
	@Column(name = "slsj")
	public Date getSLSJ() {
		return super.getSLSJ();
	}

	@Override
	@Column(name = "ywlsh")
	public String getYWLSH() {
		return super.getYWLSH();
	}

	@Override
	@Column(name = "CASENUM")
	public String getCASENUM() {
		return super.getCASENUM();
	}
	@Override
	@Column(name = "BLJD")
	public String getBLJD() {
		return super.getBLJD();
	}
	@Override
	@Column(name = "SBLX")
	public String getSBLX() {
		return super.getSBLX();
	}
	@Override
	@Column(name = "TSSJ")
	public Date getTSSJ() {
		return super.getTSSJ();
	}
	@Override
	@Column(name = "FAILCAUSE")
	public String getFAILCAUSE() {
		return super.getFAILCAUSE();
	}
}
