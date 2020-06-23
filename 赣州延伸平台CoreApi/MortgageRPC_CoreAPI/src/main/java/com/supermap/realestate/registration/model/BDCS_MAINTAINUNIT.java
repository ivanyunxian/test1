package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2016-8-26 
//* ----------------------------------------
//* Public Entity bdcs_maintainunit 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_MAINTAINUNIT;

@Entity
@Table(name = "bdcs_maintainunit", schema = "bdck")
public class BDCS_MAINTAINUNIT extends GenerateBDCS_MAINTAINUNIT {

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
	@Column(name = "lbdcdyh")
	public String getLBDCDYH() {
		return super.getLBDCDYH();
	}

	@Override
	@Column(name = "lzl")
	public String getLZL() {
		return super.getLZL();
	}

	@Override
	@Column(name = "lbdcqzh")
	public String getLBDCQZH() {
		return super.getLBDCQZH();
	}

	@Override
	@Column(name = "lcqr")
	public String getLCQR() {
		return super.getLCQR();
	}

	@Override
	@Column(name = "xbdcdyh")
	public String getXBDCDYH() {
		return super.getXBDCDYH();
	}

	@Override
	@Column(name = "xzl")
	public String getXZL() {
		return super.getXZL();
	}

	@Override
	@Column(name = "xbdcqzh")
	public String getXBDCQZH() {
		return super.getXBDCQZH();
	}

	@Override
	@Column(name = "xcqr")
	public String getXCQR() {
		return super.getXCQR();
	}

	@Override
	@Column(name = "xgrq")
	public Date getXGRQ() {
		return super.getXGRQ();
	}

	@Override
	@Column(name = "xgr")
	public String getXGR() {
		return super.getXGR();
	}
}
