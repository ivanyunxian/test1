package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-9-24 
//* ----------------------------------------
//* Public Entity bdcs_jjsyfsqr 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_JJSYFSQR;

@Entity
@Table(name = "bdcs_jjsyfsqr", schema = "bdck")
public class BDCS_JJSYFSQR extends GenerateBDCS_JJSYFSQR {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "sqrxm")
	public String getSQRXM() {
		return super.getSQRXM();
	}

	@Override
	@Column(name = "sqrzjlx")
	public String getSQRZJLX() {
		return super.getSQRZJLX();
	}

	@Override
	@Column(name = "sqrzjh")
	public String getSQRZJH() {
		return super.getSQRZJH();
	}

	@Override
	@Column(name = "bz1")
	public String getBZ1() {
		return super.getBZ1();
	}

	@Override
	@Column(name = "bz2")
	public String getBZ2() {
		return super.getBZ2();
	}

	@Override
	@Column(name = "bz3")
	public String getBZ3() {
		return super.getBZ3();
	}

	@Override
	@Column(name = "bz4")
	public String getBZ4() {
		return super.getBZ4();
	}

	@Override
	@Column(name = "createtime")
	public Date getCREATETIME() {
		return super.getCREATETIME();
	}

	@Override
	@Column(name = "modifytime")
	public Date getMODIFYTIME() {
		return super.getMODIFYTIME();
	}
}
