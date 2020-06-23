package com.supermap.wisdombusiness.framework.model.gxjyk;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/8 
//* ----------------------------------------
//* Public Entity c 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.supermap.wisdombusiness.framework.model.gxjyk.genrt.GenerateC;

@Entity
@Table(name = "c", schema = "gxjyk")
public class JYC extends GenerateC {

	@Override
	@Id
	@Column(name = "bsm", length = 100)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "cid")
	public String getCID() {
		return super.getCID();
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
	@Column(name = "cbqmj")
	public Double getCBQMJ() {
		return super.getCBQMJ();
	}

	@Override
	@Column(name = "ljzid")
	public String getLJZID() {
		return super.getLJZID();
	}

	@Override
	@Column(name = "gxxmbh")
	public String getGXXMBH() {
		return super.getGXXMBH();
	}
}
