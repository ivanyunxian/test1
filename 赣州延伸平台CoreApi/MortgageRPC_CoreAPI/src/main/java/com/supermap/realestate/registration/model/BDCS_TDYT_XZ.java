package com.supermap.realestate.registration.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-10-26 
//* ----------------------------------------
//* Public Entity bdcs_tdyt_xz 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_TDYT_XZ;
import com.supermap.realestate.registration.model.interfaces.TDYT;
import com.supermap.realestate.registration.util.ConstHelper;

@Entity
@Table(name = "bdcs_tdyt_xz", schema = "bdck")
public class BDCS_TDYT_XZ extends GenerateBDCS_TDYT_XZ implements TDYT {

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
	@Column(name = "sfzyt")
	public String getSFZYT() {
		return super.getSFZYT();
	}

	@Override
	@Column(name = "tdyt")
	public String getTDYT() {
		return super.getTDYT();
	}

	@Override
	@Column(name = "tdytmc")
	public String getTDYTMC() {
		return super.getTDYTMC();
	}

	@Override
	@Column(name = "crjbz")
	public String getCRJBZ() {
		return super.getCRJBZ();
	}
	
	@Override
	@Column(name = "tddj")
	public String getTDDJ() {
		return super.getTDDJ();
	}

	@Override
	@Column(name = "tdjg")
	public Double getTDJG() {
		return super.getTDJG();
	}

	@Override
	@Temporal(TemporalType.DATE)
	@Column(name = "qsrq")
	public Date getQSRQ() {
		return super.getQSRQ();
	}

	@Override
	@Temporal(TemporalType.DATE)
	@Column(name = "zzrq")
	public Date getZZRQ() {
		return super.getZZRQ();
	}
	
	@Override
	@Column(name = "syqx")
	public Double getSYQX() {
		return super.getSYQX();
	}
	
	@Override
	@Column(name = "qlxz")
	public String getQLXZ() {
		return super.getQLXZ();
	}
	
	@Override
	@Column(name = "TDMJ")
	public Double getTDMJ() {
		return super.getTDMJ();
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

	private String tdytname;

	@Transient
	public String getTDYTName() {
		if (tdytname == null) {
			if (this.getTDYT() != null) {
				tdytname = ConstHelper.getNameByValue("TDYT", this.getTDYT());
			}
		}
		return tdytname;
	}

	private String tddjname;

	@Transient
	public String getTDDJName() {
		if (tddjname == null) {
			if (this.getTDDJ() != null) {
				tddjname = ConstHelper.getNameByValue("TDDJ", this.getTDDJ());
			}
		}
		return tddjname;
	}
	
	private String qlxzname;
	
	@Transient
	public String getQLXZName() {
		if (qlxzname == null) {
			if (this.getQLXZ() != null) {
				qlxzname = ConstHelper.getNameByValue("QLXZ", this.getQLXZ());
			}
		}
		return qlxzname;
	}
	
	@Override
	@Column(name = "ZYSZ")
	public String getZYSZ() {
		return super.getZYSZ();
	}
}
