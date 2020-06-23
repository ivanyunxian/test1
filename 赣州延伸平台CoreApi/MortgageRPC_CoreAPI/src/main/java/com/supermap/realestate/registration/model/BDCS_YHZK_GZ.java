package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-25 
//* ----------------------------------------
//* Public Entity bdcs_yhzk_gz 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_YHZK_GZ;
import com.supermap.realestate.registration.model.interfaces.YHZK;
import com.supermap.realestate.registration.util.ConstHelper;

@Entity
@Table(name = "bdcs_yhzk_gz", schema = "bdck")
public class BDCS_YHZK_GZ extends GenerateBDCS_YHZK_GZ implements YHZK {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "bdcdyid")
	public String getBDCDYID() {
		return super.getBDCDYID();
	}

	@Override
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}

	@Override
	@Column(name = "zhdm")
	public String getZHDM() {
		return super.getZHDM();
	}

	@Override
	@Column(name = "yhfs")
	public String getYHFS() {
		return super.getYHFS();
	}

    private String yhfsname;
	@Transient
	public String getYHFSName() {
		if (yhfsname == null) {
			if (this.getYHFS() != null) {
				yhfsname = ConstHelper.getNameByValue("YHFS", this.getYHFS());
			}
		}
		return yhfsname;
	}

	@Override
	@Column(name = "yhmj")
	public Double getYHMJ() {
		return super.getYHMJ();
	}

	@Override
	@Column(name = "jtyt")
	public String getJTYT() {
		return super.getJTYT();
	}

	@Override
	@Column(name = "syjse")
	public Double getSYJSE() {
		return super.getSYJSE();
	}

	@Override
	@Column(name = "dcxmid")
	public String getDCXMID() {
		return super.getDCXMID();
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
}
