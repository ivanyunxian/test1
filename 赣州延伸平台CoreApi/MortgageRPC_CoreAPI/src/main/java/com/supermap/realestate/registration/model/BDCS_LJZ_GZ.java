package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-7-11 
//* ----------------------------------------
//* Public Entity bdcs_ljz_gz 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_LJZ_GZ;
import com.supermap.realestate.registration.model.interfaces.LogicBuilding;

@Entity
@Table(name = "bdcs_ljz_gz", schema = "bdck")
public class BDCS_LJZ_GZ extends GenerateBDCS_LJZ_GZ implements LogicBuilding {

	@Override
	@Id
	@Column(name = "ljzid", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "ysdm")
	public String getYSDM() {
		return super.getYSDM();
	}

	@Override
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}

	@Override
	@Column(name = "ljzh")
	public String getLJZH() {
		return super.getLJZH();
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
	@Column(name = "mph")
	public String getMPH() {
		return super.getMPH();
	}

	@Override
	@Column(name = "ycjzmj")
	public Double getYCJZMJ() {
		return super.getYCJZMJ();
	}

	@Override
	@Column(name = "ycdxmj")
	public Double getYCDXMJ() {
		return super.getYCDXMJ();
	}

	@Override
	@Column(name = "ycqtmj")
	public Double getYCQTMJ() {
		return super.getYCQTMJ();
	}

	@Override
	@Column(name = "scjzmj")
	public Double getSCJZMJ() {
		return super.getSCJZMJ();
	}

	@Override
	@Column(name = "scdxmj")
	public Double getSCDXMJ() {
		return super.getSCDXMJ();
	}

	@Override
	@Column(name = "scqtmj")
	public Double getSCQTMJ() {
		return super.getSCQTMJ();
	}

	@Override
	@Column(name = "fwjg1")
	public String getFWJG1() {
		return super.getFWJG1();
	}

	@Override
	@Column(name = "fwjg2")
	public String getFWJG2() {
		return super.getFWJG2();
	}

	@Override
	@Column(name = "fwjg3")
	public String getFWJG3() {
		return super.getFWJG3();
	}

	@Override
	@Column(name = "jzwzt")
	public String getJZWZT() {
		return super.getJZWZT();
	}

	@Override
	@Column(name = "fwyt1")
	public String getFWYT1() {
		return super.getFWYT1();
	}

	@Override
	@Column(name = "fwyt2")
	public String getFWYT2() {
		return super.getFWYT2();
	}

	@Override
	@Column(name = "fwyt3")
	public String getFWYT3() {
		return super.getFWYT3();
	}

	@Override
	@Column(name = "zcs")
	public Integer getZCS() {
		return super.getZCS();
	}

	@Override
	@Column(name = "dscs")
	public Integer getDSCS() {
		return super.getDSCS();
	}

	@Override
	@Column(name = "dxcs")
	public Integer getDXCS() {
		return super.getDXCS();
	}

	@Override
	@Column(name = "bz")
	public String getBZ() {
		return super.getBZ();
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
	@Override
@Column(name = "relationid")
public String getRELATIONID() {
return super.getRELATIONID();
}
}
