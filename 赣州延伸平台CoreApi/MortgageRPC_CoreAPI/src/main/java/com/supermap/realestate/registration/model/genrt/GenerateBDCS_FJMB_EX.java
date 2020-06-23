package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-7-11 
//* ----------------------------------------
//* Internal Entity bdcs_qlr_gz 
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

public class GenerateBDCS_FJMB_EX implements SuperModel<String>{

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

	public String getName() {
		return nAME;
	}

	public void setName(String nAME) {
		if (this.nAME != nAME) {
			this.nAME = nAME;
			modify_nAME = true;
		}
	}

	private String sQL;
	private boolean modify_sQL = false;

	public String getSql() {
		return sQL;
	}

	public void setSql(String sQL) {
		if (this.sQL != sQL) {
			this.sQL = sQL;
			modify_sQL = true;
		}
	}
	
	private String cONDITION;
	private boolean modify_cONDITION = false;

	public String getCondition() {
		return cONDITION;
	}

	public void setCondition(String cONDITION) {
		if (this.cONDITION != cONDITION) {
			this.cONDITION = cONDITION;
			modify_cONDITION = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_nAME = false;
		modify_sQL = false;
		modify_cONDITION = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_nAME)
			listStrings.add("nAME");
		if (!modify_sQL)
			listStrings.add("sQL");
		if (!modify_cONDITION)
			listStrings.add("cONDITION");

		return StringHelper.ListToStrings(listStrings);
	}
}
