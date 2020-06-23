package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2016/11/5 
//* ----------------------------------------
//* Public Entity bdcs_reportinfo 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_REPORTINFO;

@Entity
@Table(name = "bdcs_reportinfo", schema = "bdck")
public class BDCS_REPORTINFO extends GenerateBDCS_REPORTINFO {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "reporttime")
	public Date getREPORTTIME() {
		return super.getREPORTTIME();
	}

	@Override
	@Column(name = "reportuser")
	public String getREPORTUSER() {
		return super.getREPORTUSER();
	}

	@Override
	@Column(name = "bizmsgid")
	public String getBIZMSGID() {
		return super.getBIZMSGID();
	}

	@Override
	@Column(name = "djdyid")
	public String getDJDYID() {
		return super.getDJDYID();
	}

	@Override
	@Column(name = "qlid")
	public String getQLID() {
		return super.getQLID();
	}

	@Override
	@Column(name = "proinstid")
	public String getPROINSTID() {
		return super.getPROINSTID();
	}

	@Override
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}

	@Override
	@Column(name = "rectype")
	public String getRECTYPE() {
		return super.getRECTYPE();
	}

	@Override
	@Column(name = "reportcontent")
	public String getREPORTCONTENT() {
		return super.getREPORTCONTENT();
	}

	@Override
	@Column(name = "reporttype")
	public String getREPORTTYPE() {
		return super.getREPORTTYPE();
	}

	@Override
	@Column(name = "successflag")
	public String getSUCCESSFLAG() {
		return super.getSUCCESSFLAG();
	}

	@Override
	@Column(name = "responsecode")
	public String getRESPONSECODE() {
		return super.getRESPONSECODE();
	}

	@Override
	@Column(name = "responseinfo")
	public String getRESPONSEINFO() {
		return super.getRESPONSEINFO();
	}
	
	@Override
	@Column(name = "localcheck")
	public String getLOCALCHECK() {
		return super.getLOCALCHECK();
	}
	
	@Override
	@Column(name = "localcheckinfo")
	public String getLOCALCHECKINFO() {
		return super.getLOCALCHECKINFO();
	}
	
	@Override
	@Column(name = "certid")
	public String getCERTID() {
		return super.getCERTID();
	}
	
	@Override
	@Column(name = "qrcode")
	public String getQRCODE() {
		return super.getQRCODE();
	}
	
	@Override
	@Column(name = "rescount")
	public Integer getRESCOUNT() {
		return super.getRESCOUNT();
	}

	@Override
	@Column(name = "respensecontent")
	public String getRESPENSECONTENT() {
		return super.getRESPENSECONTENT();
	}
	
	@Override
	@Column(name = "lastrestime")
	public Date getLASTRESTIME() {
		return super.getLASTRESTIME();
	}

	@Override
	@Column(name = "yxbz")
	public String getYXBZ() {
		return super.getYXBZ();
	}
}
