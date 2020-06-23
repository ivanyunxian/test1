package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015/11/16 
//* ----------------------------------------
//* Public Entity rt_constraintexp 
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

import com.supermap.realestate.registration.model.genrt.GenerateRT_CONSTRAINTEXP;

@Entity
@Table(name = "rt_constraintexp", schema = "bdck")
public class RT_CONSTRAINTEXP extends GenerateRT_CONSTRAINTEXP {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "workflowcode")
	public String getWORKFLOWCODE() {
		return super.getWORKFLOWCODE();
	}

	@Override
	@Column(name = "constraintid")
	public String getCONSTRAINTID() {
		return super.getCONSTRAINTID();
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
