package com.supermap.luzhouothers.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-10-29 
//* ----------------------------------------
//* Public Entity bdc_v_compact 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.luzhouothers.model.genrt.GenerateV_COMPACT;

@Entity
@Table(name = "v_compact", schema = "bdck")
public class V_COMPACT extends GenerateV_COMPACT {

	@Override
	@Id
	@Column(name = "id", length = 4000)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "category")
	public Integer getCATEGORY() {
		return super.getCATEGORY();
	}

	@Override
	@Column(name = "realtypeid")
	public Integer getREALTYPEID() {
		return super.getREALTYPEID();
	}

	@Override
	@Column(name = "flowid")
	public Integer getFLOWID() {
		return super.getFLOWID();
	}

	@Override
	@Column(name = "bastatus")
	public Integer getBASTATUS() {
		return super.getBASTATUS();
	}

	@Override
	@Column(name = "mastatus")
	public Integer getMASTATUS() {
		return super.getMASTATUS();
	}

	@Override
	@Column(name = "bitspno")
	public Integer getBITSPNO() {
		return super.getBITSPNO();
	}

	@Override
	@Column(name = "createdatetime")
	public Date getCREATEDATETIME() {
		return super.getCREATEDATETIME();
	}

	@Override
	@Column(name = "applytime")
	public Date getAPPLYTIME() {
		return super.getAPPLYTIME();
	}

	@Override
	@Column(name = "commitdatetime")
	public Date getCOMMITDATETIME() {
		return super.getCOMMITDATETIME();
	}

	@Override
	@Column(name = "contractno")
	public String getCONTRACTNO() {
		return super.getCONTRACTNO();
	}

	@Override
	@Column(name = "contracttype")
	public Integer getCONTRACTTYPE() {
		return super.getCONTRACTTYPE();
	}

	@Override
	@Column(name = "contractdatetime")
	public Date getCONTRACTDATETIME() {
		return super.getCONTRACTDATETIME();
	}

	@Override
	@Column(name = "currency")
	public String getCURRENCY() {
		return super.getCURRENCY();
	}

	@Override
	@Column(name = "moneyratio")
	public Double getMONEYRATIO() {
		return super.getMONEYRATIO();
	}

	@Override
	@Column(name = "calpricetype")
	public String getCALPRICETYPE() {
		return super.getCALPRICETYPE();
	}

	@Override
	@Column(name = "price")
	public Double getPRICE() {
		return super.getPRICE();
	}

	@Override
	@Column(name = "amount")
	public Double getAMOUNT() {
		return super.getAMOUNT();
	}

	@Override
	@Column(name = "paytype")
	public String getPAYTYPE() {
		return super.getPAYTYPE();
	}

	@Override
	@Column(name = "firstpayratio")
	public Double getFIRSTPAYRATIO() {
		return super.getFIRSTPAYRATIO();
	}

	@Override
	@Column(name = "loanbank")
	public String getLOANBANK() {
		return super.getLOANBANK();
	}

	@Override
	@Column(name = "deliverdate")
	public Date getDELIVERDATE() {
		return super.getDELIVERDATE();
	}

	@Override
	@Column(name = "password")
	public String getPASSWORD() {
		return super.getPASSWORD();
	}

	@Override
	@Column(name = "managecompany")
	public String getMANAGECOMPANY() {
		return super.getMANAGECOMPANY();
	}

	@Override
	@Column(name = "managecompanycno")
	public String getMANAGECOMPANYCNO() {
		return super.getMANAGECOMPANYCNO();
	}

	@Override
	@Column(name = "morttype")
	public Integer getMORTTYPE() {
		return super.getMORTTYPE();
	}

	@Override
	@Column(name = "landusetype")
	public String getLANDUSETYPE() {
		return super.getLANDUSETYPE();
	}

	@Override
	@Column(name = "landuseno")
	public String getLANDUSENO() {
		return super.getLANDUSENO();
	}

	@Override
	@Column(name = "landdistrict")
	public String getLANDDISTRICT() {
		return super.getLANDDISTRICT();
	}

	@Override
	@Column(name = "landstreet")
	public String getLANDSTREET() {
		return super.getLANDSTREET();
	}

	@Override
	@Column(name = "landdoorno")
	public String getLANDDOORNO() {
		return super.getLANDDOORNO();
	}

	@Override
	@Column(name = "landusage")
	public String getLANDUSAGE() {
		return super.getLANDUSAGE();
	}

	@Override
	@Column(name = "landarea")
	public Double getLANDAREA() {
		return super.getLANDAREA();
	}

	@Override
	@Column(name = "landusestartdate")
	public Date getLANDUSESTARTDATE() {
		return super.getLANDUSESTARTDATE();
	}

	@Override
	@Column(name = "landuseenddate")
	public Date getLANDUSEENDDATE() {
		return super.getLANDUSEENDDATE();
	}

	@Override
	@Column(name = "planpermitno")
	public String getPLANPERMITNO() {
		return super.getPLANPERMITNO();
	}

	@Override
	@Column(name = "operationpermitno")
	public String getOPERATIONPERMITNO() {
		return super.getOPERATIONPERMITNO();
	}

	@Override
	@Column(name = "buildprocess")
	public String getBUILDPROCESS() {
		return super.getBUILDPROCESS();
	}

	@Override
	@Column(name = "projectstartdate")
	public Date getPROJECTSTARTDATE() {
		return super.getPROJECTSTARTDATE();
	}

	@Override
	@Column(name = "projectenddate")
	public Date getPROJECTENDDATE() {
		return super.getPROJECTENDDATE();
	}

	@Override
	@Column(name = "allprojectname")
	public String getALLPROJECTNAME() {
		return super.getALLPROJECTNAME();
	}

	@Override
	@Column(name = "anlocation")
	public String getANLOCATION() {
		return super.getANLOCATION();
	}

	@Override
	@Column(name = "oldrightpeoplename")
	public String getOLDRIGHTPEOPLENAME() {
		return super.getOLDRIGHTPEOPLENAME();
	}

	@Override
	@Column(name = "newrightpeoplename")
	public String getNEWRIGHTPEOPLENAME() {
		return super.getNEWRIGHTPEOPLENAME();
	}
}
