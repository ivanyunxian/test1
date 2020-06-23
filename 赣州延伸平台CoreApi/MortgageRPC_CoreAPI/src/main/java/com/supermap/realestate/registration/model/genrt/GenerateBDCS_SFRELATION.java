package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-7-22 
//* ----------------------------------------
//* Internal Entity bdcs_sfrelation 
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

public class GenerateBDCS_SFRELATION implements SuperModel<String> {

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

	private String pRODEF_ID;
	private boolean modify_pRODEF_ID = false;

	public String getPRODEF_ID() {
		return pRODEF_ID;
	}

	public void setPRODEF_ID(String pRODEF_ID) {
		if (this.pRODEF_ID != pRODEF_ID) {
			this.pRODEF_ID = pRODEF_ID;
			modify_pRODEF_ID = true;
		}
	}

	private String sFDYID;
	private boolean modify_sFDYID = false;

	public String getSFDYID() {
		return sFDYID;
	}

	public void setSFDYID(String sFDYID) {
		if (this.sFDYID != sFDYID) {
			this.sFDYID = sFDYID;
			modify_sFDYID = true;
		}
	}

	private Date cREAT_TIME;
	private boolean modify_cREAT_TIME = false;

	public Date getCREAT_TIME() {
		return cREAT_TIME;
	}

	public void setCREAT_TIME(Date cREAT_TIME) {
		if (this.cREAT_TIME != cREAT_TIME) {
			this.cREAT_TIME = cREAT_TIME;
			modify_cREAT_TIME = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_pRODEF_ID = false;
		modify_sFDYID = false;
		modify_cREAT_TIME = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_pRODEF_ID)
			listStrings.add("pRODEF_ID");
		if (!modify_sFDYID)
			listStrings.add("sFDYID");
		if (!modify_cREAT_TIME)
			listStrings.add("cREAT_TIME");

		return StringHelper.ListToStrings(listStrings);
	}
}
