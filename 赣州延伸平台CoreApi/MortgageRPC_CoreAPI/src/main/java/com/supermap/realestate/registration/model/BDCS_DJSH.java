package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-11 
//* ----------------------------------------
//* Public Entity bdcs_djsh 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_DJSH;

@Entity
@Table(name = "bdcs_djsh", schema = "bdck")
public class BDCS_DJSH extends GenerateBDCS_DJSH {

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
	@Column(name = "jdmc")
	public String getJDMC() {
		return super.getJDMC();
	}

	@Override
	@Column(name = "sxh")
	public Integer getSXH() {
		return super.getSXH();
	}

	@Override
	@Column(name = "shryxm")
	public String getSHRYXM() {
		return super.getSHRYXM();
	}

	@Override
	@Column(name = "shkssj")
	public Date getSHKSSJ() {
		return super.getSHKSSJ();
	}

	@Override
	@Column(name = "shjssj")
	public Date getSHJSSJ() {
		return super.getSHJSSJ();
	}

	@Override
	@Column(name = "shyj")
	public String getSHYJ() {
		return super.getSHYJ();
	}

	@Override
	@Column(name = "czjg")
	public String getCZJG() {
		return super.getCZJG();
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
