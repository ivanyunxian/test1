package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2017/05/16 
//* ----------------------------------------
//* Internal Entity bdcs_table_color 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateBDCS_TABLE_COLOR implements SuperModel<String> {

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

	private String gROUPNAME;
	private boolean modify_gROUPNAME = false;

	public String getGROUPNAME() {
		return gROUPNAME;
	}

	public void setGROUPNAME(String gROUPNAME) {
		if (this.gROUPNAME != gROUPNAME) {
			this.gROUPNAME = gROUPNAME;
			modify_gROUPNAME = true;
		}
	}

	private String gROUPCOLOR;
	private boolean modify_gROUPCOLOR = false;

	public String getGROUPCOLOR() {
		return gROUPCOLOR;
	}

	public void setGROUPCOLOR(String gROUPCOLOR) {
		if (this.gROUPCOLOR != gROUPCOLOR) {
			this.gROUPCOLOR = gROUPCOLOR;
			modify_gROUPCOLOR = true;
		}
	}

	private String gROUPINDEX;
	private boolean modify_gROUPINDEX = false;

	public String getGROUPINDEX() {
		return gROUPINDEX;
	}

	public void setGROUPINDEX(String gROUPINDEX) {
		if (this.gROUPINDEX != gROUPINDEX) {
			this.gROUPINDEX = gROUPINDEX;
			modify_gROUPINDEX = true;
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
		modify_gROUPNAME = false;
		modify_gROUPCOLOR = false;
		modify_gROUPINDEX = false;
		modify_dESCRIPTION = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_gROUPNAME)
			listStrings.add("gROUPNAME");
		if (!modify_gROUPCOLOR)
			listStrings.add("gROUPCOLOR");
		if (!modify_gROUPINDEX)
			listStrings.add("gROUPINDEX");
		if (!modify_dESCRIPTION)
			listStrings.add("dESCRIPTION");

		return StringHelper.ListToStrings(listStrings);
	}
}
