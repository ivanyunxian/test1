package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2016/1/26 
//* ----------------------------------------
//* Public Entity bdcs_idcard_pic 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_IDCARD_PIC;

@Entity
@Table(name = "bdcs_idcard_pic", schema = "bdck")
public class BDCS_IDCARD_PIC extends GenerateBDCS_IDCARD_PIC {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "zjh")
	public String getZJH() {
		return super.getZJH();
	}

	@Override
	@Column(name = "pic1")
	public String getPIC1() {
		return super.getPIC1();
	}

	@Override
	@Column(name = "pic2")
	public String getPIC2() {
		return super.getPIC2();
	}
}
