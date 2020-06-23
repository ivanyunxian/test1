package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2016/1/8 
//* ----------------------------------------
//* Internal Entity bdcs_xm_dyxz 
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

public class GenerateBDCS_XM_DYXZ implements SuperModel<String> {

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

	private String bDCDYID;
	private boolean modify_bDCDYID = false;

	public String getBDCDYID() {
		return bDCDYID;
	}

	public void setBDCDYID(String bDCDYID) {
		if (this.bDCDYID != bDCDYID) {
			this.bDCDYID = bDCDYID;
			modify_bDCDYID = true;
		}
	}

	private String bDCDYLX;
	private boolean modify_bDCDYLX = false;

	public String getBDCDYLX() {
		return bDCDYLX;
	}

	public void setBDCDYLX(String bDCDYLX) {
		if (this.bDCDYLX != bDCDYLX) {
			this.bDCDYLX = bDCDYLX;
			modify_bDCDYLX = true;
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

	private String dYXZID;
	private boolean modify_dYXZID = false;

	public String getDYXZID() {
		return dYXZID;
	}

	public void setDYXZID(String dYXZID) {
		if (this.dYXZID != dYXZID) {
			this.dYXZID = dYXZID;
			modify_dYXZID = true;
		}
	}

	private String zXBZ;
	private boolean modify_zXBZ = false;

	public String getZXBZ() {
		return zXBZ;
	}

	public void setZXBZ(String zXBZ) {
		if (this.zXBZ != zXBZ) {
			this.zXBZ = zXBZ;
			modify_zXBZ = true;
		}
	}

	private String zXYJ;
	private boolean modify_zXYJ = false;

	public String getZXYJ() {
		return zXYJ;
	}

	public void setZXYJ(String zXYJ) {
		if (this.zXYJ != zXYJ) {
			this.zXYJ = zXYJ;
			modify_zXYJ = true;
		}
	}

	private String zXXZWJHM;
	private boolean modify_zXXZWJHM = false;

	public String getZXXZWJHM() {
		return zXXZWJHM;
	}

	public void setZXXZWJHM(String zXXZWJHM) {
		if (this.zXXZWJHM != zXXZWJHM) {
			this.zXXZWJHM = zXXZWJHM;
			modify_zXXZWJHM = true;
		}
	}

	private String zXXZDW;
	private boolean modify_zXXZDW = false;

	public String getZXXZDW() {
		return zXXZDW;
	}

	public void setZXXZDW(String zXXZDW) {
		if (this.zXXZDW != zXXZDW) {
			this.zXXZDW = zXXZDW;
			modify_zXXZDW = true;
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
		modify_bDCDYID = false;
		modify_bDCDYLX = false;
		modify_xMBH = false;
		modify_dYXZID = false;
		modify_zXBZ = false;
		modify_zXYJ = false;
		modify_zXXZWJHM = false;
		modify_zXXZDW = false;
		modify_cREATETIME = false;
		modify_mODIFYTIME = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_bDCDYID)
			listStrings.add("bDCDYID");
		if (!modify_bDCDYLX)
			listStrings.add("bDCDYLX");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_dYXZID)
			listStrings.add("dYXZID");
		if (!modify_zXBZ)
			listStrings.add("zXBZ");
		if (!modify_zXYJ)
			listStrings.add("zXYJ");
		if (!modify_zXXZWJHM)
			listStrings.add("zXXZWJHM");
		if (!modify_zXXZDW)
			listStrings.add("zXXZDW");
		if (!modify_cREATETIME)
			listStrings.add("cREATETIME");
		if (!modify_mODIFYTIME)
			listStrings.add("mODIFYTIME");

		return StringHelper.ListToStrings(listStrings);
	}
}
