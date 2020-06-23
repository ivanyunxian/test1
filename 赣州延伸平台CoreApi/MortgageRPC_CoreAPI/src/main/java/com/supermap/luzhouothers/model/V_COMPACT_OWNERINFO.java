package com.supermap.luzhouothers.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-10-29 
//* ----------------------------------------
//* Public Entity bdc_v_compact_ownerinfo 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.luzhouothers.model.genrt.GenerateV_COMPACT_OWNERINFO;

@Entity
@Table(name = "v_compact_ownerinfo", schema = "bdck")
public class V_COMPACT_OWNERINFO extends GenerateV_COMPACT_OWNERINFO {

	@Override
	@Column(name = "id", length = 4000)
	public Integer getId() {
		return super.getId();
	}

	@Override
	@Id
	@Column(name = "ownerid")
	public String getOWNERID() {
		return super.getOWNERID();
	}

	@Override
	@Column(name = "owner_name")
	public String getOWNER_NAME() {
		return super.getOWNER_NAME();
	}

	@Override
	@Column(name = "birthday")
	public Date getBIRTHDAY() {
		return super.getBIRTHDAY();
	}

	@Override
	@Column(name = "idtype")
	public String getIDTYPE() {
		return super.getIDTYPE();
	}

	@Override
	@Column(name = "idno")
	public String getIDNO() {
		return super.getIDNO();
	}

	@Override
	@Column(name = "buyerattr")
	public String getBUYERATTR() {
		return super.getBUYERATTR();
	}

	@Override
	@Column(name = "address")
	public String getADDRESS() {
		return super.getADDRESS();
	}

	@Override
	@Column(name = "owner_type")
	public Integer getOWNER_TYPE() {
		return super.getOWNER_TYPE();
	}
}
