package com.supermap.wisdombusiness.framework.model.gxjyk;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/8 
//* ----------------------------------------
//* Public Entity xzdj 
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
import com.supermap.wisdombusiness.framework.model.gxjyk.genrt.GenerateXZDJ;

@Entity
@Table(name = "xzdj", schema = "gxjyk")
public class JYXZDJ extends GenerateXZDJ {

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
	@Column(name = "ywh")
	public String getYWH() {
		return super.getYWH();
	}

	@Override
	@Column(name = "qlid")
	public String getQLID() {
		return super.getQLID();
	}

	@Override
	@Column(name = "cflx")
	public String getCFLX() {
		return super.getCFLX();
	}

	@Override
	@Column(name = "cfjg")
	public String getCFJG() {
		return super.getCFJG();
	}

	@Override
	@Column(name = "cfwh")
	public String getCFWH() {
		return super.getCFWH();
	}

	@Override
	@Column(name = "cfwj")
	public String getCFWJ() {
		return super.getCFWJ();
	}

	@Override
	@Column(name = "cffw")
	public String getCFFW() {
		return super.getCFFW();
	}

	@Override
	@Column(name = "jfjg")
	public String getJFJG() {
		return super.getJFJG();
	}

	@Override
	@Column(name = "jfwh")
	public String getJFWH() {
		return super.getJFWH();
	}

	@Override
	@Column(name = "jfwj")
	public String getJFWJ() {
		return super.getJFWJ();
	}

	@Override
	@Column(name = "cfqssj")
	public Date getCFQSSJ() {
		return super.getCFQSSJ();
	}

	@Override
	@Column(name = "cfjssj")
	public Date getCFJSSJ() {
		return super.getCFJSSJ();
	}

	@Override
	@Column(name = "qxdm")
	public String getQXDM() {
		return super.getQXDM();
	}

	@Override
	@Column(name = "djjg")
	public String getDJJG() {
		return super.getDJJG();
	}

	@Override
	@Column(name = "dbr")
	public String getDBR() {
		return super.getDBR();
	}

	@Override
	@Column(name = "djsj")
	public Date getDJSJ() {
		return super.getDJSJ();
	}

	@Override
	@Column(name = "jfywh")
	public String getJFYWH() {
		return super.getJFYWH();
	}

	@Override
	@Column(name = "jfdbr")
	public String getJFDBR() {
		return super.getJFDBR();
	}

	@Override
	@Column(name = "jfdjsj")
	public String getJFDJSJ() {
		return super.getJFDJSJ();
	}

	@Override
	@Column(name = "fj")
	public String getFJ() {
		return super.getFJ();
	}

	@Override
	@Column(name = "qszt")
	public String getQSZT() {
		return super.getQSZT();
	}

	@Override
	@Column(name = "bdcdyid")
	public String getBDCDYID() {
		return super.getBDCDYID();
	}

	@Override
	@Column(name = "gxxmbh")
	public String getGXXMBH() {
		return super.getGXXMBH();
	}

	@Override
	@Column(name = "zsbh")
	public String getZSBH() {
		return super.getZSBH();
	}

	@Override
	@Column(name = "bz")
	public String getBZ() {
		return super.getBZ();
	}
}
