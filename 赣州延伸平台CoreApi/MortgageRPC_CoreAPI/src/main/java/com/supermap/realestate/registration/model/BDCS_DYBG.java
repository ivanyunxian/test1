package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-12 
//* ----------------------------------------
//* Public Entity bdcs_dybg 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_DYBG;

@Entity
@Table(name = "bdcs_dybg", schema = "bdck")
public class BDCS_DYBG extends GenerateBDCS_DYBG {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "ldjdyid")
	public String getLDJDYID() {
		return super.getLDJDYID();
	}

	@Override
	@Column(name = "lbdcdyid")
	public String getLBDCDYID() {
		return super.getLBDCDYID();
	}

	@Override
	@Column(name = "xdjdyid")
	public String getXDJDYID() {
		return super.getXDJDYID();
	}

	@Override
	@Column(name = "xbdcdyid")
	public String getXBDCDYID() {
		return super.getXBDCDYID();
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
