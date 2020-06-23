package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-10-29 
//* ----------------------------------------
//* Public Entity bdcs_ysbzgg 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_YSBZGG;

@Entity
@Table(name = "bdcs_ysbzgg", schema = "bdck")
public class BDCS_YSBZGG extends GenerateBDCS_YSBZGG {

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
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}

	@Override
	@Column(name = "bdcqzh")
	public String getBDCQZH() {
		return super.getBDCQZH();
	}

	@Override
	@Column(name = "qlid")
	public String getQLID() {
		return super.getQLID();
	}

	@Override
	@Column(name = "ggqssj")
	public Date getGGQSSJ() {
		return super.getGGQSSJ();
	}

	@Override
	@Column(name = "ggjssj")
	public Date getGGJSSJ() {
		return super.getGGJSSJ();
	}

	@Override
	@Column(name = "ggsm")
	public String getGGSM() {
		return super.getGGSM();
	}

	@Override
	@Column(name = "bzzt")
	public String getBZZT() {
		return super.getBZZT();
	}
}
