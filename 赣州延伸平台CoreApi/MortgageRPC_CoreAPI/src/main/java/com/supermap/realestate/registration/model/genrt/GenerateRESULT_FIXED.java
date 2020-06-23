package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2017/06/28 
//* ----------------------------------------
//* Internal Entity result_fixed 
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

public class GenerateRESULT_FIXED implements SuperModel<String> {

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

	private String fIXEDLX;
	private boolean modify_fIXEDLX = false;

	public String getFIXEDLX() {
		return fIXEDLX;
	}

	public void setFIXEDLX(String fIXEDLX) {
		if (this.fIXEDLX != fIXEDLX) {
			this.fIXEDLX = fIXEDLX;
			modify_fIXEDLX = true;
		}
	}

	private String fIXEDCONTEXT;
	private boolean modify_fIXEDCONTEXT = false;

	public String getFIXEDCONTEXT() {
		return fIXEDCONTEXT;
	}

	public void setFIXEDCONTEXT(String fIXEDCONTEXT) {
		if (this.fIXEDCONTEXT != fIXEDCONTEXT) {
			this.fIXEDCONTEXT = fIXEDCONTEXT;
			modify_fIXEDCONTEXT = true;
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

	private Date fIXEDENDTIME;
	private boolean modify_fIXEDENDTIME = false;

	public Date getFIXEDENDTIME() {
		return fIXEDENDTIME;
	}

	public void setFIXEDENDTIME(Date fIXEDENDTIME) {
		if (this.fIXEDENDTIME != fIXEDENDTIME) {
			this.fIXEDENDTIME = fIXEDENDTIME;
			modify_fIXEDENDTIME = true;
		}
	}

	private Date fIXEDSTARTTIME;
	private boolean modify_fIXEDSTARTTIME = false;

	public Date getFIXEDSTARTTIME() {
		return fIXEDSTARTTIME;
	}

	public void setFIXEDSTARTTIME(Date fIXEDSTARTTIME) {
		if (this.fIXEDSTARTTIME != fIXEDSTARTTIME) {
			this.fIXEDSTARTTIME = fIXEDSTARTTIME;
			modify_fIXEDSTARTTIME = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_fIXEDLX = false;
		modify_fIXEDCONTEXT = false;
		modify_cREATETIME = false;
		modify_cREATOR = false;
		modify_mODIFYTIME = false;
		modify_lASTMODIFIER = false;
		modify_fIXEDENDTIME = false;
		modify_fIXEDSTARTTIME = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_fIXEDLX)
			listStrings.add("fIXEDLX");
		if (!modify_fIXEDCONTEXT)
			listStrings.add("fIXEDCONTEXT");
		if (!modify_cREATETIME)
			listStrings.add("cREATETIME");
		if (!modify_cREATOR)
			listStrings.add("cREATOR");
		if (!modify_mODIFYTIME)
			listStrings.add("mODIFYTIME");
		if (!modify_lASTMODIFIER)
			listStrings.add("lASTMODIFIER");
		if (!modify_fIXEDENDTIME)
			listStrings.add("fIXEDENDTIME");
		if (!modify_fIXEDSTARTTIME)
			listStrings.add("fIXEDSTARTTIME");

		return StringHelper.ListToStrings(listStrings);
	}
}
