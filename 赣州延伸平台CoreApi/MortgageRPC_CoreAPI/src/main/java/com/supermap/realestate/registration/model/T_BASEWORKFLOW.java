package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015/11/12 
//* ----------------------------------------
//* Public Entity t_baseworkflow 
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

import com.supermap.realestate.registration.model.genrt.GenerateT_BASEWORKFLOW;

@Entity
@Table(name = "t_baseworkflow", schema = "bdck")
public class T_BASEWORKFLOW extends GenerateT_BASEWORKFLOW {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "class")
	public String getCLASS() {
		return super.getCLASS();
	}

	@Override
	@Column(name = "name")
	public String getNAME() {
		return super.getNAME();
	}

	@Override
	@Column(name = "selectorid")
	public String getSELECTORID() {
		return super.getSELECTORID();
	}

	@Override
	@Column(name = "handlerid")
	public String getHANDLERID() {
		return super.getHANDLERID();
	}

	@Override
	@Column(name = "djlx")
	public String getDJLX() {
		return super.getDJLX();
	}

	@Override
	@Column(name = "qllx")
	public String getQLLX() {
		return super.getQLLX();
	}

	@Override
	@Column(name = "unittype")
	public String getUNITTYPE() {
		return super.getUNITTYPE();
	}

	@Override
	@Column(name = "description")
	public String getDESCRIPTION() {
		return super.getDESCRIPTION();
	}

	@Override
	@Column(name = "unitpagejsp")
	public String getUNITPAGEJSP() {
		return super.getUNITPAGEJSP();
	}

	@Override
	@Column(name = "rightspagejsp")
	public String getRIGHTSPAGEJSP() {
		return super.getRIGHTSPAGEJSP();
	}

	@Override
	@Column(name = "bookpagejsp")
	public String getBOOKPAGEJSP() {
		return super.getBOOKPAGEJSP();
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
	@Column(name = "buildingselecttype")
	public String getBUILDINGSELECTTYPE() {
		return super.getBUILDINGSELECTTYPE();
	}
}
