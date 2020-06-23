package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016-1-20 
//* ----------------------------------------
//* Internal Entity bdcs_zs_ls 
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

public class GenerateBDCS_ZS_LS implements SuperModel<String> {

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

	private String zSBH;
	private boolean modify_zSBH = false;

	public String getZSBH() {
		return zSBH;
	}

	public void setZSBH(String zSBH) {
		if (this.zSBH != zSBH) {
			this.zSBH = zSBH;
			modify_zSBH = true;
		}
	}

	private String bDCQZH;
	private boolean modify_bDCQZH = false;

	public String getBDCQZH() {
		return bDCQZH;
	}

	public void setBDCQZH(String bDCQZH) {
		if (this.bDCQZH != bDCQZH) {
			this.bDCQZH = bDCQZH;
			modify_bDCQZH = true;
		}
	}

	private Date sZSJ;
	private boolean modify_sZSJ = false;

	public Date getSZSJ() {
		return sZSJ;
	}

	public void setSZSJ(Date sZSJ) {
		if (this.sZSJ != sZSJ) {
			this.sZSJ = sZSJ;
			modify_sZSJ = true;
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

	private String cFDAGH;
	private boolean modify_cFDAGH = false;

	public String getCFDAGH() {
		return cFDAGH;
	}

	public void setCFDAGH(String cFDAGH) {
		if (this.cFDAGH != cFDAGH) {
			this.cFDAGH = cFDAGH;
			modify_cFDAGH = true;
		}
	}

	private String zSDATA;
	private boolean modify_zSDATA = false;

	public String getZSDATA() {
		return zSDATA;
	}

	public void setZSDATA(String zSDATA) {
		if (this.zSDATA != zSDATA) {
			this.zSDATA = zSDATA;
			modify_zSDATA = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_xMBH = false;
		modify_zSBH = false;
		modify_bDCQZH = false;
		modify_sZSJ = false;
		modify_cREATETIME = false;
		modify_mODIFYTIME = false;
		modify_cFDAGH = false;
		modify_zSDATA = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_zSBH)
			listStrings.add("zSBH");
		if (!modify_bDCQZH)
			listStrings.add("bDCQZH");
		if (!modify_sZSJ)
			listStrings.add("sZSJ");
		if (!modify_cREATETIME)
			listStrings.add("cREATETIME");
		if (!modify_mODIFYTIME)
			listStrings.add("mODIFYTIME");
		if (!modify_cFDAGH)
			listStrings.add("cFDAGH");
		if (!modify_zSDATA)
			listStrings.add("zSDATA");

		return StringHelper.ListToStrings(listStrings);
	}
}
