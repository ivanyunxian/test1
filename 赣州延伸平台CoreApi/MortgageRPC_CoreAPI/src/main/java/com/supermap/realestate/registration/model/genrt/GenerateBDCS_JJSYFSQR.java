package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-9-24 
//* ----------------------------------------
//* Internal Entity bdcs_jjsyfsqr 
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

public class GenerateBDCS_JJSYFSQR implements SuperModel<String> {

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

	private String sQRXM;
	private boolean modify_sQRXM = false;

	public String getSQRXM() {
		return sQRXM;
	}

	public void setSQRXM(String sQRXM) {
		if (this.sQRXM != sQRXM) {
			this.sQRXM = sQRXM;
			modify_sQRXM = true;
		}
	}

	private String sQRZJLX;
	private boolean modify_sQRZJLX = false;

	public String getSQRZJLX() {
		return sQRZJLX;
	}

	public void setSQRZJLX(String sQRZJLX) {
		if (this.sQRZJLX != sQRZJLX) {
			this.sQRZJLX = sQRZJLX;
			modify_sQRZJLX = true;
		}
	}

	private String sQRZJH;
	private boolean modify_sQRZJH = false;

	public String getSQRZJH() {
		return sQRZJH;
	}

	public void setSQRZJH(String sQRZJH) {
		if (this.sQRZJH != sQRZJH) {
			this.sQRZJH = sQRZJH;
			modify_sQRZJH = true;
		}
	}

	private String bZ1;
	private boolean modify_bZ1 = false;

	public String getBZ1() {
		return bZ1;
	}

	public void setBZ1(String bZ1) {
		if (this.bZ1 != bZ1) {
			this.bZ1 = bZ1;
			modify_bZ1 = true;
		}
	}

	private String bZ2;
	private boolean modify_bZ2 = false;

	public String getBZ2() {
		return bZ2;
	}

	public void setBZ2(String bZ2) {
		if (this.bZ2 != bZ2) {
			this.bZ2 = bZ2;
			modify_bZ2 = true;
		}
	}

	private String bZ3;
	private boolean modify_bZ3 = false;

	public String getBZ3() {
		return bZ3;
	}

	public void setBZ3(String bZ3) {
		if (this.bZ3 != bZ3) {
			this.bZ3 = bZ3;
			modify_bZ3 = true;
		}
	}

	private String bZ4;
	private boolean modify_bZ4 = false;

	public String getBZ4() {
		return bZ4;
	}

	public void setBZ4(String bZ4) {
		if (this.bZ4 != bZ4) {
			this.bZ4 = bZ4;
			modify_bZ4 = true;
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

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_sQRXM = false;
		modify_sQRZJLX = false;
		modify_sQRZJH = false;
		modify_bZ1 = false;
		modify_bZ2 = false;
		modify_bZ3 = false;
		modify_bZ4 = false;
		modify_cREATETIME = false;
		modify_mODIFYTIME = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_sQRXM)
			listStrings.add("sQRXM");
		if (!modify_sQRZJLX)
			listStrings.add("sQRZJLX");
		if (!modify_sQRZJH)
			listStrings.add("sQRZJH");
		if (!modify_bZ1)
			listStrings.add("bZ1");
		if (!modify_bZ2)
			listStrings.add("bZ2");
		if (!modify_bZ3)
			listStrings.add("bZ3");
		if (!modify_bZ4)
			listStrings.add("bZ4");
		if (!modify_cREATETIME)
			listStrings.add("cREATETIME");
		if (!modify_mODIFYTIME)
			listStrings.add("mODIFYTIME");

		return StringHelper.ListToStrings(listStrings);
	}
}
