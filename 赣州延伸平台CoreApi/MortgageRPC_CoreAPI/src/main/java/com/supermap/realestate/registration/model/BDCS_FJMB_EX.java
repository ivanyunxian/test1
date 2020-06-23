package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-7-11 
//* ----------------------------------------
//* Public Entity bdcs_qlr_gz 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_FJMB_EX;

@Entity
@Table(name = "bdcs_fjmb_ex", schema = "bdck")
public class BDCS_FJMB_EX extends GenerateBDCS_FJMB_EX {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}
	
	@Override
	@Column(name = "name")
	public String getName() {
		return super.getName();
	}
	
	@Override
	@Column(name = "sql")
	public String getSql() {
		return super.getSql();
	}
	
	@Override
	@Column(name = "condition")
	public String getCondition() {
		return super.getCondition();
	}

}
