package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-12-26 
//* ----------------------------------------
//* Public Entity bdcs_xmxx 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_XMXX;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bdcs_xmxx", schema = "bdck")
public class BDCS_XMXX extends GenerateBDCS_XMXX {

	@Override
	@Id
	@Column(name = "xmbh", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "project_id")
	public String getPROJECT_ID() {
		return super.getPROJECT_ID();
	}

	@Override
	@Column(name = "dcxmid")
	public String getDCXMID() {
		return super.getDCXMID();
	}

	@Override
	@Column(name = "xmmc")
	public String getXMMC() {
		return super.getXMMC();
	}

	@Override
	@Column(name = "djlx")
	public String getDJLX() {
		return super.getDJLX();
	}

	@Override
	@Column(name = "qllx")
	public String getQLLX() {
		return super.getQLLX();
	}

	@Override
	@Column(name = "sllx1")
	public String getSLLX1() {
		return super.getSLLX1();
	}

	@Override
	@Column(name = "sllx2")
	public String getSLLX2() {
		return super.getSLLX2();
	}

	@Override
	@Column(name = "slry")
	public String getSLRY() {
		return super.getSLRY();
	}

	@Override
	@Column(name = "slryid")
	public String getSLRYID() {
		return super.getSLRYID();
	}

	@Override
	@Column(name = "slsj")
	public Date getSLSJ() {
		return super.getSLSJ();
	}

	@Override
	@Column(name = "xmqx")
	public String getXMQX() {
		return super.getXMQX();
	}

	@Override
	@Column(name = "sfdb")
	public String getSFDB() {
		return super.getSFDB();
	}

	@Override
	@Column(name = "yxbz")
	public String getYXBZ() {
		return super.getYXBZ();
	}

	@Override
	@Column(name = "createtime")
	public Date getCREATETIME() {
		return super.getCREATETIME();
	}

	@Override
	@Column(name = "modifytime")
	public Date getMODIFYTIME() {
		return super.getMODIFYTIME();
	}

	@Override
	@Column(name = "sfhbzs")
	public String getSFHBZS() {
		return super.getSFHBZS();
	}

	@Override
	@Column(name = "ywlsh")
	public String getYWLSH() {
		return super.getYWLSH();
	}

	@Override
	@Column(name = "sfbj")
	public String getSFBJ() {
		return super.getSFBJ();
	}
	
	@Override
	@Column(name = "sfsb")
	public String getSFSB() {
		return super.getSFSB();
	}

	@Override
	@Column(name = "dagh")
	public Integer getDAGH() {
		return super.getDAGH();
	}
	
	@Override
	@Column(name = "tscs")
	public Integer getTSCS() {
		return super.getTSCS();
	}
	
	@Override
	@Column(name = "ALTERDATA")
	public String getALTERDATA() {
		return super.getALTERDATA();
	}
	
	@Override
	@Column(name = "djsj")
	public Date getDJSJ() {
		return super.getDJSJ();
	}
	
	@Override
	@Column(name = "fcywh")
	public String getFCYWH() {
		return super.getFCYWH();
	}
	@Override
	@Column(name = "fcjyzt")
	public String getFCJYZT() {
		return super.getFCJYZT();
	}

	@Override
	@Column(name = "sffbgg")
	public String getSFFBGG() {
		return super.getSFFBGG();
	}
	
	@Override
	@Column(name = "ajh")
	public String getAJH() {
		return super.getAJH();
	}

	@Override
	@Column(name = "WLSH")
	public String getWLSH() {
		return super.getWLSH();
	}

	@Override
	@Column(name = "SFQR")
	public String getSFQR() {
		if (super.getSFQR() == null || super.getSFQR() == "") {
			return "0";
		}
		return super.getSFQR();
	}

	@Override
//	@Column(name = "GJJYWLSH")
	@Transient
	public String getGJJYWLSH() {
//		return super.getGJJYWLSH();
		return null;
	}
}
