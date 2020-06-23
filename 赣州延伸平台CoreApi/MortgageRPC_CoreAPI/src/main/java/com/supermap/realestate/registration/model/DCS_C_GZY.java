package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2016/11/9 
//* ----------------------------------------
//* Public Entity bdcs_c_gzy 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateDCS_C_GZY;
import com.supermap.realestate.registration.model.interfaces.Floor;

@Entity
@Table(name = "bdcs_c_gzy", schema = "bdcdck")
public class DCS_C_GZY extends GenerateDCS_C_GZY implements Floor{

	@Override
	@Id
	@Column(name = "cid", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}

	@Override
	@Column(name = "ysdm")
	public String getYSDM() {
		return super.getYSDM();
	}

	@Override
	@Column(name = "ch")
	public String getCH() {
		return super.getCH();
	}

	@Override
	@Column(name = "zrzbdcdyid")
	public String getZRZBDCDYID() {
		return super.getZRZBDCDYID();
	}

	@Override
	@Column(name = "zrzh")
	public String getZRZH() {
		return super.getZRZH();
	}

	@Override
	@Column(name = "sjc")
	public Integer getSJC() {
		return super.getSJC();
	}

	@Override
	@Column(name = "myc")
	public String getMYC() {
		return super.getMYC();
	}

	@Override
	@Column(name = "cjzmj")
	public Double getCJZMJ() {
		return super.getCJZMJ();
	}

	@Override
	@Column(name = "ctnjzmj")
	public Double getCTNJZMJ() {
		return super.getCTNJZMJ();
	}

	@Override
	@Column(name = "cytmj")
	public Double getCYTMJ() {
		return super.getCYTMJ();
	}

	@Override
	@Column(name = "cgyjzmj")
	public Double getCGYJZMJ() {
		return super.getCGYJZMJ();
	}

	@Override
	@Column(name = "cftjzmj")
	public Double getCFTJZMJ() {
		return super.getCFTJZMJ();
	}

	@Override
	@Column(name = "cg")
	public Double getCG() {
		return super.getCG();
	}

	@Override
	@Column(name = "sptymj")
	public Double getSPTYMJ() {
		return super.getSPTYMJ();
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
