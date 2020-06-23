package com.supermap.realestate.registration.model;

import java.util.Date;

///*****************************************
//* AutoGenerate by CodeTools 2015-9-10 
//* ----------------------------------------
//* Public Entity bdcs_gzxs 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_GZXS;

@Entity
@Table(name = "bdcs_gzxs", schema = "bdck")
public class BDCS_GZXS extends GenerateBDCS_GZXS {

	@Override
	@Id
	@Column(name = "gzxsid", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}

	@Override
	@Column(name = "qs")
	public Double getQS() {
		return super.getQS();
	}

	@Override
	@Column(name = "yys")
	public Double getYYS() {
		return super.getYYS();
	}

	@Override
	@Column(name = "grsds")
	public Double getGRSDS() {
		return super.getGRSDS();
	}

	@Override
	@Column(name = "yhs")
	public Double getYHS() {
		return super.getYHS();
	}

	@Override
	@Column(name = "tdzzs")
	public Double getTDZZS() {
		return super.getTDZZS();
	}

	@Override
	@Column(name = "qt1")
	public Double getQT1() {
		return super.getQT1();
	}

	@Override
	@Column(name = "qt2")
	public Double getQT2() {
		return super.getQT2();
	}

	@Override
	@Column(name = "qt3")
	public Double getQT3() {
		return super.getQT3();
	}

	@Override
	@Column(name = "qt4")
	public Double getQT4() {
		return super.getQT4();
	}

	@Override
	@Column(name = "qt5")
	public Double getQT5() {
		return super.getQT5();
	}
	
	@Override
	@Column(name = "fwbm")
	public String getFWBM() {
		return super.getFWBM();
	}
	
	@Override
	@Column(name = "szmc")
	public String getSZMC() {
		return super.getSZMC();
	}
	
	@Override
	@Column(name = "se")
	public Double getSE() {
		return super.getSE();
	}
	
	@Override
	@Column(name = "smmc")
	public String  getSMMC() {
		return super.getSMMC();
	}
	
	
	@Override
	@Column(name = "htbh")
	public String getHTBH() {
		return super.getHTBH();
	}
	
	@Override
	@Column(name = "nsrmc")
	public String getNSRMC() {
		return super.getNSRMC();
	}
	
	
	@Override
	@Column(name = "sl")
	public Double getSL() {
		return super.getSL();
	}
	
	@Override
	@Column(name = "zjh")
	public String getZJH() {
		return super.getZJH();
	}
	
	@Override
	@Column(name = "jsrq")
	public Date getJSRQ() {
		return super.getJSRQ();
	}
	
	@Override
	@Column(name = "jsyj")
	public String getJSYJ() {
		return super.getJSYJ();
	}
	
	@Override
	@Column(name = "sfws")
	public String getSFWS() {
		return super.getSFWS();
	}
}
