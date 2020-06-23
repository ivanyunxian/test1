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

public class GenerateBDCS_QLR_EX implements SuperModel<String>{

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

	private String qLRMC;
	private boolean modify_qLRMC = false;

	public String getQLRMC() {
		return qLRMC;
	}

	public void setQLRMC(String qLRMC) {
		if (this.qLRMC != qLRMC) {
			this.qLRMC = qLRMC;
			modify_qLRMC = true;
		}
	}

	private String zJH;
	private boolean modify_zJH = false;

	public String getZJH() {
		return zJH;
	}

	public void setZJH(String zJH) {
		if (this.zJH != zJH) {
			this.zJH = zJH;
			modify_zJH = true;
		}
	}
	
	private String iNDEXNAME;
	private boolean modify_iNDEXNAME = false;
	public String getINDEXNAME() {
		return iNDEXNAME;
	}

	public void setINDEXNAME(String iNDEXNAME) {
		if (this.iNDEXNAME != iNDEXNAME) {
			this.iNDEXNAME = iNDEXNAME;
			modify_iNDEXNAME = true;
		}
	}

	

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_qLRMC = false;
		modify_zJH = false;
		modify_iNDEXNAME = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_qLRMC)
			listStrings.add("qLRMC");
		if (!modify_zJH)
			listStrings.add("zJH");
		if(!modify_iNDEXNAME)
			listStrings.add("iNDEXNAME");

		return StringHelper.ListToStrings(listStrings);
	}
}
