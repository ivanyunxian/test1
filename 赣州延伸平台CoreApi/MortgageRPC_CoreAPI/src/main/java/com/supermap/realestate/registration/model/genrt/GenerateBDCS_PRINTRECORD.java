package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016-3-3 
//* ----------------------------------------
//* Internal Entity bdcs_printrecord 
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

public class GenerateBDCS_PRINTRECORD implements SuperModel<String> {

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

	private String pRINTTYPE;
	private boolean modify_pRINTTYPE = false;

	public String getPRINTTYPE() {
		return pRINTTYPE;
	}

	public void setPRINTTYPE(String pRINTTYPE) {
		if (this.pRINTTYPE != pRINTTYPE) {
			this.pRINTTYPE = pRINTTYPE;
			modify_pRINTTYPE = true;
		}
	}

	private Date pRINTTIME;
	private boolean modify_pRINTTIME = false;

	public Date getPRINTTIME() {
		return pRINTTIME;
	}

	public void setPRINTTIME(Date pRINTTIME) {
		if (this.pRINTTIME != pRINTTIME) {
			this.pRINTTIME = pRINTTIME;
			modify_pRINTTIME = true;
		}
	}

	private String pRINTSTAFF;
	private boolean modify_pRINTSTAFF = false;

	public String getPRINTSTAFF() {
		return pRINTSTAFF;
	}

	public void setPRINTSTAFF(String pRINTSTAFF) {
		if (this.pRINTSTAFF != pRINTSTAFF) {
			this.pRINTSTAFF = pRINTSTAFF;
			modify_pRINTSTAFF = true;
		}
	}

	private String rEMARK;
	private boolean modify_rEMARK = false;

	public String getREMARK() {
		return rEMARK;
	}

	public void setREMARK(String rEMARK) {
		if (this.rEMARK != rEMARK) {
			this.rEMARK = rEMARK;
			modify_rEMARK = true;
		}
	}

	private String pROJECT_ID;
	private boolean modify_pROJECT_ID = false;

	public String getPROJECT_ID() {
		return pROJECT_ID;
	}

	public void setPROJECT_ID(String pROJECT_ID) {
		if (this.pROJECT_ID != pROJECT_ID) {
			this.pROJECT_ID = pROJECT_ID;
			modify_pROJECT_ID = true;
		}
	}

	private String xMBH;
	private boolean modify_xMBH = false;

	public String getXMBH() {
		return xMBH;
	}

	public void setXMBH(String xMBH) {
		if (this.xMBH != xMBH) {
			this.xMBH = xMBH;
			modify_xMBH = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_pRINTTYPE = false;
		modify_pRINTTIME = false;
		modify_pRINTSTAFF = false;
		modify_rEMARK = false;
		modify_pROJECT_ID = false;
		modify_xMBH = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_pRINTTYPE)
			listStrings.add("pRINTTYPE");
		if (!modify_pRINTTIME)
			listStrings.add("pRINTTIME");
		if (!modify_pRINTSTAFF)
			listStrings.add("pRINTSTAFF");
		if (!modify_rEMARK)
			listStrings.add("rEMARK");
		if (!modify_pROJECT_ID)
			listStrings.add("pROJECT_ID");
		if (!modify_xMBH)
			listStrings.add("xMBH");

		return StringHelper.ListToStrings(listStrings);
	}
}
