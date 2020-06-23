package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-9-23 
//* ----------------------------------------
//* Internal Entity bdcs_djyymb 
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

public class GenerateBDCS_FJMB implements SuperModel<String> {

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

	private String bZ;
	private boolean modify_bZ = false;

	public String getBZ() {
		return bZ;
	}

	public void setBZ(String bZ) {
		if (this.bZ != bZ) {
			this.bZ = bZ;
			modify_bZ = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_uSERID = false;
		modify_uSERNAME = false;
		modify_fJ = false;
		modify_cREATETIME = false;
		modify_mODIFYTIME = false;
		modify_bZ = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_uSERID)
			listStrings.add("uSERID");
		if (!modify_uSERNAME)
			listStrings.add("uSERNAME");
		if (!modify_fJ)
			listStrings.add("fJ");
		if (!modify_cREATETIME)
			listStrings.add("cREATETIME");
		if (!modify_mODIFYTIME)
			listStrings.add("mODIFYTIME");
		if (!modify_bZ)
			listStrings.add("bZ");

		return StringHelper.ListToStrings(listStrings);
	}
}
