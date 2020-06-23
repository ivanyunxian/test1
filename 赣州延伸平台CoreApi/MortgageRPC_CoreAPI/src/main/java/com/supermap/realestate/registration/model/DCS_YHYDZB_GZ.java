package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-25 
//* ----------------------------------------
//* Public Entity bdcs_yhydzb_gz 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateDCS_YHYDZB_GZ;
import com.supermap.realestate.registration.model.interfaces.YHYDZB;

@Entity
@Table(name = "bdcs_yhydzb_gz", schema = "bdcdck")
public class DCS_YHYDZB_GZ extends GenerateDCS_YHYDZB_GZ implements YHYDZB {

	@Override
	@Id
	@Column(name = "ID", length = 50)
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
	@Column(name = "zhdm")
	public String getZHDM() {
		return super.getZHDM();
	}

	@Override
	@Column(name = "xh")
	public Integer getXH() {
		return super.getXH();
	}

	@Override
	@Column(name = "bhyh")
	public Double getBHYH() {
		return super.getBHYH();
	}

	@Override
	@Column(name = "bhnr")
	public Double getBHNR() {
		return super.getBHNR();
	}

	@Override
	@Column(name = "dcxmid")
	public String getDCXMID() {
		return super.getDCXMID();
	}

	@Override
	@Column(name = "createtime")
	public Date getCREATETIME() {
		return super.getCREATETIME();
	}

	@Override
	@Column(name = "modifytime")
	public Date getMODIFYTIME() {
		return super.getMODIFYTIME();
	}
}
