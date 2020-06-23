package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2017-05-24 
//* ----------------------------------------
//* Public Entity t_config 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_GEO;
import com.supermap.realestate.registration.model.genrt.GenerateT_CONFIG;


@Entity
@Table(name = "bdcs_geo", schema = "BDCK")
public class BDCS_GEO extends GenerateBDCS_GEO {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "bdcdyid")
	public String getBDCDYID() {
		return super.getBDCDYID();
	}

	@Override
	@Column(name = "bdcdyh")
	public String getBDCDYH() {
		return super.getBDCDYH();
	}

	@Override
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}

	@Override
	@Column(name = "zl")
	public String getZL() {
		return super.getZL();
	}

	@Override
	@Column(name = "fwbm")
	public String getFWBM() {
		return super.getFWBM();
	}

	@Override
	@Column(name = "zrzbdcdyid")
	public String getZRZBDCDYID() {
		return super.getZRZBDCDYID();
	}
	
	@Override
	@Column(name = "zrzbdcdyh")
	public String getZRZBDCDYH() {
		return super.getZRZBDCDYH();
	}
	
	@Override
	@Column(name = "xzb")
	public Double getXZB() {
		return super.getXZB();
	}

	@Override
	@Column(name = "yzb")
	public Double getYZB() {
		return super.getYZB();
	}

	@Override
	@Column(name = "gl")
	public String getGL() {
		return super.getGL();
	}

	@Override
	@Column(name = "zd")
	public String getZD() {
		return super.getZD();
	}

	@Override
	@Column(name = "time")
	public Date getTIME() {
		return super.getTIME();
	}

	@Override
	@Column(name = "zrry")
	public String getZRRY() {
		return super.getZRRY();
	}

	@Override
	@Column(name = "sm")
	public String getSM() {
		return super.getSM();
	}
	
	@Override
	@Column(name = "picture")
	public String getPICTURE() {
		return super.getPICTURE();
	}
}
