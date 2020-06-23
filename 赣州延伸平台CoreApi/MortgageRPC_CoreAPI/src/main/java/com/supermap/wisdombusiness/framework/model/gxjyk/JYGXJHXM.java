package com.supermap.wisdombusiness.framework.model.gxjyk;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/8 
//* ----------------------------------------
//* Public Entity gxjhxm 
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
import com.supermap.wisdombusiness.framework.model.gxjyk.genrt.GenerateGXJHXM;

@Entity
@Table(name = "gxjhxm", schema = "gxjyk")
public class JYGXJHXM extends GenerateGXJHXM {

	@Override
	@Id
	@Column(name = "bsm", length = 100)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "bdcdyh")
	public String getBDCDYH() {
		return super.getBDCDYH();
	}

	@Override
	@Column(name = "qllx")
	public String getQLLX() {
		return super.getQLLX();
	}

	@Override
	@Column(name = "gxlx")
	public String getGXLX() {
		return super.getGXLX();
	}

	@Override
	@Column(name = "xmmc")
	public String getXMMC() {
		return super.getXMMC();
	}

	@Override
	@Column(name = "zl")
	public String getZL() {
		return super.getZL();
	}

	@Override
	@Column(name = "gxxmbh")
	public String getGXXMBH() {
		return super.getGXXMBH();
	}

	@Override
	@Column(name = "djdl")
	public String getDJDL() {
		return super.getDJDL();
	}

	@Override
	@Column(name = "djxl")
	public String getDJXL() {
		return super.getDJXL();
	}

	@Override
	@Column(name = "sqr")
	public String getSQR() {
		return super.getSQR();
	}

	@Override
	@Column(name = "slsj")
	public Date getSLSJ() {
		return super.getSLSJ();
	}

	@Override
	@Column(name = "slry")
	public String getSLRY() {
		return super.getSLRY();
	}

	@Override
	@Column(name = "zt")
	public String getZT() {
		return super.getZT();
	}

	@Override
	@Column(name = "bljd")
	public String getBLJD() {
		return super.getBLJD();
	}

	@Override
	@Column(name = "casenum")
	public String getCASENUM() {
		return super.getCASENUM();
	}

	@Override
	@Column(name = "qlsdfs")
	public String getQLSDFS() {
		return super.getQLSDFS();
	}

	@Override
	@Column(name = "project_id")
	public String getPROJECT_ID() {
		return super.getPROJECT_ID();
	}

	@Override
	@Column(name = "tssj")
	public Date getTSSJ() {
		return super.getTSSJ();
	}

	@Override
	@Column(name = "xmid")
	public Integer getXMID() {
		return super.getXMID();
	}

	@Override
	@Column(name = "relationid")
	public Byte[] getRELATIONID() {
		return super.getRELATIONID();
	}

	@Override
	@Column(name = "tsbh")
	public String gettSBH() {
		return super.gettSBH();
	}

	@Override
	@Column(name = "cqsj")
	public Date getCQSJ() {
		return super.getCQSJ();
	}
	
	@Override
	@Column(name = "cqsj2")
	public Date getCQSJ2() {
		return super.getCQSJ2();
	}
}
