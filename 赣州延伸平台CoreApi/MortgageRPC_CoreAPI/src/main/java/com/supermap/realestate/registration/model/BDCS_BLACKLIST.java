package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2017/04/13 
//* ----------------------------------------
//* Public Entity bdcs_blacklist 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_BLACKLIST;

@Entity
@Table(name = "bdcs_blacklist", schema = "bdck")
public class BDCS_BLACKLIST extends GenerateBDCS_BLACKLIST {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "qlrmc")
	public String getQLRMC() {
		return super.getQLRMC();
	}

	@Override
	@Column(name = "zjh")
	public String getZJH() {
		return super.getZJH();
	}
	
	@Override
	@Column(name = "qzh")
	public String getQZH() {
		return super.getQZH();
	}

	@Override
	@Column(name = "detail")
	public String getDETAIL() {
		return super.getDETAIL();
	}
	
	@Override
	@Column(name = "yxbz")
	public String getYXBZ() {
		return super.getYXBZ();
	}
}
