package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015/11/12 
//* ----------------------------------------
//* Public Entity t_gridconfig_selector 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.supermap.realestate.registration.model.genrt.GenerateT_GRIDCONFIG_SELECTOR;

@Entity
@Table(name = "t_gridconfig_selector", schema = "bdck")
public class T_GRIDCONFIG_SELECTOR extends GenerateT_GRIDCONFIG_SELECTOR {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "fieldname")
	public String getFIELDNAME() {
		return super.getFIELDNAME();
	}

	@Override
	@Column(name = "columntext")
	public String getCOLUMNTEXT() {
		return super.getCOLUMNTEXT();
	}

	@Override
	@Column(name = "width")
	public String getWIDTH() {
		return super.getWIDTH();
	}

	@Override
	@Column(name = "selectorid")
	public String getSELECTORID() {
		return super.getSELECTORID();
	}

	@Temporal(TemporalType.DATE)
	@Override
	@Column(name = "createtime")
	public Date getCREATETIME() {
		return super.getCREATETIME();
	}

	@Temporal(TemporalType.DATE)
	@Override
	@Column(name = "modifytime")
	public Date getMODIFYTIME() {
		return super.getMODIFYTIME();
	}

	@Override
	@Column(name = "creator")
	public String getCREATOR() {
		return super.getCREATOR();
	}

	@Override
	@Column(name = "lastmodifier")
	public String getLASTMODIFIER() {
		return super.getLASTMODIFIER();
	}
	
	@Override
	@Column(name = "sxh")
	public Integer getSXH() {
		return super.getSXH();
	}
}
