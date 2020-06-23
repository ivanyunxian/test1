package com.supermap.wisdombusiness.framework.model.gxjyk;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/8 
//* ----------------------------------------
//* Public Entity ygdj 
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
import com.supermap.wisdombusiness.framework.model.gxjyk.genrt.GenerateYGDJ;

@Entity
@Table(name = "ygdj", schema = "gxjyk")
public class JYYGDJ extends GenerateYGDJ {

	@Override
	@Id
	@Column(name = "bsm", length = 100)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "ysdm")
	public String getYSDM() {
		return super.getYSDM();
	}

	@Override
	@Column(name = "ywh")
	public String getYWH() {
		return super.getYWH();
	}

	@Override
	@Column(name = "bdcdyh")
	public String getBDCDYH() {
		return super.getBDCDYH();
	}

	@Override
	@Column(name = "qlid")
	public String getQLID() {
		return super.getQLID();
	}

	@Override
	@Column(name = "djlx")
	public String getDJLX() {
		return super.getDJLX();
	}

	@Override
	@Column(name = "djyy")
	public String getDJYY() {
		return super.getDJYY();
	}

	@Override
	@Column(name = "bdcdjzmh")
	public String getBDCDJZMH() {
		return super.getBDCDJZMH();
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
	@Column(name = "bdczl")
	public String getBDCZL() {
		return super.getBDCZL();
	}

	@Override
	@Column(name = "ywr")
	public String getYWR() {
		return super.getYWR();
	}

	@Override
	@Column(name = "ywrzjzl")
	public String getYWRZJZL() {
		return super.getYWRZJZL();
	}

	@Override
	@Column(name = "ywrzjh")
	public String getYWRZJH() {
		return super.getYWRZJH();
	}

	@Override
	@Column(name = "ygdjzl")
	public String getYGDJZL() {
		return super.getYGDJZL();
	}

	@Override
	@Column(name = "tdsyqr")
	public String getTDSYQR() {
		return super.getTDSYQR();
	}

	@Override
	@Column(name = "ghyt")
	public String getGHYT() {
		return super.getGHYT();
	}

	@Override
	@Column(name = "fwxz")
	public String getFWXZ() {
		return super.getFWXZ();
	}

	@Override
	@Column(name = "fwjg")
	public String getFWJG() {
		return super.getFWJG();
	}

	@Override
	@Column(name = "szc")
	public String getSZC() {
		return super.getSZC();
	}

	@Override
	@Column(name = "zcs")
	public Integer getZCS() {
		return super.getZCS();
	}

	@Override
	@Column(name = "jzmj")
	public Double getJZMJ() {
		return super.getJZMJ();
	}

	@Override
	@Column(name = "qdjg")
	public Double getQDJG() {
		return super.getQDJG();
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
	@Override
	@Column(name = "tsbh")
	public String getTSBH() {
		return super.getTSBH();
	}
}
