package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2016/12/8 
//* ----------------------------------------
//* Public Entity bdcs_zszfpic 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_ZSZFPIC;

@Entity
@Table(name = "bdcs_zszfpic", schema = "bdck")
public class BDCS_ZSZFPIC extends GenerateBDCS_ZSZFPIC {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "rkqzbid")
	public String getRKQZBID() {
		return super.getRKQZBID();
	}

	@Override
	@Column(name = "sxh")
	public Integer getSXH() {
		return super.getSXH();
	}

	@Override
	@Column(name = "zspic")
	public String getZSPIC() {
		return super.getZSPIC();
	}
}
