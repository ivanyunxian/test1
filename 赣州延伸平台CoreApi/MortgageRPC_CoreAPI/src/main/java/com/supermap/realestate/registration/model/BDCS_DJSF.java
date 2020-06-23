package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-12-3 
//* ----------------------------------------
//* Public Entity bdcs_djsf 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_DJSF;
import com.supermap.realestate.registration.util.ConstHelper;

@Entity
@Table(name = "bdcs_djsf", schema = "bdck")
public class BDCS_DJSF extends GenerateBDCS_DJSF {

	@Override
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "ywh")
	public String getYWH() {
		return super.getYWH();
	}

	@Override
	@Column(name = "ysdm")
	public String getYSDM() {
		return super.getYSDM();
	}

	@Override
	@Column(name = "jfry")
	public String getJFRY() {
		return super.getJFRY();
	}

	@Override
	@Column(name = "jfrq")
	public Date getJFRQ() {
		return super.getJFRQ();
	}

	@Override
	@Column(name = "sfkmmc")
	public String getSFKMMC() {
		return super.getSFKMMC();
	}

    private String sfkmmcname;
	@Transient
	public String getSFKMMCName() {
		if (sfkmmcname == null) {
			if (this.getSFKMMC() != null) {
				sfkmmcname = ConstHelper.getNameByValue("SFKM", this.getSFKMMC());
			}
		}
		return sfkmmcname;
	}

	@Override
	@Column(name = "sfewsf")
	public String getSFEWSF() {
		return super.getSFEWSF();
	}

    private String sfewsfname;
	@Transient
	public String getSFEWSFName() {
		if (sfewsfname == null) {
			if (this.getSFEWSF() != null) {
				sfewsfname = ConstHelper.getNameByValue("SFZD", this.getSFEWSF());
			}
		}
		return sfewsfname;
	}

	@Override
	@Column(name = "sfjs")
	public Double getSFJS() {
		return super.getSFJS();
	}

	@Override
	@Column(name = "sflx")
	public String getSFLX() {
		return super.getSFLX();
	}

	@Override
	@Column(name = "ysje")
	public Double getYSJE() {
		return super.getYSJE();
	}

	@Override
	@Column(name = "zkhysje")
	public Double getZKHYSJE() {
		return super.getZKHYSJE();
	}

	@Override
	@Column(name = "sfry")
	public String getSFRY() {
		return super.getSFRY();
	}

	@Override
	@Column(name = "sfrq")
	public Date getSFRQ() {
		return super.getSFRQ();
	}

	@Override
	@Column(name = "fff")
	public String getFFF() {
		return super.getFFF();
	}

	@Override
	@Column(name = "sjffr")
	public String getSJFFR() {
		return super.getSJFFR();
	}

	@Override
	@Column(name = "ssje")
	public String getSSJE() {
		return super.getSSJE();
	}

	@Override
	@Column(name = "sfdw")
	public String getSFDW() {
		return super.getSFDW();
	}

	@Override
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
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
	@Column(name = "sfsx")
	public Double getSFSX() {
		return super.getSFSX();
	}

	@Override
	@Column(name = "sfbl")
	public Double getSFBL() {
		return super.getSFBL();
	}

	@Override
	@Column(name = "jfdw")
	public String getJFDW() {
		return super.getJFDW();
	}

	@Override
	@Column(name = "sfdyid")
	public String getSFDYID() {
		return super.getSFDYID();
	}

	@Override
	@Column(name = "jsgs")
	public String getJSGS() {
		return super.getJSGS();
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
	@Column(name = "bz")
	public String getBZ() {
		return super.getBZ();
	}

	@Override
	@Column(name = "xsgs")
	public String getXSGS() {
		return super.getXSGS();
	}

	@Override
	@Column(name = "ts")
	public Integer getTS() {
		return super.getTS();
	}
	
	@Override
	@Column(name = "sfbmmc")
	public String getSFBMMC() {
		return super.getSFBMMC();
	}
	
	@Override
	@Column(name = "sfjb")
	public String getSFJB() {
		return super.getSFJB();
	}
	
	private String sfxlmc;
	@Transient
	public String getSfxlmc() {
		return sfxlmc;
	}

	@Transient
	public void setSfxlmc(String sfxlmc) {
		this.sfxlmc = sfxlmc;
	}
	
	//收费环节收费项前添加权利人名称
	private String qlrmc;
	@Column(name = "qlrmc")
	public String getQLRMC() {
		return qlrmc;
	}

	@Transient
	public void setQLRMC(String qlrmc) {
		this.qlrmc = qlrmc;
	}
}
