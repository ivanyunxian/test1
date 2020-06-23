package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-24 
//* ----------------------------------------
//* Internal Entity bdcs_fsql_gz 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GenerateBDCS_WSXX implements SuperModel<String> {

	private String id;
	private boolean modify_id = false;

	@Override
	public String getId() {
		if (!modify_id && id == null)
		{
			id = (String) SuperHelper.GeneratePrimaryKey();
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

	private String xMBH;
	private boolean modify_xMBH = false;

	public String getXMBH() {
		return xMBH;
	}

	public void setXMBH(String xMBH) {
		if (this.xMBH != xMBH) {
			this.xMBH = xMBH;
			modify_xMBH = true;
		}
	}

	private String fSQLID;
	private boolean modify_fSQLID = false;

	public String getFSQLID() {
		return fSQLID;
	}

	public void setFSQLID(String fSQLID) {
		if (this.fSQLID != fSQLID) {
			this.fSQLID = fSQLID;
			modify_fSQLID = true;
		}
	}

	private String sSZL;
	private boolean modify_sSZL = false;

	public String getSSZL() {
		return sSZL;
	}

	public void setSSZL(String sSZL) {
		if (this.sSZL != sSZL) {
			this.sSZL = sSZL;
			modify_sSZL = true;
		}
	}


	private String sSZLMC;
	private boolean modify_sSZLMC = false;

	public String getSSZLMC() {
		return sSZLMC;
	}

	public void setSSZLMC(String sSZLMC) {
		if (this.sSZLMC != sSZLMC) {
			this.sSZLMC = sSZLMC;
			modify_sSZLMC = true;
		}
	}

	private String fJ;
	private boolean modify_fJ = false;

	public String getFJ() {
		return fJ;
	}

	public void setFJ(String fJ) {
		if (this.fJ != fJ) {
			this.fJ = fJ;
			modify_fJ = true;
		}
	}


	private String wSPZH;
	private boolean modify_wSPZH = false;

	public String getWSPZH() {
		return wSPZH;
	}

	public void setWSPZH(String wSPZH) {
		if (this.wSPZH != wSPZH) {
			this.wSPZH = wSPZH;
			modify_wSPZH = true;
		}
	}

	private Date wSSJ;
	private boolean modify_wSSJ = false;

	public Date getWSSJ() {
		return wSSJ;
	}

	public void setWSSJ(Date wSSJ) {
		if (this.wSSJ != wSSJ) {
			this.wSSJ = wSSJ;
			modify_wSSJ = true;
		}
	}

	private Double wSJE;
	private boolean modify_wSJE = false;

	public Double getWSJE() {
		return wSJE;
	}

	public void setWSJE(Double wSJE) {
		if (this.wSJE != wSJE) {
			this.wSJE = wSJE;
			modify_wSJE = true;
		}
	}

	private String yXBZ;
	private boolean modify_yXBZ = false;

	public String getYXBZ() {
		return yXBZ;
	}

	public void setYXBZ(String yXBZ) {
		if (this.yXBZ != yXBZ) {
			this.yXBZ = yXBZ;
			modify_yXBZ = true;
		}
	}

	private Date cREATETIME;
	private boolean modify_cREATETIME = false;

	public Date getCREATETIME() {
		return cREATETIME;
	}

	public void setCREATETIME(Date cREATETIME) {
		if (this.cREATETIME != cREATETIME) {
			this.cREATETIME = cREATETIME;
			modify_cREATETIME = true;
		}
	}

	private Date tIME;
	private boolean modify_tIME = false;

	public Date getTIME() {
		return tIME;
	}

	public void setTIME(Date tIME) {
		if (this.tIME != tIME) {
			this.tIME = tIME;
			modify_tIME = true;
		}
	}

	private String uSERID;
	private boolean modify_uSERID = false;

	public String getUSERID() {
		return uSERID;
	}

	public void setUSERID(String uSERID) {
		if (this.uSERID != uSERID) {
			this.uSERID = uSERID;
			modify_uSERID = true;
		}
	}

	private String uSERNAME;
	private boolean modify_uSERNAME = false;

	public String getUSERNAME() {
		return uSERNAME;
	}

	public void setUSERNAME(String uSERNAME) {
		if (this.uSERNAME != uSERNAME) {
			this.uSERNAME = uSERNAME;
			modify_uSERNAME = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_xMBH = false;
		modify_fSQLID = false;
		modify_sSZL = false;
		modify_sSZLMC = false;
		modify_fJ = false;
		modify_yXBZ = false;
		modify_cREATETIME = false;
		modify_tIME = false;
		modify_uSERID = false;
		modify_uSERNAME = false;

	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_fSQLID)
			listStrings.add("fSQLID");
		if (!modify_sSZL)
			listStrings.add("sSZL");
		if (!modify_sSZLMC)
			listStrings.add("sSZLMC");
		if (!modify_fJ)
			listStrings.add("fJ");
		if (!modify_sSZL)
			listStrings.add("sSZL");
		if (!modify_sSZLMC)
			listStrings.add("sSZLMC");
		if (!modify_cREATETIME)
			listStrings.add("cREATETIME");
		if (!modify_tIME)
			listStrings.add("tIME");
		if (!modify_uSERID)
			listStrings.add("uSERID");
		if (!modify_uSERNAME)
			listStrings.add("uSERNAME");
		if (!modify_wSPZH)
			listStrings.add("wSPZH");
		if (!modify_wSSJ)
			listStrings.add("wSSJ");
		if (!modify_wSJE)
			listStrings.add("wSJE");

		return StringHelper.ListToStrings(listStrings);
	}
}
