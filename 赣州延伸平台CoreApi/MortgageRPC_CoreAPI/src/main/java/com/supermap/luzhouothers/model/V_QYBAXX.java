package com.supermap.luzhouothers.model;

///*****************************************
//* AutoGenerate by CodeTools 2016/1/23 
//* ----------------------------------------
//* Public Entity qybaxx 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.luzhouothers.model.genrt.GenerateV_QYBAXX;

@Entity
@Table(name = "v_qybaxx", schema = "bdck")
public class V_QYBAXX extends GenerateV_QYBAXX {

	@Override
	@Id
	@Column(name = "id", length = 4000)
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
	@Column(name = "hth")
	public String getHTH() {
		return super.getHTH();
	}

	@Override
	@Column(name = "qyrq")
	public Date getQYRQ() {
		return super.getQYRQ();
	}

	@Override
	@Column(name = "gyfs")
	public String getGYFS() {
		return super.getGYFS();
	}

	@Override
	@Column(name = "dj")
	public Double getDJ() {
		return super.getDJ();
	}

	@Override
	@Column(name = "zj")
	public Double getZJ() {
		return super.getZJ();
	}

	@Override
	@Column(name = "qzh")
	public String getQZH() {
		return super.getQZH();
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
	@Column(name = "seqid_w")
	public Integer getSEQID_W() {
		return super.getSEQID_W();
	}

	@Override
	@Column(name = "seqid_r")
	public Integer getSEQID_R() {
		return super.getSEQID_R();
	}

	@Override
	@Column(name = "createdatetime")
	public Date getCREATEDATETIME() {
		return super.getCREATEDATETIME();
	}

	@Override
	@Column(name = "loaddatetime")
	public Date getLOADDATETIME() {
		return super.getLOADDATETIME();
	}

	@Override
	@Column(name = "fwbm")
	public String getFWBM() {
		return super.getFWBM();
	}
}
