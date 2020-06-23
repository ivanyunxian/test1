package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015/7/28 
//* ----------------------------------------
//* Public Entity bdcs_log 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.Date;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_LOG;
import com.supermap.realestate.registration.model.BDCS_LOG;
@Entity
@Table(name = "bdcs_log", schema = "BDC_SHARE")
public class BDCS_LOG extends GenerateBDCS_LOG {

	@Override
	@Id
	@Column(name = "log_id", length = 4000)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "project_id")
	public String getPROJECT_ID() {
		return super.getPROJECT_ID();
	}

	@Override
	@Column(name = "staff")
	public String getSTAFF() {
		return super.getSTAFF();
	}

	@Override
	@Column(name = "dept_code")
	public String getDEPT_CODE() {
		return super.getDEPT_CODE();
	}

	@Override
	@Column(name = "dept_name")
	public String getDEPT_NAME() {
		return super.getDEPT_NAME();
	}

	@Override
	@Column(name = "filename")
	public String getFILENAME() {
		return super.getFILENAME();
	}

	@Override
	@Column(name = "operate_time")
	public Date getOPERATE_TIME() {
		return super.getOPERATE_TIME();
	}

	@Override
	@Column(name = "issuccess")
	public String getISSUCCESS() {
		return super.getISSUCCESS();
	}

	@Override
	@Column(name = "feedbacktime")
	public Date getFEEDBACKTIME() {
		return super.getFEEDBACKTIME();
	}

	@Override
	@Column(name = "filepath")
	public String getFILEPATH() {
		return super.getFILEPATH();
	}

	@Override
	@Column(name = "content")
	public String getCONTENT() {
		return super.getCONTENT();
	}

	@Override
	@Column(name = "comments")
	public String getCOMMENTS() {
		return super.getCOMMENTS();
	}

	@Override
	@Column(name = "detail")
	public String getDETAIL() {
		return super.getDETAIL();
	}

	@Override
	@Column(name = "to_detpcode")
	public String getTO_DETPCODE() {
		return super.getTO_DETPCODE();
	}

	@Override
	@Column(name = "to_deptname")
	public String getTO_DEPTNAME() {
		return super.getTO_DEPTNAME();
	}
	
	@Override
	@Column(name = "xmlid")
	public String getXMLID() {
		return super.getXMLID();
	}
}
