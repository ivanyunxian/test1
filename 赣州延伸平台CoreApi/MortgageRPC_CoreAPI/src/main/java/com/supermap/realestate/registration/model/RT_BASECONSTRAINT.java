package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-12-22 
//* ----------------------------------------
//* Public Entity rt_baseconstraint 
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

import com.supermap.realestate.registration.constraint.RTCONSTRAINT;
import com.supermap.realestate.registration.model.genrt.GenerateRT_BASECONSTRAINT;

@Entity
@Table(name = "rt_baseconstraint", schema = "bdck")
public class RT_BASECONSTRAINT extends GenerateRT_BASECONSTRAINT  implements RTCONSTRAINT{

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "constrainttype")
	public String getCONSTRAINTTYPE() {
		return super.getCONSTRAINTTYPE();
	}

	@Override
	@Column(name = "baseworkflowid")
	public String getBASEWORKFLOWID() {
		return super.getBASEWORKFLOWID();
	}

	@Override
	@Column(name = "constraintid")
	public String getCONSTRAINTID() {
		return super.getCONSTRAINTID();
	}

	@Override
	@Column(name = "resulttip")
	public String getRESULTTIP() {
		return super.getRESULTTIP();
	}

	@Override
	@Column(name = "checklevel")
	public String getCHECKLEVEL() {
		return super.getCHECKLEVEL();
	}

	@Override
	@Temporal(TemporalType.DATE)
	@Column(name = "createtime")
	public Date getCREATETIME() {
		return super.getCREATETIME();
	}

	@Override
	@Temporal(TemporalType.DATE)
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
	@Column(name = "tipsql")
	public String getTIPSQL() {
		return super.getTIPSQL();
	}
}
