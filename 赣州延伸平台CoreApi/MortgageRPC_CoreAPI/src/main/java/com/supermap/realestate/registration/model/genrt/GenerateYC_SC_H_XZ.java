package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-7-8 
//* ----------------------------------------
//* Internal Entity yc_sc_h_xz 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateYC_SC_H_XZ implements SuperModel<String> {

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

	private String yCBDCDYID;
	private boolean modify_yCBDCDYID = false;

	public String getYCBDCDYID() {
		return yCBDCDYID;
	}

	public void setYCBDCDYID(String yCBDCDYID) {
		if (this.yCBDCDYID != yCBDCDYID) {
			this.yCBDCDYID = yCBDCDYID;
			modify_yCBDCDYID = true;
		}
	}

	private String sCBDCDYID;
	private boolean modify_sCBDCDYID = false;

	public String getSCBDCDYID() {
		return sCBDCDYID;
	}

	public void setSCBDCDYID(String sCBDCDYID) {
		if (this.sCBDCDYID != sCBDCDYID) {
			this.sCBDCDYID = sCBDCDYID;
			modify_sCBDCDYID = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_yCBDCDYID = false;
		modify_sCBDCDYID = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_yCBDCDYID)
			listStrings.add("yCBDCDYID");
		if (!modify_sCBDCDYID)
			listStrings.add("sCBDCDYID");

		return StringHelper.ListToStrings(listStrings);
	}
}
