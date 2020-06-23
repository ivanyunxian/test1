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

public class GenerateBDCS_NETSIGN implements SuperModel<String> {

	private String id;
	private boolean modify_id = false;

	@Override
	public String getId() {
		if (!modify_id && id == null) {
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

	private String bDCDYID;
	private boolean modify_bDCDYID = false;

	public String getBDCDYID() {
		return bDCDYID;
	}

	public void setBDCDYID(String bDCDYID) {
		if (this.bDCDYID != bDCDYID) {
			this.bDCDYID = bDCDYID;
			modify_bDCDYID = true;
		}
	}

	private String hTH;
	private boolean modify_hTH = false;

	public String getHTH() {
		return hTH;
	}

	public void setHTH(String hTH) {
		if (this.hTH != hTH) {
			this.hTH = hTH;
			modify_hTH = true;
		}
	}

	private String mSR;
	private boolean modify_mSR = false;

	public String getMSR() {
		return mSR;
	}

	public void setMSR(String mSR) {
		if (this.mSR != mSR) {
			this.mSR = mSR;
			modify_mSR = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_bDCDYID = false;
		modify_hTH = false;
		modify_mSR = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_bDCDYID)
			listStrings.add("bDCDYID");
		if (!modify_hTH)
			listStrings.add("hTH");
		if (!modify_mSR)
			listStrings.add("mSR");

		return StringHelper.ListToStrings(listStrings);
	}
}
