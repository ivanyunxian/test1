package com.supermap.luzhouothers.model;

///*****************************************
//* AutoGenerate by CodeTools 2015-10-29 
//* ----------------------------------------
//* Public Entity bdc_v_compact_h 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.supermap.luzhouothers.model.genrt.GenerateV_COMPACT_H;

@Entity
@Table(name = "v_compact_h", schema = "bdck")
public class V_COMPACT_H extends GenerateV_COMPACT_H {

	@Override
	@Id
	@Column(name = "id", length = 4000)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "rowno")
	public String getROWNO() {
		return super.getROWNO();
	}

	@Override
	@Column(name = "relationid")
	public Integer getRELATIONID() {
		return super.getRELATIONID();
	}

	@Override
	@Column(name = "zl")
	public String getZL() {
		return super.getZL();
	}

	@Override
	@Column(name = "part")
	public String getPART() {
		return super.getPART();
	}

	@Override
	@Column(name = "buildarea")
	public Double getBUILDAREA() {
		return super.getBUILDAREA();
	}

	@Override
	@Column(name = "usearea")
	public Double getUSEAREA() {
		return super.getUSEAREA();
	}

	@Override
	@Column(name = "publicarea")
	public Double getPUBLICAREA() {
		return super.getPUBLICAREA();
	}

	@Override
	@Column(name = "flooron")
	public Integer getFLOORON() {
		return super.getFLOORON();
	}

	@Override
	@Column(name = "flooronend")
	public Integer getFLOORONEND() {
		return super.getFLOORONEND();
	}

	@Override
	@Column(name = "type")
	public String getTYPE() {
		return super.getTYPE();
	}

	@Override
	@Column(name = "structure")
	public String getSTRUCTURE() {
		return super.getSTRUCTURE();
	}

	@Override
	@Column(name = "designusage")
	public String getDESIGNUSAGE() {
		return super.getDESIGNUSAGE();
	}

	@Override
	@Column(name = "usage")
	public String getUSAGE() {
		return super.getUSAGE();
	}

	@Override
	@Column(name = "buildid")
	public Integer getBUILDID() {
		return super.getBUILDID();
	}

	@Override
	@Column(name = "isvalid")
	public Integer getISVALID() {
		return super.getISVALID();
	}
}
