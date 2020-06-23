package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-11-7 
//* ----------------------------------------
//* Public Entity t_checkrule 
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

import com.supermap.realestate.registration.check.CheckRule;
import com.supermap.realestate.registration.model.genrt.GenerateT_CHECKRULE;

@Entity
@Table(name = "t_checkrule", schema = "bdck")
public class T_CHECKRULE extends GenerateT_CHECKRULE implements CheckRule {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "classname")
	public String getCLASSNAME() {
		return super.getCLASSNAME();
	}

	@Override
	@Column(name = "name")
	public String getNAME() {
		return super.getNAME();
	}

	@Override
	@Column(name = "description")
	public String getDESCRIPTION() {
		return super.getDESCRIPTION();
	}

	@Override
	@Column(name = "executetype")
	public String getEXECUTETYPE() {
		return super.getEXECUTETYPE();
	}

	@Override
	@Column(name = "executeclassname")
	public String getEXECUTECLASSNAME() {
		return super.getEXECUTECLASSNAME();
	}

	@Override
	@Column(name = "executesql")
	public String getEXECUTESQL() {
		return super.getEXECUTESQL();
	}

	@Override
	@Column(name = "sqlresultexp")
	public String getSQLRESULTEXP() {
		return super.getSQLRESULTEXP();
	}

	@Override
	@Column(name = "resulttip")
	public String getRESULTTIP() {
		return super.getRESULTTIP();
	}

	@Override
	@Column(name = "userdefine")
	public String getUSERDEFINE() {
		return super.getUSERDEFINE();
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
}
