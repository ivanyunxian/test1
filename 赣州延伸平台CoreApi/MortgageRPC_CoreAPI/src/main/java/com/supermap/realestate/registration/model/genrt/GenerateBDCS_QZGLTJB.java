package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-10-31 
//* ----------------------------------------
//* Internal Entity bdcs_qzgltjb 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateBDCS_QZGLTJB implements SuperModel<String> {

	private String id;
	private boolean modify_id = false;

	@Override
	public String getId() {
		if (!modify_id && id == null)
		{
			id = SuperHelper.GeneratePrimaryKey();
			modify_id = true;
		}
		return id;
	}

	@Override
	public void setId(String id) {
		if (this.id != id) {
			this.id = id;
			modify_id = true;
		}
	}

	private String rYID;
	private boolean modify_rYID = false;

	public String getRYID() {
		return rYID;
	}

	public void setRYID(String rYID) {
		if (this.rYID != rYID) {
			this.rYID = rYID;
			modify_rYID = true;
		}
	}

	private String rYDLM;
	private boolean modify_rYDLM = false;

	public String getRYDLM() {
		return rYDLM;
	}

	public void setRYDLM(String rYDLM) {
		if (this.rYDLM != rYDLM) {
			this.rYDLM = rYDLM;
			modify_rYDLM = true;
		}
	}

	private Date cJSJ;
	private boolean modify_cJSJ = false;

	public Date getCJSJ() {
		return cJSJ;
	}

	public void setCJSJ(Date cJSJ) {
		if (this.cJSJ != cJSJ) {
			this.cJSJ = cJSJ;
			modify_cJSJ = true;
		}
	}

	private Long qSQZBH;
	private boolean modify_qSQZBH = false;

	public Long getQSQZBH() {
		return qSQZBH;
	}

	public void setQSQZBH(Long qSQZBH) {
		if (this.qSQZBH != qSQZBH) {
			this.qSQZBH = qSQZBH;
			modify_qSQZBH = true;
		}
	}

	private Long jSQZBH;
	private boolean modify_jSQZBH = false;

	public Long getJSQZBH() {
		return jSQZBH;
	}

	public void setJSQZBH(Long jSQZBH) {
		if (this.jSQZBH != jSQZBH) {
			this.jSQZBH = jSQZBH;
			modify_jSQZBH = true;
		}
	}

	private String qZLX;
	private boolean modify_qZLX = false;

	public String getQZLX() {
		return qZLX;
	}

	public void setQZLX(String qZLX) {
		if (this.qZLX != qZLX) {
			this.qZLX = qZLX;
			modify_qZLX = true;
		}
	}

	private String sFXSYSZ;
	private boolean modify_sFXSYSZ = false;

	public String getSFXSYSZ() {
		return sFXSYSZ;
	}

	public void setSFXSYSZ(String sFXSYSZ) {
		if (this.sFXSYSZ != sFXSYSZ) {
			this.sFXSYSZ = sFXSYSZ;
			modify_sFXSYSZ = true;
		}
	}

	private String sFXSYZF;
	private boolean modify_sFXSYZF = false;

	public String getSFXSYZF() {
		return sFXSYZF;
	}

	public void setSFXSYZF(String sFXSYZF) {
		if (this.sFXSYZF != sFXSYZF) {
			this.sFXSYZF = sFXSYZF;
			modify_sFXSYZF = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_rYID = false;
		modify_rYDLM = false;
		modify_cJSJ = false;
		modify_qSQZBH = false;
		modify_jSQZBH = false;
		modify_qZLX = false;
		modify_sFXSYSZ = false;
		modify_sFXSYZF = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_rYID)
			listStrings.add("rYID");
		if (!modify_rYDLM)
			listStrings.add("rYDLM");
		if (!modify_cJSJ)
			listStrings.add("cJSJ");
		if (!modify_qSQZBH)
			listStrings.add("qSQZBH");
		if (!modify_jSQZBH)
			listStrings.add("jSQZBH");
		if (!modify_qZLX)
			listStrings.add("qZLX");
		if (!modify_sFXSYSZ)
			listStrings.add("sFXSYSZ");
		if (!modify_sFXSYZF)
			listStrings.add("sFXSYZF");

		return StringHelper.ListToStrings(listStrings);
	}
}
