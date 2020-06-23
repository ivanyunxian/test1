package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2016/12/27 
//* ----------------------------------------
//* Public Entity bdcs_gzw_xz 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_GZW_XZ;
import com.supermap.realestate.registration.model.interfaces.Structure;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;

@Entity
@Table(name = "bdcs_gzw_xz", schema = "bdck")
public class BDCS_GZW_XZ extends GenerateBDCS_GZW_XZ implements Structure{

	@Override
	@Id
	@Column(name = "bdcdyid", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "ysdm")
	public String getYSDM() {
		return super.getYSDM();
	}

	@Override
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}

	@Override
	@Column(name = "bdcdyh")
	public String getBDCDYH() {
		return super.getBDCDYH();
	}

	@Override
	@Column(name = "zdbdcdyid")
	public String getZDBDCDYID() {
		return super.getZDBDCDYID();
	}

	@Override
	@Column(name = "zddm")
	public String getZDDM() {
		return super.getZDDM();
	}

	@Override
	@Column(name = "gzwmc")
	public String getGZWMC() {
		return super.getGZWMC();
	}

	@Override
	@Column(name = "zl")
	public String getZL() {
		return super.getZL();
	}

	@Override
	@Column(name = "tdhysyqr")
	public String getTDHYSYQR() {
		return super.getTDHYSYQR();
	}

	@Override
	@Column(name = "tdhysymj")
	public Double getTDHYSYMJ() {
		return super.getTDHYSYMJ();
	}

	@Override
	@Column(name = "gjzwlx")
	public String getGJZWLX() {
		return super.getGJZWLX();
	}

	@Override
	@Column(name = "gjzwghyt")
	public String getGJZWGHYT() {
		return super.getGJZWGHYT();
	}

	@Override
	@Column(name = "jgsj")
	public Date getJGSJ() {
		return super.getJGSJ();
	}

	@Override
	@Column(name = "mjdw")
	public String getMJDW() {
		return super.getMJDW();
	}

	@Override
	@Column(name = "mj")
	public double getMJ() {
		return super.getMJ();
	}

	@Override
	@Column(name = "zt")
	public String getZT() {
		return super.getZT();
	}

	@Override
	@Column(name = "dcxmid")
	public String getDCXMID() {
		return super.getDCXMID();
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
	@Column(name = "ywh")
	public String getYWH() {
		return super.getYWH();
	}

	@Override
	@Column(name = "qllx")
	public String getQLLX() {
		return super.getQLLX();
	}

	@Override
	@Column(name = "gjzwmj")
	public Double getGJZWMJ() {
		return super.getGJZWMJ();
	}

	@Override
	@Column(name = "qxdm")
	public String getQXDM() {
		return super.getQXDM();
	}

	@Override
	@Column(name = "gjzwpmt")
	public byte[] getGJZWPMT() {
		return super.getGJZWPMT();
	}

	@Override
	@Column(name = "qszt")
	public String getQSZT() {
		return super.getQSZT();
	}

	@Transient
	public Integer getBSM() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transient
	public void setBSM(Integer bSM) {
		// TODO Auto-generated method stub
		
	}

	@Transient
	public String getDJZT() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transient
	public BDCDYLX getBDCDYLX() {
		// TODO Auto-generated method stub
		return BDCDYLX.GZW;
	}

	@Transient
	public DJDYLY getLY() {
		// TODO Auto-generated method stub
		return DJDYLY.DC;
	}

	@Transient
	public void setDJZT(String djzt) {
		// TODO Auto-generated method stub
		
	}

	@Transient
	public String getRELATIONID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transient
	public void setMJ(double mJ) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setYT(String yt) {
		// TODO Auto-generated method stub
		
	}
}
