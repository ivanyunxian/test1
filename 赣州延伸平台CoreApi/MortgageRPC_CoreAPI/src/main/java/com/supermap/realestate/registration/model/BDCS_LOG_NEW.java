package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-7-11 
//* ----------------------------------------
//* Public Entity bdcs_c_gz 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_LOG_NEW;

@Entity
@Table(name = "bdcs_log_new", schema = "bdck")
public class BDCS_LOG_NEW extends GenerateBDCS_LOG_NEW  {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}

	@Override
	@Column(name = "operatetime")
	public Date getOPERATETIME() {
		return super.getOPERATETIME();
	}

	@Override
	@Column(name = "operateuser")
	public String getOPERATEUSER() {
		return super.getOPERATEUSER();
	}
	@Override
	@Column(name = "operatetypename")
	public String getOPERATETYPENAME() {
		return super.getOPERATETYPENAME();
	}

	@Override
	@Column(name = "operatetype")
	public String getOPERATETYPE() {
		return super.getOPERATETYPE();
	}

	@Override
	@Column(name = "logcontext")
	public String getLOGCONTEXT() {
		return super.getLOGCONTEXT();
	}

}
