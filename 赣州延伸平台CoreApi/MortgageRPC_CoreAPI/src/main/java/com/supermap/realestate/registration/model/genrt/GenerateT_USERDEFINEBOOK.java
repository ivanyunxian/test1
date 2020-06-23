package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/5/4 
//* ----------------------------------------
//* Internal Entity t_userdefinebook 
//* ----------------------------------------
//* Copyright (C) SuperMap Software Co.,Ltd.  
//*****************************************/

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateT_USERDEFINEBOOK implements SuperModel<String> {

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

	private String nAME;
	private boolean modify_nAME = false;

	public String getNAME() {
		return nAME;
	}

	public void setNAME(String nAME) {
		if (this.nAME != nAME) {
			this.nAME = nAME;
			modify_nAME = true;
		}
	}

	private String cONFIGSQL;
	private boolean modify_cONFIGSQL = false;

	public String getCONFIGSQL() {
		return cONFIGSQL;
	}

	public void setCONFIGSQL(String cONFIGSQL) {
		if (this.cONFIGSQL != cONFIGSQL) {
			this.cONFIGSQL = cONFIGSQL;
			modify_cONFIGSQL = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_nAME = false;
		modify_cONFIGSQL = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_nAME)
			listStrings.add("nAME");
		if (!modify_cONFIGSQL)
			listStrings.add("cONFIGSQL");

		return StringHelper.ListToStrings(listStrings);
	}
}
