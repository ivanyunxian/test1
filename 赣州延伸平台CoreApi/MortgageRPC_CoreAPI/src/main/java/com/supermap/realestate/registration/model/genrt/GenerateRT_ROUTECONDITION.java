package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015/12/18 
//* ----------------------------------------
//* Internal Entity rt_routecondition 
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

public class GenerateRT_ROUTECONDITION implements SuperModel<String> {

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

	private String rOUTEID;
	private boolean modify_rOUTEID = false;

	public String getROUTEID() {
		return rOUTEID;
	}

	public void setROUTEID(String rOUTEID) {
		if (this.rOUTEID != rOUTEID) {
			this.rOUTEID = rOUTEID;
			modify_rOUTEID = true;
		}
	}

	private String cHECKRULEID;
	private boolean modify_cHECKRULEID = false;

	public String getCHECKRULEID() {
		return cHECKRULEID;
	}

	public void setCHECKRULEID(String cHECKRULEID) {
		if (this.cHECKRULEID != cHECKRULEID) {
			this.cHECKRULEID = cHECKRULEID;
			modify_cHECKRULEID = true;
		}
	}

	private String cHECKLEVEL;
	private boolean modify_cHECKLEVEL = false;

	public String getCHECKLEVEL() {
		return cHECKLEVEL;
	}

	public void setCHECKLEVEL(String cHECKLEVEL) {
		if (this.cHECKLEVEL != cHECKLEVEL) {
			this.cHECKLEVEL = cHECKLEVEL;
			modify_cHECKLEVEL = true;
		}
	}

	private String rOUTETYPE;
	private boolean modify_rOUTETYPE = false;

	public String getROUTETYPE() {
		return rOUTETYPE;
	}

	public void setROUTETYPE(String rOUTETYPE) {
		if (this.rOUTETYPE != rOUTETYPE) {
			this.rOUTETYPE = rOUTETYPE;
			modify_rOUTETYPE = true;
		}
	}

	private Date cREATETIME;
	private boolean modify_cREATETIME = false;

	public Date getCREATETIME() {
		return cREATETIME;
	}

	public void setCREATETIME(Date cREATETIME) {
		if (this.cREATETIME != cREATETIME) {
			this.cREATETIME = cREATETIME;
			modify_cREATETIME = true;
		}
	}

	private Date mODIFYTIME;
	private boolean modify_mODIFYTIME = false;

	public Date getMODIFYTIME() {
		return mODIFYTIME;
	}

	public void setMODIFYTIME(Date mODIFYTIME) {
		if (this.mODIFYTIME != mODIFYTIME) {
			this.mODIFYTIME = mODIFYTIME;
			modify_mODIFYTIME = true;
		}
	}

	private String cREATOR;
	private boolean modify_cREATOR = false;

	public String getCREATOR() {
		return cREATOR;
	}

	public void setCREATOR(String cREATOR) {
		if (this.cREATOR != cREATOR) {
			this.cREATOR = cREATOR;
			modify_cREATOR = true;
		}
	}

	private String lASTMODIFIER;
	private boolean modify_lASTMODIFIER = false;

	public String getLASTMODIFIER() {
		return lASTMODIFIER;
	}

	public void setLASTMODIFIER(String lASTMODIFIER) {
		if (this.lASTMODIFIER != lASTMODIFIER) {
			this.lASTMODIFIER = lASTMODIFIER;
			modify_lASTMODIFIER = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_rOUTEID = false;
		modify_cHECKRULEID = false;
		modify_cHECKLEVEL = false;
		modify_rOUTETYPE = false;
		modify_cREATETIME = false;
		modify_mODIFYTIME = false;
		modify_cREATOR = false;
		modify_lASTMODIFIER = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_rOUTEID)
			listStrings.add("rOUTEID");
		if (!modify_cHECKRULEID)
			listStrings.add("cHECKRULEID");
		if (!modify_cHECKLEVEL)
			listStrings.add("cHECKLEVEL");
		if (!modify_rOUTETYPE)
			listStrings.add("rOUTETYPE");
		if (!modify_cREATETIME)
			listStrings.add("cREATETIME");
		if (!modify_mODIFYTIME)
			listStrings.add("mODIFYTIME");
		if (!modify_cREATOR)
			listStrings.add("cREATOR");
		if (!modify_lASTMODIFIER)
			listStrings.add("lASTMODIFIER");

		return StringHelper.ListToStrings(listStrings);
	}
}
