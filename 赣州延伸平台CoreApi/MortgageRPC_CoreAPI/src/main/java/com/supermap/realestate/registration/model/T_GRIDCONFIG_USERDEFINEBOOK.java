package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2016/5/4 
//* ----------------------------------------
//* Public Entity t_gridconfig_userdefinebook 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateT_GRIDCONFIG_USERDEFINEBOOK;

@Entity
@Table(name = "t_gridconfig_userdefinebook", schema = "bdck")
public class T_GRIDCONFIG_USERDEFINEBOOK extends GenerateT_GRIDCONFIG_USERDEFINEBOOK {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "fieldname")
	public String getFIELDNAME() {
		return super.getFIELDNAME();
	}

	@Override
	@Column(name = "columntext")
	public String getCOLUMNTEXT() {
		return super.getCOLUMNTEXT();
	}

	@Override
	@Column(name = "width")
	public String getWIDTH() {
		return super.getWIDTH();
	}

	@Override
	@Column(name = "bookid")
	public String getBOOKID() {
		return super.getBOOKID();
	}

	@Override
	@Column(name = "consttype")
	public String getCONSTTYPE() {
		return super.getCONSTTYPE();
	}

	@Override
	@Column(name = "vieworder")
	public Integer getVIEWORDER() {
		return super.getVIEWORDER();
	}
}
