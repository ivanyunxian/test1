package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-11 
//* ----------------------------------------
//* Public Entity bdcs_const 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_CONST;

@Entity
@Table(name = "bdcs_const", schema = "bdck")
public class BDCS_CONST extends GenerateBDCS_CONST {

	@Override
	@Id
	@Column(name = "mbbsm", length = 38)
	public Integer getId() {
		return super.getId();
	}

	@Override
	@Column(name = "constslsid")
	public Integer getCONSTSLSID() {
		return super.getCONSTSLSID();
	}

	@Override
	@Column(name = "constvalue")
	public String getCONSTVALUE() {
		return super.getCONSTVALUE();
	}

	@Override
	@Column(name = "consttrans")
	public String getCONSTTRANS() {
		return super.getCONSTTRANS();
	}
	
	@Override
	@Column(name = "gjvalue")
	public String getGJVALUE() {
		return super.getGJVALUE();
	}

	@Override
	@Column(name = "gjconsttrans")
	public String getGJCONSTTRANS() {
		return super.getGJCONSTTRANS();
	}
	
	@Override
	@Column(name = "sfsy")
	public String getSFSY() {
		return super.getSFSY();
	}

	@Override
	@Column(name = "parentnode")
	public Integer getPARENTNODE() {
		return super.getPARENTNODE();
	}

	@Override
	@Column(name = "constorder")
	public Integer getCONSTORDER() {
		return super.getCONSTORDER();
	}

	@Override
	@Column(name = "bz")
	public String getBZ() {
		return super.getBZ();
	}
	
	@Override
	@Column(name = "reportvalue")
	public String getREPORTVALUE() {
		return super.getREPORTVALUE();
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
}
