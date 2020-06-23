package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015/12/18 
//* ----------------------------------------
//* Public Entity rt_routecondition 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateRT_ROUTECONDITION;

@Entity
@Table(name = "rt_routecondition", schema = "bdck")
public class RT_ROUTECONDITION extends GenerateRT_ROUTECONDITION {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "routeid")
	public String getROUTEID() {
		return super.getROUTEID();
	}

	@Override
	@Column(name = "checkruleid")
	public String getCHECKRULEID() {
		return super.getCHECKRULEID();
	}

	@Override
	@Column(name = "checklevel")
	public String getCHECKLEVEL() {
		return super.getCHECKLEVEL();
	}

	@Override
	@Column(name = "routetype")
	public String getROUTETYPE() {
		return super.getROUTETYPE();
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
	@Column(name = "creator")
	public String getCREATOR() {
		return super.getCREATOR();
	}

	@Override
	@Column(name = "lastmodifier")
	public String getLASTMODIFIER() {
		return super.getLASTMODIFIER();
	}
}
