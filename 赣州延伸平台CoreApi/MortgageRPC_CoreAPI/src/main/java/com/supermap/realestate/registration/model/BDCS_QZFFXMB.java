package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2017-03-21 
//* ----------------------------------------
//* Public Entity bdcs_qzffxmb 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_QZFFXMB;

@Entity
@Table(name = "bdcs_qzffxmb", schema = "bdck")
public class BDCS_QZFFXMB extends GenerateBDCS_QZFFXMB {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "xmmc")
	public String getXMMC() {
		return super.getXMMC();
	}
	
	@Override
	@Column(name = "xmid")
	public String getXMID() {
		return super.getXMID();
	}

	@Override
	@Column(name = "fffs")
	public String getFFFS() {
		return super.getFFFS();
	}

	@Override
	@Column(name = "lqry")
	public String getLQRY() {
		return super.getLQRY();
	}

	@Override
	@Column(name = "lqryid")
	public String getLQRYID() {
		return super.getLQRYID();
	}
	
	@Override
	@Column(name = "lqks")
	public String getLQKS() {
		return super.getLQKS();
	}

	@Override
	@Column(name = "lqksid")
	public String getLQKSID() {
		return super.getLQKSID();
	}

	@Override
	@Column(name = "ffyy")
	public String getFFYY() {
		return super.getFFYY();
	}

	@Override
	@Column(name = "qzlx")
	public String getQZLX() {
		return super.getQZLX();
	}

	@Override
	@Column(name = "qzzl")
	public String getQZZL() {
		return super.getQZZL();
	}

	@Override
	@Column(name = "qsqzbh")
	public Long getQSQZBH() {
		return super.getQSQZBH();
	}

	@Override
	@Column(name = "jsqzbh")
	public Long getJSQZBH() {
		return super.getJSQZBH();
	}

	@Override
	@Column(name = "cjry")
	public String getCJRY() {
		return super.getCJRY();
	}

	@Override
	@Column(name = "cjsj")
	public Date getCJSJ() {
		return super.getCJSJ();
	}

	@Override
	@Column(name = "sfrk")
	public String getSFRK() {
		return super.getSFRK();
	}

	@Override
	@Column(name = "rksj")
	public Date getRKSJ() {
		return super.getRKSJ();
	}

	@Override
	@Column(name = "rkry")
	public String getRKRY() {
		return super.getRKRY();
	}
}
