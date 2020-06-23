package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2016/5/4 
//* ----------------------------------------
//* Public Entity t_userdefinebook 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateT_USERDEFINEBOOK;

@Entity
@Table(name = "t_userdefinebook", schema = "bdck")
public class T_USERDEFINEBOOK extends GenerateT_USERDEFINEBOOK {

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

	@Override
	@Column(name = "configsql")
	public String getCONFIGSQL() {
		return super.getCONFIGSQL();
	}
}
