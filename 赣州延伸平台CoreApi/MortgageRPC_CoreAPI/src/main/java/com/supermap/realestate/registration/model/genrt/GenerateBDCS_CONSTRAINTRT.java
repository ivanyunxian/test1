package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-7-23 
//* ----------------------------------------
//* Internal Entity bdcs_constraintrt 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateBDCS_CONSTRAINTRT implements SuperModel<String> {

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

	private String wORKFLOWID;
	private boolean modify_wORKFLOWID = false;

	public String getWORKFLOWID() {
		return wORKFLOWID;
	}

	public void setWORKFLOWID(String wORKFLOWID) {
		if (this.wORKFLOWID != wORKFLOWID) {
			this.wORKFLOWID = wORKFLOWID;
			modify_wORKFLOWID = true;
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

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_wORKFLOWID = false;
		modify_cONSTRAINTID = false;
		modify_cONSTRAINTTYPE = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_wORKFLOWID)
			listStrings.add("wORKFLOWID");
		if (!modify_cONSTRAINTID)
			listStrings.add("cONSTRAINTID");
		if (!modify_cONSTRAINTTYPE)
			listStrings.add("cONSTRAINTTYPE");

		return StringHelper.ListToStrings(listStrings);
	}
}
