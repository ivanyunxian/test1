package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-3 
//* ----------------------------------------
//* Public Entity BDCS_DCXM 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateDCS_DCXM;

@Entity
@Table(name = "BDCS_DCXM", schema = "BDCDCK")
public class DCS_DCXM extends GenerateDCS_DCXM {

	@Override
	@Id
	@Column(name = "DCXMID", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "DCXMMC")
	public String getDCXMMC() {
		return super.getDCXMMC();
	}

	@Override
	@Column(name = "CJRYID")
	public Integer getCJRYID() {
		return super.getCJRYID();
	}

	@Override
	@Column(name = "CJRY")
	public String getCJRY() {
		return super.getCJRY();
	}

	@Override
	@Column(name = "CJSJ")
	public Date getCJSJ() {
		return super.getCJSJ();
	}

	@Override
	@Column(name = "DCRY")
	public String getDCRY() {
		return super.getDCRY();
	}

	@Override
	@Column(name = "DCSJ")
	public Date getDCSJ() {
		return super.getDCSJ();
	}

	@Override
	@Column(name = "XMLB")
	public String getXMLB() {
		return super.getXMLB();
	}

	@Override
	@Column(name = "YXBZ")
	public Integer getYXBZ() {
		return super.getYXBZ();
	}

	@Override
	@Column(name = "XMZT")
	public Integer getXMZT() {
		return super.getXMZT();
	}

	@Override
	@Column(name = "XMLX")
	public Integer getXMLX() {
		return super.getXMLX();
	}
}
