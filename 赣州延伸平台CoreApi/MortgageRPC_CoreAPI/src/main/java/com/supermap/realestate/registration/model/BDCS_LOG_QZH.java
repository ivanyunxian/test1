package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2016/5/10 
//* ----------------------------------------
//* Public Entity bdcs_log_qzh 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_LOG_QZH;

@Entity
@Table(name = "bdcs_log_qzh", schema = "bdck")
public class BDCS_LOG_QZH extends GenerateBDCS_LOG_QZH {

	@Override
	@Id
	@Column(name = "id", length = 4000)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}

	@Override
	@Column(name = "startxh")
	public Integer getSTARTXH() {
		return super.getSTARTXH();
	}

	@Override
	@Column(name = "startqzh")
	public String getSTARTQZH() {
		return super.getSTARTQZH();
	}

	@Override
	@Column(name = "gs")
	public Integer getGS() {
		return super.getGS();
	}

	@Override
	@Column(name = "qzlx")
	public String getQZLX() {
		return super.getQZLX();
	}
}
