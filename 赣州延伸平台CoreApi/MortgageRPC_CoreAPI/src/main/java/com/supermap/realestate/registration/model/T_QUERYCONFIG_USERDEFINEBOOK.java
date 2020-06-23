package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2016/5/5 
//* ----------------------------------------
//* Public Entity t_queryconfig_userdefinebook 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateT_QUERYCONFIG_USERDEFINEBOOK;

@Entity
@Table(name = "t_queryconfig_userdefinebook", schema = "bdck")
public class T_QUERYCONFIG_USERDEFINEBOOK extends
		GenerateT_QUERYCONFIG_USERDEFINEBOOK {

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
	@Column(name = "fieldcaption")
	public String getFIELDCAPTION() {
		return super.getFIELDCAPTION();
	}

	@Override
	@Column(name = "bookid")
	public String getBOOKID() {
		return super.getBOOKID();
	}

	@Override
	@Column(name = "fieldtype")
	public String getFIELDTYPE() {
		return super.getFIELDTYPE();
	}

	@Override
	@Column(name = "vieworder")
	public Integer getVIEWORDER() {
		return super.getVIEWORDER();
	}

	@Override
	@Column(name = "consttype")
	public String getCONSTTYPE() {
		return super.getCONSTTYPE();
	}

	@Override
	@Column(name = "hqlpre")
	public String getHQLPRE() {
		return super.getHQLPRE();
	}

	@Override
	@Column(name = "hqlsuffix")
	public String getHQLSUFFIX() {
		return super.getHQLSUFFIX();
	}

	@Override
	@Column(name = "hqltype")
	public String getHQLTYPE() {
		return super.getHQLTYPE();
	}
}
