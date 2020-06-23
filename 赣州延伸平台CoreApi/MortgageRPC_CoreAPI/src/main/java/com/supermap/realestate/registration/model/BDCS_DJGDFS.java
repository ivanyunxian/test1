package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-23 
//* ----------------------------------------
//* Public Entity bdcs_djgdfs 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_DJGDFS;

@Entity
@Table(name = "bdcs_djgdfs", schema = "bdck")
public class BDCS_DJGDFS extends GenerateBDCS_DJGDFS {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "ywh")
	public String getYWH() {
		return super.getYWH();
	}

	@Override
	@Column(name = "ysdm")
	public String getYSDM() {
		return super.getYSDM();
	}

	@Override
	@Column(name = "bz")
	public String getBZ() {
		return super.getBZ();
	}

	@Override
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}

	@Override
	@Column(name = "catalog")
	public String getCATALOG() {
		return super.getCATALOG();
	}

	@Override
	@Column(name = "filename")
	public String getFILENAME() {
		return super.getFILENAME();
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

	@Override
	@Column(name = "sjry")
	public String getSJRY() {
		return super.getSJRY();
	}

	@Override
	@Column(name = "sjsj")
	public Date getSJSJ() {
		return super.getSJSJ();
	}
}
