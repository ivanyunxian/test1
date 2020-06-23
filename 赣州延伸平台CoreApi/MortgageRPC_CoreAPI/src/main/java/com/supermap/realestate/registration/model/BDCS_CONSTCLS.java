package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-11 
//* ----------------------------------------
//* Public Entity bdcs_constcls 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_CONSTCLS;

@Entity
@Table(name = "bdcs_constcls", schema = "bdck")
public class BDCS_CONSTCLS extends GenerateBDCS_CONSTCLS {

	@Override
	@Id
	@Column(name = "mbbsm", length = 38)
	public Integer getId() {
		return super.getId();
	}

	@Override
	@Column(name = "constslsid")
	public Integer getCONSTSLSID() {
		return super.getCONSTSLSID();
	}

	@Override
	@Column(name = "constclsname")
	public String getCONSTCLSNAME() {
		return super.getCONSTCLSNAME();
	}

	@Override
	@Column(name = "constclstype")
	public String getCONSTCLSTYPE() {
		return super.getCONSTCLSTYPE();
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
