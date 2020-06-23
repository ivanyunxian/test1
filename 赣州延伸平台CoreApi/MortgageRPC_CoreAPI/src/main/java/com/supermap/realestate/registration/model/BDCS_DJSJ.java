package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-11 
//* ----------------------------------------
//* Public Entity bdcs_djsj 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_DJSJ;

@Entity
@Table(name = "bdcs_djsj", schema = "bdck")
public class BDCS_DJSJ extends GenerateBDCS_DJSJ {

	@Override
	@Id
	@Column(name = "bsm", length = 38)
	public Integer getId() {
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
	@Column(name = "sjsj")
	public Date getSJSJ() {
		return super.getSJSJ();
	}

	@Override
	@Column(name = "sjlx")
	public String getSJLX() {
		return super.getSJLX();
	}

	@Override
	@Column(name = "sjmc")
	public String getSJMC() {
		return super.getSJMC();
	}

	@Override
	@Column(name = "sjsl")
	public Integer getSJSL() {
		return super.getSJSL();
	}

	@Override
	@Column(name = "sfsybz")
	public Integer getSFSYBZ() {
		return super.getSFSYBZ();
	}

	@Override
	@Column(name = "sfewsj")
	public Integer getSFEWSJ() {
		return super.getSFEWSJ();
	}

	@Override
	@Column(name = "sfbcsj")
	public Integer getSFBCSJ() {
		return super.getSFBCSJ();
	}

	@Override
	@Column(name = "ys")
	public Integer getYS() {
		return super.getYS();
	}

	@Override
	@Column(name = "bz")
	public String getBZ() {
		return super.getBZ();
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
