package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-10-31 
//* ----------------------------------------
//* Public Entity bdcs_qzgltjb 
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

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_QZGLTJB;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "bdcs_qzgltjb", schema = "bdck")
public class BDCS_QZGLTJB extends GenerateBDCS_QZGLTJB {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "ryid")
	public String getRYID() {
		return super.getRYID();
	}

	@Override
	@Column(name = "rydlm")
	public String getRYDLM() {
		return super.getRYDLM();
	}

	@Override
	@Column(name = "cjsj")
	public Date getCJSJ() {
		return super.getCJSJ();
	}

	@Override
	@Column(name = "qsqzbh")
	public Long getQSQZBH() {
		return super.getQSQZBH();
	}

	@Override
	@Column(name = "jsqzbh")
	public Long getJSQZBH() {
		return super.getJSQZBH();
	}

	@Override
	@Column(name = "qzlx")
	public String getQZLX() {
		return super.getQZLX();
	}

	@Override
	@Column(name = "sfxsysz")
	public String getSFXSYSZ() {
		return super.getSFXSYSZ();
	}

	@Override
	@Column(name = "sfxsyzf")
	public String getSFXSYZF() {
		return super.getSFXSYZF();
	}
}
