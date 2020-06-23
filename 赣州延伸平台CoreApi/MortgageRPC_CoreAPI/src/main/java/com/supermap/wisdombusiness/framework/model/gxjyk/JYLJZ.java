package com.supermap.wisdombusiness.framework.model.gxjyk;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/8 
//* ----------------------------------------
//* Public Entity ljz 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.supermap.wisdombusiness.framework.model.gxjyk.genrt.GenerateLJZ;

@Entity
@Table(name = "ljz", schema = "gxjyk")
public class JYLJZ extends GenerateLJZ {

	@Override
	@Id
	@Column(name = "bsm", length = 100)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "ljzid")
	public String getLJZID() {
		return super.getLJZID();
	}

	@Override
	@Column(name = "ysdm")
	public String getYSDM() {
		return super.getYSDM();
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
	@Column(name = "jgrq")
	public Date getJGRQ() {
		return super.getJGRQ();
	}

	@Override
	@Column(name = "gxxmbh")
	public String getGXXMBH() {
		return super.getGXXMBH();
	}

	@Override
	@Column(name = "relationid")
	public String getRELATIONID() {
		return super.getRELATIONID();
	}
}
