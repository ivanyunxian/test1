package com.supermap.realestate.registration.model.genrt;

///*****************************************
//* AutoGenerate by CodeTools 2015-6-23 
//* ----------------------------------------
//* Internal Entity bdcs_djgdfs 
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

public class GenerateBDCS_DJGDFS implements SuperModel<String> {

	private String id;
	private boolean modify_id = false;

	@Override
	public String getId() {
		if (!modify_id && id == null)
		{
			id = (String) SuperHelper.GeneratePrimaryKey();
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

	private String yWH;
	private boolean modify_yWH = false;

	public String getYWH() {
		return yWH;
	}

	public void setYWH(String yWH) {
		if (this.yWH != yWH) {
			this.yWH = yWH;
			modify_yWH = true;
		}
	}

	private String ySDM;
	private boolean modify_ySDM = false;

	public String getYSDM() {
		return ySDM;
	}

	public void setYSDM(String ySDM) {
		if (this.ySDM != ySDM) {
			this.ySDM = ySDM;
			modify_ySDM = true;
		}
	}

	private String bZ;
	private boolean modify_bZ = false;

	public String getBZ() {
		return bZ;
	}

	public void setBZ(String bZ) {
		if (this.bZ != bZ) {
			this.bZ = bZ;
			modify_bZ = true;
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

	private String cATALOG;
	private boolean modify_cATALOG = false;

	public String getCATALOG() {
		return cATALOG;
	}

	public void setCATALOG(String cATALOG) {
		if (this.cATALOG != cATALOG) {
			this.cATALOG = cATALOG;
			modify_cATALOG = true;
		}
	}

	private String fILENAME;
	private boolean modify_fILENAME = false;

	public String getFILENAME() {
		return fILENAME;
	}

	public void setFILENAME(String fILENAME) {
		if (this.fILENAME != fILENAME) {
			this.fILENAME = fILENAME;
			modify_fILENAME = true;
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

	private String sJRY;
	private boolean modify_sJRY = false;

	public String getSJRY() {
		return sJRY;
	}

	public void setSJRY(String sJRY) {
		if (this.sJRY != sJRY) {
			this.sJRY = sJRY;
			modify_sJRY = true;
		}
	}

	private Date sJSJ;
	private boolean modify_sJSJ = false;

	public Date getSJSJ() {
		return sJSJ;
	}

	public void setSJSJ(Date sJSJ) {
		if (this.sJSJ != sJSJ) {
			this.sJSJ = sJSJ;
			modify_sJSJ = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_yWH = false;
		modify_ySDM = false;
		modify_bZ = false;
		modify_xMBH = false;
		modify_cATALOG = false;
		modify_fILENAME = false;
		modify_cREATETIME = false;
		modify_mODIFYTIME = false;
		modify_sJRY = false;
		modify_sJSJ = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("id");
		if (!modify_yWH)
			listStrings.add("yWH");
		if (!modify_ySDM)
			listStrings.add("ySDM");
		if (!modify_bZ)
			listStrings.add("bZ");
		if (!modify_xMBH)
			listStrings.add("xMBH");
		if (!modify_cATALOG)
			listStrings.add("cATALOG");
		if (!modify_fILENAME)
			listStrings.add("fILENAME");
		if (!modify_cREATETIME)
			listStrings.add("cREATETIME");
		if (!modify_mODIFYTIME)
			listStrings.add("mODIFYTIME");
		if (!modify_sJRY)
			listStrings.add("sJRY");
		if (!modify_sJSJ)
			listStrings.add("sJSJ");

		return StringHelper.ListToStrings(listStrings);
	}
}
