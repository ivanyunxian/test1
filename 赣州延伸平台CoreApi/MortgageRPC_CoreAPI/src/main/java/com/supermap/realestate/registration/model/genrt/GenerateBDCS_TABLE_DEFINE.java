package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2017/05/16 
//* ----------------------------------------
//* Internal Entity bdcs_table_define 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateBDCS_TABLE_DEFINE implements SuperModel<String> {

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

	private String gROUPID;
	private boolean modify_gROUPID = false;

	public String getGROUPID() {
		return gROUPID;
	}

	public void setGROUPID(String gROUPID) {
		if (this.gROUPID != gROUPID) {
			this.gROUPID = gROUPID;
			modify_gROUPID = true;
		}
	}

	private String sTATUS;
	private boolean modify_sTATUS = false;

	public String getSTATUS() {
		return sTATUS;
	}

	public void setSTATUS(String sTATUS) {
		if (this.sTATUS != sTATUS) {
			this.sTATUS = sTATUS;
			modify_sTATUS = true;
		}
	}

	private String sTATUSVALUE;
	private boolean modify_sTATUSVALUE = false;

	public String getSTATUSVALUE() {
		return sTATUSVALUE;
	}

	public void setSTATUSVALUE(String sTATUSVALUE) {
		if (this.sTATUSVALUE != sTATUSVALUE) {
			this.sTATUSVALUE = sTATUSVALUE;
			modify_sTATUSVALUE = true;
		}
	}

	private String dESCRIPTION;
	private boolean modify_dESCRIPTION = false;

	public String getDESCRIPTION() {
		return dESCRIPTION;
	}

	public void setDESCRIPTION(String dESCRIPTION) {
		if (this.dESCRIPTION != dESCRIPTION) {
			this.dESCRIPTION = dESCRIPTION;
			modify_dESCRIPTION = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_gROUPID = false;
		modify_sTATUS = false;
		modify_sTATUSVALUE = false;
		modify_dESCRIPTION = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_gROUPID)
			listStrings.add("gROUPID");
		if (!modify_sTATUS)
			listStrings.add("sTATUS");
		if (!modify_sTATUSVALUE)
			listStrings.add("sTATUSVALUE");
		if (!modify_dESCRIPTION)
			listStrings.add("dESCRIPTION");

		return StringHelper.ListToStrings(listStrings);
	}
}
