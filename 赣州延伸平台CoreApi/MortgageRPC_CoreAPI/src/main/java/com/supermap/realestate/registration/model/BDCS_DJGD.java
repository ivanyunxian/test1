package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-11 
//* ----------------------------------------
//* Public Entity bdcs_djgd 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_DJGD;

@Entity
@Table(name = "bdcs_djgd", schema = "bdck")
public class BDCS_DJGD extends GenerateBDCS_DJGD {

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
	@Column(name = "zl")
	public String getZL() {
		return super.getZL();
	}

	@Override
	@Column(name = "qzhm")
	public String getQZHM() {
		return super.getQZHM();
	}

	@Override
	@Column(name = "jzh")
	public String getJZH() {
		return super.getJZH();
	}

	@Override
	@Column(name = "wjjs")
	public Integer getWJJS() {
		return super.getWJJS();
	}

	@Override
	@Column(name = "zys")
	public Integer getZYS() {
		return super.getZYS();
	}

	@Override
	@Column(name = "gdzr")
	public String getGDZR() {
		return super.getGDZR();
	}

	@Override
	@Column(name = "gdsj")
	public Date getGDSJ() {
		return super.getGDSJ();
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
