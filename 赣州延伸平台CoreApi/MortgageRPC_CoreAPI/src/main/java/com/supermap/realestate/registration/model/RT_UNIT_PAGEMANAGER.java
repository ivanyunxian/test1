package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2016/5/11 
//* ----------------------------------------
//* Public Entity rt_unit_pagemanager 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateRT_UNIT_PAGEMANAGER;

@Entity
@Table(name = "rt_unit_pagemanager", schema = "bdck")
public class RT_UNIT_PAGEMANAGER extends GenerateRT_UNIT_PAGEMANAGER {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "moduleid")
	public String getMODULEID() {
		return super.getMODULEID();
	}

	@Override
	@Column(name = "sxh")
	public Integer getSXH() {
		return super.getSXH();
	}

	@Override
	@Column(name = "titlevisible")
	public String getTITLEVISIBLE() {
		return super.getTITLEVISIBLE();
	}
	
	@Override
	@Column(name = "editable")
	public String getEDITABLE() {
		return super.getEDITABLE();
	}

	@Override
	@Column(name = "title")
	public String getTITLE() {
		return super.getTITLE();
	}

	@Override
	@Column(name = "pageid")
	public String getPAGEID() {
		return super.getPAGEID();
	}
}
