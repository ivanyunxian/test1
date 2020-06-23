package com.supermap.realestate.registration.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_MSGLOG;

@Entity
@Table(name = "bdcs_msglog", schema = "bdck")
public class BDCS_MSGLOG   extends GenerateBDCS_MSGLOG {
	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}
	
	@Override
	@Column(name = "ywlsh")
	public String getYWLSH() {
		return super.getYWLSH();
	}

	@Override
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}
	@Override
	@Column(name = "project_id")
	public String getPROJECT_ID() {
		return super.getPROJECT_ID();
	}
	@Override
	@Column(name = "xmmc")
	public String getXMMC() {
		return super.getXMMC();
	}
	@Override
	@Column(name = "qllx")
	public String getQLLX() {
		return super.getQLLX();
	}
	@Override
	@Column(name = "djlx")
	public String getDJLX() {
		return super.getDJLX();
	}
	@Override
	@Column(name = "dxfssj")
	public Date getDXFSSJ() {
		return super.getDXFSSJ();
	}
	@Override
	@Column(name = "ywbjsj")
	public Date getYWBJSJ() {
		return super.getYWBJSJ();
	}
	@Override
	@Column(name = "jsdxrmc")
	public String getJSDXRMC() {
		return super.getJSDXRMC();
	}
	@Override
	@Column(name = "flag")
	public String getFLAG() {
		return super.getFLAG();
	}
	@Override
	@Column(name = "dxnr")
	public String getDXNR() {
		return super.getDXNR();
	}
	@Override
	@Column(name = "message")
	public String getMESSAGE() {
		return super.getMESSAGE();
	}
}
