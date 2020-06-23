package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015/11/12 
//* ----------------------------------------
//* Internal Entity t_detail_selector 
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

public class GenerateT_DETAIL_SELECTOR implements SuperModel<String> {

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

	private String fIELDNAME;
	private boolean modify_fIELDNAME = false;

	public String getFIELDNAME() {
		return fIELDNAME;
	}

	public void setFIELDNAME(String fIELDNAME) {
		if (this.fIELDNAME != fIELDNAME) {
			this.fIELDNAME = fIELDNAME;
			modify_fIELDNAME = true;
		}
	}

	private String fIELDCOLOR;
	private boolean modify_fIELDCOLOR = false;

	public String getFIELDCOLOR() {
		return fIELDCOLOR;
	}

	public void setFIELDCOLOR(String fIELDCOLOR) {
		if (this.fIELDCOLOR != fIELDCOLOR) {
			this.fIELDCOLOR = fIELDCOLOR;
			modify_fIELDCOLOR = true;
		}
	}

	private String fIELDTEXT;
	private boolean modify_fIELDTEXT = false;

	public String getFIELDTEXT() {
		return fIELDTEXT;
	}

	public void setFIELDTEXT(String fIELDTEXT) {
		if (this.fIELDTEXT != fIELDTEXT) {
			this.fIELDTEXT = fIELDTEXT;
			modify_fIELDTEXT = true;
		}
	}

	private String sELECTORID;
	private boolean modify_sELECTORID = false;

	public String getSELECTORID() {
		return sELECTORID;
	}

	public void setSELECTORID(String sELECTORID) {
		if (this.sELECTORID != sELECTORID) {
			this.sELECTORID = sELECTORID;
			modify_sELECTORID = true;
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
	
	private Integer sXH;
	private boolean modify_sXH = false;

	public Integer getSXH() {
		return sXH;
	}

	public void setSXH(Integer sXH) {
		if (this.sXH != sXH) {
			this.sXH = sXH;
			modify_sXH = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_fIELDNAME = false;
		modify_fIELDCOLOR = false;
		modify_fIELDTEXT = false;
		modify_sELECTORID = false;
		modify_cREATETIME = false;
		modify_mODIFYTIME = false;
		modify_cREATOR = false;
		modify_lASTMODIFIER = false;
		modify_sXH = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_fIELDNAME)
			listStrings.add("fIELDNAME");
		if (!modify_fIELDCOLOR)
			listStrings.add("fIELDCOLOR");
		if (!modify_fIELDTEXT)
			listStrings.add("fIELDTEXT");
		if (!modify_sELECTORID)
			listStrings.add("sELECTORID");
		if (!modify_cREATETIME)
			listStrings.add("cREATETIME");
		if (!modify_mODIFYTIME)
			listStrings.add("mODIFYTIME");
		if (!modify_cREATOR)
			listStrings.add("cREATOR");
		if (!modify_lASTMODIFIER)
			listStrings.add("lASTMODIFIER");
		if (!modify_sXH)
			listStrings.add("sXH");

		return StringHelper.ListToStrings(listStrings);
	}
}
