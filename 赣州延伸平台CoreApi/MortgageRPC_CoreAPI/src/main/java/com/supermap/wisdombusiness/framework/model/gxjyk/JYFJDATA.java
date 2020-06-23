package com.supermap.wisdombusiness.framework.model.gxjyk;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/8 
//* ----------------------------------------
//* Public Entity fjdata 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.supermap.wisdombusiness.framework.model.gxjyk.genrt.GenerateFJDATA;

@Entity
@Table(name = "fjdata", schema = "gxjyk")
public class JYFJDATA extends GenerateFJDATA {

	@Override
	@Id
	@Column(name = "id", length = 4000)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "fjid")
	public String getFJID() {
		return super.getFJID();
	}

	@Override
	@Column(name = "nr")
	public Byte[] getNR() {
		return super.getNR();
	}
}
