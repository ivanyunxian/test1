package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2016/6/27 
//* ----------------------------------------
//* Public Entity bdcs_ztcxlog 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_ZTCXLOG;

@Entity
@Table(name = "bdcs_ztcxlog", schema = "bdck")
public class BDCS_ZTCXLOG extends GenerateBDCS_ZTCXLOG {

	@Override
	@Id
	@Column(name = "id", length = 4000)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "cxsj")
	public Date getCXSJ() {
		return super.getCXSJ();
	}

	@Override
	@Column(name = "cxtj")
	public String getCXTJ() {
		return super.getCXTJ();
	}

	@Override
	@Column(name = "ip")
	public String getIP() {
		return super.getIP();
	}

}
