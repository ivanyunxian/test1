package com.supermap.wisdombusiness.framework.model.gxjyk;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/8 
//* ----------------------------------------
//* Public Entity cfdj 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.supermap.wisdombusiness.framework.model.gxjyk.genrt.GenerateCFDJ;

@Entity
@Table(name = "cfdj", schema = "gxjyk")
public class JYCFDJ extends GenerateCFDJ {

//	@Override
//	@Id
//	@Column(name = "bsm", length = 100)
//	public String getId() {
//		return super.getId();
//	}

	@Override
	@Column(name = "bdcdyh")
	public String getBDCDYH() {
		return super.getBDCDYH();
	}

	@Override
	@Column(name = "ywh")
	public String getYWH() {
		return super.getYWH();
	}

	@Override
	@Column(name = "qlid")
	public String getQLID() {
		return super.getQLID();
	}

	@Override
	@Column(name = "cflx")
	public String getCFLX() {
		return super.getCFLX();
	}

	@Override
	@Column(name = "cfjg")
	public String getCFJG() {
		return super.getCFJG();
	}

	@Override
	@Column(name = "cfwh")
	public String getCFWH() {
		return super.getCFWH();
	}

	@Lob
	@Override
	@Basic(fetch=FetchType.LAZY) 
	@Column(name = "cfwj",columnDefinition="BLOB", nullable=true)
	public byte[] getCFWJ() {
		return super.getCFWJ();
	}
	

	@Override
	@Column(name = "cffw")
	public String getCFFW() {
		return super.getCFFW();
	}

	@Override
	@Column(name = "jfjg")
	public String getJFJG() {
		return super.getJFJG();
	}

	@Override
	@Column(name = "jfwh")
	public String getJFWH() {
		return super.getJFWH();
	}

	@Lob
	@Override
	@Basic(fetch=FetchType.LAZY) 
	@Column(name = "jfwj",columnDefinition="BLOB", nullable=true)
	public byte[] getJFWJ() {
		return super.getJFWJ();
	}
	
	@Override
	@Column(name = "qlqssj")
	public Date getQLQSSJ() {
		return super.getQLQSSJ();
	}

	@Override
	@Column(name = "qljssj")
	public Date getQLJSSJ() {
		return super.getQLJSSJ();
	}

	@Override
	@Column(name = "qxdm")
	public String getQXDM() {
		return super.getQXDM();
	}

	@Override
	@Column(name = "djjg")
	public String getDJJG() {
		return super.getDJJG();
	}

	@Override
	@Column(name = "dbr")
	public String getDBR() {
		return super.getDBR();
	}

	@Override
	@Column(name = "djsj")
	public Date getDJSJ() {
		return super.getDJSJ();
	}

	@Override
	@Column(name = "jfywh")
	public String getJFYWH() {
		return super.getJFYWH();
	}

	@Override
	@Column(name = "jfdbr")
	public String getJFDBR() {
		return super.getJFDBR();
	}

	@Override
	@Column(name = "jfdjsj")
	public String getJFDJSJ() {
		return super.getJFDJSJ();
	}

	@Override
	@Column(name = "fj")
	public String getFJ() {
		return super.getFJ();
	}

	@Override
	@Column(name = "qszt")
	public String getQSZT() {
		return super.getQSZT();
	}

	@Override
	@Column(name = "bdcdyid")
	public String getBDCDYID() {
		return super.getBDCDYID();
	}

	@Override
	@Column(name = "gxxmbh")
	public String getGXXMBH() {
		return super.getGXXMBH();
	}

	@Override
	@Column(name = "lyqlid")
	public String getLYQLID() {
		return super.getLYQLID();
	}

	@Override
	@Column(name = "zsbh")
	public String getZSBH() {
		return super.getZSBH();
	}

	@Override
	@Column(name = "bz")
	public String getBZ() {
		return super.getBZ();
	}
	
	@Override
	@Id
	@Column(name = "bsm")
	public String getBSM() {
		return super.getBSM();
	}
}
