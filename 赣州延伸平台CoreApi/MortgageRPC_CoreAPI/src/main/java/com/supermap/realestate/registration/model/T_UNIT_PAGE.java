package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2016/5/11 
//* ----------------------------------------
//* Public Entity t_unit_page 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateT_UNIT_PAGE;

@Entity
@Table(name = "t_unit_page", schema = "bdck")
public class T_UNIT_PAGE extends GenerateT_UNIT_PAGE {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "name")
	public String getNAME() {
		return super.getNAME();
	}
}
