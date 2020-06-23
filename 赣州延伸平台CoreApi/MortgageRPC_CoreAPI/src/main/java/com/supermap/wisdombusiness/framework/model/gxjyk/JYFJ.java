package com.supermap.wisdombusiness.framework.model.gxjyk;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/8 
//* ----------------------------------------
//* Public Entity fj 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.supermap.wisdombusiness.framework.model.gxjyk.genrt.GenerateFJ;

@Entity
@Table(name = "fj", schema = "gxjyk")
public class JYFJ extends GenerateFJ {

	@Override
	@Id
	@Column(name = "id", length = 4000)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "gxxmbh")
	public String getGXXMBH() {
		return super.getGXXMBH();
	}

	@Override
	@Column(name = "fl1")
	public String getFL1() {
		return super.getFL1();
	}

	@Override
	@Column(name = "fl2")
	public String getFL2() {
		return super.getFL2();
	}

	@Override
	@Column(name = "wjmc")
	public String getWJMC() {
		return super.getWJMC();
	}

	@Override
	@Column(name = "sxh")
	public Integer getSXH() {
		return super.getSXH();
	}

	@Override
	@Column(name = "hzm")
	public String getHZM() {
		return super.getHZM();
	}
}
