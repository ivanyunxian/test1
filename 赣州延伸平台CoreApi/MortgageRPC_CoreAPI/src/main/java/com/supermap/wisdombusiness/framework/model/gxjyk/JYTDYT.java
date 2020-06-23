package com.supermap.wisdombusiness.framework.model.gxjyk;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/8 
//* ----------------------------------------
//* Public Entity tdyt 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.supermap.wisdombusiness.framework.model.gxjyk.genrt.GenerateTDYT;

@Entity
@Table(name = "tdyt", schema = "gxjyk")
public class JYTDYT extends GenerateTDYT {

	@Override
	@Id
	@Column(name = "bsm", length = 100)
	public String getId() {
		return super.getId();
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
	@Column(name = "bz")
	public String getBZ() {
		return super.getBZ();
	}

	@Override
	@Column(name = "relationid")
	public String getRELATIONID() {
		return super.getRELATIONID();
	}

	@Override
	@Column(name = "gxlx")
	public String getGXLX() {
		return super.getGXLX();
	}

	@Override
	@Column(name = "tdyt")
	public String getTDYT() {
		return super.getTDYT();
	}

	@Override
	@Column(name = "tdytmc")
	public String getTDYTMC() {
		return super.getTDYTMC();
	}

	@Override
	@Column(name = "sfzyt")
	public String getSFZYT() {
		return super.getSFZYT();
	}

	@Override
	@Column(name = "tddj")
	public String getTDDJ() {
		return super.getTDDJ();
	}

	@Override
	@Column(name = "tdjg")
	public Double getTDJG() {
		return super.getTDJG();
	}

	@Override
	@Column(name = "qlqsrq")
	public Date getQLQSRQ() {
		return super.getQLQSRQ();
	}

	@Override
	@Column(name = "qlzzrq")
	public Date getQLZZRQ() {
		return super.getQLZZRQ();
	}

	@Override
	@Column(name = "syqx")
	public Integer getSYQX() {
		return super.getSYQX();
	}

	@Override
	@Column(name = "crjbz")
	public Double getCRJBZ() {
		return super.getCRJBZ();
	}
}
