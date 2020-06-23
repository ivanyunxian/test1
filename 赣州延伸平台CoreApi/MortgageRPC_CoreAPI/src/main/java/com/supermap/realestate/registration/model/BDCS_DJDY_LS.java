package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-12 
//* ----------------------------------------
//* Public Entity bdcs_djdy_ls 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_DJDY_LS;
import com.supermap.realestate.registration.model.interfaces.RegisterUnit;

@Entity
@Table(name = "bdcs_djdy_ls", schema = "bdck")
public class BDCS_DJDY_LS extends GenerateBDCS_DJDY_LS  implements RegisterUnit {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "djdyid")
	public String getDJDYID() {
		return super.getDJDYID();
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
	@Column(name = "bdcdylx")
	public String getBDCDYLX() {
		return super.getBDCDYLX();
	}

	@Override
	@Column(name = "bdcdyid")
	public String getBDCDYID() {
		return super.getBDCDYID();
	}

	@Override
	@Column(name = "dcxmid")
	public String getDCXMID() {
		return super.getDCXMID();
	}

	@Override
	@Column(name = "createtime")
	public Date getCreateTime() {
		return super.getCreateTime();
	}

	@Override
	@Column(name = "modifytime")
	public Date getModifyTime() {
		return super.getModifyTime();
	}

	@Override
	@Column(name = "ly")
	public String getLY() {
		return super.getLY();
	}
	
	@Override
	@Column(name = "groupid")
	public Integer getGROUPID() {
		return super.getGROUPID();
	}
}
