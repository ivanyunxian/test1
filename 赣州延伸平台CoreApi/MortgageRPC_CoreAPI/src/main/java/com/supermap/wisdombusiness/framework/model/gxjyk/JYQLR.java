package com.supermap.wisdombusiness.framework.model.gxjyk;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/11 
//* ----------------------------------------
//* Public Entity qlr 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.supermap.wisdombusiness.framework.model.gxjyk.genrt.GenerateQLR;

@Entity
@Table(name = "qlr", schema = "gxjyk")
public class JYQLR extends GenerateQLR {

	@Override
	@Id
	@Column(name = "bsm", length = 100)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "qlrid")
	public String getQLRID() {
		return super.getQLRID();
	}

	@Override
	@Column(name = "qlid")
	public String getQLID() {
		return super.getQLID();
	}

	@Override
	@Column(name = "sxh")
	public Integer getSXH() {
		return super.getSXH();
	}

	@Override
	@Column(name = "qlrmc")
	public String getQLRMC() {
		return super.getQLRMC();
	}

	@Override
	@Column(name = "bdcqzh")
	public String getBDCQZH() {
		return super.getBDCQZH();
	}

	@Override
	@Column(name = "zjzl")
	public String getZJZL() {
		return super.getZJZL();
	}

	@Override
	@Column(name = "zjh")
	public String getZJH() {
		return super.getZJH();
	}

	@Override
	@Column(name = "fzjg")
	public String getFZJG() {
		return super.getFZJG();
	}

	@Override
	@Column(name = "sshy")
	public String getSSHY() {
		return super.getSSHY();
	}

	@Override
	@Column(name = "gj")
	public String getGJ() {
		return super.getGJ();
	}

	@Override
	@Column(name = "hjszss")
	public String getHJSZSS() {
		return super.getHJSZSS();
	}

	@Override
	@Column(name = "xb")
	public String getXB() {
		return super.getXB();
	}

	@Override
	@Column(name = "dh")
	public String getDH() {
		return super.getDH();
	}

	@Override
	@Column(name = "dz")
	public String getDZ() {
		return super.getDZ();
	}

	@Override
	@Column(name = "yb")
	public String getYB() {
		return super.getYB();
	}

	@Override
	@Column(name = "gzdw")
	public String getGZDW() {
		return super.getGZDW();
	}

	@Override
	@Column(name = "dzyj")
	public String getDZYJ() {
		return super.getDZYJ();
	}

	@Override
	@Column(name = "qlrlx")
	public String getQLRLX() {
		return super.getQLRLX();
	}

	@Override
	@Column(name = "qlbl")
	public String getQLBL() {
		return super.getQLBL();
	}

	@Override
	@Column(name = "gyfs")
	public String getGYFS() {
		return super.getGYFS();
	}

	@Override
	@Column(name = "gyqk")
	public String getGYQK() {
		return super.getGYQK();
	}

	@Override
	@Column(name = "bz")
	public String getBZ() {
		return super.getBZ();
	}

	@Override
	@Column(name = "isczr")
	public String getISCZR() {
		return super.getISCZR();
	}

	@Override
	@Column(name = "ysdm")
	public String getYSDM() {
		return super.getYSDM();
	}

	@Override
	@Column(name = "bdcdyh")
	public String getBDCDYH() {
		return super.getBDCDYH();
	}

	@Override
	@Column(name = "qzysxlh")
	public String getQZYSXLH() {
		return super.getQZYSXLH();
	}

	@Override
	@Column(name = "gxlx")
	public String getGXLX() {
		return super.getGXLX();
	}

	@Override
	@Column(name = "gxxmbh")
	public String getGXXMBH() {
		return super.getGXXMBH();
	}

	@Override
	@Column(name = "sqrlb")
	public String getSQRLB() {
		return super.getSQRLB();
	}
}
