package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-8-31 
//* ----------------------------------------
//* Public Entity bdcs_sfdy 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_SFDY;
import com.supermap.realestate.registration.util.ConstHelper;

@Entity
@Table(name = "bdcs_sfdy", schema = "bdck")
public class BDCS_SFDY extends GenerateBDCS_SFDY {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "sfdlmc")
	public String getSFDLMC() {
		return super.getSFDLMC();
	}

	@Override
	@Column(name = "sfxlmc")
	public String getSFXLMC() {
		return super.getSFXLMC();
	}

	@Override
	@Column(name = "sfkmmc")
	public String getSFKMMC() {
		return super.getSFKMMC();
	}

	@Override
	@Column(name = "sflx")
	public String getSFLX() {
		return super.getSFLX();
	}

    private String sflxname;
	@Transient
	public String getSFLXName() {
		if (sflxname == null) {
			if (this.getSFLX() != null) {
				sflxname = ConstHelper.getNameByValue("SFLX", this.getSFLX());
			}
		}
		return sflxname;
	}

	@Override
	@Column(name = "sfjs")
	public Double getSFJS() {
		return super.getSFJS();
	}

	@Override
	@Column(name = "mjjs")
	public Double getMJJS() {
		return super.getMJJS();
	}

	@Override
	@Column(name = "mjzl")
	public Double getMJZL() {
		return super.getMJZL();
	}

	@Override
	@Column(name = "sfzl")
	public Double getSFZL() {
		return super.getSFZL();
	}

	@Override
	@Column(name = "zlfysx")
	public Double getZLFYSX() {
		return super.getZLFYSX();
	}

	@Override
	@Column(name = "sfbl")
	public Double getSFBL() {
		return super.getSFBL();
	}

	@Override
	@Column(name = "jsgs")
	public String getJSGS() {
		return super.getJSGS();
	}

	@Override
	@Column(name = "creat_time")
	public Date getCREAT_TIME() {
		return super.getCREAT_TIME();
	}

	@Override
	@Column(name = "sfdw")
	public String getSFDW() {
		return super.getSFDW();
	}

	@Override
	@Column(name = "caltype")
	public String getCALTYPE() {
		return super.getCALTYPE();
	}

	@Override
	@Column(name = "sqlexp")
	public String getSQLEXP() {
		return super.getSQLEXP();
	}
	
	@Override
	@Column(name = "cacsql")
	public String getCACSQL() {
		return super.getCACSQL();
	}

	@Override
	@Column(name = "bz")
	public String getBZ() {
		return super.getBZ();
	}
	
	@Override
	@Column(name = "symbol")
	public String getSYMBOL() {
		return super.getSYMBOL();
	}
	
	@Override
	@Column(name = "sfbmmc")
	public String getSFBMMC() {
		return super.getSFBMMC();
	}
	
	@Override
	@Column(name = "tjbz")
	public String getTJBZ() {
		return super.getTJBZ();
	}
	
}
