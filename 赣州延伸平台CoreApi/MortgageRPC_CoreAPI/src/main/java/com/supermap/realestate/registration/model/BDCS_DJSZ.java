package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-11 
//* ----------------------------------------
//* Public Entity bdcs_djsz 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_DJSZ;

@Entity
@Table(name = "bdcs_djsz", schema = "bdck")
public class BDCS_DJSZ extends GenerateBDCS_DJSZ {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "ywh")
	public String getYWH() {
		return super.getYWH();
	}

	@Override
	@Column(name = "ysdm")
	public String getYSDM() {
		return super.getYSDM();
	}

	@Override
	@Column(name = "szmc")
	public String getSZMC() {
		return super.getSZMC();
	}

	@Override
	@Column(name = "szzh")
	public String getSZZH() {
		return super.getSZZH();
	}

	@Override
	@Column(name = "ysxlh")
	public String getYSXLH() {
		return super.getYSXLH();
	}

	@Override
	@Column(name = "szry")
	public String getSZRY() {
		return super.getSZRY();
	}

	@Override
	@Column(name = "szsj")
	public Date getSZSJ() {
		return super.getSZSJ();
	}

	@Override
	@Column(name = "bz")
	public String getBZ() {
		return super.getBZ();
	}

	@Override
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}

	@Override
	@Column(name = "createtime")
	public Date getCreateTime() {
		return super.getCreateTime();
	}

	@Override
	@Column(name = "modifytime")
	public Date getModifyTime() {
		return super.getModifyTime();
	}
}
