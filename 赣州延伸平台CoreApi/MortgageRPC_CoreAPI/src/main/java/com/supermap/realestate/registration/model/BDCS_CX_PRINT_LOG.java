package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2016/8/24 
//* ----------------------------------------
//* Public Entity bdcs_cx_print_log 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.supermap.realestate.registration.util.ConstHelper;
import javax.persistence.Transient;
import java.util.Date;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_CX_PRINT_LOG;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "bdcs_cx_print_log", schema = "bdck")
public class BDCS_CX_PRINT_LOG extends GenerateBDCS_CX_PRINT_LOG {

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
	@Column(name = "cxlx")
	public String getCXLX() {
		return super.getCXLX();
	}

	@Override
	@Column(name = "operateuser")
	public String getOPERATEUSER() {
		return super.getOPERATEUSER();
	}

	@Override
	@Column(name = "operatetime")
	public Date getOPERATETIME() {
		return super.getOPERATETIME();
	}

	@Override
	@Column(name = "operatetype")
	public String getOPERATETYPE() {
		return super.getOPERATETYPE();
	}

	@Override
	@Column(name = "operatcontext")
	public String getOPERATCONTEXT() {
		return super.getOPERATCONTEXT();
	}
}
