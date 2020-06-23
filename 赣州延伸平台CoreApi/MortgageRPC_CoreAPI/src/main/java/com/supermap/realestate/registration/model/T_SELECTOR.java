package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015/11/12 
//* ----------------------------------------
//* Public Entity t_selector 
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

import com.supermap.realestate.registration.model.genrt.GenerateT_SELECTOR;

@Entity
@Table(name = "t_selector", schema = "bdck")
public class T_SELECTOR extends GenerateT_SELECTOR {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "name")
	public String getNAME() {
		return super.getNAME();
	}

	@Override
	@Column(name = "selectbdcdy")
	public String getSELECTBDCDY() {
		return super.getSELECTBDCDY();
	}

	@Override
	@Column(name = "selectql")
	public String getSELECTQL() {
		return super.getSELECTQL();
	}

	@Override
	@Column(name = "selectqllx")
	public String getSELECTQLLX() {
		return super.getSELECTQLLX();
	}

	@Override
	@Column(name = "bdcdylx")
	public String getBDCDYLX() {
		return super.getBDCDYLX();
	}

	@Override
	@Column(name = "ly")
	public String getLY() {
		return super.getLY();
	}

	@Override
	@Column(name = "condition")
	public String getCONDITION() {
		return super.getCONDITION();
	}

	@Override
	@Column(name = "singleselect")
	public String getSINGLESELECT() {
		return super.getSINGLESELECT();
	}

	@Override
	@Column(name = "idfieldname")
	public String getIDFIELDNAME() {
		return super.getIDFIELDNAME();
	}

	@Override
	@Column(name = "useconfigsql")
	public String getUSECONFIGSQL() {
		return super.getUSECONFIGSQL();
	}

	@Override
	@Column(name = "configsql")
	public String getCONFIGSQL() {
		return super.getCONFIGSQL();
	}

	@Override
	@Column(name = "defaultselectfirt")
	public String getDEFAULTSELECTFIRT() {
		return super.getDEFAULTSELECTFIRT();
	}

	@Override
	@Column(name = "showdetailalterselect")
	public String getSHOWDETAILALTERSELECT() {
		return super.getSHOWDETAILALTERSELECT();
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
