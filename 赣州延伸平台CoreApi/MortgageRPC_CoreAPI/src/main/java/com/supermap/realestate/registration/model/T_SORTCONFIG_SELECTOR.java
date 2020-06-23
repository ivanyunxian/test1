package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015/11/25 
//* ----------------------------------------
//* Public Entity t_sortconfig_selector 
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

import com.supermap.realestate.registration.model.genrt.GenerateT_SORTCONFIG_SELECTOR;

@Entity
@Table(name = "t_sortconfig_selector", schema = "bdck")
public class T_SORTCONFIG_SELECTOR extends GenerateT_SORTCONFIG_SELECTOR {

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
	@Column(name = "entityname")
	public String getENTITYNAME() {
		return super.getENTITYNAME();
	}

	@Override
	@Column(name = "sorttype")
	public String getSORTTYPE() {
		return super.getSORTTYPE();
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
