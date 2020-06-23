package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-24 
//* ----------------------------------------
//* Public Entity bdcs_fsql_gz 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_WSXX;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "bdcs_wsxx", schema = "bdck")
public class BDCS_WSXX extends GenerateBDCS_WSXX  implements Serializable {

	@Override
	@Id
	@Column(name = "id")
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "fsqlid")
	public String getFSQLID() {
		return super.getFSQLID();
	}

	@Override
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}


	@Override
	@Column(name = "wspzh")
	public String getWSPZH() {
		return super.getWSPZH();
	}

	@Override
	@Column(name = "wsje")
	public Double getWSJE() {
		return super.getWSJE();
	}

	@Override
	@Column(name = "wssj")
	public Date getWSSJ() {
		return super.getWSSJ();
	}

	@Override
	@Column(name = "sszl")
	public String getSSZL() {
		return super.getSSZL();
	}

	@Override
	@Column(name = "sszlmc")
	public String getSSZLMC() {
		return super.getSSZLMC();
	}

	@Override
	@Column(name = "fj")
	public String getFJ() {
		return super.getFJ();
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
	@Column(name = "modify_time")
	public Date getTIME() {
		return super.getTIME();
	}

	@Override
	@Column(name = "modify_userid")
	public String getUSERID() {
		return super.getUSERID();
	}

	@Override
	@Column(name = "modify_username")
	public String getUSERNAME() {
		return super.getUSERNAME();
	}

}
