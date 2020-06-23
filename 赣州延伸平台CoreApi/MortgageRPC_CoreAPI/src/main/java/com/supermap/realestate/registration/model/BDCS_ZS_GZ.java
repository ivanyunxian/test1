package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2016-1-20 
//* ----------------------------------------
//* Public Entity bdcs_zs_gz 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.supermap.realestate.registration.util.ConstHelper;
import javax.persistence.Transient;
import java.util.Date;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_ZS_GZ;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "bdcs_zs_gz", schema = "bdck")
public class BDCS_ZS_GZ extends GenerateBDCS_ZS_GZ {

	@Override
	@Id
	@Column(name = "zsid", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}

	@Override
	@Column(name = "zsbh")
	public String getZSBH() {
		return super.getZSBH();
	}

	@Override
	@Column(name = "bdcqzh")
	public String getBDCQZH() {
		return super.getBDCQZH();
	}

	@Override
	@Column(name = "szsj")
	public Date getSZSJ() {
		return super.getSZSJ();
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
	@Column(name = "cfdagh")
	public String getCFDAGH() {
		return super.getCFDAGH();
	}

	@Override
	@Column(name = "zsdata")
	public String getZSDATA() {
		return super.getZSDATA();
	}
}
