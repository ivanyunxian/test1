package com.supermap.realestate.registration.model;

import java.util.Date;

///*****************************************
//* AutoGenerate by CodeTools 2017/06/28 
//* ----------------------------------------
//* Public Entity result_fixed 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateRESULT_FIXED;

@Entity
@Table(name = "result_fixed", schema = "bdck")
public class RESULT_FIXED extends GenerateRESULT_FIXED {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "fixedlx")
	public String getFIXEDLX() {
		return super.getFIXEDLX();
	}

	@Override
	@Column(name = "fixedcontext")
	public String getFIXEDCONTEXT() {
		return super.getFIXEDCONTEXT();
	}

	@Override
	@Column(name = "createtime")
	public Date getCREATETIME() {
		return super.getCREATETIME();
	}

	@Override
	@Column(name = "creator")
	public String getCREATOR() {
		return super.getCREATOR();
	}

	@Override
	@Column(name = "modifytime")
	public Date getMODIFYTIME() {
		return super.getMODIFYTIME();
	}

	@Override
	@Column(name = "lastmodifier")
	public String getLASTMODIFIER() {
		return super.getLASTMODIFIER();
	}

	@Override
	@Column(name = "fixedendtime")
	public Date getFIXEDENDTIME() {
		return super.getFIXEDENDTIME();
	}

	@Override
	@Column(name = "fixedstarttime")
	public Date getFIXEDSTARTTIME() {
		return super.getFIXEDSTARTTIME();
	}
}
