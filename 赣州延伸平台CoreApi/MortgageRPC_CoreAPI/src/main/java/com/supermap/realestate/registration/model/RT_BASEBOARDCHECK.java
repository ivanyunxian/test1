package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015/11/16 
//* ----------------------------------------
//* Public Entity rt_baseboardcheck 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.supermap.realestate.registration.model.genrt.GenerateRT_BASEBOARDCHECK;

@Entity
@Table(name = "rt_baseboardcheck", schema = "bdck")
public class RT_BASEBOARDCHECK extends GenerateRT_BASEBOARDCHECK {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "baseworkflowid")
	public String getBASEWORKFLOWID() {
		return super.getBASEWORKFLOWID();
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

	@Temporal(TemporalType.DATE)
	@Override
	@Column(name = "createtime")
	public Date getCREATETIME() {
		return super.getCREATETIME();
	}

	@Temporal(TemporalType.DATE)
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
