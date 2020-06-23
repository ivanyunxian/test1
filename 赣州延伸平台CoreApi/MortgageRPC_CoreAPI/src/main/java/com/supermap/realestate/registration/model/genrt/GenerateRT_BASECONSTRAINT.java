package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-12-22 
//* ----------------------------------------
//* Internal Entity rt_baseconstraint 
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

public class GenerateRT_BASECONSTRAINT implements SuperModel<String> {

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

	private String cONSTRAINTTYPE;
	private boolean modify_cONSTRAINTTYPE = false;

	public String getCONSTRAINTTYPE() {
		return cONSTRAINTTYPE;
	}

	public void setCONSTRAINTTYPE(String cONSTRAINTTYPE) {
		if (this.cONSTRAINTTYPE != cONSTRAINTTYPE) {
			this.cONSTRAINTTYPE = cONSTRAINTTYPE;
			modify_cONSTRAINTTYPE = true;
		}
	}

	private String bASEWORKFLOWID;
	private boolean modify_bASEWORKFLOWID = false;

	public String getBASEWORKFLOWID() {
		return bASEWORKFLOWID;
	}

	public void setBASEWORKFLOWID(String bASEWORKFLOWID) {
		if (this.bASEWORKFLOWID != bASEWORKFLOWID) {
			this.bASEWORKFLOWID = bASEWORKFLOWID;
			modify_bASEWORKFLOWID = true;
		}
	}

	private String cONSTRAINTID;
	private boolean modify_cONSTRAINTID = false;

	public String getCONSTRAINTID() {
		return cONSTRAINTID;
	}

	public void setCONSTRAINTID(String cONSTRAINTID) {
		if (this.cONSTRAINTID != cONSTRAINTID) {
			this.cONSTRAINTID = cONSTRAINTID;
			modify_cONSTRAINTID = true;
		}
	}

	private String rESULTTIP;
	private boolean modify_rESULTTIP = false;

	public String getRESULTTIP() {
		return rESULTTIP;
	}

	public void setRESULTTIP(String rESULTTIP) {
		if (this.rESULTTIP != rESULTTIP) {
			this.rESULTTIP = rESULTTIP;
			modify_rESULTTIP = true;
		}
	}

	private String cHECKLEVEL;
	private boolean modify_cHECKLEVEL = false;

	public String getCHECKLEVEL() {
		return cHECKLEVEL;
	}

	public void setCHECKLEVEL(String cHECKLEVEL) {
		if (this.cHECKLEVEL != cHECKLEVEL) {
			this.cHECKLEVEL = cHECKLEVEL;
			modify_cHECKLEVEL = true;
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

	private Date mODIFYTIME;
	private boolean modify_mODIFYTIME = false;

	public Date getMODIFYTIME() {
		return mODIFYTIME;
	}

	public void setMODIFYTIME(Date mODIFYTIME) {
		if (this.mODIFYTIME != mODIFYTIME) {
			this.mODIFYTIME = mODIFYTIME;
			modify_mODIFYTIME = true;
		}
	}

	private String cREATOR;
	private boolean modify_cREATOR = false;

	public String getCREATOR() {
		return cREATOR;
	}

	public void setCREATOR(String cREATOR) {
		if (this.cREATOR != cREATOR) {
			this.cREATOR = cREATOR;
			modify_cREATOR = true;
		}
	}

	private String lASTMODIFIER;
	private boolean modify_lASTMODIFIER = false;

	public String getLASTMODIFIER() {
		return lASTMODIFIER;
	}

	public void setLASTMODIFIER(String lASTMODIFIER) {
		if (this.lASTMODIFIER != lASTMODIFIER) {
			this.lASTMODIFIER = lASTMODIFIER;
			modify_lASTMODIFIER = true;
		}
	}

	private String tIPSQL;
	private boolean modify_tIPSQL = false;

	public String getTIPSQL() {
		return tIPSQL;
	}

	public void setTIPSQL(String tIPSQL) {
		if (this.tIPSQL != tIPSQL) {
			this.tIPSQL = tIPSQL;
			modify_tIPSQL = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_cONSTRAINTTYPE = false;
		modify_bASEWORKFLOWID = false;
		modify_cONSTRAINTID = false;
		modify_rESULTTIP = false;
		modify_cHECKLEVEL = false;
		modify_cREATETIME = false;
		modify_mODIFYTIME = false;
		modify_cREATOR = false;
		modify_lASTMODIFIER = false;
		modify_tIPSQL = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_cONSTRAINTTYPE)
			listStrings.add("cONSTRAINTTYPE");
		if (!modify_bASEWORKFLOWID)
			listStrings.add("bASEWORKFLOWID");
		if (!modify_cONSTRAINTID)
			listStrings.add("cONSTRAINTID");
		if (!modify_rESULTTIP)
			listStrings.add("rESULTTIP");
		if (!modify_cHECKLEVEL)
			listStrings.add("cHECKLEVEL");
		if (!modify_cREATETIME)
			listStrings.add("cREATETIME");
		if (!modify_mODIFYTIME)
			listStrings.add("mODIFYTIME");
		if (!modify_cREATOR)
			listStrings.add("cREATOR");
		if (!modify_lASTMODIFIER)
			listStrings.add("lASTMODIFIER");
		if (!modify_tIPSQL)
			listStrings.add("tIPSQL");

		return StringHelper.ListToStrings(listStrings);
	}
}
