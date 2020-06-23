package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2016/12/14 
//* ----------------------------------------
//* Public Entity bdcs_xzcf 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.supermap.realestate.registration.util.ConstHelper;
import javax.persistence.Transient;
import java.util.Date;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_XZCF;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "xzcf", schema = "bdck")
public class BDCS_XZCF extends GenerateBDCS_XZCF {

	@Override
	@Id
	@Column(name = "id", length = 257)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "ytdzid")
	public String getYTDZID() {
		return super.getYTDZID();
	}

	@Override
	@Column(name = "zszt")
	public Double getZSZT() {
		return super.getZSZT();
	}

	@Override
	@Column(name = "djjg")
	public String getDJJG() {
		return super.getDJJG();
	}

	@Override
	@Column(name = "bz")
	public String getBZ() {
		return super.getBZ();
	}

	@Override
	@Column(name = "zdid")
	public String getZDID() {
		return super.getZDID();
	}

	@Override
	@Column(name = "qlrid")
	public String getQLRID() {
		return super.getQLRID();
	}

	@Override
	@Column(name = "cfcs")
	public Double getCFCS() {
		return super.getCFCS();
	}

	@Override
	@Column(name = "spfgrmxid")
	public String getSPFGRMXID() {
		return super.getSPFGRMXID();
	}

	@Override
	@Column(name = "sqsjid")
	public String getSQSJID() {
		return super.getSQSJID();
	}

	@Override
	@Column(name = "cfid")
	public String getCFID() {
		return super.getCFID();
	}

	@Override
	@Column(name = "bgspbid")
	public String getBGSPBID() {
		return super.getBGSPBID();
	}

	@Override
	@Column(name = "t_zid")
	public String getT_ZID() {
		return super.getT_ZID();
	}

	@Override
	@Column(name = "tdzh")
	public String getTDZH() {
		return super.getTDZH();
	}

	@Override
	@Column(name = "bs")
	public String getBS() {
		return super.getBS();
	}

	@Override
	@Column(name = "dabh")
	public String getDABH() {
		return super.getDABH();
	}

	@Override
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}

	@Override
	@Column(name = "djh")
	public String getDJH() {
		return super.getDJH();
	}

	@Override
	@Column(name = "cfwj")
	public String getCFWJ() {
		return super.getCFWJ();
	}

	@Override
	@Column(name = "cfwh")
	public String getCFWH() {
		return super.getCFWH();
	}

	@Override
	@Column(name = "cffy")
	public String getCFFY() {
		return super.getCFFY();
	}

	@Override
	@Column(name = "cfrq")
	public Date getCFRQ() {
		return super.getCFRQ();
	}

	@Override
	@Column(name = "fj")
	public String getFJ() {
		return super.getFJ();
	}

	@Override
	@Column(name = "cfqssj")
	public Date getCFQSSJ() {
		return super.getCFQSSJ();
	}

	@Override
	@Column(name = "cfjzrq")
	public Date getCFJZRQ() {
		return super.getCFJZRQ();
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
	@Column(name = "qlr")
	public String getQLR() {
		return super.getQLR();
	}

	@Override
	@Column(name = "zl")
	public String getZL() {
		return super.getZL();
	}

	@Override
	@Column(name = "zdmj")
	public Double getZDMJ() {
		return super.getZDMJ();
	}

	@Override
	@Column(name = "tdzid")
	public String getTDZID() {
		return super.getTDZID();
	}

	@Override
	@Column(name = "yxbz")
	public Double getYXBZ() {
		return super.getYXBZ();
	}

	@Override
	@Column(name = "zdbdcdyid")
	public String getZDBDCDYID() {
		return super.getZDBDCDYID();
	}
}
