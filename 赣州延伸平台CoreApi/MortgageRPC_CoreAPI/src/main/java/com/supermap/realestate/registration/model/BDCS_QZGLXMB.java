package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-10-29 
//* ----------------------------------------
//* Public Entity bdcs_qzglxmb 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_QZGLXMB;

@Entity
@Table(name = "bdcs_qzglxmb", schema = "bdck")
public class BDCS_QZGLXMB extends GenerateBDCS_QZGLXMB {

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
	@Column(name = "xmlx")
	public String getXMLX() {
		return super.getXMLX();
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
	@Column(name = "sfyx")
	public String getSFYX() {
		return super.getSFYX();
	}

	@Override
	@Column(name = "sxsj")
	public Date getSXSJ() {
		return super.getSXSJ();
	}

	@Override
	@Column(name = "qzlx")
	public String getQZLX() {
		return super.getQZLX();
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
	@Column(name = "lqzhry")
	public String getLQZHRY() {
		return super.getLQZHRY();
	}
	
	@Override
	@Column(name = "zszt")
	public String getZSZT() {
		return super.getZSZT();
	}
	
	@Override
	@Column(name = "qzzl")
	public String getQZZL() {
		return super.getQZZL();
	}
	
	@Override
	@Column(name = "rkry")
	public String getRKRY() {
		return super.getRKRY();
	}
	
	@Override
	@Column(name = "qzly")
	public String getQZLY() {
		return super.getQZLY();
	}
}
