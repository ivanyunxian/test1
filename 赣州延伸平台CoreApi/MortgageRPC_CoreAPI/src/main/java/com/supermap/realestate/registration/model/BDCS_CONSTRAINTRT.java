package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-7-23 
//* ----------------------------------------
//* Public Entity bdcs_constraintrt 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_CONSTRAINTRT;

@Entity
@Table(name = "bdcs_constraintrt", schema = "bdck")
public class BDCS_CONSTRAINTRT extends GenerateBDCS_CONSTRAINTRT {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "workflowid")
	public String getWORKFLOWID() {
		return super.getWORKFLOWID();
	}

	@Override
	@Column(name = "constraintid")
	public String getCONSTRAINTID() {
		return super.getCONSTRAINTID();
	}

	@Override
	@Column(name = "constrainttype")
	public String getCONSTRAINTTYPE() {
		return super.getCONSTRAINTTYPE();
	}
}
