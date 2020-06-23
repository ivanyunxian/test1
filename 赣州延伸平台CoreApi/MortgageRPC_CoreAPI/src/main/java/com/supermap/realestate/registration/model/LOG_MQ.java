package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-12-4 
//* ----------------------------------------
//* Public Entity log_mq 
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

import com.supermap.realestate.registration.model.genrt.GenerateLOG_MQ;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "log_mq", schema = "bdck")
public class LOG_MQ extends GenerateLOG_MQ {

	@Override
	@Id
	@Column(name = "id", length = 4000)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "bsm")
	public String getBSM() {
		return super.getBSM();
	}

	@Override
	@Column(name = "keyid")
	public String getKeyID() {
		return super.getKeyID();
	}

	@Override
	@Column(name = "logtime")
	public Date getLogTime() {
		return super.getLogTime();
	}

	@Override
	@Column(name = "mqmessage")
	public String getMQMessage() {
		return super.getMQMessage();
	}

	@Override
	@Column(name = "parentkeyid")
	public String getParentKeyID() {
		return super.getParentKeyID();
	}

	@Override
	@Column(name = "success")
	public String getSuccess() {
		return super.getSuccess();
	}

	@Override
	@Column(name = "status")
	public Integer getStatus() {
		return super.getStatus();
	}

	@Override
	@Column(name = "xmmc")
	public String getXMMC() {
		return super.getXMMC();
	}

	@Override
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}

	@Override
	@Column(name = "actinst_id")
	public String getActinst_id() {
		return super.getActinst_id();
	}

	@Override
	@Column(name = "mqfrom")
	public String getMQFrom() {
		return super.getMQFrom();
	}

	@Override
	@Column(name = "mqto")
	public String getMQTo() {
		return super.getMQTo();
	}
}
