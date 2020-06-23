package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/6/27 
//* ----------------------------------------
//* Internal Entity bdcs_ztcxlog 
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


public class GenerateBDCS_ZTCXLOG implements SuperModel<String> {

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

	private Date cXSJ;
	private boolean modify_cXSJ = false;

	public Date getCXSJ() {
		return cXSJ;
	}

	public void setCXSJ(Date cXSJ) {
		if (this.cXSJ != cXSJ) {
			this.cXSJ = cXSJ;
			modify_cXSJ = true;
		}
	}

	private String cXTJ;
	private boolean modify_cXTJ = false;

	public String getCXTJ() {
		return cXTJ;
	}

	public void setCXTJ(String cXTJ) {
		if (this.cXTJ != cXTJ) {
			this.cXTJ = cXTJ;
			modify_cXTJ = true;
		}
	}

	private String iP;
	private boolean modify_iP = false;

	public String getIP() {
		return iP;
	}

	public void setIP(String iP) {
		if (this.iP != iP) {
			this.iP = iP;
			modify_iP = true;
		}
	}


	public void resetModifyState() {
		modify_id = false;
		modify_cXSJ = false;
		modify_cXTJ = false;
		modify_iP = false;
	}


	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_cXSJ)
			listStrings.add("cXSJ");
		if (!modify_cXTJ)
			listStrings.add("cXTJ");
		if (!modify_iP)
			listStrings.add("iP");

		return StringHelper.ListToStrings(listStrings);
	}
}
