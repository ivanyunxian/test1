package com.supermap.wisdombusiness.framework.model.gxjyk;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/8 
//* ----------------------------------------
//* Public Entity zrz 
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
import com.supermap.wisdombusiness.framework.model.gxjyk.genrt.GenerateZRZ;

@Entity
@Table(name = "zrz", schema = "gxjyk")
public class JYZRZ extends GenerateZRZ {

	@Override
	@Id
	@Column(name = "bsm", length = 100)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "bdcdyid")
	public String getBDCDYID() {
		return super.getBDCDYID();
	}

	@Override
	@Column(name = "ysdm")
	public String getYSDM() {
		return super.getYSDM();
	}

	@Override
	@Column(name = "xmmc")
	public String getXMMC() {
		return super.getXMMC();
	}

	@Override
	@Column(name = "bdcdyh")
	public String getBDCDYH() {
		return super.getBDCDYH();
	}

	@Override
	@Column(name = "zddm")
	public String getZDDM() {
		return super.getZDDM();
	}

	@Override
	@Column(name = "zrzh")
	public String getZRZH() {
		return super.getZRZH();
	}

	@Override
	@Column(name = "zl")
	public String getZL() {
		return super.getZL();
	}

	@Override
	@Column(name = "jzwmc")
	public String getJZWMC() {
		return super.getJZWMC();
	}

	@Override
	@Column(name = "jgrq")
	public Date getJGRQ() {
		return super.getJGRQ();
	}

	@Override
	@Column(name = "jzwgd")
	public Double getJZWGD() {
		return super.getJZWGD();
	}

	@Override
	@Column(name = "zzdmj")
	public Double getZZDMJ() {
		return super.getZZDMJ();
	}

	@Override
	@Column(name = "zydmj")
	public Double getZYDMJ() {
		return super.getZYDMJ();
	}

	@Override
	@Column(name = "ycjzmj")
	public Double getYCJZMJ() {
		return super.getYCJZMJ();
	}

	@Override
	@Column(name = "scjzmj")
	public Double getSCJZMJ() {
		return super.getSCJZMJ();
	}

	@Override
	@Column(name = "fdcjyjg")
	public Double getFDCJYJG() {
		return super.getFDCJYJG();
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
	@Column(name = "dxsd")
	public Double getDXSD() {
		return super.getDXSD();
	}

	@Override
	@Column(name = "ghyt")
	public String getGHYT() {
		return super.getGHYT();
	}

	@Override
	@Column(name = "fwjg")
	public String getFWJG() {
		return super.getFWJG();
	}

	@Override
	@Column(name = "zts")
	public Integer getZTS() {
		return super.getZTS();
	}

	@Override
	@Column(name = "jzwjbyt")
	public String getJZWJBYT() {
		return super.getJZWJBYT();
	}

	@Override
	@Column(name = "bz")
	public String getBZ() {
		return super.getBZ();
	}

	@Override
	@Column(name = "zt")
	public String getZT() {
		return super.getZT();
	}

	@Override
	@Column(name = "relationid")
	public String getRELATIONID() {
		return super.getRELATIONID();
	}

	@Override
	@Column(name = "gxxmbh")
	public String getGXXMBH() {
		return super.getGXXMBH();
	}

	@Override
	@Column(name = "fwzt")
	public String getFWZT() {
		return super.getFWZT();
	}
}
