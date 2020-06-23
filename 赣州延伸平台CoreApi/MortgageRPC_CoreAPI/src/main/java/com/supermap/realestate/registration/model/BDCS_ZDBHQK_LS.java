package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-29 
//* ----------------------------------------
//* Public Entity bdcs_zdbhqk_ls 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_ZDBHQK_LS;

@Entity
@Table(name = "bdcs_zdbhqk_ls", schema = "bdck")
public class BDCS_ZDBHQK_LS extends GenerateBDCS_ZDBHQK_LS {

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
	@Column(name = "zddm")
	public String getZDDM() {
		return super.getZDDM();
	}

	@Override
	@Column(name = "bhyh")
	public String getBHYH() {
		return super.getBHYH();
	}

	@Override
	@Column(name = "bhnr")
	public String getBHNR() {
		return super.getBHNR();
	}

	@Override
	@Column(name = "djsj")
	public Date getDJSJ() {
		return super.getDJSJ();
	}

	@Override
	@Column(name = "dbr")
	public String getDBR() {
		return super.getDBR();
	}

	@Override
	@Column(name = "fj")
	public String getFJ() {
		return super.getFJ();
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
