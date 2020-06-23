package com.supermap.wisdombusiness.framework.model.gxjyk.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/4/8 
//* ----------------------------------------
//* Internal Entity fjdata 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class GenerateFJDATA implements SuperModel<String> {

	private String id;
	private boolean modify_id = false;


	public String getId() {
		if (!modify_id && id == null)
		{
			id = SuperHelper.GeneratePrimaryKey();
			modify_id = true;
		}
		return id;
	}


	public void setId(String id) {
		if (this.id != id) {
			this.id = id;
			modify_id = true;
		}
	}

	private String fJID;
	private boolean modify_fJID = false;

	public String getFJID() {
		return fJID;
	}

	public void setFJID(String fJID) {
		if (this.fJID != fJID) {
			this.fJID = fJID;
			modify_fJID = true;
		}
	}

	private Byte[] nR;
	private boolean modify_nR = false;

	public Byte[] getNR() {
		return nR;
	}

	public void setNR(Byte[] nR) {
		if (this.nR != nR) {
			this.nR = nR;
			modify_nR = true;
		}
	}


	public void resetModifyState() {
		modify_id = false;
		modify_fJID = false;
		modify_nR = false;
	}


	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_fJID)
			listStrings.add("fJID");
		if (!modify_nR)
			listStrings.add("nR");

		return StringHelper.ListToStrings(listStrings);
	}
}
